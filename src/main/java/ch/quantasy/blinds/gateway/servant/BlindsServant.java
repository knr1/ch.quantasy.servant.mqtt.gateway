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
import java.net.URI;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 *
 * @author reto
 * @param <S>
 */
public class BlindsServant extends GatewayClient<BlindsServantContract> {

    private final Object synchronizationObject = new Object();
    private BlindsDefinition definition;
    private DualRelayServiceContract dualRelayServiceContract;
    private DeviceState state;

    public BlindsServant(URI mqttURI, BlindsDefinition blindsDefinition) throws MqttException {
        super(mqttURI, "BlindsServant" + blindsDefinition.getId(), new BlindsServantContract("BlindsServant", blindsDefinition.getId()));
        this.definition = blindsDefinition;
        this.dualRelayServiceContract = new DualRelayServiceContract(blindsDefinition.getDualRelayId(), TinkerforgeDeviceClass.DualRelay.toString());

        subscribe(this.dualRelayServiceContract.STATUS_STATE, (String topic, byte[] payload) -> {
            synchronized (synchronizationObject) {
                state = getMapper().readValue(payload, DeviceState.class);
                synchronizationObject.notifyAll();
            }
        });
        subscribe(getContract().INTENT_ACTION + "/#", (topic, payload) -> {
            BlindsAction blindsAction = getMapper().readValue(payload, BlindsAction.class);
            if (blindsAction.getDirection() == BlindsAction.Direction.stop) {
                DeviceSelectedState selectedState = new DeviceSelectedState((short) 1, false);
                publishIntent(dualRelayServiceContract.INTENT_SELECTED_STATE + "/blindsServant" + blindsDefinition.getId(), selectedState);
            }
            if (blindsAction.getDirection() == BlindsAction.Direction.up) {
                DeviceState desiredState = new DeviceState(true, false);
                synchronized (synchronizationObject) {
                    if (this.state != null && this.state.equals(desiredState)) {
                        return;
                    }
                    while (this.state == null || this.state.getRelay1()) {
                        DeviceSelectedState powerState = new DeviceSelectedState((short) 1, false);
                        publishIntent(dualRelayServiceContract.INTENT_SELECTED_STATE + "/blindsServant" + blindsDefinition.getId(), powerState);
                        synchronizationObject.wait(1000);
                    }
                    Thread.sleep(50);
                    while (this.state == null || this.state.getRelay2()) {
                        DeviceSelectedState directionState = new DeviceSelectedState((short) 2, false);
                        publishIntent(dualRelayServiceContract.INTENT_SELECTED_STATE + "/blindsServant" + blindsDefinition.getId(), directionState);
                        synchronizationObject.wait(1000);
                    }
                    Thread.sleep(50);
                    while (this.state == null || !this.state.getRelay1()) {
                        DeviceSelectedState powerState = new DeviceSelectedState((short) 1, true);
                        publishIntent(dualRelayServiceContract.INTENT_SELECTED_STATE + "/blindsServant" + blindsDefinition.getId(), powerState);
                        synchronizationObject.wait(1000);
                    }
                    DeviceMonoflopParameters stopMonoflop = new DeviceMonoflopParameters((short) 1, true, 1000 * 180);
                    publishIntent(dualRelayServiceContract.INTENT_MONOFLOP, stopMonoflop);
                }

            }
            if (blindsAction.getDirection() == BlindsAction.Direction.down) {
                DeviceState desiredState = new DeviceState(true, true);
                synchronized (synchronizationObject) {
                    if (this.state != null && this.state.equals(desiredState)) {
                        return;
                    }
                    while (this.state == null || this.state.getRelay1()) {
                        DeviceSelectedState powerState = new DeviceSelectedState((short) 1, false);
                        publishIntent(dualRelayServiceContract.INTENT_SELECTED_STATE + "/blindsServant" + blindsDefinition.getId(), powerState);
                        synchronizationObject.wait(1000);
                    }
                    Thread.sleep(50);
                    while (this.state == null || !this.state.getRelay2()) {
                        DeviceSelectedState directionState = new DeviceSelectedState((short) 2, true);
                        publishIntent(dualRelayServiceContract.INTENT_SELECTED_STATE + "/blindsServant" + blindsDefinition.getId(), directionState);
                        synchronizationObject.wait(1000);
                    }
                    Thread.sleep(50);
                    while (this.state == null || !this.state.getRelay1()) {
                        DeviceSelectedState powerState = new DeviceSelectedState((short) 1, true);
                        publishIntent(dualRelayServiceContract.INTENT_SELECTED_STATE + "/blindsServant" + blindsDefinition.getId(), powerState);
                        synchronizationObject.wait(1000);
                    }
                    DeviceMonoflopParameters stopMonoflop = new DeviceMonoflopParameters((short) 1, true, 1000 * 180);
                    DeviceMonoflopParameters directionMonoflop = new DeviceMonoflopParameters((short) 2, true, 1000 * 200);
                    publishIntent(dualRelayServiceContract.INTENT_MONOFLOP, stopMonoflop);
                    publishIntent(dualRelayServiceContract.INTENT_MONOFLOP, directionMonoflop);
                }

            }

        });
        connect();

    }

    public BlindsDefinition getDefinition() {
        return definition;
    }

}
