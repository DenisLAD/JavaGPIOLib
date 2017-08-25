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

import free.lucifer.chiplib.AbstractChip;
import free.lucifer.chiplib.Chip;
import free.lucifer.chiplib.PinMode;
import free.lucifer.chiplib.modules.ssd1306.Commands;
import free.lucifer.chiplib.modules.ssd1306.Consts;
import free.lucifer.chiplib.modules.ssd1306.ControlType;
import free.lucifer.chiplib.modules.ssd1306.DisplayType;
import free.lucifer.chiplib.utils.Registry;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.Arrays;

public class SSD1306 {

    private final AbstractChip CHIP = Registry.INSTANCE.board();

    private final Chip.Pin data;
    private final Chip.Pin clk;
    private final Chip.Pin dc;
    private final Chip.Pin rst;
    private final Chip.Pin cs;

    private final ControlType control;
    private final DisplayType display;

    private final boolean externalVCC;

    private final byte[] backBuffer;

    private final int[] beginCommands = Arrays.copyOf(Consts.BASE_INIT, Consts.BASE_INIT.length);

    private final I2C i2c;
    private final SPI spi;

    public SSD1306(DisplayType displayType, boolean externalVCC, SPI spi, Chip.Pin dc, Chip.Pin rst, Chip.Pin cs) {
        this.data = null;
        this.clk = null;
        this.dc = dc;
        this.rst = rst;
        this.cs = cs;
        this.control = ControlType.HW_SPI;

        this.display = displayType;

        this.externalVCC = externalVCC;

        this.backBuffer = new byte[displayType.HEIGHT * displayType.WIDTH];

        this.i2c = null;
        this.spi = spi;

        prepareCommands();
    }

    public SSD1306(DisplayType displayType, boolean externalVCC, int i2cBus, int i2cAddr, Chip.Pin rst) {
        this.data = null;
        this.clk = null;
        this.dc = null;
        this.rst = rst;
        this.cs = null;
        this.control = ControlType.I2C;
        this.display = displayType;

        this.externalVCC = externalVCC;

        this.backBuffer = new byte[displayType.HEIGHT * displayType.WIDTH];

        this.i2c = new I2C(i2cBus, i2cAddr);
        this.spi = null;

        prepareCommands();
    }

    public SSD1306(DisplayType displayType, boolean externalVCC, I2C i2c, Chip.Pin rst) {
        this.data = null;
        this.clk = null;
        this.dc = null;
        this.rst = rst;
        this.cs = null;
        this.control = ControlType.I2C;
        this.display = displayType;

        this.externalVCC = externalVCC;

        this.backBuffer = new byte[displayType.HEIGHT * displayType.WIDTH];

        this.i2c = i2c;
        this.spi = null;

        prepareCommands();
    }

    public SSD1306(DisplayType displayType, boolean externalVCC, Chip.Pin data, Chip.Pin clk, Chip.Pin dc, Chip.Pin rst, Chip.Pin cs) {
        this.data = data;
        this.clk = clk;
        this.dc = dc;
        this.rst = rst;
        this.cs = cs;
        this.control = ControlType.SW_SPI;
        this.display = displayType;

        this.externalVCC = externalVCC;

        this.backBuffer = new byte[displayType.HEIGHT * displayType.WIDTH];

        this.i2c = null;
        this.spi = null;

        prepareCommands();
    }

    private void prepareCommands() {
        beginCommands[4] = display.HEIGHT - 1;
        beginCommands[9] = externalVCC ? 0x10 : 0x14;

        switch (display) {
            case SSD1306_128x64:
                beginCommands[15] = 0x12;
                beginCommands[17] = externalVCC ? 0x9F : 0xCF;
                break;
            case SSD1306_128x32:
                beginCommands[15] = 0x02;
                beginCommands[17] = 0x8F;
                break;
            case SSD1306_96x16:
                beginCommands[15] = 0x02;
                beginCommands[17] = externalVCC ? 0x10 : 0xAF;
                break;
        }

        beginCommands[19] = externalVCC ? 0x22 : 0xF1;

    }

    public void begin(boolean reset) {

        switch (control) {
            case SW_SPI:
                CHIP.pinMode(dc, PinMode.OUTPUT);
                CHIP.pinMode(cs, PinMode.OUTPUT);
                CHIP.pinMode(clk, PinMode.OUTPUT);
                CHIP.pinMode(data, PinMode.OUTPUT);
                break;
            case HW_SPI:
                CHIP.pinMode(dc, PinMode.OUTPUT);
                CHIP.pinMode(cs, PinMode.OUTPUT);
                spi.open();
                break;
            case I2C:
                i2c.open();
                break;
        }

        if (reset && rst != null) {
            CHIP.pinMode(rst, PinMode.OUTPUT);
            CHIP.digitalWrite(rst, 1);
            CHIP.delay(1);
            CHIP.digitalWrite(rst, 0);
            CHIP.delay(10);
            CHIP.digitalWrite(rst, 1);
        }

        for (int cmd : beginCommands) {
            command(cmd);
        }
    }

    public void invert(boolean negative) {
        if (negative) {
            command(Commands.SSD1306_INVERTDISPLAY);
        } else {
            command(Commands.SSD1306_NORMALDISPLAY);
        }
    }

    public void display(BufferedImage img) {
        BufferedImage tmp = new BufferedImage(display.HEIGHT, display.WIDTH, BufferedImage.TYPE_BYTE_BINARY);

        AffineTransform rotate = AffineTransform.getRotateInstance(Math.PI / 2d, display.HEIGHT / 2, display.WIDTH / 2);
        Graphics2D g = (Graphics2D) tmp.getGraphics();

        g.drawImage(img, rotate, null);

        img = tmp;

        byte[] b = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
        int c = 0;
        for (int i = 0; i < 8; i++) {
            for (int t = 0; t < 128; t++) {
                backBuffer[c] = b[(7 - i) + t * 8];
                c++;
            }
        }

        g.dispose();

        display();
    }

    public void display() {
        command(Commands.SSD1306_COLUMNADDR);
        command(0);
        command(127);
        command(Commands.SSD1306_PAGEADDR);
        command(0);
        command(7);
        CHIP.digitalWrite(cs, 1);
        CHIP.digitalWrite(dc, 1);
        CHIP.digitalWrite(cs, 0);

        for (int i = 0; i < 128 * 8; i++) {
            spiWrite(backBuffer[i]);
        }
        Chip.I.digitalWrite(cs, 1);
    }

    public void command(int cmd) {
        switch (control) {
            case SW_SPI:
            case HW_SPI:
                CHIP.digitalWrite(cs, 1);
                CHIP.digitalWrite(dc, 0);
                CHIP.digitalWrite(cs, 0);
                spiWrite(cmd);
                CHIP.digitalWrite(cs, 1);
                break;
            case I2C:
                i2c.write(new byte[]{0x00, (byte) cmd});
                break;
        }
    }

    public void spiWrite(int cmd) {

        switch (control) {
            case HW_SPI:
                spi.transfer(cmd);
                break;
            case SW_SPI:
                for (int i = 0x80; i != 0; i >>= 1) {
                    CHIP.digitalWrite(clk, 0);
                    if ((cmd & i) != 0) {
                        CHIP.digitalWrite(data, 1);
                    } else {
                        CHIP.digitalWrite(data, 0);
                    }
                    CHIP.digitalWrite(clk, 1);
                }
                break;
            default:
                break;
        }
    }
}
