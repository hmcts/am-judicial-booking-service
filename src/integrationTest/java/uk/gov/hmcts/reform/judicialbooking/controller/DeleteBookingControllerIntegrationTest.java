package uk.gov.hmcts.reform.judicialbooking.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.hmcts.reform.judicialbooking.controller.endpoints.DeleteBookingController;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class DeleteBookingControllerIntegrationTest extends BaseTestIntegration {

    private transient MockMvc mockMvc;

    private static final MediaType JSON_CONTENT_TYPE = new MediaType(
        MediaType.APPLICATION_JSON.getType(),
        MediaType.APPLICATION_JSON.getSubtype(),
        StandardCharsets.UTF_8
    );

    @Autowired
    private transient DeleteBookingController deleteBookingController;

    @BeforeEach
    public void setUp() {
        this.mockMvc = standaloneSetup(this.deleteBookingController).build();
    }

    @Test
    public void deleteBookingByUserIdApiTest() throws Exception {
        final var url = "/am/bookings/";
        final var userId = "631d322c-eea7-4d53-bd92-e6ec51bcb390";

        mockMvc.perform(delete(url + userId)
                .contentType(JSON_CONTENT_TYPE)
                .headers(getHttpHeaders()))
                .andExpect(status().isNoContent())
                .andReturn();
    }
}
