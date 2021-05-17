package uk.gov.hmcts.reform.judicialbooking.domain.service.common;

import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.judicialbooking.domain.model.OrmBookingAssignmentsRequest;
import uk.gov.hmcts.reform.judicialbooking.feignclients.ORMFeignClient;

@Service
public class OrgRoleMappingService {

    private final ORMFeignClient ormFeignClient;

    public OrgRoleMappingService(ORMFeignClient ormFeignClient) {
        this.ormFeignClient = ormFeignClient;
    }

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 3))
    public ResponseEntity<Object> createBookingAssignments(OrmBookingAssignmentsRequest bookingAssignmentsRequest) {
        return ormFeignClient.createBookingAssignments(bookingAssignmentsRequest);
    }

}
