package uk.gov.hmcts.reform.judicialbooking.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrmBookingAssignmentsRequest {

    @JsonProperty(value = "userRequest")
    OrmUserRequest userRequest;
    @JsonProperty(value = "bookings")
    List<BookingEntity> bookings;

}




