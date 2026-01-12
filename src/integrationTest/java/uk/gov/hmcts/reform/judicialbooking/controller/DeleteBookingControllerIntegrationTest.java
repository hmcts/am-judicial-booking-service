package uk.gov.hmcts.reform.judicialbooking.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static uk.gov.hmcts.reform.judicialbooking.controller.utils.WiremockFixtures.ACTOR_ID1;

public class DeleteBookingControllerIntegrationTest extends BaseTestIntegration {
    private static final String URL = "/am/bookings/";

    @Test
    public void deleteBookingByUserIdApiTest() throws Exception {
        getRequestSpecification()
                .when().delete(String.format("%s%s", URL, ACTOR_ID1))
                .then().assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
