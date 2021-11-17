package uk.gov.hmcts.reform.judicialbooking.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
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
import lombok.NonNull;

@Builder(toBuilder = true)
@Getter
@Setter
@Entity(name = "booking")
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BookingEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String appointmentId;

    @Column(nullable = false)
    private String roleId;

    @Column(nullable = false)
    private String contractTypeId;

    private String baseLocationId;

    private String regionId;

    @CreationTimestamp
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_PATTERN, timezone = Constants.TIMEZONE)
    private ZonedDateTime created;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_PATTERN, timezone = Constants.TIMEZONE)
    private ZonedDateTime beginTime;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_PATTERN, timezone = Constants.TIMEZONE)
    private ZonedDateTime endTime;

    @Column(nullable = false)
    private String status;

    private String log;

}