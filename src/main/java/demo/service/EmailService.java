package demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService{


    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String fromEmail;
    public void sendEmail(String toEmail,String subject ,String body){
        SimpleMailMessage mail=new SimpleMailMessage();
        mail.setTo(toEmail);
        mail.setSubject(subject);
        mail.setText(body);

        mail.setFrom(fromEmail);
        javaMailSender.send(mail);

    }

}
