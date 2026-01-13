package uk.gov.hmcts.reform.judicialbooking.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nimbusds.jose.JOSEException;
import io.restassured.specification.RequestSpecification;
import net.serenitybdd.annotations.WithTag;
import net.serenitybdd.annotations.WithTags;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.rest.SerenityRest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.judicialbooking.controller.utils.MockUtils;
import uk.gov.hmcts.reform.judicialbooking.controller.utils.WiremockFixtures;

import static uk.gov.hmcts.reform.judicialbooking.controller.utils.WiremockFixtures.ACTOR_ID1;
import static uk.gov.hmcts.reform.judicialbooking.controller.utils.WiremockFixtures.SERVICE_NAME_EXUI;

@ExtendWith({SerenityJUnit5Extension.class, SpringExtension.class})
@WithTags({@WithTag("testType:Integration")})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class BaseAuthorisedTestIntegration extends BaseTestIntegration {

    protected static final String BASEURL = "http://localhost";

    private WiremockFixtures wiremockFixtures;

    @LocalServerPort
    private int serverPort;

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
