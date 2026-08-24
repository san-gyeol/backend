package com.sangyeol.control.infrastructure.knps;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

@Configuration
public class KnpsClientConfig {

    private static final String KNPS_BASE_URL = "https://www.knps.or.kr";
    private static final String USER_AGENT = "sangyeol-backend/0.1";

    @Bean
    public RestClient knpsRestClient(RestClient.Builder builder) {
        return builder
                .baseUrl(KNPS_BASE_URL)
                .defaultHeader(HttpHeaders.USER_AGENT, USER_AGENT)
                .build();
    }
}
