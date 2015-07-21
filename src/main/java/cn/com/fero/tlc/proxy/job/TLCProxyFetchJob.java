package cn.com.fero.tlc.proxy.job;

import cn.com.fero.tlc.proxy.common.TLCProxyConstants;
import cn.com.fero.tlc.proxy.common.TLCProxyProxyException;
import cn.com.fero.tlc.proxy.fetcher.TLCProxyIpFetcher;
import cn.com.fero.tlc.proxy.logger.TLCProxyLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * Created by wanghongmeng on 2015/7/15.
 */
@Component
@EnableScheduling
public class TLCProxyFetchJob extends TLCProxyJob {
    @Autowired
    private ExecutorService threadPool;
    @Autowired
    private List<TLCProxyIpFetcher> fetchers;

    @Scheduled(cron = "0 0 */1 * * ?")
    public void startJob() {
        execute(new TLCProxyJobExecutor() {
            @Override
            public void doExecute() throws Exception {
                final CountDownLatch gate = new CountDownLatch(fetchers.size());
                for (final TLCProxyIpFetcher ipFetcher : fetchers) {
                    threadPool.submit(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Map<String, TLCProxyConstants.PROXY_TYPE> ipMap = ipFetcher.doFetch();

                                for (Map.Entry<String, TLCProxyConstants.PROXY_TYPE> entry : ipMap.entrySet()) {
                                    if (entry.getValue() == TLCProxyConstants.PROXY_TYPE.HTTP) {
                                        httpFetchQueue.add(entry.getKey());
                                    }
                                    if (entry.getValue() == TLCProxyConstants.PROXY_TYPE.HTTPS) {
                                        httpsFetchQueue.add(entry.getKey());
                                    }
                                }
                            } catch (Exception e) {
                                throw new TLCProxyProxyException(e);
                            } finally {
                                gate.countDown();
                            }
                        }
                    });
                }
                gate.await();
                TLCProxyLogger.getLogger().info("抓取代理IP结束");
            }
        });
    }
}
