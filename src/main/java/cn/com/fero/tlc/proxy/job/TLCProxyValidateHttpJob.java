package cn.com.fero.tlc.proxy.job;

import cn.com.fero.tlc.proxy.common.TLCProxyConstants;
import cn.com.fero.tlc.proxy.common.TLCProxyJsonUtil;
import cn.com.fero.tlc.proxy.http.TLCProxyRequest;
import cn.com.fero.tlc.proxy.logger.TLCProxyLogger;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by wanghongmeng on 2015/7/15.
 */
@Component
@EnableScheduling
public class TLCProxyValidateHttpJob extends TLCProxyJob {
    @Value("${tlc.proxy.url.test.http}")
    private String httpTestUrl;
    @Value("${tlc.proxy.ip.localhost}")
    private String localhost;
    @Resource
    private Set<String> httpProxy;
    private final Map<String, String> param = new HashMap(){{put("ip", "myip");}};


    @Scheduled(cron = "0/10 * * * * *")
    public void startJob() {
        execute(new TLCProxyJobExecutor() {
            @Override
            public void doExecute() throws Exception {
                populateProxy(httpFetchQueue, httpProxy, TLCProxyConstants.PROXY_TYPE.HTTP);
                validateProxy(httpProxy, TLCProxyConstants.PROXY_TYPE.HTTP, new TLCProxyJobValidator() {
                    @Override
                    public boolean doValidate(String ip, String port) throws Exception {
                        if (BooleanUtils.isFalse(NumberUtils.isNumber(port))) {
                            return false;
                        }

                        Map<String, Object> response = TLCProxyRequest.postViaProxy(httpTestUrl, param, ip, Integer.parseInt(port));
                        int responseCode = (int) response.get(TLCProxyConstants.SPIDER_CONST_RESPONSE_STATUS);

                        if (responseCode != TLCProxyConstants.SPIDER_CONST_RESPONSE_STATUS_SUCCESS) {
                            return false;
                        }

                        String responseContent = (String) response.get(TLCProxyConstants.SPIDER_CONST_RESPONSE_CONTENT);
                        String data = TLCProxyJsonUtil.getString(responseContent, "data");
                        if (StringUtils.isEmpty(data)) {
                            return false;
                        }

                        String responseIp = TLCProxyJsonUtil.getString(data, "ip");
                        if (StringUtils.equalsIgnoreCase(responseIp, localhost)) {
                            return false;
                        }

                        return true;
                    }
                });
            }
        });
    }
}
