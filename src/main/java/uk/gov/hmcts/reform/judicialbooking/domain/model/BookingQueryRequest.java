package uk.gov.hmcts.reform.judicialbooking.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.validation.annotation.Validated;

@Data
@NoArgsConstructor
@Validated
@Builder
public class BookingQueryRequest {

    @JsonProperty("queryRequest")
    private RequestIds queryRequest;

    public BookingQueryRequest(@NonNull RequestIds queryRequest) {
        this.queryRequest = queryRequest;
    }
}
