package com.boxfishedu.component.boxfish.util.http;


import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.util.EntityUtils;

import java.util.Map;
import java.util.concurrent.Future;


public class HttpAsyncClient {

    CloseableHttpAsyncClient httpAsyncClient;

    RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(10_000)
            .setConnectTimeout(10_000)
            .setConnectionRequestTimeout(10_000)
            .build();


    public HttpAsyncClient(CloseableHttpAsyncClient httpAsyncClient) {
        this.httpAsyncClient = httpAsyncClient;
    }

    public String get(String url, Map<String, String> parameters, Map<String, String> headers) throws Exception {
        HttpGet request = new HttpGet(HttpRequestParameters.build(url, parameters));
        request.setHeaders(HttpRequestHeader.build(headers));
        request.setConfig(requestConfig);

        Future<HttpResponse> future = httpAsyncClient.execute(request, null);
        if (future.get().getStatusLine().getStatusCode() == 200) {
            return EntityUtils.toString(future.get().getEntity(), "UTF-8");
        } else {
            throw new IllegalStateException("[boxfish-component] [get] 数据状态异常.");
        }
    }

    public String get(String url, Map<String, String> parameters, Map<String, String> headers, String username, String password) throws Exception {
        HttpGet request = new HttpGet(HttpRequestParameters.build(url, parameters));
        request.setHeaders(HttpRequestHeader.build(headers, username, password));
        request.setConfig(requestConfig);

        Future<HttpResponse> future = httpAsyncClient.execute(request, null);
        if (future.get().getStatusLine().getStatusCode() == 200) {
            return EntityUtils.toString(future.get().getEntity(), "UTF-8");
        } else {
            throw new IllegalStateException("[boxfish-component] [get] 数据状态异常.");
        }
    }

    public String get(String url) throws Exception {
        return get(url, null, null);
    }

    public String post(String url, Map<String, String> parameters, Map<String, String> headers, String body) throws Exception {
        HttpPost request = new HttpPost(HttpRequestParameters.build(url, parameters));
        request.setHeaders(HttpRequestHeader.build(headers));
        request.setEntity(new StringEntity(body, "UTF-8"));
        request.setConfig(requestConfig);

        Future<HttpResponse> future = httpAsyncClient.execute(request, null);
        if (future.get().getStatusLine().getStatusCode() == 200) {
            return EntityUtils.toString(future.get().getEntity(), "UTF-8");
        } else {
            throw new IllegalStateException("[boxfish-component] [post] 数据状态异常.");
        }
    }

    public String post(String url, Map<String, String> parameters, Map<String, String> headers, String username, String password, String body) throws Exception {
        HttpPost request = new HttpPost(HttpRequestParameters.build(url, parameters));
        request.setHeaders(HttpRequestHeader.build(headers, username, password));
        request.setEntity(new StringEntity(body, "UTF-8"));
        request.setConfig(requestConfig);

        Future<HttpResponse> future = httpAsyncClient.execute(request, null);
        if (future.get().getStatusLine().getStatusCode() == 200) {
            return EntityUtils.toString(future.get().getEntity(), "UTF-8");
        } else {
            throw new IllegalStateException("[boxfish-component] [post] 数据状态异常.");
        }
    }

    public String post(String url, String body) throws Exception {
        return post(url, null, null, body);
    }

}
