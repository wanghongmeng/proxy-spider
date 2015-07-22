package cn.com.fero.tlc.proxy.common;

/**
 * Created by wanghongmeng on 2015/6/24.
 */
public final class TLCProxyConstants {
    public static final String SPIDER_CONST_CHARACTER_ENCODING = "UTF-8";
    public static final String SPIDER_CONST_COLON = ":";
    public static final String SPIDER_CONST_RESPONSE_STATUS = "status";
    public static final String SPIDER_CONST_RESPONSE_CONTENT = "content";
    public static final String SPIDER_CONST_DATE_TIME_FORMAT = "yyyy-MM-ss HH:mm:ss";
    public static final String SPIDER_CONST_RESPONSE_MESSAGE_NOT_FOUND = "没有数据";
    public static final String SPIDER_CONST_RESPONSE_MESSAGE_ERROR = "发生错误";
    public static final String SPIDER_CONST_CRON_EXPRESSION_FETCH = "0 0 */1 * * ?";
    public static final String SPIDER_CONST_CRON_EXPRESSION_VALIDATION = "0 */5 * * * ?";
    public static final int SPIDER_CONST_RESPONSE_STATUS_SUCCESS = 200;
    public static final int SPIDER_CONST_RESPONSE_STATUS_ERROR = 500;
    public static final int SPIDER_CONST_RESPONSE_STATUS_NOT_FOUND = 404;
    public static final int SPIDER_CONST_HTTP_TIMEOUT = 20000;
    public static final int SPIDER_CONST_THREAD_SIZE = 5;

    private TLCProxyConstants() {
        throw new UnsupportedOperationException();
    }

    public enum PROXY_TYPE {
        HTTP("http"), HTTPS("https");

        private String value;

        private PROXY_TYPE(String value) {
            this.value = value;
        }

        public String toString() {
            return this.value;
        }
    }


}
