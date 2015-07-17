package cn.com.fero.tlc.spider.fetcher;

import java.util.List;

/**
 * Created by wanghongmeng on 2015/7/15.
 */
public abstract class TLCSpiderIpFetcher {
    public abstract List<String> doFetch();
}
