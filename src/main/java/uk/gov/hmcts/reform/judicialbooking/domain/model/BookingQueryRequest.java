package uk.gov.hmcts.reform.judicialbooking.domain.model;

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

    private RequestIds queryRequest;

    public BookingQueryRequest(@NonNull RequestIds queryRequest) {
        this.queryRequest = queryRequest;
    }
}
