package uk.gov.hmcts.reform.judicialbooking.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.validation.annotation.Validated;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;

@Data
@NoArgsConstructor
@Validated
public class BookingRequest {

    @JsonProperty("bookingRequest")
    private BookingEntity bookingRequestObject;

    public BookingRequest(@NonNull BookingEntity bookingRequestObject) {
        this.bookingRequestObject = bookingRequestObject;
    }
}
