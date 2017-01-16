/*
 * Copyright 2016 Denis Andreev.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package free.lucifer.chiplib.modules;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import free.lucifer.chiplib.natives.CLib;
import free.lucifer.chiplib.natives.SpiDev;
import free.lucifer.chiplib.natives.datatypes.NativeSize;
import free.lucifer.chiplib.natives.datatypes.uint16;
import free.lucifer.chiplib.natives.datatypes.uint32;
import free.lucifer.chiplib.natives.datatypes.uint64;
import free.lucifer.chiplib.natives.datatypes.uint8;
import free.lucifer.chiplib.natives.spi.SpiIocTransfer;

public class SPI {

    private CLib clib = CLib.INSTANCE;
    private final int bus;
    private final int device;
    private int fileDescriptor = -1;

    private int spiMode = 0;
    private int bitsPerWord = 0;
    private int maxSpeedHZ = 0;

    public SPI(int bus, int device) {
        this.bus = bus;
        this.device = device;
    }

    private boolean spiSetMode(int mode) {
        uint8 test = new uint8(0);
        uint8 m = new uint8(mode);
        if (clib.ioctl(fileDescriptor, mode, m) == -1) {
            return false;
        }

        if (clib.ioctl(fileDescriptor, mode, test) == -1) {
            return false;
        }

        if (test.intValue() != m.intValue()) {
            return false;
        }

        return true;
    }

    public boolean writeBytes(byte[] buff) {
        return clib.write(fileDescriptor, buff, new NativeSize(buff.length)).intValue() == -1;
    }

    public byte[] readBytes(int len) {
        byte[] data = new byte[len];
        clib.read(fileDescriptor, data, new NativeSize(len));
        return data;
    }

    public byte[] xfer(byte[] buff, int speedHZ, int delayUSec, int bitsPerWord) {
        int len = buff.length;

        long txAddr = Native.malloc(len);
        long rxAddr = Native.malloc(len);

        bitsPerWord = bitsPerWord == 0 ? this.bitsPerWord : bitsPerWord;
        speedHZ = speedHZ == 0 ? this.maxSpeedHZ : speedHZ;

        SpiIocTransfer sit = new SpiIocTransfer();
        sit.txBuff = new Pointer(txAddr);
        sit.rxBuff = new Pointer(rxAddr);

        sit.delayUSecs = new uint16(delayUSec);
        sit.bitsPerWord = new uint8(bitsPerWord);
        sit.speedHZ = new uint32(speedHZ);

        sit.csChange = new uint8(0);

        sit.txNBits = new uint8(0);
        sit.rxNBits = new uint8(0);

        sit.txBuff.write(0, buff, 0, len);

        int status = clib.ioctl(fileDescriptor, SpiDev.SPI_IOC_MESSAGE(1), sit);

        byte[] ret = new byte[len];

        if (status != -1) {
            sit.rxBuff.read(0, ret, 0, len);
        }

        if ((spiMode & SpiDev.SPI_CS_HIGH) != 0) {
            readBytes(0);
        }

        Native.free(rxAddr);
        Native.free(txAddr);

        return status == -1 ? null : ret;
    }

    public void open() {
        if (fileDescriptor == -1) {
            fileDescriptor = CLib.INSTANCE.open(String.format("/dev/spidev%d.%d", bus, device), CLib.O_RDWR);
            if (fileDescriptor != -1) {
                spiMode = read8(SpiDev.SPI_IOC_RD_MODE);
                bitsPerWord = read8(SpiDev.SPI_IOC_RD_BITS_PER_WORD);
                maxSpeedHZ = read32(SpiDev.SPI_IOC_RD_MAX_SPEED_HZ);
            }
        }
    }

    public int getFileDescriptor() {
        return fileDescriptor;
    }

    public int getMode() {
        return spiMode & (SpiDev.SPI_CPHA | SpiDev.SPI_CPOL);
    }

    public boolean getCsHigh() {
        return (spiMode & SpiDev.SPI_CS_HIGH) != 0;
    }

    public boolean getLsbFirst() {
        return (spiMode & SpiDev.SPI_LSB_FIRST) != 0;
    }

    public boolean get3Wire() {
        return (spiMode & SpiDev.SPI_3WIRE) != 0;
    }

    public boolean getLoop() {
        return (spiMode & SpiDev.SPI_LOOP) != 0;
    }

    public void setMode(int mode) {
        if (mode < 0 || mode > 3) {
            return;
        }

        int tmp = (this.spiMode & ~(SpiDev.SPI_CPHA | SpiDev.SPI_CPOL)) | mode;
        spiSetMode(tmp);
        this.spiMode = tmp;
    }

    public void setCsHigh(boolean isHigh) {
        int tmp;
        if (isHigh) {
            tmp = spiMode | SpiDev.SPI_CS_HIGH;
        } else {
            tmp = spiMode | ~SpiDev.SPI_CS_HIGH;
        }

        spiSetMode(tmp);

        spiMode = tmp;
    }

    public void setLsbFirst(boolean isLsbFirst) {
        int tmp;
        if (isLsbFirst) {
            tmp = spiMode | SpiDev.SPI_LSB_FIRST;
        } else {
            tmp = spiMode | ~SpiDev.SPI_LSB_FIRST;
        }

        spiSetMode(tmp);

        spiMode = tmp;
    }

    public void set3Wire(boolean is3Wire) {
        int tmp;
        if (is3Wire) {
            tmp = spiMode | SpiDev.SPI_3WIRE;
        } else {
            tmp = spiMode | ~SpiDev.SPI_3WIRE;
        }

        spiSetMode(tmp);

        spiMode = tmp;
    }

    public void setLoop(boolean isLoop) {
        int tmp;
        if (isLoop) {
            tmp = spiMode | SpiDev.SPI_LOOP;
        } else {
            tmp = spiMode | ~SpiDev.SPI_LOOP;
        }

        spiSetMode(tmp);

        spiMode = tmp;
    }

    public void setBitsPerWord(int bitsPerWord) {
        if (bitsPerWord < 8 || bitsPerWord > 16) {
            return;
        }
        if (this.bitsPerWord != bitsPerWord) {
            uint8 tmp = new uint8(bitsPerWord);
            if (clib.ioctl(fileDescriptor, SpiDev.SPI_IOC_RW_BITS_PER_WORD, tmp) == -1) {
                return;
            }
        }
        this.bitsPerWord = bitsPerWord;
    }

    public int getBitsPerWord() {
        return bitsPerWord;
    }

    public void setMaxSpeedHZ(int maxSpeedHZ) {
        if (this.maxSpeedHZ != maxSpeedHZ) {
            uint32 tmp = new uint32(maxSpeedHZ);
            if (clib.ioctl(fileDescriptor, SpiDev.SPI_IOC_RW_MAX_SPEED_HZ, tmp) == -1) {
                return;
            }
        }
        this.maxSpeedHZ = maxSpeedHZ;
    }

    public int getMaxSpeedHZ() {
        return maxSpeedHZ;
    }

    private int read8(int cmd) {
        uint8 tmp = new uint8(0);
        if (clib.ioctl(fileDescriptor, cmd, tmp) == -1) {
            return -1;
        }
        return tmp.byteValue();
    }

    private int read16(int cmd) {
        uint16 tmp = new uint16(0);
        if (clib.ioctl(fileDescriptor, cmd, tmp) == -1) {
            return -1;
        }
        return tmp.shortValue();
    }

    private int read32(int cmd) {
        uint32 tmp = new uint32(0);
        if (clib.ioctl(fileDescriptor, cmd, tmp) == -1) {
            return -1;
        }
        return tmp.intValue();
    }

    private long read64(int cmd) {
        uint64 tmp = new uint64(0);
        if (clib.ioctl(fileDescriptor, cmd, tmp) == -1) {
            return -1;
        }
        return tmp.longValue();
    }

    public void close() {
        if (fileDescriptor != -1) {
            clib.close(fileDescriptor);
            fileDescriptor = -1;
            spiMode = 0;
            maxSpeedHZ = 0;
            bitsPerWord = 0;
        }
    }

    public byte[] transfer(int cmd) {
        return xfer(new byte[]{(byte) cmd}, maxSpeedHZ, device, bitsPerWord);
    }
}
