package uk.gov.hmcts.reform.judicialbooking.domain.service.createbooking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingResponse;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.ParseRequestService;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.PersistenceService;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.PrepareDataService;
import uk.gov.hmcts.reform.judicialbooking.helper.TestDataBuilder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CreateBookingOrchestratorTest {


    private final ParseRequestService parseRequestService = mock(ParseRequestService.class);
    private final PersistenceService persistenceService = mock(PersistenceService.class);
    private final PrepareDataService prepareDataService = mock(PrepareDataService.class);

    @InjectMocks
    private final CreateBookingOrchestrator sut = new CreateBookingOrchestrator(
            parseRequestService,
            persistenceService,
            prepareDataService
    );

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createBooking() throws Exception {

        when(parseRequestService.parseBookingRequest(any())).thenReturn(TestDataBuilder.buildParsedBooking());

        when(persistenceService.persistBooking(any()))
                .thenReturn(TestDataBuilder.buildPreparedBooking());

        BookingEntity bookingResponseObject = TestDataBuilder.buildPreparedBooking();

        ResponseEntity<BookingResponse> bookingResponseEntity =
                ResponseEntity.status(HttpStatus.OK).body(TestDataBuilder.buildBookingResponse(bookingResponseObject));

        when(prepareDataService.prepareBookingResponse(any())).thenReturn(bookingResponseEntity);

        ResponseEntity<BookingResponse> createBookingResponse =
                sut.createBooking(TestDataBuilder.buildBookingRequest());

        assertNotNull(createBookingResponse);

    }
}