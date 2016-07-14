package com.boxfishedu.component.boxfish.util.http;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class HttpRequestHeader {

    // Empty
    static public Header[] build0(Map<String, String> customHeaders) {
        List<Header> requestHeaders = new ArrayList<>();

        // Set custom headers
        if (customHeaders != null) {
            customHeaders.forEach((key, value) -> requestHeaders.add(new BasicHeader(key, value)));
        }

        return requestHeaders.toArray(new Header[requestHeaders.size()]);
    }

    // Text
    static public Header[] build(Map<String, String> customHeaders) {
        List<Header> requestHeaders = new ArrayList<>();
        requestHeaders.add(new BasicHeader("Accept-Charset", "UTF-8"));
        requestHeaders.add(new BasicHeader("Content-Type", "application/json"));

        // Set custom headers
        if (customHeaders != null) {
            customHeaders.forEach((key, value) -> requestHeaders.add(new BasicHeader(key, value)));
        }

        return requestHeaders.toArray(new Header[requestHeaders.size()]);
    }

    // Password
    static public Header[] build(Map<String, String> customHeaders, String username, String password) {
        List<Header> requestHeaders = new ArrayList<>();
        requestHeaders.add(new BasicHeader("Accept-Charset", "UTF-8"));
        requestHeaders.add(new BasicHeader("Content-Type", "application/json"));

        String input = username + ":" + password;
        String encoding = Base64.encodeBase64String(input.getBytes());
        requestHeaders.add(new BasicHeader("Authorization", "Basic " + encoding));

        // Set custom headers
        if (customHeaders != null) {
            customHeaders.forEach((key, value) -> requestHeaders.add(new BasicHeader(key, value)));
        }

        return requestHeaders.toArray(new Header[requestHeaders.size()]);
    }
}
