package it.unibo.iot.domain.impl.support;

import it.unibo.iot.domain.interfaces.Emitter;
import it.unibo.iot.interaction.impl.ZMQConnectionFactories;
import it.unibo.iot.interaction.interfaces.ConnectionHandle;

import java.io.IOException;

public class EventEmitter implements Emitter {
    private ConnectionHandle handle;

    public EventEmitter(String host, int port) throws IOException {
        handle = ZMQConnectionFactories.PubSub.connection().connectAsClient(host, port);
    }

    @Override
    public void emit(String event) {
        try {
            handle.send(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
