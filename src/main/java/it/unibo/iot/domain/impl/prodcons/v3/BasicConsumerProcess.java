package it.unibo.iot.domain.impl.prodcons.v3;

import it.unibo.iot.domain.interfaces.ConsumerPort;
import it.unibo.iot.domain.interfaces.ConsumptionSource;

import java.util.function.Consumer;

public class BasicConsumerProcess extends ConsumerProcess{

    private final Consumer<Object> consumeLogic;

    public BasicConsumerProcess(ConsumerPort port, ConsumptionSource source, Consumer<Object> consumeLogic) {
        super(port, source);
        this.consumeLogic = consumeLogic;
    }

    @Override
    public void consume(Object element) {
        consumeLogic.accept(element);
    }
}
