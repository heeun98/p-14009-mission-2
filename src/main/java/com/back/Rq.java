package com.back;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Rq {


    private final Map<String, String> paramsMap = new HashMap<>();

    public Rq(String cmd) {

        String[] cmdBits = cmd.split("//?", 2);
        String actionName = cmdBits[0];
        String queryString = cmdBits.length > 1? cmdBits[1].trim() : "";

        String[] queryStringBits = queryString.split("&");

       /* for (String queryStringBit : queryStringBits) {
            String[] queryParamBits = queryStringBit.split("=", 2);

            String key = queryParamBits[0].trim();
            String value = queryParamBits.length > 1 ? queryStringBits[1].trim() : "";

            if (value.isEmpty()) {
                continue;
            }

            paramsMap.put(key, value);
        }*/


        Arrays.stream(queryStringBits)
                .map(queryStringBit -> queryStringBit.split("=", 2))
                .filter(queryParamBits -> queryParamBits.length > 1 && !queryParamBits[1].trim().isEmpty())
                .forEach(queryParamBits -> {
                    String key = queryParamBits[0].trim();
                    String value = queryParamBits[1].trim();
                    paramsMap.put(key, value);
                });

    }

    public String getParam(String paramName, String defaultValue) {
        return paramsMap.getOrDefault(paramName,defaultValue);
    }

    public int getParamAsInt(String paramName, int defaultValue) {
        String value = getParam(paramName, "");

        if (value.isEmpty()) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }




}
