/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * WebClientProxy.java
 *
 * Created on November 23, 2004, 12:23 PM
 */
package com.see.truetransact.clientproxy;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Properties;

import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.MalformedURLException;
import java.io.*;

import org.apache.log4j.Logger;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import java.util.zip.*;

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
public class WebClientProxy extends ProxyFactory {
    private static Logger log = Logger.getLogger(ProxyFactory.class);
    private String jndiHomeName = "URL";
    private Map cache;                  //used to hold references to Resources for re-use
    
    private static WebClientProxy me;   // singleton object
    
    static {
        try {
            log.info("Creating Web Client Proxy...");
            me = new WebClientProxy();
        } catch(ClientProxyException se) {
            log.error(se);
        } catch(Exception e) {
            log.error(e);
        }
    }
    
    /**
     * Creates a private constructor
     *
     * @exception  Exception
     */
    private WebClientProxy() throws Exception {
        cache = Collections.synchronizedMap(new HashMap());
    }
    
    /**
     * Returns an instance of EJBClientProxy.
     *
     * @return              EJBClientProxy
     */
    static public WebClientProxy getInstance() {
        return me;
    }
    
    /** It will get the ejb Remote home factory. If this ejb home factory has already been
     * clients need to cast to the type of EJBHome they desire
     *
     * @return the EJB Home corresponding to the homeName
     * @throws Exception Throws Exception if the Remote Interface gives error
     */
    public HttpURLConnection getRemoteInterface() throws Exception {
        HttpURLConnection connection = null;
//        if (cache.containsKey(jndiHomeName)) {
//            connection =(HttpURLConnection) cache.get(jndiHomeName);
//        } else {
            URL url = new URL(ClientConstants.HTTPS_SERVER_ROOT + "GateWayServlet");
            connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            
//            cache.put(jndiHomeName, connection);
//        }
        
        return connection;
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
//        System.out.println("@$ inside WebClientProxy execute obj:"+obj+" / param:"+param);
        HashMap resultMap = null;
        addInfoToMap(obj);
        HashMap data = new HashMap();
        data.put("DATA",toCompressedBytes(obj));
        HttpURLConnection connection = getRemoteInterface();
        
        HashMap servletMap = new HashMap();
        servletMap.put(CommonConstants.METHOD, CommonConstants.EXECUTE);
//        servletMap.put(CommonConstants.OBJ, obj);
        servletMap.put(CommonConstants.OBJ, data);
        servletMap.put(CommonConstants.PARAM, param);
        
        try {
            // Writing to the Servlet
            ObjectOutputStream dataOut = new ObjectOutputStream(connection.getOutputStream());
            dataOut.writeObject(servletMap);
            dataOut.flush();
            dataOut.close();

            // Checking the Http Response Status
            int response = connection.getResponseCode();

            if (response == java.net.HttpURLConnection.HTTP_OK) {
                // Reading from the Servlet
                ObjectInputStream input = new ObjectInputStream(connection.getInputStream());
                resultMap = (HashMap) input.readObject();
                if (resultMap != null && resultMap.get("DATA") != null) {
                    resultMap = (HashMap) fromCompressedBytes((byte[]) resultMap.get("DATA"));
                }
                data.clear();
                data = null;
                if (resultMap != null && resultMap.containsKey(CommonConstants.STATUS_EXCEPTION)) {
                    TTException exception = null;
                    exception = (TTException) resultMap.get(CommonConstants.STATUS_EXCEPTION);
                    if (exception != null && CommonUtil.convertObjToStr(exception.getMessage()).equals("SE")) {
                        TrueTransactMain.startAutoLogoutTimerSessionExpiry();
                    }
                    throw (Exception) resultMap.get(CommonConstants.STATUS_EXCEPTION);
                }
            }
        } catch (java.io.EOFException eof) {
            // ignore 
        }
        connection = null;
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
    public HashMap executeQuery(HashMap obj, HashMap param) throws Exception {
        HashMap resultMap = null;
        
        addInfoToMap(obj);
        HashMap data = new HashMap();
        data.put("DATA",toCompressedBytes(obj));
        HttpURLConnection connection = getRemoteInterface();
        
        HashMap servletMap = new HashMap();
        servletMap.put(CommonConstants.METHOD, CommonConstants.EXECUTE_QUERY);
//        servletMap.put(CommonConstants.OBJ, obj);
        servletMap.put(CommonConstants.OBJ, data);
        servletMap.put(CommonConstants.PARAM, param);
        
        try {        
            // Writing to the Servlet
            ObjectOutputStream dataOut = new ObjectOutputStream(connection.getOutputStream());
            dataOut.writeObject(servletMap);
            dataOut.flush();
            dataOut.close();

            // Checking the Http Response Status
            int response = connection.getResponseCode();

            if (response == java.net.HttpURLConnection.HTTP_OK) {
                // Reading from the Servlet
                ObjectInputStream input = new ObjectInputStream(connection.getInputStream());
                resultMap = (HashMap) input.readObject();
                if (resultMap!=null && resultMap.get("DATA")!=null) {
                    resultMap = (HashMap) fromCompressedBytes((byte[])resultMap.get("DATA"));
                }
                data.clear();
                data = null;
                if (resultMap.containsKey(CommonConstants.STATUS_EXCEPTION)) {
                    TTException exception = null;
                    exception = (TTException) resultMap.get(CommonConstants.STATUS_EXCEPTION);
                    if (exception != null && exception.getMessage() !=null && exception.getMessage().equals("SE")) {
                        TrueTransactMain.startAutoLogoutTimerSessionExpiry();
                    }
                    throw (Exception) resultMap.get(CommonConstants.STATUS_EXCEPTION);
                }
            }
        } catch (java.io.EOFException eof) {
            // ignore 
        }        
        connection = null;
        return resultMap;
    }

    public static void main(String str[]) {
        try {
            WebClientProxy web = WebClientProxy.getInstance();
            HashMap obj = new HashMap();
            obj.put("From", "Web Client Proxy");
            HashMap param = new HashMap();
            obj.put("TOObj", new java.util.Date());
            web.execute(obj, param);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
}