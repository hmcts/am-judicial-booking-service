package uk.gov.hmcts.reform.judicialbooking.controller.endpoints;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class DeleteBookingControllerTest {

    @InjectMocks
    private DeleteBookingController sut;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private final BookingOrchestrator bookingOrchestrator =
            mock(BookingOrchestrator.class);

    @Test
    void deleteBookingByUserId() {
        ResponseEntity<Void> expectedResponse = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        when(bookingOrchestrator.deleteBookingByUserId(any())).thenReturn(expectedResponse);
        ResponseEntity<Void> response = sut.deleteBookingByUserId(UUID.randomUUID().toString());
        assertNotNull(response);
        assertEquals(expectedResponse.getStatusCode(), response.getStatusCode());
    }
}