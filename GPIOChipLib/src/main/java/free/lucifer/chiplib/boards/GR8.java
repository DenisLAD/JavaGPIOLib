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

import com.sun.jna.Pointer;
import free.lucifer.chiplib.ChipPro;
import free.lucifer.chiplib.ChipPro.Pin;
import free.lucifer.chiplib.PinMode;
import free.lucifer.chiplib.boards.R8.ChipPin.ChipPort;
import free.lucifer.chiplib.natives.CLib;
import free.lucifer.chiplib.natives.datatypes.NativeSize;

public class GR8 implements IOBoard {

    private static final int REGISTER_START = 0x01c20000;
    private static final int REGISTER_SIZE = 0x61000;

    private static final int PWM = 0x01C20C00;
    private static final int PWM0_CTRL = PWM + 0x0200;
    private static final int PWM0_CH0_PERIOD = PWM + 0x0204;
    private static final int PWM1_CTRL = PWM + 0x0200;
    private static final int PWM1_CH0_PERIOD = PWM + 0x0204;

    private static final int LRADC = 0x01C22800;
    private static final int LRADC_CTRL = LRADC + 0x00;
    private static final int LRADC_DATA0 = LRADC + 0x0c;

    private int memFile = -1;
    private Pointer registers = null;

    @Override
    public void open() {
        try {
            if (memFile == -1) {
                memFile = CLib.INSTANCE.open("/dev/mem", CLib.O_RDWR | CLib.O_SYNC);
                if (memFile == -1) {
                    System.out.println("Cannot access to /dev/mem");
                } else if (registers == null) {
                    registers = CLib.INSTANCE.mmap(null, new NativeSize(REGISTER_SIZE), 3, 1, memFile, new NativeSize(REGISTER_START));
                    if (registers.equals(Pointer.NULL)) {
                        System.out.println("Cannot map file /dev/mem");
                    }
                } else {

                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        try {
            if (registers != null) {
                CLib.INSTANCE.munmap(registers, new NativeSize(REGISTER_SIZE));
                registers = null;
            }

            if (memFile != -1) {
                CLib.INSTANCE.close(memFile);
                memFile = -1;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void pinMode(Enum pin, Enum mode) {
        if (pin == ChipPro.Pin.LRADC) {
            int lrAdcCtrlVal = 0x01;
            writeRegister(LRADC_CTRL, lrAdcCtrlVal);
        } else {
            ChipPin p = (ChipPin) ((ChipPro.Pin) pin).customPin;
            if (p != null) {
                int register = p.register;
                int index = p.index;

                if (mode == PinMode.PWM) {
                    mode = PinMode.ANALOG;
                }
                if (mode == PinMode.INPUT_PULLUP) {
                    mode = PinMode.INPUT;
                    int pullupReg = p.port.pull[register >> 2];
                    int pullupCfg = readRegister(pullupReg);

                    pullupCfg = (pullupCfg & ~(0x03 << (index * 2))) | (((PinMode) mode).id << (index * 2));
                    writeRegister(pullupReg, pullupCfg);
                } else {

                    int pullupReg = p.port.pull[register >> 2];
                    int pullupCfg = readRegister(pullupReg);

                    pullupCfg = (pullupCfg & ~(0x03 << (index * 2))) | (((PinMode) mode).id << (index * 2));
                    writeRegister(pullupReg, pullupCfg);
                }
                int cfgReg = p.port.cfg[register];
                int cfgVal = readRegister(cfgReg);

                cfgVal = (cfgVal & ~(0x07 << (index * 4))) | (((PinMode) mode).id << (index * 4));

                writeRegister(cfgReg, cfgVal);

            }
        }
        if (pin == Pin.PWM0 && mode == PinMode.ANALOG) {
            int pwmCtrlVal = (1 << 6) | (1 << 5) | (1 << 4);
            int pwmPeriodVal = (0xff << 16) | 0;
            writeRegister(PWM0_CH0_PERIOD, pwmPeriodVal);
            writeRegister(PWM0_CTRL, pwmCtrlVal);
        }
        if (pin == Pin.PWM1 && mode == PinMode.ANALOG) {
            int pwmCtrlVal = (1 << 6) | (1 << 5) | (1 << 4);
            int pwmPeriodVal = (0xff << 16) | 0;
            writeRegister(PWM1_CH0_PERIOD, pwmPeriodVal);
            writeRegister(PWM1_CTRL, pwmCtrlVal);
        }
    }

    @Override
    public void pwmWrite(Enum pin, int value) {
        if (pin == Pin.PWM0) {
            int pwmPeriodVal = (0xff << 16) | Math.round(value);
            writeRegister(PWM0_CH0_PERIOD, pwmPeriodVal);
        }
        if (pin == Pin.PWM1) {
            int pwmPeriodVal = (0xff << 16) | Math.round(value);
            writeRegister(PWM1_CH0_PERIOD, pwmPeriodVal);
        }
    }

    @Override
    public int analogRead(Enum pin) {
        if (pin == Pin.LRADC) {
            return readRegister(LRADC_DATA0);
        }

        return -1;
    }

    @Override
    public void analogWrite(Enum pin, int value) {
        pwmWrite(pin, value);
    }

    @Override
    public int digiatalRead(Enum pin) {
        if (pin != Pin.LRADC) {
            ChipPin p = (ChipPin) ((ChipPro.Pin) pin).customPin;
            if (p != null) {
//                int dataVal = readRegister(p.port.data);
//                int pinMask = 1 << (p.register * 8 + p.index);
                return ((readRegister(p.port.data) & p.shift) != 0) ? 1 : 0;
            }
        }
        return -1;
    }

    @Override
    public void digitalWrite(Enum pin, int value) {
        ChipPin p = (ChipPin) ((ChipPro.Pin) pin).customPin;
        if (p != null) {
            int dataVal = readRegister(p.port.data);
            int pinMask = p.shift;

            if (value != 0) {
                dataVal |= pinMask;
            } else {
                dataVal &= ~pinMask;
            }

            writeRegister(p.port.data, dataVal);
        }
    }

    protected void writeRegister(int register, int value) {
        registers.setInt(register - REGISTER_START, value);
    }

    private int readRegister(int register) {
        return registers.getInt(register - REGISTER_START);
    }

    public static enum ChipPin {
        PWM0(ChipPort.B, 0, 2),
        PWM1(ChipPort.G, 1, 5),
        TWI1_SCK(ChipPort.B, 1, 7),
        TWI1_SDA(ChipPort.B, 2, 0),
        UART2_TX(ChipPort.D, 0, 2),
        UART2_RX(ChipPort.D, 0, 3),
        UART2_CTS(ChipPort.D, 0, 4),
        UART2_RTS(ChipPort.D, 0, 5),
        CSIPCK(ChipPort.E, 3, 0),
        CSIMCLK(ChipPort.E, 3, 1),
        CSIHSYNC(ChipPort.E, 3, 2),
        CSIVSYNC(ChipPort.E, 3, 3),
        CSID0(ChipPort.E, 0, 4),
        CSID1(ChipPort.E, 0, 5),
        CSID2(ChipPort.E, 0, 6),
        CSID3(ChipPort.E, 0, 7),
        CSID4(ChipPort.E, 1, 0),
        CSID5(ChipPort.E, 1, 1),
        CSID6(ChipPort.E, 1, 2),
        CSID7(ChipPort.E, 1, 3),
        UART1_TX(ChipPort.G, 0, 3),
        UART1_RX(ChipPort.G, 0, 4),
        I2S_MCLK(ChipPort.B, 0, 5),
        I2S_BLCK(ChipPort.B, 0, 6),
        I2S_LCLK(ChipPort.B, 0, 7),
        I2S_DO(ChipPort.B, 1, 0),
        I2S_DI(ChipPort.B, 1, 1);

        public final ChipPort port;
        public final int register;
        public final int index;
        public final int shift;

        private ChipPin(ChipPort port, int register, int index) {
            this.port = port;
            this.register = register;
            this.index = index;
            this.shift = 1 << (register * 8 + index);
        }

        private static final int PIO = 0x01C20800;

    }
}
