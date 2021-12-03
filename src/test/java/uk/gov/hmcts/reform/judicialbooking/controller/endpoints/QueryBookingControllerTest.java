package uk.gov.hmcts.reform.judicialbooking.controller.endpoints;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingQueryRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingQueryResponse;
import uk.gov.hmcts.reform.judicialbooking.domain.model.RequestIds;
import uk.gov.hmcts.reform.judicialbooking.domain.service.querybookings.QueryBookingOrchestrator;
import uk.gov.hmcts.reform.judicialbooking.helper.TestDataBuilder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Objects;

class QueryBookingControllerTest {

    private final QueryBookingOrchestrator queryBookingOrchestrator =
            mock(QueryBookingOrchestrator.class);

    @InjectMocks
    private QueryBookingController sut;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void queryBooking() throws Exception {
        RequestIds requestIds = TestDataBuilder.buildRequestIds();

        ResponseEntity<BookingQueryResponse> expectedResponse = TestDataBuilder.buildQueryResponseEntity();

        when(queryBookingOrchestrator.queryBookings(any())).thenReturn(expectedResponse);

        ResponseEntity<BookingQueryResponse> response = sut.queryBookings("",
                BookingQueryRequest.builder().queryRequest(requestIds).build());

        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(Objects.requireNonNull(response.getBody()).getBookingEntities());
        Assertions.assertEquals(2, response.getBody().getBookingEntities().size());
        Assertions.assertEquals(requestIds.getUserIds().get(0),
                response.getBody().getBookingEntities().get(0).getUserId());
        Assertions.assertEquals(requestIds.getUserIds().get(1),
                response.getBody().getBookingEntities().get(1).getUserId());

    }
}