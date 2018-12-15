package it.unibo.iot.domain.impl.support;

public class ServiceRef {
    private String serverHost;
    private int serverPort;
    private String protocol;

    public ServiceRef(String serverHost, int serverPort, String protocol) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.protocol = protocol;
    }

    public int getServerPort() {
        return serverPort;
    }

    public String getServerHost() {
        return serverHost;
    }

    public String getProtocol() {
        return protocol;
    }
}
