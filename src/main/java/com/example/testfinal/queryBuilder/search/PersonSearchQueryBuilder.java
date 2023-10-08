package com.example.testfinal.queryBuilder.search;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;

public class PersonSearchQueryBuilder {

    public static String buildSearchQuery(Map.Entry<String, Object> entry) {
        String key = entry.getKey();
        String snakeCaseKey = convertToSnakeCase(key);
        Object value = entry.getValue();
        boolean isDate = checkIfParamIsDate(value);

        if (entry.getValue() instanceof String && !isDate) {
            return handleString(snakeCaseKey, value);
        }

        if (entry.getValue() instanceof Integer || entry.getValue() instanceof Long) {
            return handleIntOrLong(snakeCaseKey, value);
        }

        if (isDate) {
            return handleDate(snakeCaseKey, value);
        }

        return "";
    }

    private static String handleString(String key, Object value) {
        return "LOWER(" + key + ") LIKE LOWER('%" + value + "%')";
    }

    private static String handleIntOrLong(String key, Object value) {
        if (key.startsWith("min")) {
            return key.substring(4) + " >= " + value;
        } else if (key.startsWith("max")) {
            return key.substring(4) + " <= " + value;
        } else {
            return key + " = " + value;
        }
    }

    private static String handleDate(String key, Object value) {
        if (key.startsWith("min")) {
            return key.substring(4) + " >= " + "DATE " + "'" + value + "'";
        }
        if (key.startsWith("max")) {
            return key.substring(4) + " <= " + "DATE " + "'" + value + "'";
        }
        return "";
    }

    private static boolean checkIfParamIsDate(Object value) {
        try {
            LocalDate.parse(value.toString());
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private static String convertToSnakeCase(String input) {
        String[] words = input.split("(?=[A-Z])");
        return String.join("_", words).toLowerCase();
    }
}
