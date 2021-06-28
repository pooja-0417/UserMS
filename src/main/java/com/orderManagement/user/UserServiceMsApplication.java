package com.orderManagement.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class UserServiceMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceMsApplication.class, args);
	}

}
