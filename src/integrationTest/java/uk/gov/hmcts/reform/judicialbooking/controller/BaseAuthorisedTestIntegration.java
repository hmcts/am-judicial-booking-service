package uk.gov.hmcts.reform.judicialbooking.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.nimbusds.jose.JOSEException;
import io.restassured.specification.RequestSpecification;
import net.serenitybdd.rest.SerenityRest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.reform.idam.client.models.UserInfo;
import uk.gov.hmcts.reform.judicialbooking.controller.utils.MockUtils;
import uk.gov.hmcts.reform.judicialbooking.controller.utils.WiremockFixtures;

import java.util.List;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static uk.gov.hmcts.reform.judicialbooking.controller.utils.WiremockFixtures.ACTOR_ID1;
import static uk.gov.hmcts.reform.judicialbooking.controller.utils.WiremockFixtures.OBJECT_MAPPER;
import static uk.gov.hmcts.reform.judicialbooking.controller.utils.WiremockFixtures.SERVICE_NAME_EXUI;

public abstract class BaseAuthorisedTestIntegration extends BaseTestIntegration {

    private WiremockFixtures wiremockFixtures;

    @LocalServerPort
    private int serverPort;

    private UserInfo getUserInfo(String actorId) {
        return UserInfo.builder()
                .uid(actorId)
                .givenName("Super")
                .familyName("User")
                .roles(List.of("%s"))
                .build();
    }

    protected RequestSpecification getRequestSpecification()
            throws JOSEException, JsonProcessingException {
        return getRequestSpecification(SERVICE_NAME_EXUI, ACTOR_ID1);
    }

    protected RequestSpecification getRequestSpecification(String serviceName, String actorId)
            throws JOSEException, JsonProcessingException {
        resetWiremockServer(serviceName, actorId);
        return SerenityRest.given()
                .relaxedHTTPSValidation()
                .baseUri(BASEURL)
                .port(serverPort)
                .headers(MockUtils.getHttpHeaders(MockUtils.S2S_JBS));
    }

    protected void resetWiremockServer(String serviceName, String actorId) throws JsonProcessingException {
        wiremockFixtures = new WiremockFixtures();
        WIRE_MOCK_SERVER.resetAll();
        wiremockFixtures.stubIdamConfig();
        wiremockFixtures.stubAuthorisation(serviceName, actorId);
    }
}
