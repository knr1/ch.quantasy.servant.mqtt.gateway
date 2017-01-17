/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.quantasy.blinds.manager;

/**
 *
 * @author reto
 */
public interface BlindsManagerCallback {
    public void ledStripAdded(BlindsDefinition definition);
    public void ledStripRemoved(BlindsDefinition definition);
}
