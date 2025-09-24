/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * WebHttpsClientProxy.java
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
//import com.sun.net.ssl.internal.www.protocol.https.HttpsURLConnection;
import sun.net.www.protocol.https.HttpsURLConnectionImpl;
import java.net.URLEncoder;
import java.net.MalformedURLException;
import javax.net.ssl.HostnameVerifier;
import java.io.*;
import com.see.truetransact.clientexception.ClientParseException;

import org.apache.log4j.Logger;

import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;

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
public class WebHttpsClientProxy extends ProxyFactory {
    private static Logger log = Logger.getLogger(ProxyFactory.class);
    private String jndiHomeName = "URL";
    private Map cache;                  //used to hold references to Resources for re-use

    private static WebHttpsClientProxy me;   // singleton object

    static {
        try {
            log.info("Creating Web Https Client Proxy...");
            me = new WebHttpsClientProxy();
            ClientParseException parse = ClientParseException.getInstance();
            parse.getErrorLogFile();
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
    private WebHttpsClientProxy() throws Exception {
        cache = Collections.synchronizedMap(new HashMap());
    }
    
    /**
     * Returns an instance of EJBClientProxy.
     *
     * @return              EJBClientProxy
     */
    static public WebHttpsClientProxy getInstance() {
        return me;
    }
    
    /** It will get the ejb Remote home factory. If this ejb home factory has already been
     * clients need to cast to the type of EJBHome they desire
     *
     * @return the EJB Home corresponding to the homeName
     * @throws Exception Throws Exception if the Remote Interface gives error
     */
    public HttpsURLConnectionImpl getRemoteInterface() throws Exception {
        HttpsURLConnectionImpl connection = null;
        
        System.setProperty("javax.net.ssl.trustStore", System.getProperty("user.home")+"\\tt\\"+"cacerts");
        System.setProperty("javax.net.ssl.keystorePassword","changeit");

        InputStream in = getClass().getResourceAsStream("/cacerts");

        if(in==null){
            System.out.println ("Certificate file is missing in the resource ..Please contact System Admin.");
        } else {
            File f = new File(System.getProperty("user.home")+"\\tt\\"+"cacerts");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            int c=0;
            
            while((c=in.read())!=-1){
                fo.write((char)c);
            }
            fo.close();
        }

        URL url = new URL(ClientConstants.HTTPS_SERVER_ROOT + "GateWayServlet");
        connection = (HttpsURLConnectionImpl) url.openConnection();
//        System.out.println("Connection class : " + connection.getClass());
        ((HttpsURLConnectionImpl)connection).setHostnameVerifier(new  MyHostnameVerifier());    

        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);
//            cache.put(jndiHomeName, connection);
//        }
        return connection;
    }
    
    private class MyHostnameVerifier implements HostnameVerifier {
      public boolean verify(String hostname,  javax.net.ssl.SSLSession session) {        
            return true;       
        }
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
        HashMap resultMap = null;
        addInfoToMap(obj);
        
        HttpsURLConnectionImpl connection = getRemoteInterface();
        
        HashMap servletMap = new HashMap();
        servletMap.put(CommonConstants.METHOD, CommonConstants.EXECUTE);
        servletMap.put(CommonConstants.OBJ, obj);
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

                if (resultMap != null && resultMap.containsKey(CommonConstants.STATUS_EXCEPTION)) {
                    throw (Exception) resultMap.get(CommonConstants.STATUS_EXCEPTION);
                }
            }
        } catch (java.io.EOFException eof) {
            // ignore 
            //eof.printStackTrace();
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
        
        HttpsURLConnectionImpl connection = getRemoteInterface();
        
        HashMap servletMap = new HashMap();
        servletMap.put(CommonConstants.METHOD, CommonConstants.EXECUTE_QUERY);
        servletMap.put(CommonConstants.OBJ, obj);
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

                if (resultMap.containsKey(CommonConstants.STATUS_EXCEPTION)) {
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
            WebHttpsClientProxy web = WebHttpsClientProxy.getInstance();
            HashMap obj = new HashMap();
            obj.put("From", "Web Client Proxy");
            HashMap param = new HashMap();
            obj.put("TOObj", new java.util.Date());
            web.execute(obj, param);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    
}