package uk.gov.hmcts.reform.judicialbooking.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.service.BookingOrchestrator;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springdoc.core.Constants.SWAGGER_UI_URL;


class WelcomeControllerTest {

    @InjectMocks
    private WelcomeController sut;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private final BookingOrchestrator bookingOrchestrator =
            mock(BookingOrchestrator.class);

    @Test
    void swaggerRedirect() {
        var response = sut.swaggerRedirect();

        assertNotNull(response);
        assertTrue(response.isRedirectView());
        assertEquals(SWAGGER_UI_URL, response.getUrl());
    }

    @Test
    void welcome() {
        assertEquals("Welcome to Judicial Booking service", sut.welcome());
    }

    @Test
    void deleteBookingByUserId() {
        ResponseEntity<Void> expectedResponse = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        when(bookingOrchestrator.deleteBookingByUserId(any())).thenReturn(expectedResponse);
        ResponseEntity<Void> response = sut.deleteBookingByUserId(UUID.randomUUID().toString());
        assertNotNull(response);
        assertEquals(expectedResponse.getStatusCode(), response.getStatusCode());
    }

}