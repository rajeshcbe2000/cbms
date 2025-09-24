/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ChangePasswordOB.java
 *
 * Created on September 30, 2004, 4:43 PM
 */

package com.see.truetransact.ui.login.newpasswd;

import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.ui.login.newpasswd.ChangePasswordRB;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.commonutil.StringEncrypter;
import com.see.truetransact.ui.common.passwordrules.PasswordRules ;
import com.see.truetransact.ui.TrueTransactMain;

import java.util.HashMap;
import java.util.List;
import java.util.Date;
/**
 *
 * @author  Lohith R.
 */
public class ChangePasswordOB  extends CObservable{
    private final int PWD_HISTORY_COUNT = 3 ;
    private static ChangePasswordOB objChangePasswordOB; // singleton object
    private final static ClientParseException parseException = ClientParseException.getInstance();
    final ChangePasswordRB objChangePasswordRB = new ChangePasswordRB();
    
    private String pwdOldPasswd = "";
    private String pwdNewPasswd = "";
    private String pwdConfirmPasswd = "";
    private String lblUserName = "";
    private int messageOption;
    
    private ProxyFactory proxy;
    private HashMap operationMap;
    
    private StringEncrypter encrypt = null;
    private static Date currDt = null;
    static {
        try {
            currDt = ClientUtil.getCurrentDate();
            objChangePasswordOB = new ChangePasswordOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /** Creates a new instance of ChangePasswordOB */
    private ChangePasswordOB() throws Exception {
        proxy = ProxyFactory.createProxy();
        encrypt = new StringEncrypter();
        
    }
    
    /** Creates a new instance of ViewLogOB */
    public static ChangePasswordOB getInstance() {
        return objChangePasswordOB;
    }
    
    /** Setter method for pwdOldPasswd */
    void setPwdOldPasswd(String pwdOldPasswd){
        this.pwdOldPasswd = pwdOldPasswd;
        setChanged();
    }
    /** Getter method for pwdOldPasswd */
    String getPwdOldPasswd(){
        return this.pwdOldPasswd;
    }
    
    /** Setter method for pwdNewPasswd */
    void setPwdNewPasswd(String pwdNewPasswd){
        this.pwdNewPasswd = pwdNewPasswd;
        setChanged();
    }
    /** Getter method for pwdNewPasswd */
    String getPwdNewPasswd(){
        return this.pwdNewPasswd;
    }
    
    /** Setter method for pwdConfirmPasswd */
    void setPwdConfirmPasswd(String pwdConfirmPasswd){
        this.pwdConfirmPasswd = pwdConfirmPasswd;
        setChanged();
    }
    /** Getter method for pwdConfirmPasswd */
    String getPwdConfirmPasswd(){
        return this.pwdConfirmPasswd;
    }
    
    /** Setter method for lblUserName */
    void setLblUserName(String lblUserName){
        this.lblUserName = lblUserName;
        setChanged();
    }
    /** Getter method for lblUserName */
    String getLblUserName(){
        return this.lblUserName;
    }
    
    /** To validate an User by taking his UserID & Password */
    public HashMap validateUser(String userID, String password) {
        HashMap resultMap = null;
        try{
            final HashMap whereMap = new HashMap();
            whereMap.put("USERID", userID);
            whereMap.put("BRANCH", TrueTransactMain.BRANCH_ID);
            whereMap.put("PASSSWORD", encrypt.encrypt(password));
            whereMap.put("CURR_DATE", currDt.clone());

            final List users = ClientUtil.executeQuery("userValidate", whereMap);
            
            if( users.size() > 0 ){
                resultMap = (HashMap) users.get(0);
            }
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return resultMap;
    }
    
    private boolean chkPwdHistory(HashMap whereMap){
        PasswordRules pwdRules = new PasswordRules();
        int pwdHistoryCount = pwdRules.getPwdHistoryCount();
        boolean historyOk = true ;
        List pwdLst = ClientUtil.executeQuery("chkPwdHistory", whereMap);
        if(pwdLst == null) {
            //true case, do nothing
        }
        else{
            for(int i = 0, j = pwdLst.size(); i < j ; i++ ){
                if(i < pwdHistoryCount){
                    if(((String)pwdLst.get(i)).equals((String)whereMap.get("PASSSWORD"))){
                        historyOk = false ;
                        break ;
                    }
                }
                else{
                    break ;
                }
            }
        }
        return historyOk ;
    }
    
    public boolean doAction(){
        boolean passwdChanged = false;
        try {
            HashMap whereMap = new HashMap();
            final String USER_ID = getLblUserName();
            final String USER_PWD =  getPwdOldPasswd();
            final String USER_NEW_PWD = getPwdNewPasswd();
            final String USER_NEW_CONFIRM_PWD = getPwdConfirmPasswd();
            
            HashMap USERINFO = validateUser(USER_ID, USER_PWD);
            
            if(USERINFO != null){
                if(USER_NEW_PWD.equals(USER_NEW_CONFIRM_PWD)){
                    whereMap.put("USERID", USER_ID);
                    whereMap.put("PASSSWORD", encrypt.encrypt(USER_NEW_PWD));
                    whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                    whereMap.put("CURR_DATE", currDt.clone());
                    if(chkPwdHistory(whereMap)){
                        try{
                            ClientUtil.execute("userPasswordChanged", whereMap);
                            ClientUtil.execute("insertPasswordChanged", whereMap);
                            passwdChanged = true;
                            resetPassword();
                            ShowDialogue(objChangePasswordRB.getString("passwordUpdated"));
                        }catch(Exception e){
                            passwdChanged = false;
                            ShowDialogue(objChangePasswordRB.getString("passwordError"));
                        }
                    }
                    else{
                        ShowDialogue(objChangePasswordRB.getString("pwdHistoryMissmatch"));
                        resetPassword();
                        passwdChanged = false;
                    }
                }else{
                    ShowDialogue(objChangePasswordRB.getString("newPasswordMissmatch"));
                    resetPassword();
                    passwdChanged = false;
                }
            }else{
                ShowDialogue(objChangePasswordRB.getString("oldPasswordMissmatch"));
                resetPassword();
                passwdChanged = false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return passwdChanged;
    }
    
    /** Display message  */
    private void ShowDialogue(String message){
        String[] options = {objChangePasswordRB.getString("cDialogOk")};
        messageOption = COptionPane.showOptionDialog(null, message, CommonConstants.WARNINGTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
    }
    
    /** Resets all the Fields to Null  */
    public void resetPassword(){
        setPwdNewPasswd("");
        setPwdConfirmPasswd("");
        setPwdOldPasswd("");
        notifyObservers();
    }
}
