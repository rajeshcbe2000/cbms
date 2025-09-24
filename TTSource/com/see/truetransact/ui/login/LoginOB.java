/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * LoginOB.java
 *
 * Created on September 30, 2003, 12:39 PM
 */

package com.see.truetransact.ui.login;

import java.util.HashMap;
import java.util.List;


import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.transferobject.sysadmin.user.UserTO;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientproxy.ProxyFactory;

import com.see.truetransact.commonutil.StringEncrypter;

import org.apache.log4j.Logger;
/** To Validate a User by getting his password
 * @author karthik
 */
public class LoginOB extends java.util.Observable{
    private String txtUserId = ""; //"admin";//static value is given for demo
    private String pwdPassword = ""; //"admin";//static value is given for demo
    private String txtBank = "";
    private String txtBranch = "";//ClientConstants.HO == null ? "" :ClientConstants.HO; //"admin";//static value is given for demo
    private ProxyFactory proxy;
    private HashMap operationMap;
    private final static Logger log = Logger.getLogger(LoginOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    //--- Declarations for operationMap
    private final String LOGIN_JNDI = "LoginJNDI";
    private final String LOGIN_HOME = "login.LoginHome";
    private final String LOGIN_REMOTE = "login.Login";
    private StringEncrypter encrypt = null;
    
    //private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    /** Creates a new instance of LoginOB */
    public LoginOB() throws Exception {
        proxy = ProxyFactory.createProxy();
        encrypt = new StringEncrypter();
        setOperationMap();
    }
    
   /* Sets the HashMap required to set JNDI,Home and Remote*/
    private void setOperationMap() throws Exception{
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, LOGIN_JNDI);
        operationMap.put(CommonConstants.HOME, LOGIN_HOME);
        operationMap.put(CommonConstants.REMOTE, LOGIN_REMOTE);
    }
    
    void setTxtUserId(String txtUserId){
        this.txtUserId = txtUserId;
        setChanged();
    }
    String getTxtUserId(){
        return this.txtUserId;
    }
    
    void setTxtBranch(String txtBranch){
        this.txtBranch = txtBranch;
//         this.txtBranch=txtBranch.toUpperCase();
        setChanged();
    }
    String getTxtBranch(){
        return this.txtBranch;
    }
    
    void setPwdPassword(String pwdPassword){
        this.pwdPassword = pwdPassword;
        setChanged();
    }
    String getPwdPassword(){
        return this.pwdPassword;
    }
    
    /*public String getLblStatus(){
        return this.lblStatus;
    }
    
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }*/
    
    /** To validate an User by taking his UserID & Password */    
    public HashMap validateUser() {
        HashMap resultMap = null;
        try{
            //System.out.println("this.txtUserID:" + this.getTxtUserId());
            HashMap whereMap = new HashMap();
            whereMap.put("USERID", this.getTxtUserId());
            whereMap.put("PWD", encrypt.encrypt(this.getPwdPassword()));
//            whereMap.put("PWD", this.getPwdPassword());
            whereMap.put("BRANCHCODE", this.getTxtBranch());
            whereMap.put("DATE_TIME", new java.util.Date());
//            whereMap.put("LOGIN_DATE", currDt.clone());
            //System.out.println("whereMap:" + whereMap);
            resultMap =  proxy.executeQuery(whereMap, operationMap);
        }catch(Exception e){
             parseException.logException(e,true);
        }
        return resultMap;
    }
    
    /**
     * Getter for property txtBank.
     * @return Value of property txtBank.
     */
    public java.lang.String getTxtBank() {
        return txtBank;
    }
    
    /**
     * Setter for property txtBank.
     * @param txtBank New value of property txtBank.
     */
    public void setTxtBank(java.lang.String txtBank) {
        this.txtBank = txtBank;
    }
    
    /** To set the status based on ActionType, either New, Edit, etc., */
    /*public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }*/
    
    /** To update the Status based on result performed by doAction() method */
    /*public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }*/
}