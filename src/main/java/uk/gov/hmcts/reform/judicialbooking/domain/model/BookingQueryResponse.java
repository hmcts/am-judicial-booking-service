package uk.gov.hmcts.reform.judicialbooking.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class BookingQueryResponse {

    @JsonProperty("bookingQueryResponse")
    private List<BookingEntity> bookingEntities;

}
