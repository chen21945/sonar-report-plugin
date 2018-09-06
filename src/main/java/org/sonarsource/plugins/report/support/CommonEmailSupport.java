package org.sonarsource.plugins.report.support;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;

@Slf4j
public class CommonEmailSupport {

    public static void sendEmailWithAttachments(String host, String username, String password, String name,
                                                String fromAddress, String[] toAddress, String subject, String message, String attachFile) throws EmailException {
        // Create the email message
        MultiPartEmail email = new MultiPartEmail();
        email.setHostName(host);
        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
            email.setAuthentication(username, password);
            email.setStartTLSEnabled(true);
        }
        email.setFrom(fromAddress, name)
                .addTo(toAddress)
                .setSubject(subject)
                .setMsg(message);
        email.setSocketConnectionTimeout(30000);
        email.setSocketTimeout(30000);
        email.setCharset("utf-8");

        // add the attachment
        if (StringUtils.isNotEmpty(attachFile)) {
            EmailAttachment attachment = new EmailAttachment();
            attachment.setPath(attachFile);
            attachment.setDisposition(EmailAttachment.ATTACHMENT);
            attachment.setDescription("SonarQube analysis result");
            email.attach(attachment);
        }
        // send the email
        email.send();
    }


    public static void sendSimpleEmail(String host, String username, String password, String name,
                                       String fromAddress, String[] toAddress, String subject, String message) throws EmailException {
        // Create the email message
        SimpleEmail email = new SimpleEmail();
        email.setHostName(host);
        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
            email.setAuthentication(username, password);
            email.setStartTLSEnabled(true);
        }
        email.setFrom(fromAddress, name)
                .addTo(toAddress)
                .setSubject(subject)
                .setMsg(message);
        email.setSocketConnectionTimeout(30000);
        email.setSocketTimeout(30000);
        email.setCharset("utf-8");
        // send the email
        email.send();
    }


}
