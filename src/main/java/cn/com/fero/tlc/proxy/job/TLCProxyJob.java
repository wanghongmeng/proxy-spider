package cn.com.fero.tlc.proxy.job;

import cn.com.fero.tlc.proxy.common.TLCProxyConstants;
import cn.com.fero.tlc.proxy.exception.TLCProxyProxyException;
import cn.com.fero.tlc.proxy.service.TLCProxyHTMLService;
import cn.com.fero.tlc.proxy.service.TLCProxyJsonService;
import cn.com.fero.tlc.proxy.service.TLCProxyLoggerService;
import cn.com.fero.tlc.proxy.service.TLCProxyRequestService;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.htmlcleaner.TagNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executor;

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

    protected void populateProxy(Queue<String> fetchQueue, Queue<String> proxy, TLCProxyConstants.PROXY_TYPE proxyType) {
        try {
            if (fetchQueue.isEmpty()) {
                tlcProxyLoggerService.getLogger().info("{}抓取队列为空，无新添加{}代理", proxyType.toString(), proxyType.toString());
            } else {
                String ele;
                while ((ele = fetchQueue.poll()) != null) {
                    if (!proxy.contains(ele)) {
                        proxy.add(ele);
                    }
                }
            }
        } catch (Exception e) {
            throw new TLCProxyProxyException(e);
        }
    }

    protected void validateProxy(Queue<String> proxy, TLCProxyConstants.PROXY_TYPE proxyType, TLCProxyJobValidator validator) {
        try {
            Iterator<String> proxyIterator = proxy.iterator();
            while (proxyIterator.hasNext()) {
                String ele = proxyIterator.next();
                tlcProxyLoggerService.getLogger().info("验证{}代理: {}", proxyType.toString(), ele);

                String[] ipAddressArray = ele.split(TLCProxyConstants.SPIDER_CONST_COLON);
                String ip = ipAddressArray[0];
                String port = ipAddressArray[1];
                Integer portNum = Integer.parseInt(port);

                if (BooleanUtils.isFalse(validator.doValidate(ip, portNum))) {
                    tlcProxyLoggerService.getLogger().info("{}代理不可用，删除代理： {}", proxyType.toString(), ele);
                    proxyIterator.remove();
                } else {
                    tlcProxyLoggerService.getLogger().info("{}代理{}可用", proxyType.toString(), ele);
                }
            }
            tlcProxyLoggerService.getLogger().info("{}当前可用代理数: {}", proxyType.toString(), proxy.size());
        } catch (NumberFormatException e) {
            throw new TLCProxyProxyException(e);
        }
    }

    public interface TLCProxyJobValidator {
        public boolean doValidate(String ip, int port);
    }
}