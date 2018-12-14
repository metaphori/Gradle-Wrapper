package it.unibo.iot.domain.system;

import it.unibo.iot.domain.impl.prodcons.v3.BasicConsumerProcess;
import it.unibo.iot.domain.impl.prodcons.v3.BasicMqttConsumerPort;
import it.unibo.iot.domain.impl.prodcons.v3.BasicMqttProducerPort;
import it.unibo.iot.domain.impl.prodcons.v3.BasicProducerProcess;
import it.unibo.iot.domain.impl.support.GlobalConfig;
import it.unibo.iot.domain.impl.support.LogEmitterFactory;
import it.unibo.iot.domain.interfaces.*;
import it.unibo.iot.interaction.interfaces.ConnectionFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class ConfiguratorManyToMany implements Configurator {
    private Runnable cons1, cons2, prod1, prod2;
    private int bufferCapacity;
    ConnectionFactory connectionFactory;
    private static final int port = 8001;
    private static final String host = "127.0.0.1";

    public static void main(String[] args) throws InterruptedException {
        ConfiguratorManyToMany configurator = new ConfiguratorManyToMany();
        configurator.setup();
        configurator.start();
        Thread.sleep(TimeUnit.HOURS.toMillis(5));
        configurator.teardown();
    }


    @Override public void setup(){
        try {
            EmitterFactory ef = new LogEmitterFactory();
            ConsumerPort cp1 = new BasicMqttConsumerPort(GlobalConfig.serverURI, "cons1");
            cons1 = new BasicConsumerProcess(cp1, null, (msg) -> System.out.println("Consuming1 " + msg));

            ConsumerPort cp2 = new BasicMqttConsumerPort(GlobalConfig.serverURI, "cons2");
            cons2 = new BasicConsumerProcess(cp2, null, (msg) -> System.out.println("Consuming2 " + msg));

            ProducerPort pp1 = new BasicMqttProducerPort(GlobalConfig.serverURI, "prod1");
            prod1 = new BasicProducerProcess(pp1, null, () -> new String("product1"));

            ProducerPort pp2 = new BasicMqttProducerPort(GlobalConfig.serverURI, "prod2");
            prod2 = new BasicProducerProcess(pp2, null, () -> new String("product2"));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override public void start(){
        ExecutorService es = ForkJoinPool.commonPool();
        es.execute(cons1);
        es.execute(cons2);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        es.execute(prod1);
        es.execute(prod2);
    }

    @Override public void teardown(){
        System.exit(0);
    }
}
