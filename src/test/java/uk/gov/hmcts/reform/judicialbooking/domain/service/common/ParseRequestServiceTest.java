package uk.gov.hmcts.reform.judicialbooking.domain.service.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.BadRequestException;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.UnprocessableEntityException;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingQueryRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.UserRequest;
import uk.gov.hmcts.reform.judicialbooking.helper.TestDataBuilder;
import uk.gov.hmcts.reform.judicialbooking.util.SecurityUtils;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

class ParseRequestServiceTest {

    @InjectMocks
    private ParseRequestService sut;

    @Mock
    private SecurityUtils securityUtils;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void parseBookingRequest() {
        String userId = "5629957f-4dcd-40b8-a0b2-e64ff5898b28";
        when(securityUtils.getUserId()).thenReturn(userId);

        BookingEntity booking = sut.parseBookingRequest(TestDataBuilder.buildBookingRequest());
        Assertions.assertNotNull(booking.getCreated());
        Assertions.assertEquals(userId, booking.getUserId());
    }

    @Test
    void parseBookingRequestWrongIdPassed() {
        String userId = "5629957f-4dcd-40b8-a0b2-e64ff5898b28";
        when(securityUtils.getUserId()).thenReturn(userId);
        String requestUserId = "5629957f-4dcd-40b8-a0b2-e64ff5898b40";

        BookingRequest bookingRequest = TestDataBuilder.buildBookingRequest();
        bookingRequest.setUserId(requestUserId);

        Assertions.assertThrows(UnprocessableEntityException.class, () -> sut.parseBookingRequest(bookingRequest));
    }

    @Test
    void parseQueryRequest() {
        UserRequest userRequest = TestDataBuilder.buildRequestIds();

        List<String> parsedUserIds = sut.parseQueryRequest(
                BookingQueryRequest.builder().queryRequest(userRequest).build());

        Assertions.assertNotNull(parsedUserIds);
        Assertions.assertEquals(userRequest.getUserIds(), parsedUserIds);
        Assertions.assertEquals(2, parsedUserIds.size());
    }

    @Test
    void parseQueryRequest_EmptyIds() {
        UserRequest userRequest = UserRequest.builder().build();

        BookingQueryRequest bookingQueryRequest =
                BookingQueryRequest.builder().queryRequest(userRequest).build();

        Assertions.assertThrows(BadRequestException.class, () -> sut.parseQueryRequest(bookingQueryRequest));
    }

    @Test
    void parseQueryRequest_userIdAsParam() {
        String userId = UUID.randomUUID().toString();
        BookingRequest request = TestDataBuilder.buildBookingRequest();
        request.setUserId(userId);
        when(securityUtils.getUserId()).thenReturn(userId);

        BookingEntity entity = sut.parseBookingRequest(request);
        Assertions.assertEquals(userId, entity.getUserId());
    }

    @Test
    void testParseBookingRequest() {
        Assertions.assertThrows(NullPointerException.class, () -> sut.parseBookingRequest(null));
    }

    @Test
    void testParseQueryRequest() {
        UserRequest userRequest = null;
        BookingQueryRequest bookingQueryRequest = BookingQueryRequest.builder().queryRequest(userRequest).build();
        Assertions.assertThrows(BadRequestException.class, () -> sut.parseQueryRequest(bookingQueryRequest));
    }

    @Test
    void testPareseQueryValidateRequest() {
        UserRequest userRequest = TestDataBuilder.buildRequestIds();
        BookingQueryRequest bookingQueryRequestnouserid =
                BookingQueryRequest.builder().queryRequest(userRequest).build();
        bookingQueryRequestnouserid.getQueryRequest().setUserIds(List.of("abc"));
        Assertions.assertThrows(UnprocessableEntityException.class, () ->
                sut.parseQueryRequest(bookingQueryRequestnouserid));
    }

    @Test
    void testParseBookingValidateRequest() {
        BookingRequest bookingRequest = TestDataBuilder.buildWrongBooking();
        Assertions.assertThrows(BadRequestException.class, () -> sut.parseBookingRequest(bookingRequest));
    }
}