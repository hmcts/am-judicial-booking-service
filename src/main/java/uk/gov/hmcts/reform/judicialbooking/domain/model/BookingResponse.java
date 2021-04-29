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
public class BookingResponse extends RepresentationModel<BookingResponse> {

    @JsonProperty("bookingResponse")
    private Booking bookingResponseObject;

    public BookingResponse(@NonNull Booking bookingResponseObject) {
        this.bookingResponseObject = bookingResponseObject;
    }
}
