package uk.gov.hmcts.reform.judicialbooking.controller;

import static org.springframework.http.ResponseEntity.ok;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class S2SAuthentication {
    @GetMapping(value = "/testS2SAuthorization")
    public ResponseEntity<String> testS2SAuthorization() {
        return ok("S2S Authentication is successful !!");
    }
}


