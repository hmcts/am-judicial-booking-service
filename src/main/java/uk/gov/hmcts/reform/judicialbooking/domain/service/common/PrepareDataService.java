package uk.gov.hmcts.reform.judicialbooking.domain.service.common;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.UnprocessableEntityException;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.Appointment;
import uk.gov.hmcts.reform.judicialbooking.domain.model.Authorisation;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingResponse;
import uk.gov.hmcts.reform.judicialbooking.domain.model.JudicialUserProfile;
import uk.gov.hmcts.reform.judicialbooking.domain.model.OrmBooking;
import uk.gov.hmcts.reform.judicialbooking.domain.model.OrmBookingAssignmentsRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.OrmBookingRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.enums.Status;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequestScope
public class PrepareDataService {

    public BookingEntity prepareBooking(BookingEntity booking, JudicialUserProfile judicialUserProfile) {
        //TODO test this by manipulating the data coming in
        List<Appointment> appointment = judicialUserProfile.getAppointments().stream()
                .filter(appointment1 -> appointment1.getAppointmentId().equals(booking.getAppointmentId()))
                .collect(Collectors.toList());

        if (appointment.isEmpty()) {
            throw new UnprocessableEntityException("Given appointment couldn't be found for this user");
        }

        return prepareBookingVars(appointment.get(0), booking);
    }

    public BookingEntity prepareBookingVars(Appointment appointment, BookingEntity booking) {
        if (StringUtils.isBlank(booking.getRoleId())) {
            booking.setRoleId(appointment.getRoleId());
        }
        if (StringUtils.isBlank(booking.getBaseLocationId())) {
            booking.setBaseLocationId(appointment.getBaseLocationId());
        }
        booking.setContractTypeId(appointment.getContractTypeId());
        booking.setRegionId(appointment.getRegionId());
        booking.setStatus(Status.NEW.toString());

        return booking;
    }

    public OrmBookingAssignmentsRequest prepareOrmBookingRequest(BookingEntity booking,
                                                                 JudicialUserProfile judicialUserProfiles) {
        OrmBooking ormBooking = convertBookingToOrmBooking(booking);
        OrmBookingRequest ormBookingRequest = OrmBookingRequest.builder()
                .actorId(booking.getUserId())
                .authorisationIds(
                        judicialUserProfiles.getAuthorisations().stream()
                                .map(Authorisation::getAuthorisationId).collect(Collectors.toList())
                )
                .build();
        return OrmBookingAssignmentsRequest.builder()
                .bookings(Collections.singletonList(ormBooking))
                .bookingRequest(ormBookingRequest).build();
    }

    public ResponseEntity<BookingResponse> prepareBookingResponse(BookingEntity bookingEntity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new BookingResponse(bookingEntity));
    }

    public OrmBooking convertBookingToOrmBooking(BookingEntity booking) {
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

}
