/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * EJBClientProxy.java
 *
 * Created on June 23, 2003, 12:23 PM
 */
package com.see.truetransact.clientproxy;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.rmi.PortableRemoteObject;

import org.apache.log4j.Logger;

import com.see.truetransact.businessdelegate.ejb.BusinessDelegateRemote;
import com.see.truetransact.businessdelegate.ejb.BusinessDelegateHome;

import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import java.io.*;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;


//import com.see.health.jms.client.JMSHelper;

/** This class is an implementation of the Service Locator pattern. It is
 * used to looukup resources.
 *
 * This implementation uses the "singleton" strategy and also the "caching"
 * strategy.
 *
 * This implementation is intended to be used on the web tier and
 * not on the ejb tier.
 *
 * @author Balachandar
 */
public class EJBClientProxy extends ProxyFactory {
//    private JMSHelper jmsHelper;
    
    private final static Logger log = Logger.getLogger(ProxyFactory.class);
//    private final String jndiHomeName = "BusinessDelegateJNDI";   // JNDI lookup string
    private final String jndiHomeName = "BusinessDelegate";   // JNDI lookup string for JBoss 7.1.1
    
    private Context ic;          // Context object
    private Map cache;                  //used to hold references to Resources for re-use
    
    private static EJBClientProxy me;   // singleton object
    
    static {
        try {
            log.info ("Creating EJB Client Proxy...");
            me = new EJBClientProxy();
        } catch(ClientProxyException se) {
            log.error (se);
        } catch(Exception e) {
            log.error (e);
        }
    }
    
    /**
     * Creates a private constructor
     *
     * @exception  Exception
     */
    private EJBClientProxy() throws Exception {
        // Initial Context Factory Properties
//        ic = CommonUtil.getInitialContext();

        Properties prop = new Properties();
        prop.put(Context.INITIAL_CONTEXT_FACTORY, CommonConstants.INITIAL_CONTEXT_FACTORY);
        prop.put(Context.PROVIDER_URL, CommonConstants.PROVIDER_URL);
        prop.put(Context.SECURITY_PRINCIPAL, CommonConstants.SECURITY_PRINCIPAL);
        prop.put(Context.SECURITY_CREDENTIALS, CommonConstants.SECURITY_CREDENTIALS);
//        prop.put(Context.URL_PKG_PREFIXES, CommonConstants.URL_PKG_PREFIXES);
//        Properties properties = new Properties();
//        properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
//        properties.put(Context.PROVIDER_URL, "remote://localhost:4447");
//        properties.put(Context.SECURITY_PRINCIPAL, "rajesh");
//        properties.put(Context.SECURITY_CREDENTIALS, "manager");
//        System.out.println("#$%%#$ EJBClientProxy INITIAL_CONTEXT_FACTORY:"+CommonConstants.INITIAL_CONTEXT_FACTORY);
//        System.out.println("#$%%#$ EJBClientProxy PROVIDER_URL:"+CommonConstants.PROVIDER_URL);
//        System.out.println("#$%%#$ EJBClientProxy SECURITY_PRINCIPAL:"+CommonConstants.SECURITY_PRINCIPAL);
//        System.out.println("#$%%#$ EJBClientProxy SECURITY_CREDENTIALS:"+CommonConstants.SECURITY_CREDENTIALS);
        ic = new InitialContext(prop);
//        ConnectionFactory factory = (ConnectionFactory) context.lookup("jms/RemoteConnectionFactory");
//        ic = new InitialContext(prop);

//        weblogic.jndi.Environment env = new weblogic.jndi.Environment();
//        env.setProviderUrl(CommonConstants.PROVIDER_URL);
//        env.setInitialContextFactory(CommonConstants.INITIAL_CONTEXT_FACTORY);
//        ic = env.getInitialContext();
        
        // creating a synchronize Map
        cache = Collections.synchronizedMap(new HashMap());
        
//        jmsHelper = new JMSHelper();
    }
    
    /**
     * Returns an instance of EJBClientProxy.
     *
     * @return              EJBClientProxy
     */
    static public EJBClientProxy getInstance() {
        return me;
    }
    
    /** It will get the ejb Remote home factory. If this ejb home factory has already been
     * clients need to cast to the type of EJBHome they desire
     *
     * @return the EJB Home corresponding to the homeName
     * @throws Exception Throws Exception if the Remote Interface gives error
     */
    public BusinessDelegateRemote getRemoteInterface() throws Exception {
        BusinessDelegateHome home = null;
        BusinessDelegateRemote remoteInter = null;

        if (cache.containsKey(jndiHomeName)) {
            home =(BusinessDelegateHome) cache.get(jndiHomeName);
        } else {
            Object objref = ic.lookup("arcat/TTServer/"+jndiHomeName+"!com.see.truetransact.businessdelegate.ejb.BusinessDelegateHome");
            Object obj = PortableRemoteObject.narrow(objref, BusinessDelegateHome.class);
            home = (BusinessDelegateHome) obj;
            cache.put(jndiHomeName, home);
        }
        remoteInter = (BusinessDelegateRemote) PortableRemoteObject.narrow 
              (home.create(), BusinessDelegateRemote.class);
        return remoteInter;
    }

    /**
     * This is mainly for insert, update & delete existing data from the Database.
     * Calling Business Delegate and doing the process.
     *
     * @param       obj             HashMap (Bean object)
     * @param       param           HashMap 
     * @param       addInfoFlag     boolean
     * @exception   Exception       if an invalid lookup.
     */
    public HashMap execute(HashMap obj, HashMap param, boolean addInfoFlag) throws Exception {
//        jmsHelper.updateTimeStampInMessage("entry");
        HashMap resultMap = null;        

        try {
            if (addInfoFlag) {
                addInfoToMap(obj);
            }
//            HashMap data = new HashMap();
//            data.put("DATA",toCompressedBytes(obj));
            resultMap = getRemoteInterface().execute(obj, param);
//            if (resultMap!=null && resultMap.get("DATA")!=null) {
//                resultMap = (HashMap) fromCompressedBytes((byte[])resultMap.get("DATA"));
//            } else {
//                resultMap = null;
//            }
//            data.clear();
//            data = null;
        } catch (javax.naming.CommunicationException exc) {
//            com.see.truetransact.clientutil.ClientUtil.exceptionAlert(exc);
        } catch (java.rmi.ConnectException cexc) {
//            com.see.truetransact.clientutil.ClientUtil.exceptionAlert(cexc);
        } catch (Exception e) {
            throw e;
        }
        return resultMap;
//        jmsHelper.updateTimeStampInMessage("exit");
//        HashMap msgProps = new HashMap();
//        msgProps.put("cmpdescription",this.getClass().getName());
//        msgProps.put("machineip",obj.get("IP_ADDR"));
//        msgProps.put("remark","EJB Proxy Time Taken");
//        msgProps.put("datetime",new java.util.Date().toString());
//        jmsHelper.sendTimelyMessage(msgProps);
    }
    
    /**
     * This is mainly for insert, update & delete existing data from the Database.
     * Calling Business Delegate and doing the process.
     *
     * @param       obj     HashMap (Bean object)
     * @param       param   HashMap 
     * @exception  Exception  if an invalid lookup.
     */
    public HashMap execute(HashMap obj, HashMap param) throws Exception {
        return execute(obj, param, true);
    }
    
    /** Returns an int based executeQuery operation success or failure state.
     * This is mainly for deleting existing data from the Database.
     * Calling Business Delegate and doing the process.
     *
     * @param   obj             HashMap (Bean objects)
     * @param   param           HashMap (Session Bean Parameters)
     * @param   addInfoFlag     boolean
     * @return  HashMap         Returns Database data
     * @exception Exception if an invalid lookup.
     */
    public HashMap executeQuery (HashMap obj, HashMap param, boolean addInfoFlag) throws Exception {
        HashMap resultMap = null;
        
//        jmsHelper.updateTimeStampInMessage("entry");
        try {
            if (addInfoFlag) {
                addInfoToMap(obj);
            }
            resultMap = getRemoteInterface().executeQuery(obj, param);
        } catch (javax.naming.CommunicationException exc) {
//            com.see.truetransact.clientutil.ClientUtil.exceptionAlert(exc);
        } catch (java.rmi.ConnectException cexc) {
//            com.see.truetransact.clientutil.ClientUtil.exceptionAlert(cexc);
        } catch (Exception e) {
            throw e;
        }
        
//        jmsHelper.updateTimeStampInMessage("exit");
//        HashMap msgProps = new HashMap();
//        msgProps.put("cmpdescription",this.getClass().getName());
//        msgProps.put("machineip",obj.get("IP_ADDR"));
//        msgProps.put("remark","EJB Proxy Time Taken");
//        msgProps.put("datetime",new java.util.Date().toString());
//        jmsHelper.sendTimelyMessage(msgProps);
        
        return resultMap;
    }
    
    /** Returns an int based executeQuery operation success or failure state.
     * This is mainly for deleting existing data from the Database.
     * Calling Business Delegate and doing the process.
     *
     * @param obj HashMap (Bean objects)
     * @param param HashMap (Session Bean Parameters)
     * @return HashMapReturns Database data
     * @exception Exception if an invalid lookup.
     */
    public HashMap executeQuery (HashMap obj, HashMap param) throws Exception {
        return executeQuery (obj, param, true);
    }

    public static byte[] toCompressedBytes(Object o) throws IOException {
        if (o==null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ObjectOutputStream oos = new ObjectOutputStream(baos);
//        oos.writeObject(o);
//        oos.close();
//        byte[] bytes = baos.toByteArray();
//        baos.close();
//        System.out.println("@$ inside toCompressedBytes before compress:"+bytes.length);
        baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(new DeflaterOutputStream(baos));
        oos.writeObject(o);
        oos.close();
        oos = null;
        byte[] bytes = baos.toByteArray();
        baos.close();
        baos = null;
//        System.out.println("@$ inside toCompressedBytes after compress:"+bytes.length);
        return bytes;
    }
        
    public static Object fromCompressedBytes(byte[] bytes) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new InflaterInputStream(new ByteArrayInputStream(bytes)));
        Object retObj = ois.readObject();
        ois.close();
        ois = null;
        return retObj;
    }
}