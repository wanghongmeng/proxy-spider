package cn.com.fero.util.proxy.job;

import cn.com.fero.util.proxy.common.TLCProxyConstants;
import cn.com.fero.util.proxy.exception.TLCProxyProxyException;
import cn.com.fero.util.proxy.service.TLCProxyHTMLService;
import cn.com.fero.util.proxy.service.TLCProxyJsonService;
import cn.com.fero.util.proxy.service.TLCProxyLoggerService;
import cn.com.fero.util.proxy.service.TLCProxyRequestService;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.htmlcleaner.TagNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import javax.annotation.Resource;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Semaphore;

/**
 * Created by wanghongmeng on 2015/7/15.
 */
public abstract class TLCProxyJob implements SchedulingConfigurer {
    @Resource
    protected Queue<String> httpFetchQueue;
    @Resource
    protected Queue<String> httpsFetchQueue;
    @Autowired
    protected TLCProxyJsonService tlcProxyJsonService;
    @Autowired
    protected TLCProxyLoggerService tlcProxyLoggerService;
    @Autowired
    protected TLCProxyRequestService tlcProxyRequestService;
    @Autowired
    protected TLCProxyHTMLService tlcProxyHTMLService;
    @Autowired
    protected Executor schedulePool;
    @Autowired
    protected Executor threadPool;
    @Autowired
    protected Semaphore semaphore;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(schedulePool);
    }

    public abstract void execute();

    protected void fetchProxy(String fetchUrl, String nodePath, String typePath, String ipPath, String portPath) {
        try {
            String content = tlcProxyRequestService.get(fetchUrl)
                    .get(TLCProxyConstants.SPIDER_CONST_RESPONSE_CONTENT).toString();
            List<TagNode> ipNodeList = tlcProxyHTMLService.parseNode(content, nodePath);

            for (TagNode ipNode : ipNodeList) {
                String type = tlcProxyHTMLService.parseText(ipNode, typePath);
                String ip = tlcProxyHTMLService.parseText(ipNode, ipPath);
                String port = tlcProxyHTMLService.parseText(ipNode, portPath);

                if (StringUtils.isEmpty(ip) || StringUtils.isEmpty(port)) {
                    continue;
                }

                String ipStr = ip + TLCProxyConstants.SPIDER_CONST_COLON + port;
                if (StringUtils.containsIgnoreCase(type, TLCProxyConstants.PROXY_TYPE.HTTP.toString())) {
                    if (!httpFetchQueue.contains(ipStr)) {
                        httpFetchQueue.add(ipStr);
                    }
                }
                if (StringUtils.containsIgnoreCase(type, TLCProxyConstants.PROXY_TYPE.HTTPS.toString())) {
                    if (!httpsFetchQueue.contains(ipStr)) {
                        httpsFetchQueue.add(ipStr);
                    }
                }
            }
        } catch (Exception e) {
            throw new TLCProxyProxyException(e);
        }
    }

    protected void populateProxy(Queue<String> fetchQueue, CopyOnWriteArrayList<String> proxy, TLCProxyConstants.PROXY_TYPE proxyType) {
        try {
            if (fetchQueue.isEmpty()) {
                tlcProxyLoggerService.getLogger().info("{}抓取队列为空，无新添加{}代理", proxyType.toString(), proxyType.toString());
            } else {
                while (fetchQueue.peek() != null) {
                    proxy.addIfAbsent(fetchQueue.poll());
                }
            }
        } catch (Exception e) {
            throw new TLCProxyProxyException(e);
        }
    }

    protected void validateProxy(final CopyOnWriteArrayList<String> proxy, final TLCProxyConstants.PROXY_TYPE proxyType, final TLCProxyJobValidator validator) {
        try {
            for (final String proxyStr : proxy) {
                semaphore.acquire();
                String[] ipAddressArray = proxyStr.split(TLCProxyConstants.SPIDER_CONST_COLON);
                final String ip = ipAddressArray[0];
                String port = ipAddressArray[1];
                final Integer portNum = Integer.parseInt(port);

                threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            tlcProxyLoggerService.getLogger().info("验证{}代理: {}", proxyType.toString(), proxyStr);
                            if (BooleanUtils.isFalse(validator.doValidate(ip, portNum))) {
                                tlcProxyLoggerService.getLogger().info("{}代理不可用，删除代理： {}", proxyType.toString(), proxyStr);
                                proxy.remove(proxyStr);
                            } else {
                                tlcProxyLoggerService.getLogger().info("{}代理{}可用", proxyType.toString(), proxyStr);
                            }
                        } finally {
                            semaphore.release();
                        }
                    }
                });
            }
            tlcProxyLoggerService.getLogger().info("{}当前可用代理数: {}", proxyType.toString(), proxy.size());
        } catch (Exception e) {
            throw new TLCProxyProxyException(e);
        }
    }

    public interface TLCProxyJobValidator {
        public boolean doValidate(String ip, int port);
    }
}