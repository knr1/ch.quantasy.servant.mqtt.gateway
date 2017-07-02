/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.quantasy.binder.gateway.manager;

import ch.quantasy.blinds.gateway.BlindsContract;
import java.util.Map;

/**
 *
 * @author reto
 */
public class BinderManagerContract extends BlindsContract {

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

    public BinderManagerContract(String baseClass) {
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
    }

    @Override
    protected void describe(Map<String, String> descriptions) {
        descriptions.put(INTENT_ADD, "id: <String> \n dualRelayId: <String>");
        descriptions.put(INTENT_REMOVE, "id: <String>");
        descriptions.put(STATUS_BLINDS + "<id>", "id: <String> \n dualRelayID: <String>");
    }

}
