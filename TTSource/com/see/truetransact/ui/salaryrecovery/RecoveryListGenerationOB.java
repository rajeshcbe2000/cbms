/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * RecoveryListGenerationOB.java
 *
 * 
 */

package com.see.truetransact.ui.salaryrecovery;

import java.util.*;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;


/**
 *
 * @author Suresh
 */

public class RecoveryListGenerationOB extends CObservable{
    final ArrayList tableTitle = new ArrayList();
    private EnhancedTableModel tblSalaryRecoveryList;
    private ProxyFactory proxy;
    private HashMap map;
    private final static Logger log = Logger.getLogger(RecoveryListGenerationOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private int result;
    private int actionType;
    private List finalList = null;
    private HashMap finalMap = new HashMap();
    private String tdtCalcIntUpto = "";
   // private String tdtCalcIntUpto = "";
    private ComboBoxModel cbmProdType;
    private String cboProdType = "";
    private ComboBoxModel cbmProdId;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private String txtAccountNo = "";
    private Date currDate = null;
    private boolean chkDE = false;
    private HashMap loanDetailsMap = new HashMap();

    
    public RecoveryListGenerationOB() {
        try {
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "SalaryRecoveryListJNDI");
            map.put(CommonConstants.HOME, "salaryrecovery.RecoveryListGenerationHome");
            map.put(CommonConstants.REMOTE, "salaryrecovery.RecoveryListGeneration");
            fillDropdown();
            currDate = ClientUtil.getCurrentDate();
            setDepositInterestTableTitle();
            tblSalaryRecoveryList = new EnhancedTableModel(null, tableTitle);
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
       /** A method to set the combo box values */
    private void fillDropdown() throws Exception{
        try{
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME,null);
            final ArrayList lookup_keys = new ArrayList();
            lookup_keys.add("PRODUCTTYPE");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get("PRODUCTTYPE"));
            cbmProdType = new ComboBoxModel(key,value);
            cbmProdType.removeKeyAndElement("TD");
            cbmProdType.removeKeyAndElement("TL");
            cbmProdId = new ComboBoxModel();
            keyValue = null;
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    public void setCbmProdId(String prodType) {
        if (prodType.equals("GL")) {
            key = new ArrayList();
            value = new ArrayList();
        } else {
            try {
                lookUpHash = new HashMap();
                lookUpHash.put(CommonConstants.MAP_NAME,"Cash.getAccProduct" + prodType);
                lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                keyValue = ClientUtil.populateLookupData(lookUpHash);
                getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        cbmProdId = new ComboBoxModel(key,value);
        this.cbmProdId = cbmProdId;
        setChanged();
    }
    
    public void setDepositInterestTableTitle(){
        tableTitle.add("SNo.");
        tableTitle.add("Emp Ref.No.");
        tableTitle.add("Member Name");
        tableTitle.add("Scheme Name");
        tableTitle.add("A/c No.");
        tableTitle.add("Total Demand");
        tableTitle.add("Principal");
        tableTitle.add("Interest");
        tableTitle.add("Penal Int");
        tableTitle.add("Charges");
        tableTitle.add("Clear Balance");
        tableTitle.add("Omit Principal"); // To be tested
        tableTitle.add("Omit Interest"); // To be tested
    }
    
    public void insertTableData(){
        try{
            ArrayList rowList = new ArrayList();
            ArrayList tableList = new ArrayList();
            HashMap dataMap = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("CALC_INT_UPTO_DT", DateUtil.getDateMMDDYYYY(getTdtCalcIntUpto()));
          if(getLoanDetailsMap()!=null && getLoanDetailsMap().size()>0){
               whereMap.put("OMIT_DETAILS",getLoanDetailsMap()) ;
            }
           if (isChkDE() == true) {
                whereMap.put("DE_STATUS", isChkDE());
            }
            HashMap proxyResultMap = proxy.executeQuery(whereMap, map);
            System.out.println("!@!@ proxyResultMap : "+proxyResultMap);
            if (proxyResultMap != null && proxyResultMap.size()>0) {
                System.out.println("!@!@ RECOVERY_LIST_TABLE_DATA : "+proxyResultMap.get("RECOVERY_LIST_TABLE_DATA"));
                tableList = (ArrayList) proxyResultMap.get("RECOVERY_LIST_TABLE_DATA");
                setFinalList(tableList);
            }
            System.out.println("#$# tableList:"+tableList);
            tblSalaryRecoveryList= new EnhancedTableModel((ArrayList)tableList, tableTitle);
        }catch(Exception e){
            e.printStackTrace();
            parseException.logException(e,true);
        }
    }
    public void viewTblData(ArrayList viewtblList){
          tblSalaryRecoveryList= new EnhancedTableModel((ArrayList)viewtblList, tableTitle);
        
    }
public HashMap getLoanDetailsMap() {
        return loanDetailsMap;
    }

    public void setLoanDetailsMap(HashMap loanDetailsMap) {
        this.loanDetailsMap = loanDetailsMap;
    }
    
    
    
    /** To perform the necessary operation */
    public void doAction() {
        TTException exception = null;
        log.info("In doAction()");
        try {
            doActionPerform();
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
    public void doInsert(HashMap insMap,int no){
        final HashMap data1 = new HashMap();
        try{
            if(no==1){
                data1.put("INSMAP",insMap);
                data1.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                data1.put("INSERT","INSERT");
                System.out.println("Data in RecoveryList Generation OB 2 : " + data1);  
            }
            else if(no==2){
                data1.put("INSMAP",insMap);
                data1.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                data1.put("UPDATE","UPDATE");
            }
            else if(no==3){
                data1.put("INSMAP",insMap);
                data1.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                data1.put("DELETE","DELETE");
            }
            HashMap proxyResultMap = proxy.execute(data1, map);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        final HashMap data = new HashMap();
        data.put("CALC_INT_UPTO_DT", DateUtil.getDateMMDDYYYY(getTdtCalcIntUpto()));
        data.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
        System.out.println("Data in RecoveryList Generation OB : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
        setResult(getActionType());
    }
    
    public void resetForm(){
        setChkDE(false);
        resetTableValues();
        setChanged();
       setLoanDetailsMap(null);
    }
     
     public void resetTableValues(){
        tblSalaryRecoveryList.setDataArrayList(null,tableTitle);
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
     * Getter for property tblSalaryRecoveryList.
     * @return Value of property tblSalaryRecoveryList.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblSalaryRecoveryList() {
        return tblSalaryRecoveryList;
    }
    
    /**
     * Setter for property tblSalaryRecoveryList.
     * @param tblSalaryRecoveryList New value of property tblSalaryRecoveryList.
     */
    public void setTblSalaryRecoveryList(com.see.truetransact.clientutil.EnhancedTableModel tblSalaryRecoveryList) {
        this.tblSalaryRecoveryList = tblSalaryRecoveryList;
    }
    
    /**
     * Getter for property tdtCalcIntUpto.
     * @return Value of property tdtCalcIntUpto.
     */
    public java.lang.String getTdtCalcIntUpto() {
        return tdtCalcIntUpto;
    }
    
    /**
     * Setter for property tdtCalcIntUpto.
     * @param tdtCalcIntUpto New value of property tdtCalcIntUpto.
     */
    public void setTdtCalcIntUpto(java.lang.String tdtCalcIntUpto) {
        this.tdtCalcIntUpto = tdtCalcIntUpto;
    }
    
    /**
     * Getter for property finalMap.
     * @return Value of property finalMap.
     */
    public java.util.HashMap getFinalMap() {
        return finalMap;
    }
    
    /**
     * Setter for property finalMap.
     * @param finalMap New value of property finalMap.
     */
    public void setFinalMap(java.util.HashMap finalMap) {
        this.finalMap = finalMap;
    }
      public java.lang.String getCboProdType() {
        return cboProdType;
    }
    
    /**
     * Setter for property cboProdType.
     * @param cboProdType New value of property cboProdType.
     */
    public void setCboProdType(java.lang.String cboProdType) {
        this.cboProdType = cboProdType;
    }
    
    /**
     * Getter for property cbmProdId.
     * @return Value of property cbmProdId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdId() {
        return cbmProdId;
    }
    
    /**
     * Setter for property cbmProdId.
     * @param cbmProdId New value of property cbmProdId.
     */
    public void setCbmProdId(com.see.truetransact.clientutil.ComboBoxModel cbmProdId) {
        this.cbmProdId = cbmProdId;
    }
    
    /**
     * Getter for property cbmProdType.
     * @return Value of property cbmProdType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdType() {
        return cbmProdType;
    }
    
    /**
     * Setter for property cbmProdType.
     * @param cbmProdType New value of property cbmProdType.
     */
    public void setCbmProdType(com.see.truetransact.clientutil.ComboBoxModel cbmProdType) {
        this.cbmProdType = cbmProdType;
    }
    
    /**
     * Getter for property txtAccountNo.
     * @return Value of property txtAccountNo.
     */
    public java.lang.String getTxtAccountNo() {
        return txtAccountNo;
    }
    
    /**
     * Setter for property txtAccountNo.
     * @param txtAccountNo New value of property txtAccountNo.
     */
    public void setTxtAccountNo(java.lang.String txtAccountNo) {
        this.txtAccountNo = txtAccountNo;
    }
    
    public boolean isChkDE() {
        return chkDE;
    }

    public void setChkDE(boolean chkDE) {
        this.chkDE = chkDE;
    }
    
    
    /**
     * Getter for property transactionOB.
     * @return Value of property transactionOB.
     */
   
    
}