package cn.com.fero.tlc.proxy.http.test;

import cn.com.fero.tlc.proxy.http.TLCProxyRequest;
import org.junit.Test;
import org.springframework.boot.test.SpringApplicationConfiguration;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gizmo on 15/6/19.
 */
@SpringApplicationConfiguration(classes = TLCProxyHttpTest.class)
public class TLCProxyHttpTest extends TLCProxyTest {

    @Test
    public void testHttp() throws UnsupportedEncodingException {
        Map<String, String> param = new HashMap() {{
            put("ip", "myip");
        }};
        Map response = TLCProxyRequest.postViaProxy("http://ip.taobao.com/service/getIpInfo2.php", param, "183.69.138.147", 80);
        System.out.println(response);
    }

    @Test
    public void testHttps() throws UnsupportedEncodingException {
        Map response = TLCProxyRequest.getViaProxy("https://www.baidu.com", "110.25.23.251", 8123);
        System.out.println(response);
    }
}
