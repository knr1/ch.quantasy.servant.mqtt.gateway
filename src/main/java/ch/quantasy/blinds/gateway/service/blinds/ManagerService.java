/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.quantasy.blinds.gateway.service.blinds;

import ch.quantasy.blinds.manager.BlindsDefinition;
import ch.quantasy.blinds.manager.BlindsManager;
import ch.quantasy.blinds.manager.BlindsManagerCallback;
import ch.quantasy.mqtt.gateway.client.GatewayClient;
import java.net.URI;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 *
 * @author reto
 */
public class ManagerService extends GatewayClient<BlindsManagerContract> implements BlindsManagerCallback {

    private final BlindsManager manager;

    public ManagerService(URI mqttURI) throws MqttException {
        super(mqttURI, "Servant.BlindsManager", new BlindsManagerContract("Manager"));
        this.manager = new BlindsManager(mqttURI,this);
        subscribe(getContract().INTENT_ADD + "/#", (topic, payload) -> {
            final BlindsDefinition definition = getMapper().readValue(payload, BlindsDefinition.class);
            manager.addBlinds(definition);
        });
        subscribe(getContract().INTENT_REMOVE + "/#", (topic, payload) -> {
            final String id = getMapper().readValue(payload, String.class);
            manager.removeBlinds(id);
        });
        connect();

        publishDescription(getContract().INTENT_ADD, "id: <String> \n dualRelayId: <String>");
        publishDescription(getContract().INTENT_REMOVE, "id: <String>");
        publishDescription(getContract().STATUS_BLINDS+"<id>", "id: <String> \n dualRelayID: <String>");
    }

    @Override
    public void ledStripAdded(BlindsDefinition definition) {
            publishStatus(getContract().STATUS_BLINDS+"/"+definition.getId(),definition);
    }

    @Override
    public void ledStripRemoved(BlindsDefinition definition) {
            publishStatus(getContract().STATUS_BLINDS+"/"+definition.getId(),null);
    }

}
