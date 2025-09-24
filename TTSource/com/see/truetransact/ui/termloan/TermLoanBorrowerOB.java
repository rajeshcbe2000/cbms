/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanBorrowerOB.java
 *
 * Created on July 7, 2004, 11:58 AM
 */

package com.see.truetransact.ui.termloan;

/**
 *
 * @author  shanmuga
 *
 */

import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.JointAcctHolderManipulation;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.transferobject.termloan.TermLoanBorrowerTO;
import com.see.truetransact.transferobject.termloan.TermLoanJointAcctTO;
import com.see.truetransact.ui.common.authorizedsignatory.AuthorizedSignatoryOB;
import com.see.truetransact.ui.common.powerofattorney.PowerOfAttorneyOB;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uicomponent.COptionPane;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class TermLoanBorrowerOB extends CObservable{
    
    /**
     * Creates a new instance of TermLoanBorrowerOB
     * @throws Exception exception
     */
    private TermLoanBorrowerOB() throws Exception{
        termLoanBorrowerOB();
    }
    
    private final   String    AREA_CODE = "AREA_CODE";
    private final   String    BRANCH_CURRENT_DT = "BRANCH_CURRENT_DT";
    private final   String    CITY = "CITY";
    private final   String    COMMAND = "COMMAND";
    private final   String    CORPORATE = "CORPORATE";
    private final   String    CUST_ID = "CUST_ID";
    private final   String    FAX = "FAX";
    private final   String    FLD_FOR_DB_YES_NO = "DBYesOrNo";
    private final   String    INDUVIDUAL = "INDUVIDUAL";
    private final   String    INDIVIDUAL = "INDIVIDUAL";
    private final   String    JOINT_ACCOUNT = "JOINT_ACCOUNT";
    private final   String    PHONE_NUMBER = "PHONE_NUMBER";
    private final   String    PIN_CODE = "PIN_CODE";
    private final   String    STATE = "STATE";
    private final   String    STATUS = "STATUS";
    
    private       static JointAcctHolderManipulation objJointAcctHolderManipulation;
    private       static TermLoanBorrowerOB termLoanBorrowerOB;
    private       static AuthorizedSignatoryOB termLoanAuthorizedSignatoryOB;
    private       static PowerOfAttorneyOB termLoanPoAOB;
    
    private final static Logger log = Logger.getLogger(TermLoanBorrowerOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    private final   java.util.ResourceBundle objTermLoanRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.TermLoanRB", ProxyParameters.LANGUAGE);
    
//    private final   TermLoanRB objTermLoanRB = new TermLoanRB();
    
    private final   ArrayList borrowerTabTitle = new ArrayList();       //  Table Title of Borrower
    
    private EnhancedTableModel tblBorrowerTab;
    
    private LinkedHashMap jntAcctAll = new LinkedHashMap();            // All Joint Account Details
    
    private ComboBoxModel cbmCategory;
    private ComboBoxModel cbmConstitution;
    
    private String lblOpenDate2 = "";
    private String lblCustName_2 = "";
    private String lblCity_BorrowerProfile_2 = "";
    private String lblFax_BorrowerProfile_2 = "";
    private String lblState_BorrowerProfile_2 = "";
    private String lblPin_BorrowerProfile_2 = "";
    private String lblPhone_BorrowerProfile_2 = "";
    private String txtCustID = "";
    private String tdtOpenDate = "";
    private String cboConstitution = "";
    private String txtApplicationNo = "";
    private String cboCategory = "";
    private String txtReferences = "";
    private String lblBorrowerNo_2 = "";
    public boolean btnPressed = false;
    private String txtSHGId = "";
    
    private void termLoanBorrowerOB() throws Exception{
        setBorrowerTabTitle();
        tblBorrowerTab = new EnhancedTableModel(null, borrowerTabTitle);
        objJointAcctHolderManipulation  = new JointAcctHolderManipulation();
    }
    
    static {
        try {
            termLoanBorrowerOB = new TermLoanBorrowerOB();
        } catch(Exception e) {
            log.info("try: " + e);
            parseException.logException(e,true);
            e.printStackTrace();
        }
    }
    
    public void setAuthSignAndPoAOB(AuthorizedSignatoryOB authSignOB, PowerOfAttorneyOB poaOB){
        try{
            termLoanAuthorizedSignatoryOB = authSignOB;
            termLoanPoAOB = poaOB;
        }catch(Exception e){
            log.info("setAuthSignAndPoAOB: " + e);
            parseException.logException(e,true);
        }
    }
    /**
     * To create the singleton object TermLoanBorrowerOB
     * @return the instance of TermLoanBorrowerOB
     */
    public static TermLoanBorrowerOB getInstance() {
        return termLoanBorrowerOB;
    }
    
    /**
     * Reset all the fields in Borrower Tab
     */
    public void resetBorrowerDetails(){
        resetBorrowerTabCustomer();
        setTxtCustID("");
        setLblBorrowerNo_2("");
        setCboConstitution("");
        setCboCategory("");
        setTxtReferences("");
        setTxtSHGId("");
        setTxtApplicationNo("");
    }
    
    /**
     * Reset the Customer details table, in the Borrower Tab
     */
    public void resetBorrowerTabCTable(){
        tblBorrowerTab = new EnhancedTableModel(null, borrowerTabTitle);
    }
    
    private void setBorrowerTabTitle() throws Exception{
        try {
            borrowerTabTitle.add(objTermLoanRB.getString("tblColumnBorrowerName"));
            borrowerTabTitle.add(objTermLoanRB.getString("tblColumnBorrowerCustID"));
            borrowerTabTitle.add(objTermLoanRB.getString("tblColumnBorrowerType"));
            borrowerTabTitle.add(objTermLoanRB.getString("tblColumnBorrowerMain/Joint"));
        }catch(Exception e) {
            log.info("Exception in setBorrowerTabTitle()..."+e);
            parseException.logException(e,true);
        }
    }
    
    /**
     * Reset only the Customer details in the Borrower Tab
     */
    public void resetBorrowerTabCustomer(){
        setLblOpenDate("");
        setLblCustName("");
        setLblCity("");
        setLblState("");
        setLblPin("");
        setLblPhone("");
        setLblFax("");
    }
    
    public void changeStatusJointAcct(){
        java.util.Set keySet =  jntAcctAll.keySet();
        Object[] objKeySet = (Object[]) keySet.toArray();
        HashMap oneRecord;
        // To change the Insert command to Update after Save Buttone Pressed
        // For Joint Account Details
        for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
            oneRecord = (HashMap) jntAcctAll.get(objKeySet[j]);
            if (oneRecord.get(STATUS).equals(CommonConstants.STATUS_CREATED)){
                // If the status is in Insert Mode then change the mode to Update
                oneRecord.put(STATUS, CommonConstants.STATUS_MODIFIED);
                jntAcctAll.put(objKeySet[j], oneRecord);
            }
            oneRecord = null;
        }
        keySet = null;
        objKeySet = null;
    }
    
    // Populate the Borrower Details Tab
    public void setTermLoanBorrowerTO(TermLoanBorrowerTO objTermLoanBorrowerTO) {
        log.info("In setTermLoanBorrowerTO...");
        // Remove all keys and values before add
        for (int i = termLoanPoAOB.getCbmPoACust().getSize() - 1;i >= 0;--i){
            termLoanPoAOB.getCbmPoACust().removeKeyAndElement(termLoanPoAOB.getCbmPoACust().getKey(i));
        }
        // To add the Customer ID in ComboBoxModel in PoA Details
        if (!termLoanPoAOB.getCbmPoACust().containsElement("")){
            termLoanPoAOB.getCbmPoACust().addKeyAndElement("", "");
        }
        String strCust = termLoanPoAOB.getCustName(objTermLoanBorrowerTO.getCustId());
        if (!termLoanPoAOB.getCbmPoACust().containsElement(strCust)){
            termLoanPoAOB.getCbmPoACust().addKeyAndElement(objTermLoanBorrowerTO.getCustId(), strCust);
        }
        setTxtCustID(objTermLoanBorrowerTO.getCustId());
        setLblBorrowerNo_2(objTermLoanBorrowerTO.getBorrowNo());
        setCboConstitution(CommonUtil.convertObjToStr(getCbmConstitution().getDataForKey(objTermLoanBorrowerTO.getConstitution())));
        setCboCategory(CommonUtil.convertObjToStr(getCbmCategory().getDataForKey(objTermLoanBorrowerTO.getCategory())));
        setTxtReferences(objTermLoanBorrowerTO.getReferences());
        setStatusBy(objTermLoanBorrowerTO.getStatusBy());
        setAuthorizeStatus(objTermLoanBorrowerTO.getAuthorizeStatus());
        setTxtSHGId(objTermLoanBorrowerTO.getShgID());
        setTxtApplicationNo(objTermLoanBorrowerTO.getApplicationNo());
        strCust = null;
    }
    
    // Populate the Joint Account Details
    public void setTermLoanJointAcctTO(ArrayList jntAcctList){
        TermLoanJointAcctTO termLoanJointAcctTO;
        HashMap jntRecordMap;
        List custListData;
        HashMap custMapData;
        LinkedHashMap allJntRecords = new LinkedHashMap();
        String strCust = null;
        // To retrieve the Interest Details from the Serverside
        for (int i = 0;i < jntAcctList.size();++i){
            termLoanJointAcctTO = (TermLoanJointAcctTO) jntAcctList.get(i);
            jntRecordMap = new HashMap();
            jntRecordMap.put(CUST_ID, CommonUtil.convertObjToStr(termLoanJointAcctTO.getCustId()));
            jntRecordMap.put(FLD_FOR_DB_YES_NO, "Yes");
            jntRecordMap.put(STATUS, CommonConstants.STATUS_MODIFIED);
            jntRecordMap.put(COMMAND, CommonConstants.STATUS_MODIFIED);
            allJntRecords.put(termLoanJointAcctTO.getCustId(), jntRecordMap);
            strCust = termLoanPoAOB.getCustName(termLoanJointAcctTO.getCustId());
            if (!termLoanPoAOB.getCbmPoACust().containsElement(strCust)){
                termLoanPoAOB.getCbmPoACust().addKeyAndElement(termLoanJointAcctTO.getCustId(), strCust);
            }
            if(!CommonUtil.convertObjToStr(termLoanJointAcctTO.getStatus()).equals(CommonConstants.STATUS_DELETED)){
                custListData = ClientUtil.executeQuery("getSelectAccInfoTblDisplay", jntRecordMap);
                if (custListData.size() > 0){
                    custMapData = (HashMap) custListData.get(0);
                    objJointAcctHolderManipulation.setJntAcctTableData(custMapData, true, tblBorrowerTab);
                    setTblBorrower(objJointAcctHolderManipulation.getTblJointAccnt());
                }
            }
            strCust = null;
            jntRecordMap = null;
            custMapData = null;
            custListData = null;
        }
        jntAcctAll.clear();
        jntAcctAll = allJntRecords;
        allJntRecords = null;
    }
    
    /**
     *
     * @return
     */
    public HashMap setTermLoanJointAcct(){
        HashMap jointAcctMap = new HashMap();
        try{
            TermLoanJointAcctTO objTermLoanJointAcctTO;
            java.util.Set keySet =  jntAcctAll.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            HashMap oneRecord;
            // To set the values for Security Transfer Object
            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
                oneRecord = (HashMap) jntAcctAll.get(objKeySet[j]);
                objTermLoanJointAcctTO = new TermLoanJointAcctTO();
                objTermLoanJointAcctTO.setBorrowNo(lblBorrowerNo_2);
                objTermLoanJointAcctTO.setCommand(CommonUtil.convertObjToStr(oneRecord.get(STATUS)));
                objTermLoanJointAcctTO.setCustId(CommonUtil.convertObjToStr(oneRecord.get(CUST_ID)));
                objTermLoanJointAcctTO.setStatus(CommonUtil.convertObjToStr(oneRecord.get(STATUS)));
                objTermLoanJointAcctTO.setStatusBy(TrueTransactMain.USER_ID);
                objTermLoanJointAcctTO.setStatusDt(ClientUtil.getCurrentDate());
                jointAcctMap.put(String.valueOf(jointAcctMap.size() + 1), objTermLoanJointAcctTO);
                objTermLoanJointAcctTO = null;
                oneRecord = null;
            }
            keySet = null;
            objKeySet = null;
        }catch(Exception e){
            log.info("Exception caught in setTermLoanJointAcct: ");
            parseException.logException(e,true);
        }
        return jointAcctMap;
    }
    
    public TermLoanBorrowerTO setTermLoanBorrower() {
        final TermLoanBorrowerTO objTermLoanBorrowerTO = new TermLoanBorrowerTO();
        try{
            objTermLoanBorrowerTO.setCustId(txtCustID);
            objTermLoanBorrowerTO.setBorrowNo(lblBorrowerNo_2);
            objTermLoanBorrowerTO.setConstitution(CommonUtil.convertObjToStr(cbmConstitution.getKeyForSelected()));
            objTermLoanBorrowerTO.setCategory(CommonUtil.convertObjToStr(cbmCategory.getKeyForSelected()));
            objTermLoanBorrowerTO.setReferences(txtReferences);
            objTermLoanBorrowerTO.setStatusBy(TrueTransactMain.USER_ID);
            objTermLoanBorrowerTO.setStatusDt(ClientUtil.getCurrentDate());
            objTermLoanBorrowerTO.setBranchCode(getSelectedBranchID());
            objTermLoanBorrowerTO.setInitiatedBranch(ProxyParameters.BRANCH_ID);
            objTermLoanBorrowerTO.setShgID(getTxtSHGId());
            objTermLoanBorrowerTO.setApplicationNo(getTxtApplicationNo());
        }catch(Exception e){
            log.info("Error in setTermLoanBorrower()"+e);
            parseException.logException(e,true);
        }
        return objTermLoanBorrowerTO;
    }
    
    void setTxtCustID(String txtCustID){
        this.txtCustID = txtCustID;
        setChanged();
    }
    String getTxtCustID(){
        return this.txtCustID;
    }
    
    void setTdtOpenDate(String tdtOpenDate){
        this.tdtOpenDate = tdtOpenDate;
        setChanged();
    }
    String getTdtOpenDate(){
        return this.tdtOpenDate;
    }
    
    public void setCbmConstitution(ComboBoxModel cbmConstitution){
        this.cbmConstitution = cbmConstitution;
        setChanged();
    }
    
    ComboBoxModel getCbmConstitution(){
        return cbmConstitution;
    }
    
    void setCboConstitution(String cboConstitution){
        this.cboConstitution = cboConstitution;
        setChanged();
    }
    String getCboConstitution(){
        return this.cboConstitution;
    }
    
    public void setCbmCategory(ComboBoxModel cbmCategory){
        this.cbmCategory = cbmCategory;
        setChanged();
    }
    
    ComboBoxModel getCbmCategory(){
        return cbmCategory;
    }
    
    void setCboCategory(String cboCategory){
        this.cboCategory = cboCategory;
        setChanged();
    }
    String getCboCategory(){
        return this.cboCategory;
    }
    
    void setTxtReferences(String txtReferences){
        this.txtReferences = txtReferences;
        setChanged();
    }
    String getTxtReferences(){
        return this.txtReferences;
    }
    
    public void setLblBorrowerNo_2(String lblBorrowerNo_2){
        this.lblBorrowerNo_2 = lblBorrowerNo_2;
        setChanged();
    }
    
    public String getLblBorrowerNo_2(){
        return this.lblBorrowerNo_2;
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    public void setLblOpenDate(String lblOpenDate2){
        this.lblOpenDate2 = lblOpenDate2;
        setChanged();
    }
    public String getLblOpenDate(){
        return this.lblOpenDate2;
    }
    
    public void setLblCustName(String lblCustName_2){
        this.lblCustName_2 = lblCustName_2;
        setChanged();
    }
    public String getLblCustName(){
        return this.lblCustName_2;
    }
    
    public void setLblCity(String lblCity_BorrowerProfile_2){
        this.lblCity_BorrowerProfile_2 = lblCity_BorrowerProfile_2;
        setChanged();
    }
    
    public String getLblCity(){
        return this.lblCity_BorrowerProfile_2;
    }
    
    
    public void setLblState(String lblState_BorrowerProfile_2){
        this.lblState_BorrowerProfile_2 = lblState_BorrowerProfile_2;
        setChanged();
    }
    
    public String getLblState(){
        return this.lblState_BorrowerProfile_2;
    }
    
    public void setLblPin(String lblPin_BorrowerProfile_2){
        this.lblPin_BorrowerProfile_2 = lblPin_BorrowerProfile_2;
        setChanged();
    }
    
    public String getLblPin(){
        return this.lblPin_BorrowerProfile_2;
    }
    
    public void setLblPhone(String lblPhone_BorrowerProfile_2){
        this.lblPhone_BorrowerProfile_2 = lblPhone_BorrowerProfile_2;
        setChanged();
    }
    
    public String getLblPhone(){
        return this.lblPhone_BorrowerProfile_2;
    }
    
    public void setLblFax(String lblFax_BorrowerProfile_2){
        this.lblFax_BorrowerProfile_2 = lblFax_BorrowerProfile_2;
        setChanged();
    }
    
    public String getLblFax(){
        return this.lblFax_BorrowerProfile_2;
    }
    
    void setTblBorrower(EnhancedTableModel tblBorrowerTab){
        log.info("In setTblBorrower...");
        
        this.tblBorrowerTab = tblBorrowerTab;
        setChanged();
    }
    
    EnhancedTableModel getTblBorrower(){
        return this.tblBorrowerTab;
    }
    
    void setJointAccountAll(LinkedHashMap jntAcctAll){
        this.jntAcctAll = jntAcctAll;
    }
    
    LinkedHashMap getJointAccountAll(){
        return this.jntAcctAll;
    }
    
    public List getCustOpenDate(String CUSTID){
        List returnList = null;
        try{
            HashMap transactionMap = new HashMap();
            transactionMap.put("custId",CUSTID);
            returnList = (List) ClientUtil.executeQuery("getSelectCustomerOpenDate", transactionMap);
            transactionMap = null;
        }catch(Exception e){
            log.info("Exception caught in getCustOpenDate: "+e);
            parseException.logException(e,true);
        }
        return returnList;
    }
    
    public boolean setCustOpenDate(String CUSTID){
        boolean display = false;
        try {
            HashMap retrieve = new HashMap();
            List resultList = getCustOpenDate(CUSTID);
            if (resultList.size() > 0){
                // If atleast one Record exist
                retrieve = (HashMap) resultList.get(0);
                if (retrieve.get("CUST_TYPE").equals("INDIVIDUAL")){
                    // If it is the Idividual Customer
                    setLblCustName(CommonUtil.convertObjToStr(retrieve.get("CUSTOMER NAME")));
                    display = true;
                }else{
                    setLblCustName(CommonUtil.convertObjToStr(retrieve.get("COMP_NAME")));
                }
                setLblOpenDate(DateUtil.getStringDate((java.util.Date) retrieve.get("CREATEDDT")));
            }
            retrieve = null;
            resultList = null;
        }catch(Exception e){
            log.info("Exception caught in setCustOpenDate: "+e);
            parseException.logException(e,true);
        }
        return display;
    }
    
    public void setCustAddr(String CUSTID){
        try {
            HashMap transactionMap = new HashMap();
            HashMap retrieve = new HashMap();
            transactionMap.put("custId",CUSTID);
            List resultList2 = ClientUtil.executeQuery("getSelectCustomerAddress", transactionMap);
            if (resultList2.size() > 0){
                // If atleast one Record exist
                retrieve = (HashMap) resultList2.get(0);
                setLblCity(CommonUtil.convertObjToStr(retrieve.get(CITY)));
                setLblState(CommonUtil.convertObjToStr(retrieve.get(STATE)));
                setLblPin(CommonUtil.convertObjToStr(retrieve.get(PIN_CODE)));
            }
            retrieve = null;
            transactionMap = null;
            resultList2 = null;
        }catch(Exception e){
            log.info("Exception caught in setCustAddr: "+e);
            parseException.logException(e,true);
        }
    }
    
    public List getCustPhone(String CUSTID){
        System.out.println("hiiiiii");
        List returnList = null;
        try{
            HashMap transactionMap = new HashMap();
            transactionMap.put("custId",CUSTID);
            returnList = ClientUtil.executeQuery("getSelectCustomerPhone", transactionMap);
            System.out.println("returnList==="+returnList);
            transactionMap = null;
        }catch(Exception e){
        }
        return returnList;
    }
    
    public void setCustPhone(String CUSTID){
        try {
            System.out.println("dgdfgdfg");
            StringBuffer stbPhone = new StringBuffer("");
            StringBuffer stbFax   = new StringBuffer("");
            int phoneCount = 0;
            int faxCount   = 0;
            HashMap retrieve;
            List resultList3 = getCustPhone(CUSTID);
            // To retrieve the Contact Phone Numbers of the Customer
            for (int i = resultList3.size() - 1,j = 0;i >= 0;--i,++j){
                retrieve = (HashMap) resultList3.get(j);
                if ((retrieve.get("PHONE_TYPE_ID")).equals("LAND LINE")){
                    // If the Phone Type ID is Land Line
                    if (phoneCount != 0){
                        // To avoid appending comma at the end
                        stbPhone.append(", ");
                    }
                    stbPhone.append(CommonUtil.convertObjToStr(retrieve.get(AREA_CODE)));
                    stbPhone.append(CommonUtil.convertObjToStr(retrieve.get(PHONE_NUMBER)));
                    phoneCount++;
                }
                if ((retrieve.get("PHONE_TYPE_ID")).equals(FAX)){
                    // If the Phone Type ID is Fax
                    if (faxCount != 0){
                        // To avoid appending comma at the end
                        stbFax.append(", ");
                    }
                    stbFax.append(CommonUtil.convertObjToStr(retrieve.get(AREA_CODE)));
                    stbFax.append(CommonUtil.convertObjToStr(retrieve.get(PHONE_NUMBER)));
                    faxCount++;
                }
                retrieve = null;
            }
            setLblFax(stbFax.toString());
            System.out.println("stbPhone==="+stbPhone);
            setLblPhone(stbPhone.toString());
            stbPhone = null;
            stbFax = null;
            resultList3 = null;
        }catch(Exception e){
            log.info("Exception caught in setCustPhone: "+e);
            parseException.logException(e,true);
        }
    }
    
    /**
     * Gives the Customer does not exist warning
     */
    public void mainCustDoesntExistWarn(){
        // Give the warning message if the main Customer doesn't exist
        int option = -1;
        String[] options = {objTermLoanRB.getString("cDialogOk")};
        option = COptionPane.showOptionDialog(null, objTermLoanRB.getString("existanceCustomerWarning"), CommonConstants.WARNINGTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
    }
    
    /**
     * Gives the warning that when you change constitution from
     * Joint Account to any other
     * @return the integer value on the basis of user's selection
     * of the dialog box
     */
    public int jointAcctWarn(){
        int option = -1;
        String[] options = {objTermLoanRB.getString("cDialogYes"), objTermLoanRB.getString("cDialogNo")};
        option = COptionPane.showOptionDialog(null, objTermLoanRB.getString("dialogForJointAccntHolder"), CommonConstants.WARNINGTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
        return option;
    }
    
    /**
     *
     * @param queryWhereMap
     * @param jntAcctNewClicked
     * @return
     */
    public boolean populateBorrowerTabCustomerDetails(HashMap queryWhereMap, boolean jntAcctNewClicked, String loanType){
        // Populate Customer Details
        boolean display = false;
        try {
            queryWhereMap.put("BRANCH_ID",ProxyParameters.BRANCH_ID);
            List custListData = ClientUtil.executeQuery("getSelectAccInfoDisplay",queryWhereMap);
            if(custListData !=null && custListData.size()>0) {
            HashMap custMapData;
            custMapData = (HashMap) custListData.get(0);
            System.out.println("custMapData######"+custMapData);
            String memClass = CommonUtil.convertObjToStr(custMapData.get("MEMBERSHIP_CLASS"));
//            if(((!memClass.equals("NONE")) && loanType.equals("OTHERS")) || loanType.equals("LTD") || btnPressed){
                 if( loanType.equals("OTHERS") || loanType.equals("LTD") || btnPressed){
            String strPrevMainCust_ID = getTxtCustID();
            if(jntAcctNewClicked==false){
                // If it is Main acctnt, set CustomerId in Main
                setTxtCustID(CommonUtil.convertObjToStr(custMapData.get(CUST_ID)));
                objJointAcctHolderManipulation.setJntAcctTableData(custMapData, false, tblBorrowerTab);
                setTblBorrower(objJointAcctHolderManipulation.getTblJointAccnt());
            }
            if (!custMapData.get("CUST_TYPE").equals("")){
                // If it is the Individual Customer
                setLblCustName(CommonUtil.convertObjToStr(custMapData.get("Name")));
                display = true;
            }
            //else{
               // setLblCustName(CommonUtil.convertObjToStr(custMapData.get("Name")));
           // }
            if ((custMapData.get("PHONE_TYPE_ID") != null) && (custMapData.get("PHONE_TYPE_ID").equals("LAND LINE"))){
                // If the Phone Type is not null and if it is a Land Line
                setLblPhone(CommonUtil.convertObjToStr(custMapData.get("AREA_CODE"))+CommonUtil.convertObjToStr(custMapData.get("PHONE_NUMBER")));
            }
            if ((custMapData.get("PHONE_TYPE_ID") != null) && (custMapData.get("PHONE_TYPE_ID").equals("FAX"))){
                // If the Phone Type is not null and if it is a Fax
                setLblFax(CommonUtil.convertObjToStr(custMapData.get("AREA_CODE"))+CommonUtil.convertObjToStr(custMapData.get("PHONE_NUMBER")));
            }
            setLblOpenDate(DateUtil.getStringDate((java.util.Date)custMapData.get("CREATEDDT")));
            setLblCity(CommonUtil.convertObjToStr(custMapData.get("CITY1")));
            setLblState(CommonUtil.convertObjToStr(custMapData.get("STATE1")));
            setLblPin(CommonUtil.convertObjToStr(custMapData.get("PIN_CODE")));
            String strPrevMainCust = termLoanPoAOB.getCustName(strPrevMainCust_ID);
            String strCust = termLoanPoAOB.getCustName(CommonUtil.convertObjToStr(queryWhereMap.get("CUST_ID")));
            if (!jntAcctNewClicked){
                // Remove the previous main customer, when the main customer's populate button pressed
                if (strPrevMainCust != "" && termLoanPoAOB.getCbmPoACust().containsElement(strPrevMainCust)){
                    termLoanPoAOB.getCbmPoACust().removeKeyAndElement(strPrevMainCust_ID);
                }
            }
            if (!termLoanPoAOB.getCbmPoACust().containsElement(strCust)){
                termLoanPoAOB.getCbmPoACust().addKeyAndElement(queryWhereMap.get("CUST_ID"), strCust);
            }
            strCust = null;
            custListData = null;
            custMapData=null;
           }
          // else {
          //    ClientUtil.displayAlert("This customer is not a member.\n\nSelect another Customer.");
          // }
            
            else {
                ClientUtil.displayAlert("Invalid Customer Id No");
                setTxtCustID("");
            }
          }else{
                ClientUtil.displayAlert("Invalid Customer Id No");
                setTxtCustID("");
            }
        } catch( Exception e ) {
            log.info("Exception caught in populateBorrowerTabCustomerDetails: "+e);
            parseException.logException(e,true);
        }
        btnPressed = false;
        return display;
    }
    
    /**
     *
     * @param queryWhereMap
     * @return
     */
    public boolean populateJointAccntTable(HashMap queryWhereMap){
        // To populate when Joint account is selected
        boolean display = false;
        try{
            jntAcctAll =  objJointAcctHolderManipulation.populateJointAccntTable(queryWhereMap, jntAcctAll, tblBorrowerTab);
            setTblBorrower(objJointAcctHolderManipulation.getTblJointAccnt());
            String strCust = termLoanPoAOB.getCustName(CommonUtil.convertObjToStr(queryWhereMap.get("CUST_ID")));
            if (!termLoanPoAOB.getCbmPoACust().containsElement(strCust)){
                termLoanPoAOB.getCbmPoACust().addKeyAndElement(queryWhereMap.get("CUST_ID"), strCust);
            }
        }catch(Exception e){
            log.info("Exception caught in populateJointAccntTable: "+e);
            parseException.logException(e,true);
        }
        return display;
    }
    
    /**
     * It will delete the selected Customer details
     * @param strDelRowCount is the Customer id
     * @param intDelRowCount is the selected row
     * @return false
     */
    public boolean deleteJointAccntHolder(String strDelRowCount, int intDelRowCount){
        // To delete one Record from the CTable
        boolean display = false;
        try{
            jntAcctAll =  objJointAcctHolderManipulation.delJointAccntHolder(strDelRowCount, intDelRowCount, tblBorrowerTab, jntAcctAll);
            setTblBorrower(objJointAcctHolderManipulation.getTblJointAccnt());
            if (termLoanPoAOB.getCbmPoACust().containsElement(termLoanPoAOB.getCustName(strDelRowCount))){
                termLoanPoAOB.getCbmPoACust().removeKeyAndElement(strDelRowCount);
            }
        }catch(Exception e){
            log.info("Exception caught in deleteJointAccntHolder: "+e);
            parseException.logException(e,true);
        }
        return display;
    }
    
    /**
     * Move the selected customer to be the main customer
     * when the constitution is Joint Account
     * @param mainAccntRow is the main customer's id
     * @param strRowSelected is the selected customer id
     * @param intRowSelected is the row selected in the table
     */
    public void moveToMain(String mainAccntRow, String strRowSelected , int intRowSelected){
        // Move the Joint Acct to the Main Acct
        try{
            jntAcctAll = objJointAcctHolderManipulation.moveToMain(mainAccntRow, strRowSelected, intRowSelected, tblBorrowerTab, jntAcctAll);
            setTxtCustID(strRowSelected);
            setTblBorrower(objJointAcctHolderManipulation.getTblJointAccnt());
            HashMap hash = new HashMap();
            hash.put("CUST_ID", getTxtCustID());
            populateBorrowerTabCustFields(hash, CommonUtil.convertObjToStr(tblBorrowerTab.getValueAt(0, 2)));
        }catch(Exception e){
            log.info("Exception caught in moveToMain: "+e);
            parseException.logException(e,true);
        }
    }
    
    public void validateConstitutionCustID(String strConstitutionValue, String strCustType){
        String strConstitutionKey = CommonUtil.convertObjToStr(getCbmConstitution().getKeyForSelected());
        if ((strConstitutionKey.equals(INDIVIDUAL) || strConstitutionKey.equals(INDUVIDUAL)) && strCustType.equals(CORPORATE)){
            int option = -1;
            String[] options = {objTermLoanRB.getString("cDialogOk")};
            option = COptionPane.showOptionDialog(null, objTermLoanRB.getString("individualCustWarning"), CommonConstants.WARNINGTITLE,
            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
            setCboConstitution("");
            return;
        }
        if (!(strConstitutionKey.equals(INDIVIDUAL) || strConstitutionKey.equals(INDUVIDUAL) || strConstitutionKey.equals(JOINT_ACCOUNT)) && strCustType.equals(INDIVIDUAL)){
            int option = -1;
            String[] options = {objTermLoanRB.getString("cDialogOk")};
            option = COptionPane.showOptionDialog(null, objTermLoanRB.getString("corporateCustWarning"), CommonConstants.WARNINGTITLE,
            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
            setCboConstitution("");
            return;
        }
    }
    
    public void populateBorrowerTabCustFields(HashMap hash, String custType){
        try{
            hash.put("BRANCH_CURRENT_DT", ClientUtil.getCurrentDate());
            List custDetailsList = ClientUtil.executeQuery("getBorrowerTabCustFields", hash);
            HashMap custDetailsMap = null;
            if (custDetailsList.size() > 0){
                custDetailsMap = (HashMap) custDetailsList.get(0);
                TermLoanCompanyOB termLoanCompanyOB = TermLoanCompanyOB.getInstance();
                termLoanCompanyOB.setTxtRiskRating(CommonUtil.convertObjToStr(custDetailsMap.get("RISK_RATING")));
                termLoanCompanyOB.setTxtNetWorth(CommonUtil.convertObjToStr(custDetailsMap.get("NETWORTH")));
                termLoanCompanyOB.setNetWorthInCust(CommonUtil.convertObjToStr(custDetailsMap.get("NETWORTH")));
                termLoanCompanyOB.setTdtCreditFacilityAvailSince(DateUtil.getStringDate((java.util.Date)custDetailsMap.get("CR_AVAILED_SINCE")));
                termLoanCompanyOB.setCboNatureBusiness(CommonUtil.convertObjToStr(termLoanCompanyOB.getCbmNatureBusiness().getDataForKey(CommonUtil.convertObjToStr(custDetailsMap.get("BUSINESS")))));
                if (CommonUtil.convertObjToStr(custDetailsMap.get("DEALING_WITH_BANK_SINCE")).length() > 0){
                    termLoanCompanyOB.setTdtDealingWithBankSince(DateUtil.getStringDate((java.util.Date)custDetailsMap.get("DEALING_WITH_BANK_SINCE")));
                }
                if (CommonUtil.convertObjToStr(custDetailsMap.get("NETWORTH_AS_ON")).length() > 0){
                    termLoanCompanyOB.setTdtAsOn(DateUtil.getStringDate((java.util.Date)custDetailsMap.get("NETWORTH_AS_ON")));
                }
            }
            
            if (custType.equals(CORPORATE)){
                custDetailsList = ClientUtil.executeQuery("getCorpMainCustDetails", hash);
                if (custDetailsList.size() > 0){
                    custDetailsMap = (HashMap) custDetailsList.get(0);
                    TermLoanCompanyOB termLoanCompanyOB = TermLoanCompanyOB.getInstance();
                    termLoanCompanyOB.setCboAddressType(CommonUtil.convertObjToStr(termLoanCompanyOB.getCbmAddressType().getDataForKey(custDetailsMap.get("COMM_ADDR_TYPE"))));
                    termLoanCompanyOB.setCboCity_CompDetail(CommonUtil.convertObjToStr(termLoanCompanyOB.getCbmCity_CompDetail().getDataForKey(custDetailsMap.get("CITY"))));
                    termLoanCompanyOB.setCboCountry_CompDetail(CommonUtil.convertObjToStr(termLoanCompanyOB.getCbmCountry_CompDetail().getDataForKey(custDetailsMap.get("COUNTRY_CODE"))));
                    termLoanCompanyOB.setCboState_CompDetail(CommonUtil.convertObjToStr(termLoanCompanyOB.getCbmState_CompDetail().getDataForKey(custDetailsMap.get("STATE"))));
                    termLoanCompanyOB.setTxtCompanyRegisNo(CommonUtil.convertObjToStr(custDetailsMap.get("COMP_REG_NO")));
                    if (CommonUtil.convertObjToStr(custDetailsMap.get("ESTABLISH_DT")).length() > 0){
                        termLoanCompanyOB.setTdtDateEstablishment(DateUtil.getStringDate((java.util.Date)custDetailsMap.get("ESTABLISH_DT")));
                    }
                    termLoanCompanyOB.setTxtChiefExecutiveName(CommonUtil.convertObjToStr(custDetailsMap.get("NAME")));
                    termLoanCompanyOB.setTxtPin_CompDetail(CommonUtil.convertObjToStr(custDetailsMap.get("PIN_CODE")));
                    termLoanCompanyOB.setTxtPhone_CompDetail(CommonUtil.convertObjToStr(custDetailsMap.get("PHONE_NUMBER")));
                    termLoanCompanyOB.setTxtStreet_CompDetail(CommonUtil.convertObjToStr(custDetailsMap.get("STREET")));
                    termLoanCompanyOB.setTxtArea_CompDetail(CommonUtil.convertObjToStr(custDetailsMap.get("AREA")));
                }
            }
        }catch(Exception e){
            log.info("Exception caught in populateBorrowerTabCustFields: "+e);
            parseException.logException(e,true);
        }
    }
    
    /**
     * Creates the needed objects for Borrower Tab
     */
    public void createObject(){
        objJointAcctHolderManipulation  = new JointAcctHolderManipulation();
        tblBorrowerTab = new EnhancedTableModel(null, borrowerTabTitle);
    }
    
    /**
     * Destroys the objects related to Borrower Tab
     */
    public void destroyObjects(){
        objJointAcctHolderManipulation = null;
        tblBorrowerTab = null;
    }
    
    /**
     * Getter for property txtSHGId.
     * @return Value of property txtSHGId.
     */
    public java.lang.String getTxtSHGId() {
        return txtSHGId;
    }
    
    /**
     * Setter for property txtSHGId.
     * @param txtSHGId New value of property txtSHGId.
     */
    public void setTxtSHGId(java.lang.String txtSHGId) {
        this.txtSHGId = txtSHGId;
    }
    
    /**
     * Getter for property txtApplicationNo.
     * @return Value of property txtApplicationNo.
     */
    public java.lang.String getTxtApplicationNo() {
        return txtApplicationNo;
    }
    
    /**
     * Setter for property txtApplicationNo.
     * @param txtApplicationNo New value of property txtApplicationNo.
     */
    public void setTxtApplicationNo(java.lang.String txtApplicationNo) {
        this.txtApplicationNo = txtApplicationNo;
    }
    
}
