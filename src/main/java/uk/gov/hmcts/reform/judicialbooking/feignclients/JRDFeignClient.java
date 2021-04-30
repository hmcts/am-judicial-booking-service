package uk.gov.hmcts.reform.judicialbooking.feignclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import uk.gov.hmcts.reform.judicialbooking.domain.model.JudicialUserProfile;
import uk.gov.hmcts.reform.judicialbooking.feignclients.configuration.DatastoreFeignClientConfiguration;
import uk.gov.hmcts.reform.judicialbooking.feignclients.configuration.JRDFeignClientFallback;

import java.util.List;

//TODO verify these values
@FeignClient(value = "judicialrefdataclient", url = "${feign.client.config.jrdclient.url}",
        configuration = DatastoreFeignClientConfiguration.class,
        fallback = JRDFeignClientFallback.class)
public interface JRDFeignClient {

    @PostMapping(value = "/refdata/judicial/users/fetch")
    List<JudicialUserProfile> getJudicialUserProfiles(List<String> userIds);

}
