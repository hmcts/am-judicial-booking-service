package uk.gov.hmcts.reform.judicialbooking;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
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

    @Bean
    @Primary
    public PrepareDataService getPrepareDataService() {
        return new PrepareDataService();
    }

    @Bean
    @Primary
    public SecurityUtils securityUtils() {
        return Mockito.mock(SecurityUtils.class);
    }

    @Bean
    @Primary
    public PersistenceService persistenceService() {
        return Mockito.mock(PersistenceService.class);
    }

    @Bean
    @Primary
    public CorrelationInterceptorUtil correlationInterceptorUtil() {
        return Mockito.mock(CorrelationInterceptorUtil.class);
    }

    @Bean
    @Primary
    public ParseRequestService getParseRequestService() {
        return new ParseRequestService(securityUtils(),"am_org_role_mapping_service");
    }

    @Bean
    @Primary
    public CacheManager cacheManager() {
        return Mockito.mock(CacheManager.class);
    }

    @Bean
    @Primary
    public BookingOrchestrator bookingOrchestrator() {
        return new BookingOrchestrator(
                getParseRequestService(),
                persistenceService(),
                getPrepareDataService()
        );
    }
}
