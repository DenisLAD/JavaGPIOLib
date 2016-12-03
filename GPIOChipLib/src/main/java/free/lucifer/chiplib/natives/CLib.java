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

package free.lucifer.chiplib.natives;

import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;

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
