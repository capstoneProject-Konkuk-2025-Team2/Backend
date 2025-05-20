package com.capstone.backend.core.configuration.env;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppEnv {

    private final String applicationName;

    public AppEnv(@Value("${spring.application.name}") String applicationName) {
        this.applicationName = applicationName;
    }

    public String getId() {
        return applicationName;
    }
}
