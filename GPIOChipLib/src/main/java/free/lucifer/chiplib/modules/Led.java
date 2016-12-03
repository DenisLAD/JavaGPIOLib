/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package free.lucifer.chiplib.modules;

import free.lucifer.chiplib.Chip;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author lucifer
 */
public class Led extends Task {

    private boolean active = false;
    private boolean taskAdded = false;
    private Chip.Pin pin;

    public Led(Chip.Pin pin) {
        Chip.I.pinMode(pin, Chip.Pin.PinMode.OUTPUT);
        this.pin = pin;
    }

    @Override
    public void doTask() {
        if (active) {
            off();
        } else {
            on();
        }
    }

    public void blink(long ms) {
        period = TimeUnit.MILLISECONDS.toNanos(ms);
        delta = 0;
        if (!taskAdded) {
            Chip.I.addTask(this);
            taskAdded = true;
        }
    }

    public void on() {
        active = true;
        Chip.I.digitalWrite(pin, 1);
    }

    public void off() {
        active = false;
        Chip.I.digitalWrite(pin, 0);
    }

    public void stopBlinking() {
        if (taskAdded) {
            Chip.I.removeTask(this);
            taskAdded = false;
        }
    }
}
