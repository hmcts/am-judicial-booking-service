package uk.gov.hmcts.reform.judicialbooking.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

@Getter
@Setter
@Configuration
@ActiveProfiles("serenity")
public class TestConfigProperties {

    @Value("${oauth2-client-secret}")
    public String clientSecret;

    @Value("${test.user.password}")
    public String testUserPassword;

    @Value("${idam.api.url}")
    public String idamApiUrl;

    @Value("${idam.client.redirect_uri}")
    public String oauthRedirectUrl;

    @Value("${idam.client.id:xuiwebapp}")
    public String clientId;

    @Value("${scope-name}")
    public String scope;

}
