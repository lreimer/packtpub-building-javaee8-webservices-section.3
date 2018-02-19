package com.packtpub.javaee8;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonPatch;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
@Path("json-p")
@Produces(MediaType.APPLICATION_JSON)
public class JsonpResource {

    private static final Logger LOGGER = Logger.getAnonymousLogger();

    private JsonArray jsonArray;

    @PostConstruct
    @HEAD
    public void initialize() {
        // TODO initialize me
        this.jsonArray = null;
    }

    // TODO add marshalling and unmarshalling


    @PATCH
    @Consumes(MediaType.APPLICATION_JSON_PATCH_JSON)
    public void patch(JsonArray jsonPatchArray) {
        LOGGER.log(Level.INFO, "Unmarshalled JSON-P Patch {0}.", jsonPatchArray);

        JsonPatch jsonPatch = Json.createPatchBuilder(jsonPatchArray).build();
        this.jsonArray = jsonPatch.apply(jsonArray);
        LOGGER.log(Level.INFO, "Patched {0}.", jsonArray);
    }
}
