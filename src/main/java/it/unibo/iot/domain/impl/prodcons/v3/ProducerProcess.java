package it.unibo.iot.domain.impl.prodcons.v3;

import it.unibo.iot.domain.interfaces.Producer;
import it.unibo.iot.domain.interfaces.ProducerPort;
import it.unibo.iot.domain.interfaces.ProductionTarget;

public abstract class ProducerProcess implements Producer, Runnable {
    private ProducerPort port;
    private final ProductionTarget target;

    public ProducerProcess(ProducerPort port, ProductionTarget target) {
        this.port = port;
        this.target = target;
    }

    @Override
    public void run() {
        try {
            while (!done()) {
                Object product = produce();
                port.sendProduction(target, product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
