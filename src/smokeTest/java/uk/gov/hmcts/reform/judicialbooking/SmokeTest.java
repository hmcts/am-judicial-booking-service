package uk.gov.hmcts.reform.judicialbooking;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.NoArgsConstructor;
import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTags;
import org.apache.commons.lang3.Validate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;

@RunWith(SpringIntegrationSerenityRunner.class)
@NoArgsConstructor
@WithTags({@WithTag("testType:Smoke")})
public class SmokeTest extends BaseTest {
    public static final String AUTHORIZATION = "Authorization";
    public static final String SERVICE_AUTHORIZATION = "ServiceAuthorization";
    public static final String BEARER = "Bearer ";

    UserTokenProviderConfig config;
    String accessToken;
    String serviceAuth;

    @Before
    public void setUp() {
        config = new UserTokenProviderConfig();
        accessToken = searchUserByUserId(config);
        serviceAuth = authTokenGenerator(
                config.getSecret(),
                config.getMicroService(),
                generateServiceAuthorisationApi(config.getS2sUrl())
        ).generate();
    }

    @Test
    public void should_receive_response_for_welcomeAPI() {

        var targetInstance = getRequiredVariable("TEST_URL") + "/welcome";
        RestAssured.useRelaxedHTTPSValidation();

        Response response = SerenityRest
                .given()
                .relaxedHTTPSValidation()
                .when()
                .get(targetInstance)
                .andReturn();
        response.then().assertThat().statusCode(HttpStatus.OK.value());
    }

    @Test
    public void should_receive_response_for_query_bookings() {

        var targetInstance = config.getBaseUrl() + "/am/bookings/query";
        RestAssured.useRelaxedHTTPSValidation();

        String requestBody = "{\"queryRequest\" : {\n"
                + "    \"userIds\" : [\"b0444ddd-e640-4bbe-a6e9-d0b771171401\"]\n"
                + "  }}";

        Response response = SerenityRest
                .given()
                .relaxedHTTPSValidation()
                .header("Content-Type", "application/json")
                .header(SERVICE_AUTHORIZATION, BEARER + serviceAuth)
                .header(AUTHORIZATION, BEARER + accessToken)
                .body(requestBody)
                .when()
                .post(targetInstance)
                .andReturn();
        response.then().assertThat().statusCode(HttpStatus.OK.value());

    }

    public static String getRequiredVariable(String name) {
        return Validate.notNull(System.getenv(name), "Environment variable `%s` is required", name);
    }

}
