package com.packtpub.javaee8;

import org.junit.Test;

import javax.json.*;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;
import java.io.StringReader;
import java.io.StringWriter;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonpTest {

    @Test
    public void testJsonParser() {
        JsonParser parser = Json.createParser(new StringReader("{ \"aString\": \"Hello Json-P\" }"));

        JsonParser.Event next = parser.next();
        assertThat(next).isEqualTo(JsonParser.Event.START_OBJECT);

        JsonObject jsonObject = parser.getObject();
        assertThat(jsonObject).isNotNull();

        String aString = jsonObject.getString("aString");
        assertThat(aString).isEqualTo("Hello Json-P");
    }

    @Test
    public void testJsonGenerator() {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = Json.createGenerator(writer);
        generator.writeStartObject()
                .write("aString", "Hello Json-P")
                .writeStartArray("arrayOfInt")
                .write(1).write(2).write(3)
                .writeEnd()
                .writeEnd()
                .close();

        assertThat(writer.toString()).isEqualTo("{\"aString\":\"Hello Json-P\",\"arrayOfInt\":[1,2,3]}");
    }

    @Test
    public void testJsonBuilder() {
        JsonArray values = Json.createArrayBuilder()
                .add(Json.createObjectBuilder()
                        .add("aString", "Hello Json-P 1")
                        .add("aInteger", 42)
                        .add("aBoolean", false)
                        .add("aNullValue", JsonValue.NULL)
                        .build())
                .add(Json.createObjectBuilder()
                        .add("aString", "Hello Json-P 2")
                        .add("aInteger", 23)
                        .add("aBoolean", true)
                        .build())
                .build();

        assertThat(values).isNotNull();
        assertThat(values.size()).isEqualTo(2);
        assertThat(values.getJsonObject(1).getBoolean("aBoolean")).isTrue();
    }

    @Test
    public void testJsonReadObject() {
        JsonReader jsonReader = Json.createReader(new StringReader("{\"aString\":\"Hello Json-P\",\"arrayOfInt\":[1,2,3]}"));
        JsonObject jsonObject = jsonReader.readObject();

        assertThat(jsonObject).isNotNull();
        assertThat(jsonObject.getString("aString")).isEqualTo("Hello Json-P");
        assertThat(jsonObject.getJsonArray("arrayOfInt").getInt(1)).isEqualTo(2);
    }

    @Test
    public void testJsonReadStructure() {
        JsonReader jsonReader = Json.createReader(new StringReader("{\"aString\":\"Hello Json-P\",\"arrayOfInt\":[1,2,3]}"));
        JsonStructure jsonStructure = jsonReader.read();

        assertThat(jsonStructure.getValueType()).isEqualTo(JsonValue.ValueType.OBJECT);
        assertThat(jsonStructure.asJsonObject()).isNotNull();

        assertThat(jsonStructure.getValue("/aString")).isInstanceOf(JsonString.class);
        assertThat(jsonStructure.getValue("/arrayOfInt")).isInstanceOf(JsonArray.class);
        assertThat(jsonStructure.getValue("/arrayOfInt/1")).isInstanceOf(JsonNumber.class);
    }

    @Test
    public void testJsonPointer() {
        JsonReader jsonReader = Json.createReader(new StringReader("{\"aString\":\"Hello Json-P\",\"arrayOfInt\":[1,2,3]}"));
        JsonObject jsonObject = jsonReader.readObject();
        JsonArray jsonArray = jsonObject.getJsonArray("arrayOfInt");

        // access an array value by index
        JsonPointer jsonPointer = Json.createPointer("/arrayOfInt/1");
        JsonValue jsonValue = jsonPointer.getValue(jsonObject);
        assertThat(jsonValue).isInstanceOf(JsonNumber.class);
        assertThat(jsonValue).isEqualTo(jsonArray.get(1));

        // replace the array value by index
        jsonObject = jsonPointer.replace(jsonObject, Json.createValue(42));
        jsonArray = jsonObject.getJsonArray("arrayOfInt");
        assertThat(jsonArray.getInt(1)).isEqualTo(42);

        // remove the array value by index
        jsonObject = jsonPointer.remove(jsonObject);
        jsonArray = jsonObject.getJsonArray("arrayOfInt");
        assertThat(jsonArray.size()).isEqualTo(2);
    }

    @Test
    public void testJsonPatch() {
        JsonReader jsonReader = Json.createReader(new StringReader("{\"aString\":\"Hello Json-P\",\"arrayOfInt\":[1,2,3]}"));
        JsonObject jsonObject = jsonReader.readObject();

        JsonPatch patch = Json.createPatchBuilder()
                .replace("/aString", "Patched Json-P.")
                .remove("/arrayOfInt/1")
                .build();

        jsonObject = patch.apply(jsonObject);
        assertThat(jsonObject.getString("aString")).isEqualTo("Patched Json-P.");
        assertThat(jsonObject.getJsonArray("arrayOfInt").size()).isEqualTo(2);
    }

    @Test
    public void testJsonDiff() {
        JsonObject source = Json.createObjectBuilder().add("aString", "abc").build();
        JsonObject target = Json.createObjectBuilder().add("aString", "xyz").build();

        JsonPatch diff = Json.createDiff(source, target);
        JsonObject replace = diff.toJsonArray().getJsonObject(0);
        assertThat(replace.getString("op")).isEqualTo("replace");
        assertThat(replace.getString("path")).isEqualTo("/aString");
        assertThat(replace.getString("value")).isEqualTo("xyz");

        source = diff.apply(source);
        assertThat(source).isEqualTo(target);
    }


}
