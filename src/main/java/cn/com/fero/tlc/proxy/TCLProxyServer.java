package cn.com.fero.tlc.proxy;

import cn.com.fero.tlc.proxy.logger.TLCProxyLogger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PreDestroy;


/**
 * Created by wanghongmeng on 2015/6/16.
 */
@SpringBootApplication
public class TCLProxyServer {

    public static void main(String[] args) {
       SpringApplication.run(TCLProxyServer.class, args);
    }

    @PreDestroy
    public void destory() {
        TLCProxyLogger.getLogger().info("close tlc-proxy java process");
    }
}