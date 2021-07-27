package com.fish.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @author: fjjdragon
 * @date: 2021-07-26 22:34
 */
@Slf4j
public class HttpRequestUtil {
    private static HttpRequestUtil instance = new HttpRequestUtil();

    public static HttpRequestUtil getInstance() {
        return instance;
    }

    private HttpRequestUtil() {
    }

    public void close() {
        if (null != httpClient) {
            try {
                httpClient.close();
            } catch (IOException e) {
                log.error("", e);
            }
        }
    }

    // *创建默认的httpClient实例(CloseableHttpClient).
    private CloseableHttpClient httpClient = HttpClients.createDefault();

    public JSONObject doGet(String url) {
        return getJsonObject(new HttpGet(url));
    }

    public JSONObject doPOst(String url, String data) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(data, "utf-8"));
        return getJsonObject(httpPost);
    }

    private JSONObject getJsonObject(HttpUriRequest httpRequest) {
        JSONObject response = null;
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String str = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
                response = JSON.parseObject(str);
            }
        } catch (IOException e) {
            log.error("", e);
        } finally {
            if (null != httpResponse) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    log.error("", e);
                }

            }
        }
        return response;
    }

    public static void main(String[] args) {



    }
}