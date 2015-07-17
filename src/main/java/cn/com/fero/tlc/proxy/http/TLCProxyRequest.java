package cn.com.fero.tlc.proxy.http;

import cn.com.fero.tlc.proxy.common.TLCProxyConstants;
import cn.com.fero.tlc.proxy.exception.TLCProxyProxyException;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gizmo on 15/6/18.
 */
public class TLCProxyRequest {
    public static Map<String, Object> get(String url, boolean useProxy) {
        if (StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException();
        }

        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);

            RequestConfig.Builder builder = RequestConfig.custom();
            builder.setConnectTimeout(TLCProxyConstants.SPIDER_CONST_HTTP_TIMEOUT);

            if (useProxy && TLCProxyConstants.SPIDER_CONST_PROXY_STATUS) {
                builder.setProxy(getHost());
            }

            httpGet.setConfig(builder.build());
            CloseableHttpResponse response = httpClient.execute(httpGet);
            String responseContent = EntityUtils.toString(response.getEntity(), TLCProxyConstants.SPIDER_CONST_CHARACTER_ENCODING);

            Map<String, Object> responseMap = new HashMap();
            responseMap.put(TLCProxyConstants.SPIDER_CONST_RESPONSE_STATUS_CODE, response.getStatusLine().getStatusCode());
            responseMap.put(TLCProxyConstants.SPIDER_CONST_RESPONSE_CONTENT, responseContent);
            return responseMap;
        } catch (Exception e) {
            throw new TLCProxyProxyException(e);
        }
    }

    public static Map<String, Object> post(String url, Map<String, String> param, boolean useProxy) {
        if (StringUtils.isEmpty(url) || MapUtils.isEmpty(param)) {
            throw new IllegalArgumentException();
        }

        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);

            List<NameValuePair> paramList = new ArrayList();
            for (Map.Entry<String, String> entry : param.entrySet()) {
                paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(paramList, CharsetUtils.get(TLCProxyConstants.SPIDER_CONST_CHARACTER_ENCODING)));

            RequestConfig.Builder builder = RequestConfig.custom();
            builder.setConnectTimeout(TLCProxyConstants.SPIDER_CONST_HTTP_TIMEOUT);

            if (useProxy && TLCProxyConstants.SPIDER_CONST_PROXY_STATUS) {
                builder.setProxy(getHost());
            }

            httpPost.setConfig(builder.build());
            CloseableHttpResponse response = httpClient.execute(httpPost);
            String responseContent = EntityUtils.toString(response.getEntity(), TLCProxyConstants.SPIDER_CONST_CHARACTER_ENCODING);

            Map<String, Object> responseMap = new HashMap();
            responseMap.put(TLCProxyConstants.SPIDER_CONST_RESPONSE_STATUS_CODE, response.getStatusLine().getStatusCode());
            responseMap.put(TLCProxyConstants.SPIDER_CONST_RESPONSE_CONTENT, responseContent);
            return responseMap;
        } catch (Exception e) {
            throw new TLCProxyProxyException(e);
        }
    }

    private static HttpHost getHost() throws UnknownHostException {
        String ip = System.getProperty(TLCProxyConstants.SPIDER_CONST_HTTP_PROXY_HOST);
        String port = System.getProperty(TLCProxyConstants.SPIDER_CONST_HTTP_PROXY_PORT);
        int portNum = Integer.parseInt(port);
        return new HttpHost(ip, portNum);
    }
}
