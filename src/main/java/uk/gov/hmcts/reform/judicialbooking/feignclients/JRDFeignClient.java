package uk.gov.hmcts.reform.judicialbooking.feignclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import uk.gov.hmcts.reform.judicialbooking.domain.model.JudicialUserProfile;
import uk.gov.hmcts.reform.judicialbooking.feignclients.configuration.FeignClientConfiguration;
import uk.gov.hmcts.reform.judicialbooking.feignclients.configuration.JRDFeignClientFallback;

import java.util.List;

@FeignClient(value = "judicialrefdataclient", url = "${feign.client.config.jrdClient.url}",
        configuration = FeignClientConfiguration.class,
        fallback = JRDFeignClientFallback.class)
public interface JRDFeignClient {

    @PostMapping(value = "/refdata/judicial/users/fetch")
    List<JudicialUserProfile> getJudicialUserProfiles(List<String> userIds);

}
