/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * GateWayServlet.java
 *
 * Created on November 16, 2004, 11:49 AM
 */

package com.see.truetransact.businessdelegate;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.see.truetransact.clientproxy.EJBClientProxy;
//import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;

/**
 *
 * @author  bala
 * @version
 */
public class GateWayServlet extends HttpServlet {
    
    /** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
    }
    
    /** Destroys the servlet.
     */
    public void destroy() {
        
    }
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        HashMap resultMap = null;
        try {
            System.out.println ("Servlet's process ..... Request called...");
            ObjectInputStream input = new ObjectInputStream(request.getInputStream());
            HashMap inputMap = (HashMap) input.readObject();
            
            EJBClientProxy proxy = EJBClientProxy.getInstance();
            HashMap objMap = (HashMap) inputMap.get(CommonConstants.OBJ);
            
//            ProxyParameters.BRANCH_ID = CommonUtil.convertObjToStr(objMap.get(CommonConstants.BRANCH_ID));
//            ProxyParameters.USER_ID = CommonUtil.convertObjToStr(objMap.get(CommonConstants.USER_ID));
            
            if (inputMap.get(CommonConstants.METHOD).toString().equals(CommonConstants.EXECUTE)) {
                resultMap = proxy.execute(objMap, (HashMap) inputMap.get(CommonConstants.PARAM), false);
            } else if (inputMap.get(CommonConstants.METHOD).toString().equals(CommonConstants.EXECUTE_QUERY)) {
                resultMap = proxy.executeQuery(objMap, (HashMap) inputMap.get(CommonConstants.PARAM), false);
            } 
            
            ObjectOutputStream dataOut = new ObjectOutputStream(response.getOutputStream());
            dataOut.writeObject(resultMap);
            dataOut.flush();
            dataOut.close();
        } catch (ServletException servExc) {
            servExc.printStackTrace();
        } catch (IOException ioExc) {
            ioExc.printStackTrace();
        } catch (Exception exc) {
            resultMap = new HashMap();
            resultMap.put (CommonConstants.STATUS_EXCEPTION, exc);
            
            try {
                ObjectOutputStream dataOut = new ObjectOutputStream(response.getOutputStream());
                dataOut.writeObject(resultMap);
                dataOut.flush();
                dataOut.close();
            } catch (Exception ex) {}
            
            exc.printStackTrace();
        }
    }
    
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
    
}
