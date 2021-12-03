package uk.gov.hmcts.reform.judicialbooking.domain.service.querybookings;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingQueryRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingQueryResponse;
import uk.gov.hmcts.reform.judicialbooking.domain.model.RequestIds;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.ParseRequestService;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.PersistenceService;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.PrepareDataService;
import uk.gov.hmcts.reform.judicialbooking.helper.TestDataBuilder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.List;
import java.util.Objects;

class QueryBookingOrchestratorTest {

    private final ParseRequestService parseRequestService =
            mock(ParseRequestService.class);
    private final PrepareDataService prepareDataService =
            mock(PrepareDataService.class);
    private final PersistenceService persistenceService =
            mock(PersistenceService.class);

    @InjectMocks
    private QueryBookingOrchestrator sut;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void queryBooking() throws ParseException {
        RequestIds requestIds = TestDataBuilder.buildRequestIds();

        List<BookingEntity> bookingEntities =
                List.of(TestDataBuilder.buildRetrievedBooking(requestIds.getUserIds().get(0)),
                TestDataBuilder.buildRetrievedBooking(requestIds.getUserIds().get(1)));
        ResponseEntity<BookingQueryResponse> expectedResponse =
                ResponseEntity.ok(TestDataBuilder.buildQueryResponse(bookingEntities));

        when(parseRequestService.parseQueryRequest(any())).thenReturn(requestIds.getUserIds());
        when(prepareDataService.prepareQueryResponse(any())).thenReturn(expectedResponse);
        when(persistenceService.getValidBookings(requestIds.getUserIds())).thenReturn(bookingEntities);

        ResponseEntity<BookingQueryResponse> response = sut.queryBookings(
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