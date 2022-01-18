package uk.gov.hmcts.reform.judicialbooking;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import uk.gov.hmcts.reform.judicialbooking.domain.service.BookingOrchestrator;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.ParseRequestService;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.PersistenceService;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.PrepareDataService;
import uk.gov.hmcts.reform.judicialbooking.util.CorrelationInterceptorUtil;
import uk.gov.hmcts.reform.judicialbooking.util.SecurityUtils;

@TestConfiguration
public class ProviderTestConfiguration {

    @MockBean
    private PersistenceService persistenceService;

    @Bean
    @Primary
    public PrepareDataService getPrepareDataService() {
        return new PrepareDataService();
    }

    @MockBean
    private CorrelationInterceptorUtil correlationInterceptorUtil;

    @Bean
    @Primary
    public ParseRequestService getParseRequestService() {
        return new ParseRequestService();
    }

    @MockBean
    SecurityUtils securityUtils;

    @MockBean
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
