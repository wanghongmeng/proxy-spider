package cn.com.fero.tlc.proxy.fetcher;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by wanghongmeng on 2015/7/15.
 */
public abstract class TLCProxyIpFetcher {
    public abstract List<String> doFetch();
}
