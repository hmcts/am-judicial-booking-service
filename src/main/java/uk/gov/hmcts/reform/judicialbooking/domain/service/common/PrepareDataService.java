package uk.gov.hmcts.reform.judicialbooking.domain.service.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingResponse;
import uk.gov.hmcts.reform.judicialbooking.domain.model.OrmBookingAssignmentsRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.OrmUserRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.enums.Status;

import java.util.Arrays;

@Service
@RequestScope
public class PrepareDataService {

    public BookingEntity prepareBooking(BookingEntity booking) {
        booking.setStatus(Status.NEW.toString());
        return booking;
    }

    public OrmBookingAssignmentsRequest prepareOrmBookingRequest(BookingEntity booking) {
        OrmUserRequest ormUserRequest = OrmUserRequest.builder()
                .actorIds(Arrays.asList(booking.getUserId()))
                .build();
        return OrmBookingAssignmentsRequest.builder()
                .userRequest(ormUserRequest)
                .bookings(Arrays.asList(booking))
                .build();
    }

    public ResponseEntity<BookingResponse> prepareBookingResponse(BookingEntity bookingEntity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new BookingResponse(bookingEntity));
    }

}
