package utils;

import com.ibm.jms.JMSMessage;
import com.ibm.mq.jms.*;
import com.ibm.msg.client.wmq.WMQConstants;

import javax.jms.JMSException;
import javax.jms.Session;

public class Mqlib {

    public static MQQueueConnection connectToMQ(String mqHost, String mqPort, String mqQueueManager, String mqChannel){
        MQQueueConnection connect = null;
        try {
            MQQueueConnectionFactory cf = new MQQueueConnectionFactory();
            cf.setHostName(mqHost);
            cf.setPort(Integer.parseInt(mqPort));
            //cf.setTransportType(JMSC.MQJMS_TP_CLIENT_MQ_TCPIP);
            cf.setTransportType (WMQConstants.WMQ_CM_CLIENT);
            cf.setQueueManager(mqQueueManager);
            cf.setChannel(mqChannel);
            connect = (MQQueueConnection) cf.createConnection();
        }catch(Exception e){
            e.printStackTrace();
        }
        return connect;
    }

    public static String receiveMessageFromMQ(String queueName, String mqHost, String mqPort, String mqQueueManager
            , String mqChannel) {

        try {
            MQQueueConnectionFactory cf = new MQQueueConnectionFactory();
            cf.setHostName(mqHost);
            cf.setPort(Integer.parseInt(mqPort));
//			cf.setTransportType(JMSC.MQJMS_TP_CLIENT_MQ_TCPIP);
            cf.setTransportType (WMQConstants.WMQ_CM_CLIENT);
            cf.setQueueManager(mqQueueManager);
            cf.setChannel(mqChannel);

            MQQueueConnection connect = (MQQueueConnection) cf.createQueueConnection();
            MQQueueSession session = (MQQueueSession) connect.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            MQQueue queue = (MQQueue) session.createQueue(queueName);

            /*
            try {
                QueueBrowser browser = null;
                browser = session.createBrowser(queue);
                // Чтение сообщений
                Enumeration<?> messages = browser.getEnumeration();

                int count = 0;
                Message message;
                // Просмотр сообщений
                System.out.println("Просмотр JMS сообщений : ");
                while (messages.hasMoreElements()) {
                    message = (Message) messages.nextElement();
                    System.out.println("\nСообщение " + (++count) + " :");
                    System.out.println(message);
                }
                System.out.println("\nПросмотр JMS сообщений завершен\n");
            } catch (JMSException e) {
                e.printStackTrace();
            }
            */

            MQQueueReceiver receiver = (MQQueueReceiver) session.createReceiver(queue);
            connect.start();
            JMSMessage receivedMessage = (JMSMessage) receiver.receive(10000);
            System.out.println("Received message: \n" + receivedMessage);

            byte[] arrayBt = receivedMessage.getBody(byte[].class);
            String messageBody = new String(arrayBt, "UTF-8");
            System.out.println("message  is :\n" + messageBody);

            receiver.close();
            session.close();
            connect.close();
            System.out.println("SUCCESS!!!");
            return messageBody;
        } catch (JMSException jmsex) {
            jmsex.printStackTrace();
            return "Error";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error";
        }
    }
}
