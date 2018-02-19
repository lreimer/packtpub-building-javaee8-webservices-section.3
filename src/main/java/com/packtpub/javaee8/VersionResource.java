package com.packtpub.javaee8;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("version")
public class VersionResource {

    /**
     * MediaType implementation for the version resource in v1.
     */
    public static final MediaType V1 = null;

    /**
     * MediaType implementation for the version resource in v2.
     */
    public static final MediaType V2 = null;

    @GET
    @Produces("TODO")
    public Response v2() {
        // TODO Implement me
        return null;
    }

    @GET
    @Produces("TODO")
    public Response v1() {
        // TODO Implement me
        return null;
    }

}
