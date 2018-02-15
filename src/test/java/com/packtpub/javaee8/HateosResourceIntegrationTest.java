package com.packtpub.javaee8;

import org.glassfish.jersey.jsonb.JsonBindingFeature;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HateosResourceIntegrationTest {

    private Client client;
    private WebTarget webTarget;

    private static String bookUri;
    private static Link authorLink;
    private static String booksUri;

    @Before
    public void setUp() throws Exception {
        client = ClientBuilder.newBuilder()
                .connectTimeout(5, TimeUnit.SECONDS).readTimeout(5, TimeUnit.SECONDS)
                .register(JsonBindingFeature.class)
                .build();

        webTarget = client.target("http://localhost:8080").path("/content-service/api").path("/hateos");
    }

    @After
    public void tearDown() throws Exception {
        client.close();
    }

    @Test
    public void _1_GetBooks() {
        JsonArray books = webTarget.path("/books").request().accept(MediaType.APPLICATION_JSON).get(JsonArray.class);
        assertThat(books).isNotNull();
        assertThat(books.size()).isEqualTo(2);

        bookUri = ((JsonString) books.getValue("/0/_links/self/href")).getString();
        assertThat(bookUri).isNotBlank();
    }

    @Test
    public void _2_GetBookByUri() {
        Response response = client.target(bookUri).request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus()).isEqualTo(200);

        HateosResource.Book book = response.readEntity(HateosResource.Book.class);
        assertThat(book).isNotNull();
        assertThat(book.isbn).isEqualTo("0345391802");
        assertThat(book.title).isEqualTo("The Hitchhiker's Guide to the Galaxy");

        authorLink = response.getLink("author");
        assertThat(authorLink).isNotNull();
    }

    @Test
    public void _3_GetAuthorByLink() {
        Response response = client.target(authorLink).request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus()).isEqualTo(200);

        JsonObject jsonObject = response.readEntity(JsonObject.class);
        assertThat(jsonObject.getString("name")).isEqualTo("Douglas Adams");

        booksUri = jsonObject.getJsonObject("_links").getJsonObject("books").getString("href");
    }

    @Test
    public void _4_GetBooksByAuthor() {
        JsonArray jsonArray = client.target(booksUri).request(MediaType.APPLICATION_JSON).get(JsonArray.class);
        assertThat(jsonArray.size()).isEqualTo(1);
    }
}