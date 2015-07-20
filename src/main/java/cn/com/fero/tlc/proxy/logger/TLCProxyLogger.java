package cn.com.fero.tlc.proxy.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by gizmo on 15/6/17.
 */
public final class TLCProxyLogger {
    private static final Logger logger = LoggerFactory.getLogger(TLCProxyLogger.class);

    private TLCProxyLogger() {
        throw new UnsupportedOperationException();
    }

    public static Logger getLogger() {
        return logger;
    }
}
