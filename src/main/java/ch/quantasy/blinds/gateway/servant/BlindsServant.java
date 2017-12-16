/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.quantasy.blinds.gateway.servant;

import ch.quantasy.blinds.gateway.message.BlindsDefinition;
import ch.quantasy.blinds.gateway.message.BlindsDirection;
import ch.quantasy.blinds.gateway.message.BlindsServantIntent;
import ch.quantasy.gateway.service.device.dualRelay.DualRelayServiceContract;
import ch.quantasy.mqtt.gateway.client.GatewayClient;
import ch.quantasy.tinkerforge.device.TinkerforgeDeviceClass;
import ch.quantasy.gateway.message.dualRelay.DeviceMonoflopParameters;
import ch.quantasy.gateway.message.dualRelay.DeviceSelectedRelayState;
import ch.quantasy.gateway.message.dualRelay.DeviceRelayState;
import ch.quantasy.gateway.message.dualRelay.DualRelayIntent;
import ch.quantasy.gateway.message.dualRelay.RelayStateStatus;
import java.util.HashSet;
import java.net.URI;
import java.util.Set;
import java.util.TreeSet;
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
    private DeviceRelayState state;

    public BlindsServant(URI mqttURI, BlindsDefinition blindsDefinition) throws MqttException {
        super(mqttURI, "BlindsServant" + blindsDefinition.getId(), new BlindsServantContract("BlindsServant", blindsDefinition.getId()));
        this.definition = blindsDefinition;
        this.dualRelayServiceContract = new DualRelayServiceContract(blindsDefinition.getDualRelayId(), TinkerforgeDeviceClass.DualRelay.toString());
        DualRelayIntent intent = new DualRelayIntent();
        intent.selectedRelayStates = new HashSet();
        intent.monoflopParameters = new HashSet();
        intent.monoflopParameters.add(new DeviceMonoflopParameters((short) 1, true, 1000 * 180));

        subscribe(this.dualRelayServiceContract.STATUS_STATE, (String topic, byte[] payload) -> {
            synchronized (synchronizationObject) {
                state = new TreeSet<RelayStateStatus>(toMessageSet(payload, RelayStateStatus.class)).last().value;
                synchronizationObject.notifyAll();
            }
        });
        subscribe(getContract().INTENT + "/#", (topic, payload) -> {
            Set<BlindsServantIntent> blindsIntents = toMessageSet(payload, BlindsServantIntent.class);
            for (BlindsServantIntent blindsIntent : blindsIntents) {
                if (blindsIntent != null) {
                    if (blindsIntent.getDirection() == BlindsDirection.stop) {
                        DeviceSelectedRelayState selectedState1 = new DeviceSelectedRelayState((short) 1, false);
                        intent.selectedRelayStates.add(selectedState1);
                        super.getPublishingCollector().readyToPublish(dualRelayServiceContract.INTENT + "/blindsServant" + blindsDefinition.getId(), intent);
                    }
                    if (blindsIntent.getDirection() == BlindsDirection.up) {
                        DeviceRelayState desiredState = new DeviceRelayState(true, false);
                        synchronized (synchronizationObject) {
                            if (this.state != null && this.state.equals(desiredState)) {
                                return;
                            }
                            while (this.state == null || this.state.getRelay1()) {
                                DeviceSelectedRelayState powerState = new DeviceSelectedRelayState((short) 1, false);
                                intent.selectedRelayStates.clear();
                                intent.selectedRelayStates.add(powerState);
                                super.getPublishingCollector().readyToPublish(dualRelayServiceContract.INTENT + "/blindsServant" + blindsDefinition.getId(), intent);
                                synchronizationObject.wait(1000);
                            }
                            Thread.sleep(100);
                            while (this.state == null || this.state.getRelay2()) {
                                DeviceSelectedRelayState directionState = new DeviceSelectedRelayState((short) 2, false);
                                intent.selectedRelayStates.clear();
                                intent.selectedRelayStates.add(directionState);
                                super.getPublishingCollector().readyToPublish(dualRelayServiceContract.INTENT + "/blindsServant" + blindsDefinition.getId(), intent);
                                synchronizationObject.wait(1000);
                            }
                            Thread.sleep(100);
                            while (this.state == null || !this.state.getRelay1()) {
                                DeviceSelectedRelayState powerState = new DeviceSelectedRelayState((short) 1, true);
                                intent.selectedRelayStates.clear();
                                intent.selectedRelayStates.add(powerState);
                                super.getPublishingCollector().readyToPublish(dualRelayServiceContract.INTENT + "/blindsServant" + blindsDefinition.getId(), intent);
                                synchronizationObject.wait(1000);
                            }

                        }

                    }
                    if (blindsIntent.getDirection() == BlindsDirection.down) {
                        DeviceRelayState desiredState = new DeviceRelayState(true, true);
                        synchronized (synchronizationObject) {
                            if (this.state != null && this.state.equals(desiredState)) {
                                return;
                            }
                            while (this.state == null || this.state.getRelay1()) {
                                DeviceSelectedRelayState powerState = new DeviceSelectedRelayState((short) 1, false);
                                DualRelayIntent dualRelayIntent = new DualRelayIntent();
                                dualRelayIntent.selectedRelayStates.add(powerState);
                                super.getPublishingCollector().readyToPublish(dualRelayServiceContract.INTENT + "/blindsServant" + blindsDefinition.getId(), dualRelayIntent);
                                synchronizationObject.wait(1000);
                            }
                            Thread.sleep(100);
                            while (this.state == null || !this.state.getRelay2()) {
                                DeviceSelectedRelayState directionState = new DeviceSelectedRelayState((short) 2, true);
                                DualRelayIntent dualRelayIntent = new DualRelayIntent();
                                dualRelayIntent.selectedRelayStates.add(directionState);
                                super.getPublishingCollector().readyToPublish(dualRelayServiceContract.INTENT + "/blindsServant" + blindsDefinition.getId(), dualRelayIntent);
                                synchronizationObject.wait(1000);
                            }
                            Thread.sleep(100);
                            while (this.state == null || !this.state.getRelay1()) {
                                DeviceSelectedRelayState powerState = new DeviceSelectedRelayState((short) 1, false);
                                DualRelayIntent dualRelayIntent = new DualRelayIntent();
                                dualRelayIntent.selectedRelayStates.add(powerState);
                                super.getPublishingCollector().readyToPublish(dualRelayServiceContract.INTENT + "/blindsServant" + blindsDefinition.getId(), dualRelayIntent);
                                synchronizationObject.wait(1000);
                            }
                            DeviceMonoflopParameters stopMonoflop = new DeviceMonoflopParameters((short) 1, true, 1000 * 180);
                            DeviceMonoflopParameters directionMonoflop = new DeviceMonoflopParameters((short) 2, true, 1000 * 200);
                            DualRelayIntent dualRelayIntent = new DualRelayIntent();
                            dualRelayIntent.monoflopParameters.add(stopMonoflop);
                            dualRelayIntent.monoflopParameters.add(directionMonoflop);

                            super.getPublishingCollector().readyToPublish(dualRelayServiceContract.INTENT + "/blindsServant" + blindsDefinition.getId(), dualRelayIntent);
                        }

                    }
                }
            }
        });

        connect();

    }

    public BlindsDefinition getDefinition() {
        return definition;
    }

}
