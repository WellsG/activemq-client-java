package com.example.mq.client.producer;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSslConnectionFactory;

public class MsgSSLProducer {

	public static void main(String[] args) {
		try {
			System.setProperty("javax.net.ssl.keyStore","/home/wguo/test/activemq/client.ks");
			System.setProperty("javax.net.ssl.keyStorePassword","password");
			System.setProperty("javax.net.ssl.trustStore","/home/wguo/test/activemq/client.ts");
			System.setProperty("javax.net.ssl.trustStorePassword","password");
			//System.setProperty("javax.net.debug","ssl");
            // Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQSslConnectionFactory("ssl://10.66.137.175:61617");

            // Create a Connection
            Connection connection = connectionFactory.createConnection();
            connection.start();

            // Create a Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue("maitai_test");

            // Create a MessageProducer from the Session to the Topic or Queue
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            // Create a messages
            String text = "Hello world ssl!";
            TextMessage message = session.createTextMessage(text);

            // Tell the producer to send the message
            System.out.println("Sent message: "+ message.hashCode() + " : " + Thread.currentThread().getName());
            producer.setTimeToLive(30000);
            producer.send(message);

            // Clean up
            session.close();
            connection.close();
        }
        catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
	}

}
