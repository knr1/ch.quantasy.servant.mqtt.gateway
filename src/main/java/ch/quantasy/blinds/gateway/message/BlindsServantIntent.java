/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.quantasy.blinds.gateway.message;

import ch.quantasy.mqtt.gateway.client.message.AnIntent;
import ch.quantasy.mqtt.gateway.client.message.annotations.NonNull;
import ch.quantasy.mqtt.gateway.client.message.annotations.StringForm;

/**
 *
 * @author reto
 */
public class BlindsServantIntent extends AnIntent {


    @NonNull
    public BlindsDirection direction;

    private BlindsServantIntent() {

    }

    public BlindsServantIntent(BlindsDirection direction) {
        this.direction = direction;
    }

    public BlindsDirection getDirection() {
        return direction;
    }

    
}
