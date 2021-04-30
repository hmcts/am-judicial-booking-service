package uk.gov.hmcts.reform.judicialbooking.util;

import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.Booking;

@Service
public class PersistenceUtil {

    public BookingEntity convertBookingToEntity(Booking booking) {
        return BookingEntity.builder()
                .userId(booking.getUserId())
                .appointmentId(booking.getAppointmentId())
                .baseLocationId(booking.getBaseLocationId())
                .roleId(booking.getRoleId())
                .regionId(booking.getRegionId())
                .contractTypeId(booking.getContractTypeId())
                .beginTime(booking.getBeginTime())
                .endTime(booking.getEndTime())
                .created(booking.getCreated())
                .status(booking.getStatus())
                .log(booking.getLog())
                .build();
    }
}
