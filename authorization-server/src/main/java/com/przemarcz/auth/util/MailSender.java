package com.przemarcz.auth.util;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.internet.InternetAddress;
import java.util.ArrayList;
import java.util.List;

@Component
public class MailSender {

    @Value("${email.user:user}")
    private String user;
    @Value("${email.user-name:user-name}")
    private String userName;
    @Value("${email.password:password}")
    private String password;
    @Value("${email.email:email@test.pl}")
    private String email;
    @Value("${email.host:host}")
    private String host;
    @Value("${email.smtp-port:123}")
    private Integer smtpPort;
    @Value("${email.ssl-smtp:123}")
    private String sslSmtp;
    @Value("${email.subject:subject}")
    private String subject;
    @Value("${email.content:content}")
    private String content;
    @Value("${email.charset:UTF-8}")
    private String charset;

    @Async
    public void sendEmail(String userEmail, String activationKey, String login) throws EmailException {
        List<InternetAddress> address = prepareAddress(userEmail);
        HtmlEmail htmlEmail = prepareConnection();
        setContent(activationKey, address, htmlEmail, login);
        htmlEmail.send();
    }

    private List<InternetAddress> prepareAddress(String userEmail) {
        List<InternetAddress> address = new ArrayList<>();
        InternetAddress ia = new InternetAddress();
        ia.setAddress(userEmail);
        address.add(ia);
        return address;
    }

    private HtmlEmail prepareConnection() {
        HtmlEmail htmlEmail = new HtmlEmail();
        htmlEmail.setAuthentication(user, password);
        htmlEmail.setCharset(charset);
        htmlEmail.setHostName(host);
        htmlEmail.setSmtpPort(smtpPort);
        htmlEmail.setSslSmtpPort(sslSmtp);
        htmlEmail.setStartTLSRequired(true);
        return htmlEmail;
    }

    private void setContent(String activationKey, List<InternetAddress> address, HtmlEmail htmlEmail, String login) throws EmailException {
        htmlEmail.setHtmlMsg(String.format(content, login, activationKey, login, activationKey));
        htmlEmail.setSubject(subject);
        htmlEmail.setFrom(email, userName);
        htmlEmail.setTo(address);
    }

}
