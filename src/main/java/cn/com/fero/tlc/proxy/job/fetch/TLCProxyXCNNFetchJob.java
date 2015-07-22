package cn.com.fero.tlc.proxy.job.fetch;

import cn.com.fero.tlc.proxy.job.TLCProxyJob;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by wanghongmeng on 2015/7/15.
 */
@Component
@EnableScheduling
public class TLCProxyXCNNFetchJob extends TLCProxyJob {
    @Value("${tlc.proxy.url.fetch.xcnn}")
    private String fetchUrl;

    @Scheduled(cron = "0 0 */1 * * ?")
    @Override
    public void execute() {
        tlcProxyLoggerService.getLogger().info("开始抓取西刺国内高匿代理");
        String nodePath = "//table[@id='ip_list']/tbody/tr";
        String typePath = "td[7]";
        String ipPath = "td[3]";
        String portPath = "td[4]";
        fetchProxy(fetchUrl, nodePath, typePath, ipPath, portPath);
        tlcProxyLoggerService.getLogger().info("抓取西刺国内高匿代理结束");
    }
}
