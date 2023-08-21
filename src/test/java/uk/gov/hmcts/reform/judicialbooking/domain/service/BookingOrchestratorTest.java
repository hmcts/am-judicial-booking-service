package uk.gov.hmcts.reform.judicialbooking.domain.service;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.BadRequestException;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingQueryRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingQueryResponse;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingResponse;
import uk.gov.hmcts.reform.judicialbooking.domain.model.UserRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.ParseRequestService;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.PersistenceService;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.PrepareDataService;
import uk.gov.hmcts.reform.judicialbooking.helper.TestDataBuilder;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;


class BookingOrchestratorTest {

    private final ParseRequestService parseRequestService =
            mock(ParseRequestService.class);
    private final PrepareDataService prepareDataService =
            mock(PrepareDataService.class);
    private final PersistenceService persistenceService =
            mock(PersistenceService.class);


    @InjectMocks
    private BookingOrchestrator sut;

    private final BookingOrchestrator bookingOrchestrator =
            mock(BookingOrchestrator.class);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void queryBooking() {
        UserRequest userRequest = TestDataBuilder.buildRequestIds();

        List<BookingEntity> bookingEntities =
                List.of(TestDataBuilder.buildRetrievedBooking(userRequest.getUserIds().get(0)),
                        TestDataBuilder.buildRetrievedBooking(userRequest.getUserIds().get(1)));
        ResponseEntity<BookingQueryResponse> expectedResponse =
                ResponseEntity.ok(TestDataBuilder.buildQueryResponse(bookingEntities));

        when(parseRequestService.parseQueryRequest(any())).thenReturn(userRequest.getUserIds());
        when(prepareDataService.prepareQueryResponse(any())).thenReturn(expectedResponse);
        when(persistenceService.getValidBookings(userRequest.getUserIds())).thenReturn(bookingEntities);

        ResponseEntity<BookingQueryResponse> response = sut.queryBookings(
                BookingQueryRequest.builder().queryRequest(userRequest).build());

        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(Objects.requireNonNull(response.getBody()).getBookingEntities());
        Assertions.assertEquals(2, response.getBody().getBookingEntities().size());
        Assertions.assertEquals(userRequest.getUserIds().get(0),
                response.getBody().getBookingEntities().get(0).getUserId());
        Assertions.assertEquals(userRequest.getUserIds().get(1),
                response.getBody().getBookingEntities().get(1).getUserId());

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

    @Test
    void deleteBookingByUserId() {
        ResponseEntity<Void> expectedResponse = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        when(bookingOrchestrator.deleteBookingByUserId(any())).thenReturn(expectedResponse);
        ResponseEntity<Void> response = sut.deleteBookingByUserId(UUID.randomUUID().toString());
        assertNotNull(response);
        Assert.assertEquals(expectedResponse.getStatusCode(), response.getStatusCode());
    }

    @Test
    void deleteBookingByInvalidUserId() {
        final String inputs = "abcde";
        ResponseEntity<Void> expectedResponse = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        when(bookingOrchestrator.deleteBookingByUserId(any())).thenReturn(expectedResponse);
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () ->
                sut.deleteBookingByUserId(inputs));
        Assertions.assertTrue(exception.getLocalizedMessage().contains(String.format(
                "The input parameter: \"%s\", does not comply with the required pattern",
                inputs)));
    }

    @Test
    void deleteBookingVerify() {
        ResponseEntity<Void> response = sut.deleteBookingByUserId(UUID.randomUUID().toString());
        bookingOrchestrator.deleteBookingByUserId(any());
        Mockito.verify(bookingOrchestrator, times(1)).deleteBookingByUserId(any());
    }

    @Test
    void deleteNullBookingVerify() {
        bookingOrchestrator.deleteBookingByUserId(null);
        Mockito.verify(bookingOrchestrator, times(1)).deleteBookingByUserId(null);
    }
}