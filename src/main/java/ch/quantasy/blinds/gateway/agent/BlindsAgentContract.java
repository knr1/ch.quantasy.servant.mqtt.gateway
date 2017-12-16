/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.quantasy.blinds.gateway.agent;

import ch.quantasy.mqtt.gateway.client.contract.AyamlServiceContract;
import java.util.Map;

/**
 *
 * @author reto
 */
public class BlindsAgentContract extends AyamlServiceContract{

    public BlindsAgentContract(String rootContext, String baseClass, String instance) {
        super(rootContext, baseClass, instance);
    }

    @Override
    protected void describe(Map<String, String> descriptions) {
        //No descriptions
    }
    
}
