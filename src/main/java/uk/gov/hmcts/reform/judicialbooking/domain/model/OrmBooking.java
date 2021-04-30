package uk.gov.hmcts.reform.judicialbooking.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrmBooking {

    private String bookingId;
    private String appointmentId;
    private String roleId;
    private String contractTypeId;
    private String baseLocationId;
    private String regionId;
    private ZonedDateTime created;
    private ZonedDateTime beginTime;
    private ZonedDateTime endTime;

}
