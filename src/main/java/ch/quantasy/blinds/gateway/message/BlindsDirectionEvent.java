/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.quantasy.blinds.gateway.message;

import ch.quantasy.mqtt.gateway.client.message.AnEvent;
import ch.quantasy.mqtt.gateway.client.message.annotations.NonNull;
import ch.quantasy.mqtt.gateway.client.message.annotations.StringForm;

/**
 * I
 *
 * @author reto
 */
public class BlindsDirectionEvent extends AnEvent {

    @NonNull
    @StringForm
    public String id;

    @NonNull
    public BlindsDirection direction;

    private BlindsDirectionEvent() {
    }

    public BlindsDirectionEvent(String id, BlindsDirection direction) {
        this.id = id;
        this.direction = direction;
    }
    
    

    
}
