package com.example.emailservice.consumers;

import com.example.emailservice.DTO.EmailDto;
import com.example.emailservice.utils.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;


@Component
public class SendEmailConsumer {
    @Autowired
   private ObjectMapper objectMapper;

    @KafkaListener(topics = "user_signedin",groupId = "emailService")
    public void sendEmail(String message){
        try{
            EmailDto emailDto=objectMapper.readValue(message,EmailDto.class);
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
            props.put("mail.smtp.port", "587"); //TLS Port
            props.put("mail.smtp.auth", "true"); //enable authentication
            props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

            //create Authenticator object to pass in Session.getInstance argument
            Authenticator auth = new Authenticator() {
                //override the getPasswordAuthentication method
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(emailDto.getFrom(),"aaxosoeykenvklol");
                }
            };
            Session session = Session.getInstance(props, auth);

            EmailUtil.sendEmail(session,emailDto.getTo() ,emailDto.getSubject(),emailDto.getBody());
        }
        catch (JacksonException exception){
            throw new RuntimeException(exception);
        }
    }
}

