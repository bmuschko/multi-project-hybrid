package com.bmuschko.utils;

import org.apache.commons.lang3.StringUtils;

public class StringModifierImpl implements StringModifier {
    @Override
    public String capitalize(String s) {
        return StringUtils.capitalize(s);
    }
}