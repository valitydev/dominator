package com.empayre.dominator.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TermSetConverterUtils {

    public static final String EMPTY = "";

    public static String replaceNull(String source) {
        return source == null ? EMPTY : source;
    }
}
