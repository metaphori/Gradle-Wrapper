package it.unibo.iot.domain.interfaces;

public interface ProducerPort {
    void sendProduction(ProductionTarget target, Object product);
}
