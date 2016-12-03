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

package free.lucifer.chiplib.modules;

import free.lucifer.chiplib.Chip;

public class Battery implements Chip.PinListener {

    private float voltage = 0;

    public Battery() {
        Chip.I.pinMode(Chip.Pin.BAT, Chip.Pin.PinMode.INPUT);
        Chip.I.subscribePinChange(Chip.Pin.BAT, this);
    }

    @Override
    public void onPinChange(Chip.Pin pin, int old, int val) {
        voltage = (float) val * 1.1f / 1000f;
    }

    public float getVoltage() {
        return voltage;
    }

}
