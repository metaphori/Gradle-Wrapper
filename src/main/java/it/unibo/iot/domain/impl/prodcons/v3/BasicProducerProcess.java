package it.unibo.iot.domain.impl.prodcons.v3;

import it.unibo.iot.domain.interfaces.ProducerPort;
import it.unibo.iot.domain.interfaces.ProductionTarget;

import java.util.function.Supplier;

public class BasicProducerProcess extends ProducerProcess {
    private final Supplier<Object> supplier;
    private Object product = null;

    public BasicProducerProcess(ProducerPort port, ProductionTarget target, Supplier<Object> supplier) {
        super(port, target);
        this.supplier = supplier;
    }

    @Override
    public Object produce() {
        return product;
    }

    @Override
    public boolean done() {
        product = supplier.get();
        return product == null;
    }
}
