/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositInterestApplicationOB.java
 *
 * Created on Mon Jun 13 18:24:58 IST 2011
 */

package com.see.truetransact.ui.sms;


import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientexception.ClientParseException;

/**
 *
 * @author
 */

public class SendSMSOB extends CObservable{
    
    
    
    final ArrayList tableTitle = new ArrayList();
    private ArrayList IncVal = new ArrayList();
    
    private EnhancedTableModel tblDepositInterestApplication;
    private ProxyFactory proxy;
    private HashMap map;
    private final static Logger log = Logger.getLogger(SendSMSOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private int result;
    private int actionType;
    private boolean rdoPrizedMember_Yes = false;
    private boolean rdoPrizedMember_No = false;
    private List finalList = null;
    private List finalTableList = null;
    
    private String txtProductID = "";
    private String txtTokenNo = "";
    private List calFreqAccountList = null;
    
    public SendSMSOB() {
        try {
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "DepositInterestApplicationJNDI");
            map.put(CommonConstants.HOME, "deposit.interestapplication.DepositInterestApplicationHome");
            map.put(CommonConstants.REMOTE, "deposit.interestapplication.DepositInterestApplication");
            setDepositInterestTableTitle();
            tblDepositInterestApplication = new EnhancedTableModel(null, tableTitle);
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    public void setDepositInterestTableTitle(){
        tableTitle.add("Select");
        tableTitle.add("Cust ID");
        tableTitle.add("Account No");
        tableTitle.add("Name");
        tableTitle.add("Dep Amt");
        tableTitle.add("Dep Date");
        tableTitle.add("Mat Date");
        tableTitle.add("From Date");
        tableTitle.add("To Date");
        tableTitle.add("Interest");
        tableTitle.add("SI A/c No");
        tableTitle.add("Cal Freq");
        IncVal = new ArrayList();
    }
   
    public void insertTableData(HashMap whereMap){
        try{
            ArrayList rowList = new ArrayList();
            ArrayList tableList = new ArrayList();
            HashMap dataMap = new HashMap();
            
            HashMap proxyResultMap = proxy.executeQuery(whereMap, map);
            if (proxyResultMap != null && proxyResultMap.size()>0) {
                System.out.println("!@!@ INTEREST_DATA : "+proxyResultMap.get("INTEREST_DATA"));
                tableList = (ArrayList) proxyResultMap.get("INTEREST_DATA");
                setFinalList(tableList);
            }
                System.out.println("#$# tableList:"+tableList);
                tblDepositInterestApplication= new EnhancedTableModel((ArrayList)tableList, tableTitle);
//            }
        }catch(Exception e){
            e.printStackTrace();
            parseException.logException(e,true);
        }
    }
    
    
    /** To perform the necessary operation */
    public void doAction(List finalTableList) {
        TTException exception = null;
        log.info("In doAction()");
        try {
            doActionPerform(finalTableList);
        } catch (Exception e) {
            System.out.println("##$$$##$#$#$#$# Exception e : " + e);
            log.info("Error In doAction()");
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            e.printStackTrace();
            if(e instanceof TTException) {
                exception = (TTException) e;
            }
        }
        if (exception != null) {
            parseException.logException(exception, true);
            setResult(actionType);
        }
    }

    
    /** To perform the necessary action */
    private void doActionPerform(List finalTableList) throws Exception{
        final HashMap data = new HashMap();
        if(finalTableList!= null && finalTableList.size()>0){
            data.put("ACCOUNT_LIST",finalTableList);
            if(getCalFreqAccountList()!= null && getCalFreqAccountList().size()>0){
                data.put("CAL_FREQ_ACCOUNT_LIST", getCalFreqAccountList());
            }
            data.put(CommonConstants.PRODUCT_ID,getTxtProductID());
            data.put("DO_TRANSACTION",new Boolean(true));
            data.put("TOKEN_NO", getTxtTokenNo());
        }
        System.out.println("Data in DepositInterestApplication OB : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
        setResult(getActionType());
    }
    
    public void resetForm(){
        resetTableValues();
        setChanged();
    }
     
     public void resetTableValues(){
        tblDepositInterestApplication.setDataArrayList(null,tableTitle);
    }
    
    /**
     * Getter for property tblDepositInterestApplication.
     * @return Value of property tblDepositInterestApplication.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblDepositInterestApplication() {
        return tblDepositInterestApplication;
    }    
      
    /**
     * Setter for property tblDepositInterestApplication.
     * @param tblDepositInterestApplication New value of property tblDepositInterestApplication.
     */
    public void setTblDepositInterestApplication(com.see.truetransact.clientutil.EnhancedTableModel tblDepositInterestApplication) {
        this.tblDepositInterestApplication = tblDepositInterestApplication;
    }    
    
    /**
     * Getter for property rdoPrizedMember_Yes.
     * @return Value of property rdoPrizedMember_Yes.
     */
    public boolean getRdoPrizedMember_Yes() {
        return rdoPrizedMember_Yes;
    }
    
    /**
     * Setter for property rdoPrizedMember_Yes.
     * @param rdoPrizedMember_Yes New value of property rdoPrizedMember_Yes.
     */
    public void setRdoPrizedMember_Yes(boolean rdoPrizedMember_Yes) {
        this.rdoPrizedMember_Yes = rdoPrizedMember_Yes;
    }
    
    /**
     * Getter for property rdoPrizedMember_No.
     * @return Value of property rdoPrizedMember_No.
     */
    public boolean getRdoPrizedMember_No() {
        return rdoPrizedMember_No;
    }
    
    /**
     * Setter for property rdoPrizedMember_No.
     * @param rdoPrizedMember_No New value of property rdoPrizedMember_No.
     */
    public void setRdoPrizedMember_No(boolean rdoPrizedMember_No) {
        this.rdoPrizedMember_No = rdoPrizedMember_No;
    }
    
    /**
     * Getter for property finalList.
     * @return Value of property finalList.
     */
    public java.util.List getFinalList() {
        return finalList;
    }    
    
    /**
     * Setter for property finalList.
     * @param finalList New value of property finalList.
     */
    public void setFinalList(java.util.List finalList) {
        this.finalList = finalList;
    }    
    
    /**
     * Getter for property txtProductID.
     * @return Value of property txtProductID.
     */
    public java.lang.String getTxtProductID() {
        return txtProductID;
    }
    
    /**
     * Setter for property txtProductID.
     * @param txtProductID New value of property txtProductID.
     */
    public void setTxtProductID(java.lang.String txtProductID) {
        this.txtProductID = txtProductID;
    }
    
    /**
     * Getter for property result.
     * @return Value of property result.
     */
    public int getResult() {
        return result;
    }
    
    /**
     * Setter for property result.
     * @param result New value of property result.
     */
    public void setResult(int result) {
        this.result = result;
    }
    
    /**
     * Getter for property actionType.
     * @return Value of property actionType.
     */
    public int getActionType() {
        return actionType;
    }
    
    /**
     * Setter for property actionType.
     * @param actionType New value of property actionType.
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }
    
    /**
     * Getter for property tableTitle.
     * @return Value of property tableTitle.
     */
    public java.util.ArrayList getTableTitle() {
        return tableTitle;
    }
    
    /**
     * Getter for property txtTokenNo.
     * @return Value of property txtTokenNo.
     */
    public java.lang.String getTxtTokenNo() {
        return txtTokenNo;
    }
    
    /**
     * Setter for property txtTokenNo.
     * @param txtTokenNo New value of property txtTokenNo.
     */
    public void setTxtTokenNo(java.lang.String txtTokenNo) {
        this.txtTokenNo = txtTokenNo;
    }
    
    /**
     * Getter for property calFreqAccountList.
     * @return Value of property calFreqAccountList.
     */
    public java.util.List getCalFreqAccountList() {
        return calFreqAccountList;
    }
    
    /**
     * Setter for property calFreqAccountList.
     * @param calFreqAccountList New value of property calFreqAccountList.
     */
    public void setCalFreqAccountList(java.util.List calFreqAccountList) {
        this.calFreqAccountList = calFreqAccountList;
    }
    
}