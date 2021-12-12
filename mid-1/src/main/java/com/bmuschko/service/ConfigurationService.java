package com.bmuschko.service;

import java.util.Map;

public interface ConfigurationService {
    Map<String, String> read();
}