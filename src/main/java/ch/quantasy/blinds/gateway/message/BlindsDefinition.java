/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.quantasy.blinds.gateway.message;

import ch.quantasy.mqtt.gateway.client.message.annotations.AValidator;
import ch.quantasy.mqtt.gateway.client.message.annotations.NonNull;
import ch.quantasy.mqtt.gateway.client.message.annotations.StringForm;

/**
 *
 * @author reto
 */
public class BlindsDefinition extends AValidator{
   
    @NonNull
    @StringForm
    private String id;
    @StringForm
    private String dualRelayId;
    
    private BlindsDefinition() {
    }

    public BlindsDefinition(String id, String dualRelayId) {
        this.id = id;
        this.dualRelayId = dualRelayId;
    }

    public BlindsDefinition(String id) {
        this.id = id;
    }
    

    public String getDualRelayId() {
        return dualRelayId;
    }

    public String getId() {
        return id;
    }
    
    
}
