package uk.gov.hmcts.reform.judicialbooking.domain.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingQueryRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingQueryResponse;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingResponse;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.ParseRequestService;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.PersistenceService;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.PrepareDataService;
import uk.gov.hmcts.reform.judicialbooking.util.Constants;
import uk.gov.hmcts.reform.judicialbooking.util.ValidationUtil;

import java.text.ParseException;
import java.util.List;

@RequestScope
@Service
public class BookingOrchestrator {

    private final ParseRequestService parseRequestService;
    private final PersistenceService persistenceService;
    private final PrepareDataService prepareDataService;

    public BookingOrchestrator(ParseRequestService parseRequestService,
                               PersistenceService persistenceService,
                               PrepareDataService prepareDataService) {
        this.parseRequestService = parseRequestService;
        this.persistenceService = persistenceService;
        this.prepareDataService = prepareDataService;
    }

    public ResponseEntity<BookingResponse> createBooking(BookingRequest bookingRequest) throws ParseException {

        BookingEntity parsedBookingRequest = parseRequestService.parseBookingRequest(bookingRequest);

        BookingEntity bookingEntity = persistenceService.persistBooking(parsedBookingRequest);

        return prepareDataService.prepareBookingResponse(bookingEntity);
    }

    public ResponseEntity<BookingQueryResponse> queryBookings(BookingQueryRequest bookingRequest) {
        List<String> userIds = parseRequestService.parseQueryRequest(bookingRequest);
        return prepareDataService.prepareQueryResponse(persistenceService.getValidBookings(userIds));
    }

    public ResponseEntity<Void> deleteBookingByUserId(String userId) {
        ValidationUtil.validateInputParams(Constants.UUID_PATTERN, userId);
        persistenceService.deleteBookings(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
