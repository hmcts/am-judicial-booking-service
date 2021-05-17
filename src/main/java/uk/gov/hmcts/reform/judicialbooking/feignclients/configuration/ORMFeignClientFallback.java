package uk.gov.hmcts.reform.judicialbooking.feignclients.configuration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.judicialbooking.domain.model.OrmBookingAssignmentsRequest;
import uk.gov.hmcts.reform.judicialbooking.feignclients.ORMFeignClient;

@Component
public class ORMFeignClientFallback implements ORMFeignClient {

    @Override
    public ResponseEntity<Object> createBookingAssignments(OrmBookingAssignmentsRequest bookingAssignmentsRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
