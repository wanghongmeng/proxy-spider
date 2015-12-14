package cn.com.fero.util.proxy.exception;

/**
 * Created by wanghongmeng on 2015/5/13.
 */
public final class TLCProxyProxyException extends RuntimeException {
    public TLCProxyProxyException() {
    }

    public TLCProxyProxyException(String message) {
        super(message);
    }

    public TLCProxyProxyException(String message, Throwable cause) {
        super(message, cause);
    }

    public TLCProxyProxyException(Throwable cause) {
        super(cause);
    }

    public TLCProxyProxyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
