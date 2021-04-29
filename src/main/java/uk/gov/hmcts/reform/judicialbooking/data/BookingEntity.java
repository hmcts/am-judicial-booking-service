package uk.gov.hmcts.reform.judicialbooking.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.time.ZonedDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Builder(toBuilder = true)
@Getter
@Setter
@Entity(name = "booking")
@NoArgsConstructor
@AllArgsConstructor
public class BookingEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @Column(name = "user_Id", nullable = false)
    private String userId;

    @Column(name = "appointment_id", nullable = false)
    private String appointmentId;

    @Column(name = "role_id", nullable = false)
    private String roleId;

    @Column(name = "contract_type_id", nullable = false)
    private String contractTypeId;

    @Column(name = "base_location_id")
    private String baseLocationId;

    @Column(name = "region_id")
    private String regionId;

    @CreationTimestamp
    @Column(name = "created", nullable = false)
    private ZonedDateTime created;

    @Column(name = "begin_time", nullable = false)
    private ZonedDateTime beginTime;

    @Column(name = "end_time", nullable = false)
    private ZonedDateTime endTime;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "log")
    private String log;


}