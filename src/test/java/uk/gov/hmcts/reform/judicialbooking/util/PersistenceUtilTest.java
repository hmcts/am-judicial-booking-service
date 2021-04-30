package uk.gov.hmcts.reform.judicialbooking.util;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import uk.gov.hmcts.reform.judicialbooking.helper.TestDataBuilder;

import static org.junit.jupiter.api.Assertions.*;

class PersistenceUtilTest {

    PersistenceUtil persistenceUtil = new PersistenceUtil();

    @Test
    void convertBookingToEntity() {
        assertNotNull(persistenceUtil.convertBookingToEntity(TestDataBuilder.buildBooking()));
    }
}