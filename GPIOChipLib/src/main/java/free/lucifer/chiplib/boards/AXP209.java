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

import free.lucifer.chiplib.Chip;
import free.lucifer.chiplib.Chip.Pin;
import free.lucifer.chiplib.modules.I2C;
import java.util.concurrent.locks.ReentrantLock;


public class AXP209 implements IOBoard {

    private static final byte BUTTON_REGISTER = 0x4a;
    private static final byte INT_TEMP_MSB_REGISTER = 0x5e;
    private static final byte INT_TEMP_LSB_REGISTER = 0x5f;
    private static final byte BAT_VOLT_MSB_REGISTER = 0x78;
    private static final byte BAT_VOLT_LSB_REGISTER = 0x79;
    private static final byte BAT_ADC_REGISTER = (byte) 0x82;
    private static final byte GPIO2_REGISTER = (byte) 0x93;

    private ReentrantLock lock = new ReentrantLock();

    private I2C i2c;

    public AXP209(int bus, int address) {
        i2c = new I2C(bus, address);
    }

    @Override
    public void open() {
        i2c.open();
    }

    @Override
    public void close() {
        i2c.close();
    }

    @Override
    public void pinMode(Pin pin, Pin.PinMode mode) {
        if (pin == Pin.BAT) {
            configureBatAdc();
        }
    }

    @Override
    public void pwmWrite(Chip.Pin pin, int value) {

    }

    @Override
    public int analogRead(Chip.Pin pin) {
        if (pin == Pin.BAT) {
            return readBatVolts();
        } else if (pin == Pin.INTTEMP) {
            return readIntTemp();
        } else if (pin == Pin.BTN) {
            return readButton();
        } else {
            return -1;
        }
    }

    @Override
    public void analogWrite(Chip.Pin pin, int value) {
    }

    @Override
    public int digiatalRead(Chip.Pin pin) {
        return -1;
    }

    @Override
    public void digitalWrite(Chip.Pin pin, int value) {
        if (pin == Pin.STATUS) {
            writeGpio2(value);
        }
    }

    private int readBatVolts() {
        lock.lock();
        try {
            return readAdc(BAT_VOLT_MSB_REGISTER, BAT_VOLT_LSB_REGISTER);
        } finally {
            lock.unlock();
        }
    }

    private int readIntTemp() {
        lock.lock();
        try {
            return readAdc(INT_TEMP_MSB_REGISTER, INT_TEMP_LSB_REGISTER);
        } finally {
            lock.unlock();
        }
    }

    private int readButton() {
        lock.lock();
        try {
            boolean ret = (i2c.readRegister(BUTTON_REGISTER, 1)[0] & 0x02) != 0;
            if (ret) {
                i2c.writeRegister(BUTTON_REGISTER, new byte[]{2});
            }

            return ret ? 1 : 0;
        } finally {
            lock.unlock();
        }

    }

    private void configureBatAdc() {
        i2c.writeRegister(BAT_ADC_REGISTER, new byte[]{(byte) 0xc3});
    }

    private void writeGpio2(int value) {
        i2c.writeRegister(GPIO2_REGISTER, new byte[]{(byte) value});
    }

    private int readAdc(byte msbRegister, byte lsbRegister) {
        msbRegister = i2c.readRegister(msbRegister, 1)[0];
        lsbRegister = i2c.readRegister(lsbRegister, 1)[0];
        return ((msbRegister < 0 ? 256 + msbRegister : msbRegister) << 4) | (lsbRegister & 0xf);
    }

}
