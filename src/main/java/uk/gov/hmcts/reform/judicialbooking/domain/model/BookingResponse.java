package uk.gov.hmcts.reform.judicialbooking.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.validation.annotation.Validated;

@Data
@NoArgsConstructor
@Validated
public class BookingResponse {

    @JsonProperty("bookingResponse")
    private Booking bookingResponseObject;

    public BookingResponse(@NonNull Booking bookingResponseObject) {
        this.bookingResponseObject = bookingResponseObject;
    }
}
