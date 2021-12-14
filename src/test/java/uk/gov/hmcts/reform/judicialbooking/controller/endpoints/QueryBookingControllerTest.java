package uk.gov.hmcts.reform.judicialbooking.controller.endpoints;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingQueryRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingQueryResponse;
import uk.gov.hmcts.reform.judicialbooking.domain.model.UserRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.service.BookingOrchestrator;
import uk.gov.hmcts.reform.judicialbooking.helper.TestDataBuilder;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class QueryBookingControllerTest {

    private final BookingOrchestrator bookingOrchestrator =
            mock(BookingOrchestrator.class);

    @InjectMocks
    private QueryBookingController sut;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void queryBooking() throws Exception {
        UserRequest userRequest = TestDataBuilder.buildRequestIds();

        ResponseEntity<BookingQueryResponse> expectedResponse = TestDataBuilder.buildQueryResponseEntity();

        when(bookingOrchestrator.queryBookings(any())).thenReturn(expectedResponse);

        ResponseEntity<BookingQueryResponse> response = sut.queryBookings("",
                BookingQueryRequest.builder().queryRequest(userRequest).build());

        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(Objects.requireNonNull(response.getBody()).getBookingEntities());
        Assertions.assertEquals(2, response.getBody().getBookingEntities().size());
        Assertions.assertEquals(userRequest.getUserIds().get(0),
                response.getBody().getBookingEntities().get(0).getUserId());
        Assertions.assertEquals(userRequest.getUserIds().get(1),
                response.getBody().getBookingEntities().get(1).getUserId());

    }
}