package uk.gov.hmcts.reform.orgrolemapping;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.core.model.annotations.PactFolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import io.restassured.http.ContentType;
import jakarta.validation.constraints.NotNull;
import net.serenitybdd.rest.SerenityRest;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import uk.gov.hmcts.reform.judicialbooking.domain.model.JudicialRefreshRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.UserRequest;

import java.util.List;
import java.util.Map;

import static io.pactfoundation.consumer.dsl.LambdaDsl.newJsonBody;

@ExtendWith(PactConsumerTestExt.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@PactTestFor(providerName = "am_orgRoleMapping_refresh")
@PactFolder("pacts")
@ContextConfiguration(classes = {OrgRoleMappingApplication.class})
@TestPropertySource(properties = {"feign.client.config.jbsClient.url=http://localhost:4097"})
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class OrgRoleMappingRefreshConsumerTest extends BaseTestContract {

    private static final String CONTENT_TYPE = "application/vnd.uk.gov.hmcts.am-org-role-mapping-service"
            + ".map-judicial-assignments+json;charset=UTF-8;version=1.0";
    private static final String ORM_REFRESH_URL = "/am/role-mapping/judicial/refresh";
    private static final String USER_ID = "5629957f-4dcd-40b8-a0b2-e64ff5898b28";
    private static final String USER_ID2 = "5629957f-4dcd-40b8-a0b2-e64ff5898b29";

    @BeforeEach
    public void setUpEachTest() throws InterruptedException {
        Thread.sleep(2000);
    }

    private HttpHeaders getHttpHeaders() {
        var headers = new HttpHeaders();
        headers.add("ServiceAuthorization", "Bearer " + "1234");
        headers.add("Authorization", "Bearer " + "2345");
        return headers;
    }

    @NotNull
    private Map<String, String> getResponseHeaders() {
        Map<String, String> responseHeaders = Maps.newHashMap();
        responseHeaders.put("Content-Type", CONTENT_TYPE);
        return responseHeaders;
    }

    private JudicialRefreshRequest createJudicialRefreshRequest() {
        return JudicialRefreshRequest.builder()
                .refreshRequest(UserRequest.builder()
                        .userIds(List.of(USER_ID, USER_ID2)).build()).build();
    }

    private DslPart createJudicialRefreshResponse() {
        return newJsonBody(o -> o
                .stringValue("Message", "Role assignments have been refreshed successfully"))
                .build();
    }

    @Pact(provider = "am_orgRoleMapping_refresh", consumer = "accessMgmt_judicialBooking")
    public RequestResponsePact executeRefreshJudicial(PactDslWithProvider builder) throws JsonProcessingException {

        return builder
                .given("A refresh request is received with a valid userId passed")
                .uponReceiving("A refresh request is received with a valid userId passed")
                .path(ORM_REFRESH_URL)
                .body(new ObjectMapper().writeValueAsString(createJudicialRefreshRequest()))
                .method(HttpMethod.POST.toString())
                .willRespondWith()
                .status(HttpStatus.OK.value())
                .headers(getResponseHeaders())
                .body(createJudicialRefreshResponse())
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "executeRefreshJudicial")
    void executeRefresh(MockServer mockServer)
            throws JSONException {
        var actualResponseBody =
                SerenityRest
                        .given()
                        .headers(getHttpHeaders())
                        .body(createJudicialRefreshRequest())
                        .contentType(ContentType.JSON)
                        .post(mockServer.getUrl() + ORM_REFRESH_URL)
                        .then()
                        .log().all().extract().asString();

        var response = new JSONObject(actualResponseBody);
        Assertions.assertThat(response).isNotNull();
    }
}
