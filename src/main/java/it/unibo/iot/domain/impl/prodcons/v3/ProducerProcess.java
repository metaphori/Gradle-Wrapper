package it.unibo.iot.domain.impl.prodcons.v3;

import it.unibo.iot.domain.interfaces.Producer;
import it.unibo.iot.domain.interfaces.ProducerPort;
import it.unibo.iot.domain.interfaces.ProductionTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public abstract class ProducerProcess implements Producer, Runnable {
    private static final Logger L = LoggerFactory.getLogger(ProducerProcess.class);
    private ProducerPort port;
    private final ProductionTarget target;

    public ProducerProcess(ProducerPort port, ProductionTarget target) {
        this.port = port;
        this.target = target;
    }

    @Override
    public void run() {
        try {
            L.info("Starting to produce");
            while (!done()) {
                Object product = produce();
                port.sendProduction(target, product);
                Thread.sleep(500);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
