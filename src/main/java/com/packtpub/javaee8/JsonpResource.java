package com.packtpub.javaee8;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonPatch;
import javax.ws.rs.Consumes;
import javax.ws.rs.HEAD;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
@Path("json-p")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JsonpResource {

    private static final Logger LOGGER = Logger.getAnonymousLogger();

    private JsonArray jsonArray;

    @HEAD
    @PostConstruct
    public void initialize() {
        // TODO initialize me
        this.jsonArray = null;
    }

    // TODO add marshalling and unmarshalling

    // TODO add annotations
    public void patch(JsonArray jsonPatchArray) {
        LOGGER.log(Level.INFO, "Unmarshalled JSON-P Patch {0}.", jsonPatchArray);

        JsonPatch jsonPatch = Json.createPatchBuilder(jsonPatchArray).build();
        this.jsonArray = jsonPatch.apply(jsonArray);
        LOGGER.log(Level.INFO, "Patched {0}.", jsonArray);
    }
}
