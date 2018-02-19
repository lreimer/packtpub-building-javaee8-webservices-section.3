package com.packtpub.javaee8;

import org.glassfish.jersey.jsonb.JsonBindingFeature;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JsonbResourceIntegrationTest {

    private Client client;
    private WebTarget webTarget;

    @Before
    public void setUp() throws Exception {
        client = ClientBuilder.newBuilder()
                .connectTimeout(5, TimeUnit.SECONDS).readTimeout(5, TimeUnit.SECONDS)
                .register(JsonBindingFeature.class)
                .build();

        webTarget = client.target("http://localhost:8080").path("/content-service/api").path("/json-b");
    }

    @After
    public void tearDown() throws Exception {
        client.close();
    }

    @Test
    public void _1_GetJsonb() {
        JsonbResource.JsonbPojo pojo = webTarget.request()
                .accept(MediaType.APPLICATION_JSON)
                .get(JsonbResource.JsonbPojo.class);
        assertThat(pojo).isNotNull();
        assertThat(pojo.getMessage()).isEqualTo("Hello World.");
        assertThat(pojo.getAnswerToEverything()).isEqualTo(42);
        assertThat(pojo.getToday()).isEqualTo(LocalDate.now());
    }

    @Test
    public void _2_PostUpdatedJsonb() {
        JsonbResource.JsonbPojo pojo = new JsonbResource.JsonbPojo("Updated JSON-B.", null, null);
        Response response = webTarget.request().post(Entity.json(pojo));
        assertThat(response.getStatus()).isEqualTo(204);
    }

    @Test
    public void _3_GetUpdatedJsonb() {
        JsonbResource.JsonbPojo pojo = webTarget.request()
                .accept(MediaType.APPLICATION_JSON)
                .get(JsonbResource.JsonbPojo.class);
        assertThat(pojo).isNotNull();
        assertThat(pojo.getMessage()).isEqualTo("Updated JSON-B.");
        assertThat(pojo.getAnswerToEverything()).isNull();
        assertThat(pojo.getToday()).isNull();
    }

    @Test
    public void _99_Reset() {
        Response response = webTarget.request().head();
        assertThat(response.getStatus()).isEqualTo(204);
    }
}