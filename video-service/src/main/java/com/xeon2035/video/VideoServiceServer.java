package com.xeon2035.video;

import com.xeon2035.video.utils.LogUtil;

import com.xeon2035.video.utils.ClearTerminal;
import io.helidon.microprofile.server.Server;

import java.io.IOException;
import java.util.logging.Logger;

public final class VideoServiceServer {

    private static final Logger LOGGER = LogUtil.getLogger(VideoServiceServer.class);

    
    public static void main(final String[] args) throws IOException {
        // Start the video server on a dynamic port
        Server server = startServer();

        // Log the dynamically assigned port
        LOGGER.info("Video Server started on http://localhost:" + server.port());

        // Log endpoints with the dynamic port
        LOGGER.info(String.format("\nEndpoints available: \n- Videos: http://localhost:%d/video/{videoName}", server.port()));
    }

    static Server startServer() {
        ClearTerminal.clearTerminal();

        // Get port from system property with fallback to 0 (for dynamic port allocation)
        int port = Integer.parseInt(System.getProperty("server.port", "0"));

        // Start server on the dynamic port (0 means OS will choose an available port)
        Server server = Server.builder().port(port).build();

        // Start the server and return the instance
        server.start();
        return server;
    }
}
