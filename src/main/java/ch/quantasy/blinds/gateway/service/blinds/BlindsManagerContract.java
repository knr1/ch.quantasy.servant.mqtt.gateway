/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.quantasy.blinds.gateway.service.blinds;

import ch.quantasy.blinds.gateway.BlindsContract;
import ch.quantasy.blinds.gateway.message.BlindsDefinitionStatus;
import ch.quantasy.blinds.gateway.message.BlindsManagerIntent;
import java.util.Map;

/**
 *
 * @author reto
 */
public class BlindsManagerContract extends BlindsContract {

    public final String ADD;
    public final String INTENT_ADD;
    public final String REMOVE;
    public final String INTENT_REMOVE;

    public final String ADDED;
    public final String EVENT_ADDED;
    public final String REMOVED;
    public final String EVENT_REMOVED;

    public final String BLINDS;
    public final String STATUS_BLINDS;

    public BlindsManagerContract(String baseClass) {
        super(baseClass, null);

        ADD = "add";
        ADDED = "added";
        INTENT_ADD = INTENT + "/" + ADD;
        EVENT_ADDED = EVENT + "/" + ADDED;
        REMOVE = "remove";
        REMOVED = "removed";
        INTENT_REMOVE = INTENT + "/" + REMOVE;
        EVENT_REMOVED = EVENT + "/" + REMOVED;

        BLINDS = "Blinds";
        STATUS_BLINDS = STATUS + "/" + BLINDS;
        
        addMessageTopic(INTENT, BlindsManagerIntent.class);
        addMessageTopic(STATUS_BLINDS, BlindsDefinitionStatus.class);
    }

}
