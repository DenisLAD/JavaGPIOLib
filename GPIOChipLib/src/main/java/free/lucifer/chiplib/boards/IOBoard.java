/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package free.lucifer.chiplib.boards;

import free.lucifer.chiplib.Chip.Pin;

/**
 *
 * @author lucifer
 */
public interface IOBoard {

    public void open();

    public void close();

    public void pinMode(Pin pin, Pin.PinMode mode);

    public void pwmWrite(Pin pin, int value);

    public int analogRead(Pin pin);

    public void analogWrite(Pin pin, int value);

    public int digiatalRead(Pin pin);

    public void digitalWrite(Pin pin, int value);
}
