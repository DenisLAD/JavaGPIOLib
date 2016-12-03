/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package free.lucifer.chiplib.modules;

import free.lucifer.chiplib.Chip;

/**
 *
 * @author lucifer
 */
public class Battery implements Chip.PinListener {

    private float voltage = 0;

    public Battery() {
        Chip.I.pinMode(Chip.Pin.BAT, Chip.Pin.PinMode.INPUT);
        Chip.I.subscribePinChange(Chip.Pin.BAT, this);
    }

    @Override
    public void onPinChange(Chip.Pin pin, int old, int val) {
        voltage = (float) val * 1.1f / 1000f;
    }

    public float getVoltage() {
        return voltage;
    }

}
