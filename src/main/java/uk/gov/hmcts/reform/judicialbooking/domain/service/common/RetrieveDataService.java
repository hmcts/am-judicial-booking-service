package uk.gov.hmcts.reform.judicialbooking.domain.service.common;

import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.judicialbooking.domain.model.JudicialUserProfile;
import uk.gov.hmcts.reform.judicialbooking.feignclients.JRDFeignClient;

import java.util.List;

@Service
public class RetrieveDataService {

    private final JRDFeignClient jrdFeignClient;

    public RetrieveDataService(JRDFeignClient jrdFeignClient) {
        this.jrdFeignClient = jrdFeignClient;
    }

    public List<JudicialUserProfile> getJudicialUserProfile(List<String> userIds) {
        return jrdFeignClient.getJudicialUserProfiles(userIds);
    }


}
