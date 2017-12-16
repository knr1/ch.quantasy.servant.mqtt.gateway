/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.quantasy.blinds.gateway.service.blinds;

import ch.quantasy.blinds.gateway.message.BlindsDefinition;
import ch.quantasy.blinds.gateway.message.BlindsDefinitionStatus;
import ch.quantasy.blinds.gateway.message.BlindsManagerIntent;
import ch.quantasy.blinds.manager.BlindsManager;
import ch.quantasy.blinds.manager.BlindsManagerCallback;
import ch.quantasy.mqtt.gateway.client.GatewayClient;
import java.net.URI;
import java.util.Set;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 *
 * @author reto
 */
public class BlindsManagerService extends GatewayClient<BlindsManagerContract> implements BlindsManagerCallback {
    
    private final BlindsManager manager;
    
    public BlindsManagerService(URI mqttURI) throws MqttException {
        super(mqttURI, "Servant.BlindsManager", new BlindsManagerContract("Manager"));
        this.manager = new BlindsManager(mqttURI, this);
        subscribe(getContract().INTENT + "/#", (topic, payload) -> {
            Set<BlindsManagerIntent> intents = toMessageSet(payload, BlindsManagerIntent.class);
            for (BlindsManagerIntent intent : intents) {
                if (intent != null && intent.isValid()) {
                    if (intent.action == BlindsManagerIntent.Action.add) {
                        manager.addBlinds(intent.blindsDefinition);
                    }
                    if(intent.action==BlindsManagerIntent.Action.remove){
                        manager.removeBlinds(intent.blindsDefinition.getId());
                    }
                }
            }
        });
        connect();
    }
    
    @Override
    public void blindsAdded(BlindsDefinition definition) {
        getPublishingCollector().readyToPublish(getContract().STATUS_BLINDS + "/" + definition.getId(), new BlindsDefinitionStatus(definition));
    }
    
    @Override
    public void blindsRemoved(BlindsDefinition definition) {
        getPublishingCollector().clearPublish(getContract().STATUS_BLINDS + "/" + definition.getId());
    }
    
}
