// AudioServiceServer.java
package com.xeon2035.audio;

import com.xeon2035.audio.utils.LogUtil;
import com.xeon2035.audio.utils.ClearTerminal;
import io.helidon.microprofile.server.Server;

import java.io.IOException;
import java.util.logging.Logger;

public final class AudioServiceServer {

    private static final Logger LOGGER = LogUtil.getLogger(AudioServiceServer.class);

    private AudioServiceServer() {
    }

    public static void main(final String[] args) throws IOException {
        // Start the audio server
        ClearTerminal.clearTerminal();

        // Disable metrics programmatically
        System.setProperty("mp.metrics.disabled", "true");

        // Set server properties programmatically to ensure they're applied
        System.setProperty("server.port", "8085");
        System.setProperty("server.host", "0.0.0.0");
        System.setProperty("mp.health.enabled", "false");

        // Start server
        Server server = startServer();

        // Log the dynamically assigned port
        LOGGER.info("Audio Server started on http://localhost:" + server.port());

        // Log endpoints with the dynamic port
        LOGGER.info(String.format("\nEndpoints available: \n- Audio: http://localhost:%d/audio/{audioName}", server.port()));
    }

    static Server startServer() {
        return Server.create().start();
    }
}
