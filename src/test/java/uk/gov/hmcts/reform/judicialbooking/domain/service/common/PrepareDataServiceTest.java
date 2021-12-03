package uk.gov.hmcts.reform.judicialbooking.domain.service.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingQueryResponse;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingResponse;
import uk.gov.hmcts.reform.judicialbooking.domain.model.RequestIds;
import uk.gov.hmcts.reform.judicialbooking.helper.TestDataBuilder;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class PrepareDataServiceTest {

    private final PrepareDataService sut = new PrepareDataService();

    @Test
    void prepareBookingResponse() {
        ResponseEntity<BookingResponse> bookingResponseEntity =
                sut.prepareBookingResponse(TestDataBuilder.buildPreparedBooking());

        assertNotNull(bookingResponseEntity);
    }

    @Test
    void prepareQueryResponse() {
        RequestIds requestIds = TestDataBuilder.buildRequestIds();

        ResponseEntity<BookingQueryResponse> response = sut.prepareQueryResponse(TestDataBuilder.buildListOfBookings());

        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(Objects.requireNonNull(response.getBody()).getBookingEntities());
        Assertions.assertEquals(2, response.getBody().getBookingEntities().size());
        Assertions.assertEquals(requestIds.getUserIds().get(0),
                response.getBody().getBookingEntities().get(0).getUserId());
        Assertions.assertEquals(requestIds.getUserIds().get(1),
                response.getBody().getBookingEntities().get(1).getUserId());
    }
}