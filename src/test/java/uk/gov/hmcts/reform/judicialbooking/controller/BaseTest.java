package uk.gov.hmcts.reform.judicialbooking.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.TestPropertySourceUtils;
import uk.gov.hmcts.reform.judicialbooking.controller.utils.WiremockFixtures;

import java.nio.charset.StandardCharsets;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@ContextConfiguration(initializers = {BaseTest.WireMockServerInitializer.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties
public abstract class BaseTest {

    public static final WireMockServer WIRE_MOCK_SERVER = new WireMockServer(options().dynamicPort());

    @MockBean(name = "clientRegistrationRepository")
    private ClientRegistrationRepository getClientRegistrationRepository;

    @MockBean(name = "reactiveClientRegistrationRepository")
    private ReactiveClientRegistrationRepository getReactiveClientRegistrationRepository;

    static {
        if (!WIRE_MOCK_SERVER.isRunning()) {
            WIRE_MOCK_SERVER.start();
        }
        // Force re-initialisation of base types for each test suite
    }

    protected static final MediaType JSON_CONTENT_TYPE = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            StandardCharsets.UTF_8
    );

    public static class WireMockServerInitializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        private final WiremockFixtures wiremockFixtures = new WiremockFixtures();

        @Override
        public void initialize(@NonNull ConfigurableApplicationContext applicationContext) {

            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    applicationContext,
                    "wiremock.server.port=" + WIRE_MOCK_SERVER.port()
            );

            try {
                wiremockFixtures.stubIdamConfig();
                wiremockFixtures.stubAuthorisation();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            applicationContext.addApplicationListener((ApplicationListener<ContextClosedEvent>) event -> {
                if (WIRE_MOCK_SERVER.isRunning()) {
                    WIRE_MOCK_SERVER.shutdown();
                }
            });
        }
    }
}
