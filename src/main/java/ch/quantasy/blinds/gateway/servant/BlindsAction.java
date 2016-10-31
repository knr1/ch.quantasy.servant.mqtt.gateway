/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.quantasy.blinds.gateway.servant;

/**
 *
 * @author reto
 */
public class BlindsAction {
    public static enum Direction{
        up,down,stop;     
    }
    private Direction direction;
    
    private BlindsAction(){
        
    }
    public BlindsAction(Direction direction){
        this.direction=direction;
    }

    public Direction getDirection() {
        return direction;
    }
    
}
