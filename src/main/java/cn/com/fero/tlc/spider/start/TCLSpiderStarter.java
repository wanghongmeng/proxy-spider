package cn.com.fero.tlc.spider.start;

import cn.com.fero.tlc.spider.schedule.TLCSpiderScheduler;
import cn.com.fero.tlc.spider.util.TLCSpiderLoggerUtil;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by wanghongmeng on 2015/6/16.
 */
public final class TCLSpiderStarter {

    private static ApplicationContext applicationContext;
    private static TLCSpiderScheduler proxyScheduler;


    public static void main(String[] args) throws InterruptedException {
        try {
            TLCSpiderLoggerUtil.getLogger().info("加载spring配置文件");
            applicationContext = new ClassPathXmlApplicationContext("classpath*: spring-*.xml");

            proxyScheduler = initProxyScheduler();
        } catch (BeansException e) {
            TLCSpiderLoggerUtil.getLogger().error(ExceptionUtils.getFullStackTrace(e));
            if (null != proxyScheduler) {
                proxyScheduler.shutdown();
            }
        }
    }

    private static TLCSpiderScheduler initProxyScheduler() {
        proxyScheduler = (TLCSpiderScheduler) applicationContext.getBean("tlcSpiderProxyScheduler");
        proxyScheduler.init();
        proxyScheduler.loadJobs();
        proxyScheduler.start();
        return proxyScheduler;
    }
}