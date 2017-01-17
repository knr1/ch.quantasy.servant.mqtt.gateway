/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.quantasy.blinds.manager;

import ch.quantasy.blinds.gateway.servant.BlindsServant;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 *
 * @author reto
 */
public class BlindsManager {
    private final URI mqttURI;
    private final Map<String,BlindsServant> blindsServantMap;
    private final BlindsManagerCallback callback;
    public BlindsManager(URI mqttURI,BlindsManagerCallback callback){
        this.callback=callback;
        this.mqttURI=mqttURI;
        blindsServantMap=new HashMap<>();
    }
    public void addBlinds(BlindsDefinition definition){
        if(blindsServantMap.containsKey(definition.getId()))
            return;
        try {
            blindsServantMap.put(definition.getId(),new BlindsServant(mqttURI, definition));
            callback.ledStripAdded(definition);
        } catch (MqttException ex) {
            Logger.getLogger(BlindsManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void removeBlinds(String id){
        BlindsServant servant=blindsServantMap.remove(id);
        if(servant!=null){
            try {
                servant.disconnect();
            } catch (MqttException ex) {
                Logger.getLogger(BlindsManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            callback.ledStripRemoved(servant.getDefinition());
        }
    }
}
