/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package free.lucifer.chiplib.modules;

/**
 *
 * @author lucifer
 */
public abstract class Task {

    public long period;
    public long delta;

    public abstract void doTask();

}
