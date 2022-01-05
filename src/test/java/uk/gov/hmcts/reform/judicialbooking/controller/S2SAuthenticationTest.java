package uk.gov.hmcts.reform.judicialbooking.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.junit.jupiter.api.Assertions.assertEquals;

class S2SAuthenticationTest {

    @InjectMocks
    private S2SAuthentication sut = new S2SAuthentication();


    @Test
    void testS2SAuthorization() {
        assertEquals("S2S Authentication is successful !!", sut.testS2SAuthorization().getBody());
    }
}