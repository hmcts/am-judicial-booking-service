package uk.gov.hmcts.reform.orgrolemapping;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import uk.gov.hmcts.reform.idam.client.IdamClient;
import uk.gov.hmcts.reform.judicialbooking.oidc.IdamRepository;
import uk.gov.hmcts.reform.judicialbooking.oidc.OIdcAdminConfiguration;
import uk.gov.hmcts.reform.judicialbooking.util.SecurityUtils;

@SpringBootApplication
public class OrgRoleMappingApplication {

    @MockitoBean
    IdamClient idamClient;

    @MockitoBean
    SecurityUtils securityUtils;

    @MockitoBean
    IdamRepository idamRepository;

    @MockitoBean
    OIdcAdminConfiguration oidcAdminConfiguration;


}
