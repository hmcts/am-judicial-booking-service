package uk.gov.hmcts.reform.judicialbooking.domain.service.getbooking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.*;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.*;
import uk.gov.hmcts.reform.judicialbooking.util.SecurityUtils;

import java.text.ParseException;
import java.util.List;

@Slf4j
@Service
public class RetrieveBookingOrchestrator {

    private final PersistenceService persistenceService;
    private final SecurityUtils securityUtils;
    private final PrepareDataService prepareDataService;

    public RetrieveBookingOrchestrator(PersistenceService persistenceService,
                                       PrepareDataService prepareDataService,
                                       SecurityUtils securityUtils) {
        this.persistenceService = persistenceService;
        this.prepareDataService = prepareDataService;
        this.securityUtils = securityUtils;
    }

    public ResponseEntity<BookingsResponse> getBookings() throws ParseException {
        List<BookingEntity> bookingList = persistenceService.getValidBookings(securityUtils.getUserId());
        return prepareDataService.prepareBookingResponse(bookingList);
    }

}