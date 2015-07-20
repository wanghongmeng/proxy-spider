package cn.com.fero.tlc.proxy.fetcher;

import cn.com.fero.tlc.proxy.common.TLCProxyConstants;

import java.util.Map;

/**
 * Created by wanghongmeng on 2015/7/15.
 */
public abstract class TLCProxyIpFetcher {
    public abstract Map<String, TLCProxyConstants.PROXY_TYPE> doFetch();
}
