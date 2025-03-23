

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Path("/images")
@RequestScoped
public class ImageResource {

    
    // For PNGs
    @GET
    @Path("/png/{imageName}")
    @Produces("image/png")
    public Response getPngImage(@PathParam("imageName") String imageName) throws IOException {
        // Construct the path to the requested image
        File imageFile = new File("src/main/resources/images/png/" + imageName);

        // Ensure the file exists
        if (!imageFile.exists()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Image not found: " + imageName).build();
        }

        // Create an InputStream to read the file
        InputStream inputStream = new FileInputStream(imageFile);

        // Return the image as a response with the correct content type
        return Response.ok(inputStream)
                .header("Content-Type", "image/png")
                .build();
    }

    // For JPEGs
    @GET
    @Path("/jpg/{imageName}")
    @Produces("image/jpeg")
    public Response getJpgImage(@PathParam("imageName") String imageName) throws IOException {
        File imageFile = new File("src/main/resources/images/jpg/" + imageName);

        if (!imageFile.exists()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Image not found: " + imageName).build();
        }

        InputStream inputStream = new FileInputStream(imageFile);

        return Response.ok(inputStream)
                .header("Content-Type", "image/jpeg")
                .build();
    }

    // For GIFs
    @GET
    @Path("/gif/{imageName}")
    @Produces("image/gif")
    public Response getGifImage(@PathParam("imageName") String imageName) throws IOException {
        File imageFile = new File("src/main/resources/images/gif/" + imageName);

        if (!imageFile.exists()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Image not found: " + imageName).build();
        }

        InputStream inputStream = new FileInputStream(imageFile);

        return Response.ok(inputStream)
                .header("Content-Type", "image/gif")
                .build();
    }

    // For Webp
    @GET
    @Path("/webp/{imageName}")
    @Produces("image/webp")
    public Response getWebpImage(@PathParam("imageName") String imageName) throws IOException {
        File imageFile = new File("src/main/resources/images/webp/" + imageName);

        if (!imageFile.exists()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Image not found: " + imageName).build();
        }

        InputStream inputStream = new FileInputStream(imageFile);

        return Response.ok(inputStream)
                .header("Content-Type", "image/webp")
                .build();
    }
}
