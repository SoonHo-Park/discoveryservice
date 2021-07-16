package com.example.gatewayservice.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config>{

	public GlobalFilter() {
		super(Config.class);
	}
	
	@Override
	public GatewayFilter apply(Config config) {
		// Custom Pre Filter
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			ServerHttpResponse response = exchange.getResponse();
			
			System.out.println("Global Filter baseMessage: ");
		
			System.out.println("Global Filter Staert: request id ->"+ request.getId());
			
			// Custom Post Filter
			return chain.filter(exchange).then(Mono.fromRunnable(() -> {
				System.out.println("Custom Post filter: response id ->"+ response.getStatusCode());
				
				System.out.println("Global Filter End: response id ->"+ response.getStatusCode());
			}));
		};
	}
	
	@Data 
	public static class Config {
		// Put the Configuration properties
		private String baseMessage;
		private boolean preLogger;
		private boolean postLogger;
	}
}
