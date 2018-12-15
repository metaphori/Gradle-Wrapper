package it.unibo.iot.domain.impl.prodcons.v1;

import it.unibo.iot.domain.interfaces.Consumer;
import it.unibo.iot.domain.interfaces.Emitter;
import it.unibo.iot.domain.interfaces.EmitterFactory;
import it.unibo.iot.interaction.interfaces.Connection;
import it.unibo.iot.interaction.interfaces.ConnectionHandle;

import java.io.IOException;

public class ConsumerServer implements Consumer, Runnable {
    private Emitter E;
    private Connection connection;
    private int port;

    public ConsumerServer(Emitter emitter, Connection connection, int port) {
        this.E = emitter;
        this.connection = connection;
        this.port = port;
    }

    @Override
    public void consume(Object element) {
        E.emit("Consume " + element);
    }

    @Override
    public void run() {
        try {
            ConnectionHandle ch = connection.connectAsServer(this.port);
            while(true){
                String element = ch.receive();
                consume(element);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
