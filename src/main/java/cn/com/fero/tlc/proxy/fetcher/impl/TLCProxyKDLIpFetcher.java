package cn.com.fero.tlc.proxy.fetcher.impl;

import cn.com.fero.tlc.proxy.common.TLCProxyConstants;
import cn.com.fero.tlc.proxy.exception.TLCProxyProxyException;
import cn.com.fero.tlc.proxy.http.TLCProxyHTMLParser;
import cn.com.fero.tlc.proxy.http.TLCProxyRequest;
import cn.com.fero.tlc.proxy.fetcher.TLCProxyIpFetcher;
import cn.com.fero.tlc.proxy.util.TLCProxyLoggerUtil;
import org.apache.commons.lang3.StringUtils;
import org.htmlcleaner.TagNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanghongmeng on 2015/7/15.
 */
public class TLCProxyKDLIpFetcher extends TLCProxyIpFetcher {
    @Value("${tlc.proxy.url.fetch.kdl}")
    private String fetchUrl;

    private List<TagNode> getIpNode(String urlPrefix, int page) {
        String url = urlPrefix + page;
        String content = TLCProxyRequest.get(url, false)
                .get(TLCProxyConstants.SPIDER_CONST_RESPONSE_CONTENT).toString();
        return TLCProxyHTMLParser.parseNode(content, "//table[@class='table table-bordered table-striped']/tbody/tr");
    }

    private String getTotalPage(String urlPrefix) {
        String url = urlPrefix + 1;
        String content = TLCProxyRequest.get(url, false)
                .get(TLCProxyConstants.SPIDER_CONST_RESPONSE_CONTENT).toString();
        List<TagNode> pageNodeList = TLCProxyHTMLParser.parseNode(content, "//div[@id='listnav']/ul/li");
        int length = pageNodeList.size();
        return pageNodeList.get(length - 2).getText().toString();
    }

    @Override
    public List<String> doFetch() {
        List<String> ipList = new ArrayList();
        try {
            String totalPage = getTotalPage(fetchUrl);
            TLCProxyLoggerUtil.getLogger().info("抓取快代理，总页数:" + totalPage);

            int totalPageNum = Integer.parseInt(totalPage);
            for (int page = 1; page <= totalPageNum; page++) {
                TLCProxyLoggerUtil.getLogger().info("开始抓取快代理第" + page + "页");
                List<TagNode> ipNodeList = getIpNode(fetchUrl, page);
                addToIpList(ipNodeList, ipList);
            }

            return ipList;
        } catch (Exception e) {
            throw new TLCProxyProxyException(e);
        } finally {
            TLCProxyLoggerUtil.getLogger().info("抓取快代理结束");
        }
    }

    private void addToIpList(List<TagNode> ipNodeList, List<String> ipList) throws IOException {
        for (TagNode ipNode : ipNodeList) {
            String type = TLCProxyHTMLParser.parseText(ipNode, "td[4]");
            if(StringUtils.containsIgnoreCase(type, TLCProxyConstants.SPIDER_CONST_HTTPS)) {
                String ip = TLCProxyHTMLParser.parseText(ipNode, "td[1]");
                String port = TLCProxyHTMLParser.parseText(ipNode, "td[2]");
                if (StringUtils.isEmpty(ip) || StringUtils.isEmpty(port)) {
                    continue;
                }

                String ipStr = ip + TLCProxyConstants.SPIDER_CONST_COLON + port;
                ipList.add(ipStr);
            }
        }
    }
}
