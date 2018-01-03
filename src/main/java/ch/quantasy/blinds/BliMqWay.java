/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.quantasy.blinds;

import ch.quantasy.blinds.gateway.service.blinds.BlindsManagerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.net.URI;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 *
 * @author reto
 */
public class BliMqWay {

    public static void main(String[] args) throws MqttException, InterruptedException, JsonProcessingException, IOException {
        //URI mqttURI = URI.create("tcp://smarthome01:1883");
        URI mqttURI = URI.create("tcp://127.0.0.1:1883");
        if (args.length > 0) {
            mqttURI = URI.create(args[0]);
        } else {
            System.out.printf("Per default, 'tcp://127.0.0.1:1883' is chosen.\nYou can provide another address as first argument i.e.: tcp://iot.eclipse.org:1883\n");
        }
        System.out.printf("\n%s will be used as broker address.\n", mqttURI);

        BlindsManagerService managerService = new BlindsManagerService(mqttURI);
        System.out.println("" + managerService);
        System.in.read();
    }
}
