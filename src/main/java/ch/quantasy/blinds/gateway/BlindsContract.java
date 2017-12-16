/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.quantasy.blinds.gateway;

import ch.quantasy.mqtt.gateway.client.contract.AyamlServiceContract;
import java.util.Map;

/**
 *
 * @author reto
 */
public abstract class BlindsContract extends AyamlServiceContract {

    public BlindsContract(String baseClass, String instance) {
        super("Blinds", baseClass, instance);
    }

}
