package uk.gov.hmcts.reform.judicialbooking.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import wiremock.com.jayway.jsonpath.internal.filter.ValueNode;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class JacksonUtilsTest {

    @Test
    void convertValue() {
        assertNotNull(JacksonUtils.convertValue(ValueNode.createJsonNode("Test")));
    }

    @Test
    void convertObjectIntoJsonNode() {
        assertNotNull(JacksonUtils.convertValueJsonNode("\"id\": \"21334a2b-79ce-44eb-9168-2d49a744be9c\""));
    }

    @Test
    void getHashMapTypeReference() {
        assertNotNull(JacksonUtils.getHashMapTypeReference());
    }
}