package uk.gov.hmcts.reform.judicialbooking.domain.service.common;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.ResourceNotFoundException;
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
public class PrepareDataService {

    public BookingEntity prepareBooking(BookingEntity booking, List<JudicialUserProfile> judicialUserProfiles) {

        List<Appointment> appointment = judicialUserProfiles.get(0).getAppointments().stream()
                .filter(appointment1 -> appointment1.getAppointmentId().equals(booking.getAppointmentId()))
                .collect(Collectors.toList());

        if (appointment.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Appointment retrieved from JRD is empty, your appointmentId may be invalid");
        }

        return prepareBookingVars(appointment, booking);
    }

    public BookingEntity prepareBookingVars(List<Appointment> appointment, BookingEntity booking) {
        if (StringUtils.isBlank(booking.getRoleId())) {
            booking.setRoleId(appointment.get(0).getRoleId());
        }
        if (StringUtils.isBlank(booking.getBaseLocationId())) {
            booking.setBaseLocationId(appointment.get(0).getBaseLocationId());
        }
        booking.setContractTypeId(appointment.get(0).getContractTypeId());
        //TODO booking.setRegionId(); this one requires another endpoint call I think
        booking.setStatus(Status.NEW.toString());

        return booking;
    }

    public OrmBookingAssignmentsRequest prepareOrmBookingRequest(BookingEntity booking,
                                                                 List<JudicialUserProfile> judicialUserProfiles) {
        OrmBooking ormBooking = convertBookingToOrmBooking(booking);
        OrmBookingRequest ormBookingRequest = OrmBookingRequest.builder()
                .actorId(booking.getUserId())
                .authorisationIds(
                        judicialUserProfiles.get(0).getAuthorisations().stream()
                                .map(Authorisation::getAuthorisationId).collect(Collectors.toList())
                )
                .build();
        return OrmBookingAssignmentsRequest.builder()
                .bookings(Collections.singletonList(ormBooking)).bookingRequest(ormBookingRequest).build();
    }

    public ResponseEntity<BookingResponse> prepareBookingResponse(BookingEntity bookingEntity) {
        //TODO do we need to return different http code or just let the status in body show status of request?
        return ResponseEntity.status(HttpStatus.OK).body(new BookingResponse(bookingEntity));
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
