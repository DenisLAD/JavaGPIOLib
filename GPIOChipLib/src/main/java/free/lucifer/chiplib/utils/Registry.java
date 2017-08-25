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
package free.lucifer.chiplib.utils;

import free.lucifer.chiplib.AbstractChip;
import free.lucifer.chiplib.boards.IOBoard;
import java.lang.reflect.Field;

/**
 *
 * @author lucifer
 */
public final class Registry {

    public static final Registry INSTANCE = new Registry();

    private Class<?> target;
    private Class<?> targetEnum;
    private AbstractChip board;

    private Registry() {
    }

    public void setTarget(Class<?> target) {
        if (target != null) {
            throw new RuntimeException("Target class [" + this.target.getName() + "] already defined");
        }
        try {
            this.targetEnum = targetEnum();
            this.board = getBoard();
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {
            throw new RuntimeException("Cannot determine targets", ex);
        }
        this.target = target;
    }

    public Class<?> getTarget() {
        return target;
    }

    private Class<?> targetEnum() throws ClassNotFoundException {
        return Class.forName(target.getName() + "$Pin.class");
    }

    private AbstractChip getBoard() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        return (AbstractChip) target.getDeclaredField("I").get(null);
    }

    public AbstractChip board() {
        return board;
    }

    public <T> T[] getEnums() {
        return (T[]) targetEnum.getEnumConstants();
    }

    public static <T> T get(Enum e, String field) {
        try {
            Field f = e.getDeclaringClass().getDeclaredField(field);
            f.setAccessible(true);
            return (T) f.get(e);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ex) {
            throw new RuntimeException("Declared field[" + field + "] for Enum[" + e.name() + "] not found", ex);
        }
    }

    public static <T> T get(Enum e, String field, Class<T> cls) {
        return (T) get(e, field);
    }

    public static void set(Enum e, String field, Object value) {
        try {
            Field f = e.getDeclaringClass().getDeclaredField(field);
            f.setAccessible(true);
            f.set(e, value);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ex) {
            throw new RuntimeException("Declared field[" + field + "] for Enum[" + e.name() + "] not found", ex);
        }
    }

    public static Enum find(String name) {
        for (Enum e : (Enum[]) INSTANCE.getEnums()) {
            if (e.name().equals(name)) {
                return e;
            }
        }
        return null;
    }

}
