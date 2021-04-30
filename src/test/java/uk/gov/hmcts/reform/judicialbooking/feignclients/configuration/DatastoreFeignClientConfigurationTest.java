package uk.gov.hmcts.reform.judicialbooking.feignclients.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertNotNull;


class DatastoreFeignClientConfigurationTest {

    @InjectMocks
    DatastoreFeignClientConfiguration datastoreFeignClientConfiguration = new DatastoreFeignClientConfiguration();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void client() {
        assertNotNull(datastoreFeignClientConfiguration.client());
    }

    @Test
    void errorDecoder() {
        assertNotNull(datastoreFeignClientConfiguration.errorDecoder());
    }

}