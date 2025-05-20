package com.capstone.backend.core.common.web.response;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MessageResolver {
    private static final String BUNDLE_NAME = "api-error-messages";
    private static final ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME);

    private MessageResolver() {
        // private constructor to prevent instantiation
    }

    public static String resolve(String code) {
        return resolve(code, null);
    }

    public static String resolve(String code, Object[] args) {
        try {
            String pattern = bundle.getString(code);
            if (args == null || args.length == 0) {
                return pattern;
            } else {
                return MessageFormat.format(pattern, args);
            }
        } catch (MissingResourceException e) {
            return null;
        }
    }
}