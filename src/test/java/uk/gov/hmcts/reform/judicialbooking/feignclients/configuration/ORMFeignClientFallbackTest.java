package uk.gov.hmcts.reform.judicialbooking.feignclients.configuration;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.judicialbooking.helper.TestDataBuilder;

import static org.junit.jupiter.api.Assertions.*;

class ORMFeignClientFallbackTest {

    private final ORMFeignClientFallback ormFeignClientFallback = new ORMFeignClientFallback();

    @Test
    void createBookingAssignments() {
        assertNotNull(ormFeignClientFallback.createBookingAssignments(
                TestDataBuilder.buildOrmBookingAssignmentsRequest()
        ));
    }
}