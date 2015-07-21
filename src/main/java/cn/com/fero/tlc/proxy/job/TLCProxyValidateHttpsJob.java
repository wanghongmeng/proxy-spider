package cn.com.fero.tlc.proxy.job;

import cn.com.fero.tlc.proxy.common.TLCProxyConstants;
import cn.com.fero.tlc.proxy.http.TLCProxyRequest;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
    public void startJob() {
        execute(new TLCProxyJobExecutor() {
            @Override
            public void doExecute() throws Exception {
                populateProxy(httpsFetchQueue, httpsProxy, TLCProxyConstants.PROXY_TYPE.HTTPS);
                validateProxy(httpsProxy, TLCProxyConstants.PROXY_TYPE.HTTPS, new TLCProxyJobValidator() {
                    @Override
                    public boolean doValidate(String ip, String port) throws Exception {
                        if (BooleanUtils.isFalse(NumberUtils.isNumber(port))) {
                            return false;
                        }

                        Map<String, Object> response = TLCProxyRequest.getViaProxy(httpsTestUrl, ip, Integer.parseInt(port));
                        int responseStatusCode = (int) response.get(TLCProxyConstants.SPIDER_CONST_RESPONSE_STATUS);

                        return responseStatusCode == TLCProxyConstants.SPIDER_CONST_RESPONSE_STATUS_SUCCESS;
                    }
                });
            }
        });
    }
}
