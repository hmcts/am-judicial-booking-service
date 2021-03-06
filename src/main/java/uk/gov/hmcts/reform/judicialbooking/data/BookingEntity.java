package uk.gov.hmcts.reform.judicialbooking.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import uk.gov.hmcts.reform.judicialbooking.util.Constants;

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
    @ApiModelProperty(required = false, hidden = true)
    private UUID id;

    @Column(name = "user_Id", nullable = false)
    @ApiModelProperty(required = false, hidden = true)
    private String userId;

    @Column(name = "appointment_id", nullable = false)
    private String appointmentId;

    @Column(name = "role_id", nullable = false)
    @ApiModelProperty(required = false, hidden = true)
    private String roleId;

    @Column(name = "contract_type_id", nullable = false)
    @ApiModelProperty(required = false, hidden = true)
    private String contractTypeId;

    @Column(name = "base_location_id")
    private String baseLocationId;

    @Column(name = "region_id")
    private String regionId;

    @CreationTimestamp
    @Column(name = "created", nullable = false)
    @ApiModelProperty(required = false, hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_PATTERN, timezone = Constants.TIMEZONE)
    private ZonedDateTime created;

    @Column(name = "begin_time", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_PATTERN, timezone = Constants.TIMEZONE)
    private ZonedDateTime beginTime;

    @Column(name = "end_time", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_PATTERN, timezone = Constants.TIMEZONE)
    private ZonedDateTime endTime;

    @Column(name = "status", nullable = false)
    @ApiModelProperty(required = false, hidden = true)
    private String status;

    @Column(name = "log")
    @ApiModelProperty(required = false, hidden = true)
    private String log;


}