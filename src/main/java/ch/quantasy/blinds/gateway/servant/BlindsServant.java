/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.quantasy.blinds.gateway.servant;

import ch.quantasy.blinds.manager.BlindsDefinition;
import ch.quantasy.gateway.service.device.dualRelay.DualRelayServiceContract;
import ch.quantasy.mqtt.gateway.client.GatewayClient;
import ch.quantasy.tinkerforge.device.TinkerforgeDeviceClass;
import ch.quantasy.tinkerforge.device.dualRelay.DeviceMonoflopParameters;
import ch.quantasy.tinkerforge.device.dualRelay.DeviceSelectedState;
import ch.quantasy.tinkerforge.device.dualRelay.DeviceState;
import com.tinkerforge.BrickletDualRelay;
import java.net.URI;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 *
 * @author reto
 * @param <S>
 */
public class BlindsServant extends GatewayClient<BlindsServantContract> {

    private BlindsDefinition definition;
    private DualRelayServiceContract dualRelayServiceContract;

    public BlindsServant(URI mqttURI, BlindsDefinition blindsDefinition) throws MqttException {
        super(mqttURI, "BlindsServant"+blindsDefinition.getId(), new BlindsServantContract("BlindsServant", blindsDefinition.getId()));
        this.definition = blindsDefinition;
        this.dualRelayServiceContract = new DualRelayServiceContract(blindsDefinition.getDualRelayId(), TinkerforgeDeviceClass.DualRelay.toString());
        subscribe(getContract().INTENT_ACTION + "/#", (topic, payload) -> {
            BlindsAction blindsAction = getMapper().readValue(payload, BlindsAction.class);
            if (blindsAction.getDirection() == BlindsAction.Direction.stop) {
                DeviceSelectedState selectedState = new DeviceSelectedState((short) 1, false);
                addIntent(dualRelayServiceContract.INTENT_SELECTED_STATE + "/blindsServant" + blindsDefinition.getId(), selectedState);
            }
            if (blindsAction.getDirection() == BlindsAction.Direction.up) {
                DeviceState state = new DeviceState(true, false);
                addIntent(dualRelayServiceContract.INTENT_STATE + "/blindsServant" + blindsDefinition.getId(), state);
                DeviceMonoflopParameters monoflop1 = new DeviceMonoflopParameters((short) 1, true, 1000 * 180);
                addIntent(dualRelayServiceContract.INTENT_MONOFLOP, monoflop1);

            }
            if (blindsAction.getDirection() == BlindsAction.Direction.down) {
                DeviceState state = new DeviceState(true, true);
                addIntent(dualRelayServiceContract.INTENT_STATE + "/blindsServant" + blindsDefinition.getId(), state);
                DeviceMonoflopParameters monoflop1 = new DeviceMonoflopParameters((short) 1, true, 1000 * 180);
                DeviceMonoflopParameters monoflop2 = new DeviceMonoflopParameters((short) 2, true, 1000 * 180);
                addIntent(dualRelayServiceContract.INTENT_MONOFLOP, monoflop1);
                addIntent(dualRelayServiceContract.INTENT_MONOFLOP, monoflop2);

            }

        });
        connect();

        addDescription(getContract().INTENT_ACTION, "direction: [up|down|stop]");
    }

    public BlindsDefinition getDefinition() {
        return definition;
    }

}
