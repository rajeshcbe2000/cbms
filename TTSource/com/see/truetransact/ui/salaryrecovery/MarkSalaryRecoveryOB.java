/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * MarkSalaryRecoveryOB.java
 *
 * 
 */

package com.see.truetransact.ui.salaryrecovery;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientexception.ClientParseException;

/**
 *
 * @author Suresh
 */

public class MarkSalaryRecoveryOB extends CObservable{
    private int result;
    private HashMap map;
    private int actionType;
    private ProxyFactory proxy;
    private List finalList = null;
    private HashMap finalMap = new HashMap();
    final ArrayList tableTitle = new ArrayList();
    private EnhancedTableModel tblSalaryRecoveryList;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    public MarkSalaryRecoveryOB() {
        try {
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "SalaryRecoveryListJNDI");
            map.put(CommonConstants.HOME, "salaryrecovery.RecoveryListGenerationHome");
            map.put(CommonConstants.REMOTE, "salaryrecovery.RecoveryListGeneration");
            setDepositInterestTableTitle();
            tblSalaryRecoveryList = new EnhancedTableModel(null, tableTitle);
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    public void setDepositInterestTableTitle(){
        tableTitle.add("Act Number");
        tableTitle.add("Prod Type");
        tableTitle.add("Prod ID");
          tableTitle.add("Prod Desc");
        tableTitle.add("Salary Recovery Yes/No");
    }
    
    public void insertTableData(HashMap hash){
        try{
            HashMap dataMap = new HashMap();
            ArrayList rowList = new ArrayList();
            ArrayList tableList = new ArrayList();
            List recoveryList = ClientUtil.executeQuery("getSalaryRecoveryList", hash);
            if(recoveryList!=null && recoveryList.size()>0){
                for (int i = 0;i<recoveryList.size();i++){
                    dataMap = (HashMap) recoveryList.get(i);
                    rowList = new ArrayList();
                    rowList.add(dataMap.get("ACT_NUM"));
                    rowList.add(dataMap.get("PROD_TYPE"));
                    rowList.add(dataMap.get("PROD_ID"));
                      rowList.add(dataMap.get("PROD_DESC"));
                    rowList.add(new Boolean(false));
                    tableList.add(rowList);
                }
                tblSalaryRecoveryList= new EnhancedTableModel((ArrayList)tableList, tableTitle);
            }
        }catch(Exception e){
            e.printStackTrace();
            parseException.logException(e,true);
        }
    }
    
    public void resetForm(){
        resetTableValues();
        setChanged();
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
    
}