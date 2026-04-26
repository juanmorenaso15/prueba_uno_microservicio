package com.prueba_ms.service_tasks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ServiceTasksApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceTasksApplication.class, args);
	}

}
