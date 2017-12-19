/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.quantasy.binder.gateway.manager;

import ch.quantasy.binder.BinderCallback;
import ch.quantasy.blinds.gateway.message.BlindsDefinition;
import ch.quantasy.blinds.gateway.message.BlindsManagerIntent;
import ch.quantasy.mqtt.gateway.client.GatewayClient;
import java.net.URI;
import java.util.Set;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 *
 * @author reto
 */
public class BinderManager extends GatewayClient<BinderManagerContract> implements BinderCallback {

   // private final BinderService binder;

    public BinderManager(URI mqttURI) throws MqttException {
        super(mqttURI, "Binderxxx", new BinderManagerContract("BinderManager"));
        //this.binder = new BinderService(mqttURI,this);
        subscribe(getContract().INTENT + "/#", (topic, payload) -> {
            Set<BlindsManagerIntent> definitions = toMessageSet(payload, BlindsManagerIntent.class);
            for(BlindsManagerIntent intent:definitions){
                if(intent.action==BlindsManagerIntent.Action.add){
                    //    binder.addBlinds(definition);
                }
                if(intent.action==BlindsManagerIntent.Action.remove){
                    //    binder.removeBlinds(definition);
                }
            }
                    
        });
        connect();
    }

//   // @Override
//    public void ledStripAdded(BlindsDefinition definition) {
//        publishStatus(getContract().STATUS_BLINDS + "/" + definition.getId(), definition);
//    }
//
//   // @Override
//    public void ledStripRemoved(BlindsDefinition definition) {
//        publishStatus(getContract().STATUS_BLINDS + "/" + definition.getId(), null);
//    }

}
