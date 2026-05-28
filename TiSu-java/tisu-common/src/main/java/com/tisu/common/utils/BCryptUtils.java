package com.tisu.common.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptUtils {
    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    public static String encode(String raw) {
        return ENCODER.encode(raw);
    }

    public static boolean matches(String raw, String encoded) {
        return ENCODER.matches(raw, encoded);
    }
}
