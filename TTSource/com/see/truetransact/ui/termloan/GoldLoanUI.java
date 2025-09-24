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
package com.see.truetransact.ui.termloan;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.*;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.transferobject.common.charges.LoanSlabChargesTO;
import com.see.truetransact.transferobject.termloan.TermLoanInterestTO;
import com.see.truetransact.transferobject.termloan.TermLoanSanctionFacilityTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.ui.common.customer.MembershipLiabilityUI;
import com.see.truetransact.ui.common.nominee.NomineeOB;
import com.see.truetransact.ui.common.nominee.NomineeUI;
import com.see.truetransact.ui.common.powerofattorney.PowerOfAttorneyUI;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;
import com.see.truetransact.ui.common.viewall.*;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
import com.see.truetransact.ui.customer.CustomerOB;
import com.see.truetransact.ui.customer.CustomerUISupport;
import com.see.truetransact.ui.customer.IndividualCustUI;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListDebitUI;
import com.see.truetransact.ui.termloan.customerDetailsScreen.CustomerDetailsScreenUI;
import com.see.truetransact.ui.termloan.emicalculator.TermLoanInstallmentUI;
import com.see.truetransact.ui.termloan.loandisbursement.LoanDisbursementOB;
import com.see.truetransact.ui.termloan.loandisbursement.LoanDisbursementUI;
import com.see.truetransact.ui.transaction.common.TransDetailsUI;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.DefaultValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.PercentageValidation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import com.see.truetransact.commonutil.SuspiciousAccountValidation;

import com.github.sarxos.webcam.Webcam;
import com.see.truetransact.clientutil.ImageFileFilter;
import com.see.truetransact.commonutil.SuspiciousAccountValidation;
import com.see.truetransact.uicomponent.CLabel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

/*
 *
 * @author  Sathiya
 * Created on September 13, 2010, 3:55 PM
 *
 */
public class GoldLoanUI extends CInternalFrame implements java.util.Observer, UIMandatoryField, Runnable {

    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.GoldLoanRB", ProxyParameters.LANGUAGE);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    //    TermLoanRB resourceBundle = new TermLoanRB();
    //    AuthorizedSignatoryUI authSignUI = null;
    PowerOfAttorneyUI poaUI = null;
    GoldLoanOB observable;
    GoldLoanBorrowerOB observableBorrow;
//    TermLoanCompanyOB observableComp;
    GoldLoanSecurityOB observableSecurity;
    GoldLoanRepaymentOB observableRepay;
//    TermLoanGuarantorOB observableGuarantor;
    TermLoanDocumentDetailsOB observableDocument;
    GoldLoanInterestOB observableInt;
    GoldLoanClassificationOB observableClassi;
//    TermLoanOtherDetailsOB observableOtherDetails;
    TermLoanAdditionalSanctionOB observableAdditionalSanctionOB;
    LoanDisbursementUI loanDisbursementUI = null;
    LoanDisbursementOB loanDisbursementOB = null;
    //    CustomerDetailsScreenUI customerDetailsScreenUI = null;
    private Date date;
    private Date repayFromdate;
    private JTable table = null;
    private HashMap mandatoryMap;
    private static TermLoanDocumentDetailsOB termLoanDocumentDetailsOB;
    IndividualCustUI individualCustUI;
    CustomerOB customerOB;
    private boolean updateModeAuthorize = false;
    private boolean phoneNumberContained = false;
    private boolean updateModePoA = false;
    private boolean updateSanctionFacility = false;
    private boolean updateSanctionMain = false;
    private boolean updateSecurity = false;
    private boolean updateRepayment = false;
    private boolean repayNewMode = false;
    private boolean allowMultiRepay = true;
    private boolean updateGuarantor = false;
    private boolean updateDocument = false;
    private boolean updateInterest = false;
    private boolean alreadyChecked = true;
    private boolean isFilled = false;
    private boolean sanMousePress = false;
    private boolean sanValueChanged = false;
    private boolean sanDetailMousePressedForLTD = false;
    private boolean additionalSanMousePress = false;
    private boolean facilityFlag = true;
    private boolean outStandingAmtRepayment = false;
    private boolean existRecord = false;
    private boolean repayMorotoruimavailable = false;
    private HashMap accNumMap = new HashMap();
    private Date curr_dt = null;
    private String oldAccountNum = "";
    private String oldGoldLoanNo = "";
    private TransDetailsUI transDetailsUI = null;
    private HashMap renewalparamMap = new HashMap();
    int result;
    int modeAuthorize = -1;
    int rnw = 0;
    int modePoA = -1;
    int rowSanctionFacility = -1;
    int rowSanctionMain = -1;
    int rowFacilityTabSanction = -1;
    int rowFacilityTabFacility = -1;
    int rowSecurity = -1;
    int rowRepayment = -1;
    int dumRowRepay = -1;
    int rowGuarantor = -1;
    int rowDocument = -1;
    int rowInterest = -1;
    int transCount = 0;
    boolean sanction = false;
    boolean sandetail = false;
    boolean santab = false;
    boolean sanfacTab = false;
    int rowmaintab = -1;
    int rowfactab = -1;
    int rowsan = -1;
    int rowsanDetail = -1;
    final int DELETE = 1, AUTHORIZED = 2;
    private final static Logger log = Logger.getLogger(GoldLoanUI.class);
    private final String ACT = "ACT";
    private final String AUTHORIZE = "AUTHORIZE";
    //    private final        String CORPORATE = "CORPORATE";
    //    private final        String FLOATING_RATE = "FLOATING_RATE";
    //    private final        String FIXED_RATE = "FIXED_RATE";
    //    private final        String INDIVIDUAL = "INDIVIDUAL";
    //    private final        String IS_COOPERATIVE = "IS_COOPERATIVE";
    private final String JOINT_ACCOUNT = "JOINT_ACCOUNT";
    private final String LOANS_AGAINST_DEPOSITS = "LOANS_AGAINST_DEPOSITS";
    private final String REJECT = "REJECT";
    private final String EXCEPTION = "EXCEPTION";
    private final String PROD = "PROD";
    private String viewType = "";
    private String loanType = "OTHERS";
    private boolean facilitySaved = false;
    private boolean btnNewPressed = false;
    private int addSanctionPosition = -1;
    private boolean allowResetVisit = false;
    private boolean deleteInstallment = false;
    private boolean enableControls = false;
    private boolean updateRecords = false;
    private String customerScreen = "TERMLOAN_SCREEN";
    private boolean notSavedRecords = false;
    private boolean mousePressedRec = false;
    private boolean tableFlag = false;
    private String eachProdId = "";
     private String rnwEachProdId = "";
    private double totMarginAmt = 0;
    private double totEligibleAmt = 0;
    private double totSecurityValue = 0;
    private int selectedRow = -1;
    private boolean newRecordAdding = false;
    private String totalSecValue = "";
    private String totalMarginValue = "";
    private String totalEligibleValue = "";
    private boolean finalChecking = false;
    public String branchID;
    public String prodDesc = "";
    public String rnewProdDesc = "";
    HashMap map;
    private ArrayList selectedNomineeData;
    private List chargelst = null;
    final String SCREEN = "TL";
    String minNominee = "";
    private boolean transNew = true;
    Rounding rd = new Rounding();
    double perGramAmt = 0;
    double totRecivableAmt = 0;
    NomineeOB nomineeOB;
    String accountClosingCharge = null;
    boolean fromAuthorizeUI = false;
    boolean fromManagerAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
    AuthorizeListDebitUI ManagerauthorizeListUI=null;
    private int rejectFlag = 0;
    private boolean checkLimitChange = true;
    private String checkLimitChangeValue = "";
    private String renewalAccNo = "";
    DecimalFormat myFormat = new DecimalFormat("0.000");
    private AcctSearchUI acctsearch = null;
    boolean chktrans = false;
    boolean chkok=false;
    private String cr_prod_type = "";
    private boolean alreadyOpenedRecords = false;
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;  
    Webcam wc;
    boolean cameraFlag = false;
    boolean webCamOpenedForStockPhoto =  false;
    boolean webCamOpenedForStockRenewalPhoto = false;
    /**
     * Declare a new instance of the NomineeUI and IntroducerUI...
     */
    NomineeUI nomineeUi = new NomineeUI(SCREEN, false);
    
    public HashMap serviceTaxApplMap = new HashMap();// Added by nithya on 09-10-2018 for KD 263 GST - Needed to implement the GST in GOld Loan renewal
    public HashMap serviceTaxIdMap = new HashMap();//KD 263
    ServiceTaxCalculation objServiceTax;
    HashMap serviceTax_Map=new HashMap();
    private List taxListForGoldLoan = new ArrayList();   
    private final String PHOTO = "lblPhoto";
    private final String SIGN = "lblSign";
    

    /**
     * Creates new form TermLoanUI
     */
    public GoldLoanUI() {
        this.loanType = "OTHERS";

        termLoanUI();
    }

    public GoldLoanUI(String loanType) {
        this.loanType = loanType;
        termLoanUI();
    }

    public GoldLoanUI(String loanType, String ProdId) {
        this.loanType = loanType;
        this.eachProdId = ProdId;
        termLoanUI();
    }

    private void termLoanUI() {
        initComponents();
        setFieldNames();
        internationalize();
        setMaxLength();
        setObservable();
        branchID = TrueTransactMain.BRANCH_ID;
        transDetailsUI = new TransDetailsUI(panRenewalTransactionDetails);
        //        lblEligibleLoan.setVisible(false);
        //        txtEligibleLoan1.setVisible(false);
        lblMandatory_DOC.setVisible(false);
        lblExpiryDate_DOC.setVisible(false);
        panMandatory_DOC.setVisible(false);
        tdtExpiryDate_DOC.setVisible(false);
        //        lblSecurityAccHeadValue.setVisible(false);
        //        lblSecurityAccHead.setVisible(false);
        lblDocumentAccHeadValue.setVisible(false);
        lblDocumentAccHead.setVisible(false);
        //        lblSecurityProdIdValue.setText(String.valueOf(eachProdId));
        //comm by Jiby
//        lblDocumentProdIdValue.setText(String.valueOf(eachProdId));
//        observable.setLoadingProdId(String.valueOf(eachProdId));
        ///////////////////////////////////////////////////////

        //        tdtDemandPromNoteExpDate1.setVisible(false);
        //        lblDemandPromNoteExpDate1.setVisible(false);
        //        authSignUI = new AuthorizedSignatoryUI("TL");
        poaUI = new PowerOfAttorneyUI("TL");
        /**
         * To add the Nominee Tab and Introduce Tab in the AccountUI...
         */
        tabLimitAmount.add(nomineeUi, "Nominee", 2);
        nomineeUi.disableNewButton(false);
        //        tabLimitAmount.remove(panPeakSanctionDetails);
        //        settlementUI=new SettlementUI();
        //        actTransUI=new ActTransUI();
        //         agriSubSidyUI=new AgriSubSidyUI();
        //        observableBorrow.setAuthSignAndPoAOB(authSignUI.getAuthorizedSignatoryOB(), poaUI.getPowerOfAttorneyOB());
        //        observable.setExtendedOB(authSignUI.getAuthorizedSignatoryOB(), authSignUI.getAuthorizedSignatoryInstructionOB(),
        //        poaUI.getPowerOfAttorneyOB(),loanDisbursementUI.getAgriSubLimitOB());//,agriSubSidyUI.getAgriSubSidyOB(),settlementUI.getSettlementOB(),actTransUI.getActTransOB());
        //        tabLimitAmount.add(authSignUI, "Authorized Signatory", 1);
        //        tabLimitAmount.add(poaUI, "Power of Attorney", 2);
        //        tabLimitAmount.add("Other Facility Details", agriSubSidyUI);
        //        tabLimitAmount.add("Settlement",settlementUI);
        //        tabLimitAmount.add("Act Transfer",actTransUI);

        loanDisbursementUI = new LoanDisbursementUI();
        ClientUtil.enableDisable(this, false);
        allEnableDisable();
        initComponentData();
        
        if (loanType.equals("LTD")) {
            if (observable.getCbmIntGetFrom().containsElement("Product")) {
                observable.getCbmIntGetFrom().removeKeyAndElement("PROD");
            }
        } else if (!observable.getCbmIntGetFrom().containsElement("Product")) {
            observable.getCbmIntGetFrom().addKeyAndElement("PROD", "Product");
        }
        if (loanType.equals("OTHERS") && observable.getCbmTypeOfFacility().containsElement("Loans Against Deposits")) {
            //            observable.getCbmTypeOfFacility().removeElement("Loans Against Deposits");
            observable.getCbmTypeOfFacility().removeKeyAndElement(LOANS_AGAINST_DEPOSITS);
        } else if (loanType.equals("LTD") && !observable.getCbmTypeOfFacility().containsElement("Loans Against Deposits")) {
            observable.getCbmTypeOfFacility().addKeyAndElement(LOANS_AGAINST_DEPOSITS, "Loans Against Deposits");
        }
        if (loanType.equals("OTHERS") && observable.getCbmRepayFreq().containsElement("Lump Sum")) {
            observable.getCbmRepayFreq().removeKeyAndElement("1");
        } else if (!observable.getCbmRepayFreq().containsElement("Lump Sum")) {
            observable.getCbmRepayFreq().addKeyAndElement("1", "Lump Sum");
        }

        if (loanType.equals("OTHERS") && observableRepay.getCbmRepayType().containsElement("Lump Sum")) {
            observableRepay.getCbmRepayType().removeKeyAndElement("LUMP_SUM");
        } else if (!observableRepay.getCbmRepayType().containsElement("Lump Sum")) {
            observableRepay.getCbmRepayType().addKeyAndElement("LUMP_SUM", "Lump Sum");
        }

        setMandatoryHashMap();
        setHelpMessage();
        observable.resetForm();
        observable.resetStatus();
        //        authSignUI.setLblStatus(observable.getLblStatus());
        poaUI.setLblStatus(observable.getLblStatus());
        //        authSignUI.setViewType(viewType);
        //        poaUI.setViewType(viewType);
        //        tabLimitAmount.add(panSecurityDetails,"Security Details");
        tabLimitAmount.add(panDocumentDetails, "Document Details");
        //        tabLimitAmount.remove(panAccountDetails);
        tabLimitAmount.resetVisits();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panSanctionDetails_Table);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panSecurityDetails_security);
        btnDelete.setVisible(true);
        btnDelete.setEnabled(true);
        btnView.setEnabled(!btnView.isEnabled());
        curr_dt = ClientUtil.getCurrentDate();
        //        btnClosedAcc.setVisible(false);
//        curr_dt=ClientUtil.getCurrentDate();
        lblAccountNo.setVisible(false);
        txtAccountNo.setVisible(false);
        cboAccStatus.setEnabled(false);
        tdtAccountCloseDate.setEnabled(false);
        btnCustID.setEnabled(false);
//        btnAppraiserId.setEnabled(false);
        lblAppraiserNameValue.setText("");
        btnMembershipLia.setEnabled(false);
        if (loanType.equals("Renewal")) {
            btnRenew.setVisible(false);
        } else {
            btnRenew.setVisible(false);
        }
        tabLimitAmount.remove(panBorrowRenewalCompanyDetails);
        tabLimitAmount.remove(panStockDetails);
        btnSecurityAdd.setEnabled(false);
        txtNextAccNo.setText("");
        txtNextAccNo.setEnabled(false);
        btnRepayShedule.setEnabled(false);
    }
    private void btnCustomerID_GDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerID_GDActionPerformed
        // Add your handling code here:
        popUp("Guarant_Cust_Id");
    }//GEN-LAST:event_btnCustomerID_GDActionPerformed

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

    private void EMI_CalculateActionPerformed(boolean show) {
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
        if (observable.isRenewalYesNo()) {
            repayData.put("FROM_DATE", DateUtil.getDateMMDDYYYY(tdtRenewalSanctionDate.getDateValue()));
        } else {
            repayData.put("FROM_DATE", DateUtil.getDateMMDDYYYY(tdtSanctionDate.getDateValue()));
        }
        //                else
        //                    repayData.put("FROM_DATE", DateUtil.getDateMMDDYYYY(tdtFacility_Repay_Date.getDateValue()));
        //            if (txtNoInstallments.getText().equals("1"))
        //                repayData.put("FROM_DATE", tdtFromDate.getDateValue());
        if (observable.isRenewalYesNo()) {
            repayData.put("TO_DATE", DateUtil.getDateMMDDYYYY(observable.getTdtRenewalToDate()));
            repayData.put("REPAYMENT_START_DT", DateUtil.getDateMMDDYYYY(tdtRenewalSanctionDate.getDateValue()));
            repayData.put("NO_INSTALL", txtRenewalNoInstallments.getText());
            repayData.put("DURATION_YY", txtRenewalNoInstallments.getText());
            repayData.put("PRINCIPAL_AMOUNT", txtRenewalLimit_SD.getText());
            repayData.put("REPAYMENT_FREQUENCY", observable.getCbmRenewalRepayFreq().getKeyForSelected());
            //Added by nithya on 06-07-2019 for KD 546 - New Gold Loan -45 days maturity type
            if(CommonUtil.convertObjToDouble(observable.getCbmRenewalRepayFreq().getKeyForSelected()) == 0){
                HashMap marginMap = new HashMap();               
                marginMap.put("PROD_ID", ((ComboBoxModel) cboRnwGoldLoanProd.getModel()).getKeyForSelected().toString());               
                List lst = ClientUtil.executeQuery("getSelectMarginPercentage", marginMap);
                if (lst != null && lst.size() > 0) {
                   marginMap = (HashMap) lst.get(0);
                   double userDefinedRepayFreq = CommonUtil.convertObjToDouble(marginMap.get("MAX_PERIOD"));
                   repayData.put("REPAYMENT_FREQUENCY", userDefinedRepayFreq);
                   repayData.put("USER_DEFINED_GOLD_LOAN_REPAY_FREQUENCY", "USER_DEFINED_GOLD_LOAN_REPAY_FREQUENCY");
                }
            }
            // End
            repayData.put("INTEREST", txtRenewalInter.getText());
            repayData.put("REPAYMENT_TYPE", "");
            // repayData.put("REPAYMENT_TYPE", "UNIFORM_PRINCIPLE_EMI");
        } else {
            repayData.put("TO_DATE", DateUtil.getDateMMDDYYYY(tdtSanctionDate.getDateValue()));
            repayData.put("REPAYMENT_START_DT", DateUtil.getDateMMDDYYYY(tdtSanctionDate.getDateValue()));                  
            repayData.put("NO_INSTALL", txtNoInstallments.getText());
            repayData.put("DURATION_YY", txtNoInstallments.getText());
            repayData.put("PRINCIPAL_AMOUNT", txtLimit_SD.getText());
            repayData.put("REPAYMENT_FREQUENCY", observableRepay.getCbmRepayFreq_Repayment().getKeyForSelected());
            //Added by nithya on 06-07-2019 for KD 546 - New Gold Loan -45 days maturity type
            if(CommonUtil.convertObjToDouble(observableRepay.getCbmRepayFreq_Repayment().getKeyForSelected()) == 0){
                HashMap marginMap = new HashMap();
                LinkedHashMap whereMap = new LinkedHashMap();
                marginMap.put("PROD_ID", ((ComboBoxModel)cboGoldLoanProd.getModel()).getKeyForSelected().toString());
                whereMap.put("PROD_ID", ((ComboBoxModel)cboGoldLoanProd.getModel()).getKeyForSelected().toString());
                List lst = ClientUtil.executeQuery("getSelectMarginPercentage", marginMap);
                if (lst != null && lst.size() > 0) {
                   marginMap = (HashMap) lst.get(0);
                   double userDefinedRepayFreq = CommonUtil.convertObjToDouble(marginMap.get("MAX_PERIOD"));
                   repayData.put("REPAYMENT_FREQUENCY", userDefinedRepayFreq);
                   repayData.put("USER_DEFINED_GOLD_LOAN_REPAY_FREQUENCY", "USER_DEFINED_GOLD_LOAN_REPAY_FREQUENCY");
                }
            }
            // End
            repayData.put("INTEREST", txtInter.getText());
            repayData.put("REPAYMENT_TYPE", observableRepay.getCbmRepayType().getKeyForSelected());
        }


        repayData.put("ISDURATION_DDMMYY", "YES");
        //            if (rdoInterest_Compound.isSelected()){
        repayData.put("INTEREST_TYPE", "COMPOUND");
        //            }else if (rdoInterest_Simple.isSelected()){
        //                repayData.put("INTEREST_TYPE", "SIMPLE");
        //            }
        // repayData.put("DURATION_YY", txtNoInstallments.getText());
        //            date=null;
        repayData.put("COMPOUNDING_PERIOD", observableRepay.getCbmRepayFreq_Repayment().getKeyForSelected());//CommonUtil.convertObjToStr(prodLevelValues.get("DEBITINT_COMP_FREQ")));
        //                if (cboRepayType.getSelectedItem().equals("User Defined")){
        //                    repayData.put("REPAYMENT_TYPE", observableRepay.getCbmRepayType().getKeyForSelected());
        //                }else{
        if (observable.isRenewalYesNo()) {
            repayData.put("REPAYMENT_TYPE", "UNIFORM_PRINCIPLE_EMI");
        } else {
            repayData.put("REPAYMENT_TYPE", observableRepay.getCbmRepayType().getKeyForSelected());
        }
        //                }
        //            repayData.put("EMI_TYPE","UNIFORM_EMI");
        //  repayData.put("PRINCIPAL_AMOUNT", txtLimit_SD.getText());
        repayData.put("ROUNDING_FACTOR", "1_RUPEE");
        //                repayData.put("ROUNDING_TYPE", CommonUtil.convertObjToStr(prodLevelValues.get("DEBIT_INT_ROUNDOFF")));
        // repayData.put("REPAYMENT_FREQUENCY", observableRepay.getCbmRepayFreq_Repayment().getKeyForSelected());
        //  repayData.put("INTEREST", txtInter.getText());
        repayData.put(loanType, "");
        repayData.put("PROD_ID",((ComboBoxModel)cboGoldLoanProd.getModel()).getKeyForSelected().toString()); //added by shihad
        if(tdtEmiDate.getDateValue()!=null && !tdtEmiDate.getDateValue().equals("")){
            repayData.put("EMIF_DATE", DateUtil.getDateMMDDYYYY(tdtEmiDate.getDateValue()));
        }
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
            if(!show)
                new TermLoanInstallmentUI(this, repayData, false);
            else
                new TermLoanInstallmentUI(this, repayData, true).show();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (observable.isRenewalYesNo()) {
            observableRepay.addRenewalRepaymentDetails(false, observable);
        } else {
            observableRepay.addRepaymentDetails(false);
        }
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
        tdtAsOn_GDFocusLost();
    }//GEN-LAST:event_tdtAsOn_GDFocusLost
    private void calculateSanctionToDate() {        
        if (observable.isRenewalYesNo()) //        if (! (viewType.equals(AUTHORIZE)|| (viewType.equals("Edit") && observable.getClearBalance()==0) || viewType.equals(EXCEPTION) || viewType.equals(REJECT) || viewType.equals("Delete")))
        {
            if (!CommonUtil.convertObjToStr(tdtRenewalAccountOpenDate.getDateValue()).equals("") && !CommonUtil.convertObjToStr(cboRenewalRepayFreq.getSelectedItem()).equals("") && !CommonUtil.convertObjToStr(txtRenewalNoInstallments.getText()).equals("")) {
//                moratorium_Given_Calculation();
                java.util.GregorianCalendar gCalendar = new java.util.GregorianCalendar();
                java.util.GregorianCalendar gCalendarrepaydt = new java.util.GregorianCalendar(); //forrepaydate shoude change from first dt

//                    tdtRenewalAccountOpenDate.setDateValue(tdtRenewalAccountOpenDate.getDateValue());
                //gCalendar.setGregorianChange(DateUtil.getDateMMDDYYYY(tdtRenewalAccountOpenDate.getDateValue()));
                gCalendar.setTime(DateUtil.getDateMMDDYYYY(tdtRenewalAccountOpenDate.getDateValue()));
                //gCalendarrepaydt.setGregorianChange(DateUtil.getDateMMDDYYYY(tdtRenewalAccountOpenDate.getDateValue()));
                gCalendarrepaydt.setTime(DateUtil.getDateMMDDYYYY(tdtRenewalAccountOpenDate.getDateValue()));
                int dateVal = observable.getIncrementType();
                int incVal = observable.getInstallNo(txtNoInstallments.getText(), dateVal);
                date = new java.util.Date();
                date = DateUtil.getDateMMDDYYYY(tdtRenewalAccountOpenDate.getDateValue());
                if (txtNoInstallments.getText().equals("1")) {
                    date = DateUtil.getDateMMDDYYYY(tdtRenewalAccountOpenDate.getDateValue());
                }
                if (cboRenewalRepayFreq.getSelectedItem().equals("USER_DEFINED_GOLD_LOAN") && observable.getMaximumDaysForRenewLoan() > 0) {//Added by nithya on 06-07-2019 for KD 546 - New Gold Loan -45 days maturity type
                    dateVal = observable.getMaximumDaysForRenewLoan();
                    gCalendar.add(gCalendar.DATE, dateVal);
                } else {
                    if (dateVal <= 7) {
                        gCalendar.add(gCalendar.DATE, incVal);
                    } else if (dateVal > 7 && dateVal < 30) {
                        gCalendar.add(gCalendar.DATE, dateVal);
                    } else if (dateVal >= 30) {
                        gCalendar.add(gCalendar.MONTH, incVal);
                        int firstInstall = dateVal / 30;
                        gCalendarrepaydt.add(gCalendarrepaydt.MONTH, firstInstall);//for repaydate
                    }
                }
//                tdtTDate.setDateValue(DateUtil.getStringDate(gCalendar.getTime()));

                observable.setTdtRenewalToDate(DateUtil.getStringDate(gCalendar.getTime()));
                observableRepay.setTdtRenewalLastInstall(DateUtil.getStringDate(gCalendar.getTime()));
                observableRepay.setTdtRenewalfirstInstall(DateUtil.getStringDate(gCalendar.getTime()));
                //Added BY Suresh
//                if(CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")){
//                    HashMap whereMap = new HashMap();
//                    if((observable.getActionType()==ClientConstants.ACTIONTYPE_NEW) || (observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT)){
////                        tdtFDate.setEnabled(false);
////                        tdtFacility_Repay_Date.setEnabled(true);
//                    }
//              
//                }else{
//                    if((observable.getActionType()==ClientConstants.ACTIONTYPE_NEW) || (observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT)){
////                        tdtFDate.setEnabled(true);
////                        tdtFacility_Repay_Date.setEnabled(false);
//                    }
//                }
//                tdtFirstInstall.setDateValue(tdtFacility_Repay_Date.getDateValue());//repay
//                tdtFacility_Repay_Date.setDateValue(DateUtil.getStringDate(gCalendarrepaydt.getTime()));
//                observable.setTdtFacility_Repay_Date(tdtFacility_Repay_Date.getDateValue()); //for repaydate
                gCalendar = null;
                gCalendarrepaydt = null;
            } else {
//                tdtTDate.setDateValue("");
//                observable.setTdtTDate("");
//                tdtFacility_Repay_Date.setDateValue("");
//                observable.setTdtFacility_Repay_Date("");
//                observable.setTxtPeriodDifference_Days("");
//                observable.setTxtPeriodDifference_Months("");
//                observable.setTxtPeriodDifference_Years("");
//                updatePeriodDifference();
            }
        }
    }

    private void allEnableDisable() {
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

    private void disableFields() {
        txtCustID.setEditable(true);
        disableLastIntApplDate();
    }

    private void setObservable() {
        try {
            //            observable = GoldLoanOB.getInstance();            //Changed By Suresh
            //            observableBorrow = GoldLoanBorrowerOB.getInstance();
            //            observableComp = TermLoanCompanyOB.getInstance();
            //            observableSecurity = GoldLoanSecurityOB.getInstance();
            //            observableInt = GoldLoanInterestOB.getInstance();
            //            observableGuarantor = TermLoanGuarantorOB.getInstance();
            //            observableOtherDetails = TermLoanOtherDetailsOB.getInstance();
            observableDocument = TermLoanDocumentDetailsOB.getInstance();
            //            observableClassi = GoldLoanClassificationOB.getInstance();
            observable = new GoldLoanOB();
            observable.addObserver(this);
            observable.setLoanType(loanType);
            observableBorrow = observable.getTermLoanBorrowerOB();
            observableBorrow.addObserver(this);
//            observableComp = observable.getTermLoanCompanyOB();
//            observableComp.addObserver(this);
            observableSecurity = observable.getTermLoanSecurityOB();
            observableSecurity.addObserver(this);
            observableRepay = observable.getTermLoanRepaymentOB();
            observableRepay.addObserver(this);
            observableInt = observable.getTermLoanInterestOB();
            observableInt.addObserver(this);
//            observableGuarantor = observable.getTermLoanGuarantorOB();
//            observableGuarantor.addObserver(this);
//            observableOtherDetails = observable.getTermLoanOtherDetailsOB();
//            observableOtherDetails.addObserver(this);
//            observableDocument = observable.getTermLoanDocumentDetailsOB();
//            observableDocument.addObserver(this);
            observableClassi = observable.getTermLoanClassificationOB();
            observableClassi.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("TermLoanOB..." + e);
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
        mandatoryMap.put("tdtAccountOpenDate", new Boolean(true));
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
        final GoldLoanMRB objMandatoryRB = new GoldLoanMRB();
        txtCustID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCustID"));
        cboAccStatus.setHelpMessage(lblMsg, objMandatoryRB.getString("cboAccStatus"));
        cboConstitution.setHelpMessage(lblMsg, objMandatoryRB.getString("cboConstitution"));
        cboCategory.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCategory"));
        tdtDealingWithBankSince.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDealingWithBankSince"));
        tdtSanctionDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtSanctionDate"));
        cboSanctioningAuthority.setHelpMessage(lblMsg, objMandatoryRB.getString("cboSanctioningAuthority"));
        txtSanctionRemarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSanctionRemarks"));
        txtNoInstallments.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoInstallments"));
        cboRepayFreq.setHelpMessage(lblMsg, objMandatoryRB.getString("cboRepayFreq"));
        txtLimit_SD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLimit_SD"));
        tdtDemandPromNoteDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDemandPromNoteDate"));
        tdtDemandPromNoteExpDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDemandPromNoteExpDate"));
        txtInter.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInter"));
        txtPenalInter.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPenalInter"));
        tdtSubmitDate_DocumentDetails.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtSubmitDate_DocumentDetails"));
        txtRemarks_DocumentDetails.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRemarks_DocumentDetails"));
        rdoYes_DocumentDetails.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoYes_DocumentDetails"));
        rdoNo_DocumentDetails.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoNo_DocumentDetails"));
        rdoYes_Executed_DOC.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoYes_Executed_DOC"));
        rdoNo_Executed_DOC.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoNo_Executed_DOC"));
        rdoYes_Mandatory_DOC.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoYes_Mandatory_DOC"));
        rdoNo_Mandatory_DOC.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoNo_Mandatory_DOC"));
        tdtExecuteDate_DOC.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtExecuteDate_DOC"));
        tdtExpiryDate_DOC.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtExpiryDate_DOC"));
        tdtAccountOpenDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtAccountOpenDate"));

    }

    private void setFieldNames() {
        lblExpiryDate_DOC.setName("lblExpiryDate_DOC");
        tdtExpiryDate_DOC.setName("tdtExpiryDate_DOC");
        lblExecuteDate_DOC.setName("lblExecuteDate_DOC");
        tdtExecuteDate_DOC.setName("tdtExecuteDate_DOC");
        lblExecuted_DOC.setName("lblExecuted_DOC");
        rdoNo_Executed_DOC.setName("rdoNo_Executed_DOC");
        rdoYes_Executed_DOC.setName("rdoYes_Executed_DOC");
        lblMandatory_DOC.setName("lblMandatory_DOC");
        rdoNo_Mandatory_DOC.setName("rdoNo_Mandatory_DOC");
        rdoYes_Mandatory_DOC.setName("rdoYes_Mandatory_DOC");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnCustID.setName("btnCustID");
        btnNew_Borrower.setName("btnNew_Borrower");
        btnDeleteBorrower.setName("btnDeleteBorrower");
        btnToMain_Borrower.setName("btnToMain_Borrower");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnSave.setName("btnSave");
        btnAuthorize.setName("btnAuthorize");
        btnReject.setName("btnReject");
        btnException.setName("btnException");
        btnSave_DocumentDetails.setName("btnSave_DocumentDetails");
        lblDocDesc_Disp_DocumentDetails.setName("lblDocDesc_Disp_DocumentDetails");
        lblDocDesc_DocumentDetails.setName("lblDocDesc_DocumentDetails");
        lblDocNo_Disp_DocumentDetails.setName("lblDocNo_Disp_DocumentDetails");
        lblDocNo_DocumentDetails.setName("lblDocNo_DocumentDetails");
        lblDocType_Disp_DocumentDetails.setName("lblDocType_Disp_DocumentDetails");
        lblDocType_DocumentDetails.setName("lblDocType_DocumentDetails");
        lblSubmitDate_DocumentDetails.setName("lblSubmitDate_DocumentDetails");
        lblSubmitted_DocumentDetails.setName("lblSubmitted_DocumentDetails");
        lblRemarks_DocumentDetails.setName("lblRemarks_DocumentDetails");
        txtRemarks_DocumentDetails.setName("txtRemarks_DocumentDetails");
        tdtSubmitDate_DocumentDetails.setName("tdtSubmitDate_DocumentDetails");
        rdoYes_DocumentDetails.setName("rdoYes_DocumentDetails");
        rdoNo_DocumentDetails.setName("rdoNo_DocumentDetails");
        srpTable_DocumentDetails.setName("srpTable_DocumentDetails");
        panTable_DocumentDetails.setName("panTable_DocumentDetails");
        tblTable_DocumentDetails.setName("tblTable_DocumentDetails");
        panSubmitted_DocumentDetails.setName("panSubmitted_DocumentDetails");
        cboAccStatus.setName("cboAccStatus");
        cboCategory.setName("cboCategory");
        cboConstitution.setName("cboConstitution");
        cboRepayFreq.setName("cboRepayFreq");
        cboSanctioningAuthority.setName("cboSanctioningAuthority");
        lblAccStatus.setName("lblAccStatus");
        lblAcctNo_Sanction_Disp.setName("lblAcctNo_Sanction_Disp");
        lblAcctNo_Sanction.setName("lblAcctNo_Sanction");
        lblCategory.setName("lblCategory");
        lblConstitution.setName("lblConstitution");
        lblCustID.setName("lblCustID");
        //        lblCustName.setName("lblCustName");
        lblDealingWithBankSince.setName("lblDealingWithBankSince");
        lblDemandPromNoteDate.setName("lblDemandPromNoteDate");
        lblDemandPromNoteExpDate.setName("lblDemandPromNoteExpDate");
        lblLimit_SD.setName("lblLimit_SD");
        lblMsg.setName("lblMsg");
        lblNoInstallments.setName("lblNoInstallments");
        lblPenalInter.setName("lblPenalInter");
        lblRemarks.setName("lblRemarks");
        lblRepayFreq.setName("lblRepayFreq");
        lblSanctionDate.setName("lblSanctionDate");
        lblSanctioningAuthority.setName("lblSanctioningAuthority");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblStatus.setName("lblStatus");
        lblAccOpenDt.setName("lblAccOpenDt");
        lblAccCloseDt.setName("lblAccCloseDt");
        mbrTermLoan.setName("mbrTermLoan");
        panBorrowerTabCTable.setName("panBorrowerTabCTable");
        panBorrowerTabTools.setName("panBorrowerTabTools");
        panTermLoan.setName("panTermLoan");
        panBorrowCompanyDetails.setName("panBorrowCompanyDetails");
        panBorrowProfile.setName("panBorrowProfile");
        panBorrowProfile_CustID.setName("panBorrowProfile_CustID");
        panBorrowProfile_CustName.setName("panBorrowProfile_CustName");
        panDemandPromssoryDate.setName("panDemandPromssoryDate");
        panDocumentDetails.setName("panDocumentDetails");
        panSanctionDetails_Table.setName("panSanctionDetails_Table");
        panSanctionDetails_Sanction.setName("panSanctionDetails_Sanction");
        panSanctionDetails_Table.setName("panSanctionDetails_Table");
        panSecurityDetails_security.setName("panSecurityDetails_security");
        panStatus.setName("panStatus");
        panTableFields_SD.setName("panTableFields_SD");
        srpBorrowerTabCTable.setName("srpBorrowerTabCTable");
        tabLimitAmount.setName("tabLimitAmount");
        tblBorrowerTabCTable.setName("tblBorrowerTabCTable");
        tdtDealingWithBankSince.setName("tdtDealingWithBankSince");
        tdtDemandPromNoteDate.setName("tdtDemandPromNoteDate");
        tdtDemandPromNoteExpDate.setName("tdtDemandPromNoteExpDate");
        tdtSanctionDate.setName("tdtSanctionDate");
        txtCustID.setName("txtCustID");
        txtInter.setName("txtInter");
        txtLimit_SD.setName("txtLimit_SD");
        txtNoInstallments.setName("txtNoInstallments");
        txtPenalInter.setName("txtPenalInter");
        txtSanctionRemarks.setName("txtSanctionRemarks");
        tdtAccountOpenDate.setName("tdtAccountOpenDate");
        tdtAccountCloseDate.setName("tdtAccountCloseDate");
        //mobile banking
        panMobileBanking.setName("panMobileBanking");
        chkMobileBankingTLAD.setName("chkMobileBankingTLAD");
        lblMobileNo.setName("lblMobileNo");
        txtMobileNo.setName("txtMobileNo");
        lblMobileSubscribedFrom.setName("lblMobileSubscribedFrom");
        tdtMobileSubscribedFrom.setName("tdtMobileSubscribedFrom");



        panRenewalSanctionDetails_Sanction.setName("panRenewalSanctionDetails_Sanction");
        lblRenewalAcctNo_Sanction.setName("lblRenewalAcctNo_Sanction");
        lblRenewalAcctNo_Sanction_Disp.setName("lblRenewalAcctNo_Sanction_Disp");
        lblRenewalSanctionDate.setName("lblRenewalSanctionDate");
        tdtRenewalSanctionDate.setName("tdtRenewalSanctionDate");
        lblRenewalSanctioningAuthority.setName("lblRenewalSanctioningAuthority");
        cboRenewalSanctioningAuthority.setName("cboRenewalSanctioningAuthority");
        rdoRenewalPriority.setName("rdoRenewalPriority");
        rdoRenewalNonPriority.setName("rdoRenewalNonPriority");
        lblRenewalPurposeOfLoan.setName("lblRenewalPurposeOfLoan");
        cboRenewalPurposeOfLoan.setName("cboRenewalPurposeOfLoan");
        lblRenewalRemarks.setName("lblRenewalRemarks");
        txtRenewalSanctionRemarks.setName("txtRenewalSanctionRemarks");
        panRenewalInterestDeails.setName("panRenewalInterestDeails");
        lblRenewalLimit_SD.setName("lblRenewalLimit_SD");
        txtRenewalLimit_SD.setName("txtRenewalLimit_SD");
        lblRenewalAccOpenDt.setName("lblRenewalAccOpenDt");
        tdtRenewalAccountOpenDate.setName("tdtRenewalAccountOpenDate");
        lblRenewalAccStatus.setName("lblRenewalAccStatus");
        cboRenewalAccStatus.setName("cboRenewalAccStatus");
        lblRenewalNoInstallments.setName("lblRenewalNoInstallments");
        txtRenewalNoInstallments.setName("txtRenewalNoInstallments");
        lblRenewalRepayFreq.setName("lblRenewalRepayFreq");
        cboRenewalRepayFreq.setName("cboRenewalRepayFreq");
        panRenewalDemandPromssoryDate.setName("panRenewalDemandPromssoryDate");
        lblRenewalDemandPromNoteDate.setName("lblRenewalDemandPromNoteDate");
        lblRenewalDemandPromNoteExpDate.setName("lblRenewalDemandPromNoteExpDate");
        tdtRenewalDemandPromNoteExpDate.setName("tdtRenewalDemandPromNoteExpDate");
        panRenewalInterestDeails2.setName("panRenewalInterestDeails2");
        lblRenewalInter.setName("lblRenewalInter");
        txtRenewalInter.setName("txtRenewalInter");
        lblRenewalPenalInter.setName("lblRenewalPenalInter");
        txtRenewalPenalInter.setName("txtRenewalPenalInter");
        panRenewalSecurityDetails_security.setName("panRenewalSecurityDetails_security");
        lblRenewalGrossWeight.setName("lblRenewalGrossWeight");
        txtRenewalGrossWeight.setName("txtRenewalGrossWeight");
        lblRenewalNetWeight.setName("lblRenewalNetWeight");
        txtRenewalNetWeight.setName("txtRenewalNetWeight");
        lblRenewalpurity.setName("lblRenewalpurity");
        cboRenewalPurityOfGold.setName("cboRenewalPurityOfGold");
        lblRenewalMarketRate.setName("lblRenewalMarketRate");
        txtRenewalMarketRate.setName("txtRenewalMarketRate");
        lblRenewalSecurityValue.setName("lblRenewalSecurityValue");
        txtRenewalSecurityValue.setName("txtRenewalSecurityValue");
        lblRenewalParticulars.setName("lblRenewalParticulars");
        txtRenewalAreaParticular.setName("txtRenewalAreaParticular");
        lblRenewalMargin.setName("lblRenewalMargin");
        txtRenewalMargin.setName("txtRenewalMargin");
        lblRenewalMarginAmt.setName("lblRenewalMarginAmt");
        txtRenewalMarginAmt.setName("txtRenewalMarginAmt");
        lblRenewalMaxLoanAmt.setName("lblRenewalMaxLoanAmt");
        txtRenewalEligibleLoan.setName("txtRenewalEligibleLoan");
        lblRenewalAppraiserId.setName("lblRenewalAppraiserId");
        cboRenewalAppraiserId.setName("cboRenewalAppraiserId");
        lblRenewalAppraiserName.setName("lblRenewalAppraiserName");
        lblRenewalAppraiserNameValue.setName("lblRenewalAppraiserNameValue");
        panRenewalChargeDetails.setName("panRenewalChargeDetails");
        lblItem.setName("lblItem");
        cboItem.setName("cboItem");
        lblQty.setName("lblQty");
        txtQty.setName("txtQty");
        lblSecurityRemarks.setName("lblSecurityRemarks");
        txtSecurityRemarks.setName("txtSecurityRemarks");
        btnSecurityAdd.setName("btnSecurityAdd");
        lblNoOfPacket.setName("lblNoOfPacket");
        txtNoOfPacket.setName("txtNoOfPacket");
        panItemSecurity.setName("panItemSecurity");
        txtCustMobNo.setName("txtCustMobNo");
    }

    private void internationalize() {
        lblExecuteDate_DOC.setText(resourceBundle.getString("lblExecuteDate_DOC"));
        lblExecuted_DOC.setText(resourceBundle.getString("lblExecuted_DOC"));
        lblExpiryDate_DOC.setText(resourceBundle.getString("lblExpiryDate_DOC"));
        lblMandatory_DOC.setText(resourceBundle.getString("lblMandatory_DOC"));
        rdoYes_Executed_DOC.setText(resourceBundle.getString("rdoYes_Executed_DOC"));
        rdoNo_Executed_DOC.setText(resourceBundle.getString("rdoNo_Executed_DOC"));
        rdoYes_Mandatory_DOC.setText(resourceBundle.getString("rdoYes_Mandatory_DOC"));
        rdoNo_Mandatory_DOC.setText(resourceBundle.getString("rdoNo_Mandatory_DOC"));
        lblAccStatus.setText(resourceBundle.getString("lblAccStatus"));
        //        lblCustName.setText(resourceBundle.getString("lblCustName"));
        lblCategory.setText(resourceBundle.getString("lblCategory"));
        lblCustID.setText(resourceBundle.getString("lblCustID"));
        lblPenalInter.setText(resourceBundle.getString("lblPenalInter"));
        lblAcctNo_Sanction_Disp.setText(resourceBundle.getString("lblAcctNo_Sanction_Disp"));
        lblAcctNo_Sanction.setText(resourceBundle.getString("lblAcctNo_Sanction"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblDemandPromNoteExpDate.setText(resourceBundle.getString("lblDemandPromNoteExpDate"));
        lblSanctionDate.setText(resourceBundle.getString("lblSanctionDate"));
        lblDemandPromNoteDate.setText(resourceBundle.getString("lblDemandPromNoteDate"));
        lblSanctioningAuthority.setText(resourceBundle.getString("lblSanctioningAuthority"));
        lblRepayFreq.setText(resourceBundle.getString("lblRepayFreq"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        //        btnSecurityNew.setText(resourceBundle.getString("btnSecurityNew"));
        ((javax.swing.border.TitledBorder) panBorrowProfile.getBorder()).setTitle(resourceBundle.getString("panBorrowProfile"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        //        lblSanctionNo.setText(resourceBundle.getString("lblSanctionNo"));
        btnCustID.setText(resourceBundle.getString("btnCustID"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblLimit_SD.setText(resourceBundle.getString("lblLimit_SD"));
        //        lblModeSanction.setText(resourceBundle.getString("lblModeSanction"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblNoInstallments.setText(resourceBundle.getString("lblNoInstallments"));
        lblInter.setText(resourceBundle.getString("lblInter"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        //        btnSecuritySave.setText(resourceBundle.getString("btnSecuritySave"));
        lblDealingWithBankSince.setText(resourceBundle.getString("lblDealingWithBankSince"));
        lblConstitution.setText(resourceBundle.getString("lblConstitution"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblRemarks.setText(resourceBundle.getString("lblRemarks"));
        ((javax.swing.border.TitledBorder) panDemandPromssoryDate.getBorder()).setTitle(resourceBundle.getString("panDemandPromssoryDate"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        btnNew_Borrower.setText(resourceBundle.getString("btnNew_Borrower"));
        btnToMain_Borrower.setText(resourceBundle.getString("btnToMain_Borrower"));
        btnDeleteBorrower.setText(resourceBundle.getString("btnDelete_Borrower"));
        //        lblPenalInterestValueIN.setText(resourceBundle.getString("lblPenalInterestValueIN"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        //        chkNonMainMinBalChrgAD.setText(resourceBundle.getString("chkNonMainMinBalChrgAD"));
        //        lblAgClearingIN.setText(resourceBundle.getString("lblAgClearingIN"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        btnException.setText(resourceBundle.getString("btnException"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        //        lblCustName.setText(resourceBundle.getString("lblCustName"));
        lblAccOpenDt.setText(resourceBundle.getString("lblAccOpenDt"));
        lblAccCloseDt.setText(resourceBundle.getString("lblAccCloseDt"));

        lblRenewalAcctNo_Sanction.setText(resourceBundle.getString("lblRenewalAcctNo_Sanction"));
        lblRenewalAcctNo_Sanction_Disp.setText(resourceBundle.getString("lblRenewalAcctNo_Sanction_Disp"));
        lblRenewalSanctionDate.setText(resourceBundle.getString("lblRenewalSanctionDate"));
        lblRenewalSanctioningAuthority.setText(resourceBundle.getString("lblRenewalSanctioningAuthority"));
        rdoRenewalPriority.setText(resourceBundle.getString("rdoRenewalPriority"));
        rdoRenewalNonPriority.setText(resourceBundle.getString("rdoRenewalNonPriority"));
        lblRenewalPurposeOfLoan.setText(resourceBundle.getString("lblRenewalPurposeOfLoan"));
        lblRenewalRemarks.setText(resourceBundle.getString("lblRenewalRemarks"));
        lblRenewalLimit_SD.setText(resourceBundle.getString("lblRenewalLimit_SD"));
        lblRenewalAccOpenDt.setText(resourceBundle.getString("lblRenewalAccOpenDt"));
        lblRenewalAccStatus.setText(resourceBundle.getString("lblRenewalAccStatus"));
        lblRenewalNoInstallments.setText(resourceBundle.getString("lblRenewalNoInstallments"));
        lblRenewalRepayFreq.setText(resourceBundle.getString("lblRenewalRepayFreq"));
        lblRenewalDemandPromNoteDate.setText(resourceBundle.getString("lblRenewalDemandPromNoteDate"));
        lblRenewalDemandPromNoteExpDate.setText(resourceBundle.getString("lblRenewalDemandPromNoteExpDate"));
        lblRenewalInter.setText(resourceBundle.getString("lblRenewalInter"));
        lblRenewalPenalInter.setText(resourceBundle.getString("lblRenewalPenalInter"));
        lblRenewalGrossWeight.setText(resourceBundle.getString("lblRenewalGrossWeight"));
        lblRenewalNetWeight.setText(resourceBundle.getString("lblRenewalNetWeight"));
        lblRenewalpurity.setText(resourceBundle.getString("lblRenewalpurity"));
        lblRenewalMarketRate.setText(resourceBundle.getString("lblRenewalMarketRate"));
        lblRenewalSecurityValue.setText(resourceBundle.getString("lblRenewalSecurityValue"));
        lblRenewalParticulars.setText(resourceBundle.getString("lblRenewalParticulars"));
        lblRenewalMargin.setText(resourceBundle.getString("lblRenewalMargin"));
        lblRenewalMarginAmt.setText(resourceBundle.getString("lblRenewalMarginAmt"));
        lblRenewalMaxLoanAmt.setText(resourceBundle.getString("lblRenewalMaxLoanAmt"));
        lblRenewalAppraiserId.setText(resourceBundle.getString("lblRenewalAppraiserId"));
        lblRenewalAppraiserName.setText(resourceBundle.getString("lblRenewalAppraiserName"));
        lblRenewalAppraiserNameValue.setText(resourceBundle.getString("lblRenewalAppraiserNameValue"));

        lblItem.setText(resourceBundle.getString("lblItem"));
        lblQty.setText(resourceBundle.getString("lblQty"));
        lblSecurityRemarks.setText(resourceBundle.getString("lblSecurityRemarks"));
        txtSecurityRemarks.setName("txtSecurityRemarks");
        btnSecurityAdd.setText(resourceBundle.getString("btnSecurityAdd"));
        lblNoOfPacket.setText(resourceBundle.getString("lblNoOfPacket"));

    }
    private String getLockDetails(String lockedBy, String screenId) {
        HashMap map = new HashMap();
        StringBuffer data = new StringBuffer();
        map.put("LOCKED_BY", lockedBy);
        map.put("SCREEN_ID", screenId);
        java.util.List lstLock = ClientUtil.executeQuery("getLockedDetails", map);
        map.clear();
        if (lstLock != null && lstLock.size() > 0) {
            map = (HashMap) (lstLock.get(0));
            data.append("\nLog in Time : ").append(map.get("LOCKED_TIME"));
            data.append("\nIP Address : ").append(map.get("IP_ADDR"));
            data.append("\nBranch : ").append(map.get("BRANCH_ID"));
        }
        lstLock = null;
        map = null;
        return data.toString();
    }
    private boolean whenTableRowSelected(HashMap paramMap) {
        boolean lock = false;
        String lockedBy = "";
        HashMap map = new HashMap();
        map.put("SCREEN_ID", getScreenID());
        map.put("RECORD_KEY", paramMap.get("ACCT_NUM"));
        map.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        map.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        map.put("CUR_DATE", curr_dt.clone());
        List lstLock = ClientUtil.executeQuery("selectEditLock", map);
        if (lstLock != null && lstLock.size() > 0) {
            lockedBy = CommonUtil.convertObjToStr(lstLock.get(0));
            if (!lockedBy.equals(ProxyParameters.USER_ID)) {
                btnSave.setEnabled(false);
            } else {
                btnSave.setEnabled(true);
            }
        } else {
            btnSave.setEnabled(true);
        }
        setOpenForEditBy(lockedBy);
        if (lockedBy.equals("")) {
            ClientUtil.execute("insertEditLock", map);
        }
        if (lockedBy.length() > 0 && !lockedBy.equals(ProxyParameters.USER_ID)) {
            String data = getLockDetails(lockedBy, getScreenID());
            ClientUtil.showMessageWindow("Selected Record is Opened/Modified by " + lockedBy + data.toString());
            alreadyOpenedRecords = true;
            btnSave.setEnabled(false);
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
                lock = true;
            } else {
                lock = false;
            }
        }
        return lock;
    }

    private void removeRadioButtons() {
        //        removeFacilityRadioBtns();
        removeDocumentRadioBtns();
        //        removeGuarantorRadioBtns();
        removeRenewalPriority();
    }

    private void removeDocumentRadioBtns() {
        removeDocSubmittRadioBtns();
        removeDocExecuteRadioBtns();
        removeDocMandatoryRadioBtns();
    }

    private void removeRenewalPriority() {
        rdoRenewalPriorityGroup.remove(rdoRenewalPriority);
        rdoRenewalPriorityGroup.remove(rdoRenewalNonPriority);
    }

    private void removeDocSubmittRadioBtns() {
        rdoIsSubmitted_DocumentDetails.remove(rdoYes_DocumentDetails);
        rdoIsSubmitted_DocumentDetails.remove(rdoNo_DocumentDetails);
    }

    private void removeDocExecuteRadioBtns() {
        rdoExecuted_DOC.remove(rdoYes_Executed_DOC);
        rdoExecuted_DOC.remove(rdoNo_Executed_DOC);
    }

    private void removeDocMandatoryRadioBtns() {
        rdoMandatory_DOC.remove(rdoYes_Mandatory_DOC);
        rdoMandatory_DOC.remove(rdoNo_Mandatory_DOC);
    }

    public void update(Observable observed, Object arg) {
        removeRadioButtons();
        lblStatus.setText(observable.getLblStatus());
        txtCustMobNo.setText(observableBorrow.getTxtCustomerPhoneNumber());
        //        lblProdID_Disp_ODetails.setText(observableOtherDetails.getLblProdID_Disp_ODetails());
        //        lblAcctHead_Disp_ODetails.setText(observableOtherDetails.getLblAcctHead_Disp_ODetails());
        //        lblAcctNo_Disp_ODetails.setText(observableOtherDetails.getLblAcctNo_Disp_ODetails());
        lblAcctNo_Sanction_Disp.setText(observable.getStrACNumber());
        lblDocumentAccNoValue.setText(observable.getStrACNumber());
        //        lblSecurityAccNoValue.setText(observable.getStrACNumber());
        txtCustID.setText(observableBorrow.getTxtCustID());
        nomineeUi.setMainCustomerId(txtCustID.getText());
        lblCustNameValue.setText(observableBorrow.getLblCustNameValue());
        //        lblSecurityCustNameValue.setText(observableBorrow.getLblCustNameValue());
        lblDocumentCustNameValue.setText(observableBorrow.getLblCustNameValue());
        cboConstitution.setSelectedItem(observableBorrow.getCboConstitution());
        cboCategory.setSelectedItem(observableBorrow.getCboCategory());
//        tdtDealingWithBankSince.setDateValue(observableComp.getTdtDealingWithBankSince());
        cboAccStatus.setSelectedItem(observable.getCboAccStatus());
        //        txtSanctionNo.setText(observable.getTxtSanctionNo());
        tdtSanctionDate.setDateValue(observable.getTdtSanctionDate());
        cboSanctioningAuthority.setSelectedItem(observable.getCboSanctioningAuthority());
        txtSanctionRemarks.setText(observable.getTxtSanctionRemarks());
        //        cboModeSanction.setSelectedItem(observable.getCboModeSanction());
        txtLimit_SD.setText(observable.getTxtLimit_SD());
        tdtDemandPromNoteDate.setDateValue(observable.getTdtDemandPromNoteDate());
        tdtDemandPromNoteExpDate.setDateValue(observable.getTdtDemandPromNoteExpDate());

        lblDocDesc_Disp_DocumentDetails.setText(observableDocument.getLblDocDesc_Disp_DocumentDetails());
        lblDocNo_Disp_DocumentDetails.setText(observableDocument.getLblDocNo_Disp_DocumentDetails());
        lblDocType_Disp_DocumentDetails.setText(observableDocument.getLblDocType_Disp_DocumentDetails());
        txtRemarks_DocumentDetails.setText(observableDocument.getTxtRemarks_DocumentDetails());
        tdtSubmitDate_DocumentDetails.setDateValue(observableDocument.getTdtSubmitDate_DocumentDetails());
        rdoYes_DocumentDetails.setSelected(observableDocument.getRdoYes_DocumentDetails());
        rdoNo_DocumentDetails.setSelected(observableDocument.getRdoNo_DocumentDetails());
        rdoYes_Executed_DOC.setSelected(observableDocument.getRdoYes_Executed_DOC());
        rdoNo_Executed_DOC.setSelected(observableDocument.getRdoNo_Executed_DOC());
        rdoYes_Mandatory_DOC.setSelected(observableDocument.getRdoYes_Mandatory_DOC());
        rdoNo_Mandatory_DOC.setSelected(observableDocument.getRdoNo_Mandatory_DOC());
        tdtExecuteDate_DOC.setDateValue(observableDocument.getTdtExecuteDate_DOC());
        tdtExpiryDate_DOC.setDateValue(observableDocument.getTdtExpiryDate_DOC());
        txtInter.setText(observableInt.getTxtInter());
        txtPenalInter.setText(observableInt.getPenalInter());
        tblBorrowerTabCTable.setModel(observableBorrow.getTblBorrower());
        //        tblSecurityTable.setModel(observableSecurity.getTblSecurityTab());
        tblTable_DocumentDetails.setModel(observableDocument.getTblDocumentTab());
        tdtAccountOpenDate.setDateValue(observable.getAccountOpenDate());
        tdtAccountCloseDate.setDateValue(observable.getAccountCloseDate());
        cboPurposeOfLoan.setSelectedItem(observableClassi.getCboPurposeCode());
        txtNoInstallments.setText(observableRepay.getTxtNoInstall());
        cboRepayFreq.setSelectedItem(observable.getCboRepayFreq());

        txtPenalInter.setText(observable.getPenalInterest());
        txtInter.setText(observable.getInterest());
        //        lblSecurityAccHeadValue.setText(observable.getSecurityAccHeadValue());
        lblDocumentAccHeadValue.setText(observable.getSecurityAccHeadValue());

        //Security details
        rdoNonPriority.setSelected(observableClassi.getRdoNonPriority());
        rdoPriority.setSelected(observableClassi.getRdoPriority());
        //        lblSLNoValue.setText(observableSecurity.getSlNo());
        //        tdtAson.setDateValue(observableSecurity.getTdtAson());
        txtGrossWeight.setText(observableSecurity.getTxtGrossWeight());
        txtNetWeight.setText(observableSecurity.getTxtNetWeight());
        cboPurityOfGold.setSelectedItem(observableSecurity.getCboPurityOfGold());
        txtMarketRate.setText(observableSecurity.getTxtMarketRate());
        txtSecurityValue.setText(observableSecurity.getSecurityValue());
        //        txtAvalSecVal.setText(observableSecurity.getTotalSecurityValue());
        txtMargin.setText(observableSecurity.getTxtMargin());
        txtMarginAmt.setText(observableSecurity.getTotalMarginAmt());
        txtEligibleLoan.setText(observableSecurity.getTotalEligibleLoanAmt());
        txtNoOfPacket.setText(observableSecurity.getTxtNoOfPacket());
        //        txtAppraiserId.setText(observableSecurity.getTxtAppraiserId());
//        cboAppraiserId.getModel().).setMod.setSelectedItem(observableSecurity.getTxtAppraiserId());
    //    cboAppraiserId.setSelectedItem(CommonUtil.convertObjToStr(observableSecurity.getTxtAppraiserId()));
        (((ComboBoxModel) (cboAppraiserId).getModel())).setKeyForSelected(CommonUtil.convertObjToStr(observableSecurity.getTxtAppraiserId()));
        lblAppraiserNameValue.setText(observableSecurity.getLblAppraiserNameValue());
        txtAreaParticular.setText(observableSecurity.getTxtAreaParticular());
        if (viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT) || viewType.equals("Delete")) {
            txtNoInstallmentsFocusLost();
            cboProductIDActionPerformed();
        }
        if (allowResetVisit) {
            tabLimitAmount.resetVisits();
        }
        addRadioButtons();
        allowResetVisit = false;
        chkMobileBankingTLAD.setSelected(observable.getIsMobileBanking());
        txtMobileNo.setText(observable.getTxtMobileNo());
        tdtMobileSubscribedFrom.setDateValue(observable.getTdtMobileSubscribedFrom());

        // gold loan renewal 
        //lblRenewalAcctNo_Sanction_Disp     
        tdtRenewalSanctionDate.setDateValue(observable.getTdtRenewalSanctionDate());
        cboRenewalSanctioningAuthority.setSelectedItem(observable.getCboRenewalSanctioningAuthority());
        rdoRenewalPriority.setSelected(observableClassi.getRdoPriority());
        rdoRenewalNonPriority.setSelected(observableClassi.getRdoNonPriority());
        cboRenewalPurposeOfLoan.setSelectedItem(observableClassi.getCboPurposeCode());//observable.getCboRenewalPurposeOfLoan());
        txtRenewalSanctionRemarks.setText(observable.getTxtRenewalSanctionRemarks());
        txtRenewalLimit_SD.setText(observable.getTxtRenewalLimit_SD());
        tdtRenewalAccountOpenDate.setDateValue(observable.getTdtRenewalAccountOpenDate());
        cboRenewalAccStatus.setSelectedItem(observable.getCboRenewalAccStatus());
        txtRenewalNoInstallments.setText(observable.getTxtRenewalNoInstallments());
        cboRenewalRepayFreq.setSelectedItem(observable.getCboRenewalRepayFreq());
        lblRenewalAcctNo_Sanction_Disp.setText(observable.getLblRenewalAcctNo_Sanction_Disp());
        //observable. lblRenewalDemandPromNoteDate
        tdtRenewalDemandPromNoteExpDate.setDateValue(observable.getTdtRenewalDemandPromNoteExpDate());
        tdtRenewalDemandPromNoteDate.setDateValue(observable.getTdtRenewalDemandPromNoteDate());
        txtRenewalInter.setText(observable.getTxtRenewalInter());
        txtRenewalPenalInter.setText(observable.getTxtRenewalPenalInter());
        txtRenewalGrossWeight.setText(observableSecurity.getTxtRenewalGrossWeight());
        txtRenewalNetWeight.setText(observableSecurity.getTxtRenewalNetWeight());
        cboRenewalPurityOfGold.setSelectedItem(observableSecurity.getCboRenewalPurityOfGold());
        txtRenewalMarketRate.setText(observableSecurity.getTxtRenewalMarketRate());
        txtRenewalSecurityValue.setText(observableSecurity.getTxtRenewalSecurityValue());
        txtRenewalAreaParticular.setText(observableSecurity.getTxtRenewalAreaParticular());
        txtRenewalMargin.setText(observableSecurity.getTxtRenewalMargin());
        txtRenewalMarginAmt.setText(observableSecurity.getTxtRenewalMarginAmount());
        lblServiceTaxVal.setText(observable.getLblServiceTaxval());// Added by nithya on 09-10-2018 for KD 263 GST - Needed to implement the GST in GOld Loan renewal
        txtRenewalEligibleLoan.setText(observableSecurity.getTxtRenewalEligibleLoan());
        cboRenewalAppraiserId.setSelectedItem(observableSecurity.getCboRenewalAppraiserId());
        getRenewalAppraiserDefaultName(observableSecurity.getCboRenewalAppraiserId());
        calculateSanctionToDate();
        //observable. lblRenewalAppraiserNameValue        
        if (observable.getPhotoByteArray() != null) {// Added by nithya on 29-10-2019 for KD-763	Need Gold ornaments photo saving option
            lblPhoto.setIcon(new javax.swing.ImageIcon(observable.getPhotoByteArray()));
        }if (observable.getRenewalPhotoByteArray() != null) {
            lblRenewPhoto.setIcon(new javax.swing.ImageIcon(observable.getRenewalPhotoByteArray()));
        }
    }

    private void addRadioButtons() {
        //        addFacilityRadioBtns();
        addDocumentRadioBtns();
        addRenewalPriority();

        //        addGuranRadioBtns();
    }

    private void addRenewalPriority() {
        rdoRenewalPriorityGroup = new CButtonGroup();
        rdoRenewalPriorityGroup.add(rdoRenewalPriority);
        rdoRenewalPriorityGroup.add(rdoRenewalNonPriority);
    }

    private void addDocumentRadioBtns() {
        addDocSubmittRadioBtns();
        addDocExecuteRadioBtns();
        addDocMandatoryRadioBtns();
    }

    private void addDocSubmittRadioBtns() {
        rdoIsSubmitted_DocumentDetails = new CButtonGroup();
        rdoIsSubmitted_DocumentDetails.add(rdoYes_DocumentDetails);
        rdoIsSubmitted_DocumentDetails.add(rdoNo_DocumentDetails);
    }

    private void addDocExecuteRadioBtns() {
        rdoExecuted_DOC = new CButtonGroup();
        rdoExecuted_DOC.add(rdoYes_Executed_DOC);
        rdoExecuted_DOC.add(rdoNo_Executed_DOC);
    }

    private void addDocMandatoryRadioBtns() {
        rdoMandatory_DOC = new CButtonGroup();
        rdoMandatory_DOC.add(rdoYes_Mandatory_DOC);
        rdoMandatory_DOC.add(rdoNo_Mandatory_DOC);
    }
public void splitAndArageParticular()
{
     String prtculrs = txtAreaParticular.getText();
    String[] strarr = prtculrs.split("\n");
    StringBuffer retBuff = new StringBuffer();
    if (strarr.length > 3) {
        for (int i = 0; i < strarr.length; i=i+2) {
            int k=i + 1;
            
            //System.out.println("strarr[i]  :"+strarr[i]+"strarr[i]");
            if (i < strarr.length) {
//                if (i != 0) {
//                    retBuff.append("\n");
//                }
                retBuff.append(strarr[i]);
            } 
            if (k < strarr.length) {
                retBuff.append(", ");
                retBuff.append(strarr[k]);
                retBuff.append("\n");
            } 
           
            
        }
         txtAreaParticular.setText(retBuff+"");
    }
    else{
           txtAreaParticular.setText(prtculrs);
    }
   
}
    public void updateOBFields() {
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setSelectedBranchID(getSelectedBranchID());
        observableBorrow.setSelectedBranchID(getSelectedBranchID());
        observableClassi.setSelectedBranchID(getSelectedBranchID());
//        observableComp.setSelectedBranchID(getSelectedBranchID());
        observableDocument.setSelectedBranchID(getSelectedBranchID());
        observableInt.setSelectedBranchID(getSelectedBranchID());
//        observableOtherDetails.setSelectedBranchID(getSelectedBranchID());
        observableRepay.setSelectedBranchID(getSelectedBranchID());
        observableBorrow.setTxtCustID(txtCustID.getText());
        observableBorrow.setLblCustNameValue(lblCustNameValue.getText());
        //        observableBorrow.setLblCustNameValue(lblSecurityCustNameValue.getText());
        observableBorrow.setLblCustNameValue(lblDocumentCustNameValue.getText());
        //        observableBorrow.setCboConstitution(CommonUtil.convertObjToStr(cboConstitution.getSelectedItem()));
        observableBorrow.setCboCategory(CommonUtil.convertObjToStr(cboCategory.getSelectedItem()));
        observable.setCboAccStatus(CommonUtil.convertObjToStr(cboAccStatus.getSelectedItem()));
        //        observable.setTxtSanctionNo(txtSanctionNo.getText());
        observable.setTdtSanctionDate(tdtSanctionDate.getDateValue());
        observable.setCboSanctioningAuthority(CommonUtil.convertObjToStr(cboSanctioningAuthority.getSelectedItem()));
        observable.setTxtSanctionRemarks(txtSanctionRemarks.getText());
        //        observable.setCboModeSanction(CommonUtil.convertObjToStr(cboModeSanction.getSelectedItem()));
        observableRepay.setTxtNoInstall(txtNoInstallments.getText());
        observable.setCboRepayFreq(CommonUtil.convertObjToStr(cboRepayFreq.getSelectedItem()));
        observable.setTxtLimit_SD(txtLimit_SD.getText());
        observable.setAccountOpenDate(tdtAccountOpenDate.getDateValue());
        observable.setAccountCloseDate(tdtAccountCloseDate.getDateValue());
        observable.setTdtDemandPromNoteDate(tdtDemandPromNoteDate.getDateValue());
        observable.setTdtDemandPromNoteExpDate(tdtDemandPromNoteExpDate.getDateValue());
        //        observableSecurity.setLblProdId_Disp(lblSecurityProdIdValue.getText());
        //        observableSecurity.setTblSecurityTab((com.see.truetransact.clientutil.EnhancedTableModel)tblSecurityTable.getModel());
        observableDocument.setLblDocDesc_Disp_DocumentDetails(lblDocDesc_Disp_DocumentDetails.getText());
        observableDocument.setLblDocNo_Disp_DocumentDetails(lblDocNo_Disp_DocumentDetails.getText());
        observableDocument.setLblDocType_Disp_DocumentDetails(lblDocType_Disp_DocumentDetails.getText());
        observableDocument.setTxtRemarks_DocumentDetails(txtRemarks_DocumentDetails.getText());
        observableDocument.setTdtSubmitDate_DocumentDetails(tdtSubmitDate_DocumentDetails.getDateValue());
        observableDocument.setRdoYes_DocumentDetails(rdoYes_DocumentDetails.isSelected());
        observableDocument.setRdoNo_DocumentDetails(rdoNo_DocumentDetails.isSelected());
        observableDocument.setRdoYes_Executed_DOC(rdoYes_Executed_DOC.isSelected());
        observableDocument.setRdoNo_Executed_DOC(rdoNo_Executed_DOC.isSelected());
        observableDocument.setRdoYes_Mandatory_DOC(rdoYes_Mandatory_DOC.isSelected());
        observableDocument.setRdoNo_Mandatory_DOC(rdoNo_Mandatory_DOC.isSelected());
        observableDocument.setTdtExecuteDate_DOC(tdtExecuteDate_DOC.getDateValue());
        observableDocument.setTdtExpiryDate_DOC(tdtExpiryDate_DOC.getDateValue());
        observable.setPenalInterest(txtPenalInter.getText());
        observable.setInterest(txtInter.getText());
        observable.setLblStatus(lblStatus.getText());
        //        observableOtherDetails.setLblAcctHead_Disp_ODetails(lblAcctHead_Disp_ODetails.getText());
        //        observableOtherDetails.setLblAcctNo_Disp_ODetails(lblAcctNo_Disp_ODetails.getText());
        observableClassi.setCboPurposeCode(CommonUtil.convertObjToStr(cboPurposeOfLoan.getSelectedItem()));

        //        observable.setSecurityAccHeadValue(lblSecurityAccHeadValue.getText());
        observable.setSecurityAccHeadValue(lblDocumentAccHeadValue.getText());
        //Security details
        //        observableSecurity.setSlNo(lblSLNoValue.getText());
        //        observableSecurity.setTdtAson(tdtAson.getDateValue());
        observableSecurity.setTxtGrossWeight(txtGrossWeight.getText());
        observableSecurity.setTxtNetWeight(txtNetWeight.getText());
        observableSecurity.setCboPurityOfGold(CommonUtil.convertObjToStr(cboPurityOfGold.getSelectedItem()));
        observableSecurity.setTxtMarketRate(txtMarketRate.getText());
        observableSecurity.setSecurityValue(txtSecurityValue.getText());
        //        observableSecurity.setTotalSecurityValue(txtAvalSecVal.getText());
        observableSecurity.setTxtMargin(txtMargin.getText());
        observableSecurity.setTotalMarginAmt(txtMarginAmt.getText());
        observableSecurity.setTotalEligibleLoanAmt(txtEligibleLoan.getText());
        //        observableSecurity.setTxtAppraiserId(txtAppraiserId.getText());
        observableSecurity.setTxtAppraiserId(CommonUtil.convertObjToStr(observable.getCbmAppraiserId().getKeyForSelected())); //cboAppraiserId.getSelectedItem()));
        
        splitAndArageParticular();
        observableSecurity.setTxtAreaParticular(txtAreaParticular.getText());
        observableSecurity.setTxtNoOfPacket(txtNoOfPacket.getText());
        observable.setStrACNumber(lblAcctNo_Sanction_Disp.getText());
        observable.setStrACNumber(lblDocumentAccNoValue.getText());
        //        observable.setStrACNumber(lblSecurityAccNoValue.getText());
        observableClassi.setRdoNonPriority(rdoNonPriority.isSelected());
        observableClassi.setRdoPriority(rdoPriority.isSelected());

        observable.setIsMobileBanking(chkMobileBankingTLAD.isSelected());
        observable.setTxtMobileNo(txtMobileNo.getText());
        observable.setTdtMobileSubscribedFrom(tdtMobileSubscribedFrom.getDateValue());
        //RENEWAL

//lblRenewalAcctNo_Sanction_Disp     
        observable.setTdtRenewalSanctionDate(tdtRenewalSanctionDate.getDateValue());
//observable. lblRenewalSanctioningAuthority
//observable. cboRenewalSanctioningAuthority
        observable.setRdoRenewalPriority(rdoRenewalPriority.isSelected());
        observable.setRdoRenewalNonPriority(rdoRenewalNonPriority.isSelected());
//observable. cboRenewalPurposeOfLoan
        observable.setTxtRenewalSanctionRemarks(txtRenewalSanctionRemarks.getText());
        observable.setTxtRenewalLimit_SD(txtRenewalLimit_SD.getText());
        observable.setTdtRenewalAccountOpenDate(tdtRenewalAccountOpenDate.getDateValue());
        observable.setTdtRenewalDemandPromNoteDate(tdtRenewalDemandPromNoteDate.getDateValue());
        observable.setTdtDemandPromNoteExpDate(tdtDemandPromNoteExpDate.getDateValue());
//observable. cboRenewalAccStatus
        observable.setTxtRenewalNoInstallments(txtRenewalNoInstallments.getText());
//observable. cboRenewalRepayFreq
//observable. lblRenewalDemandPromNoteDate
        observable.setTdtRenewalDemandPromNoteExpDate(tdtRenewalDemandPromNoteExpDate.getDateValue());
        observable.setTxtRenewalInter(txtRenewalInter.getText());
        observable.setTxtRenewalPenalInter(txtRenewalPenalInter.getText());
        observable.setCboRenewalRepayFreq(CommonUtil.convertObjToStr(cboRepayFreq.getSelectedItem()));

        observableSecurity.setTxtRenewalGrossWeight(txtRenewalGrossWeight.getText());
        observableSecurity.setTxtRenewalNetWeight(txtRenewalNetWeight.getText());
        observableSecurity.setCboRenewalPurityOfGold(CommonUtil.convertObjToStr(cboRenewalPurityOfGold.getSelectedItem()));
        observableSecurity.setCboRenewalAppraiserId(CommonUtil.convertObjToStr(cboRenewalAppraiserId.getSelectedItem()));
        observableSecurity.setTxtRenewalMarketRate(txtRenewalMarketRate.getText());
        observableSecurity.setTxtRenewalSecurityValue(txtRenewalSecurityValue.getText());
        observableSecurity.setTxtRenewalAreaParticular(txtRenewalAreaParticular.getText());
        observableSecurity.setTxtRenewalMargin(txtRenewalMargin.getText());
        observableSecurity.setTxtRenewalMarginAmount(txtRenewalMarginAmt.getText());
        observableSecurity.setTxtRenewalEligibleLoan(txtRenewalEligibleLoan.getText());
        observableBorrow.setTxtCustomerPhoneNumber(txtCustMobNo.getText());

//observableSecurity.setCcboRenewalAppraiserId
//observableSecurity. lblRenewalAppraiserNameValue

        observable.setTxtNoInstallments(txtNoInstallments.getText());// Added by nithya on 10-12-2016 for 5493
        // Added by nithya on 09-10-2018 for KD 263 GST - Needed to implement the GST in GOld Loan renewal
        observable.setLblServiceTaxval(lblServiceTaxVal.getText());
        observable.setServiceTax_Map(serviceTax_Map);
        if(observable.isRenewalYesNo()){
        observable.setCboRenewalRepayFreq(CommonUtil.convertObjToStr(cboRenewalRepayFreq.getSelectedItem()));
        }

    }

    private void initComponentData() {
        cboGoldLoanProd.setModel(observable.getCbmGoldLoanProd());
        cboCategory.setModel(observableBorrow.getCbmCategory());
        cboConstitution.setModel(observableBorrow.getCbmConstitution());
        cboAccStatus.setModel(observable.getCbmAccStatus());
        cboSanctioningAuthority.setModel(observable.getCbmSanctioningAuthority());
        //        cboModeSanction.setModel(observable.getCbmModeSanction());
        cboRepayFreq.setModel(observableRepay.getCbmRepayFreq_Repayment());
        cboRepaymentType.setModel(observableRepay.getCbmRepayType());
        //        cboOpModeAI.setModel(observableOtherDetails.getCbmOpModeAI());
        //        cboSettlementModeAI.setModel(observableOtherDetails.getCbmSettlementModeAI());
        //        cboStmtFreqAD.setModel(observableOtherDetails.getCbmStmtFreqAD());
        cboPurityOfGold.setModel(observableSecurity.getCbmPurityOfGold());
        cboPurposeOfLoan.setModel(observableClassi.getCbmPurposeCode());
        cboAppraiserId.setModel(observable.getCbmAppraiserId());

        cboRenewalSanctioningAuthority.setModel(observable.getCbmRenewalSanctioningAuthority());
        cboRenewalPurposeOfLoan.setModel(observable.getCbmRenewalPurposeOfLoan());
        cboRenewalAccStatus.setModel(observable.getCbmRenewalAccStatus());
        cboRenewalRepayFreq.setModel(observable.getCbmRenewalRepayFreq());
        cboRenewalPurityOfGold.setModel(observableSecurity.getCbmRenewalPurityOfGold());
        cboRenewalAppraiserId.setModel(observable.getCbmRenewalAppraiserId());
        cboItem.setModel(observableSecurity.getCbmItem());
        cboRnwGoldLoanProd.setModel(observable.getCbmRnwGoldLoanProd());
       
        AutoCompleteDecorator.decorate(cboAppraiserId);
        AutoCompleteDecorator.decorate(cboRenewalAppraiserId);

    }

    private void setMaxLength() {

        //Customer/Sanction Details
        txtCustID.setMaxLength(16);
        txtCustID.setAllowAll(true);
        txtAccountNo.setMaxLength(16);
        txtAccountNo.setAllowAll(true);
        //        txtSanctionNo.setMaxLength(16);
        //        txtSanctionNo.setAllowNumber(true);
        txtSanctionRemarks.setMaxLength(128);
        txtSanctionRemarks.setValidation(new DefaultValidation());
        txtLimit_SD.setMaxLength(16);
        txtLimit_SD.setValidation(new CurrencyValidation(14, 2));
        txtNoInstallments.setMaxLength(3);
        txtNoInstallments.setValidation(new NumericValidation());
        txtNoOfPacket.setValidation(new NumericValidation());
        txtQty.setValidation(new NumericValidation());
        txtSecurityRemarks.setAllowAll(true);
        txtRemarks_DocumentDetails.setMaxLength(128);
        txtInter.setMaxLength(5);
        txtInter.setValidation(new PercentageValidation());
        txtPenalInter.setMaxLength(5);
        txtPenalInter.setValidation(new PercentageValidation());
        txtMobileNo.setValidation(new NumericValidation());
        //Security Details
        txtGrossWeight.setValidation(new NumericValidation(10, 4));
        txtNetWeight.setValidation(new NumericValidation(10, 4));
        txtSecurityValue.setValidation(new CurrencyValidation(14, 2));
        txtMargin.setValidation(new NumericValidation(3, 2));
        txtEligibleLoan.setValidation(new CurrencyValidation(14, 2));
        //        txtAvalSecVal.setValidation(new CurrencyValidation(14,2));
        //        txtpurity.setValidation(new NumericValidation(14,2));
        txtMarketRate.setValidation(new CurrencyValidation(14, 2));
        txtMarginAmt.setValidation(new CurrencyValidation(14, 2));
        txtRenewalSanctionRemarks.setAllowAll(true);
        txtRenewalLimit_SD.setValidation(new CurrencyValidation(14, 2));
        txtRenewalNoInstallments.setValidation(new NumericValidation());
        txtRenewalInter.setValidation(new PercentageValidation());
        txtRenewalPenalInter.setValidation(new PercentageValidation());
    }

    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        btnSave.setEnabled(btnCancel.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        setAuthBtnEnableDisable();
    }

    /**
     * To Enable or Disable Authorize, Rejection and Exception Button
     */
    private void setAuthBtnEnableDisable() {
        final boolean enableDisable = !btnSave.isEnabled();
        btnAuthorize.setEnabled(enableDisable);
        btnException.setEnabled(enableDisable);
        btnReject.setEnabled(enableDisable);
        mitAuthorize.setEnabled(enableDisable);
        mitException.setEnabled(enableDisable);
        mitReject.setEnabled(enableDisable);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     *
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
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
        rdoRenewalPriorityGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrTermLoan = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace51 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace52 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace53 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace54 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace55 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblspace3 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace56 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        lblSpace57 = new com.see.truetransact.uicomponent.CLabel();
        btnClosedAccounts = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panTermLoan = new com.see.truetransact.uicomponent.CPanel();
        tabLimitAmount = new com.see.truetransact.uicomponent.CTabbedPane();
        panBorrowCompanyDetails = new com.see.truetransact.uicomponent.CPanel();
        panBorrowProfile = new com.see.truetransact.uicomponent.CPanel();
        panSecurityDetails_security = new com.see.truetransact.uicomponent.CPanel();
        panSecurityType = new com.see.truetransact.uicomponent.CPanel();
        txtGrossWeight = new com.see.truetransact.uicomponent.CTextField();
        lblNetWeight = new com.see.truetransact.uicomponent.CLabel();
        txtNetWeight = new com.see.truetransact.uicomponent.CTextField();
        lblGrossWeight = new com.see.truetransact.uicomponent.CLabel();
        txtSecurityValue = new com.see.truetransact.uicomponent.CTextField();
        lblSecurityValue = new com.see.truetransact.uicomponent.CLabel();
        txtMarketRate = new com.see.truetransact.uicomponent.CTextField();
        lblMarketRate = new com.see.truetransact.uicomponent.CLabel();
        lblpurity = new com.see.truetransact.uicomponent.CLabel();
        cboPurityOfGold = new com.see.truetransact.uicomponent.CComboBox();
        lblMargin = new com.see.truetransact.uicomponent.CLabel();
        txtMargin = new com.see.truetransact.uicomponent.CTextField();
        lblMarginAmt = new com.see.truetransact.uicomponent.CLabel();
        txtMarginAmt = new com.see.truetransact.uicomponent.CTextField();
        lblMaxLoanAmt = new com.see.truetransact.uicomponent.CLabel();
        txtEligibleLoan = new com.see.truetransact.uicomponent.CTextField();
        lblAppraiserId = new com.see.truetransact.uicomponent.CLabel();
        lblAppraiserName = new com.see.truetransact.uicomponent.CLabel();
        lblAppraiserNameValue = new com.see.truetransact.uicomponent.CLabel();
        cboAppraiserId = new com.see.truetransact.uicomponent.CComboBox();
        panItemSecurity = new com.see.truetransact.uicomponent.CPanel();
        cboItem = new com.see.truetransact.uicomponent.CComboBox();
        lblItem = new com.see.truetransact.uicomponent.CLabel();
        lblQty = new com.see.truetransact.uicomponent.CLabel();
        lblSecurityRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtQty = new com.see.truetransact.uicomponent.CTextField();
        txtSecurityRemarks = new com.see.truetransact.uicomponent.CTextField();
        btnSecurityAdd = new com.see.truetransact.uicomponent.CButton();
        panSecurityParticulars = new com.see.truetransact.uicomponent.CPanel();
        lblParticulars = new com.see.truetransact.uicomponent.CLabel();
        srpTxtAreaParticulars = new com.see.truetransact.uicomponent.CScrollPane();
        txtAreaParticular = new com.see.truetransact.uicomponent.CTextArea();
        lblNoOfPacket = new com.see.truetransact.uicomponent.CLabel();
        txtNoOfPacket = new com.see.truetransact.uicomponent.CTextField();
        cLabel2 = new com.see.truetransact.uicomponent.CLabel();
        lblServiceTaxOpeningVal = new com.see.truetransact.uicomponent.CLabel();
        panChargeDetails = new com.see.truetransact.uicomponent.CPanel();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        panBorrowProfile_CustName = new com.see.truetransact.uicomponent.CPanel();
        panBorrowProfile_CustID1 = new com.see.truetransact.uicomponent.CPanel();
        panExistingCustomer = new com.see.truetransact.uicomponent.CPanel();
        rdoExistingCustomer_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoExistingCustomer_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblAccountNo = new com.see.truetransact.uicomponent.CLabel();
        txtAccountNo = new com.see.truetransact.uicomponent.CTextField();
        lblCustID = new com.see.truetransact.uicomponent.CLabel();
        panCustId = new com.see.truetransact.uicomponent.CPanel();
        txtCustID = new com.see.truetransact.uicomponent.CTextField();
        btnCustID = new com.see.truetransact.uicomponent.CButton();
        lblCustNameValue = new com.see.truetransact.uicomponent.CLabel();
        lblDealingWithBankSince = new com.see.truetransact.uicomponent.CLabel();
        tdtDealingWithBankSince = new com.see.truetransact.uicomponent.CDateField();
        lblConstitution = new com.see.truetransact.uicomponent.CLabel();
        cboConstitution = new com.see.truetransact.uicomponent.CComboBox();
        lblCategory = new com.see.truetransact.uicomponent.CLabel();
        cboCategory = new com.see.truetransact.uicomponent.CComboBox();
        lblExistingCustomer = new com.see.truetransact.uicomponent.CLabel();
        btnMembershipLia = new com.see.truetransact.uicomponent.CButton();
        lblCustMobNo = new com.see.truetransact.uicomponent.CLabel();
        txtCustMobNo = new com.see.truetransact.uicomponent.CTextField();
        btnGoldLiability = new com.see.truetransact.uicomponent.CButton();
        panBorrowProfile_CustID = new com.see.truetransact.uicomponent.CPanel();
        panBorrowerTabCTable = new com.see.truetransact.uicomponent.CPanel();
        srpBorrowerTabCTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblBorrowerTabCTable = new com.see.truetransact.uicomponent.CTable();
        panBorrowerTabTools = new com.see.truetransact.uicomponent.CPanel();
        btnNew_Borrower = new com.see.truetransact.uicomponent.CButton();
        btnToMain_Borrower = new com.see.truetransact.uicomponent.CButton();
        btnDeleteBorrower = new com.see.truetransact.uicomponent.CButton();
        jPanel2 = new com.see.truetransact.uicomponent.CPanel();
        jLabel1 = new com.see.truetransact.uicomponent.CLabel();
        txtNextAccNo = new com.see.truetransact.uicomponent.CTextField();
        cPanel2 = new com.see.truetransact.uicomponent.CPanel();
        lblGoldLoanProd = new com.see.truetransact.uicomponent.CLabel();
        cboGoldLoanProd = new com.see.truetransact.uicomponent.CComboBox();
        jPanel3 = new javax.swing.JPanel();
        panSanctionDetails_Table = new com.see.truetransact.uicomponent.CPanel();
        panTableFields_SD = new com.see.truetransact.uicomponent.CPanel();
        panDemandPromssoryDate = new com.see.truetransact.uicomponent.CPanel();
        lblDemandPromNoteDate = new com.see.truetransact.uicomponent.CLabel();
        tdtDemandPromNoteDate = new com.see.truetransact.uicomponent.CDateField();
        lblDemandPromNoteExpDate = new com.see.truetransact.uicomponent.CLabel();
        tdtDemandPromNoteExpDate = new com.see.truetransact.uicomponent.CDateField();
        tdtEmiDate = new com.see.truetransact.uicomponent.CDateField();
        lblEmiDate = new com.see.truetransact.uicomponent.CLabel();
        panInterestDeails = new com.see.truetransact.uicomponent.CPanel();
        lblPenalInter = new com.see.truetransact.uicomponent.CLabel();
        lblInter = new com.see.truetransact.uicomponent.CLabel();
        txtPenalInter = new com.see.truetransact.uicomponent.CTextField();
        txtInter = new com.see.truetransact.uicomponent.CTextField();
        panInterestDeails1 = new com.see.truetransact.uicomponent.CPanel();
        lblLimit_SD = new com.see.truetransact.uicomponent.CLabel();
        txtLimit_SD = new com.see.truetransact.uicomponent.CTextField();
        txtNoInstallments = new com.see.truetransact.uicomponent.CTextField();
        lblNoInstallments = new com.see.truetransact.uicomponent.CLabel();
        cboRepayFreq = new com.see.truetransact.uicomponent.CComboBox();
        lblRepayFreq = new com.see.truetransact.uicomponent.CLabel();
        lblAccCloseDt = new com.see.truetransact.uicomponent.CLabel();
        lblAccOpenDt = new com.see.truetransact.uicomponent.CLabel();
        lblAccStatus = new com.see.truetransact.uicomponent.CLabel();
        cboAccStatus = new com.see.truetransact.uicomponent.CComboBox();
        tdtAccountOpenDate = new com.see.truetransact.uicomponent.CDateField();
        tdtAccountCloseDate = new com.see.truetransact.uicomponent.CDateField();
        panSanctionDetails_Sanction = new com.see.truetransact.uicomponent.CPanel();
        lblSanctionDate = new com.see.truetransact.uicomponent.CLabel();
        tdtSanctionDate = new com.see.truetransact.uicomponent.CDateField();
        cboSanctioningAuthority = new com.see.truetransact.uicomponent.CComboBox();
        txtSanctionRemarks = new com.see.truetransact.uicomponent.CTextField();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        lblSanctioningAuthority = new com.see.truetransact.uicomponent.CLabel();
        lblPurposeOfLoan = new com.see.truetransact.uicomponent.CLabel();
        cboPurposeOfLoan = new com.see.truetransact.uicomponent.CComboBox();
        panGender = new com.see.truetransact.uicomponent.CPanel();
        rdoPriority = new com.see.truetransact.uicomponent.CRadioButton();
        rdoNonPriority = new com.see.truetransact.uicomponent.CRadioButton();
        lblAcctNo_Sanction = new com.see.truetransact.uicomponent.CLabel();
        lblAcctNo_Sanction_Disp = new com.see.truetransact.uicomponent.CLabel();
        jPanel1 = new javax.swing.JPanel();
        btnRenew = new com.see.truetransact.uicomponent.CButton();
        lblSanRepaymentType = new com.see.truetransact.uicomponent.CLabel();
        cboRepaymentType = new com.see.truetransact.uicomponent.CComboBox();
        btnRepayShedule = new com.see.truetransact.uicomponent.CButton();
        panDocumentDetails = new com.see.truetransact.uicomponent.CPanel();
        panTable_DocumentDetails = new com.see.truetransact.uicomponent.CPanel();
        srpTable_DocumentDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblTable_DocumentDetails = new com.see.truetransact.uicomponent.CTable();
        panTabDetails_DocumentDetails = new com.see.truetransact.uicomponent.CPanel();
        lblDocType_DocumentDetails = new com.see.truetransact.uicomponent.CLabel();
        lblDocType_Disp_DocumentDetails = new com.see.truetransact.uicomponent.CLabel();
        lblDocNo_DocumentDetails = new com.see.truetransact.uicomponent.CLabel();
        lblDocNo_Disp_DocumentDetails = new com.see.truetransact.uicomponent.CLabel();
        lblDocDesc_DocumentDetails = new com.see.truetransact.uicomponent.CLabel();
        lblDocDesc_Disp_DocumentDetails = new com.see.truetransact.uicomponent.CLabel();
        lblSubmitted_DocumentDetails = new com.see.truetransact.uicomponent.CLabel();
        panSubmitted_DocumentDetails = new com.see.truetransact.uicomponent.CPanel();
        rdoYes_DocumentDetails = new com.see.truetransact.uicomponent.CRadioButton();
        rdoNo_DocumentDetails = new com.see.truetransact.uicomponent.CRadioButton();
        lblSubmitDate_DocumentDetails = new com.see.truetransact.uicomponent.CLabel();
        tdtSubmitDate_DocumentDetails = new com.see.truetransact.uicomponent.CDateField();
        lblRemarks_DocumentDetails = new com.see.truetransact.uicomponent.CLabel();
        txtRemarks_DocumentDetails = new com.see.truetransact.uicomponent.CTextField();
        btnSave_DocumentDetails = new com.see.truetransact.uicomponent.CButton();
        lblMandatory_DOC = new com.see.truetransact.uicomponent.CLabel();
        panMandatory_DOC = new com.see.truetransact.uicomponent.CPanel();
        rdoYes_Mandatory_DOC = new com.see.truetransact.uicomponent.CRadioButton();
        rdoNo_Mandatory_DOC = new com.see.truetransact.uicomponent.CRadioButton();
        lblExecuted_DOC = new com.see.truetransact.uicomponent.CLabel();
        panExecuted_DOC = new com.see.truetransact.uicomponent.CPanel();
        rdoYes_Executed_DOC = new com.see.truetransact.uicomponent.CRadioButton();
        rdoNo_Executed_DOC = new com.see.truetransact.uicomponent.CRadioButton();
        lblExecuteDate_DOC = new com.see.truetransact.uicomponent.CLabel();
        tdtExecuteDate_DOC = new com.see.truetransact.uicomponent.CDateField();
        lblExpiryDate_DOC = new com.see.truetransact.uicomponent.CLabel();
        tdtExpiryDate_DOC = new com.see.truetransact.uicomponent.CDateField();
        panSecurity1 = new com.see.truetransact.uicomponent.CPanel();
        lblDocumentProdId = new com.see.truetransact.uicomponent.CLabel();
        lblDocumentProdIdValue = new com.see.truetransact.uicomponent.CLabel();
        lblDocumentCustName = new com.see.truetransact.uicomponent.CLabel();
        lblDocumentCustNameValue = new com.see.truetransact.uicomponent.CLabel();
        lblDocumentAccNo = new com.see.truetransact.uicomponent.CLabel();
        lblDocumentAccNoValue = new com.see.truetransact.uicomponent.CLabel();
        lblDocumentAccHead = new com.see.truetransact.uicomponent.CLabel();
        lblDocumentAccHeadValue = new com.see.truetransact.uicomponent.CLabel();
        panMobileBanking = new com.see.truetransact.uicomponent.CPanel();
        chkMobileBankingTLAD = new com.see.truetransact.uicomponent.CCheckBox();
        lblMobileNo = new com.see.truetransact.uicomponent.CLabel();
        txtMobileNo = new com.see.truetransact.uicomponent.CTextField();
        tdtMobileSubscribedFrom = new com.see.truetransact.uicomponent.CDateField();
        lblMobileSubscribedFrom = new com.see.truetransact.uicomponent.CLabel();
        panBorrowRenewalCompanyDetails = new com.see.truetransact.uicomponent.CPanel();
        panBorrowProfile1 = new com.see.truetransact.uicomponent.CPanel();
        panRenewalSecurityDetails_security = new com.see.truetransact.uicomponent.CPanel();
        panSecurityType1 = new com.see.truetransact.uicomponent.CPanel();
        txtRenewalGrossWeight = new com.see.truetransact.uicomponent.CTextField();
        lblRenewalNetWeight = new com.see.truetransact.uicomponent.CLabel();
        txtRenewalNetWeight = new com.see.truetransact.uicomponent.CTextField();
        lblRenewalGrossWeight = new com.see.truetransact.uicomponent.CLabel();
        txtRenewalSecurityValue = new com.see.truetransact.uicomponent.CTextField();
        lblRenewalSecurityValue = new com.see.truetransact.uicomponent.CLabel();
        txtRenewalMarketRate = new com.see.truetransact.uicomponent.CTextField();
        lblRenewalMarketRate = new com.see.truetransact.uicomponent.CLabel();
        lblRenewalpurity = new com.see.truetransact.uicomponent.CLabel();
        cboRenewalPurityOfGold = new com.see.truetransact.uicomponent.CComboBox();
        lblRenewalMargin = new com.see.truetransact.uicomponent.CLabel();
        txtRenewalMargin = new com.see.truetransact.uicomponent.CTextField();
        lblRenewalMarginAmt = new com.see.truetransact.uicomponent.CLabel();
        txtRenewalMarginAmt = new com.see.truetransact.uicomponent.CTextField();
        lblRenewalMaxLoanAmt = new com.see.truetransact.uicomponent.CLabel();
        txtRenewalEligibleLoan = new com.see.truetransact.uicomponent.CTextField();
        lblRenewalAppraiserId = new com.see.truetransact.uicomponent.CLabel();
        lblRenewalAppraiserName = new com.see.truetransact.uicomponent.CLabel();
        lblRenewalAppraiserNameValue = new com.see.truetransact.uicomponent.CLabel();
        cboRenewalAppraiserId = new com.see.truetransact.uicomponent.CComboBox();
        lblRnwLoanType = new com.see.truetransact.uicomponent.CLabel();
        cboRnwGoldLoanProd = new com.see.truetransact.uicomponent.CComboBox();
        lblRenewCustMobNo = new com.see.truetransact.uicomponent.CLabel();
        txtRenewCustMobNo = new com.see.truetransact.uicomponent.CTextField();
        panSecurityParticulars1 = new com.see.truetransact.uicomponent.CPanel();
        lblRenewalParticulars = new com.see.truetransact.uicomponent.CLabel();
        srpTxtAreaParticulars1 = new com.see.truetransact.uicomponent.CScrollPane();
        txtRenewalAreaParticular = new com.see.truetransact.uicomponent.CTextArea();
        panRenewalChargeDetails = new com.see.truetransact.uicomponent.CPanel();
        panRenewalTransactionDetails = new com.see.truetransact.uicomponent.CPanel();
        panSanctionDetails_Table1 = new com.see.truetransact.uicomponent.CPanel();
        panTableFields_SD1 = new com.see.truetransact.uicomponent.CPanel();
        panRenewalDemandPromssoryDate = new com.see.truetransact.uicomponent.CPanel();
        lblRenewalDemandPromNoteDate = new com.see.truetransact.uicomponent.CLabel();
        tdtRenewalDemandPromNoteDate = new com.see.truetransact.uicomponent.CDateField();
        lblRenewalDemandPromNoteExpDate = new com.see.truetransact.uicomponent.CLabel();
        tdtRenewalDemandPromNoteExpDate = new com.see.truetransact.uicomponent.CDateField();
        panRenewalInterestDeails2 = new com.see.truetransact.uicomponent.CPanel();
        lblRenewalPenalInter = new com.see.truetransact.uicomponent.CLabel();
        lblRenewalInter = new com.see.truetransact.uicomponent.CLabel();
        txtRenewalPenalInter = new com.see.truetransact.uicomponent.CTextField();
        txtRenewalInter = new com.see.truetransact.uicomponent.CTextField();
        panRenewalInterestDeails = new com.see.truetransact.uicomponent.CPanel();
        lblRenewalLimit_SD = new com.see.truetransact.uicomponent.CLabel();
        txtRenewalLimit_SD = new com.see.truetransact.uicomponent.CTextField();
        txtRenewalNoInstallments = new com.see.truetransact.uicomponent.CTextField();
        lblRenewalNoInstallments = new com.see.truetransact.uicomponent.CLabel();
        cboRenewalRepayFreq = new com.see.truetransact.uicomponent.CComboBox();
        lblRenewalRepayFreq = new com.see.truetransact.uicomponent.CLabel();
        lblRenewalAccOpenDt = new com.see.truetransact.uicomponent.CLabel();
        lblRenewalAccStatus = new com.see.truetransact.uicomponent.CLabel();
        cboRenewalAccStatus = new com.see.truetransact.uicomponent.CComboBox();
        tdtRenewalAccountOpenDate = new com.see.truetransact.uicomponent.CDateField();
        txtCharges = new com.see.truetransact.uicomponent.CTextField();
        lblCharges = new com.see.truetransact.uicomponent.CLabel();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        lblServiceTaxVal = new com.see.truetransact.uicomponent.CLabel();
        panRenewalSanctionDetails_Sanction = new com.see.truetransact.uicomponent.CPanel();
        lblRenewalSanctionDate = new com.see.truetransact.uicomponent.CLabel();
        tdtRenewalSanctionDate = new com.see.truetransact.uicomponent.CDateField();
        cboRenewalSanctioningAuthority = new com.see.truetransact.uicomponent.CComboBox();
        txtRenewalSanctionRemarks = new com.see.truetransact.uicomponent.CTextField();
        lblRenewalRemarks = new com.see.truetransact.uicomponent.CLabel();
        lblRenewalSanctioningAuthority = new com.see.truetransact.uicomponent.CLabel();
        lblRenewalPurposeOfLoan = new com.see.truetransact.uicomponent.CLabel();
        cboRenewalPurposeOfLoan = new com.see.truetransact.uicomponent.CComboBox();
        panGender1 = new com.see.truetransact.uicomponent.CPanel();
        rdoRenewalPriority = new com.see.truetransact.uicomponent.CRadioButton();
        rdoRenewalNonPriority = new com.see.truetransact.uicomponent.CRadioButton();
        lblRenewalAcctNo_Sanction = new com.see.truetransact.uicomponent.CLabel();
        lblRenewalAcctNo_Sanction_Disp = new com.see.truetransact.uicomponent.CLabel();
        panTranDetView = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabTransView = new com.see.truetransact.uicomponent.CTable();
        panStockDetails = new com.see.truetransact.uicomponent.CPanel();
        srpPhotoLoad = new com.see.truetransact.uicomponent.CScrollPane();
        lblPhoto = new com.see.truetransact.uicomponent.CLabel();
        btnLoad = new com.see.truetransact.uicomponent.CButton();
        btnPhotoRemove = new com.see.truetransact.uicomponent.CButton();
        srpRenewPhotoLoad = new com.see.truetransact.uicomponent.CScrollPane();
        lblRenewPhoto = new com.see.truetransact.uicomponent.CLabel();
        btnAddPhoto = new com.see.truetransact.uicomponent.CButton();
        btnRenewLoad = new com.see.truetransact.uicomponent.CButton();
        btnRenewPhotoRemove = new com.see.truetransact.uicomponent.CButton();
        panWebCamStockPhoto = new com.see.truetransact.uicomponent.CPanel();
        btnScanStockPhoto = new com.see.truetransact.uicomponent.CButton();
        btnStockPhotoCapture = new com.see.truetransact.uicomponent.CButton();
        panWebCamRenewalStockPhoto = new com.see.truetransact.uicomponent.CPanel();
        btnScanRenewalStockPhoto = new com.see.truetransact.uicomponent.CButton();
        btnRenewalStockPhotoCapture = new com.see.truetransact.uicomponent.CButton();
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
        setTitle("Gold Loan Account");
        setMinimumSize(new java.awt.Dimension(900, 665));
        setPreferredSize(new java.awt.Dimension(900, 665));

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

        lblSpace51.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace51.setText("     ");
        lblSpace51.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace51);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnEdit);

        lblSpace52.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace52.setText("     ");
        lblSpace52.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace52);

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

        lblSpace53.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace53.setText("     ");
        lblSpace53.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace53);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
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
        btnAuthorize.setEnabled(false);
        btnAuthorize.setMaximumSize(new java.awt.Dimension(29, 27));
        btnAuthorize.setMinimumSize(new java.awt.Dimension(29, 27));
        btnAuthorize.setPreferredSize(new java.awt.Dimension(29, 27));
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnAuthorize);

        lblSpace54.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace54.setText("     ");
        lblSpace54.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace54);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setEnabled(false);
        btnException.setFocusable(false);
        btnException.setMaximumSize(new java.awt.Dimension(29, 27));
        btnException.setMinimumSize(new java.awt.Dimension(29, 27));
        btnException.setPreferredSize(new java.awt.Dimension(29, 27));
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnException);

        lblSpace55.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace55.setText("     ");
        lblSpace55.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace55);

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
        btnPrint.setFocusable(false);
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnPrint);

        lblSpace56.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace56.setText("     ");
        lblSpace56.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace56);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.setFocusable(false);
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnClose);

        lblSpace57.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace57.setText("     ");
        lblSpace57.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace57.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace57.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace57);

        btnClosedAccounts.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnClosedAccounts.setFocusable(false);
        btnClosedAccounts.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClosedAccounts.setMaximumSize(new java.awt.Dimension(29, 27));
        btnClosedAccounts.setMinimumSize(new java.awt.Dimension(29, 27));
        btnClosedAccounts.setPreferredSize(new java.awt.Dimension(29, 27));
        btnClosedAccounts.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnClosedAccounts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClosedAccountsActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnClosedAccounts);

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

        panTermLoan.setMinimumSize(new java.awt.Dimension(850, 650));
        panTermLoan.setPreferredSize(new java.awt.Dimension(850, 650));
        panTermLoan.setLayout(new java.awt.GridBagLayout());

        tabLimitAmount.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        tabLimitAmount.setMinimumSize(new java.awt.Dimension(850, 649));
        tabLimitAmount.setPreferredSize(new java.awt.Dimension(850, 649));

        panBorrowCompanyDetails.setMinimumSize(new java.awt.Dimension(659, 545));
        panBorrowCompanyDetails.setPreferredSize(new java.awt.Dimension(659, 545));
        panBorrowCompanyDetails.setLayout(new java.awt.GridBagLayout());

        panBorrowProfile.setBorder(javax.swing.BorderFactory.createTitledBorder("Borrower's Profile"));
        panBorrowProfile.setMinimumSize(new java.awt.Dimension(940, 375));
        panBorrowProfile.setPreferredSize(new java.awt.Dimension(945, 375));
        panBorrowProfile.setLayout(new java.awt.GridBagLayout());

        panSecurityDetails_security.setBorder(javax.swing.BorderFactory.createTitledBorder("Security Details"));
        panSecurityDetails_security.setMaximumSize(new java.awt.Dimension(455, 320));
        panSecurityDetails_security.setMinimumSize(new java.awt.Dimension(455, 320));
        panSecurityDetails_security.setPreferredSize(new java.awt.Dimension(455, 320));
        panSecurityDetails_security.setLayout(new java.awt.GridBagLayout());

        panSecurityType.setMinimumSize(new java.awt.Dimension(490, 150));
        panSecurityType.setPreferredSize(new java.awt.Dimension(490, 150));
        panSecurityType.setLayout(new java.awt.GridBagLayout());

        txtGrossWeight.setAllowAll(true);
        txtGrossWeight.setAllowNumber(true);
        txtGrossWeight.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panSecurityType.add(txtGrossWeight, gridBagConstraints);

        lblNetWeight.setText(" Net Weight");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 4);
        panSecurityType.add(lblNetWeight, gridBagConstraints);

        txtNetWeight.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNetWeight.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNetWeightFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panSecurityType.add(txtNetWeight, gridBagConstraints);

        lblGrossWeight.setText("Gross Weight");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 4);
        panSecurityType.add(lblGrossWeight, gridBagConstraints);

        txtSecurityValue.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panSecurityType.add(txtSecurityValue, gridBagConstraints);

        lblSecurityValue.setText("Security Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 4);
        panSecurityType.add(lblSecurityValue, gridBagConstraints);

        txtMarketRate.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panSecurityType.add(txtMarketRate, gridBagConstraints);

        lblMarketRate.setText("Market Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 4);
        panSecurityType.add(lblMarketRate, gridBagConstraints);

        lblpurity.setText("Purity");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 4);
        panSecurityType.add(lblpurity, gridBagConstraints);

        cboPurityOfGold.setMaximumSize(new java.awt.Dimension(100, 21));
        cboPurityOfGold.setMinimumSize(new java.awt.Dimension(100, 21));
        cboPurityOfGold.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPurityOfGoldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panSecurityType.add(cboPurityOfGold, gridBagConstraints);

        lblMargin.setText("Margin %");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 4);
        panSecurityType.add(lblMargin, gridBagConstraints);

        txtMargin.setMaximumSize(new java.awt.Dimension(100, 21));
        txtMargin.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMargin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMarginActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panSecurityType.add(txtMargin, gridBagConstraints);

        lblMarginAmt.setText("Margin Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 4);
        panSecurityType.add(lblMarginAmt, gridBagConstraints);

        txtMarginAmt.setEnabled(false);
        txtMarginAmt.setMaximumSize(new java.awt.Dimension(100, 21));
        txtMarginAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMarginAmt.setOpaque(false);
        txtMarginAmt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMarginAmtActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panSecurityType.add(txtMarginAmt, gridBagConstraints);

        lblMaxLoanAmt.setText("Eligible Loan Amt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 18, 0, 4);
        panSecurityType.add(lblMaxLoanAmt, gridBagConstraints);

        txtEligibleLoan.setEnabled(false);
        txtEligibleLoan.setMaximumSize(new java.awt.Dimension(100, 21));
        txtEligibleLoan.setMinimumSize(new java.awt.Dimension(100, 21));
        txtEligibleLoan.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panSecurityType.add(txtEligibleLoan, gridBagConstraints);

        lblAppraiserId.setText("Appraiser Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSecurityType.add(lblAppraiserId, gridBagConstraints);

        lblAppraiserName.setText("Appr Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSecurityType.add(lblAppraiserName, gridBagConstraints);

        lblAppraiserNameValue.setForeground(new java.awt.Color(0, 51, 255));
        lblAppraiserNameValue.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        lblAppraiserNameValue.setMaximumSize(new java.awt.Dimension(120, 18));
        lblAppraiserNameValue.setMinimumSize(new java.awt.Dimension(120, 18));
        lblAppraiserNameValue.setPreferredSize(new java.awt.Dimension(120, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        panSecurityType.add(lblAppraiserNameValue, gridBagConstraints);

        cboAppraiserId.setEditable(true);
        cboAppraiserId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboAppraiserId.setPopupWidth(150);
        cboAppraiserId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboAppraiserIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panSecurityType.add(cboAppraiserId, gridBagConstraints);

        panItemSecurity.setMinimumSize(new java.awt.Dimension(420, 25));
        panItemSecurity.setPreferredSize(new java.awt.Dimension(420, 25));
        panItemSecurity.setLayout(new java.awt.GridBagLayout());

        cboItem.setMaximumSize(new java.awt.Dimension(100, 21));
        cboItem.setMinimumSize(new java.awt.Dimension(0, 0));
        cboItem.setPreferredSize(new java.awt.Dimension(0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panItemSecurity.add(cboItem, gridBagConstraints);

        lblItem.setText("Item");
        lblItem.setMinimumSize(new java.awt.Dimension(0, 0));
        lblItem.setPreferredSize(new java.awt.Dimension(0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 4);
        panItemSecurity.add(lblItem, gridBagConstraints);

        lblQty.setText("Qty");
        lblQty.setMinimumSize(new java.awt.Dimension(0, 0));
        lblQty.setPreferredSize(new java.awt.Dimension(0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 4);
        panItemSecurity.add(lblQty, gridBagConstraints);

        lblSecurityRemarks.setText("Remarks");
        lblSecurityRemarks.setMinimumSize(new java.awt.Dimension(0, 0));
        lblSecurityRemarks.setPreferredSize(new java.awt.Dimension(0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 4);
        panItemSecurity.add(lblSecurityRemarks, gridBagConstraints);

        txtQty.setMinimumSize(new java.awt.Dimension(0, 0));
        txtQty.setPreferredSize(new java.awt.Dimension(0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panItemSecurity.add(txtQty, gridBagConstraints);

        txtSecurityRemarks.setMinimumSize(new java.awt.Dimension(0, 0));
        txtSecurityRemarks.setPreferredSize(new java.awt.Dimension(0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panItemSecurity.add(txtSecurityRemarks, gridBagConstraints);

        btnSecurityAdd.setText("Gold Items");
        btnSecurityAdd.setNextFocusableComponent(txtLimit_SD);
        btnSecurityAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSecurityAddActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 7, 0, 4);
        panItemSecurity.add(btnSecurityAdd, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 4;
        panSecurityType.add(panItemSecurity, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 9;
        panSecurityDetails_security.add(panSecurityType, gridBagConstraints);

        panSecurityParticulars.setMinimumSize(new java.awt.Dimension(470, 70));
        panSecurityParticulars.setPreferredSize(new java.awt.Dimension(470, 70));

        lblParticulars.setText("Particulars");

        srpTxtAreaParticulars.setMinimumSize(new java.awt.Dimension(335, 45));
        srpTxtAreaParticulars.setPreferredSize(new java.awt.Dimension(335, 45));

        txtAreaParticular.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        txtAreaParticular.setLineWrap(true);
        txtAreaParticular.setMinimumSize(new java.awt.Dimension(20, 100));
        txtAreaParticular.setPreferredSize(new java.awt.Dimension(20, 100));
        srpTxtAreaParticulars.setViewportView(txtAreaParticular);

        lblNoOfPacket.setText("No of Packet");

        txtNoOfPacket.setMinimumSize(new java.awt.Dimension(30, 21));
        txtNoOfPacket.setPreferredSize(new java.awt.Dimension(30, 21));

        cLabel2.setText("Service Tax");

        javax.swing.GroupLayout panSecurityParticularsLayout = new javax.swing.GroupLayout(panSecurityParticulars);
        panSecurityParticulars.setLayout(panSecurityParticularsLayout);
        panSecurityParticularsLayout.setHorizontalGroup(
            panSecurityParticularsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSecurityParticularsLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(panSecurityParticularsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panSecurityParticularsLayout.createSequentialGroup()
                        .addComponent(lblParticulars, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17)
                        .addComponent(srpTxtAreaParticulars, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panSecurityParticularsLayout.createSequentialGroup()
                        .addComponent(lblNoOfPacket, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(txtNoOfPacket, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblServiceTaxOpeningVal, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        panSecurityParticularsLayout.setVerticalGroup(
            panSecurityParticularsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSecurityParticularsLayout.createSequentialGroup()
                .addGroup(panSecurityParticularsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panSecurityParticularsLayout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(lblParticulars, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(srpTxtAreaParticulars, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addGroup(panSecurityParticularsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panSecurityParticularsLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(lblNoOfPacket, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panSecurityParticularsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtNoOfPacket, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblServiceTaxOpeningVal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(1, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 9;
        panSecurityDetails_security.add(panSecurityParticulars, gridBagConstraints);

        panChargeDetails.setMinimumSize(new java.awt.Dimension(430, 110));
        panChargeDetails.setPreferredSize(new java.awt.Dimension(430, 110));
        panChargeDetails.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 9;
        panSecurityDetails_security.add(panChargeDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBorrowProfile.add(panSecurityDetails_security, gridBagConstraints);

        cPanel1.setLayout(new java.awt.GridBagLayout());

        panBorrowProfile_CustName.setBorder(javax.swing.BorderFactory.createTitledBorder("Customer Details"));
        panBorrowProfile_CustName.setMaximumSize(new java.awt.Dimension(440, 340));
        panBorrowProfile_CustName.setMinimumSize(new java.awt.Dimension(400, 320));
        panBorrowProfile_CustName.setPreferredSize(new java.awt.Dimension(405, 320));
        panBorrowProfile_CustName.setLayout(new java.awt.GridBagLayout());

        panBorrowProfile_CustID1.setMaximumSize(new java.awt.Dimension(395, 240));
        panBorrowProfile_CustID1.setMinimumSize(new java.awt.Dimension(390, 170));
        panBorrowProfile_CustID1.setPreferredSize(new java.awt.Dimension(390, 170));
        panBorrowProfile_CustID1.setLayout(new java.awt.GridBagLayout());

        panExistingCustomer.setLayout(new java.awt.GridBagLayout());

        rdoIsSubmitted_DocumentDetails.add(rdoExistingCustomer_Yes);
        rdoExistingCustomer_Yes.setText("Yes");
        rdoExistingCustomer_Yes.setMinimumSize(new java.awt.Dimension(49, 18));
        rdoExistingCustomer_Yes.setPreferredSize(new java.awt.Dimension(49, 18));
        rdoExistingCustomer_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoExistingCustomer_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        panExistingCustomer.add(rdoExistingCustomer_Yes, gridBagConstraints);

        rdoIsSubmitted_DocumentDetails.add(rdoExistingCustomer_No);
        rdoExistingCustomer_No.setText("No");
        rdoExistingCustomer_No.setMaximumSize(new java.awt.Dimension(41, 18));
        rdoExistingCustomer_No.setMinimumSize(new java.awt.Dimension(41, 18));
        rdoExistingCustomer_No.setPreferredSize(new java.awt.Dimension(41, 18));
        rdoExistingCustomer_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoExistingCustomer_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        panExistingCustomer.add(rdoExistingCustomer_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panBorrowProfile_CustID1.add(panExistingCustomer, gridBagConstraints);

        lblAccountNo.setText("Member No / Account No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 2);
        panBorrowProfile_CustID1.add(lblAccountNo, gridBagConstraints);

        txtAccountNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccountNo.setNextFocusableComponent(txtCustID);
        txtAccountNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccountNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panBorrowProfile_CustID1.add(txtAccountNo, gridBagConstraints);

        lblCustID.setText("Customer Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 2);
        panBorrowProfile_CustID1.add(lblCustID, gridBagConstraints);

        panCustId.setFocusable(false);
        panCustId.setMinimumSize(new java.awt.Dimension(125, 24));
        panCustId.setPreferredSize(new java.awt.Dimension(125, 24));
        panCustId.setLayout(new java.awt.GridBagLayout());

        txtCustID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCustID.setNextFocusableComponent(btnCustID);
        txtCustID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCustIDActionPerformed(evt);
            }
        });
        txtCustID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCustIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCustId.add(txtCustID, gridBagConstraints);

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
        panCustId.add(btnCustID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 0, 0);
        panBorrowProfile_CustID1.add(panCustId, gridBagConstraints);

        lblCustNameValue.setForeground(new java.awt.Color(0, 51, 204));
        lblCustNameValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblCustNameValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblCustNameValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblCustNameValue.setMinimumSize(new java.awt.Dimension(150, 16));
        lblCustNameValue.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 14);
        panBorrowProfile_CustID1.add(lblCustNameValue, gridBagConstraints);

        lblDealingWithBankSince.setText("Dealing With the Bank Since");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 2);
        panBorrowProfile_CustID1.add(lblDealingWithBankSince, gridBagConstraints);

        tdtDealingWithBankSince.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtDealingWithBankSinceFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 0);
        panBorrowProfile_CustID1.add(tdtDealingWithBankSince, gridBagConstraints);

        lblConstitution.setText("Constitution");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 2);
        panBorrowProfile_CustID1.add(lblConstitution, gridBagConstraints);

        cboConstitution.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboConstitution.setMinimumSize(new java.awt.Dimension(100, 21));
        cboConstitution.setPopupWidth(235);
        cboConstitution.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboConstitutionActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 0);
        panBorrowProfile_CustID1.add(cboConstitution, gridBagConstraints);

        lblCategory.setText("Category");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 2);
        panBorrowProfile_CustID1.add(lblCategory, gridBagConstraints);

        cboCategory.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboCategory.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCategory.setNextFocusableComponent(tdtSanctionDate);
        cboCategory.setPopupWidth(225);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 0);
        panBorrowProfile_CustID1.add(cboCategory, gridBagConstraints);

        lblExistingCustomer.setText("Existing Customer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 2);
        panBorrowProfile_CustID1.add(lblExistingCustomer, gridBagConstraints);

        btnMembershipLia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/members2.jpg"))); // NOI18N
        btnMembershipLia.setToolTipText("View MemberShip Liability");
        btnMembershipLia.setMaximumSize(new java.awt.Dimension(15, 15));
        btnMembershipLia.setMinimumSize(new java.awt.Dimension(15, 15));
        btnMembershipLia.setPreferredSize(new java.awt.Dimension(15, 15));
        btnMembershipLia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMembershipLiaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panBorrowProfile_CustID1.add(btnMembershipLia, gridBagConstraints);

        lblCustMobNo.setText("Mob No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panBorrowProfile_CustID1.add(lblCustMobNo, gridBagConstraints);

        txtCustMobNo.setAllowAll(true);
        txtCustMobNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCustMobNo.setNextFocusableComponent(tdtDealingWithBankSince);
        txtCustMobNo.setPreferredSize(new java.awt.Dimension(80, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panBorrowProfile_CustID1.add(txtCustMobNo, gridBagConstraints);

        btnGoldLiability.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/report.gif"))); // NOI18N
        btnGoldLiability.setToolTipText("Gold liability");
        btnGoldLiability.setMaximumSize(new java.awt.Dimension(15, 15));
        btnGoldLiability.setMinimumSize(new java.awt.Dimension(15, 15));
        btnGoldLiability.setPreferredSize(new java.awt.Dimension(15, 15));
        btnGoldLiability.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGoldLiabilityActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 18);
        panBorrowProfile_CustID1.add(btnGoldLiability, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 1, 0);
        panBorrowProfile_CustName.add(panBorrowProfile_CustID1, gridBagConstraints);

        panBorrowProfile_CustID.setMaximumSize(new java.awt.Dimension(375, 105));
        panBorrowProfile_CustID.setMinimumSize(new java.awt.Dimension(375, 105));
        panBorrowProfile_CustID.setPreferredSize(new java.awt.Dimension(375, 105));
        panBorrowProfile_CustID.setLayout(new java.awt.GridBagLayout());

        panBorrowerTabCTable.setMinimumSize(new java.awt.Dimension(370, 80));
        panBorrowerTabCTable.setPreferredSize(new java.awt.Dimension(370, 80));
        panBorrowerTabCTable.setLayout(new java.awt.GridBagLayout());

        srpBorrowerTabCTable.setMinimumSize(new java.awt.Dimension(370, 60));
        srpBorrowerTabCTable.setPreferredSize(new java.awt.Dimension(370, 60));

        tblBorrowerTabCTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblBorrowerTabCTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBorrowerTabCTableMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblBorrowerTabCTableMousePressed(evt);
            }
        });
        srpBorrowerTabCTable.setViewportView(tblBorrowerTabCTable);

        panBorrowerTabCTable.add(srpBorrowerTabCTable, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panBorrowProfile_CustID.add(panBorrowerTabCTable, gridBagConstraints);

        panBorrowerTabTools.setMinimumSize(new java.awt.Dimension(228, 33));
        panBorrowerTabTools.setPreferredSize(new java.awt.Dimension(228, 33));
        panBorrowerTabTools.setLayout(new java.awt.GridBagLayout());

        btnNew_Borrower.setText("New");
        btnNew_Borrower.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNew_BorrowerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panBorrowerTabTools.add(btnNew_Borrower, gridBagConstraints);

        btnToMain_Borrower.setText("To Main");
        btnToMain_Borrower.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToMain_BorrowerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        panBorrowerTabTools.add(btnToMain_Borrower, gridBagConstraints);

        btnDeleteBorrower.setText("Delete");
        btnDeleteBorrower.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteBorrowerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panBorrowerTabTools.add(btnDeleteBorrower, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 7, 0);
        panBorrowProfile_CustID.add(panBorrowerTabTools, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panBorrowProfile_CustName.add(panBorrowProfile_CustID, gridBagConstraints);

        jPanel2.setMinimumSize(new java.awt.Dimension(302, 25));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel1.setForeground(new java.awt.Color(51, 102, 255));
        jLabel1.setText("Next Account Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel2.add(jLabel1, gridBagConstraints);

        txtNextAccNo.setEditable(false);
        txtNextAccNo.setEnabled(false);
        txtNextAccNo.setPreferredSize(new java.awt.Dimension(150, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel2.add(txtNextAccNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panBorrowProfile_CustName.add(jPanel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        cPanel1.add(panBorrowProfile_CustName, gridBagConstraints);

        cPanel2.setMinimumSize(new java.awt.Dimension(280, 23));
        cPanel2.setPreferredSize(new java.awt.Dimension(280, 23));
        cPanel2.setLayout(new java.awt.GridBagLayout());

        lblGoldLoanProd.setText("Loan Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel2.add(lblGoldLoanProd, gridBagConstraints);

        cboGoldLoanProd.setMaximumRowCount(12);
        cboGoldLoanProd.setMinimumSize(new java.awt.Dimension(150, 21));
        cboGoldLoanProd.setPreferredSize(new java.awt.Dimension(150, 21));
        cboGoldLoanProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboGoldLoanProdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        cPanel2.add(cboGoldLoanProd, gridBagConstraints);

        cPanel1.add(cPanel2, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        panBorrowProfile.add(cPanel1, gridBagConstraints);

        panBorrowCompanyDetails.add(panBorrowProfile, new java.awt.GridBagConstraints());

        jPanel3.setMinimumSize(new java.awt.Dimension(940, 210));
        jPanel3.setPreferredSize(new java.awt.Dimension(940, 210));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        panSanctionDetails_Table.setBorder(javax.swing.BorderFactory.createTitledBorder("Sanction Details"));
        panSanctionDetails_Table.setMinimumSize(new java.awt.Dimension(940, 250));
        panSanctionDetails_Table.setPreferredSize(new java.awt.Dimension(940, 200));
        panSanctionDetails_Table.setLayout(new java.awt.GridBagLayout());

        panTableFields_SD.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panTableFields_SD.setMinimumSize(new java.awt.Dimension(520, 200));
        panTableFields_SD.setPreferredSize(new java.awt.Dimension(520, 200));
        panTableFields_SD.setLayout(new java.awt.GridBagLayout());

        panDemandPromssoryDate.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Demand Promissory Note", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 11))); // NOI18N
        panDemandPromssoryDate.setMinimumSize(new java.awt.Dimension(220, 75));
        panDemandPromssoryDate.setPreferredSize(new java.awt.Dimension(220, 95));
        panDemandPromssoryDate.setLayout(new java.awt.GridBagLayout());

        lblDemandPromNoteDate.setText("Issue Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 2);
        panDemandPromssoryDate.add(lblDemandPromNoteDate, gridBagConstraints);

        tdtDemandPromNoteDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtDemandPromNoteDate.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panDemandPromssoryDate.add(tdtDemandPromNoteDate, gridBagConstraints);

        lblDemandPromNoteExpDate.setText("Expiry Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 2);
        panDemandPromssoryDate.add(lblDemandPromNoteExpDate, gridBagConstraints);

        tdtDemandPromNoteExpDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtDemandPromNoteExpDate.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panDemandPromssoryDate.add(tdtDemandPromNoteExpDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panDemandPromssoryDate.add(tdtEmiDate, gridBagConstraints);

        lblEmiDate.setText("EMIF Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panDemandPromssoryDate.add(lblEmiDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panTableFields_SD.add(panDemandPromssoryDate, gridBagConstraints);

        panInterestDeails.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Interest Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 11))); // NOI18N
        panInterestDeails.setMinimumSize(new java.awt.Dimension(220, 75));
        panInterestDeails.setPreferredSize(new java.awt.Dimension(220, 75));
        panInterestDeails.setLayout(new java.awt.GridBagLayout());

        lblPenalInter.setText("Penal Interest %");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 8, 2);
        panInterestDeails.add(lblPenalInter, gridBagConstraints);

        lblInter.setText("Interest %");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 2);
        panInterestDeails.add(lblInter, gridBagConstraints);

        txtPenalInter.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 8, 0);
        panInterestDeails.add(txtPenalInter, gridBagConstraints);

        txtInter.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panInterestDeails.add(txtInter, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panTableFields_SD.add(panInterestDeails, gridBagConstraints);

        panInterestDeails1.setMinimumSize(new java.awt.Dimension(260, 155));
        panInterestDeails1.setPreferredSize(new java.awt.Dimension(265, 170));
        panInterestDeails1.setLayout(new java.awt.GridBagLayout());

        lblLimit_SD.setText("Limit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 2);
        panInterestDeails1.add(lblLimit_SD, gridBagConstraints);

        txtLimit_SD.setMinimumSize(new java.awt.Dimension(100, 21));
        txtLimit_SD.setNextFocusableComponent(txtNoInstallments);
        txtLimit_SD.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtLimit_SDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panInterestDeails1.add(txtLimit_SD, gridBagConstraints);

        txtNoInstallments.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNoInstallments.setNextFocusableComponent(cboRepayFreq);
        txtNoInstallments.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNoInstallmentsFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panInterestDeails1.add(txtNoInstallments, gridBagConstraints);

        lblNoInstallments.setText("No. of Installments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 2);
        panInterestDeails1.add(lblNoInstallments, gridBagConstraints);

        cboRepayFreq.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboRepayFreq.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRepayFreq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboRepayFreqActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panInterestDeails1.add(cboRepayFreq, gridBagConstraints);

        lblRepayFreq.setText("Repay Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 2);
        panInterestDeails1.add(lblRepayFreq, gridBagConstraints);

        lblAccCloseDt.setText("Account Open Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 2);
        panInterestDeails1.add(lblAccCloseDt, gridBagConstraints);

        lblAccOpenDt.setText("Account Open Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 2);
        panInterestDeails1.add(lblAccOpenDt, gridBagConstraints);

        lblAccStatus.setText("Account Status");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 2);
        panInterestDeails1.add(lblAccStatus, gridBagConstraints);

        cboAccStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboAccStatus.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panInterestDeails1.add(cboAccStatus, gridBagConstraints);

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
        panInterestDeails1.add(tdtAccountOpenDate, gridBagConstraints);

        tdtAccountCloseDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtAccountCloseDate.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panInterestDeails1.add(tdtAccountCloseDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panTableFields_SD.add(panInterestDeails1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 16);
        panSanctionDetails_Table.add(panTableFields_SD, gridBagConstraints);

        panSanctionDetails_Sanction.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panSanctionDetails_Sanction.setMinimumSize(new java.awt.Dimension(350, 200));
        panSanctionDetails_Sanction.setPreferredSize(new java.awt.Dimension(350, 200));
        panSanctionDetails_Sanction.setLayout(new java.awt.GridBagLayout());

        lblSanctionDate.setText("Sanction Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 2);
        panSanctionDetails_Sanction.add(lblSanctionDate, gridBagConstraints);

        tdtSanctionDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtSanctionDate.setNextFocusableComponent(cboSanctioningAuthority);
        tdtSanctionDate.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtSanctionDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtSanctionDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 0);
        panSanctionDetails_Sanction.add(tdtSanctionDate, gridBagConstraints);

        cboSanctioningAuthority.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboSanctioningAuthority.setMinimumSize(new java.awt.Dimension(100, 21));
        cboSanctioningAuthority.setPopupWidth(200);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 0);
        panSanctionDetails_Sanction.add(cboSanctioningAuthority, gridBagConstraints);

        txtSanctionRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSanctionRemarks.setNextFocusableComponent(txtGrossWeight);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 0);
        panSanctionDetails_Sanction.add(txtSanctionRemarks, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 2);
        panSanctionDetails_Sanction.add(lblRemarks, gridBagConstraints);

        lblSanctioningAuthority.setText("Sanctioning Authority");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 2);
        panSanctionDetails_Sanction.add(lblSanctioningAuthority, gridBagConstraints);

        lblPurposeOfLoan.setText("Purpose of loan");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 2);
        panSanctionDetails_Sanction.add(lblPurposeOfLoan, gridBagConstraints);

        cboPurposeOfLoan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboPurposeOfLoan.setMinimumSize(new java.awt.Dimension(100, 21));
        cboPurposeOfLoan.setNextFocusableComponent(txtSanctionRemarks);
        cboPurposeOfLoan.setPopupWidth(115);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 0);
        panSanctionDetails_Sanction.add(cboPurposeOfLoan, gridBagConstraints);

        panGender.setName("panGender"); // NOI18N
        panGender.setLayout(new java.awt.GridBagLayout());

        rdoPriority.setText("Priority");
        rdoPriority.setMaximumSize(new java.awt.Dimension(77, 18));
        rdoPriority.setMinimumSize(new java.awt.Dimension(77, 18));
        rdoPriority.setName("rdoGender_Male"); // NOI18N
        rdoPriority.setPreferredSize(new java.awt.Dimension(77, 18));
        rdoPriority.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPriorityActionPerformed(evt);
            }
        });
        panGender.add(rdoPriority, new java.awt.GridBagConstraints());

        rdoNonPriority.setText("Non Priority");
        rdoNonPriority.setMaximumSize(new java.awt.Dimension(100, 27));
        rdoNonPriority.setMinimumSize(new java.awt.Dimension(100, 27));
        rdoNonPriority.setName("rdoGender_Female"); // NOI18N
        rdoNonPriority.setPreferredSize(new java.awt.Dimension(100, 18));
        rdoNonPriority.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoNonPriorityActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panGender.add(rdoNonPriority, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 2);
        panSanctionDetails_Sanction.add(panGender, gridBagConstraints);

        lblAcctNo_Sanction.setText("Loan Account No :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 2);
        panSanctionDetails_Sanction.add(lblAcctNo_Sanction, gridBagConstraints);

        lblAcctNo_Sanction_Disp.setForeground(new java.awt.Color(0, 51, 204));
        lblAcctNo_Sanction_Disp.setFont(new java.awt.Font("MS Sans Serif", 1, 15)); // NOI18N
        lblAcctNo_Sanction_Disp.setMaximumSize(new java.awt.Dimension(120, 15));
        lblAcctNo_Sanction_Disp.setMinimumSize(new java.awt.Dimension(120, 15));
        lblAcctNo_Sanction_Disp.setPreferredSize(new java.awt.Dimension(120, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 2);
        panSanctionDetails_Sanction.add(lblAcctNo_Sanction_Disp, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        btnRenew.setForeground(new java.awt.Color(255, 0, 51));
        btnRenew.setText("Renew");
        btnRenew.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnRenew.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnRenew.setMaximumSize(new java.awt.Dimension(60, 27));
        btnRenew.setMinimumSize(new java.awt.Dimension(60, 27));
        btnRenew.setPreferredSize(new java.awt.Dimension(60, 27));
        btnRenew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRenewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel1.add(btnRenew, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panSanctionDetails_Sanction.add(jPanel1, gridBagConstraints);

        lblSanRepaymentType.setText("Install Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 2);
        panSanctionDetails_Sanction.add(lblSanRepaymentType, gridBagConstraints);

        cboRepaymentType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboRepaymentType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRepaymentType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboRepaymentTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panSanctionDetails_Sanction.add(cboRepaymentType, gridBagConstraints);

        btnRepayShedule.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/uicomponent/images/schedule.gif"))); // NOI18N
        btnRepayShedule.setToolTipText("Loan Repayment schedule");
        btnRepayShedule.setFocusable(false);
        btnRepayShedule.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRepayShedule.setMaximumSize(new java.awt.Dimension(28, 27));
        btnRepayShedule.setMinimumSize(new java.awt.Dimension(28, 27));
        btnRepayShedule.setPreferredSize(new java.awt.Dimension(28, 27));
        btnRepayShedule.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRepayShedule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRepaySheduleActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 43);
        panSanctionDetails_Sanction.add(btnRepayShedule, gridBagConstraints);

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
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel3.add(panSanctionDetails_Table, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panBorrowCompanyDetails.add(jPanel3, gridBagConstraints);

        tabLimitAmount.addTab("Customer/Sanction Details", panBorrowCompanyDetails);

        panDocumentDetails.setMinimumSize(new java.awt.Dimension(940, 550));
        panDocumentDetails.setPreferredSize(new java.awt.Dimension(940, 550));
        panDocumentDetails.setLayout(new java.awt.GridBagLayout());

        panTable_DocumentDetails.setMaximumSize(new java.awt.Dimension(425, 345));
        panTable_DocumentDetails.setMinimumSize(new java.awt.Dimension(425, 345));
        panTable_DocumentDetails.setPreferredSize(new java.awt.Dimension(425, 345));
        panTable_DocumentDetails.setLayout(new java.awt.GridBagLayout());

        srpTable_DocumentDetails.setMaximumSize(new java.awt.Dimension(425, 340));
        srpTable_DocumentDetails.setMinimumSize(new java.awt.Dimension(425, 340));
        srpTable_DocumentDetails.setPreferredSize(new java.awt.Dimension(425, 340));

        tblTable_DocumentDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblTable_DocumentDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblTable_DocumentDetailsMousePressed(evt);
            }
        });
        srpTable_DocumentDetails.setViewportView(tblTable_DocumentDetails);

        panTable_DocumentDetails.add(srpTable_DocumentDetails, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDocumentDetails.add(panTable_DocumentDetails, gridBagConstraints);

        panTabDetails_DocumentDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panTabDetails_DocumentDetails.setMaximumSize(new java.awt.Dimension(260, 350));
        panTabDetails_DocumentDetails.setMinimumSize(new java.awt.Dimension(260, 340));
        panTabDetails_DocumentDetails.setPreferredSize(new java.awt.Dimension(260, 340));
        panTabDetails_DocumentDetails.setLayout(new java.awt.GridBagLayout());

        lblDocType_DocumentDetails.setText("Document Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(lblDocType_DocumentDetails, gridBagConstraints);

        lblDocType_Disp_DocumentDetails.setMaximumSize(new java.awt.Dimension(100, 15));
        lblDocType_Disp_DocumentDetails.setMinimumSize(new java.awt.Dimension(100, 15));
        lblDocType_Disp_DocumentDetails.setPreferredSize(new java.awt.Dimension(100, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(lblDocType_Disp_DocumentDetails, gridBagConstraints);

        lblDocNo_DocumentDetails.setText("Document Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(lblDocNo_DocumentDetails, gridBagConstraints);

        lblDocNo_Disp_DocumentDetails.setMaximumSize(new java.awt.Dimension(100, 15));
        lblDocNo_Disp_DocumentDetails.setMinimumSize(new java.awt.Dimension(100, 15));
        lblDocNo_Disp_DocumentDetails.setPreferredSize(new java.awt.Dimension(100, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(lblDocNo_Disp_DocumentDetails, gridBagConstraints);

        lblDocDesc_DocumentDetails.setText("Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(lblDocDesc_DocumentDetails, gridBagConstraints);

        lblDocDesc_Disp_DocumentDetails.setMaximumSize(new java.awt.Dimension(100, 15));
        lblDocDesc_Disp_DocumentDetails.setMinimumSize(new java.awt.Dimension(100, 15));
        lblDocDesc_Disp_DocumentDetails.setPreferredSize(new java.awt.Dimension(100, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(lblDocDesc_Disp_DocumentDetails, gridBagConstraints);

        lblSubmitted_DocumentDetails.setText("Is Submitted?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(lblSubmitted_DocumentDetails, gridBagConstraints);

        panSubmitted_DocumentDetails.setLayout(new java.awt.GridBagLayout());

        rdoIsSubmitted_DocumentDetails.add(rdoYes_DocumentDetails);
        rdoYes_DocumentDetails.setText("Yes");
        rdoYes_DocumentDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoYes_DocumentDetailsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        panSubmitted_DocumentDetails.add(rdoYes_DocumentDetails, gridBagConstraints);

        rdoIsSubmitted_DocumentDetails.add(rdoNo_DocumentDetails);
        rdoNo_DocumentDetails.setText("No");
        rdoNo_DocumentDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoNo_DocumentDetailsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        panSubmitted_DocumentDetails.add(rdoNo_DocumentDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(panSubmitted_DocumentDetails, gridBagConstraints);

        lblSubmitDate_DocumentDetails.setText("Submitted Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(lblSubmitDate_DocumentDetails, gridBagConstraints);

        tdtSubmitDate_DocumentDetails.setMaximumSize(new java.awt.Dimension(100, 21));
        tdtSubmitDate_DocumentDetails.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtSubmitDate_DocumentDetails.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtSubmitDate_DocumentDetails.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtSubmitDate_DocumentDetailsFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(tdtSubmitDate_DocumentDetails, gridBagConstraints);

        lblRemarks_DocumentDetails.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(lblRemarks_DocumentDetails, gridBagConstraints);

        txtRemarks_DocumentDetails.setMaximumSize(new java.awt.Dimension(100, 21));
        txtRemarks_DocumentDetails.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(txtRemarks_DocumentDetails, gridBagConstraints);

        btnSave_DocumentDetails.setText("Save");
        btnSave_DocumentDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSave_DocumentDetailsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(btnSave_DocumentDetails, gridBagConstraints);

        lblMandatory_DOC.setText(" Is Mandatory?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(lblMandatory_DOC, gridBagConstraints);

        panMandatory_DOC.setLayout(new java.awt.GridBagLayout());

        rdoMandatory_DOC.add(rdoYes_Mandatory_DOC);
        rdoYes_Mandatory_DOC.setText("Yes");
        panMandatory_DOC.add(rdoYes_Mandatory_DOC, new java.awt.GridBagConstraints());

        rdoMandatory_DOC.add(rdoNo_Mandatory_DOC);
        rdoNo_Mandatory_DOC.setText("No");
        panMandatory_DOC.add(rdoNo_Mandatory_DOC, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(panMandatory_DOC, gridBagConstraints);

        lblExecuted_DOC.setText(" Is Executed?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(lblExecuted_DOC, gridBagConstraints);

        panExecuted_DOC.setLayout(new java.awt.GridBagLayout());

        rdoExecuted_DOC.add(rdoYes_Executed_DOC);
        rdoYes_Executed_DOC.setText("Yes");
        rdoYes_Executed_DOC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoYes_Executed_DOCActionPerformed(evt);
            }
        });
        panExecuted_DOC.add(rdoYes_Executed_DOC, new java.awt.GridBagConstraints());

        rdoExecuted_DOC.add(rdoNo_Executed_DOC);
        rdoNo_Executed_DOC.setText("No");
        rdoNo_Executed_DOC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoNo_Executed_DOCActionPerformed(evt);
            }
        });
        panExecuted_DOC.add(rdoNo_Executed_DOC, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(panExecuted_DOC, gridBagConstraints);

        lblExecuteDate_DOC.setText("Executed Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(lblExecuteDate_DOC, gridBagConstraints);

        tdtExecuteDate_DOC.setMaximumSize(new java.awt.Dimension(100, 21));
        tdtExecuteDate_DOC.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtExecuteDate_DOC.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(tdtExecuteDate_DOC, gridBagConstraints);

        lblExpiryDate_DOC.setText("Expiry Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(lblExpiryDate_DOC, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTabDetails_DocumentDetails.add(tdtExpiryDate_DOC, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panDocumentDetails.add(panTabDetails_DocumentDetails, gridBagConstraints);

        panSecurity1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panSecurity1.setMaximumSize(new java.awt.Dimension(800, 120));
        panSecurity1.setMinimumSize(new java.awt.Dimension(800, 120));
        panSecurity1.setPreferredSize(new java.awt.Dimension(800, 120));
        panSecurity1.setLayout(new java.awt.GridBagLayout());

        lblDocumentProdId.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDocumentProdId.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        panSecurity1.add(lblDocumentProdId, gridBagConstraints);

        lblDocumentProdIdValue.setMaximumSize(new java.awt.Dimension(100, 15));
        lblDocumentProdIdValue.setMinimumSize(new java.awt.Dimension(100, 15));
        lblDocumentProdIdValue.setPreferredSize(new java.awt.Dimension(100, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSecurity1.add(lblDocumentProdIdValue, gridBagConstraints);

        lblDocumentCustName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDocumentCustName.setText("Customer Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        panSecurity1.add(lblDocumentCustName, gridBagConstraints);

        lblDocumentCustNameValue.setMaximumSize(new java.awt.Dimension(200, 16));
        lblDocumentCustNameValue.setMinimumSize(new java.awt.Dimension(200, 16));
        lblDocumentCustNameValue.setPreferredSize(new java.awt.Dimension(200, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSecurity1.add(lblDocumentCustNameValue, gridBagConstraints);

        lblDocumentAccNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDocumentAccNo.setText("Account No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSecurity1.add(lblDocumentAccNo, gridBagConstraints);

        lblDocumentAccNoValue.setForeground(new java.awt.Color(0, 51, 204));
        lblDocumentAccNoValue.setMaximumSize(new java.awt.Dimension(125, 15));
        lblDocumentAccNoValue.setMinimumSize(new java.awt.Dimension(125, 15));
        lblDocumentAccNoValue.setPreferredSize(new java.awt.Dimension(125, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSecurity1.add(lblDocumentAccNoValue, gridBagConstraints);

        lblDocumentAccHead.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDocumentAccHead.setText("Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSecurity1.add(lblDocumentAccHead, gridBagConstraints);

        lblDocumentAccHeadValue.setMaximumSize(new java.awt.Dimension(100, 15));
        lblDocumentAccHeadValue.setMinimumSize(new java.awt.Dimension(100, 15));
        lblDocumentAccHeadValue.setPreferredSize(new java.awt.Dimension(100, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSecurity1.add(lblDocumentAccHeadValue, gridBagConstraints);

        panMobileBanking.setBorder(javax.swing.BorderFactory.createTitledBorder("Mobile Banking"));
        panMobileBanking.setMinimumSize(new java.awt.Dimension(380, 80));
        panMobileBanking.setPreferredSize(new java.awt.Dimension(380, 80));
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
        txtMobileNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtMobileNoKeyTyped(evt);
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
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        panSecurity1.add(panMobileBanking, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 8, 4);
        panDocumentDetails.add(panSecurity1, gridBagConstraints);

        tabLimitAmount.addTab("Document Details", panDocumentDetails);

        panBorrowRenewalCompanyDetails.setMinimumSize(new java.awt.Dimension(659, 545));
        panBorrowRenewalCompanyDetails.setPreferredSize(new java.awt.Dimension(659, 545));
        panBorrowRenewalCompanyDetails.setLayout(new java.awt.GridBagLayout());

        panBorrowProfile1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panBorrowProfile1.setMinimumSize(new java.awt.Dimension(940, 335));
        panBorrowProfile1.setPreferredSize(new java.awt.Dimension(940, 335));
        panBorrowProfile1.setLayout(new java.awt.GridBagLayout());

        panRenewalSecurityDetails_security.setBorder(javax.swing.BorderFactory.createTitledBorder("Renewal  Security Details"));
        panRenewalSecurityDetails_security.setMaximumSize(new java.awt.Dimension(425, 325));
        panRenewalSecurityDetails_security.setMinimumSize(new java.awt.Dimension(475, 325));
        panRenewalSecurityDetails_security.setPreferredSize(new java.awt.Dimension(475, 325));
        panRenewalSecurityDetails_security.setLayout(new java.awt.GridBagLayout());

        panSecurityType1.setMinimumSize(new java.awt.Dimension(450, 145));
        panSecurityType1.setPreferredSize(new java.awt.Dimension(450, 145));
        panSecurityType1.setLayout(new java.awt.GridBagLayout());

        txtRenewalGrossWeight.setAllowAll(true);
        txtRenewalGrossWeight.setAllowNumber(true);
        txtRenewalGrossWeight.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panSecurityType1.add(txtRenewalGrossWeight, gridBagConstraints);

        lblRenewalNetWeight.setText(" Net Weight");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 4);
        panSecurityType1.add(lblRenewalNetWeight, gridBagConstraints);

        txtRenewalNetWeight.setAllowAll(true);
        txtRenewalNetWeight.setAllowNumber(true);
        txtRenewalNetWeight.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRenewalNetWeight.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRenewalNetWeightFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panSecurityType1.add(txtRenewalNetWeight, gridBagConstraints);

        lblRenewalGrossWeight.setText("Gross Weight");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 4);
        panSecurityType1.add(lblRenewalGrossWeight, gridBagConstraints);

        txtRenewalSecurityValue.setAllowAll(true);
        txtRenewalSecurityValue.setAllowNumber(true);
        txtRenewalSecurityValue.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panSecurityType1.add(txtRenewalSecurityValue, gridBagConstraints);

        lblRenewalSecurityValue.setText("Security Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 4);
        panSecurityType1.add(lblRenewalSecurityValue, gridBagConstraints);

        txtRenewalMarketRate.setAllowAll(true);
        txtRenewalMarketRate.setAllowNumber(true);
        txtRenewalMarketRate.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panSecurityType1.add(txtRenewalMarketRate, gridBagConstraints);

        lblRenewalMarketRate.setText("Market Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 4);
        panSecurityType1.add(lblRenewalMarketRate, gridBagConstraints);

        lblRenewalpurity.setText("Purity");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 4);
        panSecurityType1.add(lblRenewalpurity, gridBagConstraints);

        cboRenewalPurityOfGold.setMaximumSize(new java.awt.Dimension(100, 21));
        cboRenewalPurityOfGold.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRenewalPurityOfGold.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboRenewalPurityOfGoldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panSecurityType1.add(cboRenewalPurityOfGold, gridBagConstraints);

        lblRenewalMargin.setText("Margin %");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 4);
        panSecurityType1.add(lblRenewalMargin, gridBagConstraints);

        txtRenewalMargin.setAllowAll(true);
        txtRenewalMargin.setAllowNumber(true);
        txtRenewalMargin.setMaximumSize(new java.awt.Dimension(100, 21));
        txtRenewalMargin.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panSecurityType1.add(txtRenewalMargin, gridBagConstraints);

        lblRenewalMarginAmt.setText("Margin Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 4);
        panSecurityType1.add(lblRenewalMarginAmt, gridBagConstraints);

        txtRenewalMarginAmt.setAllowAll(true);
        txtRenewalMarginAmt.setAllowNumber(true);
        txtRenewalMarginAmt.setEnabled(false);
        txtRenewalMarginAmt.setMaximumSize(new java.awt.Dimension(100, 21));
        txtRenewalMarginAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRenewalMarginAmt.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panSecurityType1.add(txtRenewalMarginAmt, gridBagConstraints);

        lblRenewalMaxLoanAmt.setText("Eligible Loan Amt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 18, 0, 4);
        panSecurityType1.add(lblRenewalMaxLoanAmt, gridBagConstraints);

        txtRenewalEligibleLoan.setAllowAll(true);
        txtRenewalEligibleLoan.setAllowNumber(true);
        txtRenewalEligibleLoan.setEnabled(false);
        txtRenewalEligibleLoan.setMaximumSize(new java.awt.Dimension(100, 21));
        txtRenewalEligibleLoan.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRenewalEligibleLoan.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panSecurityType1.add(txtRenewalEligibleLoan, gridBagConstraints);

        lblRenewalAppraiserId.setText("Appraiser Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSecurityType1.add(lblRenewalAppraiserId, gridBagConstraints);

        lblRenewalAppraiserName.setText("Appr Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSecurityType1.add(lblRenewalAppraiserName, gridBagConstraints);

        lblRenewalAppraiserNameValue.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        lblRenewalAppraiserNameValue.setMaximumSize(new java.awt.Dimension(120, 18));
        lblRenewalAppraiserNameValue.setMinimumSize(new java.awt.Dimension(120, 18));
        lblRenewalAppraiserNameValue.setPreferredSize(new java.awt.Dimension(120, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        panSecurityType1.add(lblRenewalAppraiserNameValue, gridBagConstraints);

        cboRenewalAppraiserId.setEditable(true);
        cboRenewalAppraiserId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRenewalAppraiserId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboRenewalAppraiserIdActionPerformed(evt);
            }
        });
        cboRenewalAppraiserId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboRenewalAppraiserIdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panSecurityType1.add(cboRenewalAppraiserId, gridBagConstraints);

        lblRnwLoanType.setText(" Loan Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panSecurityType1.add(lblRnwLoanType, gridBagConstraints);

        cboRnwGoldLoanProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboRnwGoldLoanProdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panSecurityType1.add(cboRnwGoldLoanProd, gridBagConstraints);

        lblRenewCustMobNo.setText("Mob No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panSecurityType1.add(lblRenewCustMobNo, gridBagConstraints);

        txtRenewCustMobNo.setAllowAll(true);
        txtRenewCustMobNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRenewCustMobNo.setNextFocusableComponent(tdtDealingWithBankSince);
        txtRenewCustMobNo.setPreferredSize(new java.awt.Dimension(80, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        panSecurityType1.add(txtRenewCustMobNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 9;
        panRenewalSecurityDetails_security.add(panSecurityType1, gridBagConstraints);

        panSecurityParticulars1.setMinimumSize(new java.awt.Dimension(450, 50));
        panSecurityParticulars1.setPreferredSize(new java.awt.Dimension(450, 50));
        panSecurityParticulars1.setLayout(new java.awt.GridBagLayout());

        lblRenewalParticulars.setText("Particulars");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panSecurityParticulars1.add(lblRenewalParticulars, gridBagConstraints);

        srpTxtAreaParticulars1.setMinimumSize(new java.awt.Dimension(380, 45));
        srpTxtAreaParticulars1.setPreferredSize(new java.awt.Dimension(380, 45));

        txtRenewalAreaParticular.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        txtRenewalAreaParticular.setLineWrap(true);
        txtRenewalAreaParticular.setTabSize(100);
        txtRenewalAreaParticular.setMaximumSize(new java.awt.Dimension(20, 100));
        txtRenewalAreaParticular.setMinimumSize(new java.awt.Dimension(20, 100));
        txtRenewalAreaParticular.setPreferredSize(new java.awt.Dimension(20, 100));
        srpTxtAreaParticulars1.setViewportView(txtRenewalAreaParticular);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panSecurityParticulars1.add(srpTxtAreaParticulars1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 9;
        panRenewalSecurityDetails_security.add(panSecurityParticulars1, gridBagConstraints);

        panRenewalChargeDetails.setMinimumSize(new java.awt.Dimension(430, 110));
        panRenewalChargeDetails.setPreferredSize(new java.awt.Dimension(430, 110));
        panRenewalChargeDetails.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 9;
        panRenewalSecurityDetails_security.add(panRenewalChargeDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        panBorrowProfile1.add(panRenewalSecurityDetails_security, gridBagConstraints);

        panRenewalTransactionDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Pending Due Details"));
        panRenewalTransactionDetails.setMaximumSize(new java.awt.Dimension(425, 325));
        panRenewalTransactionDetails.setMinimumSize(new java.awt.Dimension(425, 325));
        panRenewalTransactionDetails.setPreferredSize(new java.awt.Dimension(425, 325));
        panRenewalTransactionDetails.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        panBorrowProfile1.add(panRenewalTransactionDetails, gridBagConstraints);

        panBorrowRenewalCompanyDetails.add(panBorrowProfile1, new java.awt.GridBagConstraints());

        panSanctionDetails_Table1.setBorder(javax.swing.BorderFactory.createTitledBorder("Renewal  Sanction Details"));
        panSanctionDetails_Table1.setMinimumSize(new java.awt.Dimension(940, 270));
        panSanctionDetails_Table1.setPreferredSize(new java.awt.Dimension(940, 270));
        panSanctionDetails_Table1.setLayout(new java.awt.GridBagLayout());

        panTableFields_SD1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panTableFields_SD1.setMinimumSize(new java.awt.Dimension(520, 240));
        panTableFields_SD1.setPreferredSize(new java.awt.Dimension(520, 240));
        panTableFields_SD1.setLayout(new java.awt.GridBagLayout());

        panRenewalDemandPromssoryDate.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Demand Promissory Note", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 11))); // NOI18N
        panRenewalDemandPromssoryDate.setMinimumSize(new java.awt.Dimension(220, 75));
        panRenewalDemandPromssoryDate.setPreferredSize(new java.awt.Dimension(220, 75));
        panRenewalDemandPromssoryDate.setLayout(new java.awt.GridBagLayout());

        lblRenewalDemandPromNoteDate.setText("Issue Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 2);
        panRenewalDemandPromssoryDate.add(lblRenewalDemandPromNoteDate, gridBagConstraints);

        tdtRenewalDemandPromNoteDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtRenewalDemandPromNoteDate.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panRenewalDemandPromssoryDate.add(tdtRenewalDemandPromNoteDate, gridBagConstraints);

        lblRenewalDemandPromNoteExpDate.setText("Expiry Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 6, 2);
        panRenewalDemandPromssoryDate.add(lblRenewalDemandPromNoteExpDate, gridBagConstraints);

        tdtRenewalDemandPromNoteExpDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtRenewalDemandPromNoteExpDate.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 6, 0);
        panRenewalDemandPromssoryDate.add(tdtRenewalDemandPromNoteExpDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panTableFields_SD1.add(panRenewalDemandPromssoryDate, gridBagConstraints);

        panRenewalInterestDeails2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Renewal Interest Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 11))); // NOI18N
        panRenewalInterestDeails2.setMinimumSize(new java.awt.Dimension(220, 75));
        panRenewalInterestDeails2.setPreferredSize(new java.awt.Dimension(220, 75));
        panRenewalInterestDeails2.setLayout(new java.awt.GridBagLayout());

        lblRenewalPenalInter.setText("Penal Interest %");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 8, 2);
        panRenewalInterestDeails2.add(lblRenewalPenalInter, gridBagConstraints);

        lblRenewalInter.setText("Interest %");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 2);
        panRenewalInterestDeails2.add(lblRenewalInter, gridBagConstraints);

        txtRenewalPenalInter.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 8, 0);
        panRenewalInterestDeails2.add(txtRenewalPenalInter, gridBagConstraints);

        txtRenewalInter.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panRenewalInterestDeails2.add(txtRenewalInter, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panTableFields_SD1.add(panRenewalInterestDeails2, gridBagConstraints);

        panRenewalInterestDeails.setMinimumSize(new java.awt.Dimension(260, 155));
        panRenewalInterestDeails.setPreferredSize(new java.awt.Dimension(260, 155));
        panRenewalInterestDeails.setLayout(new java.awt.GridBagLayout());

        lblRenewalLimit_SD.setText("Limit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 2);
        panRenewalInterestDeails.add(lblRenewalLimit_SD, gridBagConstraints);

        txtRenewalLimit_SD.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRenewalLimit_SD.setNextFocusableComponent(txtNoInstallments);
        txtRenewalLimit_SD.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRenewalLimit_SDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panRenewalInterestDeails.add(txtRenewalLimit_SD, gridBagConstraints);

        txtRenewalNoInstallments.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRenewalNoInstallments.setNextFocusableComponent(cboRepayFreq);
        txtRenewalNoInstallments.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRenewalNoInstallmentsFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panRenewalInterestDeails.add(txtRenewalNoInstallments, gridBagConstraints);

        lblRenewalNoInstallments.setText("No. of Installments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 2);
        panRenewalInterestDeails.add(lblRenewalNoInstallments, gridBagConstraints);

        cboRenewalRepayFreq.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboRenewalRepayFreq.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRenewalRepayFreq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboRenewalRepayFreqActionPerformed(evt);
            }
        });
        cboRenewalRepayFreq.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboRenewalRepayFreqFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panRenewalInterestDeails.add(cboRenewalRepayFreq, gridBagConstraints);

        lblRenewalRepayFreq.setText("Repay Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 2);
        panRenewalInterestDeails.add(lblRenewalRepayFreq, gridBagConstraints);

        lblRenewalAccOpenDt.setText("Account Open Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 2);
        panRenewalInterestDeails.add(lblRenewalAccOpenDt, gridBagConstraints);

        lblRenewalAccStatus.setText("Account Status");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 2);
        panRenewalInterestDeails.add(lblRenewalAccStatus, gridBagConstraints);

        cboRenewalAccStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboRenewalAccStatus.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panRenewalInterestDeails.add(cboRenewalAccStatus, gridBagConstraints);

        tdtRenewalAccountOpenDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtRenewalAccountOpenDate.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtRenewalAccountOpenDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtRenewalAccountOpenDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panRenewalInterestDeails.add(tdtRenewalAccountOpenDate, gridBagConstraints);

        txtCharges.setAllowAll(true);
        txtCharges.setAllowNumber(true);
        txtCharges.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtChargesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 96;
        panRenewalInterestDeails.add(txtCharges, gridBagConstraints);

        lblCharges.setText("Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panRenewalInterestDeails.add(lblCharges, gridBagConstraints);

        cLabel1.setText("Service Tax");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panRenewalInterestDeails.add(cLabel1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        panRenewalInterestDeails.add(lblServiceTaxVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panTableFields_SD1.add(panRenewalInterestDeails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 16);
        panSanctionDetails_Table1.add(panTableFields_SD1, gridBagConstraints);

        panRenewalSanctionDetails_Sanction.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panRenewalSanctionDetails_Sanction.setMinimumSize(new java.awt.Dimension(350, 240));
        panRenewalSanctionDetails_Sanction.setPreferredSize(new java.awt.Dimension(350, 240));
        panRenewalSanctionDetails_Sanction.setLayout(new java.awt.GridBagLayout());

        lblRenewalSanctionDate.setText("Sanction Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 2);
        panRenewalSanctionDetails_Sanction.add(lblRenewalSanctionDate, gridBagConstraints);

        tdtRenewalSanctionDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtRenewalSanctionDate.setNextFocusableComponent(cboSanctioningAuthority);
        tdtRenewalSanctionDate.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 0);
        panRenewalSanctionDetails_Sanction.add(tdtRenewalSanctionDate, gridBagConstraints);

        cboRenewalSanctioningAuthority.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboRenewalSanctioningAuthority.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRenewalSanctioningAuthority.setPopupWidth(200);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 0);
        panRenewalSanctionDetails_Sanction.add(cboRenewalSanctioningAuthority, gridBagConstraints);

        txtRenewalSanctionRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 0);
        panRenewalSanctionDetails_Sanction.add(txtRenewalSanctionRemarks, gridBagConstraints);

        lblRenewalRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 2);
        panRenewalSanctionDetails_Sanction.add(lblRenewalRemarks, gridBagConstraints);

        lblRenewalSanctioningAuthority.setText("Sanctioning Authority");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 2);
        panRenewalSanctionDetails_Sanction.add(lblRenewalSanctioningAuthority, gridBagConstraints);

        lblRenewalPurposeOfLoan.setText("Purpose of loan");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 2);
        panRenewalSanctionDetails_Sanction.add(lblRenewalPurposeOfLoan, gridBagConstraints);

        cboRenewalPurposeOfLoan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboRenewalPurposeOfLoan.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRenewalPurposeOfLoan.setNextFocusableComponent(txtSanctionRemarks);
        cboRenewalPurposeOfLoan.setPopupWidth(115);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 0);
        panRenewalSanctionDetails_Sanction.add(cboRenewalPurposeOfLoan, gridBagConstraints);

        panGender1.setName("panGender"); // NOI18N
        panGender1.setLayout(new java.awt.GridBagLayout());

        rdoRenewalPriorityGroup.add(rdoRenewalPriority);
        rdoRenewalPriority.setText("Priority");
        rdoRenewalPriority.setMaximumSize(new java.awt.Dimension(77, 18));
        rdoRenewalPriority.setMinimumSize(new java.awt.Dimension(77, 18));
        rdoRenewalPriority.setName("rdoGender_Male"); // NOI18N
        rdoRenewalPriority.setPreferredSize(new java.awt.Dimension(77, 18));
        panGender1.add(rdoRenewalPriority, new java.awt.GridBagConstraints());

        rdoRenewalPriorityGroup.add(rdoRenewalNonPriority);
        rdoRenewalNonPriority.setText("Non Priority");
        rdoRenewalNonPriority.setMaximumSize(new java.awt.Dimension(100, 27));
        rdoRenewalNonPriority.setMinimumSize(new java.awt.Dimension(100, 27));
        rdoRenewalNonPriority.setName("rdoGender_Female"); // NOI18N
        rdoRenewalNonPriority.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panGender1.add(rdoRenewalNonPriority, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 2);
        panRenewalSanctionDetails_Sanction.add(panGender1, gridBagConstraints);

        lblRenewalAcctNo_Sanction.setText("Loan Account No :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 2);
        panRenewalSanctionDetails_Sanction.add(lblRenewalAcctNo_Sanction, gridBagConstraints);

        lblRenewalAcctNo_Sanction_Disp.setForeground(new java.awt.Color(0, 51, 204));
        lblRenewalAcctNo_Sanction_Disp.setFont(new java.awt.Font("MS Sans Serif", 1, 15)); // NOI18N
        lblRenewalAcctNo_Sanction_Disp.setMaximumSize(new java.awt.Dimension(120, 15));
        lblRenewalAcctNo_Sanction_Disp.setMinimumSize(new java.awt.Dimension(120, 15));
        lblRenewalAcctNo_Sanction_Disp.setPreferredSize(new java.awt.Dimension(120, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 2);
        panRenewalSanctionDetails_Sanction.add(lblRenewalAcctNo_Sanction_Disp, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panSanctionDetails_Table1.add(panRenewalSanctionDetails_Sanction, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panBorrowRenewalCompanyDetails.add(panSanctionDetails_Table1, gridBagConstraints);

        tabLimitAmount.addTab("Renewal/Sanction Details", panBorrowRenewalCompanyDetails);

        tabTransView.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tabTransView.setPreferredScrollableViewportSize(new java.awt.Dimension(850, 400));
        jScrollPane1.setViewportView(tabTransView);

        panTranDetView.add(jScrollPane1);

        tabLimitAmount.addTab("Transaction Details", panTranDetView);

        srpPhotoLoad.setPreferredSize(new java.awt.Dimension(120, 150));

        lblPhoto.setName("lblPhoto"); // NOI18N
        lblPhoto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblPhotoMouseClicked(evt);
            }
        });
        srpPhotoLoad.setViewportView(lblPhoto);

        btnLoad.setText("Load");
        btnLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadActionPerformed(evt);
            }
        });

        btnPhotoRemove.setText("Remove");
        btnPhotoRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPhotoRemoveActionPerformed(evt);
            }
        });

        srpRenewPhotoLoad.setPreferredSize(new java.awt.Dimension(120, 150));

        lblRenewPhoto.setName("lblRenewPhoto"); // NOI18N
        lblRenewPhoto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblRenewPhotoMouseClicked(evt);
            }
        });
        srpRenewPhotoLoad.setViewportView(lblRenewPhoto);

        btnAddPhoto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_RIGHTARR.jpg"))); // NOI18N
        btnAddPhoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddPhotoActionPerformed(evt);
            }
        });

        btnRenewLoad.setText("Load");
        btnRenewLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRenewLoadActionPerformed(evt);
            }
        });

        btnRenewPhotoRemove.setText("Remove");
        btnRenewPhotoRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRenewPhotoRemoveActionPerformed(evt);
            }
        });

        panWebCamStockPhoto.setBorder(javax.swing.BorderFactory.createTitledBorder("Web Cam"));

        btnScanStockPhoto.setText("Scan");
        btnScanStockPhoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnScanStockPhotoActionPerformed(evt);
            }
        });

        btnStockPhotoCapture.setText("Capture");
        btnStockPhotoCapture.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStockPhotoCaptureActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panWebCamStockPhotoLayout = new javax.swing.GroupLayout(panWebCamStockPhoto);
        panWebCamStockPhoto.setLayout(panWebCamStockPhotoLayout);
        panWebCamStockPhotoLayout.setHorizontalGroup(
            panWebCamStockPhotoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panWebCamStockPhotoLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(btnScanStockPhoto, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnStockPhotoCapture, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );
        panWebCamStockPhotoLayout.setVerticalGroup(
            panWebCamStockPhotoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panWebCamStockPhotoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panWebCamStockPhotoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnScanStockPhoto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnStockPhotoCapture, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        panWebCamRenewalStockPhoto.setBorder(javax.swing.BorderFactory.createTitledBorder("Web Cam"));

        btnScanRenewalStockPhoto.setText("Scan");
        btnScanRenewalStockPhoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnScanRenewalStockPhotoActionPerformed(evt);
            }
        });

        btnRenewalStockPhotoCapture.setText("Capture");
        btnRenewalStockPhotoCapture.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRenewalStockPhotoCaptureActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panWebCamRenewalStockPhotoLayout = new javax.swing.GroupLayout(panWebCamRenewalStockPhoto);
        panWebCamRenewalStockPhoto.setLayout(panWebCamRenewalStockPhotoLayout);
        panWebCamRenewalStockPhotoLayout.setHorizontalGroup(
            panWebCamRenewalStockPhotoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panWebCamRenewalStockPhotoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnScanRenewalStockPhoto, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnRenewalStockPhotoCapture, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
        );
        panWebCamRenewalStockPhotoLayout.setVerticalGroup(
            panWebCamRenewalStockPhotoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panWebCamRenewalStockPhotoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panWebCamRenewalStockPhotoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnScanRenewalStockPhoto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRenewalStockPhotoCapture, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panStockDetailsLayout = new javax.swing.GroupLayout(panStockDetails);
        panStockDetails.setLayout(panStockDetailsLayout);
        panStockDetailsLayout.setHorizontalGroup(
            panStockDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panStockDetailsLayout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addGroup(panStockDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panStockDetailsLayout.createSequentialGroup()
                        .addComponent(srpPhotoLoad, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(panStockDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panStockDetailsLayout.createSequentialGroup()
                                .addComponent(btnPhotoRemove, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(25, 25, 25)
                                .addComponent(btnAddPhoto, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnLoad, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(panWebCamStockPhoto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panStockDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(srpRenewPhotoLoad, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                    .addComponent(panWebCamRenewalStockPhoto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panStockDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnRenewPhotoRemove, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRenewLoad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        panStockDetailsLayout.setVerticalGroup(
            panStockDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panStockDetailsLayout.createSequentialGroup()
                .addGroup(panStockDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panStockDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(panStockDetailsLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(srpRenewPhotoLoad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panStockDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panStockDetailsLayout.createSequentialGroup()
                                .addGap(166, 166, 166)
                                .addComponent(srpPhotoLoad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panStockDetailsLayout.createSequentialGroup()
                                .addGap(203, 203, 203)
                                .addComponent(btnLoad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnPhotoRemove, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(panStockDetailsLayout.createSequentialGroup()
                        .addGap(198, 198, 198)
                        .addComponent(btnRenewLoad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAddPhoto, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panStockDetailsLayout.createSequentialGroup()
                        .addGap(236, 236, 236)
                        .addComponent(btnRenewPhotoRemove, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(60, 60, 60)
                .addGroup(panStockDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panWebCamStockPhoto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panWebCamRenewalStockPhoto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(171, Short.MAX_VALUE))
        );

        tabLimitAmount.addTab("Stock Details", panStockDetails);

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

    private void btnSecurityAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSecurityAddActionPerformed
        // TODO add your handling code here:
        
     //   HashMap valueLst=(HashMap) observable.getGoldItemMap().get("VALUE");
        //StringBuffer buffer = new StringBuffer();
        String particulars = CommonUtil.convertObjToStr(txtAreaParticular.getText());
        // buffer.append(particulars);
        HashMap selItemList = new HashMap();
        if (particulars.length() != 0) {
            //    buffer.append("\n");
            String[] strarr = particulars.split("\n");
            String regex = "(\\d+)";
            Pattern p = Pattern.compile(regex);
            for (String test1 : strarr) {
                //System.out.println(test1);
                 String[] strarr1 = test1.split(",");
               // test=test.replaceAll("-", "");
//                  Matcher m1 = p.matcher(tempStr);
//                 if (m1.find()) {
//                  String slno=m1.group();
//                  test=tempStr.substring(slno.length(),tempStr.length());
//                 }
                for (String test : strarr1) {
                    Matcher m = p.matcher(test);
                    if (m.find()) {
                        String qtyy = m.group();
                        String key = test.substring(0, test.indexOf(qtyy)).trim();
                        key = key.toUpperCase();
                        int numberPosition = m.start();
                        int end = m.end();
                        String rem1 = test.substring(end, test.length()).trim();
                        char numberChar = test.charAt(numberPosition);
                        selItemList.put(key.replaceAll("-", ""), qtyy.replaceAll("-", "") + "#" + rem1.replaceAll("-", ""));
                    }
                }
            }

        }
        GoldLoanItemView surtyTab = new GoldLoanItemView("GOLDLOAN", observable.getGoldItemMap(),selItemList);
        surtyTab.show();
        if (surtyTab.getSelDataBuff() != null && surtyTab.getSelDataBuff().length() > 0) {
        txtAreaParticular.setText(CommonUtil.convertObjToStr(surtyTab.getSelDataBuff()));
        }
        
//        String jewalItem = CommonUtil.convertObjToStr(cboItem.getSelectedItem());
//        if (jewalItem.length() > 0) {
//            String qty = CommonUtil.convertObjToStr(txtQty.getText());
//            String remarks = CommonUtil.convertObjToStr(txtSecurityRemarks.getText());
//            if (qty.length() > 0) {
//                buffer.append(jewalItem);
//                buffer.append(" ");
//                buffer.append(qty);
//                buffer.append(" ");
//                buffer.append(remarks);
//                txtAreaParticular.setText(buffer.toString());
//                cboItem.setSelectedItem("");
//                txtSecurityRemarks.setText("");
//                txtQty.setText("");
//            } else {
//                ClientUtil.showMessageWindow("Please Enter Quantity then add ");
//                return;
//            }
//        } else {
//            ClientUtil.showMessageWindow("Please Select Item then add ");
//            return;
//        }
    }//GEN-LAST:event_btnSecurityAddActionPerformed

    private void tdtRenewalAccountOpenDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtRenewalAccountOpenDateFocusLost
        // TODO add your handling code here:
        String renewalDate = CommonUtil.convertObjToStr(tdtRenewalAccountOpenDate.getDateValue());
        if (renewalDate.length() > 0) {
            if (DateUtil.dateDiff(curr_dt, DateUtil.getDateMMDDYYYY(renewalDate)) > 0) {
                ClientUtil.displayAlert("Please Choose To Day Date as Account Open Date");
                tdtRenewalAccountOpenDate.setDateValue(DateUtil.getStringDate(curr_dt));
            }
        }
    }//GEN-LAST:event_tdtRenewalAccountOpenDateFocusLost

    private void cboRenewalAppraiserIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboRenewalAppraiserIdFocusLost
        // TODO add your handling code here:
        if (cboRenewalAppraiserId.getSelectedIndex() > 0 && observable.getCbmRenewalAppraiserId() != null) {
            HashMap hashMap = new HashMap();
//            observableSecurity.setTxtAppraiserId(CommonUtil.convertObjToStr(cboAppraiserId.getSelectedItem()));
            observableSecurity.setTxtAppraiserId(CommonUtil.convertObjToStr(cboRenewalAppraiserId.getSelectedItem()));
            hashMap.put("EMPLOYEE_CODE", CommonUtil.convertObjToStr(observable.getCbmRenewalAppraiserId().getKeyForSelected()));
            List lst = ClientUtil.executeQuery("getAppraiserName", hashMap);
            if (lst != null && lst.size() > 0) {
                hashMap = new HashMap();
                hashMap = (HashMap) lst.get(0);
                String apprName = (String) hashMap.get("EMPLOYEE_NAME");
                lblRenewalAppraiserNameValue.setText(apprName);
            } else {
                lblRenewalAppraiserNameValue.setText("");
            }
        } else {
            lblRenewalAppraiserNameValue.setText("");
//            observableSecurity.setTxtAppraiserId("");
            observableSecurity.setTxtRenewalAppraiserId("");
        }


    }//GEN-LAST:event_cboRenewalAppraiserIdFocusLost

    private void cboRenewalRepayFreqFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboRenewalRepayFreqFocusLost
        // TODO add your handling code here:
        calculateSanctionToDate();
        calculateSanctionToDateAndDepositoryForRenewal();
    }//GEN-LAST:event_cboRenewalRepayFreqFocusLost

    private void txtRenewalNoInstallmentsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRenewalNoInstallmentsFocusLost
        // TODO add your handling code here:
        calculateSanctionToDate();
        calculateSanctionToDateAndDepositoryForRenewal();
    }//GEN-LAST:event_txtRenewalNoInstallmentsFocusLost

    private void txtRenewalLimit_SDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRenewalLimit_SDFocusLost
        // TODO add your handling code here:
        checkLimitChange = !checkLimitChange;
        //  double outstandingAmt = transDetailsUI.calculatetotalRecivableAmountForRenewalLoan()+CommonUtil.convertObjToDouble(txtCharges.getText());
        //    observable.setTotRecivableAmt(transDetailsUI.calculatetotalRecivableAmountForRenewalLoan());
        ////System.out.println("observable.getTotRecivableAmt("+observable.getTotRecivableAmt());
        //   double renewalAmt = CommonUtil.convertObjToDouble(txtRenewalLimit_SD.getText()).doubleValue();
        //   double eligibleLoanAmt = CommonUtil.convertObjToDouble(txtRenewalEligibleLoan.getText()).doubleValue();
        //  if (renewalAmt > 0) {
        //     if (eligibleLoanAmt < renewalAmt) {
        // ClientUtil.showMessageWindow("Limit Amount Should not Exceed the Max Loan Eligibility Amount");//bbauuu
        //  double charge=CommonUtil.convertObjToDouble(txtCharges.getText());
        //  txtRenewalLimit_SD.setText(String.valueOf(transDetailsUI.calculatetotalRecivableAmountForRenewalLoan()));//+charge));
//                txtRenewalLimit_SD.setText("");
        //     txtRenewalLimit_SD.setFocusable(true);
        //     return;
        // } else if (renewalAmt < observable.getTotRecivableAmt()) {
        //     if (renewalparamMap.containsKey("RENEW_WITH_OLDAMT") && renewalparamMap.get("RENEW_WITH_OLDAMT").toString().equals("N")) {
        //     ClientUtil.showMessageWindow("Renewal Amount Should not be Less than Rs :" + outstandingAmt);
        //      txtRenewalLimit_SD.setText(String.valueOf(transDetailsUI.calculatetotalRecivableAmountForRenewalLoan()));
        //      txtRenewalLimit_SD.setFocusable(true);
        //      return;
        //     }
        //   }
        //    calculateSanctionToDate();
        //  }
//double totCharge=CommonUtil.convertObjToDouble(txtCharges.getText());
//observable.setTotRecivableAmt(observable.getTotRecivableAmt()+totCharge);

        String oldValue = txtRenewalLimit_SD.getText();
        System.out.println("old vallue :: " + txtRenewalLimit_SD.getText());
        editChargeAmount(viewType);
        txtRenewalLimit_SD.setText(oldValue);
        calculateTot();
        System.out.println("new vallue :: " + txtRenewalLimit_SD.getText());
        if(evt != null)
           getInterestRateOnRenewalLimitChange();


    }//GEN-LAST:event_txtRenewalLimit_SDFocusLost
    private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) curr_dt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }
    private boolean chkAuthorizationPending() {
        boolean chkFlag = true;
        String actNum = CommonUtil.convertObjToStr(lblAcctNo_Sanction_Disp.getText());
        if (actNum.length() > 0) {
            HashMap transCashMap = new HashMap();
            transCashMap.put("BATCH_ID", actNum);
            transCashMap.put("TRANS_DT", curr_dt);
            transCashMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            transCashMap.put("AUTHORIZE_STATUS", "AUTHORIZE_STATUS");
            //Commented and added new map By Kannan AR bcz IBT also should consider
            //List transferList = ClientUtil.executeQuery("getTransferDetails", transCashMap);
            List transferList = ClientUtil.executeQuery("getPendingTransferDetails", transCashMap);            
            if (transferList.size() > 0) {
                ClientUtil.displayAlert("Transaction is pending for authorization!!");
                return false;
            }
            //Commented and added new map By Kannan AR bcz IBT also should consider
            //List cashList = ClientUtil.executeQuery("getCashDetails", transCashMap);
            List cashList = ClientUtil.executeQuery("getPendingCashDetails", transCashMap);
            if (cashList.size() > 0) {
                ClientUtil.displayAlert("Transaction is pending for authorization!!");
                return false;
            }
        }
        return chkFlag;
    }
    private void btnRenewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRenewActionPerformed
        // TODO add your handling code here:
        HashMap chckMap = new HashMap();
        chckMap.put("ACCT_NUM", lblAcctNo_Sanction_Disp.getText());
        chckMap.put("CURR_DT", curr_dt);
        List unAuthList = ClientUtil.executeQuery("getNoOfUnauthorizedTransaction", chckMap);
        if (unAuthList != null && unAuthList.size() > 0) {// Added by nithya on 11-12-2018 for KD 327
            ClientUtil.showMessageWindow(" There is Pending Transaction please Authorize OR Reject first ");
            chckMap = null;           
            return;
        }
        if (CommonUtil.convertObjToStr(cboAccStatus.getSelectedItem()).equals("Closed")) {
            ClientUtil.showMessageWindow("Account closed");
            return;
        }
        String acctOpenDate = CommonUtil.convertObjToStr(tdtAccountOpenDate.getDateValue());
        if (acctOpenDate.length() > 0 && DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(tdtAccountOpenDate.getDateValue()), curr_dt) == 0) {
            ClientUtil.showMessageWindow("Cannot Renew Again on the Same Day");
            return;
        }
        if(!chkAuthorizationPending()){
            return;
        }
        
        //Suspicious Account Check
        String suspiciousAccountWarning = SuspiciousAccountValidation.checkForSuspiciousActivity(lblAcctNo_Sanction_Disp.getText(),"CREDIT");
        if(suspiciousAccountWarning.length() > 0){
            ClientUtil.showAlertWindow(suspiciousAccountWarning);
            btnCancelActionPerformed(null);
            return;
        }
        // End
        
        int confirm = ClientUtil.confirmationAlert("Do you Want to Renew this Account");
        if (confirm == 0) {
            eachProdId = ((ComboBoxModel) cboGoldLoanProd.getModel()).getKeyForSelected().toString();
            renewalparamMap.put("PROD_ID", eachProdId);
            List paramDetail = ClientUtil.executeQuery("getgoldRenewalDetails", renewalparamMap);
            renewalparamMap = (HashMap) paramDetail.get(0);
            observable.setRenewalParamMap(renewalparamMap);
            tabLimitAmount.add(panBorrowRenewalCompanyDetails, "Renewal/Sanction Details");
            transDetailsUI.setSourceFrame(this);
            if (observable.getRenewalAcctNum().length() == 0) {
                copyUIComponentsforRenewal();
                if(cboRnwGoldLoanProd.getSelectedIndex() > 0){ //Added by nithya on 17-09-2021 for KD-3037
                    cboRnwGoldLoanProdAction();
                    txtRenewalNetWeightFocusLost(null);
                }
                cboPurityOfGoldActionPerformed(null);
                //  calcAfterRenewalEligibleLoanAmount();//bb
                interestCalculationTLAD(CommonUtil.convertObjToStr(lblAcctNo_Sanction_Disp.getText()));
                //transDetailsUI.setSourceScreen("LOAN_ACT_CLOSING");
                //transDetailsUI.setSourceFrame(this);
                transDetailsUI.setTransDetails("TL", ProxyParameters.BRANCH_ID, CommonUtil.convertObjToStr(lblAcctNo_Sanction_Disp.getText()));
                transDetailsUI.setSourceFrame(this);
                txtRenewalLimit_SD.setText(String.valueOf(transDetailsUI.calculatetotalRecivableAmountForRenewalLoan()));
                ClientUtil.enableDisable(panRenewalSanctionDetails_Sanction, true);
                ClientUtil.enableDisable(panTableFields_SD1, true);
                cboRenewalAppraiserId.setEnabled(true);
                observable.setRenewalYesNo(true);
                checkLimitChange = true;
                txtRenewalLimit_SDFocusLost(null);
                checkLimitChangeValue = txtRenewalLimit_SD.getText();
                calculateSanctionToDateAndDepositoryForRenewal();
                tabLimitAmount.setSelectedComponent(panBorrowRenewalCompanyDetails);
            }
            double outstandingAmt = transDetailsUI.calculatetotalRecivableAmountForRenewalLoan();
            observable.setTotRecivableAmt(transDetailsUI.calculatetotalRecivableAmountForRenewalLoan());
            double renewalAmt = CommonUtil.convertObjToDouble(txtRenewalLimit_SD.getText()).doubleValue();
            double eligibleLoanAmt = CommonUtil.convertObjToDouble(txtRenewalEligibleLoan.getText()).doubleValue();
            if (renewalAmt > 0) {
//            if(eligibleLoanAmt<renewalAmt){
//                ClientUtil.showMessageWindow("Limit Amount Should not Exceed the Max Loan Eligibility Amount");
//                txtRenewalLimit_SD.setText(String.valueOf(transDetailsUI.calculatetotalRecivableAmountForRenewalLoan()));
////                txtRenewalLimit_SD.setText("");
//                txtRenewalLimit_SD.setFocusable(true);
//                return;
//            }else if(renewalAmt<observable.getTotRecivableAmt()){
//                if(renewalparamMap.containsKey("RENEW_WITH_OLDAMT") && renewalparamMap.get("RENEW_WITH_OLDAMT").toString().equals("N")){
//                ClientUtil.showMessageWindow("Renewal Amount Should not be Less than Rs :"+outstandingAmt);
//                txtRenewalLimit_SD.setText(String.valueOf(transDetailsUI.calculatetotalRecivableAmountForRenewalLoan()));
//                txtRenewalLimit_SD.setFocusable(true);
//                return;
//                }
//            }
                calculateSanctionToDate();
                HashMap rnwIntRate = new HashMap();
                rnwIntRate.put("PROD_ID", eachProdId);
                String oldOrNw = "";
                List rnwWithInt = ClientUtil.executeQuery("getRnwWithOrWithoutInt", rnwIntRate);
                if (rnwWithInt != null && rnwWithInt.size() > 0) {
                    for (int i = 0; i < rnwWithInt.size(); i++) {

                        rnwIntRate = new HashMap();
                        rnwIntRate = (HashMap) rnwWithInt.get(0);
                        //if(rnwIntRate.containsKey("RENEW_WITH_NEW_INTRATE")rnwIntRate.get("RENEW_WITH_NEW_INTRATE")!=null)
                        oldOrNw = rnwIntRate.get("RENEW_WITH_NEW_INTRATE").toString();
                    }
                }
                HashMap whereMap = new HashMap();
                whereMap.put("CATEGORY_ID", observableBorrow.getCbmCategory().getKeyForSelected());
                whereMap.put("AMOUNT", getBigDecimal(CommonUtil.convertObjToDouble(txtRenewalLimit_SD.getText()).doubleValue()));
                whereMap.put("PROD_ID", eachProdId);
                //        if (tdtAccountOpenDate.getDateValue().length()==0) tdtFDate.setDateValue(observable.getTdtFDate());
                if (oldOrNw.equals("Y")) {
                    whereMap.put("FROM_DATE",setProperDtFormat(curr_dt));
                } else if (oldOrNw.equals("N")) {
                    whereMap.put("FROM_DATE", setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtRenewalAccountOpenDate.getDateValue())));
                }
                //        if (tdtTDate.getDateValue().length()==0) tdtTDate.setDateValue(observable.getTdtTDate());
                whereMap.put("TO_DATE", setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtRenewalDemandPromNoteExpDate.getDateValue())));
                //        deleteAllInterestDetails();
                observableInt.resetInterestDetails();

                updateInterestDetails();
                // Populate the values
                //                String interestType=CommonUtil.convertObjToStr(((ComboBoxModel)cboInterestType).getKeyForSelected().toString());
                //        String interestType=(String)(((ComboBoxModel)cboInterestType.getModel()).getKeyForSelected()).toString();
                ArrayList interestList = null;
                //        if(interestType !=null && interestType.equals("FLOATING_RATE"))
                //            interestList = (java.util.ArrayList)ClientUtil.executeQuery("getSelectProductTermLoanInterestFloatTO", whereMap);
                //        else
                ArrayList interestType = (ArrayList) ClientUtil.executeQuery("getSelectProductTermLoanInterestTO", whereMap);
                //        List list = (List) sqlMap.executeQueryForList("getSelectTermLoanJointAcctTO", where);
                //        returnMap.put("TermLoanJointAcctTO", list);
//            appraiserCommisionAmt();

                if (interestType != null && interestType.size() > 0) {
                    TermLoanInterestTO termLoanInterestTO = (TermLoanInterestTO) interestType.get(0);
                    txtRenewalInter.setText(CommonUtil.convertObjToStr(termLoanInterestTO.getInterest()));
                    observable.setTxtRenewalInter(txtRenewalInter.getText());
                    txtRenewalPenalInter.setText(CommonUtil.convertObjToStr(termLoanInterestTO.getPenalInterest()));
                    observable.setTxtRenewalPenalInter(txtRenewalPenalInter.getText());
                    txtRenewalInter.setEnabled(false);
                    txtRenewalPenalInter.setEnabled(false);
                } else {
                    ClientUtil.showAlertWindow("Rate of interest is not set for this product");
                    return;
                }
            }
            rnw = 1;
            //Added By Suresh
            txtRenewalAreaParticular.setEnabled(false);
            srpTxtAreaParticulars1.setEnabled(false);
        } else {
            tabLimitAmount.remove(panBorrowRenewalCompanyDetails);
        }
//        //Commented By Suresh
//        txtRenewalAreaParticular.setEditable(true);
//        txtRenewalAreaParticular.setEnabled(true);    
        panRenewalSecurityDetails_security.setEnabled(true);
        ClientUtil.enableDisable(panRenewalSecurityDetails_security, true);
        ClientUtil.enableDisable(panSecurityType1, true);
        ClientUtil.enableDisable(panSecurityParticulars1, true);
        List chargeList = null;
        HashMap whereMap1 = new HashMap();
        whereMap1.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        String loanProdId = ((ComboBoxModel) cboGoldLoanProd.getModel()).getKeyForSelected().toString();
        whereMap1.put("PRODUCT_ID", loanProdId);
        chargeList = (List) (ClientUtil.executeQuery("getSelectNextAccNo", whereMap1));
        if (chargeList != null && chargeList.size() > 0) {
            renewalAccNo = CommonUtil.convertObjToStr((chargeList.get(0)));
        }
        chargeList = null;//bb1
        cboRenewalPurityOfGold.setSelectedItem("22CT");
        cboRenewalPurityOfGoldActionPerformed(null);
        lblAppraiserNameValue.setText("");
      //  cboAppraiserId.setSelectedItem(CommonUtil.convertObjToStr(com.see.truetransact.ui.TrueTransactMain.USERINFO.get("EMPLOYEE_ID")));
        getAppraiserDefaultName();
      //  cboRenewalAppraiserId.setSelectedItem(CommonUtil.convertObjToStr(com.see.truetransact.ui.TrueTransactMain.USERINFO.get("EMPLOYEE_ID")));
        getRenewalAppraiserDefaultName("");
        cboRenewalAppraiserIdFocusLost(null);
        double serviceTax = 0.0;
        if(CommonUtil.convertObjToStr(lblServiceTaxVal.getText()).length() > 0){
           serviceTax = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(lblServiceTaxVal.getText()));
        }
        double totCharge = CommonUtil.convertObjToDouble(txtCharges.getText());
        totCharge = (double) higher((long) (totCharge * 100), 100) / 100;
        double val = CommonUtil.convertObjToDouble(txtRenewalLimit_SD.getText());
        val = val + totCharge + serviceTax;
        txtRenewalLimit_SD.setText(String.valueOf(val));
        // getTotalRenewalAmt();
        observable.setTotRecivableAmt(observable.getTotRecivableAmt());// + totCharge);comm on 09-05-2014 babu
        displayNextAccNo();
        txtCharges.setEnabled(false);
        txtCharges.setEditable(false);
        txtRenewalMarginAmt.setEditable(false);
        txtRenewalMarginAmt.setEnabled(false);
        txtRenewalEligibleLoan.setEditable(false);
        //added by rishad 
        HashMap paramMap = new HashMap();
        paramMap.put("ACCT_NUM", lblAcctNo_Sanction_Disp.getText());
        boolean lock = whenTableRowSelected(paramMap);
        if (lock == true) {
            ClientUtil.enableDisable(panBorrowRenewalCompanyDetails, false);
           return;
        }
        setAppraiserName();
        editChargeAmount(viewType); //KD-3774
        
        String integrateWebCam = CommonUtil.convertObjToStr(TrueTransactMain.CBMSPARAMETERS.get("WEBCAM_INTEGRATION"));
        System.out.println("integrateWebCam :: " + integrateWebCam);
        if (integrateWebCam.equals("Y")) {
            panWebCamStockPhoto.setVisible(false);
            panWebCamRenewalStockPhoto.setVisible(true);
            btnScanRenewalStockPhoto.setEnabled(true);
            btnRenewalStockPhotoCapture.setEnabled(true);
        } else {
            panWebCamStockPhoto.setVisible(false);
            panWebCamRenewalStockPhoto.setVisible(false);
        }
        
         
    }//GEN-LAST:event_btnRenewActionPerformed
    public void modifyTransData(Object objTextUI) {
        //         TextUI objTextUI
        double totRecivable = transDetailsUI.calculatetotalRecivableAmountFromAccountClosing();
        //  //System.out.println("totRecivable" + totRecivable + "       waived penal    " + CommonUtil.convertObjToDouble(transDetailsUI.getTermLoanCloseCharge().get("PENAL_INT")).doubleValue());

        /*  if (observable.getWaiveEditPenalTransAmt() > 0.0) {
        totRecivable -= CommonUtil.convertObjToDouble(transDetailsUI.getTermLoanCloseCharge().get("PENAL_INT")).doubleValue();
        }
        if (observable.isWaiveOffInterest()) {
        totRecivable -= CommonUtil.convertObjToDouble(transDetailsUI.getTermLoanCloseCharge().get("CURR_MONTH_INT")).doubleValue();
        }*/
        //observable.get
       /* if (CommonConstants.OPERATE_MODE.equals(CommonConstants.IMPLEMENTATION) && prodType.equals("TermLoan")) {
        this.txtInterestPayable.setText(String.valueOf(totRecivable));
        observable.setTxtInterestPayable(String.valueOf(totRecivable));
        observable.setLoanInt(String.valueOf(totRecivable));
        }*/
        getTotalRenewalAmt();
        //   calcAndDisplayAvailableBalance();
        totRecivable = 0;
    }
    //added by rishad 24/07/2015
    public void removeEditLockScreen(String recordKey) {
        if (recordKey.length() > 0) {
            HashMap map = new HashMap();
            map.put("SCREEN_ID", getScreenID());
            map.put("RECORD_KEY", recordKey);
            map.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
            map.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            map.put("CUR_DATE", curr_dt.clone());
            ClientUtil.execute("deleteEditLock", map);
        }
        setMode(ClientConstants.ACTIONTYPE_CANCEL);
    }
    private void copyUIComponentsforRenewal() {

        lblRenewalAcctNo_Sanction_Disp.setText(observable.getRenewalAcctNum());
        tdtRenewalSanctionDate.setDateValue(DateUtil.getStringDate(curr_dt));//tdtSanctionDate.getDateValue());
        cboRenewalSanctioningAuthority.setSelectedItem(CommonUtil.convertObjToStr(cboSanctioningAuthority.getSelectedItem()));
        rdoRenewalPriority.setSelected(rdoPriority.isSelected());
        rdoRenewalNonPriority.setSelected(rdoNonPriority.isSelected());
        cboRenewalPurposeOfLoan.setSelectedItem(cboPurposeOfLoan.getSelectedItem());
        txtRenewalSanctionRemarks.setText(txtSanctionRemarks.getText());
        //  double charge=CommonUtil.convertObjToDouble(txtCharges.getText());
        txtRenewalLimit_SD.setText(String.valueOf(transDetailsUI.calculatetotalRecivableAmountForRenewalLoan()));//+charge));
        tdtRenewalAccountOpenDate.setDateValue(DateUtil.getStringDate(curr_dt));//tdtAccountOpenDate.getDateValue()
        cboRenewalAccStatus.setSelectedItem(cboAccStatus.getSelectedItem());
        txtRenewalNoInstallments.setText(txtNoInstallments.getText());
        cboRenewalRepayFreq.setSelectedItem(cboRepayFreq.getSelectedItem());
        observable.setCboRenewalRepayFreq(CommonUtil.convertObjToStr(cboRepayFreq.getSelectedItem()));
        tdtRenewalDemandPromNoteDate.setDateValue(DateUtil.getStringDate(curr_dt));
        tdtRenewalDemandPromNoteExpDate.setDateValue(tdtDemandPromNoteExpDate.getDateValue());
        txtRenewalInter.setText(txtInter.getText());
        txtRenewalPenalInter.setText(txtPenalInter.getText());
        cboRnwGoldLoanProd.setSelectedItem(cboGoldLoanProd.getSelectedItem());
       	double GrossWeight = CommonUtil.convertObjToDouble(txtGrossWeight.getText());
        //GrossWeight=(double) getNearest((long) (GrossWeight * 100), 100) / 100;13-02-2014
        txtRenewalGrossWeight.setText(String.valueOf(GrossWeight));

        double NetWeight = CommonUtil.convertObjToDouble(txtNetWeight.getText());
        //  NetWeight=(double) getNearest((long) (NetWeight * 100), 100) / 100;13-02-2014
        txtRenewalNetWeight.setText(String.valueOf(NetWeight));
        cboRenewalPurityOfGold.setSelectedItem(CommonUtil.convertObjToStr(cboPurityOfGold.getSelectedItem()));
        ((ComboBoxModel) cboRenewalPurityOfGold.getModel()).setSelectedItem(CommonUtil.convertObjToStr(cboPurityOfGold.getSelectedItem()));
        observableSecurity.setCboRenewalPurityOfGold(CommonUtil.convertObjToStr(cboPurityOfGold.getSelectedItem()));

        double MarketRate = CommonUtil.convertObjToDouble(txtMarketRate.getText());
        MarketRate = (double) getNearest((long) (MarketRate * 100), 100) / 100;
        txtRenewalMarketRate.setText(String.valueOf(MarketRate));

        double RenewalSecurityValue = CommonUtil.convertObjToDouble(txtSecurityValue.getText());
        RenewalSecurityValue = (double) getNearest((long) (RenewalSecurityValue * 100), 100) / 100;
        txtRenewalSecurityValue.setText(String.valueOf(RenewalSecurityValue));
        txtRenewalAreaParticular.setText(txtAreaParticular.getText());

        double Margin = CommonUtil.convertObjToDouble(txtMargin.getText());
        Margin = (double) getNearest((long) (Margin * 100), 100) / 100;
        txtRenewalMargin.setText(String.valueOf(Margin));
        /***********13-02-2014*************/
        String prodId = ((ComboBoxModel) cboGoldLoanProd.getModel()).getKeyForSelected().toString();
        HashMap singleAuthorizeMapOpBal = new HashMap();
        singleAuthorizeMapOpBal.put("PROD_ID", prodId);
        singleAuthorizeMapOpBal.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        List aListOp = ClientUtil.executeQuery("getFixedOrMarket", singleAuthorizeMapOpBal);
        String isFixedOrMarket = "N";
        double fixedRate = 0;
        if (aListOp.size() > 0 && aListOp.get(0) != null) {
            HashMap mapop = (HashMap) aListOp.get(0);
            isFixedOrMarket = CommonUtil.convertObjToStr(mapop.get("BY_MARGIN_FIXED"));
            fixedRate = CommonUtil.convertObjToDouble(mapop.get("FIXED_RATE_GOLD"));
        }
        if (isFixedOrMarket != null && isFixedOrMarket.equalsIgnoreCase("Y")) {
            txtRenewalMarginAmt.setText(String.valueOf(fixedRate));
            double eligibleLoan = CommonUtil.convertObjToDouble(txtRenewalMarginAmt.getText()) * NetWeight;
            eligibleLoan = (double) getNearest((long) (eligibleLoan * 100), 100) / 100;
            txtRenewalEligibleLoan.setText(String.valueOf(eligibleLoan));
        } else {
            double MarginAmt = CommonUtil.convertObjToDouble(txtMarginAmt.getText());
            MarginAmt = (double) getNearest((long) (MarginAmt * 100), 100) / 100;
            txtRenewalMarginAmt.setText(String.valueOf(MarginAmt));

            double eligibleLoan = CommonUtil.convertObjToDouble(txtEligibleLoan.getText());
            eligibleLoan = (double) getNearest((long) (eligibleLoan * 100), 100) / 100;
            txtRenewalEligibleLoan.setText(String.valueOf(eligibleLoan));
        }
        cboRenewalAppraiserId.setSelectedItem(cboAppraiserId.getSelectedItem());
        observable.setCharges(CommonUtil.convertObjToDouble(txtCharges.getText()));
        txtRenewCustMobNo.setText(txtCustMobNo.getText());
    }

    public long getNearest(long number, long roundingFactor) {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor % 2) != 0) {
            roundingFactorOdd += 1;
        }
        long mod = number % roundingFactor;
        if ((mod <= (roundingFactor / 2)) || (mod <= (roundingFactorOdd / 2))) {
            return lower(number, roundingFactor);
        } else {
            return higher(number, roundingFactor);
        }
    }

    private HashMap interestCalculationTLAD(String accountNo) {
        HashMap map = new HashMap();
        HashMap hash = null;
        try {
            String prod_id = "";
            map.put("ACT_NUM", accountNo);
            //		if((ComboBoxModel)cboProdId.getModel()!=null)
            //                if((((ComboBoxModel)cboProdId.getModel()).getKeyForSelected().toString())!=null)
            //                    prod_id=((ComboBoxModel)cboProdId.getModel()).getKeyForSelected().toString();
            map.put("PROD_ID", prod_id);
            map.put("TRANS_DT", ClientUtil.getCurrentDate());
            map.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
            map.put("INT_CALC_FROM_SCREEN","INT_CALC_FROM_SCREEN"); // Added by nithya on 08-01-2020 for KD-572 
            String mapNameForCalcInt = "IntCalculationDetail";
//                if (observable.getProdType().equals("AD")) {
//                    mapNameForCalcInt = "IntCalculationDetailAD";
//                }
            List lst = ClientUtil.executeQuery(mapNameForCalcInt, map);
            if (lst != null && lst.size() > 0) {
                hash = (HashMap) lst.get(0);
                if (hash.get("AS_CUSTOMER_COMES") != null && hash.get("AS_CUSTOMER_COMES").equals("N")) {
                    hash = new HashMap();
                    return hash;
                }
                map.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
                map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
//                    if(noOfInstallment>0)
//                        map.put("NO_OF_INSTALLMENT", new Long(noOfInstallment));

                //                    InterestCalculationTask interestcalTask=new InterestCalculationTask(header);
                map.putAll(hash);
                map.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");
                map.put("CURR_DATE", curr_dt);
                map.put("PREMATURE_ONEMONTH_INT", "PREMATURE_ONEMONTH_INT");
                map.put("SOURCE_SCREEN", "LOAN_CLOSING");
                //                    hash =interestcalTask.interestCalcTermLoanAD(map);
                observable.setAsAnWhenCustomer(CommonUtil.convertObjToStr(map.get("AS_CUSTOMER_COMES")));
                hash = observable.loanInterestCalculationAsAndWhen(map);
                if (hash == null) {
                    hash = new HashMap();
                }
                hash.putAll(map);
                hash.put("AS_CUSTOMER_COMES", map.get("AS_CUSTOMER_COMES"));
                transDetailsUI.setAsAndWhenMap(hash);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return hash;
    }
    private void chkMobileBankingTLADActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkMobileBankingTLADActionPerformed
        // TODO add your handling code here:
        if (chkMobileBankingTLAD.isSelected()) {
            if(txtCustID.getText().length()>0){
                long mobileNo = observable.getMobileNo(CommonUtil.convertObjToStr(txtCustID.getText()));
                if(mobileNo != 0){
                    txtMobileNo.setText(CommonUtil.convertObjToStr(mobileNo));
                    tdtMobileSubscribedFrom.setDateValue(CommonUtil.convertObjToStr(curr_dt.clone()));
                }
            }
            EnableDisbleMobileBanking(true);
        } else {
            EnableDisbleMobileBanking(false);
            txtMobileNo.setText("");
            tdtMobileSubscribedFrom.setDateValue("");
        }

    }//GEN-LAST:event_chkMobileBankingTLADActionPerformed
    private void EnableDisbleMobileBanking(boolean flag) {
        txtMobileNo.setEnabled(flag);
        tdtMobileSubscribedFrom.setEnabled(flag);
    }
    private void btnMembershipLiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMembershipLiaActionPerformed
        // TODO add your handling code here:
        if (txtCustID.getText().length() > 0) {
            new MembershipLiabilityUI(CommonUtil.convertObjToStr(txtCustID.getText()), CommonUtil.convertObjToStr(txtAccountNo.getText())).show();
        }
    }//GEN-LAST:event_btnMembershipLiaActionPerformed
    private void setAppraiserName(){
        HashMap hashMap = new HashMap();
        hashMap.put("EMPLOYEE_CODE", CommonUtil.convertObjToStr(observable.getCbmAppraiserId().getKeyForSelected()));
        List lst2 = ClientUtil.executeQuery("getAppraiserName", hashMap);
        if (lst2 != null && lst2.size() > 0) {
            hashMap = new HashMap();
            hashMap = (HashMap) lst2.get(0);
            String apprName = (String) hashMap.get("EMPLOYEE_NAME");
            cboAppraiserId.setSelectedItem(CommonUtil.convertObjToStr(apprName));
            lblAppraiserNameValue.setText(CommonUtil.convertObjToStr(apprName));
        }
    }
    private void cboAppraiserIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboAppraiserIdActionPerformed
        // TODO add your handling code here:
        if (cboAppraiserId.getSelectedIndex() > 0) {
            HashMap hashMap = new HashMap();
            observableSecurity.setTxtAppraiserId(CommonUtil.convertObjToStr(cboAppraiserId.getSelectedItem()));
            hashMap.put("EMPLOYEE_CODE", CommonUtil.convertObjToStr(observable.getCbmAppraiserId().getKeyForSelected()));
            List lst = ClientUtil.executeQuery("getAppraiserName", hashMap);
            if (lst != null && lst.size() > 0) {
                hashMap = new HashMap();
                hashMap = (HashMap) lst.get(0);
                String apprName = (String) hashMap.get("EMPLOYEE_NAME");
                lblAppraiserNameValue.setText(apprName);
            } else {
                lblAppraiserNameValue.setText("");
            }
        } else {
            lblAppraiserNameValue.setText("");
            observableSecurity.setTxtAppraiserId("");
        }
    }//GEN-LAST:event_cboAppraiserIdActionPerformed

    private void rdoNonPriorityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoNonPriorityActionPerformed
        // TODO add your handling code here:
        rdoPriority.setSelected(false);
        rdoNonPriority.setSelected(true);
    }//GEN-LAST:event_rdoNonPriorityActionPerformed

    private void rdoPriorityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPriorityActionPerformed
        // TODO add your handling code here:
        rdoPriority.setSelected(true);
        rdoNonPriority.setSelected(false);
    }//GEN-LAST:event_rdoPriorityActionPerformed

    private void tdtSanctionDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtSanctionDateFocusLost
        // TODO add your handling code here:
        String acct_open_dt = CommonUtil.convertObjToStr(tdtAccountOpenDate.getDateValue());
        String sanction_dt = CommonUtil.convertObjToStr(tdtSanctionDate.getDateValue());
        if (acct_open_dt.length() > 0 && sanction_dt.length() > 0) {
            ClientUtil.validateToDate(tdtAccountOpenDate, sanction_dt, true);
            return;
        }
    }//GEN-LAST:event_tdtSanctionDateFocusLost

    private void txtNetWeightFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNetWeightFocusLost
        // TODO add your handling code here:
        double grossWeight = CommonUtil.convertObjToDouble(txtGrossWeight.getText()).doubleValue();
        double netWeight = CommonUtil.convertObjToDouble(txtNetWeight.getText()).doubleValue();
        if (grossWeight < netWeight) {
            ClientUtil.showAlertWindow("NetWeight should be less than grossweight");
            txtNetWeight.setText("");
            return;
        } else if (cboPurityOfGold.getSelectedItem() !=null && !cboPurityOfGold.getSelectedItem().equals("")) {
            calcEligibleLoanAmount();
//            HashMap purityMap = new HashMap();
//            String purity = CommonUtil.convertObjToStr(cboPurityOfGold.getSelectedItem());
//            purityMap.put("PURITY",purity);
//            purityMap.put("TODAY_DATE",curr_dt);
//            List lst = ClientUtil.executeQuery("getSelectTodaysMarketRate",purityMap);
//            if(lst!=null && lst.size()>0){
//                purityMap = (HashMap)lst.get(0);
//                totSecurityValue = 0;
//                totMarginAmt = 0;
//                totEligibleAmt = 0;
//                double perGramAmt = CommonUtil.convertObjToDouble(purityMap.get("PER_GRAM_RATE")).doubleValue();
//                txtMarketRate.setText(String.valueOf(perGramAmt));
//                totSecurityValue = perGramAmt * CommonUtil.convertObjToDouble(txtNetWeight.getText()).doubleValue();
//                txtSecurityValue.setText(String.valueOf(totSecurityValue));
//                double margin = CommonUtil.convertObjToDouble(txtMargin.getText()).doubleValue();
//                totMarginAmt = ((margin * CommonUtil.convertObjToDouble(txtSecurityValue.getText()).doubleValue())/100);
//                totEligibleAmt = CommonUtil.convertObjToDouble(txtSecurityValue.getText()).doubleValue() - totMarginAmt;
//                setTotalSecValue(String.valueOf(totSecurityValue));
//                setTotalMarginValue(String.valueOf(totMarginAmt));
//                setTotalEligibleValue(String.valueOf(totEligibleAmt));
//            }
        }
    }//GEN-LAST:event_txtNetWeightFocusLost

    private void tblBorrowerTabCTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBorrowerTabCTableMouseClicked
        // TODO add your handling code here:
        //        if(tblBorrowerTabCTable.getSelectedRowCount()>0){
        //            new CustomerDetailsScreenUI().show();
        ////            customerDetailsScreenUI.show();
        ////            com.see.truetransact.ui.customerDetailsScreenUI.show();
        //        }
    }//GEN-LAST:event_tblBorrowerTabCTableMouseClicked

    private void txtMarginAmtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMarginAmtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMarginAmtActionPerformed

    private void txtMarginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMarginActionPerformed
        // TODO add your handling code here:
        double margin = CommonUtil.convertObjToDouble(txtMargin.getText()).doubleValue();
        double eligibleMargin = 100 - margin;
        double eigibleAmt = CommonUtil.convertObjToDouble(txtSecurityValue.getText()).doubleValue() * eligibleMargin / 100;
        //        txtEligibleAmt.setText(String.valueOf(eigibleAmt));
        double existingEligibleAmt = CommonUtil.convertObjToDouble(txtEligibleLoan.getText()).doubleValue();
        txtEligibleLoan.setText(String.valueOf(eigibleAmt + existingEligibleAmt));
        //        double existingSecurityAmt = CommonUtil.convertObjToDouble(txtAvalSecVal.getText()).doubleValue();
        //        txtAvalSecVal.setText(String.valueOf(CommonUtil.convertObjToDouble(txtSecurityValue.getText()).doubleValue() + existingSecurityAmt));
    }//GEN-LAST:event_txtMarginActionPerformed

    private void cboPurityOfGoldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPurityOfGoldActionPerformed
        // TODO add your handling code here:        
        if (cboPurityOfGold.getSelectedItem() !=null && !cboPurityOfGold.getSelectedItem().equals("")) {
            HashMap purityMap = new HashMap();
            String purity = CommonUtil.convertObjToStr(cboPurityOfGold.getSelectedItem());
            purityMap.put("PURITY", purity);
            purityMap.put("TODAY_DATE", curr_dt);
            List lst = ClientUtil.executeQuery("getSelectTodaysMarketRate", purityMap);
            if (lst != null && lst.size() > 0) {
                purityMap = (HashMap) lst.get(0);
                perGramAmt = CommonUtil.convertObjToDouble(purityMap.get("PER_GRAM_RATE")).doubleValue();
                if (cboPurityOfGold.getSelectedItem() !=null && !cboPurityOfGold.getSelectedItem().equals("")) {
                    calcEligibleLoanAmount();
                }
                //                setTotalSecValue(String.valueOf(totSecurityValue));
                //                setTotalMarginValue(String.valueOf(totMarginAmt));
                //                setTotalEligibleValue(String.valueOf(totEligibleAmt));
            }
        }
    }//GEN-LAST:event_cboPurityOfGoldActionPerformed

    private void calcEligibleLoanAmountRenewal() {
        perGramAmt = CommonUtil.convertObjToDouble(txtRenewalMarketRate.getText());
        perGramAmt = (double) getNearest((long) (perGramAmt * 100), 100) / 100;
        txtRenewalMarketRate.setText(CommonUtil.convertObjToStr(perGramAmt));
        totSecurityValue = perGramAmt * CommonUtil.convertObjToDouble(txtRenewalNetWeight.getText()).doubleValue();
        totSecurityValue = (double) getNearest((long) (totSecurityValue * 100), 100) / 100;
        txtRenewalSecurityValue.setText(CommonUtil.convertObjToStr((totSecurityValue)));
        double margin = CommonUtil.convertObjToDouble(txtRenewalMargin.getText()).doubleValue();
        totMarginAmt = ((margin * CommonUtil.convertObjToDouble(txtRenewalSecurityValue.getText()).doubleValue()) / 100);
        //13-02-2014
        String prodId = ((ComboBoxModel) cboRnwGoldLoanProd.getModel()).getKeyForSelected().toString();
        HashMap singleAuthorizeMapOpBal = new HashMap();
        singleAuthorizeMapOpBal.put("PROD_ID", prodId);
        singleAuthorizeMapOpBal.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        List aListOp = ClientUtil.executeQuery("getFixedOrMarket", singleAuthorizeMapOpBal);
        String isFixedOrMarket = "N";
        double fixedRate = 0;
        if (aListOp.size() > 0 && aListOp.get(0) != null) {
            HashMap mapop = (HashMap) aListOp.get(0);
            isFixedOrMarket = CommonUtil.convertObjToStr(mapop.get("BY_MARGIN_FIXED"));
            fixedRate = CommonUtil.convertObjToDouble(mapop.get("FIXED_RATE_GOLD"));
        }
        if (isFixedOrMarket != null && isFixedOrMarket.equalsIgnoreCase("Y")) {
            totMarginAmt = fixedRate;
            totEligibleAmt = totMarginAmt * CommonUtil.convertObjToDouble(txtRenewalNetWeight.getText()).doubleValue();
            totEligibleAmt = (double) getNearest((long) (totEligibleAmt * 100), 100) / 100;
        } else {
            totEligibleAmt = CommonUtil.convertObjToDouble(txtRenewalSecurityValue.getText()).doubleValue() - totMarginAmt;
        }
        totMarginAmt = (double) getNearest((long) (totMarginAmt * 100), 100) / 100;
        totEligibleAmt = (double) getNearest((long) (totEligibleAmt * 100), 100) / 100;
        txtRenewalMarginAmt.setText(CommonUtil.convertObjToStr(totMarginAmt));
        txtRenewalEligibleLoan.setText(CommonUtil.convertObjToStr(totEligibleAmt));
    }

    private void calcEligibleLoanAmount() {
        txtMarketRate.setText(String.valueOf(perGramAmt));
        totSecurityValue = perGramAmt * CommonUtil.convertObjToDouble(txtNetWeight.getText()).doubleValue();
        txtSecurityValue.setText(String.valueOf(totSecurityValue));
        double margin = CommonUtil.convertObjToDouble(txtMargin.getText()).doubleValue();
        totMarginAmt = ((margin * CommonUtil.convertObjToDouble(txtSecurityValue.getText()).doubleValue()) / 100);
        String prodId = ((ComboBoxModel) cboGoldLoanProd.getModel()).getKeyForSelected().toString();
        HashMap singleAuthorizeMapOpBal = new HashMap();
        singleAuthorizeMapOpBal.put("PROD_ID", prodId);
        singleAuthorizeMapOpBal.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        List aListOp = ClientUtil.executeQuery("getFixedOrMarket", singleAuthorizeMapOpBal);
        String isFixedOrMarket = "N";
        double fixedRate = 0;
        if (aListOp.size() > 0 && aListOp.get(0) != null) {
            HashMap mapop = (HashMap) aListOp.get(0);
            isFixedOrMarket = CommonUtil.convertObjToStr(mapop.get("BY_MARGIN_FIXED"));
            fixedRate = CommonUtil.convertObjToDouble(mapop.get("FIXED_RATE_GOLD"));
        }
        if (isFixedOrMarket != null && isFixedOrMarket.equalsIgnoreCase("Y")) {
            totMarginAmt = fixedRate;
            totEligibleAmt = totMarginAmt * CommonUtil.convertObjToDouble(txtNetWeight.getText()).doubleValue();
            totEligibleAmt = (double) getNearest((long) (totEligibleAmt * 100), 100) / 100;
        } else {
            totEligibleAmt = CommonUtil.convertObjToDouble(txtSecurityValue.getText()).doubleValue() - totMarginAmt;
        }
        txtMarginAmt.setText(String.valueOf(totMarginAmt));
        txtEligibleLoan.setText(String.valueOf(totEligibleAmt));
    }

    private void calcRenewalEligibleLoanAmount() {

        perGramAmt = (double) getNearest((long) (perGramAmt * 100), 100) / 100;
        txtRenewalMarketRate.setText(CommonUtil.convertObjToStr(perGramAmt));
        totSecurityValue = perGramAmt * CommonUtil.convertObjToDouble(txtRenewalNetWeight.getText()).doubleValue();
        totSecurityValue = (double) getNearest((long) (totSecurityValue * 100), 100) / 100;
        txtRenewalSecurityValue.setText(CommonUtil.convertObjToStr(totSecurityValue));
        double margin = CommonUtil.convertObjToDouble(txtRenewalMargin.getText()).doubleValue();
        totMarginAmt = ((margin * CommonUtil.convertObjToDouble(txtRenewalSecurityValue.getText()).doubleValue()) / 100);
        String prodId = ((ComboBoxModel) cboRnwGoldLoanProd.getModel()).getKeyForSelected().toString();
        HashMap singleAuthorizeMapOpBal = new HashMap();
        singleAuthorizeMapOpBal.put("PROD_ID", prodId);
        singleAuthorizeMapOpBal.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        List aListOp = ClientUtil.executeQuery("getFixedOrMarket", singleAuthorizeMapOpBal);
        String isFixedOrMarket = "N";
        double fixedRate = 0;
        if (aListOp.size() > 0 && aListOp.get(0) != null) {
            HashMap mapop = (HashMap) aListOp.get(0);
            isFixedOrMarket = CommonUtil.convertObjToStr(mapop.get("BY_MARGIN_FIXED"));
            fixedRate = CommonUtil.convertObjToDouble(mapop.get("FIXED_RATE_GOLD"));
        }
        if (isFixedOrMarket != null && isFixedOrMarket.equalsIgnoreCase("Y")) {
            totMarginAmt = fixedRate;
            totEligibleAmt = totMarginAmt * CommonUtil.convertObjToDouble(txtRenewalNetWeight.getText()).doubleValue();
            
        } else {
            totEligibleAmt = CommonUtil.convertObjToDouble(txtRenewalSecurityValue.getText()).doubleValue() - totMarginAmt;
        }
        totMarginAmt = (double) getNearest((long) (totMarginAmt * 100), 100) / 100;
        totEligibleAmt = (double) getNearest((long) (totEligibleAmt * 100), 100) / 100;
        txtRenewalMarginAmt.setText(CommonUtil.convertObjToStr(totMarginAmt));
        txtRenewalEligibleLoan.setText(CommonUtil.convertObjToStr(totEligibleAmt));
    }

    private void calcAfterRenewalEligibleLoanAmount() {
        double pergrAmt = (double) getNearest((long) (perGramAmt * 100), 100) / 100;
        txtRenewalMarketRate.setText(CommonUtil.convertObjToStr(pergrAmt));
        totSecurityValue = perGramAmt * CommonUtil.convertObjToDouble(txtRenewalNetWeight.getText()).doubleValue();

        totSecurityValue = (double) getNearest((long) (totSecurityValue * 100), 100) / 100;

        txtRenewalSecurityValue.setText(String.valueOf(lower((long) totSecurityValue, (long) 1)));
        double margin = CommonUtil.convertObjToDouble(txtRenewalMargin.getText()).doubleValue();
        double totRenewalMarginAmt = ((margin * CommonUtil.convertObjToDouble(txtRenewalSecurityValue.getText()).doubleValue()) / 100);
        double totRenewalEligibleAmt = CommonUtil.convertObjToDouble(txtRenewalSecurityValue.getText()).doubleValue() - totRenewalMarginAmt;

        totRenewalMarginAmt = (double) getNearest((long) (totRenewalMarginAmt * 100), 100) / 100;
        totRenewalEligibleAmt = (double) getNearest((long) (totRenewalEligibleAmt * 100), 100) / 100;
        txtRenewalMarginAmt.setText(String.valueOf(lower((long) totRenewalMarginAmt, (long) 1)));
        txtRenewalEligibleLoan.setText(String.valueOf(lower((long) totRenewalEligibleAmt, (long) 1)));
    }

    private void txtAccountNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccountNoFocusLost
        // TODO add your handling code here:
        callView("EXISTING_CUSTOMER");
    }//GEN-LAST:event_txtAccountNoFocusLost

    private void rdoExistingCustomer_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoExistingCustomer_NoActionPerformed
        // TODO add your handling code here:
        if (rdoExistingCustomer_No.isSelected() == true) {
            txtAccountNo.setText("");
            txtCustID.setText("");
            lblCustNameValue.setText("");
            nomineeUi.setMainCustomerId(txtCustID.getText());
            //            lblSecurityCustNameValue.setText("");
            lblDocumentCustNameValue.setText("");
            lblAccountNo.setVisible(false);
            txtAccountNo.setVisible(false);
            tblBorrowerTabCTable.revalidate();
            txtCustID.setEnabled(true);
            individualCustUI = new IndividualCustUI();
            com.see.truetransact.ui.TrueTransactMain.showScreen(individualCustUI);
            individualCustUI.loanCreationCustId(this);
            //            if(!customerOB.getCustIdReturing().equals("") && customerOB.getCustIdReturing().length()>0){
            //                String cutId = customerOB.getCustIdReturing();
            //                HashMap custMap = new HashMap();
            //                custMap.put("CUSTOMER ID",cutId);
            //                fillData(custMap);
            //            }
            //            HashMap custMap = new HashMap();
            //            custMap.put("CUSTOMER ID","C030009088");
            //            viewType = "CUSTOMER ID";
            //            fillData(custMap);

        }
    }//GEN-LAST:event_rdoExistingCustomer_NoActionPerformed

    private void rdoExistingCustomer_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoExistingCustomer_YesActionPerformed
        // TODO add your handling code here:
        if (rdoExistingCustomer_Yes.isSelected() == true) {
            lblAccountNo.setVisible(true);
            txtAccountNo.setVisible(true);
            lblAccountNo.setEnabled(true);
            txtAccountNo.setEnabled(true);
            //            txtCustID.setEnabled(false);
            //            btnCustID.setEnabled(false);
        }
    }//GEN-LAST:event_rdoExistingCustomer_YesActionPerformed

    private void tdtAccountOpenDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtAccountOpenDateFocusLost
        // TODO add your handling code here:
        //        String acct_open_dt=CommonUtil.convertObjToStr(tdtAccountOpenDate.getDateValue());
        //        String sanction_dt=CommonUtil.convertObjToStr(tdtSanctionDate.getDateValue());
        //        if(acct_open_dt.length()>0 && sanction_dt.length()>0){
        //            ClientUtil.validateToDate(tdtAccountOpenDate,sanction_dt,true);
        //            return;
        //        }
    }//GEN-LAST:event_tdtAccountOpenDateFocusLost

    private void setTotalMainAdditionalSanction() {
        double totalLimit = 0;
        ArrayList resultList = (ArrayList) observableAdditionalSanctionOB.getTblPeakSanctionTab().getDataArrayList();
        double mainLimit = CommonUtil.convertObjToDouble(txtLimit_SD.getText()).doubleValue();
        if (resultList.size() > 0) {
            for (int i = 0; i < resultList.size(); i++) {
                ArrayList singleList = (ArrayList) resultList.get(i);
                totalLimit += CommonUtil.convertObjToDouble(singleList.get(2)).doubleValue();
            }
        }
    }

    private void txtCustIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCustIDFocusLost
        // TODO add your handling code here:
        txtCustIDActionPerform();
        setNomineeDetails();
        setCustPhoneNUmber();
    }//GEN-LAST:event_txtCustIDFocusLost

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        popUp("Enquirystatus");
        btnCheck();
//        btnAppraiserId.setEnabled(false);

    }//GEN-LAST:event_btnViewActionPerformed

    private void txtCustIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCustIDActionPerformed
        //        txtCustIDActionPerform();
        //        // TODO add your handling code here:
        //        String cust_id=txtCustID.getText();
        //        List lst=null;
        //        HashMap executeMap=new HashMap();
        //
        //        if(cust_id !=null && (!cust_id.equals(""))){
        //
        //            executeMap.put("BRANCH_CODE",getSelectedBranchID());
        //            executeMap.put("CUST_ID",new String(cust_id));
        //            viewType="CUSTOMER ID";
        //        }
        //             if (loanType.equals("LTD")) {
        //                lst=ClientUtil.executeQuery("getSelectCustListForLTD",executeMap);
        //            } else {
        //               lst=ClientUtil.executeQuery("getSelectLoanAccInfoList",executeMap);
        //        }
        //        if(lst !=null && lst.size()>0){
        //            executeMap=(HashMap)lst.get(0);
        //            fillData(executeMap);
        //            lst=null;
        //            executeMap=null;
        //        }else{
        //            ClientUtil.displayAlert("Invalid Customer Number");
        //            txtCustID.setText("");
        //        }
        checkShareOutstanding();
    }

    public void checkShareOutstanding() {
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            String share_no = txtAccountNo.getText();
            String cust_id = txtCustID.getText();
            if (share_no != null && !share_no.equals("") && cust_id != null && !cust_id.equals("")) {
                HashMap singleAuthorizeout = new HashMap();
                singleAuthorizeout.put("SHARE_ACCT_NO", share_no);
                singleAuthorizeout.put("CUST_ID", cust_id);
                List shareOut = ClientUtil.executeQuery("getOutstandingReq", singleAuthorizeout);
                if (shareOut.size() > 0 && shareOut.get(0) != null) {
                    HashMap mapshare = (HashMap) shareOut.get(0);
                    String isReq = CommonUtil.convertObjToStr(mapshare.get("IS_OUTSTANDING_REQUIRED"));
                    if (isReq != null && isReq.equals("Y")) {
                        HashMap singleAuthorizeMapOpBal = new HashMap();
                        singleAuthorizeMapOpBal.put("SHARE_ACCT_NO", share_no);
                        singleAuthorizeMapOpBal.put("CUST_ID", cust_id);
                        List aList = ClientUtil.executeQuery("getShareOutstanding", singleAuthorizeMapOpBal);
                        if (aList.size() > 0 && aList.get(0) != null) {
                            HashMap mapop = (HashMap) aList.get(0);
                            double amt = CommonUtil.convertObjToDouble(mapop.get("OUTSTANDING_AMOUNT"));
                            if (amt <= 0 || amt <= 0.0) {
                                ClientUtil.displayAlert("Share outstanding amount is zero. So, loan cannot be issued!!!");//20-03-2014
                                txtCustID.setText("");
                                txtAccountNo.setText("");
                                observableBorrow.resetBorrowerTabCTable();
                                tblBorrowerTabCTable.setModel(observableBorrow.getTblBorrower());
                                return;
                            }
                        }
                    }
                }
            }
        }
    }//GEN-LAST:event_txtCustIDActionPerformed
    private void txtCustIDActionPerform() {


        // TODO add your handling code here:
        String cust_id = CommonUtil.convertObjToStr(txtCustID.getText());
        List lst = null;
        HashMap executeMap = new HashMap();
        HashMap custMap = new HashMap();
        if (cust_id.length() > 0) {
            if (tblBorrowerTabCTable.getRowCount() > 0) {
                for (int i = 0; i < tblBorrowerTabCTable.getRowCount(); i++) {
                    custMap.put(tblBorrowerTabCTable.getValueAt(i, 1), "");
                }
            }
            if (custMap.containsKey(cust_id)) {
                return;
            }
            executeMap.put("BRANCH_CODE", getSelectedBranchID());
            executeMap.put("CUST_ID", new String(cust_id));
            viewType = "CUSTOMER ID";
            if (loanType.equals("LTD")) {
                lst = ClientUtil.executeQuery("getSelectCustListForLTD", executeMap);
            } else {
                lst = ClientUtil.executeQuery("getSelectLoanAccInfoList", executeMap);
            }
            if (lst != null && lst.size() > 0) {
                executeMap = (HashMap) lst.get(0);
                fillData(executeMap);
                lst = null;
                executeMap = null;
            } else {
                ClientUtil.displayAlert("Invalid Customer Number");
                txtCustID.setText("");
                nomineeUi.setMainCustomerId(txtCustID.getText());
            }
            //Added by sreekrishnan
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                HashMap suspenseMap = new HashMap();
                suspenseMap.put("CUST_ID", cust_id);
                List susList =  ClientUtil.executeQuery("getSuspenseAccountOfCustomer", suspenseMap);
                if (susList != null && susList.size() > 0) {
                    ClientUtil.showMessageWindow("Liability occured for this Customer!");
                }
            }
        }
    }

    private HashMap setCustPhoneNUmber() {
        HashMap custMap = new HashMap();
        String CUST_ID = CommonUtil.convertObjToStr(txtCustID.getText());
        HashMap executeMap = new HashMap();
        if (CUST_ID.length() > 0) {
            executeMap.put("BRANCH_CODE", getSelectedBranchID());
            executeMap.put("CUST_ID", new String(CUST_ID));
            List lst = ClientUtil.executeQuery("getCustomerPhoneNo", executeMap);
            if (lst != null && lst.size() > 0) {
                for (int i = 0; i < lst.size(); i++) {
                    custMap = (HashMap) lst.get(i);
                }
            }
            executeMap.clear();
            executeMap = null;
        }
        return custMap;
    }

    private void phoneNoContailsOrNot() {
        HashMap containsPhoneNo = setCustPhoneNUmber();
        if (containsPhoneNo.isEmpty() || containsPhoneNo == null) {
            insToCustPhone();
        } else {
            upToCustPhone();
        }

    }

    private void setNomineeDetails() {
        try {
            if (txtCustID.getText().length() > 0) {
                nomineeUi.resetTable();
                nomineeUi.resetNomineeData();
                nomineeUi.resetNomineeTab();
                nomineeOB = nomineeUi.getNomineeOB();
                HashMap whereMap = new HashMap();
                ArrayList nomineeList = null;
                whereMap.put("CUST_ID", txtCustID.getText());
                List nomineeLst = (List) ClientUtil.executeQuery("getLoanNomineeDetails", whereMap);
                if (nomineeLst != null && nomineeLst.size() > 0) {
                    nomineeList = (ArrayList) nomineeLst;
                    nomineeOB.setNomimeeList(nomineeList);
                    nomineeOB.setNomineeTabData();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void btnCheck() {
        btnCancel.setEnabled(true);
        btnSave.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnEdit.setEnabled(false);
    }

    private void rdoNo_Executed_DOCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoNo_Executed_DOCActionPerformed
        // TODO add your handling code here:
        rdoNo_Executed_DOCActionPerformed();
    }//GEN-LAST:event_rdoNo_Executed_DOCActionPerformed
    private void rdoNo_Executed_DOCActionPerformed() {
        // TODO add your handling code here:
        if (rdoNo_Executed_DOC.isSelected()) {
            observableDocument.setTdtExecuteDate_DOC("");
            tdtExecuteDate_DOC.setDateValue("");
            tdtExecuteDate_DOC.setEnabled(false);
        }
    }
    private void rdoYes_Executed_DOCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoYes_Executed_DOCActionPerformed
        // TODO add your handling code here:
        rdoYes_Executed_DOCActionPerformed();
    }//GEN-LAST:event_rdoYes_Executed_DOCActionPerformed
    private void rdoYes_Executed_DOCActionPerformed() {
        // TODO add your handling code here:
        if (rdoYes_Executed_DOC.isSelected()) {
            tdtExecuteDate_DOC.setEnabled(true);
        }
    }

    //    private boolean isLoanAmtExceedLimit(){
    //        //        if (loanType.equals("OTHERS"))
    //        if (rdoActive_Repayment.isSelected() && !updateRepayment && CommonUtil.convertObjToDouble(observableRepay.getStrLimitAmt()).doubleValue() < (observableRepay.getActiveLoanAmt() + CommonUtil.convertObjToDouble(txtLaonAmt.getText()).doubleValue())){
    //            observableRepay.repayTabWarning("loanExceedLimitWarning");
    //            txtLaonAmt.setText("");
    //            return false;
    //        }else{
    //            return true;
    //        }
    //        //        else
    //        //            return true;
    //    }
    private void chkNPAChrgADActionPerformed() {
        /* we have to show the NPA charge date only if the corresponding
         * check box has been selected
         */
        //        tdtNPAChrgAD.setEnabled(chkNPAChrgAD.isSelected());
        //        tdtNPAChrgAD.setDateValue("");
//        observableOtherDetails.setTdtNPAChrgAD("");
    }

    private void chkABBChrgADActionPerformed() {
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
    }

    private void chkNonMainMinBalChrgADActionPerformed() {
        /* we have to show the Non maintenance of minimum balance charge text box
         * only if the corresponding check box has been selected
         */
        //        boolean isSelected = chkNonMainMinBalChrgAD.isSelected();
        //        txtMinActBalanceAD.setEditable(isSelected);
        //        txtMinActBalanceAD.setEnabled(isSelected);
        if (!isSelected) {
            //            txtMinActBalanceAD.setText("");
//            observableOtherDetails.setTxtMinActBalanceAD("");
        }
    }

    private void chkCreditADActionPerformed() {
        /* we have to enable the Credit card No., text field and the validity date
         * only when the user selected the credit card option
         */
        //        boolean isSelected = chkCreditAD.isSelected();
        //        txtCreditNoAD.setEditable(isSelected);
        //        txtCreditNoAD.setEnabled(isSelected);
        //        tdtCreditFromDateAD.setEnabled(isSelected);
        //        tdtCreditToDateAD.setEnabled(isSelected);
        //        chkCreditAD.setEnabled(isSelected);
        if (!isSelected) {
//            observableOtherDetails.setTxtCreditNoAD("");
//            observableOtherDetails.setTdtCreditToDateAD("");
//            observableOtherDetails.setTdtCreditFromDateAD("");
            //            txtCreditNoAD.setText("");
            //            tdtCreditFromDateAD.setDateValue("");
            //            tdtCreditToDateAD.setDateValue("");
        }
    }

    private void chkDebitADActionPerformed() {
        /* we have to enable the debit card No., text field and the validity date
         * only when the user selected the debit card option
         */
        //        boolean isSelected = chkDebitAD.isSelected();
        //        txtDebitNoAD.setEditable(isSelected);
        //        txtDebitNoAD.setEnabled(isSelected);
        //        tdtDebitToDateAD.setEnabled(isSelected);
        //        tdtDebitFromDateAD.setEnabled(isSelected);
        //        chkDebitAD.setEnabled(isSelected);
        if (!isSelected) {
//            observableOtherDetails.setTxtDebitNoAD("");
//            observableOtherDetails.setTdtDebitToDateAD("");
//            observableOtherDetails.setTdtDebitFromDateAD("");
            //            txtDebitNoAD.setText("");
            //            tdtDebitToDateAD.setDateValue("");
            //            tdtDebitFromDateAD.setDateValue("");
        }
    }

    private java.sql.Timestamp getTimestamp(java.util.Date date) {
        return new java.sql.Timestamp(date.getYear(), date.getMonth(), date.getDate(), date.getHours(), date.getMinutes(), date.getSeconds(), 0);
    }

    private java.math.BigDecimal getBigDecimal(double doubleValue) {
        return new java.math.BigDecimal(doubleValue);
    }

    private void updateInterestDetails() {
        txtInter.setText(observable.getInterest());
        txtPenalInter.setText(observable.getPenalInterest());
    }

    private void rdoNo_DocumentDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoNo_DocumentDetailsActionPerformed
        // TODO add your handling code here:
        rdoNo_DocumentDetailsActionPerformed();
    }//GEN-LAST:event_rdoNo_DocumentDetailsActionPerformed
    private void rdoNo_DocumentDetailsActionPerformed() {
        if (rdoNo_DocumentDetails.isSelected()) {
            observableDocument.setTdtSubmitDate_DocumentDetails("");
            tdtSubmitDate_DocumentDetails.setDateValue("");
            tdtSubmitDate_DocumentDetails.setEnabled(false);
        }
    }
    private void rdoYes_DocumentDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoYes_DocumentDetailsActionPerformed
        // TODO add your handling code here:
        rdoYes_DocumentDetailsActionPerformed();
    }//GEN-LAST:event_rdoYes_DocumentDetailsActionPerformed
    private void rdoYes_DocumentDetailsActionPerformed() {
        if (rdoYes_DocumentDetails.isSelected()) {
            tdtSubmitDate_DocumentDetails.setEnabled(true);
        }
    }
    private void tdtSubmitDate_DocumentDetailsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtSubmitDate_DocumentDetailsFocusLost
        // TODO add your handling code here:
        tdtSubmitDate_DocumentDetailsFocusLost();
    }//GEN-LAST:event_tdtSubmitDate_DocumentDetailsFocusLost
    private void tdtSubmitDate_DocumentDetailsFocusLost() {
        // To check whether the Submitted Date is not future date
        ClientUtil.validateLTDate(tdtSubmitDate_DocumentDetails);
    }
    private void btnSave_DocumentDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSave_DocumentDetailsActionPerformed
        // TODO add your handling code here:
        btnSave_DocumentDetailsActionPerformed();
    }//GEN-LAST:event_btnSave_DocumentDetailsActionPerformed
    private void btnSave_DocumentDetailsActionPerformed() {
        String strWarnMsg = isMandatoryEnteredNDocDetails();
        if (strWarnMsg.length() != 0) {
            displayAlert(strWarnMsg);
        } else {
            updateOBFields();
            observableDocument.saveDocumentTab(rowDocument);
            observableDocument.resetDocumentDetails();
            observable.ttNotifyObservers();
            setAllDocumentDetailsEnableDisable(false);
            rowDocument = -1;
            updateDocument = false;
        }
    }

    private String isMandatoryEnteredNDocDetails() {
        StringBuffer stbWarnMsg = new StringBuffer("");
        GoldLoanMRB termLoanMRB = new GoldLoanMRB();
        if (rdoYes_Executed_DOC.isSelected() && tdtExecuteDate_DOC.getDateValue().length() <= 0) {
            stbWarnMsg.append(termLoanMRB.getString("tdtExecuteDate_DOC"));
        }
        if (rdoYes_DocumentDetails.isSelected() && tdtSubmitDate_DocumentDetails.getDateValue().length() <= 0) {
            stbWarnMsg.append(termLoanMRB.getString("tdtSubmitDate_DocumentDetails"));
        }
        termLoanMRB = null;
        return stbWarnMsg.toString();
    }
    private void tblTable_DocumentDetailsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTable_DocumentDetailsMousePressed
        // TODO add your handling code here:
        tblTable_DocumentDetailsMousePressed();
    }//GEN-LAST:event_tblTable_DocumentDetailsMousePressed

    private void tblTable_DocumentDetailsMousePressed() {
        if (tblTable_DocumentDetails.getSelectedRow() >= 0) {
            updateOBFields();
            removeDocumentRadioBtns();
            addDocumentRadioBtns();
            observableDocument.resetDocumentDetails();
            observableDocument.populateDocumentDetails((tblTable_DocumentDetails.getSelectedRow()));
            if ((observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) || (viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT) || viewType.equals("Enquirystatus"))) {
                // If the record is populated for Delete or Authorization
                setAllDocumentDetailsEnableDisable(false);
                setDocumentToolBtnEnableDisable(false);
            } else {
                setAllDocumentDetailsEnableDisable(true);
                setDocumentToolBtnEnableDisable(true);
                if (observableDocument.getRdoNo_DocumentDetails()) {
                    tdtSubmitDate_DocumentDetails.setEnabled(false);
                }
                updateDocument = true;
            }
            rowDocument = tblTable_DocumentDetails.getSelectedRow();
            observable.ttNotifyObservers();
        }
    }

    private void txtNoInstallmentsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNoInstallmentsFocusLost
        // TODO add your handling code here:
        //        txtNoInstallmentsFocusLost();
        cboRepayFreqActionPerformed();
        btnRepayShedule.setEnabled(true);
    }//GEN-LAST:event_txtNoInstallmentsFocusLost

    private boolean txtNoInstallmentsFocusLost() {
        boolean flag = false;
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            if (cboRepayFreq.getSelectedIndex() > 0) {
                HashMap repaymentMap = new HashMap();
                repaymentMap.put("PROD_ID", lblDocumentProdIdValue.getText());
                double period = 0.0;
                if (cboRepayFreq.getSelectedItem().equals("Yearly")) {
                    period = 365;
                } else if (cboRepayFreq.getSelectedItem().equals("Half Yearly")) {
                    period = 180;
                } else if (cboRepayFreq.getSelectedItem().equals("Quaterly")) {
                    period = 90;
                } else if (cboRepayFreq.getSelectedItem().equals("Monthly")) {
                    period = 30;
                }else if(cboRepayFreq.getSelectedItem().equals("Fortnight")){
                    period = 15;
                }else if(cboRepayFreq.getSelectedItem().equals("USER_DEFINED_GOLD_LOAN")){// Added by nithya on 03-07-2019 for KD-546 New Gold Loan -45 days maturity type
                    period = observable.getMaximumDaysForLoan();
                }
                repaymentMap.put("PERIOD", new Double(period));
                List lst = ClientUtil.executeQuery("getRepaymentPeriod", repaymentMap);
                if (lst != null && lst.size() > 0) {
                    repaymentMap = (HashMap) lst.get(0);
                    double min = CommonUtil.convertObjToDouble(repaymentMap.get("MIN_PERIOD")).doubleValue();
                    double max = CommonUtil.convertObjToDouble(repaymentMap.get("MAX_PERIOD")).doubleValue();
                    if (period >= min && period <= max) {
                    } else {
                        ClientUtil.showAlertWindow("Repayment Minimum period  : " + min + " days  " + " Repayment Maximum Period : " + max + " days");
                        flag = true;
                    }
                }
            }
        }
        return flag;
    }

    private void txtLimit_SDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLimit_SDFocusLost
        // TODO add your handling code here:
        setImbpSettings();//this line added by Anju Anand for Mantid Id: 0010365
        String mainLimit = CommonUtil.convertObjToStr(txtLimit_SD.getText());
        //        txtSanctionLimitValue.setText(String.valueOf(mainLimit));
        //deposit lien shoude be changed while limit is changed
        //        if(loanType.equals("LTD"))//&& observable.getStrACNumber().length()>0)
        //            if(mainLimitMarginValidation(mainLimit))
        //                return;
        //
        //        rupeesValidation(mainLimit);
        observable.setFacilityAcctHead();
        boolean flag = txtLimit_SDFocusLost();
        if (flag != true) {
            //od balance check
            //        txtLimit_SDFocusLostOD(true);
            if (cboCategory.getSelectedIndex() <= 0) {
                ClientUtil.showAlertWindow("Category should not be empty !!!!");
                txtLimit_SD.setText("");
                return;
            }
            HashMap whereMap = new HashMap();
            whereMap.put("CATEGORY_ID", observableBorrow.getCbmCategory().getKeyForSelected());
            whereMap.put("AMOUNT", getBigDecimal(CommonUtil.convertObjToDouble(txtLimit_SD.getText()).doubleValue()));
            whereMap.put("PROD_ID", eachProdId);
            //        if (tdtAccountOpenDate.getDateValue().length()==0) tdtFDate.setDateValue(observable.getTdtFDate());
            whereMap.put("FROM_DATE", setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtAccountOpenDate.getDateValue())));
            //        if (tdtTDate.getDateValue().length()==0) tdtTDate.setDateValue(observable.getTdtTDate());
            whereMap.put("TO_DATE", setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtDemandPromNoteExpDate.getDateValue())));
            //        deleteAllInterestDetails();
            observableInt.resetInterestDetails();
            updateInterestDetails();
            // Populate the values
            //                String interestType=CommonUtil.convertObjToStr(((ComboBoxModel)cboInterestType).getKeyForSelected().toString());
            //        String interestType=(String)(((ComboBoxModel)cboInterestType.getModel()).getKeyForSelected()).toString();
            ArrayList interestList = null;
            //        if(interestType !=null && interestType.equals("FLOATING_RATE"))
            //            interestList = (java.util.ArrayList)ClientUtil.executeQuery("getSelectProductTermLoanInterestFloatTO", whereMap);
            //        else
            ArrayList interestType = (ArrayList) ClientUtil.executeQuery("getSelectProductTermLoanInterestTO", whereMap);
            //        List list = (List) sqlMap.executeQueryForList("getSelectTermLoanJointAcctTO", where);
            //        returnMap.put("TermLoanJointAcctTO", list);
//            appraiserCommisionAmt();
             chargeAmount();//babu comm on 18-01-2014
            if (interestType != null && interestType.size() > 0) {
                TermLoanInterestTO termLoanInterestTO = (TermLoanInterestTO) interestType.get(0);
                txtInter.setText(CommonUtil.convertObjToStr(termLoanInterestTO.getInterest()));
                txtPenalInter.setText(CommonUtil.convertObjToStr(termLoanInterestTO.getPenalInterest()));
                txtInter.setEnabled(false);
                txtPenalInter.setEnabled(false);
            } else {
                ClientUtil.showAlertWindow("Rate of interest is not set for this product");
                return;
            }    
             //Added by sreekrishnan for 11027
            if(!(txtGrossWeight.getText()!=null && !txtGrossWeight.getText().equals("") && txtNetWeight.getText()!=null && !txtNetWeight.getText().equals(""))){
                if(CommonUtil.convertObjToDouble(txtLimit_SD.getText())>0 && CommonUtil.convertObjToDouble(txtMarketRate.getText())>0){
                    double requiredGold = CommonUtil.convertObjToDouble(txtLimit_SD.getText())/CommonUtil.convertObjToDouble(txtMarketRate.getText());
                    ClientUtil.showMessageWindow("<html>Required Gold(Approximation) For Requested Loan Amount is : <b>"+Math.round(requiredGold)+"g</b></html>");
                }
            }
            if((txtGrossWeight.getText()!=null && !txtGrossWeight.getText().equals("") && txtNetWeight.getText()!=null && !txtNetWeight.getText().equals(""))&& mainLimit!=null && mainLimit.length()>0){
                double allowedGoldRate = CommonUtil.convertObjToDouble(txtLimit_SD.getText())/CommonUtil.convertObjToDouble(txtNetWeight.getText());
                ClientUtil.showMessageWindow("<html>Granted Gold Rate(Approximation)/g For Loan Amount is : <b>"+Math.round(allowedGoldRate)+"Rs.</b></html>");
            }
        }    
    }//GEN-LAST:event_txtLimit_SDFocusLost
    private void chargeAmount() {
        serviceTaxApplMap = new HashMap();//31-07
        serviceTaxIdMap = new HashMap();
        HashMap appraiserMap = new HashMap();
        appraiserMap.put("SCHEME_ID", prodDesc);
        if (transNew) {
            appraiserMap.put("DEDUCTION_ACCU", "O");
        } else {
           // appraiserMap.put("DEDUCTION_ACCU", "C");//20-04-2014
            appraiserMap.put("DEDUCTION_ACCU_RENEWAL", "DEDUCTION_ACCU_RENEWAL");
            
        }
        chargelst = ClientUtil.executeQuery("getAllChargeDetailsData", appraiserMap);
        HashMap chargeMap = new HashMap();
        if (chargelst != null && chargelst.size() > 0) {
            for (int i = 0; i < chargelst.size(); i++) {
                String accHead = "";
                chargeMap = (HashMap) chargelst.get(i);
                accHead = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_ID"));
                 double loanRunPeriod=0;
                 double totalLoanPeriod=0;
              if(tdtAccountOpenDate.getDateValue()==null||tdtAccountOpenDate.getDateValue().isEmpty()||tdtAccountOpenDate.getDateValue().equals("")){
                  }
              else    {
                 loanRunPeriod = DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(tdtAccountOpenDate.getDateValue()), (Date)curr_dt.clone());
                 totalLoanPeriod = DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(tdtAccountOpenDate.getDateValue()), DateUtil.getDateMMDDYYYY(tdtDemandPromNoteExpDate.getDateValue()));
              }

                 //31-07
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
                            serviceTaxApplMap.put(accHead,checkFlag);   
                            serviceTaxIdMap.put(accHead,serviceTaxId);
                            }
                        }
                    }                    
                } 
              
              
                for (int j = 0; j < table.getRowCount(); j++) {
                    if (CommonUtil.convertObjToStr(table.getValueAt(j, 1)).equals(accHead)) {
                        double chargeAmt = 0;
                        if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Sanction Amount")) {
                            chargeAmt = CommonUtil.convertObjToDouble(txtLimit_SD.getText()).doubleValue()
                                    * CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(chargeMap.get("CHARGE_RATE"))).doubleValue() / 100;
                            long roundOffType = getRoundOffType(CommonUtil.convertObjToStr(chargeMap.get("ROUND_OFF_TYPE")));
                            if (roundOffType != 0) {
                                chargeAmt = rd.getNearest((long) (chargeAmt * roundOffType), roundOffType) / roundOffType;
                            }
                            double minAmt = CommonUtil.convertObjToDouble(chargeMap.get("MIN_CHARGE_AMOUNT")).doubleValue();
                            double maxAmt = CommonUtil.convertObjToDouble(chargeMap.get("MAX_CHARGE_AMOUNT")).doubleValue();                            
                            if (!CommonUtil.convertObjToStr(chargeMap.get("DAY_WISE_CALC")).equals("") && CommonUtil.convertObjToStr(chargeMap.get("DAY_WISE_CALC"))!=null
                                    && CommonUtil.convertObjToStr(chargeMap.get("DAY_WISE_CALC")).equals("Y")) {
                                    chargeAmt = Math.round((chargeAmt/totalLoanPeriod)*loanRunPeriod);
                            }
                            //Added by sreekrishnan 
                            if(!CommonUtil.convertObjToStr(chargeMap.get("DAILY_PRODUCT")).equals("") && CommonUtil.convertObjToStr(chargeMap.get("DAILY_PRODUCT"))!=null
                                    && CommonUtil.convertObjToStr(chargeMap.get("DAILY_PRODUCT")).equals("Y")) {                                 
                                chargeAmt = Math.round((CommonUtil.convertObjToDouble(txtLimit_SD.getText()).doubleValue()*loanRunPeriod*CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(chargeMap.get("CHARGE_RATE"))).doubleValue())/36500);
                            }
                            if (chargeAmt < minAmt) {
                                chargeAmt = minAmt;
                            }
                            if (chargeAmt > maxAmt) {
                                chargeAmt = maxAmt;
                            }
                            chargeAmt = (double) higher((long) (chargeAmt * 100), 100) / 100;
                            table.setValueAt(String.valueOf(chargeAmt), j, 3);
                        } else if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Amount Range")) {
                            List chargeslabLst = ClientUtil.executeQuery("getSelectLoanSlabChargesTO", chargeMap);
                            double limit = CommonUtil.convertObjToDouble(txtLimit_SD.getText()).doubleValue();
                            double minAmt = 0;
                            double maxAmt = 0;
                            if (chargeslabLst != null && chargeslabLst.size() > 0) {                                
                                for (int k = 0; k < chargeslabLst.size(); k++) {
                                    LoanSlabChargesTO objLoanSlabChargesTO = (LoanSlabChargesTO) chargeslabLst.get(k);

                                    double minAmtRange = CommonUtil.convertObjToDouble(objLoanSlabChargesTO.getFromSlabAmt()).doubleValue();
                                    double maxAmtRange = CommonUtil.convertObjToDouble(objLoanSlabChargesTO.getToSlabAmt()).doubleValue();
                                    if (limit >= minAmtRange && limit <= maxAmtRange) {
                                        double chargeRate = CommonUtil.convertObjToDouble(objLoanSlabChargesTO.getChargeRate()).doubleValue();
                                        minAmt = CommonUtil.convertObjToDouble(objLoanSlabChargesTO.getMinChargeAmount()).doubleValue();
                                        maxAmt = CommonUtil.convertObjToDouble(objLoanSlabChargesTO.getMaxChargeAmount()).doubleValue();

                                        chargeAmt = CommonUtil.convertObjToDouble(txtLimit_SD.getText()).doubleValue() * chargeRate / 100;
                                        if (chargeAmt < minAmt) {
                                            chargeAmt = minAmt;
                                        }
                                        if (chargeAmt > maxAmt) {
                                            chargeAmt = maxAmt;
                                        }
                                        break;
                                    }
                                }

                            }
                            if (!CommonUtil.convertObjToStr(chargeMap.get("DAY_WISE_CALC")).equals("") && CommonUtil.convertObjToStr(chargeMap.get("DAY_WISE_CALC"))!=null
                                    && CommonUtil.convertObjToStr(chargeMap.get("DAY_WISE_CALC")).equals("Y")) {
                                    chargeAmt = Math.round((chargeAmt/totalLoanPeriod)*loanRunPeriod);
                            }
                            if (chargeAmt < minAmt) {
                                chargeAmt = minAmt;
                            }
                            if (chargeAmt > maxAmt) {
                                chargeAmt = maxAmt;
                            }
                            chargeAmt = (double) higher((long) (chargeAmt * 100), 100) / 100;
                            table.setValueAt(String.valueOf(chargeAmt), j, 3);
//                            chargeAmt = CommonUtil.convertObjToDouble(txtLimit_SD.getText()).doubleValue() *
//                            CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(chargeMap.get("CHARGE_RATE"))).doubleValue()/100;
//                            table.setValueAt(String.valueOf(chargeAmt),j, 2);
                        } else if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Flat Charge")) {
                            if (!CommonUtil.convertObjToStr(chargeMap.get("DAY_WISE_CALC")).equals("") && CommonUtil.convertObjToStr(chargeMap.get("DAY_WISE_CALC"))!=null
                                    && CommonUtil.convertObjToStr(chargeMap.get("DAY_WISE_CALC")).equals("Y")) {
                                    chargeAmt = Math.round((chargeAmt/totalLoanPeriod)*loanRunPeriod);
                            }
                            chargeAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(chargeMap.get("FLAT_CHARGE"))).doubleValue();
                        }
                        chargeMap.put("CHARGE_AMOUNT", String.valueOf(chargeAmt));
                    }
                }
            }
            
                  List taxSettingsList = new ArrayList();
            System.out.println("serviceTaxIdMap :: " + serviceTaxIdMap);           
            if (serviceTaxApplMap != null && serviceTaxApplMap.size() > 0) {                           
                double chrgamt = 0;
                for (int k = 0; k < table.getRowCount(); k++) {
                    HashMap serviceTaSettingsMap = new HashMap();
                    boolean checkFlag = new Boolean(CommonUtil.convertObjToStr(table.getValueAt(k, 0))).booleanValue();
                    String descVal = CommonUtil.convertObjToStr(table.getValueAt(k, 1));
                    if (checkFlag && CommonUtil.convertObjToStr(serviceTaxApplMap.get(descVal)).equals("Y") && CommonUtil.convertObjToStr(serviceTaxIdMap.get(descVal)).length() > 0) {
                        chrgamt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(table.getValueAt(k, 3)));
                    }
                    if (chrgamt > 0) {   
                        serviceTaSettingsMap.put("SETTINGS_ID",serviceTaxIdMap.get(descVal));
                        serviceTaSettingsMap.put(ServiceTaxCalculation.TOT_AMOUNT,CommonUtil.convertObjToStr(chrgamt));  
                        taxSettingsList.add(serviceTaSettingsMap);
                    } 
                }
            }            
            if (taxSettingsList != null && taxSettingsList.size() > 0) {
                try {
                    objServiceTax = new ServiceTaxCalculation();
                    HashMap ser_Tax_Val = new HashMap();
                    ser_Tax_Val.put(ServiceTaxCalculation.CURR_DT, ClientUtil.getCurrentDate());
                    ser_Tax_Val.put("SERVICE_TAX_DATA", taxSettingsList);
                    serviceTax_Map = objServiceTax.calculateServiceTax(ser_Tax_Val);
                    if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.TOT_TAX_AMT)) {
                        String amt = CommonUtil.convertObjToStr(serviceTax_Map.get(ServiceTaxCalculation.TOT_TAX_AMT));
                        lblServiceTaxOpeningVal.setText(amt);
                        serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, amt);
                    } else {
                        lblServiceTaxOpeningVal.setText("0.00");
                        serviceTax_Map = null;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                lblServiceTaxOpeningVal.setText("0.00");
                serviceTax_Map = null;
            }           
            
            
            table.revalidate();
            table.updateUI();
            //            observable.setChargelst(chargelst);
        }

    }

    private int getRoundOffType(String roundOff) {
        int returnVal = 0;
        if (roundOff.equals("Nearest Value")) {
            returnVal = 1 * 100;
        } else if (roundOff.equals("Nearest Hundreds")) {
            returnVal = 100 * 100;
        } else if (roundOff.equals("Nearest Tens")) {
            returnVal = 10 * 100;
        }
        return returnVal;
    }

    private double getSanctionAmount(String viewType) {
        HashMap hash = new HashMap();
        double sanctionAmt = 0;
        String acNum = lblAcctNo_Sanction_Disp.getText();
        hash.put("ACCT_NUM", acNum);
        List sanctionLst = null;
        if (viewType.equals(AUTHORIZE) || viewType.equals(REJECT)) {
            sanctionLst = ClientUtil.executeQuery("getSanctionAmountwithoutauth", hash);
            panRenewalChargeDetails.setEnabled(false);
        } else {
            sanctionLst = ClientUtil.executeQuery("getSanctionAmount", hash);
        }
        if (sanctionLst != null && sanctionLst.size() > 0) {
            hash = (HashMap) sanctionLst.get(0);
            sanctionAmt = CommonUtil.convertObjToDouble(hash.get("SANCTION_AMOUNT")).doubleValue();
        }
        return sanctionAmt;
    }

    private void editChargeAmount(String viewType) {
        /*  HashMap appraiserMap = new HashMap();
        appraiserMap.put("SCHEME_ID", prodDesc);
        //appraiserMap.put("DEDUCTION_ACCU", "O");
        appraiserMap.put("DEDUCTION_ACCU", "C");
        chargelst = ClientUtil.executeQuery("getAllChargeDetailsData", appraiserMap);
        HashMap chargeMap = new HashMap();
        if (chargelst != null && chargelst.size() > 0) {
        for (int i = 0; i < chargelst.size(); i++) {
        String accHead = "";
        chargeMap = (HashMap) chargelst.get(i);
        accHead = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_DESC"));
        ////System.out.println("$#@@$ accHead" + accHead);
        for (int j = 0; j < table.getRowCount(); j++) {
        ////System.out.println("$#@@$ accHead inside table " + table.getValueAt(j, 0));
        if (CommonUtil.convertObjToStr(table.getValueAt(j, 0)).equals(accHead)) {
        double chargeAmt = 0;
        if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Sanction Amount")) {
        chargeAmt = CommonUtil.convertObjToDouble(txtLimit_SD.getText()).doubleValue()
         * CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(chargeMap.get("CHARGE_RATE"))).doubleValue() / 100;
        long roundOffType = getRoundOffType(CommonUtil.convertObjToStr(chargeMap.get("ROUND_OFF_TYPE")));
        if (roundOffType != 0) {
        chargeAmt = rd.getNearest((long) (chargeAmt * roundOffType), roundOffType) / roundOffType;
        }
        double minAmt = CommonUtil.convertObjToDouble(chargeMap.get("MIN_CHARGE_AMOUNT")).doubleValue();
        double maxAmt = CommonUtil.convertObjToDouble(chargeMap.get("MAX_CHARGE_AMOUNT")).doubleValue();
        if (chargeAmt < minAmt) {
        chargeAmt = minAmt;
        }
        if (chargeAmt > maxAmt) {
        chargeAmt = maxAmt;
        }
        table.setValueAt(String.valueOf(chargeAmt), j, 1);
        //System.out.println("chargeAmt gl 1=="+chargeAmt);
        } else if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Amount Range")) {
        List chargeslabLst = ClientUtil.executeQuery("getSelectLoanSlabChargesTO", chargeMap);
        double limit = CommonUtil.convertObjToDouble(txtLimit_SD.getText()).doubleValue();
        if (chargeslabLst != null && chargeslabLst.size() > 0) {
        double minAmt = 0;
        double maxAmt = 0;
        for (int k = 0; k < chargeslabLst.size(); k++) {
        LoanSlabChargesTO objLoanSlabChargesTO = (LoanSlabChargesTO) chargeslabLst.get(k);
        
        double minAmtRange = CommonUtil.convertObjToDouble(objLoanSlabChargesTO.getFromSlabAmt()).doubleValue();
        double maxAmtRange = CommonUtil.convertObjToDouble(objLoanSlabChargesTO.getToSlabAmt()).doubleValue();
        if (limit >= minAmtRange && limit <= maxAmtRange) {
        double chargeRate = CommonUtil.convertObjToDouble(objLoanSlabChargesTO.getChargeRate()).doubleValue();
        minAmt = CommonUtil.convertObjToDouble(objLoanSlabChargesTO.getMinChargeAmount()).doubleValue();
        maxAmt = CommonUtil.convertObjToDouble(objLoanSlabChargesTO.getMaxChargeAmount()).doubleValue();
        
        chargeAmt = CommonUtil.convertObjToDouble(txtLimit_SD.getText()).doubleValue() * chargeRate / 100;
        if (chargeAmt < minAmt) {
        chargeAmt = minAmt;
        }
        if (chargeAmt > maxAmt) {
        chargeAmt = maxAmt;
        }
        break;
        }
        }
        
        }
        
        //                             chargeAmt = CommonUtil.convertObjToDouble(txtLimit_SD.getText()).doubleValue() *
        //                             CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(chargeMap.get("CHARGE_RATE"))).doubleValue()/100;
        table.setValueAt(String.valueOf(chargeAmt), j, 1);
        //                            chargeAmt = CommonUtil.convertObjToDouble(txtLimit_SD.getText()).doubleValue() *
        //                            CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(chargeMap.get("CHARGE_RATE"))).doubleValue()/100;
        //                            table.setValueAt(String.valueOf(chargeAmt),j, 1);
        } else if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Flat Charge")) {
        chargeAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(chargeMap.get("FLAT_CHARGE"))).doubleValue();
        }
        chargeMap.put("CHARGE_AMOUNT", String.valueOf(chargeAmt));
        //System.out.println("chargeAmt gl 2=="+chargeAmt);
        }
        }
        }
        ////System.out.println("#$#$$# chargeMap:" + chargeMap);
        ////System.out.println("#$#$$# chargelst:" + chargelst);
        table.revalidate();
        table.updateUI();
        //            observable.setChargelst(chargelst);
        }
         */
        serviceTaxApplMap = new HashMap();// Added by nithya on 06-10-2018 for KD 263
        serviceTaxIdMap = new HashMap();// Added by nithya on 06-10-2018 for KD 263
        double sanctionAmt = getSanctionAmount(viewType);
//        if(observable.isRenewalYesNo()){//KD-3774
//            sanctionAmt = CommonUtil.convertObjToDouble(txtRenewalLimit_SD.getText());
//        }
        HashMap hash = new HashMap();
        hash.put("SCHEME_ID", CommonUtil.convertObjToStr(prodDesc));
       // hash.put("DEDUCTION_ACCU", "C");//20-03-2014
        hash.put("DEDUCTION_ACCU_RENEWAL", "DEDUCTION_ACCU_RENEWAL");
        chargelst = ClientUtil.executeQuery("getAllChargeDetailsData", hash);
        HashMap chargeMap = new HashMap();
        if (chargelst != null && chargelst.size() > 0) {
            for (int i = 0; i < chargelst.size(); i++) {
                //                String accHead="";
                String desc = "";
                String editable = "";
                chargeMap = (HashMap) chargelst.get(i);
                String chargeType = CommonUtil.convertObjToStr(chargeMap.get("DEDUCTION_ACCU"));
                if (observable.isRenewalYesNo() && chargeType.equals("O")) {
                    sanctionAmt = CommonUtil.convertObjToDouble(txtRenewalLimit_SD.getText());
                }
                //                accHead = CommonUtil.convertObjToStr(chargeMap.get("ACC_HEAD"));
                desc = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_ID"));
                // Added by nithya on 09-10-2018 for KD 263 GST - Needed to implement the GST in GOld Loan renewal
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
                            serviceTaxApplMap.put(desc,checkFlag);   
                            serviceTaxIdMap.put(desc,serviceTaxId);
                            }
                        }
                    }
                    String actNum = lblAcctNo_Sanction_Disp.getText();
                    String prodId = ((ComboBoxModel) cboGoldLoanProd.getModel()).getKeyForSelected().toString();
                    taxListForGoldLoan = observable.calcServiceTaxAmount(actNum, prodId);                    
                }
               // End
                editable = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_EDITABLE"));                
                double loanRunPeriod = DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(tdtAccountOpenDate.getDateValue()), (Date)curr_dt.clone());
                double totalLoanPeriod = DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(tdtAccountOpenDate.getDateValue()), DateUtil.getDateMMDDYYYY(tdtDemandPromNoteExpDate.getDateValue()));
                ClientUtil.executeQuery("PRINT_SERVRSIDE", chargeMap); //Added By Kannan AR
                for (int j = 0; j < table.getRowCount(); j++) {
                    //System.out.println("$#@@$ desc inside table  " + table.getValueAt(j, 1) + "desc === " + desc);
                    if (CommonUtil.convertObjToStr(table.getValueAt(j, 1)).equals(desc)) {
                        double chargeAmt = 0;
                        if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Sanction Amount")) {
                            chargeAmt = sanctionAmt * CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(chargeMap.get("CHARGE_RATE"))).doubleValue() / 100;
                            long roundOffType = getRoundOffType(CommonUtil.convertObjToStr(chargeMap.get("ROUND_OFF_TYPE")));
                            if (roundOffType != 0) {
                                chargeAmt = rd.getNearest((long) (chargeAmt * roundOffType), roundOffType) / roundOffType;
                            }
                            double minAmt = CommonUtil.convertObjToDouble(chargeMap.get("MIN_CHARGE_AMOUNT")).doubleValue();
                            double maxAmt = CommonUtil.convertObjToDouble(chargeMap.get("MAX_CHARGE_AMOUNT")).doubleValue();
                            
                            if (!CommonUtil.convertObjToStr(chargeMap.get("DAY_WISE_CALC")).equals("") && CommonUtil.convertObjToStr(chargeMap.get("DAY_WISE_CALC"))!=null
                                    && CommonUtil.convertObjToStr(chargeMap.get("DAY_WISE_CALC")).equals("Y")) {
                                    chargeAmt = Math.round((chargeAmt/totalLoanPeriod)*loanRunPeriod);
                            }
                            //Added by sreekrishnan 
                            if(!CommonUtil.convertObjToStr(chargeMap.get("DAILY_PRODUCT")).equals("") && CommonUtil.convertObjToStr(chargeMap.get("DAILY_PRODUCT"))!=null
                                    && CommonUtil.convertObjToStr(chargeMap.get("DAILY_PRODUCT")).equals("Y")) {                                 
                                chargeAmt = Math.round((sanctionAmt*loanRunPeriod*CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(chargeMap.get("CHARGE_RATE"))).doubleValue())/36500);
                            }
                            if (chargeAmt < minAmt) {
                                chargeAmt = minAmt;
                            }
                            if (chargeAmt > maxAmt) {
                                chargeAmt = maxAmt;
                            }
                            table.setValueAt(String.valueOf(chargeAmt), j, 3);
                            //System.out.println("chargeAmt0001 = " + chargeAmt);
                        } else if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Amount Range")) {
                            HashMap slabMap = new HashMap();
                            double sancAmt = sanctionAmt;
                            slabMap.put("CHARGE_ID", chargeMap.get("CHARGE_ID"));
                            slabMap.put("AMOUNT", sancAmt);
                            List slablst = ClientUtil.executeQuery("getSlabAmount", slabMap);
                            if (slablst != null && slablst.size() > 0) {
                                slabMap = (HashMap) slablst.get(0);
                                chargeAmt = sanctionAmt
                                        * CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(slabMap.get("CHARGE_RATE"))).doubleValue() / 100;
                                long roundOffType = getRoundOffType(CommonUtil.convertObjToStr(slabMap.get("ROUND_OFF_TYPE")));
                                if (roundOffType != 0) {
                                    chargeAmt = rd.getNearest((long) (chargeAmt * roundOffType), roundOffType) / roundOffType;
                                }
                                double minAmt = CommonUtil.convertObjToDouble(slabMap.get("MIN_CHARGE_AMOUNT")).doubleValue();
                                double maxAmt = CommonUtil.convertObjToDouble(slabMap.get("MAX_CHARGE_AMOUNT")).doubleValue();
                                
                                if (!CommonUtil.convertObjToStr(chargeMap.get("DAY_WISE_CALC")).equals("") && CommonUtil.convertObjToStr(chargeMap.get("DAY_WISE_CALC"))!=null
                                    && CommonUtil.convertObjToStr(chargeMap.get("DAY_WISE_CALC")).equals("Y")) {
                                    chargeAmt = Math.round((chargeAmt/totalLoanPeriod)*loanRunPeriod);
                                }
                                if (chargeAmt < minAmt) {
                                    chargeAmt = minAmt;
                                }
                                if (chargeAmt > maxAmt) {
                                    chargeAmt = maxAmt;
                                }
                                chargeAmt = (double) higher((long) (chargeAmt * 100), 100) / 100;
                                table.setValueAt(String.valueOf(chargeAmt), j, 3);
                                //System.out.println("chargeAmt0002 = " + chargeAmt);
                            }
                        } else if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Flat Charge")) {
                            if (!CommonUtil.convertObjToStr(chargeMap.get("DAY_WISE_CALC")).equals("") && CommonUtil.convertObjToStr(chargeMap.get("DAY_WISE_CALC"))!=null
                                    && CommonUtil.convertObjToStr(chargeMap.get("DAY_WISE_CALC")).equals("Y")) {
                                    chargeAmt = Math.round((chargeAmt/totalLoanPeriod)*loanRunPeriod);
                            }
                            chargeAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(chargeMap.get("FLAT_CHARGE"))).doubleValue();
                        }
                        chargeMap.put("CHARGE_AMOUNT", String.valueOf(chargeAmt));
                    }
                    if (editable.equals("Y")) {
                        double chargeAmt1 = CommonUtil.convertObjToDouble(table.getValueAt(j,3));
                        chargeMap.put("CHARGE_AMOUNT", String.valueOf(chargeAmt1));
                    }
                }
            }
            // Added by nithya on 09-10-2018 for KD 263 GST - Needed to implement the GST in GOld Loan renewal
            List taxSettingsList = new ArrayList();
            //System.out.println("serviceTaxIdMap :: " + serviceTaxIdMap);           
            if (serviceTaxApplMap != null && serviceTaxApplMap.size() > 0) {                           
                double chrgamt = 0;
                for (int k = 0; k < table.getRowCount(); k++) {
                    HashMap serviceTaSettingsMap = new HashMap();
                    boolean checkFlag = new Boolean(CommonUtil.convertObjToStr(table.getValueAt(k, 0))).booleanValue();
                    String descVal = CommonUtil.convertObjToStr(table.getValueAt(k, 1));
                    if (checkFlag && CommonUtil.convertObjToStr(serviceTaxApplMap.get(descVal)).equals("Y") && CommonUtil.convertObjToStr(serviceTaxIdMap.get(descVal)).length() > 0) {
                        chrgamt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(table.getValueAt(k, 3)));
                    }
                    if (chrgamt > 0) {   
                        serviceTaSettingsMap.put("SETTINGS_ID",serviceTaxIdMap.get(descVal));
                        serviceTaSettingsMap.put(ServiceTaxCalculation.TOT_AMOUNT,CommonUtil.convertObjToStr(chrgamt));
                        //serviceTaSettingsMap.put(serviceTaxIdMap.get(descVal), CommonUtil.convertObjToStr(chrgamt));    
                        taxSettingsList.add(serviceTaSettingsMap);
                    } 
                }
            }
            if(taxListForGoldLoan != null && taxListForGoldLoan.size() > 0){
                taxSettingsList.addAll(taxSettingsList.size(), taxListForGoldLoan);
             }
                //System.out.println("serviceTaSettingsMap :: "+ taxSettingsList);
                if(taxSettingsList != null && taxSettingsList.size() > 0){
                try {
                    objServiceTax = new ServiceTaxCalculation();
                    HashMap ser_Tax_Val = new HashMap();
                    ser_Tax_Val.put(ServiceTaxCalculation.CURR_DT, ClientUtil.getCurrentDate());
                    //ser_Tax_Val.put(ServiceTaxCalculation.TOT_AMOUNT, CommonUtil.convertObjToStr(chrgamt));
                    ser_Tax_Val.put("SERVICE_TAX_DATA", taxSettingsList);
                    serviceTax_Map = objServiceTax.calculateServiceTax(ser_Tax_Val);
                    if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.TOT_TAX_AMT)) {
                        String amt = CommonUtil.convertObjToStr(serviceTax_Map.get(ServiceTaxCalculation.TOT_TAX_AMT));
//                        lblServiceTaxval.setText(objServiceTax.roundOffAmt(amt, "NEAREST_VALUE"));
//                        serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, objServiceTax.roundOffAmt(amt, "NEAREST_VALUE"));
                        lblServiceTaxVal.setText(amt);
                        serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, amt);
                    } else {
                        lblServiceTaxVal.setText("0.00");
                        serviceTax_Map = null;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }else{
              lblServiceTaxVal.setText("0.00");  
              serviceTax_Map = null;
            }
            //END
        }
        ////System.out.println("#$#$$# chargeMap:"+chargeMap);
        ////System.out.println("#$#$$# chargelst@@1:"+chargelst);
        table.revalidate();
        table.updateUI();
//        hash=new HashMap();
//        hash.put("PROD_ID",observable.getCboProductID());
//        List lst = ClientUtil.executeQuery("getIncidentalAcHead", hash);
//        if(lst!=null && lst.size()>0){
//            chargeMap = (HashMap)lst.get(0);
//            chargeMap.put("CHARGE_AMOUNT",String.valueOf(incidentAmt));
//            chargelst.add(chargeMap);
//            ////System.out.println("#$#$$# chargelst@@4:"+chargelst);
//        }
        ////System.out.println("#$#$$# chargelst@@22222222222222222222222 :" + chargelst);
    }

    private void appraiserCommisionAmt() {
        HashMap appraiserMap = new HashMap();
        appraiserMap.put("AMOUNT", txtLimit_SD.getText());
        appraiserMap.put("TODAYS_DATE", curr_dt.clone());
        appraiserMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        List lst = ClientUtil.executeQuery("getSelectAppraiserRate", appraiserMap);
        if (lst != null && lst.size() > 0) {
            appraiserMap = (HashMap) lst.get(0);
            double commisionAmt = 0.0;
            double roundedCommisionValue = 0;
            double appraiserAmt = CommonUtil.convertObjToDouble(appraiserMap.get("AMOUNT")).doubleValue();
            double percentage = CommonUtil.convertObjToDouble(appraiserMap.get("PERCENTAGE")).doubleValue();
            double serviceTax = CommonUtil.convertObjToDouble(appraiserMap.get("SERVICE_TAX")).doubleValue();
            if (percentage > 0) {
                roundedCommisionValue = CommonUtil.convertObjToDouble(txtLimit_SD.getText()).doubleValue() * percentage / 100;
            } else {
                roundedCommisionValue = (double) getNearest((long) (appraiserAmt * 100), 100) / 100;
                commisionAmt = roundedCommisionValue;
            }
            double serviceTaxAmt = roundedCommisionValue * serviceTax / 100;
            double roundedServiceTaxValue = (double) getNearest((long) (serviceTaxAmt * 100), 100) / 100;
            //            lblAppraiserFeeValue.setText(String.valueOf(roundedCommisionValue));
            //            lblServiceTaxValue.setText(String.valueOf(roundedServiceTaxValue));
        }
    }

    private boolean mainLimitMarginValidation(String mainLimit) {
        //        HashMap transactionMap=new HashMap();
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
        //                return true;
        //            }
        //        }
        return false;
    }

    private boolean txtLimit_SDFocusLost() {
        boolean flag = false;
        if (!txtLimit_SD.getText().equals("")) {
            if (!observable.checkLimitValue(txtLimit_SD.getText())) {
                String message = new String("The Limit value must fall within " + observable.getMinLimitValue().toString() + " and  " + observable.getMaxLimitValue().toString());
                displayAlert(message);
                observable.setTxtLimit_SD("");
                txtLimit_SD.setText(observable.getTxtLimit_SD());
                message = null;
                flag = true;
            }
        }
        return flag;
    }

    private void txtLimit_SDFocusLostOD(boolean isTrue) {
        if (!txtLimit_SD.getText().equals("") && observable.getStrACNumber() != null && observable.getStrACNumber().length() > 0) {
            HashMap totbalancemap = new HashMap();
            HashMap newLiablityMap = new HashMap();
            ////System.out.println("txtLimit_SD.getText()###" + txtLimit_SD.getText());
            double limit = Double.parseDouble(txtLimit_SD.getText());
            totbalancemap.put("ACT_NUM", observable.getStrACNumber());
            List totbalance = ClientUtil.executeQuery("getActDataAD", totbalancemap);
            if (totbalance != null && totbalance.size() > 0 && isTrue) {
                totbalancemap = (HashMap) totbalance.get(0);
                double clearbal = CommonUtil.convertObjToDouble(totbalancemap.get("CLEAR_BALANCE")).doubleValue();
                observable.setBEHAVES_LIKE(CommonUtil.convertObjToStr(totbalancemap.get("BEHAVES_LIKE")));
                clearbal *= (-1);
                if (limit <= clearbal) {
                    int result = ClientUtil.showAlertWindow("Sanction Amount is Less than Liablity" + "\n"
                            + "Liablity balance is" + CommonUtil.convertObjToStr(totbalancemap.get("CLEAR_BALANCE"))
                            + "Shall I continue");
                    if (result == 0) {
                        newLiablityMap.put("CLEAR_BALANCE", new Double(clearbal));
                        newLiablityMap.put("LIMIT", new Double(limit));
                        newLiablityMap.put("ACCT_NUM", observable.getStrACNumber());
                        newLiablityMap.put("DIFFERENT_AMT", new Double(clearbal - limit));
                        newLiablityMap.put("PROD_ID", totbalancemap.get("PROD_ID"));
                        newLiablityMap.put("USER_ID", TrueTransactMain.USER_ID);
                        //                    observable.setAdvanceLiablityMap(newLiablityMap.put);
                        observable.setAdvanceLiablityMap(new HashMap());
                        observable.getAdvanceLiablityMap().putAll(newLiablityMap);
                        //                        txtLimit_SD.setText("");
                        return;
                    } else {
                        observable.setAdvanceLiablityMap(new HashMap());
                    }

                }
                observable.setRenewAvailableBalance(String.valueOf(limit - clearbal));
            }
        }
    }
    private void rupeesValidation(String mainLimit) {
        if (mainLimit.length() > 0) {
            double limit = Double.parseDouble(mainLimit);
            if ((limit % 1) < 1 && (limit % 1) != 0) {
                ClientUtil.showMessageWindow("Enter The Amount  in Rupees");
                txtLimit_SD.setText("");
            }

        }
    }

    private void tdtDealingWithBankSinceFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtDealingWithBankSinceFocusLost
        // TODO add your handling code here:
        tdtDealingWithBankSinceFocusLost();
    }//GEN-LAST:event_tdtDealingWithBankSinceFocusLost
    private void tdtDealingWithBankSinceFocusLost() {
        // To check the entered date is less than or equal to current date
        ClientUtil.validateLTDate(tdtDealingWithBankSince);
    }

    private void cboTypeOfFacilityActionPerformed() {
        // Account Head should be resetted since its value will be changed
        // based on Type of Facility and Product ID
        observable.setLblAccHead_2("");
        //        lblAccHead_2.setText(observable.getLblAccHead_2());
        // update the Type of Facility in Observable
        String strFacilityType = getCboTypeOfFacilityKeyForSelected();
        if (strFacilityType.equals(LOANS_AGAINST_DEPOSITS)) {
            //            if (!observable.isThisCustDepositAcctHolder(txtCustID.getText())){
            //                clearFieldsForDefaultFacilityType();  // Commented by Rajesh
            //                return;
            //            }
        } else if (!strFacilityType.equals(LOANS_AGAINST_DEPOSITS)) {
            if (strFacilityType.length() > 0) {
                observable.setProductFacilityType(strFacilityType);

                if (!observable.isThisCustShareHolder(txtCustID.getText())) {
                    clearFieldsForDefaultFacilityType();
                    return;
                }
            }
        }
        //        observable.setCboTypeOfFacility(CommonUtil.convertObjToStr(cboTypeOfFacility.getSelectedItem()));
        if (strFacilityType.equals("CC") || strFacilityType.equals("OD")) {
            changesInUIForAdvanceProducts();
            if (!(viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT) || viewType.equals("Delete"))) {
                changesInUIExceptLoanAgainstDeposit();
            }
        } else {
            changesInUIForLoanProducts();
            if (strFacilityType.equals(LOANS_AGAINST_DEPOSITS)) {
                changesInUIForLoanAgainstDeposit();
            } else if (!(viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT) || viewType.equals("Delete"))) {
                changesInUIExceptLoanAgainstDeposit();
            }
        }
        // Set the Product ID model according to the Type of Facility
        if (observable.getCboTypeOfFacility().length() > 0) {
            observable.setFacilityProductID();
        } else {
            observable.setProdIDAsBlank();
        }
        // Update Product ID in the Observer thru Product ID ComboBoxModel
        //        cboProductId.setModel(observable.getCbmProductId());
        //        if (strFacilityType.equals(LOANS_AGAINST_DEPOSITS)){
        //            if (cboProductId.getItemCount() > 1)
        //                cboProductId.setSelectedIndex(1);
        //        }
        strFacilityType = null;
        // Populate the Product ID which is in the existing record
        if (sandetail) {
            observable.populateSanctionTabProdID(rowSanctionFacility);
        }


    }

    private String getCboTypeOfFacilityKeyForSelected() {
        //        return CommonUtil.convertObjToStr(((ComboBoxModel) cboTypeOfFacility.getModel()).getKeyForSelected());
        return "";
    }

    private void clearFieldsForDefaultFacilityType() {
        //        cboTypeOfFacility.setSelectedItem("");
        observable.getCbmTypeOfFacility().setKeyForSelected("");
        observable.setProdIDAsBlank();
        //        cboProductId.setModel(observable.getCbmProductId());
        observable.getCbmProductId().setKeyForSelected("");
        //        observable.setCboProductId("");
        //        cboProductId.setSelectedItem(observable.getCboProductId());
        if (!(viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT) || viewType.equals("Delete"))) {
            changesInUIExceptLoanAgainstDeposit();
        }
    }

    private void changesInUIForAdvanceProducts() {
        //        tabLimitAmount.add(panAccountDetails, "Other Details");
        //        tabLimitAmount.remove(settlementUI);
        //        fieldsToHideBasedOnAccount(false);
        lblNoInstallments.setText(resourceBundle.getString("lblNoInstallments_PROD"));
        lblRepayFreq.setText("");
        if (!(viewType.equals(AUTHORIZE) || viewType.equals("Delete"))) {
            observable.setCbmRepayFreq(observable.getCbmRepayFreq_ADVANCE());
            cboRepayFreq.setModel(observable.getCbmRepayFreq());
        }
        if ((observable.getStrACNumber().length() <= 0) || (observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) || (viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT))) {
            //            ClientUtil.enableDisable(panAccountDetails, false);
        } else {
            //            ClientUtil.enableDisable(panAccountDetails, true);
            disableLastIntApplDate();
        }
    }

    private void disableLastIntApplDate() {
        //        tdtDebit.setEnabled(false);
        //        tdtCredit.setEnabled(false);
    }

    private void changesInUIForLoanProducts() {
        //        fieldsToHideBasedOnAccount(true);
        lblNoInstallments.setText(resourceBundle.getString("lblNoInstallments"));
        lblRepayFreq.setText(resourceBundle.getString("lblRepayFreq"));
        if (!(viewType.equals(AUTHORIZE) || viewType.equals("Delete"))) {
            observable.setCbmRepayFreq(observable.getCbmRepayFreq_LOAN());
            cboRepayFreq.setModel(observable.getCbmRepayFreq());
        }
    }

    private void changesInUIForLoanAgainstDeposit() {
        HashMap whereMap = new HashMap();
        HashMap keyMap = new HashMap();
        HashMap depositName = null;
        boolean haveData = false;

        if (observable.getStrACNumber().length() > 0) {
            keyMap.put("ACCT_NO", observable.getStrACNumber());

            whereMap.put(CommonConstants.MAP_NAME, "getDepositLienDetails");
            whereMap.put(CommonConstants.MAP_WHERE, keyMap);
            java.util.List lst = ClientUtil.executeQuery("getDepositLienHolderName", keyMap);
            if (lst.size() > 0) {
                depositName = (HashMap) lst.get(0);
                //                haveData = ClientUtil.setTableModel(whereMap, tblSecurityTable, false);
                observableSecurity.setLblCustName_Security_Display(CommonUtil.convertObjToStr(depositName.get("NAME")));
            } else {
                ClientUtil.showMessageWindow("Deposit Lien details not found...");
            }
        }

        if (!haveData) {
            observableSecurity.setTblDepositSecurityTable();
            //            tblSecurityTable.setModel(observableSecurity.getTblSecurityTab());
            //            tabLimitAmount.add(panSecurityDetails, "Security Details");
        } else {
            //            observable.setDepositNo(CommonUtil.convertObjToStr(tblSecurityTable.getValueAt(0,0)));
            //            observableSecurity.setTblSecurityTab((com.see.truetransact.clientutil.EnhancedTableModel)tblSecurityTable.getModel());
        }

        whereMap = null;
        keyMap = null;
        setAllSecurityDetailsEnableDisable(false);
        setAllSecurityBtnsEnableDisable(false);
        //        tblSecurityTable.setEnabled(false);
    }

    private void changesInUIExceptLoanAgainstDeposit() {
        observableSecurity.setTblSecurityTable();
        //        tblSecurityTable.setModel(observableSecurity.getTblSecurityTab());
        //        tblSecurityTable.setEnabled(true);
    }

    //    private void fieldsToHideBasedOnAccount(boolean val){
    ////        lblFacility_Repay_Date.setVisible(val);
    ////        tdtFacility_Repay_Date.setVisible(val);
    ////        rdoDP_YES.setVisible(!val);
    ////        rdoDP_NO.setVisible(!val);
    //        //        lblDPLimit.setVisible(!val);                                                                                                                                            private void txtToAmtFocusLost(){
    //        // To check whether the From amount is less than the To amount
    //        updateFromToAmountOB();
    ////        if (!txtFromAmt.getText().equals("")){
    ////            if (CommonUtil.convertObjToInt(txtFromAmt.getText()) > CommonUtil.convertObjToInt(txtToAmt.getText())){
    ////                observableInt.setTxtToAmt("");
    ////            }
    ////        }
    //        updateFromToAmount();
    //    }
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
//    private void calculateSanctionToDate(){
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
    //                ////System.out.println("Date##"+date);
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
//    }
    private void calculateSanctionToDateAndDepository() {
        if (!(viewType.equals(AUTHORIZE) || viewType.equals("Edit") || viewType.equals(EXCEPTION) || viewType.equals(REJECT) || viewType.equals("Delete"))) {
            if (!CommonUtil.convertObjToStr(tdtAccountOpenDate.getDateValue()).equals("") && !CommonUtil.convertObjToStr(cboRepayFreq.getSelectedItem()).equals("") && !txtNoInstallments.getText().equals("")) {
                java.util.GregorianCalendar gCalendar = new java.util.GregorianCalendar();
                java.util.GregorianCalendar gCalendarrepaydt = new java.util.GregorianCalendar();//forrepaydate shoude change from first dt
//                        if(CommonUtil.convertObjToStr(tdtFacility_Repay_Date.getDateValue()).equals(""))
//                            tdtFacility_Repay_Date.setDateValue(tdtFDate.getDateValue());
                //gCalendar.setGregorianChange(DateUtil.getDateMMDDYYYY(tdtAccountOpenDate.getDateValue()));
                gCalendar.setTime(DateUtil.getDateMMDDYYYY(tdtAccountOpenDate.getDateValue()));
                gCalendar.setTime(DateUtil.getDateMMDDYYYY(tdtAccountOpenDate.getDateValue()));
                //gCalendarrepaydt.setGregorianChange(DateUtil.getDateMMDDYYYY(tdtAccountOpenDate.getDateValue()));
                gCalendarrepaydt.setTime(DateUtil.getDateMMDDYYYY(tdtAccountOpenDate.getDateValue()));
                int dateVal = getIncrementType();
                int incVal = observable.getInstallNo(txtNoInstallments.getText(), dateVal);
                date = new java.util.Date();
//                        date=DateUtil.getDateMMDDYYYY(tdtFacility_Repay_Date.getDateValue());
                if (txtNoInstallments.getText().equals("1")) {
                    date = DateUtil.getDateMMDDYYYY(tdtAccountOpenDate.getDateValue());
                }
                //System.out.println("Date##" + incVal);
                //System.out.println("dateVal##" + dateVal);
                
                if (cboRepayFreq.getSelectedItem().equals("USER_DEFINED_GOLD_LOAN") && observable.getMaximumDaysForLoan() > 0) {//Added by nithya on 06-07-2019 for KD 546 - New Gold Loan -45 days maturity type
                    dateVal = observable.getMaximumDaysForLoan();
                    gCalendar.add(gCalendar.DATE, dateVal);                    
                } else {
                    if (dateVal <= 7) {
                        gCalendar.add(gCalendar.DATE, incVal);
                    } else if (dateVal > 7 && dateVal < 30) {
                        gCalendar.add(gCalendar.DATE, dateVal);
                    } else if (dateVal >= 30) {
                        //System.out.println("inside else dateVal ::  " + dateVal);
                        gCalendar.add(gCalendar.MONTH, incVal);
                        int firstInstall = dateVal / 30;
                        gCalendarrepaydt.add(gCalendarrepaydt.MONTH, firstInstall);//for repaydate                  
                    }
                }
                //System.out.println("gCalendar.getTime()$@@$@$"+gCalendar.getTime());
                tdtDemandPromNoteExpDate.setDateValue(DateUtil.getStringDate(gCalendar.getTime()));

                observable.setTdtDemandPromNoteExpDate(tdtDemandPromNoteExpDate.getDateValue());
//                       tdtFacility_Repay_Date.setDateValue(DateUtil.getStringDate(gCalendarrepaydt.getTime()));
//                      observable.setTdtFacility_Repay_Date(tdtFacility_Repay_Date.getDateValue()); //for repaydate
                gCalendar = null;
                gCalendarrepaydt = null;
            } else {
//                       tdtTDate.setDateValue("");
//                        observable.setTdtTDate("");
//                        tdtFacility_Repay_Date.setDateValue("");
//                       observable.setTdtFacility_Repay_Date("");
//                       observable.setTxtPeriodDifference_Days("");
//                        observable.setTxtPeriodDifference_Months("");
//                       observable.setTxtPeriodDifference_Years("");
            }
        }
    }

    private void calculateSanctionToDateAndDepositoryForRenewal() {
        //System.out.println("observable.isRenewalYesNo()"+observable.isRenewalYesNo());
        if (observable.isRenewalYesNo()) {            
            if (!CommonUtil.convertObjToStr(tdtRenewalAccountOpenDate.getDateValue()).equals("") && !CommonUtil.convertObjToStr(cboRenewalRepayFreq.getSelectedItem()).equals("") && !CommonUtil.convertObjToStr(txtRenewalNoInstallments.getText()).equals("")) {
                java.util.GregorianCalendar gCalendar = new java.util.GregorianCalendar();
                java.util.GregorianCalendar gCalendarrepaydt = new java.util.GregorianCalendar(); //forrepaydate shoude change from first dt
//                        if(CommonUtil.convertObjToStr(tdtFacility_Repay_Date.getDateValue()).equals(""))
//                            tdtFacility_Repay_Date.setDateValue(tdtFDate.getDateValue());
                //gCalendar.setGregorianChange(DateUtil.getDateMMDDYYYY(tdtRenewalAccountOpenDate.getDateValue()));
                gCalendar.setTime(DateUtil.getDateMMDDYYYY(tdtRenewalAccountOpenDate.getDateValue()));
                //gCalendarrepaydt.setGregorianChange(DateUtil.getDateMMDDYYYY(tdtRenewalAccountOpenDate.getDateValue()));
                gCalendarrepaydt.setTime(DateUtil.getDateMMDDYYYY(tdtRenewalAccountOpenDate.getDateValue()));
                int dateVal = getRenewalIncrementType();
                //System.out.println("dateVal"+dateVal);
                int incVal = observable.getInstallNo(txtRenewalNoInstallments.getText(), dateVal);
                date = new java.util.Date();
//                        date=DateUtil.getDateMMDDYYYY(tdtFacility_Repay_Date.getDateValue());
                if (txtNoInstallments.getText().equals("1")) {
                    date = DateUtil.getDateMMDDYYYY(tdtRenewalAccountOpenDate.getDateValue());
                }
                ////System.out.println("Date##" + date);                
                HashMap marginMap = new HashMap();
                LinkedHashMap whereMap = new LinkedHashMap();
                marginMap.put("PROD_ID", ((ComboBoxModel) cboRnwGoldLoanProd.getModel()).getKeyForSelected().toString());                        
                List lst = ClientUtil.executeQuery("getSelectMarginPercentage", marginMap);
                if (lst != null && lst.size() > 0) {
                    marginMap = (HashMap) lst.get(0);
                    observable.setMaximumDaysForRenewLoan(CommonUtil.convertObjToInt(marginMap.get("MAX_PERIOD")));
                }
                if (cboRenewalRepayFreq.getSelectedItem().equals("USER_DEFINED_GOLD_LOAN") && observable.getMaximumDaysForRenewLoan() > 0) {//Added by nithya on 06-07-2019 for KD 546 - New Gold Loan -45 days maturity type
                    dateVal = observable.getMaximumDaysForRenewLoan();
                    gCalendar.add(gCalendar.DATE, dateVal);
                } else {
                    if (dateVal <= 7) {
                        gCalendar.add(gCalendar.DATE, incVal);
                    } else if (dateVal > 7 && dateVal < 30) {
                        gCalendar.add(gCalendar.DATE, dateVal);
                    } else if (dateVal >= 30) {
                        gCalendar.add(gCalendar.MONTH, incVal);
                        int firstInstall = dateVal / 30;
                        gCalendarrepaydt.add(gCalendarrepaydt.MONTH, firstInstall);//for repaydate
                    }
                }
                //System.out.println("DateUtil.getStringDate(gCalendar.getTime())"+DateUtil.getStringDate(gCalendar.getTime()));
                tdtRenewalDemandPromNoteExpDate.setDateValue(DateUtil.getStringDate(gCalendar.getTime()));
                observable.setTdtRenewalToDate(DateUtil.getStringDate(gCalendar.getTime()));//Added by nithya on 06-07-2019 for KD 546 - New Gold Loan -45 days maturity type

                observable.setTdtRenewalDemandPromNoteExpDate(tdtRenewalDemandPromNoteExpDate.getDateValue());
//                       tdtFacility_Repay_Date.setDateValue(DateUtil.getStringDate(gCalendarrepaydt.getTime()));
//                      observable.setTdtFacility_Repay_Date(tdtFacility_Repay_Date.getDateValue()); //for repaydate
                gCalendar = null;
                gCalendarrepaydt = null;
            } else {
//                       tdtTDate.setDateValue("");
//                        observable.setTdtTDate("");
//                        tdtFacility_Repay_Date.setDateValue("");
//                       observable.setTdtFacility_Repay_Date("");
//                       observable.setTxtPeriodDifference_Days("");
//                        observable.setTxtPeriodDifference_Months("");
//                       observable.setTxtPeriodDifference_Years("");
            }
        }
    }

    public int getRenewalIncrementType() {
        int incType = 0;
        try {
//            Double incVal = CommonUtil.convertObjToDouble(cbmRepayFreq.getKeyForSelected());//gold loan they are using observableRepay.getCbmRepayFreq_Repayment()
            Double incVal = CommonUtil.convertObjToDouble(((ComboBoxModel) cboRenewalRepayFreq.getModel()).getKeyForSelected());
            incType = incVal.intValue();
            incVal = null;
        } catch (Exception e) {
            log.info("Exception caught in getIncrementType: " + e);

        }
        return incType;
    }
    
    public int getIncrementType() {
        int incType = 0;
        try {
//            Double incVal = CommonUtil.convertObjToDouble(cbmRepayFreq.getKeyForSelected());//gold loan they are using observableRepay.getCbmRepayFreq_Repayment()
            Double incVal = CommonUtil.convertObjToDouble(((ComboBoxModel) cboRepayFreq.getModel()).getKeyForSelected());
            incType = incVal.intValue();
            incVal = null;
        } catch (Exception e) {
            log.info("Exception caught in getIncrementType: " + e);

        }
        return incType;
    }

    private void populatePeriodDifference() {
        //        if (!tdtFDate.getDateValue().equals("") && !tdtTDate.getDateValue().equals("") && !tdtFacility_Repay_Date.getDateValue().equals("") && !cboRepayFreq.getSelectedItem().equals("") && !txtNoInstallments.getText().equals("")){
        //            if (CommonUtil.convertObjToStr(((ComboBoxModel)cboRepayFreq.getModel()).getKeyForSelected()).equals("0")){
        //                observable.populatePeriodDifference(tdtFDate.getDateValue(), tdtTDate.getDateValue());
        //            }else{
        //
        //            }
        //        }else{
        //            observable.setTxtPeriodDifference_Days("");
        //            observable.setTxtPeriodDifference_Months("");
        //            observable.setTxtPeriodDifference_Years("");
        //        }
    }

    private void tdtDemandPromNoteExpDateFocusLost() {
        // To check whether this To date is greater than this details From date
        ClientUtil.validateToDate(tdtDemandPromNoteExpDate, tdtDemandPromNoteDate.getDateValue());
    }

    private void tdtDemandPromNoteDateFocusLost() {
        // To check whether this From date is less than this details To date
        ClientUtil.validateFromDate(tdtDemandPromNoteDate, tdtDemandPromNoteExpDate.getDateValue());
        if (observable.getStrACNumber().equals("")) {
            calculateDPNExpDate();
        }
    }
    //old method

    private void calculateDPNExpDate_oldmethod() {
        if (tdtDemandPromNoteDate.getDateValue().length() > 0) {
            java.util.GregorianCalendar gCalendar = new java.util.GregorianCalendar();
            gCalendar.setGregorianChange(DateUtil.getDateMMDDYYYY(tdtDemandPromNoteDate.getDateValue()));
            gCalendar.setTime(DateUtil.getDateMMDDYYYY(tdtDemandPromNoteDate.getDateValue()));
            gCalendar.add(gCalendar.YEAR, 3);
            tdtDemandPromNoteExpDate.setDateValue(DateUtil.getStringDate(DateUtil.addDays(gCalendar.getTime(), -1)));
            observable.setTdtDemandPromNoteExpDate(CommonUtil.convertObjToStr(tdtDemandPromNoteExpDate.getDateValue()));
            gCalendar = null;
        }
    }

    //new dpExpdate calcailation old dp calculation above
    private void calculateDPNExpDate() {
        if (tdtDemandPromNoteDate.getDateValue().length() > 0) {
            calculateSanctionToDateAndDepository();
//            java.util.GregorianCalendar gCalendar = new java.util.GregorianCalendar();
//            gCalendar.setGregorianChange(DateUtil.getDateMMDDYYYY(tdtDemandPromNoteDate.getDateValue()));
//            gCalendar.setTime(DateUtil.getDateMMDDYYYY(tdtDemandPromNoteDate.getDateValue()));
//            
//            gCalendar.add(gCalendar.YEAR, 3);
//            tdtDemandPromNoteExpDate.setDateValue(DateUtil.getStringDate(DateUtil.addDays(gCalendar.getTime(), -1)));
//            observable.setTdtDemandPromNoteExpDate(CommonUtil.convertObjToStr(tdtDemandPromNoteExpDate.getDateValue()));
//            gCalendar = null;
        }
    }

    private void tdtAsOn_GDFocusLost() {
        // To check the entered date is less than or equal to current date
        //        ClientUtil.validateLTDate(tdtAsOn_GD);
    }

    private void btnDeleteBorrowerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteBorrowerActionPerformed
        // Add your handling code here:
        if (poaUI.checkCustIDExistInJointAcctAndPoA(CommonUtil.convertObjToStr(tblBorrowerTabCTable.getValueAt(tblBorrowerTabCTable.getSelectedRow(), 1)))) {
            btnDeleteBorrowerActionPerformed();
        }
    }//GEN-LAST:event_btnDeleteBorrowerActionPerformed
    private void btnDeleteBorrowerActionPerformed() {
        updateOBFields();
        setBorrowerNewOnlyEnable();
        String strCustIDToDel = CommonUtil.convertObjToStr(tblBorrowerTabCTable.getValueAt(tblBorrowerTabCTable.getSelectedRow(), 1));
        observableBorrow.deleteJointAccntHolder(strCustIDToDel, tblBorrowerTabCTable.getSelectedRow());
        observableBorrow.resetBorrowerTabCustomer();
        //        authSignUI.removeAcctLevelCustomer(strCustIDToDel);
        strCustIDToDel = null;
        observable.ttNotifyObservers();
    }
    private void btnDelete_BorrowerMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDelete_BorrowerMousePressed
        // Add your handling code here:
    }//GEN-LAST:event_btnDelete_BorrowerMousePressed

    private void btnToMain_BorrowerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToMain_BorrowerActionPerformed
        // Add your handling code here:
        btnToMain_BorrowerActionPerformed();
    }//GEN-LAST:event_btnToMain_BorrowerActionPerformed
    private void btnToMain_BorrowerActionPerformed() {
        updateOBFields();
        setBorrowerNewOnlyEnable();
//        observableComp.resetCustomerDetails();
        observableBorrow.moveToMain(CommonUtil.convertObjToStr(tblBorrowerTabCTable.getValueAt(0, 1)), CommonUtil.convertObjToStr(tblBorrowerTabCTable.getValueAt(tblBorrowerTabCTable.getSelectedRow(), 1)), tblBorrowerTabCTable.getSelectedRow());
        observable.ttNotifyObservers();
    }
//GEN-FIRST:event_btnDelete_BorrowerActionPerformed
//GEN-LAST:event_btnDelete_BorrowerActionPerformed
                                                                                                                                                                private void btnNew_BorrowerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNew_BorrowerActionPerformed
                                                                                                                                                                    // Add your handling code here:
                                                                                                                                                                    if (tblBorrowerTabCTable.getRowCount() != 0) {
                                                                                                                                                                        // If the Main Accnt Holder is selected,
                                                                                                                                                                        callView("JOINT ACCOUNT");
                                                                                                                                                                        // Allow the user to add Jnt Acct Holder
                                                                                                                                                                    } else {
                                                                                                                                                                        // Else if the Main Acct Holder is not selected, prompt the user to select
                                                                                                                                                                        // the Main Acct. holder
                                                                                                                                                                        observableBorrow.mainCustDoesntExistWarn();
                                                                                                                                                                        btnCustID.requestFocus(true);
                                                                                                                                                                    }
    }//GEN-LAST:event_btnNew_BorrowerActionPerformed

    private void tblBorrowerTabCTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBorrowerTabCTableMousePressed
        // Add your handling code here:
        //        observableBorrow.btnPressed = true;
        //        observableBorrow.resetBorrowerTabCustomer();
        if ((observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) || (viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT))) {
            // Don't do anything if the record is in Delete or Authroization mode
            setAllBorrowerBtnsEnableDisable(false);
            setBorrowerDetailsEnableDisable(false);
            HashMap cust = new HashMap();
            cust.put("CUST_ID", tblBorrowerTabCTable.getValueAt(tblBorrowerTabCTable.getSelectedRow(), 1));
            observableBorrow.populateBorrowerTabCustomerDetails(cust, true, loanType);
            updateBorrowerTabCustDetails();
            cust = null;
            if (tblBorrowerTabCTable.getSelectedRowCount() > 0 && evt.getClickCount() == 2) {
                new CustomerDetailsScreenUI(CommonUtil.convertObjToStr(tblBorrowerTabCTable.getValueAt(tblBorrowerTabCTable.getSelectedRow(), 1))).show();
            }
        } else {
            if (tblBorrowerTabCTable.getSelectedRowCount() > 0 && evt.getClickCount() == 2) {
                new CustomerDetailsScreenUI(CommonUtil.convertObjToStr(tblBorrowerTabCTable.getValueAt(tblBorrowerTabCTable.getSelectedRow(), 1))).show();
            }
            tblBorrowerTabCTableMousePressed(tblBorrowerTabCTable.getSelectedRow());
        }
    }//GEN-LAST:event_tblBorrowerTabCTableMousePressed
    private void tblBorrowerTabCTableMousePressed(int rowSelected) {
        if (tblBorrowerTabCTable.getSelectedRow() != 0) {
            if ((((ComboBoxModel) cboConstitution.getModel()).getKeyForSelected()).equals(JOINT_ACCOUNT)) {
                setAllBorrowerBtnsEnableDisable(true);
            } else {
                setAllBorrowerBtnsEnableDisable(false);
            }
        } else {
            if ((((ComboBoxModel) cboConstitution.getModel()).getKeyForSelected()).equals(JOINT_ACCOUNT)) {
                setBorrowerNewOnlyEnable();
            } else {
                setAllBorrowerBtnsEnableDisable(false);
            }
        }
        HashMap cust = new HashMap();
        cust.put("CUST_ID", tblBorrowerTabCTable.getValueAt(tblBorrowerTabCTable.getSelectedRow(), 1));
        observableBorrow.populateBorrowerTabCustomerDetails(cust, true, loanType);
        updateBorrowerTabCustDetails();
        cust = null;
    }
    private void cboConstitutionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboConstitutionActionPerformed
        // Add your handling code here:
        observableBorrow.setCboConstitution(CommonUtil.convertObjToStr(cboConstitution.getSelectedItem()));
        if (observableBorrow.getCboConstitution().length() > 0) {
            cboConstitutionActionPerformed();
            validateConstitutionCustID();
        }
    }//GEN-LAST:event_cboConstitutionActionPerformed
    private void cboConstitutionActionPerformed() {
        if (observableBorrow.getCbmConstitution().getKeyForSelected().equals(JOINT_ACCOUNT)) {
            setBorrowerNewOnlyEnable();
        } else {
            // To delete all the Customer(Excluding Main) Records
            // when Constitution is not Joint Account
            checkJointAccntHolderForData();
        }
    }

    private String isJointAcctHavingAtleastOneCust() {
        StringBuffer stbWarnMsg = new StringBuffer("");
        if (observableBorrow.getCbmConstitution().getKeyForSelected().equals(JOINT_ACCOUNT) && tblBorrowerTabCTable.getRowCount() <= 1) {
            stbWarnMsg.append("\n");
            stbWarnMsg.append(resourceBundle.getString("jointAcctDontHaveProperCustDetailsWarning"));
        }
        return stbWarnMsg.toString();
    }

    private void addCustIDNAuthSignatory() {
        //        int borrowerTabRowCount = tblBorrowerTabCTable.getRowCount();
        //        for (int i = borrowerTabRowCount - 1,j = 0;i >= 0;--i,++j){
        //            authSignUI.addAcctLevelCustomer(CommonUtil.convertObjToStr(tblBorrowerTabCTable.getValueAt(j, 1)));
        //        }
    }

    private void removedJointAcctCustIDNAuthSignatory() {
        int borrowerTabRowCount = tblBorrowerTabCTable.getRowCount();
        for (int i = borrowerTabRowCount - 1, j = 1; i >= 1; --i, ++j) {
            //            authSignUI.removeAcctLevelCustomer(CommonUtil.convertObjToStr(tblBorrowerTabCTable.getValueAt(j, 1)));
        }
    }

    private void checkJointAccntHolderForData() {
        if (tblBorrowerTabCTable.getRowCount() > 1) {
            int reset = observableBorrow.jointAcctWarn();
            if (reset == 0) {
                removedJointAcctCustIDNAuthSignatory();
                observableBorrow.resetBorrowerTabCTable();
                custInfoDisplay(txtCustID.getText(), loanType);
                poaUI.resetPoACustID(txtCustID.getText());
                observableBorrow.resetBorrowerTabCustomer();
                updateBorrowerTabCustDetails();
                setAllBorrowerBtnsEnableDisable(false);
                poaUI.setCboPoACustModel();
            } else if (reset == 1) {
                observableBorrow.setCboConstitution(CommonUtil.convertObjToStr(observableBorrow.getCbmConstitution().getDataForKey(JOINT_ACCOUNT)));
                cboConstitution.setSelectedItem(observableBorrow.getCboConstitution());
                //                observableBorrow.getCbmConstitution().setKeyForSelected(observableBorrow.getCbmConstitution().getDataForKey(JOINT_ACCOUNT));
            }
        } else {
            setAllBorrowerBtnsEnableDisable(false);
        }
    }

    public void callView(String currField) {
        viewType = currField;
        //        authSignUI.setViewType(viewType);
        //        poaUI.setViewType(viewType);
        // If Customer Id is selected OR JointAccnt New is clciked, show the popup Screen of Customer Table
        if ((currField.equals("CUSTOMER ID")) || (currField.equals("JOINT ACCOUNT"))) {
            HashMap viewMap = new HashMap();
            StringBuffer presentCust = new StringBuffer();
            int jntAccntTablRow = tblBorrowerTabCTable.getRowCount();
            if (tblBorrowerTabCTable.getRowCount() != 0) {
                for (int i = 0, sizeJointAcctAll = tblBorrowerTabCTable.getRowCount(); i < sizeJointAcctAll; i++) {
                    if (i == 0 || i == sizeJointAcctAll) {
                        presentCust.append("'" + CommonUtil.convertObjToStr(tblBorrowerTabCTable.getValueAt(i, 1)) + "'");
                    } else {
                        presentCust.append("," + "'" + CommonUtil.convertObjToStr(tblBorrowerTabCTable.getValueAt(i, 1)) + "'");
                    }
                }
            }
            if (loanType.equals("LTD")) {
                viewMap.put("MAPNAME", "getSelectCustListForLTD");
            } else {
                viewMap.put("MAPNAME", "getSelectLoanAccInfoList");
            }
            HashMap whereMap = new HashMap();
            whereMap.put("CUSTOMER_ID", presentCust);
            //            String strSelectedProdType = CommonUtil.convertObjToStr(((ComboBoxModel)cboProdType.getModel()).getKeyForSelected());
            //            if (!strSelectedProdType.equals(LOANS_AGAINST_DEPOSITS)){
            //                // If the Product is not Loan against Term Deposit
            //                // then the customer should be a share holder
            //                // else the customer should be Term Deposit Holder
            //                whereMap.put(IS_COOPERATIVE, CommonConstants.IS_COOPERATIVE);
            //            }
            whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            whereMap.put("CURR_DT", (Date) curr_dt.clone());
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, viewMap).show();
            presentCust = null;
        } else if (currField.equals("SECURITY_CUSTOMER ID")) {
            HashMap viewMap = new HashMap();
            viewMap.put("MAPNAME", "getSelectCustSecurityTOList");
            HashMap whereMap = new HashMap();
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, viewMap).show();
        } else if (currField.equals("DISBURSEMENT_DETAILS")) {
            HashMap viewMap = new HashMap();
            viewMap.put("MAPNAME", "getDisbursementDetails");
            HashMap whereMap = new HashMap();
            whereMap.put("ACT_NUM", observable.getStrACNumber());
            HashMap statusMap = observableRepay.getActiveAndInActiveScheduleNo();
            if (statusMap.containsKey("INACTIVE_NO")) {
                whereMap.put("REPAYMENT_SCHEDULE_NO", statusMap.get("INACTIVE_NO"));
            }
            if (statusMap.containsKey("ACTIVE_NO")) {
                whereMap.put("DISBURSEMENT_ID", statusMap.get("ACTIVE_NO"));
            }
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, viewMap).show();
        } else if (currField.equals("DEPOSIT_CUSTOMER")) {
            HashMap viewMap = new HashMap();
            viewMap.put("MAPNAME", "getSelectDepositCustListForLTD");
            HashMap whereMap = new HashMap();
            Date currDt = (Date) curr_dt.clone();
            Date sancDt = DateUtil.getDateMMDDYYYY(tdtSanctionDate.getDateValue());
            currDt.setDate(sancDt.getDate());
            currDt.setMonth(sancDt.getMonth());
            currDt.setYear(sancDt.getYear());
            whereMap.put("CURR_DT", currDt);
            whereMap.put("LOAN_PROD_ID", CommonUtil.convertObjToStr(observable.getCbmProductId().getKeyForSelected()));
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, viewMap).show();
        } else if (currField.equals("EXISTING_CUSTOMER") && txtAccountNo.getText().length() > 0) {
            HashMap existingMap = new HashMap();
            existingMap.put("ACT_NUM", txtAccountNo.getText());
            List mapDataList = ClientUtil.executeQuery("getSelectExistingCustId", existingMap);
            ////System.out.println("#### mapDataList :" + mapDataList);
            if (mapDataList != null && mapDataList.size() > 0) {
                existingMap = (HashMap) mapDataList.get(0);
                existingMap.put("ACT_NUM", txtAccountNo.getText());
                fillData(existingMap);
            } else {
                ClientUtil.showAlertWindow("Invalid Member No / Account No");
                txtAccountNo.setText("");
                return;
            }
        } else if (currField.equals("APPRAISER_ID")) {
            HashMap viewMap = new HashMap();
            viewMap.put("MAPNAME", "getSelectAppraiserId");
            //            HashMap whereMap = new HashMap();
            //            Date currDt = (Date)curr_dt.clone();
            //            Date sancDt = DateUtil.getDateMMDDYYYY(tdtSanctionDate.getDateValue());
            //            currDt.setDate(sancDt.getDate());
            //            currDt.setMonth(sancDt.getMonth());
            //            currDt.setYear(sancDt.getYear());
            //            whereMap.put("CURR_DT",currDt);
            //            whereMap.put("LOAN_PROD_ID",CommonUtil.convertObjToStr(observable.getCbmProductId().getKeyForSelected()));
            //            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, viewMap).show();
        }
    }

    private void tdtToFocusLost() {
        // The to date should not be later than the sanction to date for the facility
        //        if (!tdtTo.getDateValue().equals(tdtTDate.getDateValue())){
        //            // To check whether this To date is fall within Facility details To date
        //            ClientUtil.validateFromDate(tdtTo, tdtTDate.getDateValue());
        //        }
        //        // To check whether this To date is fall within Facility details From date
        //        ClientUtil.validateToDate(tdtTo, tdtFDate.getDateValue());
        //        // Check for the last interest payment date
        //        if (tblInterMaintenance.getRowCount() > 0){
        //            if (updateInterest && tblInterMaintenance.getSelectedRow() > 0){
        //                ClientUtil.validateToDate(tdtTo, observableInt.checkLastInterestDate(tblInterMaintenance.getSelectedRow()));
        //            }else if (!updateInterest){
        //                ClientUtil.validateToDate(tdtTo, observableInt.checkLastInterestDate(tblInterMaintenance.getRowCount()));
        //            }
        //        }
        // To check whether this To date is greater than this details From date
        //        ClientUtil.validateToDate(tdtTo, tdtFrom.getDateValue());
        //         if(cboIntGetFrom.getSelectedItem().equals("Account")){
        //             tdtTo.setDateValue("");
        //         }
    }

    private void tdtFromFocusLost() {
        // The from date should not be earlier than the sanction from date for the facility
        // To check whether this From date is fall within Facility details To date
        //        ClientUtil.validateFromDate(tdtFrom, tdtTDate.getDateValue());
        // To check whether this From date is fall within Facility details From date
        //        ClientUtil.validateToDate(tdtFrom, tdtFDate.getDateValue());
        // Check for the last interest payment date
        //        if ((!tdtFrom.getDateValue().equals(tdtFDate.getDateValue())) && (tblInterMaintenance.getSelectedRow() == 0 || tblInterMaintenance.getRowCount() == 0)){
        //            ClientUtil.validateFromDate(tdtFrom, tdtFDate.getDateValue());
        //        }
        //        if (tblInterMaintenance.getRowCount() > 0){
        //            if (updateInterest && tblInterMaintenance.getSelectedRow() > 0){
        //                ClientUtil.validateToDate(tdtFrom, observableInt.checkLastInterestDate(tblInterMaintenance.getSelectedRow()));
        //            }else if (!updateInterest){
        //                ClientUtil.validateToDate(tdtFrom, observableInt.checkLastInterestDate(tblInterMaintenance.getRowCount()));
        //            }
        //        }
        //        ClientUtil.validateFromDate(tdtFrom, tdtTo.getDateValue());
    }

    private String getSanctionFrequency() {
        String sanFreq = CommonUtil.convertObjToStr(((ComboBoxModel) cboRepayFreq.getModel()).getKeyForSelected());
        if (sanFreq.equals("365")) {
            return "EYI";
        }
        if (sanFreq.equals("30")) {
            return "EMI";
        }
        if (sanFreq.equals("180")) {
            return "EHI";
        }
        if (sanFreq.equals("90")) {
            return "EQI";
        }
        return "";
    }

    private void btninterestSaveActionPerformed() {
        updateOBFields();
        if (observableInt.addInterestDetails(rowInterest, updateInterest) == 1) {
            // Donot reset the fields when return value is 1(Option is No for replacing the existing record)
        } else {
            // To reset the Fields
            setAllInterestDetailsEnableDisable(false);
            setInterestDetailsOnlyNewEnabled();
            updateInterest = false;
            observableInt.slabBasedEnableDisable(false);
            observableInt.resetInterestDetails();
        }
        observable.ttNotifyObservers();
    }

    private void btninterestNewActionPerformed() {
        //    updateOBFields();
        //    observableInt.resetInterestDetails();
        //    rowInterest = -1;
        //    updateInterest = false;
        //    //        setAllInterestDetailsEnableDisable(true);
        //    setAllInterestDetailsEnableDisableBased(true);
        //    observableInt.setTxtPenalInter(observableInt.getPenalInter());
        //    if (observableInt.getEnableInterExpLimit()){
        //        txtInterExpLimit.setEnabled(true);
        //    }else{
        //        txtInterExpLimit.setEnabled(false);
        //        observableInt.setTxtInterExpLimit("");
        //    }
        //    setInterestDefaultValues();
        //    if(observableInt.setNextValue()) {
        //        txtFromAmt.setEnabled(false);
        //        tdtFrom.setEnabled(true);
        //    }else{
        //        txtFromAmt.setEnabled(false);
        //        tdtFrom.setEnabled(false);
        //    }
        //    observable.ttNotifyObservers();
        //    setInterestDetailsOnlyDeleteDisabled();
        //    if(((EnhancedTableModel)observableInt.getTblInterestTab()).getRowCount()==0) {
        //        tdtFrom.setEnabled(false);
        //        tdtFrom.setDateValue(tdtSanctionDate.getDateValue());
        //    }
        //    observableInt.setIntAuthStatusMap(new HashMap());
        //    txtToAmt.requestFocus();
    }

    private void setAllInterestDetailsEnableDisableBased(boolean val) {
        //    tdtFrom.setEnabled(val);
        //    tdtTo.setEnabled(val);
        //    txtFromAmt.setEnabled(val);
        //    txtFromAmt.setEditable(val);
        //    txtToAmt.setEnabled(val);
        //    txtToAmt.setEditable(val);
        //    txtInter.setEnabled(val);
        //    txtInter.setEditable(val);
        //    txtPenalInter.setEnabled(val);
        //    txtPenalInter.setEditable(val);
        //    txtAgainstClearingInter.setEnabled(val);
        //    txtAgainstClearingInter.setEditable(val);
        //    txtPenalStatement.setEnabled(val);
        //    txtPenalStatement.setEditable(val);
        //    txtInterExpLimit.setEnabled(val);
        //    txtInterExpLimit.setEditable(val);
        //    //        ClientUtil.enableDisable(panTableFields, val);
        //    tdtTo.setEnabled(false);
    }

    private void setInterestDefaultValues() {
        //        if (tblInterMaintenance.getRowCount() > 0){
        //            tdtFrom.setEnabled(false);
        //            observableInt.populateFromDate();
        //        }
    }

    private void tblinterestDetailsMousePressed() {
        //    if (tblInterMaintenance.getSelectedRow() >= 0){
        //        updateOBFields();
        //        // If the table is in editable mode
        //        setAllInterestDetailsEnableDisable(false);
        //        setAllInterestBtnsEnableDisable(false);
        //        observableInt.populateInterestDetails(tblInterMaintenance.getSelectedRow());
        //        if (((observable.getCbmIntGetFrom().getKeyForSelected().equals(PROD)) ||
        //        (observable.getCbmIntGetFrom().getKeyForSelected().equals("")) ||
        //        (observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) ||
        //        (viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT) || viewType.equals("Enquirystatus"))
        //        || loanType.equals("LTD") || observable.getCboAccStatus().equals("Closed"))){
        //            // If the interest is from Product level or nothing selected
        //            // If the record is populated for Delete or Authorization
        //            setAllInterestDetailsEnableDisable(false);
        //            setAllInterestBtnsEnableDisable(false);
        //            observable.ttNotifyObservers();
        //        }else{
        //            setAllInterestDetailsEnableDisable(true);
        //            if(observableInt.getIntAuthStatusMap() !=null &&
        //            observableInt.getIntAuthStatusMap().get("AUTHORIZE_STATUS") !=null){
        //                setAllInterestDetailsEnableDisable(false);
        //                setInterestDetailsOnlyNewEnabled();
        //            }
        //            txtFromAmt.setEnabled(false);
        //            if (observableInt.getEnableInterExpLimit()){
        //                txtInterExpLimit.setEnabled(true);
        //            }else{
        //                txtInterExpLimit.setEnabled(false);
        //                observableInt.setTxtInterExpLimit("");
        //            }
        //            //                if (tblInterMaintenance.getSelectedRow() > 0){
        //            //                    tdtFrom.setEnabled(false);
        //            //                }
        //            //                if (!((observable.getCbmIntGetFrom().getKeyForSelected().equals(PROD)) || (observable.getCbmIntGetFrom().getKeyForSelected().equals(""))) && tblInterMaintenance.getSelectedRow() == (tblInterMaintenance.getRowCount()-1)){
        //            //                    tdtTo.setEnabled(true);
        //            //                }else{
        //            //                    tdtTo.setEnabled(false);
        //            //                }
        //            updateInterest = true;
        //            if(observableInt.getIntAuthStatusMap() !=null &&
        //            observableInt.getIntAuthStatusMap().get("AUTHORIZE_STATUS") !=null){
        //                setAllInterestDetailsEnableDisable(false);
        //                setInterestDetailsOnlyNewEnabled();
        //                updateInterest = false;
        //            } else if (enableControls) {
        //                setAllInterestBtnsEnableDisable(true);
        //                setAllInterestDetailsEnableDisable(true);
        //                if (observableInt.getEnableInterExpLimit()){
        //                    txtInterExpLimit.setEnabled(true);
        //                }else{
        //                    txtInterExpLimit.setEnabled(false);
        //                    observableInt.setTxtInterExpLimit("");
        //                }
        //            }
        //            txtFromAmt.setEnabled(false);
        //            observable.ttNotifyObservers();
        //        }
        //        rowInterest = tblInterMaintenance.getSelectedRow();
        //    }
    }    // Actions have to be taken when a record from Guarantor Details have been chosen

//    private void btnguarantorDeleteActionPerformed(){
//        updateOBFields();
//        observableGuarantor.deleteGuarantorTabRecord(rowGuarantor);
//        setGuarantorDetailsNewOnlyEnabled();
//        rowGuarantor = -1;
//        updateGuarantor = false;
//        observableGuarantor.resetGuarantorDetails();
//        observableGuarantor.resetInstitGuarantorDetails();
//        observable.ttNotifyObservers();
//    }
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
        authEnableDisable();
        authorizeActionPerformed(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    private void resetUiSecurityDetails() {
        //        lblSLNoValue.setText("");
        //        tdtAson.setDateValue("");
        txtGrossWeight.setText("");
        //        txtNetWeight.setText("");
        txtSecurityValue.setText("");
        cboPurityOfGold.setSelectedItem("");
        txtMarketRate.setText("");
        txtAreaParticular.setText("");
        nomineeUi.setActionType(AUTHORIZED);
        nomineeUi.disableNewButton(false);
    }
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authEnableDisable();
        authorizeActionPerformed(CommonConstants.STATUS_REJECTED);
//        btnAppraiserId.setEnabled(false);
        nomineeUi.setActionType(AUTHORIZED);
        nomineeUi.disableNewButton(false);

    }//GEN-LAST:event_btnRejectActionPerformed
    public void authorizeStatus(String auth) {
        authorizeActionPerformed(auth);
    }
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        authEnableDisable();
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeActionPerformed(CommonConstants.STATUS_AUTHORIZED);
        nomineeUi.setActionType(AUTHORIZED);
        nomineeUi.disableNewButton(false);
        
//        btnAppraiserId.setEnabled(false);

    }//GEN-LAST:event_btnAuthorizeActionPerformed
    private void authEnableDisable() {
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
        setStockDetailsTabDisable(false); //28-10-2019
        txtCustID.setEnabled(false);
    }

    private void setStockDetailsTabDisable(boolean val) {//28-10-2019
       btnLoad.setEnabled(val);
       btnPhotoRemove.setEnabled(val);
       btnAddPhoto.setEnabled(val);
       btnRenewLoad.setEnabled(val);
       btnRenewPhotoRemove.setEnabled(val);
    }
    
    private boolean tokenValidation(String tokenNo) {
        boolean tokenflag = false;
        HashMap tokenWhereMap = new HashMap();// Separating Serias No and Token No
        char[] chrs = tokenNo.toCharArray();
        StringBuffer seriesNo = new StringBuffer();
        int i = 0;
        for (int j = chrs.length; i < j; i++) {
            if (Character.isDigit(chrs[i])) {
                break;
            } else {
                seriesNo.append(chrs[i]);
            }
        }
        tokenWhereMap.put("SERIES_NO", seriesNo.toString());
        tokenWhereMap.put("TOKEN_NO", CommonUtil.convertObjToInt(tokenNo.substring(i)));
        tokenWhereMap.put("USER_ID", ProxyParameters.USER_ID);
        tokenWhereMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        tokenWhereMap.put("CURRENT_DT", ClientUtil.getCurrentDate());
        List lst = ClientUtil.executeQuery("validateTokenNo", tokenWhereMap);
        if (((Integer) lst.get(0)).intValue() == 0) {
            tokenflag = false;
        } else {
            tokenflag = true;
        }
        return tokenflag;
    }

    // Actions have to be taken when Authorize button is pressed
    private void authorizeActionPerformed(String authorizeStatus) {
        //        tblSanctionDetails.setEnabled(false);
        if ((viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT)) && isFilled) {
            // If a record is populated for authorize
            //Changed By Suresh
//            String isAllTabsVisited = tabLimitAmount.isAllTabsVisited();

            //--- If all the tabs are not visited, then show the Message
//            if(isAllTabsVisited.length()>0){
//                ClientUtil.displayAlert(isAllTabsVisited);
//                return;
//            }else{
            final HashMap singleAuthorizeMap = new HashMap();
            java.util.ArrayList arrList = new java.util.ArrayList();
            HashMap authDataMap = new HashMap();
            if (observable.getStrACNumber().length() > 0) {
                authDataMap.put("ACCT_NUM", observable.getStrACNumber());
            } else {
                authDataMap.put("ACCT_NUM", lblAcctNo_Sanction_Disp.getText());
            }
            if (observable.getRenewalAcctNum().length() > 0) {
                authDataMap.put("RENEWAL_NEW_NO", observable.getLblRenewalAcctNo_Sanction_Disp());
            } else {
                authDataMap.put("RENEWAL_NEW_NO", lblRenewalAcctNo_Sanction_Disp.getText());
            }
            arrList.add(authDataMap);
            //                if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE && observable.getAvailableBalance() >0){
            //                    String [] debitType = {"Cash","Transfer"};
            //                    String[] obj4 = {"Yes","No"};
            //                    int option3 = COptionPane.showOptionDialog(null,("Do you want to make Transaction?"), ("Transaction"),
            //                    COptionPane.YES_NO_CANCEL_OPTION,COptionPane.QUESTION_MESSAGE,null,obj4,obj4[0]);
            //                    if(option3 == 0){
            //                        String transType = (String)COptionPane.showInputDialog(null,"Select Transaction Type", "Transaction type", COptionPane.QUESTION_MESSAGE, null, debitType, "");
            //                        authDataMap.put("TRANSACTION_PART","TRANSACTION_PART");
            //                        authDataMap.put("TRANS_TYPE",transType.toUpperCase());
            //                        authDataMap.put("APPRAISER_AMT",lblAppraiserFeeValue.getText());
            //                        authDataMap.put("SERVICE_TAX_AMT",lblServiceTaxValue.getText());
            //                        if(CommonUtil.convertObjToStr(transType.toUpperCase()).equals("CASH")){
            //                            boolean flag = true;
            //                            do {
            //                                String tokenNo = COptionPane.showInputDialog(this,resourceBundle.getString("REMARK_CASH_TRANS"));
            //                                if (tokenNo != null && tokenNo.length()>0) {
            //                                    flag = tokenValidation(tokenNo);
            //                                    if(flag == false){
            //                                        ClientUtil.showAlertWindow("Token is invalid or not issued for you. Please verify.");
            //                                    }else{
            //                                        authDataMap.put("TOKEN_NO",tokenNo);
            //                                    }
            //                                } else {
            //                                    ClientUtil.showMessageWindow("Transaction Not Created");
            //                                    flag = true;
            //                                    authDataMap.remove("TRANSACTION_PART");
            //                                }
            //                            } while (!flag);
            //                        }else if(CommonUtil.convertObjToStr(transType.toUpperCase()).equals("TRANSFER")){
            //                            boolean flag = true;
            //                            do {
            //                                String sbAcNo = firstEnteredActNo();
            //                                if(sbAcNo!=null && sbAcNo.length()>0){
            //                                    flag = checkingActNo(sbAcNo);
            //                                    if(flag == false && finalChecking == false){
            //                                        ClientUtil.showAlertWindow("Account No is invalid, Please enter correct no");
            //                                    }else{
            //                                        authDataMap.put("CR_ACT_NUM",sbAcNo);
            //                                    }
            //                                    finalChecking = false;
            //                                } else {
            //                                    ClientUtil.showMessageWindow("Transaction Not Created");
            //                                    flag = true;
            //                                    authDataMap.remove("TRANSACTION_PART");
            //                                }
            //                            } while (!flag);
            //                        }
            //                    }
            //                }
            ///OD TODAUTHORIZE
            HashMap todMap = new HashMap();
            HashMap finalMap = new HashMap();
            HashMap finalMap1 = new HashMap();
            todMap.put("ACCT_NUM", observable.getStrACNumber());
            todMap.put("CURR_DT", (Date) curr_dt.clone());
            List lst = ClientUtil.executeQuery("getSelectExistAccountListUI", todMap);
            if (lst != null && lst.size() > 0) {
                ArrayList todauthList = new ArrayList();
                HashMap todExist = (HashMap) lst.get(0);
                todExist.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
                todauthList.add(todExist);
                //                finalMap.put(CommonConstants.AUTHORIZEDATA,todauthList);
                finalMap1.put("AUTHORIZESTATUS", "AUTHORIZED");
                finalMap1.put(CommonConstants.AUTHORIZEDATA, todauthList);
                finalMap1.put("TermLoanUI", "TermLoanUI");
                finalMap.put("AUTHORIZEMAP", finalMap1);
                finalMap.put("ACCT_NUM", observable.getStrACNumber());
                //                finalMap1.put(CommonConstants.AUTHORIZEDATA,todauthList);
                finalMap.put("MODE", null);
                finalMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                finalMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                ////System.out.println("finalMap1$$$$$" + finalMap);
                observable.setAdvanceLiablityMap(finalMap);
            }

            /*validate select screen lock
             *
             */
            if (validateScreenLock()) {
                return;
            }

            //
            singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            if (observable.getRenewalAcctNum().length() > 0) {
                if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
                    HashMap serauthMap = new HashMap();
                    serauthMap.put("ACCT_NUM", observable.getStrACNumber());
                    serauthMap.put("STATUS", authorizeStatus);
                    serauthMap.put("USER_ID", TrueTransactMain.USER_ID);
                    serauthMap.put("AUTHORIZEDT", curr_dt);
                    singleAuthorizeMap.put("SER_TAX_AUTH", serauthMap);
                }
            }
            if (viewType.equals(REJECT)) {
                observable.setPartReject("PARTIALLY_REJECT");
            } else {
                observable.setPartReject("");
            }
            //authorize(singleAuthorizeMap);
            //            ClientUtil.enableDisable(this, false);
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
            txtMarginAmt.setText("");
            txtEligibleLoan.setText("");
            txtCustMobNo.setText("");
            observable.setAuthorizeMap(null);
            cboAppraiserId.setSelectedItem(null);
            lblAppraiserNameValue.setText("");
//            }
        } else {
            // If no record is populated for authorize
            HashMap mapParam = new HashMap();
            HashMap authorizeMapCondition = new HashMap();
            authorizeMapCondition.put("STATUS_BY", TrueTransactMain.USER_ID);
            //authorizeMapCondition.put("BRANCH_ID", getSelectedBranchID());
            authorizeMapCondition.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            //            authorizeMapCondition.put("PROD_ID",eachProdId);
            authorizeMapCondition.put("AUTHORIZE_REMARK", "!= 'GOLD_LOAN'");
            authorizeMapCondition.put("GOLD_LOAN", "GOLD_LOAN");
            authorizeMapCondition.put("TRANS_DT",curr_dt.clone());
            authorizeMapCondition.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, authorizeMapCondition);
            if (loanType.equals("LTD")) {
                mapParam.put(CommonConstants.MAP_NAME, "getSelectTermLoanAuthorizeTOListForLTD");
            } else {
                authorizeMapCondition.put("PROD_ID", eachProdId);
                if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                    mapParam.put(CommonConstants.MAP_NAME, "getSelectTermLoanCashierAuthorizeTOList");
                } else {
                    mapParam.put(CommonConstants.MAP_NAME, "getSelectTermLoanAuthorizeTOList");
                }                
                //mapParam.put(CommonConstants.MAP_NAME, "getSelectTermLoanAuthorizeTOList");
            }
            if (authorizeStatus.equals(CommonConstants.STATUS_AUTHORIZED)) {
                viewType = AUTHORIZE;
            } else if (authorizeStatus.equals(CommonConstants.STATUS_EXCEPTION)) {
                viewType = EXCEPTION;
            } else if (authorizeStatus.equals(CommonConstants.STATUS_REJECTED)) {
                viewType = REJECT;
            }
            //            authSignUI.setViewType(viewType);
            //            poaUI.setViewType(viewType);

            isFilled = false;
            tabLimitAmount.resetVisits();
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSaveDisable();
          // setAuthBtnEnableDisable();
            if (viewType.equals(AUTHORIZE) || viewType.equals(REJECT)) {
                if (viewType.equals(AUTHORIZE)) {
                    this.btnReject.setEnabled(false);
                    this.btnException.setEnabled(false);
                }
            }
            if (viewType.equals(REJECT)) {
                this.btnAuthorize.setEnabled(false);
                this.btnException.setEnabled(false);
            }
            authorizeMapCondition = null;
            //__ If there's no data to be Authorized, call Cancel action...
            if(viewType.equals(REJECT)&&!btnReject.isEnabled()){
                btnReject.setEnabled(true);
            }
            if(viewType.equals(AUTHORIZE)&&!btnAuthorize.isEnabled()){
                btnAuthorize.setEnabled(true);
            }
            setAppraiserName();
            if (!isModified()) {
                setButtonEnableDisable();
                btnCancelActionPerformed(null);
            }
        }
          tabLimitAmount.add(panTranDetView, "Transaction Details");

    }

     private HashMap firstEnteredActNo() {
//        String sbAcNo = COptionPane.showInputDialog(this, resourceBundle.getString("REMARK_TRANSFER_TRANS"));
        HashMap acctDetailsMap = new HashMap();
        String otherBankRequired = "N";
        HashMap transMap =  new HashMap();
        transMap.put("PROD_ID", ((ComboBoxModel) cboGoldLoanProd.getModel()).getKeyForSelected().toString());  
        List transList = ClientUtil.executeQuery("getOtherBankTransactionRequiredForLoan", transMap);
        if (transList != null && transList.size() > 0) {
            transMap = (HashMap) transList.get(0);
            otherBankRequired = CommonUtil.convertObjToStr(transMap.get("INCLUDE_OTHERBANK_TRANS"));
        } 
        if(otherBankRequired.equals("Y")){
          acctsearch = new AcctSearchUI(true);  
        }else{
         acctsearch=new AcctSearchUI();
        }
         acctsearch.show();
         String sbAcNo=acctsearch.getAccountNo();
        String prodType = acctsearch.getProdType();
        System.out.println("prodType" + prodType);
        System.out.println("sbAcNo"+sbAcNo);
        acctDetailsMap.put("ACT_NUM", sbAcNo);
        acctDetailsMap.put("PROD_TYPE", prodType);
        return acctDetailsMap;
     }

//    private String firstEnteredActNo() {
////        String sbAcNo = COptionPane.showInputDialog(this, resourceBundle.getString("REMARK_TRANSFER_TRANS"));
//
//         acctsearch=new AcctSearchUI();
//         acctsearch.show();
//         String sbAcNo=acctsearch.getAccountNo();
//         return sbAcNo;
//     }

    private boolean checkingActNo(String sbAcNo,String prodType) {
        boolean flag = false;
        List mapDataList;
        HashMap existingMap = new HashMap();
        existingMap.put("ACT_NUM", sbAcNo.toUpperCase());
        if(prodType.equals("AB")){
           mapDataList = ClientUtil.executeQuery("getValidateOtherBankAct", existingMap); 
        }else{
           mapDataList = ClientUtil.executeQuery("getAccNoDet", existingMap);
        }
        ////System.out.println("#### mapDataList :" + mapDataList);
        if (mapDataList != null && mapDataList.size() > 0) {
            existingMap = (HashMap) mapDataList.get(0);
            if(existingMap != null && !ProxyParameters.BRANCH_ID.equals(CommonUtil.convertObjToStr(existingMap.get("BRANCH_CODE")))){
                Date selectedBranchDt = ClientUtil.getOtherBranchCurrentDate(CommonUtil.convertObjToStr(existingMap.get("BRANCH_CODE")));
                Date currentDate = ClientUtil.getCurrentDate();
                //System.out.println("selectedBranchDt : "+selectedBranchDt + " currentDate : "+currentDate);
                if(selectedBranchDt == null){
                    ClientUtil.displayAlert("BOD is not completed for the selected branch " +"\n"+"Interbranch Transaction Not allowed");
                    flag = false;
                    finalChecking = true;
                }
//                else if(DateUtil.dateDiff(currentDate, selectedBranchDt)!=0){ // Commented by nithya on 05-10-2018 for KD 270 - Gold Loan Renewal - Now it is not possible to do the interbranch transaction Credit
//                    ClientUtil.displayAlert("Application Date is different in the Selected branch " +"\n"+"Interbranch Transaction Not allowed");
//                    flag = false;
//                    finalChecking = true;
//                } 
                else {
                    String memNo=CommonUtil.convertObjToStr(existingMap.get("MEMBERSHIP_NO"));
                    String memNoStr="";
                    if(memNo!=null && memNo.length()>0){
                       memNoStr="\n Member No: "+ memNo;
                    }
                    cr_prod_type = CommonUtil.convertObjToStr(existingMap.get("PROD_TYPE")); // Added by nithya on 01-12-2016 for 5507
                    //System.out.println("Continue for interbranch trasactions ...");
                    String[] obj5 = {"Proceed", "ReEnter"};
                    chktrans = false;
                    int option4 = COptionPane.showOptionDialog(null, ("Please check whether Account No, Name correct or not " + "\nOperative AcctNo is : " + CommonUtil.convertObjToStr((existingMap.get("Account Number") == null) ? existingMap.get("ACCOUNT NUMBER") : existingMap.get("Account Number")) + "\nCustomer Name :" + CommonUtil.convertObjToStr((existingMap.get("Customer Name") == null) ? existingMap.get("CUSTOMER NAME") : existingMap.get("Customer Name")))+memNoStr, ("Transaction Part"),
                    COptionPane.YES_NO_CANCEL_OPTION, COptionPane.QUESTION_MESSAGE, null, obj5, obj5[0]);
                    if (option4 == 0) {
                        flag = true;
                    } else {
                        flag = false;
                    }
                }
            } else{
                String memNo=CommonUtil.convertObjToStr(existingMap.get("MEMBERSHIP_NO"));
                    String memNoStr="";
                    if(memNo!=null && memNo.length()>0){
                       memNoStr="\n Member No: "+ memNo;
                    }
                    cr_prod_type = CommonUtil.convertObjToStr(existingMap.get("PROD_TYPE"));
                String[] obj5 = {"Proceed", "ReEnter"};
                chktrans = false;
                int option4 = COptionPane.showOptionDialog(null, ("Please check whether Account No, Name correct or not " + "\nOperative AcctNo is : " + CommonUtil.convertObjToStr((existingMap.get("Account Number") == null) ? existingMap.get("ACCOUNT NUMBER") : existingMap.get("Account Number")) + "\nCustomer Name :" + CommonUtil.convertObjToStr((existingMap.get("Customer Name") == null) ? existingMap.get("CUSTOMER NAME") : existingMap.get("Customer Name")))+memNoStr, ("Transaction Part"),
                COptionPane.YES_NO_CANCEL_OPTION, COptionPane.QUESTION_MESSAGE, null, obj5, obj5[0]);
                if (option4 == 0) {
                    flag = true;
                } else {
                    flag = false;
                }
            }           
        }
        
        if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("N")) {
            if (prodType.equals("OA") && observable.isRenewalYesNo()) {
                double totalReceivableAmt = CommonUtil.convertObjToDouble(observable.getTotRecivableAmt())
                        + CommonUtil.convertObjToDouble(lblServiceTaxVal.getText())
                        + CommonUtil.convertObjToDouble(txtCharges.getText());
                double renewalLimitAmt = CommonUtil.convertObjToDouble(txtRenewalLimit_SD.getText());
                Double enteredAmt = totalReceivableAmt - renewalLimitAmt;
                System.out.println("totalReceivableAmt :: " + totalReceivableAmt);
                System.out.println("enteredAmt :: " + enteredAmt);
                if (enteredAmt > 0) {
                    HashMap availableBalCheckMap = new HashMap();
                    availableBalCheckMap.put("ACT_NUM", sbAcNo.toUpperCase());
                    List availableBalLst = ClientUtil.executeQuery("getOABalanceForStanding", availableBalCheckMap);
                    if (availableBalLst != null && availableBalLst.size() > 0) {
                        HashMap balMap = (HashMap) availableBalLst.get(0);
                        if (balMap.containsKey("TOTAL_BALANCE") && null != balMap.get("TOTAL_BALANCE") && !balMap.get("TOTAL_BALANCE").equals("")) {
                            Double availableActBal = CommonUtil.convertObjToDouble(balMap.get("TOTAL_BALANCE"));

                            if (availableActBal < enteredAmt) {
                                displayAlert("Available Balance( " + balMap.get("TOTAL_BALANCE") + " )is less than the input Amount");
                                flag = false;
                            }
                        }
                    }
                }
            }
        }
        
        
        
        return flag;
    }

    private boolean validateScreenLock() {
        HashMap authDataMap = new HashMap();
        authDataMap.put("TRANS_ID", observable.getStrACNumber());
        authDataMap.put("USER_ID", ProxyParameters.USER_ID);
        authDataMap.put("TRANS_DT", ClientUtil.getCurrentDate());
        authDataMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
        List lst = ClientUtil.executeQuery("selectauthorizationLock", authDataMap);
        StringBuffer open = new StringBuffer();
        if (lst != null && lst.size() > 0) {
            for (int i = 0; i < lst.size(); i++) {
                HashMap map = (HashMap) lst.get(i);
                open.append("\n" + "User Id  :" + " ");
                open.append(CommonUtil.convertObjToStr(map.get("OPEN_BY")) + "\n");
                open.append("Mode Of Operation  :" + " ");
                open.append(CommonUtil.convertObjToStr(map.get("MODE_OF_OPERATION")) + " ");
            }
            ClientUtil.showMessageWindow("Already opened by" + open);
            return true;
        }
        return false;
    }

    private void deletescreenLock() {
        HashMap map = new HashMap();
        map.put("USER_ID", ProxyParameters.USER_ID);
        map.put("TRANS_DT", curr_dt);
        map.put("INITIATED_BRANCH", TrueTransactMain.BRANCH_ID);
        ClientUtil.execute("DELETE_SCREEN_LOCK", map);
    }

    private void btnSaveDisable() {
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
        observable.doAction(nomineeUi.getNomineeOB(), 4);
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
             if (fromNewAuthorizeUI) {
                newauthorizeListUI.removeSelectedRow();
                this.dispose();
                newauthorizeListUI.setFocusToTable();
                newauthorizeListUI.displayDetails("Gold Loan Account Opening");
            }
            if (fromAuthorizeUI) {
                authorizeListUI.removeSelectedRow();
                this.dispose();
                authorizeListUI.setFocusToTable();
                authorizeListUI.displayDetails("Gold Loan Account Opening");
            }
            if (fromManagerAuthorizeUI) {
                ManagerauthorizeListUI.removeSelectedRow();
                this.dispose();
                ManagerauthorizeListUI.setFocusToTable();
            }
        }
        //        if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED && observable.getAvailableBalance() >0) {
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
        //                    ////System.out.println("#### :" +transMap);
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
        //        }
        super.setOpenForEditBy(observable.getStatusBy());
        super.removeEditLock(observable.getStrACNumber());
        isFilled = false;
        btnCancelActionPerformed(null);
        observable.setResultStatus();
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

    private void setSecurityDefaultValWhenNewBtnPressed() {
        //        observableSecurity.setTdtFromDate(tdtFDate.getDateValue());
        //        tdtFromDate.setDateValue(observableSecurity.getTdtFromDate());
        //        observableSecurity.setTdtToDate(tdtTDate.getDateValue());
        //        tdtToDate.setDateValue(observableSecurity.getTdtToDate());
    }

    // Actions have to be taken when a record of Facility Details is selected in Facility Table(Sanction Details)
    private void sanctionFacilityTabPressed() {
        int selRow = -1;
        if (loanType.equals("LTD")) {
            selRow = 0;
        } else if (sanfacTab) {
            selRow = rowfactab;
        } else {
            //            selRow = tblSanctionDetails.getSelectedRow();
            rowfactab = selRow;
        }
        sanfacTab = false;
        if (selRow >= 0) {
            // If the the table is in editable mode
            updateSecurity = false;
            updateRepayment = false;
            updateGuarantor = false;
            updateInterest = false;
            updateDocument = false;
            if (loanType.equals("OTHERS") && !viewType.equals(CommonConstants.STATUS_AUTHORIZED)) {
                observableSecurity.resetAllSecurityDetails();
                observableSecurity.resetSecurityTableUtil();
//                observableOtherDetails.resetOtherDetailsFields();
                observableRepay.resetAllRepayment();
                observableRepay.resetRepaymentCTable();
//                observableGuarantor.resetGuarantorDetails();
//                observableGuarantor.resetInstitGuarantorDetails();
//                observableGuarantor.resetGuarantorCTable();
                observableDocument.resetAllDocumentDetails();
                observableDocument.resetDocCTable();
                observableInt.resetAllInterestDetails();
                observableClassi.resetClassificationDetails();
                observable.ttNotifyObservers();
            }
            observable.setFacilityAcctHead();
            observable.populateFacilityDetails(rowSanctionMain, selRow);
            //            if ((observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) || (viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT) || (observable.getCboAccStatus().equals("Closed")))){
            //                // If the record is populated for Delete and Authorize
            setAllFacilityDetailsEnableDisable(false);
            setFacilityBtnsEnableDisable(false);
            //                setAllInsuranceBtnsEnableDisable(false);
            setAllGuarantorBtnsEnableDisable(false);
            setDocumentToolBtnEnableDisable(false);
            //                additionalSanctionbtnEnableDisable(false);
            //            }else{
            //                // If the record is populated for Edit mode
            //                setAllFacilityDetailsEnableDisable(true);
            //                setFacilityBtnsEnableDisable(true);
            //                additionalSanctionbtnEnableDisable(true);
            //            }
            displayTabsByAccountNumber();
            rowFacilityTabFacility = selRow;
        }
        observable.setFacilityProdID(selRow);
        observable.ttNotifyObservers();
        String strFacilityType = getCboTypeOfFacilityKeyForSelected();
        if (strFacilityType.equals(LOANS_AGAINST_DEPOSITS)) {
            changesInUIForLoanAgainstDeposit();
            if (observable.getStrACNumber().length() > 0) {
                if (loanType.equals("LTD")) {
                    if (facilitySaved && btnNewPressed) {
                        populateInterestRateForLTD();
                        btnNewPressed = false;
                    }
                }
            }
            strFacilityType = null;
            if (loanType.equals("LTD")) {
                //                rdoInterest_Simple.setEnabled(false);
                //                rdoInterest_Compound.setEnabled(false);
                //            if (observable.isLienChanged())
                //                tabLimitAmount.setSelectedIndex(5);
                //            else
                //                tabLimitAmount.setSelectedIndex(4);
            }
        }
    }

    private boolean populateInterestRateForLTD() {
        //        if (tblInterMaintenance.getRowCount()<1) {
        HashMap whereMap = new HashMap();
        whereMap.put("CATEGORY_ID", observableBorrow.getCbmCategory().getKeyForSelected());
        if (CommonUtil.convertObjToDouble(observable.getTxtLimit_SD()).doubleValue() > 0) {
            whereMap.put("AMOUNT", getBigDecimal(CommonUtil.convertObjToDouble(observable.getTxtLimit_SD()).doubleValue()));
        } else {
            whereMap.put("AMOUNT", getBigDecimal(CommonUtil.convertObjToDouble(txtLimit_SD.getText()).doubleValue()));
        }
        whereMap.put("PROD_ID", CommonUtil.convertObjToStr(observable.getCbmProductId().getKeyForSelected()));//observable.getLblProductID_FD_Disp());
        //            whereMap.put("FROM_DATE", getTimestamp(DateUtil.getDateMMDDYYYY(tdtFDate.getDateValue())));
        //            whereMap.put("TO_DATE", getTimestamp(DateUtil.getDateMMDDYYYY(tdtTDate.getDateValue())));
        //            deleteAllInterestDetails();
        observableInt.resetInterestDetails();
        updateInterestDetails();
        // Populate the values
        ArrayList interestList = (java.util.ArrayList) ClientUtil.executeQuery("getSelectProductTermLoanInterestTO", whereMap);
        observableInt.setIsNew(true);
        if (interestList != null && interestList.size() > 0) {
            //                if (observable.getStrACNumber().length() > 0){
            observableInt.setLoanType(loanType);
            observable.setLoanType(loanType);
            observableInt.setTermLoanInterestTO(interestList, null);
            observable.setCboIntGetFrom("");
            //                    cboIntGetFrom.setSelectedItem("Account");

            //                }
        } else {
            displayAlert("Interest rates not created for this product...");
            //                cboIntGetFrom.setSelectedItem("");
            return true;
        }
        //            if (tblRepaymentCTable.getRowCount()<1) {
        ////                observable.ttNotifyObservers();
        //                btnRepayment_NewActionPerformed();
        //                btnEMI_CalculateActionPerformed();
        //                btnRepayment_SaveActionPerformed();
        //            }
        observableInt.setIsNew(false);
        //        } else if (tblRepaymentCTable.getRowCount()>0) {
        HashMap where = new HashMap();
        //            where.put("ACT_NO", lblAccNo_RS_2.getText());
        int installmentCount = CommonUtil.convertObjToInt(ClientUtil.executeQuery("getCountOfInstallments", where).get(0));
        if (installmentCount <= 0) {
            sanMousePress = true;
            //                tblRepaymentCTableMousePressed();
            //                btnEMI_CalculateActionPerformed();
            //                btnRepayment_SaveActionPerformed();
        }
        where = null;
        //        }
        return false;
    }

    private boolean checkInterestRateForLTD() {
        HashMap whereMap = new HashMap();
        whereMap.put("CATEGORY_ID", observableBorrow.getCbmCategory().getKeyForSelected());
        if (CommonUtil.convertObjToDouble(observable.getTxtLimit_SD()).doubleValue() > 0) {
            whereMap.put("AMOUNT", getBigDecimal(CommonUtil.convertObjToDouble(observable.getTxtLimit_SD()).doubleValue()));
        } else {
            whereMap.put("AMOUNT", getBigDecimal(CommonUtil.convertObjToDouble(txtLimit_SD.getText()).doubleValue()));
        }
        whereMap.put("PROD_ID", CommonUtil.convertObjToStr(observable.getCbmProductId().getKeyForSelected()));//observable.getLblProductID_FD_Disp());
        //        whereMap.put("FROM_DATE", getTimestamp(DateUtil.getDateMMDDYYYY(tdtFDate.getDateValue())));
        //        whereMap.put("TO_DATE", getTimestamp(DateUtil.getDateMMDDYYYY(tdtTDate.getDateValue())));
        //            deleteAllInterestDetails();
        //            observableInt.resetInterestDetails();
        //            updateInterestDetails();
        // Populate the values
        ArrayList interestList = (java.util.ArrayList) ClientUtil.executeQuery("getSelectProductTermLoanInterestTO", whereMap);
        //        observableInt.setIsNew(true);
        if (interestList == null || interestList.size() == 0) {
            displayAlert("Interest rates not created for this product...");
            return true;
        }
        return false;
    }

    // Security Details, Repayment Schedule will be populated on the basis of Account Number
    private void displayTabsByAccountNumber() {
        final HashMap hash = new HashMap();
        updateOBFields();
        if (observable.getStrACNumber().length() > 0) {
            // Retrieve the values on the basis of Account Number
            hash.put("WHERE", observable.getStrACNumber());
            hash.put("KEY_VALUE", "ACCOUNT_NUMBER");

            //            observable.populateData(hash, authSignUI.getAuthorizedSignatoryOB(), poaUI.getPowerOfAttorneyOB());

        } else {
            btnsDisableBasedOnAccountNumber();
            setDefaultValB4AcctCreation();
            observableClassi.populateClassiDetailsFromProd();
            observableClassi.setClassifiDetails(CommonConstants.TOSTATUS_INSERT);
//            observableOtherDetails.setOtherDetailsMode(CommonConstants.TOSTATUS_INSERT);
            updateProdClassiFields();
            //            ClientUtil.enableDisable(panAccountDetails, false);
        }
        //        authSignUI.resetDisableNoOfAuthSign(true);
        // This will populate the customer details in account level tabs
        populateCustomerProdLeveFields();
        observable.ttNotifyObservers();
    }

    private void populateCustomerProdLeveFields() {
        // This will populate Group description field
        observable.populateCustomerProdLeveFields();
    }

    private void enableDisableGetIntFrom(boolean val) {
        //        cboIntGetFrom.setEnabled(val);
    }

    private void setSanctionProductDetailsDisable() {
        //        cboTypeOfFacility.setEnabled(false);
        //        cboProductId.setEnabled(false);
    }

    private void setDefaultValB4AcctCreation() {
        observable.setDefaultValB4AcctCreation();
        tdtDemandPromNoteDate.setDateValue(observable.getTdtDemandPromNoteDate());
        calculateDPNExpDate();
        observableClassi.setDefaultValB4AcctCreation();
        //        observableOtherDetails.populateProdLevelValB4AcctCreation(CommonUtil.convertObjToStr(((ComboBoxModel) cboProductId.getModel()).getKeyForSelected()));
    }

    private void btnsDisableBasedOnAccountNumber() {
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

    private void resetTabsDependsOnAccountNumber() {
        observableSecurity.resetAllSecurityDetails();
        observableSecurity.resetSecurityTableUtil();
//        observableOtherDetails.resetOtherDetailsFields();
        observableRepay.resetAllRepayment();
        observableRepay.resetRepaymentCTable();
//        observableGuarantor.resetAllGuarantorDetails();
//        observableGuarantor.resetGuarantorCTable();
        observableDocument.resetAllDocumentDetails();
        observableDocument.resetDocCTable();
        observableInt.resetAllInterestDetails();
        observableInt.resetInterestCTable();
        observableClassi.resetClassificationDetails();
    }
    private void cboProductIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductIdActionPerformed
        // Add your handling code here:
        cboProductIDActionPerformed();
    }//GEN-LAST:event_cboProductIdActionPerformed
    private void cboProductIDActionPerformed() {
        //        observable.setCboProductId(CommonUtil.convertObjToStr(cboProductId.getSelectedItem()));
        //        observable.setCbmProductId(((ComboBoxModel)cboProductId).getKeyForSelected().toString());
        //        observable.getCbmProductId().setKeyForSelected(((ComboBoxModel)cboProductId.getModel()).getKeyForSelected());
        if (observable.getCboProductId().length() > 0) {
            observable.setFacilityAcctHead();
            if (observable.getStrACNumber().equals("")) {
                if (tblBorrowerTabCTable.getRowCount() > 0) {
                    observable.setFacilityContactDetails(CommonUtil.convertObjToStr(tblBorrowerTabCTable.getValueAt(0, 1)));
                    //                    txtContactPerson.setText(observable.getTxtContactPerson());
                    //                    txtContactPhone.setText(observable.getTxtContactPhone());
                }
                observableClassi.populateClassiDetailsFromProd();
                updateProdClassiFields();
                //                observableOtherDetails.populateProdLevelValB4AcctCreation(CommonUtil.convertObjToStr(((ComboBoxModel) cboProductId.getModel()).getKeyForSelected()));
                updateOtherDetailsTab();
                //FOR CHECK SHARE HOLDER OR NOT
                checkShareHolder();
            } else {
                observableInt.setValByProdID();
            }
        } else {
            observable.setLblAccHead_2("");
            updateRdoSubsidyAndInterestNature();
        }
        //        lblAccHead_2.setText(observable.getLblAccHead_2());
        updateAccHead_ProdID();
    }

    private void checkShareHolder() {
        //        String selectKey=CommonUtil.convertObjToStr(((ComboBoxModel)cboProductId.getModel()).getKeyForSelected());
        HashMap shareMap = new HashMap();
        //        shareMap.put("PROD_ID",selectKey);
        String sharetype = "NOMINAL";
        List shareList = ClientUtil.executeQuery("getSelectShareProductLoanAcct", shareMap);
        if (shareList != null && shareList.size() > 0) {
            //            if(CommonUtil.convertObjToStr(((ComboBoxModel)cboConstitution.getModel()).getKeyForSelected()).equals("JOINT_ACCOUNT") && tblBorrowerTabCTable.getRowCount()>0)

            shareMap.put("CUST_ID", txtCustID.getText());
            List share = ClientUtil.executeQuery("getShareAccInfoDataForLoan", shareMap);
            if (share != null && share.size() > 0) {
                shareMap = (HashMap) share.get(0);
                String notElegibleLoan = CommonUtil.convertObjToStr(shareMap.get("NOT_ELIGIBLE_LOAN"));
                if (notElegibleLoan != null && notElegibleLoan.equals("Y") && shareMap.get("NOT_ELIGIBLE_DT") != null) {
                    Date eligibal_dt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(shareMap.get("NOT_ELIGIBLE_DT")));
                    if (DateUtil.dateDiff(eligibal_dt, (Date) curr_dt.clone()) < 0) {
                        ClientUtil.showMessageWindow(" Eligible Date is Not Expiry");
                    }
                    return;


                } else if (shareMap.get("SHARE_TYPE") != null && sharetype.equals(CommonUtil.convertObjToStr(shareMap.get("SHARE_TYPE")))) {
                    ClientUtil.showMessageWindow("Share Type is NOMINAL");
                    return;
                }
                //            ClientUtil.showMessageWindow("This account Holder Not Having Share");
            }
        }
    }

    private void updateProdClassiFields() {
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

    private void updateAccHead_ProdID() {
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

    private void updateOtherDetailsTab() {
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
    private void btnFacilityDeletePressed() {
        if (allCTablesNotNull()) {
            observable.deleteFacilityRecord(rowFacilityTabSanction, rowFacilityTabFacility);
            //            deleteAllSecurityDetails();
            //            deleteAllGuarantorDetails();
            //            deleteAllInsuranceDetails();
            //            deleteAllInterestDetails();
            observable.resetAllFacilityDetails();
            updateCboTypeOfFacility();
            observable.ttNotifyObservers();
            updateOBFields();
            observableClassi.setClassifiDetails(CommonConstants.TOSTATUS_DELETE);
//            observableOtherDetails.setOtherDetailsMode(CommonConstants.TOSTATUS_DELETE);
            observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
            observable.doAction(nomineeUi.getNomineeOB(), 3);
            //            authSignUI.setLblStatus(observable.getLblStatus());
            //            poaUI.setLblStatus(observable.getLblStatus());
            resetTabsDependsOnAccountNumber();
            observable.setResultStatus();
            //            rowFacilityTabSanction = tblSanctionDetails2.getSelectedRow();
            setAllFacilityDetailsEnableDisable(false);
            setFacilityBtnsEnableDisable(false);
            btnsDisableBasedOnAccountNumber();
            updateRdoSubsidyAndInterestNature();
        }
    }
    private void btnFacilitySaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFacilitySaveActionPerformed
        // Add your handling code here:
        btnFacilitySavePressed();
    }//GEN-LAST:event_btnFacilitySaveActionPerformed
    private boolean btnFacilitySavePressed() {
        //changeplace to 12###@
        facilitySaved = false;
        facilityFlag = false;
        sanction = true;
        sandetail = true;
        santab = true;
        sanfacTab = true;
        //        ////System.out.println("@@@@ tblSecurityTable.getRowCount() : "+tblSecurityTable.getRowCount());
        boolean isWarnMsgExist = false;
        if (rowSanctionMain == -1) {
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


        String mandatoryMessage4 = "";
        String mandatoryMessage5 = "";
        String mandatoryMessage6 = isJointAcctHavingAtleastOneCust();
        if (productBasedValidation()) {
            mandatoryMessage5 = new GoldLoanMRB().getString("rdoMultiDisburseAllow_Yes");
        }

        String mandatoryMessage7 = "";
        String mandatoryMessage8 = "";
        if (observable.getStrACNumber().length() > 0) {
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
            mandatoryMessage7 = isInterestDetailsExistForThisAcct();
            mandatoryMessage8 = validateOtherDetailsMandatoryFields();
        }

        //        if (mandatoryMessage1.length() > 0 || mandatoryMessage2.length() > 0 || mandatoryMessage3.length() > 0 || mandatoryMessage4.length() > 0 || mandatoryMessage5.length() > 0 || mandatoryMessage6.length() > 0 || mandatoryMessage7.length() > 0 || mandatoryMessage8.length() > 0){
        //            displayAlert(mandatoryMessage1+mandatoryMessage2+mandatoryMessage3+mandatoryMessage4+mandatoryMessage5+mandatoryMessage6+mandatoryMessage7+mandatoryMessage8);
        isWarnMsgExist = true;
        //        }else{

        //change from line 12#### top check
        //        ////System.out.println("@@@@ tblSecurityTable.getRowCount() : "+tblSecurityTable.getRowCount());
        //            if (isTablesInEditMode(false) && allCTablesNotNull() && checkForSecurityValue() && repayTableNotNull() && repayTableLimitCheckingRule()
        //            && observable.checkMaxAmountRange(tdtTDate.getDateValue())){
        updateOBFields();
        if (loanType.equals("LTD"))//|| loanType.equals("OTHERS"))
        {
            rowFacilityTabSanction = 0; //BY ABI FOR ONE MORE LOAN SANCTION NOT UPDATE
        } else {
            rowFacilityTabSanction = rowsan;
        }
        observable.addFacilityDetails(rowFacilityTabSanction, rowFacilityTabFacility);
        updateOBFields();

        //                if(observable.getStrACNumber().length()>0)
        //                    agriSubSidyUI.updateOBFields();

        observable.doAction(nomineeUi.getNomineeOB(), 2);
        //                if (observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED && loanType.equals("LTD"))
        //                    btnFacilitySave.setEnabled(true);
        //                else
        //                    btnFacilitySave.setEnabled(false);

        facilitySaved = true;
        //        authSignUI.setLblStatus(observable.getLblStatus());
        //        poaUI.setLblStatus(observable.getLblStatus());
        observable.resetSanctionFacility();
        resetTabsDependsOnAccountNumber();
        observable.resetAllFacilityDetails();
        updateCboTypeOfFacility();
        observable.setBorrowerNumber();
        observable.setResultStatus();
        setAllSanctionFacilityEnableDisable(false);
        setSanctionFacilityBtnsEnableDisable(false);
        //                btnNew1.setEnabled(true);
        setAllFacilityDetailsEnableDisable(false);
        setFacilityBtnsEnableDisable(false);
        btnsDisableBasedOnAccountNumber();
        //                rowFacilityTabSanction = tblSanctionDetails2.getSelectedRow();
        observable.ttNotifyObservers();
        updateRdoSubsidyAndInterestNature();
        observable.setStrACNumber(observable.getLoanACNo());
        lblAcctNo_Sanction_Disp.setText(observable.getLoanACNo());
        //                if(loanType.equals("OTHERS") ){    //ltd and testing for same abi
        btnSave2_SDActionPerformed(null);
        tblSanctionDetails2MousePressed(null);
        tblSanctionDetailsMousePressed(null);
        ////System.out.println("####%%%% observable.getCbmProductId().getSelectedItem() " + observable.getCbmProductId().getSelectedItem());
        //                ////System.out.println("####%%%% cboProductId.getSelectedItem() "+cboProductId.getSelectedItem());
        //                    cboProductId.setSelectedItem(observable.getCbmProductId().getSelectedItem());
        //                    cboProductIdActionPerformed(null);
        //                }
        if (observable.isLienChanged()) {
            tblSanctionDetails2MousePressed(null);
        }
        //            }else{
        isWarnMsgExist = true;
        //            }
        //        }
        //        facilityFlag=true;
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
    //        //System.out.print("repaytable###"+behaves);
    //        if(loanType.equals("LTD") && observable.getStrACNumber().length()>0)
    //            if(behaves !=null && (!(behaves.equals("OD") || behaves.equals("CC")))){
    //                LinkedHashMap repaymentMap=observableRepay.getTableUtilRepayment().getAllValues();
    //                LinkedHashMap additionalSanctionMap=observableAdditionalSanctionOB.getAdditionalSanUtil().getAllValues();
    //                ArrayList repaymentList=observableRepay.getTableUtilRepayment().getTableValues();
    //                ArrayList additionalSanctionList=observableAdditionalSanctionOB.getAdditionalSanUtil().getTableValues();
    //                ////System.out.println(additionalSanctionMap+"repaymentMap  ####"+repaymentMap);
    //                ////System.out.println("\n"+additionalSanctionList+"repaymentList  ####"+repaymentList);
    //                java.util.Set set=  repaymentMap.keySet();
    //                Object objKeySet[]=(Object[])set.toArray();
    //                if(repaymentMap !=null && repaymentMap.size()>0){
    //                    for(int i=0;i<repaymentMap.size();i++){
    //                        singleMap=(HashMap)repaymentMap.get(objKeySet[i]);
    //                        totalRepaymentLimit+=CommonUtil.convertObjToDouble(singleMap.get("TOT_BASE_AMT")).doubleValue();
    //                    }
    //                    ////System.out.println("totalRepaymentLimit   "+totalRepaymentLimit);
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
    //                    ////System.out.println("totalRepaymentLimit1   "+totalRepaymentLimit);
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

    private String validateOtherDetailsMandatoryFields() {
        StringBuffer stbWarnMsg = new StringBuffer("");
        String strSelectedProdType = getCboTypeOfFacilityKeyForSelected();

        if ((strSelectedProdType.equals("OD") || strSelectedProdType.equals("CC"))) {
            GoldLoanMRB objMandatoryRB = new GoldLoanMRB();
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
            objMandatoryRB = null;
        }

        return stbWarnMsg.toString();
    }

    private String isInterestDetailsExistForThisAcct() {
        StringBuffer stbWarnMsg = new StringBuffer("");
        String strSelectedProdType = getCboTypeOfFacilityKeyForSelected();
        //        if (!(strSelectedProdType.equals("OD") || strSelectedProdType.equals("CC")) && tblInterMaintenance.getRowCount() < 1){
        stbWarnMsg.append("\n");
        //            stbWarnMsg.append(resourceBundle.getString("interestDetailsWarning"));
        //        }
        return stbWarnMsg.toString();
    }

    private void updateRdoSubsidyAndInterestNature() {
        updateRdoSubsidy();
        updateRdoInterestNature();
    }

    private void updateRdoSubsidy() {
        observable.resetFacilityTabSubsidy();
        //        removeFacilitySubsidy();
        //        rdoSubsidy_Yes.setSelected(observable.getRdoSubsidy_Yes());
        //        rdoSubsidy_No.setSelected(observable.getRdoSubsidy_No());
        //        addFacilitySubsidyRadioBtns();
    }

    private void updateRdoInterestNature() {
        observable.resetFacilityTabInterestNature();
        //        removeFacilityInterestNature();
        //        rdoNatureInterest_PLR.setSelected(observable.getRdoNatureInterest_PLR());
        //        rdoNatureInterest_NonPLR.setSelected(observable.getRdoNatureInterest_NonPLR());
        //        addFacilityInterestNatureBtns();
    }

    private void sanctionMainTabPressed() {
        int selRow = -1;
        if (loanType.equals("LTD")) {
            selRow = 0;
        } else if (santab) {
            selRow = rowmaintab;
        } else {
            //            selRow = tblSanctionDetails2.getSelectedRow();
            rowmaintab = selRow;
        }
        santab = false;
        if (selRow >= 0) {
            updateOBFields();
            observable.populateFacilityTabSanction(selRow/*, rowSanctionMain*/);
            rowFacilityTabSanction = selRow;
            if (!(viewType.equals("Delete") || viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT))) {
                observable.resetAllFacilityDetails();
                updateCboTypeOfFacility();
                observableSecurity.resetSecurityTableUtil();
                observableSecurity.resetAllSecurityDetails();
//                observableOtherDetails.resetOtherDetailsFields();
                observableRepay.resetAllRepayment();
                observableRepay.resetRepaymentCTable();
//                observableGuarantor.resetGuarantorCTable();
//                observableGuarantor.resetAllGuarantorDetails();
                observableDocument.resetAllDocumentDetails();
                observableDocument.resetDocCTable();
                observableInt.resetInterestCTable();
                observableInt.resetAllInterestDetails();
                observableClassi.resetClassificationDetails();
                observableAdditionalSanctionOB.resetAdditionalSanctionDetails();
            }
            setAllFacilityDetailsEnableDisable(false);
            setFacilityBtnsEnableDisable(false);
            btnsDisableBasedOnAccountNumber();
            observable.ttNotifyObservers();
        }
    }
    private void tblSanctionDetailsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSanctionDetailsMousePressed
        // Add your handling code here:
        ////System.out.println("jjjjjjjjjjjjaaaaaaaaaaaaa");
        tblSanctionDetailsMousePressed();
    }//GEN-LAST:event_tblSanctionDetailsMousePressed

    private void tblSanctionDetailsMousePressed() {
        if (enableControls) {
            if (observable.getStrACNumber().length() > 0) {
                ClientUtil.showMessageWindow(observable.getStrACNumber() + " is in Edit mode. Press Save/Cancel/New.");
            } else {
                ClientUtil.showMessageWindow("This a/c is in New mode. Press Save/Cancel/New.");
            }
            return;
        }

        if (!(viewType.equals(AUTHORIZE) || viewType.equals("Delete"))) {
            tblSanctionDetailsPopulate();
        }
        //        if(loanType.equals("LTD")){
        //            morotoriumEnableDisable(false);
        //        }
        if (CommonUtil.convertObjToStr(cboAccStatus.getSelectedItem()).equals("Closed")) {
            HashMap map = new HashMap();
            if (lblAcctNo_Sanction_Disp.getText() != null && lblAcctNo_Sanction_Disp.getText().length() > 0) {
                map.put("ACT_NUM", lblAcctNo_Sanction_Disp.getText());
                List lst = ClientUtil.executeQuery("getDepositClosingAccounts", map);
                if (lst != null && lst.size() > 0) {
                    ClientUtil.showMessageWindow("Account Closed but Authorization pending ");
                    ClientUtil.enableDisable(this, false);
                    btnSave.setEnabled(false);
                } else {
                    ////System.out.println("test22222222222222");
                    ClientUtil.showMessageWindow("Account Closed");
                    ClientUtil.enableDisable(this, false);
                    btnRenew.setEnabled(false);
                    btnSave.setEnabled(false);
                }
            } else {
                map = new HashMap();
            }

        } else if (viewType.equals("Edit")) {
            btnRenew.setVisible(true);
        }

        if (observable.getStrACNumber() != null && observable.getStrACNumber().length() > 0) {
            //            txtSanctionNo.setEnabled(false);
            insertScreenLock();
            if (validateScreenLock()) {
                btnCancelActionPerformed(null);
            }
            return;
        }

    }

    private void insertScreenLock() {
        HashMap hash = new HashMap();
        hash.put("USER_ID", ProxyParameters.USER_ID);
        hash.put("TRANS_ID", observable.getStrACNumber());
        hash.put("MODE_OF_OPERATION", viewType);
        ClientUtil.execute("insertauthorizationLock", hash);
    }
    private void tblSanctionDetails2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSanctionDetails2MousePressed
        // Add your handling code here:
        if (enableControls) {
            if (observable.getStrACNumber().length() > 0) {
                ClientUtil.showMessageWindow(observable.getStrACNumber() + " is in Edit mode. Press Save/Cancel/New.");
            } else {
                ClientUtil.showMessageWindow("This a/c is in New mode. Press Save/Cancel/New.");
            }
            return;
        }
        tblSanctionDetails2Populate();
    }//GEN-LAST:event_tblSanctionDetails2MousePressed
    private void updateCboTypeOfFacility() {
        //        cboTypeOfFacility.setSelectedItem(observable.getCboTypeOfFacility());
        //        observable.getCbmTypeOfFacility().setSelectedItem(observable.getCboTypeOfFacility());
    }

    private void tblSanctionDetailsPopulate() {
        sanMousePress = false;
        // Actions have to be taken when a record of Facility Details is selected in Sanction Details Tab
        //        if ((viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT) || viewType.equals("Edit")))
        //        if (tblSanctionDetails.getSelectedRow() >= 0 || loanType.equals("LTD") || sandetail){
        allowMultiRepay = true;
        observableRepay.setAllowMultiRepay(true);
        updateOBFields();
        if (!loanType.equals("LTD") && (viewType.equals(AUTHORIZE) || viewType.equals("Delete"))) {
            observable.resetSanctionFacility();
        }
        // If Facility Details is in Edit Mode
        //if ((tblSanctionDetails2.getSelectedRow() == tblFacilityDetails.getSelectedRow()) && (tblSanctionDetails.getSelectedRow() == tblFacilityDetails2.getSelectedRow())){
        setSanctionFacilityBtnsEnableDisable(false);
        setAllSanctionFacilityEnableDisable(false);
        //                observable.sanctionFacilityEditWarning();
        if (!(viewType.equals("Delete") || viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT) || (observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW))) {
            if (loanType.equals("LTD")) {
                rowSanctionFacility = 0;
                sanDetailMousePressedForLTD = true;
            } else if (sandetail) {
                rowSanctionFacility = rowsanDetail;
            } else {
                //                    rowSanctionFacility = tblSanctionDetails.getSelectedRow();
                rowsanDetail = rowSanctionFacility;
                sanMousePress = true;
            }

            //                if ((observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) || (viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT))){
            //                    setSanctionFacilityBtnsEnableDisable(false);
            //                    setAllSanctionFacilityEnableDisable(false);
            //                }else{
            //                    updateSanctionFacility = true;
            //                    setSanctionFacilityBtnsEnableDisable(true);
            //                    setAllSanctionFacilityEnableDisable(true);
            //                }
            //                observable.populateSanctionFacility(tblSanctionDetails.getSelectedRow());
            observable.populateSanctionFacility(rowSanctionFacility);
            updateCboTypeOfFacility();
            sandetail = false;
        }
        observable.ttNotifyObservers();
        sanctionFacilityTabPressed();
        if (viewType != null && viewType.equals("Edit")) {
            //                btnLTD.setEnabled(false);
            //                btnNew1.setVisible(true);
            //                btnSave1.setVisible(true);
            //                btnNew1.setEnabled(true);
            //                btnSave1.setEnabled(true);
        }
        //            if (observable.getStrACNumber().length()>0) {
        //                btnFacilitySave.setVisible(false);
        //            } else {
        //                btnFacilitySave.setVisible(true);
        //            }
        //        }
        //        if(loanType.equals("LTD")) {
        //            cboTypeOfFacility.setEnabled(false);
        //        }
        //        tdtTDate.setEnabled(false);
        //        observable.setUpdateAvailableBalance(false);
        //        txtLimit_SD.setEnabled(false);
        //        btnLTD.setEnabled(false);
        //        txtNoInstallments.setEnabled(false);
        //        cboRepayFreq.setEnabled(false);
        //        tdtFDate.setEnabled(false);
        //        chkMoratorium_Given.setEnabled(false);
        //        additionalSanctionEnableDisable(false);
        //        additionalSanctionbtnEnableDisable(false);
        //        setAllInterestDetailsEnableDisable(false);
        //        setAllInterestBtnsEnableDisable(false);
        //        setAllSettlmentEnableDisable(false);
    }

    private void setAllSettlmentEnableDisable(boolean val) {
        //        settlementUI.setAllPoAEnableDisable(val);//dontdelete
    }

    private void tblSanctionDetails2Populate() {
        // Actions have to be taken when a record of Sanction Details is selected in Sanction Details Tab
        rowSanctionFacility = -1;
        updateSanctionFacility = false;
        int selRow = -1;
        if (loanType.equals("LTD")) {
            selRow = 0;
            //            ClientUtil.enableDisable(panTable_SD,false);
        } else if (sanction) {
            selRow = rowsan;
        } else {
            //            selRow = tblSanctionDetails2.getSelectedRow();
            rowsan = selRow;
        }
        ClientUtil.enableDisable(panSanctionDetails_Sanction, false);
        sanction = false;
        if (selRow >= 0) {
            if (!updateSanctionFacility) {
                updateOBFields();
                updateSanctionMain = true;
                observable.populateSanctionMain(selRow);
                rowSanctionMain = selRow;
                if (!(viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT) || viewType.equals("Delete"))) {
                    observable.resetSanctionFacility();
                }
                if (!(viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT) || viewType.equals("Delete"))) {
                    updateCboTypeOfFacility();
                }
                setAllSanctionMainEnableDisable(false);
                setSanctionFacilityBtnsEnableDisable(false);
                setAllSanctionFacilityEnableDisable(false);
                //                additionalSanctionEnableDisable(false);
                if ((observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) || (viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT) || viewType.equals("Enquirystatus"))) {
                    //                    btnNew1.setVisible(false);
                } else {
                    //                    btnNew1.setVisible(true);
                    //                    btnNew1.setEnabled(true);
                    //                    btnNew2_SD.setEnabled(true);
                }
                observable.ttNotifyObservers();
                sanctionMainTabPressed();
            } else {
                ClientUtil.showMessageWindow("A/c No." + observable.getStrACNumber() + " is in Edit mode. Please Save it.");
            }
        }
        //        if (loanType.equals("LTD")) {
        //            tblSanctionDetailsMousePressed(null);
        //        }
    }

    private void btnDelete1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete1ActionPerformed
        // Add your handling code here:
        btnDelete1Action();
    }//GEN-LAST:event_btnDelete1ActionPerformed
    private void btnDelete1Action() {
        // Facility Details CTable(Sanction Details Tab) Delete pressed
        observable.setLoanACNo(lblAcctNo_Sanction_Disp.getText());
        int mainSlno = rowSanctionMain;
        //        int slno = CommonUtil.convertObjToInt(((ArrayList)((EnhancedTableModel)tblSanctionDetails.getModel()).getDataArrayList().get(rowSanctionFacility)).get(0));
        int rows = observable.deleteSanctionFacility(rowSanctionFacility, updateSanctionMain, rowSanctionMain, rowFacilityTabSanction, rowFacilityTabFacility);
        if (rows != -1) {
            //            observable.deleteFacilityRecord(mainSlno, slno);
            observable.resetSanctionFacility();
            observable.resetAllFacilityDetails();
            updateCboTypeOfFacility();
            resetTabsDependsOnAccountNumber();
            setSanctionFacilityBtnsEnableDisable(false);
            setAllSanctionFacilityEnableDisable(false);
            setAllFacilityDetailsEnableDisable(false);
            setFacilityBtnsEnableDisable(false);
            btnsDisableBasedOnAccountNumber();
            //            btnNew1.setEnabled(true);
            observable.ttNotifyObservers();
            //            if (tblSanctionDetails.getRowCount() < 1){
            //                btnDelete2_SD.setEnabled(true);
            //            }


            rowSanctionFacility = -1;
            updateSanctionFacility = false;
        }
        //        else if(rows==0){
        //            btnFacilityDeletePressed();
        //        }
    }
    private void btnSave1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSave1ActionPerformed
        // Add your handling code here:
        btnSave1ActionPerformed();
    }//GEN-LAST:event_btnSave1ActionPerformed

    private void btnSave1ActionPerformed() {
        if (CommonUtil.convertObjToStr(cboAccStatus.getSelectedItem()).equals("Closed")) {
            return;
        }
        enableControls = true;
        setSanctionMainBtnsEnableDisable(true);
        //        additionalSanctionEnableDisable(true);
        //        additionalSanctionNewEnableDisable(true);
        //        if (tblSanctionDetails.getRowCount() > 0){
        //            btnDelete2_SD.setEnabled(false);
        //        }
        setAllSanctionMainEnableDisable(true);
        setSanctionFacilityBtnsEnableDisable(true);
        setAllSanctionFacilityEnableDisable(true);
        //        actTransUI.setActTransferEnableDisable(true);//dontdelete
        updateSanctionFacility = true;
        updateRecords = true;
        allowMultiRepay = observableRepay.getAllowMultiRepay();
        HashMap hash2 = new HashMap();
        boolean lienAuthorized = false;
        //            hash2.put("PROD_DESC",CommonUtil.convertObjToStr(cboProductId.getModel().getSelectedItem()));
        List behaveslike = ClientUtil.executeQuery("getLoanBehaves", hash2);
        if (behaveslike != null && behaveslike.size() > 0) {
            hash2 = (HashMap) behaveslike.get(0);
        }
        if (hash2.containsKey("BEHAVES_LIKE") && lblAcctNo_Sanction_Disp.getText() != null && lblAcctNo_Sanction_Disp.getText().length() > 0) {
            String behavesLike = CommonUtil.convertObjToStr(hash2.get("BEHAVES_LIKE"));
            if (behavesLike.equals("OD") || behavesLike.equals("CC")) {
                observable.setUpdateAvailableBalance(true);
                txtLimit_SD.setEnabled(true);
                txtNoInstallments.setEnabled(true);
                //                    btnLTD.setEnabled(true);
                cboRepayFreq.setEnabled(true);
                //                    tdtFDate.setEnabled(true);
                //                    chkMoratorium_Given.setEnabled(true);
                //                    additionalSanctionEnableDisable(false);
                //                    additionalSanctionbtnEnableDisable(false);
                //                    rdoDP_YES.setVisible(true);
                //                    rdoDP_NO.setVisible(true);
                //                    lblDPLimit.setVisible(true);
                //                    rdoDP_YES.setEnabled(true);
                //                    rdoDP_NO.setEnabled(true);
                //                    lblDPLimit.setEnabled(true);
                //                    panInterest1
            } else {
                //                    rdoDP_YES.setVisible(false);
                //                    rdoDP_NO.setVisible(false);
                //                    lblDPLimit.setVisible(false);
            }
        }
        if (hash2.containsKey("BEHAVES_LIKE") && (!CommonUtil.convertObjToStr(hash2.get("BEHAVES_LIKE")).equals("OD")) && lblAcctNo_Sanction_Disp.getText() != null && lblAcctNo_Sanction_Disp.getText().length() > 0) {
            hash2.put("ACT_NUM", lblAcctNo_Sanction_Disp.getText());
            hash2.put("ACCT_NUM", lblAcctNo_Sanction_Disp.getText());
            //disbursement also over we have to check customer repaid or not  modifiy by abi 14-feb-09
            List lst = ClientUtil.executeQuery("checkTransaction", hash2);
            hash2 = (HashMap) lst.get(0);
            transCount = 0;
            transCount = CommonUtil.convertObjToInt(hash2.get("CNT"));
            //System.out.print("hash###2" + hash2);
            hash2.put("ACCT_NUM", lblAcctNo_Sanction_Disp.getText());
            lst = null;
            if (loanType.equals("LTD")) {
                lst = ClientUtil.executeQuery("getDepositLienAmount", hash2);
            }
            if (lst != null && lst.size() > 0) {
                lienAuthorized = true;
            }
            Date curr_dts = (Date) curr_dt.clone();
            //                Date repay_dt=DateUtil.getDateMMDDYYYY(tdtFacility_Repay_Date.getDateValue());
            //                if(transCount !=0 || (repay_dt !=null && DateUtil.dateDiff(repay_dt,curr_dts )>0) || observable.getShadowCredit()>0 || observable.getShadowDebit()>0 || observable.getClearBalance()<0 || lienAuthorized) {
            //                observable.setUpdateAvailableBalance(false);
            txtLimit_SD.setEnabled(false);
            //                btnLTD.setEnabled(false);
            txtNoInstallments.setEnabled(false);
            cboRepayFreq.setEnabled(false);
            //                tdtFDate.setEnabled(false);
            //                chkMoratorium_Given.setEnabled(false);
            observable.setPartReject("PARTILLY_REJECT");
            //                additionalSanctionEnableDisable(false);
            //                additionalSanctionbtnEnableDisable(false);
            //                cboProductId.setEnabled(false);
            //                cboTypeOfFacility.setEnabled(false);
            setAllFacilityDetailsEnableDisable(true);
            setFacilityBtnsEnableDisable(true);
            //                additionalSanctionbtnEnableDisable(true);
            //                setAllFacilityDetailsEnableDisable(false);

            //                if( observable.getShadowCredit()>0 || observable.getShadowDebit()>0){
            //                    additionalSanctionEnableDisable(false);
            //                    additionalSanctionbtnEnableDisable(false);
            //                }
            //                if(observable.getClearBalance()<0 ){
            //                    additionalSanctionEnableDisable(true);
            //                    additionalSanctionbtnEnableDisable(true);
            //                }else{
            //                    additionalSanctionEnableDisable(false);
            //                    additionalSanctionbtnEnableDisable(false);
            //                }


        } else {
            observable.setUpdateAvailableBalance(true);
            txtLimit_SD.setEnabled(true);
            //                    txtLimit_SD2.setEnabled(true);
            txtNoInstallments.setEnabled(true);
            //                    btnLTD.setEnabled(true);
            cboRepayFreq.setEnabled(true);
            //                    tdtFDate.setEnabled(true);
            //                    chkMoratorium_Given.setEnabled(true);
            //                    additionalSanctionEnableDisable(false);
            //                    additionalSanctionbtnEnableDisable(false);
            observable.setPartReject("");
            setAllFacilityDetailsEnableDisable(true);
            setFacilityBtnsEnableDisable(true);
            //                additionalSanctionbtnEnableDisable(true);

            String strFacilityType = getCboTypeOfFacilityKeyForSelected();
            setAllSecurityDetailsEnableDisable(false);
            if (!strFacilityType.equals(LOANS_AGAINST_DEPOSITS)) {
                setSecurityBtnsOnlyNewEnable();
            } else {
                setAllSecurityBtnsEnableDisable(false);
                setTotalMainAdditionalSanction();
            }
            strFacilityType = null;
            enableDisableGetIntFrom(true);
            setAllInterestDetailsEnableDisable(false);
            //                setAllRepaymentDetailsEnableDisable(false);
            setRepaymentNewOnlyEnable();
            //                setAllGuarantorDetailsEnableDisable(false);
            setGuarantorDetailsNewOnlyEnabled();
            setAllDocumentDetailsEnableDisable(false);
            setDocumentToolBtnEnableDisable(false);
            //                }
            String intGetFrom = CommonUtil.convertObjToStr(observable.getCbmIntGetFrom().getKeyForSelected());
            if ((intGetFrom.equals(PROD)) || (intGetFrom.equals(""))) {
                setAllInterestBtnsEnableDisable(false);
            } else {
                setInterestDetailsOnlyNewEnabled();
            }
            //            ClientUtil.enableDisable(panAccountDetails, true);
            disableLastIntApplDate();
            //                if (rdoSecurityDetails_Fully.isSelected()){
            //                    rdoSecurityDetails_FullyActionPerformed(null);
            //                }else if (rdoSecurityDetails_Partly.isSelected()){
            //                    rdoSecurityDetails_PartlyActionPerformed(null);
            //                }else if (rdoSecurityDetails_Unsec.isSelected()){
            //                    rdoSecurityDetails_UnsecActionPerformed(null);
            //                }

            //                if (chkGurantor.isSelected()){
            //                    chkGurantorActionPerformed(null);
            //                }else{
            //                    setAllGuarantorBtnsEnableDisable(false);
            //                }
            //
            //                if (chkInsurance.isSelected()){
            //                    chkInsuranceActionPerformed(null);
            //                }else{
            //                    //                    setAllInsuranceBtnsEnableDisable(false);
            //                    //                    setAllInsuranceDetailsEnableDisable(false);
            //                }
            setSanctionProductDetailsDisable();

            observableInt.setValByProdID();
        }
        //renewal od
        if (hash2.containsKey("BEHAVES_LIKE") && (!CommonUtil.convertObjToStr(hash2.get("BEHAVES_LIKE")).equals("OD")
                || CommonUtil.convertObjToStr(hash2.get("BEHAVES_LIKE")).equals("CC")) && lblAcctNo_Sanction_Disp.getText() != null && lblAcctNo_Sanction_Disp.getText().length() > 0) {
            if (DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(observable.getTdtTDate()), DateUtil.getDateWithoutMinitues((Date) curr_dt.clone())) >= 0) {
                if (accNumMap.containsKey(lblAcctNo_Sanction_Disp.getText()) && sanMousePress) {
                    //                        if(tblRepaymentCTable.getRowCount()>0){
                    sanMousePress = true;
                    //                            tblRepaymentCTableMousePressed();
                    //                            btnRepayment_DeleteActionPerformed();
                    //                            ClientUtil.displayAlert("Change the sanction Details so Create new Repayment schdule");
                    //                            tabLimitAmount.setSelectedIndex(8);

                    //                        }
                }
            }
        }
        //        btnSave1.setEnabled(false);
        //        btnSave1.setVisible(false);
        if (observable.getCboAccStatus().equals("Closed") && viewType.equals(CommonConstants.STATUS_AUTHORIZED)) {
            setAllClassificationDetailsEnableDisable(false);
        } else {
            setAllClassificationDetailsEnableDisable(true);
        }

        //        if (tblInterMaintenance.getSelectedRow()>=0) {
        //            tblinterestDetailsMousePressed();
        //        }
        //        if (tblRepaymentCTable.getSelectedRow()>=0) {
        //            tblRepaymentCTableMousePressed();
        //        }
        //        if (loanType.equals("OTHERS") && !viewType.equals(CommonConstants.STATUS_AUTHORIZED)) {
        //            observableSecurity.resetAllSecurityDetails();
        ////            observableSecurity.resetSecurityTableUtil();
        //            observableOtherDetails.resetOtherDetailsFields();
        //            observableRepay.resetAllRepayment();
        ////            observableRepay.resetRepaymentCTable();
        //            observableGuarantor.resetGuarantorDetails();
        ////            observableGuarantor.resetGuarantorCTable();
        //            observableDocument.resetAllDocumentDetails();
        ////            observableDocument.resetDocCTable();
        //            observableInt.resetAllInterestDetails();
        //            observableClassi.resetClassificationDetails();
        //            observable.ttNotifyObservers();
        //        }

        //        String mandatoryMessage="";
        //        mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panTableFields_SD);
        //        /* mandatoryMessage1 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        //        if (chkMoratorium_Given.isSelected() && (txtFacility_Moratorium_Period.getText().length() == 0)){
        //            TermLoanRB termLoanRB = new TermLoanRB();
        //            mandatoryMessage = mandatoryMessage + termLoanRB.getString("moratorium_Given_Warning");
        //            termLoanRB = null;
        //            }
        //        //ltd loan number not generator nut checking lien
        //        if(loanType.equals("LTD") && observable.getStrACNumber().length()==0) {
        //            String mainLimit=CommonUtil.convertObjToStr(txtLimit_SD.getText());
        //            if(mainLimitMarginValidation(mainLimit))
        //                return;
        //            if(checkInterestRateForLTD()){
        //                return;
        //        }
        //        }
        //        //for renew   od
        //        txtLimit_SDFocusLostOD(false);
        //        //
        //        //check sanctionDetails change or not if change delete repayment schedule
        //        if(sanctionDetailsBasedRepayment())
        //            return;
        //
        //        if (mandatoryMessage.length() > 0){
        //            displayAlert(mandatoryMessage);
        //        }else{
        //            //check repay detail delete or not
        //            if(!((observable.getTxtLimit_SD()).equals(txtLimit_SD.getText())) ||
        //            !((observable.getTxtNoInstallments()).equals(txtNoInstallments.getText())) ||
        //            !(((String)cboRepayFreq.getSelectedItem()).equals(observable.getCboRepayFreq() )) ||
        //            !((observable.getTdtFDate()).equals(tdtFDate.getDateValue()))){
        //
        //
        //                accNumMap.put(lblAcctNo_Sanction_Disp.getText(),lblAcctNo_Sanction_Disp.getText());
        //            }
        //            //end checking
        //            boolean periodFlag = false;
        //            boolean limitFlag = false;
        //            String message = new String();
        //            if (loanType.equals("OTHERS")) {
        //                if (!(cboRepayFreq.getSelectedItem().equals("User Defined") || cboRepayFreq.getSelectedItem().equals("Lump Sum")) && !observable.checkFacilityPeriod(txtNoInstallments.getText(), txtFacility_Moratorium_Period.getText())){
        //                    observable.decoratePeriod();
        //                    message = message.concat("The Limit Period must fall within "+observable.getMinDecLoanPeriod()+" and  "+observable.getMaxDecLoanPeriod());
        //                    periodFlag = false;
        //                }else if ((cboRepayFreq.getSelectedItem().equals("User Defined") || cboRepayFreq.getSelectedItem().equals("Lump Sum")) && !observable.checkFacilityPeriod(DateUtil.getDateMMDDYYYY(tdtFDate.getDateValue()), DateUtil.getDateMMDDYYYY(tdtTDate.getDateValue()))){
        //                    observable.decoratePeriod();
        //                    message = message.concat("The Limit Period must fall within "+observable.getMinDecLoanPeriod()+" and  "+observable.getMaxDecLoanPeriod());
        //                    periodFlag = false;
        //                }else{
        //                    periodFlag = true;
        //                }
        //            }else{
        //                periodFlag = true;
        //            }
        //            if (loanType.equals("OTHERS")) {
        //                if (!observable.checkLimitValue(txtLimit_SD.getText())) {
        //                    observable.setTxtLimit_SD("");
        //                    txtLimit_SD.setText(observable.getTxtLimit_SD());
        //                    message = message.concat("\nThe Limit value must fall within "+observable.getMinLimitValue().toString()+" and  "+observable.getMaxLimitValue().toString());
        //                    limitFlag = false;
        //                }else{
        //                    limitFlag = true;
        //                }
        //            }else{
        //                limitFlag = true;
        //            }
        //            if (!(periodFlag && limitFlag)){
        //                btnSave1Action();
        //            }else{
        //                displayAlert(message);
        //            }
        //            message = null;
        //        }
    }

    private void checkProductLevelInterestDetailsforLTD() {
        HashMap whereMap = new HashMap();
        whereMap.put("CATEGORY_ID", observableBorrow.getCbmCategory().getKeyForSelected());
        whereMap.put("AMOUNT", getBigDecimal(CommonUtil.convertObjToDouble(observable.getTxtLimit_SD()).doubleValue()));
        whereMap.put("PROD_ID", observable.getLblProductID_FD_Disp());
        //        whereMap.put("FROM_DATE", getTimestamp(DateUtil.getDateMMDDYYYY(tdtFDate.getDateValue())));
        //        whereMap.put("TO_DATE", getTimestamp(DateUtil.getDateMMDDYYYY(tdtTDate.getDateValue())));

    }

    private boolean sanctionDetailsBasedRepayment() {
        String acct_num = CommonUtil.convertObjToStr(lblAcctNo_Sanction_Disp.getText());
        if (!acct_num.equals("")) {
            StringBuffer msg = new StringBuffer();
            HashMap map = new HashMap();
            map.put(CommonConstants.MAP_WHERE, acct_num);
            List lst = null;
            if (alreadyChecked) {
                lst = ClientUtil.executeQuery("getSelectTermLoanSanctionFacilityTO.AUTHORIZE", map);
            }
            if (lst != null && lst.size() > 0) {
                boolean deleteRecord = false;
                TermLoanSanctionFacilityTO termLoanSanctionFacilityTO = (TermLoanSanctionFacilityTO) lst.get(0);
                if (termLoanSanctionFacilityTO.getLimit().doubleValue() != CommonUtil.convertObjToDouble(txtLimit_SD.getText()).doubleValue()) {
                    deleteRecord = true;
                    msg.append("Actual Limit     :" + termLoanSanctionFacilityTO.getLimit().doubleValue() + "\n"
                            + "Change Limit " + txtLimit_SD.getText() + "\n");
                }
                //                if(DateUtil.dateDiff(termLoanSanctionFacilityTO.getFromDt(),DateUtil.getDateMMDDYYYY(tdtFDate.getDateValue()))!=0){
                //                    deleteRecord=true;
                //                    msg.append("From Date was   :"+termLoanSanctionFacilityTO.getFromDt()+"\n"+
                //                    "Now it is Changed to   "+txtLimit_SD.getText()+"\n");
                //
                //                }
                if (termLoanSanctionFacilityTO.getNoInstall().doubleValue() != CommonUtil.convertObjToDouble(txtNoInstallments.getText()).doubleValue()) {
                    deleteRecord = true;
                    msg.append(" Installment  was   :" + termLoanSanctionFacilityTO.getNoInstall().doubleValue() + "\n"
                            + "Now it is Changed to   " + txtNoInstallments.getText() + "\n");

                }
                if (termLoanSanctionFacilityTO.getRepaymentFrequency().doubleValue() != CommonUtil.convertObjToDouble(((ComboBoxModel) cboRepayFreq.getModel()).getKeyForSelected()).doubleValue()) {
                    deleteRecord = true;
                    msg.append("Repayment Frequency  was   :" + setSanctionDetailsFrequency(termLoanSanctionFacilityTO.getRepaymentFrequency()) + "\n"
                            + "Now it is Changed to   " + CommonUtil.convertObjToStr(cboRepayFreq.getSelectedItem()) + "\n");

                }
                //                if(!CommonUtil.convertObjToStr(txtFacility_Moratorium_Period.getText()).equals("")){
                //                    if(termLoanSanctionFacilityTO.getNoMoratorium().doubleValue()!=CommonUtil.convertObjToDouble(txtFacility_Moratorium_Period.getText()).doubleValue()){
                //                        deleteRecord=true;
                //                        msg.append("Morotorium  was   :"+termLoanSanctionFacilityTO.getNoMoratorium().doubleValue()+"\n"+
                //                        "Now it is Changed to   "+txtFacility_Moratorium_Period.getText()+"\n");
                //
                //                    }
                //                }
                //                if( deleteRecord && loanType.equals("OTHERS")){
                //                    msg.append("Create a new Repayment Schedule and then save");
                //                    ClientUtil.showMessageWindow(""+msg);
                //                    dumRowRepay=0;
                //                    sanValueChanged=true;
                //                    if(tblRepaymentCTable.getRowCount()>0){
                //                        tblRepaymentCTableMousePressed();
                //                        btnRepayment_DeleteActionPerformed();
                //                        sanValueChanged=false;
                //                        alreadyChecked=false;
                //                        return true;
                //                    }
                //                }
            }
        }
        return false;
    }

    private String setSanctionDetailsFrequency(Double obj) {
        if (365 == obj.doubleValue()) {
            return "Yearly";
        }
        if (180 == obj.doubleValue()) {
            return "HalfYearly";
        }
        if (90 == obj.doubleValue()) {
            return "Quaterly";
        }
        if (30 == obj.doubleValue()) {
            return "Monthly";
        }
        if (15 == obj.doubleValue()) {
            return "Fortnight";
        } 
        if (0 == obj.doubleValue()) {// Added by nithya on 03-07-2019 for KD-546 New Gold Loan -45 days maturity type
            return "USER_DEFINED_GOLD_LOAN";
        }
        return "";
    }

    private void btnSave1Action() {
        updateOBFields();
        if ((observable.addSanctionFacilityTab(rowSanctionFacility, updateSanctionFacility, updateSanctionMain, rowSanctionMain, rowFacilityTabSanction)) == 1) {
            setAllSanctionFacilityEnableDisable(true);
        } else {
            // It will update the database tables based on the Account Number
            if (observable.getStrACNumber().length() > 0) {
                if (!btnFacilitySavePressed()) {
                    resetEnableDisableFieldsBasedOnSave1Action();
                }
            } else {
                resetEnableDisableFieldsBasedOnSave1Action();
            }
        }
        observable.ttNotifyObservers();
        //        if (loanType.equals("LTD")) btnSave2_SDActionPerformed(null); for testig by abi
    }

    private void resetEnableDisableFieldsBasedOnSave1Action() {
        observable.resetSanctionFacility();
        setSanctionFacilityBtnsEnableDisable(false);
        setAllSanctionFacilityEnableDisable(false);
        //        btnNew1.setEnabled(true);
        //        btnSave2_SD.setEnabled(true);
        rowSanctionFacility = -1;
        updateSanctionFacility = false;
        updateRecords = false;
        observableSecurity.resetAllSecurityDetails();
        observable.resetAllFacilityDetails();
        updateCboTypeOfFacility();
        resetTabsDependsOnAccountNumber();
        setAllFacilityDetailsEnableDisable(false);
        setFacilityBtnsEnableDisable(false);
        btnsDisableBasedOnAccountNumber();
    }
    private void btnNew1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNew1ActionPerformed
        // Add your handling code here:
        btnNew1Action();
    }//GEN-LAST:event_btnNew1ActionPerformed
    private void btnNew1Action() {
        // Facility Details CTable(Sanction Details Tab) New pressed
        enableControls = true;
        if (loanType.equals("LTD")) {
            //            if (tblSanctionDetails.getRowCount()>=1) {
            //                ClientUtil.showMessageWindow("More than one loan not allowed for a single borrower...");
            //                return;
            //            }
        }
        //        if(loanType.equals("LTD"))
        //            btnLTD.setEnabled(true);
        observable.resetSanctionFacility();
        observable.resetAllFacilityDetails();
        updateCboTypeOfFacility();
        resetTabsDependsOnAccountNumber();
        setAllFacilityDetailsEnableDisable(true);
        //        setFacilityBtnsEnableDisable(false);
        btnsDisableBasedOnAccountNumber();
        observable.setTdtFDate(DateUtil.getStringDate((Date) curr_dt.clone()));
        observable.setAccountOpenDate(DateUtil.getStringDate((Date) curr_dt.clone()));
        setDefaultValB4AcctCreation();
        observable.ttNotifyObservers();
        setSanctionFacilityBtnsEnableDisable(true);
        setAllSanctionFacilityEnableDisable(true);
        setAllClassificationDetailsEnableDisable(true);
        //        btnDelete1.setEnabled(false);
        rowSanctionFacility = -1;
        updateSanctionFacility = false;
        updateRecords = true;
        //        if (loanType.equals("LTD")) {
        //            ((ComboBoxModel) cboTypeOfFacility.getModel()).setKeyForSelected(LOANS_AGAINST_DEPOSITS);
        //            cboTypeOfFacility.setEnabled(false);
        //        }
        sandetail = true;
        enableDisableGetIntFrom(true);
        setRepaymentBtnsEnableDisable(true);
        //        ClientUtil.enableDisable(panClassDetails, true);
        //        btnSave1.setVisible(false);
        //        btnSave1.setEnabled(false);
        observableClassi.setClassifiDetails(CommonConstants.TOSTATUS_INSERT);
//        observableOtherDetails.setOtherDetailsMode(CommonConstants.TOSTATUS_INSERT);
    }
    private void btnDelete2_SDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete2_SDActionPerformed
        // Add your handling code here:
        btnDelete2_SDAction();
    }//GEN-LAST:event_btnDelete2_SDActionPerformed
    private void btnDelete2_SDAction() {
        // Sanction Details CTable(Sanction Details Tab) Delete pressed
        observable.deleteSanctionMain(rowSanctionMain);
        observable.resetSanctionMain();
        observable.resetSanctionFacilityTable();
        observable.resetSanctionFacility();
        observable.resetAllFacilityDetails();
        updateCboTypeOfFacility();
        resetTabsDependsOnAccountNumber();
        setSanctionFacilityBtnsEnableDisable(false);
        setAllSanctionFacilityEnableDisable(false);
        setSanctionMainBtnsEnableDisable(false);
        setAllSanctionMainEnableDisable(false);
        setAllFacilityDetailsEnableDisable(false);
        setFacilityBtnsEnableDisable(false);
        btnsDisableBasedOnAccountNumber();
        //        btnNew2_SD.setEnabled(true);
        rowSanctionMain = -1;
        observable.ttNotifyObservers();
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
    private void btnSave2_SDAction() {
        // Sanction Details CTable(Sanction Details Tab) Save pressed
        //check sanction no if change means we shoude update facilitydetails and sanction facility_details
        //        String newSanctionNo=CommonUtil.convertObjToStr(txtSanctionNo.getText());
        //        String sanctionSlNo=CommonUtil.convertObjToStr(txtSanctionSlNo.getText());
        //        if(!(sanctionSlNo.equals("") &&  newSanctionNo.equals("")))
        //            if(observable.getOldSanction_no() !=newSanctionNo)
        //                if(observable.checkAllfacilitySanctionnoUpdateDetails( newSanctionNo,sanctionSlNo))
        //                    updateSanctionMain=true;
        updateOBFields();
        //        if (tblSanctionDetails.getRowCount() > 0){
        if (updateSanctionMain == false) {
            observable.setSanctionNumber();
        }
        observable.addSanctionMainTab(rowSanctionMain, updateSanctionMain);
        setSanctionFacilityBtnsEnableDisable(false);
        setAllSanctionFacilityEnableDisable(false);
        setSanctionMainBtnsEnableDisable(false);
        setAllSanctionMainEnableDisable(false);
        observable.resetSanctionFacility();
        observable.resetSanctionMain();
        observable.resetSanctionFacilityTable();
        observable.resetAllFacilityDetails();
        updateCboTypeOfFacility();
        resetTabsDependsOnAccountNumber();
        setAllFacilityDetailsEnableDisable(false);
        setFacilityBtnsEnableDisable(false);
        btnsDisableBasedOnAccountNumber();
        //            btnNew2_SD.setEnabled(true);
        //        }else{
        //            observable.setSanctionTableWarningMessage();
        //        }
        observable.ttNotifyObservers();
    }
    private void btnNew2_SDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNew2_SDActionPerformed
        // Add your handling code here:
        //        if(loanType.equals("LTD") && tblSanctionDetails2.getRowCount()>0){
        //            ClientUtil.displayAlert("More than one loan not allowed for a single borrower...");
        //        return;
        //    }
        String cust_id = txtCustID.getText();
        if (cust_id != null && cust_id.length() > 0) {
            btnNew2_SDAction();
        } else {
            ClientUtil.displayAlert("Enter Borrower Details");
            return;
        }
    }//GEN-LAST:event_btnNew2_SDActionPerformed

    private void btnNew2_SDAction() {
        // Sanction Details CTable(Sanction Details Tab) New pressed
        observable.createSanctionMainRowObjects();
        observable.setStrRealSanctionNo("");
        observable.createTableUtilSanctionFacility();
        setSanctionMainBtnsEnableDisable(true);  //false changed as true by Rajesh
        setAllSanctionMainEnableDisable(true);
        setAllFacilityDetailsEnableDisable(false);
        //        btnNew1.setEnabled(true);
        //        btnNew2_SD.setEnabled(true);
        //        btnDelete2_SD.setEnabled(false);         // This line added by Rajesh
        rowSanctionMain = -1;
        rowSanctionFacility = -1;
        updateSanctionFacility = false;
        updateSanctionMain = false;
        //        observableBorrow.setCboCategory(CommonUtil.convertObjToStr(cboCategory.getSelectedItem()));
        updateOBFields();
        observable.resetSanctionMain();
        observable.setTdtSanctionDate(DateUtil.getStringDate((Date) curr_dt.clone()));
        observable.destroyCreateSanctionFacilityObjects();
        btnNew1Action();
        //        btnNew1.setVisible(false);
        //        btnSave1.setVisible(false);
        //        txtSanctionNo.requestFocus();
        //        txtSanctionSlNo.setText("1");
        observable.setTxtSanctionSlNo("1");
    }

    private void btnCustIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustIDActionPerformed
        // Add your handling code here:
        //        callView("CUSTOMER ID");
        String goldLoan = "";
        new CheckCustomerIdUI(this);       
        if(txtCustID.getText()!=null&&txtCustID.getText().length()>0){
            txtCustID.setEnabled(false);
            //Added by sreekrishnan
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                HashMap suspenseMap = new HashMap();
                suspenseMap.put("CUST_ID", txtCustID.getText());
                List susList =  ClientUtil.executeQuery("getSuspenseAccountOfCustomer", suspenseMap);
                if (susList != null && susList.size() > 0) {
                    ClientUtil.showMessageWindow("Liability occured for this Customer!");
                }
            }
        }else
        {
            txtCustID.setEnabled(true); 
        }
        setNomineeDetails();
        HashMap contains = setCustPhoneNUmber();
        //System.out.println("eeeeeeeeeeeeeeeee" + contains);
        if (!contains.isEmpty()) {
            txtCustMobNo.setText("" + CommonUtil.convertObjToStr(contains.get("PHONE_NUMBER")));
        } else {
            txtCustMobNo.setText("");
        }
    }//GEN-LAST:event_btnCustIDActionPerformed
    public void insertCustTableRecords(HashMap hash) {
        viewType = "CUSTOMER ID";
        if (hash.containsKey("STATUS") && hash.get("STATUS").toString().equals("CLOSED")) {
            ClientUtil.showMessageWindow("Closed Member Cannot Continue further");
            ClientUtil.enableDisable(this, false);
            btnnewActionPerformed();
        } else {
            txtAccountNo.setText(CommonUtil.convertObjToStr(hash.get("MEMBER NO")));
            fillData(hash);
            if (TrueTransactMain.MULTI_SHARE_ALLOWED.equals("Y")) {
              highPriorityShareWarning();
            }
        }
    }
    
    private void highPriorityShareWarning() {
        HashMap whereMap = new HashMap();
        whereMap.put("CUST_ID", txtCustID.getText());
        whereMap.put("SHARE_ACCT_NO", txtAccountNo.getText());
        List shareLst = ClientUtil.executeQuery("getCustomerHighPriorityShare", whereMap);
        if (shareLst != null && shareLst.size() > 0) {
            whereMap = (HashMap)shareLst.get(0);
            if(whereMap.containsKey("SHARE_ACCT_NO") && whereMap.get("SHARE_ACCT_NO") != null && CommonUtil.convertObjToStr(whereMap.get("SHARE_ACCT_NO")).length() > 0){
                ClientUtil.showMessageWindow("High Priority Share Exists for the customer - " + whereMap.get("SHARE_ACCT_NO"));
            }            
        }
    }

    private void validateConstitutionCustID() {
        String strConstitution = CommonUtil.convertObjToStr(cboConstitution.getSelectedItem());
        // If the constitution is blank then allow to select the customer
        if (strConstitution.length() != 0 && tblBorrowerTabCTable.getRowCount() > 0) {
            observableBorrow.validateConstitutionCustID(strConstitution, CommonUtil.convertObjToStr(tblBorrowerTabCTable.getValueAt(0, 2)));
            cboConstitution.setSelectedItem(observableBorrow.getCboConstitution());
            //            observableBorrow.getCbmConstitution().setKeyForSelected(observableBorrow.getCbmConstitution().getDataForKey(strConstitution));
        }
    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
        nomineeUi.resetNomineeData();
        nomineeUi.resetTable();
        //        eachProdId = "";
    }//GEN-LAST:event_btnCloseActionPerformed

    private void removeObservable() {
        observable.deleteObserver(this);
        observableBorrow.deleteObserver(this);
//        observableComp.deleteObserver(this);
        observableSecurity.deleteObserver(this);
        observableRepay.deleteObserver(this);
//        observableGuarantor.deleteObserver(this);
        observableInt.deleteObserver(this);
        observableClassi.deleteObserver(this);
    }

    private void destroyOBObjects() {
        observableBorrow = null;
        observableClassi = null;
//        observableComp = null;
//        observableGuarantor = null;
        observableInt = null;
        observable = null;
        observableRepay = null;
        observableSecurity = null;
    }
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // Add your handling code here:
        HashMap reportParamMap = new HashMap();
        com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        rnw = 0;
        nomineeUi.resetTable();
        nomineeUi.resetNomineeData();
        nomineeUi.resetNomineeTab();
        nomineeUi.disableNewButton(false);
        btncancelActionPerformed();
        txtNextAccNo.setText("");
//        table.setVisible(false);
//        srpChargeDetails.repaint();
    }//GEN-LAST:event_btnCancelActionPerformed
    //    private void resetAllExtendedTab(){
    //        authSignUI.resetAllFieldsInAuthTab();
    //        poaUI.getPowerOfAttorneyOB().resetAllFieldsInPoA();
    //    }

    private void btncancelActionPerformed() {
        //        if( viewType.equals("Edit") ||  viewType.equals("Delete") ){
        //        if(observable.getAuthorizeStatus() !=null ) //commented for checking edit lock by using borrow no but it shoude be edit lock by loan no  dont delete
        //        super.removeEditLock(lblBorrowerNo_2.getText());
        //        }
        btnSecurityAdd.setEnabled(false);
        setFocusFirstTab();
        transDetailsUI.setTransDetails(null, null, null);
        //transDetailsUI.setSourceFrame(null);
        removeEditLockScreen(lblAcctNo_Sanction_Disp.getText());
        observable.resetForm();
        observable.setRenewalYesNo(false);
        tabLimitAmount.remove(panBorrowRenewalCompanyDetails);

        tabLimitAmount.remove(panTranDetView);
        //        chkGurantorActionPerformeds();
        //        rdoFullySecuredActionPerformed();
        //        rdoPartlySecuredActionPerformed();
        //        lblTotalLimitAmt.setText("");
        //        resetAllExtendedTab();
        observable.resetAllFacilityDetails();
        updateCboTypeOfFacility();
        observable.destroyObjects();
        observable.createObject();
        outStandingAmtRepayment = false;
        deletescreenLock();
        //        authSignUI.setUpdateModeAuthorize(false);
        //        poaUI.setUpdateModePoA(false);
        setMode(ClientConstants.ACTIONTYPE_CANCEL);
        ClientUtil.enableDisable(this, false);// Disables the panel...
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        observable.setStatus();
        //        authSignUI.setLblStatus(observable.getLblStatus());
        //        poaUI.setLblStatus(observable.getLblStatus());
        setButtonEnableDisable();
        setAllTableBtnsEnableDisable(false); // To disable the Tool buttons for all the CTable
        setbtnCustEnableDisable(false);
        setAllTablesEnableDisable(true);
        btnsDisableBasedOnAccountNumber();
        resetUiSecurityDetails();
        txtMarginAmt.setText("");
        txtEligibleLoan.setText("");
        //        txtAvalSecVal.setText("");
//        txtAppraiserId.setText("");
        cboAppraiserId.setSelectedItem("");
        txtInter.setText("");
        txtPenalInter.setText("");
        observable.setInterest("");
        observable.setPenalInterest("");
        cboPurposeOfLoan.setSelectedItem("");
        txtAccountNo.setText("");
        //        txtSanctionLimitValue.setText("");
        lblAppraiserNameValue.setText("");
        //        lblSecurityProdIdValue.setText("");
        observableSecurity.setLblProdId_Disp("");
        //        lblDocumentProdIdValue.setText("");
        observableSecurity.setTotalSecurityValue("");
        observableSecurity.setTxtMargin("");
        observableSecurity.setTxtMarginAmt("");
        observableSecurity.setTxtEligibleLoanAmt("");
        observableSecurity.setTxtAppraiserId("");
        txtCustMobNo.setText("");
        //        eachProdId = "";
//        btnAppraiserId.setEnabled(false);
        setModified(false);
        rowSanctionFacility = -1;
        updateSanctionFacility = false;
        updateRecords = false;
        viewType = "";
        date = null;
        sandetail = false;
        enableControls = false;
        //        lblAppraiserFeeValue.setText("");
        //        lblServiceTaxValue.setText("");
        txtMargin.setText("");
        txtMarginAmt.setText("");
        txtEligibleLoan.setText("");
//        panChargeDetails.removeAll();
//        panChargeDetails.setVisible(false);
//        srpChargeDetails.revalidate();
        btnView.setEnabled(true);
        chrgTableEnableDisable();
        observable.setRenewalAcctNum("");
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
        btnMembershipLia.setEnabled(false);
        if (loanType.equals("Renewal")) {
            btnRenew.setVisible(true);
        } else {
            btnRenew.setVisible(false);
        }
        cboGoldLoanProd.setSelectedItem("");
        panChargeDetails.removeAll();
        panChargeDetails.setVisible(false);
         
        TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(tabTransView);
            com.see.truetransact.clientutil.TableModel tableModel = new com.see.truetransact.clientutil.TableModel();
            tableModel.setHeading(new ArrayList());
            tableModel.setData(new ArrayList());
            tableModel.fireTableDataChanged();
            tableSorter.setModel(tableModel);
            tableSorter.fireTableDataChanged();
            
            tabTransView.setModel(tableSorter);
            tabTransView.revalidate();
        //HashMap viewMap = new HashMap(); 
      // ArrayList heading = observable.populateData(viewMap, tabTransView);
      txtCustID.setEnabled(true);
      btnRepayShedule.setEnabled(false);
      //Added by sreekrishnan
      tdtEmiDate.setEnabled(false);
      tdtEmiDate.setVisible(false);
      lblEmiDate.setVisible(false);
      tdtEmiDate.setDateValue("");
      lblServiceTaxVal.setText("");// KD 263
      lblPhoto.setIcon(null);//28-10-2019
      lblRenewPhoto.setIcon(null);
      
      
      serviceTaxApplMap = null;
        taxListForGoldLoan = null;
        serviceTaxIdMap = null;
        System.out.println("Execute here ..");
        serviceTax_Map = null;
        observable.setServiceTax_Map(null);
        lblServiceTaxOpeningVal.setText("0.00");
      
      
      
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
    }//GEN-LAST:event_btnDeleteActionPerformed
    private void btndeleteActionPerformed() {
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        observableSecurity.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        observableBorrow.setLoanActionType(ClientConstants.ACTIONTYPE_DELETE);
        setAllTableBtnsEnableDisable(false); // To disable the Tool buttons for all the CTable
        nomineeUi.setActionType(DELETE);
        popUp("Delete");
        //        authSignUI.setAuthTabBtnEnableDisable(false);
        setAllTablesEnableDisable(true); // To disable the All tables...
        rowSanctionFacility = -1;
        updateSanctionFacility = false;
        updateRecords = false;
//        btnAppraiserId.setEnabled(false);
    }

    private void setAllTablesEnableDisable(boolean val) {
        //        tblSanctionDetails.setEnabled(val);
    }
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        transNew = false;
        btneditActionPerformed();
//        btnAppraiserId.setEnabled(false);
        txtGrossWeight.setEnabled(true);
        txtNetWeight.setEnabled(true);
        cboPurityOfGold.setEnabled(true);
        setEnableJewalDetails(true);
        setStockDetailsTabDisable(false); //28-10-2019
//      btnSave.setEnabled(true);
        nomineeUi.enableDisableNominee_SaveDelete();
        ClientUtil.enableDisable(panBorrowCompanyDetails, false);
        txtNextAccNo.setText("");
        txtNextAccNo.setEnabled(false);
        populateTransData();
    }//GEN-LAST:event_btnEditActionPerformed
    private void setEnableJewalDetails(boolean flag) {
        cboItem.setEnabled(flag);
        txtQty.setEnabled(flag);
        txtSecurityRemarks.setEnabled(flag);
        btnSecurityAdd.setEnabled(flag);
        txtNoOfPacket.setEnabled(flag);
        btnSecurityAdd.setEnabled(flag);
    }

    private void btneditActionPerformed() {
        observableBorrow.btnPressed = true;
        observable.createObject();
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        observableSecurity.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        observableBorrow.setLoanActionType(ClientConstants.ACTIONTYPE_EDIT);
           tabLimitAmount.add(panTranDetView, "Transaction Details");

        popUp("Edit");
        //        authSignUI.setAuthEnableDisable(false);
        //        authSignUI.setAuthOnlyNewBtnEnable();
        //        authSignUI.setAllAuthInstEnableDisable(false);
        //        authSignUI.setAuthInstOnlyNewBtnEnable();
        //        poaUI.setAllPoAEnableDisable(false);
        setSanctionFacilityBtnsEnableDisable(false);
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
        //btnSave.setEnabled(false);
        if (observable.isRenewalYesNo()) {
            ////System.out.println("checkLimitChangeValue.." + checkLimitChangeValue + "from ui" + txtRenewalLimit_SD.getText());
            ////System.out.println("checkLimitChange...." + checkLimitChange);
            if (checkLimitChangeValue.equals(txtRenewalLimit_SD.getText()) && checkLimitChange == false) {
                txtRenewalLimit_SDFocusLost(null);
                ////System.out.println("txtLimit_SD.getText().." + txtRenewalLimit_SD.getText());
            }
            if (cboRenewalAppraiserId.getSelectedIndex() <= 0) {
                ClientUtil.showAlertWindow("Renewal Appraiser id should not be empty !!! ");
                return;
            }
            if (cboRenewalPurityOfGold.getSelectedIndex() <= 0) {
                ClientUtil.showAlertWindow("Purity should not be empty !!! ");
                return;
            }

        } else {
            if (cboPurityOfGold.getSelectedIndex() <= 0) {
                ClientUtil.showAlertWindow("Purity should not be empty !!! ");
                return;
            }
        }
        
        if (chkMobileBankingTLAD.isSelected() == true && txtMobileNo.getText().length() == 0) {
            ClientUtil.displayAlert("Mobile no should not be empty!!!");
            return;
         }
        rnw = 0;
        //added by rishad 22/07/2015 for avoiding doubling issue
        CommonUtil comm = new CommonUtil();
        final JDialog loading = comm.addProgressBar();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws InterruptedException /** Execute some operation */
            {
                if (viewType.equals("Delete")) {
                    saveAction();
                } else {
                    btnsaveActionPerformed();
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
        txtNextAccNo.setText("");
        txtNextAccNo.setEnabled(false);
        btnSecurityAdd.setEnabled(false);       
        
    }//GEN-LAST:event_btnSaveActionPerformed
    private void btnsaveActionPerformed() {
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            HashMap hashmap = new HashMap();
            if (tblBorrowerTabCTable != null && tblBorrowerTabCTable.getRowCount() > 0) {
                int row = tblBorrowerTabCTable.getRowCount();
                for (int i = 0; i < row; i++) {
                    String custid = CommonUtil.convertObjToStr(tblBorrowerTabCTable.getValueAt(i, 1));
                    hashmap.put("CUST_ID", custid);
                    hashmap.put("MEMBER_NO", custid);
                    List lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashmap);
                    if (lst1 != null && lst1.size() > 0) {
                        ClientUtil.displayAlert("Customer is death marked please select another customerId");
                        return;
                    }
                }

            }
            if (nomineeUi.getTblRowCount() > 0) {
                for (int i = 0; i < nomineeUi.getTblRowCount(); i++) {
                    NomineeOB observable = nomineeUi.getNomineeOB();
                    observable.populateNomineeTab(i);
                    hashmap.put("CUST_ID", observable.getLblCustNo());
                    hashmap.put("MEMBER_NO", observable.getLblCustNo());
                    List lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashmap);
                    if (lst1 != null && lst1.size() > 0) {
                        ClientUtil.displayAlert("Customer is death marked please select another customerId");
                        return;
                    }
                }
            }
        }
        //observable.setAuthorizeNo();
        //        if (observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT && loanType.equals("LTD")) {
        //            btnFacilitySavePressed();
        //            saveAction();
        //        } else
        ////System.out.println("renewal1111111" + observable.isRenewalYesNo());
        String mandatoryMessage = "";
        mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panSanctionDetails_Sanction);
        /* mandatoryMessage1 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        //        if (chkMoratorium_Given.isSelected() && (txtFacility_Moratorium_Period.getText().length() == 0)){
        //            TermLoanRB termLoanRB = new TermLoanRB();
        //            mandatoryMessage = mandatoryMessage + termLoanRB.getString("moratorium_Given_Warning");
        //            termLoanRB = null;
        //        }
        //ltd loan number not generator nut checking lien
        if (loanType.equals("LTD") && observable.getStrACNumber().length() == 0) {
            String mainLimit = CommonUtil.convertObjToStr(txtLimit_SD.getText());
            if (mainLimitMarginValidation(mainLimit)) {
                return;
            }
            if (checkInterestRateForLTD()) {
                return;
            }
        }
        //for renew   od
        txtLimit_SDFocusLostOD(false);
        //
        //check sanctionDetails change or not if change delete repayment schedule
        //        if(sanctionDetailsBasedRepayment())
        //            return;

        //        mandatoryMessage = mandatoryMessage + new MandatoryCheck().checkMandatory(getClass().getName(), panSanctionDetails_Sanction);
        /* mandatoryMessage1 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/

        //        mandatoryMessage = mandatoryMessage + new MandatoryCheck().checkMandatory(getClass().getName(), panSanctionDetails_Mode);
        /* mandatoryMessage2 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        //        int sanctionRow=tblSanctionDetails.getSelectedRow();
        //        if ((sanctionRow !=-1  || enableControls)&& mandatoryMessage.length() > 0){
        //        displayAlert(mandatoryMessage);
        //        return;
        //        }else{
        //check repay detail delete or not
        //            if(!((observable.getTxtLimit_SD()).equals(txtLimit_SD.getText())) ||
        //            !((observable.getTxtNoInstallments()).equals(txtNoInstallments.getText())) ||
        //            !(((String)cboRepayFreq.getSelectedItem()).equals(observable.getCboRepayFreq() )) ||
        //            !((observable.getTdtFDate()).equals(tdtFDate.getDateValue()))){
        //
        //
        //                accNumMap.put(lblAcctNo_Sanction_Disp.getText(),lblAcctNo_Sanction_Disp.getText());
        //            }
        //end checking
        //            boolean periodFlag = false;
        //            boolean limitFlag = false;
        //            if (loanType.equals("OTHERS")) {
        //                if (!(cboRepayFreq.getSelectedItem().equals("User Defined") || cboRepayFreq.getSelectedItem().equals("Lump Sum")) && !observable.checkFacilityPeriod(txtNoInstallments.getText(), txtFacility_Moratorium_Period.getText())){
        //                    observable.decoratePeriod();
        //                    mandatoryMessage = mandatoryMessage.concat("The Limit Period must fall within "+observable.getMinDecLoanPeriod()+" and  "+observable.getMaxDecLoanPeriod());
        //                    periodFlag = false;
        //                }else if ((cboRepayFreq.getSelectedItem().equals("User Defined") || cboRepayFreq.getSelectedItem().equals("Lump Sum")) && !observable.checkFacilityPeriod(DateUtil.getDateMMDDYYYY(tdtFDate.getDateValue()), DateUtil.getDateMMDDYYYY(tdtTDate.getDateValue()))){
        //                    observable.decoratePeriod();
        //                    mandatoryMessage = mandatoryMessage.concat("The Limit Period must fall within "+observable.getMinDecLoanPeriod()+" and  "+observable.getMaxDecLoanPeriod());
        //                    periodFlag = false;
        //                }else{
        //                    periodFlag = true;
        //                }
        //            }else{
        //                periodFlag = true;
        //            }
        //            if (loanType.equals("OTHERS")) {
        //                if (!observable.checkLimitValue(txtLimit_SD.getText())) {
        //                    observable.setTxtLimit_SD("");
        //                    txtLimit_SD.setText(observable.getTxtLimit_SD());
        //                    mandatoryMessage = mandatoryMessage.concat("\nThe Limit value must fall within "+observable.getMinLimitValue().toString()+" and  "+observable.getMaxLimitValue().toString());
        //                    limitFlag = false;
        //                }else{
        //                    limitFlag = true;
        //                }
        //            }else{
        //                limitFlag = true;
        //            }
        //        }


        mandatoryMessage = mandatoryMessage + new MandatoryCheck().checkMandatory(getClass().getName(), panSanctionDetails_Table);
        /* mandatoryMessage1 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/

        //                mandatoryMessage = mandatoryMessage + new MandatoryCheck().checkMandatory(getClass().getName(), panFDAccount);
        //        /* mandatoryMessage2 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        //
        //                mandatoryMessage = mandatoryMessage + new MandatoryCheck().checkMandatory(getClass().getName(), panFDDate);
        //        /* mandatoryMessage3 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/

        //        mandatoryMessage = mandatoryMessage + isJointAcctHavingAtleastOneCust();
        //        if (productBasedValidation()){
        //            mandatoryMessage = mandatoryMessage + new TermLoanMRB().getString("rdoMultiDisburseAllow_Yes");
        //        }
        //
        //        if (observable.getStrACNumber().length() > 0){
        ////            mandatoryMessage = mandatoryMessage + new MandatoryCheck().checkMandatory(getClass().getName(), panProd_IM);
        //            mandatoryMessage = mandatoryMessage + isInterestDetailsExistForThisAcct();
        //            mandatoryMessage = mandatoryMessage + validateOtherDetailsMandatoryFields();
        //        }
   //     cboAppraiserId.setSelectedItem(CommonUtil.convertObjToStr(com.see.truetransact.ui.TrueTransactMain.USERINFO.get("EMPLOYEE_ID")));
        //System.out.println("cboAppraiserId --XXX--- :"+cboAppraiserId.getSelectedItem());
        String apprId = CommonUtil.convertObjToStr(observable.getCbmAppraiserId().getKeyForSelected());
        ////System.out.println("observable.getCbmAppraiserId() :");
        //System.out.println("apprId :"+apprId);
        if (mandatoryMessage.length() > 0) {
            displayAlert(mandatoryMessage);
        } else if (rdoPriority.isSelected() == false && rdoNonPriority.isSelected() == false) {
            ClientUtil.showAlertWindow("Priority or NonPriroity should not be empty !!! ");
        } else if (cboPurposeOfLoan.getSelectedIndex() <= 0) {
            ClientUtil.showAlertWindow("Purpose of loan should not be empty !!! ");
        } else if (cboAppraiserId.getSelectedIndex() <= 0 && CommonUtil.convertObjToStr(observable.getCbmAppraiserId().getKeyForSelected()).length() <=0) {
            ClientUtil.showAlertWindow("Appraiser id should not be empty !!! ");
        } else if (txtAreaParticular.getText().length() == 0) {
            ClientUtil.showAlertWindow("Particular should not be empty !!! ");
        } else if (txtCustID.getText().length() == 0) {
            ClientUtil.showAlertWindow("Customer Id should not be empty !!!");
        } else if (txtMargin.getText().length() == 0) {
            ClientUtil.showAlertWindow("Margin should not be empty !!!");
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && CommonUtil.convertObjToDouble(txtLimit_SD.getText()).doubleValue() > CommonUtil.convertObjToDouble(txtEligibleLoan.getText()).doubleValue()) {
            ClientUtil.showAlertWindow("Sanction Limit Amount should be less than the Eligible Loan Amount !!!");
        } else if (txtLimit_SD.getText().length() == 0) {
            ClientUtil.showAlertWindow("Limit Amount Should Not be Empty !!!");
        } else if (CommonUtil.convertObjToDouble(txtInter.getText()).doubleValue() == 0 && CommonUtil.convertObjToDouble(txtPenalInter.getText()).doubleValue() == 0) {
            ClientUtil.showAlertWindow("Rate of Interest and Penal Interest not set for this Product !!! ");
        } else if (txtNoInstallmentsFocusLost()) {
            return;
        } else if ((((ComboBoxModel) cboConstitution.getModel()).getKeyForSelected()).equals(JOINT_ACCOUNT) && tblBorrowerTabCTable.getRowCount() == 1) {
            ClientUtil.showAlertWindow("Select the Joint Account Holder !!! ");
        } else {
            if (observable.isRenewalYesNo()) {
                if (CommonUtil.convertObjToDouble(txtRenewalLimit_SD.getText()).doubleValue() > CommonUtil.convertObjToDouble(txtRenewalEligibleLoan.getText()).doubleValue()) {
                    ClientUtil.showAlertWindow("Sanction Renewal Limit Amount should be less than the Eligible Renewal Loan Amount !!!");
                    return;
                }
            }
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.isRenewalYesNo()) {
                ////System.out.println("renewal22222222222222" + observable.isRenewalYesNo());
                updateOBFields();
                EMI_CalculateActionPerformed(false);
            } else {
                updateOBFields();
            }

            finalizeCharges();
            if (transNew || observable.isRenewalYesNo()) {
                insertTransactionPart();
                if (observable.getAllLoanAmount() == null) {
                    observable.setAllLoanAmount(new HashMap());
                }
                //System.out.println("txtCharges.getText()====" + txtCharges.getText());
                observable.setCharges(CommonUtil.convertObjToDouble(txtCharges.getText()));
                observable.setAllLoanAmount(transDetailsUI.getTermLoanCloseCharge());
            }
            if(chktrans!=true)
                {
            saveAction();
            chkok=false;
                }
            //            displayTransDetail();
             else{
             // Commented by nithya on 17-07-2019 for KD 557 - Gold loan Renewal did not create transaction
//              int yesNo2 = 0;
//                String[] options = {"Yes", "No"};
//                yesNo2 = COptionPane.showOptionDialog(null, "Do you want to create loan account without transaction?", CommonConstants.WARNINGTITLE,
//                        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
//                        null, options, options[0]);
//                if (yesNo2 == 0) {
//                    com.see.truetransact.clientutil.ttrintegration.TTIntegration ttIntgration = null;
//                    saveAction();  
//                    chkok=false; 
//               
//                }
//                else{
//                ClientUtil.showAlertWindow("Transaction processing aborted");
//                chkok=true; 
//                }
            
             }

        }
    }

    private void finalizeCharges() {
        HashMap chargeMap = new HashMap();
        if (chargelst != null && chargelst.size() > 0) {
            for (int i = 0; i < chargelst.size(); i++) {
                String desc = "";
                chargeMap = (HashMap) chargelst.get(i);
                desc = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_ID"));
                ////System.out.println("$#@@$ accHead" + accHead);
                for (int j = 0; j < table.getRowCount(); j++) {
                    ////System.out.println("$#@@$ accHead inside table " + table.getValueAt(j, 1));
                    if (CommonUtil.convertObjToStr(table.getValueAt(j, 1)).equals(desc) && !((Boolean) table.getValueAt(j, 0)).booleanValue()) {
                        chargelst.remove(i--);
                    } else {
                        if (CommonUtil.convertObjToStr(table.getValueAt(j, 1)).equals(desc)/* && CommonUtil.convertObjToStr(table.getValueAt(j, 4)).equals("Y")*/) {
                            String chargeAmt = CommonUtil.convertObjToStr(table.getValueAt(j, 3));
                            chargeMap.put("CHARGE_AMOUNT", String.valueOf(chargeAmt));
                        }
                    }
                }
            }
            ////System.out.println("#$#$$# final chargelst:" + chargelst);
            observable.setChargelst(chargelst);
        }

    }

    private void insertTransactionPart() {
        String prodId = CommonUtil.convertObjToStr(((ComboBoxModel) cboGoldLoanProd.getModel()).getKeyForSelected());
        //System.out.println("prodIdprodIdprodId" + prodId);
        HashMap supMap = new HashMap();
        supMap.put("PROD_ID", prodId);
        List lstSupName = ClientUtil.executeQuery("getProdIdForSelectedItem", supMap);
        HashMap supMap1 = new HashMap();
        double maxPay = 0.0;
        if(lstSupName!=null && lstSupName.size()>0){
        	supMap1 = (HashMap) lstSupName.get(0);
        }
        maxPay=CommonUtil.convertObjToDouble(supMap1.get("MAX_AMT_OF_CASH_PAYMENT"));
        double loanAmount=CommonUtil.convertObjToDouble(txtLimit_SD.getText());
        HashMap singleAuthorizeMap = new HashMap();
        java.util.ArrayList arrList = new java.util.ArrayList();
        HashMap authDataMap = new HashMap();
        if (observable.getStrACNumber().length() > 0) {
            authDataMap.put("ACCT_NUM", observable.getStrACNumber());
        } else {
            authDataMap.put("ACCT_NUM", lblAcctNo_Sanction_Disp.getText());
        }
        arrList.add(authDataMap);
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT && observable.getAvailableBalance() > 0)
                || (observable.isRenewalYesNo() && observable.getTxtRenewalLimit_SD().length() > 0)) {
             if(loanAmount>maxPay){
            //        if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE && observable.getAvailableBalance() >0){
            String[] debitType = {"Transfer"};
            String[] obj4 = {"Yes", "No"};
//            int option3 = COptionPane.showOptionDialog(null,("Do you want to make Transaction?"), ("Transaction"),
//            COptionPane.YES_NO_CANCEL_OPTION,COptionPane.QUESTION_MESSAGE,null,obj4,obj4[0]);
            int option3 = 0;
            boolean isloop = false;
            String transType = "";
            if (option3 == 0) {
                do {
                    transType = (String) COptionPane.showInputDialog(null, "Select Transaction Type", "Transaction type", COptionPane.QUESTION_MESSAGE, null, debitType, "");
//                    if(!(CommonUtil.convertObjToStr(transType.toUpperCase()).equals("CASH") || CommonUtil.convertObjToStr(transType.toUpperCase()).equals("TRANSFER"))){
                    if (transType == null) {
                        if (observable.isRenewalYesNo()) { 
                            isloop = true;
                            ClientUtil.showMessageWindow("Account Being Renewed Please Select Cash or Transfer ");
                        }
                    } else {
                        isloop = false;
                    }
                } while (isloop);
                authDataMap.put("TRANSACTION_PART", "TRANSACTION_PART");
                authDataMap.put("TRANS_TYPE", transType.toUpperCase());
//                authDataMap.put("APPRAISER_AMT","100");
//                authDataMap.put("SERVICE_TAX_AMT","50");
                if (CommonUtil.convertObjToStr(transType.toUpperCase()).equals("CASH")) {
                    boolean flag = true;
                  
                    do {
                        String tokenNo = COptionPane.showInputDialog(this, resourceBundle.getString("REMARK_CASH_TRANS"));
                        if (tokenNo != null && tokenNo.length() > 0) {
                            flag = tokenValidation(tokenNo);
                            chktrans=false;
                            chkok=false;
                        } 
//                        else {
//                             ClientUtil.showMessageWindow("Transaction Not Created");
//                             authDataMap.remove("TRANSACTION_PART");
//                             observable.setTransactionMap(null);
//                            authDataMap=null;
//                                    flag = true;
//                                    chktrans = true;
//                                     return;
//                        }
                        if (flag == false) {
                            ClientUtil.showAlertWindow("Token is invalid or not issued for you. Please verify.");
                        } else {
                            authDataMap.put("TOKEN_NO", tokenNo);
                        }
                        //                        } else {
                        //                            ClientUtil.showMessageWindow("Transaction Not Created");
                        //                            flag = true;
                        //                            authDataMap.remove("TRANSACTION_PART");
                        //                        }
                    } while (!flag);
                } else if (CommonUtil.convertObjToStr(transType.toUpperCase()).equals("TRANSFER")) {
                    boolean flag = true;
                    double serviceTax = 0.0;
                    double val = CommonUtil.convertObjToDouble(transDetailsUI.calculatetotalRecivableAmountForRenewalLoan());//observable.getRen
                    double totCharge = CommonUtil.convertObjToDouble(txtCharges.getText());
                    if (CommonUtil.convertObjToStr(lblServiceTaxVal.getText()).length() > 0) {
                        serviceTax = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(lblServiceTaxVal.getText()));
                    }
                    double newval = val + totCharge + serviceTax;
                    double limtamt = CommonUtil.convertObjToDouble(txtRenewalLimit_SD.getText());
                    boolean omitmap = true;
                    if (observable.isRenewalYesNo() && newval == limtamt) {
                        omitmap = false;
                    }
                    if (omitmap) {
                        do {
                            String sbAcNo = null;
                            String prodType = null;
                            HashMap acctDetailsMap = firstEnteredActNo();
                            if (acctDetailsMap != null && acctDetailsMap.size() > 0 && acctDetailsMap.containsKey("ACT_NUM") && acctDetailsMap.containsKey("PROD_TYPE")) {
                                sbAcNo = CommonUtil.convertObjToStr(acctDetailsMap.get("ACT_NUM"));
                                prodType = CommonUtil.convertObjToStr(acctDetailsMap.get("PROD_TYPE"));
                            }
                            //String sbAcNo = firstEnteredActNo();
                            if (sbAcNo != null && sbAcNo.length() > 0) {
                                flag = checkingActNo(sbAcNo,prodType);
                                if (flag == false && finalChecking == false) {
                                    ClientUtil.showAlertWindow("Account No is invalid, Please enter correct no");
                                } else {
                                    authDataMap.put("CR_ACT_NUM", sbAcNo);
                                    authDataMap.put("CR_PROD_TYPE", cr_prod_type);
                                    chkok = false;
                                }
                                finalChecking = false;
                            } else {
                                ClientUtil.showMessageWindow("Transaction Not Created.");
                                flag = true;
                                authDataMap.remove("TRANSACTION_PART");
                                observable.setTransactionMap(null);//20-03-2014
                                authDataMap = null;
                                sbAcNo = "";
                                chktrans = true;
                                return;
                            }
                        } while (!flag);
                    } else {
                        authDataMap.put("CR_ACT_NUM", "CR_ACT_NUM");
                        chkok = false;
                        finalChecking = false;
                    }
                } 
                else if (observable.isRenewalYesNo()) {
                }
                //System.out.println("authDataMap  ====================="+authDataMap);
                observable.setTransactionMap(authDataMap);
            }
             }
             else{
                   //        if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE && observable.getAvailableBalance() >0){
            String[] debitType = {"Cash", "Transfer"};
            String[] obj4 = {"Yes", "No"};
//            int option3 = COptionPane.showOptionDialog(null,("Do you want to make Transaction?"), ("Transaction"),
//            COptionPane.YES_NO_CANCEL_OPTION,COptionPane.QUESTION_MESSAGE,null,obj4,obj4[0]);
            int option3 = 0;
            boolean isloop = false;
            String transType = "";
            if (option3 == 0) {
                do {
                    transType = (String) COptionPane.showInputDialog(null, "Select Transaction Type", "Transaction type", COptionPane.QUESTION_MESSAGE, null, debitType, "");
//                    if(!(CommonUtil.convertObjToStr(transType.toUpperCase()).equals("CASH") || CommonUtil.convertObjToStr(transType.toUpperCase()).equals("TRANSFER"))){
                    if (transType == null) {
                        if (observable.isRenewalYesNo()) { 
                            isloop = true;
                            ClientUtil.showMessageWindow("Account Being Renewed Please Select Cash or Transfer ");
                        }
                    } else {
                        isloop = false;
                    }
                } while (isloop);
                authDataMap.put("TRANSACTION_PART", "TRANSACTION_PART");
                authDataMap.put("TRANS_TYPE", transType.toUpperCase());
//                authDataMap.put("APPRAISER_AMT","100");
//                authDataMap.put("SERVICE_TAX_AMT","50");
                if (CommonUtil.convertObjToStr(transType.toUpperCase()).equals("CASH")) {
                    boolean flag = true;
                  
                    do {
                        String tokenNo = COptionPane.showInputDialog(this, resourceBundle.getString("REMARK_CASH_TRANS"));
                        if (tokenNo != null && tokenNo.length() > 0) {
                            flag = tokenValidation(tokenNo);
                            chktrans=false;
                            chkok=false;
                        } 
//                        else {
//                             ClientUtil.showMessageWindow("Transaction Not Created");
//                             authDataMap.remove("TRANSACTION_PART");
//                             observable.setTransactionMap(null);
//                            authDataMap=null;
//                                    flag = true;
//                                    chktrans = true;
//                                     return;
//                        }
                        if (flag == false) {
                            ClientUtil.showAlertWindow("Token is invalid or not issued for you. Please verify.");
                        } else {
                            authDataMap.put("TOKEN_NO", tokenNo);
                        }
                        //                        } else {
                        //                            ClientUtil.showMessageWindow("Transaction Not Created");
                        //                            flag = true;
                        //                            authDataMap.remove("TRANSACTION_PART");
                        //                        }
                    } while (!flag);
                } else if (CommonUtil.convertObjToStr(transType.toUpperCase()).equals("TRANSFER")) {
                    boolean flag = true;
                    double serviceTax = 0.0;
                    double val = CommonUtil.convertObjToDouble(transDetailsUI.calculatetotalRecivableAmountForRenewalLoan());//observable.getRen
                    if (CommonUtil.convertObjToStr(lblServiceTaxVal.getText()).length() > 0) {
                        serviceTax = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(lblServiceTaxVal.getText()));
                    }
                    double totCharge = CommonUtil.convertObjToDouble(txtCharges.getText());
                    double newval = val + totCharge + serviceTax;
                    double limtamt = CommonUtil.convertObjToDouble(txtRenewalLimit_SD.getText());
                    boolean omitmap = true;
                    if (observable.isRenewalYesNo() && newval == limtamt) {
                        omitmap = false;
                    }
                  if(omitmap){
                    do {
                         String sbAcNo = null;
                         String prodType = null;
                         HashMap acctDetailsMap = firstEnteredActNo();
                        if (acctDetailsMap != null && acctDetailsMap.size() > 0 && acctDetailsMap.containsKey("ACT_NUM") && acctDetailsMap.containsKey("PROD_TYPE")) {
                            sbAcNo = CommonUtil.convertObjToStr(acctDetailsMap.get("ACT_NUM"));
                            prodType = CommonUtil.convertObjToStr(acctDetailsMap.get("PROD_TYPE"));
                        }
                        if (sbAcNo != null && sbAcNo.length() > 0) {
                            flag = checkingActNo(sbAcNo,prodType);
                            if (flag == false && finalChecking == false) {
                                ClientUtil.showAlertWindow("Account No is invalid, Please enter correct no");
                            } else {
                                authDataMap.put("CR_ACT_NUM", sbAcNo);
                                authDataMap.put("CR_PROD_TYPE", cr_prod_type);
                                chkok=false;
                            }
                            finalChecking = false;
                        } else {
                            ClientUtil.showMessageWindow("Transaction Not Created.");
                            flag = true;
                            authDataMap.remove("TRANSACTION_PART");
                            observable.setTransactionMap(null);//20-03-2014
                            authDataMap=null;
                            sbAcNo="";
                            chktrans= true;
                            return;
                        }
                    } while (!flag);
                    } else {
                        authDataMap.put("CR_ACT_NUM", "CR_ACT_NUM");
                         chkok=false;
                         finalChecking = false;
                    }
                }
                else if (observable.isRenewalYesNo()) {
                }
                //System.out.println("authDataMap  ====================="+authDataMap);
                observable.setTransactionMap(authDataMap);
            }

             }
        }
    }
    public void populateTransData() {
        //        updateOBFields();
        ////System.out.println("11111111111111@@@@");
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        //        observable.setTxtLodgementId(CommonUtil.convertObjToStr(hash.get()
        viewMap.put(CommonConstants.MAP_NAME, "getAllBillsTransactions");
        whereMap.put("LODGEMENT_ID", observable.getStrACNumber());
         whereMap.put("TRANS_DT", curr_dt);
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        try {
            ArrayList heading = observable.populateData(viewMap, tabTransView);
            //System.out.println("headingheading"+heading);
            heading = null;
        } catch( Exception e ) {
            System.err.println( "Exception " + e.toString() + "Caught" );
            e.printStackTrace();
        }
        viewMap = null;
        whereMap = null;
    }
    private void displayTransDetail() {
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            String displayStr = "";
            String oldBatchId = "";
            String newBatchId = "";
            String transType = "";
            int CreditcashCount = 0;
            int DebitcashCount = 0;
            boolean transferFlag=false;
            String debitCashSingleId="";//20-03-2014
            String creditCashSingleId="";
            String transferSingleTransId = ""; //KD-3277 -
            String actNum = CommonUtil.convertObjToStr(observable.getStrACNumber());
            HashMap transMap = new HashMap();
            HashMap transTypeMap = new HashMap();
            HashMap transIdMap = new HashMap();
            int cashCount = 0;
            int transferCount = 0;
            transMap.put("LOAN_NO", observable.getStrACNumber());
            transMap.put("CURR_DT", curr_dt);
              if (observable.isRenewalYesNo() == true) {
                   transMap.put("LOAN_NO", oldAccountNum);
              }
            //added by Chithra on 24-07-2014
            populateTransData();
            //end-------------
            transMap.put("AUTH_STATUS", "AUTH_STATUS");
            List lst = ClientUtil.executeQuery("getTransferTransLoanAuthDetails", transMap);
            
            
            if (oldGoldLoanNo.length() > 0 || observable.isRenewalYesNo()) {
                if(oldGoldLoanNo.length() > 0){
                  transMap.put("LOAN_NO", oldGoldLoanNo);
                }else{
                  transMap.put("LOAN_NO", observable.getStrACNumber());  
                }
                List newNoLst = ClientUtil.executeQuery("getTransferTransLoanAuthDetails", transMap);
                if (newNoLst != null && newNoLst.size() > 0) {
                    lst.addAll(newNoLst);
                }
                if (observable.isRenewalYesNo() == true) {
                    transMap.put("LOAN_NO", oldAccountNum);
                }
            }      
            
            
            if (lst != null && lst.size() > 0) {
                displayStr += "Transfer Transaction Details...\n";
                for (int i = 0; i < lst.size(); i++) {
                    transMap = (HashMap) lst.get(i);
                    displayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Batch Id : " + transMap.get("BATCH_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if (actNum != null && !actNum.equals("")) {
                        displayStr += "   Account No : " + transMap.get("ACT_NUM")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    } else {
//                        displayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
//                                + "   Interest Amount : " + transMap.get("AMOUNT") + "\n";
                            
                          displayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                                + "   " + transMap.get("AMOUNT") + "\n";
                    }
                    ////System.out.println("#### :" + transMap);
                    oldBatchId = newBatchId;
                    transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "TRANSFER");
                    transferSingleTransId = CommonUtil.convertObjToStr(transMap.get("SINGLE_TRANS_ID"));
                }
                transferCount++;
                transType = "TRANSFER";
                transferFlag=true;
            }
            if (observable.isRenewalYesNo() == true) {
                ////System.out.println("isRenewalYesNo" + observable.isRenewalYesNo());
                actNum = CommonUtil.convertObjToStr(observable.getStrACNumber());
                ////System.out.println("actNumactNum" + actNum + "dghfdh" + oldAccountNum);
                transMap = new HashMap();
                transMap.put("LOAN_NO", oldAccountNum);//observable.getStrACNumber());// oldAccountNum);
                transMap.put("AUTH_STATUS", "AUTH_STATUS");
                oldAccountNum = "";
                transMap.put("CURR_DT", curr_dt);
                lst = ClientUtil.executeQuery("getCashTransLoanAuthDetails", transMap);
                
                if (oldGoldLoanNo.length() > 0 || observable.isRenewalYesNo()) {
                    if (oldGoldLoanNo.length() > 0) {
                        transMap.put("LOAN_NO", oldGoldLoanNo);
                    } else {
                        transMap.put("LOAN_NO", observable.getStrACNumber());
                    }
                    List newNoLst = ClientUtil.executeQuery("getCashTransLoanAuthDetails", transMap);
                    if (newNoLst != null && newNoLst.size() > 0) {
                        lst.addAll(newNoLst);
                    }
                }
                
                
            } else {
                actNum = CommonUtil.convertObjToStr(observable.getStrACNumber());
                ////System.out.println("actNumactNum" + actNum);
                transMap = new HashMap();
                transMap.put("LOAN_NO", actNum);
                transMap.put("AUTH_STATUS", "AUTH_STATUS");
                // oldAccountNum="";
                transMap.put("CURR_DT", curr_dt);
                lst = ClientUtil.executeQuery("getCashTransLoanAuthDetails", transMap);
                
                System.out.println("oldGoldLoanNo in cash :: " + oldGoldLoanNo);
                if (oldGoldLoanNo.length() > 0 || observable.isRenewalYesNo()) {
                    if (oldGoldLoanNo.length() > 0) {
                        transMap.put("LOAN_NO", oldGoldLoanNo);
                    } else {
                        transMap.put("LOAN_NO", observable.getStrACNumber());
                    }
                    List newNoLst = ClientUtil.executeQuery("getCashTransLoanAuthDetails", transMap);
                    if (newNoLst != null && newNoLst.size() > 0) {
                        lst.addAll(newNoLst);
                    }
                }
                
                
            }
//            actNum = CommonUtil.convertObjToStr(observable.getStrACNumber());
//            transMap = new HashMap();
//            transMap.put("LOAN_NO",actNum);
//            transMap.put("CURR_DT", curr_dt);
//            lst = ClientUtil.executeQuery("getCashTransLoanAuthDetails", transMap);
            if (lst != null && lst.size() > 0) {
                displayStr += "Cash Transaction Details...\n";
                for (int i = 0; i < lst.size(); i++) {
                    transMap = (HashMap) lst.get(i);
                    displayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if (actNum != null && !actNum.equals("")) {
                        displayStr += "   Account No :  " + transMap.get("ACT_NUM")
                                + "   Amount :  " + transMap.get("AMOUNT") + "\n";
                    } else {//20-03-2014
                        displayStr += "   Ac Hd Desc :  " + transMap.get("AC_HD_ID");
                        if (CommonUtil.convertObjToStr(transMap.get("INSTRUMENT_NO2")).equals("LOAN_ACT_CLOSING_CHARGE")) {
                            displayStr += "   Account closing  Charges :  " + transMap.get("AMOUNT") + "\n";
                        }
                        if (CommonUtil.convertObjToStr(transMap.get("INSTRUMENT_NO2")).equals("LOAN_POSTAGE_CHARGES")) {
                            displayStr += "   Postage Charges :  " + transMap.get("AMOUNT") + "\n";
                        }
                        if (CommonUtil.convertObjToStr(transMap.get("INSTRUMENT_NO2")).equals("LOAN_NOTICE_CHARGES")) {
                            displayStr += "   Notice Charges :  " + transMap.get("AMOUNT") + "\n";
                        }
                        if (CommonUtil.convertObjToStr(transMap.get("INSTRUMENT_NO2")).equals("LOAN_PENAL_INT")) {
                            displayStr += "   Penal Amount :  " + transMap.get("AMOUNT") + "\n";
                        }
                        if (CommonUtil.convertObjToStr(transMap.get("INSTRUMENT_NO2")).equals("LOAN_INTEREST")) {
                            displayStr += "   Interest Amount :  " + transMap.get("AMOUNT") + "\n";
                        }
                        if (CommonUtil.convertObjToStr(transMap.get("INSTRUMENT_NO2")).equals("LOAN_PRINCIPAL")) {
                            displayStr += "   Principal Amount :  " + transMap.get("AMOUNT") + "\n";
                        }
                        if(CommonUtil.convertObjToStr(transMap.get("INSTRUMENT_NO2")).equals("LOAN_ACT_OPENING_CHARGE"))
                            displayStr += "   Account opening Charges :  " + transMap.get("AMOUNT") + "\n";
                        if (CommonUtil.convertObjToStr(transMap.get("INSTRUMENT_NO2")).equals("LOAN_OTHER_CHARGES")) {
                            displayStr += "   Other Charges :  " + transMap.get("AMOUNT") + "\n";
                        }
                        if (CommonUtil.convertObjToStr(transMap.get("INSTRUMENT_NO2")).equals("SERVICETAX_CHARGE")) {
                            displayStr += "   GST :  " + transMap.get("AMOUNT") + "\n";
                        }
                    }
                    transTypeMap.put(transMap.get("SINGLE_TRANS_ID"), transMap.get("TRANS_TYPE"));
                    transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "CASH");
                    //System.out.println("transTypeMap === =" + transTypeMap);
                    if (CommonUtil.convertObjToStr(transMap.get("TRANS_TYPE")).equals("DEBIT")) {
                        DebitcashCount++;
                        debitCashSingleId=CommonUtil.convertObjToStr(transMap.get("SINGLE_TRANS_ID"));
                        //System.out.println("DebitcashCount===" + DebitcashCount +""+debitCashSingleId);
                    }
                    if (CommonUtil.convertObjToStr(transMap.get("TRANS_TYPE")).equals("CREDIT")) {
                        CreditcashCount++;
                        creditCashSingleId=CommonUtil.convertObjToStr(transMap.get("SINGLE_TRANS_ID"));
                        //System.out.println("CreditcashCount===" + CreditcashCount +""+creditCashSingleId);
                    }
                }
                cashCount++;
                transType = "CASH";
            }
            if (!displayStr.equals("")) {
                ClientUtil.showMessageWindow("" + displayStr);
                /**************************************/
                //            TTIntegration ttIntgration = null;
                //            HashMap paramMap = new HashMap();
                //            paramMap.put("TransId", transId);
                //            paramMap.put("TransDt", observable.getCurrDt());
                //            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                //            ttIntgration.setParam(paramMap);
                //            ttIntgration.integrationForPrint("ReceiptPayment", false);
                TTIntegration ttIntgration = null;
                HashMap printParamMap = new HashMap();
                printParamMap.put("TransDt", curr_dt);
                printParamMap.put("BranchId", ProxyParameters.BRANCH_ID);
                System.out.println("transIdMap final:: " + transIdMap+" transferSingleTransId :: " + transferSingleTransId);
                Object keys1[] = transIdMap.keySet().toArray();
                //System.out.println("keys1.length ===" + keys1.length + " keys1==" + keys1);
                int yesNo = 0;
                if (transType.equals("TRANSFER")|| transferFlag) {
                    //printParamMap.put("TransId", keys1[0]);
                    printParamMap.put("TransId", transferSingleTransId);
                    ttIntgration.setParam(printParamMap);
                    if(chkok!=true){
                    String[] options = {"Yes", "No"};
                        if (viewType == AUTHORIZE) {
                            yesNo = 1;
                        } else {
                            yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                                    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                                    null, options, options[0]);
                        }
                    if (yesNo == 0) {
                        ttIntgration.integrationForPrint("ReceiptPayment");
                    }
                    
                }
                }

                if (transType.equals("CASH")) {
                    if (CreditcashCount > 0) {
                        printParamMap.put("TransId",creditCashSingleId);//20-03-2014
                        ttIntgration.setParam(printParamMap);
                        int yesNoCredit = 0;
                        if(chkok!=true){
                        String[] options = {"Yes", "No"};
                            if (viewType == AUTHORIZE) {
                                yesNoCredit = 1;
                            } else {
                                yesNoCredit = COptionPane.showOptionDialog(null, "Do you want to print Receipt?", CommonConstants.WARNINGTITLE,
                                        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                                        null, options, options[0]);
                            }
                        if (yesNoCredit == 0) {
                            ttIntgration.integrationForPrint("CashReceipt", false);
                        }
                    }
                    }
                    if (DebitcashCount > 0) {
                        printParamMap.put("TransId",debitCashSingleId);//20-03-2014
                        ttIntgration.setParam(printParamMap);
                        if(chkok!=true){
                        int yesNoDebit = 0;
                        String[] options = {"Yes", "No"};
                            if (viewType == AUTHORIZE) {
                                yesNoDebit = 1;
                            } else {
                                yesNoDebit = COptionPane.showOptionDialog(null, "Do you want to print Voucher?", CommonConstants.WARNINGTITLE,
                                        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                                        null, options, options[0]);
                            }
                        if (yesNoDebit == 0) {
                            ttIntgration.integrationForPrint("CashPayment", false);
                        }
                    }
                    }

                }

            }

        }

       oldGoldLoanNo = "";
    }

    private void upToCustPhone() {
        HashMap hash = new HashMap();
        if (observable.isRenewalYesNo()) {
            hash.put("PHONE_NUMBER", CommonUtil.convertObjToStr(txtRenewCustMobNo.getText()));
        } else {
            hash.put("PHONE_NUMBER", CommonUtil.convertObjToStr(txtCustMobNo.getText()));
        }
        hash.put("CUST_ID", CommonUtil.convertObjToStr(txtCustID.getText()));
        ////System.out.println("HH" + hash);
        ClientUtil.execute("updateCustPhone", hash);
    }

    private void insToCustPhone() {
        HashMap hash = new HashMap();
        if (observable.isRenewalYesNo()) {
            hash.put("PHONE_NUMBER", CommonUtil.convertObjToStr(txtRenewCustMobNo.getText()));
        } else {
            hash.put("PHONE_NUMBER", CommonUtil.convertObjToStr(txtCustMobNo.getText()));
        }
        hash.put("CUST_ID", CommonUtil.convertObjToStr(txtCustID.getText()));
        hash.put("PHONE_TYPE_ID", "LAND LINE");
        hash.put("AREA_CODE", "0000");
        hash.put("ADDR_TYPE", "HOME");
        hash.put("PHONE_ID", CommonUtil.convertObjToInt("1"));
        hash.put("STATUS", "CREATED");
        hash.put("STATUS_BY", "SYSADMIN");
        hash.put("STATUS_DT", ClientUtil.getCurrentDate());
        ////System.out.println("HH" + hash);
        ClientUtil.execute("insertCustPhone", hash);
    }

    private void saveAction() {
        if (!viewType.equals("Delete")) {
            updateOBFields();
        }
        phoneNoContailsOrNot();
        if (observable.getStrACNumber().length() == 0 && loanType.equals("LTD")) {
            observable.setLTDSecurityDetails();
        }
       
        final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panBorrowCompanyDetails);
        StringBuffer strBAlert = new StringBuffer();
        if (mandatoryMessage.length() > 0) {
            //                displayAlert(mandatoryMessage);
            strBAlert.append(mandatoryMessage + "\n");
        }
        if (observable.getResult() != ClientConstants.ACTIONTYPE_DELETE) {
            //__ To Check if the Total Share of the Nominee(s) is 100% or not...

            String alert = nomineeUi.validateData();
            if (!alert.equalsIgnoreCase("")) {
                strBAlert.append(alert);
            }
        }
    
        observable.doAction(nomineeUi.getNomineeOB(), 1);
        /*if (observable.isRenewalYesNo()) {//bb1//Commented By Kannan AR Ref abi DAO side updation will happen On 16-Jun-2017
            HashMap val = new HashMap();
            val.put("ACT_NUM", renewalAccNo);
            val.put("PARTICULARS", txtRenewalAreaParticular.getText());
            val.put("APP_ID", CommonUtil.convertObjToStr(observable.getCbmRenewalAppraiserId().getKeyForSelected()));
            val.put("TOTAL", txtRenewalSecurityValue.getText());
            ClientUtil.execute("updateSecurityDetails", val);//bb1
        }
        if (!observable.isRenewalYesNo()&& viewType.equalsIgnoreCase("EDIT")) {//bb1
            HashMap val = new HashMap();
            val.put("ACT_NUM", lblAcctNo_Sanction_Disp.getText());
            val.put("PARTICULARS", txtAreaParticular.getText());
            ClientUtil.execute("updateSecurityDetails", val);//bb1
        }*/
        renewalAccNo = "";
        //        super.removeEditLock(lblBorrowerNo_2.getText()); remove edit lock by abi for authorize only remove the lock
        if (observable.getResult() == ClientConstants.ACTIONTYPE_FAILED) {
            observable.setResultStatus();
            return;
        } else {
            if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().containsKey("ACCTNO")) {
                String actNum = (String) observable.getProxyReturnMap().get("ACCTNO");
                ////System.out.println("observable.getProxyReturnMap()>>>>>>>" + observable.getProxyReturnMap());
                if(chkok!=true){
                int yesNo = 0;
                String[] options = {"Yes", "No"};
                yesNo = COptionPane.showOptionDialog(null, "Do you want to print GoldBond?", CommonConstants.WARNINGTITLE,
                        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                        null, options, options[0]);
                ////System.out.println("#$#$$ yesNo : " + yesNo);
                if (yesNo == 0) {
                    com.see.truetransact.clientutil.ttrintegration.TTIntegration ttIntgration = null;
                    HashMap reportTransIdMap = new HashMap();
                    reportTransIdMap.put("Act_Num", actNum);
                    ttIntgration.setParam(reportTransIdMap);
                    String transType = "";
                    ttIntgration.integrationForPrint("Goldbond");
                }
            }

                HashMap transTypeMap = new HashMap();
                HashMap transMap = new HashMap();
                HashMap transCashMap = new HashMap();
                transCashMap.put("BATCH_ID", actNum);
                transCashMap.put("TRANS_DT", curr_dt);
                transCashMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                HashMap transIdMap = new HashMap();
                List list = ClientUtil.executeQuery("getTransferDetails", transCashMap);
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        transMap = (HashMap) list.get(i);
                        transIdMap.put(transMap.get("BATCH_ID"), "TRANSFER");
                    }
                }
                list = ClientUtil.executeQuery("getCashDetails", transCashMap);
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        transMap = (HashMap) list.get(i);
                        transIdMap.put(transMap.get("TRANS_ID"), "CASH");
                        transTypeMap.put(transMap.get("TRANS_ID"), transMap.get("TRANS_TYPE"));
                    }
                }
                //purpose mantis id =6367
                /*yesNo = 0;
                String[] voucherOptions = {"Yes", "No"};
                yesNo = COptionPane.showOptionDialog(null, "Do you want to print Voucher?", CommonConstants.WARNINGTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                null, voucherOptions, voucherOptions[0]);
                if (yesNo == 0) {
                com.see.truetransact.clientutil.ttrintegration.TTIntegration ttIntgration = null;
                HashMap paramMap = new HashMap();
                paramMap.put("TransDt", curr_dt);
                paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                Object keys[] = transIdMap.keySet().toArray();
                for (int i = 0; i < keys.length; i++) {
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
                }*/
            }
            if (transNew || observable.isRenewalYesNo()) {
                displayTransDetail();
            }
        }
        observable.resetForm();
        deletescreenLock();
        //        lblTotalLimitAmt.setText("");
        //        resetAllExtendedTab();
        observable.resetAllFacilityDetails();
        updateCboTypeOfFacility();
        ClientUtil.enableDisable(this, false);
        //        setButtonEnableDisable();test by abi
        setAllTableBtnsEnableDisable(false); // To disable the Tool buttons for the CTable
        setbtnCustEnableDisable(false);  // To disable the Customer Buttons
        observable.setResultStatus();
        //        authSignUI.setLblStatus(observable.getLblStatus());
        //        poaUI.setLblStatus(observable.getLblStatus());
        observable.destroyObjects();
        observable.createObject();
        observable.ttNotifyObservers();
        rowSanctionFacility = -1;
        sandetail = false;
        enableControls = false;
        txtMargin.setText("");
        txtMarginAmt.setText("");
        txtEligibleLoan.setText("");
        txtCustMobNo.setText("");
        //        txtAvalSecVal.setText("");
        //        txtAppraiserId.setText("");
        txtInter.setText("");
        txtPenalInter.setText("");
        observable.setInterest("");
        observable.setPenalInterest("");
        cboPurposeOfLoan.setSelectedItem("");
        txtAccountNo.setText("");
        
        serviceTaxApplMap = null;
        taxListForGoldLoan = null;
        serviceTaxIdMap = null;
        System.out.println("Execute here ..");
        serviceTax_Map = null;
        observable.setServiceTax_Map(null);
        lblServiceTaxOpeningVal.setText("0.00");
        
        lblAppraiserNameValue.setText("");
        observableSecurity.setLblProdId_Disp("");
        observableSecurity.setTotalSecurityValue("");
        observableSecurity.setTxtMargin("");
        observableSecurity.setTxtMarginAmt("");
        observableSecurity.setTxtEligibleLoanAmt("");
        observableSecurity.setTxtAppraiserId("");
        nomineeUi.resetTable();
        nomineeUi.resetNomineeData();
        nomineeUi.getNomineeOB().ttNotifyObservers();
        nomineeUi.disableNewButton(false);
        //        btnAppraiserId.setEnabled(false);
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            setModified(false);
            alreadyChecked = true;
            btncancelActionPerformed();
            panChargeDetails.setVisible(false);
        }
        updateRecords = false;
    }

    private boolean checkForSecurityValue() {
        //        ////System.out.println("@@@@ tblSecurityTable.getRowCount() : "+tblSecurityTable.getRowCount());
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

    private boolean checkFieldsWhenMainSavePressed() {
        if (!((observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) || (viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT)))) {
            final String mandatoryMessage1 = new MandatoryCheck().checkMandatory(getClass().getName(), panBorrowProfile_CustName);
            /* mandatoryMessage length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
            String mandatoryMessage2 = "";
            String mandatoryMessage3 = "";
            String mandatoryMessage5 = "";
            String mandatoryMessage6 = "";
            String mandatoryMessage7 = isJointAcctHavingAtleastOneCust();
            String mandatoryMessage8 = "";
            if (observable.getStrACNumber().length() > 0) {
                //                mandatoryMessage3 = new MandatoryCheck().checkMandatory(getClass().getName(), panProd_IM);
                //                mandatoryMessage5 = new MandatoryCheck().checkMandatory(getClass().getName(), panFDAccount);
                //                mandatoryMessage6 = new MandatoryCheck().checkMandatory(getClass().getName(), panFDDate);
                if (loanType.equals("OTHERS")) {
                    mandatoryMessage8 = isInterestDetailsExistForThisAcct();
                }
            }
            //            if (rdoSecurityDetails_Fully.isSelected() && observableSecurity.chkForSecValLessThanLimiVal(txtLimit_SD.getText())){
            //                mandatoryMessage2 = resourceBundle.getString("securityValueWarning");
            //            }
            final String mandatoryMessage4 = new MandatoryCheck().checkMandatory(getClass().getName(), panBorrowProfile_CustName);
            /* mandatoryMessage4 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
            if (loanType.equals("OTHERS")) {
                if (mandatoryMessage1.length() > 0 || mandatoryMessage2.length() > 0 || mandatoryMessage3.length() > 0 || mandatoryMessage4.length() > 0 || mandatoryMessage5.length() > 0 || mandatoryMessage6.length() > 0 || mandatoryMessage7.length() > 0 || mandatoryMessage8.length() > 0) {
                    displayAlert(mandatoryMessage1 + mandatoryMessage2 + mandatoryMessage3 + mandatoryMessage4 + mandatoryMessage5 + mandatoryMessage6 + mandatoryMessage7 + mandatoryMessage8);
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isTablesInEditMode(boolean isMainSave) {
        if (observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) {
            return true;
        }
        StringBuffer stbWarnMsg = new StringBuffer("");
        //        if (authSignUI.isUpdateModeAuthorize()){
        //            stbWarnMsg.append(resourceBundle.getString("authSignatoryEditWarning"));
        //            stbWarnMsg.append("\n");
        //        }
        //        if (authSignUI.isUpdateModeAuthorizeInst()){
        //            stbWarnMsg.append(resourceBundle.getString("authSignatoryInstEditWarning"));
        //            stbWarnMsg.append("\n");
        //        }
        //        if (poaUI.isUpdateModePoA()){
        //            stbWarnMsg.append(resourceBundle.getString("poaEditWarning"));
        //            stbWarnMsg.append("\n");
        //        }
        if (updateRepayment) {
            stbWarnMsg.append(resourceBundle.getString("repaymentEditWarning"));
            stbWarnMsg.append("\n");
        }
        //        if (isMainSave && updateSanctionFacility){
        //            stbWarnMsg.append(resourceBundle.getString("santionFacilityEditWarning"));
        //            stbWarnMsg.append("\n");
        //        }
        if (updateDocument) {
            stbWarnMsg.append(resourceBundle.getString("documentDetailsEditWarning"));
            stbWarnMsg.append("\n");
        }
        if (updateInterest) {
            stbWarnMsg.append(resourceBundle.getString("interestDetailsEditWarning"));
            stbWarnMsg.append("\n");
        }
        if (updateGuarantor) {
            stbWarnMsg.append(resourceBundle.getString("guarantorDetailsEditWarning"));
            stbWarnMsg.append("\n");
        }
        if (updateSecurity) {
            stbWarnMsg.append(resourceBundle.getString("securityDetailsEditWarning"));
            stbWarnMsg.append("\n");
        }
        if (stbWarnMsg.length() > 0) {
            displayAlert(stbWarnMsg.toString());
            return false;
        } else {
            return true;
        }
    }

    private boolean repayTableNotNull() {
        //        String behaves=getCboTypeOfFacilityKeyForSelected();//System.out.print("repaytable###"+behaves);
        //        if(behaves !=null && (!(behaves.equals("OD") || behaves.equals("CC"))))
        //            if ( tblRepaymentCTable.getRowCount() < 1 ){ //&&  tblRepaymentCTable.getRowCount() < 1
        //                String strWarning =  resourceBundle.getString("repayfreqtable");
        //                displayAlert(strWarning);
        //                return false;
        //            }
        return true;
    }

    private boolean allCTablesNotNull() {
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
        if (!strWarning.equals("")) {
            displayAlert(strWarning);
            strWarning = null;
            return false;
        }
        strWarning = null;
        return true;
    }

    private boolean productBasedValidation() {
        boolean isNotSelected = false;
        String strFacilityType = getCboTypeOfFacilityKeyForSelected();
        if (!(strFacilityType.equals("CC") || strFacilityType.equals("OD"))) {
            //            if (!(rdoMultiDisburseAllow_No.isSelected() || rdoMultiDisburseAllow_Yes.isSelected())){
            //                isNotSelected = true;
            //            }
        }
        return isNotSelected;
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        viewType = "";  //Added By Suresh R on 14-Jun-2019 (Without Mandatory Fields Save Allowed) KDSA-520 : Gold Loan Save And Authorization Completed While Security Details Failed To Insert Properly
        //Once Seleccted Delete Button, Then if select NEW Button after Click Top Save Button (Without entering Any Fields) Loan account was generating. Because View Type was Delete. So we eed to Reset the View Type.
        if (!CommonUtil.convertObjToStr(TrueTransactMain.selBranch).equals("") && CommonUtil.convertObjToStr(TrueTransactMain.selBranch).length()>0 && 
        !TrueTransactMain.BRANCH_ID.equals(CommonUtil.convertObjToStr(TrueTransactMain.selBranch))) {
            ClientUtil.showMessageWindow("Interbranch Account creation not allowed for this screen...");
            TrueTransactMain.populateBranches();
            TrueTransactMain.selBranch = ProxyParameters.BRANCH_ID;
            observable.setSelectedBranchID(ProxyParameters.BRANCH_ID);
            setSelectedBranchID(ProxyParameters.BRANCH_ID);
            return;
        }else{
        rnw = 0;
//        txtNextAccNo.setText("");
//        List chargeList=null;
//        HashMap whereMap = new HashMap();
//        whereMap.put("BRANCH_ID",TrueTransactMain.BRANCH_ID);
//        whereMap.put("PRODUCT_ID",lblDocumentProdIdValue.getText());
//        chargeList=(List)(ClientUtil.executeQuery("getSelectNextAccNo", whereMap));
//        if(chargeList!=null && chargeList.size() > 0 ){
//            accountClosingCharge = CommonUtil.convertObjToStr((chargeList.get(0)));
//            ////System.out.println("accountClosingCharge"+accountClosingCharge);
//            txtNextAccNo.setText(String.valueOf(accountClosingCharge));
//        }
//        chargeList = null ;
        transNew = true;
         //Added by sreekrishnan
        tdtEmiDate.setEnabled(false);
        tdtEmiDate.setVisible(false);
        lblEmiDate.setVisible(false);
        btnnewActionPerformed();
        txtNextAccNo.setEnabled(false);
        txtNextAccNo.setEditable(false);
        txtMobileNo.setEnabled(false);
        tdtMobileSubscribedFrom.setEnabled(false);
        txtCustMobNo.setText("");
        cboGoldLoanProd.requestFocus();
        btnRepayShedule.setEnabled(false);        
        }
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnnewActionPerformed() {
        btnNewPressed = true;
        setFocusFirstTab();
        observable.destroyObjects();
        observable.createObject();
        observable.resetForm();
        observable.resetAllFacilityDetails();
        updateCboTypeOfFacility();
        ClientUtil.enableDisable(this, true);// Enables the panel...
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observableSecurity.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observableBorrow.setLoanActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.setStatus();
        setButtonEnableDisable();
        setAllTablesEnableDisable(true);
        setbtnCustEnableDisable(false);
        btnCustID.setEnabled(true);
        newPressedEnableDisable(true);
        setCompanyDetailsEnableDisable(false);
        setBorrowerDetailsEnableDisable(true);
        setSanctionFacilityBtnsEnableDisable(false);
        setAllSanctionFacilityEnableDisable(false);
        setSanctionMainBtnsEnableDisable(false);
        setAllSanctionMainEnableDisable(false);
        setAllFacilityDetailsEnableDisable(false);
        setFacilityBtnsEnableDisable(false);
        btnsDisableBasedOnAccountNumber();
        updateModeAuthorize = false;
        updateModePoA = false;
        updateSanctionFacility = false;
        updateSecurity = false;
        updateGuarantor = false;
        updateDocument = false;
        updateInterest = false;
        rowSanctionFacility = -1;
        setModified(true);
        btnNew2_SDAction();
        ClientUtil.enableDisable(panSecurityDetails_security, false);
        ClientUtil.enableDisable(panChargeDetails, false);
        txtEligibleLoan.setEnabled(false);
//        txtAppraiserId.setEnabled(false);
        cboAccStatus.setEnabled(false);
        tdtAccountCloseDate.setEnabled(false);
        btnView.setEnabled(false);
        tdtAccountOpenDate.setEnabled(false);
        txtMarginAmt.setText("");
        txtEligibleLoan.setText("");
        rdoExistingCustomer_Yes.setSelected(true);
        txtAccountNo.setVisible(true);
        lblAccountNo.setVisible(true);
        cboConstitution.setSelectedItem(observableBorrow.getCbmConstitution().getDataForKey("INDIVIDUAL"));
        cboCategory.setSelectedItem(observableBorrow.getCbmCategory().getDataForKey("GENERAL_CATEGORY"));
        cboSanctioningAuthority.setSelectedItem(observable.getCbmSanctioningAuthority().getDataForKey("BRANCH_MANAGER"));
        cboPurposeOfLoan.setSelectedItem(observableClassi.getCbmPurposeCode().getDataForKey("OTHERS"));
        rdoPriority.setSelected(true);
        txtNoInstallments.setText("1");
        if (CommonUtil.convertObjToStr(CommonConstants.BANK_TYPE).equals("DCCB")) {
            txtNoInstallments.setEnabled(true);
        } else {
            txtNoInstallments.setEnabled(false);
        }
        txtGrossWeight.setEnabled(true);
        txtNetWeight.setEnabled(true);
        cboPurityOfGold.setEnabled(true);
        setEnableJewalDetails(true);
        //cboPurityOfGold.setSelectedItem("22CT");
        lblAppraiserNameValue.setText("");
       // cboAppraiserId.setSelectedItem(CommonUtil.convertObjToStr(com.see.truetransact.ui.TrueTransactMain.USERINFO.get("EMPLOYEE_ID")));
        getAppraiserDefaultName();
//        btnAppraiserId.setEnabled(true);
        cboAppraiserId.setEnabled(true);
        txtAreaParticular.setEnabled(true);
        //comm by jiby
//        HashMap marginMap = new HashMap();
//        LinkedHashMap whereMap = new LinkedHashMap();
//        marginMap.put("PROD_ID",eachProdId);
//        whereMap.put("PROD_ID",eachProdId);
//        List lst = ClientUtil.executeQuery("getSelectMarginPercentage", marginMap);
//        cboRepayFreq.setSelectedIndex(0);
//        if(lst!=null && lst.size()>0){
//            marginMap = (HashMap)lst.get(0);
//            txtMargin.setText(CommonUtil.convertObjToStr(marginMap.get("DEP_ELIGIBLE_LOAN_AMT")));
//            ((ComboBoxModel)cboRepayFreq.getModel()).setKeyForSelected(CommonUtil.convertObjToStr(marginMap.get("MAX_PERIOD")));
//            if (cboRepayFreq.getSelectedIndex()==0) {
//                cboRepayFreq.setSelectedItem("Yearly");
//            }
//        }
//        
//        List prodDesclst = ClientUtil.executeQuery("TermLoan.getProdId", whereMap);
//        if(prodDesclst!=null && prodDesclst.size()>0){
//            whereMap = (LinkedHashMap)prodDesclst.get(0);
//            prodDesc = CommonUtil.convertObjToStr(whereMap.get("PROD_DESC"));
//        }
//        nomineeUi.resetNomineeTab();
//        createChargeTable();
//        if(tableFlag)
//         chargeAmount();
        /////////////////////////////////////////////////////////
        btnRenew.setVisible(false);
        txtNoOfPacket.setText(String.valueOf("1"));
        tableFlag = false;
        panChargeDetails.removeAll();
        panChargeDetails.setVisible(false);
        tabLimitAmount.remove(panTranDetView);
        HashMap whereMap = new HashMap();
        List list = ClientUtil.executeQuery("getDefaultGoldLoanProduct", whereMap);
        if (list != null && list.size() >0) {
            HashMap tempMap= (HashMap) list.get(0);
            if(tempMap!=null && tempMap.containsKey("VALUE")){
               cboGoldLoanProd.setSelectedItem(tempMap.get("VALUE"));
               cboGoldLoanProdActionPerformed(null);
             }
       }
        String cmbPrdVal=CommonUtil.convertObjToStr(cboGoldLoanProd.getSelectedItem()).trim();
//         if (cmbPrdVal!=null && cmbPrdVal.length()>0) {
//            cboGoldLoanProdAction();
//        }
    }

    private void getAppraiserDefaultName() {
        HashMap whereMap = new HashMap();
        whereMap.put("USER_ID", TrueTransactMain.USER_ID);
        whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        List lst = ClientUtil.executeQuery("getSelectAppraiserName", whereMap);
        if (lst != null && lst.size() > 0) {
            whereMap = (HashMap) lst.get(0);
            cboAppraiserId.setSelectedItem(whereMap.get("EMPLOYEE_NAME"));
            observableSecurity.setTxtAppraiserId(CommonUtil.convertObjToStr(whereMap.get("EMPLOYEE_CODE")));
            observable.getCbmAppraiserId().setKeyForSelected(CommonUtil.convertObjToStr(whereMap.get("EMPLOYEE_CODE")));
            lblAppraiserNameValue.setText(CommonUtil.convertObjToStr(whereMap.get("EMPLOYEE_NAME")));

        }
    }

    private void getRenewalAppraiserDefaultName(String emp_code) {
        if (emp_code != null && !emp_code.equals("")) {
            HashMap whereMap = new HashMap();
            whereMap.put("EMP_CODE", emp_code);
            List lst = ClientUtil.executeQuery("getSelectRenewalAppraiserName", whereMap);
            if (lst != null && lst.size() > 0) {
                whereMap = (HashMap) lst.get(0);
                cboRenewalAppraiserId.setSelectedItem(whereMap.get("EMPLOYEE_NAME"));
                 ((ComboBoxModel) cboRenewalAppraiserId.getModel()).setKeyForSelected(CommonUtil.convertObjToStr(emp_code));
                 observable.getCbmRenewalAppraiserId().setKeyForSelected(CommonUtil.convertObjToStr(emp_code));
            }
        } else {
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            List lst = ClientUtil.executeQuery("getSelectAppraiserName", whereMap);
            if (lst != null && lst.size() > 0) {
                whereMap = (HashMap) lst.get(0);
                cboRenewalAppraiserId.setSelectedItem(whereMap.get("EMPLOYEE_NAME"));
                ((ComboBoxModel) cboRenewalAppraiserId.getModel()).setKeyForSelected(CommonUtil.convertObjToStr(whereMap.get("EMPLOYEE_CODE")));
                observable.getCbmRenewalAppraiserId().setKeyForSelected(CommonUtil.convertObjToStr(whereMap.get("EMPLOYEE_CODE")));
                ////System.out.println("vvvvvvvvvvvv--->"+whereMap.get("EMPLOYEE_CODE"));
            }
        }
    }

    private void createChargeTable() {
        panChargeDetails.removeAll();// Added by nithya for 0010600: GOLD LOAN(GL)
        if (transNew) {
            String prodId = prodDesc;
            ////System.out.println("prodId 0000 nnnn====== " + prodId);
            HashMap tableMap = buildData(prodId);
            //System.out.println("tableMap 0000 nnnn====== " + tableMap);
            ArrayList dataList = new ArrayList();
            dataList = (ArrayList) tableMap.get("DATA");
            if (dataList != null && dataList.size() > 0) {
                // });
                tableFlag = true;
                panChargeDetails.setVisible(true);
                SimpleTableModel stm = new SimpleTableModel((ArrayList) tableMap.get("DATA"), (ArrayList) tableMap.get("HEAD"));
                table = new JTable(stm);
                table.setSize(430, 110);
                table.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        tableMouseClicked(evt);
                    }
                });
                srpChargeDetails = new JScrollPane(table);
                srpChargeDetails.setMinimumSize(new Dimension(430, 110));
                srpChargeDetails.setPreferredSize(new Dimension(430, 110));
                panChargeDetails.add(srpChargeDetails, new GridBagConstraints());
                table.revalidate();

            } else {
                tableFlag = false;
                chrgTableEnableDisable();
            }
        } else {
            HashMap tableMap = editBuildData(prodDesc);
            ArrayList dataList = new ArrayList();
            dataList = (ArrayList) tableMap.get("DATA");
            if (dataList != null && dataList.size() > 0) {
                tableFlag = true;
                ArrayList headers;
                panRenewalChargeDetails.setVisible(true);
                SimpleTableModel stm = new SimpleTableModel((ArrayList) tableMap.get("DATA"), (ArrayList) tableMap.get("HEAD"));
                table = new JTable(stm);
                table.setSize(430, 110);
                table.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        tableMouseClicked(evt);
                    }
                });
                srpChargeDetails = new javax.swing.JScrollPane(table);
                srpChargeDetails.setMinimumSize(new java.awt.Dimension(430, 110));
                srpChargeDetails.setPreferredSize(new java.awt.Dimension(430, 110));
                panRenewalChargeDetails.add(srpChargeDetails, new java.awt.GridBagConstraints());
                table.revalidate();
            } else {
                tableFlag = false;
                chrgTableEnableDisable();
            }
            //System.out.println("tableFlag  0000 nnnn====== " + tableFlag);
        }
    }

    private void chrgTableEnableDisable() {
        tableFlag = false;
        panChargeDetails.removeAll();
        panChargeDetails.setVisible(false);
        panRenewalChargeDetails.removeAll();
        panRenewalChargeDetails.setVisible(false);
    }

    private HashMap buildData(String prodId) {
        /*   HashMap whereMap = new HashMap();
        whereMap.put("SCHEME_ID", prodId);
        whereMap.put("DEDUCTION_ACCU", "O");
        //  whereMap.put("DEDUCTION_ACCU", "C");
        List list = ClientUtil.executeQuery("getChargeDetailsData", whereMap);
        boolean _isAvailable = list.size() > 0 ? true : false;
        map.put("HEAD", _heading);
        map.put("DATA", data);
        return map;*/
        ////////////
        //   String prodId=prodDesc;
        HashMap whereMap = new HashMap();
        whereMap.put("SCHEME_ID", prodId);
        if (transNew) {
            whereMap.put("DEDUCTION_ACCU", "O");
        } else {
            //whereMap.put("DEDUCTION_ACCU", "C");//20-04-2014
            whereMap.put("DEDUCTION_ACCU_RENEWAL", "DEDUCTION_ACCU_RENEWAL");
        }
        List list = ClientUtil.executeQuery("getChargeDetailsData", whereMap);
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
        } else {
            _heading = new ArrayList();
            _heading.add("Select");
            _heading.add("DESC");
            _heading.add("AMOUNT");
            _heading.add("M");
        }

        String cellData = "", keyData = "";
        Object obj = null;
        for (int i = 0, j = list.size(); i < j; i++) {
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
        return map;
    }

    private void editChargeTable() {
        HashMap tableMap = editBuildData(prodDesc);
        ArrayList dataList = new ArrayList();
        dataList = (ArrayList) tableMap.get("DATA");
        if (dataList != null && dataList.size() > 0) {
            tableFlag = true;
            ArrayList headers;
            panRenewalChargeDetails.setVisible(true);
            SimpleTableModel stm = new SimpleTableModel((ArrayList) tableMap.get("DATA"), (ArrayList) tableMap.get("HEAD"));
            table = new JTable(stm);
            table.setSize(430, 110);
            srpChargeDetails = new javax.swing.JScrollPane(table);
            srpChargeDetails.setMinimumSize(new java.awt.Dimension(430, 110));
            srpChargeDetails.setPreferredSize(new java.awt.Dimension(430, 110));
            panRenewalChargeDetails.add(srpChargeDetails, new java.awt.GridBagConstraints());
            table.revalidate();
            table.addMouseListener(new java.awt.event.MouseAdapter() {

                public void mouseClicked(java.awt.event.MouseEvent e) {
                    //System.out.println("Txt mmmmmmmmmmmmmmm===============" + txtCharges.getText());
                    if (tableFlag == true) {
                        accClosingCharges();

                    }
                }
            });
            table.addPropertyChangeListener(
                    new PropertyChangeListener(){ 
                public void propertyChange(java.beans.PropertyChangeEvent e) {  
                     if (tableFlag == true) {
                        accClosingCharges();

                    }
                }
            });
            getTotalRenewalAmt();
        } else {
            tableFlag = false;
            chrgTableEnableDisable();
        }
        //System.out.println("Txt chargesss===============" + txtCharges.getText());
        //System.out.println("Txt txtRenewalLimit_SD===============" + txtRenewalLimit_SD.getText());
        getTotalRenewalAmt();
    }

    public void calculateTot() {
        double totCharge = 0;
        table.revalidate();
        //        totCharge = accountClosingCharge;   // PRODUCT LEVEL ACC_CLOSING CHARGE
        for (int i = 0; i < table.getRowCount(); i++) {
            if (CommonUtil.convertObjToStr(table.getValueAt(i, 0)).equals("true")) {
                totCharge = totCharge + CommonUtil.convertObjToDouble(table.getValueAt(i, 3).toString()).doubleValue();
            }
        }
        totCharge = (double) higher((long) (totCharge * 100), 100) / 100;
        txtCharges.setText(CurrencyValidation.formatCrore(String.valueOf(totCharge)));
        
        // Added by nithya on 09-10-2018 for KD 263 GST - Needed to implement the GST in GOld Loan renewal
        List taxSettingsList = new ArrayList();
        ////System.out.println("serviceTaxApplMap....." + serviceTaxApplMap);
        ////System.out.println("table.getRowCount() :: "+ table.getRowCount());
        if (serviceTaxApplMap != null && serviceTaxApplMap.size() > 0) {            
            for (int i = 0; i < table.getRowCount(); i++) {
                double chrgamt = 0;
                HashMap serviceTaSettingsMap = new HashMap();
                ////System.out.println("boolean val "+ table.getValueAt(i, 0));
                ////System.out.println("charge val "+ CommonUtil.convertObjToStr(table.getValueAt(i, 1)));
                boolean checkFlag = new Boolean(CommonUtil.convertObjToStr(table.getValueAt(i, 0))).booleanValue();
                String descVal = CommonUtil.convertObjToStr(table.getValueAt(i, 1));
                ////System.out.println("checkFlag here" + checkFlag);
                if (CommonUtil.convertObjToStr(table.getValueAt(i, 0)).equals("true") && CommonUtil.convertObjToStr(serviceTaxApplMap.get(descVal)).equals("Y") && CommonUtil.convertObjToStr(serviceTaxIdMap.get(descVal)).length() > 0) {
                    ////System.out.println("entered" + descVal);
                    chrgamt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(table.getValueAt(i, 3)));
                    if (chrgamt > 0) {
                        serviceTaSettingsMap.put("SETTINGS_ID", serviceTaxIdMap.get(descVal));
                        serviceTaSettingsMap.put(ServiceTaxCalculation.TOT_AMOUNT, CommonUtil.convertObjToStr(chrgamt));
                        taxSettingsList.add(serviceTaSettingsMap);
                    }
                }
            }
        }  
        ////System.out.println("taxListForGoldLoan...here "+ taxListForGoldLoan);
        if (taxListForGoldLoan != null && taxListForGoldLoan.size() > 0) {
            taxSettingsList.addAll(taxSettingsList.size(), taxListForGoldLoan);
        }
           // //System.out.println("serviceTaSettingsMap inside calculateTot:: " + taxSettingsList);
            if(taxSettingsList != null && taxSettingsList.size() > 0){
                try {
                    objServiceTax = new ServiceTaxCalculation();
                    HashMap ser_Tax_Val = new HashMap();
                    ser_Tax_Val.put(ServiceTaxCalculation.CURR_DT, ClientUtil.getCurrentDate());
                    ser_Tax_Val.put("SERVICE_TAX_DATA", taxSettingsList);
                    serviceTax_Map = objServiceTax.calculateServiceTax(ser_Tax_Val);
                    if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.TOT_TAX_AMT)) {
                        String amt = CommonUtil.convertObjToStr(serviceTax_Map.get(ServiceTaxCalculation.TOT_TAX_AMT));
                        lblServiceTaxVal.setText(amt);
                        serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, amt);
                    } else {
                        lblServiceTaxVal.setText("0.00");
                        serviceTax_Map = null;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }else{
              lblServiceTaxVal.setText("0.00");  
              serviceTax_Map = null;
            }         
        // End

    }

    public void accClosingCharges() {
        if (tableFlag == true) {
            //            getSanctionAmount();
            //            chargeAmount();
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                calculateTot();
                //this fn add charges
            }
            //            finalizeCharges();
            panRenewalChargeDetails.setEnabled(true);
            srpChargeDetails.setEnabled(true);
            table.setEnabled(true);
            getTotalRenewalAmt();//this fn again geting charges and limit amount
            //calcAndDisplayAvailableBalance();


            //            double totCharge = 0;
            //            double recBalance = 0;
            //            totCharge = CommonUtil.convertObjToDouble(txtAccountClosingCharges.getText()).doubleValue();    // Acc Closing Charges (Table Values)
            //            recBalance = payableBalance;    // Balance + Product Level ACC Closing Charge(Column Value)
            //            txtPayableBalance.setText(CurrencyValidation.formatCrore(String.valueOf((totCharge+recBalance-accountClosingCharge))));
            //            transactionUI.setCallingAmount(txtPayableBalance.getText());
            //            observable.setTxtPayableBalance(txtPayableBalance.getText());
        }

    }    

    private HashMap editBuildData(String prodDesc) {
        /*  //System.out.println("prodDesc  888 ==== "+prodDesc);
        HashMap whereMap = new HashMap();
        whereMap.put("SCHEME_ID", prodDesc);
        whereMap.put("DEDUCTION_ACCU", "O");
        //  whereMap.put("DEDUCTION_ACCU", "C");
        data.add(colData);
        }
        map = new HashMap();
        map.put("HEAD", _heading);
        map.put("DATA", data);
        return map;*/
        String prodId = prodDesc;
        HashMap whereMap = new HashMap();
        whereMap.put("SCHEME_ID", prodId);
        // whereMap.put("DEDUCTION_ACCU", "O");
       // whereMap.put("DEDUCTION_ACCU", "C");//20-03-2014
        whereMap.put("DEDUCTION_ACCU_RENEWAL", "DEDUCTION_ACCU_RENEWAL");//20-03-2014
        List list = ClientUtil.executeQuery("getChargeDetailsData", whereMap);
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
            _heading.add("select");
            while (iterator.hasNext()) {
                _heading.add((String) iterator.next());
            }
        }

        String cellData = "", keyData = "";
        Object obj = null;
        for (int i = 0, j = list.size(); i < j; i++) {
            map = (HashMap) list.get(i);
            colData = new ArrayList();
            iterator = map.values().iterator(); 
            if (CommonUtil.convertObjToStr(map.get("M")).equals("Y")) {
                colData.add(new Boolean(true));
            } else if(viewType==AUTHORIZE){
                colData.add(new Boolean(true));
            }else{
                colData.add(new Boolean(false));
            }
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

    //this function added by Anju Anand for Mantid Id: 0010365: IMBP Limit Setting
    private void setImbpSettings() {
        if (cboGoldLoanProd.getSelectedItem().equals("") || txtCustID.getText().equals("")) {
            ClientUtil.showMessageWindow("Please enter all the necessary details..!!!");
            txtLimit_SD.setText("");
            return;
        } else {
            String prodId = "";
            prodId = CommonUtil.convertObjToStr(((ComboBoxModel) cboGoldLoanProd.getModel()).getKeyForSelected());
            HashMap dataMap = new HashMap();
            dataMap.put("PROD_ID", prodId);
            List prodList = null;
            prodList = ClientUtil.executeQuery("getImbpType", dataMap);
            if (prodList != null && prodList.size() > 0) {
                HashMap prodMap = new HashMap();
                prodMap = (HashMap) prodList.get(0);
                String imbpType = "";
                imbpType = CommonUtil.convertObjToStr(prodMap.get("IMBP_TYPE"));
                dataMap.put("IMBP_TYPE", imbpType);
                String custId = "";
                custId = txtCustID.getText();
                String shareNo = "";
                shareNo = txtAccountNo.getText();
                dataMap.put("SHARE_ACCT_NO", shareNo);
                List shareList = null;
                shareList = ClientUtil.executeQuery("selectShareType", dataMap);
                if (shareList != null && shareList.size() > 0) {
                    HashMap newMap = new HashMap();
                    newMap = (HashMap) shareList.get(0);
                    String shareType = "";
                    shareType = CommonUtil.convertObjToStr(newMap.get("SHARE_TYPE"));
                    if (shareType != null && !shareType.equals("")) {
                        dataMap.put("SHARE_TYPE", shareType);
                    } else {
                        dataMap.put("SHARE_TYPE", " ");
                    }
                } else {
                    dataMap.put("SHARE_TYPE", " ");
                }
                List imbpList = null;
                imbpList = ClientUtil.executeQuery("getMaxImbpLoanAmt", dataMap);
                if (imbpList != null && imbpList.size() > 0) {
                    HashMap imbpMap = new HashMap();
                    imbpMap = (HashMap) imbpList.get(0);
                    double maxImbpAmt = 0;
                    maxImbpAmt = CommonUtil.convertObjToDouble(imbpMap.get("MAX_LOAN_AMOUNT"));
                    int maxNoOfLoans = 0;
                    if(imbpMap.containsKey("MAX_NO_OF_LOANS") && imbpMap.get("MAX_NO_OF_LOANS")!= null && !imbpMap.get("MAX_NO_OF_LOANS").equals("")){
                        maxNoOfLoans = CommonUtil.convertObjToInt(imbpMap.get("MAX_NO_OF_LOANS"));
                    }
                    //MAX_NO_OF_LOANS
                    double loanAmt = 0;
                    loanAmt = CommonUtil.convertObjToDouble(txtLimit_SD.getText());
                    dataMap.put("CUST_ID", custId);
                    List list = null;
                    list = ClientUtil.executeQuery("getLoanDetails", dataMap);
                    if (list != null && list.size() > 0) {
                        double clearBal = 0;
                        double shadowDebit = 0;
                        double shadowCredit = 0;
                        double totalAmt = 0;
                        // Added for loop by nithya on 21-07-2016 for 4922
                        for(int i=0; i< list.size(); i++){
                            HashMap resultMap = new HashMap();
                            resultMap = (HashMap) list.get(i);
                            clearBal = clearBal + CommonUtil.convertObjToDouble(resultMap.get("CLEAR_BALANCE"));                        
                            shadowDebit = shadowDebit + CommonUtil.convertObjToDouble(resultMap.get("SHADOW_DEBIT"));
                            shadowCredit = shadowCredit + CommonUtil.convertObjToDouble(resultMap.get("SHADOW_CREDIT"));
                        }
                        if (shadowDebit > 0 || shadowCredit > 0) {
                            ClientUtil.showMessageWindow("Authorization pending for this Customer..Hence new loan cannot be created!!");
                            ClientUtil.clearAll(panBorrowProfile);
                            txtLimit_SD.setText("");
                        } else {
                            if (clearBal < 0) {
                                clearBal = clearBal * (-1);
                            }
                            totalAmt = clearBal + loanAmt;
                            if(maxNoOfLoans > 0){
                                List countList = ClientUtil.executeQuery("getParticularLoanCountForCustomer", dataMap);
                                if(countList != null && countList.size() > 0){
                                   HashMap countResultMap = new HashMap() ;
                                   countResultMap = (HashMap)countList.get(0);
                                   if(countResultMap.containsKey("NO_OF_LOANS") && countResultMap.get("NO_OF_LOANS")!= null){
                                       int loanCount = CommonUtil.convertObjToInt(countResultMap.get("NO_OF_LOANS"));
                                       if(loanCount+1 > maxNoOfLoans){
                                           ClientUtil.showMessageWindow("This customer cannot be given more than " + maxNoOfLoans + " loans\n Currently " + loanCount + " Loans exist" );
                                           txtLimit_SD.setText("");
                                       }else if (totalAmt > maxImbpAmt) {
                                           if (clearBal > maxImbpAmt) {
                                               ClientUtil.showMessageWindow("This customer cannot be given loan as his/her outstanding amount already exceeds the IMBP limit..!!");
                                           } else if (clearBal < maxImbpAmt) {
                                               double amount = 0;
                                               amount = maxImbpAmt - clearBal;
                                               String amt = CurrencyValidation.formatCrore(CommonUtil.convertObjToStr(amount));
                                               ClientUtil.showMessageWindow("The amount entered exceeds IMBP limit amount...This customer can be given loan only for a maximum amount of Rs. " + amt + "/-");
                                           }
                                           txtLimit_SD.setText("");
                                       }
                                   }                                   
                                }                                
                            }
                            else if (totalAmt > maxImbpAmt) {
                                if (clearBal > maxImbpAmt) {
                                    ClientUtil.showMessageWindow("This customer cannot be given loan as his/her outstanding amount already exceeds the IMBP limit..!!");
                                } else if (clearBal < maxImbpAmt) {
                                    double amount = 0;
                                    amount = maxImbpAmt - clearBal;
                                    String amt = CurrencyValidation.formatCrore(CommonUtil.convertObjToStr(amount));
                                    ClientUtil.showMessageWindow("The amount entered exceeds IMBP limit amount...This customer can be given loan only for a maximum amount of Rs. " + amt + "/-");
                                }
                                txtLimit_SD.setText("");
                            }
                        }
                    } else {
                        if (loanAmt > maxImbpAmt) {
                            String imbpAmt = CurrencyValidation.formatCrore(CommonUtil.convertObjToStr(maxImbpAmt));
                            ClientUtil.showMessageWindow("The loan amount exceeds the IMBP limit amount...Please enter an amount less than Rs. " + imbpAmt + "/-");
                            txtLimit_SD.setText("");
                        }
                    }
                }
            }
        }
    }

    public class SimpleTableModel extends AbstractTableModel {

        private ArrayList dataVector;
        private ArrayList headingVector;

        public SimpleTableModel(ArrayList dataVector, ArrayList headingVector) {
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
            ArrayList rowVector = (ArrayList) dataVector.get(row);
            return rowVector.get(col);
        }

        public String getColumnName(int column) {
            return headingVector.get(column).toString();
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        public boolean isCellEditable(int row, int col) {
            if (col == 0 && (CommonUtil.convertObjToStr(getValueAt(row, col + 3)).equals("Y"))) { // (CommonUtil.convertObjToStr(getValueAt(row, headingVector.size() - 1)).equals("Y"))
                return false;
            } else {
                if (col != 0) {
                    if (col == 3 && (CommonUtil.convertObjToStr(getValueAt(row, col + 2)).equals("Y"))) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
            }
        }

        public void setValueAt(Object aValue, int row, int col) {
            ArrayList rowVector = (ArrayList) dataVector.get(row);
            rowVector.set(col, aValue);
        }
    }

    public void repaymentFillData(java.util.LinkedHashMap data, double totRepayVal) {
        observableRepay.populateEMICalculatedFields(data, totRepayVal);
        deleteInstallment = false;
    }

    // To display the All the Cust Id's which r having status as
    // created or updated, in a table...
    private void popUp(String field) {


        final HashMap viewMap = new HashMap();
        viewType = field;
        //if ( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||  observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
        if (field.equals("Edit") || field.equals("Delete") || field.equals("Enquirystatus") || field.equals("CLOSED_DATA")) {
            ArrayList lst = new ArrayList();
            lst.add("LOAN_NO");
//             viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;

            lblStatus.setText(ClientConstants.ACTION_STATUS[0]);
            HashMap editMapCondition = new HashMap();
            editMapCondition.put("BRANCH_ID", getSelectedBranchID());
            //            editMapCondition.put("PROD_ID", eachProdId);
            editMapCondition.put("AUTHORIZE_REMARK", "IS NULL");
            editMapCondition.put("GOLD_LOAN", "GOLD_LOAN");
            viewMap.put(CommonConstants.MAP_WHERE, editMapCondition);
            if (loanType.equals("LTD")) {
                viewMap.put(CommonConstants.MAP_NAME, "viewTermLoanForLTD");
              
            } else {
                if (eachProdId != null && !eachProdId.equals("")) {
                    editMapCondition.put("PROD_ID", eachProdId);
                }
                if(field.equals("CLOSED_DATA")){
                    editMapCondition.put("CLOSED_ACCT","CLOSED_ACCT");
                }else{
                   editMapCondition.put("NOT_CLOSED_ACCT","NOT_CLOSED_ACCT");  
                }
                viewMap.put(CommonConstants.MAP_NAME, "viewGoldLoan");
                
            }
            if (field.equals("Delete")) {
                if (loanType.equals("LTD")) {
                    viewMap.put(CommonConstants.MAP_NAME, "viewTermLoanForDeleteLTD");
                } else {
                    viewMap.put(CommonConstants.MAP_NAME, "viewTermLoanForDelete");
                }
            } //mapped statement: viewTermLoan---> result map should be a Hashmap in OB...
        } else if (field.equals("Borr_Cust_Id")) {
            viewMap.put(CommonConstants.MAP_NAME, "getCustomers");
        } else if (field.equals("Guarant_Cust_Id")) {
            viewMap.put(CommonConstants.MAP_NAME, "getCustomers");
        } else if (field.equals("GUARANTOR_ACCT_NO")) {
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

    private void setFocusFirstTab() {
        tabLimitAmount.setSelectedIndex(0);
    }

    private void setFocusAcctLevelTab() {
        tabLimitAmount.setSelectedIndex(4);
    }

    public void fillData(Object param) {
        HashMap hash = (HashMap) param;
        HashMap Loanhash = (HashMap) param;
        ////System.out.println("calling filldata#####" + hash);
        nomineeUi.setViewType(viewType);
        transDetailsUI.setSourceFrame(this);
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
        if (hash.containsKey("FROM_AUTHORIZE_LIST_UI")) {
            fromAuthorizeUI = true;
            authorizeListUI = (AuthorizeListUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            authEnableDisable();
            btnReject.setEnabled(false);
            rejectFlag = 1;
        }
        if (hash.containsKey("FROM_MANAGER_AUTHORIZE_LIST_UI")) {
            fromManagerAuthorizeUI = true;
            ManagerauthorizeListUI = (AuthorizeListDebitUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            authEnableDisable();
            btnReject.setEnabled(false);
            rejectFlag = 1;
        }
        if (hash.containsKey("FROM_CASHIER_APPROVAL_REJ_UI")) {
            ////System.out.println("HASH DATE ====================" + hash);
            fromAuthorizeUI = false;
            fromManagerAuthorizeUI = false;
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
            observable.setStatus();
            btnSaveDisable();
        }
        if (viewType != null) {
            if (viewType.equals("CLOSED_DATA")||viewType.equals("Edit") || viewType.equals("Delete") || viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION)
                    || viewType.equals(REJECT) || viewType.equals("Enquirystatus")) {

                isFilled = true;
                if (viewType.equals("Edit") || viewType.equals("Enquirystatus") ||viewType.equals("CLOSED_DATA")) {
                    hash.put("BORROW_NO", hash.get("BORROWER NO"));
                    if (viewType.equals("Edit") ||viewType.equals("CLOSED_DATA")) {
                        hash.put("RENEWED_LOAN_NO", null);
                    }
//                     hash.put("ACCT_NUM",hash.get("LOAN_NO"));
                }
                ////System.out.println("hashhashhash" + hash);
                observable.populateData(hash, nomineeUi.getNomineeOB());//, authSignUI.getAuthorizedSignatoryOB(), poaUI.getPowerOfAttorneyOB());
                if (hash.containsKey("RENEWED_LOAN_NO") && CommonUtil.convertObjToStr(hash.get("RENEWED_LOAN_NO")).length() > 0 && (!(viewType.equals("Edit") ))&&(!(viewType.equals("CLOSED_DATA") ))) {
                    tabLimitAmount.add(panBorrowRenewalCompanyDetails, "Renewal/Sanction Details");
                    interestCalculationTLAD(CommonUtil.convertObjToStr(lblRenewalAcctNo_Sanction_Disp.getText()));
                    transDetailsUI.setSourceScreen("LOAN_ACT_CLOSING");
                    //transDetailsUI.setIsDebitSelect(true);

                    transDetailsUI.setTransDetails("TL", ProxyParameters.BRANCH_ID, CommonUtil.convertObjToStr(lblRenewalAcctNo_Sanction_Disp.getText()));
                    transDetailsUI.setSourceFrame(this);
                    calcAfterRenewalEligibleLoanAmount();
                    btnRenew.setVisible(false);
                    super.removeEditLock(CommonUtil.convertObjToStr(hash.get("RENEWED_LOAN_NO")));
                } else if (viewType.equals("Edit")) {
                    btnRenew.setVisible(true);
                }
                
                if(hash.containsKey("MEMBERSHIP_NO") && hash.get("MEMBERSHIP_NO")!=null){
                    txtAccountNo.setText(CommonUtil.convertObjToStr(hash.get("MEMBERSHIP_NO")));
                }
                rdoExistingCustomer_Yes.setSelected(true);
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                        || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION
                        || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT
                        || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                        || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
                    txtAccountNo.setVisible(false);
                    lblAccountNo.setVisible(false);

                    ClientUtil.enableDisable(panBorrowProfile_CustName, false);
                    ClientUtil.enableDisable(panSanctionDetails_Table, false);
                    ClientUtil.enableDisable(panSecurityDetails_security, false);
                    //                    ClientUtil.enableDisable(panEligibleLoan,false);
                    ClientUtil.enableDisable(panTabDetails_DocumentDetails, false);
                    allowResetVisit = true;
                    allowResetVisit = true;
                    if (hash.containsKey("RENEWED_LOAN_NO") && CommonUtil.convertObjToStr(hash.get("RENEWED_LOAN_NO")).length() > 0) {
                        observable.setStrACNumber(CommonUtil.convertObjToStr(hash.get("RENEWED_LOAN_NO")));
                        oldGoldLoanNo = CommonUtil.convertObjToStr(hash.get("ACCT_NUM"));
                    } else {
                        observable.setStrACNumber(CommonUtil.convertObjToStr(hash.get("ACCT_NUM")));
                    }
                    if (CommonUtil.convertObjToStr(CommonConstants.BANK_TYPE).equals("DCCB")) {
                        displayTransDetail();
                    }
                    if (viewType.equals(AUTHORIZE) || viewType.equals(REJECT)) {
                        if (viewType.equals(AUTHORIZE)) {
                            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                            this.btnReject.setEnabled(false);
                            this.btnException.setEnabled(false);
                            btnAuthorize.setEnabled(true);
                            btnAuthorize.requestFocusInWindow();
                            btnAuthorize.setFocusable(true);
                        } else if (viewType.equals(REJECT)) {
                            observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
                            this.btnAuthorize.setEnabled(false);
                            this.btnException.setEnabled(false);
                        }
                        HashMap newCustMap = new HashMap();
                        newCustMap.put("ACCOUNTNO", hash.get("ACCT_NUM"));
                        newCustMap.put("PROD_ID", hash.get("PROD_ID"));
                        List lst = ClientUtil.executeQuery("TermLoanChargescustomerName", newCustMap);
                        if (lst != null && lst.size() > 0) {
                            newCustMap = (HashMap) lst.get(0);
                            if (newCustMap.get("AUTHORIZE_STATUS") == null) {
                                HashMap authMap = new HashMap();
                                authMap.put("PROD_ID", hash.get("PROD_ID"));
                                lst = ClientUtil.executeQuery("getCustomerAuthPermission", authMap);
                                if (lst != null && lst.size() > 0) {
                                    authMap = (HashMap) lst.get(0);
                                    rdoExistingCustomer_Yes.setSelected(false);
                                    rdoExistingCustomer_No.setSelected(true);
                                    ClientUtil.showAlertWindow("Authorize Customer Id");
                                    individualCustUI = new IndividualCustUI();
                                    com.see.truetransact.ui.TrueTransactMain.showScreen(individualCustUI);
                                    HashMap customerMap = new HashMap();
                                    customerMap.put("BRANCH_CODE", getSelectedBranchID());
                                    customerMap.put("WHERE", newCustMap.get("CUST_ID"));
                                    customerMap.put("CUST_ID", newCustMap.get("CUST_ID"));
                                    customerMap.put("LOAN_CUSTOMER_AUTHORIZE", "LOAN_CUSTOMER_AUTHORIZE");
                                    if (viewType.equals(AUTHORIZE)) {
                                        customerMap.put("LOAN_CUSTOMER_AUTHORIZATION", "LOAN_CUSTOMER_AUTHORIZATION");
                                    } else if (viewType.equals(REJECT)) {
                                        customerMap.put("LOAN_CUSTOMER_REJECTION", "LOAN_CUSTOMER_REJECTION");
                                    }
                                    individualCustUI.fillData(customerMap);
                                    HashMap whereMap = new HashMap();
                                    whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
                                    observable.populateData(Loanhash, nomineeUi.getNomineeOB());
                                    individualCustUI.setGoldLoanUI(this);
                                    nomineeUi.setMainCustomerId(txtCustID.getText());

                                    nomineeUi.setAuthInstEnableDisable(false);
                                    /**
                                     * TO get the Max of the deleted Nominee(s)
                                     * for the particular Account-Holder...
                                     */
                                    nomineeUi.callMaxDel(CommonUtil.convertObjToStr(hash.get("ACCT_NUM")));
                                    nomineeUi.resetNomineeTab();
                                    nomineeUi.setMinNominee(CommonUtil.convertObjToStr(minNominee));
                                } else {
                                    ClientUtil.showAlertWindow("Customer ID creation not yet Authorised");
                                    btnCancelActionPerformed(null);
                                    return;
                                }
                            }
                        }
                    } else if (viewType.equals(EXCEPTION)) {
                        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
                    } else if (viewType.equals(CommonConstants.TOSTATUS_DELETE)) {
                        observableClassi.setClassifiDetails(CommonConstants.TOSTATUS_DELETE);
//                         observableOtherDetails.setOtherDetailsMode(CommonConstants.TOSTATUS_DELETE);
                    }
                    observable.setStatus();
                } else {
                    txtAccountNo.setVisible(false);
                    lblAccountNo.setVisible(false);
                    if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT && observable.getAuthorizeDt().length() > 0
                            && observable.getAvailableBalance() == 0) {
                        ClientUtil.enableDisable(panBorrowProfile_CustName, false);
                        ClientUtil.enableDisable(panTableFields_SD, false);
                        cboConstitution.setEnabled(true);
                        cboCategory.setEnabled(true);
                        ClientUtil.enableDisable(panSanctionDetails_Sanction, true);
                    } else {
                        ClientUtil.enableDisable(panBorrowProfile_CustName, true);
                        ClientUtil.enableDisable(panSanctionDetails_Table, true);
                    }
                    if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                        chkMobileBankingTLAD.setEnabled(true);
                    }
                    setFocusFirstTab();
                    cboAccStatus.setEnabled(false);
                    tdtAccountCloseDate.setEnabled(false);
                    rdoExistingCustomer_Yes.setEnabled(false);
                    rdoExistingCustomer_No.setEnabled(false);
                    cboPurposeOfLoan.setSelectedItem(observableClassi.getCboPurposeCode());
                    if (hash.containsKey("RENEWED_LOAN_NO") && CommonUtil.convertObjToStr(hash.get("RENEWED_LOAN_NO")).length() > 0 && (!(viewType.equals("Edit")))&&(!(viewType.equals("CLOSED_DATA") ))) {
                        observable.setStrACNumber(CommonUtil.convertObjToStr(hash.get("RENEWED_LOAN_NO")));
                    } else {
                        observable.setStrACNumber(CommonUtil.convertObjToStr(hash.get("ACCT_NUM")));
                    }
                    hash.put(CommonConstants.MAP_WHERE, hash.get("LOAN_NO"));
                    hash.put("BORROW_NO", hash.get("BORROWER NO"));
                    // To populate the tabs on the basis of KEY_VALUE->BORROWER NO
                    hash.put("KEY_VALUE", "AUTHORIZE");//BORROWER_NUMBER
                    txtInter.setEnabled(false);
                    txtPenalInter.setEnabled(false);
                    if (observable.getStrACNumber() != null) {
                        String actNum = observable.getStrACNumber();
                        if(chkok!=true){
                        int yesNo = 0;
                        String[] options = {"Yes", "No"};
                        yesNo = COptionPane.showOptionDialog(null, "Do you want to print GoldBond?", CommonConstants.WARNINGTITLE,
                                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                                null, options, options[0]);
                        ////System.out.println("#$#$$ yesNo : " + yesNo);
                        if (yesNo == 0) {
                            com.see.truetransact.clientutil.ttrintegration.TTIntegration ttIntgration = null;
                            HashMap reportTransIdMap = new HashMap();
                            reportTransIdMap.put("Act_Num", actNum);
                            ttIntgration.setParam(reportTransIdMap);
                            String transType = "";
                            ttIntgration.integrationForPrint("Goldbond");
                        }
                    }

                        HashMap transTypeMap = new HashMap();
                        HashMap transMap = new HashMap();
                        HashMap transCashMap = new HashMap();
                        transCashMap.put("BATCH_ID", actNum);
                        transCashMap.put("TRANS_DT", curr_dt);
                        transCashMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                        HashMap transIdMap = new HashMap();
                        List list = ClientUtil.executeQuery("getTransferDetails", transCashMap);
                        if (list != null && list.size() > 0) {
                            for (int i = 0; i < list.size(); i++) {
                                transMap = (HashMap) list.get(i);
                                transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "TRANSFER");
                            }
                        }
                        list = ClientUtil.executeQuery("getCashDetails", transCashMap);
                        if (list != null && list.size() > 0) {
                            for (int i = 0; i < list.size(); i++) {
                                transMap = (HashMap) list.get(i);
                                transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "CASH");
                                transTypeMap.put(transMap.get("SINGLE_TRANS_ID"), transMap.get("TRANS_TYPE"));
                            }
                        }
//                         yesNo = 0;
//                         String[] voucherOptions = {"Yes", "No"};
//                         yesNo = COptionPane.showOptionDialog(null,"Do you want to print Voucher?", CommonConstants.WARNINGTITLE,
//                         COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
//                         null, voucherOptions, voucherOptions[0]);
//                         if (yesNo==0) {
//                             com.see.truetransact.clientutil.ttrintegration.TTIntegration ttIntgration = null;
//                             HashMap paramMap = new HashMap();
//                             paramMap.put("TransDt", curr_dt);
//                             paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
//                             Object keys[] = transIdMap.keySet().toArray();
//                             for (int i=0; i<keys.length; i++) {
//                                 paramMap.put("TransId", keys[i]);
//                                 ttIntgration.setParam(paramMap);
//                                 if (CommonUtil.convertObjToStr(transIdMap.get(keys[i])).equals("TRANSFER")) {
//                                     ttIntgration.integrationForPrint("ReceiptPayment");
//                                 } else if (CommonUtil.convertObjToStr(transTypeMap.get(keys[i])).equals("DEBIT")) {
//                                     ttIntgration.integrationForPrint("CashPayment", false);
//                                 } else {
//                                     ttIntgration.integrationForPrint("CashReceipt", false);
//                                 }
//                             }
//                         }
                    }
                }
                appraiserCommisionAmt();
                if (observable.getStrACNumber().length() > 0) {
                    HashMap acctMap = new HashMap();
                    acctMap.put("ACCT_NUM", observable.getStrACNumber());
                    List list = (List) ClientUtil.executeQuery("TermLoan.getAcctProdHead", acctMap);
                    if (list != null && list.size() > 0) {
                        acctMap = (HashMap) list.get(0);
                        //                        lblSecurityAccHeadValue.setText(CommonUtil.convertObjToStr(acctMap.get("AC_HEAD")));
                        lblDocumentAccHeadValue.setText(CommonUtil.convertObjToStr(acctMap.get("AC_HEAD")));
                        observable.setSecurityAccHeadValue(CommonUtil.convertObjToStr(acctMap.get("AC_HEAD")));
                    }
                }

                // To add the Borrower level Customer ID's in Authorized Signatory's
                // acctLevelCustomerList
                addCustIDNAuthSignatory();
                observable.ttNotifyObservers();
                if (CommonUtil.convertObjToStr(cboAccStatus.getSelectedItem()).equals("Closed")) {
                    HashMap map = new HashMap();
                    if (lblAcctNo_Sanction_Disp.getText() != null && lblAcctNo_Sanction_Disp.getText().length() > 0) {
                        map.put("ACT_NUM", lblAcctNo_Sanction_Disp.getText());
                        List lst = ClientUtil.executeQuery("getDepositClosingAccounts", map);
                        if (lst != null && lst.size() > 0) {
                            ClientUtil.showMessageWindow("Account Closed but Authorization pending ");
                            ClientUtil.enableDisable(this, false);
                            btnSave.setEnabled(false);
                        } else {
                            ////System.out.println("test1111111111111111111111111111111");
                            ClientUtil.showMessageWindow("Account Closed");
                            ClientUtil.enableDisable(this, false);
                            btnSave.setEnabled(false);
                        }
                    } else {
                        map = new HashMap();
                    }
                }

                observableBorrow.setCustOpenDate(CommonUtil.convertObjToStr(hash.get("CUSTOMER ID")));
                observableBorrow.setCustAddr(CommonUtil.convertObjToStr(hash.get("CUSTOMER ID")));
                observableBorrow.setCustPhone(CommonUtil.convertObjToStr(hash.get("CUSTOMER ID")));
                observable.setStatus();
                setButtonEnableDisable();
                if (CommonUtil.convertObjToStr(cboAccStatus.getSelectedItem()).equals("Closed")) {
                    btnSave.setEnabled(false);
                }
                tabLimitAmount.setSelectedComponent(panBorrowCompanyDetails);
            } else if (viewType.equals("Borr_Cust_Id")) {
                final String CustID = CommonUtil.convertObjToStr(hash.get("CUSTOMER ID"));
                observableBorrow.setTxtCustID(CustID);
                observableBorrow.setCustAddr(CustID);
                observableBorrow.setCustPhone(CustID);
                observable.ttNotifyObservers();
                //                txtCustID.setEditable(false);
            } else if (viewType.equals("Guarant_Cust_Id")) {
//                 observableGuarantor.resetGuarantorDetails();
//                 observableGuarantor.resetInstitGuarantorDetails();
//                 updateGuarantorTabCustDetails();
//                 observableGuarantor.setTxtCustomerID_GD(CommonUtil.convertObjToStr(hash.get("CUSTOMER ID")));
//                 final String CustID = CommonUtil.convertObjToStr(hash.get("CUSTOMER ID"));
//                 observableGuarantor.setTxtGuarantorNetWorth(CommonUtil.convertObjToStr(hash.get("NETWORTH")));
//                 
//                 if (hash.get("DOB") != null && CommonUtil.convertObjToStr(hash.get("DOB")).length() > 0){
//                     observableGuarantor.setTdtDOB_GD(DateUtil.getStringDate((java.util.Date) hash.get("DOB")));
//                 }else{
//                     observableGuarantor.setTdtDOB_GD("");
//                 }
//                 observableGuarantor.setGuarantorCustOtherAccounts(CustID);
//                 observableGuarantor.setGuarantorCustAddr(CustID);
//                 observableGuarantor.setGuarantorCustName(CustID);
//                 observableGuarantor.setGuarantorCustPhone(CustID);
//                 //                cboProdType.setSelectedItem(observableGuarantor.getCboProdType());
//                 //                cboProdType.setEnabled(false);
//                 //                cboProdId.setSelectedItem(observableGuarantor.getCboProdId());
//                 //                cboProdId.setEnabled(false);
//                 //                txtGuaranAccNo.setText(observableGuarantor.getTxtGuaranAccNo());
//                 updateGuarantorTabCustDetails();
            } else if (viewType.equals("CUSTOMER ID")) {
                if (customerScreen.equals("CUSTOMER_SCREEN")) {
                    btnNewActionPerformed(null);
                    btnSave.setEnabled(true);
                    btnCancel.setEnabled(true);
                    btnEdit.setEnabled(false);
                    btnDelete.setEnabled(false);
                    btnAuthorize.setEnabled(false);
                    btnReject.setEnabled(false);
                    btnException.setEnabled(false);
                    btnView.setEnabled(false);
                    btnPrint.setEnabled(false);
                    btnNew.setEnabled(false);
                    observable.setLoadingProdId(lblDocumentProdIdValue.getText());
                    observableBorrow.setTxtCustID(CommonUtil.convertObjToStr(hash.get("CUSTOMER ID")));
                    observable.setNewCustIdNo(CommonUtil.convertObjToStr(hash.get("CUSTOMER ID")));
                }
//                 observableComp.resetCustomerDetails();
                if (hash.containsKey("CUST_ID")) {
                    hash.put("CUSTOMER ID", hash.get("CUST_ID"));                     
                 }
//                 if(hash.containsKey("AADHAAR_NO")){
//                     txtAccountNo.setText(CommonUtil.convertObjToStr(hash.get("AADHAAR_NO")));
//                 }
                if (hash.containsKey("SHARE_ACCT_NO")) {
                    if (hash.containsKey("STATUS")&& hash.get("STATUS")!=null && hash.get("STATUS").toString().equals("CLOSED")) {
                        ClientUtil.showMessageWindow("Closed Member Cannot Continue further");
                        ClientUtil.enableDisable(this, false);
                        btnnewActionPerformed();
                        hash = null;
                    } else {
                        txtAccountNo.setText(CommonUtil.convertObjToStr(hash.get("SHARE_ACCT_NO")));
                    }
                }               
                custInfoDisplay(CommonUtil.convertObjToStr(hash.get("CUSTOMER ID")), loanType);
                validateConstitutionCustID();
                lblCustNameValue.setText(observableBorrow.getLblCustNameValue());
                //                lblSecurityCustNameValue.setText(observableBorrow.getLblCustNameValue());
                lblDocumentCustNameValue.setText(observableBorrow.getLblCustNameValue());
                //                 tdtDealingWithBankSince.setDateValue(observableComp.getTdtDealingWithBankSince());
                cboConstitution.setSelectedItem(observableBorrow.getCboConstitution());
                cboCategory.setSelectedItem(observableBorrow.getCboCategory());
            } else if (viewType.equals("JOINT ACCOUNT")) {
                jointAcctDisplay(CommonUtil.convertObjToStr(hash.get("CUSTOMER ID")));
            } else if (viewType.equals("EMP_ID")) {
                //                txtPermittedBy.setText(CommonUtil.convertObjToStr(hash.get("EMPLOYEE ID")));
                //                lblPermittedName.setText(CommonUtil.convertObjToStr(hash.get("EMP NAME")));
                //                lblDesignatedName.setText(CommonUtil.convertObjToStr(hash.get("DESIGNATION")));
            } else if (viewType.equals("SECURITY_CUSTOMER ID")) {
                securityCustDetails(hash);
            } else if (viewType.equals("SECURITY NO")) {
                securityIDDetails(hash);
            } else if (viewType.equals("DISBURSEMENT_DETAILS")) {
                populateDisbursementDetails(hash);
            } else if (viewType.equals("GUARANTOR_ACCT_NO")) {
                //                 observableGuarantor.setTxtGuaranAccNo(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
                //                 //                txtGuaranAccNo.setText(observableGuarantor.getTxtGuaranAccNo());
                //                 observableGuarantor.setCboConstitution_GD(CommonUtil.convertObjToStr(observableGuarantor.getCbmConstitution_GD().getDataForKey(CommonUtil.convertObjToStr(hash.get("CONSTITUTION")))));
                //                cboConstitution_GD.setSelectedItem(observableGuarantor.getCboConstitution_GD());
            } else if (viewType.equals("DEPOSIT_CUSTOMER")) {
                ////System.out.println("### DEPOSIT_CUSTOMER fillData hash : " + hash);
                //                if(observable.isDepositDaily(hash)){
                //                    DateUtil.getStringDate(date);
                //                    ClientUtil.showAlertWindow("This Daily Deposit Not Eligible for Loan "+"\n"+
                //                    "This Deposit Opened On :"+DateUtil.getStringDate((Date)hash.get("DEPOSIT_DT"))+"\n"+
                //                    "Deposit should Run for Minimum period "+ CommonUtil.convertObjToDouble(hash.get("PREMATURE_WITHDRAWAL"))+ " days From the Date of Deposit");
                //                    // "Deposit Open Date is  :"+DateUtil.dateDiff((Date)hash.get("DEPOSIT_DT"), ClientUtil.getCurrentDate())+"\n"+
                //                    return;
                //                }

                double bal = CommonUtil.convertObjToDouble(hash.get("BALANCE")).doubleValue();
                observable.setDepositNo("");
                observable.setDepositNo(CommonUtil.convertObjToStr(hash.get("DEPOSIT_NO")));
                observable.setLblDepositNo(CommonUtil.convertObjToStr(hash.get("DEPOSIT_NO")));
                //                lblDepositNo.setText(CommonUtil.convertObjToStr(hash.get("DEPOSIT_NO")));
                if (bal > 0) {
                    //                    depositSanctionRoundOff(bal*(observable.getEligibleMargin()/100.0));
                    txtLimit_SD.setText(String.valueOf(depositSanctionRoundOff(bal * (observable.getEligibleMargin() / 100.0))));//85
                    //
                    boolean checkSameCustomer = observable.setDetailsForLTD(hash);
                    if (checkSameCustomer) {
                        setAllSanctionFacilityEnableDisable(true);
                        //                        cboTypeOfFacility.setEnabled(false);
                        //                        tdtFacility_Repay_Date.setDateValue(tdtFDate.getDateValue());//for need ltd
                        //                        tdtFacility_Repay_Date.setDateValue(observable.tdtRepaymentDate);//observable.getTdtTDate());
                        //                        tdtTDate.setDateValue(observable.tdtRepaymentDate);
                        //                        rdoInterest_Compound.setSelected(observable.getRdoInterest_Compound());
                        //                        rdoInterest_Simple.setSelected(observable.getRdoInterest_Simple());
                        cboRepayFreq.setSelectedItem(
                                ((ComboBoxModel) cboRepayFreq.getModel()).getDataForKey("1"));
                        txtNoInstallments.setText("1");
                        txtNoInstallmentsFocusLost();
                        //                        rdoSecurityDetails_Unsec.setSelected(true);
                        //                        rdoSecurityDetails_Fully.setSelected(true);
                        //                        deleteAllInterestDetails();
                        populateInterestRateForLTD();
                        //                    observable.setCboProductId(CommonUtil.convertObjToStr(hash.get("PRODUCT ID")));
                        //                    cboProductId.setSelectedItem(
                        //                    ((ComboBoxModel) cboProductId.getModel()).getDataForKey(CommonUtil.convertObjToStr(hash.get("PRODUCT ID"))));
                    } else {
                        setAllSanctionFacilityEnableDisable(false);
                        txtLimit_SD.setText("");
                    }
                } else {
                    //                    ClientUtil.showAlertWindow("Deposit Amount is Zero");
                    ClientUtil.showMessageWindow("Deposit Amount is Zero");
                    return;
                }
            } else if (viewType.equals("EXISTING_CUSTOMER")) {
                //                String prodType = CommonUtil.convertObjToStr(hash.get("PROD_TYPE"));
                txtAccountNo.setText(CommonUtil.convertObjToStr(hash.get("ACT_NUM")));
                //                HashMap ExistingMap = new HashMap();
                //                ExistingMap.put("ACT_NUM",hash.get("ACT_NUM"));
                //                if(prodType.equals("OA")){
                //                    List lst = ClientUtil.executeQuery("getSelectExistingCustId", ExistingMap);
                //                    if(lst!=null && lst.size()>0){
                //                        ExistingMap = (HashMap)lst.get(0);
                //                        hash.put("CUSTOMER ID",ExistingMap.get("CUST_ID"));
                //                    }
                //                }else if(prodType.equals("TD")){
                //                    List lst = ClientUtil.executeQuery("getSelectDepositCustId", ExistingMap);
                //                    if(lst!=null && lst.size()>0){
                //                        ExistingMap = (HashMap)lst.get(0);
                //                        hash.put("CUSTOMER ID",ExistingMap.get("CUST_ID"));
                //                    }
                //                }else if(prodType.equals("TL")){
                //                    List lst = ClientUtil.executeQuery("getSelectLoanCustId", ExistingMap);
                //                    if(lst!=null && lst.size()>0){
                //                        ExistingMap = (HashMap)lst.get(0);
                //                        hash.put("CUSTOMER ID",ExistingMap.get("CUST_ID"));
                //                    }
                //                }
                hash.put("CUSTOMER ID", hash.get("CUST_ID"));
                //                ExistingMap = null;
                //                 observableComp.resetCustomerDetails();
                observableBorrow.setTxtCustID(CommonUtil.convertObjToStr(hash.get("CUSTOMER ID")));
                custInfoDisplay(CommonUtil.convertObjToStr(hash.get("CUSTOMER ID")), loanType);
                validateConstitutionCustID();
                lblCustNameValue.setText(observableBorrow.getLblCustNameValue());
                //                lblSecurityCustNameValue.setText(observableBorrow.getLblCustNameValue());
                lblDocumentCustNameValue.setText(observableBorrow.getLblCustNameValue());
                //                 tdtDealingWithBankSince.setDateValue(observableComp.getTdtDealingWithBankSince());
                cboConstitution.setSelectedItem(observableBorrow.getCboConstitution());
                cboCategory.setSelectedItem(observableBorrow.getCboCategory());
                txtCustMobNo.setText(observableBorrow.getTxtCustomerPhoneNumber());
                //Added by sreekrishnan
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                    HashMap suspenseMap = new HashMap();
                    suspenseMap.put("CUST_ID", txtCustID.getText());
                    List susList =  ClientUtil.executeQuery("getSuspenseAccountOfCustomer", suspenseMap);
                    if (susList != null && susList.size() > 0) {
                        ClientUtil.showMessageWindow("Liability occured for this Customer!");
                    }
                }
            } else if (viewType.equals("APPRAISER_ID")) {
                //                 txtAppraiserId.setText(CommonUtil.convertObjToStr(hash.get("EMPLOYEE_CODE")));
                observableSecurity.setTxtAppraiserId(CommonUtil.convertObjToStr(hash.get("EMPLOYEE_CODE")));
                lblAppraiserNameValue.setText(CommonUtil.convertObjToStr(hash.get("EMPLOYEE_NAME")));
            }
            if (viewType.equals("Edit") || viewType.equals("Delete") || viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION)
                    || viewType.equals(REJECT) || viewType.equals("Enquirystatus")||viewType.equals("CLOSED_DATA")) {
                LinkedHashMap whereMap = new LinkedHashMap();
                whereMap.put("PROD_ID", eachProdId);
                List prodDesclst = ClientUtil.executeQuery("TermLoan.getProdId", whereMap);
                if (prodDesclst != null && prodDesclst.size() > 0) {
                    whereMap = (LinkedHashMap) prodDesclst.get(0);
                    prodDesc = CommonUtil.convertObjToStr(whereMap.get("PROD_DESC"));
                }
                //  String actNewAccNo= CommonUtil.convertObjToStr(hash.get("ACT_NUM"));
                editChargeTable();
                //System.out.println("tableFlag 11==" + tableFlag);
                if (tableFlag) {
                    // accClosingCharges();
                    editChargeAmount(viewType);
                    panRenewalChargeDetails.setEnabled(true);
                    srpChargeDetails.setEnabled(true);
                    table.remove(0);
                    table.setEnabled(true);
                    //System.out.println("viewType in ----------------------" + viewType);
                   
                    if (viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION)
                            || viewType.equals(REJECT)) {
                        calculateTot();
                        table.setEnabled(false);
                    } else {
                        //System.out.println("viewType in -------------11---------" + viewType);
                        calculateTot();
                    }
                }
            }
            //Added By Suresh
            if (viewType.equals("Edit") ||viewType.equals("CLOSED_DATA")) {
                chkMobileBankingTLAD.setEnabled(true);
                chkMobileBankingTLADActionPerformed(null);
            }
        }
        if (hash.containsKey("FROM_AUTHORIZE_LIST_UI")) {
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnView.setEnabled(false);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(true);
        }
        // Added by nithya on 08-09-2017
        if (hash.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")) {
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnView.setEnabled(false);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(true);
        }
        // End
        if (hash.containsKey("FROM_MANAGER_AUTHORIZE_LIST_UI")) {
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnView.setEnabled(false);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(true);
        }
        //View MemberShip Liabolity
        if (txtCustID.getText().length() > 0) {
            btnMembershipLia.setEnabled(true);
        }
        setModified(true);
        oldAccountNum = lblAcctNo_Sanction_Disp.getText().toString();
        if (rejectFlag == 1) {
            btnReject.setEnabled(false);
        }
        checkShareOutstanding();
        if (hash.containsKey("RENEWED_LOAN_NO") && CommonUtil.convertObjToStr(hash.get("RENEWED_LOAN_NO")).length() > 0 && (!(viewType.equals("Edit")))&&(!(viewType.equals("CLOSED_DATA") ))) {
                    if(viewType == AUTHORIZE){
                      tabLimitAmount.setSelectedComponent(panBorrowRenewalCompanyDetails);  
                    }
        }
        if (viewType.equals("CLOSED_DATA")) {
            btnCancel.setEnabled(true);
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
            btnAuthorize.setEnabled(false);
            btnReject.setEnabled(false);
            btnException.setEnabled(false);
            btnView.setEnabled(false);
            btnPrint.setEnabled(false);
        }
      txtRenewCustMobNo.setText(txtCustMobNo.getText());
        //added by rishad 24/07/2015
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_RENEW
                || observable.getActionType() == ClientConstants.ACTIONTYPE_EXTENSION
                || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            HashMap paramMap = new HashMap();
            paramMap.put("ACCT_NUM", hash.get("ACCT_NUM"));
            boolean lock = whenTableRowSelected(paramMap);
            if (lock == true) {
                cboConstitution.setEnabled(false);
                ClientUtil.enableDisable(tabLimitAmount, false);
                return;
            }
           //   btnAuthorize.setEnabled(false);
            btnReject.setEnabled(false);
            btnException.setEnabled(false);

        }
//   
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

    private long depositSanctionRoundOff(double limit) {
        Rounding re = new Rounding();
        long roundOffValue = 0;
        //        CommonUtil.convertObjToLong(
        long longLimit = (long) limit;
        if (observable.getSanctionAmtRoundOff().length() > 0) {
            String roundOff = observable.getSanctionAmtRoundOff();
            if (roundOff.length() > 0) {
                if (roundOff.equals("NEAREST_TENS")) {
                    roundOffValue = 10;
                }
                if (roundOff.equals("NEAREST_HUNDREDS")) {
                    roundOffValue = 100;
                }
                if (roundOff.equals("NEAREST_VALUE")) {
                    roundOffValue = 1;
                }
            }
            //         long lienAmt=(long)(enterAmt/eligibleMargin);
            Rounding rd = new Rounding();
            /* lien marked next higher but limit marked lower  */
            if (!roundOff.equals("NO_ROUND_OFF")) {
                longLimit = rd.lower(longLimit, roundOffValue);
            }
        }
        return longLimit;
    }

    private void updateGuarantorTabCustDetails() {
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

    private void custInfoDisplay(String Cust_ID, String loanType) {
        HashMap hash = new HashMap();
        hash.put("CUST_ID", Cust_ID);
        //        hash.put("CUSTOMER ID",Cust_ID);
        // Remove the old Main CUSTOMER ID
        //        authSignUI.removeAcctLevelCustomer(observableBorrow.getTxtCustID());
        if (observableBorrow.populateBorrowerTabCustomerDetails(hash, false, loanType)) {
            //            setCompanyDetailsEnableDisable(true);
            //            authSignUI.addAcctLevelCustomer(observableBorrow.getTxtCustID());
            updateBorrowerTabCustDetails();
            txtCustID.setText(observableBorrow.getTxtCustID());
            nomineeUi.setMainCustomerId(txtCustID.getText());
            tblBorrowerTabCTable.setModel(observableBorrow.getTblBorrower());
            ////System.out.println("inside custinfo@@@@@@@@");
            observableBorrow.populateBorrowerTabCustFields(hash, CommonUtil.convertObjToStr(tblBorrowerTabCTable.getValueAt(0, 2)));
            updateBorrowerTabCustFields();
            hash = null;
        } else {
            txtCustID.setText(observableBorrow.getTxtCustID());
            nomineeUi.setMainCustomerId(txtCustID.getText());
            //            setCompanyDetailsEnableDisable(false);
        }
        HashMap newMap = new HashMap();
        newMap.put("PROD_ID", lblDocumentProdIdValue.getText());
        List list = (List) ClientUtil.executeQuery("getSelectAcctLevelDocDetails", newMap);
        newMap.put("TermLoanDocumentTO", list);
        list = null;
        //        observableDocument = new TermLoanDocumentDetailsOB();
        observableDocument.resetDocumentDetails();
        //        observableDocument.getTblDocumentTab().setDataArrayList(null, termLoanDocumentDetailsOB.getDocumentTabTitle());
        observableDocument.setTermLoanDocumentTO((ArrayList) (newMap.get("TermLoanDocumentTO")));

        // Add the new Main CUSTOMER ID
       /* authSignUI.addAcctLevelCustomer(observableBorrow.getTxtCustID());
        updateBorrowerTabCustDetails();
        txtCustID.setText(observableBorrow.getTxtCustID());
        tblBorrowerTabCTable.setModel(observableBorrow.getTblBorrower());
        ////System.out.println("inside custinfo@@@@@@@@");
        observableBorrow.populateBorrowerTabCustFields(hash, CommonUtil.convertObjToStr(tblBorrowerTabCTable.getValueAt(0, 2)));
        updateBorrowerTabCustFields();
        hash = null;*/
    }

    private void updateBorrowerTabCustFields() {
        //        txtNetWorth.setText(observableComp.getTxtNetWorth());
        //        txtRiskRating.setText(observableComp.getTxtRiskRating());
        //        tdtCreditFacilityAvailSince.setDateValue(observableComp.getTdtCreditFacilityAvailSince());
        //        tdtDealingWithBankSince.setDateValue(observableComp.getTdtDealingWithBankSince());
        //        cboNatureBusiness.setSelectedItem(observableComp.getCboNatureBusiness());
        //        tdtAsOn.setDateValue(observableComp.getTdtAsOn());
        updateCompanyDetails();
    }

    public void customerIdPopulating(String status) {
        if (status.equals("CANCELLED")) {
            btnAuthorize.setEnabled(false);
            btnReject.setEnabled(false);
        } else if (status.equals("AUTHORIZED")) {
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnView.setEnabled(false);
        } else if (status.equals("REJECTED")) {
            btnCancel.setEnabled(true);
            btnReject.setEnabled(true);
            btnView.setEnabled(false);
        }
    }

    private void updateCompanyDetails() {
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

    private void updateBorrowerTabCustDetails() {
        //        lblCustName_2.setText(observableBorrow.getLblCustName());
        //        lblOpenDate2.setText(observableBorrow.getLblOpenDate());
        //        lblCity_BorrowerProfile_2.setText(observableBorrow.getLblCity());
        //        lblState_BorrowerProfile_2.setText(observableBorrow.getLblState());
        //        lblPin_BorrowerProfile_2.setText(observableBorrow.getLblPin());
        //        lblPhone_BorrowerProfile_2.setText(observableBorrow.getLblPhone());
        //        lblFax_BorrowerProfile_2.setText(observableBorrow.getLblFax());
    }

    private void jointAcctDisplay(String Cust_ID) {
        HashMap hash = new HashMap();
        hash.put("CUST_ID", Cust_ID);
        observableBorrow.populateJointAccntTable(hash);
        tblBorrowerTabCTable.setModel(observableBorrow.getTblBorrower());
        setBorrowerNewOnlyEnable();
        //        authSignUI.addAcctLevelCustomer(Cust_ID);
        hash = null;
    }

    private void securityCustDetails(HashMap map) {
        String strPrevCustID = observableSecurity.getTxtCustID_Security();
        observableSecurity.populateCustDetails(map);
        //        txtCustID_Security.setText(observableSecurity.getTxtCustID_Security());
        //        lblCustName_Security_Display.setText(observableSecurity.getLblCustName_Security_Display());
        if (!(strPrevCustID.equals(observableSecurity.getTxtCustID_Security()))) {
            // If the prev. cust ID doesn't match with the current one then
            // clear the security no.
            observableSecurity.setTxtSecurityNo("");
            //            txtSecurityNo.setText(observableSecurity.getTxtSecurityNo());
            observableSecurity.setTxtMargin("");
            txtMargin.setText(observableSecurity.getTxtMargin());
        }
        strPrevCustID = null;
    }

    private void securityIDDetails(HashMap hash) {
        observableSecurity.populateSecurityID_Value(hash, txtLimit_SD.getText());
        //        txtSecurityNo.setText(observableSecurity.getTxtSecurityNo());
        txtSecurityValue.setText(observableSecurity.getTxtSecurityValue());
    }

    private void populateDisbursementDetails(HashMap hash) {
        observableRepay.populateDisbursementDetails(hash);
        //        txtLaonAmt.setText(observableRepay.getTxtLaonAmt());
    }
    private void cboRepayFreqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboRepayFreqActionPerformed
        // Add your handling code here:
        cboRepayFreqActionPerformed();
    }//GEN-LAST:event_cboRepayFreqActionPerformed
    private void displayNextAccNo() {
        txtNextAccNo.setText("");
        List chargeList = null;
        HashMap whereMap1 = new HashMap();
        whereMap1.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        String loanProdId = ((ComboBoxModel) cboGoldLoanProd.getModel()).getKeyForSelected().toString();
        whereMap1.put("PRODUCT_ID", loanProdId);
        chargeList = (List) (ClientUtil.executeQuery("getSelectNextAccNo", whereMap1));
        if (chargeList != null && chargeList.size() > 0) {
            accountClosingCharge = CommonUtil.convertObjToStr((chargeList.get(0)));
            txtNextAccNo.setText(String.valueOf(accountClosingCharge));
            lblRenewalAcctNo_Sanction_Disp.setText(String.valueOf(accountClosingCharge));
        }
        chargeList = null;
        txtNextAccNo.setEnabled(false);
        txtNextAccNo.setEditable(false);
    }
    private void cboGoldLoanProdAction(boolean createTable){
        this.eachProdId = ((ComboBoxModel) cboGoldLoanProd.getModel()).getKeyForSelected().toString();
        ////System.out.println("eachProdIdeachProdId" + eachProdId);
        lblDocumentProdIdValue.setText(String.valueOf(eachProdId));
        observable.setLoadingProdId(String.valueOf(eachProdId));
        HashMap marginMap = new HashMap();
        LinkedHashMap whereMap = new LinkedHashMap();
        marginMap.put("PROD_ID", eachProdId);
        whereMap.put("PROD_ID", eachProdId);
        List lst = ClientUtil.executeQuery("getSelectMarginPercentage", marginMap);
        cboRepayFreq.setSelectedIndex(0);
        if (lst != null && lst.size() > 0) {
            marginMap = (HashMap) lst.get(0);
            txtMargin.setText(CommonUtil.convertObjToStr(marginMap.get("DEP_ELIGIBLE_LOAN_AMT")));
            ((ComboBoxModel) cboRepayFreq.getModel()).setKeyForSelected(CommonUtil.convertObjToStr(marginMap.get("MAX_PERIOD")));
            if (cboRepayFreq.getSelectedIndex() == 0) {
                cboRepayFreq.setSelectedItem("Yearly");
            }
            observable.setMaximumDaysForLoan(CommonUtil.convertObjToInt(marginMap.get("MAX_PERIOD")));
        }
        
        // 28-10-2019 added By Nithya KD-763
        String goldStockPhotoRequired = "N";
        HashMap photoMap = new HashMap();
        photoMap.put("PROD_ID", eachProdId);
        List photoLst = ClientUtil.executeQuery("getSelectGoldStockPhotoRequired", photoMap);
        if(photoLst != null && photoLst.size() > 0){
            photoMap = (HashMap) photoLst.get(0);
            goldStockPhotoRequired = CommonUtil.convertObjToStr(photoMap.get("GOLD_STOCK_PHOTO_REQUIRED"));
        }
        if(goldStockPhotoRequired.equalsIgnoreCase("Y")){
           tabLimitAmount.add(panStockDetails, "Gold Stock Details");
           btnRenewLoad.setEnabled(false);
           panWebCamStockPhoto.setVisible(false);
           panWebCamRenewalStockPhoto.setVisible(false);
           btnAddPhoto.setEnabled(false);
           btnRenewPhotoRemove.setEnabled(false);
           btnLoad.setEnabled(true);
           btnPhotoRemove.setEnabled(true);
           //Check for webcam parameter
            String integrateWebCam = CommonUtil.convertObjToStr(TrueTransactMain.CBMSPARAMETERS.get("WEBCAM_INTEGRATION"));
            System.out.println("integrateWebCam :: " + integrateWebCam);
            if (integrateWebCam.equals("Y")) {
                panWebCamStockPhoto.setVisible(true);
                panWebCamRenewalStockPhoto.setVisible(false);
                btnScanRenewalStockPhoto.setEnabled(false);
                btnRenewalStockPhotoCapture.setEnabled(false);
            } else {
                panWebCamStockPhoto.setVisible(false);
                panWebCamRenewalStockPhoto.setVisible(false);
            }
        }else{
           tabLimitAmount.remove(panStockDetails);
        }
        // END

        List prodDesclst = ClientUtil.executeQuery("TermLoan.getProdId", whereMap);
        if (prodDesclst != null && prodDesclst.size() > 0) {
            whereMap = (LinkedHashMap) prodDesclst.get(0);
            prodDesc = CommonUtil.convertObjToStr(whereMap.get("PROD_DESC"));
            cboRepaymentType.setSelectedItem(observableRepay.getCbmRepayType().getDataForKey(CommonUtil.convertObjToStr(whereMap.get("REPAYMENT_TYPE"))));
            cboRepaymentTypeActionPerformed(null);
            int intPayFreq = CommonUtil.convertObjToInt(whereMap.get("REPAYMENT_FREQ"));
            cboRepayFreq.setSelectedItem(observable.getCbmRepayFreq().getDataForKey(CommonUtil.convertObjToStr(intPayFreq))); // Removed the comment by nithya on 29-08-2016
            //cboRepayFreq.setSelectedItem(observable.getCbmRepayFreq().getDataForKey(CommonUtil.convertObjToStr(whereMap.get("REPAYMENT_FREQ")))); // Removed the comment by nithya on 29-08-2016
        }
        nomineeUi.resetNomineeTab();
        //if(!createTable){//Modified by nithya on 0010600: GOLD LOAN(GL)
        if (transNew) {
            createChargeTable(); //bbb
            // //System.out.println("tableFlag =33========"+tableFlag);
            if (tableFlag) {
                chargeAmount();
            }
        }
       // }


        txtNextAccNo.setText("");
        List chargeList = null;
        HashMap whereMap1 = new HashMap();
        whereMap1.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        String loanProdId = ((ComboBoxModel) cboGoldLoanProd.getModel()).getKeyForSelected().toString();
        whereMap1.put("PRODUCT_ID", loanProdId);
        chargeList = (List) (ClientUtil.executeQuery("getSelectNextAccNo", whereMap1));
        if (chargeList != null && chargeList.size() > 0) {
            accountClosingCharge = CommonUtil.convertObjToStr((chargeList.get(0)));
            ////System.out.println("accountClosingCharge" + accountClosingCharge);
            txtNextAccNo.setText(String.valueOf(accountClosingCharge));
        }
        chargeList = null;
        //transNew = true;
        // btnnewActionPerformed();
        txtNextAccNo.setEnabled(false);
        txtNextAccNo.setEditable(false);
        displayNextAccNo(); 
        //Added by sreekrishnan
        HashMap goldMap = new HashMap();
        goldMap.put("LOOKUP_ID", "GOLD_CONFIGURATION");
        List goldList = ClientUtil.executeQuery("getDefaultGoldPurity", goldMap);
        ////System.out.println("getGoldLoanProductIDs" + goldList);        
        if (goldList != null && goldList.size() > 0) {
            goldMap = (HashMap) goldList.get(0);
            if(goldMap.get("DEFAULT_ITEM")!=null  && !goldMap.get("DEFAULT_ITEM").equals("") && goldMap.get("DEFAULT_ITEM").equals("Y")){
                cboPurityOfGold.setSelectedItem(goldMap.get("VALUE"));
                cboPurityOfGoldActionPerformed(null);
            }
        }
    }
    private void cboGoldLoanProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboGoldLoanProdActionPerformed
        // TODO add your handling code here:
        if (cboGoldLoanProd.getSelectedIndex() > 0) {
            HashMap checkMap = new HashMap();
            checkMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            checkMap.put("PROD_ID", (String) ((ComboBoxModel) cboGoldLoanProd.getModel()).getKeyForSelected());
            List actList = (List) (ClientUtil.executeQuery("getAccountMaintenanceCount", checkMap));
            if (actList != null && actList.size() > 0) {
                checkMap = (HashMap) actList.get(0);
                int cnt = CommonUtil.convertObjToInt(checkMap.get("CNT"));
                if (cnt == 0) {
                    ClientUtil.displayAlert("Branch Account Number Settings Not Done. Please Check !!!");
                    btnCancelActionPerformed(null);
                }
            }
        }
        boolean createTable = false;
//        if(evt != null){
//           createTable = true;  
//        }
        cboGoldLoanProdAction(createTable);
        txtLimit_SD.setText("");//this line added by Anju Anand for Mantid Id: 0010365
    }//GEN-LAST:event_cboGoldLoanProdActionPerformed
    private void txtRenewalNetWeightFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRenewalNetWeightFocusLost
        double grossWeight = CommonUtil.convertObjToDouble(txtRenewalGrossWeight.getText()).doubleValue();
        double netWeight = CommonUtil.convertObjToDouble(txtRenewalNetWeight.getText()).doubleValue();
        //System.out.println("grossWeight === " + grossWeight + "netWeight===" + netWeight);
        //System.out.println("rrrrr === " + cboRenewalPurityOfGold.getSelectedIndex());
        if (grossWeight < netWeight) {
            ClientUtil.showAlertWindow("Gross Weight should be grater than net Weight!!!");
            return;
        }
        if (cboRenewalPurityOfGold.getSelectedItem() !=null && !cboRenewalPurityOfGold.getSelectedItem().equals("")) {
            calcEligibleLoanAmountRenewal();
        }
    }//GEN-LAST:event_txtRenewalNetWeightFocusLost
    private void cboRenewalAppraiserIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboRenewalAppraiserIdActionPerformed
        if (cboRenewalAppraiserId.getSelectedIndex() > 0) {
            HashMap hashMap = new HashMap();
            observableSecurity.setTxtRenewalAppraiserId(CommonUtil.convertObjToStr(cboRenewalAppraiserId.getSelectedItem()));
            hashMap.put("EMPLOYEE_CODE", CommonUtil.convertObjToStr(observable.getCbmRenewalAppraiserId().getKeyForSelected()));
            List lst = ClientUtil.executeQuery("getAppraiserName", hashMap);
            if (lst != null && lst.size() > 0) {
                hashMap = new HashMap();
                hashMap = (HashMap) lst.get(0);
                String apprName = (String) hashMap.get("EMPLOYEE_NAME");
                lblRenewalAppraiserNameValue.setText(apprName);
            } else {
                lblRenewalAppraiserNameValue.setText("");
            }
        } else {
            lblRenewalAppraiserNameValue.setText("");
        }
    }//GEN-LAST:event_cboRenewalAppraiserIdActionPerformed

    private void cboRenewalPurityOfGoldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboRenewalPurityOfGoldActionPerformed
        // TODO add your handling code here:
        if (cboRenewalPurityOfGold.getSelectedItem() !=null && !cboRenewalPurityOfGold.getSelectedItem().equals("")) {
            HashMap purityMap = new HashMap();
            String purity = CommonUtil.convertObjToStr(cboRenewalPurityOfGold.getSelectedItem());
            purityMap.put("PURITY", purity);
            purityMap.put("TODAY_DATE", curr_dt);
            List lst = ClientUtil.executeQuery("getSelectTodaysMarketRate", purityMap);
            if (lst != null && lst.size() > 0) {
                purityMap = (HashMap) lst.get(0);
                perGramAmt = CommonUtil.convertObjToDouble(purityMap.get("PER_GRAM_RATE")).doubleValue();
                //System.out.println("gramamt>>>>>22222>>>>>222=====" + perGramAmt + "puritymap>>>>>" + purityMap);
                if (cboRenewalPurityOfGold.getSelectedItem() !=null && !cboRenewalPurityOfGold.getSelectedItem().equals("")) {
                    calcRenewalEligibleLoanAmount();//Renewal
                }
                //                setTotalSecValue(String.valueOf(totSecurityValue));
                //                setTotalMarginValue(String.valueOf(totMarginAmt));
                //                setTotalEligibleValue(String.valueOf(totEligibleAmt));
            }
        }
    }//GEN-LAST:event_cboRenewalPurityOfGoldActionPerformed
    private void cboRnwGoldLoanProdAction() {
        this.rnwEachProdId = ((ComboBoxModel) cboRnwGoldLoanProd.getModel()).getKeyForSelected().toString();
        this.eachProdId = ((ComboBoxModel) cboGoldLoanProd.getModel()).getKeyForSelected().toString();
        lblDocumentProdIdValue.setText(rnwEachProdId);
        observable.setRnwLoadingProId(eachProdId);
        observable.setLoadingProdId(rnwEachProdId);
        HashMap marginMap = new HashMap();
        LinkedHashMap whereMap = new LinkedHashMap();
        marginMap.put("PROD_ID", rnwEachProdId);
        whereMap.put("PROD_ID", rnwEachProdId);        
        List lst = ClientUtil.executeQuery("getSelectMarginPercentage", marginMap);
        if (lst != null && lst.size() > 0) {
            marginMap = (HashMap) lst.get(0);
            txtRenewalMargin.setText(CommonUtil.convertObjToStr(marginMap.get("DEP_ELIGIBLE_LOAN_AMT")));
            ((ComboBoxModel) cboRenewalRepayFreq.getModel()).setKeyForSelected(CommonUtil.convertObjToStr(marginMap.get("MAX_PERIOD")));            
            if (cboRenewalRepayFreq.getSelectedIndex() == 0) {
                cboRenewalRepayFreq.setSelectedItem("Yearly");
            } 
            if (cboRenewalRepayFreq.getSelectedIndex() == -1) {//Added by nithya on 06-07-2019 for KD 546 - New Gold Loan -45 days maturity type
                cboRenewalRepayFreq.setSelectedItem("USER_DEFINED_GOLD_LOAN");
             }
             observable.setMaximumDaysForRenewLoan(CommonUtil.convertObjToInt(marginMap.get("MAX_PERIOD")));
        } 
        
        // added by nithya KD-763 at 28-10-2019
        String goldStockPhotoRequired = "N";
        HashMap photoMap = new HashMap();
        photoMap.put("PROD_ID", rnwEachProdId);
        List photoLst = ClientUtil.executeQuery("getSelectGoldStockPhotoRequired", photoMap);
        if(photoLst != null && photoLst.size() > 0){
            photoMap = (HashMap) photoLst.get(0);
            goldStockPhotoRequired = CommonUtil.convertObjToStr(photoMap.get("GOLD_STOCK_PHOTO_REQUIRED"));
        }
        if(goldStockPhotoRequired.equalsIgnoreCase("Y")){
           tabLimitAmount.add(panStockDetails, "Gold Stock Details");
           btnLoad.setEnabled(false);
           btnPhotoRemove.setEnabled(false);
           if(lblPhoto.getIcon() == null){
            btnAddPhoto.setEnabled(false);
           }
           btnRenewLoad.setEnabled(true);
           btnAddPhoto.setEnabled(true);
           btnRenewPhotoRemove.setEnabled(true);
        }else{
           tabLimitAmount.remove(panStockDetails);
        }
        // END
        List prodDesclst = ClientUtil.executeQuery("TermLoan.getProdId", whereMap);
        if (prodDesclst != null && prodDesclst.size() > 0) {
            whereMap = (LinkedHashMap) prodDesclst.get(0);
            rnewProdDesc = CommonUtil.convertObjToStr(whereMap.get("PROD_DESC"));
        }
//        nomineeUi.resetNomineeTab();
//        if (transNew) {
//            createChargeTable(); //bbb
//            // //System.out.println("tableFlag =33========"+tableFlag);
//            if (tableFlag) {
//                chargeAmount();
//            }
//        }



        lblRenewalAcctNo_Sanction_Disp.setText("");
        List chargeList = null;
        HashMap whereMap1 = new HashMap();
        whereMap1.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        String loanProdId = ((ComboBoxModel) cboRnwGoldLoanProd.getModel()).getKeyForSelected().toString();
        whereMap1.put("PRODUCT_ID", loanProdId);
        chargeList = (List) (ClientUtil.executeQuery("getSelectNextAccNo", whereMap1));
        if (chargeList != null && chargeList.size() > 0) {
           String  nxtacno = CommonUtil.convertObjToStr((chargeList.get(0)));
            ////System.out.println("accountClosingCharge" + accountClosingCharge);
            lblRenewalAcctNo_Sanction_Disp.setText(nxtacno);
        }
        chargeList = null;
        //transNew = true;
        // btnnewActionPerformed();
       // lblRenewalAcctNo_Sanction_Disp.setEnabled(false);
        //displayNextAccNo(); 
        //Added by sreekrishnan
        HashMap goldMap = new HashMap();
        goldMap.put("LOOKUP_ID", "GOLD_CONFIGURATION");
        List goldList = ClientUtil.executeQuery("getDefaultGoldPurity", goldMap);
        ////System.out.println("getGoldLoanProductIDs" + goldList);        
        if (goldList != null && goldList.size() > 0) {
            goldMap = (HashMap) goldList.get(0);
            if(goldMap.get("DEFAULT_ITEM")!=null  && !goldMap.get("DEFAULT_ITEM").equals("") && goldMap.get("DEFAULT_ITEM").equals("Y")){               
                cboRenewalPurityOfGold.setSelectedItem(goldMap.get("VALUE"));
                cboRenewalPurityOfGoldActionPerformed(null);
            }
        }
    }
    private void cboRnwGoldLoanProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboRnwGoldLoanProdActionPerformed
        // TODO add your handling code here:
        cboRnwGoldLoanProdAction();
        txtRenewalNetWeightFocusLost(null); // Added by nithya on 17-09-2021 - KD-3037
        if(CommonUtil.convertObjToDouble(txtRenewalLimit_SD.getText())>0){
        if (cboCategory.getSelectedIndex() <= 0) {
                ClientUtil.showAlertWindow("Category should not be empty !!!!");
                txtRenewalLimit_SD.setText("");
                return;
            }
        
            HashMap whereMap = new HashMap();
            whereMap.put("CATEGORY_ID", observableBorrow.getCbmCategory().getKeyForSelected());
            whereMap.put("AMOUNT", getBigDecimal(CommonUtil.convertObjToDouble(txtRenewalLimit_SD.getText())));
             String loanProdId = ((ComboBoxModel) cboRnwGoldLoanProd.getModel()).getKeyForSelected().toString();
            whereMap.put("PROD_ID",loanProdId);
            whereMap.put("FROM_DATE", setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtRenewalAccountOpenDate.getDateValue())));
            whereMap.put("TO_DATE", setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtRenewalDemandPromNoteExpDate.getDateValue())));
            
             chargeAmount();// Moved code by nithya on 28-04-2020 for KD-1609
            ArrayList interestList = null;
            if (DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(tdtRenewalAccountOpenDate.getDateValue()), DateUtil.getDateMMDDYYYY(tdtRenewalDemandPromNoteExpDate.getDateValue())) > 0) { // Added by nithya on 28-04-2020 for KD-1609
                ArrayList interestType = (ArrayList) ClientUtil.executeQuery("getSelectProductTermLoanInterestTO", whereMap);

                if (interestType != null && interestType.size() > 0) {
                    TermLoanInterestTO termLoanInterestTO = (TermLoanInterestTO) interestType.get(0);
                    txtRenewalInter.setText(CommonUtil.convertObjToStr(termLoanInterestTO.getInterest()));
                    txtRenewalPenalInter.setText(CommonUtil.convertObjToStr(termLoanInterestTO.getPenalInterest()));
                    ////System.out.println("interestList" + interestList);
                    txtRenewalInter.setEnabled(false);
                    txtRenewalPenalInter.setEnabled(false);
                } else {
                    ClientUtil.showAlertWindow("Rate of interest is not set for this product");
                    return;
                }
           }
        
        }
    }//GEN-LAST:event_cboRnwGoldLoanProdActionPerformed

    private void btnClosedAccountsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClosedAccountsActionPerformed
        // TODO add your handling code here:
         observable.setActionType(ClientConstants.ACTIONTYPE_VIEW_MODE);
        btnSave.setEnabled(false);
        popUp("CLOSED_DATA");
    }//GEN-LAST:event_btnClosedAccountsActionPerformed

    private void btnGoldLiabilityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoldLiabilityActionPerformed
        // TODO add your handling code here:
          if (txtCustID.getText().length() > 0) {
            new MembershipLiabilityUI(CommonUtil.convertObjToStr(txtCustID.getText()), CommonUtil.convertObjToStr(txtAccountNo.getText()), "GOLD_LOAN").show();
        }
    }//GEN-LAST:event_btnGoldLiabilityActionPerformed

private void cboRenewalRepayFreqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboRenewalRepayFreqActionPerformed
// TODO add your handling code here:
     calculateSanctionToDate();
     calculateSanctionToDateAndDepositoryForRenewal();
}//GEN-LAST:event_cboRenewalRepayFreqActionPerformed

private void cboRepaymentTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboRepaymentTypeActionPerformed
        // TODO add your handling code here:

        String repayType = CommonUtil.convertObjToStr(cboRepaymentType.getSelectedItem());

        if (repayType.length() > 0) {
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                if (repayType.equals("EMI")) {
                    // When the Repayment type is EMI the Repayment Frequency
                    // must be Monthly                   
                    cboRepayFreq.setSelectedItem("Monthly");
                } else if (repayType.equals("EQI")) {
                    // When the Repayment type is EQI the Repayment Frequency
                    // must be Quaterly
                    cboRepayFreq.setSelectedItem("Quaterly");
                } else if (repayType.equals("EHI")) {
                    cboRepayFreq.setSelectedItem("Half Yearly");
                } else if (repayType.equals("EYI")) {
                    cboRepayFreq.setSelectedItem("Yearly");
                } else if (repayType.equals("Uniform Principle EMI")) { //This block added by Rajesh
                    if (cboRepayFreq.getSelectedIndex() == 0) {	//bb1
                        cboRepayFreq.setSelectedItem("Monthly");
                    } 
                    // if(cboRepayFreq.getSelectedItem().equals("") || cboRepayFreq.getSelectedItem() ==null)
                    //     cboRepayFreq.setSelectedItem("Monthly");
                }
            }
            if (repayType.equals("EMI")) {
                    // When the Repayment type is EMI the Repayment Frequency
                    // must be Monthly
                    cboRepayFreq.setSelectedItem("Monthly");
                    tdtEmiDate.setEnabled(true);
                    tdtEmiDate.setVisible(true);
                    lblEmiDate.setVisible(true);
            }else{
                tdtEmiDate.setEnabled(false);
                tdtEmiDate.setVisible(false);
                lblEmiDate.setVisible(false);
                tdtEmiDate.setDateValue("");
            } 
            //if (repayType.equals("EMI")) {
            //    chkDiminishing.setSelected(true);
            //} else {
            //    chkDiminishing.setSelected(false);
            //}
            ////System.out.println("OOOO ====" + observable.isDailyLoan() + "AAAA ===" + repayType);
            // if(observable.isDailyLoan() && repayType.equals("Uniform Principle EMI"))
            // {
            //    cboRepayType.setSelectedItem("User Defined");
            //   cboRepayFreq.setSelectedItem("User Defined");
            //  }
        }
}//GEN-LAST:event_cboRepaymentTypeActionPerformed

private void btnRepaySheduleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRepaySheduleActionPerformed
         EMI_CalculateActionPerformed(true);
}//GEN-LAST:event_btnRepaySheduleActionPerformed

    private void txtMobileNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMobileNoFocusLost
        // TODO add your handling code here:
        tdtMobileSubscribedFrom.setDateValue(CommonUtil.convertObjToStr(curr_dt.clone()));
    }//GEN-LAST:event_txtMobileNoFocusLost

    private void lblPhotoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPhotoMouseClicked
        // TODO add your handling code here:
      
    }//GEN-LAST:event_lblPhotoMouseClicked

    private void btnLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadActionPerformed
        // TODO add your handling code here:
        // Added by nithya on 29-10-2019 for KD-763 Need Gold ornaments photo saving option
        final JFileChooser objJFileChooser = new JFileChooser();
        objJFileChooser.setAccessory(new com.see.truetransact.clientutil.ImagePreview(objJFileChooser));
        final ImageFileFilter objImageFileFilter = new ImageFileFilter();
        final File selFile;
        byte[] byteArray;
        StringBuffer filePath;
        String fileName;
        
        objJFileChooser.setFileFilter(objImageFileFilter);
        objJFileChooser.removeChoosableFileFilter(objJFileChooser.getAcceptAllFileFilter());
        if (objJFileChooser.showOpenDialog(null) == objJFileChooser.APPROVE_OPTION){
            selFile = objJFileChooser.getSelectedFile();
            filePath = new StringBuffer(selFile.getAbsolutePath());
            try{
                lblPhoto.setIcon(new ImageIcon(CommonUtil.convertObjToStr(filePath)));
                fileName = filePath.substring(filePath.lastIndexOf("."));                
                final FileInputStream reader = new FileInputStream(selFile);
                final int size = reader.available();
                byteArray = new byte[size];
                reader.read(byteArray);
                reader.close();  
                observable.setPhotoFile(fileName);
                observable.setPhotoByteArray(byteArray);
                btnPhotoRemove.setEnabled(true);
                byteArray = null;
            } catch (Exception e) {
                parseException.logException(e,true);
            }
        }
    }//GEN-LAST:event_btnLoadActionPerformed

    private void lblRenewPhotoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblRenewPhotoMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lblRenewPhotoMouseClicked

    private void btnRenewLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRenewLoadActionPerformed
        // TODO add your handling code here:
        // Added by nithya on 29-10-2019 for KD-763	Need Gold ornaments photo saving option
        final JFileChooser objJFileChooser = new JFileChooser();
        objJFileChooser.setAccessory(new com.see.truetransact.clientutil.ImagePreview(objJFileChooser));
        final ImageFileFilter objImageFileFilter = new ImageFileFilter();
        final File selFile;
        byte[] byteArray;
        StringBuffer filePath;
        String fileName;        
        objJFileChooser.setFileFilter(objImageFileFilter);
        objJFileChooser.removeChoosableFileFilter(objJFileChooser.getAcceptAllFileFilter());
        if (objJFileChooser.showOpenDialog(null) == objJFileChooser.APPROVE_OPTION){        
            selFile = objJFileChooser.getSelectedFile();
            filePath = new StringBuffer(selFile.getAbsolutePath());
            try{
                lblRenewPhoto.setIcon(new ImageIcon(CommonUtil.convertObjToStr(filePath)));
                fileName = filePath.substring(filePath.lastIndexOf("."));                
                final FileInputStream reader = new FileInputStream(selFile);
                final int size = reader.available();
                byteArray = new byte[size];
                reader.read(byteArray);
                reader.close();  
                observable.setPhotoFile(fileName);
                observable.setRenewalPhotoByteArray(byteArray);
                btnPhotoRemove.setEnabled(true);
                byteArray = null;
            } catch (Exception e) {
                parseException.logException(e,true);
            }
        }
    }//GEN-LAST:event_btnRenewLoadActionPerformed

    private void btnAddPhotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddPhotoActionPerformed
       // Added by nithya on 29-10-2019 for KD-763	Need Gold ornaments photo saving option
        if(lblPhoto.getIcon() == null){
        }else{
          lblRenewPhoto.setIcon(lblPhoto.getIcon());   
        }       
        if(observable.getPhotoByteArray() != null){
            observable.setRenewalPhotoByteArray(observable.getPhotoByteArray());
        }
    }//GEN-LAST:event_btnAddPhotoActionPerformed

    private void btnPhotoRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPhotoRemoveActionPerformed
        // TODO add your handling code here:
        // Added by nithya on 29-10-2019 for KD-763	Need Gold ornaments photo saving option
        lblPhoto.setIcon(null);
        btnPhotoRemove.setEnabled(false);
        observable.setPhotoFile(null);
        observable.setPhotoByteArray(null);
    }//GEN-LAST:event_btnPhotoRemoveActionPerformed

    private void btnRenewPhotoRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRenewPhotoRemoveActionPerformed
        // TODO add your handling code here:
        // Added by nithya on 29-10-2019 for KD-763	Need Gold ornaments photo saving option
        lblRenewPhoto.setIcon(null);
        btnRenewPhotoRemove.setEnabled(false);
        observable.setPhotoFile(null);
        observable.setRenewalPhotoByteArray(null);
    }//GEN-LAST:event_btnRenewPhotoRemoveActionPerformed

    private void txtMobileNoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMobileNoKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if (((c < '0') || (c > '9')) && (c != java.awt.event.KeyEvent.VK_SPACE)) {
            evt.consume();  // ignore event
        }
    }//GEN-LAST:event_txtMobileNoKeyTyped

    private void btnScanStockPhotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnScanStockPhotoActionPerformed
        // TODO add your handling code here:
        webCamOpenedForStockPhoto= true;
        webCamOpenedForStockRenewalPhoto = false;
        cameraFlag = true;
        new Thread(this).start();
        btnScanStockPhoto.setEnabled(false);
        btnStockPhotoCapture.setEnabled(true);
        btnPhotoRemove.setEnabled(true);
    }//GEN-LAST:event_btnScanStockPhotoActionPerformed

    private void btnStockPhotoCaptureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStockPhotoCaptureActionPerformed
        // TODO add your handling code here:
        new Thread(this).stop();
        cameraFlag = false;
        webCamOpenedForStockPhoto = false;
        btnPhotoRemove.setEnabled(true);
        btnStockPhotoCapture.setEnabled(false);
        btnScanStockPhoto.setEnabled(true);
        SaveImage(lblPhoto);
    }//GEN-LAST:event_btnStockPhotoCaptureActionPerformed

    private void btnScanRenewalStockPhotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnScanRenewalStockPhotoActionPerformed
        // TODO add your handling code here:
        webCamOpenedForStockPhoto= false;
        webCamOpenedForStockRenewalPhoto = true;
        cameraFlag = true;
        new Thread(this).start();
        btnScanRenewalStockPhoto.setEnabled(false);
        btnRenewalStockPhotoCapture.setEnabled(true);
        btnPhotoRemove.setEnabled(true);
        
    }//GEN-LAST:event_btnScanRenewalStockPhotoActionPerformed

    private void btnRenewalStockPhotoCaptureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRenewalStockPhotoCaptureActionPerformed
        // TODO add your handling code here:        
        new Thread(this).stop();
        cameraFlag = false;
        webCamOpenedForStockRenewalPhoto = false;
        btnPhotoRemove.setEnabled(true);
        btnRenewalStockPhotoCapture.setEnabled(false);
        btnScanRenewalStockPhoto.setEnabled(true);
        SaveImage(lblRenewPhoto);
        
    }//GEN-LAST:event_btnRenewalStockPhotoCaptureActionPerformed
  
public void getTotalRenewalAmt()
   {
        double serviceTax = 0.0;
        double val=CommonUtil.convertObjToDouble(transDetailsUI.calculatetotalRecivableAmountForRenewalLoan());//observable.getRen
        double totCharge=CommonUtil.convertObjToDouble(txtCharges.getText());
        if(CommonUtil.convertObjToStr(lblServiceTaxVal.getText()).length() > 0){
           serviceTax = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(lblServiceTaxVal.getText()));
        }    
        double  newval=val+totCharge+serviceTax;        
        if(viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT))
        {
          txtRenewalLimit_SD.setText(CommonUtil.convertObjToStr(transDetailsUI.getLimitAmount()));
          observable.setTotRecivableAmt(CommonUtil.convertObjToDouble(transDetailsUI.getLimitAmount()));
        }
        else
        {
          txtRenewalLimit_SD.setText(String.valueOf(newval));
          observable.setTotRecivableAmt(val);//newval);//babu comm on 12-05-2014
        }
        //System.out.println("newvalnewvalnewvalnewvalnewval 1111==="+newval);
        //System.out.println("LImit amt 1111==="+transDetailsUI.getLimitAmount());
        
   }
    private void txtChargesFocusLost(java.awt.event.FocusEvent evt) {                                     
        // TODO add your handling code here:
        //   getTotalRenewalAmt();
    }

    private void cboRepayFreqActionPerformed() {
        if (CommonUtil.convertObjToStr(cboRepayFreq.getSelectedItem()).equals("User Defined") || CommonUtil.convertObjToStr(cboRepayFreq.getSelectedItem()).equals("Lump Sum")) {
            if ((observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) || (viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT))) {
                //                tdtTDate.setEnabled(false);
            } else {
                //                tdtTDate.setEnabled(true);
            }
            //            moratorium_Given_Calculation();
        } else {
            //            tdtTDate.setEnabled(false);
            calculateSanctionToDate();
            calculateSanctionToDateAndDepository();
        }
        populatePeriodDifference();
    }

    private void displayAlert(String message) {
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }

    //To enable and disable the textfields and buttons when NEW button is pressed
    private void newPressedEnableDisable(boolean val) {
        //        poaUI.setPoANewOnlyEnable();
        //        btnCustomerID_GD.setEnabled(!val);
        //        btnAccNo.setEnabled(!val);
    }

    //To enable or disable text fields and buttons of all Customer fields
    //in the tabbed panel
    private void setbtnCustEnableDisable(boolean val) {
        btnCustID.setEnabled(val);
        //        btnCustomerID_GD.setEnabled(val);
        //        btnAccNo.setEnabled(val);
        //        authSignUI.setbtnCustEnableDisable(val);
        //        poaUI.setbtnCustEnableDisable(val);
    }

    private void setBorrowerDetailsEnableDisable(boolean val) {
        tdtDealingWithBankSince.setEnabled(false);
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

    private void setCompanyDetailsEnableDisable(boolean val) {
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
    private void setAllTableBtnsEnableDisable(boolean val) {
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
    private void setSanctionFacilityBtnsEnableDisable(boolean val) {
        //        btnNew1.setEnabled(val);
        //        btnSave1.setEnabled(val);
        //        btnDelete1.setEnabled(val);
        //        btnLTD.setEnabled(val);
    }

    private void setAllSanctionFacilityEnableDisable(boolean val) {
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

    private void setSanctionMainBtnsEnableDisable(boolean val) {
        //        btnNew2_SD.setEnabled(val);
        //        btnSave2_SD.setEnabled(val);
        //        btnDelete2_SD.setEnabled(val);
    }

    private void setAllSanctionMainEnableDisable(boolean val) {
        //        txtSanctionSlNo.setEnabled(val);
        //        txtSanctionSlNo.setEditable(false);
        //        txtSanctionNo.setEnabled(val);
        //        tdtSanctionDate.setEnabled(val);
        //        cboSanctioningAuthority.setEnabled(val);
        //        txtSanctionRemarks.setEnabled(val);
        //        cboModeSanction.setEnabled(val);
    }

    private void setAllFacilityDetailsEnableDisable(boolean val) {
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

    private void setFacilityBtnsEnableDisable(boolean val) {
        //        btnFacilityDelete.setEnabled(val);
        //        btnFacilitySave.setEnabled(val);
    }

    private void setAllSecurityDetailsEnableDisable(boolean val) {
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

    private void setSecurityBtnsOnlyNewEnable() {
        //        btnSecurityDelete.setEnabled(false);
        //        btnSecurityNew.setEnabled(true);
        //        btnSecuritySave.setEnabled(false);
    }

    private void setSecurityBtnsOnlyNewSaveEnable() {
        //        btnSecurityDelete.setEnabled(false);
        //        btnSecurityNew.setEnabled(true);
        //        btnSecuritySave.setEnabled(true);
    }

    private void setAllSecurityBtnsEnableDisable(boolean val) {
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

    private void setAllRepaymentBtnsEnableDisable(boolean val) {
        //        btnRepayment_Delete.setEnabled(val);
        //        btnRepayment_New.setEnabled(val);
        //        btnRepayment_Save.setEnabled(val);
    }

    private void setRepaymentBtnsEnableDisable(boolean val) {
        //        btnRepayment_Delete.setEnabled(!val);
        //        btnRepayment_New.setEnabled(val);
        //        btnRepayment_Save.setEnabled(!val);
    }

    private void setRepaymentNewOnlyEnable() {
        //        btnRepayment_Delete.setEnabled(false);
        //        //        if (loanType.equals("LTD"))
        //        //            btnRepayment_New.setEnabled(false);
        //        //        else
        //        btnRepayment_New.setEnabled(true);
        //        btnRepayment_Save.setEnabled(false);
        //        btnEMI_Calculate.setEnabled(false);
    }

    private void setRepaymentDeleteOnlyDisbale() {
        //        btnRepayment_Delete.setEnabled(false);
        //        if (loanType.equals("LTD"))
        //            btnRepayment_New.setEnabled(false);
        //        else
        //            btnRepayment_New.setEnabled(true);
        //        btnRepayment_Save.setEnabled(true);
        //        btnEMI_Calculate.setEnabled(true);
    }

    private void setAllGuarantorDetailsEnableDisable(boolean val) {
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

    private void setAllInstitGuarantorDetailsEnableDisable(boolean val) {
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

    private void setGuarantorDetailsNewOnlyEnabled() {
        //        btnGuarantorNew.setEnabled(true);
        //        btnGuarantorSave.setEnabled(false);
        //        btnGuarantorDelete.setEnabled(false);
    }

    private void setGuarantorDetailsDeleteOnlyDisabled() {
        //        btnGuarantorNew.setEnabled(true);
        //        btnGuarantorSave.setEnabled(true);
        //        btnGuarantorDelete.setEnabled(false);
    }

    private void setAllGuarantorBtnsEnableDisable(boolean val) {
        //        btnGuarantorNew.setEnabled(val);
        //        btnGuarantorSave.setEnabled(val);
        //        btnGuarantorDelete.setEnabled(val);
        //        rdoGuarnIndividual.setEnabled(val);
        //        rdoGuarnInsititutional.setEnabled(val);
        //
    }

    private void setAllDocumentDetailsEnableDisable(boolean val) {
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
        ClientUtil.enableDisable(panTabDetails_DocumentDetails, val);
    }

    private void setDocumentToolBtnEnableDisable(boolean val) {
        btnSave_DocumentDetails.setEnabled(val);
    }

    private void setAllInterestDetailsEnableDisable(boolean val) {
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

    private void setInterestDetailsOnlyNewEnabled() {
        //        btnInterestMaintenanceNew.setEnabled(true);
        //        btnInterestMaintenanceSave.setEnabled(false);
        //        btnInterestMaintenanceDelete.setEnabled(false);
    }

    public void calculateOpeningServiceTaxOnEditCharge() {       
        table.revalidate();
        List taxSettingsList = new ArrayList();
        System.out.println("serviceTaxApplMap.....1 ::" + serviceTaxApplMap);
        System.out.println("table.getRowCount() :: "+ table.getRowCount());
        if (serviceTaxApplMap != null && serviceTaxApplMap.size() > 0) {            
            for (int i = 0; i < table.getRowCount(); i++) {
                double chrgamt = 0;
                HashMap serviceTaSettingsMap = new HashMap();
                System.out.println("boolean val "+ table.getValueAt(i, 0));
                System.out.println("charge val "+ CommonUtil.convertObjToStr(table.getValueAt(i, 1)));
                boolean checkFlag = new Boolean(CommonUtil.convertObjToStr(table.getValueAt(i, 0))).booleanValue();
                String descVal = CommonUtil.convertObjToStr(table.getValueAt(i, 1));
                System.out.println("checkFlag here" + checkFlag);
                if (CommonUtil.convertObjToStr(table.getValueAt(i, 0)).equals("true") && CommonUtil.convertObjToStr(serviceTaxApplMap.get(descVal)).equals("Y") && CommonUtil.convertObjToStr(serviceTaxIdMap.get(descVal)).length() > 0) {
                    System.out.println("entered" + descVal);
                    chrgamt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(table.getValueAt(i, 3)));
                    if (chrgamt > 0) {
                        serviceTaSettingsMap.put("SETTINGS_ID", serviceTaxIdMap.get(descVal));
                        serviceTaSettingsMap.put(ServiceTaxCalculation.TOT_AMOUNT, CommonUtil.convertObjToStr(chrgamt));
                        taxSettingsList.add(serviceTaSettingsMap);
                    }
                }
            }
        }  
        System.out.println("taxListForGoldLoan...here "+ taxListForGoldLoan);
        if (taxListForGoldLoan != null && taxListForGoldLoan.size() > 0) {
            taxSettingsList.addAll(taxSettingsList.size(), taxListForGoldLoan);
        }
        System.out.println("serviceTaSettingsMap inside calculateTot:: " + taxSettingsList);
            if(taxSettingsList != null && taxSettingsList.size() > 0){
                try {
                    objServiceTax = new ServiceTaxCalculation();
                    HashMap ser_Tax_Val = new HashMap();
                    ser_Tax_Val.put(ServiceTaxCalculation.CURR_DT, ClientUtil.getCurrentDate());
                    ser_Tax_Val.put("SERVICE_TAX_DATA", taxSettingsList);
                    serviceTax_Map = objServiceTax.calculateServiceTax(ser_Tax_Val);
                    if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.TOT_TAX_AMT)) {
                        String amt = CommonUtil.convertObjToStr(serviceTax_Map.get(ServiceTaxCalculation.TOT_TAX_AMT));
                        lblServiceTaxOpeningVal.setText(amt);
                        serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, amt);
                    } else {
                        lblServiceTaxOpeningVal.setText("0.00");
                        serviceTax_Map = null;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }else{
              lblServiceTaxOpeningVal.setText("0.00");  
              serviceTax_Map = null;
            }         
        // End

    }
    
    
    private void tableMouseClicked(java.awt.event.MouseEvent evt) { 
        System.out.println("Execute here inside  nithya");
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW && TrueTransactMain.SERVICE_TAX_REQ.equalsIgnoreCase("Y")){
            calculateOpeningServiceTaxOnEditCharge();
        }
    }   
    
    
    
    private void setInterestDetailsOnlyDeleteDisabled() {
        //        btnInterestMaintenanceNew.setEnabled(true);
        //        btnInterestMaintenanceSave.setEnabled(true);
        //        btnInterestMaintenanceDelete.setEnabled(false);
    }

    private void setAllInterestBtnsEnableDisable(boolean val) {
        //        btnInterestMaintenanceNew.setEnabled(val);
        //        btnInterestMaintenanceSave.setEnabled(val);
        //        btnInterestMaintenanceDelete.setEnabled(val);
    }

    private void setAllClassificationDetailsEnableDisable(boolean val) {
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

    //Added by Nithya on 22-Mar-2025 for KD-3940 : Gold Loan Renewal Rate Picking Is Not Correct 
    private void getInterestRateOnRenewalLimitChange() {
        System.out.println("Rate Picking on Limit change - Renewal");
        if (CommonUtil.convertObjToDouble(txtRenewalLimit_SD.getText()) > 0) {
            if (cboCategory.getSelectedIndex() <= 0) {
                ClientUtil.showAlertWindow("Category should not be empty !!!!");
                txtRenewalLimit_SD.setText("");
                return;
            }
            HashMap whereMap = new HashMap();
            whereMap.put("CATEGORY_ID", observableBorrow.getCbmCategory().getKeyForSelected());
            whereMap.put("AMOUNT", getBigDecimal(CommonUtil.convertObjToDouble(txtRenewalLimit_SD.getText())));
            String loanProdId = ((ComboBoxModel) cboRnwGoldLoanProd.getModel()).getKeyForSelected().toString();
            whereMap.put("PROD_ID", loanProdId);
            whereMap.put("FROM_DATE", setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtRenewalAccountOpenDate.getDateValue())));
            whereMap.put("TO_DATE", setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtRenewalDemandPromNoteExpDate.getDateValue())));
            
            ArrayList interestType = (ArrayList) ClientUtil.executeQuery("getSelectProductTermLoanInterestTO", whereMap);
            if (interestType != null && interestType.size() > 0) {
                TermLoanInterestTO termLoanInterestTO = (TermLoanInterestTO) interestType.get(0);
                txtRenewalInter.setText(CommonUtil.convertObjToStr(termLoanInterestTO.getInterest()));
                txtRenewalPenalInter.setText(CommonUtil.convertObjToStr(termLoanInterestTO.getPenalInterest()));
                observable.setTxtRenewalInter(txtRenewalInter.getText());
                observable.setTxtRenewalPenalInter(txtRenewalPenalInter.getText());
                txtRenewalInter.setEnabled(false);
                txtRenewalPenalInter.setEnabled(false);
            } else {
                ClientUtil.showAlertWindow("Rate of interest is not set for this product");
                return;
            }
        }
    }

    
    
    
    private void setAllBorrowerBtnsEnableDisable(boolean val) {
        btnNew_Borrower.setEnabled(val);
        btnDeleteBorrower.setEnabled(val);
        btnToMain_Borrower.setEnabled(val);
    }

    private void setBorrowerToMainOnlyDisable() {
        btnNew_Borrower.setEnabled(true);
        btnDeleteBorrower.setEnabled(true);
        btnToMain_Borrower.setEnabled(false);
    }

    private void setBorrowerNewOnlyEnable() {
        btnNew_Borrower.setEnabled(true);
        btnDeleteBorrower.setEnabled(false);
        btnToMain_Borrower.setEnabled(false);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        javax.swing.JFrame frm = new javax.swing.JFrame();
        GoldLoanUI termLoanUI = new GoldLoanUI();
        frm.getContentPane().add(termLoanUI);
        termLoanUI.show();
        frm.setSize(600, 500);
        frm.show();
//        TableCellRenderingExample3 tcre = new TableCellRenderingExample3(args);
//            tcre.setSize(600,400);
//            tcre.setVisible(true);
    }

    /**
     * Getter for property facilitySaved.
     *
     * @return Value of property facilitySaved.
     */
    public boolean isFacilitySaved() {
        return facilitySaved;
    }

    /**
     * Setter for property facilitySaved.
     *
     * @param facilitySaved New value of property facilitySaved.
     */
    public void setFacilitySaved(boolean facilitySaved) {
        this.facilitySaved = facilitySaved;
    }

    /**
     * Getter for property viewType.
     *
     * @return Value of property viewType.
     */
    public java.lang.String getViewType() {
        return viewType;
    }

    /**
     * Setter for property viewType.
     *
     * @param viewType New value of property viewType.
     */
    public void setViewType(java.lang.String viewType) {
        this.viewType = viewType;
    }

    /**
     * Getter for property customerScreen.
     *
     * @return Value of property customerScreen.
     */
    public java.lang.String getCustomerScreen() {
        return customerScreen;
    }

    /**
     * Setter for property customerScreen.
     *
     * @param customerScreen New value of property customerScreen.
     */
    public void setCustomerScreen(java.lang.String customerScreen) {
        this.customerScreen = customerScreen;
    }

    /**
     * Getter for property totalSecValue.
     *
     * @return Value of property totalSecValue.
     */
    public java.lang.String getTotalSecValue() {
        return totalSecValue;
    }

    /**
     * Setter for property totalSecValue.
     *
     * @param totalSecValue New value of property totalSecValue.
     */
    public void setTotalSecValue(java.lang.String totalSecValue) {
        this.totalSecValue = totalSecValue;
    }

    /**
     * Getter for property totalMarginValue.
     *
     * @return Value of property totalMarginValue.
     */
    public java.lang.String getTotalMarginValue() {
        return totalMarginValue;
    }

    /**
     * Setter for property totalMarginValue.
     *
     * @param totalMarginValue New value of property totalMarginValue.
     */
    public void setTotalMarginValue(java.lang.String totalMarginValue) {
        this.totalMarginValue = totalMarginValue;
    }

    /**
     * Getter for property totalEligibleValue.
     *
     * @return Value of property totalEligibleValue.
     */
    public java.lang.String getTotalEligibleValue() {
        return totalEligibleValue;
    }

    /**
     * Setter for property totalEligibleValue.
     *
     * @param totalEligibleValue New value of property totalEligibleValue.
     */
    public void setTotalEligibleValue(java.lang.String totalEligibleValue) {
        this.totalEligibleValue = totalEligibleValue;
    }
    
    public void run() {
       
        wc = Webcam.getDefault();
        wc.open();
        while (cameraFlag) {
            Image img = wc.getImage();
            if(webCamOpenedForStockPhoto){
            img = img.getScaledInstance(
                    lblPhoto.getWidth(), lblPhoto.getHeight(),
                    Image.SCALE_SMOOTH);
            lblPhoto.setIcon(new ImageIcon(img));
            }else if(webCamOpenedForStockRenewalPhoto){
                  img = img.getScaledInstance(
                    lblRenewPhoto.getWidth(), lblRenewPhoto.getHeight(),
                    Image.SCALE_SMOOTH);
                    lblRenewPhoto.setIcon(new ImageIcon(img)); 
            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                System.out.println("exception here 5 ::" + e);
            }
        }
       wc.close(); 
    }
    
    public static BufferedImage toBufferedImage(Image img) {
        int width = img.getWidth(null);
        int height = img.getHeight(null);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return bi;
    }

    
 private void SaveImage(CLabel label) {   
        String labelName = label.getName();       
        byte[] byteArray;      
        try {
            System.out.println("label Name ::  " + labelName);               
                ImageIcon ImageIcon = (ImageIcon) label.getIcon();
                BufferedImage buf = toBufferedImage(ImageIcon.getImage());
                try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                ImageIO.write(buf, "jpg", baos);
                baos.flush();
                ByteBuffer byteBuffer = ByteBuffer.wrap(baos.toByteArray());                
                byteArray = byteBuffer.array();
                    System.out.println("byteArray checked :: " + byteArray);
                if (labelName.equals(lblPhoto.getName())) {
                    observable.setPhotoByteArray(byteArray);
                } else if (labelName.equals(lblRenewPhoto.getName())) {
                    observable.setRenewalPhotoByteArray(byteArray);
                }                
               }                    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }   
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAddPhoto;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnClosedAccounts;
    private com.see.truetransact.uicomponent.CButton btnCustID;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDeleteBorrower;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnGoldLiability;
    private com.see.truetransact.uicomponent.CButton btnLoad;
    private com.see.truetransact.uicomponent.CButton btnMembershipLia;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnNew_Borrower;
    private com.see.truetransact.uicomponent.CButton btnPhotoRemove;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnRenew;
    private com.see.truetransact.uicomponent.CButton btnRenewLoad;
    private com.see.truetransact.uicomponent.CButton btnRenewPhotoRemove;
    private com.see.truetransact.uicomponent.CButton btnRenewalStockPhotoCapture;
    private com.see.truetransact.uicomponent.CButton btnRepayShedule;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSave_DocumentDetails;
    private com.see.truetransact.uicomponent.CButton btnScanRenewalStockPhoto;
    private com.see.truetransact.uicomponent.CButton btnScanStockPhoto;
    private com.see.truetransact.uicomponent.CButton btnSecurityAdd;
    private com.see.truetransact.uicomponent.CButton btnStockPhotoCapture;
    private com.see.truetransact.uicomponent.CButton btnToMain_Borrower;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CLabel cLabel2;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CPanel cPanel2;
    private com.see.truetransact.uicomponent.CComboBox cboAccStatus;
    private com.see.truetransact.uicomponent.CComboBox cboAppraiserId;
    private com.see.truetransact.uicomponent.CComboBox cboCategory;
    private com.see.truetransact.uicomponent.CComboBox cboConstitution;
    private com.see.truetransact.uicomponent.CComboBox cboGoldLoanProd;
    private com.see.truetransact.uicomponent.CComboBox cboItem;
    private com.see.truetransact.uicomponent.CComboBox cboPurityOfGold;
    private com.see.truetransact.uicomponent.CComboBox cboPurposeOfLoan;
    private com.see.truetransact.uicomponent.CComboBox cboRenewalAccStatus;
    private com.see.truetransact.uicomponent.CComboBox cboRenewalAppraiserId;
    private com.see.truetransact.uicomponent.CComboBox cboRenewalPurityOfGold;
    private com.see.truetransact.uicomponent.CComboBox cboRenewalPurposeOfLoan;
    private com.see.truetransact.uicomponent.CComboBox cboRenewalRepayFreq;
    private com.see.truetransact.uicomponent.CComboBox cboRenewalSanctioningAuthority;
    private com.see.truetransact.uicomponent.CComboBox cboRepayFreq;
    private com.see.truetransact.uicomponent.CComboBox cboRepaymentType;
    private com.see.truetransact.uicomponent.CComboBox cboRnwGoldLoanProd;
    private com.see.truetransact.uicomponent.CComboBox cboSanctioningAuthority;
    private com.see.truetransact.uicomponent.CCheckBox chkMobileBankingTLAD;
    private com.see.truetransact.uicomponent.CLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private com.see.truetransact.uicomponent.CPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private com.see.truetransact.uicomponent.CLabel lblAccCloseDt;
    private com.see.truetransact.uicomponent.CLabel lblAccOpenDt;
    private com.see.truetransact.uicomponent.CLabel lblAccStatus;
    private com.see.truetransact.uicomponent.CLabel lblAccountNo;
    private com.see.truetransact.uicomponent.CLabel lblAcctNo_Sanction;
    private com.see.truetransact.uicomponent.CLabel lblAcctNo_Sanction_Disp;
    private com.see.truetransact.uicomponent.CLabel lblAppraiserId;
    private com.see.truetransact.uicomponent.CLabel lblAppraiserName;
    private com.see.truetransact.uicomponent.CLabel lblAppraiserNameValue;
    private com.see.truetransact.uicomponent.CLabel lblCategory;
    private com.see.truetransact.uicomponent.CLabel lblCharges;
    private com.see.truetransact.uicomponent.CLabel lblConstitution;
    private com.see.truetransact.uicomponent.CLabel lblCustID;
    private com.see.truetransact.uicomponent.CLabel lblCustMobNo;
    private com.see.truetransact.uicomponent.CLabel lblCustNameValue;
    private com.see.truetransact.uicomponent.CLabel lblDealingWithBankSince;
    private com.see.truetransact.uicomponent.CLabel lblDemandPromNoteDate;
    private com.see.truetransact.uicomponent.CLabel lblDemandPromNoteExpDate;
    private com.see.truetransact.uicomponent.CLabel lblDocDesc_Disp_DocumentDetails;
    private com.see.truetransact.uicomponent.CLabel lblDocDesc_DocumentDetails;
    private com.see.truetransact.uicomponent.CLabel lblDocNo_Disp_DocumentDetails;
    private com.see.truetransact.uicomponent.CLabel lblDocNo_DocumentDetails;
    private com.see.truetransact.uicomponent.CLabel lblDocType_Disp_DocumentDetails;
    private com.see.truetransact.uicomponent.CLabel lblDocType_DocumentDetails;
    private com.see.truetransact.uicomponent.CLabel lblDocumentAccHead;
    private com.see.truetransact.uicomponent.CLabel lblDocumentAccHeadValue;
    private com.see.truetransact.uicomponent.CLabel lblDocumentAccNo;
    private com.see.truetransact.uicomponent.CLabel lblDocumentAccNoValue;
    private com.see.truetransact.uicomponent.CLabel lblDocumentCustName;
    private com.see.truetransact.uicomponent.CLabel lblDocumentCustNameValue;
    private com.see.truetransact.uicomponent.CLabel lblDocumentProdId;
    private com.see.truetransact.uicomponent.CLabel lblDocumentProdIdValue;
    private com.see.truetransact.uicomponent.CLabel lblEmiDate;
    private com.see.truetransact.uicomponent.CLabel lblExecuteDate_DOC;
    private com.see.truetransact.uicomponent.CLabel lblExecuted_DOC;
    private com.see.truetransact.uicomponent.CLabel lblExistingCustomer;
    private com.see.truetransact.uicomponent.CLabel lblExpiryDate_DOC;
    private com.see.truetransact.uicomponent.CLabel lblGoldLoanProd;
    private com.see.truetransact.uicomponent.CLabel lblGrossWeight;
    private com.see.truetransact.uicomponent.CLabel lblInter;
    private com.see.truetransact.uicomponent.CLabel lblItem;
    private com.see.truetransact.uicomponent.CLabel lblLimit_SD;
    private com.see.truetransact.uicomponent.CLabel lblMandatory_DOC;
    private com.see.truetransact.uicomponent.CLabel lblMargin;
    private com.see.truetransact.uicomponent.CLabel lblMarginAmt;
    private com.see.truetransact.uicomponent.CLabel lblMarketRate;
    private com.see.truetransact.uicomponent.CLabel lblMaxLoanAmt;
    private com.see.truetransact.uicomponent.CLabel lblMobileNo;
    private com.see.truetransact.uicomponent.CLabel lblMobileSubscribedFrom;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNetWeight;
    private com.see.truetransact.uicomponent.CLabel lblNoInstallments;
    private com.see.truetransact.uicomponent.CLabel lblNoOfPacket;
    private com.see.truetransact.uicomponent.CLabel lblParticulars;
    private com.see.truetransact.uicomponent.CLabel lblPenalInter;
    private com.see.truetransact.uicomponent.CLabel lblPhoto;
    private com.see.truetransact.uicomponent.CLabel lblPurposeOfLoan;
    private com.see.truetransact.uicomponent.CLabel lblQty;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblRemarks_DocumentDetails;
    private com.see.truetransact.uicomponent.CLabel lblRenewCustMobNo;
    private com.see.truetransact.uicomponent.CLabel lblRenewPhoto;
    private com.see.truetransact.uicomponent.CLabel lblRenewalAccOpenDt;
    private com.see.truetransact.uicomponent.CLabel lblRenewalAccStatus;
    private com.see.truetransact.uicomponent.CLabel lblRenewalAcctNo_Sanction;
    private com.see.truetransact.uicomponent.CLabel lblRenewalAcctNo_Sanction_Disp;
    private com.see.truetransact.uicomponent.CLabel lblRenewalAppraiserId;
    private com.see.truetransact.uicomponent.CLabel lblRenewalAppraiserName;
    private com.see.truetransact.uicomponent.CLabel lblRenewalAppraiserNameValue;
    private com.see.truetransact.uicomponent.CLabel lblRenewalDemandPromNoteDate;
    private com.see.truetransact.uicomponent.CLabel lblRenewalDemandPromNoteExpDate;
    private com.see.truetransact.uicomponent.CLabel lblRenewalGrossWeight;
    private com.see.truetransact.uicomponent.CLabel lblRenewalInter;
    private com.see.truetransact.uicomponent.CLabel lblRenewalLimit_SD;
    private com.see.truetransact.uicomponent.CLabel lblRenewalMargin;
    private com.see.truetransact.uicomponent.CLabel lblRenewalMarginAmt;
    private com.see.truetransact.uicomponent.CLabel lblRenewalMarketRate;
    private com.see.truetransact.uicomponent.CLabel lblRenewalMaxLoanAmt;
    private com.see.truetransact.uicomponent.CLabel lblRenewalNetWeight;
    private com.see.truetransact.uicomponent.CLabel lblRenewalNoInstallments;
    private com.see.truetransact.uicomponent.CLabel lblRenewalParticulars;
    private com.see.truetransact.uicomponent.CLabel lblRenewalPenalInter;
    private com.see.truetransact.uicomponent.CLabel lblRenewalPurposeOfLoan;
    private com.see.truetransact.uicomponent.CLabel lblRenewalRemarks;
    private com.see.truetransact.uicomponent.CLabel lblRenewalRepayFreq;
    private com.see.truetransact.uicomponent.CLabel lblRenewalSanctionDate;
    private com.see.truetransact.uicomponent.CLabel lblRenewalSanctioningAuthority;
    private com.see.truetransact.uicomponent.CLabel lblRenewalSecurityValue;
    private com.see.truetransact.uicomponent.CLabel lblRenewalpurity;
    private com.see.truetransact.uicomponent.CLabel lblRepayFreq;
    private com.see.truetransact.uicomponent.CLabel lblRnwLoanType;
    private com.see.truetransact.uicomponent.CLabel lblSanRepaymentType;
    private com.see.truetransact.uicomponent.CLabel lblSanctionDate;
    private com.see.truetransact.uicomponent.CLabel lblSanctioningAuthority;
    private com.see.truetransact.uicomponent.CLabel lblSecurityRemarks;
    private com.see.truetransact.uicomponent.CLabel lblSecurityValue;
    private com.see.truetransact.uicomponent.CLabel lblServiceTaxOpeningVal;
    private com.see.truetransact.uicomponent.CLabel lblServiceTaxVal;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace51;
    private com.see.truetransact.uicomponent.CLabel lblSpace52;
    private com.see.truetransact.uicomponent.CLabel lblSpace53;
    private com.see.truetransact.uicomponent.CLabel lblSpace54;
    private com.see.truetransact.uicomponent.CLabel lblSpace55;
    private com.see.truetransact.uicomponent.CLabel lblSpace56;
    private com.see.truetransact.uicomponent.CLabel lblSpace57;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblSubmitDate_DocumentDetails;
    private com.see.truetransact.uicomponent.CLabel lblSubmitted_DocumentDetails;
    private com.see.truetransact.uicomponent.CLabel lblpurity;
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
    private com.see.truetransact.uicomponent.CPanel panBorrowCompanyDetails;
    private com.see.truetransact.uicomponent.CPanel panBorrowProfile;
    private com.see.truetransact.uicomponent.CPanel panBorrowProfile1;
    private com.see.truetransact.uicomponent.CPanel panBorrowProfile_CustID;
    private com.see.truetransact.uicomponent.CPanel panBorrowProfile_CustID1;
    private com.see.truetransact.uicomponent.CPanel panBorrowProfile_CustName;
    private com.see.truetransact.uicomponent.CPanel panBorrowRenewalCompanyDetails;
    private com.see.truetransact.uicomponent.CPanel panBorrowerTabCTable;
    private com.see.truetransact.uicomponent.CPanel panBorrowerTabTools;
    private com.see.truetransact.uicomponent.CPanel panChargeDetails;
    private com.see.truetransact.uicomponent.CPanel panCustId;
    private com.see.truetransact.uicomponent.CPanel panDemandPromssoryDate;
    private com.see.truetransact.uicomponent.CPanel panDocumentDetails;
    private com.see.truetransact.uicomponent.CPanel panExecuted_DOC;
    private com.see.truetransact.uicomponent.CPanel panExistingCustomer;
    private com.see.truetransact.uicomponent.CPanel panGender;
    private com.see.truetransact.uicomponent.CPanel panGender1;
    private com.see.truetransact.uicomponent.CPanel panInterestDeails;
    private com.see.truetransact.uicomponent.CPanel panInterestDeails1;
    private com.see.truetransact.uicomponent.CPanel panItemSecurity;
    private com.see.truetransact.uicomponent.CPanel panMandatory_DOC;
    private com.see.truetransact.uicomponent.CPanel panMobileBanking;
    private com.see.truetransact.uicomponent.CPanel panRenewalChargeDetails;
    private com.see.truetransact.uicomponent.CPanel panRenewalDemandPromssoryDate;
    private com.see.truetransact.uicomponent.CPanel panRenewalInterestDeails;
    private com.see.truetransact.uicomponent.CPanel panRenewalInterestDeails2;
    private com.see.truetransact.uicomponent.CPanel panRenewalSanctionDetails_Sanction;
    private com.see.truetransact.uicomponent.CPanel panRenewalSecurityDetails_security;
    private com.see.truetransact.uicomponent.CPanel panRenewalTransactionDetails;
    private com.see.truetransact.uicomponent.CPanel panSanctionDetails_Sanction;
    private com.see.truetransact.uicomponent.CPanel panSanctionDetails_Table;
    private com.see.truetransact.uicomponent.CPanel panSanctionDetails_Table1;
    private com.see.truetransact.uicomponent.CPanel panSecurity1;
    private com.see.truetransact.uicomponent.CPanel panSecurityDetails_security;
    private com.see.truetransact.uicomponent.CPanel panSecurityParticulars;
    private com.see.truetransact.uicomponent.CPanel panSecurityParticulars1;
    private com.see.truetransact.uicomponent.CPanel panSecurityType;
    private com.see.truetransact.uicomponent.CPanel panSecurityType1;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panStockDetails;
    private com.see.truetransact.uicomponent.CPanel panSubmitted_DocumentDetails;
    private com.see.truetransact.uicomponent.CPanel panTabDetails_DocumentDetails;
    private com.see.truetransact.uicomponent.CPanel panTableFields_SD;
    private com.see.truetransact.uicomponent.CPanel panTableFields_SD1;
    private com.see.truetransact.uicomponent.CPanel panTable_DocumentDetails;
    private com.see.truetransact.uicomponent.CPanel panTermLoan;
    private javax.swing.JPanel panTranDetView;
    private com.see.truetransact.uicomponent.CPanel panWebCamRenewalStockPhoto;
    private com.see.truetransact.uicomponent.CPanel panWebCamStockPhoto;
    private com.see.truetransact.uicomponent.CButtonGroup rdoAccLimit;
    private com.see.truetransact.uicomponent.CButtonGroup rdoAccType;
    private com.see.truetransact.uicomponent.CButtonGroup rdoDoAddSIs;
    private com.see.truetransact.uicomponent.CButtonGroup rdoExecuted_DOC;
    private com.see.truetransact.uicomponent.CRadioButton rdoExistingCustomer_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoExistingCustomer_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoGuarnConstution;
    private com.see.truetransact.uicomponent.CButtonGroup rdoInterest;
    private com.see.truetransact.uicomponent.CButtonGroup rdoIsSubmitted_DocumentDetails;
    private com.see.truetransact.uicomponent.CButtonGroup rdoMandatory_DOC;
    private com.see.truetransact.uicomponent.CButtonGroup rdoMultiDisburseAllow;
    private com.see.truetransact.uicomponent.CButtonGroup rdoNatureInterest;
    private com.see.truetransact.uicomponent.CRadioButton rdoNo_DocumentDetails;
    private com.see.truetransact.uicomponent.CRadioButton rdoNo_Executed_DOC;
    private com.see.truetransact.uicomponent.CRadioButton rdoNo_Mandatory_DOC;
    private com.see.truetransact.uicomponent.CRadioButton rdoNonPriority;
    private com.see.truetransact.uicomponent.CButtonGroup rdoPostDatedCheque;
    private com.see.truetransact.uicomponent.CRadioButton rdoPriority;
    private com.see.truetransact.uicomponent.CRadioButton rdoRenewalNonPriority;
    private com.see.truetransact.uicomponent.CRadioButton rdoRenewalPriority;
    private com.see.truetransact.uicomponent.CButtonGroup rdoRenewalPriorityGroup;
    private com.see.truetransact.uicomponent.CButtonGroup rdoRiskWeight;
    private com.see.truetransact.uicomponent.CButtonGroup rdoSecurityDetails;
    private com.see.truetransact.uicomponent.CButtonGroup rdoSecurityType;
    private com.see.truetransact.uicomponent.CButtonGroup rdoStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdoStatus_Repayment;
    private com.see.truetransact.uicomponent.CButtonGroup rdoSubsidy;
    private com.see.truetransact.uicomponent.CRadioButton rdoYes_DocumentDetails;
    private com.see.truetransact.uicomponent.CRadioButton rdoYes_Executed_DOC;
    private com.see.truetransact.uicomponent.CRadioButton rdoYes_Mandatory_DOC;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptException;
    private javax.swing.JSeparator sptPrint;
    private javax.swing.JSeparator sptProcess;
    private com.see.truetransact.uicomponent.CScrollPane srpBorrowerTabCTable;
    private com.see.truetransact.uicomponent.CScrollPane srpPhotoLoad;
    private com.see.truetransact.uicomponent.CScrollPane srpRenewPhotoLoad;
    private com.see.truetransact.uicomponent.CScrollPane srpTable_DocumentDetails;
    private com.see.truetransact.uicomponent.CScrollPane srpTxtAreaParticulars;
    private com.see.truetransact.uicomponent.CScrollPane srpTxtAreaParticulars1;
    private com.see.truetransact.uicomponent.CTabbedPane tabLimitAmount;
    private com.see.truetransact.uicomponent.CTable tabTransView;
    private com.see.truetransact.uicomponent.CTable tblBorrowerTabCTable;
    private com.see.truetransact.uicomponent.CTable tblTable_DocumentDetails;
    private javax.swing.JToolBar tbrTermLoan;
    private com.see.truetransact.uicomponent.CDateField tdtAccountCloseDate;
    private com.see.truetransact.uicomponent.CDateField tdtAccountOpenDate;
    private com.see.truetransact.uicomponent.CDateField tdtDealingWithBankSince;
    private com.see.truetransact.uicomponent.CDateField tdtDemandPromNoteDate;
    private com.see.truetransact.uicomponent.CDateField tdtDemandPromNoteExpDate;
    private com.see.truetransact.uicomponent.CDateField tdtEmiDate;
    private com.see.truetransact.uicomponent.CDateField tdtExecuteDate_DOC;
    private com.see.truetransact.uicomponent.CDateField tdtExpiryDate_DOC;
    private com.see.truetransact.uicomponent.CDateField tdtMobileSubscribedFrom;
    private com.see.truetransact.uicomponent.CDateField tdtRenewalAccountOpenDate;
    private com.see.truetransact.uicomponent.CDateField tdtRenewalDemandPromNoteDate;
    private com.see.truetransact.uicomponent.CDateField tdtRenewalDemandPromNoteExpDate;
    private com.see.truetransact.uicomponent.CDateField tdtRenewalSanctionDate;
    private com.see.truetransact.uicomponent.CDateField tdtSanctionDate;
    private com.see.truetransact.uicomponent.CDateField tdtSubmitDate_DocumentDetails;
    private com.see.truetransact.uicomponent.CTextField txtAccountNo;
    private com.see.truetransact.uicomponent.CTextArea txtAreaParticular;
    private com.see.truetransact.uicomponent.CTextField txtCharges;
    public static com.see.truetransact.uicomponent.CTextField txtCustID;
    private com.see.truetransact.uicomponent.CTextField txtCustMobNo;
    private com.see.truetransact.uicomponent.CTextField txtEligibleLoan;
    private com.see.truetransact.uicomponent.CTextField txtGrossWeight;
    private com.see.truetransact.uicomponent.CTextField txtInter;
    private com.see.truetransact.uicomponent.CTextField txtLimit_SD;
    private com.see.truetransact.uicomponent.CTextField txtMargin;
    private com.see.truetransact.uicomponent.CTextField txtMarginAmt;
    private com.see.truetransact.uicomponent.CTextField txtMarketRate;
    private com.see.truetransact.uicomponent.CTextField txtMobileNo;
    private com.see.truetransact.uicomponent.CTextField txtNetWeight;
    private com.see.truetransact.uicomponent.CTextField txtNextAccNo;
    private com.see.truetransact.uicomponent.CTextField txtNoInstallments;
    private com.see.truetransact.uicomponent.CTextField txtNoOfPacket;
    private com.see.truetransact.uicomponent.CTextField txtPenalInter;
    private com.see.truetransact.uicomponent.CTextField txtQty;
    private com.see.truetransact.uicomponent.CTextField txtRemarks_DocumentDetails;
    private com.see.truetransact.uicomponent.CTextField txtRenewCustMobNo;
    private com.see.truetransact.uicomponent.CTextArea txtRenewalAreaParticular;
    private com.see.truetransact.uicomponent.CTextField txtRenewalEligibleLoan;
    private com.see.truetransact.uicomponent.CTextField txtRenewalGrossWeight;
    private com.see.truetransact.uicomponent.CTextField txtRenewalInter;
    private com.see.truetransact.uicomponent.CTextField txtRenewalLimit_SD;
    private com.see.truetransact.uicomponent.CTextField txtRenewalMargin;
    private com.see.truetransact.uicomponent.CTextField txtRenewalMarginAmt;
    private com.see.truetransact.uicomponent.CTextField txtRenewalMarketRate;
    private com.see.truetransact.uicomponent.CTextField txtRenewalNetWeight;
    private com.see.truetransact.uicomponent.CTextField txtRenewalNoInstallments;
    private com.see.truetransact.uicomponent.CTextField txtRenewalPenalInter;
    private com.see.truetransact.uicomponent.CTextField txtRenewalSanctionRemarks;
    private com.see.truetransact.uicomponent.CTextField txtRenewalSecurityValue;
    private com.see.truetransact.uicomponent.CTextField txtSanctionRemarks;
    private com.see.truetransact.uicomponent.CTextField txtSecurityRemarks;
    private com.see.truetransact.uicomponent.CTextField txtSecurityValue;
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
    private com.see.truetransact.uicomponent.CLabel lblPLIBranch;
    private com.see.truetransact.uicomponent.CComboBox cboPLIBranch;
    private com.see.truetransact.uicomponent.CLabel lblGuaratNo;
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
