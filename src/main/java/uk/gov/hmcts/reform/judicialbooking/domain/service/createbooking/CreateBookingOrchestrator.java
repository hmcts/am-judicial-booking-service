package uk.gov.hmcts.reform.judicialbooking.domain.service.createbooking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingResponse;

import java.text.ParseException;

@Slf4j
@Service
public class CreateBookingOrchestrator {

    BookingEntity bookingEntity;

    public ResponseEntity<BookingResponse> createBooking(BookingRequest bookingRequest) throws ParseException {


        return ResponseEntity.status(HttpStatus.OK).body(new BookingResponse());
    }
}
