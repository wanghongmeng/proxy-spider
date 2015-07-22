package cn.com.fero.tlc.proxy.job.validate;

import cn.com.fero.tlc.proxy.common.TLCProxyConstants;
import cn.com.fero.tlc.proxy.job.TLCProxyJob;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Queue;

/**
 * Created by wanghongmeng on 2015/7/15.
 */
@Component
@EnableScheduling
public class TLCProxyValidateHttpsJob extends TLCProxyJob {
    @Value("${tlc.proxy.url.test.https}")
    private String httpsTestUrl;
    @Resource
    private Queue<String> httpsProxy;

    @Scheduled(cron = "0 */10 * * * ?")
    @Override
    public void execute() {
        populateProxy(httpsFetchQueue, httpsProxy, TLCProxyConstants.PROXY_TYPE.HTTPS);
        validateProxy(httpsProxy, TLCProxyConstants.PROXY_TYPE.HTTPS, new TLCProxyJobValidator() {
            @Override
            public boolean doValidate(String ip, int port) {
                Map<String, Object> response = tlcProxyRequestService.getViaProxy(httpsTestUrl, ip, port);
                int responseStatusCode = (int) response.get(TLCProxyConstants.SPIDER_CONST_RESPONSE_STATUS);

                return responseStatusCode == TLCProxyConstants.SPIDER_CONST_RESPONSE_STATUS_SUCCESS;
            }
        });
    }
}
