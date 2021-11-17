package uk.gov.hmcts.reform.judicialbooking.domain.service.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.helper.TestDataBuilder;
import uk.gov.hmcts.reform.judicialbooking.util.SecurityUtils;

import static org.mockito.Mockito.when;

import java.text.ParseException;

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
        //Assertions.assertTrue(booking.getCreated().toString().contains("Z"));
    }
}