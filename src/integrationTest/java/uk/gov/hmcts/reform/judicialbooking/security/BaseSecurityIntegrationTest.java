package uk.gov.hmcts.reform.judicialbooking.security;

import io.restassured.specification.RequestSpecification;
import uk.gov.hmcts.reform.judicialbooking.controller.BaseAuthorisedTestIntegration;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingRequest;

import java.time.LocalDate;

import static uk.gov.hmcts.reform.judicialbooking.controller.utils.TestAuthenticationUtils.getJwtHeaders;
import static uk.gov.hmcts.reform.judicialbooking.controller.utils.WireMockStubs.SERVICE_NAME_EXUI;

public class BaseSecurityIntegrationTest extends BaseAuthorisedTestIntegration {

    protected static final String VALID_ISSUER_1 = "http://localhost:5062/o";
    protected static final String VALID_ISSUER_2 = "https://secondary-idam.platform.hmcts.net";
    protected static final String ROGUE_ISSUER = "https://rogue-issuer.com";

    protected static final BookingRequest BOOKING_REQUEST =
            new BookingRequest(
                    null,
                    REGION,
                    LOCATION,
                    LocalDate.now(),
                    LocalDate.now().plusDays(1));

    protected RequestSpecification jwtRequest(
            String issuer,
            boolean expired)
            throws Exception {

        return getRequestSpecification(
                SERVICE_NAME_EXUI,
                ACTOR_ID1,
                getJwtHeaders(issuer, expired));

    }

    protected RequestSpecification unexpiredJwt(
            String issuer)
            throws Exception {

        return jwtRequest(issuer, false);
    }

    protected RequestSpecification expiredJwt(
            String issuer)
            throws Exception {

        return jwtRequest(issuer, true);
    }
}
