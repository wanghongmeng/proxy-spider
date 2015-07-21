package cn.com.fero.tlc.proxy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by gizmo on 15/6/17.
 */
@Service
public class TLCProxyLoggerService {
    private static final Logger logger = LoggerFactory.getLogger(TLCProxyLoggerService.class);

    public Logger getLogger() {
        return logger;
    }
}
