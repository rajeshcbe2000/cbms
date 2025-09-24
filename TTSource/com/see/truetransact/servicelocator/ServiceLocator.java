/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 **
 *
 * ServiceLocator.java
 *
 * Created on June 23, 2003, 4:20 PM
 */

package com.see.truetransact.servicelocator;

import java.io.Reader;
import java.net.URL;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;

import javax.sql.DataSource;

import javax.rmi.PortableRemoteObject;

import javax.jms.QueueConnectionFactory;
import javax.jms.Queue;
import javax.jms.TopicConnectionFactory;
import javax.jms.Topic;

import org.apache.log4j.Logger;

import com.ibatis.db.sqlmap.SqlMap;
import com.ibatis.common.resources.Resources;
import com.ibatis.db.sqlmap.XmlSqlMapBuilder;

import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.serverutil.ServerConstants;

import com.see.truetransact.serverside.EJBTTHome;
import com.see.truetransact.serverside.EJBTTRemote;


/**
 * This class is an implementation of the Service Locator pattern. It is
 * used to looukup resources such as EJBHomes, JMS Destinations, etc.
 * This implementation uses the "singleton" strategy and also the "caching"
 * strategy.
 * This implementation is intended to be used on the web tier and
 * not on the ejb tier.
 *
 * @author  Balachandar
 */

public class ServiceLocator  implements java.io.Serializable{
    
    private InitialContext ic;
    private Map cache; //used to hold references to EJBHomes/JMS Resources for re-use
    private HashMap serviceLocatercacheSessionMap=new HashMap(); // ADDED BY ABI FOR UNIQUE SESSION FOR EACH USER 16-06-2015
    private final static Logger _log = Logger.getLogger(ServiceLocator.class);
    private static ServiceLocator me;
    private HashMap lookupAddedMap = new HashMap();
    static {
        try {
            me = new ServiceLocator();
        } catch(ServiceLocatorException se) {
            _log.error(se);
            System.err.println(se);
            se.printStackTrace(System.err);
        }
    }
    
    private ServiceLocator() throws ServiceLocatorException  {
        try {
//            Properties prop = new Properties();
//            prop.put(Context.INITIAL_CONTEXT_FACTORY, CommonConstants.INITIAL_CONTEXT_FACTORY);
//            prop.put(Context.PROVIDER_URL, CommonConstants.PROVIDER_URL);
//            prop.put(Context.URL_PKG_PREFIXES, CommonConstants.URL_PKG_PREFIXES);
//            ic = new InitialContext(prop);
//            System.out.println("#$@@ inside ServiceLocator constructor...");
            ic = new InitialContext();
//            System.out.println("#$@@ inside ServiceLocator constructor... InitialContext created...");
//            ic = CommonUtil.getInitialContext();
//        weblogic.jndi.Environment env = new weblogic.jndi.Environment();
//        env.setProviderUrl(CommonConstants.PROVIDER_URL);
//        env.setInitialContextFactory(CommonConstants.INITIAL_CONTEXT_FACTORY);
//        ic = env.getInitialContext();

            cache = Collections.synchronizedMap(new HashMap());
        } catch (Exception e) {
            throw new ServiceLocatorException(e);
        }
    }
    
    static public ServiceLocator getInstance() {
        return me;
    }
    
    /**
     * will get the ejb Local home factory. If this ejb home factory has already been
     * clients need to cast to the type of EJBHome they desire
     *
     * @return the EJB Home corresponding to the homeName
     */
    public EJBLocalHome getLocalHome(String jndiHomeName) throws ServiceLocatorException {
        jndiHomeName = jndiHomeName.replaceAll("JNDI", "");
        EJBLocalHome home = null;
        try {
            if (cache.containsKey(jndiHomeName)) {
                home = (EJBLocalHome) cache.get(jndiHomeName);
            } else {
//                home = (EJBLocalHome) ic.lookup("TTServer/"+jndiHomeName+"!com.see.truetransact.serverside.EJBTTHome");
                home = (EJBLocalHome) ic.lookup("java:global/arcat/TTServer/"+jndiHomeName+"!com.see.truetransact.serverside.EJBTTHome");
//                "ejb:" + appName + "/" + moduleName + "/" + distinctName + "/" + beanName + "!" + viewClassName
                cache.put(jndiHomeName, home);
//                System.out.println ("Cached : " + cache);
            }
        } catch (Exception e) {
            throw new ServiceLocatorException(e);
        }
        return home;
    }
    
    
    /**
     * will get the ejb Local home factory. If this ejb home factory has already been
     * clients need to cast to the type of EJBHome they desire
     *
     * @return the EJB Home corresponding to the homeName
     */
    public EJBTTRemote getLocal(String jndiHomeName) throws ServiceLocatorException {
        EJBTTRemote remote = null;
        try {
                remote = ((EJBTTHome) getLocalHome(jndiHomeName)).create();
//                System.out.println ("Cached : " + cache);
        } catch (Exception e) {
            throw new ServiceLocatorException(e);
        }
        return remote;
    }
    
    /**
     * will get the ejb Remote home factory. If this ejb home factory has already been
     * clients need to cast to the type of EJBHome they desire
     *
     * @return the EJB Home corresponding to the homeName
     */
    public EJBHome getRemoteHome(String jndiHomeName, Class className) throws ServiceLocatorException {
        EJBHome home = null;
        try {
            if (cache.containsKey(jndiHomeName)) {
                home = (EJBHome) cache.get(jndiHomeName);
            } else {
                Object objref = ic.lookup(jndiHomeName);
                Object obj = PortableRemoteObject.narrow(objref, className);
                home = (EJBHome)obj;
                //cache.put(jndiHomeName, home);
            }
        } catch (Exception e) {
            throw new ServiceLocatorException(e);
        }
        
        return home;
    }
    
    /**
     * will get the ejb Remote home factory. If this ejb home factory has already been
     * clients need to cast to the type of EJBHome they desire
     *
     * @return the EJB Home corresponding to the homeName
     *
    public SqlMap getDAOSqlMap() throws ServiceLocatorException {
        SqlMap sqlMap = null;
        try {
            if (cache.containsKey(ServerConstants.DAO_URL)) {
                sqlMap = (SqlMap) cache.get(ServerConstants.DAO_URL);
            } else {
                Reader reader = new java.io.InputStreamReader(ServiceLocator.class.getResourceAsStream("/" + ServerConstants.DAO_URL));
                sqlMap = XmlSqlMapBuilder.buildSqlMap(reader);
                cache.put(ServerConstants.DAO_URL, sqlMap);
            }
        } catch (Exception e) {
            throw new ServiceLocatorException(e);
        }
        return sqlMap;
    }*/
    
    public SqlMap getDAOSqlMap() throws ServiceLocatorException {
        SqlMap sqlMap = null;
        try {
            if (cache.containsKey(ServerConstants.DAO_URL)) {
                sqlMap = (SqlMap) cache.get(ServerConstants.DAO_URL);
            } else {
                Reader reader = new java.io.InputStreamReader(ServiceLocator.class.getResourceAsStream("/" + ServerConstants.DAO_URL));
                sqlMap = XmlSqlMapBuilder.buildSqlMap(reader);
                
//                sqlMap.getCache("WEAK").setFlushInterval(1);
//                sqlMap.getCache("SOFT").setFlushInterval(1);
//                sqlMap.getCache("STRONG").setFlushInterval(1);
                cache.put(ServerConstants.DAO_URL, sqlMap);
            }
            flushCache(sqlMap);
        } catch (Exception e) {
            throw new ServiceLocatorException(e);
        }
        return sqlMap;
    }
    
    public static void flushCache(SqlMap sqlMap){
        java.util.Iterator iter = sqlMap.getCaches();
        while (iter.hasNext()){
            ((com.ibatis.db.sqlmap.cache.CacheModel) iter.next()).flush();
        }
    }
    
    /**
     * will get the SqlMap for 
     *
     * @return the SqlMap
     */
    public SqlMap getDAOSqlMapBatch() throws ServiceLocatorException {
        SqlMap sqlMap = null;
        try {
            if (cache.containsKey(ServerConstants.DAO_URL_BATCH)) {
                sqlMap = (SqlMap) cache.get(ServerConstants.DAO_URL_BATCH);
            } else {
                Reader reader = new java.io.InputStreamReader(ServiceLocator.class.getResourceAsStream("/" + ServerConstants.DAO_URL_BATCH));
                sqlMap = XmlSqlMapBuilder.buildSqlMap(reader);
                cache.put(ServerConstants.DAO_URL_BATCH, sqlMap);
            }
        } catch (Exception e) {
            throw new ServiceLocatorException(e);
        }
        return sqlMap;
    }
    
    /**
     * will get the SqlMap for 
     *
     * @return the SqlMap
     */
    public SqlMap getDAOSqlMapLog() throws ServiceLocatorException {
        SqlMap sqlMap = null;
        try {
            if (cache.containsKey(ServerConstants.DAO_URL_LOG)) {
                sqlMap = (SqlMap) cache.get(ServerConstants.DAO_URL_LOG);
            } else {
                Reader reader = new java.io.InputStreamReader(ServiceLocator.class.getResourceAsStream("/" + ServerConstants.DAO_URL_BATCH));
                sqlMap = XmlSqlMapBuilder.buildSqlMap(reader);
                cache.put(ServerConstants.DAO_URL_LOG, sqlMap);
            }
        } catch (Exception e) {
            throw new ServiceLocatorException(e);
        }
        return sqlMap;
    }
    /**
     * @return the factory for the factory to get queue connections from
     */
    public QueueConnectionFactory getQueueConnectionFactory(String qConnFactoryName)
    throws ServiceLocatorException {
        QueueConnectionFactory factory = null;
        try {
            if (cache.containsKey(qConnFactoryName)) {
                factory = (QueueConnectionFactory) cache.get(qConnFactoryName);
            } else {
                factory = (QueueConnectionFactory) ic.lookup(qConnFactoryName);
                cache.put(qConnFactoryName, factory);
            }
        } catch (Exception e) {
            throw new ServiceLocatorException(e);
        }
        return factory;
    }
    
    
    /**
     * @return the Queue Destination to send messages to
     */
    public  Queue getQueue(String queueName) throws ServiceLocatorException {
        Queue queue = null;
        try {
            if (cache.containsKey(queueName)) {
                queue = (Queue) cache.get(queueName);
            } else {
                queue =(Queue)ic.lookup(queueName);
                cache.put(queueName, queue);
            }
        } catch (Exception e) {
            throw new ServiceLocatorException(e);
        }
        
        return queue;
    }
    
    
    /**
     * This method helps in obtaining the topic factory
     * @return the factory for the factory to get topic connections from
     */
    public  TopicConnectionFactory getTopicConnectionFactory(String topicConnFactoryName) throws ServiceLocatorException {
        TopicConnectionFactory factory = null;
        try {
            if (cache.containsKey(topicConnFactoryName)) {
                factory = (TopicConnectionFactory) cache.get(topicConnFactoryName);
            } else {
                factory = (TopicConnectionFactory) ic.lookup(topicConnFactoryName);
                cache.put(topicConnFactoryName, factory);
            }
        } catch (Exception e) {
            throw new ServiceLocatorException(e);
        }
        return factory;
    }
    
    /**
     * This method obtains the topc itself for a caller
     * @return the Topic Destination to send messages to
     */
    public  Topic getTopic(String topicName) throws ServiceLocatorException {
        Topic topic = null;
        try {
            if (cache.containsKey(topicName)) {
                topic = (Topic) cache.get(topicName);
            } else {
                topic = (Topic)ic.lookup(topicName);
                cache.put(topicName, topic);
            }
        } catch (Exception e) {
            throw new ServiceLocatorException(e);
        }
        return topic;
    }
    
    /**
     * This method obtains the datasource itself for a caller
     * @return the DataSource corresponding to the name parameter
     */
    public DataSource getDataSource(String dataSourceName) throws ServiceLocatorException {
        DataSource dataSource = null;
        try {
            if (cache.containsKey(dataSourceName)) {
                dataSource = (DataSource) cache.get(dataSourceName);
            } else {
                dataSource = (DataSource)ic.lookup(dataSourceName);
                cache.put(dataSourceName, dataSource );
            }
        } catch (Exception e) {
            throw new ServiceLocatorException(e);
        }
        return dataSource;
    }
    
    /**
     * @return the URL value corresponding
     * to the env entry name.
     */
    public URL getUrl(String envName) throws ServiceLocatorException {
        URL url = null;
        try {
            url = (URL)ic.lookup(envName);
        } catch (Exception e) {
            throw new ServiceLocatorException(e);
        }
        
        return url;
    }
    
    /**
     * @return the boolean value corresponding
     * to the env entry such as SEND_CONFIRMATION_MAIL property.
     */
    public boolean getBoolean(String envName) throws ServiceLocatorException {
        Boolean bool = null;
        try {
            bool = (Boolean)ic.lookup(envName);
        } catch (Exception e) {
            throw new ServiceLocatorException(e);
        }
        return bool.booleanValue();
    }
    
    /**
     * @return the String value corresponding
     * to the env entry name.
     */
    public String getString(String envName) throws ServiceLocatorException {
        String envEntry = null;
        try {
            envEntry = (String)ic.lookup(envName);
        } catch (Exception e) {
            throw new ServiceLocatorException(e);
        }
        return envEntry ;
    }
    
    public static void main (String[] arg) {
        try {
                Reader reader = new java.io.InputStreamReader(ServiceLocator.class.getResourceAsStream("/"+ServerConstants.DAO_URL));
                SqlMap sqlMap = XmlSqlMapBuilder.buildSqlMap(reader);
                 
           
           System.out.println (new java.io.File(ServiceLocator.class.getResource("/" + ServerConstants.DAO_URL).getFile()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
    public HashMap getServiceLocatercacheSessionMap() {
        return serviceLocatercacheSessionMap;
    }

    public void setServiceLocatercacheSessionMap(HashMap serviceLocatercacheSessionMap) {
        this.serviceLocatercacheSessionMap = serviceLocatercacheSessionMap;
    }
    
    public HashMap getLookupAddedMap() {
        return lookupAddedMap;
    }

    public void setLookupAddedMap(HashMap lookupAddedMap) {
        this.lookupAddedMap = lookupAddedMap;
    }
}