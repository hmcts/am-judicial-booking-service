package uk.gov.hmcts.reform.judicialbooking;

import feign.Feign;
import feign.jackson.JacksonEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import uk.gov.hmcts.reform.authorisation.ServiceAuthorisationApi;
import uk.gov.hmcts.reform.authorisation.generators.ServiceAuthTokenGenerator;

@SpringBootApplication
@EnableCircuitBreaker
@EnableFeignClients
@ConfigurationProperties
public class JudicialBookingApplication implements CommandLineRunner {

    @Autowired
    private Environment env;

    private static final Logger logger = LoggerFactory.getLogger(JudicialBookingApplication.class);

    public static void main(final String[] args) {
        SpringApplication.run(JudicialBookingApplication.class);
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Start printing env variables");
        logger.info("{}", env);
        logger.info("Host is : {}", env.getProperty("JUDICIAL_BOOKING_SERVICE_POSTGRES_HOST"));
        logger.info(" user is: {}", env.getProperty("JUDICIAL_BOOKING_SERVICE_POSTGRES_USER"));
        logger.info(" passwor : {}", env.getProperty("JUDICIAL_BOOKING_SERVICE_POSTGRES_PASS"));
        logger.info(" port iS : {}", env.getProperty("JUDICIAL_BOOKING_SERVICE_POSTGRES_PORT"));
        logger.info("End printing env variables :");
        logger.error("Start printing env variables");
        logger.error("{}", env);
        logger.error("Host is : {}", env.getProperty("JUDICIAL_BOOKING_SERVICE_POSTGRES_HOST"));
        logger.error(" user is: {}", env.getProperty("JUDICIAL_BOOKING_SERVICE_POSTGRES_USER"));
        logger.error(" passwor : {}", env.getProperty("JUDICIAL_BOOKING_SERVICE_POSTGRES_PASS"));
        logger.error(" port iS : {}", env.getProperty("JUDICIAL_BOOKING_SERVICE_POSTGRES_PORT"));

        logger.error("End printing env variables");
        System.out.println("Start printing env variables");

        System.out.println("Host is :" + env.getProperty("JUDICIAL_BOOKING_SERVICE_POSTGRES_HOST"));
        System.out.println(" user is:" + env.getProperty("JUDICIAL_BOOKING_SERVICE_POSTGRES_USER"));
        System.out.println(" passwor :" + env.getProperty("JUDICIAL_BOOKING_SERVICE_POSTGRES_PASS"));
        System.out.println(" port iS :" + env.getProperty("JUDICIAL_BOOKING_SERVICE_POSTGRES_PORT"));
        System.out.println(" S2S iS :" + env.getProperty("AM_JUDICIAL_BOOKING_SERVICE_SECRET"));

        System.out.println("End printing env variables");
    }

    @Bean
    public ServiceAuthorisationApi generateServiceAuthorisationApi(@Value("${idam.s2s-auth.url}") final String s2sUrl) {
        return Feign.builder()
                    .encoder(new JacksonEncoder())
                    .contract(new SpringMvcContract())
                    .target(ServiceAuthorisationApi.class, s2sUrl);
    }

    @Bean
    public ServiceAuthTokenGenerator authTokenGenerator(
        @Value("${idam.s2s-auth.totp_secret}") final String secret,
        @Value("${idam.s2s-auth.microservice}") final String microService,
        final ServiceAuthorisationApi serviceAuthorisationApi) {
        return new ServiceAuthTokenGenerator(secret, microService, serviceAuthorisationApi);
    }
}
