/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.quantasy.blinds.gateway.servant;

import ch.quantasy.blinds.gateway.BlindsContract;
import ch.quantasy.blinds.gateway.message.BlindsDirectionEvent;
import ch.quantasy.blinds.gateway.message.BlindsDefinitionStatus;
import ch.quantasy.blinds.gateway.message.BlindsServantIntent;
import ch.quantasy.mqtt.gateway.client.message.Message;
import java.util.Map;

/**
 *
 * @author reto
 */
public class BlindsServantContract extends BlindsContract {

    private final String ACTION;
    private final String DEFINITION;
    public final String STATUS_DEFINITION;
    public final String EVENT_ACTION;

    public BlindsServantContract(String baseClass, String instance) {
        super(baseClass, instance);
        ACTION = "action";
        DEFINITION = "definition";
        STATUS_DEFINITION = STATUS + "/" + DEFINITION;
        EVENT_ACTION = EVENT + "/" + ACTION;

    }

    @Override
    public void setMessageTopics(Map<String, Class<? extends Message>> messageTopicMap) {
        messageTopicMap.put(INTENT, BlindsServantIntent.class);
        messageTopicMap.put(STATUS_DEFINITION, BlindsDefinitionStatus.class);
        messageTopicMap.put(EVENT_ACTION, BlindsDirectionEvent.class);
    }

}
