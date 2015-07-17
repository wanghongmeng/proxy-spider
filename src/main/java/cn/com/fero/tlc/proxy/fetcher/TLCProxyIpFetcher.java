package cn.com.fero.tlc.proxy.fetcher;

import java.util.List;

/**
 * Created by wanghongmeng on 2015/7/15.
 */
public abstract class TLCProxyIpFetcher {
    public abstract List<String> doFetch();
}
