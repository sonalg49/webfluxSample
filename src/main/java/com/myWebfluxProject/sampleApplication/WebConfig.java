package com.myWebfluxProject.sampleApplication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;


import com.myWebfluxProject.sampleApplication.handlers.ShowHandler;



@Configuration
@EnableWebFlux
public class WebConfig implements WebFluxConfigurer{
	
	@Bean
	public RouterFunction<ServerResponse> routeShow(ShowHandler showHandler){
		return RouterFunctions
				.route(RequestPredicates.GET("/shows/{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), showHandler::getShowById)
				.andRoute(RequestPredicates.GET("/shows/title/{title}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), showHandler::getShowBytitle)
				.andRoute(RequestPredicates.GET("/shows").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), showHandler::getShowList)
				.andRoute(RequestPredicates.POST("/shows").and(RequestPredicates.accept(MediaType.APPLICATION_JSON))
						.and(RequestPredicates.contentType(MediaType.APPLICATION_JSON)), showHandler::addShow)
				.andRoute(RequestPredicates.POST("/shows/all").and(RequestPredicates.accept(MediaType.APPLICATION_JSON))
						.and(RequestPredicates.contentType(MediaType.APPLICATION_JSON)), showHandler::addAllShow)
				.andRoute(RequestPredicates.PUT("/shows").and(RequestPredicates.accept(MediaType.APPLICATION_JSON))
						.and(RequestPredicates.contentType(MediaType.APPLICATION_JSON)), showHandler::updateShow)
				.andRoute(RequestPredicates.DELETE("/shows/{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), showHandler::deleteShow);
	}
}
