package org.sonarsource.plugins.report.support;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.sonarsource.plugins.report.constant.WSConfig;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;


@Slf4j
public final class RequestManager {

    /**
     * Encoding for http content
     */
    private static final String UTF_8 = "UTF-8";
    /**
     * HeaderFooter http for setting the content type of a request
     */
    private static final String CONTENT_TYPE = "content-type";

    /**
     * reuse Cookie
     */
    private static final String COOKIE = "Cookie";

    /**
     * Authorization
     */
    private static final String AUTHORIZATION = "Authorization";
    /**
     * Json type for a content
     */
    private static final String APPLICATION_JSON = "application/json";

    /**
     * Instance of the singleton
     */
    private static RequestManager ourInstance = null;

    /**
     * Use of private constructor to singletonize this class
     */
    private RequestManager() {
    }

    /**
     * Return the unique instance
     *
     * @return the singleton
     */
    public static synchronized RequestManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new RequestManager();
        }
        return ourInstance;
    }

    /**
     * Execute a get http request
     *
     * @param url getUrl to request
     * @return response as string
     * @throws IOException error on response
     */
    public String get(String url) throws IOException {
        // returned string containing the response as raw string
        final String toReturn;
        // create a client
        final CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // set the request
        final HttpGet request = new HttpGet(url);
        // set content type to json
        request.addHeader(CONTENT_TYPE, APPLICATION_JSON);
//        request.addHeader(COOKIE, RequestContext.getCookie().orElse(""));
        request.addHeader(AUTHORIZATION, getAuthorization());
        // future result of the request
        final HttpResponse result;
        try {
            // execute the request
            result = httpClient.execute(request);
            // convert to string
            toReturn = EntityUtils.toString(result.getEntity(), UTF_8);
        } finally {
            // always close the connexion
            request.reset();
            httpClient.close();
        }
        // return string result
        return toReturn;
    }

    /**
     * Execute a get http request
     *
     * @param url  getUrl to request
     * @param data list of pairs containing resources to post
     * @return response as string
     * @throws IOException error on response
     */
    public String post(String url, List<NameValuePair> data) throws IOException {
        // create a client
        final CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // set the request
        final HttpPost request = new HttpPost(url);
        request.addHeader(CONTENT_TYPE, APPLICATION_JSON);
//        request.addHeader(COOKIE, RequestContext.getCookie().orElse(""));
        request.addHeader(AUTHORIZATION, getAuthorization());
        request.setEntity(new UrlEncodedFormEntity(data));
        // future result of the request
        final HttpResponse result;
        try {
            // execute the request
            result = httpClient.execute(request);
        } finally {
            // always close the connexion
            request.reset();
        }

        // return string result
        return EntityUtils.toString(result.getEntity(), UTF_8);
    }


    private String getAuthorization() {
        String token = PropertyUtils.get("sonar.token");
        String authorization = null;
        try {
            authorization = Base64.getEncoder().encodeToString((token + ":").getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            log.warn("get authorization failed", e);
        }
        return authorization != null ? "Basic " + authorization : null;
    }
}
