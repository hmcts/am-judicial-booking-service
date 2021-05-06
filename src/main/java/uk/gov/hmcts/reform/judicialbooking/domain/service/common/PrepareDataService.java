package uk.gov.hmcts.reform.judicialbooking.domain.service.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.ResourceNotFoundException;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.*;
import uk.gov.hmcts.reform.judicialbooking.domain.model.enums.Status;

import java.util.Collections;
import java.util.List;

@Service
public class PrepareDataService {

    public BookingEntity prepareBooking(BookingEntity booking, List<JudicialUserProfile> judicialUserProfiles) {

        booking.setRoleId(judicialUserProfiles.get(0).getAppointments().get(0).getRoleId());
        booking.setContractTypeId(judicialUserProfiles.get(0).getContractTypeId());
        //TODO what to do when it is provided in initial request replace or compare?
        booking.setBaseLocationId(judicialUserProfiles.get(0).getAppointments().get(0).getBaseLocationId());
        //TODO booking.setRegionId(); this one requires another endpoint call I think
        booking.setStatus(Status.NEW.toString());
        return booking;
    }

    public OrmBookingAssignmentsRequest prepareOrmBookingRequest(BookingEntity booking,
                                                                 List<JudicialUserProfile> judicialUserProfiles) {
        OrmBooking ormBooking = convertBookingToOrmBooking(booking);
        OrmBookingRequest ormBookingRequest = OrmBookingRequest.builder()
                .actorId(booking.getUserId())
                .authorisations(judicialUserProfiles.get(0).getAuthorisations())
                .build();
        return OrmBookingAssignmentsRequest.builder()
                .bookings(Collections.singletonList(ormBooking)).bookingRequest(ormBookingRequest).build();
    }

    public ResponseEntity<BookingResponse> prepareBookingResponse(BookingEntity bookingEntity) {
        BookingEntity booking = convertBookingEntityToBooking(bookingEntity);
        //TODO do we need to return different http code or just let the status in body show status of request?
        return ResponseEntity.status(HttpStatus.OK).body(new BookingResponse(
                booking));
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

    public BookingEntity convertBookingEntityToBooking(BookingEntity bookingEntity) {
        return BookingEntity.builder()
                .userId(bookingEntity.getUserId())
                .appointmentId(bookingEntity.getAppointmentId())
                .baseLocationId(bookingEntity.getBaseLocationId())
                .roleId(bookingEntity.getRoleId())
                .regionId(bookingEntity.getRegionId())
                .contractTypeId(bookingEntity.getContractTypeId())
                .beginTime(bookingEntity.getBeginTime())
                .endTime(bookingEntity.getEndTime())
                .created(bookingEntity.getCreated())
                .status(bookingEntity.getStatus())
                .log(bookingEntity.getLog())
                .build();
    }

    public ResponseEntity<BookingsResponse> prepareBookingResponse(List<BookingEntity> bookingList) {
        if (CollectionUtils.isEmpty(bookingList)) {
            throw new ResourceNotFoundException("");
        }

        return ResponseEntity.status(HttpStatus.OK).body(new BookingsResponse(bookingList));
    }

}