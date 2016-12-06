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

package free.lucifer.chiplib.modules.ssd1306;


public class Commands {

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
}
