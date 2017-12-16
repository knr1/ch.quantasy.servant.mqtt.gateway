/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.quantasy.blinds.gateway.message;

import ch.quantasy.mqtt.gateway.client.message.AStatus;

/**
 *
 * @author reto
 */
public class BlindsDefinitionStatus extends AStatus{
    public BlindsDefinition blindsDefinition;

    public BlindsDefinitionStatus(BlindsDefinition blindsDefinition) {
        this.blindsDefinition = blindsDefinition;
    }

    private BlindsDefinitionStatus() {
    }
    
}
