package it.unibo.iot;

import it.unibo.iot.domain.impl.prodcons.v3.*;
import it.unibo.iot.domain.impl.support.EventEmitterFactory;
import it.unibo.iot.domain.impl.support.EventService;
import it.unibo.iot.domain.impl.support.EventServiceMqttPort;
import it.unibo.iot.domain.impl.support.GlobalConfig;
import it.unibo.iot.domain.interfaces.ConsumerPort;
import it.unibo.iot.domain.interfaces.EmitterFactory;
import it.unibo.iot.domain.interfaces.ProducerPort;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ManyToManyTest {
    private EventService evs;

    @Before public void setup() throws Exception {
        final ExecutorService es = Executors.newCachedThreadPool();

        EmitterFactory ef = new EventEmitterFactory();
        EventServiceMqttPort esp = new EventServiceMqttPort(GlobalConfig.serverURI, "event-serv");
        evs = new EventService(esp);
        es.execute(evs);

        ConsumerPort cp1 = new BasicMqttConsumerPort(GlobalConfig.serverURI, "cons1");
        ConsumerProcess cons1 = new BasicConsumerProcess(cp1, null, (msg) -> System.out.println("Consuming1 " + msg));

        ConsumerPort cp2 = new BasicMqttConsumerPort(GlobalConfig.serverURI, "cons2");
        ConsumerProcess cons2 = new BasicConsumerProcess(cp2, null, (msg) -> System.out.println("Consuming2 " + msg));

        ProducerPort pp1 = new BasicMqttProducerPort(GlobalConfig.serverURI, "prod1");
        ProducerProcess prod1 = new BasicProducerProcess(pp1, null, () -> new String("product1"));

        ProducerPort pp2 = new BasicMqttProducerPort(GlobalConfig.serverURI, "prod2");
        ProducerProcess prod2 = new BasicProducerProcess(pp2, null, () -> new String("product2"));

        es.execute(cons1);
        es.execute(cons2);
        es.execute(prod1);
        es.execute(prod2);

        es.shutdown();
        es.awaitTermination(2, TimeUnit.SECONDS);
    }

    @Test public void basicTest() throws IOException {
        List<String> lines = evs.getAllEvents();

        lines.forEach(System.out::println);

        ProducerConsumerAssertions.assertOrderedConsumptionOnLog(lines);
    }

}
