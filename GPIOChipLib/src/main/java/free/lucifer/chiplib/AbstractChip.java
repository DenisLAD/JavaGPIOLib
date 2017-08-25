/*
 * Copyright 2017 lucifer.
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
package free.lucifer.chiplib;

import free.lucifer.chiplib.boards.IOBoard;
import free.lucifer.chiplib.modules.Task;
import free.lucifer.chiplib.utils.Registry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 *
 * @author lucifer
 */
public abstract class AbstractChip implements IOBoard, Runnable {

    protected Map<Class<? extends IOBoard>, IOBoard> registry = new HashMap<>();
    protected boolean open = false;

    protected final List<Enum> poolPin = new CopyOnWriteArrayList<>();
    protected final List<Task> tasks = new CopyOnWriteArrayList<>();
    protected final Map<Enum, List<Chip.PinListener>> listeners = new ConcurrentHashMap<>();

    private static final long POOL_INTERVAL = TimeUnit.MILLISECONDS.toNanos(20);
    private Thread poolThread;

    protected abstract Class<?> getTargetPinMapping();

    public AbstractChip(IOBoard... boards) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                close();
            }
        });
        for (IOBoard board : boards) {
            registry.put(board.getClass(), board);
        }

    }

    public void delay(int ms) {
        LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(ms));
    }

    public void delayMicro(int ms) {
        LockSupport.parkNanos(TimeUnit.MICROSECONDS.toNanos(ms));
    }

    protected void embedIO() {
        for (Enum pin : (Enum[]) Registry.INSTANCE.getEnums()) {
            Registry.set(pin, "managerInstance", registry.get(Registry.get(pin, "manager")));
        }
    }

    protected void parsePins() {
        for (Enum pin : (Enum[]) getTargetPinMapping().getEnumConstants()) {
            Enum p = Registry.find(pin.toString());
            Registry.set(p, "customPin", pin);
        }
    }

    @Override
    public void run() {
        long shift = 0;
        while (open) {
            if (POOL_INTERVAL - shift > 0) {
                LockSupport.parkNanos(POOL_INTERVAL - shift);
            }
            long nano = System.nanoTime();
            for (Enum p : poolPin) {
                Chip.Pin pin = (Chip.Pin) p;
                if (pin.mode == PinMode.INPUT) {
                    pin.lastReport = analogRead(pin);
                } else {
                    pin.lastReport = digiatalRead(pin);
                }

                if (pin.report != pin.lastReport) {
                    emit(pin);
                    pin.report = pin.lastReport;
                }
            }
            for (Task task : tasks) {
                task.delta += POOL_INTERVAL;
                if (task.delta > task.period) {
                    task.delta -= task.period;
                    task.doTask();
                }
            }
            shift = System.nanoTime() - nano;
        }
    }

    public void subscribePinChange(Enum pin, PinListener listener) {
        List<PinListener> list = listeners.get(pin);
        if (list == null) {
            list = new CopyOnWriteArrayList<>();
            listeners.put(pin, list);
        }

        list.add(listener);
    }

    public void unSubscribePinChange(Enum pin, PinListener listener) {
        List<PinListener> list = listeners.get(pin);
        if (list == null) {
            list = new CopyOnWriteArrayList<>();
            listeners.put(pin, list);
        }

        list.remove(listener);
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }

    @Override
    public void open() {
        Registry.INSTANCE.setTarget(this.getClass());

        embedIO();
        parsePins();

        for (IOBoard io : registry.values()) {
            io.open();
        }

        open = true;
        poolThread = new Thread(this);
        poolThread.start();
    }

    @Override
    public void close() {
        for (IOBoard io : registry.values()) {
            io.close();
        }
        open = false;
    }

    @Override
    public void pinMode(Enum pin, Enum mode) {
        Registry.set(pin, "mode", mode);

        if (mode == PinMode.INPUT || mode == PinMode.PWM || mode == PinMode.ANALOG) {
            if (!poolPin.contains(pin)) {
                poolPin.add(pin);
            }
        } else if (poolPin.contains(pin)) {
            poolPin.remove(pin);
        }

        Registry.get(pin, "managerInstance", IOBoard.class).pinMode(pin, mode);
    }

    @Override
    public void pwmWrite(Enum pin, int value) {
        Registry.get(pin, "managerInstance", IOBoard.class).pwmWrite(pin, value);
    }

    @Override
    public int analogRead(Enum pin) {
        return Registry.get(pin, "managerInstance", IOBoard.class).analogRead(pin);
    }

    @Override
    public void analogWrite(Enum pin, int value) {
        pwmWrite(pin, value);
    }

    @Override
    public int digiatalRead(Enum pin) {
        return Registry.get(pin, "managerInstance", IOBoard.class).digiatalRead(pin);
    }

    @Override
    public void digitalWrite(Enum pin, int value) {
        Registry.get(pin, "managerInstance", IOBoard.class).digitalWrite(pin, value);
    }

    private void emit(Enum pin) {
        List<PinListener> list = listeners.get(pin);
        if (list == null) {
            return;
        }
        for (PinListener listener : list) {
            listener.onPinChange(pin, (int) Registry.get(pin, "report"), (int) Registry.get(pin, "lastReport"));
        }
    }

    public void shiftOut(Enum dataPin, Enum clockPin, byte value) {
        shiftOut(dataPin, clockPin, value, true);
    }

    public void shiftOut(Enum dataPin, Enum clockPin, byte value, boolean isBigEndian) {
        for (int i = 0; i < 8; i++) {
            digitalWrite(clockPin, 0);
            if (isBigEndian) {
                digitalWrite(dataPin, (value & (1 << (7 - i))) == 0 ? 0 : 1);
            } else {
                digitalWrite(dataPin, (value & (1 << i)) == 0 ? 0 : 1);
            }
            digitalWrite(clockPin, 1);
        }
    }

    public static enum EventType {
        DATA_CHANGE,
    }

    public static interface PinListener {

        void onPinChange(Enum pin, int old, int val);
    }

}
