package it.unibo.iot.domain.impl.prodcons.v3;

import it.unibo.iot.domain.interfaces.Consumer;
import it.unibo.iot.domain.interfaces.ConsumerPort;
import it.unibo.iot.domain.interfaces.ConsumptionSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ConsumerProcess implements Consumer, Runnable {
    private static final Logger L = LoggerFactory.getLogger(ConsumerProcess.class);

    private final ConsumerPort port;
    private final ConsumptionSource source;

    public ConsumerProcess(ConsumerPort port, ConsumptionSource source) {
        this.port = port;
        this.source = source;
    }

    @Override
    public void run() {
        try {
            L.info("Starting to consume");
            while(true){
                Object element = port.receiveElementForConsumption(source);
                consume(element);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
