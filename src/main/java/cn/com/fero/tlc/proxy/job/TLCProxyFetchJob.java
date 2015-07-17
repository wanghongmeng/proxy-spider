package cn.com.fero.tlc.proxy.job;

import cn.com.fero.tlc.proxy.exception.TLCProxyProxyException;
import cn.com.fero.tlc.proxy.fetcher.TLCProxyIpFetcher;
import cn.com.fero.tlc.proxy.util.TLCProxyLoggerUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * Created by wanghongmeng on 2015/7/15.
 */
@Component
@EnableScheduling
public class TLCProxyFetchJob {
    @Resource
    public Set<String> usefulIp;
    @Autowired
    private List<TLCProxyIpFetcher> fetchers;
    @Autowired
    private ExecutorService threadPool;

    @Scheduled(cron = "0/10 * * * * *")
    public void execute() {
        try {
            usefulIp.clear();
            final CountDownLatch gate = new CountDownLatch(fetchers.size());
            for (final TLCProxyIpFetcher ipFetcher : fetchers) {
                threadPool.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            usefulIp.addAll(ipFetcher.doFetch());
                        } catch (Exception e) {
                            throw new TLCProxyProxyException(e);
                        } finally {
                            gate.countDown();
                        }
                    }
                });
            }

            gate.await();
            TLCProxyLoggerUtil.getLogger().info("抓取代理IP结束");
        } catch (InterruptedException e) {
            TLCProxyLoggerUtil.getLogger().error(ExceptionUtils.getStackTrace(e));
        }
    }
}
