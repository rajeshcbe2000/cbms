/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TokenConfigOB.java
 *
 * Created on Thu Jan 20 15:43:27 IST 2005
 */

package com.see.truetransact.ui.locker.lockeroperation;

import org.apache.log4j.Logger;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;

import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.CObservable;
//import com.see.truetransact.transferobject.transaction.token.tokenconfig.TokenConfigTO;
import com.see.truetransact.transferobject.locker.lockeroperation.LockerOperationTO;
import com.see.truetransact.transferobject.locker.lockerissue.LockerPwdDetailsTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.commonutil.StringEncrypter;
import java.util.Date;

/**
 *
 * @author Ashok Vijayakumar
 */

public class LockerOperationOB extends CObservable{
    
    /** Variables Declaration - Corresponding each Variable is in TokenConfigUI*/
    //    private String txtTokenConfigId = "";
    //    private String cboTokenType = "";
    //    private ComboBoxModel cbmTokenType;//Model for ui combobox cboTokenType
    //    private String txtSeriesNo = "";
    //    private String txtStartingTokenNo = "";
    //    private String txtEndingTokenNo = "";
    //    private String txtNoOfTokens = "";
    
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.locker.lockeroperation.LockerOperationRB", ProxyParameters.LANGUAGE);
    
    private String optID = "";
    private String lblDateVal = "";
    private String lblLockerOutDtVal = "";
    private String lblCustNameVal = "";
    private String lblCustomerIdVal = "";
    private String lblModeOfOpVal = "";
    private String txtLockerNo = "";
    private String txtCustId = "";
    private String txtName = "";
    private String txtPassword = "";
    private String  lblConstitutionVal="";
    private String txtNewPwd = "";
    private String txtConPwd = "";
    private String txtCust = "";
    public LinkedHashMap allowedPwd = null;
    
    private EnhancedTableModel tbmInstructions2;
    private ArrayList existData = null;
    private ArrayList tblHeadings3;
    private boolean deletingExists = false;
    private ArrayList existingData;
    private ArrayList newInstructionRow = null;
    private ArrayList newData = new ArrayList();
    private String issueId = "";
    private String operatingCustomer = ""; // Added by nithya on 11-12-2018 for KD -289 Locker operation for joint operators are not marking properly.
    
    
    /** Other Varibales Declartion */
    private ProxyFactory proxy;
    private HashMap map;//HashMap for setting key,values for Jndi,home,remote
    private static LockerOperationOB objLockerOperationOB;//Singleton Object Reference
    private final static Logger log = Logger.getLogger(LockerOperationOB.class);//Creating Instace of Log
    private ArrayList key,value;//ArrayList for populating the ComboBoxModel key-Storing keys value-for Storing Values
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private int _actionType;//This vairable is used to type of action 1-New 2-Edit 3-Delete
    private int _result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private ArrayList allInstructionsList = null;
    private StringEncrypter encrypt = null;
    private Date currDt = null;
    /** Consturctor Declaration  for  TokenConfigOB */
    public LockerOperationOB() {
        try {
            currDt = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            encrypt = new StringEncrypter();
            setOperationMap();
            initUIComboBoxModel();
            createTblHeadings();
            createTbmHead();
            //            fillDropdown();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    static {
        try {
            log.info("Creating ParameterOB...");
            objLockerOperationOB = new LockerOperationOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    // Sets the HashMap required to set JNDI,Home and Remote
    private void setOperationMap() throws Exception{
        map = new HashMap();
        map.put(CommonConstants.JNDI, "LockerOperationJNDI");
        map.put(CommonConstants.HOME, "locker.lockeroperation.LockerOperationHome");
        map.put(CommonConstants.REMOTE, "locker.lockeroperation.LockerOperation");
    }
    
    private void createTblHeadings(){
        tblHeadings3 = new ArrayList();
        tblHeadings3.add(resourceBundle.getString("tblHeading1"));
        tblHeadings3.add(resourceBundle.getString("tblHeading2"));
        tblHeadings3.add(resourceBundle.getString("tblHeading3"));
        //        tblHeadings3.add(resourceBundle.getString("tblHeading3"));
        //        tblHeadings3.add(resourceBundle.getString("tblHeadingLock4"));
        //        tblHeadings3.add(resourceBundle.getString("tblHeadingLock5"));
        //        tblHeadings1.add(resourceBundle.getString("tblHeading4"));
    }
    private void createTbmHead(){
        tbmInstructions2 = new EnhancedTableModel(null, tblHeadings3);
    }
    /** Creating instance for ComboboxModel cbmTokenType */
    private void initUIComboBoxModel(){
        //        cbmTokenType = new ComboBoxModel();
    }
        
    /**
     * Returns an instance of TokenConfigOB.
     * @return  TokenConfigOB
     */
    
    public static LockerOperationOB getInstance()throws Exception{
        return objLockerOperationOB;
    }
    
    public void populateTblActData(HashMap mapData){
        HashMap param = new HashMap();
        param.put("CUST_ID", mapData.get("CUST_ID"));
        param.put("ISSUE_ID", mapData.get("ISSUE_ID"));
        param.put("LOCKER_NUM", mapData.get("LOCKER_NUM"));
        param.put("BRANCH_CODE", mapData.get("BRANCH_CODE"));
        List lstData = null;
        if (getActionType()==ClientConstants.ACTIONTYPE_NEW) {
            lstData = (List) ClientUtil.executeQuery("selectLockerActOptTO", param);
        } else {
            lstData = (List) ClientUtil.executeQuery("selectLockerActOptDetailsTO", param);
        }
        param = null;
        allInstructionsList = new ArrayList();
        if(lstData != null && lstData.size() > 0){
            for(int i=0; i < lstData.size(); i++){
                param = (HashMap) lstData.get(i);
                existData = new ArrayList();
                existData.add(param.get("CUST_ID"));
               existData.add(param.get("NAME"));
                existData.add("");
                
                //              existData.add(param.get(""));
                ArrayList tempList = new ArrayList();
                tempList.addAll(existData);
                tempList.add("");
//                tempList.add(encrypt.decrypt(CommonUtil.convertObjToStr(mapData.get("PWD"))));
                allInstructionsList.add(allInstructionsList.size(), tempList);
                tempList = null;
                //              existData.add(param.get("END_DT"));
                //              existData.add(param.get("COMMISION"));
                //              existData.add(param.get("SERVICE_TAX"));
                tbmInstructions2.insertRow(tbmInstructions2.getRowCount(), existData);
                
            }
        }
    }
    
    public void setNewPwdDetails(String cust){
        LockerPwdDetailsTO objLockerPwdDetailsTO = new LockerPwdDetailsTO();
        objLockerPwdDetailsTO.setPwd(getTxtNewPwd());
        objLockerPwdDetailsTO.setCustID(cust);
        objLockerPwdDetailsTO.setLocNum(getTxtLockerNo());
        objLockerPwdDetailsTO.setStatus(CommonConstants.STATUS_MODIFIED);
        objLockerPwdDetailsTO.setStatusDt(currDt);
        objLockerPwdDetailsTO.setBranID(ProxyParameters.BRANCH_ID);
        allowedPwd.put(cust, objLockerPwdDetailsTO);
        System.out.println("!!!!!!!!!!!allowedPwd"+allowedPwd);
    }
    
    /**
     * Getter for property issueId.
     * @return Value of property issueId.
     */
    public java.lang.String getIssueId() {
        return issueId;
    }
    
    /**
     * Setter for property issueId.
     * @param issueId New value of property issueId.
     */
    public void setIssueId(java.lang.String issueId) {
        this.issueId = issueId;
    }
    
    /**
     * Getter for property txtTokenConfigId.
     * @return Value of property txtTokenConfigId.
     */
    //    public java.lang.String getTxtTokenConfigId() {
    //        return txtTokenConfigId;
    //    }
    
    /**
     * Setter for property txtTokenConfigId.
     * @param txtTokenConfigId New value of property txtTokenConfigId.
     */
    //    public void setTxtTokenConfigId(java.lang.String txtTokenConfigId) {
    //        this.txtTokenConfigId = txtTokenConfigId;
    //        setChanged();
    //    }
    //
    //    // Setter method for cboTokenType
    //    void setCboTokenType(String cboTokenType){
    //        this.cboTokenType = cboTokenType;
    //        setChanged();
    //    }
    //    // Getter method for cboTokenType
    //    String getCboTokenType(){
    //        return this.cboTokenType;
    //    }
    //
    //    /**
    //     * Getter for property cbmTokenType.
    //     * @return Value of property cbmTokenType.
    //     */
    //    public com.see.truetransact.clientutil.ComboBoxModel getCbmTokenType() {
    //        return cbmTokenType;
    //    }
    //
    //    /**
    //     * Setter for property cbmTokenType.
    //     * @param cbmTokenType New value of property cbmTokenType.
    //     */
    //    public void setCbmTokenType(com.see.truetransact.clientutil.ComboBoxModel cbmTokenType) {
    //        this.cbmTokenType = cbmTokenType;
    //    }
    //
    //    // Setter method for txtSeriesNo
    //    void setTxtSeriesNo(String txtSeriesNo){
    //        this.txtSeriesNo = txtSeriesNo;
    //        setChanged();
    //    }
    //    // Getter method for txtSeriesNo
    //    String getTxtSeriesNo(){
    //        return this.txtSeriesNo;
    //    }
    //
    //    // Setter method for txtStartingTokenNo
    //    void setTxtStartingTokenNo(String txtStartingTokenNo){
    //        this.txtStartingTokenNo = txtStartingTokenNo;
    //        setChanged();
    //    }
    //    // Getter method for txtStartingTokenNo
    //    String getTxtStartingTokenNo(){
    //        return this.txtStartingTokenNo;
    //    }
    //
    //    // Setter method for txtEndingTokenNo
    //    void setTxtEndingTokenNo(String txtEndingTokenNo){
    //        this.txtEndingTokenNo = txtEndingTokenNo;
    //        setChanged();
    //    }
    // Getter method for txtEndingTokenNo
    //    String getTxtEndingTokenNo(){
    //        return this.txtEndingTokenNo;
    //    }
    //
    //    // Setter method for txtNoOfTokens
    //    void setTxtNoOfTokens(String txtNoOfTokens){
    //        this.txtNoOfTokens = txtNoOfTokens;
    //        setChanged();
    //    }
    //    // Getter method for txtNoOfTokens
    //    String getTxtNoOfTokens(){
    //        return this.txtNoOfTokens;
    //    }
    
    public void setResult(int result) {
        _result = result;
        setResultStatus();
        setChanged();
    }
    
    public int getActionType(){
        return _actionType;
    }
    
    public void setActionType(int actionType) {
        _actionType = actionType;
        setStatus();
        setChanged();
    }
    
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }
    
    /** Getter for property lblStatus.
     * @return Value of property lblStatus.
     *
     */
    public java.lang.String getLblStatus() {
        return lblStatus;
    }
    
    /** Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     *
     */
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
    }
    
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    
    public int getResult(){
        return _result;
    }
    
    /** Creates an Instance of TokenConfigTO Bean and sets its variables with OBMethods */
    private LockerOperationTO getLockerOperationTO(String command){
        LockerOperationTO objLockerOperationTO = new LockerOperationTO();
        //        objTokenConfigTO.setCommand(command);
        //        objTokenConfigTO.setConfigId(getTxtTokenConfigId());
        //        objTokenConfigTO.setTokenType(CommonUtil.convertObjToStr(getCbmTokenType().getKeyForSelected()));
        //        objTokenConfigTO.setSeriesNo(getTxtSeriesNo());
        //        objTokenConfigTO.setTokenStartNo(new Double(getTxtStartingTokenNo()));
        //        objTokenConfigTO.setTokenEndNo(new Double(getTxtEndingTokenNo()));
        //        objTokenConfigTO.setBranchId(TrueTransactMain.BRANCH_ID);
        //        if(command.equals(CommonConstants.TOSTATUS_INSERT)){
        //            objTokenConfigTO.setCreatedBy(TrueTransactMain.USER_ID);
        //            objTokenConfigTO.setCreatedDt(currDt);
        //        }
        //        objTokenConfigTO.setStatusBy(TrueTransactMain.USER_ID);
        //        objTokenConfigTO.setStatusDt(currDt);
        objLockerOperationTO.setRemarks(getIssueId());
        objLockerOperationTO.setCommand(command);
        objLockerOperationTO.setLocNum(getTxtLockerNo());
        //objLockerOperationTO.setCustId(getLblCustomerIdVal());
        if(getOperatingCustomer().length() > 0){// Added by nithya on 11-12-2018 for KD -289 Locker operation for joint operators are not marking properly.
            objLockerOperationTO.setCustId(getOperatingCustomer()); 
        }else{
            objLockerOperationTO.setCustId(getLblCustomerIdVal());
        }        
        objLockerOperationTO.setOptMode(getLblModeOfOpVal());
        objLockerOperationTO.setStatusBy(TrueTransactMain.USER_ID);
        objLockerOperationTO.setStatusDt(currDt);
        objLockerOperationTO.setBranchID(TrueTransactMain.BRANCH_ID);
        objLockerOperationTO.setCreatedBy(TrueTransactMain.USER_ID);
        objLockerOperationTO.setAcctName(getLblCustNameVal());
        objLockerOperationTO.setLblConstitutionVal(getLblConstitutionVal());
        if(command.equals("UPDATE")){
            objLockerOperationTO.setLockerOutBy(TrueTransactMain.USER_ID);
            objLockerOperationTO.setOptId(getOptID());
            //            objLockerOperationTO.setLockerOutDt(ClientUtil.getCurrentDateWithTime());
        }
        if(command.equals("DELETE")){
            objLockerOperationTO.setOptId(getOptID());
        }
        System.out.println("@@@@@@@@@@@@@objLockerOperationTO"+objLockerOperationTO);
        return objLockerOperationTO;
    }
    
    private void setLockerOptTO(LockerOperationTO objLockerOperationTO){
        setTxtLockerNo(objLockerOperationTO.getLocNum());
        setLblCustNameVal(objLockerOperationTO.getAcctName());
        setLblCustomerIdVal(objLockerOperationTO.getCustId());
        System.out.println("@@@@@@@@@@@@@objLockerOperationTO.getOptDt()"+objLockerOperationTO.getOptDt());
        setLblDateVal(objLockerOperationTO.getOptDt().toString());
        setLblModeOfOpVal(objLockerOperationTO.getOptMode());
              setLblConstitutionVal(objLockerOperationTO.getLblConstitutionVal());
        //        setTxtTokenConfigId(objTokenConfigTO.getConfigId());
        //        setCboTokenType(CommonUtil.convertObjToStr(getCbmTokenType().getDataForKey(objTokenConfigTO.getTokenType())));
        //        setTxtSeriesNo(objTokenConfigTO.getSeriesNo());
        //        setTxtStartingTokenNo(CommonUtil.convertObjToStr(objTokenConfigTO.getTokenStartNo()));
        //        setTxtEndingTokenNo(CommonUtil.convertObjToStr(objTokenConfigTO.getTokenEndNo()));
        //        setStatusBy(objTokenConfigTO.getStatusBy());
        //         setAuthorizeStatus(objTokenConfigTO.getAuthorizeStatus());
        System.out.println("$$$$$$$$$$$$"+getLblCustNameVal()+"&&&&&&&&&&"+getLblDateVal());
        notifyObservers();
    }
    
    /** Resets all the UI Fields */
    public void resetForm(){
        //        setTxtTokenConfigId("");
        //        setCboTokenType("");
        //        setTxtSeriesNo("");
        //        setTxtStartingTokenNo("");
        //        setTxtEndingTokenNo("");
        setLblCustNameVal("");
        setLblCustomerIdVal("");
        setTxtLockerNo("");
        setLblLockerOutDtVal("");
        setLblModeOfOpVal("");
        setLblDateVal("");
        setOperatingCustomer("");// Added by nithya on 11-12-2018 for KD -289 Locker operation for joint operators are not marking properly.
        notifyObservers();
    }
    
    /* Executes Query using the TO object */
    public void execute(String command) {
        try {
            HashMap term = new HashMap();
            term.put(CommonConstants.MODULE, getModule());
            term.put(CommonConstants.SCREEN, getScreen());
            term.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            term.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            term.put("LockerOperationTO", getLockerOperationTO(command));
            term.put("OperationList", allInstructionsList);
            System.out.println("#$#$ Before proxy.execute map : "+term);
//            HashMap proxyReturnMap=null;
            HashMap proxyReturnMap = proxy.execute(term, map);
            setProxyReturnMap(proxyReturnMap);
            setResult(getActionType());
            String message = "";
            if (proxyReturnMap != null && proxyReturnMap.containsKey("LOCKER_OPT_ID"))  {
                message = "Operation ID : "+CommonUtil.convertObjToStr(proxyReturnMap.get("LOCKER_OPT_ID"))+"\n";
                /* +"Instrument No. : "+CommonUtil.convertObjToStr(proxyResultMap.get("INST_NO1")) + "-" + CommonUtil.convertObjToStr(proxyResultMap.get("INST_NO2"))*/
            }
            if (proxyReturnMap != null && proxyReturnMap.containsKey("LockerOperationTO"))  {
                java.text.SimpleDateFormat DATE_FORMAT = new java.text.SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                LockerOperationTO objLockerOperationTO = (LockerOperationTO)proxyReturnMap.get("LockerOperationTO");
                message += "IN TIME : " +DATE_FORMAT.format((java.util.Date)objLockerOperationTO.getOptDt()) + "\n";
                if (objLockerOperationTO.getLockerOutDt()!=null) {
                    message += "OUT TIME : " +DATE_FORMAT.format((java.util.Date)objLockerOperationTO.getLockerOutDt())+"\n";
                Date inTime=(java.util.Date)objLockerOperationTO.getOptDt();
                Date outTime=(java.util.Date)objLockerOperationTO.getLockerOutDt();
                    long timeDiff = outTime.getTime()-inTime.getTime();                         
                    long diffSeconds = timeDiff / 1000 % 60;
                    long diffMinutes = timeDiff / (60 * 1000) % 60;         
                    long diffHours = timeDiff / (60 * 60 * 1000);
                    message+="TOTAL USAGE :"+diffHours +"h "+diffMinutes +"m "+diffSeconds +"s";
                }
            }
            if (message.length()>0) {
                ClientUtil.showMessageWindow(message);
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    public void populateSelectedRowAct(int row){
        ArrayList data = (ArrayList)tbmInstructions2.getDataArrayList().get(row);
        //        return data;
//        ArrayList data = (ArrayList)allInstructionsList.get(row);
        
        setTxtCustId(CommonUtil.convertObjToStr(data.get(0)));
        setTxtName(CommonUtil.convertObjToStr(data.get(1)));
        //        setTxtPassword(CommonUtil.convertObjToStr(data.get(2)));
        //        ttNotifyObservers();
        //        setTxtPassword(CommonUtil.convertObjToStr(data.get(1)));
        
    }
    
    public void setTableValueAt(int row){
        deletingExists = true;
        final ArrayList data = tbmInstructions2.getDataArrayList();
        if(existingData!=null){
            existingData.add(data.get(row));
        }
        //        if(!getCbmStdInstruction().getKeyForSelected().equals("OTHERS")){
        tbmInstructions2.setValueAt(getTxtCustId(),row,0);
        //            tbmInstructions.setValueAt(getCboStdInstruction(),row,1);
        tbmInstructions2.setValueAt(getTxtName(),row,1);
                ArrayList tempList = new ArrayList();
                tempList.addAll(existData);
                try {
                    String strEncrypt = txtPassword.length() == 0 ? "" : encrypt.encrypt(txtPassword);
                    tempList.add(strEncrypt);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                tempList.add(txtPassword.toString());
                allInstructionsList.set(row, tempList);
                tempList = null;
        
        //            tbmInstructions2.setValueAt("*",row,2);
        //        }else{
        //            tbmInstructions.setValueAt(getTxtStdInstruction(),row,1);
        //        }
        //         setTxtTotalAmt(calculateTotalAmount(data));
        //         setTxttotalServTax(calculateTotalServTaxAmount(data));
    }
    
    public boolean checkPasswordRecords() {
        boolean pwd = false;
        ArrayList tempList = null;
        
        if (allInstructionsList!=null && allInstructionsList.size()>0) {
            if(getLblModeOfOpVal().equals("Jointly") && getLblConstitutionVal().equals("JOINT")){
           
           //for (int i=0; i<allInstructionsList.size(); i++) {
            for (int i=0; i<allInstructionsList.size(); i++)
            {
                pwd=false;
                tempList = (ArrayList) allInstructionsList.get(i);
                if (tempList.get(2)!=null && CommonUtil.convertObjToStr(tempList.get(3)).length()>0) {
                    pwd = true;
                   // break;
                }
           }
            } 
            else{
                int k=1;
                for (int i=0; i<allInstructionsList.size(); i++)
                { 
                    pwd=false;
                     tempList = (ArrayList) allInstructionsList.get(i);
                    
                if (tempList.get(2)!=null && CommonUtil.convertObjToStr(tempList.get(3)).length()>0){
                    pwd = true;
                    k++;
                }
                 // break;
                     if(k!=1)
                    if(getLblConstitutionVal().equals("JOINT")){
                        pwd=true;
                        
                    }
                
           //pwd=true;
            }
            
        }
        }
        return pwd;
    
    
    }
    public void resetInstTbl(){
        tbmInstructions2.setDataArrayList(null, tblHeadings3);
    }
    
    public int addTblInstructionData(){
        int optionSelected = -1;
        String columnData = "";
        //        getCbmStdInstruction().setSelectedItem(getCboStdInstruction());
        //        if(!getCbmStdInstruction().getKeyForSelected().equals("OTHERS")){
        //            columnData = CommonUtil.convertObjToStr(getCbmStdInstruction().getKeyForSelected());
        //            columnData = CommonUtil.convertObjToStr(getCboStdInstruction());
        columnData = CommonUtil.convertObjToStr(getTxtCustId());
        //            columnData = CommonUtil.convertObjToStr(getTxtName());
        //        }else{
        //            columnData = getTxtStdInstruction();
        //            columnData = CommonUtil.convertObjToStr(getTxtPassword());
        //            columnData = CommonUtil.convertObjToStr(getTxtServiceTax());
        //        }
        try{
            if (newInstructionRow == null) {
                newInstructionRow = new ArrayList();
            }
            newInstructionRow.add(new Integer(tbmInstructions2.getRowCount()+1));
            //            if(!getCbmStdInstruction().getKeyForSelected().equals("OTHERS")){
            //////                newInstructionRow.add(getCbmStdInstruction().getKeyForSelected());
            //                newInstructionRow.add(getCboStdInstruction());
            newInstructionRow.add(getTxtCustId());
            //                newInstructionRow.add(getTxtServiceTax());
            //            }else{
            //                newInstructionRow.add(getTxtStdInstruction());
            //                newInstructionRow.add(getTxtAmount());
            //                newInstructionRow.add(getTxtServiceTax());
            //            }
            ArrayList data = tbmInstructions2.getDataArrayList();
            final int dataSize = data.size();
            boolean exist = false;
            for (int i=0;i<dataSize;i++){
                if (CommonUtil.convertObjToStr(((ArrayList)data.get(i)).get(1)).equalsIgnoreCase(columnData)){
                    // Checking whether existing Data is equal new data entered by the user
                    exist = true;
                    String[] options = {resourceBundle.getString("cDialogYes"),resourceBundle.getString("cDialogNo"),resourceBundle.getString("cDialogCancel")};
                    optionSelected = COptionPane.showOptionDialog(null, resourceBundle.getString("existanceWarning"), CommonConstants.WARNINGTITLE,
                    COptionPane.YES_NO_CANCEL_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                    if (optionSelected == 0){
                        // Newly Entered data already exists and the user wants to modify it
                        updateTbmInstructions(i);
                        //doUpdateData(i);
                    }
                    break;
                }
                
            }
            if (!exist){
                //The condition that the Entered data is not in the table
                doNewData();
                insertNewData();
            }
            //            setTxtTotalAmt(calculateTotalAmount(data));
            //            setTxttotalServTax(calculateTotalServTaxAmount(data));
            newInstructionRow = null;
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return optionSelected;
    }
    
    private void updateTbmInstructions(int row) throws Exception{
        //        if(!getCbmStdInstruction().getKeyForSelected().equals("OTHERS")){
        //            tbmInstructions.setValueAt(getCbmStdInstruction().getKeyForSelected(), row, 1);
        //            tbmInstructions.setValueAt(getCboStdInstruction(), row, 1);
        //        }else{
        tbmInstructions2.setValueAt(getTxtCustId(), row, 1);
        //        }
        
    }
    
    private void doNewData(){
        newData.add(newInstructionRow);
    }
    private void insertNewData() throws Exception{
        //final TerminalTO objTerminalTO = new TerminalTO();
        int row = tbmInstructions2.getRowCount();
        tbmInstructions2.insertRow(row,newInstructionRow);
    }
    public void ttNotifyObservers(){
        this.notifyObservers();
        setChanged();
    }
    /* Populates the TO object by executing a Query */
    public void populateData(HashMap whereMap) {
        HashMap mapData=null;
        try {
            mapData = proxy.executeQuery(whereMap, map);
            LockerOperationTO objLockerOperationTO =
            (LockerOperationTO) ((List) mapData.get("LockerOptTO")).get(0);
            setLockerOptTO(objLockerOperationTO);
            com.see.truetransact.transferobject.locker.lockerissue.LockerIssueTO objLockerIssueTO =
            (com.see.truetransact.transferobject.locker.lockerissue.LockerIssueTO) ((List) mapData.get("LockerTO")).get(0);
            whereMap.put("LockerTO",objLockerIssueTO);
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
            
        }
    }
    
    /**
     * Getter for property lblDateVal.
     * @return Value of property lblDateVal.
     */
    public java.lang.String getLblDateVal() {
        return lblDateVal;
    }
    
    /**
     * Setter for property lblDateVal.
     * @param lblDateVal New value of property lblDateVal.
     */
    public void setLblDateVal(java.lang.String lblDateVal) {
        this.lblDateVal = lblDateVal;
    }
    
    /**
     * Getter for property lblLockerOutDtVal.
     * @return Value of property lblLockerOutDtVal.
     */
    public java.lang.String getLblLockerOutDtVal() {
        return lblLockerOutDtVal;
    }
    
    /**
     * Setter for property lblLockerOutDtVal.
     * @param lblLockerOutDtVal New value of property lblLockerOutDtVal.
     */
    public void setLblLockerOutDtVal(java.lang.String lblLockerOutDtVal) {
        this.lblLockerOutDtVal = lblLockerOutDtVal;
    }
    
    /**
     * Getter for property lblCustNameVal.
     * @return Value of property lblCustNameVal.
     */
    public java.lang.String getLblCustNameVal() {
        return lblCustNameVal;
    }
    
    /**
     * Setter for property lblCustNameVal.
     * @param lblCustNameVal New value of property lblCustNameVal.
     */
    public void setLblCustNameVal(java.lang.String lblCustNameVal) {
        this.lblCustNameVal = lblCustNameVal;
    }
    
    /**
     * Getter for property lblCustomerIdVal.
     * @return Value of property lblCustomerIdVal.
     */
    public java.lang.String getLblCustomerIdVal() {
        return lblCustomerIdVal;
    }
    
    /**
     * Setter for property lblCustomerIdVal.
     * @param lblCustomerIdVal New value of property lblCustomerIdVal.
     */
    public void setLblCustomerIdVal(java.lang.String lblCustomerIdVal) {
        this.lblCustomerIdVal = lblCustomerIdVal;
    }
    
    /**
     * Getter for property lblModeOfOpVal.
     * @return Value of property lblModeOfOpVal.
     */
    public java.lang.String getLblModeOfOpVal() {
        return lblModeOfOpVal;
    }
    
    /**
     * Setter for property lblModeOfOpVal.
     * @param lblModeOfOpVal New value of property lblModeOfOpVal.
     */
    public void setLblModeOfOpVal(java.lang.String lblModeOfOpVal) {
        this.lblModeOfOpVal = lblModeOfOpVal;
    }
    
    /**
     * Getter for property txtLockerNo.
     * @return Value of property txtLockerNo.
     */
    public java.lang.String getTxtLockerNo() {
        return txtLockerNo;
    }
    
    /**
     * Setter for property txtLockerNo.
     * @param txtLockerNo New value of property txtLockerNo.
     */
    public void setTxtLockerNo(java.lang.String txtLockerNo) {
        this.txtLockerNo = txtLockerNo;
    }
    
    /**
     * Getter for property txtCustId.
     * @return Value of property txtCustId.
     */
    public java.lang.String getTxtCustId() {
        return txtCustId;
    }
    
    /**
     * Setter for property txtCustId.
     * @param txtCustId New value of property txtCustId.
     */
    public void setTxtCustId(java.lang.String txtCustId) {
        this.txtCustId = txtCustId;
    }
    
    /**
     * Getter for property txtName.
     * @return Value of property txtName.
     */
    public java.lang.String getTxtName() {
        return txtName;
    }
    
    /**
     * Setter for property txtName.
     * @param txtName New value of property txtName.
     */
    public void setTxtName(java.lang.String txtName) {
        this.txtName = txtName;
    }
    
    /**
     * Getter for property txtPassword.
     * @return Value of property txtPassword.
     */
    public java.lang.String getTxtPassword() {
        return txtPassword;
    }
    
    /**
     * Setter for property txtPassword.
     * @param txtPassword New value of property txtPassword.
     */
    public void setTxtPassword(java.lang.String txtPassword) {
        this.txtPassword = txtPassword;
    }
    
    /**
     * Getter for property tbmInstructions2.
     * @return Value of property tbmInstructions2.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTbmInstructions2() {
        return tbmInstructions2;
    }
    
    /**
     * Setter for property tbmInstructions2.
     * @param tbmInstructions2 New value of property tbmInstructions2.
     */
    public void setTbmInstructions2(com.see.truetransact.clientutil.EnhancedTableModel tbmInstructions2) {
        this.tbmInstructions2 = tbmInstructions2;
    }
    
    /**
     * Getter for property optID.
     * @return Value of property optID.
     */
    public java.lang.String getOptID() {
        return optID;
    }
    
    /**
     * Setter for property optID.
     * @param optID New value of property optID.
     */
    public void setOptID(java.lang.String optID) {
        this.optID = optID;
    }
    
    /**
     * Getter for property txtNewPwd.
     * @return Value of property txtNewPwd.
     */
    public java.lang.String getTxtNewPwd() {
        return txtNewPwd;
    }
    
    /**
     * Setter for property txtNewPwd.
     * @param txtNewPwd New value of property txtNewPwd.
     */
    public void setTxtNewPwd(java.lang.String txtNewPwd) {
        this.txtNewPwd = txtNewPwd;
    }
    
    /**
     * Getter for property txtConPwd.
     * @return Value of property txtConPwd.
     */
    public java.lang.String getTxtConPwd() {
        return txtConPwd;
    }
     public void  setLblConstitutionVal(java.lang.String  lblConstitutionVal) {
        this. lblConstitutionVal= lblConstitutionVal;
     }
     
     public String getLblConstitutionVal() {
       return lblConstitutionVal;
     }
    /**
     * Setter for property txtConPwd.
     * @param txtConPwd New value of property txtConPwd.
     */
    public void setTxtConPwd(java.lang.String txtConPwd) {
        this.txtConPwd = txtConPwd;
    }
    
    /**
     * Getter for property txtCust.
     * @return Value of property txtCust.
     */
    public java.lang.String getTxtCust() {
        return txtCust;
    }
    
    /**
     * Setter for property txtCust.
     * @param txtCust New value of property txtCust.
     */
    public void setTxtCust(java.lang.String txtCust) {
        this.txtCust = txtCust;
    }
    
    /*Checks for the duplication of TokenType if so retuns a boolean type vairable as true */
    //    public boolean isTokenTypeExists(String tokenType){
    //        boolean exists = false;
    //        HashMap resultMap = null;
    //        HashMap where = new HashMap();
    //        where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
    //        ArrayList resultList =(ArrayList) ClientUtil.executeQuery("getSelectTokenType", where);
    //        where = null;
    //        if(resultList.size() > 0){
    //            for(int i=0; i<resultList.size(); i++){
    //                resultMap= (HashMap)resultList.get(i);
    //                if(resultMap.get("TOKEN_TYPE").equals(tokenType)){
    //                    exists = true;
    //                    break;
    //                }
    //            }
    //        }
    //        return exists;
    //    }
    
    /*Checks for the duplication of SeriesNo if so retuns a boolean type vairable as true */
    //    public boolean isSeriesNoExists(String tokenType,String seriesNo){
    //        boolean exists = false;
    //        HashMap resultMap = null;
    //        HashMap where = new HashMap();
    //        where.put("TOKEN_TYPE", tokenType);
    //        where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
    //        ArrayList resultList =(ArrayList) ClientUtil.executeQuery("getSelectTokenSeries", where);
    //        where = null;
    //        if(resultList.size() > 0){
    //            for(int i=0; i<resultList.size(); i++){
    //                resultMap= (HashMap)resultList.get(i);
    //                if(resultMap.get("SERIES_NO").equals(seriesNo)){
    //                    exists = true;
    //                    break;
    //                }
    //            }
    //        }
    //        return exists;
    //    }

    public String getOperatingCustomer() {
        return operatingCustomer;
    }

    public void setOperatingCustomer(String operatingCustomer) {
        this.operatingCustomer = operatingCustomer;
    }
    
}