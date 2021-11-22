package uk.gov.hmcts.reform.judicialbooking.domain.service.createbooking;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingResponse;
import uk.gov.hmcts.reform.judicialbooking.domain.model.OrmBookingAssignmentsRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.enums.Status;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.OrgRoleMappingService;
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
    private final OrgRoleMappingService orgRoleMappingService;

    public CreateBookingOrchestrator(ParseRequestService parseRequestService,
                                     PersistenceService persistenceService,
                                     PrepareDataService prepareDataService,
                                     OrgRoleMappingService orgRoleMappingService) {
        this.parseRequestService = parseRequestService;
        this.persistenceService = persistenceService;
        this.prepareDataService = prepareDataService;
        this.orgRoleMappingService = orgRoleMappingService;
    }

    public ResponseEntity<BookingResponse> createBooking(BookingRequest bookingRequest) throws ParseException {


        BookingEntity parsedBookingRequest = parseRequestService.parseBookingRequest(bookingRequest);

        BookingEntity preparedBooking = prepareDataService.prepareBooking(parsedBookingRequest);

        BookingEntity bookingEntity = persistenceService.persistBooking(preparedBooking);

        //Fetch all the bookings for this user and prepare the ORMBookingAssignmentRequest

        OrmBookingAssignmentsRequest ormBookingRequest
                = prepareDataService.prepareOrmBookingRequest(preparedBooking);
        ResponseEntity<Object> ormResponse = orgRoleMappingService.createBookingAssignments(ormBookingRequest);

        if (ormResponse.getStatusCode().is2xxSuccessful()) {
            bookingEntity.setStatus(Status.LIVE.toString());
            persistenceService.persistBooking(bookingEntity);
        } else {
            bookingEntity.setStatus(Status.FAILED.toString());
            persistenceService.persistBooking(bookingEntity);
        }

        return prepareDataService.prepareBookingResponse(bookingEntity);
    }
}
