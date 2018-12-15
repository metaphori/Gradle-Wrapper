package it.unibo.iot.domain.impl.support;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unibo.iot.domain.interfaces.EventServicePort;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class EventServiceMqttPort implements EventServicePort {
    private static final Gson GSON = new GsonBuilder().create();
    private final BlockingQueue queue = new ArrayBlockingQueue(10);
    private final MqttClient client;

    public EventServiceMqttPort(String serverURI, String clientId) throws MqttException {
        client = new MqttClient(serverURI, clientId, new MemoryPersistence());
        MqttConnectOptions opts = new MqttConnectOptions();
        opts.setCleanSession(true);
        client.connect(opts);
        client.subscribe(GlobalConfig.ProductionActivityTopic);
        client.subscribe(GlobalConfig.ConsumptionActivityTopic);
        client.setCallback(new MqttCallback() {
            @Override  public void connectionLost(Throwable cause) {  }

            @Override  public void messageArrived(String topic, MqttMessage message) {
                try {
                    Object obj = GSON.fromJson(new String(message.getPayload()), Object.class);
                    queue.put(obj);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override  public void deliveryComplete(IMqttDeliveryToken token) { }
        });
    }

    @Override public Object getEvent() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }
}
