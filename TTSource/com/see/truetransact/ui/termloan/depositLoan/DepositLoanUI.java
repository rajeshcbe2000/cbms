/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * GoldLoanUI.java
 *
 * Created on November 28, 2003, 3:55 PM
 */

package com.see.truetransact.ui.termloan.depositLoan;




import java.awt.*;
import java.util.*;
import javax.swing.*;
//import java.util.Date;
import java.util.List;
//import java.util.HashMap;
import javax.swing.table.*;
//import java.util.ArrayList;
//import java.util.Observable;
//import org.apache.log4j.Logger;
import com.see.truetransact.ui.common.nominee.*;
import com.see.truetransact.ui.customer.CustomerOB;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.customer.IndividualCustUI;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
//import com.see.truetransact.uivalidation.DefaultValidation;
import com.see.truetransact.uivalidation.NumericValidation;
//import com.see.truetransact.ui.termloan.emicalculator.TermLoanInstallmentUI;
//import com.see.truetransact.ui.termloan.customerDetailsScreen.CustomerDetailsScreenUI;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;

import com.see.truetransact.ui.TrueTransactMain;

import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.CMandatoryDialog;
//import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
//import com.see.truetransact.transferobject.termloan.TermLoanSanctionFacilityTO;
import com.see.truetransact.ui.common.powerofattorney.PowerOfAttorneyUI;
import com.see.truetransact.ui.termloan.loandisbursement.LoanDisbursementUI;
import com.see.truetransact.ui.termloan.loandisbursement.LoanDisbursementOB;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.PercentageValidation;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;
import com.see.truetransact.uivalidation.PhoneNoValidation;
import java.text.DecimalFormat;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListDebitUI;
import com.see.truetransact.uicomponent.CTable;
import com.see.truetransact.ui.common.viewall.*;
import com.see.truetransact.ui.termloan.emicalculator.TermLoanInstallmentUI;
import com.see.truetransact.uivalidation.ToDateValidation;
//import com.see.truetransact.transferobject.termloan.TermLoanInterestTO;

/*
 *
 * @author  Sathiya
 * Created on September 13, 2010, 3:55 PM
 *
 */

public class DepositLoanUI extends CInternalFrame implements java.util.Observer, UIMandatoryField {
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.depositLoan.DepositLoanRB", ProxyParameters.LANGUAGE);
    //    TermLoanRB resourceBundle = new TermLoanRB();
    //    AuthorizedSignatoryUI authSignUI = null;
    PowerOfAttorneyUI poaUI = null;
    DepositLoanOB observable;
    //    GoldLoanBorrowerOB observableBorrow;
    //    TermLoanCompanyOB observableComp;
    //    GoldLoanSecurityOB observableSecurity;
    //    GoldLoanRepaymentOB observableRepay;
    //    TermLoanGuarantorOB observableGuarantor;
    //    TermLoanDocumentDetailsOB observableDocument;
    //    GoldLoanInterestOB observableInt;
    //    GoldLoanClassificationOB observableClassi;
    //    TermLoanOtherDetailsOB observableOtherDetails;
    //    TermLoanAdditionalSanctionOB observableAdditionalSanctionOB;
    DepositLoanRepaymentOB observableRepay;
    LoanDisbursementUI loanDisbursementUI = null;
    LoanDisbursementOB loanDisbursementOB = null;
    //    CustomerDetailsScreenUI customerDetailsScreenUI = null;
    private Date date;
    private Date repayFromdate;
    private JTable table = null;
    private HashMap mandatoryMap;
    //    private static TermLoanDocumentDetailsOB termLoanDocumentDetailsOB;
    IndividualCustUI individualCustUI;
    CustomerOB customerOB;
    private boolean updateModeAuthorize = false;
    private boolean updateModePoA = false;
    private boolean updateSanctionFacility = false;
    private boolean updateSanctionMain = false;
    private boolean updateSecurity = false;
    private boolean updateRepayment = false;
    private boolean repayNewMode    = false;
    private boolean allowMultiRepay = true;
    private boolean updateGuarantor = false;
    private boolean updateDocument = false;
    private boolean updateInterest  = false;
    private boolean alreadyChecked  =true;
    private boolean isFilled = false;
    private boolean sanMousePress=false;
    private boolean sanValueChanged=false;
    private boolean sanDetailMousePressedForLTD = false;
    private boolean additionalSanMousePress =false;
    private boolean facilityFlag=true;
    private boolean outStandingAmtRepayment=false;
    private boolean existRecord=false;
    private boolean repayMorotoruimavailable=false;
    private HashMap accNumMap=new HashMap();
    private Date curr_dt=null;
    //charge details
    private boolean selectMode = false;//charge details, calculating total of selected amount
    public String prodDesc1 ="";
    //charge end..
    int result;
    int modeAuthorize = -1;
    int modePoA       = -1;
    int rowSanctionFacility     = -1;
    int rowSanctionMain         = -1;
    int rowFacilityTabSanction  = -1;
    int rowFacilityTabFacility  = -1;
    int rowSecurity  = -1;
    int rowRepayment = -1;
    int dumRowRepay  = -1;
    int rowGuarantor = -1;
    int rowDocument = -1;
    int rowInterest  = -1;
    int transCount=0;
    boolean sanction=false;
    boolean sandetail=false;
    boolean santab=false;
    boolean sanfacTab=false;
    int rowmaintab=-1;
    int rowfactab=-1;
    int rowsan=-1;
    int rowsanDetail=-1;
    boolean multipleDeposit=false;
    boolean newRecord=false;
    String accountClosingCharge=null;
    final int DELETE = 1, AUTHORIZED = 2;
    //    private final static Logger log = Logger.getLogger(GoldLoanUI.class);
    private final        String ACT = "ACT";
    private final        String AUTHORIZE = "AUTHORIZE";
    //    private final        String CORPORATE = "CORPORATE";
    //    private final        String FLOATING_RATE = "FLOATING_RATE";
    //    private final        String FIXED_RATE = "FIXED_RATE";
    //    private final        String INDIVIDUAL = "INDIVIDUAL";
    //    private final        String IS_COOPERATIVE = "IS_COOPERATIVE";
    private final        String JOINT_ACCOUNT = "JOINT_ACCOUNT";
    private final        String LOANS_AGAINST_DEPOSITS = "LOANS_AGAINST_DEPOSITS";
    private final        String REJECT = "REJECT";
    private final        String EXCEPTION = "EXCEPTION";
    private final        String PROD = "PROD";
    
    private String viewType = "";
    private String loanType = "OTHERS";
    private boolean facilitySaved = false;
    private boolean btnNewPressed = false;
    private int  addSanctionPosition=-1;
    private boolean allowResetVisit=false;
    private boolean deleteInstallment=false;
    private boolean enableControls = false;
    private boolean updateRecords = false;
    private String customerScreen = "TERMLOAN_SCREEN";
    private boolean notSavedRecords = false;
    private boolean mousePressedRec = false;
    private boolean tableFlag = false;
    private String eachProdId = "";
    private double totMarginAmt = 0;
    private double totEligibleAmt = 0;
    private double eligibleAmt=0;
    private double totSecurityValue = 0;
    private int selectedRow = -1;
    private boolean newRecordAdding = false;
    private String totalSecValue = "";
    private String totalMarginValue = "";
    private String totalEligibleValue = "";
    private boolean finalChecking = false;
    public String branchID;
    public String prodDesc ="";
    HashMap map;
    private ArrayList selectedNomineeData;
    private List chargelst = null;
    final String SCREEN = "TL";
    String minNominee = "";
    private boolean transNew =true;
    Rounding rd = new Rounding();
    double perGramAmt = 0;
    NomineeOB nomineeOB;
    boolean fromAuthorizeUI = false;
    boolean fromManagerAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
    AuthorizeListDebitUI ManagerauthorizeListUI=null;
    private int rejectFlag=0;
    double eligblLoanAmt = 0.0;
    private Date currDt = null;
    private AcctSearchUI acctsearch = null;
    boolean chktrans = false;
    boolean chkok=false;
    String loannaration="";
    private Date repaymentDate = null;
    public boolean loanRenewal=false;
    private String suspenseActNum=null;
    public static double oldloanAmt=0;
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;
    public HashMap serviceTaxApplMap = new HashMap();
    public HashMap serviceTaxIdMap = new HashMap();
    HashMap serviceTax_Map=new HashMap();
    ServiceTaxCalculation objServiceTax;
    
    /**
     * Declare a new instance of the NomineeUI and IntroducerUI...
     */
    NomineeUI nomineeUi = new NomineeUI(SCREEN, false);
    
    
    /** Creates new form TermLoanUI */
    public DepositLoanUI() {
        termLoanUI();
    }
    
    public DepositLoanUI(String loanType) {
        this.loanType = loanType;
        termLoanUI();
    }
    
    public DepositLoanUI(String loanType, String ProdId) {
        this.loanType = loanType;
        this.eachProdId = ProdId;
        termLoanUI();
    }
    
    private void termLoanUI(){
        initComponents();
        setFieldNames();
        internationalize();
        setMaxLength();
        setObservable();
        branchID = TrueTransactMain.BRANCH_ID;
        nomineeUi.disableNewButton(false);
        loanDisbursementUI = new LoanDisbursementUI();
        ClientUtil.enableDisable(this, false);
        allEnableDisable();
        initComponentData();
        setThriftEnabled(false);
        setMandatoryHashMap();
        setHelpMessage();
        btnDelete.setEnabled(true);
         curr_dt=ClientUtil.getCurrentDate();
        btnCustomer(false);
        lblAdditionalLoanfacility.setVisible(false);
        panAdditionalLoanFacility.setVisible(false);
         btnNew_Borrower.setVisible(false);
        setAllBorrowerBtnsEnableDisable(false);
        btnDepositNo.setEnabled(false);
        
        panMDSLoanDetailFields.setVisible(false);
        panPaddyLoanDetailFields.setVisible(false);
        panCustIdDetails.setVisible(false);
        currDt=ClientUtil.getCurrentDate();
    }
    private void btnCustomerID_GDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerID_GDActionPerformed
        // Add your handling code here:
        popUp("Guarant_Cust_Id");
    }//GEN-LAST:event_btnCustomerID_GDActionPerformed
    private void btnCustomer(boolean val){
        btnDepositNo.setEnabled(val);
    }
    private void cboProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeActionPerformed
        // TODO add your handling code here:
        //        cboProdTypeActionPerformed();
    }//GEN-LAST:event_cboProdTypeActionPerformed
    
    private void cboProdIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdIdActionPerformed
        // TODO add your handling code here:
        //        cboProdIdActionPerformed();
    }//GEN-LAST:event_cboProdIdActionPerformed
    
    private void btnAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccNoActionPerformed
        // TODO add your handling code here:
        //        btnAccNoActionPerformed();
    }//GEN-LAST:event_btnAccNoActionPerformed
    
    private void EMI_CalculateActionPerformed() {
        //        if(tblInterMaintenance.getRowCount() > 0){
        //            updateOBFields();
        //                calculateRepaymentToDate();
        //            if(observableRepay.getRepaymentisActive())
        //                deleteInstallment=true;
        //            if(!existRecord)
        //                if(checkRepaymentType(true))
        //                    return;
        HashMap repayData = new HashMap();
        //            HashMap prodLevelValues = observable.getCompFreqRoundOffValues();
        //            repayData.put("ACT_NO", lblAccNo_RS_2.getText());
        //            if(repayNewMode){
        repayData.put("NEW_INSTALLMENT", "NEW_INSTALLMENT");
        //            }
        
        //            repayData.put("FROM_DATE", tdtFirstInstall.getDateValue());
        
        //Regarding outstanding amount based on repayment schedule
        //            if(outStandingAmtRepayment){
        //                //                if(date !=null)
        //                //                    repayData.put("FROM_DATE", DateUtil.getStringDate(date));
        //                //                else
        //                // If OPERATE_MODE is IMPLEMENTATION firstInstallment date may be older date
        //                if (!CommonUtil.convertObjToStr(CommonConstants.OPERATE_MODE).equals(CommonConstants.IMPLEMENTATION)) {
        //                    if(DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(tdtFirstInstall.getDateValue()),ClientUtil.getCurrentDate())>0)
        //                    {
        //                        ClientUtil.showMessageWindow("First Installment Date should be greater than or equal to currDate");
        //                        return;
        //                    }
        //                }
        //                repayData.put("FROM_DATE", repayFromdate);//tdtFirstInstall.getDateValue()));
        //                repayData.put("REPAYMENT_START_DT", DateUtil.getDateMMDDYYYY(tdtFirstInstall.getDateValue()));
        //
        //                //            if (txtNoInstallments.getText().equals("1"))
        //                //                repayData.put("FROM_DATE", tdtFromDate.getDateValue());
        //                repayData.put("TO_DATE", DateUtil.getDateMMDDYYYY(tdtLastInstall.getDateValue()));
        //                repayData.put("NO_INSTALL", txtNoInstall.getText());
        //                repayData.put("ISDURATION_DDMMYY", "YES");
        //                //            if (rdoInterest_Compound.isSelected()){
        //                repayData.put("INTEREST_TYPE", "COMPOUND");
        //                //            }else if (rdoInterest_Simple.isSelected()){
        //                //                repayData.put("INTEREST_TYPE", "SIMPLE");
        //                //            }
        //                repayData.put("DURATION_YY", txtNoInstall.getText());
        //                //            date=null;
        //                repayData.put("COMPOUNDING_PERIOD",  observableRepay.getCbmRepayFreq_Repayment().getKeyForSelected());//CommonUtil.convertObjToStr(prodLevelValues.get("DEBITINT_COMP_FREQ")));
        //                if (cboRepayType.getSelectedItem().equals("User Defined")){
        //                    repayData.put("REPAYMENT_TYPE", observableRepay.getCbmRepayType().getKeyForSelected());
        //                }else{
        //                    repayData.put("REPAYMENT_TYPE", observableRepay.getCbmRepayType().getKeyForSelected());
        //                }
        //                //            repayData.put("EMI_TYPE","UNIFORM_EMI");
        //                repayData.put("PRINCIPAL_AMOUNT", txtLimit_SD.getText());
        //                repayData.put("ROUNDING_FACTOR", "1_RUPEE");
        //                repayData.put("ROUNDING_TYPE", CommonUtil.convertObjToStr(prodLevelValues.get("DEBIT_INT_ROUNDOFF")));
        //                repayData.put("REPAYMENT_FREQUENCY",  observableRepay.getCbmRepayFreq_Repayment().getKeyForSelected());
        //            }else{
        //                if(repayFromdate !=null)
        //                    repayData.put("FROM_DATE", DateUtil.getDateMMDDYYYY(DateUtil.getStringDate(repayFromdate)));
        //                else if(loanType.equals("LTD"))
        //        repayData.put("FROM_DATE", DateUtil.getDateMMDDYYYY(tdtSanctionDate.getDateValue()));
        //        //                else
        //        //                    repayData.put("FROM_DATE", DateUtil.getDateMMDDYYYY(tdtFacility_Repay_Date.getDateValue()));
        //        //            if (txtNoInstallments.getText().equals("1"))
        //        //                repayData.put("FROM_DATE", tdtFromDate.getDateValue());
        //        repayData.put("TO_DATE", DateUtil.getDateMMDDYYYY(tdtSanctionDate.getDateValue()));
        //        repayData.put("REPAYMENT_START_DT",DateUtil.getDateMMDDYYYY(tdtSanctionDate.getDateValue()));
        //        repayData.put("NO_INSTALL", txtNoInstallments.getText());
        //        repayData.put("ISDURATION_DDMMYY", "YES");
        //        //            if (rdoInterest_Compound.isSelected()){
        //        repayData.put("INTEREST_TYPE", "COMPOUND");
        //            }else if (rdoInterest_Simple.isSelected()){
        //                repayData.put("INTEREST_TYPE", "SIMPLE");
        //            }
        
        
        //            }
        //            if(tblRepaymentCTable.getRowCount()>0 && tblRepaymentCTable.getSelectedRow() !=-1)//purpose existing installment taking from oracle table
        //             repayData.put("SCHEDULE_ID", tblRepaymentCTable.getValueAt(tblRepaymentCTable.getSelectedRow(),0));
        
        //
        //            java.util.ArrayList interestList= new ArrayList();
        //            if(loanType.equals("LTD"))
        //                interestList = observableInt.getInterestDetails(tdtFDate.getDateValue(), tdtTDate.getDateValue(), DateUtil.getDateMMDDYYYY(tdtTDate.getDateValue()),DateUtil.getDateMMDDYYYY(tdtFirstInstall.getDateValue()));
        //            else  if (txtNoInstallments.getText().equals("1"))
        //                interestList = observableInt.getInterestDetails(tdtFDate.getDateValue(), tdtTDate.getDateValue(), DateUtil.getDateMMDDYYYY(tdtTDate.getDateValue()),DateUtil.getDateMMDDYYYY(tdtFirstInstall.getDateValue()));
        //            else
        //                interestList = observableInt.getInterestDetails(tdtFacility_Repay_Date.getDateValue(), tdtTDate.getDateValue(), DateUtil.getDateMMDDYYYY(tdtTDate.getDateValue()),DateUtil.getDateMMDDYYYY(tdtFirstInstall.getDateValue()));
        //            if (interestList !=null && interestList.size() > 0){
        //                repayData.put("VARIOUS_INTEREST_RATE", interestList);
        //                ArrayList deletedRepaySchNosList = observableRepay.getDeletedRepaymentScheduleNos();
        
        //                StringBuffer deletedScheduleNos = new StringBuffer();
        //                int delCount = deletedRepaySchNosList.size();
        //                if(delCount!=0) {
        //                    for(int i=0; i<delCount; i++){
        //                        if(i==0 || i==delCount){
        //                            deletedScheduleNos.append(CommonUtil.convertObjToDouble(deletedRepaySchNosList.get(i)));
        //                        } else{
        //                            deletedScheduleNos.append("," + CommonUtil.convertObjToDouble(deletedRepaySchNosList.get(i)));
        //                        }
        //                    }
        //                    repayData.put("DELETEDSCHEDULES", deletedScheduleNos);
        //                }
        //                deletedRepaySchNosList = null;
        //                if(updateRepayment){
        //                    showExistingInstallment(repayData);
        //                    return;
        //                }
        //                int installmentCount = CommonUtil.convertObjToInt(ClientUtil.executeQuery("getCountOfInstallments",repayData).get(0));
        //                int installmentCountTL = CommonUtil.convertObjToInt(ClientUtil.executeQuery("getCountOfInstallmentsTL",repayData).get(0));
        //                if (installmentCount>0  || installmentCountTL==0 || deleteInstallment || repayNewMode){
        //                    new TermLoanInstallmentUI(this, repayData);
        //                }else{
        try {
            //        new TermLoanInstallmentUI(this, repayData,false);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        //        observableRepay.addRepaymentDetails(false);
        //                    objTermLoanInstallmentUI.e
        //                    observable.setInstallmentAllMap(repayData);
        //                    observableRepay.setRepaymentAll((LinkedHashMap)((HashMap)repayData).get("ALL_RECORDS"));
        //                }
        //            }else{
        //                displayAlert(resourceBundle.getString("interestDetailsWarning"));
        //            }
        //            repayData = null;
        //        }else{
        //            displayAlert(resourceBundle.getString("interestDetailsWarning"));
        //        }
    }
    private void tdtAsOn_GDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtAsOn_GDFocusLost
        // Add your handling code here:
        
    }//GEN-LAST:event_tdtAsOn_GDFocusLost
    
    
    
    private void allEnableDisable(){
        setBorrowerDetailsEnableDisable(false);
        setButtonEnableDisable();
        setAllBorrowerBtnsEnableDisable(false);
        setAllTablesEnableDisable(true);
        setAllTableBtnsEnableDisable(false); // To disable the Tool buttons Authorized Signatory
        setAllSanctionFacilityEnableDisable(false);
        setAllSanctionMainEnableDisable(false);
        setAllFacilityDetailsEnableDisable(false);
        setAllSecurityDetailsEnableDisable(false);
        setAllRepaymentBtnsEnableDisable(false);
        setAllGuarantorBtnsEnableDisable(false);
        setAllDocumentDetailsEnableDisable(false);
        setDocumentToolBtnEnableDisable(false);
        setAllInterestDetailsEnableDisable(false);
        setAllInterestBtnsEnableDisable(false);
        setAllSettlmentEnableDisable(false);
        setAllClassificationDetailsEnableDisable(false);
        disableFields();
    }
    
    private void disableFields(){
        //        txtCustID.setEditable(true);
        
    }
    
    private void setObservable(){
        try {
                       observable = DepositLoanOB.getInstance(); 
                       //Changed By Suresh
            //            observableBorrow = GoldLoanBorrowerOB.getInstance();
            //            observableComp = TermLoanCompanyOB.getInstance();
            //            observableSecurity = GoldLoanSecurityOB.getInstance();
            //            observableInt = GoldLoanInterestOB.getInstance();
            //            observableGuarantor = TermLoanGuarantorOB.getInstance();
            //            observableOtherDetails = TermLoanOtherDetailsOB.getInstance();
            //            observableDocument = TermLoanDocumentDetailsOB.getInstance();
            //            //            observableClassi = GoldLoanClassificationOB.getInstance();
                        observable = new DepositLoanOB();
                        observable.addObserver(this);
                        observableRepay = observable.getTermLoanRepaymentOB();
                        observableRepay.addObserver(this);
            //             observable.setLoanType(loanType);
            //            observableBorrow = observable.getTermLoanBorrowerOB();
            //            observableBorrow.addObserver(this);
            ////            observableComp = observable.getTermLoanCompanyOB();
            ////            observableComp.addObserver(this);
            //            observableSecurity = observable.getTermLoanSecurityOB();
            //            observableSecurity.addObserver(this);
            //            observableRepay = observable.getTermLoanRepaymentOB();
            //            observableRepay.addObserver(this);
            //            observableInt = observable.getTermLoanInterestOB();
            //            observableInt.addObserver(this);
            ////            observableGuarantor = observable.getTermLoanGuarantorOB();
            ////            observableGuarantor.addObserver(this);
            //            observableOtherDetails = observable.getTermLoanOtherDetailsOB();
            //            observableOtherDetails.addObserver(this);
            //            observableDocument = observable.getTermLoanDocumentDetailsOB();
            //            observableDocument.addObserver(this);
            //            observableClassi = observable.getTermLoanClassificationOB();
            //            observableClassi.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
            //            log.info("TermLoanOB..."+e);
        }
        
        //        if (loanType.equals("OTHERS")) {
        //        }
        //        observableAdditionalSanctionOB=TermLoanAdditionalSanctionOB.getInstance();
        //        observableAdditionalSanctionOB.addObserver(this);
        //         loanDisbursementOB=LoanDisbursementOB.getInstance();
        //         loanDisbursementOB.addObserver(this);
        
    }
    
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtAcct_Name", new Boolean(true));
        mandatoryMap.put("txtRepayScheduleMode", new Boolean(true));
        mandatoryMap.put("tdtExecuteDate_DOC", new Boolean(true));
        mandatoryMap.put("tdtExpiryDate_DOC", new Boolean(true));
        mandatoryMap.put("tdtDOB_GD", new Boolean(true));
        mandatoryMap.put("tdtDisbursement_Dt", new Boolean(true));
        mandatoryMap.put("cboProdId", new Boolean(true));
        mandatoryMap.put("cboProdType", new Boolean(true));
        mandatoryMap.put("cboIntGetFrom", new Boolean(true));
        mandatoryMap.put("txtPoANo", new Boolean(true));
        mandatoryMap.put("cboRepayFreq_Repayment", new Boolean(true));
        mandatoryMap.put("cboAddrType_PoA", new Boolean(true));
        mandatoryMap.put("txtCustID", new Boolean(true));
        mandatoryMap.put("cboAccStatus", new Boolean(true));
        mandatoryMap.put("cboConstitution", new Boolean(true));
        mandatoryMap.put("cboCategory", new Boolean(true));
        mandatoryMap.put("txtReferences", new Boolean(true));
        mandatoryMap.put("txtCompanyRegisNo", new Boolean(true));
        mandatoryMap.put("tdtDateEstablishment", new Boolean(true));
        mandatoryMap.put("tdtDealingWithBankSince", new Boolean(true));
        mandatoryMap.put("txtRiskRating", new Boolean(true));
        mandatoryMap.put("cboNatureBusiness", new Boolean(true));
        mandatoryMap.put("txtRemarks__CompDetail", new Boolean(true));
        mandatoryMap.put("txtNetWorth", new Boolean(true));
        mandatoryMap.put("tdtAsOn", new Boolean(true));
        mandatoryMap.put("tdtCreditFacilityAvailSince", new Boolean(true));
        mandatoryMap.put("txtChiefExecutiveName", new Boolean(true));
        mandatoryMap.put("cboAddressType", new Boolean(true));
        mandatoryMap.put("txtStreet_CompDetail", new Boolean(true));
        mandatoryMap.put("txtArea_CompDetail", new Boolean(true));
        mandatoryMap.put("cboCity_CompDetail", new Boolean(true));
        mandatoryMap.put("cboState_CompDetail", new Boolean(true));
        mandatoryMap.put("cboCountry_CompDetail", new Boolean(true));
        mandatoryMap.put("txtPin_CompDetail", new Boolean(true));
        mandatoryMap.put("txtPhone_CompDetail", new Boolean(true));
        mandatoryMap.put("txtCustomerID", new Boolean(true));
        mandatoryMap.put("txtLimits", new Boolean(true));
        mandatoryMap.put("txtSanctionSlNo", new Boolean(true));
        mandatoryMap.put("tdtSanctionDate", new Boolean(true));
        mandatoryMap.put("cboSanctioningAuthority", new Boolean(true));
        mandatoryMap.put("txtSanctionRemarks", new Boolean(true));
        mandatoryMap.put("txtNoInstallments", new Boolean(true));
        mandatoryMap.put("cboRepayFreq", new Boolean(true));
        mandatoryMap.put("cboTypeOfFacility", new Boolean(true));
        mandatoryMap.put("txtLimit_SD", new Boolean(true));
        mandatoryMap.put("txtLimit_SD2", new Boolean(true));
        mandatoryMap.put("tdtFDate", new Boolean(true));
        mandatoryMap.put("tdtTDate", new Boolean(true));
        mandatoryMap.put("cboSanctionSlNo", new Boolean(true));
        mandatoryMap.put("cboProductId", new Boolean(true));
        mandatoryMap.put("rdoSecurityDetails_Unsec", new Boolean(true));
        mandatoryMap.put("chkStockInspect", new Boolean(true));
        mandatoryMap.put("chkInsurance", new Boolean(true));
        mandatoryMap.put("chkGurantor", new Boolean(true));
        mandatoryMap.put("rdoAccType_New", new Boolean(true));
        mandatoryMap.put("rdoAccLimit_Main", new Boolean(true));
        mandatoryMap.put("rdoNatureInterest_PLR", new Boolean(true));
        mandatoryMap.put("cboInterestType", new Boolean(true));
        mandatoryMap.put("rdoRiskWeight_Yes", new Boolean(true));
        mandatoryMap.put("tdtDemandPromNoteDate", new Boolean(true));
        mandatoryMap.put("tdtDemandPromNoteExpDate", new Boolean(true));
        mandatoryMap.put("tdtAODDate", new Boolean(true));
        mandatoryMap.put("rdoMultiDisburseAllow_Yes", new Boolean(true));
        mandatoryMap.put("rdoSubsidy_Yes", new Boolean(true));
        mandatoryMap.put("txtPurposeDesc", new Boolean(true));
        mandatoryMap.put("txtGroupDesc", new Boolean(true));
        mandatoryMap.put("rdoInterest_Simple", new Boolean(true));
        mandatoryMap.put("rdoRecarable_Yes", new Boolean(true));
        mandatoryMap.put("txtContactPerson", new Boolean(true));
        mandatoryMap.put("txtContactPhone", new Boolean(true));
        mandatoryMap.put("txtRemarks", new Boolean(true));
        mandatoryMap.put("txtSecurityNo", new Boolean(true));
        mandatoryMap.put("txtCustID_Security", new Boolean(true));
        mandatoryMap.put("rdoSecurityType_Primary", new Boolean(true));
        mandatoryMap.put("txtSecurityValue", new Boolean(true));
        mandatoryMap.put("txtParticulars", new Boolean(true));
        mandatoryMap.put("cboNatureCharge", new Boolean(true));
        mandatoryMap.put("tdtDateCharge", new Boolean(true));
        mandatoryMap.put("chkSelCommodityItem", new Boolean(true));
        mandatoryMap.put("cboForMillIndus", new Boolean(true));
        mandatoryMap.put("tdtDateInspection", new Boolean(true));
        mandatoryMap.put("cboStockStateFreq", new Boolean(true));
        mandatoryMap.put("tdtFromDate", new Boolean(true));
        mandatoryMap.put("tdtToDate", new Boolean(true));
        mandatoryMap.put("txtMargin", new Boolean(true));
        mandatoryMap.put("txtScheduleNo", new Boolean(true));
        mandatoryMap.put("txtLaonAmt", new Boolean(true));
        mandatoryMap.put("cboRepayFreq_Repayment", new Boolean(true));
        mandatoryMap.put("cboRepayType", new Boolean(true));
        mandatoryMap.put("chkMoraGiven", new Boolean(true));
        mandatoryMap.put("chkMoratorium_Given", new Boolean(true));
        mandatoryMap.put("txtNoMonthsMora", new Boolean(true));
        mandatoryMap.put("txtFacility_Moratorium_Period", new Boolean(true));
        mandatoryMap.put("tdtFacility_Repay_Date", new Boolean(true));
        mandatoryMap.put("tdtFirstInstall", new Boolean(true));
        mandatoryMap.put("tdtLastInstall", new Boolean(true));
        mandatoryMap.put("txtTotalBaseAmt", new Boolean(true));
        mandatoryMap.put("txtAmtPenulInstall", new Boolean(true));
        mandatoryMap.put("txtAmtLastInstall", new Boolean(true));
        mandatoryMap.put("txtTotalInstallAmt", new Boolean(true));
        mandatoryMap.put("rdoDoAddSIs_Yes", new Boolean(true));
        mandatoryMap.put("rdoPostDatedCheque_Yes", new Boolean(true));
        mandatoryMap.put("rdoStatus_Repayment", new Boolean(true));
        mandatoryMap.put("txtNoInstall", new Boolean(true));
        mandatoryMap.put("txtCustomerID_GD", new Boolean(true));
        mandatoryMap.put("txtGuaranAccNo", new Boolean(true));
        mandatoryMap.put("txtGuaranName", new Boolean(true));
        mandatoryMap.put("txtAge", new Boolean(true));
        mandatoryMap.put("txtStreet_GD", new Boolean(true));
        mandatoryMap.put("txtArea_GD", new Boolean(true));
        mandatoryMap.put("cboCity_GD", new Boolean(true));
        mandatoryMap.put("txtPin_GD", new Boolean(true));
        mandatoryMap.put("cboState_GD", new Boolean(true));
        mandatoryMap.put("cboCountry_GD", new Boolean(true));
        mandatoryMap.put("txtPhone_GD", new Boolean(true));
        mandatoryMap.put("cboConstitution_GD", new Boolean(true));
        mandatoryMap.put("txtGuarantorNetWorth", new Boolean(true));
        mandatoryMap.put("tdtAsOn_GD", new Boolean(true));
        mandatoryMap.put("tdtAccountOpenDate",new Boolean(true));
        mandatoryMap.put("txtInsureCompany", new Boolean(true));
        mandatoryMap.put("txtPolicyNumber", new Boolean(true));
        mandatoryMap.put("txtPolicyAmt", new Boolean(true));
        mandatoryMap.put("tdtPolicyDate", new Boolean(true));
        mandatoryMap.put("txtPremiumAmt", new Boolean(true));
        mandatoryMap.put("tdtExpityDate", new Boolean(true));
        mandatoryMap.put("cboNatureRisk", new Boolean(true));
        mandatoryMap.put("tdtFrom", new Boolean(true));
        mandatoryMap.put("tdtTo", new Boolean(true));
        mandatoryMap.put("txtFromAmt", new Boolean(true));
        mandatoryMap.put("txtToAmt", new Boolean(true));
        mandatoryMap.put("txtInter", new Boolean(true));
        mandatoryMap.put("txtPenalInter", new Boolean(true));
        mandatoryMap.put("txtAgainstClearingInter", new Boolean(true));
        mandatoryMap.put("txtPenalStatement", new Boolean(true));
        mandatoryMap.put("txtInterExpLimit", new Boolean(true));
        mandatoryMap.put("cboCommodityCode", new Boolean(true));
        mandatoryMap.put("cboGuaranteeCoverCode", new Boolean(true));
        mandatoryMap.put("cboSectorCode1", new Boolean(true));
        mandatoryMap.put("cboHealthCode", new Boolean(true));
        mandatoryMap.put("cboTypeFacility", new Boolean(true));
        mandatoryMap.put("cboDistrictCode", new Boolean(true));
        mandatoryMap.put("cboPurposeCode", new Boolean(true));
        mandatoryMap.put("cboSectorCode2", new Boolean(true));
        mandatoryMap.put("cboIndusCode", new Boolean(true));
        mandatoryMap.put("cboRepaymentCode", new Boolean(true));
        mandatoryMap.put("cbo20Code", new Boolean(true));
        mandatoryMap.put("cboRefinancingInsti", new Boolean(true));
        mandatoryMap.put("cboGovtSchemeCode", new Boolean(true));
        mandatoryMap.put("cboAssetCode", new Boolean(true));
        mandatoryMap.put("tdtNPADate", new Boolean(true));
        mandatoryMap.put("chkDirectFinance", new Boolean(true));
        mandatoryMap.put("chkECGC", new Boolean(true));
        mandatoryMap.put("chkPrioritySector", new Boolean(true));
        mandatoryMap.put("chkDocumentcomplete", new Boolean(true));
        mandatoryMap.put("chkQIS", new Boolean(true));
        mandatoryMap.put("cboSecurityNo_Insurance", new Boolean(true));
        mandatoryMap.put("txtRemark_Insurance", new Boolean(true));
        mandatoryMap.put("txtRemarks_DocumentDetails", new Boolean(true));
        mandatoryMap.put("tdtSubmitDate_DocumentDetails", new Boolean(true));
        mandatoryMap.put("txtPeriodDifference_Years", new Boolean(true));
        mandatoryMap.put("txtPeriodDifference_Months", new Boolean(true));
        mandatoryMap.put("txtPeriodDifference_Days", new Boolean(true));
        mandatoryMap.put("txtEligibleLoan", new Boolean(true));
        mandatoryMap.put("chkChequeBookAD", new Boolean(true));
        mandatoryMap.put("chkCustGrpLimitValidationAD", new Boolean(true));
        mandatoryMap.put("chkMobileBankingAD", new Boolean(true));
        mandatoryMap.put("chkNROStatusAD", new Boolean(true));
        mandatoryMap.put("chkATMAD", new Boolean(true));
        mandatoryMap.put("txtATMNoAD", new Boolean(true));
        mandatoryMap.put("tdtATMFromDateAD", new Boolean(true));
        mandatoryMap.put("tdtATMToDateAD", new Boolean(true));
        mandatoryMap.put("chkDebitAD", new Boolean(true));
        mandatoryMap.put("txtDebitNoAD", new Boolean(true));
        mandatoryMap.put("tdtDebitFromDateAD", new Boolean(true));
        mandatoryMap.put("tdtDebitToDateAD", new Boolean(true));
        mandatoryMap.put("chkCreditAD", new Boolean(true));
        mandatoryMap.put("txtCreditNoAD", new Boolean(true));
        mandatoryMap.put("tdtCreditFromDateAD", new Boolean(true));
        mandatoryMap.put("tdtCreditToDateAD", new Boolean(true));
        mandatoryMap.put("cboSettlementModeAI", new Boolean(true));
        mandatoryMap.put("cboOpModeAI", new Boolean(true));
        mandatoryMap.put("txtAccOpeningChrgAD", new Boolean(true));
        mandatoryMap.put("txtMisServiceChrgAD", new Boolean(true));
        mandatoryMap.put("chkStopPmtChrgAD", new Boolean(true));
        mandatoryMap.put("txtChequeBookChrgAD", new Boolean(true));
        mandatoryMap.put("chkChequeRetChrgAD", new Boolean(true));
        mandatoryMap.put("txtFolioChrgAD", new Boolean(true));
        mandatoryMap.put("chkInopChrgAD", new Boolean(true));
        mandatoryMap.put("txtAccCloseChrgAD", new Boolean(true));
        mandatoryMap.put("chkStmtChrgAD", new Boolean(true));
        mandatoryMap.put("cboStmtFreqAD", new Boolean(true));
        mandatoryMap.put("chkNonMainMinBalChrgAD", new Boolean(true));
        mandatoryMap.put("txtExcessWithChrgAD", new Boolean(true));
        mandatoryMap.put("chkABBChrgAD", new Boolean(true));
        mandatoryMap.put("chkNPAChrgAD", new Boolean(true));
        mandatoryMap.put("txtABBChrgAD", new Boolean(true));
        mandatoryMap.put("tdtNPAChrgAD", new Boolean(true));
        mandatoryMap.put("txtMinActBalanceAD", new Boolean(true));
        mandatoryMap.put("tdtDebit", new Boolean(true));
        mandatoryMap.put("tdtCredit", new Boolean(true));
        mandatoryMap.put("chkPayIntOnCrBalIN", new Boolean(true));
        mandatoryMap.put("chkPayIntOnDrBalIN", new Boolean(true));
        
    }
    
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    public void setHelpMessage() {
        final DepositLoanMRB objMandatoryRB = new DepositLoanMRB();
        cboDepositProduct.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProductId"));
        txtDepositNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDepositNo"));
        cboCategory.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCategory"));
        cboSanctionBy.setHelpMessage(lblMsg, objMandatoryRB.getString("cboSanctionBy"));
        txtLoanAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLoanAmt"));
        tdtAccountOpenDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtAccountOpenDate"));
        tdtRepaymentDt.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtRepaymentDt"));
        txtInter.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInter"));
    }
    
    
    private void setFieldNames() {
            lblProductId.setName("lblProductId");
            cboDepositProduct.setName("cboDepositProduct");
            lblDepositNo.setName("lblDepositNo");
            txtDepositNo.setName("txtDepositNo");
            lblAccountHeadId.setName("lblAccountHeadId");
            lblShowAccountHeadId.setName("lblShowAccountHeadId");
            lblConsititution.setName("lblConsititution");
            lblCategory.setName("lblCategory");
            cboCategory.setName("cboCategory");
            panBorrowProfile_CustID1.setName("panBorrowProfile_CustID1");
            lblCustNameValue.setName("lblCustNameValue");
            lblCustomerId.setName("lblCustomerId");
            lblCustomerIdValue.setName("lblCustomerIdValue");
            lblMemberId.setName("lblMemberId");
            lblMemberIdValue.setName("lblMemberIdValue");
            panBorrowProfile_CustID.setName("panBorrowProfile_CustID");
            lblAcctNo_Sanction.setName("lblAcctNo_Sanction");
            lblAcctNo_Sanction_Disp.setName("lblAcctNo_Sanction_Disp");
            lblSanctionRef.setName("lblSanctionRef");
            lblSanctionBy.setName("lblSanctionBy");
            cboSanctionBy.setName("cboSanctionBy");
            lblTotalShareAmount.setName("lblTotalShareAmount");
            lblTotalNoOfShare.setName("lblTotalNoOfShare");
            txtTotalNoOfShare.setName("txtTotalNoOfShare");
            txtTotalShareAmount.setName("txtTotalShareAmount");
            lblEligibileAmtValue.setName("lblEligibileAmtValue");
            lblLoanAmt.setName("lblLoanAmt");
            txtLoanAmt.setName("txtLoanAmt");
            lblAccOpenDt.setName("lblAccOpenDt");
            tdtAccountOpenDate.setName("tdtAccountOpenDate");
            
            
            lblRemarks.setName("lblRemarks");
            txtRemarks.setName("txtRemarks");
            lblAdditionalLoanfacility.setName("lblAdditionalLoanfacility");
            rdoAdditionalLoanFacility_Yes.setName("rdoAdditionalLoanFacility_Yes");
            rdoAdditionalLoanFacility_No.setName("rdoAdditionalLoanFacility_No");
            panAdditionalLoanFacility.setName("panAdditionalLoanFacility");

            
            lblRepaymentDt.setName("lblRepaymentDt");
            tdtRepaymentDt.setName("tdtRepaymentDt");
            lblInter.setName("lblInter");
            txtInter.setName("txtInter");
            btnDepositNo.setName("btnDepositNo");
            
             btnAuthorize.setName("btnAuthorize");
             btnDelete.setName("btnDelete");
             btnCancel.setName("btnCancel");
        //mobile banking
        panMobileBanking.setName("panMobileBanking");
        chkMobileBankingTLAD.setName("chkMobileBankingTLAD");
        lblMobileNo.setName("lblMobileNo");
        txtMobileNo.setName("txtMobileNo");
        lblMobileSubscribedFrom.setName("lblMobileSubscribedFrom");
        tdtMobileSubscribedFrom.setName("tdtMobileSubscribedFrom");
       // chkDepositUnlien.setName("chkDepositUnlien");
    }
    
    
    private void internationalize() {
            lblProductId.setText(resourceBundle.getString("lblProductId"));
            lblDepositNo.setText(resourceBundle.getString("lblDepositNo"));
            lblAccountHeadId.setText(resourceBundle.getString("lblAccountHeadId"));
            lblConsititution.setText(resourceBundle.getString("lblConsititution"));
            lblCategory.setText(resourceBundle.getString("lblCategory"));
            lblCustomerId.setText(resourceBundle.getString("lblCustomerId"));
            lblMemberId.setText(resourceBundle.getString("lblMemberId"));
            lblAcctNo_Sanction.setText(resourceBundle.getString("lblAcctNo_Sanction"));
            lblSanctionRef.setText(resourceBundle.getString("lblSanctionRef"));
            lblSanctionBy.setText(resourceBundle.getString("lblSanctionBy"));
            lblTotalNoOfShare.setText(resourceBundle.getString("lblTotalNoOfShare"));
            lblTotalShareAmount.setText(resourceBundle.getString("lblTotalShareAmount"));
            lblLoanAmt.setText(resourceBundle.getString("lblLoanAmt"));
            lblAccOpenDt.setText(resourceBundle.getString("lblAccOpenDt"));
            lblAdditionalLoanfacility.setText(resourceBundle.getString("lblAdditionalLoanfacility"));
            lblRepaymentDt.setText(resourceBundle.getString("lblRepaymentDt"));
            lblInter.setText(resourceBundle.getString("lblInter"));
           
            
     
        //        ((javax.swing.border.TitledBorder)panBorrowProfile.getBorder()).setTitle(resourceBundle.getString("panBorrowProfile"));
        //        lblStatus.setText(resourceBundle.getString("lblStatus"));

        //        ((javax.swing.border.TitledBorder)panDemandPromssoryDate.getBorder()).setTitle(resourceBundle.getString("panDemandPromssoryDate"));
        //        btnCancel.setText(resourceBundle.getString("btnCancel"));
        //        btnNew_Borrower.setText(resourceBundle.getString("btnNew_Borrower"));
 
    }
    
    private void removeRadioButtons() {
     removeFacilityRadioBtns();
    }
    
    private void removeFacilityRadioBtns(){
        rdoAdditionalFacilityGroup.remove(rdoAdditionalLoanFacility_Yes);
        rdoAdditionalFacilityGroup.remove(rdoAdditionalLoanFacility_No);
        rdoMultiDisburseAllow.remove(rdoMultiDisburseAllow_No);
        rdoMultiDisburseAllow.remove(rdoMultiDisburseAllow_Yes);
      
    }
    private void removeDocSubmittRadioBtns(){
        //        rdoIsSubmitted_DocumentDetails.remove(rdoYes_DocumentDetails);
        //        rdoIsSubmitted_DocumentDetails.remove(rdoNo_DocumentDetails);
    }
    private void removeDocExecuteRadioBtns(){
        //        rdoExecuted_DOC.remove(rdoYes_Executed_DOC);
        //        rdoExecuted_DOC.remove(rdoNo_Executed_DOC);
    }
    private void removeDocMandatoryRadioBtns(){
        //        rdoMandatory_DOC.remove(rdoYes_Mandatory_DOC);
        //        rdoMandatory_DOC.remove(rdoNo_Mandatory_DOC);
    }
    
    public void update(Observable observed, Object arg) {
        removeRadioButtons();
        lblStatus.setText(observable.getLblStatus());
        txtDepositNo.setText(CommonUtil.convertObjToStr(observable.getTxtDepositNo()));
        lblShowAccountHeadId.setText(CommonUtil.convertObjToStr(observable.getLblShowAccountHeadId()));
        lblCustNameValue.setText(CommonUtil.convertObjToStr(observable.getLblCustNameValue()));
        lblCustomerIdValue.setText(CommonUtil.convertObjToStr(observable.getLblCustomerIdValue()));
        lblMemberIdValue.setText(CommonUtil.convertObjToStr(observable.getLblMemberIdValue()));
        lblAcctNo_Sanction_Disp.setText(CommonUtil.convertObjToStr(observable.getLblAcctNo_Sanction_Disp()));
        txtSanctionRef.setText(CommonUtil.convertObjToStr(observable.getTxtSanctionRef()));
        cboSanctionBy.setSelectedItem(observable.getCboSanctioningAuthority());
        txtTotalNoOfShare.setText(CommonUtil.convertObjToStr(observable.getTxtTotalNoOfShare()));
        txtTotalShareAmount.setText(CommonUtil.convertObjToStr(observable.getTxtTotalShareAmount()));
        txtLoanAmt.setText(CommonUtil.convertObjToStr(observable.getTxtLoanAmt()));
        lblEligibileAmtValue.setText(observable.getLblEligibileAmtValue());
        tdtAccountOpenDate.setDateValue(CommonUtil.convertObjToStr(observable.getTdtAccountOpenDate()));
        //System.out.println("repayment date is" + observable.getTdtRepaymentDt());
        tdtRepaymentDt.setDateValue(CommonUtil.convertObjToStr(observable.getTdtRepaymentDt()));
        txtInter.setText(CommonUtil.convertObjToStr(observable.getTxtInter()));
        txtRemarks.setText(CommonUtil.convertObjToStr(observable.getTxtRemarks()));
        if (observable.getProductAuthRemarks().equals("MDS_LOAN")) {
            tblDepositDetails.setModel(observable.getTblSanctionMainMds());
        } else {
            tblDepositDetails.setModel(observable.getTblSanctionMain());
        }
        addRadioButtons();
        //        allowResetVisit=false;
        chkMobileBankingTLAD.setSelected(observable.getIsMobileBanking());
        txtMobileNo.setText(observable.getTxtMobileNo());
        tdtMobileSubscribedFrom.setDateValue(observable.getTdtMobileSubscribedFrom());
        //Added By Suresh
        if (observable.getMdsMap() != null && observable.getMdsMap().size() > 0) {
            Map hash = observable.getMdsMap();
            observable.setTxtDepositNo(CommonUtil.convertObjToStr(hash.get("CHITTAL_NO")));
            lblMDSMemberNoVal.setText(CommonUtil.convertObjToStr(hash.get("MEMBER_NO")));
            lblMDSMemberTypeVal.setText(CommonUtil.convertObjToStr(hash.get("MEMBER_TYPE")));
            lblMDSMemberNameVal.setText(CommonUtil.convertObjToStr(hash.get("MEMBER_NAME")));
            lblMDSChitAmountPaidVal.setText("Rs. " + CommonUtil.convertObjToStr(hash.get("AMOUNT")));
        }
        
        if (observable.getPaddyMap() != null && observable.getPaddyMap().size() > 0) {
            Map hash = observable.getPaddyMap();
            observable.setTxtDepositNo(CommonUtil.convertObjToStr(hash.get("CND_NO")));
            lblPurchaseIDVal.setText(CommonUtil.convertObjToStr(hash.get("PURCHASE_ID")));
            lblPurchaseNameVal.setText(CommonUtil.convertObjToStr(hash.get("PURCHASE_NAME")));
            lblTransactionDateVal.setText(CommonUtil.convertObjToStr(hash.get("TRANS_DT")));
            lblPurchaseDateVal.setText(CommonUtil.convertObjToStr(hash.get("PURCHASE_DATE")));
            lblTotalWeightVal.setText(CommonUtil.convertObjToStr(hash.get("WEIGHT")));
            lblAcreageVal.setText(CommonUtil.convertObjToStr(hash.get("ACRE")));
            lblPurchaseAmountVal.setText("Rs. " + CommonUtil.convertObjToStr(hash.get("AMOUNT")));
        }
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
            btnAuthorize.setEnabled(true);
            btnAuthorize.requestFocusInWindow();
        }
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
            int count = tblDepositDetails.getRowCount();
            double depAmt = 0;
            for (int i = 0; i < count; i++) {
                depAmt += CommonUtil.convertObjToDouble(tblDepositDetails.getValueAt(i, 1)).doubleValue();
            }
            HashMap whrMap = new HashMap();
            whrMap.put("DEPOSIT_AVAILABLE_BALANCE", depAmt);
            double currdepositAmt = observable.getLienAmount(whrMap, false);
            lblEligibileAmtValue.setText(CommonUtil.convertObjToStr(currdepositAmt));
        }
        cboDepositProductFocusLost(null);
        tdtToDate.setDateValue(observable.getToDate());
        txtNoOfInstall.setText(observable.getTxtNoInstallments());
        rdoMultiDisburseAllow_Yes.setSelected(observable.isRdoMultiDisburseAllow_Yes());
        rdoMultiDisburseAllow_No.setSelected(observable.isRdoMultiDisburseAllow_No());
        
     }
    
    private void addRadioButtons(){
        addFacilityRadioBtns();
    }
    
    private void addFacilityRadioBtns(){
        rdoAdditionalFacilityGroup = new CButtonGroup();
        rdoAdditionalFacilityGroup.add(rdoAdditionalLoanFacility_Yes);
        rdoAdditionalFacilityGroup.add(rdoAdditionalLoanFacility_No);
        rdoMultiDisburseAllow = new CButtonGroup();
        rdoMultiDisburseAllow.add(rdoMultiDisburseAllow_No);
        rdoMultiDisburseAllow.add(rdoMultiDisburseAllow_Yes);
      
    }
    
    private void displayShareDetails(HashMap hash) {
        List shareLst = ClientUtil.executeQuery("getShareAccountDetails", hash);
        if (shareLst != null && shareLst.size() > 0) {
            HashMap shareMap = new HashMap();
            shareMap = (HashMap) shareLst.get(0);
            observable.setTxtTotalNoOfShare(CommonUtil.convertObjToStr(shareMap.get("NO_OF_SHARES")));
            observable.setTxtTotalShareAmount(CommonUtil.convertObjToStr(shareMap.get("TOTAL_SHARE_AMOUNT")));
        } else {
            observable.setTxtTotalNoOfShare("");
            observable.setTxtTotalShareAmount("0");
        }
    }

    
    public void updateOBFields() {
//        if(chkDepositUnlien.isSelected()){
//            observable.setChkDepositLien("Y");
//        }else{
//            observable.setChkDepositLien("N");
//        }
         observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setSelectedBranchID(getSelectedBranchID());
        observable.setCboLoanProduct(CommonUtil.convertObjToStr(cboLoanProduct.getSelectedItem()));
        observable.setTxtDepositNo(CommonUtil.convertObjToStr(txtDepositNo.getText()));
        observable.setCboConstitution(CommonUtil.convertObjToStr(cboConsitution.getSelectedItem()));
        observable.setCboCategory(CommonUtil.convertObjToStr(cboCategory.getSelectedItem()));
        observable.setLblCustNameValue(lblCustNameValue.getText());
        observable.setLblCustomerIdValue(lblCustomerIdValue.getText());
        observable.setLblMemberIdValue(lblMemberIdValue.getText());
        observable.setLblAcctNo_Sanction_Disp(lblAcctNo_Sanction_Disp.getText());
        observable.setTxtSanctionRef(txtSanctionRef.getText());
        observable.setCboSanctioningAuthority(CommonUtil.convertObjToStr(cboSanctionBy.getSelectedItem()));
        observable.setTxtTotalNoOfShare(txtTotalNoOfShare.getText());
        observable.setTxtTotalShareAmount(txtTotalShareAmount.getText());
        observable.setTxtLoanAmt(txtLoanAmt.getText());
        observable.setLoanDisbursalAmt(txtLoanAmt.getText());
        observable.setLblEligibileAmtValue(lblEligibileAmtValue.getText());
        observable.setTdtAccountOpenDate(tdtAccountOpenDate.getDateValue());
        //System.out.println("rishhhhhhhhhhhhhhh"+tdtRepaymentDt.getDateValue());
        observable.setTdtRepaymentDt(tdtRepaymentDt.getDateValue());
        observable.setTxtInter(CommonUtil.convertObjToStr(txtInter.getText()));
        observable.setTblSanctionMain((com.see.truetransact.clientutil.EnhancedTableModel) tblDepositDetails.getModel());
        observable.setTxtRemarks(txtRemarks.getText());

        observable.setIsMobileBanking(chkMobileBankingTLAD.isSelected());
        observable.setTxtMobileNo(txtMobileNo.getText());
        observable.setTdtMobileSubscribedFrom(tdtMobileSubscribedFrom.getDateValue());
         if(observable.getDepoBehavesLike().equalsIgnoreCase("THRIFT")){
        observable.setToDate(tdtToDate.getDateValue());
        observable.setRdoMultiDisburseAllow_Yes(rdoMultiDisburseAllow_Yes.isSelected());
        observable.setRdoMultiDisburseAllow_No(rdoMultiDisburseAllow_No.isSelected());
        observable.setTxtNoInstallments(txtNoOfInstall.getText());
         }
         
         observable.setServiceTax_Map(serviceTax_Map);
    }

    private void initComponentData() {
        tblDepositDetails.setModel(observable.getTblSanctionMainMds());
        tblDepositDetails.setModel(observable.getTblSanctionMain());
        cboCategory.setModel(observable.getCbmCategory());
        cboConsitution.setModel(observable.getCbmConstitution());
        cboSanctionBy.setModel(observable.getCbmSanctioningAuthority());
//        cboDepositProduct.setModel(observable.getCbmDepositProduct());
        cboLoanProduct.setModel(observable.getCbmLoanProduct());
    }

    private void setMaxLength() {
        //Customer/Sanction Details
        txtDepositNo.setMaxLength(16);
        txtDepositNo.setAllowAll(true);
        txtLoanAmt.setValidation(new CurrencyValidation(14, 2));
        txtInter.setValidation(new PercentageValidation());
//          txtMobileNo.setValidation(new NumericValidation());
        txtMobileNo.setValidation(new PhoneNoValidation());
        txtMobileNo.setMaxLength(11);
        txtInter.setMaxLength(5);
        txtCustId.setAllowAll(true);
        txtNoOfInstall.setAllowNumber(true);
        txtInstallAmount.setAllowNumber(true);
    }
    
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        btnSave.setEnabled( btnCancel.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        setAuthBtnEnableDisable();
    }
    
    /** To Enable or Disable Authorize, Rejection and Exception Button */
    private void setAuthBtnEnableDisable(){
        final boolean enableDisable = !btnSave.isEnabled();
        btnAuthorize.setEnabled(enableDisable);
        btnException.setEnabled(enableDisable);
        btnReject.setEnabled(enableDisable);
        mitAuthorize.setEnabled(enableDisable);
        mitException.setEnabled(enableDisable);
        mitReject.setEnabled(enableDisable);
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     *
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoStatus = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoSecurityDetails = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoAccType = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoAccLimit = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoNatureInterest = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoRiskWeight = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoMultiDisburseAllow = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoSubsidy = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoInterest = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoSecurityType = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoDoAddSIs = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoPostDatedCheque = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoIsSubmitted_DocumentDetails = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoStatus_Repayment = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoExecuted_DOC = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoMandatory_DOC = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoGuarnConstution = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoAdditionalFacilityGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrTermLoan = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace17 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace18 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace19 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace20 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace21 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblspace3 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace22 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panTermLoan = new com.see.truetransact.uicomponent.CPanel();
        tabLimitAmount = new com.see.truetransact.uicomponent.CTabbedPane();
        panBorrowCompanyDetails = new com.see.truetransact.uicomponent.CPanel();
        panBorrowProfile = new com.see.truetransact.uicomponent.CPanel();
        panBorrowProfile_CustName = new com.see.truetransact.uicomponent.CPanel();
        panBorrowProfile_CustID = new com.see.truetransact.uicomponent.CPanel();
        lblAccountHeadId = new com.see.truetransact.uicomponent.CLabel();
        panCustId = new com.see.truetransact.uicomponent.CPanel();
        txtDepositNo = new com.see.truetransact.uicomponent.CTextField();
        btnDepositNo = new com.see.truetransact.uicomponent.CButton();
        lblCustNameValue = new com.see.truetransact.uicomponent.CLabel();
        cboConsitution = new com.see.truetransact.uicomponent.CComboBox();
        lblCustomerId = new com.see.truetransact.uicomponent.CLabel();
        lblDepositNo = new com.see.truetransact.uicomponent.CLabel();
        lblConsititution = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerIdValue = new com.see.truetransact.uicomponent.CLabel();
        lblShowAccountHeadId = new com.see.truetransact.uicomponent.CLabel();
        lblMemberIdValue = new com.see.truetransact.uicomponent.CLabel();
        lblProductId = new com.see.truetransact.uicomponent.CLabel();
        lblCategory = new com.see.truetransact.uicomponent.CLabel();
        lblMemberId = new com.see.truetransact.uicomponent.CLabel();
        cboLoanProduct = new com.see.truetransact.uicomponent.CComboBox();
        cboCategory = new com.see.truetransact.uicomponent.CComboBox();
        lblProductId1 = new com.see.truetransact.uicomponent.CLabel();
        cboDepositProduct = new com.see.truetransact.uicomponent.CComboBox();
        panCustIdDetails = new com.see.truetransact.uicomponent.CPanel();
        txtCustId = new com.see.truetransact.uicomponent.CTextField();
        btnCustID = new com.see.truetransact.uicomponent.CButton();
        panChargeDetails = new com.see.truetransact.uicomponent.CPanel();
        txtNextAccNo = new com.see.truetransact.uicomponent.CTextField();
        jLabel1 = new com.see.truetransact.uicomponent.CLabel();
        chkDepositUnlien = new com.see.truetransact.uicomponent.CCheckBox();
        panMobileBanking = new com.see.truetransact.uicomponent.CPanel();
        chkMobileBankingTLAD = new com.see.truetransact.uicomponent.CCheckBox();
        lblMobileNo = new com.see.truetransact.uicomponent.CLabel();
        txtMobileNo = new com.see.truetransact.uicomponent.CTextField();
        tdtMobileSubscribedFrom = new com.see.truetransact.uicomponent.CDateField();
        lblMobileSubscribedFrom = new com.see.truetransact.uicomponent.CLabel();
        panServiceTax = new com.see.truetransact.uicomponent.CPanel();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        lblServiceTaxval = new com.see.truetransact.uicomponent.CLabel();
        panBorrowProfile_CustID1 = new com.see.truetransact.uicomponent.CPanel();
        panBorrowerTabCTable = new com.see.truetransact.uicomponent.CPanel();
        srpBorrowerTabCTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblDepositDetails = new com.see.truetransact.uicomponent.CTable();
        panBorrowerTabTools = new com.see.truetransact.uicomponent.CPanel();
        btnSave_Borrower = new com.see.truetransact.uicomponent.CButton();
        btnDeleteBorrower = new com.see.truetransact.uicomponent.CButton();
        btnNew_Borrower = new com.see.truetransact.uicomponent.CButton();
        panShareDetails = new com.see.truetransact.uicomponent.CPanel();
        lblTotalShareAmount = new com.see.truetransact.uicomponent.CLabel();
        lblTotalNoOfShare = new com.see.truetransact.uicomponent.CLabel();
        txtTotalNoOfShare = new com.see.truetransact.uicomponent.CTextField();
        txtTotalShareAmount = new com.see.truetransact.uicomponent.CTextField();
        panMDSLoanDetailFields = new com.see.truetransact.uicomponent.CPanel();
        lblMDSMemberTypeVal = new com.see.truetransact.uicomponent.CLabel();
        lblMDSMemberType = new com.see.truetransact.uicomponent.CLabel();
        lblMDSMemberName = new com.see.truetransact.uicomponent.CLabel();
        lblMDSChitAmountPaid = new com.see.truetransact.uicomponent.CLabel();
        lblMDSMemberNo = new com.see.truetransact.uicomponent.CLabel();
        lblMDSMemberNameVal = new com.see.truetransact.uicomponent.CLabel();
        lblMDSChitAmountPaidVal = new com.see.truetransact.uicomponent.CLabel();
        lblMDSMemberNoVal = new com.see.truetransact.uicomponent.CLabel();
        panPaddyLoanDetailFields = new com.see.truetransact.uicomponent.CPanel();
        lblPurchaseNameVal = new com.see.truetransact.uicomponent.CLabel();
        lblPurchaseName = new com.see.truetransact.uicomponent.CLabel();
        lblPurchaseDate = new com.see.truetransact.uicomponent.CLabel();
        lblTotalWeight = new com.see.truetransact.uicomponent.CLabel();
        lblPurchaseID = new com.see.truetransact.uicomponent.CLabel();
        lblPurchaseDateVal = new com.see.truetransact.uicomponent.CLabel();
        lblTotalWeightVal = new com.see.truetransact.uicomponent.CLabel();
        lblPurchaseIDVal = new com.see.truetransact.uicomponent.CLabel();
        lblAcreage = new com.see.truetransact.uicomponent.CLabel();
        lblPurchaseAmount = new com.see.truetransact.uicomponent.CLabel();
        lblTransactionDate = new com.see.truetransact.uicomponent.CLabel();
        lblAcreageVal = new com.see.truetransact.uicomponent.CLabel();
        lblPurchaseAmountVal = new com.see.truetransact.uicomponent.CLabel();
        lblTransactionDateVal = new com.see.truetransact.uicomponent.CLabel();
        panSanctionDetails_Table = new com.see.truetransact.uicomponent.CPanel();
        panSanctionDetails_Sanction = new com.see.truetransact.uicomponent.CPanel();
        panInterestDeails1 = new com.see.truetransact.uicomponent.CPanel();
        lblEligibileAmtValue = new com.see.truetransact.uicomponent.CLabel();
        txtLoanAmt = new com.see.truetransact.uicomponent.CTextField();
        lblRepaymentDt = new com.see.truetransact.uicomponent.CLabel();
        tdtRepaymentDt = new com.see.truetransact.uicomponent.CDateField();
        lblInter = new com.see.truetransact.uicomponent.CLabel();
        txtInter = new com.see.truetransact.uicomponent.CTextField();
        panAdditionalLoanFacility = new com.see.truetransact.uicomponent.CPanel();
        lblEligibileAmt = new com.see.truetransact.uicomponent.CLabel();
        lblLoanAmt = new com.see.truetransact.uicomponent.CLabel();
        lblIntrestAmt = new com.see.truetransact.uicomponent.CLabel();
        lblNoOfInstall = new com.see.truetransact.uicomponent.CLabel();
        lblTodate = new com.see.truetransact.uicomponent.CLabel();
        tdtToDate = new com.see.truetransact.uicomponent.CDateField();
        txtNoOfInstall = new com.see.truetransact.uicomponent.CTextField();
        txtInstallAmount = new com.see.truetransact.uicomponent.CTextField();
        lblInstallMentAmount = new com.see.truetransact.uicomponent.CLabel();
        panInterestDeails2 = new com.see.truetransact.uicomponent.CPanel();
        cboSanctionBy = new com.see.truetransact.uicomponent.CComboBox();
        lblSanctionBy = new com.see.truetransact.uicomponent.CLabel();
        lblSanctionRef = new com.see.truetransact.uicomponent.CLabel();
        lblAcctNo_Sanction = new com.see.truetransact.uicomponent.CLabel();
        lblAcctNo_Sanction_Disp = new com.see.truetransact.uicomponent.CLabel();
        lblAccOpenDt = new com.see.truetransact.uicomponent.CLabel();
        tdtAccountOpenDate = new com.see.truetransact.uicomponent.CDateField();
        txtSanctionRef = new com.see.truetransact.uicomponent.CTextField();
        lblMultiDisburseAllow = new com.see.truetransact.uicomponent.CLabel();
        rdoMultiDisburseAllow_Yes = new javax.swing.JRadioButton();
        rdoMultiDisburseAllow_No = new javax.swing.JRadioButton();
        lblAdditionalLoanfacility = new com.see.truetransact.uicomponent.CLabel();
        rdoAdditionalLoanFacility_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoAdditionalLoanFacility_No = new com.see.truetransact.uicomponent.CRadioButton();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        mbrTermLoan = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptProcess = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitAuthorize = new javax.swing.JMenuItem();
        mitException = new javax.swing.JMenuItem();
        mitReject = new javax.swing.JMenuItem();
        sptException = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        sptPrint = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Deposit Loan Ac");
        setMinimumSize(new java.awt.Dimension(860, 665));
        setPreferredSize(new java.awt.Dimension(860, 665));

        tbrTermLoan.setMinimumSize(new java.awt.Dimension(345, 29));

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnView);

        lblSpace4.setText("     ");
        tbrTermLoan.add(lblSpace4);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnNew);

        lblSpace17.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace17.setText("     ");
        lblSpace17.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace17);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnEdit);

        lblSpace18.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace18.setText("     ");
        lblSpace18.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace18);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnDelete);

        lblSpace2.setText("     ");
        tbrTermLoan.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnSave);

        lblSpace19.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace19.setText("     ");
        lblSpace19.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace19);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.setEnabled(false);
        btnCancel.setFocusable(false);
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnCancel);

        lblSpace3.setText("     ");
        tbrTermLoan.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setMaximumSize(new java.awt.Dimension(29, 27));
        btnAuthorize.setMinimumSize(new java.awt.Dimension(29, 27));
        btnAuthorize.setPreferredSize(new java.awt.Dimension(29, 27));
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnAuthorize);

        lblSpace20.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace20.setText("     ");
        lblSpace20.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace20);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setEnabled(false);
        btnException.setMaximumSize(new java.awt.Dimension(29, 27));
        btnException.setMinimumSize(new java.awt.Dimension(29, 27));
        btnException.setPreferredSize(new java.awt.Dimension(29, 27));
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnException);

        lblSpace21.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace21.setText("     ");
        lblSpace21.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace21);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setEnabled(false);
        btnReject.setMaximumSize(new java.awt.Dimension(29, 27));
        btnReject.setMinimumSize(new java.awt.Dimension(29, 27));
        btnReject.setPreferredSize(new java.awt.Dimension(29, 27));
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnReject);

        lblspace3.setMaximumSize(new java.awt.Dimension(15, 15));
        lblspace3.setMinimumSize(new java.awt.Dimension(15, 15));
        lblspace3.setPreferredSize(new java.awt.Dimension(15, 15));
        tbrTermLoan.add(lblspace3);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnPrint);

        lblSpace22.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace22.setText("     ");
        lblSpace22.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace22);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnClose);

        getContentPane().add(tbrTermLoan, java.awt.BorderLayout.NORTH);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace1.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace1, gridBagConstraints);

        lblStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus.setText("                      ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblStatus, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblMsg, gridBagConstraints);

        getContentPane().add(panStatus, java.awt.BorderLayout.SOUTH);

        panTermLoan.setMinimumSize(new java.awt.Dimension(1000, 650));
        panTermLoan.setPreferredSize(new java.awt.Dimension(1000, 650));
        panTermLoan.setLayout(new java.awt.GridBagLayout());

        tabLimitAmount.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        tabLimitAmount.setMinimumSize(new java.awt.Dimension(1000, 639));
        tabLimitAmount.setPreferredSize(new java.awt.Dimension(1000, 639));

        panBorrowCompanyDetails.setMinimumSize(new java.awt.Dimension(1000, 545));
        panBorrowCompanyDetails.setPreferredSize(new java.awt.Dimension(1000, 545));
        panBorrowCompanyDetails.setLayout(new java.awt.GridBagLayout());

        panBorrowProfile.setBorder(javax.swing.BorderFactory.createTitledBorder("Borrower's Profile"));
        panBorrowProfile.setMinimumSize(new java.awt.Dimension(1000, 335));
        panBorrowProfile.setPreferredSize(new java.awt.Dimension(1000, 335));
        panBorrowProfile.setLayout(new java.awt.GridBagLayout());

        panBorrowProfile_CustName.setMaximumSize(new java.awt.Dimension(550, 310));
        panBorrowProfile_CustName.setMinimumSize(new java.awt.Dimension(550, 310));
        panBorrowProfile_CustName.setPreferredSize(new java.awt.Dimension(550, 310));
        panBorrowProfile_CustName.setLayout(new java.awt.GridBagLayout());

        panBorrowProfile_CustID.setMaximumSize(new java.awt.Dimension(550, 250));
        panBorrowProfile_CustID.setMinimumSize(new java.awt.Dimension(550, 250));
        panBorrowProfile_CustID.setPreferredSize(new java.awt.Dimension(550, 250));
        panBorrowProfile_CustID.setLayout(new java.awt.GridBagLayout());

        lblAccountHeadId.setText("Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panBorrowProfile_CustID.add(lblAccountHeadId, gridBagConstraints);

        panCustId.setFocusable(false);
        panCustId.setMinimumSize(new java.awt.Dimension(143, 24));
        panCustId.setPreferredSize(new java.awt.Dimension(143, 24));
        panCustId.setLayout(new java.awt.GridBagLayout());

        txtDepositNo.setAllowAll(true);
        txtDepositNo.setMinimumSize(new java.awt.Dimension(110, 21));
        txtDepositNo.setPreferredSize(new java.awt.Dimension(110, 21));
        txtDepositNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDepositNoActionPerformed(evt);
            }
        });
        txtDepositNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDepositNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCustId.add(txtDepositNo, gridBagConstraints);

        btnDepositNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDepositNo.setToolTipText("Select Customer");
        btnDepositNo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDepositNo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnDepositNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDepositNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDepositNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDepositNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panCustId.add(btnDepositNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 2);
        panBorrowProfile_CustID.add(panCustId, gridBagConstraints);

        lblCustNameValue.setForeground(new java.awt.Color(0, 51, 204));
        lblCustNameValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblCustNameValue.setMaximumSize(new java.awt.Dimension(80, 16));
        lblCustNameValue.setMinimumSize(new java.awt.Dimension(80, 16));
        lblCustNameValue.setPreferredSize(new java.awt.Dimension(80, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panBorrowProfile_CustID.add(lblCustNameValue, gridBagConstraints);

        cboConsitution.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panBorrowProfile_CustID.add(cboConsitution, gridBagConstraints);

        lblCustomerId.setText("Customer Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panBorrowProfile_CustID.add(lblCustomerId, gridBagConstraints);

        lblDepositNo.setText("Deposit No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, -20, 1, 2);
        panBorrowProfile_CustID.add(lblDepositNo, gridBagConstraints);

        lblConsititution.setText("Constitution");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panBorrowProfile_CustID.add(lblConsititution, gridBagConstraints);

        lblCustomerIdValue.setForeground(new java.awt.Color(0, 51, 204));
        lblCustomerIdValue.setMaximumSize(new java.awt.Dimension(150, 16));
        lblCustomerIdValue.setMinimumSize(new java.awt.Dimension(150, 16));
        lblCustomerIdValue.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 2);
        panBorrowProfile_CustID.add(lblCustomerIdValue, gridBagConstraints);

        lblShowAccountHeadId.setMaximumSize(new java.awt.Dimension(150, 16));
        lblShowAccountHeadId.setMinimumSize(new java.awt.Dimension(150, 16));
        lblShowAccountHeadId.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panBorrowProfile_CustID.add(lblShowAccountHeadId, gridBagConstraints);

        lblMemberIdValue.setForeground(new java.awt.Color(0, 51, 204));
        lblMemberIdValue.setMaximumSize(new java.awt.Dimension(150, 16));
        lblMemberIdValue.setMinimumSize(new java.awt.Dimension(150, 16));
        lblMemberIdValue.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panBorrowProfile_CustID.add(lblMemberIdValue, gridBagConstraints);

        lblProductId.setText("Loan Product");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panBorrowProfile_CustID.add(lblProductId, gridBagConstraints);

        lblCategory.setText("Category");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panBorrowProfile_CustID.add(lblCategory, gridBagConstraints);

        lblMemberId.setText("Member No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panBorrowProfile_CustID.add(lblMemberId, gridBagConstraints);

        cboLoanProduct.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        cboLoanProduct.setMinimumSize(new java.awt.Dimension(100, 21));
        cboLoanProduct.setPopupWidth(150);
        cboLoanProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLoanProductActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panBorrowProfile_CustID.add(cboLoanProduct, gridBagConstraints);

        cboCategory.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panBorrowProfile_CustID.add(cboCategory, gridBagConstraints);

        lblProductId1.setText("Deposit Product");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panBorrowProfile_CustID.add(lblProductId1, gridBagConstraints);

        cboDepositProduct.setMinimumSize(new java.awt.Dimension(100, 21));
        cboDepositProduct.setOpaque(false);
        cboDepositProduct.setPopupWidth(160);
        cboDepositProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboDepositProductActionPerformed(evt);
            }
        });
        cboDepositProduct.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboDepositProductFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panBorrowProfile_CustID.add(cboDepositProduct, gridBagConstraints);

        panCustIdDetails.setFocusable(false);
        panCustIdDetails.setMinimumSize(new java.awt.Dimension(125, 24));
        panCustIdDetails.setPreferredSize(new java.awt.Dimension(125, 24));
        panCustIdDetails.setLayout(new java.awt.GridBagLayout());

        txtCustId.setAllowAll(true);
        txtCustId.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCustId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCustIdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCustIdDetails.add(txtCustId, gridBagConstraints);

        btnCustID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCustID.setToolTipText("Select Customer");
        btnCustID.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCustID.setMaximumSize(new java.awt.Dimension(21, 21));
        btnCustID.setMinimumSize(new java.awt.Dimension(21, 21));
        btnCustID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCustID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panCustIdDetails.add(btnCustID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        panBorrowProfile_CustID.add(panCustIdDetails, gridBagConstraints);

        panChargeDetails.setMinimumSize(new java.awt.Dimension(650, 70));
        panChargeDetails.setPreferredSize(new java.awt.Dimension(650, 70));
        panChargeDetails.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.ipadx = 9;
        panBorrowProfile_CustID.add(panChargeDetails, gridBagConstraints);

        txtNextAccNo.setEditable(false);
        txtNextAccNo.setEnabled(false);
        txtNextAccNo.setMaximumSize(new java.awt.Dimension(150, 21));
        txtNextAccNo.setMinimumSize(new java.awt.Dimension(150, 21));
        txtNextAccNo.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panBorrowProfile_CustID.add(txtNextAccNo, gridBagConstraints);

        jLabel1.setForeground(new java.awt.Color(51, 102, 255));
        jLabel1.setText("Next Account No");
        jLabel1.setMaximumSize(new java.awt.Dimension(175, 21));
        jLabel1.setMinimumSize(new java.awt.Dimension(175, 21));
        jLabel1.setPreferredSize(new java.awt.Dimension(175, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panBorrowProfile_CustID.add(jLabel1, gridBagConstraints);

        chkDepositUnlien.setText("Deposit Unlien");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panBorrowProfile_CustID.add(chkDepositUnlien, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        panBorrowProfile_CustName.add(panBorrowProfile_CustID, gridBagConstraints);

        panMobileBanking.setBorder(javax.swing.BorderFactory.createTitledBorder("Mobile Banking"));
        panMobileBanking.setMinimumSize(new java.awt.Dimension(430, 65));
        panMobileBanking.setPreferredSize(new java.awt.Dimension(430, 65));
        panMobileBanking.setLayout(new java.awt.GridBagLayout());

        chkMobileBankingTLAD.setText("Required");
        chkMobileBankingTLAD.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkMobileBankingTLAD.setMinimumSize(new java.awt.Dimension(80, 21));
        chkMobileBankingTLAD.setPreferredSize(new java.awt.Dimension(80, 21));
        chkMobileBankingTLAD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkMobileBankingTLADActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panMobileBanking.add(chkMobileBankingTLAD, gridBagConstraints);

        lblMobileNo.setText("Mobile No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panMobileBanking.add(lblMobileNo, gridBagConstraints);

        txtMobileNo.setAllowAll(true);
        txtMobileNo.setMaxLength(16);
        txtMobileNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMobileNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMobileNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 2);
        panMobileBanking.add(txtMobileNo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 4);
        panMobileBanking.add(tdtMobileSubscribedFrom, gridBagConstraints);

        lblMobileSubscribedFrom.setText("Subscribed From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panMobileBanking.add(lblMobileSubscribedFrom, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panBorrowProfile_CustName.add(panMobileBanking, gridBagConstraints);

        panServiceTax.setLayout(new java.awt.GridBagLayout());

        cLabel1.setText("Service Tax :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panServiceTax.add(cLabel1, gridBagConstraints);

        lblServiceTaxval.setText("0.0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        panServiceTax.add(lblServiceTaxval, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panBorrowProfile_CustName.add(panServiceTax, gridBagConstraints);

        panBorrowProfile.add(panBorrowProfile_CustName, new java.awt.GridBagConstraints());

        panBorrowProfile_CustID1.setMaximumSize(new java.awt.Dimension(300, 310));
        panBorrowProfile_CustID1.setMinimumSize(new java.awt.Dimension(300, 310));
        panBorrowProfile_CustID1.setPreferredSize(new java.awt.Dimension(300, 310));
        panBorrowProfile_CustID1.setLayout(new java.awt.GridBagLayout());

        panBorrowerTabCTable.setMaximumSize(new java.awt.Dimension(300, 120));
        panBorrowerTabCTable.setMinimumSize(new java.awt.Dimension(300, 120));
        panBorrowerTabCTable.setPreferredSize(new java.awt.Dimension(300, 120));
        panBorrowerTabCTable.setLayout(new java.awt.GridBagLayout());

        srpBorrowerTabCTable.setMaximumSize(new java.awt.Dimension(300, 120));
        srpBorrowerTabCTable.setMinimumSize(new java.awt.Dimension(300, 120));
        srpBorrowerTabCTable.setPreferredSize(new java.awt.Dimension(300, 120));

        tblDepositDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblDepositDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDepositDetailsMouseClicked(evt);
            }
        });
        srpBorrowerTabCTable.setViewportView(tblDepositDetails);

        panBorrowerTabCTable.add(srpBorrowerTabCTable, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBorrowProfile_CustID1.add(panBorrowerTabCTable, gridBagConstraints);

        panBorrowerTabTools.setMinimumSize(new java.awt.Dimension(228, 33));
        panBorrowerTabTools.setPreferredSize(new java.awt.Dimension(228, 33));
        panBorrowerTabTools.setLayout(new java.awt.GridBagLayout());

        btnSave_Borrower.setText("Save");
        btnSave_Borrower.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSave_BorrowerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panBorrowerTabTools.add(btnSave_Borrower, gridBagConstraints);

        btnDeleteBorrower.setText("Delete");
        btnDeleteBorrower.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteBorrowerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panBorrowerTabTools.add(btnDeleteBorrower, gridBagConstraints);

        btnNew_Borrower.setText("New");
        btnNew_Borrower.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNew_BorrowerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panBorrowerTabTools.add(btnNew_Borrower, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        panBorrowProfile_CustID1.add(panBorrowerTabTools, gridBagConstraints);

        panShareDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Share Details"));
        panShareDetails.setMinimumSize(new java.awt.Dimension(285, 75));
        panShareDetails.setPreferredSize(new java.awt.Dimension(285, 75));
        panShareDetails.setLayout(new java.awt.GridBagLayout());

        lblTotalShareAmount.setText("Total Share Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 6, 4);
        panShareDetails.add(lblTotalShareAmount, gridBagConstraints);

        lblTotalNoOfShare.setText("Total No Of Share");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 44, 2, 4);
        panShareDetails.add(lblTotalNoOfShare, gridBagConstraints);

        txtTotalNoOfShare.setBackground(new java.awt.Color(212, 208, 200));
        txtTotalNoOfShare.setEditable(false);
        txtTotalNoOfShare.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtTotalNoOfShare.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 4);
        panShareDetails.add(txtTotalNoOfShare, gridBagConstraints);

        txtTotalShareAmount.setBackground(new java.awt.Color(212, 208, 200));
        txtTotalShareAmount.setEditable(false);
        txtTotalShareAmount.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTotalShareAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 5, 4);
        panShareDetails.add(txtTotalShareAmount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
        panBorrowProfile_CustID1.add(panShareDetails, gridBagConstraints);

        panMDSLoanDetailFields.setBorder(javax.swing.BorderFactory.createTitledBorder("MDS Member Details"));
        panMDSLoanDetailFields.setMaximumSize(new java.awt.Dimension(300, 150));
        panMDSLoanDetailFields.setMinimumSize(new java.awt.Dimension(300, 150));
        panMDSLoanDetailFields.setPreferredSize(new java.awt.Dimension(300, 150));
        panMDSLoanDetailFields.setLayout(new java.awt.GridBagLayout());

        lblMDSMemberTypeVal.setText("                         ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        panMDSLoanDetailFields.add(lblMDSMemberTypeVal, gridBagConstraints);

        lblMDSMemberType.setText("Member Type : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMDSLoanDetailFields.add(lblMDSMemberType, gridBagConstraints);

        lblMDSMemberName.setText("Member Name : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMDSLoanDetailFields.add(lblMDSMemberName, gridBagConstraints);

        lblMDSChitAmountPaid.setText("Chit Amount Paid : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMDSLoanDetailFields.add(lblMDSChitAmountPaid, gridBagConstraints);

        lblMDSMemberNo.setText("Member No : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMDSLoanDetailFields.add(lblMDSMemberNo, gridBagConstraints);

        lblMDSMemberNameVal.setForeground(new java.awt.Color(0, 51, 204));
        lblMDSMemberNameVal.setText("                                      ");
        lblMDSMemberNameVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        panMDSLoanDetailFields.add(lblMDSMemberNameVal, gridBagConstraints);

        lblMDSChitAmountPaidVal.setText("                         ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        panMDSLoanDetailFields.add(lblMDSChitAmountPaidVal, gridBagConstraints);

        lblMDSMemberNoVal.setForeground(new java.awt.Color(0, 51, 204));
        lblMDSMemberNoVal.setText("                         ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        panMDSLoanDetailFields.add(lblMDSMemberNoVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBorrowProfile_CustID1.add(panMDSLoanDetailFields, gridBagConstraints);

        panPaddyLoanDetailFields.setBorder(javax.swing.BorderFactory.createTitledBorder("Paddy Details"));
        panPaddyLoanDetailFields.setMaximumSize(new java.awt.Dimension(300, 220));
        panPaddyLoanDetailFields.setMinimumSize(new java.awt.Dimension(300, 220));
        panPaddyLoanDetailFields.setPreferredSize(new java.awt.Dimension(300, 220));
        panPaddyLoanDetailFields.setLayout(new java.awt.GridBagLayout());

        lblPurchaseNameVal.setText("                         ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        panPaddyLoanDetailFields.add(lblPurchaseNameVal, gridBagConstraints);

        lblPurchaseName.setText("Name : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPaddyLoanDetailFields.add(lblPurchaseName, gridBagConstraints);

        lblPurchaseDate.setText("Purchase Date : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPaddyLoanDetailFields.add(lblPurchaseDate, gridBagConstraints);

        lblTotalWeight.setText("Total Weight : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPaddyLoanDetailFields.add(lblTotalWeight, gridBagConstraints);

        lblPurchaseID.setText("Purchase ID : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPaddyLoanDetailFields.add(lblPurchaseID, gridBagConstraints);

        lblPurchaseDateVal.setForeground(new java.awt.Color(0, 51, 204));
        lblPurchaseDateVal.setText("                                      ");
        lblPurchaseDateVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        panPaddyLoanDetailFields.add(lblPurchaseDateVal, gridBagConstraints);

        lblTotalWeightVal.setText("                         ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        panPaddyLoanDetailFields.add(lblTotalWeightVal, gridBagConstraints);

        lblPurchaseIDVal.setForeground(new java.awt.Color(0, 51, 204));
        lblPurchaseIDVal.setText("                         ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        panPaddyLoanDetailFields.add(lblPurchaseIDVal, gridBagConstraints);

        lblAcreage.setText("Acreage : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPaddyLoanDetailFields.add(lblAcreage, gridBagConstraints);

        lblPurchaseAmount.setText("Purchase Amount : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPaddyLoanDetailFields.add(lblPurchaseAmount, gridBagConstraints);

        lblTransactionDate.setText("Transaction Date : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPaddyLoanDetailFields.add(lblTransactionDate, gridBagConstraints);

        lblAcreageVal.setText("                         ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        panPaddyLoanDetailFields.add(lblAcreageVal, gridBagConstraints);

        lblPurchaseAmountVal.setText("                         ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        panPaddyLoanDetailFields.add(lblPurchaseAmountVal, gridBagConstraints);

        lblTransactionDateVal.setText("                         ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        panPaddyLoanDetailFields.add(lblTransactionDateVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBorrowProfile_CustID1.add(panPaddyLoanDetailFields, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        panBorrowProfile.add(panBorrowProfile_CustID1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBorrowCompanyDetails.add(panBorrowProfile, gridBagConstraints);

        panSanctionDetails_Table.setBorder(javax.swing.BorderFactory.createTitledBorder("Sanction Details"));
        panSanctionDetails_Table.setMinimumSize(new java.awt.Dimension(1000, 270));
        panSanctionDetails_Table.setPreferredSize(new java.awt.Dimension(1000, 270));
        panSanctionDetails_Table.setLayout(new java.awt.GridBagLayout());

        panSanctionDetails_Sanction.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panSanctionDetails_Sanction.setMinimumSize(new java.awt.Dimension(850, 265));
        panSanctionDetails_Sanction.setPreferredSize(new java.awt.Dimension(850, 265));
        panSanctionDetails_Sanction.setLayout(new java.awt.GridBagLayout());

        panInterestDeails1.setMinimumSize(new java.awt.Dimension(350, 190));
        panInterestDeails1.setPreferredSize(new java.awt.Dimension(350, 190));
        panInterestDeails1.setLayout(new java.awt.GridBagLayout());

        lblEligibileAmtValue.setText("Loan Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 11, 0, 13);
        panInterestDeails1.add(lblEligibileAmtValue, gridBagConstraints);

        txtLoanAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtLoanAmt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLoanAmtActionPerformed(evt);
            }
        });
        txtLoanAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtLoanAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panInterestDeails1.add(txtLoanAmt, gridBagConstraints);

        lblRepaymentDt.setText("Repayment Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 2);
        panInterestDeails1.add(lblRepaymentDt, gridBagConstraints);

        tdtRepaymentDt.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtRepaymentDt.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panInterestDeails1.add(tdtRepaymentDt, gridBagConstraints);

        lblInter.setText("Interest %");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 2);
        panInterestDeails1.add(lblInter, gridBagConstraints);

        txtInter.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panInterestDeails1.add(txtInter, gridBagConstraints);

        panAdditionalLoanFacility.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterestDeails1.add(panAdditionalLoanFacility, gridBagConstraints);

        lblEligibileAmt.setText("Eligibile Amt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 2);
        panInterestDeails1.add(lblEligibileAmt, gridBagConstraints);

        lblLoanAmt.setText("Loan Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 2);
        panInterestDeails1.add(lblLoanAmt, gridBagConstraints);

        lblIntrestAmt.setMaximumSize(new java.awt.Dimension(97, 18));
        lblIntrestAmt.setMinimumSize(new java.awt.Dimension(97, 18));
        lblIntrestAmt.setPreferredSize(new java.awt.Dimension(97, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInterestDeails1.add(lblIntrestAmt, gridBagConstraints);

        lblNoOfInstall.setText("No of Installments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        panInterestDeails1.add(lblNoOfInstall, gridBagConstraints);

        lblTodate.setText("ToDate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        panInterestDeails1.add(lblTodate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panInterestDeails1.add(tdtToDate, gridBagConstraints);

        txtNoOfInstall.setMinimumSize(new java.awt.Dimension(100, 25));
        txtNoOfInstall.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNoOfInstallActionPerformed(evt);
            }
        });
        txtNoOfInstall.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNoOfInstallFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panInterestDeails1.add(txtNoOfInstall, gridBagConstraints);

        txtInstallAmount.setMaximumSize(new java.awt.Dimension(110, 21));
        txtInstallAmount.setMinimumSize(new java.awt.Dimension(110, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panInterestDeails1.add(txtInstallAmount, gridBagConstraints);

        lblInstallMentAmount.setText("Instllment Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 2);
        panInterestDeails1.add(lblInstallMentAmount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 14, 4);
        panSanctionDetails_Sanction.add(panInterestDeails1, gridBagConstraints);

        panInterestDeails2.setMinimumSize(new java.awt.Dimension(260, 190));
        panInterestDeails2.setPreferredSize(new java.awt.Dimension(260, 190));
        panInterestDeails2.setLayout(new java.awt.GridBagLayout());

        cboSanctionBy.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboSanctionBy.setMinimumSize(new java.awt.Dimension(100, 21));
        cboSanctionBy.setPopupWidth(200);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panInterestDeails2.add(cboSanctionBy, gridBagConstraints);

        lblSanctionBy.setText("Sanction By");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 2);
        panInterestDeails2.add(lblSanctionBy, gridBagConstraints);

        lblSanctionRef.setText("Sanction Ref");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 2);
        panInterestDeails2.add(lblSanctionRef, gridBagConstraints);

        lblAcctNo_Sanction.setText("Loan Account No :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 2);
        panInterestDeails2.add(lblAcctNo_Sanction, gridBagConstraints);

        lblAcctNo_Sanction_Disp.setForeground(new java.awt.Color(0, 51, 204));
        lblAcctNo_Sanction_Disp.setFont(new java.awt.Font("MS Sans Serif", 1, 15)); // NOI18N
        lblAcctNo_Sanction_Disp.setMaximumSize(new java.awt.Dimension(120, 15));
        lblAcctNo_Sanction_Disp.setMinimumSize(new java.awt.Dimension(120, 15));
        lblAcctNo_Sanction_Disp.setPreferredSize(new java.awt.Dimension(120, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panInterestDeails2.add(lblAcctNo_Sanction_Disp, gridBagConstraints);

        lblAccOpenDt.setText("Account Open Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 2);
        panInterestDeails2.add(lblAccOpenDt, gridBagConstraints);

        tdtAccountOpenDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtAccountOpenDate.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtAccountOpenDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtAccountOpenDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panInterestDeails2.add(tdtAccountOpenDate, gridBagConstraints);

        txtSanctionRef.setAllowAll(true);
        txtSanctionRef.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panInterestDeails2.add(txtSanctionRef, gridBagConstraints);

        lblMultiDisburseAllow.setText("Multi Disbursement");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panInterestDeails2.add(lblMultiDisburseAllow, gridBagConstraints);

        rdoMultiDisburseAllow.add(rdoMultiDisburseAllow_Yes);
        rdoMultiDisburseAllow_Yes.setText("Yes");
        rdoMultiDisburseAllow_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoMultiDisburseAllow_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInterestDeails2.add(rdoMultiDisburseAllow_Yes, gridBagConstraints);

        rdoMultiDisburseAllow.add(rdoMultiDisburseAllow_No);
        rdoMultiDisburseAllow_No.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        rdoMultiDisburseAllow_No.setText("No");
        rdoMultiDisburseAllow_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoMultiDisburseAllow_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, -120, 0, 0);
        panInterestDeails2.add(rdoMultiDisburseAllow_No, gridBagConstraints);

        lblAdditionalLoanfacility.setText("Additional Loan facility");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        panInterestDeails2.add(lblAdditionalLoanfacility, gridBagConstraints);

        rdoAdditionalFacilityGroup.add(rdoAdditionalLoanFacility_Yes);
        rdoAdditionalLoanFacility_Yes.setText("Yes");
        rdoAdditionalLoanFacility_Yes.setFocusCycleRoot(true);
        rdoAdditionalLoanFacility_Yes.setMinimumSize(new java.awt.Dimension(65, 21));
        rdoAdditionalLoanFacility_Yes.setPreferredSize(new java.awt.Dimension(65, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInterestDeails2.add(rdoAdditionalLoanFacility_Yes, gridBagConstraints);

        rdoAdditionalFacilityGroup.add(rdoAdditionalLoanFacility_No);
        rdoAdditionalLoanFacility_No.setText("No");
        rdoAdditionalLoanFacility_No.setMinimumSize(new java.awt.Dimension(65, 21));
        rdoAdditionalLoanFacility_No.setPreferredSize(new java.awt.Dimension(65, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, -5);
        panInterestDeails2.add(rdoAdditionalLoanFacility_No, gridBagConstraints);

        txtRemarks.setAllowAll(true);
        txtRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panInterestDeails2.add(txtRemarks, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        panInterestDeails2.add(lblRemarks, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 14, 3);
        panSanctionDetails_Sanction.add(panInterestDeails2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panSanctionDetails_Table.add(panSanctionDetails_Sanction, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panBorrowCompanyDetails.add(panSanctionDetails_Table, gridBagConstraints);

        tabLimitAmount.addTab("Customer/Sanction Details", panBorrowCompanyDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTermLoan.add(tabLimitAmount, gridBagConstraints);

        getContentPane().add(panTermLoan, java.awt.BorderLayout.CENTER);

        mnuProcess.setText("Process");
        mnuProcess.setMinimumSize(new java.awt.Dimension(73, 19));

        mitNew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        mitNew.setMnemonic('N');
        mitNew.setText("New");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        mitEdit.setMnemonic('E');
        mitEdit.setText("Edit");
        mitEdit.setEnabled(false);
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        mitDelete.setMnemonic('D');
        mitDelete.setText("Delete");
        mitDelete.setEnabled(false);
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptProcess);

        mitSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        mitSave.setMnemonic('S');
        mitSave.setText("Save");
        mitSave.setEnabled(false);
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setMnemonic('C');
        mitCancel.setText("Cancel");
        mitCancel.setEnabled(false);
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitAuthorize.setText("Authorize");
        mitAuthorize.setEnabled(false);
        mitAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitAuthorizeActionPerformed(evt);
            }
        });
        mnuProcess.add(mitAuthorize);

        mitException.setText("Exception");
        mitException.setEnabled(false);
        mitException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitExceptionActionPerformed(evt);
            }
        });
        mnuProcess.add(mitException);

        mitReject.setText("Rejection");
        mitReject.setEnabled(false);
        mitReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitRejectActionPerformed(evt);
            }
        });
        mnuProcess.add(mitReject);
        mnuProcess.add(sptException);

        mitPrint.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        mitPrint.setMnemonic('P');
        mitPrint.setText("Print");
        mitPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitPrintActionPerformed(evt);
            }
        });
        mnuProcess.add(mitPrint);
        mnuProcess.add(sptPrint);

        mitClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        mitClose.setMnemonic('l');
        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrTermLoan.add(mnuProcess);

        setJMenuBar(mbrTermLoan);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tdtAccountOpenDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtAccountOpenDateFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tdtAccountOpenDateFocusLost

    private void chkMobileBankingTLADActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkMobileBankingTLADActionPerformed
        // TODO add your handling code here:
         if(chkMobileBankingTLAD.isSelected()){
             if(lblCustomerIdValue.getText().length()>0){
                long mobileNo = observable.getMobileNo(CommonUtil.convertObjToStr(lblCustomerIdValue.getText()));
                if(mobileNo != 0){
                    txtMobileNo.setText(CommonUtil.convertObjToStr(mobileNo));
                    tdtMobileSubscribedFrom.setDateValue(CommonUtil.convertObjToStr(currDt.clone()));
                }
            }
            EnableDisbleMobileBanking(true);
         }else{
            EnableDisbleMobileBanking(false);
            txtMobileNo.setText("");
            tdtMobileSubscribedFrom.setDateValue("");
        }
    }//GEN-LAST:event_chkMobileBankingTLADActionPerformed
 private void EnableDisbleMobileBanking(boolean flag){
        txtMobileNo.setEnabled(flag);
        tdtMobileSubscribedFrom.setEnabled(flag);
    }

    private void txtCustIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCustIdFocusLost
        // TODO add your handling code here:    //Added By Suresh
        if(txtCustId.getText().length()>0 && observable.getProductAuthRemarks().equals("PADDY_LOAN")){
            String cust_id=CommonUtil.convertObjToStr(txtCustId.getText());
            List lst=null;
            HashMap executeMap=new HashMap();
            executeMap.put("BRANCH_CODE",getSelectedBranchID());
            executeMap.put("CUST_ID",new String(cust_id));
            viewType="PADDY_CUSTOMER_ID";
            lst=ClientUtil.executeQuery("getSelectCustListForLTD",executeMap);
            if(lst !=null && lst.size()>0){
                executeMap=(HashMap)lst.get(0);
                fillData(executeMap);
                lst=null;
                executeMap=null;
            }else{
                ClientUtil.displayAlert("Invalid Customer ID !!!");
                txtCustId.setText("");
            }
        }
    }//GEN-LAST:event_txtCustIdFocusLost

    private void btnCustIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustIDActionPerformed
        // TODO add your handling code here:
        if(observable.getProductAuthRemarks().equals("PADDY_LOAN")){
            viewType = "PADDY_CUSTOMER_ID";
            new CheckCustomerIdUI(this);
        }
    }//GEN-LAST:event_btnCustIDActionPerformed

    private void btnNew_BorrowerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNew_BorrowerActionPerformed
        // TODO add your handling code here:
      //  btnNew_Borrower
        if(checkTransaction())
              return;
        setAllBorrowerBtnsEnableDisable(true);
        observable.resetRemainingFields(false);
        btnDeleteBorrower.setEnabled(false);
        ClientUtil.enableDisable(this,true);
         productEnableDisable(false);
        
         btnCustomer(true);
         enableDisableInterest(false);
         newRecord=true;
        
    }//GEN-LAST:event_btnNew_BorrowerActionPerformed

    private void enableDisableInterest(boolean flag){
         tdtRepaymentDt.setEnabled(flag);
         txtInter.setEnabled(flag);
         tdtAccountOpenDate.setEnabled(flag);
    }
    private void btnDeleteBorrowerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteBorrowerActionPerformed
        // TODO add your handling code here:
        int yes = ClientUtil.confirmationAlert("Are you sure to Delete Deposit");        
        if (yes == 0) {
            if (checkTransaction()) {
                return;
            }
            if (observable.getProductAuthRemarks().equals("MDS_LOAN")) {
                observable.deleteTableMdsData(tblDepositDetails.getSelectedRow());                
            } else {
                observable.deleteTableData(tblDepositDetails.getSelectedRow());
            }
            observable.ttNotifyObservers();
            //Added by Jeffin John on 23/05/2014 for Mantis ID - 9159  
            txtDepositNo.setEnabled(true);
            btnDepositNo.setEnabled(true);
            cboDepositProduct.setEnabled(true);
        } else {
            return;
        }
    }//GEN-LAST:event_btnDeleteBorrowerActionPerformed

    private boolean checkTransaction(){
         HashMap map = new HashMap();
        if (CommonUtil.convertObjToStr(lblAcctNo_Sanction_Disp.getText()).length() > 0) {
            map.put("ACT_NUM", lblAcctNo_Sanction_Disp.getText());
            List lst = ClientUtil.executeQuery("checkTransaction", map);
            if (lst != null && lst.size() > 0) {
                map = (HashMap) lst.get(0);
                if (CommonUtil.convertObjToInt(map.get("CNT")) > 0) {
                    ClientUtil.displayAlert("This Loan Account Disbursement was completed!!!");                    
                    return true;
                }
            }
        }
        return false;
    }
    private void tblDepositDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDepositDetailsMouseClicked
        // TODO add your handling code here:
        if(!(observable.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE ||observable.getActionType()==ClientConstants.ACTIONTYPE_REJECT))
        setAllBorrowerBtnsEnableDisable(true);
    }//GEN-LAST:event_tblDepositDetailsMouseClicked

    private void cboDepositProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboDepositProductActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_cboDepositProductActionPerformed

    private void cboLoanProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLoanProductActionPerformed
        // TODO add your handling code here:
        //Added By Suresh
        if(cboLoanProduct.getSelectedIndex()>0){
            
            HashMap checkMap = new HashMap();
            checkMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            checkMap.put("PROD_ID", (String) ((ComboBoxModel) cboLoanProduct.getModel()).getKeyForSelected());
            List actList = (List) (ClientUtil.executeQuery("getAccountMaintenanceCount", checkMap));
            if (actList != null && actList.size() > 0) {
                checkMap = (HashMap) actList.get(0);
                int cnt = CommonUtil.convertObjToInt(checkMap.get("CNT"));
                if (cnt == 0) {
                    ClientUtil.displayAlert("Branch Account Number Settings Not Done. Please Check !!!");
                    btnCancelActionPerformed(null);
                }
            }
            
            String loanProduct_ID = (String)observable.getCbmLoanProduct().getKeyForSelected();
            HashMap whereMap = new HashMap();
            whereMap.put("PROD_ID", loanProduct_ID);
            List lstAuthRemark=ClientUtil.executeQuery("getLoanProductAuthRemarks",whereMap);
            if(lstAuthRemark !=null && lstAuthRemark.size()>0){
                whereMap = (HashMap)lstAuthRemark.get(0);
                observable.setProductAuthRemarks(CommonUtil.convertObjToStr(whereMap.get("AUTHORIZE_REMARK")));
        observable.getDepositProducts((String)observable.getCbmLoanProduct().getKeyForSelected());
        cboDepositProduct.setModel(observable.getCbmDepositProduct());
                if(observable.getProductAuthRemarks().equals("MDS_LOAN")){
                    tblDepositDetails.setModel(observable.getTblSanctionMainMds());
                    lblProductId1.setText("MDS Product");
                    lblDepositNo.setText("Chittal No");
                    panMDSLoanDetailFields.setVisible(false);
                    panBorrowerTabCTable.setVisible(true);
                    panPaddyLoanDetailFields.setVisible(false);
                    panBorrowerTabTools.setVisible(true);
                    observable .setLblShowAccountHeadId(CommonUtil.convertObjToStr(whereMap.get("ACCT_HEAD")));
                    lblShowAccountHeadId.setText(CommonUtil.convertObjToStr(whereMap.get("ACCT_HEAD")));
                }else if(observable.getProductAuthRemarks().equals("PADDY_LOAN")){
                    lblProductId1.setText("Paddy Product");
                    lblDepositNo.setText("Paddy No");
                    panMDSLoanDetailFields.setVisible(false);
                    panPaddyLoanDetailFields.setVisible(true);
                    panBorrowerTabCTable.setVisible(false);
                    panBorrowerTabTools.setVisible(false);
                    observable .setLblShowAccountHeadId(CommonUtil.convertObjToStr(whereMap.get("ACCT_HEAD")));
                    lblShowAccountHeadId.setText(CommonUtil.convertObjToStr(whereMap.get("ACCT_HEAD")));
                }else if(observable.getProductAuthRemarks().equals("OTHER_LOAN")){
                    tblDepositDetails.setModel(observable.getTblSanctionMain());
                    lblProductId1.setText("Deposit Product");
                    lblDepositNo.setText("Deposit No");
                    panMDSLoanDetailFields.setVisible(false);
                    panPaddyLoanDetailFields.setVisible(false);
                    panBorrowerTabCTable.setVisible(true);
                    panBorrowerTabTools.setVisible(true);
                }
            }
        }
      
        if (cboLoanProduct.getSelectedIndex() > 0) {
            txtNextAccNo.setText("");
            String loanProduct_ID2 = (String) observable.getCbmLoanProduct().getKeyForSelected();
            //System.out.println("loanProduct_ID2========"+loanProduct_ID2);
            List chargeList = null;
            HashMap whereMap1 = new HashMap();//cboLoanProduct.getModel()
            whereMap1.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            whereMap1.put("PRODUCT_ID", loanProduct_ID2);
            chargeList = (List) (ClientUtil.executeQuery("getSelectNextAccNo", whereMap1));
            if (chargeList != null && chargeList.size() > 0) {
                accountClosingCharge = CommonUtil.convertObjToStr((chargeList.get(0)));
                //System.out.println("accountClosingCharge======"+accountClosingCharge);
                txtNextAccNo.setText(String.valueOf(accountClosingCharge));
            }
            chargeList = null;

        }
        
        
    }//GEN-LAST:event_cboLoanProductActionPerformed
    private void clearMDSDetails(){
        lblMDSChitAmountPaidVal.setText("");
        lblMDSMemberNameVal.setText("");
        lblMDSMemberNoVal.setText("");
        lblMDSMemberTypeVal.setText("");
        lblIntrestAmt.setText("");
    }

    private void clearPaddyDetails(){
        lblPurchaseAmountVal.setText("");
        lblPurchaseDateVal.setText("");
        lblPurchaseIDVal.setText("");
        lblPurchaseNameVal.setText("");
        lblTransactionDateVal.setText("");
        lblTotalWeightVal.setText("");
        lblAcreageVal.setText("");
    }
    private void btnSave_BorrowerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSave_BorrowerActionPerformed
        // TODO add your handling code here:
        HashMap noIfDepMap = new HashMap();
        List noOfLoanlist = null;
        boolean isLoanExsist = false;
        if (CommonUtil.convertObjToStr(txtDepositNo.getText()).length() > 0) {
        noIfDepMap.put("DEPOSIT_NUM", CommonUtil.convertObjToStr(txtDepositNo.getText()));
        noOfLoanlist = ClientUtil.executeQuery("getNoOfLoan", noIfDepMap);
        }
        if(noOfLoanlist != null && noOfLoanlist.size() > 0){
            noIfDepMap = (HashMap)noOfLoanlist.get(0);
            isLoanExsist = true;
        }
        //if(noIfDepMap != null && noIfDepMap.containsKey("COUNT") && noIfDepMap.containsKey("LIEN_AC_NO") && noIfDepMap.get("COUNT") != null && noIfDepMap.get("LIEN_AC_NO") != null){
        //    if(CommonUtil.convertObjToInt(noIfDepMap.get("COUNT")) >=1){
        //        isLoanExsist = true;
        //    }
        //}
        HashMap wheremap = new HashMap();
        HashMap resultMap = new HashMap();
        wheremap.put("PROD_DESC", CommonUtil.convertObjToStr(cboLoanProduct.getSelectedItem()));
        List list = ClientUtil.executeQuery("getDepositLoanAuthRemark", wheremap);
        resultMap = (HashMap) list.get(0);
        //System.out.println("resultMap=" + resultMap);
        //    if(cboLoanProduct.getSelectedItem().equals("Deposit Loan"))
        if (resultMap.containsKey("AUTHORIZE_REMARK") && resultMap != null && resultMap.size() > 0) {
            if (resultMap.get("AUTHORIZE_REMARK").equals("OTHER_LOAN")) {
                if (CommonUtil.convertObjToStr(txtDepositNo.getText()).length() > 0) {
                    if(isLoanExsist){
                    ClientUtil.showMessageWindow("This Deposit is lien marked for Loan no. "+CommonUtil.convertObjToStr(noIfDepMap.get("LIEN_AC_NO")+"\n"
                            + "Available balance is : "+CommonUtil.convertObjToStr(noIfDepMap.get("AVAILABLE_BALANCE")))); 
                    //}else{
                    }
                      savetoTable("OTHER_LOAN");
                    //}
                    isLoanExsist=false;
                    if (!(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW)) {
                        newRecord = false;
                    }
                } else {
                    ClientUtil.showMessageWindow("Please Select Deposit Number .Then Press save");
                    return;

                }
            }
            System.out.println("cboLoanProduct.getSelectedItem()@@11>>>>" + cboLoanProduct.getSelectedItem());
//        if(cboLoanProduct.getSelectedItem().equals("MDS LOAN"))
            if (resultMap.get("AUTHORIZE_REMARK").equals("MDS_LOAN")) //   if(cboLoanProduct.getSelectedItem().equals("MDS LOAN"))
            {
                System.out.println("txtDepositNo.getText()11@@>>>>" + txtDepositNo.getText());
                if (CommonUtil.convertObjToStr(txtDepositNo.getText()).length() > 0) {


                    savetoTable("MDS_LOAN");
                    if (!(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW)) {
                        newRecord = false;
                    }
                } else {
                    ClientUtil.showMessageWindow("Please Select Chittal Number .Then Press save");
                    return;

                }
            }
        }
    }//GEN-LAST:event_btnSave_BorrowerActionPerformed
    
    private void savetoTable(String Type){
        HashMap wheremap = new HashMap();

        if (Type != null) {
            if (Type.equals("OTHER_LOAN")) {
                int result = 0;
                if (multipleDeposit) {
                    result = ClientUtil.confirmationAlert("Do You Want To Select  Multiple Deposits for This Loan");
                    //System.out.println("resultOTHER"+result);
                    if (result == 0) {
                        multipleDeposit = false;
                    }else{
                        updateOBFields();
                        observable.setTableData(true);
                        productEnableDisable(false);
                        updateOBFields();
                        txtDepositNo.setEnabled(false);
                        btnDepositNo.setEnabled(false);
                    }
                } else {
                    result = 0;
                }
                if (result == 0) {
                    updateOBFields();
                    observable.setTableData(true);
                    productEnableDisable(false);
                    cboDepositProduct.setEnabled(true);

                    //            callView("CUSTOMER ID");
                }
                tblDepositDetails.setModel(observable.getTblSanctionMain());
            }
            if (Type.equals("MDS_LOAN")) {
                int result = 0;
                System.out.println("multipleDeposit" + multipleDeposit);
                if (multipleDeposit) {
                    result = ClientUtil.confirmationAlert("Do You Want To Select  Multiple Deposits for This MDS Loan");
                    System.out.println("resultMDS"+result);
                    if (result == 0) {
                        multipleDeposit = false;
                    }else{
                        updateOBFields();
                        observable.setTableData1(true);
                        productEnableDisable(false);
                        updateOBFields();
                        txtDepositNo.setEnabled(false);
                        btnDepositNo.setEnabled(false);
                    }
                } else {
                    result = 0;
                }
                if (result == 0) {
                    updateOBFields();
                    observable.setTableData1(true);
                    productEnableDisable(false);
                    cboDepositProduct.setEnabled(true);

                    //            callView("CUSTOMER ID");
                }
                tblDepositDetails.setModel(observable.getTblSanctionMainMds());
            }
        }
    }
    private void productEnableDisable(boolean flag){
         cboLoanProduct.setEnabled(flag);
         cboDepositProduct.setEnabled(flag);
     }
    private void calcEligibleLoanAmount() {
        //        txtMarketRate.setText(String.valueOf(perGramAmt));
        //        totSecurityValue = perGramAmt * CommonUtil.convertObjToDouble(txtNetWeight.getText()).doubleValue();
        //        txtSecurityValue.setText(String.valueOf(totSecurityValue));
        //        double margin = CommonUtil.convertObjToDouble(txtMargin.getText()).doubleValue();
        //        totMarginAmt = ((margin * CommonUtil.convertObjToDouble(txtSecurityValue.getText()).doubleValue())/100);
        //        totEligibleAmt = CommonUtil.convertObjToDouble(txtSecurityValue.getText()).doubleValue() - totMarginAmt;
        //        txtMarginAmt.setText(String.valueOf(totMarginAmt));
        //        txtEligibleLoan.setText(String.valueOf(totEligibleAmt));
    }
    
    
    
    
    
    private void txtDepositNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDepositNoFocusLost
        // TODO add your handling code here:
        String depositNo=CommonUtil.convertObjToStr(txtDepositNo.getText());
        List lst=null;
        HashMap executeMap=new HashMap();
        HashMap depositMap=new HashMap();
        if(depositNo !=null && (!depositNo.equals(""))){
            depositMap.put("BRANCH_CODE",getSelectedBranchID());
            depositMap.put("DEPOSIT_NO",new String(depositNo));
            executeMap.put(CommonConstants.MAP_WHERE,depositMap);
            System.out.println("observable.getProductAuthRemarks()>>>"+observable.getProductAuthRemarks());
            if(observable.getProductAuthRemarks().equals("MDS_LOAN")){
                viewType="MDS_CUSTOMER";
                depositMap.put("MDS_PROD_ID",CommonUtil.convertObjToStr(observable.getCbmDepositProduct().getKeyForSelected()));
                if (depositNo.indexOf("_")!=-1) {
                    depositNo = depositNo.substring(0,depositNo.indexOf("_"));
                }
                depositMap.put("CHITTAL_NUMBER",depositNo);
                lst=ClientUtil.executeQuery("getSelectMDSCustDetails",depositMap);
            }else if(observable.getProductAuthRemarks().equals("PADDY_LOAN")){
                viewType="PADDY_CUSTOMER";
                depositMap.put("PADDY_PROD_ID",CommonUtil.convertObjToStr(observable.getCbmDepositProduct().getKeyForSelected()));
                depositMap.put("PADDY_NO",new String(depositNo));
                lst=ClientUtil.executeQuery("getSelectPaddyCustDetails",depositMap);
            }else if(observable.getProductAuthRemarks().equals("OTHER_LOAN")){
                viewType="DEPOSIT ID";
                if(getListTableDepositNo(depositNo)){
                    return;
                }
            lst=ClientUtil.executeQuery("getLoanProductFromDepositProduct",depositMap);
            }
            if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW){
                HashMap lienExistornotMap = new HashMap();
                lienExistornotMap.put("DEPOSIT_NO",depositMap.get("DEPOSIT_NO"));
                List lienLst = ClientUtil.executeQuery("checkDepositLienDetails", lienExistornotMap);
                if (lienLst!=null && lienLst.size() > 0) {
                    ClientUtil.showAlertWindow("Loan already Created for this Deposit and Pending for Authorization !!!");
                    txtDepositNo.setText("");
                    return;
                }
            }
            if(lst !=null && lst.size()>0){
                executeMap=(HashMap)lst.get(0);
                fillData(executeMap);
                lst=null;
                executeMap=null;
            }else{
                if(observable.getProductAuthRemarks().equals("MDS_LOAN")){
                    ClientUtil.showAlertWindow("Invalid Chittal Number");
                    clearMDSDetails();
                }else if(observable.getProductAuthRemarks().equals("PADDY_LOAN")){
                    ClientUtil.showAlertWindow("Invalid Paddy Number");
                    clearPaddyDetails();
                }else if(observable.getProductAuthRemarks().equals("OTHER_LOAN")){
                    ClientUtil.showAlertWindow("Invalid Deposit Number");
                }
                txtDepositNo.setText("");
            }
        }
        observable.setFacilityAcctHead();
    }//GEN-LAST:event_txtDepositNoFocusLost
    private boolean getListTableDepositNo(String depositNo){
        StringBuffer presentCust = new StringBuffer();
            String  maturityDt= "";
            double rateInt =0;
            int jntAccntTablRow = tblDepositDetails.getRowCount();
            if(tblDepositDetails.getRowCount()!=0) {
            for(int i =0, sizeJointAcctAll = tblDepositDetails.getRowCount();i<sizeJointAcctAll;i++){
//            if(i==0 || i==sizeJointAcctAll){
                presentCust.append("'" + CommonUtil.convertObjToStr(tblDepositDetails.getValueAt(i, 0)) + "'");
                if(depositNo.equals(CommonUtil.convertObjToStr(tblDepositDetails.getValueAt(i, 0)))){
                    ClientUtil.showMessageWindow("This Deposit No   : " +depositNo+"   Already Selected "+"\n"+
                    "Please Select Another Deposit");
                    txtDepositNo.setText("");
                    return true;
                }
//                maturityDt=CommonUtil.convertObjToStr(tblDepositDetails.getValueAt(i, 5));
//                rateInt =CommonUtil.convertObjToDouble(tblDepositDetails.getValueAt(i, 2)).doubleValue();
//            } 
//                else{
//                presentCust.append("," + "'" + CommonUtil.convertObjToStr(tblDepositDetails.getValueAt(i, 0)) + "'");
//            }
            }
            }  
            return false;
    }
    
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        
        btnCheck();
        //        btnAppraiserId.setEnabled(false);
        
    }//GEN-LAST:event_btnViewActionPerformed
    
    private void txtDepositNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDepositNoActionPerformed
        
            
        
       
    }//GEN-LAST:event_txtDepositNoActionPerformed
      private boolean populateInterestRateForLTD(HashMap depositIntMap) {
            HashMap whereMap = new HashMap();
            whereMap.put("CATEGORY_ID", CommonUtil.convertObjToStr(depositIntMap.get("CATEGORY")));
            if(CommonUtil.convertObjToDouble(observable.getTxtLoanAmt()).doubleValue()>0)
                whereMap.put("AMOUNT", getBigDecimal(CommonUtil.convertObjToDouble(observable.getTxtLoanAmt()).doubleValue()));
            else
                whereMap.put("AMOUNT", getBigDecimal(CommonUtil.convertObjToDouble(txtLoanAmt.getText()).doubleValue()));
            whereMap.put("PROD_ID", CommonUtil.convertObjToStr(observable.getCbmLoanProduct().getKeyForSelected()));//observable.getLblProductID_FD_Disp());
            if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW)
              //  whereMap.put("FROM_DATE", getTimestamp(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(curr_dt))));
                 whereMap.put("FROM_DATE", setProperDtFormat(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(curr_dt))));
            else
             //   whereMap.put("FROM_DATE", getTimestamp(DateUtil.getDateMMDDYYYY(tdtAccountOpenDate.getDateValue())));
            //whereMap.put("TO_DATE", getTimestamp((Date)depositIntMap.get("MATURITY_DT")));//DateUtil.getDateMMDDYYYY(observable.getTdtRepaymentDt())));hash.get("MATURITY_DT")
                 whereMap.put("FROM_DATE", setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtAccountOpenDate.getDateValue())));
            whereMap.put("TO_DATE", setProperDtFormat(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositIntMap.get("MATURITY_DT")))));//DateUtil.getDateMMDDYYYY(observable.getTdtRepaymentDt())));hash.get("MATURITY_DT")
            
            // Populate the values
            ArrayList interestList = (java.util.ArrayList)ClientUtil.executeQuery("getSelectProductTermLoanInterestTO", whereMap);
            if (interestList!=null && interestList.size()>0) {
                if(observable.setTermLoanInterestTO(interestList, depositIntMap,true))
                    return true;
            }
            else {
                displayAlert("Interest rates not created for this product...");
                return true;
            }
        return false;
    }
      
   
    
    private boolean checkInterestRateForLTD() {
        HashMap whereMap = new HashMap();
        whereMap.put("CATEGORY_ID", observable.getCbmCategory().getKeyForSelected());
        if(CommonUtil.convertObjToDouble(observable.getTxtLoanAmt()).doubleValue()>0)
            whereMap.put("AMOUNT", getBigDecimal(CommonUtil.convertObjToDouble(observable.getTxtLoanAmt()).doubleValue()));
        else
            whereMap.put("AMOUNT", getBigDecimal(CommonUtil.convertObjToDouble(txtLoanAmt.getText()).doubleValue()));
        whereMap.put("PROD_ID", CommonUtil.convertObjToStr(observable.getCbmLoanProduct().getKeyForSelected()));//observable.getLblProductID_FD_Disp());
        //whereMap.put("FROM_DATE", getTimestamp(DateUtil.getDateMMDDYYYY(tdtAccountOpenDate.getDateValue())));
        //whereMap.put("TO_DATE", getTimestamp(DateUtil.getDateMMDDYYYY(tdtRepaymentDt.getDateValue())));
         whereMap.put("FROM_DATE", setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtAccountOpenDate.getDateValue())));
        whereMap.put("TO_DATE", setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtRepaymentDt.getDateValue())));
        //            deleteAllInterestDetails();
        //            observableInt.resetInterestDetails();
        //            updateInterestDetails();
        // Populate the values
        ArrayList interestList = (java.util.ArrayList)ClientUtil.executeQuery("getSelectProductTermLoanInterestTO", whereMap);
//        observableInt.setIsNew(true);
        if (interestList==null || interestList.size()==0) {
            displayAlert("Interest rates not created for this product...");
            return true;
        }
        return false;
    }
    private Date setProperDtFormat(Date dt) {
        Date tempDt=(Date)currDt.clone();
        if(dt!=null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }
    private void txtCustIDActionPerform() {
        
        
        // TODO add your handling code here:
        
    }
    
    
    private void mitRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitRejectActionPerformed
        // TODO add your handling code here:
        btnRejectActionPerformed(evt);
    }//GEN-LAST:event_mitRejectActionPerformed
    
    private void mitExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitExceptionActionPerformed
        // TODO add your handling code here:
        btnExceptionActionPerformed(evt);
    }//GEN-LAST:event_mitExceptionActionPerformed
    
    private void mitAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitAuthorizeActionPerformed
        // TODO add your handling code here:
        btnAuthorizeActionPerformed(evt);
    }//GEN-LAST:event_mitAuthorizeActionPerformed
    
    private void btnCheck(){
        btnCancel.setEnabled(true);
        btnSave.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnEdit.setEnabled(false);
    }
    
    
    private void chkNPAChrgADActionPerformed() {
        /* we have to show the NPA charge date only if the corresponding
         * check box has been selected
         */
        //        tdtNPAChrgAD.setEnabled(chkNPAChrgAD.isSelected());
        //        tdtNPAChrgAD.setDateValue("");
        //        observableOtherDetails.setTdtNPAChrgAD("");
    }    private void chkABBChrgADActionPerformed() {
        /* we have to show the ABBA charge text box only if the corresponding
         * check box has been selected
         */
        //        boolean isSelected = chkABBChrgAD.isSelected();
        //        txtABBChrgAD.setEditable(isSelected);
        //        txtABBChrgAD.setEnabled(isSelected);
        //        if(!isSelected){
        //            txtABBChrgAD.setText("");
        //            observableOtherDetails.setTxtABBChrgAD("");
        //        }
    }    private void chkNonMainMinBalChrgADActionPerformed(){
        /* we have to show the Non maintenance of minimum balance charge text box
         * only if the corresponding check box has been selected
         */
        //        boolean isSelected = chkNonMainMinBalChrgAD.isSelected();
        //        txtMinActBalanceAD.setEditable(isSelected);
        //        txtMinActBalanceAD.setEnabled(isSelected);
        if(!isSelected){
            //            txtMinActBalanceAD.setText("");
            //            observableOtherDetails.setTxtMinActBalanceAD("");
        }
    }                            private void chkCreditADActionPerformed() {
        /* we have to enable the Credit card No., text field and the validity date
         * only when the user selected the credit card option
         */
        //        boolean isSelected = chkCreditAD.isSelected();
        //        txtCreditNoAD.setEditable(isSelected);
        //        txtCreditNoAD.setEnabled(isSelected);
        //        tdtCreditFromDateAD.setEnabled(isSelected);
        //        tdtCreditToDateAD.setEnabled(isSelected);
        //        chkCreditAD.setEnabled(isSelected);
        if(!isSelected){
            //            observableOtherDetails.setTxtCreditNoAD("");
            //            observableOtherDetails.setTdtCreditToDateAD("");
            //            observableOtherDetails.setTdtCreditFromDateAD("");
            //            txtCreditNoAD.setText("");
            //            tdtCreditFromDateAD.setDateValue("");
            //            tdtCreditToDateAD.setDateValue("");
        }
    }    private void chkDebitADActionPerformed() {
        /* we have to enable the debit card No., text field and the validity date
         * only when the user selected the debit card option
         */
        //        boolean isSelected = chkDebitAD.isSelected();
        //        txtDebitNoAD.setEditable(isSelected);
        //        txtDebitNoAD.setEnabled(isSelected);
        //        tdtDebitToDateAD.setEnabled(isSelected);
        //        tdtDebitFromDateAD.setEnabled(isSelected);
        //        chkDebitAD.setEnabled(isSelected);
        if(!isSelected){
            //            observableOtherDetails.setTxtDebitNoAD("");
            //            observableOtherDetails.setTdtDebitToDateAD("");
            //            observableOtherDetails.setTdtDebitFromDateAD("");
            //            txtDebitNoAD.setText("");
            //            tdtDebitToDateAD.setDateValue("");
            //            tdtDebitFromDateAD.setDateValue("");
        }
    }
    
    private java.sql.Timestamp getTimestamp(java.util.Date date){
       // return new java.sql.Timestamp(date.getYear(), date.getMonth(), date.getDate(), date.getHours(), date.getMinutes(), date.getSeconds(), 0);
        return new java.sql.Timestamp(date.getYear(), date.getMonth(), date.getDate(), 0, 0, 0, 0); //added by rajesh for polpully
    }
    
    private java.math.BigDecimal getBigDecimal(double doubleValue){
        return new java.math.BigDecimal(doubleValue);
    }
    
    
    
    
    
    
    
    private void txtLoanAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLoanAmtFocusLost
        // TODO add your handling code here:
        
        // Added by nithya on 29-07-2016
        if (observable.getProductAuthRemarks().equals("MDS_LOAN")) {            
            updateOBFields();
            HashMap depositIntMap = new HashMap();
            depositIntMap.put("CATEGORY", "GENERAL_CATEGORY");
            depositIntMap.put("DEPOSIT_INT", new Double(0));
            Date maturityDt =  DateUtil.getDateMMDDYYYY(observable.getTdtRepaymentDt());            
            depositIntMap.put("MATURITY_DT", maturityDt);
            populateInterestRateForLTD(depositIntMap);
            observable.ttNotifyObservers();
        }
        // End
        
           String mainLimit=CommonUtil.convertObjToStr(txtLoanAmt.getText());
           
//           observable.setFacilityAcctHead();
        //deposit lien shoude be changed while limit is changed
//        if(loanType.equals("LTD")) {
//            if (observable.getProductCategory().equals("OTHER_LOAN")) {//&& observable.getStrACNumber().length()>0)
//                if(mainLimitMarginValidation(mainLimit))
//                    return;
//            } else {
                if (observable.getEligibleAmt() < CommonUtil.convertObjToDouble(mainLimit).doubleValue()) {
                    System.out.println("observable.getEligibleAmt()222>>>>"+observable.getEligibleAmt());
                    ClientUtil.showAlertWindow("Limit amount should not exceed "+observable.getEligibleAmt());
                    txtLoanAmt.setText(String.valueOf(observable.getEligibleAmt()));
                     
                    return;
                }
//            }
//            morotoriumEnableDisable(false);
//        }
        //
        rupeesValidation(mainLimit);
        if(observable.getProductAuthRemarks().equals("OTHER_LOAN"))
        txtLimit_SDFocusLost();
        //od balance check
      
//        if(!CommonUtil.convertObjToStr(txtLimit_SD.getText()).equals(CommonUtil.convertObjToStr(observable.getTxtLimit_SD())))
//            if(loanType.equals("LTD")&& tblRepaymentCTable.getRowCount()>0 && (observable.getActionType()==ClientConstants.ACTIONTYPE_NEW ||
//            observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT))
//                if (tblRepaymentCTable.getRowCount()>0) {
//                    sanMousePress=true;
//                    updateOBFields();
//                    tblRepaymentCTableMousePressed();
//                    btnRepayment_DeleteActionPerformed();
//                    btnRepayment_NewActionPerformed();
//                    btnEMI_CalculateActionPerformed();
//                    btnRepayment_SaveActionPerformed();
//                    //            ClientUtil.displayAlert("Change the sanction Details so Create new Repayment schdule");
//                    //            tabLimitAmount.setSelectedComponent(panRepaymentSchedule);
//                    //
//                }
        
        
//        if(!loanType.equals("LTD") && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
//            if(CommonUtil.convertObjToDouble(txtLimit_SD.getText()).doubleValue()>0){
//                double shareSanctionAmt =0.0;
//                double totalShareSanctionAmt =0.0;
//                double finalTotalSanctionAmt =0.0;
//                HashMap shareMap = new HashMap();
//                String loanType ="";
//                String displayStr = "";
//                shareMap.put("CUST_ID",txtCustID.getText());
//                List shareLimitLst =ClientUtil.executeQuery("getShareLoanLimitPercentage", shareMap);
//                if(shareLimitLst!=null && shareLimitLst.size()>0){
//                    for(int i=0;i<shareLimitLst.size();i++){
//                        double percentage =0.0;
//                        double totalSanctionAmt =0.0;
//                        shareMap = (HashMap)shareLimitLst.get(i);
//                        percentage = CommonUtil.convertObjToDouble(shareMap.get("BORROWER_SHARE_PERCENTAGE")).doubleValue();
//                        loanType = CommonUtil.convertObjToStr(shareMap.get("LOAN_TYPE"));
//                        if(loanType.equals("OTHER_LOAN")){
//                            totalSanctionAmt = CommonUtil.convertObjToDouble(txtLimit_SD.getText()).doubleValue();
//                        }
//                        HashMap loanSanctionMap = new HashMap();
//                        loanSanctionMap.put("CUST_ID",txtCustID.getText());
//                        loanSanctionMap.put("AUTHORIZE_REMARK",loanType);
//                        List loanSanctionLst =ClientUtil.executeQuery("getTotalSanctionAmount", loanSanctionMap);
//                        if(loanSanctionLst!=null && loanSanctionLst.size()>0){
//                            loanSanctionMap = new HashMap();
//                            for(int j=0;j<loanSanctionLst.size();j++){
//                                loanSanctionMap = (HashMap)loanSanctionLst.get(j);
//                                totalSanctionAmt= totalSanctionAmt + CommonUtil.convertObjToDouble(loanSanctionMap.get("SANCTION_AMOUNT")).doubleValue();
//                                displayStr += "Existing Loan No  : "+loanSanctionMap.get("ACCT_NUM")+ "\n"+
//                                "Limit                     : Rs "+loanSanctionMap.get("SANCTION_AMOUNT")+"\n";
//                            }
//                            finalTotalSanctionAmt = finalTotalSanctionAmt + totalSanctionAmt;
//                            shareSanctionAmt = totalSanctionAmt*percentage/100;
//                            
//                        }
//                        totalShareSanctionAmt = totalShareSanctionAmt+shareSanctionAmt;
//                        
//                    }
//                    if(finalTotalSanctionAmt>0){
//                        finalTotalSanctionAmt = finalTotalSanctionAmt-CommonUtil.convertObjToDouble(txtLimit_SD.getText()).doubleValue();
//                    }
//                    double shortFallAmt =0.0;
//                    shortFallAmt = totalShareSanctionAmt-CommonUtil.convertObjToDouble(txtTotalShareAmount.getText()).doubleValue();
//                    Rounding rod =new Rounding();
//                    shortFallAmt = (double)rod.getNearest((long)(shortFallAmt *100),100)/100;
//                    if(CommonUtil.convertObjToDouble(txtTotalShareAmount.getText()).doubleValue() < totalShareSanctionAmt){
//                        displayStr +=     "Total Limits                                      : Rs "+finalTotalSanctionAmt +"\n";
//                        displayStr +=     "Share Value to be Subscribed         : Rs "+totalShareSanctionAmt+"\n";
//                        displayStr +=     "Present Share Amount Subscribed : Rs "+txtTotalShareAmount.getText()+"\n";
//                        displayStr +=     "Shortfall                                          : Rs "+shortFallAmt;
//                        
//                        if(!displayStr.equals("")){
//                            ClientUtil.showMessageWindow(""+displayStr);
//                        }
//                        int c = ClientUtil.confirmationAlert("Do you want to Continue");
//                        int d= 0;
//                        System.out.println("####### Yes/No : "+c);
//                        if(c!=d)
//                            return;
//                    }
//                }
//            }
//        }
                        //charge details
        //Added By Suresh
        if(txtLoanAmt.getText()!=null && !txtLoanAmt.getText().equals("")){
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){    // Loan Charges
            prodDesc1 = CommonUtil.convertObjToStr(cboLoanProduct.getModel().getSelectedItem());
            System.out.println("#### prodDesc1>>>"+prodDesc1);
            chrgTableEnableDisable();
            createChargeTable(prodDesc1);
            chargeAmount();
          //  txtLoanAmts.setText(txtLoanAmt.getText());
           
        }
//         if(CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")){
//             if(!cboSchemName.getSelectedItem().equals("") && cboSchemName.getSelectedItem().toString()!=null){
//                 System.out.println("hiiiiiiii");
//                 chkEligibleAmount();
//             }
//         }
       
        }
    }//GEN-LAST:event_txtLoanAmtFocusLost
    
    //charge details
    
        private void chargeAmount(){
        HashMap appraiserMap = new HashMap();
        appraiserMap.put("SCHEME_ID",prodDesc1);
        appraiserMap.put("DEDUCTION_ACCU","O");
        chargelst = ClientUtil.executeQuery("getAllChargeDetailsData", appraiserMap);
        HashMap chargeMap = new HashMap();
            //System.out.println("chargelistttt>>>@@>>>>"+chargelst);
        //11-06-2020
        serviceTaxApplMap = new HashMap();
        serviceTaxIdMap = new HashMap();
        // End
        if(chargelst!=null && chargelst.size()>0){
            for(int i=0; i<chargelst.size(); i++){
                String accHead="";
                String chargeId = "";
                chargeMap = (HashMap)chargelst.get(i);
                accHead = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_DESC"));
                chargeId = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_ID"));
                 if (TrueTransactMain.SERVICE_TAX_REQ.equalsIgnoreCase("Y")) {
                    String accHead_No = CommonUtil.convertObjToStr(chargeMap.get("ACC_HEAD"));
                    if (accHead_No != null && accHead_No.length() > 0) {
                        HashMap whereMap = new HashMap();
                        whereMap.put("AC_HD_ID", accHead_No);
                        List accHeadList = ClientUtil.executeQuery("getCheckServiceTaxApplicableForShare", whereMap);
                        if (accHeadList != null && accHeadList.size() > 0) {
                            HashMap accHeadMap = (HashMap) accHeadList.get(0);
                            if (accHeadMap != null && accHeadMap.containsKey("SERVICE_TAX_APPLICABLE")) {
                            String   checkFlag = CommonUtil.convertObjToStr(accHeadMap.get("SERVICE_TAX_APPLICABLE"));
                            String serviceTaxId = CommonUtil.convertObjToStr(accHeadMap.get("SERVICE_TAX_ID"));
                            serviceTaxApplMap.put(chargeId,checkFlag);
                            serviceTaxIdMap.put(chargeId,serviceTaxId);
                            }
                        }
                    }
                }
                 System.out.println("serviceTaxApplMap in charge amount:: " + serviceTaxApplMap);
                 System.out.println("serviceTaxIdMap in charge amount ::  " + serviceTaxIdMap);
               // System.out.println("$#@@$ accHead"+accHead);
                for(int j=0; j<table.getRowCount();j++) {
                    System.out.println("$#@@$ accHead inside table "+table.getValueAt(j, 2));
                    if (CommonUtil.convertObjToStr(table.getValueAt(j, 2)).equals(accHead)) {
                        double chargeAmt = 0;
                        if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Sanction Amount")) {
                            chargeAmt = CommonUtil.convertObjToDouble(txtLoanAmt.getText()).doubleValue() *
                            CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(chargeMap.get("CHARGE_RATE"))).doubleValue()/100;
                            long roundOffType = getRoundOffType(CommonUtil.convertObjToStr(chargeMap.get("ROUND_OFF_TYPE")));
                            if (roundOffType!=0) {
                                chargeAmt = rd.getNearest((long)(chargeAmt*roundOffType), roundOffType)/roundOffType;
                            }
                            double minAmt = CommonUtil.convertObjToDouble(chargeMap.get("MIN_CHARGE_AMOUNT")).doubleValue();
                            double maxAmt = CommonUtil.convertObjToDouble(chargeMap.get("MAX_CHARGE_AMOUNT")).doubleValue();
                            if (chargeAmt<minAmt) {
                                chargeAmt = minAmt;
                            }
                            if (chargeAmt>maxAmt) {
                                chargeAmt = maxAmt;
                            }
                            table.setValueAt(String.valueOf(chargeAmt),j, 3);
                        }else if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Amount Range")) {
                            chargeAmt = CommonUtil.convertObjToDouble(txtLoanAmt.getText()).doubleValue() *
                            CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(chargeMap.get("CHARGE_RATE"))).doubleValue()/100;
                            table.setValueAt(String.valueOf(chargeAmt),j, 3);
                        }else if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Flat Charge")) {
                            chargeAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(chargeMap.get("FLAT_CHARGE"))).doubleValue();
                        }
                        chargeMap.put("CHARGE_AMOUNT",String.valueOf(chargeAmt));
                    }
                }
            }
            System.out.println("#$#$$# chargeMap:"+chargeMap);
           // System.out.println("#$#$$# chargelst:"+chargelst);
           // editChargeTable();
            table.revalidate();
            table.updateUI();
            selectMode = true;//charge details, calculating total of selected amount
        
              System.out.println("serviceTaxIdMap :: " + serviceTaxIdMap);
            //serviceTaxIdMap :: {CHRG000001=STG00005, CHRG000002=STG00005}
            if (serviceTaxApplMap != null && serviceTaxApplMap.size() > 0) {                
                List taxSettingsList = new ArrayList();                
                for (int i = 0; i < table.getRowCount(); i++) {
                    double chrgamt = 0;
                    HashMap serviceTaSettingsMap = new HashMap();
                    boolean checkFlag = new Boolean(CommonUtil.convertObjToStr(table.getValueAt(i, 0))).booleanValue();
                    String descVal = CommonUtil.convertObjToStr(table.getValueAt(i, 1));
                    if (checkFlag && CommonUtil.convertObjToStr(serviceTaxApplMap.get(descVal)).equals("Y") && CommonUtil.convertObjToStr(serviceTaxIdMap.get(descVal)).length() > 0) {
                        chrgamt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(table.getValueAt(i, 3)));
                    }
                    if (chrgamt > 0) {   
                        serviceTaSettingsMap.put("SETTINGS_ID",serviceTaxIdMap.get(descVal));
                        serviceTaSettingsMap.put(ServiceTaxCalculation.TOT_AMOUNT,CommonUtil.convertObjToStr(chrgamt));
                        //serviceTaSettingsMap.put(serviceTaxIdMap.get(descVal), CommonUtil.convertObjToStr(chrgamt));    
                        taxSettingsList.add(serviceTaSettingsMap);
                    } 
                }
                System.out.println("serviceTaSettingsMap :: "+ taxSettingsList);
                try {
                    objServiceTax = new ServiceTaxCalculation();
                    HashMap ser_Tax_Val = new HashMap();
                    ser_Tax_Val.put(ServiceTaxCalculation.CURR_DT, ClientUtil.getCurrentDate());                    
                    ser_Tax_Val.put("SERVICE_TAX_DATA", taxSettingsList);
                    serviceTax_Map = objServiceTax.calculateServiceTax(ser_Tax_Val);
                    if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.TOT_TAX_AMT)) {
                        String amt = CommonUtil.convertObjToStr(serviceTax_Map.get(ServiceTaxCalculation.TOT_TAX_AMT));
//                        lblServiceTaxval.setText(objServiceTax.roundOffAmt(amt, "NEAREST_VALUE"));
//                        serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, objServiceTax.roundOffAmt(amt, "NEAREST_VALUE"));
                        lblServiceTaxval.setText(amt);
                        serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, amt);
                    } else {
                        lblServiceTaxval.setText("0.00");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            
            /*    table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked(evt);
            }
          });*/
        }
        
    }
        private Object[][] setTableData() {
            
            return null;
        }
//    private void adds(){
//                table.setModel(new javax.swing.table.DefaultTableModel(
//                setTableData(),
//                new String[]{
//            "SELECT", "DESC", "AMOUNT", "M", "E"}) {
//            Class[] types = new Class[]{
//                java.lang.String.class,
//                java.lang.String.class,
//                java.lang.String.class,
//                java.lang.String.class,
//                java.lang.String.class
//            };
//            boolean[] canEdit = new boolean[]{
//                false, false, false, false, false
//            };
//
//            public Class getColumnClass(int columnIndex) {
//                return types[columnIndex];
//            }
//
//            public boolean isCellEditable(int rowIndex, int columnIndex) {
//                if (columnIndex == 6 || columnIndex == 12 || columnIndex == 13) {
//                    return true;
//                }
//                return canEdit[columnIndex];
//            }
//        });
//    }
        private boolean isEditable = false;

    public boolean getIsEditable() {
        return isEditable;
    }

    public void setIsEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }
        
        private boolean gerr(){
            boolean check = false;
            table.addMouseListener(new java.awt.event.MouseAdapter() {

                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    int clickedRow = CommonUtil.convertObjToInt(table.getSelectedRow());
                    String clickedColumn = CommonUtil.convertObjToStr(table.getSelectedColumn());
                    String isChargeEditable = CommonUtil.convertObjToStr(table.getValueAt(clickedRow, 5));
                    System.out.println("table...>>>>>>78788888888888888888888888888888888888" + isChargeEditable);
                    if (isChargeEditable.equals("Y")) {
                        setIsEditable(true);
                    }else
                    {
                        setIsEditable(false);
                    }
               tableMouseClicked(evt);
                }
            });
            return check;
        }
        private void createChargeTable(String prodDesc1) {
        HashMap tableMap = buildData(prodDesc1);
        ArrayList dataList = new ArrayList();
        dataList = (ArrayList)tableMap.get("DATA");
        //System.out.println("tableMap.get...>>>>>>>>"+tableMap.get("DATA"));
        System.out.println("dataList...>>>>>>>>"+dataList);
        //System.out.println("dataList.size...>>>>>>>>"+dataList.size());
        if(dataList!= null && dataList.size()>0){
            tableFlag = true;
            ArrayList headers;
            panChargeDetails.setVisible(true);
            //begin
            
            
            
            //end
            SimpleTableModel  stm = new SimpleTableModel((ArrayList)tableMap.get("DATA"),(ArrayList)tableMap.get("HEAD"));
            System.out.println("stm...>>>>>>>>"+stm);
            table = new CTable(){
                public boolean isCellEditable(int row, int column) {
                    gerr();
                   boolean checks = getIsEditable();
                    switch (column) {
                        case 0: 
                            return true;
                        case 3:  // select the cell you want make it not editable 
                            System.out.println("iddddd"+getIsEditable());
                            if (checks == true) {
                                return true;
                            } else {
                                return false;
                            }
                        default:
                            return false;
                    }

                }
            };
            table.setModel(stm);
//            adds();
          // lblTotalTransactionAmtVal.setText("0.0");
            System.out.println("table...>>>>>>>>"+table);
            table.setSize(430, 110);
            srpChargeDetails = new javax.swing.JScrollPane(table);
            srpChargeDetails.setMinimumSize(new java.awt.Dimension(430, 110));
            srpChargeDetails.setPreferredSize(new java.awt.Dimension(430, 110));
            panChargeDetails.add(srpChargeDetails, new java.awt.GridBagConstraints());
            table.revalidate();
               //charge details
            //calculating total of selected amount akkuu

            //akkuu
           
        }else{
            tableFlag = false;
            chrgTableEnableDisable();
        }
           
    }
    
        private void chrgTableEnableDisable(){
       // lblTotalTransactionAmtVal.setText(null);
        tableFlag = false;
        panChargeDetails.removeAll();
        panChargeDetails.setVisible(false);
    }
    
    private HashMap buildData(String prodDesc1){
        HashMap whereMap = new HashMap();
        whereMap.put("SCHEME_ID",prodDesc1);
        whereMap.put("DEDUCTION_ACCU","O");
        
        List list = ClientUtil.executeQuery("getChargeDetailsData",whereMap);
        //System.out.println("list...>>>>>>>>"+list);
        //System.out.println("list size...>>>>>>>>"+list.size());
        boolean _isAvailable = list.size() > 0 ? true : false;
        ArrayList _heading = null;
        ArrayList data = new ArrayList();
        ArrayList colData = new ArrayList();
        HashMap map;
        Iterator iterator = null;
        if (_isAvailable) {
            map = (HashMap) list.get(0);
            iterator = map.keySet().iterator();
        }
        if (_isAvailable && _heading == null) {
            _heading = new ArrayList();
            _heading.add("Select");
            while (iterator.hasNext()) {
                _heading.add((String) iterator.next());
            }
        }
        
        String cellData="", keyData="";
        Object obj = null;
        for (int i=0, j=list.size(); i < j; i++) {
            map = (HashMap) list.get(i);
            colData = new ArrayList();
            iterator = map.values().iterator();
                       if (CommonUtil.convertObjToStr(map.get("M")).equals("Y")) {
                colData.add(new Boolean(true));
            } else {
                colData.add(new Boolean(false));
            }
            while (iterator.hasNext()) {
                obj = iterator.next();
                //                if (obj != null) {
                colData.add(CommonUtil.convertObjToStr(obj));
                //                } else {
                //                    colData.add("");
                //                }
            }
            data.add(colData);
        }
        map = new HashMap();
        map.put("HEAD", _heading);
        map.put("DATA", data);
        //System.out.println("map in creating charges...."+map);
        return map;
    }
    
    //charge end..
    
    
    
    
    private int getRoundOffType(String roundOff) {
        int returnVal = 0;
        if (roundOff.equals("Nearest Value")) {
            returnVal = 1*100;
        } else if (roundOff.equals("Nearest Hundreds")) {
            returnVal = 100*100;
        } else if (roundOff.equals("Nearest Tens")) {
            returnVal = 10*100;
        }
        return returnVal;
    }
    
     private void txtLimit_SDFocusLost(){
        if (!txtLoanAmt.getText().equals("")){
            if (!observable.checkLimitValue(txtLoanAmt.getText())){
                String message = new String("The Limit value must fall within "+observable.getMinLimitValue().toString()+" and  "+observable.getMaxLimitValue().toString());
                displayAlert(message);
                observable.setTxtLoanAmt("");
                txtLoanAmt.setText(observable.getTxtLoanAmt());
                message = null;
            }
        }
    }
    
    
    private void txtLoanAmtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLoanAmtActionPerformed
        // TODO add your handling code here:
                
           String mainLimit=CommonUtil.convertObjToStr(txtLoanAmt.getText());
           
//           observable.setFacilityAcctHead();
        //deposit lien shoude be changed while limit is changed
//        if(loanType.equals("LTD")) {
//            if (observable.getProductCategory().equals("OTHER_LOAN")) {//&& observable.getStrACNumber().length()>0)
//                if(mainLimitMarginValidation(mainLimit))
//                    return;
//            } else {
                if (observable.getEligibleAmt() < CommonUtil.convertObjToDouble(mainLimit).doubleValue()) {
                    System.out.println("observable.getEligibleAmt()111>>>>"+observable.getEligibleAmt());
                    ClientUtil.showAlertWindow("Limit amount should not exceed "+observable.getEligibleAmt());
                    txtLoanAmt.setText(String.valueOf(observable.getEligibleAmt()));
                     
                    return;
                }
//            }
//            morotoriumEnableDisable(false);
//        }
        //
        rupeesValidation(mainLimit);
        if(observable.getProductAuthRemarks().equals("OTHER_LOAN"))
        txtLimit_SDFocusLost();
        //od balance check
      
//        if(!CommonUtil.convertObjToStr(txtLimit_SD.getText()).equals(CommonUtil.convertObjToStr(observable.getTxtLimit_SD())))
//            if(loanType.equals("LTD")&& tblRepaymentCTable.getRowCount()>0 && (observable.getActionType()==ClientConstants.ACTIONTYPE_NEW ||
//            observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT))
//                if (tblRepaymentCTable.getRowCount()>0) {
//                    sanMousePress=true;
//                    updateOBFields();
//                    tblRepaymentCTableMousePressed();
//                    btnRepayment_DeleteActionPerformed();
//                    btnRepayment_NewActionPerformed();
//                    btnEMI_CalculateActionPerformed();
//                    btnRepayment_SaveActionPerformed();
//                    //            ClientUtil.displayAlert("Change the sanction Details so Create new Repayment schdule");
//                    //            tabLimitAmount.setSelectedComponent(panRepaymentSchedule);
//                    //
//                }
        
        
//        if(!loanType.equals("LTD") && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
//            if(CommonUtil.convertObjToDouble(txtLimit_SD.getText()).doubleValue()>0){
//                double shareSanctionAmt =0.0;
//                double totalShareSanctionAmt =0.0;
//                double finalTotalSanctionAmt =0.0;
//                HashMap shareMap = new HashMap();
//                String loanType ="";
//                String displayStr = "";
//                shareMap.put("CUST_ID",txtCustID.getText());
//                List shareLimitLst =ClientUtil.executeQuery("getShareLoanLimitPercentage", shareMap);
//                if(shareLimitLst!=null && shareLimitLst.size()>0){
//                    for(int i=0;i<shareLimitLst.size();i++){
//                        double percentage =0.0;
//                        double totalSanctionAmt =0.0;
//                        shareMap = (HashMap)shareLimitLst.get(i);
//                        percentage = CommonUtil.convertObjToDouble(shareMap.get("BORROWER_SHARE_PERCENTAGE")).doubleValue();
//                        loanType = CommonUtil.convertObjToStr(shareMap.get("LOAN_TYPE"));
//                        if(loanType.equals("OTHER_LOAN")){
//                            totalSanctionAmt = CommonUtil.convertObjToDouble(txtLimit_SD.getText()).doubleValue();
//                        }
//                        HashMap loanSanctionMap = new HashMap();
//                        loanSanctionMap.put("CUST_ID",txtCustID.getText());
//                        loanSanctionMap.put("AUTHORIZE_REMARK",loanType);
//                        List loanSanctionLst =ClientUtil.executeQuery("getTotalSanctionAmount", loanSanctionMap);
//                        if(loanSanctionLst!=null && loanSanctionLst.size()>0){
//                            loanSanctionMap = new HashMap();
//                            for(int j=0;j<loanSanctionLst.size();j++){
//                                loanSanctionMap = (HashMap)loanSanctionLst.get(j);
//                                totalSanctionAmt= totalSanctionAmt + CommonUtil.convertObjToDouble(loanSanctionMap.get("SANCTION_AMOUNT")).doubleValue();
//                                displayStr += "Existing Loan No  : "+loanSanctionMap.get("ACCT_NUM")+ "\n"+
//                                "Limit                     : Rs "+loanSanctionMap.get("SANCTION_AMOUNT")+"\n";
//                            }
//                            finalTotalSanctionAmt = finalTotalSanctionAmt + totalSanctionAmt;
//                            shareSanctionAmt = totalSanctionAmt*percentage/100;
//                            
//                        }
//                        totalShareSanctionAmt = totalShareSanctionAmt+shareSanctionAmt;
//                        
//                    }
//                    if(finalTotalSanctionAmt>0){
//                        finalTotalSanctionAmt = finalTotalSanctionAmt-CommonUtil.convertObjToDouble(txtLimit_SD.getText()).doubleValue();
//                    }
//                    double shortFallAmt =0.0;
//                    shortFallAmt = totalShareSanctionAmt-CommonUtil.convertObjToDouble(txtTotalShareAmount.getText()).doubleValue();
//                    Rounding rod =new Rounding();
//                    shortFallAmt = (double)rod.getNearest((long)(shortFallAmt *100),100)/100;
//                    if(CommonUtil.convertObjToDouble(txtTotalShareAmount.getText()).doubleValue() < totalShareSanctionAmt){
//                        displayStr +=     "Total Limits                                      : Rs "+finalTotalSanctionAmt +"\n";
//                        displayStr +=     "Share Value to be Subscribed         : Rs "+totalShareSanctionAmt+"\n";
//                        displayStr +=     "Present Share Amount Subscribed : Rs "+txtTotalShareAmount.getText()+"\n";
//                        displayStr +=     "Shortfall                                          : Rs "+shortFallAmt;
//                        
//                        if(!displayStr.equals("")){
//                            ClientUtil.showMessageWindow(""+displayStr);
//                        }
//                        int c = ClientUtil.confirmationAlert("Do you want to Continue");
//                        int d= 0;
//                        System.out.println("####### Yes/No : "+c);
//                        if(c!=d)
//                            return;
//                    }
//                }
//            }
//        }
                        //charge details
        //Added By Suresh
        if(txtLoanAmt.getText()!=null && !txtLoanAmt.getText().equals("")){
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){    // Loan Charges
            prodDesc1 = CommonUtil.convertObjToStr(cboLoanProduct.getModel().getSelectedItem());
            System.out.println("#### prodDesc1>>>"+prodDesc1);
            chrgTableEnableDisable();
            createChargeTable(prodDesc1);
            chargeAmount();
          //  txtLoanAmts.setText(txtLoanAmt.getText());
           
        }
//         if(CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")){
//             if(!cboSchemName.getSelectedItem().equals("") && cboSchemName.getSelectedItem().toString()!=null){
//                 System.out.println("hiiiiiiii");
//                 chkEligibleAmount();
//             }
//         }
        }
        //end..
        
    }//GEN-LAST:event_txtLoanAmtActionPerformed
    private void calculateRepaymentToDate() {
        //Added BY Suresh
         observableRepay.setTdtFirstInstall(tdtRepaymentDt.getDateValue());
//        if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")) {
//            tdtFirstInstall.setDateValue(tdtFacility_Repay_Date.getDateValue());
//            observableRepay.setTdtFirstInstall(tdtFacility_Repay_Date.getDateValue());
//        } else if (!tdtFirstIns.getDateValue().equals("") && !cboRepayFreq_Repayment.getSelectedItem().equals("") && !cboRepayFreq_Repayment.getSelectedItem().equals("User Defined") && !txtNoInstall.getText().equals("")) {
//            moratorium_Given_RepayscheduleCalculation();
//            java.util.GregorianCalendar gCalendar = new java.util.GregorianCalendar();
//            java.util.GregorianCalendar gCalendarrepaydt = new java.util.GregorianCalendar(); //forrepaydate shoude change from first dt
//            gCalendar.setGregorianChange(DateUtil.getDateMMDDYYYY(tdtRepaymentDt.getDateValue()));//tdtFirstInstall.getDateValue()));
//            gCalendar.setTime(DateUtil.getDateMMDDYYYY(tdtRepaymentDt.getDateValue()));//tdtFirstInstall.getDateValue()));
//            gCalendarrepaydt.setGregorianChange(DateUtil.getDateMMDDYYYY(tdtRepaymentDt.getDateValue()));//tdtFirstInstall.getDateValue()));
//            gCalendarrepaydt.setTime(DateUtil.getDateMMDDYYYY(tdtRepaymentDt.getDateValue())); //tdtFirstInstall.getDateValue()));
//            int dateVal = observableRepay.getRepayScheduleIncrementType();
//            int incVal = observable.getInstallNo(txtNoOfInstall.getText(), dateVal);
//            repayFromdate = new java.util.Date();
//          
//                repayFromdate = DateUtil.getDateMMDDYYYY(tdtRepaymentDt.getDateValue());
//
//            if (txtNoOfInstall.getText().equals("1")) {
//                date = DateUtil.getDateMMDDYYYY(tdtRepaymentDt.getDateValue());//tdtFirstInstall.getDateValue());
//            }
//            //System.out.println("Date##" + date);
//            if (dateVal <= 7) {
//                gCalendar.add(gCalendar.DATE, incVal);
//            } else if (dateVal >= 30) {
//                gCalendar.add(gCalendar.MONTH, incVal);
//                int firstInstall = dateVal / 30;
//                gCalendarrepaydt.add(gCalendarrepaydt.MONTH, firstInstall);//for repaydate
//            }
//           // tdtLastInstall.setDateValue(DateUtil.getStringDate(gCalendar.getTime()));
//            observableRepay.setTdtLastInstall(tdtLastInstall.getDateValue());
//           // tdtFirstInstall.setDateValue(DateUtil.getStringDate(gCalendarrepaydt.getTime()));
//            observableRepay.setTdtFirstInstall(tdtFirstInstall.getDateValue());
//            gCalendar = null;
//            gCalendarrepaydt = null;
//        }
    }

    public void repaymentFillData(java.util.LinkedHashMap data, double totRepayVal) {
        observableRepay.populateEMICalculatedFields(data, totRepayVal);
        deleteInstallment = false;
    }
    private void updateRepamentField() {
        observableRepay.setSelectedBranchID(getSelectedBranchID());
        observableRepay.setTxtNoInstall(txtNoOfInstall.getText());
        observable.setTxtNoInstallments(txtNoOfInstall.getText());
        observableRepay.setTdtFirstInstall(tdtRepaymentDt.getDateValue());
        observableRepay.setTxtLaonAmt(txtLoanAmt.getText());
        observableRepay.setTdtRepayFromDate(tdtRepaymentDt.getDateValue());
        observableRepay.setTdtFirstInstall(tdtRepaymentDt.getDateValue());
        observableRepay.setTxtTotalBaseAmt(txtLoanAmt.getText());
        observableRepay.setRdoActive_Repayment(true);
        observableRepay.setTxtRepayScheduleMode(CommonConstants.REPAY_SCHEDULE_MODE_REGULAR);
        observableRepay.setTdtDisbursement_Dt(tdtAccountOpenDate.getDateValue());
    }
    private void btnEMI_CalculateActionPerformed() {
         updateOBFields();
        updateRepamentField();
        HashMap repayData = new HashMap();
        //repayData.put("SCHEDULE_MODE","REGULAR");
        repayData.put("NEW_INSTALLMENT", "NEW_INSTALLMENT");
        repayData.put("FROM_DATE", DateUtil.getDateMMDDYYYY(tdtRepaymentDt.getDateValue()));
        repayData.put("TO_DATE", DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue()));
        repayData.put("REPAYMENT_START_DT", DateUtil.getDateMMDDYYYY(tdtRepaymentDt.getDateValue()));
        repayData.put("NO_INSTALL", txtNoOfInstall.getText());
        repayData.put("DURATION_YY", txtNoOfInstall.getText());
        repayData.put("PRINCIPAL_AMOUNT", txtLoanAmt.getText());
        repayData.put("REPAYMENT_FREQUENCY", "30");
        repayData.put("INTEREST", txtInter.getText());
        repayData.put("REPAYMENT_TYPE", "UNIFORM_PRINCIPLE_EMI");
        repayData.put("ISDURATION_DDMMYY", "YES");
        repayData.put("INTEREST_TYPE", "COMPOUND");
        repayData.put("COMPOUNDING_PERIOD", "30");//CommonUtil.convertObjToStr(prodLevelValues.get("DEBITINT_COMP_FREQ")));
        repayData.put("REPAYMENT_TYPE", "UNIFORM_PRINCIPLE_EMI");
        repayData.put("ROUNDING_FACTOR", "1_RUPEE");
        repayData.put(loanType, "");
        repayData.put("PROD_ID", ((ComboBoxModel) cboLoanProduct.getModel()).getKeyForSelected().toString()); //added by shihad
        if (CommonUtil.convertObjToDouble(txtInstallAmount.getText()) > 0) {
            repayData.put("INSTALLMENT_AMOUNT", CommonUtil.convertObjToDouble(txtInstallAmount.getText()));
        }
        try {
            new TermLoanInstallmentUI(this, repayData, false);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        //System.out.println("renewal333333333" + observable.isRenewalYesNo());
        observableRepay.addRepaymentDetails(false);
    }
    private void rupeesValidation(String mainLimit){
        if(mainLimit.length()>0){
            double limit=CommonUtil.convertObjToDouble(mainLimit);
            if((limit%1) <1 && (limit%1)!=0){
                ClientUtil.showMessageWindow("Enter The Amount  in Rupees");
                //                txtLimit_SD.setText("");
            }
            
        }
    }
    
    
    private void tdtDealingWithBankSinceFocusLost(){
        // To check the entered date is less than or equal to current date
        //        ClientUtil.validateLTDate(tdtDealingWithBankSince);
    }
    
    
    
    
    
    private void changesInUIExceptLoanAgainstDeposit(){
        
        //        tblSecurityTable.setModel(observableSecurity.getTblSecurityTab());
        //        tblSecurityTable.setEnabled(true);
    }
    
        private void fieldsToHideBasedOnAccount(boolean val){
        lblMultiDisburseAllow.setVisible(val);
        rdoMultiDisburseAllow_No.setVisible(val);
        rdoMultiDisburseAllow_Yes.setVisible(val);
        }
    //
    //    private void txtFromAmtFocusLost(){
    //        // To check whether the From amount is less than the To amount
    //        updateFromToAmountOB();
    ////        if (!txtToAmt.getText().equals("")){
    ////            if (CommonUtil.convertObjToInt(txtFromAmt.getText()) > CommonUtil.convertObjToInt(txtToAmt.getText())){
    ////                observableInt.setTxtFromAmt("");
    ////            }
    ////        }
    //        updateFromToAmount();
    //    }
    //
    //    private void tdtTDateFocusLost(){
    //        // To check whether this To date is greater than this details From date
    //        ClientUtil.validateToDate(tdtTDate, tdtFDate.getDateValue());
    //        // To check whether this To date is greater than this details repayment date
    //        int installmentInt=0;
    //        if((!txtNoInstallments.getText().equals(""))){
    //            String instNo=txtNoInstallments.getText();
    //            installmentInt=Integer.parseInt(instNo);
    //        }
    //        if(loanType.equals("OTHERS")){
    //            if (cboRepayFreq.getSelectedItem().equals("Lump Sum"))
    //                tdtFacility_Repay_Date.setDateValue(tdtTDate.getDateValue());
    //            else if(installmentInt>1)
    //                ClientUtil.validateToDate(tdtTDate, tdtFacility_Repay_Date.getDateValue());
    //            tdtTDate.setEnabled(true);
    //        }else
    //            tdtTDate.setEnabled(false);
    //        populatePeriodDifference();
    //    }    private void tdtFDateFocusLost(){
    //        // To check whether this From date is less than this details To date
    //        ClientUtil.validateFromDate(tdtFDate, tdtTDate.getDateValue());
    //        // To check whether this From date is greater than the sanction date
    //        if (!tdtFDate.getDateValue().equals(tdtSanctionDate.getDateValue())){
    //            ClientUtil.validateToDate(tdtFDate, tdtSanctionDate.getDateValue());
    //        }
    //        if (cboRepayFreq.getSelectedItem().equals("User Defined") || cboRepayFreq.getSelectedItem().equals("Lump Sum")){
    //
    //        }else{
    //            calculateSanctionToDate();
    //        }
    //        populatePeriodDifference();
    //    }
    
    //private void calculateSanctionToDate(){
        //        if (! (viewType.equals(AUTHORIZE)|| viewType.equals("Edit") || viewType.equals(EXCEPTION) || viewType.equals(REJECT) || viewType.equals("Delete")))
        //            if (!tdtFDate.getDateValue().equals("") && !cboRepayFreq.getSelectedItem().equals("") && !txtNoInstallments.getText().equals("")){
        //                java.util.GregorianCalendar gCalendar = new java.util.GregorianCalendar();
        //                java.util.GregorianCalendar gCalendarrepaydt = new java.util.GregorianCalendar(); //forrepaydate shoude change from first dt
        //                if(CommonUtil.convertObjToStr(tdtFacility_Repay_Date.getDateValue()).equals(""))
        //                    tdtFacility_Repay_Date.setDateValue(tdtFDate.getDateValue());
        //                gCalendar.setGregorianChange(DateUtil.getDateMMDDYYYY(tdtFacility_Repay_Date.getDateValue()));
        //                gCalendar.setTime(DateUtil.getDateMMDDYYYY(tdtFacility_Repay_Date.getDateValue()));
        //                gCalendarrepaydt.setGregorianChange(DateUtil.getDateMMDDYYYY(tdtFacility_Repay_Date.getDateValue()));
        //                gCalendarrepaydt.setTime(DateUtil.getDateMMDDYYYY(tdtFacility_Repay_Date.getDateValue()));
        //                int dateVal = observable.getIncrementType();
        //                int incVal = observable.getInstallNo(txtNoInstallments.getText(), dateVal);
        //                date=new java.util.Date();
        //                date=DateUtil.getDateMMDDYYYY(tdtFacility_Repay_Date.getDateValue());
        //                if (txtNoInstallments.getText().equals("1"))
        //                    date=DateUtil.getDateMMDDYYYY(tdtFDate.getDateValue());
        //                System.out.println("Date##"+date);
        //                if (dateVal <= 7){
        //                    gCalendar.add(gCalendar.DATE, incVal);
        //                }else if (dateVal >= 30){
        //                    gCalendar.add(gCalendar.MONTH, incVal);
        //                    int firstInstall=dateVal/30;
        //                    gCalendarrepaydt.add(gCalendarrepaydt.MONTH,firstInstall);//for repaydate
        //                }
        //                tdtTDate.setDateValue(DateUtil.getStringDate(gCalendar.getTime()));
        //
        //                observable.setTdtTDate(tdtTDate.getDateValue());
        //                tdtFacility_Repay_Date.setDateValue(DateUtil.getStringDate(gCalendarrepaydt.getTime()));
        //                observable.setTdtFacility_Repay_Date(tdtFacility_Repay_Date.getDateValue()); //for repaydate
        //                gCalendar = null;
        //                gCalendarrepaydt=null;
        //            }else{
        //                tdtTDate.setDateValue("");
        //                observable.setTdtTDate("");
        //                tdtFacility_Repay_Date.setDateValue("");
        //                observable.setTdtFacility_Repay_Date("");
        //                observable.setTxtPeriodDifference_Days("");
        //                observable.setTxtPeriodDifference_Months("");
        //                observable.setTxtPeriodDifference_Years("");
        //            }
   // }
    
    
    
    
    
    
    private void btnDelete_BorrowerMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDelete_BorrowerMousePressed
        // Add your handling code here:
    }//GEN-LAST:event_btnDelete_BorrowerMousePressed
    private void btnToMain_BorrowerActionPerformed(){
        updateOBFields();
        setBorrowerNewOnlyEnable();
        //        observableComp.resetCustomerDetails();
        //        observableBorrow.moveToMain(CommonUtil.convertObjToStr(tblBorrowerTabCTable.getValueAt(0, 1)), CommonUtil.convertObjToStr(tblBorrowerTabCTable.getValueAt(tblBorrowerTabCTable.getSelectedRow(), 1)), tblBorrowerTabCTable.getSelectedRow());
        //        observable.ttNotifyObservers();
    }
//GEN-FIRST:event_btnDelete_BorrowerActionPerformed
                                                                                                                                                                 private void tblBorrowerTabCTableMousePressed(int rowSelected){//GEN-LAST:event_btnDelete_BorrowerActionPerformed
                                                                                                                                                                     
                                                                                                                                                                     
                                                                                                                                                                 }
                                                                                                                                                                 
public void callView(String currField) {
    try {
        viewType = currField;
        if (currField.equals("CUSTOMER ID")) {
            StringBuffer presentCust = new StringBuffer();
            String maturityDt = "";
            double rateInt = 0;
            int jntAccntTablRow = tblDepositDetails.getRowCount();
            if (tblDepositDetails.getRowCount() != 0) {
                for (int i = 0, sizeJointAcctAll = tblDepositDetails.getRowCount(); i < sizeJointAcctAll; i++) {
                    if (i == 0 || i == sizeJointAcctAll) {
                        presentCust.append("'" + CommonUtil.convertObjToStr(tblDepositDetails.getValueAt(i, 0)) + "'");
                        maturityDt = CommonUtil.convertObjToStr(tblDepositDetails.getValueAt(i, 5));
                        rateInt = CommonUtil.convertObjToDouble(tblDepositDetails.getValueAt(i, 2)).doubleValue();
                    } else {
                        presentCust.append("," + "'" + CommonUtil.convertObjToStr(tblDepositDetails.getValueAt(i, 0)) + "'");
                    }
                }
            }
            HashMap viewMap = new HashMap();
            viewMap.put("MAPNAME", "getSelectDepositCustListForLTD");
            HashMap whereMap = new HashMap();
            Date currDt = (Date) curr_dt.clone();
            whereMap.put("CURR_DT", currDt);
            whereMap.put("LOAN_PROD_ID", CommonUtil.convertObjToStr(observable.getCbmLoanProduct().getKeyForSelected()));
            whereMap.put("DEPOSIT_PROD_ID", CommonUtil.convertObjToStr(observable.getCbmDepositProduct().getKeyForSelected()));
            whereMap.put("DEPOSIT_NO_PRESENT", presentCust);
            if (rateInt > 0) {
                whereMap.put("RATE_OF_INT", new Double(rateInt));
            }
//            if(maturityDt !=null && maturityDt.length()>0){
//                System.out.println("@#@#@@ DateUtil.getDateMMDDYYYY(maturityDt):"+maturityDt);
//                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");   
//                Date dateWithoutTime = sdf.parse(sdf.format(new Date())); 
//                
////                formatter = new SimpleDateFormat("dd-MMM-yy");
////  s = formatter.format(date);
//  
//                System.out.println(dateWithoutTime);
//                whereMap.put("MATURITY_DT", maturityDt);//DateUtil.getDateMMDDYYYY(maturityDt));
//            }
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, viewMap).show();
            presentCust = null;
        } else if (currField.equals("MDS_CUSTOMER")) { //Added By Suresh
            HashMap viewMap = new HashMap();
            viewMap.put("MAPNAME", "getSelectMDSCustDetails");
            HashMap whereMap = new HashMap();
            whereMap.put("MDS_PROD_ID", CommonUtil.convertObjToStr(observable.getCbmDepositProduct().getKeyForSelected()));
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, viewMap).show();
        } else if (currField.equals("MDS_CUSTOMER_AUCTION")) { //Added By ji
            HashMap viewMap = new HashMap();
            viewMap.put("MAPNAME", "getSelectAuctionMDSCustDetails");
            HashMap whereMap = new HashMap();
            whereMap.put("MDS_PROD_ID", CommonUtil.convertObjToStr(observable.getCbmDepositProduct().getKeyForSelected()));
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, viewMap).show();
        } else if (currField.equals("PADDY_CUSTOMER")) { //Added By Suresh
            HashMap viewMap = new HashMap();
            viewMap.put("MAPNAME", "getSelectPaddyCustDetails");
            HashMap whereMap = new HashMap();
            whereMap.put("PADDY_PROD_ID", CommonUtil.convertObjToStr(observable.getCbmDepositProduct().getKeyForSelected()));
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, viewMap).show();
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

public void fillData(Object param) {
    HashMap hash = (HashMap) param;
    HashMap Loanhash = (HashMap) param;
    System.out.println("calling filldata#####"+hash);
    if (hash.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")) {
        fromNewAuthorizeUI = true;
        newauthorizeListUI = (NewAuthorizeListUI) hash.get("PARENT");
        hash.remove("PARENT");
        viewType = AUTHORIZE;
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        observable.setStatus();
        authEnableDisable();
        btnReject.setEnabled(false);
        rejectFlag = 1;

    }
    if(hash.containsKey("FROM_AUTHORIZE_LIST_UI")){
        fromAuthorizeUI = true;
        authorizeListUI = (AuthorizeListUI) hash.get("PARENT");
        hash.remove("PARENT");
        viewType = AUTHORIZE;
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        observable.setStatus();
        authEnableDisable();
        btnReject.setEnabled(false);
        rejectFlag=1;
    }
    if(hash.containsKey("FROM_MANAGER_AUTHORIZE_LIST_UI")){
        fromManagerAuthorizeUI = true;
        ManagerauthorizeListUI = (AuthorizeListDebitUI) hash.get("PARENT");
        hash.remove("PARENT");
        viewType = AUTHORIZE;
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        observable.setStatus();
        authEnableDisable();
        btnReject.setEnabled(false);
        rejectFlag=1;
    }
     if(hash.containsKey("FROM_CASHIER_APPROVAL_REJ_UI")){
            fromAuthorizeUI = false;
            fromManagerAuthorizeUI = false;
            viewType =AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
            observable.setStatus();
            btnSaveDisable();
        }
    if (viewType != null) {
        if (viewType.equals("Edit") || viewType.equals("Delete") || viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) ||
        viewType.equals(REJECT) || viewType.equals("Enquirystatus")) {
            if(viewType.equals(AUTHORIZE) ||viewType.equals(REJECT))
                hash.put("LOAN_NO",hash.get("ACCT_NUM"));
            isFilled = true;
            observable.populateData(hash);//, authSignUI.getAuthorizedSignatoryOB(), poaUI.getPowerOfAttorneyOB());
            observable.setLblCustNameValue(CommonUtil.convertObjToStr(hash.get("CUSTOMER NAME")));
            observable.setLblCustomerIdValue(CommonUtil.convertObjToStr(hash.get("CUSTOMER ID")));
            observable.setLblMemberIdValue(CommonUtil.convertObjToStr(hash.get("MEMBERSHIP_NO")));
            setButtonEnableDisable();
            newRecord=false;
        }
        if(viewType.equals(AUTHORIZE)||viewType.equals(REJECT)){
             displayTransDetail();
        }
        if (viewType.equals("CUSTOMER ID")) {
            HashMap depositMap=new HashMap();
            HashMap executeMap=new HashMap();
            depositMap.put("DEPOSIT_NO",hash.get("DEPOSIT_NO"));
            executeMap.put(CommonConstants.MAP_WHERE,depositMap);
            List lst=ClientUtil.executeQuery("getLoanProductFromDepositProduct",executeMap);
            if(lst !=null && lst.size()>0){
                hash=(HashMap)lst.get(0);
                viewType="DEPOSIT ID";
                HashMap lienExistornotMap = new HashMap();
                lienExistornotMap.put("DEPOSIT_NO",depositMap.get("DEPOSIT_NO"));
                List lienLst = ClientUtil.executeQuery("checkDepositLienDetails", lienExistornotMap);
                if (lienLst!=null && lienLst.size() > 0) {
                    ClientUtil.showAlertWindow("Loan already Created for this Deposit and Pending for Authorization !!!");
					txtDepositNo.setText("");
                    return;
                }
            }
        }
        //Added By 
        if (viewType.equals("MDS_CUSTOMER")) {
//            tblDepositDetails.setModel(observable.getTblSanctionMainMds());
            clearMDSDetails();
            double bal = CommonUtil.convertObjToDouble(hash.get("AMOUNT")).doubleValue();
            String membNo=CommonUtil.convertObjToStr(hash.get("MEMBER_NO"));
            String membType=CommonUtil.convertObjToStr(hash.get("MEMBER_TYPE"));
            String membName=CommonUtil.convertObjToStr(hash.get("MEMBER_NAME"));
            String chittAmt=CommonUtil.convertObjToStr(hash.get("AMOUNT"));
            String chittalNo = CommonUtil.convertObjToStr(hash.get("CHITTAL_NO"));
            observable.setChittalNoMds(chittalNo);
            observable.setMembNo(membNo);
            observable.setMembName(membName);
            observable.setMembType(membType);
            observable.setChittAmt(chittAmt);
            lblMDSMemberNoVal.setText(CommonUtil.convertObjToStr(hash.get("MEMBER_NO")));
            lblMDSMemberTypeVal.setText(CommonUtil.convertObjToStr(hash.get("MEMBER_TYPE")));
            lblMDSMemberNameVal.setText(CommonUtil.convertObjToStr(hash.get("MEMBER_NAME")));
            lblMDSChitAmountPaidVal.setText("Rs. "+CommonUtil.convertObjToStr(hash.get("AMOUNT")));
            
            observable.getCbmCategory().setKeyForSelected("GENERAL_CATEGORY");
            observable.getCbmConstitution().setKeyForSelected("INDIVIDUAL");
            HashMap existingMap = new HashMap();
            existingMap.put("ACT_NUM",CommonUtil.convertObjToStr(hash.get("MEMBER_NO")));
            List mapDataList = ClientUtil.executeQuery("getSelectExistingCustId", existingMap);
            if (mapDataList!=null && mapDataList.size()>0) {
                existingMap = (HashMap)mapDataList.get(0);
                observable .setLblCustomerIdValue(CommonUtil.convertObjToStr(existingMap.get("CUST_ID")));
                existingMap.put("CUSTOMER ID",CommonUtil.convertObjToStr(existingMap.get("CUST_ID")));
                displayShareDetails(existingMap);
            }
            if (bal>0) {
                observable.setKeyValueForPaddyAndMDSLoans(CommonUtil.convertObjToStr(hash.get("CHITTAL_NO")));
                eligibleAmt = 0.0;
                existingMap.put("prodId", observable.getCbmLoanProduct().getKeyForSelected());
                List eligibleMarginlst = ClientUtil.executeQuery("TermLoan.getProdHead", existingMap);
                if (eligibleMarginlst.size()>0) {
                    existingMap = (HashMap)eligibleMarginlst.get(0);
                    observable.setSanctionAmtRoundOff(CommonUtil.convertObjToStr(existingMap.get("DEPOSIT_ROUNDOFF")));
                   observable.setSanctionAmtRoundOff(CommonUtil.convertObjToStr(existingMap.get("DEPOSIT_ROUNDOFF")));
                    int PERIOD=CommonUtil.convertObjToInt(existingMap.get("DEP_AMT_MATURING_PERIOD"));
                    Date currentDate=ClientUtil.getCurrentDate();
                    Date cdate=DateUtil.addDays(currentDate, PERIOD);
                    Date matdate=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hash.get("END_DT")));
                    if(DateUtil.dateDiff(matdate, cdate)>=0){
                         observable.setEligibleMargin(CommonUtil.convertObjToDouble(existingMap.get("DEP_AMT_LOAN_MATURING")).doubleValue());
                    }else{
                         observable.setEligibleMargin(CommonUtil.convertObjToDouble(existingMap.get("DEP_ELIGIBLE_LOAN_AMT")).doubleValue());
                    }
//                    observable.setEligibleMargin(CommonUtil.convertObjToDouble(existingMap.get("DEP_ELIGIBLE_LOAN_AMT")).doubleValue());
                    eligibleAmt = depositSanctionRoundOff(bal*(observable.getEligibleMargin()/100.0));
//                    observable.setEligibleAmt(eligibleAmt);
                   // double eligblLoanAmt = 0.0;
                    eligblLoanAmt = Math.round(eligibleAmt + eligblLoanAmt);
                    observable.setEligibleAmt(eligblLoanAmt);
                    observable.setTxtLoanAmt(String.valueOf(eligblLoanAmt));
                    lblEligibileAmtValue.setText(String.valueOf(eligblLoanAmt));
                    observable.setLblEligibileAmtValue(String.valueOf(eligblLoanAmt));
                    observable.setTxtLoanAmt(String.valueOf(eligblLoanAmt));
                    
                    hash.put("CATEGORY","GENERAL_CATEGORY");
                    hash.put("DEPOSIT_INT",new Double(0));
                    Date maturityDt =(Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hash.get("END_DT")));
                    hash.put("MATURITY_DT",maturityDt);
                    populateInterestRateForLTD(hash);
                    observable.setTdtRepaymentDt(DateUtil.getStringDate((java.util.Date)hash.get("END_DT")));
                }
            }else{
                ClientUtil.showMessageWindow("MDS Amount is Zero");
                return;
            }
            hash.put("DEPOSIT_AVAILABLE_BALANCE",hash.get("AMOUNT"));
            double currdepositAmt= observable.getLienAmount(hash,false);
            observable.setTxtDepositNo(CommonUtil.convertObjToStr(hash.get("CHITTAL_NO")));
            observable .setLblCustNameValue(CommonUtil.convertObjToStr(hash.get("MEMBER_NAME")));
            observable .setLblMemberIdValue(CommonUtil.convertObjToStr(hash.get("MEMBER_NO")));
            observable.getCbmCategory().setKeyForSelected("GENERAL_CATEGORY");
            observable.getCbmConstitution().setKeyForSelected("INDIVIDUAL");
            observable.setDeposit_amt(String.valueOf(eligibleAmt));
            observable.setTdtAccountOpenDate(DateUtil.getStringDate(curr_dt));
            tdtAccountOpenDate.setDateValue(DateUtil.getStringDate(curr_dt));
            tblDepositDetails.setModel(observable.getTblSanctionMainMds());
        }
           if (viewType.equals("MDS_CUSTOMER_AUCTION")) {
//               tblDepositDetails.setModel(observable.getTblSanctionMainMds());
            clearMDSDetails();
            ClientUtil.displayAlert("Chittal Already Prized !!! Auction No IS : "+hash.get("AUCTION_NO"));
            double bal = CommonUtil.convertObjToDouble(hash.get("AMOUNT")).doubleValue();
            lblMDSMemberNoVal.setText(CommonUtil.convertObjToStr(hash.get("MEMBER_NO")));
            lblMDSMemberTypeVal.setText(CommonUtil.convertObjToStr(hash.get("MEMBER_TYPE")));
            lblMDSMemberNameVal.setText(CommonUtil.convertObjToStr(hash.get("MEMBER_NAME")));
            lblMDSChitAmountPaidVal.setText("Rs. "+CommonUtil.convertObjToStr(hash.get("AMOUNT")));
            String membNo=CommonUtil.convertObjToStr(hash.get("MEMBER_NO"));
            String membType=CommonUtil.convertObjToStr(hash.get("MEMBER_TYPE"));
            String membName=CommonUtil.convertObjToStr(hash.get("MEMBER_NAME"));
            String chittAmt=CommonUtil.convertObjToStr(hash.get("AMOUNT"));
            String chittalNo = CommonUtil.convertObjToStr(hash.get("CHITTAL_NO"));
            observable.setChittalNoMds(chittalNo);
            observable.setMembNo(membNo);
            observable.setMembName(membName);
            observable.setMembType(membType);
            observable.setChittAmt(chittAmt);
            observable.getCbmCategory().setKeyForSelected("GENERAL_CATEGORY");
            observable.getCbmConstitution().setKeyForSelected("INDIVIDUAL");
            HashMap existingMap = new HashMap();
            existingMap.put("ACT_NUM",CommonUtil.convertObjToStr(hash.get("MEMBER_NO")));
            List mapDataList = ClientUtil.executeQuery("getSelectExistingCustId", existingMap);
            if (mapDataList!=null && mapDataList.size()>0) {
                existingMap = (HashMap)mapDataList.get(0);
                observable .setLblCustomerIdValue(CommonUtil.convertObjToStr(existingMap.get("CUST_ID")));
                existingMap.put("CUSTOMER ID",CommonUtil.convertObjToStr(existingMap.get("CUST_ID")));
                displayShareDetails(existingMap);
            }
            if (bal>0) {
                observable.setKeyValueForPaddyAndMDSLoans(CommonUtil.convertObjToStr(hash.get("CHITTAL_NO")));
                eligibleAmt = 0.0;
                existingMap.put("prodId", observable.getCbmLoanProduct().getKeyForSelected());
                List eligibleMarginlst = ClientUtil.executeQuery("TermLoan.getProdHead", existingMap);
                if (eligibleMarginlst.size()>0) {
                    existingMap = (HashMap)eligibleMarginlst.get(0);
                    observable.setSanctionAmtRoundOff(CommonUtil.convertObjToStr(existingMap.get("DEPOSIT_ROUNDOFF")));
                   observable.setSanctionAmtRoundOff(CommonUtil.convertObjToStr(existingMap.get("DEPOSIT_ROUNDOFF")));
                    int PERIOD=CommonUtil.convertObjToInt(existingMap.get("DEP_AMT_MATURING_PERIOD"));
                    Date currentDate=ClientUtil.getCurrentDate();
                    Date cdate=DateUtil.addDays(currentDate, PERIOD);
                    Date matdate=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hash.get("END_DT")));
                    if(DateUtil.dateDiff(matdate, cdate)>=0){
                         observable.setEligibleMargin(CommonUtil.convertObjToDouble(existingMap.get("DEP_AMT_LOAN_MATURING")).doubleValue());
                    }else{
                         observable.setEligibleMargin(CommonUtil.convertObjToDouble(existingMap.get("DEP_ELIGIBLE_LOAN_AMT")).doubleValue());
                    }
//                    observable.setEligibleMargin(CommonUtil.convertObjToDouble(existingMap.get("DEP_ELIGIBLE_LOAN_AMT")).doubleValue());
                    eligibleAmt =bal; //depositSanctionRoundOff(bal*(observable.getEligibleMargin()/100.0));
//                    observable.setEligibleAmt(eligibleAmt);
                     eligblLoanAmt = Math.round(eligibleAmt + eligblLoanAmt);
                    observable.setEligibleAmt(eligibleAmt);
                    observable.setTxtLoanAmt(String.valueOf(eligblLoanAmt));
                    txtLoanAmt.setText(String.valueOf(eligblLoanAmt));
                    lblEligibileAmtValue.setText(String.valueOf(eligblLoanAmt));
                    observable.setLblEligibileAmtValue(String.valueOf(eligblLoanAmt));
//                    observable.setTxtLoanAmt(String.valueOf(eligibleAmt));
                    
                    hash.put("CATEGORY","GENERAL_CATEGORY");
                    hash.put("DEPOSIT_INT",new Double(0));
                    Date maturityDt =(Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hash.get("END_DT")));
                    hash.put("MATURITY_DT",maturityDt);
                    populateInterestRateForLTD(hash);
                    observable.setTdtRepaymentDt(DateUtil.getStringDate((java.util.Date)hash.get("END_DT")));
                }
            }else{
                ClientUtil.showMessageWindow("MDS Amount is Zero");
                return;
            }
            hash.put("DEPOSIT_AVAILABLE_BALANCE",hash.get("AMOUNT"));
            double currdepositAmt= observable.getLienAmount(hash,false);
            observable.setTxtDepositNo(CommonUtil.convertObjToStr(hash.get("CHITTAL_NO")));
            observable .setLblCustNameValue(CommonUtil.convertObjToStr(hash.get("MEMBER_NAME")));
            observable .setLblMemberIdValue(CommonUtil.convertObjToStr(hash.get("MEMBER_NO")));
            observable.getCbmCategory().setKeyForSelected("GENERAL_CATEGORY");
            observable.getCbmConstitution().setKeyForSelected("INDIVIDUAL");
            observable.setDeposit_amt(String.valueOf(eligibleAmt));
            observable.setTdtAccountOpenDate(DateUtil.getStringDate(curr_dt));
            tdtAccountOpenDate.setDateValue(DateUtil.getStringDate(curr_dt));
            tblDepositDetails.setModel(observable.getTblSanctionMainMds());
        }
        lblCustomerIdValue.setVisible(true);
        tdtRepaymentDt.setEnabled(false);
        panCustIdDetails.setVisible(false);
        if (viewType.equals("PADDY_CUSTOMER")) {
            clearPaddyDetails();
            double bal = CommonUtil.convertObjToDouble(hash.get("AMOUNT")).doubleValue();
            observable.setKeyValueForPaddyAndMDSLoans(CommonUtil.convertObjToStr(hash.get("CND_NO")));
            observable.setTxtDepositNo(CommonUtil.convertObjToStr(hash.get("CND_NO")));
            lblPurchaseIDVal.setText(CommonUtil.convertObjToStr(hash.get("PURCHASE_ID")));
            lblPurchaseNameVal.setText(CommonUtil.convertObjToStr(hash.get("PURCHASE_NAME")));
            lblTransactionDateVal.setText(CommonUtil.convertObjToStr(hash.get("TRANS_DT")));
            lblPurchaseDateVal.setText(CommonUtil.convertObjToStr(hash.get("PURCHASE_DATE")));
            lblTotalWeightVal.setText(CommonUtil.convertObjToStr(hash.get("WEIGHT")));
            lblAcreageVal.setText(CommonUtil.convertObjToStr(hash.get("ACRE")));
            lblPurchaseAmountVal.setText("Rs. "+CommonUtil.convertObjToStr(hash.get("AMOUNT")));
            observable.getCbmCategory().setKeyForSelected("GENERAL_CATEGORY");
            observable.getCbmConstitution().setKeyForSelected("INDIVIDUAL");
            HashMap existingMap = new HashMap();
            if (bal>0) {
                observable.setKeyValueForPaddyAndMDSLoans(CommonUtil.convertObjToStr(hash.get("CND_NO")));
                eligibleAmt = 0.0;
                existingMap.put("prodId", observable.getCbmLoanProduct().getKeyForSelected());
                List eligibleMarginlst = ClientUtil.executeQuery("TermLoan.getProdHead", existingMap);
                if (eligibleMarginlst.size()>0) {
                    existingMap = (HashMap)eligibleMarginlst.get(0);
                    observable.setSanctionAmtRoundOff(CommonUtil.convertObjToStr(existingMap.get("DEPOSIT_ROUNDOFF")));
                    int PERIOD=CommonUtil.convertObjToInt(existingMap.get("DEP_AMT_MATURING_PERIOD"));
                    Date currentDate=ClientUtil.getCurrentDate();
                    Date cdate=DateUtil.addDays(currentDate, PERIOD);
                    Date matdate=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hash.get("MATURITY_DT")));
                    if(DateUtil.dateDiff(matdate, cdate)>=0){
                         observable.setEligibleMargin(CommonUtil.convertObjToDouble(existingMap.get("DEP_AMT_LOAN_MATURING")).doubleValue());
                    }else{
                         observable.setEligibleMargin(CommonUtil.convertObjToDouble(existingMap.get("DEP_ELIGIBLE_LOAN_AMT")).doubleValue());
                    }
                    eligibleAmt = Math.round(depositSanctionRoundOff(bal*(observable.getEligibleMargin()/100.0)));
                    observable.setEligibleAmt(eligibleAmt);
                    txtLoanAmt.setText(String.valueOf(eligibleAmt));
                //    double eligblLoanAmt = 0.0;
//                    eligblLoanAmt = eligibleAmt + eligblLoanAmt;
//                    System.out.println("eligblLoanAmt@@##222!!>>>>"+eligblLoanAmt);
                    observable.setTxtLoanAmt(String.valueOf(eligibleAmt));
                    lblEligibileAmtValue.setText(String.valueOf(eligibleAmt));
                    observable.setLblEligibileAmtValue(String.valueOf(eligibleAmt));
                    hash.put("CATEGORY","GENERAL_CATEGORY");
                    hash.put("DEPOSIT_INT",new Double(0));
                    Date maturityDt =(Date) curr_dt.clone();
                    maturityDt.setDate(curr_dt.getDate()+1);
                    hash.put("MATURITY_DT",maturityDt);
                    populateInterestRateForLTD(hash);
                    observable.setTdtRepaymentDt(DateUtil.getStringDate((java.util.Date)hash.get("END_DT")));
                    panCustIdDetails.setVisible(true);
                    lblCustomerIdValue.setVisible(false);
                    tdtRepaymentDt.setEnabled(true);
                }
            }else{
                ClientUtil.showMessageWindow("Paddy Amount is Zero");
                return;
            }
        }
        if (viewType.equals("PADDY_CUSTOMER_ID")) {
            if(hash.containsKey("CUST_ID")){
                hash.put("CUSTOMER ID",hash.get("CUST_ID"));
            }
            if(hash.containsKey("SHARE_ACCT_NO")){
                hash.put("MEMBER_NO",hash.get("SHARE_ACCT_NO"));
            }
            if(hash.containsKey("Name")){
                hash.put("NAME",hash.get("Name"));
            }
            if(hash.containsKey("MEMBER_NO")){
                observable .setLblMemberIdValue(CommonUtil.convertObjToStr(hash.get("MEMBER_NO")));
            }
            panCustIdDetails.setVisible(true);
            lblCustomerIdValue.setVisible(false);
            observable .setLblCustomerIdValue(CommonUtil.convertObjToStr(hash.get("CUSTOMER ID")));
            txtCustId.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMER ID")));
            observable .setLblCustNameValue(CommonUtil.convertObjToStr(hash.get("NAME")));
            hash.put("CUSTOMER ID",CommonUtil.convertObjToStr(hash.get("CUSTOMER ID")));
            displayShareDetails(hash);
            tdtRepaymentDt.setEnabled(true);
        }
        
        if (viewType.equals("DEPOSIT ID")) {
            HashMap checkMap = new HashMap();
            checkMap.put("DEPOSIT_NO",CommonUtil.convertObjToStr(hash.get("DEPOSIT_NO")));
            List checkList = ClientUtil.executeQuery("getAuthorizeStatusForDepositLien", checkMap);
            if(checkList.size()>0 && checkList!=null)
            checkMap = (HashMap)checkList.get(0);
           // System.out.println("checkMap111>>>>"+checkMap);    
            if(checkMap.get("AUTHORIZE_STATUS")==null){
             ClientUtil.showAlertWindow("Please authorize this Deposit No in Deposit Accounts screen first!!!");
             return;
            }else if(!checkMap.get("AUTHORIZE_STATUS").equals("AUTHORIZED")){
              ClientUtil.showAlertWindow("Please authorize this Deposit No in Deposit Accounts screen first!!!");
             return;  
            }
            String depositNo = CommonUtil.convertObjToStr(hash.get("DEPOSIT_NO"));
            String loanProduct = CommonUtil.convertObjToStr(hash.get("LOAN_PRODUCT"));
            String depsoitProduct = CommonUtil.convertObjToStr(hash.get("DEPOSIT_PRODUCT"));
            String loanAcctHead = CommonUtil.convertObjToStr(hash.get("LOAN_ACCOUNT_HEAD"));
            String custId = CommonUtil.convertObjToStr(hash.get("CUSTOMER_ID"));
            String customerName = CommonUtil.convertObjToStr(hash.get("CUSTOMER_NAME"));
            String membershipNo = CommonUtil.convertObjToStr(hash.get("MEMBERSHIP_NO"));
            observable.setTxtDepositNo(depositNo);
            observable.getCbmLoanProduct().setKeyForSelected(loanProduct);
            observable .getCbmDepositProduct().setKeyForSelected(depsoitProduct);
            observable .setLblShowAccountHeadId(loanAcctHead);
            observable .setLblCustNameValue(customerName);
            observable .setLblCustomerIdValue(custId);
            observable .setLblMemberIdValue(membershipNo);
            observable.getCbmCategory().setKeyForSelected("GENERAL_CATEGORY");
            observable.getCbmConstitution().setKeyForSelected("INDIVIDUAL");
            if(hash.containsKey("MATURITY_DT") && hash.get("MATURITY_DT") !=null)
            observable.setDep_mat_dt(CommonUtil.convertObjToStr(observable.getProperFormatDate(hash.get("MATURITY_DT"))));
            observable.setDep_mat_amt(CommonUtil.convertObjToStr(hash.get("MATURITY_AMT")));
            observable.setDep_credit_int(CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(hash.get("TOTAL_INT_CREDIT"))));
            observable.setDep_rate_int(CommonUtil.convertObjToStr(hash.get("DEPOSIT_INT")));
            //observable.setDeposit_amt(CommonUtil.convertObjToStr(hash.get("DEPOSIT_AMT")));
            observable.setDeposit_amt(CommonUtil.convertObjToStr(hash.get("DEPOSIT_AVAILABLE_BALANCE"))); //KD-3641 : DAY DEPOSIT LOAN
            
            observable.setTdtAccountOpenDate(DateUtil.getStringDate(curr_dt));
            tdtAccountOpenDate.setDateValue(DateUtil.getStringDate(curr_dt));
            hash.put("CUSTOMER ID",hash.get("CUSTOMER_ID"));
            displayShareDetails(hash);
            if(!observable.setDetailsForLTD(hash)) {
                observable.resetRemainingFields(false);
                return;
            }
            double currdepositAmt= observable.getLienAmount(hash,false);
//            lblEligibileAmtValue.setText(String.valueOf(currdepositAmt));
//            observable.setLblEligibileAmtValue(String.valueOf(currdepositAmt));
            double availBal = 0;
            availBal = CommonUtil.convertObjToDouble(hash.get("DEPOSIT_AVAILABLE_BALANCE"));
            if (availBal <= 0) {
                ClientUtil.showMessageWindow("Insufficient Available Balance!!!");
                txtDepositNo.setText("");
                return;
            } else {
              if(populateInterestRateForLTD(hash)){
                if(CommonUtil.convertObjToStr(observable.getTxtLoanAmt()).length()>0){
                    double loanAmt=CommonUtil.convertObjToDouble(observable.getTxtLoanAmt())-currdepositAmt;
                    if(loanAmt<0)
                        loanAmt*=-1;
                    observable.setTxtLoanAmt(String.valueOf(loanAmt));
                    return;
                }
            }
        }
            observable.setFacilityAcctHead();
        }
        if (viewType.equals("DEPOSIT_CUSTOMER")) {
            double bal = CommonUtil.convertObjToDouble(hash.get("BALANCE")).doubleValue();
            observable.setTxtDepositNo("");
            observable.setTxtDepositNo(CommonUtil.convertObjToStr(hash.get("DEPOSIT_NO")));
            if (bal>0) {
                boolean checkSameCustomer=observable.setDetailsForLTD(hash);
                if(checkSameCustomer){
                    setAllSanctionFacilityEnableDisable(true);
                }else{
                    setAllSanctionFacilityEnableDisable(false);
                }
            }else{
                ClientUtil.showMessageWindow("Deposit Amount is Zero");
                return;
            }
        }
        if (viewType.equals("Edit") || viewType.equals("Delete") || viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) ||
        viewType.equals(REJECT) || viewType.equals("Enquirystatus")) {
            if(observable.getProductAuthRemarks().equals("MDS_LOAN") || observable.getProductAuthRemarks().equals("PADDY_LOAN")){
                HashMap shareMap = new HashMap();
                shareMap.put("CUSTOMER ID",CommonUtil.convertObjToStr(observable.getLblCustomerIdValue()));
                displayShareDetails(shareMap);
            }
            if (viewType.equals("Edit")){
                if (observable.getStrACNumber()!=null) {                // Printing
                    String actNum = observable.getStrACNumber();
                    HashMap transTypeMap = new HashMap();
                    HashMap transMap = new HashMap();
                    HashMap transCashMap = new HashMap();
                    transCashMap.put("BATCH_ID",actNum);
                    transCashMap.put("TRANS_DT", curr_dt);
                    transCashMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                    HashMap transIdMap = new HashMap();
                    List list = ClientUtil.executeQuery("getTransferDetails", transCashMap);
                    if(list !=null && list.size()>0){
                        for(int i = 0;i<list.size();i++){
                            transMap = (HashMap)list.get(i);
                            transIdMap.put(transMap.get("SINGLE_TRANS_ID"),"TRANSFER");
                        }
                    }
                    list = ClientUtil.executeQuery("getCashDetails", transCashMap);
                    if(list !=null && list.size()>0){
                        for(int i = 0;i<list.size();i++){
                            transMap = (HashMap)list.get(i);
                            transIdMap.put(transMap.get("SINGLE_TRANS_ID"),"CASH");
                            transTypeMap.put(transMap.get("SINGLE_TRANS_ID"),transMap.get("TRANS_TYPE"));
                        }
                    }
                    int yesNo = 0;
                    String[] voucherOptions = {"Yes", "No"};
                    if(list !=null && list.size()>0){
                       
                        yesNo = COptionPane.showOptionDialog(null,"Do you want to print?", CommonConstants.WARNINGTITLE,
                        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                        null, voucherOptions, voucherOptions[0]);
                        if (yesNo==0) {
                            com.see.truetransact.clientutil.ttrintegration.TTIntegration ttIntgration = null;
                            HashMap paramMap = new HashMap();
                            paramMap.put("TransDt", curr_dt);
                            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                            Object keys[] = transIdMap.keySet().toArray();
                            for (int i=0; i<keys.length; i++) {
                                paramMap.put("TransId", keys[i]);
                                ttIntgration.setParam(paramMap);
                                if (CommonUtil.convertObjToStr(transIdMap.get(keys[i])).equals("TRANSFER")) {
                                    ttIntgration.integrationForPrint("ReceiptPayment");
                                } else if (CommonUtil.convertObjToStr(transTypeMap.get(keys[i])).equals("DEBIT")) {
                                    ttIntgration.integrationForPrint("CashPayment", false);
                                } else {
                                    ttIntgration.integrationForPrint("CashReceipt", false);
                                }
                            }
                        }
                        
                    }
                }
                btnNew_Borrower.setVisible(true);
                setAllBorrowerBtnsEnableDisable(false);
                //Added By Suresh
                chkMobileBankingTLAD.setEnabled(true);
                chkMobileBankingTLADActionPerformed(null);
            }else{
                btnNew_Borrower.setVisible(false);
            }
            //    -----------------
            LinkedHashMap whereMap = new LinkedHashMap();
            whereMap.put("PROD_ID",eachProdId);
            List prodDesclst = ClientUtil.executeQuery("TermLoan.getProdId", whereMap);
            if(prodDesclst!=null && prodDesclst.size()>0){
                whereMap = (LinkedHashMap)prodDesclst.get(0);
                prodDesc1 = CommonUtil.convertObjToStr(whereMap.get("PROD_DESC"));
            }
            editChargeTable();
            if (tableFlag) {
                srpChargeDetails.setEnabled(false);
                table.remove(0);
                table.setEnabled(false);
            }
            btnCustomer(false);
        }
    }
    if(hash.containsKey("FROM_AUTHORIZE_LIST_UI")){
        setAuthBtnEnableDisable();
        btnSave.setEnabled(false);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnView.setEnabled(false);
    }
     if(hash.containsKey("FROM_MANAGER_AUTHORIZE_LIST_UI")){
        setAuthBtnEnableDisable();
        btnSave.setEnabled(false);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnView.setEnabled(false);
    }
    observable.ttNotifyObservers();
    setModified(true);
    if(rejectFlag==1){
           btnReject.setEnabled(false);
       }
}
                                                                                                                                                                 
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
//        authEnableDisable();
        authorizeActionPerformed(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    private void resetUiSecurityDetails(){
        //        lblSLNoValue.setText("");
        
    }
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeActionPerformed(CommonConstants.STATUS_REJECTED);
        
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeActionPerformed(CommonConstants.STATUS_AUTHORIZED);
        
    }//GEN-LAST:event_btnAuthorizeActionPerformed
     public void authorizeStatus(String authorizeStatus) 
     {
              authorizeActionPerformed(authorizeStatus);
     }
       // Actions have to be taken when Authorize button is pressed
    private void authorizeActionPerformed(String authorizeStatus){
        //        tblSanctionDetails.setEnabled(false);
        if ((viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT)) && isFilled){
            // If a record is populated for authorize
            final HashMap singleAuthorizeMap = new HashMap();
            java.util.ArrayList arrList = new java.util.ArrayList();
            HashMap authDataMap = new HashMap();
            
            authDataMap.put("ACCT_NUM", observable.getStrACNumber());
            
            arrList.add(authDataMap);
            ///OD TODAUTHORIZE
            HashMap todMap=new HashMap();
            HashMap finalMap=new HashMap();
            HashMap finalMap1=new HashMap();
            todMap.put("ACCT_NUM",observable.getStrACNumber());
            todMap.put("CURR_DT",(Date)curr_dt.clone());
            List lst=ClientUtil.executeQuery("getSelectExistAccountListUI", todMap);
            if(lst !=null && lst.size()>0){
                ArrayList todauthList=new ArrayList();
                HashMap todExist =(HashMap)lst.get(0);
                todExist.put("AUTHORIZED_BY",TrueTransactMain.USER_ID);
                todauthList.add(todExist);
                //                finalMap.put(CommonConstants.AUTHORIZEDATA,todauthList);
                finalMap1.put("AUTHORIZESTATUS","AUTHORIZED");
                finalMap1.put(CommonConstants.AUTHORIZEDATA,todauthList);
                finalMap1.put("TermLoanUI","TermLoanUI");
                finalMap.put("AUTHORIZEMAP",finalMap1);
                finalMap.put("ACCT_NUM",observable.getStrACNumber());
                //                finalMap1.put(CommonConstants.AUTHORIZEDATA,todauthList);
                finalMap.put("MODE",null);
                finalMap.put(CommonConstants.USER_ID,TrueTransactMain.USER_ID);
                finalMap.put(CommonConstants.BRANCH_ID,TrueTransactMain.BRANCH_ID);
                System.out.println("finalMap1$$$$$"+finalMap);
//                observable.setAdvanceLiablityMap(finalMap);
            }
            
            /*validate select screen lock
             *
             */
            if(validateScreenLock())
                return;
            
            //
            singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
//            if(viewType.equals(REJECT))
//                observable.setPartReject("PARTIALLY_REJECT");
//            else
//                observable.setPartReject("");
             observable.setAllScreenData();
            authorize(singleAuthorizeMap);
                        //Added by sreekrishnan
            CommonUtil comm = new CommonUtil();
            final JDialog loading = comm.addProgressBar();
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                @Override
                protected Void doInBackground() throws InterruptedException /** Execute some operation */
                {
                    try {
                        authorize(singleAuthorizeMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void done() {
                    loading.dispose();
                }
            };
            worker.execute();
            loading.show();
            try {
                worker.get();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            ClientUtil.enableDisable(this, false);
            observable.setAuthorizeMap(null);
             btnCancelActionPerformed(null);
        }else{
            // If no record is populated for authorize
            HashMap mapParam = new HashMap();
            
            HashMap authorizeMapCondition = new HashMap();
            authorizeMapCondition.put("STATUS_BY", TrueTransactMain.USER_ID);
            authorizeMapCondition.put("BRANCH_ID", getSelectedBranchID());
//            authorizeMapCondition.put("AUTHORIZE_REMARK", "OTHER_LOAN");
            authorizeMapCondition.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMapCondition.put("TRANS_DT", ClientUtil.getCurrentDate());
            mapParam.put(CommonConstants.MAP_WHERE, authorizeMapCondition);
//            if (loanType.equals("LTD"))
              //  mapParam.put(CommonConstants.MAP_NAME, "getSelectTermLoanAuthorizeTOListForLTD");
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                authorizeMapCondition.put("AUTH_TRANS_TYPE", "DEBIT");
                mapParam.put(CommonConstants.MAP_NAME, "getSelectTermLoanCashierAuthorizeTOListForLTD");
            } else {
                authorizeMapCondition.put("AUTH_TRANS_TYPE", "DEBIT");
                mapParam.put(CommonConstants.MAP_NAME, "getSelectTermLoanAuthorizeTOListForLTD");
            }
//            else
//                mapParam.put(CommonConstants.MAP_NAME, "getSelectTermLoanAuthorizeTOList");
            if (authorizeStatus.equals(CommonConstants.STATUS_AUTHORIZED)){
                 observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                viewType = AUTHORIZE;
            }else if (authorizeStatus.equals(CommonConstants.STATUS_EXCEPTION)){
                viewType = EXCEPTION;
            }else if (authorizeStatus.equals(CommonConstants.STATUS_REJECTED)){
                viewType = REJECT;
            }
           
           
            isFilled = false;
//            tabLimitAmount.resetVisits();
            observable.setStatus();
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSaveDisable();
            setAuthBtnEnableDisable();
            
            if (viewType.equals(AUTHORIZE)|| viewType.equals(REJECT))
                if (viewType.equals(AUTHORIZE)){
                    this.btnReject.setEnabled(false);
                    this.btnException.setEnabled(false);
                }
            if (viewType.equals(REJECT)){
                this.btnAuthorize.setEnabled(false);
                this.btnException.setEnabled(false);
            }
            authorizeMapCondition = null;
            //__ If there's no data to be Authorized, call Cancel action...
            if(!isModified()){
                setButtonEnableDisable();
                btnCancelActionPerformed(null);
            }
        }
        
    }

    
    private void authEnableDisable(){
        setAllBorrowerBtnsEnableDisable(false);
        setbtnCustEnableDisable(false);
        setAllTablesEnableDisable(true);
        setAllTableBtnsEnableDisable(false); // To disable the Tool buttons Authorized Signatory
        setAllSanctionFacilityEnableDisable(false);
        
        setAllSanctionMainEnableDisable(false);
        setAllFacilityDetailsEnableDisable(false);
        setAllSecurityDetailsEnableDisable(false);
        setAllRepaymentBtnsEnableDisable(false);
        //        setAllGuarantorDetailsEnableDisable(false);
        setAllGuarantorBtnsEnableDisable(false);
        setAllDocumentDetailsEnableDisable(false);
        setDocumentToolBtnEnableDisable(false);
        setAllInterestDetailsEnableDisable(false);
        setAllInterestBtnsEnableDisable(false);
        setAllSettlmentEnableDisable(false);
        setAllClassificationDetailsEnableDisable(false);
        
    }
    
    private boolean tokenValidation(String tokenNo){
        boolean tokenflag = false;
        HashMap tokenWhereMap = new HashMap();// Separating Serias No and Token No
        char[] chrs = tokenNo.toCharArray();
        StringBuffer seriesNo = new StringBuffer();
        int i=0;
        for (int j= chrs.length; i < j; i++ ) {
            if (Character.isDigit(chrs[i]))
                break;
            else
                seriesNo.append(chrs[i]);
        }
        tokenWhereMap.put("SERIES_NO", seriesNo.toString());
        tokenWhereMap.put("TOKEN_NO", CommonUtil.convertObjToInt(tokenNo.substring(i)));
        tokenWhereMap.put("USER_ID", ProxyParameters.USER_ID);
        tokenWhereMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        tokenWhereMap.put("CURRENT_DT", ClientUtil.getCurrentDate());
        List lst = ClientUtil.executeQuery("validateTokenNo", tokenWhereMap);
        if (((Integer) lst.get(0)).intValue() == 0) {
            tokenflag = false;
        }else{
            tokenflag = true;
        }
        return tokenflag;
    }
    
    // Actions have to be taken when Authorize button is pressed
    
    private boolean validateScreenLock(){
        HashMap authDataMap=new HashMap();
//        authDataMap.put("TRANS_ID", observable.getStrACNumber());
//        authDataMap.put("USER_ID",ProxyParameters.USER_ID);
//        authDataMap.put("TRANS_DT", ClientUtil.getCurrentDate());
//        authDataMap.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
//        List lst=ClientUtil.executeQuery("selectauthorizationLock", authDataMap);
//        StringBuffer open=new StringBuffer();
//        if(lst !=null && lst.size()>0) {
//            for(int i=0;i<lst.size();i++){
//                HashMap map=(HashMap)lst.get(i);
//                open.append("\n"+"User Id  :"+" ");
//                open.append(CommonUtil.convertObjToStr(map.get("OPEN_BY"))+"\n");
//                open.append("Mode Of Operation  :" +" ");
//                open.append(CommonUtil.convertObjToStr(map.get("MODE_OF_OPERATION"))+" ");
//            }
//            ClientUtil.showMessageWindow("Already opened by"+open);
//            return true;
//        }
        return false;
    }
    
    private void deletescreenLock(){
        HashMap map=new HashMap();
        map.put("USER_ID",ProxyParameters.USER_ID);
        map.put("TRANS_DT", curr_dt);
        map.put("INITIATED_BRANCH", TrueTransactMain.BRANCH_ID);
        ClientUtil.execute("DELETE_SCREEN_LOCK", map);
    }
    private void btnSaveDisable(){
        btnSave.setEnabled(false);
        mitSave.setEnabled(false);
    }
    public void authorize(HashMap map) {
        String strWarnMsg = null;
//        strWarnMsg = tabLimitAmount.isAllTabsVisited();
//        if (strWarnMsg.length() > 0){
//            displayAlert(strWarnMsg);
//            return;
//        }
        strWarnMsg = null;
        tabLimitAmount.resetVisits();
        observable.setAuthorizeMap(map);
        observable.doAction(1);
        if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            if (fromNewAuthorizeUI) {
                newauthorizeListUI.removeSelectedRow();
                newauthorizeListUI.displayDetails("Deposit Loan Account Opening");
                this.dispose();
                newauthorizeListUI.setFocusToTable();
            }
            if (fromAuthorizeUI) {
                authorizeListUI.removeSelectedRow();
                authorizeListUI.displayDetails("Deposit Loan Account Opening");
                this.dispose();
                 authorizeListUI.setFocusToTable();
            }
            if (fromManagerAuthorizeUI) {
                ManagerauthorizeListUI.removeSelectedRow();
                this.dispose();
            }
        //            String displayStr = "";
        //            String oldBatchId = "";
        //            String newBatchId = "";
        //            String actNum = lblAcctNo_Sanction_Disp.getText();
        //            HashMap transMap = new HashMap();
        //            transMap.put("LOAN_NO",actNum);
        //            transMap.put("CURR_DT", curr_dt);
        //            List lst = ClientUtil.executeQuery("getTransferTransLoanAuthDetails", transMap);
        //            if(lst !=null && lst.size()>0){
        //                displayStr += "Transfer Transaction Details...\n";
        //                for(int i = 0;i<lst.size();i++){
        //                    transMap = (HashMap)lst.get(i);
        //                    displayStr += "Trans Id : "+transMap.get("TRANS_ID")+
        //                    "   Batch Id : "+transMap.get("BATCH_ID")+
        //                    "   Trans Type : "+transMap.get("TRANS_TYPE");
        //                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
        //                    if(actNum != null && !actNum.equals("")){
        //                        displayStr +="   Account No : "+transMap.get("ACT_NUM")+
        //                        "   Deposit Amount : "+transMap.get("AMOUNT")+"\n";
        //                    }else{
        //                        displayStr += "   Account Head : "+transMap.get("AC_HD_ID")+
        //                        "   Interest Amount : "+transMap.get("AMOUNT")+"\n";
        //                    }
        //                    System.out.println("#### :" +transMap);
        //                    oldBatchId = newBatchId;
        //                }
        //            }
        //            actNum = lblAcctNo_Sanction_Disp.getText();
        //            transMap = new HashMap();
        //            transMap.put("LOAN_NO",actNum);
        //            transMap.put("CURR_DT", curr_dt);
        //            lst = ClientUtil.executeQuery("getCashTransLoanAuthDetails", transMap);
        //            if(lst !=null && lst.size()>0){
        //                displayStr += "Cash Transaction Details...\n";
        //                for(int i = 0;i<lst.size();i++){
        //                    transMap = (HashMap)lst.get(i);
        //                    displayStr +="Trans Id : "+transMap.get("TRANS_ID")+
        //                    "   Trans Type : "+transMap.get("TRANS_TYPE");
        //                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
        //                    if(actNum != null && !actNum.equals("")){
        //                        displayStr +="   Account No :  "+transMap.get("ACT_NUM")+
        //                        "   Deposit Amount :  "+transMap.get("AMOUNT")+"\n";
        //                    }else{
        //                        displayStr +="   Account Head :  "+transMap.get("AC_HD_ID")+
        //                        "   Interest Amount :  "+transMap.get("AMOUNT")+"\n";
        //                    }
        //                }
        //            }
        //            if(!displayStr.equals("")){
        //                ClientUtil.showMessageWindow(""+displayStr);
        //            }
            }
        super.setOpenForEditBy(observable.getStatusBy());
        //            super.removeEditLock(lblBorrowerNo_2.getText());
        isFilled = false;
        btnCancelActionPerformed(null);
        //        observable.setResultStatus();
    }
    // Actions have to be taken when a record from Security Details have been chosen
    //    private void tblsecurityTableMousePressed(){
    //        updateOBFields();
    //        if (tblSecurityTable.getSelectedRow() >= 0){
    //            // If the table is in editable mode
    //            setAllSecurityDetailsEnableDisable(true);
    //            setAllSecurityBtnsEnableDisable(true);
    //            String acct_status=CommonUtil.convertObjToStr(((ComboBoxModel)cboAccStatus.getModel()).getKeyForSelected());
    //            observableSecurity.populateSecurityDetails(tblSecurityTable.getSelectedRow());
    //            if ((observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) ||
    //            (viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT) || viewType.equals("Enquirystatus") || acct_status.equals("CLOSED"))){
    //                // If the record is populated for Delete or Authorization
    //                ClientUtil.enableDisable(panSecurityDetails_security,false);
    //                ClientUtil.enableDisable(panEligibleLoan,false);
    //                btnSecurityNew.setEnabled(false);
    //                btnSecuritySave.setEnabled(false);
    //                btnSecurityDelete.setEnabled(false);
    //                btnAppraiserId.setEnabled(false);
    //                txtMargin.setEnabled(false);
    //            }else{
    //                ClientUtil.enableDisable(panSecurityDetails_security,true);
    //                ClientUtil.enableDisable(panEligibleLoan,false);
    //                btnSecurityNew.setEnabled(false);
    //                txtMargin.setEnabled(false);
    //                btnAppraiserId.setEnabled(true);
    //            }
    //            updateSecurity = true;
    //            rowSecurity = tblSecurityTable.getSelectedRow();
    //            selectedRow = rowSecurity;
    //            txtMarketRate.setEnabled(false);
    //            txtSecurityValue.setEnabled(false);
    //        }
    //        observable.ttNotifyObservers();
    //        if(tblSecurityTable.getRowCount()>0){
    //            double totAmt = 0;
    //            for(int i = 0;i<tblSecurityTable.getRowCount();i++){
    //                totAmt += CommonUtil.convertObjToDouble(tblSecurityTable.getValueAt(i, 2)).doubleValue();
    //            }
    //            txtAvalSecVal.setText(String.valueOf(totAmt));
    //            totAmt = 0;
    //            for(int i = 0;i<tblSecurityTable.getRowCount();i++){
    //                totAmt += CommonUtil.convertObjToDouble(tblSecurityTable.getValueAt(i, 3)).doubleValue();
    //            }
    //            txtMarginAmt.setText(String.valueOf(totAmt));
    //            totAmt = 0;
    //            for(int i = 0;i<tblSecurityTable.getRowCount();i++){
    //                totAmt += CommonUtil.convertObjToDouble(tblSecurityTable.getValueAt(i, 4)).doubleValue();
    //            }
    //            txtEligibleLoan.setText(String.valueOf(totAmt));
    //            //            String mainLimit = CommonUtil.convertObjToStr(txtSanctionLimitValue.getText());
    ////            txtSanctionLimitValue.setText(String.valueOf(txtLimit_SD.getText()));
    //        }
    //        tabLimitAmount.setSelectedComponent(panSecurityDetails);
    //    }    // Actions have to be taken when Security Details Delete button pressed
    //    private void btnsecurityDeleteActionPerformed(){
    //        updateOBFields();
    //        if(tblSecurityTable.getRowCount()>0){
    //            double existingLoanAmt = CommonUtil.convertObjToDouble(observableSecurity.getTotalEligibleLoanAmt()).doubleValue();
    //            double existingMarginAmt = CommonUtil.convertObjToDouble(observableSecurity.getTotalMarginAmt()).doubleValue();
    //            double existingAvailAmt = CommonUtil.convertObjToDouble(observableSecurity.getTotalSecurityValue()).doubleValue();
    //            double availSecAmt = CommonUtil.convertObjToDouble(tblSecurityTable.getValueAt(rowSecurity,2)).doubleValue();
    //            double marginAmt = CommonUtil.convertObjToDouble(tblSecurityTable.getValueAt(rowSecurity,3)).doubleValue();
    //            double eligibleLoan = CommonUtil.convertObjToDouble(tblSecurityTable.getValueAt(rowSecurity,4)).doubleValue();
    //            double presentAvailAmt = existingAvailAmt - availSecAmt;
    //            double presentMarginAmt = existingMarginAmt - marginAmt;
    //            double presentLoanAmt = existingLoanAmt - eligibleLoan;
    //            observableSecurity.setTotalEligibleLoanAmt(String.valueOf(presentLoanAmt));
    //            observableSecurity.setTotalSecurityValue(String.valueOf(presentAvailAmt));
    //            observableSecurity.setTotalMarginAmt(String.valueOf(presentMarginAmt));
    //            txtEligibleLoan.setText(observableSecurity.getTotalEligibleLoanAmt());
    //            txtAvalSecVal.setText(observableSecurity.getTotalSecurityValue());
    //            txtMarginAmt.setText(observableSecurity.getTotalMarginAmt());
    //            observableSecurity.deleteSecurityTabRecord(rowSecurity);
    //        }
    //        //        setAllSecurityDetailsEnableDisable(false);
    //        setSecurityBtnsOnlyNewEnable();
    //        updateSecurity = false;
    //        rowSecurity = -1;
    //        observableSecurity.resetSecurityDetails();
    //        observable.ttNotifyObservers();
    //        tabLimitAmount.setSelectedComponent(panSecurityDetails);
    //        ClientUtil.enableDisable(panSecurityDetails_security,false);
    //        notSavedRecords = false;
    //    }    // Actions have to be taken when Security Details Save button pressed
    //    private void btnsecuritySaveActionPerformed(){
    ////        if(tdtAson.getDateValue().length() == 0){
    ////            ClientUtil.showAlertWindow("As on Date should not be empty");
    ////            return;
    ////        }else
    //         if(txtGrossWeight.getText().length() == 0){
    //            ClientUtil.showAlertWindow("Gross weight should not be empty");
    //            return;
    //        }else if(txtNetWeight.getText().length() == 0){
    //            ClientUtil.showAlertWindow("Net weight should not be empty");
    //            return;
    //        }else if(cboPurityOfGold.getSelectedIndex()<=0){
    //            ClientUtil.showAlertWindow("Purity of gold should not be empty");
    //            return;
    //        }else if(txtMarketRate.getText().length() == 0){
    //            ClientUtil.showAlertWindow("Market rate should not be empty");
    //            return;
    //        }else if(txtSecurityValue.getText().length() == 0){
    //            ClientUtil.showAlertWindow("Security Amount should not be empty");
    //            return;
    //        }else if(txtMargin.getText().length() == 0){
    //            ClientUtil.showAlertWindow("Margin should not be empty");
    //        }else if(txtAreaParticular.getText().length() == 0){
    //            ClientUtil.showAlertWindow("Particular should not be empty");
    //            return;
    //        }else{
    //            updateOBFields();
    //            if(tblSecurityTable.getRowCount()>0){
    //                totSecurityValue = 0;
    //                totMarginAmt = 0;
    //                totEligibleAmt = 0;
    //                if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW ||observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
    //                    if(newRecordAdding == true){
    //                        for(int i = 0;i<tblSecurityTable.getRowCount();i++){
    //                            totSecurityValue += CommonUtil.convertObjToDouble(tblSecurityTable.getValueAt(i, 2)).doubleValue();
    //                        }
    //                        totSecurityValue += CommonUtil.convertObjToDouble(getTotalSecValue()).doubleValue();
    //                        txtAvalSecVal.setText(String.valueOf(totSecurityValue));
    //                        observableSecurity.setTotalSecurityValue(String.valueOf(totSecurityValue));
    //                        for(int i = 0;i<tblSecurityTable.getRowCount();i++){
    //                            totMarginAmt += CommonUtil.convertObjToDouble(tblSecurityTable.getValueAt(i, 3)).doubleValue();
    //                        }
    //                        totMarginAmt += CommonUtil.convertObjToDouble(getTotalMarginValue()).doubleValue();
    //                        observableSecurity.setTxtMarginAmt(getTotalMarginValue());
    //                        txtMarginAmt.setText(String.valueOf(totMarginAmt));
    //                        observableSecurity.setTotalMarginAmt(String.valueOf(totMarginAmt));
    //                        for(int i = 0;i<tblSecurityTable.getRowCount();i++){
    //                            totEligibleAmt += CommonUtil.convertObjToDouble(tblSecurityTable.getValueAt(i, 4)).doubleValue();
    //                        }
    //                        totEligibleAmt += CommonUtil.convertObjToDouble(getTotalEligibleValue()).doubleValue();
    //                        txtEligibleLoan.setText(String.valueOf(totEligibleAmt));
    //                        observableSecurity.setTotalEligibleLoanAmt(String.valueOf(totEligibleAmt));
    //                        observableSecurity.setTxtEligibleLoanAmt(getTotalEligibleValue());
    //                    }else{
    //                        for(int i = 0;i<tblSecurityTable.getRowCount();i++){
    //                            if(selectedRow != i){
    //                                totSecurityValue += CommonUtil.convertObjToDouble(tblSecurityTable.getValueAt(i, 2)).doubleValue();
    //                            }else{
    //                                totSecurityValue += CommonUtil.convertObjToDouble(getTotalSecValue()).doubleValue();
    //                            }
    //                        }
    //                        txtAvalSecVal.setText(String.valueOf(totSecurityValue));
    //                        observableSecurity.setTotalSecurityValue(String.valueOf(totSecurityValue));
    //                        for(int i = 0;i<tblSecurityTable.getRowCount();i++){
    //                            if(selectedRow != i){
    //                                totMarginAmt += CommonUtil.convertObjToDouble(tblSecurityTable.getValueAt(i, 3)).doubleValue();
    //                            }else{
    //                                totMarginAmt += CommonUtil.convertObjToDouble(getTotalMarginValue()).doubleValue();
    //                            }
    //                        }
    //                        observableSecurity.setTxtMarginAmt(getTotalMarginValue());
    //                        txtMarginAmt.setText(String.valueOf(totMarginAmt));
    //                        observableSecurity.setTotalMarginAmt(String.valueOf(totMarginAmt));
    //                        for(int i = 0;i<tblSecurityTable.getRowCount();i++){
    //                            if(selectedRow != i){
    //                                totEligibleAmt += CommonUtil.convertObjToDouble(tblSecurityTable.getValueAt(i, 4)).doubleValue();
    //                            }else{
    //                                totEligibleAmt += CommonUtil.convertObjToDouble(getTotalEligibleValue()).doubleValue();
    //                            }
    //                        }
    //                        observableSecurity.setTxtEligibleLoanAmt(getTotalEligibleValue());
    //                        txtEligibleLoan.setText(String.valueOf(totEligibleAmt));
    //                        observableSecurity.setTotalEligibleLoanAmt(String.valueOf(totEligibleAmt));
    //                    }
    //                }
    //                if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
    //
    //                }
    //            }else{
    //                txtAvalSecVal.setText(String.valueOf(getTotalSecValue()));
    //                observableSecurity.setTotalSecurityValue(getTotalSecValue());
    //                txtMarginAmt.setText(String.valueOf(getTotalMarginValue()));
    //                observableSecurity.setTotalMarginAmt(getTotalMarginValue());
    //                observableSecurity.setTxtMarginAmt(getTotalMarginValue());
    //                txtEligibleLoan.setText(String.valueOf(getTotalEligibleValue()));
    //                observableSecurity.setTotalEligibleLoanAmt(getTotalEligibleValue());
    //                observableSecurity.setTxtEligibleLoanAmt(getTotalEligibleValue());
    //            }
    //            if (observableSecurity.addSecurityDetails(rowSecurity, updateSecurity) == 1){
    //                // Donot reset the fields when return value is 1(Option is No for replacing the existing record)
    //            }else{
    //                // To reset the Fields
    //                setAllSecurityDetailsEnableDisable(false);
    //                setSecurityBtnsOnlyNewEnable();
    //                updateSecurity = false;
    //                rowSecurity = -1;
    //                observableSecurity.resetSecurityDetails();
    //            }
    //            observable.ttNotifyObservers();
    //            ClientUtil.enableDisable(panSecurityDetails_security,false);
    //            notSavedRecords = false;
    //            newRecordAdding = false;
    //            totSecurityValue = 0;
    //            totMarginAmt = 0;
    //            totEligibleAmt = 0;
    //        }
    //    }    // Actions have to be taken when Security Details New button is pressed
    //    private void btnsecurityNewActionPerformed(){
    //        updateOBFields();
    //        updateSecurity = false;
    //        observableSecurity.resetSecurityDetails();
    //        setAllSecurityDetailsEnableDisable(true);
    //        setSecurityBtnsOnlyNewSaveEnable();
    //        setSecurityDefaultValWhenNewBtnPressed();
    //        rowSecurity = -1;
    //        observable.ttNotifyObservers();
    //        tabLimitAmount.setSelectedComponent(panSecurityDetails);
    //        ClientUtil.enableDisable(panSecurityDetails_security,true);
    //        ClientUtil.enableDisable(panEligibleLoan,false);
    //        txtMargin.setEnabled(false);
    //        btnSecurityNew.setEnabled(false);
    //        btnAppraiserId.setEnabled(true);
    //        //        txtMargin.setText(String.valueOf("25"));
    //        txtMarketRate.setEnabled(false);
    //        txtSecurityValue.setEnabled(false);
    ////        txtSanctionLimitValue.setEnabled(true);
    ////        tdtAson.setDateValue(DateUtil.getStringDate(curr_dt));
    //        if(tblSecurityTable.getRowCount() == 0){
    //            HashMap marginMap = new HashMap();
    //            marginMap.put("PROD_ID",eachProdId);
    //            List lst = ClientUtil.executeQuery("getSelectMarginPercentage", marginMap);
    //            if(lst!=null && lst.size()>0){
    //                marginMap = (HashMap)lst.get(0);
    //                txtMargin.setText(CommonUtil.convertObjToStr(marginMap.get("MARGIN")));
    //            }
    ////            lblSLNoValue.setText(String.valueOf("1"));
    //        }else{
    //            int count = tblSecurityTable.getRowCount()+1;
    ////            lblSLNoValue.setText(String.valueOf(count));
    //        }
    //        notSavedRecords = true;
    //        txtMargin.setEnabled(false);
    ////        txtSanctionLimitValue.setEnabled(false);
    //        newRecordAdding = true;
    //    }
    
    private void setSecurityDefaultValWhenNewBtnPressed(){
        //        observableSecurity.setTdtFromDate(tdtFDate.getDateValue());
        //        tdtFromDate.setDateValue(observableSecurity.getTdtFromDate());
        //        observableSecurity.setTdtToDate(tdtTDate.getDateValue());
        //        tdtToDate.setDateValue(observableSecurity.getTdtToDate());
    }
    
    // Actions have to be taken when a record of Facility Details is selected in Facility Table(Sanction Details)
    
    
    
//    private boolean checkInterestRateForLTD() {
//        HashMap whereMap = new HashMap();
//        //        whereMap.put("CATEGORY_ID", observableBorrow.getCbmCategory().getKeyForSelected());
//        //        if(CommonUtil.convertObjToDouble(observable.getTxtLimit_SD()).doubleValue()>0)
//        //            whereMap.put("AMOUNT", getBigDecimal(CommonUtil.convertObjToDouble(observable.getTxtLimit_SD()).doubleValue()));
//        //        else
//        //            whereMap.put("AMOUNT", getBigDecimal(CommonUtil.convertObjToDouble(txtLimit_SD.getText()).doubleValue()));
//        //        whereMap.put("PROD_ID", CommonUtil.convertObjToStr(observable.getCbmProductId().getKeyForSelected()));//observable.getLblProductID_FD_Disp());
//        //        //        whereMap.put("FROM_DATE", getTimestamp(DateUtil.getDateMMDDYYYY(tdtFDate.getDateValue())));
//        //        //        whereMap.put("TO_DATE", getTimestamp(DateUtil.getDateMMDDYYYY(tdtTDate.getDateValue())));
//        //        //            deleteAllInterestDetails();
//        //        //            observableInt.resetInterestDetails();
//        //        //            updateInterestDetails();
//        //        // Populate the values
//        //        ArrayList interestList = (java.util.ArrayList)ClientUtil.executeQuery("getSelectProductTermLoanInterestTO", whereMap);
//        //        observableInt.setIsNew(true);
//        //        if (interestList==null || interestList.size()==0) {
//        //            displayAlert("Interest rates not created for this product...");
//        //            return true;
//        //        }
//        return false;
//    }
    
    // Security Details, Repayment Schedule will be populated on the basis of Account Number
    //    private void displayTabsByAccountNumber(){
    //        final HashMap hash = new HashMap();
    //        updateOBFields();
    //        if (observable.getStrACNumber().length() > 0){
    //            // Retrieve the values on the basis of Account Number
    //            hash.put("WHERE", observable.getStrACNumber());
    //            hash.put("KEY_VALUE", "ACCOUNT_NUMBER");
    //
    //            //            observable.populateData(hash, authSignUI.getAuthorizedSignatoryOB(), poaUI.getPowerOfAttorneyOB());
    //
    //        }else{
    //            btnsDisableBasedOnAccountNumber();
    //            setDefaultValB4AcctCreation();
    //            observableClassi.populateClassiDetailsFromProd();
    //            observableClassi.setClassifiDetails(CommonConstants.TOSTATUS_INSERT);
    ////            observableOtherDetails.setOtherDetailsMode(CommonConstants.TOSTATUS_INSERT);
    //            updateProdClassiFields();
    //            //            ClientUtil.enableDisable(panAccountDetails, false);
    //        }
    //        //        authSignUI.resetDisableNoOfAuthSign(true);
    //        // This will populate the customer details in account level tabs
    //        populateCustomerProdLeveFields();
    //        observable.ttNotifyObservers();
    //    }
    
    private void populateCustomerProdLeveFields(){
        // This will populate Group description field
        //        observable.populateCustomerProdLeveFields();
    }
    
    private void enableDisableGetIntFrom(boolean val){
        //        cboIntGetFrom.setEnabled(val);
    }
    private void setSanctionProductDetailsDisable(){
        //        cboTypeOfFacility.setEnabled(false);
        //        cboProductId.setEnabled(false);
    }
    
    private void setDefaultValB4AcctCreation(){
        //        observable.setDefaultValB4AcctCreation();
        //        tdtDemandPromNoteDate.setDateValue(observable.getTdtDemandPromNoteDate());
        //        calculateDPNExpDate();
        //        observableClassi.setDefaultValB4AcctCreation();
        //        observableOtherDetails.populateProdLevelValB4AcctCreation(CommonUtil.convertObjToStr(((ComboBoxModel) cboProductId.getModel()).getKeyForSelected()));
    }
    
    private void btnsDisableBasedOnAccountNumber(){
        enableDisableGetIntFrom(false);
        //        tdtAODDate.setEnabled(false);
        setAllSecurityDetailsEnableDisable(false);
        setAllSecurityBtnsEnableDisable(false);
        //        setAllRepaymentDetailsEnableDisable(false);
        //        btnEMI_Calculate.setEnabled(false);
        //        setAllInsuranceDetailsEnableDisable(false);
        //        setAllInsuranceBtnsEnableDisable(false);
        setAllRepaymentBtnsEnableDisable(false);
        //        setAllGuarantorDetailsEnableDisable(false);
        //        setAllInstitGuarantorDetailsEnableDisable(false);
        setAllGuarantorBtnsEnableDisable(false);
        setAllDocumentDetailsEnableDisable(false);
        setDocumentToolBtnEnableDisable(false);
        setAllInterestDetailsEnableDisable(false);
        setAllInterestBtnsEnableDisable(false);
        setAllSettlmentEnableDisable(false);
        setAllClassificationDetailsEnableDisable(false);
        //        additionalSanctionbtnEnableDisable(false);
        //        additionalSanctionEnableDisable(false);
    }
    
    private void resetTabsDependsOnAccountNumber(){
        
    }
    private void cboProductIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductIdActionPerformed
        // Add your handling code here:
        cboProductIDActionPerformed();
    }//GEN-LAST:event_cboProductIdActionPerformed
    private void cboProductIDActionPerformed(){
        
    }
    private void checkShareHolder(){
        //        String selectKey=CommonUtil.convertObjToStr(((ComboBoxModel)cboProductId.getModel()).getKeyForSelected());
        
    }
    private void updateProdClassiFields(){
        //        removeFacilitySecurityRadioBtns();
        //        rdoSecurityDetails_Fully.setSelected(observable.getRdoSecurityDetails_Fully());
        //        rdoSecurityDetails_Partly.setSelected(observable.getRdoSecurityDetails_Partly());
        //        rdoSecurityDetails_Unsec.setSelected(observable.getRdoSecurityDetails_Unsec());
        //        addFacilitySecurityRadioBtns();
        //
        //        cboCommodityCode.setSelectedItem(observableClassi.getCboCommodityCode());
        //        cboGuaranteeCoverCode.setSelectedItem(observableClassi.getCboGuaranteeCoverCode());
        //        cboSectorCode1.setSelectedItem(observableClassi.getCboSectorCode1());
        //        cboHealthCode.setSelectedItem(observableClassi.getCboHealthCode());
        //        cboTypeFacility.setSelectedItem(observableClassi.getCboTypeFacility());
        //        cboPurposeCode.setSelectedItem(observableClassi.getCboPurposeCode());
        //        cboIndusCode.setSelectedItem(observableClassi.getCboIndusCode());
        //        cboWeakerSectionCode.setSelectedItem(observableClassi.getCboWeakerSectionCode());
        //        cbo20Code.setSelectedItem(observableClassi.getCbo20Code());
        //        cboRefinancingInsti.setSelectedItem(observableClassi.getCboRefinancingInsti());
        //        cboGovtSchemeCode.setSelectedItem(observableClassi.getCboGovtSchemeCode());
        //        //        cboAssetCode.setSelectedItem(observableClassi.getCboAssetCode());
        //        chkDirectFinance.setSelected(observableClassi.getChkDirectFinance());
        //        chkECGC.setSelected(observableClassi.getChkECGC());
        //        chkPrioritySector.setSelected(observableClassi.getChkPrioritySector());
        //        chkQIS.setSelected(observableClassi.getChkQIS());
    }
    
    private void updateAccHead_ProdID(){
        //        lblProdID_Disp_DocumentDetails.setText(observableDocument.getLblProdID_Disp_DocumentDetails());
        //        lblProdID_Disp_ODetails.setText(observableOtherDetails.getLblProdID_Disp_ODetails());
        //        lblProductID_FD_Disp.setText(observable.getLblProductID_FD_Disp());
        //        lblAcctHead_Disp_DocumentDetails.setText(observableDocument.getLblAcctHead_Disp_DocumentDetails());
        //        lblAcctHead_Disp_ODetails.setText(observableOtherDetails.getLblAcctHead_Disp_ODetails());
        //        lblAccountHead_FD_Disp.setText(observable.getLblAccountHead_FD_Disp());
        //        lblAccHeadSec_2.setText(observableSecurity.getLblAccHeadSec_2());
        //        lblAccHead_RS_2.setText(observableRepay.getLblAccHead_RS_2());
        //        lblAccHead_GD_2.setText(observableGuarantor.getLblAccHead_GD_2());
        //        lblAccHead_IM_2.setText(observableInt.getLblAccHead_IM_2());
        //        lblAccHead_CD_2.setText(observableClassi.getLblAccHead_CD_2());
        //        lblProdId_Disp.setText(observableSecurity.getLblProdId_Disp());
        //        lblProdID_RS_Disp.setText(observableRepay.getLblProdID_RS_Disp());
        //        lblProdID_GD_Disp.setText(observableGuarantor.getLblProdID_GD_Disp());
        //        lblProdID_IM_Disp.setText(observableInt.getLblProdID_IM_Disp());
        //        lblProID_CD_Disp.setText(observableClassi.getLblProdID_CD_Disp());
    }
    
    private void updateOtherDetailsTab(){
        //        chkChequeBookAD.setSelected(observableOtherDetails.getChkChequeBookAD());
        //        chkCustGrpLimitValidationAD.setSelected(observableOtherDetails.getChkCustGrpLimitValidationAD());
        //        chkMobileBankingAD.setSelected(observableOtherDetails.getChkMobileBankingAD());
        //        chkNROStatusAD.setSelected(observableOtherDetails.getChkNROStatusAD());
        //        chkATMAD.setSelected(observableOtherDetails.getChkATMAD());
        //        txtATMNoAD.setText(observableOtherDetails.getTxtATMNoAD());
        //        tdtATMFromDateAD.setDateValue(observableOtherDetails.getTdtATMFromDateAD());
        //        tdtATMToDateAD.setDateValue(observableOtherDetails.getTdtATMToDateAD());
        //        chkDebitAD.setSelected(observableOtherDetails.getChkDebitAD());
        //        txtDebitNoAD.setText(observableOtherDetails.getTxtDebitNoAD());
        //        tdtDebitFromDateAD.setDateValue(observableOtherDetails.getTdtDebitFromDateAD());
        ////        tdtDebitToDateAD.setDateValue(observableOtherDetails.getTdtDebitToDateAD());
        //        chkCreditAD.setSelected(observableOtherDetails.getChkCreditAD());
        //        txtCreditNoAD.setText(observableOtherDetails.getTxtCreditNoAD());
        //        tdtCreditFromDateAD.setDateValue(observableOtherDetails.getTdtCreditFromDateAD());
        //        tdtCreditToDateAD.setDateValue(observableOtherDetails.getTdtCreditToDateAD());
        //        cboSettlementModeAI.setSelectedItem(observableOtherDetails.getCboSettlementModeAI());
        //        cboOpModeAI.setSelectedItem(observableOtherDetails.getCboOpModeAI());
        //        txtAccOpeningChrgAD.setText(observableOtherDetails.getTxtAccOpeningChrgAD());
        //        txtMisServiceChrgAD.setText(observableOtherDetails.getTxtMisServiceChrgAD());
        //        chkStopPmtChrgAD.setSelected(observableOtherDetails.getChkStopPmtChrgAD());
        //        txtChequeBookChrgAD.setText(observableOtherDetails.getTxtChequeBookChrgAD());
        //        chkChequeRetChrgAD.setSelected(observableOtherDetails.getChkChequeRetChrgAD());
        //        txtFolioChrgAD.setText(observableOtherDetails.getTxtFolioChrgAD());
        //        chkInopChrgAD.setSelected(observableOtherDetails.getChkInopChrgAD());
        //        txtAccCloseChrgAD.setText(observableOtherDetails.getTxtAccCloseChrgAD());
        //        chkStmtChrgAD.setSelected(observableOtherDetails.getChkStmtChrgAD());
        //        cboStmtFreqAD.setSelectedItem(observableOtherDetails.getCboStmtFreqAD());
        //        chkNonMainMinBalChrgAD.setSelected(observableOtherDetails.getChkNonMainMinBalChrgAD());
        //        txtExcessWithChrgAD.setText(observableOtherDetails.getTxtExcessWithChrgAD());
        //        chkABBChrgAD.setSelected(observableOtherDetails.getChkABBChrgAD());
        //        chkNPAChrgAD.setSelected(observableOtherDetails.getChkNPAChrgAD());
        //        txtABBChrgAD.setText(observableOtherDetails.getTxtABBChrgAD());
        //        tdtNPAChrgAD.setDateValue(observableOtherDetails.getTdtNPAChrgAD());
        //        txtMinActBalanceAD.setText(observableOtherDetails.getTxtMinActBalanceAD());
        //        tdtDebit.setDateValue(observableOtherDetails.getTdtDebit());
        //        tdtCredit.setDateValue(observableOtherDetails.getTdtCredit());
        //        chkPayIntOnCrBalIN.setSelected(observableOtherDetails.getChkPayIntOnCrBalIN());
        //        chkPayIntOnDrBalIN.setSelected(observableOtherDetails.getChkPayIntOnDrBalIN());
        //        lblRateCodeValueIN.setText(observableOtherDetails.getLblRateCodeValueIN());
        //        lblCrInterestRateValueIN.setText(observableOtherDetails.getLblCrInterestRateValueIN());
        //        lblDrInterestRateValueIN.setText(observableOtherDetails.getLblDrInterestRateValueIN());
        //        lblPenalInterestValueIN.setText(observableOtherDetails.getLblPenalInterestValueIN());
        //        lblAgClearingValueIN.setText(observableOtherDetails.getLblAgClearingValueIN());
    }
    private void btnFacilityDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFacilityDeleteActionPerformed
        // Add your handling code here:
        btnFacilityDeletePressed();
    }//GEN-LAST:event_btnFacilityDeleteActionPerformed
    private void btnFacilityDeletePressed(){
        
    }
    private void btnFacilitySaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFacilitySaveActionPerformed
        // Add your handling code here:
        btnFacilitySavePressed();
    }//GEN-LAST:event_btnFacilitySaveActionPerformed
    private boolean btnFacilitySavePressed(){
        //changeplace to 12###@
        facilitySaved = false;
        facilityFlag=false;
        sanction=true;
        sandetail=true;
        santab=true;
        sanfacTab=true;
        //        System.out.println("@@@@ tblSecurityTable.getRowCount() : "+tblSecurityTable.getRowCount());
        boolean isWarnMsgExist = false;
        if (rowSanctionMain == -1){
            displayAlert(resourceBundle.getString("existenceSancDetailsTableWarning"));
            isWarnMsgExist = true;
            return isWarnMsgExist;
        }
        final String mandatoryMessage1 = new MandatoryCheck().checkMandatory(getClass().getName(), panBorrowProfile_CustName);
        /* mandatoryMessage1 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        
        //        final String mandatoryMessage2 = new MandatoryCheck().checkMandatory(getClass().getName(), panFDAccount);
        /* mandatoryMessage2 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        
        //        final String mandatoryMessage3 = new MandatoryCheck().checkMandatory(getClass().getName(), panFDDate);
        /* mandatoryMessage3 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        
        
        
        //            if (loanType.equals("LTD")) {
        //            if (facilitySaved || observable.getActionType()!=ClientConstants.ACTIONTYPE_CANCEL) {
        //                if (tblInterMaintenance.getRowCount()<1) {
        //                    cboIntGetFrom.setSelectedItem(((ComboBoxModel)cboIntGetFrom.getModel()).getDataForKey("PROD"));
        //                    observable.setCboIntGetFrom(CommonUtil.convertObjToStr(cboIntGetFrom.getSelectedItem()));
        //                    java.util.LinkedHashMap intFromProd = observableInt.getInterestAll();
        //                    cboIntGetFrom.setSelectedItem(((ComboBoxModel)cboIntGetFrom.getModel()).getDataForKey("ACT"));
        //                    btnInterestMaintenanceNewActionPerformed(null);
        //                    if(intFromProd.size()!=0) {
        //                        HashMap actIntDet = new HashMap();
        //                        actIntDet = (HashMap)intFromProd.get("1");
        //                        tdtFrom.setDateValue(CommonUtil.convertObjToStr(actIntDet.get("FROM_DATE")));
        //                        tdtTo.setDateValue(CommonUtil.convertObjToStr(actIntDet.get("TO_DATE")));
        //                        txtFromAmt.setText(CommonUtil.convertObjToStr(actIntDet.get("FROM_AMOUNT")));
        //                        txtToAmt.setText(CommonUtil.convertObjToStr(actIntDet.get("TO_AMOUNT")));
        //                        txtInter.setText(CommonUtil.convertObjToStr(actIntDet.get("INTEREST")));
        //                        txtPenalInter.setText(CommonUtil.convertObjToStr(actIntDet.get("PENALTY_INTEREST")));
        //                        txtAgainstClearingInter.setText(CommonUtil.convertObjToStr(actIntDet.get("AGAINST_CLEAR_INT")));
        //                        txtPenalStatement.setText(CommonUtil.convertObjToStr(actIntDet.get("PENAL_STATEMENT")));
        //                        txtInterExpLimit.setText(CommonUtil.convertObjToStr(actIntDet.get("INTER_EXP_LIMIT")));
        //                    }
        //                    btnInterestMaintenanceSaveActionPerformed(null);
        //                    if (tblRepaymentCTable.getRowCount()<1) {
        //                        btnRepayment_NewActionPerformed();
        //                        btnRepayment_SaveActionPerformed();
        //                    }
        //                }
        //            }
        //            }
        //            mandatoryMessage4 = new MandatoryCheck().checkMandatory(getClass().getName(), panProd_IM);
        //            mandatoryMessage7 = isInterestDetailsExistForThisAcct();
        //            mandatoryMessage8 = validateOtherDetailsMandatoryFields();
        //        }
        //
        //        //        if (mandatoryMessage1.length() > 0 || mandatoryMessage2.length() > 0 || mandatoryMessage3.length() > 0 || mandatoryMessage4.length() > 0 || mandatoryMessage5.length() > 0 || mandatoryMessage6.length() > 0 || mandatoryMessage7.length() > 0 || mandatoryMessage8.length() > 0){
        //        //            displayAlert(mandatoryMessage1+mandatoryMessage2+mandatoryMessage3+mandatoryMessage4+mandatoryMessage5+mandatoryMessage6+mandatoryMessage7+mandatoryMessage8);
        //        isWarnMsgExist = true;
        //        //        }else{
        //
        //        //change from line 12#### top check
        //        //        System.out.println("@@@@ tblSecurityTable.getRowCount() : "+tblSecurityTable.getRowCount());
        //        //            if (isTablesInEditMode(false) && allCTablesNotNull() && checkForSecurityValue() && repayTableNotNull() && repayTableLimitCheckingRule()
        //        //            && observable.checkMaxAmountRange(tdtTDate.getDateValue())){
        //        updateOBFields();
        //        if(loanType.equals("LTD") )//|| loanType.equals("OTHERS"))
        //            rowFacilityTabSanction=0; //BY ABI FOR ONE MORE LOAN SANCTION NOT UPDATE
        //        else
        //            rowFacilityTabSanction=rowsan;
        //        observable.addFacilityDetails(rowFacilityTabSanction, rowFacilityTabFacility);
        //        updateOBFields();
        //
        //        //                if(observable.getStrACNumber().length()>0)
        //        //                    agriSubSidyUI.updateOBFields();
        //
        //        observable.doAction(nomineeUi.getNomineeOB(),2);
        //        //                if (observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED && loanType.equals("LTD"))
        //        //                    btnFacilitySave.setEnabled(true);
        //        //                else
        //        //                    btnFacilitySave.setEnabled(false);
        //
        //        facilitySaved = true;
        //        //        authSignUI.setLblStatus(observable.getLblStatus());
        //        //        poaUI.setLblStatus(observable.getLblStatus());
        //        observable.resetSanctionFacility();
        //        resetTabsDependsOnAccountNumber();
        //        observable.resetAllFacilityDetails();
        //        updateCboTypeOfFacility();
        //        observable.setBorrowerNumber();
        //        observable.setResultStatus();
        //        setAllSanctionFacilityEnableDisable(false);
        //        setSanctionFacilityBtnsEnableDisable(false);
        //        //                btnNew1.setEnabled(true);
        //        setAllFacilityDetailsEnableDisable(false);
        //        setFacilityBtnsEnableDisable(false);
        //        btnsDisableBasedOnAccountNumber();
        //        //                rowFacilityTabSanction = tblSanctionDetails2.getSelectedRow();
        //        observable.ttNotifyObservers();
        //        updateRdoSubsidyAndInterestNature();
        //        observable.setStrACNumber(observable.getLoanACNo());
        //        lblAcctNo_Sanction_Disp.setText(observable.getLoanACNo());
        //        //                if(loanType.equals("OTHERS") ){    //ltd and testing for same abi
        //        btnSave2_SDActionPerformed(null);
        //        tblSanctionDetails2MousePressed(null);
        //        tblSanctionDetailsMousePressed(null);
        //        System.out.println("####%%%% observable.getCbmProductId().getSelectedItem() "+observable.getCbmProductId().getSelectedItem());
        //        //                System.out.println("####%%%% cboProductId.getSelectedItem() "+cboProductId.getSelectedItem());
        //        //                    cboProductId.setSelectedItem(observable.getCbmProductId().getSelectedItem());
        //        //                    cboProductIdActionPerformed(null);
        //        //                }
        //        if (observable.isLienChanged()) {
        //            tblSanctionDetails2MousePressed(null);
        //        }
        //        //            }else{
        //        isWarnMsgExist = true;
        //        //            }
        //        //        }
        //        //        facilityFlag=true;
        //        if(observable.getStrACNumber() !=null && observable.getStrACNumber().length()>0)
        //            txtSanctionNo.setEnabled(false);
        return isWarnMsgExist;
    }
    //    private boolean repayTableLimitCheckingRule(){
    //        //        tblRepaymentCTable.getSelectionModel().
    //        HashMap singleMap=null;
    //        double totalRepaymentLimit=0;
    //        double totalAdditionalLimit=0;
    //        String behaves=getCboTypeOfFacilityKeyForSelected();
    //        System.out.print("repaytable###"+behaves);
    //        if(loanType.equals("LTD") && observable.getStrACNumber().length()>0)
    //            if(behaves !=null && (!(behaves.equals("OD") || behaves.equals("CC")))){
    //                LinkedHashMap repaymentMap=observableRepay.getTableUtilRepayment().getAllValues();
    //                LinkedHashMap additionalSanctionMap=observableAdditionalSanctionOB.getAdditionalSanUtil().getAllValues();
    //                ArrayList repaymentList=observableRepay.getTableUtilRepayment().getTableValues();
    //                ArrayList additionalSanctionList=observableAdditionalSanctionOB.getAdditionalSanUtil().getTableValues();
    //                System.out.println(additionalSanctionMap+"repaymentMap  ####"+repaymentMap);
    //                System.out.println("\n"+additionalSanctionList+"repaymentList  ####"+repaymentList);
    //                java.util.Set set=  repaymentMap.keySet();
    //                Object objKeySet[]=(Object[])set.toArray();
    //                if(repaymentMap !=null && repaymentMap.size()>0){
    //                    for(int i=0;i<repaymentMap.size();i++){
    //                        singleMap=(HashMap)repaymentMap.get(objKeySet[i]);
    //                        totalRepaymentLimit+=CommonUtil.convertObjToDouble(singleMap.get("TOT_BASE_AMT")).doubleValue();
    //                    }
    //                    System.out.println("totalRepaymentLimit   "+totalRepaymentLimit);
    //
    //                }
    //                set =  additionalSanctionMap.keySet();
    //                Object objKeySets[]=(Object[])set.toArray();
    //
    //                if(additionalSanctionMap !=null && additionalSanctionMap.size()>0){
    //                    for(int i=0;i<additionalSanctionMap.size();i++){
    //                        singleMap=(HashMap)additionalSanctionMap.get(objKeySets[i]);
    //                        totalAdditionalLimit+=CommonUtil.convertObjToDouble(singleMap.get("ADDITIONAL LIMIT")).doubleValue();
    //                    }
    //                    System.out.println("totalRepaymentLimit1   "+totalRepaymentLimit);
    //
    //                }
    //                double mainLimit=CommonUtil.convertObjToDouble(txtLimit_SD.getText()).doubleValue();
    //                if(totalRepaymentLimit != mainLimit+totalAdditionalLimit){
    //                    ClientUtil.showMessageWindow("Repay Schedule should match With Limit Amount");
    //                    return false;
    //                }
    //            }
    //        return true;
    //    }
    private String validateOtherDetailsMandatoryFields(){
               StringBuffer stbWarnMsg = new StringBuffer("");
        //        String strSelectedProdType = getCboTypeOfFacilityKeyForSelected();
        //
        //        if ((strSelectedProdType.equals("OD") || strSelectedProdType.equals("CC"))){
        //            GoldLoanMRB objMandatoryRB = new GoldLoanMRB();
        //            if (CommonUtil.convertObjToStr(cboOpModeAI.getSelectedItem()).length() <= 0){
        //                stbWarnMsg.append("\n");
        //                stbWarnMsg.append(objMandatoryRB.getString("cboOpModeAI"));
        //            }
        //            if (chkATMAD.isSelected()){
        //                if (txtATMNoAD.getText().length() <= 0){
        //                    stbWarnMsg.append("\n");
        //                    stbWarnMsg.append(objMandatoryRB.getString("txtATMNoAD"));
        //                }
        //                if (tdtATMFromDateAD.getDateValue().length() <= 0){
        //                    stbWarnMsg.append("\n");
        //                    stbWarnMsg.append(objMandatoryRB.getString("tdtATMFromDateAD"));
        //                }
        //                if (tdtATMToDateAD.getDateValue().length() <= 0){
        //                    stbWarnMsg.append("\n");
        //                    stbWarnMsg.append(objMandatoryRB.getString("tdtATMToDateAD"));
        //                }
        //            }
        //            if (chkDebitAD.isSelected()){
        //                if (txtDebitNoAD.getText().length() <= 0){
        //                    stbWarnMsg.append("\n");
        //                    stbWarnMsg.append(objMandatoryRB.getString("txtDebitNoAD"));
        //                }
        //                if (tdtDebitFromDateAD.getDateValue().length() <= 0){
        //                    stbWarnMsg.append("\n");
        //                    stbWarnMsg.append(objMandatoryRB.getString("tdtDebitFromDateAD"));
        //                }
        //                if (tdtDebitToDateAD.getDateValue().length() <= 0){
        //                    stbWarnMsg.append("\n");
        //                    stbWarnMsg.append(objMandatoryRB.getString("tdtDebitToDateAD"));
        //                }
        //            }
        //            if (chkCreditAD.isSelected()){
        //                if (txtCreditNoAD.getText().length() <= 0){
        //                    stbWarnMsg.append("\n");
        //                    stbWarnMsg.append(objMandatoryRB.getString("txtCreditNoAD"));
        //                }
        //                if (tdtCreditFromDateAD.getDateValue().length() <= 0){
        //                    stbWarnMsg.append("\n");
        //                    stbWarnMsg.append(objMandatoryRB.getString("tdtCreditFromDateAD"));
        //                }
        //                if (tdtCreditToDateAD.getDateValue().length() <= 0){
        //                    stbWarnMsg.append("\n");
        //                    stbWarnMsg.append(objMandatoryRB.getString("tdtCreditToDateAD"));
        //                }
        //            }
        //            if (chkABBChrgAD.isSelected() && (txtABBChrgAD.getText().length() <= 0 || CommonUtil.convertObjToInt(txtABBChrgAD.getText()) == 0)){
        //                stbWarnMsg.append("\n");
        //                stbWarnMsg.append(objMandatoryRB.getString("txtABBChrgAD"));
        //            }
        //            if (chkNonMainMinBalChrgAD.isSelected() && (txtMinActBalanceAD.getText().length() <= 0 || CommonUtil.convertObjToInt(txtMinActBalanceAD.getText()) == 0)){
        //                stbWarnMsg.append("\n");
        //                stbWarnMsg.append(objMandatoryRB.getString("txtMinActBalanceAD"));
        //            }
        
        //        }
        
               return stbWarnMsg.toString();
    }
    
    private String isInterestDetailsExistForThisAcct(){
        StringBuffer stbWarnMsg = new StringBuffer("");
//        String strSelectedProdType = getCboTypeOfFacilityKeyForSelected();
//        //        if (!(strSelectedProdType.equals("OD") || strSelectedProdType.equals("CC")) && tblInterMaintenance.getRowCount() < 1){
//        stbWarnMsg.append("\n");
//        //            stbWarnMsg.append(resourceBundle.getString("interestDetailsWarning"));
        //        }
        return stbWarnMsg.toString();
    }
    
    private void updateRdoSubsidyAndInterestNature(){
        updateRdoSubsidy();
        updateRdoInterestNature();
    }
    private void updateRdoSubsidy(){
//        observable.resetFacilityTabSubsidy();
        //        removeFacilitySubsidy();
        //        rdoSubsidy_Yes.setSelected(observable.getRdoSubsidy_Yes());
        //        rdoSubsidy_No.setSelected(observable.getRdoSubsidy_No());
        //        addFacilitySubsidyRadioBtns();
    }
    
    private void updateRdoInterestNature(){
//        observable.resetFacilityTabInterestNature();
//        //        removeFacilityInterestNature();
        //        rdoNatureInterest_PLR.setSelected(observable.getRdoNatureInterest_PLR());
        //        rdoNatureInterest_NonPLR.setSelected(observable.getRdoNatureInterest_NonPLR());
        //        addFacilityInterestNatureBtns();
    }
    
    private void sanctionMainTabPressed(){
        int selRow=-1;
        if (loanType.equals("LTD"))
            selRow = 0;
        else if(santab)
            selRow=rowmaintab;
        else{
            //            selRow = tblSanctionDetails2.getSelectedRow();
            rowmaintab=selRow;
        }
        santab=false;
        if (selRow >= 0){
            updateOBFields();
//            observable.populateFacilityTabSanction(selRow/*, rowSanctionMain*/);
            rowFacilityTabSanction = selRow;
            if (!(viewType.equals("Delete") || viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT))){
//                observable.resetAllFacilityDetails();
//                updateCboTypeOfFacility();
//                observableSecurity.resetSecurityTableUtil();
//                observableSecurity.resetAllSecurityDetails();
//                //                observableOtherDetails.resetOtherDetailsFields();
//                observableRepay.resetAllRepayment();
//                observableRepay.resetRepaymentCTable();
//                //                observableGuarantor.resetGuarantorCTable();
//                //                observableGuarantor.resetAllGuarantorDetails();
//                observableDocument.resetAllDocumentDetails();
//                observableDocument.resetDocCTable();
//                observableInt.resetInterestCTable();
//                observableInt.resetAllInterestDetails();
//                observableClassi.resetClassificationDetails();
//                observableAdditionalSanctionOB.resetAdditionalSanctionDetails();
            }
            setAllFacilityDetailsEnableDisable(false);
            setFacilityBtnsEnableDisable(false);
            btnsDisableBasedOnAccountNumber();
//            observable.ttNotifyObservers();
        }
    }
    private void tblSanctionDetailsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSanctionDetailsMousePressed
        // Add your handling code here:
        
    }//GEN-LAST:event_tblSanctionDetailsMousePressed
    
   
    
    private void insertScreenLock(){
//        HashMap hash=new HashMap();
//        hash.put("USER_ID",ProxyParameters.USER_ID);
//        hash.put("TRANS_ID",observable.getStrACNumber());
//        hash.put("MODE_OF_OPERATION",viewType);
//        ClientUtil.execute("insertauthorizationLock", hash);
    }
    private void tblSanctionDetails2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSanctionDetails2MousePressed
        // Add your handling code here:
//        if(enableControls){
//            if (observable.getStrACNumber().length()>0)
//                ClientUtil.showMessageWindow(observable.getStrACNumber()+" is in Edit mode. Press Save/Cancel/New.");
//            else
//                ClientUtil.showMessageWindow("This a/c is in New mode. Press Save/Cancel/New.");
//            return;
//        }
//        tblSanctionDetails2Populate();
    }//GEN-LAST:event_tblSanctionDetails2MousePressed
    private void updateCboTypeOfFacility(){
        //        cboTypeOfFacility.setSelectedItem(observable.getCboTypeOfFacility());
        //        observable.getCbmTypeOfFacility().setSelectedItem(observable.getCboTypeOfFacility());
    }
    private void tblSanctionDetailsPopulate(){
//        sanMousePress=false;
//        // Actions have to be taken when a record of Facility Details is selected in Sanction Details Tab
//        //        if ((viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT) || viewType.equals("Edit")))
//        //        if (tblSanctionDetails.getSelectedRow() >= 0 || loanType.equals("LTD") || sandetail){
//        allowMultiRepay = true;
//        observableRepay.setAllowMultiRepay(true);
//        updateOBFields();
//        if(! loanType.equals("LTD") && (viewType.equals(AUTHORIZE)||viewType.equals("Delete")))
//            observable.resetSanctionFacility();
//        // If Facility Details is in Edit Mode
//        //if ((tblSanctionDetails2.getSelectedRow() == tblFacilityDetails.getSelectedRow()) && (tblSanctionDetails.getSelectedRow() == tblFacilityDetails2.getSelectedRow())){
//        setSanctionFacilityBtnsEnableDisable(false);
//        setAllSanctionFacilityEnableDisable(false);
//        //                observable.sanctionFacilityEditWarning();
//        if (!(viewType.equals("Delete") || viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT) ||  (observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW))){
//            if (loanType.equals("LTD")) {
//                rowSanctionFacility = 0;
//                sanDetailMousePressedForLTD = true;
//            }
//            else if(sandetail)
//                rowSanctionFacility=rowsanDetail;
//            else  {
//                //                    rowSanctionFacility = tblSanctionDetails.getSelectedRow();
//                rowsanDetail=rowSanctionFacility;
//                sanMousePress=true;
//            }
//            
//            //                if ((observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) || (viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT))){
//            //                    setSanctionFacilityBtnsEnableDisable(false);
//            //                    setAllSanctionFacilityEnableDisable(false);
//            //                }else{
//            //                    updateSanctionFacility = true;
//            //                    setSanctionFacilityBtnsEnableDisable(true);
//            //                    setAllSanctionFacilityEnableDisable(true);
//            //                }
//            //                observable.populateSanctionFacility(tblSanctionDetails.getSelectedRow());
//            observable.populateSanctionFacility(rowSanctionFacility);
//            updateCboTypeOfFacility();
//            sandetail=false;
//        }
//        observable.ttNotifyObservers();
//        sanctionFacilityTabPressed();
//        if (viewType !=null && viewType.equals("Edit")) {
//            //                btnLTD.setEnabled(false);
//            //                btnNew1.setVisible(true);
//            //                btnSave1.setVisible(true);
//            //                btnNew1.setEnabled(true);
//            //                btnSave1.setEnabled(true);
//        }
//        //            if (observable.getStrACNumber().length()>0) {
//        //                btnFacilitySave.setVisible(false);
//        //            } else {
//        //                btnFacilitySave.setVisible(true);
//        //            }
//        //        }
//        //        if(loanType.equals("LTD")) {
//        //            cboTypeOfFacility.setEnabled(false);
//        //        }
//        //        tdtTDate.setEnabled(false);
//        //        observable.setUpdateAvailableBalance(false);
//        //        txtLimit_SD.setEnabled(false);
//        //        btnLTD.setEnabled(false);
//        //        txtNoInstallments.setEnabled(false);
//        //        cboRepayFreq.setEnabled(false);
//        //        tdtFDate.setEnabled(false);
//        //        chkMoratorium_Given.setEnabled(false);
//        //        additionalSanctionEnableDisable(false);
//        //        additionalSanctionbtnEnableDisable(false);
//        //        setAllInterestDetailsEnableDisable(false);
//        //        setAllInterestBtnsEnableDisable(false);
        //        setAllSettlmentEnableDisable(false);
    }
    
    private void setAllSettlmentEnableDisable(boolean val){
        //        settlementUI.setAllPoAEnableDisable(val);//dontdelete
    }
   
    private void btnDelete1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete1ActionPerformed
        // Add your handling code here:
        btnDelete1Action();
    }//GEN-LAST:event_btnDelete1ActionPerformed
    private void btnDelete1Action(){
        // Facility Details CTable(Sanction Details Tab) Delete pressed
      
        //        else if(rows==0){
        //            btnFacilityDeletePressed();
        //        }
    }
    private void btnSave1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSave1ActionPerformed
        // Add your handling code here:
        btnSave1ActionPerformed();
    }//GEN-LAST:event_btnSave1ActionPerformed
    
    private void btnSave1ActionPerformed(){
//        if(CommonUtil.convertObjToStr(cboAccStatus.getSelectedItem()).equals("Closed"))
//            return;
//        enableControls = true;
//        setSanctionMainBtnsEnableDisable(true);
//        //        additionalSanctionEnableDisable(true);
//        //        additionalSanctionNewEnableDisable(true);
//        //        if (tblSanctionDetails.getRowCount() > 0){
//        //            btnDelete2_SD.setEnabled(false);
//        //        }
//        setAllSanctionMainEnableDisable(true);
//        setSanctionFacilityBtnsEnableDisable(true);
//        setAllSanctionFacilityEnableDisable(true);
//        //        actTransUI.setActTransferEnableDisable(true);//dontdelete
//        updateSanctionFacility = true;
//        updateRecords = true;
//        allowMultiRepay = observableRepay.getAllowMultiRepay();
//        HashMap hash2=new HashMap();
//        boolean lienAuthorized=false;
//        //            hash2.put("PROD_DESC",CommonUtil.convertObjToStr(cboProductId.getModel().getSelectedItem()));
//        List behaveslike=ClientUtil.executeQuery("getLoanBehaves", hash2);
//        if(behaveslike !=null && behaveslike.size()>0){
//            hash2=(HashMap)behaveslike.get(0);
//        }
//        if(hash2.containsKey("BEHAVES_LIKE")&& lblAcctNo_Sanction_Disp.getText() !=null && lblAcctNo_Sanction_Disp.getText().length()>0){
//            String behavesLike= CommonUtil.convertObjToStr(hash2.get("BEHAVES_LIKE"));
//            if(behavesLike.equals("OD") || behavesLike.equals("CC")) {
//                observable.setUpdateAvailableBalance(true);
//                txtLimit_SD.setEnabled(true);
//                txtNoInstallments.setEnabled(true);
//                //                    btnLTD.setEnabled(true);
//                cboRepayFreq.setEnabled(true);
//                //                    tdtFDate.setEnabled(true);
//                //                    chkMoratorium_Given.setEnabled(true);
//                //                    additionalSanctionEnableDisable(false);
//                //                    additionalSanctionbtnEnableDisable(false);
//                //                    rdoDP_YES.setVisible(true);
//                //                    rdoDP_NO.setVisible(true);
//                //                    lblDPLimit.setVisible(true);
//                //                    rdoDP_YES.setEnabled(true);
//                //                    rdoDP_NO.setEnabled(true);
//                //                    lblDPLimit.setEnabled(true);
//                //                    panInterest1
//            }
//            else{
//                //                    rdoDP_YES.setVisible(false);
//                //                    rdoDP_NO.setVisible(false);
//                //                    lblDPLimit.setVisible(false);
//            }
//        }
//        if(hash2.containsKey("BEHAVES_LIKE")&& (!CommonUtil.convertObjToStr(hash2.get("BEHAVES_LIKE")).equals("OD")) && lblAcctNo_Sanction_Disp.getText() !=null && lblAcctNo_Sanction_Disp.getText().length()>0){
//            hash2.put("ACT_NUM", lblAcctNo_Sanction_Disp.getText());
//            hash2.put("ACCT_NUM", lblAcctNo_Sanction_Disp.getText());
//            //disbursement also over we have to check customer repaid or not  modifiy by abi 14-feb-09
//            List lst=ClientUtil.executeQuery("checkTransaction", hash2);
//            hash2=(HashMap)lst.get(0);
//            transCount=0;
//            transCount=CommonUtil.convertObjToInt(hash2.get("CNT"));
//            System.out.print("hash###2"+hash2);
//            hash2.put("ACCT_NUM", lblAcctNo_Sanction_Disp.getText());
//            lst=null;
//            if(loanType.equals("LTD"))
//                lst=ClientUtil.executeQuery("getDepositLienAmount", hash2);
//            if(lst !=null && lst.size()>0){
//                lienAuthorized=true;
//            }
//            Date curr_dts=(Date)curr_dt.clone();
//            //                Date repay_dt=DateUtil.getDateMMDDYYYY(tdtFacility_Repay_Date.getDateValue());
//            //                if(transCount !=0 || (repay_dt !=null && DateUtil.dateDiff(repay_dt,curr_dts )>0) || observable.getShadowCredit()>0 || observable.getShadowDebit()>0 || observable.getClearBalance()<0 || lienAuthorized) {
//            //                observable.setUpdateAvailableBalance(false);
//            txtLimit_SD.setEnabled(false);
//            //                btnLTD.setEnabled(false);
//            txtNoInstallments.setEnabled(false);
//            cboRepayFreq.setEnabled(false);
//            //                tdtFDate.setEnabled(false);
//            //                chkMoratorium_Given.setEnabled(false);
//            observable.setPartReject("PARTILLY_REJECT");
//            //                additionalSanctionEnableDisable(false);
//            //                additionalSanctionbtnEnableDisable(false);
//            //                cboProductId.setEnabled(false);
//            //                cboTypeOfFacility.setEnabled(false);
//            setAllFacilityDetailsEnableDisable(true);
//            setFacilityBtnsEnableDisable(true);
//            //                additionalSanctionbtnEnableDisable(true);
//            //                setAllFacilityDetailsEnableDisable(false);
//            
//            //                if( observable.getShadowCredit()>0 || observable.getShadowDebit()>0){
//            //                    additionalSanctionEnableDisable(false);
//            //                    additionalSanctionbtnEnableDisable(false);
//            //                }
//            //                if(observable.getClearBalance()<0 ){
//            //                    additionalSanctionEnableDisable(true);
//            //                    additionalSanctionbtnEnableDisable(true);
//            //                }else{
//            //                    additionalSanctionEnableDisable(false);
//            //                    additionalSanctionbtnEnableDisable(false);
//            //                }
//            
//            
//        }else {
//            observable.setUpdateAvailableBalance(true);
//            txtLimit_SD.setEnabled(true);
//            //                    txtLimit_SD2.setEnabled(true);
//            txtNoInstallments.setEnabled(true);
//            //                    btnLTD.setEnabled(true);
//            cboRepayFreq.setEnabled(true);
//            //                    tdtFDate.setEnabled(true);
//            //                    chkMoratorium_Given.setEnabled(true);
//            //                    additionalSanctionEnableDisable(false);
//            //                    additionalSanctionbtnEnableDisable(false);
//            observable.setPartReject("");
//            setAllFacilityDetailsEnableDisable(true);
//            setFacilityBtnsEnableDisable(true);
//            //                additionalSanctionbtnEnableDisable(true);
//            
//            String strFacilityType = getCboTypeOfFacilityKeyForSelected();
//            setAllSecurityDetailsEnableDisable(false);
//            if (!strFacilityType.equals(LOANS_AGAINST_DEPOSITS)){
//                setSecurityBtnsOnlyNewEnable();
//            }else{
//                setAllSecurityBtnsEnableDisable(false);
//                setTotalMainAdditionalSanction();
//            }
//            strFacilityType = null;
//            enableDisableGetIntFrom(true);
//            setAllInterestDetailsEnableDisable(false);
//            //                setAllRepaymentDetailsEnableDisable(false);
//            setRepaymentNewOnlyEnable();
//            //                setAllGuarantorDetailsEnableDisable(false);
//            setGuarantorDetailsNewOnlyEnabled();
//            setAllDocumentDetailsEnableDisable(false);
//            setDocumentToolBtnEnableDisable(false);
//            //                }
//            String intGetFrom = CommonUtil.convertObjToStr(observable.getCbmIntGetFrom().getKeyForSelected());
//            if ((intGetFrom.equals(PROD)) || (intGetFrom.equals(""))){
//                setAllInterestBtnsEnableDisable(false);
//            }else{
//                setInterestDetailsOnlyNewEnabled();
//            }
//            //            ClientUtil.enableDisable(panAccountDetails, true);
//            disableLastIntApplDate();
//            //                if (rdoSecurityDetails_Fully.isSelected()){
//            //                    rdoSecurityDetails_FullyActionPerformed(null);
//            //                }else if (rdoSecurityDetails_Partly.isSelected()){
//            //                    rdoSecurityDetails_PartlyActionPerformed(null);
//            //                }else if (rdoSecurityDetails_Unsec.isSelected()){
//            //                    rdoSecurityDetails_UnsecActionPerformed(null);
//            //                }
//            
//            //                if (chkGurantor.isSelected()){
//            //                    chkGurantorActionPerformed(null);
//            //                }else{
//            //                    setAllGuarantorBtnsEnableDisable(false);
//            //                }
//            //
//            //                if (chkInsurance.isSelected()){
//            //                    chkInsuranceActionPerformed(null);
//            //                }else{
//            //                    //                    setAllInsuranceBtnsEnableDisable(false);
//            //                    //                    setAllInsuranceDetailsEnableDisable(false);
//            //                }
//            setSanctionProductDetailsDisable();
//            
//            observableInt.setValByProdID();
//        }
//        //renewal od
//        if(hash2.containsKey("BEHAVES_LIKE")&& (!CommonUtil.convertObjToStr(hash2.get("BEHAVES_LIKE")).equals("OD") ||
//        CommonUtil.convertObjToStr(hash2.get("BEHAVES_LIKE")).equals("CC")) && lblAcctNo_Sanction_Disp.getText() !=null && lblAcctNo_Sanction_Disp.getText().length()>0){
//            if(DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(observable.getTdtTDate()),DateUtil.getDateWithoutMinitues((Date)curr_dt.clone()))>=0)
//                if(accNumMap.containsKey(lblAcctNo_Sanction_Disp.getText()) && sanMousePress){
//                    //                        if(tblRepaymentCTable.getRowCount()>0){
//                    sanMousePress=true;
//                    //                            tblRepaymentCTableMousePressed();
//                    //                            btnRepayment_DeleteActionPerformed();
//                    //                            ClientUtil.displayAlert("Change the sanction Details so Create new Repayment schdule");
//                    //                            tabLimitAmount.setSelectedIndex(8);
//                    
//                    //                        }
//                }
//        }
//        //        btnSave1.setEnabled(false);
//        //        btnSave1.setVisible(false);
//        if(observable.getCboAccStatus().equals("Closed") && viewType.equals(CommonConstants.STATUS_AUTHORIZED))
//            setAllClassificationDetailsEnableDisable(false);
//        else
//            setAllClassificationDetailsEnableDisable(true);
//        
//        //        if (tblInterMaintenance.getSelectedRow()>=0) {
//        //            tblinterestDetailsMousePressed();
//        //        }
//        //        if (tblRepaymentCTable.getSelectedRow()>=0) {
//        //            tblRepaymentCTableMousePressed();
//        //        }
//        //        if (loanType.equals("OTHERS") && !viewType.equals(CommonConstants.STATUS_AUTHORIZED)) {
//        //            observableSecurity.resetAllSecurityDetails();
//        ////            observableSecurity.resetSecurityTableUtil();
//        //            observableOtherDetails.resetOtherDetailsFields();
//        //            observableRepay.resetAllRepayment();
//        ////            observableRepay.resetRepaymentCTable();
//        //            observableGuarantor.resetGuarantorDetails();
//        ////            observableGuarantor.resetGuarantorCTable();
//        //            observableDocument.resetAllDocumentDetails();
//        ////            observableDocument.resetDocCTable();
//        //            observableInt.resetAllInterestDetails();
//        //            observableClassi.resetClassificationDetails();
//        //            observable.ttNotifyObservers();
//        //        }
//        
//        //        String mandatoryMessage="";
//        //        mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panTableFields_SD);
//        //        /* mandatoryMessage1 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
//        //        if (chkMoratorium_Given.isSelected() && (txtFacility_Moratorium_Period.getText().length() == 0)){
//        //            TermLoanRB termLoanRB = new TermLoanRB();
//        //            mandatoryMessage = mandatoryMessage + termLoanRB.getString("moratorium_Given_Warning");
//        //            termLoanRB = null;
//        //            }
//        //        //ltd loan number not generator nut checking lien
//        //        if(loanType.equals("LTD") && observable.getStrACNumber().length()==0) {
//        //            String mainLimit=CommonUtil.convertObjToStr(txtLimit_SD.getText());
//        //            if(mainLimitMarginValidation(mainLimit))
//        //                return;
//        //            if(checkInterestRateForLTD()){
//        //                return;
//        //        }
//        //        }
//        //        //for renew   od
//        //        txtLimit_SDFocusLostOD(false);
//        //        //
//        //        //check sanctionDetails change or not if change delete repayment schedule
//        //        if(sanctionDetailsBasedRepayment())
//        //            return;
//        //
//        //        if (mandatoryMessage.length() > 0){
//        //            displayAlert(mandatoryMessage);
//        //        }else{
//        //            //check repay detail delete or not
//        //            if(!((observable.getTxtLimit_SD()).equals(txtLimit_SD.getText())) ||
//        //            !((observable.getTxtNoInstallments()).equals(txtNoInstallments.getText())) ||
//        //            !(((String)cboRepayFreq.getSelectedItem()).equals(observable.getCboRepayFreq() )) ||
//        //            !((observable.getTdtFDate()).equals(tdtFDate.getDateValue()))){
//        //
//        //
//        //                accNumMap.put(lblAcctNo_Sanction_Disp.getText(),lblAcctNo_Sanction_Disp.getText());
//        //            }
//        //            //end checking
//        //            boolean periodFlag = false;
//        //            boolean limitFlag = false;
//        //            String message = new String();
//        //            if (loanType.equals("OTHERS")) {
//        //                if (!(cboRepayFreq.getSelectedItem().equals("User Defined") || cboRepayFreq.getSelectedItem().equals("Lump Sum")) && !observable.checkFacilityPeriod(txtNoInstallments.getText(), txtFacility_Moratorium_Period.getText())){
//        //                    observable.decoratePeriod();
//        //                    message = message.concat("The Limit Period must fall within "+observable.getMinDecLoanPeriod()+" and  "+observable.getMaxDecLoanPeriod());
//        //                    periodFlag = false;
//        //                }else if ((cboRepayFreq.getSelectedItem().equals("User Defined") || cboRepayFreq.getSelectedItem().equals("Lump Sum")) && !observable.checkFacilityPeriod(DateUtil.getDateMMDDYYYY(tdtFDate.getDateValue()), DateUtil.getDateMMDDYYYY(tdtTDate.getDateValue()))){
//        //                    observable.decoratePeriod();
//        //                    message = message.concat("The Limit Period must fall within "+observable.getMinDecLoanPeriod()+" and  "+observable.getMaxDecLoanPeriod());
//        //                    periodFlag = false;
//        //                }else{
//        //                    periodFlag = true;
//        //                }
//        //            }else{
//        //                periodFlag = true;
//        //            }
//        //            if (loanType.equals("OTHERS")) {
//        //                if (!observable.checkLimitValue(txtLimit_SD.getText())) {
//        //                    observable.setTxtLimit_SD("");
//        //                    txtLimit_SD.setText(observable.getTxtLimit_SD());
//        //                    message = message.concat("\nThe Limit value must fall within "+observable.getMinLimitValue().toString()+" and  "+observable.getMaxLimitValue().toString());
//        //                    limitFlag = false;
//        //                }else{
//        //                    limitFlag = true;
//        //                }
//        //            }else{
//        //                limitFlag = true;
//        //            }
//        //            if (!(periodFlag && limitFlag)){
//        //                btnSave1Action();
//        //            }else{
//        //                displayAlert(message);
//        //            }
//        //            message = null;
        //        }
    }
    private void checkProductLevelInterestDetailsforLTD(){
//        HashMap whereMap = new HashMap();
//        whereMap.put("CATEGORY_ID", observableBorrow.getCbmCategory().getKeyForSelected());
//        whereMap.put("AMOUNT", getBigDecimal(CommonUtil.convertObjToDouble(observable.getTxtLimit_SD()).doubleValue()));
//        whereMap.put("PROD_ID", observable.getLblProductID_FD_Disp());
        //        whereMap.put("FROM_DATE", getTimestamp(DateUtil.getDateMMDDYYYY(tdtFDate.getDateValue())));
        //        whereMap.put("TO_DATE", getTimestamp(DateUtil.getDateMMDDYYYY(tdtTDate.getDateValue())));
        
    }
     private boolean mainLimitMarginValidation(String mainLimit){
        HashMap transactionMap=new HashMap();
//        if(observable.getDepositNo().length()>0 && observable.getStrACNumber().length()==0)
//            lblDepositNo.setText(observable.getDepositNo());
//        String depositNo=CommonUtil.convertObjToStr(lblDepositNo.getText());
//        
//        if(depositNo.length()>0){
//            transactionMap.put("prodId", observable.getCbmProductId().getKeyForSelected());
//            transactionMap.put("PROD_ID", observable.getCbmProductId().getKeyForSelected());
//            transactionMap.put("ACCOUNT_NO",depositNo);
//            transactionMap.put("NEW_SANCTION_AMT",mainLimit);
//            if(observableAdditionalSanctionOB.marginValidation(transactionMap)){
//                txtLimit_SD.setText("");
//                morotoriumEnableDisable(false);
//                return true;
//            }
//        }
      return false;
    }
    private boolean sanctionDetailsBasedRepayment(){
//        String acct_num=CommonUtil.convertObjToStr(lblAcctNo_Sanction_Disp.getText());
//        if(! acct_num.equals("")){
//            StringBuffer msg=new StringBuffer();
//            HashMap map=new HashMap();
//            map.put(CommonConstants.MAP_WHERE,acct_num);
//            List lst=null;
//            if(alreadyChecked)
//                lst=ClientUtil.executeQuery("getSelectTermLoanSanctionFacilityTO.AUTHORIZE", map);
//            if(lst !=null && lst.size()>0){
//                boolean deleteRecord=false;
//                TermLoanSanctionFacilityTO termLoanSanctionFacilityTO=(TermLoanSanctionFacilityTO)lst.get(0);
//                if(termLoanSanctionFacilityTO.getLimit().doubleValue()!=CommonUtil.convertObjToDouble(txtLimit_SD.getText()).doubleValue()){
//                    deleteRecord=true;
//                    msg.append("Actual Limit     :"+termLoanSanctionFacilityTO.getLimit().doubleValue()+"\n"+
//                    "Change Limit "+txtLimit_SD.getText()+"\n");
//                }
//                //                if(DateUtil.dateDiff(termLoanSanctionFacilityTO.getFromDt(),DateUtil.getDateMMDDYYYY(tdtFDate.getDateValue()))!=0){
//                //                    deleteRecord=true;
//                //                    msg.append("From Date was   :"+termLoanSanctionFacilityTO.getFromDt()+"\n"+
//                //                    "Now it is Changed to   "+txtLimit_SD.getText()+"\n");
//                //
//                //                }
//                if(termLoanSanctionFacilityTO.getNoInstall().doubleValue()!=CommonUtil.convertObjToDouble(txtNoInstallments.getText()).doubleValue()){
//                    deleteRecord=true;
//                    msg.append(" Installment  was   :"+termLoanSanctionFacilityTO.getNoInstall().doubleValue()+"\n"+
//                    "Now it is Changed to   "+txtNoInstallments.getText()+"\n");
//                    
//                }
//                if(termLoanSanctionFacilityTO.getRepaymentFrequency().doubleValue() !=CommonUtil.convertObjToDouble(((ComboBoxModel)cboRepayFreq.getModel()).getKeyForSelected()).doubleValue()){
//                    deleteRecord=true;
//                    msg.append("Repayment Frequency  was   :"+setSanctionDetailsFrequency(termLoanSanctionFacilityTO.getRepaymentFrequency())+"\n"+
//                    "Now it is Changed to   "+CommonUtil.convertObjToStr(cboRepayFreq.getSelectedItem())+"\n");
//                    
//                }
//                //                if(!CommonUtil.convertObjToStr(txtFacility_Moratorium_Period.getText()).equals("")){
//                //                    if(termLoanSanctionFacilityTO.getNoMoratorium().doubleValue()!=CommonUtil.convertObjToDouble(txtFacility_Moratorium_Period.getText()).doubleValue()){
//                //                        deleteRecord=true;
//                //                        msg.append("Morotorium  was   :"+termLoanSanctionFacilityTO.getNoMoratorium().doubleValue()+"\n"+
//                //                        "Now it is Changed to   "+txtFacility_Moratorium_Period.getText()+"\n");
//                //
//                //                    }
//                //                }
//                //                if( deleteRecord && loanType.equals("OTHERS")){
//                //                    msg.append("Create a new Repayment Schedule and then save");
//                //                    ClientUtil.showMessageWindow(""+msg);
//                //                    dumRowRepay=0;
//                //                    sanValueChanged=true;
//                //                    if(tblRepaymentCTable.getRowCount()>0){
//                //                        tblRepaymentCTableMousePressed();
//                //                        btnRepayment_DeleteActionPerformed();
//                //                        sanValueChanged=false;
//                //                        alreadyChecked=false;
//                //                        return true;
//                //                    }
//                //                }
//            }
//        }
        return false;
    }
    private String setSanctionDetailsFrequency(Double obj){
        if(365==obj.doubleValue())
            return "Yearly";
        if(180==obj.doubleValue())
            return "HalfYearly";
        if(90==obj.doubleValue())
            return "Quaterly";
        if(30==obj.doubleValue())
            return "Monthly";
        
        return "";
    }
    private void btnSave1Action(){
        updateOBFields();
//        if ((observable.addSanctionFacilityTab(rowSanctionFacility, updateSanctionFacility, updateSanctionMain, rowSanctionMain, rowFacilityTabSanction)) == 1){
//            setAllSanctionFacilityEnableDisable(true);
//        }else{
//            // It will update the database tables based on the Account Number
//            if (observable.getStrACNumber().length() > 0){
//                if (!btnFacilitySavePressed()){
//                    resetEnableDisableFieldsBasedOnSave1Action();
//                }
//            }else{
//                resetEnableDisableFieldsBasedOnSave1Action();
//            }
//        }
//        observable.ttNotifyObservers();
        //        if (loanType.equals("LTD")) btnSave2_SDActionPerformed(null); for testig by abi
    }
    private void resetEnableDisableFieldsBasedOnSave1Action(){
//        observable.resetSanctionFacility();
//        setSanctionFacilityBtnsEnableDisable(false);
//        setAllSanctionFacilityEnableDisable(false);
//        //        btnNew1.setEnabled(true);
//        //        btnSave2_SD.setEnabled(true);
//        rowSanctionFacility = -1;
//        updateSanctionFacility = false;
//        updateRecords = false;
//        observableSecurity.resetAllSecurityDetails();
//        observable.resetAllFacilityDetails();
//        updateCboTypeOfFacility();
//        resetTabsDependsOnAccountNumber();
//        setAllFacilityDetailsEnableDisable(false);
//        setFacilityBtnsEnableDisable(false);
//        btnsDisableBasedOnAccountNumber();
    }
    private void btnNew1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNew1ActionPerformed
        // Add your handling code here:
        btnNew1Action();
    }//GEN-LAST:event_btnNew1ActionPerformed
    private void btnNew1Action(){
        // Facility Details CTable(Sanction Details Tab) New pressed
//        enableControls = true;
//        if (loanType.equals("LTD")) {
//            
//            //            if (tblSanctionDetails.getRowCount()>=1) {
//            //                ClientUtil.showMessageWindow("More than one loan not allowed for a single borrower...");
//            //                return;
//            //            }
//            
//        }
//        //        if(loanType.equals("LTD"))
//        //            btnLTD.setEnabled(true);
//        observable.resetSanctionFacility();
//        observable.resetAllFacilityDetails();
//        updateCboTypeOfFacility();
//        resetTabsDependsOnAccountNumber();
//        setAllFacilityDetailsEnableDisable(true);
//        //        setFacilityBtnsEnableDisable(false);
//        btnsDisableBasedOnAccountNumber();
//        observable.setTdtFDate(DateUtil.getStringDate((Date)curr_dt.clone()));
//        observable.setAccountOpenDate(DateUtil.getStringDate((Date)curr_dt.clone()));
//        setDefaultValB4AcctCreation();
//        observable.ttNotifyObservers();
//        setSanctionFacilityBtnsEnableDisable(true);
//        setAllSanctionFacilityEnableDisable(true);
//        setAllClassificationDetailsEnableDisable(true);
//        //        btnDelete1.setEnabled(false);
//        rowSanctionFacility = -1;
//        updateSanctionFacility = false;
//        updateRecords = true;
//        //        if (loanType.equals("LTD")) {
//        //            ((ComboBoxModel) cboTypeOfFacility.getModel()).setKeyForSelected(LOANS_AGAINST_DEPOSITS);
//        //            cboTypeOfFacility.setEnabled(false);
//        //        }
//        sandetail=true;
//        enableDisableGetIntFrom(true);
//        setRepaymentBtnsEnableDisable(true);
//        //        ClientUtil.enableDisable(panClassDetails, true);
//        //        btnSave1.setVisible(false);
//        //        btnSave1.setEnabled(false);
//        observableClassi.setClassifiDetails(CommonConstants.TOSTATUS_INSERT);
        //        observableOtherDetails.setOtherDetailsMode(CommonConstants.TOSTATUS_INSERT);
    }
    private void btnDelete2_SDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete2_SDActionPerformed
        // Add your handling code here:
        btnDelete2_SDAction();
    }//GEN-LAST:event_btnDelete2_SDActionPerformed
    private void btnDelete2_SDAction(){
        // Sanction Details CTable(Sanction Details Tab) Delete pressed
//        observable.deleteSanctionMain(rowSanctionMain);
//        observable.resetSanctionMain();
//        observable.resetSanctionFacilityTable();
//        observable.resetSanctionFacility();
//        observable.resetAllFacilityDetails();
//        updateCboTypeOfFacility();
//        resetTabsDependsOnAccountNumber();
//        setSanctionFacilityBtnsEnableDisable(false);
//        setAllSanctionFacilityEnableDisable(false);
//        setSanctionMainBtnsEnableDisable(false);
//        setAllSanctionMainEnableDisable(false);
//        setAllFacilityDetailsEnableDisable(false);
//        setFacilityBtnsEnableDisable(false);
//        btnsDisableBasedOnAccountNumber();
//        //        btnNew2_SD.setEnabled(true);
//        rowSanctionMain = -1;
//        observable.ttNotifyObservers();
    }
    private void btnSave2_SDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSave2_SDActionPerformed
        // Add your handling code here:
        final String mandatoryMessage1 = new MandatoryCheck().checkMandatory(getClass().getName(), panSanctionDetails_Sanction);
        /* mandatoryMessage1 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        
        //        final String mandatoryMessage2 = new MandatoryCheck().checkMandatory(getClass().getName(), panSanctionDetails_Mode);
        /* mandatoryMessage2 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        //        if ((mandatoryMessage1.length() > 0 || mandatoryMessage2.length() > 0) || (facilityFlag && updateSanctionFacility )) {
        //            if(updateSanctionFacility  && facilityFlag)
        //                displayAlert(resourceBundle.getString("santionFacilityEditWarning"));
        //
        //            else
        //                displayAlert(mandatoryMessage1+mandatoryMessage2);
        //
        //        }else{
        //            rowSanctionFacility = -1;
        //            updateSanctionFacility = false;
        //            if (observable.checkSanctionNoDublication(txtSanctionNo.getText())){
        //                btnSave2_SDAction();
        //            }
        //        }
        //        if (loanType.equals("LTD")) {
        //            tblSanctionDetails2MousePressed(null);
        //        }
    }//GEN-LAST:event_btnSave2_SDActionPerformed
    private void btnSave2_SDAction(){
        // Sanction Details CTable(Sanction Details Tab) Save pressed
        //check sanction no if change means we shoude update facilitydetails and sanction facility_details
        //        String newSanctionNo=CommonUtil.convertObjToStr(txtSanctionNo.getText());
        //        String sanctionSlNo=CommonUtil.convertObjToStr(txtSanctionSlNo.getText());
        //        if(!(sanctionSlNo.equals("") &&  newSanctionNo.equals("")))
        //            if(observable.getOldSanction_no() !=newSanctionNo)
        //                if(observable.checkAllfacilitySanctionnoUpdateDetails( newSanctionNo,sanctionSlNo))
//        //                    updateSanctionMain=true;
//        updateOBFields();
//        //        if (tblSanctionDetails.getRowCount() > 0){
//        if (updateSanctionMain == false){
//            observable.setSanctionNumber();
//        }
//        observable.addSanctionMainTab(rowSanctionMain,  updateSanctionMain);
//        setSanctionFacilityBtnsEnableDisable(false);
//        setAllSanctionFacilityEnableDisable(false);
//        setSanctionMainBtnsEnableDisable(false);
//        setAllSanctionMainEnableDisable(false);
//        observable.resetSanctionFacility();
//        observable.resetSanctionMain();
//        observable.resetSanctionFacilityTable();
//        observable.resetAllFacilityDetails();
//        updateCboTypeOfFacility();
//        resetTabsDependsOnAccountNumber();
//        setAllFacilityDetailsEnableDisable(false);
//        setFacilityBtnsEnableDisable(false);
//        btnsDisableBasedOnAccountNumber();
        //            btnNew2_SD.setEnabled(true);
        //        }else{
        //            observable.setSanctionTableWarningMessage();
        //        }
//        observable.ttNotifyObservers();
    }
    private void btnNew2_SDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNew2_SDActionPerformed
        // Add your handling code here:
        //        if(loanType.equals("LTD") && tblSanctionDetails2.getRowCount()>0){
        //            ClientUtil.displayAlert("More than one loan not allowed for a single borrower...");
        //        return;
        //    }
//        String cust_id=txtCustID.getText();
//        if(cust_id!=null && cust_id.length()>0)
//            btnNew2_SDAction();
//        else {
//            ClientUtil.displayAlert("Enter Borrower Details");
//            return;
//        }
    }//GEN-LAST:event_btnNew2_SDActionPerformed
    
    private void btnNew2_SDAction(){
        // Sanction Details CTable(Sanction Details Tab) New pressed
//        observable.createSanctionMainRowObjects();
//        observable.setStrRealSanctionNo("");
//        observable.createTableUtilSanctionFacility();
//        setSanctionMainBtnsEnableDisable(true);  //false changed as true by Rajesh
//        setAllSanctionMainEnableDisable(true);
//        setAllFacilityDetailsEnableDisable(false);
//        //        btnNew1.setEnabled(true);
//        //        btnNew2_SD.setEnabled(true);
//        //        btnDelete2_SD.setEnabled(false);         // This line added by Rajesh
//        rowSanctionMain = -1;
//        rowSanctionFacility = -1;
//        updateSanctionFacility = false;
//        updateSanctionMain = false;
//        //        observableBorrow.setCboCategory(CommonUtil.convertObjToStr(cboCategory.getSelectedItem()));
//        updateOBFields();
//        observable.resetSanctionMain();
//        observable.setTdtSanctionDate(DateUtil.getStringDate((Date)curr_dt.clone()));
//        observable.destroyCreateSanctionFacilityObjects();
//        btnNew1Action();
//        //        btnNew1.setVisible(false);
//        //        btnSave1.setVisible(false);
//        //        txtSanctionNo.requestFocus();
//        //        txtSanctionSlNo.setText("1");
//        observable.setTxtSanctionSlNo("1");
    }
    
    private void btnDepositNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDepositNoActionPerformed
        // Add your handling code here:
        boolean chk = false;
        System.out.println("observable.getProductAuthRemarks()"+observable.getProductAuthRemarks());
        if (observable.getProductAuthRemarks().equals("MDS_LOAN")) {
            HashMap paramMap = new HashMap();
            paramMap.put("PROD_ID", CommonUtil.convertObjToStr(observable.getCbmLoanProduct().getKeyForSelected()));
            List paramList = ClientUtil.executeQuery("getAuctionRequire", paramMap);
            if (paramList.size() > 0) {
                HashMap auctMap = (HashMap) paramList.get(0);
                if (auctMap!=null && auctMap.containsKey("AUCTION_AMT_ALLOWED") && auctMap.get("AUCTION_AMT_ALLOWED")!=null && 
                        auctMap.get("AUCTION_AMT_ALLOWED").equals("Y")) {
                    callView("MDS_CUSTOMER_AUCTION");
                    chk = true;
                } else {
                    callView("MDS_CUSTOMER");
                }
            }
        } else if (observable.getProductAuthRemarks().equals("PADDY_LOAN")) {
            callView("PADDY_CUSTOMER");
        } else if (observable.getProductAuthRemarks().equals("OTHER_LOAN")) {
            callView("CUSTOMER ID");
        }
        String goldLoan = "";
        if (chk == true) {
            getIntrate();
        }
        
    }//GEN-LAST:event_btnDepositNoActionPerformed
   public void getIntrate(){
       double loanamt=CommonUtil.convertObjToDouble(txtLoanAmt.getText());
       double rate=CommonUtil.convertObjToDouble(txtInter.getText());
       long days=DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(tdtAccountOpenDate.getDateValue()),DateUtil.getDateMMDDYYYY(tdtRepaymentDt.getDateValue()));
       double intTotal=loanamt*rate*days/36500;
       DecimalFormat df=new DecimalFormat("##.00");
       lblIntrestAmt.setText(String.valueOf(df.format(intTotal)));
       
   }
    public void insertCustTableRecords(HashMap hash){
//        viewType = "CUSTOMER ID";
//        txtAccountNo.setText(CommonUtil.convertObjToStr(hash.get("MEMBER NO")));
        fillData(hash);
    }
    private void validateConstitutionCustID(){
//        String strConstitution = CommonUtil.convertObjToStr(cboConstitution.getSelectedItem());
//        // If the constitution is blank then allow to select the customer
//        if (strConstitution.length() != 0 && tblBorrowerTabCTable.getRowCount() > 0){
//            observableBorrow.validateConstitutionCustID(strConstitution, CommonUtil.convertObjToStr(tblBorrowerTabCTable.getValueAt(0, 2)));
//            cboConstitution.setSelectedItem(observableBorrow.getCboConstitution());
//            //            observableBorrow.getCbmConstitution().setKeyForSelected(observableBorrow.getCbmConstitution().getDataForKey(strConstitution));
//        }
    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
        nomineeUi.resetNomineeData();
        nomineeUi.resetTable();
       
        //        eachProdId = "";
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void removeObservable(){
//        observable.deleteObserver(this);
//        observableBorrow.deleteObserver(this);
//        //        observableComp.deleteObserver(this);
//        observableSecurity.deleteObserver(this);
//        observableRepay.deleteObserver(this);
//        //        observableGuarantor.deleteObserver(this);
//        observableInt.deleteObserver(this);
//        observableClassi.deleteObserver(this);
    }
    
    private void destroyOBObjects(){
//        observableBorrow = null;
//        observableClassi = null;
//        //        observableComp = null;
//        //        observableGuarantor = null;
//        observableInt = null;
//        observable = null;
//        observableRepay = null;
//        observableSecurity = null;
    }
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // Add your handling code here:
        HashMap reportParamMap = new HashMap();
        com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        setModified(false);
        chrgTableEnableDisable();//charge details
        btncancelActionPerformed();
        eligblLoanAmt = 0.0;
    }//GEN-LAST:event_btnCancelActionPerformed
    private void btncancelActionPerformed(){
       setButtonEnableDisable();
       observable.resetAllFields();
       observable .destroyObjects();
       observable .createObject();
       ClientUtil.enableDisable(this,false);
       observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
       observable.setStatus();
        setModified(false);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        btnCustomer(false);
        btnNew_Borrower.setVisible(false);
        observable.setStrACNumber("");
        observable.ttNotifyObservers();
        if (fromNewAuthorizeUI) {
            this.dispose();
            fromNewAuthorizeUI = false;
            newauthorizeListUI.setFocusToTable();
        }
        if (fromAuthorizeUI) {
            this.dispose();
            fromAuthorizeUI = false;
            authorizeListUI.setFocusToTable();
        }
        if (fromManagerAuthorizeUI) {
            this.dispose();
            fromManagerAuthorizeUI = false;
            ManagerauthorizeListUI.setFocusToTable();
        }
       //Added By Suresh
       ClientUtil.clearAll(this);
       clearMDSDetails();
       clearPaddyDetails();
        observable.setPaddyMap(null);
        observable.setMdsMap(null);
        panCustIdDetails.setVisible(false);
        txtNextAccNo.setText("");
        tblDepositDetails.setModel(observable.getTblSanctionMainMds());
        oldloanAmt = 0;
        loanRenewal = false;
        suspenseActNum = null;
        txtNoOfInstall.setText("");
//       chkDepositUnlien.setSelected(false);
    }
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void mitPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitPrintActionPerformed
        // Add your handling code here:
        btnPrintActionPerformed(evt);
    }//GEN-LAST:event_mitPrintActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        transNew = false;
        btndeleteActionPerformed();
        btnCustomer(true);
        observable.setStatus();
        enableDisbleRepayDtInt(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    private void btndeleteActionPerformed(){
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        observable.setStatus();
         btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
         popUp("Delete");
//        observableSecurity.setActionType(ClientConstants.ACTIONTYPE_DELETE);
//        observableBorrow.setLoanActionType(ClientConstants.ACTIONTYPE_DELETE);
//        setAllTableBtnsEnableDisable(false); // To disable the Tool buttons for all the CTable
//        nomineeUi.setActionType(DELETE);
//        popUp("Delete");
//        //        authSignUI.setAuthTabBtnEnableDisable(false);
//        setAllTablesEnableDisable(true); // To disable the All tables...
//        rowSanctionFacility = -1;
//        updateSanctionFacility = false;
//        updateRecords = false;
        //        btnAppraiserId.setEnabled(false);
    }
    private void setAllTablesEnableDisable(boolean val){
        //        tblSanctionDetails.setEnabled(val);
    }
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        transNew = false;
//        chkDepositUnlien.setEnabled(true);
        btneditActionPerformed();
//        //        btnAppraiserId.setEnabled(false);
//        txtGrossWeight.setEnabled(true);
//        txtNetWeight.setEnabled(true);
//        cboPurityOfGold.setEnabled(true);
//        btnSave.setEnabled(true);
//        ClientUtil.enableDisable(panBorrowCompanyDetails,false);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        enableDisbleRepayDtInt(false);
        btnCustomer(false);
        enableDisableTable(false);
        txtNextAccNo.setText("");
    }//GEN-LAST:event_btnEditActionPerformed
    private void enableDisableTable(boolean flag){
        btnNew_Borrower.setVisible(false);
        btnSave_Borrower.setVisible(flag);
       
    }
    private void btneditActionPerformed(){
//        observableBorrow.btnPressed = true;
        observable.createObject();
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        observable.setStatus();
//        observableSecurity.setActionType(ClientConstants.ACTIONTYPE_EDIT);
//        observableBorrow.setLoanActionType(ClientConstants.ACTIONTYPE_EDIT);
        popUp("Edit");
        //        authSignUI.setAuthEnableDisable(false);
        //        authSignUI.setAuthOnlyNewBtnEnable();
        //        authSignUI.setAllAuthInstEnableDisable(false);
        //        authSignUI.setAuthInstOnlyNewBtnEnable();
        //        poaUI.setAllPoAEnableDisable(false);
//        setSanctionFacilityBtnsEnableDisable(false);
        //        setAllSanctionFacilityEnableDisable(false);
        //        setSanctionMainBtnsEnableDisable(false);
        //        setAllSanctionMainEnableDisable(false);
        //        setAllFacilityDetailsEnableDisable(false);
        //        setFacilityBtnsEnableDisable(false);
        //        btnsDisableBasedOnAccountNumber();
        //        setAllTablesEnableDisable(true);
        //        btnNew2_SD.setEnabled(true);
        //        updateModeAuthorize = false;
        //        updateModePoA = false;
        //        updateSanctionFacility = false;
        //        updateRecords = false;
        //        updateSecurity = false;
        //        updateRepayment = false;
        //        updateGuarantor = false;
        //        updateInterest = false;
        //        updateDocument = false;
        //        rowSanctionFacility = -1;
    }
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
             if(transNew){
                 if (!observable.getProductAuthRemarks().equals("OTHER_LOAN")) {
            //Added By Suresh
            if(tdtRepaymentDt.getDateValue().length()<=0){
                ClientUtil.showAlertWindow("Repayment Date Should Not be Empty !!!");
                return;
            }
            if(CommonUtil.convertObjToDouble(txtInter.getText()).doubleValue() == 0) {
                ClientUtil.showAlertWindow("Rate of Interest not set for this Product !!! ");
                return;
            }
            if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW && observable.getProductAuthRemarks().equals("PADDY_LOAN") && txtCustId.getText().length()<=0){
                ClientUtil.showAlertWindow("Pls Select Customer Id !!! ");
                return;
                    }
            }
            if(tblDepositDetails.getRowCount()<=0)  {
                 ClientUtil.showAlertWindow("Pls Save security details first!!! ");
                return;
            }
                 if(tblDepositDetails !=null && tblDepositDetails.getRowCount()==0 && observable.getProductAuthRemarks().equals("OTHER_LOAN")){
                     String depositNo=CommonUtil.convertObjToStr(txtDepositNo.getText());
                    
                     if(depositNo.length()==0){
                         ClientUtil.showMessageWindow("Please Enter Deposit No  ");
                         return;
                     }
                     
                 }
                 if (observable.getProductAuthRemarks().equals("OTHER_LOAN")) {
                     double depositAmt = CommonUtil.convertObjToDouble(txtLoanAmt.getText()).doubleValue();
                     if (depositAmt == 0) {
                         ClientUtil.showMessageWindow("Loan Sanction Amount Should not be Zero");
                         return;
                     }
                 }
                 if(observable.getDepoBehavesLike().equalsIgnoreCase("THRIFT"))
                 {
                     if (tdtToDate.getDateValue().length() <= 0) {
                         ClientUtil.showAlertWindow("ToDate  Date Should Not be Empty !!!");
                         return;
                     }
                 }
                 if (chkMobileBankingTLAD.isSelected() == true && txtMobileNo.getText().length() == 0) {
                    ClientUtil.displayAlert("Mobile no should not be empty!!!");
                }
                insertTransactionPart();
                }
            if (chktrans != true) {
            btnsaveActionPerformed();
            chkok = false;
        } else {
             
             int yesNo2 = 0;
                String[] options = {"Yes", "No"};
                yesNo2 = COptionPane.showOptionDialog(null, "Do you want to create loan account without transaction?", CommonConstants.WARNINGTITLE,
                        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                        null, options, options[0]);
                if (yesNo2 == 0) {
                    com.see.truetransact.clientutil.ttrintegration.TTIntegration ttIntgration = null;
                        btnsaveActionPerformed();
                        chkok=false; 
                }
                else{
                ClientUtil.showAlertWindow("Transaction processing aborted");
                chkok=true;
                return;
                }
             }
             if(observable.getResult()==ClientConstants.ACTIONTYPE_FAILED){
            observable.setResultStatus();
            return;
        }else{
            if (observable.getProxyReturnMap()!=null && observable.getProxyReturnMap().containsKey("ACCTNO")) {
                String actNum = (String)observable.getProxyReturnMap().get("ACCTNO");
                HashMap transTypeMap = new HashMap();
                HashMap transMap = new HashMap();
                HashMap transCashMap = new HashMap();
                transCashMap.put("BATCH_ID",actNum);
                transCashMap.put("TRANS_DT", curr_dt);
                transCashMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                HashMap transIdMap = new HashMap();
                int transferCount=0,cashCreditCount=0,cashDebitCount=0;
                List list = ClientUtil.executeQuery("getTransferDetails", transCashMap);
                if(list !=null && list.size()>0){
                    for(int i = 0;i<list.size();i++){
                        transMap = (HashMap)list.get(i);
                        transIdMap.put(transMap.get("SINGLE_TRANS_ID"),"TRANSFER");
                        transferCount++;
                    }
                }
                list = ClientUtil.executeQuery("getCashDetails", transCashMap);
                if(list !=null && list.size()>0){
                    for(int i = 0;i<list.size();i++){
                        transMap = (HashMap)list.get(i);
                        transIdMap.put(transMap.get("SINGLE_TRANS_ID"),"CASH");
                        transTypeMap.put(transMap.get("SINGLE_TRANS_ID"),transMap.get("TRANS_TYPE"));
                        if(transMap.get("TRANS_TYPE").equals("DEBIT")){
                            cashDebitCount++;
                        }
                        if(transMap.get("TRANS_TYPE").equals("CREDIT")){
                            cashCreditCount++;
                        }
                    }
                }
                System.out.println("transIdMap^#^#^^#^#"+transIdMap);
                if(chkok!=true){
                int yesNo = 0;
                String[] voucherOptions = {"Yes", "No"};
                yesNo = COptionPane.showOptionDialog(null,"Do you want to print?", CommonConstants.WARNINGTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                null, voucherOptions, voucherOptions[0]);
                
                if (yesNo==0) {
                    com.see.truetransact.clientutil.ttrintegration.TTIntegration ttIntgration = null;
                    HashMap paramMap = new HashMap();
                    paramMap.put("TransDt", curr_dt);
                    paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                    Object keys[] = transIdMap.keySet().toArray();
                    for (int i=0; i<keys.length; i++) {
                        paramMap.put("TransId", keys[i]);
                        ttIntgration.setParam(paramMap);
//                        if (CommonUtil.convertObjToStr(transIdMap.get(keys[i])).equals("TRANSFER")) {
//                            ttIntgration.integrationForPrint("ReceiptPayment");
//                        } else if (CommonUtil.convertObjToStr(transTypeMap.get(keys[i])).equals("DEBIT")) {
//                            ttIntgration.integrationForPrint("CashPayment", false);
//                        } else if (CommonUtil.convertObjToStr(transTypeMap.get(keys[i])).equals("CREDIT")) {
//                            ttIntgration.integrationForPrint("CashReceipt", false);
//                        }
                    }
                    if(transferCount>0){
                        ttIntgration.integrationForPrint("ReceiptPayment");
                    }
                    if(cashDebitCount>0){
                        ttIntgration.integrationForPrint("CashPayment", false);
                    }
                    if(cashCreditCount>0){
                        ttIntgration.integrationForPrint("CashReceipt", false);
                    }
                }
            }
                
                actNum = (String)observable.getProxyReturnMap().get("ACCTNO");    
                if(chkok!=true){
                int yesNo = 0;
                String[] options = {"Yes", "No"};
                yesNo = COptionPane.showOptionDialog(null,"Do you want to print Deposit Loan Bond?", CommonConstants.WARNINGTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                null, options, options[0]);
                System.out.println("#$#$$ yesNo : "+yesNo);
               
                if (yesNo==0) {
                    com.see.truetransact.clientutil.ttrintegration.TTIntegration ttIntgration = null;
                    HashMap reportTransIdMap = new HashMap();
//                    reportTransIdMap.put("Act_Num", actNum);
                    reportTransIdMap.put("Acct_No", actNum);
                    ttIntgration.setParam(reportTransIdMap);
                    String transType = "";
//                    ttIntgration.integrationForPrint("DepositLoanBond");
                    ttIntgration.integrationForPrint("DepLoanApplication");
                }
                }  
                
//              HashMap unlienMap = new HashMap();
//                System.out.println("actNum 111>>>"+actNum);
//                System.out.println("CommonUtil.convertObjToStr(observable.getCbmDepositProduct().getKeyForSelected())>>>"+CommonUtil.convertObjToStr(observable.getCbmDepositProduct().getKeyForSelected()));   
//                unlienMap.put("PROD_ID", CommonUtil.convertObjToStr(observable.getCbmDepositProduct().getKeyForSelected()));
//                List unlienList =ClientUtil.executeQuery("getDepositUnlienCheck", unlienMap);
//                System.out.println("unlienList>>>"+unlienList);
//                if(unlienList!=null && unlienList.size()>0){
//                unlienMap=(HashMap)unlienList.get(0);
//                    System.out.println("unlienMap>>>>"+unlienMap);
//                String unlien = CommonUtil.convertObjToStr(unlienMap.get("DEPOSIT_UNLIEN"));
//                System.out.println("unlien asss>>>>"+unlien);
//                if(unlien.equals("Y") && chkDepositUnlien.isSelected()){
//                    String depNo = "";
//                    unlienMap = new HashMap();
//                   depNo = CommonUtil.convertObjToStr(txtDepositNo.getText());
//                   unlienMap.put("BATCH_ID",depNo);
//                 ClientUtil.execute("getUpdateDepositSubAcInfoStatusUnlien", unlienMap);   
//                }
//                }  
            }
            if(transNew){
                displayTransDetail();
            }
        }
//                           HashMap unlienMap = new HashMap();
//                //System.out.println("actNum 111>>>"+actNum);
//                System.out.println("CommonUtil.convertObjToStr(observable.getCbmDepositProduct().getKeyForSelected())>>>"+CommonUtil.convertObjToStr(observable.getCbmDepositProduct().getKeyForSelected()));   
//                unlienMap.put("PROD_ID", CommonUtil.convertObjToStr(observable.getCbmDepositProduct().getKeyForSelected()));
//                List unlienList =ClientUtil.executeQuery("getDepositUnlienCheck", unlienMap);
//                System.out.println("unlienList>>>"+unlienList);
//                if(unlienList!=null && unlienList.size()>0){
//                unlienMap=(HashMap)unlienList.get(0);
//                    System.out.println("unlienMap>>>>"+unlienMap);
//                String unlien = CommonUtil.convertObjToStr(unlienMap.get("DEPOSIT_UNLIEN"));
//                System.out.println("unlien asss>>>>"+unlien);
////                if(unlien.equals("Y") && chkDepositUnlien.isSelected()){
////                    String depNo = "";
////                    unlienMap = new HashMap();
////                   depNo = CommonUtil.convertObjToStr(txtDepositNo.getText());
////                   unlienMap.put("BATCH_ID",depNo);
////                   unlienMap.put("LIEN_DT",ClientUtil.getCurrentDate());
////                 ClientUtil.execute("getUpdateDepositSubAcInfoStatusUnlien", unlienMap); 
//////                 ClientUtil.execute("getUpdateDepositLienForDepositUnlien", unlienMap);
////                }
//                }
             observable.setNewTransactionMap(null);
             btnCancelActionPerformed(null);
             txtNextAccNo.setText("");
             txtNextAccNo.setEnabled(false);
             lblIntrestAmt.setText("");	
             btnNewActionPerformed(null);
    }//GEN-LAST:event_btnSaveActionPerformed
    private HashMap firstEnteredActNo() {
//        String sbAcNo = COptionPane.showInputDialog(this, resourceBundle.getString("REMARK_TRANSFER_TRANS"));
        HashMap acctDetailsMap = new HashMap();
        acctsearch = new AcctSearchUI();
        acctsearch.show();
        String sbAcNo = acctsearch.getAccountNo();
        String prodType = acctsearch.getProdType();
        System.out.println("prodType" + prodType);
        System.out.println("sbAcNo"+sbAcNo);
        acctDetailsMap.put("ACT_NUM", sbAcNo);
        acctDetailsMap.put("PROD_TYPE", prodType);
        return acctDetailsMap;
    }
    //while inserting new record we need to make transaction
       private void insertTransactionPart() {
        String prodId = CommonUtil.convertObjToStr(((ComboBoxModel) cboLoanProduct.getModel()).getKeyForSelected());
        System.out.println("prodIdprodIdprodId" + prodId);
        HashMap supMap = new HashMap();
        supMap.put("PROD_ID", prodId);
        List lstSupName = ClientUtil.executeQuery("getProdIdForSelectedItem", supMap);
        HashMap supMap1 = new HashMap();
        double maxPay = 0.0;
        if (lstSupName != null && lstSupName.size() > 0) {
            supMap1 = (HashMap) lstSupName.get(0);
            maxPay = CommonUtil.convertObjToDouble(supMap1.get("MAX_AMT_OF_CASH_PAYMENT"));
        }
        double loanAmount = CommonUtil.convertObjToDouble(txtLoanAmt.getText());
        HashMap singleAuthorizeMap = new HashMap();
        java.util.ArrayList arrList = new java.util.ArrayList();
        HashMap authDataMap = new HashMap();
        arrList.add(authDataMap);
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) { //&& observable.getAvailableBalance() >0
             if(loanRenewal==false){
            if (loanAmount > maxPay) {
                String[] debitType = {"Transfer"};
                int option3 = 0;
                if (option3 == 0) {
                    String transType = "";
                    //                System.out.println("!!! transType :"+transType);
                    while (CommonUtil.convertObjToStr(transType).length() == 0) {
                        transType = (String) COptionPane.showInputDialog(null, "Select Transaction Type", "Transaction type", COptionPane.QUESTION_MESSAGE, null, debitType, "");
                        if (CommonUtil.convertObjToStr(transType).length() > 0) {
                            if(observable.getDepoBehavesLike().equalsIgnoreCase("THRIFT")){
                            if (rdoMultiDisburseAllow_Yes.isSelected()) {
                                String remarks = "";
                                do {
                                    remarks = CommonUtil.convertObjToStr(COptionPane.showInputDialog(this, "Enter Transaction Amount"));
                                    //  if(!strFacilityType.equals("OD"))
                                    {
                                        authDataMap.put("MULTIDISBURSE", "Y");
                                    }
                                } while (remarks.length() == 0);
                                authDataMap.put("LIMIT", remarks);
                            } else {
                                authDataMap.put("LIMIT", txtLoanAmt.getText());
                            }}
                            else{
                              authDataMap.put("LIMIT", txtLoanAmt.getText());
                            }
                            authDataMap.put("TRANSACTION_PART", "TRANSACTION_PART");
                            authDataMap.put("TRANS_TYPE", transType.toUpperCase());
                           // authDataMap.put("LIMIT", txtLoanAmt.getText());
                            authDataMap.put("USER_ID", TrueTransactMain.USER_ID);
                            authDataMap.put("BRANCH_CODE", observable.getSelectedBranchID());
                            if (CommonUtil.convertObjToStr(transType.toUpperCase()).equals("CASH")) {
                                boolean flag = true;


                                do {
                                    String tokenNo = COptionPane.showInputDialog(this, resourceBundle.getString("REMARK_CASH_TRANS"));
                                    if (tokenNo != null && tokenNo.length() > 0) {
                                        flag = tokenValidation(tokenNo);
                                        chktrans = false;
                                        chkok = false;
                                    }
//                                else{
//                                    ClientUtil.showMessageWindow("Transaction Not Created");
//                                    authDataMap.remove("TRANSACTION_PART");
//                                    observable.setNewTransactionMap(null);
//                                    flag = true;
//                                    chktrans = true;
//                                }
                                    if (flag == false) {
                                        ClientUtil.showAlertWindow("Token is invalid or not issued for you. Please verify.");
                                    } else {
                                        authDataMap.put("TOKEN_NO", tokenNo);

                                    }
//                          
                                } while (!flag);
                            } else if (CommonUtil.convertObjToStr(transType.toUpperCase()).equals("TRANSFER")) {
                                boolean flag = true;
                                do {
                                    String sbAcNo = null;
                                    String prodType = null;
                                    HashMap acctDetailsMap = firstEnteredActNo();
                                    if (acctDetailsMap != null && acctDetailsMap.size() > 0 && acctDetailsMap.containsKey("ACT_NUM") && acctDetailsMap.containsKey("PROD_TYPE")) {
                                        sbAcNo = CommonUtil.convertObjToStr(acctDetailsMap.get("ACT_NUM"));
                                        prodType = CommonUtil.convertObjToStr(acctDetailsMap.get("PROD_TYPE"));
                                    }
                                    if (sbAcNo != null && sbAcNo.length() > 0) {
                                        flag = checkingActNo(sbAcNo);
                                        if (flag == false && finalChecking == false) {
                                            ClientUtil.showAlertWindow("Account No is invalid, Please enter correct no");
                                        } else {
                                            authDataMap.put("CR_ACT_NUM", sbAcNo);
                                            authDataMap.put("PROD_TYPE", prodType);

                                            chkok = false;
                                        }
                                        finalChecking = false;
                                    } else {
                                        ClientUtil.showMessageWindow("Transaction Not Created");
                                        flag = true;
                                        authDataMap.remove("TRANSACTION_PART");
                                        observable.setNewTransactionMap(null);
                                        chktrans = true;
                                    }
                                } while (!flag);
                            }
                            observable.setNewTransactionMap(authDataMap);

                        } else {
                            transType = "Cancel";
                            chktrans = true;

                        }
                    }
                }

            } else {
                String[] debitType = {"Cash", "Transfer"};

                int option3 = 0;
                if (option3 == 0) {
                    String transType = "";
                    //                System.out.println("!!! transType :"+transType);
                    while (CommonUtil.convertObjToStr(transType).length() == 0) {
                        transType = (String) COptionPane.showInputDialog(null, "Select Transaction Type", "Transaction type", COptionPane.QUESTION_MESSAGE, null, debitType, "");
                        if (CommonUtil.convertObjToStr(transType).length() > 0) {
                            if (observable.getDepoBehavesLike().equalsIgnoreCase("THRIFT")) {
                                if (rdoMultiDisburseAllow_Yes.isSelected()) {
                                    String remarks = "";
                                    do {
                                        remarks = CommonUtil.convertObjToStr(COptionPane.showInputDialog(this, "Enter Transaction Amount"));
                                        //  if(!strFacilityType.equals("OD"))
                                        {
                                            authDataMap.put("MULTIDISBURSE", "Y");
                                        }
                                    } while (remarks.length() == 0);
                                    authDataMap.put("LIMIT", remarks);
                                } else {
                                    authDataMap.put("LIMIT", txtLoanAmt.getText());
                                }
                            } else {
                                 authDataMap.put("LIMIT", txtLoanAmt.getText());
                            }
                            authDataMap.put("TRANSACTION_PART", "TRANSACTION_PART");
                            authDataMap.put("TRANS_TYPE", transType.toUpperCase());
                          //  authDataMap.put("LIMIT", txtLoanAmt.getText());
                            authDataMap.put("USER_ID", TrueTransactMain.USER_ID);
                            authDataMap.put("BRANCH_CODE", observable.getSelectedBranchID());
                            if (CommonUtil.convertObjToStr(transType.toUpperCase()).equals("CASH")) {
                                boolean flag = true;


                                do {
                                    String tokenNo = COptionPane.showInputDialog(this, resourceBundle.getString("REMARK_CASH_TRANS"));
                                    if (tokenNo != null && tokenNo.length() > 0) {
                                        flag = tokenValidation(tokenNo);
                                        chktrans = false;
                                        chkok = false;
                                    }
//                                else{
//                                    ClientUtil.showMessageWindow("Transaction Not Created");
//                                    authDataMap.remove("TRANSACTION_PART");
//                                    observable.setNewTransactionMap(null);
//                                    flag = true;
//                                    chktrans = true;
//                                }
                                    if (flag == false) {
                                        ClientUtil.showAlertWindow("Token is invalid or not issued for you. Please verify.");
                                    } else {
                                        authDataMap.put("TOKEN_NO", tokenNo);

                                    }
//                          
                                } while (!flag);
                            } else if (CommonUtil.convertObjToStr(transType.toUpperCase()).equals("TRANSFER")) {
                                boolean flag = true;
                                do {

                                    String sbAcNo = null;
                                    String prodType = null;
                                    HashMap acctDetailsMap = firstEnteredActNo();
                                    if (acctDetailsMap != null && acctDetailsMap.size() > 0 && acctDetailsMap.containsKey("ACT_NUM") && acctDetailsMap.containsKey("PROD_TYPE")) {
                                        sbAcNo = CommonUtil.convertObjToStr(acctDetailsMap.get("ACT_NUM"));
                                        prodType = CommonUtil.convertObjToStr(acctDetailsMap.get("PROD_TYPE"));
                                    }
                                    if (sbAcNo != null && sbAcNo.length() > 0) {
                                        flag = checkingActNo(sbAcNo);
                                        if (flag == false && finalChecking == false) {
                                            ClientUtil.showAlertWindow("Account No is invalid, Please enter correct no");
                                        } else {
                                            authDataMap.put("CR_ACT_NUM", sbAcNo);
                                            authDataMap.put("PROD_TYPE", prodType);
                                            chkok = false;
                                        }
                                        finalChecking = false;
                                    } else {
                                        ClientUtil.showMessageWindow("Transaction Not Created");
                                        flag = true;
                                        authDataMap.remove("TRANSACTION_PART");
                                        observable.setNewTransactionMap(null);
                                        chktrans = true;
                                    }
                                } while (!flag);
                            }
                            observable.setNewTransactionMap(authDataMap);

                        } else {
                            transType = "Cancel";
                            chktrans = true;

                        }
                    }
                }

            }
             }
             //added by rishad 04/09/2016 for loan renwal
            else {
                String[] debitType = {"Transfer"};
                int option3 = 0;
                if (option3 == 0) {
                    String transType = "";
                    //                //System.out.println("!!! transType :"+transType);
                    while (CommonUtil.convertObjToStr(transType).length() == 0) {
                        transType = (String) COptionPane.showInputDialog(null, "Select Transaction Type", "Transaction type", COptionPane.QUESTION_MESSAGE, null, debitType, "");
                        if (CommonUtil.convertObjToStr(transType).length() > 0) {
                            authDataMap.put("TRANSACTION_PART", "TRANSACTION_PART");
                            authDataMap.put("TRANS_TYPE", transType.toUpperCase());
                          //  String strFacilityType = getCboTypeOfFacilityKeyForSelected();
                            authDataMap.put("LOAN_RENEWAL", "LOAN_RENEWAL");
                            authDataMap.put("OLD_LOAN_AMT", oldloanAmt);
                            authDataMap.put("LIMIT", txtLoanAmt.getText());
                            if (CommonUtil.convertObjToStr(transType.toUpperCase()).equals("TRANSFER")) {
                                boolean flag = true;
                                do {
                                    String sbAcNo = null;
                                    String prodType = null;
                                    HashMap acctDetailsMap = firstEnteredActNo();
                                    if (acctDetailsMap != null && acctDetailsMap.size() > 0 && acctDetailsMap.containsKey("ACT_NUM") && acctDetailsMap.containsKey("PROD_TYPE")) {
                                        sbAcNo = CommonUtil.convertObjToStr(acctDetailsMap.get("ACT_NUM"));
                                        prodType = CommonUtil.convertObjToStr(acctDetailsMap.get("PROD_TYPE"));
                                    }
                                    if (sbAcNo != null && sbAcNo.length() > 0) {
                                        flag = checkingActNo(sbAcNo);
                                        flag = true;
                                        if (flag == false && finalChecking == false) {
                                            ClientUtil.showAlertWindow("Account No is invalid, Please enter correct no");
                                        } else {
                                            authDataMap.put("CR_ACT_NUM", sbAcNo);
                                            authDataMap.put("PROD_TYPE", prodType);
                                            authDataMap.put("RENWAL_ACT_NUM", suspenseActNum);
                                            authDataMap.put("RENWAl_PROD_TYPE", "SA");
                                            if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")) {
                                                if (loannaration != null) {
                                                    loannaration = loannaration + oldloanAmt;
                                                    oldloanAmt = 0;
                                                    authDataMap.put("OLD_LOAN_NARRATION", loannaration);
                                                }
                                            }
                                            chkok = false;
                                        }
                                        finalChecking = false;
                                    } else {
                                        ClientUtil.showMessageWindow("Transaction Not Created");
                                        flag = true;
                                        authDataMap.remove("TRANSACTION_PART");
                                        observable.setNewTransactionMap(null);
                                        chktrans = true;
                                    }
                                } while (!flag);
                            }
                            observable.setNewTransactionMap(authDataMap);
                        } else {
                            transType = "Cancel";
                            chktrans = true;
                        }
                    }
                }
            }
        }
    }   
     private boolean checkingActNo(String sbAcNo){
        boolean flag = false;
        HashMap existingMap = new HashMap();
        existingMap.put("ACT_NUM",sbAcNo.toUpperCase());
        List mapDataList = ClientUtil.executeQuery("getAccNoDet", existingMap);
        //System.out.println("#### mapDataList :"+mapDataList);
        if (mapDataList!=null && mapDataList.size()>0) {
            existingMap = (HashMap)mapDataList.get(0);
            if(existingMap != null && !ProxyParameters.BRANCH_ID.equals(CommonUtil.convertObjToStr(existingMap.get("BRANCH_CODE")))){
                Date selectedBranchDt = ClientUtil.getOtherBranchCurrentDate(CommonUtil.convertObjToStr(existingMap.get("BRANCH_CODE")));
                Date currentDate = ClientUtil.getCurrentDate();
                System.out.println("selectedBranchDt : "+selectedBranchDt + " currentDate : "+currentDate);
                if(selectedBranchDt == null){
                    ClientUtil.displayAlert("BOD is not completed for the selected branch " +"\n"+"Interbranch Transaction Not allowed");
                    flag = false;
                    finalChecking = true;
                }
                //else if(DateUtil.dateDiff(currentDate, selectedBranchDt)!=0){ 
                else if(DateUtil.dateDiff(currentDate, selectedBranchDt)< 0){ //KD-3498 - added by nithya
                    ClientUtil.displayAlert("Application Date is different in the Selected branch " +"\n"+"Interbranch Transaction Not allowed");
                    flag = false;
                    finalChecking = true;
                } else {
                    System.out.println("Continue for interbranch trasactions ...");
                    String[] obj5 = {"Proceed", "ReEnter"};
                    chktrans = false;
                    int option4 = COptionPane.showOptionDialog(null, ("Please check whether Account No, Name correct or not " + "\nOperative AcctNo is : " + existingMap.get("ACCOUNT NUMBER") + "\nCustomer Name :" + existingMap.get("CUSTOMER NAME")), ("Transaction Part"),
                    COptionPane.YES_NO_CANCEL_OPTION, COptionPane.QUESTION_MESSAGE, null, obj5, obj5[0]);
                    if (option4 == 0) {
                        flag = true;
                    } else {
                        flag = false;
                    }
                }
            } else{
                String[] obj5 = {"Proceed", "ReEnter"};
                chktrans = false;
                int option4 = COptionPane.showOptionDialog(null, ("Please check whether Account No, Name correct or not " + "\nOperative AcctNo is : " + existingMap.get("ACCOUNT NUMBER") + "\nCustomer Name :" + existingMap.get("CUSTOMER NAME")), ("Transaction Part"),
                COptionPane.YES_NO_CANCEL_OPTION, COptionPane.QUESTION_MESSAGE, null, obj5, obj5[0]);
                if (option4 == 0) {
                    flag = true;
                } else {
                    flag = false;
                }
            }
        }
        return flag;
    }
   
//     private String firstEnteredActNo(){
////        String sbAcNo = COptionPane.showInputDialog(this,resourceBundle.getString("REMARK_TRANSFER_TRANS"));
//        
//         acctsearch=new AcctSearchUI();
//         acctsearch.show();
//         String sbAcNo=acctsearch.getAccountNo();
//         return sbAcNo;
//         
//    }
     
    private void setJointCustData() { // Added by nithya on 24-09-2020 for KD-2275
        ArrayList jointCustList = new ArrayList();        
        if (tblDepositDetails.getRowCount() > 0) {
            for (int i = 0; i < tblDepositDetails.getRowCount(); i++) {
                String depositNo = CommonUtil.convertObjToStr(tblDepositDetails.getValueAt(i, 0));
                HashMap custMap = new HashMap();
                custMap.put("DEPOSIT_NO", depositNo);
                List custList = ClientUtil.executeQuery("getJointCustDataForDepositLoan", custMap);
                if (custList != null && custList.size() > 0) {
                    for (int l = 0; l < custList.size(); l++) {
                        custMap = (HashMap) custList.get(l);
                        String custId = CommonUtil.convertObjToStr(custMap.get("CUST_ID"));
                        jointCustList.add(custId);
                    }
                }
            }            
        }
        if(jointCustList != null && jointCustList.size() > 0){
            Set<String> finalSet = new LinkedHashSet(jointCustList);
            jointCustList = new ArrayList(finalSet);            
        }    
        observable.setJointCustData(jointCustList);
    }    

    private void btnsaveActionPerformed(){
          updateOBFields();
        finalizeCharges(); //charge details
        if (CommonUtil.convertObjToStr(txtDepositNo.getText()).length() > 0 && newRecord) {
            observable.setTableData(false);
            productEnableDisable(false);
//                savetoTable();
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            observable.setAllScreenData();
        }
        observable.checkMultipleDeposit();
        if (observable.getDepoBehavesLike().equalsIgnoreCase("THRIFT")) {
            btnEMI_CalculateActionPerformed();
        }
        //if(observable.getProductAuthRemarks().equals("OTHER_LOAN")){ // Added by nithya on 24-09-2020 for KD-2275
           setJointCustData();
        //}
        if (observable.getActionType()==ClientConstants.ACTIONTYPE_NEW){
            observable.doAction(1);
            
        }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
            observable.doAction(1);
        }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE){
              observable.setTableData(false);
            observable.doAction(1);
        }
        
            
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        //observable.setAuthorizeNo();
        //        if (observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT && loanType.equals("LTD")) {
//        //            btnFacilitySavePressed();
//        //            saveAction();
//        //        } else
//        String mandatoryMessage="";
//        mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panSanctionDetails_Sanction);
//        /* mandatoryMessage1 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
//        //        if (chkMoratorium_Given.isSelected() && (txtFacility_Moratorium_Period.getText().length() == 0)){
//        //            TermLoanRB termLoanRB = new TermLoanRB();
//        //            mandatoryMessage = mandatoryMessage + termLoanRB.getString("moratorium_Given_Warning");
//        //            termLoanRB = null;
//        //        }
//        //ltd loan number not generator nut checking lien
//        if(loanType.equals("LTD") && observable.getStrACNumber().length()==0) {
//            String mainLimit=CommonUtil.convertObjToStr(txtLimit_SD.getText());
//            if(mainLimitMarginValidation(mainLimit))
//                return;
//            if(checkInterestRateForLTD()){
//                return;
//            }
//        }
//        //for renew   od
//        txtLimit_SDFocusLostOD(false);
//        //
//        //check sanctionDetails change or not if change delete repayment schedule
//        //        if(sanctionDetailsBasedRepayment())
//        //            return;
//        
//        //        mandatoryMessage = mandatoryMessage + new MandatoryCheck().checkMandatory(getClass().getName(), panSanctionDetails_Sanction);
//        /* mandatoryMessage1 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
//        
//        //        mandatoryMessage = mandatoryMessage + new MandatoryCheck().checkMandatory(getClass().getName(), panSanctionDetails_Mode);
//        /* mandatoryMessage2 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
//        //        int sanctionRow=tblSanctionDetails.getSelectedRow();
//        //        if ((sanctionRow !=-1  || enableControls)&& mandatoryMessage.length() > 0){
//        //        displayAlert(mandatoryMessage);
//        //        return;
//        //        }else{
//        //check repay detail delete or not
//        //            if(!((observable.getTxtLimit_SD()).equals(txtLimit_SD.getText())) ||
//        //            !((observable.getTxtNoInstallments()).equals(txtNoInstallments.getText())) ||
//        //            !(((String)cboRepayFreq.getSelectedItem()).equals(observable.getCboRepayFreq() )) ||
//        //            !((observable.getTdtFDate()).equals(tdtFDate.getDateValue()))){
//        //
//        //
//        //                accNumMap.put(lblAcctNo_Sanction_Disp.getText(),lblAcctNo_Sanction_Disp.getText());
//        //            }
//        //end checking
//        //            boolean periodFlag = false;
//        //            boolean limitFlag = false;
//        //            if (loanType.equals("OTHERS")) {
//        //                if (!(cboRepayFreq.getSelectedItem().equals("User Defined") || cboRepayFreq.getSelectedItem().equals("Lump Sum")) && !observable.checkFacilityPeriod(txtNoInstallments.getText(), txtFacility_Moratorium_Period.getText())){
//        //                    observable.decoratePeriod();
//        //                    mandatoryMessage = mandatoryMessage.concat("The Limit Period must fall within "+observable.getMinDecLoanPeriod()+" and  "+observable.getMaxDecLoanPeriod());
//        //                    periodFlag = false;
//        //                }else if ((cboRepayFreq.getSelectedItem().equals("User Defined") || cboRepayFreq.getSelectedItem().equals("Lump Sum")) && !observable.checkFacilityPeriod(DateUtil.getDateMMDDYYYY(tdtFDate.getDateValue()), DateUtil.getDateMMDDYYYY(tdtTDate.getDateValue()))){
//        //                    observable.decoratePeriod();
//        //                    mandatoryMessage = mandatoryMessage.concat("The Limit Period must fall within "+observable.getMinDecLoanPeriod()+" and  "+observable.getMaxDecLoanPeriod());
//        //                    periodFlag = false;
//        //                }else{
//        //                    periodFlag = true;
//        //                }
//        //            }else{
//        //                periodFlag = true;
//        //            }
//        //            if (loanType.equals("OTHERS")) {
//        //                if (!observable.checkLimitValue(txtLimit_SD.getText())) {
//        //                    observable.setTxtLimit_SD("");
//        //                    txtLimit_SD.setText(observable.getTxtLimit_SD());
//        //                    mandatoryMessage = mandatoryMessage.concat("\nThe Limit value must fall within "+observable.getMinLimitValue().toString()+" and  "+observable.getMaxLimitValue().toString());
//        //                    limitFlag = false;
//        //                }else{
//        //                    limitFlag = true;
//        //                }
//        //            }else{
//        //                limitFlag = true;
//        //            }
//        //        }
//        
//        
//        mandatoryMessage = mandatoryMessage + new MandatoryCheck().checkMandatory(getClass().getName(), panSanctionDetails_Table);
//        /* mandatoryMessage1 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
//        
//        //                mandatoryMessage = mandatoryMessage + new MandatoryCheck().checkMandatory(getClass().getName(), panFDAccount);
//        //        /* mandatoryMessage2 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
//        //
//        //                mandatoryMessage = mandatoryMessage + new MandatoryCheck().checkMandatory(getClass().getName(), panFDDate);
//        //        /* mandatoryMessage3 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
//        
//        //        mandatoryMessage = mandatoryMessage + isJointAcctHavingAtleastOneCust();
//        //        if (productBasedValidation()){
//        //            mandatoryMessage = mandatoryMessage + new TermLoanMRB().getString("rdoMultiDisburseAllow_Yes");
//        //        }
//        //
//        //        if (observable.getStrACNumber().length() > 0){
//        ////            mandatoryMessage = mandatoryMessage + new MandatoryCheck().checkMandatory(getClass().getName(), panProd_IM);
//        //            mandatoryMessage = mandatoryMessage + isInterestDetailsExistForThisAcct();
//        //            mandatoryMessage = mandatoryMessage + validateOtherDetailsMandatoryFields();
//        //        }
//        if (mandatoryMessage.length() > 0){
//            displayAlert(mandatoryMessage);
//        }else if (rdoPriority.isSelected() == false && rdoNonPriority.isSelected() == false){
//            ClientUtil.showAlertWindow("Priority or NonPriroity should not be empty !!! ");
//        }else if (cboPurposeOfLoan.getSelectedIndex()<=0){
//            ClientUtil.showAlertWindow("Purpose of loan should not be empty !!! ");
//        }else if(cboAppraiserId.getSelectedIndex()<= 0){
//            ClientUtil.showAlertWindow("Appraiser id should not be empty !!! ");
//        }else if(txtAreaParticular.getText().length() == 0){
//            ClientUtil.showAlertWindow("Particular should not be empty !!! ");
//        }else if(txtCustID.getText().length() == 0){
//            ClientUtil.showAlertWindow("Customer Id should not be empty !!!");
//        }else if(txtMargin.getText().length() == 0){
//            ClientUtil.showAlertWindow("Margin should not be empty !!!");
//        }else if(CommonUtil.convertObjToDouble(txtLimit_SD.getText()).doubleValue()>CommonUtil.convertObjToDouble(txtEligibleLoan.getText()).doubleValue()){
//            ClientUtil.showAlertWindow("Sanction Limit Amount should be less than the Eligible Loan Amount !!!");
//        }else if(txtLimit_SD.getText().length() == 0){
//            ClientUtil.showAlertWindow("Limit Amount Should Not be Empty !!!");
//        }else if(CommonUtil.convertObjToDouble(txtInter.getText()).doubleValue() == 0 && CommonUtil.convertObjToDouble(txtPenalInter.getText()).doubleValue() == 0) {
//            ClientUtil.showAlertWindow("Rate of Interest and Penal Interest not set for this Product !!! ");
//        }else if(txtNoInstallmentsFocusLost()){
//            return;
//        }else if ((((ComboBoxModel)cboConstitution.getModel()).getKeyForSelected()).equals(JOINT_ACCOUNT) &&  tblBorrowerTabCTable.getRowCount() == 1){
//            ClientUtil.showAlertWindow("Select the Joint Account Holder !!! ");
//        }else{
//            if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
//                EMI_CalculateActionPerformed();
//            }
//            updateOBFields();
//            finalizeCharges();
//            if(transNew){
//                insertTransactionPart();
//            }
//            saveAction();
//            //            displayTransDetail();
//            
//        }
    }
    
    //charge details
    
        private void finalizeCharges() {
        HashMap chargeMap = new HashMap();
        if(chargelst!=null && chargelst.size()>0){
            for(int i=0; i<chargelst.size(); i++){
                String accHead="";
                chargeMap = (HashMap)chargelst.get(i);
                accHead = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_ID"));
                System.out.println("$#@@$ accHead>>>>>@@"+accHead);
                for(int j=0; j<table.getRowCount();j++) {
                    System.out.println("$#@@$ accHead inside table "+table.getValueAt(j, 1));
                     double chrgAmt =CommonUtil.convertObjToDouble(table.getValueAt(j, 3));
                    if (CommonUtil.convertObjToStr(table.getValueAt(j, 1)).equals(accHead) && !((Boolean)table.getValueAt(j, 0)).booleanValue()) {
                        chargelst.remove(i--);
                    }
                    if(CommonUtil.convertObjToStr(table.getValueAt(j, 1)).equals(accHead) && ((Boolean)table.getValueAt(j, 0)).booleanValue()){
                        chargeMap.put("CHARGE_AMOUNT",chrgAmt);
                        chargelst.set(i, chargeMap);
                    }
                   
                }
            }
           // System.out.println("#$#$$# final chargelst:"+chargelst);
            
            observable.setChargelst(chargelst);
        //    System.out.println("$#@@$ setChargelst>>>>>@@"+accHead);
        }
        
    }
    
    //charge end..
    
//    private void finalizeCharges() {
////        HashMap chargeMap = new HashMap();
////        if(chargelst!=null && chargelst.size()>0){
////            for(int i=0; i<chargelst.size(); i++){
////                String accHead="";
////                chargeMap = (HashMap)chargelst.get(i);
////                accHead = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_DESC"));
////                System.out.println("$#@@$ accHead"+accHead);
////                for(int j=0; j<table.getRowCount();j++) {
////                    System.out.println("$#@@$ accHead inside table "+table.getValueAt(j, 1));
////                    if (CommonUtil.convertObjToStr(table.getValueAt(j, 1)).equals(accHead) && !((Boolean)table.getValueAt(j, 0)).booleanValue()) {
////                        chargelst.remove(i--);
////                    }
////                }
////            }
////            System.out.println("#$#$$# final chargelst:"+chargelst);
////            observable.setChargelst(chargelst);
////        }
////        
//    }
//    
//    private void insertTransactionPart() {
////        HashMap singleAuthorizeMap = new HashMap();
////        java.util.ArrayList arrList = new java.util.ArrayList();
////        HashMap authDataMap = new HashMap();
////        if(observable.getStrACNumber().length()>0){
////            authDataMap.put("ACCT_NUM", observable.getStrACNumber());
////        }else{
////            authDataMap.put("ACCT_NUM", lblAcctNo_Sanction_Disp.getText());
////        }
////        arrList.add(authDataMap);
////        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT && observable.getAvailableBalance() >0){
////            //        if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE && observable.getAvailableBalance() >0){
////            String [] debitType = {"Cash","Transfer"};
////            String[] obj4 = {"Yes","No"};
////            //            int option3 = COptionPane.showOptionDialog(null,("Do you want to make Transaction?"), ("Transaction"),
////            //            COptionPane.YES_NO_CANCEL_OPTION,COptionPane.QUESTION_MESSAGE,null,obj4,obj4[0]);
////            int option3 = 0;
////            if(option3 == 0){
////                String transType = (String)COptionPane.showInputDialog(null,"Select Transaction Type", "Transaction type", COptionPane.QUESTION_MESSAGE, null, debitType, "");
////                authDataMap.put("TRANSACTION_PART","TRANSACTION_PART");
////                authDataMap.put("TRANS_TYPE",transType.toUpperCase());
////                //                authDataMap.put("APPRAISER_AMT","100");
////                //                authDataMap.put("SERVICE_TAX_AMT","50");
////                if(CommonUtil.convertObjToStr(transType.toUpperCase()).equals("CASH")){
////                    boolean flag = true;
////                    do {
////                        String tokenNo = COptionPane.showInputDialog(this,resourceBundle.getString("REMARK_CASH_TRANS"));
////                        if (tokenNo != null && tokenNo.length()>0) {
////                            flag = tokenValidation(tokenNo);
////                        }else{
////                            flag = true;
////                        }
////                        if(flag == false){
////                            ClientUtil.showAlertWindow("Token is invalid or not issued for you. Please verify.");
////                        }else{
////                            authDataMap.put("TOKEN_NO",tokenNo);
////                        }
////                        //                        } else {
////                        //                            ClientUtil.showMessageWindow("Transaction Not Created");
////                        //                            flag = true;
////                        //                            authDataMap.remove("TRANSACTION_PART");
////                        //                        }
////                    } while (!flag);
////                }else if(CommonUtil.convertObjToStr(transType.toUpperCase()).equals("TRANSFER")){
////                    boolean flag = true;
////                    do {
////                        String sbAcNo = firstEnteredActNo();
////                        if(sbAcNo!=null && sbAcNo.length()>0){
////                            flag = checkingActNo(sbAcNo);
////                            if(flag == false && finalChecking == false){
////                                ClientUtil.showAlertWindow("Account No is invalid, Please enter correct no");
////                            }else{
////                                authDataMap.put("CR_ACT_NUM",sbAcNo);
////                            }
////                            finalChecking = false;
////                        } else {
////                            ClientUtil.showMessageWindow("Transaction Not Created");
////                            flag = true;
////                            authDataMap.remove("TRANSACTION_PART");
////                        }
////                    } while (!flag);
////                }
////                observable.setTransactionMap(authDataMap);
////            }
////            
////        }
//    }
    
    private void displayTransDetail() {
        if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            String displayStr = "";
            String oldBatchId = "";
            String newBatchId = "";
            String actNum = CommonUtil.convertObjToStr(observable.getStrACNumber());
            HashMap transMap = new HashMap();
            transMap.put("LOAN_NO",observable.getStrACNumber());
            transMap.put("CURR_DT", curr_dt);
            List lst = ClientUtil.executeQuery("getTransferTransLoanAuthDetails", transMap);
            if(lst !=null && lst.size()>0){
                displayStr += "Transfer Transaction Details...\n";
                for(int i = 0;i<lst.size();i++){
                    transMap = (HashMap)lst.get(i);
                    displayStr += "Trans Id : "+transMap.get("TRANS_ID")+
                    "   Batch Id : "+transMap.get("BATCH_ID")+
                    "   Trans Type : "+transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if(actNum != null && !actNum.equals("")){
                        displayStr +="   Account No : "+transMap.get("ACT_NUM")+
                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }else{
                        displayStr += "   Ac Hd Desc : "+transMap.get("AC_HD_ID")+
                        "   Interest Amount : "+transMap.get("AMOUNT")+"\n";
                    }
                    System.out.println("#### :" +transMap);
                    oldBatchId = newBatchId;
                }
            }
            actNum = CommonUtil.convertObjToStr(observable.getStrACNumber());
            transMap = new HashMap();
            transMap.put("LOAN_NO",actNum);
            transMap.put("CURR_DT", curr_dt);
            lst = ClientUtil.executeQuery("getCashTransLoanAuthDetails", transMap);
            if(lst !=null && lst.size()>0){
                displayStr += "Cash Transaction Details...\n";
                for(int i = 0;i<lst.size();i++){
                    transMap = (HashMap)lst.get(i);
                    displayStr +="Trans Id : "+transMap.get("TRANS_ID")+
                    "   Trans Type : "+transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if(actNum != null && !actNum.equals("")){
                        displayStr +="   Account No :  "+transMap.get("ACT_NUM")+
                        "   Amount :  "+transMap.get("AMOUNT")+"\n";
                    }else{
                        displayStr +="   Ac Hd Desc :  "+transMap.get("AC_HD_ID")+
                        "   Interest Amount :  "+transMap.get("AMOUNT")+"\n";
                    }
                }
            }
            if(!displayStr.equals("")){
                ClientUtil.showMessageWindow(""+displayStr);
            }
        }
    }
    
    
    private void saveAction() {
        
//        if (!viewType.equals("Delete"))
//            updateOBFields();
//        if(observable.getStrACNumber().length()==0 && loanType.equals("LTD"))
//            observable.setLTDSecurityDetails();
//        final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(),panBorrowCompanyDetails);
//        StringBuffer strBAlert = new StringBuffer();
//        if(mandatoryMessage.length() > 0 ){
//            //                displayAlert(mandatoryMessage);
//            strBAlert.append(mandatoryMessage+"\n");
//        }
//        if( observable.getResult() != ClientConstants.ACTIONTYPE_DELETE){
//            //__ To Check if the Total Share of the Nominee(s) is 100% or not...
//            
//            String alert = nomineeUi.validateData();
//            if(!alert.equalsIgnoreCase("")){
//                strBAlert.append(alert);
//            }
//        }
//        
//        observable.doAction(nomineeUi.getNomineeOB(),1);
//        
//        
//        //        super.removeEditLock(lblBorrowerNo_2.getText()); remove edit lock by abi for authorize only remove the lock
//        if(observable.getResult()==ClientConstants.ACTIONTYPE_FAILED){
//            observable.setResultStatus();
//            return;
//        } else {
//            if (observable.getProxyReturnMap()!=null && observable.getProxyReturnMap().containsKey("ACCTNO")) {
//                String actNum = (String)observable.getProxyReturnMap().get("ACCTNO");
//                int yesNo = 0;
//                String[] options = {"Yes", "No"};
//                yesNo = COptionPane.showOptionDialog(null,"Do you want to print GoldBond?", CommonConstants.WARNINGTITLE,
//                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
//                null, options, options[0]);
//                System.out.println("#$#$$ yesNo : "+yesNo);
//                if (yesNo==0) {
//                    com.see.truetransact.clientutil.ttrintegration.TTIntegration ttIntgration = null;
//                    HashMap reportTransIdMap = new HashMap();
//                    reportTransIdMap.put("Act_Num", actNum);
//                    ttIntgration.setParam(reportTransIdMap);
//                    String transType = "";
//                    ttIntgration.integrationForPrint("Goldbond");
//                }
//                
//                HashMap transTypeMap = new HashMap();
//                HashMap transMap = new HashMap();
//                HashMap transCashMap = new HashMap();
//                transCashMap.put("BATCH_ID",actNum);
//                transCashMap.put("TRANS_DT", curr_dt);
//                transCashMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
//                HashMap transIdMap = new HashMap();
//                List list = ClientUtil.executeQuery("getTransferDetails", transCashMap);
//                if(list !=null && list.size()>0){
//                    for(int i = 0;i<list.size();i++){
//                        transMap = (HashMap)list.get(i);
//                        transIdMap.put(transMap.get("BATCH_ID"),"TRANSFER");
//                    }
//                }
//                list = ClientUtil.executeQuery("getCashDetails", transCashMap);
//                if(list !=null && list.size()>0){
//                    for(int i = 0;i<list.size();i++){
//                        transMap = (HashMap)list.get(i);
//                        transIdMap.put(transMap.get("TRANS_ID"),"CASH");
//                        transTypeMap.put(transMap.get("TRANS_ID"),transMap.get("TRANS_TYPE"));
//                    }
//                }
//                //                yesNo = 0;
//                //                String[] voucherOptions = {"Yes", "No"};
//                //                yesNo = COptionPane.showOptionDialog(null,"Do you want to print Voucher?", CommonConstants.WARNINGTITLE,
//                //                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
//                //                null, voucherOptions, voucherOptions[0]);
//                //                if (yesNo==0) {
//                //                    com.see.truetransact.clientutil.ttrintegration.TTIntegration ttIntgration = null;
//                //                    HashMap paramMap = new HashMap();
//                //                    paramMap.put("TransDt", curr_dt);
//                //                    paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
//                //                    Object keys[] = transIdMap.keySet().toArray();
//                //                    for (int i=0; i<keys.length; i++) {
//                //                        paramMap.put("TransId", keys[i]);
//                //                        ttIntgration.setParam(paramMap);
//                //                        if (CommonUtil.convertObjToStr(transIdMap.get(keys[i])).equals("TRANSFER")) {
//                //                            ttIntgration.integrationForPrint("ReceiptPayment");
//                //                        } else if (CommonUtil.convertObjToStr(transTypeMap.get(keys[i])).equals("DEBIT")) {
//                //                            ttIntgration.integrationForPrint("CashPayment", false);
//                //                        } else {
//                //                            ttIntgration.integrationForPrint("CashReceipt", false);
//                //                        }
//                //                    }
//                //                }
//            }
//            if(transNew){
//                displayTransDetail();
//            }
//        }
//        observable.resetForm();
//        deletescreenLock();
//        //        lblTotalLimitAmt.setText("");
//        //        resetAllExtendedTab();
//        observable.resetAllFacilityDetails();
//        updateCboTypeOfFacility();
//        ClientUtil.enableDisable(this, false);
//        setButtonEnableDisable();
//        setAllTableBtnsEnableDisable(false); // To disable the Tool buttons for the CTable
//        setbtnCustEnableDisable(false);  // To disable the Customer Buttons
//        observable.setResultStatus();
//        //        authSignUI.setLblStatus(observable.getLblStatus());
//        //        poaUI.setLblStatus(observable.getLblStatus());
//        observable.destroyObjects();
//        observable.createObject();
//        observable.ttNotifyObservers();
//        rowSanctionFacility = -1;
//        sandetail = false;
//        enableControls = false;
//        txtMargin.setText("");
//        txtMarginAmt.setText("");
//        txtEligibleLoan.setText("");
//        //        txtAvalSecVal.setText("");
//        //        txtAppraiserId.setText("");
//        txtInter.setText("");
//        txtPenalInter.setText("");
//        observable.setInterest("");
//        observable.setPenalInterest("");
//        cboPurposeOfLoan.setSelectedItem("");
//        txtAccountNo.setText("");
//        lblAppraiserNameValue.setText("");
//        observableSecurity.setLblProdId_Disp("");
//        observableSecurity.setTotalSecurityValue("");
//        observableSecurity.setTxtMargin("");
//        observableSecurity.setTxtMarginAmt("");
//        observableSecurity.setTxtEligibleLoanAmt("");
//        observableSecurity.setTxtAppraiserId("");
//        nomineeUi.resetTable();
//        nomineeUi.resetNomineeData();
//        nomineeUi.getNomineeOB().ttNotifyObservers();
//        nomineeUi.disableNewButton(false);
//        //        btnAppraiserId.setEnabled(false);
//        if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
//            setModified(false);
//            alreadyChecked=true;
//            btncancelActionPerformed();
//            panChargeDetails.setVisible(false);
//        }
//        updateRecords = false;
    }
    private boolean checkForSecurityValue(){
        //        System.out.println("@@@@ tblSecurityTable.getRowCount() : "+tblSecurityTable.getRowCount());
        boolean canSave = true;
        boolean isLimitNotTallied = false;
        //        if (rdoSecurityDetails_Fully.isSelected() || loanType.equals("LTD")){//&& observable.getStrACNumber().length()
        //            String strFacilityType = getCboTypeOfFacilityKeyForSelected();
        //
        //            if (!strFacilityType.equals(LOANS_AGAINST_DEPOSITS)){
        //                isLimitNotTallied = observableSecurity.chkForSecValLessThanLimiVal(txtLimit_SD.getText());
        //            }else{
        //                isLimitNotTallied = observableSecurity.chkForSecValLessThanLimiVal(txtLimit_SD.getText(), tblSecurityTable.getModel(),false);
        //                isLimitNotTallied=false;//regarding mainlimit chanaging before authorization not need to check for ltd (goldloan this validation correct but ltd not needed )
        //            }
        //            if (isLimitNotTallied == true){
        //                displayAlert(resourceBundle.getString("securityValueWarning"));
        //                canSave = false;
        //            }else{
        //                canSave = true;
        //            }
        //        }else if(rdoSecurityDetails_Partly.isSelected()){
        //            isLimitNotTallied = observableSecurity.chkForSecValLessThanLimiVal(txtLimit_SD.getText(), tblSecurityTable.getModel(),true);
        //             if (isLimitNotTallied == true){
        //                displayAlert(resourceBundle.getString("securityValuePartalyWarning"));
        //                canSave = false;
        //             }else
        //                canSave = true;
        //        }
        return canSave;
    }
    
    private boolean checkFieldsWhenMainSavePressed(){
//        if (!((observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) || (viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT)))){
//            final String mandatoryMessage1 = new MandatoryCheck().checkMandatory(getClass().getName(), panBorrowProfile_CustName);
//            /* mandatoryMessage length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
//            String mandatoryMessage2 = "";
//            String mandatoryMessage3 = "";
//            String mandatoryMessage5 = "";
//            String mandatoryMessage6 = "";
//            String mandatoryMessage7 = isJointAcctHavingAtleastOneCust();
//            String mandatoryMessage8 = "";
//            if (observable.getStrACNumber().length() > 0){
//                //                mandatoryMessage3 = new MandatoryCheck().checkMandatory(getClass().getName(), panProd_IM);
//                //                mandatoryMessage5 = new MandatoryCheck().checkMandatory(getClass().getName(), panFDAccount);
//                //                mandatoryMessage6 = new MandatoryCheck().checkMandatory(getClass().getName(), panFDDate);
//                if (loanType.equals("OTHERS"))
//                    mandatoryMessage8 = isInterestDetailsExistForThisAcct();
//            }
//            //            if (rdoSecurityDetails_Fully.isSelected() && observableSecurity.chkForSecValLessThanLimiVal(txtLimit_SD.getText())){
//            //                mandatoryMessage2 = resourceBundle.getString("securityValueWarning");
//            //            }
//            final String mandatoryMessage4 = new MandatoryCheck().checkMandatory(getClass().getName(), panBorrowProfile_CustName);
//            /* mandatoryMessage4 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
//            if (loanType.equals("OTHERS"))
//                if (mandatoryMessage1.length() > 0 || mandatoryMessage2.length() > 0 || mandatoryMessage3.length() > 0 || mandatoryMessage4.length() > 0 || mandatoryMessage5.length() > 0 || mandatoryMessage6.length() > 0 || mandatoryMessage7.length() > 0 || mandatoryMessage8.length() > 0){
//                    displayAlert(mandatoryMessage1 + mandatoryMessage2 + mandatoryMessage3 + mandatoryMessage4 + mandatoryMessage5 + mandatoryMessage6+mandatoryMessage7+mandatoryMessage8);
//                    return false;
//                }
//        }
        return true;
    }
    
    private boolean isTablesInEditMode(boolean isMainSave){
//        if (observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])){
//            return true;
//        }
//        StringBuffer stbWarnMsg = new StringBuffer("");
//        //        if (authSignUI.isUpdateModeAuthorize()){
//        //            stbWarnMsg.append(resourceBundle.getString("authSignatoryEditWarning"));
//        //            stbWarnMsg.append("\n");
//        //        }
//        //        if (authSignUI.isUpdateModeAuthorizeInst()){
//        //            stbWarnMsg.append(resourceBundle.getString("authSignatoryInstEditWarning"));
//        //            stbWarnMsg.append("\n");
//        //        }
//        //        if (poaUI.isUpdateModePoA()){
//        //            stbWarnMsg.append(resourceBundle.getString("poaEditWarning"));
//        //            stbWarnMsg.append("\n");
//        //        }
//        if (updateRepayment){
//            stbWarnMsg.append(resourceBundle.getString("repaymentEditWarning"));
//            stbWarnMsg.append("\n");
//        }
//        //        if (isMainSave && updateSanctionFacility){
//        //            stbWarnMsg.append(resourceBundle.getString("santionFacilityEditWarning"));
//        //            stbWarnMsg.append("\n");
//        //        }
//        if (updateDocument){
//            stbWarnMsg.append(resourceBundle.getString("documentDetailsEditWarning"));
//            stbWarnMsg.append("\n");
//        }
//        if (updateInterest){
//            stbWarnMsg.append(resourceBundle.getString("interestDetailsEditWarning"));
//            stbWarnMsg.append("\n");
//        }
//        if (updateGuarantor){
//            stbWarnMsg.append(resourceBundle.getString("guarantorDetailsEditWarning"));
//            stbWarnMsg.append("\n");
//        }
//        if (updateSecurity){
//            stbWarnMsg.append(resourceBundle.getString("securityDetailsEditWarning"));
//            stbWarnMsg.append("\n");
//        }
//        if (stbWarnMsg.length() > 0){
//            displayAlert(stbWarnMsg.toString());
//            return false;
//        }else{
//            return true;
//        }
        return true;
    }
    private boolean repayTableNotNull(){
        //        String behaves=getCboTypeOfFacilityKeyForSelected();System.out.print("repaytable###"+behaves);
        //        if(behaves !=null && (!(behaves.equals("OD") || behaves.equals("CC"))))
        //            if ( tblRepaymentCTable.getRowCount() < 1 ){ //&&  tblRepaymentCTable.getRowCount() < 1
        //                String strWarning =  resourceBundle.getString("repayfreqtable");
        //                displayAlert(strWarning);
        //                return false;
        //            }
        return true;
    }
    private boolean allCTablesNotNull(){
        String strWarning = "";
        //        if (tblSanctionDetails2.getRowCount() < 1){
        //            strWarning = strWarning + resourceBundle.getString("existenceSancDetailsTableWarning");
        //        }
        //        if ( chkGurantor.isSelected() && tblGuarantorTable.getRowCount() < 1){//!observable.getStrACNumber().equals("") &&
        //            strWarning = strWarning + resourceBundle.getString("existenceGuarantorDetailsTableWarning")+"\n";
        //        }
        //check AUTHORIZED SIG details
        //        if ( chkAuthorizedSignatory.isSelected() && observable.getAuthorizeSigantoryRecord() < 1){//!observable.getStrACNumber().equals("") &&
        //            strWarning = strWarning + resourceBundle.getString("existenceAurhorizedSignatoryTableWarning")+"\n";
        //        }
        //        if(tblInterMaintenance.getRowCount()==0)
        //            strWarning = strWarning + resourceBundle.getString("existenceInterestTableWarning")+"\n";
        //check POA details
        //        if ( chkPOFAttorney.isSelected() && observable.getPOARecord() < 1){//!observable.getStrACNumber().equals("") &&
        //            strWarning = strWarning + resourceBundle.getString("existencePOATableWarning")+"\n";
        //        }
        //        if(chkAccountTransfer.isSelected() && actTransUI.checkmandatoryRemarks()) dont delete
        //             strWarning = strWarning + resourceBundle.getString("acctTransferdetails");
        //        strWarning = authSignUI.isHavingProperNoOfRecords(strWarning);
        if (!strWarning.equals("")){
            displayAlert(strWarning);
            strWarning = null;
            return false;
        }
        strWarning = null;
        return true;
    }
    private boolean productBasedValidation(){
      boolean isNotSelected = false;
//        String strFacilityType = getCboTypeOfFacilityKeyForSelected();
//        if (!(strFacilityType.equals("CC") || strFacilityType.equals("OD"))){
//            //            if (!(rdoMultiDisburseAllow_No.isSelected() || rdoMultiDisburseAllow_Yes.isSelected())){
//            //                isNotSelected = true;
//            //            }
//        }
        return isNotSelected;
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        if (!CommonUtil.convertObjToStr(TrueTransactMain.selBranch).equals("") && CommonUtil.convertObjToStr(TrueTransactMain.selBranch).length()>0 && 
        !TrueTransactMain.BRANCH_ID.equals(CommonUtil.convertObjToStr(TrueTransactMain.selBranch))) {
            ClientUtil.showMessageWindow("Interbranch Account creation not allowed for this screen...");
            TrueTransactMain.populateBranches();
            TrueTransactMain.selBranch = ProxyParameters.BRANCH_ID;
            observable.setSelectedBranchID(ProxyParameters.BRANCH_ID);
            setSelectedBranchID(ProxyParameters.BRANCH_ID);
            return;
        }else{
        setModified(true);
        transNew = true;
        multipleDeposit=true;
        btnnewActionPerformed();
        enableDisbleRepayDtInt(false);
        setAllBorrowerBtnsEnableDisable(true);
         newRecord=true;
         btnCustomer(true);
         enableDisableTable(true);
         txtNextAccNo.setEnabled(false);
        txtNextAccNo.setEditable(false);
        txtMobileNo.setEnabled(false);
        tdtMobileSubscribedFrom.setEnabled(false);
        chrgTableEnableDisable();//charge details
//        chkDepositUnlien.setSelected(false);
        eligblLoanAmt = 0.0;
        cboLoanProduct.requestFocus();
        }
    }//GEN-LAST:event_btnNewActionPerformed

    private void rdoMultiDisburseAllow_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoMultiDisburseAllow_YesActionPerformed
           rdoMultiDisburseAllow_YesActionPerformed();
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoMultiDisburseAllow_YesActionPerformed

    private void rdoMultiDisburseAllow_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoMultiDisburseAllow_NoActionPerformed
      if (rdoMultiDisburseAllow_No.isSelected()) {
            tabLimitAmount.remove(loanDisbursementUI);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoMultiDisburseAllow_NoActionPerformed

    private void cboDepositProductFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboDepositProductFocusLost
        if (cboDepositProduct.getSelectedIndex() > 0) {
            String depositProduct_ID = (String) observable.getCbmDepositProduct().getKeyForSelected();
            HashMap whereMap = new HashMap();
            whereMap.put("PROD_ID", depositProduct_ID);
            List lstBehaves = ClientUtil.executeQuery("getBehavesL", whereMap);
            if (lstBehaves != null && lstBehaves.size() > 0) {
                whereMap = (HashMap) lstBehaves.get(0);
                if (whereMap != null && whereMap.containsKey("BEHAVES_LIKE") && CommonUtil.convertObjToStr(whereMap.get("BEHAVES_LIKE")).equals("THRIFT")) {
                    setThriftEnabled(true);
                    observable.setDepoBehavesLike( CommonUtil.convertObjToStr(whereMap.get("BEHAVES_LIKE")));
                } else {
                    setThriftEnabled(false);
                    observable.setDepoBehavesLike( CommonUtil.convertObjToStr(whereMap.get("BEHAVES_LIKE")));
                }
            }
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_cboDepositProductFocusLost

    private void txtNoOfInstallActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNoOfInstallActionPerformed
    renewalFocus();
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoOfInstallActionPerformed

    private void txtNoOfInstallFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNoOfInstallFocusLost
           java.util.GregorianCalendar gCalendar = new java.util.GregorianCalendar();
        java.util.GregorianCalendar gCalendarrepaydt = new java.util.GregorianCalendar(); //forrepaydate shoude change from first dt
        if (CommonUtil.convertObjToStr(tdtRepaymentDt.getDateValue()).equals("")) {
            tdtRepaymentDt.setDateValue(tdtAccountOpenDate.getDateValue());
        }
        gCalendar.setGregorianChange(DateUtil.getDateMMDDYYYY(tdtRepaymentDt.getDateValue()));
        gCalendar.setTime(DateUtil.getDateMMDDYYYY(tdtRepaymentDt.getDateValue()));
        gCalendarrepaydt.setGregorianChange(DateUtil.getDateMMDDYYYY(tdtRepaymentDt.getDateValue()));
        gCalendarrepaydt.setTime(DateUtil.getDateMMDDYYYY(tdtRepaymentDt.getDateValue()));
        int dateVal = 30;
        int incVal = observable.getInstallNo(txtNoOfInstall.getText(), dateVal);
        date = new java.util.Date();
        date = DateUtil.getDateMMDDYYYY(tdtRepaymentDt.getDateValue());
        if (txtNoOfInstall.getText().equals("1")) {
            date = DateUtil.getDateMMDDYYYY(tdtAccountOpenDate.getDateValue());
        }
        //System.out.println("Date##" + date);
        if (dateVal <= 7) {
            gCalendar.add(gCalendar.DATE, incVal);
        } else if (dateVal >= 30) {
            gCalendar.add(gCalendar.MONTH, incVal);
            int firstInstall = dateVal / 30;
            gCalendarrepaydt.add(gCalendarrepaydt.MONTH, firstInstall);//for repaydate
        }
        tdtToDate.setDateValue(DateUtil.getStringDate(gCalendar.getTime()));

        observable.setToDate(tdtToDate.getDateValue());
        if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")) {
            Installmentchk();
            HashMap whereMap = new HashMap();
            if ((observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) || (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT)) {
                tdtAccountOpenDate.setEnabled(false);
                tdtRepaymentDt.setEnabled(true);
            }
                    List recoveryParameterList = ClientUtil.executeQuery("getRecoveryParameters", whereMap);
                    if (recoveryParameterList != null & recoveryParameterList.size() > 0) {
                        whereMap = (HashMap) recoveryParameterList.get(0);
                        int firstDay = 0;
                        int sanctionDay = 0;
                        int installmentStartDay = 0;
                        Date sancDt = DateUtil.getDateMMDDYYYY(tdtAccountOpenDate.getDateValue());
                        Date instDate = DateUtil.getDateMMDDYYYY(tdtRepaymentDt.getDateValue());
                        firstDay = CommonUtil.convertObjToInt(whereMap.get("FIRST_DAY"));
                        sanctionDay = sancDt.getDate();
                        installmentStartDay = instDate.getDate();
                        //System.out.println("###### firstDay : " + firstDay);
                        //System.out.println("###### sanctionDay : " + sanctionDay);
                        GregorianCalendar cal = new GregorianCalendar((sancDt.getYear() + 1900), sancDt.getMonth(), sancDt.getDate());
                        GregorianCalendar instCal = new GregorianCalendar((instDate.getYear() + 1900), instDate.getMonth(), instDate.getDate());
                        int lastDayOfMonth = cal.getActualMaximum(cal.DAY_OF_MONTH);
                        int lastDayOfInstMonth = instCal.getActualMaximum(instCal.DAY_OF_MONTH);
                        Date dt = cal.getTime();
                        Date insDt = instCal.getTime();
                      //  if(chkMoratorium_Given.isSelected()== false){
                            if (sanctionDay <= firstDay) {
                                cal.set(dt.getYear() + 1900, dt.getMonth(), lastDayOfMonth);
                                dt = cal.getTime();
                                //System.out.println("###### Current Month gCalendarrepaydt : " + dt);
                            } else {
                                cal.set(dt.getYear() + 1900, dt.getMonth() + 1, dt.getDate());
                                lastDayOfMonth = cal.getActualMaximum(cal.DAY_OF_MONTH);
                                dt = cal.getTime();
                                dt.setDate(lastDayOfMonth);
                                //System.out.println("###### Next Month gCalendarrepaydt : " + dt);
                            }
                            tdtRepaymentDt.setDateValue(DateUtil.getStringDate(dt));
                            gCalendarrepaydt.set(dt.getYear() + 1900, dt.getMonth(), dt.getDate());
                            observableRepay.setTdtFirstInstall(tdtRepaymentDt.getDateValue());
                            tdtRepaymentDt.setDateValue(tdtRepaymentDt.getDateValue());
                            repaymentDate = (Date) dt;
                            //To Date
                            gCalendar = new java.util.GregorianCalendar();
                            gCalendar.setGregorianChange(DateUtil.getDateMMDDYYYY(tdtRepaymentDt.getDateValue()));
                            gCalendar.setTime(DateUtil.getDateMMDDYYYY(tdtRepaymentDt.getDateValue()));
                            dateVal = 30;
                            incVal = observable.getInstallNo(String.valueOf(CommonUtil.convertObjToDouble(txtNoOfInstall.getText()).doubleValue() - 1), dateVal);
                            date = new java.util.Date();
                            date = DateUtil.getDateMMDDYYYY(tdtRepaymentDt.getDateValue());
                            if (txtNoOfInstall.getText().equals("1")) {
                                date = DateUtil.getDateMMDDYYYY(tdtAccountOpenDate.getDateValue());
                            }
                            if (dateVal <= 7) {
                                gCalendar.add(gCalendar.DATE, incVal);
                            } else if (dateVal >= 30) {
                                gCalendar.add(gCalendar.MONTH, incVal);
                            }
                            tdtToDate.setDateValue(DateUtil.getStringDate(gCalendar.getTime()));
                            Date tdtFacDt = DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue());
                            if (tdtFacDt.getDate() > 10) {
                                GregorianCalendar gc = new GregorianCalendar();
                                gc.set(tdtFacDt.getYear() + 1900, tdtFacDt.getMonth(), 1);
                                java.util.Date monthStartDate = new java.util.Date(gc.getTime().getTime());
                                System.out.println("monthStartDate :" + monthStartDate);
                               Calendar calendar = Calendar.getInstance();
                            calendar.setTime(monthStartDate);
                            calendar.add(calendar.MONTH, 1);
                            calendar.add(calendar.DAY_OF_MONTH, -1);
                            java.util.Date monthEndDate = new java.util.Date(calendar.getTime().getTime());
                            tdtToDate.setDateValue(DateUtil.getStringDate(monthEndDate));
                            System.out.println("monthEndDate :" + monthEndDate);
                        }

                        observable.setToDate(tdtToDate.getDateValue());
                        //   }
//                        HashMap retireMap = new HashMap();
//                        retireMap.put("CUSTID", lblCustomerIdValue.getText());
//                        List casteList = ClientUtil.executeQuery("getSelectCasteForLoanAppl", retireMap);
//                        System.out.println("casteList??>>" + casteList);
//                        if (casteList != null && casteList.size() > 0) {
//                            retireMap = (HashMap) casteList.get(0);
//                            String caste = CommonUtil.convertObjToStr(retireMap.get("CASTE"));
//                            String DOB = CommonUtil.convertObjToStr(retireMap.get("DOB"));
//                            String retireDate = CommonUtil.convertObjToStr(retireMap.get("RETIREMENT_DT"));
//                            if (retireDate.isEmpty() || retireDate.equals("")) {
//                                ClientUtil.showMessageWindow("RetireMentDate Not Specified In Customer ");
//                                return;
//                            } else {
//                                Date retire = new Date();
//                                retire = DateUtil.getDateMMDDYYYY(retireDate);
//                                Date todate = new Date();
//                                todate = DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue());
//                                if (todate.compareTo(retire) > 0) {
//                                    Installmentchk();
////                                    ClientUtil.showMessageWindow("InstllMent End Date Exceeds The RetireMent Date . Should Be equal Or less");
////                                    tdtToDate.setDateValue(null);
////                                    tdtRepaymentDt.setDateValue(null);
////                                    txtNoOfInstall.setText(null);
////                                    return;
//                                }
//                            }
//                        }
                    } else {
                        ClientUtil.showMessageWindow("Pls Enter Recovery Parameter Details");
                        return;
                    }
                } 
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoOfInstallFocusLost
     private void Installmentchk() {
        try {
            int noInstallments = 0;
            int noOfInstalmnts;
            if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")) {
                 String noInstall = txtNoOfInstall.getText();
                String instAmount = txtInstallAmount.getText();
                if (instAmount != null && instAmount.length() > 0 && noInstall != null && noInstall.length() > 0) {
                    double eligble = CommonUtil.convertObjToDouble(txtLoanAmt.getText());
                    double totInstAmt = CommonUtil.convertObjToDouble(noInstall) * CommonUtil.convertObjToDouble(instAmount);
                    if (totInstAmt > eligble) {
                        double noInst = eligble / CommonUtil.convertObjToDouble(instAmount);
                        double noOfInstalment = Math.floor(noInst);
                        ClientUtil.showMessageWindow("Total Installment Amount Cannot Exceed Eligible Amount .Max Eligible Monthd: " + noOfInstalment);
                        txtNoOfInstall.setText(CommonUtil.convertObjToStr(noOfInstalment));
                    }
                }
                LinkedHashMap arList = new LinkedHashMap();
                HashMap whereMap = new HashMap();
                whereMap.put("CUST_ID", lblCustomerIdValue.getText());
                int serPeriod = 0;
                String prdid = CommonUtil.convertObjToStr(observable.getCbmLoanProduct().getKeyForSelected());
                double amt = Double.parseDouble(txtLoanAmt.getText());
                HashMap prodMap = new HashMap();
                prodMap.put("PROD_ID", prdid);
                prodMap.put("AMOUNT", amt);
                int ser_period = 0;
                int surety = 0;
                List prodList = ClientUtil.executeQuery("getServicewiseLoanDetail", prodMap);
                if (prodList != null && prodList.size() > 0) {
                    for (int i = 0; i < prodList.size(); i++) {
                        HashMap servicewiseMap = (HashMap) prodList.get(i);
                        ser_period = CommonUtil.convertObjToInt(servicewiseMap.get("PAST_SERVICE_PERIOD"));
                        surety = CommonUtil.convertObjToInt(servicewiseMap.get("NO_OF_SURETIES_REQUIRED"));
                    }
                }
                List serviceList = ClientUtil.executeQuery("getFutureServicePeriod", whereMap);
                if (serviceList != null && serviceList.size() > 0) {
                    HashMap hMap = (HashMap) serviceList.get(0);
                    if (hMap != null && hMap.size() > 0 && hMap.containsKey("SERVICE")) {
                        String ser = CommonUtil.convertObjToStr(hMap.get("SERVICE"));
                        String[] arr = ser.split("  ");
                        System.out.println("arr.length :"+arr.length);
                        if (arr.length > 0) {
                            String yr = arr[0].substring(0, arr[0].indexOf("Y"));
                            serPeriod = serPeriod + (CommonUtil.convertObjToInt(yr) * 12);
                        }
                        if (arr.length > 1) {
                            String mnth = arr[1].substring(0, arr[1].indexOf("M"));
                            serPeriod = serPeriod + CommonUtil.convertObjToInt(mnth);
                        }
                    }
                }
                serPeriod = serPeriod - ser_period;
                arList.put(lblCustomerIdValue.getText(), serPeriod);
                LinkedHashMap map = (LinkedHashMap) sortByValues(arList);
                if (map != null && map.size() > 0) {
                    Set set = map.entrySet();
                    Iterator iterator = set.iterator();
                    int chkPeriod = 0;
                    while (iterator.hasNext()) {
                        Map.Entry me = (Map.Entry) iterator.next();
                        System.out.print(me.getKey() + ": ");
                        System.out.println(me.getValue());
                        chkPeriod = CommonUtil.convertObjToInt(me.getValue());
                        break;
                    }
                    int instNo = CommonUtil.convertObjToInt(txtNoOfInstall.getText());
                    if (chkPeriod > 0 && instNo > chkPeriod) {
                        ClientUtil.showMessageWindow("InstallmentNo Cannot be exceed more than" + chkPeriod);
                        txtNoOfInstall.setText(CommonUtil.convertObjToStr(chkPeriod));
                        //   return;
                    }
                }
               
            }
          } catch (Exception e) {
            e.printStackTrace();
        }
    }
         private static HashMap sortByValues(HashMap map) { 
       List list = new LinkedList(map.entrySet());
       // Defined Custom Comparator here
       Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
               return ((Comparable) ((Map.Entry) (o1)).getValue())
                  .compareTo(((Map.Entry) (o2)).getValue());
            }
       });

       // Here I am copying the sorted list in HashMap
       // using LinkedHashMap to preserve the insertion order
       HashMap sortedHashMap = new LinkedHashMap();
       for (Iterator it = list.iterator(); it.hasNext();) {
              Map.Entry entry = (Map.Entry) it.next();
              sortedHashMap.put(entry.getKey(), entry.getValue());
       } 
       return sortedHashMap;
  }
    private void txtMobileNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMobileNoFocusLost
        // TODO add your handling code here:
        tdtMobileSubscribedFrom.setDateValue(CommonUtil.convertObjToStr(currDt.clone()));
    }//GEN-LAST:event_txtMobileNoFocusLost
    private void renewalFocus() {
        //added by rishad for closing Existing loan of  this customer --purpose that is closing exing one sanctioing new one
        if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")) {
            String AcctNum = "";
            HashMap resultMap = new HashMap();
            prodDesc = CommonUtil.convertObjToStr(cboLoanProduct.getModel().getSelectedItem());
            HashMap whereMap = new HashMap();
            whereMap.put("CUSTID", lblCustomerIdValue.getText());
            whereMap.put("PROD_DESC", prodDesc);
            whereMap.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            List acctList = ClientUtil.executeQuery("getLoanAcctNum", whereMap);
            if (acctList != null && acctList.size() > 0) {
                resultMap = (HashMap) acctList.get(0);
                if (resultMap != null && resultMap.containsKey("ACCT_NUM")) {
                    AcctNum = CommonUtil.convertObjToStr(resultMap.get("ACCT_NUM"));
                }
            }
            if (AcctNum != null && AcctNum.length() > 0) {
                //  int message = ClientUtil.confirmationAlert("Do You Want to Close the Existing Loan A/c?"
                // if (message == 0) {
                HashMap whereSus = new HashMap();
                //System.out.println("");
                whereSus.put("PROD_ID", observable.getCbmLoanProduct().getKeyForSelected());
                whereSus.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
                List acctListSus = ClientUtil.executeQuery("getLoanAdjSA", whereSus);
                if (acctListSus != null && acctListSus.size() > 0) {
                    resultMap = (HashMap) acctListSus.get(0);
                    if (resultMap != null && resultMap.containsKey("ACCT_NUM")) {
                        suspenseActNum = CommonUtil.convertObjToStr(resultMap.get("ACCT_NUM"));
                    }
                }
                HashMap hash = new HashMap();
                CInternalFrame frm = new com.see.truetransact.ui.operativeaccount.AccountClosingUI("TermLoan");
                frm.setSelectedBranchID(getSelectedBranchID());
                TrueTransactMain.showScreen(frm);
                loanRenewal = true;
                hash.put("FROM_TRANSACTION_SCREEN", "FROM_TRANSACTION_SCREEN");
                hash.put("ACCOUNT NUMBER", AcctNum);
                //System.out.println("prod id"+CommonUtil.convertObjToStr(cboProductId.getSelectedItem()));
                hash.put("PROD_ID", prodDesc);
                hash.put("DEBIT_NUMBER", suspenseActNum);
                hash.put("PROD_TYPE", "SA");
                hash.put("DEPOSIT_LOAN_RENEWAL", "DEPOSIT_LOAN_RENEWAL");
                frm.fillData(hash);
                loannaration = "Old Loan AcctNo:" + AcctNum + "Closing Amount:";
            }
        }

    }  
    private void setThriftEnabled(boolean  flag)
   {
   tdtToDate.setVisible(flag);
   tdtToDate.setEnabled(false);
   txtNoOfInstall.setVisible(flag);
   lblNoOfInstall.setVisible(flag);
   lblTodate.setVisible(flag);
   lblMultiDisburseAllow.setVisible(flag);
   rdoMultiDisburseAllow_Yes.setVisible(flag);
   rdoMultiDisburseAllow_No.setVisible(flag);
   }
     private void rdoMultiDisburseAllow_YesActionPerformed() {
        if (rdoMultiDisburseAllow_Yes.isSelected()) {
            //              tabLimitAmount.add("Disbursement Schedule",loanDisbursementUI);
            if (!(viewType.equals("Edit") || viewType.equals(AUTHORIZE))) {
                loanDisbursementUI.enableDisableSubLimitNewButton(false);
            }
            if (enableControls == true) {
                loanDisbursementUI.enableDisableSubLimitNewButton(false);
            }
        } else {
            tabLimitAmount.remove(loanDisbursementUI);
            //            observable.setChkStockInspect(false);
        }
    }
    private void btnnewActionPerformed(){
        btnNewPressed = true;
        setFocusFirstTab();
        observable.destroyObjects();
        
        observable.createObject();
//        observable.resetFields();
        observable.resetForm();
        
//        observable.resetAllFacilityDetails();
        
//        updateCboTypeOfFacility();
        ClientUtil.enableDisable(this, true);// Enables the panel...
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.setStatus();
        observable.setTdtAccountOpenDate(DateUtil.getStringDate(curr_dt));
        tdtAccountOpenDate.setDateValue(DateUtil.getStringDate(curr_dt));
        rdoAdditionalLoanFacility_No.setSelected(true);
        ClientUtil.enableDisable(panShareDetails, false);
        
        
//        observableSecurity.setActionType(ClientConstants.ACTIONTYPE_NEW);
//        observableBorrow.setLoanActionType(ClientConstants.ACTIONTYPE_NEW);
//        observable.setStatus();
        setButtonEnableDisable();
        btnCustomer(true);
      
//        setAllTablesEnableDisable(true);
//        setbtnCustEnableDisable(false);
//        btnCustID.setEnabled(true);
//        newPressedEnableDisable(true);
//        setCompanyDetailsEnableDisable(false);
       
       
        
       
       
//        ClientUtil.enableDisable(panSecurityDetails_security,false);
//    
//        cboConstitution.setSelectedItem(observableBorrow.getCbmConstitution().getDataForKey("INDIVIDUAL"));
//        cboCategory.setSelectedItem(observableBorrow.getCbmCategory().getDataForKey("GENERAL_CATEGORY"));
//        cboSanctioningAuthority.setSelectedItem(observable.getCbmSanctioningAuthority().getDataForKey("BRANCH_MANAGER"));
//       
//      
//        cboAppraiserId.setSelectedItem(CommonUtil.convertObjToStr(com.see.truetransact.ui.TrueTransactMain.USERINFO.get("EMPLOYEE_ID")));
//        //        btnAppraiserId.setEnabled(true);
//        cboAppraiserId.setEnabled(true);
//        txtAreaParticular.setEnabled(true);
//        HashMap marginMap = new HashMap();
//        LinkedHashMap whereMap = new LinkedHashMap();
//        marginMap.put("PROD_ID",eachProdId);
//        whereMap.put("PROD_ID",eachProdId);
//        List lst = ClientUtil.executeQuery("getSelectMarginPercentage", marginMap);
//        cboRepayFreq.setSelectedIndex(0);
//        if(lst!=null && lst.size()>0){
//            marginMap = (HashMap)lst.get(0);
//            txtMargin.setText(CommonUtil.convertObjToStr(marginMap.get("MARGIN")));
//            ((ComboBoxModel)cboRepayFreq.getModel()).setKeyForSelected(CommonUtil.convertObjToStr(marginMap.get("MAX_PERIOD")));
//            if (cboRepayFreq.getSelectedIndex()==0) {
//                cboRepayFreq.setSelectedItem("Yearly");
//            }
//        }
//        List prodDesclst = ClientUtil.executeQuery("TermLoan.getProdId", whereMap);
//        if(prodDesclst!=null && prodDesclst.size()>0){
//            whereMap = (LinkedHashMap)prodDesclst.get(0);
//            prodDesc = CommonUtil.convertObjToStr(whereMap.get("PROD_DESC"));
//        }
        
        createChargeTable();
      
        
        observable.ttNotifyObservers();
    }
    
    private void enableDisbleRepayDtInt(boolean flag){
        tdtRepaymentDt.setEnabled(flag);
        txtInter.setEnabled(flag);
    }
    private void createChargeTable() {
//        HashMap tableMap = buildData(prodDesc);
//        ArrayList dataList = new ArrayList();
//        dataList = (ArrayList)tableMap.get("DATA");
//        if(dataList!= null && dataList.size()>0){
//            tableFlag = true;
//            ArrayList headers;
//            panChargeDetails.setVisible(true);
//            SimpleTableModel  stm = new SimpleTableModel((ArrayList)tableMap.get("DATA"),(ArrayList)tableMap.get("HEAD"));
//            table = new JTable(stm);
//            table.setSize(430, 110);
//            srpChargeDetails = new javax.swing.JScrollPane(table);
//            srpChargeDetails.setMinimumSize(new java.awt.Dimension(430, 110));
//            srpChargeDetails.setPreferredSize(new java.awt.Dimension(430, 110));
//            panChargeDetails.add(srpChargeDetails, new java.awt.GridBagConstraints());
//            table.revalidate();
//        }else{
//            tableFlag = false;
//            chrgTableEnableDisable();
//        }
    }
    
//    private void chrgTableEnableDisable(){
////        tableFlag = false;
////        panChargeDetails.removeAll();
////        panChargeDetails.setVisible(false);
//    }
    
//    private HashMap buildData(String prodDesc){
////        HashMap whereMap = new HashMap();
////        whereMap.put("SCHEME_ID",prodDesc);
////        whereMap.put("DEDUCTION_ACCU","O");
////        List list = ClientUtil.executeQuery("getChargeDetailsData",whereMap);
////        boolean _isAvailable = list.size() > 0 ? true : false;
////        ArrayList _heading = null;
////        ArrayList data = new ArrayList();
////        ArrayList colData = new ArrayList();
//        HashMap map;
////        Iterator iterator = null;
////        if (_isAvailable) {
////            map = (HashMap) list.get(0);
////            iterator = map.keySet().iterator();
////        }
////        if (_isAvailable && _heading == null) {
////            _heading = new ArrayList();
////            _heading.add("Select");
////            while (iterator.hasNext()) {
////                _heading.add((String) iterator.next());
////            }
////        }
////        
////        String cellData="", keyData="";
////        Object obj = null;
////        for (int i=0, j=list.size(); i < j; i++) {
////            map = (HashMap) list.get(i);
////            colData = new ArrayList();
////            iterator = map.values().iterator();
////            if (CommonUtil.convertObjToStr(map.get("M")).equals("Y")) {
////                colData.add(new Boolean(true));
////            } else {
////                colData.add(new Boolean(false));
////            }
////            while (iterator.hasNext()) {
////                obj = iterator.next();
////                //                if (obj != null) {
////                colData.add(CommonUtil.convertObjToStr(obj));
////                //                } else {
////                //                    colData.add("");
////                //                }
////            }
////            data.add(colData);
////        }
//       map = new HashMap();
////        map.put("HEAD", _heading);
////        map.put("DATA", data);
//        return map;
//    }
    
    private void editChargeTable() {
//        HashMap tableMap = editBuildData(prodDesc);
//        ArrayList dataList = new ArrayList();
//        dataList = (ArrayList)tableMap.get("DATA");
//        if(dataList!= null && dataList.size()>0){
//            tableFlag = true;
//            ArrayList headers;
//            panChargeDetails.setVisible(true);
//            SimpleTableModel  stm = new SimpleTableModel((ArrayList)tableMap.get("DATA"),(ArrayList)tableMap.get("HEAD"));
//            table = new JTable(stm);
//            table.setSize(430, 110);
//            srpChargeDetails = new javax.swing.JScrollPane(table);
//            srpChargeDetails.setMinimumSize(new java.awt.Dimension(430, 110));
//            srpChargeDetails.setPreferredSize(new java.awt.Dimension(430, 110));
//            panChargeDetails.add(srpChargeDetails, new java.awt.GridBagConstraints());
//            table.revalidate();
//        }else{
//            tableFlag = false;
//            chrgTableEnableDisable();
//        }
    }
    
    private HashMap editBuildData(String prodDesc){
        HashMap whereMap = new HashMap();
        whereMap.put("SCHEME_ID",prodDesc);
        whereMap.put("DEDUCTION_ACCU","O");
        List list = ClientUtil.executeQuery("getChargeDetailsData",whereMap);
        boolean _isAvailable = list.size() > 0 ? true : false;
        ArrayList _heading = null;
        ArrayList data = new ArrayList();
        ArrayList colData = new ArrayList();
        HashMap map;
        Iterator iterator = null;
        if (_isAvailable) {
            map = (HashMap) list.get(0);
            iterator = map.keySet().iterator();
        }
        if (_isAvailable && _heading == null) {
            _heading = new ArrayList();
            while (iterator.hasNext()) {
                _heading.add((String) iterator.next());
            }
        }
        
        String cellData="", keyData="";
        Object obj = null;
        for (int i=0, j=list.size(); i < j; i++) {
            map = (HashMap) list.get(i);
            colData = new ArrayList();
            iterator = map.values().iterator();
            while (iterator.hasNext()) {
                obj = iterator.next();
                colData.add(CommonUtil.convertObjToStr(obj));
            }
            data.add(colData);
        }
        map = new HashMap();
        map.put("HEAD", _heading);
        map.put("DATA", data);
        return map;
    }
    
    
    public class SimpleTableModel extends AbstractTableModel {
        
        private ArrayList dataVector;
        private ArrayList headingVector;
        
        public SimpleTableModel(ArrayList dataVector, ArrayList headingVector){
            this.dataVector = dataVector;
            this.headingVector = headingVector;
        }
        public int getColumnCount() {
            return headingVector.size();
        }
        
        public int getRowCount() {
            return dataVector.size();
        }
        
        public Object getValueAt(int row, int col) {
            ArrayList rowVector = (ArrayList)dataVector.get(row);
            return rowVector.get(col);
        }
        
        public String getColumnName(int column) {
            return headingVector.get(column).toString();
        }
        
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
        
        public boolean isCellEditable(int row, int col) {
            if (col == 0 && (CommonUtil.convertObjToStr(getValueAt(row,col+3)).equals("Y"))){
                return false;
            }else{
                if (col != 0){
                    return false;
                }else{
                    return true;
                }
            }
        }
        
        public void setValueAt(Object aValue, int row, int col) {
            ArrayList rowVector = (ArrayList)dataVector.get(row);
            rowVector.set(col, aValue);
        }
    }
    
    
    
    // To display the All the Cust Id's which r having status as
    // created or updated, in a table...
    private void popUp(String field) {
        
        
        final HashMap viewMap = new HashMap();
        viewType = field;
        //if ( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||  observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
        if(field.equals("Edit") || field.equals("Delete") || field.equals("Enquirystatus") ) {
            ArrayList lst = new ArrayList();
            lst.add("BORROWER NO");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            
            lblStatus.setText(ClientConstants.ACTION_STATUS[0]);
            HashMap editMapCondition = new HashMap();
            editMapCondition.put("BRANCH_ID", getSelectedBranchID());
            //            editMapCondition.put("PROD_ID", eachProdId);
            editMapCondition.put("AUTHORIZE_REMARK", "IS NULL");
            
            if(field.equals("Edit") || field.equals("Delete"))
                editMapCondition.put("ACCT_STATUS", "ACCT_STATUS");
            
            viewMap.put(CommonConstants.MAP_WHERE, editMapCondition);
//            if (loanType.equals("LTD"))
                viewMap.put(CommonConstants.MAP_NAME, "viewTermLoanForLTD");
//            else{
//                editMapCondition.put("PROD_ID", eachProdId);
//                viewMap.put(CommonConstants.MAP_NAME, "viewTermLoan");
//            }
            if(field.equals("Delete")){
//                if (loanType.equals("LTD")){
                    viewMap.put(CommonConstants.MAP_NAME, "viewTermLoanForDeleteLTD");
//                }else{
//                    viewMap.put(CommonConstants.MAP_NAME, "viewTermLoanForDelete");
//                }
            } //mapped statement: viewTermLoan---> result map should be a Hashmap in OB...
        }else if(field.equals("Borr_Cust_Id")){
            viewMap.put(CommonConstants.MAP_NAME, "getCustomers");
        }else if(field.equals("Guarant_Cust_Id")){
            viewMap.put(CommonConstants.MAP_NAME, "getCustomers");
        }else if(field.equals("GUARANTOR_ACCT_NO")){
            //            String strSelectedProdType = CommonUtil.convertObjToStr(((ComboBoxModel)cboProdType.getModel()).getKeyForSelected());
            //            String strSelectedProdID = CommonUtil.convertObjToStr(((ComboBoxModel)cboProdId.getModel()).getKeyForSelected());
            //            if (strSelectedProdType.length() <= 0 || strSelectedProdID.length() <= 0){
            //                //If the Product is not selected then return.
            //                return;
            //            }
            updateOBFields();
            //            viewMap.put(CommonConstants.MAP_NAME, "Cash.getGuarantorAccountList"+strSelectedProdType);
            //            HashMap whereListMap = new HashMap();
            //            whereListMap.put("CUST_ID", txtCustomerID_GD.getText());
            //            whereListMap.put("PROD_ID", strSelectedProdID);
            //            viewMap.put(CommonConstants.MAP_WHERE, whereListMap);
        }
        new ViewAll(this, viewMap).show();
    }
    
    private void setFocusFirstTab(){
        tabLimitAmount.setSelectedIndex(0);
    }
    private void setFocusAcctLevelTab(){
        tabLimitAmount.setSelectedIndex(4);
    }
    
    
     private long depositSanctionRoundOff(double limit){
        Rounding re=new Rounding();
        long roundOffValue=0;
        //        CommonUtil.convertObjToLong(
        long longLimit=(long)limit;
        if(observable.getSanctionAmtRoundOff().length()>0){
            String roundOff=observable.getSanctionAmtRoundOff();
            if(roundOff.length()>0){
                if(roundOff .equals("NEAREST_TENS")){
                    roundOffValue=10;
                }
                if(roundOff .equals("NEAREST_HUNDREDS")){
                    roundOffValue=100;
                }
                if(roundOff .equals("NEAREST_VALUE")){
                    roundOffValue=1;
                }
            }
            //         long lienAmt=(long)(enterAmt/eligibleMargin);
            Rounding rd=new Rounding();
            /* lien marked next higher but limit marked lower  */
            if(!roundOff .equals("NO_ROUND_OFF"))
                longLimit= rd.lower(longLimit,roundOffValue);
        }
        return longLimit;
    }

    
    public long getNearest(long number,long roundingFactor)  {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor%2) != 0)
            roundingFactorOdd +=1;
        long mod = number%roundingFactor;
        if ((mod < (roundingFactor/2)) || (mod < (roundingFactorOdd/2)))
            return lower(number,roundingFactor);
        else
            return higher(number,roundingFactor);
    }
    
    public long lower(long number,long roundingFactor) {
        long mod = number%roundingFactor;
        return number-mod;
    }
    
    public long higher(long number,long roundingFactor) {
        long mod = number%roundingFactor;
        if ( mod == 0)
            return number;
        return (number-mod) + roundingFactor ;
    }
    
//    private long depositSanctionRoundOff(double limit){
//       Rounding re=new Rounding();
////        long roundOffValue=0;
////        //        CommonUtil.convertObjToLong(
//        long longLimit=(long)limit;
////        if(observable.getSanctionAmtRoundOff().length()>0){
////            String roundOff=observable.getSanctionAmtRoundOff();
////            if(roundOff.length()>0){
////                if(roundOff .equals("NEAREST_TENS")){
////                    roundOffValue=10;
////                }
////                if(roundOff .equals("NEAREST_HUNDREDS")){
////                    roundOffValue=100;
////                }
////                if(roundOff .equals("NEAREST_VALUE")){
////                    roundOffValue=1;
////                }
////            }
//            //         long lienAmt=(long)(enterAmt/eligibleMargin);
//            Rounding rd=new Rounding();
//            /* lien marked next higher but limit marked lower  */
////            if(!roundOff .equals("NO_ROUND_OFF"))
////                longLimit= rd.lower(longLimit,roundOffValue);
////        }
//        return longLimit;
//    }
    private void updateGuarantorTabCustDetails(){
        //        txtCustomerID_GD.setText(observableGuarantor.getTxtCustomerID_GD());
        //        txtGuaranName.setText(observableGuarantor.getTxtGuaranName());
        //        tdtAsOn_GD.setDateValue(observableGuarantor.getTdtAsOn_GD());
        //        txtArea_GD.setText(observableGuarantor.getTxtArea_GD());
        //        txtStreet_GD.setText(observableGuarantor.getTxtStreet_GD());
        //        txtPin_GD.setText(observableGuarantor.getTxtPin_GD());
        //        cboCity_GD.setSelectedItem(observableGuarantor.getCboCity_GD());
        //        cboState_GD.setSelectedItem(observableGuarantor.getCboState_GD());
        //        cboCountry_GD.setSelectedItem(observableGuarantor.getCboCountry_GD());
        //        txtPhone_GD.setText(observableGuarantor.getTxtPhone_GD());
        //        txtGuarantorNetWorth.setText(observableGuarantor.getTxtGuarantorNetWorth());
        //        tdtDOB_GD.setDateValue(observableGuarantor.getTdtDOB_GD());
    }
    
    private void custInfoDisplay(String Cust_ID, String loanType){
        HashMap hash = new HashMap();
        hash.put("CUST_ID",Cust_ID);
//        //        hash.put("CUSTOMER ID",Cust_ID);
//        // Remove the old Main CUSTOMER ID
//        //        authSignUI.removeAcctLevelCustomer(observableBorrow.getTxtCustID());
//        if (observableBorrow.populateBorrowerTabCustomerDetails(hash,false,loanType)){
//            //            setCompanyDetailsEnableDisable(true);
//            //            authSignUI.addAcctLevelCustomer(observableBorrow.getTxtCustID());
//            updateBorrowerTabCustDetails();
//            txtCustID.setText(observableBorrow.getTxtCustID());
//            nomineeUi.setMainCustomerId(txtCustID.getText());
//            tblBorrowerTabCTable.setModel(observableBorrow.getTblBorrower());
//            System.out.println("inside custinfo@@@@@@@@");
//            observableBorrow.populateBorrowerTabCustFields(hash, CommonUtil.convertObjToStr(tblBorrowerTabCTable.getValueAt(0, 2)));
//            updateBorrowerTabCustFields();
//            hash = null;
//        }else{
//            txtCustID.setText(observableBorrow.getTxtCustID());
//            nomineeUi.setMainCustomerId(txtCustID.getText());
//            //            setCompanyDetailsEnableDisable(false);
//        }
//        HashMap newMap = new HashMap();
//        newMap.put("PROD_ID",lblDocumentProdIdValue.getText());
//        List list = (List) ClientUtil.executeQuery("getSelectAcctLevelDocDetails", newMap);
//        newMap.put("TermLoanDocumentTO", list);
//        list = null;
//        //        observableDocument = new TermLoanDocumentDetailsOB();
//        observableDocument.resetDocumentDetails();
//        //        observableDocument.getTblDocumentTab().setDataArrayList(null, termLoanDocumentDetailsOB.getDocumentTabTitle());
//        observableDocument.setTermLoanDocumentTO((ArrayList) (newMap.get("TermLoanDocumentTO")));
//        
//        // Add the new Main CUSTOMER ID
//       /* authSignUI.addAcctLevelCustomer(observableBorrow.getTxtCustID());
//        updateBorrowerTabCustDetails();
//        txtCustID.setText(observableBorrow.getTxtCustID());
//        tblBorrowerTabCTable.setModel(observableBorrow.getTblBorrower());
//        System.out.println("inside custinfo@@@@@@@@");
//        observableBorrow.populateBorrowerTabCustFields(hash, CommonUtil.convertObjToStr(tblBorrowerTabCTable.getValueAt(0, 2)));
//        updateBorrowerTabCustFields();
//        hash = null;*/
    }
    
    private void updateBorrowerTabCustFields(){
        //        txtNetWorth.setText(observableComp.getTxtNetWorth());
        //        txtRiskRating.setText(observableComp.getTxtRiskRating());
        //        tdtCreditFacilityAvailSince.setDateValue(observableComp.getTdtCreditFacilityAvailSince());
        //        tdtDealingWithBankSince.setDateValue(observableComp.getTdtDealingWithBankSince());
        //        cboNatureBusiness.setSelectedItem(observableComp.getCboNatureBusiness());
        //        tdtAsOn.setDateValue(observableComp.getTdtAsOn());
        updateCompanyDetails();
    }
    
    public void customerIdPopulating(String status){
        if (status.equals("CANCELLED")) {
            btnAuthorize.setEnabled(false);
            btnReject.setEnabled(false);
        }else if (status.equals("AUTHORIZED")){
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnView.setEnabled(false);
        }else if(status.equals("REJECTED")) {
            btnCancel.setEnabled(true);
            btnReject.setEnabled(true);
            btnView.setEnabled(false);
        }
    }
    
    private void updateCompanyDetails(){
        //        cboAddressType.setSelectedItem(observableComp.getCboAddressType());
        //        cboCity_CompDetail.setSelectedItem(observableComp.getCboCity_CompDetail());
        //        cboCountry_CompDetail.setSelectedItem(observableComp.getCboCountry_CompDetail());
        //        cboState_CompDetail.setSelectedItem(observableComp.getCboState_CompDetail());
        //        txtCompanyRegisNo.setText(observableComp.getTxtCompanyRegisNo());
        //        tdtDateEstablishment.setDateValue(observableComp.getTdtDateEstablishment());
        //        txtChiefExecutiveName.setText(observableComp.getTxtChiefExecutiveName());
        //        txtPin_CompDetail.setText(observableComp.getTxtPin_CompDetail());
        //        txtPhone_CompDetail.setText(observableComp.getTxtPhone_CompDetail());
        //        txtStreet_CompDetail.setText(observableComp.getTxtStreet_CompDetail());
        //        txtArea_CompDetail.setText(observableComp.getTxtArea_CompDetail());
    }
    
    private void updateBorrowerTabCustDetails(){
        //        lblCustName_2.setText(observableBorrow.getLblCustName());
        //        lblOpenDate2.setText(observableBorrow.getLblOpenDate());
        //        lblCity_BorrowerProfile_2.setText(observableBorrow.getLblCity());
        //        lblState_BorrowerProfile_2.setText(observableBorrow.getLblState());
        //        lblPin_BorrowerProfile_2.setText(observableBorrow.getLblPin());
        //        lblPhone_BorrowerProfile_2.setText(observableBorrow.getLblPhone());
        //        lblFax_BorrowerProfile_2.setText(observableBorrow.getLblFax());
    }
    
    private void jointAcctDisplay(String Cust_ID){
        HashMap hash = new HashMap();
//        hash.put("CUST_ID", Cust_ID);
//        observableBorrow.populateJointAccntTable(hash);
//        tblBorrowerTabCTable.setModel(observableBorrow.getTblBorrower());
//        setBorrowerNewOnlyEnable();
        //        authSignUI.addAcctLevelCustomer(Cust_ID);
        hash = null;
    }
    
    private void securityCustDetails(HashMap map){
//        String strPrevCustID = observableSecurity.getTxtCustID_Security();
//        observableSecurity.populateCustDetails(map);
//        //        txtCustID_Security.setText(observableSecurity.getTxtCustID_Security());
        //        lblCustName_Security_Display.setText(observableSecurity.getLblCustName_Security_Display());
//        if (!(strPrevCustID.equals(observableSecurity.getTxtCustID_Security()))){
//            // If the prev. cust ID doesn't match with the current one then
//            // clear the security no.
//            observableSecurity.setTxtSecurityNo("");
//            //            txtSecurityNo.setText(observableSecurity.getTxtSecurityNo());
//            observableSecurity.setTxtMargin("");
//            txtMargin.setText(observableSecurity.getTxtMargin());
//        }
//        strPrevCustID = null;
    }
    
    private void securityIDDetails(HashMap hash){
//        observableSecurity.populateSecurityID_Value(hash, txtLimit_SD.getText());
//        //        txtSecurityNo.setText(observableSecurity.getTxtSecurityNo());
//        txtSecurityValue.setText(observableSecurity.getTxtSecurityValue());
    }
    
    private void populateDisbursementDetails(HashMap hash){
//        observableRepay.populateDisbursementDetails(hash);
//        //        txtLaonAmt.setText(observableRepay.getTxtLaonAmt());
//    }    private void cboRepayFreqActionPerformed(){
//        if (cboRepayFreq.getSelectedItem().equals("User Defined") || cboRepayFreq.getSelectedItem().equals("Lump Sum")){
//            if ((observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) || (viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT))){
//                //                tdtTDate.setEnabled(false);
//            }else{
//                //                tdtTDate.setEnabled(true);
//            }
//            //            moratorium_Given_Calculation();
//        }else{
//            //            tdtTDate.setEnabled(false);
//            calculateSanctionToDate();
//        }
//        populatePeriodDifference();
    }
    
    private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    //To enable and disable the textfields and buttons when NEW button is pressed
    private void newPressedEnableDisable(boolean val){
        //        poaUI.setPoANewOnlyEnable();
        //        btnCustomerID_GD.setEnabled(!val);
        //        btnAccNo.setEnabled(!val);
    }
    
    //To enable or disable text fields and buttons of all Customer fields
    //in the tabbed panel
    private void setbtnCustEnableDisable(boolean val){
//        btnCustID.setEnabled(val);
        //        btnCustomerID_GD.setEnabled(val);
        //        btnAccNo.setEnabled(val);
        //        authSignUI.setbtnCustEnableDisable(val);
        //        poaUI.setbtnCustEnableDisable(val);
    }
    
    private void setBorrowerDetailsEnableDisable(boolean val){
//        tdtDealingWithBankSince.setEnabled(false);
        //        tdtCreditFacilityAvailSince.setEnabled(false);
        ////        txtRiskRating.setEnabled(val);
        //        txtRiskRating.setEditable(false);
        //        txtNetWorth.setEnabled(val);
        //        //        txtNetWorth.setEditable(false);
        //        cboNatureBusiness.setEnabled(false);
        //        cboNatureBusiness.setEditable(false);
        //        tdtAsOn.setEnabled(val);
        //        tdtAsOn.setEnabled(false);
    }
    // To enable or diable the fields in Company details
    private void setCompanyDetailsEnableDisable(boolean val){
        //        txtCompanyRegisNo.setEnabled(val);
        //        txtCompanyRegisNo.setEditable(false);
        //        tdtDateEstablishment.setEnabled(false);
        //        txtChiefExecutiveName.setEnabled(val);
        //        txtChiefExecutiveName.setEditable(false);
        //        cboAddressType.setEnabled(false);
        //        cboAddressType.setEditable(false);
        //        txtStreet_CompDetail.setEnabled(val);
        //        txtStreet_CompDetail.setEditable(false);
        //        txtArea_CompDetail.setEnabled(val);
        //        txtArea_CompDetail.setEditable(false);
        //        cboCity_CompDetail.setEnabled(false);
        //        cboCity_CompDetail.setEditable(false);
        //        cboState_CompDetail.setEnabled(false);
        //        cboState_CompDetail.setEditable(false);
        //        cboCountry_CompDetail.setEnabled(false);
        //        cboCountry_CompDetail.setEditable(false);
        //        txtPin_CompDetail.setEnabled(val);
        //        txtPin_CompDetail.setEditable(false);
        //        txtPhone_CompDetail.setEnabled(val);
        //        txtPhone_CompDetail.setEditable(false);
    }
    
    // To enable and disable all the Tool buttons for the CTable
    private void setAllTableBtnsEnableDisable(boolean val){
        //        authSignUI.setAuthTabBtnEnableDisable(val);
        //        authSignUI.setAuthInstAllBtnsEnableDisable(val);
        //        poaUI.setPoAToolBtnsEnableDisable(val);
        setSanctionFacilityBtnsEnableDisable(val);
        setSanctionMainBtnsEnableDisable(val);
        setFacilityBtnsEnableDisable(val);
        setAllSecurityBtnsEnableDisable(val);
        setAllRepaymentBtnsEnableDisable(val);
        setAllGuarantorBtnsEnableDisable(val);
        setAllInterestBtnsEnableDisable(val);
        setAllBorrowerBtnsEnableDisable(val);
        setAllSettlmentEnableDisable(val);
    }
    
    //To enable and disable the buttons in Santion Facility
    private void setSanctionFacilityBtnsEnableDisable(boolean val){
        //        btnNew1.setEnabled(val);
        //        btnSave1.setEnabled(val);
        //        btnDelete1.setEnabled(val);
        //        btnLTD.setEnabled(val);
    }
    
    private void setAllSanctionFacilityEnableDisable(boolean val){
        //        if (loanType.equals("LTD")){
        //            cboTypeOfFacility.setEnabled(false);
        //            morotoriumEnableDisable(false);
        //        }
        //        else
        //            cboTypeOfFacility.setEnabled(val);
        //        cboProductId.setEnabled(val);
        //        txtLimit_SD.setEnabled(val);
        //        txtLimit_SD2.setEnabled(val);
        //        tdtFDate.setEnabled(val);
        //        tdtTDate.setEnabled(false);
        //        txtNoInstallments.setEnabled(val);
        //        cboRepayFreq.setEnabled(val);
        //        tdtFacility_Repay_Date.setEnabled(false);
        //        txtPeriodDifference_Days.setEnabled(false);
        ////        txtPeriodDifference_Days.setEditable(false);
        //        txtPeriodDifference_Months.setEnabled(false);
        ////        txtPeriodDifference_Months.setEditable(false);
        //        txtPeriodDifference_Years.setEnabled(false);
        ////        txtPeriodDifference_Years.setEditable(false);
        //        chkMoratorium_Given.setEnabled(false);
        //        txtFacility_Moratorium_Period.setEditable(false);//val
    }
    
    private void setSanctionMainBtnsEnableDisable(boolean val){
        //        btnNew2_SD.setEnabled(val);
        //        btnSave2_SD.setEnabled(val);
        //        btnDelete2_SD.setEnabled(val);
    }
    
    private void setAllSanctionMainEnableDisable(boolean val){
        //        txtSanctionSlNo.setEnabled(val);
        //        txtSanctionSlNo.setEditable(false);
        //        txtSanctionNo.setEnabled(val);
        //        tdtSanctionDate.setEnabled(val);
        //        cboSanctioningAuthority.setEnabled(val);
        //        txtSanctionRemarks.setEnabled(val);
        //        cboModeSanction.setEnabled(val);
    }
    
    private void setAllFacilityDetailsEnableDisable(boolean val){
        //        txtAcct_Name.setEnabled(val);
        if (loanType.equals("LTD")) {
            //            rdoSecurityDetails_Fully.setEnabled(false);
            //            rdoSecurityDetails_Partly.setEnabled(false);
            //            rdoSecurityDetails_Unsec.setEnabled(false);
            //            rdoMultiDisburseAllow_No.setEnabled(val);
            //            rdoMultiDisburseAllow_Yes.setEnabled(val);
            //            chkInsurance.setEnabled(false);
            //            chkGurantor.setEnabled(false);
            //            chkStockInspect.setEnabled(false);
            
        } else {
            //            rdoSecurityDetails_Fully.setEnabled(val);
            //            rdoSecurityDetails_Partly.setEnabled(val);
            //            rdoSecurityDetails_Unsec.setEnabled(val);
            //            rdoMultiDisburseAllow_No.setEnabled(val);
            //            rdoMultiDisburseAllow_Yes.setEnabled(val);
            //            chkStockInspect.setEnabled(val);
            //            chkInsurance.setEnabled(val);
            //            chkGurantor.setEnabled(val);
            //            rdoInterest_Simple.setEnabled(false);
            //            rdoInterest_Compound.setEnabled(false);
        }
        //        ClientUtil.enableDisable(panOtherFacilityChkBoxes,val);
        //        chkAccountTransfer.setEnabled(false);//mahila not need so disabled
        //        cboAccStatus.setEnabled(false);
        //        if (CommonUtil.convertObjToStr(CommonConstants.OPERATE_MODE).equals(CommonConstants.IMPLEMENTATION)) {
        //            tdtAccountOpenDate.setEnabled(true);
        //        } else {
        //            tdtAccountOpenDate.setEnabled(false);
        //        }
        //        tdtAccountCloseDate.setEnabled(false);
        //        cboRecommendedByType.setEnabled(val);
        //        //        rdoAccType_New.setEnabled(val);
        //        //        rdoAccType_Transfered.setEnabled(val);
        //        rdoAccLimit_Main.setEnabled(val);
        //        rdoAccLimit_Submit.setEnabled(val);
        //        rdoRiskWeight_No.setEnabled(val);
        //        rdoRiskWeight_Yes.setEnabled(val);
        //        rdoNatureInterest_NonPLR.setEnabled(false);
        //        rdoNatureInterest_PLR.setEnabled(false);
        //        cboInterestType.setEnabled(val);
        ////        tdtDemandPromNoteDate.setEnabled(val);
        ////        tdtDemandPromNoteExpDate.setEnabled(val);
        //        tdtDemandPromNoteDate.setEnabled(false);
        //        tdtDemandPromNoteExpDate.setEnabled(false);
        //        tdtAODDate.setEnabled(val);
        //        rdoSubsidy_No.setEnabled(false);
        //        rdoSubsidy_Yes.setEnabled(false);
        //        txtPurposeDesc.setEnabled(val);
        //        txtGroupDesc.setEnabled(false);
        //        //        rdoInterest_Compound.setEnabled(val);
        //        //        rdoInterest_Simple.setEnabled(val);
        //        txtContactPerson.setEnabled(val);
        //        txtContactPhone.setEnabled(val);
        //        txtRemarks.setEnabled(val);
    }
    private void setFacilityBtnsEnableDisable(boolean val){
        //        btnFacilityDelete.setEnabled(val);
        //        btnFacilitySave.setEnabled(val);
    }
    
    private void setAllSecurityDetailsEnableDisable(boolean val){
        //        ClientUtil.enableDisable(panSecurityDetails_security, val);
        //        btnSecurityNo_Security.setEnabled(val);
        //        btnCustID_Security.setEnabled(val);
        //        txtSecurityNo.setEditable(false);
        //        txtSecurityNo.setEnabled(val);
        //        txtSecurityValue.setEditable(false);
        //        txtSecurityValue.setEnabled(val);
        //        txtCustID_Security.setEditable(false);
        //        txtCustID_Security.setEnabled(val);
        //        txtMargin.setEnabled(val);
        //        txtEligibleLoan.setEditable(false);
        //        txtEligibleLoan.setEnabled(val);
        //        tdtToDate.setEnabled(val);
        //        tdtFromDate.setEnabled(val);
    }
    private void setSecurityBtnsOnlyNewEnable(){
        //        btnSecurityDelete.setEnabled(false);
        //        btnSecurityNew.setEnabled(true);
        //        btnSecuritySave.setEnabled(false);
    }
    private void setSecurityBtnsOnlyNewSaveEnable(){
        //        btnSecurityDelete.setEnabled(false);
        //        btnSecurityNew.setEnabled(true);
        //        btnSecuritySave.setEnabled(true);
    }
    private void setAllSecurityBtnsEnableDisable(boolean val){
        //        btnSecurityDelete.setEnabled(val);
        //        btnSecurityNew.setEnabled(val);
        //        btnSecuritySave.setEnabled(val);
    }
    //    private void setAllRepaymentDetailsEnableDisable(boolean val){
    //        txtScheduleNo.setEditable(false);
    //        if(loanType.equals("OTHERS")){
    //            cboRepayType.setEnabled(val);
    //            btnEMI_Calculate.setEnabled(true);
    //              tdtFirstInstall.setEnabled(false);
    //        }else {
    //            cboRepayType.setEnabled(false);
    //            btnEMI_Calculate.setEnabled(false);
    //        }
    //        if(outStandingAmtRepayment){
    //            txtLaonAmt.setEnabled(val);//false
    //            txtLaonAmt.setEditable(val);//false
    //            tdtFirstInstall.setEnabled(val);
    //            cboRepayFreq_Repayment.setEnabled(true);
    //            txtNoInstall.setEditable(val);//false//bala
    //            txtNoInstall.setEnabled(val);
    //            tdtRepayFromDate.setEnabled(val);
    //            //            txtNoMonthsMora.setEditable(true);
    //            //            txtNoMonthsMora.setEnabled(val);
    //            //        tdtLastInstall.setEnabled(val);
    //
    //        }else{
    //            txtLaonAmt.setEnabled(false);//false
    //            txtLaonAmt.setEditable(false);//false
    //            tdtFirstInstall.setEnabled(false);
    //            tdtLastInstall.setEnabled(false);
    //            tdtFirstInstall.setEnabled(false);
    //            cboRepayFreq_Repayment.setEnabled(false);
    //            txtNoMonthsMora.setEditable(false);
    //            tdtRepayFromDate.setEnabled(false);
    ////            txtNoMonthsMora.setEnabled(val);
    //            txtNoInstall.setEditable(false);//false//bala
    ////            txtNoInstall.setEnabled(val);
    //
    //        }
    //        txtRepayScheduleMode.setEditable(false);
    //        txtRepayScheduleMode.setEnabled(val);
    //        //        cboRepayFreq_Repayment.setEnabled(false);
    //
    //        //        txtNoMonthsMora.setEditable(false);
    //        //        txtNoMonthsMora.setEnabled(val);
    //
    //        tdtLastInstall.setEnabled(false);
    //        //        txtNoInstall.setEditable(val);//false//bala
    //        //        txtNoInstall.setEnabled(val);
    //        tdtDisbursement_Dt.setEnabled(false);
    //        txtTotalBaseAmt.setEditable(false);
    //        txtTotalBaseAmt.setEnabled(false);//val
    //        txtAmtLastInstall.setEditable(false);
    //        txtAmtLastInstall.setEnabled(false);//val
    //        txtAmtPenulInstall.setEditable(false);
    //        txtAmtPenulInstall.setEnabled(false);//val
    //        txtTotalInstallAmt.setEditable(false);
    //        txtTotalInstallAmt.setEnabled(false);//val
    //        rdoDoAddSIs_No.setEnabled(val);
    //        rdoDoAddSIs_Yes.setEnabled(val);
    //        rdoPostDatedCheque_No.setEnabled(val);
    //        rdoPostDatedCheque_Yes.setEnabled(val);
    //        if (rdoInActive_Repayment.isSelected()){
    //            setRdoRepaymentStatusDisable();
    //        }else{
    //            rdoActive_Repayment.setEnabled(val);
    //            rdoInActive_Repayment.setEnabled(val);
    //        }
    //    }
    
    private void setAllRepaymentBtnsEnableDisable(boolean val){
        //        btnRepayment_Delete.setEnabled(val);
        //        btnRepayment_New.setEnabled(val);
        //        btnRepayment_Save.setEnabled(val);
    }
    
    private void setRepaymentBtnsEnableDisable(boolean val){
        //        btnRepayment_Delete.setEnabled(!val);
        //        btnRepayment_New.setEnabled(val);
        //        btnRepayment_Save.setEnabled(!val);
    }
    
    private void setRepaymentNewOnlyEnable(){
        //        btnRepayment_Delete.setEnabled(false);
        //        //        if (loanType.equals("LTD"))
        //        //            btnRepayment_New.setEnabled(false);
        //        //        else
        //        btnRepayment_New.setEnabled(true);
        //        btnRepayment_Save.setEnabled(false);
        //        btnEMI_Calculate.setEnabled(false);
    }
    
    private void setRepaymentDeleteOnlyDisbale(){
        //        btnRepayment_Delete.setEnabled(false);
        //        if (loanType.equals("LTD"))
        //            btnRepayment_New.setEnabled(false);
        //        else
        //            btnRepayment_New.setEnabled(true);
        //        btnRepayment_Save.setEnabled(true);
        //        btnEMI_Calculate.setEnabled(true);
    }
    
    private void setAllGuarantorDetailsEnableDisable(boolean val){
        //        txtCustomerID_GD.setEditable(false);
        //        txtCustomerID_GD.setEnabled(val);
        //        txtGuaranAccNo.setEditable(false);
        //        txtGuaranAccNo.setEnabled(val);
        //        txtGuaranName.setEditable(false);
        //        txtGuaranName.setEnabled(val);
        //        txtGuarantorNo.setEnabled(val);
        //        txtStreet_GD.setEditable(false);
        //        txtStreet_GD.setEnabled(val);
        //        txtArea_GD.setEditable(false);
        //        txtArea_GD.setEnabled(val);
        //        cboCity_GD.setEnabled(false);
        //        cboState_GD.setEnabled(false);
        //        txtPin_GD.setEditable(false);
        //        txtPin_GD.setEnabled(val);
        //        txtPhone_GD.setEditable(false);
        //        txtPhone_GD.setEnabled(val);
        //        cboConstitution_GD.setEnabled(false);
        //        txtGuarantorNetWorth.setEditable(false);
        //        txtGuarantorNetWorth.setEnabled(val);
        //        tdtAsOn_GD.setEnabled(false);
        //        cboCountry_GD.setEnabled(false);
        //        tdtDOB_GD.setEnabled(false);
        //        cboProdId.setEnabled(val);
        //        cboProdType.setEnabled(val);
    }
    private void setAllInstitGuarantorDetailsEnableDisable(boolean val){
        //        cboPLIName.setEnabled(val);
        //        cboPLIBranch.setEnabled(val);
        //        txtGuaratNo.setEnabled(val);
        //        tdtGuaranDate.setEnabled(val);
        //        tdtGuaranPeriodFrom.setEnabled(val);
        //        tdtGuaranPeriodTo.setEnabled(val);
        //        txtGuaranCommision.setEnabled(val);
        //        cboGuaranStatus.setEnabled(val);
        //        tdtStatusDate.setEnabled(val);
        //        txtGuarnRemarks.setEnabled(val);
    }
    private void setGuarantorDetailsNewOnlyEnabled(){
        //        btnGuarantorNew.setEnabled(true);
        //        btnGuarantorSave.setEnabled(false);
        //        btnGuarantorDelete.setEnabled(false);
    }
    
    private void setGuarantorDetailsDeleteOnlyDisabled(){
        //        btnGuarantorNew.setEnabled(true);
        //        btnGuarantorSave.setEnabled(true);
        //        btnGuarantorDelete.setEnabled(false);
    }
    
    private void setAllGuarantorBtnsEnableDisable(boolean val){
        //        btnGuarantorNew.setEnabled(val);
        //        btnGuarantorSave.setEnabled(val);
        //        btnGuarantorDelete.setEnabled(val);
        //        rdoGuarnIndividual.setEnabled(val);
        //        rdoGuarnInsititutional.setEnabled(val);
        //
    }
    
    private void setAllDocumentDetailsEnableDisable(boolean val){
        //        rdoYes_DocumentDetails.setEnabled(val);
        //        rdoNo_DocumentDetails.setEnabled(val);
        //        tdtSubmitDate_DocumentDetails.setEnabled(val);
        //        txtRemarks_DocumentDetails.setEnabled(val);
        //        rdoYes_Executed_DOC.setEnabled(val);
        //        rdoNo_Executed_DOC.setEnabled(val);
        //        rdoYes_Mandatory_DOC.setEnabled(val);
        //        rdoNo_Mandatory_DOC.setEnabled(val);
        //        tdtExecuteDate_DOC.setEnabled(val);
        //        tdtExpiryDate_DOC.setEnabled(val);
//        ClientUtil.enableDisable(panTabDetails_DocumentDetails, val);
    }
    
    private void setDocumentToolBtnEnableDisable(boolean val){
//        btnSave_DocumentDetails.setEnabled(val);
    }
    
    private void setAllInterestDetailsEnableDisable(boolean val){
        //        tdtFrom.setEnabled(val);
        //        tdtTo.setEnabled(val);
        //        txtFromAmt.setEnabled(val);
        //        txtToAmt.setEnabled(val);
        //        txtInter.setEnabled(val);
        //        txtPenalInter.setEnabled(val);
        //        txtAgainstClearingInter.setEnabled(val);
        //        txtPenalStatement.setEnabled(val);
        //        txtInterExpLimit.setEnabled(val);
        //        ClientUtil.enableDisable(panTableFields, val);
    }
    
    private void setInterestDetailsOnlyNewEnabled(){
        //        btnInterestMaintenanceNew.setEnabled(true);
        //        btnInterestMaintenanceSave.setEnabled(false);
        //        btnInterestMaintenanceDelete.setEnabled(false);
    }
    
    private void setInterestDetailsOnlyDeleteDisabled(){
        //        btnInterestMaintenanceNew.setEnabled(true);
        //        btnInterestMaintenanceSave.setEnabled(true);
        //        btnInterestMaintenanceDelete.setEnabled(false);
    }
    
    private void setAllInterestBtnsEnableDisable(boolean val){
        //        btnInterestMaintenanceNew.setEnabled(val);
        //        btnInterestMaintenanceSave.setEnabled(val);
        //        btnInterestMaintenanceDelete.setEnabled(val);
    }
    
    private void setAllClassificationDetailsEnableDisable(boolean val){
        //        cboCommodityCode.setEnabled(val);
        //        cboSectorCode1.setEnabled(val);
        //        cboPurposeCode.setEnabled(val);
        //        cboIndusCode.setEnabled(val);
        //        cbo20Code.setEnabled(val);
        //        cboGovtSchemeCode.setEnabled(val);
        //        cboGuaranteeCoverCode.setEnabled(val);
        //        cboHealthCode.setEnabled(val);
        //        cboDistrictCode.setEnabled(val);
        //        cboWeakerSectionCode.setEnabled(val);
        //        cboRefinancingInsti.setEnabled(val);
        //        cboAssetCode.setEnabled(val);
        //        tdtNPADate.setEnabled(val);//false
        //        cboTypeFacility.setEnabled(val);
        //        chkDirectFinance.setEnabled(val);
        //        chkECGC.setEnabled(val);
        //        chkPrioritySector.setEnabled(val);
        //        chkDocumentcomplete.setEnabled(val);
        //        chkQIS.setEnabled(val);
    }
    private void calculateSanctionToDate() {
          Date repayDate=null;
          Date toDate;
            if (!tdtAccountOpenDate.getDateValue().equals("") && !txtNoOfInstall.getText().equals("")) {
               // moratorium_Given_Calculation();
                java.util.GregorianCalendar gCalendar = new java.util.GregorianCalendar();
                java.util.GregorianCalendar gCalendarrepaydt = new java.util.GregorianCalendar(); //forrepaydate shoude change from first dt
                if (tdtRepaymentDt.getDateValue()== null) {
                    repayDate = DateUtil.getDateMMDDYYYY(tdtAccountOpenDate.getDateValue());
                }
                gCalendar.setGregorianChange(repayDate);
                gCalendar.setTime(repayDate);
                gCalendarrepaydt.setGregorianChange(repayDate);
                gCalendarrepaydt.setTime(repayDate);
                int dateVal = 30;
                int incVal = observable.getInstallNo(txtNoOfInstall.getText(), dateVal);
                date = new java.util.Date();
                date = repayDate ;
                if (txtNoOfInstall.getText().equals("1")) {
                    date = DateUtil.getDateMMDDYYYY(tdtAccountOpenDate.getDateValue());
                }
                //System.out.println("Date##" + date);
                if (dateVal <= 7) {
                    gCalendar.add(gCalendar.DATE, incVal);
                } else if (dateVal >= 30) {
                    gCalendar.add(gCalendar.MONTH, incVal);
                    int firstInstall = dateVal / 30;
                    gCalendarrepaydt.add(gCalendarrepaydt.MONTH, firstInstall);//for repaydate
                }
                
                toDate = gCalendar.getTime();
                observable.setToDate(DateUtil.getStringDate(toDate));        
                repayDate = gCalendarrepaydt.getTime();
                observable.setFirstInstallDate(DateUtil.getStringDate(repayDate));
                gCalendar = null;
                gCalendarrepaydt = null;
                
                
//                toDate = gCalendar.getTime();
//                observable.setToDate(DateUtil.getStringDate(toDate));
//                observable.setRepayFromDate(DateUtil.getStringDate(repayDate)); 
//                //repayDate = gCalendarrepaydt.getTime();
//                observable.setFirstInstallDate(DateUtil.getStringDate(gCalendarrepaydt.getTime()));               
//                gCalendar = null;
//                gCalendarrepaydt = null;
            }       
      }
    private void setAllBorrowerBtnsEnableDisable(boolean val){
        btnSave_Borrower.setEnabled(val);
        btnDeleteBorrower.setEnabled(val);
      
    }
    
    private void setBorrowerToMainOnlyDisable(){
//        btnNew_Borrower.setEnabled(true);
//        btnDeleteBorrower.setEnabled(true);
//        btnToMain_Borrower.setEnabled(false);
    }
    
    private void setBorrowerNewOnlyEnable(){
//        btnNew_Borrower.setEnabled(true);
//        btnDeleteBorrower.setEnabled(false);
//        btnToMain_Borrower.setEnabled(false);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
//        javax.swing.JFrame frm = new javax.swing.JFrame();
//        GoldLoanUI termLoanUI = new GoldLoanUI();
//        frm.getContentPane().add(termLoanUI);
//        termLoanUI.show();
//        frm.setSize(600, 500);
//        frm.show();
        //        TableCellRenderingExample3 tcre = new TableCellRenderingExample3(args);
        //            tcre.setSize(600,400);
        //            tcre.setVisible(true);
    }
    
    /**
     * Getter for property facilitySaved.
     * @return Value of property facilitySaved.
     */
    public boolean isFacilitySaved() {
        return facilitySaved;
    }
    
    /**
     * Setter for property facilitySaved.
     * @param facilitySaved New value of property facilitySaved.
     */
    public void setFacilitySaved(boolean facilitySaved) {
        this.facilitySaved = facilitySaved;
    }
    
    /**
     * Getter for property viewType.
     * @return Value of property viewType.
     */
    public java.lang.String getViewType() {
        return viewType;
    }
    
    /**
     * Setter for property viewType.
     * @param viewType New value of property viewType.
     */
    public void setViewType(java.lang.String viewType) {
        this.viewType = viewType;
    }
    
    /**
     * Getter for property customerScreen.
     * @return Value of property customerScreen.
     */
    public java.lang.String getCustomerScreen() {
        return customerScreen;
    }
    
    /**
     * Setter for property customerScreen.
     * @param customerScreen New value of property customerScreen.
     */
    public void setCustomerScreen(java.lang.String customerScreen) {
        this.customerScreen = customerScreen;
    }
    
    /**
     * Getter for property totalSecValue.
     * @return Value of property totalSecValue.
     */
    public java.lang.String getTotalSecValue() {
        return totalSecValue;
    }
    
    /**
     * Setter for property totalSecValue.
     * @param totalSecValue New value of property totalSecValue.
     */
    public void setTotalSecValue(java.lang.String totalSecValue) {
        this.totalSecValue = totalSecValue;
    }
    
    /**
     * Getter for property totalMarginValue.
     * @return Value of property totalMarginValue.
     */
    public java.lang.String getTotalMarginValue() {
        return totalMarginValue;
    }
    
    /**
     * Setter for property totalMarginValue.
     * @param totalMarginValue New value of property totalMarginValue.
     */
    public void setTotalMarginValue(java.lang.String totalMarginValue) {
        this.totalMarginValue = totalMarginValue;
    }
    
    /**
     * Getter for property totalEligibleValue.
     * @return Value of property totalEligibleValue.
     */
    public java.lang.String getTotalEligibleValue() {
        return totalEligibleValue;
    }
    
    /**
     * Setter for property totalEligibleValue.
     * @param totalEligibleValue New value of property totalEligibleValue.
     */
    public void setTotalEligibleValue(java.lang.String totalEligibleValue) {
        this.totalEligibleValue = totalEligibleValue;
    }
    
    /**
     * Getter for property eligibleAmt.
     * @return Value of property eligibleAmt.
     */
    public double getEligibleAmt() {
        return eligibleAmt;
    }
    
    /**
     * Setter for property eligibleAmt.
     * @param eligibleAmt New value of property eligibleAmt.
     */
    public void setEligibleAmt(double eligibleAmt) {
        this.eligibleAmt = eligibleAmt;
    }
    
    
        
    private void tableMouseClicked(java.awt.event.MouseEvent evt) {         
        int column = CommonUtil.convertObjToInt(table.getSelectedColumn());
        boolean checked = false;
        String desc = CommonUtil.convertObjToStr(table.getValueAt(table.getSelectedRow(), 1));
        System.out.println("column"+column+"desc"+desc);
        System.out.println("serviceTaxApplMap inside mouse click ::" + serviceTaxApplMap);
        if (serviceTaxApplMap != null && serviceTaxApplMap.size() > 0) {                
                List taxSettingsList = new ArrayList();              
                for (int i = 0; i < table.getRowCount(); i++) {
                      double chrgamt = 0;
                    HashMap serviceTaSettingsMap = new HashMap();
                    boolean checkFlag = new Boolean(CommonUtil.convertObjToStr(table.getValueAt(i, 0))).booleanValue();
                    String descVal = CommonUtil.convertObjToStr(table.getValueAt(i, 1));
                    System.out.println("checkFlag"+checkFlag);
                    if (checkFlag && CommonUtil.convertObjToStr(serviceTaxApplMap.get(descVal)).equals("Y") && CommonUtil.convertObjToStr(serviceTaxIdMap.get(descVal)).length() > 0 ) {
                         System.out.println("entered"+descVal);
                        chrgamt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(table.getValueAt(i, 3)));
                         if (chrgamt > 0) {   
                        serviceTaSettingsMap.put("SETTINGS_ID",serviceTaxIdMap.get(descVal));
                        serviceTaSettingsMap.put(ServiceTaxCalculation.TOT_AMOUNT,CommonUtil.convertObjToStr(chrgamt));
                        //serviceTaSettingsMap.put(serviceTaxIdMap.get(descVal), CommonUtil.convertObjToStr(chrgamt));    
                        taxSettingsList.add(serviceTaSettingsMap);
                    } 
                    }
                   
                }
                System.out.println("serviceTaSettingsMap :: "+ taxSettingsList);
                try {
                    objServiceTax = new ServiceTaxCalculation();
                    HashMap ser_Tax_Val = new HashMap();
                    ser_Tax_Val.put(ServiceTaxCalculation.CURR_DT, ClientUtil.getCurrentDate());
                   // ser_Tax_Val.put(ServiceTaxCalculation.TOT_AMOUNT, CommonUtil.convertObjToStr(chrgamt));
                    ser_Tax_Val.put("SERVICE_TAX_DATA", taxSettingsList);
                    serviceTax_Map = objServiceTax.calculateServiceTax(ser_Tax_Val);
                    if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.TOT_TAX_AMT)) {
                        String amt = CommonUtil.convertObjToStr(serviceTax_Map.get(ServiceTaxCalculation.TOT_TAX_AMT));
//                        lblServiceTaxval.setText(objServiceTax.roundOffAmt(amt, "NEAREST_VALUE"));
//                        serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, objServiceTax.roundOffAmt(amt, "NEAREST_VALUE"));
                        lblServiceTaxval.setText(amt);
                        serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, amt);
                    } else {
                        lblServiceTaxval.setText("0.00");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCustID;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDeleteBorrower;
    private com.see.truetransact.uicomponent.CButton btnDepositNo;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnNew_Borrower;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSave_Borrower;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CComboBox cboCategory;
    private com.see.truetransact.uicomponent.CComboBox cboConsitution;
    private com.see.truetransact.uicomponent.CComboBox cboDepositProduct;
    private com.see.truetransact.uicomponent.CComboBox cboLoanProduct;
    private com.see.truetransact.uicomponent.CComboBox cboSanctionBy;
    private com.see.truetransact.uicomponent.CCheckBox chkDepositUnlien;
    private com.see.truetransact.uicomponent.CCheckBox chkMobileBankingTLAD;
    private com.see.truetransact.uicomponent.CLabel jLabel1;
    private com.see.truetransact.uicomponent.CLabel lblAccOpenDt;
    private com.see.truetransact.uicomponent.CLabel lblAccountHeadId;
    private com.see.truetransact.uicomponent.CLabel lblAcctNo_Sanction;
    private com.see.truetransact.uicomponent.CLabel lblAcctNo_Sanction_Disp;
    private com.see.truetransact.uicomponent.CLabel lblAcreage;
    private com.see.truetransact.uicomponent.CLabel lblAcreageVal;
    private com.see.truetransact.uicomponent.CLabel lblAdditionalLoanfacility;
    private com.see.truetransact.uicomponent.CLabel lblCategory;
    private com.see.truetransact.uicomponent.CLabel lblConsititution;
    private com.see.truetransact.uicomponent.CLabel lblCustNameValue;
    private com.see.truetransact.uicomponent.CLabel lblCustomerId;
    private com.see.truetransact.uicomponent.CLabel lblCustomerIdValue;
    private com.see.truetransact.uicomponent.CLabel lblDepositNo;
    private com.see.truetransact.uicomponent.CLabel lblEligibileAmt;
    private com.see.truetransact.uicomponent.CLabel lblEligibileAmtValue;
    private com.see.truetransact.uicomponent.CLabel lblInstallMentAmount;
    private com.see.truetransact.uicomponent.CLabel lblInter;
    private com.see.truetransact.uicomponent.CLabel lblIntrestAmt;
    private com.see.truetransact.uicomponent.CLabel lblLoanAmt;
    private com.see.truetransact.uicomponent.CLabel lblMDSChitAmountPaid;
    private com.see.truetransact.uicomponent.CLabel lblMDSChitAmountPaidVal;
    private com.see.truetransact.uicomponent.CLabel lblMDSMemberName;
    private com.see.truetransact.uicomponent.CLabel lblMDSMemberNameVal;
    private com.see.truetransact.uicomponent.CLabel lblMDSMemberNo;
    private com.see.truetransact.uicomponent.CLabel lblMDSMemberNoVal;
    private com.see.truetransact.uicomponent.CLabel lblMDSMemberType;
    private com.see.truetransact.uicomponent.CLabel lblMDSMemberTypeVal;
    private com.see.truetransact.uicomponent.CLabel lblMemberId;
    private com.see.truetransact.uicomponent.CLabel lblMemberIdValue;
    private com.see.truetransact.uicomponent.CLabel lblMobileNo;
    private com.see.truetransact.uicomponent.CLabel lblMobileSubscribedFrom;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblMultiDisburseAllow;
    private com.see.truetransact.uicomponent.CLabel lblNoOfInstall;
    private com.see.truetransact.uicomponent.CLabel lblProductId;
    private com.see.truetransact.uicomponent.CLabel lblProductId1;
    private com.see.truetransact.uicomponent.CLabel lblPurchaseAmount;
    private com.see.truetransact.uicomponent.CLabel lblPurchaseAmountVal;
    private com.see.truetransact.uicomponent.CLabel lblPurchaseDate;
    private com.see.truetransact.uicomponent.CLabel lblPurchaseDateVal;
    private com.see.truetransact.uicomponent.CLabel lblPurchaseID;
    private com.see.truetransact.uicomponent.CLabel lblPurchaseIDVal;
    private com.see.truetransact.uicomponent.CLabel lblPurchaseName;
    private com.see.truetransact.uicomponent.CLabel lblPurchaseNameVal;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblRepaymentDt;
    private com.see.truetransact.uicomponent.CLabel lblSanctionBy;
    private com.see.truetransact.uicomponent.CLabel lblSanctionRef;
    private com.see.truetransact.uicomponent.CLabel lblServiceTaxval;
    private com.see.truetransact.uicomponent.CLabel lblShowAccountHeadId;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace17;
    private com.see.truetransact.uicomponent.CLabel lblSpace18;
    private com.see.truetransact.uicomponent.CLabel lblSpace19;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace20;
    private com.see.truetransact.uicomponent.CLabel lblSpace21;
    private com.see.truetransact.uicomponent.CLabel lblSpace22;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTodate;
    private com.see.truetransact.uicomponent.CLabel lblTotalNoOfShare;
    private com.see.truetransact.uicomponent.CLabel lblTotalShareAmount;
    private com.see.truetransact.uicomponent.CLabel lblTotalWeight;
    private com.see.truetransact.uicomponent.CLabel lblTotalWeightVal;
    private com.see.truetransact.uicomponent.CLabel lblTransactionDate;
    private com.see.truetransact.uicomponent.CLabel lblTransactionDateVal;
    private com.see.truetransact.uicomponent.CLabel lblspace3;
    private com.see.truetransact.uicomponent.CMenuBar mbrTermLoan;
    private javax.swing.JMenuItem mitAuthorize;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitException;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitReject;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAdditionalLoanFacility;
    private com.see.truetransact.uicomponent.CPanel panBorrowCompanyDetails;
    private com.see.truetransact.uicomponent.CPanel panBorrowProfile;
    private com.see.truetransact.uicomponent.CPanel panBorrowProfile_CustID;
    private com.see.truetransact.uicomponent.CPanel panBorrowProfile_CustID1;
    private com.see.truetransact.uicomponent.CPanel panBorrowProfile_CustName;
    private com.see.truetransact.uicomponent.CPanel panBorrowerTabCTable;
    private com.see.truetransact.uicomponent.CPanel panBorrowerTabTools;
    private com.see.truetransact.uicomponent.CPanel panChargeDetails;
    private com.see.truetransact.uicomponent.CPanel panCustId;
    private com.see.truetransact.uicomponent.CPanel panCustIdDetails;
    private com.see.truetransact.uicomponent.CPanel panInterestDeails1;
    private com.see.truetransact.uicomponent.CPanel panInterestDeails2;
    private com.see.truetransact.uicomponent.CPanel panMDSLoanDetailFields;
    private com.see.truetransact.uicomponent.CPanel panMobileBanking;
    private com.see.truetransact.uicomponent.CPanel panPaddyLoanDetailFields;
    private com.see.truetransact.uicomponent.CPanel panSanctionDetails_Sanction;
    private com.see.truetransact.uicomponent.CPanel panSanctionDetails_Table;
    private com.see.truetransact.uicomponent.CPanel panServiceTax;
    private com.see.truetransact.uicomponent.CPanel panShareDetails;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTermLoan;
    private com.see.truetransact.uicomponent.CButtonGroup rdoAccLimit;
    private com.see.truetransact.uicomponent.CButtonGroup rdoAccType;
    private com.see.truetransact.uicomponent.CButtonGroup rdoAdditionalFacilityGroup;
    private com.see.truetransact.uicomponent.CRadioButton rdoAdditionalLoanFacility_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoAdditionalLoanFacility_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoDoAddSIs;
    private com.see.truetransact.uicomponent.CButtonGroup rdoExecuted_DOC;
    private com.see.truetransact.uicomponent.CButtonGroup rdoGuarnConstution;
    private com.see.truetransact.uicomponent.CButtonGroup rdoInterest;
    private com.see.truetransact.uicomponent.CButtonGroup rdoIsSubmitted_DocumentDetails;
    private com.see.truetransact.uicomponent.CButtonGroup rdoMandatory_DOC;
    private com.see.truetransact.uicomponent.CButtonGroup rdoMultiDisburseAllow;
    private javax.swing.JRadioButton rdoMultiDisburseAllow_No;
    private javax.swing.JRadioButton rdoMultiDisburseAllow_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoNatureInterest;
    private com.see.truetransact.uicomponent.CButtonGroup rdoPostDatedCheque;
    private com.see.truetransact.uicomponent.CButtonGroup rdoRiskWeight;
    private com.see.truetransact.uicomponent.CButtonGroup rdoSecurityDetails;
    private com.see.truetransact.uicomponent.CButtonGroup rdoSecurityType;
    private com.see.truetransact.uicomponent.CButtonGroup rdoStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdoStatus_Repayment;
    private com.see.truetransact.uicomponent.CButtonGroup rdoSubsidy;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptException;
    private javax.swing.JSeparator sptPrint;
    private javax.swing.JSeparator sptProcess;
    private com.see.truetransact.uicomponent.CScrollPane srpBorrowerTabCTable;
    private com.see.truetransact.uicomponent.CTabbedPane tabLimitAmount;
    private com.see.truetransact.uicomponent.CTable tblDepositDetails;
    private javax.swing.JToolBar tbrTermLoan;
    private com.see.truetransact.uicomponent.CDateField tdtAccountOpenDate;
    private com.see.truetransact.uicomponent.CDateField tdtMobileSubscribedFrom;
    private com.see.truetransact.uicomponent.CDateField tdtRepaymentDt;
    private com.see.truetransact.uicomponent.CDateField tdtToDate;
    private com.see.truetransact.uicomponent.CTextField txtCustId;
    private com.see.truetransact.uicomponent.CTextField txtDepositNo;
    private com.see.truetransact.uicomponent.CTextField txtInstallAmount;
    private com.see.truetransact.uicomponent.CTextField txtInter;
    private com.see.truetransact.uicomponent.CTextField txtLoanAmt;
    private com.see.truetransact.uicomponent.CTextField txtMobileNo;
    private com.see.truetransact.uicomponent.CTextField txtNextAccNo;
    private com.see.truetransact.uicomponent.CTextField txtNoOfInstall;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    private com.see.truetransact.uicomponent.CTextField txtSanctionRef;
    private com.see.truetransact.uicomponent.CTextField txtTotalNoOfShare;
    private com.see.truetransact.uicomponent.CTextField txtTotalShareAmount;
    // End of variables declaration//GEN-END:variables
    
    private com.see.truetransact.uicomponent.CComboBox cboCity_GD;
    private com.see.truetransact.uicomponent.CComboBox cboState_GD;
    private com.see.truetransact.uicomponent.CComboBox cboConstitution_GD;
    private com.see.truetransact.uicomponent.CComboBox cboCountry_GD;
    private com.see.truetransact.uicomponent.CComboBox cboProdId;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    
    private com.see.truetransact.uicomponent.CPanel panGuaranAddr;
    private com.see.truetransact.uicomponent.CPanel panRunTimeGuarantorAddr;
    private com.see.truetransact.uicomponent.CPanel panGuarantor;
    private com.see.truetransact.uicomponent.CPanel panRunTimeGuarantor;
    private com.see.truetransact.uicomponent.CTextField txtGuaranterNo;
    private com.see.truetransact.uicomponent.CTextField txtCustomerID_GD;
    private com.see.truetransact.uicomponent.CTextField txtGuaranAccNo;
    private com.see.truetransact.uicomponent.CTextField txtGuaranName;
    private com.see.truetransact.uicomponent.CDateField tdtDOB_GD;
    private com.see.truetransact.uicomponent.CTextField txtStreet_GD;
    private com.see.truetransact.uicomponent.CTextField txtArea_GD;
    private com.see.truetransact.uicomponent.CTextField txtPin_GD;
    private com.see.truetransact.uicomponent.CTextField txtPhone_GD;
    private com.see.truetransact.uicomponent.CTextField txtGuarantorNetWorth;
    private com.see.truetransact.uicomponent.CTextField txtGuarantorNo;
    private com.see.truetransact.uicomponent.CDateField tdtAsOn_GD;
    private com.see.truetransact.uicomponent.CButton btnCustomerID_GD;
    private com.see.truetransact.uicomponent.CButton btnAccNo;
    private com.see.truetransact.uicomponent.CLabel lblDOB_GD;
    private com.see.truetransact.uicomponent.CLabel lblArea_GD;
    private com.see.truetransact.uicomponent.CLabel lblCity_GD;
    private com.see.truetransact.uicomponent.CLabel lblAsOn_GD;
    private com.see.truetransact.uicomponent.CLabel lblConstitution_GD;
    private com.see.truetransact.uicomponent.CLabel lblCountry_GD;
    private com.see.truetransact.uicomponent.CLabel lblCustomerID_GD;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    private com.see.truetransact.uicomponent.CLabel lblGuaranAccNo;
    private com.see.truetransact.uicomponent.CLabel lblGuaranName;
    private com.see.truetransact.uicomponent.CLabel lblGuarantorNo;
    private com.see.truetransact.uicomponent.CLabel lblGuarantorNetWorth;
    private com.see.truetransact.uicomponent.CLabel lblPhone_GD;
    private com.see.truetransact.uicomponent.CLabel lblPin_GD;
    private com.see.truetransact.uicomponent.CLabel lblProdId1;
    private com.see.truetransact.uicomponent.CLabel lblState_GD;
    private com.see.truetransact.uicomponent.CLabel lblStreet_GD;
    private com.see.truetransact.uicomponent.CSeparator sptGuarantorDetail_Vert;
    
    
    private com.see.truetransact.uicomponent.CLabel lblPLIName;
    private com.see.truetransact.uicomponent.CComboBox cboPLIName;
    private com.see.truetransact.uicomponent.CLabel   lblPLIBranch;
    private com.see.truetransact.uicomponent.CComboBox cboPLIBranch;
    private com.see.truetransact.uicomponent.CLabel  lblGuaratNo;
    private com.see.truetransact.uicomponent.CTextField txtGuaratNo;
    private com.see.truetransact.uicomponent.CLabel lblGuaranDate;
    private com.see.truetransact.uicomponent.CDateField tdtGuaranDate;
    private com.see.truetransact.uicomponent.CLabel lblGuaranPeriodFrom;
    private com.see.truetransact.uicomponent.CDateField tdtGuaranPeriodFrom;
    private com.see.truetransact.uicomponent.CLabel lblGuaranPeriodTo;
    private com.see.truetransact.uicomponent.CDateField tdtGuaranPeriodTo;
    private com.see.truetransact.uicomponent.CLabel lblGuaranCommision;
    private com.see.truetransact.uicomponent.CTextField txtGuaranCommision;
    private com.see.truetransact.uicomponent.CLabel lblGuaranStatus;
    private com.see.truetransact.uicomponent.CComboBox cboGuaranStatus;
    private com.see.truetransact.uicomponent.CDateField tdtStatusDate;
    private com.see.truetransact.uicomponent.CLabel lblGuarnRemarks;
    private com.see.truetransact.uicomponent.CTextField txtGuarnRemarks;
    private com.see.truetransact.uicomponent.CLabel lblStatusDate;
    private javax.swing.JScrollPane srpChargeDetails;
}

