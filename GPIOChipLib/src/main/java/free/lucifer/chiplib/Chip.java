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
package free.lucifer.chiplib;

import free.lucifer.chiplib.boards.AXP209;
import free.lucifer.chiplib.boards.IOBoard;
import free.lucifer.chiplib.boards.PCF8574A;
import free.lucifer.chiplib.boards.R8;

public class Chip extends AbstractChip {

    public static final Chip I = new Chip();

    private Chip() {
        super(new R8(), new AXP209(0, 0x34), new PCF8574A(2, 0x38));
    }

    @Override
    protected Class<?> getTargetPinMapping() {
        return R8.ChipPin.class;
    }

    public static enum Pin {
        PWM0(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT, PinMode.PWM}, R8.class),
        LRADC(new PinMode[]{PinMode.ANALOG}, R8.class),
        LCD_D2(new PinMode[]{PinMode.INPUT}, R8.class),
        LCD_D3(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, R8.class),
        LCD_D4(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, R8.class),
        LCD_D5(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, R8.class),
        LCD_D6(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, R8.class),
        LCD_D7(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, R8.class),
        LCD_D10(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, R8.class),
        LCD_D11(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, R8.class),
        LCD_D12(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, R8.class),
        LCD_D13(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, R8.class),
        LCD_D14(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, R8.class),
        LCD_D15(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, R8.class),
        LCD_D18(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, R8.class),
        LCD_D19(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, R8.class),
        LCD_D20(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, R8.class),
        LCD_D21(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, R8.class),
        LCD_D22(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, R8.class),
        LCD_D23(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, R8.class),
        LCD_CLK(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, R8.class),
        LCD_VSYNC(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, R8.class),
        LCD_HSYNC(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, R8.class),
        LCD_DE(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, R8.class),
        XIO_P0(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, PCF8574A.class),
        XIO_P1(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, PCF8574A.class),
        XIO_P2(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, PCF8574A.class),
        XIO_P3(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, PCF8574A.class),
        XIO_P4(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, PCF8574A.class),
        XIO_P5(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, PCF8574A.class),
        XIO_P6(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, PCF8574A.class),
        XIO_P7(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, PCF8574A.class),
        CSIPCK(new PinMode[]{PinMode.INPUT}, R8.class),
        CSICK(new PinMode[]{PinMode.INPUT}, R8.class),
        CSIHSYNC(new PinMode[]{PinMode.INPUT}, R8.class),
        CSIVSYNC(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, R8.class),
        CSID0(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, R8.class),
        CSID1(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, R8.class),
        CSID2(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, R8.class),
        CSID3(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, R8.class),
        CSID4(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, R8.class),
        CSID5(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, R8.class),
        CSID6(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, R8.class),
        CSID7(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, R8.class),
        STATUS(new PinMode[]{PinMode.OUTPUT}, PinMode.OUTPUT, 0, 127, AXP209.class),
        BAT(new PinMode[]{PinMode.ANALOG}, PinMode.INPUT, 0, 127, AXP209.class),
        INTTEMP(new PinMode[]{PinMode.ANALOG}, PinMode.INPUT, 0, 127, AXP209.class),
        BTN(new PinMode[]{PinMode.INPUT}, PinMode.INPUT, 0, 127, AXP209.class);

        public final PinMode[] modes;
        public PinMode mode;
        public int report;
        public int lastReport;
        public int analogChannel;
        public final Class<? extends IOBoard> manager;
        public IOBoard managerInstance;
        public R8.ChipPin customPin;

        private Pin() {
            this.modes = new PinMode[]{};
            this.manager = null;
            this.report = -1;
            this.lastReport = -1;
            this.mode = PinMode.NONE;
            this.analogChannel = 127;
        }

        private Pin(PinMode[] modes, Class<? extends IOBoard> manager) {
            this.modes = modes;
            this.mode = PinMode.NONE;
            this.report = -1;
            this.lastReport = -1;
            this.analogChannel = 127;
            this.manager = manager;
        }

        private Pin(PinMode[] modes, PinMode mode, int report, int analogChannel, Class<? extends IOBoard> manager) {
            this.modes = modes;
            this.mode = mode;
            this.report = -1;
            this.lastReport = -1;
            this.analogChannel = analogChannel;
            this.manager = manager;
        }

        private Pin(PinMode[] modes, PinMode mode, int report, int analogChannel) {
            this.modes = modes;
            this.mode = mode;
            this.report = -1;
            this.lastReport = -1;
            this.analogChannel = analogChannel;
            this.manager = null;
        }

        public PinMode getMode() {
            return mode;
        }

        public void setMode(PinMode mode) {
            this.mode = mode;
        }

        public int getReport() {
            return report;
        }

        public void setReport(int report) {
            this.report = report;
        }

        public int getLastReport() {
            return lastReport;
        }

        public void setLastReport(int lastReport) {
            this.lastReport = lastReport;
        }

        public int getAnalogChannel() {
            return analogChannel;
        }

        public void setAnalogChannel(int analogChannel) {
            this.analogChannel = analogChannel;
        }
    }
}
