package it.unibo.iot.domain.system;

import it.unibo.iot.domain.impl.prodcons.v2.ConsumerServerWithAck;
import it.unibo.iot.domain.impl.prodcons.v2.ProducerClientWithAck;
import it.unibo.iot.domain.impl.support.GlobalConfig;
import it.unibo.iot.domain.impl.support.LogEmitterFactory;
import it.unibo.iot.domain.interfaces.Configurator;
import it.unibo.iot.domain.interfaces.EmitterFactory;
import it.unibo.iot.interaction.impl.ZMQConnectionReqRep;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class ConfiguratorOneToOneClientServerWithAck implements Configurator {
    private Runnable producerClient;
    private Runnable consumerServer;
    private int bufferCapacity;
    private static final int port = 8001;
    private static final String host = "127.0.0.1";

    public static void main(String[] args) throws InterruptedException {
        ConfiguratorOneToOneClientServerWithAck configurator = new ConfiguratorOneToOneClientServerWithAck(10);
        configurator.setup();
        configurator.start();
        Thread.sleep(TimeUnit.SECONDS.toMillis(5));
        configurator.teardown();
    }

    public ConfiguratorOneToOneClientServerWithAck(int bufferCapacity) {
        this.bufferCapacity = bufferCapacity;
    }

    @Override public void setup(){
        EmitterFactory ef = new LogEmitterFactory();
        consumerServer = new ConsumerServerWithAck(ef.createEmitter("cons-emitter",GlobalConfig.EventServiceHost, GlobalConfig.EventServicePort), new ZMQConnectionReqRep(), port);
        producerClient = new ProducerClientWithAck(ef.createEmitter("prod-emitter",GlobalConfig.EventServiceHost, GlobalConfig.EventServicePort), new ZMQConnectionReqRep(), host, port);
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
