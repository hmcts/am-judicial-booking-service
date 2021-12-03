package uk.gov.hmcts.reform.judicialbooking.domain.service.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingQueryResponse;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingResponse;

import java.util.List;

@Service
@RequestScope
public class PrepareDataService {


    public ResponseEntity<BookingResponse> prepareBookingResponse(BookingEntity bookingEntity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new BookingResponse(bookingEntity));
    }

    public ResponseEntity<BookingQueryResponse> prepareQueryResponse(List<BookingEntity> bookingEntities) {
        return ResponseEntity.status(HttpStatus.OK).body(new BookingQueryResponse(bookingEntities));
    }

}
