package com.example.mq.client.consumer;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 
 * Ref: http://activemq.apache.org/how-should-i-implement-request-response-with-jms.html
 *
 */
public class MsgListener implements MessageListener {

    private static String messageQueueName = "maitai_test";
    private static String messageBrokerUrl = "ssl://10.66.137.175:61617";
 
    private Session session;
    private boolean transacted = false;
    private MessageProducer replyProducer;
 
	public void onMessage(Message message) {
		try {
            if (message instanceof TextMessage) {
                TextMessage txtMsg = (TextMessage) message;
                String messageText = txtMsg.getText();
                System.out.println(messageText);
            }
        } catch (JMSException e) {
            //Handle the exception appropriately
        }
	}
	
	public void setUp() {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(messageBrokerUrl);
        Connection connection;
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            this.session = connection.createSession(this.transacted, Session.AUTO_ACKNOWLEDGE);
            Destination adminQueue = this.session.createQueue(messageQueueName);
 
            //Setup a message producer to respond to messages from clients, we will get the destination
            //to send to from the JMSReplyTo header field from a Message
            this.replyProducer = this.session.createProducer(null);
            this.replyProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
 
            //Set up a consumer to consume messages off of the admin queue
            MessageConsumer consumer = this.session.createConsumer(adminQueue);
            consumer.setMessageListener(this);
        } catch (JMSException e) {
            //Handle the exception appropriately
        }
	}
	public static void main(String[] args){
		System.setProperty("javax.net.ssl.keyStore","/home/wguo/test/activemq/client.ks");
		System.setProperty("javax.net.ssl.keyStorePassword","password");
		System.setProperty("javax.net.ssl.trustStore","/home/wguo/test/activemq/client.ts");
		System.setProperty("javax.net.ssl.trustStorePassword","password");
		MsgListener ml = new MsgListener();
		ml.setUp();
	}

}
