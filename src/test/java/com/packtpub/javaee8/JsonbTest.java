package com.packtpub.javaee8;

import org.junit.Before;
import org.junit.Test;

import javax.json.bind.Jsonb;
import javax.json.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonbTest {

    private static final String PLAIN_POJO_JSON =
            "{\"a-double\":23.5,\"a-integer\":47,\"a-string\":\"Hello JSON-B\",\"array-of-ints\":[1,2,3]}";

    private static final String ANNOTATED_POJO_JSON =
            "{\"greeting\":\"Hello JSON-B.\",\"answer-to-everything\":\"42,00\",\"today\":\"02/15/2018\"}";

    private Jsonb jsonb;

    @Before
    public void setUp() throws Exception {
        // TODO create JsonbConfig and Jsonb instance
    }

    @Test
    public void testToJsonWithPlainPojo() {
        PlainPojo pojo = PlainPojo.create();
        String json = jsonb.toJson(pojo);
        assertThat(json).isEqualTo(PLAIN_POJO_JSON);
    }

    @Test
    public void testPlainPojoFromJson() {
        PlainPojo pojo = jsonb.fromJson(PLAIN_POJO_JSON, PlainPojo.class);
        assertThat(pojo).isEqualTo(PlainPojo.create());
    }

    @Test
    public void testToJsonWithAnnotatedPojo() {
        AnnotatedPojo pojo = AnnotatedPojo.create();
        String json = jsonb.toJson(pojo);
        assertThat(json).isEqualTo(ANNOTATED_POJO_JSON);
    }

    @Test
    public void testAnnotatedPojoFromJson() {
        AnnotatedPojo pojo = jsonb.fromJson(ANNOTATED_POJO_JSON, AnnotatedPojo.class);
        assertThat(pojo).isEqualTo(AnnotatedPojo.create());
    }

    @JsonbPropertyOrder(value = {"message", "answerToEverything", "today"})
    public static class AnnotatedPojo {
        @JsonbProperty(value = "greeting", nillable = true)
        public String message;

        @JsonbNumberFormat("#,##0.00")
        public Integer answerToEverything;

        @JsonbDateFormat("MM/dd/yyyy")
        public LocalDate today;

        @JsonbTransient
        public BigDecimal invisible = BigDecimal.TEN;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AnnotatedPojo that = (AnnotatedPojo) o;
            return Objects.equals(message, that.message) &&
                    Objects.equals(answerToEverything, that.answerToEverything) &&
                    Objects.equals(today, that.today);
        }

        @Override
        public int hashCode() {
            return Objects.hash(message, answerToEverything, today);
        }

        public static AnnotatedPojo create() {
            AnnotatedPojo pojo = new AnnotatedPojo();
            pojo.message = "Hello JSON-B.";
            pojo.answerToEverything = 42;
            pojo.today = LocalDate.of(2018, 2, 15);
            return pojo;
        }
    }

    public static class PlainPojo {
        // these need to be public,
        // or public getter / setter are required
        public String aString;
        public Integer aInteger;
        public int[] arrayOfInts;
        public Double aDouble;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PlainPojo plainPojo = (PlainPojo) o;
            return Objects.equals(aString, plainPojo.aString) &&
                    Objects.equals(aInteger, plainPojo.aInteger) &&
                    Arrays.equals(arrayOfInts, plainPojo.arrayOfInts) &&
                    Objects.equals(aDouble, plainPojo.aDouble);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(aString, aInteger, aDouble);
            result = 31 * result + Arrays.hashCode(arrayOfInts);
            return result;
        }

        public static PlainPojo create() {
            PlainPojo plainPojo = new PlainPojo();
            plainPojo.aString = "Hello JSON-B";
            plainPojo.aInteger = 47;
            plainPojo.arrayOfInts = new int[]{1, 2, 3};
            plainPojo.aDouble = 23.5;
            return plainPojo;
        }
    }
}
