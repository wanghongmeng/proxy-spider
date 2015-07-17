package cn.com.fero.tlc.proxy;

import cn.com.fero.tlc.proxy.util.TLCProxyLoggerUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PreDestroy;


/**
 * Created by wanghongmeng on 2015/6/16.
 */
@SpringBootApplication
public class TCLProxyServer {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(TCLProxyServer.class);
        app.setWebEnvironment(true);
        app.setShowBanner(false);
        app.run(args);
    }

    @PreDestroy
    public void destory() {
        TLCProxyLoggerUtil.getLogger().info("close tlc-proxy java process");
    }
}