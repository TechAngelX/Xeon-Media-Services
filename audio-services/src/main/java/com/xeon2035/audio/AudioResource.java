// AudioResource.java
package com.xeon2035.audio;

import com.xeon2035.audio.utils.LogUtil;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

@Path("/audio")
@RequestScoped
public class AudioResource {
    private static final Logger logger = Logger.getLogger(AudioResource.class.getName());


    @GET
    @Path("/{audioName}")
    @Produces("audio/mp4")
    public Response getAudio(@PathParam("audioName") String audioName) {
        logger.info("Request received for audio clip: " + audioName);
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("audio-files/" + audioName);

            if (inputStream == null) {
                logger.warning("Audio file not found: " + audioName);
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Audio clip not found: " + audioName)
                        .build();
            }

            byte[] audioData = inputStream.readAllBytes();
            inputStream.close();

            logger.info("Audio clip file found: " + audioName + ", size: " + audioData.length + " bytes");

            return Response.ok(audioData)
                    .type("audio/mp4")
                    .build();
        } catch (IOException e) {
            logger.severe("Error loading audio: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error loading audio: " + e.getMessage())
                    .build();
        }
    }}
