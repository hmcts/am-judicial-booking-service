package uk.gov.hmcts.reform.judicialbooking.feignclients.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertNotNull;


class DatastoreFeignClientConfigurationTest {

    @InjectMocks
    FeignClientConfiguration feignClientConfiguration = new FeignClientConfiguration();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void client() {
        assertNotNull(feignClientConfiguration.client());
    }

}