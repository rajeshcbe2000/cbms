/*
 *
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LoanProductOB.java
 *
 * Created on November 24, 2003, 10:56 AM
 */

package com.see.truetransact.ui.product.loan.agricultureCard;

import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.LocaleConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.TableUtil;
import com.see.truetransact.transferobject.product.loan.agriculturecard.AgriLoanProductAccountTO;
import com.see.truetransact.transferobject.product.loan.agriculturecard.AgriLoanProductAccountParamTO;
import com.see.truetransact.transferobject.product.loan.agriculturecard.AgriLoanProductInterReceivableTO;
import com.see.truetransact.transferobject.product.loan.agriculturecard.AgriLoanProductChargesTO;
import com.see.truetransact.transferobject.product.loan.agriculturecard.AgriLoanProductChargesTabTO;
import com.see.truetransact.transferobject.product.loan.agriculturecard.AgriLoanProductSpeItemsTO;
import com.see.truetransact.transferobject.product.loan.agriculturecard.AgriLoanProductAccHeadTO;
import com.see.truetransact.transferobject.product.loan.agriculturecard.AgriLoanProductNonPerAssetsTO;
import com.see.truetransact.transferobject.product.loan.agriculturecard.AgriLoanProductInterCalcTO;
import com.see.truetransact.transferobject.product.loan.agriculturecard.AgriLoanProductDocumentsTO;
import com.see.truetransact.transferobject.product.loan.agriculturecard.AgriLoanProductClassificationsTO;
import com.see.truetransact.transferobject.product.loan.agriculturecard.AgriLoanProductInsuranceTO;

import com.see.truetransact.uicomponent.COptionPane;

import com.see.truetransact.ui.TrueTransactMain;

import org.apache.log4j.Logger;

import java.util.Observable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Date;
import javax.swing.table.TableModel;

import com.see.truetransact.uicomponent.CObservable;
//import javax.swing.table.*;
/**
 *
 * @author  rahul
 * Modified by prasath
 * Documents tab added
 */
public class NewLoanAgriProductOB extends CObservable {
    
    Date curDate = null;
    private HashMap lookUpHash;
    private HashMap operationMap;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private ArrayList chargeTabRow;
    final ArrayList chargeTabTitle = new ArrayList();
    final ArrayList documentsTabTitle = new ArrayList();
    final ArrayList noticeChargeTabTitle=new ArrayList();
    final ArrayList insurnaceTabTitle=new ArrayList();
    private ArrayList singleInsList=new ArrayList();
    private HashMap   singleInsMap=new HashMap();
    private ProxyFactory proxy = null;
    private String cboNoticeType="";
    private String cboIssueAfter="";
    private String txtNoticeChargeAmt="";
    private String txtPostageChargeAmt="";
    private String txtInsurancePremiumDebit="";
    //insurance details
    private String cboInsuranceType="";
    private String cboInsuranceUnderScheme="";
    private String txtBankSharePremium="";
    private String txtElgLoanAmt="";
    private String txtCustomerSharePremium="";
    private String txtInsuranceAmt="";
    private ArrayList allInsuranceList=new ArrayList();
    private LinkedHashMap allInsuranceMap=new LinkedHashMap();
    private int actionType;/** To set the status based on ActionType, either New, Edit, etc., */
    private int result;
    
    private double periodData = 0;// for setting data depending on period comboboxes...
    private double resultData = 0;// for setting data depending on period comboboxes...
    
    private int resultValue=0;// for retrieving data from the period comboboxes...
    
    private final String MANUAL = "M";
    private final String SYSTEM = "S";
    private final String BOTH = "B";
    private final String YEAR ="YEARS";
    private final String MONTH ="MONTHS";
    private final String DAY ="DAYS";
    
    private final String YEAR1 ="Years";
    private final String MONTH1 ="Months";
    private final String DAY1 ="Days";
    
    private final String YES ="Y";
    private final String NO ="N";
    private String duration = "";
    
    private final int year = 365;
    private final int month = 30;
    private final int day = 1;
    
    final String ABSOLUTE = "Absolute";
    final String PERCENT = "Percent";
    private EnhancedTableModel tblNoticeCharge;
    private EnhancedTableModel tblChargeTab;
    private EnhancedTableModel tblDocuments;
    private EnhancedTableModel tblInsurance;
    private TableUtil insuranceModel=new TableUtil();
    // To get the Value of Column Title and Dialogue Box...
    //    final NewLoanProductRB objLoanProductRB = new NewLoanProductRB();
    java.util.ResourceBundle objLoanProductRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.product.loan.agricultureCard.NewLoanAgriProductRB", ProxyParameters.LANGUAGE);
    
    private ComboBoxModel cbmOperatesLike;
    private ComboBoxModel cbmDebInterCalcFreq;
    private ComboBoxModel cbmDebitInterAppFreq;
    private ComboBoxModel cbmDebitInterComFreq;
    private ComboBoxModel cbmDebitProdRoundOff;
    private ComboBoxModel cbmDebitInterRoundOff;
    private ComboBoxModel cbmProductFreq;
    
    private ComboBoxModel cbmInsuranceType;
    private ComboBoxModel  cbmInsuranceUnderScheme;
    
    //    private ComboBoxModel cbmProdCurrency;
    private ComboBoxModel cbmFolioChargesAppFreq;
    private ComboBoxModel cbmToCollectFolioCharges;
    private ComboBoxModel cbmIncompleteFolioRoundOffFreq;
    private ComboBoxModel cbmMinPeriodsArrears;
    private ComboBoxModel cbmPeriodTranSStanAssets;
    private ComboBoxModel cbmPeriodTransDoubtfulAssets1;
    private ComboBoxModel cbmPeriodTransDoubtfulAssets2;
    private ComboBoxModel cbmPeriodTransDoubtfulAssets3;
    private ComboBoxModel cbmPeriodTransLossAssets;
    private ComboBoxModel cbmPeriodAfterWhichTransNPerformingAssets;
    private ComboBoxModel cbmcalcType;
    private ComboBoxModel cbmMinPeriod;
    private ComboBoxModel cbmMaxPeriod;
    private ComboBoxModel cbmReviewPeriod;
    private ComboBoxModel cbmLoanPeriodMul;
    private ComboBoxModel cbmCharge_Limit2;
    private ComboBoxModel cbmCharge_Limit3;
    
    
    private ComboBoxModel cbmCommodityCode;
    private ComboBoxModel cbmGuaranteeCoverCode;
    private ComboBoxModel cbmSectorCode;
    private ComboBoxModel cbmHealthCode;
    private ComboBoxModel cbmTypeFacility;
    private ComboBoxModel cbmPurposeCode;
    private ComboBoxModel cbmIndusCode;
    private ComboBoxModel cbm20Code;
    private ComboBoxModel cbmGovtSchemeCode;
    private ComboBoxModel cbmRefinancingInsti;
    private ComboBoxModel cbmSecurityDeails;
    //-----------------------------------------------------------------------------------
    //==============================================================================================
    //Declaration of the Fields in UI
    private String txtProductID = "";
    private String txtProductDesc = "";
    private String cboOperatesLike = "";
    //    private String cboProdCurrency = "";
    private String txtNumPatternFollowed = "";
    private String txtNumPatternFollowedSuffix = "";
    private String txtLastAccNum = "";
    private boolean rdoIsLimitDefnAllowed_Yes = false;
    private boolean rdoIsLimitDefnAllowed_No = false;
    private boolean rdoIsStaffAccOpened_Yes = false;
    private boolean rdoIsStaffAccOpened_No = false;
    private boolean rdoIsDebitInterUnderClearing_Yes = false;
    private boolean rdoIsDebitInterUnderClearing_No = false;
    private String txtAccHead = "";
    private boolean rdoldebitInterCharged_Yes = false;
    private boolean rdoldebitInterCharged_No = false;
    private boolean rdoasAndWhenCustomer_Yes=false;
    private boolean rdoasAndWhenCustomer_No=false;
    private boolean rdocalendarFrequency_Yes=false;
    private boolean rdocalendarFrequency_No=false;
    private boolean chkprincipalDue=false;
    private boolean chkInterestDue=false;
    private String txtMinDebitInterRate = "";
    private String txtMaxDebitInterRate = "";
    private String txtMinDebitInterAmt = "";
    private String txtMaxDebitInterAmt = "";
    private String cboDebInterCalcFreq = "";
    private String cboDebitInterAppFreq = "";
    private String cboDebitInterComFreq = "";
    private String cboDebitProdRoundOff = "";
    private String cboDebitInterRoundOff = "";
    private String tdtLastInterCalcDate = "";
    private String tdtLastInterAppDate = "";
    private String txtPenalApplicableAmt="";
    private String txtReviewPeriod="";
    private boolean rdoPLRRateAppl_Yes = false;
    private boolean rdoPLRRateAppl_No = false;
    private String txtPLRRate = "";
    private String tdtPLRAppForm = "";
    private boolean rdoPLRApplNewAcc_Yes = false;
    private boolean rdoPLRApplNewAcc_No = false;
    private boolean rdoPLRApplExistingAcc_Yes = false;
    private boolean rdoPLRApplExistingAcc_No = false;
    private String tdtPlrApplAccSancForm = "";
    private boolean rdoPenalAppl_Yes = false;
    private boolean rdoPenalAppl_No = false;
    private boolean rdoLimitExpiryInter_Yes = false;
    private boolean rdoLimitExpiryInter_No = false;
    private String txtPenalInterRate = "";
    private String txtExposureLimit_Prud = "";
    private String txtExposureLimit_Prud2 = "";
    private String txtExposureLimit_Policy = "";
    private String txtExposureLimit_Policy2 = "";
    private String cboProductFreq = "";
    private String txtAccClosingCharges = "";
    private String txtMiscSerCharges = "";
    private boolean rdoStatCharges_Yes = false;
    private boolean rdoStatCharges_No = false;
    private String txtStatChargesRate = "";
    private String txtRangeFrom = "";
    private String txtRangeTo = "";
    private String txtRateAmt = "";
    private boolean rdoFolioChargesAppl_Yes = false;
    private boolean rdoFolioChargesAppl_No = false;
    private String tdtLastFolioChargesAppl = "";
    private String txtNoEntriesPerFolio = "";
    private String tdtNextFolioDDate = "";
    private String txtRatePerFolio = "";
    private String cboFolioChargesAppFreq = "";
    private String cboToCollectFolioCharges = "";
    private boolean rdoToChargeOn_Man = false;
    private boolean rdoToChargeOn_Sys = false;
    private boolean rdoToChargeOn_Both = false;
    private boolean rdoToChargeType_Credit=false;
    private boolean rdoToChargeType_Debit=false;
    private boolean rdoToChargeType_Both=false;
    private String cboIncompleteFolioRoundOffFreq = "";
    private boolean rdoProcessCharges_Yes = false;
    private boolean rdoProcessCharges_No = false;
    //    private String txtcharge_Limit1 = "";
    private String txtCharge_Limit2 = "";
    private boolean rdoCommitmentCharge_Yes = false;
    private boolean rdoCommitmentCharge_No = false;
    private String txtCharge_Limit3 = "";
    //    private String txtCharge_Limit4 = "";
    
    private String cboCharge_Limit2 = "";
    private String cboCharge_Limit3 = "";
    
    private boolean rdoATMcardIssued_Yes = false;
    private boolean rdoATMcardIssued_No = false;
    private boolean rdoCreditCardIssued_Yes = false;
    private boolean rdoCreditCardIssued_No = false;
    private boolean rdoMobileBanlingClient_Yes = false;
    private boolean rdoMobileBanlingClient_No = false;
    private boolean rdoIsAnyBranBankingAllowed_Yes = false;
    private boolean rdoIsAnyBranBankingAllowed_No = false;
    private String txtAccClosCharges = "";
    private String txtIntPayableAccount="";
    private String txtLegalCharges="";
    private String txtMiscCharges="";
    private String txtArbitraryCharges="";
    private String txtInsuranceCharges="";
    private String txtExecutionDecreeCharges="";
    private String txtMiscServCharges = "";
    private String txtStatementCharges = "";
    private String txtNoticeCharges="";
    private String txtPostageCharges="";
    private String txtAccDebitInter = "";
    private String txtPenalInter = "";
    private String txtAccCreditInter = "";
    private String txtExpiryInter = "";
    private String txtCheReturnChargest_Out = "";
    private String txtCheReturnChargest_In = "";
    private String txtFolioChargesAcc = "";
    private String txtCommitCharges = "";
    private String txtProcessingCharges = "";
    private String txtMinPeriodsArrears = "";
    private String cboMinPeriodsArrears = "";
    private String txtPeriodTranSStanAssets = "";
    private String cboPeriodTranSStanAssets = "";
    private String txtProvisionsStanAssets = "";
    private String txtPeriodTransDoubtfulAssets1 = "";
    private String txtPeriodTransDoubtfulAssets2 = "";
    private String txtPeriodTransDoubtfulAssets3 = "";
    private String cboPeriodTransDoubtfulAssets1 = "";
    private String cboPeriodTransDoubtfulAssets2 = "";
    private String cboPeriodTransDoubtfulAssets3 = "";
    private String txtProvisionDoubtfulAssets1 = "";
    private String txtProvisionDoubtfulAssets2 = "";
    private String txtProvisionDoubtfulAssets3 = "";
    
    
    private String txtProvisionStandardAssetss = "";
    private String txtProvisionLossAssets = "";
    private String txtPeriodTransLossAssets = "";
    private String cboPeriodTransLossAssets = "";
    private String txtPeriodAfterWhichTransNPerformingAssets = "";
    private String cboPeriodAfterWhichTransNPerformingAssets = "";
    private String cbocalcType = "";
    private String txtMinPeriod = "";
    private String cboMinPeriod = "";
    private String txtMaxPeriod = "";
    private String cboMaxPeriod = "";
    private String cboReviewPeriod="";
    private String txtMinAmtLoan = "";
    private String txtMaxAmtLoan = "";
    private String txtApplicableInter = "";
    //    private String txtMaxInter = "";
    private String txtMinInterDebited = "";
    private boolean rdoSubsidy_Yes = false;
    private boolean rdoSubsidy_No = false;
    private String cboLoanPeriodMul = "";
    // Fields Required for Documents
    private ComboBoxModel cbmDocumentType;
    private ComboBoxModel cbmNoticeType;
    private ComboBoxModel cbmIssueAfter;
    private String txtDocumentNo = "";
    private String txtDocumentDesc = "";
    private String cboDocumentType = "";
    private String documentSerialNo = "";
    private String documentStatus = "";
    private static int notice_Charge_No=1;
    private static int docSI_No = 1;// Seral No for Documents table
    private static int deletedDocSI_No = 1;// Seral No for Deleted Documents
    private static int deleted_NoticeCharge=1;
    private LinkedHashMap documentsTO = null;// Contains Only the Documents which the Status is not DELETED
    private LinkedHashMap deletedDocumentsTO = null;// Contains Only the Documents which the Status is DELETED
    private LinkedHashMap deletedNoticeType=null;
    private LinkedHashMap mainDocumentsTO = null;// Contains Both the Documents
    private LinkedHashMap NoticeTypeTO=null;//contain not deleted records
    private ArrayList entireNoticeTypeRow=null;
    private ArrayList entireRows = null;// All row collections of Documents Table
    private final String STATUS_WITH_DELETE = "STATUS_WITH_DELETE";
    private final String STATUS_WITHOUT_DELETE = "STATUS_WITHOUT_DELETE";
    
    
    private String cboCommodityCode = "";
    private String cboGuaranteeCoverCode = "";
    private String cboSectorCode = "";
    private String cboHealthCode = "";
    private String cboTypeFacility = "";
    private String cboPurposeCode = "";
    private String cboIndusCode = "";
    private String cbo20Code = "";
    private String cboRefinancingInsti = "";
    private String cboGovtSchemeCode = "";
    private String cboSecurityDeails = "";
    private boolean chkDirectFinance = false;
    private boolean chkECGC = false;
    private boolean chkQIS = false;
    private List loanProductClassificationsTOList=null;
    
    
    // USED IN setLoanProductChargesTab()
    AgriLoanProductChargesTabTO objLoanProductChargesTabTO;
    
    private final static Logger log = Logger.getLogger(NewLoanAgriProductUI.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    // Initial Action type is set to cancel...
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private static NewLoanAgriProductOB loanProductOB;
    static {
        try {
            log.info("static LoanProductOB...");
            loanProductOB = new NewLoanAgriProductOB();
            
        } catch(Exception e) {
            log.info("Error in LoanProductOB Declaration");
            parseException.logException(e,true);
        }
    }
    
    /** Creates a new instance of LoanProductOB */
    private NewLoanAgriProductOB() throws Exception{
        curDate = ClientUtil.getCurrentDate();
        initianSetup();
    }
    
    private void initianSetup() throws Exception{
        setOperationMap();
        try {
            log.info("In LoanProductOB...");
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            log.info("Exception Caught in LoanProductOB Constructor...");
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        fillDropdown();// To Fill all the Combo Boxes
        setChargeTabTitle();// To set the Title of Table in Charges Tab...
        tblChargeTab = new EnhancedTableModel(null, chargeTabTitle);
        setDocumentTabTitle();// Sets the Column Names for Documents Table
        
        tblDocuments = new EnhancedTableModel(null, documentsTabTitle);
        setNoticeChargeTab();
        tblNoticeCharge=new EnhancedTableModel(null,noticeChargeTabTitle);
        setInsuranceTabTitle();
        tblInsurance=new EnhancedTableModel(null,insurnaceTabTitle);
        insuranceModel.setAttributeKey("SLNO");
        docSI_No = 1;// Seral No for Documents table
        deletedDocSI_No = 1;// Seral No for Deleted Documents
    }
    
    // To set the Column title in Table...
    private void setChargeTabTitle() throws Exception{
        log.info("In setChargeTabTitle...");
        
        chargeTabTitle.add(objLoanProductRB.getString("tblColumn1"));
        chargeTabTitle.add(objLoanProductRB.getString("tblColumn2"));
        chargeTabTitle.add(objLoanProductRB.getString("tblColumn3"));
    }
    private void setNoticeChargeTab()throws Exception{
        noticeChargeTabTitle.add(objLoanProductRB.getString("tblColumn7"));
        noticeChargeTabTitle.add(objLoanProductRB.getString("tblColumn8"));
        noticeChargeTabTitle.add(objLoanProductRB.getString("tblColumn9"));
        noticeChargeTabTitle.add(objLoanProductRB.getString("tblColumn10"));
        
        
    }
    // To set the Column title in Document Table...
    private void setDocumentTabTitle() throws Exception{
        log.info("In setDocumentTabTitle...");
        documentsTabTitle.add(objLoanProductRB.getString("tblColumn4"));
        documentsTabTitle.add(objLoanProductRB.getString("tblColumn5"));
        documentsTabTitle.add(objLoanProductRB.getString("tblColumn6"));
    }
    
    // To set the Column title in Document Table...
    private void setInsuranceTabTitle() throws Exception{
        log.info("In setDocumentTabTitle...");
        
        insurnaceTabTitle.add(objLoanProductRB.getString("tblColumn15"));
        insurnaceTabTitle.add(objLoanProductRB.getString("tblColumn11"));
        insurnaceTabTitle.add(objLoanProductRB.getString("tblColumn12"));
        insurnaceTabTitle.add(objLoanProductRB.getString("tblColumn13"));
        insurnaceTabTitle.add(objLoanProductRB.getString("tblColumn14"));
        insurnaceTabTitle.add(objLoanProductRB.getString("tblColumn16"));
    }
    /**
     * @return returns LoanProductOB object for Singleton implementation
     */
    public static NewLoanAgriProductOB getInstance() {
        log.info("In getInstance...");
        
        return loanProductOB;
    }
    
    // To set the Connections...
    private void setOperationMap() throws Exception{
        log.info("In setOperationMap...");
        
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "AgriLoanProductJNDI");
        operationMap.put(CommonConstants.HOME, "product.loan.agriculturecard.AgriLoanProductHome");
        operationMap.put(CommonConstants.REMOTE, "product.loan.agriculturecard.AgriLoanProduct");
    }
    
    //To fill The Combo Boxes...
    private void fillDropdown() throws Exception{
        log.info("In fillDropdown...");
        
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        
        lookup_keys.add("LOANPRODUCT.OPERATESLIKE");
        lookup_keys.add("FREQUENCY");
        lookup_keys.add("OPERATIVEACCTPRODUCT.ROUNDOFF");
        //        lookup_keys.add("FOREX.CURRENCY");
        lookup_keys.add("OPERATIVEACCTPRODUCT.FOLIOCHRG");
        lookup_keys.add("PERIOD");
        lookup_keys.add("LOANPRODUCT.CALCTYPE");
        lookup_keys.add("LOANPRODUCT.LOANPERIODS");
        lookup_keys.add("ADVANCESPRODUCT.CHARGETYPE");
        lookup_keys.add("LOANPRODUCT.DOCUMENT_TYPE");
        
        lookup_keys.add("TERM_LOAN.COMMODITY_CODE");
        lookup_keys.add("TERM_LOAN.SECTOR_CODE");
        lookup_keys.add("TERM_LOAN.PURPOSE_CODE");
        lookup_keys.add("TERM_LOAN.INDUSTRY_CODE");
        lookup_keys.add("TERM_LOAN.20_CODE");
        lookup_keys.add("TERM_LOAN.GOVT_SCHEME_CODE");
        lookup_keys.add("TERM_LOAN.GUARANTEE_COVER_CODE");
        lookup_keys.add("TERM_LOAN.HEALTH_CODE");
        lookup_keys.add("TERM_LOAN.REFINANCING_INSTITUTION");
        lookup_keys.add("TERM_LOAN.FACILITY");
        lookup_keys.add("LOANPRODUCT.SECURITY_TYPE");
        lookup_keys.add("TERM_LOAN.NOTICE_TYPE");
        lookup_keys.add("TERM_LOAN.ISSUE_AFTER");
        lookup_keys.add("TERMLOAN.AGRI_INSURANCE_TYPE");
        lookup_keys.add("TERMLOAN.AGRI_INSURANCE_UNDER_SCHEME");
        
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("LOANPRODUCT.OPERATESLIKE"));
        cbmOperatesLike = new ComboBoxModel(key,value);
        
        //        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("LOANPRODUCT.DOCUMENT_TYPE"));
        cbmDocumentType = new ComboBoxModel(key,value);
        
        //        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("FREQUENCY"));
        cbmDebInterCalcFreq = new ComboBoxModel(key,value);
        cbmDebitInterAppFreq = new ComboBoxModel(key,value);
        cbmProductFreq = new ComboBoxModel(key,value);
        cbmFolioChargesAppFreq = new ComboBoxModel(key,value);
        
        //        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("OPERATIVEACCTPRODUCT.ROUNDOFF"));
        cbmDebitProdRoundOff = new ComboBoxModel(key,value);
        cbmDebitInterRoundOff = new ComboBoxModel(key,value);
        
        //        //        keyValue = ClientUtil.populateLookupData(lookUpHash);
        //        getKeyValue((HashMap)keyValue.get("FOREX.CURRENCY"));
        //        cbmProdCurrency = new ComboBoxModel(key,value);
        
        //        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("OPERATIVEACCTPRODUCT.FOLIOCHRG"));
        cbmToCollectFolioCharges = new ComboBoxModel(key,value);
        
        //        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("PERIOD"));
        cbmMinPeriodsArrears = new ComboBoxModel(key,value);
        cbmPeriodTranSStanAssets = new ComboBoxModel(key,value);
        cbmPeriodTransDoubtfulAssets1 = new ComboBoxModel(key,value);
        cbmPeriodTransDoubtfulAssets2 = new ComboBoxModel(key,value);
        cbmPeriodTransDoubtfulAssets3 = new ComboBoxModel(key,value);
        cbmPeriodTransLossAssets = new ComboBoxModel(key,value);
        cbmPeriodAfterWhichTransNPerformingAssets = new ComboBoxModel(key,value);
        cbmMinPeriod = new ComboBoxModel(key,value);
        cbmMaxPeriod = new ComboBoxModel(key,value);
        
        
        //        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("LOANPRODUCT.CALCTYPE"));
        cbmcalcType = new ComboBoxModel(key,value);
        
        //        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("LOANPRODUCT.LOANPERIODS"));
        cbmDebitInterComFreq = new ComboBoxModel(key,value);
        cbmIncompleteFolioRoundOffFreq = new ComboBoxModel(key,value);
        cbmLoanPeriodMul = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("ADVANCESPRODUCT.CHARGETYPE"));
        cbmCharge_Limit2 = new ComboBoxModel(key,value);
        cbmCharge_Limit3 = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.COMMODITY_CODE"));
        cbmCommodityCode = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.SECTOR_CODE"));
        cbmSectorCode = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.PURPOSE_CODE"));
        cbmPurposeCode = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.INDUSTRY_CODE"));
        cbmIndusCode = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.20_CODE"));
        cbm20Code = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.GOVT_SCHEME_CODE"));
        cbmGovtSchemeCode = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.GUARANTEE_COVER_CODE"));
        cbmGuaranteeCoverCode = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.HEALTH_CODE"));
        cbmHealthCode = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.REFINANCING_INSTITUTION"));
        cbmRefinancingInsti = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.FACILITY"));
        cbmTypeFacility = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("LOANPRODUCT.SECURITY_TYPE"));
        cbmSecurityDeails = new ComboBoxModel(key,value);
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.NOTICE_TYPE"));
        cbmNoticeType=new ComboBoxModel(key,value);
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.ISSUE_AFTER"));
        cbmIssueAfter=new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("TERMLOAN.AGRI_INSURANCE_TYPE"));
        cbmInsuranceType=new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("TERMLOAN.AGRI_INSURANCE_UNDER_SCHEME"));
        cbmInsuranceUnderScheme=new ComboBoxModel(key,value);
        
         getKeyValue((HashMap)keyValue.get("PERIOD"));
        cbmReviewPeriod=new ComboBoxModel(key,value);
        
        
    }
    
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        log.info("In getKeyValue...");
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    //------------------------------------------------------------------------------------------------
    //Do display the Data from the Database, in UI
    public void populateData(HashMap whereMap) {
        log.info("In populateData...");
        final HashMap mapData;
        try {
            mapData = proxy.executeQuery(whereMap, operationMap);
            log.info("proxy.executeQuery is working fine");
            populateOB(mapData);
        } catch( Exception e ) {
            log.info("The error in populateData");
            parseException.logException(e,true);
            //e.printStackTrace();
        }
    }
    
    private void populateOB(HashMap mapData) throws Exception{
        log.info("In populateOB...");
        
        AgriLoanProductAccountTO objLoanProductAccountTO = null;
        AgriLoanProductAccountParamTO objLoanProductAccountParamTO = null;
        AgriLoanProductInterReceivableTO objLoanProductInterReceivableTO = null;
        AgriLoanProductChargesTO objLoanProductChargesTO = null;
        // Fo adding the Data into the Table and
        // adding the table into the Database...
        ArrayList arrayLoanProductChargesTabTO = null;
        AgriLoanProductSpeItemsTO objLoanProductSpeItemsTO = null;
        AgriLoanProductAccHeadTO objLoanProductAccHeadTO = null;
        AgriLoanProductNonPerAssetsTO objLoanProductNonPerAssetsTO = null;
        AgriLoanProductInterCalcTO objLoanProductInterCalcTO = null;
        AgriLoanProductClassificationsTO objLoanProductClassificationsTO = null;
       
        
        //Taking the Value of Prod_Id from each Table...
        objLoanProductAccountTO = (AgriLoanProductAccountTO) ((List) mapData.get("AgriLoanProductAccountTO")).get(0);
        setLoanProductAccountTO(objLoanProductAccountTO);
        
        objLoanProductAccountParamTO = (AgriLoanProductAccountParamTO) ((List) mapData.get("AgriLoanProductAccountParamTO")).get(0);
        setLoanProductAccountParamTO(objLoanProductAccountParamTO);
        
        objLoanProductInterReceivableTO = (AgriLoanProductInterReceivableTO) ((List) mapData.get("AgriLoanProductInterReceivableTO")).get(0);
        setLoanProductInterReceivableTO(objLoanProductInterReceivableTO);
        
        objLoanProductChargesTO = (AgriLoanProductChargesTO) ((List) mapData.get("AgriLoanProductChargesTO")).get(0);
        setLoanProductChargesTO(objLoanProductChargesTO);
        
        arrayLoanProductChargesTabTO =  (ArrayList) (mapData.get("AgriLoanProductChargesTabTO"));
        setLoanProductChargesTabTO(arrayLoanProductChargesTabTO);
        
        objLoanProductSpeItemsTO = (AgriLoanProductSpeItemsTO) ((List) mapData.get("AgriLoanProductSpeItemsTO")).get(0);
        setLoanProductSpeItemsTO(objLoanProductSpeItemsTO);
        
        objLoanProductAccHeadTO = (AgriLoanProductAccHeadTO) ((List) mapData.get("AgriLoanProductAccHeadTO")).get(0);
        setLoanProductAccHeadTO(objLoanProductAccHeadTO);
        
        objLoanProductNonPerAssetsTO = (AgriLoanProductNonPerAssetsTO) ((List) mapData.get("AgriLoanProductNonPerAssetsTO")).get(0);
        setLoanProductNonPerAssetsTO(objLoanProductNonPerAssetsTO);
        
        objLoanProductInterCalcTO = (AgriLoanProductInterCalcTO) ((List) mapData.get("AgriLoanProductInterCalcTO")).get(0);
        setLoanProductInterCalcTO(objLoanProductInterCalcTO);
        
        System.out.println("#### Before Classifi list in OB : ");
        loanProductClassificationsTOList = (List) mapData.get("AgriLoanProductClassificationsTO");
        //change27-3-07
        if (loanProductClassificationsTOList.size()!=0) {
            objLoanProductClassificationsTO = (AgriLoanProductClassificationsTO) loanProductClassificationsTOList.get(0);
            setLoanProductClassificationsTO(objLoanProductClassificationsTO);
        } else
            loanProductClassificationsTOList = null;
        System.out.println("#### Classifi list in OB : "+objLoanProductClassificationsTO);
        
        List list = (List) mapData.get("AgriLoanProductDocumentsTO");
        //AGRILOANSINSURANCE DETAILS
        tblInsurance.setDataArrayList(null,insurnaceTabTitle);
        setInsuranceDetails((List)mapData.get("AgriLoanProductInsuranceTO"));
        populateOBDocuments(list);
        ttNotifyObservers();
    }
    /**
     * To Populate Documents
     */
    private void populateOBDocuments(List list) throws Exception {
        log.info("In populateOBDocuments...");
        System.out.println("###### List : "+ list);
        if (list != null) {
            if (documentsTO == null)
                documentsTO = new LinkedHashMap();
            if (entireRows == null)
                entireRows = new ArrayList();
            for (int i=0,j=list.size();i<j;i++) {
                AgriLoanProductDocumentsTO objLoanProductDocumentsTO = (AgriLoanProductDocumentsTO) list.get(i);
                System.out.println("### objLoanProductDocumentsTO : " + objLoanProductDocumentsTO);
                entireRows.add(setColumnValuesForDocumentsTable(docSI_No, objLoanProductDocumentsTO));
                documentsTO.put(String.valueOf(docSI_No),objLoanProductDocumentsTO);
                docSI_No++;
                objLoanProductDocumentsTO = null;
            }
            tblDocuments.setDataArrayList(entireRows,documentsTabTitle);
        }
        
    }
    //==========ACCOUNT==========
    private void setLoanProductAccountTO(AgriLoanProductAccountTO objLoanProductAccountTO) throws Exception{
        log.info("In setLoanProductAccountTO...");
        
        setStatusBy(objLoanProductAccountTO.getStatusBy());
        setTxtProductID(CommonUtil.convertObjToStr(objLoanProductAccountTO.getProdId()));
        setTxtProductDesc(CommonUtil.convertObjToStr(objLoanProductAccountTO.getProdDesc()));
        setCboOperatesLike((String) getCbmOperatesLike().getDataForKey(CommonUtil.convertObjToStr(objLoanProductAccountTO.getBehavesLike())));
        //        setCboProdCurrency((String) getCbmProdCurrency().getDataForKey(CommonUtil.convertObjToStr(objLoanProductAccountTO.getBaseCurrency())));
        setTxtAccHead(CommonUtil.convertObjToStr(objLoanProductAccountTO.getAcctHead()));
    }
    private void setInsuranceDetails(List lst){
         AgriLoanProductInsuranceTO objAgriLoanProductInsuranceTO = null;
         ArrayList totList=new ArrayList();
        ArrayList singleList=new ArrayList();
        LinkedHashMap totMap=new LinkedHashMap();
        if(lst !=null && lst.size()>0){
            for(int i=0;i<lst.size();i++){
                objAgriLoanProductInsuranceTO=(AgriLoanProductInsuranceTO)lst.get(i);
                singleList=new ArrayList();
                singleInsMap=new LinkedHashMap();
                singleList.add(objAgriLoanProductInsuranceTO.getSlno());
                singleList.add(objAgriLoanProductInsuranceTO.getInsuranceType());
                singleList.add(objAgriLoanProductInsuranceTO.getInsuranceUnderScheme());
                singleList.add(objAgriLoanProductInsuranceTO.getBankSharePremium());
                singleList.add(objAgriLoanProductInsuranceTO.getCustomerSharePremium());
                singleList.add(objAgriLoanProductInsuranceTO.getInsuranceAmt());
                totList.add(singleList);
        singleInsMap.put("SLNO",objAgriLoanProductInsuranceTO.getSlno());
        singleInsMap.put("INSURANCE TYPE",objAgriLoanProductInsuranceTO.getInsuranceType());
        singleInsMap.put("INSURANCE UNDER SCHEME",objAgriLoanProductInsuranceTO.getInsuranceUnderScheme());
        singleInsMap.put("BANK SHARE",objAgriLoanProductInsuranceTO.getBankSharePremium());
        singleInsMap.put("CUSTOMER SHARE",objAgriLoanProductInsuranceTO.getCustomerSharePremium());
        singleInsMap.put("INSURANCE AMT",objAgriLoanProductInsuranceTO.getInsuranceAmt());
        singleInsMap.put("COMMAND","UPDATE");
        totMap.put(objAgriLoanProductInsuranceTO.getSlno(),singleInsMap);
            }
//            allInsuranceList.clear();
//            allInsuranceMap.clear();
            allInsuranceList=totList;
            allInsuranceMap=totMap;
           insuranceModel.setAllValues(allInsuranceMap);
           insuranceModel.setTableValues(allInsuranceList);
              tblInsurance.setDataArrayList(allInsuranceList,insurnaceTabTitle);
            
        }
        totList=null;
        totMap=null;
    }
    private void setLoanProductAccountParamTO(AgriLoanProductAccountParamTO objLoanProductAccountParamTO) throws Exception{
        log.info("In setLoanProductAccountParamTO...");
        
        setTxtNumPatternFollowed(CommonUtil.convertObjToStr(objLoanProductAccountParamTO.getNumberPattern()));
        setTxtNumPatternFollowedSuffix(CommonUtil.convertObjToStr(objLoanProductAccountParamTO.getNumberPatternSuffix()));
        setTxtLastAccNum(CommonUtil.convertObjToStr(objLoanProductAccountParamTO.getLastAcNo()));
        
        if (CommonUtil.convertObjToStr(objLoanProductAccountParamTO.getLimitDefAllowed()).equals(YES)) setRdoIsLimitDefnAllowed_Yes(true);
        else setRdoIsLimitDefnAllowed_No(true);
        
        if (CommonUtil.convertObjToStr(objLoanProductAccountParamTO.getStaffAcOpened()).equals(YES)) setRdoIsStaffAccOpened_Yes(true);
        else setRdoIsStaffAccOpened_No(true);
        
        if (CommonUtil.convertObjToStr(objLoanProductAccountParamTO.getDebitIntClearingappl()).equals(YES)) setRdoIsDebitInterUnderClearing_Yes(true);
        else setRdoIsDebitInterUnderClearing_No(true);
    }
    
    //==========INTEREST RECEIVABLE==========
    private void setLoanProductInterReceivableTO(AgriLoanProductInterReceivableTO objLoanProductInterReceivableTO) throws Exception{
        log.info("In setLoanProductInterReceivableTO...");
        
        if (objLoanProductInterReceivableTO.getDebitIntCharged().equals(YES)) setRdoldebitInterCharged_Yes(true);
        else setRdoldebitInterCharged_No(true);
        
        setTxtMinDebitInterRate(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getMinDebitintRate()));
        setTxtMaxDebitInterRate(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getMaxDebitintRate()));
        setTxtMinDebitInterAmt(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getMinDebitintAmt()));
        setTxtMaxDebitInterAmt(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getMaxDebitintAmt()));
        setCboDebInterCalcFreq((String) getCbmDebInterCalcFreq().getDataForKey( CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getDebitintCalcFreq())));
        setCboDebitInterAppFreq((String) getCbmDebitInterAppFreq().getDataForKey( CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getDebitintApplFreq())));
        setCboDebitInterComFreq((String) getCbmDebitInterComFreq().getDataForKey( CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getDebitintCompFreq())));
        setCboDebitProdRoundOff((String) getCbmDebitProdRoundOff().getDataForKey(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getDebitProdRoundoff())));
        setCboDebitInterRoundOff((String) getCbmDebitInterRoundOff().getDataForKey(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getDebitIntRoundoff())));
        setTdtLastInterCalcDate(DateUtil.getStringDate(objLoanProductInterReceivableTO.getLastIntcalcDtdebit()));
        setTdtLastInterAppDate(DateUtil.getStringDate(objLoanProductInterReceivableTO.getLastIntapplDtdebit()));
        setTxtPenalApplicableAmt(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getPenalExceptionAmt()));
        
        if (CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getPlrRateAppl()).equals(YES)) setRdoPLRRateAppl_Yes(true);
        else setRdoPLRRateAppl_No(true);
        
        setTxtPLRRate(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getPlrRate()));
        setTdtPLRAppForm(DateUtil.getStringDate(objLoanProductInterReceivableTO.getPlrApplFrom()));
        
        if (CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getPlrApplNewac()).equals(YES)) setRdoPLRApplNewAcc_Yes(true);
        else setRdoPLRApplNewAcc_No(true);
        
        if (CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getPlrApplExistac()).equals(YES)) setRdoPLRApplExistingAcc_Yes(true);
        else setRdoPLRApplExistingAcc_No(true);
        
        setTdtPlrApplAccSancForm(DateUtil.getStringDate(objLoanProductInterReceivableTO.getPlrApplSancfrom()));
        
        if (CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getPenalAppl()).equals(YES)) setRdoPenalAppl_Yes(true);
        else setRdoPenalAppl_No(true);
        
        if (CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getLimitExpiryInt()).equals(YES)) setRdoLimitExpiryInter_Yes(true);
        else setRdoLimitExpiryInter_No(true);
        
        if (CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getAsAndWhenCustomer_Yes()).equals(YES)) setRdoasAndWhenCustomer_Yes(true);
        else setRdoasAndWhenCustomer_No(true);
        if (CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getCalendarFrequency_Yes()).equals(YES)) setRdocalendarFrequency_Yes(true);
        else setRdocalendarFrequency_No(true);
        if (CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getPrincipalDue()).equals(YES)) setChkprincipalDue(true);
        else setChkprincipalDue(false);
        if (CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getInterestDue()).equals(YES)) setChkInterestDue(true);
        else setChkInterestDue(false);
        
        setTxtPenalInterRate(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getPenalIntRate()));
        setTxtExposureLimit_Prud(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getExpoLmtPrudentialamt()));
        setTxtExposureLimit_Prud2(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getExpoLmtPrudentialper()));
        setTxtExposureLimit_Policy(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getExpoLmtPolicyamt()));
        setTxtExposureLimit_Policy2(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getExpoLmtPolicyper()));
        setCboProductFreq((String) getCbmProductFreq().getDataForKey(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getProdFreq())));
    }
    
    //==========CHARGES==========
    private void setLoanProductChargesTO(AgriLoanProductChargesTO objLoanProductChargesTO) throws Exception{
        log.info("In setLoanProductChargesTO...");
        
        setTxtAccClosingCharges(CommonUtil.convertObjToStr(objLoanProductChargesTO.getAcClosingChrg()));
        setTxtMiscSerCharges(CommonUtil.convertObjToStr(objLoanProductChargesTO.getMiscServChrg()));
        
        if (CommonUtil.convertObjToStr(objLoanProductChargesTO.getStatChrg()).equals(YES)) setRdoStatCharges_Yes(true);
        else setRdoStatCharges_No(true);
        
        setTxtStatChargesRate(CommonUtil.convertObjToStr(objLoanProductChargesTO.getStatChrgRate()));
        
        if (CommonUtil.convertObjToStr(objLoanProductChargesTO.getFolioChrgAppl()).equals(YES)) setRdoFolioChargesAppl_Yes(true);
        else setRdoFolioChargesAppl_No(true);
        
        setTdtLastFolioChargesAppl(CommonUtil.convertObjToStr(DateUtil.getStringDate(objLoanProductChargesTO.getLastFolioChrgon())));
        setTxtNoEntriesPerFolio(CommonUtil.convertObjToStr(objLoanProductChargesTO.getNoEntriesPerFolio()));
        setTdtNextFolioDDate(DateUtil.getStringDate(objLoanProductChargesTO.getNextFolioDuedate()));
        setTxtRatePerFolio(CommonUtil.convertObjToStr(objLoanProductChargesTO.getRatePerFolio()));
        setCboFolioChargesAppFreq((String) getCbmFolioChargesAppFreq().getDataForKey(CommonUtil.convertObjToStr(objLoanProductChargesTO.getFolioChrgApplfreq())));
        setCboToCollectFolioCharges((String) getCbmToCollectFolioCharges().getDataForKey(CommonUtil.convertObjToStr(objLoanProductChargesTO.getToCollectFoliochrg())));
        
        if (CommonUtil.convertObjToStr(objLoanProductChargesTO.getToCollectChrgOn()).equals(BOTH)) setRdoToChargeOn_Both(true);
        else if (CommonUtil.convertObjToStr(objLoanProductChargesTO.getToCollectChrgOn()).equals(MANUAL)) setRdoToChargeOn_Man(true);
        else if (CommonUtil.convertObjToStr(objLoanProductChargesTO.getToCollectChrgOn()).equals(SYSTEM)) setRdoToChargeOn_Sys(true) ;
        if (CommonUtil.convertObjToStr(objLoanProductChargesTO.getToCollectFoliochrg()).equals("CREDIT")) setRdoToChargeType_Credit(true);
        else if (CommonUtil.convertObjToStr(objLoanProductChargesTO.getToCollectFoliochrg()).equals("DEBIT")) setRdoToChargeType_Debit(true);
        else if (CommonUtil.convertObjToStr(objLoanProductChargesTO.getToCollectFoliochrg()).equals("BOTH")) setRdoToChargeType_Both(true) ;
        
        setCboIncompleteFolioRoundOffFreq((String) getCbmIncompleteFolioRoundOffFreq().getDataForKey(CommonUtil.convertObjToStr(objLoanProductChargesTO.getIncompFolioRoundoff())));
        
        if (CommonUtil.convertObjToStr(objLoanProductChargesTO.getProcChrg()).equals(YES)) setRdoProcessCharges_Yes(true);
        else setRdoProcessCharges_No(true);
        
        //        setTxtcharge_Limit1(CommonUtil.convertObjToStr(objLoanProductChargesTO.getProcChrgPer()));
        
        if(CommonUtil.convertObjToInt(objLoanProductChargesTO.getProcChrgPer())!=0){
            setTxtCharge_Limit2(CommonUtil.convertObjToStr(objLoanProductChargesTO.getProcChrgPer()));
            setCboCharge_Limit2(PERCENT);
        }else if(CommonUtil.convertObjToInt(objLoanProductChargesTO.getProcChrgAmt())!= 0){
            setTxtCharge_Limit2(CommonUtil.convertObjToStr(objLoanProductChargesTO.getProcChrgAmt()));
            setCboCharge_Limit2(ABSOLUTE);
        }
        
        if (CommonUtil.convertObjToStr(objLoanProductChargesTO.getCommitChrg()).equals(YES)) setRdoCommitmentCharge_Yes(true);
        else setRdoCommitmentCharge_No(true);
        
        //        setTxtCharge_Limit3(CommonUtil.convertObjToStr(objLoanProductChargesTO.getCommitChrgPer()));
        if(CommonUtil.convertObjToInt(objLoanProductChargesTO.getCommitChrgPer())!=0){
            setTxtCharge_Limit3(CommonUtil.convertObjToStr(objLoanProductChargesTO.getCommitChrgPer()));
            setCboCharge_Limit3(PERCENT);
        }else if(CommonUtil.convertObjToInt(objLoanProductChargesTO.getCommitChrgAmt())!=0){
            setTxtCharge_Limit3(CommonUtil.convertObjToStr(objLoanProductChargesTO.getCommitChrgAmt()));
            setCboCharge_Limit3(ABSOLUTE);
        }
    }
    
    private void setLoanProductChargesTabTO(ArrayList arrayLoanProductChargesTabTO) throws Exception{
        log.info("In setLoanProductChargesTabTO...");
        System.out.println("setLoanproductcharge###"+arrayLoanProductChargesTabTO);
        AgriLoanProductChargesTabTO objLoanProductChargesTabTO= new AgriLoanProductChargesTabTO();
        NoticeTypeTO=new LinkedHashMap();
        entireNoticeTypeRow=new ArrayList();
        for (int i=0, j= arrayLoanProductChargesTabTO.size();i<j;i++){
            chargeTabRow = new ArrayList();
            
            objLoanProductChargesTabTO = (AgriLoanProductChargesTabTO)arrayLoanProductChargesTabTO.get(i);
            NoticeTypeTO.put(String.valueOf(notice_Charge_No),objLoanProductChargesTabTO);
            entireNoticeTypeRow.add(setColumnValueNoticeCharge(notice_Charge_No, objLoanProductChargesTabTO));
            notice_Charge_No++;
            //            chargeTabRow.add(CommonUtil.convertObjToStr(objLoanProductChargesTabTO.getChqReturnChrgFrom()));
            //            chargeTabRow.add(CommonUtil.convertObjToStr(objLoanProductChargesTabTO.getChqReturnChrgTo()));
            //            chargeTabRow.add(CommonUtil.convertObjToStr(objLoanProductChargesTabTO.getChqReturnChrgRate()));
            //            tblChargeTab.insertRow(0,chargeTabRow);
            
            objLoanProductChargesTabTO = (AgriLoanProductChargesTabTO)arrayLoanProductChargesTabTO.get(i);
            chargeTabRow.add(CommonUtil.convertObjToStr(objLoanProductChargesTabTO.getNoticeType()));
            chargeTabRow.add(CommonUtil.convertObjToStr(objLoanProductChargesTabTO.getIssueAfter()));
            chargeTabRow.add(CommonUtil.convertObjToDouble(objLoanProductChargesTabTO.getNoticeChargeAmt()));
            chargeTabRow.add(CommonUtil.convertObjToDouble(objLoanProductChargesTabTO.getPostageAmt()));
            
            tblNoticeCharge.insertRow(i,chargeTabRow);
            //            tblNoticeCharge.setDataArrayList(chargeTabRow,noticeChargeTabTitle);
        }
    }
    
    //==========SPECIAL ITEMS==========
    private void setLoanProductSpeItemsTO(AgriLoanProductSpeItemsTO objLoanProductSpeItemsTO) throws Exception{
        log.info("In setLoanProductSpeItemsTO...");
        
        if (CommonUtil.convertObjToStr(objLoanProductSpeItemsTO.getAtmCardIssued()).equals(YES)) setRdoATMcardIssued_Yes(true);
        else setRdoATMcardIssued_No(true);
        
        if (CommonUtil.convertObjToStr(objLoanProductSpeItemsTO.getCrCardIssued()).equals(YES)) setRdoCreditCardIssued_Yes(true);
        else setRdoCreditCardIssued_No(true);
        
        if (CommonUtil.convertObjToStr(objLoanProductSpeItemsTO.getMobileBankClient()).equals(YES)) setRdoMobileBanlingClient_Yes(true);
        else setRdoMobileBanlingClient_No(true);
        
        if (CommonUtil.convertObjToStr(objLoanProductSpeItemsTO.getBranchBankingAllowed()).equals(YES)) setRdoIsAnyBranBankingAllowed_Yes(true);
        else setRdoIsAnyBranBankingAllowed_No(true);
    }
    
    //==========ACCOUNT HEAD==========
    private void setLoanProductAccHeadTO(AgriLoanProductAccHeadTO objLoanProductAccHeadTO) throws Exception{
        log.info("In setLoanProductAccHeadTO...");
        
        setTxtAccClosCharges(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getAcClosingChrg()));
        setTxtMiscServCharges(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getMiscServChrg()));
        setTxtStatementCharges(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getStatChrg()));
        setTxtAccDebitInter(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getAcDebitInt()));
        setTxtPenalInter(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getPenalInt()));
        setTxtAccCreditInter(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getAcCreditInt()));
        setTxtExpiryInter(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getExpiryInt()));
        setTxtCheReturnChargest_Out(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getChqRetChrgOutward()));
        setTxtCheReturnChargest_In(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getChqRetChrgInward()));
        setTxtFolioChargesAcc(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getFolioChrgAc()));
        setTxtCommitCharges(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getCommitmentChrg()));
        setTxtProcessingCharges(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getProcChrg()));
        setTxtNoticeCharges(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getNoticeCharges()));
        setTxtPostageCharges(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getPostageCharges()));
        setTxtIntPayableAccount(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getIntPayableAcHd()));
        setTxtLegalCharges(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getLegalCharges()));
        //        setTxtMiscCharges(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getMiscCharges()));
        setTxtArbitraryCharges(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getArbitraryCharges()));
        setTxtInsuranceCharges(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getInsuranceCharges()));
        setTxtExecutionDecreeCharges(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getExecutionDecreeCharges()));
        setTxtInsurancePremiumDebit(objLoanProductAccHeadTO.getInsurancePremiumDebit());
    }
    
    //==========NON PERFORMING ASSET==========
    private void setLoanProductNonPerAssetsTO(AgriLoanProductNonPerAssetsTO objLoanProductNonPerAssetsTO) throws Exception {
        log.info("In setLoanProductNonPerAssetsTO...");
        
        
        setTxtProvisionStandardAssetss(CommonUtil.convertObjToStr(objLoanProductNonPerAssetsTO.getProvisionStdAssets()));
        
        resultValue =  CommonUtil.convertObjToInt(objLoanProductNonPerAssetsTO.getMinPeriodArrears());
        String period = setPeriod(resultValue);
        setCboMinPeriodsArrears(period);
        setTxtMinPeriodsArrears(String.valueOf(resultValue));
        resetPeriod();
        
        resultValue = CommonUtil.convertObjToInt(objLoanProductNonPerAssetsTO.getPeriodTransSubstandard());
        period = setPeriod(resultValue);
        setCboPeriodTranSStanAssets(period);
        setTxtPeriodTranSStanAssets(String.valueOf(resultValue));
        resetPeriod();
        
        setTxtProvisionsStanAssets(CommonUtil.convertObjToStr(objLoanProductNonPerAssetsTO.getProvisionSubstandard()));
        //        if (objLoanProductNonPerAssetsTO.getProvisionSubstandard().equals(YES)) {
        //            setChkProvisionsStanAssets(true);
        //        }else {
        //            setChkProvisionsStanAssets(false);
        //        }
        
        resultValue = CommonUtil.convertObjToInt(objLoanProductNonPerAssetsTO.getPeriodTransDoubtful());
        period = setPeriod(resultValue);
        setCboPeriodTransDoubtfulAssets1(period);
        setTxtPeriodTransDoubtfulAssets1(String.valueOf(resultValue));
        resetPeriod();
        
        setTxtProvisionDoubtfulAssets1(CommonUtil.convertObjToStr(objLoanProductNonPerAssetsTO.getProvisionDoubtful()));
        
        resultValue = CommonUtil.convertObjToInt(objLoanProductNonPerAssetsTO.getPeriodTransDoubtful2());
        period = setPeriod(resultValue);
        setCboPeriodTransDoubtfulAssets2(period);
        setTxtPeriodTransDoubtfulAssets2(String.valueOf(resultValue));
        resetPeriod();
        
        setTxtProvisionDoubtfulAssets2(CommonUtil.convertObjToStr(objLoanProductNonPerAssetsTO.getProvisionDoubtful2()));
        
        resultValue = CommonUtil.convertObjToInt(objLoanProductNonPerAssetsTO.getPeriodTransDoubtful3());
        period = setPeriod(resultValue);
        setCboPeriodTransDoubtfulAssets3(period);
        setTxtPeriodTransDoubtfulAssets3(String.valueOf(resultValue));
        resetPeriod();
        
        setTxtProvisionDoubtfulAssets3(CommonUtil.convertObjToStr(objLoanProductNonPerAssetsTO.getProvisionDoubtful3()));
        
        //        if (objLoanProductNonPerAssetsTO.getProvisionDoubtful().equals(YES)) {
        //            setChkProvisionDoubtfulAssets(true);
        //        }else {
        //            setChkProvisionDoubtfulAssets(false);
        //        }
        
        
        resultValue= CommonUtil.convertObjToInt(objLoanProductNonPerAssetsTO.getPeriodTransLoss());
        period = setPeriod(resultValue);
        setCboPeriodTransLossAssets(period);
        setTxtPeriodTransLossAssets(String.valueOf(resultValue));
        resetPeriod();
        
        setTxtProvisionLossAssets(CommonUtil.convertObjToStr(objLoanProductNonPerAssetsTO.getProvisionLoseAssets()));
        
        resultValue= CommonUtil.convertObjToInt(objLoanProductNonPerAssetsTO.getPeriodTransNoperforming());
        period = setPeriod(resultValue);
        setCboPeriodAfterWhichTransNPerformingAssets(period);
        setTxtPeriodAfterWhichTransNPerformingAssets(String.valueOf(resultValue));
        resetPeriod();
    }
    
    //==========INTEREST CALCULATION==========
    private void setLoanProductInterCalcTO(AgriLoanProductInterCalcTO objLoanProductInterCalcTO) throws Exception{
        log.info("In setLoanProductInterCalcTO...");
        
        setCbocalcType((String) getCbmcalcType().getDataForKey(CommonUtil.convertObjToStr(objLoanProductInterCalcTO.getCalcType())));
        
        resultValue = CommonUtil.convertObjToInt(objLoanProductInterCalcTO.getMinPeriod());
        String period = setPeriod(resultValue);
        setCboMinPeriod(period);
        setTxtMinPeriod(String.valueOf(resultValue));
        resetPeriod();
        
        resultValue = CommonUtil.convertObjToInt(objLoanProductInterCalcTO.getMaxPeriod());
        period = setPeriod(resultValue);
        setCboMaxPeriod(period);
        setTxtMaxPeriod(String.valueOf(resultValue));
        resetPeriod();
        //review loan details
        resultValue = CommonUtil.convertObjToInt(objLoanProductInterCalcTO.getReviewPeriod());
        period = setPeriod(resultValue);
        setCboReviewPeriod(period);
        setTxtReviewPeriod(String.valueOf(resultValue));
        resetPeriod();
        
        setTxtElgLoanAmt(CommonUtil.convertObjToStr(objLoanProductInterCalcTO.getEligibleDepForLoanAmt()));        
        setTxtMinAmtLoan(CommonUtil.convertObjToStr(objLoanProductInterCalcTO.getMinAmtLoan()));
        setTxtMaxAmtLoan(CommonUtil.convertObjToStr(objLoanProductInterCalcTO.getMaxAmtLoan()));
        setTxtApplicableInter(CommonUtil.convertObjToStr(objLoanProductInterCalcTO.getApplInterest()));
        setTxtMinInterDebited(CommonUtil.convertObjToStr(objLoanProductInterCalcTO.getMinIntDebit()));
        
        if (objLoanProductInterCalcTO.getSubsidy().equals(YES)) setRdoSubsidy_Yes(true);
        else setRdoSubsidy_No(true);
        
        setCboLoanPeriodMul((String) getCbmLoanPeriodMul().getDataForKey(CommonUtil.convertObjToStr(objLoanProductInterCalcTO.getLoanPeriodsMultiples())));
    }
    
    
    private void setLoanProductClassificationsTO(AgriLoanProductClassificationsTO objLoanProductClassificationsTO) throws Exception{
        log.info("In setLoanProductcClassificationTO...");
        setCboCommodityCode((String) getCbmCommodityCode().getDataForKey(CommonUtil.convertObjToStr(objLoanProductClassificationsTO.getCommodityCode())));
        setCboTypeFacility((String) getCbmTypeFacility().getDataForKey(CommonUtil.convertObjToStr(objLoanProductClassificationsTO.getFacilityType())));
        setCboPurposeCode((String) getCbmPurposeCode().getDataForKey(CommonUtil.convertObjToStr(objLoanProductClassificationsTO.getPurposeCode())));
        setCboIndusCode((String) getCbmIndusCode().getDataForKey(CommonUtil.convertObjToStr(objLoanProductClassificationsTO.getIndustryCode())));
        setCbo20Code((String) getCbm20Code().getDataForKey(CommonUtil.convertObjToStr(objLoanProductClassificationsTO.getTwentyCode())));
        setCboGovtSchemeCode((String) getCbmGovtSchemeCode().getDataForKey(CommonUtil.convertObjToStr(objLoanProductClassificationsTO.getGovtSchemeCode())));
        if (CommonUtil.convertObjToStr(objLoanProductClassificationsTO.getEcgc()).equals(YES)){
            setChkECGC(true);
        }else {
            setChkECGC(false);
        }
        
        setCboGuaranteeCoverCode((String) getCbmGuaranteeCoverCode().getDataForKey(CommonUtil.convertObjToStr(objLoanProductClassificationsTO.getGuaranteeCoverCode())));
        setCboHealthCode((String) getCbmHealthCode().getDataForKey(CommonUtil.convertObjToStr(objLoanProductClassificationsTO.getHealthCode())));
        setCboSectorCode((String) getCbmSectorCode().getDataForKey(CommonUtil.convertObjToStr(objLoanProductClassificationsTO.getSectorCode())));
        setCboRefinancingInsti((String) getCbmRefinancingInsti().getDataForKey(CommonUtil.convertObjToStr(objLoanProductClassificationsTO.getRefinancingInstitution())));
        if (CommonUtil.convertObjToStr(objLoanProductClassificationsTO.getDirectFinance()).equals(YES)){
            setChkDirectFinance(true);
        }else {
            setChkDirectFinance(false);
        }
        if (CommonUtil.convertObjToStr(objLoanProductClassificationsTO.getQis()).equals(YES)){
            setChkQIS(true);
        }else {
            setChkQIS(false);
        }
        setCboSecurityDeails((String) getCbmSecurityDeails().getDataForKey(CommonUtil.convertObjToStr(objLoanProductClassificationsTO.getSecurityDetails())));
    }
    private HashMap setLoanInsuranceDetails(){
        AgriLoanProductInsuranceTO  agriLoanProductInsuranceTO=new AgriLoanProductInsuranceTO();
        HashMap singleMap=new HashMap();
        HashMap finalMap=new HashMap();
        HashMap deleteMap=new HashMap();
        ArrayList removedValues=new ArrayList();
        if(allInsuranceMap !=null){
        Set set =allInsuranceMap.keySet();
        Object objkeySet[]=(Object[])set.toArray();
        for(int i=0;i<allInsuranceMap.size();i++){
            singleMap=(HashMap)allInsuranceMap.get(objkeySet[i]);
            agriLoanProductInsuranceTO=new AgriLoanProductInsuranceTO();
            agriLoanProductInsuranceTO.setSlno(CommonUtil.convertObjToStr(singleMap.get("SLNO")));
            agriLoanProductInsuranceTO.setInsuranceType(CommonUtil.convertObjToStr(singleMap.get("INSURANCE TYPE")));
            agriLoanProductInsuranceTO.setInsuranceUnderScheme(CommonUtil.convertObjToStr(singleMap.get("INSURANCE UNDER SCHEME")));
            agriLoanProductInsuranceTO.setBankSharePremium(CommonUtil.convertObjToStr(singleMap.get("BANK SHARE")));
            agriLoanProductInsuranceTO.setCustomerSharePremium(CommonUtil.convertObjToStr(singleMap.get("CUSTOMER SHARE")));
            agriLoanProductInsuranceTO.setProdId(CommonUtil.convertObjToStr(getTxtProductID()));//singleMap.get("INSURANCE TYPE")));
            agriLoanProductInsuranceTO.setInsuranceAmt(CommonUtil.convertObjToStr(singleMap.get("INSURANCE AMT")));
            if(singleMap.get("COMMAND").equals(CommonConstants.TOSTATUS_INSERT)) 
                agriLoanProductInsuranceTO.setStatus(CommonConstants.STATUS_CREATED);
            else
                agriLoanProductInsuranceTO.setStatus(CommonConstants.STATUS_MODIFIED);
            agriLoanProductInsuranceTO.setCommand(CommonUtil.convertObjToStr(singleMap.get("COMMAND")));
            finalMap.put(agriLoanProductInsuranceTO.getSlno(),agriLoanProductInsuranceTO);
        }
        }
        removedValues=insuranceModel.getRemovedValues();
        for(int i=0;i<removedValues.size();i++){
            deleteMap=(HashMap)removedValues.get(i);
            agriLoanProductInsuranceTO=new AgriLoanProductInsuranceTO();
            agriLoanProductInsuranceTO.setSlno(CommonUtil.convertObjToStr(deleteMap.get("SLNO")));
            agriLoanProductInsuranceTO.setInsuranceType(CommonUtil.convertObjToStr(deleteMap.get("INSURANCE TYPE")));
            agriLoanProductInsuranceTO.setInsuranceUnderScheme(CommonUtil.convertObjToStr(deleteMap.get("INSURANCE UNDER SCHEME")));
            agriLoanProductInsuranceTO.setBankSharePremium(CommonUtil.convertObjToStr(deleteMap.get("BANK SHARE")));
            agriLoanProductInsuranceTO.setCustomerSharePremium(CommonUtil.convertObjToStr(deleteMap.get("CUSTOMER SHARE")));
            agriLoanProductInsuranceTO.setInsuranceAmt(CommonUtil.convertObjToStr(deleteMap.get("INSURANCE AMT")));
            agriLoanProductInsuranceTO.setProdId(CommonUtil.convertObjToStr(getTxtProductID()));
            agriLoanProductInsuranceTO.setStatus(CommonConstants.STATUS_DELETED);
            agriLoanProductInsuranceTO.setCommand(CommonConstants.TOSTATUS_DELETE);
            finalMap.put(agriLoanProductInsuranceTO.getSlno(),agriLoanProductInsuranceTO);
        }
        return finalMap;
    }
    // Method to calculate is its Years, Months or Days
    //depending on the numerical value from DataBase...
    // to be displayed in UI
    private String setPeriod(int rV) throws Exception{
        String periodValue;
        if ((rV >= year) && (rV%year == 0)) {
            periodValue = YEAR1;
            rV = rV/year;
        } else if ((rV >= month) && (rV % month == 0)) {
            periodValue=MONTH1;
            rV = rV/month;
        } else if ((rV >= day) && (rV % day == 0)) {
            periodValue=DAY1;
            rV = rV;
        } else {
            periodValue="";
            rV = 0;
        }
        resultValue = rV;
        return periodValue;
    }

    
    private void resetPeriod(){
        resultValue = 0;
    }
    // to find the value of multiplier on the basis of period...
    public double setCombo(String duration) throws Exception{
        periodData=0;
        resultData=0;
        int period=0;
        if(!duration.equalsIgnoreCase("")){
            if( duration.equals(DAY))
                period = day;
            else if(duration.equals(MONTH))
                period = month;
            else if(duration.equals(YEAR))
                period = year;
        }
        
        duration = "";
        return period;
    }
    //========================================================================================
    /* To inser the data into the database from the UI*/
    
    // Set and Get methods for setting and getting Action Type like New, Edit, Delete...
    public int getActionType(){
        log.info("In getActionType...");
        
        return actionType;
    }
    
    /**
     * @param actionType Form action Type
     */
    public void setActionType(int actionType) {
        log.info("In setActionType...");
        
        this.actionType = actionType;
        setChanged();
    }
    
    public void setResult(int result) {
        log.info("In setResult...");
        
        this.result = result;
        setChanged();
    }
    public int getResult(){
        log.info("In getResult...");
        return this.result;
    }
    
    //----------Account----------
    /* To set common data in the Transfer Object*/
    public AgriLoanProductAccountTO setLoanProductAccount() {
        log.info("In setLoanProductAccount...");
        
        final AgriLoanProductAccountTO objLoanProductAccountTO = new AgriLoanProductAccountTO();
        try{
            objLoanProductAccountTO.setProdId(CommonUtil.convertObjToStr(txtProductID));
            objLoanProductAccountTO.setProdDesc(CommonUtil.convertObjToStr(txtProductDesc));
            objLoanProductAccountTO.setAcctHead(CommonUtil.convertObjToStr(txtAccHead));
            objLoanProductAccountTO.setBehavesLike(CommonUtil.convertObjToStr((String)cbmOperatesLike.getKeyForSelected()));
            objLoanProductAccountTO.setBaseCurrency(LocaleConstants.DEFAULT_CURRENCY);
            
            /** To set The Created By and/or Status By...
             * Depending on the Type of Task to be performed...
             */
            if(getActionType() ==  ClientConstants.ACTIONTYPE_NEW){
                objLoanProductAccountTO.setCreatedBy(TrueTransactMain.USER_ID);
            }else if((getActionType() ==  ClientConstants.ACTIONTYPE_DELETE)
            || (getActionType() ==  ClientConstants.ACTIONTYPE_EDIT)){
                objLoanProductAccountTO.setStatusBy(TrueTransactMain.USER_ID);
            }
        }catch(Exception e){
            log.info("Error in setLoanProductAccount()");
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objLoanProductAccountTO;
    }
    /**
     * To set the data in LoanProductDocuments TO
     */
    public AgriLoanProductDocumentsTO setLoanProductDocumentsTO() {
        log.info("In setLoanProductDocumentsTO...");
        
        final AgriLoanProductDocumentsTO objLoanProductDocumentsTO = new AgriLoanProductDocumentsTO();
        try{
            objLoanProductDocumentsTO.setProdId(CommonUtil.convertObjToStr(getTxtProductID()));
            objLoanProductDocumentsTO.setSINo(CommonUtil.convertObjToStr(getDocumentSerialNo()));
            objLoanProductDocumentsTO.setDocDesc(CommonUtil.convertObjToStr(getTxtDocumentDesc()));
            objLoanProductDocumentsTO.setDocNo(CommonUtil.convertObjToStr(getTxtDocumentNo()));
            objLoanProductDocumentsTO.setDocType(CommonUtil.convertObjToStr((String)cbmDocumentType.getKeyForSelected()));
            objLoanProductDocumentsTO.setStatus(CommonUtil.convertObjToStr(getDocumentStatus()));
            
        }catch(Exception e){
            log.info("Error in setLoanProductDocumentsTO()");
            parseException.logException(e,true);
        }
        return objLoanProductDocumentsTO;
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    /**
     * Set Documents fields in the OB
     */
    private void setLoanProductDocumentsOB(AgriLoanProductDocumentsTO objLoanProductDocumentsTO) throws Exception {
        log.info("Inside setLoanProductDocumentsOB()");
        setTxtProductID(CommonUtil.convertObjToStr(objLoanProductDocumentsTO.getProdId()));
        setTxtDocumentDesc(CommonUtil.convertObjToStr(objLoanProductDocumentsTO.getDocDesc()));
        setTxtDocumentNo(CommonUtil.convertObjToStr(objLoanProductDocumentsTO.getDocNo()));
        setDocumentSerialNo(CommonUtil.convertObjToStr(objLoanProductDocumentsTO.getSINo()));
        setDocumentStatus(CommonUtil.convertObjToStr(objLoanProductDocumentsTO.getStatus()));
        setCboDocumentType((String) getCbmDocumentType().getDataForKey(CommonUtil.convertObjToStr(objLoanProductDocumentsTO.getDocType())));
        ttNotifyObservers();
    }
    public AgriLoanProductAccountParamTO setLoanProductAccountParam() {
        log.info("In setLoanProductAccountParam...");
        
        final AgriLoanProductAccountParamTO objLoanProductAccountParamTO = new AgriLoanProductAccountParamTO();
        try{
            objLoanProductAccountParamTO.setProdId(CommonUtil.convertObjToStr(txtProductID));
            objLoanProductAccountParamTO.setNumberPattern(CommonUtil.convertObjToStr(txtNumPatternFollowed));
            objLoanProductAccountParamTO.setNumberPatternSuffix(CommonUtil.convertObjToStr(txtNumPatternFollowedSuffix));
            //            if(getTxtNumPatternFollowedSuffix().equals("0"))
            //               objLoanProductAccountParamTO.setLastAcNo("0") ;
            //            else{
            //                int s= CommonUtil.convertObjToInt(getTxtNumPatternFollowedSuffix());
            //                s=s-1;
            //                objLoanProductAccountParamTO.setLastAcNo(String.valueOf(s));
            //            }
            objLoanProductAccountParamTO.setLastAcNo(getTxtLastAccNum());
            if (getRdoIsLimitDefnAllowed_Yes() == true) {
                objLoanProductAccountParamTO.setLimitDefAllowed(YES);
            } else /*if (getRdoIsLimitDefnAllowed_No() == true)*/ {
                objLoanProductAccountParamTO.setLimitDefAllowed(NO);
            }
            
            if (getRdoIsStaffAccOpened_Yes() == true) {
                objLoanProductAccountParamTO.setStaffAcOpened(YES);
            } else /*if (getRdoIsStaffAccOpened_No() == true)*/ {
                objLoanProductAccountParamTO.setStaffAcOpened(NO);
            }
            
            if (getRdoIsDebitInterUnderClearing_Yes() == true) {
                objLoanProductAccountParamTO.setDebitIntClearingappl(YES);
            } else /*if (getRdoIsDebitInterUnderClearing_No() == true)*/ {
                objLoanProductAccountParamTO.setDebitIntClearingappl(NO);
            }
            
        } catch(Exception e){
            log.info("Error in setLoanProductAccountParam()");
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objLoanProductAccountParamTO;
    }
    
    //----------INTEREST RECEIVABLE----------
    public AgriLoanProductInterReceivableTO setLoanProductInterReceivable() {
        log.info("In setLoanProductInterReceivable...");
        final AgriLoanProductInterReceivableTO objLoanProductInterReceivableTO = new AgriLoanProductInterReceivableTO();
        try{
            objLoanProductInterReceivableTO.setProdId(txtProductID);
            
            if (getRdoldebitInterCharged_Yes() == true) {
                objLoanProductInterReceivableTO.setDebitIntCharged(YES);
            } else /*if (getRdoldebitInterCharged_No() == true)*/ {
                objLoanProductInterReceivableTO.setDebitIntCharged(NO);
            }
            objLoanProductInterReceivableTO.setMinDebitintRate(CommonUtil.convertObjToDouble(txtMinDebitInterRate));
            objLoanProductInterReceivableTO.setMaxDebitintRate(CommonUtil.convertObjToDouble(txtMaxDebitInterRate));
            objLoanProductInterReceivableTO.setMinDebitintAmt(CommonUtil.convertObjToDouble(txtMinDebitInterAmt));
            objLoanProductInterReceivableTO.setMaxDebitintAmt(CommonUtil.convertObjToDouble(txtMaxDebitInterAmt));
            objLoanProductInterReceivableTO.setDebitintCalcFreq(CommonUtil.convertObjToDouble(cbmDebInterCalcFreq.getKeyForSelected()));
            objLoanProductInterReceivableTO.setDebitintApplFreq(CommonUtil.convertObjToDouble(cbmDebitInterAppFreq.getKeyForSelected()));
            objLoanProductInterReceivableTO.setDebitintCompFreq(CommonUtil.convertObjToDouble(cbmDebitInterComFreq.getKeyForSelected()));
            objLoanProductInterReceivableTO.setDebitProdRoundoff((String)cbmDebitProdRoundOff.getKeyForSelected());
            objLoanProductInterReceivableTO.setDebitIntRoundoff((String)cbmDebitInterRoundOff.getKeyForSelected());
            objLoanProductInterReceivableTO.setPenalExceptionAmt(CommonUtil.convertObjToDouble(txtPenalApplicableAmt));
            Date LstIntDt = DateUtil.getDateMMDDYYYY(tdtLastInterCalcDate);
            if(LstIntDt != null){
                Date lstintDate = (Date)curDate.clone();
                lstintDate.setDate(LstIntDt.getDate());
                lstintDate.setMonth(LstIntDt.getMonth());
                lstintDate.setYear(LstIntDt.getYear());
                //            objLoanProductInterReceivableTO.setLastIntcalcDtdebit(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtLastInterCalcDate)));.
                objLoanProductInterReceivableTO.setLastIntcalcDtdebit(lstintDate);
            }else{
                objLoanProductInterReceivableTO.setLastIntcalcDtdebit(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtLastInterCalcDate)));
            }
            
            Date LstAppDt = DateUtil.getDateMMDDYYYY(tdtLastInterAppDate);
            if(LstAppDt != null){
                Date lsAppDate = (Date)curDate.clone();
                lsAppDate.setDate(LstAppDt.getDate());
                lsAppDate.setMonth(LstAppDt.getMonth());
                lsAppDate.setYear(LstAppDt.getYear());
                //            objLoanProductInterReceivableTO.setLastIntapplDtdebit(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtLastInterAppDate)));
                objLoanProductInterReceivableTO.setLastIntapplDtdebit(lsAppDate);
            }else{
                objLoanProductInterReceivableTO.setLastIntapplDtdebit(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtLastInterAppDate)));
            }
            
            objLoanProductInterReceivableTO.setProdFreq(CommonUtil.convertObjToDouble(cbmProductFreq.getKeyForSelected()));
            
            if (getRdoPLRRateAppl_Yes() == true) {
                objLoanProductInterReceivableTO.setPlrRateAppl(YES);
            } else /*if (getRdoPLRRateAppl_No() == true)*/ {
                objLoanProductInterReceivableTO.setPlrRateAppl(NO);
            }
            
            objLoanProductInterReceivableTO.setPlrRate(CommonUtil.convertObjToDouble(txtPLRRate));
            
            Date PlrDt = DateUtil.getDateMMDDYYYY(tdtPLRAppForm);
            if(PlrDt != null){
                Date plrDate = (Date)curDate.clone();
                plrDate.setDate(PlrDt.getDate());
                plrDate.setMonth(PlrDt.getMonth());
                plrDate.setYear(PlrDt.getYear());
                //            objLoanProductInterReceivableTO.setPlrApplFrom(DateUtil.getDateMMDDYYYY(tdtPLRAppForm));
                objLoanProductInterReceivableTO.setPlrApplFrom(plrDate);
            }else{
                objLoanProductInterReceivableTO.setPlrApplFrom(DateUtil.getDateMMDDYYYY(tdtPLRAppForm));
            }
            
            if (getRdoPLRApplNewAcc_Yes() == true) {
                objLoanProductInterReceivableTO.setPlrApplNewac(YES);
            } else /*if (getRdoPLRApplNewAcc_No() == true)*/ {
                objLoanProductInterReceivableTO.setPlrApplNewac(NO);
            }
            
            if (getRdoPLRApplExistingAcc_Yes() == true) {
                objLoanProductInterReceivableTO.setPlrApplExistac(YES);
            } else /*if (getRdoPLRApplExistingAcc_No() == true)*/ {
                objLoanProductInterReceivableTO.setPlrApplExistac(NO);
            }
            
            Date AccSanDt = DateUtil.getDateMMDDYYYY(tdtPlrApplAccSancForm);
            if(AccSanDt != null){
                Date accsanDate = (Date)curDate.clone();
                accsanDate.setDate(AccSanDt.getDate());
                accsanDate.setMonth(AccSanDt.getMonth());
                accsanDate.setYear(AccSanDt.getYear());
                //            objLoanProductInterReceivableTO.setPlrApplSancfrom(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtPlrApplAccSancForm)));
                objLoanProductInterReceivableTO.setPlrApplSancfrom(accsanDate);
            }else{
                objLoanProductInterReceivableTO.setPlrApplSancfrom(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtPlrApplAccSancForm)));
            }
            
            if (getRdoPenalAppl_Yes() == true) {
                objLoanProductInterReceivableTO.setPenalAppl(YES);
            } else /*if (getRdoPenalAppl_No() == true)*/ {
                objLoanProductInterReceivableTO.setPenalAppl(NO);
            }
            if (getRdoLimitExpiryInter_Yes() == true) {
                objLoanProductInterReceivableTO.setLimitExpiryInt(YES);
            } else /*if (getRdoLimitExpiryInter_No() == true)*/ {
                objLoanProductInterReceivableTO.setLimitExpiryInt(NO);
            }
            if (isRdoasAndWhenCustomer_Yes() == true) {
                objLoanProductInterReceivableTO.setAsAndWhenCustomer_Yes(YES);
            } else /*if (getRdoLimitExpiryInter_No() == true)*/ {
                objLoanProductInterReceivableTO.setAsAndWhenCustomer_Yes(NO);
            }
            if (isRdocalendarFrequency_Yes() == true) {
                objLoanProductInterReceivableTO.setCalendarFrequency_Yes(YES);
            } else /*if (getRdoLimitExpiryInter_No() == true)*/ {
                objLoanProductInterReceivableTO.setCalendarFrequency_Yes(NO);
            }
            if (isChkprincipalDue() == true) {
                objLoanProductInterReceivableTO.setPrincipalDue(YES);
            } else /*if (getRdoLimitExpiryInter_No() == true)*/ {
                objLoanProductInterReceivableTO.setPrincipalDue(NO);
                
            }
            if (isChkInterestDue() == true) {
                objLoanProductInterReceivableTO.setInterestDue(YES);
            } else /*if (getRdoLimitExpiryInter_No() == true)*/ {
                objLoanProductInterReceivableTO.setInterestDue(NO);
                
            }
            objLoanProductInterReceivableTO.setPenalIntRate(CommonUtil.convertObjToDouble(txtPenalInterRate));
            objLoanProductInterReceivableTO.setExpoLmtPrudentialamt(CommonUtil.convertObjToDouble(txtExposureLimit_Prud));
            objLoanProductInterReceivableTO.setExpoLmtPolicyamt(CommonUtil.convertObjToDouble(txtExposureLimit_Policy));
            objLoanProductInterReceivableTO.setExpoLmtPrudentialper(CommonUtil.convertObjToDouble(txtExposureLimit_Prud2));
            objLoanProductInterReceivableTO.setExpoLmtPolicyper(CommonUtil.convertObjToDouble(txtExposureLimit_Policy2));
        }catch(NumberFormatException ne){
            //log.info("NumberFormatException in what u expect");
        }catch(Exception e){
            log.info("Error in setLoanProductInterReceivable()");
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objLoanProductInterReceivableTO;
    }
    
    //----------CHARGES----------
    /* To set common data in the Transfer Object*/
    public AgriLoanProductChargesTO setLoanProductCharges() {
        log.info("In setLoanProductCharges...");
        
        final AgriLoanProductChargesTO objLoanProductChargesTO = new AgriLoanProductChargesTO();
        try{
            objLoanProductChargesTO.setProdId(txtProductID);
            objLoanProductChargesTO.setAcClosingChrg(CommonUtil.convertObjToDouble(txtAccClosingCharges));
            objLoanProductChargesTO.setMiscServChrg(CommonUtil.convertObjToDouble(txtMiscSerCharges));
            
            if (getRdoStatCharges_Yes() == true) {
                objLoanProductChargesTO.setStatChrg(CommonUtil.convertObjToStr(YES));
            } else /*if (getRdoStatCharges_No() == true)*/ {
                objLoanProductChargesTO.setStatChrg(CommonUtil.convertObjToStr(NO));
            }
            
            objLoanProductChargesTO.setStatChrgRate(CommonUtil.convertObjToDouble(txtStatChargesRate));
            
            if (getRdoFolioChargesAppl_Yes() == true) {
                objLoanProductChargesTO.setFolioChrgAppl(CommonUtil.convertObjToStr(YES));
            } else /*if (getRdoFolioChargesAppl_No() == true)*/ {
                objLoanProductChargesTO.setFolioChrgAppl(CommonUtil.convertObjToStr(NO));
            }
            
            Date LsFolDt = DateUtil.getDateMMDDYYYY(tdtLastFolioChargesAppl);
            if(LsFolDt != null){
                Date lsfolDate = (Date)curDate.clone();
                lsfolDate.setDate(LsFolDt.getDate());
                lsfolDate.setMonth(LsFolDt.getMonth());
                lsfolDate.setYear(LsFolDt.getYear());
                //            objLoanProductChargesTO.setLastFolioChrgon(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtLastFolioChargesAppl)));
                objLoanProductChargesTO.setLastFolioChrgon(lsfolDate);
            }else{
                objLoanProductChargesTO.setLastFolioChrgon(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtLastFolioChargesAppl)));
            }
            
            objLoanProductChargesTO.setNoEntriesPerFolio(CommonUtil.convertObjToDouble(txtNoEntriesPerFolio));
            
            Date NxFolDt = DateUtil.getDateMMDDYYYY(tdtNextFolioDDate);
            if(NxFolDt != null){
                Date nxfolDate = (Date)curDate.clone();
                nxfolDate.setDate(NxFolDt.getDate());
                nxfolDate.setMonth(NxFolDt.getMonth());
                nxfolDate.setYear(NxFolDt.getYear());
                //            objLoanProductChargesTO.setNextFolioDuedate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtNextFolioDDate)));
                objLoanProductChargesTO.setNextFolioDuedate(nxfolDate);
            }else{
                objLoanProductChargesTO.setNextFolioDuedate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtNextFolioDDate)));
            }
            
            objLoanProductChargesTO.setRatePerFolio(CommonUtil.convertObjToDouble(txtRatePerFolio));
            objLoanProductChargesTO.setFolioChrgApplfreq(CommonUtil.convertObjToDouble(cbmFolioChargesAppFreq.getKeyForSelected()));
            objLoanProductChargesTO.setToCollectFoliochrg(CommonUtil.convertObjToStr((String)cbmToCollectFolioCharges.getKeyForSelected()));
            
            if (getRdoToChargeOn_Man() == true) {
                objLoanProductChargesTO.setToCollectChrgOn(CommonUtil.convertObjToStr(MANUAL));
            }  else if (getRdoToChargeOn_Sys() == true) {
                objLoanProductChargesTO.setToCollectChrgOn(CommonUtil.convertObjToStr(SYSTEM));
            } else {
                objLoanProductChargesTO.setToCollectChrgOn(CommonUtil.convertObjToStr(BOTH));
            }
            objLoanProductChargesTO.setIncompFolioRoundoff(CommonUtil.convertObjToDouble(cbmIncompleteFolioRoundOffFreq.getKeyForSelected()));
            if(isRdoToChargeType_Credit()== true){
                objLoanProductChargesTO.setFolioChargeType("CREDIT");
            }else if(isRdoToChargeType_Debit()== true) {
                objLoanProductChargesTO.setFolioChargeType("DEBIT");
            }else{
                objLoanProductChargesTO.setFolioChargeType("BOTH");
            }
            if (getRdoProcessCharges_No() == true) {
                objLoanProductChargesTO.setProcChrg(CommonUtil.convertObjToStr(NO));
                
            } else /*if (getRdoProcessCharges_No() == true)*/ {
                objLoanProductChargesTO.setProcChrg(CommonUtil.convertObjToStr(YES));
            }
            
            if(getCboCharge_Limit2().equalsIgnoreCase(ABSOLUTE)){
                objLoanProductChargesTO.setProcChrgAmt(CommonUtil.convertObjToDouble(txtCharge_Limit2));
            }else if(getCboCharge_Limit2().equalsIgnoreCase(PERCENT)){
                objLoanProductChargesTO.setProcChrgPer(CommonUtil.convertObjToDouble(txtCharge_Limit2));
            }
            
            
            if (getRdoCommitmentCharge_No() == true) {
                objLoanProductChargesTO.setCommitChrg(CommonUtil.convertObjToStr(NO));
                
            } else /*if (getRdoCommitmentCharge_No() == true)*/ {
                objLoanProductChargesTO.setCommitChrg(CommonUtil.convertObjToStr(YES));
            }
            
            if(getCboCharge_Limit3().equalsIgnoreCase(ABSOLUTE)){
                objLoanProductChargesTO.setCommitChrgAmt(CommonUtil.convertObjToDouble(txtCharge_Limit3));
            }else if(getCboCharge_Limit3().equalsIgnoreCase(PERCENT)){
                objLoanProductChargesTO.setCommitChrgPer(CommonUtil.convertObjToDouble(txtCharge_Limit3));
            }
            
            
            
        }catch(NumberFormatException ne){
            //log.info("NumberFormatException in what u expect");
        }catch(Exception e){
            log.info("Error in setLoanProductCharges()");
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objLoanProductChargesTO;
    }
    
    public ArrayList setLoanProductChargesTab() {
        log.info("In setLoanProductChargesTab...");
        // Testing...
        //        ArrayList data = tblChargeTab.getDataArrayList(); now not need clearing charge so we have use as notic charge
        ArrayList data=tblNoticeCharge.getDataArrayList();
        ArrayList charges = new ArrayList();
        chargeTabRow = new ArrayList();
        final int dataSize = data.size();
        for (int i=0;i<dataSize;i++){
            try{
                objLoanProductChargesTabTO = new AgriLoanProductChargesTabTO();
                objLoanProductChargesTabTO.setProdId(CommonUtil.convertObjToStr(txtProductID));
                
                charges = (ArrayList)data.get(i);
                //                objLoanProductChargesTabTO.setChqReturnChrgFrom(CommonUtil.convertObjToDouble(charges.get(0)));
                //                objLoanProductChargesTabTO.setChqReturnChrgTo(CommonUtil.convertObjToDouble(charges.get(1)));
                //                objLoanProductChargesTabTO.setChqReturnChrgRate(CommonUtil.convertObjToDouble(charges.get(2)));
                
                
                objLoanProductChargesTabTO.setNoticeType(CommonUtil.convertObjToStr(charges.get(0)));
                objLoanProductChargesTabTO.setIssueAfter(CommonUtil.convertObjToStr(charges.get(1)));
                objLoanProductChargesTabTO.setNoticeChargeAmt(CommonUtil.convertObjToDouble(charges.get(2)));
                objLoanProductChargesTabTO.setPostageAmt(CommonUtil.convertObjToDouble(charges.get(2)));
                
                chargeTabRow.add(objLoanProductChargesTabTO);
            }catch(Exception e){
                log.info("Error in setLoanProductChargesTab()");
                parseException.logException(e,true);
                //e.printStackTrace();
            }
        }// End of for()...
        
        return chargeTabRow;
    }
    public int addInsuranceDetails(int row,boolean update){
        ArrayList tableList=(ArrayList)tblInsurance.getDataArrayList();
        tblInsurance.setDataArrayList(tableList,insurnaceTabTitle);
        HashMap resultMap=new HashMap();
        singleInsList=new ArrayList();
        singleInsMap=new HashMap();
        int option=-1;
        insertInsurance(tableList.size()+1);
        if(!update){
            resultMap=insuranceModel.insertTableValues(singleInsList,singleInsMap);
        }else{
            resultMap=insuranceModel.updateTableValues(singleInsList,singleInsMap,row);
            
        }
        allInsuranceList=(ArrayList)resultMap.get("TABLE_VALUES");
        allInsuranceMap=(LinkedHashMap)resultMap.get("ALL_VALUES");
        tblInsurance.setDataArrayList(allInsuranceList,insurnaceTabTitle);
        option=CommonUtil.convertObjToInt(resultMap.get("OPTION"));
        
        return option;
    }
    
    private void insertInsurance(int slno){
        singleInsList.add(String.valueOf(slno));
        singleInsList.add(getCboInsuranceType());
        singleInsList.add(getCboInsuranceUnderScheme());
        singleInsList.add(getTxtBankSharePremium());
        singleInsList.add(getTxtCustomerSharePremium());
         singleInsList.add(getTxtInsuranceAmt());
        
        singleInsMap.put("SLNO",String.valueOf(slno));
        singleInsMap.put("INSURANCE TYPE",getCboInsuranceType());
        singleInsMap.put("INSURANCE UNDER SCHEME",getCboInsuranceUnderScheme());
        singleInsMap.put("BANK SHARE",getTxtBankSharePremium());
        singleInsMap.put("CUSTOMER SHARE",getTxtCustomerSharePremium());
        singleInsMap.put("INSURANCE AMT",getTxtInsuranceAmt());
        singleInsMap.put("COMMAND","");
        
        
    }
    //----------SPECIAL ITEMS----------
    public AgriLoanProductSpeItemsTO setLoanProductSpeItems() {
        log.info("In setLoanProductSpeItems...");
        
        final AgriLoanProductSpeItemsTO objLoanProductSpeItemsTO = new AgriLoanProductSpeItemsTO();
        try{
            objLoanProductSpeItemsTO.setProdId(txtProductID);
            
            if (getRdoATMcardIssued_Yes() == true) {
                objLoanProductSpeItemsTO.setAtmCardIssued(YES);
            } else /*if (getRdoATMcardIssued_No() == true)*/ {
                objLoanProductSpeItemsTO.setAtmCardIssued(NO);
            }
            if (getRdoCreditCardIssued_Yes() == true) {
                objLoanProductSpeItemsTO.setCrCardIssued(YES);
            } else /*if (getRdoCreditCardIssued_No() == true)*/ {
                objLoanProductSpeItemsTO.setCrCardIssued(NO);
            }
            if (getRdoMobileBanlingClient_Yes() == true) {
                objLoanProductSpeItemsTO.setMobileBankClient(YES);
            } else /*if (getRdoMobileBanlingClient_No() == true)*/ {
                objLoanProductSpeItemsTO.setMobileBankClient(NO);
            }
            if (getRdoIsAnyBranBankingAllowed_Yes() == true) {
                objLoanProductSpeItemsTO.setBranchBankingAllowed(YES);
            } else /*if (getRdoIsAnyBranBankingAllowed_No() == true)*/ {
                objLoanProductSpeItemsTO.setBranchBankingAllowed(NO);
            }
        }catch(Exception e){
            log.info("Error in setLoanProductSpeItems()");
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objLoanProductSpeItemsTO;
    }
    //----------ACCOUNT HEAD----------
    public AgriLoanProductAccHeadTO setLoanProductAccHead() {
        log.info("In setLoanProductAccHead...");
        
        final AgriLoanProductAccHeadTO objLoanProductAccHeadTO = new AgriLoanProductAccHeadTO();
        try{
            objLoanProductAccHeadTO.setProdId(txtProductID);
            objLoanProductAccHeadTO.setAcClosingChrg(txtAccClosCharges);
            objLoanProductAccHeadTO.setMiscServChrg(txtMiscServCharges);
            objLoanProductAccHeadTO.setStatChrg(txtStatementCharges);
            objLoanProductAccHeadTO.setAcDebitInt(txtAccDebitInter);
            objLoanProductAccHeadTO.setPenalInt(txtPenalInter);
            objLoanProductAccHeadTO.setAcCreditInt(txtAccCreditInter);
            objLoanProductAccHeadTO.setExpiryInt(txtExpiryInter);
            objLoanProductAccHeadTO.setChqRetChrgOutward(txtCheReturnChargest_Out);
            objLoanProductAccHeadTO.setChqRetChrgInward(txtCheReturnChargest_In);
            objLoanProductAccHeadTO.setFolioChrgAc(txtFolioChargesAcc);
            objLoanProductAccHeadTO.setCommitmentChrg(txtCommitCharges);
            objLoanProductAccHeadTO.setProcChrg(txtProcessingCharges);
            objLoanProductAccHeadTO.setPostageCharges(txtPostageCharges);
            objLoanProductAccHeadTO.setNoticeCharges(txtNoticeCharges);
            objLoanProductAccHeadTO.setIntPayableAcHd(txtIntPayableAccount);
            
            objLoanProductAccHeadTO.setLegalCharges(txtLegalCharges);
            //            objLoanProductAccHeadTO.setMiscCharges(txtMiscCharges);
            objLoanProductAccHeadTO.setArbitraryCharges(txtArbitraryCharges);
            objLoanProductAccHeadTO.setInsuranceCharges(txtInsuranceCharges);
            objLoanProductAccHeadTO.setExecutionDecreeCharges(txtExecutionDecreeCharges);
            objLoanProductAccHeadTO.setInsurancePremiumDebit(txtInsurancePremiumDebit);
        }catch(Exception e){
            log.info("Error in setLoanProductAccHead()");
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objLoanProductAccHeadTO;
    }
    //----------NON PERFORMING ASSETS----------
    public AgriLoanProductNonPerAssetsTO setLoanProductNonPerAssets() {
        log.info("In setLoanProductNonPerAssets...");
        
        final AgriLoanProductNonPerAssetsTO objLoanProductNonPerAssetsTO = new AgriLoanProductNonPerAssetsTO();
        try{
            objLoanProductNonPerAssetsTO.setProdId(txtProductID);
            
            if(cboMinPeriodsArrears.length() > 0){
                duration = ((String)cbmMinPeriodsArrears.getKeyForSelected());
                periodData = setCombo(duration);
                resultData = periodData * (Double.parseDouble(txtMinPeriodsArrears));
                //objLoanProductNonPerAssetsTO.setMinPeriodArrears( new Double(resultData));
                objLoanProductNonPerAssetsTO.setMinPeriodArrears(CommonUtil.convertObjToDouble(String.valueOf(resultData)));
            }
            if(cboPeriodTranSStanAssets.length() > 0){
                duration = ((String)cbmPeriodTranSStanAssets.getKeyForSelected());
            }
            periodData = setCombo(duration);
            //            resultData = periodData * (Double.parseDouble(CommonUtil.convertObjToStr(txtPeriodTranSStanAssets)));
            resultData = periodData * (CommonUtil.convertObjToDouble(txtPeriodTranSStanAssets).doubleValue());
            //objLoanProductNonPerAssetsTO.setPeriodTransSubstandard( new Double(resultData));
            objLoanProductNonPerAssetsTO.setPeriodTransSubstandard(CommonUtil.convertObjToDouble(String.valueOf(resultData)));
            objLoanProductNonPerAssetsTO.setProvisionSubstandard(CommonUtil.convertObjToDouble(txtProvisionsStanAssets));
            
            if(cboPeriodTransDoubtfulAssets1.length() > 0){
                duration = ((String)cbmPeriodTransDoubtfulAssets1.getKeyForSelected());
            }
            periodData = setCombo(duration);
            //            resultData = periodData * (Double.parseDouble(CommonUtil.convertObjToStr(txtPeriodTransDoubtfulAssets)));
            resultData = periodData * (CommonUtil.convertObjToDouble(txtPeriodTransDoubtfulAssets1).doubleValue());
            //objLoanProductNonPerAssetsTO.setPeriodTransDoubtful( new Double(resultData));
            objLoanProductNonPerAssetsTO.setPeriodTransDoubtful(CommonUtil.convertObjToDouble(String.valueOf(resultData)));
            objLoanProductNonPerAssetsTO.setProvisionDoubtful(CommonUtil.convertObjToDouble(txtProvisionDoubtfulAssets1));
            
            if(cboPeriodTransDoubtfulAssets2.length() > 0){
                duration = ((String)cbmPeriodTransDoubtfulAssets2.getKeyForSelected());
            }
            periodData = setCombo(duration);
            resultData = periodData * (CommonUtil.convertObjToDouble(txtPeriodTransDoubtfulAssets2).doubleValue());
            objLoanProductNonPerAssetsTO.setPeriodTransDoubtful2(CommonUtil.convertObjToDouble(String.valueOf(resultData)));
            objLoanProductNonPerAssetsTO.setProvisionDoubtful2(CommonUtil.convertObjToDouble(txtProvisionDoubtfulAssets2));
            
            if(cboPeriodTransDoubtfulAssets3.length() > 0){
                duration = ((String)cbmPeriodTransDoubtfulAssets3.getKeyForSelected());
            }
            periodData = setCombo(duration);
            resultData = periodData * (CommonUtil.convertObjToDouble(txtPeriodTransDoubtfulAssets3).doubleValue());
            objLoanProductNonPerAssetsTO.setPeriodTransDoubtful3(CommonUtil.convertObjToDouble(String.valueOf(resultData)));
            objLoanProductNonPerAssetsTO.setProvisionDoubtful3(CommonUtil.convertObjToDouble(txtProvisionDoubtfulAssets3));
            
            if(cboPeriodTransLossAssets.length() > 0){
                duration = ((String)cbmPeriodTransLossAssets.getKeyForSelected());
            }
            periodData = setCombo(duration);
            //            resultData = periodData * (Double.parseDouble(txtPeriodTransLossAssets));
            resultData = periodData * (CommonUtil.convertObjToDouble(txtPeriodTransLossAssets).doubleValue());
            //objLoanProductNonPerAssetsTO.setPeriodTransLoss( new Double(resultData));
            objLoanProductNonPerAssetsTO.setPeriodTransLoss(CommonUtil.convertObjToDouble(String.valueOf(resultData)));
            
            if(cboPeriodAfterWhichTransNPerformingAssets.length() > 0){
                duration = ((String)cbmPeriodAfterWhichTransNPerformingAssets.getKeyForSelected());
                periodData = setCombo(duration);
                //                resultData = periodData * (Double.parseDouble(txtPeriodAfterWhichTransNPerformingAssets));
                resultData = periodData * (CommonUtil.convertObjToDouble(txtPeriodAfterWhichTransNPerformingAssets).doubleValue());
                //objLoanProductNonPerAssetsTO.setPeriodTransNoperforming( new Double(resultData));
                objLoanProductNonPerAssetsTO.setPeriodTransNoperforming(CommonUtil.convertObjToDouble(String.valueOf(resultData)));
            }
            
            objLoanProductNonPerAssetsTO.setProvisionStdAssets(CommonUtil.convertObjToDouble(txtProvisionStandardAssetss));
            objLoanProductNonPerAssetsTO.setProvisionLoseAssets(CommonUtil.convertObjToDouble(txtProvisionLossAssets));
            
        }catch(Exception e){
            log.info("Error in setLoanProductNonPerAssets()");
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objLoanProductNonPerAssetsTO;
    }
    //----------INTEREST CALCULATION----------
    public AgriLoanProductInterCalcTO setLoanProductInterCalc() {
        log.info("In setLoanProductInterCalc...");
        
        final AgriLoanProductInterCalcTO objLoanProductInterCalcTO = new AgriLoanProductInterCalcTO();
        try{
            objLoanProductInterCalcTO.setProdId(CommonUtil.convertObjToStr(txtProductID));
            objLoanProductInterCalcTO.setCalcType(CommonUtil.convertObjToStr((String)cbmcalcType.getKeyForSelected()));
            
            if(cboMinPeriod.length() > 0){
                duration = ((String)cbmMinPeriod.getKeyForSelected());
            }
            periodData = setCombo(duration);
            //            resultData = periodData * (Double.parseDouble(txtMinPeriod));
            resultData = periodData * (CommonUtil.convertObjToDouble(txtMinPeriod).doubleValue());
            //objLoanProductInterCalcTO.setMinPeriod( new Double(resultData));
            objLoanProductInterCalcTO.setMinPeriod(CommonUtil.convertObjToDouble(String.valueOf(resultData)));
            
            if(cboMaxPeriod.length() > 0){
                duration = ((String)cbmMaxPeriod.getKeyForSelected());
            }
            periodData = setCombo(duration);
            //            resultData = periodData * (Double.parseDouble(txtMaxPeriod));
            resultData = periodData * (CommonUtil.convertObjToDouble(txtMaxPeriod).doubleValue());
            objLoanProductInterCalcTO.setMaxPeriod(CommonUtil.convertObjToDouble(String.valueOf(resultData)));
            //rewiew period
            duration="";
             if(cboReviewPeriod.length() > 0){
                duration = ((String)cbmReviewPeriod.getKeyForSelected());
            }
              periodData = setCombo(duration);
               resultData = periodData * (CommonUtil.convertObjToDouble(txtReviewPeriod).doubleValue());
            objLoanProductInterCalcTO.setReviewPeriod(CommonUtil.convertObjToDouble(String.valueOf(resultData)));
            objLoanProductInterCalcTO.setEligibleDepForLoanAmt(CommonUtil.convertObjToDouble(txtElgLoanAmt));
            objLoanProductInterCalcTO.setMinAmtLoan(CommonUtil.convertObjToDouble(txtMinAmtLoan));
            objLoanProductInterCalcTO.setMaxAmtLoan(CommonUtil.convertObjToDouble(txtMaxAmtLoan));
            objLoanProductInterCalcTO.setApplInterest(CommonUtil.convertObjToDouble(txtApplicableInter));
            objLoanProductInterCalcTO.setMinIntDebit(CommonUtil.convertObjToDouble(txtMinInterDebited));
            
            if (getRdoSubsidy_Yes() == true) {
                objLoanProductInterCalcTO.setSubsidy(YES);
            } else /*if (getRdoSubsidy_No() == true)*/ {
                objLoanProductInterCalcTO.setSubsidy(NO);
            }
            
            objLoanProductInterCalcTO.setLoanPeriodsMultiples(CommonUtil.convertObjToDouble((String)cbmLoanPeriodMul.getKeyForSelected()));
        }catch(Exception e){
            log.info("Error in setLoanProductInterCalc()");
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objLoanProductInterCalcTO;
    }
    
    
    public AgriLoanProductClassificationsTO setLoanProductClassifications() {
        log.info("In setLoanProductClassifications...");
        
        final AgriLoanProductClassificationsTO objLoanProductClassificationsTO = new AgriLoanProductClassificationsTO();
        try{
            objLoanProductClassificationsTO.setProdId(CommonUtil.convertObjToStr(txtProductID));
            objLoanProductClassificationsTO.setCommodityCode((String)cbmCommodityCode.getKeyForSelected());
            objLoanProductClassificationsTO.setFacilityType((String)cbmTypeFacility.getKeyForSelected());
            objLoanProductClassificationsTO.setPurposeCode((String)cbmPurposeCode.getKeyForSelected());
            objLoanProductClassificationsTO.setIndustryCode((String)cbmIndusCode.getKeyForSelected());
            objLoanProductClassificationsTO.setTwentyCode((String)cbm20Code.getKeyForSelected());
            objLoanProductClassificationsTO.setGovtSchemeCode((String)cbmGovtSchemeCode.getKeyForSelected());
            
            if (getChkECGC()){
                objLoanProductClassificationsTO.setEcgc(YES);
            }else{
                objLoanProductClassificationsTO.setEcgc(NO);
            }
            
            objLoanProductClassificationsTO.setGuaranteeCoverCode((String)cbmGuaranteeCoverCode.getKeyForSelected());
            objLoanProductClassificationsTO.setHealthCode((String)cbmHealthCode.getKeyForSelected());
            objLoanProductClassificationsTO.setSectorCode((String)cbmSectorCode.getKeyForSelected());
            objLoanProductClassificationsTO.setRefinancingInstitution((String)cbmRefinancingInsti.getKeyForSelected());
            
            if (getChkDirectFinance()){
                objLoanProductClassificationsTO.setDirectFinance(YES);
            }else{
                objLoanProductClassificationsTO.setDirectFinance(NO);
            }
            
            if (getChkQIS()){
                objLoanProductClassificationsTO.setQis(YES);
            }else{
                objLoanProductClassificationsTO.setQis(NO);
            }
            if (loanProductClassificationsTOList!=null)
                objLoanProductClassificationsTO.setStatus(CommonConstants.STATUS_MODIFIED);
            else
                objLoanProductClassificationsTO.setStatus(CommonConstants.STATUS_CREATED);
            
            //            objLoanProductClassificationsTO.setStatusBy (getTxtStatusBy());
            //            objLoanProductClassificationsTO.setStatusDt (DateUtil.getDateMMDDYYYY (getTxtStatusDt()));
            
            objLoanProductClassificationsTO.setSecurityDetails((String)cbmSecurityDeails.getKeyForSelected());
            
        }catch(Exception e){
            log.info("Error in LoanProductClassificationsTO()");
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objLoanProductClassificationsTO;
    }
    
    
    
    
    //=============================================================================
    //==========INSERT TABLE OF CHARGES...==========
    void setTblCharges(EnhancedTableModel tblChargeTab){
        log.info("In setTblCharges...");
        
        this.tblChargeTab = tblChargeTab;
        setChanged();
    }
    
    EnhancedTableModel getTblCharges(){
        return this.tblChargeTab;
    }
    
    //==========INSERT TABLE OF DOCUMENTS...==========
    void setTblDocuments(EnhancedTableModel tblDocuments){
        log.info("In setTblDocuments...");
        
        this.tblDocuments = tblDocuments;
        setChanged();
    }
    
    EnhancedTableModel getTblDocuments(){
        return this.tblDocuments;
    }
    
    public int addChargesTab(){
        log.info("In addChargesTab...");
        
        int option = -1;
        try{
            chargeTabRow = new ArrayList();
            chargeTabRow.add(txtRangeFrom);
            chargeTabRow.add(txtRangeTo);
            chargeTabRow.add(txtRateAmt);
            ArrayList data = tblChargeTab.getDataArrayList();
            tblChargeTab.setDataArrayList(data,chargeTabTitle);
            final int dataSize = data.size();
            boolean exist = false;
            ArrayList charges = new ArrayList();
            
            for (int i=0;i<dataSize;i++){
                // To check for the Amount in each row...
                // Whether it is already entered or not...
                charges = (ArrayList)data.get(i);
                if( (Double.parseDouble(txtRangeFrom) >= Double.parseDouble(CommonUtil.convertObjToStr(charges.get(0))))
                && (Double.parseDouble(txtRangeFrom) <= Double.parseDouble(CommonUtil.convertObjToStr(charges.get(1))))){
                    exist = true;
                }if(!exist){
                    if( (Double.parseDouble(txtRangeTo) >= Double.parseDouble(CommonUtil.convertObjToStr(charges.get(0))))
                    && (Double.parseDouble(txtRangeTo) <= Double.parseDouble(CommonUtil.convertObjToStr(charges.get(1))))){
                        exist = true;
                    }
                }if(!exist && (Double.parseDouble(txtRangeTo) < Double.parseDouble(CommonUtil.convertObjToStr(charges.get(1))))){
                    double difference = Double.parseDouble(txtRangeTo) - Double.parseDouble(txtRangeFrom) - Double.parseDouble(CommonUtil.convertObjToStr(charges.get(0)));
                    if(difference >=0){
                        exist = true;
                    }
                }
                
                if(exist){
                    String[] options = {objLoanProductRB.getString("cDialogYes"),objLoanProductRB.getString("cDialogNo"),objLoanProductRB.getString("cDialogCancel")};
                    option = COptionPane.showOptionDialog(null, objLoanProductRB.getString("existanceWarning"), CommonConstants.WARNINGTITLE,
                    COptionPane.YES_NO_CANCEL_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                    if (option == 0){
                        // option selected is Yes
                        updateChargesTab(i);
                        resetTab();
                    }else if( option == 1){
                        resetTab();
                    }
                    break;
                }
            }
            if (!exist){
                //The condition that the Entered Code is not in the table
                insertChargesTab();
            }
            if (option == 2){
                //The option selected is Cancel
            }
            setChanged();
            ttNotifyObservers();
            chargeTabRow = null;
        }catch(Exception e){
            log.info("The error in addChargesTab()");
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return option;
    }
    
    /* */
    
    // Insert for the Table in Charges...
    private void insertChargesTab() throws Exception{
        log.info("In insertChargesTab...");
        
        tblChargeTab.insertRow(0,chargeTabRow);
    }
    
    // Update for the Table in Charges
    private void updateChargesTab(int row) throws Exception{
        log.info("In updateChargesTab...");
        
        tblChargeTab.setValueAt(txtRateAmt, row, 2);
        setChanged();
        ttNotifyObservers();
    }
    
    // Delete for the Table in Charges...
    public void deleteChargesTab(){
        log.info("In deleteChargesTab...");
        
        try{
            ArrayList data = tblChargeTab.getDataArrayList();
            final int dataSize = data.size();
            for (int i=0;i<dataSize;i++){
                if ( (((ArrayList)data.get(i)).get(0)).equals(txtRangeFrom)
                && (((ArrayList)data.get(i)).get(1)).equals(txtRangeTo)){
                    tblChargeTab.removeRow(i);
                    break;
                }
            }
            setChanged();
            ttNotifyObservers();
        }catch(Exception e){
            log.info("The error in deleteChargesTab()");
            parseException.logException(e,true);
            //e.printStackTrace();
        }
    }
    
    // To get the Date from the Table into the Amount Combo box and Rate...
    public void populateChargesTab(int row){
        log.info("In populateChargesTab...");
        
        ArrayList charges = (ArrayList)tblChargeTab.getDataArrayList().get(row);
        setTxtRangeFrom((String)charges.get(0));
        setTxtRangeTo((String)charges.get(1));
        setTxtRateAmt((String)charges.get(2));
        setChanged();
        ttNotifyObservers();
    }
    public void populateNoticeCharge(int row){
        ArrayList NoticeCharge=(ArrayList)tblNoticeCharge.getDataArrayList().get(row);
        System.out.println("populateNoticeCharge####"+NoticeCharge);
        setCboNoticeType(CommonUtil.convertObjToStr(NoticeCharge.get(0)));
        setCboIssueAfter(CommonUtil.convertObjToStr(NoticeCharge.get(1)));
        setTxtNoticeChargeAmt(CommonUtil.convertObjToStr(NoticeCharge.get(2)));
        setTxtPostageChargeAmt(CommonUtil.convertObjToStr(NoticeCharge.get(3)));
        setChanged();
        ttNotifyObservers();
    }
    public void resetTab(){
        log.info("In resetTab...");
        
        setTxtRangeFrom("");
        setTxtRangeTo("");
        setTxtRateAmt("");
        
        setChanged();
        ttNotifyObservers();
    }
    /**
     * To reset the Document Fields
     */
    public void resetDocuments() {
        resetDocumentsTextFields();
        documentsTO = null;
        deletedDocumentsTO = null;
        mainDocumentsTO = null;
        entireRows = null;
        tblDocuments.setDataArrayList(null,documentsTabTitle);
        docSI_No = 1;// Seral No for Documents table
        deletedDocSI_No = 1;// Seral No for Deleted Documents
        
    }
    
    public void resetNoticeTypeCharges(){
        resetNoticeChargeValues();
        NoticeTypeTO=null;
        entireNoticeTypeRow=null;
        tblNoticeCharge.setDataArrayList(null,noticeChargeTabTitle);
        notice_Charge_No=1;
    }
    
    
    
    
    /**
     * To reset the Document Text Fields
     */
    public void resetDocumentsTextFields() {
        setTxtDocumentDesc("");
        setTxtDocumentNo("");
        setCboDocumentType("");
        setDocumentSerialNo("");
        setDocumentStatus("");
        ttNotifyObservers();
    }
    public void resetNoticeChargeValues(){
        setCboIssueAfter("");
        setCboNoticeType("");
        setTxtPostageChargeAmt("");
        setTxtNoticeChargeAmt("");
        ttNotifyObservers();
    }
    public void populateInsuranceDetails(int row){
        Set set= allInsuranceMap.keySet();
        Object keyset[]=(Object[])set.toArray();
        ArrayList singleList=(ArrayList)tblInsurance.getDataArrayList().get(row);
        for(int i=0;i<set.size();i++){
            if(((HashMap)allInsuranceMap.get(keyset[i])).get("SLNO").equals(singleList.get(0))){
                HashMap singleMap=(HashMap)allInsuranceMap.get(keyset[i]);
                setCboInsuranceType(CommonUtil.convertObjToStr(singleMap.get("INSURANCE TYPE")));
                setCboInsuranceUnderScheme(CommonUtil.convertObjToStr(singleMap.get("INSURANCE UNDER SCHEME")));
                setTxtBankSharePremium(CommonUtil.convertObjToStr(singleMap.get("BANK SHARE")));
                setTxtCustomerSharePremium(CommonUtil.convertObjToStr(singleMap.get("CUSTOMER SHARE")));
                setTxtInsuranceAmt(CommonUtil.convertObjToStr(singleMap.get("INSURANCE AMT")));
                break;
            }
        }
        setChanged();
        ttNotifyObservers();
    }
    public void deleteInsuranceDetails(int row){
        HashMap resultMap =insuranceModel.deleteTableValues(row);
        allInsuranceList=(ArrayList)resultMap.get("TABLE_VALUES");
        allInsuranceMap=(LinkedHashMap)resultMap.get("ALL_VALUES");
        tblInsurance.setDataArrayList(allInsuranceList, insurnaceTabTitle);
    }
    //-------------------------------------------------------------------------------------------
    /** To perform the appropriate operation */
    public void doAction() {
        log.info("In doAction...");
        
        try {
            //If actionType such as NEW, EDIT, DELETE, then proceed
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
                if( getCommand() != null ){
                    doActionPerform();
                }
            }
            else
                log.info("In doAction()-->actionType is null:" );
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            log.info("Error in doAction():");
            parseException.logException(e,true);
        }
    }
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        log.info("In doActionPerform...");
        
        final AgriLoanProductAccountTO objLoanProductAccountTO = setLoanProductAccount();
        final AgriLoanProductAccountParamTO objLoanProductAccountParamTO = setLoanProductAccountParam();
        final AgriLoanProductInterReceivableTO objLoanProductInterReceivableTO = setLoanProductInterReceivable();
        final AgriLoanProductChargesTO objLoanProductChargesTO = setLoanProductCharges();
        final ArrayList arrayLoanProductChargesTabTO = setLoanProductChargesTab();
        final AgriLoanProductSpeItemsTO objLoanProductSpeItemsTO = setLoanProductSpeItems();
        final AgriLoanProductAccHeadTO objLoanProductAccHeadTO = setLoanProductAccHead();
        final AgriLoanProductNonPerAssetsTO objLoanProductNonPerAssetsTO = setLoanProductNonPerAssets();
        final AgriLoanProductInterCalcTO objLoanProductInterCalcTO = setLoanProductInterCalc();
        final AgriLoanProductClassificationsTO objLoanProductClassifications = setLoanProductClassifications();
        final HashMap  objLoanInsuranceDetails = setLoanInsuranceDetails();
        
        
        objLoanProductAccountTO.setCommand(getCommand());
        // Documents
        if (mainDocumentsTO == null)
            mainDocumentsTO = new LinkedHashMap();
        if(getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            if (deletedDocumentsTO != null) {
                mainDocumentsTO.put(STATUS_WITH_DELETE, deletedDocumentsTO);
            }
        }
        mainDocumentsTO.put(STATUS_WITHOUT_DELETE, documentsTO);
        
        final HashMap data = new HashMap();
        data.put("LoanProductAccountTO",objLoanProductAccountTO);
        data.put("LoanProductAccountParamTO",objLoanProductAccountParamTO);
        data.put("LoanProductInterReceivableTO", objLoanProductInterReceivableTO);
        data.put("LoanProductChargesTO", objLoanProductChargesTO);
        data.put("LoanProductChargesTabTO", arrayLoanProductChargesTabTO);
        data.put("LoanProductSpeItemsTO",objLoanProductSpeItemsTO);
        data.put("LoanProductAccHeadTO",objLoanProductAccHeadTO);
        data.put("LoanProductNonPerAssetsTO",objLoanProductNonPerAssetsTO);
        data.put("LoanProductInterCalcTO",objLoanProductInterCalcTO);
        data.put("LoanProductDocumentsTO",mainDocumentsTO);
        data.put("AgriLoanProductInsuranceTO",objLoanInsuranceDetails);
        data.put("MODE",getCommand());// To Maintain the Status CREATED, MODIFIED, DELETED
        
        data.put("LoanProductClassificationsTO",objLoanProductClassifications);
        
        
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        int countRec=0;
        HashMap proxyResultMap=null;
        if(getActionType() == 3){
            HashMap hash=new HashMap();
            hash.put("PROD_ID",objLoanProductAccountTO.getProdId());
            
            List lst=ClientUtil.executeQuery("getSelectLoanProductCountRecord", hash);
            hash=(HashMap)lst.get(0);
            countRec=CommonUtil.convertObjToInt(hash.get("COUNTS"));
        }
        if(countRec !=0)
            ClientUtil.displayAlert("THIS PRODUCT HAVING ACCOUNT");
        else
            proxyResultMap = proxy.execute(data,operationMap);
        log.info("doActionPerform is over...");
        setResult(actionType);
        actionType = ClientConstants.ACTIONTYPE_CANCEL;
        //        resetForm();
    }
    
    private String getCommand() throws Exception{
        log.info("In getCommand...");
        
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
            default:
        }
        return command;
    }
    
    //To Reset all the fields in UI, Called from UI...
    public void resetForm(){
        //----------ACCOUNT----------
        setTxtProductID("");
        setTxtProductDesc("");
        setCboOperatesLike("");
        //        setCboProdCurrency("");
        setTxtAccHead("");
        
        setTxtNumPatternFollowed("");
        setTxtNumPatternFollowedSuffix("");
        setTxtLastAccNum("");
        setRdoIsLimitDefnAllowed_Yes(false);
        setRdoIsLimitDefnAllowed_No(false);
        setRdoIsStaffAccOpened_Yes(false);
        setRdoIsStaffAccOpened_No(false);
        setRdoIsDebitInterUnderClearing_Yes(false);
        setRdoIsDebitInterUnderClearing_No(false);
        //----------INTEREST RECEIVABLE----------
        setRdoldebitInterCharged_Yes(false);
        setRdoldebitInterCharged_No(false);
        setTxtMinDebitInterRate("");
        setTxtMaxDebitInterRate("");
        setTxtMinDebitInterAmt("");
        setTxtMaxDebitInterAmt("");
        setCboDebInterCalcFreq("");
        setCboDebitInterAppFreq("");
        setCboDebitInterComFreq("");
        setCboDebitProdRoundOff("");
        setCboDebitInterRoundOff("");
        setTdtLastInterCalcDate("");
        setTdtLastInterAppDate("");
        setRdoPLRRateAppl_Yes(false);
        setRdoPLRRateAppl_No(false);
        setTxtPLRRate("");
        setTdtPLRAppForm("");
        setRdoPLRApplNewAcc_Yes(false);
        setRdoPLRApplNewAcc_No(false);
        setRdoPLRApplExistingAcc_Yes(false);
        setRdoPLRApplExistingAcc_No(false);
        setTdtPlrApplAccSancForm("");
        setRdoPenalAppl_Yes(false);
        setRdoPenalAppl_No(false);
        setRdoLimitExpiryInter_Yes(false);
        setRdoLimitExpiryInter_No(false);
        setTxtPenalInterRate("");
        setTxtExposureLimit_Prud("");
        setTxtExposureLimit_Prud2("");
        setTxtExposureLimit_Policy("");
        setTxtExposureLimit_Policy2("");
        setCboProductFreq("");
        setRdoasAndWhenCustomer_No(false);
        setRdoasAndWhenCustomer_Yes(false);
        setRdocalendarFrequency_Yes(false);
        setRdocalendarFrequency_No(false);
        //----------CHARGES----------
        setTxtAccClosingCharges("");
        setTxtMiscSerCharges("");
        setRdoStatCharges_Yes(false);
        setRdoStatCharges_No(false);
        setTxtStatChargesRate("");
        setRdoFolioChargesAppl_Yes(false);
        setRdoFolioChargesAppl_No(false);
        setTdtLastFolioChargesAppl("");
        setTxtNoEntriesPerFolio("");
        setTdtNextFolioDDate("");
        setTxtRatePerFolio("");
        setCboFolioChargesAppFreq("");
        setCboToCollectFolioCharges("");
        setRdoToChargeOn_Man(false);
        setRdoToChargeOn_Sys(false);
        setRdoToChargeOn_Both(false);
        setCboIncompleteFolioRoundOffFreq("");
        setRdoProcessCharges_Yes(false);
        setRdoProcessCharges_No(false);
        setCboCharge_Limit2("");
        setTxtCharge_Limit2("");
        setRdoCommitmentCharge_Yes(false);
        setRdoCommitmentCharge_No(false);
        setCboCharge_Limit3("");
        setTxtCharge_Limit3("");
        
        setTxtRateAmt("");
        setTxtRangeFrom("");
        setTxtRangeTo("");
        //----------SPECIAL ITEMS----------
        setRdoATMcardIssued_Yes(false);
        setRdoATMcardIssued_No(false);
        setRdoCreditCardIssued_Yes(false);
        setRdoCreditCardIssued_No(false);
        setRdoMobileBanlingClient_Yes(false);
        setRdoMobileBanlingClient_No(false);
        setRdoIsAnyBranBankingAllowed_Yes(false);
        setRdoIsAnyBranBankingAllowed_No(false);
        //----------ACCOUNT HEAD----------
        setTxtAccClosCharges("");
        setTxtIntPayableAccount("");
        setTxtMiscServCharges("");
        setTxtStatementCharges("");
        setTxtAccDebitInter("");
        setTxtPenalInter("");
        setTxtAccCreditInter("");
        setTxtExpiryInter("");
        setTxtCheReturnChargest_Out("");
        setTxtCheReturnChargest_In("");
        setTxtFolioChargesAcc("");
        setTxtCommitCharges("");
        setTxtProcessingCharges("");
        setTxtLegalCharges("");
        //        setTxtMiscCharges("");
        setTxtArbitraryCharges("");
        setTxtInsuranceCharges("");
        setTxtExecutionDecreeCharges("");
        //----------NON PERFORMING ASSETS----------
        setTxtMinPeriodsArrears("");
        setTxtProvisionStandardAssetss("");
        setCboMinPeriodsArrears("");
        setTxtPeriodTranSStanAssets("");
        setCboPeriodTranSStanAssets("");
        setTxtProvisionsStanAssets("");
        setTxtPeriodTransDoubtfulAssets1("");
        setTxtPeriodTransDoubtfulAssets2("");
        setTxtPeriodTransDoubtfulAssets3("");
        setCboPeriodTransDoubtfulAssets1("");
        setCboPeriodTransDoubtfulAssets2("");
        setCboPeriodTransDoubtfulAssets3("");
        setTxtProvisionDoubtfulAssets1("");
        setTxtProvisionDoubtfulAssets2("");
        setTxtProvisionDoubtfulAssets3("");
        setTxtPeriodTransLossAssets("");
        setCboPeriodTransLossAssets("");
        setTxtProvisionLossAssets("");
        setTxtPeriodAfterWhichTransNPerformingAssets("");
        setCboPeriodAfterWhichTransNPerformingAssets("");
        //----------INTEREST CALCULATION----------
        setCbocalcType("");
        setTxtMinPeriod("");
        setCboMinPeriod("");
        setTxtMaxPeriod("");
        setCboMaxPeriod("");
        setTxtMinAmtLoan("");
        setTxtMaxAmtLoan("");
        setTxtApplicableInter("");
        setTxtMinInterDebited("");
        setRdoSubsidy_Yes(false);
        setRdoSubsidy_No(false);
        setCboLoanPeriodMul("");
        setCboReviewPeriod("");
        setTxtReviewPeriod("");
        setTxtElgLoanAmt("");
        
        //-------- DOCUMENTS ------------
        resetDocuments();
        //------------NOTICE TYPE CHARGES------
        resetNoticeTypeCharges();
        //-------- Classification -------
        setCboCommodityCode("");
        setCboTypeFacility("");
        setCboPurposeCode("");
        setCboIndusCode("");
        setCbo20Code("");
        setCboGovtSchemeCode("");
        setChkECGC(false);
        setCboGuaranteeCoverCode("");
        setCboHealthCode("");
        setCboSectorCode("");
        setCboRefinancingInsti("");
        setChkDirectFinance(false);
        setChkQIS(false);
        setCboSecurityDeails("");
        /* insurance details  */
        setCboInsuranceType("");
        setCboInsuranceUnderScheme("");
        setTxtBankSharePremium("");
        setTxtCustomerSharePremium("");
        allInsuranceList=null;
        allInsuranceMap=null;
//        insuranceModel=new TableUtil();
//        insuranceModel.setAttributeKey("SLNO");
        tblInsurance.setDataArrayList(null,insurnaceTabTitle);
        ttNotifyObservers();
    }
    
    //To reset the table in UI, again called from UI
    public void resetTable(){
        try{
            setTxtRangeFrom("");
            setTxtRangeTo("");
            setTxtRateAmt("");
            ArrayList data = tblChargeTab.getDataArrayList();
            for(int i=data.size(); i>0; i--)
                tblChargeTab.removeRow(i-1);
            setChanged();
            ttNotifyObservers();
        }catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            log.info("Error in resetTable():");
            parseException.logException(e,true);
        }
    }
    
    
    
    
    
    //set() and get() functions for all the fields in UI
    
    void setTxtProductID(String txtProductID){
        this.txtProductID = txtProductID;
        setChanged();
    }
    String getTxtProductID(){
        return this.txtProductID;
    }
    
    void setTxtProductDesc(String txtProductDesc){
        this.txtProductDesc = txtProductDesc;
        setChanged();
    }
    String getTxtProductDesc(){
        return this.txtProductDesc;
    }
    //--------------------------------------------
    
    void setCboOperatesLike(String cboOperatesLike){
        this.cboOperatesLike = cboOperatesLike;
        setChanged();
    }
    String getCboOperatesLike(){
        return this.cboOperatesLike;
    }
    public void setCbmOperatesLike(ComboBoxModel cbmOperatesLike){
        cbmOperatesLike = cbmOperatesLike;
        setChanged();
    }
    
    ComboBoxModel getCbmOperatesLike(){
        return cbmOperatesLike;
    }
    //--------------------------------------------
    
    void setTxtNumPatternFollowed(String txtNumPatternFollowed){
        this.txtNumPatternFollowed = txtNumPatternFollowed;
        setChanged();
    }
    String getTxtNumPatternFollowed(){
        return this.txtNumPatternFollowed;
    }
    
    void setTxtNumPatternFollowedSuffix(String txtNumPatternFollowedSuffix){
        this.txtNumPatternFollowedSuffix = txtNumPatternFollowedSuffix;
        setChanged();
    }
    String getTxtNumPatternFollowedSuffix(){
        return this.txtNumPatternFollowedSuffix;
    }
    
    void setTxtLastAccNum(String txtLastAccNum){
        this.txtLastAccNum = txtLastAccNum;
        setChanged();
    }
    String getTxtLastAccNum(){
        return this.txtLastAccNum;
    }
    
    void setRdoIsLimitDefnAllowed_Yes(boolean rdoIsLimitDefnAllowed_Yes){
        this.rdoIsLimitDefnAllowed_Yes = rdoIsLimitDefnAllowed_Yes;
        setChanged();
    }
    boolean getRdoIsLimitDefnAllowed_Yes(){
        return this.rdoIsLimitDefnAllowed_Yes;
    }
    
    void setRdoIsLimitDefnAllowed_No(boolean rdoIsLimitDefnAllowed_No){
        this.rdoIsLimitDefnAllowed_No = rdoIsLimitDefnAllowed_No;
        setChanged();
    }
    boolean getRdoIsLimitDefnAllowed_No(){
        return this.rdoIsLimitDefnAllowed_No;
    }
    
    void setRdoIsStaffAccOpened_Yes(boolean rdoIsStaffAccOpened_Yes){
        this.rdoIsStaffAccOpened_Yes = rdoIsStaffAccOpened_Yes;
        setChanged();
    }
    boolean getRdoIsStaffAccOpened_Yes(){
        return this.rdoIsStaffAccOpened_Yes;
    }
    
    void setRdoIsStaffAccOpened_No(boolean rdoIsStaffAccOpened_No){
        this.rdoIsStaffAccOpened_No = rdoIsStaffAccOpened_No;
        setChanged();
    }
    boolean getRdoIsStaffAccOpened_No(){
        return this.rdoIsStaffAccOpened_No;
    }
    
    void setRdoIsDebitInterUnderClearing_Yes(boolean rdoIsDebitInterUnderClearing_Yes){
        this.rdoIsDebitInterUnderClearing_Yes = rdoIsDebitInterUnderClearing_Yes;
        setChanged();
    }
    boolean getRdoIsDebitInterUnderClearing_Yes(){
        return this.rdoIsDebitInterUnderClearing_Yes;
    }
    
    void setRdoIsDebitInterUnderClearing_No(boolean rdoIsDebitInterUnderClearing_No){
        this.rdoIsDebitInterUnderClearing_No = rdoIsDebitInterUnderClearing_No;
        setChanged();
    }
    boolean getRdoIsDebitInterUnderClearing_No(){
        return this.rdoIsDebitInterUnderClearing_No;
    }
    
    void setTxtAccHead(String txtAccHead){
        this.txtAccHead = txtAccHead;
        setChanged();
    }
    String getTxtAccHead(){
        return this.txtAccHead;
    }
    
    void setRdoldebitInterCharged_Yes(boolean rdoldebitInterCharged_Yes){
        this.rdoldebitInterCharged_Yes = rdoldebitInterCharged_Yes;
        setChanged();
    }
    boolean getRdoldebitInterCharged_Yes(){
        return this.rdoldebitInterCharged_Yes;
    }
    
    void setRdoldebitInterCharged_No(boolean rdoldebitInterCharged_No){
        this.rdoldebitInterCharged_No = rdoldebitInterCharged_No;
        setChanged();
    }
    boolean getRdoldebitInterCharged_No(){
        return this.rdoldebitInterCharged_No;
    }
    
    void setTxtMinDebitInterRate(String txtMinDebitInterRate){
        this.txtMinDebitInterRate = txtMinDebitInterRate;
        setChanged();
    }
    String getTxtMinDebitInterRate(){
        return this.txtMinDebitInterRate;
    }
    
    void setTxtMaxDebitInterRate(String txtMaxDebitInterRate){
        this.txtMaxDebitInterRate = txtMaxDebitInterRate;
        setChanged();
    }
    String getTxtMaxDebitInterRate(){
        return this.txtMaxDebitInterRate;
    }
    
    void setTxtMinDebitInterAmt(String txtMinDebitInterAmt){
        this.txtMinDebitInterAmt = txtMinDebitInterAmt;
        setChanged();
    }
    String getTxtMinDebitInterAmt(){
        return this.txtMinDebitInterAmt;
    }
    
    void setTxtMaxDebitInterAmt(String txtMaxDebitInterAmt){
        this.txtMaxDebitInterAmt = txtMaxDebitInterAmt;
        setChanged();
    }
    String getTxtMaxDebitInterAmt(){
        return this.txtMaxDebitInterAmt;
    }
    //--------------------------------------------
    
    void setCboDebInterCalcFreq(String cboDebInterCalcFreq){
        this.cboDebInterCalcFreq = cboDebInterCalcFreq;
        setChanged();
    }
    String getCboDebInterCalcFreq(){
        return this.cboDebInterCalcFreq;
    }
    public void setCbmDebInterCalcFreq(ComboBoxModel cbmDebInterCalcFreq){
        cbmDebInterCalcFreq = cbmDebInterCalcFreq;
        setChanged();
    }
    
    ComboBoxModel getCbmDebInterCalcFreq(){
        return cbmDebInterCalcFreq;
    }
    //--------------------------------------------
    
    void setCboDebitInterAppFreq(String cboDebitInterAppFreq){
        this.cboDebitInterAppFreq = cboDebitInterAppFreq;
        setChanged();
    }
    String getCboDebitInterAppFreq(){
        return this.cboDebitInterAppFreq;
    }
    public void setCbmDebitInterAppFreq(ComboBoxModel cbmDebitInterAppFreq){
        cbmDebitInterAppFreq = cbmDebitInterAppFreq;
        setChanged();
    }
    
    ComboBoxModel getCbmDebitInterAppFreq(){
        return cbmDebitInterAppFreq;
    }
    //--------------------------------------------
    
    void setCboDebitInterComFreq(String cboDebitInterComFreq){
        this.cboDebitInterComFreq = cboDebitInterComFreq;
        setChanged();
    }
    String getCboDebitInterComFreq(){
        return this.cboDebitInterComFreq;
    }
    public void setCbmDebitInterComFreq(ComboBoxModel cbmDebitInterComFreq){
        cbmDebitInterComFreq = cbmDebitInterComFreq;
        setChanged();
    }
    
    ComboBoxModel getCbmDebitInterComFreq(){
        return cbmDebitInterComFreq;
    }
    //--------------------------------------------
    
    void setCboDebitProdRoundOff(String cboDebitProdRoundOff){
        this.cboDebitProdRoundOff = cboDebitProdRoundOff;
        setChanged();
    }
    String getCboDebitProdRoundOff(){
        return this.cboDebitProdRoundOff;
    }
    public void setCbmDebitProdRoundOff(ComboBoxModel cbmDebitProdRoundOff){
        cbmDebitProdRoundOff = cbmDebitProdRoundOff;
        setChanged();
    }
    
    ComboBoxModel getCbmDebitProdRoundOff(){
        return cbmDebitProdRoundOff;
    }
    //--------------------------------------------
    
    void setCboDebitInterRoundOff(String cboDebitInterRoundOff){
        this.cboDebitInterRoundOff = cboDebitInterRoundOff;
        setChanged();
    }
    String getCboDebitInterRoundOff(){
        return this.cboDebitInterRoundOff;
    }
    public void setCbmDebitInterRoundOff(ComboBoxModel cbmDebitInterRoundOff){
        cbmDebitInterRoundOff = cbmDebitInterRoundOff;
        setChanged();
    }
    
    ComboBoxModel getCbmDebitInterRoundOff(){
        return cbmDebitInterRoundOff;
    }
    //--------------------------------------------
    
    void setTdtLastInterCalcDate(String tdtLastInterCalcDate){
        this.tdtLastInterCalcDate = tdtLastInterCalcDate;
        setChanged();
    }
    String getTdtLastInterCalcDate(){
        return this.tdtLastInterCalcDate;
    }
    
    void setTdtLastInterAppDate(String tdtLastInterAppDate){
        this.tdtLastInterAppDate = tdtLastInterAppDate;
        setChanged();
    }
    String getTdtLastInterAppDate(){
        return this.tdtLastInterAppDate;
    }
    
    void setRdoPLRRateAppl_Yes(boolean rdoPLRRateAppl_Yes){
        this.rdoPLRRateAppl_Yes = rdoPLRRateAppl_Yes;
        setChanged();
    }
    boolean getRdoPLRRateAppl_Yes(){
        return this.rdoPLRRateAppl_Yes;
    }
    
    void setRdoPLRRateAppl_No(boolean rdoPLRRateAppl_No){
        this.rdoPLRRateAppl_No = rdoPLRRateAppl_No;
        setChanged();
    }
    boolean getRdoPLRRateAppl_No(){
        return this.rdoPLRRateAppl_No;
    }
    
    void setTxtPLRRate(String txtPLRRate){
        this.txtPLRRate = txtPLRRate;
        setChanged();
    }
    String getTxtPLRRate(){
        return this.txtPLRRate;
    }
    
    void setTdtPLRAppForm(String tdtPLRAppForm){
        this.tdtPLRAppForm = tdtPLRAppForm;
        setChanged();
    }
    String getTdtPLRAppForm(){
        return this.tdtPLRAppForm;
    }
    
    void setRdoPLRApplNewAcc_Yes(boolean rdoPLRApplNewAcc_Yes){
        this.rdoPLRApplNewAcc_Yes = rdoPLRApplNewAcc_Yes;
        setChanged();
    }
    boolean getRdoPLRApplNewAcc_Yes(){
        return this.rdoPLRApplNewAcc_Yes;
    }
    
    void setRdoPLRApplNewAcc_No(boolean rdoPLRApplNewAcc_No){
        this.rdoPLRApplNewAcc_No = rdoPLRApplNewAcc_No;
        setChanged();
    }
    boolean getRdoPLRApplNewAcc_No(){
        return this.rdoPLRApplNewAcc_No;
    }
    
    void setRdoPLRApplExistingAcc_Yes(boolean rdoPLRApplExistingAcc_Yes){
        this.rdoPLRApplExistingAcc_Yes = rdoPLRApplExistingAcc_Yes;
        setChanged();
    }
    boolean getRdoPLRApplExistingAcc_Yes(){
        return this.rdoPLRApplExistingAcc_Yes;
    }
    
    void setRdoPLRApplExistingAcc_No(boolean rdoPLRApplExistingAcc_No){
        this.rdoPLRApplExistingAcc_No = rdoPLRApplExistingAcc_No;
        setChanged();
    }
    boolean getRdoPLRApplExistingAcc_No(){
        return this.rdoPLRApplExistingAcc_No;
    }
    
    void setTdtPlrApplAccSancForm(String tdtPlrApplAccSancForm){
        this.tdtPlrApplAccSancForm = tdtPlrApplAccSancForm;
        setChanged();
    }
    String getTdtPlrApplAccSancForm(){
        return this.tdtPlrApplAccSancForm;
    }
    
    void setRdoPenalAppl_Yes(boolean rdoPenalAppl_Yes){
        this.rdoPenalAppl_Yes = rdoPenalAppl_Yes;
        setChanged();
    }
    boolean getRdoPenalAppl_Yes(){
        return this.rdoPenalAppl_Yes;
    }
    
    void setRdoPenalAppl_No(boolean rdoPenalAppl_No){
        this.rdoPenalAppl_No = rdoPenalAppl_No;
        setChanged();
    }
    boolean getRdoPenalAppl_No(){
        return this.rdoPenalAppl_No;
    }
    
    void setRdoLimitExpiryInter_Yes(boolean rdoLimitExpiryInter_Yes){
        this.rdoLimitExpiryInter_Yes = rdoLimitExpiryInter_Yes;
        setChanged();
    }
    boolean getRdoLimitExpiryInter_Yes(){
        return this.rdoLimitExpiryInter_Yes;
    }
    
    void setRdoLimitExpiryInter_No(boolean rdoLimitExpiryInter_No){
        this.rdoLimitExpiryInter_No = rdoLimitExpiryInter_No;
        setChanged();
    }
    boolean getRdoLimitExpiryInter_No(){
        return this.rdoLimitExpiryInter_No;
    }
    
    void setTxtPenalInterRate(String txtPenalInterRate){
        this.txtPenalInterRate = txtPenalInterRate;
        setChanged();
    }
    String getTxtPenalInterRate(){
        return this.txtPenalInterRate;
    }
    
    void setTxtExposureLimit_Prud(String txtExposureLimit_Prud){
        this.txtExposureLimit_Prud = txtExposureLimit_Prud;
        setChanged();
    }
    String getTxtExposureLimit_Prud(){
        return this.txtExposureLimit_Prud;
    }
    
    void setTxtExposureLimit_Prud2(String txtExposureLimit_Prud2){
        this.txtExposureLimit_Prud2 = txtExposureLimit_Prud2;
        setChanged();
    }
    String getTxtExposureLimit_Prud2(){
        return this.txtExposureLimit_Prud2;
    }
    
    void setTxtExposureLimit_Policy(String txtExposureLimit_Policy){
        this.txtExposureLimit_Policy = txtExposureLimit_Policy;
        setChanged();
    }
    String getTxtExposureLimit_Policy(){
        return this.txtExposureLimit_Policy;
    }
    
    void setTxtExposureLimit_Policy2(String txtExposureLimit_Policy2){
        this.txtExposureLimit_Policy2 = txtExposureLimit_Policy2;
        setChanged();
    }
    String getTxtExposureLimit_Policy2(){
        return this.txtExposureLimit_Policy2;
    }
    
    //--------------------------------------------
    
    void setCboProductFreq(String cboProductFreq){
        this.cboProductFreq = cboProductFreq;
        setChanged();
    }
    String getCboProductFreq(){
        return this.cboProductFreq;
    }
    public void setCbmProductFreq(ComboBoxModel cbmProductFreq){
        cbmProductFreq = cbmProductFreq;
        setChanged();
    }
    
    ComboBoxModel getCbmProductFreq(){
        return cbmProductFreq;
    }
    //--------------------------------------------
    
    void setTxtAccClosingCharges(String txtAccClosingCharges){
        this.txtAccClosingCharges = txtAccClosingCharges;
        setChanged();
    }
    String getTxtAccClosingCharges(){
        return this.txtAccClosingCharges;
    }
    
    void setTxtMiscSerCharges(String txtMiscSerCharges){
        this.txtMiscSerCharges = txtMiscSerCharges;
        setChanged();
    }
    String getTxtMiscSerCharges(){
        return this.txtMiscSerCharges;
    }
    
    void setRdoStatCharges_Yes(boolean rdoStatCharges_Yes){
        this.rdoStatCharges_Yes = rdoStatCharges_Yes;
        setChanged();
    }
    boolean getRdoStatCharges_Yes(){
        return this.rdoStatCharges_Yes;
    }
    
    void setRdoStatCharges_No(boolean rdoStatCharges_No){
        this.rdoStatCharges_No = rdoStatCharges_No;
        setChanged();
    }
    boolean getRdoStatCharges_No(){
        return this.rdoStatCharges_No;
    }
    
    void setTxtStatChargesRate(String txtStatChargesRate){
        this.txtStatChargesRate = txtStatChargesRate;
        setChanged();
    }
    String getTxtStatChargesRate(){
        return this.txtStatChargesRate;
    }
    //------------------------------------------------
    
    
    
    //------------------------------------------------
    
    void setTxtRateAmt(String txtRateAmt){
        this.txtRateAmt = txtRateAmt;
        setChanged();
    }
    String getTxtRateAmt(){
        return this.txtRateAmt;
    }
    
    void setRdoFolioChargesAppl_Yes(boolean rdoFolioChargesAppl_Yes){
        this.rdoFolioChargesAppl_Yes = rdoFolioChargesAppl_Yes;
        setChanged();
    }
    boolean getRdoFolioChargesAppl_Yes(){
        return this.rdoFolioChargesAppl_Yes;
    }
    
    void setRdoFolioChargesAppl_No(boolean rdoFolioChargesAppl_No){
        this.rdoFolioChargesAppl_No = rdoFolioChargesAppl_No;
        setChanged();
    }
    boolean getRdoFolioChargesAppl_No(){
        return this.rdoFolioChargesAppl_No;
    }
    
    void setTdtLastFolioChargesAppl(String tdtLastFolioChargesAppl){
        this.tdtLastFolioChargesAppl = tdtLastFolioChargesAppl;
        setChanged();
    }
    String getTdtLastFolioChargesAppl(){
        return this.tdtLastFolioChargesAppl;
    }
    
    void setTxtNoEntriesPerFolio(String txtNoEntriesPerFolio){
        this.txtNoEntriesPerFolio = txtNoEntriesPerFolio;
        setChanged();
    }
    String getTxtNoEntriesPerFolio(){
        return this.txtNoEntriesPerFolio;
    }
    
    void setTdtNextFolioDDate(String tdtNextFolioDDate){
        this.tdtNextFolioDDate = tdtNextFolioDDate;
        setChanged();
    }
    String getTdtNextFolioDDate(){
        return this.tdtNextFolioDDate;
    }
    
    void setTxtRatePerFolio(String txtRatePerFolio){
        this.txtRatePerFolio = txtRatePerFolio;
        setChanged();
    }
    String getTxtRatePerFolio(){
        return this.txtRatePerFolio;
    }
    //------------------------------------------------
    
    void setCboFolioChargesAppFreq(String cboFolioChargesAppFreq){
        this.cboFolioChargesAppFreq = cboFolioChargesAppFreq;
        setChanged();
    }
    String getCboFolioChargesAppFreq(){
        return this.cboFolioChargesAppFreq;
    }
    public void setCbmFolioChargesAppFreq(ComboBoxModel cbmFolioChargesAppFreq){
        cbmFolioChargesAppFreq = cbmFolioChargesAppFreq;
        setChanged();
    }
    
    ComboBoxModel getCbmFolioChargesAppFreq(){
        return cbmFolioChargesAppFreq;
    }
    //------------------------------------------------
    
    void setCboToCollectFolioCharges(String cboToCollectFolioCharges){
        this.cboToCollectFolioCharges = cboToCollectFolioCharges;
        setChanged();
    }
    String getCboToCollectFolioCharges(){
        return this.cboToCollectFolioCharges;
    }
    public void setCbmToCollectFolioCharges(ComboBoxModel cbmToCollectFolioCharges){
        cbmToCollectFolioCharges = cbmToCollectFolioCharges;
        setChanged();
    }
    
    ComboBoxModel getCbmToCollectFolioCharges(){
        return cbmToCollectFolioCharges;
    }
    //------------------------------------------------
    
    void setRdoToChargeOn_Man(boolean rdoToChargeOn_Man){
        this.rdoToChargeOn_Man = rdoToChargeOn_Man;
        setChanged();
    }
    boolean getRdoToChargeOn_Man(){
        return this.rdoToChargeOn_Man;
    }
    
    void setRdoToChargeOn_Sys(boolean rdoToChargeOn_Sys){
        this.rdoToChargeOn_Sys = rdoToChargeOn_Sys;
        setChanged();
    }
    boolean getRdoToChargeOn_Sys(){
        return this.rdoToChargeOn_Sys;
    }
    
    void setRdoToChargeOn_Both(boolean rdoToChargeOn_Both){
        this.rdoToChargeOn_Both = rdoToChargeOn_Both;
        setChanged();
    }
    boolean getRdoToChargeOn_Both(){
        return this.rdoToChargeOn_Both;
    }
    //------------------------------------------------
    
    void setCboIncompleteFolioRoundOffFreq(String cboIncompleteFolioRoundOffFreq){
        this.cboIncompleteFolioRoundOffFreq = cboIncompleteFolioRoundOffFreq;
        setChanged();
    }
    String getCboIncompleteFolioRoundOffFreq(){
        return this.cboIncompleteFolioRoundOffFreq;
    }
    public void setCbmIncompleteFolioRoundOffFreq(ComboBoxModel cbmIncompleteFolioRoundOffFreq){
        cbmIncompleteFolioRoundOffFreq = cbmIncompleteFolioRoundOffFreq;
        setChanged();
    }
    
    ComboBoxModel getCbmIncompleteFolioRoundOffFreq(){
        return cbmIncompleteFolioRoundOffFreq;
    }
    //------------------------------------------------
    
    void setRdoProcessCharges_Yes(boolean rdoProcessCharges_Yes){
        this.rdoProcessCharges_Yes = rdoProcessCharges_Yes;
        setChanged();
    }
    boolean getRdoProcessCharges_Yes(){
        return this.rdoProcessCharges_Yes;
    }
    
    void setRdoProcessCharges_No(boolean rdoProcessCharges_No){
        this.rdoProcessCharges_No = rdoProcessCharges_No;
        setChanged();
    }
    boolean getRdoProcessCharges_No(){
        return this.rdoProcessCharges_No;
    }
    
    //    void setTxtcharge_Limit1(String txtcharge_Limit1){
    //        this.txtcharge_Limit1 = txtcharge_Limit1;
    //        setChanged();
    //    }
    //    String getTxtcharge_Limit1(){
    //        return this.txtcharge_Limit1;
    //    }
    
    void setTxtCharge_Limit2(String txtCharge_Limit2){
        this.txtCharge_Limit2 = txtCharge_Limit2;
        setChanged();
    }
    String getTxtCharge_Limit2(){
        return this.txtCharge_Limit2;
    }
    
    void setRdoCommitmentCharge_Yes(boolean rdoCommitmentCharge_Yes){
        this.rdoCommitmentCharge_Yes = rdoCommitmentCharge_Yes;
        setChanged();
    }
    boolean getRdoCommitmentCharge_Yes(){
        return this.rdoCommitmentCharge_Yes;
    }
    
    void setRdoCommitmentCharge_No(boolean rdoCommitmentCharge_No){
        this.rdoCommitmentCharge_No = rdoCommitmentCharge_No;
        setChanged();
    }
    boolean getRdoCommitmentCharge_No(){
        return this.rdoCommitmentCharge_No;
    }
    
    void setTxtCharge_Limit3(String txtCharge_Limit3){
        this.txtCharge_Limit3 = txtCharge_Limit3;
        setChanged();
    }
    String getTxtCharge_Limit3(){
        return this.txtCharge_Limit3;
    }
    
    //    void setTxtCharge_Limit4(String txtCharge_Limit4){
    //        this.txtCharge_Limit4 = txtCharge_Limit4;
    //        setChanged();
    //    }
    //    String getTxtCharge_Limit4(){
    //        return this.txtCharge_Limit4;
    //    }
    
    void setRdoATMcardIssued_Yes(boolean rdoATMcardIssued_Yes){
        this.rdoATMcardIssued_Yes = rdoATMcardIssued_Yes;
        setChanged();
    }
    boolean getRdoATMcardIssued_Yes(){
        return this.rdoATMcardIssued_Yes;
    }
    
    void setRdoATMcardIssued_No(boolean rdoATMcardIssued_No){
        this.rdoATMcardIssued_No = rdoATMcardIssued_No;
        setChanged();
    }
    boolean getRdoATMcardIssued_No(){
        return this.rdoATMcardIssued_No;
    }
    
    void setRdoCreditCardIssued_Yes(boolean rdoCreditCardIssued_Yes){
        this.rdoCreditCardIssued_Yes = rdoCreditCardIssued_Yes;
        setChanged();
    }
    boolean getRdoCreditCardIssued_Yes(){
        return this.rdoCreditCardIssued_Yes;
    }
    
    void setRdoCreditCardIssued_No(boolean rdoCreditCardIssued_No){
        this.rdoCreditCardIssued_No = rdoCreditCardIssued_No;
        setChanged();
    }
    boolean getRdoCreditCardIssued_No(){
        return this.rdoCreditCardIssued_No;
    }
    
    void setRdoMobileBanlingClient_Yes(boolean rdoMobileBanlingClient_Yes){
        this.rdoMobileBanlingClient_Yes = rdoMobileBanlingClient_Yes;
        setChanged();
    }
    boolean getRdoMobileBanlingClient_Yes(){
        return this.rdoMobileBanlingClient_Yes;
    }
    
    void setRdoMobileBanlingClient_No(boolean rdoMobileBanlingClient_No){
        this.rdoMobileBanlingClient_No = rdoMobileBanlingClient_No;
        setChanged();
    }
    boolean getRdoMobileBanlingClient_No(){
        return this.rdoMobileBanlingClient_No;
    }
    
    void setRdoIsAnyBranBankingAllowed_Yes(boolean rdoIsAnyBranBankingAllowed_Yes){
        this.rdoIsAnyBranBankingAllowed_Yes = rdoIsAnyBranBankingAllowed_Yes;
        setChanged();
    }
    boolean getRdoIsAnyBranBankingAllowed_Yes(){
        return this.rdoIsAnyBranBankingAllowed_Yes;
    }
    
    void setRdoIsAnyBranBankingAllowed_No(boolean rdoIsAnyBranBankingAllowed_No){
        this.rdoIsAnyBranBankingAllowed_No = rdoIsAnyBranBankingAllowed_No;
        setChanged();
    }
    boolean getRdoIsAnyBranBankingAllowed_No(){
        return this.rdoIsAnyBranBankingAllowed_No;
    }
    
    void setTxtAccClosCharges(String txtAccClosCharges){
        this.txtAccClosCharges = txtAccClosCharges;
        setChanged();
    }
    String getTxtAccClosCharges(){
        return this.txtAccClosCharges;
    }
    
    void setTxtMiscServCharges(String txtMiscServCharges){
        this.txtMiscServCharges = txtMiscServCharges;
        setChanged();
    }
    String getTxtMiscServCharges(){
        return this.txtMiscServCharges;
    }
    
    void setTxtStatementCharges(String txtStatementCharges){
        this.txtStatementCharges = txtStatementCharges;
        setChanged();
    }
    String getTxtStatementCharges(){
        return this.txtStatementCharges;
    }
    
    void setTxtAccDebitInter(String txtAccDebitInter){
        this.txtAccDebitInter = txtAccDebitInter;
        setChanged();
    }
    String getTxtAccDebitInter(){
        return this.txtAccDebitInter;
    }
    
    void setTxtPenalInter(String txtPenalInter){
        this.txtPenalInter = txtPenalInter;
        setChanged();
    }
    String getTxtPenalInter(){
        return this.txtPenalInter;
    }
    
    void setTxtAccCreditInter(String txtAccCreditInter){
        this.txtAccCreditInter = txtAccCreditInter;
        setChanged();
    }
    String getTxtAccCreditInter(){
        return this.txtAccCreditInter;
    }
    
    void setTxtExpiryInter(String txtExpiryInter){
        this.txtExpiryInter = txtExpiryInter;
        setChanged();
    }
    String getTxtExpiryInter(){
        return this.txtExpiryInter;
    }
    
    void setTxtCheReturnChargest_Out(String txtCheReturnChargest_Out){
        this.txtCheReturnChargest_Out = txtCheReturnChargest_Out;
        setChanged();
    }
    String getTxtCheReturnChargest_Out(){
        return this.txtCheReturnChargest_Out;
    }
    
    void setTxtCheReturnChargest_In(String txtCheReturnChargest_In){
        this.txtCheReturnChargest_In = txtCheReturnChargest_In;
        setChanged();
    }
    String getTxtCheReturnChargest_In(){
        return this.txtCheReturnChargest_In;
    }
    
    void setTxtFolioChargesAcc(String txtFolioChargesAcc){
        this.txtFolioChargesAcc = txtFolioChargesAcc;
        setChanged();
    }
    String getTxtFolioChargesAcc(){
        return this.txtFolioChargesAcc;
    }
    
    void setTxtCommitCharges(String txtCommitCharges){
        this.txtCommitCharges = txtCommitCharges;
        setChanged();
    }
    String getTxtCommitCharges(){
        return this.txtCommitCharges;
    }
    
    void setTxtProcessingCharges(String txtProcessingCharges){
        this.txtProcessingCharges = txtProcessingCharges;
        setChanged();
    }
    String getTxtProcessingCharges(){
        return this.txtProcessingCharges;
    }
    
    void setTxtMinPeriodsArrears(String txtMinPeriodsArrears){
        this.txtMinPeriodsArrears = txtMinPeriodsArrears;
        setChanged();
    }
    String getTxtMinPeriodsArrears(){
        return this.txtMinPeriodsArrears;
    }
    //------------------------------------------------
    
    void setCboMinPeriodsArrears(String cboMinPeriodsArrears){
        this.cboMinPeriodsArrears = cboMinPeriodsArrears;
        setChanged();
    }
    String getCboMinPeriodsArrears(){
        return this.cboMinPeriodsArrears;
    }
    public void setCbmMinPeriodsArrears(ComboBoxModel cbmMinPeriodsArrears){
        cbmMinPeriodsArrears = cbmMinPeriodsArrears;
        setChanged();
    }
    
    ComboBoxModel getCbmMinPeriodsArrears(){
        return cbmMinPeriodsArrears;
    }
    //------------------------------------------------
    
    void setTxtPeriodTranSStanAssets(String txtPeriodTranSStanAssets){
        this.txtPeriodTranSStanAssets = txtPeriodTranSStanAssets;
        setChanged();
    }
    String getTxtPeriodTranSStanAssets(){
        return this.txtPeriodTranSStanAssets;
    }
    //------------------------------------------------
    
    void setCboPeriodTranSStanAssets(String cboPeriodTranSStanAssets){
        this.cboPeriodTranSStanAssets = cboPeriodTranSStanAssets;
        setChanged();
    }
    String getCboPeriodTranSStanAssets(){
        return this.cboPeriodTranSStanAssets;
    }
    public void setCbmPeriodTranSStanAssets(ComboBoxModel cbmPeriodTranSStanAssets){
        cbmPeriodTranSStanAssets = cbmPeriodTranSStanAssets;
        setChanged();
    }
    
    ComboBoxModel getCbmPeriodTranSStanAssets(){
        return cbmPeriodTranSStanAssets;
    }
    //------------------------------------------------
    
    void setTxtProvisionsStanAssets(String txtProvisionsStanAssets){
        this.txtProvisionsStanAssets = txtProvisionsStanAssets;
        setChanged();
    }
    String getTxtProvisionsStanAssets(){
        return this.txtProvisionsStanAssets;
    }
    
    void setTxtPeriodTransDoubtfulAssets1(String txtPeriodTransDoubtfulAssets1){
        this.txtPeriodTransDoubtfulAssets1 = txtPeriodTransDoubtfulAssets1;
        setChanged();
    }
    String getTxtPeriodTransDoubtfulAssets1(){
        return this.txtPeriodTransDoubtfulAssets1;
    }
    
    void setTxtPeriodTransDoubtfulAssets2(String txtPeriodTransDoubtfulAssets2){
        this.txtPeriodTransDoubtfulAssets2 = txtPeriodTransDoubtfulAssets2;
        setChanged();
    }
    String getTxtPeriodTransDoubtfulAssets2(){
        return this.txtPeriodTransDoubtfulAssets2;
    }
    
    void setTxtPeriodTransDoubtfulAssets3(String txtPeriodTransDoubtfulAssets3){
        this.txtPeriodTransDoubtfulAssets3 = txtPeriodTransDoubtfulAssets3;
        setChanged();
    }
    String getTxtPeriodTransDoubtfulAssets3(){
        return this.txtPeriodTransDoubtfulAssets3;
    }
    //------------------------------------------------
    
    void setCboPeriodTransDoubtfulAssets1(String cboPeriodTransDoubtfulAssets1){
        this.cboPeriodTransDoubtfulAssets1 = cboPeriodTransDoubtfulAssets1;
        setChanged();
    }
    String getCboPeriodTransDoubtfulAssets1(){
        return this.cboPeriodTransDoubtfulAssets1;
    }
    public void setCbmPeriodTransDoubtfulAssets1(ComboBoxModel cbmPeriodTransDoubtfulAssets1){
        cbmPeriodTransDoubtfulAssets1 = cbmPeriodTransDoubtfulAssets1;
        setChanged();
    }
    
    ComboBoxModel getCbmPeriodTransDoubtfulAssets1(){
        return cbmPeriodTransDoubtfulAssets1;
    }
    
    
    void setCboPeriodTransDoubtfulAssets2(String cboPeriodTransDoubtfulAssets2){
        this.cboPeriodTransDoubtfulAssets2 = cboPeriodTransDoubtfulAssets2;
        setChanged();
    }
    String getCboPeriodTransDoubtfulAssets2(){
        return this.cboPeriodTransDoubtfulAssets2;
    }
    public void setCbmPeriodTransDoubtfulAssets2(ComboBoxModel cbmPeriodTransDoubtfulAssets2){
        cbmPeriodTransDoubtfulAssets2 = cbmPeriodTransDoubtfulAssets2;
        setChanged();
    }
    
    ComboBoxModel getCbmPeriodTransDoubtfulAssets2(){
        return cbmPeriodTransDoubtfulAssets2;
    }
    
    void setCboPeriodTransDoubtfulAssets3(String cboPeriodTransDoubtfulAssets3){
        this.cboPeriodTransDoubtfulAssets3 = cboPeriodTransDoubtfulAssets3;
        setChanged();
    }
    String getCboPeriodTransDoubtfulAssets3(){
        return this.cboPeriodTransDoubtfulAssets3;
    }
    public void setCbmPeriodTransDoubtfulAssets3(ComboBoxModel cbmPeriodTransDoubtfulAssets3){
        cbmPeriodTransDoubtfulAssets3 = cbmPeriodTransDoubtfulAssets3;
        setChanged();
    }
    
    ComboBoxModel getCbmPeriodTransDoubtfulAssets3(){
        return cbmPeriodTransDoubtfulAssets3;
    }
    
    
    //------------------------------------------------
    
    void setTxtProvisionDoubtfulAssets1(String txtProvisionDoubtfulAssets1){
        this.txtProvisionDoubtfulAssets1 = txtProvisionDoubtfulAssets1;
        setChanged();
    }
    String getTxtProvisionDoubtfulAssets1(){
        return this.txtProvisionDoubtfulAssets1;
    }
    
    void setTxtProvisionDoubtfulAssets2(String txtProvisionDoubtfulAssets2){
        this.txtProvisionDoubtfulAssets2 = txtProvisionDoubtfulAssets2;
        setChanged();
    }
    String getTxtProvisionDoubtfulAssets2(){
        return this.txtProvisionDoubtfulAssets2;
    }
    
    void setTxtProvisionDoubtfulAssets3(String txtProvisionDoubtfulAssets3){
        this.txtProvisionDoubtfulAssets3 = txtProvisionDoubtfulAssets3;
        setChanged();
    }
    String getTxtProvisionDoubtfulAssets3(){
        return this.txtProvisionDoubtfulAssets3;
    }
    
    
    
    void setTxtPeriodTransLossAssets(String txtPeriodTransLossAssets){
        this.txtPeriodTransLossAssets = txtPeriodTransLossAssets;
        setChanged();
    }
    String getTxtPeriodTransLossAssets(){
        return this.txtPeriodTransLossAssets;
    }
    //------------------------------------------------
    
    void setCboPeriodTransLossAssets(String cboPeriodTransLossAssets){
        this.cboPeriodTransLossAssets = cboPeriodTransLossAssets;
        setChanged();
    }
    String getCboPeriodTransLossAssets(){
        return this.cboPeriodTransLossAssets;
    }
    public void setCbmPeriodTransLossAssets(ComboBoxModel cbmPeriodTransLossAssets){
        cbmPeriodTransLossAssets = cbmPeriodTransLossAssets;
        setChanged();
    }
    
    ComboBoxModel getCbmPeriodTransLossAssets(){
        return cbmPeriodTransLossAssets;
    }
    //------------------------------------------------
    
    void setTxtPeriodAfterWhichTransNPerformingAssets(String txtPeriodAfterWhichTransNPerformingAssets){
        this.txtPeriodAfterWhichTransNPerformingAssets = txtPeriodAfterWhichTransNPerformingAssets;
        setChanged();
    }
    String getTxtPeriodAfterWhichTransNPerformingAssets(){
        return this.txtPeriodAfterWhichTransNPerformingAssets;
    }
    //------------------------------------------------
    
    void setCboPeriodAfterWhichTransNPerformingAssets(String cboPeriodAfterWhichTransNPerformingAssets){
        this.cboPeriodAfterWhichTransNPerformingAssets = cboPeriodAfterWhichTransNPerformingAssets;
        setChanged();
    }
    String getCboPeriodAfterWhichTransNPerformingAssets(){
        return this.cboPeriodAfterWhichTransNPerformingAssets;
    }
    public void setCbmPeriodAfterWhichTransNPerformingAssets(ComboBoxModel cbmPeriodAfterWhichTransNPerformingAssets){
        cbmPeriodAfterWhichTransNPerformingAssets = cbmPeriodAfterWhichTransNPerformingAssets;
        setChanged();
    }
    
    ComboBoxModel getCbmPeriodAfterWhichTransNPerformingAssets(){
        return cbmPeriodAfterWhichTransNPerformingAssets;
    }
    //------------------------------------------------
    void setCbocalcType(String cbocalcType){
        this.cbocalcType = cbocalcType;
        setChanged();
    }
    String getCbocalcType(){
        return this.cbocalcType;
    }
    public void setCbmcalcType(ComboBoxModel cbmcalcType){
        cbmcalcType = cbmcalcType;
        setChanged();
    }
    
    ComboBoxModel getCbmcalcType(){
        return cbmcalcType;
    }
    //------------------------------------------------
    
    void setTxtMinPeriod(String txtMinPeriod){
        this.txtMinPeriod = txtMinPeriod;
        setChanged();
    }
    String getTxtMinPeriod(){
        return this.txtMinPeriod;
    }
    //------------------------------------------------
    
    void setCboMinPeriod(String cboMinPeriod){
        this.cboMinPeriod = cboMinPeriod;
        setChanged();
    }
    String getCboMinPeriod(){
        return this.cboMinPeriod;
    }
    public void setCbmMinPeriod(ComboBoxModel cbmMinPeriod){
        cbmMinPeriod = cbmMinPeriod;
        setChanged();
    }
    
    ComboBoxModel getCbmMinPeriod(){
        return cbmMinPeriod;
    }
    //------------------------------------------------
    
    void setTxtMaxPeriod(String txtMaxPeriod){
        this.txtMaxPeriod = txtMaxPeriod;
        setChanged();
    }
    String getTxtMaxPeriod(){
        return this.txtMaxPeriod;
    }
    //------------------------------------------------
    
    void setCboMaxPeriod(String cboMaxPeriod){
        this.cboMaxPeriod = cboMaxPeriod;
        setChanged();
    }
    String getCboMaxPeriod(){
        return this.cboMaxPeriod;
    }
    public void setCbmMaxPeriod(ComboBoxModel cbmMaxPeriod){
        cbmMaxPeriod = cbmMaxPeriod;
        setChanged();
    }
    
    ComboBoxModel getCbmMaxPeriod(){
        return cbmMaxPeriod;
    }
    //------------------------------------------------
    
    void setTxtMinAmtLoan(String txtMinAmtLoan){
        this.txtMinAmtLoan = txtMinAmtLoan;
        setChanged();
    }
    String getTxtMinAmtLoan(){
        return this.txtMinAmtLoan;
    }
    
    void setTxtMaxAmtLoan(String txtMaxAmtLoan){
        this.txtMaxAmtLoan = txtMaxAmtLoan;
        setChanged();
    }
    String getTxtMaxAmtLoan(){
        return this.txtMaxAmtLoan;
    }
    
    void setTxtApplicableInter(String txtApplicableInter){
        this.txtApplicableInter = txtApplicableInter;
        setChanged();
    }
    String getTxtApplicableInter(){
        return this.txtApplicableInter;
    }//setTxtApplicableInter
    
    //    void setTxtMaxInter(String txtMaxInter){
    //        this.txtMaxInter = txtMaxInter;
    //        setChanged();
    //    }
    //    String getTxtMaxInter(){
    //        return this.txtMaxInter;
    //    }
    
    void setTxtMinInterDebited(String txtMinInterDebited){
        this.txtMinInterDebited = txtMinInterDebited;
        setChanged();
    }
    String getTxtMinInterDebited(){
        return this.txtMinInterDebited;
    }
    
    void setRdoSubsidy_Yes(boolean rdoSubsidy_Yes){
        this.rdoSubsidy_Yes = rdoSubsidy_Yes;
        setChanged();
    }
    boolean getRdoSubsidy_Yes(){
        return this.rdoSubsidy_Yes;
    }
    
    void setRdoSubsidy_No(boolean rdoSubsidy_No){
        this.rdoSubsidy_No = rdoSubsidy_No;
        setChanged();
    }
    boolean getRdoSubsidy_No(){
        return this.rdoSubsidy_No;
    }
    //------------------------------------------------
    
    void setCboLoanPeriodMul(String cboLoanPeriodMul){
        this.cboLoanPeriodMul = cboLoanPeriodMul;
        setChanged();
    }
    String getCboLoanPeriodMul(){
        return this.cboLoanPeriodMul;
    }
    public void setCbmLoanPeriodMul(ComboBoxModel cbmLoanPeriodMul){
        cbmLoanPeriodMul = cbmLoanPeriodMul;
        setChanged();
    }
    
    ComboBoxModel getCbmLoanPeriodMul(){
        return cbmLoanPeriodMul;
    }
    //------------------------------------------------
    
    public String getLblStatus(){
        return lblStatus;
    }
    
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }
    
    // To add the setter and getter for the Comboboxes and Combobox Models...
    
    //private ComboBoxModel cbmCharge_Limit2;
    //private ComboBoxModel cbmCharge_Limit3;
    
    
    void setCboCharge_Limit2(String cboCharge_Limit2){
        this.cboCharge_Limit2 = cboCharge_Limit2;
        setChanged();
    }
    String getCboCharge_Limit2(){
        return this.cboCharge_Limit2;
    }
    public void setCbmCharge_Limit2(ComboBoxModel cbmCharge_Limit2){
        cbmCharge_Limit2 = cbmCharge_Limit2;
        setChanged();
    }
    
    ComboBoxModel getCbmCharge_Limit2(){
        return cbmCharge_Limit2;
    }
    
    //    public void setCbmProdCurrency(ComboBoxModel cbmProdCurrency){
    //        cbmProdCurrency = cbmProdCurrency;
    //        setChanged();
    //    }
    //
    //    ComboBoxModel getCbmProdCurrency(){
    //        return cbmProdCurrency;
    //    }
    
    void setCboCharge_Limit3(String cboCharge_Limit3){
        this.cboCharge_Limit3 = cboCharge_Limit3;
        setChanged();
    }
    String getCboCharge_Limit3(){
        return this.cboCharge_Limit3;
    }
    public void setCbmCharge_Limit3(ComboBoxModel cbmCharge_Limit3){
        cbmCharge_Limit3 = cbmCharge_Limit3;
        setChanged();
    }
    
    ComboBoxModel getCbmCharge_Limit3(){
        return cbmCharge_Limit3;
    }
    
    // Setter method for txtProvisionStandardAssetss
    void setTxtProvisionStandardAssetss(String txtProvisionStandardAssetss){
        this.txtProvisionStandardAssetss = txtProvisionStandardAssetss;
        setChanged();
    }
    // Getter method for txtProvisionStandardAssetss
    String getTxtProvisionStandardAssetss(){
        return this.txtProvisionStandardAssetss;
    }
    
    // Setter method for txtProvisionLossAssets
    void setTxtProvisionLossAssets(String txtProvisionLossAssets){
        this.txtProvisionLossAssets = txtProvisionLossAssets;
        setChanged();
    }
    // Getter method for txtProvisionLossAssets
    String getTxtProvisionLossAssets(){
        return this.txtProvisionLossAssets;
    }
    // Documents Starts
    public void setCbmDocumentType(ComboBoxModel cbmDocumentType){
        cbmDocumentType = cbmDocumentType;
        setChanged();
    }
    
    ComboBoxModel getCbmDocumentType(){
        return cbmDocumentType;
    }
    // Setter method for txtDocumentNo
    void setTxtDocumentNo(String txtDocumentNo){
        this.txtDocumentNo = txtDocumentNo;
        setChanged();
    }
    // Getter method for txtDocumentNo
    String getTxtDocumentNo(){
        return this.txtDocumentNo;
    }
    
    // Setter method for txtDocumentDesc
    void setTxtDocumentDesc(String txtDocumentDesc){
        this.txtDocumentDesc = txtDocumentDesc;
        setChanged();
    }
    // Getter method for txtDocumentDesc
    String getTxtDocumentDesc(){
        return this.txtDocumentDesc;
    }
    
    // Setter method for documentSerialNo
    void setDocumentSerialNo(String documentSerialNo){
        this.documentSerialNo = documentSerialNo;
        setChanged();
    }
    // Getter method for documentSerialNo
    String getDocumentSerialNo(){
        return this.documentSerialNo;
    }
    
    // Setter method for documentStatus
    void setDocumentStatus(String documentStatus){
        this.documentStatus = documentStatus;
        setChanged();
    }
    // Getter method for documentStatus
    String getDocumentStatus(){
        return this.documentStatus;
    }
    
    // Setter method for cboDocumentType
    void setCboDocumentType(String cboDocumentType){
        this.cboDocumentType = cboDocumentType;
        setChanged();
    }
    // Getter method for cboDocumentType
    String getCboDocumentType(){
        return this.cboDocumentType;
    }
    
    // Documents Ends
    void setTxtRangeFrom(String txtRangeFrom){
        this.txtRangeFrom = txtRangeFrom;
        setChanged();
    }
    String getTxtRangeFrom(){
        return this.txtRangeFrom;
    }
    
    void setTxtRangeTo(String txtRangeTo){
        this.txtRangeTo = txtRangeTo;
        setChanged();
    }
    String getTxtRangeTo(){
        return this.txtRangeTo;
    }
    
    //    void setCboProdCurrency(String cboProdCurrency){
    //        this.cboProdCurrency = cboProdCurrency;
    //        setChanged();
    //    }
    //    String getCboProdCurrency(){
    //        return this.cboProdCurrency;
    //    }
    //
    
    
    void setCboCommodityCode(String cboCommodityCode){
        this.cboCommodityCode = cboCommodityCode;
        setChanged();
    }
    String getCboCommodityCode(){
        return this.cboCommodityCode;
    }
    
    void setCbmCommodityCode(ComboBoxModel cbmCommodityCode){
        this.cbmCommodityCode = cbmCommodityCode;
        setChanged();
    }
    ComboBoxModel getCbmCommodityCode(){
        return this.cbmCommodityCode;
    }
    
    void setCbmGuaranteeCoverCode(ComboBoxModel cbmGuaranteeCoverCode){
        this.cbmGuaranteeCoverCode = cbmGuaranteeCoverCode;
        setChanged();
    }
    ComboBoxModel getCbmGuaranteeCoverCode(){
        return this.cbmGuaranteeCoverCode;
    }
    
    void setCboGuaranteeCoverCode(String cboGuaranteeCoverCode){
        this.cboGuaranteeCoverCode = cboGuaranteeCoverCode;
        setChanged();
    }
    String getCboGuaranteeCoverCode(){
        return this.cboGuaranteeCoverCode;
    }
    
    void setCbmSectorCode(ComboBoxModel cbmSectorCode){
        this.cbmSectorCode = cbmSectorCode;
        setChanged();
    }
    ComboBoxModel getCbmSectorCode(){
        return this.cbmSectorCode;
    }
    
    void setCboSectorCode(String cboSectorCode){
        this.cboSectorCode = cboSectorCode;
        setChanged();
    }
    String getCboSectorCode(){
        return this.cboSectorCode;
    }
    
    void setCbmHealthCode(ComboBoxModel cbmHealthCode){
        this.cbmHealthCode = cbmHealthCode;
        setChanged();
    }
    ComboBoxModel getCbmHealthCode(){
        return this.cbmHealthCode;
    }
    
    void setCboHealthCode(String cboHealthCode){
        this.cboHealthCode = cboHealthCode;
        setChanged();
    }
    String getCboHealthCode(){
        return this.cboHealthCode;
    }
    
    void setCbmTypeFacility(ComboBoxModel cbmTypeFacility){
        this.cbmTypeFacility = cbmTypeFacility;
        setChanged();
    }
    ComboBoxModel getCbmTypeFacility(){
        return this.cbmTypeFacility;
    }
    
    void setCboTypeFacility(String cboTypeFacility){
        this.cboTypeFacility = cboTypeFacility;
        setChanged();
    }
    String getCboTypeFacility(){
        return this.cboTypeFacility;
    }
    
    void setCbmIndusCode(ComboBoxModel cbmIndusCode){
        this.cbmIndusCode = cbmIndusCode;
        setChanged();
    }
    ComboBoxModel getCbmIndusCode(){
        return this.cbmIndusCode;
    }
    
    void setCboIndusCode(String cboIndusCode){
        this.cboIndusCode = cboIndusCode;
        setChanged();
    }
    String getCboIndusCode(){
        return this.cboIndusCode;
    }
    
    void setCbm20Code(ComboBoxModel cbm20Code){
        this.cbm20Code = cbm20Code;
        setChanged();
    }
    ComboBoxModel getCbm20Code(){
        return this.cbm20Code;
    }
    
    void setCbo20Code(String cbo20Code){
        this.cbo20Code = cbo20Code;
        setChanged();
    }
    String getCbo20Code(){
        return this.cbo20Code;
    }
    
    void setCbmRefinancingInsti(ComboBoxModel cbmRefinancingInsti){
        this.cbmRefinancingInsti = cbmRefinancingInsti;
        setChanged();
    }
    ComboBoxModel getCbmRefinancingInsti(){
        return this.cbmRefinancingInsti;
    }
    
    void setCboRefinancingInsti(String cboRefinancingInsti){
        this.cboRefinancingInsti = cboRefinancingInsti;
        setChanged();
    }
    String getCboRefinancingInsti(){
        return this.cboRefinancingInsti;
    }
    
    void setCbmGovtSchemeCode(ComboBoxModel cbmGovtSchemeCode){
        this.cbmGovtSchemeCode = cbmGovtSchemeCode;
        setChanged();
    }
    ComboBoxModel getCbmGovtSchemeCode(){
        return this.cbmGovtSchemeCode;
    }
    
    void setCboGovtSchemeCode(String cboGovtSchemeCode){
        this.cboGovtSchemeCode = cboGovtSchemeCode;
        setChanged();
    }
    String getCboGovtSchemeCode(){
        return this.cboGovtSchemeCode;
    }
    
    void setCbmPurposeCode(ComboBoxModel cbmPurposeCode){
        this.cbmPurposeCode = cbmPurposeCode;
        setChanged();
    }
    ComboBoxModel getCbmPurposeCode(){
        return this.cbmPurposeCode;
    }
    
    void setCboPurposeCode(String cboPurposeCode){
        this.cboPurposeCode = cboPurposeCode;
        setChanged();
    }
    String getCboPurposeCode(){
        return this.cboPurposeCode;
    }
    //
    void setCbmSecurityDeails(ComboBoxModel cbmSecurityDeails){
        this.cbmSecurityDeails = cbmSecurityDeails;
        setChanged();
    }
    ComboBoxModel getCbmSecurityDeails(){
        return this.cbmSecurityDeails;
    }
    
    void setCboSecurityDeails(String cboSecurityDeails){
        this.cboSecurityDeails = cboSecurityDeails;
        setChanged();
    }
    String getCboSecurityDeails(){
        return this.cboSecurityDeails;
    }
    
    void setChkDirectFinance(boolean chkDirectFinance){
        this.chkDirectFinance = chkDirectFinance;
        setChanged();
    }
    boolean getChkDirectFinance(){
        return this.chkDirectFinance;
    }
    
    void setChkECGC(boolean chkECGC){
        this.chkECGC = chkECGC;
        setChanged();
    }
    boolean getChkECGC(){
        return this.chkECGC;
    }
    
    void setChkQIS(boolean chkQIS){
        this.chkQIS = chkQIS;
        setChanged();
    }
    
    boolean getChkQIS(){
        return this.chkQIS;
    }
    
    
    
    public void verifyAcctHead(com.see.truetransact.uicomponent.CTextField accountHead, String mapName) {
        try{
            final HashMap data = new HashMap();
            data.put("ACCT_HD",accountHead.getText());
            data.put(CommonConstants.MAP_NAME , mapName);
            HashMap proxyResultMap = proxy.execute(data,operationMap);
        }catch(Exception e){
            accountHead.setText("");
            parseException.logException(e,true);
        }
    }
    
    public boolean verifyProdId(String prodId) {
        boolean verify = false;
        try{
            final HashMap data = new HashMap();
            data.put("PROD_ID",prodId);
            final List resultList = ClientUtil.executeQuery("AgriTermLoan.getProdId", data);
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
    
    public int getRowCount(){
        return tblChargeTab.getRowCount();
    }
    /**
     * Set Column values in the Document details table
     */
    private ArrayList setColumnValuesForDocumentsTable(int rowClicked,AgriLoanProductDocumentsTO objLoanProductDocumentsTO) {
        ArrayList row = new ArrayList();
        row.add(String.valueOf(rowClicked));
        row.add((String) getCbmDocumentType().getDataForKey(CommonUtil.convertObjToStr(objLoanProductDocumentsTO.getDocType())));
        row.add(CommonUtil.convertObjToStr(objLoanProductDocumentsTO.getDocNo()));
        return row;
        
    }
    /**
     * Checking for duplication of document types
     */
    public boolean checkDocumentType(String prodId, String selectedDocType, String docNo, boolean press, int index) {
        boolean flag = false;
        try {
            if (((prodId.length()>0) && (prodId != null))&&((selectedDocType.length()>0) && (selectedDocType != null))&&((docNo.length()>0) && (docNo != null))) {
                int size = 0,count=0;
                if (documentsTO != null) {
                    size = documentsTO.size();
                    
                    for (int i=size;i>=1;--i) {
                        AgriLoanProductDocumentsTO loanProductDocumentsTO = (AgriLoanProductDocumentsTO) documentsTO.get(String.valueOf(i));
                        String docType = CommonUtil.convertObjToStr(loanProductDocumentsTO.getDocType());
                        String doc_No = CommonUtil.convertObjToStr(loanProductDocumentsTO.getDocNo());
                        String prod_Id = CommonUtil.convertObjToStr(loanProductDocumentsTO.getProdId());
                        
                        if ((press)&&(index>=0)&& (prodId.equals(prod_Id))&&(selectedDocType.equals(docType)) && (docNo.equals(doc_No))) {
                            if (String.valueOf(index+1).equals(String.valueOf(i))) {
                                // Nothing to do
                                flag = false;
                            } else {
                                flag = true;
                                break;
                                
                            }
                            
                        } else if ((!press)&& (prodId.equals(prod_Id)) && (selectedDocType.equals(docType)) && (docNo.equals(doc_No))) {
                            // Document Type and Document No are duplicated
                            flag = true;
                            break;
                            
                        }
                        
                        loanProductDocumentsTO = null;
                        docType = null;
                        doc_No = null;
                    }
                }
            }
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        return flag;
    }
    
    /**
     * Action Performed when Delete is pressed in Documents
     */
    public void deleteDocuments(int index) {
        log.info("deleteDocuments Invoked...");
        try {
            if (documentsTO != null) {
                AgriLoanProductDocumentsTO loanProductDocumentsTO = (AgriLoanProductDocumentsTO) documentsTO.remove(String.valueOf(index+1));
                if( ( loanProductDocumentsTO.getStatus().length()>0 ) && ( loanProductDocumentsTO.getStatus() != null ) && !(loanProductDocumentsTO.getStatus().equals(""))) {
                    if (deletedDocumentsTO == null)
                        deletedDocumentsTO = new LinkedHashMap();
                    deletedDocumentsTO.put(String.valueOf(deletedDocSI_No++), loanProductDocumentsTO);
                } else if (documentsTO != null) {
                    for(int i = index+1,j=documentsTO.size();i<=j;i++) {
                        documentsTO.put(String.valueOf(i),(AgriLoanProductDocumentsTO)documentsTO.remove(String.valueOf((i+1))));
                    }
                    
                }
                loanProductDocumentsTO = null;
                docSI_No--;
                // Reset table data
                entireRows.remove(index);
                 /* Orders the serial no in the arraylist (tableData) after the removal
               of selected Row in the table */
                for(int i=0,j = entireRows.size();i<j;i++){
                    ( (ArrayList) entireRows.get(i)).set(0,String.valueOf(i+1));
                }
                tblDocuments.setDataArrayList(entireRows,documentsTabTitle);
                
            }
        } catch (Exception  e){
            parseException.logException(e,true);
        }
    }
    
    public void deleteNoticeCharge(int index) {
        log.info("deleteDocuments Invoked...");
        try {
            if (NoticeTypeTO != null) {
                AgriLoanProductChargesTabTO loanProductChargesTabTO = (AgriLoanProductChargesTabTO) NoticeTypeTO.remove(String.valueOf(index+1));
                //                if( ( LoanProductChargesTabTO.getStatus().length()>0 ) && ( LoanProductChargesTabTO.getStatus() != null ) && !(LoanProductChargesTabTO.getStatus().equals(""))) {
                if (deletedDocumentsTO == null)
                    deletedNoticeType = new LinkedHashMap();
                deletedNoticeType.put(String.valueOf(deleted_NoticeCharge++), loanProductChargesTabTO);
                if (NoticeTypeTO != null) {
                    for(int i = index+1,j=NoticeTypeTO.size();i<=j;i++) {
                        NoticeTypeTO.put(String.valueOf(i),(AgriLoanProductChargesTabTO)NoticeTypeTO.remove(String.valueOf((i+1))));
                    }
                    
                }
                loanProductChargesTabTO = null;
                deleted_NoticeCharge--;
                // Reset table data
                entireNoticeTypeRow.remove(index);
                 /* Orders the serial no in the arraylist (tableData) after the removal
               of selected Row in the table */
                System.out.println("DeleteNoticeCharge####"+NoticeTypeTO);
                for(int i=0,j = entireNoticeTypeRow.size();i<j;i++){
                    ( (ArrayList) entireNoticeTypeRow.get(i)).set(0,String.valueOf(i+1));
                }
                tblNoticeCharge.setDataArrayList(entireNoticeTypeRow,noticeChargeTabTitle);
                
            }
        } catch (Exception  e){
            parseException.logException(e,true);
        }
    }
    /**
     * populate the selected documents detaisl
     * Also check the status null or not
     */
    public boolean populateDocuments(int row) {
        log.info("populateDocuments Invoked...");
        boolean flag = false;
        try {
            AgriLoanProductDocumentsTO loanProductDocumentsTO = (AgriLoanProductDocumentsTO) documentsTO.get(String.valueOf(row+1));
            if ((loanProductDocumentsTO.getStatus() != null) && (loanProductDocumentsTO.getStatus().length() > 0) && !(loanProductDocumentsTO.getStatus().equals(""))) {
                flag = false;
            } else {
                flag = true;
            }
            setLoanProductDocumentsOB(loanProductDocumentsTO);
        } catch(Exception e) {
            parseException.logException(e,true);
        }
        return flag;
    }
    /**
     * mandatory check for document fields
     */
    public boolean checkMandatoryForDocuments(String documentType, String documentNo) {
        log.info("checkMandatoryForDocuments Invoked...");
        boolean flag = false;
        try {
            if (documentType != null && documentNo != null && documentNo.length() > 0 && documentType.length() > 0) {
                flag = false;
            } else {
                flag = true;
            }
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        return flag;
    }
    /**
     * Save Performed when save button in Documents in pressed
     */
    public void saveDocuments(boolean tableClick,int rowClicked) {
        log.info("saveDocuments Invoked...");
        try {
            AgriLoanProductDocumentsTO objLoanProductDocumentsTO = setLoanProductDocumentsTO();
            if (documentsTO == null)
                documentsTO = new LinkedHashMap();
            if (entireRows == null)
                entireRows =  new ArrayList();
            if (tableClick) {
                objLoanProductDocumentsTO.setSINo(String.valueOf(rowClicked+1));
                entireRows.set(rowClicked, setColumnValuesForDocumentsTable(rowClicked+1, objLoanProductDocumentsTO));
                documentsTO.put(String.valueOf(rowClicked+1), objLoanProductDocumentsTO);
                setLoanProductDocumentsOB(objLoanProductDocumentsTO);
            } else {
                objLoanProductDocumentsTO.setSINo(String.valueOf(docSI_No));
                entireRows.add(setColumnValuesForDocumentsTable(docSI_No, objLoanProductDocumentsTO));
                documentsTO.put(String.valueOf(docSI_No),objLoanProductDocumentsTO);
                docSI_No++;
            }
            
            objLoanProductDocumentsTO = null;
            tblDocuments.setDataArrayList(entireRows,documentsTabTitle);
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    
    public void saveNoticeCharge(boolean tableClicked,int rowClick){
        try{
            AgriLoanProductChargesTabTO objloanProductChargesTabTO=setLoanProductNoticeChargesTO();
            if(NoticeTypeTO==null)
                NoticeTypeTO=new LinkedHashMap();
            if(entireNoticeTypeRow==null)
                entireNoticeTypeRow=new ArrayList();
            if(tableClicked) {
                entireNoticeTypeRow.set(rowClick, setColumnValueNoticeCharge((rowClick+1), objloanProductChargesTabTO));
                NoticeTypeTO.put(String.valueOf(rowClick+1), objloanProductChargesTabTO);
                //                setLoanProductDocumentsOB(objLoanProductDocumentsTO);
                setLoanProductNoticeChargeOB(objloanProductChargesTabTO);
                
            }else{
                entireNoticeTypeRow.add(setColumnValueNoticeCharge(notice_Charge_No,objloanProductChargesTabTO));
                NoticeTypeTO.put(String.valueOf(notice_Charge_No),objloanProductChargesTabTO);
                notice_Charge_No++;
                System.out.println(entireNoticeTypeRow+":  notice value###ARRayList   :"+NoticeTypeTO);
            }
            
            objloanProductChargesTabTO=null;
            tblNoticeCharge.setDataArrayList(entireNoticeTypeRow, noticeChargeTabTitle);
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public ArrayList setColumnValueNoticeCharge(int rowClicked,AgriLoanProductChargesTabTO objLoanProductChargesTabTO){
        ArrayList row =new ArrayList();
        row.add((String) getCbmNoticeType().getDataForKey(objLoanProductChargesTabTO.getNoticeType()));
        row.add((String)getCbmIssueAfter().getDataForKey(objLoanProductChargesTabTO.getIssueAfter()));
        row.add(CommonUtil.convertObjToStr(objLoanProductChargesTabTO.getNoticeChargeAmt()));
        row.add(CommonUtil.convertObjToStr(objLoanProductChargesTabTO.getPostageAmt()));
        return row;
        
        
        
    }
    
    public AgriLoanProductChargesTabTO setLoanProductNoticeChargesTO(){
        
        AgriLoanProductChargesTabTO objLoanProductChargesTabTO=new  AgriLoanProductChargesTabTO();
        objLoanProductChargesTabTO.setNoticeType(CommonUtil.convertObjToStr(getCbmNoticeType().getKeyForSelected()));
        objLoanProductChargesTabTO.setIssueAfter(CommonUtil.convertObjToStr(getCbmIssueAfter().getKeyForSelected()));
        objLoanProductChargesTabTO.setNoticeChargeAmt(new Double(Double.parseDouble(getTxtNoticeChargeAmt())));
        objLoanProductChargesTabTO.setPostageAmt(new Double(Double.parseDouble(getTxtPostageChargeAmt())));
        return objLoanProductChargesTabTO;
    }
    
    public void setLoanProductNoticeChargeOB(AgriLoanProductChargesTabTO objLoanProductChargesTabTO){
        setCboNoticeType((String) getCbmNoticeType().getDataForKey(CommonUtil.convertObjToStr(objLoanProductChargesTabTO.getNoticeType())));
        setCboIssueAfter((String) getCbmIssueAfter().getDataForKey(CommonUtil.convertObjToStr(objLoanProductChargesTabTO.getIssueAfter())));
        setTxtNoticeChargeAmt(CommonUtil.convertObjToStr(objLoanProductChargesTabTO.getNoticeChargeAmt()));
        setTxtProcessingCharges(CommonUtil.convertObjToStr(objLoanProductChargesTabTO.getPostageAmt()));
    }
    /**
     * Getter for property cbmNoticeType.
     * @return Value of property cbmNoticeType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmNoticeType() {
        return cbmNoticeType;
    }
    
    /**
     * Setter for property cbmNoticeType.
     * @param cbmNoticeType New value of property cbmNoticeType.
     */
    public void setCbmNoticeType(com.see.truetransact.clientutil.ComboBoxModel cbmNoticeType) {
        this.cbmNoticeType = cbmNoticeType;
    }
    
    /**
     * Getter for property cbmIssueAfter.
     * @return Value of property cbmIssueAfter.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmIssueAfter() {
        return cbmIssueAfter;
    }
    
    /**
     * Setter for property cbmIssueAfter.
     * @param cbmIssueAfter New value of property cbmIssueAfter.
     */
    public void setCbmIssueAfter(com.see.truetransact.clientutil.ComboBoxModel cbmIssueAfter) {
        this.cbmIssueAfter = cbmIssueAfter;
    }
    
    /**
     * Getter for property cboNoticeType.
     * @return Value of property cboNoticeType.
     */
    public java.lang.String getCboNoticeType() {
        return cboNoticeType;
    }
    
    /**
     * Setter for property cboNoticeType.
     * @param cboNoticeType New value of property cboNoticeType.
     */
    public void setCboNoticeType(java.lang.String cboNoticeType) {
        this.cboNoticeType = cboNoticeType;
    }
    
    /**
     * Getter for property cboIssueAfter.
     * @return Value of property cboIssueAfter.
     */
    public java.lang.String getCboIssueAfter() {
        return cboIssueAfter;
    }
    
    /**
     * Setter for property cboIssueAfter.
     * @param cboIssueAfter New value of property cboIssueAfter.
     */
    public void setCboIssueAfter(java.lang.String cboIssueAfter) {
        this.cboIssueAfter = cboIssueAfter;
    }
    
    /**
     * Getter for property txtNoticeChargeAmt.
     * @return Value of property txtNoticeChargeAmt.
     */
    public java.lang.String getTxtNoticeChargeAmt() {
        return txtNoticeChargeAmt;
    }
    
    /**
     * Setter for property txtNoticeChargeAmt.
     * @param txtNoticeChargeAmt New value of property txtNoticeChargeAmt.
     */
    public void setTxtNoticeChargeAmt(java.lang.String txtNoticeChargeAmt) {
        this.txtNoticeChargeAmt = txtNoticeChargeAmt;
    }
    
    /**
     * Getter for property txtPostageChargeAmt.
     * @return Value of property txtPostageChargeAmt.
     */
    public java.lang.String getTxtPostageChargeAmt() {
        return txtPostageChargeAmt;
    }
    
    /**
     * Setter for property txtPostageChargeAmt.
     * @param txtPostageChargeAmt New value of property txtPostageChargeAmt.
     */
    public void setTxtPostageChargeAmt(java.lang.String txtPostageChargeAmt) {
        this.txtPostageChargeAmt = txtPostageChargeAmt;
    }
    
    /**
     * Getter for property tblNoticeCharge.
     * @return Value of property tblNoticeCharge.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblNoticeCharge() {
        return tblNoticeCharge;
    }
    
    /**
     * Setter for property tblNoticeCharge.
     * @param tblNoticeCharge New value of property tblNoticeCharge.
     */
    public void setTblNoticeCharge(com.see.truetransact.clientutil.EnhancedTableModel tblNoticeCharge) {
        this.tblNoticeCharge = tblNoticeCharge;
    }
    
    /**
     * Getter for property txtNoticeCharges.
     * @return Value of property txtNoticeCharges.
     */
    public java.lang.String getTxtNoticeCharges() {
        return txtNoticeCharges;
    }
    
    /**
     * Setter for property txtNoticeCharges.
     * @param txtNoticeCharges New value of property txtNoticeCharges.
     */
    public void setTxtNoticeCharges(java.lang.String txtNoticeCharges) {
        this.txtNoticeCharges = txtNoticeCharges;
    }
    
    /**
     * Getter for property txtPostageCharges.
     * @return Value of property txtPostageCharges.
     */
    public java.lang.String getTxtPostageCharges() {
        return txtPostageCharges;
    }
    
    /**
     * Setter for property txtPostageCharges.
     * @param txtPostageCharges New value of property txtPostageCharges.
     */
    public void setTxtPostageCharges(java.lang.String txtPostageCharges) {
        this.txtPostageCharges = txtPostageCharges;
    }
    
    /**
     * Getter for property txtIntPayableAccount.
     * @return Value of property txtIntPayableAccount.
     */
    public java.lang.String getTxtIntPayableAccount() {
        return txtIntPayableAccount;
    }
    
    /**
     * Setter for property txtIntPayableAccount.
     * @param txtIntPayableAccount New value of property txtIntPayableAccount.
     */
    public void setTxtIntPayableAccount(java.lang.String txtIntPayableAccount) {
        this.txtIntPayableAccount = txtIntPayableAccount;
    }
    
    /**
     * Getter for property rdoToChargeType_Credit.
     * @return Value of property rdoToChargeType_Credit.
     */
    public boolean isRdoToChargeType_Credit() {
        return rdoToChargeType_Credit;
    }
    
    /**
     * Setter for property rdoToChargeType_Credit.
     * @param rdoToChargeType_Credit New value of property rdoToChargeType_Credit.
     */
    public void setRdoToChargeType_Credit(boolean rdoToChargeType_Credit) {
        this.rdoToChargeType_Credit = rdoToChargeType_Credit;
    }
    
    /**
     * Getter for property rdoToChargeType_Debit.
     * @return Value of property rdoToChargeType_Debit.
     */
    public boolean isRdoToChargeType_Debit() {
        return rdoToChargeType_Debit;
    }
    
    /**
     * Setter for property rdoToChargeType_Debit.
     * @param rdoToChargeType_Debit New value of property rdoToChargeType_Debit.
     */
    public void setRdoToChargeType_Debit(boolean rdoToChargeType_Debit) {
        this.rdoToChargeType_Debit = rdoToChargeType_Debit;
    }
    
    /**
     * Getter for property rdoToChargeType_Both.
     * @return Value of property rdoToChargeType_Both.
     */
    public boolean isRdoToChargeType_Both() {
        return rdoToChargeType_Both;
    }
    
    /**
     * Setter for property rdoToChargeType_Both.
     * @param rdoToChargeType_Both New value of property rdoToChargeType_Both.
     */
    public void setRdoToChargeType_Both(boolean rdoToChargeType_Both) {
        this.rdoToChargeType_Both = rdoToChargeType_Both;
    }
    
    /**
     * Getter for property rdoasAndWhenCustomer_Yes.
     * @return Value of property rdoasAndWhenCustomer_Yes.
     */
    public boolean isRdoasAndWhenCustomer_Yes() {
        return rdoasAndWhenCustomer_Yes;
    }
    
    /**
     * Setter for property rdoasAndWhenCustomer_Yes.
     * @param rdoasAndWhenCustomer_Yes New value of property rdoasAndWhenCustomer_Yes.
     */
    public void setRdoasAndWhenCustomer_Yes(boolean rdoasAndWhenCustomer_Yes) {
        this.rdoasAndWhenCustomer_Yes = rdoasAndWhenCustomer_Yes;
    }
    
    /**
     * Getter for property rdoasAndWhenCustomer_No.
     * @return Value of property rdoasAndWhenCustomer_No.
     */
    public boolean isRdoasAndWhenCustomer_No() {
        return rdoasAndWhenCustomer_No;
    }
    
    /**
     * Setter for property rdoasAndWhenCustomer_No.
     * @param rdoasAndWhenCustomer_No New value of property rdoasAndWhenCustomer_No.
     */
    public void setRdoasAndWhenCustomer_No(boolean rdoasAndWhenCustomer_No) {
        this.rdoasAndWhenCustomer_No = rdoasAndWhenCustomer_No;
    }
    
    /**
     * Getter for property rdocalendarFrequency_Yes.
     * @return Value of property rdocalendarFrequency_Yes.
     */
    public boolean isRdocalendarFrequency_Yes() {
        return rdocalendarFrequency_Yes;
    }
    
    /**
     * Setter for property rdocalendarFrequency_Yes.
     * @param rdocalendarFrequency_Yes New value of property rdocalendarFrequency_Yes.
     */
    public void setRdocalendarFrequency_Yes(boolean rdocalendarFrequency_Yes) {
        this.rdocalendarFrequency_Yes = rdocalendarFrequency_Yes;
    }
    
    /**
     * Getter for property rdocalendarFrequency_No.
     * @return Value of property rdocalendarFrequency_No.
     */
    public boolean isRdocalendarFrequency_No() {
        return rdocalendarFrequency_No;
    }
    
    /**
     * Setter for property rdocalendarFrequency_No.
     * @param rdocalendarFrequency_No New value of property rdocalendarFrequency_No.
     */
    public void setRdocalendarFrequency_No(boolean rdocalendarFrequency_No) {
        this.rdocalendarFrequency_No = rdocalendarFrequency_No;
    }
    
    /**
     * Getter for property chkprincipalDue.
     * @return Value of property chkprincipalDue.
     */
    public boolean isChkprincipalDue() {
        return chkprincipalDue;
    }
    
    /**
     * Setter for property chkprincipalDue.
     * @param chkprincipalDue New value of property chkprincipalDue.
     */
    public void setChkprincipalDue(boolean chkprincipalDue) {
        this.chkprincipalDue = chkprincipalDue;
    }
    
    /**
     * Getter for property chkInterestDue.
     * @return Value of property chkInterestDue.
     */
    public boolean isChkInterestDue() {
        return chkInterestDue;
    }
    
    /**
     * Setter for property chkInterestDue.
     * @param chkInterestDue New value of property chkInterestDue.
     */
    public void setChkInterestDue(boolean chkInterestDue) {
        this.chkInterestDue = chkInterestDue;
    }
    
    /**
     * Getter for property txtLegalCharges.
     * @return Value of property txtLegalCharges.
     */
    public java.lang.String getTxtLegalCharges() {
        return txtLegalCharges;
    }
    
    /**
     * Setter for property txtLegalCharges.
     * @param txtLegalCharges New value of property txtLegalCharges.
     */
    public void setTxtLegalCharges(java.lang.String txtLegalCharges) {
        this.txtLegalCharges = txtLegalCharges;
    }
    
    /**
     * Getter for property txtMiscCharges.
     * @return Value of property txtMiscCharges.
     */
    public java.lang.String getTxtMiscCharges() {
        return txtMiscCharges;
    }
    
    /**
     * Setter for property txtMiscCharges.
     * @param txtMiscCharges New value of property txtMiscCharges.
     */
    public void setTxtMiscCharges(java.lang.String txtMiscCharges) {
        this.txtMiscCharges = txtMiscCharges;
    }
    
    /**
     * Getter for property txtArbitraryCharges.
     * @return Value of property txtArbitraryCharges.
     */
    public java.lang.String getTxtArbitraryCharges() {
        return txtArbitraryCharges;
    }
    
    /**
     * Setter for property txtArbitraryCharges.
     * @param txtArbitraryCharges New value of property txtArbitraryCharges.
     */
    public void setTxtArbitraryCharges(java.lang.String txtArbitraryCharges) {
        this.txtArbitraryCharges = txtArbitraryCharges;
    }
    
    /**
     * Getter for property txtInsuranceCharges.
     * @return Value of property txtInsuranceCharges.
     */
    public java.lang.String getTxtInsuranceCharges() {
        return txtInsuranceCharges;
    }
    
    /**
     * Setter for property txtInsuranceCharges.
     * @param txtInsuranceCharges New value of property txtInsuranceCharges.
     */
    public void setTxtInsuranceCharges(java.lang.String txtInsuranceCharges) {
        this.txtInsuranceCharges = txtInsuranceCharges;
    }
    
    /**
     * Getter for property txtExecutionDecreeCharges.
     * @return Value of property txtExecutionDecreeCharges.
     */
    public java.lang.String getTxtExecutionDecreeCharges() {
        return txtExecutionDecreeCharges;
    }
    
    /**
     * Setter for property txtExecutionDecreeCharges.
     * @param txtExecutionDecreeCharges New value of property txtExecutionDecreeCharges.
     */
    public void setTxtExecutionDecreeCharges(java.lang.String txtExecutionDecreeCharges) {
        this.txtExecutionDecreeCharges = txtExecutionDecreeCharges;
    }
    
    /**
     * Getter for property cboInsuranceType.
     * @return Value of property cboInsuranceType.
     */
    public java.lang.String getCboInsuranceType() {
        return cboInsuranceType;
    }
    
    /**
     * Setter for property cboInsuranceType.
     * @param cboInsuranceType New value of property cboInsuranceType.
     */
    public void setCboInsuranceType(java.lang.String cboInsuranceType) {
        this.cboInsuranceType = cboInsuranceType;
    }
    
    /**
     * Getter for property cboInsuranceUnderScheme.
     * @return Value of property cboInsuranceUnderScheme.
     */
    public java.lang.String getCboInsuranceUnderScheme() {
        return cboInsuranceUnderScheme;
    }
    
    /**
     * Setter for property cboInsuranceUnderScheme.
     * @param cboInsuranceUnderScheme New value of property cboInsuranceUnderScheme.
     */
    public void setCboInsuranceUnderScheme(java.lang.String cboInsuranceUnderScheme) {
        this.cboInsuranceUnderScheme = cboInsuranceUnderScheme;
    }
    
    /**
     * Getter for property txtBankSharePremium.
     * @return Value of property txtBankSharePremium.
     */
    public java.lang.String getTxtBankSharePremium() {
        return txtBankSharePremium;
    }
    
    /**
     * Setter for property txtBankSharePremium.
     * @param txtBankSharePremium New value of property txtBankSharePremium.
     */
    public void setTxtBankSharePremium(java.lang.String txtBankSharePremium) {
        this.txtBankSharePremium = txtBankSharePremium;
    }
    
    /**
     * Getter for property txtCustomerSharePremium.
     * @return Value of property txtCustomerSharePremium.
     */
    public java.lang.String getTxtCustomerSharePremium() {
        return txtCustomerSharePremium;
    }
    
    /**
     * Setter for property txtCustomerSharePremium.
     * @param txtCustomerSharePremium New value of property txtCustomerSharePremium.
     */
    public void setTxtCustomerSharePremium(java.lang.String txtCustomerSharePremium) {
        this.txtCustomerSharePremium = txtCustomerSharePremium;
    }
    
    /**
     * Getter for property cbmInsuranceType.
     * @return Value of property cbmInsuranceType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmInsuranceType() {
        return cbmInsuranceType;
    }
    
    /**
     * Setter for property cbmInsuranceType.
     * @param cbmInsuranceType New value of property cbmInsuranceType.
     */
    public void setCbmInsuranceType(com.see.truetransact.clientutil.ComboBoxModel cbmInsuranceType) {
        this.cbmInsuranceType = cbmInsuranceType;
    }
    
    /**
     * Getter for property cbmInsuranceUnderScheme.
     * @return Value of property cbmInsuranceUnderScheme.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmInsuranceUnderScheme() {
        return cbmInsuranceUnderScheme;
    }
    
    /**
     * Setter for property cbmInsuranceUnderScheme.
     * @param cbmInsuranceUnderScheme New value of property cbmInsuranceUnderScheme.
     */
    public void setCbmInsuranceUnderScheme(com.see.truetransact.clientutil.ComboBoxModel cbmInsuranceUnderScheme) {
        this.cbmInsuranceUnderScheme = cbmInsuranceUnderScheme;
    }
    
    /**
     * Getter for property tblInsurance.
     * @return Value of property tblInsurance.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblInsurance() {
        return tblInsurance;
    }
    
    /**
     * Setter for property tblInsurance.
     * @param tblInsurance New value of property tblInsurance.
     */
    public void setTblInsurance(com.see.truetransact.clientutil.EnhancedTableModel tblInsurance) {
        this.tblInsurance = tblInsurance;
    }
    
    /**
     * Getter for property insuranceModel.
     * @return Value of property insuranceModel.
     */
    public com.see.truetransact.clientutil.TableUtil getInsuranceModel() {
        return insuranceModel;
    }
    
    /**
     * Setter for property insuranceModel.
     * @param insuranceModel New value of property insuranceModel.
     */
    public void setInsuranceModel(com.see.truetransact.clientutil.TableUtil insuranceModel) {
        this.insuranceModel = insuranceModel;
    }
    
    /**
     * Getter for property allInsuranceList.
     * @return Value of property allInsuranceList.
     */
    public java.util.ArrayList getAllInsuranceList() {
        return allInsuranceList;
    }
    
    /**
     * Setter for property allInsuranceList.
     * @param allInsuranceList New value of property allInsuranceList.
     */
    public void setAllInsuranceList(java.util.ArrayList allInsuranceList) {
        this.allInsuranceList = allInsuranceList;
    }
    
    /**
     * Getter for property allInsuranceMap.
     * @return Value of property allInsuranceMap.
     */
    public java.util.LinkedHashMap getAllInsuranceMap() {
        return allInsuranceMap;
    }
    
    /**
     * Setter for property allInsuranceMap.
     * @param allInsuranceMap New value of property allInsuranceMap.
     */
    public void setAllInsuranceMap(java.util.LinkedHashMap allInsuranceMap) {
        this.allInsuranceMap = allInsuranceMap;
    }
    
    /**
     * Getter for property txtInsurancePremiumDebit.
     * @return Value of property txtInsurancePremiumDebit.
     */
    public java.lang.String getTxtInsurancePremiumDebit() {
        return txtInsurancePremiumDebit;
    }
    
    /**
     * Setter for property txtInsurancePremiumDebit.
     * @param txtInsurancePremiumDebit New value of property txtInsurancePremiumDebit.
     */
    public void setTxtInsurancePremiumDebit(java.lang.String txtInsurancePremiumDebit) {
        this.txtInsurancePremiumDebit = txtInsurancePremiumDebit;
    }
    
    /**
     * Getter for property txtInsuranceAmt.
     * @return Value of property txtInsuranceAmt.
     */
    public java.lang.String getTxtInsuranceAmt() {
        return txtInsuranceAmt;
    }
    
    /**
     * Setter for property txtInsuranceAmt.
     * @param txtInsuranceAmt New value of property txtInsuranceAmt.
     */
    public void setTxtInsuranceAmt(java.lang.String txtInsuranceAmt) {
        this.txtInsuranceAmt = txtInsuranceAmt;
    }
    
    /**
     * Getter for property txtPenalApplicableAmt.
     * @return Value of property txtPenalApplicableAmt.
     */
    public java.lang.String getTxtPenalApplicableAmt() {
        return txtPenalApplicableAmt;
    }
    
    /**
     * Setter for property txtPenalApplicableAmt.
     * @param txtPenalApplicableAmt New value of property txtPenalApplicableAmt.
     */
    public void setTxtPenalApplicableAmt(java.lang.String txtPenalApplicableAmt) {
        this.txtPenalApplicableAmt = txtPenalApplicableAmt;
    }
    
    /**
     * Getter for property txtReviewPeriod.
     * @return Value of property txtReviewPeriod.
     */
    public java.lang.String getTxtReviewPeriod() {
        return txtReviewPeriod;
    }
    
    /**
     * Setter for property txtReviewPeriod.
     * @param txtReviewPeriod New value of property txtReviewPeriod.
     */
    public void setTxtReviewPeriod(java.lang.String txtReviewPeriod) {
        this.txtReviewPeriod = txtReviewPeriod;
    }
    
    /**
     * Getter for property cbmReviewPeriod.
     * @return Value of property cbmReviewPeriod.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmReviewPeriod() {
        return cbmReviewPeriod;
    }
    
    /**
     * Setter for property cbmReviewPeriod.
     * @param cbmReviewPeriod New value of property cbmReviewPeriod.
     */
    public void setCbmReviewPeriod(com.see.truetransact.clientutil.ComboBoxModel cbmReviewPeriod) {
        this.cbmReviewPeriod = cbmReviewPeriod;
    }
    
    /**
     * Getter for property cboReviewPeriod.
     * @return Value of property cboReviewPeriod.
     */
    public java.lang.String getCboReviewPeriod() {
        return cboReviewPeriod;
    }
    
    /**
     * Setter for property cboReviewPeriod.
     * @param cboReviewPeriod New value of property cboReviewPeriod.
     */
    public void setCboReviewPeriod(java.lang.String cboReviewPeriod) {
        this.cboReviewPeriod = cboReviewPeriod;
    }
    
    /**
     * Getter for property txtElgLoanAmt.
     * @return Value of property txtElgLoanAmt.
     */
    public java.lang.String getTxtElgLoanAmt() {
        return txtElgLoanAmt;
    }
    
    /**
     * Setter for property txtElgLoanAmt.
     * @param txtElgLoanAmt New value of property txtElgLoanAmt.
     */
    public void setTxtElgLoanAmt(java.lang.String txtElgLoanAmt) {
        this.txtElgLoanAmt = txtElgLoanAmt;
    }
    
}
