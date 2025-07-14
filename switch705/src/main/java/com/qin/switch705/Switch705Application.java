package com.qin.switch705;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan("com.qin.switch705.domain")
@SpringBootApplication
public class Switch705Application {

    public static void main(String[] args) {
        SpringApplication.run(Switch705Application.class, args);
    }

}
