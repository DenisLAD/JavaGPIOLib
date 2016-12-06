/*
 * Copyright 2016 lucifer.
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
package free.lucifer.chiplib.natives;

import com.sun.jna.Structure;
import free.lucifer.chiplib.natives.spi.SpiIocTransfer;

/**
 *
 * @author lucifer
 */
public class SpiDev implements SpiDevInterface {

    public static final int IOC_NRBITS = 8;
    public static final int IOC_TYPEBITS = 8;
    public static final int IOC_SIZEBITS = 13;
    public static final int IOC_DIRBITS = 3;

    public static final int IOC_NRSHIFT = 0;
    public static final int IOC_TYPESHIFT = (IOC_NRSHIFT + IOC_NRBITS);
    public static final int IOC_SIZESHIFT = (IOC_TYPESHIFT + IOC_TYPEBITS);
    public static final int IOC_DIRSHIFT = (IOC_SIZESHIFT + IOC_SIZEBITS);

    public static final int IOC_NRMASK = (1 << IOC_NRBITS) - 1;
    public static final int IOC_TYPEMASK = (1 << IOC_TYPEBITS) - 1;
    public static final int IOC_SIZEMASK = (1 << IOC_SIZEBITS) - 1;
    public static final int IOC_XSIZEMASK = (1 << IOC_SIZEBITS + 1) - 1;
    public static final int IOC_DIRMASK = (1 << IOC_DIRBITS) - 1;

    public static final int IOC_NONE = 1;
    public static final int IOC_READ = 2;
    public static final int IOC_WRITE = 4;

    public static final int IOC_IN = IOC_WRITE << IOC_DIRSHIFT;
    public static final int IOC_OUT = IOC_READ << IOC_DIRSHIFT;
    public static final int IOC_INOUT = (IOC_WRITE | IOC_READ) << IOC_DIRSHIFT;
    public static final int IOCSIZE_MASK = (IOC_XSIZEMASK << IOC_SIZESHIFT);
    public static final int IOCSIZE_SHIFT = (IOC_SIZESHIFT);

    public static final int SPI_CPHA = 0x01;
    public static final int SPI_CPOL = 0x02;

    public static final int SPI_CS_HIGH = 0x04;
    public static final int SPI_LSB_FIRST = 0x08;
    public static final int SPI_3WIRE = 0x10;
    public static final int SPI_LOOP = 0x20;
    public static final int SPI_NO_CS = 0x40;
    public static final int SPI_READY = 0x80;
    public static final int SPI_TX_DUAL = 0x100;
    public static final int SPI_TX_QUAD = 0x200;
    public static final int SPI_RX_DUAL = 0x400;
    public static final int SPI_RX_QUAD = 0x800;

    public static final int SPI_IOC_MAGIC = 'k';

    public static final int SPI_IOC_RD_MODE = IOR(SPI_IOC_MAGIC, 1, 1);
    public static final int SPI_IOC_RW_MODE = IOW(SPI_IOC_MAGIC, 1, 1);

    public static final int SPI_IOC_RD_LSB_MODE = IOR(SPI_IOC_MAGIC, 2, 1);
    public static final int SPI_IOC_RW_LSB_MODE = IOW(SPI_IOC_MAGIC, 2, 1);

    public static final int SPI_IOC_RD_BITS_PER_WORD = IOR(SPI_IOC_MAGIC, 3, 1);
    public static final int SPI_IOC_RW_BITS_PER_WORD = IOW(SPI_IOC_MAGIC, 3, 1);

    public static final int SPI_IOC_RD_MAX_SPEED_HZ = IOR(SPI_IOC_MAGIC, 4, 4);
    public static final int SPI_IOC_RW_MAX_SPEED_HZ = IOW(SPI_IOC_MAGIC, 4, 4);

    public static final int SPI_IOC_RD_MODE32 = IOR(SPI_IOC_MAGIC, 5, 1);
    public static final int SPI_IOC_RW_MODE32 = IOW(SPI_IOC_MAGIC, 5, 1);

    public static final int SPI_IOC_SIZE = SpiIocTransfer.sizeOf();

    public static int SPI_MSGSIZE(int n) {
        return (((n * SPI_IOC_SIZE)) < (1 << IOC_SIZEBITS)) ? n * SPI_IOC_SIZE : 0;
    }

    public static int SPI_IOC_MESSAGE(int n) {
        return IOW(SPI_IOC_MAGIC, 0, SPI_MSGSIZE(n));
    }

    public static int IOR(int type, int nr, int size) {
        return IOC(IOC_READ, type, nr, size);
    }

    public static int IOW(int type, int nr, int size) {
        return IOC(IOC_WRITE, type, nr, size);
    }

    public static int IOWR(int type, int nr, int size) {
        return IOC(IOC_WRITE | IOC_READ, type, nr, size);
    }

    public static int IO(int type, int nr) {
        return IOC(IOC_NONE, type, nr, 0);
    }

    public static int IOC(int dir, int type, int nr, int size) {
        return (dir << IOC_DIRSHIFT) | (type << IOC_TYPESHIFT) | (nr << IOC_NRSHIFT) | (size << IOC_SIZESHIFT);
    }

    public static int IOC_DIR(int nr) {
        return (((nr >> IOC_DIRSHIFT) & IOC_DIRMASK) & (IOC_WRITE | IOC_READ)) != 0
                ? (nr >> IOC_DIRSHIFT) & (IOC_WRITE | IOC_READ)
                : (nr >> IOC_DIRSHIFT) & IOC_DIRMASK;
    }

    public static int IOC_TYPE(int nr) {
        return (nr >> IOC_TYPESHIFT) & IOC_TYPEMASK;
    }

    public static int IOC_NR(int nr) {
        return (nr >> IOC_NRSHIFT) & IOC_XSIZEMASK;
    }

    public static int IOC_SIZE(int nr) {
        return (((nr >> IOC_DIRSHIFT) & IOC_DIRMASK) & (IOC_WRITE | IOC_READ)) != 0
                ? 0
                : (nr >> IOC_DIRSHIFT) & IOC_XSIZEMASK;
    }

}
