package cn.com.fero.tlc.proxy.job.fetch;

import cn.com.fero.tlc.proxy.common.TLCProxyConstants;
import cn.com.fero.tlc.proxy.job.TLCProxyJob;
import org.htmlcleaner.TagNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by wanghongmeng on 2015/7/15.
 */
@Component
@EnableScheduling
public class TLCProxyKDLFetchJob extends TLCProxyJob {
    @Value("${tlc.proxy.url.fetch.kdl}")
    private String fetchUrl;

    @Scheduled(cron = TLCProxyConstants.SPIDER_CONST_CRON_EXPRESSION_FETCH)
    @Override
    public void execute() {
        String totalPage = getTotalPage(fetchUrl);
        tlcProxyLoggerService.getLogger().info("抓取快代理，总页数:" + totalPage);
        int totalPageNum = Integer.parseInt(totalPage);

        String nodePath = "//table[@class='table table-bordered table-striped']/tbody/tr";
        String typePath = "td[4]";
        String ipPath = "td[1]";
        String portPath = "td[2]";

        for (int page = 1; page <= totalPageNum; page++) {
            tlcProxyLoggerService.getLogger().info("开始抓取快代理第" + page + "页");
            String url = fetchUrl + page;
            fetchProxy(url, nodePath, typePath, ipPath, portPath);
        }
        tlcProxyLoggerService.getLogger().info("抓取快代理结束");
    }

    private String getTotalPage(String urlPrefix) {
        String url = urlPrefix + 1;
        String content = tlcProxyRequestService.get(url)
                .get(TLCProxyConstants.SPIDER_CONST_RESPONSE_CONTENT).toString();
        List<TagNode> pageNodeList = tlcProxyHTMLService.parseNode(content, "//div[@id='listnav']/ul/li");
        int length = pageNodeList.size();
        return pageNodeList.get(length - 2).getText().toString();
    }
}
