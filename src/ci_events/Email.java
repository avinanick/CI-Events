/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ci_events;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

/**
 *
 * @author avina
 */
public class Email {

    private String message;
    private String[] recipient;
    private Message msg;
    private static final String FROM_EMAIL = "venusclassicgroup@gmail.com"; 
    private static final String PASS = "venusGroup1234";
    
    private class SMTPAuthenticator extends Authenticator
    {
        public PasswordAuthentication getPasswordAuthentication()
        {
            return new PasswordAuthentication(FROM_EMAIL, PASS);
        }
    }

    public Email() {
        //TODO implement constructor
        Properties props = new Properties();
        props.put("mail.smtp.user", FROM_EMAIL);
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.debug", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", "587");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        SMTPAuthenticator auth = new SMTPAuthenticator();
        Session session = Session.getDefaultInstance(props,auth);
        session.setDebug(false);
        msg = new MimeMessage(session);
        try {
            msg.setFrom(new InternetAddress(FROM_EMAIL));
        } catch (AddressException e) {
            // Handle exception
           System.out.println("Invalid address entered " + e.getMessage());
        } catch (MessagingException e) {
            // Handle exception
            System.out.println("Messaging error " + e.getMessage());
        }
    }

    public void sendEmail() {
        try {
            Transport.send(msg);
        } catch (MessagingException e) {
            // Handle exception
            System.out.println("Messaging error " + e.getMessage());

        }
    }

    public void addRecipients(String... recipients) {
        try {
            for(int i = 0; i < recipients.length; i++) {
                msg.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients[i]));
            }
        } catch (AddressException e) {
            // Handle exception
            System.out.println("Invalid address entered " + e.getMessage());
        } catch (MessagingException e) {
            // Handle exception
            System.out.println("Messaging error " + e.getMessage());
        }
    }

    public void setMessage(String message) {
        try {
            msg.setText(message);
        } catch (AddressException e) {
            // Handle exception
            System.out.println("Invalid address entered " + e.getMessage());
        } catch (MessagingException e) {
            // Handle exception
            System.out.println("Messaging error " + e.getMessage());
        }
    }
    
    public void setTitle(String title) {
        try {
            this.msg.setSubject(title);
        }
        catch (MessagingException e) {
            System.out.println("Messaging error " + e.getMessage());
        }
        
    }
}
