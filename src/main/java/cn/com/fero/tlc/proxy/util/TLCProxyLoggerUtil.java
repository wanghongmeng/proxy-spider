package cn.com.fero.tlc.proxy.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by gizmo on 15/6/17.
 */
public final class TLCProxyLoggerUtil {
    private static final Logger logger = LoggerFactory.getLogger(TLCProxyLoggerUtil.class);

    private TLCProxyLoggerUtil() {
        throw new UnsupportedOperationException();
    }

    public static Logger getLogger() {
        return logger;
    }
}
