package org.isf.service;

import org.isf.dao.User;
import org.isf.dao.Visit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.UUID;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVisitReport(Visit visit, File report) {
        try {
            String subject = "Visit Report";
            String message = "Hello " + visit.getPatient().getName() + ", here's your last visit report." ;

            MimeMessage mail = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mail, true);

            helper.setFrom("medjacket2020@gmail.com");
            helper.setTo(visit.getPatient().getEmail());
            helper.setSubject(subject);
            helper.setText(message);

            FileSystemResource file
                    = new FileSystemResource(report);
            helper.addAttachment("Report.pdf", file);

            mailSender.send(mail);
        } catch (Exception e) {
            return;
        }

    }
}
