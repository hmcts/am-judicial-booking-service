package uk.gov.hmcts.reform.judicialbooking.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class DeleteBookingControllerIntegrationTest extends BaseTestIntegration {
    private static final String URL = "/am/bookings/";
    private static final String USERID = "631d322c-eea7-4d53-bd92-e6ec51bcb390";

    @Test
    public void deleteBookingByUserIdApiTest() throws Exception {
        getRequestSpecification()
                .when().delete(String.format("%s%s", URL, USERID))
                .then().assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
