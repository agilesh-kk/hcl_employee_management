
package com.example.Hcl_training;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
	    "com.example.Hcl_training",
	    "controller",
	    "service",
	    "repository",
	    "entity",
	    "dto",
	    "exception",
	    "config",
	    "security"
	})
	@EntityScan(basePackages = "entity")
	@EnableJpaRepositories(basePackages = "repository")

public class HclTrainingApplication {

    public static void main(String[] args) {

        SpringApplication.run(
            HclTrainingApplication.class, args
        );

    }
}
