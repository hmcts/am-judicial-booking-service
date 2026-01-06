package uk.gov.hmcts.reform.judicialbooking.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mifmif.common.regex.Generex;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.reform.judicialbooking.config.TestConfigProperties;

import static org.apache.commons.lang3.RandomStringUtils.secure;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class IdamOpenId {

    private static final Logger log = LoggerFactory.getLogger(IdamOpenId.class);

    private final TestConfigProperties testConfig;
    private final Gson gson = new Gson();
    public  static String adminToken;
    public static final String EMAIL = "EMAIL";
    public static final String CREDS = "CREDS";
    public static final String EMAIL_TEMPLATE = "CWR-func-test-user-%s@justice.gov.uk";

    private static String sidamPassword;

    public static List<String> emailsTobeDeleted = new ArrayList<>();

    public IdamOpenId(TestConfigProperties testConfig) {
        this.testConfig = testConfig;
    }

    public Map<String, String> createUser(String userRole) {
        return createUser(userRole, generateRandomEmail(), "cwr-test", "cwr-test");
    }

    public Map<String, String> createUser(String userRole, String userEmail, String firstName, String lastName) {
        //Generating a random user
        String userGroup = "";
        String password = generateSidamPassword();

        String id = UUID.randomUUID().toString();

        Role role = new Role();
        role.userRole = userRole;

        List<Role> roles = new ArrayList<>();
        roles.add(role);

        Group group = new Group();
        group.userGroup = userGroup;

        User user = new User();
        user.email = userEmail;
        user.forename = firstName;
        user.id = id;
        user.surname = lastName;
        user.password = password;
        user.roles = roles;
        user.group = group;

        return getUserCredentials(userEmail, password, user);
    }

    @NotNull
    private Map<String, String> getUserCredentials(String userEmail, String password,User user) {
        String serializedUser = gson.toJson(user);

        Response createdUserResponse = null;

        for (int i = 0; i < 5; i++) {
            log.info("SIDAM createUser retry attempt : " + i + 1);
            createdUserResponse = RestAssured
                    .given()
                    .relaxedHTTPSValidation()
                    .baseUri(testConfig.idamApiUrl)
                    .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .body(serializedUser)
                    .post("/testing-support/accounts")
                    .andReturn();
            if (createdUserResponse.getStatusCode() == 504) {
                log.info("SIDAM createUser retry response for attempt " + i + 1 + " 504");
            } else {
                break;
            }
        }


        assertThat(createdUserResponse.getStatusCode()).isEqualTo(201);
        setEmailsTobeDeleted(userEmail);

        Map<String, String> userCreds = new HashMap<>();
        userCreds.put(EMAIL, userEmail);
        userCreds.put(CREDS, password);
        return userCreds;
    }

    public static String generateSidamPassword() {
        if (isBlank(sidamPassword)) {
            sidamPassword = new Generex("([A-Z])([a-z]{4})([0-9]{4})").random();
        }
        return sidamPassword;
    }

    public String getcwdAdminOpenIdToken(String role) {
        adminToken = getToken(role);
        return adminToken;
    }

    public String getToken(String role) {
        Map<String, String> userCreds = createUser(role);
        return getOpenIdToken(userCreds.get(EMAIL), userCreds.get(CREDS));
    }

    public String getOpenIdToken(String userEmail, String password) {

        Map<String, String> tokenParams = new HashMap<>();
        tokenParams.put("grant_type", "password");
        tokenParams.put("username", userEmail);
        tokenParams.put("password", password);
        tokenParams.put("client_id", testConfig.clientId);
        tokenParams.put("client_secret", testConfig.clientSecret);
        tokenParams.put("redirect_uri", testConfig.oauthRedirectUrl);
        tokenParams.put("scope", testConfig.scope);

        Response openIdTokenResponse = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .baseUri(testConfig.idamApiUrl)
                .header(CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE)
                .params(tokenParams)
                .post("/o/token")
                .andReturn();

        log.info("getOpenIdToken response: " + openIdTokenResponse.getStatusCode());

        assertThat(openIdTokenResponse.getStatusCode()).isEqualTo(200);

        BearerTokenResponse accessTokenResponse = gson.fromJson(openIdTokenResponse.getBody()
                .asString(), BearerTokenResponse.class);
        return accessTokenResponse.accessToken;

    }

    public static String generateRandomEmail() {
        return String.format(EMAIL_TEMPLATE, secure().nextAlphanumeric(10)).toLowerCase();
    }

    public static void setEmailsTobeDeleted(String emailTobeDeleted) {
        emailsTobeDeleted.add(emailTobeDeleted);
    }

    public class User {
        public String email;
        public String forename;
        public String id;
        public String surname;
        public String password;
        public List<Role> roles;
        public Group group;
    }

    @Getter
    @AllArgsConstructor
    class BearerTokenResponse {
        @SerializedName("access_token")
        private String accessToken;
    }

    public class Role {
        public String userRole;

    }

    public class Group {
        public String userGroup;
    }
}
