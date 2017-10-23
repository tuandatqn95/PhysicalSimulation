/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daodongconlac.event;

/**
 *
 * @author tuand
 */
public interface TimerListener {
    public void OnStart();
    public void OnStop();
    public void OnTick();
}
