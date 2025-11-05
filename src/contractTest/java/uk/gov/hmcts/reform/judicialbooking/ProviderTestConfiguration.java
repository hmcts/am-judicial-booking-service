package uk.gov.hmcts.reform.judicialbooking;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import uk.gov.hmcts.reform.judicialbooking.domain.service.BookingOrchestrator;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.ParseRequestService;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.PersistenceService;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.PrepareDataService;
import uk.gov.hmcts.reform.judicialbooking.util.CorrelationInterceptorUtil;
import uk.gov.hmcts.reform.judicialbooking.util.SecurityUtils;

@TestConfiguration
public class ProviderTestConfiguration {

    @MockitoBean
    private SecurityUtils securityUtils;

    @MockitoBean
    private PersistenceService persistenceService;

    @Bean
    @Primary
    public PrepareDataService getPrepareDataService() {
        return new PrepareDataService();
    }

    @MockitoBean
    private CorrelationInterceptorUtil correlationInterceptorUtil;

    @Bean
    @Primary
    public ParseRequestService getParseRequestService() {
        return new ParseRequestService(securityUtils,"");
    }

    @MockitoBean
    private CacheManager cacheManager;

    @Bean
    @Primary
    public BookingOrchestrator bookingOrchestrator() {
        return new BookingOrchestrator(getParseRequestService(),
                persistenceService,
                getPrepareDataService()
        );
    }
}
