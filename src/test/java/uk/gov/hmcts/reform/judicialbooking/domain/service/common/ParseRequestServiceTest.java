package uk.gov.hmcts.reform.judicialbooking.domain.service.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.BadRequestException;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingQueryRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.RequestIds;
import uk.gov.hmcts.reform.judicialbooking.helper.TestDataBuilder;
import uk.gov.hmcts.reform.judicialbooking.util.SecurityUtils;

import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.List;

class ParseRequestServiceTest {

    @InjectMocks
    private ParseRequestService sut;

    @Mock
    private SecurityUtils securityUtils;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void parseBookingRequest() throws ParseException {
        String userId = "5629957f-4dcd-40b8-a0b2-e64ff5898b28";
        when(securityUtils.getUserId()).thenReturn(userId);

        BookingEntity booking = sut.parseBookingRequest(TestDataBuilder.buildBookingRequest());
        Assertions.assertNotNull(booking.getCreated());
        Assertions.assertEquals(userId, booking.getUserId());
    }

    @Test
    void parseQueryRequest() throws ParseException {
        RequestIds requestIds = TestDataBuilder.buildRequestIds();

        List<String> parsedUserIds =
                sut.parseQueryRequest(
                        BookingQueryRequest.builder().queryRequest(requestIds).build());

        Assertions.assertNotNull(parsedUserIds);
        Assertions.assertEquals(requestIds.getUserIds(), parsedUserIds);
        Assertions.assertEquals(2, parsedUserIds.size());
    }

    @Test
    void parseQueryRequest_EmptyIds() {
        RequestIds requestIds = RequestIds.builder().build();

        BookingQueryRequest bookingQueryRequest =
                BookingQueryRequest.builder().queryRequest(requestIds).build();

        Assertions.assertThrows(BadRequestException.class, () -> {
            sut.parseQueryRequest(bookingQueryRequest);
        });
    }
}