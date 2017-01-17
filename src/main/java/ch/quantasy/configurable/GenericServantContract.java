/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.quantasy.configurable;

import ch.quantasy.blinds.gateway.servant.*;
import ch.quantasy.blinds.gateway.BlindsContract;

/**
 *
 * @author reto
 */
public class GenericServantContract extends BlindsContract{
    
    public final String ACTION;
    public final String INTENT_ACTION;
    public final String STATUS_ACTION;
    public final String EVENT_ACTION;
    
    public GenericServantContract(String baseClass, String instance) {
        super(baseClass, instance);
        ACTION="action";
        INTENT_ACTION=INTENT+"/"+ACTION;
        STATUS_ACTION=STATUS+"/"+ACTION;
        EVENT_ACTION=EVENT+"/"+ACTION;
    }
    
}
