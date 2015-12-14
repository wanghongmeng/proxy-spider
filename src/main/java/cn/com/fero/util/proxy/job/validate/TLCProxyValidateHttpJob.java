package cn.com.fero.util.proxy.job.validate;

import cn.com.fero.util.proxy.common.TLCProxyConstants;
import cn.com.fero.util.proxy.job.TLCProxyJob;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by wanghongmeng on 2015/7/15.
 */
@Component
@EnableScheduling
public class TLCProxyValidateHttpJob extends TLCProxyJob {
    private final Map<String, String> param = new HashMap() {{
        put("ip", "myip");
    }};

    @Value("${tlc.proxy.url.test.http}")
    private String httpTestUrl;
    @Value("${tlc.proxy.ip.localhost}")
    private String localhost;
    @Resource
    private CopyOnWriteArrayList<String> httpProxy;

    @Scheduled(cron = TLCProxyConstants.SPIDER_CONST_CRON_EXPRESSION_VALIDATION)
    @Override
    public void execute() {
        populateProxy(httpFetchQueue, httpProxy, TLCProxyConstants.PROXY_TYPE.HTTP);
        validateProxy(httpProxy, TLCProxyConstants.PROXY_TYPE.HTTP, new TLCProxyJobValidator() {
            @Override
            public boolean doValidate(String ip, int port) {
                Map<String, Object> response = tlcProxyRequestService.postViaProxy(httpTestUrl, param, ip, port);
                int responseCode = (int) response.get(TLCProxyConstants.SPIDER_CONST_RESPONSE_STATUS);

                if (responseCode != TLCProxyConstants.SPIDER_CONST_RESPONSE_STATUS_SUCCESS) {
                    return false;
                }

                String responseContent = (String) response.get(TLCProxyConstants.SPIDER_CONST_RESPONSE_CONTENT);
                String data = tlcProxyJsonService.getString(responseContent, "data");
                if (StringUtils.isEmpty(data)) {
                    return false;
                }

                String responseIp = tlcProxyJsonService.getString(data, "ip");
                if (StringUtils.equalsIgnoreCase(responseIp, localhost)) {
                    return false;
                }

                return true;
            }
        });
    }
}
