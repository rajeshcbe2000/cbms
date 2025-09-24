/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * LogMDBClient.java
 *
 * Created on April 2, 2004, 11:00 AM
 */
package com.see.truetransact.serverside.common.log;

import java.util.List;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.jms.Queue;
import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.Session;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.jms.ObjectMessage;

import org.jboss.mq.il.oil.OILServerILFactory;
import org.jboss.mq.SpyConnectionFactory;

import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.commonutil.CommonUtil;

/**
 *
 * @author bala
 */
public class LogMDBClient {

    InitialContext jndiContext = null;
    QueueConnectionFactory connectionFactory = null;
    QueueConnection connection = null;
    QueueSession session = null;
    Queue dest = null;
    QueueSender producer = null;
    ObjectMessage objMessage = null;

    /**
     * Creates a new instance of GLUpdateDAO
     */
    private LogMDBClient() throws Exception {
        jndiContext = (InitialContext) CommonUtil.getInitialContext();

        try {
            Properties props = new Properties();
            props.setProperty(OILServerILFactory.SERVER_IL_FACTORY_KEY, OILServerILFactory.SERVER_IL_FACTORY);
            props.setProperty(OILServerILFactory.CLIENT_IL_SERVICE_KEY, OILServerILFactory.CLIENT_IL_SERVICE);
            props.setProperty(OILServerILFactory.PING_PERIOD_KEY, "1000");
            props.setProperty(OILServerILFactory.OIL_ADDRESS_KEY, "localhost");
            props.setProperty(OILServerILFactory.OIL_PORT_KEY, "8090");

            connectionFactory = new SpyConnectionFactory(props);
            dest = (Queue) jndiContext.lookup("queue/testQueue");

            connection = connectionFactory.createQueueConnection();
            session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            producer = session.createSender(dest);
            objMessage = session.createObjectMessage();

        } catch (Exception e) {
            System.out.println("Failed: " + e.toString());
        }
    }
    private static LogMDBClient logClient;

    static {
        try {
            logClient = new LogMDBClient();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    public static LogMDBClient getInstance() throws Exception {
        return logClient;
    }

    public void process(LogTO objLog) throws Exception {
        try {
            objMessage.setObject(objLog);
            System.out.println("Sending message: " + objLog.toString());
            producer.send(objMessage);
        } catch (JMSException e) {
            System.out.println("Exception occurred: "
                    + e.toString());
        }
    }
}