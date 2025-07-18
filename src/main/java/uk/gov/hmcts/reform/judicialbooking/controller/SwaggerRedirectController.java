package uk.gov.hmcts.reform.judicialbooking.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import static org.springdoc.core.utils.Constants.SWAGGER_UI_URL;

@RestController
@Hidden
public class SwaggerRedirectController {

    @GetMapping(value = "/swagger")
    public RedirectView swaggerRedirect() {
        return new RedirectView(SWAGGER_UI_URL, true, false);
    }
}
