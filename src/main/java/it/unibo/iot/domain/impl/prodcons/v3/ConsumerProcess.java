package it.unibo.iot.domain.impl.prodcons.v3;

import it.unibo.iot.domain.impl.support.GlobalConfig;
import it.unibo.iot.domain.interfaces.*;
import it.unibo.iot.interaction.interfaces.Connection;
import it.unibo.iot.interaction.interfaces.ConnectionHandle;

import java.io.IOException;

public abstract class ConsumerProcess implements Consumer, Runnable {

    private final ConsumerPort port;
    private final ConsumptionSource source;

    public ConsumerProcess(ConsumerPort port, ConsumptionSource source) {
        this.port = port;
        this.source = source;
    }

    @Override
    public void run() {
        try {
            while(true){
                Object element = port.receiveElementForConsumption(source);
                consume(element);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
