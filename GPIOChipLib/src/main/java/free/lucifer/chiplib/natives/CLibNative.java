/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package free.lucifer.chiplib.natives;

import com.sun.jna.Pointer;

/**
 *
 * @author lucifer
 */
public class CLibNative implements CLibInterface {

    @Override
    native public int ioctl(int fd, int cmd, int arg);

    @Override
    native public int open(String path, int flags);

    @Override
    native public int close(int fd);

    @Override
    native public NativeSize write(int fd, byte[] buffer, NativeSize count);

    @Override
    native public NativeSize read(int fd, byte[] buffer, NativeSize count);

    @Override
    native public Pointer mmap(Pointer ptr, NativeSize size, int protect, int flags, int filedes, NativeSize offset);

    @Override
    native public int munmap(Pointer ptr, NativeSize size);
}
