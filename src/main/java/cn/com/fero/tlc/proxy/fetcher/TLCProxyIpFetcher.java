package cn.com.fero.tlc.proxy.fetcher;

import cn.com.fero.tlc.proxy.common.TLCProxyConstants;
import cn.com.fero.tlc.proxy.service.TLCProxyHTMLService;
import cn.com.fero.tlc.proxy.service.TLCProxyLoggerService;
import cn.com.fero.tlc.proxy.service.TLCProxyRequestService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by wanghongmeng on 2015/7/15.
 */
public abstract class TLCProxyIpFetcher {
    @Autowired
    protected TLCProxyLoggerService tlcProxyLoggerService;
    @Autowired
    protected TLCProxyRequestService tlcProxyRequestService;
    @Autowired
    protected TLCProxyHTMLService tlcProxyHTMLService;

    public abstract Map<String, TLCProxyConstants.PROXY_TYPE> doFetch();
}
