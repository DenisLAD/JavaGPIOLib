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

import free.lucifer.chiplib.Chip.Pin;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class DHT11 extends Task {

    private float temperature = -1;
    private float humidity = -1;
    private final Pin pin;
    private Status status = Status.OK;
    private final int[] buffer = new int[5];
    private int errorCount = 0;

    public int getErrorCount() {
        return errorCount;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getTemperature() {
        return temperature;
    }

    public Status getStatus() {
        return status;
    }

    private void delay(int ms) {
        LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(ms));
    }

    private void delayMicro(int ms) {
        LockSupport.parkNanos(TimeUnit.MICROSECONDS.toNanos(ms));
    }

    public DHT11(Pin pin) {
        this.pin = pin;
        this.period = TimeUnit.SECONDS.toNanos(2);
        pin.managerInstance.pinMode(pin, Pin.PinMode.OUTPUT);
        pin.managerInstance.digitalWrite(pin, 1);
    }

    @Override
    public void doTask() {
        read();
        pin.managerInstance.pinMode(pin, Pin.PinMode.OUTPUT);
        pin.managerInstance.digitalWrite(pin, 1);

//        SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy HH:mm:ss");
//        if (status == Status.OK) {
//            System.out.println(sdf.format(new Date()) + ": " + temperature + " celsius, " + humidity + "%");
//        }
    }

    public void read() {
        errorCount++;
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = 0;
        }
        pin.managerInstance.digitalWrite(pin, 0);
        delay(18);
        pin.managerInstance.digitalWrite(pin, 1);
//        delayMicro(2);
        pin.managerInstance.pinMode(pin, Pin.PinMode.INPUT_PULLUP);
        status = Status.OK;

        if (expectPulse(0) == 0) {
            status = Status.START_FAILED1;
            return;
        }
        if (expectPulse(1) == 0) {
            status = Status.START_FAILED2;
            return;
        }
        int cycles[] = new int[80];

        for (int i = 0; i < 80; i += 2) {
            cycles[i] = expectPulse(0);
            cycles[i + 1] = expectPulse(1);
        }

        for (int i = 0; i < 40; i++) {
            int low = cycles[i * 2];
            int hi = cycles[i * 2 + 1];
//            System.out.println(i + ": " + low + " " + hi);
            if (low == 0 || hi == 0) {
                status = Status.TIMEOUT;
                return;
            }

            buffer[i / 8] <<= 1;

            if (hi > low) {
                buffer[i / 8] |= 1;
            }

        }

        if (buffer[4] != ((buffer[0] + buffer[1] + buffer[2] + buffer[3]) & 0xFF)) {
            status = Status.CHECKSUM_ERROR;
            return;
        }

        humidity = buffer[0];
        temperature = buffer[2];
        errorCount = 0;

    }

    private int expectPulse(int val) {
        int count = 1;
        while (pin.managerInstance.digiatalRead(pin) == val) {
            if (count++ > 500) {
                return 0;
            }
        }
        return count;
    }

    public static enum Status {
        OK,
        CHECKSUM_ERROR,
        TIMEOUT,
        START_FAILED1,
        START_FAILED2,
        START_FAILED3
    }
}
