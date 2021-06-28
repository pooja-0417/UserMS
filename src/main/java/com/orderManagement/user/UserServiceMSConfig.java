
package com.orderManagement.user;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class UserServiceMSConfig {
	
	@Bean @LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
}
