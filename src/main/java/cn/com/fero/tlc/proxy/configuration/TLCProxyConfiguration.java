package cn.com.fero.tlc.proxy.configuration;

import cn.com.fero.tlc.proxy.common.TLCProxyConstants;
import cn.com.fero.tlc.proxy.fetcher.TLCProxyIpFetcher;
import cn.com.fero.tlc.proxy.fetcher.impl.TLCProxyDL5566IpFetcher;
import cn.com.fero.tlc.proxy.fetcher.impl.TLCProxyKDLIpFetcher;
import cn.com.fero.tlc.proxy.fetcher.impl.TLCProxyXCNNIpFetcher;
import cn.com.fero.tlc.proxy.fetcher.impl.TLCProxyXCNTIpFetcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    public TLCProxyIpFetcher dl5566IpFetcher() {
        return new TLCProxyDL5566IpFetcher();
    }

    @Bean
    public TLCProxyIpFetcher kdlIpFetcher() {
        return new TLCProxyKDLIpFetcher();
    }

    @Bean
    public TLCProxyIpFetcher xcnnIpFetcher() {
        return new TLCProxyXCNNIpFetcher();
    }

    @Bean
    public TLCProxyIpFetcher xcntIpFetcher() {
        return new TLCProxyXCNTIpFetcher();
    }

    @Bean
    public List<TLCProxyIpFetcher> fetchers() {
        List<TLCProxyIpFetcher> fetchList = new ArrayList<>();
        fetchList.add(dl5566IpFetcher());
        fetchList.add(kdlIpFetcher());
        fetchList.add(xcnnIpFetcher());
        fetchList.add(xcntIpFetcher());
        return fetchList;
    }

    @Bean
    public Queue<String> httpFetchQueue() {
        return new LinkedList();
    }

    @Bean
    public Queue<String> httpsFetchQueue() {
        return new LinkedList();
    }

    @Bean
    public Queue<String> httpProxy() {
        return new LinkedList();
    }

    @Bean
    public Queue<String> httpsProxy() {
        return new LinkedList();
    }

    @Bean
    public ExecutorService threadPool() {
        return Executors.newFixedThreadPool(TLCProxyConstants.SPIDER_CONST_THREAD_SIZE);
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
