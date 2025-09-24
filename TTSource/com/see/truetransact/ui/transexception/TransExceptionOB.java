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

package com.see.truetransact.ui.transexception;


import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.apache.log4j.Logger;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.TrueTransactMain;
/**
 *
 * @author
 */

public class TransExceptionOB extends CObservable{
    
    
    
    final ArrayList tableTitle = new ArrayList();
    private ArrayList IncVal = new ArrayList();
    
    private EnhancedTableModel tblTransExceptionDetails;
    private ProxyFactory proxy;
    private HashMap map;
    private final static Logger log = Logger.getLogger(TransExceptionOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private int result;
    private int actionType;
    private List finalList = null;
    private List finalTableList = null;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    
    private String txtProductID = "";
    private String txtTokenNo = "";
    private List calFreqAccountList = null;
    private List clkNoList=null;
    private HashMap mapData=null;
     private HashMap finalMap=null;
     private String lblStatus=null;
    private LinkedHashMap allowedTransactionDetailsTO = null;
    private LinkedHashMap transactionDetailsTO = null;
    private LinkedHashMap deletedTransactionDetailsTO = null;
    public TransExceptionOB() {
        try {
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "TransExceptionJNDI");
            map.put(CommonConstants.HOME, "TransExceptionHome");
            map.put(CommonConstants.REMOTE, "TransException");
            setTransExceptionTableTitle();
            tblTransExceptionDetails = new EnhancedTableModel(null, tableTitle);
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    public void setTransExceptionTableTitle(){
        tableTitle.add("Select");
        tableTitle.add("Cust ID");
        tableTitle.add("Clock_No ");
        tableTitle.add("Name");
        tableTitle.add("Product Type");
        tableTitle.add("Product Desc");
        tableTitle.add("Account No");
        tableTitle.add("Amount");
        tableTitle.add("No of Instmnts");
        tableTitle.add("Principal");
        tableTitle.add("Intrest");
        tableTitle.add("Penal");
        tableTitle.add("Bonus");
        tableTitle.add("Charges");
        tableTitle.add("Prod Id");
		tableTitle.add("Total Amt");
        tableTitle.add("Clear Balance");
        IncVal = new ArrayList();
    }
 
      public void populateData(HashMap whereMap) {
            mapData=null;
        try {
            mapData=new HashMap();
            System.out.println("whereMap=="+whereMap);
            System.out.println("map=="+map);
            mapData = proxy.executeQuery(whereMap, map);
            System.out.println("objmapData=="+ mapData);
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
            
        }
       
    }
   
    public void insertTableData(HashMap whereMap){
        try{
            ArrayList rowList = new ArrayList();
           ArrayList tableList = new ArrayList();
            HashMap dataMap = new HashMap();
            
           // HashMap proxyResultMap = proxy.executeQuery(whereMap, map);
            if (whereMap != null && whereMap.size()>0) {
                System.out.println("size of map in ob"+whereMap.size());
                System.out.println("!@!@ INSERT_DATA : "+whereMap.get("INSERT"));
                tableList = (ArrayList) whereMap.get("INSERT");
                setFinalList(tableList);
                //displayTable(tableList);
            }
                System.out.println("#$# tableList:"+tableList);
                tblTransExceptionDetails= new EnhancedTableModel((ArrayList)tableList, tableTitle);
                    
                
//            }
        }catch(Exception e){
            e.printStackTrace();
            parseException.logException(e,true);
        }
    }
    
    
//    public void displayTable(ArrayList list){
//        for(int i=0;i<list.size();i++){
//           List newList=null;
//            newList=(List)list.get(i);
//            System.out.println("newList is"+newList);
//            String j=newList.get(0).toString();
//             System.out.println("newList j is"+j);
//        }
//    }
    
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

    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        final HashMap term = new HashMap();
            term.put(CommonConstants.MODULE, getModule());
            term.put(CommonConstants.SCREEN, getScreen());
            term.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
            term.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            term.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
             if(getFinalMap() != null && getFinalMap().size()>0){
                    term.put("RECOVERY_PROCESS_LIST",getFinalMap());
                }
            term.put("INSERT",getFinalList());
        System.out.println("Data in transException OB : " + term);
        HashMap proxyResultMap = proxy.execute(term, map);
        setProxyReturnMap(proxyResultMap);
        setResult(getActionType());
    }

    public void resetForm(){
        resetTableValues();
        setChanged();
    }
     
     public void resetTableValues(){
        tblTransExceptionDetails.setDataArrayList(null,tableTitle);
    }
    
    
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
    
   
    public EnhancedTableModel getTblTransExceptionDetails() {
        return tblTransExceptionDetails;
    }
    
    /**
     * Setter for property tblTransExceptionDetails.
     * @param tblTransExceptionDetails New value of property tblTransExceptionDetails.
     */
    public void setTblTransExceptionDetails(EnhancedTableModel tblTransExceptionDetails) {
        this.tblTransExceptionDetails = tblTransExceptionDetails;
    }
    
    /**
     * Getter for property clkNoList.
     * @return Value of property clkNoList.
     */
    public List getClkNoList() {
        return clkNoList;
    }
    
    /**
     * Setter for property clkNoList.
     * @param clkNoList New value of property clkNoList.
     */
    public void setClkNoList(List clkNoList) {
        this.clkNoList = clkNoList;
    }
    
    /**
     * Getter for property mapData.
     * @return Value of property mapData.
     */
    public HashMap getMapData() {
        return mapData;
    }
    
    /**
     * Setter for property mapData.
     * @param mapData New value of property mapData.
     */
    public void setMapData(HashMap mapData) {
        this.mapData = mapData;
    }
    
    /**
     * Getter for property finalMap.
     * @return Value of property finalMap.
     */
    public HashMap getFinalMap() {
        return finalMap;
    }
    
    /**
     * Setter for property finalMap.
     * @param finalMap New value of property finalMap.
     */
    public void setFinalMap(HashMap finalMap) {
        this.finalMap = finalMap;
    }
    
    /**
     * Getter for property lblStatus.
     * @return Value of property lblStatus.
     */
    public String getLblStatus() {
        return lblStatus;
    }
    
    /**
     * Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     */
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
    }
    
}