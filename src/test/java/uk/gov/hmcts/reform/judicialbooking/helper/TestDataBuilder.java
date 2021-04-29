package uk.gov.hmcts.reform.judicialbooking.helper;

import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingResponse;

@Setter
public class TestDataBuilder {
    private TestDataBuilder() {
        //not meant to be instantiated.
    }

    public static ResponseEntity<BookingResponse> buildCreateBookingResponse() {
        return ResponseEntity.status(HttpStatus.OK).body(new BookingResponse());
    }

}
