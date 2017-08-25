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

import free.lucifer.chiplib.boards.GR8;
import free.lucifer.chiplib.boards.IOBoard;

public class ChipPro extends AbstractChip {

    public static final ChipPro I = new ChipPro();

    private ChipPro() {
        super(new GR8());
    }

    @Override
    protected Class getTargetPinMapping() {
        return GR8.ChipPin.class;
    }

    public static enum Pin {
        PWM0(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT, PinMode.PWM}, GR8.class),
        PWM1(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT, PinMode.PWM}, GR8.class),
        LRADC(new PinMode[]{PinMode.ANALOG}, GR8.class),
        CSIPCK(new PinMode[]{PinMode.INPUT}, GR8.class),
        CSIMCLK(new PinMode[]{PinMode.INPUT}, GR8.class),
        CSIHSYNC(new PinMode[]{PinMode.INPUT}, GR8.class),
        CSIVSYNC(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, GR8.class),
        CSID0(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, GR8.class),
        CSID1(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, GR8.class),
        CSID2(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, GR8.class),
        CSID3(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, GR8.class),
        CSID4(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, GR8.class),
        CSID5(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, GR8.class),
        CSID6(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, GR8.class),
        CSID7(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, GR8.class),
        TWI1_SCK(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, GR8.class),
        TWI1_SDA(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, GR8.class),
        UART2_TX(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, GR8.class),
        UART2_RX(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, GR8.class),
        UART2_CTS(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, GR8.class),
        UART2_RTS(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, GR8.class),
        UART1_TX(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, GR8.class),
        UART1_RX(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, GR8.class),
        I2S_MCLK(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, GR8.class),
        I2S_BLCK(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, GR8.class),
        I2S_LCLK(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, GR8.class),
        I2S_DO(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, GR8.class),
        I2S_DI(new PinMode[]{PinMode.INPUT, PinMode.OUTPUT}, GR8.class);

        public final PinMode[] modes;
        public PinMode mode;
        public int report;
        public int lastReport;
        public int analogChannel;
        public final Class<? extends IOBoard> manager;
        public IOBoard managerInstance;
        public Enum customPin;

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
