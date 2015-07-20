package cn.com.fero.tlc.proxy.job;

import cn.com.fero.tlc.proxy.common.TLCProxyConstants;
import cn.com.fero.tlc.proxy.logger.TLCProxyLogger;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by wanghongmeng on 2015/7/15.
 */
@Component
public abstract class TLCProxyJob {
    @Resource
    protected LinkedBlockingDeque<String> httpFetchQueue;
    @Resource
    protected LinkedBlockingDeque<String> httpsFetchQueue;

    public void execute(TLCProxyJobExecutor executor){
        try{
            executor.doExecute();
        }catch(Exception e) {
            TLCProxyLogger.getLogger().error(ExceptionUtils.getFullStackTrace(e));
        }
    }

    protected void populateProxy(BlockingDeque<String> fetchQueue, Set<String> proxy, TLCProxyConstants.PROXY_TYPE proxyType) {
        if (fetchQueue.isEmpty()) {
            TLCProxyLogger.getLogger().info("{}代理队列为空", proxyType.toString());
        } else {
            String ele;
            while ((ele = fetchQueue.poll()) != null) {
                proxy.add(ele);
            }
        }
    }

    protected void validateProxy(Set<String> proxy, TLCProxyConstants.PROXY_TYPE proxyType, TLCProxyJobValidator validator) throws Exception {
        for (String ele : proxy) {
            TLCProxyLogger.getLogger().info("验证{}代理: {}", proxyType.toString(), ele);
            String[] ipAddressArray = ele.split(TLCProxyConstants.SPIDER_CONST_COLON);
            String ip = ipAddressArray[0];
            String port = ipAddressArray[1];
            if (BooleanUtils.isFalse(validator.doValidate(ip, port))) {
                TLCProxyLogger.getLogger().info("{}代理不可用，删除代理： {}", proxyType.toString(), ele);
                proxy.remove(ip);
            } else {
                TLCProxyLogger.getLogger().info("{}代理{}可用", proxyType.toString(), ele);
            }
        }
    }

    interface TLCProxyJobExecutor{
        public void doExecute() throws Exception;
    }

    interface TLCProxyJobValidator{
        public boolean doValidate(String ip, String port) throws Exception;
    }
}
