package com.packtpub.javaee8;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RequestScoped
@Path("hateos")
@Produces(MediaType.APPLICATION_JSON)
public class HateosResource {

    @Context
    private ResourceContext context;

    @Path("/books")
    public BookResource books() {
        return context.getResource(BookResource.class);
    }

    @Path("/author")
    public AuthorResource author() {
        return context.getResource(AuthorResource.class);
    }

    @RequestScoped
    @Produces(MediaType.APPLICATION_JSON)
    public static class BookResource {
        @Context
        private UriInfo uriInfo;

        private Map<String, Book> books = new HashMap<>();

        @PostConstruct
        public void initialize() {
            books.put("1234567890", Book.from("1234567890", "Building Webservices with Java EE 8", 1));
            books.put("0345391802", Book.from("0345391802", "The Hitchhiker's Guide to the Galaxy", 2));
        }

        @GET
        @Produces(MediaType.APPLICATION_JSON)
        public JsonArray books(@QueryParam("authorId") @DefaultValue("-1") Integer authorId) {
            JsonArrayBuilder all = Json.createArrayBuilder();
            if (authorId == -1) {
                books.values()
                        .forEach(book -> all.add(asJsonObject(book,
                                createBookResourceUri(book.isbn, uriInfo),
                                createAuthorResourceUri(book.authorId, uriInfo))));
            } else {
                books.values().stream()
                        .filter(book -> book.authorId == authorId)
                        .forEach(book -> all.add(asJsonObject(book,
                                createBookResourceUri(book.isbn, uriInfo),
                                createAuthorResourceUri(book.authorId, uriInfo))));
            }
            return all.build();
        }

        @GET
        @Path("/{isbn}")
        public Response book(@PathParam("isbn") String isbn) {
            Book book = books.get(isbn);
            URI bookUri = createBookResourceUri(isbn, uriInfo);
            URI authorUri = createAuthorResourceUri(book.authorId, uriInfo);
            JsonObject jsonObject = asJsonObject(book, bookUri, authorUri);
            return Response.ok(jsonObject)
                    .link(bookUri, "self")
                    .link(authorUri, "author")
                    .build();
        }

        private JsonObject asJsonObject(Book book, URI bookUri, URI authorUri) {
            return Json.createObjectBuilder()
                    .add("isbn", book.isbn)
                    .add("title", book.title)
                    .add("_links", Json.createObjectBuilder()
                            .add("self", Json.createObjectBuilder()
                                    .add("href", bookUri.toString()))
                            .add("author", Json.createObjectBuilder()
                                    .add("href", authorUri.toString())))
                    .build();
        }
    }

    public static class Book {
        public String isbn;
        public String title;
        public Integer authorId;

        public static Book from(String isbn, String title, Integer authorId) {
            Book book = new Book();
            book.isbn = isbn;
            book.title = title;
            book.authorId = authorId;
            return book;
        }
    }

    @RequestScoped
    @Produces(MediaType.APPLICATION_JSON)
    public static class AuthorResource {
        @Context
        private UriInfo uriInfo;

        private Map<Integer, String> authors = new HashMap<>();

        @PostConstruct
        public void initialize() {
            authors.put(1, "M.Leander Reimer");
            authors.put(2, "Douglas Adams");
        }

        @GET
        @Produces(MediaType.APPLICATION_JSON)
        public JsonArray authors() {
            JsonArrayBuilder all = Json.createArrayBuilder();
            authors.forEach((i, s) -> all.add(asJsonObject(i, s,
                    createAuthorResourceUri(i, uriInfo),
                    createBooksResourceUri(i, uriInfo))));
            return all.build();
        }

        @GET
        @Path("/{authorId}")
        public Response author(@PathParam("authorId") Integer authorId) {
            String author = authors.get(authorId);
            URI autorUri = createAuthorResourceUri(authorId, uriInfo);
            URI booksUri = createBooksResourceUri(authorId, uriInfo);

            JsonObject jsonObject = asJsonObject(authorId, author, autorUri, booksUri);
            return Response.ok(jsonObject)
                    .link(autorUri, "self")
                    .link(booksUri, "books")
                    .build();
        }

        private JsonObject asJsonObject(Integer authorId, String name, URI autorUri, URI booksUri) {
            return Json.createObjectBuilder()
                    .add("id", authorId)
                    .add("name", name)
                    .add("_links", Json.createObjectBuilder()
                            .add("self", Json.createObjectBuilder()
                                    .add("href", autorUri.toString()))
                            .add("books", Json.createObjectBuilder()
                                    .add("href", booksUri.toString())))
                    .build();
        }
    }

    static URI createBookResourceUri(String isbn, UriInfo uriInfo) {
        return uriInfo.getBaseUriBuilder()
                .path(HateosResource.class)
                .path(HateosResource.class, "books")
                .path(BookResource.class, "book")
                .build(isbn);
    }

    static URI createBooksResourceUri(Integer authorId, UriInfo uriInfo) {
        return uriInfo.getBaseUriBuilder()
                .path(HateosResource.class)
                .path(HateosResource.class, "books")
                .queryParam("authorId", authorId)
                .build();
    }

    static URI createAuthorResourceUri(Integer authorId, UriInfo uriInfo) {
        return uriInfo.getBaseUriBuilder()
                .path(HateosResource.class)
                .path(HateosResource.class, "author")
                .path(AuthorResource.class, "author")
                .build(authorId);
    }

}
