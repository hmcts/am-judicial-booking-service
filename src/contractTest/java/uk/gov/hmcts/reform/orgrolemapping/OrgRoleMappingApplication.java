package uk.gov.hmcts.reform.orgrolemapping;

import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import uk.gov.hmcts.reform.idam.client.IdamClient;
import uk.gov.hmcts.reform.idam.client.IdamApi;
import uk.gov.hmcts.reform.judicialbooking.oidc.IdamRepository;
import uk.gov.hmcts.reform.judicialbooking.oidc.OIdcAdminConfiguration;
import uk.gov.hmcts.reform.judicialbooking.util.SecurityUtils;

@SpringBootApplication
public class OrgRoleMappingApplication {

    @Bean
    IdamApi idamApi() {
        return Mockito.mock(IdamApi.class);
    }

    @Bean
    IdamClient idamClient() {
        return Mockito.mock(IdamClient.class);
    }

    @Bean
    SecurityUtils securityUtils() {
        return Mockito.mock(SecurityUtils.class);
    }

    @Bean
    IdamRepository idamRepository() {
        return Mockito.mock(IdamRepository.class);
    }

    @Bean
    OIdcAdminConfiguration oidcAdminConfiguration() {
        return Mockito.mock(OIdcAdminConfiguration.class);
    }


}
