package uk.gov.hmcts.reform.judicialbooking.domain.service.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.ResourceNotFoundException;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;

import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingsResponse;
import java.util.List;

@Service
public class PrepareDataService {

    public ResponseEntity<BookingsResponse> prepareBookingResponse(List<BookingEntity> bookingList) {
        if (CollectionUtils.isEmpty(bookingList)) {
            throw new ResourceNotFoundException("");
        }
        return ResponseEntity.status(HttpStatus.OK).body(new BookingsResponse(bookingList));
    }

}