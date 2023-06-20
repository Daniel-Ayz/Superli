package MailVerification;
import HumanResources.ServiceLayer.Response;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import javax.activation.DataHandler;


public class JavaMailUtil {
    public static Response<String> sendMail(String recipient, String generatedPassword) {
//        System.out.println("Preparing to send email");

        // configure properties for mail
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");

        String myAccountEmail = "Super.Li.Original@gmail.com";
        String password = "muhlzzwgqnakptsz\n";

        // create session
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccountEmail,password);
            }
        });

        // create message
        Message message = prepareMessage(session, myAccountEmail, recipient, generatedPassword);

        // send message
        try {
            javax.mail.Transport.send(message);
            return new Response<>("mail sent successfully", true);
        } catch (Exception e) {
            return new Response<>(e.getMessage(), false);
        }
    }

    private static Message prepareMessage(Session session, String myAccountEmail, String recipient, String generatedPassword) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO,new InternetAddress(recipient));
            message.setSubject("authentication to SuperLi");
            message.setText("The password: " + generatedPassword);
            return message;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
