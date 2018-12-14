package it.unibo.iot.interaction.impl;

import it.unibo.iot.interaction.interfaces.ConnectionFactory;

public class ZMQConnectionFactories  {
    public static final ConnectionFactory PubSub = () -> new ZMQConnectionPubSub();
    public static final ConnectionFactory ReqRep = () -> new ZMQConnectionReqRep();
    public static final ConnectionFactory Pair = () -> new ZMQConnectionPair();
}
