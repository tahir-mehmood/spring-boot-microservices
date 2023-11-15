package com.tm.learning.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoutesConfiguration {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("restful-api-route", r -> r.path("/rest/**").filters(f -> f.stripPrefix(1)).uri("lb://restful-api-service"))
                /*.route("tracker-route", r -> r.path("/tracker/**").uri("lb://tracker-service"))*/
                .build();
    }
}
