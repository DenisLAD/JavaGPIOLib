/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package free.lucifer.chiplib.natives;

import com.sun.jna.Library;
import com.sun.jna.Pointer;

/**
 *
 * @author lucifer
 */
public interface CLibInterface extends Library {

    public int open(String path, int flags);

    public int ioctl(int fd, int cmd, int arg);

    public int close(int fd);

    public Pointer mmap(Pointer ptr, NativeSize size, int protect, int flags, int filedes, NativeSize offset);

    public int munmap(Pointer ptr, NativeSize size);

    public NativeSize write(int fd, byte[] buffer, NativeSize count);

    public NativeSize read(int fd, byte[] buffer, NativeSize count);

}
