package cn.com.fero.tlc.proxy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

/**
 * Created by wanghongmeng on 2015/7/21.
 */
@Service
public class TLCProxyMailService {
    @Autowired
    private MailSender mailSender;
    @Autowired
    private SimpleMailMessage templateMessage;
    @Value("${tlc.proxy.mail.subject.error}")
    private String errorSubject;
    @Value("${tlc.proxy.mail.receiver.error}")
    private String[] errorReceiver;

    public void sendErrorMail(String content) {
        SimpleMailMessage smm = new SimpleMailMessage(templateMessage);
        smm.setTo(errorReceiver);
        smm.setSubject(errorSubject);
        smm.setText(content);
        mailSender.send(smm);
    }

    public void sendTextMail(String to, String subject, String content) {
        String[] toArray = new String[]{to};
        sendTextMail(toArray, subject, content);
    }

    public void sendTextMail(String[] to, String subject, String content) {
        SimpleMailMessage smm = new SimpleMailMessage(templateMessage);
        smm.setTo(to);
        smm.setSubject(subject);
        smm.setText(content);
        mailSender.send(smm);
    }
}
