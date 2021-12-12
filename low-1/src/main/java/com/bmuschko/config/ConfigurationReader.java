package com.bmuschko.config;

import java.util.Map;

public interface ConfigurationReader {
    Map<String, String> read();
}