package cn.com.fero.tlc.proxy.http.test;

import cn.com.fero.tlc.proxy.http.TLCProxyRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gizmo on 15/6/19.
 */
@SpringApplicationConfiguration(classes = TLCProxyHttpTest.class)
public class TLCProxyHttpTest extends TLCProxyTest {

    @Test
    public void testProxy() throws UnsupportedEncodingException {
        Map<String, String> param = new HashMap(){{put("ip", "myip");}};
        Map response = TLCProxyRequest.postViaProxy("http://ip.taobao.com/service/getIpInfo2.php", param, "61.138.78.69", 8080);
        System.out.println(response);
    }
}
