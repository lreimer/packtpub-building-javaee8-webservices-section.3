package com.packtpub.javaee8;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.enterprise.context.RequestScoped;
import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequestScoped
@Path("documents")
public class DocumentsResource {

    private static final Logger LOGGER = Logger.getAnonymousLogger();

    @Context
    private ServletContext context;
    @Context
    private UriInfo uriInfo;

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

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload(@FormDataParam("file") InputStream inputStream,
                           @FormDataParam("file") FormDataContentDisposition fileInfo) {
        String fileName = fileInfo.getFileName();
        saveFile(inputStream, fileName);

        URI uri = uriInfo.getBaseUriBuilder().path(DocumentsResource.class).path(fileName).build();
        return Response.created(uri).build();
    }

    private void saveFile(InputStream file, String name) {
        try {
            java.nio.file.Path path = FileSystems.getDefault().getPath("/tmp/", name);
            Files.copy(file, path);
        } catch (IOException ie) {
            LOGGER.log(Level.WARNING, "Unable to save file.", ie);
        }
    }
}
