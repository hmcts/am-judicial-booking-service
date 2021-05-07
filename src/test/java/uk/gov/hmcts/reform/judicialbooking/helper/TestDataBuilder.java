package uk.gov.hmcts.reform.judicialbooking.helper;

import lombok.Setter;

import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.enums.Status;

import java.time.ZonedDateTime;


@Setter
public class TestDataBuilder {

    static String uuidString = "5629957f-4dcd-40b8-a0b2-e64ff5898b28";

    private TestDataBuilder() {
        //not meant to be instantiated.
    }

    public static BookingEntity buildBooking() {
        return BookingEntity.builder()
                .appointmentId("appointmentId")
                .beginTime(ZonedDateTime.now().plusDays(1))
                .endTime(ZonedDateTime.now().plusMonths(1))
                .build();
    }

    public static BookingEntity buildParsedBooking() {
        return BookingEntity.builder()
                .appointmentId("appointmentId")
                .userId("5629957f-4dcd-40b8-a0b2-e64ff5898b28")
                .created(ZonedDateTime.now())
                .beginTime(ZonedDateTime.now().plusDays(1))
                .endTime(ZonedDateTime.now().plusMonths(1))
                .build();
    }

    public static BookingEntity buildPreparedBooking() {
        return BookingEntity.builder()
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

    public static BookingEntity buildBookingEntity(BookingEntity preparedBooking) {
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