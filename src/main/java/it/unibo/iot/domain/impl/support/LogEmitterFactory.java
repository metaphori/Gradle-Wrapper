package it.unibo.iot.domain.impl.support;

import it.unibo.iot.domain.interfaces.Emitter;
import it.unibo.iot.domain.interfaces.EmitterFactory;

public class LogEmitterFactory implements EmitterFactory {
    @Override
    public Emitter createEmitter(String name, String host, int port) {
        return new LogEmitter(name);
    }
}
