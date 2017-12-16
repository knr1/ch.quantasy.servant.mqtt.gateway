/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.quantasy.blinds.manager;

import ch.quantasy.blinds.gateway.message.BlindsDefinition;

/**
 *
 * @author reto
 */
public interface BlindsManagerCallback {
    public void blindsAdded(BlindsDefinition definition);
    public void blindsRemoved(BlindsDefinition definition);
}
