/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.quantasy.blinds.gateway;

import ch.quantasy.mqtt.gateway.client.ClientContract;

/**
 *
 * @author reto
 */
public class BlindsContract extends ClientContract{
    
   
    
    public BlindsContract(String baseClass, String instance) {
        super("Blinds", baseClass, instance);
    }

}