package com.example.mq.client.consumer;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSslConnectionFactory;

public class MsgSSLConsumer {

	public static void main(String[] args){
		try {
			System.setProperty("javax.net.ssl.keyStore","/home/wguo/test/activemq/client.ks");
			System.setProperty("javax.net.ssl.keyStorePassword","password");
			System.setProperty("javax.net.ssl.trustStore","/home/wguo/test/activemq/client.ts");
			System.setProperty("javax.net.ssl.trustStorePassword","password");
            // Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQSslConnectionFactory("ssl://10.66.137.175:61617");

            // Create a Connection
            Connection connection = connectionFactory.createConnection();
            connection.start();

            //connection.setExceptionListener(this);

            // Create a Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue("maitai_test");

            // Create a MessageConsumer from the Session to the Topic or Queue
            MessageConsumer consumer = session.createConsumer(destination);

            // Wait for a message
            Message message = consumer.receive(1000);

            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String text = textMessage.getText();
                System.out.println("Received: " + text);
            } else {
                System.out.println("Received: " + message);
            }

            consumer.close();
            session.close();
            connection.close();
        } catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
	}
}
