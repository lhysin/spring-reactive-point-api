package io.lhysin.point.router;

import static org.springframework.http.MediaType.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import io.lhysin.point.handler.PointHandler;

@Configuration
public class PointRouter {

    @Bean
    public RouterFunction<ServerResponse> point(PointHandler pointHandler) {

        return route(
            POST("/points"), pointHandler::createPoint)

            .andRoute(
                PUT("/points/use"), pointHandler::usePoint)

            .andRoute(
                PATCH("/points/cancel"), pointHandler::cancelPoint)

            .andRoute(
                GET("/points/{userId}")
                    .and(accept(APPLICATION_JSON)), pointHandler::findByUserIdAndNotExpired)

            .andRoute(
                GET("/points/{userId}/summary")
                    .and(accept(APPLICATION_JSON)), pointHandler::findAvailablePointAmountByUserId);

    }

}
