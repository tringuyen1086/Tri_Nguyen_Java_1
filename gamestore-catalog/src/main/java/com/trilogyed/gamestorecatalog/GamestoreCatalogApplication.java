package com.trilogyed.gamestorecatalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.FeignClient;

@SpringBootApplication
@FeignClient
@EnableDiscoveryClient
@EnableEurekaClient
public class GamestoreCatalogApplication {

	public static void main(String[] args) {
		SpringApplication.run(GamestoreCatalogApplication.class, args);
	}

}
