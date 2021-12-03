package uk.gov.hmcts.reform.judicialbooking.domain.service.querybookings;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingQueryRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingQueryResponse;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.ParseRequestService;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.PersistenceService;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.PrepareDataService;

import java.util.List;

@Service
public class QueryBookingOrchestrator {

    private final ParseRequestService parseRequestService;
    private final PersistenceService persistenceService;
    private final PrepareDataService prepareDataService;

    public QueryBookingOrchestrator(ParseRequestService parseRequestService,
                                    PersistenceService persistenceService,
                                    PrepareDataService prepareDataService) {
        this.parseRequestService = parseRequestService;
        this.persistenceService = persistenceService;
        this.prepareDataService = prepareDataService;
    }

    public ResponseEntity<BookingQueryResponse> queryBookings(BookingQueryRequest bookingRequest) {
        List<String> userIds = parseRequestService.parseQueryRequest(bookingRequest);
        return prepareDataService.prepareQueryResponse(persistenceService.getValidBookings(userIds));
    }

}
