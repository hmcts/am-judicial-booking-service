package uk.gov.hmcts.reform.judicialbooking;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.NoArgsConstructor;
import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTags;
import org.apache.commons.lang3.Validate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

@RunWith(SpringIntegrationSerenityRunner.class)
@NoArgsConstructor
@WithTags({@WithTag("testType:Smoke")})
public class SmokeTest extends BaseTest {

    @Value("${judicialBookingUrl}")
    private String baseUrl;

    @Test
    public void should_receive_response_for_welcomeAPI() {

        String targetInstance = getRequiredVariable("TEST_URL") + "/welcome";
        RestAssured.useRelaxedHTTPSValidation();

        Response response = SerenityRest
                .given()
                .relaxedHTTPSValidation()
                .when()
                .get(targetInstance)
                .andReturn();
        response.then().assertThat().statusCode(HttpStatus.OK.value());
    }

    public static String getRequiredVariable(String name) {
        return Validate.notNull(System.getenv(name), "Environment variable `%s` is required", name);
    }
}
