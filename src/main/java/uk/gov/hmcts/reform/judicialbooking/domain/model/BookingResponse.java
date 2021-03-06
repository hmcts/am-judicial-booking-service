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
public class BookingResponse {

    @JsonProperty("bookingResponse")
    private BookingEntity bookingResponse;

    public BookingResponse(@NonNull BookingEntity bookingResponse) {
        this.bookingResponse = bookingResponse;
    }
}
