
import com.xeon2035.audio.utils.LogUtil;
import jakarta.enterprise.context.RequestScoped;
import jakarta.json.Json;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.Map;
import java.util.logging.Logger;

@Path("/ricki")
@RequestScoped
public class GreetResource {

    // Logger for the GreetResource class
    public static final Logger LOGGER = LogUtil.getLogger(GreetResource.class);

    private static final JsonBuilderFactory JSON = Json.createBuilderFactory(Map.of());

    public GreetResource() {
        // Log when the resource is initialized
        LOGGER.info("GreetResource initialized. Ready to handle requests.");
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getDefaultMessage() {
        // Log when the GET request is received
        LOGGER.info("GET request received at /ricki");

        return JSON.createObjectBuilder()
                .add("message", "Hello World")
                .build();
    }
}
