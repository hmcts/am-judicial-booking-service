package uk.gov.hmcts.reform.judicialbooking.domain.service.common;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingResponse;
import uk.gov.hmcts.reform.judicialbooking.helper.TestDataBuilder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void convertBookingToOrmBooking() {
        String bookingId = "5629957f-4dcd-40b8-a0b2-e64ff5898b30";
        BookingEntity preparedBooking = TestDataBuilder.buildPreparedBooking();
        preparedBooking.setId(UUID.fromString(bookingId));

        assertNotNull(preparedBooking);
        assertNotNull(preparedBooking.getId());
        assertEquals(bookingId, preparedBooking.getId());

    }

}