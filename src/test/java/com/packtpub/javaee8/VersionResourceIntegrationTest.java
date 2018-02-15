package com.packtpub.javaee8;

import org.glassfish.jersey.jsonb.JsonBindingFeature;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VersionResourceIntegrationTest {

    private Client client;
    private WebTarget webTarget;

    @Before
    public void setUp() throws Exception {
        client = ClientBuilder.newBuilder()
                .connectTimeout(5, TimeUnit.SECONDS).readTimeout(5, TimeUnit.SECONDS)
                .register(JsonBindingFeature.class)
                .build();

        webTarget = client.target("http://localhost:8080").path("/content-service/api").path("/version");
    }

    @After
    public void tearDown() throws Exception {
        client.close();
    }

    @Test
    public void v1UsingMediaType() {
        Response response = webTarget.request().accept(VersionResource.V1).get();

        MediaType mediaType = response.getMediaType();
        assertThat(mediaType).isEqualTo(VersionResource.V1);

        Map<String, String> body = response.readEntity(genericMap());
        assertThat(body).containsEntry("version", "v1");
    }

    @Test
    public void v1UsingApplicationJson() {
        Response response = webTarget.request(MediaType.APPLICATION_JSON).get();

        MediaType mediaType = response.getMediaType();
        assertThat(mediaType.toString()).isEqualTo(MediaType.APPLICATION_JSON);

        Map<String, String> body = response.readEntity(genericMap());
        assertThat(body).containsEntry("version", "v1");
    }

    @Test
    public void usingAnyMediaType() {
        Response response = webTarget.request(MediaType.WILDCARD).get();

        MediaType mediaType = response.getMediaType();
        assertThat(mediaType).isEqualTo(VersionResource.V1);

        Map<String, String> body = response.readEntity(genericMap());
        assertThat(body).containsEntry("version", "v1");
    }

    @Test
    public void v2UsingMediaType() {
        Response response = webTarget.request().accept(VersionResource.V2).get();

        MediaType mediaType = response.getMediaType();
        assertThat(mediaType).isEqualTo(VersionResource.V2);

        Map<String, String> body = response.readEntity(genericMap());
        assertThat(body).containsEntry("version", "v2");
    }

    private GenericType<Map<String, String>> genericMap() {
        return new GenericType<Map<String, String>>() {
        };
    }
}