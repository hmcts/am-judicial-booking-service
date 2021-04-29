package uk.gov.hmcts.reform.judicialbooking.helper;

import lombok.Setter;
import org.apache.bcel.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.Booking;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingResponse;
import uk.gov.hmcts.reform.judicialbooking.domain.model.enums.Status;

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

    public static Booking buildPreparedBooking() {
        return Booking.builder()
                .appointmentId("appointmentId")
                .baseLocationId("baseLocationId")
                .contractTypeId("contractTypeId")
                .regionId("regionId")
                .roleId("roleId")
                .userId("5629957f-4dcd-40b8-a0b2-e64ff5898b28")
                .created(ZonedDateTime.now())
                .beginTime(ZonedDateTime.now().plusDays(1))
                .endTime(ZonedDateTime.now().plusMonths(1))
                .status(Status.NEW.toString())
                .build();
    }

    public static BookingEntity buildBookingEntity(Booking preparedBooking) {
        return BookingEntity.builder()
                .userId(preparedBooking.getUserId())
                .appointmentId(preparedBooking.getAppointmentId())
                .baseLocationId(preparedBooking.getBaseLocationId())
                .roleId(preparedBooking.getRoleId())
                .regionId(preparedBooking.getRegionId())
                .contractTypeId(preparedBooking.getContractTypeId())
                .beginTime(preparedBooking.getBeginTime())
                .endTime(preparedBooking.getEndTime())
                .created(preparedBooking.getCreated())
                .status(preparedBooking.getStatus())
                .log(preparedBooking.getLog())
                .build();
    }



}
