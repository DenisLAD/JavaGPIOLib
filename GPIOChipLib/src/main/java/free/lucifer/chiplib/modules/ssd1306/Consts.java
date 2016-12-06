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

public class Consts {

    public final static int[] BASE_INIT = {
        Commands.SSD1306_DISPLAYOFF,
        Commands.SSD1306_SETDISPLAYCLOCKDIV,
        0x80,
        Commands.SSD1306_SETMULTIPLEX,
        0x00,
        Commands.SSD1306_SETDISPLAYOFFSET,
        0x00,
        Commands.SSD1306_SETSTARTLINE,
        Commands.SSD1306_CHARGEPUMP,
        0x00,
        Commands.SSD1306_MEMORYMODE,
        0x00,
        Commands.SSD1306_SEGREMAP | 0x01,
        Commands.SSD1306_COMSCANDEC,
        Commands.SSD1306_SETCOMPINS,
        0x00,
        Commands.SSD1306_SETCONTRAST,
        0x00,
        Commands.SSD1306_SETPRECHARGE,
        0x00,
        Commands.SSD1306_SETVCOMDETECT,
        0x40,
        Commands.SSD1306_DISPLAYALLON_RESUME,
        Commands.SSD1306_NORMALDISPLAY,
        Commands.SSD1306_DEACTIVATE_SCROLL,
        Commands.SSD1306_DISPLAYON
    };
}
