package dev.hkb.ananta.security.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Async
    public void sendMailToNewUser(String email, String password){
        try{
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(email);
            mail.setSubject("Account Created");
            String emailBody = "Welcome to Ananta!\n\n" +
                    "Your account has been created successfully.\n" +
                    "Username: " + email + "\n" +
                    "Password: " + password + "\n\n" +
                    "Please change your password after logging in for security.";
            mail.setText(emailBody);
            javaMailSender.send(mail);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
