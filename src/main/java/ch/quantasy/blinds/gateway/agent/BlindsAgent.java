/*
 * /*
 *  *   "TiMqWay"
 *  *
 *  *    TiMqWay(tm): A gateway to provide an MQTT-View for the Tinkerforge(tm) world (Tinkerforge-MQTT-Gateway).
 *  *
 *  *    Copyright (c) 2016 Bern University of Applied Sciences (BFH),
 *  *    Research Institute for Security in the Information Society (RISIS), Wireless Communications & Secure Internet of Things (WiCom & SIoT),
 *  *    Quellgasse 21, CH-2501 Biel, Switzerland
 *  *
 *  *    Licensed under Dual License consisting of:
 *  *    1. GNU Affero General Public License (AGPL) v3
 *  *    and
 *  *    2. Commercial license
 *  *
 *  *
 *  *    1. This program is free software: you can redistribute it and/or modify
 *  *     it under the terms of the GNU Affero General Public License as published by
 *  *     the Free Software Foundation, either version 3 of the License, or
 *  *     (at your option) any later version.
 *  *
 *  *     This program is distributed in the hope that it will be useful,
 *  *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  *     GNU Affero General Public License for more details.
 *  *
 *  *     You should have received a copy of the GNU Affero General Public License
 *  *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *  *
 *  *
 *  *    2. Licensees holding valid commercial licenses for TiMqWay may use this file in
 *  *     accordance with the commercial license agreement provided with the
 *  *     Software or, alternatively, in accordance with the terms contained in
 *  *     a written agreement between you and Bern University of Applied Sciences (BFH),
 *  *     Research Institute for Security in the Information Society (RISIS), Wireless Communications & Secure Internet of Things (WiCom & SIoT),
 *  *     Quellgasse 21, CH-2501 Biel, Switzerland.
 *  *
 *  *
 *  *     For further information contact <e-mail: reto.koenig@bfh.ch>
 *  *
 *  *
 */
package ch.quantasy.blinds.gateway.agent;

import ch.quantasy.blinds.gateway.message.BlindsDirectionEvent;
import ch.quantasy.blinds.gateway.message.BlindsDefinition;
import ch.quantasy.blinds.gateway.message.BlindsManagerIntent;
import ch.quantasy.blinds.gateway.message.BlindsServantIntent;
import ch.quantasy.blinds.gateway.servant.BlindsServantContract;
import ch.quantasy.blinds.gateway.service.blinds.BlindsManagerContract;
import ch.quantasy.mqtt.gateway.client.GatewayClient;
import java.net.URI;
import org.eclipse.paho.client.mqttv3.MqttException;
import ch.quantasy.mqtt.gateway.client.message.MessageReceiver;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author reto
 */
public class BlindsAgent {

    private final Map<String, BlindsServantContract> blindsContractMap;
    private final BlindsManagerContract managerContract;

    private final GatewayClient<BlindsAgentContract> gatewayClient;

    public BlindsAgent(URI mqttURI) throws MqttException {
        managerContract = new BlindsManagerContract("Manager");
        gatewayClient = new GatewayClient(mqttURI, "44508uf450n", new BlindsAgentContract("Agent", "Blinds", "blindsAgent01"));
        connectRemoteServices(new BlindsDefinition("EG-01", "kN8"));
        connectRemoteServices(new BlindsDefinition("EG-02", "kMM"));
        connectRemoteServices(new BlindsDefinition("EG-03", "kDc"));
        connectRemoteServices(new BlindsDefinition("EG-04", "kJy"));
        blindsContractMap = new HashMap<>();
        blindsContractMap.put("EG-01", new BlindsServantContract("BlindsServant", "EG-01"));
        blindsContractMap.put("EG-02", new BlindsServantContract("BlindsServant", "EG-02"));
        blindsContractMap.put("EG-03", new BlindsServantContract("BlindsServant", "EG-03"));
        blindsContractMap.put("EG-04", new BlindsServantContract("BlindsServant", "EG-04"));

        gatewayClient.subscribe("WebView/RemoteSwitch/E/touched/blinds/#", new MessageReceiver() {
            @Override
            public void messageReceived(String topic, byte[] mm) throws Exception {

                SortedSet<BlindsDirectionEvent> blindsDirections = gatewayClient.toMessageSet(mm, BlindsDirectionEvent.class);
                BlindsDirectionEvent blindsDirection = blindsDirections.last();
                System.out.println("BlindsParameter: "+blindsDirection);
                BlindsServantContract contract = blindsContractMap.get(blindsDirection.id);
                if (contract != null) {
                    BlindsServantIntent intent=new BlindsServantIntent(blindsDirection.direction);
                    gatewayClient.getPublishingCollector().readyToPublish(contract.INTENT, intent);
                }
            }
        }
        );
        gatewayClient.connect();

    }

    private void connectRemoteServices(BlindsDefinition... blindsDefinitions) {
        for (BlindsDefinition blindsDefinition : blindsDefinitions) {
            gatewayClient.getPublishingCollector().readyToPublish(managerContract.INTENT, new BlindsManagerIntent(BlindsManagerIntent.Action.add,blindsDefinition));
        }
    }

    public static void main(String[] args) throws Throwable {
        //URI mqttURI = URI.create("tcp://smarthome01:1883");

        URI mqttURI = URI.create("tcp://127.0.0.1:1883");
        if (args.length > 0) {
            mqttURI = URI.create(args[0]);
        } else {
            System.out.printf("Per default, 'tcp://127.0.0.1:1883' is chosen.\nYou can provide another address as first argument i.e.: tcp://iot.eclipse.org:1883\n");
        }
        System.out.printf("\n%s will be used as broker address.\n", mqttURI);
        BlindsAgent agent = new BlindsAgent(mqttURI);
        System.in.read();
    }

}
