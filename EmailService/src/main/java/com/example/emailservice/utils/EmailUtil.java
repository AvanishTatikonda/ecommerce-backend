package com.example.emailservice.utils;

import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Date;

public class EmailUtil {

    public static void sendEmail(
            Session session,
            String toEmail,
            String subject,
            String body) {

        try {
            MimeMessage msg = new MimeMessage(session);

            msg.addHeader("Content-type", "text/html; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress(
                    "tatikondaavanish72@gmail.com",
                    "Shopzen"
            ));

            msg.setReplyTo(InternetAddress.parse(
                    "tatikondaavanish72@gmail.com", false
            ));

            msg.setSubject(subject, "UTF-8");
            msg.setText(body, "UTF-8");
            msg.setSentDate(new Date());

            msg.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail, false)
            );

            System.out.println("Message is ready");

            Transport.send(msg);

            System.out.println("Email sent successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}