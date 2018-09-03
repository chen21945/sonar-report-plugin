package org.sonarsource.plugins.report.support;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

public class CommonEmailSupport {

    public static void sendEmailWithAttachments(String host, String username, String password, String name,
                                                String fromAddress, String[] toAddress, String subject, String message, String attachFile) throws EmailException {
        // Create the attachment
        EmailAttachment attachment = new EmailAttachment();
        attachment.setPath(attachFile);
        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        attachment.setDescription("SonarQube analysis result");

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

        // add the attachment
        email.attach(attachment);
        // send the email
        email.send();

    }


    public static void sendEmail(String host, String username, String password, String name,
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
                .setMsg(message + "\n No attachments");

        // send the email
        email.send();

    }

}
