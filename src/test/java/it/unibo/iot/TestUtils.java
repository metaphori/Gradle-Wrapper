package it.unibo.iot;

import java.io.IOException;
import java.net.ServerSocket;

public class TestUtils {
    public static Integer getEphemeralPort() throws IOException {
        try (
                ServerSocket socket = new ServerSocket(0);
        ) {
            return socket.getLocalPort();
        }
    }
}
