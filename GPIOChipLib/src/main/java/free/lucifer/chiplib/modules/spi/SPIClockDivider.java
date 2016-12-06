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
package free.lucifer.chiplib.modules.spi;

/**
 *
 * @author lucifer
 */
public enum SPIClockDivider {
    SPI_CLOCK_DIV4(0x00),
    SPI_CLOCK_DIV16(0x01),
    SPI_CLOCK_DIV64(0x02),
    SPI_CLOCK_DIV128(0x03),
    SPI_CLOCK_DIV2(0x04),
    SPI_CLOCK_DIV8(0x05),
    SPI_CLOCK_DIV32(0x06);

    public final int divider;

    private SPIClockDivider(int divider) {
        this.divider = divider;
    }

}
