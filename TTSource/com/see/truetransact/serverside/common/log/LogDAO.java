/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * LogDAO.java
 *
 * Created on April 2, 2004, 11:00 AM
 */
package com.see.truetransact.serverside.common.log;

//import java.util.List;
//import java.util.Properties;
//
//import javax.naming.InitialContext;
//import javax.naming.NamingException;
//
//import javax.jms.Queue;
//import javax.jms.JMSException;
//import javax.jms.QueueConnection;
//import javax.jms.QueueConnectionFactory;
//import javax.jms.QueueSender;
//import javax.jms.Session;
//import javax.jms.QueueSession;
//import javax.jms.TextMessage;
//import javax.jms.ObjectMessage;
//
//import org.jboss.mq.il.oil.OILServerILFactory;
//import org.jboss.mq.SpyConnectionFactory;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.transferobject.common.log.LogTO;
//import com.see.truetransact.commonutil.CommonUtil;

/**
 *
 * @author bala
 */
public class LogDAO {

    LogMDBClient client;
    private SqlMap sqlMap;

    /**
     * Creates a new instance of GLUpdateDAO
     */
    public LogDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public void addToLog(LogTO objLog) throws Exception {
        objLog.setApplDt(ServerUtil.getCurrentDate(objLog.getBranchId()));
        sqlMap.executeUpdate("insertLog", objLog);
    }

    /**
     * Creates a new instance of LogDAO public LogDAO() throws Exception {
     *
     * //client = LogMDBClient.getInstance(); }
     *
     * public void addToLog(LogTO objLog) throws Exception {
     * sqlMap.executeUpdate("insertLog", objLog); //client.process(objLog);
    }
     */
    public static void main(String str[]) throws Exception {
        LogDAO objLog = new LogDAO();

        LogTO objLogTO;
        for (int i = 0; i < 10; i++) {
            objLogTO = new LogTO();
            objLogTO.setData("ABC");
            objLogTO.setBranchId("123" + i);

            objLog.addToLog(objLogTO);
        }
    }
}