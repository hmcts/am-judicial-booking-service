package uk.gov.hmcts.reform.judicialbooking.domain.model;

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

    private BookingEntity bookingResponse;

}
