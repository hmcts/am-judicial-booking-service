package uk.gov.hmcts.reform.judicialbooking.helper;

import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.Appointment;
import uk.gov.hmcts.reform.judicialbooking.domain.model.Authorisation;
import uk.gov.hmcts.reform.judicialbooking.domain.model.Booking;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingResponse;
import uk.gov.hmcts.reform.judicialbooking.domain.model.JudicialUserProfile;
import uk.gov.hmcts.reform.judicialbooking.domain.model.OrmBooking;
import uk.gov.hmcts.reform.judicialbooking.domain.model.OrmBookingAssignmentsRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.OrmBookingRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.enums.Status;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.UUID;

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

    public static BookingResponse buildBookingResponse(Booking booking) {
        return new BookingResponse(booking);
    }

    public static Booking buildBooking() {
        return Booking.builder()
                .appointmentId("appointmentId")
                .beginTime(ZonedDateTime.now().plusDays(1))
                .endTime(ZonedDateTime.now().plusMonths(1))
                .build();
    }

    public static Booking buildParsedBooking() {
        return Booking.builder()
                .appointmentId("appointmentId")
                .userId("5629957f-4dcd-40b8-a0b2-e64ff5898b28")
                .created(ZonedDateTime.now())
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

    public static JudicialUserProfile buildJudicialProfile() {
        return JudicialUserProfile.builder()
                .fullName("Keith Gill")
                .contractTypeId("contractTypeId")
                .authorisations(Collections.singletonList(
                        Authorisation.builder()
                                .authorisationId("authId")
                                .build()))
                .appointments(Collections.singletonList(
                        Appointment.builder()
                                .appointmentId("appointmentId")
                                .baseLocationId("baseLocationId")
                                .roleId("roleId")
                                .contractTypeId("contractTypeId")
                                .build()))
                .build();
    }

    public static OrmBooking buildOrmBooking(Booking booking) {
        booking.setId(UUID.fromString(uuidString));
        return OrmBooking.builder()
                .bookingId(booking.getId().toString())
                .appointmentId(booking.getAppointmentId())
                .baseLocationId(booking.getBaseLocationId())
                .contractTypeId(booking.getContractTypeId())
                .regionId(booking.getRegionId())
                .roleId(booking.getRoleId())
                .beginTime(booking.getBeginTime())
                .endTime(booking.getEndTime())
                .created(booking.getCreated())
                .build();
    }

    public static OrmBookingRequest buildOrmBookingRequest() {
        return OrmBookingRequest.builder()
                .actorId(uuidString)
                .authorisations(
                        Collections.singletonList(Authorisation.builder().authorisationId("authId").build())
                )
                .build();

    }

    public static OrmBookingAssignmentsRequest buildOrmBookingAssignmentsRequest() {
        return OrmBookingAssignmentsRequest.builder()
                .bookings(Collections.singletonList(buildOrmBooking(buildPreparedBooking())))
                .bookingRequest(buildOrmBookingRequest())
                .build();
    }

}
