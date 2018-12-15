package it.unibo.iot.domain.impl.support;

import it.unibo.iot.domain.interfaces.Emitter;
import it.unibo.iot.domain.interfaces.EmitterFactory;

import java.io.IOException;

public class EventEmitterFactory implements EmitterFactory {
    @Override
    public Emitter createEmitter(String name, String host, int port) {
        try {
            return new EventEmitter(host, port);
        } catch (IOException e) {
            return new LogEmitter(name + "-ERROR");
        }
    }
}
