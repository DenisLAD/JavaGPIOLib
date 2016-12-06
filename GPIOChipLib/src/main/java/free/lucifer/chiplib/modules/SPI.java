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

import free.lucifer.chiplib.modules.spi.SPIClockDivider;


public class SPI {

    
    private final int bus;
    private final int device;

    public SPI() {
        this.bus = 0;
        this.device = 0;
        // TODO: Dummy constructor for defaults
    }

    
    
    public SPI(int bus, int device) {
        this.bus = bus;
        this.device = device;
    }

    public void open() {

    }

    public void close() {

    }

    void begin() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void transfer(int cmd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void setClockDivider(SPIClockDivider spiClockDivider) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
