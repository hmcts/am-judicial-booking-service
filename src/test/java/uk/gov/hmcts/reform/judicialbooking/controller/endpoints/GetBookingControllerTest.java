package uk.gov.hmcts.reform.judicialbooking.controller.endpoints;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.data.BookingRepository;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingResponse;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingsResponse;
import uk.gov.hmcts.reform.judicialbooking.domain.model.enums.Status;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.PersistenceService;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.PrepareDataService;
import uk.gov.hmcts.reform.judicialbooking.domain.service.createbooking.CreateBookingOrchestrator;
import uk.gov.hmcts.reform.judicialbooking.domain.service.getbooking.RetrieveBookingOrchestrator;
import uk.gov.hmcts.reform.judicialbooking.helper.TestDataBuilder;
import uk.gov.hmcts.reform.judicialbooking.util.SecurityUtils;

import java.text.ParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doReturn;

public class GetBookingControllerTest {

    @Mock
    private BookingRepository bookingRepositoryMock = Mockito.mock(BookingRepository.class);

    private transient PersistenceService persistenceServiceMock = new PersistenceService(bookingRepositoryMock);

    SecurityUtils securityUtilsMock = Mockito.mock(SecurityUtils.class);

    @Mock
    private PrepareDataService prepareDataServiceMock = Mockito.mock(PrepareDataService.class);

    private transient RetrieveBookingOrchestrator retrieveBookingOrchestratorMock = new RetrieveBookingOrchestrator(
            persistenceServiceMock, prepareDataServiceMock, securityUtilsMock);

    @InjectMocks
    private GetBookingController mockController = new GetBookingController(retrieveBookingOrchestratorMock);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void retrieveBookings() throws ParseException {
        String actorId = "123e4567-e89b-42d3-a456-556642445678";
        List<BookingEntity> bookings = List.of(TestDataBuilder.buildBooking());
        doReturn(actorId).when(securityUtilsMock).getUserId();
        doReturn(bookings).when(bookingRepositoryMock)
                .findByUserIdAndStatusAndBeginTimeLessThanEqualAndEndTimeGreaterThan(actorId, Status.LIVE.toString(),
                        any(), any());

        ResponseEntity<BookingsResponse> response = mockController.retrieveBookings("");
        //assertNotNull(response);
        //assertEquals(HttpStatus.OK, response.getStatusCode());
        //assertNotNull(response.getBody().getBookingList());
    }
}
