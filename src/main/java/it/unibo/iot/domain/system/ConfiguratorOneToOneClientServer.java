package it.unibo.iot.domain.system;

import it.unibo.iot.domain.impl.prodcons.v1.ConsumerServer;
import it.unibo.iot.domain.impl.prodcons.v1.ProducerClient;
import it.unibo.iot.domain.impl.support.GlobalConfig;
import it.unibo.iot.domain.impl.support.LogEmitterFactory;
import it.unibo.iot.domain.interfaces.Configurator;
import it.unibo.iot.domain.interfaces.EmitterFactory;
import it.unibo.iot.interaction.impl.ZMQConnectionFactories;
import it.unibo.iot.interaction.interfaces.ConnectionFactory;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class ConfiguratorOneToOneClientServer implements Configurator {
    private Runnable producerClient;
    private Runnable consumerServer;
    private int bufferCapacity;
    ConnectionFactory connectionFactory;
    private static final int port = 8001;
    private static final String host = "127.0.0.1";

    public static void main(String[] args) throws InterruptedException {
        //ConnectionFactory factory = new TCPConnectionFactory();
        ConnectionFactory factory = ZMQConnectionFactories.PubSub;
        ConfiguratorOneToOneClientServer configurator = new ConfiguratorOneToOneClientServer(10, factory);
        configurator.setup();
        configurator.start();
        Thread.sleep(TimeUnit.SECONDS.toMillis(5));
        configurator.teardown();
    }

    public ConfiguratorOneToOneClientServer(int bufferCapacity, ConnectionFactory cf) {
        this.bufferCapacity = bufferCapacity;
        this.connectionFactory = cf;
    }

    @Override public void setup(){
        EmitterFactory ef = new LogEmitterFactory();
        consumerServer = new ConsumerServer(ef.createEmitter("cons-emitter", GlobalConfig.EventServiceHost, GlobalConfig.EventServicePort), connectionFactory.connection(), port);
        producerClient = new ProducerClient(ef.createEmitter("prod-emitter",GlobalConfig.EventServiceHost, GlobalConfig.EventServicePort), connectionFactory.connection(), host, port);
    }

    @Override public void start(){
        ForkJoinPool.commonPool().execute(consumerServer);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ForkJoinPool.commonPool().execute(producerClient);
    }

    @Override public void teardown(){
        System.exit(0);
    }
}
