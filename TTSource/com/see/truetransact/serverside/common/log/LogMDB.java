/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * LogMDB.java
 */
package com.see.truetransact.serverside.common.log;

import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.ejb.CreateException;

import javax.jms.MessageListener;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.jms.ObjectMessage;



import com.ibatis.db.sqlmap.SqlMap;

import com.see.truetransact.transferobject.common.log.LogTO;

import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;

public class LogMDB implements MessageDrivenBean, MessageListener {

    private transient MessageDrivenContext mdc = null;
    private transient SqlMap sqlMap;

    public void ejbCreate() {
        try {
            ServiceLocator locate = ServiceLocator.getInstance();
            sqlMap = (SqlMap) locate.getDAOSqlMap();
        } catch (ServiceLocatorException sle) {
        }
    }

    public void ejbRemove() {
    }

    public void setMessageDrivenContext(MessageDrivenContext messageDrivenContext) throws javax.ejb.EJBException {
        this.mdc = messageDrivenContext;
    }

    public void onMessage(javax.jms.Message inMessage) {
        TextMessage msg = null;

        try {
            if (inMessage instanceof TextMessage) {
                msg = (TextMessage) inMessage;
                System.out.println("MESSAGE BEAN: Message received: " + msg.getText());
            } else if (inMessage instanceof ObjectMessage) {
                LogTO objLog = (LogTO) ((ObjectMessage) inMessage).getObject();

                sqlMap.startTransaction();
                sqlMap.executeUpdate("insertLog", objLog);
                sqlMap.commitTransaction();

                System.out.println("Message type: " + inMessage.getClass().getName());
                System.out.println("Message Data : " + objLog.toString());
            } else {
                System.out.println("Message of wrong type: " + inMessage.getClass().getName());
            }
        } catch (java.sql.SQLException sqle) {
            try {
                System.err.println("MessageBean.onMessage: Exception: " + sqle.toString());
                sqlMap.rollbackTransaction();
            } catch (Exception exc) {
            }
        } catch (JMSException jmse) {
            System.err.println("MessageBean.onMessage: JMSException: " + jmse.toString());
            mdc.setRollbackOnly();
        } catch (Throwable te) {
            System.err.println("MessageBean.onMessage: Exception: " + te.toString());
        }
    }
}
