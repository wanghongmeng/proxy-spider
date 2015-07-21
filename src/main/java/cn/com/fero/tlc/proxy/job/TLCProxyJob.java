package cn.com.fero.tlc.proxy.job;

import cn.com.fero.tlc.proxy.common.TLCProxyConstants;
import cn.com.fero.tlc.proxy.service.TLCProxyJsonService;
import cn.com.fero.tlc.proxy.service.TLCProxyLoggerService;
import cn.com.fero.tlc.proxy.service.TLCProxyRequestService;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.Queue;

/**
 * Created by wanghongmeng on 2015/7/15.
 */
@Component
public abstract class TLCProxyJob {
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


    public void execute(TLCProxyJobExecutor executor) {
        try {
            executor.doExecute();
        } catch (Exception e) {
            tlcProxyLoggerService.getLogger().error(ExceptionUtils.getFullStackTrace(e));
        }
    }

    protected void populateProxy(Queue<String> fetchQueue, Queue<String> proxy, TLCProxyConstants.PROXY_TYPE proxyType) {
        if (fetchQueue.isEmpty()) {
            tlcProxyLoggerService.getLogger().info("{}抓取队列为空", proxyType.toString());
        } else {
            String ele;
            while ((ele = fetchQueue.poll()) != null) {
                proxy.add(ele);
            }
        }
    }

    protected void validateProxy(Queue<String> proxy, TLCProxyConstants.PROXY_TYPE proxyType, TLCProxyJobValidator validator) throws Exception {
        Iterator<String> proxyIterator = proxy.iterator();
        while (proxyIterator.hasNext()) {
            String ele = proxyIterator.next();
            tlcProxyLoggerService.getLogger().info("验证{}代理: {}", proxyType.toString(), ele);

            String[] ipAddressArray = ele.split(TLCProxyConstants.SPIDER_CONST_COLON);
            String ip = ipAddressArray[0];
            String port = ipAddressArray[1];

            if (BooleanUtils.isFalse(validator.doValidate(ip, port))) {
                tlcProxyLoggerService.getLogger().info("{}代理不可用，删除代理： {}", proxyType.toString(), ele);
                proxyIterator.remove();
            } else {
                tlcProxyLoggerService.getLogger().info("{}代理{}可用", proxyType.toString(), ele);
            }
        }
    }

    interface TLCProxyJobExecutor {
        public void doExecute() throws Exception;
    }

    interface TLCProxyJobValidator {
        public boolean doValidate(String ip, String port) throws Exception;
    }
}
