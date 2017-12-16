/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.quantasy.blinds.gateway.message;

import ch.quantasy.mqtt.gateway.client.message.AnIntent;
import ch.quantasy.mqtt.gateway.client.message.annotations.NonNull;

/**
 *
 *
 * @author reto
 */
public class BlindsManagerIntent extends AnIntent {

    public static enum Action {
        add, remove;
    }
    @NonNull
    public Action action;
    @NonNull
    public BlindsDefinition blindsDefinition;

    public BlindsManagerIntent() {
    }

    public BlindsManagerIntent(Action action,BlindsDefinition blindsDefinition) {
        this.action=action;
        this.blindsDefinition = blindsDefinition;
    }

}
