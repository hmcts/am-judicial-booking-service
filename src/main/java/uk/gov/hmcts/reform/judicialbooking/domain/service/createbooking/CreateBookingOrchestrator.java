package uk.gov.hmcts.reform.judicialbooking.domain.service.createbooking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.Booking;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingResponse;
import uk.gov.hmcts.reform.judicialbooking.domain.model.JudicialUserProfile;
import uk.gov.hmcts.reform.judicialbooking.domain.model.OrmBookingAssignmentsRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.enums.Status;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.OrgRoleMappingService;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.ParseRequestService;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.PersistenceService;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.PrepareDataService;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.RetrieveDataService;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class CreateBookingOrchestrator {

    private final ParseRequestService parseRequestService;
    private final PersistenceService persistenceService;
    private final PrepareDataService prepareDataService;
    private final RetrieveDataService retrieveDataService;
    private final OrgRoleMappingService orgRoleMappingService;

    BookingEntity bookingEntity;

    public CreateBookingOrchestrator(ParseRequestService parseRequestService,
                                     PersistenceService persistenceService,
                                     PrepareDataService prepareDataService,
                                     RetrieveDataService retrieveDataService,
                                     OrgRoleMappingService orgRoleMappingService) {
        this.parseRequestService = parseRequestService;
        this.persistenceService = persistenceService;
        this.prepareDataService = prepareDataService;
        this.retrieveDataService = retrieveDataService;
        this.orgRoleMappingService = orgRoleMappingService;
    }

    public ResponseEntity<BookingResponse> createBooking(BookingRequest bookingRequest) throws ParseException {


        Booking parsedBookingRequest = parseRequestService.parseBookingRequest(bookingRequest);

        List<JudicialUserProfile> judicialUserProfiles = retrieveDataService.getJudicialUserProfile(
                Collections.singletonList(parsedBookingRequest.getUserId()));

        Booking preparedBooking = prepareDataService.prepareBooking(parsedBookingRequest, judicialUserProfiles);

        bookingEntity = persistenceService.persistBooking(preparedBooking);

        OrmBookingAssignmentsRequest ormBookingRequest
                = prepareDataService.prepareOrmBookingRequest(preparedBooking, judicialUserProfiles);
        ResponseEntity<Object> ormResponse = orgRoleMappingService.createBookingAssignments(ormBookingRequest);

        if (ormResponse.getStatusCode().is2xxSuccessful()) {
            bookingEntity.setStatus(Status.LIVE.toString());
            persistenceService.updateBooking(bookingEntity);
        } else {
            bookingEntity.setStatus(Status.FAILED.toString());
            persistenceService.updateBooking(bookingEntity);
        }

        return prepareDataService.prepareBookingResponse(bookingEntity);
    }
}
