package cn.com.fero.util.proxy.configuration;

import cn.com.fero.util.proxy.common.TLCProxyConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by wanghongmeng on 2015/7/17.
 */
@Configuration
public class TLCProxyConfiguration {
    @Value("${tlc.proxy.mail.config.host}")
    private String mailHost;
    @Value("${tlc.proxy.mail.config.username}")
    private String mailUsername;
    @Value("${tlc.proxy.mail.config.password}")
    private String mailPassword;

    @Bean
    public Queue<String> httpFetchQueue() {
        return new LinkedList();
    }

    @Bean
    public Queue<String> httpsFetchQueue() {
        return new LinkedList();
    }

    @Bean
    public List<String> httpProxy() {
        return new CopyOnWriteArrayList();
    }

    @Bean
    public List<String> httpsProxy() {
        return new CopyOnWriteArrayList();
    }

    @Bean
    public Executor schedulePool() {
        return Executors.newScheduledThreadPool(TLCProxyConstants.SPIDER_CONST_SCHEDULE_POOL_SIZE);
    }

    @Bean
    public Executor threadPool() {
        return Executors.newFixedThreadPool(TLCProxyConstants.SPIDER_CONST_THREAD_POOL_SIZE);
    }

    @Bean
    public Semaphore semaphore() {
        return new Semaphore(TLCProxyConstants.SPIDER_CONST_THREAD_POOL_SIZE);
    }

    @Bean
    public MailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setDefaultEncoding(TLCProxyConstants.SPIDER_CONST_CHARACTER_ENCODING);
        mailSender.setHost(mailHost);
        mailSender.setUsername(mailUsername);
        mailSender.setPassword(mailPassword);
        return mailSender;
    }

    @Bean
    public SimpleMailMessage templateMessage() {
        SimpleMailMessage mailSender = new SimpleMailMessage();
        mailSender.setFrom(mailUsername);
        return mailSender;
    }
}
