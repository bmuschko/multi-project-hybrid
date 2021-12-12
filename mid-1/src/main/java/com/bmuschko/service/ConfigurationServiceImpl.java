package com.bmuschko.service;

import com.bmuschko.config.ConfigurationReader;
import com.bmuschko.config.PropertiesConfigurationReader;

import java.util.Map;

public class ConfigurationServiceImpl implements ConfigurationService {
    private final ConfigurationReader configurationReader = new PropertiesConfigurationReader();

    @Override
    public Map<String, String> read() {
        return configurationReader.read();
    }
}