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
public enum SPIMode {
    SPI_MPDE0(0x00),
    SPI_MODE1(0x04),
    SPI_MODE2(0x08),
    SPI_MODE3(0x0c);

    public final int mode;

    private SPIMode(int mode) {
        this.mode = mode;
    }

}
