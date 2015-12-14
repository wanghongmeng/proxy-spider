package cn.com.fero.util.proxy.http.test;

import cn.com.fero.util.proxy.service.TLCProxyMailService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by gizmo on 15/6/19.
 */
public class TLCProxyMailTest extends TLCProxyTest {

    @Autowired
    private TLCProxyMailService tlcProxyMailService;

    @Test
    public void testSendMial() {
        tlcProxyMailService.sendTextMail("wanghongmeng@fero.com.cn", "test", "aaaaaaaaaaaaa");
    }

    @Test
    public void testSendErrorMial() {
        tlcProxyMailService.sendErrorMail("发生重大异常");
    }
}
