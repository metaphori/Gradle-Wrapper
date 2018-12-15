package it.unibo.iot;

import it.unibo.iot.domain.impl.prodcons.v3.*;
import it.unibo.iot.domain.impl.support.EventEmitterFactory;
import it.unibo.iot.domain.impl.support.EventService;
import it.unibo.iot.domain.impl.support.EventServiceMqttPort;
import it.unibo.iot.domain.impl.support.GlobalConfig;
import it.unibo.iot.domain.interfaces.ConsumerPort;
import it.unibo.iot.domain.interfaces.EmitterFactory;
import it.unibo.iot.domain.interfaces.ProducerPort;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
        K k1 = new K(70);
        ProducerProcess prod1 = new BasicProducerProcess(pp1, null, () -> { k1.inc(5); return k1.value()<=85 ? new String("1-" + k1.value()) : null; });

        ProducerPort pp2 = new BasicMqttProducerPort(GlobalConfig.serverURI, "prod2");
        K k2 = new K(50);
        ProducerProcess prod2 = new BasicProducerProcess(pp2, null, () -> { k2.inc(5); return k2.value()<=60 ? new String("2-" + k2.value()) : null; });

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

        Map<String,List<String>> producedStuff = parseLog(lines, "Produce", "-");
        Map<String,List<String>> consumedStuff = parseLog(lines, "Consume", "/");
        consumedStuff.forEach((c,v) -> {
            producedStuff.forEach((p,pv) -> {
                Assert.assertEquals(v.stream().map(s -> s.split("/")[1]).filter(s -> s.startsWith(p)).collect(Collectors.toList()), pv);
            });
        });
    }

    private Map<String,List<String>> parseLog(List<String> lines, String filterLabel, String splitStr){
        return lines.stream()
                .filter(s -> s.contains(filterLabel))
                .map(s -> s.substring(s.lastIndexOf(" ")+1))
                .collect(Collectors.groupingBy(((String s) -> {
                    String[] parts = s.split(splitStr);
                    return parts[0];
                })));
    }

    public class K {
        private int k;

        public K(int k) {
            this.k = k;
        }

        public int value() {
            return k;
        }

        public void inc(int v) {
            this.k += v;
        }
    }
}
