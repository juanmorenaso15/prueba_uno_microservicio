package com.prueba_ms.service_tasks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

import com.prueba_ms.service_tasks.filter.UserContextFilter;

@SpringBootApplication
@EnableFeignClients
public class ServiceTasksApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceTasksApplication.class, args);
    }
    
    @Bean
    public FilterRegistrationBean<UserContextFilter> userContextFilterRegistration() {
        FilterRegistrationBean<UserContextFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new UserContextFilter());
        registrationBean.addUrlPatterns("/api/tasks/*", "/api/tasks"); // Añadir ambos patrones
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE); // Prioridad máxima
        registrationBean.setName("userContextFilter");
        return registrationBean;
    }
}