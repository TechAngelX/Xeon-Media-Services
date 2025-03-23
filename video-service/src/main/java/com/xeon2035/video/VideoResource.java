// VideoResource.java
package com.xeon2035.video;

import com.xeon2035.video.utils.LogUtil;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

@Path("/video")
@RequestScoped
public class VideoResource {
    private static final Logger logger = Logger.getLogger(VideoResource.class.getName());


    @GET
    @Path("/{videoName}")
    @Produces("video/mp4")
    public Response getVideo(@PathParam("videoName") String videoName) {
        logger.info("Request received for video clip: " + videoName);
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("video-files/" + videoName);

            if (inputStream == null) {
                logger.warning("Video file not found: " + videoName);
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Video clip not found: " + videoName)
                        .build();
            }

            byte[] videoData = inputStream.readAllBytes();
            inputStream.close();

            logger.info("Video clip file found: " + videoName + ", size: " + videoData.length + " bytes");

            return Response.ok(videoData)
                    .type("video/mp4")
                    .build();
        } catch (IOException e) {
            logger.severe("Error loading video: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error loading video: " + e.getMessage())
                    .build();
        }
    }}
