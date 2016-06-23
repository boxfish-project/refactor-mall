package com.boxfishedu.component.boxfish.util.http;


import java.util.Map;


public class HttpRequestParameters {

    static public String build(String url, Map<String, String> parameters) {
        StringBuilder urlWithParameters = new StringBuilder(url);

        if (parameters != null) {
            urlWithParameters.append("?");
            parameters.forEach((key, value) -> urlWithParameters.append(key).append("=").append(value).append("&"));
            urlWithParameters.deleteCharAt(urlWithParameters.length() - 1);
        }

        return urlWithParameters.toString();
    }
}
