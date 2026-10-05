package uk.gov.hmcts.reform.judicialbooking.controller.testingsupport;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.reform.judicialbooking.controller.BaseAuthorisedTestIntegration;

class DeleteBookingControllerIntegrationTest extends BaseAuthorisedTestIntegration {
    private static final String URL = "/am/testing-support/bookings/";

    @Test
    void deleteBookingByUserIdApiTest() throws Exception {
        getRequestSpecification()
                .when().delete(String.format("%s%s", URL, ACTOR_ID1))
                .then().assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

}
