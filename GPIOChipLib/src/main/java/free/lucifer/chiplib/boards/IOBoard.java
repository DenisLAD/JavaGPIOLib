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

package free.lucifer.chiplib.boards;

import free.lucifer.chiplib.Chip.Pin;


public interface IOBoard {

    public void open();

    public void close();

    public void pinMode(Pin pin, Pin.PinMode mode);

    public void pwmWrite(Pin pin, int value);

    public int analogRead(Pin pin);

    public void analogWrite(Pin pin, int value);

    public int digiatalRead(Pin pin);

    public void digitalWrite(Pin pin, int value);
}
