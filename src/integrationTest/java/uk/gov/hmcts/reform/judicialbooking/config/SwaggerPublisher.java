package uk.gov.hmcts.reform.judicialbooking.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uk.gov.hmcts.reform.judicialbooking.controller.BaseTestIntegration;

import javax.inject.Inject;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springdoc.core.utils.Constants.DEFAULT_API_DOCS_URL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Built-in feature which saves service's swagger specs in temporary directory.
 * Each run of workflow .github/workflows/swagger.yml on master should automatically save and upload (if updated)
 * documentation.
 */
public class SwaggerPublisher extends BaseTestIntegration {

    private MockMvc mockMvc;

    @Inject
    private WebApplicationContext wac;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void generateDocs() throws Exception {
        byte[] specs = mockMvc.perform(get(DEFAULT_API_DOCS_URL))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsByteArray();

        try (OutputStream outputStream = Files.newOutputStream(Paths.get("/tmp/openapi-specs.json"))) {
            outputStream.write(specs);
        }
    }
}
