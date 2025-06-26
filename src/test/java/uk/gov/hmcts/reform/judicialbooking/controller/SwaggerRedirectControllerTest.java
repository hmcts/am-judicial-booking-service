package uk.gov.hmcts.reform.judicialbooking.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springdoc.core.utils.Constants.SWAGGER_UI_URL;

class SwaggerRedirectControllerTest {

    private final SwaggerRedirectController sut = new SwaggerRedirectController();

    @Test
    void swaggerRedirect() {
        var response = sut.swaggerRedirect();

        assertNotNull(response);
        assertTrue(response.isRedirectView());
        assertEquals(SWAGGER_UI_URL, response.getUrl());
    }
}
