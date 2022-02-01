package uk.gov.hmcts.reform.judicialbooking.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class BookingResponse {
    @JsonProperty(value = "bookingResponse")
    private BookingEntity bookingResponseEntity;

}
