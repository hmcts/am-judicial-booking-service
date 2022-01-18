package uk.gov.hmcts.reform.orgrolemapping;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.mock.mockito.MockBean;
import uk.gov.hmcts.reform.idam.client.IdamClient;
import uk.gov.hmcts.reform.judicialbooking.oidc.IdamRepository;
import uk.gov.hmcts.reform.judicialbooking.oidc.OIdcAdminConfiguration;
import uk.gov.hmcts.reform.judicialbooking.util.SecurityUtils;

@SpringBootApplication
public class OrgRoleMappingApplication {

    @MockBean
    IdamClient idamClient;

    @MockBean
    SecurityUtils securityUtils;

    @MockBean
    IdamRepository idamRepository;

    @MockBean
    OIdcAdminConfiguration oidcAdminConfiguration;


}
