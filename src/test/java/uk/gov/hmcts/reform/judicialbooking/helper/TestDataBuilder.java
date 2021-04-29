package uk.gov.hmcts.reform.judicialbooking.helper;

import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.Booking;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingResponse;

import java.time.ZonedDateTime;

@Setter
public class TestDataBuilder {
    private TestDataBuilder() {
        //not meant to be instantiated.
    }

    public static ResponseEntity<BookingResponse> buildCreateBookingResponse() {
        return ResponseEntity.status(HttpStatus.OK).body(new BookingResponse());
    }

    public static BookingRequest buildBookingRequest() {
        return new BookingRequest(buildBooking());
    }

    public static Booking buildBooking() {
        return Booking.builder()
                .appointmentId("appointmentId")
                .beginTime(ZonedDateTime.now().plusDays(1))
                .endTime(ZonedDateTime.now().plusMonths(1))
                .build();
    }

}
