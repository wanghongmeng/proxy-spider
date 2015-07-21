package cn.com.fero.tlc.proxy.fetcher.impl;

import cn.com.fero.tlc.proxy.common.TLCProxyConstants;
import cn.com.fero.tlc.proxy.exception.TLCProxyProxyException;
import cn.com.fero.tlc.proxy.fetcher.TLCProxyIpFetcher;
import org.apache.commons.lang3.StringUtils;
import org.htmlcleaner.TagNode;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wanghongmeng on 2015/7/15.
 */
public class TLCProxyKDLIpFetcher extends TLCProxyIpFetcher {
    @Value("${tlc.proxy.url.fetch.kdl}")
    private String fetchUrl;

    private List<TagNode> getIpNode(String urlPrefix, int page) {
        String url = urlPrefix + page;
        String content = tlcProxyRequestService.get(url)
                .get(TLCProxyConstants.SPIDER_CONST_RESPONSE_CONTENT).toString();
        return tlcProxyHTMLService.parseNode(content, "//table[@class='table table-bordered table-striped']/tbody/tr");
    }

    private String getTotalPage(String urlPrefix) {
        String url = urlPrefix + 1;
        String content = tlcProxyRequestService.get(url)
                .get(TLCProxyConstants.SPIDER_CONST_RESPONSE_CONTENT).toString();
        List<TagNode> pageNodeList = tlcProxyHTMLService.parseNode(content, "//div[@id='listnav']/ul/li");
        int length = pageNodeList.size();
        return pageNodeList.get(length - 2).getText().toString();
    }

    @Override
    public Map<String, TLCProxyConstants.PROXY_TYPE> doFetch() {
        Map<String, TLCProxyConstants.PROXY_TYPE> ipMap = new HashMap();
        try {
            String totalPage = getTotalPage(fetchUrl);
            tlcProxyLoggerService.getLogger().info("抓取快代理，总页数:" + totalPage);

            int totalPageNum = Integer.parseInt(totalPage);
            for (int page = 1; page <= totalPageNum; page++) {
                tlcProxyLoggerService.getLogger().info("开始抓取快代理第" + page + "页");
                List<TagNode> ipNodeList = getIpNode(fetchUrl, page);
                addToIpList(ipNodeList, ipMap);
            }

            return ipMap;
        } catch (Exception e) {
            throw new TLCProxyProxyException(e);
        } finally {
            tlcProxyLoggerService.getLogger().info("抓取快代理结束");
        }
    }

    private void addToIpList(List<TagNode> ipNodeList, Map<String, TLCProxyConstants.PROXY_TYPE> ipMap) throws IOException {
        for (TagNode ipNode : ipNodeList) {
            String type = tlcProxyHTMLService.parseText(ipNode, "td[4]");
            String ip = tlcProxyHTMLService.parseText(ipNode, "td[1]");
            String port = tlcProxyHTMLService.parseText(ipNode, "td[2]");

            if (StringUtils.isEmpty(ip) || StringUtils.isEmpty(port)) {
                continue;
            }

            String ipStr = ip + TLCProxyConstants.SPIDER_CONST_COLON + port;
            if (StringUtils.containsIgnoreCase(type, TLCProxyConstants.PROXY_TYPE.HTTP.toString())) {
                ipMap.put(ipStr, TLCProxyConstants.PROXY_TYPE.HTTP);
            }
            if (StringUtils.containsIgnoreCase(type, TLCProxyConstants.PROXY_TYPE.HTTPS.toString())) {
                ipMap.put(ipStr, TLCProxyConstants.PROXY_TYPE.HTTPS);
            }
        }
    }
}
