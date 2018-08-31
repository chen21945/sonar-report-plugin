package org.sonarsource.plugins.report.support;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * 发送邮件通知
 */
@Slf4j
public class EmailSupport {

    /**
     * 匿名发送邮件
     *
     * @param host
     * @param fromAddress
     * @param name
     * @param toAddress
     * @param subject
     * @param message
     * @param attachFiles
     * @throws MessagingException
     */
    public static void sendEmailAnonymous(String host, String fromAddress, String name, String[] toAddress, String subject,
                                          String message, String[] attachFiles) throws MessagingException {
        sendEmailWithAttachments(host, 25, null, null, name, fromAddress, toAddress, subject, message, attachFiles);
    }


    /**
     * 发送邮件
     *
     * @param host
     * @param port
     * @param fromAddress
     * @param password
     * @param name
     * @param toAddress
     * @param subject
     * @param message
     * @param attachFiles
     * @throws MessagingException
     */
    public static void sendEmailWithAttachments(String host, Integer port, String username, String password, String name,
                                                String fromAddress, String[] toAddress, String subject, String message, String[] attachFiles) throws MessagingException {
        if (StringUtils.isBlank(host)) {
            throw new IllegalArgumentException("sending email error, host can not be null");
        }
        if (port == null) {
            port = 25;
        }
        if (StringUtils.isBlank(fromAddress)) {
            throw new IllegalArgumentException("sending email error, fromAddress is required");
        }
        if (toAddress == null || toAddress.length == 0) {
            throw new IllegalArgumentException("sending email error, toAddress is required");
        }
        if (StringUtils.isBlank(subject)) {
            subject = "SonarQube analysis report";
            log.info("sending email, subject is null, use default instead");
        }
        if (StringUtils.isEmpty(message) && (attachFiles == null || attachFiles.length == 0)) {
            throw new IllegalArgumentException("sending email error, message content and attachment can not be empty at the same time");
        }

        // creates a new session with an authenticator
        Authenticator auth = null;
        boolean annoymous = false;
        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
            auth = new Authenticator() {
                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            };
        } else {
            annoymous = true;
        }
        log.info("sending email...host={},annoymous={},fromAddress={},toAddress={},subject={},message={},file={}", host, annoymous, fromAddress, toAddress, subject, message, attachFiles);

        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", !annoymous);
        properties.put("mail.smtp.starttls.enable", !annoymous);


        Session session = Session.getInstance(properties, auth);
        // creates a new e-mail message
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(getSenderName(fromAddress, name)));
        InternetAddress[] toAddresses = InternetAddress.parse(Arrays.stream(toAddress).collect(Collectors.joining(",")));
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);
        msg.setSentDate(new Date());

        // creates message part
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(message, "utf-8");

        // creates multi-part
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        // adds attachments
        if (attachFiles != null && attachFiles.length > 0) {
            for (String filePath : attachFiles) {
                MimeBodyPart attachPart = new MimeBodyPart();
                try {
                    attachPart.attachFile(filePath);
                } catch (IOException ex) {
                    log.error("email add attachFile error, filepath={}", filePath, ex);
                }
                multipart.addBodyPart(attachPart);
            }
        }
        // sets the multi-part as e-mail's content
        msg.setContent(multipart);
        // sends the e-mail
        Transport.send(msg);
        log.info("send success!");
    }


    private static String getSenderName(String userName, String nickName) {
        if (nickName != null) {
            try {
                String nick = javax.mail.internet.MimeUtility.encodeText(nickName);
                return nick + " <" + userName + ">";
            } catch (UnsupportedEncodingException e) {
                log.warn("email get sender nick name failed, use username instead");
            }
        }
        return userName;
    }

}
