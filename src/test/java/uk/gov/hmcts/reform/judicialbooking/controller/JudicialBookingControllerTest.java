package uk.gov.hmcts.reform.judicialbooking.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.judicialbooking.controller.endpoints.JudicialBookingController;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingResponse;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingsResponse;
import uk.gov.hmcts.reform.judicialbooking.domain.service.BookingOrchestrator;
import uk.gov.hmcts.reform.judicialbooking.helper.TestDataBuilder;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JudicialBookingControllerTest {

    private final BookingOrchestrator bookingOrchestrator = mock(BookingOrchestrator.class);

    @InjectMocks
    private JudicialBookingController sut;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createBooking() throws Exception {
        BookingRequest bookingRequest = new BookingRequest();
        ResponseEntity<BookingResponse> expectedResponse = TestDataBuilder.buildCreateBookingResponse();
        when(bookingOrchestrator.createBooking(any())).thenReturn(expectedResponse);
        ResponseEntity<BookingResponse> response = sut.createBooking("", bookingRequest);
        assertNotNull(response);
        assertEquals(expectedResponse.getStatusCode(), response.getStatusCode());
        assertEquals(expectedResponse.getBody(), response.getBody());
    }

    @Test
    void retrieveBookings() {
        ResponseEntity<BookingsResponse> response = sut.retrieveBookings("");
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(Objects.requireNonNull(response.getBody()).getBookingList());
    }
}