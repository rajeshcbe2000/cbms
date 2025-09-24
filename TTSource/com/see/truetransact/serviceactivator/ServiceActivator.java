/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * ServiceActivator.java
 *
 * Created on July 9, 2003, 3:39 PM
 */

package com.see.truetransact.serviceactivator;

import javax.ejb.*;
import javax.naming.*;
import javax.jms.*;

/**
 * The ServiceActivator class is a message-driven bean.  It implements
 * the javax.ejb.MessageDrivenBean and javax.jms.MessageListener
 * interfaces. It is defined as public (but not final or
 * abstract).  It defines a constructor and the methods
 * setMessageDrivenContext, ejbCreate, onMessage, and
 * ejbRemove.
 */
public class ServiceActivator implements MessageDrivenBean, MessageListener {

    private transient MessageDrivenContext mdc = null;
    private Context context;
    
    /**
     * Constructor, which is public and takes no arguments.
     */
    public ServiceActivator() {
    }
    
    /**
     * setMessageDrivenContext method, declared as public (but
     * not final or static), with a return type of void, and
     * with one argument of type javax.ejb.MessageDrivenContext.
     *
     * @param mdc    the context to set
     */
    public void setMessageDrivenContext(MessageDrivenContext mdc) {
        this.mdc = mdc;
    }
    
    /**
     * ejbCreate method, declared as public (but not final or
     * static), with a return type of void, and with no
     * arguments.
     */
    public void ejbCreate() {
    }
    
    /**
     * onMessage method, declared as public (but not final or
     * static), with a return type of void, and with one argument
     * of type javax.jms.Message.
     *
     * Casts the incoming Message to a TextMessage and displays
     * the text.
     *
     * @param inMessage    the incoming message
     */
    public void onMessage(Message inMessage) {
        TextMessage msg = null;
        
        try {
            if (inMessage instanceof TextMessage) {
                msg = (TextMessage) inMessage;
                System.out.println("MESSAGE BEAN: Message received: " + msg.getText());
            } else {
                System.out.println("Message of wrong type: " +
                inMessage.getClass().getName());
            }
        } catch (JMSException e) {
            System.err.println("MessageBean.onMessage: JMSException: " + e.toString());
            mdc.setRollbackOnly();
        } catch (Throwable te) {
            System.err.println("MessageBean.onMessage: Exception: " + te.toString());
        }
    }
    
    /**
     * ejbRemove method, declared as public (but not final or
     * static), with a return type of void, and with no
     * arguments.
     */
    public void ejbRemove() {
    }
}
