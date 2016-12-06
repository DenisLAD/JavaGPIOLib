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

import free.lucifer.chiplib.modules.spi.SPIClockDivider;
import free.lucifer.chiplib.natives.CLib;
import free.lucifer.chiplib.natives.SpiDev;
import free.lucifer.chiplib.natives.datatypes.uint16;
import free.lucifer.chiplib.natives.datatypes.uint32;
import free.lucifer.chiplib.natives.datatypes.uint64;
import free.lucifer.chiplib.natives.datatypes.uint8;

public class SPI {

    private CLib clib = CLib.INSTANCE;
    private final int bus;
    private final int device;
    private int fileDescriptor = -1;
    private int mode = -1;
    private int bitsPerWord = -1;
    private int maxSpeedHZ = -1;

    public SPI(int bus, int device) {
        this.bus = bus;
        this.device = device;
    }

    public void open() {
        if (fileDescriptor == -1) {
            fileDescriptor = CLib.INSTANCE.open(String.format("/dev/spidev%d.%d", bus, device), CLib.O_RDWR);
            if (fileDescriptor != -1) {
                mode = read8(SpiDev.SPI_IOC_RD_MODE);
                bitsPerWord = read8(SpiDev.SPI_IOC_RD_BITS_PER_WORD);
                maxSpeedHZ = read32(SpiDev.SPI_IOC_RD_MAX_SPEED_HZ);
            }
        }
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
        }
    }

    public void begin() {

    }

    public void transfer(int cmd) {

    }

    public void setClockDivider(SPIClockDivider spiClockDivider) {

    }

}
