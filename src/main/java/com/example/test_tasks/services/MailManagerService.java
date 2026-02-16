package com.example.test_tasks.services;

import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Slf4j
public class MailManagerService {

    JavaMailSender sender;

    @SneakyThrows
    public boolean hasMxRecord(String email) {
        String domain = email.substring(email.indexOf("@") + 1);

        Hashtable<String, String> env = new Hashtable<>();
        env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
        env.put("com.sun.jndi.dns.timeout.initial", "2000");
        env.put("com.sun.jndi.dns.timeout.retries", "1");

        DirContext ctx = new InitialDirContext(env);
        Attributes attrs = ctx.getAttributes(domain, new String[]{"MX"});
        Attribute mx = attrs.get("MX");

        return (mx != null && mx.size() > 0);
    }

    public void sendMessage(String email){

    }

    @Async
    @SneakyThrows
    public void sendVerificationEmail(String email,String code){

        MimeMessage message = sender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");

        helper.setFrom("no-reply@bib.com");

        helper.setTo(email);

        helper.setText("This is email verification email. Press on the link below to    " +
                "complete email verification procedure\n\n\n"+
                "http://localhost:8080/home/verify/"+code+"\n\n\n"+
                "*NOTE: If you never send this verification email, just ignore it." +
                " It will automatically disappear after some time");

        sender.send(message);


    }



}
