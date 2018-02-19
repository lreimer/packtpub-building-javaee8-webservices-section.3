package com.packtpub.javaee8;

import org.glassfish.jersey.media.multipart.MultiPartFeature;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Configures a JAX-RS endpoint.
 */
@ApplicationPath("api")
public class JAXRSConfiguration extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();

        classes.add(DocumentsResource.class);
        classes.add(MultiPartFeature.class);

        classes.add(JsonbResource.class);
        classes.add(JsonpResource.class);
        classes.add(VersionResource.class);

        return classes;
    }
}
