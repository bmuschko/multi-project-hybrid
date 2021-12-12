package com.bmuschko.app;

import com.bmuschko.service.ConfigurationService;
import com.bmuschko.service.ConfigurationServiceImpl;

import java.util.Map;

public class Application {
    public static void main(String[] args) {
        ConfigurationService configurationService = new ConfigurationServiceImpl();
        Map<String, String> configuration = configurationService.read();

        for (Map.Entry<String, String> entry : configuration.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}