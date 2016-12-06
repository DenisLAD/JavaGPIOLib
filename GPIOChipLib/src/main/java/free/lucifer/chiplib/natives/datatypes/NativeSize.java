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

package free.lucifer.chiplib.natives.datatypes;

import com.sun.jna.IntegerType;
import com.sun.jna.Native;

public class NativeSize extends IntegerType {

    private static final long serialVersionUID = 2398288011955445078L;

    public static int SIZE = Native.SIZE_T_SIZE;//Platform.is64Bit() ? 8 : 4;

    public NativeSize() {
        this(0);
    }

    public NativeSize(long value) {
        super(SIZE, value);
    }
}
