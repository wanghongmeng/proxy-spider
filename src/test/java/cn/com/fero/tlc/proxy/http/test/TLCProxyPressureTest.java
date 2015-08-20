package cn.com.fero.tlc.proxy.http.test;

import cn.com.fero.tlc.proxy.service.TLCProxyRequestService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by gizmo on 15/6/19.
 */
public class TLCProxyPressureTest extends TLCProxyTest {
    @Autowired
    private TLCProxyRequestService tlcProxyRequestService;

    @Test
    public void testPressure() throws UnsupportedEncodingException, InterruptedException {
        int total = 100;
        Executor threadPool = Executors.newFixedThreadPool(total);
        final CountDownLatch gate = new CountDownLatch(total);
        for (int a = 0; a < total; a++) {
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        gate.countDown();
                        gate.await();
                        System.out.println(tlcProxyRequestService.get("http://localhost:9090/proxy/http"));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        Thread.sleep(Long.MAX_VALUE);
    }
}
