/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package free.lucifer.chiplib.modules;

import free.lucifer.chiplib.natives.CLib;
import free.lucifer.chiplib.natives.NativeSize;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author lucifer
 */
public class I2C {

    private final int bus;
    private final int address;
    private int fileDescriptor = -1;
    private ReentrantLock lock = new ReentrantLock();

    public I2C(int bus, int address) {
        this.bus = bus;
        this.address = address;
    }

    public void open() {
        if (fileDescriptor == -1) {
            fileDescriptor = CLib.INSTANCE.open(String.format("/dev/i2c-%d", bus), CLib.O_RDWR | CLib.O_SYNC);
            if (fileDescriptor != -1) {
                int res = CLib.INSTANCE.ioctl(fileDescriptor, 0x0706, address);
                if (res != 0) {
                    CLib.INSTANCE.close(fileDescriptor);
                    fileDescriptor = -1;
                }
            }
        }
    }

    public void close() {
        if (fileDescriptor != -1) {
            CLib.INSTANCE.close(fileDescriptor);
        }
    }

    public void write(byte[] value) {
        lock.lock();
        try {
            CLib.INSTANCE.write(fileDescriptor, value, new NativeSize(value.length));
        } finally {
            lock.unlock();
        }
    }

    public byte[] read(int size) {
        lock.lock();
        try {
            byte[] ret = new byte[size];
            CLib.INSTANCE.read(fileDescriptor, ret, new NativeSize(size));
            return ret;
        } finally {
            lock.unlock();
        }
    }

    public void writeRegister(byte register, byte[] value) {
        byte[] data = concatenate(new byte[]{register}, value);
        write(data);
    }

    public byte[] readRegister(byte register, int size) {
        write(new byte[]{register});
        return read(size);
    }

    public byte[] concatenate(byte[] a, byte[] b) {
        int aLen = a.length;
        int bLen = b.length;

        byte[] c = new byte[aLen + bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);

        return c;
    }
}
