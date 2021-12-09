package uk.gov.hmcts.reform.judicialbooking.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    @GetMapping(value = "/swagger")
    public String index() {
        return "redirect:swagger-ui.html";
    }


    @GetMapping(value = "/welcome")
    public String welcome() {
        return "Welcome to Judicial Booking service";
    }
}
