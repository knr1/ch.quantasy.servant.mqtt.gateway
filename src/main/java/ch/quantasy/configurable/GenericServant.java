/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.quantasy.configurable;

import ch.quantasy.mqtt.gateway.client.GatewayClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 *
 * @author reto
 */
public class GenericServant {

    private transient GatewayClient<GenericServantContract> gatewayClient;
    private Map<String, String> subscriptions;
    private Map<String, String> publications;
    //private Map<String, Action> actions;
    //private Map<String, Condition> conditions;
    //private Map<String, Term> terms;

    public GenericServant(URI mqttURI, String id) throws MqttException {
        gatewayClient = new GatewayClient<>(mqttURI, "ConfigurableServant" + id, new GenericServantContract("ConfigurableServant", id));
        subscriptions = new TreeMap<>();
        publications = new TreeMap<>();
        gatewayClient.connect();
    }

    /**
     * Only absolute subscriptions allowed
     *
     * @param id
     * @param topic
     */
    public void addSubscription(String id, String topic) {
        subscriptions.put(id, topic);
        gatewayClient.subscribe(topic, (topicIn, payload) -> {
            JsonNode node = gatewayClient.getMapper().readTree(payload);
            System.out.println("Topic: " + topicIn + "    Node: " + node);
            JsonNode matchingNode = node.get(0).at("/value"); //First element in array, field  '/value' oder explizit
            //JsonNode matchingNode = node.get(0).get("value"); //First element in array, field  'value'
            
            System.out.println("Matching Node: " + matchingNode);

            Iterator<JsonNode> innerNodeIterator = node.elements();
            while (innerNodeIterator.hasNext()) {
                JsonNode innerNode = innerNodeIterator.next();
                System.out.println("InnerNode: " + innerNode);

            }

        });
    }

    public void removeSubscription(String id) {
        subscriptions.remove(id);
    }

    /**
     * Only absolute subscriptions allowed
     *
     * @param id
     * @param topic
     */
    public void addPublication(String id, String topic) {
        publications.put(id, topic);
    }

    public void removePublication(String id, String topic) {
        publications.remove(id);
    }

    @Override
    public String toString() {
        String returnString = null;
        try {
            returnString = gatewayClient.getMapper().writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(GenericServant.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnString;
    }

    public static String computerName;

    static {
        try {
            computerName = java.net.InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            Logger.getLogger(GenericServant.class.getName()).log(Level.SEVERE, null, ex);
            computerName = "undefined";
        }
    }

    public static void main(String[] args) throws MqttException, IOException {
        URI mqttURI = URI.create("tcp://127.0.0.1:1883");

        GenericServant s = new GenericServant(mqttURI, computerName);
        s.addSubscription("All", "Timer/Tick/prisma/E/tick/pipipi");
        // s.addSubscription("motion1", "TF/MotionDetection/ASDF/E/motionDetected");
        // s.addSubscription("motion2", "TF/MotionDetection/JKLÖ/E/motionDetected");
        // s.addPublication("timer", "Timer/Ticky/prisma/tick");
        System.out.println(s.toString());
        System.in.read();
    }

}
