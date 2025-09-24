/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AuthorizedSignatoryInstructionOB.java
 *
 * Created on March 1, 2005, 10:48 AM
 */

package com.see.truetransact.ui.common.authorizedsignatory;

import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.TableUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.transferobject.common.authorizedsignatory.AuthorizedSignatoryInstructionTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uicomponent.CObservable;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Date;
/**
 *
 * @author  152713
 */
public class AuthorizedSignatoryInstructionOB extends CObservable{
    
    private AuthorizedSignatoryInstructionOB authorizedSignatoryInstructionOB;
    
    private final static Logger log = Logger.getLogger(AuthorizedSignatoryInstructionOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
//    private final   AuthorizedSignatoryRB authSignRB = new AuthorizedSignatoryRB();
    private final   java.util.ResourceBundle authSignRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.common.authorizedsignatory.AuthorizedSignatoryRB", ProxyParameters.LANGUAGE);
    
    public          String  strMaxDelSlNoMapName = "";
    
    private final   String  ALL_VALUES = "ALL_VALUES";
    private final   String  COMMAND = "COMMAND";
    private final   String  FROM_AMOUNT = "FROM_AMOUNT";
    private final   String  INSERT = "INSERT";
    private final   String  INSTRUCTION = "INSTRUCTION";
    private final   String  OPTION = "OPTION";
    private final   String  SLNO = "SLNO";
    private final   String  TABLE_VALUES = "TABLE_VALUES";
    private final   String  TO_AMOUNT = "TO_AMOUNT";
    private final   String  UPDATE = "UPDATE";
    
    private final   ArrayList authorizedInstTabTitle = new ArrayList();     //  Table Title of Authorized Signatory Instruction
    private         ArrayList authorizedInstTabRow;                         //  Single Record of Authorized Instruction Table 
    private         ArrayList authorizeInstTableList;                       //  ArrayList to display in Authorized Instruction Table
    
    private HashMap authorizedInstRec;                                      //  Single Record equivalent to the Authorised Instruction Table
    private LinkedHashMap authorizedInstAll = new LinkedHashMap();          //  List which contains all the values of the Authorized Instruction Table
    
    private EnhancedTableModel tblInstructionTable;
    
    private TableUtil tableUtilAuthorizeInst = new TableUtil();
    
    private String borrowerNo = "";
    private String txtFromAmount = "";
    private String txtToAmount = "";
    private String txtInstruction = "";
    private Date currDt = null;
    /** Creates a new instance of AuthorizedSignatoryInstructionOB */
    public AuthorizedSignatoryInstructionOB(String strModule) {
        authorizedSignatoryInstructionOB(strModule);
    }
    
    private void authorizedSignatoryInstructionOB(String strModule){
        try{
            currDt = ClientUtil.getCurrentDate();
        setAutorizedInstructionTabTitle();
        tableUtilAuthorizeInst.setAttributeKey(SLNO);
        tblInstructionTable = new EnhancedTableModel(null, authorizedInstTabTitle);
        setStrMaxDelSlNoMapName(strModule);
        ttNotifyObservers();
        }catch(Exception e){
            log.info("Exception in authorizedSignatoryInstructionOB(): "+e);
            parseException.logException(e,true);
        }
    }
    
    private void setAutorizedInstructionTabTitle() throws Exception{
        try {
            authorizedInstTabTitle.add(authSignRB.getString("tblColumnAuthInstructionSlNo"));
            authorizedInstTabTitle.add(authSignRB.getString("tblColumnAuthInstructionFromAmt"));
            authorizedInstTabTitle.add(authSignRB.getString("tblColumnAuthInstructionToAmt"));
            authorizedInstTabTitle.add(authSignRB.getString("tblColumnAuthInstruction"));
        }catch(Exception e) {
            log.info("Exception in setAutorizedInstructionTabTitle(): "+e);
            parseException.logException(e,true);
        }
        
    }
    
    public void resetAllInstruction(){
        resetInstructionFields();
        destroyObjects();
        createObject();
        ttNotifyObservers();
    }
    
    public void resetInstructionFields(){
        setTxtFromAmount("");
        setTxtToAmount("");
        setTxtInstruction("");
    }
    
    public void ttNotifyObservers(){
        setChanged();
        notifyObservers();
    }
    
    /**
     * Getter for property strMaxDelSlNoMapName.
     * @return Value of property strMaxDelSlNoMapName.
     */
    public java.lang.String getStrMaxDelSlNoMapName() {
        return strMaxDelSlNoMapName;
    }
    
    /**
     * Setter for property strMaxDelSlNoMapName.
     * @param strMaxDelSlNoMapName New value of property strMaxDelSlNoMapName.
     */
    public void setStrMaxDelSlNoMapName(java.lang.String strMaxDelSlNoMapName) {
        this.strMaxDelSlNoMapName = "getSelectAuthorizedSignatoryInstructionMaxSLNO" + strMaxDelSlNoMapName;
        this.setChanged();
    }
    // Populate Authorized Signatory Instruction Tab
    public void setAuthorizedSignatoryInstructionTO(ArrayList authInstList, String borrowNo){
        log.info("In setAuthorizedSignatoryInstructionTO...");
        try{
            setBorrowerNo(borrowNo);
            ArrayList arrayListRecord;
            ArrayList removedValues = new ArrayList();
            HashMap hashMapRecord;
            LinkedHashMap authorizeAllMap = new LinkedHashMap();
            authorizeInstTableList = new ArrayList();
            AuthorizedSignatoryInstructionTO objAuthorizedSignatoryInstructionTO = null;
            // To retrieve Authorized signatory Instruction records one by one from the Database
            for (int i = authInstList.size() - 1,k = 0;i >= 0;--i,++k){
                arrayListRecord = new ArrayList();
                objAuthorizedSignatoryInstructionTO = (AuthorizedSignatoryInstructionTO) authInstList.get(k);
                
                arrayListRecord.add(CommonUtil.convertObjToStr(objAuthorizedSignatoryInstructionTO.getSlNo()));
                arrayListRecord.add(CommonUtil.convertObjToStr(objAuthorizedSignatoryInstructionTO.getFromAmt()));
                arrayListRecord.add(CommonUtil.convertObjToStr(objAuthorizedSignatoryInstructionTO.getToAmt()));
                arrayListRecord.add(CommonUtil.convertObjToStr(objAuthorizedSignatoryInstructionTO.getInstruction()));

                authorizeInstTableList.add(arrayListRecord);  //  List to be displayed in the Table
                arrayListRecord = null;
                hashMapRecord = new HashMap();

                hashMapRecord.put(SLNO, CommonUtil.convertObjToStr(objAuthorizedSignatoryInstructionTO.getSlNo()));
                hashMapRecord.put(FROM_AMOUNT,CommonUtil.convertObjToStr(objAuthorizedSignatoryInstructionTO.getFromAmt()));
                hashMapRecord.put(TO_AMOUNT,CommonUtil.convertObjToStr(objAuthorizedSignatoryInstructionTO.getToAmt()));
                hashMapRecord.put(INSTRUCTION, objAuthorizedSignatoryInstructionTO.getInstruction());
                hashMapRecord.put(COMMAND, UPDATE);
                
                authorizeAllMap.put(CommonUtil.convertObjToStr(objAuthorizedSignatoryInstructionTO.getSlNo()), hashMapRecord);  //List of values corresponding to the table
                
                objAuthorizedSignatoryInstructionTO = null;
                hashMapRecord = null;
            }
            tblInstructionTable.setDataArrayList(authorizeInstTableList, authorizedInstTabTitle);

            authorizedInstAll.clear();
            authorizedInstAll = authorizeAllMap;
            setMax_Del_AuthSignInst_No(borrowNo);
            arrayListRecord = null;
            hashMapRecord = null;
            authorizeAllMap = null;
            removedValues = null;
            tableUtilAuthorizeInst.setTableValues(authorizeInstTableList);
            tableUtilAuthorizeInst.setAllValues(authorizedInstAll);
            ttNotifyObservers();
        }catch(Exception e){
            log.info("Error In setAuthorizedSignatoryInstructionTO..."+e);
            parseException.logException(e,true);
        }
    }
    
     // Populate Authorized Signatory Instruction Tab
    public void setTermLoanAuthorizedSignatoryInstructionTO(ArrayList authInstList, String borrowNo,String acct_num){
        log.info("In setAuthorizedSignatoryInstructionTO...");
        try{
            setBorrowerNo(borrowNo);
            
            ArrayList arrayListRecord;
            ArrayList removedValues = new ArrayList();
            HashMap hashMapRecord;
            LinkedHashMap authorizeAllMap = new LinkedHashMap();
            authorizeInstTableList = new ArrayList();
            AuthorizedSignatoryInstructionTO objAuthorizedSignatoryInstructionTO = null;
            // To retrieve Authorized signatory Instruction records one by one from the Database
            for (int i = authInstList.size() - 1,k = 0;i >= 0;--i,++k){
                arrayListRecord = new ArrayList();
                objAuthorizedSignatoryInstructionTO = (AuthorizedSignatoryInstructionTO) authInstList.get(k);
                
                arrayListRecord.add(CommonUtil.convertObjToStr(objAuthorizedSignatoryInstructionTO.getSlNo()));
                arrayListRecord.add(CommonUtil.convertObjToStr(objAuthorizedSignatoryInstructionTO.getFromAmt()));
                arrayListRecord.add(CommonUtil.convertObjToStr(objAuthorizedSignatoryInstructionTO.getToAmt()));
                arrayListRecord.add(CommonUtil.convertObjToStr(objAuthorizedSignatoryInstructionTO.getInstruction()));

                authorizeInstTableList.add(arrayListRecord);  //  List to be displayed in the Table
                arrayListRecord = null;
                hashMapRecord = new HashMap();

                hashMapRecord.put(SLNO, CommonUtil.convertObjToStr(objAuthorizedSignatoryInstructionTO.getSlNo()));
                hashMapRecord.put(FROM_AMOUNT,CommonUtil.convertObjToStr(objAuthorizedSignatoryInstructionTO.getFromAmt()));
                hashMapRecord.put(TO_AMOUNT,CommonUtil.convertObjToStr(objAuthorizedSignatoryInstructionTO.getToAmt()));
                hashMapRecord.put(INSTRUCTION, objAuthorizedSignatoryInstructionTO.getInstruction());
                hashMapRecord.put(COMMAND, UPDATE);
                
                authorizeAllMap.put(CommonUtil.convertObjToStr(objAuthorizedSignatoryInstructionTO.getSlNo()), hashMapRecord);  //List of values corresponding to the table
                
                objAuthorizedSignatoryInstructionTO = null;
                hashMapRecord = null;
            }
            tblInstructionTable.setDataArrayList(authorizeInstTableList, authorizedInstTabTitle);

            authorizedInstAll.clear();
            authorizedInstAll = authorizeAllMap;
            setMax_Del_AuthSignInst_No(borrowNo);
            arrayListRecord = null;
            hashMapRecord = null;
            authorizeAllMap = null;
            removedValues = null;
            tableUtilAuthorizeInst.setTableValues(authorizeInstTableList);
            tableUtilAuthorizeInst.setAllValues(authorizedInstAll);
            ttNotifyObservers();
        }catch(Exception e){
            log.info("Error In setAuthorizedSignatoryInstructionTO..."+e);
            parseException.logException(e,true);
        }
    }

    
    private void setMax_Del_AuthSignInst_No(String borrowNo){
        try{
            HashMap transactionMap = new HashMap();
            HashMap retrieve = new HashMap();
            transactionMap.put("borrowNo", borrowNo);
            List resultList = ClientUtil.executeQuery(getStrMaxDelSlNoMapName(), transactionMap);
            if (resultList.size() > 0){
                retrieve = (HashMap) resultList.get(0);
                tableUtilAuthorizeInst.setStr_MAX_DEL_SL_NO(CommonUtil.convertObjToStr(retrieve.get("MAX_AUTHORIZE_INSTRUCTION_NO")));
            }
            retrieve = null;
            transactionMap = null;
            resultList = null;
        }catch(Exception e){
            log.info("Error In setMax_Del_AuthSignInst_No: "+e);
            parseException.logException(e,true);
        }
    }
    public HashMap setAuthorizedSignatoryInstruction(){
        AuthorizedSignatoryInstructionTO objAuthorizedSignatoryInstructionTO;
        HashMap authorizedTOList = new HashMap();
        HashMap temp = new HashMap();
        try{
            java.util.Set keySet =  authorizedInstAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            // To set the values for Authorized Signatory Instruction Transfer Object
            for (int i = authorizedInstAll.size() - 1, j = 0;i >= 0;--i,++j){
                temp = (HashMap) authorizedInstAll.get(objKeySet[j]);
                objAuthorizedSignatoryInstructionTO = new AuthorizedSignatoryInstructionTO();
                objAuthorizedSignatoryInstructionTO.setSlNo(CommonUtil.convertObjToInt(temp.get(SLNO)));
                objAuthorizedSignatoryInstructionTO.setFromAmt(CommonUtil.convertObjToDouble(temp.get(FROM_AMOUNT)));
                objAuthorizedSignatoryInstructionTO.setToAmt(CommonUtil.convertObjToDouble(temp.get(TO_AMOUNT)));
                objAuthorizedSignatoryInstructionTO.setInstruction(CommonUtil.convertObjToStr(temp.get(INSTRUCTION)));
                objAuthorizedSignatoryInstructionTO.setBorrowNo(borrowerNo);
                objAuthorizedSignatoryInstructionTO.setCommand(CommonUtil.convertObjToStr(temp.get(COMMAND)));
                if (temp.get(COMMAND).equals(INSERT)){
                    objAuthorizedSignatoryInstructionTO.setStatus(CommonConstants.STATUS_CREATED);
                }else if (temp.get(COMMAND).equals(UPDATE)){
                    objAuthorizedSignatoryInstructionTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
                objAuthorizedSignatoryInstructionTO.setStatusBy(TrueTransactMain.USER_ID);
                objAuthorizedSignatoryInstructionTO.setStatusDt(currDt);
                authorizedTOList.put(temp.get(SLNO), objAuthorizedSignatoryInstructionTO);
                temp = null;
                objAuthorizedSignatoryInstructionTO = null;
            }
            // To set the values for Authorized Signatory Instruction Transfer Object
            // where as the existing records in Database are deleted in client side
            // useful for updating the status
            for (int i = tableUtilAuthorizeInst.getRemovedValues().size() - 1,j = 0;i >= 0;--i,++j){
                temp = (HashMap) tableUtilAuthorizeInst.getRemovedValues().get(j);
                objAuthorizedSignatoryInstructionTO = new AuthorizedSignatoryInstructionTO();
                objAuthorizedSignatoryInstructionTO.setSlNo(CommonUtil.convertObjToInt(temp.get(SLNO)));
                objAuthorizedSignatoryInstructionTO.setFromAmt(CommonUtil.convertObjToDouble(temp.get(FROM_AMOUNT)));
                objAuthorizedSignatoryInstructionTO.setToAmt(CommonUtil.convertObjToDouble(temp.get(TO_AMOUNT)));
                objAuthorizedSignatoryInstructionTO.setInstruction(CommonUtil.convertObjToStr(temp.get(INSTRUCTION)));
                objAuthorizedSignatoryInstructionTO.setBorrowNo(borrowerNo);
                objAuthorizedSignatoryInstructionTO.setCommand(CommonUtil.convertObjToStr( temp.get(COMMAND)));
                objAuthorizedSignatoryInstructionTO.setStatus(CommonConstants.STATUS_DELETED);
                objAuthorizedSignatoryInstructionTO.setStatusBy(TrueTransactMain.USER_ID);
                objAuthorizedSignatoryInstructionTO.setStatusDt(currDt);
                authorizedTOList.put(temp.get(SLNO), objAuthorizedSignatoryInstructionTO);
                temp = null;
                objAuthorizedSignatoryInstructionTO = null;
            }
            
        }catch(Exception e){
            log.info("Error In setAuthorizedSignatoryInstruction: "+e);
            parseException.logException(e,true);
        }
        return authorizedTOList;
    }
    
    public void changeStatusAuthorizedSignatoryInstruction(int resultType){
        try{
            if (resultType != 2){
                //If the Main Save Button pressed
                tableUtilAuthorizeInst.getRemovedValues().clear();
            }
            java.util.Set keySet =  authorizedInstAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            HashMap oneRecord;
            // To change the Insert command to Update after Save Buttone Pressed
            // For Authorized Details
            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) authorizedInstAll.get(objKeySet[j]);
                if (oneRecord.get(COMMAND).equals(INSERT)){
                    // If the status is in Insert Mode then change the mode to Update
                    oneRecord.put(COMMAND, UPDATE);
                    authorizedInstAll.put(objKeySet[j], oneRecord);
                }
                oneRecord = null;
            }

            tableUtilAuthorizeInst.setAllValues(authorizedInstAll);
            keySet = null;
            objKeySet = null;
            ttNotifyObservers();
        }catch(Exception e){
            log.info("Error In changeStatusAuthorizedSignatoryInstruction: "+e);
            parseException.logException(e,true);
        }
    }
    
    public void setBorrowerNo(String borrowerNo){
        this.borrowerNo = borrowerNo;
        setChanged();
    }
    
    public String getBorrowerNo(){
        return this.borrowerNo;
    }
    
    /**
     * Getter for property txtFromAmount.
     * @return Value of property txtFromAmount.
     */
    public java.lang.String getTxtFromAmount() {
        return txtFromAmount;
    }
    
    /**
     * Setter for property txtFromAmount.
     * @param txtFromAmount New value of property txtFromAmount.
     */
    public void setTxtFromAmount(java.lang.String txtFromAmount) {
        setChanged();
        this.txtFromAmount = txtFromAmount;
    }
    
    /**
     * Getter for property txtToAmount.
     * @return Value of property txtToAmount.
     */
    public java.lang.String getTxtToAmount() {
        return txtToAmount;
    }
    
    /**
     * Setter for property txtToAmount.
     * @param txtToAmount New value of property txtToAmount.
     */
    public void setTxtToAmount(java.lang.String txtToAmount) {
        setChanged();
        this.txtToAmount = txtToAmount;
    }
    
    /**
     * Getter for property txtInstruction.
     * @return Value of property txtInstruction.
     */
    public java.lang.String getTxtInstruction() {
        return txtInstruction;
    }
    
    /**
     * Setter for property txtInstruction.
     * @param txtInstruction New value of property txtInstruction.
     */
    public void setTxtInstruction(java.lang.String txtInstruction) {
        setChanged();
        this.txtInstruction = txtInstruction;
    }
    
    // ADD, UPDATE Records in Authorized Instruction Table
    public int addAuthorizedInstTab(int row, boolean update){ 
        int option = -1;
        try{
            authorizedInstTabRow = new ArrayList();
            authorizedInstRec = new HashMap();
            HashMap result = new HashMap();
            ArrayList data = tblInstructionTable.getDataArrayList();
            tblInstructionTable.setDataArrayList(data, authorizedInstTabTitle);
            final int dataSize = data.size();
            boolean exist = false;
            boolean found = false;
            
            insertAuthorizedInstRecord(dataSize+1);
            if (!update){
                // If the table is not in Edit Mode
                result = tableUtilAuthorizeInst.insertTableValues(authorizedInstTabRow, authorizedInstRec);
                authorizeInstTableList = (ArrayList) result.get(TABLE_VALUES);
                authorizedInstAll = (LinkedHashMap) result.get(ALL_VALUES);
                option = CommonUtil.convertObjToInt(result.get(OPTION));
                tblInstructionTable.setDataArrayList(authorizeInstTableList,authorizedInstTabTitle);
            }else{
                option = updateAuthorizedInstTab(row);
            }
            ttNotifyObservers();
            authorizedInstRec = null;
            authorizedInstTabRow = null;
            data = null;
            result = null;
        }catch(Exception e){
            log.info("in addAuthorizedInstTab: "+e);
            parseException.logException(e,true);
        }
        return option;
    }
    
    // Details to be inserted or updated in Authorized Signatory Instruction Table
    private void insertAuthorizedInstRecord(int slno){
        //TO INSERT RECORD FOR AUTHORIZED SIGNATORY INSTRUCTION TABLE
        try{
            authorizedInstTabRow.add(String.valueOf(slno));
            authorizedInstTabRow.add(getTxtFromAmount());
            authorizedInstTabRow.add(getTxtToAmount());
            authorizedInstTabRow.add(getTxtInstruction());
            
            authorizedInstRec.put(SLNO,String.valueOf(slno));
            authorizedInstRec.put(FROM_AMOUNT,getTxtFromAmount());
            authorizedInstRec.put(TO_AMOUNT, getTxtToAmount());
            authorizedInstRec.put(INSTRUCTION, getTxtInstruction());
            authorizedInstRec.put(COMMAND,"");
        }catch(Exception e){
            log.info("in insertAuthorizedInstRecord(): "+e);
            parseException.logException(e,true);
        }
    }
    
    // TO POPULATE CORRESPONDING RECORDS IN UI
    public void populateAuthorizeInstTab(int row){
        log.info("In populateAuthorizeTab...");
        try{
            ArrayList authorizedInstTableValue = (ArrayList)tblInstructionTable.getDataArrayList().get(row);
            HashMap authorizedInstOneRecord = new HashMap();
            java.util.Set keySet =  authorizedInstAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            // To populate the corresponding record from the Authorized Signatory Instruction HashMap
            for (int i = authorizedInstAll.size() - 1,j = 0;i >= 0 ;i--,++j){
                authorizedInstOneRecord = (HashMap)authorizedInstAll.get(objKeySet[j]);
                if ((authorizedInstTableValue.get(0)).equals(authorizedInstOneRecord.get(SLNO))){
                    // To populate the Corresponding record from CTable
                    setTxtFromAmount(CommonUtil.convertObjToStr(authorizedInstOneRecord.get(FROM_AMOUNT)));
                    setTxtToAmount(CommonUtil.convertObjToStr(authorizedInstOneRecord.get(TO_AMOUNT)));
                    setTxtInstruction(CommonUtil.convertObjToStr(authorizedInstOneRecord.get(INSTRUCTION)));
                    setChanged();
                    ttNotifyObservers();
                }
            }
            authorizedInstOneRecord = null;
            authorizedInstTableValue = null;
            keySet = null;
            objKeySet = null;
        }catch(Exception e){
            log.info("in populateAuthorizeInstTab(): "+e);
            parseException.logException(e,true);
        }
    }
    
    // TO UPDATE AUTHORIZED SIGNATORY INSTRUCTION TABLE (row is the corresponding Serial Number)
    private int updateAuthorizedInstTab(int row){
        int option = -1;
        HashMap result = new HashMap();
        try{
        result = tableUtilAuthorizeInst.updateTableValues(authorizedInstTabRow, authorizedInstRec, row);
        authorizeInstTableList = (ArrayList) result.get(TABLE_VALUES);
        authorizedInstAll = (LinkedHashMap) result.get(ALL_VALUES);
        option = CommonUtil.convertObjToInt(result.get(OPTION));
        
        tblInstructionTable.setDataArrayList(authorizeInstTableList, authorizedInstTabTitle);
        
        result = null;
        }catch(Exception e){
            log.info("Exception caught in updateAuthorizedInstTab: "+e);
            parseException.logException(e,true);
        }
        ttNotifyObservers();
        return option;
    }
    
    // Delete for the Table Authorized Signatory Instruction. (row is the corresponding Serial Number)
    public void deleteAuthorizedInstTab(int row){
        
        try{
            HashMap result = new HashMap();
            
            result = tableUtilAuthorizeInst.deleteTableValues(row);
            authorizeInstTableList = (ArrayList) result.get(TABLE_VALUES);
            tblInstructionTable.setDataArrayList(authorizeInstTableList,authorizedInstTabTitle);
            authorizedInstAll = (LinkedHashMap) result.get(ALL_VALUES);
            
            result = null;
            ttNotifyObservers();
        }catch(Exception e){
            log.info("Exception caught in deleteAuthorizedInstTab: "+e);
            parseException.logException(e,true);
        }
    }
    
    // To create objects
    public void createObject(){
        authorizedInstAll = new LinkedHashMap();
        authorizeInstTableList = new ArrayList();
        
        tblInstructionTable = new EnhancedTableModel(null, authorizedInstTabTitle);
        tableUtilAuthorizeInst = new TableUtil();
        tableUtilAuthorizeInst.setAttributeKey(SLNO);
        
    }
    
    // To destroy Objects
    public void destroyObjects(){
        tblInstructionTable = null;
        authorizedInstTabRow = null;
        authorizedInstRec = null;
        authorizedInstAll = null;
        authorizeInstTableList = null;
        tableUtilAuthorizeInst = null;
    }
    
    /**
     * Getter for property tblInstructionTable.
     * @return Value of property tblInstructionTable.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblInstructionTable() {
        return tblInstructionTable;
    }
    
    /**
     * Setter for property tblInstructionTable.
     * @param tblInstructionTable New value of property tblInstructionTable.
     */
    public void setTblInstructionTable(com.see.truetransact.clientutil.EnhancedTableModel tblInstructionTable) {
        this.tblInstructionTable = tblInstructionTable;
        setChanged();
    }
    
}
