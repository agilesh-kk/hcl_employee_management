package config;

import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Component
public class EmployeeBeanLifecycle {

    @PostConstruct
    public void initialize() {
        System.out.println("Employee Bean Initialized");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Employee Bean Destroyed");
    }
}