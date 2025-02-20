package uk.gov.hmcts.reform.judicialbooking.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class JsonBConverterTest {

    @InjectMocks
    JsonBConverter sut = new JsonBConverter();

    String json = "{\"id\":1,\"name\":\"John Doe\"}";

    @Test
    void convertToDatabaseColumn() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(json);
        String result = sut.convertToDatabaseColumn(node);
        assertEquals(json, result);
    }

    @Test
    void convertToDatabaseColumn_Null() {
        String result = sut.convertToDatabaseColumn(null);
        Assertions.assertNull(result);
    }

    @Test
    void convertToEntityAttribute() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(json);
        JsonNode result = sut.convertToEntityAttribute(json);
        assertEquals(node, result);
    }

    @Test
    void convertToNullEntityAttribute() {
        JsonNode result = sut.convertToEntityAttribute(null);
        assertNull(result);
    }

    @Test
    void convertWrongJsonToEntityAttribute() {
        Assertions.assertThrows(RuntimeException.class, () ->
                sut.convertToEntityAttribute("{\"id\":1\"name\":\"John Doe"));
    }
}