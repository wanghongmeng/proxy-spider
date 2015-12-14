package cn.com.fero.util.proxy.job.fetch;

import cn.com.fero.util.proxy.common.TLCProxyConstants;
import cn.com.fero.util.proxy.job.TLCProxyJob;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by wanghongmeng on 2015/7/15.
 */
@Component
@EnableScheduling
public class TLCProxyDL5566FetchJob extends TLCProxyJob {
    @Value("${tlc.proxy.url.fetch.dl5566}")
    private String fetchUrl;

    @Scheduled(cron = TLCProxyConstants.SPIDER_CONST_CRON_EXPRESSION_FETCH)
    @Override
    public void execute() {
        tlcProxyLoggerService.getLogger().info("开始抓取代理5566国内高匿代理");
        String nodePath = "//div[@id='list']//table[@class='table table-bordered table-striped']/tbody/tr";
        String typePath = "td[4]";
        String ipPath = "td[1]";
        String portPath = "td[2]";
        fetchProxy(fetchUrl, nodePath, typePath, ipPath, portPath);
        tlcProxyLoggerService.getLogger().info("抓取代理5566国内高匿代理结束");
    }
}
