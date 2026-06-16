package uk.gov.hmcts.reform.judicialbooking;

import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.IgnoreNoPactsToVerify;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.junitsupport.loader.VersionSelector;
import au.com.dius.pact.provider.spring.junit5.MockMvcTestTarget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.judicialbooking.controller.endpoints.CreateBookingController;
import uk.gov.hmcts.reform.judicialbooking.domain.service.BookingOrchestrator;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.PersistenceService;
import uk.gov.hmcts.reform.judicialbooking.helper.TestDataBuilder;
import uk.gov.hmcts.reform.judicialbooking.util.CorrelationInterceptorUtil;
import uk.gov.hmcts.reform.judicialbooking.util.SecurityUtils;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@Provider("am_judicialBooking_create")
@PactBroker(scheme = "${PACT_BROKER_SCHEME:http}",
        host = "${PACT_BROKER_URL:localhost}", port = "${PACT_BROKER_PORT:9292}",
        consumerVersionSelectors = {@VersionSelector(tag = "master")})
@TestPropertySource(properties = {"spring.cache.type=none"})
@Import(ProviderTestConfiguration.class)
@IgnoreNoPactsToVerify
public class CreateBookingProviderTest {

    @Autowired
    private PersistenceService persistenceService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private CorrelationInterceptorUtil correlationInterceptorUtil;

    @Autowired
    private BookingOrchestrator bookingOrchestrator;

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        if (context != null) {
            context.verifyInteraction();
        }
    }

    @BeforeEach
    void beforeCreate(PactVerificationContext context) {
        var testTarget = new MockMvcTestTarget();
        testTarget.setControllers(new CreateBookingController(
                bookingOrchestrator
        ));
        if (context != null) {
            context.setTarget(testTarget);
        }
    }

    @State({"A create request is received with valid begin and end dates"})
    public void createSingleBooking() {
        initCreateMocks();
    }

    private void initCreateMocks() {

        Mockito.when(persistenceService.persistBooking(any()))
                .thenReturn(TestDataBuilder.buildPreparedBooking());

        Mockito.when(securityUtils.getUserId()).thenReturn("3168da13-00b3-41e3-81fa-cbc71ac28a0f");
        Mockito.when(correlationInterceptorUtil.preHandle(any()))
                .thenReturn("14a21569-eb80-4681-b62c-6ae2ed069e2d");

    }

}
