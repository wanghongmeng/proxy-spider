package cn.com.fero.tlc.proxy.fetcher.impl;

import cn.com.fero.tlc.proxy.common.TLCProxyConstants;
import cn.com.fero.tlc.proxy.exception.TLCProxyProxyException;
import cn.com.fero.tlc.proxy.fetcher.TLCProxyIpFetcher;
import org.apache.commons.lang3.StringUtils;
import org.htmlcleaner.TagNode;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wanghongmeng on 2015/7/15.
 */
public class TLCProxyXCNTIpFetcher extends TLCProxyIpFetcher {
    @Value("${tlc.proxy.url.fetch.xcnt}")
    private String fetchUrl;

    @Override
    public Map<String, TLCProxyConstants.PROXY_TYPE> doFetch() {
        try {
            tlcProxyLoggerService.getLogger().info("开始抓取西刺国内透明代理");
            Map<String, TLCProxyConstants.PROXY_TYPE> ipMap = new HashMap();

            String content = tlcProxyRequestService.get(fetchUrl)
                    .get(TLCProxyConstants.SPIDER_CONST_RESPONSE_CONTENT).toString();
            List<TagNode> ipNodeList = tlcProxyHTMLService.parseNode(content, "//table[@id='ip_list']/tbody/tr");

            for (TagNode ipNode : ipNodeList) {
                String type = tlcProxyHTMLService.parseText(ipNode, "td[7]");
                String ip = tlcProxyHTMLService.parseText(ipNode, "td[3]");
                String port = tlcProxyHTMLService.parseText(ipNode, "td[4]");
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

            return ipMap;
        } catch (Exception e) {
            throw new TLCProxyProxyException(e);
        } finally {
            tlcProxyLoggerService.getLogger().info("抓取西刺国内透明代理结束");
        }
    }
}
