package uk.gov.hmcts.reform.judicialbooking.helper;

import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingResponse;
import uk.gov.hmcts.reform.judicialbooking.domain.model.OrmBookingAssignmentsRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.OrmUserRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.enums.Status;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;

@Setter
public class TestDataBuilder {

    static String uuidString = "5629957f-4dcd-40b8-a0b2-e64ff5898b28";

    private TestDataBuilder() {
        //not meant to be instantiated.
    }

    public static ResponseEntity<BookingResponse> buildCreateBookingResponse() {
        return ResponseEntity.status(HttpStatus.OK).body(new BookingResponse());
    }

    public static BookingRequest buildBookingRequest() {
        return new BookingRequest(buildBooking());
    }

    public static BookingResponse buildBookingResponse(BookingEntity booking) {
        return new BookingResponse(booking);
    }

    public static BookingEntity buildBooking() {
        return BookingEntity.builder()
                .beginTime(ZonedDateTime.now().plusDays(1))
                .endTime(ZonedDateTime.now().plusMonths(1))
                .build();
    }

    public static BookingEntity buildParsedBooking() {
        return BookingEntity.builder()
                .userId("5629957f-4dcd-40b8-a0b2-e64ff5898b28")
                .created(ZonedDateTime.now())
                .beginTime(ZonedDateTime.now().plusDays(1))
                .endTime(ZonedDateTime.now().plusMonths(1))
                .build();
    }

    public static BookingEntity buildPreparedBooking() {
        return BookingEntity.builder()
                .locationId("baseLocationId")
                .regionId("regionId")
                .userId("5629957f-4dcd-40b8-a0b2-e64ff5898b28")
                .created(ZonedDateTime.now())
                .beginTime(ZonedDateTime.now().plusDays(1))
                .endTime(ZonedDateTime.now().plusMonths(1))
                .status(Status.NEW.toString())
                .build();
    }


    public static OrmUserRequest buildOrmBookingRequest() {
        return OrmUserRequest.builder()
                .actorIds(Arrays.asList(uuidString))
                .build();

    }

    public static OrmBookingAssignmentsRequest buildOrmBookingAssignmentsRequest() {
        return OrmBookingAssignmentsRequest.builder()
                .bookings(Collections.singletonList(buildPreparedBooking()))
                .userRequest(buildOrmBookingRequest())
                .build();
    }

}
