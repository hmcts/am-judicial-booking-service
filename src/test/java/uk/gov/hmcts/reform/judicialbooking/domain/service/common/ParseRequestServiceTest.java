package uk.gov.hmcts.reform.judicialbooking.domain.service.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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


    private ParseRequestService sut;

    @Mock
    private SecurityUtils securityUtils;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.sut = new ParseRequestService(securityUtils,"am_org_role_mapping_service");
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

        when(securityUtils.getServiceName()).thenReturn("am_org_role_mapping_service");
        List<String> parsedUserIds = sut.parseQueryRequest(
                BookingQueryRequest.builder().queryRequest(userRequest).build());

        Assertions.assertNotNull(parsedUserIds);
        Assertions.assertEquals(userRequest.getUserIds(), parsedUserIds);
        Assertions.assertEquals(2, parsedUserIds.size());
    }

    @Test
    void parseQueryRequestWithOldIdamId() {
        UserRequest userRequest = TestDataBuilder.buildRequestIdsWithOldIdamId();

        when(securityUtils.getServiceName()).thenReturn("am_org_role_mapping_service");
        List<String> parsedUserIds = sut.parseQueryRequest(
                BookingQueryRequest.builder().queryRequest(userRequest).build());

        Assertions.assertNotNull(parsedUserIds);
        Assertions.assertEquals(userRequest.getUserIds(), parsedUserIds);
        Assertions.assertEquals(3, parsedUserIds.size());
    }

    @Test
    void parseQueryRequestWithOldIdamIdWithNonServiceName() {
        UserRequest userRequest = TestDataBuilder.buildRequestIdsWithOldIdamId();

        when(securityUtils.getServiceName()).thenReturn("ccd");

        List<String> parsedUserIds = sut.parseQueryRequest(
                BookingQueryRequest.builder().queryRequest(userRequest).build());

        Assertions.assertNotNull(parsedUserIds);
        Assertions.assertEquals(userRequest.getUserIds(), parsedUserIds);
        Assertions.assertEquals(3, parsedUserIds.size());
    }

    @Test
    void parseQueryRequestWithOldIdamIdWithNullServiceName() {
        UserRequest userRequest = TestDataBuilder.buildRequestIdsWithOldIdamId();

        List<String> parsedUserIds = sut.parseQueryRequest(
                BookingQueryRequest.builder().queryRequest(userRequest).build());

        Assertions.assertNotNull(parsedUserIds);
        Assertions.assertEquals(userRequest.getUserIds(), parsedUserIds);
        Assertions.assertEquals(3, parsedUserIds.size());
    }

    @Test
    void parseQueryRequestWithInvalidOldIdamId() {
        UserRequest userRequest = TestDataBuilder.buildRequestIdsWithInvalidOldIdamId();

        BookingQueryRequest bookingQueryRequest =
                BookingQueryRequest.builder().queryRequest(userRequest).build();
        Assertions.assertThrows(BadRequestException.class, () -> sut.parseQueryRequest(bookingQueryRequest));

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
        Assertions.assertThrows(BadRequestException.class, () -> sut.parseBookingRequest(null));
    }

    @Test
    void testParseQueryRequest() {
        UserRequest userRequest = null;
        BookingQueryRequest bookingQueryRequest = BookingQueryRequest.builder().queryRequest(userRequest).build();
        Assertions.assertThrows(BadRequestException.class, () -> sut.parseQueryRequest(bookingQueryRequest));
    }

    @Test
    void testParseQueryValidateRequest() {
        UserRequest userRequest = TestDataBuilder.buildRequestIds();
        BookingQueryRequest bookingQueryRequestNonUserId =
                BookingQueryRequest.builder().queryRequest(userRequest).build();
        bookingQueryRequestNonUserId.getQueryRequest().setUserIds(List.of("abc"));
        Assertions.assertThrows(BadRequestException.class, () ->
                sut.parseQueryRequest(bookingQueryRequestNonUserId));
    }

    @Test
    void testParseBookingValidateRequest() {
        BookingRequest bookingRequest = TestDataBuilder.buildWrongBooking();
        Assertions.assertThrows(BadRequestException.class, () -> sut.parseBookingRequest(bookingRequest));
    }
}