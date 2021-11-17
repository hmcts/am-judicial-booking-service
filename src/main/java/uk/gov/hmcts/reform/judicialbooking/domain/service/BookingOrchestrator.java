package uk.gov.hmcts.reform.judicialbooking.domain.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.annotation.RequestScope;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.DuplicateRequestException;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.UnprocessableEntityException;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingResponse;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingsResponse;
import uk.gov.hmcts.reform.judicialbooking.domain.model.JudicialUserProfile;
import uk.gov.hmcts.reform.judicialbooking.domain.model.OrmBookingAssignmentsRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.enums.Status;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.OrgRoleMappingService;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.ParseRequestService;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.PersistenceService;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.PrepareDataService;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.RetrieveDataService;
import uk.gov.hmcts.reform.judicialbooking.util.SecurityUtils;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;

@RequestScope
@Service
public class BookingOrchestrator {

    private final ParseRequestService parseRequestService;
    private final PersistenceService persistenceService;
    private final PrepareDataService prepareDataService;
    private final RetrieveDataService retrieveDataService;
    private final OrgRoleMappingService orgRoleMappingService;
    private final SecurityUtils securityUtils;

    public BookingOrchestrator(ParseRequestService parseRequestService,
                               PersistenceService persistenceService,
                               PrepareDataService prepareDataService,
                               RetrieveDataService retrieveDataService,
                               OrgRoleMappingService orgRoleMappingService,
                               SecurityUtils securityUtils) {
        this.parseRequestService = parseRequestService;
        this.persistenceService = persistenceService;
        this.prepareDataService = prepareDataService;
        this.retrieveDataService = retrieveDataService;
        this.orgRoleMappingService = orgRoleMappingService;
        this.securityUtils = securityUtils;
    }

    public ResponseEntity<BookingResponse> createBooking(BookingRequest bookingRequest) throws ParseException {


        BookingEntity parsedBookingRequest = parseRequestService.parseBookingRequest(bookingRequest);

        if (!CollectionUtils.isEmpty(persistenceService.checkExists(parsedBookingRequest.getUserId(),
                parsedBookingRequest.getAppointmentId()))) {
            throw new DuplicateRequestException("Booking already exists for the provided appointmentId");
        }

        List<JudicialUserProfile> judicialUserProfile = retrieveDataService.getJudicialUserProfile(
                Collections.singletonList(parsedBookingRequest.getUserId()));

        if (CollectionUtils.isEmpty(judicialUserProfile)) {
            throw new UnprocessableEntityException("Judicial user profile couldn't be found for the logged in user");
        }

        BookingEntity preparedBooking = prepareDataService.prepareBooking(parsedBookingRequest,
                judicialUserProfile.get(0));

        BookingEntity bookingEntity = persistenceService.persistBooking(preparedBooking);

        OrmBookingAssignmentsRequest ormBookingRequest
                = prepareDataService.prepareOrmBookingRequest(preparedBooking, judicialUserProfile.get(0));
        ResponseEntity<Object> ormResponse = orgRoleMappingService.createBookingAssignments(ormBookingRequest);

        if (ormResponse.getStatusCode().is2xxSuccessful()) {
            bookingEntity.setStatus(Status.LIVE.toString());
        } else {
            bookingEntity.setStatus(Status.FAILED.toString());
        }
        persistenceService.persistBooking(bookingEntity);

        return prepareDataService.prepareBookingResponse(bookingEntity);
    }

    public ResponseEntity<BookingsResponse> getBookings() {
        List<BookingEntity> bookingList = persistenceService.getValidBookings(securityUtils.getUserId());
        return prepareDataService.prepareBookingResponse(bookingList);
    }
}
