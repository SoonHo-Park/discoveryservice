package com.example.gatewayservice.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config>{

	public LoggingFilter() {
		super(Config.class);
	}
	
	@Override
	public GatewayFilter apply(Config config) {
		// Custom Pre Filter
//		return ((exchange, chain) -> {
//			ServerHttpRequest request = exchange.getRequest();
//			ServerHttpResponse response = exchange.getResponse();
//			
//			System.out.println("Global Filter baseMessage: ");
//		
//			System.out.println("Global Filter Staert: request id ->"+ request.getId());
//			
//			// Custom Post Filter
//			return chain.filter(exchange).then(Mono.fromRunnable(() -> {
//				System.out.println("Custom Post filter: response id ->"+ response.getStatusCode());
//				
//				System.out.println("Global Filter End: response id ->"+ response.getStatusCode());
//			}));
//		});
		
		GatewayFilter filter = new OrderedGatewayFilter((exchange, chain) -> {
			
			ServerHttpRequest request = exchange.getRequest();
			ServerHttpResponse response = exchange.getResponse();

			log.info("Logging Filter baseMessage: {}");
			
			System.out.println("Logging Filter Staert: request id ->"+ request.getId());
			
			return chain.filter(exchange).then(Mono.fromRunnable(() -> {
				System.out.println("Logging Post filter: response id ->"+ response.getStatusCode());
			}));
		}, Ordered.HIGHEST_PRECEDENCE); //필터의 우선순위를 가장 높은 순위로 변경 
		
		return filter;
	}
	
	@Data
	public static class Config {
		// Put the Configuration properties
		private String baseMessage;
		private boolean preLogger;
		private boolean postLogger;
	}
}
