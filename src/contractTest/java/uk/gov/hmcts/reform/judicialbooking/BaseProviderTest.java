package uk.gov.hmcts.reform.judicialbooking;

import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junitsupport.IgnoreNoPactsToVerify;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.junitsupport.loader.PactBrokerConsumerVersionSelectors;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import au.com.dius.pact.provider.junitsupport.loader.SelectorBuilder;
import au.com.dius.pact.provider.spring.junit5.MockMvcTestTarget;
import au.com.dius.pact.provider.spring.spring6.PactVerificationSpring6Provider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.hmcts.reform.judicialbooking.controller.endpoints.QueryBookingController;
import uk.gov.hmcts.reform.judicialbooking.domain.service.BookingOrchestrator;
import uk.gov.hmcts.reform.judicialbooking.domain.service.common.PersistenceService;
import uk.gov.hmcts.reform.judicialbooking.util.CorrelationInterceptorUtil;
import uk.gov.hmcts.reform.judicialbooking.util.SecurityUtils;

@ExtendWith(SpringExtension.class)
@PactBroker(url = "${PACT_BROKER_SCHEME:http}://${PACT_BROKER_URL:localhost}:${PACT_BROKER_PORT:9292}")
@TestPropertySource(properties = {"spring.cache.type=none"})
@Import(ProviderTestConfiguration.class)
@PactFolder("pacts")
@IgnoreNoPactsToVerify
public abstract class BaseProviderTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected PersistenceService persistenceService;

    @Autowired
    protected SecurityUtils securityUtils;

    @Autowired
    protected CorrelationInterceptorUtil correlationInterceptorUtil;

    @Autowired
    protected BookingOrchestrator bookingOrchestrator;

    @BeforeEach
    void beforeCreate(PactVerificationContext context) {
        MockMvcTestTarget testTarget = new MockMvcTestTarget();
        testTarget.setControllers(new QueryBookingController(
                bookingOrchestrator
        ));
        if (context != null) {
            context.setTarget(testTarget);
        }
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @PactBrokerConsumerVersionSelectors
    public static SelectorBuilder consumerVersionSelectors() {
        return new SelectorBuilder()
                .matchingBranch()
                .mainBranch()
                .deployedOrReleased();
    }

    @TestTemplate
    @ExtendWith(PactVerificationSpring6Provider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        if (context != null) {
            context.verifyInteraction();
        }
    }
}
