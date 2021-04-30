package uk.gov.hmcts.reform.judicialbooking.util;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.judicialbooking.helper.TestDataBuilder;

import static org.junit.jupiter.api.Assertions.assertNotNull;


class PersistenceUtilTest {

    PersistenceUtil persistenceUtil = new PersistenceUtil();

    @Test
    void convertBookingToEntity() {
        assertNotNull(persistenceUtil.convertBookingToEntity(TestDataBuilder.buildBooking()));
    }
}