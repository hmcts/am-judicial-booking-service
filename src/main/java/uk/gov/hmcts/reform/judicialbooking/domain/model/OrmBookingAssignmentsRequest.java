package uk.gov.hmcts.reform.judicialbooking.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrmBookingAssignmentsRequest {

    @JsonProperty(value = "bookingRequest")
    OrmBookingRequest bookingRequest;
    @JsonProperty(value = "bookings")
    List<OrmBooking> bookings;

}




