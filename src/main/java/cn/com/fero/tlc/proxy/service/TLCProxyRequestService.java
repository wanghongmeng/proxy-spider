package cn.com.fero.tlc.proxy.service;

import cn.com.fero.tlc.proxy.common.TLCProxyConstants;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gizmo on 15/6/18.
 */
@Service
public class TLCProxyRequestService {

    public Map<String, Object> get(String url) {
        if (StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException();
        }

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);

        RequestConfig.Builder builder = RequestConfig.custom();
        builder.setConnectTimeout(TLCProxyConstants.SPIDER_CONST_HTTP_TIMEOUT);
        httpGet.setConfig(builder.build());

        return executeRequest(httpClient, httpGet);
    }

    public Map<String, Object> getViaProxy(String url, String ip, int port) {
        if (StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException();
        }

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);

        RequestConfig.Builder builder = RequestConfig.custom();
        builder.setConnectTimeout(TLCProxyConstants.SPIDER_CONST_HTTP_TIMEOUT);
        builder.setProxy(new HttpHost(ip, port));
        httpGet.setConfig(builder.build());
        return executeRequest(httpClient, httpGet);
    }


    public Map<String, Object> postViaProxy(String url, Map<String, String> param, String ip, int port) throws UnsupportedEncodingException {
        if (StringUtils.isEmpty(url) || MapUtils.isEmpty(param)) {
            throw new IllegalArgumentException();
        }

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        List<NameValuePair> paramList = new ArrayList();
        for (Map.Entry<String, String> entry : param.entrySet()) {
            paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(paramList, CharsetUtils.get(TLCProxyConstants.SPIDER_CONST_CHARACTER_ENCODING)));

        RequestConfig.Builder builder = RequestConfig.custom();
        builder.setConnectTimeout(TLCProxyConstants.SPIDER_CONST_HTTP_TIMEOUT);
        builder.setProxy(new HttpHost(ip, port));
        httpPost.setConfig(builder.build());

        return executeRequest(httpClient, httpPost);
    }

    private Map<String, Object> executeRequest(CloseableHttpClient httpClient, HttpUriRequest request) {
        Map<String, Object> responseMap = new HashMap();
        try {
            CloseableHttpResponse response = httpClient.execute(request);
            String responseContent = EntityUtils.toString(response.getEntity(), TLCProxyConstants.SPIDER_CONST_CHARACTER_ENCODING);

            responseMap.put(TLCProxyConstants.SPIDER_CONST_RESPONSE_STATUS, response.getStatusLine().getStatusCode());
            responseMap.put(TLCProxyConstants.SPIDER_CONST_RESPONSE_CONTENT, responseContent);
        } catch (Exception e) {
            responseMap.put(TLCProxyConstants.SPIDER_CONST_RESPONSE_STATUS, TLCProxyConstants.SPIDER_CONST_RESPONSE_STATUS_ERROR);
            responseMap.put(TLCProxyConstants.SPIDER_CONST_RESPONSE_CONTENT, ExceptionUtils.getFullStackTrace(e));
        }
        return responseMap;
    }
}
