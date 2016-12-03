/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package free.lucifer.chiplib.boards;

import free.lucifer.chiplib.Chip.Pin;
import static free.lucifer.chiplib.Chip.Pin.XIO_P0;
import static free.lucifer.chiplib.Chip.Pin.XIO_P1;
import static free.lucifer.chiplib.Chip.Pin.XIO_P2;
import static free.lucifer.chiplib.Chip.Pin.XIO_P3;
import static free.lucifer.chiplib.Chip.Pin.XIO_P4;
import static free.lucifer.chiplib.Chip.Pin.XIO_P5;
import static free.lucifer.chiplib.Chip.Pin.XIO_P6;
import static free.lucifer.chiplib.Chip.Pin.XIO_P7;
import free.lucifer.chiplib.modules.I2C;

/**
 *
 * @author lucifer
 */
public class PCF8574A implements IOBoard {

    private I2C i2c;
    private int writeMask = 0;
    private int readMask = 0;

    public PCF8574A(int bus, int address) {
        i2c = new I2C(bus, address);
    }

    @Override
    public void open() {
        i2c.open();
        i2c.write(new byte[]{0});
    }

    @Override
    public void close() {
        i2c.close();
    }

    private int pinNum(Pin pin) {
        int id = -1;
        switch (pin) {
            case XIO_P0:
                id = 0;
                break;
            case XIO_P1:
                id = 1;
                break;
            case XIO_P2:
                id = 2;
                break;
            case XIO_P3:
                id = 3;
                break;
            case XIO_P4:
                id = 4;
                break;
            case XIO_P5:
                id = 5;
                break;
            case XIO_P6:
                id = 6;
                break;
            case XIO_P7:
                id = 7;
                break;
        }

        return id;
    }

    private Pin pinFromNum(int pin) {
        Pin id = null;
        switch (pin) {
            case 0:
                id = Pin.XIO_P0;
                break;
            case 1:
                id = Pin.XIO_P1;
                break;
            case 2:
                id = Pin.XIO_P2;
                break;
            case 3:
                id = Pin.XIO_P3;
                break;
            case 4:
                id = Pin.XIO_P4;
                break;
            case 5:
                id = Pin.XIO_P5;
                break;
            case 6:
                id = Pin.XIO_P6;
                break;
            case 7:
                id = Pin.XIO_P7;
                break;
        }

        return id;
    }

    @Override
    public void pinMode(Pin pin, Pin.PinMode mode) {
        int id = pinNum(pin);
        if (id == -1) {
            return;
        }

        if (mode == Pin.PinMode.INPUT) {
            readMask |= (1 << id);
        } else {
            readMask &= ~(1 << id);
        }

        digitalWrite(pin, 0);
    }

    @Override
    public void digitalWrite(Pin pin, int value) {
        int id = pinNum(pin);
        if (id == -1) {
            return;
        }
        if (value > 0) {
            writeMask |= (1 << id);
        } else {
            writeMask &= ~(1 << id);
        }

//        System.out.print("W:" + (~readMask & writeMask & 0xff) + " ");

        i2c.write(new byte[]{(byte) (~readMask & writeMask & 0xff)});
    }

    @Override
    public int digiatalRead(Pin pin) {
        int val = read();
//        System.out.print("R:" + val + " ");
        int id = pinNum(pin);
        return (val & (1 << id)) == 0 ? 0 : 1;
    }

    private int read() {
        return i2c.read(1)[0];
    }

    @Override
    public void pwmWrite(Pin pin, int value) {

    }

    @Override
    public int analogRead(Pin pin) {
        return -1;
    }

    @Override
    public void analogWrite(Pin pin, int value) {

    }
}
