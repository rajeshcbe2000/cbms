/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AccountsOB.java
 *
 * Created on September 3, 2003, 07:35 PM
 */

package com.see.truetransact.ui.locker.lockerissue;

import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.*;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientexception.ClientParseException;

import com.see.truetransact.transferobject.operativeaccount.*;
import com.see.truetransact.ui.common.nominee.*;
import com.see.truetransact.ui.common.powerofattorney.*;
import com.see.truetransact.ui.common.authorizedsignatory.*;
import com.see.truetransact.uicomponent.CObservable ;
import java.util.*;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.common.transaction.TransactionOB;

import com.see.truetransact.transferobject.locker.lockerissue.LockerIssueChargesTO;
import com.see.truetransact.transferobject.locker.lockerissue.LockerIssueTO;
import com.see.truetransact.transferobject.locker.lockerissue.LockerPwdDetailsTO;
import com.see.truetransact.transferobject.locker.lockerissue.LockerIssueJointTO;

import com.see.truetransact.commonutil.StringEncrypter;
import com.see.truetransact.transferobject.servicetax.servicetaxdetails.ServiceTaxDetailsTO;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;

/**
 *
 * @author  Pranav
 */
public class LockerIssueOB extends CObservable {
    JointAcctHolderManipulation objJointAcctHolderManipulation  = new JointAcctHolderManipulation();
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String TBL_JNT_ACCNT_COLUMN_1 = "tblJntAccntColumn1";
    private String TBL_JNT_ACCNT_COLUMN_2 = "tblJntAccntColumn2";
    private String TBL_JNT_ACCNT_COLUMN_3 = "tblJntAccntColumn3";
    private String TBL_JNT_ACCNT_COLUMN_4 = "tblJntAccntColumn4";
    // Added New
    private String TBL_JNT_ACCNT_COLUMN_5 = "tblJntAccntColumn5";
    //--- Declaration for DB Yes Or No
    private final String FLD_FOR_DB_YES_NO = "DBYesOrNo";
    private final String YES_FULL_STR = "Yes";
    
    private HashMap map = null;
    private ProxyFactory proxy = null;
    //    private AccountsRB resourceBundle = new AccountsRB();
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.locker.lockerissue.LockerIssueRB", ProxyParameters.LANGUAGE);
    private int actionType;
    private HashMap lookUpHash;
    private String txtBranchCodeAI = "";
    private String txtPrevActNoAI = "";
    private String dtdActOpenDateAI = "";
    private String txtAmoutTransAI = "";
    private String txtRemarksAI = "";
    private String cboProductIdAI = "";
    private String txtCustomerIdAI = "";
    private String txtPrevActNumAI = "";
    
    private String dtdOpeningDateAI = "";
    private String cboActStatusAI = "";
    private String cboConstitutionAI = "";
    private String cboOpModeAI = "";
    private String cboCommAddr = "";
    // private String txtODLimitAI = "";
    //private String cboGroupCodeAI = "";
    // private String cboSettlementModeAI = "";
    private String cboCategory= "";
    private String cboBaseCurrAI = "";
    private String txtAccountNoITP1 = "";
    private String txtBankITP2 = "";
    private String txtBranchITP2 = "";
    private String txtAccountNoITP2 = "";
    private String txtNameITP2 = "";
    private String cboDocTypeITP3 = "";
    private String txtDocNoITP3 = "";
    private String txtIssuedByITP3 = "";
    private String dtdIssuedDateITP3 = "";
    private String dtdExpiryDate = "";
    private String cboIdentityTypeITP4 = "";
    private String txtIssuedAuthITP4 = "";
    private String txtIdITP4 = "";
    private String txtIntroNameOTP5 = "";
    private String txtDesignationOTP5 = "";
    private String txtACodeOTP5 = "";
    private String txtPhoneOTP5 = "";
    private String collectRentMM="";
    private String   collectRentYYYY="";
    private boolean    siYes=false;
    private boolean   siNo=false;
    private boolean   pwdYes=false;
    private boolean   pwdNo=false;
    private String txtPasCustId = "";
    private String txtPassword = "";
    private String txtConPassword = "";
    private String cboProductId = "";
    private ComboBoxModel cbmProdType;
    private ComboBoxModel cbmProdId;
    private String cboProdType;
    private String cboProdId;
    private String txtCustomerIdCr;
    private String lblCustomerNameCrValue;
    private ComboBoxModel cbmProductId;
    private String txtLockerNo = "";
    private String lblLockerKeyNoVal = "";
    private String txtAccNo="";
    private String cboChargeType = "";
    private String tdtFromDt = "";
    private String tdtToDt = "";
    private String txtAmt = "";
    
    private boolean chkPayIntOnCrBalIN = false;
    private boolean chkPayIntOnDrBalIN = false;
    private boolean chkChequeBookAD = false;
    private boolean chkCustGrpLimitValidationAD = false;
    private boolean chkMobileBankingAD = false;
    private boolean chkNROStatusAD = false;
    private boolean chkATMAD = false;
    private String txtATMNoAD = "";
    private String dtdATMFromDateAD = "";
    private String dtdATMToDateAD = "";
    private boolean chkDebitAD = false;
    private String txtDebitNoAD = "";
    private String dtdDebitFromDateAD = "";
    private String dtdDebitToDateAD = "";
    private boolean chkCreditAD = false;
    private String txtCreditNoAD = "";
    private String dtdCreditFromDateAD = "";
    private String dtdCreditToDateAD = "";
    private boolean chkFlexiAD = false;
    private String txtMinBal1FlexiAD = "";
    private String txtMinBal2FlexiAD = "";
    private String txtReqFlexiPeriodAD = "";
    private String cboDMYAD = "";
    private String txtAccOpeningChrgAD = "";
    private String txtMisServiceChrgAD = "";
    private boolean chkStopPmtChrgAD = false;
    private String txtChequeBookChrgAD = "";
    private boolean chkChequeRetChrgAD = false;
    private String txtFolioChrgAD = "";
    private boolean chkInopChrgAD = false;
    private String txtAccCloseChrgAD = "";
    private boolean chkStmtChrgAD = false;
    private boolean chkPassBook = false;
    private boolean chkABBChrgAD = false;
    private boolean chkNPAChrgAD = false;
    private String cboStmtFreqAD = "";
    private boolean chkNonMainMinBalChrgAD = false;
    private String txtExcessWithChrgAD = "";
    private String txtMinActBalanceAD = "";
    private String txtABBChrgAD = "";
    private String dtdNPAChrgAD = "";
    private String dtdDebit = "";
    private String dtdCredit = "";
    private String txtNomineeNameNO = "";
    private String cboNomineeRelationNO = "";
    private String txtNomineePhoneNO = "";
    private String txtNomineeACodeNO = "";
    private boolean rdoMajorNO = false;
    private boolean rdoMinorNO = false;
    private String txtNomineeShareNO = "";
    private String dtdMinorDOBNO = "";
    private String cboRelationNO = "";
    private String txtGuardianNameNO = "";
    private String txtGuardianPhoneNO = "";
    private String txtGuardianACodeNO = "";
    private String txtTotalShareNO = "";
    private String txtPOANamePA = "";
    private String txtPOAPhonePA = "";
    private String txtPOAACodePA = "";
    private String dtdPOAFromDatePA = "";
    private String dtdPOAToDatePA = "";
    private String txtRemarksPA = "";
    private String lblCustValue = "";
    private String lblCityValue = "";
    private String lblDOBValue = "";
    private String lblCountryValue = "";
    private String lblStreetValue = "";
    private String lblStateValue = "";
    private String lblAreaValue = "";
    private String lblPinValue = "";
    private String lblMajOMinVal = "";
    private String prodType="";
    private String txtActName = "";
    private String txtRemarks = "";
    private String ClosedDt = "";
    
    private boolean chkHideBalanceTrans = false;
    private String cboRoleHierarchy = "";
    
    private ComboBoxModel cbmProductIdAI;
    private ComboBoxModel cbmActStatusAI;
    private ComboBoxModel cbmConstitutionAI;
    private ComboBoxModel cbmOpModeAI;
    private ComboBoxModel cbmCommAddr;
    private ComboBoxModel cbmGroupCodeAI;
    private ComboBoxModel cbmSettlementModeAI;
    private ComboBoxModel cbmCategory;
    private ComboBoxModel cbmBaseCurrAI;
    private ComboBoxModel cbmDocTypeITP3;
    private ComboBoxModel cbmIdentityTypeITP4;
    private ComboBoxModel cbmDMYAD;
    private ComboBoxModel cbmStmtFreqAD;
    private ComboBoxModel cbmNomineeRelationNO;
    private ComboBoxModel cbmRelationNO;
    private ComboBoxModel cbmPrevAcctNo;
    private ComboBoxModel cbmRoleHierarchy;
    
    private ComboBoxModel cbmChargeType;
    
    //--- Declarations for Joint Account Table
    public LinkedHashMap jntAcctAll;
    HashMap jntAcctSingleRec;
    private EnhancedTableModel tblJointAccnt;
    private ArrayList tblJointAccntColTitle;
    LinkedHashMap jntAcctTOMap;
    //--- End of Declarations for Joint Account Table
    
    // transfering branch information
    private String lblBranchNameValueAI = "";
    
    // account details
    private String lblActHeadValueAI = "";
    private String lblCustomerNameValueAI = "";
    
    // interest rates
    private String lblRateCodeValueIN = "";
    private String lblCrInterestRateValueIN = "";
    private String lblDrInterestRateValueIN = "";
    private String lblPenalInterestValueIN = "";
    private String lblAgClearingValueIN = "";
    
    // in case of self and existing customer
    private String lblCustomerIdValueITP1 = "";
    private String lblNameValueITP1 = "";
    private String lblActHeadValueITP1 = "";
    private String lblBranchCodeValueITP1 = "";
    private String lblBranchValueITP1 = "";
    
    // some addresses
    private Address customerAddress;
    private Address othersAddress;
    
    // Array list of nominees and PoAs
    private ArrayList nomineeTOList;
    
    //To populate comboboxes
    private ArrayList key;
    private ArrayList value;
    private HashMap keyValue;
    private HashMap lookupMap;
    
    // string for storing the account number
    private String accountNumber = "";
    private String lblAccountNo;
    private String staffOnly;
    private HashMap _authorizeMap;
    
    // store whic operation INSERT/UPDATE/DELETE/LIST is going on
    private int operation;
    
    //To manipulate status message
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    public String addrType = "";
    
    // Final Strings
    private final String YES = "Y";
    private final String NO = "N";
    private final String DIALOG_YES = "dialogYes";
    private final String DIALOG_OK = "dialogOk";
    private final String DIALOG_NO = "dialogNo";
    
    private double tabLenght = 0;
    private Date curDate = null;
    
    /* To Define the Objects of the POA, and Nominee..*/
    PowerOfAttorneyOB poaOB;
    NomineeOB nomineeOB;
    AuthorizedSignatoryOB authSignOB;
    private EnhancedTableModel tbmInstructions2;
    private EnhancedTableModel tbmLockCharges;
    private ArrayList tblHeadings2;
    private ArrayList tblInstruction2;
    private ArrayList tblHeadings3;
    private ArrayList existData = null;
    private boolean deletingExists = false;
    private boolean deletingExistsChrgs = false;
    private ArrayList existingData;
    private ArrayList existingDataChrgs;
    private ArrayList newInstructionRow = null;
    private ArrayList newInstructionRowChrgs = null;
    private ArrayList newData = new ArrayList();
    private ArrayList removedRow;
    private ArrayList removedRowChrgs;
    private ArrayList pwdTabRow;
    private ArrayList chargeTabRow;
    
    LockerPwdDetailsTO objLockerPwdDetailsTO;
    LockerIssueChargesTO objLockerIssueChargesTO;
    
    private LinkedHashMap allowedTransactionDetailsTO = null;
    private LinkedHashMap transactionDetailsTO = null;
    private LinkedHashMap deletedTransactionDetailsTO = null;
    
    private final String DELETED_TRANS_TOs = "DELETED_TRANS_TOs";
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    
    private TransactionOB transactionOB;
    private String txtCharges = "";
    private String txtServiceTax = "";
    
    private StringEncrypter encrypt = null;
    private ArrayList allInstructionsList = new ArrayList();
    private Date authorizeDt = null;
    private String existingPwd = "";
    private boolean rdoFreeze=false;
    private boolean rdoUnFreeze=false;
    private String dtdFreezeDt="";
    private String txtFreezeRemarks="";
    private String slNo="";
    private String chkNoTransaction = "";
    private String lblServiceTaxval = "";
    private HashMap serviceTax_Map = null;// Added by nithya on 23-05-2019 for KD 402 - 0019214: gst problem in locker opening
    
    /** Creates a new instance of AccountsOB */
    public LockerIssueOB() {
        
        // First load the data for combo boxes
        curDate = ClientUtil.getCurrentDate();
        map = new HashMap();
        map.put(CommonConstants.JNDI, "LockerIssueJNDI");
        map.put(CommonConstants.HOME, "locker.lockerissue.LockerIssueHome");
        map.put(CommonConstants.REMOTE, "locker.lockerissue.LockerIssue");
        
        try {
            proxy = ProxyFactory.createProxy();
            fillDropdown();
            setJntAcccntTblCol();
            createTblActHeadings();
            createTbmAct();
            createTblLockHeadings();
            createTbmLockCharges();
            // createTbmPassword();
            encrypt = new StringEncrypter();
        } catch (Exception e) {
            e.printStackTrace();
        }
        tblJointAccnt = new EnhancedTableModel(null, tblJointAccntColTitle);
    }
    
    private  void setJntAcccntTblCol() throws Exception{
        try{
            tblJointAccntColTitle = new ArrayList();
            tblJointAccntColTitle.add(resourceBundle.getString(TBL_JNT_ACCNT_COLUMN_1));
            tblJointAccntColTitle.add(resourceBundle.getString(TBL_JNT_ACCNT_COLUMN_2));
            tblJointAccntColTitle.add(resourceBundle.getString(TBL_JNT_ACCNT_COLUMN_3));
            tblJointAccntColTitle.add(resourceBundle.getString(TBL_JNT_ACCNT_COLUMN_4));
            tblJointAccntColTitle.add(resourceBundle.getString(TBL_JNT_ACCNT_COLUMN_5));
        }catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    private void createTblActHeadings(){
        tblInstruction2 = new ArrayList();
        tblInstruction2.add(resourceBundle.getString("tblHeadingAct1"));
        tblInstruction2.add(resourceBundle.getString("tblInstructionColumn2"));
        tblInstruction2.add(resourceBundle.getString("tblInstructionColumn3"));
    }
    
    /** Creates an instance of EnhancedTableModel with arrayList for heading **/
    private void createTbmAct(){
        tbmInstructions2 = new EnhancedTableModel(null, tblInstruction2);
        allInstructionsList = new ArrayList();
    }
    
    private void createTblLockHeadings(){
        tblHeadings3 = new ArrayList();
        tblHeadings3.add(resourceBundle.getString("tblHeadingLock1"));
        tblHeadings3.add(resourceBundle.getString("tblHeadingLock2"));
        tblHeadings3.add(resourceBundle.getString("tblHeadingLock3"));
        tblHeadings3.add(resourceBundle.getString("tblHeadingLock4"));
        tblHeadings3.add(resourceBundle.getString("tblHeadingLock5"));
    }
    private void createTbmLockCharges(){
        tbmLockCharges = new EnhancedTableModel(null, tblHeadings3);
    }
    
    public void populateSelectedRowAct(int row){
        //        ArrayList data = (ArrayList)tbmInstructions2.getDataArrayList().get(row);
        ArrayList data = (ArrayList)allInstructionsList.get(row);
        //        return data;
        setTxtPasCustId(CommonUtil.convertObjToStr(data.get(0)));
        setTxtPassword(CommonUtil.convertObjToStr(data.get(3)));
        setTxtConPassword(CommonUtil.convertObjToStr(data.get(3)));
        //        setTxtPassword(CommonUtil.convertObjToStr(data.get(1)));
    }
    
    public Map checkPwdTable() {
        Map errorMap = new HashMap();
        if (allInstructionsList!=null && allInstructionsList.size()>0) {
            ArrayList tempList = null;
            for (int i=0; i<allInstructionsList.size(); i++) {
                tempList = (ArrayList)allInstructionsList.get(i);
                if (CommonUtil.convertObjToStr(tempList.get(2)).length()==0) {
                    errorMap.put(tempList.get(0), tempList.get(1));
                }
            }
        }
        System.out.println("#$@#!@ checkPwdTable() : "+errorMap);
        return errorMap;
    }
    
    public void populateSelectedRowLockCharges(int row){
        ArrayList data = (ArrayList)tbmLockCharges.getDataArrayList().get(row);
        //        return data;
        setCboChargeType(CommonUtil.convertObjToStr(data.get(0)));
        setTdtFromDt(CommonUtil.convertObjToStr(data.get(1)));
        setTdtToDt(CommonUtil.convertObjToStr(data.get(2)));
        setTxtAmt(CommonUtil.convertObjToStr(data.get(3)));
        setTxtServiceTax(CommonUtil.convertObjToStr(data.get(4)));
        
        //        setTxtPassword(CommonUtil.convertObjToStr(data.get(1)));
    }
    public void setTableValueAt(int row){
        deletingExists = true;
        final ArrayList data = tbmInstructions2.getDataArrayList();
        if(existingData!=null){
            existingData.add(data.get(row));
        }
        //        if(!getCbmStdInstruction().getKeyForSelected().equals("OTHERS")){
        tbmInstructions2.setValueAt(getTxtPasCustId(),row,0);
        ArrayList tempList = (ArrayList)allInstructionsList.get(row);
        tempList.set(0, getTxtPasCustId());
        tempList.set(1, getTxtPassword());
        tempList.set(2, getTxtPassword());
        allInstructionsList.set(row, tempList);
        tempList = null;
        System.out.println("#$#$@#!@ allInstructionsList in setTableValueAt : "+allInstructionsList);
        
        //            tbmInstructions.setValueAt(getCboStdInstruction(),row,1);
        //            tbmInstructions2.setValueAt(getTxtPassword(),row,3);
        //            tbmInstructions2.setValueAt(getTxtConPassword(),row,4);
        //        }else{
        //            tbmInstructions.setValueAt(getTxtStdInstruction(),row,1);
        //        }
        //         setTxtTotalAmt(calculateTotalAmount(data));
        //         setTxttotalServTax(calculateTotalServTaxAmount(data));
    }
    
    public void setTableValueLockCharges(int row){
        deletingExistsChrgs = true;
        final ArrayList data = tbmLockCharges.getDataArrayList();
        if(existingDataChrgs!=null){
            existingDataChrgs.add(data.get(row));
        }
        //        if(!getCbmStdInstruction().getKeyForSelected().equals("OTHERS")){
        tbmLockCharges.setValueAt(getCboChargeType(),row,0);
        //            tbmInstructions.setValueAt(getCboStdInstruction(),row,1);
        tbmLockCharges.setValueAt(getTdtFromDt(),row,1);
        tbmLockCharges.setValueAt(getTdtToDt(),row,2);
        tbmLockCharges.setValueAt(getTxtAmt(),row,3);
        tbmLockCharges.setValueAt(getTxtServiceTax(),row,4);
        
        //        }else{
        //            tbmInstructions.setValueAt(getTxtStdInstruction(),row,1);
        //        }
        //         setTxtTotalAmt(calculateTotalAmount(data));
        //         setTxttotalServTax(calculateTotalServTaxAmount(data));
    }
    
    
    public int addTblInstructionData(){
        int optionSelected = -1;
        String columnData = "";
        //        getCbmStdInstruction().setSelectedItem(getCboStdInstruction());
        //        if(!getCbmStdInstruction().getKeyForSelected().equals("OTHERS")){
        //            columnData = CommonUtil.convertObjToStr(getCbmStdInstruction().getKeyForSelected());
        //            columnData = CommonUtil.convertObjToStr(getCboStdInstruction());
        columnData = CommonUtil.convertObjToStr(getTxtPasCustId());
        columnData = CommonUtil.convertObjToStr(getTxtPassword());
        //        }else{
        //            columnData = getTxtStdInstruction();
        columnData = CommonUtil.convertObjToStr(getTxtConPassword());
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
            newInstructionRow.add(getTxtPasCustId());
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
                    }else if(optionSelected == 1){
                        // Newly Entered data already exists and the user doesn't wants to modify it
                        //                        resetInstructions();
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
    
    public int addTblLockChargesData(){
        int optionSelected = -1;
        String columnData = "";
        //        getCbmStdInstruction().setSelectedItem(getCboStdInstruction());
        //        if(!getCbmStdInstruction().getKeyForSelected().equals("OTHERS")){
        //            columnData = CommonUtil.convertObjToStr(getCbmStdInstruction().getKeyForSelected());
        //            columnData = CommonUtil.convertObjToStr(getCboStdInstruction());
        columnData = CommonUtil.convertObjToStr(getCbmChargeType().getKeyForSelected());
        columnData = CommonUtil.convertObjToStr(getTdtFromDt());
        columnData = CommonUtil.convertObjToStr(getTdtToDt());
        columnData = CommonUtil.convertObjToStr(getTxtServiceTax());
        //        }else{
        //            columnData = getTxtStdInstruction();
        columnData = CommonUtil.convertObjToStr(getTxtAmt());
        
        //            columnData = CommonUtil.convertObjToStr(getTxtServiceTax());
        //        }
        try{
            if (newInstructionRowChrgs == null) {
                newInstructionRowChrgs = new ArrayList();
            }
            newInstructionRowChrgs.add(new Integer(tbmLockCharges.getRowCount()+1));
            //            if(!getCbmStdInstruction().getKeyForSelected().equals("OTHERS")){
            //////                newInstructionRow.add(getCbmStdInstruction().getKeyForSelected());
            //                newInstructionRow.add(getCboStdInstruction());
            newInstructionRowChrgs.add(getCbmChargeType().getKeyForSelected());
            newInstructionRowChrgs.add(getTdtFromDt());
            newInstructionRowChrgs.add(getTdtToDt());
            newInstructionRowChrgs.add(getTxtAmt());
            newInstructionRowChrgs.add(getTxtServiceTax());
            //                newInstructionRow.add(getTxtServiceTax());
            //            }else{
            //                newInstructionRow.add(getTxtStdInstruction());
            //                newInstructionRow.add(getTxtAmount());
            //                newInstructionRow.add(getTxtServiceTax());
            //            }
            ArrayList data = tbmLockCharges.getDataArrayList();
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
                    }else if(optionSelected == 1){
                        // Newly Entered data already exists and the user doesn't wants to modify it
                        //                        resetInstructions();
                    }
                    break;
                }
                
            }
            if (!exist){
                //The condition that the Entered data is not in the table
                doNewDataChrgs();
                insertNewDataChrgs();
            }
            //            setTxtTotalAmt(calculateTotalAmount(data));
            //            setTxttotalServTax(calculateTotalServTaxAmount(data));
            newInstructionRow = null;
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return optionSelected;
    }
    
    public void deleteSelectedRow(int row){
        final ArrayList data = tbmInstructions2.getDataArrayList();
        if(removedRow!=null){
            removedRow.add(data.get(row));
        }
        tbmInstructions2.removeRow(row);
        allInstructionsList.remove(row);
        System.out.println("#$#$@#!@ allInstructionsList in deleteSelectedRow : "+allInstructionsList);
        
        //        for(int i=0; i<tbmInstructions2.getRowCount(); i++){
        //            tbmInstructions2.setValueAt(new Integer(i+1),i,0);
        //        }
        //        resetInstructions();
        //        setTxtTotalAmt(calculateTotalAmount(data));
        //        setTxttotalServTax(calculateTotalServTaxAmount(data));
        //        notifyObservers();
    }
    
    
    public void deleteSelectedCustIDPwd(String custID){
        final ArrayList data = tbmInstructions2.getDataArrayList();
        int row = -1;
        for (int i=0; i<data.size(); i++) {
            if (CommonUtil.convertObjToStr(tbmInstructions2.getValueAt(i, 0)).equals(custID)) {
                row = i;
                break;
            }
        }
        if (row!=-1) {
            if(removedRow!=null){
                removedRow.add(data.get(row));
            }
            tbmInstructions2.removeRow(row);
            allInstructionsList.remove(row);
        }
        System.out.println("#$#$@#!@ allInstructionsList in deleteSelectedRow : "+allInstructionsList);
        
    }
    
    
    public void deleteSelectedRowChrgs(int row){
        final ArrayList data = tbmLockCharges.getDataArrayList();
        if(removedRowChrgs!=null){
            removedRowChrgs.add(data.get(row));
        }
        tbmLockCharges.removeRow(row);
        for(int i=0; i<tbmLockCharges.getRowCount(); i++){
            tbmLockCharges.setValueAt(new Integer(i+1),i,0);
        }
        //        resetInstructions();
        //        setTxtTotalAmt(calculateTotalAmount(data));
        //        setTxttotalServTax(calculateTotalServTaxAmount(data));
        //        notifyObservers();
    }
    
    private void doNewData(){
        newData.add(newInstructionRow);
    }
    private void doNewDataChrgs(){
        newData.add(newInstructionRowChrgs);
    }
    private void insertNewData() throws Exception{
        //final TerminalTO objTerminalTO = new TerminalTO();
        int row = tbmInstructions2.getRowCount();
        tbmInstructions2.insertRow(row,newInstructionRow);
    }
    
    private void insertNewDataChrgs() throws Exception{
        //final TerminalTO objTerminalTO = new TerminalTO();
        int row = tbmLockCharges.getRowCount();
        tbmLockCharges.insertRow(row,newInstructionRowChrgs);
    }
    private void updateTbmInstructions(int row) throws Exception{
        //        if(!getCbmStdInstruction().getKeyForSelected().equals("OTHERS")){
        //            tbmInstructions.setValueAt(getCbmStdInstruction().getKeyForSelected(), row, 1);
        //            tbmInstructions.setValueAt(getCboStdInstruction(), row, 1);
        //        }else{
        tbmInstructions2.setValueAt(getTxtPasCustId(), row, 1);
        ArrayList tempList = (ArrayList)allInstructionsList.get(row);
        tempList.set(0, getTxtPasCustId());
        allInstructionsList.set(row, tempList);
        System.out.println("#$#$@#!@ allInstructionsList in updateTbmInstructions : "+allInstructionsList);
        tempList = null;
        //        }
        
    }
    
    public void removeTbmActRow(){
        tbmInstructions2 = new EnhancedTableModel(null,tblInstruction2);
        setTbmInstructions2(tbmInstructions2);
        allInstructionsList = new ArrayList();
    }
    public void removeTbmLockChrgsRow(){
        tbmLockCharges = new EnhancedTableModel(null,tblHeadings3);
        setTbmLockCharges(tbmLockCharges);
    }
    /** To fill the comboboxes */
    private void fillDropdown() throws Exception{
        lookupMap = new HashMap();
        lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
        lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
        lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
        
        final HashMap param = new HashMap();
        
        param.put(CommonConstants.MAP_NAME, "getLockerProducts");
        param.put(CommonConstants.PARAMFORQUERY, null);
        HashMap lookupValues = ClientUtil.populateLookupData(param);
        fillData((HashMap)lookupValues.get(CommonConstants.DATA));
        int j=value.size();
        for(int i=1;i<j;i++){
            value.set(i, (String)value.get(i)+" ("+(String)key.get(i)+")");
        }
        cbmProductIdAI = new ComboBoxModel(key,value);
        
        param.put(CommonConstants.MAP_NAME,null);
        
        final ArrayList lookupKey = new ArrayList();
        lookupKey.add("ACCOUNTSTATUS");
        lookupKey.add("ACT.CONSTITUTION");
        lookupKey.add("ACT_OP_MODE");
        //        lookupKey.add("CUSTOMER.ADDRTYPE");
        lookupKey.add("CUSTOMER.CUSTOMERGROUP");
        lookupKey.add("SETTLEMENT_MODE");
        lookupKey.add("CATEGORY");
        lookupKey.add("CURRENCYTYPE");
        lookupKey.add("INTRO_DOCUMENT");
        lookupKey.add("INDENTITY_TYPE");
        lookupKey.add("PERIOD");
        lookupKey.add("FREQUENCY");
        lookupKey.add("RELATIONSHIP");
        lookupKey.add("ROLE_HIERARCHY");
        lookupKey.add("LOCKER.CHARGES");
        lookupKey.add("PRODUCTTYPE");        
        param.put(CommonConstants.PARAMFORQUERY, lookupKey);
        
        lookupValues = ClientUtil.populateLookupData(param);
        
        fillData((HashMap)lookupValues.get("ACCOUNTSTATUS"));
        cbmActStatusAI = new ComboBoxModel(key, value);
        
        fillData((HashMap)lookupValues.get("ACT.CONSTITUTION"));
        cbmConstitutionAI = new ComboBoxModel(key, value);
        
        fillData((HashMap)lookupValues.get("ACT_OP_MODE"));
        cbmOpModeAI = new ComboBoxModel(key, value);
        
        //        fillData((HashMap)lookupValues.get("CUSTOMER.ADDRTYPE"));
        //        cbmCommAddr = new ComboBoxModel(key, value);
        
        fillData((HashMap)lookupValues.get("CUSTOMER.CUSTOMERGROUP"));
        cbmGroupCodeAI = new ComboBoxModel(key,value);
        
        fillData((HashMap)lookupValues.get("SETTLEMENT_MODE"));
        cbmSettlementModeAI = new ComboBoxModel(key, value);
        
        fillData((HashMap)lookupValues.get("CATEGORY"));
        cbmCategory = new ComboBoxModel(key, value);
        
        fillData((HashMap)lookupValues.get("CURRENCYTYPE"));
        cbmBaseCurrAI = new ComboBoxModel(key,value);
        
        
        fillData((HashMap)lookupValues.get("PRODUCTTYPE"));  
        cbmProdType = new ComboBoxModel(key,value);
        // getKeyValue(HashMap)keyValue.get("PROD");
        // cbmProdId=new ComboBoxModel(key,value);
        fillData((HashMap)lookupValues.get("INTRO_DOCUMENT"));
        cbmDocTypeITP3 = new ComboBoxModel(key, value);
        
        fillData((HashMap)lookupValues.get("INDENTITY_TYPE"));
        cbmIdentityTypeITP4 = new ComboBoxModel(key, value);
        
        fillData((HashMap)lookupValues.get("PERIOD"));
        cbmDMYAD = new ComboBoxModel(key, value);
        
        fillData((HashMap)lookupValues.get("FREQUENCY"));
        cbmStmtFreqAD = new ComboBoxModel(key,value);
        
        fillData((HashMap)lookupValues.get("RELATIONSHIP"));
        cbmNomineeRelationNO = new ComboBoxModel(key, value);
        
        fillData((HashMap)lookupValues.get("RELATIONSHIP"));
        cbmRelationNO = new ComboBoxModel(key, value);
        
        fillData((HashMap)lookupValues.get("ROLE_HIERARCHY"));
        cbmRoleHierarchy = new ComboBoxModel(key, value);
        
        fillData((HashMap)lookupValues.get("LOCKER.CHARGES"));
        cbmChargeType = new ComboBoxModel(key, value);
        
        cbmPrevAcctNo=new ComboBoxModel(new ArrayList(),new ArrayList());
        //        cbmChargeType=new ComboBoxModel(new ArrayList(),new ArrayList());
    }
    
    /** To set the key & value for comboboxes */
    private void fillData(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    /** To get data for comboboxes */
    private HashMap populateDataLocal(HashMap obj)  throws Exception{
        keyValue = proxy.executeQuery(obj,lookupMap);
        
        return keyValue;
    }
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    /** Sets the Joint account holder Tbale
     *  @param Enhanced Table Model that has to be set
     */
    void setTblJointAccnt(EnhancedTableModel tblJointAccnt){
        this.tblJointAccnt = tblJointAccnt;
        setChanged();
    }
    
    /** Returns the Joint AccountHolder Table
     */
    EnhancedTableModel getTblJointAccnt(){
        return this.tblJointAccnt;
    }
    
    /** this mehtod will return the branch name, based on the branch code
     */
    public String getBranchNameForCode(String branchCode) {
        /*
         * may be there is nothing in the branchCode, so it will be ""
         */
        if (branchCode == null || branchCode.equals("")) {
            return "";
        }
        
        HashMap hash, keyValue = null;
        ArrayList data = null;
        try {
            hash = new HashMap();
            // get the customerdetails from database
            hash.put(CommonConstants.MAP_NAME, "getBranchDetails");
            hash.put(CommonConstants.MAP_WHERE, branchCode);
            keyValue = (HashMap) proxy.executeQuery(hash, map);
            
            // get the actual ArrayList which will have the CustomerDeatils info
            data = (ArrayList) keyValue.get("BranchDetails");
            if(data!=null) {
                return (String)((HashMap)data.get(0)).get("branchName");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return "";
    }
    
    public void populateTblActData(HashMap mapData){
        //        if(subRegType.equals("CPD")){
        //        if(mapData.get("PRODUCT_ID").equals("CPD")){
        if (allInstructionsList==null) {
            allInstructionsList = new ArrayList();
        }
        //                        createTbmAct();
        existData = new ArrayList();
        existData.add(mapData.get("CUSTOMER_ID"));
        //existData.add(mapData.get("NAME"));
        existData.add("");
        existData.add("");
        //                    existData.add(mapData.get("CUSTOMER_TYPE"));
        //                    existData.add(mapData.get(""));
        //                    existData.add(mapData.get(""));
        ArrayList tempList = new ArrayList();
        tempList.addAll(existData);
        System.out.println("#$#!! getExistingPwd():"+getExistingPwd());
        if (getExistingPwd().length()==0) {
            tempList.add(mapData.get(""));
            tempList.add(mapData.get(""));
        } else {
            tempList.add(getExistingPwd());
            tempList.add(getExistingPwd());
        }
        existingPwd = "";
        //                    if(tbmInstructions2.getRowCount() == 0){
        String addCust = "";
        if (mapData.containsKey("ADDCUST")) {
            addCust = CommonUtil.convertObjToStr(mapData.get("ADDCUST"));
        }
        if (addCust.equals("JOINT")) {
            tbmInstructions2.insertRow(tbmInstructions2.getRowCount(), existData);
            allInstructionsList.add(allInstructionsList.size(), tempList);
        }else{
            if (tbmInstructions2.getRowCount()!=0) {
                tbmInstructions2.removeRow(0);
                allInstructionsList.remove(0);
            }
            tbmInstructions2.insertRow(0, existData);
            allInstructionsList.add(0, tempList);
        }
        System.out.println("#$#$@#!@ allInstructionsList in populateTblActData : "+allInstructionsList);
        tempList = null;
        //        }else{
        //                    existData = new ArrayList();
        //                    existData.add(mapData.get("PRODUCT_ID"));
        //                    existData.add(mapData.get("ACT_NUM"));
        //                    existData.add(mapData.get("ACT_NAME"));
        ////                    tbmInstructions2.insertRow(tbmInstructions2.getRowCount(), existData);
        //                    if(tbmInstructions2.getRowCount() == 1){
        //                    tbmInstructions2.insertRow(tbmInstructions2.getRowCount(), existData);
        //                    }else{
        //                        tbmInstructions2.removeRow(1);
        //                        tbmInstructions2.insertRow(1, existData);
        //                    }
        //        }
        //        }else{
        //
        //            existData = new ArrayList();
        //            existData.add(mapData.get("PRODUCT_ID"));
        //            existData.add(mapData.get("ACT_NUM"));
        //            existData.add(mapData.get("ACT_NAME"));
        //            if(tbmInstructions2.getRowCount() == 0){
        //                    tbmInstructions2.insertRow(tbmInstructions2.getRowCount(), existData);
        //                    }else{
        //                        tbmInstructions2.removeRow(0);
        //                        tbmInstructions2.insertRow(0, existData);
        //                    }
        //        }
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
    
    
    public void moveLockerPwdToMain(String mainCustID, String selectedCustID, int row){
        
        if (allInstructionsList==null) {
            allInstructionsList = new ArrayList();
        }
        existData = (ArrayList) allInstructionsList.get(row);
        allInstructionsList.remove(row);
        allInstructionsList.add(0, existData);
        
        ArrayList tempList = (ArrayList)tbmInstructions2.getDataArrayList().get(row);
        tbmInstructions2.removeRow(row);
        tbmInstructions2.insertRow(0, tempList);
        
        System.out.println("#$#$@#!@ allInstructionsList in moveLockerPwdToMain : "+allInstructionsList);
        tempList = null;
    }
    
    public void populateLockerCharges(String prodID){
        //        if(subRegType.equals("CPD")){
        //        if(mapData.get("PRODUCT_ID").equals("CPD")){
        HashMap param = new HashMap();
        param.put("PROD_ID", prodID);
        List lstData = (List) ClientUtil.executeQuery("selectLockerProdChargesTO", param);
        param = null;
        if(lstData != null && lstData.size() > 0){
            for(int i=0; i < lstData.size(); i++){
                param = (HashMap) lstData.get(i);
                existData = new ArrayList();
                existData.add(param.get("CHARGE_TYPE"));
                existData.add(param.get("START_DT"));
                existData.add(param.get("END_DT"));
                existData.add(param.get("COMMISION"));
                existData.add(param.get("SERVICE_TAX"));
                tbmLockCharges.insertRow(tbmLockCharges.getRowCount(), existData);
                
            }
        }
        
        //                    if(tbmInstructions2.getRowCount() == 0){
        
        //                    }else{
        //                        tbmInstructions2.removeRow(0);
        //                        tbmInstructions2.insertRow(0, existData);
        //                    }
        //        }else{
        //                    existData = new ArrayList();
        //                    existData.add(mapData.get("PRODUCT_ID"));
        //                    existData.add(mapData.get("ACT_NUM"));
        //                    existData.add(mapData.get("ACT_NAME"));
        ////                    tbmInstructions2.insertRow(tbmInstructions2.getRowCount(), existData);
        //                    if(tbmInstructions2.getRowCount() == 1){
        //                    tbmInstructions2.insertRow(tbmInstructions2.getRowCount(), existData);
        //                    }else{
        //                        tbmInstructions2.removeRow(1);
        //                        tbmInstructions2.insertRow(1, existData);
        //                    }
        //        }
        //        }else{
        //
        //            existData = new ArrayList();
        //            existData.add(mapData.get("PRODUCT_ID"));
        //            existData.add(mapData.get("ACT_NUM"));
        //            existData.add(mapData.get("ACT_NAME"));
        //            if(tbmInstructions2.getRowCount() == 0){
        //                    tbmInstructions2.insertRow(tbmInstructions2.getRowCount(), existData);
        //                    }else{
        //                        tbmInstructions2.removeRow(0);
        //                        tbmInstructions2.insertRow(0, existData);
        //                    }
        //        }
    }
    
    /** set the value of Account head ID and description based on the product selected
     * in the UI
     * this method will use the LookupMap
     */
    public String getAccountHeadForProductId(String productId) {
        
        /* may be the screen has been cleared, in that scenario we will have
         * the cboProductId as "", and we don;t want anything to be shown in
         * place of the account head description
         */
        if (productId == null || productId.equals("")) {
            return "";
        }
        /* based on the selection from the product combo box, one accound head
         * will be fetched from database and displayed on screen
         * same LookUp bean will be used for this purpose
         */
        HashMap hash, keyValue;
        ArrayList key, value;
        
        hash = new HashMap();
        hash.put(CommonConstants.MAP_NAME,"getAccHead");
        hash.put(CommonConstants.PARAMFORQUERY, productId);
        keyValue = (HashMap)(ClientUtil.populateLookupData(hash)).get(CommonConstants.DATA);
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
        return key.get(1) + " [" + value.get(1) + "]";
    }
    
    /** this mehtod will return the customer name and customer address as
     * two elements in the hashmap
     * this method will use the AccountMap
     */
    public HashMap getDetailsForCustomerId(String customerId) {
        
        HashMap hash, keyValue = null;
        ArrayList data;
        try {
            hash = new HashMap();
            // get the customerdetails from database
            hash.put(CommonConstants.MAP_NAME, "getCustomerDetails");
            hash.put(CommonConstants.MAP_WHERE, customerId);
            keyValue = (HashMap) proxy.executeQuery(hash, map);
            
            // get the actual ArrayList which will have the CustomerDeatils info
            data = (ArrayList) keyValue.get("CustomerDetails");
            
            // create a hashmap with customername and customeraddress
            keyValue = new HashMap();
            keyValue.put("NAME", ((HashMap)data.get(0)).get("customerName"));
            
            Address temp = new Address();
            temp.setStreet((String)((HashMap)data.get(0)).get("street"));
            temp.setArea((String)((HashMap)data.get(0)).get("area"));
            temp.setCountry((String)((HashMap)data.get(0)).get("countryCode"));
            temp.setState((String)((HashMap)data.get(0)).get("state"));
            temp.setCity((String)((HashMap)data.get(0)).get("city"));
            temp.setPincode((String)((HashMap)data.get(0)).get("pinCode"));
            keyValue.put("ADDRESS", temp);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return keyValue;
    }
    
    /** this mehtod will return the basic account information for the transfering
     * account, as the data elsements in the hashmap
     */
    public HashMap getTransActDetails(String actNum) {
        /*
         * may be there is nothing in the actNum, so it will be ""
         */
        if (actNum == null || actNum.equals("")) {
            return null;
        }
        HashMap hash, keyValue = null;
        ArrayList data;
        try {
            hash = new HashMap();
            // get the customerdetails from database
            hash.put(CommonConstants.MAP_NAME, "getTransActDetails");
            hash.put(CommonConstants.MAP_WHERE, actNum);
            keyValue = (HashMap) proxy.executeQuery(hash, map);
            
            // get the actual ArrayList which will have the Account Details info
            data = (ArrayList) keyValue.get("TransActDetails");
            
            // create a hashmap with account deatils
            keyValue = new HashMap();
            if (data.size() > 0){
                keyValue.put("ACT_NUM", ((HashMap)data.get(0)).get("actNum"));
                keyValue.put("CREATE_DT", ((HashMap)data.get(0)).get("createDate"));
                keyValue.put("CLEAR_BALANCE", ((HashMap)data.get(0)).get("clearBalance"));
                keyValue.put("REMARKS", ((HashMap)data.get(0)).get("remarks"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return keyValue;
    }
    public void getPreviousAccountDetails(){
        try{
            HashMap hash=new HashMap();
            hash.put(CommonConstants.MAP_NAME, "getPrevoisAccountDetails");
            hash.put(CommonConstants.MAP_WHERE, (String)cbmPrevAcctNo.getKeyForSelected());
            hash = (HashMap) proxy.executeQuery(hash, map);
            List list=(List)hash.get("getPrevoisAccountDetails");
            if(list!=null) {
                hash=(HashMap)list.get(0);
            }
            this.setTxtAmoutTransAI(((java.math.BigDecimal)hash.get("AVLB")).toString());
            this.setDtdActOpenDateAI(DateUtil.getStringDate((Date)hash.get("OPENINGDATE")));
            setChanged();
            notifyObservers();
        }catch(Exception e){
            e.printStackTrace();
            this.setResult(ClientConstants.ACTIONTYPE_FAILED);
        }
    }
    public void populatePreviousAccounts(){
        final HashMap param = new HashMap();
        try{
            param.put(CommonConstants.MAP_NAME, "getPreviousAccountList");
            param.put(CommonConstants.PARAMFORQUERY, this.getTxtBranchCodeAI());
            HashMap lookupValues = ClientUtil.populateLookupData(param);
            fillData((HashMap)lookupValues.get(CommonConstants.DATA));
            this.cbmPrevAcctNo = new ComboBoxModel(key,value);
            setChanged();
            notifyObservers();
            param.put(CommonConstants.MAP_NAME,null);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /** Populate the Customer details
     */
    public void populateScreen(HashMap queryWhereMap, boolean jntAcctNewClicked, PowerOfAttorneyOB objPOAOB) {
        try {
            String strPrevMainCust_ID = getTxtCustomerIdAI();
            queryWhereMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            System.out.println("queryWhereMap%%%%"+queryWhereMap);
            List custListData = ClientUtil.executeQuery("getSelectAccInfoDisplay",queryWhereMap );
            if(custListData != null && custListData.size() > 0){
                HashMap custMapData;
                custMapData = (HashMap) custListData.get(0);
                if(jntAcctNewClicked==false){//--- If it is Main acctnt,set CustomerId in Main
                    setTxtCustomerIdAI(CommonUtil.convertObjToStr(custMapData.get("CUST_ID")));
                    objJointAcctHolderManipulation.setJntAcctTableData(custMapData, false, tblJointAccnt);
                    setTblJointAccnt(objJointAcctHolderManipulation.getTblJointAccnt());
                }
                
                
                setLblCustValue(CommonUtil.convertObjToStr(custMapData.get("Name")));
                setLblDOBValue(DateUtil.getStringDate((java.util.Date)custMapData.get("DOB")));
                setLblStreetValue(CommonUtil.convertObjToStr(custMapData.get("STREET")));
                setLblAreaValue(CommonUtil.convertObjToStr(custMapData.get("AREA")));
                setLblCityValue(CommonUtil.convertObjToStr(custMapData.get("CITY1")));
                setLblStateValue(CommonUtil.convertObjToStr(custMapData.get("STATE1")));
                setLblCountryValue(CommonUtil.convertObjToStr(custMapData.get("COUNTRY")));
                setLblPinValue(CommonUtil.convertObjToStr(custMapData.get("PIN_CODE")));
                if(custMapData.get("MINOR")!=null && custMapData.get("MINOR").equals("Y")) {
                    setLblMajOMinVal("MINOR");
                } else {
                    setLblMajOMinVal("MAJOR");
                }
                
                //__ Cust Arrr Type..
                setAddrType(CommonUtil.convertObjToStr(custMapData.get("COMM_ADDR_TYPE")));
                
                //__ Data for the Power Of Attorney...
                String strPrevMainCust = objPOAOB.getCustName(strPrevMainCust_ID);
                String strCust = objPOAOB.getCustName(CommonUtil.convertObjToStr(queryWhereMap.get("CUST_ID")));
                if (!jntAcctNewClicked){
                    // Remove the previous main customer, when the main customer's populate button pressed
                    if (strPrevMainCust != "" && objPOAOB.getCbmPoACust().containsElement(strPrevMainCust)){
                        objPOAOB.getCbmPoACust().removeKeyAndElement(strPrevMainCust_ID);
                    }
                }
                if (!objPOAOB.getCbmPoACust().containsElement(strCust)){
                    objPOAOB.getCbmPoACust().addKeyAndElement(queryWhereMap.get("CUST_ID"), strCust);
                }
                
                
                custListData = null;
                custMapData=null;
            } else {
                ClientUtil.displayAlert("Invalid Customer Id No");
            }
        } catch( Exception e ) {
            parseException.logException(e,true);
        }
    }
    
    /** Populates the Joint Account Holder Table
     */
    public void populateJointAccntTable(HashMap queryWhereMap, PowerOfAttorneyOB objPOA){
        try {
            jntAcctAll =  objJointAcctHolderManipulation.populateJointAccntTable(queryWhereMap, jntAcctAll, tblJointAccnt);
            setTblJointAccnt(objJointAcctHolderManipulation.getTblJointAccnt());
            
            String strCust = objPOA.getCustName(CommonUtil.convertObjToStr(queryWhereMap.get("CUST_ID")));
            if (!objPOA.getCbmPoACust().containsElement(strCust)){
                objPOA.getCbmPoACust().addKeyAndElement(queryWhereMap.get("CUST_ID"), strCust);
            }
            
        } catch( Exception e ) {
            parseException.logException(e,true);
        }
    }
    
    /** Swaps the Selected Joint Acccount Holder and the Main Account Holder to
     *  each other
     */
    public void moveToMain(String mainAccntRow, String strRowSelected , int intRowSelected){
        jntAcctAll = objJointAcctHolderManipulation.moveToMain(mainAccntRow, strRowSelected, intRowSelected, tblJointAccnt, jntAcctAll);
        setTxtCustomerIdAI(strRowSelected);
        setTblJointAccnt(objJointAcctHolderManipulation.getTblJointAccnt());
    }
    
    /** Deletes the Joint Account Holder data from the table
     */
    public void delJointAccntHolder(String strDelRowCount, int intDelRowCount, PowerOfAttorneyOB objPOAOB){
        jntAcctAll =  objJointAcctHolderManipulation.delJointAccntHolder(strDelRowCount, intDelRowCount, tblJointAccnt, jntAcctAll);
        setTblJointAccnt(objJointAcctHolderManipulation.getTblJointAccnt());
        
        if (objPOAOB.getCbmPoACust().containsElement(objPOAOB.getCustName(strDelRowCount))){
            objPOAOB.getCbmPoACust().removeKeyAndElement(strDelRowCount);
        }
    }
    
    /** Resets the customer details
     */
    public void resetCustDetails(){
        setLblCustValue("");
        setLblDOBValue("");
        setLblStreetValue("");
        setLblAreaValue("");
        setLblCityValue("");
        setLblStateValue("");
        setLblCountryValue("");
        setLblPinValue("");
        setLblMajOMinVal("");
    }
    
    /** this mehtod will return the SELF_CUSTOMER introducer's details as a hashmap
     * this method will use the AccountMap
     */
    public HashMap getProductInterestRates(String productId) {
        
        HashMap hash, keyValue = null;
        ArrayList data;
        try {
            hash = new HashMap();
            // get the customerdetails from database
            hash.put(CommonConstants.MAP_NAME, "getProductInterests");
            hash.put(CommonConstants.MAP_WHERE, productId);
            keyValue = (HashMap) proxy.executeQuery(hash, map);
            
            // get the actual ArrayList which will have the CustomerDeatils info
            data = (ArrayList) keyValue.get("ProductInterests");
            
            // create a hashmap with introducer's details
            keyValue = new HashMap();
            if (data.size() > 0){
                keyValue.put("APPL_CR_INT_RATE", ((HashMap)data.get(0)).get("crInterestRate"));
                keyValue.put("APPL_DEBIT_INT_RATE", ((HashMap)data.get(0)).get("drInterestRate"));
                keyValue.put("PENAL_INT_DEBIT_BALACCT", ((HashMap)data.get(0)).get("penalInterestRate"));
                keyValue.put("AG_CLEARING", ((HashMap)data.get(0)).get("agClearing"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return keyValue;
    }
    
    /* This method is used to reset the OB values on this class for PoA which are
     * mapped to some values in the UI class. Reset the values and then
     * call notifyObservers()
     */
    public void resetPoAOBFields() {
        txtPOANamePA = "";
        txtPOAPhonePA = "";
        txtPOAACodePA = "";
        dtdPOAFromDatePA = "";
        dtdPOAToDatePA = "";
        txtRemarksPA = "";
    }
    
    /* This method is used to reset the OB values on this class which are
     * mapped to some values in the UI class. Reset the values and then
     * call notifyObservers()
     */
    public void resetOBFields() {
        txtLockerNo = "";
        lblLockerKeyNoVal = "";
        txtBranchCodeAI = "";
        txtPrevActNoAI = "";
        dtdActOpenDateAI = "";
        txtAmoutTransAI = "";
        txtRemarksAI = "";
        cboProductIdAI = "";
        txtCustomerIdAI = "";
        txtActName = "";
        txtRemarks = "";
        txtPrevActNumAI = "";
        dtdOpeningDateAI = "";
        cboActStatusAI = "";
        cboConstitutionAI = "";
        cboOpModeAI = "";
        cboCommAddr = "";
        collectRentMM="";
        collectRentYYYY="";
        siYes=false;
        siNo=false;
        pwdYes=false;
        pwdNo=false;
        //    txtODLimitAI = "";
        //  cboGroupCodeAI = "";
        //  cboSettlementModeAI = "";
        cboCategory= "";
        cboBaseCurrAI = "";
        txtAccountNoITP1 = "";
        txtBankITP2 = "";
        txtBranchITP2 = "";
        txtAccountNoITP2 = "";
        txtNameITP2 = "";
        cboDocTypeITP3 = "";
        txtDocNoITP3 = "";
        txtIssuedByITP3 = "";
        dtdIssuedDateITP3 = "";
        dtdExpiryDate = "";
        cboIdentityTypeITP4 = "";
        txtIssuedAuthITP4 = "";
        txtIdITP4 = "";
        txtIntroNameOTP5 = "";
        txtDesignationOTP5 = "";
        txtACodeOTP5 = "";
        txtPhoneOTP5 = "";
        chkPayIntOnCrBalIN = false;
        chkPayIntOnDrBalIN = false;
        chkChequeBookAD = false;
        chkCustGrpLimitValidationAD = false;
        chkMobileBankingAD = false;
        chkNROStatusAD = false;
        chkATMAD = false;
        txtATMNoAD = "";
        dtdATMFromDateAD = "";
        dtdATMToDateAD = "";
        chkDebitAD = false;
        txtDebitNoAD = "";
        dtdDebitFromDateAD = "";
        dtdDebitToDateAD = "";
        chkCreditAD = false;
        txtCreditNoAD = "";
        dtdCreditFromDateAD = "";
        dtdCreditToDateAD = "";
        chkFlexiAD = false;
        txtMinBal1FlexiAD = "";
        txtMinBal2FlexiAD = "";
        txtReqFlexiPeriodAD = "";
        cboDMYAD = "";
        txtAccOpeningChrgAD = "";
        txtMisServiceChrgAD = "";
        chkStopPmtChrgAD = false;
        txtChequeBookChrgAD = "";
        chkChequeRetChrgAD = false;
        txtFolioChrgAD = "";
        chkInopChrgAD = false;
        txtAccCloseChrgAD = "";
        chkStmtChrgAD = false;
        chkPassBook = false;
        chkABBChrgAD = false;
        chkNPAChrgAD = false;
        cboStmtFreqAD = "";
        chkNonMainMinBalChrgAD = false;
        txtExcessWithChrgAD = "";
        txtMinActBalanceAD = "";
        txtABBChrgAD = "";
        dtdNPAChrgAD = "";
        dtdDebit = "";
        dtdCredit = "";
        ClosedDt = "";
        lblCustomerNameCrValue="";
        chkHideBalanceTrans = false;
        cboRoleHierarchy = "";
        
        // transfering branch information
        lblBranchNameValueAI = "";
        
        // account details
        lblActHeadValueAI = "";
        lblCustomerNameValueAI = "";
        
        // interest rates
        lblRateCodeValueIN = "";
        lblCrInterestRateValueIN = "";
        lblDrInterestRateValueIN = "";
        lblPenalInterestValueIN = "";
        lblAgClearingValueIN = "";
        
        // in case of self and existing customer
        lblCustomerIdValueITP1 = "";
        lblNameValueITP1 = "";
        lblActHeadValueITP1 = "";
        lblBranchCodeValueITP1 = "";
        lblBranchValueITP1 = "";
        
        // some addresses
        customerAddress = null;
        othersAddress = null;
        
        // reset Nominee and PoA fields
        //        resetNomineeOBFields();
        resetPoAOBFields();
        
        // nominee and PoA list
        nomineeTOList = null;
        
        //cbmPrevAcctNo="";
        // reset the account number
        accountNumber = "";
        // reset the Account No
        setAccountNo("");
        this.cbmPrevAcctNo=new ComboBoxModel(new ArrayList(),new ArrayList());
        getCbmConstitutionAI().setKeyForSelected("");
        /* its must to call setChanged(), before calling notifyObservers(),
         * because only then the update() method in the UI will be called
         */
        setChanged();
        setRdoFreeze(false);
        setRdoUnFreeze(false);
        setTxtFreezeRemarks("");
        setDtdFreezeDt("");
        setSlNo("");
        setServiceTax_Map(null);// Added by nithya on 23-05-2019 for KD 402 - 0019214: gst problem in locker opening
        notifyObservers();
    }
    
    
    public void resetLabels() {
        this.setLblRateCodeValueIN("");
        this.setLblCrInterestRateValueIN("");
        this.setLblDrInterestRateValueIN("");
        this.setLblPenalInterestValueIN("");
        this.setLblAgClearingValueIN("");
        this.setAccountNo("");
    }
    
    /* this method will be used to update the OB value and the corresponding
     * UI vlaues by calling notifyObservers()
     */
    private void populateOB(HashMap mapData, NomineeOB objNomineeOB, PowerOfAttorneyOB objPOAOB, AuthorizedSignatoryOB objAuthSign, AuthorizedSignatoryInstructionOB objAuthSignInst) throws Exception{
        LockerIssueTO account = null;
        //        AccountParamTO paramTO = null;
        ArrayList nomineeList = null;
        System.out.println("mapData : "+mapData);
        if(mapData.containsKey("AccountTO") && !mapData.get("AccountTO").equals("")){
            account = (LockerIssueTO) ((List) mapData.get("AccountTO")).get(0);
            setAccountTO(account);
        }
        //--- To Populate the Main Customer
        HashMap queryMap = new HashMap();
        queryMap.put("CUST_ID",getTxtCustomerIdAI());
        getCustAddrData(getTxtCustomerIdAI());
        
        populateScreen(queryMap, false, objPOAOB);
        //--- populates if the Constitution is Joint Account
        if(getCboConstitutionAI().equalsIgnoreCase("Joint")) {
            List jntAccntDetailsList =  (List) ((List) mapData.get("JointAcctDetails"));
            setJointAcctDetails(jntAccntDetailsList);
            jntAccntDetailsList = null;
        }
        populateTblPwdDetails(mapData);
        populateTblLockerChrgsDetails(mapData);
        
        //        if (((List) mapData.get("AccountParamTO")).size() > 0){
        //            paramTO = (AccountParamTO) ((List) mapData.get("AccountParamTO")).get(0);
        //            setParamTO(paramTO);
        //        }
        
        /* To set the ArrayList in NomineeOB so as to set the data in the Nominee-Table...*/
        nomineeList =  (ArrayList) mapData.get("AccountNomineeList");
        objNomineeOB.setNomimeeList(nomineeList);
        objNomineeOB.setNomineeTabData();
        objNomineeOB.ttNotifyObservers();
        
        objPOAOB.setTermLoanPowerAttorneyTO((ArrayList) (mapData.get("PowerAttorneyTO")), getAccountNo());
        objAuthSign.setAuthorizedSignatoryTO((ArrayList) (mapData.get("AuthorizedSignatoryTO")), getAccountNo());
        objAuthSignInst.setAuthorizedSignatoryInstructionTO((ArrayList) (mapData.get("AuthorizedSignatoryInstructionTO")), getAccountNo());
        
        //  if (getAuthorizeDt()==null) {
        List  list = (List) mapData.get("TransactionTO");
        if (list!= null && !list.isEmpty()) {
            transactionOB.setDetails(list);
            //            setTranCnt(list.size());
            //            if(transactionOB.getLoneActNum()!=null)
            //                loanActMap.put("LINK_BATCH_ID",transactionOB.getLoneActNum()) ;
        }
        // }
        /* its must to call setChanged(), before calling notifyObservers(),
         * because only then the update() method in the UI will be called
         */
        //        setChanged();
        //        notifyObservers();
    }
    
    private void setJointAcctDetails(List jntAccntDetailsList){
        //        tblJointAccnt.setDataArrayList(null, tblJointAccntColTitle);
        jntAcctAll = new LinkedHashMap();
        HashMap custMapData;
        HashMap calcJointAcct;
        List custListData;
        int jntAccntDetailsListSize = jntAccntDetailsList.size();
        for(int i=0; i<jntAccntDetailsListSize; i++){
            calcJointAcct =  (HashMap) jntAccntDetailsList.get(i);
            jntAcctSingleRec = new HashMap();
            jntAcctSingleRec.put("CUST_ID", CommonUtil.convertObjToStr(calcJointAcct.get("CUST_ID")));
            jntAcctSingleRec.put(FLD_FOR_DB_YES_NO, YES_FULL_STR);
            jntAcctSingleRec.put("STATUS", CommonUtil.convertObjToStr(calcJointAcct.get("STATUS")));
            jntAcctAll.put(CommonUtil.convertObjToStr(calcJointAcct.get("CUST_ID")), jntAcctSingleRec);
            if(!CommonUtil.convertObjToStr(calcJointAcct.get("STATUS")).equals("DELETED")){
                custListData = ClientUtil.executeQuery("getSelectAccInfoTblDisplay",jntAcctSingleRec);
                custMapData = (HashMap) custListData.get(0);
                objJointAcctHolderManipulation.setJntAcctTableData(custMapData,true,tblJointAccnt);
                setTblJointAccnt(objJointAcctHolderManipulation.getTblJointAccnt());
            }
            custMapData = null;
            jntAcctSingleRec = null;
            calcJointAcct = null;
            custListData = null;
        }
    }
    public ArrayList setLockerPwdTab() {
        System.out.println("allInstructionsList####"+allInstructionsList);
        ArrayList charges = new ArrayList();
        pwdTabRow = new ArrayList();
        //        final int dataSize = data.size();
        if (allInstructionsList != null && allInstructionsList.size() > 0) {// added by nithya on 03-11-2018 for 0018008: Locker Security deposit cannot change
            final int dataSize = allInstructionsList.size();
            for (int i = 0; i < dataSize; i++) {
                try {

                    objLockerPwdDetailsTO = new LockerPwdDetailsTO();

                    //                charges = (ArrayList)data.get(i);
                    charges = (ArrayList) allInstructionsList.get(i);
                    objLockerPwdDetailsTO.setProdID(CommonUtil.convertObjToStr(getCboProductIdAI()));
                    objLockerPwdDetailsTO.setCustID(CommonUtil.convertObjToStr(charges.get(0)));
                    objLockerPwdDetailsTO.setLocNum(CommonUtil.convertObjToStr(getTxtLockerNo()));
                    objLockerPwdDetailsTO.setPwd(CommonUtil.convertObjToStr(charges.get(1)));
                    String strEncrypt = CommonUtil.convertObjToStr(charges.get(1)).length() == 0 ? "" : encrypt.encrypt(CommonUtil.convertObjToStr(charges.get(1)));
                    objLockerPwdDetailsTO.setPwd(strEncrypt);
                    System.out.println("objLockerPwdDetailsTO#########" + objLockerPwdDetailsTO);
                    pwdTabRow.add(objLockerPwdDetailsTO);

                } catch (Exception e) {

                    parseException.logException(e, true);

                }
            }
        }
        return pwdTabRow;
    }
    
    public ArrayList setLockerChrgsTab() {
        // ArrayList data=new  ArrayList();
        //        ArrayList data=tbmLockCharges.getDataArrayList();
        //data=entireBillsDataRow;
        //        System.out.println("Data####"+data);
        //        ArrayList charges = new ArrayList();
        chargeTabRow = new ArrayList();
        //        final int dataSize = data.size();
        //        for (int i=0;i<dataSize;i++){
        try{
            
            objLockerIssueChargesTO = new LockerIssueChargesTO();
            
            //                charges = (ArrayList)data.get(i);
            objLockerIssueChargesTO.setChargeType("RENT_CHARGES");
            objLockerIssueChargesTO.setProdID(CommonUtil.convertObjToStr(getCboProductIdAI()));
            objLockerIssueChargesTO.setCustID(CommonUtil.convertObjToStr(getTxtCustomerIdAI()));
            objLockerIssueChargesTO.setLocNum(CommonUtil.convertObjToStr(getTxtLockerNo()));
            //                String dt=CommonUtil.convertObjToStr(charges.get(1));
            //                System.out.println("Date####"+dt);
            // objBillsChargesTabTO.setStartDate();
            objLockerIssueChargesTO.setFromDate(curDate);
            //                dt = CommonUtil.convertObjToStr(curDate);
            objLockerIssueChargesTO.setToDate(curDate);
            objLockerIssueChargesTO.setCommision(CommonUtil.convertObjToDouble(getTxtCharges()));
            objLockerIssueChargesTO.setServiceTax(CommonUtil.convertObjToDouble(getTxtServiceTax()));
            
            System.out.println("objLockerIssueChargesTO#########"+objLockerIssueChargesTO);
            chargeTabRow.add(objLockerIssueChargesTO);
            
        }catch(Exception e){
            
            parseException.logException(e,true);
            
        }
        //        }
        
        return chargeTabRow;
    }
    
    public double getTotCharges(){
        ArrayList data=tbmLockCharges.getDataArrayList();
        //data=entireBillsDataRow;
        System.out.println("Data####"+data);
        ArrayList charges = new ArrayList();
        final int dataSize = data.size();
        double rentAmt = 0.0;
        for (int i=0;i<dataSize;i++){
            try{
                objLockerIssueChargesTO = new LockerIssueChargesTO();
                charges = (ArrayList)data.get(i);
                if(CommonUtil.convertObjToStr(charges.get(0)).equals("RENT_CHARGES")){
                    rentAmt = CommonUtil.convertObjToDouble(charges.get(3)).doubleValue();
                    
                }
            }catch(Exception e){
                
                parseException.logException(e,true);
                
            }
            
        }
        return rentAmt;
    }
    public double getServiceTax(){
        ArrayList data=tbmLockCharges.getDataArrayList();
        //data=entireBillsDataRow;
        System.out.println("Data####"+data);
        ArrayList charges = new ArrayList();
        final int dataSize = data.size();
        double servTax = 0.0;
        for (int i=0;i<dataSize;i++){
            try{
                objLockerIssueChargesTO = new LockerIssueChargesTO();
                charges = (ArrayList)data.get(i);
                if(CommonUtil.convertObjToStr(charges.get(0)).equals("RENT_CHARGES")){
                    servTax = CommonUtil.convertObjToDouble(charges.get(4)).doubleValue();
                }
            }catch(Exception e){
                
                parseException.logException(e,true);
                
            }
            
        }
        return servTax;
    }
    private void setAccountTO(LockerIssueTO account) {
        
        setCboProductIdAI(CommonUtil.convertObjToStr(account.getProdId()));
        setTxtCustomerIdAI(CommonUtil.convertObjToStr(account.getCustId()));
        //setTxtPrevActNumAI(account.getPrevActNum());
        //        if ( account.getPrevActNum()!= null && account.getPrevActNum().length()>0 &&
        //        (this.getOperation()==ClientConstants.ACTIONTYPE_NEWTI ||
        //        this.getOperation()==ClientConstants.ACTIONTYPE_EDITTI )) {
        //            getPreviousActDetails(account.getPrevActNum());
        //        }
        setTxtLockerNo(CommonUtil.convertObjToStr(account.getLocNum()));
        setTxtPrevActNumAI(CommonUtil.convertObjToStr(account.getDepositNo()));
        //        setLblLockerKeyNoVal(CommonUtil.convertObjToStr(account.getLoc
        setDtdOpeningDateAI(DateUtil.getStringDate(account.getCreateDt()));
        setCollectRentMM(CommonUtil.convertObjToStr(account.getCollectRentMM()));
        setCollectRentYYYY(CommonUtil.convertObjToStr(account.getcollectRentYYYY()));
        if (CommonUtil.convertObjToStr(account.getSi()).equals(YES)) {
            setSiYes(true);
            setCboProdType(CommonUtil.convertObjToStr(account.getProdType()));
            setCboProductId(CommonUtil.convertObjToStr(account.getTxtProdId()));
            setTxtCustomerIdCr(CommonUtil.convertObjToStr(account.getCustomerIdCr()));
            setLblCustomerNameCrValue(CommonUtil.convertObjToStr(account.getCustomerNameCrValue()));
            setSiNo(false);
            
        } else if (CommonUtil.convertObjToStr(account.getSi()).equals(NO)) {
            
            setSiYes(false);
            
            setSiNo(true);
        }
        if(CommonUtil.convertObjToStr(account.getPwd()).equals(YES)){
            setPwdYes(true);
            setPwdNo(false);
            
        }else //if(account.getPwd().equals(NO))
        {
            setPwdYes(false);
            setPwdNo(true);
        }
         if(CommonUtil.convertObjToStr(account.getFreezeStatus()).equals(YES) && CommonUtil.convertObjToStr(account.getUnFreezeStatus()).equals(YES)){
             setRdoFreeze(false);
             setRdoUnFreeze(true);
             setTxtFreezeRemarks(account.getUnFreezeRemarks());
             setDtdFreezeDt(CommonUtil.convertObjToStr(account.getUnFreezeDt()));
             setSlNo(CommonUtil.convertObjToStr(account.getSlNo()));

         } else if (CommonUtil.convertObjToStr(account.getFreezeStatus()).equals(YES)){
            setRdoFreeze(true);
            setRdoUnFreeze(false);
            setTxtFreezeRemarks(account.getFreezeRemarks());
            setDtdFreezeDt(CommonUtil.convertObjToStr(account.getFreezeDt()));
            setSlNo(CommonUtil.convertObjToStr(account.getSlNo()));
        }
       
        
        // setPwdNo(CommonUtil.convertObjToStr(account.getPwdNo()));
        
        
        
        
        
        
        
        // if (account.getExpDt()!=null) {
        // setDtdExpiryDate(DateUtil.getStringDate(account.getExpDt()));
        //} else {
        //setDtdExpiryDate(DateUtil.getStringDate(DateUtil.addDays(account.getCreateDt(), 365)));
        // }
        setCboActStatusAI(CommonUtil.convertObjToStr(account.getLocStatusId()));
        setCboConstitutionAI(CommonUtil.convertObjToStr(account.getLocCatId()));
        
        if (getCbmConstitutionAI().containsElement("Individual")) {
            getCbmConstitutionAI().removeKeyAndElement("INDIVIDUAL");
            getCbmConstitutionAI().removeKeyAndElement("JOINT");
        }
        if (getCbmConstitutionAI().containsElement("Corporate")) {
            getCbmConstitutionAI().removeKeyAndElement("CORPORATE");
        }
        if (account.getLocCatId().equals("INDIVIDUAL") || account.getLocCatId().equals("JOINT")) {
            getCbmConstitutionAI().addKeyAndElement("INDIVIDUAL", "Individual");
            getCbmConstitutionAI().addKeyAndElement("JOINT", "Joint");
        } else {
            getCbmConstitutionAI().addKeyAndElement("CORPORATE", "Corporate");
        }
        getCbmConstitutionAI().setKeyForSelected(account.getLocCatId());
        
        //        setCboConstitutionAI(CommonUtil.convertObjToStr(getCbmConstitutionAI().getDataForKey(CommonUtil.convertObjToStr(account.getActCatId()))));
        setStatusBy(account.getStatusBy());
        setAuthorizeStatus(account.getAuthorizationStatus());
        setCboOpModeAI(CommonUtil.convertObjToStr(account.getOptModeId()));
        setCboCommAddr(CommonUtil.convertObjToStr(account.getCommAddrType()));
        //        setTxtODLimitAI(CommonUtil.convertObjToStr(account.getTodLimit()));
        //        setCboGroupCodeAI(CommonUtil.convertObjToStr(account.getGroupCodeId()));
        //  setCboSettlementModeAI(CommonUtil.convertObjToStr(account.getSettmtModeId()));
        setCboCategory(CommonUtil.convertObjToStr(account.getCategoryId()));
        //        setCboBaseCurrAI(CommonUtil.convertObjToStr(account.getBaseCurr()));
        
        setTxtActName(CommonUtil.convertObjToStr(account.getAcctName()));
        setTxtRemarks(CommonUtil.convertObjToStr(account.getRemarks()));
        setClosedDt(CommonUtil.convertObjToStr(account.getClosedDt()));
        setAuthorizeDt(account.getAuthorizedDt());
        setDtdExpiryDate(CommonUtil.convertObjToStr(account.getExpDt()));
    }
    
    private void populateTblPwdDetails(HashMap mapData){
        if(mapData.containsKey("LockerPwdDetailsTO")){
            List instList = (List) mapData.get("LockerPwdDetailsTO");
            //            double totalAmount = 0.0;
            //            double totServTax = 0.0;
            //            String returnTotalAmt = "";
            //            String totalSerAmt = "";
            try {
                allInstructionsList = new ArrayList();
                if(instList != null && instList.size() !=0){
                    for(int i=0; i<instList.size();i++){
                        HashMap dataMap = (HashMap) instList.get(i);
                        existData = new ArrayList();
                        //                    existData.add(new Integer(i+1));
                        existData.add(dataMap.get("CUST_ID"));
                        existData.add("");
                        existData.add("");
                        // existData.add(dataMap.get("ACCT_NAME"));
                        //                    existData.add(dataMap.get("CATEGORY_ID"));
                        //                    existData.add(dataMap.get("PWD"));
                        //                    existData.add(dataMap.get("PWD"));
                        //                    existData.add(dataMap.get("CATEGORY_ID"));
                        //                    totalAmount += Double.parseDouble(CommonUtil.convertObjToStr(dataMap.get("AMOUNT")));
                        //                    totServTax += Double.parseDouble(CommonUtil.convertObjToStr(dataMap.get("SERVICE_TX")));
                        tbmInstructions2.insertRow(tbmInstructions2.getRowCount(), existData);
                        ArrayList tempList = new ArrayList();
                        tempList.addAll(existData);
                        String pwd = CommonUtil.convertObjToStr(dataMap.get("PWD"));
                        tempList.add(pwd.length() == 0 ? "" : encrypt.decrypt(pwd));
                        tempList.add(pwd.length() == 0 ? "" : encrypt.decrypt(pwd));
                        allInstructionsList.add(allInstructionsList.size(), tempList);
                        tempList = null;
                        //                    existData = null;
                    }
                    //                returnTotalAmt =  CommonUtil.convertObjToStr(new Double(String.valueOf(totalAmount)));
                    //                totalSerAmt = CommonUtil.convertObjToStr(new Double(String.valueOf(totServTax)));
                    //                setTxtTotalAmt(returnTotalAmt);
                    //                setTxttotalServTax(totalSerAmt);
                    //                notifyObservers();
                }
                System.out.println("#$#$@#!@ allInstructionsList in populateTblPwdDetails : "+allInstructionsList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }
    
    private void populateTblLockerChrgsDetails(HashMap mapData){
        if(mapData.containsKey("LockerIssueChrgsTO")){
            List instList = (List) mapData.get("LockerIssueChrgsTO");
            if(instList != null && instList.size() !=0){
                HashMap dataMap = (HashMap) instList.get(0);
                setTxtCharges(CommonUtil.convertObjToStr(dataMap.get("COMMISION")));
                setTxtServiceTax(CommonUtil.convertObjToStr(dataMap.get("SERVICE_TAX")));
            }
        }
        
    }
    
    public ComboBoxModel getCbmProductIdAI(){
        return this.cbmProductIdAI;
    }
    
    public ComboBoxModel getCbmActStatusAI(){
        return this.cbmActStatusAI;
    }
    
    public ComboBoxModel getCbmConstitutionAI(){
        return this.cbmConstitutionAI;
    }
    
    public ComboBoxModel getCbmOpModeAI(){
        return this.cbmOpModeAI;
    }
    public void setCbmProductId(ComboBoxModel cbmProductId){
        this.cbmProductId = cbmProductId;
        setChanged();
    }
    ComboBoxModel getCbmProductId(){
        return cbmProductId;
    }
    public ComboBoxModel getCbmCommAddr(){
        return this.cbmCommAddr;
    }
    
    public ComboBoxModel getCbmGroupCodeAI(){
        return this.cbmGroupCodeAI;
    }
    
    public ComboBoxModel getCbmSettlementModeAI(){
        return this.cbmSettlementModeAI;
    }
    
    public ComboBoxModel getCbmCategory(){
        return this.cbmCategory;
    }
    
    public ComboBoxModel getCbmBaseCurrAI(){
        return this.cbmBaseCurrAI;
    }
    
    public ComboBoxModel getCbmDocTypeITP3(){
        return this.cbmDocTypeITP3;
    }
    
    public ComboBoxModel getCbmIdentityTypeITP4(){
        return this.cbmIdentityTypeITP4;
    }
    
    public ComboBoxModel getCbmDMYAD(){
        return this.cbmDMYAD;
    }
    
    public ComboBoxModel getCbmStmtFreqAD(){
        return this.cbmStmtFreqAD;
    }
    
    public ComboBoxModel getCbmNomineeRelationNO(){
        return this.cbmNomineeRelationNO;
    }
    
    public ComboBoxModel getCbmRelationNO(){
        return this.cbmRelationNO;
    }
    
    public int getActionType(){
        return this.actionType;
    }
    
    /* To set the Action Type */
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }
    
    
    
    
    /* use this method to load the data corresponding to the selected account
     * this method will call populateOB() to load the data in actual OB and
     * will call update() to update the values in the Ui screen
     */
    public void populateData(HashMap whereMap, NomineeOB objNomineeOB, PowerOfAttorneyOB poaOB, AuthorizedSignatoryOB objAuthSign, AuthorizedSignatoryInstructionOB objAuthSignInst) {
        HashMap mapData = null;
        try {
            mapData = (HashMap) proxy.executeQuery(whereMap, map);
            populateOB(mapData, objNomineeOB, poaOB, objAuthSign, objAuthSignInst);
        } catch( Exception e ) {
            e.printStackTrace();
        }
    }
    
    public void setTransactionOB(TransactionOB transactionOB) {
        this.transactionOB = transactionOB;
    }
    
    public void setAllowedTransactionDetailsTO(java.util.LinkedHashMap allowedTransactionDetailsTO) {
        this.allowedTransactionDetailsTO = allowedTransactionDetailsTO;
    }
    /** To perform necessary action */
    public void doAction(NomineeOB objNomineeOB, PowerOfAttorneyOB objPOAOB, AuthorizedSignatoryOB objAuthSign, AuthorizedSignatoryInstructionOB objAuthSignInst) {
        TTException exception = null;
        try {
            map.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
            System.out.println("the map is"+map);
            //            populateBean(objNomineeOB, objPOAOB, objAuthSign, objAuthSignInst);
            //            HashMap proxyResultMap = new HashMap();
            System.out.println("#$%$%#$%$%map:"+map);
           
           
                HashMap proxyResultMap = proxy.execute(populateBean(objNomineeOB, objPOAOB, objAuthSign, objAuthSignInst), map);
                System.out.println("proxyResultMap : " + proxyResultMap);
                setProxyReturnMap(proxyResultMap);
                if (proxyResultMap != null && proxyResultMap.containsKey(CommonConstants.TRANS_ID)) {
                    ClientUtil.showMessageWindow(resourceBundle.getString("lblAccountNumber") + ": " + CommonUtil.convertObjToStr(proxyResultMap.get(CommonConstants.TRANS_ID)));
                }
                
                setResult(getOperation());
                //operation = ClientConstants.ACTIONTYPE_CANCEL;
            
        } catch (Exception e) {
            //            ClientUtil.showMessageWindow(e.getMessage());
            System.out.println("#$#$ from LockerIssueOB doAction : "+e);
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            e.printStackTrace();
            if(e instanceof TTException) {
                exception = (TTException) e;
            } else {
                ClientUtil.displayAlert(e.toString());
            }
        }
        
        // If TT Exception
        if (exception != null) {
            parseException.logException(exception, true);
            //            setResult(getOperation());
        }
        //            HashMap exceptionHashMap = exception.getExceptionHashMap();
        //            System.out.println("exception###"+exception);
        //            System.out.println("exception.getExceptionHashMap();"+exception.getExceptionHashMap());
        //            if (exceptionHashMap != null) {
        //                ArrayList list =(ArrayList)exceptionHashMap.get(CommonConstants.EXCEPTION_LIST);
        //                if(list !=null && list.size()>0 && list.get(0) instanceof String && ((String)list.get(0)).startsWith("SUSPECIOUS") ||
        //                CommonUtil.convertObjToStr(list.get(0)).equals("AED") || CommonUtil.convertObjToStr(list.get(0)).equals("AEL")
        //                 || CommonUtil.convertObjToStr(list.get(0)).equals("ESL")){
        //                    parseException.logException(exception, true);
        //                    Object []dialogOption={"EXCEPTION","CANCEL"};
        //                    parseException.setDialogOptions(dialogOption);
        //                    if(parseException.logException(exception,true)==0){
        //                        try{
        //                            //                        setResult(ac);
        //                            doAction("EXCEPTION");
        //
        //                        }catch(Exception e){
        //                            if(e instanceof TTException){
        //                                Object []dialog={"OK"};
        //                                parseException.setDialogOptions(dialog);
        //                                exception = (TTException) e;
        //                                parseException.logException(exception, true);
        //                            }
        //                        }
        //                    }
        //                    Object[] dialogOption1 = {"OK"};
        //                    parseException.setDialogOptions(dialogOption1);
        //                }
        //                else if(CommonUtil.convertObjToStr(list.get(0)).equals("MIN")){
        //                    minBalException=true;
        //                    Object []dialogOption={"CONTINUE","CANCEL"};
        //                    parseException.setDialogOptions(dialogOption);
        //                    if(parseException.logException(exception,true)==0){
        //                        try{
        //                            //                        setResult(ac);
        //                            doAction("EXCEPTION");
        //
        //                        }catch(Exception e){
        //                            if(e instanceof TTException){
        //                                Object []dialog={"OK"};
        //                                parseException.setDialogOptions(dialog);
        //                                exception = (TTException) e;
        //                                parseException.logException(exception, true);
        //                            }
        //                        }
        //                    }
        //                    Object[] dialogOption1 = {"OK"};
        //                    parseException.setDialogOptions(dialogOption1);
        //                }
        //                else {
        //                    parseException.logException(exception, true);
        //                }
        //            } else { // To Display Transaction No showing String message
        //                parseException.logException(exception, true);
        //                setResult(getOperation());
        //            }
        //        }
    }
    
    public ServiceTaxDetailsTO setServiceTaxDetails() {// Added by nithya on 23-05-2019 for KD 402 - 0019214: gst problem in locker opening
        final ServiceTaxDetailsTO objservicetaxDetTo = new ServiceTaxDetailsTO();
        try {

            objservicetaxDetTo.setBranchID(ProxyParameters.BRANCH_ID);
            objservicetaxDetTo.setCommand("INSERT");
            objservicetaxDetTo.setStatus("CREATED");
            objservicetaxDetTo.setStatusBy(TrueTransactMain.USER_ID);
            objservicetaxDetTo.setAcct_Num(getTxtLockerNo());
            objservicetaxDetTo.setParticulars("Loan Closing");

            if (serviceTax_Map != null && serviceTax_Map.containsKey("SERVICE_TAX")) {
                objservicetaxDetTo.setServiceTaxAmt(CommonUtil.convertObjToDouble(serviceTax_Map.get("SERVICE_TAX")));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey("EDUCATION_CESS")) {
                objservicetaxDetTo.setEducationCess(CommonUtil.convertObjToDouble(serviceTax_Map.get("EDUCATION_CESS")));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey("HIGHER_EDU_CESS")) {
                objservicetaxDetTo.setHigherCess(CommonUtil.convertObjToDouble(serviceTax_Map.get("HIGHER_EDU_CESS")));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.SWACHH_CESS)) {
                objservicetaxDetTo.setSwachhCess(CommonUtil.convertObjToDouble(serviceTax_Map.get(ServiceTaxCalculation.SWACHH_CESS)));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS)) {
                objservicetaxDetTo.setKrishiKalyan(CommonUtil.convertObjToDouble(serviceTax_Map.get(ServiceTaxCalculation.KRISHIKALYAN_CESS)));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey("TOT_TAX_AMT")) {
                objservicetaxDetTo.setTotalTaxAmt(CommonUtil.convertObjToDouble(serviceTax_Map.get("TOT_TAX_AMT")));
            }
            double roudVal = objservicetaxDetTo.getTotalTaxAmt() - (objservicetaxDetTo.getServiceTaxAmt() + objservicetaxDetTo.getEducationCess() + objservicetaxDetTo.getHigherCess() + objservicetaxDetTo.getSwachhCess() + objservicetaxDetTo.getKrishiKalyan());
            ServiceTaxCalculation serviceTaxObj = new ServiceTaxCalculation();
            objservicetaxDetTo.setRoundVal(CommonUtil.convertObjToStr(serviceTaxObj.roundOffAmtForRoundVal(roudVal)));
            objservicetaxDetTo.setStatusDt(curDate);
            objservicetaxDetTo.setTrans_type("C");
           
                objservicetaxDetTo.setCreatedBy(TrueTransactMain.USER_ID);
                objservicetaxDetTo.setCreatedDt(curDate);
            
        } catch (Exception e) {            
            e.printStackTrace();
        }
        return objservicetaxDetTo;

    }
    
    private HashMap populateBean(NomineeOB objNomineeOB, PowerOfAttorneyOB poaOB, AuthorizedSignatoryOB objAuthSign, AuthorizedSignatoryInstructionOB objAuthSignInst) {
        HashMap account = new HashMap();
        LockerIssueTO acctTO = getLockerTO();
        account.put("LockerTO", acctTO);
        final ArrayList lockerPwdDetails = setLockerPwdTab();
        final ArrayList lockerIssueCharges = setLockerChrgsTab();
        account.put("LockerPwdDetailsTO", lockerPwdDetails);
        account.put("LockerIssueChargesTO", lockerIssueCharges);
        //--- Puts the data if the Constitution is JointAccount
        if(acctTO.getLocCatId().equals("JOINT")){
            account.put("JointLockerTO",setJointAccntData());
            jntAcctTOMap = null;
        }
        
        //        account.put("AccountParamTO", getAccountParamTO());
        
        /**
         * To Send the Nominee Related Data to the Account-DAO...
         */
        account.put("AccountNomineeTO", objNomineeOB.getNomimeeList());
        account.put("AccountNomineeDeleteTO", objNomineeOB.getDeleteNomimeeList());
        poaOB.setBorrowerNo(getTxtLockerNo());
        
        account.put("PowerAttorneyTO",poaOB.setTermLoanPowerAttorney());
        objAuthSign.setBorrowerNo(getTxtLockerNo());
        account.put("AuthorizedSignatoryTO",objAuthSign.setAuthorizedSignatory());
        account.put("AuthorizedSignatoryInstructionTO",objAuthSignInst.setAuthorizedSignatoryInstruction());
        if (transactionDetailsTO == null) {
            transactionDetailsTO = new LinkedHashMap();
        }
        
        if (deletedTransactionDetailsTO != null) {
            transactionDetailsTO.put(DELETED_TRANS_TOs,deletedTransactionDetailsTO);
            deletedTransactionDetailsTO = null;
        }
        
        transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,allowedTransactionDetailsTO);
        allowedTransactionDetailsTO = null;
        
        account.put("TransactionTO",transactionDetailsTO);
        account.put("MODE", acctTO.getCommand());
        account.put("SCREEN", "Locker Issue");
        if(getChkNoTransaction().equalsIgnoreCase("Y")){
           account.put("LOCKER_ISSUE_WITH_NO_TRANSACTION", "LOCKER_ISSUE_WITH_NO_TRANSACTION"); 
        }
        if (getOperation()==ClientConstants.ACTIONTYPE_NEW) {// Added by nithya on 23-05-2019 for KD 402 - 0019214: gst problem in locker opening
            if (getServiceTax_Map() != null && getServiceTax_Map().size() > 0) {
                account.put("serviceTaxDetails", getServiceTax_Map());
                account.put("serviceTaxDetailsTO", setServiceTaxDetails());
            }
        }
        System.out.println("######account"+account);
        return account;
    }
    
    /* To set Joint Account data in the Transfer Object*/
    public HashMap setJointAccntData(){
        HashMap singleRecordJntAcct;
        jntAcctTOMap = new LinkedHashMap();
        try{
            LockerIssueJointTO objAccountJointTO;
            int jntAcctSize = jntAcctAll.size();
            for(int i = 0;i<jntAcctSize;i++){
                singleRecordJntAcct = (HashMap)jntAcctAll.get(CommonUtil.convertObjToStr(jntAcctAll.keySet().toArray()[i]));
                objAccountJointTO = new LockerIssueJointTO();
                objAccountJointTO.setCustId(CommonUtil.convertObjToStr(singleRecordJntAcct.get("CUST_ID")));
                //                objAccountJointTO.setActNum(getAccountNo());
                objAccountJointTO.setStatus(CommonUtil.convertObjToStr(singleRecordJntAcct.get("STATUS")));
                //                objAccountJointTO.setCommand(getCommand());
                
                //--- To set the Command
                if ( getOperation() == ClientConstants.ACTIONTYPE_EDIT || getOperation() == ClientConstants.ACTIONTYPE_EDITTI) {
                    objAccountJointTO.setCommand(CommonConstants.TOSTATUS_UPDATE);
                }
                else if(getOperation() == ClientConstants.ACTIONTYPE_DELETE ) {
                    objAccountJointTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                }
                else {
                    objAccountJointTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                }
                //-- To set the Account No
                //                if ( getOperation() == ClientConstants.ACTIONTYPE_EDIT || getOperation() == ClientConstants.ACTIONTYPE_EDITTI) {
                objAccountJointTO.setLocNum(getTxtLockerNo());
                //                }
                jntAcctTOMap.put(String.valueOf(i),objAccountJointTO);
                objAccountJointTO = null;
                singleRecordJntAcct = null;
            }
            jntAcctAll = null;// Added by nithya on 29-11-2017 for 8293
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return jntAcctTOMap;
    }
    
    private LockerIssueTO getLockerTO() {
        LockerIssueTO lockerTo = new LockerIssueTO();
        
        lockerTo.setLocNum(getTxtLockerNo());
        lockerTo.setProdId(getCboProductIdAI());
        lockerTo.setCustId(getTxtCustomerIdAI());
        //        lockerTo.setBranchCode()
        //        lockerTo.setCreateDt
        lockerTo.setLocStatusId("NEW");
        lockerTo.setLocCatId(getCboConstitutionAI());
        lockerTo.setOptModeId(getCboOpModeAI());
        // lockerTo.setSettmtModeId(getCboSettlementModeAI());
        lockerTo.setCreatedBy(TrueTransactMain.USER_ID);
        //        lockerTo.setAuthorizedBy
        //        lockerTo.setAuthorizationStatus
        //        lockerTo.setClosedBy
        //        lockerTo.setClosedDt
        //        lockerTo.setAuthorizedDt
        //        lockerTo.setStatus
        //        lockerTo.setStatusBy
        //        lockerTo.setStatusDt
        lockerTo.setCommAddrType(getCboCommAddr());
        lockerTo.setCategoryId(getCboCategory());
        lockerTo.setAcctName(getTxtActName());
        lockerTo.setRemarks(getTxtRemarks());
        lockerTo.setDepositNo(getTxtPrevActNumAI());
        lockerTo.setCollectRentMM(getCollectRentMM());
        lockerTo.setCollectRentYYYY(getCollectRentYYYY());
        //lockerTo.setSiYes(getSiYes());
        // lockerTo.SetSiNo(getSiNo());
        if (getSiYes()==true) {
            lockerTo.setSi(CommonUtil.convertObjToStr(YES));
        } else  if (getSiNo()==true) {
            lockerTo. setSi(CommonUtil.convertObjToStr(NO));
        }
        
        if (getPwdYes()==true) {
            lockerTo.setPwd(CommonUtil.convertObjToStr(YES));
        } else if (getPwdNo()==true) {
            lockerTo. setPwd(CommonUtil.convertObjToStr(NO));
        }
        if (isRdoFreeze() == true) {
            lockerTo.setFreezeStatus(CommonUtil.convertObjToStr(YES));
            lockerTo.setFreezeRemarks(getTxtFreezeRemarks());
            Date FreezeDate = DateUtil.getDateMMDDYYYY(getDtdFreezeDt());
            if (FreezeDate != null) {
                Date FreezeDt = (Date) curDate.clone();
                FreezeDt.setDate(FreezeDate.getDate());
                FreezeDt.setMonth(FreezeDate.getMonth());
                FreezeDt.setYear(FreezeDate.getYear());
                lockerTo.setFreezeDt(FreezeDt);
            }
            lockerTo.setSlNo(getSlNo());
        } else if (isRdoUnFreeze() == true) {
            lockerTo.setUnFreezeStatus(CommonUtil.convertObjToStr(YES));
            lockerTo.setUnFreezeRemarks(getTxtFreezeRemarks());
            Date unFreezeDate = DateUtil.getDateMMDDYYYY(getDtdFreezeDt());
            if (unFreezeDate != null) {
                Date unFreezeDt = (Date) curDate.clone();
                unFreezeDt.setDate(unFreezeDate.getDate());
                unFreezeDt.setMonth(unFreezeDate.getMonth());
                unFreezeDt.setYear(unFreezeDate.getYear());
                lockerTo.setUnFreezeDt(unFreezeDt);
            }
            lockerTo.setSlNo(getSlNo());
        }

        //  lockerTo.setPwdNo(getPwdNo());
        // lockerTo.setPwdYes(getPwdYes());
        if(getSiYes()==true){
            lockerTo.setProdType(getCboProdType());
            lockerTo.setTxtProdId(getCboProductId());
            lockerTo. setCustomerIdCr(getTxtCustomerIdCr());
            lockerTo.setCustomerNameCrValue(getLblCustomerNameCrValue());
        }
        else if(getSiNo()==true) {
            lockerTo.setProdType("");
            lockerTo.setTxtProdId("");
            lockerTo. setCustomerIdCr("");
            lockerTo.setCustomerNameCrValue("");
        }
        if (null != getDtdExpiryDate() && !getDtdExpiryDate().equals("")) {
        Date tdtExpDt = DateUtil.getDateMMDDYYYY(getDtdExpiryDate());
        Date expDt = (Date)curDate.clone();
        expDt.setDate(tdtExpDt.getDate());
        expDt.setMonth(tdtExpDt.getMonth());
        expDt.setYear(tdtExpDt.getYear());
        lockerTo.setExpDt(expDt);
        }
        
        if ( getOperation() == ClientConstants.ACTIONTYPE_EDIT || getOperation() == ClientConstants.ACTIONTYPE_EDITTI) {
            //            account.setActNum(getAccountNumber());
            lockerTo.setCommand(CommonConstants.TOSTATUS_UPDATE);
            //            account.setStatus(CommonConstants.STATUS_MODIFIED);
        }
        else if(getOperation() == ClientConstants.ACTIONTYPE_DELETE ) {
            //            account.setActNum(getAccountNumber());
            lockerTo.setCommand(CommonConstants.TOSTATUS_DELETE);
            //            account.setStatus(CommonConstants.STATUS_DELETED);
        }
        else {
            lockerTo.setCommand(CommonConstants.TOSTATUS_INSERT);
            //            account.setStatus(CommonConstants.STATUS_CREATED);
        }
        //        account.setInitiatedBranch(ProxyParameters.BRANCH_ID);
        //        account.setProdId(getCboProductIdAI());
        //        account.setCustId(getTxtCustomerIdAI());
        //        account.setBranchCode(getSelectedBranchID());
        //
        //        Date OpDt = DateUtil.getDateMMDDYYYY(getDtdOpeningDateAI());
        //        if(OpDt != null){
        //        Date opDate = (Date) curDate.clone();
        //        opDate.setDate(OpDt.getDate());
        //        opDate.setMonth(OpDt.getMonth());
        //        opDate.setYear(OpDt.getYear());
        ////        account.setCreateDt(DateUtil.getDateMMDDYYYY(getDtdOpeningDateAI()));
        //        account.setCreateDt(opDate);
        //        }else{
        //            account.setCreateDt(DateUtil.getDateMMDDYYYY(getDtdOpeningDateAI()));
        //        }
        //
        //        account.setActStatusId(getCboActStatusAI());
        //        account.setActCatId(getCboConstitutionAI());
        //        account.setOptModeId(getCboOpModeAI());
        //        account.setCommAddrType(getCboCommAddr());
        //        account.setTodLimit(CommonUtil.convertObjToDouble(getTxtODLimitAI()));
        //        account.setGroupCodeId(getCboGroupCodeAI());
        //        account.setSettmtModeId(getCboSettlementModeAI());
        //        account.setCategoryId(getCboCategory());
        //        account.setPrevActNum((String)this.cbmPrevAcctNo.getKeyForSelected());
        //        account.setClearBalance(CommonUtil.convertObjToDouble("0.0"));
        //        account.setUnclearBalance(CommonUtil.convertObjToDouble("0.0"));
        //        account.setFloatBalance(CommonUtil.convertObjToDouble("0.0"));
        //        account.setEffectiveBalance(CommonUtil.convertObjToDouble("0.0"));
        //        if(this.getTxtAmoutTransAI()!=null && this.getTxtAmoutTransAI().length()>0)
        //            account.setAvailableBalance(CommonUtil.convertObjToDouble(this.getTxtAmoutTransAI()));
        //        else
        //            account.setAvailableBalance(CommonUtil.convertObjToDouble("0.0"));
        //        //        account.setCreatedBy(TrueTransactMain.USER_ID);
        //        account.setAuthorizationStatus("");
        //        //        account.setClosedBy("");
        //        //        account.setClosedDt(new Date());
        //        account.setBaseCurr(LocaleConstants.DEFAULT_CURRENCY);
        //
        //        //        account.setStatusBy(TrueTransactMain.USER_ID);
        //        //        account.setStatusDt(currDt);
        //        account.setAcctName(getTxtActName());
        //        account.setRemarks(getTxtRemarks());
        
        return lockerTo;
    }
    
    //    private AccountSelfIntroTO getAccountSelfIntroTO() {
    //        AccountSelfIntroTO intro = new AccountSelfIntroTO();
    //        if ( getOperation() == ClientConstants.ACTIONTYPE_EDIT || getOperation() == ClientConstants.ACTIONTYPE_EDITTI) {
    //            intro.setActNum(getAccountNumber());
    //        }
    //
    //        intro.setActNumIntro(getTxtAccountNoITP1());
    //        intro.setStatus(this.getTOStatus());
    //        intro.setStatusBy(TrueTransactMain.USER_ID);
    //        intro.setStatusDt(currDt);
    //
    //        return intro;
    //    }
    
    public boolean checkAcNoWithoutProdType(String actNum) {
        HashMap mapData=new HashMap();
        boolean isExists = false;
        try{//dont delete chck selectalldao
            mapData.put("ACT_NUM", actNum);
            List mapDataList = ClientUtil.executeQuery("getActNumFromAllProducts", mapData);
            System.out.println("#### mapDataList :"+mapDataList);
            if (mapDataList!=null && mapDataList.size()>0) {
                mapData=(HashMap)mapDataList.get(0);
                setTxtCustomerIdCr(CommonUtil.convertObjToStr(mapData.get("ACT_NUM")));
                cbmProdType.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                setProdType(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                setCbmProdId(getProdType());
                cbmProdId.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_ID")));
                isExists = true;
            } else {
                isExists = false;
            }
            mapDataList = null;
        }catch(Exception e){
            e.printStackTrace();
        }
        mapData = null;
        return isExists;
    }
    
    
    void setTabLength(double tabLength){
        this.tabLenght = tabLenght;
    }
    
    double getTabLength(){
        return this.tabLenght;
    }
    
    
    
    public void setAccountName(String AccountNo,String prodType){
        HashMap resultMap = new HashMap();
        final HashMap accountNameMap = new HashMap();
        List resultList = new ArrayList();
        try {
            if (!prodType.equals("")) {
                accountNameMap.put("ACC_NUM",AccountNo);
                String pID = !prodType.equals("GL") ? getCbmProdId().getKeyForSelected().toString() : "";
                if(prodType.equals("GL") && getTxtAccNo().length()>0){
                    resultList = ClientUtil.executeQuery("getAccountNumberNameTL",accountNameMap);
                } else {
                    resultList = ClientUtil.executeQuery("getAccountNumberName"+this.getProdType(),accountNameMap);
                }
                if(resultList != null && resultList.size() > 0){
                    if(!prodType.equals("GL")) {
                        HashMap dataMap = new HashMap();
                        accountNameMap.put("BRANCH_ID", getSelectedBranchID());
                        List lst = (List) ClientUtil.executeQuery("getProdIdForActNo"+prodType,accountNameMap);
                        if(lst != null && lst.size() > 0)
                            dataMap = (HashMap) lst.get(0);
                        if(dataMap.get("PROD_ID").equals(pID)){
                            resultMap = (HashMap)resultList.get(0);
                        }
                    } else {
                        resultMap = (HashMap)resultList.get(0);
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        if(resultMap.containsKey("CUSTOMER_NAME")){
            System.out.println("%^$%^$%^$%^inside here"+resultMap);
            setLblCustomerNameCrValue(resultMap.get("CUSTOMER_NAME").toString());
        }else
            setLblCustomerNameCrValue("");
        //            if(resultList != null){
        //                final HashMap resultMap = (HashMap)resultList.get(0);
        //                setLblAccName(resultMap.get("CUSTOMER_NAME").toString());
        //            } else {
        //                setLblAccName("");
        //            }
        //        }catch(Exception e){
        //
        //        }
    }
    
    
    void setTxtBranchCodeAI(String txtBranchCodeAI){
        this.txtBranchCodeAI = txtBranchCodeAI;
        setChanged();
    }
    String getTxtBranchCodeAI(){
        return this.txtBranchCodeAI;
    }
    
    void setTxtPrevActNoAI(String txtPrevActNoAI){
        this.txtPrevActNoAI = txtPrevActNoAI;
        setChanged();
    }
    String getTxtPrevActNoAI(){
        return this.txtPrevActNoAI;
    }
    
    void setDtdActOpenDateAI(String dtdActOpenDateAI){
        this.dtdActOpenDateAI = dtdActOpenDateAI;
        setChanged();
    }
    String getDtdActOpenDateAI(){
        return this.dtdActOpenDateAI;
    }
    
    void setTxtAmoutTransAI(String txtAmoutTransAI){
        this.txtAmoutTransAI = txtAmoutTransAI;
        setChanged();
    }
    String getTxtAmoutTransAI(){
        return this.txtAmoutTransAI;
    }
    
    void setTxtRemarksAI(String txtRemarksAI){
        this.txtRemarksAI = txtRemarksAI;
        setChanged();
    }
    String getTxtRemarksAI(){
        return this.txtRemarksAI;
    }
    
    void setCboProductIdAI(String cboProductIdAI){
        this.cboProductIdAI = cboProductIdAI;
        setChanged();
    }
    String getCboProductIdAI(){
        return this.cboProductIdAI;
    }
    
    void setTxtCustomerIdAI(String txtCustomerIdAI){
        this.txtCustomerIdAI = txtCustomerIdAI;
        setChanged();
    }
    String getTxtCustomerIdAI(){
        return this.txtCustomerIdAI;
    }
    
    void setLblCustValue(String lblCustValue){
        this.lblCustValue = lblCustValue;
        setChanged();
    }
    String getLblCustValue(){
        return this.lblCustValue;
    }
    
    void setLblCityValue(String lblCityValue){
        this.lblCityValue = lblCityValue;
        setChanged();
    }
    String getLblCityValue(){
        return this.lblCityValue;
    }
    
    void setLblDOBValue(String lblDOBValue){
        this.lblDOBValue = lblDOBValue;
        setChanged();
    }
    String getLblDOBValue(){
        return this.lblDOBValue;
    }
    
    void setLblCountryValue(String lblCountryValue){
        this.lblCountryValue = lblCountryValue;
        setChanged();
    }
    String getLblCountryValue(){
        return this.lblCountryValue;
    }
    
    void setLblStreetValue(String lblStreetValue){
        this.lblStreetValue = lblStreetValue;
        setChanged();
    }
    String getLblStreetValue(){
        return this.lblStreetValue;
    }
    
    void setLblStateValue(String lblStateValue){
        this.lblStateValue = lblStateValue;
        setChanged();
    }
    
    String getLblStateValue(){
        return this.lblStateValue;
    }
    
    void setLblAreaValue(String lblAreaValue){
        this.lblAreaValue = lblAreaValue;
        setChanged();
    }
    
    String getLblAreaValue(){
        return this.lblAreaValue;
    }
    
    void setLblPinValue(String lblPinValue){
        this.lblPinValue = lblPinValue;
        setChanged();
    }
    
    String getLblPinValue(){
        return this.lblPinValue;
    }
    
    void setTxtPrevActNumAI(String txtPrevActNumAI){
        this.txtPrevActNumAI = txtPrevActNumAI;
        setChanged();
    }
    String getTxtPrevActNumAI(){
        return this.txtPrevActNumAI;
    }
    
    void setDtdOpeningDateAI(String dtdOpeningDateAI){
        this.dtdOpeningDateAI = dtdOpeningDateAI;
        setChanged();
    }
    String getDtdOpeningDateAI(){
        return this.dtdOpeningDateAI;
    }
    
    void setCboActStatusAI(String cboActStatusAI){
        this.cboActStatusAI = cboActStatusAI;
        setChanged();
    }
    String getCboActStatusAI(){
        return this.cboActStatusAI;
    }
    
    void setCboConstitutionAI(String cboConstitutionAI){
        this.cboConstitutionAI = cboConstitutionAI;
        setChanged();
    }
    String getCboConstitutionAI(){
        return this.cboConstitutionAI;
    }
    
    void setCboOpModeAI(String cboOpModeAI){
        this.cboOpModeAI = cboOpModeAI;
        setChanged();
    }
    String getCboOpModeAI(){
        return this.cboOpModeAI;
    }
    
    void setCboCommAddr(String cboCommAddr){
        this.cboCommAddr = cboCommAddr;
        setChanged();
    }
    String getCboCommAddr(){
        return this.cboCommAddr;
    }
    void setCollectRentMM(String collectRentMM){
        this.collectRentMM = collectRentMM;
        setChanged();
    }
    String getCollectRentMM(){
        return collectRentMM;
    }
    void setCollectRentYYYY(String collectRentYYYY){
        this.collectRentYYYY = collectRentYYYY;
        setChanged();
    }
    String getCollectRentYYYY(){
        return collectRentYYYY ;
    }
    void setSiYes(boolean siYes){
        this.siYes = siYes;
        setChanged();
    }
    boolean getSiYes(){
        return this.siYes;
    }
    void setSiNo(boolean siNo){
        this.siNo = siNo;
        setChanged();
    }
    boolean getSiNo(){
        return this.siNo;
    }
    
    
    void setPwdYes(boolean pwdYes){
        this.pwdYes = pwdYes;
        setChanged();
    }
    boolean getPwdYes(){
        return this.pwdYes;
    }
    
    void setPwdNo(boolean pwdNo){
        this.pwdNo = pwdNo;
        setChanged();
    }
    boolean getPwdNo(){
        return this.pwdNo;
    }
    
    //  void setTxtODLimitAI(String txtODLimitAI){
    //  this.txtODLimitAI = txtODLimitAI;
    // setChanged();
    //}
    //String getTxtODLimitAI(){
    // return this.txtODLimitAI;
    //  }
    
    // void setCboGroupCodeAI(String cboGroupCodeAI){
    // this.cboGroupCodeAI = cboGroupCodeAI;
    // setChanged();
    // }
    // String getCboGroupCodeAI(){
    // return this.cboGroupCodeAI;
    //  }
    
    // void setCboSettlementModeAI(String cboSettlementModeAI){
    //    this.cboSettlementModeAI = cboSettlementModeAI;
    //    setChanged();
    // }
    // String getCboSettlementModeAI(){
    //    return this.cboSettlementModeAI;
    //}
    
    void setCboCategory(String cboCategory){
        this.cboCategory = cboCategory;
        setChanged();
    }
    String getCboCategory(){
        return this.cboCategory;
    }
    
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdType() {
        return cbmProdType;
    }
    public void setCbmProdType(com.see.truetransact.clientutil.ComboBoxModel cbmProdType) {
        this.cbmProdType=cbmProdType;
    }
    
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
     * Getter for property cboProdId.
     * @return Value of property cboProdId.
     */
    public java.lang.String getCboProdId() {
        return cboProdId;
    }
    
    /**
     * Setter for property cboProdId.
     * @param cboProdId New value of property cboProdId.
     */
    public void setCboProdId(java.lang.String cboProdId) {
        this.cboProdId = cboProdId;
    }
    
    /**
     * Getter for property cboProdType.
     * @return Value of property cboProdType.
     */
    public java.lang.String getCboProdType() {
        return cboProdType;
    }
    
    /**
     * Setter for property cboProdType.
     * @param cboProdType New value of property cboProdType.
     */
    public void setCboProdType(java.lang.String cboProdType) {
        this.cboProdType = cboProdType;
        setChanged();
    }
    public void setCboProductId(String cboProductId){
        this.cboProductId = cboProductId;
        setChanged();
    }
    String getCboProductId(){
        
        return this.cboProductId;
    }
    
    public void setTxtCustomerIdCr(java.lang.String txtCustomerIdCr) {
        this.txtCustomerIdCr=txtCustomerIdCr;
    }
    public String getTxtCustomerIdCr() {
        return txtCustomerIdCr;
    }
    
    public void setLblCustomerNameCrValue(java.lang.String lblCustomerNameCrValue) {
        this.lblCustomerNameCrValue=lblCustomerNameCrValue;
    }
    public String getLblCustomerNameCrValue() {
        return lblCustomerNameCrValue;
    }
    void setCboBaseCurrAI(String cboBaseCurrAI){
        this.cboBaseCurrAI = cboBaseCurrAI;
        setChanged();
    }
    String getCboBaseCurrAI(){
        return this.cboBaseCurrAI;
    }
    
    void setTxtAccountNoITP1(String txtAccountNoITP1){
        this.txtAccountNoITP1 = txtAccountNoITP1;
        setChanged();
    }
    String getTxtAccountNoITP1(){
        return this.txtAccountNoITP1;
    }
    
    void setTxtBankITP2(String txtBankITP2){
        this.txtBankITP2 = txtBankITP2;
        setChanged();
    }
    String getTxtBankITP2(){
        return this.txtBankITP2;
    }
    
    void setTxtBranchITP2(String txtBranchITP2){
        this.txtBranchITP2 = txtBranchITP2;
        setChanged();
    }
    String getTxtBranchITP2(){
        return this.txtBranchITP2;
    }
    
    void setTxtAccountNoITP2(String txtAccountNoITP2){
        this.txtAccountNoITP2 = txtAccountNoITP2;
        setChanged();
    }
    String getTxtAccountNoITP2(){
        return this.txtAccountNoITP2;
    }
    
    void setTxtNameITP2(String txtNameITP2){
        this.txtNameITP2 = txtNameITP2;
        setChanged();
    }
    String getTxtNameITP2(){
        return this.txtNameITP2;
    }
    
    void setCboDocTypeITP3(String cboDocTypeITP3){
        this.cboDocTypeITP3 = cboDocTypeITP3;
        setChanged();
    }
    String getCboDocTypeITP3(){
        return this.cboDocTypeITP3;
    }
    
    void setTxtDocNoITP3(String txtDocNoITP3){
        this.txtDocNoITP3 = txtDocNoITP3;
        setChanged();
    }
    String getTxtDocNoITP3(){
        return this.txtDocNoITP3;
    }
    
    void setTxtIssuedByITP3(String txtIssuedByITP3){
        this.txtIssuedByITP3 = txtIssuedByITP3;
        setChanged();
    }
    String getTxtIssuedByITP3(){
        return this.txtIssuedByITP3;
    }
    
    void setDtdIssuedDateITP3(String dtdIssuedDateITP3){
        this.dtdIssuedDateITP3 = dtdIssuedDateITP3;
        setChanged();
    }
    String getDtdIssuedDateITP3(){
        return this.dtdIssuedDateITP3;
    }
    
    void setDtdExpiryDate(String dtdExpiryDate){
        this.dtdExpiryDate = dtdExpiryDate;
        setChanged();
    }
    String getDtdExpiryDate(){
        return this.dtdExpiryDate;
    }
    
    void setCboIdentityTypeITP4(String cboIdentityTypeITP4){
        this.cboIdentityTypeITP4 = cboIdentityTypeITP4;
        setChanged();
    }
    String getCboIdentityTypeITP4(){
        return this.cboIdentityTypeITP4;
    }
    
    void setTxtIssuedAuthITP4(String txtIssuedAuthITP4){
        this.txtIssuedAuthITP4 = txtIssuedAuthITP4;
        setChanged();
    }
    String getTxtIssuedAuthITP4(){
        return this.txtIssuedAuthITP4;
    }
    
    void setTxtIdITP4(String txtIdITP4){
        this.txtIdITP4 = txtIdITP4;
        setChanged();
    }
    String getTxtIdITP4(){
        return this.txtIdITP4;
    }
    
    void setTxtIntroNameOTP5(String txtIntroNameOTP5){
        this.txtIntroNameOTP5 = txtIntroNameOTP5;
        setChanged();
    }
    String getTxtIntroNameOTP5(){
        return this.txtIntroNameOTP5;
    }
    
    void setTxtDesignationOTP5(String txtDesignationOTP5){
        this.txtDesignationOTP5 = txtDesignationOTP5;
        setChanged();
    }
    String getTxtDesignationOTP5(){
        return this.txtDesignationOTP5;
    }
    
    void setTxtACodeOTP5(String txtACodeOTP5){
        this.txtACodeOTP5 = txtACodeOTP5;
        setChanged();
    }
    String getTxtACodeOTP5(){
        return this.txtACodeOTP5;
    }
    
    void setTxtPhoneOTP5(String txtPhoneOTP5){
        this.txtPhoneOTP5 = txtPhoneOTP5;
        setChanged();
    }
    String getTxtPhoneOTP5(){
        return this.txtPhoneOTP5;
    }
    
    void setChkPayIntOnCrBalIN(boolean chkPayIntOnCrBalIN){
        this.chkPayIntOnCrBalIN = chkPayIntOnCrBalIN;
        setChanged();
    }
    boolean getChkPayIntOnCrBalIN(){
        return this.chkPayIntOnCrBalIN;
    }
    
    void setChkPayIntOnDrBalIN(boolean chkPayIntOnDrBalIN){
        this.chkPayIntOnDrBalIN = chkPayIntOnDrBalIN;
        setChanged();
    }
    boolean getChkPayIntOnDrBalIN(){
        return this.chkPayIntOnDrBalIN;
    }
    
    void setChkChequeBookAD(boolean chkChequeBookAD){
        this.chkChequeBookAD = chkChequeBookAD;
        setChanged();
    }
    boolean getChkChequeBookAD(){
        return this.chkChequeBookAD;
    }
    
    void setChkCustGrpLimitValidationAD(boolean chkCustGrpLimitValidationAD){
        this.chkCustGrpLimitValidationAD = chkCustGrpLimitValidationAD;
        setChanged();
    }
    boolean getChkCustGrpLimitValidationAD(){
        return this.chkCustGrpLimitValidationAD;
    }
    
    void setChkMobileBankingAD(boolean chkMobileBankingAD){
        this.chkMobileBankingAD = chkMobileBankingAD;
        setChanged();
    }
    boolean getChkMobileBankingAD(){
        return this.chkMobileBankingAD;
    }
    
    void setChkNROStatusAD(boolean chkNROStatusAD){
        this.chkNROStatusAD = chkNROStatusAD;
        setChanged();
    }
    boolean getChkNROStatusAD(){
        return this.chkNROStatusAD;
    }
    
    void setChkATMAD(boolean chkATMAD){
        this.chkATMAD = chkATMAD;
        setChanged();
    }
    boolean getChkATMAD(){
        return this.chkATMAD;
    }
    
    void setTxtATMNoAD(String txtATMNoAD){
        this.txtATMNoAD = txtATMNoAD;
        setChanged();
    }
    String getTxtATMNoAD(){
        return this.txtATMNoAD;
    }
    
    void setDtdATMFromDateAD(String dtdATMFromDateAD){
        this.dtdATMFromDateAD = dtdATMFromDateAD;
        setChanged();
    }
    String getDtdATMFromDateAD(){
        return this.dtdATMFromDateAD;
    }
    
    void setDtdATMToDateAD(String dtdATMToDateAD){
        this.dtdATMToDateAD = dtdATMToDateAD;
        setChanged();
    }
    String getDtdATMToDateAD(){
        return this.dtdATMToDateAD;
    }
    
    void setChkDebitAD(boolean chkDebitAD){
        this.chkDebitAD = chkDebitAD;
        setChanged();
    }
    boolean getChkDebitAD(){
        return this.chkDebitAD;
    }
    
    void setTxtDebitNoAD(String txtDebitNoAD){
        this.txtDebitNoAD = txtDebitNoAD;
        setChanged();
    }
    String getTxtDebitNoAD(){
        return this.txtDebitNoAD;
    }
    
    void setDtdDebitFromDateAD(String dtdDebitFromDateAD){
        this.dtdDebitFromDateAD = dtdDebitFromDateAD;
        setChanged();
    }
    String getDtdDebitFromDateAD(){
        return this.dtdDebitFromDateAD;
    }
    
    void setDtdDebitToDateAD(String dtdDebitToDateAD){
        this.dtdDebitToDateAD = dtdDebitToDateAD;
        setChanged();
    }
    String getDtdDebitToDateAD(){
        return this.dtdDebitToDateAD;
    }
    
    void setChkCreditAD(boolean chkCreditAD){
        this.chkCreditAD = chkCreditAD;
        setChanged();
    }
    boolean getChkCreditAD(){
        return this.chkCreditAD;
    }
    
    void setTxtCreditNoAD(String txtCreditNoAD){
        this.txtCreditNoAD = txtCreditNoAD;
        setChanged();
    }
    String getTxtCreditNoAD(){
        return this.txtCreditNoAD;
    }
    
    void setDtdCreditFromDateAD(String dtdCreditFromDateAD){
        this.dtdCreditFromDateAD = dtdCreditFromDateAD;
        setChanged();
    }
    String getDtdCreditFromDateAD(){
        return this.dtdCreditFromDateAD;
    }
    
    void setDtdCreditToDateAD(String dtdCreditToDateAD){
        this.dtdCreditToDateAD = dtdCreditToDateAD;
        setChanged();
    }
    String getDtdCreditToDateAD(){
        return this.dtdCreditToDateAD;
    }
    
    void setChkFlexiAD(boolean chkFlexiAD){
        this.chkFlexiAD = chkFlexiAD;
        setChanged();
    }
    boolean getChkFlexiAD(){
        return this.chkFlexiAD;
    }
    
    void setTxtMinBal1FlexiAD(String txtMinBal1FlexiAD){
        this.txtMinBal1FlexiAD = txtMinBal1FlexiAD;
        setChanged();
    }
    String getTxtMinBal1FlexiAD(){
        return this.txtMinBal1FlexiAD;
    }
    
    void setTxtMinBal2FlexiAD(String txtMinBal2FlexiAD){
        this.txtMinBal2FlexiAD = txtMinBal2FlexiAD;
        setChanged();
    }
    String getTxtMinBal2FlexiAD(){
        return this.txtMinBal2FlexiAD;
    }
    
    void setTxtReqFlexiPeriodAD(String txtReqFlexiPeriodAD){
        this.txtReqFlexiPeriodAD = txtReqFlexiPeriodAD;
        setChanged();
    }
    String getTxtReqFlexiPeriodAD(){
        return this.txtReqFlexiPeriodAD;
    }
    
    void setCboDMYAD(String cboDMYAD){
        this.cboDMYAD = cboDMYAD;
        setChanged();
    }
    String getCboDMYAD(){
        return this.cboDMYAD;
    }
    
    void setTxtAccOpeningChrgAD(String txtAccOpeningChrgAD){
        this.txtAccOpeningChrgAD = txtAccOpeningChrgAD;
        setChanged();
    }
    String getTxtAccOpeningChrgAD(){
        return this.txtAccOpeningChrgAD;
    }
    
    void setTxtMisServiceChrgAD(String txtMisServiceChrgAD){
        this.txtMisServiceChrgAD = txtMisServiceChrgAD;
        setChanged();
    }
    String getTxtMisServiceChrgAD(){
        return this.txtMisServiceChrgAD;
    }
    
    void setChkStopPmtChrgAD(boolean chkStopPmtChrgAD){
        this.chkStopPmtChrgAD = chkStopPmtChrgAD;
        setChanged();
    }
    boolean getChkStopPmtChrgAD(){
        return this.chkStopPmtChrgAD;
    }
    
    void setTxtChequeBookChrgAD(String txtChequeBookChrgAD){
        this.txtChequeBookChrgAD = txtChequeBookChrgAD;
        setChanged();
    }
    String getTxtChequeBookChrgAD(){
        return this.txtChequeBookChrgAD;
    }
    
    void setChkChequeRetChrgAD(boolean chkChequeRetChrgAD){
        this.chkChequeRetChrgAD = chkChequeRetChrgAD;
        setChanged();
    }
    boolean getChkChequeRetChrgAD(){
        return this.chkChequeRetChrgAD;
    }
    
    void setTxtFolioChrgAD(String txtFolioChrgAD){
        this.txtFolioChrgAD = txtFolioChrgAD;
        setChanged();
    }
    String getTxtFolioChrgAD(){
        return this.txtFolioChrgAD;
    }
    
    void setChkInopChrgAD(boolean chkInopChrgAD){
        this.chkInopChrgAD = chkInopChrgAD;
        setChanged();
    }
    boolean getChkInopChrgAD(){
        return this.chkInopChrgAD;
    }
    
    void setTxtAccCloseChrgAD(String txtAccCloseChrgAD){
        this.txtAccCloseChrgAD = txtAccCloseChrgAD;
        setChanged();
    }
    String getTxtAccCloseChrgAD(){
        return this.txtAccCloseChrgAD;
    }
    
    void setChkStmtChrgAD(boolean chkStmtChrgAD){
        this.chkStmtChrgAD = chkStmtChrgAD;
        setChanged();
    }
    boolean getChkStmtChrgAD(){
        return this.chkStmtChrgAD;
    }
    
    
    void setChkHideBalanceTrans(boolean chkHideBalanceTrans){
        this.chkHideBalanceTrans = chkHideBalanceTrans;
        setChanged();
    }
    boolean getChkHideBalanceTrans(){
        return this.chkHideBalanceTrans;
    }
    
    
    void setChkABBChrgAD(boolean chkABBChrgAD){
        this.chkABBChrgAD = chkABBChrgAD;
        setChanged();
    }
    boolean getChkABBChrgAD(){
        return this.chkABBChrgAD;
    }
    
    void setChkNPAChrgAD(boolean chkNPAChrgAD){
        this.chkNPAChrgAD = chkNPAChrgAD;
        setChanged();
    }
    boolean getChkNPAChrgAD(){
        return this.chkNPAChrgAD;
    }
    
    void setCboStmtFreqAD(String cboStmtFreqAD){
        this.cboStmtFreqAD = cboStmtFreqAD;
        setChanged();
    }
    String getCboStmtFreqAD(){
        return this.cboStmtFreqAD;
    }
    
    void setCboRoleHierarchy(String cboRoleHierarchy){
        this.cboRoleHierarchy = cboRoleHierarchy;
        setChanged();
    }
    String getCboRoleHierarchy(){
        return this.cboRoleHierarchy;
    }
    
    
    
    void setChkNonMainMinBalChrgAD(boolean chkNonMainMinBalChrgAD){
        this.chkNonMainMinBalChrgAD = chkNonMainMinBalChrgAD;
        setChanged();
    }
    boolean getChkNonMainMinBalChrgAD(){
        return this.chkNonMainMinBalChrgAD;
    }
    
    void setTxtExcessWithChrgAD(String txtExcessWithChrgAD){
        this.txtExcessWithChrgAD = txtExcessWithChrgAD;
        setChanged();
    }
    String getTxtExcessWithChrgAD(){
        return this.txtExcessWithChrgAD;
    }
    
    void setTxtMinActBalanceAD(String txtMinActBalanceAD){
        this.txtMinActBalanceAD = txtMinActBalanceAD;
        setChanged();
    }
    String getTxtMinActBalanceAD(){
        return this.txtMinActBalanceAD;
    }
    
    void setTxtABBChrgAD(String txtABBChrgAD){
        this.txtABBChrgAD = txtABBChrgAD;
        setChanged();
    }
    String getTxtABBChrgAD(){
        return this.txtABBChrgAD;
    }
    
    void setDtdNPAChrgAD(String dtdNPAChrgAD){
        this.dtdNPAChrgAD = dtdNPAChrgAD;
        setChanged();
    }
    String getDtdNPAChrgAD(){
        return this.dtdNPAChrgAD;
    }
    
    void setDtdDebit(String dtdDebit){
        this.dtdDebit = dtdDebit;
        setChanged();
    }
    String getDtdDebit(){
        return this.dtdDebit;
    }
    
    void setDtdCredit(String dtdCredit){
        this.dtdCredit = dtdCredit;
        setChanged();
    }
    
    String getDtdCredit(){
        return this.dtdCredit;
    }
    
    void setTxtNomineeNameNO(String txtNomineeNameNO){
        this.txtNomineeNameNO = txtNomineeNameNO;
        setChanged();
    }
    String getTxtNomineeNameNO(){
        return this.txtNomineeNameNO;
    }
    
    void setCboNomineeRelationNO(String cboNomineeRelationNO){
        this.cboNomineeRelationNO = cboNomineeRelationNO;
        setChanged();
    }
    String getCboNomineeRelationNO(){
        return this.cboNomineeRelationNO;
    }
    
    void setTxtNomineePhoneNO(String txtNomineePhoneNO){
        this.txtNomineePhoneNO = txtNomineePhoneNO;
        setChanged();
    }
    String getTxtNomineePhoneNO(){
        return this.txtNomineePhoneNO;
    }
    
    void setTxtNomineeACodeNO(String txtNomineeACodeNO){
        this.txtNomineeACodeNO = txtNomineeACodeNO;
        setChanged();
    }
    String getTxtNomineeACodeNO(){
        return this.txtNomineeACodeNO;
    }
    
    void setRdoMajorNO(boolean rdoMajorNO){
        this.rdoMajorNO = rdoMajorNO;
        setChanged();
    }
    boolean getRdoMajorNO(){
        return this.rdoMajorNO;
    }
    
    void setRdoMinorNO(boolean rdoMinorNO){
        this.rdoMinorNO = rdoMinorNO;
        setChanged();
    }
    boolean getRdoMinorNO(){
        return this.rdoMinorNO;
    }
    
    void setTxtNomineeShareNO(String txtNomineeShareNO){
        this.txtNomineeShareNO = txtNomineeShareNO;
        setChanged();
    }
    String getTxtNomineeShareNO(){
        return this.txtNomineeShareNO;
    }
    
    void setDtdMinorDOBNO(String dtdMinorDOBNO){
        this.dtdMinorDOBNO = dtdMinorDOBNO;
        setChanged();
    }
    String getDtdMinorDOBNO(){
        return this.dtdMinorDOBNO;
    }
    
    void setCboRelationNO(String cboRelationNO){
        this.cboRelationNO = cboRelationNO;
        setChanged();
    }
    String getCboRelationNO(){
        return this.cboRelationNO;
    }
    
    void setTxtGuardianNameNO(String txtGuardianNameNO){
        this.txtGuardianNameNO = txtGuardianNameNO;
        setChanged();
    }
    String getTxtGuardianNameNO(){
        return this.txtGuardianNameNO;
    }
    
    void setTxtGuardianPhoneNO(String txtGuardianPhoneNO){
        this.txtGuardianPhoneNO = txtGuardianPhoneNO;
        setChanged();
    }
    String getTxtGuardianPhoneNO(){
        return this.txtGuardianPhoneNO;
    }
    
    void setTxtGuardianACodeNO(String txtGuardianACodeNO){
        this.txtGuardianACodeNO = txtGuardianACodeNO;
        setChanged();
    }
    String getTxtGuardianACodeNO(){
        return this.txtGuardianACodeNO;
    }
    
    void setTxtTotalShareNO(String txtTotalShareNO){
        this.txtTotalShareNO = txtTotalShareNO;
        setChanged();
    }
    String getTxtTotalShareNO(){
        return this.txtTotalShareNO;
    }
    
    void setTxtPOANamePA(String txtPOANamePA){
        this.txtPOANamePA = txtPOANamePA;
        setChanged();
    }
    String getTxtPOANamePA(){
        return this.txtPOANamePA;
    }
    
    void setTxtPOAPhonePA(String txtPOAPhonePA){
        this.txtPOAPhonePA = txtPOAPhonePA;
        setChanged();
    }
    String getTxtPOAPhonePA(){
        return this.txtPOAPhonePA;
    }
    
    void setTxtPOAACodePA(String txtPOAACodePA){
        this.txtPOAACodePA = txtPOAACodePA;
        setChanged();
    }
    String getTxtPOAACodePA(){
        return this.txtPOAACodePA;
    }
    
    void setDtdPOAFromDatePA(String dtdPOAFromDatePA){
        this.dtdPOAFromDatePA = dtdPOAFromDatePA;
        setChanged();
    }
    String getDtdPOAFromDatePA(){
        return this.dtdPOAFromDatePA;
    }
    
    void setDtdPOAToDatePA(String dtdPOAToDatePA){
        this.dtdPOAToDatePA = dtdPOAToDatePA;
        setChanged();
    }
    String getDtdPOAToDatePA(){
        return this.dtdPOAToDatePA;
    }
    
    void setTxtRemarksPA(String txtRemarksPA){
        this.txtRemarksPA = txtRemarksPA;
        setChanged();
    }
    String getTxtRemarksPA(){
        return this.txtRemarksPA;
    }
    
    String getLblActHeadValueAI() {
        return lblActHeadValueAI;
    }
    void setLblActHeadValueAI(String lblActHeadValueAI) {
        this.lblActHeadValueAI = lblActHeadValueAI;
    }
    
    String getLblActHeadValueITP1() {
        return lblActHeadValueITP1;
    }
    void setLblActHeadValueITP1(String lblActHeadValueITP1) {
        this.lblActHeadValueITP1 = lblActHeadValueITP1;
    }
    
    String getLblAgClearingValueIN() {
        return lblAgClearingValueIN;
    }
    void setLblAgClearingValueIN(String lblAgClearingValueIN) {
        this.lblAgClearingValueIN = lblAgClearingValueIN;
    }
    
    String getLblBranchCodeValueITP1() {
        return lblBranchCodeValueITP1;
    }
    void setLblBranchCodeValueITP1(String lblBranchCodeValueITP1) {
        this.lblBranchCodeValueITP1 = lblBranchCodeValueITP1;
    }
    
    String getLblBranchNameValueAI() {
        return lblBranchNameValueAI;
    }
    void setLblBranchNameValueAI(String lblBranchNameValueAI) {
        this.lblBranchNameValueAI = lblBranchNameValueAI;
    }
    
    String getLblBranchValueITP1() {
        return lblBranchValueITP1;
    }
    void setLblBranchValueITP1(String lblBranchValueITP1) {
        this.lblBranchValueITP1 = lblBranchValueITP1;
    }
    
    String getLblCrInterestRateValueIN() {
        return lblCrInterestRateValueIN;
    }
    void setLblCrInterestRateValueIN(String lblCrInterestRateValueIN) {
        this.lblCrInterestRateValueIN = lblCrInterestRateValueIN;
    }
    
    String getLblCustomerIdValueITP1() {
        return lblCustomerIdValueITP1;
    }
    void setLblCustomerIdValueITP1(String lblCustomerIdValueITP1) {
        this.lblCustomerIdValueITP1 = lblCustomerIdValueITP1;
    }
    
    String getLblCustomerNameValueAI() {
        return lblCustomerNameValueAI;
    }
    void setLblCustomerNameValueAI(String lblCustomerNameValueAI) {
        this.lblCustomerNameValueAI = lblCustomerNameValueAI;
    }
    
    String getLblDrInterestRateValueIN() {
        return lblDrInterestRateValueIN;
    }
    void setLblDrInterestRateValueIN(String lblDrInterestRateValueIN) {
        this.lblDrInterestRateValueIN = lblDrInterestRateValueIN;
    }
    
    //    String getLblIntroducerTypeValueIT() {
    //        return lblIntroducerTypeValueIT;
    //    }
    //    void setLblIntroducerTypeValueIT(String lblIntroducerTypeValueIT) {
    //        this.lblIntroducerTypeValueIT = lblIntroducerTypeValueIT;
    //    }
    
    String getLblNameValueITP1() {
        return lblNameValueITP1;
    }
    void setLblNameValueITP1(String lblNameValueITP1) {
        this.lblNameValueITP1 = lblNameValueITP1;
    }
    
    String getLblPenalInterestValueIN() {
        return lblPenalInterestValueIN;
    }
    void setLblPenalInterestValueIN(String lblPenalInterestValueIN) {
        this.lblPenalInterestValueIN = lblPenalInterestValueIN;
    }
    
    String getLblRateCodeValueIN() {
        return lblRateCodeValueIN;
    }
    void setLblRateCodeValueIN(String lblRateCodeValueIN) {
        this.lblRateCodeValueIN = lblRateCodeValueIN;
    }
    
    Address getCustomerAddress() {
        return customerAddress;
    }
    void setCustomerAddress(Address customerAddress) {
        this.customerAddress = customerAddress;
    }
    
    Address getOthersAddress() {
        return othersAddress;
    }
    void setOthersAddress(Address othersAddress) {
        this.othersAddress = othersAddress;
    }
    
    //    Address getPoaAddress() {
    //        return poaAddress;
    //    }
    //    void setPoaAddress(Address poaAddress) {
    //        this.poaAddress = poaAddress;
    //    }
    
    String getAccountNumber() {
        return accountNumber;
    }
    void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    int getOperation() {
        return operation;
    }
    void setOperation(int operation) {
        this.operation = operation;
    }
    
    public int getResult(){
        return this.result;
    }
    
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    
    public String getLblStatus(){
        return this.lblStatus;
    }
    
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getOperation()]);
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }
    
    // To set the Value of Account No in UI...
    void setAccountNo(String lblAccountNo) {
        this.lblAccountNo = lblAccountNo;
    }
    
    String getAccountNo() {
        return lblAccountNo;
    }
    
    /*
     * Meathods to take the Data from the Product oppt Account and to implement
     *the rules in AccontUI...
     */
    public HashMap getAccountParamData(String ID){
        HashMap resultMap = new HashMap();
        try {
            final HashMap accountDateMap = new HashMap();
            accountDateMap.put("PRODID",ID);
            final List resultList = ClientUtil.executeQuery("getAccountParamData", accountDateMap);
            resultMap = (HashMap)resultList.get(0);
        }catch(Exception e){
            System.out.println("Error in getAccountParamData()");
        }
        return resultMap;
    }
    
    public HashMap getAccountCardsData(String ID){
        HashMap resultMap = new HashMap();
        try {
            final HashMap accountCardsDateMap = new HashMap();
            accountCardsDateMap.put("PRODID",ID);
            final List resultList = ClientUtil.executeQuery("getAccountCardsData", accountCardsDateMap);
            resultMap = (HashMap)resultList.get(0);
        }catch(Exception e){
            System.out.println("Error in getAccountCardsData()");
        }
        return resultMap;
    }
    
    public HashMap getAccountChargesData(String ID){
        HashMap resultMap = new HashMap();
        try {
            final HashMap accountChargesDateMap = new HashMap();
            accountChargesDateMap.put("PRODID",ID);
            final List resultList = ClientUtil.executeQuery("getAccountChargesData", accountChargesDateMap);
            resultMap = (HashMap)resultList.get(0);
        }catch(Exception e){
            System.out.println("Error in getAccountChargesData()");
        }
        return resultMap;
    }
    
    public HashMap getAccountCreditData(String ID){
        HashMap resultMap = new HashMap();
        try {
            final HashMap accountCreditDateMap = new HashMap();
            accountCreditDateMap.put("PRODID",ID);
            final List resultList = ClientUtil.executeQuery("getAccountCreditData", accountCreditDateMap);
            resultMap = (HashMap)resultList.get(0);
        }catch(Exception e){
            System.out.println("Error in getAccountCreditData()");
        }
        return resultMap;
    }
    
    
    public ComboBoxModel getCbmPrevAcctNo() {
        return cbmPrevAcctNo;
    }
    public void setCbmPrevAcctNo(ComboBoxModel cbmPrevAcctNo) {
        this.cbmPrevAcctNo = cbmPrevAcctNo;
    }
    
    public ComboBoxModel getCbmRoleHierarchy() {
        return cbmRoleHierarchy;
    }
    public void setCbmRoleHierarchy(ComboBoxModel cbmRoleHierarchy) {
        this.cbmRoleHierarchy = cbmRoleHierarchy;
    }
    
    public ComboBoxModel getCbmChargeType() {
        return cbmChargeType;
    }
    public void setCbmChargeType(ComboBoxModel cbmChargeType) {
        this.cbmChargeType = cbmChargeType;
    }
    
    
    
    /** Method for displaying the Message box
     * @param dialog that has to be displayed
     * returns int value depending onw hich u slect, whethere Yes or No.
     */
    public int showDialog(String dialogString){
        COptionPane dialog = new COptionPane();
        String[] strDialog = {resourceBundle.getString(DIALOG_YES),resourceBundle.getString(DIALOG_NO)};
        int yesOrNo = dialog.showOptionDialog(null,resourceBundle.getString(dialogString),CommonConstants.WARNINGTITLE,COptionPane.YES_NO_OPTION,COptionPane.WARNING_MESSAGE, null, strDialog, strDialog[0]);
        return yesOrNo;
    }
    /** Method for displaying the Message box
     * @param dialogStr that has to be displayed
     * returns int value depending onw hich u slect, whethere Yes or No.
     */
    public int checkMainAcctHolder(String dialogStr){
        COptionPane dialog = new COptionPane();
        String[] strDialog = {resourceBundle.getString(DIALOG_OK)};
        int checkDt = dialog.showOptionDialog(null,resourceBundle.getString(dialogStr),CommonConstants.WARNINGTITLE,COptionPane.OK_OPTION,COptionPane.WARNING_MESSAGE, null, strDialog, strDialog[0]);
        return checkDt;
    }
    
    /** Method for resetting the Joint Account Holder  table
     */
    public void resetJntAccntHoldTbl(){
        tblJointAccnt.setDataArrayList(null, tblJointAccntColTitle);
    }
    
    public void resetPassTable(){
        tbmInstructions2 = new EnhancedTableModel(null, tblInstruction2);
        //        ttNotifyObservers();
        allInstructionsList = null;
    }
    public void resetChrgsTable(){
        tbmLockCharges = new EnhancedTableModel(null, tblHeadings3);
        ttNotifyObservers();
    }
    public void ttNotifyObservers(){
        this.notifyObservers();
    }
    public String getCustName(String custNo){
        String custName = "";
        HashMap dataMap = new HashMap();
        dataMap.put("CUST_ID",custNo);
        final List resultList = ClientUtil.executeQuery("Account.getCustName", dataMap);
        final HashMap resultMap = (HashMap)resultList.get(0);
        custName = CommonUtil.convertObjToStr(resultMap.get("NAME"));
        
        return custName;
    }
    
    /**
     * To get the Value of CustomerAddr ComboBox...
     */
    public void getCustAddrData(String custID){
        try {
            HashMap lookUpHash = new HashMap();
            
            lookUpHash.put(CommonConstants.MAP_NAME,"Account.getCustAddr");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, custID);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            fillData((HashMap)keyValue.get(CommonConstants.DATA));
            cbmCommAddr = new ComboBoxModel(key,value);
        }catch(Exception e){
            e.printStackTrace();
            parseException.logException(e,true);
        }
    }
    
    public HashMap getCustAddrType(HashMap dataMap){
        HashMap resultMap = new HashMap();
        System.out.println("getCustAddrType Called");
        final List custAddrData = ClientUtil.executeQuery("Account.getCustAddressData",dataMap );
        if(custAddrData != null && custAddrData.size() > 0){
            resultMap = (HashMap)custAddrData.get(0);
            
            setLblCustValue(CommonUtil.convertObjToStr(resultMap.get("Name")));
            setLblDOBValue(DateUtil.getStringDate((java.util.Date)resultMap.get("DOB")));
            setLblStreetValue(CommonUtil.convertObjToStr(resultMap.get("STREET")));
            setLblAreaValue(CommonUtil.convertObjToStr(resultMap.get("AREA")));
            setLblCityValue(CommonUtil.convertObjToStr(resultMap.get("CITY1")));
            setLblStateValue(CommonUtil.convertObjToStr(resultMap.get("STATE1")));
            
            
            setLblCountryValue(CommonUtil.convertObjToStr(resultMap.get("COUNTRY1")));
            setLblPinValue(CommonUtil.convertObjToStr(resultMap.get("PIN_CODE")));
            if(resultMap.get("MINOR")!=null && resultMap.get("MINOR").equals("Y")) {
                setLblMajOMinVal("MINOR");
            } else {
                setLblMajOMinVal("MAJOR");
            }
            
        }
        return resultMap;
    }
    
    public void doAction(String command) {
        try {
            //            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
            //If actionType has got propervalue then doActionPerform, else throw error
            //                if( getCommand() != null || getAuthorizeMap() != null){
            doActionPerform(command);
            //                }
            //                else{
            //                    final RemittancePaymentRB objRemittancePaymentRB = new RemittancePaymentRB();
            //                    throw new TTException(objRemittancePaymentRB.getString("TOCommandError"));
            //                }
            //            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    private void doActionPerform(String command) throws Exception{
        final HashMap data = new HashMap();
        String exp=getDtdExpiryDate();
        data.put("MODE", command);
        //data.put("EXP_DT",exp);
        // data.put("EXP_DT",);
        //        if(getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || getActionType() == ClientConstants.ACTIONTYPE_REJECT
        //        || getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION){
        data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
        
        //   data.put(CommonConstants.AUTHORIZEMAP,getDtdExpiryDate());
        data.put(CommonConstants.USER_ID, _authorizeMap.get(CommonConstants.USER_ID));
        //        }else {
        //            final LockerSurrenderTO objLockerSurrenderTO = getLockerSurrenderTO(command);
        ////            objRemittancePaymentTO.setCommand(getCommand());
        //            if (transactionDetailsTO == null)
        //                transactionDetailsTO = new LinkedHashMap();
        //
        //            if (deletedTransactionDetailsTO != null) {
        //                transactionDetailsTO.put(DELETED_TRANS_TOs,deletedTransactionDetailsTO);
        //                deletedTransactionDetailsTO = null;
        //            }
        //
        //            transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,allowedTransactionDetailsTO);
        //            allowedTransactionDetailsTO = null;
        //
        //            data.put("TransactionTO",transactionDetailsTO);
        //
        //            data.put("LockerSurrenderTO",objLockerSurrenderTO);
        //            data.put(CommonConstants.MODULE, getModule());
        //            data.put(CommonConstants.SCREEN, getScreen());
        //            System.out.println("DATA## : " + data);
        //        }
        data.put(CommonConstants.SCREEN, getScreen()); // 23-08-2016
        System.out.println("DATA###### : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
        //        setResult(actionType);
    }
    
    String getTxtActName() {
        return txtActName;
    }
    public void setTxtActName(String txtActName) {
        this.txtActName = txtActName;
    }
    
    
    String getTxtRemarks() {
        return txtRemarks;
    }
    public void setTxtRemarks(String txtRemarks) {
        this.txtRemarks = txtRemarks;
    }
    
    
    public String getAddrType() {
        return this.addrType;
    }
    public void setAddrType(String addrType) {
        this.addrType = addrType;
    }
    
    /**
     * Getter for property chkPassBook.
     * @return Value of property chkPassBook.
     */
    public boolean isChkPassBook() {
        return chkPassBook;
    }
    void setTxtAccNo(String txtAccNo){
        this.txtAccNo = txtAccNo;
        setChanged();
    }
    String getTxtAccNo(){
        return this.txtAccNo;
    }
    /**
     * Setter for property chkPassBook.
     * @param chkPassBook New value of property chkPassBook.
     */
    public void setChkPassBook(boolean chkPassBook) {
        this.chkPassBook = chkPassBook;
    }
    
    /**
     * Getter for property ClosedDt.
     * @return Value of property ClosedDt.
     */
    public java.lang.String getClosedDt() {
        return ClosedDt;
    }
    
    /**
     * Setter for property ClosedDt.
     * @param ClosedDt New value of property ClosedDt.
     */
    public void setClosedDt(java.lang.String ClosedDt) {
        this.ClosedDt = ClosedDt;
    }
    /**
     * Getter for property staffOnly.
     * @return Value of property staffOnly.
     */
    public java.lang.String getStaffOnly() {
        return staffOnly;
    }
    
    /**
     * Setter for property staffOnly.
     * @param staffOnly New value of property staffOnly.
     */
    public void setStaffOnly(java.lang.String staffOnly) {
        this.staffOnly = staffOnly;
    }
    
    /**
     * Getter for property lblMajOMinVal.
     * @return Value of property lblMajOMinVal.
     */
    public java.lang.String getLblMajOMinVal() {
        return lblMajOMinVal;
    }
    
    /**
     * Setter for property lblMajOMinVal.
     * @param lblMajOMinVal New value of property lblMajOMinVal.
     */
    public void setLblMajOMinVal(java.lang.String lblMajOMinVal) {
        this.lblMajOMinVal = lblMajOMinVal;
    }
    
    /**
     * Getter for property tbmInstructions2.
     * @return Value of property tbmInstructions2.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTbmInstructions2() {
        return tbmInstructions2;
    }
    /**
     * Getter for property _authorizeMap.
     * @return Value of property _authorizeMap.
     */
    public java.util.HashMap getAuthorizeMap() {
        return _authorizeMap;
    }
    
    /**
     * Setter for property _authorizeMap.
     * @param _authorizeMap New value of property _authorizeMap.
     */
    public void setAuthorizeMap(java.util.HashMap _authorizeMap) {
        this._authorizeMap = _authorizeMap;
    }
    /**
     * Setter for property tbmInstructions2.
     * @param tbmInstructions2 New value of property tbmInstructions2.
     */
    public void setTbmInstructions2(com.see.truetransact.clientutil.EnhancedTableModel tbmInstructions2) {
        this.tbmInstructions2 = tbmInstructions2;
    }
    
    /**
     * Getter for property txtPasCustId.
     * @return Value of property txtPasCustId.
     */
    public java.lang.String getTxtPasCustId() {
        return txtPasCustId;
    }
    
    /**
     * Setter for property txtPasCustId.
     * @param txtPasCustId New value of property txtPasCustId.
     */
    public void setTxtPasCustId(java.lang.String txtPasCustId) {
        this.txtPasCustId = txtPasCustId;
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
     * Getter for property txtConPassword.
     * @return Value of property txtConPassword.
     */
    public java.lang.String getTxtConPassword() {
        return txtConPassword;
    }
    
    /**
     * Setter for property txtConPassword.
     * @param txtConPassword New value of property txtConPassword.
     */
    public void setTxtConPassword(java.lang.String txtConPassword) {
        this.txtConPassword = txtConPassword;
    }
    
    /**
     * Getter for property txtConPassword.
     * @return Value of property txtConPassword.
     */
    public java.lang.String getTxtLockerNo() {
        return txtLockerNo;
    }
    
    /**
     * Setter for property txtConPassword.
     * @param txtConPassword New value of property txtConPassword.
     */
    public void setTxtLockerNo(java.lang.String txtLockerNo) {
        this.txtLockerNo = txtLockerNo;
    }
    
    /**
     * Getter for property txtConPassword.
     * @return Value of property txtConPassword.
     */
    public java.lang.String getLblLockerKeyNoVal() {
        return lblLockerKeyNoVal;
    }
    
    /**
     * Setter for property txtConPassword.
     * @param txtConPassword New value of property txtConPassword.
     */
    public void setLblLockerKeyNoVal(java.lang.String lblLockerKeyNoVal) {
        this.lblLockerKeyNoVal = lblLockerKeyNoVal;
    }
    
    /**
     * Getter for property cboChargeType.
     * @return Value of property cboChargeType.
     */
    public java.lang.String getCboChargeType() {
        return cboChargeType;
    }
    
    /**
     * Setter for property cboChargeType.
     * @param cboChargeType New value of property cboChargeType.
     */
    public void setCboChargeType(java.lang.String cboChargeType) {
        this.cboChargeType = cboChargeType;
    }
    
    /**
     * Getter for property tdtFromDt.
     * @return Value of property tdtFromDt.
     */
    public java.lang.String getTdtFromDt() {
        return tdtFromDt;
    }
    
    /**
     * Setter for property tdtFromDt.
     * @param tdtFromDt New value of property tdtFromDt.
     */
    public void setTdtFromDt(java.lang.String tdtFromDt) {
        this.tdtFromDt = tdtFromDt;
    }
    
    /**
     * Getter for property tdtToDt.
     * @return Value of property tdtToDt.
     */
    public java.lang.String getTdtToDt() {
        return tdtToDt;
    }
    
    /**
     * Setter for property tdtToDt.
     * @param tdtToDt New value of property tdtToDt.
     */
    public void setTdtToDt(java.lang.String tdtToDt) {
        this.tdtToDt = tdtToDt;
    }
    
    /**
     * Getter for property txtAmt.
     * @return Value of property txtAmt.
     */
    public java.lang.String getTxtAmt() {
        return txtAmt;
    }
    
    /**
     * Setter for property txtAmt.
     * @param txtAmt New value of property txtAmt.
     */
    public void setTxtAmt(java.lang.String txtAmt) {
        this.txtAmt = txtAmt;
    }
    
    /**
     * Getter for property txtServiceTax.
     * @return Value of property txtServiceTax.
     */
    public java.lang.String getTxtServiceTax() {
        return txtServiceTax;
    }
    
    /**
     * Setter for property txtServiceTax.
     * @param txtServiceTax New value of property txtServiceTax.
     */
    public void setTxtServiceTax(java.lang.String txtServiceTax) {
        this.txtServiceTax = txtServiceTax;
    }
    
    /**
     * Getter for property tbmLockCharges.
     * @return Value of property tbmLockCharges.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTbmLockCharges() {
        return tbmLockCharges;
    }
    
    /**
     * Setter for property tbmLockCharges.
     * @param tbmLockCharges New value of property tbmLockCharges.
     */
    public void setTbmLockCharges(com.see.truetransact.clientutil.EnhancedTableModel tbmLockCharges) {
        this.tbmLockCharges = tbmLockCharges;
    }
    
    /**
     * Getter for property txtCharges.
     * @return Value of property txtCharges.
     */
    public java.lang.String getTxtCharges() {
        return txtCharges;
    }
    
    /**
     * Setter for property txtCharges.
     * @param txtCharges New value of property txtCharges.
     */
    public void setTxtCharges(java.lang.String txtCharges) {
        this.txtCharges = txtCharges;
    }

    public String getDtdFreezeDt() {
        return dtdFreezeDt;
    }

    public void setDtdFreezeDt(String dtdFreezeDt) {
        this.dtdFreezeDt = dtdFreezeDt;
    }

    public boolean isRdoFreeze() {
        return rdoFreeze;
    }

    public void setRdoFreeze(boolean rdoFreeze) {
        this.rdoFreeze = rdoFreeze;
    }

    public boolean isRdoUnFreeze() {
        return rdoUnFreeze;
    }

    public void setRdoUnFreeze(boolean rdoUnFreeze) {
        this.rdoUnFreeze = rdoUnFreeze;
    }

    public String getTxtFreezeRemarks() {
        return txtFreezeRemarks;
    }

    public void setTxtFreezeRemarks(String txtFreezeRemarks) {
        this.txtFreezeRemarks = txtFreezeRemarks;
    }

    public String getSlNo() {
        return slNo;
    }

    public void setSlNo(String slNo) {
        this.slNo = slNo;
    }
    
    /**
     * Getter for property authorizeDt.
     * @return Value of property authorizeDt.
     */
    public java.util.Date getAuthorizeDt() {
        return authorizeDt;
    }
    
    /**
     * Setter for property authorizeDt.
     * @param authorizeDt New value of property authorizeDt.
     */
    public void setAuthorizeDt(java.util.Date authorizeDt) {
        this.authorizeDt = authorizeDt;
    }
    public void setProdType(java.lang.String prodType) {
        this.prodType = prodType;
    }
    String getProdType() {
        return prodType;
    }
    /**
     * Getter for property existingPwd.
     * @return Value of property existingPwd.
     */
    public java.lang.String getExistingPwd() {
        return existingPwd;
    }
    
    /**
     * Setter for property existingPwd.
     * @param existingPwd New value of property existingPwd.
     */
    public void setExistingPwd() {
        if (allInstructionsList!=null && allInstructionsList.size()>0) {
            ArrayList tempList = null;
            tempList = (ArrayList)allInstructionsList.get(0);
            existingPwd = CommonUtil.convertObjToStr(tempList.get(3));
        }
        
    }

    public String getChkNoTransaction() {
        return chkNoTransaction;
    }

    public void setChkNoTransaction(String chkNoTransaction) {
        this.chkNoTransaction = chkNoTransaction;
    }
    
    public HashMap checkServiceTaxApplicable(String accheadId) {
        String checkFlag = "N";
        HashMap checkForTaxMap = new HashMap();
        if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
            HashMap whereMap = new HashMap();
            whereMap.put("AC_HD_ID", accheadId);
            List accHeadList = ClientUtil.executeQuery("getCheckServiceTaxApplicableForShare", whereMap);                  
            if (accHeadList != null && accHeadList.size() > 0) {
                HashMap accHeadMap = (HashMap) accHeadList.get(0);
                if (accHeadMap != null && accHeadMap.containsKey("SERVICE_TAX_APPLICABLE")&& accHeadMap.containsKey("SERVICE_TAX_ID")) {
                    checkFlag = CommonUtil.convertObjToStr(accHeadMap.get("SERVICE_TAX_APPLICABLE"));
                    checkForTaxMap.put("SERVICE_TAX_APPLICABLE",accHeadMap.get("SERVICE_TAX_APPLICABLE"));
                    checkForTaxMap.put("SERVICE_TAX_ID",accHeadMap.get("SERVICE_TAX_ID"));
                }
            }
        }
        return checkForTaxMap;
    }
    
    // Added by nithya on 23-05-2019 for KD 402 - 0019214: gst problem in locker opening

    public String getLblServiceTaxval() {
        return lblServiceTaxval;
    }

    public void setLblServiceTaxval(String lblServiceTaxval) {
        this.lblServiceTaxval = lblServiceTaxval;
    }

    public HashMap getServiceTax_Map() {
        return serviceTax_Map;
    }

    public void setServiceTax_Map(HashMap serviceTax_Map) {
        this.serviceTax_Map = serviceTax_Map;
    }
    
}
