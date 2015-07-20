package cn.com.fero.tlc.proxy.configuration;

import cn.com.fero.tlc.proxy.common.TLCProxyConstants;
import cn.com.fero.tlc.proxy.fetcher.TLCProxyIpFetcher;
import cn.com.fero.tlc.proxy.fetcher.impl.TLCProxyDL5566IpFetcher;
import cn.com.fero.tlc.proxy.fetcher.impl.TLCProxyKDLIpFetcher;
import cn.com.fero.tlc.proxy.fetcher.impl.TLCProxyXCNNIpFetcher;
import cn.com.fero.tlc.proxy.fetcher.impl.TLCProxyXCNTIpFetcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by wanghongmeng on 2015/7/17.
 */
@Configuration
public class TLCProxyConfiguration {
    @Bean
    public TLCProxyIpFetcher dl5566IpFetcher() {
        return new TLCProxyDL5566IpFetcher();
    }

    @Bean
    public TLCProxyIpFetcher kdlIpFetcher() {
        return new TLCProxyKDLIpFetcher();
    }

    @Bean
    public TLCProxyIpFetcher xcnnIpFetcher() {
        return new TLCProxyXCNNIpFetcher();
    }

    @Bean
    public TLCProxyIpFetcher xcntIpFetcher() {
        return new TLCProxyXCNTIpFetcher();
    }

    @Bean
    public List<TLCProxyIpFetcher> fetchers() {
        List<TLCProxyIpFetcher> fetchList = new ArrayList<>();
        fetchList.add(dl5566IpFetcher());
        fetchList.add(kdlIpFetcher());
        fetchList.add(xcnnIpFetcher());
        fetchList.add(xcntIpFetcher());
        return fetchList;
    }

    @Bean
    public LinkedBlockingDeque<String> httpFetchQueue() {
        return new LinkedBlockingDeque(TLCProxyConstants.SPIDER_CONST_QUEUE_INIT_SIZE);
    }

    @Bean
    public LinkedBlockingDeque<String> httpsFetchQueue() {
        return new LinkedBlockingDeque(TLCProxyConstants.SPIDER_CONST_QUEUE_INIT_SIZE);
    }

    @Bean
    public Set<String> httpProxy() {
        return new HashSet();
    }

    @Bean
    public Set<String> httpsProxy() {
        return new HashSet();
    }

    @Bean
    public ExecutorService threadPool() {
        return Executors.newFixedThreadPool(TLCProxyConstants.SPIDER_CONST_THREAD_SIZE);
    }
}
