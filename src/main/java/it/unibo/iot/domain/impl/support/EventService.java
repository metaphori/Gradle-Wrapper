package it.unibo.iot.domain.impl.support;

import it.unibo.iot.domain.interfaces.EventServicePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class EventService implements Runnable {
    private EventServicePort esp;
    private final Logger L = LoggerFactory.getLogger(EventService.class);
    private List<String> events;

    public EventService(EventServicePort esp) {
        this.esp = esp;
        this.events = new LinkedList<>();
    }

    @Override
    public void run() {
        try {
            String ev = null;
            while ((ev = esp.getEvent().toString()) != null){
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
