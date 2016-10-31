/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.quantasy.blinds.manager;

/**
 *
 * @author reto
 */
public class BlindsDefinition {
    private String id;
    private String dualRelayId;

    private BlindsDefinition() {
    }

    public BlindsDefinition(String id, String dualRelayId) {
        this.id = id;
        this.dualRelayId = dualRelayId;
    }

    public String getDualRelayId() {
        return dualRelayId;
    }

    public String getId() {
        return id;
    }
    
    
    
    
}
