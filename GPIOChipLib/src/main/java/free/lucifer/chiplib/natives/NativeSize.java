/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package free.lucifer.chiplib.natives;

import com.sun.jna.IntegerType;
import com.sun.jna.Native;

/**
 *
 * @author lucifer
 */
public class NativeSize extends IntegerType {

    private static final long serialVersionUID = 2398288011955445078L;

    public static int SIZE = Native.SIZE_T_SIZE;//Platform.is64Bit() ? 8 : 4;

    public NativeSize() {
        this(0);
    }

    public NativeSize(long value) {
        super(SIZE, value);
    }
}
