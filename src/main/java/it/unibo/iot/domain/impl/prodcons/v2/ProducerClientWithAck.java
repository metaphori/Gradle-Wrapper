package it.unibo.iot.domain.impl.prodcons.v2;

import it.unibo.iot.domain.impl.support.GlobalConfig;
import it.unibo.iot.domain.interfaces.Emitter;
import it.unibo.iot.domain.interfaces.EmitterFactory;
import it.unibo.iot.domain.interfaces.Producer;
import it.unibo.iot.interaction.interfaces.Connection;
import it.unibo.iot.interaction.interfaces.ConnectionHandle;

import java.util.concurrent.TimeUnit;

public class ProducerClientWithAck implements Producer, Runnable {
    private Emitter E;
    private Connection connection;
    private final String host;
    private final int port;
    private int k = 0;

    public ProducerClientWithAck(Emitter emitter, Connection connection, String host, int port) {
        this.E = emitter;
        this.connection = connection;
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            ConnectionHandle ch = connection.connectAsClient(host, port);
            Thread.sleep(TimeUnit.MILLISECONDS.toMillis(1000));
            while (!done()) {
                ch.send(produce().toString());
                String msg = ch.receive();
                E.emit("Got ACK");
                if(!msg.equals(GlobalConfig.ACK)) throw new Exception("I was expecting an ACK and I got " + msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object produce() {
        k++;
        E.emit("Produce " + k);
        return k;
    }

    @Override
    public boolean done() {
        return k > 3;
    }
}
