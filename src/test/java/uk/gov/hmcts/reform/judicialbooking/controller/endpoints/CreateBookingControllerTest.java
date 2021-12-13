package uk.gov.hmcts.reform.judicialbooking.controller.endpoints;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingResponse;
import uk.gov.hmcts.reform.judicialbooking.domain.service.BookingOrchestrator;
import uk.gov.hmcts.reform.judicialbooking.helper.TestDataBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CreateBookingControllerTest {

    private final BookingOrchestrator bookingOrchestrator =
            mock(BookingOrchestrator.class);

    @InjectMocks
    private CreateBookingController sut;

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
}