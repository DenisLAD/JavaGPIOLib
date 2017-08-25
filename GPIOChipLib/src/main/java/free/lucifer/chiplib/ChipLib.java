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
package free.lucifer.chiplib;

import free.lucifer.chiplib.modules.Task;

/**
 *
 * @author lucifer
 */
public class ChipLib {

    private static final Chip chip = Chip.I;

    public void subscribePinChange(Chip.Pin pin, Chip.PinListener listener) {
        chip.subscribePinChange(pin, listener);
    }

    public void unSubscribePinChange(Chip.Pin pin, Chip.PinListener listener) {
        chip.unSubscribePinChange(pin, listener);
    }

    public void addTask(Task task) {
        chip.addTask(task);
    }

    public void removeTask(Task task) {
        chip.removeTask(task);
    }

    public void open() {
        chip.open();
    }

    public void close() {
        chip.close();
    }

    public void pinMode(Enum pin, PinMode mode) {
        chip.pinMode(pin, mode);
    }

    public void pwmWrite(Enum pin, int value) {
        chip.pwmWrite(pin, value);
    }

    public int analogRead(Enum pin) {
        return chip.analogRead(pin);
    }

    public void analogWrite(Enum pin, int value) {
        chip.analogWrite(pin, value);
    }

    public int digiatalRead(Enum pin) {
        return chip.digiatalRead(pin);
    }

    public void digitalWrite(Enum pin, int value) {
        chip.digitalWrite(pin, value);
    }

    public void shiftOut(Enum dataPin, Enum clockPin, byte value) {
        chip.shiftOut(dataPin, clockPin, value);
    }

    public void shiftOut(Enum dataPin, Enum clockPin, byte value, boolean isBigEndian) {
        chip.shiftOut(dataPin, clockPin, value, isBigEndian);
    }

}
