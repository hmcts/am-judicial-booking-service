package uk.gov.hmcts.reform.judicialbooking.feignclients.configuration;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.judicialbooking.oidc.IdamRepository;
import uk.gov.hmcts.reform.judicialbooking.oidc.OIdcAdminConfiguration;
import uk.gov.hmcts.reform.judicialbooking.util.Constants;
import uk.gov.hmcts.reform.judicialbooking.util.SecurityUtils;

@Service
public class OrmApiRequestInterceptor {

    @Autowired
    SecurityUtils securityUtils;
    @Autowired
    IdamRepository idamRepository;
    @Autowired
    OIdcAdminConfiguration oidcAdminConfiguration;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            if (!requestTemplate.url().contains("health")) {
                requestTemplate.header(Constants.SERVICE_AUTHORIZATION2, "Bearer "
                        + securityUtils.getServiceAuthorizationHeader());
                requestTemplate.header(HttpHeaders.AUTHORIZATION, "Bearer "
                        + idamRepository.getManageUserToken(oidcAdminConfiguration.getUserId()));
                requestTemplate.header(HttpHeaders.CONTENT_TYPE, "application/json");
            }
        };
    }
}