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
package free.lucifer.chiplib.natives.spi;

import com.sun.jna.Structure;
import free.lucifer.chiplib.natives.datatypes.uint16;
import free.lucifer.chiplib.natives.datatypes.uint32;
import free.lucifer.chiplib.natives.datatypes.uint64;
import free.lucifer.chiplib.natives.datatypes.uint8;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lucifer
 */
public class SpiIocTransfer extends Structure {

    private final static List fields = new ArrayList() {
        {
            add("txBuff");
            add("rxBuff");
            add("len");
            add("speedHZ");
            add("delayUSecs");
            add("bitsPerWord");
            add("csChange");
            add("txNBits");
            add("rxNBits");
            add("pad");
        }
    };

    public uint64 txBuff;
    public uint64 rxBuff;
    public uint32 len;
    public uint32 speedHZ;
    public uint16 delayUSecs;
    public uint8 bitsPerWord;
    public uint8 csChange;
    public uint8 txNBits;
    public uint8 rxNBits;
    public uint16 pad;

    @Override
    protected List getFieldOrder() {
        return fields;
    }

    public static int sizeOf() {
        return Structure.newInstance(SpiIocTransfer.class).size();
    }

    public static void main(String[] args) {
        System.out.println(SpiIocTransfer.sizeOf());
    }

}
