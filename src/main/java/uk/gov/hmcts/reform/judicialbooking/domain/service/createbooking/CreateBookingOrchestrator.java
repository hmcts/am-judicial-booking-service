package uk.gov.hmcts.reform.judicialbooking.domain.service.createbooking;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingResponse;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.ParseRequestService;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.PersistenceService;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.PrepareDataService;

import java.text.ParseException;

@RequestScope
@Service
public class CreateBookingOrchestrator {

    private final ParseRequestService parseRequestService;
    private final PersistenceService persistenceService;
    private final PrepareDataService prepareDataService;

    public CreateBookingOrchestrator(ParseRequestService parseRequestService,
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
}
