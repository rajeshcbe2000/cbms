/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * JMSSTPHelper.java
 */
package com.see.truetransact.ui.supporting.standinginstruction.stp;

import java.io.PrintStream;
//import java.util.*;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.HashMap;
import javax.jms.*;
import javax.naming.*;

import com.see.truetransact.commonutil.CommonConstants;

import com.see.health.util.HMConstants;

public class JMSSTPHelper
{

    public JMSSTPHelper()
    {
        CONNFACTORY = "ConnectionFactory";
        entrytimestamp = "";
        exittimestamp = "";
        session = null;
        senderQ = null;
        replyQ = null;
    }

    public QueueSender getQueueSender(String queueConnectionFactoryName, String queueName)
        throws NamingException, JMSException
    {
        QueueConnectionFactory factory = (QueueConnectionFactory)getJndiContext().lookup(queueConnectionFactoryName);
        System.out.println("Queue connection factory lookup successfull");
        QueueConnection connection = factory.createQueueConnection();
        QueueSession session = connection.createQueueSession(false, 1);
        this.session = session;
        System.out.println("Connection and session created successfully");
        Queue queue = (Queue)getJndiContext().lookup(queueName);
        senderQ = queue;
        System.out.println("Queue created");
        QueueSender sender = session.createSender(queue);
        return sender;
    }

    public String getControlQName()
    {
        return "queue/CtrlQ";
    }

    public TopicPublisher getTopicPublisher(String topicConnectionFactoryName, String topicName)
        throws NamingException, JMSException
    {
        Context jndiContext = new InitialContext();
        TopicConnectionFactory factory = (TopicConnectionFactory)jndiContext.lookup(topicConnectionFactoryName);
        Topic topic = (Topic)jndiContext.lookup(topicName);
        TopicConnection connection = factory.createTopicConnection();
        TopicSession session = connection.createTopicSession(false, 1);
        TopicPublisher publisher = session.createPublisher(topic);
        return publisher;
    }

    public QueueReceiver getQueueReceiver(String queueConnectionFactoryName, String queueName)
        throws NamingException, JMSException
    {
        QueueConnectionFactory factory = (QueueConnectionFactory)getJndiContext().lookup(queueConnectionFactoryName);
        QueueConnection connection = factory.createQueueConnection();
        QueueSession session = connection.createQueueSession(false, 1);
        Queue queue = (Queue)getJndiContext().lookup(queueName);
        replyQ = queue;
        QueueReceiver receiver = session.createReceiver(queue);
        connection.start();
        return receiver;
    }

    public TopicSubscriber getTopicSubscriber(String topicConnectionFactoryName, String topicName)
        throws NamingException, JMSException
    {
        Context jndiContext = new InitialContext();
        TopicConnectionFactory factory = (TopicConnectionFactory)jndiContext.lookup(topicConnectionFactoryName);
        Topic topic = (Topic)jndiContext.lookup(topicName);
        TopicConnection connection = factory.createTopicConnection();
        TopicSession session = connection.createTopicSession(false, 1);
        TopicSubscriber subscriber = session.createSubscriber(topic);
        return subscriber;
    }

    public String getQueueConnectionFactory()
    {
        return CONNFACTORY;
    }

    public QueueSession getQSession(String queueConnectionFactoryName)
    {
        QueueSession session = null;
        try
        {
            QueueConnectionFactory factory = (QueueConnectionFactory)getJndiContext().lookup(queueConnectionFactoryName);
            QueueConnection connection = factory.createQueueConnection();
            session = connection.createQueueSession(false, 1);
        }
        catch(NamingException nme)
        {
            nme.printStackTrace();
        }
        catch(JMSException jmse)
        {
            jmse.printStackTrace();
        }
        return session;
    }

    public Queue getQ(String qname)
    {
        Queue queue = null;
        try
        {
            queue = (Queue)getJndiContext().lookup(qname);
        }
        catch(NamingException nm)
        {
            nm.printStackTrace();
        }
        return queue;
    }

    public void sendMessagetoQueue(Message message, String qname)
        throws NamingException, JMSException
    {
        QueueSender sender = getQueueSender(getQueueConnectionFactory(), qname);
        sender.send(message);
        System.out.println("Message sent successfully" + message);
        sender.close();
    }

    public Queue getSenderQ()
    {
        return senderQ;
    }

    public Queue getReplyQ()
    {
        Queue queue = null;
        try
        {
            queue = (Queue)getJndiContext().lookup("replyQ");
        }
        catch(NamingException nm)
        {
            nm.printStackTrace();
        }
        return queue;
    }

    public void sendToInvalidQ(Message invalidmsg)
    {
        try
        {
            QueueSender sender = getQueueSender(getQueueConnectionFactory(), "invalidQ");
            sender.send(invalidmsg);
            System.out.println("Invalid Msg sent to InvalidMsg Channel" + invalidmsg);
        }
        catch(NamingException nm)
        {
            nm.printStackTrace();
        }
        catch(JMSException jmse)
        {
            jmse.printStackTrace();
        }
    }

    public void updateTimeStampInMessage(String timetype)
    {
        if(timetype.equalsIgnoreCase("entry"))
        {
            entrytimestamp = "\t\t<entryTimeStamp>" + (new GregorianCalendar()).getTimeInMillis() + "</entryTimeStamp>\n";
            exittimestamp = "\t\t<exitTimeStamp></exitTimeStamp>\n";
        } else
        {
            exittimestamp = "\t\t<exitTimeStamp>" + (new GregorianCalendar()).getTimeInMillis() + "</exitTimeStamp>\n";
        }
    }

    public String getSTPQName()
    {
        return "queue/SourceReqQ";
    }
    
    public void sendSTPMessage(String xmlString)
        throws JMSException, NamingException
    {
        System.out.println("Got ConnectionFactory");
        TextMessage txtmsg = getQSession(getQueueConnectionFactory()).createTextMessage();
        txtmsg.setText(xmlString);
        
        sendMessagetoQueue(txtmsg, getSTPQName());
    }
    
    public Context getJndiContext()
    {
        Context jndiContext = null;
        try
        {
            Properties JNDI_PROPERTIES = new Properties();
            JNDI_PROPERTIES.put(Context.INITIAL_CONTEXT_FACTORY, CommonConstants.INITIAL_CONTEXT_FACTORY);
            JNDI_PROPERTIES.put(Context.PROVIDER_URL, CommonConstants.PROVIDER_URL);
            JNDI_PROPERTIES.put(Context.URL_PKG_PREFIXES, CommonConstants.URL_PKG_PREFIXES); 

            jndiContext = new InitialContext(JNDI_PROPERTIES);
        }
        catch(NamingException nm)
        {
            nm.printStackTrace();
        }
        return jndiContext;
    }
    
    public void sendTimelyMessage(HashMap properties)
        throws JMSException, NamingException
    {
        System.out.println("Got ConnectionFactory");
        TextMessage txtmsg = getQSession(getQueueConnectionFactory()).createTextMessage();
        String clsFullName = (String)properties.get("cmpdescription");
        
        StringBuffer strBTxtMsg = new StringBuffer();
        strBTxtMsg.append ("<controlInfo>\n\t<messageId>M-");
        strBTxtMsg.append (System.currentTimeMillis());
        strBTxtMsg.append ("</messageId>\n\t<datetime>");
        strBTxtMsg.append ((String) properties.get("datetime"));
        strBTxtMsg.append ("</datetime>\n");
        strBTxtMsg.append (entrytimestamp);
        strBTxtMsg.append (exittimestamp);
        strBTxtMsg.append ("\t<srcComponent>\n");
        strBTxtMsg.append ("\t\t<compId>");
        strBTxtMsg.append (clsFullName.substring(clsFullName.lastIndexOf(".") + 1));
        strBTxtMsg.append ("</compId>\n");
        strBTxtMsg.append ("\t\t<description>");
        strBTxtMsg.append (clsFullName);
        strBTxtMsg.append ("</description>\n");
        strBTxtMsg.append ("\t\t<remark>");
        strBTxtMsg.append ((String) properties.get("remark"));
        strBTxtMsg.append ("</remark>\n");
        strBTxtMsg.append ("\t\t<machineIP>");
        strBTxtMsg.append ((String)properties.get("machineip"));
        strBTxtMsg.append ("</machineIP>\n");
        strBTxtMsg.append ("\t\t<memoryUsage>");
        strBTxtMsg.append (Runtime.getRuntime().totalMemory());
        strBTxtMsg.append ("</memoryUsage>\n");
        strBTxtMsg.append ("\t</srcComponent>\n");
        strBTxtMsg.append ("</controlInfo>\n");
        
        txtmsg.setText(strBTxtMsg.toString());
        
        sendMessagetoQueue(txtmsg, getControlQName());
    }


    public static void main(String args1[]) throws Exception
    {
        JMSSTPHelper stp = new JMSSTPHelper ();
        stp.sendSTPMessage("");
    }

    public String CONNFACTORY;
    private String entrytimestamp;
    private String exittimestamp;
    public QueueSession session;
    public Queue senderQ;
    public Queue replyQ;
}