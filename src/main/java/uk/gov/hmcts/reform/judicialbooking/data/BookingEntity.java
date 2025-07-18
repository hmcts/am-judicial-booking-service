package uk.gov.hmcts.reform.judicialbooking.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.gov.hmcts.reform.judicialbooking.util.Constants;

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
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid2")
    private UUID id;

    private String userId;

    private String regionId;

    private String locationId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATETIME_PATTERN, timezone = Constants.TIMEZONE)
    private ZonedDateTime created;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATETIME_PATTERN, timezone = Constants.TIMEZONE)
    private ZonedDateTime beginTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATETIME_PATTERN, timezone = Constants.TIMEZONE)
    private ZonedDateTime endTime;

    @Transient
    private String log;

}