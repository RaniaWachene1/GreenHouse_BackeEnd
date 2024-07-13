package com.greenhouse.gh_backend.services;

import com.greenhouse.gh_backend.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleEmail(String toEmail,
                                String subject,
                                String body
    ) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("raniawachen21@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);
        mailSender.send(message);
        System.out.println("Mail Send...");


    }
    public void sendPasswordResetEmail(User user) {
        // Construct the email body with the password reset instructions
        String emailBody = "Dear " + user.getFirstName() + ",\n\n"
                + "You have requested to reset your password. "
                + "Please use the following link to reset your password:\n"
                + "http://localhost:4200/resetPassword?token=" + user.getResetPasswordToken() + "\n\n"
                + "If you did not request this, please ignore this email.\n\n"
                + "Regards,\n"
                + "EspritCollab";

        // Send the email
        sendSimpleEmail(user.getEmail(), "Password Reset Instructions", emailBody);
    }
    public void sendVerificationEmail(User user, String verificationToken) {
        String emailBody = "Dear " + user.getFirstName() + ",\n\n"
                + "Thank you for registering with EspritCollab. "
                + "Please click the following link to verify your email address:\n\n"
                + "http://localhost:4200/login?token=" + verificationToken + "\n\n"
                + "If you did not register with us, please ignore this email.\n\n"
                + "Regards,\n"
                + "EspritCollab Team";

        sendSimpleEmail(user.getEmail(), "Email Verification", emailBody);
    }

}
