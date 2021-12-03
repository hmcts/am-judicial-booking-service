package uk.gov.hmcts.reform.judicialbooking.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.validation.annotation.Validated;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;

import java.util.List;

@Data
@NoArgsConstructor
@Validated
public class BookingQueryResponse {

    @JsonProperty("bookingQueryResponse")
    private List<BookingEntity> bookingEntities;

    public BookingQueryResponse(@NonNull List<BookingEntity> bookingQueryResponse) {
        this.bookingEntities = bookingQueryResponse;
    }
}
