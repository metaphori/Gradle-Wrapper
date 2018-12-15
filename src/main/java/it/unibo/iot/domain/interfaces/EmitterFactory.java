package it.unibo.iot.domain.interfaces;

public interface EmitterFactory {
    Emitter createEmitter(String name, String host, int port);
}
