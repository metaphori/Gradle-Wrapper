package it.unibo.iot.domain.impl.support;

import it.unibo.iot.interaction.impl.ZMQConnectionFactories;
import it.unibo.iot.interaction.interfaces.ConnectionHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class EventService implements Runnable {
    private final Logger L = LoggerFactory.getLogger(EventService.class);
    private final int port;
    private List<String> events;

    public EventService(int port) {
        this.port = port;
        events = new LinkedList<>();
    }

    @Override
    public void run() {
        try {
            ConnectionHandle handle = ZMQConnectionFactories.PubSub.connection().connectAsServer(port);
            String ev = null;
            while ((ev = handle.receive()) != null){
                L.info("Got event " + ev);
                events.add(ev);
            }
        } catch(Exception exc){
            exc.printStackTrace();
            L.error(exc.getMessage());
        }
    }

    public List<String> getAllEvents(){
        return events;
    }
}
