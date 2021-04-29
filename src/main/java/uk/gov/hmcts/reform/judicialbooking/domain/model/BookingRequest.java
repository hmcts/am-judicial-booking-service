package uk.gov.hmcts.reform.judicialbooking.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.validation.annotation.Validated;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Validated
public class BookingRequest extends RepresentationModel<BookingResponse> {

    @JsonProperty("bookingRequest")
    private Booking bookingRequestObject;

    public BookingRequest(@NonNull Booking bookingRequestObject) {
        this.bookingRequestObject = bookingRequestObject;
    }
}
