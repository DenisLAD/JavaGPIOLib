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
package free.lucifer.chiplib.natives.datatypes;

import com.sun.jna.IntegerType;

/**
 *
 * @author lucifer
 */
public class uint64 extends IntegerType {

    public uint64() {
        super(8, true);
    }

    public uint64(long val) {
        super(8, val, true);
    }

}
