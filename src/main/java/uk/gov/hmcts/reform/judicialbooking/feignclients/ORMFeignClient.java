package uk.gov.hmcts.reform.judicialbooking.feignclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import uk.gov.hmcts.reform.judicialbooking.domain.model.OrmBookingAssignmentsRequest;
import uk.gov.hmcts.reform.judicialbooking.feignclients.configuration.DatastoreFeignClientConfiguration;
import uk.gov.hmcts.reform.judicialbooking.feignclients.configuration.ORMFeignClientFallback;

@FeignClient(value = "orgrolemappingclient", url = "${feign.client.config.ormclient.url}",
        configuration = DatastoreFeignClientConfiguration.class,
        fallback = ORMFeignClientFallback.class)
public interface ORMFeignClient {

    @PostMapping(value = "/am/role-mapping/judicial/bookings")
    ResponseEntity<Object> createBookingAssignments(OrmBookingAssignmentsRequest bookingAssignmentsRequest);
}
