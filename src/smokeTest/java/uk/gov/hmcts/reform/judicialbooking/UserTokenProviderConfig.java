package uk.gov.hmcts.reform.judicialbooking;

import lombok.Getter;
import uk.gov.hmcts.befta.util.EnvironmentVariableUtils;
import uk.gov.hmcts.reform.idam.client.models.TokenRequest;


@Getter
public class UserTokenProviderConfig {

    private final String idamURL;
    private final String baseUrl;
    private final String secret;
    private final String microService;
    private final String s2sUrl;

    private final String clientId;
    private final String clientSecret;
    private final String username;
    private final String password;
    private final String scope;
    private static final String MICRO_SERVICE_NAME = "am_judicial_booking_service";
    private static final String USER_NAME = "TEST_AM_USER2_BEFTA@test.local";

    public UserTokenProviderConfig() {

        idamURL = EnvironmentVariableUtils.getRequiredVariable("IDAM_URL");
        baseUrl = EnvironmentVariableUtils.getRequiredVariable("TEST_URL");
        secret = EnvironmentVariableUtils.getRequiredVariable("AM_JUDICIAL_BOOKING_SERVICE_SECRET");
        microService = MICRO_SERVICE_NAME;
        s2sUrl = EnvironmentVariableUtils.getRequiredVariable("IDAM_S2S_URL");
        clientSecret = EnvironmentVariableUtils.getRequiredVariable("JUDICIAL_BOOKING_IDAM_CLIENT_SECRET");
        clientId = EnvironmentVariableUtils.getRequiredVariable("IDAM_CLIENT_ID");
        username = USER_NAME;
        password = EnvironmentVariableUtils.getRequiredVariable("TEST_AM_USER2_BEFTA_PWD");
        scope = EnvironmentVariableUtils.getRequiredVariable("OPENID_SCOPE_VARIABLES");
    }


    public TokenRequest prepareTokenRequest() {

        return new TokenRequest(
                clientId,
                clientSecret,
                "password",
                "",
                username,
                password,
                "openid roles profile authorities",
                "4",
                ""
        );
    }
}
