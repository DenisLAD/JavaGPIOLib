/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package free.lucifer.chiplib.natives;

import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;

/**
 *
 * @author lucifer
 */
public class CLib {

    public static final int O_RDWR = 0x00000002;
    public static final int O_NONBLOCK = 0x00000800;
    public static final int O_NOCTTY = 0x00000100;
    public static final int O_NDELAY = 0x00000800;
    public static final int O_SYNC = 0x00001000;
    public static final int F_GETFL = 0x00000003;
    public static final int F_SETFL = 0x00000004;

    static {
        Native.register(CLibNative.class, Platform.C_LIBRARY_NAME);
    }

    public final static CLib INSTANCE = new CLib();

    private final CLibNative nativeLib;

    private CLib() {
        nativeLib = new CLibNative();
    }

    public int ioctl(int fd, int cmd, int arg) {
        return nativeLib.ioctl(fd, cmd, arg);
    }

    public int open(String path, int flags) {
        return nativeLib.open(path, flags);
    }

    public int close(int fd) {
        return nativeLib.close(fd);
    }

    public NativeSize write(int fd, byte[] buffer, NativeSize count) {
        return nativeLib.write(fd, buffer, count);
    }

    public NativeSize read(int fd, byte[] buffer, NativeSize count) {
        return nativeLib.read(fd, buffer, count);
    }

    public Pointer mmap(Pointer ptr, NativeSize size, int protect, int flags, int filedes, NativeSize offset) {
        return nativeLib.mmap(ptr, size, protect, flags, filedes, offset);
    }

    public int munmap(Pointer ptr, NativeSize size) {
        return nativeLib.munmap(ptr, size);
    }

}
