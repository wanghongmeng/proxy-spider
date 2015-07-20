package cn.com.fero.tlc.proxy.common;

/**
 * Created by wanghongmeng on 2015/6/24.
 */
public final class TLCProxyConstants {
    public static final String SPIDER_CONST_CHARACTER_ENCODING = "UTF-8";
    public static final String SPIDER_CONST_COLON = ":";
    public static final String SPIDER_CONST_RESPONSE_STATUS = "status";
    public static final String SPIDER_CONST_RESPONSE_CONTENT = "content";
    public static final int SPIDER_CONST_RESPONSE_STATUS_SUCCESS = 200;
    public static final int SPIDER_CONST_RESPONSE_STATUS_FAIL = 500;
    public static final int SPIDER_CONST_HTTP_TIMEOUT = 5000;
    public static final int SPIDER_CONST_THREAD_SIZE = 5;
    public static final int SPIDER_CONST_QUEUE_INIT_SIZE = 1000;
    public static final String SPIDER_CONST_DATE_TIME_FORMAT = "yyyy-MM-ss HH:mm:ss";

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
