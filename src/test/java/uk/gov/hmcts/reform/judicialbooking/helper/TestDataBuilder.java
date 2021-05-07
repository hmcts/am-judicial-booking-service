package uk.gov.hmcts.reform.judicialbooking.helper;

import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.Appointment;
import uk.gov.hmcts.reform.judicialbooking.domain.model.Authorisation;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingResponse;
import uk.gov.hmcts.reform.judicialbooking.domain.model.JudicialUserProfile;
import uk.gov.hmcts.reform.judicialbooking.domain.model.OrmBooking;
import uk.gov.hmcts.reform.judicialbooking.domain.model.OrmBookingAssignmentsRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.OrmBookingRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.enums.Status;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

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
                .appointmentId("appointmentId_1")
                .beginTime(ZonedDateTime.now().plusDays(1))
                .endTime(ZonedDateTime.now().plusMonths(1))
                .build();
    }

    public static BookingEntity buildParsedBooking() {
        return BookingEntity.builder()
                .appointmentId("appointmentId_1")
                .userId("5629957f-4dcd-40b8-a0b2-e64ff5898b28")
                .created(ZonedDateTime.now())
                .beginTime(ZonedDateTime.now().plusDays(1))
                .endTime(ZonedDateTime.now().plusMonths(1))
                .build();
    }

    public static BookingEntity buildPreparedBooking() {
        return BookingEntity.builder()
                .appointmentId("appointmentId_1")
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

    public static JudicialUserProfile buildJudicialProfile() {
        return JudicialUserProfile.builder()
                .fullName("Keith Gill")
                .emailId("Keith.Gill@robinhood.com")
                .contractTypeId("contractTypeId")
                .idamId("idamId")
                .knowAs("Geohot")
                .personalCode("PS32010")
                .authorisations(List.of(
                        Authorisation.builder().authorisationId("authId_1").build(),
                        Authorisation.builder().authorisationId("authId_2").build(),
                        Authorisation.builder().authorisationId("authId_3").build()
                )).appointments(List.of(
                        Appointment.builder()
                                .appointmentId("appointmentId_1")
                                .baseLocationId("baseLocationId_1")
                                .roleId("roleId_1")
                                .contractTypeId("contractTypeId_1")
                                .build(),
                        Appointment.builder()
                                .appointmentId("appointmentId_2")
                                .baseLocationId("baseLocationId_2")
                                .roleId("roleId_2")
                                .contractTypeId("contractTypeId_2")
                                .build()
                ))
                .build();
    }

    public static OrmBooking buildOrmBooking(BookingEntity booking) {
        booking.setId(uuidString);
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
                .authorisationIds(
                        List.of("authId")
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
