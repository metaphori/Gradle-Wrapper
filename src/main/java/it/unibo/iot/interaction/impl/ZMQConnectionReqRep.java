package it.unibo.iot.interaction.impl;

import org.zeromq.ZMQ;

public class ZMQConnectionReqRep extends ZMQConnection {

    protected int getClientSocketType(){ return ZMQ.REQ; };
    protected int getServerSocketType(){ return ZMQ.REP; };
}
