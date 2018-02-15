# Building Web Services with Java EE 8 <br>Section 3: Content Marshalling with JSON-B and JSON-P

## Videos

### Video 3.1: Introduction to Content-Types and Content Negotiation

In this video we are going to talk about Content-Types and Content Negotiation.

| Method | URI          | Status | Description |
|--------|--------------|--------|-------------|
| GET    | /api/version | 200    | Get version string based on header |
| GET    | /api/documents/magic.gif | 200 | Get and display GIF |
| GET    | /api/documents/me.jpg | 200 | Get and download JPEG |


### Video 3.2: Easy Data Binding using JSON-B

In this video we are showing how to use JSON-B for easy data binding.

| Method | URI | Status | Description |
|--------|-----|--------|-------------|
| GET    | /api/json-b/ | 200 | Marshall JSON-B annotated POJO |
| POST   | /api/json-b/ | 204 | Unmarshall and update JSON-B annotated POJO |
| GET    | /api/json-b/custom | 200 | Marshall custom POJO using Jsonb |
| POST   | /api/json-b/custom | 204 | Unmarshall custom POJO using Jsonb |

### Video 3.3: Flexible JSON processing with JSON-P

In this video we are showing how to use JSON-P for flexible JSON processing.

| Method | URI | Status | Description |
|--------|-----|--------|-------------|
| GET    | /api/json-p/ | 200 | Marshall a JsonArray of JsonObject using JSON-P |
| POST   | /api/json-p/ | 204 | Unmarshall and Update a JsonArray of JsonObject using JSON-P |
| PATCH  | /api/json-p/ | 204 | Patch a JsonArray of JsonObject using JSON-P Pointer |

### Video 2.4: Implementing hypermedia-driven REST APIs

In this video we are showing how to build hypermedia-driven REST APIs.

| Method | URI | Status | Description |
|--------|-----|--------|-------------|
| GET    | /api/hypermedia/ | 200 |  |
 

## Building and Running

```bash
$ mvn clean verify

$ docker build -t content-service:1.0 .
$ docker run -it -p 8080:8080 content-service:1.0
```
