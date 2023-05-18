package uk.gov.hmcts.reform.judicialbooking.config;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import static uk.gov.hmcts.reform.judicialbooking.apihelper.Constants.AUTHORIZATION;
import static uk.gov.hmcts.reform.judicialbooking.apihelper.Constants.SERVICE_AUTHORIZATION2;

@Configuration
public class SwaggerConfiguration {

    private static final String DESCRIPTION = "API will help to create bookings for fee paid judges and would provided"
            + " APIs to Fetch and create booking.";

    @Bean
    public GroupedOpenApi publicApi(OperationCustomizer customGlobalHeaders) {
        return GroupedOpenApi.builder()
                .group("am-judicial-booking-service")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(
                                AUTHORIZATION,
                                new io.swagger.v3.oas.models.security.SecurityScheme()
                                        .name(AUTHORIZATION)
                                        .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Valid IDAM user token, (Bearer keyword is "
                                                + "added automatically)")
                        )
                        .addSecuritySchemes(SERVICE_AUTHORIZATION2,
                                new io.swagger.v3.oas.models.security.SecurityScheme()
                                        .in(io.swagger.v3.oas.models.security.SecurityScheme.In.HEADER)
                                        .name(SERVICE_AUTHORIZATION2)
                                        .type(SecurityScheme.Type.APIKEY)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Valid Service-to-Service JWT token for a "
                                                + "whitelisted micro-service")
                        )
                )
                .info(new Info().title("AM Judicial Booking Service")
                        .description(DESCRIPTION))
                .externalDocs(new ExternalDocumentation()
                        .description("README")
                        .url("https://github.com/hmcts/am-judicial-booking-service#readme"))
                .addSecurityItem(new SecurityRequirement().addList(AUTHORIZATION))
                .addSecurityItem(new SecurityRequirement().addList(SERVICE_AUTHORIZATION2));
    }

    @Bean
    public OperationCustomizer customGlobalHeaders() {
        return (Operation customOperation, HandlerMethod handlerMethod) -> {
            Parameter serviceAuthorizationHeader = new Parameter()
                    .in(ParameterIn.HEADER.toString())
                    .schema(new StringSchema())
                    .name(SERVICE_AUTHORIZATION2)
                    .description("Valid Service-to-Service JWT token for a whitelisted micro-service")
                    .required(true);
            Parameter authorizationHeader = new Parameter()
                    .in(ParameterIn.HEADER.toString())
                    .schema(new StringSchema())
                    .name(AUTHORIZATION)
                    .description("Keyword `Bearer` followed by a valid IDAM user token")
                    .required(true);
            customOperation.addParametersItem(authorizationHeader);
            customOperation.addParametersItem(serviceAuthorizationHeader);
            return customOperation;
        };
    }
}
