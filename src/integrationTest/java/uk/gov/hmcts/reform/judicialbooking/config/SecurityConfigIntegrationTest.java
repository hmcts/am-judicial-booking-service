package uk.gov.hmcts.reform.judicialbooking.config;

import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.reform.judicialbooking.controller.BaseTestIntegration;

import java.util.Collections;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class SecurityConfigIntegrationTest extends BaseTestIntegration {

    private static final String VALID_ISSUER_1 = "https://valid-issuer-1.com";
    private static final String VALID_ISSUER_2 = "https://valid-issuer-2.com";
    private static final String ROGUE_ISSUER = "https://rogue-issuer.com";
    private static final String LOCAL_ISSUER = "HTTP://LOCALHOST:5062/O";
    private static final String EMPTY_STRING = "";
    private static final String MISSING_ISS_CLAIM = null;

    @ParameterizedTest
    @MethodSource("getParams")
    void test(Boolean issuerValidation, List<String> allowedIssuers,
              String tokenIssuer, Boolean expired, HttpStatus expectedStatus) throws Exception {
        assertNotNull(allowedIssuers);
        // TODO
    }

    private static Stream<Arguments> getParams() {
        List<String> validIssuers = List.of(VALID_ISSUER_1, VALID_ISSUER_2);
        return Stream.of(
                // 1. Normal token accepted when flag is off.
                Arguments.of(false, validIssuers, VALID_ISSUER_1, false, HttpStatus.OK),
                // 2. Untrusted tokens allowed.
                Arguments.of(false, validIssuers, ROGUE_ISSUER, false, HttpStatus.OK),
                // 3. Missing iss claim allowed.
                Arguments.of(false, validIssuers, null, false, HttpStatus.OK),
                // 4. Timestamp validation STILL active when flag off.
                Arguments.of(false, validIssuers, VALID_ISSUER_1, true, HttpStatus.UNAUTHORIZED),
                // 5. Primary whitelisted issuer accepted.
                Arguments.of(true, validIssuers, VALID_ISSUER_1, false, HttpStatus.OK),
                // 6. Secondary whitelisted issuer accepted.
                Arguments.of(true, validIssuers,VALID_ISSUER_2, false, HttpStatus.OK),
                // 7. Untrusted/foreign issuers rejected.
                Arguments.of(true, validIssuers, ROGUE_ISSUER, false, HttpStatus.UNAUTHORIZED),
                // 8. Tokens with missing iss claim rejected.
                Arguments.of(true, validIssuers, null, false, HttpStatus.UNAUTHORIZED),
                // 9. Blank iss strings rejected.
                Arguments.of(true, validIssuers, EMPTY_STRING, false, HttpStatus.UNAUTHORIZED),
                // 10. Trailing slash mismatch rejected (exact match).
                Arguments.of(true, validIssuers, LOCAL_ISSUER + "/", false, HttpStatus.UNAUTHORIZED),
                // 11. Case sensitivity mismatch rejected.
                Arguments.of(true, validIssuers, LOCAL_ISSUER, false, HttpStatus.UNAUTHORIZED),
                // 12. Valid issuer, but token is expired.
                Arguments.of(true, validIssuers, VALID_ISSUER_1, true, HttpStatus.UNAUTHORIZED),
                // 13. Empty whitelist blocks all tokens.
                Arguments.of(true, Collections.emptyList(), VALID_ISSUER_1, false, HttpStatus.UNAUTHORIZED)
        );
    }
}