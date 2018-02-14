package com.packtpub.javaee8;

import javax.enterprise.context.ApplicationScoped;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.File;

@ApplicationScoped
@Path("documents")
public class DocumentsResource {

    @Context
    private ServletContext context;

    @GET
    @Path("/me.jpg")
    @Produces("image/jpeg")
    public Response jpg() {
        String path = context.getRealPath("/me.jpg");
        return Response.ok(new File(path))
                .header("Content-Disposition", "attachment; filename=me.jpg")
                .build();
    }

    @GET
    @Path("/magic.gif")
    @Produces("image/gif")
    public Response gif() {
        String path = context.getRealPath("/magic.gif");
        return Response.ok(new File(path)).build();
    }
}
