package uk.gov.hmcts.reform.judicialbooking.controller.endpoints;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.ResourceNotFoundException;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.data.BookingRepository;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingsResponse;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.PersistenceService;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.PrepareDataService;
import uk.gov.hmcts.reform.judicialbooking.domain.service.getbooking.RetrieveBookingOrchestrator;
import uk.gov.hmcts.reform.judicialbooking.helper.TestDataBuilder;
import uk.gov.hmcts.reform.judicialbooking.util.SecurityUtils;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

public class GetBookingControllerTest {

    @Mock
    private BookingRepository bookingRepositoryMock = Mockito.mock(BookingRepository.class);
    @Mock
    SecurityUtils securityUtilsMock = Mockito.mock(SecurityUtils.class);
    private transient PersistenceService persistenceServiceMock = new PersistenceService(bookingRepositoryMock);
    private PrepareDataService prepareDataServiceMock = new PrepareDataService();
    private transient RetrieveBookingOrchestrator retrieveBookingOrchestratorMock = new RetrieveBookingOrchestrator(
            persistenceServiceMock, prepareDataServiceMock, securityUtilsMock);
    @InjectMocks
    private GetBookingController mockController = new GetBookingController(retrieveBookingOrchestratorMock);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void verifyBookingsWithOneActiveBooking() {
        String actorId = "123e4567-e89b-42d3-a456-556642445678";
        List<BookingEntity> expectedBookings = List.of(TestDataBuilder.buildBooking());
        doReturn(actorId).when(securityUtilsMock).getUserId();
        doReturn(expectedBookings).when(bookingRepositoryMock)
                .findByUserIdAndStatusAndBeginTimeLessThanEqualAndEndTimeGreaterThan(any(String.class),
                        any(String.class), any(ZonedDateTime.class), any(ZonedDateTime.class));

        ResponseEntity<BookingsResponse> response = mockController.retrieveBookings("");
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<BookingEntity> actualBookings = response.getBody().getBookingList();
        assertNotNull(actualBookings);
        Assertions.assertThat(expectedBookings).containsExactlyInAnyOrder(
                actualBookings.toArray(new BookingEntity[actualBookings.size()]));
    }

    @Test
    public void verifyBookingsWithMultipleActiveBooking() {
        String actorId = "123e4567-e89b-42d3-a456-556642445678";
        List<BookingEntity> expectedBookings = List.of(TestDataBuilder.buildBooking(), TestDataBuilder.buildBooking(),
                TestDataBuilder.buildBooking());
        doReturn(actorId).when(securityUtilsMock).getUserId();
        doReturn(expectedBookings).when(bookingRepositoryMock)
                .findByUserIdAndStatusAndBeginTimeLessThanEqualAndEndTimeGreaterThan(any(String.class),
                        any(String.class), any(ZonedDateTime.class), any(ZonedDateTime.class));

        ResponseEntity<BookingsResponse> response = mockController.retrieveBookings("");
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<BookingEntity> actualBookings = response.getBody().getBookingList();
        assertNotNull(actualBookings);
        Assertions.assertThat(expectedBookings).containsExactlyInAnyOrder(
                actualBookings.toArray(new BookingEntity[actualBookings.size()]));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void verifyEmptyBookingList() {
        String actorId = "123e4567-e89b-42d3-a456-556642445678";
        List<BookingEntity> bookings = List.of(TestDataBuilder.buildBooking());
        doReturn(actorId).when(securityUtilsMock).getUserId();
        doReturn(Collections.emptyList()).when(bookingRepositoryMock)
                .findByUserIdAndStatusAndBeginTimeLessThanEqualAndEndTimeGreaterThan(any(String.class),
                        any(String.class), any(ZonedDateTime.class), any(ZonedDateTime.class));

        ResponseEntity<BookingsResponse> response = mockController.retrieveBookings("");
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
