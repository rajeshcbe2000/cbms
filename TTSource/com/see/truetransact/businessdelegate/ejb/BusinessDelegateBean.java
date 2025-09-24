/* Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BusinessDelegateBean.java
 *
 * Created on June 23, 2003, 12:23 PM
 */

package com.see.truetransact.businessdelegate.ejb;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;
import javax.rmi.PortableRemoteObject;

import java.util.HashMap;
import java.rmi.RemoteException;

import org.apache.log4j.Logger;

//import com.see.truetransact.businessdelegate.proxy.ProxyFactory;
import com.see.truetransact.serverside.EJBTTHome;
import com.see.truetransact.serverside.EJBTTRemote;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import java.io.*;
import java.util.zip.*;
import java.util.ArrayList;

//import com.see.health.jms.client.JMSHelper;

/**
 * BusinessDelegateBean used as a Control point for all process.
 * All the request reaches BusinessDelegateBean and it redirect 
 * to other beans based on parameter passed in the bean.
 *
 * This class is an implementation of the BusinessDelegate pattern. It is
 * used to call Business Process (Session Bean).
 * This implementation is intended to be used on the EJB tier.
 *
 * @author  Balachandar
 */

public class BusinessDelegateBean implements SessionBean, TTAction {
    private EJBTTRemote _remote = null;                  // remote interface
    private ServiceLocator _serviceLocator = null;       // JNDI Lookup
    private final static Logger _log = Logger.getLogger(BusinessDelegateBean.class);
    private SessionContext sessContext = null; 
    //private JMSHelper jmsHelper;
    private int transCount = 0;
    private HashMap deleteUserdata = null;
    /** EJB Call back method.
     * Instanciates Service Locator.
     *
     * @throws CreateException Throws ejb Create exception
     * @throws RemoteException Throws Remote exception (Uses Remote Interface)
     */
    public void ejbCreate() throws CreateException, RemoteException {
        //jmsHelper = new JMSHelper();
        _log.info("Calling Service Locator");
        _serviceLocator = ServiceLocator.getInstance();
        _log.info("Service Locator Instanceated");
    }
    
    /**
     * EJB Call back method.
     * Destroying Service Locator.
     */
    public void ejbRemove()  {
//        jmsHelper = null;
        _serviceLocator = null;
        _log.info("Service Locator Removed");
    }
    
    /** EJB Call back method.
     * Setting Session Context
     *
     * @param sess Setting Session Context
     */
    public void setSessionContext(SessionContext sess) {
        sessContext = sess;
//        sessContext. .getEnvironment().put("SessionID", String.valueOf(new java.util.Date().getTime()));
    }
    
    /**
     * EJB Call back method.
     * Activate from the instance Pool
     */
    public void ejbActivate() {
    }
    
    /**
     * EJB Call back method.
     * Storing into the Instance Pool
     */
    public void ejbPassivate(){
    }

    /**
     * Executes operation.
     * This is mainly for insert, update and deleting data from the Database.
     *
     * @param       obj     HashMap (Bean objects)
     * @param       param   HashMap (Parameter for Connecting to the Session Bean)
     * @exception  Exception  (if any exception occurred)
     */
    public HashMap execute (HashMap obj, HashMap param) throws Exception {
//        System.out.println("@$ inside BusinessDelegateBean execute obj:"+obj+" / param:"+param);
        if (obj.get("DATA")!=null) {
            obj = (HashMap) fromCompressedBytes((byte[]) obj.get("DATA"));
        } else {
            obj = new HashMap();
        }
       //_log.info(obj.toString());
        //_log.info(param.toString());
//        jmsHelper.updateTimeStampInMessage("entry");
        HashMap data = null;
        String sessionid = CommonUtil.convertObjToStr(obj.get("SESSION_ID"));// ADDED BY ABI FOR UNIQUE SESSION FOR EACH USER 16-06-2015
        String userid = CommonUtil.convertObjToStr(obj.get("USER_ID"));
        String mapName = CommonUtil.convertObjToStr(obj.get("MAPNAME"));
        if(mapName.length()>0 &&  (mapName.equals("insertDailyDayendStatusFinal") || mapName.equals("daybeginOterUserLogout"))){
            deleteUserdata =new HashMap();
            deleteUserdata=(HashMap)obj.get("WHERE");
            if(deleteUserdata.containsKey("LOGOUT_LIST")){
            deleteUserList((ArrayList)deleteUserdata.get("LOGOUT_LIST"));
            }
        }
        //System.out.println("execute userid : "+userid +" sessionid : "+sessionid+" obj : "+obj);
        if (sessionid.length() > 0 &&(!CommonUtil.convertObjToStr(_serviceLocator.getServiceLocatercacheSessionMap().get(userid)).equals(sessionid))) {
            if (!(CommonUtil.convertObjToStr(obj.get("MAPNAME")).equals("updateUserLogoutStatus")
                    || CommonUtil.convertObjToStr(obj.get("MAPNAME")).equals("loginHistory"))) {
                throw new TTException("SE");
            }
        }
//        EJBTTHome home = (EJBTTHome) _serviceLocator.getLocalHome((String) param.get(CommonConstants.JNDI));
//        _remote = home.create();
        
        _remote = _serviceLocator.getLocal((String) param.get(CommonConstants.JNDI));
        try{
//            System.out.println("@$ inside BusinessDelegateBean execute obj:"+obj);
            data = new HashMap();
            data.put("DATA",toCompressedBytes(_remote.execute (obj)));
//            data = _remote.execute (obj);
            //ProxyFactory.createProxy().execute(obj, param);
        } catch(Exception tte){
            System.out.println("TTException caught");
            tte.printStackTrace();
            new BusinessRuleExceptionWriter().logException(tte, obj);
            throw tte;
        }
        return data;
//        jmsHelper.updateTimeStampInMessage("exit");
//        HashMap msgProps = new HashMap();
//        msgProps.put("cmpdescription",this.getClass().getName());
//        msgProps.put("machineip",obj.get("IP_ADDR"));
//        msgProps.put("remark","Serverside Time Taken");
//        msgProps.put("datetime",new java.util.Date().toString());
//        jmsHelper.sendTimelyMessage(msgProps);
    }
    
    /** Returns an HashMap based on Parameter passed to this function.
     * This is mainly for retrived data from the Database.
     *
     * @param obj HashMap  (Parameter for the query - Where condition)
     * @param param HashMap  (Parameter for Connecting to the Session Bean)
     * @return HashMap
     * @exception Exception (if any exception)
     */
    public HashMap executeQuery (HashMap obj, HashMap param) throws Exception {
        //System.out.println("sessContext.getEnvironment()" + sessContext.getEnvironment().get("SessionID"));
        if (obj.get("DATA")!=null) {
            obj = (HashMap) fromCompressedBytes((byte[]) obj.get("DATA"));
        } else {
            obj = new HashMap();
        }
        HashMap data = null;
        //_log.info(obj.toString());
        //_log.info(param.toString());
//        jmsHelper.updateTimeStampInMessage("entry");
        
//        EJBTTHome home = (EJBTTHome) _serviceLocator.getLocalHome((String) param.get(CommonConstants.JNDI));
//        _remote = home.create();
//        System.out.println("@$ inside BusinessDelegateBean executeQuery param:"+param);
        _remote = _serviceLocator.getLocal((String) param.get(CommonConstants.JNDI));
        try {
//            System.out.println("@$ inside BusinessDelegateBean executeQuery obj:"+obj);
            data = new HashMap();
            data = _remote.executeQuery(obj);
     //       System.out.println("execute query data#####"+data +"_serviceLocator.getServiceLocatercacheSessionMap()"+_serviceLocator.getServiceLocatercacheSessionMap());
            String sessionid = CommonUtil.convertObjToStr(data.get("SESSION_ID"));
            String userid = CommonUtil.convertObjToStr(data.get("USER_ID"));
            String existsessionid = CommonUtil.convertObjToStr(obj.get("SESSION_ID"));
            String existuserid = CommonUtil.convertObjToStr(obj.get("USER_ID"));
            if (sessionid.length()>0 && userid.length()>0) {
                _serviceLocator.getServiceLocatercacheSessionMap().put(userid, sessionid); // ADDED BY ABI FOR UNIQUE SESSION FOR EACH USER 16-06-2015
            } else if (existuserid.length()>0 && existsessionid.length() > 0 &&
                    (!CommonUtil.convertObjToStr(_serviceLocator.getServiceLocatercacheSessionMap().get(existuserid)).equals(existsessionid))) {
                throw new TTException("SE");
            }
            data.put("DATA",toCompressedBytes(data));
            
//             System.out.println("cacheSessionMap executeQuery FINAL  :"+cacheSessionMap +"sessionid######"+sessionid+"      :"+_serviceLocator.getServiceLocatercacheSessionMap());
        } catch(Exception tte){
            System.out.println("TTException caught");
            tte.printStackTrace();
            new BusinessRuleExceptionWriter().logException(tte, obj);
            throw tte;
        }

//        jmsHelper.updateTimeStampInMessage("exit");
//        HashMap msgProps = new HashMap();
//        msgProps.put("cmpdescription",this.getClass().getName());
//        msgProps.put("machineip",obj.get("IP_ADDR"));
//        msgProps.put("remark","Serverside Time Taken");
//        msgProps.put("datetime",new java.util.Date().toString());
//        jmsHelper.sendTimelyMessage(msgProps);
//        
        return data;
    }
    
    public static byte[] toCompressedBytes(Object o) throws IOException {
        if (o==null) {
            return null;
        }
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ObjectOutputStream oos = new ObjectOutputStream(baos);
//        oos.writeObject(o);
//        oos.close();
//        byte[] bytes = baos.toByteArray();
//        baos.close();
//        System.out.println("@$ inside toCompressedBytes before compress:"+bytes.length);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(new DeflaterOutputStream(baos){
            {def.setLevel(Deflater.BEST_COMPRESSION);
            }
        });
        oos.writeObject(o);
        oos.close();
        oos = null;
        byte[] bytes = baos.toByteArray();
//        System.out.println("@$ inside toCompressedBytes after single compress:"+bytes.length);
//        GZIPOutputStream zos = new GZIPOutputStream(baos) {{
//            def.setLevel(Deflater.BEST_COMPRESSION);
//        }};
//        zos.write(bytes);
//        zos.finish();
//        zos.flush();
//        bytes = baos.toByteArray();
        baos.close();
        baos = null;
//        System.out.println("@$ inside toCompressedBytes after best compress:"+bytes.length);
        return bytes;
    }
        
    public static Object fromCompressedBytes(byte[] bytes) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new InflaterInputStream(new ByteArrayInputStream(bytes)));
        Object retObj = ois.readObject();
        ois.close();
        ois = null;
//        ObjectInputStream ois = new ObjectInputStream(new GZIPInputStream(new ByteArrayInputStream(bytes)));
//        Object retObj = ois.readObject();
//        ois.close();
//        ois = null;
        return retObj;
    }
    
    private void deleteUserList(ArrayList lst)throws IOException{
      //  System.out.println("lst######"+lst);
        HashMap dataMap =new HashMap();
        if(lst !=null && lst.size()>0){
            for (int i=0;i<lst.size();i++){
                dataMap=(HashMap)lst.get(i);
                String user=CommonUtil.convertObjToStr(dataMap.get("USER_ID"));
                 _serviceLocator.getServiceLocatercacheSessionMap().remove(user);
            }
        }
    }
}
