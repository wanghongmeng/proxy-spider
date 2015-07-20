package cn.com.fero.tlc.proxy.http.test;

import cn.com.fero.tlc.proxy.common.TLCProxyConstants;
import cn.com.fero.tlc.proxy.http.TLCProxyRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by gizmo on 15/6/19.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TLCProxyTest.class)
public class TLCProxyTest {

    @Test
    public void testProxy() {
        TLCProxyConstants.SPIDER_CONST_PROXY_STATUS = true;
        System.setProperty(TLCProxyConstants.SPIDER_CONST_HTTP_PROXY_SET, "true");
        System.setProperty(TLCProxyConstants.SPIDER_CONST_HTTP_PROXY_HOST, "218.92.227.172");
        System.setProperty(TLCProxyConstants.SPIDER_CONST_HTTP_PROXY_PORT, "34034");
//        System.setProperty(TLCSpiderConstants.SPIDER_CONST_HTTP_PROXY_HOST, "58.220.2.140");
//        System.setProperty(TLCSpiderConstants.SPIDER_CONST_HTTP_PROXY_PORT, "80");

//        QDYHCFEWJob job = new QDYHCFEWJob();
//        int totalPage = job.getTotalPage(job.constructSpiderParam());
//        System.out.println(totalPage);
        String response = TLCProxyRequest.get("http://1111.ip138.com/ic.asp", true)
                .get(TLCProxyConstants.SPIDER_CONST_RESPONSE_CONTENT).toString();
        System.out.println(response);
    }
}
