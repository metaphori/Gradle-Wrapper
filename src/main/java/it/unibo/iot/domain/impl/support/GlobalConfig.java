package it.unibo.iot.domain.impl.support;

public class GlobalConfig {
    public static final int EventServicePort = 9999;
    public static final String EventServiceHost = "127.0.0.1";
    public static final String ProductionTopic = "prod";
    public static final String ConsumptionActivityTopic = "cons-activity";
    public static String ProductionActivityTopic = "prod-activity";
    public static String ACK = "ack";
    public static String serverURI = "tcp://127.0.0.1:1883";
}
