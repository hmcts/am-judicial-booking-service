package uk.gov.hmcts.reform.judicialbooking.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.validation.annotation.Validated;

@Data
@NoArgsConstructor
@Validated
public class BookingRequest {

    @JsonProperty("bookingRequest")
    private Booking bookingRequestObject;

    public BookingRequest(@NonNull Booking bookingRequestObject) {
        this.bookingRequestObject = bookingRequestObject;
    }
}
