package cn.com.fero.tlc.proxy.service;

import cn.com.fero.tlc.proxy.common.TLCProxyConstants;
import cn.com.fero.tlc.proxy.exception.TLCProxyProxyException;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
    @Autowired
    private TLCProxyLoggerService tlcProxyLoggerService;

    public Map<String, Object> getViaProxy(String url, String ip, int port) {
        if (StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException();
        }

        try {
            RequestConfig config = constructProxyConfig(ip, port);
            return executeGetRequest(url, config);
        } catch (Exception e) {
            tlcProxyLoggerService.getLogger().error("使用代理发生异常");
            return constructErrorResponse(ExceptionUtils.getFullStackTrace(e));
        }
    }

    public Map<String, Object> get(String url) {
        if (StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException();
        }

        try {
            RequestConfig config = constructHttpConfig();
            return executeGetRequest(url, config);
        } catch (Exception e) {
            throw new TLCProxyProxyException(e);
        }
    }

    public Map<String, Object> postViaProxy(String url, Map<String, String> param, String ip, int port) {
        if (StringUtils.isEmpty(url) || MapUtils.isEmpty(param)) {
            throw new IllegalArgumentException();
        }

        try {
            RequestConfig config = constructProxyConfig(ip, port);
            return executePostRequest(url, param, config);
        } catch (Exception e) {
            tlcProxyLoggerService.getLogger().error("使用代理发生异常");
            return constructErrorResponse(ExceptionUtils.getFullStackTrace(e));
        }
    }

    public Map<String, Object> post(String url, Map<String, String> param) {
        if (StringUtils.isEmpty(url) || MapUtils.isEmpty(param)) {
            throw new IllegalArgumentException();
        }

        try {
            RequestConfig config = constructHttpConfig();
            return executePostRequest(url, param, config);
        } catch (Exception e) {
            throw new TLCProxyProxyException(e);
        }
    }

    private Map<String, Object> executeGetRequest(String url, RequestConfig config) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        return executeRequest(httpClient, httpGet, config);
    }

    private Map<String, Object> executePostRequest(String url, Map<String, String> param, RequestConfig config) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        HttpEntity entity = constructPostEntity(param);
        httpPost.setEntity(entity);

        return executeRequest(httpClient, httpPost, config);
    }

    private HttpEntity constructPostEntity(Map<String, String> param) throws UnsupportedEncodingException {
        List<NameValuePair> paramList = new ArrayList();
        for (Map.Entry<String, String> entry : param.entrySet()) {
            paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return new UrlEncodedFormEntity(paramList, CharsetUtils.get(TLCProxyConstants.SPIDER_CONST_CHARACTER_ENCODING));
    }

    private RequestConfig constructProxyConfig(String ip, int port) {
        RequestConfig.Builder builder = RequestConfig.custom();
        builder.setConnectTimeout(TLCProxyConstants.SPIDER_CONST_HTTP_TIMEOUT);
        builder.setProxy(new HttpHost(ip, port));
        return builder.build();
    }

    private RequestConfig constructHttpConfig() {
        RequestConfig.Builder builder = RequestConfig.custom();
        builder.setConnectTimeout(TLCProxyConstants.SPIDER_CONST_HTTP_TIMEOUT);
        return builder.build();
    }

    private Map<String, Object> executeRequest(CloseableHttpClient httpClient, HttpRequestBase request, RequestConfig config) throws IOException {
        request.setConfig(config);
        CloseableHttpResponse response = httpClient.execute(request);

        Map<String, Object> responseMap = new HashMap();
        responseMap.put(TLCProxyConstants.SPIDER_CONST_RESPONSE_STATUS, response.getStatusLine().getStatusCode());
        responseMap.put(TLCProxyConstants.SPIDER_CONST_RESPONSE_CONTENT, EntityUtils.toString(response.getEntity(), TLCProxyConstants.SPIDER_CONST_CHARACTER_ENCODING));
        return responseMap;
    }

    private Map<String, Object> constructErrorResponse(String errorMessage) {
        Map<String, Object> responseMap = new HashMap();
        responseMap.put(TLCProxyConstants.SPIDER_CONST_RESPONSE_STATUS, TLCProxyConstants.SPIDER_CONST_RESPONSE_STATUS_ERROR);
        responseMap.put(TLCProxyConstants.SPIDER_CONST_RESPONSE_CONTENT, errorMessage);
        return responseMap;
    }
}
