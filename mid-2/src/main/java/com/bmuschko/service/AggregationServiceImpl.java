package com.bmuschko.service;

import com.bmuschko.service.ConfigurationService;
import com.bmuschko.service.ConfigurationServiceImpl;
import com.bmuschko.service.StringService;
import com.bmuschko.service.StringServiceImpl;

import java.util.Map;

public class AggregationServiceImpl implements AggregationService {
    private final ConfigurationService configurationService = new ConfigurationServiceImpl();
    private final StringService stringService = new StringServiceImpl();

    @Override
    public String capitalizeFoundProperty(String search) {
        Map<String, String> props = configurationService.read();

        if (props.containsKey(search)) {
            return stringService.capitalize(props.get(search));
        }

        return null;
    }
}