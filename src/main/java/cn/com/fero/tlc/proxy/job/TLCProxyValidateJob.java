package cn.com.fero.tlc.proxy.job;

import cn.com.fero.tlc.proxy.common.TLCProxyConstants;
import cn.com.fero.tlc.proxy.http.TLCProxyRequest;
import cn.com.fero.tlc.proxy.logger.TLCProxyLogger;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;

/**
 * Created by wanghongmeng on 2015/7/15.
 */
@Component
@EnableScheduling
public class TLCProxyValidateJob {

    @Value("${tlc.proxy.url.fetch.test}")
    private String testUrl;
    @Value("${tlc.proxy.ip.localhost}")
    private String localhost;
    @Resource
    private Set<String> usefulIp;

    @Scheduled(cron = "0/10 * * * * *")
    public void execute() {
        if (isValidateProxy()) {
            TLCProxyConstants.SPIDER_CONST_PROXY_STATUS = true;
        } else {
            TLCProxyConstants.SPIDER_CONST_PROXY_STATUS = false;
            updateProxy();
        }
    }

    public void updateProxy() {
        if (CollectionUtils.isEmpty(usefulIp)) {
            TLCProxyLogger.getLogger().info("代理IP列表为空");
            clearProxy();
            return;
        }

        int random = RandomUtils.nextInt(0, usefulIp.size());
        String ipAddress = (String) usefulIp.toArray()[random];
        TLCProxyLogger.getLogger().info("更新代理IP: " + ipAddress);
        String[] ipAddressArray = ipAddress.split(TLCProxyConstants.SPIDER_CONST_COLON);
        String ip = ipAddressArray[0];
        String port = ipAddressArray[1];
        System.setProperty(TLCProxyConstants.SPIDER_CONST_HTTP_PROXY_SET, "true");
        System.setProperty(TLCProxyConstants.SPIDER_CONST_HTTP_PROXY_HOST, ip);
        System.setProperty(TLCProxyConstants.SPIDER_CONST_HTTP_PROXY_PORT, port);

        if (isValidateProxy()) {
            TLCProxyConstants.SPIDER_CONST_PROXY_STATUS = true;
        } else {
            usefulIp.remove(ipAddress);
            updateProxy();
        }
    }

    public boolean isValidateProxy() {
        String ip = System.getProperty(TLCProxyConstants.SPIDER_CONST_HTTP_PROXY_HOST);
        String port = System.getProperty(TLCProxyConstants.SPIDER_CONST_HTTP_PROXY_PORT);

        if (StringUtils.isEmpty(ip) || StringUtils.isEmpty(port)) {
            return false;
        }

        if (BooleanUtils.isFalse(NumberUtils.isNumber(port))) {
            return false;
        }

        Map<String, Object> response;
        try {
            TLCProxyConstants.SPIDER_CONST_PROXY_STATUS = true;
            response = TLCProxyRequest.get(testUrl, true);
        } catch (Exception e) {
            TLCProxyLogger.getLogger().info("代理不可用，重新更新代理");
            return false;
        } finally {
            TLCProxyConstants.SPIDER_CONST_PROXY_STATUS = false;
        }

//        if (response.get(TLCSpiderConstants.SPIDER_CONST_RESPONSE_STATUS_CODE) == TLCSpiderConstants.SPIDER_CONST_RESPONSE_STATUS_SUCCESS
//                && !response.get(TLCSpiderConstants.SPIDER_CONST_RESPONSE_CONTENT).toString().contains(localhost)) {
//            TLCSpiderLoggerUtil.getLogger().info("代理可用，执行后续抓取");
//            return true;
//        }

        TLCProxyLogger.getLogger().info("代理不可用，重新更新代理");
        return false;
    }

    public void clearProxy() {
        TLCProxyLogger.getLogger().info("清除代理");
        System.setProperty(TLCProxyConstants.SPIDER_CONST_HTTP_PROXY_SET, "false");
        System.setProperty(TLCProxyConstants.SPIDER_CONST_HTTP_PROXY_HOST, StringUtils.EMPTY);
        System.setProperty(TLCProxyConstants.SPIDER_CONST_HTTP_PROXY_PORT, StringUtils.EMPTY);
    }
}
