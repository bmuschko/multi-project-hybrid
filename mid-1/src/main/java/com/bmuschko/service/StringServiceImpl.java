package com.bmuschko.service;

import com.bmuschko.utils.StringModifier;
import com.bmuschko.utils.StringModifierImpl;

public class StringServiceImpl implements StringService {
    private final StringModifier stringModifier = new StringModifierImpl();

    @Override
    public String capitalize(String s) {
        return stringModifier.capitalize(s);
    }
}