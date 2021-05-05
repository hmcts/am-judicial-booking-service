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
public class BookingsResponse {

    @JsonProperty("bookings")
    private List<BookingEntity> bookingList;

    public BookingsResponse(@NonNull List<BookingEntity> bookingsObject) {
        this.bookingList = bookingList;
    }
}