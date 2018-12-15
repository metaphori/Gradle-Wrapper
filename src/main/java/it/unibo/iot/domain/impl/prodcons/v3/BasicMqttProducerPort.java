package it.unibo.iot.domain.impl.prodcons.v3;

import com.google.gson.Gson;
import it.unibo.iot.domain.impl.support.GlobalConfig;
import it.unibo.iot.domain.interfaces.ProducerPort;
import it.unibo.iot.domain.interfaces.ProductionTarget;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class BasicMqttProducerPort implements ProducerPort {
    private MqttClient client;

    public BasicMqttProducerPort(String serverURI, String clientId) throws MqttException {
        client = new MqttClient(serverURI, clientId, new MemoryPersistence());
        MqttConnectOptions opts = new MqttConnectOptions();
        opts.setCleanSession(true);
        client.connect(opts);
    }

    @Override
    public void sendProduction(ProductionTarget target, Object product) {
        byte[] msg = new Gson().toJson(product).getBytes();
        try {
            byte[] log = new Gson().toJson("Produce " +product).getBytes();
            client.publish(GlobalConfig.ProductionActivityTopic, new MqttMessage(log));

            client.publish(GlobalConfig.ProductionTopic, new MqttMessage(msg));
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
