package it.unibo.iot.domain.interfaces;

public interface ConsumerPort {
    Object receiveElementForConsumption(ConsumptionSource source);
}
