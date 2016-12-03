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

import com.sun.jna.Pointer;

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
