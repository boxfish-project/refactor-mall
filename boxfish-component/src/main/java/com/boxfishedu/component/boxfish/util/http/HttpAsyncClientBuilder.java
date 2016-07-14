package com.boxfishedu.component.boxfish.util.http;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;


public class HttpAsyncClientBuilder {

    // Default
    static public CloseableHttpAsyncClient build() {
        CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
        httpclient.start();
        return httpclient;
    }

    // Password
    static public CloseableHttpAsyncClient build(String username, String password)  {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
                new UsernamePasswordCredentials(username, password));

        CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom()
                .setDefaultCredentialsProvider(credentialsProvider)
                .build();
        httpclient.start();
        return httpclient;
    }

    // SSL
    static public CloseableHttpAsyncClient build(File keyStorePath, String password) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");

        try (FileInputStream instream = new FileInputStream(keyStorePath)) {
            keyStore.load(instream, password.toCharArray());
        }

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, password.toCharArray())
                .build();

        // Allow TLSv1 protocol only
        SSLIOSessionStrategy sslSessionStrategy = new SSLIOSessionStrategy(
                sslcontext,
                new String[]{"TLSv1"},
                null,
                SSLIOSessionStrategy.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

        CloseableHttpAsyncClient httpClient = HttpAsyncClients.custom()
                .setSSLStrategy(sslSessionStrategy)
                .build();

        httpClient.start();
        return httpClient;
    }

}
