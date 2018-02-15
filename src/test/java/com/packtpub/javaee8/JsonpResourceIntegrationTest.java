package com.packtpub.javaee8;

import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.json.Json;
import javax.json.JsonArray;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JsonpResourceIntegrationTest {

    private Client client;
    private WebTarget webTarget;

    @Before
    public void setUp() throws Exception {
        client = ClientBuilder.newBuilder()
                .property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true)
                .connectTimeout(5, TimeUnit.SECONDS).readTimeout(5, TimeUnit.SECONDS)
                .build();

        webTarget = client.target("http://localhost:8080").path("/content-service/api").path("/json-p");
    }

    @After
    public void tearDown() throws Exception {
        client.close();
    }

    @Test
    public void _1_GetJsonp() {
        JsonArray jsonArray = webTarget.request().accept(MediaType.APPLICATION_JSON).get(JsonArray.class);
        assertThat(jsonArray.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    public void _2_PostJsonp() {
        JsonArray jsonArray = Json.createArrayBuilder()
                .add(Json.createObjectBuilder()
                        .add("aString", "Updated Json-P")
                        .add("aInteger", 23)
                        .add("aBoolean", true)
                        .add("aNullValue", "Not NULL"))
                .build();

        Response response = webTarget.request().post(Entity.json(jsonArray));
        assertThat(response.getStatus()).isEqualTo(204);
    }

    @Test
    public void _3_GetJsonpAfterPost() {
        JsonArray jsonArray = webTarget.request().accept(MediaType.APPLICATION_JSON).get(JsonArray.class);
        assertThat(jsonArray.getJsonObject(0).getString("aString")).isEqualTo("Updated Json-P");
    }

    @Test
    public void _4_PatchJsonp() {
        JsonArray jsonArray = Json.createArrayBuilder()
                .add(Json.createObjectBuilder()
                        .add("op", "replace")
                        .add("path", "/0/aString")
                        .add("value", "Patched Json-P"))
                .build();

        Response response = webTarget.request().method(HttpMethod.PATCH, Entity.entity(jsonArray, MediaType.APPLICATION_JSON_PATCH_JSON));
        assertThat(response.getStatus()).isEqualTo(204);
    }

    @Test
    public void _5_GetJsonpAfterPatch() {
        JsonArray jsonArray = webTarget.request().accept(MediaType.APPLICATION_JSON).get(JsonArray.class);
        assertThat(jsonArray.getJsonObject(0).getString("aString")).isEqualTo("Patched Json-P");
    }

    @Test
    public void _99_Reset() {
        Response response = webTarget.request().head();
        assertThat(response.getStatus()).isEqualTo(204);
    }
}