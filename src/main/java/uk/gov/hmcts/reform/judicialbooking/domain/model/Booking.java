package uk.gov.hmcts.reform.judicialbooking.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    private UUID id;
    private String userId;
    private String appointmentId;
    private String roleId;
    private String contractTypeId;
    private String baseLocationId;
    private String regionId;
    private ZonedDateTime created;
    private ZonedDateTime beginTime;
    private ZonedDateTime endTime;
    private String status;
    private String log;
}
