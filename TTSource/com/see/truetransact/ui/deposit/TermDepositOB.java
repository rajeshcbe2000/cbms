/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermDepositOB.java
 *
 * Created on January 6, 2004, 11:44 AM
 */
package com.see.truetransact.ui.deposit;

/**
 *
 * @author K.R.Jayakrishnan
 */
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.InterestCalc;
import com.see.truetransact.ui.common.authorizedsignatory.AuthorizedSignatoryOB;
import com.see.truetransact.ui.common.authorizedsignatory.AuthorizedSignatoryInstructionOB;
import com.see.truetransact.ui.common.powerofattorney.PowerOfAttorneyOB;
import com.see.truetransact.ui.common.nominee.NomineeOB;
import com.see.truetransact.transferobject.deposit.AccInfoTO;
import com.see.truetransact.transferobject.deposit.TransferInTO;
import com.see.truetransact.transferobject.deposit.DepSubNoAccInfoTO;
import com.see.truetransact.transferobject.deposit.JointAccntTO;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.JointAcctHolderManipulation;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.ClientConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Date;
import java.util.List;
import java.util.GregorianCalendar;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.ui.common.transaction.TransactionOB;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.deposit.*;
import com.see.truetransact.transferobject.product.deposits.DepositsThriftBenevolentTO;
import java.util.*;
import com.see.truetransact.transferobject.common.mobile.SMSSubscriptionTO;
import java.util.logging.Level;
import java.util.logging.Logger;

//import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
public class  TermDepositOB extends CObservable {

    private TransactionOB transactionOB;
    JointAcctHolderManipulation objJointAcctHolderManipulation = new JointAcctHolderManipulation();
    TableManipulation objTableManipulation = new TableManipulation();
    private ArrayList nomineeTOList;
    AuthorizedSignatoryOB authorizedSignatoryOB = null;
    AuthorizedSignatoryInstructionOB authorizedSignatoryInstructionOB = null;
    PowerOfAttorneyOB powerOfAttorneyOB = null;
    NomineeOB nomineeOB = new NomineeOB();
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private int actionType;
    private int result;
    private ProxyFactory proxy;
    private HashMap operationMap;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private ComboBoxModel cbmNomDetStatus;
    private ComboBoxModel cbmOtherCity;
    private ComboBoxModel cbmOtherCountry;
    private ComboBoxModel cbmOtherState;
    private ComboBoxModel cbmIdType;
    private ComboBoxModel cbmDocType;
    private ComboBoxModel cbmInterestPaymentMode;
    private ComboBoxModel cbmInterestPaymentFrequency;
    private ComboBoxModel cbmSettlementMode;
    private ComboBoxModel cbmCommunicationAddress;
    private ComboBoxModel cbmConstitution;
    private ComboBoxModel cbmCategory;
    private ComboBoxModel cbmProductId;
    private ComboBoxModel cbmOnBehalfOf;
    private ComboBoxModel cbmDepositNo;
    private ComboBoxModel cbmNomDetRelationship;
    private ComboBoxModel cbmInstallmentAmount;
    private ComboBoxModel cbmPaymentType;
    private ComboBoxModel cbmNominatedBy;
    private ComboBoxModel cbmMemberType;
    private ComboBoxModel cbmCalenderFreqDay;
    private ComboBoxModel cbmProdType;
    private String prodType;
    private ComboBoxModel cbmProdId;
    private String cboProdId = "";
    private String cboProdType = "";
    private String customerIdCr = "";
    private String productTypes = "";
    private String cboCalenderFreq;
    private String cboAddressType;
    private String txtMdsGroup = "";
    private String txaMdsRemarks = "";
    private double adtAmt = 0.0; 
    private ComboBoxModel cbmRenewalCalenderFreqDay;
    private ComboBoxModel cbmRenewalDepTransMode;
    private ComboBoxModel cbmRenewalInterestTransMode;
    private ComboBoxModel cbmRenewalInterestPaymentMode;
    private ComboBoxModel cbmRenewalInterestPaymentFrequency;
    private ComboBoxModel cbmRenewalDepTransProdType;
    private ComboBoxModel cbmRenewalDepTransProdId;
    private ComboBoxModel cbmRenewalInterestTransProdType;
    private ComboBoxModel cbmRenewalInterestTransProdId;
    private ComboBoxModel cbmRenewalProdType;
    private ComboBoxModel cbmRenewalProdId, cbmAddressType;
    private String cboRenewalDepTransMode = "";
    private String cboRenewalInterestTransMode = "";
    private String cboRenewalInterestPaymentMode = "";
    private String cboRenewalInterestPaymentFrequency = "";
    private String cboRenewalDepTransProdType = "";
    private String cboRenewalInterestTransProdType = "";
    private String cboRenewalProdType = "";
    private String cboRenewalDepTransProdId = "";
    private String cboRenewalInterestTransProdId = "";
    private String cboRenewalProdId = "";
    private String txtRenewalDepTransAmtValue = "";
    private String txtRenewalDepTransTokenNo = "";
    private String txtRenewalIntAmtValue = "";
    private String txtRenewalIntTokenNoVal = "";
    private String txtRenewalPrintedOption = "";
    private String cboRenewalCalenderFreqDay;
    private ComboBoxModel cbmExtensionCalenderFreqDay;
    private String lblExtensionTotalRepayAmtValue = "";
    private String txtExtensionIntAmtValue = "";
    private String txtExtensionTransAmtValue = "";
    private String txtExtensionTransTokenNo = "";
    private String txtExtensionTransAccNo = "";
    private String txtExtensionPaymentAccNo = "";
    private ComboBoxModel cbmExtensionTransMode;
    private ComboBoxModel cbmExtensionTransProdType;
    private ComboBoxModel cbmExtensionTransProdId;
    private ComboBoxModel cbmExtensionPaymentMode;
    private ComboBoxModel cbmExtensionPaymentProdType;
    private ComboBoxModel cbmExtensionPaymentProdId;
    private ComboBoxModel cbmExtensionProdType;
    private ComboBoxModel cbmExtensionProdId;
    private ComboBoxModel cbmExtensionPaymentFrequency;
    private String cboExtensionTransMode = "";
    private String cboExtensionTransProdType = "";
    private String cboExtensionTransProdId = "";
    private String cboExtensionPaymentMode = "";
    private String cboExtensionPaymentProdType = "";
    private String cboExtensionPaymentProdId = "";
    private String cboExtensionProdType = "";
    private String cboExtensionProdId = "";
    private String cboExtensionPaymentFrequency = "";
    private String txtExtensionPrintedOption = "";
    private String extensionAlreadyWithdrawn = "";
    private String extensionAlreadyCredited = "";
    private String extensionTotalIntAmt = "";
    private String cboExtensionCalenderFreqDay;
    private String valExtensionDep = "";
    private String extensionOriginalIntValue = "";
    private String extensionBalanceIntValue = "";
    public String productBehavesLike = new String();
    public String productInterestType = new String();
    private Date nextInterestApplDate = null;
    public boolean isTransactionDone = false;
    public boolean transferInSelect;
    private final String SCREEN = "TD";
    //    private String panel;
    private final int YES = 0;
    private final int NO = 1;
    private final int CANCEL = 2;
    private Double renewalCount;
    private String prodId = "";
    private String category = "";
    private String referenceNo="";
    //--- To check whether the Inter.WarnMsg is already there in the queue
    boolean enteredMsgIntWarn = false;
    public String intWarnMsg = new String();
    public final String HALFYEARLY = "Half Yearly";
    public final String MONTHLY = "Monthly";
    public final String YEARLY = "Yearly";
    public final String QUARTERLY = "Quarterly";
    public final String DATEOFMATURITY = "Date of Maturity";
    public final String YES_STR = "Y";
    public final String NO_STR = "N";
    private final String NEW = "New";
    private final String RENEW = "Renewal";
    private final String EXISTING = "Existing";
    private final String NORMAL = "Normal";
    private final String TRANSFER_IN = "TransferIn";
    private final String RENEWAL = "Renewal";
    private final String EXTENSION = "Extension";
    public final String DEPOSIT_DT = "Deposit Date";
    public final String END_INSTALLMENT = "End of installment frequency period";
    public final String CHOSEN_DT = "Chosen Date during the frequency period";
    private final String DIALOG_YES = "dialogYes";
    private final String DIALOG_OK = "dialogOk";
    private final String DIALOG_NO = "dialogNo";
    private final String WARN_MESSAGE = "warningMessage";
    private final String WARN_MESSAGE_OK = "warningMessageOK";
    //--- Declarations for TableModel's Column Name
    private final String TBL_DEP_SUB_NO_COLUMN_1 = "tblDepSubNoColumn1";
    private final String TBL_DEP_SUB_NO_COLUMN_2 = "tblDepSubNoColumn2";
    private final String TBL_DEP_SUB_NO_COLUMN_3 = "tblDepSubNoColumn3";
    private final String TBL_DEP_SUB_NO_COLUMN_4 = "tblDepSubNoColumn4";
    private final String TBL_DEP_SUB_NO_RENEW_COLUMN_1 = "tblDepSubNoRenewColumn1";
    private final String TBL_DEP_SUB_NO_RENEW_COLUMN_2 = "tblDepSubNoRenewColumn2";
    private final String TBL_DEP_SUB_NO_RENEW_COLUMN_3 = "tblDepSubNoRenewColumn3";
    private final String TBL_DEP_SUB_NO_RENEW_COLUMN_4 = "tblDepSubNoRenewColumn4";
    private final String TBL_JNT_ACCNT_COLUMN_1 = "tblJntAccntColumn1";
    private final String TBL_JNT_ACCNT_COLUMN_2 = "tblJntAccntColumn2";
    private final String TBL_JNT_ACCNT_COLUMN_3 = "tblJntAccntColumn3";
    private final String TBL_JNT_ACCNT_COLUMN_4 = "tblJntAccntColumn4";
    private final String TBL_DEP_LIEN_COLUMN_1 = "Lien No";//LIEN DETAILS
    private final String TBL_DEP_LIEN_COLUMN_2 = "Lien Amount";
    private final String TBL_DEP_LIEN_COLUMN_3 = "Lien Date";
    private final String TBL_DEP_LIEN_COLUMN_4 = "Loan No";
    private final String TBL_DEP_LIEN_COLUMN_5 = "Status";
    //--- Declarations for operationMap
    private final String TERM_DEP_JNDI = "TermDepositJNDI";
    private final String TERM_DEP_HOME = "deposit.TermDepositHome";
    private final String TERM_DEP_REMOTE = "deposit.TermDeposit";
    //--- Declarations for ComboboxModels
    private final String NOMINEE_STATUS = "MAJOR_MINOR";
    private final String CUSTOMER_COUNTRY = "CUSTOMER.COUNTRY";
    private final String CUSTOMER_CITY = "CUSTOMER.CITY";
    private final String CUSTOMER_STATE = "CUSTOMER.STATE";
    private final String PAY_MODE = "REMITTANCE_PAYMENT.PAY_MODE";
    private final String PAY_FREQUENCY = "DEPOSITSPRODUCT.DEPOSITPERIOD";
    //    private final String PAY_FREQUENCY = "LOANPRODUCT.LOANPERIODS";
    private final String SETTLEMENT_MODE = "SETTLEMENT_MODE";
    private final String COMM_ADD = "DEPOSIT.COMM_ADD";
    private final String CONSTITUTION = "CONSTITUTION";
    private final String CATEGORY = "CATEGORY";
    private final String PROD_ID = "PROD_ID";
    private final String GUARDIAN_TYPE = "GUARDIAN_TYPE";
    private final String DEP_GET_PROD_ID = "deposit_getProdId";
    private final String OPER_PROD_ID = "getOperativeProdId";
    private final String RELATIONSHIP = "RELATIONSHIP";
    private final String DEPOSITINSTALLAMT = "DEPOSIT.INSTALL_AMT";
    private final String DEPOSITPAYMENTDAY = "DEPOSIT.PAYMENT_DAY";
    private final String MEMBER_TYPE = "SHARE_TYPE";
    private static String mdsGroup = null;
    private static String mdsRemarks = null;
    public String getReferenceNo() {
        return referenceNo;
    }
    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }
    //--- Declaration for Error Command
    private final String TO_COMMAND_ERROR = "TOCommandError";
    //--- Declarations for TO's going to DAO
    private final String TERM_DEPOSIT_FOR_DAO = "TERMDEPOSIT";
    private final String DEP_SUB_NO_FOR_DAO = "DepSubNoAccInfoTO";
    private final String TRANSFER_IN_DAO = "TransferInTO";
    private final String JOINT_ACCNT_FOR_DAO = "JointAccntTO";
    private final String AUTH_SIGN_DAO = "AuthorizedSignatoryTO";
    private final String AUTH_SIGN_INST_DAO = "AuthorizedSignatoryInstructionTO";
    private final String POA_FOR_DAO = "PoATO";
    private final String NOM_FOR_DAO = "NomTO";
    //--- Serial No Declaration
    private final String FLD_SL_NO = "SlNo";
    //--- Declarations for Deposit Sub No HashMap
    private final String FLD_DEP_SUB_NO_DEP_AMT = "DepAmt";
    private final String FLD_DEP_SUB_NO_DEP_DT = "DepDate";
    private final String FLD_DEP_SUB_NO_DEP_PER_DD = "DepPeriodDd";
    private final String FLD_DEP_SUB_NO_DEP_PER_MM = "DepPeriodMm";
    private final String FLD_DEP_SUB_NO_DEP_PER_YY = "DepPeriodYy";
    private final String FLD_DEP_SUB_NO_DEP_PER_Wk = "DepPeriodWk";
    private final String FLD_DEP_SUB_NO_DEP_SUB_NO = "DepSubNo";
    private final String FLD_DEP_SUB_NO_INT_PAY_FREQ = "IntPayFreq";
    private final String FLD_DEP_SUB_NO_INT_PAY_MODE = "IntPayMode";
    private final String FLD_DEP_SUB_NO_MDS_GROUP = "MdsGroup";
    private final String FLD_ADT_AMT = "adtAmt";
    private final String FLD_DEP_SUB_INST_TYPE = "InstallmentType";
    private final String FLD_DEP_SUB_PAY_TYPE = "PaymentType";
    private final String FLD_DEP_SUB_PAY_DATE = "PaymentDate";
    private final String FLD_DEP_SUB_INT_PROD_TYPE = "ProdType";
    private final String FLD_DEP_SUB_INT_PROD_ID = "ProdId";
    private final String FLD_DEP_SUB_INT_AC_NO = "AccountNo";
    private final String FLD_DEP_SUB_CUST_NAME = "CustomerName";
    private final String FLD_DEP_SUB_CALENDER_FREQ = "CalenderFreq";
    private final String FLD_DEP_SUB_CALENDER_DATE = "CalenderDate";
    private final String FLD_DEP_SUB_CALENDER_DAY = "CalenderDay";
    //    private final String FLD_DEP_SUB_FLEXI_STATUS = "flexiStatus";
    //    private final String FLD_DEP_SUB_PRINTING_NO = "Printing_No";
    private final String FLD_DEP_SUB_NO_MAT_AMT = "MaturityAmt";
    private final String FLD_DEP_SUB_NO_MAT_DT = "MatDate";
    private final String FLD_DEP_SUB_NO_PER_INT_AMT = "PeriodIntAmt";
    private final String FLD_DEP_SUB_NO_RATE_OF_INT = "RateOfInt";
    private final String FLD_DEP_SUB_NO_STATUS = "Status";
    private final String FLD_DEP_SUB_NO_TOT_INT_AMT = "TotIntAmt";
    private final String FLD_DEP_SUB_NO_TOT_INSTALLMENTS = "TotInstallments";
    //--- Declaration for DB Yes Or No
    private final String FLD_FOR_DB_YES_NO = "DBYesOrNo";
    private final String YES_FULL_STR = "Yes";
    private final String NO_FULL_STR = "No";
    //--- Declaration for Status
    private final String FLD_STATUS = "Status";
    final String prodBehavesLikeRecurr = "RECURRING";
    final String prodBehavesLikeDaily = "DAILY";
    final String prodBehavesLikeThrift = "THRIFT";
    final String prodBehavesLikeFixed = "FIXED";
    final String prodBehavesLikeCummulative = "CUMMULATIVE";
    final String prodBehavesLikeFloating = "FLOATING_RATE";
    final String prodInterestType = "SIMPLE";
    final String prodBehavesBenevolent = "BENEVOLENT"; // Added by nithya on 08-03.2016 for 0003920
    //Renewal deposit details...
    private final String FLD_DEP_RENEWAL_SUB_NO_DEP_NO = "RenewDepNo";
    private final String FLD_DEP_RENEWAL_SUB_NO_DEP_AMT = "RenewDepAmt";
    private final String FLD_DEP_RENEWAL_SUB_NO_DEP_DT = "RenewDepDate";
    private final String FLD_DEP_RENEWAL_SUB_NO_DEP_PER_DD = "RenewDepPeriodDd";
    private final String FLD_DEP_RENEWAL_SUB_NO_DEP_PER_MM = "RenewDepPeriodMm";
    private final String FLD_DEP_RENEWAL_SUB_NO_DEP_PER_YY = "RenewDepPeriodYy";
    private final String FLD_DEP_RENEWAL_SUB_NO_DEP_SUB_NO = "RenewDepSubNo";
    private final String FLD_DEP_RENEWAL_SUB_NO_INT_PAY_FREQ = "RenewIntPayFreq";
    private final String FLD_DEP_RENEWAL_SUB_DEP_PAY_MODE = "RenewDepPayMode";
    private final String FLD_DEP_RENEWAL_SUB_DEP_ACCT_NUM = "RenewDepAcctNum";
    private final String FLD_DEP_RENEWAL_SUB_DEP_PROD_TYPE = "RenewDepProdType";
    private final String FLD_DEP_RENEWAL_SUB_DEP_PROD_ID = "RenewDepProdId";
    private final String FLD_DEP_RENEWAL_SUB_DEP_AC_NO = "RenewDepAccountNo";
    private final String FLD_DEP_RENEWAL_SUB_DEP_CUST_NAME = "RenewCustomerName";
    private final String FLD_DEP_RENEWAL_SUB_PAY_MODE = "RenewPayMode";
    private final String FLD_DEP_RENEWAL_SUB_PROD_TYPE = "RenewProdType";
    private final String FLD_DEP_RENEWAL_SUB_PROD_ID = "RenewProdId";
    private final String FLD_DEP_RENEWAL_SUB_AC_NO = "RenewAccountNo";
    private final String FLD_DEP_RENEWAL_SUB_CUST_NAME = "RenewCustomerName";
    private final String FLD_DEP_RENEWAL_SUB_INT_PAY_MODE = "RenewIntPayMode";
    private final String FLD_DEP_RENEWAL_SUB_INT_PROD_TYPE = "RenewIntProdType";
    private final String FLD_DEP_RENEWAL_SUB_INT_PROD_ID = "RenewIntProdId";
    private final String FLD_DEP_RENEWAL_SUB_INT_ACCT_NUM = "RenewIntAcctNum";
    private final String FLD_DEP_RENEWAL_SUB_INT_CUST_NAME = "RenewCustomerName";
    private final String FLD_DEP_RENEWAL_SUB_CALENDER_FREQ = "RenewCalenderFreq";
    private final String FLD_DEP_RENEWAL_SUB_CALENDER_DATE = "RenewCalenderDate";
    private final String FLD_DEP_RENEWAL_SUB_CALENDER_DAY = "RenewCalenderDay";
    private final String FLD_DEP_RENEWAL_SUB_NO_MAT_AMT = "RenewMaturityAmt";
    private final String FLD_DEP_RENEWAL_SUB_NO_MAT_DT = "RenewMatDate";
    private final String FLD_DEP_RENEWAL_SUB_NO_PER_INT_AMT = "RenewPeriodIntAmt";
    private final String FLD_DEP_RENEWAL_SUB_NO_RATE_OF_INT = "RenewRateOfInt";
    private final String FLD_DEP_RENEWAL_SUB_NO_STATUS = "RenewStatus";
    private final String FLD_DEP_RENEWAL_SUB_NO_TOT_INT_AMT = "RenewTotIntAmt";
    private final String FLD_DEP_RENEWAL_SUB_BALANCE_INTAMT = "RenewBalanceIntAmt";
    private final String FLD_DEP_RENEWAL_SUB_SB_INTAMT = "RenewSBIntAmt";
    private final String FLD_DEP_RENEWAL_SUB_SB_PERIOD = "RenewSBPeriod";
    private final String FLD_DEP_RENEWAL_SUB_WITHDRAWING_DEPAMT = "RenewWithDrawDepAmt";
    private final String FLD_DEP_RENEWAL_SUB_ADDING_DEPAMT = "RenewAddingDepAmt";
    private final String FLD_DEP_RENEWAL_SUB_WITHDRAWING_INTAMT = "RenewWithDrawIntAmt";
    private final String FLD_DEP_RENEWAL_SUB_WITHDRAWING_DEPAMT_TOKENO = "RenewWithDrawDepTokenNo";
    private final String FLD_DEP_RENEWAL_SUB_WITHDRAWING_INTAMT_TOKENO = "RenewWithDrawIntTokenNo";
    private final String FLD_DEP_RENEWAL_SUB_PRODUCT_ID = "RenewProductId";
    private final String FLD_DEP_RENEWAL_SUB_CATEGORY = "RenewCategory";
    private final String FLD_DEP_RENEWAL_SUB_PRINTINGNO = "RenewPrintingNo";
    //extension deposit details...
    private final String FLD_DEP_EXTENSION_SUB_NO_DEP_AMT = "ExtensionDepAmt";
    private final String FLD_DEP_EXTENSION_SUB_NO_DEP_DT = "ExtensionDepDate";
    private final String FLD_DEP_EXTENSION_SUB_NO_DEP_PER_DD = "ExtensionDepPeriodDd";
    private final String FLD_DEP_EXTENSION_SUB_NO_DEP_PER_MM = "ExtensionDepPeriodMm";
    private final String FLD_DEP_EXTENSION_SUB_NO_DEP_PER_YY = "ExtensionDepPeriodYy";
    private final String FLD_DEP_EXTENSION_SUB_NO_DEP_SUB_NO = "ExtensionDepSubNo";
    private final String FLD_DEP_EXTENSION_SUB_NO_INT_PAY_FREQ = "ExtensionIntPayFreq";
    private final String FLD_DEP_EXTENSION_SUB_CALENDER_FREQ = "ExtensionCalenderFreq";
    private final String FLD_DEP_EXTENSION_SUB_CALENDER_DATE = "ExtensionCalenderDate";
    private final String FLD_DEP_EXTENSION_SUB_CALENDER_DAY = "ExtensionCalenderDay";
    private final String FLD_DEP_EXTENSION_SUB_NO_MAT_AMT = "ExtensionMaturityAmt";
    private final String FLD_DEP_EXTENSION_SUB_NO_MAT_DT = "ExtensionMatDate";
    private final String FLD_DEP_EXTENSION_SUB_NO_PER_INT_AMT = "ExtensionPeriodIntAmt";
    private final String FLD_DEP_EXTENSION_SUB_NO_RATE_OF_INT = "ExtensionRateOfInt";
    private final String FLD_DEP_EXTENSION_SUB_NO_STATUS = "ExtensionStatus";
    private final String FLD_DEP_EXTENSION_SUB_NO_TOT_INT_AMT = "ExtensionTotIntAmt";
    private final String FLD_DEP_EXTENSION_SUB_BALANCE_INTAMT = "ExtensionBalanceIntAmt";
    private final String FLD_DEP_EXTENSION_SUB_WITHDRAW_AMT_CALC = "ExtensionWithdrawIntAmt";
    private final String FLD_DEP_EXTENSION_SUB_BALANCE_AMT_CALC = "ExtensionBalIntAmt";
    private final String FLD_DEP_EXTENSION_SUB_WITHDRAWING_DEPAMT = "ExtensionWithDrawDepAmt";
    private final String FLD_DEP_EXTENSION_SUB_ADDING_DEPAMT = "ExtensionAddingDepAmt";
    private final String FLD_DEP_EXTENSION_SUB_WITHDRAWING_INTAMT = "ExtensionWithDrawIntAmt";
    private final String FLD_DEP_EXTENSION_SUB_WITHDRAWING_DEPAMT_TOKENO = "ExtensionWithDrawDepTokenNo";
    private final String FLD_DEP_EXTENSION_SUB_WITHDRAWING_INTAMT_TOKENO = "ExtensionWithDrawIntTokenNo";
    private final String FLD_DEP_EXTENSION_SUB_PRODUCT_ID = "ExtensionProductId";
    private final String FLD_DEP_EXTENSION_SUB_CATEGORY = "ExtensionCategory";
    private final String FLD_DEP_EXTENSION_SUB_PRINTINGNO = "ExtensionCategory";
    private final String FLD_DEP_EXTENSION_SUB_DEP_PAY_MODE = "ExtensionDepPayMode";
    private final String FLD_DEP_EXTENSION_SUB_DEP_ACCT_NUM = "ExtensionDepAcctNum";
    private final String FLD_DEP_EXTENSION_SUB_DEP_PROD_TYPE = "ExtensionDepProdType";
    private final String FLD_DEP_EXTENSION_SUB_DEP_PROD_ID = "ExtensionDepProdId";
    private final String FLD_DEP_EXTENSION_SUB_DEP_AC_NO = "ExtensionDepAccountNo";
    private final String FLD_DEP_EXTENSION_SUB_DEP_CUST_NAME = "ExtensionCustomerName";
    private final String FLD_DEP_EXTENSION_SUB_PAY_MODE = "ExtensionPayMode";
    private final String FLD_DEP_EXTENSION_SUB_PROD_TYPE = "ExtensionProdType";
    private final String FLD_DEP_EXTENSION_SUB_PROD_ID = "ExtensionProdId";
    private final String FLD_DEP_EXTENSION_SUB_AC_NO = "ExtensionAccountNo";
    private final String FLD_DEP_EXTENSION_SUB_CUST_NAME = "ExtensionCustomerName";
    //    private final String FLD_DEP_SUB_FLEXI_STATUS = "flexiStatus";
    //    private final String FLD_DEP_SUB_PRINTING_NO = "Printing_No";
    //--- Declaration for DB Yes Or No
    //    private final String FLD_FOR_DB_YES_NO = "DBYesOrNo";
    //    private final String YES_FULL_STR = "Yes";
    //    private final String NO_FULL_STR = "No";
    //    //--- Declaration for Status
    //    private final String FLD_STATUS  = "Status";
    //--- To set and get the deposit Amount from DepSubNo.
    double depAmt = 0;
    double matAmount = 0;
    double outStandingBal = 0;
    //--- To set and get the maturity Dt from DepSubNo. in Renewal mode
    //    String strMatDt;
    String startDate;
    int backDateFreq = 0;
    String txtAmount;
    double balIntAmt = 0.0;
    double drawn = 0.0;
    double credited = 0.0;
    String txtDepDate;
    String txtMatDate;
    Date strDepDt = null;
    Date strMatDt = null;
    private double sbIntAmount = 0.0;
    private double sbPeriodRun = 0.0;
    private boolean setAmt = false;
    private String amtReduce = "";
    String depDt = "";
    String matDt = "";
    //    private String transferBranchCode = null;
    //this details filled with the present Position Details tab....
    //--- TO Object Declarations and HashMap Declarations for doActionPerform
    HashMap data;
    TransferInTO objTransferInTO;
    AccInfoTO objAccInfoTO;
    private String transMode = null;
    private String transactionId = null;
    private String cashId = null;
    private String viewTypeDeposit = null;
    //--- Declarations for Joint Account Table
    public LinkedHashMap jntAcctAll;
    HashMap jntAcctSingleRec;
    private EnhancedTableModel tblJointAccnt;
    private ArrayList tblJointAccntColTitle;
    private ArrayList jntAccntRow;
    LinkedHashMap jntAcctTOMap;
    public boolean withDrawFlag = false;
    public boolean addingFlag = false;
    private boolean closeFlag;
    //--- End of Declarations for Joint Account Table
    //--- Declarations for DepSubNoAccInfo Table
    public int depSubNoMode; // For "New Record"   ----> mode = 0
    // For "Modification" ----> mode = 1
    private int depSubNoRowDel;
    private EnhancedTableModel tblDepSubNoAccInfo;
    private EnhancedTableModel tblRenewalDepSubNoAccInfo;
    private EnhancedTableModel tblExtensionDepSubNoAccInfo;
    private EnhancedTableModel tblLienDetails;//deposit lien details
    private EnhancedTableModel tblOldDepDet;
    private ArrayList depSubNoRow;
    private ArrayList renewalDepSubNoRow;
    private ArrayList extensionDepSubNoRow;
    private ArrayList depLienRow;
    private ArrayList oldDepAccDet;
    private ArrayList tblDepSubNoColTitle;
    private ArrayList tblRenewalDepSubNoColTitle;
    private ArrayList tblExtensionDepSubNoColTitle;
    private ArrayList tblLienColTitle;
    private ArrayList tblOldDepColTitle;
    private HashMap depSubNoRec;
    private HashMap renewaldepSubNoRec;
    private HashMap extensiondepSubNoRec;
    public int depSubNo;
    private int ModifyDepSubNo;
    HashMap depSubNoAll;
    HashMap renewaldepSubNoAll;
    HashMap extensiondepSubNoAll;
    HashMap depSubNoCheckValues;
    HashMap depSubNoCheckRenewalValues;
    HashMap depSubNoCheckExtensionValues;
    LinkedHashMap depSubNoTOMap;
    String depSubNoStatus;
    int depSubNoK;
    int depSubNoRowCount; // Gets the row count of the duplicate record from tblDepSubNoAccInfo
    int getSelectedRowCount; // gets the RowCount of the selected record
    Date currDt = null;
    int depSubNoCount;
    int renewaldepSubNoCount;
    public boolean extension = false;
    private HashMap transactionMap;
    private boolean salaryRecoveryYes = false;
    private boolean salaryRecoveryNo = false;
    private boolean unLock = false;
    private String lockStatus = "";
    private HashMap interestRateMap = null;
    private String postageRenewAmt = "";
    private String postageAmt = "";
    private LinkedHashMap allowedTransactionDetailsTO = null;
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    private LinkedHashMap transactionDetailsTO;
    private List tranListforAuth = null;
    private String renewalCountWithScreen = "FALSE";
    private ComboBoxModel cbmIntroducer;
    private String cboIntroducer = "";
    private String txtExistingAccNo = "";
    
    //--- End of Declarations for DepSubNoAccInfo Table
    
    // Added by nithya on 08-03.2016 for 0003920
    private String additionalAmount = "";
    private String effectiveDate = "";
    private TableModel tbmThriftBenevolent;
    private ArrayList additionalAmountRecord;
    List objThriftBenevolentAdditionalAmtTOList;
    SMSSubscriptionTO objSMSSubscriptionTO = null;
    private boolean chkMobileBanking = false;
    //Added By Revathi.L
    private String txtDealer = "";
    private static String depositGroup = ""; // Added by nithya on 26/09/2017 for group deposit
    private static String depositGroupRemarks = "";// Added by nithya on 26/09/2017 for group deposit
    private String isGroupDeposit = ""; // Added by nithya on 26/09/2017 for group deposit
    // Added by nithya on 13-09-2017 for 0007656: Need yearly installment remittance option in rd type deposit
    private String isYearlyRD = "N"; 
    StringBuffer strBBehavesLike = new StringBuffer();

    public String getIsYearlyRD() {
        return isYearlyRD;
    }

    public void setIsYearlyRD(String isYearlyRD) {
        this.isYearlyRD = isYearlyRD;
    }

    // End
    
    public boolean getChkMobileBanking() {
        return chkMobileBanking;
    }

    public void setChkMobileBanking(boolean chkMobileBanking) {
        this.chkMobileBanking = chkMobileBanking;
    }

    public String getTdtMobileSubscribedFrom() {
        return tdtMobileSubscribedFrom;
    }

    public void setTdtMobileSubscribedFrom(String tdtMobileSubscribedFrom) {
        this.tdtMobileSubscribedFrom = tdtMobileSubscribedFrom;
    }

    public String getTxtMobileNo() {
        return txtMobileNo;
    }

    public void setTxtMobileNo(String txtMobileNo) {
        this.txtMobileNo = txtMobileNo;
    }
    private String txtMobileNo = "";    
    private String tdtMobileSubscribedFrom = "";
    public String getAdditionalAmount() {
        return additionalAmount;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setAdditionalAmount(String additionalAmount) {
        this.additionalAmount = additionalAmount;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public ArrayList getAdditionalAmountRecord() {
        return additionalAmountRecord;
    }

    public TableModel getTbmThriftBenevolent() {
        return tbmThriftBenevolent;
    }

    public void setAdditionalAmountRecord(ArrayList additionalAmountRecord) {
        this.additionalAmountRecord = additionalAmountRecord;
    }

    public void setTbmThriftBenevolent(TableModel tbmThriftBenevolent) {
        this.tbmThriftBenevolent = tbmThriftBenevolent;
    }
    
    // End
    
    //    TermDepositRB objTermDepositRB = new TermDepositRB();
    java.util.ResourceBundle objTermDepositRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.deposit.TermDepositRB", ProxyParameters.LANGUAGE);
    CommonRB objCommRB = new CommonRB();
    //    private static TermDepositOB termDepositOB; // singleton object

    //    static {
    //        try {
    //
    //            termDepositOB = new TermDepositOB();
    //
    //        } catch(Exception e) {
    //            parseException.logException(e,true);
    //        }
    //    }
    public static TermDepositOB getInstance() throws Exception {
        TermDepositOB termDepositOB = new TermDepositOB();
        return termDepositOB;
    }

    /**
     * Creates a new instance of TermDepositOB
     */
    private TermDepositOB() throws Exception {
        proxy = ProxyFactory.createProxy();
        currDt = ClientUtil.getCurrentDate();
        setOperationMap();
        fillDropdown();
        setDepSubNoTblCol();
        setJntAcccntTblCol();
        setTblLienDetails();
        setRenewalDepSubNoTblCol();
        setExtensionDepSubNoTblCol();
        setTblOldDepDet();
        setThriftBenevolentTable(); // Added by nithya on 08-03.2016 for 0003920
        additionalAmountRecord = new ArrayList(); // Added by nithya on 08-03.2016 for 0003920
        tblDepSubNoAccInfo = new EnhancedTableModel(null, tblDepSubNoColTitle);
        tblRenewalDepSubNoAccInfo = new EnhancedTableModel(null, tblRenewalDepSubNoColTitle);
        tblExtensionDepSubNoAccInfo = new EnhancedTableModel(null, tblExtensionDepSubNoColTitle);
        tblLienDetails = new EnhancedTableModel(null, tblLienColTitle);
        tblJointAccnt = new EnhancedTableModel(null, tblJointAccntColTitle);
        tblOldDepDet = new EnhancedTableModel(null, tblOldDepColTitle);
        makeNull();

    }

    private void setTblOldDepDet() throws Exception {
        try {
            tblOldDepColTitle = new ArrayList();
            tblOldDepColTitle.add("SlNo");
            tblOldDepColTitle.add("From Date");
            tblOldDepColTitle.add("To Date");
            tblOldDepColTitle.add("ROI");
            tblOldDepColTitle.add("Int Amt");
            tblOldDepColTitle.add("Dep Amt");
            tblOldDepColTitle.add("Mat Amt");
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }
private void setTblLienDetails() throws Exception {//deposit lien details.
        try {
            tblLienColTitle = new ArrayList();
            tblLienColTitle.add(TBL_DEP_LIEN_COLUMN_1);
            tblLienColTitle.add(TBL_DEP_LIEN_COLUMN_2);
            tblLienColTitle.add(TBL_DEP_LIEN_COLUMN_3);
            tblLienColTitle.add(TBL_DEP_LIEN_COLUMN_4);
            tblLienColTitle.add(TBL_DEP_LIEN_COLUMN_5);

        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }
    //--- Sets the DepositSubNoAccInfo Details Table Column Headings
    private void setDepSubNoTblCol() throws Exception {
        try {
            tblDepSubNoColTitle = new ArrayList();
            tblDepSubNoColTitle.add(objTermDepositRB.getString(TBL_DEP_SUB_NO_COLUMN_1));
            tblDepSubNoColTitle.add(objTermDepositRB.getString(TBL_DEP_SUB_NO_COLUMN_2));
            tblDepSubNoColTitle.add(objTermDepositRB.getString(TBL_DEP_SUB_NO_COLUMN_3));
            tblDepSubNoColTitle.add(objTermDepositRB.getString(TBL_DEP_SUB_NO_COLUMN_4));

        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void setRenewalDepSubNoTblCol() throws Exception {
        try {
            tblRenewalDepSubNoColTitle = new ArrayList();
            tblRenewalDepSubNoColTitle.add(objTermDepositRB.getString(TBL_DEP_SUB_NO_RENEW_COLUMN_1));
            tblRenewalDepSubNoColTitle.add(objTermDepositRB.getString(TBL_DEP_SUB_NO_RENEW_COLUMN_2));
            tblRenewalDepSubNoColTitle.add(objTermDepositRB.getString(TBL_DEP_SUB_NO_RENEW_COLUMN_3));
            tblRenewalDepSubNoColTitle.add(objTermDepositRB.getString(TBL_DEP_SUB_NO_RENEW_COLUMN_4));
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void setExtensionDepSubNoTblCol() throws Exception {
        try {
            tblExtensionDepSubNoColTitle = new ArrayList();
            tblExtensionDepSubNoColTitle.add(objTermDepositRB.getString(TBL_DEP_SUB_NO_RENEW_COLUMN_1));
            tblExtensionDepSubNoColTitle.add(objTermDepositRB.getString(TBL_DEP_SUB_NO_RENEW_COLUMN_2));
            tblExtensionDepSubNoColTitle.add(objTermDepositRB.getString(TBL_DEP_SUB_NO_RENEW_COLUMN_3));
            tblExtensionDepSubNoColTitle.add(objTermDepositRB.getString(TBL_DEP_SUB_NO_RENEW_COLUMN_4));
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void setJntAcccntTblCol() throws Exception {
        try {
            tblJointAccntColTitle = new ArrayList();
            tblJointAccntColTitle.add(objCommRB.getString(TBL_JNT_ACCNT_COLUMN_1));
            tblJointAccntColTitle.add(objCommRB.getString(TBL_JNT_ACCNT_COLUMN_2));
            tblJointAccntColTitle.add(objCommRB.getString(TBL_JNT_ACCNT_COLUMN_3));
            tblJointAccntColTitle.add(objCommRB.getString(TBL_JNT_ACCNT_COLUMN_4));
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    /* Sets the HashMap required to set JNDI,Home and Remote*/
    private void setOperationMap() throws Exception {
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, TERM_DEP_JNDI);
        operationMap.put(CommonConstants.HOME, TERM_DEP_HOME);
        operationMap.put(CommonConstants.REMOTE, TERM_DEP_REMOTE);
    }

    /*To set data for all dropdowns*/
    public void fillDropdown() throws Exception {
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME, null);
        final ArrayList lookup_keys = new ArrayList();
        lookup_keys.add(NOMINEE_STATUS);
        lookup_keys.add(CUSTOMER_COUNTRY);
        lookup_keys.add(CUSTOMER_CITY);
        lookup_keys.add(CUSTOMER_STATE);
        lookup_keys.add(PAY_MODE);
        lookup_keys.add(PAY_FREQUENCY);
        lookup_keys.add(SETTLEMENT_MODE);
        lookup_keys.add(COMM_ADD);
        lookup_keys.add(CONSTITUTION);
        lookup_keys.add(CATEGORY);
        lookup_keys.add(PROD_ID);
        lookup_keys.add(PROD_ID);
        lookup_keys.add(DEPOSITPAYMENTDAY);
        lookup_keys.add(DEPOSITINSTALLAMT);
        lookup_keys.add(MEMBER_TYPE);
        lookup_keys.add("CUSTOMER.ADDRTYPE");
        lookup_keys.add("MDS.GROUP");
        lookup_keys.add("INTEREST_TRANS_PRODUCTTYPE");

        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);

        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get(NOMINEE_STATUS));
        cbmNomDetStatus = new ComboBoxModel(key, value);

        //        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get(CUSTOMER_COUNTRY));
        cbmOtherCountry = new ComboBoxModel(key, value);

        //        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get(CUSTOMER_CITY));
        cbmOtherCity = new ComboBoxModel(key, value);

        //        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get(CUSTOMER_STATE));
        cbmOtherState = new ComboBoxModel(key, value);

        //Added By Suresh
        getKeyValue((HashMap) keyValue.get("CUSTOMER.ADDRTYPE"));
        cbmAddressType = new ComboBoxModel(key, value);
        
        //        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get(PAY_MODE));
        cbmInterestPaymentMode = new ComboBoxModel(key, value);

        //        keyValue = ClientUtil.populateLookupData(lookUpHash);
        HashMap mapData=new HashMap();
          List mapDataList = ClientUtil.executeQuery("getAllDepositPeriod", mapData);
            //system.out.println("#### mapDataList :"+mapDataList);
           key=new ArrayList();
               value=new ArrayList();
            if (mapDataList != null && mapDataList.size() > 0) {
                for(int i=0;i< mapDataList.size();i++){
                mapData = (HashMap) mapDataList.get(i);
              //  getKeyValue((HashMap) keyValue.get(PAY_FREQUENCY));
              
               
                String key1=CommonUtil.convertObjToStr(mapData.get("LOOKUP_REF_ID"));
                String val1=CommonUtil.convertObjToStr(mapData.get("LOOKUP_DESC"));
                 cbmInterestPaymentFrequency = new ComboBoxModel(key, value);
                 if(i==0)
                     cbmInterestPaymentFrequency.addKeyAndElement("", "");
                cbmInterestPaymentFrequency.addKeyAndElement(key1, val1);
                }
            }
       /* cbmInterestPaymentFrequency.addKeyAndElement("60", "2 Months");
        cbmInterestPaymentFrequency.addKeyAndElement("120", "4 Months");
        cbmInterestPaymentFrequency.addKeyAndElement("150", "5 Months");
        cbmInterestPaymentFrequency.addKeyAndElement("210", "7 Months");
        cbmInterestPaymentFrequency.addKeyAndElement("240", "8 Months");
        cbmInterestPaymentFrequency.addKeyAndElement("270", "9 Months");
        cbmInterestPaymentFrequency.addKeyAndElement("300", "10 Months");
        cbmInterestPaymentFrequency.addKeyAndElement("330", "11 Months");*/

        getKeyValue((HashMap) keyValue.get(PAY_MODE));
        cbmRenewalDepTransMode = new ComboBoxModel(key, value);

        getKeyValue((HashMap) keyValue.get(PAY_MODE));
        cbmRenewalInterestTransMode = new ComboBoxModel(key, value);

        getKeyValue((HashMap) keyValue.get(PAY_MODE));
        cbmRenewalInterestPaymentMode = new ComboBoxModel(key, value);

        getKeyValue((HashMap) keyValue.get(PAY_FREQUENCY));
        cbmRenewalInterestPaymentFrequency = new ComboBoxModel(key, value);

        getKeyValue((HashMap) keyValue.get(PAY_MODE));
        cbmExtensionTransMode = new ComboBoxModel(key, value);

        //        getKeyValue((HashMap)keyValue.get(PAY_MODE));
        //        cbmExtensionInterestPaymentMode = new ComboBoxModel(key,value);

        getKeyValue((HashMap) keyValue.get(PAY_MODE));
        cbmExtensionPaymentMode = new ComboBoxModel(key, value);

        getKeyValue((HashMap) keyValue.get(PAY_FREQUENCY));
        cbmExtensionPaymentFrequency = new ComboBoxModel(key, value);

        //        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get(DEPOSITINSTALLAMT));
        cbmInstallmentAmount = new ComboBoxModel(key, value);

        //        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get(DEPOSITPAYMENTDAY));
        cbmPaymentType = new ComboBoxModel(key, value);

        //        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get(SETTLEMENT_MODE));
        cbmSettlementMode = new ComboBoxModel(key, value);


        //        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get(COMM_ADD));
        cbmCommunicationAddress = new ComboBoxModel(key, value);

        //        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get(CONSTITUTION));
        cbmConstitution = new ComboBoxModel(key, value);

        //        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get(MEMBER_TYPE));
        cbmMemberType = new ComboBoxModel(key, value);

        getKeyValue((HashMap) keyValue.get(CATEGORY));
        cbmCategory = new ComboBoxModel(key, value);

        getKeyValue((HashMap) keyValue.get(CATEGORY));
        cbmRenewalDepositCategory = new ComboBoxModel(key, value);

        getKeyValue((HashMap) keyValue.get(CATEGORY));
        cbmExtensionDepositCategory = new ComboBoxModel(key, value);

        getKeyValue((HashMap) keyValue.get("INTEREST_TRANS_PRODUCTTYPE"));
        key.add("RM");
        value.add("Remittance");
        key.add("INV");
        value.add("Investment");
        key.add("MDS");
        value.add("MDS");
        cbmProdType = new ComboBoxModel(key, value);
        cbmRenewalDepTransProdType = new ComboBoxModel(key, value);
        cbmRenewalInterestTransProdType = new ComboBoxModel(key, value);
        cbmRenewalProdType = new ComboBoxModel(key, value);
        cbmExtensionTransProdType = new ComboBoxModel(key, value);
        cbmExtensionPaymentProdType = new ComboBoxModel(key, value);
        cbmExtensionProdType = new ComboBoxModel(key, value);

        key = new ArrayList();
        value = new ArrayList();
        key.add("");
        value.add("");
        for (int i = 1; i <= 31; i++) {
            key.add(String.valueOf(i));
            value.add(String.valueOf(i));
            cbmCalenderFreqDay = new ComboBoxModel(key, value);
        }
        key = new ArrayList();
        value = new ArrayList();
        key.add("");
        value.add("");
        for (int i = 1; i <= 31; i++) {
            key.add(String.valueOf(i));
            value.add(String.valueOf(i));
            cbmRenewalCalenderFreqDay = new ComboBoxModel(key, value);
        }
        key = new ArrayList();
        value = new ArrayList();
        key.add("");
        value.add("");
        for (int i = 1; i <= 31; i++) {
            key.add(String.valueOf(i));
            value.add(String.valueOf(i));
            cbmExtensionCalenderFreqDay = new ComboBoxModel(key, value);
        }
        lookUpHash.put(CommonConstants.MAP_NAME, DEP_GET_PROD_ID);
        lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));
        cbmProductId = new ComboBoxModel(key, value);
        cbmRenewalDepositProdId = new ComboBoxModel(key, value);
        cbmExtensionDepositProdId = new ComboBoxModel(key, value);
        //Added by sreekrishnan
        HashMap introMap=new HashMap();
        List introList = ClientUtil.executeQuery("getDepositIntroducers", introMap);
        System.out.println("#### introList :"+introList);
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
        makeNull();
    }

    /* Splits the keyValue HashMap into key and value arraylists*/
    private void getKeyValue(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }
    private String lblValBalanceInterestPayable = "";
    private String lblValAgentname = "";
    private String lblValTransferingBranchName = "";
    private String tdtDateOfDeposit = "";
    private String tdtPaymentDate = "";
    private String lblValAccountHead = "";
    private String lblValCustomerName = "";
    private String lblValDateOfBirth = "";
    private String lblValStreet = "";
    private String lblValArea = "";
    private String lblValCity = "";
    private String lblValState = "";
    private String lblValCountry = "";
    private String lblValPin = "";
    private String lblProductDescription = "";
    private String tdtMaturityDate = "";
    private String txtRateOfInterest = "";
    private String txtAgentId = "";
    private String txtMaturityAmount = "";
    private String txtTotalInterestAmount = "";
    private String txtPeriodOfDeposit_Years = "";
    private String txtPeriodOfDeposit_Months = "";
    private String txtPeriodOfDeposit_Days = "";
    private String txtPeriodicInterestAmount = "";
    private String cboOtherState = "";
    private String cboOtherCity = "";
    private String cboOtherCountry = "";
    private String cboConstitution = "";
    private String cboProductId = "";
    private String cboPaymentType = "";
    private String cboInstallmentAmount = "";
    private String cboOnBehalfOf = "";
    private String cboNominatedBy = "";
    private String cboIdType = "";
    private String cboDocType = "";
    private String txtDepsoitNo = "";
    private String cboCategory = "";
    private String cboMember = "";
    private String txtCustomerId = "";
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private boolean rdoOpeningMode_Normal = false;
    private boolean rdoOpeningMode_TransferIn = false;
    private boolean rdoOpeningMode_Renewal = false;
    private boolean rdoOpeningMode_Extension = false;
    private String txtDepositAmount = "";
    private String txtPanNumber = "";
    private String lblValDepositSubNo = "";
    private String txtRemarks = "";
    private boolean chkPowerOfAttorney = false;
    private boolean chkAuthorizedSignatory = false;
    private boolean chk15hDeclarations = false;
    private boolean chkTaxDeductions = false;
    private boolean chkNomineeDetails = false;
    private boolean chkMember = false;
    private boolean chkAccZeroBalYN = false;
    private String lblValCommunicationAddress = "";
    private String lblBalanceInterestPayable = "";
    private String cboSettlementMode = "";
    private String cboInterestPaymentMode = "";
    private String cboInterestPaymentFrequency = "";
    private String tdtOriginalDateOfDeposit = "";
    private String txtOriginalAccountNumber = "";
    private String txtTransferingBranchCode = "";
    private String txtPrintedNoOfTheFdr = "";
    private String txtInterBranchTransferNo = "";
    private String txtTransferingAmount = "";
    private String tdtDateOfTransfer = "";
    private String tdtLastInterestCalculatedDate = "";
    private String txtInterestProvidedAmount = "";
    private String txtInterestPaid = "";
    private String txtTotalNumberOfInstallments = "";
    private String tdtLastInstallmentReceivedDate = "";
    private String tdtTdsCollectedUpto = "";
    private String txtLastTdsCollected = "";
    private String txtTotalInstallmentReceived = "";
    private String txtDocNo = "";
    private String tdtIssDt = "";
    private String tdtExpDt = "";
    private String txtIssBy = "";
    private String txtNo = "";
    private String txtIssAuth = "";
    private String txtBankName = "";
    private String txtBranchName = "";
    private String txtAccNo = "";
    private String txtName = "";
    private String txtOtherArea = "";
    private String txtDesig = "";
    private String txtOtherStreet = "";
    private String txtOtherPin = "";
    private String txtOtherAreaCode = "";
    private String txtOtherPhone = "";
    private String lblValRenewDep = null;
    private String customerNameCrValue = "";
    private String txtPeriodOfDeposit_Weeks = "";

    public String getTxtPeriodOfDeposit_Weeks() {
        return txtPeriodOfDeposit_Weeks;
    }

    public void setTxtPeriodOfDeposit_Weeks(String txtPeriodOfDeposit_Weeks) {
        this.txtPeriodOfDeposit_Weeks = txtPeriodOfDeposit_Weeks;
    }
    //this details filled with the present Position Details tab....
    private String availableBalanceValue = "";
    private String clearBalanceValue = "";
    private String totalBalanceValue = "";
    private String totalInterestValue = "";
    private String lastInterestPaidDateValue = "";
    private String paidnterestAmountValue = "";
    private String balanceInterestAmountValue = "";
    private String totalInterestPayableValue = "";
    private String accHeadValue = "";
    private String tdsAmountValue = "";
    private String lastInterestProvisionDateValue = "";
    private String closedDateValue = "";
    private String depositPeriodRunValue = "";
    //those 7things not using
    private String depositLienNoValue = "";
    private String lienAmountValue = "";
    private String lienDateValue = "";
    private String loanNoValue = "";
    private String loanBalanceValue = "";
    private String loanTakenDateValue = "";
    private String closingTypeValue = "";
    private String RenewalDateValue = "";
    private String closingRateOfInterestValue = "";
    private String closingInterestAmountValue = "";
    private String SBRateOfInterestValue = "";
    private String SBInterestAmountValue = "";
    private String transferOutBranchValue = "";
    private String renewalClosedDepNo = "";
    private double balanceAmt = 0.0;
    private boolean rdoDeathClaim_Yes = false;
    private boolean rdoDeathClaim_No = false;
    private boolean rdoAutoRenewal_Yes = false;
    private boolean rdoAutoRenewal_No = false;
    private boolean rdowithIntRenewal_Yes = false;
    private boolean rdowithIntRenewal_No = false;
    private boolean rdoMatAlertReport_Yes = false;
    private boolean rdoMatAlertReport_No = false;
    private boolean rdoStandingInstruction_Yes = false;
    private boolean rdoStandingInstruction_No = false;
    private boolean rdoRenewalMatAlert_report_Yes = false;
    private boolean rdoRenewalMatAlert_report_No = false;
    private boolean rdoRenewalAutoRenewal_Yes = false;
    private boolean rdoRenewalAutoRenewal_No = false;
    private boolean rdoRenewalWith_intRenewal_Yes = false;
    private boolean rdoRenewalWith_intRenewal_No = false;
    private boolean rdoExtensionMatAlertReport_Yes = false;
    private boolean rdoExtensionMatAlertReport_No = false;
    private boolean rdoCalenderFreq_Yes = false;
    private boolean rdoCalenderFreq_No = false;
    private boolean rdoExtensionAutoRenewal_Yes = false;
    private boolean rdoExtensionAutoRenewal_No = false;
    private boolean rdoExtensionWithIntAutoRenewal_Yes = false;
    private boolean rdoExtensionWithIntAutoRenewal_No = false;
    private String calenderFreqDate = "";
    private String printing_No = "";
    private HashMap _authorizeMap;
    public double totalIntCredit = 0.0;
    //standing Instruction details...
    private String SICreatedDateValue = "";
    private String SINoValue = "";
    private String SIProductTypeValue = "";
    private String SIProductIdValue = "";
    private String SIAccountNoValue = "";
    private String SIAmountValue = "";
    private String SIParticularsValue = "";
    private String SIFrequencyValue = "";
    private String SIForwardCountValue = "";
    private String SIStartDateValue = "";
    private String SIEndDateValue = "";
    private String AcceptanceChargesValue = "";
    private String SIFailureChargesValue = "";
    private String SIExecutionChargesValue = "";
    private String lblMemberVal = "";
    private String lblDelayedMonthValue = "";
    private String lblDelayedAmountValue = "";
//Renewal calculation Details...
    private boolean rdoRenewalWithdrawing_Yes = false;
    private boolean rdoRenewalWithdrawing_No = false;
    private boolean rdoRenewalAdding_Yes = false;
    private boolean rdoRenewalAdding_No = false;
    private boolean rdoRenewalWithdrawingInt_Yes = false;
    private boolean rdoRenewalWithdrawingInt_No = false;
    private boolean rdoRenewalCalenderFreq_Yes = false;
    private boolean rdoRenewalCalenderFreq_No = false;
    private String renewalBalIntAmtVal = "";
    private String renewalBalIntAmt = "";
    private String renewalDepDate = "";
    private String renewalValPeriodRun = "";
    private String renewalSBPeriodVal = "";
    private String renewalSBIntAmtVal = "";
    private String renewalSBIntRateVal = "";
    private String renewalInterestRepayAmtVal = "";
    private String renewaltdtDateOfDeposit = "";
    private String renewaltxtPeriodOfDeposit_Years = "";
    private String renewaltxtPeriodOfDeposit_Months = "";
    private String renewaltxtPeriodOfDeposit_Days = "";
    private String renewaltxtPeriodOfDeposit_Weeks = "";
    private String renewaltdtMaturityDate = "";
    private String renewaltxtDepositAmount = "";
    private String renewalValDepositSubNo = "";
    private String renewaltxtRateOfInterest = "";
    private String renewaltxtMaturityAmount = "";
    private String renewaltxtTotalInterestAmount = "";
    private String renewaltxtPeriodicInterestAmount = "";
    private String renewalcboInterestPaymentMode = "";
    private String renewalcboInterestPaymentFrequency = "";
    private String renewalcboCalenderFreq = "";
    private String renewalcboProdId = "";
    private String renewalcboProdType = "";
    private String renewalcustomerIdCr = "";
    private String renewalcustomerNameCrValue = "";
    private String renewalcustomerIdCrInt = "";
    private String renewalcustomerNameCrValueInt = "";
    private String renewalcustomerIdCrDep = "";
    private String renewalcustomerNameCrValueDep = "";
    private String lblRenewalNoticeNewVal = "";
    private String lblAutoRenewalNewVal = "";
    private String lblRenewalWithIntNewVal = "";
    private String lblMemberTypeRenewalNewVal = "";
    private String lblTaxDeclareRenewalNewVal = "";
    private String lbl15HDeclareRenewalNewVal = "";
    private String renewalMemberTypeVal = "";
    private String renewalDepositDateEAMode = "";
    private String cboRenewalDepositProdId = "";
    private ComboBoxModel cbmRenewalDepositProdId;
    private String cboRenewalDepositCategory = "";
    private ComboBoxModel cbmRenewalDepositCategory;
    private String renewedNewNo = "";
    //Extension calculation Details...
    private boolean rdoExtensionWithdrawing_Yes = false;
    private boolean rdoExtensionWithdrawing_No = false;
    private boolean rdoExtensionAdding_Yes = false;
    private boolean rdoExtensionAdding_No = false;
    private boolean rdoExtensionWithdrawingInt_Yes = false;
    private boolean rdoExtensionWithdrawingInt_No = false;
    private boolean rdoExtensionCalenderFreq_Yes = false;
    private boolean rdoExtensionCalenderFreq_No = false;
//    private boolean rdoExtensionMatAlertReport_Yes = false;
//    private boolean rdoExtensionMatAlertReport_No = false;
//    private boolean rdoExtensionAutoRenewal_Yes = false;
//    private boolean rdoExtensionAutoRenewal_No = false;
//    private boolean rdoExtensionWithIntAutoRenewal_Yes = false;
//    private boolean rdoExtensionWithIntAutoRenewal_No = false;
    private String ExtensionBalInterestAmtVal = "";
    private String ExtensionWithdrawIntAmtVal = "";
    private String ExtensionBalIntAmtVal = "";
    private String ExtensionDepDate = "";
    private String ExtensionCurrDt = "";
    private String ExtensionRateOfIntVal = "";
    private String lblExtensionWithdrawIntAmtValue1 = "";
    private String lblExtensionWithdrawIntAmtValue2 = "";
    private String ExtensionPaymentFreqValue = "";
    private String ExtensionActualPeriodRun = "";
    //    private String ExtensionSBPeriodVal = "";
    //    private String ExtensionSBIntAmtVal = "";
    private String ExtensiontdtDateOfDeposit = "";
    private String ExtensiontxtPeriodOfDeposit_Years = "";
    private String ExtensiontxtPeriodOfDeposit_Months = "";
    private String ExtensiontxtPeriodOfDeposit_Days = "";
    private String ExtensiontxtPeriodOfDeposit_Weeks = "";
    private String ExtensiontdtMaturityDate = "";
    private String ExtensiontxtDepositAmount = "";
    private String ExtensiontxtRateOfInterest = "";
    private String ExtensiontxtMaturityAmount = "";
    private String ExtensiontxtTotalInterestAmount = "";
    private String ExtensiontxtPeriodicInterestAmount = "";
    private String ExtensioncboInterestPaymentMode = "";
    private String ExtensioncboInterestPaymentFrequency = "";
    private String ExtensioncboCalenderFreq = "";
    private String ExtensioncboProdId = "";
    private String ExtensioncboProdType = "";
    private String ExtensioncustomerIdCr = "";
    private String ExtensioncustomerNameCrValue = "";
    private String ExtensioncustomerIdCrInt = "";
    private String ExtensioncustomerNameCrValueInt = "";
    private String ExtensioncustomerIdCrDep = "";
    private String ExtensioncustomerNameCrValueDep = "";
    private String cboExtensionDepositProdId = "";
    private ComboBoxModel cbmExtensionDepositProdId;
    private String cboExtensionDepositCategory = "";
    private ComboBoxModel cbmExtensionDepositCategory;
    private String extensionValDepositSubNo = "";
    private boolean chkRenewinterest = false;
    private String preBalIntVal = "";
    private String retirementDt;
    private String lblTDSAmount = "";
    private String tdsAcHd = "";

    public String getRetirementDt() {
        return retirementDt;
    }

    public void setRetirementDt(String retirementDt) {
        this.retirementDt = retirementDt;
    }

   
    

    public String getPreBalIntVal() {
        return preBalIntVal;
    }

    public void setPreBalIntVal(String preBalIntVal) {
        this.preBalIntVal = preBalIntVal;
    }

    public boolean isChkRenewinterest() {
        return chkRenewinterest;
    }

    public void setChkRenewinterest(boolean chkRenewinterest) {
        this.chkRenewinterest = chkRenewinterest;
    }

    public String getLblStatus() {
        return (this.lblStatus);
    }

    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }

    public void setStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }

    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }

    void setLblBalanceInterestPayable(String lblBalanceInterestPayable) {
        this.lblBalanceInterestPayable = lblBalanceInterestPayable;
        setChanged();
    }

    String getLblBalanceInterestPayable() {
        return this.lblBalanceInterestPayable;
    }

    void setLblValTransferingBranchName(String lblValTransferingBranchName) {
        this.lblValTransferingBranchName = lblValTransferingBranchName;
        setChanged();
    }

    String getLblValTransferingBranchName() {
        return this.lblValTransferingBranchName;
    }

    void setLblValBalanceInterestPayable(String lblValBalanceInterestPayable) {
        this.lblValBalanceInterestPayable = lblValBalanceInterestPayable;
        setChanged();
    }

    String getLblValBalanceInterestPayable() {
        return this.lblValBalanceInterestPayable;
    }

    void setLblValCustomerName(String lblValCustomerName) {
        this.lblValCustomerName = lblValCustomerName;
        setChanged();
    }

    String getLblValCustomerName() {
        return this.lblValCustomerName;
    }

    void setLblValDateOfBirth(String lblValDateOfBirth) {
        this.lblValDateOfBirth = lblValDateOfBirth;
        setChanged();
    }

    String getLblValDateOfBirth() {
        return this.lblValDateOfBirth;
    }

    void setLblValStreet(String lblValStreet) {
        this.lblValStreet = lblValStreet;
        setChanged();
    }

    String getLblValStreet() {
        return this.lblValStreet;
    }

    void setLblValArea(String lblValArea) {
        this.lblValArea = lblValArea;
        setChanged();
    }

    String getLblValArea() {
        return this.lblValArea;
    }

    void setLblValCity(String lblValCity) {
        this.lblValCity = lblValCity;
        setChanged();

    }

    String getLblValCity() {
        return this.lblValCity;
    }

    void setLblValState(String lblValState) {
        this.lblValState = lblValState;
        setChanged();
    }

    String getLblValState() {
        return this.lblValState;
    }

    void setLblValCountry(String lblValCountry) {
        this.lblValCountry = lblValCountry;
        setChanged();
    }

    String getLblValCountry() {
        return this.lblValCountry;
    }

    void setLblValPin(String lblValPin) {
        this.lblValPin = lblValPin;
        setChanged();
    }

    String getLblValPin() {
        return this.lblValPin;
    }

    void setTdtMaturityDate(String tdtMaturityDate) {
        this.tdtMaturityDate = tdtMaturityDate;
        setChanged();
    }

    String getTdtMaturityDate() {
        return this.tdtMaturityDate;
    }

    void setTxtRateOfInterest(String txtRateOfInterest) {
        this.txtRateOfInterest = txtRateOfInterest;
        setChanged();
    }

    String getTxtRateOfInterest() {
        return this.txtRateOfInterest;
    }

    void setTxtMaturityAmount(String txtMaturityAmount) {
        this.txtMaturityAmount = txtMaturityAmount;
        setChanged();
    }

    String getTxtMaturityAmount() {
        return this.txtMaturityAmount;
    }

    void setTxtTotalInterestAmount(String txtTotalInterestAmount) {
        this.txtTotalInterestAmount = txtTotalInterestAmount;
        setChanged();
    }

    String getTxtTotalInterestAmount() {
        return this.txtTotalInterestAmount;
    }

    void setTxtPeriodOfDeposit_Years(String txtPeriodOfDeposit_Years) {
        this.txtPeriodOfDeposit_Years = txtPeriodOfDeposit_Years;
        setChanged();
    }

    String getTxtPeriodOfDeposit_Years() {
        return this.txtPeriodOfDeposit_Years;
    }

    void setTxtPeriodOfDeposit_Months(String txtPeriodOfDeposit_Months) {
        this.txtPeriodOfDeposit_Months = txtPeriodOfDeposit_Months;
        setChanged();
    }

    String getTxtPeriodOfDeposit_Months() {
        return this.txtPeriodOfDeposit_Months;
    }

    void setTxtPeriodOfDeposit_Days(String txtPeriodOfDeposit_Days) {

        this.txtPeriodOfDeposit_Days = txtPeriodOfDeposit_Days;
        setChanged();
    }

    String getTxtPeriodOfDeposit_Days() {
        return this.txtPeriodOfDeposit_Days;
    }

    void setTxtPeriodicInterestAmount(String txtPeriodicInterestAmount) {
        this.txtPeriodicInterestAmount = txtPeriodicInterestAmount;
        setChanged();
    }

    String getTxtPeriodicInterestAmount() {
        return this.txtPeriodicInterestAmount;
    }

    void setCboConstitution(String cboConstitution) {
        this.cboConstitution = cboConstitution;
        setChanged();
    }

    String getCboConstitution() {
        return this.cboConstitution;
    }

    public void setCbmConstitution(ComboBoxModel cbmConstitution) {
        this.cbmConstitution = cbmConstitution;
        setChanged();
    }

    ComboBoxModel getCbmConstitution() {
        return cbmConstitution;
    }

    void setCboInstallmentAmount(String cboInstallmentAmount) {
        this.cboInstallmentAmount = cboInstallmentAmount;
        setChanged();
    }

    String getCboInstallmentAmount() {
        return this.cboInstallmentAmount;
    }

    public void setCbmInstallmentAmount(ComboBoxModel cbmInstallmentAmount) {
        this.cbmInstallmentAmount = cbmInstallmentAmount;
        setChanged();
    }

    ComboBoxModel getCbmInstallmentAmount() {
        return cbmInstallmentAmount;
    }

    void setCboPaymentType(String cboPaymentType) {
        this.cboPaymentType = cboPaymentType;
        setChanged();
    }

    String getCboPaymentType() {
        return this.cboPaymentType;
    }

    public void setCbmPaymentType(ComboBoxModel cbmPaymentType) {
        this.cbmPaymentType = cbmPaymentType;
        setChanged();
    }

    ComboBoxModel getCbmPaymentType() {
        return cbmPaymentType;
    }

    void setCboOnBehalfOf(String cboOnBehalfOf) {
        this.cboOnBehalfOf = cboOnBehalfOf;
        setChanged();
    }

    String getCboOnBehalfOf() {
        return this.cboOnBehalfOf;
    }

    public void setCbmOnBehalfOf(ComboBoxModel cbmOnBehalfOf) {
        this.cbmOnBehalfOf = cbmOnBehalfOf;
        setChanged();
    }

    ComboBoxModel getCbmOnBehalfOf() {
        return cbmOnBehalfOf;
    }

    void setCboNominatedBy(String cboNominatedBy) {
        this.cboNominatedBy = cboNominatedBy;
        setChanged();
    }

    String getCboNominatedBy() {
        return this.cboNominatedBy;
    }

    public void setCbmNominatedBy(ComboBoxModel cbmNominatedBy) {
        this.cbmNominatedBy = cbmNominatedBy;
        setChanged();
    }

    ComboBoxModel getCbmNominatedBy() {
        return cbmNominatedBy;
    }

    void setCboOtherState(String cboOtherState) {
        this.cboOtherState = cboOtherState;
        setChanged();
    }

    String getCboOtherState() {
        return this.cboOtherState;
    }

    public void setCbmOtherState(ComboBoxModel cbmOtherState) {
        this.cbmOtherState = cbmOtherState;
        setChanged();
    }

    ComboBoxModel getCbmOtherState() {
        return cbmOtherState;
    }

    void setCboOtherCity(String cboOtherCity) {
        this.cboOtherCity = cboOtherCity;
        setChanged();
    }

    String getCboOtherCity() {
        return this.cboOtherCity;
    }

    public void setCbmOtherCity(ComboBoxModel cbmOtherCity) {
        this.cbmOtherCity = cbmOtherCity;
        setChanged();
    }

    ComboBoxModel getCbmOtherCity() {
        return cbmOtherCity;
    }

    void setCboOtherCountry(String cboOtherCountry) {
        this.cboOtherCountry = cboOtherCountry;
        setChanged();
    }

    String getCboOtherCountry() {
        return this.cboOtherCountry;
    }

    public void setCbmOtherCountry(ComboBoxModel cbmOtherCountry) {
        this.cbmOtherCountry = cbmOtherCountry;
        setChanged();
    }

    ComboBoxModel getCbmOtherCountry() {
        return cbmOtherCountry;
    }

    public void setCboProductId(String cboProductId) {
        this.cboProductId = cboProductId;
        setChanged();
    }

    String getCboProductId() {

        return this.cboProductId;
    }

    public void setCbmProductId(ComboBoxModel cbmProductId) {
        this.cbmProductId = cbmProductId;
        setChanged();
    }

    ComboBoxModel getCbmProductId() {
        return cbmProductId;
    }

    void setTxtDepsoitNo(String txtDepsoitNo) {
        this.txtDepsoitNo = txtDepsoitNo;
        setChanged();
    }

    String getTxtDepsoitNo() {
        return this.txtDepsoitNo;
    }

    public void setCbmDepositNo(ComboBoxModel cbmDepositNo) {
        this.cbmDepositNo = cbmDepositNo;
        setChanged();
    }

    ComboBoxModel getCbmDepositNo() {
        return cbmDepositNo;
    }

    void setCboDocType(String cboDocType) {
        this.cboDocType = cboDocType;
        setChanged();
    }

    String getCboDocType() {
        return this.cboDocType;
    }

    public void setCbmDocType(ComboBoxModel cbmDocType) {
        this.cbmDocType = cbmDocType;
        setChanged();
    }

    ComboBoxModel getCbmDocType() {
        return cbmDocType;
    }

    void setCboCategory(String cboCategory) {
        this.cboCategory = cboCategory;
        setChanged();
    }

    String getCboCategory() {
        return this.cboCategory;
    }

    public void setCbmCategory(ComboBoxModel cbmCategory) {
        this.cbmCategory = cbmCategory;
        setChanged();
    }

    ComboBoxModel getCbmCategory() {
        return cbmCategory;
    }

    void setTxtCustomerId(String txtCustomerId) {
        this.txtCustomerId = txtCustomerId;
        setChanged();
    }

    String getTxtCustomerId() {
        return this.txtCustomerId;
    }

    void setRdoOpeningMode_Renewal(boolean rdoOpeningMode_Renewal) {
        this.rdoOpeningMode_Renewal = rdoOpeningMode_Renewal;
        setChanged();
    }

    boolean getRdoOpeningMode_Renewal() {
        return this.rdoOpeningMode_Renewal;
    }

    void setRdoOpeningMode_Normal(boolean rdoOpeningMode_Normal) {
        this.rdoOpeningMode_Normal = rdoOpeningMode_Normal;
        setChanged();
    }

    boolean getRdoOpeningMode_Normal() {
        return this.rdoOpeningMode_Normal;
    }

    void setRdoOpeningMode_TransferIn(boolean rdoOpeningMode_TransferIn) {
        this.rdoOpeningMode_TransferIn = rdoOpeningMode_TransferIn;
        setChanged();
    }

    boolean getRdoOpeningMode_TransferIn() {
        return this.rdoOpeningMode_TransferIn;
    }

    void setTxtDepositAmount(String txtDepositAmount) {
        this.txtDepositAmount = txtDepositAmount;
        setChanged();
    }

    String getTxtDepositAmount() {
        return this.txtDepositAmount;
    }

    void setTxtPanNumber(String txtPanNumber) {
        this.txtPanNumber = txtPanNumber;
        setChanged();
    }

    String getTxtPanNumber() {
        return this.txtPanNumber;
    }

    void setLblValDepositSubNo(String lblValDepositSubNo) {
        this.lblValDepositSubNo = lblValDepositSubNo;
        setChanged();
    }

    String getLblValDepositSubNo() {
        return this.lblValDepositSubNo;
    }

    void setTxtRemarks(String txtRemarks) {
        this.txtRemarks = txtRemarks;
        setChanged();
    }

    String getTxtRemarks() {
        return this.txtRemarks;
    }

    void setChkPowerOfAttorney(boolean chkPowerOfAttorney) {
        this.chkPowerOfAttorney = chkPowerOfAttorney;
        setChanged();
    }

    boolean getChkPowerOfAttorney() {
        return this.chkPowerOfAttorney;
    }

    void setChkAuthorizedSignatory(boolean chkAuthorizedSignatory) {
        this.chkAuthorizedSignatory = chkAuthorizedSignatory;
        setChanged();
    }

    boolean getChkAuthorizedSignatory() {
        return this.chkAuthorizedSignatory;
    }

    void setChk15hDeclarations(boolean chk15hDeclarations) {
        this.chk15hDeclarations = chk15hDeclarations;
        setChanged();
    }

    boolean getChk15hDeclarations() {
        return this.chk15hDeclarations;
    }

    void setChkTaxDeductions(boolean chkTaxDeductions) {
        this.chkTaxDeductions = chkTaxDeductions;
        setChanged();
    }

    boolean getChkTaxDeductions() {
        return this.chkTaxDeductions;
    }

    void setChkNomineeDetails(boolean chkNomineeDetails) {
        this.chkNomineeDetails = chkNomineeDetails;
        setChanged();
    }

    boolean getChkNomineeDetails() {
        return this.chkNomineeDetails;
    }

    void setLblValCommunicationAddress(String lblValCommunicationAddress) {
        this.lblValCommunicationAddress = lblValCommunicationAddress;
        setChanged();
    }

    String getLblValCommunicationAddress() {
        return this.lblValCommunicationAddress;
    }

    void setLblProductDescription(String lblProductDescription) {
        this.lblProductDescription = lblProductDescription;
        setChanged();
    }

    String getLblProductDescription() {
        return this.lblProductDescription;
    }

    public void setCbmCommunicationAddress(ComboBoxModel cbmCommunicationAddress) {
        this.cbmCommunicationAddress = cbmCommunicationAddress;
        setChanged();
    }

    ComboBoxModel getCbmCommunicationAddress() {
        return cbmCommunicationAddress;
    }

    void setCboSettlementMode(String cboSettlementMode) {
        this.cboSettlementMode = cboSettlementMode;
        setChanged();
    }

    String getCboSettlementMode() {
        return this.cboSettlementMode;
    }

    public void setCbmSettlementMode(ComboBoxModel cbmSettlementMode) {
        this.cbmSettlementMode = cbmSettlementMode;
        setChanged();
    }

    ComboBoxModel getCbmSettlementMode() {
        return cbmSettlementMode;
    }

    void setCboInterestPaymentMode(String cboInterestPaymentMode) {
        this.cboInterestPaymentMode = cboInterestPaymentMode;
        setChanged();
    }

    String getCboInterestPaymentMode() {
        return this.cboInterestPaymentMode;
    }

    public void setCbmInterestPaymentMode(ComboBoxModel cbmInterestPaymentMode) {
        this.cbmInterestPaymentMode = cbmInterestPaymentMode;
        setChanged();
    }

    ComboBoxModel getCbmInterestPaymentMode() {
        return cbmInterestPaymentMode;
    }

    void setCboIdType(String cboIdType) {
        this.cboIdType = cboIdType;
        setChanged();
    }

    String getCboIdType() {
        return this.cboIdType;
    }

    public void setCbmIdType(ComboBoxModel cbmIdType) {
        this.cbmIdType = cbmIdType;
        setChanged();
    }

    ComboBoxModel getCbmIdType() {
        return cbmIdType;
    }

    void setCboInterestPaymentFrequency(String cboInterestPaymentFrequency) {
        this.cboInterestPaymentFrequency = cboInterestPaymentFrequency;
        setChanged();
    }

    String getCboInterestPaymentFrequency() {
        return this.cboInterestPaymentFrequency;
    }

    public void setCbmInterestPaymentFrequency(ComboBoxModel cbmInterestPaymentFrequency) {
        this.cbmInterestPaymentFrequency = cbmInterestPaymentFrequency;
        setChanged();
    }

    ComboBoxModel getCbmInterestPaymentFrequency() {
        return cbmInterestPaymentFrequency;
    }

    void setTdtOriginalDateOfDeposit(String tdtOriginalDateOfDeposit) {
        this.tdtOriginalDateOfDeposit = tdtOriginalDateOfDeposit;
        setChanged();
    }

    String getTdtOriginalDateOfDeposit() {
        return this.tdtOriginalDateOfDeposit;
    }

    void setTxtOriginalAccountNumber(String txtOriginalAccountNumber) {
        this.txtOriginalAccountNumber = txtOriginalAccountNumber;
        setChanged();
    }

    String getTxtOriginalAccountNumber() {
        return this.txtOriginalAccountNumber;
    }

    void setTxtTransferingBranchCode(String txtTransferingBranchCode) {
        this.txtTransferingBranchCode = txtTransferingBranchCode;
        setChanged();
    }

    String getTxtTransferingBranchCode() {
        return this.txtTransferingBranchCode;
    }

    void setTxtPrintedNoOfTheFdr(String txtPrintedNoOfTheFdr) {
        this.txtPrintedNoOfTheFdr = txtPrintedNoOfTheFdr;
        setChanged();
    }

    String getTxtPrintedNoOfTheFdr() {
        return this.txtPrintedNoOfTheFdr;
    }

    void setTxtInterBranchTransferNo(String txtInterBranchTransferNo) {
        this.txtInterBranchTransferNo = txtInterBranchTransferNo;
        setChanged();
    }

    String getTxtInterBranchTransferNo() {
        return this.txtInterBranchTransferNo;
    }

    void setTxtTransferingAmount(String txtTransferingAmount) {
        this.txtTransferingAmount = txtTransferingAmount;
        setChanged();
    }

    String getTxtTransferingAmount() {
        return this.txtTransferingAmount;
    }

    void setTdtDateOfTransfer(String tdtDateOfTransfer) {
        this.tdtDateOfTransfer = tdtDateOfTransfer;
        setChanged();
    }

    String getTdtDateOfTransfer() {
        return this.tdtDateOfTransfer;
    }

    void setTdtLastInterestCalculatedDate(String tdtLastInterestCalculatedDate) {
        this.tdtLastInterestCalculatedDate = tdtLastInterestCalculatedDate;
        setChanged();
    }

    String getTdtLastInterestCalculatedDate() {
        return this.tdtLastInterestCalculatedDate;
    }

    void setTxtInterestProvidedAmount(String txtInterestProvidedAmount) {
        this.txtInterestProvidedAmount = txtInterestProvidedAmount;
        setChanged();
    }

    String getTxtInterestProvidedAmount() {
        return this.txtInterestProvidedAmount;
    }

    void setTxtInterestPaid(String txtInterestPaid) {
        this.txtInterestPaid = txtInterestPaid;
        setChanged();
    }

    String getTxtInterestPaid() {
        return this.txtInterestPaid;
    }

    void setTxtTotalNumberOfInstallments(String txtTotalNumberOfInstallments) {
        this.txtTotalNumberOfInstallments = txtTotalNumberOfInstallments;
        setChanged();
    }

    String getTxtTotalNumberOfInstallments() {
        return this.txtTotalNumberOfInstallments;
    }

    void setTdtLastInstallmentReceivedDate(String tdtLastInstallmentReceivedDate) {
        this.tdtLastInstallmentReceivedDate = tdtLastInstallmentReceivedDate;
        setChanged();
    }

    String getTdtLastInstallmentReceivedDate() {
        return this.tdtLastInstallmentReceivedDate;
    }

    void setTdtTdsCollectedUpto(String tdtTdsCollectedUpto) {
        this.tdtTdsCollectedUpto = tdtTdsCollectedUpto;
        setChanged();
    }

    String getTdtTdsCollectedUpto() {
        return this.tdtTdsCollectedUpto;
    }

    void setTxtLastTdsCollected(String txtLastTdsCollected) {
        this.txtLastTdsCollected = txtLastTdsCollected;
        setChanged();
    }

    String getTxtLastTdsCollected() {
        return this.txtLastTdsCollected;
    }

    void setTxtTotalInstallmentReceived(String txtTotalInstallmentReceived) {
        this.txtTotalInstallmentReceived = txtTotalInstallmentReceived;
        setChanged();
    }

    String getTxtTotalInstallmentReceived() {
        return this.txtTotalInstallmentReceived;
    }

    public void ttNotifyObservers() {
        notifyObservers();
    }

    void setTdtDateOfDeposit(String tdtDateOfDeposit) {
        this.tdtDateOfDeposit = tdtDateOfDeposit;
        setChanged();
    }

    String getTdtDateOfDeposit() {
        return this.tdtDateOfDeposit;
    }

    void setTdtPaymentDate(String tdtPaymentDate) {
        this.tdtPaymentDate = tdtPaymentDate;
        setChanged();
    }

    String getTdtPaymentDate() {
        return this.tdtPaymentDate;
    }

    void setLblValAccountHead(String lblValAccountHead) {
        this.lblValAccountHead = lblValAccountHead;
        setChanged();
    }

    String getLblValAccountHead() {
        return this.lblValAccountHead;
    }

    void setTxtDocNo(String txtDocNo) {
        this.txtDocNo = txtDocNo;
        setChanged();
    }

    String getTxtDocNo() {
        return this.txtDocNo;
    }

    void setTdtIssDt(String tdtIssDt) {
        this.tdtIssDt = tdtIssDt;
        setChanged();
    }

    String getTdtIssDt() {
        return this.tdtIssDt;
    }

    void setTdtExpDt(String tdtExpDt) {
        this.tdtExpDt = tdtExpDt;
        setChanged();
    }

    String getTdtExpDt() {
        return this.tdtExpDt;
    }

    void setTxtIssBy(String txtIssBy) {
        this.txtIssBy = txtIssBy;
        setChanged();
    }

    String getTxtIssBy() {
        return this.txtIssBy;
    }

    void setTxtNo(String txtNo) {
        this.txtNo = txtNo;
        setChanged();
    }

    String getTxtNo() {
        return this.txtNo;
    }

    void setTxtIssAuth(String txtIssAuth) {
        this.txtIssAuth = txtIssAuth;
        setChanged();
    }

    String getTxtIssAuth() {
        return this.txtIssAuth;
    }

    void setTxtBankName(String txtBankName) {
        this.txtBankName = txtBankName;
        setChanged();
    }

    String getTxtBankName() {
        return this.txtBankName;
    }

    void setTxtBranchName(String txtBranchName) {
        this.txtBranchName = txtBranchName;
        setChanged();
    }

    String getTxtBranchName() {
        return this.txtBranchName;
    }

    void setTxtAccNo(String txtAccNo) {
        this.txtAccNo = txtAccNo;
        setChanged();
    }

    String getTxtAccNo() {
        return this.txtAccNo;
    }

    void setTxtName(String txtName) {
        this.txtName = txtName;
        setChanged();
    }

    String getTxtName() {
        return this.txtName;
    }

    void setTxtOtherArea(String txtOtherArea) {
        this.txtOtherArea = txtOtherArea;
        setChanged();
    }

    String getTxtOtherArea() {
        return this.txtOtherArea;
    }

    void setTxtDesig(String txtDesig) {
        this.txtDesig = txtDesig;
        setChanged();
    }

    String getTxtDesig() {
        return this.txtDesig;
    }

    void setTxtOtherStreet(String txtOtherStreet) {
        this.txtOtherStreet = txtOtherStreet;
        setChanged();
    }

    String getTxtOtherStreet() {
        return this.txtOtherStreet;
    }

    void setTxtOtherPin(String txtOtherPin) {
        this.txtOtherPin = txtOtherPin;
        setChanged();
    }

    String getTxtOtherPin() {
        return this.txtOtherPin;
    }

    void setTxtOtherAreaCode(String txtOtherAreaCode) {
        this.txtOtherAreaCode = txtOtherAreaCode;
        setChanged();
    }

    String getTxtOtherAreaCode() {
        return this.txtOtherAreaCode;
    }

    void setTxtOtherPhone(String txtOtherPhone) {
        this.txtOtherPhone = txtOtherPhone;
        setChanged();
    }

    String getTxtOtherPhone() {
        return this.txtOtherPhone;
    }

    //--- resets the AccInfo Tab screen
    public void resetAccInfo() {
        setTdtMaturityDate("");
        setTxtRateOfInterest("");
        setTxtMaturityAmount("");
        setTxtTotalInterestAmount("");
        setTxtPeriodOfDeposit_Years("");
        setTxtPeriodOfDeposit_Months("");
        setTxtPeriodOfDeposit_Days("");
        setTxtPeriodicInterestAmount("");
        setCboConstitution("");
        setCboAddressType("");
        getCbmProductId().setKeyForSelected("");
        setCboProductId("");
        setTxtDepsoitNo("");
        setCboCategory("");
        setTxtCustomerId("");
        setTxtDepositAmount("");
        setTxtPanNumber("");
        setLblValDepositSubNo("");
        setTxtRemarks("");
        setLblValCommunicationAddress("");
        setCboSettlementMode("");
        setCboInterestPaymentMode("");
        setCboInterestPaymentFrequency("");
        setLblStatus(ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL]);
        setLblValAccountHead("");
        resetCustDetails();
        setTdtDateOfDeposit("");
        setTdtPaymentDate("");
        setTxtCustomerId("");
        setLblProductDescription("");
        setLblValAccountHead("");
        setLblValRenewDep("");
        setTxtAgentId("");
        setLblValAgentname("");
        setCboMember("");
        setReferenceNo("");
        setPrinting_No("");
        setRenewalClosedDepNo("");
        setLblMemberVal("");
        setRdoDeathClaim_No(false);
        setRdoDeathClaim_Yes(false);
        setRdoAutoRenewal_No(false);
        setRdoAutoRenewal_Yes(false);
        setRdoMatAlertReport_No(false);
        setRdoMatAlertReport_Yes(false);
        setRdowithIntRenewal_No(false);
        setRdowithIntRenewal_Yes(false);
        setRdoStandingInstruction_Yes(false);
        setRdoStandingInstruction_No(false);
        setRdoCalenderFreq_No(false);
        setRdoCalenderFreq_Yes(false);
        setChk15hDeclarations(false);
        setChkAuthorizedSignatory(false);
        setChkNomineeDetails(false);
        setChkPowerOfAttorney(false);
        setChkTaxDeductions(false);
        setChkMember(false);
        setChkAccZeroBalYN(false);
        setRdoOpeningMode_Normal(false);
        setRdoOpeningMode_Extension(false);
        setRdoOpeningMode_TransferIn(false);
        setRdoOpeningMode_Renewal(false);
        setRdoRenewalWithdrawing_Yes(false);
        setRdoRenewalWithdrawing_No(false);
        setRdoRenewalAdding_Yes(false);
        setRdoRenewalAdding_No(false);
        setRdoRenewalWithdrawingInt_Yes(false);
        setRdoRenewalWithdrawingInt_No(false);
        setRdoRenewalCalenderFreq_Yes(false);
        setRdoRenewalCalenderFreq_No(false);
        setRdoRenewalMatAlert_report_Yes(false);
        setRdoRenewalMatAlert_report_No(false);
        setRdoRenewalAutoRenewal_Yes(false);
        setRdoRenewalAutoRenewal_No(false);
        setRdoRenewalWith_intRenewal_Yes(false);
        setRdoRenewalWith_intRenewal_No(false);

        setRdoExtensionAdding_No(false);
        setRdoExtensionAdding_Yes(false);
        setRdoExtensionCalenderFreq_No(false);
        setRdoExtensionCalenderFreq_Yes(false);
        setRdoExtensionWithdrawingInt_No(false);
        setRdoExtensionWithdrawingInt_Yes(false);
        setRdoExtensionWithdrawing_No(false);
        setRdoExtensionWithdrawing_Yes(false);
        setRdoExtensionWithdrawing_No(false);
        setRdoExtensionAutoRenewal_Yes(false);
        setRdoExtensionAutoRenewal_No(false);
        setRdoExtensionMatAlertReport_Yes(false);
        setRdoExtensionMatAlertReport_No(false);
        setRdoExtensionWithIntAutoRenewal_Yes(false);
        setRdoExtensionWithIntAutoRenewal_No(false);
        setCboIntroducer("");
        // Added by nithya on 08-03.2016 for 0003920
        setAdditionalAmount(""); 
        setEffectiveDate("");         
        deleteThriftBenevolentTableData();
        // Added by nithya on 13-12-2016
        setTxtMobileNo("");
        setChkMobileBanking(false);
        setIsYearlyRD("N"); //  Added by nithya on 15-09-2017 for 0007656: Need yearly installment remittance option in rd type deposit
        setIsGroupDeposit("N"); // Added by nithya on 09-10-2017 for group deposit changes
    }

    //--- resets the Customer Details
    public void resetCustDetails() {
        setLblValArea("");
        setLblValCity("");
        setLblValCountry("");
        setLblValCustomerName("");
        setLblValDateOfBirth("");
        setLblValPin("");
        setLblValState("");
        setLblValStreet("");
    }

    //--- resets the TransferIn TabmoveToMain
    public void resetTransferIn() {
        setTxtOriginalAccountNumber("");
        setTxtTransferingBranchCode("");
        setTxtPrintedNoOfTheFdr("");
        setTxtInterBranchTransferNo("");
        setTxtTransferingAmount("");
        setTxtInterestProvidedAmount("");
        setTxtInterestPaid("");
        setTxtTotalNumberOfInstallments("");
        setTxtLastTdsCollected("");
        setTxtTotalInstallmentReceived("");
        setLblValBalanceInterestPayable("");
        setLblValTransferingBranchName("");
        setTdtDateOfTransfer("");
        setTdtLastInstallmentReceivedDate("");
        setTdtLastInterestCalculatedDate("");
        setTdtOriginalDateOfDeposit("");
        setTdtTdsCollectedUpto("");

    }

    public void resetAmount() {
        setTxtDepositAmount("");
        ttNotifyObservers();
    }

    //--- resets the UI form
    public void resetForm() {
        resetAccInfo();
        resetLables();
        resetTransferIn();
        resetDepSubNo();
        resetDepSubNoTbl();
        resetJntAccntHoldTbl();
        resetDepLienTbl();
        resetPresentPosition();
        renewalReset();
        resetRenewalDepSubNoTbl();
        resetExtensionDepSubNoTbl();
        extensionReset();
        nomineeTOList = null;
        jntAcctAll = null;
        interestRateMap = null;        
//        setTxtMdsGroup("");
//        setTxaMdsRemarks("");
         mdsGroup = null;
        mdsRemarks = null;    
        depositGroup = ""; // Added by nithya on 12-10-2017 for group deposit changes
        depositGroupRemarks = "";
        renewaldepSubNoAll = null; //KD-3918 on 17 feb 2025 by nithya
    }

    public void extensionReset() {
        setExtensiontdtDateOfDeposit("");
        setExtensiontdtMaturityDate("");
        setExtensiontxtRateOfInterest("");
        setExtensiontxtDepositAmount("");
        setExtensionValDepositSubNo("");
        setExtensiontxtMaturityAmount("");
        setExtensiontxtTotalInterestAmount("");
        setExtensiontxtPeriodOfDeposit_Years("");
        setExtensiontxtPeriodOfDeposit_Months("");
        setExtensiontxtPeriodOfDeposit_Days("");
        setExtensiontxtPeriodicInterestAmount("");
        setCboExtensionTransMode("");
        setCboExtensionTransProdId("");
        setCboExtensionTransProdType("");
        setCboExtensionDepositCategory("");
        setCboExtensionDepositProdId("");
        setCboExtensionCalenderFreqDay("");
        setCboExtensionPaymentFrequency("");
        setCboExtensionPaymentMode("");
        setCboExtensionPaymentProdId("");
        setCboExtensionPaymentProdType("");
        setCboExtensionPaymentFrequency("");
        setTxtExtensionTransAmtValue("");
        setTxtExtensionTransTokenNo("");
        setTxtExtensionIntAmtValue("");
        setExtensionDepDate("");
        setExtensionBalIntAmtVal("");
        setExtensioncustomerIdCrInt("");
        setTxtExtensionPrintedOption("");
        setExtensioncustomerNameCrValueInt("");
        setExtensionBalanceIntValue("");
        setExtensionBalIntAmtVal("");
        setExtensioncustomerIdCr("");
        setExtensioncustomerNameCrValue("");
        setExtensioncustomerNameCrValueDep("");

    }

    public void resetPresentPosition() {
        setAvailableBalanceValue("");
        setTotalBalanceValue("");
        setClearBalanceValue("");
        setLastInterestPaidDateValue("");
        setTotalInterestValue("");
        setTotalInterestPayableValue("");
        setTdsAmountValue("");
        setLastInterestProvisionDateValue("");
        setClosedDateValue("");
        setDepositLienNoValue("");
        setLienAmountValue("");
        setLoanNoValue("");
        setLoanBalanceValue("");
        setLoanTakenDateValue("");
        setClosingTypeValue("");
        setDepositPeriodRunValue("");
        setRenewalDateValue("");
        setClosingInterestAmountValue("");
        setSBInterestAmountValue("");
        setClosingRateOfInterestValue("");
        setSBRateOfInterestValue("");
        setSICreatedDateValue(""); //from here standingInstruciton...
        setSINoValue("");
        setSIProductTypeValue("");
        setSIProductIdValue("");
        setSIAccountNoValue("");
        setSIAmountValue("");
        setSIParticularsValue("");
        setSIFrequencyValue("");
        setSIForwardCountValue("");
        setSIStartDateValue("");
        setSIEndDateValue("");
        setAcceptanceChargesValue("");
        setSIFailureChargesValue("");
        setSIExecutionChargesValue("");
        setTransferOutBranchValue("");
        setLblDelayedMonthValue("");
        setLblDelayedAmountValue("");
    }

    public void resetLables() {
        setLblValArea("");
        setLblValCity("");
        setLblValCountry("");
        setLblValCustomerName("");
        setLblValDateOfBirth("");
        setLblValPin("");
        setLblValState("");
        setLblValStreet("");
    }

    public void renewalReset() {
        setRenewaltdtDateOfDeposit("");
        setRenewaltdtMaturityDate("");
        setRenewaltxtRateOfInterest("");
        setRenewaltxtDepositAmount("");
        setRenewalValDepositSubNo("");
        setRenewaltxtMaturityAmount("");
        setRenewaltxtTotalInterestAmount("");
        setRenewaltxtPeriodOfDeposit_Years("");
        setRenewaltxtPeriodOfDeposit_Months("");
        setRenewaltxtPeriodOfDeposit_Days("");
        setRenewaltxtPeriodicInterestAmount("");
        setCboRenewalDepTransMode("");
        setCboRenewalDepTransProdId("");
        setCboRenewalDepTransProdType("");
        setCboRenewalDepositCategory("");
        setCboRenewalDepositProdId("");
        setCboRenewalCalenderFreqDay("");
        setCboRenewalInterestPaymentFrequency("");
        setCboRenewalInterestPaymentMode("");
        setCboRenewalInterestTransMode("");
        setCboRenewalInterestTransProdId("");
        setCboRenewalInterestTransProdType("");
        setCboRenewalProdId("");
        setCboRenewalProdType("");
        setTxtRenewalDepTransAmtValue("");
        setTxtRenewalDepTransTokenNo("");
        setTxtRenewalIntAmtValue("");
        setTxtRenewalIntTokenNoVal("");
        setRenewalDepDate("");
        setRenewalBalIntAmtVal("");
        setRenewalValPeriodRun("");
        setRenewalSBIntAmtVal("");
        setRenewalSBIntRateVal("");
        setRenewalInterestRepayAmtVal("");
        setRenewalcustomerIdCrInt("");
        setTxtRenewalPrintedOption("");
        setRenewalcustomerNameCrValueInt("");
        setRenewalcustomerIdCrDep("");
        setRenewalcustomerNameCrValueDep("");
        setRenewalcustomerIdCr("");
        setRenewalcustomerNameCrValue("");
        setLblRenewalNoticeNewVal("");
        setLblAutoRenewalNewVal("");
        setLblRenewalWithIntNewVal("");
        setLblMemberTypeRenewalNewVal("");
        setLblTaxDeclareRenewalNewVal("");
        setLbl15HDeclareRenewalNewVal("");
        setRenewalMemberTypeVal("");
        setPostageRenewAmt("");
        setPostageAmt("");
        setRenewalCountWithScreen("FALSE");
        setChkRenewinterest(false);
        setLblTDSAmount("");
        setTdsAcHd("");
        setRenewedNewNo("");//KD-3529
    }
    //--- resets the Joint Accnt Holder Table

    public void resetJntAccntHoldTbl() {
        tblJointAccnt.setDataArrayList(null, tblJointAccntColTitle);
    }
    //--- resets the deposit lien table.

    public void resetDepLienTbl() {
        tblLienDetails.setDataArrayList(null, tblLienColTitle);
    }
    public void resetOldDepDet() {
        tblOldDepDet.setDataArrayList(null, tblOldDepColTitle);
    }
    //--- resets the DepositSubNoAccInfo Table
    public void resetDepSubNoTbl() {
        tblDepSubNoAccInfo.setDataArrayList(null, tblDepSubNoColTitle);
    }

    public void resetRenewalDepSubNoTbl() {
        tblRenewalDepSubNoAccInfo.setDataArrayList(null, tblRenewalDepSubNoColTitle);
    }

    public void resetExtensionDepSubNoTbl() {
        tblExtensionDepSubNoAccInfo.setDataArrayList(null, tblExtensionDepSubNoColTitle);
    }
    //--- reset the DepositSubNoAccInfo Tab

    public void resetDepSubNo() {
        setLblValDepositSubNo("");
        setTxtPeriodOfDeposit_Years("");
        setTxtPeriodOfDeposit_Months("");
        setTxtPeriodOfDeposit_Days("");
        setTdtMaturityDate("");
        setTdtDateOfDeposit("");
        setTdtPaymentDate("");
        setTxtRateOfInterest("");
        setTxtMaturityAmount("");
        setTxtTotalInterestAmount("");
        setTxtPeriodicInterestAmount("");
        setCboInterestPaymentMode("");
        setCboInterestPaymentFrequency("");
        setCboPaymentType("");
        setTdtPaymentDate("");
        setCboInstallmentAmount("");
        setTxtDepositAmount("");
        setCboProdType("");
        setCboProdId("");
        //        setCustomerIdCr("");
        setCustomerNameCrValue("");
        setCalenderFreqDate("");
        setCboCalenderFreq("");
        setNextInterestApplDate(null);
       
    }
//    added by nikhil

    public boolean checkAcNoWithoutProdType(String actNum) {
        HashMap mapData = new HashMap();
        boolean isExists = false;
        try {//dont delete chck selectalldao
            mapData.put("ACT_NUM", actNum);
            List mapDataList = ClientUtil.executeQuery("getActNumFromAllProducts", mapData);
            //system.out.println("#### mapDataList :"+mapDataList);
            if (mapDataList != null && mapDataList.size() > 0) {
                mapData = (HashMap) mapDataList.get(0);
                setCustomerIdCr(CommonUtil.convertObjToStr(mapData.get("ACT_NUM")));
                cbmProdType.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                setProdType(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                setCbmProdId(getProdType());
                cbmProdId.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_ID")));
                isExists = true;
            } else {
                isExists = false;
            }
            mapDataList = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapData = null;
        return isExists;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
        setChanged();
    }

    public void setAccountName(String AccountNo, String prodType) {
        HashMap resultMap = new HashMap();
        final HashMap accountNameMap = new HashMap();
        List resultList = new ArrayList();
        try {
            if (!prodType.equals("")) {
                accountNameMap.put("ACC_NUM", AccountNo);
                String pID = !prodType.equals("GL") ? getCbmProdId().getKeyForSelected().toString() : "";
                if (prodType.equals("GL") && getTxtAccNo().length() > 0) {
                    resultList = ClientUtil.executeQuery("getAccountNumberNameTL", accountNameMap);
                } else {
                    resultList = ClientUtil.executeQuery("getAccountNumberName" + this.getProdType(), accountNameMap);
                }
                if (resultList != null && resultList.size() > 0) {
                    if (!prodType.equals("GL")) {
                        HashMap dataMap = new HashMap();
                        accountNameMap.put("BRANCH_ID", getSelectedBranchID());
                        List lst = (List) ClientUtil.executeQuery("getProdIdForActNo" + prodType, accountNameMap);
                        if (lst != null && lst.size() > 0) {
                            dataMap = (HashMap) lst.get(0);
                        }
                        if (dataMap.get("PROD_ID").equals(pID)) {
                            resultMap = (HashMap) resultList.get(0);
                        }
                    } else {
                        resultMap = (HashMap) resultList.get(0);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (resultMap.containsKey("CUSTOMER_NAME")) {
            //system.out.println("%^$%^$%^$%^inside here"+resultMap);
            setCustomerNameCrValue(resultMap.get("CUSTOMER_NAME").toString());
        } else {
            setCustomerNameCrValue("");
        }
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

    /**
     * To perform the appropriate operation
     */
    public void doAction(NomineeOB objNomineeOB) throws Exception {
        //If actionType such as NEW, EDIT, DELETE, then proceed
        TTException exception = null;
        try {
	        if (actionType != ClientConstants.ACTIONTYPE_CANCEL) {
	            //If actionType has got propervalue then doActionPerform, else throw error
	            if (getCommand() != null || getAuthorizeMap() != null) {
	                doActionPerform(objNomineeOB);
	            } else {
	                throw new TTException(objTermDepositRB.getString(TO_COMMAND_ERROR));
	            }
	        }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            e.printStackTrace();
            if (e instanceof TTException) {
                exception = (TTException) e;
            }
        }
        if (exception != null) {
            HashMap exceptionHashMap = exception.getExceptionHashMap();
            if (exceptionHashMap != null) {
                ArrayList list = (ArrayList) exceptionHashMap.get(CommonConstants.EXCEPTION_LIST);
                parseException.logException(exception, true);
            } else { // To Display Transaction No showing String message
                parseException.logException(exception, true);
            }
        }
    }

    public void resetRenewalDepSubNo() {
        setRenewaltxtPeriodOfDeposit_Years("");
        setRenewaltxtPeriodOfDeposit_Months("");
        setRenewaltxtPeriodOfDeposit_Days("");
        setRenewaltdtMaturityDate("");
        setRenewaltdtDateOfDeposit("");
        setRenewaltxtRateOfInterest("");
        setRenewaltxtDepositAmount("");
        setRenewaltxtMaturityAmount("");
        setRenewaltxtTotalInterestAmount("");
        setRenewaltxtPeriodicInterestAmount("");
        setCboRenewalInterestPaymentMode("");
        setCboRenewalInterestPaymentFrequency("");
        setRenewaltxtDepositAmount("");
        //        setRenewalValDepositSubNo("");
        setRenewaltxtRateOfInterest("");
        setRenewalcustomerNameCrValue("");
        setRenewalcustomerIdCr("");
        setCalenderFreqDate("");
        setCboCalenderFreq("");
        //        setCboRenewalProdId("");
        //        getCbmRenewalProdId().setKeyForSelected("");
        setCboRenewalProdType("");
    }

    public void resetExtensionDepSubNo() {
        setExtensiontxtPeriodOfDeposit_Years("");
        setExtensiontxtPeriodOfDeposit_Months("");
        setExtensiontxtPeriodOfDeposit_Days("");
        setExtensiontdtMaturityDate("");
        setExtensiontdtDateOfDeposit("");
        setExtensiontxtRateOfInterest("");
        setExtensiontxtDepositAmount("");
        setExtensiontxtMaturityAmount("");
        setExtensiontxtTotalInterestAmount("");
        setExtensiontxtPeriodicInterestAmount("");
        setCboExtensionPaymentMode("");
        setCboExtensionPaymentFrequency("");
        setExtensiontxtDepositAmount("");
        //        setRenewalValDepositSubNo("");
        //        setRenewalcustomerNameCrValue("");
        setExtensioncustomerIdCr("");
        setCalenderFreqDate("");
        setCboCalenderFreq("");
        setCboRenewalProdId("");
        setCboRenewalProdType("");
    }

    /* To set Account Transfer data in the Transfer Object*/
    public AccInfoTO setAccInfoData() {
        objAccInfoTO = new AccInfoTO();
        try {
            /* Sets the Authroized signatory to "Y" if it is checked
             else it assigns "N" */
            if (getChkAuthorizedSignatory() == true) {
                objAccInfoTO.setAuthorizedSignatory(YES_STR);
            } else {
                objAccInfoTO.setAuthorizedSignatory(NO_STR);
            }

            objAccInfoTO.setCommAddress(CommonUtil.convertObjToStr(cbmCommunicationAddress.getKeyForSelected()));
            objAccInfoTO.setCommand(getCommand());
            objAccInfoTO.setCustId(this.getTxtCustomerId());
            /* If it is INSERT or RENEW, DEPOSIT NO. will take Generated Code
             else it will take code which is in textbox */
            if (this.getCommand().equals(CommonConstants.TOSTATUS_INSERT) || getCommand().equals(CommonConstants.TOSTATUS_RENEW)) {
                if (this.getTxtDepsoitNo().equals("") || this.getTxtDepsoitNo().equals(RENEW)) {
                    objAccInfoTO.setDepositNo(null);
                } else {
                    objAccInfoTO.setDepositNo(getTxtDepsoitNo());
                }
            } else {
                objAccInfoTO.setDepositNo(getTxtDepsoitNo());                
            }
            System.out.println("DepositNo : "+objAccInfoTO.getDepositNo());
            System.out.println("DepositStatus : "+objAccInfoTO.getDepositStatus());
            /* Sets the Renewal Count for RENEWED Deposits */
            if (getCommand().equals(CommonConstants.TOSTATUS_RENEW)) {
                objAccInfoTO.setRenewalFromDeposit(getLblValRenewDep());
                //                objAccInfoTO.setRenewalFromDeposit(getTxtDepsoitNo());
                objAccInfoTO.setOpeningMode("Renewal");
                objAccInfoTO.setDepositNo(getTxtDepsoitNo());
                objAccInfoTO.setRenewalCount(CommonUtil.convertObjToDouble(String.valueOf(renewalCount.doubleValue() + 1)));
            }
            if (getCommand().equals(CommonConstants.TOSTATUS_EXTENSION)) {
                objAccInfoTO.setRenewalFromDeposit(getLblValRenewDep());
                objAccInfoTO.setOpeningMode("Extension");
                objAccInfoTO.setDepositNo(getTxtDepsoitNo());
                //                objAccInfoTO.setRenewalCount(CommonUtil.convertObjToDouble(String.valueOf(renewalCount.doubleValue()+1)));
            }
            /* Sets the 15H Declarations to "Y" if it is checked
             else it assigns "N" */
            if (getChk15hDeclarations() == true) {
                objAccInfoTO.setFifteenhDeclare(YES_STR);
            } else {
                objAccInfoTO.setFifteenhDeclare(NO_STR);
            }
            /* Sets the Nominee Details to "Y" if it is checked
             else it assigns "N" */
            if (getChkNomineeDetails() == true) {
                objAccInfoTO.setNomineeDetails(YES_STR);
            } else {
                objAccInfoTO.setNomineeDetails(NO_STR);
            }
            /* Sets the Opening Mode to Normal if "Normal" is selected,
             else it assigns "TransferIn" */
            if (getRdoOpeningMode_Normal() == true) {
                objAccInfoTO.setOpeningMode(NORMAL);
                objAccInfoTO.setRenewalFromDeposit(null);
                objAccInfoTO.setTransOut("N");
            } else if (getRdoOpeningMode_TransferIn() == true) {
                objAccInfoTO.setOpeningMode(TRANSFER_IN);
                objAccInfoTO.setRenewalFromDeposit(getLblValRenewDep());
                objAccInfoTO.setTransOut("Y");
            } else if (getRdoOpeningMode_Renewal() == true) {
                objAccInfoTO.setOpeningMode("Renewal");
                objAccInfoTO.setTransOut("NR");
            } else if (getRdoOpeningMode_Extension() == true) {
                objAccInfoTO.setOpeningMode("Extension");
                objAccInfoTO.setTransOut("E");
            }

            if (getRdoDeathClaim_Yes() == true) {
                objAccInfoTO.setDeathClaim("Y");
            } else {
                objAccInfoTO.setDeathClaim("N");
            }
            if (getRdoAutoRenewal_Yes() == true) {
                objAccInfoTO.setAutoRenewal("Y");
            } else {
                objAccInfoTO.setAutoRenewal("N");
            }
            if (getRdowithIntRenewal_Yes() == true) {
                objAccInfoTO.setRenewWithInt("Y");
            } else {
                objAccInfoTO.setRenewWithInt("N");
            }
            if (getRdoMatAlertReport_Yes() == true) {
                objAccInfoTO.setMatAlertRep("Y");
            } else {
                objAccInfoTO.setMatAlertRep("N");
            }
            if (getRdoStandingInstruction_Yes() == true) {
                objAccInfoTO.setStandingInstruct("Y");
            } else {
                objAccInfoTO.setStandingInstruct("N");
            }
            objAccInfoTO.setPanNumber(this.getTxtPanNumber());
            /* Sets the PoA to "Y" if it is checked,
             else it assigns "N" */
            if (getChkPowerOfAttorney() == true) {
                objAccInfoTO.setPoa(YES_STR);
            } else {
                objAccInfoTO.setPoa(NO_STR);
            }
            objAccInfoTO.setAgentId(getTxtAgentId());
            objAccInfoTO.setProdId(CommonUtil.convertObjToStr(cbmProductId.getKeyForSelected()));
            objAccInfoTO.setRemarks(this.getTxtRemarks());
            objAccInfoTO.setSettlementMode(CommonUtil.convertObjToStr(cbmSettlementMode.getKeyForSelected()));
            objAccInfoTO.setConstitution(CommonUtil.convertObjToStr(cbmConstitution.getKeyForSelected()));
            objAccInfoTO.setAddressType(CommonUtil.convertObjToStr(cbmAddressType.getKeyForSelected()));
            objAccInfoTO.setCategory(CommonUtil.convertObjToStr(cbmCategory.getKeyForSelected()));
            objAccInfoTO.setCustType(CommonUtil.convertObjToStr(cbmMemberType.getKeyForSelected()));
//            objAccInfoTO.setMdsGroup(CommonUtil.convertObjToStr(txtMdsGroup));
//            objAccInfoTO.setMdsRemarks(CommonUtil.convertObjToStr(txaMdsRemarks));           
             objAccInfoTO.setMdsGroup(CommonUtil.convertObjToStr(mdsGroup));
            objAccInfoTO.setMdsRemarks(CommonUtil.convertObjToStr(mdsRemarks));
            objAccInfoTO.setDepositGroup(CommonUtil.convertObjToStr(depositGroup));
            objAccInfoTO.setBranchId(getSelectedBranchID());
            objAccInfoTO.setCreatedBy(TrueTransactMain.USER_ID);
            objAccInfoTO.setCreatedDt(currDt);
            objAccInfoTO.setStatusBy(TrueTransactMain.USER_ID);
            objAccInfoTO.setStatus(CommonConstants.STATUS_CREATED);
            objAccInfoTO.setStatusDt(currDt);
            objAccInfoTO.setInitiatedBranch(ProxyParameters.BRANCH_ID);
            objAccInfoTO.setPrintingNo(CommonUtil.convertObjToInt(getPrinting_No()));
            objAccInfoTO.setReferenceNo(getReferenceNo());
            /* Sets the Member type to "Y" if it is checked,
             else it assigns "N" */
            if (getChkMember() == true) {
                objAccInfoTO.setMember(YES_STR);
            } else {
                objAccInfoTO.setMember(NO_STR);
            }
            /* Sets the Tax Deductions to "Y" if it is checked,
             else it assigns "N" */
            if (getChkTaxDeductions() == true) {
                objAccInfoTO.setTaxDeductions(YES_STR);
            } else {
                objAccInfoTO.setTaxDeductions(NO_STR);
            }
            if (getChkAccZeroBalYN() == true) {
                objAccInfoTO.setAccZeroBalYN(YES_STR);
            } else {
                objAccInfoTO.setAccZeroBalYN(NO_STR);
            }
            //Added by sreekrishnan
            //objAccInfoTO.setIntroducer(getCboIntroducer());
            objAccInfoTO.setIntroducer(getTxtDealer());//Added By Revathi.L
            System.out.println("@##@$@#$@#$@#$@#$#@$"+cbmIntroducer.getKeyForSelected());
        } catch (Exception e) {
            parseException.logException(e, true);
        }
        return objAccInfoTO;
    }

    /* To set TransferIn data in the Transfer Object*/
    public TransferInTO setTransInData() {
        objTransferInTO = new TransferInTO();
        try {
            objTransferInTO.setBalanceIntPayable(CommonUtil.convertObjToDouble(this.getLblValBalanceInterestPayable()));
            objTransferInTO.setCommand(getCommand());
            objTransferInTO.setDepositNo(this.getTxtDepsoitNo());
            objTransferInTO.setIntPaid(CommonUtil.convertObjToDouble(this.getTxtInterestPaid()));
            objTransferInTO.setIntProvAmt(CommonUtil.convertObjToDouble(this.getTxtInterestProvidedAmount()));
            objTransferInTO.setInterbranchTransNo(this.getTxtInterBranchTransferNo());
            objTransferInTO.setLastInstallRecdt(DateUtil.getDateMMDDYYYY(this.getTdtLastInstallmentReceivedDate()));
            objTransferInTO.setLastIntcalcDt(DateUtil.getDateMMDDYYYY(this.getTdtLastInterestCalculatedDate()));
            objTransferInTO.setLastTdsCollected(CommonUtil.convertObjToDouble(this.getTxtLastTdsCollected()));
            objTransferInTO.setOriginalAcNumber(this.getTxtOriginalAccountNumber());
            objTransferInTO.setOriginalDepositDt(DateUtil.getDateMMDDYYYY(this.getTdtOriginalDateOfDeposit()));
            objTransferInTO.setPrintedFdr(CommonUtil.convertObjToDouble(this.getTxtPrintedNoOfTheFdr()));
            objTransferInTO.setTdsCollectedUpto(DateUtil.getDateMMDDYYYY(this.getTdtTdsCollectedUpto()));
            objTransferInTO.setTotInstallReceived(CommonUtil.convertObjToDouble(this.getTxtTotalInstallmentReceived()));
            objTransferInTO.setTotNoInstall(CommonUtil.convertObjToDouble(this.getTxtTotalNumberOfInstallments()));
            objTransferInTO.setTransAmt(CommonUtil.convertObjToDouble(this.getTxtTransferingAmount()));
            objTransferInTO.setTransBranchCode(this.getTxtTransferingBranchCode());
            objTransferInTO.setTransDt(DateUtil.getDateMMDDYYYY(this.getTdtDateOfTransfer()));
        } catch (Exception e) {
            parseException.logException(e, true);
        }
        return objTransferInTO;
    }

    /**
     * To perform the necessary action
     */
    private void doActionPerform(NomineeOB objNomineeOB) throws Exception {
        data = new HashMap();
        data.put("UI_PRODUCT_TYPE", SCREEN);
        if (getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
            data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
            data.put(CommonConstants.USER_ID, _authorizeMap.get(CommonConstants.USER_ID));
            List alist = getTranListforAuth();
            if (alist != null && alist.size() > 0) {
                TransactionTO objTransactionTO = (TransactionTO) alist.get(0);
                String ptype = CommonUtil.convertObjToStr(objTransactionTO.getProductType());
               
                if (ptype.equals("INV")) {
                    data.put("INV", "INV");
//                    transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,allowedTransactionDetailsTO);
//                    data.put("TransactionTO",transactionDetailsTO);


                }
            }
        } else {
            objAccInfoTO = setAccInfoData();
            objAccInfoTO.setCommand(getCommand());

            data.put(TERM_DEPOSIT_FOR_DAO, objAccInfoTO);
            data.put(DEP_SUB_NO_FOR_DAO, setDepSubNoAccInfoData());
            //This condition added for validation - to block deposits created with no records in deposit_sub_acinfo table in opening mode - KD-4242
            if(getCommand().equals(CommonConstants.TOSTATUS_INSERT) && objAccInfoTO.getOpeningMode().equals(NORMAL) && setDepSubNoAccInfoData().size() == 0){
                ClientUtil.showAlertWindow("Found Some Technical Errors in Data. Please Redo the process after closing the screen!!!!");
                resetForm();
                return;
            }
            if (getTransactionMap() != null) {
                data.put("Transaction Details Data", getTransactionMap());
            }
            depSubNoTOMap = null;
            //--- puts the data if the Opening mode is TransferIn
            if (objAccInfoTO.getOpeningMode().equals(TRANSFER_IN)) {
                objTransferInTO = setTransInData();
                objTransferInTO.setCommand(getCommand());
                data.put(TRANSFER_IN_DAO, objTransferInTO);
                objTransferInTO = null;
            }
            //sb interest calculation is stored in this map....
            System.out.println("getLblValRenewDep()  :: " + getLblValRenewDep());
            System.out.println("this.getTxtDepsoitNo() :: " + this.getTxtDepsoitNo());
            System.out.println("objAccInfoTO.getOpeningMode() :: " + objAccInfoTO.getOpeningMode());
            System.out.println("renewaldepSubNoAll :: " + renewaldepSubNoAll);
            if (this.getTxtDepsoitNo().equals(RENEW) || objAccInfoTO.getOpeningMode().equals(RENEWAL) && renewaldepSubNoAll != null) {
                HashMap renewalMap = new HashMap();
                int depSubNoSize = renewaldepSubNoAll.size();
                HashMap renewaldepSubNoAccInfo = new HashMap();
                for (int i = 0; i < depSubNoSize; i++) {
                    renewaldepSubNoAccInfo = (HashMap) renewaldepSubNoAll.get(String.valueOf(i));
                    renewalMap.put("RENEWAL_DEPOSIT_DT", renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_NO_DEP_DT));
                    renewalMap.put("RENEWAL_MATURITY_DT", renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_NO_MAT_DT));
                    renewalMap.put("RENEWAL_DEPOSIT_AMT", renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_NO_DEP_AMT));
                    renewalMap.put("RENEWAL_MATURITY_AMT", CommonUtil.convertObjToInt(renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_NO_MAT_AMT)));
                    renewalMap.put("RENEWAL_DEPOSIT_DAYS", CommonUtil.convertObjToInt(renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_NO_DEP_PER_DD)));
                    renewalMap.put("RENEWAL_DEPOSIT_MONTHS", CommonUtil.convertObjToInt(renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_NO_DEP_PER_MM)));
                    renewalMap.put("RENEWAL_DEPOSIT_YEARS", CommonUtil.convertObjToInt(renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_NO_DEP_PER_YY)));
                    renewalMap.put("RENEWAL_RATE_OF_INT", renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_NO_RATE_OF_INT));
                    renewalMap.put("RENEWAL_PERIODIC_INT", renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_NO_PER_INT_AMT));
                    renewalMap.put("RENEWAL_TOT_INTAMT", renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_NO_TOT_INT_AMT));
                    HashMap sameMap = new HashMap();
                    String oldBehaves = "";
                    String oldProdId = CommonUtil.convertObjToStr(objAccInfoTO.getProdId());
                    sameMap.put("PROD_ID", oldProdId);
                    List lstProd = ClientUtil.executeQuery("getBehavesLikeForDeposit", sameMap);
                    if (lstProd != null && lstProd.size() > 0) {
                        sameMap = (HashMap) lstProd.get(0);
                        //system.out.println("****** getBehavesLikeForDeposit : "+sameMap);
                        oldBehaves = CommonUtil.convertObjToStr(sameMap.get("BEHAVES_LIKE"));
                    }
                    String newProdId = null;
                    String newBehaves = "";
                    HashMap oldMap = new HashMap();
                    oldMap.put("PROD_ID", cbmRenewalDepositProdId.getKeyForSelected());
                    lstProd = ClientUtil.executeQuery("getBehavesLikeForDeposit", oldMap);
                    if (lstProd != null && lstProd.size() > 0) {
                        oldMap = (HashMap) lstProd.get(0);
                        //system.out.println("****** getBehavesLikeForDeposit : "+sameMap);
                        newBehaves = CommonUtil.convertObjToStr(oldMap.get("BEHAVES_LIKE"));
                        newProdId = CommonUtil.convertObjToStr(cbmRenewalDepositProdId.getKeyForSelected());
                    }
                    boolean noFlag = false;
                    HashMap sameNoMap = new HashMap();
                    sameNoMap.put("DEPOSIT NO", getLblValRenewDep());
                    List lstRenew = ClientUtil.executeQuery("getRenewalCountStartingDep", sameNoMap);
                    if (lstRenew != null && lstRenew.size() > 0) {
                        sameNoMap = (HashMap) lstRenew.get(0);
                        double renewalCount = CommonUtil.convertObjToDouble(sameNoMap.get("RENEWAL_COUNT")).doubleValue();
                        HashMap productMap = new HashMap();
                        productMap.put("PROD_ID", sameNoMap.get("PROD_ID"));
                        lstRenew = ClientUtil.executeQuery("getMaxNoSameNoAllowed", productMap);
                        if (lstRenew != null && lstRenew.size() > 0) {
                            productMap = (HashMap) lstRenew.get(0);
                            double productCount = CommonUtil.convertObjToDouble(productMap.get("MAX_NO_SAME_RENEWAL")).doubleValue();
                            if (oldBehaves.equals(newBehaves) && newBehaves.equals("FIXED") && oldProdId.equals(newProdId)
                                    && productMap.get("SAME_NO_ALLOWED") != null && productMap.get("SAME_NO_ALLOWED").equals("Y")
                                    && (renewalCount + 1) > productCount) {
                                noFlag = true;
                            }


                        } else {
                            if (getRenewalCountWithScreen().equals("SAME_NO")) {
                                noFlag = false;
                            } else {
                                noFlag = true;
                            }
                        }
                    }
                    if (getRdoRenewalWithdrawing_Yes() == true) {
                        if (renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_DEP_PAY_MODE) != null && renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_DEP_PAY_MODE).equals("CASH")) {
                            renewalMap.put("RENEWAL_DEP_TRANS_MODE", "CASH");
                            renewalMap.put("RENEWAL_DEP_TOKEN_NO", getTxtRenewalDepTransTokenNo());
                            //                            renewalMap.put("RENEWAL_DEP_TOKEN_NO", renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_WITHDRAWING_DEPAMT_TOKENO));
                        } else if (renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_DEP_PAY_MODE) != null && renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_DEP_PAY_MODE).equals("TRANSFER")) {
                            renewalMap.put("RENEWAL_DEP_TOKEN_NO", getTxtRenewalDepTransTokenNo());
                            renewalMap.put("RENEWAL_DEP_TRANS_MODE", "TRANSFER");
                            renewalMap.put("RENEWAL_DEP_TRANS_PRODTYPE", CommonUtil.convertObjToStr(cbmRenewalDepTransProdType.getKeyForSelected()));
                            renewalMap.put("RENEWAL_DEP_TRANS_PRODID", CommonUtil.convertObjToStr(cbmRenewalDepTransProdId.getKeyForSelected()));
                            renewalMap.put("RENEWAL_DEP_TRANS_ACCNO", renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_DEP_ACCT_NUM));
                        }
                        renewalMap.put("WITDRAWING_DEP_AMT", renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_WITHDRAWING_DEPAMT));
                        renewalMap.put("RENEWAL_DEP_WITHDRAWING", "YES");
                        noFlag = true;
                    } else if (getRdoRenewalWithdrawing_No() == true) {
                        renewalMap.put("RENEWAL_DEP_WITHDRAWING", "NO");
                    }
                    if (getRdoRenewalAdding_Yes() == true) {
//                        changed by nikhil
                        if (renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_DEP_PAY_MODE) != null && renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_DEP_PAY_MODE).equals("CASH")) {
                            renewalMap.put("RENEWAL_DEP_ADD_TRANS_MODE", "CASH");
                        } else if (renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_DEP_PAY_MODE) != null && renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_DEP_PAY_MODE).equals("TRANSFER")) {
//                            renewalMap.put("RENEWAL_DEP_TOKEN_NO", getTxtRenewalDepTransTokenNo());
                            renewalMap.put("RENEWAL_DEP_ADD_TRANS_MODE", "TRANSFER");
                            renewalMap.put("RENEWAL_DEP_ADD_TRANS_PRODTYPE", CommonUtil.convertObjToStr(cbmRenewalDepTransProdType.getKeyForSelected()));
                            renewalMap.put("RENEWAL_DEP_ADD_TRANS_PRODID", CommonUtil.convertObjToStr(cbmRenewalDepTransProdId.getKeyForSelected()));
                            renewalMap.put("RENEWAL_DEP_ADD_TRANS_ACCNO", renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_DEP_ACCT_NUM));
                            HashMap accountNameMap = new HashMap();
                            if (renewalMap.get("RENEWAL_DEP_TRANS_ACCNO") != null) {
                                accountNameMap.put("ACC_NUM", CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TRANS_ACCNO")));
                                if (!renewalMap.get("RENEWAL_DEP_TRANS_PRODTYPE").equals("") && !renewalMap.get("RENEWAL_DEP_TRANS_PRODTYPE").equals("RM")) {
                                    final List resultList = ClientUtil.executeQuery("getAccountNumberName" + renewalMap.get("RENEWAL_DEP_TRANS_PRODTYPE"), accountNameMap);
                                    if (resultList != null && resultList.size() > 0) {
                                        final HashMap resultMap = (HashMap) resultList.get(0);
                                        setRenewalcustomerNameCrValueDep(resultMap.get("CUSTOMER_NAME").toString());
//                                        renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_DEP_CUST_NAME,resultMap.get("CUSTOMER_NAME").toString());
                                    }
                                }
                            }
                        }
                        renewalMap.put("ADDING_DEP_AMT", renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_ADDING_DEPAMT));
                        renewalMap.put("RENEWAL_DEP_ADDING", "YES");
                        noFlag = true;
                    } else if (getRdoRenewalAdding_No() == true) {
                        renewalMap.put("RENEWAL_DEP_ADDING", "NO");
                        if (noFlag == false && (oldBehaves.equals(newBehaves) && (newBehaves.equals("FIXED") || newBehaves.equals("CUMMULATIVE"))) && oldProdId.equals(newProdId)) {
                            data.put("SAME_DEPOSIT_NO", "");
                        }
                    }
                    if (getRdoRenewalWithdrawingInt_Yes() == true) {
                        renewalMap.put("RENEWAL_INT_PAYABLE", "Y");
                        if (renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_INT_PAY_MODE) != null && renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_INT_PAY_MODE).equals("CASH")) {
                            renewalMap.put("RENEWAL_INT_TRANS_MODE", "CASH");
                            //                            renewalMap.put("RENEWAL_INT_TOKEN_NO", renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_WITHDRAWING_INTAMT_TOKENO));
                            renewalMap.put("RENEWAL_INT_TOKEN_NO", getTxtRenewalIntTokenNoVal());
                        } else if (renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_INT_PAY_MODE) != null && renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_INT_PAY_MODE).equals("TRANSFER")) {
                            renewalMap.put("RENEWAL_INT_TOKEN_NO", getTxtRenewalIntTokenNoVal());
                            renewalMap.put("RENEWAL_INT_TRANS_MODE", "TRANSFER");
                            renewalMap.put("RENEWAL_INT_TRANS_PRODTYPE", CommonUtil.convertObjToStr(cbmRenewalInterestTransProdType.getKeyForSelected()));
                            renewalMap.put("RENEWAL_INT_TRANS_PRODID", CommonUtil.convertObjToStr(cbmRenewalInterestTransProdId.getKeyForSelected()));
                            renewalMap.put("RENEWAL_INT_TRANS_ACCNO", renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_INT_ACCT_NUM));
                        } else if (CommonUtil.convertObjToStr(cbmRenewalDepTransProdType.getKeyForSelected()).equals("RM")) {
                            renewalMap.put("RENEWAL_INT_TRANS_MODE", "TRANSFER");
                        }
                        renewalMap.put("WITHDRAWING_INT_AMT", renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_WITHDRAWING_INTAMT));
                        renewalMap.put("RENEWAL_INT_WITHDRAWING", "YES");

                        if (noFlag == false && (oldBehaves.equals(newBehaves) && (newBehaves.equals("FIXED") || newBehaves.equals("CUMMULATIVE"))) && oldProdId.equals(newProdId)) {
                            data.put("SAME_DEPOSIT_NO", "");
                        }

                    } else if (getRdoRenewalWithdrawingInt_No() == true) {
                        renewalMap.put("RENEWAL_INT_WITHDRAWING", "NO");

                        //system.out.println("noFlag"+noFlag);
                        if (noFlag == false && (oldBehaves.equals(newBehaves) && (newBehaves.equals("FIXED") || newBehaves.equals("CUMMULATIVE"))) && oldProdId.equals(newProdId)) {
                            data.put("SAME_DEPOSIT_NO", "");
                        }
                        if (isChkRenewinterest() == true && data.containsKey("SAME_DEPOSIT_NO")) {
                            renewalMap.put("RENEWAL_INT_PAYABLE", "N");
                        } else {
                            renewalMap.put("RENEWAL_INT_PAYABLE", "Y");
                        }
                    }
                    if (oldBehaves.equals(newBehaves) && newBehaves.equals("FIXED")) {
                        if (getRdoRenewalCalenderFreq_Yes() == true) {
                            renewalMap.put("RENEWAL_CALENDER_FREQ", "Y");
                            renewalMap.put("RENEWAL_CALENDER_FREQ_DAY", CommonUtil.convertObjToStr(cbmRenewalCalenderFreqDay.getKeyForSelected()));
                        } else if (getRdoRenewalCalenderFreq_No() == true) {
                            renewalMap.put("RENEWAL_CALENDER_FREQ", "N");
                            renewalMap.put("RENEWAL_CALENDER_FREQ_DAY", String.valueOf(""));
                        }
                    } else {
                        renewalMap.put("RENEWAL_CALENDER_FREQ", "N");
                        renewalMap.put("RENEWAL_CALENDER_FREQ_DAY", String.valueOf(""));
                    }
//                    renewalMap.put("BALANCE_INT_AMT",renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_BALANCE_INTAMT));
                    renewalMap.put("PREV_INT_AMT", CommonUtil.convertObjToStr(getPreBalIntVal()));
                    renewalMap.put("BALANCE_INT_AMT", CommonUtil.convertObjToStr(getRenewalBalIntAmtVal()));
                    renewalMap.put("SB_INT_AMT", CommonUtil.convertObjToDouble(renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_SB_INTAMT)));
                    renewalMap.put("SB_PERIOD_RUN", CommonUtil.convertObjToInt(renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_SB_PERIOD)));
                    renewalMap.put("RENEWAL_PRODID", CommonUtil.convertObjToStr(cbmRenewalDepositProdId.getKeyForSelected()));
                    renewalMap.put("RENEWAL_CATEGORY", CommonUtil.convertObjToStr(cbmRenewalDepositCategory.getKeyForSelected()));
                    renewalMap.put("RENEWAL_PAY_FREQ", CommonUtil.convertObjToStr(getCbmRenewalInterestPaymentFrequency().getDataForKey(CommonUtil.convertObjToStr(renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_NO_INT_PAY_FREQ)))));
                    if (renewaldepSubNoAccInfo.containsKey(FLD_DEP_RENEWAL_SUB_PRINTINGNO) && renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_PRINTINGNO).equals("")) {
                        renewalMap.put("FLD_DEP_RENEWAL_SUB_PRINTINGNO", String.valueOf(""));
                    } else if (renewaldepSubNoAccInfo.containsKey(FLD_DEP_RENEWAL_SUB_PRINTINGNO) && renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_PRINTINGNO) != null) {
//                        changed by nikhil
                        renewalMap.put("FLD_DEP_RENEWAL_SUB_PRINTINGNO", renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_PRINTINGNO));
                    } else {
                        renewalMap.put("FLD_DEP_RENEWAL_SUB_PRINTINGNO", String.valueOf(""));
                    }

//                    else{
//                        renewalMap.put("FLD_DEP_RENEWAL_SUB_PRINTINGNO", renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_PRINTINGNO));
//                    }

                    if (getCommand() != null && getCommand().equals("UPDATE")) {
                        renewalMap.put("DEPOSIT_NO", getRenewalValDepositSubNo());
                    } else {
                        renewalMap.put("DEPOSIT_NO", getLblValRenewDep());
                    }
                    renewalMap.put("PENDING_AMT_RATE", getRenewalSBIntRateVal());
                    renewalMap.put("BRANCH_CODE", objAccInfoTO.getBranchId());
                    renewalMap.put(CommonConstants.INITIATED_BRANCH, objAccInfoTO.getInitiatedBranch());
                    if (renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_PAY_MODE) != null && renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_PAY_MODE).equals("TRANSFER")) {
                        renewalMap.put("RENEWAL_PAY_MODE", renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_PAY_MODE));
                        renewalMap.put("RENEWAL_PAY_PRODTYPE", renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_PROD_TYPE));
                        renewalMap.put("RENEWAL_PAY_PRODID", renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_PROD_ID));
                        renewalMap.put("RENEWAL_PAY_ACCNO", renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_AC_NO));
                    } else if (renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_PAY_MODE) != null && renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_PAY_MODE).equals("CASH")) {
                        renewalMap.put("RENEWAL_PAY_MODE", renewaldepSubNoAccInfo.get(FLD_DEP_RENEWAL_SUB_PAY_MODE));
                    } else {
                        renewalMap.put("RENEWAL_PAY_MODE", String.valueOf(""));
                        renewalMap.put("RENEWAL_PAY_PRODTYPE", String.valueOf(""));
                        renewalMap.put("RENEWAL_PAY_PRODID", String.valueOf(""));
                        renewalMap.put("RENEWAL_PAY_ACCNO", String.valueOf(""));
                        renewalMap.put("RENEWAL_PAY_MODE", String.valueOf(""));
                    }
                    renewalMap.put("USER_ID", ProxyParameters.USER_ID);
                    if (getRdoRenewalMatAlert_report_Yes() == true) {
                        renewalMap.put("RENEWAL_NOTICE", "Y");
                    } else {
                        renewalMap.put("RENEWAL_NOTICE", "N");
                    }
                    if (getRdoRenewalAutoRenewal_Yes() == true) {
                        renewalMap.put("AUTO_RENEWAL", "Y");
                        if (getRdoRenewalWith_intRenewal_Yes() == true) {
                            renewalMap.put("AUTO_RENEW_WITH", "Y");
                        } else {
                            renewalMap.put("AUTO_RENEW_WITH", "N");
                        }
                    } else {
                        renewalMap.put("AUTO_RENEWAL", "N");
                        renewalMap.put("AUTO_RENEW_WITH", "N");
                    }
                    renewalMap.put("RENEW_POSTAGE_AMT", getPostageRenewAmt());

                    //                    renewalMap.put("AUTO_RENEWAL", getLblAutoRenewalNewVal());
                    //                    renewalMap.put("AUTO_RENEW_WITH", getLblRenewalWithIntNewVal());
                    //                    renewalMap.put("MEMBER_TYPE", getLblMemberTypeRenewalNewVal());
                    //                    renewalMap.put("MEMBER_VALUE", getRenewalMemberTypeVal());
                    //                    renewalMap.put("TAX_DECLARE", getLblTaxDeclareRenewalNewVal());
                    //                    renewalMap.put("15H_DECLARE", getLbl15HDeclareRenewalNewVal());
                    //                                setLblRenewalNoticeNewVal(CommonUtil.convertObjToStr(newRenewMap.get("MAT_ALERT_REPORT")));
                    //                                setLblAutoRenewalNewVal(CommonUtil.convertObjToStr(newRenewMap.get("AUTO_RENEWAL")));
                    //                                setLblRenewalWithIntNewVal(CommonUtil.convertObjToStr(newRenewMap.get("RENEW_WITH_INT")));
                    //                                setLblMemberTypeRenewalNewVal(CommonUtil.convertObjToStr(newRenewMap.get("CUST_TYPE")));
                    //                                setRenewalMemberTypeVal(CommonUtil.convertObjToStr(newRenewMap.get("MEMBER")));
                    //                                if(newRenewMap.get("TAX_DEDUCTIONS")!=null && newRenewMap.get("TAX_DEDUCTIONS").equals("Y")){
                    //                                    setLblTaxDeclareRenewalNewVal(CommonUtil.convertObjToStr(newRenewMap.get("TAX_DEDUCTIONS")));
                    //                                }else{
                    //                                    setLbl15HDeclareRenewalNewVal(CommonUtil.convertObjToStr(newRenewMap.get("FIFTEENH_DECLARE")));
                    //                                }
                    //                    return;
                    renewalMap.put("OLD_DEPOSIT_DATE", getTdtDateOfDeposit());
                    if (getLastInterestPaidDateValue() != null && getLastInterestPaidDateValue().length() > 0) {
                        renewalMap.put("OLD_DEPOSIT_DATE", getLastInterestPaidDateValue());
                    }
                    renewalMap.put("OLD_MATURITY_DATE", getRenewalDepDate());
                }
                renewalMap.put("TDS_AMOUNT",getLblTDSAmount());
                renewalMap.put("TDS_AC_HD",getTdsAcHd());
                data.put("RENEWALMAP", renewalMap);
                renewalMap = null;
                renewaldepSubNoAccInfo = null;
                HashMap intMap = new HashMap();
                intMap.put("TRANS_MODE", getTransMode());
                if (addingFlag == true) {
                    intMap.put("INT", "ADD");
                } else {
                    intMap.put("INT", "INT");
                }
                intMap.put("SB_INT_AMT", String.valueOf(getSbIntAmount()));
                intMap.put("SB_PERIOD_RUN", String.valueOf(getSbPeriodRun()));
//                intMap.put("BAL_INT_AMT",String.valueOf(getBalanceInterestAmountValue()));
                intMap.put("BAL_INT_AMT", CommonUtil.convertObjToStr(getRenewalBalIntAmtVal()));
                intMap.put("ACT_NUM", getLblValRenewDep());
                intMap.put("ROI", getTxtRateOfInterest());
                intMap.put("CUST_ID", getTxtCustomerId());
                intMap.put("DEPOSIT_AMT", getTxtDepositAmount());
                data.put("INTMAP", intMap);
                intMap = null;
                //                HashMap sameMap = new HashMap();
                //                sameMap.put("ACT_NUM",getLblValRenewDep());
                //                List lst = ClientUtil.executeQuery("getOldDepositAmt", sameMap);
                //                if(lst!=null && lst.size()>0){
                //                    sameMap = (HashMap)lst.get(0);
                //                    String oldBehaves = null;
                //                    String newBehaves = null;
                //                    double oldAmt = CommonUtil.convertObjToDouble(sameMap.get("DEPOSIT_AMT")).doubleValue();
                //                    double sbAmt = getSbIntAmount();
                //                    double intAmt = CommonUtil.convertObjToDouble(getBalanceInterestAmountValue()).doubleValue();
                //                    oldAmt = oldAmt + sbAmt+intAmt;
                //                    double newAmt = getdepositAmount();
                //                    String oldProdId = CommonUtil.convertObjToStr(sameMap.get("PROD_ID"));
                //                    List lstProd = ClientUtil.executeQuery("getBehavesLikeForDeposit", sameMap);
                //                    if(lstProd!=null && lstProd.size()>0){
                //                        sameMap = (HashMap)lstProd.get(0);
                //                        //system.out.println("****** getBehavesLikeForDeposit : "+sameMap);
                //                        oldBehaves = CommonUtil.convertObjToStr(sameMap.get("BEHAVES_LIKE"));
                //                    }
                //                    String newProdId = null;
                //                    HashMap oldMap = new HashMap();
                //                    oldMap.put("PROD_ID",objAccInfoTO.getProdId());
                //                    lstProd = ClientUtil.executeQuery("getBehavesLikeForDeposit", oldMap);
                //                    if(lstProd!=null && lstProd.size()>0){
                //                        oldMap = (HashMap)lstProd.get(0);
                //                        //system.out.println("****** getBehavesLikeForDeposit : "+sameMap);
                //                        newBehaves = CommonUtil.convertObjToStr(oldMap.get("BEHAVES_LIKE"));
                //                        newProdId = CommonUtil.convertObjToStr(objAccInfoTO.getProdId());
                //                    }
                ////                    if(newBehaves.equals("FIXED") && oldBehaves.equals("FIXED") &&
                ////                    oldProdId.equals(newProdId) && withDrawFlag == false)// && oldAmt >= newAmt) && getAmtReduce().equals("WIT"))
                ////                        data.put("SAME_DEPOSIT_NO", "");
                //                }
            }
            if (this.getTxtDepsoitNo().equals(EXTENSION) || objAccInfoTO.getOpeningMode().equals(EXTENSION) && extensiondepSubNoAll != null) {
                HashMap extensionMap = new HashMap();
                int depSubNoSize = extensiondepSubNoAll.size();
                HashMap extensiondepSubNoAccInfo = new HashMap();
                for (int i = 0; i < depSubNoSize; i++) {
                    extensiondepSubNoAccInfo = (HashMap) extensiondepSubNoAll.get(String.valueOf(i));
                    extensionMap.put("EXTENSION_DEPOSIT_DT", extensiondepSubNoAccInfo.get(FLD_DEP_EXTENSION_SUB_NO_DEP_DT));
                    extensionMap.put("EXTENSION_MATURITY_DT", extensiondepSubNoAccInfo.get(FLD_DEP_EXTENSION_SUB_NO_MAT_DT));
                    extensionMap.put("EXTENSION_DEPOSIT_AMT", extensiondepSubNoAccInfo.get(FLD_DEP_EXTENSION_SUB_NO_DEP_AMT));
                    extensionMap.put("EXTENSION_MATURITY_AMT", extensiondepSubNoAccInfo.get(FLD_DEP_EXTENSION_SUB_NO_MAT_AMT));
                    extensionMap.put("EXTENSION_DEPOSIT_DAYS", extensiondepSubNoAccInfo.get(FLD_DEP_EXTENSION_SUB_NO_DEP_PER_DD));
                    extensionMap.put("EXTENSION_DEPOSIT_MONTHS", extensiondepSubNoAccInfo.get(FLD_DEP_EXTENSION_SUB_NO_DEP_PER_MM));
                    extensionMap.put("EXTENSION_DEPOSIT_YEARS", extensiondepSubNoAccInfo.get(FLD_DEP_EXTENSION_SUB_NO_DEP_PER_YY));
                    extensionMap.put("EXTENSION_RATE_OF_INT", extensiondepSubNoAccInfo.get(FLD_DEP_EXTENSION_SUB_NO_RATE_OF_INT));
                    extensionMap.put("EXTENSION_PERIODIC_INT", extensiondepSubNoAccInfo.get(FLD_DEP_EXTENSION_SUB_NO_PER_INT_AMT));
                    extensionMap.put("EXTENSION_TOT_INTAMT", extensiondepSubNoAccInfo.get(FLD_DEP_EXTENSION_SUB_NO_TOT_INT_AMT));
                    HashMap sameMap = new HashMap();
                    String oldBehaves = "";
                    String oldProdId = CommonUtil.convertObjToStr(objAccInfoTO.getProdId());
                    sameMap.put("PROD_ID", oldProdId);
                    List lstProd = ClientUtil.executeQuery("getBehavesLikeForDeposit", sameMap);
                    if (lstProd != null && lstProd.size() > 0) {
                        sameMap = (HashMap) lstProd.get(0);
                        //system.out.println("****** getBehavesLikeForDeposit : "+sameMap);
                        oldBehaves = CommonUtil.convertObjToStr(sameMap.get("BEHAVES_LIKE"));
                    }
                    String newProdId = null;
                    String newBehaves = "";
                    HashMap oldMap = new HashMap();
                    oldMap.put("PROD_ID", cbmExtensionDepositProdId.getKeyForSelected());
                    lstProd = ClientUtil.executeQuery("getBehavesLikeForDeposit", oldMap);
                    if (lstProd != null && lstProd.size() > 0) {
                        oldMap = (HashMap) lstProd.get(0);
                        //system.out.println("****** getBehavesLikeForDeposit : "+sameMap);
                        newBehaves = CommonUtil.convertObjToStr(oldMap.get("BEHAVES_LIKE"));
                        newProdId = CommonUtil.convertObjToStr(objAccInfoTO.getProdId());
                    }
                    boolean noFlag = false;
                    if (getRdoExtensionWithdrawing_Yes() == true) {
                        if (extensiondepSubNoAccInfo.get(FLD_DEP_EXTENSION_SUB_DEP_PAY_MODE) != null && extensiondepSubNoAccInfo.get(FLD_DEP_EXTENSION_SUB_DEP_PAY_MODE).equals("CASH")) {
                            extensionMap.put("EXTENSION_TRANS_MODE", "CASH");
                            extensionMap.put("EXTENSION_TOKEN_NO", getTxtExtensionTransTokenNo());
                        } else if (extensiondepSubNoAccInfo.get(FLD_DEP_EXTENSION_SUB_DEP_PAY_MODE) != null && extensiondepSubNoAccInfo.get(FLD_DEP_EXTENSION_SUB_DEP_PAY_MODE).equals("TRANSFER")) {
                            extensionMap.put("EXTENSION_TOKEN_NO", String.valueOf(""));
                            extensionMap.put("EXTENSION_TRANS_MODE", "TRANSFER");
                            extensionMap.put("EXTENSION_TRANS_PRODTYPE", CommonUtil.convertObjToStr(cbmExtensionTransProdType.getKeyForSelected()));
                            extensionMap.put("EXTENSION_TRANS_PRODID", CommonUtil.convertObjToStr(cbmExtensionTransProdId.getKeyForSelected()));
                            extensionMap.put("EXTENSION_TRANS_ACCNO", extensiondepSubNoAccInfo.get(FLD_DEP_EXTENSION_SUB_DEP_ACCT_NUM));
                        }
                        extensionMap.put("WITDRAWING_AMT", extensiondepSubNoAccInfo.get(FLD_DEP_EXTENSION_SUB_WITHDRAWING_DEPAMT));
                        extensionMap.put("EXTENSION_WITHDRAWING", "YES");
                        extensionMap.put("EXTENSION_ADDING", "NO");
                        noFlag = true;
                    } else if (getRdoExtensionWithdrawing_No() == true) {
                        extensionMap.put("EXTENSION_WITHDRAWING", "NO");
                    }
                    if (getRdoExtensionAdding_Yes() == true) {
                        extensionMap.put("ADDING_AMT", extensiondepSubNoAccInfo.get(FLD_DEP_EXTENSION_SUB_ADDING_DEPAMT));
                        extensionMap.put("EXTENSION_ADDING", "YES");
                        noFlag = true;
                    } else if (getRdoExtensionAdding_No() == true) {
                        extensionMap.put("EXTENSION_ADDING", "NO");
                    }
                    if (getRdoExtensionWithdrawingInt_Yes() == true) {
                        if (extensiondepSubNoAccInfo.get(FLD_DEP_EXTENSION_SUB_DEP_PAY_MODE) != null && extensiondepSubNoAccInfo.get(FLD_DEP_EXTENSION_SUB_DEP_PAY_MODE).equals("CASH")) {
                            extensionMap.put("EXTENSION_TRANS_MODE", "CASH");
                            extensionMap.put("EXTENSION_TOKEN_NO", getTxtExtensionTransTokenNo());
                        } else if (extensiondepSubNoAccInfo.get(FLD_DEP_EXTENSION_SUB_DEP_PAY_MODE) != null && extensiondepSubNoAccInfo.get(FLD_DEP_EXTENSION_SUB_DEP_PAY_MODE).equals("TRANSFER")) {
                            extensionMap.put("EXTENSION_TOKEN_NO", String.valueOf(""));
                            extensionMap.put("EXTENSION_TRANS_MODE", "TRANSFER");
                            extensionMap.put("EXTENSION_TRANS_PRODTYPE", CommonUtil.convertObjToStr(cbmExtensionTransProdType.getKeyForSelected()));
                            extensionMap.put("EXTENSION_TRANS_PRODID", CommonUtil.convertObjToStr(cbmExtensionTransProdId.getKeyForSelected()));
                            extensionMap.put("EXTENSION_TRANS_ACCNO", extensiondepSubNoAccInfo.get(FLD_DEP_EXTENSION_SUB_DEP_ACCT_NUM));
                        }
                        extensionMap.put("WITHDRAWING_INT_AMT", extensiondepSubNoAccInfo.get(FLD_DEP_EXTENSION_SUB_WITHDRAWING_INTAMT));
                        extensionMap.put("EXTENSION_INT_WITHDRAWING", "YES");
                    } else if (getRdoExtensionWithdrawingInt_No() == true) {
                        extensionMap.put("EXTENSION_INT_WITHDRAWING", "NO");
                        extensionMap.put("WITHDRAWING_INT_AMT", new Double(0.0));
                    }
                    if (oldBehaves.equals(newBehaves) && newBehaves.equals("FIXED")) {
                        if (getRdoExtensionCalenderFreq_Yes() == true) {
                            extensionMap.put("EXTENSION_CALENDER_FREQ", "Y");
                            extensionMap.put("EXTENSION_CALENDER_FREQ_DAY", CommonUtil.convertObjToStr(cbmExtensionCalenderFreqDay.getKeyForSelected()));
                        } else if (getRdoExtensionCalenderFreq_No() == true) {
                            extensionMap.put("EXTENSION_CALENDER_FREQ", "N");
                            extensionMap.put("EXTENSION_CALENDER_FREQ_DAY", String.valueOf(""));
                        }
                    } else {
                        extensionMap.put("EXTENSION_CALENDER_FREQ", "N");
                        extensionMap.put("EXTENSION_CALENDER_FREQ_DAY", String.valueOf(""));
                    }
                    extensionMap.put("BALANCE_INT_AMT", getExtensionBalInterestAmtVal());
                    extensionMap.put("WITHDRAW_AMT_CALC", getExtensionBalIntAmtVal());
                    extensionMap.put("BALANCE_AMT_CALC", getExtensionWithdrawIntAmtVal());
                    extensionMap.put("ALREADY_WITHDRAWN", getExtensionAlreadyWithdrawn());
                    extensionMap.put("ALREADY_CREDITED", getExtensionAlreadyCredited());
                    extensionMap.put("TOTAL_INT_AMOUNT", getExtensionTotalIntAmt());
                    extensionMap.put("TOTAL_AMOUNT", getExtensionBalanceIntValue());
                    extensionMap.put("EXTENSION_PRODID", CommonUtil.convertObjToStr(cbmExtensionDepositProdId.getKeyForSelected()));
                    extensionMap.put("EXTENSION_CATEGORY", CommonUtil.convertObjToStr(cbmExtensionDepositCategory.getKeyForSelected()));
                    if (extensiondepSubNoAccInfo.get(FLD_DEP_EXTENSION_SUB_NO_INT_PAY_FREQ) != null) {
                        extensionMap.put("EXTENSION_PAY_FREQ", extensiondepSubNoAccInfo.get(FLD_DEP_EXTENSION_SUB_NO_INT_PAY_FREQ));
                    }
                    if (extensiondepSubNoAccInfo.containsKey(FLD_DEP_EXTENSION_SUB_PRINTINGNO) && extensiondepSubNoAccInfo.get(FLD_DEP_EXTENSION_SUB_PRINTINGNO).equals("")) {
                        extensionMap.put("EXTENSION_PRINTINGNO", String.valueOf(""));
                    } else {
                        extensionMap.put("EXTENSION_PRINTINGNO", extensiondepSubNoAccInfo.get(FLD_DEP_EXTENSION_SUB_PRINTINGNO));
                    }
                    if (getCommand() != null && getCommand().equals("UPDATE")) {
                        extensionMap.put("OLD_DEPOSIT_NO", getRenewalValDepositSubNo());
                    } else {
                        extensionMap.put("OLD_DEPOSIT_NO", getValExtensionDep());
                    }
                    extensionMap.put("BRANCH_CODE", objAccInfoTO.getBranchId());
                    if (extensiondepSubNoAccInfo.get(FLD_DEP_EXTENSION_SUB_PAY_MODE) != null && extensiondepSubNoAccInfo.get(FLD_DEP_EXTENSION_SUB_PAY_MODE).equals("TRANSFER")) {
                        extensionMap.put("EXTENSION_PAY_MODE", extensiondepSubNoAccInfo.get(FLD_DEP_EXTENSION_SUB_PAY_MODE));
                        extensionMap.put("EXTENSION_PAY_PRODTYPE", CommonUtil.convertObjToStr(cbmExtensionPaymentProdType.getKeyForSelected()));
                        extensionMap.put("EXTENSION_PAY_PRODID", CommonUtil.convertObjToStr(cbmExtensionPaymentProdId.getKeyForSelected()));
                        extensionMap.put("EXTENSION_PAY_ACCNO", getExtensioncustomerIdCrInt());
                    } else if (extensiondepSubNoAccInfo.get(FLD_DEP_EXTENSION_SUB_PAY_MODE) != null && extensiondepSubNoAccInfo.get(FLD_DEP_EXTENSION_SUB_PAY_MODE).equals("CASH")) {
                        extensionMap.put("EXTENSION_PAY_MODE", extensiondepSubNoAccInfo.get(FLD_DEP_EXTENSION_SUB_PAY_MODE));
                    } else {
                        extensionMap.put("EXTENSION_PAY_MODE", String.valueOf(""));
                        extensionMap.put("EXTENSION_PAY_PRODTYPE", String.valueOf(""));
                        extensionMap.put("EXTENSION_PAY_PRODID", String.valueOf(""));
                        extensionMap.put("EXTENSION_PAY_ACCNO", String.valueOf(""));
                        extensionMap.put("EXTENSION_PAY_MODE", String.valueOf(""));
                    }
                    extensionMap.put("OLD_FREQUENCY", getExtensionPaymentFreqValue());
                    extensionMap.put("OLD_PERIOD_RUN", getExtensionActualPeriodRun());
                    extensionMap.put("OLD_RATE_OF_INT", getExtensionRateOfIntVal());
                    extensionMap.put("ORIGINAL_INT_AMT", getExtensionOriginalIntValue());
                    if (getRdoExtensionMatAlertReport_Yes() == true) {
                        extensionMap.put("EXTENSION_NOTICE", "Y");
                    } else {
                        extensionMap.put("EXTENSION_NOTICE", "N");
                    }
                    if (getRdoExtensionAutoRenewal_Yes() == true) {
                        extensionMap.put("EXTENSION_AUTO_RENEWAL", "Y");
                        if (getRdoExtensionWithIntAutoRenewal_Yes() == true) {
                            extensionMap.put("EXTENSION_AUTO_RENEW_WITH", "Y");
                        } else {
                            extensionMap.put("EXTENSION_AUTO_RENEW_WITH", "N");
                        }
                    } else {
                        extensionMap.put("EXTENSION_AUTO_RENEWAL", "N");
                        extensionMap.put("EXTENSION_AUTO_RENEW_WITH", "N");
                    }
                    extensionMap.put("USER_ID", ProxyParameters.USER_ID);
                    //                    return;
                }
                data.put("EXTENSIONMAP", extensionMap);
                extensionMap = null;
                extensiondepSubNoAccInfo = null;
            }
            //--- Puts the data if the Constitution is JointAccount
            if (objAccInfoTO.getConstitution().equals("JOINT_ACCOUNT")) {
                data.put(JOINT_ACCNT_FOR_DAO, setJointAccntData());
                jntAcctTOMap = null;
            }
            //--- Puts the data if the Authorized signatory is checked
            if (objAccInfoTO.getAuthorizedSignatory().equals(YES_STR)) {
                data.put(AUTH_SIGN_DAO, authorizedSignatoryOB.setAuthorizedSignatory());
                data.put(AUTH_SIGN_INST_DAO, authorizedSignatoryInstructionOB.setAuthorizedSignatoryInstruction());
            }

            //--- Puts the data if the Poa is checked
            if (objAccInfoTO.getPoa().equals(YES_STR)) {
                data.put(POA_FOR_DAO, powerOfAttorneyOB.setTermLoanPowerAttorney());
            }

            //--- puts the data if the NomineeDetails is checked
            if (objAccInfoTO.getNomineeDetails().equals(YES_STR)) {
                data.put("AccountNomineeTO", objNomineeOB.getNomimeeList());
                data.put("AccountNomineeDeleteTO", objNomineeOB.getDeleteNomimeeList());
            }
            //            if(objNomineeOB == null){
            //                HashMap intMap = new HashMap();
            //                intMap.put("TRANS_MODE",getTransMode());
            //                HashMap prodMap = new HashMap();
            ////                double intAmt = CommonUtil.convertObjToDouble(txtTotalInterestAmount.getText()).doubleValue();
            //                double intAmt = CommonUtil.convertObjToDouble(getTxtTotalInterestAmount()).doubleValue();
            //                String depositNo = CommonUtil.convertObjToStr(getLblValRenewDep());
            //                String subNo = CommonUtil.convertObjToStr(getLblValDepositSubNo());
            //                String accountNo = depositNo + "_" + subNo;
            //                //system.out.println("###accountNo : "+accountNo);
            //                intMap.put("INT_AMT",new Double(intAmt));
            //                intMap.put("PROD_ID",objAccInfoTO.getProdId());
            //                intMap.put("SELECTED_BRANCH",ProxyParameters.BRANCH_ID);
            //                intMap.put("ACCOUNT NO",accountNo);
            //                intMap.put("BRANCH_ID",ProxyParameters.BRANCH_ID);
            //                intMap.put("USER_ID",ProxyParameters.USER_ID);
            //                prodMap.put("PROD_ID",intMap.get("PROD_ID"));
            //                List lst = ClientUtil.executeQuery("getAccountHeadIdForDeposits", prodMap);
            //                if(lst.size()>0){
            //                    prodMap = (HashMap)lst.get(0);
            //                    intMap.put("AC_HD_ID",prodMap.get("INT_PAY"));
            //                }
            //                intMap.put("TRANSMODE",getTransMode());
            //                data.put("INTMAP",intMap);
            //                //system.out.println("******* intMap :"+data);
            //            }
            if (transactionDetailsTO == null) {
                transactionDetailsTO = new LinkedHashMap();
            }
            data.put(CommonConstants.MODULE, getModule());
            data.put(CommonConstants.SCREEN, getScreen());
            data.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
            transactionDetailsTO.put(NOT_DELETED_TRANS_TOs, allowedTransactionDetailsTO);
            data.put("TransactionTO", transactionDetailsTO);
            data.put("MODE", CommonConstants.TOSTATUS_INSERT);
        }
        //system.out.println("data : " + data);
//        HashMap proxyResultMap = null;
        if (data.containsKey("SAME_DEPOSIT_NO") && getActionType() == ClientConstants.ACTIONTYPE_RENEW) {
//            int confirm=ClientUtil.confirmationAlert("Do You Want to Renewal With Same No");
            if (getRenewalCountWithScreen().equals("TRUE")) {
                //system.out.println("remove remove");
                data.remove("SAME_DEPOSIT_NO");
                objAccInfoTO.setRenewalCount(new Double(objAccInfoTO.getRenewalCount().doubleValue() - 1));
            }
        }
        
        //  Added by nithya on 08-03.2016 for 0003920       
        final ThriftBenevolentAdditionalAmtTO objThriftBenevolentAdditionalAmtTO = setAdditionalAmountForDeposit();    
        if(productBehavesLike.equalsIgnoreCase(prodBehavesBenevolent) || productBehavesLike.equalsIgnoreCase(prodBehavesLikeThrift)){
            data.put("THRIFT_BENEVOLENT_ADDITIONAL_AMT",objThriftBenevolentAdditionalAmtTO);
        }
        if (getMobileBanking(objAccInfoTO) != null) {
            data.put("SMSSubscriptionTO",getMobileBanking(objAccInfoTO));
        }
       // End 
        
//        if(getRenewalCountWithScreen().equals("TRUE")){
//            objAccInfoTO.setRenewalCount(new Double(objAccInfoTO.getRenewalCount().doubleValue()-1));
//        }
        
        
        HashMap proxyResultMap = proxy.execute(data, operationMap);
        //        setResult(actionType);
        setResult(getActionType());

        System.out.println("proxyResultMap : " + proxyResultMap);
        if (proxyResultMap != null && proxyResultMap.containsKey(CommonConstants.TRANS_ID)) {
            HashMap behavesMap = new HashMap();
            behavesMap.put("ACT_NUM", proxyResultMap.get(CommonConstants.TRANS_ID));
            List lstBehaves = ClientUtil.executeQuery("getBehavesLikeForDepositNo", behavesMap);
            if (lstBehaves != null && lstBehaves.size() > 0) {
                behavesMap = (HashMap) lstBehaves.get(0);
                if (behavesMap.get("BEHAVES_LIKE").equals("RECURRING") && getRdoStandingInstruction_Yes() == true) {
                    ClientUtil.showMessageWindow("<html>" + objTermDepositRB.getString("lblDepositNo") + ": "
                            + CommonUtil.convertObjToStr(proxyResultMap.get(CommonConstants.TRANS_ID)) + " "
                            + "<br><br><b>ENTER STANDING INSTRUCTION...</b>" + "</html>");
                } else {
                    ClientUtil.showMessageWindow(objTermDepositRB.getString("lblDepositNo") + ": " + CommonUtil.convertObjToStr(proxyResultMap.get(CommonConstants.TRANS_ID)));
                }
            }
            //            ClientUtil.showMessageWindow(objTermDepositRB.getString("lblDepositNo") + ": " + CommonUtil.convertObjToStr(proxyResultMap.get(CommonConstants.TRANS_ID)));
        }
        if (proxyResultMap != null && proxyResultMap.containsKey("DEPOSIT NO")) {
            setTxtDepsoitNo(CommonUtil.convertObjToStr(proxyResultMap.get("DEPOSIT NO")));
            //system.out.println("#$%#%#$%etTxtDepsoitNo"+getTxtDepsoitNo());
        }
//                if(getCommand().equals(CommonConstants.TOSTATUS_RENEW)){
//                    HashMap transMap = new HashMap();
//                    List lst = null;
//                    if(getTransactionId()!= null){
//                        transMap.put("BATCH_ID",getTransactionId());//TransferTransaction id collecting from transfer_trans.....
//                        lst = ClientUtil.executeQuery("getDepForTransferDeposit", transMap);
//                    }else{
//                        transMap.put("TRANS_ID",getCashId());//cashTransaction id collecting from cash_tanstable.....
//                        lst = ClientUtil.executeQuery("getDepForCashDeposit", transMap);
//                    }
//                    if(lst.size()>0){
//                        transMap = (HashMap)lst.get(0);
//                        if(transMap.get("AUTHORIZE_STATUS") == null)
//                            ClientUtil.displayAlert("Already some Transaction is done go to authorize come back again....!");
//                    }
//                }
        if (proxyResultMap != null && proxyResultMap.containsKey("CASH_TRANSFER_ID")) {
            ClientUtil.showMessageWindow("Transaction No..." + CommonUtil.convertObjToStr(proxyResultMap.get("CASH_TRANSFER_ID")));
            setTransactionId(CommonUtil.convertObjToStr(proxyResultMap.get("CASH_TRANSFER_ID")));
            //system.out.println("######## : "+getTransactionId());
        }

        setProxyReturnMap(proxyResultMap);
        //        actionType = ClientConstants.ACTIONTYPE_CANCEL;
        //        resetForm();
        //        data=null;
    }

    //----To make all the TO's null
    protected void makeToNull() {
        depSubNoAll = null;
    }

    /* To set Depsosit Sub No Account Info data in the Transfer Object*/
    public HashMap setDepSubNoAccInfoData() {
        HashMap depSubNoAccInfoSingleRec;
        depSubNoTOMap = new LinkedHashMap();
        try {
            DepSubNoAccInfoTO objDepSubNoAccInfoTO;
            int depSubNoSize = depSubNoAll.size();
            for (int i = 0; i < depSubNoSize; i++) {
                depSubNoAccInfoSingleRec = (HashMap) depSubNoAll.get(String.valueOf(i));
                objDepSubNoAccInfoTO = new DepSubNoAccInfoTO();
                objDepSubNoAccInfoTO.setCommand(getCommand());
                objDepSubNoAccInfoTO.setAdtAmt(getAdtAmt());
                objDepSubNoAccInfoTO.setDepositAmt(CommonUtil.convertObjToDouble(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_NO_DEP_AMT)));
                objDepSubNoAccInfoTO.setDepositDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_NO_DEP_DT))));
                objDepSubNoAccInfoTO.setDepositNo(this.getTxtDepsoitNo());
                objDepSubNoAccInfoTO.setDepositPeriodDd(CommonUtil.convertObjToDouble(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_NO_DEP_PER_DD)));
                objDepSubNoAccInfoTO.setDepositPeriodMm(CommonUtil.convertObjToDouble(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_NO_DEP_PER_MM)));
                objDepSubNoAccInfoTO.setDepositPeriodYy(CommonUtil.convertObjToDouble(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_NO_DEP_PER_YY)));
                //mm  
                objDepSubNoAccInfoTO.setDepositPeriodWk(CommonUtil.convertObjToDouble(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_NO_DEP_PER_Wk)));
                
                objDepSubNoAccInfoTO.setDepositSubNo(CommonUtil.convertObjToInt(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_NO_DEP_SUB_NO)));
                objDepSubNoAccInfoTO.setDepositSubNo(CommonUtil.convertObjToInt("1"));
                objDepSubNoAccInfoTO.setIntpayFreq(CommonUtil.convertObjToDouble(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_NO_INT_PAY_FREQ)));
                objDepSubNoAccInfoTO.setIntpayMode(CommonUtil.convertObjToStr(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_NO_INT_PAY_MODE)));
                objDepSubNoAccInfoTO.setInstallType(CommonUtil.convertObjToStr(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_INST_TYPE)));
                objDepSubNoAccInfoTO.setPaymentType(CommonUtil.convertObjToStr(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_PAY_TYPE)));
                objDepSubNoAccInfoTO.setPaymentDay(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_NO_MAT_DT))));
                objDepSubNoAccInfoTO.setMaturityAmt(CommonUtil.convertObjToDouble(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_NO_MAT_AMT)));
                objDepSubNoAccInfoTO.setMaturityDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_NO_MAT_DT))));
                objDepSubNoAccInfoTO.setPeriodicIntAmt(CommonUtil.convertObjToDouble(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_NO_PER_INT_AMT)));
                objDepSubNoAccInfoTO.setRateOfInt(CommonUtil.convertObjToDouble(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_NO_RATE_OF_INT)));
                //                objDepSubNoAccInfoTO.setStatus(CommonUtil.convertObjToStr(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_NO_STATUS)));
                objDepSubNoAccInfoTO.setCreateBy(TrueTransactMain.USER_ID);
                objDepSubNoAccInfoTO.setSubstatusBy(TrueTransactMain.USER_ID);
                objDepSubNoAccInfoTO.setSubstatusDt(currDt);
                objDepSubNoAccInfoTO.setStatus(CommonUtil.convertObjToStr(CommonConstants.STATUS_CREATED));
                objDepSubNoAccInfoTO.setTotIntAmt(CommonUtil.convertObjToDouble(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_NO_TOT_INT_AMT)));
                objDepSubNoAccInfoTO.setIntPayProdId(CommonUtil.convertObjToStr(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_INT_PROD_ID)));
                objDepSubNoAccInfoTO.setIntPayProdType(CommonUtil.convertObjToStr(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_INT_PROD_TYPE)));
                objDepSubNoAccInfoTO.setIntPayAcNo(CommonUtil.convertObjToStr(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_INT_AC_NO)));
                if (depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_CALENDER_FREQ) != null && depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_CALENDER_FREQ).equals("Y")) {
                    objDepSubNoAccInfoTO.setCalender_freq(CommonUtil.convertObjToStr(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_CALENDER_FREQ)));
                    objDepSubNoAccInfoTO.setCalender_date(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_CALENDER_DATE))));
                    objDepSubNoAccInfoTO.setCalender_day(CommonUtil.convertObjToDouble(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_CALENDER_DAY)));
                } else {
                    objDepSubNoAccInfoTO.setCalender_freq("N");
                    objDepSubNoAccInfoTO.setCalender_date(DateUtil.getDateMMDDYYYY(""));
                    objDepSubNoAccInfoTO.setCalender_day(CommonUtil.convertObjToDouble(""));
                }
                if (getCommand().equals("RENEW")) {
                    objDepSubNoAccInfoTO.setFlexi_status("NR");
                } else {
                    objDepSubNoAccInfoTO.setFlexi_status("N");
                }
                if (getRdoOpeningMode_TransferIn() == true) {
                    objDepSubNoAccInfoTO.setTotalIntCredit(CommonUtil.convertObjToDouble(getTxtInterestProvidedAmount()));
                    objDepSubNoAccInfoTO.setTotalIntDrawn(CommonUtil.convertObjToDouble(getTxtInterestPaid()));
                }
                if (getSalaryRecoveryYes() == true) {
                    objDepSubNoAccInfoTO.setSalaryRecovery("Y");
                } else {
                    objDepSubNoAccInfoTO.setSalaryRecovery("N");
                }
                String productId = objAccInfoTO.getProdId();
                if (!objAccInfoTO.getCommand().equals("UPDATE")) {
                    objDepSubNoAccInfoTO.setAcctStatus("NEW");
                }
                objDepSubNoAccInfoTO.setPostageAmt(CommonUtil.convertObjToDouble(getPostageAmt()));
                objDepSubNoAccInfoTO.setRenewPostageAmt(CommonUtil.convertObjToDouble(getPostageRenewAmt()));
                if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                calculateNextAppDate(CommonUtil.convertObjToInt(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_NO_INT_PAY_FREQ)));
                objDepSubNoAccInfoTO.setNextIntAppDate(getNextInterestApplDate());
                }
                //system.out.println("############Status****** :" +objDepSubNoAccInfoTO.getAcctStatus());
                HashMap recurringMap = new HashMap();
                recurringMap.put("PROD_ID", productId);
                List lst = ClientUtil.executeQuery("getBehavesLikeForDeposit", recurringMap);
                if (lst.size() > 0) {
                    recurringMap = (HashMap) lst.get(0);
                    if (recurringMap.get("BEHAVES_LIKE").equals("RECURRING")) {
                        double period = CommonUtil.convertObjToInt(objDepSubNoAccInfoTO.getDepositPeriodYy());
                        double periodMm = 0.0;
                        if (period >= 1) {
                            period = period * 12.0;
                            periodMm = CommonUtil.convertObjToInt(objDepSubNoAccInfoTO.getDepositPeriodMm());
                            period = period + periodMm;
                        } else {
                            period = CommonUtil.convertObjToInt(objDepSubNoAccInfoTO.getDepositPeriodMm());
                        }

                        objDepSubNoAccInfoTO.setTotalInstallments(new Double(period));
                        if (objDepSubNoAccInfoTO.getDepositPeriodWk() != null && objDepSubNoAccInfoTO.getDepositPeriodWk() > 0) {
                            objDepSubNoAccInfoTO.setTotalInstallments(objDepSubNoAccInfoTO.getDepositPeriodWk());
                        }
                        // Added by nithya on 13/09/2017
                        int dep_freq =0;
                        HashMap chkMap = new HashMap();
                        chkMap.put("PID", productId);
                        List chklist = ClientUtil.executeQuery("getDailyDepositFrequency", chkMap);
                        if (chklist != null && chklist.size() > 0) {
                            HashMap sing = (HashMap) chklist.get(0);
                            if (sing != null && sing.containsKey("DEPOSIT_FREQ")) {
                                dep_freq = CommonUtil.convertObjToInt(sing.get("DEPOSIT_FREQ"));
                                if(dep_freq == 365){
                                    objDepSubNoAccInfoTO.setTotalInstallments(objDepSubNoAccInfoTO.getDepositPeriodYy());                                    
                                }
                            }
                        }
                    }
                    if (recurringMap.get("BEHAVES_LIKE").equals("DAILY")) {
                        double yearPeriod = CommonUtil.convertObjToDouble(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_NO_DEP_PER_YY)).doubleValue();
                        double monthPeriod = CommonUtil.convertObjToDouble(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_NO_DEP_PER_MM)).doubleValue();
                        if (yearPeriod > 1) {
                            yearPeriod = yearPeriod * 12;
                            yearPeriod = yearPeriod + monthPeriod;
                            objDepSubNoAccInfoTO.setDepositPeriodMm(CommonUtil.convertObjToDouble(new Double(yearPeriod)));
                            objDepSubNoAccInfoTO.setDepositPeriodYy(CommonUtil.convertObjToDouble(new Double(0.0)));
                        }
                    }
                }
                depSubNoTOMap.put(String.valueOf(i), objDepSubNoAccInfoTO);
                objDepSubNoAccInfoTO = null;
                depSubNoAccInfoSingleRec = null;
            }
        } catch (Exception e) {
            parseException.logException(e);
        }
        return depSubNoTOMap;
    }
    public void calculateNextAppDate(int intPayFreq) {
        if (intPayFreq > 0) {
            Date lastIntdt = null;
            Date nxtIntDt = null;
            if(getLastInterestPaidDateValue()!=null && getLastInterestPaidDateValue().length()>0){    
	            lastIntdt = DateUtil.getDateMMDDYYYY(getLastInterestPaidDateValue());
	            nxtIntDt = DateUtil.addDays(lastIntdt, intPayFreq);
            }
            setNextInterestApplDate(nxtIntDt);
        }
    }

    /* To set Joint Account data in the Transfer Object*/
    public HashMap setJointAccntData() {
        HashMap singleRecordJntAcct;
        jntAcctTOMap = new LinkedHashMap();
        try {
            JointAccntTO objJointAccntTO;
            int jntAcctSize = jntAcctAll.size();
            for (int i = 0; i < jntAcctSize; i++) {
                singleRecordJntAcct = (HashMap) jntAcctAll.get(CommonUtil.convertObjToStr(jntAcctAll.keySet().toArray()[i]));
                objJointAccntTO = new JointAccntTO();
                objJointAccntTO.setCustId(CommonUtil.convertObjToStr(singleRecordJntAcct.get("CUST_ID")));
                objJointAccntTO.setDepositNo(getTxtDepsoitNo());
                objJointAccntTO.setStatus(CommonUtil.convertObjToStr(singleRecordJntAcct.get("STATUS")));
                objJointAccntTO.setCommand(getCommand());
                jntAcctTOMap.put(String.valueOf(i), objJointAccntTO);
                objJointAccntTO = null;
                singleRecordJntAcct = null;
            }
        } catch (Exception e) {
            parseException.logException(e, true);
        }
        return jntAcctTOMap;
    }

    /* To get the command type according to the Action */
    private String getCommand() throws Exception {
        String command = null;
        switch (actionType) {
            case ClientConstants.ACTIONTYPE_NEW:
                command = CommonConstants.TOSTATUS_INSERT;
                break;
            case ClientConstants.ACTIONTYPE_EDIT:


                command = CommonConstants.TOSTATUS_UPDATE;
                break;
            case ClientConstants.ACTIONTYPE_DELETE:
                command = CommonConstants.TOSTATUS_DELETE;
                break;
            case ClientConstants.ACTIONTYPE_RENEW:
                command = CommonConstants.TOSTATUS_RENEW;
                break;
            case ClientConstants.ACTIONTYPE_EXTENSION:
                command = CommonConstants.TOSTATUS_EXTENSION;
                break;
            default:
        }

        return command;
    }

    /* To get the Action Type */
    public int getActionType() {
        return this.actionType;
    }

    /* To set the Action Type */
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }

    /* To make the class variables null*/
    private void makeNull() {
        key = null;
        value = null;
        lookUpHash = null;
        keyValue = null;

    }

    /* To Populate the Data */
    public void populateData(HashMap whereMap, NomineeOB objNomineeOB, String viewType) {        
        try {
            whereMap.put("UI_PRODUCT_TYPE", SCREEN);
            whereMap.put("VIEW_TYPE", viewType);
            HashMap mapData = proxy.executeQuery(whereMap, operationMap);
            System.out.println("mapdata map for getdata : " + mapData);
            populateOB(mapData, objNomineeOB, viewType);
            mapData = null;
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    /* To set the Transferring Branch Name according to Transferring Branch Code Tab */
    public void populateTransBranchName(HashMap queryWhereMap) {
        try {
            List transListData = ClientUtil.executeQuery("getSelectBranchName", queryWhereMap);
            HashMap transMapData;
            transMapData = (HashMap) transListData.get(0);
            setTxtTransferingBranchCode(CommonUtil.convertObjToStr(transMapData.get("BRANCH_CODE")));
            setLblValTransferingBranchName(CommonUtil.convertObjToStr(transMapData.get("BRANCH_NAME")));
            transListData = null;
            transMapData = null;
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    /* To set the Transferring Branch Name according to Transferring Branch Code Tab */
    public void populateAccountNumber(HashMap queryWhereMap) {
        try {
            final List accountNumbeListData = ClientUtil.executeQuery("Deposite.getSelectedAccountNumber", queryWhereMap);
            final HashMap accountNumberMapData = (HashMap) accountNumbeListData.get(0);
            setTxtOriginalAccountNumber(CommonUtil.convertObjToStr(accountNumberMapData.get("DEPOSIT_NO")));
            setTdtOriginalDateOfDeposit(CommonUtil.convertObjToStr(DateUtil.getStringDate((Date) accountNumberMapData.get("CREATED_DT"))));
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void populateJointAccntTable(HashMap queryWhereMap) {
        try {
            jntAcctAll = objJointAcctHolderManipulation.populateJointAccntTable(queryWhereMap, jntAcctAll, tblJointAccnt);
            setTblJointAccnt(objJointAcctHolderManipulation.getTblJointAccnt());

            String strCust = powerOfAttorneyOB.getCustName(CommonUtil.convertObjToStr(queryWhereMap.get("CUST_ID")));
            if (!powerOfAttorneyOB.getCbmPoACust().containsElement(strCust)) {
                powerOfAttorneyOB.getCbmPoACust().addKeyAndElement(queryWhereMap.get("CUST_ID"), strCust);
            }

        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    void setTblJointAccnt(EnhancedTableModel tblJointAccnt) {
        this.tblJointAccnt = tblJointAccnt;
        setChanged();
    }

    EnhancedTableModel getTblJointAccnt() {
        return this.tblJointAccnt;
    }

    public void moveToMain(String mainAccntRow, String strRowSelected, int intRowSelected) {
        jntAcctAll = objJointAcctHolderManipulation.moveToMain(mainAccntRow, strRowSelected, intRowSelected, tblJointAccnt, jntAcctAll);
        setTxtCustomerId(strRowSelected);
        setTblJointAccnt(objJointAcctHolderManipulation.getTblJointAccnt());
    }

    public void delJointAccntHolder(String strDelRowCount, int intDelRowCount) {
        jntAcctAll = objJointAcctHolderManipulation.delJointAccntHolder(strDelRowCount, intDelRowCount, tblJointAccnt, jntAcctAll);
        setTblJointAccnt(objJointAcctHolderManipulation.getTblJointAccnt());

        if (powerOfAttorneyOB.getCbmPoACust().containsElement(powerOfAttorneyOB.getCustName(strDelRowCount))) {
            powerOfAttorneyOB.getCbmPoACust().removeKeyAndElement(strDelRowCount);
        }

    }

    public void populateScreen(HashMap queryWhereMap, boolean jntAcctNewClicked) {
        try {
            if ((queryWhereMap.containsKey("OPENING_MODE") && queryWhereMap.get("OPENING_MODE").equals(TRANSFER_IN))
                    || (!getTxtTransferingBranchCode().equals("") && getTxtTransferingBranchCode().length() > 0)) {
            } else {
                queryWhereMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            }
            //system.out.println("populateScreen :" +queryWhereMap);
            List custListData = ClientUtil.executeQuery("getSelectAccInfoDisplay", queryWhereMap);
            if (custListData != null && custListData.size() > 0) {
                HashMap custMapData;
                custMapData = (HashMap) custListData.get(0);
                //            //system.out.println("custMapData:" + custMapData);
                String strPrevMainCust_ID = getTxtCustomerId();
                if (jntAcctNewClicked == false) {//--- If it is Main acctnt,set CustomerId in Main
                    setTxtCustomerId(CommonUtil.convertObjToStr(custMapData.get("CUST_ID")));
                    objJointAcctHolderManipulation.setJntAcctTableData(custMapData, false, tblJointAccnt);
                    setTblJointAccnt(objJointAcctHolderManipulation.getTblJointAccnt());
                }
                setLblValCustomerName(CommonUtil.convertObjToStr(custMapData.get("Name")));
                setLblValDateOfBirth(DateUtil.getStringDate((Date) custMapData.get("DOB")));
                setLblValStreet(CommonUtil.convertObjToStr(custMapData.get("STREET")));
                setLblValArea(CommonUtil.convertObjToStr(custMapData.get("AREA")));
                setLblValCity(CommonUtil.convertObjToStr(custMapData.get("CITY1")));
                setLblValState(CommonUtil.convertObjToStr(custMapData.get("STATE1")));
                setLblValCountry(CommonUtil.convertObjToStr(custMapData.get("COUNTRY")));
                setLblValPin(CommonUtil.convertObjToStr(custMapData.get("PIN_CODE")));
                setLblMemberVal(CommonUtil.convertObjToStr(custMapData.get("MEMBERSHIP_CLASS")));
              //  CommonUtil.convertObjToStr(custListData.get("RETIREMENT_DT"));
                //added by rish
                setRetirementDt(CommonUtil.convertObjToStr(custMapData.get("RETIREMENT_DT")));
                setTdtMaturityDate(getRetirementDt());
                if (custMapData.get("MEMBERSHIP_NO") != null && !custMapData.get("MEMBERSHIP_NO").equals("")) {
                    setLblMemberVal(CommonUtil.convertObjToStr(custMapData.get("MEMBERSHIP_NO")));
                }
                if (custMapData.get("MEMBERSHIP_NO")!=null && !custMapData.get("MEMBERSHIP_NO").equals(""))
                    setTxtExistingAccNo(CommonUtil.convertObjToStr(custMapData.get("MEMBERSHIP_NO")));
                //            CUSTOMER.CUST_TYPE_ID
                //            setCboCategory(CommonUtil.convertObjToStr(getCbmCategory().getDataForKey(CommonUtil.convertObjToStr(custMapData.get("CUST_TYPE_ID")))));
                setLblValCommunicationAddress(CommonUtil.convertObjToStr(getCbmCommunicationAddress().getDataForKey(custMapData.get("COMM_ADDR_TYPE"))));
                String strPrevMainCust = powerOfAttorneyOB.getCustName(strPrevMainCust_ID);
                String strCust = powerOfAttorneyOB.getCustName(CommonUtil.convertObjToStr(queryWhereMap.get("CUST_ID")));
                if (!jntAcctNewClicked) {
                    // Remove the previous main customer, when the main customer's populate button pressed
                    if (strPrevMainCust != "" && powerOfAttorneyOB.getCbmPoACust().containsElement(strPrevMainCust)) {
                        powerOfAttorneyOB.getCbmPoACust().removeKeyAndElement(getTxtCustomerId());
                    }
                }
                if (!powerOfAttorneyOB.getCbmPoACust().containsElement(strCust)) {
                    powerOfAttorneyOB.getCbmPoACust().addKeyAndElement(queryWhereMap.get("CUST_ID"), strCust);
                }
                custListData = null;
                custMapData = null;
            } else {
                ClientUtil.displayAlert("Invalid Customer Id NO");
                return;
            }
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void populateOB(HashMap mapData, NomineeOB objNomineeOB, String viewType) throws Exception {
        //system.out.println("###termdeposit populateOB :"+mapData);
        //        HashMap accountInfoMap = (HashMap)((List)mapData.get("AcctInfoDetails")).get(0);
        HashMap accountInfoMap = null;
        ThriftBenevolentAdditionalAmtTO objThriftBenevolentAdditionalAmtTO = null;
        List TransList = ((List) mapData.get("TransactionTO"));
        transactionOB.setDetails(TransList);
        setTranListforAuth(TransList);
        List lstAcc = ((List) mapData.get("AcctInfoDetails"));
        for (int i = 0; i < lstAcc.size(); i++) {
            accountInfoMap = (HashMap) lstAcc.get(i);
            if (accountInfoMap.get("DEPOSIT_STATUS").equals("NEW")) {
                accountInfoMap = accountInfoMap;
                break;
            } else {
                accountInfoMap = accountInfoMap;
            }
        }
        ArrayList nomineeList = null;
        setAcctInfoDetails(accountInfoMap);
        category = CommonUtil.convertObjToStr(accountInfoMap.get("CATEGORY"));
        prodId = CommonUtil.convertObjToStr(accountInfoMap.get("PROD_ID"));
        //        accountInfoMap = null;
        List depSubNoDetailsList = (List) ((List) mapData.get("DepSubNoAcctInfoDetails"));
        //        for(int i=0;i<depSubNoDetailsList.size();i++){
        //            HashMap subMap = (HashMap)depSubNoDetailsList.get(i);
        //            if(subMap.get("ACCT_STATUS").equals("NEW")){
        //                depSubNoDetailsList = (List)depSubNoDetailsList.get(i);
        //                break;
        //            }else{
        //                depSubNoDetailsList = (HashMap)depSubNoDetailsList
        //            }
        //        }
        if (viewType.equals("RENEW")) {
            //system.out.println("****** renewal populateOB :"+depSubNoDetailsList);
            setDepSubNoDetailsInRenewal(depSubNoDetailsList);
        } else if (viewType.equals("EXTENSION_OF_DEPOSIT")) {
            setDepSubNoDetailsInExtension(depSubNoDetailsList);
        } else {
            setDepSubNoDetails(depSubNoDetailsList);
        }
        HashMap subAccInfo = null;
        if (depSubNoDetailsList != null & depSubNoDetailsList.size() > 0) {
            for (int i = 0; i < depSubNoDetailsList.size(); i++) {
                subAccInfo = (HashMap) depSubNoDetailsList.get(i);
            }
        }
        setDepAccInfoDetails(subAccInfo);
        setStatusBy(CommonUtil.convertObjToStr(accountInfoMap.get("STATUS_BY")));
        setAuthorizeStatus(CommonUtil.convertObjToStr(accountInfoMap.get("AUTHORIZE_STATUS")));

        depSubNoDetailsList = null;
        //--- To Populate the Main Customer
        HashMap queryMap = new HashMap();
        queryMap.put("CUST_ID", getTxtCustomerId());
        queryMap.put("OPENING_MODE", accountInfoMap.get("OPENING_MODE"));
        populateScreen(queryMap, false);
        //--- Populates if the TransferIn is selected
        if (getRdoOpeningMode_TransferIn() == true) {
            HashMap transInMap = (HashMap) ((List) mapData.get("TransInDetails")).get(0);
            setTransInDetails(transInMap);
            transInMap = null;
        }
        if ((getViewTypeDeposit().equals("AUTHORIZE") || getViewTypeDeposit().equals("EDIT")) && getRenewedNewNo().length() > 0) {
            HashMap jointMap = new HashMap();
            jointMap.put("DEPOSIT NO", getRenewedNewNo());
            List list = (List) ClientUtil.executeQuery("getSelectJointAccntHolderTO", jointMap);
            if (list != null && list.size() > 0) {
                setCboConstitution("Joint Account");
                mapData.put("JointAcctDetails", list);
            }
        }
        //--- populates if the Constitution is Joint Account
        if ((getViewTypeDeposit().equals("AUTHORIZE") || getViewTypeDeposit().equals("EDIT")) && getRenewedNewNo().length() > 0) {
            HashMap jointMap = new HashMap();
            jointMap.put("DEPOSIT NO", getRenewedNewNo());
            List list = (List) ClientUtil.executeQuery("getSelectJointAccntHolderTO", jointMap);
            if (list != null && list.size() > 0) {
                setCboConstitution("Joint Account");
                mapData.put("JointAcctDetails", list);
            }
        }
        //--- populates if the Constitution is Joint Account
        if (getCboConstitution().equals("Joint Account")) {
            if (mapData.containsKey("JointAcctDetails")) {
                List jntAccntDetailsList = (List) ((List) mapData.get("JointAcctDetails"));
                if (jntAccntDetailsList.size() > 0) {
                    setJointAcctDetails(jntAccntDetailsList);
                }
                jntAccntDetailsList = null;
            }
        }
        //--- populates if the Authorized sigantory is checked
        if (getChkAuthorizedSignatory() == true) {
            authorizedSignatoryOB.setAuthorizedSignatoryTO((ArrayList) (mapData.get("AuthorizedSignatoryTO")), getTxtDepsoitNo());
            authorizedSignatoryInstructionOB.setAuthorizedSignatoryInstructionTO((ArrayList) (mapData.get("AuthorizedSignatoryInstructionTO")), getTxtDepsoitNo());
        }
        //--- populates if the Poa is checked
        if (getChkPowerOfAttorney() == true) {
            powerOfAttorneyOB.setTermLoanPowerAttorneyTO((ArrayList) (mapData.get("PowerAttorneyTO")), getTxtDepsoitNo());
        }
        //---populates if the NomineeDetails is checked
        if (getChkNomineeDetails() == true && mapData.containsKey("AccountNomineeList")) {
            /* To set the ArrayList in NomineeOB so as to set the data in the Nominee-Table...*/
            //            if(getViewTypeDeposit().equals("EDIT") || getViewTypeDeposit().equals("AUTHORIZE") ||
            //            getActionType() == ClientConstants.ACTIONTYPE_RENEW){// && getLblValRenewDep()!=null &&
            ////            getTxtDepsoitNo()!=null && !getTxtDepsoitNo().equals(getLblValRenewDep())){
            //                HashMap renewNomineeMap = new HashMap();
            //                renewNomineeMap.put("DEPOSIT_NO",getTxtDepsoitNo());
            //                List list = (List) ClientUtil.executeQuery("getSelectRenewNomineeTOTD", renewNomineeMap);
            //                mapData.put("AccountNomineeList", list);
            //                nomineeList =  (ArrayList) mapData.get("AccountNomineeList");
            //                objNomineeOB.setNomimeeList(nomineeList);
            //                objNomineeOB.setNomineeTabData();
            //                objNomineeOB.ttNotifyObservers();
            //            }else{
            //            if(){
            nomineeList = (ArrayList) mapData.get("AccountNomineeList");
            objNomineeOB.setNomimeeList(nomineeList);
            objNomineeOB.setNomineeTabData();
            objNomineeOB.ttNotifyObservers();
            //            }
        }
        // Added by nithya on 08-03.2016 for 0003920 
        if(mapData.containsKey("DEPO_ACCT_ADDITIONAL_AMT")){
            objThriftBenevolentAdditionalAmtTOList = new ArrayList();           
            for(int i = 0;i<((List)mapData.get("DEPO_ACCT_ADDITIONAL_AMT")).size(); i++){
                objThriftBenevolentAdditionalAmtTO = (ThriftBenevolentAdditionalAmtTO) ((List)mapData.get("DEPO_ACCT_ADDITIONAL_AMT")).get(i);        
                HashMap thriftBenevolentMap = setThriftBenevelontDataForAccount(objThriftBenevolentAdditionalAmtTO);
                objThriftBenevolentAdditionalAmtTOList.add(thriftBenevolentMap);                
            }
            if(objThriftBenevolentAdditionalAmtTOList != null && objThriftBenevolentAdditionalAmtTOList.size() > 0){
                populateThriftBenevolentTable(objThriftBenevolentAdditionalAmtTOList);
            } 
            else{
                deleteThriftBenevolentTableData();
            }
            
        }
        if (mapData.containsKey("SMSSubscriptionTO") && ((List) mapData.get("SMSSubscriptionTO")).size() > 0){
            objSMSSubscriptionTO = (SMSSubscriptionTO) ((List) mapData.get("SMSSubscriptionTO")).get(0);
            objSMSSubscriptionTO.setStatus(CommonConstants.STATUS_MODIFIED);
            setSMSSubscriptionTO(objSMSSubscriptionTO);
            notifyObservers();
        }
       // End 
        accountInfoMap = null;
    }

    private void setDepAccInfoDetails(HashMap subAccInfo) {
        if (CommonUtil.convertObjToStr(subAccInfo.get("SALARY_RECOVERY")).equals("Y")) {
            setSalaryRecoveryYes(true);
        } else {
            setSalaryRecoveryNo(true);
        }
        if (CommonUtil.convertObjToStr(subAccInfo.get("LOCK_STATUS")).equals("Y")) {
            setLockStatus("LOCKED");
        } else {
            setLockStatus("UNLOCKED");
        }
        setPostageAmt(CommonUtil.convertObjToStr(subAccInfo.get("POSTAGE_AMT")));
        setPostageRenewAmt(CommonUtil.convertObjToStr(subAccInfo.get("POSTAGE_AMT")));

    }

    private void setDepSubNoDetails(List depSubNoDetailsList) {
        tblDepSubNoAccInfo.setDataArrayList(null, tblDepSubNoColTitle);
        tblLienDetails.setDataArrayList(null, tblLienColTitle);
      //  tblOldDepDet.setDataArrayList(null, tblOldDepColTitle);
        depSubNoAll = new HashMap();
        int depSubNoDetailsListSize = depSubNoDetailsList.size();
        double availBal = 0;
        double depAmt = 0;
        HashMap calcDepSubNo;
        for (int i = 0; i < depSubNoDetailsListSize; i++) {
            calcDepSubNo = (HashMap) depSubNoDetailsList.get(i);
            depSubNoRec = new HashMap();
            depSubNoRec.put(FLD_DEP_SUB_NO_DEP_SUB_NO, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_SUB_NO")));
            depSubNoRec.put(FLD_DEP_SUB_NO_MAT_DT, DateUtil.getStringDate((Date) calcDepSubNo.get("MATURITY_DT")));
            depSubNoRec.put(FLD_DEP_SUB_NO_MAT_AMT, CommonUtil.convertObjToStr(calcDepSubNo.get("MATURITY_AMT")));
            depSubNoRec.put(FLD_DEP_SUB_NO_RATE_OF_INT, CommonUtil.convertObjToStr(calcDepSubNo.get("RATE_OF_INT")));
            depSubNoRec.put(FLD_DEP_SUB_NO_TOT_INT_AMT, CommonUtil.convertObjToStr(calcDepSubNo.get("TOT_INT_AMT")));
            depSubNoRec.put(FLD_DEP_SUB_NO_PER_INT_AMT, CommonUtil.convertObjToStr(calcDepSubNo.get("PERIODIC_INT_AMT")));
            depSubNoRec.put(FLD_DEP_SUB_NO_DEP_DT, DateUtil.getStringDate((Date) calcDepSubNo.get("DEPOSIT_DT")));
            depSubNoRec.put(FLD_DEP_SUB_NO_DEP_AMT, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_AMT")));
            depSubNoRec.put(FLD_DEP_SUB_NO_DEP_PER_DD, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_PERIOD_DD")));
            depSubNoRec.put(FLD_DEP_SUB_NO_DEP_PER_MM, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_PERIOD_MM")));
            depSubNoRec.put(FLD_DEP_SUB_NO_DEP_PER_YY, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_PERIOD_YY")));
            depSubNoRec.put(FLD_DEP_SUB_NO_DEP_PER_Wk, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_PERIOD_WK")));

            depSubNoRec.put(FLD_DEP_SUB_NO_INT_PAY_FREQ, CommonUtil.convertObjToStr(calcDepSubNo.get("INTPAY_FREQ")));
            depSubNoRec.put(FLD_DEP_SUB_NO_INT_PAY_MODE, CommonUtil.convertObjToStr(calcDepSubNo.get("INTPAY_MODE")));
            depSubNoRec.put(FLD_DEP_SUB_NO_STATUS, CommonUtil.convertObjToStr(calcDepSubNo.get("STATUS")));
            depSubNoRec.put(FLD_DEP_SUB_INST_TYPE, CommonUtil.convertObjToStr(calcDepSubNo.get("INSTALL_TYPE")));
            depSubNoRec.put(FLD_DEP_SUB_PAY_TYPE, CommonUtil.convertObjToStr(calcDepSubNo.get("PAYMENT_TYPE")));
            depSubNoRec.put(FLD_DEP_SUB_PAY_DATE, DateUtil.getStringDate((Date) calcDepSubNo.get("PAYMENT_DAY")));
            depSubNoRec.put(FLD_DEP_SUB_INT_PROD_TYPE, CommonUtil.convertObjToStr(calcDepSubNo.get("INT_PAY_PROD_TYPE")));
            depSubNoRec.put(FLD_DEP_SUB_INT_PROD_ID, CommonUtil.convertObjToStr(calcDepSubNo.get("INT_PAY_PROD_ID")));
            depSubNoRec.put(FLD_DEP_SUB_INT_AC_NO, CommonUtil.convertObjToStr(calcDepSubNo.get("INT_PAY_ACC_NO")));
            if (CommonUtil.convertObjToStr(calcDepSubNo.get("CALENDER_FREQ")).equals(YES_STR)) {
                setRdoCalenderFreq_Yes(true);
                depSubNoRec.put(FLD_DEP_SUB_CALENDER_DAY, CommonUtil.convertObjToStr(calcDepSubNo.get("CALENDER_DAY")));
                depSubNoRec.put(FLD_DEP_SUB_CALENDER_FREQ, CommonUtil.convertObjToStr(calcDepSubNo.get("CALENDER_FREQ")));
                depSubNoRec.put(FLD_DEP_SUB_CALENDER_DATE, CommonUtil.convertObjToStr(calcDepSubNo.get("CALENDER_DATE")));
                setCalenderFreqDate(CommonUtil.convertObjToStr(calcDepSubNo.get("CALENDER_DATE")));
                setCboCalenderFreq(CommonUtil.convertObjToStr(calcDepSubNo.get("CALENDER_DAY")));
            } else {
                setRdoCalenderFreq_No(true);
                depSubNoRec.put(FLD_DEP_SUB_CALENDER_FREQ, CommonUtil.convertObjToStr(calcDepSubNo.get("CALENDER_FREQ")));
                setCalenderFreqDate("");
            }
            //Added By Suresh
            setTxtMaturityAmount(CommonUtil.convertObjToStr(calcDepSubNo.get("MATURITY_AMT")));
            setAdtAmt(CommonUtil.convertObjToDouble(calcDepSubNo.get("ADT_AMT")));
            setTxtTotalInterestAmount(CommonUtil.convertObjToStr(calcDepSubNo.get("TOT_INT_AMT")));

            //--- To check whether transaction has been done
            depAmt = Double.parseDouble(CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_AMT")));
            String strAvailBal = CommonUtil.convertObjToStr(calcDepSubNo.get("AVAILABLE_BALANCE"));
            if (strAvailBal.trim().length() > 0) {
                availBal = Double.parseDouble(CommonUtil.convertObjToStr(calcDepSubNo.get("AVAILABLE_BALANCE")));
            }
            if (availBal == depAmt) {
                isTransactionDone = true;
            } else {
                isTransactionDone = false;
            }
            //this details filled with the present Position Details tab....
            double availAmt = CommonUtil.convertObjToDouble(calcDepSubNo.get("TOTAL_BALANCE")).doubleValue();
            if (availAmt != 0 || calcDepSubNo.get("ACCT_STATUS").equals("CLOSED") || calcDepSubNo.get("ACCT_STATUS").equals("NEW")) {
                long period = 0;
                if (getViewTypeDeposit().equals("CLOSED_DEPOSIT")) {// || getViewTypeDeposit().equals("AUTHORIZE")){
                    StringBuffer periodLetters = new StringBuffer();
                    HashMap closedMap = new HashMap();
                    if (calcDepSubNo.get("FLEXI_STATUS") != null && calcDepSubNo.get("FLEXI_STATUS").equals("PC")
                            || calcDepSubNo.get("FLEXI_STATUS") != null && calcDepSubNo.get("FLEXI_STATUS").equals("NC")) {
                        if (calcDepSubNo.get("FLEXI_STATUS") != null && calcDepSubNo.get("FLEXI_STATUS").equals("PC")) {
                            setClosingTypeValue(CommonUtil.convertObjToStr(CommonConstants.PREMATURE_CLOSURE));
                            if (calcDepSubNo.get("CLOSE_DT") != null) {
                                period = DateUtil.dateDiff((Date) calcDepSubNo.get("DEPOSIT_DT"), (Date) calcDepSubNo.get("CLOSE_DT"));
                            }
                            double year = 0.0;
                            double month = 0.0;
                            double days = 0.0;
                            double freqency = 0.0;
                            freqency = CommonUtil.convertObjToDouble(calcDepSubNo.get("INTPAY_FREQ")).doubleValue();
                            Date nextDate = (Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_DT")));
                            Date depDt = (Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_DT")));
                            Date closedt = (Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(calcDepSubNo.get("CLOSE_DT")));
                            if (productBehavesLike.equals(prodBehavesLikeFixed)) {
                                HashMap prematureDateMap = new HashMap();
                                prematureDateMap.put("FROM_DATE", depDt);
                                prematureDateMap.put("TO_DATE", closedt);
                                List lst = ClientUtil.executeQuery("periodRunMap", prematureDateMap);
                                if (lst != null && lst.size() > 0) {
                                    prematureDateMap = (HashMap) lst.get(0);
                                    month = CommonUtil.convertObjToDouble(prematureDateMap.get("NO_OF_MONTHS")).doubleValue();
                                    days = CommonUtil.convertObjToLong(prematureDateMap.get("DAYS"));
                                }
                            }
                            if (productBehavesLike.equals(prodBehavesLikeFixed) && freqency == 0) {
                                periodLetters.append(String.valueOf(calcDepSubNo.get("DEPOSIT_PERIOD_YY") + " Years "));
                                periodLetters.append(String.valueOf(calcDepSubNo.get("DEPOSIT_PERIOD_MM") + " Months "));
                                periodLetters.append(String.valueOf(calcDepSubNo.get("DEPOSIT_PERIOD_DD") + " Days "));
                            } else if (productBehavesLike.equals(prodBehavesLikeFixed) && freqency == 30) {
                                periodLetters.append(String.valueOf("0.0" + " Years "));
                                periodLetters.append(String.valueOf(month + " Months "));
                                periodLetters.append(String.valueOf(days + " Days "));
                            } else if (productBehavesLike.equals(prodBehavesLikeFixed) && freqency == 90) {
                                year = (long) roundOffLower((long) ((month / 3) * 100), 100) / 100;
                                month = month - year * 3;
                                periodLetters.append(String.valueOf(year + " Quaters "));
                                periodLetters.append(String.valueOf(month + " Months "));
                                periodLetters.append(String.valueOf(days + " Days "));
                            } else if (productBehavesLike.equals(prodBehavesLikeFixed) && freqency == 180) {
                                year = (long) roundOffLower((long) ((month / 6) * 100), 100) / 100;
                                month = month - year * 6;
                                periodLetters.append(String.valueOf(year + " Half Yearly "));
                                periodLetters.append(String.valueOf(month + " Months "));
                                periodLetters.append(String.valueOf(days + " Days "));
                            } else if (productBehavesLike.equals(prodBehavesLikeFixed) && freqency == 360) {
                                year = (long) roundOffLower((long) ((month / 12) * 100), 100) / 100;
                                month = month - year * 12;
                                periodLetters.append(String.valueOf(year + " Yearly "));
                                periodLetters.append(String.valueOf(month + " Months "));
                                periodLetters.append(String.valueOf(days + " Days "));
                            } else if (!productBehavesLike.equals(prodBehavesLikeFixed)) {
                                int j = 0;
                                Date k = DateUtil.nextCalcDate(depDt, depDt, 30);
                                for (; DateUtil.dateDiff(k, closedt) > 0; k = DateUtil.nextCalcDate(depDt, k, 30)) {
                                    j = j + 1;
                                    nextDate = k;
                                }
                                year = (long) roundOffLower((long) ((j / 3) * 100), 100) / 100;
                                month = year * 3;
                                Date cummDepDate = null;
                                cummDepDate = (Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_DT")));
                                for (int a = 0; a < month; a++) {
                                    cummDepDate = DateUtil.addDays(cummDepDate, 30);
                                }
                                days = DateUtil.dateDiff(cummDepDate, closedt);
                                periodLetters.append(String.valueOf(year + " Quaters "));
                                periodLetters.append(String.valueOf("0.0" + " Months "));
                                periodLetters.append(String.valueOf(days + " Days "));
                            }
                        } else if (calcDepSubNo.get("FLEXI_STATUS") != null && calcDepSubNo.get("FLEXI_STATUS").equals("NC")) {
                            setClosingTypeValue(CommonUtil.convertObjToStr(CommonConstants.NORMAL_CLOSURE));
                            //                            period = DateUtil.dateDiff((Date)calcDepSubNo.get("DEPOSIT_DT"),(Date)calcDepSubNo.get("CLOSE_DT"));
                            periodLetters.append(String.valueOf(calcDepSubNo.get("DEPOSIT_PERIOD_YY") + " Years "));
                            periodLetters.append(String.valueOf(calcDepSubNo.get("DEPOSIT_PERIOD_MM") + " Months "));
                            periodLetters.append(String.valueOf(calcDepSubNo.get("DEPOSIT_PERIOD_DD") + " Days "));
                        }
                        closedMap.put("DEPOSIT_NO", calcDepSubNo.get("DEPOSIT_NO")); //getClosedCurrRateOfInterest
                        List lstClose = ClientUtil.executeQuery("getSelectDepSubNoIntDetailsSameNo", closedMap);
                        if (lstClose != null && lstClose.size() > 0) {
                            closedMap = (HashMap) lstClose.get(0);
                            double currRate = CommonUtil.convertObjToDouble(closedMap.get("CURR_RATE_OF_INT")).doubleValue();
                            double penalRate = CommonUtil.convertObjToDouble(closedMap.get("PENAL_RATE")).doubleValue();
                            double rateOfInt = currRate;// - penalRate;
                            setClosingRateOfInterestValue(String.valueOf(rateOfInt));
                            setClosingInterestAmountValue("Rs." + " " + CommonUtil.convertObjToStr(closedMap.get("INTEREST_AMT")));
                        }
                        lstClose = null;
                    } else if (calcDepSubNo.get("FLEXI_STATUS") != null && calcDepSubNo.get("FLEXI_STATUS").equals("NR")) {
                        setClosingTypeValue(CommonUtil.convertObjToStr("RENEWAL"));
                        if (calcDepSubNo.get("CLOSE_DT") != null) {
                            period = DateUtil.dateDiff((Date) calcDepSubNo.get("DEPOSIT_DT"), (Date) calcDepSubNo.get("CLOSE_DT"));
                        }
                        periodLetters.append(String.valueOf(calcDepSubNo.get("DEPOSIT_PERIOD_YY") + " Years "));
                        periodLetters.append(String.valueOf(calcDepSubNo.get("DEPOSIT_PERIOD_MM") + " Months "));
                        periodLetters.append(String.valueOf(calcDepSubNo.get("DEPOSIT_PERIOD_DD") + " Days "));
                        setClosingRateOfInterestValue(CommonUtil.convertObjToStr(calcDepSubNo.get("RATE_OF_INT")));
                        HashMap renewTempMap = new HashMap();
                        if (getViewTypeDeposit().equals("CLOSED_DEPOSIT")) {
                            renewTempMap.put("OLD_DEPOSIT_NO", calcDepSubNo.get("DEPOSIT_NO")); //getClosedCurrRateOfInterest
                            List listRenew = ClientUtil.executeQuery("getSelectClosedPeriodofRenewal", renewTempMap);
                            if (listRenew != null && listRenew.size() > 0) {
                                renewTempMap = (HashMap) listRenew.get(0);
                                setSBRateOfInterestValue(CommonUtil.convertObjToStr(renewTempMap.get("SB_PERIOD_RUN")));
                                setRenewalClosedDepNo(CommonUtil.convertObjToStr(renewTempMap.get("DEPOSIT_NO")));
                                setSBInterestAmountValue("Rs." + " " + CommonUtil.convertObjToStr(renewTempMap.get("SB_INT_AMT")));
                                setClosingInterestAmountValue("Rs." + " " + CommonUtil.convertObjToStr(renewTempMap.get("BALANCE_INT_AMT")));
                            }
                            listRenew = null;
                        } else if (getViewTypeDeposit().equals("AUTHORIZE")) {
                            renewTempMap.put("OLD_DEPOSIT_NO", calcDepSubNo.get("DEPOSIT_NO")); //getClosedCurrRateOfInterest
                            List listRenew = ClientUtil.executeQuery("getSelectPeriodofRenewal", renewTempMap);
                            if (listRenew != null && listRenew.size() > 0) {
                                renewTempMap = (HashMap) listRenew.get(0);
                                setSBRateOfInterestValue(CommonUtil.convertObjToStr(renewTempMap.get("SB_PERIOD_RUN")));
                                setRenewalClosedDepNo(CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_NO")));
                                setSBInterestAmountValue("Rs." + " " + CommonUtil.convertObjToStr(renewTempMap.get("SB_INT_AMT")));
                                setClosingInterestAmountValue("Rs." + " " + CommonUtil.convertObjToStr(renewTempMap.get("BALANCE_INT_AMT")));
                            }
                            listRenew = null;
                        }
                        setRenewalDateValue(CommonUtil.convertObjToStr(calcDepSubNo.get("CLOSE_DT")));
                        renewTempMap = null;
                    } else if (calcDepSubNo.get("FLEXI_STATUS") != null && calcDepSubNo.get("FLEXI_STATUS").equals("AR")) {
                        setClosingTypeValue(CommonUtil.convertObjToStr("AUTO RENEWAL"));
                        period = DateUtil.dateDiff((Date) calcDepSubNo.get("DEPOSIT_DT"), (Date) calcDepSubNo.get("CLOSE_DT"));
                        periodLetters.append(String.valueOf(calcDepSubNo.get("DEPOSIT_PERIOD_YY") + " Years "));
                        periodLetters.append(String.valueOf(calcDepSubNo.get("DEPOSIT_PERIOD_MM") + " Months "));
                        periodLetters.append(String.valueOf(calcDepSubNo.get("DEPOSIT_PERIOD_DD") + " Days "));
                    } else if (calcDepSubNo.get("FLEXI_STATUS") != null && calcDepSubNo.get("FLEXI_STATUS").equals("EX")) {
                        setClosingTypeValue(CommonUtil.convertObjToStr("EXTENSION"));
                        if (calcDepSubNo.get("CLOSE_DT") != null) {
                            period = DateUtil.dateDiff((Date) calcDepSubNo.get("DEPOSIT_DT"), (Date) calcDepSubNo.get("CLOSE_DT"));
                        }
                        HashMap extensionMap = new HashMap();
                        extensionMap.put("OLD_DEPOSIT_NO", calcDepSubNo.get("DEPOSIT_NO"));
                        List lst = ClientUtil.executeQuery("getSelectExtensionOldDetails", extensionMap);
                        if (lst != null && lst.size() > 0) {
                            extensionMap = (HashMap) lst.get(0);
                            setClosingRateOfInterestValue(CommonUtil.convertObjToStr(extensionMap.get("OLD_RATE_OF_INT")));
                            periodLetters.append(CommonUtil.convertObjToStr(extensionMap.get("OLD_PERIOD_RUN")));
                            setClosingInterestAmountValue("Rs." + " " + CommonUtil.convertObjToStr(extensionMap.get("RECALCUALTE_INTEREST_AMT")));
                            setRenewalClosedDepNo(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_DEPOSIT_NO")));
                        }
                        extensionMap = null;
                    }
                    setDepositPeriodRunValue(periodLetters.toString());
                }
                if (calcDepSubNo.get("ACCT_STATUS").equals("CLOSED") && (getViewTypeDeposit().equals("CLOSED_DEPOSIT")
                        || getViewTypeDeposit().equals("ACCOUNT NUMBER") || getViewTypeDeposit().equals("AUTHORIZE")
                        || getViewTypeDeposit().equals("EDIT"))) {
                    boolean transferIn = false;
                    if (getViewTypeDeposit().equals("CLOSED_DEPOSIT")) {
                        HashMap transMap = new HashMap();
                        transMap.put("DEPOSIT_NO", calcDepSubNo.get("DEPOSIT_NO"));
                        List lstTrans = ClientUtil.executeQuery("DepositViewAllAccountNumber", transMap);
                        if (lstTrans != null && lstTrans.size() > 0) {
                            transMap = (HashMap) lstTrans.get(0);
                            transferIn = true;
                        }
                        setClosedDateValue(CommonUtil.convertObjToStr(calcDepSubNo.get("CLOSE_DT")));
                    } else {
                        HashMap transMap = new HashMap();
                        transMap.put("DEPOSIT_NUMBER", calcDepSubNo.get("DEPOSIT_NO"));
                        List lstTrans = ClientUtil.executeQuery("Deposite.ViewAllAccountNumber", transMap);
                        if (lstTrans != null && lstTrans.size() > 0) {
                            transMap = (HashMap) lstTrans.get(0);
                            transferIn = true;
                            Date lastIntApplDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(calcDepSubNo.get("LAST_INT_APPL_DT")));
                            setTdtOriginalDateOfDeposit(CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_DT")));
                            setTxtOriginalAccountNumber(CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_NO")));
                            setTxtTransferingBranchCode(CommonUtil.convertObjToStr(transMap.get("TRANSFER_OUT_BRANCH")));
                            setTxtPrintedNoOfTheFdr(CommonUtil.convertObjToStr(transMap.get("")));
                            setTxtInterBranchTransferNo(CommonUtil.convertObjToStr(transMap.get("INTER_TRANS_NO")));
                            setTxtTransferingAmount(CommonUtil.convertObjToStr(transMap.get("TRANS_AMT")));
                            setTdtDateOfTransfer(CommonUtil.convertObjToStr(transMap.get("TRANS_DT")));
                            if (lastIntApplDt == null) {
                                setTdtLastInterestCalculatedDate(CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_DT")));
                            } else {
                                setTdtLastInterestCalculatedDate(CommonUtil.convertObjToStr(calcDepSubNo.get("LAST_INT_APPL_DT")));
                            }
                            setTxtInterestProvidedAmount(CommonUtil.convertObjToStr(calcDepSubNo.get("TOTAL_INT_CREDIT")));
                            setTxtInterestPaid(CommonUtil.convertObjToStr(calcDepSubNo.get("TOTAL_INT_DRAWN")));
                            setTxtTotalNumberOfInstallments(CommonUtil.convertObjToStr(calcDepSubNo.get("TOTAL_INSTALLMENTS")));
                            Date lastTransDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(calcDepSubNo.get("LAST_TRANS_DT")));
                            HashMap lastTransMap = new HashMap();
                            lastTransMap.put("ACCOUNTNO", calcDepSubNo.get("DEPOSIT_NO") + "_1");
                            List lstLastTrans = ClientUtil.executeQuery("getSelectDepositMaxTransDt", lastTransMap);
                            if (lstLastTrans != null && lstLastTrans.size() > 0) {
                                lastTransMap = (HashMap) lstLastTrans.get(0);
                                setTdtLastInstallmentReceivedDate(CommonUtil.convertObjToStr(lastTransMap.get("TRANS_DT")));
                            }
                            setTdtTdsCollectedUpto(CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_DT")));
                            setTxtLastTdsCollected(String.valueOf(0.0));
                            double credited = CommonUtil.convertObjToDouble(calcDepSubNo.get("TOTAL_INT_CREDIT")).doubleValue();
                            double totInt = CommonUtil.convertObjToDouble(calcDepSubNo.get("TOT_INT_AMT")).doubleValue();
                            double balance = totInt - credited;
                            setTxtTotalInstallmentReceived(CommonUtil.convertObjToStr(calcDepSubNo.get("TOTAL_INSTALL_PAID")));
                            setLblValBalanceInterestPayable(String.valueOf(balance));
                            setLblValTransferingBranchName(CommonUtil.convertObjToStr(transMap.get("")));
                        }
                    }
                    if (transferIn == true) {
                        HashMap transferInMap = new HashMap();
                        transferInMap.put("DEPOSIT_NO", calcDepSubNo.get("DEPOSIT_NO"));
                        transferInMap.put("AUTHORIZE_STATUS", CommonConstants.STATUS_AUTHORIZED);
                        List lst = ClientUtil.executeQuery("getTransferOutDeposit", transferInMap);
                        if (lst != null && lst.size() > 0) {
                            transferInMap = (HashMap) lst.get(0);
                            setTransferOutBranchValue(CommonUtil.convertObjToStr(transferInMap.get("BRANCH_NAME")));
                            setClosingTypeValue(CommonUtil.convertObjToStr(CommonConstants.TRANSFER_OUT_CLOSURE));
                            period = DateUtil.dateDiff((Date) calcDepSubNo.get("DEPOSIT_DT"), (Date) calcDepSubNo.get("CLOSE_DT"));
                        }
                    }
                    //                    setDepositPeriodRunValue(String.valueOf(period)+" "+"Days");
                    setBalanceInterestAmountValue(String.valueOf(0.0));
                    setAvailableBalanceValue("Rs." + " " + CommonUtil.convertObjToStr(calcDepSubNo.get("AVAILABLE_BALANCE")));
                    setClearBalanceValue("Rs." + " " + CommonUtil.convertObjToStr(calcDepSubNo.get("CLEAR_BALANCE")));
                    setTotalBalanceValue("Rs." + " " + CommonUtil.convertObjToStr(calcDepSubNo.get("TOTAL_BALANCE")));
                    setLastInterestPaidDateValue(CommonUtil.convertObjToStr(calcDepSubNo.get("LAST_INT_APPL_DT")));
                    setTotalInterestValue("Rs." + " " + CommonUtil.convertObjToStr(calcDepSubNo.get("TOT_INT_AMT")));
                    setPaidnterestAmountValue("Rs." + " " + CommonUtil.convertObjToStr(calcDepSubNo.get("TOTAL_INT_DRAWN")));
                    setAccHeadValue(CommonUtil.convertObjToStr(calcDepSubNo.get("INSTALL_TYPE")));
                    setLastInterestProvisionDateValue(CommonUtil.convertObjToStr(calcDepSubNo.get("LST_PROV_DT")));
                    setLblDelayedMonthValue(CommonUtil.convertObjToStr(calcDepSubNo.get("DELAYED_MONTH")));
                    setLblDelayedAmountValue("Rs." + " " + CommonUtil.convertObjToStr(calcDepSubNo.get("DELAYED_AMOUNT")));
                    //                    HashMap closedMap = new HashMap();
                    //                    closedMap.put("DEPOSIT_NO",calcDepSubNo.get("DEPOSIT_NO")); //getClosedCurrRateOfInterest
                    //                    List lstClose = ClientUtil.executeQuery("getSelectDepSubNoIntDetailsSameNo", closedMap);
                    //                    if(lstClose!=null && lstClose.size()>0){
                    //                        closedMap = (HashMap)lstClose.get(0);
                    //                        double currRate = CommonUtil.convertObjToDouble(closedMap.get("CURR_RATE_OF_INT")).doubleValue();
                    //                        double penalRate = CommonUtil.convertObjToDouble(closedMap.get("PENAL_RATE")).doubleValue();
                    //                        double rateOfInt = currRate - penalRate;
                    //                        setClosingRateOfInterestValue(String.valueOf(rateOfInt));
                    //                    }
                    //                        setClosingInterestAmountValue("Rs."+" "+ CommonUtil.convertObjToStr(closedMap.get("INTEREST_AMT")));
                    //                        setSBInterestAmountValue("Rs."+" "+ CommonUtil.convertObjToStr(closedMap.get("SB_INT_AMT")));
                    //                        double periodRun = CommonUtil.convertObjToDouble(closedMap.get("SB_PERIOD_RUN")).doubleValue();
                    //                        if(periodRun>0)
                    //                            setSBRateOfInterestValue(" " +String.valueOf(periodRun)+" Days");
                    HashMap depositMap = new HashMap();
                    ArrayList lienList = new ArrayList();
                    depositMap.put("DEPOSIT_NO", calcDepSubNo.get("DEPOSIT_NO"));
                    List lst = ClientUtil.executeQuery("getPresentDepStatus", depositMap);
                    if (lst != null && lst.size() > 0) {
                        for (int k = 0; k < lst.size(); k++) {
                            depLienRow = new ArrayList();
                            depositMap = (HashMap) lst.get(k);
                            depLienRow.add(0, CommonUtil.convertObjToStr(depositMap.get("LIEN_NO")));
                            depLienRow.add(1, CommonUtil.convertObjToStr(depositMap.get("LIEN_AMOUNT")));
                            depLienRow.add(2, DateUtil.getStringDate((Date) depositMap.get("LIEN_DT")));
                            depLienRow.add(3, CommonUtil.convertObjToStr(depositMap.get("LIEN_AC_NO")));
                            depLienRow.add(4, CommonUtil.convertObjToStr(depositMap.get("STATUS")));
                            depLienRow.add(5, CommonUtil.convertObjToStr(depositMap.get("UNLIEN_DT")));
                            tblLienDetails.insertRow(tblLienDetails.getRowCount(), depLienRow);
                        }
                    }
                    HashMap tdsMap = new HashMap();
                    tdsMap.put("ACCT_NUM", calcDepSubNo.get("DEPOSIT_NO") + "_1");
                    lst = ClientUtil.executeQuery("getTdsDeductStatus", tdsMap);
                    if (lst != null && lst.size() > 0) {
                        tdsMap = (HashMap) lst.get(0);
                        setTdsAmountValue(CommonUtil.convertObjToStr(tdsMap.get("SUM(TDS_AMT)")));
                    }
                    depSubNoRec.put(FLD_FOR_DB_YES_NO, YES_FULL_STR);
                    depSubNoAll.put(String.valueOf(i), depSubNoRec);
                    depSubNoRow = new ArrayList();

                    //--- If the record is Not DELETED , Show it in Table
                    if (!(CommonUtil.convertObjToStr(depSubNoRec.get(FLD_DEP_SUB_NO_STATUS)).equals(CommonConstants.STATUS_DELETED))) {
                        depSubNoRow.add(0, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_SUB_NO")));
                        depSubNoRow.add(1, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_AMT")));
                        depSubNoRow.add(2, DateUtil.getStringDate((Date) calcDepSubNo.get("MATURITY_DT")));
                        depSubNoRow.add(3, CommonUtil.convertObjToStr(calcDepSubNo.get("TOT_INT_AMT")));
                        tblDepSubNoAccInfo.insertRow(tblDepSubNoAccInfo.getRowCount(), depSubNoRow);
                    }
                    depSubNoCount = (int) Integer.parseInt(CommonUtil.convertObjToStr(depSubNoRec.get(FLD_DEP_SUB_NO_DEP_SUB_NO)));
                    HashMap renewalMap = new HashMap();
                    renewalMap.put("OLD_DEPOSIT_NO", calcDepSubNo.get("DEPOSIT_NO"));
                    List lstRenewal = ClientUtil.executeQuery("getSelectOldDepositRenewalDetails", renewalMap);
                    if (lstRenewal != null && lstRenewal.size() > 0) {
                        if (getViewTypeDeposit().equals("AUTHORIZE") || getViewTypeDeposit().equals("EDIT")) {
                            renewalMap = (HashMap) lstRenewal.get(0);
                            renewaldepSubNoRec = new HashMap();
                            renewaldepSubNoAll = new HashMap();
                            setRdoOpeningMode_Normal(false);
                            setRdoOpeningMode_Renewal(true);
                            String oldDepositNo = CommonUtil.convertObjToStr(renewalMap.get("OLD_DEPOSIT_NO"));
                            String newDepositNo = CommonUtil.convertObjToStr(renewalMap.get("DEPOSIT_NO"));
                            setRenewalValDepositSubNo(newDepositNo);
                            setLblValRenewDep(CommonUtil.convertObjToStr(renewalMap.get("OLD_DEPOSIT_NO")));
                            setPostageRenewAmt(CommonUtil.convertObjToStr(renewalMap.get("RENEW_POSTAGE_AMT")));
                            Date depDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEPOSIT_DT")));
                            Date matDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_MATURITY_DT")));
                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_DEP_SUB_NO, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_SUB_NO")));
                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_MAT_DT, matDt);
                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_DEP_DT, depDt);
                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_DEP_AMT, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEPOSIT_AMT")));
                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_DEP_NO, CommonUtil.convertObjToStr(renewalMap.get("DEPOSIT_NO")));
                            setTxtDepsoitNo(CommonUtil.convertObjToStr(renewalMap.get("DEPOSIT_NO")));
                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_MAT_AMT, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_MATURITY_AMT")));
                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_DEP_PER_DD, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEPOSIT_DAYS")));
                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_DEP_PER_MM, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEPOSIT_MONTHS")));
                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_DEP_PER_YY, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEPOSIT_YEARS")));
                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_RATE_OF_INT, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_RATE_OF_INT")));
                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_TOT_INT_AMT, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_TOT_INTAMT")));
                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_PER_INT_AMT, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_PERIODIC_INT")));
                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_INT_PAY_MODE, CommonUtil.convertObjToStr(renewalMap.get("INTPAY_MODE")));
                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_BALANCE_INTAMT, CommonUtil.convertObjToStr(renewalMap.get("BALANCE_INT_AMT")));
                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_SB_INTAMT, CommonUtil.convertObjToStr(renewalMap.get("SB_INT_AMT")));
                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_SB_PERIOD, CommonUtil.convertObjToStr(renewalMap.get("SB_PERIOD_RUN")));
                            boolean flag = false;
                            if (CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_WITHDRAWING")).equals("Y")) {
                                setCboRenewalDepTransMode(CommonUtil.convertObjToStr(getCbmRenewalDepTransMode().getDataForKey(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TRANS_MODE")))));
                                if (renewalMap.get("RENEWAL_DEP_TRANS_MODE") != null && renewalMap.get("RENEWAL_DEP_TRANS_MODE").equals("CASH")) {
                                    renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_WITHDRAWING_DEPAMT_TOKENO, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TOKEN_NO")));
                                    renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_DEP_PAY_MODE, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TRANS_MODE")));
                                } else if (renewalMap.get("RENEWAL_DEP_TRANS_MODE") != null && renewalMap.get("RENEWAL_DEP_TRANS_MODE").equals("TRANSFER")) {
                                    if (CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_INT_WITHDRAWING")).equals("Y") && CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_WITHDRAWING")).equals("Y") && renewalMap.get("RENEWAL_DEP_TRANS_PRODTYPE").equals("RM")) {
                                        flag = true;
                                        renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_INT_PAY_MODE, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TRANS_MODE")));
                                        renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_INT_PROD_TYPE, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TRANS_PRODTYPE")));
                                        renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_INT_PROD_ID, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TRANS_PRODID")));
                                        renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_INT_ACCT_NUM, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TRANS_ACCNO")));
                                        renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_WITHDRAWING_INTAMT_TOKENO, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TOKEN_NO")));
                                        setCboRenewalInterestTransProdType(CommonUtil.convertObjToStr(getCbmRenewalInterestTransProdType().getDataForKey(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TRANS_PRODTYPE")))));
                                        setCboRenewalInterestTransProdId(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TRANS_PRODID")));
                                        setRenewalcustomerIdCrInt(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TRANS_ACCNO")));
                                    }
                                    renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_DEP_PAY_MODE, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TRANS_MODE")));
                                    renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_DEP_PROD_TYPE, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TRANS_PRODTYPE")));
                                    renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_DEP_PROD_ID, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TRANS_PRODID")));
                                    renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_DEP_AC_NO, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TRANS_ACCNO")));
                                    HashMap accountNameMap = new HashMap();
                                    if (renewalMap.get("RENEWAL_DEP_TRANS_ACCNO") != null) {
                                        accountNameMap.put("ACC_NUM", CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TRANS_ACCNO")));
                                        if (!renewalMap.get("RENEWAL_DEP_TRANS_PRODTYPE").equals("") && !renewalMap.get("RENEWAL_DEP_TRANS_PRODTYPE").equals("RM")) {
                                            final List resultList = ClientUtil.executeQuery("getAccountNumberName" + renewalMap.get("RENEWAL_DEP_TRANS_PRODTYPE"), accountNameMap);
                                            if (resultList != null && resultList.size() > 0) {
                                                final HashMap resultMap = (HashMap) resultList.get(0);
                                                setRenewalcustomerNameCrValueDep(resultMap.get("CUSTOMER_NAME").toString());
                                                renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_DEP_CUST_NAME, resultMap.get("CUSTOMER_NAME").toString());
                                            }
                                        } else {
                                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_WITHDRAWING_DEPAMT_TOKENO, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TOKEN_NO")));
                                        }
                                        accountNameMap = null;
                                        setCboRenewalDepTransProdType(CommonUtil.convertObjToStr(getCbmRenewalDepTransProdType().getDataForKey(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TRANS_PRODTYPE")))));
                                        setCboRenewalDepTransProdId(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TRANS_PRODID")));
                                        setRenewalcustomerIdCrDep(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TRANS_ACCNO")));
                                    }
                                }
                            }
                            if (CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_INT_WITHDRAWING")).equals("Y") && flag == false) {
                                setCboRenewalInterestTransMode(CommonUtil.convertObjToStr(getCbmRenewalInterestTransMode().getDataForKey(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_INT_TRANS_MODE")))));
                                if (renewalMap.get("RENEWAL_INT_TRANS_MODE") != null && renewalMap.get("RENEWAL_INT_TRANS_MODE").equals("CASH")) {
                                    renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_WITHDRAWING_INTAMT_TOKENO, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_INT_TOKEN_NO")));
                                    renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_INT_PAY_MODE, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_INT_TRANS_MODE")));
                                } else if (renewalMap.get("RENEWAL_INT_TRANS_MODE") != null && renewalMap.get("RENEWAL_INT_TRANS_MODE").equals("TRANSFER")) {
                                    //                                    if(renewalMap.get("RENEWAL_INT_TRANS_PRODTYPE")!=null && !renewalMap.get("RENEWAL_INT_TRANS_PRODTYPE").equals("RM")){
                                    renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_INT_PAY_MODE, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_INT_TRANS_MODE")));
                                    renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_INT_PROD_TYPE, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_INT_TRANS_PRODTYPE")));
                                    renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_INT_PROD_ID, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_INT_TRANS_PRODID")));
                                    renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_INT_ACCT_NUM, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_INT_TRANS_ACCNO")));
                                    HashMap accountNameMap = new HashMap();
                                    if (renewalMap.get("RENEWAL_INT_TRANS_ACCNO") != null && !renewalMap.get("RENEWAL_INT_TRANS_ACCNO").equals("")) {
                                        accountNameMap.put("ACC_NUM", CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_INT_TRANS_ACCNO")));
                                        if (!renewalMap.get("RENEWAL_INT_TRANS_PRODTYPE").equals("") && !renewalMap.get("RENEWAL_INT_TRANS_PRODTYPE").equals("RM")) {
                                            final List resultList = ClientUtil.executeQuery("getAccountNumberName" + renewalMap.get("RENEWAL_INT_TRANS_PRODTYPE"), accountNameMap);
                                            if (resultList != null && resultList.size() > 0) {
                                                final HashMap resultMap = (HashMap) resultList.get(0);
                                                setRenewalcustomerNameCrValueInt(resultMap.get("CUSTOMER_NAME").toString());
                                                renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_INT_CUST_NAME, CommonUtil.convertObjToStr(renewalMap.get("WITDRAWING_DEP_AMT")));
                                            }
                                        } else {
                                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_WITHDRAWING_INTAMT_TOKENO, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_INT_TOKEN_NO")));
                                        }
                                        accountNameMap = null;
                                        setCboRenewalInterestTransProdType(CommonUtil.convertObjToStr(getCbmRenewalInterestTransProdType().getDataForKey(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_INT_TRANS_PRODTYPE")))));
                                        setCboRenewalInterestTransProdId(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_INT_TRANS_PRODID")));
                                        setRenewalcustomerIdCrInt(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_INT_TRANS_ACCNO")));
                                    }
                                    //                                    }
                                }
                            }
                            //                            setCboRenewalInterestPaymentMode(CommonUtil.convertObjToStr(getCbmRenewalInterestPaymentMode().getDataForKey(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_PAY_MODE")))));
                            if (renewalMap.get("RENEWAL_PAY_MODE") != null && renewalMap.get("RENEWAL_PAY_MODE").equals("CASH")) {
                                renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_PAY_MODE, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_PAY_MODE")));
                            } else if (renewalMap.get("RENEWAL_PAY_MODE") != null && renewalMap.get("RENEWAL_PAY_MODE").equals("TRANSFER")) {
                                renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_PAY_MODE, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_PAY_MODE")));
                                renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_PROD_TYPE, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_PAY_PRODTYPE")));
                                renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_PROD_ID, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_PAY_PRODID")));
                                renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_AC_NO, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_PAY_ACCNO")));
                                //                                HashMap accountNameMap = new HashMap();
                                //                                if(renewalMap.get("RENEWAL_PAY_ACCNO")!=null){
                                //                                    accountNameMap.put("ACC_NUM",CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_PAY_ACCNO")));
                                //                                    if(!renewalMap.get("RENEWAL_PAY_PRODTYPE").equals("") && !renewalMap.get("RENEWAL_PAY_PRODTYPE").equals("RM")){
                                //                                        final List resultList = ClientUtil.executeQuery("getAccountNumberName"+renewalMap.get("RENEWAL_PAY_PRODTYPE"),accountNameMap);
                                //                                        if(resultList != null && resultList.size()>0){
                                //                                            final HashMap resultMap = (HashMap)resultList.get(0);
                                //                                            setRenewalcustomerNameCrValue(resultMap.get("CUSTOMER_NAME").toString());
                                //                                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_CUST_NAME,resultMap.get("CUSTOMER_NAME").toString());
                                //                                        }
                                //                                    }
                                //                                    accountNameMap = null;
                                //                                    setCboRenewalProdType(CommonUtil.convertObjToStr(getCbmRenewalProdType().getDataForKey(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_PAY_PRODTYPE")))));
                                //                                    setCboRenewalProdId(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_PAY_PRODID")));
                                //                                    setRenewalcustomerIdCr(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_PAY_ACCNO")));
                                //                                }
                            }
                            if (renewalMap.containsKey("RENEWAL_DEP_WITHDRAWING") && renewalMap.get("RENEWAL_DEP_WITHDRAWING").equals("Y")) {
                                setRdoRenewalWithdrawing_Yes(true);
                                renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_WITHDRAWING_DEPAMT, CommonUtil.convertObjToStr(renewalMap.get("WITDRAWING_DEP_AMT")));
                                setTxtRenewalDepTransAmtValue(CommonUtil.convertObjToStr(renewalMap.get("WITDRAWING_DEP_AMT")));
                            } else if (renewalMap.containsKey("RENEWAL_DEP_WITHDRAWING") && renewalMap.get("RENEWAL_DEP_WITHDRAWING").equals("N")) {
                                setRdoRenewalWithdrawing_No(true);
                            }
                            if (renewalMap.containsKey("RENEWAL_DEP_ADDING") && renewalMap.get("RENEWAL_DEP_ADDING").equals("Y")) {
                                setRdoRenewalAdding_Yes(true);
                                renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_ADDING_DEPAMT, CommonUtil.convertObjToStr(renewalMap.get("ADDING_DEP_AMT")));
                                setTxtRenewalDepTransAmtValue(CommonUtil.convertObjToStr(renewalMap.get("ADDING_DEP_AMT")));

//                              ADDED BY NIKHIL  
                                setCboRenewalDepTransMode(CommonUtil.convertObjToStr(getCbmRenewalDepTransMode().getDataForKey(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TRANS_MODE")))));
                                if (renewalMap.get("RENEWAL_DEP_TRANS_MODE") != null && renewalMap.get("RENEWAL_DEP_TRANS_MODE").equals("CASH")) {
//                                    renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_WITHDRAWING_DEPAMT_TOKENO ,CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TOKEN_NO")));
//                                    renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_DEP_PAY_MODE,CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TRANS_MODE")));
                                } else if (renewalMap.get("RENEWAL_DEP_TRANS_MODE") != null && renewalMap.get("RENEWAL_DEP_TRANS_MODE").equals("TRANSFER")) {

                                    setCboRenewalInterestTransProdType(CommonUtil.convertObjToStr(getCbmRenewalInterestTransProdType().getDataForKey(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TRANS_PRODTYPE")))));
                                    setCboRenewalInterestTransProdId(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TRANS_PRODID")));
                                    setRenewalcustomerIdCrInt(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TRANS_ACCNO")));
//                                    }
//                                    renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_DEP_PAY_MODE,CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TRANS_MODE")));
//                                    renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_DEP_PROD_TYPE,CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TRANS_PRODTYPE")));
//                                    renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_DEP_PROD_ID,CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TRANS_PRODID")));
//                                    renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_DEP_AC_NO,CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TRANS_ACCNO")));
                                    HashMap accountNameMap = new HashMap();
                                    if (renewalMap.get("RENEWAL_DEP_TRANS_ACCNO") != null) {
                                        accountNameMap.put("ACC_NUM", CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TRANS_ACCNO")));
                                        if (!renewalMap.get("RENEWAL_DEP_TRANS_PRODTYPE").equals("") && !renewalMap.get("RENEWAL_DEP_TRANS_PRODTYPE").equals("RM")) {
                                            final List resultList = ClientUtil.executeQuery("getAccountNumberName" + renewalMap.get("RENEWAL_DEP_TRANS_PRODTYPE"), accountNameMap);
                                            if (resultList != null && resultList.size() > 0) {
                                                final HashMap resultMap = (HashMap) resultList.get(0);
                                                setRenewalcustomerNameCrValueDep(resultMap.get("CUSTOMER_NAME").toString());
//                                                renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_DEP_CUST_NAME,resultMap.get("CUSTOMER_NAME").toString());
                                            }
                                        } else {
//                                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_WITHDRAWING_DEPAMT_TOKENO ,CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TOKEN_NO")));
                                        }
                                        accountNameMap = null;
                                        setCboRenewalDepTransProdType(CommonUtil.convertObjToStr(getCbmRenewalDepTransProdType().getDataForKey(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TRANS_PRODTYPE")))));
                                        setCboRenewalDepTransProdId(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TRANS_PRODID")));
                                        setRenewalcustomerIdCrDep(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TRANS_ACCNO")));
                                    }
                                }
//                    TILL HERE          ADDED BY NIKHIL                                
                            } else if (renewalMap.containsKey("RENEWAL_DEP_ADDING") && renewalMap.get("RENEWAL_DEP_ADDING").equals("N")) {
                                setRdoRenewalAdding_No(true);
                            }
                            if (renewalMap.containsKey("RENEWAL_INT_WITHDRAWING") && renewalMap.get("RENEWAL_INT_WITHDRAWING").equals("Y")) {
                                setRdoRenewalWithdrawingInt_Yes(true);
                                renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_WITHDRAWING_INTAMT, CommonUtil.convertObjToStr(renewalMap.get("WITHDRAWING_INT_AMT")));
                                setTxtRenewalIntAmtValue(CommonUtil.convertObjToStr(renewalMap.get("WITHDRAWING_INT_AMT")));
                            } else if (renewalMap.containsKey("RENEWAL_INT_WITHDRAWING") && renewalMap.get("RENEWAL_INT_WITHDRAWING").equals("N")) {
                                setRdoRenewalWithdrawingInt_No(true);
                            }
                            if (renewalMap.containsKey("RENEWAL_CALENDER_FREQ") && renewalMap.get("RENEWAL_CALENDER_FREQ").equals("Y")) {
                                setRdoRenewalCalenderFreq_Yes(true);
                                setCboRenewalCalenderFreqDay(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_CALENDER_DAY")));
                                renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_CALENDER_DAY, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_CALENDER_FREQ_DAY")));
                            } else if (renewalMap.containsKey("RENEWAL_CALENDER_FREQ") && renewalMap.get("RENEWAL_CALENDER_FREQ").equals("N")) {
                                setRdoRenewalCalenderFreq_No(true);
                            }
                            setTxtRenewalDepTransTokenNo(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TOKEN_NO")));
                            setTxtRenewalIntAmtValue(CommonUtil.convertObjToStr(renewalMap.get("WITHDRAWING_INT_AMT")));
                            setTxtRenewalIntTokenNoVal(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_INT_TOKEN_NO")));
                            if (oldDepositNo.equals(newDepositNo)) {
                                setRenewalDepDate(CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_DT")));
                            } else {
                                setRenewalDepDate(CommonUtil.convertObjToStr(calcDepSubNo.get("MATURITY_DT")));
                            }
                            //                            setRenewalDepositDateEAMode(CommonUtil.convertObjToStr(calcDepSubNo.get("RENEWAL_DEPOSIT_DT")));
                            if (productBehavesLike.equals(prodBehavesLikeCummulative)) {//--- here Maturity amount is the  Deposit Amount
                                setRenewalBalIntAmtVal(String.valueOf("0.0"));
                            } else {
                                setRenewalBalIntAmtVal(CommonUtil.convertObjToStr(renewalMap.get("BALANCE_INT_AMT")));
                            }
                            setRenewalBalIntAmt(CommonUtil.convertObjToStr(renewalMap.get("BALANCE_INT_AMT")));
                            setRenewalValPeriodRun(CommonUtil.convertObjToStr(renewalMap.get("SB_PERIOD_RUN")));
                            //                            setSBRateOfInterestValue(CommonUtil.convertObjToStr(renewalMap.get("SB_PERIOD_RUN")));
                            setRenewalSBIntAmtVal(CommonUtil.convertObjToStr(renewalMap.get("SB_INT_AMT")));
                            setRenewalSBIntRateVal(CommonUtil.convertObjToStr(renewalMap.get("PENDING_AMT_RATE")));
                            if (getViewTypeDeposit().equals("CLOSED_DEPOSIT")) {
                                setSBRateOfInterestValue(CommonUtil.convertObjToStr(renewalMap.get("SB_PERIOD_RUN")));
                                setSBInterestAmountValue(CommonUtil.convertObjToStr(renewalMap.get("SB_INT_AMT")));
                                //                                setClosedDateValue(CommonUtil.convertObjToStr(sameNoMap.get("CLOSE_DT")));
                            }
                            double balanceInt = CommonUtil.convertObjToDouble(renewalMap.get("BALANCE_INT_AMT")).doubleValue();
                            double sbInt = CommonUtil.convertObjToDouble(renewalMap.get("SB_INT_AMT")).doubleValue();
                            if (productBehavesLike.equals(prodBehavesLikeCummulative)) {//--- here Maturity amount is the  Deposit Amount
                                setRenewalInterestRepayAmtVal(String.valueOf(sbInt));
                            } else {
                                setRenewalInterestRepayAmtVal(String.valueOf(balanceInt + sbInt));
                            }
                            //                            setSBInterestAmountValue(CommonUtil.convertObjToStr(renewalMap.get("SB_INT_AMT")));
                            setRenewaltxtRateOfInterest(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_RATE_OF_INT")));
                            setTxtRenewalPrintedOption(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_PRINTING_NO")));
                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_RATE_OF_INT, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_RATE_OF_INT")));
                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_INT_PAY_FREQ, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_PAY_FREQ")));
                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_PRODUCT_ID, CommonUtil.convertObjToStr(getCbmRenewalDepositProdId().getDataForKey(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_PRODID")))));
                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_CATEGORY, CommonUtil.convertObjToStr(getCbmRenewalDepositCategory().getDataForKey(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_CATEGORY")))));
                            setCboRenewalDepositProdId(CommonUtil.convertObjToStr(getCbmRenewalDepositProdId().getDataForKey(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_PRODID")))));
                            setCboRenewalDepositCategory(CommonUtil.convertObjToStr(getCbmRenewalDepositCategory().getDataForKey(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_CATEGORY")))));
                            //                            setCboRenewalInterestPaymentFrequency(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_PAY_FREQ")));
                            //                            setCboRenewalInterestPaymentMode(CommonUtil.convertObjToStr(getCbmRenewalInterestPaymentMode().getDataForKey(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_PAY_MODE")))));
                            renewaldepSubNoAll.put(String.valueOf(i), renewaldepSubNoRec);

                            double totAmt = CommonUtil.convertObjToDouble(calcDepSubNo.get("TOT_INT_AMT")).doubleValue();
                            double drAmt = CommonUtil.convertObjToDouble(calcDepSubNo.get("TOTAL_INT_DRAWN")).doubleValue();
                            credited = CommonUtil.convertObjToDouble(calcDepSubNo.get("TOTAL_INT_CREDIT")).doubleValue();
                            drawn = CommonUtil.convertObjToDouble(calcDepSubNo.get("TOTAL_INT_DRAWN")).doubleValue();
                            double crAmt = CommonUtil.convertObjToDouble(calcDepSubNo.get("TOTAL_INT_CREDIT")).doubleValue();
                            totalIntCredit = CommonUtil.convertObjToDouble(calcDepSubNo.get("TOTAL_INT_CREDIT")).doubleValue();
                            balIntAmt = totAmt - drAmt;
                            double balAmt = crAmt - drAmt;
                            balIntAmt = (double) getNearest((long) (balIntAmt * 100), 100) / 100;
                            balAmt = (double) getNearest((long) (balAmt * 100), 100) / 100;
                            setTotalInterestPayableValue(String.valueOf(balAmt));
                            setBalanceInterestAmountValue(String.valueOf(balIntAmt));
                            HashMap newRenewMap = new HashMap();
                            newRenewMap.put("DEPOSIT_NO", renewalMap.get("DEPOSIT_NO"));
                            List newDet = ClientUtil.executeQuery("getSelectRenewalModeStatus", newRenewMap);
                            if (newDet != null && newDet.size() > 0) {
                                newRenewMap = (HashMap) newDet.get(0);
                                if (newRenewMap.get("MAT_ALERT_REPORT") != null && newRenewMap.get("MAT_ALERT_REPORT").equals("Y")) {
                                    setRdoRenewalMatAlert_report_Yes(true);
                                } else {
                                    setRdoRenewalMatAlert_report_No(true);
                                }
                                if (newRenewMap.get("AUTO_RENEWAL") != null && newRenewMap.get("AUTO_RENEWAL").equals("Y")) {
                                    setRdoRenewalAutoRenewal_Yes(true);
                                    if (newRenewMap.get("RENEW_WITH_INT") != null && newRenewMap.get("RENEW_WITH_INT").equals("Y")) {
                                        setRdoRenewalWith_intRenewal_Yes(true);
                                    } else {
                                        setRdoRenewalWith_intRenewal_No(true);
                                    }
                                } else {
                                    setRdoRenewalAutoRenewal_No(true);
                                }

                            }
                            newRenewMap = null;
                            renewalDepSubNoRow = new ArrayList();
                            if (!(CommonUtil.convertObjToStr(renewaldepSubNoRec.get(FLD_DEP_SUB_NO_STATUS)).equals(CommonConstants.STATUS_DELETED))) {//renewal..
                                renewalDepSubNoRow.add(0, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_SUB_NO")));
                                renewalDepSubNoRow.add(1, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEPOSIT_AMT")));
                                renewalDepSubNoRow.add(2, DateUtil.getStringDate((Date) renewalMap.get("RENEWAL_DEPOSIT_DT")));
                                renewalDepSubNoRow.add(3, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_TOT_INTAMT")));
                                tblRenewalDepSubNoAccInfo.insertRow(tblRenewalDepSubNoAccInfo.getRowCount(), renewalDepSubNoRow);
                            }
                            //                        renewaldepSubNoCount = (int)Integer.parseInt(CommonUtil.convertObjToStr(renewaldepSubNoRec.get(FLD_DEP_SUB_NO_DEP_SUB_NO)));
                        }
                        HashMap renewMap = new HashMap();
                        renewMap.put("PROD_ID", prodId);
                        lst = ClientUtil.executeQuery("getDepositsBackDatedDay", renewMap);
                        if (lst != null && lst.size() > 0) {
                            renewMap = (HashMap) lst.get(0);
                            backDateFreq = CommonUtil.convertObjToInt(renewMap.get("MAX_PDBKDT_RENEWAL"));
                        }
                    }
                    /*-----------------------------*/
                    HashMap extensionMap = new HashMap();
                    extensionMap.put("DEPOSIT_NO", calcDepSubNo.get("DEPOSIT_NO"));
                    List lstExtension = ClientUtil.executeQuery("getSelectExtensionDetails", extensionMap);
                    if (lstExtension != null && lstExtension.size() > 0) {
                        if (getViewTypeDeposit().equals("AUTHORIZE") || getViewTypeDeposit().equals("EDIT")) {
                            extensionMap = (HashMap) lstExtension.get(0);
                            extensiondepSubNoRec = new HashMap();
                            extensiondepSubNoAll = new HashMap();
                            HashMap accountNameMap = null;
                            setRdoOpeningMode_Normal(false);
                            setRdoOpeningMode_Extension(true);
                            Date depDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_DEPOSIT_DT")));
                            Date matDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_MATURITY_DT")));
                            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_DEP_SUB_NO, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_DEPOSIT_NO")));
                            setLblValRenewDep(CommonUtil.convertObjToStr(extensionMap.get("OLD_DEPOSIT_NO")));
                            setTxtDepsoitNo(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_DEPOSIT_NO")));
                            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_MAT_DT, matDt);
                            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_DEP_DT, depDt);
                            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_DEP_AMT, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_DEPOSIT_AMT")));
                            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_MAT_AMT, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_MATURITY_AMT")));
                            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_DEP_PER_DD, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_DEPOSIT_DAYS")));
                            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_DEP_PER_MM, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_DEPOSIT_MONTHS")));
                            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_DEP_PER_YY, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_DEPOSIT_YEARS")));
                            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_RATE_OF_INT, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_RATE_OF_INT")));
                            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_TOT_INT_AMT, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_TOT_INTAMT")));
                            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_PER_INT_AMT, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_PERIODIC_INT")));
                            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_BALANCE_INTAMT, CommonUtil.convertObjToStr(extensionMap.get("BALANCE_INTEREST_AMT")));
                            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_WITHDRAW_AMT_CALC, CommonUtil.convertObjToStr(extensionMap.get("WITHDRAW_AMT_CALC")));
                            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_BALANCE_AMT_CALC, CommonUtil.convertObjToStr(extensionMap.get("BALANCE_AMT_CALC")));
                            setCboExtensionTransMode(CommonUtil.convertObjToStr(getCbmExtensionTransMode().getDataForKey(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_TRANS_MODE")))));
                            if (extensionMap.get("EXTENSION_TRANS_MODE") != null && extensionMap.get("EXTENSION_TRANS_MODE").equals("CASH")) {
                                extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_WITHDRAWING_DEPAMT_TOKENO, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_DEP_TOKEN_NO")));
                                extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_DEP_PAY_MODE, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_TRANS_MODE")));
                            } else if (extensionMap.get("EXTENSION_TRANS_MODE") != null && extensionMap.get("EXTENSION_TRANS_MODE").equals("TRANSFER")) {
                                extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_DEP_PAY_MODE, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_TRANS_MODE")));
                                extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_DEP_PROD_TYPE, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_TRANS_PRODTYPE")));
                                extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_DEP_PROD_ID, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_TRANS_PRODID")));
                                extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_DEP_AC_NO, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_TRANS_ACCNO")));
                                accountNameMap = new HashMap();
                                if (extensionMap.get("EXTENSION_TRANS_ACCNO") != null) {
                                    accountNameMap.put("ACC_NUM", CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_TRANS_ACCNO")));
                                    if (!extensionMap.get("EXTENSION_TRANS_PRODTYPE").equals("") && !extensionMap.get("EXTENSION_TRANS_PRODTYPE").equals("RM")) {
                                        final List resultList = ClientUtil.executeQuery("getAccountNumberName" + extensionMap.get("EXTENSION_TRANS_PRODTYPE"), accountNameMap);
                                        if (resultList != null && resultList.size() > 0) {
                                            final HashMap resultMap = (HashMap) resultList.get(0);
                                            setExtensioncustomerNameCrValueDep(resultMap.get("CUSTOMER_NAME").toString());
                                            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_DEP_CUST_NAME, resultMap.get("CUSTOMER_NAME").toString());
                                        }
                                    }
                                    accountNameMap = null;
                                    setCboExtensionTransProdType(CommonUtil.convertObjToStr(getCbmExtensionTransProdType().getDataForKey(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_TRANS_PRODTYPE")))));
                                    setCbmExtensionTransProdId(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_TRANS_PRODTYPE")));
                                    setCboExtensionTransProdId((String) getCbmExtensionTransProdId().getDataForKey(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_TRANS_PRODID"))));
                                    setExtensioncustomerIdCrDep(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_TRANS_ACCNO")));
                                }
                            }
                            if (extensionMap.get("EXTENSION_INT_TRANS_MODE") != null && extensionMap.get("EXTENSION_INT_TRANS_MODE").equals("CASH")) {
                                extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_WITHDRAWING_INTAMT_TOKENO, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_INT_TOKEN_NO")));
                                extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_DEP_PAY_MODE, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_INT_TRANS_MODE")));
                            } else if (extensionMap.get("EXTENSION_INT_TRANS_MODE") != null && extensionMap.get("EXTENSION_INT_TRANS_MODE").equals("TRANSFER")) {
                                extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_DEP_PAY_MODE, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_INT_TRANS_MODE")));
                                extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_DEP_PROD_TYPE, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_INT_TRANS_PRODTYPE")));
                                extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_DEP_PROD_ID, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_INT_TRANS_PRODID")));
                                extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_DEP_AC_NO, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_INT_TRANS_ACCNO")));
                                accountNameMap = new HashMap();
                                accountNameMap.put("ACC_NUM", CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_INT_TRANS_ACCNO")));
                                final List resultList = ClientUtil.executeQuery("getAccountNumberName" + extensionMap.get("EXTENSION_INT_TRANS_PRODTYPE"), accountNameMap);
                                if (resultList != null && resultList.size() > 0) {
                                    final HashMap resultMap = (HashMap) resultList.get(0);
                                    setExtensioncustomerNameCrValueInt(resultMap.get("CUSTOMER_NAME").toString());
                                    extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_DEP_CUST_NAME, CommonUtil.convertObjToStr(extensionMap.get("WITDRAWING_DEP_AMT")));
                                }
                                accountNameMap = null;
                                setCboExtensionPaymentProdType(CommonUtil.convertObjToStr(getCbmExtensionPaymentProdType().getDataForKey(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_INT_TRANS_PRODTYPE")))));
                                setCboExtensionPaymentProdId(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_INT_TRANS_PRODID")));
                                setExtensioncustomerIdCrInt(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_INT_TRANS_ACCNO")));
                            }
                            setCboExtensionPaymentMode(CommonUtil.convertObjToStr(getCbmExtensionPaymentMode().getDataForKey(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_PAY_MODE")))));
                            if (extensionMap.get("EXTENSION_PAY_MODE") != null && extensionMap.get("EXTENSION_PAY_MODE").equals("CASH")) {
                                extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_PAY_MODE, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_PAY_MODE")));
                            } else if (extensionMap.get("EXTENSION_PAY_MODE") != null && extensionMap.get("EXTENSION_PAY_MODE").equals("TRANSFER")) {
                                extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_PAY_MODE, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_PAY_MODE")));
                                extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_PROD_TYPE, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_PAY_PRODTYPE")));
                                extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_PROD_ID, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_PAY_PRODID")));
                                extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_AC_NO, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_PAY_ACCNO")));
                                accountNameMap = new HashMap();
                                if (extensionMap.get("EXTENSION_PAY_ACCNO") != null) {
                                    accountNameMap.put("ACC_NUM", CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_PAY_ACCNO")));
                                    if (!extensionMap.get("EXTENSION_PAY_PRODTYPE").equals("") && !extensionMap.get("EXTENSION_PAY_PRODTYPE").equals("RM")) {
                                        final List resultList = ClientUtil.executeQuery("getAccountNumberName" + extensionMap.get("EXTENSION_PAY_PRODTYPE"), accountNameMap);
                                        if (resultList != null && resultList.size() > 0) {
                                            final HashMap resultMap = (HashMap) resultList.get(0);
                                            setExtensioncustomerNameCrValue(resultMap.get("CUSTOMER_NAME").toString());
                                            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_CUST_NAME, resultMap.get("CUSTOMER_NAME").toString());
                                        }
                                    }
                                    accountNameMap = null;
                                    setCboExtensionProdType(CommonUtil.convertObjToStr(getCbmExtensionProdType().getDataForKey(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_PAY_PRODTYPE")))));
                                    setCboExtensionProdId(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_PAY_PRODID")));
                                    setExtensioncustomerIdCr(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_PAY_ACCNO")));
                                }
                            }
                            double withdrawingDepAmt = 0.0;
                            if (extensionMap.containsKey("EXTENSION_DEP_WITHDRAWING") && extensionMap.get("EXTENSION_DEP_WITHDRAWING").equals("Y")) {
                                setRdoExtensionWithdrawing_Yes(true);
                                extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_WITHDRAWING_DEPAMT, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_WITDRAWING_DEPAMT")));
                                setTxtExtensionTransAmtValue(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_WITDRAWING_DEPAMT")));
                                withdrawingDepAmt = CommonUtil.convertObjToDouble(extensionMap.get("EXTENSION_WITDRAWING_DEPAMT")).doubleValue();
                            } else if (extensionMap.containsKey("EXTENSION_DEP_WITHDRAWING") && extensionMap.get("EXTENSION_DEP_WITHDRAWING").equals("N")) {
                                setRdoExtensionWithdrawing_No(true);

                                withdrawingDepAmt = 0.0;
                            }
                            if (extensionMap.containsKey("EXTENSION_PENALTY") && extensionMap.get("EXTENSION_PENALTY").equals("Y")) {
                                setRdoExtensionAdding_Yes(true);
                                extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_ADDING_DEPAMT, CommonUtil.convertObjToStr(extensionMap.get("ADDING_DEP_AMT")));
                            } else if (extensionMap.containsKey("EXTENSION_PENALTY") && extensionMap.get("EXTENSION_PENALTY").equals("N")) {
                                setRdoExtensionAdding_No(true);
                            }
                            double withdrawingIntAmt = 0.0;
                            if (extensionMap.containsKey("EXTENSION_INT_WITHDRAWING") && extensionMap.get("EXTENSION_INT_WITHDRAWING").equals("Y")) {
                                setRdoExtensionWithdrawingInt_Yes(true);
                                extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_WITHDRAWING_INTAMT, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_WITDRAWING_INTAMT")));
                                setTxtExtensionIntAmtValue(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_WITDRAWING_INTAMT")));
                                withdrawingIntAmt = CommonUtil.convertObjToDouble(extensionMap.get("EXTENSION_WITDRAWING_INTAMT")).doubleValue();
                            } else if (extensionMap.containsKey("EXTENSION_INT_WITHDRAWING") && extensionMap.get("EXTENSION_INT_WITHDRAWING").equals("N")) {
                                setRdoExtensionWithdrawingInt_No(true);
                                withdrawingIntAmt = 0.0;
                            }
                            setLblExtensionTotalRepayAmtValue(String.valueOf(withdrawingDepAmt + withdrawingIntAmt));
                            if (extensionMap.containsKey("EXTENSION_CALENDER_FREQ") && extensionMap.get("EXTENSION_CALENDER_FREQ").equals("Y")) {
                                setRdoExtensionCalenderFreq_Yes(true);
                                setCboExtensionCalenderFreqDay(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_CALENDER_DAY")));
                                extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_CALENDER_DAY, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_CALENDER_DAY")));
                            } else if (extensionMap.containsKey("EXTENSION_CALENDER_FREQ") && extensionMap.get("EXTENSION_CALENDER_FREQ").equals("N")) {
                                setRdoExtensionCalenderFreq_No(true);
                            }
                            setTxtExtensionTransTokenNo(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_TOKEN_NO")));
                            setExtensionDepDate(CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_DT")));
                            setExtensionPaymentFreqValue(CommonUtil.convertObjToStr(extensionMap.get("OLD_FREQUENCY")));
                            setExtensionActualPeriodRun(CommonUtil.convertObjToStr(extensionMap.get("OLD_PERIOD_RUN")));
                            setExtensionRateOfIntVal(CommonUtil.convertObjToStr(extensionMap.get("OLD_RATE_OF_INT")));
                            double actualrateOfInt = CommonUtil.convertObjToDouble(extensionMap.get("OLD_RATE_OF_INT")).doubleValue();
                            if (extensionMap.containsKey("EXTENSION_DEP_WITHDRAWING") && extensionMap.get("EXTENSION_DEP_WITHDRAWING").equals("Y")) {
                                setLblExtensionWithdrawIntAmtValue1(String.valueOf(actualrateOfInt - 1));
                                setLblExtensionWithdrawIntAmtValue2(String.valueOf(actualrateOfInt));
                            } else if (extensionMap.containsKey("EXTENSION_PENAL") && extensionMap.get("EXTENSION_PENAL").equals("N")) {
                                setLblExtensionWithdrawIntAmtValue2(String.valueOf(actualrateOfInt));
                                setLblExtensionWithdrawIntAmtValue1(String.valueOf(actualrateOfInt));
                            }
                            setExtensionBalIntAmtVal(CommonUtil.convertObjToStr(extensionMap.get("WITHDRAW_AMT_CALC")));
                            setExtensionWithdrawIntAmtVal(CommonUtil.convertObjToStr(extensionMap.get("BALANCE_AMT_CALC")));
                            setExtensionBalInterestAmtVal(CommonUtil.convertObjToStr(extensionMap.get("BALANCE_INTEREST_AMT")));
                            double amt = CommonUtil.convertObjToDouble(extensionMap.get("BALANCE_INTEREST_AMT")).doubleValue() - CommonUtil.convertObjToDouble(calcDepSubNo.get("TOTAL_INT_DRAWN")).doubleValue();

                            setExtensiontxtRateOfInterest(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_RATE_OF_INT")));
                            setTxtExtensionPrintedOption(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_PRINTING_NO")));
                            setSBInterestAmountValue(CommonUtil.convertObjToStr(extensionMap.get("SB_INT_AMT")));
                            setExtensiontxtRateOfInterest(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_RATE_OF_INT")));
                            //                            setTxtExtensionPrintedOption(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_PRINTING_NO")));
                            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_RATE_OF_INT, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_RATE_OF_INT")));
                            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_INT_PAY_FREQ, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_PAY_FREQ")));
                            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_PRODUCT_ID, CommonUtil.convertObjToStr(getCbmExtensionDepositProdId().getDataForKey(CommonUtil.convertObjToStr(renewalMap.get("EXTENSION_PROD_ID")))));
                            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_CATEGORY, CommonUtil.convertObjToStr(getCbmExtensionDepositCategory().getDataForKey(CommonUtil.convertObjToStr(renewalMap.get("EXTENSION_CATEGORY")))));
                            setCboExtensionDepositProdId(CommonUtil.convertObjToStr(getCbmExtensionDepositProdId().getDataForKey(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_PROD_ID")))));
                            setCboExtensionDepositCategory(CommonUtil.convertObjToStr(getCbmExtensionDepositCategory().getDataForKey(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_CATEGORY")))));
                            setCboExtensionPaymentFrequency(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_PAY_FREQ")));
                            double recalculate = CommonUtil.convertObjToDouble(extensionMap.get("RECALCUALTE_INTEREST_AMT")).doubleValue();
                            setExtensionBalanceIntValue(String.valueOf(recalculate));
                            setExtensionBalanceIntValue(String.valueOf(amt));
                            setCboExtensionPaymentMode(CommonUtil.convertObjToStr(getCbmExtensionPaymentMode().getDataForKey(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_PAY_MODE")))));
                            setCboExtensionTransMode(CommonUtil.convertObjToStr(getCbmExtensionPaymentMode().getDataForKey(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_TRANS_MODE")))));
                            extensiondepSubNoAll.put(String.valueOf(i), extensiondepSubNoRec);
                            HashMap newRenewMap = new HashMap();
                            newRenewMap.put("DEPOSIT_NO", extensionMap.get("EXTENSION_DEPOSIT_NO"));
                            List newDet = ClientUtil.executeQuery("getSelectRenewalModeStatus", newRenewMap);
                            if (newDet != null && newDet.size() > 0) {
                                newRenewMap = (HashMap) newDet.get(0);

                                if (newRenewMap.get("MAT_ALERT_REPORT") != null && newRenewMap.get("MAT_ALERT_REPORT").equals("Y")) {
                                    setRdoExtensionMatAlertReport_Yes(true);
                                } else {
                                    setRdoExtensionMatAlertReport_No(true);
                                }
                                if (newRenewMap.get("AUTO_RENEWAL") != null && newRenewMap.get("AUTO_RENEWAL").equals("Y")) {
                                    setRdoExtensionAutoRenewal_Yes(true);
                                    if (newRenewMap.get("RENEW_WITH_INT") != null && newRenewMap.get("RENEW_WITH_INT").equals("Y")) {
                                        setRdoExtensionWithIntAutoRenewal_Yes(true);
                                    } else {
                                        setRdoExtensionWithIntAutoRenewal_No(true);
                                    }
                                } else {
                                    setRdoExtensionAutoRenewal_No(true);
                                }
                                double totAmt = CommonUtil.convertObjToDouble(calcDepSubNo.get("TOT_INT_AMT")).doubleValue();
                                double drAmt = CommonUtil.convertObjToDouble(calcDepSubNo.get("TOTAL_INT_DRAWN")).doubleValue();
                                credited = CommonUtil.convertObjToDouble(calcDepSubNo.get("TOTAL_INT_CREDIT")).doubleValue();
                                drawn = CommonUtil.convertObjToDouble(calcDepSubNo.get("TOTAL_INT_DRAWN")).doubleValue();
                                double crAmt = CommonUtil.convertObjToDouble(calcDepSubNo.get("TOTAL_INT_CREDIT")).doubleValue();
                                totalIntCredit = CommonUtil.convertObjToDouble(calcDepSubNo.get("TOTAL_INT_CREDIT")).doubleValue();
                                balIntAmt = totAmt - drAmt;
                                double balAmt = crAmt - drAmt;
                                balIntAmt = (double) getNearest((long) (balIntAmt * 100), 100) / 100;
                                balAmt = (double) getNearest((long) (balAmt * 100), 100) / 100;
                                setTotalInterestPayableValue(String.valueOf(balAmt));
                                setBalanceInterestAmountValue(String.valueOf(balIntAmt));
                            }
                            newRenewMap = null;
                            extensionDepSubNoRow = new ArrayList();
                            if (!(CommonUtil.convertObjToStr(extensiondepSubNoRec.get(FLD_DEP_SUB_NO_STATUS)).equals(CommonConstants.STATUS_DELETED))) {//renewal..
                                extensionDepSubNoRow.add(0, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_SUB_NO")));
                                extensionDepSubNoRow.add(1, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_DEPOSIT_AMT")));
                                extensionDepSubNoRow.add(2, DateUtil.getStringDate((Date) extensionMap.get("EXTENSION_DEPOSIT_DT")));
                                extensionDepSubNoRow.add(3, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_TOT_INTAMT")));
                                tblExtensionDepSubNoAccInfo.insertRow(tblExtensionDepSubNoAccInfo.getRowCount(), extensionDepSubNoRow);
                            }
                            //                        renewaldepSubNoCount = (int)Integer.parseInt(CommonUtil.convertObjToStr(renewaldepSubNoRec.get(FLD_DEP_SUB_NO_DEP_SUB_NO)));

                        }
                    }
                    depSubNoRow = null;
                    depSubNoRec = null;
                    calcDepSubNo = null;
                    break;
                } else if (!getViewTypeDeposit().equals("CLOSED_DEPOSIT") && calcDepSubNo.get("ACCT_STATUS").equals("NEW")) {
                    double totAmt = CommonUtil.convertObjToDouble(calcDepSubNo.get("TOT_INT_AMT")).doubleValue();
                    double drAmt = CommonUtil.convertObjToDouble(calcDepSubNo.get("TOTAL_INT_DRAWN")).doubleValue();
                    credited = CommonUtil.convertObjToDouble(calcDepSubNo.get("TOTAL_INT_CREDIT")).doubleValue();
                    drawn = CommonUtil.convertObjToDouble(calcDepSubNo.get("TOTAL_INT_DRAWN")).doubleValue();
                    double crAmt = CommonUtil.convertObjToDouble(calcDepSubNo.get("TOTAL_INT_CREDIT")).doubleValue();
                    totalIntCredit = CommonUtil.convertObjToDouble(calcDepSubNo.get("TOTAL_INT_CREDIT")).doubleValue();
                    balIntAmt = totAmt - drAmt;
                    double balAmt = crAmt - drAmt;
                    balIntAmt = (double) getNearest((long) (balIntAmt * 100), 100) / 100;
                    balAmt = (double) getNearest((long) (balAmt * 100), 100) / 100;
                    setTotalInterestPayableValue(String.valueOf(balAmt));
                    setBalanceInterestAmountValue(String.valueOf(balIntAmt));
                    setAvailableBalanceValue("Rs." + " " + CommonUtil.convertObjToStr(calcDepSubNo.get("AVAILABLE_BALANCE")));
                    setClearBalanceValue("Rs." + " " + CommonUtil.convertObjToStr(calcDepSubNo.get("CLEAR_BALANCE")));
                    setTotalBalanceValue("Rs." + " " + CommonUtil.convertObjToStr(calcDepSubNo.get("TOTAL_BALANCE")));
                    setLastInterestPaidDateValue(CommonUtil.convertObjToStr(calcDepSubNo.get("LAST_INT_APPL_DT")));
                    setTotalInterestValue("Rs." + " " + CommonUtil.convertObjToStr(calcDepSubNo.get("TOT_INT_AMT")));
                    setPaidnterestAmountValue("Rs." + " " + CommonUtil.convertObjToStr(calcDepSubNo.get("TOTAL_INT_DRAWN")));
                    setAccHeadValue(CommonUtil.convertObjToStr(calcDepSubNo.get("INSTALL_TYPE")));
                    setLastInterestProvisionDateValue(CommonUtil.convertObjToStr(calcDepSubNo.get("LST_PROV_DT")));
                    //                    setRenewalDateValue(DateUtil.getStringDate((Date)calcDepSubNo.get("PAYMENT_DAY")));
                    setClosedDateValue(CommonUtil.convertObjToStr(calcDepSubNo.get("CLOSE_DT")));
                    setLblDelayedMonthValue(CommonUtil.convertObjToStr(calcDepSubNo.get("DELAYED_MONTH")));
                    setLblDelayedAmountValue("Rs." + " " + CommonUtil.convertObjToStr(calcDepSubNo.get("DELAYED_AMOUNT")));

                    //Standing Instruction Details...
                    HashMap standingInstnCreditMap = new HashMap();
                    standingInstnCreditMap.put("DEPOSIT_NO", calcDepSubNo.get("DEPOSIT_NO") + "_1");
                    List lstStand = ClientUtil.executeQuery("getSelectStandingInstnDetails", standingInstnCreditMap);
                    if (lstStand != null && lstStand.size() > 0) {
                        standingInstnCreditMap = (HashMap) lstStand.get(0);
                        setSICreatedDateValue(CommonUtil.convertObjToStr(standingInstnCreditMap.get("SI_DT")));
                        setSINoValue(CommonUtil.convertObjToStr(standingInstnCreditMap.get("SI_ID")));
                        setSIProductTypeValue(CommonUtil.convertObjToStr(standingInstnCreditMap.get("PROD_TYPE")));
                        setSIProductIdValue(CommonUtil.convertObjToStr(standingInstnCreditMap.get("PROD_ID")));
                        setSIAccountNoValue(CommonUtil.convertObjToStr(standingInstnCreditMap.get("ACCT_NO")));
                        setSIAmountValue(CommonUtil.convertObjToStr(standingInstnCreditMap.get("AMOUNT")));
                        setSIParticularsValue(CommonUtil.convertObjToStr(standingInstnCreditMap.get("PARTICULARS")));
                        double siFreq = CommonUtil.convertObjToDouble(standingInstnCreditMap.get("FREQUENCY")).doubleValue();
                        if (siFreq == 30) {
                            setSIFrequencyValue(CommonUtil.convertObjToStr("Monthly"));
                        } else if (siFreq == 90) {
                            setSIFrequencyValue(CommonUtil.convertObjToStr("Quarterly"));
                        } else if (siFreq == 180) {
                            setSIFrequencyValue(CommonUtil.convertObjToStr("Half Yearly"));
                        } else if (siFreq == 360) {
                            setSIFrequencyValue(CommonUtil.convertObjToStr("Yearly"));
                        } else if (siFreq == 60) {
                            setSIFrequencyValue(CommonUtil.convertObjToStr("2 Months"));
                        } else if (siFreq == 120) {
                            setSIFrequencyValue(CommonUtil.convertObjToStr("4 Months"));
                        } else if (siFreq == 150) {
                            setSIFrequencyValue(CommonUtil.convertObjToStr("5 Months"));
                        } else if (siFreq == 210) {
                            setSIFrequencyValue(CommonUtil.convertObjToStr("7 Months"));
                        } else if (siFreq == 240) {
                            setSIFrequencyValue(CommonUtil.convertObjToStr("8 Months"));
                        } else if (siFreq == 270) {
                            setSIFrequencyValue(CommonUtil.convertObjToStr("9 Months"));
                        } else if (siFreq == 300) {
                            setSIFrequencyValue(CommonUtil.convertObjToStr("10 Months"));
                        } else if (siFreq == 330) {
                            setSIFrequencyValue(CommonUtil.convertObjToStr("11 Months"));
                        }
                        setSIForwardCountValue(CommonUtil.convertObjToStr(standingInstnCreditMap.get("COUNT")));
                        setSIStartDateValue(CommonUtil.convertObjToStr(standingInstnCreditMap.get("SI_START_DT")));
                        setSIEndDateValue(CommonUtil.convertObjToStr(standingInstnCreditMap.get("SI_END_DT")));
                        setAcceptanceChargesValue(CommonUtil.convertObjToStr(standingInstnCreditMap.get("ACCEPTANCE_CHARGE")));
                        setSIFailureChargesValue(CommonUtil.convertObjToStr(standingInstnCreditMap.get("FAILURE_CHARGE")));
                        setSIExecutionChargesValue(CommonUtil.convertObjToStr(standingInstnCreditMap.get("EXEC_CHARGE")));
                    }

                    HashMap depositMap = new HashMap();
                    ArrayList lienList = new ArrayList();
                    depositMap.put("DEPOSIT_NO", calcDepSubNo.get("DEPOSIT_NO"));
                    List lst = ClientUtil.executeQuery("getPresentDepStatus", depositMap);
                    if (lst != null && lst.size() > 0) {
                        for (int k = 0; k < lst.size(); k++) {
                            depLienRow = new ArrayList();
                            depositMap = (HashMap) lst.get(k);
                            depLienRow.add(0, CommonUtil.convertObjToStr(depositMap.get("LIEN_NO")));
                            depLienRow.add(1, CommonUtil.convertObjToStr(depositMap.get("LIEN_AMOUNT")));
                            depLienRow.add(2, DateUtil.getStringDate((Date) depositMap.get("LIEN_DT")));
                            depLienRow.add(3, CommonUtil.convertObjToStr(depositMap.get("LIEN_AC_NO")));
                            depLienRow.add(4, CommonUtil.convertObjToStr(depositMap.get("STATUS")));
                            depLienRow.add(5, CommonUtil.convertObjToStr(depositMap.get("UNLIEN_DT")));
                            tblLienDetails.insertRow(tblLienDetails.getRowCount(), depLienRow);
                        }
                    }
                    HashMap tdsMap = new HashMap();
                    tdsMap.put("ACCT_NUM", calcDepSubNo.get("DEPOSIT_NO") + "_1");
                    lst = ClientUtil.executeQuery("getTdsDeductStatus", tdsMap);
                    if (lst != null && lst.size() > 0) {
                        tdsMap = (HashMap) lst.get(0);
                        setTdsAmountValue(CommonUtil.convertObjToStr(tdsMap.get("SUM(TDS_AMT)")));
                    }
                    //                    depSubNoRec.put(FLD_FOR_DB_YES_NO , YES_FULL_STR);
                    //                    i =0;
                    //                    depSubNoAll.put(String.valueOf(i), depSubNoRec);
                    //                    depSubNoRow = new ArrayList();
                    //
                    //                    //--- If the record is Not DELETED , Show it in Table
                    //                    if(!(CommonUtil.convertObjToStr(depSubNoRec.get(FLD_DEP_SUB_NO_STATUS)).equals(CommonConstants.STATUS_DELETED))){
                    //                        depSubNoRow.add(0, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_SUB_NO")));
                    //                        depSubNoRow.add(1, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_AMT")));
                    //                        depSubNoRow.add(2, DateUtil.getStringDate((Date)calcDepSubNo.get("MATURITY_DT")));
                    //                        depSubNoRow.add(3, CommonUtil.convertObjToStr(calcDepSubNo.get("TOT_INT_AMT")));
                    //                        tblDepSubNoAccInfo.insertRow(tblDepSubNoAccInfo.getRowCount(), depSubNoRow);
                    //                    }
                    //                    depSubNoCount= (int)Integer.parseInt(CommonUtil.convertObjToStr(depSubNoRec.get(FLD_DEP_SUB_NO_DEP_SUB_NO)));
                    HashMap renewalMap = new HashMap();
                    renewalMap.put("OLD_DEPOSIT_NO", calcDepSubNo.get("DEPOSIT_NO"));
                    List lstRenewal = ClientUtil.executeQuery("getSelectOldDepositRenewalDetails", renewalMap);
                    if (lstRenewal != null && lstRenewal.size() > 0) {
                        if ((getViewTypeDeposit().equals("AUTHORIZE") || getViewTypeDeposit().equals("EDIT"))) {
                            renewalMap = (HashMap) lstRenewal.get(0);
                            setRdoOpeningMode_Normal(false);
                            setRdoOpeningMode_Renewal(true);
                            renewaldepSubNoRec = new HashMap();
                            renewaldepSubNoAll = new HashMap();
                            String oldDepositNo = CommonUtil.convertObjToStr(renewalMap.get("OLD_DEPOSIT_NO"));
                            String newDepositNo = CommonUtil.convertObjToStr(renewalMap.get("DEPOSIT_NO"));
                            setRenewalValDepositSubNo(newDepositNo);
                            Date depDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEPOSIT_DT")));
                            Date matDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_MATURITY_DT")));
                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_DEP_SUB_NO, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_SUB_NO")));
                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_MAT_DT, matDt);
                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_DEP_DT, depDt);
                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_DEP_AMT, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEPOSIT_AMT")));
                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_DEP_NO, CommonUtil.convertObjToStr(renewalMap.get("DEPOSIT_NO")));
                            setTxtDepsoitNo(CommonUtil.convertObjToStr(renewalMap.get("DEPOSIT_NO")));
                            setLblValRenewDep(CommonUtil.convertObjToStr(renewalMap.get("OLD_DEPOSIT_NO")));
                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_MAT_AMT, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_MATURITY_AMT")));
                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_DEP_PER_DD, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEPOSIT_DAYS")));
                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_DEP_PER_MM, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEPOSIT_MONTHS")));
                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_DEP_PER_YY, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEPOSIT_YEARS")));
                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_RATE_OF_INT, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_RATE_OF_INT")));
                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_TOT_INT_AMT, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_TOT_INTAMT")));
                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_PER_INT_AMT, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_PERIODIC_INT")));
                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_INT_PAY_MODE, CommonUtil.convertObjToStr(renewalMap.get("INTPAY_MODE")));
                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_BALANCE_INTAMT, CommonUtil.convertObjToStr(renewalMap.get("BALANCE_INT_AMT")));
                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_SB_INTAMT, CommonUtil.convertObjToStr(renewalMap.get("SB_INT_AMT")));
                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_SB_PERIOD, CommonUtil.convertObjToStr(renewalMap.get("SB_PERIOD_RUN")));
                            if (renewalMap.get("RENEWAL_DEP_TRANS_MODE") != null && renewalMap.get("RENEWAL_DEP_TRANS_MODE").equals("CASH")) {
                                renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_WITHDRAWING_DEPAMT_TOKENO, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TOKEN_NO")));
                            } else if (renewalMap.get("RENEWAL_DEP_TRANS_MODE") != null && renewalMap.get("RENEWAL_DEP_TRANS_MODE").equals("TRANSFER")) {
                                renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_DEP_PAY_MODE, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TRANS_MODE")));
                                renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_DEP_PROD_TYPE, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TRANS_PRODTYPE")));
                                renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_DEP_PROD_ID, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TRANS_PRODID")));
                                renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_DEP_AC_NO, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TRANS_ACCNO")));
                                HashMap accountNameMap = new HashMap();
                                if (renewalMap.get("RENEWAL_INT_TRANS_ACCNO") != null) {
                                    accountNameMap.put("ACC_NUM", CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_INT_TRANS_ACCNO")));
                                    if (!renewalMap.get("RENEWAL_INT_TRANS_PRODTYPE").equals("") && !renewalMap.get("RENEWAL_INT_TRANS_PRODTYPE").equals("RM")) {
                                        final List resultList = ClientUtil.executeQuery("getAccountNumberName" + renewalMap.get("RENEWAL_INT_TRANS_PRODTYPE"), accountNameMap);
                                        if (resultList != null && resultList.size() > 0) {
                                            final HashMap resultMap = (HashMap) resultList.get(0);
                                            setRenewalcustomerNameCrValueDep(resultMap.get("CUSTOMER_NAME").toString());
                                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_INT_CUST_NAME, CommonUtil.convertObjToStr(renewalMap.get("WITDRAWING_DEP_AMT")));
                                        }
                                    }
                                    accountNameMap = null;
                                    setCboRenewalDepTransMode(CommonUtil.convertObjToStr(getCbmRenewalDepTransMode().getDataForKey(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TRANS_MODE")))));
                                    setCboRenewalDepTransProdType(CommonUtil.convertObjToStr(getCbmRenewalDepTransProdType().getDataForKey(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TRANS_PRODTYPE")))));
                                    setCboRenewalInterestTransProdId(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TRANS_PRODID")));
                                    setRenewalcustomerIdCrDep(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TRANS_ACCNO")));
                                }
                            }
                            if (renewalMap.get("RENEWAL_INT_TRANS_MODE") != null && renewalMap.get("RENEWAL_INT_TRANS_MODE").equals("CASH")) {
                                renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_WITHDRAWING_INTAMT_TOKENO, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_INT_TOKEN_NO")));
                            } else if (renewalMap.get("RENEWAL_INT_TRANS_MODE") != null && renewalMap.get("RENEWAL_INT_TRANS_MODE").equals("TRANSFER")) {
                                renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_INT_PAY_MODE, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_INT_TRANS_MODE")));
                                renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_INT_PROD_TYPE, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_INT_TRANS_PRODTYPE")));
                                renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_INT_PROD_ID, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_INT_TRANS_PRODID")));
                                renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_INT_ACCT_NUM, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_INT_TRANS_ACCNO")));
                                HashMap accountNameMap = new HashMap();
                                if (renewalMap.get("RENEWAL_INT_TRANS_ACCNO") != null) {
                                    accountNameMap.put("ACC_NUM", CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_INT_TRANS_ACCNO")));
                                    if (!renewalMap.get("RENEWAL_INT_TRANS_PRODTYPE").equals("") && !renewalMap.get("RENEWAL_INT_TRANS_PRODTYPE").equals("RM")) {
                                        final List resultList = ClientUtil.executeQuery("getAccountNumberName" + renewalMap.get("RENEWAL_INT_TRANS_PRODTYPE"), accountNameMap);
                                        if (resultList != null && resultList.size() > 0) {
                                            final HashMap resultMap = (HashMap) resultList.get(0);
                                            setRenewalcustomerNameCrValueInt(resultMap.get("CUSTOMER_NAME").toString());
                                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_INT_CUST_NAME, CommonUtil.convertObjToStr(renewalMap.get("WITDRAWING_DEP_AMT")));
                                        }
                                    }
                                    accountNameMap = null;
                                    setCboRenewalInterestTransMode(CommonUtil.convertObjToStr(getCbmRenewalInterestTransMode().getDataForKey(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_INT_TRANS_MODE")))));
                                    setCboRenewalInterestTransProdType(CommonUtil.convertObjToStr(getCbmRenewalInterestTransProdType().getDataForKey(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_INT_TRANS_PRODTYPE")))));
                                    setCboRenewalInterestTransProdId(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_INT_TRANS_PRODID")));
                                    setRenewalcustomerIdCrInt(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_INT_TRANS_ACCNO")));
                                }
                            }
                            if (renewalMap.get("RENEWAL_PAY_MODE") != null && renewalMap.get("RENEWAL_PAY_MODE").equals("CASH")) {
                                renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_PAY_MODE, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_PAY_MODE")));
                            } else if (renewalMap.get("RENEWAL_PAY_MODE") != null && renewalMap.get("RENEWAL_PAY_MODE").equals("TRANSFER")) {
                                renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_PAY_MODE, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_PAY_MODE")));
                                renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_PROD_TYPE, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_PAY_PRODTYPE")));
                                renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_PROD_ID, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_PAY_PRODID")));
                                renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_AC_NO, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_PAY_ACCNO")));
                                HashMap accountNameMap = new HashMap();
                                //                                if(renewalMap.get("RENEWAL_PAY_ACCNO")!=null){
                                //                                    accountNameMap.put("ACC_NUM",CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_PAY_ACCNO")));
                                //                                    if(!renewalMap.get("RENEWAL_PAY_PRODTYPE").equals("") && !renewalMap.get("RENEWAL_PAY_PRODTYPE").equals("RM")){
                                //                                        final List resultList = ClientUtil.executeQuery("getAccountNumberName"+renewalMap.get("RENEWAL_PAY_PRODTYPE"),accountNameMap);
                                //                                        if(resultList != null && resultList.size()>0){
                                //                                            final HashMap resultMap = (HashMap)resultList.get(0);
                                //                                            setRenewalcustomerNameCrValue(resultMap.get("CUSTOMER_NAME").toString());
                                //                                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_CUST_NAME,resultMap.get("CUSTOMER_NAME").toString());
                                //                                        }
                                //                                    }
                                //                                    accountNameMap = null;
                                //                                    setCboRenewalInterestPaymentMode(CommonUtil.convertObjToStr(getCbmRenewalInterestPaymentMode().getDataForKey(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_PAY_MODE")))));
                                //                                    setCboRenewalProdType(CommonUtil.convertObjToStr(getCbmRenewalProdType().getDataForKey(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_PAY_PRODTYPE")))));
                                //                                    setCboRenewalInterestTransProdId(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_PAY_PRODID")));
                                //                                    setRenewalcustomerIdCr(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_PAY_ACCNO")));
                                //                                }
                            }
                            if (renewalMap.containsKey("RENEWAL_DEP_WITHDRAWING") && renewalMap.get("RENEWAL_DEP_WITHDRAWING").equals("Y")) {
                                setRdoRenewalWithdrawing_Yes(true);
                                renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_WITHDRAWING_DEPAMT, CommonUtil.convertObjToStr(renewalMap.get("WITDRAWING_DEP_AMT")));
                                setTxtRenewalDepTransAmtValue(CommonUtil.convertObjToStr(renewalMap.get("WITDRAWING_DEP_AMT")));
                            } else if (renewalMap.containsKey("RENEWAL_DEP_WITHDRAWING") && renewalMap.get("RENEWAL_DEP_WITHDRAWING").equals("N")) {
                                setRdoRenewalWithdrawing_No(true);
                            }
                            if (renewalMap.containsKey("RENEWAL_DEP_ADDING") && renewalMap.get("RENEWAL_DEP_ADDING").equals("Y")) {
                                setRdoRenewalAdding_Yes(true);
                                renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_ADDING_DEPAMT, CommonUtil.convertObjToStr(renewalMap.get("ADDING_DEP_AMT")));
                                setTxtRenewalDepTransAmtValue(CommonUtil.convertObjToStr(renewalMap.get("ADDING_DEP_AMT")));
                            } else if (renewalMap.containsKey("RENEWAL_DEP_ADDING") && renewalMap.get("RENEWAL_DEP_ADDING").equals("N")) {
                                setRdoRenewalAdding_No(true);
                            }
                            if (renewalMap.containsKey("RENEWAL_INT_WITHDRAWING") && renewalMap.get("RENEWAL_INT_WITHDRAWING").equals("Y")) {
                                setRdoRenewalWithdrawingInt_Yes(true);
                                renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_WITHDRAWING_INTAMT, CommonUtil.convertObjToStr(renewalMap.get("WITHDRAWING_INT_AMT")));
                                setTxtRenewalIntAmtValue(CommonUtil.convertObjToStr(renewalMap.get("WITHDRAWING_INT_AMT")));
                            } else if (renewalMap.containsKey("RENEWAL_INT_WITHDRAWING") && renewalMap.get("RENEWAL_INT_WITHDRAWING").equals("N")) {
                                setRdoRenewalWithdrawingInt_No(true);
                            }
                            if (renewalMap.containsKey("RENEWAL_CALENDER_FREQ") && renewalMap.get("RENEWAL_CALENDER_FREQ").equals("Y")) {
                                setRdoRenewalCalenderFreq_Yes(true);
                                setCboRenewalCalenderFreqDay(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_CALENDER_DAY")));
                                renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_CALENDER_DAY, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_CALENDER_FREQ_DAY")));
                            } else if (renewalMap.containsKey("RENEWAL_CALENDER_FREQ") && renewalMap.get("RENEWAL_CALENDER_FREQ").equals("N")) {
                                setRdoRenewalCalenderFreq_No(true);
                            }
                            setTxtRenewalDepTransTokenNo(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEP_TOKEN_NO")));
                            setTxtRenewalIntAmtValue(CommonUtil.convertObjToStr(renewalMap.get("WITHDRAWING_INT_AMT")));
                            setTxtRenewalIntTokenNoVal(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_INT_TOKEN_NO")));
                            if (!oldDepositNo.equals(newDepositNo)) {
                                //                                setRenewalDepDate(CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_DT")));
                                //                            else
                                setRenewalDepDate(CommonUtil.convertObjToStr(calcDepSubNo.get("MATURITY_DT")));
                            }
                            //                            setRenewalDepositDateEAMode(CommonUtil.convertObjToStr(calcDepSubNo.get("RENEWAL_DEPOSIT_DT")));
                            if (productBehavesLike.equals(prodBehavesLikeCummulative)) {//--- here Maturity amount is the  Deposit Amount
                                setRenewalBalIntAmtVal(String.valueOf("0.0"));
                            } else {
                                setRenewalBalIntAmtVal(CommonUtil.convertObjToStr(renewalMap.get("BALANCE_INT_AMT")));
                            }
                            setRenewalBalIntAmt(CommonUtil.convertObjToStr(renewalMap.get("BALANCE_INT_AMT")));
                            double balanceInt = CommonUtil.convertObjToDouble(renewalMap.get("BALANCE_INT_AMT")).doubleValue();
                            double sbInt = CommonUtil.convertObjToDouble(renewalMap.get("SB_INT_AMT")).doubleValue();
                            if (productBehavesLike.equals(prodBehavesLikeCummulative)) {//--- here Maturity amount is the  Deposit Amount
                                setRenewalInterestRepayAmtVal(String.valueOf(sbInt));
                            } else {
                                setRenewalInterestRepayAmtVal(String.valueOf(balanceInt + sbInt));
                            }
                            setRenewalValPeriodRun(CommonUtil.convertObjToStr(renewalMap.get("SB_PERIOD_RUN")));
                            setRenewalSBIntAmtVal(CommonUtil.convertObjToStr(renewalMap.get("SB_INT_AMT")));
                            setRenewalSBIntRateVal(CommonUtil.convertObjToStr(renewalMap.get("PENDING_AMT_RATE")));
                            if (getViewTypeDeposit().equals("CLOSED_DEPOSIT")) {
                                setSBRateOfInterestValue(CommonUtil.convertObjToStr(renewalMap.get("SB_PERIOD_RUN")));
                                setSBInterestAmountValue(CommonUtil.convertObjToStr(renewalMap.get("SB_INT_AMT")));
                                //                                setClosedDateValue(CommonUtil.convertObjToStr(sameNoMap.get("CLOSE_DT")));
                            }
                            setRenewaltxtRateOfInterest(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_RATE_OF_INT")));
                            setTxtRenewalPrintedOption(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_PRINTING_NO")));
                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_RATE_OF_INT, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_RATE_OF_INT")));
                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_INT_PAY_FREQ, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_PAY_FREQ")));
                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_PRODUCT_ID, CommonUtil.convertObjToStr(getCbmRenewalDepositProdId().getDataForKey(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_PRODID")))));
                            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_CATEGORY, CommonUtil.convertObjToStr(getCbmRenewalDepositCategory().getDataForKey(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_CATEGORY")))));
                            setCboRenewalDepositProdId(CommonUtil.convertObjToStr(getCbmRenewalDepositProdId().getDataForKey(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_PRODID")))));
                            setCboRenewalDepositCategory(CommonUtil.convertObjToStr(getCbmRenewalDepositCategory().getDataForKey(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_CATEGORY")))));
                            //                            setCboRenewalInterestPaymentFrequency(CommonUtil.convertObjToStr(getCbmRenewalInterestPaymentFrequency().getDataForKey(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_PAY_FREQ")))));
                            setCboRenewalInterestPaymentMode(CommonUtil.convertObjToStr(getCbmRenewalInterestPaymentMode().getDataForKey(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_PAY_MODE")))));
                            renewaldepSubNoAll.put(String.valueOf(i), renewaldepSubNoRec);
                            HashMap newRenewMap = new HashMap();
                            newRenewMap.put("DEPOSIT_NO", renewalMap.get("DEPOSIT_NO"));
                            List newDet = ClientUtil.executeQuery("getSelectRenewalModeStatus", newRenewMap);
                            if (newDet != null && newDet.size() > 0) {
                                newRenewMap = (HashMap) newDet.get(0);

                                if (newRenewMap.get("MAT_ALERT_REPORT") != null && newRenewMap.get("MAT_ALERT_REPORT").equals("Y")) {
                                    setRdoRenewalMatAlert_report_Yes(true);
                                } else {
                                    setRdoRenewalMatAlert_report_No(true);
                                }
                                if (newRenewMap.get("AUTO_RENEWAL") != null && newRenewMap.get("AUTO_RENEWAL").equals("Y")) {
                                    setRdoRenewalAutoRenewal_Yes(true);
                                    if (newRenewMap.get("RENEW_WITH_INT") != null && newRenewMap.get("RENEW_WITH_INT").equals("Y")) {
                                        setRdoRenewalWith_intRenewal_Yes(true);
                                    } else {
                                        setRdoRenewalWith_intRenewal_No(true);
                                    }
                                } else {
                                    setRdoRenewalAutoRenewal_No(true);
                                }
                            }
                            newRenewMap = null;
                            if (oldDepositNo != null && newDepositNo != null && oldDepositNo.equals(newDepositNo)) {
                                HashMap sameNoMap = new HashMap();
                                sameNoMap.put("DEPOSIT NO", oldDepositNo);
                                lst = ClientUtil.executeQuery("getSelectSameDepSubNoAccInfoTO", sameNoMap);
                                if (lst != null && lst.size() > 0) {
                                    sameNoMap = (HashMap) lst.get(0);
                                    depSubNoRec = new HashMap();
                                    depSubNoRec.put(FLD_DEP_SUB_NO_DEP_SUB_NO, CommonUtil.convertObjToStr(sameNoMap.get("DEPOSIT_SUB_NO")));
                                    depSubNoRec.put(FLD_DEP_SUB_NO_MAT_DT, DateUtil.getStringDate((Date) sameNoMap.get("MATURITY_DT")));
                                    depSubNoRec.put(FLD_DEP_SUB_NO_MAT_AMT, CommonUtil.convertObjToStr(sameNoMap.get("MATURITY_AMT")));
                                    depSubNoRec.put(FLD_DEP_SUB_NO_RATE_OF_INT, CommonUtil.convertObjToStr(sameNoMap.get("RATE_OF_INT")));
                                    depSubNoRec.put(FLD_DEP_SUB_NO_TOT_INT_AMT, CommonUtil.convertObjToStr(sameNoMap.get("TOT_INT_AMT")));
                                    depSubNoRec.put(FLD_DEP_SUB_NO_PER_INT_AMT, CommonUtil.convertObjToStr(sameNoMap.get("PERIODIC_INT_AMT")));
                                    depSubNoRec.put(FLD_DEP_SUB_NO_DEP_DT, DateUtil.getStringDate((Date) sameNoMap.get("DEPOSIT_DT")));
                                    depSubNoRec.put(FLD_DEP_SUB_NO_DEP_AMT, CommonUtil.convertObjToStr(sameNoMap.get("DEPOSIT_AMT")));
                                    depSubNoRec.put(FLD_DEP_SUB_NO_DEP_PER_DD, CommonUtil.convertObjToStr(sameNoMap.get("DEPOSIT_PERIOD_DD")));
                                    depSubNoRec.put(FLD_DEP_SUB_NO_DEP_PER_MM, CommonUtil.convertObjToStr(sameNoMap.get("DEPOSIT_PERIOD_MM")));
                                    depSubNoRec.put(FLD_DEP_SUB_NO_DEP_PER_YY, CommonUtil.convertObjToStr(sameNoMap.get("DEPOSIT_PERIOD_YY")));
                                    depSubNoRec.put(FLD_DEP_SUB_NO_INT_PAY_FREQ, CommonUtil.convertObjToStr(sameNoMap.get("INTPAY_FREQ")));
                                    depSubNoRec.put(FLD_DEP_SUB_NO_INT_PAY_MODE, CommonUtil.convertObjToStr(sameNoMap.get("INTPAY_MODE")));
                                    depSubNoRec.put(FLD_DEP_SUB_NO_STATUS, CommonUtil.convertObjToStr(sameNoMap.get("STATUS")));
                                    depSubNoRec.put(FLD_DEP_SUB_INST_TYPE, CommonUtil.convertObjToStr(sameNoMap.get("INSTALL_TYPE")));
                                    depSubNoRec.put(FLD_DEP_SUB_PAY_TYPE, CommonUtil.convertObjToStr(sameNoMap.get("PAYMENT_TYPE")));
                                    depSubNoRec.put(FLD_DEP_SUB_PAY_DATE, DateUtil.getStringDate((Date) sameNoMap.get("PAYMENT_DAY")));
                                    depSubNoRec.put(FLD_DEP_SUB_INT_PROD_TYPE, CommonUtil.convertObjToStr(sameNoMap.get("INT_PAY_PROD_TYPE")));
                                    depSubNoRec.put(FLD_DEP_SUB_INT_PROD_ID, CommonUtil.convertObjToStr(sameNoMap.get("INT_PAY_PROD_ID")));
                                    depSubNoRec.put(FLD_DEP_SUB_INT_AC_NO, CommonUtil.convertObjToStr(sameNoMap.get("INT_PAY_ACC_NO")));
                                    setLastInterestPaidDateValue(CommonUtil.convertObjToStr(sameNoMap.get("LAST_INT_APPL_DT")));
                                    setLastInterestProvisionDateValue(CommonUtil.convertObjToStr(sameNoMap.get("LST_PROV_DT")));
                                    setRenewalDepDate(CommonUtil.convertObjToStr(sameNoMap.get("MATURITY_DT")));
                                    totAmt = CommonUtil.convertObjToDouble(sameNoMap.get("TOT_INT_AMT")).doubleValue();
                                    drAmt = CommonUtil.convertObjToDouble(sameNoMap.get("TOTAL_INT_DRAWN")).doubleValue();
                                    credited = CommonUtil.convertObjToDouble(sameNoMap.get("TOTAL_INT_CREDIT")).doubleValue();
                                    drawn = CommonUtil.convertObjToDouble(sameNoMap.get("TOTAL_INT_DRAWN")).doubleValue();
                                    crAmt = CommonUtil.convertObjToDouble(sameNoMap.get("TOTAL_INT_CREDIT")).doubleValue();
                                    totalIntCredit = CommonUtil.convertObjToDouble(sameNoMap.get("TOTAL_INT_CREDIT")).doubleValue();
                                    balIntAmt = totAmt - drAmt;
                                    balAmt = crAmt - drAmt;
                                    balIntAmt = (double) getNearest((long) (balIntAmt * 100), 100) / 100;
                                    balAmt = (double) getNearest((long) (balAmt * 100), 100) / 100;
                                    setTotalInterestPayableValue(String.valueOf(balAmt));
                                    setBalanceInterestAmountValue(String.valueOf(balIntAmt));
                                    setTotalInterestValue("Rs." + " " + CommonUtil.convertObjToStr(sameNoMap.get("TOT_INT_AMT")));
                                    if (CommonUtil.convertObjToStr(sameNoMap.get("CALENDER_FREQ")).equals(YES_STR)) {
                                        setRdoCalenderFreq_Yes(true);
                                        depSubNoRec.put(FLD_DEP_SUB_CALENDER_DAY, CommonUtil.convertObjToStr(sameNoMap.get("CALENDER_DAY")));
                                        depSubNoRec.put(FLD_DEP_SUB_CALENDER_FREQ, CommonUtil.convertObjToStr(sameNoMap.get("CALENDER_FREQ")));
                                        depSubNoRec.put(FLD_DEP_SUB_CALENDER_DATE, CommonUtil.convertObjToStr(sameNoMap.get("CALENDER_DATE")));
                                        setCalenderFreqDate(CommonUtil.convertObjToStr(sameNoMap.get("CALENDER_DATE")));
                                        setCboCalenderFreq(CommonUtil.convertObjToStr(sameNoMap.get("CALENDER_DAY")));
                                    } else {
                                        setRdoCalenderFreq_No(true);
                                        depSubNoRec.put(FLD_DEP_SUB_CALENDER_FREQ, CommonUtil.convertObjToStr(sameNoMap.get("CALENDER_FREQ")));
                                        setCalenderFreqDate("");
                                    }
                                }
                                depSubNoRec.put(FLD_FOR_DB_YES_NO, YES_FULL_STR);
                                i = 0;
                                depSubNoAll.put(String.valueOf(i), depSubNoRec);
                                depSubNoRow = new ArrayList();
                                if (!(CommonUtil.convertObjToStr(depSubNoRec.get(FLD_DEP_SUB_NO_STATUS)).equals(CommonConstants.STATUS_DELETED))) {
                                    depSubNoRow.add(0, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_SUB_NO")));
                                    depSubNoRow.add(1, CommonUtil.convertObjToStr(sameNoMap.get("DEPOSIT_AMT")));
                                    depSubNoRow.add(2, DateUtil.getStringDate((Date) sameNoMap.get("MATURITY_DT")));
                                    depSubNoRow.add(3, CommonUtil.convertObjToStr(sameNoMap.get("TOT_INT_AMT")));
                                    tblDepSubNoAccInfo.insertRow(tblDepSubNoAccInfo.getRowCount(), depSubNoRow);
                                }
                                sameNoMap = null;
                            } else {
                                depSubNoRec.put(FLD_FOR_DB_YES_NO, YES_FULL_STR);
                                i = 0;
                                depSubNoAll.put(String.valueOf(i), depSubNoRec);
                                depSubNoRow = new ArrayList();
                                if (!(CommonUtil.convertObjToStr(depSubNoRec.get(FLD_DEP_SUB_NO_STATUS)).equals(CommonConstants.STATUS_DELETED))) {
                                    depSubNoRow.add(0, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_SUB_NO")));
                                    depSubNoRow.add(1, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_AMT")));
                                    depSubNoRow.add(2, DateUtil.getStringDate((Date) calcDepSubNo.get("MATURITY_DT")));
                                    depSubNoRow.add(3, CommonUtil.convertObjToStr(calcDepSubNo.get("TOT_INT_AMT")));
                                    tblDepSubNoAccInfo.insertRow(tblDepSubNoAccInfo.getRowCount(), depSubNoRow);
                                }
                            }
                            renewalDepSubNoRow = new ArrayList();
                            if (!(CommonUtil.convertObjToStr(depSubNoRec.get(FLD_DEP_SUB_NO_STATUS)).equals(CommonConstants.STATUS_DELETED))) {//renewal..
                                renewalDepSubNoRow.add(0, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_SUB_NO")));
                                renewalDepSubNoRow.add(1, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEPOSIT_AMT")));
                                renewalDepSubNoRow.add(2, DateUtil.getStringDate((Date) renewalMap.get("RENEWAL_DEPOSIT_DT")));
                                renewalDepSubNoRow.add(3, CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_TOT_INTAMT")));
                                tblRenewalDepSubNoAccInfo.insertRow(tblRenewalDepSubNoAccInfo.getRowCount(), renewalDepSubNoRow);
                            }
                        }
                        HashMap renewMap = new HashMap();
                        renewMap.put("PROD_ID", prodId);
                        lst = ClientUtil.executeQuery("getDepositsBackDatedDay", renewMap);
                        if (lst != null && lst.size() > 0) {
                            renewMap = (HashMap) lst.get(0);
                            backDateFreq = CommonUtil.convertObjToInt(renewMap.get("MAX_PDBKDT_RENEWAL"));
                        }
                        //                        renewaldepSubNoCount = (int)Integer.parseInt(CommonUtil.convertObjToStr(renewaldepSubNoRec.get(FLD_DEP_SUB_NO_DEP_SUB_NO)));
                    } else {
                        depSubNoRec.put(FLD_FOR_DB_YES_NO, YES_FULL_STR);
                        i = 0;
                        depSubNoAll.put(String.valueOf(i), depSubNoRec);
                        depSubNoRow = new ArrayList();
                        //--- If the record is Not DELETED , Show it in Table
                        if (!(CommonUtil.convertObjToStr(depSubNoRec.get(FLD_DEP_SUB_NO_STATUS)).equals(CommonConstants.STATUS_DELETED))) {
                            depSubNoRow.add(0, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_SUB_NO")));
                            depSubNoRow.add(1, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_AMT")));
                            depSubNoRow.add(2, DateUtil.getStringDate((Date) calcDepSubNo.get("MATURITY_DT")));
                            depSubNoRow.add(3, CommonUtil.convertObjToStr(calcDepSubNo.get("TOT_INT_AMT")));
                            tblDepSubNoAccInfo.insertRow(tblDepSubNoAccInfo.getRowCount(), depSubNoRow);
                        }
                    }
                    depSubNoCount = (int) Integer.parseInt(CommonUtil.convertObjToStr(depSubNoRec.get(FLD_DEP_SUB_NO_DEP_SUB_NO)));
                    /*--------------------------------------------------------------------------------------------------------------------------------------*/
                    HashMap extensionMap = new HashMap();
                    extensionMap.put("DEPOSIT_NO", calcDepSubNo.get("DEPOSIT_NO"));
                    List lstExtension = ClientUtil.executeQuery("getSelectExtensionDetails", extensionMap);
                    if (lstExtension != null && lstExtension.size() > 0) {
                        if (getViewTypeDeposit().equals("AUTHORIZE") || getViewTypeDeposit().equals("EDIT")) {
                            extensionMap = (HashMap) lstExtension.get(0);
                            extensiondepSubNoRec = new HashMap();
                            extensiondepSubNoAll = new HashMap();
                            setRdoOpeningMode_Normal(false);
                            setRdoOpeningMode_Extension(true);
                            Date depDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_DEPOSIT_DT")));
                            Date matDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_MATURITY_DT")));
                            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_DEP_SUB_NO, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_DEPOSIT_NO")));
                            setLblValRenewDep(CommonUtil.convertObjToStr(extensionMap.get("OLD_DEPOSIT_NO")));
                            setTxtDepsoitNo(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_DEPOSIT_NO")));
                            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_MAT_DT, matDt);
                            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_DEP_DT, depDt);
                            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_DEP_AMT, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_DEPOSIT_AMT")));
                            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_MAT_AMT, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_MATURITY_AMT")));
                            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_DEP_PER_DD, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_DEPOSIT_DAYS")));
                            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_DEP_PER_MM, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_DEPOSIT_MONTHS")));
                            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_DEP_PER_YY, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_DEPOSIT_YEARS")));
                            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_RATE_OF_INT, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_RATE_OF_INT")));
                            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_TOT_INT_AMT, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_TOT_INTAMT")));
                            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_PER_INT_AMT, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_PERIODIC_INT")));
                            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_BALANCE_INTAMT, CommonUtil.convertObjToStr(extensionMap.get("BALANCE_INT_AMT")));
                            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_WITHDRAW_AMT_CALC, CommonUtil.convertObjToStr(extensionMap.get("SB_INT_AMT")));
                            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_BALANCE_AMT_CALC, CommonUtil.convertObjToStr(extensionMap.get("SB_PERIOD_RUN")));
                            setCboExtensionTransMode(CommonUtil.convertObjToStr(getCbmExtensionTransMode().getDataForKey(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_TRANS_MODE")))));
                            if (extensionMap.get("EXTENSION_TRANS_MODE") != null && extensionMap.get("EXTENSION_TRANS_MODE").equals("CASH")) {
                                extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_WITHDRAWING_DEPAMT_TOKENO, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_DEP_TOKEN_NO")));
                                extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_DEP_PAY_MODE, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_TRANS_MODE")));
                            } else if (extensionMap.get("EXTENSION_TRANS_MODE") != null && extensionMap.get("EXTENSION_TRANS_MODE").equals("TRANSFER")) {
                                extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_DEP_PAY_MODE, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_TRANS_MODE")));
                                extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_DEP_PROD_TYPE, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_TRANS_PRODTYPE")));
                                extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_DEP_PROD_ID, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_TRANS_PRODID")));
                                extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_DEP_AC_NO, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_TRANS_ACCNO")));
                                HashMap accountNameMap = new HashMap();
                                if (extensionMap.get("EXTENSION_TRANS_ACCNO") != null) {
                                    accountNameMap.put("ACC_NUM", CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_TRANS_ACCNO")));
                                    if (!extensionMap.get("EXTENSION_TRANS_PRODTYPE").equals("") && !extensionMap.get("EXTENSION_TRANS_PRODTYPE").equals("RM")) {
                                        final List resultList = ClientUtil.executeQuery("getAccountNumberName" + extensionMap.get("EXTENSION_TRANS_PRODTYPE"), accountNameMap);
                                        if (resultList != null && resultList.size() > 0) {
                                            final HashMap resultMap = (HashMap) resultList.get(0);
                                            setExtensioncustomerNameCrValueDep(resultMap.get("CUSTOMER_NAME").toString());
                                            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_DEP_CUST_NAME, resultMap.get("CUSTOMER_NAME").toString());
                                        }
                                    }
                                    accountNameMap = null;
                                    setCboExtensionTransProdType(CommonUtil.convertObjToStr(getCbmExtensionTransProdType().getDataForKey(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_TRANS_PRODTYPE")))));
                                    setCbmExtensionTransProdId(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_TRANS_PRODTYPE")));
                                    setCboExtensionTransProdId((String) getCbmExtensionTransProdId().getDataForKey(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_TRANS_PRODID"))));
                                    setExtensioncustomerIdCrDep(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_TRANS_ACCNO")));
                                }
                            }
                            setCboExtensionPaymentMode(CommonUtil.convertObjToStr(getCbmExtensionPaymentMode().getDataForKey(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_PAY_MODE")))));
                            if (extensionMap.get("EXTENSION_PAY_MODE") != null && extensionMap.get("EXTENSION_PAY_MODE").equals("CASH")) {
                                extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_PAY_MODE, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_PAY_MODE")));
                            } else if (extensionMap.get("EXTENSION_PAY_MODE") != null && extensionMap.get("EXTENSION_PAY_MODE").equals("TRANSFER")) {
                                extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_PAY_MODE, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_PAY_MODE")));
                                extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_PROD_TYPE, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_PAY_PRODTYPE")));
                                extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_PROD_ID, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_PAY_PRODID")));
                                extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_AC_NO, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_PAY_ACCNO")));
                                HashMap accountNameMap = new HashMap();
                                if (extensionMap.get("EXTENSION_PAY_ACCNO") != null) {
                                    accountNameMap.put("ACC_NUM", CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_PAY_ACCNO")));
                                    if (!extensionMap.get("EXTENSION_PAY_PRODTYPE").equals("") && !extensionMap.get("EXTENSION_PAY_PRODTYPE").equals("RM")) {
                                        final List resultList = ClientUtil.executeQuery("getAccountNumberName" + extensionMap.get("EXTENSION_PAY_PRODTYPE"), accountNameMap);
                                        if (resultList != null && resultList.size() > 0) {
                                            final HashMap resultMap = (HashMap) resultList.get(0);
                                            setExtensioncustomerNameCrValue(resultMap.get("CUSTOMER_NAME").toString());
                                            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_CUST_NAME, resultMap.get("CUSTOMER_NAME").toString());
                                        }
                                    }
                                    accountNameMap = null;
                                    setCboExtensionProdType(CommonUtil.convertObjToStr(getCbmExtensionProdType().getDataForKey(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_PAY_PRODTYPE")))));
                                    setCboExtensionProdId(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_PAY_PRODID")));
                                    setExtensioncustomerIdCr(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_PAY_ACCNO")));
                                }
                            }
                            if (extensionMap.containsKey("EXTENSION_DEP_WITHDRAWING") && extensionMap.get("EXTENSION_DEP_WITHDRAWING").equals("Y")) {
                                setRdoExtensionWithdrawing_Yes(true);
                                extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_WITHDRAWING_DEPAMT, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_WITDRAWING_DEPAMT")));
                                setTxtExtensionTransAmtValue(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_WITDRAWING_DEPAMT")));
                            } else if (extensionMap.containsKey("EXTENSION_DEP_WITHDRAWING") && extensionMap.get("EXTENSION_DEP_WITHDRAWING").equals("N")) {
                                setRdoExtensionWithdrawing_No(true);
                            }
                            if (extensionMap.containsKey("EXTENSION_PENALTY") && extensionMap.get("EXTENSION_PENALTY").equals("Y")) {
                                setRdoExtensionAdding_Yes(true);
                            } else if (extensionMap.containsKey("EXTENSION_PENALTY") && extensionMap.get("EXTENSION_PENALTY").equals("N")) {
                                setRdoExtensionAdding_No(true);
                            }
                            if (extensionMap.containsKey("EXTENSION_INT_WITHDRAWING") && extensionMap.get("EXTENSION_INT_WITHDRAWING").equals("Y")) {
                                setRdoExtensionWithdrawingInt_Yes(true);
                                extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_WITHDRAWING_INTAMT, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_WITDRAWING_INTAMT")));
                                setTxtExtensionIntAmtValue(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_WITDRAWING_INTAMT")));
                            } else if (extensionMap.containsKey("EXTENSION_INT_WITHDRAWING") && extensionMap.get("EXTENSION_INT_WITHDRAWING").equals("N")) {
                                setRdoExtensionWithdrawingInt_No(true);
                            }
                            if (extensionMap.containsKey("EXTENSION_CALENDER_FREQ") && extensionMap.get("EXTENSION_CALENDER_FREQ").equals("Y")) {
                                setRdoExtensionCalenderFreq_Yes(true);
                                setCboExtensionCalenderFreqDay(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_CALENDER_DAY")));
                                extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_CALENDER_DAY, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_CALENDER_DAY")));
                            } else if (extensionMap.containsKey("EXTENSION_CALENDER_FREQ") && extensionMap.get("EXTENSION_CALENDER_FREQ").equals("N")) {
                                setRdoExtensionCalenderFreq_No(true);
                            }
                            setTxtExtensionTransTokenNo(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_TOKEN_NO")));
                            setExtensionDepDate(CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_DT")));
                            setExtensionPaymentFreqValue(CommonUtil.convertObjToStr(extensionMap.get("OLD_FREQUENCY")));
                            setExtensionActualPeriodRun(CommonUtil.convertObjToStr(extensionMap.get("OLD_PERIOD_RUN")));
                            setExtensionRateOfIntVal(CommonUtil.convertObjToStr(extensionMap.get("OLD_RATE_OF_INT")));
                            double actualrateOfInt = CommonUtil.convertObjToDouble(extensionMap.get("OLD_RATE_OF_INT")).doubleValue();
                            if (extensionMap.containsKey("EXTENSION_PENAL") && extensionMap.get("EXTENSION_PENAL").equals("Y")) {
                                setLblExtensionWithdrawIntAmtValue1(String.valueOf(actualrateOfInt - 1));
                                setLblExtensionWithdrawIntAmtValue2(String.valueOf(actualrateOfInt));
                            } else if (extensionMap.containsKey("EXTENSION_PENAL") && extensionMap.get("EXTENSION_PENAL").equals("N")) {
                                setLblExtensionWithdrawIntAmtValue1(String.valueOf(actualrateOfInt));
                                setLblExtensionWithdrawIntAmtValue2(String.valueOf(actualrateOfInt));
                            }
                            setExtensionBalIntAmtVal(CommonUtil.convertObjToStr(extensionMap.get("WITHDRAW_AMT_CALC")));
                            setExtensionWithdrawIntAmtVal(CommonUtil.convertObjToStr(extensionMap.get("BALANCE_AMT_CALC")));
                            setExtensionBalInterestAmtVal(CommonUtil.convertObjToStr(extensionMap.get("BALANCE_INTEREST_AMT")));
                            setExtensiontxtRateOfInterest(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_RATE_OF_INT")));
                            setTxtExtensionPrintedOption(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_PRINTING_NO")));
                            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_RATE_OF_INT, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_RATE_OF_INT")));
                            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_INT_PAY_FREQ, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_PAY_FREQ")));
                            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_PRODUCT_ID, CommonUtil.convertObjToStr(getCbmExtensionDepositProdId().getDataForKey(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_PROD_ID")))));
                            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_CATEGORY, CommonUtil.convertObjToStr(getCbmExtensionDepositCategory().getDataForKey(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_CATEGORY")))));
                            setCboExtensionDepositProdId(CommonUtil.convertObjToStr(getCbmExtensionDepositProdId().getDataForKey(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_PROD_ID")))));
                            setCboExtensionDepositCategory(CommonUtil.convertObjToStr(getCbmExtensionDepositCategory().getDataForKey(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_CATEGORY")))));
                            setCboExtensionPaymentFrequency(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_PAY_FREQ")));
                            setCboExtensionPaymentMode(CommonUtil.convertObjToStr(getCbmExtensionPaymentMode().getDataForKey(CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_PAY_MODE")))));
                            //                            setExtensionBalanceIntValue(CommonUtil.convertObjToStr(extensionMap.get("RECALCUALTE_INTEREST_AMT")));
                            double recalculate = CommonUtil.convertObjToDouble(extensionMap.get("RECALCUALTE_INTEREST_AMT")).doubleValue();
                            setExtensionBalanceIntValue(String.valueOf(recalculate));
                            extensiondepSubNoAll.put(String.valueOf(i), extensiondepSubNoRec);
                            HashMap newRenewMap = new HashMap();
                            newRenewMap.put("DEPOSIT_NO", extensionMap.get("EXTENSION_DEPOSIT_NO"));
                            List newDet = ClientUtil.executeQuery("getSelectRenewalModeStatus", newRenewMap);
                            if (newDet != null && newDet.size() > 0) {
                                newRenewMap = (HashMap) newDet.get(0);

                                if (newRenewMap.get("MAT_ALERT_REPORT") != null && newRenewMap.get("MAT_ALERT_REPORT").equals("Y")) {
                                    setRdoExtensionMatAlertReport_Yes(true);
                                } else {
                                    setRdoExtensionMatAlertReport_No(true);
                                }
                                if (newRenewMap.get("AUTO_RENEWAL") != null && newRenewMap.get("AUTO_RENEWAL").equals("Y")) {
                                    setRdoExtensionAutoRenewal_Yes(true);
                                    if (newRenewMap.get("RENEW_WITH_INT") != null && newRenewMap.get("RENEW_WITH_INT").equals("Y")) {
                                        setRdoExtensionWithIntAutoRenewal_Yes(true);
                                    } else {
                                        setRdoExtensionWithIntAutoRenewal_No(true);
                                    }
                                } else {
                                    setRdoExtensionAutoRenewal_No(true);
                                }
                                totAmt = CommonUtil.convertObjToDouble(calcDepSubNo.get("TOT_INT_AMT")).doubleValue();
                                drAmt = CommonUtil.convertObjToDouble(calcDepSubNo.get("TOTAL_INT_DRAWN")).doubleValue();
                                credited = CommonUtil.convertObjToDouble(calcDepSubNo.get("TOTAL_INT_CREDIT")).doubleValue();
                                drawn = CommonUtil.convertObjToDouble(calcDepSubNo.get("TOTAL_INT_DRAWN")).doubleValue();
                                crAmt = CommonUtil.convertObjToDouble(calcDepSubNo.get("TOTAL_INT_CREDIT")).doubleValue();
                                totalIntCredit = CommonUtil.convertObjToDouble(calcDepSubNo.get("TOTAL_INT_CREDIT")).doubleValue();
                                balIntAmt = totAmt - drAmt;
                                balAmt = crAmt - drAmt;
                                balIntAmt = (double) getNearest((long) (balIntAmt * 100), 100) / 100;
                                balAmt = (double) getNearest((long) (balAmt * 100), 100) / 100;
                                setTotalInterestPayableValue(String.valueOf(balAmt));
                                setBalanceInterestAmountValue(String.valueOf(balIntAmt));
                            }
                            newRenewMap = null;
                            extensionDepSubNoRow = new ArrayList();
                            if (!(CommonUtil.convertObjToStr(extensiondepSubNoRec.get(FLD_DEP_SUB_NO_STATUS)).equals(CommonConstants.STATUS_DELETED))) {//renewal..
                                extensionDepSubNoRow.add(0, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_SUB_NO")));
                                extensionDepSubNoRow.add(1, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_DEPOSIT_AMT")));
                                extensionDepSubNoRow.add(2, DateUtil.getStringDate((Date) extensionMap.get("EXTENSION_DEPOSIT_DT")));
                                extensionDepSubNoRow.add(3, CommonUtil.convertObjToStr(extensionMap.get("EXTENSION_TOT_INTAMT")));
                                tblExtensionDepSubNoAccInfo.insertRow(tblExtensionDepSubNoAccInfo.getRowCount(), extensionDepSubNoRow);
                            }
                            //                        renewaldepSubNoCount = (int)Integer.parseInt(CommonUtil.convertObjToStr(renewaldepSubNoRec.get(FLD_DEP_SUB_NO_DEP_SUB_NO)));
                        }
                    }
                    depSubNoRow = null;
                    depSubNoRec = null;
                    renewaldepSubNoRec = null;
                    calcDepSubNo = null;
                    renewalDepSubNoRow = null;
                    break;
                }
            }
        }
    }

    public void flexiDepositDetails(String depositNo, Date unLienDt) {
        if (unLienDt != null && unLienDt.getDate() > 0) {
            HashMap flexiMap = new HashMap();
            flexiMap.put("DEPOSIT_NO", depositNo);
            flexiMap.put("CLOSE_DT", unLienDt);
            List lst = ClientUtil.executeQuery("getSelectDepSubNoIntDetailsSameNo", flexiMap);
            if (lst != null && lst.size() > 0) {
                flexiMap = (HashMap) lst.get(0);
                setClosedDateValue(CommonUtil.convertObjToStr(flexiMap.get("CLOSE_DT")));
                long period = 0;
                if (DateUtil.dateDiff((Date) flexiMap.get("CLOSE_DT"), (Date) flexiMap.get("MATURITY_DT")) >= 0) {
                    setClosingTypeValue(CommonUtil.convertObjToStr(CommonConstants.PREMATURE_CLOSURE));
                    period = DateUtil.dateDiff((Date) flexiMap.get("DEPOSIT_DT"), (Date) flexiMap.get("CLOSE_DT"));
                } else {
                    period = DateUtil.dateDiff((Date) flexiMap.get("DEPOSIT_DT"), (Date) flexiMap.get("MATURITY_DT"));
                    setClosingTypeValue(CommonUtil.convertObjToStr(CommonConstants.NORMAL_CLOSURE));
                }
                setDepositPeriodRunValue(String.valueOf(period) + " " + "Days");
                setClosingRateOfInterestValue(CommonUtil.convertObjToStr(flexiMap.get("CURR_RATE_OF_INT")));
                setClosingInterestAmountValue("Rs." + " " + CommonUtil.convertObjToStr(flexiMap.get("INTEREST_AMT")));
            }
        }
    }

    public String calculateMatDate(HashMap data) {
        int yearTobeAdded = 1900;
        int monthTobeAdded = 1;

        Date depDt = (Date) data.get("DEPOSIT_DT");
        int yrs = 0;
        int months = 0;
        int days = 0;
        if (data.get("DEPOSIT_PERIOD_DD").equals("")) {
            data.put("DEPOSIT_PERIOD_DD", "0");
        }
        if (data.get("DEPOSIT_PERIOD_MM").equals("")) {
            data.put("DEPOSIT_PERIOD_MM", "0");
        }
        if (data.get("DEPOSIT_PERIOD_YY").equals("")) {
            data.put("DEPOSIT_PERIOD_YY", "0");
        }
        yrs = Integer.parseInt(CommonUtil.convertObjToStr(data.get("DEPOSIT_PERIOD_YY")));
        months = Integer.parseInt(CommonUtil.convertObjToStr(data.get("DEPOSIT_PERIOD_MM")));
        days = Integer.parseInt(CommonUtil.convertObjToStr(data.get("DEPOSIT_PERIOD_DD")));
        GregorianCalendar cal = new GregorianCalendar((depDt.getYear() + yearTobeAdded), depDt.getMonth(), depDt.getDate());
        cal.add(GregorianCalendar.YEAR, yrs);
        cal.add(GregorianCalendar.MONTH, months);
        cal.add(GregorianCalendar.DAY_OF_MONTH, days);
        return DateUtil.getStringDate(cal.getTime());
    }

    private void setDepSubNoDetailsInExtension(List depSubNoDetailsList) {
        setAmt = true;
        getProductBehaveLike(prodId);
        tblDepSubNoAccInfo.setDataArrayList(null, tblDepSubNoColTitle);
        tblExtensionDepSubNoAccInfo.setDataArrayList(null, tblExtensionDepSubNoColTitle);
        tblLienDetails.setDataArrayList(null, tblLienColTitle);
        depSubNoAll = new HashMap();
        extensiondepSubNoAll = new HashMap();
        Date todaysDt = currDt;
        depDt = DateUtil.getStringDate(todaysDt);
        int period = 0;
        int depSubNoDetailsListSize = depSubNoDetailsList.size();
        HashMap calcDepSubNo;
        for (int i = 0; i < depSubNoDetailsListSize; i++) {
            calcDepSubNo = (HashMap) depSubNoDetailsList.get(i);
            HashMap detailsHash = new HashMap();
            //--- If Product Behaves Like Fixed, then Maturity Amount = Deposit Amount
            //--- here maturity amount is the Deposit amount in Renewal mode
            setLblValRenewDep(CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_NO")));
            setValExtensionDep(CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_NO")));
            setTxtDepsoitNo("Extension");
            Date depDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_DT")));
            Date matDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(calcDepSubNo.get("MATURITY_DT")));
            depAmt = Double.parseDouble(CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_AMT")));
            matAmount = Double.parseDouble(CommonUtil.convertObjToStr(calcDepSubNo.get("MATURITY_AMT")));
            outStandingBal = Double.parseDouble(CommonUtil.convertObjToStr(calcDepSubNo.get("TOTAL_BALANCE")));
            //--- For checking the Maturity Date in UI
            String paymtDt = setPaymentDt(calcDepSubNo);
            depSubNoRec = new HashMap(calcDepSubNo);

            depSubNoRec.put(FLD_DEP_SUB_NO_DEP_SUB_NO, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_SUB_NO")));
            depSubNoRec.put(FLD_DEP_SUB_NO_DEP_DT, depDt);
            depSubNoRec.put(FLD_DEP_SUB_NO_MAT_DT, matDt);
            depSubNoRec.put(FLD_DEP_SUB_NO_MAT_AMT, calcDepSubNo.get("MATURITY_AMT"));
            depSubNoRec.put(FLD_DEP_SUB_NO_RATE_OF_INT, CommonUtil.convertObjToStr(calcDepSubNo.get("RATE_OF_INT")));
            depSubNoRec.put(FLD_DEP_SUB_NO_TOT_INT_AMT, calcDepSubNo.get("TOT_INT_AMT"));
            depSubNoRec.put(FLD_DEP_SUB_NO_PER_INT_AMT, CommonUtil.convertObjToStr(calcDepSubNo.get("PERIODIC_INT_AMT")));
            depSubNoRec.put(FLD_DEP_SUB_NO_DEP_AMT, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_AMT")));
            depSubNoRec.put(FLD_DEP_SUB_NO_DEP_PER_DD, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_PERIOD_DD")));
            depSubNoRec.put(FLD_DEP_SUB_NO_DEP_PER_MM, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_PERIOD_MM")));
            depSubNoRec.put(FLD_DEP_SUB_NO_DEP_PER_YY, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_PERIOD_YY")));
            depSubNoRec.put(FLD_DEP_SUB_NO_INT_PAY_FREQ, CommonUtil.convertObjToStr(calcDepSubNo.get("INTPAY_FREQ")));
            depSubNoRec.put(FLD_DEP_SUB_NO_INT_PAY_MODE, CommonUtil.convertObjToStr(calcDepSubNo.get("INTPAY_MODE")));
            depSubNoRec.put(FLD_DEP_SUB_NO_STATUS, CommonUtil.convertObjToStr(calcDepSubNo.get("STATUS")));
            depSubNoRec.put(FLD_DEP_SUB_INST_TYPE, CommonUtil.convertObjToStr(calcDepSubNo.get("INSTALL_TYPE")));
            depSubNoRec.put(FLD_DEP_SUB_PAY_TYPE, CommonUtil.convertObjToStr(calcDepSubNo.get("PAYMENT_TYPE")));
            depSubNoRec.put(FLD_DEP_SUB_PAY_DATE, paymtDt);
            depSubNoRec.put(FLD_DEP_SUB_INT_PROD_TYPE, CommonUtil.convertObjToStr(calcDepSubNo.get("INT_PAY_PROD_TYPE")));
            depSubNoRec.put(FLD_DEP_SUB_INT_PROD_ID, CommonUtil.convertObjToStr(calcDepSubNo.get("INT_PAY_PROD_ID")));
            depSubNoRec.put(FLD_DEP_SUB_INT_AC_NO, CommonUtil.convertObjToStr(calcDepSubNo.get("INT_PAY_ACC_NO")));
            depSubNoRec.put(FLD_DEP_SUB_CALENDER_DAY, CommonUtil.convertObjToStr(calcDepSubNo.get("CALENDER_DAY")));
            depSubNoAll.put(String.valueOf(i), depSubNoRec);

            extensiondepSubNoRec = new HashMap(calcDepSubNo);
            HashMap calcMap = new HashMap();
            depDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_DT")));
            matDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(calcDepSubNo.get("MATURITY_DT")));
            calcMap.put("DEPOSIT_PERIOD_YY", CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_PERIOD_YY")));
            calcMap.put("DEPOSIT_PERIOD_MM", CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_PERIOD_MM")));
            calcMap.put("DEPOSIT_PERIOD_DD", CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_PERIOD_DD")));
            calcMap.put("DEPOSIT_DT", currDt.clone());
            calcMap = calculateMaturityDate(calcMap);
            matDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(calcMap.get("MATURITY_DT")));

            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_DEP_SUB_NO, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_SUB_NO")));
            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_MAT_DT, matDt);
            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_MAT_AMT, calcDepSubNo.get("MATURITY_AMT"));
            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_RATE_OF_INT, CommonUtil.convertObjToStr(calcDepSubNo.get("RATE_OF_INT")));
            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_TOT_INT_AMT, calcDepSubNo.get("TOT_INT_AMT"));
            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_PER_INT_AMT, CommonUtil.convertObjToStr(calcDepSubNo.get("PERIODIC_INT_AMT")));
            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_DEP_DT, depDt);
            //--- If Product Behaves Like Fixed, then Maturity Amount = Deposit Amount
            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_DEP_AMT, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_AMT")));
            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_DEP_PER_DD, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_PERIOD_DD")));
            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_DEP_PER_MM, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_PERIOD_MM")));
            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_DEP_PER_YY, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_PERIOD_YY")));
            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_DEP_PAY_MODE, CommonUtil.convertObjToStr(calcDepSubNo.get("INTPAY_MODE")));
            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_STATUS, CommonUtil.convertObjToStr(calcDepSubNo.get("STATUS")));
            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_DEP_PROD_TYPE, CommonUtil.convertObjToStr(calcDepSubNo.get("INT_PAY_PROD_TYPE")));
            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_DEP_PROD_ID, CommonUtil.convertObjToStr(calcDepSubNo.get("INT_PAY_PROD_ID")));
            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_DEP_ACCT_NUM, CommonUtil.convertObjToStr(calcDepSubNo.get("INT_PAY_ACC_NO")));
            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_CALENDER_DAY, CommonUtil.convertObjToStr(calcDepSubNo.get("CALENDER_DAY")));
            extensiondepSubNoRec.put(FLD_FOR_DB_YES_NO, NO_FULL_STR);
            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_PRODUCT_ID, prodId);
            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_CATEGORY, category);

            //--- This is for checking the record is coming from Database Table  or newly//--- entered in the UI
            extensiondepSubNoAll.put(String.valueOf(i), extensiondepSubNoRec);
            depSubNoRow = new ArrayList();
            setRdoExtensionMatAlertReport_Yes(true);
            setRdoExtensionAutoRenewal_No(true);
            setRdoExtensionCalenderFreq_No(true);
            //this details filled with the present Position Details tab....
            setAvailableBalanceValue("Rs." + " " + CommonUtil.convertObjToStr(calcDepSubNo.get("AVAILABLE_BALANCE")));
            setClearBalanceValue("Rs." + " " + CommonUtil.convertObjToStr(calcDepSubNo.get("CLEAR_BALANCE")));
            setTotalBalanceValue("Rs." + " " + CommonUtil.convertObjToStr(calcDepSubNo.get("TOTAL_BALANCE")));
            setLastInterestPaidDateValue(CommonUtil.convertObjToStr(calcDepSubNo.get("LAST_INT_APPL_DT")));
            setTotalInterestValue("Rs." + " " + CommonUtil.convertObjToStr(calcDepSubNo.get("TOT_INT_AMT")));
            setPaidnterestAmountValue("Rs." + " " + CommonUtil.convertObjToStr(calcDepSubNo.get("TOTAL_INT_DRAWN")));
            setExtensionAlreadyWithdrawn(CommonUtil.convertObjToStr(calcDepSubNo.get("TOTAL_INT_DRAWN")));
            setExtensionAlreadyCredited(CommonUtil.convertObjToStr(calcDepSubNo.get("TOTAL_INT_CREDIT")));
            setExtensionTotalIntAmt(CommonUtil.convertObjToStr(calcDepSubNo.get("TOT_INT_AMT")));
            setAccHeadValue(CommonUtil.convertObjToStr(calcDepSubNo.get("INSTALL_TYPE")));
            setLastInterestProvisionDateValue(CommonUtil.convertObjToStr(calcDepSubNo.get("LST_PROV_DT")));
            setClosedDateValue(CommonUtil.convertObjToStr(calcDepSubNo.get("CLOSE_DT")));
            if (calcDepSubNo.get("ACCT_STATUS").equals("CLOSED") && getViewTypeDeposit().equals("CLOSED_DEPOSIT")) {
            } else {
                HashMap renewMap = new HashMap();
                renewMap.put("PROD_ID", prodId);
                List lst = ClientUtil.executeQuery("getDepositsBackDatedDay", renewMap);
                if (lst != null && lst.size() > 0) {
                    renewMap = (HashMap) lst.get(0);
                    backDateFreq = CommonUtil.convertObjToInt(renewMap.get("MAX_PDBKDT_RENEWAL"));
                }
                Date dtBefRen = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(calcDepSubNo.get("MATURITY_DT")));
                double totAmt = CommonUtil.convertObjToDouble(calcDepSubNo.get("TOT_INT_AMT")).doubleValue();
                double drAmt = CommonUtil.convertObjToDouble(calcDepSubNo.get("TOTAL_INT_DRAWN")).doubleValue();
                double crAmt = CommonUtil.convertObjToDouble(calcDepSubNo.get("TOTAL_INT_CREDIT")).doubleValue();
                balIntAmt = totAmt - drAmt;
                double balAmt = crAmt - drAmt;
                balIntAmt = (double) getNearest((long) (balIntAmt * 100), 100) / 100;
                balAmt = (double) getNearest((long) (balAmt * 100), 100) / 100;
                setTotalInterestPayableValue(String.valueOf(balAmt));
                setBalanceInterestAmountValue(String.valueOf(balIntAmt));
                long diff = 0;
                long totalMonths = 0;
                long diffDay = 0;
                //                long difference = 0;
                HashMap prematureDateMap = new HashMap();
                prematureDateMap.put("FROM_DATE", depDt);
                prematureDateMap.put("TO_DATE", currDt);
                lst = ClientUtil.executeQuery("periodRunMap", prematureDateMap);
                if (lst != null && lst.size() > 0) {
                    prematureDateMap = (HashMap) lst.get(0);
                    totalMonths = CommonUtil.convertObjToLong(prematureDateMap.get("NO_OF_MONTHS"));
                    diffDay = CommonUtil.convertObjToLong(prematureDateMap.get("DAYS"));
                }
                double depAmount = CommonUtil.convertObjToDouble(calcDepSubNo.get("DEPOSIT_AMT")).doubleValue();
                HashMap paramMap = new HashMap();
                double interest = 0.0;
                double monthPeriod = 0.0;
                double yearPeriod = 0.0;
                String mthPeriod = null;
                String yrPeriod = null;
                String dysPeriod = null;
                long completedQuarter = 0;
                StringBuffer periodLetters = new StringBuffer();
                extension = false;
                double rateOfInt = setExtensionRateOfInterset(depDt, depAmount, extension);
                double freq = CommonUtil.convertObjToDouble(calcDepSubNo.get("INTPAY_FREQ")).doubleValue();
                HashMap custMap = new HashMap();
                custMap.put("DEPOSIT_NO", calcDepSubNo.get("DEPOSIT_NO"));
                List list = ClientUtil.executeQuery("getCustDepositNoBehavesLike", custMap);
                if (list != null && list.size() > 0) {
                    custMap = (HashMap) list.get(0);
                    double interestgreater = 0.0;
                    String discounted = CommonUtil.convertObjToStr(custMap.get("DISCOUNTED_RATE"));
                    if (custMap.containsKey("BEHAVES_LIKE") && custMap.get("BEHAVES_LIKE").equals("FIXED")) {
                        if (freq == 30) {
                            yrPeriod = String.valueOf(completedQuarter);
                            mthPeriod = String.valueOf(totalMonths);
                        } else if (freq == 90) {
                            completedQuarter = totalMonths / 3;
                            completedQuarter = (long) roundOffLower((long) (completedQuarter * 100), 100) / 100;
                            yrPeriod = String.valueOf(completedQuarter);
                            totalMonths = totalMonths - (completedQuarter * 3);
                            mthPeriod = String.valueOf(totalMonths);
                        } else if (freq == 180) {
                            completedQuarter = totalMonths / 6;
                            completedQuarter = (long) roundOffLower((long) (completedQuarter * 100), 100) / 100;
                            yrPeriod = String.valueOf(completedQuarter);
                            totalMonths = totalMonths - (completedQuarter * 6);
                            mthPeriod = String.valueOf(totalMonths);
                        } else if (freq == 360) {
                            completedQuarter = totalMonths / 12;
                            completedQuarter = (long) roundOffLower((long) (completedQuarter * 100), 100) / 100;
                            yrPeriod = String.valueOf(completedQuarter);
                            totalMonths = totalMonths - (completedQuarter * 12);
                            mthPeriod = String.valueOf(totalMonths);
                        } else {
                            diffDay = DateUtil.dateDiff(depDt, currDt);
                            yrPeriod = String.valueOf("0");
                            mthPeriod = String.valueOf("0");
                        }
                    } else if (custMap.get("BEHAVES_LIKE").equals("RECURRING")
                            || custMap.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
                        if (totalMonths >= 3) {
                            completedQuarter = totalMonths / 3;
                            completedQuarter = (long) roundOffLower((long) (completedQuarter * 100), 100) / 100;
                            yrPeriod = String.valueOf(completedQuarter);
                            totalMonths = totalMonths - (completedQuarter * 3);
                            mthPeriod = String.valueOf(totalMonths);
                            completedQuarter = completedQuarter * 3;
                            Date cummDepDate = null;
                            cummDepDate = depDt;
                            for (int j = 0; j < completedQuarter; j++) {
                                cummDepDate = DateUtil.addDays(cummDepDate, 30);
                            }
                            diffDay = DateUtil.dateDiff(cummDepDate, currDt);
                            mthPeriod = String.valueOf("0");
                        } else {
                            yrPeriod = "0";
                            mthPeriod = "0";
                            diffDay = DateUtil.dateDiff(depDt, currDt);
                        }
                    }
                    String temp = "0";
                    if (yrPeriod != null) {
                        temp = yrPeriod.toString();
                    }
                    if (custMap.get("BEHAVES_LIKE").equals("FIXED")) {
                        if (freq == 90) {
                            periodLetters.append(yrPeriod + " Qters ");
                        } else if (freq == 180) {
                            periodLetters.append(yrPeriod + " Half Yearly ");
                        } else if (freq == 360) {
                            periodLetters.append(yrPeriod + " Yearly ");
                        } else {
                            periodLetters.append(yrPeriod + " Qters ");
                        }
                    } else {
                        periodLetters.append(yrPeriod + " Qters ");
                    }
                    temp = "0";
                    if (mthPeriod != null) {
                        temp = mthPeriod.toString();
                    }
                    periodLetters.append(mthPeriod + " Months ");
                    dysPeriod = String.valueOf(diffDay);
                    temp = "0";
                    if (dysPeriod != null) {
                        temp = dysPeriod.toString();
                    }
                    periodLetters.append(dysPeriod + " Days ");
                    setExtensionActualPeriodRun(periodLetters.toString());
                    if (freq == 0) {
                        setExtensionPaymentFreqValue("Date of Maturity");
                    } else if (freq == 30) {
                        setExtensionPaymentFreqValue("Monthly");
                    } else if (freq == 90) {
                        setExtensionPaymentFreqValue("Quarterly");
                    } else if (freq == 180) {
                        setExtensionPaymentFreqValue("Half Yearly");
                    } else if (freq == 360) {
                        setExtensionPaymentFreqValue("Yearly");
                    } else if (freq == 60) {
                        setExtensionPaymentFreqValue(CommonUtil.convertObjToStr("2 Months"));
                    } else if (freq == 120) {
                        setExtensionPaymentFreqValue(CommonUtil.convertObjToStr("4 Months"));
                    } else if (freq == 150) {
                        setExtensionPaymentFreqValue(CommonUtil.convertObjToStr("5 Months"));
                    } else if (freq == 210) {
                        setExtensionPaymentFreqValue(CommonUtil.convertObjToStr("7 Months"));
                    } else if (freq == 240) {
                        setExtensionPaymentFreqValue(CommonUtil.convertObjToStr("8 Months"));
                    } else if (freq == 270) {
                        setExtensionPaymentFreqValue(CommonUtil.convertObjToStr("9 Months"));
                    } else if (freq == 300) {
                        setExtensionPaymentFreqValue(CommonUtil.convertObjToStr("10 Months"));
                    } else if (freq == 330) {
                        setExtensionPaymentFreqValue(CommonUtil.convertObjToStr("11 Months"));
                    }
                }
                setExtensionDepDate(CommonUtil.convertObjToStr(depDt));
                interest = (double) getNearest((long) (interest * 100), 100) / 100;
                setExtensionBalIntAmtVal(String.valueOf(interest));
                setExtensionRateOfIntVal(String.valueOf(rateOfInt));
                setExtensionCurrDt(CommonUtil.convertObjToStr(currDt));
            }
            //system.out.println("balIntAmt "+balIntAmt);
            HashMap depositMap = new HashMap();
            ArrayList lienList = new ArrayList();
            depositMap.put("DEPOSIT_NO", calcDepSubNo.get("DEPOSIT_NO"));
            List lst = ClientUtil.executeQuery("getPresentDepStatus", depositMap);
            if (lst != null && lst.size() > 0) {
                for (int k = 0; k < lst.size(); k++) {
                    depLienRow = new ArrayList();
                    depositMap = (HashMap) lst.get(k);
                    depLienRow.add(0, CommonUtil.convertObjToStr(depositMap.get("LIEN_NO")));
                    depLienRow.add(1, CommonUtil.convertObjToStr(depositMap.get("LIEN_AMOUNT")));
                    depLienRow.add(2, DateUtil.getStringDate((Date) depositMap.get("LIEN_DT")));
                    depLienRow.add(3, CommonUtil.convertObjToStr(depositMap.get("LIEN_AC_NO")));
                    depLienRow.add(4, CommonUtil.convertObjToStr(depositMap.get("STATUS")));
                    depLienRow.add(5, CommonUtil.convertObjToStr(depositMap.get("UNLIEN_DT")));
                    tblLienDetails.insertRow(tblLienDetails.getRowCount(), depLienRow);
                }
            }
            HashMap tdsMap = new HashMap();
            tdsMap.put("ACCT_NUM", calcDepSubNo.get("DEPOSIT_NO") + "_1");
            lst = ClientUtil.executeQuery("getTdsDeductStatus", tdsMap);
            if (lst != null && lst.size() > 0) {
                tdsMap = (HashMap) lst.get(0);
                setTdsAmountValue(CommonUtil.convertObjToStr(tdsMap.get("SUM(TDS_AMT)")));
            }
            //--- If the record is Not DELETED , Show it in Table
            if (!(CommonUtil.convertObjToStr(depSubNoRec.get(FLD_DEP_SUB_NO_STATUS)).equals(CommonConstants.STATUS_DELETED))) {
                depSubNoRow.add(0, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_SUB_NO")));
                depSubNoRow.add(1, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_AMT")));
                depSubNoRow.add(2, depDt);
                depSubNoRow.add(3, calcDepSubNo.get("TOT_INT_AMT"));
                tblDepSubNoAccInfo.insertRow(tblDepSubNoAccInfo.getRowCount(), depSubNoRow);
            }
            extensionDepSubNoRow = new ArrayList();
            if (!(CommonUtil.convertObjToStr(extensiondepSubNoRec.get(FLD_DEP_SUB_NO_STATUS)).equals(CommonConstants.STATUS_DELETED))) {//renewal..
                extensionDepSubNoRow.add(0, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_SUB_NO")));
                extensionDepSubNoRow.add(1, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_AMT")));
                extensionDepSubNoRow.add(2, depDt);
                extensionDepSubNoRow.add(3, CommonUtil.convertObjToStr(calcDepSubNo.get("TOT_INT_AMT")));
                tblExtensionDepSubNoAccInfo.insertRow(tblExtensionDepSubNoAccInfo.getRowCount(), extensionDepSubNoRow);
            }
            depSubNoCount = (int) Integer.parseInt(CommonUtil.convertObjToStr(depSubNoRec.get(FLD_DEP_SUB_NO_DEP_SUB_NO)));
            //            renewaldepSubNoCount = (int)Integer.parseInt(CommonUtil.convertObjToStr(renewaldepSubNoRec.get(FLD_DEP_SUB_NO_DEP_SUB_NO)));
            depSubNoRow = null;
            depSubNoRec = null;
            renewaldepSubNoRec = null;
            calcDepSubNo = null;
            renewalDepSubNoRow = null;
        }
        //system.out.println("@@@@@@ setDepSubNoDetails" + depSubNoDetailsList);
    }

    public HashMap calculateMaturityDate(HashMap data) {
        int yearTobeAdded = 1900;
        int monthTobeAdded = 1;

        Date depDt = (Date) data.get("DEPOSIT_DT");
        int yrs = 0;
        int months = 0;
        int days = 0;
        if (data.containsKey("DEPOSIT_PERIOD_DD") && data.get("DEPOSIT_PERIOD_DD").equals("")) {
            data.put("DEPOSIT_PERIOD_DD", "0");
        }
        if (data.containsKey("DEPOSIT_PERIOD_MM") && data.get("DEPOSIT_PERIOD_MM").equals("")) {
            data.put("DEPOSIT_PERIOD_MM", "0");
        }
        if (data.containsKey("DEPOSIT_PERIOD_YY") && data.get("DEPOSIT_PERIOD_YY").equals("")) {
            data.put("DEPOSIT_PERIOD_YY", "0");
        }
        yrs = Integer.parseInt(CommonUtil.convertObjToStr(data.get("DEPOSIT_PERIOD_YY")));
        months = Integer.parseInt(CommonUtil.convertObjToStr(data.get("DEPOSIT_PERIOD_MM")));
        days = Integer.parseInt(CommonUtil.convertObjToStr(data.get("DEPOSIT_PERIOD_DD")));
        GregorianCalendar cal = new GregorianCalendar((depDt.getYear() + yearTobeAdded), depDt.getMonth(), depDt.getDate());
        cal.add(GregorianCalendar.YEAR, yrs);
        cal.add(GregorianCalendar.MONTH, months);
        cal.add(GregorianCalendar.DAY_OF_MONTH, days);
        String matDt = DateUtil.getStringDate(cal.getTime());
        data.put("MATURITY_DT", matDt);
        return data;
        //       return DateUtil.getStringDate(cal.getTime());
    }

    public long getNearest(long number, long roundingFactor) {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor % 2) != 0) {
            roundingFactorOdd += 1;
        }
        long mod = number % roundingFactor;
        if ((mod < (roundingFactor / 2)) || (mod < (roundingFactorOdd / 2))) {
            return lower(number, roundingFactor);
        } else {
            return higher(number, roundingFactor);
        }
    }

    public long lower(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        return number - mod;
    }

    public long higher(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        if (mod == 0) {
            return number;
        }
        return (number - mod) + roundingFactor;
    }

    public HashMap calculateRenewalMaturityDate(HashMap data) {
        int yearTobeAdded = 1900;
        int monthTobeAdded = 1;

        Date depDt = (Date) data.get("DEPOSIT_DT");
        int yrs = 0;
        int months = 0;
        int days = 0;
        if (data.containsKey("DEPOSIT_PERIOD_DD") && data.get("DEPOSIT_PERIOD_DD").equals("")) {
            data.put("DEPOSIT_PERIOD_DD", "0");
        }
        if (data.containsKey("DEPOSIT_PERIOD_MM") && data.get("DEPOSIT_PERIOD_MM").equals("")) {
            data.put("DEPOSIT_PERIOD_MM", "0");
        }
        if (data.containsKey("DEPOSIT_PERIOD_YY") && data.get("DEPOSIT_PERIOD_YY").equals("")) {
            data.put("DEPOSIT_PERIOD_YY", "0");
        }
        yrs = Integer.parseInt(CommonUtil.convertObjToStr(data.get("DEPOSIT_PERIOD_YY")));
        months = Integer.parseInt(CommonUtil.convertObjToStr(data.get("DEPOSIT_PERIOD_MM")));
        days = Integer.parseInt(CommonUtil.convertObjToStr(data.get("DEPOSIT_PERIOD_DD")));
        double txtBoxPeriod = (double) days;
        String totMonths = String.valueOf(txtBoxPeriod / 365);
        long totyears = new Long(totMonths.substring(0, totMonths.indexOf("."))).longValue();
        double leftOverMth = new Double(totMonths.substring(totMonths.indexOf("."))).doubleValue();
        java.text.DecimalFormat df = new java.text.DecimalFormat("#####");
        leftOverMth = new Double(df.format(leftOverMth * 365)).doubleValue();
        if (totyears >= 1) {
            yrs = (int) totyears;
            days = (int) leftOverMth;
        }
        GregorianCalendar cal = new GregorianCalendar((depDt.getYear() + yearTobeAdded), depDt.getMonth(), depDt.getDate());
        cal.add(GregorianCalendar.YEAR, yrs);
        cal.add(GregorianCalendar.MONTH, months);
        cal.add(GregorianCalendar.DAY_OF_MONTH, days);
        String matDt = DateUtil.getStringDate(cal.getTime());
        data.put("MATURITY_DT", matDt);
        return data;
        //       return DateUtil.getStringDate(cal.getTime());
    }

    public HashMap simpleInterestCalculation(HashMap calculationMap) {
        //system.out.println("Simple Calculation : "+calculationMap);
        double rateOfInt = 0.0;
        double sbCalcAmt = 0.0;
        double principal = 0.0;
        Date depositDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(calculationMap.get("DEPOSIT_DT")));
        Date maturityDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(calculationMap.get("TO_DAY_DT")));
        double period = DateUtil.dateDiff(depositDate, maturityDate);
 
        if (period + 1 > backDateFreq) {
            HashMap sbInterestMap = new HashMap();
            sbInterestMap.put("PRODUCT_TYPE", "OA");
            HashMap sbProdIdMap = new HashMap();
            sbProdIdMap.put("BEHAVIOR", "SB");
            List lstProd = ClientUtil.executeQuery("getProdIdForOperative", sbProdIdMap);
            if (lstProd != null && lstProd.size() > 0) {
                HashMap prodMap = new HashMap();
                prodMap = (HashMap) lstProd.get(0);
                sbInterestMap.put("PROD_ID", prodMap.get("PROD_ID"));
            }
            //            if(getCboCategory().equalsIgnoreCase("GENERAL CATEGORY"))
            //                sbInterestMap.put("CATEGORY_ID","GENERAL_CATEGORY");
            //            else if(getCboCategory().equalsIgnoreCase("SENIOR CITIZENS"))
            //                sbInterestMap.put("CATEGORY_ID",category);
            //            else if(getCboCategory().equalsIgnoreCase("STAFF"))
            //                sbInterestMap.put("CATEGORY_ID","STAFF");
            //            else if(getCboCategory().equalsIgnoreCase("Ex-Serviceman"))
            //                sbInterestMap.put("CATEGORY_ID","EX_SERVICEMAN");
            //            else if(getCboCategory().equalsIgnoreCase("Ex-Staff"))
            //                sbInterestMap.put("CATEGORY_ID","EX_STAFF");

            sbInterestMap.put("CATEGORY_ID", category);
            sbInterestMap.put("DEPOSIT_DT", maturityDate);
            sbInterestMap.put("AMOUNT", CommonUtil.convertObjToDouble(calculationMap.get("DEPOSIT_AMT")));
            sbInterestMap.put("PERIOD", CommonUtil.convertObjToLong(calculationMap.get("PERIOD")));
            List lstInt = (List) ClientUtil.executeQuery("icm.getInterestRates", sbInterestMap);
            if (lstInt != null && lstInt.size() > 0) {
                HashMap sbRateOfInt = (HashMap) lstInt.get(0);
                rateOfInt = CommonUtil.convertObjToDouble(sbRateOfInt.get("ROI")).doubleValue();
            }
            principal = CommonUtil.convertObjToDouble(calculationMap.get("DEPOSIT_AMT")).doubleValue();
            sbCalcAmt = principal + (principal * rateOfInt * period) / (36500);
            sbCalcAmt = sbCalcAmt - principal;
            //            ClientUtil.showMessageWindow("This Deposit is Matured....\n" +
            //            "\n Matured Date is : " + depositDate +
            //            "\n CurrentDate is : " + String.valueOf(currDt) +
            //            "\n Principal : " + principal +
            //            "\n Period : " + period +
            //            "\n RateOf Interest : "+ rateOfInt +
            //            "\n Interest Amount is : " + sbCalcAmt);
            sbCalcAmt = (double) getNearest((long) (sbCalcAmt * 100), 100) / 100;
            calculationMap.put("CALC_AMT", new Double(sbCalcAmt));
            setRenewalSBIntRateVal(String.valueOf(rateOfInt));
            setSbIntAmount(sbCalcAmt);
            setSbPeriodRun(period);
        }
        return calculationMap;
    }

    private HashMap calcMatAmtAndROI(HashMap data) {
        double depAmt = 0;
        double roi = 0;
        double yr;
        int yrs = 0;
        int months = 0;
        int days = 0;
        yrs = Integer.parseInt(CommonUtil.convertObjToStr(data.get("DEPOSIT_PERIOD_YY")));
        months = Integer.parseInt(CommonUtil.convertObjToStr(data.get("DEPOSIT_PERIOD_MM")));
        days = Integer.parseInt(CommonUtil.convertObjToStr(data.get("DEPOSIT_PERIOD_DD")));
        depAmt = Double.parseDouble(CommonUtil.convertObjToStr(data.get("MATURITY_AMT")));
        roi = Double.parseDouble(CommonUtil.convertObjToStr(data.get("RATE_OF_INT")));
        yr = yrs;
        yr = yr + months / 12;
        yr = yr + days / 365;
        double secTerm = (1 + ((roi) * yr));
        double matAmt = depAmt * (float) secTerm;
        double totalIntAmt = matAmt - depAmt;
        HashMap retMap = new HashMap();
        retMap.put("Mat", new Double(matAmt));
        retMap.put("TotalIntAmt", new Double(totalIntAmt));
        return retMap;
    }

    private String setPaymentDt(HashMap data) {
        String paymtType = "";
        String depDt = "";
        String matDt = "";
        String chDt = "";
        String retDt = "";
        paymtType = CommonUtil.convertObjToStr(data.get("PAYMENT_TYPE"));
        depDt = CommonUtil.convertObjToStr(data.get("DEPOSIT_DT"));
        matDt = CommonUtil.convertObjToStr(data.get("MATURITY_DT"));
        if (paymtType.equals(DEPOSIT_DT)) {
            retDt = depDt;
        } else if (paymtType.equals(END_INSTALLMENT)) {
            retDt = matDt;
        } else if (paymtType.equals(CHOSEN_DT)) {
            retDt = matDt;
        }
        return retDt;
    }
    private void getAllOldDepositDetails(String dep_no) {
        tblOldDepDet.setDataArrayList(null, tblOldDepColTitle);
        HashMap depositMap = new HashMap();
        depositMap.put("DEPOSIT_NO", dep_no);
        List lst = ClientUtil.executeQuery("getAllSameDepSubNoAccInfo", depositMap);
        if (lst != null && lst.size() > 0) {
            for (int k = 0; k < lst.size(); k++) {
                oldDepAccDet = new ArrayList();
                depositMap = (HashMap) lst.get(k);
                oldDepAccDet.add(0, CommonUtil.convertObjToStr((k + 1)));
                oldDepAccDet.add(1, CommonUtil.convertObjToStr(depositMap.get("DEPOSIT_DT")));
                oldDepAccDet.add(2, DateUtil.getStringDate((Date) depositMap.get("MATURITY_DT")));
                oldDepAccDet.add(3, CommonUtil.convertObjToStr(depositMap.get("RATE_OF_INT")));
                oldDepAccDet.add(4, CommonUtil.convertObjToStr(depositMap.get("TOT_INT_AMT")));
                oldDepAccDet.add(5, CommonUtil.convertObjToStr(depositMap.get("DEPOSIT_AMT")));
                oldDepAccDet.add(6, CommonUtil.convertObjToStr(depositMap.get("MATURITY_AMT")));
                tblOldDepDet.insertRow(tblOldDepDet.getRowCount(), oldDepAccDet);
            }
        }
    }
    private void setDepSubNoDetailsInRenewal(List depSubNoDetailsList) {
        setAmt = true;
        getProductBehaveLike(prodId);
        tblDepSubNoAccInfo.setDataArrayList(null, tblDepSubNoColTitle);
        tblRenewalDepSubNoAccInfo.setDataArrayList(null, tblRenewalDepSubNoColTitle);
        tblLienDetails.setDataArrayList(null, tblLienColTitle);
        depSubNoAll = new HashMap();
        renewaldepSubNoAll = new HashMap();
        Date todaysDt = currDt;
        depDt = DateUtil.getStringDate(todaysDt);
        int period = 0;
        int depSubNoDetailsListSize = depSubNoDetailsList.size();
        HashMap calcDepSubNo;
        for (int i = 0; i < depSubNoDetailsListSize; i++) {
            calcDepSubNo = (HashMap) depSubNoDetailsList.get(i);
            HashMap detailsHash = new HashMap();
            //--- If Product Behaves Like Fixed, then Maturity Amount = Deposit Amount
            //--- here maturity amount is the Deposit amount in Renewal mode
            strDepDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_DT")));
            strMatDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(calcDepSubNo.get("MATURITY_DT")));
            depAmt = Double.parseDouble(CommonUtil.convertObjToStr(calcDepSubNo.get("TOTAL_BALANCE")));
            //            matAmount = Double.parseDouble(CommonUtil.convertObjToStr(calcDepSubNo.get("MATURITY_AMT")));
            //--- For checking the Maturity Date in UI
            String paymtDt = setPaymentDt(calcDepSubNo);
            depSubNoRec = new HashMap(calcDepSubNo);

            depSubNoRec.put(FLD_DEP_SUB_NO_DEP_SUB_NO, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_SUB_NO")));
            depSubNoRec.put(FLD_DEP_SUB_NO_DEP_DT, strDepDt);
            depSubNoRec.put(FLD_DEP_SUB_NO_MAT_DT, strMatDt);
            depSubNoRec.put(FLD_DEP_SUB_NO_MAT_AMT, CommonUtil.convertObjToStr(calcDepSubNo.get("MATURITY_AMT")));
            depSubNoRec.put(FLD_DEP_SUB_NO_RATE_OF_INT, CommonUtil.convertObjToStr(calcDepSubNo.get("RATE_OF_INT")));
            depSubNoRec.put(FLD_DEP_SUB_NO_TOT_INT_AMT, calcDepSubNo.get("TOT_INT_AMT"));
            depSubNoRec.put(FLD_DEP_SUB_NO_PER_INT_AMT, CommonUtil.convertObjToStr(calcDepSubNo.get("PERIODIC_INT_AMT")));
            //--- If Product Behaves Like Fixed, then Maturity Amount = Deposit Amount
            //            if(!productBehavesLike.equals(prodBehavesLikeFixed))//--- here Maturity amount is the  Deposit Amount
            //                depSubNoRec.put(FLD_DEP_SUB_NO_DEP_AMT , CommonUtil.convertObjToStr(calcDepSubNo.get("TOTAL_BALANCE")));
            //            else //--- here Deposit Amount is the  Deposit Amount
            depSubNoRec.put(FLD_DEP_SUB_NO_DEP_AMT, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_AMT")));

            depSubNoRec.put(FLD_DEP_SUB_NO_DEP_PER_DD, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_PERIOD_DD")));
            depSubNoRec.put(FLD_DEP_SUB_NO_DEP_PER_MM, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_PERIOD_MM")));
            depSubNoRec.put(FLD_DEP_SUB_NO_DEP_PER_YY, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_PERIOD_YY")));
            depSubNoRec.put(FLD_DEP_SUB_NO_INT_PAY_FREQ, CommonUtil.convertObjToStr(calcDepSubNo.get("INTPAY_FREQ")));
            depSubNoRec.put(FLD_DEP_SUB_NO_INT_PAY_MODE, CommonUtil.convertObjToStr(calcDepSubNo.get("INTPAY_MODE")));
            depSubNoRec.put(FLD_DEP_SUB_NO_STATUS, CommonUtil.convertObjToStr(calcDepSubNo.get("STATUS")));
            depSubNoRec.put(FLD_DEP_SUB_INST_TYPE, CommonUtil.convertObjToStr(calcDepSubNo.get("INSTALL_TYPE")));
            depSubNoRec.put(FLD_DEP_SUB_PAY_TYPE, CommonUtil.convertObjToStr(calcDepSubNo.get("PAYMENT_TYPE")));
            depSubNoRec.put(FLD_DEP_SUB_PAY_DATE, paymtDt);
            depSubNoRec.put(FLD_DEP_SUB_INT_PROD_TYPE, CommonUtil.convertObjToStr(calcDepSubNo.get("INT_PAY_PROD_TYPE")));
            depSubNoRec.put(FLD_DEP_SUB_INT_PROD_ID, CommonUtil.convertObjToStr(calcDepSubNo.get("INT_PAY_PROD_ID")));
            depSubNoRec.put(FLD_DEP_SUB_INT_AC_NO, CommonUtil.convertObjToStr(calcDepSubNo.get("INT_PAY_ACC_NO")));
            depSubNoRec.put(FLD_DEP_SUB_CALENDER_DAY, CommonUtil.convertObjToStr(calcDepSubNo.get("CALENDER_DAY")));
            depSubNoAll.put(String.valueOf(i), depSubNoRec);

            depSubNoRow = new ArrayList();
            //this details filled with the present Position Details tab....
            setAvailableBalanceValue("Rs." + " " + CommonUtil.convertObjToStr(calcDepSubNo.get("AVAILABLE_BALANCE")));
            setClearBalanceValue("Rs." + " " + CommonUtil.convertObjToStr(calcDepSubNo.get("CLEAR_BALANCE")));
            setTotalBalanceValue("Rs." + " " + CommonUtil.convertObjToStr(calcDepSubNo.get("TOTAL_BALANCE")));
            setLastInterestPaidDateValue(CommonUtil.convertObjToStr(calcDepSubNo.get("LAST_INT_APPL_DT")));
            setTotalInterestValue("Rs." + " " + CommonUtil.convertObjToStr(calcDepSubNo.get("TOT_INT_AMT")));
            setPaidnterestAmountValue("Rs." + " " + CommonUtil.convertObjToStr(calcDepSubNo.get("TOTAL_INT_DRAWN")));
            setAccHeadValue(CommonUtil.convertObjToStr(calcDepSubNo.get("INSTALL_TYPE")));
            setLastInterestProvisionDateValue(CommonUtil.convertObjToStr(calcDepSubNo.get("LST_PROV_DT")));
            setClosedDateValue(CommonUtil.convertObjToStr(calcDepSubNo.get("CLOSE_DT")));
            //            setRenewalDateValue(DateUtil.getStringDate((Date)calcDepSubNo.get("PAYMENT_DAY")));
            //            if(calcDepSubNo.get("ACCT_STATUS").equals("CLOSED") && getViewTypeDeposit().equals("CLOSED_DEPOSIT")){
            //                long diffperiod = 0;
            //                if(closeFlag == true){
            //                    if(DateUtil.dateDiff((Date)calcDepSubNo.get("CLOSE_DT"),(Date)calcDepSubNo.get("MATURITY_DT")) >= 0){
            //                        setClosingTypeValue(CommonUtil.convertObjToStr(CommonConstants.PREMATURE_CLOSURE));
            //                        diffperiod = DateUtil.dateDiff((Date)calcDepSubNo.get("DEPOSIT_DT"),(Date)calcDepSubNo.get("CLOSE_DT"));
            //                    }else{
            //                        diffperiod = DateUtil.dateDiff((Date)calcDepSubNo.get("DEPOSIT_DT"),(Date)calcDepSubNo.get("MATURITY_DT"));
            //                        setClosingTypeValue(CommonUtil.convertObjToStr(CommonConstants.NORMAL_CLOSURE));
            //                    }
            //                    setDepositPeriodRunValue(String.valueOf(diffperiod)+" "+"Days");
            //                }
            //                setBalanceInterestAmountValue(String.valueOf(0.0));
            //            }else{
            String basedOnRate = "";
            HashMap renewMap = new HashMap();
            renewMap.put("PROD_ID", prodId);
            List lst = ClientUtil.executeQuery("getDepositsBackDatedDay", renewMap);
            if (lst != null && lst.size() > 0) {
                renewMap = (HashMap) lst.get(0);
                backDateFreq = CommonUtil.convertObjToInt(renewMap.get("MAX_PDBKDT_RENEWAL"));
                basedOnRate = CommonUtil.convertObjToStr(renewMap.get("INT_RATE_APPLIED_OVERDUE"));
            }
            Date dtBefRen = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(calcDepSubNo.get("MATURITY_DT")));
            double totAmt = CommonUtil.convertObjToDouble(calcDepSubNo.get("TOT_INT_AMT")).doubleValue();
            double drAmt = CommonUtil.convertObjToDouble(calcDepSubNo.get("TOTAL_INT_DRAWN")).doubleValue();
            double crAmt = CommonUtil.convertObjToDouble(calcDepSubNo.get("TOTAL_INT_CREDIT")).doubleValue();
            balIntAmt = totAmt - drAmt;
            double balAmt = crAmt - drAmt;
            balIntAmt = (double) getNearest((long) (balIntAmt * 100), 100) / 100;
            balAmt = (double) getNearest((long) (balAmt * 100), 100) / 100;
            setTotalInterestPayableValue(String.valueOf(balAmt));
            setBalanceInterestAmountValue(String.valueOf(balIntAmt));
            long diff = 0;
            double sbAmount = 0.0;
            if ((DateUtil.dateDiff(dtBefRen, currDt) + 1) <= backDateFreq) {
                diff = DateUtil.dateDiff(strMatDt, currDt);
                depDt = CommonUtil.convertObjToStr(dtBefRen);
                calcDepSubNo.put("DEPOSIT_DT", DateUtil.getDateMMDDYYYY(depDt));
                matDt = calculateMatDate(calcDepSubNo);
            } else {
                calcDepSubNo.put("DEPOSIT_DT", currDt);
                matDt = calculateMatDate(calcDepSubNo);
                HashMap calculationMap = new HashMap();
                diff = DateUtil.dateDiff(strMatDt, currDt);
                calculationMap.put("DEPOSIT_DT", strMatDt);
                calculationMap.put("TO_DAY_DT", currDt);
                calculationMap.put("DEPOSIT_AMT", calcDepSubNo.get("MATURITY_AMT"));
                calculationMap.put("PERIOD", new Double(diff));
                if (basedOnRate.equals("") || basedOnRate.equals("Y")) {
                    HashMap calcMap = simpleInterestCalculation(calculationMap);
                    sbAmount = CommonUtil.convertObjToDouble(calcMap.get("CALC_AMT")).doubleValue();
                } else {
                    calculationMap.put("RENEWAL_CALCULATION", renewMap);
                    HashMap calcMap = depositRateInterestCalculation(calculationMap, prodId);
                    sbAmount = CommonUtil.convertObjToDouble(calcMap.get("CALC_AMT")).doubleValue();
                }
                depDt = CommonUtil.convertObjToStr(currDt);
            }
            setRenewalDepDate(CommonUtil.convertObjToStr(strMatDt));
            if (productBehavesLike.equals(prodBehavesLikeCummulative)) {//--- here Maturity amount is the  Deposit Amount
                setRenewalBalIntAmtVal(String.valueOf("0.0"));
            } else {
                setRenewalBalIntAmtVal(String.valueOf(balIntAmt));
            }
            setRenewalBalIntAmt(String.valueOf(balIntAmt));
            setRenewalValPeriodRun(String.valueOf(diff) + " " + "Days");
            setRenewalSBIntAmtVal(String.valueOf(sbAmount));
            if (productBehavesLike.equals(prodBehavesLikeCummulative)) {//--- here Maturity amount is the  Deposit Amount
                setRenewalInterestRepayAmtVal(String.valueOf(sbAmount));
            } else if (!productBehavesLike.equals(prodBehavesLikeCummulative)) {//--- here Maturity amount is the  Deposit Amount
                if (balIntAmt < 0) {
                    setRenewalInterestRepayAmtVal(String.valueOf(sbAmount));
                } else {
                    setRenewalInterestRepayAmtVal(String.valueOf(sbAmount + balIntAmt));
                }
            }
            setRdoRenewalMatAlert_report_Yes(true);
            setRdoRenewalAutoRenewal_No(true);
            renewaldepSubNoRec = new HashMap(calcDepSubNo);
            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_DEP_SUB_NO, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_SUB_NO")));
            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_MAT_DT, strMatDt);
            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_MAT_AMT, CommonUtil.convertObjToStr(calcDepSubNo.get("MATURITY_AMT")));
            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_RATE_OF_INT, CommonUtil.convertObjToStr(calcDepSubNo.get("RATE_OF_INT")));
            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_TOT_INT_AMT, CommonUtil.convertObjToStr(calcDepSubNo.get("TOT_INT_AMT")));
            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_PER_INT_AMT, CommonUtil.convertObjToStr(calcDepSubNo.get("PERIODIC_INT_AMT")));
            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_DEP_DT, strDepDt);
            //--- If Product Behaves Like Fixed, then Maturity Amount = Deposit Amount
            double maturityAmt = CommonUtil.convertObjToDouble(calcDepSubNo.get("MATURITY_AMT")).doubleValue();
            double outStanding = CommonUtil.convertObjToDouble(calcDepSubNo.get("TOTAL_BALANCE")).doubleValue();
            if (balIntAmt < 0) {
                matAmount = outStanding + balIntAmt;
                maturityAmt = matAmount;
                renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_DEP_AMT, String.valueOf(maturityAmt));
            } else {
                renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_DEP_AMT, CommonUtil.convertObjToDouble(calcDepSubNo.get("MATURITY_AMT")));
                matAmount = maturityAmt;
            }
            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_DEP_NO, "");
            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_DEP_PER_DD, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_PERIOD_DD")));
            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_DEP_PER_MM, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_PERIOD_MM")));
            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_DEP_PER_YY, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_PERIOD_YY")));
            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_INT_PAY_FREQ, CommonUtil.convertObjToStr(calcDepSubNo.get("INTPAY_FREQ")));
            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_INT_PAY_MODE, CommonUtil.convertObjToStr(calcDepSubNo.get("INTPAY_MODE")));
            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_STATUS, CommonUtil.convertObjToStr(calcDepSubNo.get("STATUS")));
            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_INT_PROD_TYPE, CommonUtil.convertObjToStr(calcDepSubNo.get("INT_PAY_PROD_TYPE")));
            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_INT_PROD_ID, CommonUtil.convertObjToStr(calcDepSubNo.get("INT_PAY_PROD_ID")));
            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_INT_ACCT_NUM, CommonUtil.convertObjToStr(calcDepSubNo.get("INT_PAY_ACC_NO")));
            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_CALENDER_DAY, CommonUtil.convertObjToStr(calcDepSubNo.get("CALENDER_DAY")));
            renewaldepSubNoRec.put(FLD_FOR_DB_YES_NO, NO_FULL_STR);
            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_PRODUCT_ID, prodId);
            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_CATEGORY, category);
            //--- This is for checking the record is coming from Database Table  or newly//--- entered in the UI
            renewaldepSubNoAll.put(String.valueOf(i), renewaldepSubNoRec);
            
            // For TDS
            System.out.println("balance Int amount = " + balIntAmt);
            if(balIntAmt > 0){
                double tdsAmt = balIntAmt;
                HashMap tdsCalcMap =  new HashMap();
                tdsCalcMap.put("TDS_CALCULATION", "TDS_CALCULATION");
                tdsCalcMap.put("CUST_ID", getTxtCustomerId());
                tdsCalcMap.put("PROD_ID", prodId);
                tdsCalcMap.put("DEPOSIT_NO", getTxtDepsoitNo());
                tdsCalcMap.put("RATE_OF_INT", calcDepSubNo.get("RATE_OF_INT"));
                tdsCalcMap.put("TDS_AMOUNT", new Double(tdsAmt));
                tdsCalcMap.put("BRANCH_CODE",ProxyParameters.BRANCH_ID);
                System.out.println("tdsCalcMap :: " + tdsCalcMap +"operationMap :: " + operationMap);
                try {
                    HashMap tdsData = proxy.executeQuery(tdsCalcMap, operationMap);
                    if (tdsData != null && tdsData.size() > 0) {
                      if(tdsData.containsKey("TDSDRAMT") && tdsData.get("TDSDRAMT") != null && CommonUtil.convertObjToDouble(tdsData.get("TDSDRAMT")) > 0){  
                        setLblTDSAmount(CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(tdsData.get("TDSDRAMT"))));
                        setTdsAcHd(CommonUtil.convertObjToStr(tdsData.get("TDSCrACHdId")));
                      }else{
                        setLblTDSAmount("0.0");  
                      }
                    }
                    System.out.println("tdsData :: " + tdsData);
                } catch (Exception ex) {
                    Logger.getLogger(TermDepositOB.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            // end

            HashMap paramMap = new HashMap();
            paramMap.put("CATEGORY_ID", category);
            paramMap.put("PROD_ID", prodId);
            paramMap.put("AMOUNT", String.valueOf(maturityAmt));
            paramMap.put("DEPOSIT_DT", DateUtil.getDateMMDDYYYY(depDt));
            //system.out.println("33333333 inside for loop setDepSubNoDetails calcDepSubNo" + calcDepSubNo);
            if ((CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_PERIOD_DD")) != null) && (!CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_PERIOD_DD")).equals(""))) {
                period = period + Integer.parseInt(CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_PERIOD_DD")));
            }
            if ((CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_PERIOD_MM")) != null) && (!CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_PERIOD_MM")).equals(""))) {
                period = period + Integer.parseInt(CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_PERIOD_MM"))) * 30;
            }
            if ((CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_PERIOD_YY")) != null) && (!CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_PERIOD_YY")).equals(""))) {
                period = period + Integer.parseInt(CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_PERIOD_YY"))) * 365;
            }
            paramMap.put("PERIOD", String.valueOf(period));
            //                double roi = setRenewalRateOfInterset(paramMap);
            //                setRenewaltxtRateOfInterest(CommonUtil.convertObjToStr(calcDepSubNo.get("RATE_OF_INT")));
            //                if(!productBehavesLike.equals(prodBehavesLikeFixed)){
            detailsHash.put("AMOUNT", String.valueOf(maturityAmt));
            //                } else {
            //                    detailsHash.put("AMOUNT", CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_AMT")));
            //                }
            detailsHash.put("DEPOSIT_DT", DateUtil.getDateMMDDYYYY(depDt));
            detailsHash.put("MATURITY_DT", DateUtil.getDateMMDDYYYY(matDt));
            detailsHash.put("ROI", CommonUtil.convertObjToStr(calcDepSubNo.get("RATE_OF_INT")));
            detailsHash.put("PERIOD", String.valueOf(period));
            detailsHash.put("CATEGORY_ID", category);
            detailsHash.put("PROD_ID", prodId);
            detailsHash.put("BEHAVES_LIKE", productBehavesLike);
            detailsHash.put("INTEREST_TYPE", "RENEW");
            detailsHash = setRenewalAmountsAccROI(detailsHash, null);
            //            }
            //system.out.println("balIntAmt "+balIntAmt);
            HashMap depositMap = new HashMap();
            ArrayList lienList = new ArrayList();
            depositMap.put("DEPOSIT_NO", calcDepSubNo.get("DEPOSIT_NO"));
            lst = ClientUtil.executeQuery("getPresentDepStatus", depositMap);
            if (lst != null && lst.size() > 0) {
                for (int k = 0; k < lst.size(); k++) {
                    depLienRow = new ArrayList();
                    depositMap = (HashMap) lst.get(k);
                    depLienRow.add(0, CommonUtil.convertObjToStr(depositMap.get("LIEN_NO")));
                    depLienRow.add(1, CommonUtil.convertObjToStr(depositMap.get("LIEN_AMOUNT")));
                    depLienRow.add(2, DateUtil.getStringDate((Date) depositMap.get("LIEN_DT")));
                    depLienRow.add(3, CommonUtil.convertObjToStr(depositMap.get("LIEN_AC_NO")));
                    depLienRow.add(4, CommonUtil.convertObjToStr(depositMap.get("STATUS")));
                    depLienRow.add(5, CommonUtil.convertObjToStr(depositMap.get("UNLIEN_DT")));
                    tblLienDetails.insertRow(tblLienDetails.getRowCount(), depLienRow);
                }
            }
            getAllOldDepositDetails(CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_NO")));
            HashMap tdsMap = new HashMap();
            tdsMap.put("ACCT_NUM", calcDepSubNo.get("DEPOSIT_NO") + "_1");
            lst = ClientUtil.executeQuery("getTdsDeductStatus", tdsMap);
            if (lst != null && lst.size() > 0) {
                tdsMap = (HashMap) lst.get(0);
                setTdsAmountValue(CommonUtil.convertObjToStr(tdsMap.get("SUM(TDS_AMT)")));
            }
            //--- If the record is Not DELETED , Show it in Table
            if (!(CommonUtil.convertObjToStr(depSubNoRec.get(FLD_DEP_SUB_NO_STATUS)).equals(CommonConstants.STATUS_DELETED))) {
                depSubNoRow.add(0, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_SUB_NO")));
                //                if(!productBehavesLike.equals(prodBehavesLikeFixed)){
                //                    depSubNoRow.add(1, CommonUtil.convertObjToStr(calcDepSubNo.get("TOTAL_BALANCE")));
                //                } else {
                depSubNoRow.add(1, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_AMT")));
                //                }
                depSubNoRow.add(2, strMatDt);
                depSubNoRow.add(3, calcDepSubNo.get("TOT_INT_AMT"));
                tblDepSubNoAccInfo.insertRow(tblDepSubNoAccInfo.getRowCount(), depSubNoRow);
            }
            renewalDepSubNoRow = new ArrayList();
            if (!(CommonUtil.convertObjToStr(renewaldepSubNoRec.get(FLD_DEP_SUB_NO_STATUS)).equals(CommonConstants.STATUS_DELETED))) {//renewal..
                renewalDepSubNoRow.add(0, CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_SUB_NO")));
                renewalDepSubNoRow.add(1, String.valueOf(maturityAmt));
                renewalDepSubNoRow.add(2, depDt);
                renewalDepSubNoRow.add(3, calcDepSubNo.get("TOT_INT_AMT"));
                tblRenewalDepSubNoAccInfo.insertRow(tblRenewalDepSubNoAccInfo.getRowCount(), renewalDepSubNoRow);
            }
            depSubNoCount = (int) Integer.parseInt(CommonUtil.convertObjToStr(depSubNoRec.get(FLD_DEP_SUB_NO_DEP_SUB_NO)));
            //            renewaldepSubNoCount = (int)Integer.parseInt(CommonUtil.convertObjToStr(renewaldepSubNoRec.get(FLD_DEP_SUB_NO_DEP_SUB_NO)));
            depSubNoRow = null;
            depSubNoRec = null;
            renewaldepSubNoRec = null;
            calcDepSubNo = null;
            renewalDepSubNoRow = null;
        }
        //system.out.println("@@@@@@ setDepSubNoDetails" + depSubNoDetailsList);
    }

    /**
     * To retrive Account Head details based on Product Id
     */
    public HashMap getAcctHeadForProdRenewal() {
        HashMap resultMap;
        HashMap accountHeadMap = new HashMap();
        accountHeadMap.put("PROD_ID", CommonUtil.convertObjToStr(cbmRenewalDepositProdId.getKeyForSelected()));
        setLblProductDescription(getCboRenewalDepositProdId());
        List resultList = ClientUtil.executeQuery("getAcctHead", accountHeadMap);

        //        //system.out.println("accountHeadMap:" + accountHeadMap);
        List listIntApplFreq = (List) ClientUtil.executeQuery("getDepProdIntPay", accountHeadMap);
        //        //system.out.println("listIntApplFreq:" + listIntApplFreq);
        HashMap hashIntApplFreq = (HashMap) listIntApplFreq.get(0);
        //        //system.out.println("hashIntApplFreq:" + hashIntApplFreq);

        resultMap = (HashMap) resultList.get(0);
        setLblValAccountHead(CommonUtil.convertObjToStr(resultMap.get("ACCT_HEAD")));
        List listProdDetails = ClientUtil.executeQuery("getDepProdDetails", accountHeadMap);
        resultMap = (HashMap) listProdDetails.get(0);

        List listProd = ClientUtil.executeQuery("getDepositsBackDatedDay", accountHeadMap);
        HashMap renewalresultMap = (HashMap) listProd.get(0);
        resultMap.put("MINIMUM_PERIOD_OF_RENEWAL", renewalresultMap.get("MINIMUM_PERIOD_OF_RENEWAL"));
        //        //system.out.println("B4 adding resultMap:" + resultMap);
        resultMap.put("INT_APPL_FREQ", hashIntApplFreq.get("INT_APPL_FREQ"));
        //        //system.out.println("A4 adding resultMap:" + resultMap);

        hashIntApplFreq = null;
        accountHeadMap = null;
        return resultMap;
    }

    public HashMap getAcctHeadForProdExtension() {
        HashMap resultMap;
        HashMap accountHeadMap = new HashMap();
        accountHeadMap.put("PROD_ID", CommonUtil.convertObjToStr(cbmExtensionDepositProdId.getKeyForSelected()));
        setLblProductDescription(getCboProductId());
        List resultList = ClientUtil.executeQuery("getAcctHead", accountHeadMap);

        //        //system.out.println("accountHeadMap:" + accountHeadMap);
        List listIntApplFreq = (List) ClientUtil.executeQuery("getDepProdIntPay", accountHeadMap);
        //        //system.out.println("listIntApplFreq:" + listIntApplFreq);
        HashMap hashIntApplFreq = (HashMap) listIntApplFreq.get(0);
        //        //system.out.println("hashIntApplFreq:" + hashIntApplFreq);

        resultMap = (HashMap) resultList.get(0);
        setLblValAccountHead(CommonUtil.convertObjToStr(resultMap.get("ACCT_HEAD")));
        List listProdDetails = ClientUtil.executeQuery("getDepProdDetails", accountHeadMap);
        resultMap = (HashMap) listProdDetails.get(0);

        //        //system.out.println("B4 adding resultMap:" + resultMap);
        resultMap.put("INT_APPL_FREQ", hashIntApplFreq.get("INT_APPL_FREQ"));
        //        //system.out.println("A4 adding resultMap:" + resultMap);

        hashIntApplFreq = null;
        accountHeadMap = null;
        return resultMap;
    }

    private void setJointAcctDetails(List jntAccntDetailsList) {
        jntAcctAll = new LinkedHashMap();
        HashMap custMapData;
        HashMap calcJointAcct;
        List custListData;
        int jntAccntDetailsListSize = jntAccntDetailsList.size();
        for (int i = 0; i < jntAccntDetailsListSize; i++) {
            calcJointAcct = (HashMap) jntAccntDetailsList.get(i);
            jntAcctSingleRec = new HashMap();
            jntAcctSingleRec.put("CUST_ID", CommonUtil.convertObjToStr(calcJointAcct.get("CUST_ID")));
            jntAcctSingleRec.put(FLD_FOR_DB_YES_NO, YES_FULL_STR);
            jntAcctSingleRec.put("STATUS", CommonUtil.convertObjToStr(calcJointAcct.get("STATUS")));
            //            jntAcctSingleRec.put("BRANCH_CODE",ProxyParameters.BRANCH_ID);
            jntAcctAll.put(CommonUtil.convertObjToStr(calcJointAcct.get("CUST_ID")), jntAcctSingleRec);
            //            //system.out.println("#######setJointAcctDetails : " +jntAcctSingleRec);
            if (!CommonUtil.convertObjToStr(calcJointAcct.get("STATUS")).equals("DELETED")) {
                custListData = ClientUtil.executeQuery("getSelectAccInfoTblDisplay", jntAcctSingleRec);
                custMapData = (HashMap) custListData.get(0);
                objJointAcctHolderManipulation.setJntAcctTableData(custMapData, true, tblJointAccnt);
                setTblJointAccnt(objJointAcctHolderManipulation.getTblJointAccnt());
            }
            custMapData = null;
            jntAcctSingleRec = null;
            calcJointAcct = null;
            custListData = null;
        }
    }

    private void setAcctInfoDetails(HashMap accountInfoMap) {
        //system.out.println("^^^^^^^accountInfoMap :"+accountInfoMap);
        // If "Normal" is in database, it will select "Normal" rdo
        // else if "TransferIn" is in database, it will select "Transfer In" rdo
        System.out.println("asdmhsa"+accountInfoMap);
        getCbmProductId().setKeyForSelected(CommonUtil.convertObjToStr(accountInfoMap.get("PROD_ID")));
        setCboProductId(CommonUtil.convertObjToStr(getCbmProductId().getDataForKey(CommonUtil.convertObjToStr(accountInfoMap.get("PROD_ID")))));
        setRdowithIntRenewal_Yes(false);
        setRdowithIntRenewal_No(false);
        setRdoAutoRenewal_Yes(false);
        setRdoAutoRenewal_No(false);
        setRdoStandingInstruction_Yes(false);
        setRdoStandingInstruction_No(false);
        setRdoDeathClaim_No(false);
        setRdoDeathClaim_Yes(false);
        setRdoMatAlertReport_Yes(false);
        setRdoMatAlertReport_No(false);
        if (getActionType() == ClientConstants.ACTIONTYPE_EXTENSION) {
            setRdoOpeningMode_Extension(true);
        } else if (getActionType() == ClientConstants.ACTIONTYPE_RENEW) {
            setRdoOpeningMode_Renewal(true);
        } else if (getActionType() == ClientConstants.ACTIONTYPE_NEWTI) {
            setRdoOpeningMode_TransferIn(true);
        } else if (getActionType() == ClientConstants.ACTIONTYPE_EDIT
                || getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                || getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
            HashMap extensionMap = new HashMap();
            boolean setting = false;
            extensionMap.put("DEPOSIT_NO", accountInfoMap.get("DEPOSIT_NO"));
            List lstExtension = ClientUtil.executeQuery("getSelectExtensionDetails", extensionMap);
            if (lstExtension != null && lstExtension.size() > 0) {
                if (getViewTypeDeposit().equals("AUTHORIZE") || getViewTypeDeposit().equals("EDIT")) {
                    extensionMap = (HashMap) lstExtension.get(0);
                    setRdoOpeningMode_Extension(true);
                    setting = true;
                }
            }
            extensionMap = null;
            lstExtension = null;
            HashMap renewalMap = new HashMap();
            renewalMap.put("OLD_DEPOSIT_NO", accountInfoMap.get("DEPOSIT_NO"));
            List lstRenewal = ClientUtil.executeQuery("getSelectOldDepositRenewalDetails", renewalMap);
            if (lstRenewal != null && lstRenewal.size() > 0) {
                if (getViewTypeDeposit().equals("AUTHORIZE") || getViewTypeDeposit().equals("EDIT")) {
                    renewalMap = (HashMap) lstRenewal.get(0);
                    setRdoOpeningMode_Renewal(true);
                    setting = true;
                }
            }
            renewalMap = null;
            lstRenewal = null;
            if (setting == false) {
                if (CommonUtil.convertObjToStr(accountInfoMap.get("OPENING_MODE")).equals(NORMAL)) {
                    setRdoOpeningMode_Normal(true);
                } else if (!accountInfoMap.get("TRANS_OUT").equals("") && accountInfoMap.get("TRANS_OUT").equals("Y")) {
                    setRdoOpeningMode_TransferIn(true);
                } else if (CommonUtil.convertObjToStr(accountInfoMap.get("OPENING_MODE")).equalsIgnoreCase(TRANSFER_IN)) {
                    setRdoOpeningMode_TransferIn(true);
                } else if (CommonUtil.convertObjToStr(accountInfoMap.get("OPENING_MODE")).equalsIgnoreCase(RENEWAL)) {
                    setRdoOpeningMode_Renewal(true);
                } else if (CommonUtil.convertObjToStr(accountInfoMap.get("OPENING_MODE")).equals(TRANSFER_IN)) {
                    setRdoOpeningMode_TransferIn(true);
                } else if (CommonUtil.convertObjToStr(accountInfoMap.get("OPENING_MODE")).equals(EXTENSION)) {
                    setRdoOpeningMode_Extension(true);
                }
            }
            setting = false;
        }
        //            if (getActionType()==ClientConstants.ACTIONTYPE_RENEW)
        //                setRdoOpeningMode_Renewal(true);
        //            else if (getActionType()==ClientConstants.ACTIONTYPE_EXTENSION)
        //                setRdoOpeningMode_Extension(true);
        //            else if(!accountInfoMap.get("TRANS_OUT").equals("") && accountInfoMap.get("TRANS_OUT").equals("N"))
        //                setRdoOpeningMode_Normal(true);
        //            else if(!accountInfoMap.get("TRANS_OUT").equals("") && accountInfoMap.get("TRANS_OUT").equals("Y"))
        //                setRdoOpeningMode_TransferIn(true);
        //        else if (CommonUtil.convertObjToStr(accountInfoMap.get("OPENING_MODE")).equals(TRANSFER_IN)) {
        //            setRdoOpeningMode_TransferIn(true);
        //        }else if(CommonUtil.convertObjToStr(accountInfoMap.get("OPENING_MODE")).equals(RENEWAL)) {
        //            setRdoOpeningMode_Renewal(true);
        //        }else if(CommonUtil.convertObjToStr(accountInfoMap.get("OPENING_MODE")).equals(EXTENSION)) {
        //            setRdoOpeningMode_Extension(true);
        //        }
        setTxtDepsoitNo(CommonUtil.convertObjToStr(accountInfoMap.get("DEPOSIT_NO")));
        setTxtCustomerId(CommonUtil.convertObjToStr(accountInfoMap.get("CUST_ID")));
        setCboConstitution(CommonUtil.convertObjToStr(getCbmConstitution().getDataForKey(CommonUtil.convertObjToStr(accountInfoMap.get("CONSTITUTION")))));
        setTxtMdsGroup(CommonUtil.convertObjToStr(accountInfoMap.get("MDS_GROUP")));
        setTxaMdsRemarks(CommonUtil.convertObjToStr(accountInfoMap.get("MDS_REMARKS")));
        setMdsGroup(CommonUtil.convertObjToStr(accountInfoMap.get("MDS_GROUP"))); //Added by nithya on 08-08-2016
        setMdsRemarks(CommonUtil.convertObjToStr(accountInfoMap.get("MDS_REMARKS"))); //Added by nithya on 08-08-2016
        setCboAddressType(CommonUtil.convertObjToStr(getCbmAddressType().getDataForKey(CommonUtil.convertObjToStr(accountInfoMap.get("ADDR_TYPE")))));
        setCboCategory(CommonUtil.convertObjToStr(getCbmCategory().getDataForKey(CommonUtil.convertObjToStr(accountInfoMap.get("CATEGORY")))));
        setCboMember(CommonUtil.convertObjToStr(getCbmMemberType().getDataForKey(CommonUtil.convertObjToStr(accountInfoMap.get("CUST_TYPE")))));
        setTxtPanNumber(CommonUtil.convertObjToStr(accountInfoMap.get("PAN_NUMBER")));
        setTxtRemarks(CommonUtil.convertObjToStr(accountInfoMap.get("REMARKS")));
        setCboSettlementMode(CommonUtil.convertObjToStr(getCbmSettlementMode().getDataForKey(CommonUtil.convertObjToStr(accountInfoMap.get("SETTLEMENT_MODE")))));
        setLblValCommunicationAddress(CommonUtil.convertObjToStr(getCbmCommunicationAddress().getDataForKey(CommonUtil.convertObjToStr(accountInfoMap.get("COMM_ADDRESS")))));
        setStatusBy(CommonUtil.convertObjToStr(accountInfoMap.get("STATUS_BY")));
        setPrinting_No(CommonUtil.convertObjToStr(accountInfoMap.get("PRINTING_NO")));
        setReferenceNo(CommonUtil.convertObjToStr(accountInfoMap.get("REFERENCE_NO")));
        //If "POA" is "Y" in database then check the POA Checkbox
        if (CommonUtil.convertObjToStr(accountInfoMap.get("POA")).equals(YES_STR)) {
            setChkPowerOfAttorney(true);
        } else {
            setChkPowerOfAttorney(false);
        }
        //If "AUTHORIZED_SIGNATORY" is "Y" in database then check the Authorized Signatory Checkbox
        if (CommonUtil.convertObjToStr(accountInfoMap.get("AUTHORIZED_SIGNATORY")).equals(YES_STR)) {
            setChkAuthorizedSignatory(true);
        } else {
            setChkAuthorizedSignatory(false);
        }
        if (CommonUtil.convertObjToStr(accountInfoMap.get("ACC_ZERO_BAL_YN")).equals(YES_STR)) {
            setChkAccZeroBalYN(true);
        } else {
            setChkAccZeroBalYN(false);
        }
        //If "TAX_DEDUCTIONS" is "Y" in database then check the Tax Deductions Checkbox
        //        if (CommonUtil.convertObjToStr(accountInfoMap.get("TAX_DEDUCTIONS")).equals(YES_STR)) {
        //            setChkTaxDeductions(true);
        //        } else {
        //            setChkTaxDeductions(false);
        //        }
        //If "MEMBER TYPE" is "Y" in database then check the MEMBER TYPE Checkbox
        if (CommonUtil.convertObjToStr(accountInfoMap.get("STANDING_INSTRUCT")).equals(YES_STR)) {
            setRdoStandingInstruction_Yes(true);
        } else {
            setRdoStandingInstruction_No(true);
        }
        if (CommonUtil.convertObjToStr(accountInfoMap.get("MEMBER")).equals(YES_STR)) {
            setChkMember(true);
        } else {
            setChkMember(false);
        }
        //If "FIFTEENH_DECLARE" is "Y" in database then check the 15H Declarations Checkbox
        if (CommonUtil.convertObjToStr(accountInfoMap.get("FIFTEENH_DECLARE")).equals(YES_STR)) {
            setChk15hDeclarations(true);
        } else {
            setChk15hDeclarations(false);
        }
        if (CommonUtil.convertObjToStr(accountInfoMap.get("TAX_DEDUCTIONS")).equals(YES_STR)) {
            setChkTaxDeductions(true);
        } else {
            setChk15hDeclarations(false);
        }
        //If "NOMINEE_DETAILS" is "Y" in database then check the Nominee details Checkbox
        //        if(cboConstitution.getSelectedItem().equals("Joint Account") || cboConstitution.getSelectedItem().equals("Induvidual"))
        if ((getCboConstitution().equals("Joint Account") || getCboConstitution().equals("Individual") || getCboConstitution().equals("Induvidual"))
                && CommonUtil.convertObjToStr(accountInfoMap.get("NOMINEE_DETAILS")).equals(YES_STR)) {
            setChkNomineeDetails(true);
        } else {
            setChkNomineeDetails(false);
        }
        if (CommonUtil.convertObjToStr(accountInfoMap.get("AUTO_RENEWAL")).equals(YES_STR)) {
            setRdoAutoRenewal_Yes(true);
        } else {
            setRdoAutoRenewal_No(true);
        }
        if (CommonUtil.convertObjToStr(accountInfoMap.get("RENEW_WITH_INT")).equals(YES_STR)) {
            setRdowithIntRenewal_Yes(true);
        } else {
            setRdowithIntRenewal_No(true);
        }
        if (CommonUtil.convertObjToStr(accountInfoMap.get("DEATH_CLAIM")).equals(YES_STR)) {
            setRdoDeathClaim_Yes(true);
        } else {
            setRdoDeathClaim_No(true);
        }
        if (CommonUtil.convertObjToStr(accountInfoMap.get("MAT_ALERT_REPORT")).equals(YES_STR)) {
            setRdoMatAlertReport_Yes(true);
        } else {
            setRdoMatAlertReport_No(true);
        }
        //        if(accountInfoMap.get("DEPOSIT_STATUS").equals("CLOSED")){
        //            if(accountInfoMap.get("OPENING_MODE").equals("Normal")){
        //                setClosingTypeValue(CommonUtil.convertObjToStr("Normal"));
        //                closeFlag = true;
        //            }else
        //                setClosingTypeValue(CommonUtil.convertObjToStr("Renewal"));
        //        }
        //        transferBranchCode = CommonUtil.convertObjToStr(accountInfoMap.get("BRANCH_ID"));
        setTxtAgentId(CommonUtil.convertObjToStr(accountInfoMap.get("AGENT_ID")));
        //        getCbmProductId().setKeyForSelected(CommonUtil.convertObjToStr(accountInfoMap.get("PROD_ID")));
        //        setCboProductId(CommonUtil.convertObjToStr(getCbmProductId().getDataForKey(CommonUtil.convertObjToStr(accountInfoMap.get("PROD_ID")))));
        setTxtCustomerId(CommonUtil.convertObjToStr(accountInfoMap.get("CUST_ID")));
        setLblValRenewDep(CommonUtil.convertObjToStr(accountInfoMap.get("RENEWAL_FROM_DEPOSIT")));
        //Sets the Renewal count to the class variable renewalCount for using it , while renewing it
        renewalCount = CommonUtil.convertObjToDouble(accountInfoMap.get("RENEWAL_COUNT"));
//        setCboIntroducer(CommonUtil.convertObjToStr(accountInfoMap.get("INTRODUCER")));
        setTxtDealer(CommonUtil.convertObjToStr(accountInfoMap.get("INTRODUCER")));//Added By Revathi.L
        System.out.println("%#$%#$%#$%#%$#% innnn"+CommonUtil.convertObjToStr(accountInfoMap.get("INTRODUCER")));
        System.out.println("%#$%#$%#$%#%$#% setCboIntroducer"+getCboIntroducer());
        //        setCboProdId(CommonUtil.convertObjToStr(getCbmProdId().getDataForKey()));
    }

    public void populateRenewalDepSubNoFields(int row) {
        try {
            HashMap listDepSubNoAll = (HashMap) renewaldepSubNoAll.get(String.valueOf(row));
            depSubNoMode = 1; //Set the mode for "Modification" of record
            depSubNoRowDel = row + 1;
            //            if(getViewTypeDeposit().equals("AUTHORIZE") || getViewTypeDeposit().equals("EDIT")){
            //                setRenewaltdtDateOfDeposit(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_DEP_DT)));
            //                setRenewaltdtMaturityDate(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_MAT_DT)));
            //            }else{
            if (getRdoRenewalAdding_Yes() == true) {
                setRenewaltdtDateOfDeposit(CommonUtil.convertObjToStr(currDt.clone()));
            } else {
                long diff = DateUtil.dateDiff((Date) DateUtil.getDateMMDDYYYY(getRenewalDepDate()), (Date) currDt.clone());
                if (diff >= backDateFreq) {
                    setRenewaltdtDateOfDeposit(CommonUtil.convertObjToStr(currDt.clone()));
                } else {
                    setRenewaltdtDateOfDeposit(CommonUtil.convertObjToStr(getRenewalDepDate()));
                }
            }

            HashMap calcMap = new HashMap();
            calcMap.put("DEPOSIT_DT", DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getRenewaltdtDateOfDeposit())));
            calcMap.put("DEPOSIT_PERIOD_YY", listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_DEP_PER_YY));
            calcMap.put("DEPOSIT_PERIOD_MM", listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_DEP_PER_MM));
            calcMap.put("DEPOSIT_PERIOD_DD", listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_DEP_PER_DD));
            calcMap = calculateRenewalMaturityDate(calcMap);
            setRenewaltdtMaturityDate(CommonUtil.convertObjToStr(calcMap.get("MATURITY_DT")));
            //            }
            setRenewaltxtPeriodOfDeposit_Days(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_DEP_PER_DD)));
            setRenewaltxtPeriodOfDeposit_Months(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_DEP_PER_MM)));
            setRenewaltxtPeriodOfDeposit_Years(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_DEP_PER_YY)));
            setRenewaltxtDepositAmount(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_DEP_AMT)));
            setRenewalValDepositSubNo(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_DEP_NO)));
            setRenewaltxtRateOfInterest(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_RATE_OF_INT)));
            setRenewaltxtMaturityAmount(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_MAT_AMT)));
            setRenewaltxtTotalInterestAmount(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_TOT_INT_AMT)));
            setRenewaltxtPeriodicInterestAmount(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_PER_INT_AMT)));
            setLblValDepositSubNo(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_DEP_SUB_NO)));
            setCboRenewalDepositProdId(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_PRODUCT_ID)));
            setCboRenewalDepositCategory(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_CATEGORY)));
            setCboRenewalInterestPaymentFrequency(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_INT_PAY_FREQ)));
            setCboRenewalInterestPaymentMode(CommonUtil.convertObjToStr(getCbmInterestPaymentMode().getDataForKey(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_PAY_MODE)))));
            if (!CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_PAY_MODE)).equals("") && CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_PAY_MODE)).equals("TRANSFER")) {// &&
                //            (getViewTypeDeposit().equals("AUTHORIZE")||getViewTypeDeposit().equals("EDIT"))){
                setCboRenewalProdType(CommonUtil.convertObjToStr(getCbmProdType().getDataForKey(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_PROD_TYPE)))));
                if (!CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_PROD_TYPE)).equals("GL")) {
                    setCbmRenewalProdId(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_PROD_TYPE)));
                    setCboRenewalProdId((String) getCbmRenewalProdId().getDataForKey(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_PROD_ID))));
                    setRenewalcustomerIdCr(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_AC_NO)));
                    HashMap accountNameMap = new HashMap();
                    //                if(listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_AC_NO)!=null){
                    accountNameMap.put("ACC_NUM", CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_AC_NO)));
                    if (!listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_PROD_TYPE).equals("") && !listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_PROD_TYPE).equals("RM")) {
                        final List resultList = ClientUtil.executeQuery("getAccountNumberName" + listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_PROD_TYPE), accountNameMap);
                        if (resultList != null && resultList.size() > 0) {
                            final HashMap resultMap = (HashMap) resultList.get(0);
                            setRenewalcustomerNameCrValue(resultMap.get("CUSTOMER_NAME").toString());
                        }
                    }
                } else {
                    setRenewalcustomerIdCr(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_AC_NO)));
                    setRenewalcustomerNameCrValue(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_AC_NO)));
                }
                //                setRenewalcustomerNameCrValue(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_CUST_NAME)));
                String prodType = CommonUtil.convertObjToStr(getCboRenewalProdType());
                //                //                if(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_PROD_TYPE)).equals("RM"))
                //                    setCboRenewalProdId("PAY ORDR");
                //                this.setProductTypes(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_PROD_TYPE)));
            }
            if (getRdoCalenderFreq_Yes() == true) {
                setCboRenewalCalenderFreqDay(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_CALENDER_DAY)));
            }
            if (listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_INT_PAY_FREQ) != null && listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_INT_PAY_FREQ).equals("0")) {
                setCboRenewalInterestPaymentFrequency("Date of Maturity");
            } else if (listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_INT_PAY_FREQ) != null && listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_INT_PAY_FREQ).equals("30")) {
                setCboRenewalInterestPaymentFrequency("Monthly");
            } else if (listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_INT_PAY_FREQ) != null && listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_INT_PAY_FREQ).equals("90")) {
                setCboRenewalInterestPaymentFrequency("Quarterly");
            } else if (listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_INT_PAY_FREQ) != null && listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_INT_PAY_FREQ).equals("180")) {
                setCboRenewalInterestPaymentFrequency("Half Yearly");
            } else if (listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_INT_PAY_FREQ) != null && listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_INT_PAY_FREQ).equals("360")) {
                setCboRenewalInterestPaymentFrequency("Yearly");
            } else if (listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_INT_PAY_FREQ) != null && listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_INT_PAY_FREQ).equals("60")) {
                setCboRenewalInterestPaymentFrequency("2 Months");
            } else if (listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_INT_PAY_FREQ) != null && listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_INT_PAY_FREQ).equals("120")) {
                setCboRenewalInterestPaymentFrequency("4 Months");
            } else if (listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_INT_PAY_FREQ) != null && listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_INT_PAY_FREQ).equals("150")) {
                setCboRenewalInterestPaymentFrequency("5 Months");
            } else if (listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_INT_PAY_FREQ) != null && listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_INT_PAY_FREQ).equals("210")) {
                setCboRenewalInterestPaymentFrequency("7 Months");
            } else if (listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_INT_PAY_FREQ) != null && listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_INT_PAY_FREQ).equals("240")) {
                setCboRenewalInterestPaymentFrequency("8 Months");
            } else if (listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_INT_PAY_FREQ) != null && listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_INT_PAY_FREQ).equals("270")) {
                setCboRenewalInterestPaymentFrequency("9 Months");
            } else if (listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_INT_PAY_FREQ) != null && listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_INT_PAY_FREQ).equals("300")) {
                setCboRenewalInterestPaymentFrequency("10 Months");
            } else if (listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_INT_PAY_FREQ) != null && listDepSubNoAll.get(FLD_DEP_RENEWAL_SUB_NO_INT_PAY_FREQ).equals("330")) {
                setCboRenewalInterestPaymentFrequency("11 Months");
            }
            //system.out.println("%%%%%% listDepSubNoAll : "+listDepSubNoAll);
            notifyObservers();
            listDepSubNoAll = null;
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public double setRenewalRateOfInterset(HashMap paramMap) {
        double retInt = -1;
        long period = 0;
        HashMap whereMap = new HashMap();
        whereMap.put("PRODUCT_TYPE", SCREEN);
        String sourceProdId = getCbmProductId().getKeyForSelected() + "";
        String prodId = getCbmRenewalDepositProdId().getKeyForSelected() + "";
        //system.out.println("@#@# sourceProdId :"+sourceProdId+" / new prodId:"+prodId);
//        //system.out.println("@#@# interestRateMap :"+interestRateMap);
//        if (interestRateMap!=null && interestRateMap.containsKey(prodId)) {
//            retInt = CommonUtil.convertObjToDouble(interestRateMap.get(prodId)).doubleValue();
//        } else {
//            if (interestRateMap==null) {
//                interestRateMap = new HashMap();
//            }
        if (paramMap == null) {
            HashMap renewMap = new HashMap();
            renewMap.put("PROD_ID", sourceProdId);
            List lst = ClientUtil.executeQuery("getDepositsBackDatedDay", renewMap);
            if (lst != null && lst.size() > 0) {
                renewMap = (HashMap) lst.get(0);
            }
            Date startDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(renewaltdtDateOfDeposit));
            Date endDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(renewaltdtMaturityDate));
            whereMap.put("CATEGORY_ID", getCbmRenewalDepositCategory().getKeyForSelected());
            whereMap.put("PROD_ID", prodId);
            whereMap.put("AMOUNT", CommonUtil.convertObjToDouble(getRenewaltxtDepositAmount()));
            whereMap.put("DEPOSIT_DT", startDt);
            if (startDt != null && endDt != null) {
                //added by rishad at 14/02/2015  for mantis 0010356
                int year = CommonUtil.convertObjToInt(getTxtPeriodOfDeposit_Years());
                int month = CommonUtil.convertObjToInt(getTxtPeriodOfDeposit_Months());
                if (year != 0 || month != 0) {
                    HashMap whereDateMap = new HashMap();
                    whereDateMap.put("START_DT", startDt);
                    whereDateMap.put("END_DT", endDt);
                    List monthList = (List) ClientUtil.executeQuery("getMonthInBetweenDayes", whereDateMap);
                    if (monthList != null && monthList.size() > 0) {
                        HashMap resultMap = (HashMap) monthList.get(0);
                        if (resultMap.containsKey("COUNT")) {
                            period = CommonUtil.convertObjToLong(resultMap.get("COUNT"));
                        }
                    }
                }
                period += DateUtil.dateDiff(startDt, endDt);
                //system.out.println("#### period : "+period);
                //                int count=0;
                //                while (DateUtil.dateDiff(startDt, endDt)>0) {
                //                    int month = startDt.getMonth();
                //                    int startYear = startDt.getYear()+1900;
                //                    if (month==1 && startYear%4==0)
                //                        count++;
                //                    startDt = DateUtil.addDays(startDt, 30);
                //                }
                //                period -= count;
                whereMap.put("PERIOD", period);
                if (renewMap != null && renewMap.get("DATE_OF_MATURITY") != null && renewMap.get("DATE_OF_MATURITY").equals("Y")) {
                    whereMap.put("DEPOSIT_DT", startDt);
                } else if (renewMap != null && renewMap.get("DATE_OF_RENEWAL") != null && renewMap.get("DATE_OF_RENEWAL").equals("Y")) {
                    whereMap.put("DEPOSIT_DT", currDt);
                }
            }
        } else {
            whereMap.put("CATEGORY_ID", getCbmRenewalDepositProdId().getKeyForSelected());
            whereMap.put("PROD_ID", getCbmRenewalDepositCategory().getKeyForSelected());
            whereMap.putAll(paramMap);
        }
        String PRODUCT_TYPE = "PRODUCT_TYPE";
        String PRODUCT_TYPE_VALUE = "TD";
        whereMap.put(PRODUCT_TYPE, "TD");
        List dataList = (List) ClientUtil.executeQuery("icm.getInterestRates", whereMap);
        HashMap roiHash = new HashMap();
        if (dataList != null && dataList.size() > 0) {
            roiHash = (HashMap) dataList.get(0);
            retInt = CommonUtil.convertObjToDouble(roiHash.get("ROI")).doubleValue();
            //system.out.println("rate of interest "+retInt);
        } else {
            retInt = 0;
            msgIntWarn();
        }
//            interestRateMap.put(prodId, new Double(retInt));
        //system.out.println("roiHash:" + roiHash);
//        }
        //system.out.println("paramMap:" + paramMap);
        //system.out.println("retInt:" + retInt);
        return retInt;
    }

    private void setTransInDetails(HashMap TransInMap) {
        setTdtOriginalDateOfDeposit(DateUtil.getStringDate((Date) TransInMap.get("ORIGINAL_DEPOSIT_DT")));
        setTxtPrintedNoOfTheFdr(CommonUtil.convertObjToStr(TransInMap.get("PRINTED_FDR")));
        setTxtInterBranchTransferNo(CommonUtil.convertObjToStr(TransInMap.get("INTERBRANCH_TRANS_NO")));
        setTxtTransferingAmount(CommonUtil.convertObjToStr(TransInMap.get("TRANS_AMT")));
        setTdtDateOfTransfer(DateUtil.getStringDate((Date) TransInMap.get("TRANS_DT")));
        setTdtLastInterestCalculatedDate(DateUtil.getStringDate((Date) TransInMap.get("LAST_INTCALC_DT")));
        setTxtInterestProvidedAmount(CommonUtil.convertObjToStr(TransInMap.get("INT_PROV_AMT")));
        setTxtInterestPaid(CommonUtil.convertObjToStr(TransInMap.get("INT_PAID")));
        setTxtTotalNumberOfInstallments(CommonUtil.convertObjToStr(TransInMap.get("TOT_NO_INSTALL")));
        setTdtLastInstallmentReceivedDate(DateUtil.getStringDate((Date) TransInMap.get("LAST_INSTALL_RECDT")));
        setTdtTdsCollectedUpto(DateUtil.getStringDate((Date) TransInMap.get("TDS_COLLECTED_UPTO")));
        setTxtLastTdsCollected(CommonUtil.convertObjToStr(TransInMap.get("LAST_TDS_COLLECTED")));
        setTxtTotalInstallmentReceived(CommonUtil.convertObjToStr(TransInMap.get("TOT_INSTALL_RECEIVED")));
        setLblValBalanceInterestPayable(CommonUtil.convertObjToStr(TransInMap.get("BALANCE_INT_PAYABLE")));
        setTxtTransferingBranchCode(CommonUtil.convertObjToStr(TransInMap.get("TRANS_BRANCH_CODE")));
        setTxtOriginalAccountNumber(CommonUtil.convertObjToStr(TransInMap.get("ORIGINAL_AC_NUMBER")));
        notifyObservers();
    }

    /**
     * To retrive Account Head details based on Product Id
     */
    public HashMap getAcctHeadForProd() {
        HashMap resultMap;
        HashMap accountHeadMap = new HashMap();
        accountHeadMap.put("PROD_ID", cbmProductId.getKeyForSelected());
        setLblProductDescription(getCboProductId());
        List resultList = ClientUtil.executeQuery("getAcctHead", accountHeadMap);

        //        //system.out.println("accountHeadMap:" + accountHeadMap);
        List listIntApplFreq = (List) ClientUtil.executeQuery("getDepProdIntPay", accountHeadMap);
        //        //system.out.println("listIntApplFreq:" + listIntApplFreq);
        HashMap hashIntApplFreq = (HashMap) listIntApplFreq.get(0);
        //        //system.out.println("hashIntApplFreq:" + hashIntApplFreq);

        resultMap = (HashMap) resultList.get(0);
        setLblValAccountHead(CommonUtil.convertObjToStr(resultMap.get("ACCT_HEAD")));
        List listProdDetails = ClientUtil.executeQuery("getDepProdDetails", accountHeadMap);
        resultMap = (HashMap) listProdDetails.get(0);

        //        //system.out.println("B4 adding resultMap:" + resultMap);
        resultMap.put("INT_APPL_FREQ", hashIntApplFreq.get("INT_APPL_FREQ"));
        //        //system.out.println("A4 adding resultMap:" + resultMap);

        hashIntApplFreq = null;
        accountHeadMap = null;
        return resultMap;
    }

    public void setCustomerAccountName(String AccountNo) {
        try {
            if (!getProductTypes().equals("RM")) {
                final HashMap accountNameMap = new HashMap();

                if (getProductTypes().equals("MDS")) {
                    AccountNo = AccountNo.lastIndexOf("_") != -1 ? AccountNo.substring(0, AccountNo.length() - 2) : AccountNo;
                }
                accountNameMap.put("ACC_NUM", AccountNo);
                final List resultList = ClientUtil.executeQuery("getAccountNumberName" + this.getProductTypes(), accountNameMap);
                if (resultList != null && resultList.size() > 0) {
                    final HashMap resultMap = (HashMap) resultList.get(0);
                    setCustomerNameCrValue(resultMap.get("CUSTOMER_NAME").toString());
                }
            }
        } catch (Exception e) {
        }
    }

    public double setExtensionRateOfInterset(Date depDt, double depAmount, boolean extension) {
        double retInt = -1;
        long period = 0;
        HashMap whereMap = new HashMap();
        whereMap.put("PRODUCT_TYPE", SCREEN);
        //        Date startDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtDateOfDeposit));
        //        Date endDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(currDt));
        whereMap.put("AMOUNT", new Double(depAmount));
        whereMap.put("DEPOSIT_DT", depDt);
        if (depDt != null && currDt != null) {
            //system.out.println("#### period : "+period);
            int count = 0;
            if (extension == true) {
                //                whereMap.put("CATEGORY_ID", cboExtensionDepositCategory.toUpperCase());
                //                whereMap.put("PROD_ID", cboExtensionDepositProdId);
                whereMap.put("CATEGORY_ID", getCbmExtensionDepositCategory().getKeyForSelected());
                whereMap.put("PROD_ID", getCbmExtensionDepositProdId().getKeyForSelected());
                Date todayDt = currDt;
                period = DateUtil.dateDiff(todayDt, depDt);
                while (DateUtil.dateDiff(todayDt, depDt) > 0) {
                    int month = depDt.getMonth();
                    int startYear = depDt.getYear() + 1900;
                    if (month == 1 && startYear % 4 == 0) {
                        count++;
                    }
                    todayDt = DateUtil.addDays(todayDt, 30);
                }
            } else {
                whereMap.put("CATEGORY_ID", category);
                whereMap.put("PROD_ID", prodId);
                period = DateUtil.dateDiff(depDt, currDt);
                while (DateUtil.dateDiff(depDt, currDt) > 0) {
                    int month = depDt.getMonth();
                    int startYear = depDt.getYear() + 1900;
                    if (month == 1 && startYear % 4 == 0) {
                        count++;
                    }
                    depDt = DateUtil.addDays(depDt, 30);
                }
            }
//            period -= count;
            whereMap.put("PERIOD", period);
        }
        String PRODUCT_TYPE = "PRODUCT_TYPE";
        String PRODUCT_TYPE_VALUE = "TD";
        whereMap.put(PRODUCT_TYPE, "TD");
        List dataList = (List) ClientUtil.executeQuery("icm.getInterestRates", whereMap);
        HashMap roiHash = new HashMap();
        if (dataList != null && dataList.size() > 0) {
            roiHash = (HashMap) dataList.get(0);
            retInt = CommonUtil.convertObjToDouble(roiHash.get("ROI")).doubleValue();
            //system.out.println("rate of interest "+retInt);
        } else {
            retInt = 0;
            msgIntWarn();
        }
        //        //system.out.println("paramMap:" + paramMap);
        //        //system.out.println("roiHash:" + roiHash);
        return retInt;
    }

    private int checkDepSubNoForDuplicationRenewal(int depSubNoMode) {
        if (renewaldepSubNoAll == null) {
            renewaldepSubNoAll = new HashMap();
        }
        int j = CANCEL;
        COptionPane depSubNoDuplicationCheck;
        HashMap depCalc;
        depSubNoCheckValueRenewalInsert();
        //        renewaldepSubNoAll.put(FLD_DEP_RENEWAL_SUB_PRODUCT_ID, CommonUtil.convertObjToStr(cbmRenewalDepositProdId.getKeyForSelected()));
        //        renewaldepSubNoAll.put(FLD_DEP_RENEWAL_SUB_CATEGORY ,CommonUtil.convertObjToStr(cbmRenewalDepositCategory.getKeyForSelected()));
        int depSubNoAllSize = renewaldepSubNoAll.size();
        for (int i = 0; i < depSubNoAllSize; i++) {
            depSubNoCheckRenewalValues.put(FLD_DEP_SUB_NO_DEP_SUB_NO, String.valueOf(i + 1));
            //--- For checking the duplication add the Status and DbYesOrNo field
            depCalc = (HashMap) renewaldepSubNoAll.get(String.valueOf(i));
            break;
            //            depSubNoCheckRenewalValues.put(FLD_DEP_SUB_NO_STATUS , CommonUtil.convertObjToStr(depCalc.get(FLD_DEP_SUB_NO_STATUS)));
            //            depSubNoCheckRenewalValues.put(FLD_FOR_DB_YES_NO , CommonUtil.convertObjToStr(depCalc.get(FLD_FOR_DB_YES_NO)));
            //            if(((i != depSubNoRowDel) && (depSubNoMode==1)) || (depSubNoMode==0)){
            //                if((renewaldepSubNoAll.get(String.valueOf(i)).equals(depSubNoCheckRenewalValues))){
            //                    depSubNoDuplicationCheck = new COptionPane();
            //                    String[] options = {objTermDepositRB.getString(DIALOG_OK)};
            //                    j=depSubNoDuplicationCheck.showOptionDialog(null,objTermDepositRB.getString(WARN_MESSAGE_OK),CommonConstants.WARNINGTITLE,COptionPane.OK_OPTION,COptionPane.WARNING_MESSAGE, null, options, options[0]);
            //                    if(j==YES){
            //                        depSubNoRowCount = i;
            //                    }
            //                    break;
            //                } else {
            //                    j=CANCEL;
            //                }
            //            }
        }
        depSubNoDuplicationCheck = null;
        depSubNoCheckRenewalValues = null;
        depCalc = null;
        return j;
    }

    public void populateDepSubNoFields(int row) {
        try {
            HashMap listDepSubNoAll = (HashMap) depSubNoAll.get(String.valueOf(row));
            depSubNoMode = 1; //Set the mode for "Modification" of record
            depSubNoRowDel = row;
            setTdtPaymentDate(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_SUB_PAY_DATE)));
            setCboPaymentType(CommonUtil.convertObjToStr(getCbmPaymentType().getDataForKey(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_SUB_PAY_TYPE)))));
            setCboInstallmentAmount(CommonUtil.convertObjToStr(getCbmInstallmentAmount().getDataForKey(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_SUB_INST_TYPE)))));
            setLblValDepositSubNo(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_SUB_NO_DEP_SUB_NO)));
            setTdtMaturityDate(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_SUB_NO_MAT_DT)));
            setTxtMaturityAmount(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_SUB_NO_MAT_AMT)));
            setTxtRateOfInterest(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_SUB_NO_RATE_OF_INT)));
            setTxtTotalInterestAmount(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_SUB_NO_TOT_INT_AMT)));
            setTxtPeriodicInterestAmount(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_SUB_NO_PER_INT_AMT)));
            setTdtDateOfDeposit(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_SUB_NO_DEP_DT)));
            setTxtDepositAmount(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_SUB_NO_DEP_AMT)));
            setTxtPeriodOfDeposit_Days(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_SUB_NO_DEP_PER_DD)));
            setTxtPeriodOfDeposit_Months(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_SUB_NO_DEP_PER_MM)));
            setTxtPeriodOfDeposit_Years(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_SUB_NO_DEP_PER_YY)));
            setTxtPeriodOfDeposit_Weeks(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_SUB_NO_DEP_PER_Wk)));
            setCboInterestPaymentFrequency(CommonUtil.convertObjToStr(getCbmInterestPaymentFrequency().getDataForKey(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_SUB_NO_INT_PAY_FREQ)))));
            setCboInterestPaymentMode(CommonUtil.convertObjToStr(getCbmInterestPaymentMode().getDataForKey(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_SUB_NO_INT_PAY_MODE)))));
            if (!CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_SUB_INT_PROD_TYPE)).equals("")) {
                setCboProdType(CommonUtil.convertObjToStr(getCbmProdType().getDataForKey(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_SUB_INT_PROD_TYPE)))));
                setCbmProdId(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_SUB_INT_PROD_TYPE)));
                setCboProdId((String) getCbmProdId().getDataForKey(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_SUB_INT_PROD_ID))));
                setCustomerIdCr(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_SUB_INT_AC_NO)));
                String AccountNo = CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_SUB_INT_AC_NO));
                String prodType = CommonUtil.convertObjToStr(getCboProdType());
                //                if(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_SUB_INT_PROD_TYPE)).equals("RM"))
                //                    setCboProdId("PAY ORDR");

                this.setProductTypes(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_SUB_INT_PROD_TYPE)));
                setCustomerAccountName(AccountNo);
            }
            setCalenderFreqDate(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_SUB_CALENDER_DATE)));
            setCboCalenderFreq(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_SUB_CALENDER_DAY)));
            //system.out.println("%%%%%% listDepSubNoAll : "+listDepSubNoAll);
            txtAmount = CommonUtil.convertObjToStr(getTxtDepositAmount());
            txtDepDate = CommonUtil.convertObjToStr(getTdtDateOfDeposit());
            txtMatDate = CommonUtil.convertObjToStr(getTdtMaturityDate());
            notifyObservers();
            listDepSubNoAll = null;
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void populateDepSubNoTbl() {
        try {
            if (depSubNoMode == 0) {
                //--- Enters if a New Record is to be saved
                depSubNoK = checkDepSubNoForDuplication(depSubNoMode);
                if (depSubNoK == CANCEL) {
                    newModePopulateDepSubNoTbl();
                }
            } else if (depSubNoMode == 1) {
                //--- Enters if Modification of existing record is taking place
                depSubNoK = checkDepSubNoForDuplication(depSubNoMode);
                if (depSubNoK == CANCEL) {
                    editModePopulateDepSubNoTbl();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
        }
    }

    private void editModePopulateDepSubNoTbl() {
        depSubNoRow = tblDepSubNoAccInfo.getDataArrayList();
        //        depLienRow = tblLienDetails.getDataArrayList();
        ArrayList changedDepSubNoRow = new ArrayList();
        ModifyDepSubNo = depSubNoRowDel + 1;
        changedDepSubNoRow.add(0, String.valueOf(ModifyDepSubNo));
        changedDepSubNoRow.add(1, getTxtDepositAmount());
        changedDepSubNoRow.add(2, getTdtMaturityDate());
        changedDepSubNoRow.add(3, getTxtTotalInterestAmount());
//        depSubNoRow.set(getSelectedRowCount, changedDepSubNoRow);
        tblDepSubNoAccInfo.setDataArrayList(depSubNoRow, tblDepSubNoColTitle);
        //        tblLienDetails.setDataArrayList(depLienRow, tblLienColTitle);
        depSubNoAll.remove(String.valueOf(depSubNoRowDel));
        depSubNoRec = new HashMap();
        insertDepSubNoRecord();
        depSubNoAll.put(String.valueOf(depSubNoRowDel), depSubNoRec);
        depSubNoRow = null;
        depSubNoRec = null;
        changedDepSubNoRow = null;
    }

    /**
     * Deletes the Selected Record from the Deposit Sub No CTable
     *
     * @param delRowCount ---> int data type, the Selected Row that has to be
     * deleted
     * @param deleteKeyPressed ---> boolean data type, Enter whether Delete
     * Button is clicked or not
     */
    public void delDepSubNoTab(int delRowCount, boolean deleteKeyPressed) {
        //--- If the method is called by Pressing the Delete Button,
        //--- then get the RowCount as Row Selected.
        if (deleteKeyPressed == true) {
            delRowCount = depSubNoRowDel;
        }
        //--- Checks whether data is from Database or not and Does Deletion according to it.
        HashMap depSubNoCalc = (HashMap) depSubNoAll.get(String.valueOf(delRowCount));
        if (CommonUtil.convertObjToStr(depSubNoCalc.get(FLD_FOR_DB_YES_NO)).equals(YES_FULL_STR)) {
            depSubNoCalc.put(FLD_DEP_SUB_NO_STATUS, depSubNoStatus);
            depSubNoAll.put(String.valueOf(delRowCount), depSubNoCalc);
        } else if (CommonUtil.convertObjToStr(depSubNoCalc.get(FLD_FOR_DB_YES_NO)).equals(NO_FULL_STR)) {
            depSubNoAll.remove(String.valueOf(delRowCount));
            depSubNoCount = depSubNoCount - 1;
            notifyObservers();
        }
        //--- Resets the Table after Deleting the required row if it is from DB
        //--- else it removes the data
        if (CommonUtil.convertObjToStr(depSubNoCalc.get(FLD_FOR_DB_YES_NO)).equals(NO_FULL_STR)) {
            tblDepSubNoAccInfo.removeRow(getSelectedRowCount);
            int j = getSelectedRowCount;
            HashMap calcDepSubNo;
            int depSubNoAllSize = depSubNoAll.size();
            for (int i = delRowCount; i < depSubNoAllSize; i++) {
                tblDepSubNoAccInfo.setValueAt(new Integer(i + 1), j, 0);
                //--- Resets the HashMap after deleting the required row
                calcDepSubNo = (HashMap) depSubNoAll.get(String.valueOf(i + 1));
                calcDepSubNo.put(FLD_DEP_SUB_NO_DEP_SUB_NO, String.valueOf(i + 1));
                depSubNoAll.remove(String.valueOf(i + 1));
                depSubNoAll.put(String.valueOf(i), calcDepSubNo);
                j = j + 1;
            }
            calcDepSubNo = null;
        } else if (CommonUtil.convertObjToStr(depSubNoCalc.get(FLD_FOR_DB_YES_NO)).equals(YES_FULL_STR)) {
            tblDepSubNoAccInfo.removeRow(getSelectedRowCount);
        }
        depSubNoCalc = null;
    }

    private int checkDepSubNoForDuplication(int depSubNoMode) {
        if (depSubNoAll == null) {
            depSubNoAll = new HashMap();
        }
        int j = CANCEL;
        COptionPane depSubNoDuplicationCheck;
        HashMap depCalc;
        depSubNoCheckValueInsert();
        int depSubNoAllSize = depSubNoAll.size();
        for (int i = 0; i < depSubNoAllSize; i++) {
            depSubNoCheckValues.put(FLD_DEP_SUB_NO_DEP_SUB_NO, String.valueOf(i + 1));
            //--- For checking the duplication add the Status and DbYesOrNo field
            depCalc = (HashMap) depSubNoAll.get(String.valueOf(i));
            depSubNoCheckValues.put(FLD_DEP_SUB_NO_STATUS, CommonUtil.convertObjToStr(depCalc.get(FLD_DEP_SUB_NO_STATUS)));
            depSubNoCheckValues.put(FLD_FOR_DB_YES_NO, CommonUtil.convertObjToStr(depCalc.get(FLD_FOR_DB_YES_NO)));
            if (((i != depSubNoRowDel) && (depSubNoMode == 1)) || (depSubNoMode == 0)) {
                if ((depSubNoAll.get(String.valueOf(i)).equals(depSubNoCheckValues))) {
                    depSubNoDuplicationCheck = new COptionPane();
                    String[] options = {objTermDepositRB.getString(DIALOG_OK)};
                    j = depSubNoDuplicationCheck.showOptionDialog(null, objTermDepositRB.getString(WARN_MESSAGE_OK), CommonConstants.WARNINGTITLE, COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                    if (j == YES) {
                        depSubNoRowCount = i;
                    }
                    break;
                } else {
                    j = CANCEL;
                }
            }
        }
        depSubNoDuplicationCheck = null;
        depSubNoCheckValues = null;
        depCalc = null;
        return j;
    }

    private void newModePopulateDepSubNoTbl() {
        if (depSubNoAll == null) {
            depSubNoAll = new HashMap();
        }
        depSubNo = depSubNoCount + 1;
        if (getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            depSubNo = 0;
        }
        depSubNoRow = new ArrayList();
        depSubNoRow.add(0, String.valueOf(depSubNo));
        depSubNoRow.add(1, getTxtDepositAmount());
        depSubNoRow.add(2, getTdtMaturityDate());
        depSubNoRow.add(3, getTxtTotalInterestAmount());
        tblDepSubNoAccInfo.insertRow(tblDepSubNoAccInfo.getRowCount(), depSubNoRow);
        depSubNoRec = new HashMap();
        insertDepSubNoRecord();
        if (getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            depSubNoAll.put(String.valueOf(depSubNo), depSubNoRec);
        } else {
            depSubNoAll.put(String.valueOf(depSubNo - 1), depSubNoRec);
        }
        depSubNoRec = null;
        depSubNoRow = null;
        depSubNoCount = depSubNoCount + 1;
    }

    void setTblDepSubNo(EnhancedTableModel tblDepSubNoAccInfo) {
        this.tblDepSubNoAccInfo = tblDepSubNoAccInfo;
        setChanged();
    }

    EnhancedTableModel getTblDepSubNo() {
        return this.tblDepSubNoAccInfo;
    }

    void setRenewalTblDepSubNo(EnhancedTableModel tblRenewalDepSubNoAccInfo) {
        this.tblRenewalDepSubNoAccInfo = tblRenewalDepSubNoAccInfo;
        setChanged();
    }

    EnhancedTableModel getRenewalTblDepSubNo() {
        return this.tblRenewalDepSubNoAccInfo;
    }

    void setTblLien(EnhancedTableModel tblLienDetails) {
        this.tblLienDetails = tblLienDetails;
        setChanged();
    }

    EnhancedTableModel getTblLien() {
        return this.tblLienDetails;
    }

    private int checkDepSubNoForDuplicationExtension(int depSubNoMode) {
        if (renewaldepSubNoAll == null) {
            renewaldepSubNoAll = new HashMap();
        }
        int j = CANCEL;
        COptionPane depSubNoDuplicationCheck;
        HashMap depCalc;
        depSubNoCheckValueExtensionInsert();
        //        renewaldepSubNoAll.put(FLD_DEP_RENEWAL_SUB_PRODUCT_ID, CommonUtil.convertObjToStr(cbmRenewalDepositProdId.getKeyForSelected()));
        //        renewaldepSubNoAll.put(FLD_DEP_RENEWAL_SUB_CATEGORY ,CommonUtil.convertObjToStr(cbmRenewalDepositCategory.getKeyForSelected()));
        int depSubNoAllSize = renewaldepSubNoAll.size();
        for (int i = 0; i < depSubNoAllSize; i++) {
            depSubNoCheckRenewalValues.put(FLD_DEP_SUB_NO_DEP_SUB_NO, String.valueOf(i + 1));
            //--- For checking the duplication add the Status and DbYesOrNo field
            depCalc = (HashMap) renewaldepSubNoAll.get(String.valueOf(i));
            break;
            //            depSubNoCheckRenewalValues.put(FLD_DEP_SUB_NO_STATUS , CommonUtil.convertObjToStr(depCalc.get(FLD_DEP_SUB_NO_STATUS)));
            //            depSubNoCheckRenewalValues.put(FLD_FOR_DB_YES_NO , CommonUtil.convertObjToStr(depCalc.get(FLD_FOR_DB_YES_NO)));
            //            if(((i != depSubNoRowDel) && (depSubNoMode==1)) || (depSubNoMode==0)){
            //                if((renewaldepSubNoAll.get(String.valueOf(i)).equals(depSubNoCheckRenewalValues))){
            //                    depSubNoDuplicationCheck = new COptionPane();
            //                    String[] options = {objTermDepositRB.getString(DIALOG_OK)};
            //                    j=depSubNoDuplicationCheck.showOptionDialog(null,objTermDepositRB.getString(WARN_MESSAGE_OK),CommonConstants.WARNINGTITLE,COptionPane.OK_OPTION,COptionPane.WARNING_MESSAGE, null, options, options[0]);
            //                    if(j==YES){
            //                        depSubNoRowCount = i;
            //                    }
            //                    break;
            //                } else {
            //                    j=CANCEL;
            //                }
            //            }
        }
        depSubNoDuplicationCheck = null;
        depSubNoCheckRenewalValues = null;
        depCalc = null;
        return j;
    }

    private void newModePopulateRenewalDepSubNoTbl() {
        if (renewaldepSubNoAll == null) {
            renewaldepSubNoAll = new HashMap();
        }
        depSubNo = depSubNoCount + 1;
        renewalDepSubNoRow = new ArrayList();
        renewalDepSubNoRow.add(0, String.valueOf(depSubNo));
        renewalDepSubNoRow.add(1, getRenewaltxtDepositAmount());
        renewalDepSubNoRow.add(2, getRenewaltdtDateOfDeposit());
        renewalDepSubNoRow.add(3, getRenewaltxtTotalInterestAmount());
        tblRenewalDepSubNoAccInfo.insertRow(tblRenewalDepSubNoAccInfo.getRowCount(), renewalDepSubNoRow);
        renewaldepSubNoRec = new HashMap();
        insertRenewalDepSubNoRecord();
        renewaldepSubNoAll.put(String.valueOf(depSubNo - 1), renewaldepSubNoRec);
        renewaldepSubNoRec = null;
        renewalDepSubNoRow = null;
        depSubNoCount = depSubNoCount + 1;
    }

    /**
     * To set the Rate of Interest based on Product Id, Category ID, Period Of
     * Deposit and Deposit Amount
     */
    public double setRateOfInterset(HashMap paramMap) {
        double retInt = -1;
        long period = 0;
        HashMap whereMap = new HashMap();
        whereMap.put("PRODUCT_TYPE", SCREEN);
        if (paramMap == null) {
            whereMap.put("CATEGORY_ID", getCbmCategory().getKeyForSelected());
            whereMap.put("PROD_ID", getCbmProductId().getKeyForSelected());
            whereMap.put("AMOUNT", CommonUtil.convertObjToDouble(getTxtDepositAmount()));
            whereMap.put("DEPOSIT_DT", DateUtil.getDateMMDDYYYY(getTdtDateOfDeposit()));
            //system.out.println("#### setRateOfinterest : "+whereMap);
            Date startDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtDateOfDeposit));
            Date endDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtMaturityDate));
            //System.out.println("startDt"+startDt+"endDt"+endDt);
            //added by rishad at 13/02/2015  for mantis 0010356
            if (startDt != null && endDt != null) {
                int year = CommonUtil.convertObjToInt(getTxtPeriodOfDeposit_Years());
                int month = CommonUtil.convertObjToInt(getTxtPeriodOfDeposit_Months());
                if (year != 0 || month != 0) {
                    HashMap whereDateMap = new HashMap();
                    whereDateMap.put("START_DT", startDt);
                    whereDateMap.put("END_DT", endDt);
                    List monthList = (List) ClientUtil.executeQuery("getMonthInBetweenDayes", whereDateMap);
                    if (monthList != null && monthList.size() > 0) {
                        HashMap resultMap = (HashMap) monthList.get(0);
                        if (resultMap.containsKey("COUNT")) {
                            period = CommonUtil.convertObjToLong(resultMap.get("COUNT"));
                        }
                    }
                }
                period += DateUtil.dateDiff(startDt, endDt);
            }
            //system.out.println("#### period : "+period);
            //        int endYear = endDt.getYear()+1900;
            int count = 0;
//            while (DateUtil.dateDiff(startDt, endDt)>0) {
//                int month = startDt.getMonth();
//                int startYear = startDt.getYear()+1900;
////                if (month==1 && startYear%4==0)
//                if ((month==1 && startDt.getDate()>28 && count==0) || 
//                    (month>1 && startYear%4==0 && count==0))
//                    count++;
//                startDt = DateUtil.addDays(startDt, 30);
//            }
            period -= count;
            //        if ((getTxtPeriodOfDeposit_Days()!= null)&&(!getTxtPeriodOfDeposit_Days().equals(""))){
            //            period = period + Integer.parseInt(getTxtPeriodOfDeposit_Days());
            //        }
            //        if ((getTxtPeriodOfDeposit_Months()!= null)&&(!getTxtPeriodOfDeposit_Months().equals(""))){
            //            period = period + Integer.parseInt(getTxtPeriodOfDeposit_Months())*30;
            //        }
            //        if ((getTxtPeriodOfDeposit_Years()!= null)&&(!getTxtPeriodOfDeposit_Years().equals(""))){
            //            period = period + Integer.parseInt(getTxtPeriodOfDeposit_Years())*365;
            //            period = period /365;
            //        }
            whereMap.put("PERIOD", period);
        } else {
            whereMap.put("CATEGORY_ID", getCbmCategory().getKeyForSelected());
            whereMap.put("PROD_ID", getCbmProductId().getKeyForSelected());
            whereMap.putAll(paramMap);
        }
        String PRODUCT_TYPE = "PRODUCT_TYPE";
        String PRODUCT_TYPE_VALUE = "TD";
        whereMap.put(PRODUCT_TYPE, "TD");
        List dataList = (List) ClientUtil.executeQuery("icm.getInterestRates", whereMap);
        HashMap roiHash = new HashMap();
        if (dataList != null && dataList.size() > 0) {
            roiHash = (HashMap) dataList.get(0);
            //            if(roiHash.containsKey("ROI")){
            retInt = CommonUtil.convertObjToDouble(roiHash.get("ROI")).doubleValue();
            //system.out.println("rate of interest "+retInt);
            //            }
        } else {
            retInt = 0;
            //comm by jjjjjj
            // msgIntWarn();
            ///////
        }
        //system.out.println("paramMap:" + paramMap);
        //system.out.println("roiHash:" + roiHash);
        return retInt;

    }

    public void populateExtensionDepSubNoFields(int row) {
        try {
            HashMap listDepSubNoAll = (HashMap) extensiondepSubNoAll.get(String.valueOf(row));
            depSubNoMode = 1; //Set the mode for "Modification" of record
            depSubNoRowDel = row + 1;
            if (getViewTypeDeposit().equals("AUTHORIZE") || getViewTypeDeposit().equals("EDIT")) {
                setExtensiontdtDateOfDeposit(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_DEP_DT)));
                setExtensiontdtMaturityDate(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_MAT_DT)));
            } else {
                setExtensiontdtDateOfDeposit(CommonUtil.convertObjToStr(depDt));
                setExtensiontdtMaturityDate(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_MAT_DT)));
            }
            setExtensiontxtPeriodOfDeposit_Days(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_DEP_PER_DD)));
            setExtensiontxtPeriodOfDeposit_Months(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_DEP_PER_MM)));
            setExtensiontxtPeriodOfDeposit_Years(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_DEP_PER_YY)));
            setExtensiontxtDepositAmount(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_DEP_AMT)));
            setExtensiontxtRateOfInterest(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_RATE_OF_INT)));
            setExtensiontxtMaturityAmount(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_MAT_AMT)));
            setExtensiontxtTotalInterestAmount(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_TOT_INT_AMT)));
            setExtensiontxtPeriodicInterestAmount(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_PER_INT_AMT)));
            setExtensionValDepositSubNo(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_DEP_SUB_NO)));
            if (listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_PRODUCT_ID) != null && !listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_PRODUCT_ID).equals("")) {
                setCboExtensionDepositProdId(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_PRODUCT_ID)));
            }
            if (listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_CATEGORY) != null && !listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_CATEGORY).equals("")) {
                setCboExtensionDepositCategory(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_CATEGORY)));
            }
            if (listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_INT_PAY_FREQ) != null && !listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_INT_PAY_FREQ).equals("")) {
                setCboExtensionPaymentFrequency((String) getCbmExtensionPaymentFrequency().getDataForKey(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_INT_PAY_FREQ))));
            }
            setCboExtensionPaymentMode(CommonUtil.convertObjToStr(getCbmExtensionPaymentMode().getDataForKey(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_PAY_MODE)))));
            if (!CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_PROD_TYPE)).equals("")
                    && (getViewTypeDeposit().equals("AUTHORIZE") || getViewTypeDeposit().equals("EDIT"))) {
                setCboExtensionPaymentProdType(CommonUtil.convertObjToStr(getCbmExtensionPaymentProdType().getDataForKey(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_PROD_TYPE)))));
                setCbmExtensionPaymentProdId(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_PROD_TYPE)));
                setCboExtensionPaymentProdId((String) getCbmExtensionPaymentProdId().getDataForKey(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_PROD_ID))));
                setExtensioncustomerIdCrInt(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_AC_NO)));
                setExtensioncustomerNameCrValueInt(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_CUST_NAME)));
                String prodType = CommonUtil.convertObjToStr(getCboExtensionPaymentProdType());
                this.setProductTypes(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_PROD_TYPE)));
            }
            if (getRdoCalenderFreq_Yes() == true) {
                setCboExtensionCalenderFreqDay(CommonUtil.convertObjToStr(listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_CALENDER_DAY)));
            }
            if (listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_INT_PAY_FREQ) != null && listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_INT_PAY_FREQ).equals("0")) {
                setCboExtensionPaymentFrequency("Date of Maturity");
            } else if (listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_INT_PAY_FREQ) != null && listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_INT_PAY_FREQ).equals("30")) {
                setCboExtensionPaymentFrequency("Monthly");
            } else if (listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_INT_PAY_FREQ) != null && listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_INT_PAY_FREQ).equals("90")) {
                setCboExtensionPaymentFrequency("Quarterly");
            } else if (listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_INT_PAY_FREQ) != null && listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_INT_PAY_FREQ).equals("180")) {
                setCboExtensionPaymentFrequency("Half Yearly");
            } else if (listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_INT_PAY_FREQ) != null && listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_INT_PAY_FREQ).equals("360")) {
                setCboExtensionPaymentFrequency("Yearly");
            } else if (listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_INT_PAY_FREQ) != null && listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_INT_PAY_FREQ).equals("60")) {
                setCboExtensionPaymentFrequency("2 Months");
            } else if (listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_INT_PAY_FREQ) != null && listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_INT_PAY_FREQ).equals("120")) {
                setCboExtensionPaymentFrequency("4 Months");
            } else if (listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_INT_PAY_FREQ) != null && listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_INT_PAY_FREQ).equals("150")) {
                setCboExtensionPaymentFrequency("5 Months");
            } else if (listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_INT_PAY_FREQ) != null && listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_INT_PAY_FREQ).equals("180")) {
                setCboExtensionPaymentFrequency("6 Months");
            } else if (listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_INT_PAY_FREQ) != null && listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_INT_PAY_FREQ).equals("210")) {
                setCboExtensionPaymentFrequency("7 Months");
            } else if (listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_INT_PAY_FREQ) != null && listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_INT_PAY_FREQ).equals("240")) {
                setCboExtensionPaymentFrequency("8 Months");
            } else if (listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_INT_PAY_FREQ) != null && listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_INT_PAY_FREQ).equals("270")) {
                setCboExtensionPaymentFrequency("9 Months");
            } else if (listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_INT_PAY_FREQ) != null && listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_INT_PAY_FREQ).equals("300")) {
                setCboExtensionPaymentFrequency("10 Months");
            } else if (listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_INT_PAY_FREQ) != null && listDepSubNoAll.get(FLD_DEP_EXTENSION_SUB_NO_INT_PAY_FREQ).equals("330")) {
                setCboExtensionPaymentFrequency("11 Months");
            }
            //system.out.println("%%%%%% listDepSubNoAll : "+listDepSubNoAll);
            notifyObservers();
            listDepSubNoAll = null;
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void editModePopulateExtensionDepSubNoTbl() {
        extensionDepSubNoRow = tblExtensionDepSubNoAccInfo.getDataArrayList();
        ArrayList changedDepSubNoRow = new ArrayList();
        ModifyDepSubNo = depSubNoRowDel;
        if (ModifyDepSubNo == 0) {
            ModifyDepSubNo = 1;
        }
        changedDepSubNoRow.add(0, String.valueOf(ModifyDepSubNo));
        changedDepSubNoRow.add(1, getExtensiontxtDepositAmount());
        changedDepSubNoRow.add(2, getExtensiontdtDateOfDeposit());
        changedDepSubNoRow.add(3, getExtensiontxtTotalInterestAmount());
        extensionDepSubNoRow.set(getSelectedRowCount, changedDepSubNoRow);
        tblExtensionDepSubNoAccInfo.setDataArrayList(extensionDepSubNoRow, tblExtensionDepSubNoColTitle);
        extensiondepSubNoAll.remove(String.valueOf(depSubNoRowDel));
        extensiondepSubNoRec = new HashMap();
        insertExtensionDepSubNoRecord();
        extensiondepSubNoAll.put(String.valueOf(depSubNoRowDel - 1), extensiondepSubNoRec);
        extensionDepSubNoRow = null;
        extensiondepSubNoRec = null;
        changedDepSubNoRow = null;
    }

    public void populateExtensionDepSubNoTbl() {
        try {
            //            if(depSubNoMode == 0){
            //                //--- Enters if a New Record is to be saved
            //                depSubNoK=checkDepSubNoForDuplication(depSubNoMode);
            //                if(depSubNoK == CANCEL){
            //                    newModePopulateRenewalDepSubNoTbl();
            //                }
            //            } else
            //            if(depSubNoMode == 1){
            //--- Enters if Modification of existing record is taking place
            depSubNoK = checkDepSubNoForDuplicationExtension(depSubNoMode);
            if (depSubNoK == CANCEL) {
                editModePopulateExtensionDepSubNoTbl();
            }
            //            }
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void setAccountName(String AccountNo) {
        try {
            final HashMap accountNameMap = new HashMap();
            accountNameMap.put("ACC_NUM", AccountNo);
            final List resultList = ClientUtil.executeQuery("getAccountNumberName" + getCboProdType(), accountNameMap);
            if (resultList != null) {
                final HashMap resultMap = (HashMap) resultList.get(0);
                setCustomerNameCrValue(resultMap.get("CUSTOMER_NAME").toString());
            }
        } catch (Exception e) {
        }
    }

    private void editModePopulateRenewalDepSubNoTbl() {
        renewalDepSubNoRow = tblRenewalDepSubNoAccInfo.getDataArrayList();
        ArrayList changedDepSubNoRow = new ArrayList();
        ModifyDepSubNo = depSubNoRowDel;
        if (ModifyDepSubNo == 0) {
            ModifyDepSubNo = 1;
        }
        changedDepSubNoRow.add(0, String.valueOf(ModifyDepSubNo));
        changedDepSubNoRow.add(1, getRenewaltxtDepositAmount());
        changedDepSubNoRow.add(2, getRenewaltdtDateOfDeposit());
        changedDepSubNoRow.add(3, getRenewaltxtTotalInterestAmount());
        renewalDepSubNoRow.set(getSelectedRowCount, changedDepSubNoRow);
        tblRenewalDepSubNoAccInfo.setDataArrayList(renewalDepSubNoRow, tblRenewalDepSubNoColTitle);
        renewaldepSubNoAll.remove(String.valueOf(depSubNoRowDel));
        renewaldepSubNoRec = new HashMap();
        insertRenewalDepSubNoRecord();
        renewaldepSubNoAll.put(String.valueOf(depSubNoRowDel - 1), renewaldepSubNoRec);
        renewalDepSubNoRow = null;
        renewaldepSubNoRec = null;
        changedDepSubNoRow = null;
    }

    public void setCbmProdId(String prodType) {
        if (prodType.equals("GL")) {
            key = new ArrayList();
            value = new ArrayList();
        } else {
            try {
                lookUpHash = new HashMap();
                lookUpHash.put(CommonConstants.MAP_NAME, "Cash.getAccProduct" + prodType);
                lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                keyValue = ClientUtil.populateLookupData(lookUpHash);
                getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        cbmProdId = new ComboBoxModel(key, value);
        this.cbmProdId = cbmProdId;
        setChanged();
    }

    public long roundOffLower(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        return number - mod;
    }

    private void depSubNoCheckValueExtensionInsert() {
        depSubNoCheckExtensionValues = new HashMap();
        depSubNoCheckExtensionValues.put(FLD_DEP_EXTENSION_SUB_NO_MAT_DT, getExtensiontdtMaturityDate());
        depSubNoCheckExtensionValues.put(FLD_DEP_EXTENSION_SUB_NO_MAT_AMT, getExtensiontxtMaturityAmount());
        depSubNoCheckExtensionValues.put(FLD_DEP_EXTENSION_SUB_NO_RATE_OF_INT, getExtensiontxtRateOfInterest());
        depSubNoCheckExtensionValues.put(FLD_DEP_EXTENSION_SUB_NO_TOT_INT_AMT, getExtensiontxtTotalInterestAmount());
        depSubNoCheckExtensionValues.put(FLD_DEP_EXTENSION_SUB_NO_PER_INT_AMT, getExtensiontxtPeriodicInterestAmount());
        depSubNoCheckExtensionValues.put(FLD_DEP_EXTENSION_SUB_NO_DEP_DT, getExtensiontdtDateOfDeposit());
        //        depSubNoCheckRenewalValues.put(FLD_DEP_SUB_PAY_DATE , getTdtPaymentDate());
        depSubNoCheckExtensionValues.put(FLD_DEP_EXTENSION_SUB_NO_DEP_AMT, getExtensiontxtDepositAmount());
        //        depSubNoCheckExtensionValues.put(FLD_DEP_RENEWAL_SUB_NO_DEP_NO , getExtensionValDepositSubNo());
        depSubNoCheckExtensionValues.put(FLD_DEP_EXTENSION_SUB_NO_DEP_PER_DD, getExtensiontxtPeriodOfDeposit_Days());
        depSubNoCheckExtensionValues.put(FLD_DEP_EXTENSION_SUB_NO_DEP_PER_MM, getExtensiontxtPeriodOfDeposit_Months());
        depSubNoCheckExtensionValues.put(FLD_DEP_EXTENSION_SUB_NO_DEP_PER_YY, getExtensiontxtPeriodOfDeposit_Years());
        depSubNoCheckExtensionValues.put(FLD_DEP_EXTENSION_SUB_NO_INT_PAY_FREQ, CommonUtil.convertObjToStr(cbmExtensionPaymentFrequency.getKeyForSelected()));
        depSubNoCheckExtensionValues.put(FLD_DEP_SUB_NO_INT_PAY_MODE, CommonUtil.convertObjToStr(cbmExtensionPaymentMode.getKeyForSelected()));
        if (getRdoExtensionWithdrawing_Yes() == true) {
            depSubNoCheckExtensionValues.put(FLD_DEP_EXTENSION_SUB_WITHDRAWING_DEPAMT, getTxtExtensionTransAmtValue());
        } else if (getRdoExtensionAdding_Yes() == true) {
            depSubNoCheckExtensionValues.put(FLD_DEP_EXTENSION_SUB_ADDING_DEPAMT, getTxtExtensionTransAmtValue());
        }
        depSubNoCheckExtensionValues.put(FLD_DEP_EXTENSION_SUB_WITHDRAWING_INTAMT, getTxtExtensionIntAmtValue());
        depSubNoCheckExtensionValues.put(FLD_DEP_EXTENSION_SUB_WITHDRAWING_DEPAMT_TOKENO, getTxtExtensionTransTokenNo());
        //        depSubNoCheckExtensionValues.put(FLD_DEP_EXTENSION_SUB_WITHDRAWING_INTAMT_TOKENO, getTxtExtensionIntTokenNoVal());
        depSubNoCheckExtensionValues.put(FLD_DEP_EXTENSION_SUB_PRODUCT_ID, CommonUtil.convertObjToStr(cbmExtensionDepositProdId.getKeyForSelected()));
        depSubNoCheckExtensionValues.put(FLD_DEP_EXTENSION_SUB_CATEGORY, CommonUtil.convertObjToStr(cbmExtensionDepositCategory.getKeyForSelected()));
        //crediting to his customer accounts.....

        //        String prodType = CommonUtil.convertObjToStr(cbmExtensionProdType.getKeyForSelected());
        //        if(prodType.length() >0){
        //            depSubNoCheckRenewalValues.put(FLD_DEP_EXTENSION_SUB_PROD_TYPE, CommonUtil.convertObjToStr(cbmExtensionProdType.getKeyForSelected()));
        //            depSubNoCheckRenewalValues.put(FLD_DEP_EXTENSION_SUB_PROD_ID, CommonUtil.convertObjToStr(cbmExtensionProdId.getKeyForSelected()));
        //            depSubNoCheckRenewalValues.put(FLD_DEP_EXTENSION_SUB_AC_NO, CommonUtil.convertObjToStr(getExtensioncustomerIdCr()));
        //            depSubNoCheckRenewalValues.put(FLD_DEP_EXTENSION_SUB_CUST_NAME, CommonUtil.convertObjToStr(getExtensioncustomerNameCrValue()));
        //        }
    }

    private void insertExtensionDepSubNoRecord() {
        //        if(depSubNoMode == 0){
        extensiondepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_DEP_SUB_NO, String.valueOf(ModifyDepSubNo));
        //        }else if(depSubNoMode == 1){
        //            extensiondepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_DEP_SUB_NO, String.valueOf(ModifyDepSubNo));
        //        }
        extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_MAT_DT, getExtensiontdtMaturityDate());
        extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_MAT_AMT, getExtensiontxtMaturityAmount());
        extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_RATE_OF_INT, getExtensiontxtRateOfInterest());
        extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_TOT_INT_AMT, getExtensiontxtTotalInterestAmount());
        extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_PER_INT_AMT, getExtensiontxtPeriodicInterestAmount());
        extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_DEP_DT, getExtensiontdtDateOfDeposit());
        extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_DEP_AMT, getExtensiontxtDepositAmount());
        //        extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_DEP_NO , getRenewalValDepositSubNo());
        extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_DEP_PER_DD, getExtensiontxtPeriodOfDeposit_Days());
        extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_DEP_PER_MM, getExtensiontxtPeriodOfDeposit_Months());
        extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_DEP_PER_YY, getExtensiontxtPeriodOfDeposit_Years());
        //        extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_BALANCE_INTAMT, getExtensionBalIntAmt());
        //        extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_WITHDRAW_AMT_CALC, getExtensionSBIntAmtVal());
        //        extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_BALANCE_AMT_CALC, getExtensionValPeriodRun());
        extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_NO_INT_PAY_FREQ, CommonUtil.convertObjToStr(cbmExtensionPaymentFrequency.getKeyForSelected()));
        extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_DEP_PAY_MODE, CommonUtil.convertObjToStr(cbmExtensionTransMode.getKeyForSelected()));
        extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_PAY_MODE, CommonUtil.convertObjToStr(cbmExtensionPaymentMode.getKeyForSelected()));
        if (getRdoExtensionWithdrawing_Yes() == true) {
            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_WITHDRAWING_DEPAMT, getTxtExtensionTransAmtValue());
        } else if (getRdoRenewalAdding_Yes() == true) {
            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_ADDING_DEPAMT, getTxtExtensionTransAmtValue());
        }
        extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_WITHDRAWING_INTAMT, getTxtExtensionIntAmtValue());
        String depProdType = CommonUtil.convertObjToStr(cbmExtensionTransProdType.getKeyForSelected());
        if (extensiondepSubNoRec.get(FLD_DEP_EXTENSION_SUB_DEP_PAY_MODE).equals("TRANSFER") && depProdType.length() > 0) {
            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_DEP_PROD_TYPE, CommonUtil.convertObjToStr(cbmExtensionTransProdType.getKeyForSelected()));
            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_DEP_PROD_ID, CommonUtil.convertObjToStr(cbmExtensionTransProdId.getKeyForSelected()));
            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_DEP_ACCT_NUM, CommonUtil.convertObjToStr(getExtensioncustomerIdCrDep()));
            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_DEP_CUST_NAME, CommonUtil.convertObjToStr(getExtensioncustomerNameCrValueDep()));
        } else if (extensiondepSubNoRec.get(FLD_DEP_EXTENSION_SUB_DEP_PAY_MODE).equals("CASH"))//depositAmtTransaction
        {
            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_WITHDRAWING_DEPAMT_TOKENO, getTxtExtensionTransTokenNo());
        }

        if (getRdoExtensionCalenderFreq_Yes() == true) {
            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_CALENDER_FREQ, "Y");
            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_CALENDER_DAY, CommonUtil.convertObjToStr(cbmExtensionCalenderFreqDay.getKeyForSelected()));
        } else {
            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_CALENDER_FREQ, "N");
            extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_CALENDER_DAY, String.valueOf("Y"));
        }
        extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_PRODUCT_ID, CommonUtil.convertObjToStr(cbmExtensionDepositProdId.getKeyForSelected()));
        extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_CATEGORY, CommonUtil.convertObjToStr(cbmExtensionDepositCategory.getKeyForSelected()));
        extensiondepSubNoRec.put(FLD_DEP_EXTENSION_SUB_PRINTINGNO, CommonUtil.convertObjToStr(getTxtExtensionPrintedOption()));
        Date depDate = DateUtil.getDateMMDDYYYY(getExtensiontdtDateOfDeposit());
        int day = CommonUtil.convertObjToInt(cbmCalenderFreqDay.getKeyForSelected());
        depDate.setDate(day);
        setCalenderFreqDate(String.valueOf(depDate));
        //        renewaldepSubNoRec.put(FLD_DEP_SUB_CALENDER_DAY, String.valueOf(day));
        //        renewaldepSubNoRec.put(FLD_DEP_SUB_CALENDER_DATE, depDate);
        //        renewaldepSubNoRec.put(FLD_DEP_SUB_PAY_TYPE , CommonUtil.convertObjToStr(cbmPaymentType.getKeyForSelected()));
        //        renewaldepSubNoRec.put(FLD_DEP_SUB_NO_STATUS, depSubNoStatus);
        //        // To represent the data is not in Database
        //        renewaldepSubNoRec.put(FLD_FOR_DB_YES_NO, NO_FULL_STR);
    }

    public HashMap setExtensionAmountsAccROI(HashMap detailsHash, HashMap param) {
        //system.out.println("########setAmountsAccROI : "+detailsHash );
        HashMap amtDetHash = new HashMap();
        long period = 0;
        long cummPeriod = 0;
        long cummMonth = 0;
        double totalAmt = 0.0;
        if (param == null) {
            if (detailsHash.get("BEHAVES_LIKE").equals("FIXED")) {
                if (detailsHash.get("INTEREST_TYPE").equals("MONTHLY")) {
                    period = Integer.parseInt(getExtensiontxtPeriodOfDeposit_Months()) * 30;
                    period = period + Integer.parseInt(getExtensiontxtPeriodOfDeposit_Years()) * 360;
                } else {
                    Date startDt = null;
                    Date endDt = null;
                    if (detailsHash.containsKey("DEPOSIT_DT") && detailsHash.containsKey("MATURITY_DT")) {
                        startDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(detailsHash.get("DEPOSIT_DT")));
                        endDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(detailsHash.get("MATURITY_DT")));
                    } else {
                        startDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(ExtensiontdtDateOfDeposit));
                        endDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(ExtensiontdtMaturityDate));
                    }
                    period = DateUtil.dateDiff(startDt, endDt);
                    int count = 0;
                    while (DateUtil.dateDiff(startDt, endDt) > 0) {
                        int month = startDt.getMonth();
                        int startYear = startDt.getYear() + 1900;
                        if (month == 1 && startYear % 4 == 0) {
                            count++;
                        }
                        startDt = DateUtil.addDays(startDt, 30);
                    }
                    period -= count;
                }
            } else if (detailsHash.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
                if ((getExtensiontxtPeriodOfDeposit_Days() != null) && (!getExtensiontxtPeriodOfDeposit_Days().equals(""))) {
                    period = period + Integer.parseInt(getExtensiontxtPeriodOfDeposit_Days());
                    cummPeriod = period;
                }
                if ((getExtensiontxtPeriodOfDeposit_Months() != null) && (!getExtensiontxtPeriodOfDeposit_Months().equals(""))) {
                    period = period + Integer.parseInt(getExtensiontxtPeriodOfDeposit_Months()) * 30;
                    cummMonth = Integer.parseInt(getExtensiontxtPeriodOfDeposit_Months());
                }
                if ((getExtensiontxtPeriodOfDeposit_Years() != null) && (!getExtensiontxtPeriodOfDeposit_Years().equals(""))) {
                    period = period + Integer.parseInt(getExtensiontxtPeriodOfDeposit_Years()) * 360;
                }
                long fullPeriod = 0;
                fullPeriod = period;
                double simpleAmt = 0.0;
                double completeAmt = 0.0;
                cummPeriod = cummPeriod % 30;
                cummMonth = cummMonth % 3;
                if (cummMonth == 0) {
                    //system.out.println("******** cummPeriod == 0: "+cummPeriod);
                }
                if (cummPeriod > 0 || cummMonth > 0) {
                    cummMonth = cummMonth * 30;
                    cummPeriod = cummPeriod + cummMonth;
                    //system.out.println("******** cummPeriod != 0: "+cummPeriod);
                }
                if (fullPeriod > 0) {
                    period = fullPeriod - cummPeriod;
                    detailsHash.put("BEHAVES_LIKE", "CUMMULATIVE");
                    detailsHash.put("PEROID", String.valueOf(period));
                    detailsHash.put("CATEGORY_ID", cbmExtensionDepositCategory.getKeyForSelected());
                    detailsHash.put("PROD_ID", cbmExtensionDepositProdId.getKeyForSelected());
                    List list = (List) ClientUtil.executeQuery("getDepProdIntPay", detailsHash);
                    if (list.size() > 0) {
                        detailsHash.putAll((HashMap) list.get(0));
                        InterestCalc interestCalc = new InterestCalc();
                        amtDetHash = interestCalc.calcAmtDetails(detailsHash);
                        completeAmt = CommonUtil.convertObjToDouble(amtDetHash.get("INTEREST")).doubleValue();
                    }
                    detailsHash.put("AMOUNT", amtDetHash.get("AMOUNT"));
                    detailsHash.remove("PEROID");
                    int yearPer = CommonUtil.convertObjToInt(getExtensiontxtPeriodOfDeposit_Years());
                    yearPer = yearPer * 12;
                    int monthPer = CommonUtil.convertObjToInt(getExtensiontxtPeriodOfDeposit_Months());
                    monthPer = (monthPer + yearPer) / 3;
                    int totMonth = monthPer * 3;
                    int tot = 0;
                    Date endDt = null;//commented bcz 2,3 days showing wrongly...
                    //                    Date startDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(renewaltdtDateOfDeposit));
                    //                    if(monthPer >=1 ||cummPeriod>0){
                    //                        int yearTobeAdded = 1900;
                    //                        int totYr = 0;
                    //                        int bal = 0;
                    //                        int day = 0;
                    //                        monthPer = CommonUtil.convertObjToInt(getRenewaltxtPeriodOfDeposit_Months());
                    //                        int month = 0;
                    //                        int year = 0;
                    //                        if(totMonth>=12){
                    //                            totYr = totMonth /12;
                    //                            year = year +totYr;
                    //                        }else{
                    //                            bal = totMonth;
                    //                        }
                    //                        if(totYr>=1){
                    //                            totYr = totYr *12;
                    //                            bal = totMonth - totYr;
                    //                        }
                    //                        GregorianCalendar cal = new GregorianCalendar((startDt.getYear()+yearTobeAdded),startDt.getMonth(),startDt.getDate());
                    //                        cal.add(GregorianCalendar.YEAR, year);
                    //                        cal.add(GregorianCalendar.MONTH, bal);
                    //                        cal.add(GregorianCalendar.DAY_OF_MONTH, day);
                    //                        String depDt =DateUtil.getStringDate(cal.getTime());
                    //                        Date date = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depDt));
                    //                        endDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(renewaltdtMaturityDate));
                    //                        period = DateUtil.dateDiff(date, endDt);
                    //                    }
                }
                if (cummPeriod > 0) {
                    detailsHash.put("BEHAVES_LIKE", "FIXED");
                    detailsHash.put("PEROID", String.valueOf(cummPeriod));
                    detailsHash.put("CATEGORY_ID", cbmExtensionDepositCategory.getKeyForSelected());
                    detailsHash.put("PROD_ID", cbmExtensionDepositProdId.getKeyForSelected());
                    List list = (List) ClientUtil.executeQuery("getDepProdIntPay", detailsHash);
                    if (list.size() > 0) {
                        detailsHash.putAll((HashMap) list.get(0));
                        InterestCalc interestCalc = new InterestCalc();
                        detailsHash.put("INTEREST_TYPE", "YEARLY");
                        amtDetHash = interestCalc.calcAmtDetails(detailsHash);
                        simpleAmt = CommonUtil.convertObjToDouble(amtDetHash.get("INTEREST")).doubleValue();
                    }
                }
                detailsHash.put("BEHAVES_LIKE", "CUMMULATIVE");
                double interest = simpleAmt + completeAmt;
                amtDetHash.put("INTEREST", new Double(interest));
                //system.out.println("******** : "+detailsHash);
            } else {
                detailsHash.put("PEROID", String.valueOf(period));
            }
            //system.out.println(" set Amount ROI rate of interest param"+detailsHash);
        }
        if (!detailsHash.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
            //--- If the param is NUll then put category and Prod ,else put all the param value to the detailHash
            detailsHash.put("PEROID", String.valueOf(period));
            if (param == null) {
                detailsHash.put("CATEGORY_ID", cbmExtensionDepositCategory.getKeyForSelected());
                detailsHash.put("PROD_ID", cbmExtensionDepositProdId.getKeyForSelected());
            } else {
                detailsHash.putAll(param);
            }
            HashMap discountedMap = new HashMap();
            discountedMap.put("PROD_ID", detailsHash.get("PROD_ID"));
            List lstDiscounted = ClientUtil.executeQuery("getDepProdDetails", discountedMap);
            if (lstDiscounted != null && lstDiscounted.size() > 0) {
                discountedMap = (HashMap) lstDiscounted.get(0);
                detailsHash.put("DISCOUNTED_RATE", discountedMap.get("DISCOUNTED_RATE"));
            }
            discountedMap = null;
            List list = (List) ClientUtil.executeQuery("getDepProdIntPay", detailsHash);
            if (list != null && list.size() > 0) {
                detailsHash.putAll((HashMap) list.get(0));
                InterestCalc interestCalc = new InterestCalc();
                amtDetHash = interestCalc.calcAmtDetails(detailsHash);
            }
        }
        return amtDetHash;
    }

    public void populateRenewalDepSubNoTbl() {
        try {
            //            if(depSubNoMode == 0){
            //                //--- Enters if a New Record is to be saved
            //                depSubNoK=checkDepSubNoForDuplication(depSubNoMode);
            //                if(depSubNoK == CANCEL){
            //                    newModePopulateRenewalDepSubNoTbl();
            //                }
            //            } else
            //            if(depSubNoMode == 1){
            //--- Enters if Modification of existing record is taking place
            depSubNoK = checkDepSubNoForDuplicationRenewal(depSubNoMode);
            if (depSubNoK == CANCEL) {
                editModePopulateRenewalDepSubNoTbl();
            }
            //            }
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
        }
    }

    //    public void setCbmExtensionTransMode(String prodType) {
    //        if (prodType.equals("GL")) {
    //            key = new ArrayList();
    //            value = new ArrayList();
    //        } else {
    //            try {
    //                lookUpHash = new HashMap();
    //                lookUpHash.put(CommonConstants.MAP_NAME,"Cash.getAccProduct" + prodType);
    //                lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
    //                keyValue = ClientUtil.populateLookupData(lookUpHash);
    //                getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
    //            } catch (Exception ex) {
    //                ex.printStackTrace();
    //            }
    //        }
    //        cbmExtensionTransProdId = new ComboBoxModel(key,value);
    //        this.cbmExtensionTransProdId = cbmExtensionTransProdId;
    //        setChanged();
    //    }
    //    public void setCbmExtensionPayMode(String prodType) {
    //        if (prodType.equals("GL")) {
    //            key = new ArrayList();
    //            value = new ArrayList();
    //        } else {
    //            try {
    //                lookUpHash = new HashMap();
    //                lookUpHash.put(CommonConstants.MAP_NAME,"Cash.getAccProduct" + prodType);
    //                lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
    //                keyValue = ClientUtil.populateLookupData(lookUpHash);
    //                getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
    //            } catch (Exception ex) {
    //                ex.printStackTrace();
    //            }
    //        }
    //        cbmExtensionPaymentProdId = new ComboBoxModel(key,value);
    //        this.cbmExtensionPaymentProdId = cbmExtensionPaymentProdId;
    //        setChanged();
    //    }
    public HashMap setAmountsAccROI(HashMap detailsHash, HashMap param) {
        //system.out.println("########setAmountsAccROI : "+detailsHash );
        HashMap amtDetHash = new HashMap();
        long period = 0;
        long cummPeriod = 0;
        long cummMonth = 0;
        double totalAmt = 0.0;
        //        double amount = CommonUtil.convertObjToDouble(detailsHash.get("AMOUNT")).doubleValue();
        //--- If the param is NUll then calculate period ,else put all the param value to the detailHash
        if (param == null) {
            if (detailsHash.get("BEHAVES_LIKE").equals("FIXED")) {
                if (detailsHash.get("INTEREST_TYPE").equals("MONTHLY")) {
                    if (detailsHash.containsKey("EXTENSION")) {
                        period = CommonUtil.convertObjToLong(detailsHash.get("PERIOD"));
                    } else {
                        period = Integer.parseInt(getTxtPeriodOfDeposit_Months()) * 30;
                        period = period + Integer.parseInt(getTxtPeriodOfDeposit_Years()) * 360;
                    }
                } else {
                    Date startDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtDateOfDeposit));
                    Date endDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtMaturityDate));
                    period = DateUtil.dateDiff(startDt, endDt);
                    //system.out.println("#####period :"+period);
                    if (!detailsHash.containsKey("EXTENSION")) {
                        int count = 0;
                        while (DateUtil.dateDiff(startDt, endDt) > 0) {
                            int month = startDt.getMonth();
                            int startYear = startDt.getYear() + 1900;
                            if (month == 1 && startYear % 4 == 0) {
                                count++;
                            }
                            startDt = DateUtil.addDays(startDt, 30);
                        }
                        period -= count;
                    }
                }
            } else {
                if ((getTxtPeriodOfDeposit_Days() != null) && (!getTxtPeriodOfDeposit_Days().equals(""))) {
                    if (detailsHash.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
                        period = period + Integer.parseInt(getTxtPeriodOfDeposit_Days());
                        cummPeriod = period;
                    }//else
                    //                    period = period + Integer.parseInt(getTxtPeriodOfDeposit_Days());
                    //                //system.out.println(" set Amount ROI rate of interest param Period Days"+period);
                }
                if ((getTxtPeriodOfDeposit_Months() != null) && (!getTxtPeriodOfDeposit_Months().equals(""))) {
                    //                if(detailsHash.get("BEHAVES_LIKE").equals("FIXED")){
                    //                    period = period + Integer.parseInt(getTxtPeriodOfDeposit_Months());
                    //                    period = CommonUtil.convertObjToInt(detailsHash.get("PERIOD_MONTHS"));
                    //                    int month = Integer.parseInt(getTxtPeriodOfDeposit_Months());
                    //                    if(month >0)
                    //                        period = period + Integer.parseInt(getTxtPeriodOfDeposit_Months()) * 30;
                    //                        period = (period/12)*365;
                    //                    period = period + Integer.parseInt(getTxtPeriodOfDeposit_Months())*30;
                    //                }else
                    period = period + Integer.parseInt(getTxtPeriodOfDeposit_Months()) * 30;
                    //system.out.println(" set Amount ROI rate of interest param Period Months"+period);
                    if (detailsHash.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
                        cummMonth = Integer.parseInt(getTxtPeriodOfDeposit_Months());
                    }
                }
                if ((getTxtPeriodOfDeposit_Years() != null) && (!getTxtPeriodOfDeposit_Years().equals(""))) {
                    period = period + Integer.parseInt(getTxtPeriodOfDeposit_Years()) * 360;
                    //system.out.println(" set Amount ROI rate of interest param Period Years"+period);
                }
            }
            if (detailsHash.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
                long fullPeriod = 0;
                fullPeriod = period;
                double simpleAmt = 0.0;
                double completeAmt = 0.0;
                cummPeriod = cummPeriod % 30;
                cummMonth = cummMonth % 3;
                if (cummMonth == 0) {
                    //system.out.println("******** cummPeriod == 0: "+cummPeriod);
                }
                if (cummPeriod > 0 || cummMonth > 0) {
                    cummMonth = cummMonth * 30;
                    cummPeriod = cummPeriod + cummMonth;
                    //system.out.println("******** cummPeriod != 0: "+cummPeriod);
                }
                if (fullPeriod > 0) {
                    period = fullPeriod - cummPeriod;
                    detailsHash.put("BEHAVES_LIKE", "CUMMULATIVE");
                    detailsHash.put("PEROID", String.valueOf(period));
                    detailsHash.put("CATEGORY_ID", cbmCategory.getKeyForSelected());
                    detailsHash.put("PROD_ID", cbmProductId.getKeyForSelected());
                    List list = (List) ClientUtil.executeQuery("getDepProdIntPay", detailsHash);
                    if (list.size() > 0) {
                        detailsHash.putAll((HashMap) list.get(0));
                        InterestCalc interestCalc = new InterestCalc();
                        //system.out.println(" beforeinterestCalcamtDetHash"+detailsHash);
                        amtDetHash = interestCalc.calcAmtDetails(detailsHash);
                        completeAmt = CommonUtil.convertObjToDouble(amtDetHash.get("INTEREST")).doubleValue();
                        //system.out.println("******** : "+completeAmt);
                        //system.out.println("******** AMOUNT : "+amtDetHash.get("AMOUNT"));
                    }
                    detailsHash.put("AMOUNT", amtDetHash.get("AMOUNT"));
                    detailsHash.remove("PEROID");
                    int yearPer = CommonUtil.convertObjToInt(getTxtPeriodOfDeposit_Years());
                    yearPer = yearPer * 12;
                    //system.out.println("******** yearPer : "+yearPer);
                    int monthPer = CommonUtil.convertObjToInt(getTxtPeriodOfDeposit_Months());
                    monthPer = (monthPer + yearPer) / 3;
                    int totMonth = monthPer * 3;
                    //                    monthPer = (double)roundOffLower((long)(monthPer*100),100)/100;
                    //system.out.println("******** monthPer : "+monthPer);
                    int tot = 0;
                    Date endDt = null;//commented bcz 2,3 days showing wrongly...
                    Date startDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtDateOfDeposit));
                    if (monthPer >= 1 || cummPeriod > 0) {
                        int yearTobeAdded = 1900;
                        int totYr = 0;
                        int bal = 0;
                        int day = 0;
                        monthPer = CommonUtil.convertObjToInt(getTxtPeriodOfDeposit_Months());
                        int month = 0;
                        int year = 0;
                        if (totMonth >= 12) {
                            totYr = totMonth / 12;
                            year = year + totYr;
                        } else {
                            bal = totMonth;
                        }
                        if (totYr >= 1) {
                            totYr = totYr * 12;
                            bal = totMonth - totYr;
                        }
                        GregorianCalendar cal = new GregorianCalendar((startDt.getYear() + yearTobeAdded), startDt.getMonth(), startDt.getDate());
                        cal.add(GregorianCalendar.YEAR, year);
                        cal.add(GregorianCalendar.MONTH, bal);
                        cal.add(GregorianCalendar.DAY_OF_MONTH, day);
                        String depDt = DateUtil.getStringDate(cal.getTime());
                        Date date = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depDt));
                        //                        monthPer = (double)roundOffLower((long)(monthPer*100),100)/100;
                        //                        monthPer = monthPer *3;
                        //                        tot = (int) (yearPer + monthPer);
                        //                        for(int i =0; i<tot;i++)
                        //                            startDt = DateUtil.addDays(startDt,30);
                        if (detailsHash.containsKey("EXTENSION")) {
                            endDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(currDt));
                        } else {
                            endDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtMaturityDate));
                        }
                        period = DateUtil.dateDiff(date, endDt);
                        //system.out.println("******** period : "+period);
                    } else {
                        //                        tot = (int)yearPer;
                        //                        for(int i =0; i<tot;i++)
                        //                            startDt = DateUtil.addDays(startDt,30);
                        //                        endDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtMaturityDate));
                        //                        period = DateUtil.dateDiff(startDt, endDt);
                        //                        //system.out.println("******** period : "+period);
                    }
                }
                if (cummPeriod > 0) {
                    detailsHash.put("BEHAVES_LIKE", "FIXED");
                    detailsHash.put("PEROID", String.valueOf(cummPeriod));
                    detailsHash.put("CATEGORY_ID", cbmCategory.getKeyForSelected());
                    detailsHash.put("PROD_ID", cbmProductId.getKeyForSelected());
                    List list = (List) ClientUtil.executeQuery("getDepProdIntPay", detailsHash);
                    if (list.size() > 0) {
                        detailsHash.putAll((HashMap) list.get(0));
                        InterestCalc interestCalc = new InterestCalc();
                        detailsHash.put("INTEREST_TYPE", "YEARLY");
                        //system.out.println(" beforeinterestCalcamtDetHash"+detailsHash);
                        detailsHash.put("INTEREST_TYPE", "YEARLY");
                        amtDetHash = interestCalc.calcAmtDetails(detailsHash);
                        simpleAmt = CommonUtil.convertObjToDouble(amtDetHash.get("INTEREST")).doubleValue();
                        //system.out.println("******** : "+simpleAmt);
                    }
                }
                detailsHash.put("BEHAVES_LIKE", "CUMMULATIVE");
                double interest = simpleAmt + completeAmt;
                amtDetHash.put("INTEREST", new Double(interest));
                //system.out.println("******** : "+detailsHash);
            } else {
                
                detailsHash.put("PEROID", String.valueOf(period));
            }
            //system.out.println(" set Amount ROI rate of interest param"+detailsHash);
        }
        if (!detailsHash.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
            //--- If the param is NUll then put category and Prod ,else put all the param value to the detailHash
            if (param == null) {
                detailsHash.put("CATEGORY_ID", cbmCategory.getKeyForSelected());
                detailsHash.put("PROD_ID", cbmProductId.getKeyForSelected());
            } else {
                detailsHash.putAll(param);
            }
            HashMap discountedMap = new HashMap();
            discountedMap.put("PROD_ID", detailsHash.get("PROD_ID"));
            List lstDiscounted = ClientUtil.executeQuery("getDepProdDetails", discountedMap);
            if (lstDiscounted != null && lstDiscounted.size() > 0) {
                discountedMap = (HashMap) lstDiscounted.get(0);
                detailsHash.put("DISCOUNTED_RATE", discountedMap.get("DISCOUNTED_RATE"));
            }
            List list = (List) ClientUtil.executeQuery("getDepProdIntPay", detailsHash);
            //Added By Chithra		
            if (detailsHash.get("BEHAVES_LIKE").equals("RECURRING") && getTxtPeriodOfDeposit_Weeks() != null
                    && !getTxtPeriodOfDeposit_Weeks().equals("") && CommonUtil.convertObjToInt(getTxtPeriodOfDeposit_Weeks()) > 0) {
                period = CommonUtil.convertObjToLong(getTxtPeriodOfDeposit_Weeks()) * 7;
                detailsHash.put("PEROID", CommonUtil.convertObjToStr(period));
                detailsHash.put("WEEKLY_DEP", "WEEKLY_DEP");
                amtDetHash = calcWeeklyIntrestForRD(detailsHash);
            }
           else if (list.size() > 0) {
                detailsHash.putAll((HashMap) list.get(0));
                InterestCalc interestCalc = new InterestCalc();
                //system.out.println(" beforeinterestCalcamtDetHash"+detailsHash);
                amtDetHash = interestCalc.calcAmtDetails(detailsHash);
            }
        }
        //system.out.println(" inside set Amount ROI rate of interest amtDetHash"+amtDetHash);
        //        //system.out.println("detailsHash:" + detailsHash);
        return amtDetHash;
    }
    public HashMap calcWeeklyIntrestForRD(HashMap fromMap) {
        int totInst = CommonUtil.convertObjToInt(getTxtPeriodOfDeposit_Weeks());
        Date dt = (Date) fromMap.get("DEPOSIT_DT");
        Date matDt = (Date) fromMap.get("MATURITY_DT");
        double amt = 0;
        amt = CommonUtil.convertObjToDouble(getTxtDepositAmount());
        double instNo = 0, instPenal = 0;
        List slabList = ClientUtil.executeQuery("getRdWeeklyInstallmentSlabs", fromMap);
        for (int k = 0; k < slabList.size(); k++) {
            HashMap each = (HashMap) slabList.get(k);
            if (each != null && each.size() > 0) {
                double fromInst = CommonUtil.convertObjToDouble(each.get("FROM_INSTALL"));
                double toInst = CommonUtil.convertObjToDouble(each.get("TO_INSTALL"));
                if (totInst > fromInst && totInst <= toInst) {
                    instNo = CommonUtil.convertObjToDouble(each.get("INSTALLMENT_NO"));
                }
            }
        }
        double intamt = amt * instNo;
        fromMap.put("INTEREST", intamt);
        amt = (amt * totInst) + intamt;
        fromMap.put("AMOUNT", amt);

        return fromMap;
    }
    public void msgIntWarn() {
        intWarnMsg = " Rate of Interest is not set for this Period. \n";
    }

    private void insertRenewalDepSubNoRecord() {
        //        if(depSubNoMode == 0){
        renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_DEP_SUB_NO, String.valueOf(ModifyDepSubNo));
        //        }else if(depSubNoMode == 1){
        //            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_DEP_SUB_NO, String.valueOf(ModifyDepSubNo));
        //        }
        renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_MAT_DT, getRenewaltdtMaturityDate());
        renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_MAT_AMT, getRenewaltxtMaturityAmount());
        renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_RATE_OF_INT, getRenewaltxtRateOfInterest());
        renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_TOT_INT_AMT, getRenewaltxtTotalInterestAmount());
        renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_PER_INT_AMT, getRenewaltxtPeriodicInterestAmount());
        renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_DEP_DT, getRenewaltdtDateOfDeposit());
        renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_DEP_AMT, getRenewaltxtDepositAmount());
        renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_DEP_NO, getRenewalValDepositSubNo());
        renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_DEP_PER_DD, getRenewaltxtPeriodOfDeposit_Days());
        renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_DEP_PER_MM, getRenewaltxtPeriodOfDeposit_Months());
        renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_DEP_PER_YY, getRenewaltxtPeriodOfDeposit_Years());
        renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_BALANCE_INTAMT, getRenewalBalIntAmt());
        renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_SB_INTAMT, getRenewalSBIntAmtVal());
        renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_SB_PERIOD, getRenewalValPeriodRun());
        renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_NO_INT_PAY_FREQ, CommonUtil.convertObjToStr(cbmRenewalInterestPaymentFrequency.getKeyForSelected()));
        renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_INT_PAY_MODE, CommonUtil.convertObjToStr(cbmRenewalInterestTransMode.getKeyForSelected()));
        renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_DEP_PAY_MODE, CommonUtil.convertObjToStr(cbmRenewalDepTransMode.getKeyForSelected()));
        renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_PAY_MODE, CommonUtil.convertObjToStr(cbmRenewalInterestPaymentMode.getKeyForSelected()));
        if (getRdoRenewalWithdrawing_Yes() == true) {
            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_WITHDRAWING_DEPAMT, getTxtRenewalDepTransAmtValue());
        } else if (getRdoRenewalAdding_Yes() == true) {
            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_ADDING_DEPAMT, getTxtRenewalDepTransAmtValue());
        }
        renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_WITHDRAWING_INTAMT, getTxtRenewalIntAmtValue());
        String depProdType = CommonUtil.convertObjToStr(cbmRenewalDepTransProdType.getKeyForSelected());
        if (renewaldepSubNoRec.get(FLD_DEP_RENEWAL_SUB_DEP_PAY_MODE).equals("TRANSFER") && depProdType.length() > 0) {
            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_DEP_PROD_TYPE, CommonUtil.convertObjToStr(cbmRenewalDepTransProdType.getKeyForSelected()));
            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_DEP_PROD_ID, CommonUtil.convertObjToStr(cbmRenewalDepTransProdId.getKeyForSelected()));
            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_DEP_ACCT_NUM, CommonUtil.convertObjToStr(getRenewalcustomerIdCrDep()));
            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_DEP_CUST_NAME, CommonUtil.convertObjToStr(getRenewalcustomerNameCrValueDep()));
        } else if (renewaldepSubNoRec.get(FLD_DEP_RENEWAL_SUB_DEP_PAY_MODE).equals("CASH"))//depositAmtTransaction
        {
            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_WITHDRAWING_DEPAMT_TOKENO, getTxtRenewalIntTokenNoVal());
        }

        String intProdType = CommonUtil.convertObjToStr(cbmRenewalInterestTransProdType.getKeyForSelected());
        if (renewaldepSubNoRec.get(FLD_DEP_RENEWAL_SUB_INT_PAY_MODE).equals("TRANSFER") && intProdType.length() > 0) {
            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_INT_PROD_TYPE, CommonUtil.convertObjToStr(cbmRenewalInterestTransProdType.getKeyForSelected()));
            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_INT_PROD_ID, CommonUtil.convertObjToStr(cbmRenewalInterestTransProdId.getKeyForSelected()));
            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_INT_ACCT_NUM, CommonUtil.convertObjToStr(getRenewalcustomerIdCrInt()));
            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_INT_CUST_NAME, CommonUtil.convertObjToStr(getRenewalcustomerNameCrValueInt()));
        } else if (renewaldepSubNoRec.get(FLD_DEP_RENEWAL_SUB_INT_PAY_MODE).equals("CASH"))//intAmtTransaction
        {
            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_WITHDRAWING_INTAMT_TOKENO, getTxtRenewalDepTransTokenNo());
        }

        //crediting to his customer accounts.....
        String prodType = CommonUtil.convertObjToStr(cbmRenewalProdType.getKeyForSelected());
        if (renewaldepSubNoRec.get(FLD_DEP_RENEWAL_SUB_PAY_MODE).equals("TRANSFER") && prodType.length() > 0) {
            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_PROD_TYPE, CommonUtil.convertObjToStr(cbmRenewalProdType.getKeyForSelected()));
            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_PROD_ID, CommonUtil.convertObjToStr(cbmRenewalProdId.getKeyForSelected()));
            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_AC_NO, CommonUtil.convertObjToStr(getRenewalcustomerIdCr()));
            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_CUST_NAME, CommonUtil.convertObjToStr(getRenewalcustomerNameCrValue()));
        }
        if (getRdoRenewalCalenderFreq_Yes() == true) {
            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_CALENDER_FREQ, "Y");
            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_CALENDER_DAY, CommonUtil.convertObjToStr(cbmRenewalCalenderFreqDay.getKeyForSelected()));
        } else {
            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_CALENDER_FREQ, "N");
            renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_CALENDER_DAY, String.valueOf("Y"));
        }
        renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_PRODUCT_ID, CommonUtil.convertObjToStr(cbmRenewalDepositProdId.getKeyForSelected()));
        renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_CATEGORY, CommonUtil.convertObjToStr(cbmRenewalDepositCategory.getKeyForSelected()));
        renewaldepSubNoRec.put(FLD_DEP_RENEWAL_SUB_PRINTINGNO, CommonUtil.convertObjToStr(getTxtRenewalPrintedOption()));
        Date depDate = DateUtil.getDateMMDDYYYY(getRenewaltdtDateOfDeposit());
        int day = CommonUtil.convertObjToInt(cbmCalenderFreqDay.getKeyForSelected());
        depDate.setDate(day);
        setCalenderFreqDate(String.valueOf(depDate));
        //        renewaldepSubNoRec.put(FLD_DEP_SUB_CALENDER_DAY, String.valueOf(day));
        //        renewaldepSubNoRec.put(FLD_DEP_SUB_CALENDER_DATE, depDate);
        //        renewaldepSubNoRec.put(FLD_DEP_SUB_PAY_TYPE , CommonUtil.convertObjToStr(cbmPaymentType.getKeyForSelected()));
        //        renewaldepSubNoRec.put(FLD_DEP_SUB_NO_STATUS, depSubNoStatus);
        //        // To represent the data is not in Database
        //        renewaldepSubNoRec.put(FLD_FOR_DB_YES_NO, NO_FULL_STR);
    }

    public void setCbmRenewalDepTransProdId(String prodType) {
        if (prodType.equals("GL")) {
            key = new ArrayList();
            value = new ArrayList();
        } else {
            try {
                lookUpHash = new HashMap();
                lookUpHash.put(CommonConstants.MAP_NAME, "Cash.getAccProduct" + prodType);
                lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                keyValue = ClientUtil.populateLookupData(lookUpHash);
                getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        cbmRenewalDepTransProdId = new ComboBoxModel(key, value);
        this.cbmRenewalDepTransProdId = cbmRenewalDepTransProdId;
        setChanged();
    }

    public void setCbmRenewalProdId(String prodType) {
        if (prodType.equals("GL")) {
            key = new ArrayList();
            value = new ArrayList();
        } else {
            try {
                lookUpHash = new HashMap();
                lookUpHash.put(CommonConstants.MAP_NAME, "Cash.getAccProduct" + prodType);
                lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                keyValue = ClientUtil.populateLookupData(lookUpHash);
                getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        cbmRenewalProdId = new ComboBoxModel(key, value);
        this.cbmRenewalProdId = cbmRenewalProdId;
        setChanged();
    }

    public void setCbmRenewalInterestTransProdId(String prodType) {
        if (prodType.equals("GL")) {
            key = new ArrayList();
            value = new ArrayList();
        } else {
            try {
                lookUpHash = new HashMap();
                lookUpHash.put(CommonConstants.MAP_NAME, "Cash.getAccProduct" + prodType);
                lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                keyValue = ClientUtil.populateLookupData(lookUpHash);
                getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        cbmRenewalInterestTransProdId = new ComboBoxModel(key, value);
        this.cbmRenewalInterestTransProdId = cbmRenewalInterestTransProdId;
        setChanged();
    }

    public void setCbmExtensionTransProdId(String prodType) {
        if (prodType.equals("GL")) {
            key = new ArrayList();
            value = new ArrayList();
        } else {
            try {
                lookUpHash = new HashMap();
                lookUpHash.put(CommonConstants.MAP_NAME, "Cash.getAccProduct" + prodType);
                lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                keyValue = ClientUtil.populateLookupData(lookUpHash);
                getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        cbmExtensionTransProdId = new ComboBoxModel(key, value);
        this.cbmExtensionTransProdId = cbmExtensionTransProdId;
        setChanged();
    }

    public void setCbmExtensionPaymentProdId(String prodType) {
        if (prodType.equals("GL")) {
            key = new ArrayList();
            value = new ArrayList();
        } else {
            try {
                lookUpHash = new HashMap();
                lookUpHash.put(CommonConstants.MAP_NAME, "Cash.getAccProduct" + prodType);
                lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                keyValue = ClientUtil.populateLookupData(lookUpHash);
                getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        cbmExtensionPaymentProdId = new ComboBoxModel(key, value);
        this.cbmExtensionPaymentProdId = cbmExtensionPaymentProdId;
        setChanged();
    }

    private void insertDepSubNoRecord() {
        if (depSubNoMode == 0) {
            depSubNoRec.put(FLD_DEP_SUB_NO_DEP_SUB_NO, String.valueOf(depSubNo));
        } else if (depSubNoMode == 1) {
            depSubNoRec.put(FLD_DEP_SUB_NO_DEP_SUB_NO, String.valueOf(ModifyDepSubNo));
        }
        depSubNoRec.put(FLD_DEP_SUB_NO_MAT_DT, getTdtMaturityDate());
        depSubNoRec.put(FLD_DEP_SUB_NO_MAT_AMT, getTxtMaturityAmount());
        depSubNoRec.put(FLD_DEP_SUB_NO_RATE_OF_INT, getTxtRateOfInterest());
        depSubNoRec.put(FLD_DEP_SUB_NO_TOT_INT_AMT, getTxtTotalInterestAmount());
        depSubNoRec.put(FLD_DEP_SUB_NO_PER_INT_AMT, getTxtPeriodicInterestAmount());
        depSubNoRec.put(FLD_DEP_SUB_NO_DEP_DT, getTdtDateOfDeposit());
        depSubNoRec.put(FLD_DEP_SUB_PAY_DATE, getTdtPaymentDate());
        depSubNoRec.put(FLD_DEP_SUB_NO_DEP_AMT, getTxtDepositAmount());
        depSubNoRec.put(FLD_DEP_SUB_NO_DEP_PER_DD, getTxtPeriodOfDeposit_Days());
        depSubNoRec.put(FLD_DEP_SUB_NO_DEP_PER_MM, getTxtPeriodOfDeposit_Months());
        depSubNoRec.put(FLD_DEP_SUB_NO_DEP_PER_YY, getTxtPeriodOfDeposit_Years());
        depSubNoRec.put(FLD_DEP_SUB_NO_DEP_PER_Wk, getTxtPeriodOfDeposit_Weeks());
        depSubNoRec.put(FLD_DEP_SUB_NO_INT_PAY_FREQ, CommonUtil.convertObjToStr(cbmInterestPaymentFrequency.getKeyForSelected()));
        depSubNoRec.put(FLD_DEP_SUB_NO_INT_PAY_MODE, CommonUtil.convertObjToStr(cbmInterestPaymentMode.getKeyForSelected()));
        depSubNoRec.put(FLD_DEP_SUB_INST_TYPE, CommonUtil.convertObjToStr(cbmInstallmentAmount.getKeyForSelected()));
        //crediting to his customer accounts.....
        String prodType = CommonUtil.convertObjToStr(cbmProdType.getKeyForSelected());
        if (prodType.length() > 0) {
            depSubNoRec.put(FLD_DEP_SUB_INT_PROD_TYPE, CommonUtil.convertObjToStr(cbmProdType.getKeyForSelected()));
            depSubNoRec.put(FLD_DEP_SUB_INT_PROD_ID, CommonUtil.convertObjToStr(cbmProdId.getKeyForSelected()));
            depSubNoRec.put(FLD_DEP_SUB_INT_AC_NO, CommonUtil.convertObjToStr(getCustomerIdCr()));
            depSubNoRec.put(FLD_DEP_SUB_CUST_NAME, CommonUtil.convertObjToStr(getCustomerNameCrValue()));
        }
        if (getRdoCalenderFreq_Yes() == true) {
            depSubNoRec.put(FLD_DEP_SUB_CALENDER_FREQ, "Y");
        } else {
            depSubNoRec.put(FLD_DEP_SUB_CALENDER_FREQ, "N");
        }
        Date depDate = DateUtil.getDateMMDDYYYY(getTdtDateOfDeposit());
        int day = CommonUtil.convertObjToInt(cbmCalenderFreqDay.getKeyForSelected());
        depDate.setDate(day);
        setCalenderFreqDate(String.valueOf(depDate));
        depSubNoRec.put(FLD_DEP_SUB_CALENDER_DAY, String.valueOf(day));
        depSubNoRec.put(FLD_DEP_SUB_CALENDER_DATE, depDate);
        depSubNoRec.put(FLD_DEP_SUB_PAY_TYPE, CommonUtil.convertObjToStr(cbmPaymentType.getKeyForSelected()));
        depSubNoRec.put(FLD_DEP_SUB_NO_STATUS, depSubNoStatus);
        // To represent the data is not in Database
        depSubNoRec.put(FLD_FOR_DB_YES_NO, NO_FULL_STR);
    }

    private void depSubNoCheckValueInsert() {
        depSubNoCheckValues = new HashMap();
        depSubNoCheckValues.put(FLD_DEP_SUB_NO_MAT_DT, getTdtMaturityDate());
        depSubNoCheckValues.put(FLD_DEP_SUB_NO_MAT_AMT, getTxtMaturityAmount());
        depSubNoCheckValues.put(FLD_DEP_SUB_NO_RATE_OF_INT, getTxtRateOfInterest());
        depSubNoCheckValues.put(FLD_DEP_SUB_NO_TOT_INT_AMT, getTxtTotalInterestAmount());
        depSubNoCheckValues.put(FLD_DEP_SUB_NO_PER_INT_AMT, getTxtPeriodicInterestAmount());
        depSubNoCheckValues.put(FLD_DEP_SUB_NO_DEP_DT, getTdtDateOfDeposit());
        depSubNoCheckValues.put(FLD_DEP_SUB_PAY_DATE, getTdtPaymentDate());
        depSubNoCheckValues.put(FLD_DEP_SUB_NO_DEP_AMT, getTxtDepositAmount());
        depSubNoCheckValues.put(FLD_DEP_SUB_NO_DEP_PER_DD, getTxtPeriodOfDeposit_Days());
        depSubNoCheckValues.put(FLD_DEP_SUB_NO_DEP_PER_MM, getTxtPeriodOfDeposit_Months());
        depSubNoCheckValues.put(FLD_DEP_SUB_NO_DEP_PER_YY, getTxtPeriodOfDeposit_Years());
        depSubNoCheckValues.put(FLD_DEP_SUB_NO_DEP_PER_Wk, getTxtPeriodOfDeposit_Weeks());
        depSubNoCheckValues.put(FLD_DEP_SUB_NO_INT_PAY_FREQ, CommonUtil.convertObjToStr(cbmInterestPaymentFrequency.getKeyForSelected()));
        depSubNoCheckValues.put(FLD_DEP_SUB_NO_INT_PAY_MODE, CommonUtil.convertObjToStr(cbmInterestPaymentMode.getKeyForSelected()));
        //crediting to his customer accounts.....
        String prodType = CommonUtil.convertObjToStr(cbmProdType.getKeyForSelected());
        if (prodType.length() > 0) {
            depSubNoCheckValues.put(FLD_DEP_SUB_INT_PROD_TYPE, CommonUtil.convertObjToStr(cbmProdType.getKeyForSelected()));
            depSubNoCheckValues.put(FLD_DEP_SUB_INT_PROD_ID, CommonUtil.convertObjToStr(cbmProdId.getKeyForSelected()));
            depSubNoCheckValues.put(FLD_DEP_SUB_INT_AC_NO, CommonUtil.convertObjToStr(getCustomerIdCr()));
            depSubNoCheckValues.put(FLD_DEP_SUB_CUST_NAME, CommonUtil.convertObjToStr(getCustomerNameCrValue()));
        }
    }

    /**
     * Used to get the Behaves_Like Field from the Deposit_product for the
     * Product we are selecting in the ProdId ComboBox
     */
    public StringBuffer getMandatoryFieldForRD() {
        HashMap whereMap = new HashMap();
        System.out.println("whereMap : "+whereMap+"prodId : "+CommonUtil.convertObjToStr(getCbmProductId().getKeyForSelected()));
//        if(whereMap.containsKey("PROD_ID") && CommonUtil.convertObjToStr(whereMap.get("PROD_ID")).equals(CommonUtil.convertObjToStr(getCbmProductId().getKeyForSelected()))){
//            return strBBehavesLike;
//        }else{
            whereMap.put("PROD_ID", CommonUtil.convertObjToStr(getCbmProductId().getKeyForSelected()));
//        }
        List behavesLike = ClientUtil.executeQuery("getBehavesLike", whereMap);
        if (behavesLike != null && behavesLike.size() > 0) {
            strBBehavesLike = new StringBuffer(CommonUtil.convertObjToStr(((HashMap) behavesLike.get(0)).get("BEHAVES_LIKE")));
        }
        return strBBehavesLike;
    }

    /**
     * Used to get the Behaves_Like Field from the Deposit_product for the
     * Product we are selecting in the ProdId ComboBox
     */
    public StringBuffer getMandatoryFieldForRD(String strWhere) {
        if(strWhere != null && strWhere.length()>0){
//        StringBuffer strBBehavesLike = new StringBuffer();
        HashMap whereMap = new HashMap();
        System.out.println("whereMap : "+whereMap+"prodId : "+CommonUtil.convertObjToStr(getCbmProductId().getKeyForSelected()));
//        if(whereMap.containsKey("PROD_ID") && CommonUtil.convertObjToStr(whereMap.get("PROD_ID")).equals(CommonUtil.convertObjToStr(getCbmProductId().getKeyForSelected()))){
//            return strBBehavesLike;
//        }else{
            whereMap.put("PROD_ID", CommonUtil.convertObjToStr(strWhere));
//        }
        
        List behavesLike = ClientUtil.executeQuery("getBehavesLike", whereMap);
        if (behavesLike != null && behavesLike.size() > 0) {
            strBBehavesLike = new StringBuffer(CommonUtil.convertObjToStr(((HashMap) behavesLike.get(0)).get("BEHAVES_LIKE")));
        }
        }
        return strBBehavesLike;
    }

    private void depSubNoCheckValueRenewalInsert() {
        depSubNoCheckRenewalValues = new HashMap();
        depSubNoCheckRenewalValues.put(FLD_DEP_SUB_NO_MAT_DT, getRenewaltdtMaturityDate());
        depSubNoCheckRenewalValues.put(FLD_DEP_SUB_NO_MAT_AMT, getRenewaltxtMaturityAmount());
        depSubNoCheckRenewalValues.put(FLD_DEP_SUB_NO_RATE_OF_INT, getRenewaltxtRateOfInterest());
        depSubNoCheckRenewalValues.put(FLD_DEP_SUB_NO_TOT_INT_AMT, getRenewaltxtTotalInterestAmount());
        depSubNoCheckRenewalValues.put(FLD_DEP_SUB_NO_PER_INT_AMT, getRenewaltxtPeriodicInterestAmount());
        depSubNoCheckRenewalValues.put(FLD_DEP_SUB_NO_DEP_DT, getRenewaltdtDateOfDeposit());
        //        depSubNoCheckRenewalValues.put(FLD_DEP_SUB_PAY_DATE , getTdtPaymentDate());
        depSubNoCheckRenewalValues.put(FLD_DEP_SUB_NO_DEP_AMT, getRenewaltxtDepositAmount());
        depSubNoCheckRenewalValues.put(FLD_DEP_RENEWAL_SUB_NO_DEP_NO, getRenewalValDepositSubNo());
        depSubNoCheckRenewalValues.put(FLD_DEP_SUB_NO_DEP_PER_DD, getRenewaltxtPeriodOfDeposit_Days());
        depSubNoCheckRenewalValues.put(FLD_DEP_SUB_NO_DEP_PER_MM, getRenewaltxtPeriodOfDeposit_Months());
        depSubNoCheckRenewalValues.put(FLD_DEP_SUB_NO_DEP_PER_YY, getRenewaltxtPeriodOfDeposit_Years());
        depSubNoCheckRenewalValues.put(FLD_DEP_RENEWAL_SUB_NO_INT_PAY_FREQ, CommonUtil.convertObjToStr(cbmRenewalInterestPaymentFrequency.getKeyForSelected()));
        depSubNoCheckRenewalValues.put(FLD_DEP_SUB_NO_INT_PAY_MODE, CommonUtil.convertObjToStr(cbmRenewalInterestPaymentMode.getKeyForSelected()));
        if (getRdoRenewalWithdrawing_Yes() == true) {
            depSubNoCheckRenewalValues.put(FLD_DEP_RENEWAL_SUB_WITHDRAWING_DEPAMT, getTxtRenewalDepTransAmtValue());
        } else if (getRdoRenewalAdding_Yes() == true) {
            depSubNoCheckRenewalValues.put(FLD_DEP_RENEWAL_SUB_ADDING_DEPAMT, getTxtRenewalDepTransAmtValue());
        }
        depSubNoCheckRenewalValues.put(FLD_DEP_RENEWAL_SUB_WITHDRAWING_INTAMT, getTxtRenewalIntAmtValue());
        depSubNoCheckRenewalValues.put(FLD_DEP_RENEWAL_SUB_WITHDRAWING_DEPAMT_TOKENO, getTxtRenewalDepTransTokenNo());
        depSubNoCheckRenewalValues.put(FLD_DEP_RENEWAL_SUB_WITHDRAWING_INTAMT_TOKENO, getTxtRenewalIntTokenNoVal());
        depSubNoCheckRenewalValues.put(FLD_DEP_RENEWAL_SUB_PRODUCT_ID, CommonUtil.convertObjToStr(cbmRenewalDepositProdId.getKeyForSelected()));
        depSubNoCheckRenewalValues.put(FLD_DEP_RENEWAL_SUB_CATEGORY, CommonUtil.convertObjToStr(cbmRenewalDepositCategory.getKeyForSelected()));
        //crediting to his customer accounts.....
        //        String prodType = CommonUtil.convertObjToStr(cbmProdType.getKeyForSelected());
        //        if(prodType.length() >0){
        //            depSubNoCheckRenewalValues.put(FLD_DEP_SUB_INT_PROD_TYPE, CommonUtil.convertObjToStr(cbmRenewalProdType.getKeyForSelected()));
        //            depSubNoCheckRenewalValues.put(FLD_DEP_SUB_INT_PROD_ID, CommonUtil.convertObjToStr(cbmRenewalProdId.getKeyForSelected()));
        //            depSubNoCheckRenewalValues.put(FLD_DEP_SUB_INT_AC_NO, CommonUtil.convertObjToStr(getCustomerIdCr()));
        //            depSubNoCheckRenewalValues.put(FLD_DEP_SUB_CUST_NAME, CommonUtil.convertObjToStr(getCustomerNameCrValue()));
        //        }
    }

    /**
     * Sets the CustomerId ComboBoxModel in PoA screen
     *
     * @param keys ---> ArrayList having keys
     * @param values ---> ArrayList having values
     */
    public void fillCustomerName(ArrayList keys, ArrayList values) {
        cbmOnBehalfOf = new ComboBoxModel(keys, values);
        cbmNominatedBy = new ComboBoxModel(keys, values);
        setCbmOnBehalfOf(cbmOnBehalfOf);
        setCbmNominatedBy(cbmNominatedBy);
    }

    /**
     * Calculates the Maturity Date and returns it.
     *
     * @return Maturity Date - The calculated Maturity Date is returned.
     */
    public String getMaturityDateArrayList() {
        int tblDepSubNoAccInfoRow = tblDepSubNoAccInfo.getRowCount();
        String maturityDate = new String();
        Date leastDate = currDt;
        ArrayList dateArrayList = new ArrayList();
        if (tblDepSubNoAccInfoRow >= 2) {
            for (int i = 0; i < tblDepSubNoAccInfoRow; i++) {
                dateArrayList.add(CommonUtil.convertObjToStr(((HashMap) depSubNoAll.get(String.valueOf(i))).get(FLD_DEP_SUB_NO_MAT_DT)));
            }
            leastDate = compareDateLeastOne(dateArrayList);
        } else {
            leastDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(((HashMap) depSubNoAll.get(String.valueOf(0))).get(FLD_DEP_SUB_NO_MAT_DT)));
        }
        maturityDate = DateUtil.getStringDate(leastDate);
        return maturityDate;

    }

    /**
     * Compares the Dates and returns the Least Date of all these.
     *
     * @param dateArrayList ArrayList containing the Dates to be compared
     * @return Least Date - The Least Date is returned.
     */
    public Date compareDateLeastOne(ArrayList dateArrayList) {
        Date leastDate = currDt;
        leastDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(dateArrayList.get(0)));
        for (int i = 0; i < dateArrayList.size(); i++) {
            if (leastDate.before(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(dateArrayList.get(1))))) {
                leastDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(dateArrayList.get(1)));
            }
        }
        return leastDate;
    }

    public HashMap setRenewalAmountsAccROI(HashMap detailsHash, HashMap param) {
        //system.out.println("########setAmountsAccROI : "+detailsHash );
        HashMap amtDetHash = new HashMap();
        long period = 0;
        long cummPeriod = 0;
        long cummMonth = 0;
        double totalAmt = 0.0;
        if (param == null) {
            if (detailsHash.get("BEHAVES_LIKE").equals("FIXED")) {
                if (detailsHash.get("INTEREST_TYPE").equals("MONTHLY")) {
                    period = Integer.parseInt(getRenewaltxtPeriodOfDeposit_Months()) * 30;
                    period = period + Integer.parseInt(getRenewaltxtPeriodOfDeposit_Years()) * 360;
                } else {
                    Date startDt = null;
                    Date endDt = null;
                    if (detailsHash.containsKey("DEPOSIT_DT") && detailsHash.containsKey("MATURITY_DT")) {
                        startDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(detailsHash.get("DEPOSIT_DT")));
                        endDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(detailsHash.get("MATURITY_DT")));
                    } else {
                        startDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(renewaltdtDateOfDeposit));
                        endDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(renewaltdtMaturityDate));
                    }
                    period = DateUtil.dateDiff(startDt, endDt);
                    int count = 0;
                    while (DateUtil.dateDiff(startDt, endDt) > 0) {
                        int month = startDt.getMonth();
                        int startYear = startDt.getYear() + 1900;
                        if (month == 1 && startYear % 4 == 0) {
                            count++;
                        }
                        startDt = DateUtil.addDays(startDt, 30);
                    }
                    period -= count;
                }
            } else if (detailsHash.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
                if ((getRenewaltxtPeriodOfDeposit_Days() != null) && (!getRenewaltxtPeriodOfDeposit_Days().equals(""))) {
                    period = period + Integer.parseInt(getRenewaltxtPeriodOfDeposit_Days());
                    cummPeriod = period;
                }
                if ((getRenewaltxtPeriodOfDeposit_Months() != null) && (!getRenewaltxtPeriodOfDeposit_Months().equals(""))) {
                    period = period + Integer.parseInt(getRenewaltxtPeriodOfDeposit_Months()) * 30;
                    cummMonth = Integer.parseInt(getRenewaltxtPeriodOfDeposit_Months());
                }
                if ((getRenewaltxtPeriodOfDeposit_Years() != null) && (!getRenewaltxtPeriodOfDeposit_Years().equals(""))) {
                    period = period + Integer.parseInt(getRenewaltxtPeriodOfDeposit_Years()) * 360;
                }
                long fullPeriod = 0;
                fullPeriod = period;
                double simpleAmt = 0.0;
                double completeAmt = 0.0;
                cummPeriod = cummPeriod % 30;
                cummMonth = cummMonth % 3;
                if (cummMonth == 0) {
                    //system.out.println("******** cummPeriod == 0: "+cummPeriod);
                }
                if (cummPeriod > 0 || cummMonth > 0) {
                    cummMonth = cummMonth * 30;
                    cummPeriod = cummPeriod + cummMonth;
                    //system.out.println("******** cummPeriod != 0: "+cummPeriod);
                }
                if (fullPeriod > 0) {
                    period = fullPeriod - cummPeriod;
                    detailsHash.put("BEHAVES_LIKE", "CUMMULATIVE");
                    detailsHash.put("PEROID", String.valueOf(period));
                    detailsHash.put("CATEGORY_ID", cbmRenewalDepositCategory.getKeyForSelected());
                    detailsHash.put("PROD_ID", cbmRenewalDepositProdId.getKeyForSelected());
                    List list = (List) ClientUtil.executeQuery("getDepProdIntPay", detailsHash);
                    if (list != null && list.size() > 0) {
                        detailsHash.putAll((HashMap) list.get(0));
                        InterestCalc interestCalc = new InterestCalc();
                        amtDetHash = interestCalc.calcAmtDetails(detailsHash);
                        completeAmt = CommonUtil.convertObjToDouble(amtDetHash.get("INTEREST")).doubleValue();
                    }
                    detailsHash.put("AMOUNT", amtDetHash.get("AMOUNT"));
                    detailsHash.remove("PEROID");
                    int yearPer = CommonUtil.convertObjToInt(getRenewaltxtPeriodOfDeposit_Years());
                    yearPer = yearPer * 12;
                    int monthPer = CommonUtil.convertObjToInt(getRenewaltxtPeriodOfDeposit_Months());
                    monthPer = (monthPer + yearPer) / 3;
                    int totMonth = monthPer * 3;
                    int tot = 0;
                    Date endDt = null;//commented bcz 2,3 days showing wrongly...
                    //                    Date startDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(renewaltdtDateOfDeposit));
                    //                    if(monthPer >=1 ||cummPeriod>0){
                    //                        int yearTobeAdded = 1900;
                    //                        int totYr = 0;
                    //                        int bal = 0;
                    //                        int day = 0;
                    //                        monthPer = CommonUtil.convertObjToInt(getRenewaltxtPeriodOfDeposit_Months());
                    //                        int month = 0;
                    //                        int year = 0;
                    //                        if(totMonth>=12){
                    //                            totYr = totMonth /12;
                    //                            year = year +totYr;
                    //                        }else{
                    //                            bal = totMonth;
                    //                        }
                    //                        if(totYr>=1){
                    //                            totYr = totYr *12;
                    //                            bal = totMonth - totYr;
                    //                        }
                    //                        GregorianCalendar cal = new GregorianCalendar((startDt.getYear()+yearTobeAdded),startDt.getMonth(),startDt.getDate());
                    //                        cal.add(GregorianCalendar.YEAR, year);
                    //                        cal.add(GregorianCalendar.MONTH, bal);
                    //                        cal.add(GregorianCalendar.DAY_OF_MONTH, day);
                    //                        String depDt =DateUtil.getStringDate(cal.getTime());
                    //                        Date date = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depDt));
                    //                        endDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(renewaltdtMaturityDate));
                    //                        period = DateUtil.dateDiff(date, endDt);
                    //                    }
                }
                if (cummPeriod > 0) {
                    detailsHash.put("BEHAVES_LIKE", "FIXED");
                    detailsHash.put("PEROID", String.valueOf(cummPeriod));
                    detailsHash.put("CATEGORY_ID", getCbmRenewalDepositCategory().getKeyForSelected());
//                    detailsHash.put("CATEGORY_ID", cbmRenewalDepositCategory.getKeyForSelected());
//                    detailsHash.put("PROD_ID", cbmRenewalDepositProdId.getKeyForSelected());
                    detailsHash.put("PROD_ID", getCbmRenewalDepositProdId().getKeyForSelected());
                    List list = (List) ClientUtil.executeQuery("getDepProdIntPay", detailsHash);
                    if (list != null && list.size() > 0) {
                        detailsHash.putAll((HashMap) list.get(0));
                        InterestCalc interestCalc = new InterestCalc();
                        detailsHash.put("INTEREST_TYPE", "YEARLY");
                        detailsHash.put("CALC_OPENING_MODE", "CALC_OPENING_MODE");
                        amtDetHash = interestCalc.calcAmtDetails(detailsHash);
                        simpleAmt = CommonUtil.convertObjToDouble(amtDetHash.get("INTEREST")).doubleValue();
                    }
                }
                detailsHash.put("BEHAVES_LIKE", "CUMMULATIVE");
                double interest = simpleAmt + completeAmt;
                amtDetHash.put("INTEREST", new Double(interest));
                //system.out.println("******** : "+detailsHash);
            } else {
                detailsHash.put("PEROID", String.valueOf(period));
            }
            //system.out.println(" set Amount ROI rate of interest param"+detailsHash);
        }
        if (!detailsHash.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
            //--- If the param is NUll then put category and Prod ,else put all the param value to the detailHash
            detailsHash.put("PEROID", String.valueOf(period));
            if (param == null) {
                detailsHash.put("CATEGORY_ID", cbmRenewalDepositCategory.getKeyForSelected());
                detailsHash.put("PROD_ID", cbmRenewalDepositProdId.getKeyForSelected());
            } else {
                detailsHash.putAll(param);
            }
            HashMap discountedMap = new HashMap();
            discountedMap.put("PROD_ID", detailsHash.get("PROD_ID"));
            List lstDiscounted = ClientUtil.executeQuery("getDepProdDetails", discountedMap);
            if (lstDiscounted != null && lstDiscounted.size() > 0) {
                discountedMap = (HashMap) lstDiscounted.get(0);
                detailsHash.put("DISCOUNTED_RATE", discountedMap.get("DISCOUNTED_RATE"));
            }
            List list = (List) ClientUtil.executeQuery("getDepProdIntPay", detailsHash);
            if (list != null && list.size() > 0) {
                detailsHash.putAll((HashMap) list.get(0));
                InterestCalc interestCalc = new InterestCalc();
                amtDetHash = interestCalc.calcAmtDetails(detailsHash);
            }
        }
        return amtDetHash;
    }

    /**
     * Resets the Period of deposit and the Maturity Date
     */
    public void resetDateofTransfer() {
        setTxtPeriodOfDeposit_Years("");
        setTxtPeriodOfDeposit_Months("");
        setTxtPeriodOfDeposit_Days("");
        setTdtMaturityDate("");
        notifyObservers();
    }

    /**
     * Executes the query and sets the result in "ProductBehavesLike" and
     * "ProductInterestType".
     */
    public void getProductBehaveLike(String param) {
        final HashMap whereMap = new HashMap();
        if (param == null) {
            whereMap.put("PROD_ID", CommonUtil.convertObjToStr(cbmProductId.getKeyForSelected()));
        } else {
            whereMap.put("PROD_ID", param);
        }
        final List resultList = ClientUtil.executeQuery("getProductBehavesLike", whereMap);
        HashMap resultProductBehavesLike = (HashMap) resultList.get(0);
        productBehavesLike = CommonUtil.convertObjToStr(resultProductBehavesLike.get("BEHAVES_LIKE"));
        productInterestType = CommonUtil.convertObjToStr(resultProductBehavesLike.get("INT_TYPE"));
        setBehavesLike(productBehavesLike);
    }

    public HashMap depositRateInterestCalculation(HashMap calculationMap, String prodId) {
        //system.out.println("Simple Calculation : "+calculationMap);
        double rateOfInt = 0.0;
        double renewalRate = 0.0;
        double maturityRate = 0.0;
        double principal = 0.0;
        double sbCalcAmt = 0.0;
        HashMap renewMap = (HashMap) calculationMap.get("RENEWAL_CALCULATION");
        Date depositDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(calculationMap.get("DEPOSIT_DT")));
        Date maturityDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(calculationMap.get("TO_DAY_DT")));
        double period = DateUtil.dateDiff(depositDate, maturityDate);
        if (period + 1 > backDateFreq) {
            List lstRenewalInt = null;
            List lstMaturityInt = null;
            HashMap renewalMap = new HashMap();
            renewalMap.put("PRODUCT_TYPE", "TD");
            renewalMap.put("PROD_ID", prodId);
            renewalMap.put("CATEGORY_ID", category);
            renewalMap.put("AMOUNT", CommonUtil.convertObjToDouble(calculationMap.get("DEPOSIT_AMT")));
            renewalMap.put("PERIOD", calculationMap.get("PERIOD"));
            renewalMap.put("DEPOSIT_DT", maturityDate);
            lstRenewalInt = (List) ClientUtil.executeQuery("icm.getInterestRates", renewalMap);
            if (lstRenewalInt != null && lstRenewalInt.size() > 0) {
                renewalMap = (HashMap) lstRenewalInt.get(0);
                renewalRate = CommonUtil.convertObjToDouble(renewalMap.get("ROI")).doubleValue();
            }
            HashMap maturityMap = new HashMap();
            maturityMap.put("PRODUCT_TYPE", "TD");
            maturityMap.put("PROD_ID", prodId);
            maturityMap.put("CATEGORY_ID", category);
            maturityMap.put("AMOUNT", CommonUtil.convertObjToDouble(calculationMap.get("DEPOSIT_AMT")));
            maturityMap.put("PERIOD", CommonUtil.convertObjToLong(calculationMap.get("PERIOD")));
            maturityMap.put("DEPOSIT_DT", depositDate);
            lstMaturityInt = (List) ClientUtil.executeQuery("icm.getInterestRates", maturityMap);
            if (lstMaturityInt != null && lstMaturityInt.size() > 0) {
                maturityMap = (HashMap) lstMaturityInt.get(0);
                maturityRate = CommonUtil.convertObjToDouble(maturityMap.get("ROI")).doubleValue();
            }
            if (lstRenewalInt != null && lstRenewalInt.size() > 0 && lstMaturityInt != null && lstMaturityInt.size() > 0) {
                if (renewMap.get("DATE_OF_MATURITY") != null && renewMap.get("DATE_OF_MATURITY").equals("Y") && maturityRate > 0) {
                    rateOfInt = maturityRate;
                } else if (renewMap.get("DATE_OF_RENEWAL") != null && renewMap.get("DATE_OF_RENEWAL").equals("Y") && renewalRate > 0) {
                    rateOfInt = renewalRate;
                } else if (renewMap.get("ELIGIBLE_TWO_RATE") != null && renewMap.get("ELIGIBLE_TWO_RATE").equals("N")) {
                    if (renewalRate <= maturityRate) {
                        rateOfInt = renewalRate;
                    } else {
                        rateOfInt = maturityRate;
                    }
                } else if (renewMap.get("ELIGIBLE_TWO_RATE") != null && renewMap.get("ELIGIBLE_TWO_RATE").equals("Y")) {
                    if (renewalRate <= maturityRate) {
                        rateOfInt = maturityRate;
                    } else {
                        rateOfInt = renewalRate;
                    }
                }
                if (rateOfInt > 0) {
                    setRenewalSBIntRateVal(String.valueOf(rateOfInt));
                    principal = CommonUtil.convertObjToDouble(calculationMap.get("DEPOSIT_AMT")).doubleValue();
                    sbCalcAmt = principal + (principal * rateOfInt * period) / (36500);
                    sbCalcAmt = sbCalcAmt - principal;
                    sbCalcAmt = (double) getNearest((long) (sbCalcAmt * 100), 100) / 100;
                    calculationMap.put("CALC_AMT", new Double(sbCalcAmt));
                    setSbIntAmount(sbCalcAmt);
                    setSbPeriodRun(period);
                }
            } else if ((lstRenewalInt != null && lstRenewalInt.size() > 0) || (lstMaturityInt != null && lstMaturityInt.size() > 0)) {
                //                boolean availableRate = true;
                //                boolean maturityRateFlag = true;
                //                double availRenealRate = 0.0;
                //                double availmaturityRate = 0.0;
                //                boolean renewalRateFlag = true;
                //                if(renewMap.get("DATE_OF_MATURITY") != null && renewMap.get("DATE_OF_MATURITY").equals("Y")){
                //                    availableRate = true;
                //                    if(maturityRate>0){
                //                        availmaturityRate = maturityRate;
                ////                    }else if(renewalRate>0){
                ////                        availmaturityRate = renewalRate;
                //                    }
                //                }else if(renewMap.get("DATE_OF_RENEWAL") != null && renewMap.get("DATE_OF_RENEWAL").equals("Y")){
                ////                    availableRate = true;
                //                    if(renewalRate>0){
                //                        availRenealRate = renewalRate;
                ////                    }else if(maturityRate>0){
                ////                        availRenealRate = maturityRate;
                //                    }
                //                }else {
                ////                    availableRate = true;
                //                    if(renewalRate>0){
                //                        rateOfInt = renewalRate;
                //                    }else if(maturityRate>0){
                //                        rateOfInt = maturityRate;
                //                    }
                //                }

                if (renewalRate > 0 && renewMap.get("DATE_OF_RENEWAL") != null && renewMap.get("DATE_OF_RENEWAL").equals("Y")) {//incase renewal rate is avail and parameter is Y
                    rateOfInt = renewalRate;
                    setRenewalSBIntRateVal(String.valueOf(rateOfInt));
                    principal = CommonUtil.convertObjToDouble(calculationMap.get("DEPOSIT_AMT")).doubleValue();
                    sbCalcAmt = principal + (principal * rateOfInt * period) / (36500);
                    sbCalcAmt = sbCalcAmt - principal;
                    sbCalcAmt = (double) getNearest((long) (sbCalcAmt * 100), 100) / 100;
                    calculationMap.put("CALC_AMT", new Double(sbCalcAmt));
                } else if (maturityRate > 0 && renewMap.get("DATE_OF_MATURITY") != null && renewMap.get("DATE_OF_MATURITY").equals("Y")) {//incase maturity rate is avail and parameter is Y
                    rateOfInt = maturityRate;
                    setRenewalSBIntRateVal(String.valueOf(rateOfInt));
                    principal = CommonUtil.convertObjToDouble(calculationMap.get("DEPOSIT_AMT")).doubleValue();
                    sbCalcAmt = principal + (principal * rateOfInt * period) / (36500);
                    sbCalcAmt = sbCalcAmt - principal;
                    sbCalcAmt = (double) getNearest((long) (sbCalcAmt * 100), 100) / 100;
                    calculationMap.put("CALC_AMT", new Double(sbCalcAmt));
                } else if (renewMap.get("ONE_RATE_AVAIL") != null && renewMap.get("ONE_RATE_AVAIL").equals("Y")) {// above both condition fails this one will check
                    if (renewalRate > 0) {
                        rateOfInt = renewalRate;
                    } else if (maturityRate > 0) {
                        rateOfInt = maturityRate;
                    }
                    setRenewalSBIntRateVal(String.valueOf(rateOfInt));
                    principal = CommonUtil.convertObjToDouble(calculationMap.get("DEPOSIT_AMT")).doubleValue();
                    sbCalcAmt = principal + (principal * rateOfInt * period) / (36500);
                    sbCalcAmt = sbCalcAmt - principal;
                    sbCalcAmt = (double) getNearest((long) (sbCalcAmt * 100), 100) / 100;
                    calculationMap.put("CALC_AMT", new Double(sbCalcAmt));
                } else if (renewMap.get("ONE_RATE_AVAIL") != null && renewMap.get("ONE_RATE_AVAIL").equals("N")) {// simple inerest calculation will do...
                    simpleInterestCalculation(calculationMap);
                    rateOfInt = sbCalcAmt;
                }
                setSbIntAmount(rateOfInt);
                setSbPeriodRun(period);
            } else {
                if (renewMap.get("BOTH_RATE_NOT_AVAIL") != null && renewMap.get("BOTH_RATE_NOT_AVAIL").equals("Y")) {
                    simpleInterestCalculation(calculationMap);
                } else {
                    setSbIntAmount(sbCalcAmt);
                    setSbPeriodRun(period);
                }
            }
        }
        return calculationMap;
    }

    /**
     * Getter for property _authorizeMap.
     *
     * @return Value of property _authorizeMap.
     */
    public HashMap getAuthorizeMap() {
        return _authorizeMap;
    }

    /**
     * Setter for property _authorizeMap.
     *
     * @param _authorizeMap New value of property _authorizeMap.
     */
    public void setAuthorizeMap(HashMap _authorizeMap) {
        this._authorizeMap = _authorizeMap;
    }

    /**
     * Getter for property lblValRenewDep.
     *
     * @return Value of property lblValRenewDep.
     */
    public java.lang.String getLblValRenewDep() {
        return lblValRenewDep;
    }

    /**
     * Setter for property lblValRenewDep.
     *
     * @param lblValRenewDep New value of property lblValRenewDep.
     */
    public void setLblValRenewDep(java.lang.String lblValRenewDep) {
        this.lblValRenewDep = lblValRenewDep;
    }

    public void setBehavesLike(String productBehavesLike) {
        this.productBehavesLike = productBehavesLike;
    }

    public String getBehavesLike() {
        return productBehavesLike;
    }

    /**
     * Getter for property authorizedSignatoryOB.
     *
     * @return Value of property authorizedSignatoryOB.
     */
    public com.see.truetransact.ui.common.authorizedsignatory.AuthorizedSignatoryOB getAuthorizedSignatoryOB() {
        return authorizedSignatoryOB;
    }

    /**
     * Setter for property authorizedSignatoryOB.
     *
     * @param authorizedSignatoryOB New value of property authorizedSignatoryOB.
     */
    public void setAuthorizedSignatoryOB(com.see.truetransact.ui.common.authorizedsignatory.AuthorizedSignatoryOB authorizedSignatoryOB) {
        this.authorizedSignatoryOB = authorizedSignatoryOB;
    }

    /**
     * Getter for property powerOfAttorneyOB.
     *
     * @return Value of property powerOfAttorneyOB.
     */
    public com.see.truetransact.ui.common.powerofattorney.PowerOfAttorneyOB getPowerOfAttorneyOB() {
        return powerOfAttorneyOB;
    }

    /**
     * Setter for property powerOfAttorneyOB.
     *
     * @param powerOfAttorneyOB New value of property powerOfAttorneyOB.
     */
    public void setPowerOfAttorneyOB(com.see.truetransact.ui.common.powerofattorney.PowerOfAttorneyOB powerOfAttorneyOB) {
        this.powerOfAttorneyOB = powerOfAttorneyOB;
    }

    /**
     * Getter for property txtAgentId.
     *
     * @return Value of property txtAgentId.
     */
    public java.lang.String getTxtAgentId() {
        return txtAgentId;
    }

    /**
     * Setter for property txtAgentId.
     *
     * @param txtAgentId New value of property txtAgentId.
     */
    public void setTxtAgentId(java.lang.String txtAgentId) {
        this.txtAgentId = txtAgentId;
    }

    /**
     * Getter for property lblValAgentname.
     *
     * @return Value of property lblValAgentname.
     */
    public java.lang.String getLblValAgentname() {
        return lblValAgentname;
    }

    /**
     * Setter for property lblValAgentname.
     *
     * @param lblValAgentname New value of property lblValAgentname.
     */
    public void setLblValAgentname(java.lang.String lblValAgentname) {
        this.lblValAgentname = lblValAgentname;
    }

    /**
     * Getter for property authorizedSignatoryInstructionOB.
     *
     * @return Value of property authorizedSignatoryInstructionOB.
     */
    public com.see.truetransact.ui.common.authorizedsignatory.AuthorizedSignatoryInstructionOB getAuthorizedSignatoryInstructionOB() {
        return authorizedSignatoryInstructionOB;
    }

    /**
     * Setter for property authorizedSignatoryInstructionOB.
     *
     * @param authorizedSignatoryInstructionOB New value of property
     * authorizedSignatoryInstructionOB.
     */
    public void setAuthorizedSignatoryInstructionOB(com.see.truetransact.ui.common.authorizedsignatory.AuthorizedSignatoryInstructionOB authorizedSignatoryInstructionOB) {
        this.authorizedSignatoryInstructionOB = authorizedSignatoryInstructionOB;
    }

    /**
     * to get the Total deposit Amount
     *
     */
    protected double getdepositAmount() {
       int depSubNoAllSize = 0;
        if(depSubNoAll!=null)
         depSubNoAllSize = depSubNoAll.size();
        double retDepAmt = 0;
        //--- adds the depsoit amount
        for (int i = 0; i < depSubNoAllSize; i++) {
            HashMap hah = (HashMap) depSubNoAll.get(String.valueOf(i));
            //system.out.println("geetttdeppooodfgzdg Akhilll"+hah.get("Status")+"  "+hah.get("DepAmt"));
            //--- If it is not deleted, add to the deposit amount
            if (hah.containsKey("Status") && !CommonUtil.convertObjToStr(hah.get("Status")).equals("") && CommonUtil.convertObjToStr(hah.get("Status")) != null && 
            CommonUtil.convertObjToStr(hah.get("Status")).length()>0 && !CommonUtil.convertObjToStr(hah.get("Status")).equals("DELETED")) {
                retDepAmt = retDepAmt + Double.parseDouble(CommonUtil.convertObjToStr(hah.get("DepAmt")));
            }
        }
        return retDepAmt;
    }

    /**
     * Getter for property transMode.
     *
     * @return Value of property transMode.
     */
    public java.lang.String getTransMode() {
        return transMode;
    }

    /**
     * Setter for property transMode.
     *
     * @param transMode New value of property transMode.
     */
    public void setTransMode(java.lang.String transMode) {
        this.transMode = transMode;
    }

    /**
     * Getter for property transactionId.
     *
     * @return Value of property transactionId.
     */
    public java.lang.String getTransactionId() {
        return transactionId;
    }

    /**
     * Setter for property transactionId.
     *
     * @param transactionId New value of property transactionId.
     */
    public void setTransactionId(java.lang.String transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * Getter for property cashId.
     *
     * @return Value of property cashId.
     */
    public java.lang.String getCashId() {
        return cashId;
    }

    /**
     * Setter for property cashId.
     *
     * @param cashId New value of property cashId.
     */
    public void setCashId(java.lang.String cashId) {
        this.cashId = cashId;
    }

    /**
     * Getter for property sbIntAmount.
     *
     * @return Value of property sbIntAmount.
     */
    public double getSbIntAmount() {
        return sbIntAmount;
    }

    /**
     * Setter for property sbIntAmount.
     *
     * @param sbIntAmount New value of property sbIntAmount.
     */
    public void setSbIntAmount(double sbIntAmount) {
        this.sbIntAmount = sbIntAmount;
    }

    /**
     * Getter for property availableBalanceValue.
     *
     * @return Value of property availableBalanceValue.
     */
    public java.lang.String getAvailableBalanceValue() {
        return availableBalanceValue;
    }

    /**
     * Setter for property availableBalanceValue.
     *
     * @param availableBalanceValue New value of property availableBalanceValue.
     */
    public void setAvailableBalanceValue(java.lang.String availableBalanceValue) {
        this.availableBalanceValue = availableBalanceValue;
    }

    /**
     * Getter for property clearBalanceValue.
     *
     * @return Value of property clearBalanceValue.
     */
    public java.lang.String getClearBalanceValue() {
        return clearBalanceValue;
    }

    /**
     * Setter for property clearBalanceValue.
     *
     * @param clearBalanceValue New value of property clearBalanceValue.
     */
    public void setClearBalanceValue(java.lang.String clearBalanceValue) {
        this.clearBalanceValue = clearBalanceValue;
    }

    /**
     * Getter for property totalBalanceValue.
     *
     * @return Value of property totalBalanceValue.
     */
    public java.lang.String getTotalBalanceValue() {
        return totalBalanceValue;
    }

    /**
     * Setter for property totalBalanceValue.
     *
     * @param totalBalanceValue New value of property totalBalanceValue.
     */
    public void setTotalBalanceValue(java.lang.String totalBalanceValue) {
        this.totalBalanceValue = totalBalanceValue;
    }

    /**
     * Getter for property totalInterestValue.
     *
     * @return Value of property totalInterestValue.
     */
    public java.lang.String getTotalInterestValue() {
        return totalInterestValue;
    }

    /**
     * Setter for property totalInterestValue.
     *
     * @param totalInterestValue New value of property totalInterestValue.
     */
    public void setTotalInterestValue(java.lang.String totalInterestValue) {
        this.totalInterestValue = totalInterestValue;
    }

    /**
     * Getter for property lastInterestPaidDateValue.
     *
     * @return Value of property lastInterestPaidDateValue.
     */
    public java.lang.String getLastInterestPaidDateValue() {
        return lastInterestPaidDateValue;
    }

    /**
     * Setter for property lastInterestPaidDateValue.
     *
     * @param lastInterestPaidDateValue New value of property
     * lastInterestPaidDateValue.
     */
    public void setLastInterestPaidDateValue(java.lang.String lastInterestPaidDateValue) {
        this.lastInterestPaidDateValue = lastInterestPaidDateValue;
    }

    /**
     * Getter for property paidnterestAmountValue.
     *
     * @return Value of property paidnterestAmountValue.
     */
    public java.lang.String getPaidnterestAmountValue() {
        return paidnterestAmountValue;
    }

    /**
     * Setter for property paidnterestAmountValue.
     *
     * @param paidnterestAmountValue New value of property
     * paidnterestAmountValue.
     */
    public void setPaidnterestAmountValue(java.lang.String paidnterestAmountValue) {
        this.paidnterestAmountValue = paidnterestAmountValue;
    }

    /**
     * Getter for property balanceInterestAmountValue.
     *
     * @return Value of property balanceInterestAmountValue.
     */
    public java.lang.String getBalanceInterestAmountValue() {
        return balanceInterestAmountValue;
    }

    /**
     * Setter for property balanceInterestAmountValue.
     *
     * @param balanceInterestAmountValue New value of property
     * balanceInterestAmountValue.
     */
    public void setBalanceInterestAmountValue(java.lang.String balanceInterestAmountValue) {
        this.balanceInterestAmountValue = balanceInterestAmountValue;
    }

    /**
     * Getter for property depositLienNoValue.
     *
     * @return Value of property depositLienNoValue.
     */
    public java.lang.String getDepositLienNoValue() {
        return depositLienNoValue;
    }

    /**
     * Setter for property depositLienNoValue.
     *
     * @param depositLienNoValue New value of property depositLienNoValue.
     */
    public void setDepositLienNoValue(java.lang.String depositLienNoValue) {
        this.depositLienNoValue = depositLienNoValue;
    }

    /**
     * Getter for property lienAmountValue.
     *
     * @return Value of property lienAmountValue.
     */
    public java.lang.String getLienAmountValue() {
        return lienAmountValue;
    }

    /**
     * Setter for property lienAmountValue.
     *
     * @param lienAmountValue New value of property lienAmountValue.
     */
    public void setLienAmountValue(java.lang.String lienAmountValue) {
        this.lienAmountValue = lienAmountValue;
    }

    /**
     * Getter for property lienDateValue.
     *
     * @return Value of property lienDateValue.
     */
    public java.lang.String getLienDateValue() {
        return lienDateValue;
    }

    /**
     * Setter for property lienDateValue.
     *
     * @param lienDateValue New value of property lienDateValue.
     */
    public void setLienDateValue(java.lang.String lienDateValue) {
        this.lienDateValue = lienDateValue;
    }

    /**
     * Getter for property loanNoValue.
     *
     * @return Value of property loanNoValue.
     */
    public java.lang.String getLoanNoValue() {
        return loanNoValue;
    }

    /**
     * Setter for property loanNoValue.
     *
     * @param loanNoValue New value of property loanNoValue.
     */
    public void setLoanNoValue(java.lang.String loanNoValue) {
        this.loanNoValue = loanNoValue;
    }

    /**
     * Getter for property loanBalanceValue.
     *
     * @return Value of property loanBalanceValue.
     */
    public java.lang.String getLoanBalanceValue() {
        return loanBalanceValue;
    }

    /**
     * Setter for property loanBalanceValue.
     *
     * @param loanBalanceValue New value of property loanBalanceValue.
     */
    public void setLoanBalanceValue(java.lang.String loanBalanceValue) {
        this.loanBalanceValue = loanBalanceValue;
    }

    /**
     * Getter for property loanTakenDateValue.
     *
     * @return Value of property loanTakenDateValue.
     */
    public java.lang.String getLoanTakenDateValue() {
        return loanTakenDateValue;
    }

    /**
     * Setter for property loanTakenDateValue.
     *
     * @param loanTakenDateValue New value of property loanTakenDateValue.
     */
    public void setLoanTakenDateValue(java.lang.String loanTakenDateValue) {
        this.loanTakenDateValue = loanTakenDateValue;
    }

    /**
     * Getter for property cbmProdType.
     *
     * @return Value of property cbmProdType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdType() {
        return cbmProdType;
    }

    /**
     * Setter for property cbmProdType.
     *
     * @param cbmProdType New value of property cbmProdType.
     */
    public void setCbmProdType(com.see.truetransact.clientutil.ComboBoxModel cbmProdType) {
        this.cbmProdType = cbmProdType;
    }

    /**
     * Getter for property cbmProdId.
     *
     * @return Value of property cbmProdId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdId() {
        return cbmProdId;
    }

    /**
     * Setter for property cbmProdId.
     *
     * @param cbmProdId New value of property cbmProdId.
     */
    public void setCbmProdId(com.see.truetransact.clientutil.ComboBoxModel cbmProdId) {
        this.cbmProdId = cbmProdId;
    }

    /**
     * Getter for property cboProdId.
     *
     * @return Value of property cboProdId.
     */
    public java.lang.String getCboProdId() {
        return cboProdId;
    }

    /**
     * Setter for property cboProdId.
     *
     * @param cboProdId New value of property cboProdId.
     */
    public void setCboProdId(java.lang.String cboProdId) {
        this.cboProdId = cboProdId;
    }

    /**
     * Getter for property cboProdType.
     *
     * @return Value of property cboProdType.
     */
    public java.lang.String getCboProdType() {
        return cboProdType;
    }

    /**
     * Setter for property cboProdType.
     *
     * @param cboProdType New value of property cboProdType.
     */
    public void setCboProdType(java.lang.String cboProdType) {
        this.cboProdType = cboProdType;
    }

    /**
     * Getter for property customerNameCrValue.
     *
     * @return Value of property customerNameCrValue.
     */
    public java.lang.String getCustomerNameCrValue() {
        return customerNameCrValue;
    }

    /**
     * Setter for property customerNameCrValue.
     *
     * @param customerNameCrValue New value of property customerNameCrValue.
     */
    public void setCustomerNameCrValue(java.lang.String customerNameCrValue) {
        this.customerNameCrValue = customerNameCrValue;
    }

    /**
     * Getter for property customerIdCr.
     *
     * @return Value of property customerIdCr.
     */
    public java.lang.String getCustomerIdCr() {
        return customerIdCr;
    }

    /**
     * Setter for property customerIdCr.
     *
     * @param customerIdCr New value of property customerIdCr.
     */
    public void setCustomerIdCr(java.lang.String customerIdCr) {
        this.customerIdCr = customerIdCr;
    }

    /**
     * Getter for property productTypes.
     *
     * @return Value of property productTypes.
     */
    public java.lang.String getProductTypes() {
        return productTypes;
    }

    /**
     * Setter for property productTypes.
     *
     * @param productTypes New value of property productTypes.
     */
    public void setProductTypes(java.lang.String productTypes) {
        this.productTypes = productTypes;
    }

    /**
     * Getter for property totalInterestPayableValue.
     *
     * @return Value of property totalInterestPayableValue.
     */
    public java.lang.String getTotalInterestPayableValue() {
        return totalInterestPayableValue;
    }

    /**
     * Setter for property totalInterestPayableValue.
     *
     * @param totalInterestPayableValue New value of property
     * totalInterestPayableValue.
     */
    public void setTotalInterestPayableValue(java.lang.String totalInterestPayableValue) {
        this.totalInterestPayableValue = totalInterestPayableValue;
    }

    /**
     * Getter for property accHeadValue.
     *
     * @return Value of property accHeadValue.
     */
    public java.lang.String getAccHeadValue() {
        return accHeadValue;
    }

    /**
     * Setter for property accHeadValue.
     *
     * @param accHeadValue New value of property accHeadValue.
     */
    public void setAccHeadValue(java.lang.String accHeadValue) {
        this.accHeadValue = accHeadValue;
    }

    /**
     * Getter for property amtReduce.
     *
     * @return Value of property amtReduce.
     */
    public java.lang.String getAmtReduce() {
        return amtReduce;
    }

    /**
     * Setter for property amtReduce.
     *
     * @param amtReduce New value of property amtReduce.
     */
    public void setAmtReduce(java.lang.String amtReduce) {
        this.amtReduce = amtReduce;
    }

    /**
     * Getter for property balanceAmt.
     *
     * @return Value of property balanceAmt.
     */
    public double getBalanceAmt() {
        return balanceAmt;
    }

    /**
     * Setter for property balanceAmt.
     *
     * @param balanceAmt New value of property balanceAmt.
     */
    public void setBalanceAmt(double balanceAmt) {
        this.balanceAmt = balanceAmt;
    }

    /**
     * Getter for property tdsAmountValue.
     *
     * @return Value of property tdsAmountValue.
     */
    public java.lang.String getTdsAmountValue() {
        return tdsAmountValue;
    }

    /**
     * Setter for property tdsAmountValue.
     *
     * @param tdsAmountValue New value of property tdsAmountValue.
     */
    public void setTdsAmountValue(java.lang.String tdsAmountValue) {
        this.tdsAmountValue = tdsAmountValue;
    }

    /**
     * Getter for property lastInterestProvisionDateValue.
     *
     * @return Value of property lastInterestProvisionDateValue.
     */
    public java.lang.String getLastInterestProvisionDateValue() {
        return lastInterestProvisionDateValue;
    }

    /**
     * Setter for property lastInterestProvisionDateValue.
     *
     * @param lastInterestProvisionDateValue New value of property
     * lastInterestProvisionDateValue.
     */
    public void setLastInterestProvisionDateValue(java.lang.String lastInterestProvisionDateValue) {
        this.lastInterestProvisionDateValue = lastInterestProvisionDateValue;
    }

    /**
     * Getter for property closedDateValue.
     *
     * @return Value of property closedDateValue.
     */
    public java.lang.String getClosedDateValue() {
        return closedDateValue;
    }

    /**
     * Setter for property closedDateValue.
     *
     * @param closedDateValue New value of property closedDateValue.
     */
    public void setClosedDateValue(java.lang.String closedDateValue) {
        this.closedDateValue = closedDateValue;
    }

    /**
     * Getter for property closingTypeValue.
     *
     * @return Value of property closingTypeValue.
     */
    public java.lang.String getClosingTypeValue() {
        return closingTypeValue;
    }

    /**
     * Setter for property closingTypeValue.
     *
     * @param closingTypeValue New value of property closingTypeValue.
     */
    public void setClosingTypeValue(java.lang.String closingTypeValue) {
        this.closingTypeValue = closingTypeValue;
    }

    /**
     * Getter for property viewTypeDeposit.
     *
     * @return Value of property viewTypeDeposit.
     */
    public java.lang.String getViewTypeDeposit() {
        return viewTypeDeposit;
    }

    /**
     * Setter for property viewTypeDeposit.
     *
     * @param viewTypeDeposit New value of property viewTypeDeposit.
     */
    public void setViewTypeDeposit(java.lang.String viewTypeDeposit) {
        this.viewTypeDeposit = viewTypeDeposit;
    }

    /**
     * Getter for property cbmMemberType.
     *
     * @return Value of property cbmMemberType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmMemberType() {
        return cbmMemberType;
    }

    /**
     * Setter for property cbmMemberType.
     *
     * @param cbmMemberType New value of property cbmMemberType.
     */
    public void setCbmMemberType(com.see.truetransact.clientutil.ComboBoxModel cbmMemberType) {
        this.cbmMemberType = cbmMemberType;
    }

    /**
     * Getter for property cboMember.
     *
     * @return Value of property cboMember.
     */
    public java.lang.String getCboMember() {
        return cboMember;
    }

    /**
     * Setter for property cboMember.
     *
     * @param cboMember New value of property cboMember.
     */
    public void setCboMember(java.lang.String cboMember) {
        this.cboMember = cboMember;
    }

    /**
     * Getter for property chkMember.
     *
     * @return Value of property chkMember.
     */
    //    public boolean isChkMember() {
    //        return chkMember;
    //    }
    //
    //    /**
    //     * Setter for property chkMember.
    //     * @param chkMember New value of property chkMember.
    //     */
    //    public void setChkMember(boolean chkMember) {
    //        this.chkMember = chkMember;
    //    }
    void setChkMember(boolean chkMember) {
        this.chkMember = chkMember;
        setChanged();
    }

    boolean getChkMember() {
        return this.chkMember;
    }

    /**
     * Getter for property RenewalDateValue.
     *
     * @return Value of property RenewalDateValue.
     */
    public java.lang.String getRenewalDateValue() {
        return RenewalDateValue;
    }

    /**
     * Setter for property RenewalDateValue.
     *
     * @param RenewalDateValue New value of property RenewalDateValue.
     */
    public void setRenewalDateValue(java.lang.String RenewalDateValue) {
        this.RenewalDateValue = RenewalDateValue;
    }

    /**
     * Getter for property rdoDeathClaim_Yes.
     *
     * @return Value of property rdoDeathClaim_Yes.
     */
    boolean getRdoDeathClaim_Yes() {
        return rdoDeathClaim_Yes;
    }

    /**
     * Setter for property rdoDeathClaim_Yes.
     *
     * @param rdoDeathClaim_Yes New value of property rdoDeathClaim_Yes.
     */
    void setRdoDeathClaim_Yes(boolean rdoDeathClaim_Yes) {
        this.rdoDeathClaim_Yes = rdoDeathClaim_Yes;
        setChanged();
    }

    /**
     * Getter for property rdoDeathClaim_No.
     *
     * @return Value of property rdoDeathClaim_No.
     */
    boolean getRdoDeathClaim_No() {
        return rdoDeathClaim_No;
    }

    /**
     * Setter for property rdoDeathClaim_No.
     *
     * @param rdoDeathClaim_No New value of property rdoDeathClaim_No.
     */
    void setRdoDeathClaim_No(boolean rdoDeathClaim_No) {
        this.rdoDeathClaim_No = rdoDeathClaim_No;
        setChanged();
    }

    /**
     * Getter for property rdoAutoRenewal_Yes.
     *
     * @return Value of property rdoAutoRenewal_Yes.
     */
    boolean getRdoAutoRenewal_Yes() {
        return this.rdoAutoRenewal_Yes;
    }

    /**
     * Setter for property rdoAutoRenewal_Yes.
     *
     * @param rdoAutoRenewal_Yes New value of property rdoAutoRenewal_Yes.
     */
    void setRdoAutoRenewal_Yes(boolean rdoAutoRenewal_Yes) {
        this.rdoAutoRenewal_Yes = rdoAutoRenewal_Yes;
        setChanged();
    }

    /**
     * Getter for property rdoAutoRenewal_No.
     *
     * @return Value of property rdoAutoRenewal_No.
     */
    boolean getRdoAutoRenewal_No() {
        return this.rdoAutoRenewal_No;
    }

    /**
     * Setter for property rdoAutoRenewal_No.
     *
     * @param rdoAutoRenewal_No New value of property rdoAutoRenewal_No.
     */
    void setRdoAutoRenewal_No(boolean rdoAutoRenewal_No) {
        this.rdoAutoRenewal_No = rdoAutoRenewal_No;
        setChanged();
    }

    /**
     * Getter for property rdowithIntRenewal_Yes.
     *
     * @return Value of property rdowithIntRenewal_Yes.
     */
    boolean getRdowithIntRenewal_Yes() {
        return rdowithIntRenewal_Yes;
    }

    /**
     * Setter for property rdowithIntRenewal_Yes.
     *
     * @param rdowithIntRenewal_Yes New value of property rdowithIntRenewal_Yes.
     */
    void setRdowithIntRenewal_Yes(boolean rdowithIntRenewal_Yes) {
        this.rdowithIntRenewal_Yes = rdowithIntRenewal_Yes;
        setChanged();
    }

    /**
     * Getter for property rdowithIntRenewal_No.
     *
     * @return Value of property rdowithIntRenewal_No.
     */
    boolean getRdowithIntRenewal_No() {
        return rdowithIntRenewal_No;
    }

    /**
     * Setter for property rdowithIntRenewal_No.
     *
     * @param rdowithIntRenewal_No New value of property rdowithIntRenewal_No.
     */
    void setRdowithIntRenewal_No(boolean rdowithIntRenewal_No) {
        this.rdowithIntRenewal_No = rdowithIntRenewal_No;
        setChanged();
    }

    /**
     * Getter for property rdoMatAlertReport_Yes.
     *
     * @return Value of property rdoMatAlertReport_Yes.
     */
    public boolean getRdoMatAlertReport_Yes() {
        return rdoMatAlertReport_Yes;
    }

    /**
     * Setter for property rdoMatAlertReport_Yes.
     *
     * @param rdoMatAlertReport_Yes New value of property rdoMatAlertReport_Yes.
     */
    void setRdoMatAlertReport_Yes(boolean rdoMatAlertReport_Yes) {
        this.rdoMatAlertReport_Yes = rdoMatAlertReport_Yes;
        setChanged();
    }

    /**
     * Getter for property rdoMatAlertReport_No.
     *
     * @return Value of property rdoMatAlertReport_No.
     */
    public boolean getRdoMatAlertReport_No() {
        return rdoMatAlertReport_No;
    }

    /**
     * Setter for property rdoMatAlertReport_No.
     *
     * @param rdoMatAlertReport_No New value of property rdoMatAlertReport_No.
     */
    void setRdoMatAlertReport_No(boolean rdoMatAlertReport_No) {
        this.rdoMatAlertReport_No = rdoMatAlertReport_No;
        setChanged();
    }

    /**
     * Getter for property closingRateOfInterestValue.
     *
     * @return Value of property closingRateOfInterestValue.
     */
    public java.lang.String getClosingRateOfInterestValue() {
        return closingRateOfInterestValue;
    }

    /**
     * Setter for property closingRateOfInterestValue.
     *
     * @param closingRateOfInterestValue New value of property
     * closingRateOfInterestValue.
     */
    public void setClosingRateOfInterestValue(java.lang.String closingRateOfInterestValue) {
        this.closingRateOfInterestValue = closingRateOfInterestValue;
    }

    /**
     * Getter for property closingInterestAmountValue.
     *
     * @return Value of property closingInterestAmountValue.
     */
    public java.lang.String getClosingInterestAmountValue() {
        return closingInterestAmountValue;
    }

    /**
     * Setter for property closingInterestAmountValue.
     *
     * @param closingInterestAmountValue New value of property
     * closingInterestAmountValue.
     */
    public void setClosingInterestAmountValue(java.lang.String closingInterestAmountValue) {
        this.closingInterestAmountValue = closingInterestAmountValue;
    }

    /**
     * Getter for property SBRateOfInterestValue.
     *
     * @return Value of property SBRateOfInterestValue.
     */
    public java.lang.String getSBRateOfInterestValue() {
        return SBRateOfInterestValue;
    }

    /**
     * Setter for property SBRateOfInterestValue.
     *
     * @param SBRateOfInterestValue New value of property SBRateOfInterestValue.
     */
    public void setSBRateOfInterestValue(java.lang.String SBRateOfInterestValue) {
        this.SBRateOfInterestValue = SBRateOfInterestValue;
    }

    /**
     * Getter for property SBInterestAmountValue.
     *
     * @return Value of property SBInterestAmountValue.
     */
    public java.lang.String getSBInterestAmountValue() {
        return SBInterestAmountValue;
    }

    /**
     * Setter for property SBInterestAmountValue.
     *
     * @param SBInterestAmountValue New value of property SBInterestAmountValue.
     */
    public void setSBInterestAmountValue(java.lang.String SBInterestAmountValue) {
        this.SBInterestAmountValue = SBInterestAmountValue;
    }

    /**
     * Getter for property sbPeriodRun.
     *
     * @return Value of property sbPeriodRun.
     */
    public double getSbPeriodRun() {
        return sbPeriodRun;
    }

    /**
     * Setter for property sbPeriodRun.
     *
     * @param sbPeriodRun New value of property sbPeriodRun.
     */
    public void setSbPeriodRun(double sbPeriodRun) {
        this.sbPeriodRun = sbPeriodRun;
    }

    /**
     * Getter for property rdoStandingInstruction_Yes.
     *
     * @return Value of property rdoStandingInstruction_Yes.
     */
    public boolean getRdoStandingInstruction_Yes() {
        return rdoStandingInstruction_Yes;
    }

    /**
     * Setter for property rdoStandingInstruction_Yes.
     *
     * @param rdoStandingInstruction_Yes New value of property
     * rdoStandingInstruction_Yes.
     */
    public void setRdoStandingInstruction_Yes(boolean rdoStandingInstruction_Yes) {
        this.rdoStandingInstruction_Yes = rdoStandingInstruction_Yes;
        setChanged();
    }

    /**
     * Getter for property rdoStandingInstruction_No.
     *
     * @return Value of property rdoStandingInstruction_No.
     */
    public boolean getRdoStandingInstruction_No() {
        return rdoStandingInstruction_No;
    }

    /**
     * Setter for property rdoStandingInstruction_No.
     *
     * @param rdoStandingInstruction_No New value of property
     * rdoStandingInstruction_No.
     */
    public void setRdoStandingInstruction_No(boolean rdoStandingInstruction_No) {
        this.rdoStandingInstruction_No = rdoStandingInstruction_No;
        setChanged();
    }

    /**
     * Getter for property SICreatedDateValue.
     *
     * @return Value of property SICreatedDateValue.
     */
    public java.lang.String getSICreatedDateValue() {
        return SICreatedDateValue;
    }

    /**
     * Setter for property SICreatedDateValue.
     *
     * @param SICreatedDateValue New value of property SICreatedDateValue.
     */
    public void setSICreatedDateValue(java.lang.String SICreatedDateValue) {
        this.SICreatedDateValue = SICreatedDateValue;
    }

    /**
     * Getter for property SINoValue.
     *
     * @return Value of property SINoValue.
     */
    public java.lang.String getSINoValue() {
        return SINoValue;
    }

    /**
     * Setter for property SINoValue.
     *
     * @param SINoValue New value of property SINoValue.
     */
    public void setSINoValue(java.lang.String SINoValue) {
        this.SINoValue = SINoValue;
    }

    /**
     * Getter for property SIProductTypeValue.
     *
     * @return Value of property SIProductTypeValue.
     */
    public java.lang.String getSIProductTypeValue() {
        return SIProductTypeValue;
    }

    /**
     * Setter for property SIProductTypeValue.
     *
     * @param SIProductTypeValue New value of property SIProductTypeValue.
     */
    public void setSIProductTypeValue(java.lang.String SIProductTypeValue) {
        this.SIProductTypeValue = SIProductTypeValue;
    }

    /**
     * Getter for property SIProductIdValue.
     *
     * @return Value of property SIProductIdValue.
     */
    public java.lang.String getSIProductIdValue() {
        return SIProductIdValue;
    }

    /**
     * Setter for property SIProductIdValue.
     *
     * @param SIProductIdValue New value of property SIProductIdValue.
     */
    public void setSIProductIdValue(java.lang.String SIProductIdValue) {
        this.SIProductIdValue = SIProductIdValue;
    }

    /**
     * Getter for property SIAccountNoValue.
     *
     * @return Value of property SIAccountNoValue.
     */
    public java.lang.String getSIAccountNoValue() {
        return SIAccountNoValue;
    }

    /**
     * Setter for property SIAccountNoValue.
     *
     * @param SIAccountNoValue New value of property SIAccountNoValue.
     */
    public void setSIAccountNoValue(java.lang.String SIAccountNoValue) {
        this.SIAccountNoValue = SIAccountNoValue;
    }

    /**
     * Getter for property SIAmountValue.
     *
     * @return Value of property SIAmountValue.
     */
    public java.lang.String getSIAmountValue() {
        return SIAmountValue;
    }

    /**
     * Setter for property SIAmountValue.
     *
     * @param SIAmountValue New value of property SIAmountValue.
     */
    public void setSIAmountValue(java.lang.String SIAmountValue) {
        this.SIAmountValue = SIAmountValue;
    }

    /**
     * Getter for property SIParticularsValue.
     *
     * @return Value of property SIParticularsValue.
     */
    public java.lang.String getSIParticularsValue() {
        return SIParticularsValue;
    }

    /**
     * Setter for property SIParticularsValue.
     *
     * @param SIParticularsValue New value of property SIParticularsValue.
     */
    public void setSIParticularsValue(java.lang.String SIParticularsValue) {
        this.SIParticularsValue = SIParticularsValue;
    }

    /**
     * Getter for property SIFrequencyValue.
     *
     * @return Value of property SIFrequencyValue.
     */
    public java.lang.String getSIFrequencyValue() {
        return SIFrequencyValue;
    }

    /**
     * Setter for property SIFrequencyValue.
     *
     * @param SIFrequencyValue New value of property SIFrequencyValue.
     */
    public void setSIFrequencyValue(java.lang.String SIFrequencyValue) {
        this.SIFrequencyValue = SIFrequencyValue;
    }

    /**
     * Getter for property SIForwardCountValue.
     *
     * @return Value of property SIForwardCountValue.
     */
    public java.lang.String getSIForwardCountValue() {
        return SIForwardCountValue;
    }

    /**
     * Setter for property SIForwardCountValue.
     *
     * @param SIForwardCountValue New value of property SIForwardCountValue.
     */
    public void setSIForwardCountValue(java.lang.String SIForwardCountValue) {
        this.SIForwardCountValue = SIForwardCountValue;
    }

    /**
     * Getter for property SIStartDateValue.
     *
     * @return Value of property SIStartDateValue.
     */
    public java.lang.String getSIStartDateValue() {
        return SIStartDateValue;
    }

    /**
     * Setter for property SIStartDateValue.
     *
     * @param SIStartDateValue New value of property SIStartDateValue.
     */
    public void setSIStartDateValue(java.lang.String SIStartDateValue) {
        this.SIStartDateValue = SIStartDateValue;
    }

    /**
     * Getter for property SIEndDateValue.
     *
     * @return Value of property SIEndDateValue.
     */
    public java.lang.String getSIEndDateValue() {
        return SIEndDateValue;
    }

    /**
     * Setter for property SIEndDateValue.
     *
     * @param SIEndDateValue New value of property SIEndDateValue.
     */
    public void setSIEndDateValue(java.lang.String SIEndDateValue) {
        this.SIEndDateValue = SIEndDateValue;
    }

    /**
     * Getter for property AcceptanceChargesValue.
     *
     * @return Value of property AcceptanceChargesValue.
     */
    public java.lang.String getAcceptanceChargesValue() {
        return AcceptanceChargesValue;
    }

    /**
     * Setter for property AcceptanceChargesValue.
     *
     * @param AcceptanceChargesValue New value of property
     * AcceptanceChargesValue.
     */
    public void setAcceptanceChargesValue(java.lang.String AcceptanceChargesValue) {
        this.AcceptanceChargesValue = AcceptanceChargesValue;
    }

    /**
     * Getter for property SIFailureChargesValue.
     *
     * @return Value of property SIFailureChargesValue.
     */
    public java.lang.String getSIFailureChargesValue() {
        return SIFailureChargesValue;
    }

    /**
     * Setter for property SIFailureChargesValue.
     *
     * @param SIFailureChargesValue New value of property SIFailureChargesValue.
     */
    public void setSIFailureChargesValue(java.lang.String SIFailureChargesValue) {
        this.SIFailureChargesValue = SIFailureChargesValue;
    }

    /**
     * Getter for property SIExecutionChargesValue.
     *
     * @return Value of property SIExecutionChargesValue.
     */
    public java.lang.String getSIExecutionChargesValue() {
        return SIExecutionChargesValue;
    }

    /**
     * Setter for property SIExecutionChargesValue.
     *
     * @param SIExecutionChargesValue New value of property
     * SIExecutionChargesValue.
     */
    public void setSIExecutionChargesValue(java.lang.String SIExecutionChargesValue) {
        this.SIExecutionChargesValue = SIExecutionChargesValue;
    }

    /**
     * Getter for property depositPeriodRunValue.
     *
     * @return Value of property depositPeriodRunValue.
     */
    public java.lang.String getDepositPeriodRunValue() {
        return depositPeriodRunValue;
    }

    /**
     * Setter for property depositPeriodRunValue.
     *
     * @param depositPeriodRunValue New value of property depositPeriodRunValue.
     */
    public void setDepositPeriodRunValue(java.lang.String depositPeriodRunValue) {
        this.depositPeriodRunValue = depositPeriodRunValue;
    }

    /**
     * Getter for property transferOutBranchValue.
     *
     * @return Value of property transferOutBranchValue.
     */
    public java.lang.String getTransferOutBranchValue() {
        return transferOutBranchValue;
    }

    /**
     * Setter for property transferOutBranchValue.
     *
     * @param transferOutBranchValue New value of property
     * transferOutBranchValue.
     */
    public void setTransferOutBranchValue(java.lang.String transferOutBranchValue) {
        this.transferOutBranchValue = transferOutBranchValue;
    }

    /**
     * Getter for property rdoCalenderFreq_Yes.
     *
     * @return Value of property rdoCalenderFreq_Yes.
     */
    public boolean getRdoCalenderFreq_Yes() {
        return rdoCalenderFreq_Yes;
    }

    /**
     * Setter for property rdoCalenderFreq_Yes.
     *
     * @param rdoCalenderFreq_Yes New value of property rdoCalenderFreq_Yes.
     */
    public void setRdoCalenderFreq_Yes(boolean rdoCalenderFreq_Yes) {
        this.rdoCalenderFreq_Yes = rdoCalenderFreq_Yes;
    }

    /**
     * Getter for property rdoCalenderFreq_No.
     *
     * @return Value of property rdoCalenderFreq_No.
     */
    public boolean getRdoCalenderFreq_No() {
        return rdoCalenderFreq_No;
    }

    /**
     * Setter for property rdoCalenderFreq_No.
     *
     * @param rdoCalenderFreq_No New value of property rdoCalenderFreq_No.
     */
    public void setRdoCalenderFreq_No(boolean rdoCalenderFreq_No) {
        this.rdoCalenderFreq_No = rdoCalenderFreq_No;
    }

    /**
     * Getter for property calenderFreqDate.
     *
     * @return Value of property calenderFreqDate.
     */
    public java.lang.String getCalenderFreqDate() {
        return calenderFreqDate;
    }

    /**
     * Setter for property calenderFreqDate.
     *
     * @param calenderFreqDate New value of property calenderFreqDate.
     */
    public void setCalenderFreqDate(java.lang.String calenderFreqDate) {
        this.calenderFreqDate = calenderFreqDate;
    }

    /**
     * Getter for property printing_No.
     *
     * @return Value of property printing_No.
     */
    public java.lang.String getPrinting_No() {
        return printing_No;
    }

    /**
     * Setter for property printing_No.
     *
     * @param printing_No New value of property printing_No.
     */
    public void setPrinting_No(java.lang.String printing_No) {
        this.printing_No = printing_No;
    }

    /**
     * Getter for property cbmCalenderFreqDay.
     *
     * @return Value of property cbmCalenderFreqDay.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCalenderFreqDay() {
        return cbmCalenderFreqDay;
    }

    /**
     * Setter for property cbmCalenderFreqDay.
     *
     * @param cbmCalenderFreqDay New value of property cbmCalenderFreqDay.
     */
    public void setCbmCalenderFreqDay(com.see.truetransact.clientutil.ComboBoxModel cbmCalenderFreqDay) {
        this.cbmCalenderFreqDay = cbmCalenderFreqDay;
    }

    /**
     * Getter for property cboCalenderFreq.
     *
     * @return Value of property cboCalenderFreq.
     */
    public java.lang.String getCboCalenderFreq() {
        return cboCalenderFreq;
    }

    /**
     * Setter for property cboCalenderFreq.
     *
     * @param cboCalenderFreq New value of property cboCalenderFreq.
     */
    public void setCboCalenderFreq(java.lang.String cboCalenderFreq) {
        this.cboCalenderFreq = cboCalenderFreq;
    }

    /**
     * Getter for property renewalBalIntAmtVal.
     *
     * @return Value of property renewalBalIntAmtVal.
     */
    public java.lang.String getRenewalBalIntAmtVal() {
        return renewalBalIntAmtVal;
    }

    /**
     * Setter for property renewalBalIntAmtVal.
     *
     * @param renewalBalIntAmtVal New value of property renewalBalIntAmtVal.
     */
    public void setRenewalBalIntAmtVal(java.lang.String renewalBalIntAmtVal) {
        this.renewalBalIntAmtVal = renewalBalIntAmtVal;
    }

    /**
     * Getter for property renewalValPeriodRun.
     *
     * @return Value of property renewalValPeriodRun.
     */
    public java.lang.String getRenewalValPeriodRun() {
        return renewalValPeriodRun;
    }

    /**
     * Setter for property renewalValPeriodRun.
     *
     * @param renewalValPeriodRun New value of property renewalValPeriodRun.
     */
    public void setRenewalValPeriodRun(java.lang.String renewalValPeriodRun) {
        this.renewalValPeriodRun = renewalValPeriodRun;
    }

    /**
     * Getter for property renewalSBPeriodVal.
     *
     * @return Value of property renewalSBPeriodVal.
     */
    public java.lang.String getRenewalSBPeriodVal() {
        return renewalSBPeriodVal;
    }

    /**
     * Setter for property renewalSBPeriodVal.
     *
     * @param renewalSBPeriodVal New value of property renewalSBPeriodVal.
     */
    public void setRenewalSBPeriodVal(java.lang.String renewalSBPeriodVal) {
        this.renewalSBPeriodVal = renewalSBPeriodVal;
    }

    /**
     * Getter for property renewalSBIntAmtVal.
     *
     * @return Value of property renewalSBIntAmtVal.
     */
    public java.lang.String getRenewalSBIntAmtVal() {
        return renewalSBIntAmtVal;
    }

    /**
     * Setter for property renewalSBIntAmtVal.
     *
     * @param renewalSBIntAmtVal New value of property renewalSBIntAmtVal.
     */
    public void setRenewalSBIntAmtVal(java.lang.String renewalSBIntAmtVal) {
        this.renewalSBIntAmtVal = renewalSBIntAmtVal;
    }

    /**
     * Getter for property renewalDepDate.
     *
     * @return Value of property renewalDepDate.
     */
    public java.lang.String getRenewalDepDate() {
        return renewalDepDate;
    }

    /**
     * Setter for property renewalDepDate.
     *
     * @param renewalDepDate New value of property renewalDepDate.
     */
    public void setRenewalDepDate(java.lang.String renewalDepDate) {
        this.renewalDepDate = renewalDepDate;
    }

    /**
     * Getter for property renewaltdtDateOfDeposit.
     *
     * @return Value of property renewaltdtDateOfDeposit.
     */
    public java.lang.String getRenewaltdtDateOfDeposit() {
        return renewaltdtDateOfDeposit;
    }

    /**
     * Setter for property renewaltdtDateOfDeposit.
     *
     * @param renewaltdtDateOfDeposit New value of property
     * renewaltdtDateOfDeposit.
     */
    public void setRenewaltdtDateOfDeposit(java.lang.String renewaltdtDateOfDeposit) {
        this.renewaltdtDateOfDeposit = renewaltdtDateOfDeposit;
    }

    /**
     * Getter for property renewaltxtPeriodOfDeposit_Years.
     *
     * @return Value of property renewaltxtPeriodOfDeposit_Years.
     */
    public java.lang.String getRenewaltxtPeriodOfDeposit_Years() {
        return renewaltxtPeriodOfDeposit_Years;
    }

    /**
     * Setter for property renewaltxtPeriodOfDeposit_Years.
     *
     * @param renewaltxtPeriodOfDeposit_Years New value of property
     * renewaltxtPeriodOfDeposit_Years.
     */
    public void setRenewaltxtPeriodOfDeposit_Years(java.lang.String renewaltxtPeriodOfDeposit_Years) {
        this.renewaltxtPeriodOfDeposit_Years = renewaltxtPeriodOfDeposit_Years;
    }

    /**
     * Getter for property renewaltxtPeriodOfDeposit_Months.
     *
     * @return Value of property renewaltxtPeriodOfDeposit_Months.
     */
    public java.lang.String getRenewaltxtPeriodOfDeposit_Months() {
        return renewaltxtPeriodOfDeposit_Months;
    }

    /**
     * Setter for property renewaltxtPeriodOfDeposit_Months.
     *
     * @param renewaltxtPeriodOfDeposit_Months New value of property
     * renewaltxtPeriodOfDeposit_Months.
     */
    public void setRenewaltxtPeriodOfDeposit_Months(java.lang.String renewaltxtPeriodOfDeposit_Months) {
        this.renewaltxtPeriodOfDeposit_Months = renewaltxtPeriodOfDeposit_Months;
    }

    /**
     * Getter for property renewaltxtPeriodOfDeposit_Days.
     *
     * @return Value of property renewaltxtPeriodOfDeposit_Days.
     */
    public java.lang.String getRenewaltxtPeriodOfDeposit_Days() {
        return renewaltxtPeriodOfDeposit_Days;
    }

    /**
     * Setter for property renewaltxtPeriodOfDeposit_Days.
     *
     * @param renewaltxtPeriodOfDeposit_Days New value of property
     * renewaltxtPeriodOfDeposit_Days.
     */
    public void setRenewaltxtPeriodOfDeposit_Days(java.lang.String renewaltxtPeriodOfDeposit_Days) {
        this.renewaltxtPeriodOfDeposit_Days = renewaltxtPeriodOfDeposit_Days;
    }

    /**
     * Getter for property renewaltdtMaturityDate.
     *
     * @return Value of property renewaltdtMaturityDate.
     */
    public java.lang.String getRenewaltdtMaturityDate() {
        return renewaltdtMaturityDate;
    }

    /**
     * Setter for property renewaltdtMaturityDate.
     *
     * @param renewaltdtMaturityDate New value of property
     * renewaltdtMaturityDate.
     */
    public void setRenewaltdtMaturityDate(java.lang.String renewaltdtMaturityDate) {
        this.renewaltdtMaturityDate = renewaltdtMaturityDate;
    }

    /**
     * Getter for property renewaltxtDepositAmount.
     *
     * @return Value of property renewaltxtDepositAmount.
     */
    public java.lang.String getRenewaltxtDepositAmount() {
        return renewaltxtDepositAmount;
    }

    /**
     * Setter for property renewaltxtDepositAmount.
     *
     * @param renewaltxtDepositAmount New value of property
     * renewaltxtDepositAmount.
     */
    public void setRenewaltxtDepositAmount(java.lang.String renewaltxtDepositAmount) {
        this.renewaltxtDepositAmount = renewaltxtDepositAmount;
    }

    /**
     * Getter for property renewaltxtRateOfInterest.
     *
     * @return Value of property renewaltxtRateOfInterest.
     */
    public java.lang.String getRenewaltxtRateOfInterest() {
        return renewaltxtRateOfInterest;
    }

    /**
     * Setter for property renewaltxtRateOfInterest.
     *
     * @param renewaltxtRateOfInterest New value of property
     * renewaltxtRateOfInterest.
     */
    public void setRenewaltxtRateOfInterest(java.lang.String renewaltxtRateOfInterest) {
        this.renewaltxtRateOfInterest = renewaltxtRateOfInterest;
    }

    /**
     * Getter for property renewaltxtMaturityAmount.
     *
     * @return Value of property renewaltxtMaturityAmount.
     */
    public java.lang.String getRenewaltxtMaturityAmount() {
        return renewaltxtMaturityAmount;
    }

    /**
     * Setter for property renewaltxtMaturityAmount.
     *
     * @param renewaltxtMaturityAmount New value of property
     * renewaltxtMaturityAmount.
     */
    public void setRenewaltxtMaturityAmount(java.lang.String renewaltxtMaturityAmount) {
        this.renewaltxtMaturityAmount = renewaltxtMaturityAmount;
    }

    /**
     * Getter for property renewaltxtTotalInterestAmount.
     *
     * @return Value of property renewaltxtTotalInterestAmount.
     */
    public java.lang.String getRenewaltxtTotalInterestAmount() {
        return renewaltxtTotalInterestAmount;
    }

    /**
     * Setter for property renewaltxtTotalInterestAmount.
     *
     * @param renewaltxtTotalInterestAmount New value of property
     * renewaltxtTotalInterestAmount.
     */
    public void setRenewaltxtTotalInterestAmount(java.lang.String renewaltxtTotalInterestAmount) {
        this.renewaltxtTotalInterestAmount = renewaltxtTotalInterestAmount;
    }

    /**
     * Getter for property renewaltxtPeriodicInterestAmount.
     *
     * @return Value of property renewaltxtPeriodicInterestAmount.
     */
    public java.lang.String getRenewaltxtPeriodicInterestAmount() {
        return renewaltxtPeriodicInterestAmount;
    }

    /**
     * Setter for property renewaltxtPeriodicInterestAmount.
     *
     * @param renewaltxtPeriodicInterestAmount New value of property
     * renewaltxtPeriodicInterestAmount.
     */
    public void setRenewaltxtPeriodicInterestAmount(java.lang.String renewaltxtPeriodicInterestAmount) {
        this.renewaltxtPeriodicInterestAmount = renewaltxtPeriodicInterestAmount;
    }

    /**
     * Getter for property renewalcboInterestPaymentMode.
     *
     * @return Value of property renewalcboInterestPaymentMode.
     */
    public java.lang.String getRenewalcboInterestPaymentMode() {
        return renewalcboInterestPaymentMode;
    }

    /**
     * Setter for property renewalcboInterestPaymentMode.
     *
     * @param renewalcboInterestPaymentMode New value of property
     * renewalcboInterestPaymentMode.
     */
    public void setRenewalcboInterestPaymentMode(java.lang.String renewalcboInterestPaymentMode) {
        this.renewalcboInterestPaymentMode = renewalcboInterestPaymentMode;
    }

    /**
     * Getter for property renewalcboInterestPaymentFrequency.
     *
     * @return Value of property renewalcboInterestPaymentFrequency.
     */
    public java.lang.String getRenewalcboInterestPaymentFrequency() {
        return renewalcboInterestPaymentFrequency;
    }

    /**
     * Setter for property renewalcboInterestPaymentFrequency.
     *
     * @param renewalcboInterestPaymentFrequency New value of property
     * renewalcboInterestPaymentFrequency.
     */
    public void setRenewalcboInterestPaymentFrequency(java.lang.String renewalcboInterestPaymentFrequency) {
        this.renewalcboInterestPaymentFrequency = renewalcboInterestPaymentFrequency;
    }

    /**
     * Getter for property renewalcboCalenderFreq.
     *
     * @return Value of property renewalcboCalenderFreq.
     */
    public java.lang.String getRenewalcboCalenderFreq() {
        return renewalcboCalenderFreq;
    }

    /**
     * Setter for property renewalcboCalenderFreq.
     *
     * @param renewalcboCalenderFreq New value of property
     * renewalcboCalenderFreq.
     */
    public void setRenewalcboCalenderFreq(java.lang.String renewalcboCalenderFreq) {
        this.renewalcboCalenderFreq = renewalcboCalenderFreq;
    }

    /**
     * Getter for property renewalcboProdId.
     *
     * @return Value of property renewalcboProdId.
     */
    public java.lang.String getRenewalcboProdId() {
        return renewalcboProdId;
    }

    /**
     * Setter for property renewalcboProdId.
     *
     * @param renewalcboProdId New value of property renewalcboProdId.
     */
    public void setRenewalcboProdId(java.lang.String renewalcboProdId) {
        this.renewalcboProdId = renewalcboProdId;
    }

    /**
     * Getter for property renewalcboProdType.
     *
     * @return Value of property renewalcboProdType.
     */
    public java.lang.String getRenewalcboProdType() {
        return renewalcboProdType;
    }

    /**
     * Setter for property renewalcboProdType.
     *
     * @param renewalcboProdType New value of property renewalcboProdType.
     */
    public void setRenewalcboProdType(java.lang.String renewalcboProdType) {
        this.renewalcboProdType = renewalcboProdType;
    }

    /**
     * Getter for property renewalcustomerIdCr.
     *
     * @return Value of property renewalcustomerIdCr.
     */
    public java.lang.String getRenewalcustomerIdCr() {
        return renewalcustomerIdCr;
    }

    /**
     * Setter for property renewalcustomerIdCr.
     *
     * @param renewalcustomerIdCr New value of property renewalcustomerIdCr.
     */
    public void setRenewalcustomerIdCr(java.lang.String renewalcustomerIdCr) {
        this.renewalcustomerIdCr = renewalcustomerIdCr;
    }

    /**
     * Getter for property renewalcustomerNameCrValue.
     *
     * @return Value of property renewalcustomerNameCrValue.
     */
    public java.lang.String getRenewalcustomerNameCrValue() {
        return renewalcustomerNameCrValue;
    }

    /**
     * Setter for property renewalcustomerNameCrValue.
     *
     * @param renewalcustomerNameCrValue New value of property
     * renewalcustomerNameCrValue.
     */
    public void setRenewalcustomerNameCrValue(java.lang.String renewalcustomerNameCrValue) {
        this.renewalcustomerNameCrValue = renewalcustomerNameCrValue;
    }

    /**
     * Getter for property cbmRenewalDepTransMode.
     *
     * @return Value of property cbmRenewalDepTransMode.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRenewalDepTransMode() {
        return cbmRenewalDepTransMode;
    }

    /**
     * Setter for property cbmRenewalDepTransMode.
     *
     * @param cbmRenewalDepTransMode New value of property
     * cbmRenewalDepTransMode.
     */
    public void setCbmRenewalDepTransMode(com.see.truetransact.clientutil.ComboBoxModel cbmRenewalDepTransMode) {
        this.cbmRenewalDepTransMode = cbmRenewalDepTransMode;
    }

    /**
     * Getter for property cbmRenewalInterestTransMode.
     *
     * @return Value of property cbmRenewalInterestTransMode.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRenewalInterestTransMode() {
        return cbmRenewalInterestTransMode;
    }

    /**
     * Setter for property cbmRenewalInterestTransMode.
     *
     * @param cbmRenewalInterestTransMode New value of property
     * cbmRenewalInterestTransMode.
     */
    public void setCbmRenewalInterestTransMode(com.see.truetransact.clientutil.ComboBoxModel cbmRenewalInterestTransMode) {
        this.cbmRenewalInterestTransMode = cbmRenewalInterestTransMode;
    }

    /**
     * Getter for property cbmRenewalInterestPaymentMode.
     *
     * @return Value of property cbmRenewalInterestPaymentMode.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRenewalInterestPaymentMode() {
        return cbmRenewalInterestPaymentMode;
    }

    /**
     * Setter for property cbmRenewalInterestPaymentMode.
     *
     * @param cbmRenewalInterestPaymentMode New value of property
     * cbmRenewalInterestPaymentMode.
     */
    public void setCbmRenewalInterestPaymentMode(com.see.truetransact.clientutil.ComboBoxModel cbmRenewalInterestPaymentMode) {
        this.cbmRenewalInterestPaymentMode = cbmRenewalInterestPaymentMode;
    }

    /**
     * Getter for property cbmRenewalInterestPaymentFrequency.
     *
     * @return Value of property cbmRenewalInterestPaymentFrequency.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRenewalInterestPaymentFrequency() {
        return cbmRenewalInterestPaymentFrequency;
    }

    /**
     * Setter for property cbmRenewalInterestPaymentFrequency.
     *
     * @param cbmRenewalInterestPaymentFrequency New value of property
     * cbmRenewalInterestPaymentFrequency.
     */
    public void setCbmRenewalInterestPaymentFrequency(com.see.truetransact.clientutil.ComboBoxModel cbmRenewalInterestPaymentFrequency) {
        this.cbmRenewalInterestPaymentFrequency = cbmRenewalInterestPaymentFrequency;
    }

    /**
     * Getter for property cbmRenewalDepTransProdType.
     *
     * @return Value of property cbmRenewalDepTransProdType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRenewalDepTransProdType() {
        return cbmRenewalDepTransProdType;
    }

    /**
     * Setter for property cbmRenewalDepTransProdType.
     *
     * @param cbmRenewalDepTransProdType New value of property
     * cbmRenewalDepTransProdType.
     */
    public void setCbmRenewalDepTransProdType(com.see.truetransact.clientutil.ComboBoxModel cbmRenewalDepTransProdType) {
        this.cbmRenewalDepTransProdType = cbmRenewalDepTransProdType;
    }

    /**
     * Getter for property cbmRenewalInterestTransProdType.
     *
     * @return Value of property cbmRenewalInterestTransProdType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRenewalInterestTransProdType() {
        return cbmRenewalInterestTransProdType;
    }

    /**
     * Setter for property cbmRenewalInterestTransProdType.
     *
     * @param cbmRenewalInterestTransProdType New value of property
     * cbmRenewalInterestTransProdType.
     */
    public void setCbmRenewalInterestTransProdType(com.see.truetransact.clientutil.ComboBoxModel cbmRenewalInterestTransProdType) {
        this.cbmRenewalInterestTransProdType = cbmRenewalInterestTransProdType;
    }

    /**
     * Getter for property cbmRenewalProdType.
     *
     * @return Value of property cbmRenewalProdType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRenewalProdType() {
        return cbmRenewalProdType;
    }

    /**
     * Setter for property cbmRenewalProdType.
     *
     * @param cbmRenewalProdType New value of property cbmRenewalProdType.
     */
    public void setCbmRenewalProdType(com.see.truetransact.clientutil.ComboBoxModel cbmRenewalProdType) {
        this.cbmRenewalProdType = cbmRenewalProdType;
    }

    /**
     * Getter for property cboRenewalDepTransMode.
     *
     * @return Value of property cboRenewalDepTransMode.
     */
    public java.lang.String getCboRenewalDepTransMode() {
        return cboRenewalDepTransMode;
    }

    /**
     * Setter for property cboRenewalDepTransMode.
     *
     * @param cboRenewalDepTransMode New value of property
     * cboRenewalDepTransMode.
     */
    public void setCboRenewalDepTransMode(java.lang.String cboRenewalDepTransMode) {
        this.cboRenewalDepTransMode = cboRenewalDepTransMode;
    }

    /**
     * Getter for property cboRenewalInterestTransMode.
     *
     * @return Value of property cboRenewalInterestTransMode.
     */
    public java.lang.String getCboRenewalInterestTransMode() {
        return cboRenewalInterestTransMode;
    }

    /**
     * Setter for property cboRenewalInterestTransMode.
     *
     * @param cboRenewalInterestTransMode New value of property
     * cboRenewalInterestTransMode.
     */
    public void setCboRenewalInterestTransMode(java.lang.String cboRenewalInterestTransMode) {
        this.cboRenewalInterestTransMode = cboRenewalInterestTransMode;
    }

    /**
     * Getter for property cboRenewalInterestPaymentMode.
     *
     * @return Value of property cboRenewalInterestPaymentMode.
     */
    public java.lang.String getCboRenewalInterestPaymentMode() {
        return cboRenewalInterestPaymentMode;
    }

    /**
     * Setter for property cboRenewalInterestPaymentMode.
     *
     * @param cboRenewalInterestPaymentMode New value of property
     * cboRenewalInterestPaymentMode.
     */
    public void setCboRenewalInterestPaymentMode(java.lang.String cboRenewalInterestPaymentMode) {
        this.cboRenewalInterestPaymentMode = cboRenewalInterestPaymentMode;
    }

    /**
     * Getter for property cboRenewalInterestPaymentFrequency.
     *
     * @return Value of property cboRenewalInterestPaymentFrequency.
     */
    public java.lang.String getCboRenewalInterestPaymentFrequency() {
        return cboRenewalInterestPaymentFrequency;
    }

    /**
     * Setter for property cboRenewalInterestPaymentFrequency.
     *
     * @param cboRenewalInterestPaymentFrequency New value of property
     * cboRenewalInterestPaymentFrequency.
     */
    public void setCboRenewalInterestPaymentFrequency(java.lang.String cboRenewalInterestPaymentFrequency) {
        this.cboRenewalInterestPaymentFrequency = cboRenewalInterestPaymentFrequency;
    }

    /**
     * Getter for property cboRenewalDepTransProdType.
     *
     * @return Value of property cboRenewalDepTransProdType.
     */
    public java.lang.String getCboRenewalDepTransProdType() {
        return cboRenewalDepTransProdType;
    }

    /**
     * Setter for property cboRenewalDepTransProdType.
     *
     * @param cboRenewalDepTransProdType New value of property
     * cboRenewalDepTransProdType.
     */
    public void setCboRenewalDepTransProdType(java.lang.String cboRenewalDepTransProdType) {
        this.cboRenewalDepTransProdType = cboRenewalDepTransProdType;
    }

    /**
     * Getter for property cboRenewalInterestTransProdType.
     *
     * @return Value of property cboRenewalInterestTransProdType.
     */
    public java.lang.String getCboRenewalInterestTransProdType() {
        return cboRenewalInterestTransProdType;
    }

    /**
     * Setter for property cboRenewalInterestTransProdType.
     *
     * @param cboRenewalInterestTransProdType New value of property
     * cboRenewalInterestTransProdType.
     */
    public void setCboRenewalInterestTransProdType(java.lang.String cboRenewalInterestTransProdType) {
        this.cboRenewalInterestTransProdType = cboRenewalInterestTransProdType;
    }

    /**
     * Getter for property cboRenewalProdType.
     *
     * @return Value of property cboRenewalProdType.
     */
    public java.lang.String getCboRenewalProdType() {
        return cboRenewalProdType;
    }

    /**
     * Setter for property cboRenewalProdType.
     *
     * @param cboRenewalProdType New value of property cboRenewalProdType.
     */
    public void setCboRenewalProdType(java.lang.String cboRenewalProdType) {
        this.cboRenewalProdType = cboRenewalProdType;
    }

    /**
     * Getter for property cbmRenewalDepTransProdId.
     *
     * @return Value of property cbmRenewalDepTransProdId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRenewalDepTransProdId() {
        return cbmRenewalDepTransProdId;
    }

    /**
     * Setter for property cbmRenewalDepTransProdId.
     *
     * @param cbmRenewalDepTransProdId New value of property
     * cbmRenewalDepTransProdId.
     */
    public void setCbmRenewalDepTransProdId(com.see.truetransact.clientutil.ComboBoxModel cbmRenewalDepTransProdId) {
        this.cbmRenewalDepTransProdId = cbmRenewalDepTransProdId;
    }

    /**
     * Getter for property cbmRenewalInterestTransProdId.
     *
     * @return Value of property cbmRenewalInterestTransProdId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRenewalInterestTransProdId() {
        return cbmRenewalInterestTransProdId;
    }

    /**
     * Setter for property cbmRenewalInterestTransProdId.
     *
     * @param cbmRenewalInterestTransProdId New value of property
     * cbmRenewalInterestTransProdId.
     */
    public void setCbmRenewalInterestTransProdId(com.see.truetransact.clientutil.ComboBoxModel cbmRenewalInterestTransProdId) {
        this.cbmRenewalInterestTransProdId = cbmRenewalInterestTransProdId;
    }

    /**
     * Getter for property cbmRenewalProdId.
     *
     * @return Value of property cbmRenewalProdId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRenewalProdId() {
        return cbmRenewalProdId;
    }

    /**
     * Setter for property cbmRenewalProdId.
     *
     * @param cbmRenewalProdId New value of property cbmRenewalProdId.
     */
    public void setCbmRenewalProdId(com.see.truetransact.clientutil.ComboBoxModel cbmRenewalProdId) {
        this.cbmRenewalProdId = cbmRenewalProdId;
    }

    /**
     * Getter for property cboRenewalDepTransProdId.
     *
     * @return Value of property cboRenewalDepTransProdId.
     */
    public java.lang.String getCboRenewalDepTransProdId() {
        return cboRenewalDepTransProdId;
    }

    /**
     * Setter for property cboRenewalDepTransProdId.
     *
     * @param cboRenewalDepTransProdId New value of property
     * cboRenewalDepTransProdId.
     */
    public void setCboRenewalDepTransProdId(java.lang.String cboRenewalDepTransProdId) {
        this.cboRenewalDepTransProdId = cboRenewalDepTransProdId;
    }

    /**
     * Getter for property cboRenewalInterestTransProdId.
     *
     * @return Value of property cboRenewalInterestTransProdId.
     */
    public java.lang.String getCboRenewalInterestTransProdId() {
        return cboRenewalInterestTransProdId;
    }

    /**
     * Setter for property cboRenewalInterestTransProdId.
     *
     * @param cboRenewalInterestTransProdId New value of property
     * cboRenewalInterestTransProdId.
     */
    public void setCboRenewalInterestTransProdId(java.lang.String cboRenewalInterestTransProdId) {
        this.cboRenewalInterestTransProdId = cboRenewalInterestTransProdId;
    }

    /**
     * Getter for property cboRenewalProdId.
     *
     * @return Value of property cboRenewalProdId.
     */
    public java.lang.String getCboRenewalProdId() {
        return cboRenewalProdId;
    }

    /**
     * Setter for property cboRenewalProdId.
     *
     * @param cboRenewalProdId New value of property cboRenewalProdId.
     */
    public void setCboRenewalProdId(java.lang.String cboRenewalProdId) {
        this.cboRenewalProdId = cboRenewalProdId;
    }

    /**
     * Getter for property cbmRenewalDepositProdId.
     *
     * @return Value of property cbmRenewalDepositProdId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRenewalDepositProdId() {
        return cbmRenewalDepositProdId;
    }

    /**
     * Setter for property cbmRenewalDepositProdId.
     *
     * @param cbmRenewalDepositProdId New value of property
     * cbmRenewalDepositProdId.
     */
    public void setCbmRenewalDepositProdId(com.see.truetransact.clientutil.ComboBoxModel cbmRenewalDepositProdId) {
        this.cbmRenewalDepositProdId = cbmRenewalDepositProdId;
    }

    /**
     * Getter for property cboRenewalDepositCategory.
     *
     * @return Value of property cboRenewalDepositCategory.
     */
    public java.lang.String getCboRenewalDepositCategory() {
        return cboRenewalDepositCategory;
    }

    /**
     * Setter for property cboRenewalDepositCategory.
     *
     * @param cboRenewalDepositCategory New value of property
     * cboRenewalDepositCategory.
     */
    public void setCboRenewalDepositCategory(java.lang.String cboRenewalDepositCategory) {
        this.cboRenewalDepositCategory = cboRenewalDepositCategory;
    }

    /**
     * Getter for property cboRenewalDepositProdId.
     *
     * @return Value of property cboRenewalDepositProdId.
     */
    public java.lang.String getCboRenewalDepositProdId() {
        return cboRenewalDepositProdId;
    }

    /**
     * Setter for property cboRenewalDepositProdId.
     *
     * @param cboRenewalDepositProdId New value of property
     * cboRenewalDepositProdId.
     */
    public void setCboRenewalDepositProdId(java.lang.String cboRenewalDepositProdId) {
        this.cboRenewalDepositProdId = cboRenewalDepositProdId;
    }

    /**
     * Getter for property cbmRenewalDepositCategory.
     *
     * @return Value of property cbmRenewalDepositCategory.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRenewalDepositCategory() {
        return cbmRenewalDepositCategory;
    }

    /**
     * Setter for property cbmRenewalDepositCategory.
     *
     * @param cbmRenewalDepositCategory New value of property
     * cbmRenewalDepositCategory.
     */
    public void setCbmRenewalDepositCategory(com.see.truetransact.clientutil.ComboBoxModel cbmRenewalDepositCategory) {
        this.cbmRenewalDepositCategory = cbmRenewalDepositCategory;
    }

    /**
     * Getter for property rdoRenewalWithdrawing_Yes.
     *
     * @return Value of property rdoRenewalWithdrawing_Yes.
     */
    public boolean getRdoRenewalWithdrawing_Yes() {
        return this.rdoRenewalWithdrawing_Yes;
    }

    /**
     * Setter for property rdoRenewalWithdrawing_Yes.
     *
     * @param rdoRenewalWithdrawing_Yes New value of property
     * rdoRenewalWithdrawing_Yes.
     */
    public void setRdoRenewalWithdrawing_Yes(boolean rdoRenewalWithdrawing_Yes) {
        this.rdoRenewalWithdrawing_Yes = rdoRenewalWithdrawing_Yes;
        setChanged();
    }

    /**
     * Getter for property rdoRenewalWithdrawing_No.
     *
     * @return Value of property rdoRenewalWithdrawing_No.
     */
    public boolean getRdoRenewalWithdrawing_No() {
        return this.rdoRenewalWithdrawing_No;
    }

    /**
     * Setter for property rdoRenewalWithdrawing_No.
     *
     * @param rdoRenewalWithdrawing_No New value of property
     * rdoRenewalWithdrawing_No.
     */
    public void setRdoRenewalWithdrawing_No(boolean rdoRenewalWithdrawing_No) {
        this.rdoRenewalWithdrawing_No = rdoRenewalWithdrawing_No;
        setChanged();
    }

    /**
     * Getter for property rdoRenewalAdding_Yes.
     *
     * @return Value of property rdoRenewalAdding_Yes.
     */
    public boolean getRdoRenewalAdding_Yes() {
        return this.rdoRenewalAdding_Yes;
    }

    /**
     * Setter for property rdoRenewalAdding_Yes.
     *
     * @param rdoRenewalAdding_Yes New value of property rdoRenewalAdding_Yes.
     */
    public void setRdoRenewalAdding_Yes(boolean rdoRenewalAdding_Yes) {
        this.rdoRenewalAdding_Yes = rdoRenewalAdding_Yes;
        setChanged();
    }

    /**
     * Getter for property rdoRenewalAdding_No.
     *
     * @return Value of property rdoRenewalAdding_No.
     */
    public boolean getRdoRenewalAdding_No() {
        return this.rdoRenewalAdding_No;
    }

    /**
     * Setter for property rdoRenewalAdding_No.
     *
     * @param rdoRenewalAdding_No New value of property rdoRenewalAdding_No.
     */
    public void setRdoRenewalAdding_No(boolean rdoRenewalAdding_No) {
        this.rdoRenewalAdding_No = rdoRenewalAdding_No;
        setChanged();
    }

    /**
     * Getter for property rdoRenewalWithdrawingInt_Yes.
     *
     * @return Value of property rdoRenewalWithdrawingInt_Yes.
     */
    public boolean getRdoRenewalWithdrawingInt_Yes() {
        return this.rdoRenewalWithdrawingInt_Yes;
    }

    /**
     * Setter for property rdoRenewalWithdrawingInt_Yes.
     *
     * @param rdoRenewalWithdrawingInt_Yes New value of property
     * rdoRenewalWithdrawingInt_Yes.
     */
    public void setRdoRenewalWithdrawingInt_Yes(boolean rdoRenewalWithdrawingInt_Yes) {
        this.rdoRenewalWithdrawingInt_Yes = rdoRenewalWithdrawingInt_Yes;
        setChanged();
    }

    /**
     * Getter for property rdoRenewalWithdrawingInt_No.
     *
     * @return Value of property rdoRenewalWithdrawingInt_No.
     */
    public boolean getRdoRenewalWithdrawingInt_No() {
        return this.rdoRenewalWithdrawingInt_No;
    }

    /**
     * Setter for property rdoRenewalWithdrawingInt_No.
     *
     * @param rdoRenewalWithdrawingInt_No New value of property
     * rdoRenewalWithdrawingInt_No.
     */
    public void setRdoRenewalWithdrawingInt_No(boolean rdoRenewalWithdrawingInt_No) {
        this.rdoRenewalWithdrawingInt_No = rdoRenewalWithdrawingInt_No;
        setChanged();
    }

    /**
     * Getter for property txtRenewalDepTransAmtValue.
     *
     * @return Value of property txtRenewalDepTransAmtValue.
     */
    public java.lang.String getTxtRenewalDepTransAmtValue() {
        return txtRenewalDepTransAmtValue;
    }

    /**
     * Setter for property txtRenewalDepTransAmtValue.
     *
     * @param txtRenewalDepTransAmtValue New value of property
     * txtRenewalDepTransAmtValue.
     */
    public void setTxtRenewalDepTransAmtValue(java.lang.String txtRenewalDepTransAmtValue) {
        this.txtRenewalDepTransAmtValue = txtRenewalDepTransAmtValue;
    }

    /**
     * Getter for property txtRenewalDepTransTokenNo.
     *
     * @return Value of property txtRenewalDepTransTokenNo.
     */
    public java.lang.String getTxtRenewalDepTransTokenNo() {
        return txtRenewalDepTransTokenNo;
    }

    /**
     * Setter for property txtRenewalDepTransTokenNo.
     *
     * @param txtRenewalDepTransTokenNo New value of property
     * txtRenewalDepTransTokenNo.
     */
    public void setTxtRenewalDepTransTokenNo(java.lang.String txtRenewalDepTransTokenNo) {
        this.txtRenewalDepTransTokenNo = txtRenewalDepTransTokenNo;
    }

    /**
     * Getter for property txtRenewalIntAmtValue.
     *
     * @return Value of property txtRenewalIntAmtValue.
     */
    public java.lang.String getTxtRenewalIntAmtValue() {
        return txtRenewalIntAmtValue;
    }

    /**
     * Setter for property txtRenewalIntAmtValue.
     *
     * @param txtRenewalIntAmtValue New value of property txtRenewalIntAmtValue.
     */
    public void setTxtRenewalIntAmtValue(java.lang.String txtRenewalIntAmtValue) {
        this.txtRenewalIntAmtValue = txtRenewalIntAmtValue;
    }

    /**
     * Getter for property txtRenewalIntTokenNoVal.
     *
     * @return Value of property txtRenewalIntTokenNoVal.
     */
    public java.lang.String getTxtRenewalIntTokenNoVal() {
        return txtRenewalIntTokenNoVal;
    }

    /**
     * Setter for property txtRenewalIntTokenNoVal.
     *
     * @param txtRenewalIntTokenNoVal New value of property
     * txtRenewalIntTokenNoVal.
     */
    public void setTxtRenewalIntTokenNoVal(java.lang.String txtRenewalIntTokenNoVal) {
        this.txtRenewalIntTokenNoVal = txtRenewalIntTokenNoVal;
    }

    /**
     * Getter for property renewalcustomerIdCrInt.
     *
     * @return Value of property renewalcustomerIdCrInt.
     */
    public java.lang.String getRenewalcustomerIdCrInt() {
        return renewalcustomerIdCrInt;
    }

    /**
     * Setter for property renewalcustomerIdCrInt.
     *
     * @param renewalcustomerIdCrInt New value of property
     * renewalcustomerIdCrInt.
     */
    public void setRenewalcustomerIdCrInt(java.lang.String renewalcustomerIdCrInt) {
        this.renewalcustomerIdCrInt = renewalcustomerIdCrInt;
    }

    /**
     * Getter for property renewalcustomerNameCrValueInt.
     *
     * @return Value of property renewalcustomerNameCrValueInt.
     */
    public java.lang.String getRenewalcustomerNameCrValueInt() {
        return renewalcustomerNameCrValueInt;
    }

    /**
     * Setter for property renewalcustomerNameCrValueInt.
     *
     * @param renewalcustomerNameCrValueInt New value of property
     * renewalcustomerNameCrValueInt.
     */
    public void setRenewalcustomerNameCrValueInt(java.lang.String renewalcustomerNameCrValueInt) {
        this.renewalcustomerNameCrValueInt = renewalcustomerNameCrValueInt;
    }

    /**
     * Getter for property renewalcustomerIdCrDep.
     *
     * @return Value of property renewalcustomerIdCrDep.
     */
    public java.lang.String getRenewalcustomerIdCrDep() {
        return renewalcustomerIdCrDep;
    }

    /**
     * Setter for property renewalcustomerIdCrDep.
     *
     * @param renewalcustomerIdCrDep New value of property
     * renewalcustomerIdCrDep.
     */
    public void setRenewalcustomerIdCrDep(java.lang.String renewalcustomerIdCrDep) {
        this.renewalcustomerIdCrDep = renewalcustomerIdCrDep;
    }

    /**
     * Getter for property renewalcustomerNameCrValueDep.
     *
     * @return Value of property renewalcustomerNameCrValueDep.
     */
    public java.lang.String getRenewalcustomerNameCrValueDep() {
        return renewalcustomerNameCrValueDep;
    }

    /**
     * Setter for property renewalcustomerNameCrValueDep.
     *
     * @param renewalcustomerNameCrValueDep New value of property
     * renewalcustomerNameCrValueDep.
     */
    public void setRenewalcustomerNameCrValueDep(java.lang.String renewalcustomerNameCrValueDep) {
        this.renewalcustomerNameCrValueDep = renewalcustomerNameCrValueDep;
    }

    /**
     * Getter for property cbmRenewalCalenderFreqDay.
     *
     * @return Value of property cbmRenewalCalenderFreqDay.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRenewalCalenderFreqDay() {
        return cbmRenewalCalenderFreqDay;
    }

    /**
     * Setter for property cbmRenewalCalenderFreqDay.
     *
     * @param cbmRenewalCalenderFreqDay New value of property
     * cbmRenewalCalenderFreqDay.
     */
    public void setCbmRenewalCalenderFreqDay(com.see.truetransact.clientutil.ComboBoxModel cbmRenewalCalenderFreqDay) {
        this.cbmRenewalCalenderFreqDay = cbmRenewalCalenderFreqDay;
    }

    /**
     * Getter for property cboRenewalCalenderFreqDay.
     *
     * @return Value of property cboRenewalCalenderFreqDay.
     */
    public java.lang.String getCboRenewalCalenderFreqDay() {
        return cboRenewalCalenderFreqDay;
    }

    /**
     * Setter for property cboRenewalCalenderFreqDay.
     *
     * @param cboRenewalCalenderFreqDay New value of property
     * cboRenewalCalenderFreqDay.
     */
    public void setCboRenewalCalenderFreqDay(java.lang.String cboRenewalCalenderFreqDay) {
        this.cboRenewalCalenderFreqDay = cboRenewalCalenderFreqDay;
    }

    /**
     * Getter for property rdoRenewalCalenderFreq_Yes.
     *
     * @return Value of property rdoRenewalCalenderFreq_Yes.
     */
    public boolean getRdoRenewalCalenderFreq_Yes() {
        return rdoRenewalCalenderFreq_Yes;
    }

    /**
     * Setter for property rdoRenewalCalenderFreq_Yes.
     *
     * @param rdoRenewalCalenderFreq_Yes New value of property
     * rdoRenewalCalenderFreq_Yes.
     */
    public void setRdoRenewalCalenderFreq_Yes(boolean rdoRenewalCalenderFreq_Yes) {
        this.rdoRenewalCalenderFreq_Yes = rdoRenewalCalenderFreq_Yes;
    }

    /**
     * Getter for property rdoRenewalCalenderFreq_No.
     *
     * @return Value of property rdoRenewalCalenderFreq_No.
     */
    public boolean getRdoRenewalCalenderFreq_No() {
        return rdoRenewalCalenderFreq_No;
    }

    /**
     * Setter for property rdoRenewalCalenderFreq_No.
     *
     * @param rdoRenewalCalenderFreq_No New value of property
     * rdoRenewalCalenderFreq_No.
     */
    public void setRdoRenewalCalenderFreq_No(boolean rdoRenewalCalenderFreq_No) {
        this.rdoRenewalCalenderFreq_No = rdoRenewalCalenderFreq_No;
    }

    /**
     * Getter for property txtRenewalPrintedOption.
     *
     * @return Value of property txtRenewalPrintedOption.
     */
    public java.lang.String getTxtRenewalPrintedOption() {
        return txtRenewalPrintedOption;
    }

    /**
     * Setter for property txtRenewalPrintedOption.
     *
     * @param txtRenewalPrintedOption New value of property
     * txtRenewalPrintedOption.
     */
    public void setTxtRenewalPrintedOption(java.lang.String txtRenewalPrintedOption) {
        this.txtRenewalPrintedOption = txtRenewalPrintedOption;
    }

    /**
     * Getter for property rdoOpeningMode_Extension.
     *
     * @return Value of property rdoOpeningMode_Extension.
     */
    public boolean getRdoOpeningMode_Extension() {
        return rdoOpeningMode_Extension;
    }

    /**
     * Setter for property rdoOpeningMode_Extension.
     *
     * @param rdoOpeningMode_Extension New value of property
     * rdoOpeningMode_Extension.
     */
    public void setRdoOpeningMode_Extension(boolean rdoOpeningMode_Extension) {
        this.rdoOpeningMode_Extension = rdoOpeningMode_Extension;
    }

    /**
     * Getter for property cbmExtensionCalenderFreqDay.
     *
     * @return Value of property cbmExtensionCalenderFreqDay.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmExtensionCalenderFreqDay() {
        return cbmExtensionCalenderFreqDay;
    }

    /**
     * Setter for property cbmExtensionCalenderFreqDay.
     *
     * @param cbmExtensionCalenderFreqDay New value of property
     * cbmExtensionCalenderFreqDay.
     */
    public void setCbmExtensionCalenderFreqDay(com.see.truetransact.clientutil.ComboBoxModel cbmExtensionCalenderFreqDay) {
        this.cbmExtensionCalenderFreqDay = cbmExtensionCalenderFreqDay;
    }

    /**
     * Getter for property txtExtensionPrintedOption.
     *
     * @return Value of property txtExtensionPrintedOption.
     */
    public java.lang.String getTxtExtensionPrintedOption() {
        return txtExtensionPrintedOption;
    }

    /**
     * Setter for property txtExtensionPrintedOption.
     *
     * @param txtExtensionPrintedOption New value of property
     * txtExtensionPrintedOption.
     */
    public void setTxtExtensionPrintedOption(java.lang.String txtExtensionPrintedOption) {
        this.txtExtensionPrintedOption = txtExtensionPrintedOption;
    }

    /**
     * Getter for property cboExtensionCalenderFreqDay.
     *
     * @return Value of property cboExtensionCalenderFreqDay.
     */
    public java.lang.String getCboExtensionCalenderFreqDay() {
        return cboExtensionCalenderFreqDay;
    }

    /**
     * Setter for property cboExtensionCalenderFreqDay.
     *
     * @param cboExtensionCalenderFreqDay New value of property
     * cboExtensionCalenderFreqDay.
     */
    public void setCboExtensionCalenderFreqDay(java.lang.String cboExtensionCalenderFreqDay) {
        this.cboExtensionCalenderFreqDay = cboExtensionCalenderFreqDay;
    }

    /**
     * Getter for property rdoExtensionWithdrawing_Yes.
     *
     * @return Value of property rdoExtensionWithdrawing_Yes.
     */
    public boolean getRdoExtensionWithdrawing_Yes() {
        return this.rdoExtensionWithdrawing_Yes;
    }

    /**
     * Setter for property rdoExtensionWithdrawing_Yes.
     *
     * @param rdoExtensionWithdrawing_Yes New value of property
     * rdoExtensionWithdrawing_Yes.
     */
    public void setRdoExtensionWithdrawing_Yes(boolean rdoExtensionWithdrawing_Yes) {
        this.rdoExtensionWithdrawing_Yes = rdoExtensionWithdrawing_Yes;
        setChanged();
    }

    /**
     * Getter for property rdoExtensionWithdrawing_No.
     *
     * @return Value of property rdoExtensionWithdrawing_No.
     */
    public boolean getRdoExtensionWithdrawing_No() {
        return this.rdoExtensionWithdrawing_No;
    }

    /**
     * Setter for property rdoExtensionWithdrawing_No.
     *
     * @param rdoExtensionWithdrawing_No New value of property
     * rdoExtensionWithdrawing_No.
     */
    public void setRdoExtensionWithdrawing_No(boolean rdoExtensionWithdrawing_No) {
        this.rdoExtensionWithdrawing_No = rdoExtensionWithdrawing_No;
        setChanged();
    }

    /**
     * Getter for property rdoExtensionAdding_Yes.
     *
     * @return Value of property rdoExtensionAdding_Yes.
     */
    public boolean getRdoExtensionAdding_Yes() {
        return this.rdoExtensionAdding_Yes;
    }

    /**
     * Setter for property rdoExtensionAdding_Yes.
     *
     * @param rdoExtensionAdding_Yes New value of property
     * rdoExtensionAdding_Yes.
     */
    public void setRdoExtensionAdding_Yes(boolean rdoExtensionAdding_Yes) {
        this.rdoExtensionAdding_Yes = rdoExtensionAdding_Yes;
        setChanged();
    }

    /**
     * Getter for property rdoExtensionAdding_No.
     *
     * @return Value of property rdoExtensionAdding_No.
     */
    public boolean getRdoExtensionAdding_No() {
        return this.rdoExtensionAdding_No;
    }

    /**
     * Setter for property rdoExtensionAdding_No.
     *
     * @param rdoExtensionAdding_No New value of property rdoExtensionAdding_No.
     */
    public void setRdoExtensionAdding_No(boolean rdoExtensionAdding_No) {
        this.rdoExtensionAdding_No = rdoExtensionAdding_No;
        setChanged();
    }

    /**
     * Getter for property rdoExtensionWithdrawingInt_Yes.
     *
     * @return Value of property rdoExtensionWithdrawingInt_Yes.
     */
    public boolean getRdoExtensionWithdrawingInt_Yes() {
        return this.rdoExtensionWithdrawingInt_Yes;
    }

    /**
     * Setter for property rdoExtensionWithdrawingInt_Yes.
     *
     * @param rdoExtensionWithdrawingInt_Yes New value of property
     * rdoExtensionWithdrawingInt_Yes.
     */
    public void setRdoExtensionWithdrawingInt_Yes(boolean rdoExtensionWithdrawingInt_Yes) {
        this.rdoExtensionWithdrawingInt_Yes = rdoExtensionWithdrawingInt_Yes;
        setChanged();
    }

    /**
     * Getter for property rdoExtensionWithdrawingInt_No.
     *
     * @return Value of property rdoExtensionWithdrawingInt_No.
     */
    public boolean getRdoExtensionWithdrawingInt_No() {
        return this.rdoExtensionWithdrawingInt_No;
    }

    /**
     * Setter for property rdoExtensionWithdrawingInt_No.
     *
     * @param rdoExtensionWithdrawingInt_No New value of property
     * rdoExtensionWithdrawingInt_No.
     */
    public void setRdoExtensionWithdrawingInt_No(boolean rdoExtensionWithdrawingInt_No) {
        this.rdoExtensionWithdrawingInt_No = rdoExtensionWithdrawingInt_No;
        setChanged();
    }

    /**
     * Getter for property rdoExtensionCalenderFreq_Yes.
     *
     * @return Value of property rdoExtensionCalenderFreq_Yes.
     */
    public boolean getRdoExtensionCalenderFreq_Yes() {
        return this.rdoExtensionCalenderFreq_Yes;
    }

    /**
     * Setter for property rdoExtensionCalenderFreq_Yes.
     *
     * @param rdoExtensionCalenderFreq_Yes New value of property
     * rdoExtensionCalenderFreq_Yes.
     */
    public void setRdoExtensionCalenderFreq_Yes(boolean rdoExtensionCalenderFreq_Yes) {
        this.rdoExtensionCalenderFreq_Yes = rdoExtensionCalenderFreq_Yes;
        setChanged();
    }

    /**
     * Getter for property rdoExtensionCalenderFreq_No.
     *
     * @return Value of property rdoExtensionCalenderFreq_No.
     */
    public boolean getRdoExtensionCalenderFreq_No() {
        return this.rdoExtensionCalenderFreq_No;
    }

    /**
     * Setter for property rdoExtensionCalenderFreq_No.
     *
     * @param rdoExtensionCalenderFreq_No New value of property
     * rdoExtensionCalenderFreq_No.
     */
    public void setRdoExtensionCalenderFreq_No(boolean rdoExtensionCalenderFreq_No) {
        this.rdoExtensionCalenderFreq_No = rdoExtensionCalenderFreq_No;
        setChanged();
    }

    /**
     * Getter for property ExtensionBalIntAmtVal.
     *
     * @return Value of property ExtensionBalIntAmtVal.
     */
    public java.lang.String getExtensionBalIntAmtVal() {
        return ExtensionBalIntAmtVal;
    }

    /**
     * Setter for property ExtensionBalIntAmtVal.
     *
     * @param ExtensionBalIntAmtVal New value of property ExtensionBalIntAmtVal.
     */
    public void setExtensionBalIntAmtVal(java.lang.String ExtensionBalIntAmtVal) {
        this.ExtensionBalIntAmtVal = ExtensionBalIntAmtVal;
    }

    /**
     * Getter for property ExtensionDepDate.
     *
     * @return Value of property ExtensionDepDate.
     */
    public java.lang.String getExtensionDepDate() {
        return ExtensionDepDate;
    }

    /**
     * Setter for property ExtensionDepDate.
     *
     * @param ExtensionDepDate New value of property ExtensionDepDate.
     */
    public void setExtensionDepDate(java.lang.String ExtensionDepDate) {
        this.ExtensionDepDate = ExtensionDepDate;
    }

    /**
     * Getter for property ExtensiontdtDateOfDeposit.
     *
     * @return Value of property ExtensiontdtDateOfDeposit.
     */
    public java.lang.String getExtensiontdtDateOfDeposit() {
        return ExtensiontdtDateOfDeposit;
    }

    /**
     * Setter for property ExtensiontdtDateOfDeposit.
     *
     * @param ExtensiontdtDateOfDeposit New value of property
     * ExtensiontdtDateOfDeposit.
     */
    public void setExtensiontdtDateOfDeposit(java.lang.String ExtensiontdtDateOfDeposit) {
        this.ExtensiontdtDateOfDeposit = ExtensiontdtDateOfDeposit;
    }

    /**
     * Getter for property ExtensiontxtPeriodOfDeposit_Years.
     *
     * @return Value of property ExtensiontxtPeriodOfDeposit_Years.
     */
    public java.lang.String getExtensiontxtPeriodOfDeposit_Years() {
        return ExtensiontxtPeriodOfDeposit_Years;
    }

    /**
     * Setter for property ExtensiontxtPeriodOfDeposit_Years.
     *
     * @param ExtensiontxtPeriodOfDeposit_Years New value of property
     * ExtensiontxtPeriodOfDeposit_Years.
     */
    public void setExtensiontxtPeriodOfDeposit_Years(java.lang.String ExtensiontxtPeriodOfDeposit_Years) {
        this.ExtensiontxtPeriodOfDeposit_Years = ExtensiontxtPeriodOfDeposit_Years;
    }

    /**
     * Getter for property ExtensiontxtPeriodOfDeposit_Months.
     *
     * @return Value of property ExtensiontxtPeriodOfDeposit_Months.
     */
    public java.lang.String getExtensiontxtPeriodOfDeposit_Months() {
        return ExtensiontxtPeriodOfDeposit_Months;
    }

    /**
     * Setter for property ExtensiontxtPeriodOfDeposit_Months.
     *
     * @param ExtensiontxtPeriodOfDeposit_Months New value of property
     * ExtensiontxtPeriodOfDeposit_Months.
     */
    public void setExtensiontxtPeriodOfDeposit_Months(java.lang.String ExtensiontxtPeriodOfDeposit_Months) {
        this.ExtensiontxtPeriodOfDeposit_Months = ExtensiontxtPeriodOfDeposit_Months;
    }

    /**
     * Getter for property ExtensiontxtPeriodOfDeposit_Days.
     *
     * @return Value of property ExtensiontxtPeriodOfDeposit_Days.
     */
    public java.lang.String getExtensiontxtPeriodOfDeposit_Days() {
        return ExtensiontxtPeriodOfDeposit_Days;
    }

    /**
     * Setter for property ExtensiontxtPeriodOfDeposit_Days.
     *
     * @param ExtensiontxtPeriodOfDeposit_Days New value of property
     * ExtensiontxtPeriodOfDeposit_Days.
     */
    public void setExtensiontxtPeriodOfDeposit_Days(java.lang.String ExtensiontxtPeriodOfDeposit_Days) {
        this.ExtensiontxtPeriodOfDeposit_Days = ExtensiontxtPeriodOfDeposit_Days;
    }

    /**
     * Getter for property ExtensiontdtMaturityDate.
     *
     * @return Value of property ExtensiontdtMaturityDate.
     */
    public java.lang.String getExtensiontdtMaturityDate() {
        return ExtensiontdtMaturityDate;
    }

    /**
     * Setter for property ExtensiontdtMaturityDate.
     *
     * @param ExtensiontdtMaturityDate New value of property
     * ExtensiontdtMaturityDate.
     */
    public void setExtensiontdtMaturityDate(java.lang.String ExtensiontdtMaturityDate) {
        this.ExtensiontdtMaturityDate = ExtensiontdtMaturityDate;
    }

    /**
     * Getter for property ExtensiontxtDepositAmount.
     *
     * @return Value of property ExtensiontxtDepositAmount.
     */
    public java.lang.String getExtensiontxtDepositAmount() {
        return ExtensiontxtDepositAmount;
    }

    /**
     * Setter for property ExtensiontxtDepositAmount.
     *
     * @param ExtensiontxtDepositAmount New value of property
     * ExtensiontxtDepositAmount.
     */
    public void setExtensiontxtDepositAmount(java.lang.String ExtensiontxtDepositAmount) {
        this.ExtensiontxtDepositAmount = ExtensiontxtDepositAmount;
    }

    /**
     * Getter for property ExtensiontxtRateOfInterest.
     *
     * @return Value of property ExtensiontxtRateOfInterest.
     */
    public java.lang.String getExtensiontxtRateOfInterest() {
        return ExtensiontxtRateOfInterest;
    }

    /**
     * Setter for property ExtensiontxtRateOfInterest.
     *
     * @param ExtensiontxtRateOfInterest New value of property
     * ExtensiontxtRateOfInterest.
     */
    public void setExtensiontxtRateOfInterest(java.lang.String ExtensiontxtRateOfInterest) {
        this.ExtensiontxtRateOfInterest = ExtensiontxtRateOfInterest;
    }

    /**
     * Getter for property ExtensiontxtMaturityAmount.
     *
     * @return Value of property ExtensiontxtMaturityAmount.
     */
    public java.lang.String getExtensiontxtMaturityAmount() {
        return ExtensiontxtMaturityAmount;
    }

    /**
     * Setter for property ExtensiontxtMaturityAmount.
     *
     * @param ExtensiontxtMaturityAmount New value of property
     * ExtensiontxtMaturityAmount.
     */
    public void setExtensiontxtMaturityAmount(java.lang.String ExtensiontxtMaturityAmount) {
        this.ExtensiontxtMaturityAmount = ExtensiontxtMaturityAmount;
    }

    /**
     * Getter for property ExtensiontxtTotalInterestAmount.
     *
     * @return Value of property ExtensiontxtTotalInterestAmount.
     */
    public java.lang.String getExtensiontxtTotalInterestAmount() {
        return ExtensiontxtTotalInterestAmount;
    }

    /**
     * Setter for property ExtensiontxtTotalInterestAmount.
     *
     * @param ExtensiontxtTotalInterestAmount New value of property
     * ExtensiontxtTotalInterestAmount.
     */
    public void setExtensiontxtTotalInterestAmount(java.lang.String ExtensiontxtTotalInterestAmount) {
        this.ExtensiontxtTotalInterestAmount = ExtensiontxtTotalInterestAmount;
    }

    /**
     * Getter for property ExtensiontxtPeriodicInterestAmount.
     *
     * @return Value of property ExtensiontxtPeriodicInterestAmount.
     */
    public java.lang.String getExtensiontxtPeriodicInterestAmount() {
        return ExtensiontxtPeriodicInterestAmount;
    }

    /**
     * Setter for property ExtensiontxtPeriodicInterestAmount.
     *
     * @param ExtensiontxtPeriodicInterestAmount New value of property
     * ExtensiontxtPeriodicInterestAmount.
     */
    public void setExtensiontxtPeriodicInterestAmount(java.lang.String ExtensiontxtPeriodicInterestAmount) {
        this.ExtensiontxtPeriodicInterestAmount = ExtensiontxtPeriodicInterestAmount;
    }

    /**
     * Getter for property ExtensioncboInterestPaymentMode.
     *
     * @return Value of property ExtensioncboInterestPaymentMode.
     */
    public java.lang.String getExtensioncboInterestPaymentMode() {
        return ExtensioncboInterestPaymentMode;
    }

    /**
     * Setter for property ExtensioncboInterestPaymentMode.
     *
     * @param ExtensioncboInterestPaymentMode New value of property
     * ExtensioncboInterestPaymentMode.
     */
    public void setExtensioncboInterestPaymentMode(java.lang.String ExtensioncboInterestPaymentMode) {
        this.ExtensioncboInterestPaymentMode = ExtensioncboInterestPaymentMode;
    }

    /**
     * Getter for property ExtensioncboInterestPaymentFrequency.
     *
     * @return Value of property ExtensioncboInterestPaymentFrequency.
     */
    public java.lang.String getExtensioncboInterestPaymentFrequency() {
        return ExtensioncboInterestPaymentFrequency;
    }

    /**
     * Setter for property ExtensioncboInterestPaymentFrequency.
     *
     * @param ExtensioncboInterestPaymentFrequency New value of property
     * ExtensioncboInterestPaymentFrequency.
     */
    public void setExtensioncboInterestPaymentFrequency(java.lang.String ExtensioncboInterestPaymentFrequency) {
        this.ExtensioncboInterestPaymentFrequency = ExtensioncboInterestPaymentFrequency;
    }

    /**
     * Getter for property ExtensioncboCalenderFreq.
     *
     * @return Value of property ExtensioncboCalenderFreq.
     */
    public java.lang.String getExtensioncboCalenderFreq() {
        return ExtensioncboCalenderFreq;
    }

    /**
     * Setter for property ExtensioncboCalenderFreq.
     *
     * @param ExtensioncboCalenderFreq New value of property
     * ExtensioncboCalenderFreq.
     */
    public void setExtensioncboCalenderFreq(java.lang.String ExtensioncboCalenderFreq) {
        this.ExtensioncboCalenderFreq = ExtensioncboCalenderFreq;
    }

    /**
     * Getter for property ExtensioncboProdId.
     *
     * @return Value of property ExtensioncboProdId.
     */
    public java.lang.String getExtensioncboProdId() {
        return ExtensioncboProdId;
    }

    /**
     * Setter for property ExtensioncboProdId.
     *
     * @param ExtensioncboProdId New value of property ExtensioncboProdId.
     */
    public void setExtensioncboProdId(java.lang.String ExtensioncboProdId) {
        this.ExtensioncboProdId = ExtensioncboProdId;
    }

    /**
     * Getter for property ExtensioncboProdType.
     *
     * @return Value of property ExtensioncboProdType.
     */
    public java.lang.String getExtensioncboProdType() {
        return ExtensioncboProdType;
    }

    /**
     * Setter for property ExtensioncboProdType.
     *
     * @param ExtensioncboProdType New value of property ExtensioncboProdType.
     */
    public void setExtensioncboProdType(java.lang.String ExtensioncboProdType) {
        this.ExtensioncboProdType = ExtensioncboProdType;
    }

    /**
     * Getter for property ExtensioncustomerIdCr.
     *
     * @return Value of property ExtensioncustomerIdCr.
     */
    public java.lang.String getExtensioncustomerIdCr() {
        return ExtensioncustomerIdCr;
    }

    /**
     * Setter for property ExtensioncustomerIdCr.
     *
     * @param ExtensioncustomerIdCr New value of property ExtensioncustomerIdCr.
     */
    public void setExtensioncustomerIdCr(java.lang.String ExtensioncustomerIdCr) {
        this.ExtensioncustomerIdCr = ExtensioncustomerIdCr;
    }

    /**
     * Getter for property ExtensioncustomerNameCrValue.
     *
     * @return Value of property ExtensioncustomerNameCrValue.
     */
    public java.lang.String getExtensioncustomerNameCrValue() {
        return ExtensioncustomerNameCrValue;
    }

    /**
     * Setter for property ExtensioncustomerNameCrValue.
     *
     * @param ExtensioncustomerNameCrValue New value of property
     * ExtensioncustomerNameCrValue.
     */
    public void setExtensioncustomerNameCrValue(java.lang.String ExtensioncustomerNameCrValue) {
        this.ExtensioncustomerNameCrValue = ExtensioncustomerNameCrValue;
    }

    /**
     * Getter for property ExtensioncustomerIdCrInt.
     *
     * @return Value of property ExtensioncustomerIdCrInt.
     */
    public java.lang.String getExtensioncustomerIdCrInt() {
        return ExtensioncustomerIdCrInt;
    }

    /**
     * Setter for property ExtensioncustomerIdCrInt.
     *
     * @param ExtensioncustomerIdCrInt New value of property
     * ExtensioncustomerIdCrInt.
     */
    public void setExtensioncustomerIdCrInt(java.lang.String ExtensioncustomerIdCrInt) {
        this.ExtensioncustomerIdCrInt = ExtensioncustomerIdCrInt;
    }

    /**
     * Getter for property ExtensioncustomerNameCrValueInt.
     *
     * @return Value of property ExtensioncustomerNameCrValueInt.
     */
    public java.lang.String getExtensioncustomerNameCrValueInt() {
        return ExtensioncustomerNameCrValueInt;
    }

    /**
     * Setter for property ExtensioncustomerNameCrValueInt.
     *
     * @param ExtensioncustomerNameCrValueInt New value of property
     * ExtensioncustomerNameCrValueInt.
     */
    public void setExtensioncustomerNameCrValueInt(java.lang.String ExtensioncustomerNameCrValueInt) {
        this.ExtensioncustomerNameCrValueInt = ExtensioncustomerNameCrValueInt;
    }

    /**
     * Getter for property ExtensioncustomerIdCrDep.
     *
     * @return Value of property ExtensioncustomerIdCrDep.
     */
    public java.lang.String getExtensioncustomerIdCrDep() {
        return ExtensioncustomerIdCrDep;
    }

    /**
     * Setter for property ExtensioncustomerIdCrDep.
     *
     * @param ExtensioncustomerIdCrDep New value of property
     * ExtensioncustomerIdCrDep.
     */
    public void setExtensioncustomerIdCrDep(java.lang.String ExtensioncustomerIdCrDep) {
        this.ExtensioncustomerIdCrDep = ExtensioncustomerIdCrDep;
    }

    /**
     * Getter for property ExtensioncustomerNameCrValueDep.
     *
     * @return Value of property ExtensioncustomerNameCrValueDep.
     */
    public java.lang.String getExtensioncustomerNameCrValueDep() {
        return ExtensioncustomerNameCrValueDep;
    }

    /**
     * Setter for property ExtensioncustomerNameCrValueDep.
     *
     * @param ExtensioncustomerNameCrValueDep New value of property
     * ExtensioncustomerNameCrValueDep.
     */
    public void setExtensioncustomerNameCrValueDep(java.lang.String ExtensioncustomerNameCrValueDep) {
        this.ExtensioncustomerNameCrValueDep = ExtensioncustomerNameCrValueDep;
    }

    /**
     * Getter for property cboExtensionDepositProdId.
     *
     * @return Value of property cboExtensionDepositProdId.
     */
    public java.lang.String getCboExtensionDepositProdId() {
        return cboExtensionDepositProdId;
    }

    /**
     * Setter for property cboExtensionDepositProdId.
     *
     * @param cboExtensionDepositProdId New value of property
     * cboExtensionDepositProdId.
     */
    public void setCboExtensionDepositProdId(java.lang.String cboExtensionDepositProdId) {
        this.cboExtensionDepositProdId = cboExtensionDepositProdId;
    }

    /**
     * Getter for property cbmExtensionDepositProdId.
     *
     * @return Value of property cbmExtensionDepositProdId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmExtensionDepositProdId() {
        return cbmExtensionDepositProdId;
    }

    /**
     * Setter for property cbmExtensionDepositProdId.
     *
     * @param cbmExtensionDepositProdId New value of property
     * cbmExtensionDepositProdId.
     */
    public void setCbmExtensionDepositProdId(com.see.truetransact.clientutil.ComboBoxModel cbmExtensionDepositProdId) {
        this.cbmExtensionDepositProdId = cbmExtensionDepositProdId;
    }

    /**
     * Getter for property cboExtensionDepositCategory.
     *
     * @return Value of property cboExtensionDepositCategory.
     */
    public java.lang.String getCboExtensionDepositCategory() {
        return cboExtensionDepositCategory;
    }

    /**
     * Setter for property cboExtensionDepositCategory.
     *
     * @param cboExtensionDepositCategory New value of property
     * cboExtensionDepositCategory.
     */
    public void setCboExtensionDepositCategory(java.lang.String cboExtensionDepositCategory) {
        this.cboExtensionDepositCategory = cboExtensionDepositCategory;
    }

    /**
     * Getter for property cbmExtensionDepositCategory.
     *
     * @return Value of property cbmExtensionDepositCategory.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmExtensionDepositCategory() {
        return cbmExtensionDepositCategory;
    }

    /**
     * Setter for property cbmExtensionDepositCategory.
     *
     * @param cbmExtensionDepositCategory New value of property
     * cbmExtensionDepositCategory.
     */
    public void setCbmExtensionDepositCategory(com.see.truetransact.clientutil.ComboBoxModel cbmExtensionDepositCategory) {
        this.cbmExtensionDepositCategory = cbmExtensionDepositCategory;
    }

    /**
     * Getter for property ExtensionRateOfIntVal.
     *
     * @return Value of property ExtensionRateOfIntVal.
     */
    public java.lang.String getExtensionRateOfIntVal() {
        return ExtensionRateOfIntVal;
    }

    /**
     * Setter for property ExtensionRateOfIntVal.
     *
     * @param ExtensionRateOfIntVal New value of property ExtensionRateOfIntVal.
     */
    public void setExtensionRateOfIntVal(java.lang.String ExtensionRateOfIntVal) {
        this.ExtensionRateOfIntVal = ExtensionRateOfIntVal;
    }

    /**
     * Getter for property ExtensionCurrDt.
     *
     * @return Value of property ExtensionCurrDt.
     */
    public java.lang.String getExtensionCurrDt() {
        return ExtensionCurrDt;
    }

    /**
     * Setter for property ExtensionCurrDt.
     *
     * @param ExtensionCurrDt New value of property ExtensionCurrDt.
     */
    public void setExtensionCurrDt(java.lang.String ExtensionCurrDt) {
        this.ExtensionCurrDt = ExtensionCurrDt;
    }

    /**
     * Getter for property tblExtensionDepSubNoAccInfo.
     *
     * @return Value of property tblExtensionDepSubNoAccInfo.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblExtensionDepSubNoAccInfo() {
        return tblExtensionDepSubNoAccInfo;
    }

    /**
     * Setter for property tblExtensionDepSubNoAccInfo.
     *
     * @param tblExtensionDepSubNoAccInfo New value of property
     * tblExtensionDepSubNoAccInfo.
     */
    public void setTblExtensionDepSubNoAccInfo(com.see.truetransact.clientutil.EnhancedTableModel tblExtensionDepSubNoAccInfo) {
        this.tblExtensionDepSubNoAccInfo = tblExtensionDepSubNoAccInfo;
    }

    /**
     * Getter for property renewalValDepositSubNo.
     *
     * @return Value of property renewalValDepositSubNo.
     */
    public java.lang.String getRenewalValDepositSubNo() {
        return renewalValDepositSubNo;
    }

    /**
     * Setter for property renewalValDepositSubNo.
     *
     * @param renewalValDepositSubNo New value of property
     * renewalValDepositSubNo.
     */
    public void setRenewalValDepositSubNo(java.lang.String renewalValDepositSubNo) {
        this.renewalValDepositSubNo = renewalValDepositSubNo;
    }

    /**
     * Getter for property renewalBalIntAmt.
     *
     * @return Value of property renewalBalIntAmt.
     */
    public java.lang.String getRenewalBalIntAmt() {
        return renewalBalIntAmt;
    }

    /**
     * Setter for property renewalBalIntAmt.
     *
     * @param renewalBalIntAmt New value of property renewalBalIntAmt.
     */
    public void setRenewalBalIntAmt(java.lang.String renewalBalIntAmt) {
        this.renewalBalIntAmt = renewalBalIntAmt;
    }

    /**
     * Getter for property ExtensionPaymentFreqValue.
     *
     * @return Value of property ExtensionPaymentFreqValue.
     */
    public java.lang.String getExtensionPaymentFreqValue() {
        return ExtensionPaymentFreqValue;
    }

    /**
     * Setter for property ExtensionPaymentFreqValue.
     *
     * @param ExtensionPaymentFreqValue New value of property
     * ExtensionPaymentFreqValue.
     */
    public void setExtensionPaymentFreqValue(java.lang.String ExtensionPaymentFreqValue) {
        this.ExtensionPaymentFreqValue = ExtensionPaymentFreqValue;
    }

    /**
     * Getter for property ExtensionActualPeriodRun.
     *
     * @return Value of property ExtensionActualPeriodRun.
     */
    public java.lang.String getExtensionActualPeriodRun() {
        return ExtensionActualPeriodRun;
    }

    /**
     * Setter for property ExtensionActualPeriodRun.
     *
     * @param ExtensionActualPeriodRun New value of property
     * ExtensionActualPeriodRun.
     */
    public void setExtensionActualPeriodRun(java.lang.String ExtensionActualPeriodRun) {
        this.ExtensionActualPeriodRun = ExtensionActualPeriodRun;
    }

    /**
     * Getter for property renewalClosedDepNo.
     *
     * @return Value of property renewalClosedDepNo.
     */
    public java.lang.String getRenewalClosedDepNo() {
        return renewalClosedDepNo;
    }

    /**
     * Setter for property renewalClosedDepNo.
     *
     * @param renewalClosedDepNo New value of property renewalClosedDepNo.
     */
    public void setRenewalClosedDepNo(java.lang.String renewalClosedDepNo) {
        this.renewalClosedDepNo = renewalClosedDepNo;
    }

    /**
     * Getter for property ExtensionBalInterestAmtVal.
     *
     * @return Value of property ExtensionBalInterestAmtVal.
     */
    public java.lang.String getExtensionBalInterestAmtVal() {
        return ExtensionBalInterestAmtVal;
    }

    /**
     * Setter for property ExtensionBalInterestAmtVal.
     *
     * @param ExtensionBalInterestAmtVal New value of property
     * ExtensionBalInterestAmtVal.
     */
    public void setExtensionBalInterestAmtVal(java.lang.String ExtensionBalInterestAmtVal) {
        this.ExtensionBalInterestAmtVal = ExtensionBalInterestAmtVal;
    }

    /**
     * Getter for property ExtensionWithdrawIntAmtVal.
     *
     * @return Value of property ExtensionWithdrawIntAmtVal.
     */
    public java.lang.String getExtensionWithdrawIntAmtVal() {
        return ExtensionWithdrawIntAmtVal;
    }

    /**
     * Setter for property ExtensionWithdrawIntAmtVal.
     *
     * @param ExtensionWithdrawIntAmtVal New value of property
     * ExtensionWithdrawIntAmtVal.
     */
    public void setExtensionWithdrawIntAmtVal(java.lang.String ExtensionWithdrawIntAmtVal) {
        this.ExtensionWithdrawIntAmtVal = ExtensionWithdrawIntAmtVal;
    }

    /**
     * Getter for property extensionAlreadyWithdrawn.
     *
     * @return Value of property extensionAlreadyWithdrawn.
     */
    public java.lang.String getExtensionAlreadyWithdrawn() {
        return extensionAlreadyWithdrawn;
    }

    /**
     * Setter for property extensionAlreadyWithdrawn.
     *
     * @param extensionAlreadyWithdrawn New value of property
     * extensionAlreadyWithdrawn.
     */
    public void setExtensionAlreadyWithdrawn(java.lang.String extensionAlreadyWithdrawn) {
        this.extensionAlreadyWithdrawn = extensionAlreadyWithdrawn;
    }

    /**
     * Getter for property extensionAlreadyCredited.
     *
     * @return Value of property extensionAlreadyCredited.
     */
    public java.lang.String getExtensionAlreadyCredited() {
        return extensionAlreadyCredited;
    }

    /**
     * Setter for property extensionAlreadyCredited.
     *
     * @param extensionAlreadyCredited New value of property
     * extensionAlreadyCredited.
     */
    public void setExtensionAlreadyCredited(java.lang.String extensionAlreadyCredited) {
        this.extensionAlreadyCredited = extensionAlreadyCredited;
    }

    /**
     * Getter for property extensionTotalIntAmt.
     *
     * @return Value of property extensionTotalIntAmt.
     */
    public java.lang.String getExtensionTotalIntAmt() {
        return extensionTotalIntAmt;
    }

    /**
     * Setter for property extensionTotalIntAmt.
     *
     * @param extensionTotalIntAmt New value of property extensionTotalIntAmt.
     */
    public void setExtensionTotalIntAmt(java.lang.String extensionTotalIntAmt) {
        this.extensionTotalIntAmt = extensionTotalIntAmt;
    }

    /**
     * Getter for property txtExtensionTransAmtValue.
     *
     * @return Value of property txtExtensionTransAmtValue.
     */
    public java.lang.String getTxtExtensionTransAmtValue() {
        return txtExtensionTransAmtValue;
    }

    /**
     * Setter for property txtExtensionTransAmtValue.
     *
     * @param txtExtensionTransAmtValue New value of property
     * txtExtensionTransAmtValue.
     */
    public void setTxtExtensionTransAmtValue(java.lang.String txtExtensionTransAmtValue) {
        this.txtExtensionTransAmtValue = txtExtensionTransAmtValue;
    }

    /**
     * Getter for property txtExtensionTransTokenNo.
     *
     * @return Value of property txtExtensionTransTokenNo.
     */
    public java.lang.String getTxtExtensionTransTokenNo() {
        return txtExtensionTransTokenNo;
    }

    /**
     * Setter for property txtExtensionTransTokenNo.
     *
     * @param txtExtensionTransTokenNo New value of property
     * txtExtensionTransTokenNo.
     */
    public void setTxtExtensionTransTokenNo(java.lang.String txtExtensionTransTokenNo) {
        this.txtExtensionTransTokenNo = txtExtensionTransTokenNo;
    }

    /**
     * Getter for property txtExtensionTransAccNo.
     *
     * @return Value of property txtExtensionTransAccNo.
     */
    public java.lang.String getTxtExtensionTransAccNo() {
        return txtExtensionTransAccNo;
    }

    /**
     * Setter for property txtExtensionTransAccNo.
     *
     * @param txtExtensionTransAccNo New value of property
     * txtExtensionTransAccNo.
     */
    public void setTxtExtensionTransAccNo(java.lang.String txtExtensionTransAccNo) {
        this.txtExtensionTransAccNo = txtExtensionTransAccNo;
    }

    /**
     * Getter for property cbmExtensionTransMode.
     *
     * @return Value of property cbmExtensionTransMode.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmExtensionTransMode() {
        return cbmExtensionTransMode;
    }

    /**
     * Setter for property cbmExtensionTransMode.
     *
     * @param cbmExtensionTransMode New value of property cbmExtensionTransMode.
     */
    public void setCbmExtensionTransMode(com.see.truetransact.clientutil.ComboBoxModel cbmExtensionTransMode) {
        this.cbmExtensionTransMode = cbmExtensionTransMode;
    }

    /**
     * Getter for property cbmExtensionTransProdType.
     *
     * @return Value of property cbmExtensionTransProdType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmExtensionTransProdType() {
        return cbmExtensionTransProdType;
    }

    /**
     * Setter for property cbmExtensionTransProdType.
     *
     * @param cbmExtensionTransProdType New value of property
     * cbmExtensionTransProdType.
     */
    public void setCbmExtensionTransProdType(com.see.truetransact.clientutil.ComboBoxModel cbmExtensionTransProdType) {
        this.cbmExtensionTransProdType = cbmExtensionTransProdType;
    }

    /**
     * Getter for property cbmExtensionTransProdId.
     *
     * @return Value of property cbmExtensionTransProdId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmExtensionTransProdId() {
        return cbmExtensionTransProdId;
    }

    /**
     * Setter for property cbmExtensionTransProdId.
     *
     * @param cbmExtensionTransProdId New value of property
     * cbmExtensionTransProdId.
     */
    public void setCbmExtensionTransProdId(com.see.truetransact.clientutil.ComboBoxModel cbmExtensionTransProdId) {
        this.cbmExtensionTransProdId = cbmExtensionTransProdId;
    }

    /**
     * Getter for property cbmExtensionPaymentMode.
     *
     * @return Value of property cbmExtensionPaymentMode.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmExtensionPaymentMode() {
        return cbmExtensionPaymentMode;
    }

    /**
     * Setter for property cbmExtensionPaymentMode.
     *
     * @param cbmExtensionPaymentMode New value of property
     * cbmExtensionPaymentMode.
     */
    public void setCbmExtensionPaymentMode(com.see.truetransact.clientutil.ComboBoxModel cbmExtensionPaymentMode) {
        this.cbmExtensionPaymentMode = cbmExtensionPaymentMode;
    }

    /**
     * Getter for property cbmExtensionPaymentProdType.
     *
     * @return Value of property cbmExtensionPaymentProdType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmExtensionPaymentProdType() {
        return cbmExtensionPaymentProdType;
    }

    /**
     * Setter for property cbmExtensionPaymentProdType.
     *
     * @param cbmExtensionPaymentProdType New value of property
     * cbmExtensionPaymentProdType.
     */
    public void setCbmExtensionPaymentProdType(com.see.truetransact.clientutil.ComboBoxModel cbmExtensionPaymentProdType) {
        this.cbmExtensionPaymentProdType = cbmExtensionPaymentProdType;
    }

    /**
     * Getter for property cbmExtensionPaymentProdId.
     *
     * @return Value of property cbmExtensionPaymentProdId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmExtensionPaymentProdId() {
        return cbmExtensionPaymentProdId;
    }

    /**
     * Setter for property cbmExtensionPaymentProdId.
     *
     * @param cbmExtensionPaymentProdId New value of property
     * cbmExtensionPaymentProdId.
     */
    public void setCbmExtensionPaymentProdId(com.see.truetransact.clientutil.ComboBoxModel cbmExtensionPaymentProdId) {
        this.cbmExtensionPaymentProdId = cbmExtensionPaymentProdId;
    }

    /**
     * Getter for property cbmExtensionProdType.
     *
     * @return Value of property cbmExtensionProdType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmExtensionProdType() {
        return cbmExtensionProdType;
    }

    /**
     * Setter for property cbmExtensionProdType.
     *
     * @param cbmExtensionProdType New value of property cbmExtensionProdType.
     */
    public void setCbmExtensionProdType(com.see.truetransact.clientutil.ComboBoxModel cbmExtensionProdType) {
        this.cbmExtensionProdType = cbmExtensionProdType;
    }

    /**
     * Getter for property cbmExtensionProdId.
     *
     * @return Value of property cbmExtensionProdId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmExtensionProdId() {
        return cbmExtensionProdId;
    }

    /**
     * Setter for property cbmExtensionProdId.
     *
     * @param cbmExtensionProdId New value of property cbmExtensionProdId.
     */
    public void setCbmExtensionProdId(com.see.truetransact.clientutil.ComboBoxModel cbmExtensionProdId) {
        this.cbmExtensionProdId = cbmExtensionProdId;
    }

    /**
     * Getter for property cbmExtensionPaymentFrequency.
     *
     * @return Value of property cbmExtensionPaymentFrequency.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmExtensionPaymentFrequency() {
        return cbmExtensionPaymentFrequency;
    }

    /**
     * Setter for property cbmExtensionPaymentFrequency.
     *
     * @param cbmExtensionPaymentFrequency New value of property
     * cbmExtensionPaymentFrequency.
     */
    public void setCbmExtensionPaymentFrequency(com.see.truetransact.clientutil.ComboBoxModel cbmExtensionPaymentFrequency) {
        this.cbmExtensionPaymentFrequency = cbmExtensionPaymentFrequency;
    }

    /**
     * Getter for property cboExtensionTransMode.
     *
     * @return Value of property cboExtensionTransMode.
     */
    public java.lang.String getCboExtensionTransMode() {
        return cboExtensionTransMode;
    }

    /**
     * Setter for property cboExtensionTransMode.
     *
     * @param cboExtensionTransMode New value of property cboExtensionTransMode.
     */
    public void setCboExtensionTransMode(java.lang.String cboExtensionTransMode) {
        this.cboExtensionTransMode = cboExtensionTransMode;
    }

    /**
     * Getter for property cboExtensionTransProdType.
     *
     * @return Value of property cboExtensionTransProdType.
     */
    public java.lang.String getCboExtensionTransProdType() {
        return cboExtensionTransProdType;
    }

    /**
     * Setter for property cboExtensionTransProdType.
     *
     * @param cboExtensionTransProdType New value of property
     * cboExtensionTransProdType.
     */
    public void setCboExtensionTransProdType(java.lang.String cboExtensionTransProdType) {
        this.cboExtensionTransProdType = cboExtensionTransProdType;
    }

    /**
     * Getter for property cboExtensionTransProdId.
     *
     * @return Value of property cboExtensionTransProdId.
     */
    public java.lang.String getCboExtensionTransProdId() {
        return cboExtensionTransProdId;
    }

    /**
     * Setter for property cboExtensionTransProdId.
     *
     * @param cboExtensionTransProdId New value of property
     * cboExtensionTransProdId.
     */
    public void setCboExtensionTransProdId(java.lang.String cboExtensionTransProdId) {
        this.cboExtensionTransProdId = cboExtensionTransProdId;
    }

    /**
     * Getter for property cboExtensionPaymentMode.
     *
     * @return Value of property cboExtensionPaymentMode.
     */
    public java.lang.String getCboExtensionPaymentMode() {
        return cboExtensionPaymentMode;
    }

    /**
     * Setter for property cboExtensionPaymentMode.
     *
     * @param cboExtensionPaymentMode New value of property
     * cboExtensionPaymentMode.
     */
    public void setCboExtensionPaymentMode(java.lang.String cboExtensionPaymentMode) {
        this.cboExtensionPaymentMode = cboExtensionPaymentMode;
    }

    /**
     * Getter for property cboExtensionPaymentProdType.
     *
     * @return Value of property cboExtensionPaymentProdType.
     */
    public java.lang.String getCboExtensionPaymentProdType() {
        return cboExtensionPaymentProdType;
    }

    /**
     * Setter for property cboExtensionPaymentProdType.
     *
     * @param cboExtensionPaymentProdType New value of property
     * cboExtensionPaymentProdType.
     */
    public void setCboExtensionPaymentProdType(java.lang.String cboExtensionPaymentProdType) {
        this.cboExtensionPaymentProdType = cboExtensionPaymentProdType;
    }

    /**
     * Getter for property cboExtensionPaymentProdId.
     *
     * @return Value of property cboExtensionPaymentProdId.
     */
    public java.lang.String getCboExtensionPaymentProdId() {
        return cboExtensionPaymentProdId;
    }

    /**
     * Setter for property cboExtensionPaymentProdId.
     *
     * @param cboExtensionPaymentProdId New value of property
     * cboExtensionPaymentProdId.
     */
    public void setCboExtensionPaymentProdId(java.lang.String cboExtensionPaymentProdId) {
        this.cboExtensionPaymentProdId = cboExtensionPaymentProdId;
    }

    /**
     * Getter for property cboExtensionProdType.
     *
     * @return Value of property cboExtensionProdType.
     */
    public java.lang.String getCboExtensionProdType() {
        return cboExtensionProdType;
    }

    /**
     * Setter for property cboExtensionProdType.
     *
     * @param cboExtensionProdType New value of property cboExtensionProdType.
     */
    public void setCboExtensionProdType(java.lang.String cboExtensionProdType) {
        this.cboExtensionProdType = cboExtensionProdType;
    }

    /**
     * Getter for property cboExtensionProdId.
     *
     * @return Value of property cboExtensionProdId.
     */
    public java.lang.String getCboExtensionProdId() {
        return cboExtensionProdId;
    }

    /**
     * Setter for property cboExtensionProdId.
     *
     * @param cboExtensionProdId New value of property cboExtensionProdId.
     */
    public void setCboExtensionProdId(java.lang.String cboExtensionProdId) {
        this.cboExtensionProdId = cboExtensionProdId;
    }

    /**
     * Getter for property cboExtensionPaymentFrequency.
     *
     * @return Value of property cboExtensionPaymentFrequency.
     */
    public java.lang.String getCboExtensionPaymentFrequency() {
        return cboExtensionPaymentFrequency;
    }

    /**
     * Setter for property cboExtensionPaymentFrequency.
     *
     * @param cboExtensionPaymentFrequency New value of property
     * cboExtensionPaymentFrequency.
     */
    public void setCboExtensionPaymentFrequency(java.lang.String cboExtensionPaymentFrequency) {
        this.cboExtensionPaymentFrequency = cboExtensionPaymentFrequency;
    }

    /**
     * Getter for property txtExtensionIntAmtValue.
     *
     * @return Value of property txtExtensionIntAmtValue.
     */
    public java.lang.String getTxtExtensionIntAmtValue() {
        return txtExtensionIntAmtValue;
    }

    /**
     * Setter for property txtExtensionIntAmtValue.
     *
     * @param txtExtensionIntAmtValue New value of property
     * txtExtensionIntAmtValue.
     */
    public void setTxtExtensionIntAmtValue(java.lang.String txtExtensionIntAmtValue) {
        this.txtExtensionIntAmtValue = txtExtensionIntAmtValue;
    }

    /**
     * Getter for property txtExtensionPaymentAccNo.
     *
     * @return Value of property txtExtensionPaymentAccNo.
     */
    public java.lang.String getTxtExtensionPaymentAccNo() {
        return txtExtensionPaymentAccNo;
    }

    /**
     * Setter for property txtExtensionPaymentAccNo.
     *
     * @param txtExtensionPaymentAccNo New value of property
     * txtExtensionPaymentAccNo.
     */
    public void setTxtExtensionPaymentAccNo(java.lang.String txtExtensionPaymentAccNo) {
        this.txtExtensionPaymentAccNo = txtExtensionPaymentAccNo;
    }

    /**
     * Getter for property valExtensionDep.
     *
     * @return Value of property valExtensionDep.
     */
    public java.lang.String getValExtensionDep() {
        return valExtensionDep;
    }

    /**
     * Setter for property valExtensionDep.
     *
     * @param valExtensionDep New value of property valExtensionDep.
     */
    public void setValExtensionDep(java.lang.String valExtensionDep) {
        this.valExtensionDep = valExtensionDep;
    }

    /**
     * Getter for property extensionValDepositSubNo.
     *
     * @return Value of property extensionValDepositSubNo.
     */
    public java.lang.String getExtensionValDepositSubNo() {
        return extensionValDepositSubNo;
    }

    /**
     * Setter for property extensionValDepositSubNo.
     *
     * @param extensionValDepositSubNo New value of property
     * extensionValDepositSubNo.
     */
    public void setExtensionValDepositSubNo(java.lang.String extensionValDepositSubNo) {
        this.extensionValDepositSubNo = extensionValDepositSubNo;
    }

    /**
     * Getter for property renewalInterestRepayAmtVal.
     *
     * @return Value of property renewalInterestRepayAmtVal.
     */
    public java.lang.String getRenewalInterestRepayAmtVal() {
        return renewalInterestRepayAmtVal;
    }

    /**
     * Setter for property renewalInterestRepayAmtVal.
     *
     * @param renewalInterestRepayAmtVal New value of property
     * renewalInterestRepayAmtVal.
     */
    public void setRenewalInterestRepayAmtVal(java.lang.String renewalInterestRepayAmtVal) {
        this.renewalInterestRepayAmtVal = renewalInterestRepayAmtVal;
    }

    /**
     * Getter for property lblRenewalNoticeNewVal.
     *
     * @return Value of property lblRenewalNoticeNewVal.
     */
    public java.lang.String getLblRenewalNoticeNewVal() {
        return lblRenewalNoticeNewVal;
    }

    /**
     * Setter for property lblRenewalNoticeNewVal.
     *
     * @param lblRenewalNoticeNewVal New value of property
     * lblRenewalNoticeNewVal.
     */
    public void setLblRenewalNoticeNewVal(java.lang.String lblRenewalNoticeNewVal) {
        this.lblRenewalNoticeNewVal = lblRenewalNoticeNewVal;
    }

    /**
     * Getter for property lblAutoRenewalNewVal.
     *
     * @return Value of property lblAutoRenewalNewVal.
     */
    public java.lang.String getLblAutoRenewalNewVal() {
        return lblAutoRenewalNewVal;
    }

    /**
     * Setter for property lblAutoRenewalNewVal.
     *
     * @param lblAutoRenewalNewVal New value of property lblAutoRenewalNewVal.
     */
    public void setLblAutoRenewalNewVal(java.lang.String lblAutoRenewalNewVal) {
        this.lblAutoRenewalNewVal = lblAutoRenewalNewVal;
    }

    /**
     * Getter for property lblRenewalWithIntNewVal.
     *
     * @return Value of property lblRenewalWithIntNewVal.
     */
    public java.lang.String getLblRenewalWithIntNewVal() {
        return lblRenewalWithIntNewVal;
    }

    /**
     * Setter for property lblRenewalWithIntNewVal.
     *
     * @param lblRenewalWithIntNewVal New value of property
     * lblRenewalWithIntNewVal.
     */
    public void setLblRenewalWithIntNewVal(java.lang.String lblRenewalWithIntNewVal) {
        this.lblRenewalWithIntNewVal = lblRenewalWithIntNewVal;
    }

    /**
     * Getter for property lblMemberTypeRenewalNewVal.
     *
     * @return Value of property lblMemberTypeRenewalNewVal.
     */
    public java.lang.String getLblMemberTypeRenewalNewVal() {
        return lblMemberTypeRenewalNewVal;
    }

    /**
     * Setter for property lblMemberTypeRenewalNewVal.
     *
     * @param lblMemberTypeRenewalNewVal New value of property
     * lblMemberTypeRenewalNewVal.
     */
    public void setLblMemberTypeRenewalNewVal(java.lang.String lblMemberTypeRenewalNewVal) {
        this.lblMemberTypeRenewalNewVal = lblMemberTypeRenewalNewVal;
    }

    /**
     * Getter for property lblTaxDeclareRenewalNewVal.
     *
     * @return Value of property lblTaxDeclareRenewalNewVal.
     */
    public java.lang.String getLblTaxDeclareRenewalNewVal() {
        return lblTaxDeclareRenewalNewVal;
    }

    /**
     * Setter for property lblTaxDeclareRenewalNewVal.
     *
     * @param lblTaxDeclareRenewalNewVal New value of property
     * lblTaxDeclareRenewalNewVal.
     */
    public void setLblTaxDeclareRenewalNewVal(java.lang.String lblTaxDeclareRenewalNewVal) {
        this.lblTaxDeclareRenewalNewVal = lblTaxDeclareRenewalNewVal;
    }

    /**
     * Getter for property lbl15HDeclareRenewalNewVal.
     *
     * @return Value of property lbl15HDeclareRenewalNewVal.
     */
    public java.lang.String getLbl15HDeclareRenewalNewVal() {
        return lbl15HDeclareRenewalNewVal;
    }

    /**
     * Setter for property lbl15HDeclareRenewalNewVal.
     *
     * @param lbl15HDeclareRenewalNewVal New value of property
     * lbl15HDeclareRenewalNewVal.
     */
    public void setLbl15HDeclareRenewalNewVal(java.lang.String lbl15HDeclareRenewalNewVal) {
        this.lbl15HDeclareRenewalNewVal = lbl15HDeclareRenewalNewVal;
    }

    /**
     * Getter for property renewalMemberTypeVal.
     *
     * @return Value of property renewalMemberTypeVal.
     */
    public java.lang.String getRenewalMemberTypeVal() {
        return renewalMemberTypeVal;
    }

    /**
     * Setter for property renewalMemberTypeVal.
     *
     * @param renewalMemberTypeVal New value of property renewalMemberTypeVal.
     */
    public void setRenewalMemberTypeVal(java.lang.String renewalMemberTypeVal) {
        this.renewalMemberTypeVal = renewalMemberTypeVal;
    }

    /**
     * Getter for property rdoRenewalMatAlert_report_Yes.
     *
     * @return Value of property rdoRenewalMatAlert_report_Yes.
     */
    public boolean getRdoRenewalMatAlert_report_Yes() {
        return rdoRenewalMatAlert_report_Yes;
    }

    /**
     * Setter for property rdoRenewalMatAlert_report_Yes.
     *
     * @param rdoRenewalMatAlert_report_Yes New value of property
     * rdoRenewalMatAlert_report_Yes.
     */
    public void setRdoRenewalMatAlert_report_Yes(boolean rdoRenewalMatAlert_report_Yes) {
        this.rdoRenewalMatAlert_report_Yes = rdoRenewalMatAlert_report_Yes;
    }

    /**
     * Getter for property rdoRenewalMatAlert_report_No.
     *
     * @return Value of property rdoRenewalMatAlert_report_No.
     */
    public boolean getRdoRenewalMatAlert_report_No() {
        return rdoRenewalMatAlert_report_No;
    }

    /**
     * Setter for property rdoRenewalMatAlert_report_No.
     *
     * @param rdoRenewalMatAlert_report_No New value of property
     * rdoRenewalMatAlert_report_No.
     */
    public void setRdoRenewalMatAlert_report_No(boolean rdoRenewalMatAlert_report_No) {
        this.rdoRenewalMatAlert_report_No = rdoRenewalMatAlert_report_No;
    }

    /**
     * Getter for property rdoRenewalAutoRenewal_Yes.
     *
     * @return Value of property rdoRenewalAutoRenewal_Yes.
     */
    public boolean getRdoRenewalAutoRenewal_Yes() {
        return rdoRenewalAutoRenewal_Yes;
    }

    /**
     * Setter for property rdoRenewalAutoRenewal_Yes.
     *
     * @param rdoRenewalAutoRenewal_Yes New value of property
     * rdoRenewalAutoRenewal_Yes.
     */
    public void setRdoRenewalAutoRenewal_Yes(boolean rdoRenewalAutoRenewal_Yes) {
        this.rdoRenewalAutoRenewal_Yes = rdoRenewalAutoRenewal_Yes;
    }

    /**
     * Getter for property rdoRenewalAutoRenewal_No.
     *
     * @return Value of property rdoRenewalAutoRenewal_No.
     */
    public boolean getRdoRenewalAutoRenewal_No() {
        return rdoRenewalAutoRenewal_No;
    }

    /**
     * Setter for property rdoRenewalAutoRenewal_No.
     *
     * @param rdoRenewalAutoRenewal_No New value of property
     * rdoRenewalAutoRenewal_No.
     */
    public void setRdoRenewalAutoRenewal_No(boolean rdoRenewalAutoRenewal_No) {
        this.rdoRenewalAutoRenewal_No = rdoRenewalAutoRenewal_No;
    }

    /**
     * Getter for property rdoRenewalWith_intRenewal_Yes.
     *
     * @return Value of property rdoRenewalWith_intRenewal_Yes.
     */
    public boolean getRdoRenewalWith_intRenewal_Yes() {
        return rdoRenewalWith_intRenewal_Yes;
    }

    /**
     * Setter for property rdoRenewalWith_intRenewal_Yes.
     *
     * @param rdoRenewalWith_intRenewal_Yes New value of property
     * rdoRenewalWith_intRenewal_Yes.
     */
    public void setRdoRenewalWith_intRenewal_Yes(boolean rdoRenewalWith_intRenewal_Yes) {
        this.rdoRenewalWith_intRenewal_Yes = rdoRenewalWith_intRenewal_Yes;
    }

    /**
     * Getter for property rdoRenewalWith_intRenewal_No.
     *
     * @return Value of property rdoRenewalWith_intRenewal_No.
     */
    public boolean getRdoRenewalWith_intRenewal_No() {
        return rdoRenewalWith_intRenewal_No;
    }

    /**
     * Setter for property rdoRenewalWith_intRenewal_No.
     *
     * @param rdoRenewalWith_intRenewal_No New value of property
     * rdoRenewalWith_intRenewal_No.
     */
    public void setRdoRenewalWith_intRenewal_No(boolean rdoRenewalWith_intRenewal_No) {
        this.rdoRenewalWith_intRenewal_No = rdoRenewalWith_intRenewal_No;
    }

    /**
     * Getter for property lblMemberVal.
     *
     * @return Value of property lblMemberVal.
     */
    public java.lang.String getLblMemberVal() {
        return lblMemberVal;
    }

    /**
     * Setter for property lblMemberVal.
     *
     * @param lblMemberVal New value of property lblMemberVal.
     */
    public void setLblMemberVal(java.lang.String lblMemberVal) {
        this.lblMemberVal = lblMemberVal;
    }

    /**
     * Getter for property renewalDepositDateEAMode.
     *
     * @return Value of property renewalDepositDateEAMode.
     */
    public java.lang.String getRenewalDepositDateEAMode() {
        return renewalDepositDateEAMode;
    }

    /**
     * Setter for property renewalDepositDateEAMode.
     *
     * @param renewalDepositDateEAMode New value of property
     * renewalDepositDateEAMode.
     */
    public void setRenewalDepositDateEAMode(java.lang.String renewalDepositDateEAMode) {
        this.renewalDepositDateEAMode = renewalDepositDateEAMode;
    }

    /**
     * Getter for property renewedNewNo.
     *
     * @return Value of property renewedNewNo.
     */
    public java.lang.String getRenewedNewNo() {
        return renewedNewNo;
    }

    /**
     * Setter for property renewedNewNo.
     *
     * @param renewedNewNo New value of property renewedNewNo.
     */
    public void setRenewedNewNo(java.lang.String renewedNewNo) {
        this.renewedNewNo = renewedNewNo;
    }

    /**
     * Getter for property extensionOriginalIntValue.
     *
     * @return Value of property extensionOriginalIntValue.
     */
    public java.lang.String getExtensionOriginalIntValue() {
        return extensionOriginalIntValue;
    }

    /**
     * Setter for property extensionOriginalIntValue.
     *
     * @param extensionOriginalIntValue New value of property
     * extensionOriginalIntValue.
     */
    public void setExtensionOriginalIntValue(java.lang.String extensionOriginalIntValue) {
        this.extensionOriginalIntValue = extensionOriginalIntValue;
    }

    /**
     * Getter for property lblExtensionTotalRepayAmtValue.
     *
     * @return Value of property lblExtensionTotalRepayAmtValue.
     */
    public java.lang.String getLblExtensionTotalRepayAmtValue() {
        return lblExtensionTotalRepayAmtValue;
    }

    /**
     * Setter for property lblExtensionTotalRepayAmtValue.
     *
     * @param lblExtensionTotalRepayAmtValue New value of property
     * lblExtensionTotalRepayAmtValue.
     */
    public void setLblExtensionTotalRepayAmtValue(java.lang.String lblExtensionTotalRepayAmtValue) {
        this.lblExtensionTotalRepayAmtValue = lblExtensionTotalRepayAmtValue;
    }

    /**
     * Getter for property extensionBalanceIntValue.
     *
     * @return Value of property extensionBalanceIntValue.
     */
    public java.lang.String getExtensionBalanceIntValue() {
        return extensionBalanceIntValue;
    }

    /**
     * Setter for property extensionBalanceIntValue.
     *
     * @param extensionBalanceIntValue New value of property
     * extensionBalanceIntValue.
     */
    public void setExtensionBalanceIntValue(java.lang.String extensionBalanceIntValue) {
        this.extensionBalanceIntValue = extensionBalanceIntValue;
    }

    /**
     * Getter for property rdoExtensionMatAlertReport_Yes.
     *
     * @return Value of property rdoExtensionMatAlertReport_Yes.
     */
    public boolean getRdoExtensionMatAlertReport_Yes() {
        return rdoExtensionMatAlertReport_Yes;
    }

    /**
     * Setter for property rdoExtensionMatAlertReport_Yes.
     *
     * @param rdoExtensionMatAlertReport_Yes New value of property
     * rdoExtensionMatAlertReport_Yes.
     */
    public void setRdoExtensionMatAlertReport_Yes(boolean rdoExtensionMatAlertReport_Yes) {
        this.rdoExtensionMatAlertReport_Yes = rdoExtensionMatAlertReport_Yes;
    }

    /**
     * Getter for property rdoExtensionMatAlertReport_No.
     *
     * @return Value of property rdoExtensionMatAlertReport_No.
     */
    public boolean getRdoExtensionMatAlertReport_No() {
        return rdoExtensionMatAlertReport_No;
    }

    /**
     * Setter for property rdoExtensionMatAlertReport_No.
     *
     * @param rdoExtensionMatAlertReport_No New value of property
     * rdoExtensionMatAlertReport_No.
     */
    public void setRdoExtensionMatAlertReport_No(boolean rdoExtensionMatAlertReport_No) {
        this.rdoExtensionMatAlertReport_No = rdoExtensionMatAlertReport_No;
    }

    /**
     * Getter for property rdoExtensionAutoRenewal_Yes.
     *
     * @return Value of property rdoExtensionAutoRenewal_Yes.
     */
    public boolean getRdoExtensionAutoRenewal_Yes() {
        return rdoExtensionAutoRenewal_Yes;
    }

    /**
     * Setter for property rdoExtensionAutoRenewal_Yes.
     *
     * @param rdoExtensionAutoRenewal_Yes New value of property
     * rdoExtensionAutoRenewal_Yes.
     */
    public void setRdoExtensionAutoRenewal_Yes(boolean rdoExtensionAutoRenewal_Yes) {
        this.rdoExtensionAutoRenewal_Yes = rdoExtensionAutoRenewal_Yes;
    }

    /**
     * Getter for property rdoExtensionAutoRenewal_No.
     *
     * @return Value of property rdoExtensionAutoRenewal_No.
     */
    public boolean getRdoExtensionAutoRenewal_No() {
        return rdoExtensionAutoRenewal_No;
    }

    /**
     * Setter for property rdoExtensionAutoRenewal_No.
     *
     * @param rdoExtensionAutoRenewal_No New value of property
     * rdoExtensionAutoRenewal_No.
     */
    public void setRdoExtensionAutoRenewal_No(boolean rdoExtensionAutoRenewal_No) {
        this.rdoExtensionAutoRenewal_No = rdoExtensionAutoRenewal_No;
    }

    /**
     * Getter for property rdoExtensionWithIntAutoRenewal_Yes.
     *
     * @return Value of property rdoExtensionWithIntAutoRenewal_Yes.
     */
    public boolean getRdoExtensionWithIntAutoRenewal_Yes() {
        return rdoExtensionWithIntAutoRenewal_Yes;
    }

    /**
     * Setter for property rdoExtensionWithIntAutoRenewal_Yes.
     *
     * @param rdoExtensionWithIntAutoRenewal_Yes New value of property
     * rdoExtensionWithIntAutoRenewal_Yes.
     */
    public void setRdoExtensionWithIntAutoRenewal_Yes(boolean rdoExtensionWithIntAutoRenewal_Yes) {
        this.rdoExtensionWithIntAutoRenewal_Yes = rdoExtensionWithIntAutoRenewal_Yes;
    }

    /**
     * Getter for property rdoExtensionWithIntAutoRenewal_No.
     *
     * @return Value of property rdoExtensionWithIntAutoRenewal_No.
     */
    public boolean getRdoExtensionWithIntAutoRenewal_No() {
        return rdoExtensionWithIntAutoRenewal_No;
    }

    /**
     * Setter for property rdoExtensionWithIntAutoRenewal_No.
     *
     * @param rdoExtensionWithIntAutoRenewal_No New value of property
     * rdoExtensionWithIntAutoRenewal_No.
     */
    public void setRdoExtensionWithIntAutoRenewal_No(boolean rdoExtensionWithIntAutoRenewal_No) {
        this.rdoExtensionWithIntAutoRenewal_No = rdoExtensionWithIntAutoRenewal_No;
    }

    /**
     * Getter for property lblDelayedMonthValue.
     *
     * @return Value of property lblDelayedMonthValue.
     */
    public java.lang.String getLblDelayedMonthValue() {
        return lblDelayedMonthValue;
    }

    /**
     * Setter for property lblDelayedMonthValue.
     *
     * @param lblDelayedMonthValue New value of property lblDelayedMonthValue.
     */
    public void setLblDelayedMonthValue(java.lang.String lblDelayedMonthValue) {
        this.lblDelayedMonthValue = lblDelayedMonthValue;
    }

    /**
     * Getter for property lblDelayedAmountValue.
     *
     * @return Value of property lblDelayedAmountValue.
     */
    public java.lang.String getLblDelayedAmountValue() {
        return lblDelayedAmountValue;
    }

    /**
     * Setter for property lblDelayedAmountValue.
     *
     * @param lblDelayedAmountValue New value of property lblDelayedAmountValue.
     */
    public void setLblDelayedAmountValue(java.lang.String lblDelayedAmountValue) {
        this.lblDelayedAmountValue = lblDelayedAmountValue;
    }

    /**
     * Getter for property lblExtensionWithdrawIntAmtValue2.
     *
     * @return Value of property lblExtensionWithdrawIntAmtValue2.
     */
    public java.lang.String getLblExtensionWithdrawIntAmtValue2() {
        return lblExtensionWithdrawIntAmtValue2;
    }

    /**
     * Setter for property lblExtensionWithdrawIntAmtValue2.
     *
     * @param lblExtensionWithdrawIntAmtValue2 New value of property
     * lblExtensionWithdrawIntAmtValue2.
     */
    public void setLblExtensionWithdrawIntAmtValue2(java.lang.String lblExtensionWithdrawIntAmtValue2) {
        this.lblExtensionWithdrawIntAmtValue2 = lblExtensionWithdrawIntAmtValue2;
    }

    /**
     * Getter for property lblExtensionWithdrawIntAmtValue1.
     *
     * @return Value of property lblExtensionWithdrawIntAmtValue1.
     */
    public java.lang.String getLblExtensionWithdrawIntAmtValue1() {
        return lblExtensionWithdrawIntAmtValue1;
    }

    /**
     * Setter for property lblExtensionWithdrawIntAmtValue1.
     *
     * @param lblExtensionWithdrawIntAmtValue1 New value of property
     * lblExtensionWithdrawIntAmtValue1.
     */
    public void setLblExtensionWithdrawIntAmtValue1(java.lang.String lblExtensionWithdrawIntAmtValue1) {
        this.lblExtensionWithdrawIntAmtValue1 = lblExtensionWithdrawIntAmtValue1;
    }

    /**
     * Getter for property renewalSBIntRateVal.
     *
     * @return Value of property renewalSBIntRateVal.
     */
    public java.lang.String getRenewalSBIntRateVal() {
        return renewalSBIntRateVal;
    }

    /**
     * Setter for property renewalSBIntRateVal.
     *
     * @param renewalSBIntRateVal New value of property renewalSBIntRateVal.
     */
    public void setRenewalSBIntRateVal(java.lang.String renewalSBIntRateVal) {
        this.renewalSBIntRateVal = renewalSBIntRateVal;
    }

    /**
     * Getter for property prodType.
     *
     * @return Value of property prodType.
     */
    public java.lang.String getProdType() {
        return prodType;
    }

    /**
     * Setter for property prodType.
     *
     * @param prodType New value of property prodType.
     */
    public void setProdType(java.lang.String prodType) {
        this.prodType = prodType;
    }

    /**
     * Getter for property transactionMap.
     *
     * @return Value of property transactionMap.
     */
    public java.util.HashMap getTransactionMap() {
        return transactionMap;
    }

    /**
     * Setter for property transactionMap.
     *
     * @param transactionMap New value of property transactionMap.
     */
    public void setTransactionMap(java.util.HashMap transactionMap) {
        this.transactionMap = transactionMap;
    }

    public boolean getSalaryRecoveryYes() {
        return salaryRecoveryYes;
    }

    public void setSalaryRecoveryYes(boolean salaryRecoveryYes) {
        this.salaryRecoveryYes = salaryRecoveryYes;
    }

    public boolean getSalaryRecoveryNo() {
        return salaryRecoveryNo;
    }

    public void setSalaryRecoveryNo(boolean salaryRecoveryNo) {
        this.salaryRecoveryNo = salaryRecoveryNo;
    }

    public String getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(String lockStatus) {
        this.lockStatus = lockStatus;
    }

    public boolean getUnLock() {
        return unLock;
    }

    public void setUnLock(boolean unLock) {
        this.unLock = unLock;
    }

    /**
     * Getter for property postageRenewAmt.
     *
     * @return Value of property postageRenewAmt.
     */
    public java.lang.String getPostageRenewAmt() {
        return postageRenewAmt;
    }

    /**
     * Setter for property postageRenewAmt.
     *
     * @param postageRenewAmt New value of property postageRenewAmt.
     */
    public void setPostageRenewAmt(java.lang.String postageRenewAmt) {
        this.postageRenewAmt = postageRenewAmt;
    }

    /**
     * Getter for property postageAmt.
     *
     * @return Value of property postageAmt.
     */
    public java.lang.String getPostageAmt() {
        return postageAmt;
    }

    /**
     * Setter for property postageAmt.
     *
     * @param postageAmt New value of property postageAmt.
     */
    public void setPostageAmt(java.lang.String postageAmt) {
        this.postageAmt = postageAmt;
    }

    public void setTransactionOB(TransactionOB transactionOB) {
        this.transactionOB = transactionOB;
    }

    public java.util.LinkedHashMap getAllowedTransactionDetailsTO() {
        return allowedTransactionDetailsTO;
    }

    /**
     * Setter for property allowedTransactionDetailsTO.
     *
     * @param allowedTransactionDetailsTO New value of property
     * allowedTransactionDetailsTO.
     */
    public void setAllowedTransactionDetailsTO(java.util.LinkedHashMap allowedTransactionDetailsTO) {
        // //system.out.println("In OB of RemIssue : " + allowedTransactionDetailsTO);
        this.allowedTransactionDetailsTO = allowedTransactionDetailsTO;
    }

    /**
     * Getter for property tranListforAuth.
     *
     * @return Value of property tranListforAuth.
     */
    public java.util.List getTranListforAuth() {
        return tranListforAuth;
    }

    /**
     * Setter for property tranListforAuth.
     *
     * @param tranListforAuth New value of property tranListforAuth.
     */
    public void setTranListforAuth(java.util.List tranListforAuth) {
        this.tranListforAuth = tranListforAuth;
    }

    /**
     * Getter for property objTermDepositRB.
     *
     * @return Value of property objTermDepositRB.
     */
    public java.util.ResourceBundle getObjTermDepositRB() {
        return objTermDepositRB;
    }

    /**
     * Setter for property objTermDepositRB.
     *
     * @param objTermDepositRB New value of property objTermDepositRB.
     */
    public void setObjTermDepositRB(java.util.ResourceBundle objTermDepositRB) {
        this.objTermDepositRB = objTermDepositRB;
    }

    /**
     * Getter for property renewalCountWithScreen.
     *
     * @return Value of property renewalCountWithScreen.
     */
    public java.lang.String getRenewalCountWithScreen() {
        return renewalCountWithScreen;
    }

    /**
     * Setter for property renewalCountWithScreen.
     *
     * @param renewalCountWithScreen New value of property
     * renewalCountWithScreen.
     */
    public void setRenewalCountWithScreen(java.lang.String renewalCountWithScreen) {
        this.renewalCountWithScreen = renewalCountWithScreen;
    }

    /**
     * Getter for property balIntAmt.
     *
     * @return Value of property balIntAmt.
     */
    public double getBalIntAmt() {
        return balIntAmt;
    }

    /**
     * Setter for property balIntAmt.
     *
     * @param balIntAmt New value of property balIntAmt.
     */
    public void setBalIntAmt(double balIntAmt) {
        this.balIntAmt = balIntAmt;
    }

    /**
     * Getter for property txtPeriodOfDeposit_Weeks.
     *
     * @return Value of property txtPeriodOfDeposit_Weeks.
     */
    /**
     * Getter for property renewaltxtPeriodOfDeposit_Weeks.
     *
     * @return Value of property renewaltxtPeriodOfDeposit_Weeks.
     */
    public String getRenewaltxtPeriodOfDeposit_Weeks() {
        return renewaltxtPeriodOfDeposit_Weeks;
    }

    /**
     * Setter for property renewaltxtPeriodOfDeposit_Weeks.
     *
     * @param renewaltxtPeriodOfDeposit_Weeks New value of property
     * renewaltxtPeriodOfDeposit_Weeks.
     */
    public void setRenewaltxtPeriodOfDeposit_Weeks(String renewaltxtPeriodOfDeposit_Weeks) {
        this.renewaltxtPeriodOfDeposit_Weeks = renewaltxtPeriodOfDeposit_Weeks;
    }

    /**
     * Getter for property ExtensiontxtPeriodOfDeposit_Weeks.
     *
     * @return Value of property ExtensiontxtPeriodOfDeposit_Weeks.
     */
    public String getExtensiontxtPeriodOfDeposit_Weeks() {
        return ExtensiontxtPeriodOfDeposit_Weeks;
    }

    /**
     * Setter for property ExtensiontxtPeriodOfDeposit_Weeks.
     *
     * @param ExtensiontxtPeriodOfDeposit_Weeks New value of property
     * ExtensiontxtPeriodOfDeposit_Weeks.
     */
    public void setExtensiontxtPeriodOfDeposit_Weeks(String ExtensiontxtPeriodOfDeposit_Weeks) {
        this.ExtensiontxtPeriodOfDeposit_Weeks = ExtensiontxtPeriodOfDeposit_Weeks;
    }

    public ComboBoxModel getCbmAddressType() {
        return cbmAddressType;
    }

    public void setCbmAddressType(ComboBoxModel cbmAddressType) {
        this.cbmAddressType = cbmAddressType;
    }
  
    public String getCboAddressType() {
        return cboAddressType;
    }

    public void setCboAddressType(String cboAddressType) {
        this.cboAddressType = cboAddressType;
    }

    public String getTxaMdsRemarks() {
        return txaMdsRemarks;
    }

    public void setTxaMdsRemarks(String txaMdsRemarks) {
        this.txaMdsRemarks = txaMdsRemarks;
    }

    public String getTxtMdsGroup() {
        return txtMdsGroup;
    }

    public void setTxtMdsGroup(String txtMdsGroup) {
        this.txtMdsGroup = txtMdsGroup;
    }
    
    public Date getNextInterestApplDate() {
        return nextInterestApplDate;
    }

    public void setNextInterestApplDate(Date nextInterestApplDate) {
        this.nextInterestApplDate = nextInterestApplDate;
    }
    public boolean getChkAccZeroBalYN() {
        return chkAccZeroBalYN;
    }

    public void setChkAccZeroBalYN(boolean chkAccZeroBalYN) {
        this.chkAccZeroBalYN = chkAccZeroBalYN;
        setChanged();
    }

    public EnhancedTableModel getTblOldDepDet() {
        return tblOldDepDet;
    }

    public void setTblOldDepDet(EnhancedTableModel tblOldDepDet) {
        this.tblOldDepDet = tblOldDepDet;
    }

    public double getAdtAmt() {
        return adtAmt;
    }

    public void setAdtAmt(double adtAmt) {
        this.adtAmt = adtAmt;
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

    public String getTxtExistingAccNo() {
        return txtExistingAccNo;
    }

    public void setTxtExistingAccNo(String txtExistingAccNo) {
        this.txtExistingAccNo = txtExistingAccNo;
    }

    public static String getMdsGroup() {
        return mdsGroup;
    }

    public static void setMdsGroup(String mdsGroup) {
        TermDepositOB.mdsGroup = mdsGroup;
    }

    public static String getMdsRemarks() {
        return mdsRemarks;
    }

    public static void setMdsRemarks(String mdsRemarks) {
        TermDepositOB.mdsRemarks = mdsRemarks;
    }

    public String getTxtDealer() {
        return txtDealer;
    }

    public void setTxtDealer(String txtDealer) {
        this.txtDealer = txtDealer;
    }
    
    
    
    
     /* To set common data in the Transfer Object -- DB Table: DEPO_ACCT_THRIFTBENEVOLENT */
    public ThriftBenevolentAdditionalAmtTO setAdditionalAmountForDeposit(){
        
        ThriftBenevolentAdditionalAmtTO objThriftBenevolentAdditionalAmtTO = new ThriftBenevolentAdditionalAmtTO();        
        try{
            if(getEffectiveDate().length() > 0){                
                System.out.println("in setAdditionalAmountForDeposit  getEffectiveDate:: if ");                
                objThriftBenevolentAdditionalAmtTO.setProductId(objAccInfoTO.getProdId());
                objThriftBenevolentAdditionalAmtTO.setEffectiveDate(DateUtil.getDateMMDDYYYY(getEffectiveDate()));
//                System.out.println("getTxtDepsoitNo : " + this.getTxtDepsoitNo());
//                objThriftBenevolentAdditionalAmtTO.setDepositNo(getTxtDepsoitNo());
                objThriftBenevolentAdditionalAmtTO.setAdditionalAmount(CommonUtil.convertObjToDouble(getAdditionalAmount()));
                System.out.println("in setAdditionalAmountForDeposit  getEffectiveDate:: " + getEffectiveDate());                  
            }
            else{
                objThriftBenevolentAdditionalAmtTO = null;
            }
            
        }catch(Exception e){            
            //e.printStackTrace();
            parseException.logException(e,true);
        }
        return objThriftBenevolentAdditionalAmtTO;
    }
        
    public boolean verifyProdEffectiveDate(String prodId,Date effectiveDate,String depositNo) {
        boolean verify = false;
        try{
            final HashMap data = new HashMap();            
            data.put("PROD_ID",prodId);            
            data.put("EFFECTIVE_DATE",effectiveDate);
            data.put("DEPOSIT_NO",depositNo);
            System.out.println("effective date :: " + effectiveDate);            
            System.out.println("deposit number :: " + depositNo);
            final List resultList = ClientUtil.executeQuery("getEffectiveDateForDepositAccount", data);
            System.out.println("resultList :: " + resultList);            
            if(resultList.size() > 0){
                verify = true;
            }
            else{
                verify = false;
            }
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return verify;
    }    
    
    public void setThriftBenevolentTable() {
        ArrayList columnHeader = new ArrayList();
        
        columnHeader.add("Effective Date");
        columnHeader.add("Additional Amount");
       
        ArrayList data = new ArrayList();
        tbmThriftBenevolent = new TableModel(data, columnHeader);
    }
   
     
 private ArrayList setRow(ThriftBenevolentAdditionalAmtTO objThriftBenevolentAdditionalAmtTO) {
        ArrayList row = new ArrayList();
        row.add(objThriftBenevolentAdditionalAmtTO.getEffectiveDate());  
        row.add(objThriftBenevolentAdditionalAmtTO.getAdditionalAmount());              
        return row;
}

void deleteThriftBenevolentTableData(){
        if(tbmThriftBenevolent.getRowCount() > 0){
         for(int i=0; i< tbmThriftBenevolent.getRowCount(); i++){
           tbmThriftBenevolent.removeRow(i);
         } 
        } 
}

private void populateThriftBenevolentTable(List lstData) {
        additionalAmountRecord=  new ArrayList();
        int size = lstData.size();
        setThriftBenevolentTable();
        for (int i = 0; i < size; i++) {
            ThriftBenevolentAdditionalAmtTO objThriftBenevolentAdditionalAmtTO = new ThriftBenevolentAdditionalAmtTO();
            HashMap newMap = new HashMap();
            newMap = (HashMap) lstData.get(i);
            System.out.println("new map new map :: "+newMap);
            String effectiveDate = CommonUtil.convertObjToStr(newMap.get("EFFECTIVE_DATE"));            
            objThriftBenevolentAdditionalAmtTO.setEffectiveDate(DateUtil.getDateMMDDYYYY(effectiveDate));            
            objThriftBenevolentAdditionalAmtTO.setAdditionalAmount(CommonUtil.convertObjToDouble(newMap.get("ADDITIONAL_AMOUNT")));            
            additionalAmountRecord.add(objThriftBenevolentAdditionalAmtTO);
            ArrayList irRow = this.setRow(objThriftBenevolentAdditionalAmtTO);            
            tbmThriftBenevolent.insertRow(tbmThriftBenevolent.getRowCount(), irRow);
        }
        setTbmThriftBenevolent(tbmThriftBenevolent);
        tbmThriftBenevolent.fireTableDataChanged();
        ttNotifyObservers();
}

   private HashMap setThriftBenevelontDataForAccount(ThriftBenevolentAdditionalAmtTO objThriftBenevolentAdditionalAmtTO) {
        
        HashMap thriftBenevolentMap = new HashMap();
        thriftBenevolentMap.put("PROD_ID", objThriftBenevolentAdditionalAmtTO.getProductId()); 
        thriftBenevolentMap.put("DEPOSIT_NO",objThriftBenevolentAdditionalAmtTO.getDepositNo());
        thriftBenevolentMap.put("EFFECTIVE_DATE",objThriftBenevolentAdditionalAmtTO.getEffectiveDate());
        thriftBenevolentMap.put("ADDITIONAL_AMOUNT",objThriftBenevolentAdditionalAmtTO.getAdditionalAmount());
        return thriftBenevolentMap;
    }
    // End
    private SMSSubscriptionTO getMobileBanking(AccInfoTO objAccInfoTO) {
        if (getChkMobileBanking() && objAccInfoTO != null) {
            objSMSSubscriptionTO = new SMSSubscriptionTO();
            objSMSSubscriptionTO.setProdType("TD");
            objSMSSubscriptionTO.setProdId(objAccInfoTO.getProdId());
            objSMSSubscriptionTO.setActNum(objAccInfoTO.getDepositNo());
            objSMSSubscriptionTO.setMobileNo(CommonUtil.convertObjToStr(getTxtMobileNo()));
            Date smsSubscriptionDt = DateUtil.getDateMMDDYYYY(getTdtMobileSubscribedFrom());
            if (smsSubscriptionDt != null) {
                Date smsDt = (Date) currDt.clone();
                smsDt.setDate(smsSubscriptionDt.getDate());
                smsDt.setMonth(smsSubscriptionDt.getMonth());
                smsDt.setYear(smsSubscriptionDt.getYear());
                objSMSSubscriptionTO.setSubscriptionDt(smsDt);
            } else {
                objSMSSubscriptionTO.setSubscriptionDt(DateUtil.getDateMMDDYYYY(getTdtMobileSubscribedFrom()));
            }
            if (!CommonUtil.convertObjToStr(objSMSSubscriptionTO.getStatus()).equals(CommonConstants.STATUS_MODIFIED)) {
                objSMSSubscriptionTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            objSMSSubscriptionTO.setStatusBy(ProxyParameters.USER_ID);
            objSMSSubscriptionTO.setStatusDt((Date) currDt.clone());
            objSMSSubscriptionTO.setCreatedBy(ProxyParameters.USER_ID);
            objSMSSubscriptionTO.setCreatedDt((Date) currDt.clone());
        } else if (objSMSSubscriptionTO != null) {
            //date setting added by Kannan AR
            Date smsSubscriptionDt = objSMSSubscriptionTO.getSubscriptionDt();
            if (smsSubscriptionDt != null) {
                Date smsDt = (Date) currDt.clone();
                smsDt.setDate(smsSubscriptionDt.getDate());
                smsDt.setMonth(smsSubscriptionDt.getMonth());
                smsDt.setYear(smsSubscriptionDt.getYear());
                objSMSSubscriptionTO.setSubscriptionDt(smsDt);
            }
            objSMSSubscriptionTO.setStatus(CommonConstants.STATUS_DELETED);
        } else {
            objSMSSubscriptionTO = null;
        }
        return objSMSSubscriptionTO;
    }

    private void setSMSSubscriptionTO(SMSSubscriptionTO objSMSSubscriptionTO) {
        setTxtMobileNo(CommonUtil.convertObjToStr(objSMSSubscriptionTO.getMobileNo()));
        setTdtMobileSubscribedFrom(DateUtil.getStringDate(objSMSSubscriptionTO.getSubscriptionDt()));
        setChkMobileBanking(true);
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
    
    // Added by nithya on 26-09-2017 for group deposit
    
    public static String getDepositGroup() {
        return depositGroup;
    }

    public static void setDepositGroup(String depositGroup) {
         TermDepositOB.depositGroup = depositGroup;        
    }
    public static String getDepositGroupRemarks() {
        return depositGroupRemarks;
    }

    public static void setDepositGroupRemarks(String depositGroupRemarks) {
        TermDepositOB.depositGroupRemarks = depositGroupRemarks;
    }    
    
    public String getIsGroupDeposit() {
        return isGroupDeposit;
    }

    public void setIsGroupDeposit(String isGroupDeposit) {
        this.isGroupDeposit = isGroupDeposit;
    }

    // End

    public String getLblTDSAmount() {
        return lblTDSAmount;
    }

    public void setLblTDSAmount(String lblTDSAmount) {
        this.lblTDSAmount = lblTDSAmount;
    }

    public String getTdsAcHd() {
        return tdsAcHd;
    }

    public void setTdsAcHd(String tdsAcHd) {
        this.tdsAcHd = tdsAcHd;
    }

}
