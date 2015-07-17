package cn.com.fero.tlc.spider.common;

import cn.com.fero.tlc.spider.util.TLCSpiderPropertiesUtil;

/**
 * Created by wanghongmeng on 2015/6/24.
 */
public final class TLCSpiderConstants {
    public static final String SPIDER_CONST_CHARACTER_ENCODING = "UTF-8";
    public static final String SPIDER_CONST_COLON = ":";
    public static final String SPIDER_CONST_HTTP_PROXY_SET = "http.proxySet";
    public static final String SPIDER_CONST_HTTP_PROXY_HOST = "http.proxyHost";
    public static final String SPIDER_CONST_HTTP_PROXY_PORT = "http.proxyPort";
    public static final String SPIDER_CONST_HTTPS = "HTTPS";
    public static final String SPIDER_CONST_RESPONSE_STATUS_CODE = "statusCode";
    public static final String SPIDER_CONST_RESPONSE_CONTENT = "content";
    public static final int SPIDER_CONST_RESPONSE_STATUS_SUCCESS = 200;
    public static final int SPIDER_CONST_HTTP_TIMEOUT = 10000;
    public static final int SPIDER_CONST_THREAD_SIZE = 5;
    public static volatile boolean SPIDER_CONST_PROXY_STATUS = false;
    private TLCSpiderConstants() {
        throw new UnsupportedOperationException();
    }
}
