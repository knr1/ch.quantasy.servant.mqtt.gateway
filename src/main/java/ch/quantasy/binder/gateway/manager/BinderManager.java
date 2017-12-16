/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.quantasy.binder.gateway.manager;

import ch.quantasy.binder.BinderCallback;
import ch.quantasy.blinds.gateway.message.BlindsDefinition;
import ch.quantasy.mqtt.gateway.client.GatewayClient;
import java.net.URI;
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
        subscribe(getContract().INTENT_ADD + "/#", (topic, payload) -> {
            final BlindsDefinition definition = getMapper().readValue(payload, BlindsDefinition.class);
            //    binder.addBlinds(definition);
        });
        subscribe(getContract().INTENT_REMOVE + "/#", (topic, payload) -> {
            final String id = getMapper().readValue(payload, String.class);
            //    binder.removeBlinds(id);
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
