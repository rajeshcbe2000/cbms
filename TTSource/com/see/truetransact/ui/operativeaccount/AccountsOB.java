/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * AccountsOB.java
 *
 * Created on September 3, 2003, 07:35 PM
 */

package com.see.truetransact.ui.operativeaccount;

import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.*;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.LocaleConstants;
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
import java.util.Date;
import com.see.truetransact.transferobject.common.mobile.SMSSubscriptionTO;
import com.see.truetransact.transferobject.operativeaccount.CardAccountTO;
import com.see.truetransact.uicomponent.CTable;
import java.text.SimpleDateFormat;

/**
 *
 * @author  Pranav
 */
public class AccountsOB extends CObservable {
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
    private final String NO_FULL_STR = "No";
    private final String SCREEN = "OA";
    
    
    private HashMap map = null;
    private ProxyFactory proxy = null;
    //    private AccountsRB resourceBundle = new AccountsRB();
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.operativeaccount.AccountsRB", ProxyParameters.LANGUAGE);
    private String cboAgentId = "";
    private String txtBranchCodeAI = "";
    private String txtPrevActNoAI = "";
    private String dtdActOpenDateAI = "";
    private String txtAmoutTransAI = "";
    private String txtRemarksAI = "";
    private String txtOpeningAmount = "";
    private String cboProductIdAI = "";
    private String txtCustomerIdAI = "";
    private String txtPrevActNumAI = "";
    private String dtdOpeningDateAI = "";
    private String cboActStatusAI = "";
    private String cboLinkingProductId = "";
    private String cboConstitutionAI = "";
    private String cboOpModeAI = "";
    private String cboCommAddr = "";
    private String txtODLimitAI = "";
    private String cboGroupCodeAI = "";
    private String cboSettlementModeAI = "";
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
    private String dtdExpiryDateITP3 = "";
    private String cboIdentityTypeITP4 = "";
    private String txtIssuedAuthITP4 = "";
    private String txtIdITP4 = "";
    private String txtIntroNameOTP5 = "";
    private String txtDesignationOTP5 = "";
    private String txtACodeOTP5 = "";
    private String txtPhoneOTP5 = "";
    private boolean chkPayIntOnCrBalIN = false;
    private boolean chkPayIntOnDrBalIN = false;
    private boolean chkChequeBookAD = false;
    private boolean chkCustGrpLimitValidationAD = false;
    private boolean chkMobileBankingAD = false;
    private String txtMobileNo = "";    
    private String tdtMobileSubscribedFrom = "";    
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
    private ComboBoxModel cbmagentID;
    private String txtActName = "";
    private String txtRemarks = "";
    private String txtCardActNumber = "";
    private String ClosedDt = "";
    private boolean newData = false;
    
    private boolean chkHideBalanceTrans = false;
    private String cboRoleHierarchy = "";
    
    private ComboBoxModel cbmLinkingProductId;
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
    private String txtAgentID = "";//Added By Revathi.L
    private String txtDealerID = "";
    private String txtUPIMobileNo = "";
    
    //--- Declarations for Joint Account Table
    public LinkedHashMap jntAcctAll;
    HashMap jntAcctSingleRec;
    private EnhancedTableModel tblJointAccnt;
    private ArrayList tblJointAccntColTitle;
    private ArrayList jntAccntRow;
    LinkedHashMap jntAcctTOMap;
    private ArrayList deletedJointDetails = new ArrayList();
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
    
    // store whic operation INSERT/UPDATE/DELETE/LIST is going on
    private int operation;
    
    //To manipulate status message
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    private String counter = new String(new Date().getTime()+"").substring(7);
    
    public String addrType = "";
    
    // Final Strings
    private final String YES = "Y";
    private final String NO = "N";
    private final String DAYS = "DAYS";
    private final String DIALOG_YES = "dialogYes";
    private final String DIALOG_OK = "dialogOk";
    private final String DIALOG_NO = "dialogNo";
    private final String WARN_MESSAGE = "warningMessage";
    private final String YEAR_STR ="YEARS";
    private final String MONTH_STR ="MONTHS";
    private final String DAY_STR ="DAYS";
    
    private final int YEAR_INT = 365;
    private final int MONTH_INT = 30;
    private final int DAY_INT = 1;
    
    private String cboagentId;
    private double tabLenght = 0;
    private Date curDate = null;
    private HashMap transactionMap;
    private HashMap singleAuthorizeMap;
    
    //Card Details
    private EnhancedTableModel tblATMCardDetails;
    final ArrayList cardTitle = new ArrayList();
    String actionType="";
    String actionDt="";
    String txtCardRemarks="";
    String lblATMCardNoVal="";
    private LinkedHashMap cardDetailsMap;
    
    /* To Define the Objects of the POA, and Nominee..*/
    PowerOfAttorneyOB poaOB;
    NomineeOB nomineeOB;
    AuthorizedSignatoryOB authSignOB;
    
    SMSSubscriptionTO objSMSSubscriptionTO = null;

    private boolean rdoKYCNormsYes = false;
    
    private String txtLinkingActNum = "";
    private String txtAtmCardLimit = "";
    private String chkPrimaryAccount = "N";

    public String getTxtAtmCardLimit() {
        return txtAtmCardLimit;
    }

    public void setTxtAtmCardLimit(String txtAtmCardLimit) {
        this.txtAtmCardLimit = txtAtmCardLimit;
    }

    public String getCboLinkingProductId() {
        return cboLinkingProductId;
    }

    public void setCboLinkingProductId(String cboLinkingProductId) {
        this.cboLinkingProductId = cboLinkingProductId;
        setChanged();
    }

    public String getTxtLinkingActNum() {
        return txtLinkingActNum;
    }

    public void setTxtLinkingActNum(String txtLinkingActNum) {
        this.txtLinkingActNum = txtLinkingActNum;
    }

    public boolean getRdoKYCNormsYes() {
        return rdoKYCNormsYes;
    }

    public void setRdoKYCNormsYes(boolean rdoKYCNormsYes) {
        this.rdoKYCNormsYes = rdoKYCNormsYes;
    }

    public boolean getRdoKYCNormsNo() {
        return rdoKYCNormsNo;
    }

    public void setRdoKYCNormsNo(boolean rdoKYCNormsNo) {
        this.rdoKYCNormsNo = rdoKYCNormsNo;
    }
    private boolean rdoKYCNormsNo = false;
    
    public String getCboAgentId() {
        return cboAgentId;
    }

    public void setCboAgentId(String cboAgentId) {
        this.cboAgentId = cboAgentId;
    }

    public ComboBoxModel getCbmagentID() {
        return cbmagentID;
    }

    public void setCbmagentID(ComboBoxModel cbmagentID) {
        this.cbmagentID = cbmagentID;
    }

    public String getCboagentId() {
        return cboagentId;
    }

    public void setCboagentId(String cboagentId) {
        this.cboagentId = cboagentId;
    }
    
    private ComboBoxModel cbmIntroducer;
    private String cboIntroducer = "";
    
    
    // Security details for SB OD - Added by nithya

    private String suretyMemberNo = "";
    private String suretyMemberName = "";
    private String suretyMemberType = "";
    private String suretyMemberContact = "";
    private String suretyMemberSalary = "";
    private String suretyMemberNetworth = "";
    private String borrowerMemberNo = "";
    private String borrowerSalary = "";
    private String borrowerNetworth = "";
    private String chkRenew = ""; // Indicated whether staff OD is in renewal or not
    private LinkedHashMap memberTypeMap;
    private LinkedHashMap deletedMemberTypeMap;    
    private boolean memberTypeData = false;
    private EnhancedTableModel tblMemberTypeDetails;
    final ArrayList tableMemberTitle = new ArrayList();
    private ArrayList IncVal = new ArrayList();
    private String fromDate = null;
    private String toDate = null;
    private String isRenewd = "";
    private TableModel _tableModel;
    private String borrowerAppliedAmnt = "";
    private String isSbOD = ""; // Added by nithya on 19.08.2016
    private String suretyEligAmnt = "";
    private String isODEnhanced = "N";
    private String chkODClose = "";
    private ArrayList oldSuretys = new ArrayList();
    private boolean enhanceValidate = false;

    public String getChkODClose() {
        return chkODClose;
    }

    public void setChkODClose(String chkODClose) {
        this.chkODClose = chkODClose;
    }

    public String getIsODEnhanced() {
        return isODEnhanced;
    }

    public void setIsODEnhanced(String isODEnhanced) {
        this.isODEnhanced = isODEnhanced;
    }    
    
    public String getSuretyEligAmnt() {
        return suretyEligAmnt;
    }

    public void setSuretyEligAmnt(String suretyEligAmnt) {
        this.suretyEligAmnt = suretyEligAmnt;
    }
    
    public String getBorrowerAppliedAmnt() {
        return borrowerAppliedAmnt;
    }

    public void setBorrowerAppliedAmnt(String borrowerAppliedAmnt) {
        this.borrowerAppliedAmnt = borrowerAppliedAmnt;
    }    
    
    public String getIsRenewd() {
        return isRenewd;
    }

    public void setIsRenewd(String isRenewd) {
        this.isRenewd = isRenewd;
    }    
    
    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getBorrowerMemberNo() {
        return borrowerMemberNo;
    }

    public void setBorrowerMemberNo(String borrowerMemberNo) {
        this.borrowerMemberNo = borrowerMemberNo;
    }

    public String getChkRenew() {
        return chkRenew;
    }

    public void setChkRenew(String chkRenew) {
        this.chkRenew = chkRenew;
    }
    
    public String getSuretyMemberContact() {
        return suretyMemberContact;
    }

    public void setSuretyMemberContact(String suretyMemberContact) {
        this.suretyMemberContact = suretyMemberContact;
    }

    public String getSuretyMemberName() {
        return suretyMemberName;
    }

    public void setSuretyMemberName(String suretyMemberName) {
        this.suretyMemberName = suretyMemberName;
    }

    public String getSuretyMemberNetworth() {
        return suretyMemberNetworth;
    }

    public void setSuretyMemberNetworth(String suretyMemberNetworth) {
        this.suretyMemberNetworth = suretyMemberNetworth;
    }

    public String getSuretyMemberNo() {
        return suretyMemberNo;
    }

    public void setSuretyMemberNo(String suretyMemberNo) {
        this.suretyMemberNo = suretyMemberNo;
    }

    public String getSuretyMemberSalary() {
        return suretyMemberSalary;
    }

    public void setSuretyMemberSalary(String suretyMemberSalary) {
        this.suretyMemberSalary = suretyMemberSalary;
    }

    public String getSuretyMemberType() {
        return suretyMemberType;
    }

    public void setSuretyMemberType(String suretyMemberType) {
        this.suretyMemberType = suretyMemberType;
    }

    public String getBorrowerNetworth() {
        return borrowerNetworth;
    }

    public void setBorrowerNetworth(String borrowerNetworth) {
        this.borrowerNetworth = borrowerNetworth;
    }

    public String getBorrowerSalary() {
        return borrowerSalary;
    }

    public void setBorrowerSalary(String borrowerSalary) {
        this.borrowerSalary = borrowerSalary;
    }

    public String getIsSbOD() {
        return isSbOD;
    }

    public void setIsSbOD(String isSbOD) {
        this.isSbOD = isSbOD;
    }    
    
    /**
     * Getter for property memberTypeData.
     * @return Value of property memberTypeData.
     */
    public boolean isMemberTypeData() {
        return memberTypeData;
    }
    
    /**
     * Setter for property memberTypeData.
     * @param memberTypeData New value of property memberTypeData.
     */
    public void setMemberTypeData(boolean memberTypeData) {
        this.memberTypeData = memberTypeData;
    }
    
    /**
     * Getter for property tblMemberTypeDetails.
     * @return Value of property tblMemberTypeDetails.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblMemberTypeDetails() {
        return tblMemberTypeDetails;
    }
    
    /**
     * Setter for property tblMemberTypeDetails.
     * @param tblMemberTypeDetails New value of property tblMemberTypeDetails.
     */
    public void setTblMemberTypeDetails(com.see.truetransact.clientutil.EnhancedTableModel tblMemberTypeDetails) {
        this.tblMemberTypeDetails = tblMemberTypeDetails;
    }
   // End 
            
    
    /** Creates a new instance of AccountsOB */
    public AccountsOB() {
        
        // First load the data for combo boxes
        curDate = ClientUtil.getCurrentDate();
        map = new HashMap();
        map.put(CommonConstants.JNDI, "AccountJNDI");
        map.put(CommonConstants.HOME, "operativeaccount.AccountHome");
        map.put(CommonConstants.REMOTE, "operativeaccount.Account");
        
        try {
            proxy = ProxyFactory.createProxy();
            fillDropdown();
            setJntAcccntTblCol();
            setCardDetailsTile();
            setMemberTableTile(); // nithya
            tblATMCardDetails = new EnhancedTableModel(null, cardTitle);
            tblMemberTypeDetails = new EnhancedTableModel(null, tableMemberTitle); // nithya
        } catch (Exception e) {
            e.printStackTrace();
        }
        tblJointAccnt = new EnhancedTableModel(null, tblJointAccntColTitle);
       
    }
    
    public void setCardDetailsTile() {
        cardTitle.add("SL NO");
        cardTitle.add("ACTION");
        cardTitle.add("ACTION_DT");
        cardTitle.add("AUTH_STATUS");
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
    
    /** To fill the comboboxes */
    private void fillDropdown() throws Exception{
        lookupMap = new HashMap();
        lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
        lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
        lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
        
        final HashMap param = new HashMap();
        
        param.put(CommonConstants.MAP_NAME, "getAccProducts");
        param.put(CommonConstants.PARAMFORQUERY, null);
        HashMap lookupValues = ClientUtil.populateLookupData(param);
        fillData((HashMap)lookupValues.get(CommonConstants.DATA));
        int j=value.size();
        for(int i=1;i<j;i++){
            value.set(i, (String)value.get(i)+" ("+(String)key.get(i)+")");
        }
        cbmProductIdAI = new ComboBoxModel(key,value);
        
        param.put(CommonConstants.MAP_NAME, "getAccProducts");
        param.put(CommonConstants.PARAMFORQUERY, null);
        lookupValues = ClientUtil.populateLookupData(param);
        fillData((HashMap)lookupValues.get(CommonConstants.DATA));
        j = value.size();
        for(int i=1;i<j;i++){
            value.set(i, (String)value.get(i)+" ("+(String)key.get(i)+")");
        }
        cbmLinkingProductId = new ComboBoxModel(key,value);
        
        param.put(CommonConstants.MAP_NAME,null);
        
        final ArrayList lookupKey = new ArrayList();
        lookupKey.add("ACCOUNTSTATUS");
        lookupKey.add("ACT.CONSTITUTION");
        lookupKey.add("ACT_OP_MODE");
        lookupKey.add("CUSTOMER.ADDRTYPE");
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
        param.put(CommonConstants.PARAMFORQUERY, lookupKey);
        
        lookupValues = ClientUtil.populateLookupData(param);
        
        fillData((HashMap)lookupValues.get("ACCOUNTSTATUS"));
        cbmActStatusAI = new ComboBoxModel(key, value);
        
        fillData((HashMap)lookupValues.get("ACT.CONSTITUTION"));
        cbmConstitutionAI = new ComboBoxModel(key, value);
        
        fillData((HashMap)lookupValues.get("ACT_OP_MODE"));
        cbmOpModeAI = new ComboBoxModel(key, value);
        
        fillData((HashMap)lookupValues.get("CUSTOMER.ADDRTYPE"));
        cbmCommAddr = new ComboBoxModel(key, value);
        
        fillData((HashMap)lookupValues.get("CUSTOMER.CUSTOMERGROUP"));
        cbmGroupCodeAI = new ComboBoxModel(key,value);
        
        fillData((HashMap)lookupValues.get("SETTLEMENT_MODE"));
        cbmSettlementModeAI = new ComboBoxModel(key, value);
        
        fillData((HashMap)lookupValues.get("CATEGORY"));
        cbmCategory = new ComboBoxModel(key, value);
        
        fillData((HashMap)lookupValues.get("CURRENCYTYPE"));
        cbmBaseCurrAI = new ComboBoxModel(key,value);
        
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
        
        cbmPrevAcctNo=new ComboBoxModel(new ArrayList(),new ArrayList());
        ArrayList key = new ArrayList();
        ArrayList value = new ArrayList();
        HashMap mapShare = new HashMap();
        mapShare.put("TYPE", "A");//Added By Revathi.L
        List keyValue = null;
        //keyValue = ClientUtil.executeQuery("getAgentID", mapShare);
        keyValue = ClientUtil.executeQuery("getAgentIDNew", mapShare);//Added By Revathi.L
        key.add("");
        value.add("");
        if (keyValue != null && keyValue.size() > 0) {
            for (int i = 0; i < keyValue.size(); i++) {
                mapShare = (HashMap) keyValue.get(i);
                key.add(mapShare.get("AGENT_ID"));
                value.add(mapShare.get("FNAME"));
            }
        }
        cbmagentID = new ComboBoxModel(key,value);
        key = null;
        value = null;
        keyValue.clear();
        keyValue = null;
        mapShare.clear();
        mapShare = null;
        
        //Added by sreekrishnan
        HashMap introMap=new HashMap();
        List introList = ClientUtil.executeQuery("getDepositIntroducers", introMap);
        //System.out.println("#### introList :"+introList);
        key=new ArrayList();
        value=new ArrayList();
        if (introList != null && introList.size() > 0) {
            for(int i=0;i< introList.size();i++){
            introMap = (HashMap) introList.get(i);
            String introKey=CommonUtil.convertObjToStr(introMap.get("KEY"));
            String introValue=CommonUtil.convertObjToStr(introMap.get("VALUE"));
            cbmIntroducer = new ComboBoxModel(key, value); 
            if(i==0)
                cbmIntroducer.addKeyAndElement("", "");
            cbmIntroducer.addKeyAndElement(introKey, introValue);                        
            }
        }
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
            if(data!=null)
                return (String)((HashMap)data.get(0)).get("branchName");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return "";
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
            if(list!=null)
                hash=(HashMap)list.get(0);
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
        }
    }
    
    /** Populate the Customer details
     */
    public void populateScreen(HashMap queryWhereMap, boolean jntAcctNewClicked, PowerOfAttorneyOB objPOAOB) {
        try {
            String strPrevMainCust_ID = getTxtCustomerIdAI();
            queryWhereMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
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
                if(custMapData.get("MINOR")!=null && custMapData.get("MINOR").equals("Y"))
                setLblMajOMinVal("MINOR");
                else
                setLblMajOMinVal("MAJOR");
                
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
        deletedJointDetails.add(strDelRowCount);
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
        txtBranchCodeAI = "";
        txtPrevActNoAI = "";
        dtdActOpenDateAI = "";
        txtAmoutTransAI = "";
        txtRemarksAI = "";
        cboProductIdAI = "";
        txtCustomerIdAI = "";
        txtActName = "";
        txtRemarks = "";
        txtCardActNumber = "";
        txtOpeningAmount = "";
        txtPrevActNumAI = "";
        dtdOpeningDateAI = "";
        cboActStatusAI = "";
        cboAgentId="";
        cboConstitutionAI = "";
        cboOpModeAI = "";
        cboCommAddr = "";
        txtODLimitAI = "";
        cboGroupCodeAI = "";
        cboSettlementModeAI = "";
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
        dtdExpiryDateITP3 = "";
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
        txtMobileNo = "";
        tdtMobileSubscribedFrom = "";
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
        setTxtAgentID("");
        setTxtDealerID("");
        setTxtUPIMobileNo("");
        
        // reset Nominee and PoA fields
        //        resetNomineeOBFields();
        resetPoAOBFields();
        resetSuretyFields(); // Added by nithya
        isSbOD = ""; // Added by nithya on 19.08.2016
        // nominee and PoA list
        nomineeTOList = null;
        cboLinkingProductId = "";
        oldSuretys = null;
        enhanceValidate = false;
        //cbmPrevAcctNo="";
        // reset the account number
        accountNumber = "";
        // reset the Account No
        setAccountNo("");
        this.cbmPrevAcctNo=new ComboBoxModel(new ArrayList(),new ArrayList());
        
        /* its must to call setChanged(), before calling notifyObservers(),
         * because only then the update() method in the UI will be called
         */
        setTxtAtmCardLimit("");
        setTxtLinkingActNum("");
        setCboIntroducer("");        
        resetMemberTypeDetails();// for sb od
        resetBorrowerDetails(); // for sb od
        resetMemberTypeTbl(); // for sb od
        setChkPrimaryAccount("N");
        setChanged();
        notifyObservers();
    }
    
    // Added by nithya
    private void resetSuretyFields(){
        
        setSuretyMemberNo("");
        setSuretyMemberName("");
        setSuretyMemberType("");
        setSuretyMemberContact("");
        setSuretyMemberSalary("");
        setSuretyMemberNetworth("");
        setBorrowerMemberNo("");
        setBorrowerNetworth("");
        setBorrowerAppliedAmnt("");
        setBorrowerSalary("");
        setChkRenew("");
        setIsRenewd("");
        setIsODEnhanced("N");
        setChkODClose("N");
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
    private void populateOB(HashMap mapData, NomineeOB objNomineeOB, PowerOfAttorneyOB objPOAOB, AuthorizedSignatoryOB objAuthSign, AuthorizedSignatoryInstructionOB objAuthSignInst) {
        AccountTO account = null;
        AccountParamTO paramTO = null;
        ArrayList nomineeList = null;
        ArrayList poaList = null;
        
        account = (AccountTO) ((List) mapData.get("AccountTO")).get(0);
        setAccountTO(account);
        
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
        
        if (((List) mapData.get("AccountParamTO")).size() > 0){
            paramTO = (AccountParamTO) ((List) mapData.get("AccountParamTO")).get(0);
            setParamTO(paramTO);
        }
        
        //System.out.println("@@@ mapData:"+mapData);
        objSMSSubscriptionTO = null;
        if (getChkMobileBankingAD() && mapData.containsKey("SMSSubscriptionTO") && ((List) mapData.get("SMSSubscriptionTO")).size() > 0){
            objSMSSubscriptionTO = (SMSSubscriptionTO) ((List) mapData.get("SMSSubscriptionTO")).get(0);
            objSMSSubscriptionTO.setStatus(CommonConstants.STATUS_MODIFIED);
            setSMSSubscriptionTO(objSMSSubscriptionTO);
        }
        
        /* To set the ArrayList in NomineeOB so as to set the data in the Nominee-Table...*/
        nomineeList =  (ArrayList) mapData.get("AccountNomineeList");
        objNomineeOB.setNomimeeList(nomineeList);
        objNomineeOB.setNomineeTabData();
        objNomineeOB.ttNotifyObservers();
        
        objPOAOB.setTermLoanPowerAttorneyTO((ArrayList) (mapData.get("PowerAttorneyTO")), getAccountNo());
        objAuthSign.setAuthorizedSignatoryTO((ArrayList) (mapData.get("AuthorizedSignatoryTO")), getAccountNo());
        objAuthSignInst.setAuthorizedSignatoryInstructionTO((ArrayList) (mapData.get("AuthorizedSignatoryInstructionTO")), getAccountNo());
        
        //Added By Suresh   02-Dec-2015 Refered By Abi
        if (mapData.containsKey("CARD_ACT_DEATILS")) {
            cardDetailsMap = (LinkedHashMap) mapData.get("CARD_ACT_DEATILS");
           // System.out.println("##### cardDetailsMap : "+cardDetailsMap);
            ArrayList addList = new ArrayList(cardDetailsMap.keySet());
            for (int i = 0; i < addList.size(); i++) {
                CardAccountTO objCardAccountTO = (CardAccountTO) cardDetailsMap.get(addList.get(i));
                objCardAccountTO.setStatus(CommonConstants.STATUS_MODIFIED);
                ArrayList singleTabRow = new ArrayList();
                singleTabRow.add(objCardAccountTO.getSlNo());
                singleTabRow.add(CommonUtil.convertObjToStr(objCardAccountTO.getAction()));
                singleTabRow.add(CommonUtil.convertObjToStr(objCardAccountTO.getActionDt()));
                singleTabRow.add(CommonUtil.convertObjToStr(objCardAccountTO.getAuthorizedStatus()));
                tblATMCardDetails.addRow(singleTabRow);
            }
        }
        
        // Added by nithya for populating member details
        //System.out.println("memberListTO mapdata :: " + mapData );
        if(mapData.containsKey("memberListTO")){
                oldSuretys = new ArrayList();
                memberTypeMap = (LinkedHashMap)mapData.get("memberListTO");
                ArrayList addList =new ArrayList(memberTypeMap.keySet());
                for(int i=0;i<addList.size();i++){
                    SbODSecurityTO objMemberTypeTO = (SbODSecurityTO)  memberTypeMap.get(addList.get(i));
                    objMemberTypeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    ArrayList incTabRow = new ArrayList();
                    oldSuretys.add(objMemberTypeTO.getMemberNo());
                    incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberNo()));
                    incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberName()));
                    incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberType()));
                    incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getContactNo()));
                    incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberSalary()));
                    incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberNetworth()));
                    incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getSuretyEligAmt()));
                    tblMemberTypeDetails.addRow(incTabRow);
                    setBorrowerSalary(CommonUtil.convertObjToStr(objMemberTypeTO.getBorrowerSalary()));
                    setBorrowerNetworth(CommonUtil.convertObjToStr(objMemberTypeTO.getBorrowerNetworth()));
                    setBorrowerAppliedAmnt(CommonUtil.convertObjToStr(objMemberTypeTO.getTodSanctioned()));
                    //setChkRenew(objMemberTypeTO.getAcctstatus());
                    if(objMemberTypeTO.getAcctstatus().equalsIgnoreCase("RENEW")){
                       setIsRenewd("Y");
                    }else{
                       setIsRenewd("N"); 
                    }   
                    setBorrowerMemberNo(objMemberTypeTO.getBorrowerMemberNo());
                    setFromDate(DateUtil.getStringDate(objMemberTypeTO.getFromDt()));
                    setToDate(DateUtil.getStringDate(objMemberTypeTO.getToDt()));
                    objMemberTypeTO = null;
                }                
            }
        
        // End
        if (getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
            if (mapData.containsKey("SB_OD_ACT_STATUS") && mapData.get("SB_OD_ACT_STATUS") != null) {
                if (CommonUtil.convertObjToStr(mapData.get("SB_OD_ACT_STATUS")).equalsIgnoreCase("CLOSED")) {
                    setChkODClose("Y");
                }
            }
        }

        /* its must to call setChanged(), before calling notifyObservers(),
         * because only then the update() method in the UI will be called
         */
        //        setChanged();
        //        notifyObservers();
    }
    
    private void setSMSSubscriptionTO(SMSSubscriptionTO objSMSSubscriptionTO) {
        setTxtMobileNo(CommonUtil.convertObjToStr(objSMSSubscriptionTO.getMobileNo()));
        setTdtMobileSubscribedFrom(DateUtil.getStringDate(objSMSSubscriptionTO.getSubscriptionDt()));
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
    
    private void setAccountTO(AccountTO account) {
        
        setCboProductIdAI(CommonUtil.convertObjToStr(account.getProdId()));
        setTxtCustomerIdAI(CommonUtil.convertObjToStr(account.getCustId()));
        //setTxtPrevActNumAI(account.getPrevActNum());
        if ( account.getPrevActNum()!= null && account.getPrevActNum().length()>0 &&
        (this.getOperation()==ClientConstants.ACTIONTYPE_NEWTI ||
        this.getOperation()==ClientConstants.ACTIONTYPE_EDITTI )) {
            getPreviousActDetails(account.getPrevActNum());
        }
        setDtdOpeningDateAI(DateUtil.getStringDate(account.getCreateDt()));
        setCboActStatusAI(CommonUtil.convertObjToStr(account.getActStatusId()));
        setCboConstitutionAI(CommonUtil.convertObjToStr(account.getActCatId()));
        //        setCboConstitutionAI(CommonUtil.convertObjToStr(getCbmConstitutionAI().getDataForKey(CommonUtil.convertObjToStr(account.getActCatId()))));
        setStatusBy(account.getStatusBy());
        setAuthorizeStatus(account.getAuthorizationStatus());
        setCboOpModeAI(CommonUtil.convertObjToStr(account.getOptModeId()));
        setCboCommAddr(CommonUtil.convertObjToStr(account.getCommAddrType()));
        setTxtODLimitAI(CommonUtil.convertObjToStr(account.getTodLimit()));
        setCboGroupCodeAI(CommonUtil.convertObjToStr(account.getGroupCodeId()));
        setCboSettlementModeAI(CommonUtil.convertObjToStr(account.getSettmtModeId()));
        setCboCategory(CommonUtil.convertObjToStr(account.getCategoryId()));
        //        setCboBaseCurrAI(CommonUtil.convertObjToStr(account.getBaseCurr()));
        
        setTxtActName(CommonUtil.convertObjToStr(account.getAcctName()));
        setTxtRemarks(CommonUtil.convertObjToStr(account.getRemarks()));
        if(account.getCardActNo() != null && account.getCardActNo().length()>0){
            setTxtCardActNumber(CommonUtil.convertObjToStr(account.getCardActNo())); //Added By Suresh
            setRdoKYCNormsYes(true);
            setTxtLinkingActNum(CommonUtil.convertObjToStr(account.getLinkingActNum()));
            setCboLinkingProductId(CommonUtil.convertObjToStr(account.getLinkingActNum().substring(4,7)));
            setTxtAtmCardLimit(CommonUtil.convertObjToStr(account.getAtmCardLimitAmt()));
        }else{
            setRdoKYCNormsNo(false);
        }
        setTxtOpeningAmount(CommonUtil.convertObjToStr(account.getOpeningAmount()));
        setClosedDt(CommonUtil.convertObjToStr(account.getClosedDt()));
//        setCboagentId(CommonUtil.convertObjToStr(account.getAgentId()));
//        setCboIntroducer(CommonUtil.convertObjToStr(account.getIntroducer()));
        setTxtAgentID(CommonUtil.convertObjToStr(account.getAgentId()));
        setTxtDealerID(CommonUtil.convertObjToStr(account.getIntroducer()));
        setChkPrimaryAccount(CommonUtil.convertObjToStr(account.getPrimaryAccount()));
        setTxtUPIMobileNo(CommonUtil.convertObjToStr(account.getUpiMobileNo()));
    }
    private void getPreviousActDetails(String preActNo){
        HashMap resultMap = new HashMap();
        try {
            final HashMap preAccountDetailMap = new HashMap();
            //System.out.println("######### PreacNo:"+preActNo);
            preAccountDetailMap.put("ACCOUNTNO",preActNo);
            final List resultList = ClientUtil.executeQuery("getPreviousAccountBranch", preAccountDetailMap);
            resultMap = (HashMap)resultList.get(0);
            setTxtBranchCodeAI((String)resultMap.get("BRANCHCODE"));
            populatePreviousAccounts();
            cbmPrevAcctNo.setSelectedItem(preActNo);
            getPreviousAccountDetails();
        }catch(Exception e){
            System.out.println("Error in getPreviousAccountBranchData");
        }
        
    }
    
    private void setParamTO(AccountParamTO paramTO) {
        
        setChkPayIntOnCrBalIN(CommonUtil.convertObjToStr(paramTO.getIntCrBal()).equalsIgnoreCase(YES) ? true : false);
        setChkPayIntOnDrBalIN(CommonUtil.convertObjToStr(paramTO.getIntDrBal()).equalsIgnoreCase(YES) ? true : false);
        
        setChkChequeBookAD(CommonUtil.convertObjToStr(paramTO.getChkBook()).equalsIgnoreCase(YES) ? true : false);
        setChkNROStatusAD(CommonUtil.convertObjToStr(paramTO.getNroStatus()).equalsIgnoreCase(YES) ? true : false);
        setChkMobileBankingAD(CommonUtil.convertObjToStr(paramTO.getMobileBanking()).equals(YES) ? true : false);
        setChkCustGrpLimitValidationAD(CommonUtil.convertObjToStr(paramTO.getCustgrpLimitValidation()).equals(YES) ? true : false);
        
        // ATM Card...
        setChkATMAD(CommonUtil.convertObjToStr(paramTO.getAtmCard()).equalsIgnoreCase(YES) ? true : false);
        setTxtATMNoAD(CommonUtil.convertObjToStr(paramTO.getAtmCardNo()));
        setDtdATMFromDateAD(DateUtil.getStringDate(paramTO.getAtmCardValidfrom()));
        setDtdATMToDateAD(DateUtil.getStringDate(paramTO.getAtmCardExprdt()));
        // Credit Card...
        setChkCreditAD(CommonUtil.convertObjToStr(paramTO.getCrCard()).equalsIgnoreCase(YES) ? true : false);
        setTxtCreditNoAD(CommonUtil.convertObjToStr(paramTO.getCrCardNo()));
        setDtdCreditFromDateAD(DateUtil.getStringDate(paramTO.getCrCardValidfrom()));
        setDtdCreditToDateAD(DateUtil.getStringDate(paramTO.getCrCardExprdt()));
        // Debit Card...
        setChkDebitAD(CommonUtil.convertObjToStr(paramTO.getDrCard()).equalsIgnoreCase(YES) ? true : false);
        setTxtDebitNoAD(CommonUtil.convertObjToStr(paramTO.getDrCardNo()));
        setDtdDebitFromDateAD(DateUtil.getStringDate(paramTO.getDrCardValidfrom()));
        setDtdDebitToDateAD(DateUtil.getStringDate(paramTO.getDrCardExprdt()));
        
        setChkFlexiAD(CommonUtil.convertObjToStr(paramTO.getFlexi()).equalsIgnoreCase(YES) ? true : false);
        setTxtMinBal1FlexiAD(CommonUtil.convertObjToStr(paramTO.getMinBal1Flexi()));
        setTxtMinBal2FlexiAD(CommonUtil.convertObjToStr(paramTO.getMinBal2Flexi()));
        //        setTxtReqFlexiPeriodAD(CommonUtil.convertObjToStr(paramTO.getReqFlexiPd()));
        //        setCboDMYAD(DAYS);
        
        String periodValue = "";
        int resultValue = CommonUtil.convertObjToInt(paramTO.getReqFlexiPd());
//        if (resultValue >= YEAR_INT) {
//            periodValue = YEAR_STR;
//            resultValue = resultValue/YEAR_INT;
//        } else if (resultValue >= MONTH_INT) {
//            periodValue=MONTH_STR;
//            resultValue = resultValue/MONTH_INT;
//        } else if (resultValue >= DAY_INT) {
//            periodValue=DAY_STR;
//            resultValue = resultValue;
//        } else {
//            periodValue = "";
//            resultValue = 0;
//        }
//        setCboDMYAD(periodValue);
        setTxtReqFlexiPeriodAD(String.valueOf(resultValue));
        
        setCboStmtFreqAD("" + paramTO.getStatFreq().intValue());
        
        setChkStopPmtChrgAD(CommonUtil.convertObjToStr(paramTO.getStopPayChrg()).equalsIgnoreCase(YES) ? true : false);
        setChkChequeRetChrgAD(CommonUtil.convertObjToStr(paramTO.getChkReturn()).equalsIgnoreCase(YES) ? true : false);
        setChkInopChrgAD(CommonUtil.convertObjToStr(paramTO.getInopChrg()).equalsIgnoreCase(YES) ? true : false);
        setChkStmtChrgAD(CommonUtil.convertObjToStr(paramTO.getStatChrg()).equalsIgnoreCase(YES) ? true : false);
        setChkPassBook(CommonUtil.convertObjToStr(paramTO.getPassBook()).equalsIgnoreCase(YES) ? true : false);
        
        
        setTxtAccOpeningChrgAD(CommonUtil.convertObjToStr(paramTO.getActOpenChrg()));
        setTxtAccCloseChrgAD(CommonUtil.convertObjToStr(paramTO.getActClosingChrg()));
        setTxtMisServiceChrgAD(CommonUtil.convertObjToStr(paramTO.getMiscServChrg()));
        setTxtChequeBookChrgAD(CommonUtil.convertObjToStr(paramTO.getChkBookChrg()));
        setTxtFolioChrgAD(CommonUtil.convertObjToStr(paramTO.getFolioChrg()));
        setTxtExcessWithChrgAD(CommonUtil.convertObjToStr(paramTO.getExcessWithdChrg()));
        
        setChkNonMainMinBalChrgAD(CommonUtil.convertObjToStr(paramTO.getNonmainChrg()).equalsIgnoreCase(YES) ? true : false);
        setTxtMinActBalanceAD(CommonUtil.convertObjToStr(paramTO.getMinActBal()));
        
        setChkABBChrgAD(CommonUtil.convertObjToStr(paramTO.getAbb()).equalsIgnoreCase(YES) ? true : false);
        setTxtABBChrgAD(CommonUtil.convertObjToStr(paramTO.getAbbChrg()));
        setChkNPAChrgAD(CommonUtil.convertObjToStr(paramTO.getNpa()).equalsIgnoreCase(YES) ? true : false);
        setDtdNPAChrgAD(DateUtil.getStringDate(paramTO.getNpaDt()));
        
        setDtdDebit(DateUtil.getStringDate(paramTO.getLastDrIntAppldt()));
        setDtdCredit(DateUtil.getStringDate(paramTO.getLastCrIntAppldt()));
        
        setChkHideBalanceTrans(CommonUtil.convertObjToStr(paramTO.getHideBalance()).equalsIgnoreCase(YES) ? true : false);
        setCboRoleHierarchy(CommonUtil.convertObjToStr(paramTO.getShowBalanceTo()));
    }
    
    public ComboBoxModel getCbmProductIdAI(){
        return this.cbmProductIdAI;
    }
    
    public ComboBoxModel getCbmLinkingProductId(){
        return this.cbmLinkingProductId;
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
    
   public void doAuthorize() {
        try {
            map.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
            if(getSingleAuthorizeMap()!= null && getSingleAuthorizeMap().size() > 0){
                HashMap singleAuthMap = getSingleAuthorizeMap();
                //System.out.println("#@$@#$@#$singleAuthMap:"+singleAuthMap);
                HashMap data = new HashMap();               
                data.put("OPERATIVE_AUTHDATA",singleAuthMap);
                HashMap proxyResultMap = proxy.execute(data, map);
                //System.out.println("proxyResultMap : " + proxyResultMap);
                setProxyReturnMap(proxyResultMap);
            }
        } catch (Exception e) {
            ClientUtil.showMessageWindow(e.getMessage());
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            e.printStackTrace();
        }
   }
    /** To perform necessary action */
    public void doAction(NomineeOB objNomineeOB, PowerOfAttorneyOB objPOAOB, AuthorizedSignatoryOB objAuthSign, AuthorizedSignatoryInstructionOB objAuthSignInst) {
        try {
            map.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
            HashMap proxyResultMap = proxy.execute(populateBean(objNomineeOB, objPOAOB, objAuthSign, objAuthSignInst), map);
            //System.out.println("proxyResultMap : " + proxyResultMap);
             setProxyReturnMap(proxyResultMap);
            if (proxyResultMap != null && proxyResultMap.containsKey(CommonConstants.TRANS_ID)) {
                ClientUtil.showMessageWindow(resourceBundle.getString("lblAccountNumber") + ": " + CommonUtil.convertObjToStr(proxyResultMap.get(CommonConstants.TRANS_ID)));
            }
            
            setResult(getOperation());
            operation = ClientConstants.ACTIONTYPE_CANCEL;
        } catch (Exception e) {
            ClientUtil.showMessageWindow(e.getMessage());
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            setProxyReturnMap(null);
            e.printStackTrace();
        }
    }
    
    private HashMap populateBean(NomineeOB objNomineeOB, PowerOfAttorneyOB poaOB, AuthorizedSignatoryOB objAuthSign, AuthorizedSignatoryInstructionOB objAuthSignInst) {
        HashMap account = new HashMap();
        AccountTO acctTO = getAccountTO();       
        account.put("AccountTO", getAccountTO());
        //--- Puts the data if the Constitution is JointAccount
        if(acctTO.getActCatId().equals("JOINT")){
            account.put("JointAccntTO",setJointAccntData());
            jntAcctTOMap = null;
        }
        if(deletedJointDetails.size()>0)
        account.put("deletedJointDetails",deletedJointDetails);
        
        account.put("AccountParamTO", getAccountParamTO());
        
        /**
         * To Send the Nominee Related Data to the Account-DAO...
         */
        account.put("AccountNomineeTO", objNomineeOB.getNomimeeList());
        account.put("AccountNomineeDeleteTO", objNomineeOB.getDeleteNomimeeList());
        
        account.put("PowerAttorneyTO",poaOB.setTermLoanPowerAttorney());
        account.put("AuthorizedSignatoryTO",objAuthSign.setAuthorizedSignatory());
        account.put("AuthorizedSignatoryInstructionTO",objAuthSignInst.setAuthorizedSignatoryInstruction());
        if(getTransactionMap() != null ){
            account.put("Transaction Details Data", getTransactionMap());
        }
        if (objSMSSubscriptionTO!=null) {
            account.put("SMSSubscriptionTO", objSMSSubscriptionTO);
        }
        if(cardDetailsMap!=null && cardDetailsMap.size()>0){
            account.put("CARD_ACT_DEATILS", cardDetailsMap);
        }
        // add code by nithya for security details
        if(memberTypeMap !=null && memberTypeMap.size()>0){
            HashMap suretyFinalMap=new HashMap();
            suretyFinalMap.put("MemberTableDetails",memberTypeMap);
            suretyFinalMap.put("TOTAL_SALARY",getBorrowerSalary());
            suretyFinalMap.put("BORROWER_NETWORTH",getBorrowerNetworth());
            suretyFinalMap.put("ACCT_STATUS",getChkRenew());//16-02-2017
            if(getChkODClose().equalsIgnoreCase("Y")){
                suretyFinalMap.put("ACCT_STATUS","CLOSEOD");
            }
            suretyFinalMap.put("BORROWER_MEMBER_NO",getBorrowerMemberNo());
            suretyFinalMap.put("IS_RENEWED",isRenewd);
            suretyFinalMap.put("TOD_SANCTIONED",getTxtODLimitAI());
            if (getIsODEnhanced().equalsIgnoreCase("Y")) {
                suretyFinalMap.put("OD_ENHANCEMENT","OD_ENHANCEMENT");
            } 
            account.put("MemberTableDetails",suretyFinalMap);
            if(getOperation() == ClientConstants.ACTIONTYPE_EDIT){
              suretyFinalMap.put("FROM_DT",getFromDate());
              suretyFinalMap.put("TO_DT",getToDate());
	    }	
            memberTypeMap = null;
            suretyFinalMap = null;
        }
        if(deletedMemberTypeMap != null && deletedMemberTypeMap.size()>0 ){
            HashMap removedSuretyFinalMap=new HashMap();
            if(getOperation() == ClientConstants.ACTIONTYPE_EDIT){
              removedSuretyFinalMap.put("FROM_DT",getFromDate());
              removedSuretyFinalMap.put("TO_DT",getToDate());
            }	
            removedSuretyFinalMap.put("deletedMemberTypeData",deletedMemberTypeMap);
            account.put("deletedMemberTypeData",removedSuretyFinalMap);
        }
        //System.out.println("Date to DAO - account :: " + account);
        // End
        return account;
    }
    
    /* To set Joint Account data in the Transfer Object*/
    public HashMap setJointAccntData(){
        HashMap singleRecordJntAcct;
        jntAcctTOMap = new LinkedHashMap();
        try{
            AccountJointTO objAccountJointTO;
            int jntAcctSize = jntAcctAll.size();
            for(int i = 0;i<jntAcctSize;i++){
                singleRecordJntAcct = (HashMap)jntAcctAll.get(CommonUtil.convertObjToStr(jntAcctAll.keySet().toArray()[i]));
                objAccountJointTO = new AccountJointTO();
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
                if ( getOperation() == ClientConstants.ACTIONTYPE_EDIT || getOperation() == ClientConstants.ACTIONTYPE_EDITTI) {
                    objAccountJointTO.setActNum(getAccountNumber());
                }
                jntAcctTOMap.put(String.valueOf(i),objAccountJointTO);
                objAccountJointTO = null;
                singleRecordJntAcct = null;
            }
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return jntAcctTOMap;
    }
    
    private AccountTO getAccountTO() {
        AccountTO account = new AccountTO();
        
        if ( getOperation() == ClientConstants.ACTIONTYPE_EDIT || getOperation() == ClientConstants.ACTIONTYPE_EDITTI) {
            account.setActNum(getAccountNumber());
            account.setCommand(CommonConstants.TOSTATUS_UPDATE);
            //            account.setStatus(CommonConstants.STATUS_MODIFIED);
        }
        else if(getOperation() == ClientConstants.ACTIONTYPE_DELETE ) {
            account.setActNum(getAccountNumber());
            account.setCommand(CommonConstants.TOSTATUS_DELETE);
            //            account.setStatus(CommonConstants.STATUS_DELETED);
        }
        else {
            account.setCommand(CommonConstants.TOSTATUS_INSERT);
            //            account.setStatus(CommonConstants.STATUS_CREATED);
        }
        account.setInitiatedBranch(ProxyParameters.BRANCH_ID);
        account.setProdId(getCboProductIdAI());
        account.setCustId(getTxtCustomerIdAI());
        account.setBranchCode(getSelectedBranchID());
        
        Date OpDt = DateUtil.getDateMMDDYYYY(getDtdOpeningDateAI());
        if(OpDt != null){
        Date opDate = (Date) curDate.clone();
        opDate.setDate(OpDt.getDate());
        opDate.setMonth(OpDt.getMonth());
        opDate.setYear(OpDt.getYear());
//        account.setCreateDt(DateUtil.getDateMMDDYYYY(getDtdOpeningDateAI()));
        account.setCreateDt(opDate);
        }else{
            account.setCreateDt(DateUtil.getDateMMDDYYYY(getDtdOpeningDateAI()));
        }
        
        account.setActStatusId(getCboActStatusAI());
        account.setActCatId(getCboConstitutionAI());
        account.setOptModeId(getCboOpModeAI());
        account.setCommAddrType(getCboCommAddr());
        account.setTodLimit(CommonUtil.convertObjToDouble(getTxtODLimitAI()));
        account.setGroupCodeId(getCboGroupCodeAI());
        account.setSettmtModeId(getCboSettlementModeAI());
        account.setCategoryId(getCboCategory());
        account.setPrevActNum((String)this.cbmPrevAcctNo.getKeyForSelected());
        account.setClearBalance(CommonUtil.convertObjToDouble("0.0"));
        account.setUnclearBalance(CommonUtil.convertObjToDouble("0.0"));
        account.setFloatBalance(CommonUtil.convertObjToDouble("0.0"));
        account.setEffectiveBalance(CommonUtil.convertObjToDouble("0.0"));
        if(this.getTxtAmoutTransAI()!=null && this.getTxtAmoutTransAI().length()>0)
            account.setAvailableBalance(CommonUtil.convertObjToDouble(this.getTxtAmoutTransAI()));
        else
            account.setAvailableBalance(CommonUtil.convertObjToDouble("0.0"));
        //        account.setCreatedBy(TrueTransactMain.USER_ID);
        account.setAuthorizationStatus("");
        //        account.setClosedBy("");
        //        account.setClosedDt(new Date());
        account.setBaseCurr(LocaleConstants.DEFAULT_CURRENCY);
        
        //        account.setStatusBy(TrueTransactMain.USER_ID);
        //        account.setStatusDt(curDate);
        account.setAcctName(getTxtActName());
        account.setRemarks(getTxtRemarks());
        account.setCardActNo(getTxtCardActNumber());
        account.setLinkingActNum(getTxtLinkingActNum());
        account.setAtmCardLimitAmt(CommonUtil.convertObjToDouble(getTxtAtmCardLimit()));
        account.setOpeningAmount(CommonUtil.convertObjToDouble(getTxtOpeningAmount()));
//        account.setAgentId(getCboagentId());
//        account.setIntroducer(getCboIntroducer());
        //Added By Revathi.L
        account.setAgentId(getTxtAgentID());
        account.setIntroducer(getTxtDealerID());
        account.setPrimaryAccount(getChkPrimaryAccount());
        account.setUpiMobileNo(getTxtUPIMobileNo());
        return account;
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
    //        intro.setStatusDt(curDate);
    //
    //        return intro;
    //    }
    
    private AccountParamTO getAccountParamTO() {
        AccountParamTO param = new AccountParamTO();
        if ( getOperation() == ClientConstants.ACTIONTYPE_EDIT || getOperation() == ClientConstants.ACTIONTYPE_EDITTI) {
            param.setActNum(getAccountNumber());
        }
        param.setIntCrBal(getChkPayIntOnCrBalIN() ?YES: NO);
        param.setIntDrBal(getChkPayIntOnDrBalIN() ? YES : NO);
        param.setChkBook(getChkChequeBookAD() ? YES : NO);
        param.setNroStatus(getChkNROStatusAD() ? YES : NO);
        param.setMobileBanking(getChkMobileBankingAD() ? YES : NO);
        if (getChkMobileBankingAD()) {
            objSMSSubscriptionTO = new SMSSubscriptionTO();
            objSMSSubscriptionTO.setProdType("OA");
            objSMSSubscriptionTO.setProdId(getCboProductIdAI());
            objSMSSubscriptionTO.setActNum(getAccountNumber());
            objSMSSubscriptionTO.setMobileNo(CommonUtil.convertObjToStr(getTxtMobileNo()));
            Date smsSubscriptionDt = DateUtil.getDateMMDDYYYY(getTdtMobileSubscribedFrom());
            if(smsSubscriptionDt != null){
                Date smsDt = (Date) curDate.clone();
                smsDt.setDate(smsSubscriptionDt.getDate());
                smsDt.setMonth(smsSubscriptionDt.getMonth());
                smsDt.setYear(smsSubscriptionDt.getYear());
                objSMSSubscriptionTO.setSubscriptionDt(smsDt);
            }else{
                objSMSSubscriptionTO.setSubscriptionDt(DateUtil.getDateMMDDYYYY(getTdtMobileSubscribedFrom()));
            }
            if(!CommonUtil.convertObjToStr(objSMSSubscriptionTO.getStatus()).equals(CommonConstants.STATUS_MODIFIED))
            objSMSSubscriptionTO.setStatus(CommonConstants.STATUS_CREATED);
            objSMSSubscriptionTO.setStatusBy(ProxyParameters.USER_ID);
            objSMSSubscriptionTO.setStatusDt(curDate);
        } else if(objSMSSubscriptionTO !=null){
            
           objSMSSubscriptionTO.setStatus(CommonConstants.STATUS_DELETED);
        }else
             objSMSSubscriptionTO = null;
        param.setCustgrpLimitValidation(getChkCustGrpLimitValidationAD() ? YES : NO);
        // ATM Card...
        param.setAtmCard(getChkATMAD() ? YES : NO);
        param.setAtmCardNo(getTxtATMNoAD());
        
        Date AtmDt = DateUtil.getDateMMDDYYYY(getDtdATMFromDateAD());
        if(AtmDt != null){
        Date atmDate = (Date) curDate.clone();
        atmDate.setDate(AtmDt.getDate());
        atmDate.setMonth(AtmDt.getMonth());
        atmDate.setYear(AtmDt.getYear());
//        param.setAtmCardValidfrom(DateUtil.getDateMMDDYYYY(getDtdATMFromDateAD()));
        param.setAtmCardValidfrom(atmDate);
        }else{
            param.setAtmCardValidfrom(DateUtil.getDateMMDDYYYY(getDtdATMFromDateAD()));
        }
        
        
        Date AtmToDt = DateUtil.getDateMMDDYYYY(getDtdATMToDateAD());
        if(AtmToDt != null){
        Date atmtoDate = (Date) curDate.clone();
        atmtoDate.setDate(AtmToDt.getDate());
        atmtoDate.setMonth(AtmToDt.getMonth());
        atmtoDate.setYear(AtmToDt.getYear());
//        param.setAtmCardExprdt(DateUtil.getDateMMDDYYYY(getDtdATMToDateAD()));
        param.setAtmCardExprdt(atmtoDate);
        }else{
             param.setAtmCardExprdt(DateUtil.getDateMMDDYYYY(getDtdATMToDateAD()));
        }
        // Credit Card...
        param.setCrCard(getChkCreditAD() ? YES : NO);
        param.setCrCardNo(getTxtCreditNoAD());
        
        Date CrToDt = DateUtil.getDateMMDDYYYY(getDtdCreditFromDateAD());
        if(CrToDt != null){
        Date crDate = (Date) curDate.clone();
        crDate.setDate(CrToDt.getDate());
        crDate.setMonth(CrToDt.getMonth());
        crDate.setYear(CrToDt.getYear());
//        param.setCrCardValidfrom(DateUtil.getDateMMDDYYYY(getDtdCreditFromDateAD()));
        param.setCrCardValidfrom(crDate);
        }else{
            param.setCrCardValidfrom(DateUtil.getDateMMDDYYYY(getDtdCreditFromDateAD())); 
        }
        
        Date CrToAdDt = DateUtil.getDateMMDDYYYY(getDtdCreditToDateAD());
        if(CrToAdDt != null){
        Date cradDate = (Date) curDate.clone();
        cradDate.setDate(CrToAdDt.getDate());
        cradDate.setMonth(CrToAdDt.getMonth());
        cradDate.setYear(CrToAdDt.getYear());
//        param.setCrCardExprdt(DateUtil.getDateMMDDYYYY(getDtdCreditToDateAD()));
        param.setCrCardExprdt(cradDate);
        }else{
             param.setCrCardExprdt(DateUtil.getDateMMDDYYYY(getDtdCreditToDateAD()));
        }
        
        // Debit Card...
        param.setDrCard(getChkDebitAD() ? YES : NO);
        param.setDrCardNo(getTxtDebitNoAD());
        
        Date DbAdDt = DateUtil.getDateMMDDYYYY(getDtdDebitFromDateAD());
        if(DbAdDt != null){
        Date dbadDate = (Date) curDate.clone();
        dbadDate.setDate(DbAdDt.getDate());
        dbadDate.setMonth(DbAdDt.getMonth());
        dbadDate.setYear(DbAdDt.getYear());
//        param.setDrCardValidfrom(DateUtil.getDateMMDDYYYY(getDtdDebitFromDateAD()));
        param.setDrCardValidfrom(dbadDate);
        }else{
            param.setDrCardValidfrom(DateUtil.getDateMMDDYYYY(getDtdDebitFromDateAD()));
        }
        
        Date DbToAdDt = DateUtil.getDateMMDDYYYY(getDtdDebitToDateAD());
        if(DbToAdDt != null){
        Date dbtoadDate = (Date) curDate.clone();
        dbtoadDate.setDate(DbToAdDt.getDate());
        dbtoadDate.setMonth(DbToAdDt.getMonth());
        dbtoadDate.setYear(DbToAdDt.getYear());
//        param.setDrCardExprdt(DateUtil.getDateMMDDYYYY(getDtdDebitToDateAD()));
        param.setDrCardExprdt(dbtoadDate);
        }else{
            param.setDrCardExprdt(DateUtil.getDateMMDDYYYY(getDtdDebitToDateAD()));
        }
        
        param.setFlexi(getChkFlexiAD() ? YES : NO);
        param.setMinBal1Flexi(CommonUtil.convertObjToDouble(getTxtMinBal1FlexiAD()));
        param.setMinBal2Flexi(CommonUtil.convertObjToDouble(getTxtMinBal2FlexiAD()));
        // we should have all these kind of values in CAPITAL_LETTER
        // eventually all the dates are converted into days
//        if (getCboDMYAD().equals(DAYS) && getTxtReqFlexiPeriodAD().length()>0 && getTxtReqFlexiPeriodAD()!=null) {
//            param.setReqFlexiPd(new Double(CommonUtil.convertObjToDouble(getTxtReqFlexiPeriodAD()).doubleValue() * 1));
//        } else if (getCboDMYAD().equals("MONTHS")) {
//            param.setReqFlexiPd(new Double(CommonUtil.convertObjToDouble(getTxtReqFlexiPeriodAD()).doubleValue() * 30));
//        } else if (getCboDMYAD().equals("YEARS")) {
            param.setReqFlexiPd(new Double(CommonUtil.convertObjToDouble(getTxtReqFlexiPeriodAD()).doubleValue()));
//        }
        
        param.setStatFreq(CommonUtil.convertObjToDouble(getCboStmtFreqAD()));
        param.setStopPayChrg(getChkStopPmtChrgAD() ? YES : NO);
        param.setChkReturn(getChkChequeRetChrgAD() ? YES : NO);
        param.setInopChrg(getChkInopChrgAD() ? YES : NO);
        param.setStatChrg(getChkStmtChrgAD() ? YES : NO);
        param.setPassBook(isChkPassBook() ? YES : NO);
        
        
        param.setActOpenChrg(CommonUtil.convertObjToDouble(getTxtAccOpeningChrgAD()));
        param.setActClosingChrg(CommonUtil.convertObjToDouble(getTxtAccCloseChrgAD()));
        param.setMiscServChrg(CommonUtil.convertObjToDouble(getTxtMisServiceChrgAD()));
        param.setChkBookChrg(CommonUtil.convertObjToDouble(getTxtChequeBookChrgAD()));
        param.setFolioChrg(CommonUtil.convertObjToDouble(getTxtFolioChrgAD()));
        param.setExcessWithdChrg(CommonUtil.convertObjToDouble(getTxtExcessWithChrgAD()));
        
        param.setNonmainChrg(getChkNonMainMinBalChrgAD() ? YES : NO);
        param.setMinActBal(CommonUtil.convertObjToDouble(getTxtMinActBalanceAD()));
        param.setAbb(getChkABBChrgAD() ? YES : NO);
        param.setAbbChrg(CommonUtil.convertObjToDouble(getTxtABBChrgAD()));
        param.setNpa(getChkNPAChrgAD() ? YES : NO);
        
        Date NpaAdDt = DateUtil.getDateMMDDYYYY(getDtdNPAChrgAD());
        if(NpaAdDt != null){
        Date npaDate = (Date) curDate.clone();
        npaDate.setDate(NpaAdDt.getDate());
        npaDate.setMonth(NpaAdDt.getMonth());
        npaDate.setYear(NpaAdDt.getYear());
//        param.setNpaDt(DateUtil.getDateMMDDYYYY(getDtdNPAChrgAD()));
        param.setNpaDt(npaDate);
        }else{
            param.setNpaDt(DateUtil.getDateMMDDYYYY(getDtdNPAChrgAD()));
        }
        
        Date DtdDt = DateUtil.getDateMMDDYYYY(getDtdDebit());
        if(DtdDt != null){
        Date dtdDate = (Date) curDate.clone();
        dtdDate.setDate(DtdDt.getDate());
        dtdDate.setMonth(DtdDt.getMonth());
        dtdDate.setYear(DtdDt.getYear());
//        param.setLastDrIntAppldt(DateUtil.getDateMMDDYYYY(getDtdDebit()));
        param.setLastDrIntAppldt(dtdDate);
        }else{
            param.setLastDrIntAppldt(DateUtil.getDateMMDDYYYY(getDtdDebit()));
        }
        
        Date DtdCrDt = DateUtil.getDateMMDDYYYY(getDtdCredit());
        if(DtdCrDt != null){
        Date dtdcrDate = (Date) curDate.clone();
        dtdcrDate.setDate(DtdCrDt.getDate());
        dtdcrDate.setMonth(DtdCrDt.getMonth());
        dtdcrDate.setYear(DtdCrDt.getYear());
//        param.setLastCrIntAppldt(DateUtil.getDateMMDDYYYY(getDtdCredit()));
        param.setLastCrIntAppldt(dtdcrDate);
        }else{
            param.setLastCrIntAppldt(DateUtil.getDateMMDDYYYY(getDtdCredit()));
        }
        
        param.setHideBalance(getChkHideBalanceTrans() ? YES : NO);
        param.setShowBalanceTo(getCboRoleHierarchy());
        
        //        param.setStatus(this.getTOStatus());
        //        param.setStatusBy(TrueTransactMain.USER_ID);
        //        param.setStatusDt(curDate);
        
        return param;
    }
    
    void setTabLength(double tabLength){
        this.tabLenght = tabLenght;
    }
    
    double getTabLength(){
        return this.tabLenght;
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
    

    void setTxtODLimitAI(String txtODLimitAI){
        this.txtODLimitAI = txtODLimitAI;
        setChanged();
    }
    String getTxtODLimitAI(){
        return this.txtODLimitAI;
    }
    
    void setCboGroupCodeAI(String cboGroupCodeAI){
        this.cboGroupCodeAI = cboGroupCodeAI;
        setChanged();
    }
    String getCboGroupCodeAI(){
        return this.cboGroupCodeAI;
    }
    
    void setCboSettlementModeAI(String cboSettlementModeAI){
        this.cboSettlementModeAI = cboSettlementModeAI;
        setChanged();
    }
    String getCboSettlementModeAI(){
        return this.cboSettlementModeAI;
    }
    
    void setCboCategory(String cboCategory){
        this.cboCategory = cboCategory;
        setChanged();
    }
    String getCboCategory(){
        return this.cboCategory;
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
    
    void setDtdExpiryDateITP3(String dtdExpiryDateITP3){
        this.dtdExpiryDateITP3 = dtdExpiryDateITP3;
        setChanged();
    }
    String getDtdExpiryDateITP3(){
        return this.dtdExpiryDateITP3;
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
        jntAcctAll = null;
    }
    
    public void resetCardTbl(){
        resetCardTableDetails();
        tblATMCardDetails.setDataArrayList(null, cardTitle);
        cardDetailsMap = null;
    }
    
    // Added by nithya  tableMemberTitle
    public void resetMemberTypeTbl(){
       // tblMemberTypeDetails.setDataArrayList(null, tableMemberTitle);   
        tblMemberTypeDetails = new EnhancedTableModel(null, tableMemberTitle); 
        memberTypeMap = null;
        deletedMemberTypeMap = null;        
        oldSuretys = null;
        enhanceValidate = false;
    }
    // End
    
    private String getTOStatus(){
        if(this.getOperation()==ClientConstants.ACTIONTYPE_EDIT || this.getOperation()==ClientConstants.ACTIONTYPE_EDITTI)
            return CommonConstants.STATUS_MODIFIED;
        if(this.getOperation()==ClientConstants.ACTIONTYPE_DELETE)
            return CommonConstants.STATUS_DELETED;
        return CommonConstants.STATUS_CREATED;
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
    
    
    //Added By Suresh   01-Dec-2015 Refered By Abi
    public void addDataToCardDetailsTable(int rowSelected, boolean updateMode) {
        try {
            int rowSel = rowSelected;
            final CardAccountTO cardAcctTO = new CardAccountTO();
            if (cardDetailsMap == null) {
                cardDetailsMap = new LinkedHashMap();
            }
            int slno = 0;
            int nums[] = new int[50];
            int max = nums[0];
            if (!updateMode) {
                ArrayList data = tblATMCardDetails.getDataArrayList();
                slno = serialNo(data);
            } else {
                if (isNewData()) {
                    ArrayList data = tblATMCardDetails.getDataArrayList();
                    slno = serialNo(data);
                } else {
                    int b = CommonUtil.convertObjToInt(tblATMCardDetails.getValueAt(rowSelected, 0));
                    slno = b;
                }
            }
            cardAcctTO.setCardActNum(getLblATMCardNoVal());
            cardAcctTO.setAction(getActionType());
            cardAcctTO.setActionDt(DateUtil.getDateMMDDYYYY(getActionDt()));
            cardAcctTO.setRemarks(getTxtCardRemarks());
            cardAcctTO.setSlNo(String.valueOf(slno));
            cardDetailsMap.put(slno, cardAcctTO);
            String sno = String.valueOf(slno);
            updateCardTable(rowSel, sno,cardAcctTO);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void updateCardTable(int rowSel, String sno,CardAccountTO cardAcctTO) throws Exception {
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append        
        for (int i = tblATMCardDetails.getRowCount(), j = 0; i > 0; i--, j++) {
            selectedRow = ((ArrayList) tblATMCardDetails.getDataArrayList().get(j)).get(0);
            if (sno.equals(CommonUtil.convertObjToStr(selectedRow))) {
                rowExists = true;
                ArrayList cardDetailsRow = new ArrayList();
                ArrayList data = tblATMCardDetails.getDataArrayList();
                data.remove(rowSel);
                cardDetailsRow.add(sno);
                cardDetailsRow.add(CommonUtil.convertObjToStr(getActionType()));
                cardDetailsRow.add(CommonUtil.convertObjToStr(getActionDt()));
                cardDetailsRow.add("");
                tblATMCardDetails.insertRow(rowSel, cardDetailsRow);
                cardDetailsRow = null;
            }
        }
        if (!rowExists) {
            ArrayList cardDetailsRow = new ArrayList();
            cardDetailsRow.add(sno);
            cardDetailsRow.add(CommonUtil.convertObjToStr(getActionType()));
            cardDetailsRow.add(CommonUtil.convertObjToStr(getActionDt()));
            cardDetailsRow.add("");
            tblATMCardDetails.insertRow(tblATMCardDetails.getRowCount(), cardDetailsRow);
            cardDetailsRow = null;
        }
    }

    public int serialNo(ArrayList data) {
        final int dataSize = data.size();
        int nums[] = new int[50];
        int max = nums[0];
        int slno = 0;
        int a = 0;
        slno = dataSize + 1;
        for (int i = 0; i < data.size(); i++) {
            a = CommonUtil.convertObjToInt(tblATMCardDetails.getValueAt(i, 0));
            nums[i] = a;
            if (nums[i] > max) {
                max = nums[i];
            }
            slno = max + 1;
        }
        return slno;
    }
    
    public void populateCardTableDetails(int row) {
        try {
            resetCardTableDetails();
            final CardAccountTO cardAcctTO = (CardAccountTO) cardDetailsMap.get(row);
            populateCardTableData(cardAcctTO);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void populateCardTableData(CardAccountTO cardAcctTO) throws Exception {
        setActionType(CommonUtil.convertObjToStr(cardAcctTO.getAction()));
        setActionDt(CommonUtil.convertObjToStr(cardAcctTO.getActionDt()));
        setTxtCardRemarks(CommonUtil.convertObjToStr(cardAcctTO.getRemarks()));
    }
    public void deleteCardTableData(int val, int row) {
        CardAccountTO cardAcctTO = (CardAccountTO) cardDetailsMap.get(val);
        Object obj;
        obj = val;
        cardDetailsMap.remove(val);
        resetCardTableValues();
        try {
            populateCardTable();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void populateCardTable() throws Exception {
        ArrayList DataList = new ArrayList();
        DataList = new ArrayList(cardDetailsMap.keySet());
        ArrayList addList = new ArrayList(cardDetailsMap.keySet());
        int length = DataList.size();
        for (int i = 0; i < length; i++) {
            ArrayList singleRow = new ArrayList();
            CardAccountTO cardAcctTO = (CardAccountTO) cardDetailsMap.get(addList.get(i));
            singleRow.add(cardAcctTO.getSlNo());
            singleRow.add(CommonUtil.convertObjToStr(cardAcctTO.getAction()));
            singleRow.add(CommonUtil.convertObjToStr(getActionDt()));
            singleRow.add(CommonUtil.convertObjToStr(cardAcctTO.getAuthorizedStatus()));
            tblATMCardDetails.addRow(singleRow);
        }
    }
    
    public void resetCardTableDetails(){
        setActionType("");;
        setActionDt("");
        setTxtCardRemarks("");
    }
    
    public void resetCardTableValues() {
        tblATMCardDetails.setDataArrayList(null, cardTitle);
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
        //System.out.println("getCustAddrType Called");
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
            if(resultMap.get("MINOR")!=null && resultMap.get("MINOR").equals("Y"))
            setLblMajOMinVal("MINOR");
            else
            setLblMajOMinVal("MAJOR");
                
        }
        return resultMap;
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
     * Getter for property transactionMap.
     * @return Value of property transactionMap.
     */
    public java.util.HashMap getTransactionMap() {
        return transactionMap;
    }
    
    /**
     * Setter for property transactionMap.
     * @param transactionMap New value of property transactionMap.
     */
    public void setTransactionMap(java.util.HashMap transactionMap) {
        this.transactionMap = transactionMap;
    }
    
    /**
     * Getter for property singleAuthorizeMap.
     * @return Value of property singleAuthorizeMap.
     */
    public java.util.HashMap getSingleAuthorizeMap() {
        return singleAuthorizeMap;
    }
    
    /**
     * Setter for property singleAuthorizeMap.
     * @param singleAuthorizeMap New value of property singleAuthorizeMap.
     */
    public void setSingleAuthorizeMap(java.util.HashMap singleAuthorizeMap) {
        this.singleAuthorizeMap = singleAuthorizeMap;
    }
    
    /**
     * Getter for property txtMobileNo.
     * @return Value of property txtMobileNo.
     */
    public java.lang.String getTxtMobileNo() {
        return txtMobileNo;
    }
    
    /**
     * Setter for property txtMobileNo.
     * @param txtMobileNo New value of property txtMobileNo.
     */
    public void setTxtMobileNo(java.lang.String txtMobileNo) {
        this.txtMobileNo = txtMobileNo;
    }
    
    /**
     * Getter for property tdtMobileSubscribedFrom.
     * @return Value of property tdtMobileSubscribedFrom.
     */
    public java.lang.String getTdtMobileSubscribedFrom() {
        return tdtMobileSubscribedFrom;
    }
    
    /**
     * Setter for property tdtMobileSubscribedFrom.
     * @param tdtMobileSubscribedFrom New value of property tdtMobileSubscribedFrom.
     */
    public void setTdtMobileSubscribedFrom(java.lang.String tdtMobileSubscribedFrom) {
        this.tdtMobileSubscribedFrom = tdtMobileSubscribedFrom;
    }
    
    /**
     * Getter for property txtOpeningAmount.
     * @return Value of property txtOpeningAmount.
     */
    public java.lang.String getTxtOpeningAmount() {
        return txtOpeningAmount;
    }
    
    /**
     * Setter for property txtOpeningAmount.
     * @param txtOpeningAmount New value of property txtOpeningAmount.
     */
    public void setTxtOpeningAmount(java.lang.String txtOpeningAmount) {
        this.txtOpeningAmount = txtOpeningAmount;
    }

    public String getTxtCardActNumber() {
        return txtCardActNumber;
    }

    public void setTxtCardActNumber(String txtCardActNumber) {
        this.txtCardActNumber = txtCardActNumber;
    }
    public boolean isNewData() {
        return newData;
    }

    public void setNewData(boolean newData) {
        this.newData = newData;
    }

    public EnhancedTableModel getTblATMCardDetails() {
        return tblATMCardDetails;
    }

    public void setTblATMCardDetails(EnhancedTableModel tblATMCardDetails) {
        this.tblATMCardDetails = tblATMCardDetails;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getActionDt() {
        return actionDt;
    }

    public void setActionDt(String actionDt) {
        this.actionDt = actionDt;
    }

    public String getLblATMCardNoVal() {
        return lblATMCardNoVal;
    }

    public void setLblATMCardNoVal(String lblATMCardNoVal) {
        this.lblATMCardNoVal = lblATMCardNoVal;
    }

    public String getTxtCardRemarks() {
        return txtCardRemarks;
    }

    public void setTxtCardRemarks(String txtCardRemarks) {
        this.txtCardRemarks = txtCardRemarks;
    }
    
     public ComboBoxModel getCbmIntroducer() {
        return cbmIntroducer;
    }

    public void setCbmIntroducer(ComboBoxModel cbmIntroducer) {
        this.cbmIntroducer = cbmIntroducer;
    }

    public String getCboIntroducer() {
        return cboIntroducer;
    }

    public void setCboIntroducer(String cboIntroducer) {
        this.cboIntroducer = cboIntroducer;
    }
    
    public String getTxtAgentID() {
        return txtAgentID;
    }

    public void setTxtAgentID(String txtAgentID) {
        this.txtAgentID = txtAgentID;
    }

    public String getTxtDealerID() {
        return txtDealerID;
    }

    public void setTxtDealerID(String txtDealerID) {
        this.txtDealerID = txtDealerID;
    }
    
    
    
    // Added by nithya
    
    public void addMemberTypeTable(int rowSelected, boolean updateMode){
        try{
            int rowSel=rowSelected;            
            SbODSecurityTO ObjSbODSecurityTO =  new SbODSecurityTO();
            if( memberTypeMap == null ){
                memberTypeMap = new LinkedHashMap();
            }         
            
            if(getOperation() == ClientConstants.ACTIONTYPE_EDIT){
                if(isMemberTypeData()){
                      ObjSbODSecurityTO.setStatusDt(curDate);
                      ObjSbODSecurityTO.setStatusBy(TrueTransactMain.USER_ID);
                      ObjSbODSecurityTO.setStatus(CommonConstants.STATUS_CREATED);
                }else{
                      ObjSbODSecurityTO.setStatusDt(curDate);
                      ObjSbODSecurityTO.setStatusBy(TrueTransactMain.USER_ID);
                      ObjSbODSecurityTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }                
            }else{
                  ObjSbODSecurityTO.setStatusDt(curDate);
                  ObjSbODSecurityTO.setStatusBy(TrueTransactMain.USER_ID);
                  ObjSbODSecurityTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            //ObjSbODSecurityTO.setAcctNum(getAccountNumber());
            ObjSbODSecurityTO.setMemberNo(getSuretyMemberNo());
            ObjSbODSecurityTO.setMemberName(getSuretyMemberName());
            ObjSbODSecurityTO.setMemberType(getSuretyMemberType());
            if(getSuretyMemberContact().length()== 0){
                ObjSbODSecurityTO.setContactNo(null);
            }else{
               ObjSbODSecurityTO.setContactNo(CommonUtil.convertObjToLong(getSuretyMemberContact()));
            }
            ObjSbODSecurityTO.setMemberSalary(CommonUtil.convertObjToDouble(getSuretyMemberSalary()));
            ObjSbODSecurityTO.setMemberNetworth(getSuretyMemberNetworth());           
            ObjSbODSecurityTO.setBranchCode(ProxyParameters.BRANCH_ID);
            ObjSbODSecurityTO.setSuretyEligAmt(CommonUtil.convertObjToDouble(getSuretyEligAmnt()));
//            ObjSbODSecurityTO.setBorrowerSalary(CommonUtil.convertObjToDouble(getBorrowerSalary()));
//            ObjSbODSecurityTO.setBorrowerNetworth(getBorrowerNetworth());
          //  ObjSbODSecurityTO.setAcctstatus(getChkRenew());
//            ObjSbODSecurityTO.setFromDt(curDate);
//            ObjSbODSecurityTO.setToDt(curDate);
//             Call for getting due date 
//            Date dueDt = null;
//            HashMap dueDateMap = new HashMap();
//            dueDateMap.put("MEMBER_NO",getBorrowerMemberNo());
//            String dte = new SimpleDateFormat("dd-MM-yyyy").format(curDate);
//            System.out.println("Parsed date dte:" + dte);
//            
            // Testing started
//            String strDate = dte;
//            SimpleDateFormat sdFormat = new SimpleDateFormat("dd-MM-yyyy");
//            Date dtnew = sdFormat.parse(dte);
//            System.out.println("Parsed date dtnew:" + sdFormat.format(dtnew));
            // End of testing                      
            
//            Date dt= ClientUtil.getCurrentDate();
//            dueDateMap.put("FRM_DT",sdFormat.format(dtnew));
//            List lst = ClientUtil.executeQuery("getSBODDueDt",dueDateMap);
//            System.out.println("getSBODDueDt :: " + lst);
//            if(lst != null && lst.size() > 0){
//                HashMap dueMap = (HashMap)lst.get(0);
//                if(dueMap.containsKey("DUE_DT")){
//                    dueDt = getProperDateFormat(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(dueMap.get("DUE_DT"))));
//                }               
//            }
            //ObjSbODSecurityTO.setToDt(dueDt);
            memberTypeMap.put(ObjSbODSecurityTO.getMemberNo(),ObjSbODSecurityTO);
            
            
            updateMemberTypeDetails(rowSel,updateMode);            
            //System.out.println("ObjSbODSecurityTO :: " + ObjSbODSecurityTO + "\n memberTypeMap :: " + memberTypeMap);
            //notifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public Date getProperDateFormat(Object obj) {
        Date curr_Dt = null;
        if (obj!=null && obj.toString().length()>0) {
            Date tempDt= DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            curr_Dt=(Date)curDate.clone();
            curr_Dt.setDate(tempDt.getDate());
            curr_Dt.setMonth(tempDt.getMonth());
            curr_Dt.setYear(tempDt.getYear());
        }
        return curr_Dt;
    }
    
    
    public void resetMemberTypeDetails() {
        setSuretyMemberNo("");
        setSuretyMemberName("");
        setSuretyMemberType("");
        setSuretyMemberContact("");
        setSuretyMemberSalary("");
        setSuretyMemberNetworth(""); 
    }
    
    public void resetBorrowerDetails(){
        setBorrowerMemberNo("");
        setBorrowerSalary("");
        setBorrowerNetworth("");   
        setBorrowerAppliedAmnt("");
    }
    
    private void updateMemberTypeDetails(int rowSel , boolean updateMode)  throws Exception{
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for(int i = tblMemberTypeDetails.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblMemberTypeDetails.getDataArrayList().get(j)).get(0);
            if(getSuretyMemberNo().equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
                ArrayList IncParRow = new ArrayList();
                ArrayList data = tblMemberTypeDetails.getDataArrayList();
                data.remove(rowSel);
                IncParRow.add(getSuretyMemberNo());
                IncParRow.add(getSuretyMemberName());
                IncParRow.add(getSuretyMemberType());
                IncParRow.add(getSuretyMemberContact());
                IncParRow.add(getSuretyMemberSalary());
                IncParRow.add(getSuretyMemberNetworth());
                IncParRow.add(getSuretyEligAmnt());
                tblMemberTypeDetails.insertRow(rowSel,IncParRow);
                IncParRow = null;
            }
        }
        if(!rowExists){
            ArrayList IncParRow = new ArrayList();
                IncParRow.add(getSuretyMemberNo());
                IncParRow.add(getSuretyMemberName());
                IncParRow.add(getSuretyMemberType());
                IncParRow.add(getSuretyMemberContact());
                IncParRow.add(getSuretyMemberSalary());
                IncParRow.add(getSuretyMemberNetworth());
                IncParRow.add(getSuretyEligAmnt());
            tblMemberTypeDetails.insertRow(tblMemberTypeDetails.getRowCount(),IncParRow);
            IncParRow = null;
        }
    }
    
    private void setMemberTableTile() throws Exception{
        tableMemberTitle.add("Member No");
        tableMemberTitle.add("Name");
        tableMemberTitle.add("Member Type");
        tableMemberTitle.add("Contact No");
        tableMemberTitle.add("Salary");
        tableMemberTitle.add("Networth");
        tableMemberTitle.add("Elig Amt");
        IncVal = new ArrayList();
    }
    
    
    private void populateMemberTableData(SbODSecurityTO objMemberTypeTO)  throws Exception{
        setSuretyMemberNo(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberNo()));
        setSuretyMemberName(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberName()));
        setSuretyMemberType(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberType()));
        setSuretyMemberContact(CommonUtil.convertObjToStr(objMemberTypeTO.getContactNo()));
        setSuretyMemberSalary(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberSalary()));
        setSuretyMemberNetworth(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberNetworth()));
        setSuretyEligAmnt(CommonUtil.convertObjToStr(objMemberTypeTO.getSuretyEligAmt()));        
    }
    
    public void populateMemberTypeDetails(String row){
        try{
            resetMemberTypeDetails();
            final SbODSecurityTO objMemberTypeTO = (SbODSecurityTO)memberTypeMap.get(row);
            populateMemberTableData(objMemberTypeTO);
            
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
     public void deleteMemberTableData(String val, int row){
        if(deletedMemberTypeMap == null){
            deletedMemberTypeMap = new LinkedHashMap();
        }
        SbODSecurityTO objMemberTypeTO = (SbODSecurityTO) memberTypeMap.get(val);
        objMemberTypeTO.setStatus(CommonConstants.STATUS_DELETED);
        deletedMemberTypeMap.put(CommonUtil.convertObjToStr(tblMemberTypeDetails.getValueAt(row,0)),memberTypeMap.get(val));
        Object obj;
        obj=val;
        memberTypeMap.remove(val);
        tblMemberTypeDetails.setDataArrayList(null,tableMemberTitle);
        try{
            populateMemberTable();
        }
        catch(Exception e){
            parseException.logException(e,true);
        }
    }
    private void populateMemberTable()  throws Exception{
        ArrayList incDataList = new ArrayList();
        incDataList = new ArrayList(memberTypeMap.keySet());
        ArrayList addList =new ArrayList(memberTypeMap.keySet());
        int length = incDataList.size();
        for(int i=0; i<length; i++){
            ArrayList incTabRow = new ArrayList();
            SbODSecurityTO objMemberTypeTO = (SbODSecurityTO) memberTypeMap.get(addList.get(i));
            IncVal.add(objMemberTypeTO);
            if(!objMemberTypeTO.getStatus().equals("DELETED")){
                incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberType()));
                incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberName()));
                incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getContactNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberSalary()));
                incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberNetworth()));
                incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getSuretyEligAmt()));
                tblMemberTypeDetails.addRow(incTabRow);
            }
        }
    }    
    
    public long getMobileNo(String custId) {
        long mobileNo = 0;
        HashMap mobileMap = new HashMap();
        mobileMap.put("CUST_ID",custId);
        mobileMap.put("BRANCH_CODE",ProxyParameters.BRANCH_ID);
        List list = ClientUtil.executeQuery("getSMSContactForDepositMaturedCustomer", mobileMap);
        if (list != null && list.size() > 0) {
            mobileMap = (HashMap)list.get(0);
            mobileNo = CommonUtil.convertObjToLong(mobileMap.get("CONTACT_NO"));
        }
        return mobileNo;
    }

    public ArrayList getOldSuretys() {
        return oldSuretys;
    }

    public void setOldSuretys(ArrayList oldSuretys) {
        this.oldSuretys = oldSuretys;
    }

    public boolean isEnhanceValidate() {
        return enhanceValidate;
    }

    public void setEnhanceValidate(boolean enhanceValidate) {
        this.enhanceValidate = enhanceValidate;
    }

    public String getChkPrimaryAccount() {
        return chkPrimaryAccount;
    }

    public void setChkPrimaryAccount(String chkPrimaryAccount) {
        this.chkPrimaryAccount = chkPrimaryAccount;
    }

    public String getTxtUPIMobileNo() {
        return txtUPIMobileNo;
    }

    public void setTxtUPIMobileNo(String txtUPIMobileNo) {
        this.txtUPIMobileNo = txtUPIMobileNo;
    }
    
    
    
}
