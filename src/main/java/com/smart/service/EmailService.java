package com.smart.service;

import java.util.Properties;

import org.springframework.stereotype.Service;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
	public boolean sendEmail(String subject, String message, String to) {
		// rest of the code...

		boolean f=false;
		
		String from ="your_email@gmail.com";
		//
		
		
		String host = "smtp.gmail.com";

		// get the system properties

		Properties properties = System.getProperties();
		System.out.println("properties " + properties);

		// setting important information to properties object

		// host set
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");

		// step 1: get the session object

		Session session = Session.getInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication("your_email@gmail.com","your_secure_code");
			}

		});
		session.setDebug(true);
		// step 2: compose the message{text,multimedia}
		MimeMessage mimeMessage = new MimeMessage(session);
		try {
			mimeMessage.setFrom(from);

			// adding recipient to message
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// adding subject to message
			mimeMessage.setSubject(subject);

			// adding message text
			//mimeMessage.setText(message);
			mimeMessage.setContent(message,"text/html");

			// send

			// step 3: send the message from transport class

			Transport.send(mimeMessage);
			System.out.println("send message");
			f=true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return f;

	}

}
