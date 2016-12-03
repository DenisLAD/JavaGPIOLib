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
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class SSD1306 {

    public final static int SSD1306_SETCONTRAST = 0x81;
    public final static int SSD1306_DISPLAYALLON_RESUME = 0xA4;
    public final static int SSD1306_DISPLAYALLON = 0xA5;
    public final static int SSD1306_NORMALDISPLAY = 0xA6;
    public final static int SSD1306_INVERTDISPLAY = 0xA7;
    public final static int SSD1306_DISPLAYOFF = 0xAE;
    public final static int SSD1306_DISPLAYON = 0xAF;

    public final static int SSD1306_SETDISPLAYOFFSET = 0xD3;
    public final static int SSD1306_SETCOMPINS = 0xDA;

    public final static int SSD1306_SETVCOMDETECT = 0xDB;

    public final static int SSD1306_SETDISPLAYCLOCKDIV = 0xD5;
    public final static int SSD1306_SETPRECHARGE = 0xD9;

    public final static int SSD1306_SETMULTIPLEX = 0xA8;

    public final static int SSD1306_SETLOWCOLUMN = 0x00;
    public final static int SSD1306_SETHIGHCOLUMN = 0x10;

    public final static int SSD1306_SETSTARTLINE = 0x40;

    public final static int SSD1306_MEMORYMODE = 0x20;
    public final static int SSD1306_COLUMNADDR = 0x21;
    public final static int SSD1306_PAGEADDR = 0x22;

    public final static int SSD1306_COMSCANINC = 0xC0;
    public final static int SSD1306_COMSCANDEC = 0xC8;

    public final static int SSD1306_SEGREMAP = 0xA0;

    public final static int SSD1306_CHARGEPUMP = 0x8D;

    public final static int SSD1306_EXTERNALVCC = 0x1;
    public final static int SSD1306_SWITCHCAPVCC = 0x2;

    public final static int SSD1306_ACTIVATE_SCROLL = 0x2F;
    public final static int SSD1306_DEACTIVATE_SCROLL = 0x2E;
    public final static int SSD1306_SET_VERTICAL_SCROLL_AREA = 0xA3;
    public final static int SSD1306_RIGHT_HORIZONTAL_SCROLL = 0x26;
    public final static int SSD1306_LEFT_HORIZONTAL_SCROLL = 0x27;
    public final static int SSD1306_VERTICAL_AND_RIGHT_HORIZONTAL_SCROLL = 0x29;
    public final static int SSD1306_VERTICAL_AND_LEFT_HORIZONTAL_SCROLL = 0x2A;

    private final Chip.Pin data;
    private final Chip.Pin clk;
    private final Chip.Pin dc;
    private final Chip.Pin rst;
    private final Chip.Pin cs;
    private final byte[] backBuffer = new byte[128 * 64];

    public SSD1306(Chip.Pin data, Chip.Pin clk, Chip.Pin dc, Chip.Pin rst, Chip.Pin cs) {
        this.data = data;
        this.clk = clk;
        this.dc = dc;
        this.rst = rst;
        this.cs = cs;
    }

    public void begin() {
        Chip.I.pinMode(dc, Chip.Pin.PinMode.OUTPUT);
        Chip.I.pinMode(cs, Chip.Pin.PinMode.OUTPUT);
        Chip.I.pinMode(clk, Chip.Pin.PinMode.OUTPUT);
        Chip.I.pinMode(data, Chip.Pin.PinMode.OUTPUT);
        Chip.I.pinMode(rst, Chip.Pin.PinMode.OUTPUT);

        Chip.I.digitalWrite(rst, 1);
        delay(1);
        Chip.I.digitalWrite(rst, 0);
        delay(10);
        Chip.I.digitalWrite(rst, 1);

        command(SSD1306_DISPLAYOFF);
        command(SSD1306_SETDISPLAYCLOCKDIV);
        command(0x80);
        command(SSD1306_SETMULTIPLEX);
        command(63);
        command(SSD1306_SETDISPLAYOFFSET);
        command(0x00);
        command(SSD1306_SETSTARTLINE);
        command(SSD1306_CHARGEPUMP);
        command(0x14);
        command(SSD1306_MEMORYMODE);
        command(0x00);
        command(SSD1306_SEGREMAP | 0x01);
        command(SSD1306_COMSCANDEC);
        command(SSD1306_SETCOMPINS);
        command(0x12);
        command(SSD1306_SETCONTRAST);
        command(0xcf);
        command(SSD1306_SETPRECHARGE);
        command(0xf1);
        command(SSD1306_SETVCOMDETECT);
        command(0x40);
        command(SSD1306_DISPLAYALLON_RESUME);
        command(SSD1306_NORMALDISPLAY);
        command(SSD1306_DEACTIVATE_SCROLL);
        command(SSD1306_DISPLAYON);
    }

    public void invert(boolean negative) {
        if (negative) {
            command(SSD1306_INVERTDISPLAY);
        } else {
            command(SSD1306_NORMALDISPLAY);
        }
    }

    public void display(BufferedImage img) {
        byte[] b = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
        int c = 0;
        for (int i = 0; i < 8; i++) {
            for (int t = 0; t < 128; t++) {
                backBuffer[c] = b[(7 - i) + t * 8];
                c++;
            }
        }
        display();
    }

    public void display() {
        command(SSD1306_COLUMNADDR);
        command(0);
        command(127);
        command(SSD1306_PAGEADDR);
        command(0);
        command(7);
        Chip.I.digitalWrite(cs, 1);
        Chip.I.digitalWrite(dc, 1);
        Chip.I.digitalWrite(cs, 0);

        for (int i = 0; i < 128 * 8; i++) {
            spiWrite(backBuffer[i]); //b[(7 - i) + t * 8]);
        }
        Chip.I.digitalWrite(cs, 1);
    }

    public void command(int cmd) {
        Chip.I.digitalWrite(cs, 1);
        Chip.I.digitalWrite(dc, 0);
        Chip.I.digitalWrite(cs, 0);
        spiWrite(cmd);
        Chip.I.digitalWrite(cs, 1);

    }

    public void spiWrite(int cmd) {
        for (int i = 0x80; i != 0; i >>= 1) {
            Chip.I.digitalWrite(clk, 0);
            if ((cmd & i) != 0) {
                Chip.I.digitalWrite(data, 1);
            } else {
                Chip.I.digitalWrite(data, 0);
            }
            Chip.I.digitalWrite(clk, 1);
        }
    }

    private void delay(int ms) {
        LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(ms));
    }

    private void delayMicro(int ms) {
        LockSupport.parkNanos(TimeUnit.MICROSECONDS.toNanos(ms));
    }
}
