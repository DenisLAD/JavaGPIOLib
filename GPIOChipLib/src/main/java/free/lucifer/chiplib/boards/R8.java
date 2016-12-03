/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package free.lucifer.chiplib.boards;

import com.sun.jna.Pointer;
import free.lucifer.chiplib.Chip;
import free.lucifer.chiplib.Chip.Pin;
import free.lucifer.chiplib.natives.CLib;
import free.lucifer.chiplib.natives.NativeSize;

/**
 *
 * @author lucifer
 */
public class R8 implements IOBoard {

    private static final int REGISTER_START = 0x01c20000;
    private static final int REGISTER_SIZE = 0x61000;

    private static final int PWM = 0x01C20C00;
    private static final int PWM_CTRL = PWM + 0x0200;
    private static final int PWM_CH0_PERIOD = PWM + 0x0204;

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
    public void pinMode(Chip.Pin pin, Chip.Pin.PinMode mode) {
        if (pin == Pin.LRADC) {
            int lrAdcCtrlVal = 0x01;
            writeRegister(LRADC_CTRL, lrAdcCtrlVal);
        } else {
            ChipPin p = pin.customPin;
            if (p != null) {
                int register = p.register;
                int index = p.index;

                if (mode == Pin.PinMode.PWM) {
                    mode = Pin.PinMode.ANALOG;
                }
                if (mode == Pin.PinMode.INPUT_PULLUP) {
                    mode = Pin.PinMode.INPUT;
                    int pullupReg = p.port.pull[register >> 2];
                    int pullupCfg = readRegister(pullupReg);

                    pullupCfg = (pullupCfg & ~(0x03 << (index * 2))) | (mode.id << (index * 2));
                    writeRegister(pullupReg, pullupCfg);
                } else {

                    int pullupReg = p.port.pull[register >> 2];
                    int pullupCfg = readRegister(pullupReg);

                    pullupCfg = (pullupCfg & ~(0x03 << (index * 2))) | (mode.id << (index * 2));
                    writeRegister(pullupReg, pullupCfg);
                }
                int cfgReg = p.port.cfg[register];
                int cfgVal = readRegister(cfgReg);

                cfgVal = (cfgVal & ~(0x07 << (index * 4))) | (mode.id << (index * 4));

                writeRegister(cfgReg, cfgVal);

            }
        }
        if (pin == Pin.PWM0 && mode == Pin.PinMode.ANALOG) {
            int pwmCtrlVal = (1 << 6) | (1 << 5) | (1 << 4);
            int pwmPeriodVal = (0xff << 16) | 0;
            writeRegister(PWM_CH0_PERIOD, pwmPeriodVal);
            writeRegister(PWM_CTRL, pwmCtrlVal);
        }
    }

    @Override
    public void pwmWrite(Chip.Pin pin, int value) {
        if (pin == Pin.PWM0) {
            int pwmPeriodVal = (0xff << 16) | Math.round(value);
            writeRegister(PWM_CH0_PERIOD, pwmPeriodVal);
        }
    }

    @Override
    public int analogRead(Chip.Pin pin) {
        if (pin == Pin.LRADC) {
            return readRegister(LRADC_DATA0);
        }

        return -1;
    }

    @Override
    public void analogWrite(Chip.Pin pin, int value) {
        pwmWrite(pin, value);
    }

    @Override
    public int digiatalRead(Chip.Pin pin) {
        if (pin != Pin.LRADC) {
            ChipPin p = pin.customPin;
            if (p != null) {
//                int dataVal = readRegister(p.port.data);
//                int pinMask = 1 << (p.register * 8 + p.index);
                return ((readRegister(p.port.data) & p.shift) != 0) ? 1 : 0;
            }
        }
        return -1;
    }

    @Override
    public void digitalWrite(Chip.Pin pin, int value) {
        ChipPin p = pin.customPin;
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
        LCD_D2(ChipPort.D, 0, 2),
        LCD_D3(ChipPort.D, 0, 3),
        LCD_D4(ChipPort.D, 0, 4),
        LCD_D5(ChipPort.D, 0, 5),
        LCD_D6(ChipPort.D, 0, 6),
        LCD_D7(ChipPort.D, 0, 7),
        LCD_D10(ChipPort.D, 1, 2),
        LCD_D11(ChipPort.D, 1, 3),
        LCD_D12(ChipPort.D, 1, 4),
        LCD_D13(ChipPort.D, 1, 5),
        LCD_D14(ChipPort.D, 1, 6),
        LCD_D15(ChipPort.D, 1, 7),
        LCD_D18(ChipPort.D, 2, 2),
        LCD_D19(ChipPort.D, 2, 3),
        LCD_D20(ChipPort.D, 2, 4),
        LCD_D21(ChipPort.D, 2, 5),
        LCD_D22(ChipPort.D, 2, 6),
        LCD_D23(ChipPort.D, 2, 7),
        LCD_CLK(ChipPort.D, 3, 0),
        LCD_DE(ChipPort.D, 3, 1),
        LCD_HSYNC(ChipPort.D, 3, 2),
        LCD_VSYNC(ChipPort.D, 3, 3),
        CSIPCK(ChipPort.E, 0, 0),
        CSICK(ChipPort.E, 0, 1),
        CSIHSYNC(ChipPort.E, 0, 2),
        CSIVSYNC(ChipPort.E, 0, 3),
        CSID0(ChipPort.E, 0, 4),
        CSID1(ChipPort.E, 0, 5),
        CSID2(ChipPort.E, 0, 6),
        CSID3(ChipPort.E, 0, 7),
        CSID4(ChipPort.E, 1, 0),
        CSID5(ChipPort.E, 1, 1),
        CSID6(ChipPort.E, 1, 2),
        CSID7(ChipPort.E, 1, 3),
        LRADC(ChipPort.NONE, 0, 0);

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

        private static enum ChipPort {
            NONE(null, 0, null, null),
            B(new int[]{PIO + 0x24, PIO + 0x28, PIO + 0x2c, PIO + 0x30}, PIO + 0x34, new int[]{PIO + 0x38, PIO + 0x3c}, new int[]{PIO + 0x40, PIO + 0x44}),
            C(new int[]{PIO + 0x48, PIO + 0x4c, PIO + 0x50, PIO + 0x54}, PIO + 0x58, new int[]{PIO + 0x5c, PIO + 0x60}, new int[]{PIO + 0x64, PIO + 0x68}),
            D(new int[]{PIO + 0x6c, PIO + 0x70, PIO + 0x74, PIO + 0x78}, PIO + 0x7c, new int[]{PIO + 0x80, PIO + 0x84}, new int[]{PIO + 0x88, PIO + 0x8c}),
            E(new int[]{PIO + 0x90, PIO + 0x94, PIO + 0x98, PIO + 0x9c}, PIO + 0xa0, new int[]{PIO + 0xa4, PIO + 0xa8}, new int[]{PIO + 0xac, PIO + 0xb0}),
            F(new int[]{PIO + 0xb4, PIO + 0xb8, PIO + 0xbc, PIO + 0xc0}, PIO + 0xc4, new int[]{PIO + 0xc8, PIO + 0xcc}, new int[]{PIO + 0xd0, PIO + 0xd4});

            public final int[] cfg;
            public final int data;
            public final int[] driver;
            public final int[] pull;

            private ChipPort(int[] cfg, int data, int[] driver, int[] pull) {
                this.cfg = cfg;
                this.data = data;
                this.driver = driver;
                this.pull = pull;
            }
        }
    }
}
