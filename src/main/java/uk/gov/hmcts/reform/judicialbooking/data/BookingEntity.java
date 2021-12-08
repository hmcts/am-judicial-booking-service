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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

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

    @Column(name = "region_id")
    private String regionId;

    @Column(name = "location_id")
    private String locationId;

    @CreationTimestamp
    @Column(name = "created", nullable = false)
    @ApiModelProperty(required = false, hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATETIME_PATTERN, timezone = Constants.TIMEZONE)
    private ZonedDateTime created;

    @Column(name = "begin_time", nullable = false)
    private LocalDate beginDate;

    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATETIME_PATTERN, timezone = Constants.TIMEZONE)
    public ZonedDateTime getBeginDate() {
        return this.beginDate.atStartOfDay(ZoneId.of("UTC"));
    }

    @Column(name = "end_time", nullable = false)
    private LocalDate endDate;

    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATETIME_PATTERN, timezone = Constants.TIMEZONE)
    public ZonedDateTime getEndDate() {
        return this.endDate.atStartOfDay(ZoneId.of("UTC"));
    }

    @Column(name = "log")
    @ApiModelProperty(required = false, hidden = true)
    private String log;


}