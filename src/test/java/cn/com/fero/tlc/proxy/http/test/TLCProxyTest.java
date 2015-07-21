package cn.com.fero.tlc.proxy.http.test;

import cn.com.fero.tlc.proxy.TCLProxyServer;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by gizmo on 15/6/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TCLProxyServer.class)
@WebAppConfiguration
public abstract class TLCProxyTest {
}
