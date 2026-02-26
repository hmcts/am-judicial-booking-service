package uk.gov.hmcts.reform.judicialbooking.controller.testingsupport;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import uk.gov.hmcts.reform.judicialbooking.controller.BaseAuthorisedTestIntegration;

import static uk.gov.hmcts.reform.judicialbooking.controller.utils.WiremockFixtures.ACTOR_ID1;

@TestPropertySource(properties = {"testing.support.enabled=true"})
public class DeleteBookingControllerIntegrationTest extends BaseAuthorisedTestIntegration {
    private static final String URL = "/am/testing-support/bookings/";

    @Test
    public void deleteBookingByUserIdApiTest() throws Exception {
        getRequestSpecification()
                .when().delete(String.format("%s%s", URL, ACTOR_ID1))
                .then().assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
