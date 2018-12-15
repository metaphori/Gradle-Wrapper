package it.unibo.iot.domain.impl.support;

import it.unibo.iot.domain.interfaces.EventServicePort;
import it.unibo.iot.interaction.impl.ZMQConnectionFactories;
import it.unibo.iot.interaction.interfaces.ConnectionHandle;

import java.io.IOException;

public class EventServiceServerPort implements EventServicePort {
    ConnectionHandle handle;

    public EventServiceServerPort(int port) throws Exception {
        handle = ZMQConnectionFactories.PubSub.connection().connectAsServer(port);
    }

    @Override public Object getEvent() {
        try {
            return handle.receive();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }
}
