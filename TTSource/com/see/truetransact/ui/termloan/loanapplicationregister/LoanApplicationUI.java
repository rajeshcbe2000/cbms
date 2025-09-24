/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EmptransferUI.java
 *
 * Created on feb 9, 2009, 10:53 AM
 */
package com.see.truetransact.ui.termloan.loanapplicationregister;

import javax.swing.table.AbstractTableModel;
import javax.swing.JTable;
import java.util.*;
import java.util.HashMap;
import java.text.*;
import java.util.Observable;
import org.apache.log4j.Logger;
//import javax.swing.event.TableModelListener;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import java.util.ArrayList;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;//search popup
import java.util.List;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
//security details
import com.see.truetransact.ui.termloan.TermLoanOB;
import com.see.truetransact.ui.termloan.TermLoanBorrowerOB;
import com.see.truetransact.ui.termloan.TermLoanCompanyOB;
import com.see.truetransact.ui.termloan.TermLoanSecurityOB;
import com.see.truetransact.ui.termloan.TermLoanRepaymentOB;
import com.see.truetransact.ui.termloan.TermLoanGuarantorOB;
import com.see.truetransact.ui.termloan.TermLoanDocumentDetailsOB;
import com.see.truetransact.ui.termloan.TermLoanInterestOB;
import com.see.truetransact.ui.termloan.TermLoanClassificationOB;
import com.see.truetransact.ui.termloan.TermLoanOtherDetailsOB;
import com.see.truetransact.ui.termloan.TermLoanAdditionalSanctionOB;
//security end..
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.customer.customeridchange.CustomerIdChangeOB;
import com.see.truetransact.commonutil.DateUtil;
import java.util.Date;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.MandatoryDBCheck;
import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.uivalidation.*;
import com.see.truetransact.uicomponent.CButtonGroup;
//trans details
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
//end..
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.transferobject.common.charges.LoanSlabChargesTO;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.ui.common.viewall.NewAuthorizeListUI;
import com.see.truetransact.ui.operativeaccount.AccountClosingOB;
import com.see.truetransact.ui.operativeaccount.AccountClosingUI;
//emi calc
import com.see.truetransact.ui.termloan.emicalculator.TermLoanInstallmentUI;
import com.see.truetransact.ui.termloan.emicalculator.TermLoanInstallmentOB;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Toolkit;
import com.see.truetransact.ui.termloan.TermLoanUI;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListDebitUI;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListCreditUI;
import java.util.logging.Level;
//import java.util.Calendar;

/**
 * This form is used to manipulate CustomerIdChangeUI related functionality
 *
 * @author swaroop
 */
public class LoanApplicationUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField {

    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.loanapplicationregister.LoanApplicationRB", ProxyParameters.LANGUAGE);
    private javax.swing.JScrollPane srpChargeDetails;
    private String viewType = new String();
    private HashMap mandatoryMap;
    String isTransaction;
    private LoanApplicationOB observable;
    private final String AUTHORIZE = "Authorize";
    private final static Logger log = Logger.getLogger(LoanApplicationUI.class);
    LoanApplicationRB LoanApplicationRB = new LoanApplicationRB();
    //private String prodType="";
    java.util.ResourceBundle objMandatoryRB;
    //   int aki=0;
    // String cust_type=null;
    private boolean selectMode = false;//charge details, calculating total of selected amount
    private HashMap returnMap = null;//charge details, calculating total of selected amount
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    TransactionUI transactionUI = new TransactionUI(); //trans details
    double amtBorrow = 0.0; //trans details
    DefaultTableModel model;
    //security details
    TermLoanOB observableTerm;
    TermLoanBorrowerOB observableBorrow;
    TermLoanCompanyOB observableComp;
    TermLoanSecurityOB observableSecurity;
    TermLoanRepaymentOB observableRepay;
    TermLoanGuarantorOB observableGuarantor;
    TermLoanDocumentDetailsOB observableDocument;
    TermLoanInterestOB observableInt;
    TermLoanClassificationOB observableClassi;
    TermLoanOtherDetailsOB observableOtherDetails;
    TermLoanAdditionalSanctionOB observableAdditionalSanctionOB;
    TermLoanInstallmentOB observableInstalment;
    TermLoanUI termloanui;
    ServiceTaxCalculation objServiceTax;
    private TableModelListener tableModelListener;
    private Date curr_dt = null;
    int salarytblSelectedRow = -1;
    private final String REJECT = "REJECT";
    private boolean updateMode = false;
    private boolean updateLosMode = false;
    int updateTab = -1;
    //security end..
    //Loan Charges
    private boolean tableFlag = false;
    private JTable table = null;
    public String prodDesc = "";
    private boolean transNew = true;//charge details
    private List chargelst = null; //CHARGE DETAILS
    private boolean finalChecking = false; //CHARGE DETAILS
    double shareFees = 0.0;
    double totalChargeAmt = 0.0;
    Rounding rd = new Rounding();
    double membshpFee = 0.0;
    double reqShareAmt = 0.0;
    double availShareNo = 0.0;
    private int rejectFlag = 0;
    double totAmount = 0.0, taxAmt = 0.0;
    boolean fromAuthorizeUI = false;
    boolean fromCashierAuthorizeUI = false;
    boolean fromManagerAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
    AuthorizeListDebitUI ManagerauthorizeListUI = null;
    AuthorizeListCreditUI CashierauthorizeListUI = null;
    AccountClosingOB accountClosingob = new AccountClosingOB();
    //  private int flag = 0;
    Object totalList[][] = new Object[1][3];
    private int rowCount = 0;
    HashMap serviceTax_Map = new HashMap();
    private final String NEAREST = "NEAREST_VALUE";
    private final String HIGHER = "HIGHER_VALUE";
    private final String LOWER = "LOWER_VALUE";
    private final String NO_ROUND_OFF = "NO_ROUND_OFF";
    DecimalFormat df = new DecimalFormat("##.00");
    private HashMap tabEditDataMap = new HashMap();
    private List trans_Det_List = null;
    boolean isLoanAppln=false;
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;

    /**
     * Creates new form CustomerIdChangeUI
     */
    public LoanApplicationUI() {
        returnMap = null;
        //        btnDelete.setVisible(false);
        initComponents();
        initRunSecurityComponents();
        chkSettlement.setVisible(false);
        rdoGahanGroup.add(rdoGahanYes); //security details  
        rdoGahanGroup.add(rdoGahanNo); //security details 
        initStartUp();
        //        initRunSecurityComponents();
        btnSearch.setEnabled(false);
        //security details
        curr_dt = ClientUtil.getCurrentDate();
        //security end..
        // panRemarks.setEnabled(false);
        // scrRemarks.setEnabled(false);
        //txaRemarks.setEditable(false);
        //trans details
        panTrans.add(transactionUI);
        transactionUI.setSourceScreen("Loan_Register");
        observable.setTransactionOB(transactionUI.getTransactionOB());
        //end..


        setObservable(); //security details
        btnDelete.setVisible(false);
        tdtApplDt.setDateValue(CommonUtil.convertObjToStr(curr_dt));
        tdtApplInwrdDt.setDateValue(CommonUtil.convertObjToStr(curr_dt));
        tdtFromDt.setDateValue(CommonUtil.convertObjToStr(curr_dt));
        txtNextAccNo.setEnabled(false);
        btnPrintCheck.setEnabled(false);
        if(CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")){
            chkSettlement.setVisible(true);
        }
    }
    //security details

    private void setObservable() {
        observableTerm = TermLoanOB.getInstance();
        observableTerm.addObserver(this);
        //    observable.setLoanType(loanType);
        observableBorrow = TermLoanBorrowerOB.getInstance();
        observableBorrow.addObserver(this);
        observableComp = TermLoanCompanyOB.getInstance();
        observableComp.addObserver(this);
        observableSecurity = TermLoanSecurityOB.getInstance();
        observableSecurity.addObserver(this);
        observableRepay = TermLoanRepaymentOB.getInstance();
        observableRepay.addObserver(this);
        observableInt = TermLoanInterestOB.getInstance();
        observableInt.addObserver(this);
        //        if (loanType.equals("OTHERS")) {
        observableGuarantor = TermLoanGuarantorOB.getInstance();
        observableGuarantor.addObserver(this);
        observableOtherDetails = TermLoanOtherDetailsOB.getInstance();
        observableOtherDetails.addObserver(this);
        //        }
        observableDocument = TermLoanDocumentDetailsOB.getInstance();
        observableDocument.addObserver(this);
        observableClassi = TermLoanClassificationOB.getInstance();
        observableClassi.addObserver(this);

        observableAdditionalSanctionOB = TermLoanAdditionalSanctionOB.getInstance();
        observableAdditionalSanctionOB.addObserver(this);

    }
    //security end..

    public void setButtons(boolean flag) {
        //   btnViewPrin.setEnabled(flag);
        //   btnViewInt.setEnabled(flag);
        //   btnViewPen.setEnabled(flag);
        //   btnViewCharg.setEnabled(flag);
    }

    private void initStartUp() {

        setFieldNames();
        internationalize();
        observable = new LoanApplicationOB();
        observable.addObserver(this);
        initComponentData();
        enableDisable(false);
        setButtonEnableDisable();
        setMaxLength();
        objMandatoryRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.loanapplicationregister.LoanApplicationRB", ProxyParameters.LANGUAGE);
        setMandatoryHashMap();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panLoan, getMandatoryHashMap());
        setHelpMessage();
    }

    private void setMaxLength() {
        //           txtEmpID.setMaxLength(64);
        txtCostOfVehicleField.setValidation(new CurrencyValidation());
        txtLoanAmt.setValidation(new CurrencyValidation());
        txtLoanAmts.setValidation(new CurrencyValidation());
        txtSalaryField.setValidation(new CurrencyValidation());
        txtNoInstall.setAllowNumber(true);
        txtInstallAmount.setAllowNumber(true);
        txtMemberTotalSalary.setValidation(new CurrencyValidation());
        txtVehicleMemSal.setValidation(new CurrencyValidation());
        txtInstallAmount.setValidation(new CurrencyValidation());
        //         txtAccountNumber.setMaxLength(16);
        //         txtAccountNumber.setAllowAll(true);
        //         txtOldCustID.setAllowAll(true);
        //         txtNewCustId.setAllowAll(true);
        //   txtLoanAmt.setMaxLength(16);
        //  txtLoanAmt.setValidation(new CurrencyValidation(14,2));
    }

    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtCustId", new Boolean(true));
        mandatoryMap.put("cboSchemName", new Boolean(true));
        mandatoryMap.put("txtLoanAmt", new Boolean(true));
        mandatoryMap.put("txtMemId", new Boolean(false));
        mandatoryMap.put("txtMemName", new Boolean(false));
        mandatoryMap.put("txtApplNo", new Boolean(false));
        mandatoryMap.put("tdtApplDt", new Boolean(false));
        mandatoryMap.put("tdtApplInwrdDt", new Boolean(false));
        mandatoryMap.put("txtSuretyName", new Boolean(false));
        mandatoryMap.put("txaRemarks", new Boolean(false));
        mandatoryMap.put("cboRegstrStatus", new Boolean(false));
    }

    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    public Date getDateValue(String date1) {
        DateFormat formatter;
        Date date = null;
        try {

            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
            date = new Date(sdf2.format(sdf1.parse(date1)));
        } catch (ParseException e) {
            System.out.println("Error in getDateValue():" + e);
        }
        return date;
    }

    public String getDtPrintValue(String strDate) {
        try {
            //      System.out.println("strDate getDtPrintValue99999999999999999999999999999999999999================="+strDate);
            //create SimpleDateFormat object with source string date format
            SimpleDateFormat sdfSource = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            //parse the string into Date object
            Date date = sdfSource.parse(strDate);
            //create SimpleDateFormat object with desired date format
            SimpleDateFormat sdfDestination = new SimpleDateFormat("dd/MM/yyyy");
            //parse the date into another format
            strDate = sdfDestination.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }

    /**
     * **************** NEW METHODS ****************
     */
    //    private void updateAuthorizeStatus(String authorizeStatus) {
    //        if (viewType != AUTHORIZE){
    //            HashMap mapParam = new HashMap();
    //            HashMap whereMap = new HashMap();
    //            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
    //            whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
    //            whereMap.put("AUTHORIZE_MODE","AUTHORIZE_MODE");
    //            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
    //            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
    //            mapParam.put(CommonConstants.MAP_NAME, "getEmpTransferDetailsAuthorize");
    //            viewType = AUTHORIZE;
    //            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
    //            //            isFilled = false;
    //            authorizeUI.show();
    //            btnSave.setEnabled(false);
    //            observable.setStatus();
    //            lblStatus.setText(observable.getLblStatus());
    //        } else if (viewType == AUTHORIZE){
    //            ArrayList arrList = new ArrayList();
    //            HashMap authorizeMap = new HashMap();
    //            HashMap singleAuthorizeMap = new HashMap();
    //            singleAuthorizeMap.put("STATUS", authorizeStatus);
    //            singleAuthorizeMap.put("EMP_TRANSFER_ID", observable.getTxtEmpTransferID());
    //            singleAuthorizeMap.put("AUTH_BY", TrueTransactMain.USER_ID);
    //            singleAuthorizeMap.put("AUTH_DT",ClientUtil.getCurrentDateWithTime());
    //            singleAuthorizeMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
    //            String presentBranch = ((ComboBoxModel)cboTransBran.getModel()).getKeyForSelected().toString();
    //            if (presentBranch.lastIndexOf("-")!=-1)
    //            presentBranch = presentBranch.substring(0,presentBranch.lastIndexOf("-"));
    //            presentBranch= presentBranch.trim();
    //            singleAuthorizeMap.put("PRESENT_BRANCH_CODE",presentBranch);
    //            singleAuthorizeMap.put("EMP_ID", observable.getTxtEmpID());
    //            arrList.add(singleAuthorizeMap);
    //            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
    //            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
    //            authorize(authorizeMap,observable.getTxtEmpTransferID());
    //            viewType = "";
    //            super.setOpenForEditBy(observable.getStatusBy());
    //        }
    //    }
    //
    //    public void authorize(HashMap map,String id) {
    //        System.out.println("Authorize Map : " + map);
    //
    //        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
    //            observable.set_authorizeMap(map);
    //            observable.doAction();
    //            if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
    //                   super.setOpenForEditBy(observable.getStatusBy());
    //                   super.removeEditLock(id);
    //             }
    //            btnCancelActionPerformed(null);
    //            observable.setStatus();
    //            observable.setResultStatus();
    //            lblStatus.setText(observable.getLblStatus());
    //
    //        }
    //    }
    //    public void authorize(HashMap map) {
    //        System.out.println("Authorize Map : " + map);
    //
    //        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
    //            observable.setResult(ClientConstants.ACTIONTYPE_AUTHORIZE);
    //            observable.set_authorizeMap(map);
    //            observable.doAction();
    //            btnCancelActionPerformed(null);
    //            observable.setStatus();
    //            observable.setResultStatus();
    //            lblStatus.setText(observable.getLblStatus());
    //
    //        }
    //    }
    //security details
    private void initRunSecurityComponents() {
        panEmpTransfer = new com.see.truetransact.uicomponent.CPanel();
        tabMasterMaintenance = new com.see.truetransact.uicomponent.CTabbedPane();
        panSalaryDetails = new com.see.truetransact.uicomponent.CPanel();
        panAllSalaryDetails = new com.see.truetransact.uicomponent.CPanel();
        lblSalaryCertificateNo = new com.see.truetransact.uicomponent.CLabel();
        lblSalaryRemark = new com.see.truetransact.uicomponent.CLabel();
        lblEmployerName = new com.see.truetransact.uicomponent.CLabel();
        lblTotalSalary = new com.see.truetransact.uicomponent.CLabel();
        lblMemberTotalSalary = new com.see.truetransact.uicomponent.CLabel();
        lblDesignation = new com.see.truetransact.uicomponent.CLabel();
        lblMemberNum = new com.see.truetransact.uicomponent.CLabel();
        lblMemberName=new com.see.truetransact.uicomponent.CLabel();
        lblSalMemberRetireDate=new com.see.truetransact.uicomponent.CLabel();
        lblSalMemberRetireDateValue=new com.see.truetransact.uicomponent.CLabel();
        lblAddress = new com.see.truetransact.uicomponent.CLabel();
        lblPinCode = new com.see.truetransact.uicomponent.CLabel();
        lblCity = new com.see.truetransact.uicomponent.CLabel();
        lblRetirementDt = new com.see.truetransact.uicomponent.CLabel();
        lblContactNo = new com.see.truetransact.uicomponent.CLabel();
        lblNetWorth1 = new com.see.truetransact.uicomponent.CLabel();
        lblVehicleNetWorth = new com.see.truetransact.uicomponent.CLabel();
        lblTotalVehicleMemSal = new com.see.truetransact.uicomponent.CLabel();
        
        txtContactNo = new com.see.truetransact.uicomponent.CTextField();
        txtContactNo.setAllowNumber(true);
        txtMemberNum = new com.see.truetransact.uicomponent.CTextField();
        txtMemberNum.setAllowNumber(true);
        lblVehicleMemRetireDate = new com.see.truetransact.uicomponent.CLabel();
        lblVehicleMemberNum = new com.see.truetransact.uicomponent.CLabel();
        lblVehicleMemName=new com.see.truetransact.uicomponent.CLabel();
        txtVehicleMemberNum = new com.see.truetransact.uicomponent.CTextField();
        txtVehicleMemberNum.setAllowNumber(true);
        txtVehicleMemberName=new com.see.truetransact.uicomponent.CTextField();
        txtVehicleMemType=new com.see.truetransact.uicomponent.CTextField();
        srpVehicleType = new com.see.truetransact.uicomponent.CScrollPane();
        panVehicleTypeDetails = new com.see.truetransact.uicomponent.CPanel();
        panVehicleTypeTable = new com.see.truetransact.uicomponent.CPanel();
        txtMemberName=new com.see.truetransact.uicomponent.CTextField();
        txtSalaryRemark = new com.see.truetransact.uicomponent.CTextField();
        txtDesignation = new com.see.truetransact.uicomponent.CTextField();
        txtAddress = new com.see.truetransact.uicomponent.CTextField();
        txtEmployerName = new com.see.truetransact.uicomponent.CTextField();
        txtSalaryCertificateNo = new com.see.truetransact.uicomponent.CTextField();
        txtSalaryCertificateNo.setAllowAll(true);
        txtTotalSalary = new com.see.truetransact.uicomponent.CTextField();
        txtTotalSalary.setAllowNumber(true);
        txtMemberTotalSalary = new com.see.truetransact.uicomponent.CTextField();
        txtMemberTotalSalary.setAllowNumber(true);
        txtNetWorth1 = new com.see.truetransact.uicomponent.CTextField();
        txtNetWorth1.setAllowNumber(true);
        txtVehicleNetWorth = new com.see.truetransact.uicomponent.CTextField();
        txtVehicleNetWorth.setAllowNumber(true);
        txtVehicleMemSal= new com.see.truetransact.uicomponent.CTextField();
        txtVehicleMemSal.setAllowNumber(true);
        txtPinCode = new com.see.truetransact.uicomponent.CTextField();
        txtPinCode.setAllowNumber(true);
        cboCity = new com.see.truetransact.uicomponent.CComboBox();
        tdtRetirementDt = new com.see.truetransact.uicomponent.CDateField();
        panMemberTypeDetails = new com.see.truetransact.uicomponent.CPanel();
        panMemberTypeTable = new com.see.truetransact.uicomponent.CPanel();
        panSalaryTable = new com.see.truetransact.uicomponent.CPanel();
        srpMemberType = new com.see.truetransact.uicomponent.CScrollPane();
        srpSalary = new com.see.truetransact.uicomponent.CScrollPane();
        tblMemberType = new com.see.truetransact.uicomponent.CTable();
        tblVehicleType = new com.see.truetransact.uicomponent.CTable();
        panVehicleDetails = new com.see.truetransact.uicomponent.CPanel();
        panVehicleNumber = new com.see.truetransact.uicomponent.CPanel();
        txtVehicleContactNum = new com.see.truetransact.uicomponent.CTextField();
        txtVehicleContactNum.setAllowNumber(true);
        lblVehicleContactNum = new com.see.truetransact.uicomponent.CLabel();
        btnVehicleMemNo = new com.see.truetransact.uicomponent.CButton();
        btnVehicleSave = new com.see.truetransact.uicomponent.CButton();
        btnVehicleNew = new com.see.truetransact.uicomponent.CButton();
        btnVehicleDelete = new com.see.truetransact.uicomponent.CButton();
        txtVehicleDetals = new com.see.truetransact.uicomponent.CTextArea();
        txtVehicleType = new com.see.truetransact.uicomponent.CTextField();
        txtVehicleNo = new com.see.truetransact.uicomponent.CTextField();
        txtVehicleNo.setAllowAll(true);
        txtVehicleRcBookNo = new com.see.truetransact.uicomponent.CTextField();
        txtVehicleRcBookNo.setAllowAll(true);
        txtVehicleDate = new com.see.truetransact.uicomponent.CDateField(); 
        srpTxtAreaVehicledtails = new com.see.truetransact.uicomponent.CScrollPane();
        lblVehicleMemType = new com.see.truetransact.uicomponent.CLabel();
        lblVehicleMemNo = new com.see.truetransact.uicomponent.CLabel();
        lblVehicleNo = new com.see.truetransact.uicomponent.CLabel();
        lblVehicleType= new com.see.truetransact.uicomponent.CLabel();
        lblVehicleRcBookNo= new com.see.truetransact.uicomponent.CLabel();
        lblVehicleDate = new com.see.truetransact.uicomponent.CLabel();
        lblVehicleDetails = new com.see.truetransact.uicomponent.CLabel();
        panBtnVehicleType = new com.see.truetransact.uicomponent.CPanel();
        tblSalary = new com.see.truetransact.uicomponent.CTable();
        panMemberDetails = new com.see.truetransact.uicomponent.CPanel();
        lblGahanYesNo = new com.see.truetransact.uicomponent.CLabel();
        panGahanYesNo = new com.see.truetransact.uicomponent.CPanel();
        rdoGahanYes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoGahanNo = new com.see.truetransact.uicomponent.CRadioButton();
        rdoGahanGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        lblMemNo = new com.see.truetransact.uicomponent.CLabel();
        lblMemName = new com.see.truetransact.uicomponent.CLabel();
        lblMemRetireDate=new com.see.truetransact.uicomponent.CLabel();
        lblMemberRetireDate=new com.see.truetransact.uicomponent.CLabel();
        lblMemType = new com.see.truetransact.uicomponent.CLabel();
        lblMemNetworth = new com.see.truetransact.uicomponent.CLabel();
        txtMemNetworth = new com.see.truetransact.uicomponent.CTextField();
        txtMemNetworth.setAllowNumber(true);
        lblMemPriority = new com.see.truetransact.uicomponent.CLabel();
        txtMemPriority = new com.see.truetransact.uicomponent.CTextField();
        txtMemPriority.setAllowNumber(true);
        txtContactNum = new com.see.truetransact.uicomponent.CTextField();
        txtContactNum.setAllowNumber(true);
        lblContactNum = new com.see.truetransact.uicomponent.CLabel();
        txtMemType = new com.see.truetransact.uicomponent.CTextField();
        txtMembName = new com.see.truetransact.uicomponent.CTextField();
        panMemberNumber = new com.see.truetransact.uicomponent.CPanel();
        txtMemNo = new com.see.truetransact.uicomponent.CTextField();
        btnMemNo = new com.see.truetransact.uicomponent.CButton();
        panBtnMemberType = new com.see.truetransact.uicomponent.CPanel();
        panBtnSalaryType = new com.see.truetransact.uicomponent.CPanel();
        btnMemberNew = new com.see.truetransact.uicomponent.CButton();
        btnMemberSave = new com.see.truetransact.uicomponent.CButton();
        btnMemberDelete = new com.see.truetransact.uicomponent.CButton();
        btnSalaryNew = new com.see.truetransact.uicomponent.CButton();
        btnSalarySave = new com.see.truetransact.uicomponent.CButton();
        btnSalaryDelete = new com.see.truetransact.uicomponent.CButton();
        panCollateralTypeDetails = new com.see.truetransact.uicomponent.CPanel();
        panCollateralTable = new com.see.truetransact.uicomponent.CPanel();
        panCollateralJointTable = new com.see.truetransact.uicomponent.CPanel();
        srpCollateralTable = new com.see.truetransact.uicomponent.CScrollPane();
        srpCollateralJointTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblCollateral = new com.see.truetransact.uicomponent.CTable();
        tblJointCollateral = new com.see.truetransact.uicomponent.CTable();
        panCollatetalDetails = new com.see.truetransact.uicomponent.CPanel();
        lblOwnerMemberNo = new com.see.truetransact.uicomponent.CLabel();
        lblDocumentNo = new com.see.truetransact.uicomponent.CLabel();
        txtDocumentNo = new com.see.truetransact.uicomponent.CTextField();
        txtDocumentNo.setAllowNumber(true);
        lblDocumentType = new com.see.truetransact.uicomponent.CLabel();
        cboDocumentType = new com.see.truetransact.uicomponent.CComboBox();
        lblDocumentDate = new com.see.truetransact.uicomponent.CLabel();
        lblRegisteredOffice = new com.see.truetransact.uicomponent.CLabel();
        txtRegisteredOffice = new com.see.truetransact.uicomponent.CTextField();
        lblOwnerMemberNname = new com.see.truetransact.uicomponent.CLabel();
        txtOwnerMemberNname = new com.see.truetransact.uicomponent.CTextField();
        tdtPledgeDate = new com.see.truetransact.uicomponent.CDateField();
        lblPledgeDate = new com.see.truetransact.uicomponent.CLabel();
        lblPledgeNo = new com.see.truetransact.uicomponent.CLabel();
        txtPledgeNo = new com.see.truetransact.uicomponent.CTextField();
        txtPledgeNo.setAllowNumber(true);
        lblPledge = new com.see.truetransact.uicomponent.CLabel();
        lblVillage = new com.see.truetransact.uicomponent.CLabel();
        txtVillage = new com.see.truetransact.uicomponent.CTextField();
        lblSurveyNo = new com.see.truetransact.uicomponent.CLabel();
        txtSurveyNo = new com.see.truetransact.uicomponent.CTextField();
        txtSurveyNo.setAllowNumber(true);
        lblRight = new com.see.truetransact.uicomponent.CLabel();
        lblPledgeType = new com.see.truetransact.uicomponent.CLabel();
        txtPledgeType = new com.see.truetransact.uicomponent.CTextField();
        lblPledgeAmount = new com.see.truetransact.uicomponent.CLabel();
        txtPledgeAmount = new com.see.truetransact.uicomponent.CTextField();
        txtPledgeAmount.setAllowNumber(true);
        tdtDocumentDate = new com.see.truetransact.uicomponent.CDateField();

        lblJewelleryDetails = new com.see.truetransact.uicomponent.CLabel();
        lblGrossWeight = new com.see.truetransact.uicomponent.CLabel();
        lblNetWeight = new com.see.truetransact.uicomponent.CLabel();
        lblValueOfGold = new com.see.truetransact.uicomponent.CLabel();
        lblGoldRemarks = new com.see.truetransact.uicomponent.CLabel();

        txtJewelleryDetails = new com.see.truetransact.uicomponent.CTextArea();
        txtGrossWeight = new com.see.truetransact.uicomponent.CTextField();
        txtNetWeight = new com.see.truetransact.uicomponent.CTextField();
        txtValueOfGold = new com.see.truetransact.uicomponent.CTextField();
        txtGoldRemarks = new com.see.truetransact.uicomponent.CTextField();
        panGoldTypeDetails = new com.see.truetransact.uicomponent.CPanel();

        
        //        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        cboNature = new com.see.truetransact.uicomponent.CComboBox();
        lblTotalArea = new com.see.truetransact.uicomponent.CLabel();
        txtTotalArea = new com.see.truetransact.uicomponent.CTextField();
        txtTotalArea.setAllowNumber(true);
        panBtnCollateralType = new com.see.truetransact.uicomponent.CPanel();
        btnCollateralNew = new com.see.truetransact.uicomponent.CButton();
        btnCollateralSave = new com.see.truetransact.uicomponent.CButton();
        btnCollateralDelete = new com.see.truetransact.uicomponent.CButton();
        srpTxtAreaParticulars = new com.see.truetransact.uicomponent.CScrollPane();
        txtAreaParticular = new com.see.truetransact.uicomponent.CTextArea();
        panOwnerMemberNumber = new com.see.truetransact.uicomponent.CPanel();
        panDocumentNumber = new com.see.truetransact.uicomponent.CPanel();
        txtOwnerMemNo = new com.see.truetransact.uicomponent.CTextField();
        btnOwnerMemNo = new com.see.truetransact.uicomponent.CButton();
        btnDocumentNo = new com.see.truetransact.uicomponent.CButton();
        cboPledge = new com.see.truetransact.uicomponent.CComboBox();
        lblNature = new com.see.truetransact.uicomponent.CLabel();
        cboRight = new com.see.truetransact.uicomponent.CComboBox();

        panOtherSecurityDetails = new com.see.truetransact.uicomponent.CPanel();
        lblDepAmount = new com.see.truetransact.uicomponent.CLabel();
        //  lblProductId = new com.see.truetransact.uicomponent.CLabel();
        lblProductId2 = new com.see.truetransact.uicomponent.CLabel();
        lblRateOfInterest = new com.see.truetransact.uicomponent.CLabel();
        lblDepDt = new com.see.truetransact.uicomponent.CLabel();
        txtMaturityValue = new com.see.truetransact.uicomponent.CTextField();
        txtDepAmount = new com.see.truetransact.uicomponent.CTextField();
        txtDepAmount.setAllowNumber(true);
        txtRateOfInterest = new com.see.truetransact.uicomponent.CTextField();
        txtRateOfInterest.setAllowNumber(true);
        lblMaturityDt = new com.see.truetransact.uicomponent.CLabel();
        lblMaturityValue = new com.see.truetransact.uicomponent.CLabel();
        lblDepNo = new com.see.truetransact.uicomponent.CLabel();
        tdtDepDt = new com.see.truetransact.uicomponent.CDateField();
        btnDepositNew = new com.see.truetransact.uicomponent.CButton();
        btnDepositSave = new com.see.truetransact.uicomponent.CButton();
        btnDepositDelete = new com.see.truetransact.uicomponent.CButton();
        panBtnDeposit = new com.see.truetransact.uicomponent.CPanel();
        txtMaturityDt = new com.see.truetransact.uicomponent.CDateField();
        cboDepProdType = new com.see.truetransact.uicomponent.CComboBox();
        panDepNo = new com.see.truetransact.uicomponent.CPanel();
        btnDepNo = new com.see.truetransact.uicomponent.CButton();
        lblProductTypeSecurity = new com.see.truetransact.uicomponent.CLabel();
        cboProductTypeSecurity = new com.see.truetransact.uicomponent.CComboBox();
        panDepositType = new com.see.truetransact.uicomponent.CPanel();
        panDepositTable = new com.see.truetransact.uicomponent.CPanel();
        srpTableDeposit = new com.see.truetransact.uicomponent.CScrollPane();
        lblTotalDeposit = new com.see.truetransact.uicomponent.CLabel();
        lblTotalDepositValue = new com.see.truetransact.uicomponent.CLabel();
        txtDepNo = new com.see.truetransact.uicomponent.CTextField();
        tblDepositDetails = new com.see.truetransact.uicomponent.CTable();


        panDepositDetails = new com.see.truetransact.uicomponent.CPanel();
        lblLosName = new com.see.truetransact.uicomponent.CLabel();
        lblLosOtherInstitution = new com.see.truetransact.uicomponent.CLabel();
        lblLosSecurityNo = new com.see.truetransact.uicomponent.CLabel();
        lblLosSecurityType = new com.see.truetransact.uicomponent.CLabel();
        txtLosName = new com.see.truetransact.uicomponent.CTextField();
        txtLosSecurityNo = new com.see.truetransact.uicomponent.CTextField();
        txtLosMaturityValue = new com.see.truetransact.uicomponent.CTextField();
        lblLosIssueDate = new com.see.truetransact.uicomponent.CLabel();
        lblLosMaturityDate = new com.see.truetransact.uicomponent.CLabel();
        lblLosMaturityValue = new com.see.truetransact.uicomponent.CLabel();
        tdtLosIssueDate = new com.see.truetransact.uicomponent.CDateField();
        btnLosNew = new com.see.truetransact.uicomponent.CButton();
        btnLosSave = new com.see.truetransact.uicomponent.CButton();
        btnLosDelete = new com.see.truetransact.uicomponent.CButton();
        panLosDetails = new com.see.truetransact.uicomponent.CPanel();
        tdtLosMaturityDate = new com.see.truetransact.uicomponent.CDateField();
        cboLosOtherInstitution = new com.see.truetransact.uicomponent.CComboBox();

        lblLosRemarks = new com.see.truetransact.uicomponent.CLabel();
        cboLosSecurityType = new com.see.truetransact.uicomponent.CComboBox();
        panLosBtn = new com.see.truetransact.uicomponent.CPanel();
        panLosDetails = new com.see.truetransact.uicomponent.CPanel();
        tdtLosMaturityDt = new com.see.truetransact.uicomponent.CDateField();
        srpTableLos = new com.see.truetransact.uicomponent.CScrollPane();
        tblLosDetails = new com.see.truetransact.uicomponent.CTable();
        lblLosAmount = new com.see.truetransact.uicomponent.CLabel();
        txtLosAmount = new com.see.truetransact.uicomponent.CTextField();
        txtLosRemarks = new com.see.truetransact.uicomponent.CTextField();
        cboLosOtherInstitution = new com.see.truetransact.uicomponent.CComboBox();
        panLosTable = new com.see.truetransact.uicomponent.CPanel();

        panEmpTransfer.setLayout(new java.awt.GridBagLayout());

        panEmpTransfer.setMaximumSize(new java.awt.Dimension(650, 520));
        panEmpTransfer.setMinimumSize(new java.awt.Dimension(650, 520));
        panEmpTransfer.setPreferredSize(new java.awt.Dimension(650, 520));
        tabMasterMaintenance.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
        tabMasterMaintenance.setMinimumSize(new java.awt.Dimension(400, 325));
        tabMasterMaintenance.setPreferredSize(new java.awt.Dimension(400, 325));
        //--------------------------------------------------------------------------salary
        panAllSalaryDetails.setLayout(new java.awt.GridBagLayout());
        panAllSalaryDetails.setMinimumSize(new java.awt.Dimension(400, 650));
        panAllSalaryDetails.setPreferredSize(new java.awt.Dimension(400, 650));

        panSalaryDetails.setLayout(new java.awt.GridBagLayout());
        panSalaryDetails.setMinimumSize(new java.awt.Dimension(250, 650));
        panSalaryDetails.setPreferredSize(new java.awt.Dimension(200, 650));

        lblSalaryCertificateNo.setText("Salary Certificate No");
        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalaryDetails.add(lblSalaryCertificateNo, gridBagConstraints);

        txtSalaryCertificateNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalaryDetails.add(txtSalaryCertificateNo, gridBagConstraints);



        lblEmployerName.setText("Employer Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalaryDetails.add(lblEmployerName, gridBagConstraints);

        txtEmployerName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalaryDetails.add(txtEmployerName, gridBagConstraints);

        lblAddress.setText("Address");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalaryDetails.add(lblAddress, gridBagConstraints);

        txtAddress.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalaryDetails.add(txtAddress, gridBagConstraints);

        lblCity.setText("City");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalaryDetails.add(lblCity, gridBagConstraints);

        cboCity.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalaryDetails.add(cboCity, gridBagConstraints);

        lblPinCode.setText("PinCode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalaryDetails.add(lblPinCode, gridBagConstraints);

        txtPinCode.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalaryDetails.add(txtPinCode, gridBagConstraints);

        lblDesignation.setText("Designation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalaryDetails.add(lblDesignation, gridBagConstraints);

        txtDesignation.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalaryDetails.add(txtDesignation, gridBagConstraints);

        lblContactNo.setText("Contact No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalaryDetails.add(lblContactNo, gridBagConstraints);

        txtContactNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalaryDetails.add(txtContactNo, gridBagConstraints);

        lblRetirementDt.setText("Date Of Retirement");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalaryDetails.add(lblRetirementDt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalaryDetails.add(tdtRetirementDt, gridBagConstraints);

        tdtRetirementDt.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtRetirementDtFocusLost(evt);
            }
        });


        lblMemberNum.setText("Member No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalaryDetails.add(lblMemberNum, gridBagConstraints);

        txtMemberNum.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalaryDetails.add(txtMemberNum, gridBagConstraints);
         txtMemberNum.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMemberNumFocusLost(evt);
            }
        });

        lblMemberName.setText("MemberName");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;

        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalaryDetails.add(lblMemberName, gridBagConstraints);

        txtMemberName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalaryDetails.add(txtMemberName, gridBagConstraints);
        
        lblSalMemberRetireDate.setText("RetireDate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;

        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalaryDetails.add(lblSalMemberRetireDate, gridBagConstraints);

        lblSalMemberRetireDateValue.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalaryDetails.add(lblSalMemberRetireDateValue, gridBagConstraints);
        
        
        lblTotalSalary.setText("Total Salary");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalaryDetails.add(lblTotalSalary, gridBagConstraints);
        
        txtTotalSalary.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalaryDetails.add(txtTotalSalary, gridBagConstraints);
        txtTotalSalary.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                 if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")) { 
                txtTotalSalaryFocusLost(evt);}
        }
        });
        
        lblNetWorth1.setText("Networth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalaryDetails.add(lblNetWorth1, gridBagConstraints);

        txtNetWorth1.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalaryDetails.add(txtNetWorth1, gridBagConstraints);

        lblSalaryRemark.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalaryDetails.add(lblSalaryRemark, gridBagConstraints);

        txtSalaryRemark.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSalaryDetails.add(txtSalaryRemark, gridBagConstraints);


        panBtnSalaryType.setLayout(new java.awt.GridBagLayout());

        panBtnSalaryType.setMinimumSize(new java.awt.Dimension(105, 35));
        panBtnSalaryType.setPreferredSize(new java.awt.Dimension(105, 35));
        btnSalaryNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif")));
        btnSalaryNew.setToolTipText("New");
        btnSalaryNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnSalaryNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnSalaryNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnSalaryNew.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalaryNewActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnSalaryType.add(btnSalaryNew, gridBagConstraints);

        btnSalarySave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif")));
        btnSalarySave.setToolTipText("Save");
        btnSalarySave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnSalarySave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnSalarySave.setName("btnContactNoAdd");
        btnSalarySave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnSalarySave.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalarySaveActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnSalaryType.add(btnSalarySave, gridBagConstraints);

        btnSalaryDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif")));
        btnSalaryDelete.setToolTipText("Delete");
        btnSalaryDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnSalaryDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnSalaryDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnSalaryDelete.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalaryDeleteActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnSalaryType.add(btnSalaryDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = gridBagConstraints.CENTER;
        gridBagConstraints.insets = new java.awt.Insets(0, 100, 0, 0);
        panSalaryDetails.add(panBtnSalaryType, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);
        panAllSalaryDetails.add(panSalaryDetails, gridBagConstraints);

        //------------------table
        //                panMemberTypeDetails.setLayout(new java.awt.GridBagLayout());
        //
        //        panMemberTypeDetails.setMinimumSize(new java.awt.Dimension(300, 300));
        //        panMemberTypeDetails.setPreferredSize(new java.awt.Dimension(300, 300));
        panSalaryTable.setLayout(new java.awt.GridBagLayout());

        panSalaryTable.setMinimumSize(new java.awt.Dimension(460, 210));
        panSalaryTable.setPreferredSize(new java.awt.Dimension(460, 210));
        srpSalary.setMinimumSize(new java.awt.Dimension(450, 200));
        srpSalary.setPreferredSize(new java.awt.Dimension(450, 200));
        tblSalary.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Slno", "Certificate No", "Member No", "Name", "Contact No", "Networth"
                }));
        tblSalary.setMinimumSize(new java.awt.Dimension(375, 750));
        tblSalary.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 10000));
        tblSalary.setPreferredSize(new java.awt.Dimension(375, 750));
        tblSalary.setOpaque(false);
        tblSalary.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblSalaryMousePressed(evt);
            }
        });

        srpSalary.setViewportView(tblSalary);

        panSalaryTable.add(srpSalary, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 5, 4);
        panAllSalaryDetails.add(panSalaryTable, gridBagConstraints);
        //        panSalaryDetails.add(panBtnSalaryType);
        tabMasterMaintenance.addTab("Salary Details", panAllSalaryDetails);
        //-------------------------------------------------------------------------------------------------------------------------------salary
        panMemberTypeDetails.setLayout(new java.awt.GridBagLayout());

        panMemberTypeDetails.setMinimumSize(new java.awt.Dimension(300, 350));
        panMemberTypeDetails.setPreferredSize(new java.awt.Dimension(300, 350));
        panMemberTypeTable.setLayout(new java.awt.GridBagLayout());

        panMemberTypeTable.setMinimumSize(new java.awt.Dimension(460, 210));
        panMemberTypeTable.setPreferredSize(new java.awt.Dimension(460, 210));
        srpMemberType.setMinimumSize(new java.awt.Dimension(450, 200));
        srpMemberType.setPreferredSize(new java.awt.Dimension(450, 200));
        tblMemberType.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Member No", "Name", "Total Salary", "Contact No", "Networth"
                }));
        tblMemberType.setMinimumSize(new java.awt.Dimension(375, 750));
        tblMemberType.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 10000));
        tblMemberType.setPreferredSize(new java.awt.Dimension(375, 750));
        tblMemberType.setOpaque(false);
        tblMemberType.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblMemberTypeMousePressed(evt);
            }
        });

        srpMemberType.setViewportView(tblMemberType);

        panMemberTypeTable.add(srpMemberType, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 5, 4);
        panMemberTypeDetails.add(panMemberTypeTable, gridBagConstraints);

        panMemberDetails.setLayout(new java.awt.GridBagLayout());

        panMemberDetails.setBorder(new javax.swing.border.TitledBorder("Member Details"));
        panMemberDetails.setMinimumSize(new java.awt.Dimension(275, 300));
        panMemberDetails.setPreferredSize(new java.awt.Dimension(275, 300));
        panMemberDetails.setRequestFocusEnabled(false);
        lblMemNo.setText("Member No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMemberDetails.add(lblMemNo, gridBagConstraints);

        lblMemName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMemberDetails.add(lblMemName, gridBagConstraints);
        lblMemRetireDate.setText("RetireMentDate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMemberDetails.add(lblMemRetireDate, gridBagConstraints);
        lblMemType.setText("Type of Member");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMemberDetails.add(lblMemType, gridBagConstraints);
        lblMemberTotalSalary.setText("Total Salary");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy =4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMemberDetails.add(lblMemberTotalSalary, gridBagConstraints);
        
        txtMemberTotalSalary.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMemberDetails.add(txtMemberTotalSalary, gridBagConstraints);
        txtMemberTotalSalary.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                 if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")) { 
                txtMemberTotalSalaryFocusLost(evt);}
        }
        });
        lblMemNetworth.setText("Networth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMemberDetails.add(lblMemNetworth, gridBagConstraints);

        lblMemPriority.setText("Priority");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMemberDetails.add(lblMemPriority, gridBagConstraints);        
        
        txtMemPriority.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMemberDetails.add(txtMemPriority, gridBagConstraints);

        txtMemNetworth.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMemberDetails.add(txtMemNetworth, gridBagConstraints);

        txtContactNum.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMemberDetails.add(txtContactNum, gridBagConstraints);

        lblContactNum.setText("Contact No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMemberDetails.add(lblContactNum, gridBagConstraints);

        txtMemType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMemberDetails.add(txtMemType, gridBagConstraints);

        txtMembName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMemberDetails.add(txtMembName, gridBagConstraints);

        panMemberNumber.setLayout(new java.awt.GridBagLayout());
        lblMemberRetireDate.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMemberDetails.add(lblMemberRetireDate, gridBagConstraints);

        panMemberNumber.setLayout(new java.awt.GridBagLayout());
        txtMemNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMemberNumber.add(txtMemNo, gridBagConstraints);

        btnMemNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnMemNo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnMemNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnMemNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnMemNo.setEnabled(false);
        btnMemNo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMemNoActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMemberNumber.add(btnMemNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMemberDetails.add(panMemberNumber, gridBagConstraints);

        panBtnMemberType.setLayout(new java.awt.GridBagLayout());

        panBtnMemberType.setMinimumSize(new java.awt.Dimension(95, 35));
        panBtnMemberType.setPreferredSize(new java.awt.Dimension(95, 35));
        btnMemberNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif")));
        btnMemberNew.setToolTipText("New");
        btnMemberNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnMemberNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnMemberNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnMemberNew.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMemberNewActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnMemberType.add(btnMemberNew, gridBagConstraints);

        btnMemberSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif")));
        btnMemberSave.setToolTipText("Save");
        btnMemberSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnMemberSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnMemberSave.setName("btnContactNoAdd");
        btnMemberSave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnMemberSave.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMemberSaveActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnMemberType.add(btnMemberSave, gridBagConstraints);

        btnMemberDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif")));
        btnMemberDelete.setToolTipText("Delete");
        btnMemberDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnMemberDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnMemberDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnMemberDelete.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMemberDeleteActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnMemberType.add(btnMemberDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.ipadx = 8;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 18);
        panMemberDetails.add(panBtnMemberType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 50, 12, 4);
        panMemberTypeDetails.add(panMemberDetails, gridBagConstraints);

        tabMasterMaintenance.addTab("Member Type", panMemberTypeDetails);

        panCollateralTypeDetails.setLayout(new java.awt.GridBagLayout());
        //-------------------------------------------------table started-----------------------------one more table started
        panCollateralJointTable.setLayout(new java.awt.GridBagLayout());

        panCollateralJointTable.setMinimumSize(new java.awt.Dimension(460, 220));
        panCollateralJointTable.setPreferredSize(new java.awt.Dimension(460, 220));
        srpCollateralJointTable.setMinimumSize(new java.awt.Dimension(450, 180));
        srpCollateralJointTable.setPreferredSize(new java.awt.Dimension(450, 180));
        tblJointCollateral.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Cust Id", "Name", "Constitution"
                }));
        tblJointCollateral.setMinimumSize(new java.awt.Dimension(375, 220));
        tblJointCollateral.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 10000));
        tblJointCollateral.setPreferredSize(new java.awt.Dimension(375, 220));
        tblJointCollateral.setOpaque(false);
        //        tblCollateral.addMouseListener(new java.awt.event.MouseAdapter() {
        //            public void mousePressed(java.awt.event.MouseEvent evt) {
        //                tblCollateralMousePressed(evt);
        //            }
        //        });

        srpCollateralJointTable.setViewportView(tblJointCollateral);

        panCollateralJointTable.add(srpCollateralJointTable, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panCollateralTypeDetails.add(panCollateralJointTable, gridBagConstraints);
        ////////////////// above mine
        panCollateralTable.setLayout(new java.awt.GridBagLayout());

        panCollateralTable.setMinimumSize(new java.awt.Dimension(460, 210));
        panCollateralTable.setPreferredSize(new java.awt.Dimension(460, 210));
        srpCollateralTable.setMinimumSize(new java.awt.Dimension(450, 200));
        srpCollateralTable.setPreferredSize(new java.awt.Dimension(450, 200));
        tblCollateral.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Member No", "Name", "Doc No", "PledgeAmt", "SurveyNo", "TotalArea"
                }));
        tblCollateral.setMinimumSize(new java.awt.Dimension(375, 750));
        tblCollateral.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 10000));
        tblCollateral.setPreferredSize(new java.awt.Dimension(375, 750));
        tblCollateral.setOpaque(false);
        tblCollateral.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblCollateralMousePressed(evt);
            }
        });

        srpCollateralTable.setViewportView(tblCollateral);

        panCollateralTable.add(srpCollateralTable, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panCollateralTypeDetails.add(panCollateralTable, gridBagConstraints);

        panCollatetalDetails.setLayout(new java.awt.GridBagLayout());
        /*
         * ------------------------------------------------------------- gahan
         * --------------------------------------------------------------------------------------------------------------
         */
        panCollatetalDetails.setMinimumSize(new java.awt.Dimension(300, 570));
        panCollatetalDetails.setPreferredSize(new java.awt.Dimension(300, 570));
        panCollatetalDetails.setRequestFocusEnabled(false);
        lblGahanYesNo.setText("Gahan");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCollatetalDetails.add(lblGahanYesNo, gridBagConstraints);

        panGahanYesNo.setLayout(new java.awt.GridBagLayout());
        rdoGahanYes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        panGahanYesNo.add(rdoGahanYes, gridBagConstraints);
        rdoGahanYes.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoGahanYesActionPerformed(evt);
            }
        });
        rdoGahanNo.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panGahanYesNo.add(rdoGahanNo, gridBagConstraints);
        rdoGahanNo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoGahanNoActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(panGahanYesNo, gridBagConstraints);

        //        panCollatetalDetails.setMinimumSize(new java.awt.Dimension(400, 500));
        //        panCollatetalDetails.setPreferredSize(new java.awt.Dimension(400, 500));
        //        panCollatetalDetails.setRequestFocusEnabled(false);
        lblOwnerMemberNo.setText("Owner Member No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(lblOwnerMemberNo, gridBagConstraints);


        lblOwnerMemberNname.setText("Owner Member Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(lblOwnerMemberNname, gridBagConstraints);

        txtOwnerMemberNname.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(txtOwnerMemberNname, gridBagConstraints);


        lblDocumentNo.setText("Document No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(lblDocumentNo, gridBagConstraints);

        panDocumentNumber.setLayout(new java.awt.GridBagLayout());

        txtDocumentNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;//1
        gridBagConstraints.gridy = 0;//3
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panDocumentNumber.add(txtDocumentNo, gridBagConstraints);
        txtDocumentNo.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDocumentNoFocusLost(evt);
            }
        });

        //        txtDocumentNo.addActionListener(new java.awt.event. ActionListener(){
        //            public void actionPerformed(java.awt.event.ActionEvent evt){
        //                txtDocumentNoActionPerformed(evt);
        //            }
        //        });

        btnDocumentNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnDocumentNo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnDocumentNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDocumentNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDocumentNo.setEnabled(false);
        btnDocumentNo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDocumentNoActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;

        panDocumentNumber.add(btnDocumentNo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;

        panCollatetalDetails.add(panDocumentNumber, gridBagConstraints);

        lblDocumentType.setText("Document Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(lblDocumentType, gridBagConstraints);

        cboDocumentType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(cboDocumentType, gridBagConstraints);

        lblDocumentDate.setText("Document Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(lblDocumentDate, gridBagConstraints);


        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(tdtDocumentDate, gridBagConstraints);

        lblRegisteredOffice.setText("Registered Office");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(lblRegisteredOffice, gridBagConstraints);

        txtRegisteredOffice.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(txtRegisteredOffice, gridBagConstraints);


        // ------------------------------------------
        lblPledgeNo.setText("Pledge No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(lblPledgeNo, gridBagConstraints);

        txtPledgeNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(txtPledgeNo, gridBagConstraints);

        lblPledgeDate.setText("Pledge Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(lblPledgeDate, gridBagConstraints);


        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(tdtPledgeDate, gridBagConstraints);


        // ------------------------end----------------- -->


        lblPledgeType.setText("Pledge Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(lblPledgeType, gridBagConstraints);

        cboPledge.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(cboPledge, gridBagConstraints);






        //            lblPledge.setText("Pledge");
        //            gridBagConstraints = new java.awt.GridBagConstraints();
        //            gridBagConstraints.gridx = 0;
        //            gridBagConstraints.gridy = 9;
        //            gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        //            gridBagConstraints.insets =  new java.awt.Insets(2, 2, 2, 2);
        //            panCollatetalDetails.add(lblPledge, gridBagConstraints);

        lblPledgeAmount.setText("Pledge Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(lblPledgeAmount, gridBagConstraints);

        txtPledgeAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPledgeAmount.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPledgeAmountFocusLost(evt);
            }
        });
        //        txtPledgeAmount.addActionListener( new java.awt.event.ActionListener(){
        //           public void actionPerformed(java.awt.event.ActionEvent evt) {
        ////                btnDocumentNoActionPerformed(evt);
        //                txtPledgeAmountActionPerformed(evt);
        //            }
        //        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(txtPledgeAmount, gridBagConstraints);


        //            -----------------

        lblVillage.setText("Village");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(lblVillage, gridBagConstraints);

        txtVillage.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(txtVillage, gridBagConstraints);

        lblSurveyNo.setText("Survey No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(lblSurveyNo, gridBagConstraints);

        txtSurveyNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(txtSurveyNo, gridBagConstraints);


        lblTotalArea.setText("Total Area (In Cents)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(lblTotalArea, gridBagConstraints);

        txtTotalArea.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(txtTotalArea, gridBagConstraints);





        //        lblRemarks.setText("Remarks");
        //        gridBagConstraints = new java.awt.GridBagConstraints();
        //        gridBagConstraints.gridx = 0;
        //        gridBagConstraints.gridy = 15;
        //        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        //        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        //        panCollatetalDetails.add(lblRemarks, gridBagConstraints);

        lblNature.setText("Nature");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panCollatetalDetails.add(lblNature, gridBagConstraints);


        cboNature.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCollatetalDetails.add(cboNature, gridBagConstraints);

        lblRight.setText("Right");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panCollatetalDetails.add(lblRight, gridBagConstraints);

        cboRight.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCollatetalDetails.add(cboRight, gridBagConstraints);

        //       ---------------------------------------------------------------------------------------------------
        panBtnCollateralType.setLayout(new java.awt.GridBagLayout());

        panBtnCollateralType.setMinimumSize(new java.awt.Dimension(95, 35));
        panBtnCollateralType.setPreferredSize(new java.awt.Dimension(95, 35));
        btnCollateralNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif")));
        btnCollateralNew.setToolTipText("New");
        btnCollateralNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnCollateralNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnCollateralNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnCollateralNew.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCollateralNewActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnCollateralType.add(btnCollateralNew, gridBagConstraints);

        btnCollateralSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif")));
        btnCollateralSave.setToolTipText("Save");
        btnCollateralSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnCollateralSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnCollateralSave.setName("btnContactNoAdd");
        btnCollateralSave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnCollateralSave.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCollateralSaveActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnCollateralType.add(btnCollateralSave, gridBagConstraints);

        btnCollateralDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif")));
        btnCollateralDelete.setToolTipText("Delete");
        btnCollateralDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnCollateralDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnCollateralDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnCollateralDelete.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCollateralDeleteActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnCollateralType.add(btnCollateralDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 18);
        panCollatetalDetails.add(panBtnCollateralType, gridBagConstraints);

        srpTxtAreaParticulars.setMinimumSize(new java.awt.Dimension(150, 45));
        srpTxtAreaParticulars.setPreferredSize(new java.awt.Dimension(150, 45));
        txtAreaParticular.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        txtAreaParticular.setLineWrap(true);
        txtAreaParticular.setMaximumSize(new java.awt.Dimension(2, 14));
        txtAreaParticular.setMinimumSize(new java.awt.Dimension(2, 14));
        txtAreaParticular.setPreferredSize(new java.awt.Dimension(2, 14));
        srpTxtAreaParticulars.setViewportView(txtAreaParticular);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.gridwidth = 2;
        panCollatetalDetails.add(srpTxtAreaParticulars, gridBagConstraints);

        panOwnerMemberNumber.setLayout(new java.awt.GridBagLayout());

        txtOwnerMemNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtOwnerMemNo.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                txtOwnerMemNoFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panOwnerMemberNumber.add(txtOwnerMemNo, gridBagConstraints);

        btnOwnerMemNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnOwnerMemNo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnOwnerMemNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnOwnerMemNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnOwnerMemNo.setEnabled(false);
        btnOwnerMemNo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOwnerMemNoActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panOwnerMemberNumber.add(btnOwnerMemNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCollatetalDetails.add(panOwnerMemberNumber, gridBagConstraints);

        //        cboPledge.setMinimumSize(new java.awt.Dimension(100, 21));
        //        gridBagConstraints = new java.awt.GridBagConstraints();
        //        gridBagConstraints.gridx = 1;
        //        gridBagConstraints.gridy = 6;
        //        gridBagConstraints.ipady = 2;
        //        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        //        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        //        panCollatetalDetails.add(cboPledge, gridBagConstraints);




        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panCollateralTypeDetails.add(panCollatetalDetails, gridBagConstraints);

        tabMasterMaintenance.addTab("Collateral Type", panCollateralTypeDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panEmpTransfer.add(tabMasterMaintenance, gridBagConstraints);

        panSecurityDetails.add(panEmpTransfer, gridBagConstraints);

        txtMemNo.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMemNoFocusLost(evt);
            }
        });

        txtOwnerMemNo.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                txtOwnerMemNoFocusLost(evt);
            }
        });

        //Gold Type Security
        panGoldTypeDetails.setLayout(new java.awt.GridBagLayout());

        panGoldTypeDetails.setMinimumSize(new java.awt.Dimension(250, 300));
        panGoldTypeDetails.setPreferredSize(new java.awt.Dimension(250, 300));
        lblJewelleryDetails.setText("Jewellery Details");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 4);
        panGoldTypeDetails.add(lblJewelleryDetails, gridBagConstraints);

        lblGrossWeight.setText("Gross Weight(grams");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 4);
        panGoldTypeDetails.add(lblGrossWeight, gridBagConstraints);

        lblNetWeight.setText("Net Weight(grams)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 4);
        panGoldTypeDetails.add(lblNetWeight, gridBagConstraints);

        lblValueOfGold.setText("Value of the Gold");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 4);
        panGoldTypeDetails.add(lblValueOfGold, gridBagConstraints);

        lblGoldRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 4);
        panGoldTypeDetails.add(lblGoldRemarks, gridBagConstraints);

        txtGoldRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panGoldTypeDetails.add(txtGoldRemarks, gridBagConstraints);

        txtValueOfGold.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panGoldTypeDetails.add(txtValueOfGold, gridBagConstraints);
        txtValueOfGold.setValidation(new CurrencyValidation());

        txtNetWeight.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panGoldTypeDetails.add(txtNetWeight, gridBagConstraints);

        txtGrossWeight.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panGoldTypeDetails.add(txtGrossWeight, gridBagConstraints);

        srpTxtAreaParticulars.setMinimumSize(new java.awt.Dimension(400, 60));
        srpTxtAreaParticulars.setPreferredSize(new java.awt.Dimension(400, 60));
        txtJewelleryDetails.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        txtJewelleryDetails.setLineWrap(true);

        srpTxtAreaParticulars.setViewportView(txtJewelleryDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panGoldTypeDetails.add(srpTxtAreaParticulars, gridBagConstraints);

        txtGrossWeight.setValidation(new NumericValidation(3, 2));
        txtNetWeight.setValidation(new NumericValidation(3, 2));
        txtValueOfGold.setAllowAll(true);
        txtGoldRemarks.setAllowAll(true);
        tabMasterMaintenance.addTab("Gold Type", panGoldTypeDetails);



        tabMasterMaintenance.addTab("Gold Type", panGoldTypeDetails);


        //DepositType security

        panDepositDetails.setLayout(new java.awt.GridBagLayout());

        panDepositDetails.setMinimumSize(new java.awt.Dimension(850, 225));
        panDepositDetails.setPreferredSize(new java.awt.Dimension(850, 225));
        panDepositType.setLayout(new java.awt.GridBagLayout());

        panDepositType.setMinimumSize(new java.awt.Dimension(440, 225));
        panDepositType.setPreferredSize(new java.awt.Dimension(440, 225));
        panDepositType.setRequestFocusEnabled(false);
        lblProductId2.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panDepositType.add(lblProductId2, gridBagConstraints);

        lblDepAmount.setText("Dep Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 25, 4);
        panDepositType.add(lblDepAmount, gridBagConstraints);

        lblRateOfInterest.setText("Rate of Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 4);
        panDepositType.add(lblRateOfInterest, gridBagConstraints);

        lblDepDt.setText("Dep Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panDepositType.add(lblDepDt, gridBagConstraints);

        txtDepAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 25, 2);
        panDepositType.add(txtDepAmount, gridBagConstraints);

        txtMaturityValue.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDepositType.add(txtMaturityValue, gridBagConstraints);

        txtRateOfInterest.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDepositType.add(txtRateOfInterest, gridBagConstraints);

        lblMaturityDt.setText("Maturity Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 4);
        panDepositType.add(lblMaturityDt, gridBagConstraints);

        lblMaturityValue.setText("Maturity Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 4);
        panDepositType.add(lblMaturityValue, gridBagConstraints);

        lblDepNo.setText("Deposit No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panDepositType.add(lblDepNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panDepositType.add(tdtDepDt, gridBagConstraints);

        panBtnDeposit.setLayout(new java.awt.GridBagLayout());

        panBtnDeposit.setMinimumSize(new java.awt.Dimension(95, 35));
        panBtnDeposit.setPreferredSize(new java.awt.Dimension(95, 35));
        btnDepositNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif")));
        btnDepositNew.setToolTipText("New");
        btnDepositNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnDepositNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnDepositNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnDepositNew.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDepositNewActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnDeposit.add(btnDepositNew, gridBagConstraints);

        btnDepositSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif")));
        btnDepositSave.setToolTipText("Save");
        btnDepositSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnDepositSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnDepositSave.setName("btnContactNoAdd");
        btnDepositSave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnDepositSave.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDepositSaveActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnDeposit.add(btnDepositSave, gridBagConstraints);

        btnDepositDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif")));
        btnDepositDelete.setToolTipText("Delete");
        btnDepositDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnDepositDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnDepositDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnDepositDelete.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDepositDeleteActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnDeposit.add(btnDepositDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 2);
        panDepositType.add(panBtnDeposit, gridBagConstraints);

        txtMaturityDt.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                //txtMaturityDtFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDepositType.add(txtMaturityDt, gridBagConstraints);

        cboDepProdType.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"----Select----"}));
        cboDepProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboDepProdType.setPopupWidth(165);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panDepositType.add(cboDepProdType, gridBagConstraints);

        panDepNo.setLayout(new java.awt.GridBagLayout());

        txtDepNo.setAllowAll(true);
        txtDepNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDepNo.add(txtDepNo, gridBagConstraints);

        btnDepNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnDepNo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnDepNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDepNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDepNo.setEnabled(false);
        btnDepNo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDepNoActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panDepNo.add(btnDepNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panDepositType.add(panDepNo, gridBagConstraints);

        lblProductTypeSecurity.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panDepositType.add(lblProductTypeSecurity, gridBagConstraints);

        cboProductTypeSecurity.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"----Select----"}));
        cboProductTypeSecurity.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductTypeSecurity.setPopupWidth(120);
        cboProductTypeSecurity.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductTypeSecurityActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panDepositType.add(cboProductTypeSecurity, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panDepositDetails.add(panDepositType, gridBagConstraints);

        panDepositTable.setLayout(new java.awt.GridBagLayout());

        panDepositTable.setMinimumSize(new java.awt.Dimension(380, 220));
        panDepositTable.setPreferredSize(new java.awt.Dimension(380, 220));
        srpTableDeposit.setMinimumSize(new java.awt.Dimension(375, 160));
        srpTableDeposit.setPreferredSize(new java.awt.Dimension(375, 160));
        tblDepositDetails.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Prod Type", "Dep No", "Dep Amt", "Matur Val"
                }));
        tblDepositDetails.setMinimumSize(new java.awt.Dimension(275, 750));
        tblDepositDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 10000));
        tblDepositDetails.setPreferredSize(new java.awt.Dimension(275, 750));
        tblDepositDetails.setOpaque(false);
        tblDepositDetails.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDepositDetailsMousePressed(evt);
            }
        });

        srpTableDeposit.setViewportView(tblDepositDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 1, 0);
        panDepositTable.add(srpTableDeposit, gridBagConstraints);

        lblTotalDeposit.setText("Total Deposit Value ");
        lblTotalDeposit.setMinimumSize(new java.awt.Dimension(160, 18));
        lblTotalDeposit.setPreferredSize(new java.awt.Dimension(160, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 4, 6);
        panDepositTable.add(lblTotalDeposit, gridBagConstraints);

        lblTotalDepositValue.setText("                          ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 5);
        panDepositTable.add(lblTotalDepositValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panDepositDetails.add(panDepositTable, gridBagConstraints);

        tabMasterMaintenance.addTab("Deposit Type", panDepositDetails);
        //Other Security Details


        panOtherSecurityDetails.setLayout(new java.awt.GridBagLayout());

        panOtherSecurityDetails.setMinimumSize(new java.awt.Dimension(850, 225));
        panOtherSecurityDetails.setPreferredSize(new java.awt.Dimension(850, 225));
        panOtherSecurityDetails.setLayout(new java.awt.GridBagLayout());
        panLosDetails.setLayout(new java.awt.GridBagLayout());
        panLosDetails.setMinimumSize(new java.awt.Dimension(440, 225));
        panLosDetails.setPreferredSize(new java.awt.Dimension(440, 225));
        panLosDetails.setRequestFocusEnabled(false);
        lblLosOtherInstitution.setText("Other Institution");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panLosDetails.add(lblLosOtherInstitution, gridBagConstraints);

        cboLosOtherInstitution.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"----Select----"}));
        cboLosOtherInstitution.setMinimumSize(new java.awt.Dimension(100, 21));
        cboLosOtherInstitution.setPopupWidth(165);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLosDetails.add(cboLosOtherInstitution, gridBagConstraints);

        lblLosName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panLosDetails.add(lblLosName, gridBagConstraints);

        txtLosName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panLosDetails.add(txtLosName, gridBagConstraints);

        lblLosSecurityType.setText("Security Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panLosDetails.add(lblLosSecurityType, gridBagConstraints);



        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panLosDetails.add(cboLosSecurityType, gridBagConstraints);

        lblLosSecurityNo.setText("Security No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panLosDetails.add(lblLosSecurityNo, gridBagConstraints);

        txtLosSecurityNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panLosDetails.add(txtLosSecurityNo, gridBagConstraints);
        txtLosSecurityNo.setAllowAll(true);

        lblLosAmount.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 25, 4);
        panLosDetails.add(lblLosAmount, gridBagConstraints);

        txtLosAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 25, 2);
        panLosDetails.add(txtLosAmount, gridBagConstraints);
        txtLosAmount.setValidation(new CurrencyValidation());

        lblLosIssueDate.setText("Issue Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 4);
        panLosDetails.add(lblLosIssueDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLosDetails.add(tdtLosIssueDate, gridBagConstraints);

        lblLosMaturityDate.setText("Maturity Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 4);
        panLosDetails.add(lblLosMaturityDate, gridBagConstraints);

        tdtLosMaturityDt.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                //tdtLosMaturityDtFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLosDetails.add(tdtLosMaturityDt, gridBagConstraints);

        lblLosMaturityValue.setText("Maturity Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panLosDetails.add(lblLosMaturityValue, gridBagConstraints);

        txtLosMaturityValue.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLosDetails.add(txtLosMaturityValue, gridBagConstraints);
        txtLosMaturityValue.setValidation(new CurrencyValidation());

        lblLosRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLosDetails.add(lblLosRemarks, gridBagConstraints);



        txtLosRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLosDetails.add(txtLosRemarks, gridBagConstraints);

        panLosBtn.setLayout(new java.awt.GridBagLayout());

        panLosBtn.setMinimumSize(new java.awt.Dimension(95, 35));
        panLosBtn.setPreferredSize(new java.awt.Dimension(95, 35));
        btnLosNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif")));
        btnLosNew.setToolTipText("New");
        btnLosNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnLosNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnLosNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnLosNew.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLosNewActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLosBtn.add(btnLosNew, gridBagConstraints);

        btnLosSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif")));
        btnLosSave.setToolTipText("Save");
        btnLosSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnLosSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnLosSave.setName("btnContactNoAdd");
        btnLosSave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnLosSave.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLosSaveActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLosBtn.add(btnLosSave, gridBagConstraints);

        btnLosDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif")));
        btnLosDelete.setToolTipText("Delete");
        btnLosDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnLosDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnLosDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnLosDelete.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLosDeleteActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLosBtn.add(btnLosDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 2);
        panLosDetails.add(panLosBtn, gridBagConstraints);



        cboLosSecurityType.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"----Select----"}));
        cboLosSecurityType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboLosSecurityType.setPopupWidth(120);
        cboLosSecurityType.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // cboLoseSecurityTypeActionPerformed(evt);
            }
        });



        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panOtherSecurityDetails.add(panLosDetails, gridBagConstraints);

        panLosTable.setLayout(new java.awt.GridBagLayout());

        panLosTable.setMinimumSize(new java.awt.Dimension(380, 220));
        panLosTable.setPreferredSize(new java.awt.Dimension(380, 220));
        srpTableLos.setMinimumSize(new java.awt.Dimension(375, 160));
        srpTableLos.setPreferredSize(new java.awt.Dimension(375, 160));
        tblLosDetails.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "OtherInstitution", "Name", "Security No", "SecurityType", "Amount"
                }));
        tblLosDetails.setMinimumSize(new java.awt.Dimension(275, 750));
        tblLosDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 10000));
        tblLosDetails.setPreferredSize(new java.awt.Dimension(275, 750));
        tblLosDetails.setOpaque(false);
        tblLosDetails.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblLosDetailsMousePressed(evt);
            }
        });

        srpTableLos.setViewportView(tblLosDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 1, 0);
        panLosTable.add(srpTableLos, gridBagConstraints);


        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panOtherSecurityDetails.add(panLosTable, gridBagConstraints);

        tabMasterMaintenance.addTab("Other Society Details", panOtherSecurityDetails);
       // *****************************************************************************Vehichle********************************************
        panVehicleTypeDetails.setLayout(new java.awt.GridBagLayout());

        panVehicleTypeDetails.setMinimumSize(new java.awt.Dimension(900, 650));
        panVehicleTypeDetails.setPreferredSize(new java.awt.Dimension(900, 650));
        panVehicleTypeTable.setLayout(new java.awt.GridBagLayout());

        panVehicleTypeTable.setMinimumSize(new java.awt.Dimension(460, 210));
        panVehicleTypeTable.setPreferredSize(new java.awt.Dimension(460, 210));
        srpVehicleType.setMinimumSize(new java.awt.Dimension(450, 200));
        srpVehicleType.setPreferredSize(new java.awt.Dimension(450, 200));
        tblVehicleType.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Member No", "Name", "Vehicle number","Total Salary", "NetWorth", "RC book number"
                }));
        tblVehicleType.setMinimumSize(new java.awt.Dimension(375, 750));
        tblVehicleType.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 10000));
        tblVehicleType.setPreferredSize(new java.awt.Dimension(375, 750));
        tblVehicleType.setOpaque(false);
        tblVehicleType.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblVehicleTypeMousePressed(evt);
            }
        });

        srpVehicleType.setViewportView(tblVehicleType);

        panVehicleTypeTable.add(srpVehicleType, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 5, 4);
        panVehicleTypeDetails.add(panVehicleTypeTable, gridBagConstraints);

        panVehicleDetails.setLayout(new java.awt.GridBagLayout());

        panVehicleDetails.setBorder(new javax.swing.border.TitledBorder("Vehicle Details"));
        panVehicleDetails.setMinimumSize(new java.awt.Dimension(300, 750));
        panVehicleDetails.setPreferredSize(new java.awt.Dimension(300, 750));
        panVehicleDetails.setRequestFocusEnabled(false);
        lblVehicleMemNo.setText("Vehicle Member No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panVehicleDetails.add(lblVehicleMemNo, gridBagConstraints);

        lblVehicleMemName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panVehicleDetails.add(lblVehicleMemName, gridBagConstraints);
        lblVehicleMemRetireDate.setText("RetireMentDate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panVehicleDetails.add(lblVehicleMemRetireDate, gridBagConstraints);
        lblVehicleMemType.setText("Type of Member");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panVehicleDetails.add(lblVehicleMemType, gridBagConstraints);

        lblVehicleNo.setText("VehicleNo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panVehicleDetails.add(lblVehicleNo, gridBagConstraints);
        
        txtVehicleNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panVehicleDetails.add(txtVehicleNo, gridBagConstraints);
        
        lblVehicleType.setText("Vehicle Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panVehicleDetails.add(lblVehicleType, gridBagConstraints);
        
        txtVehicleType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panVehicleDetails.add(txtVehicleType, gridBagConstraints);
     
        lblVehicleDetails.setText("Vehicle Details");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panVehicleDetails.add(lblVehicleDetails, gridBagConstraints);
        
        lblTotalVehicleMemSal.setText("Total Salary");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panVehicleDetails.add(lblTotalVehicleMemSal, gridBagConstraints);

        txtVehicleMemSal.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panVehicleDetails.add(txtVehicleMemSal, gridBagConstraints);
        txtVehicleMemSal.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                txtVehicleMemSalFocusLost(evt);
            }
        });
        
        lblVehicleNetWorth.setText("Networth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panVehicleDetails.add(lblVehicleNetWorth, gridBagConstraints);

        txtVehicleNetWorth.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panVehicleDetails.add(txtVehicleNetWorth, gridBagConstraints);
        
        srpTxtAreaVehicledtails.setMinimumSize(new java.awt.Dimension(150, 60));
        srpTxtAreaVehicledtails.setPreferredSize(new java.awt.Dimension(150, 60));
        txtVehicleDetals.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        txtVehicleDetals.setLineWrap(true);
        srpTxtAreaVehicledtails.setViewportView(txtVehicleDetals);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        panVehicleDetails.add(srpTxtAreaVehicledtails, gridBagConstraints);

        
        lblVehicleRcBookNo.setText("Rc Book No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panVehicleDetails.add(lblVehicleRcBookNo, gridBagConstraints);
        
        txtVehicleRcBookNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panVehicleDetails.add(txtVehicleRcBookNo, gridBagConstraints);
        
        lblVehicleDate.setText("Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panVehicleDetails.add(lblVehicleDate, gridBagConstraints);
        
        txtVehicleDate.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panVehicleDetails.add(txtVehicleDate, gridBagConstraints);
        
        
        txtVehicleContactNum.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panVehicleDetails.add(txtVehicleContactNum, gridBagConstraints);

        lblVehicleContactNum.setText("Contact No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panVehicleDetails.add(lblVehicleContactNum, gridBagConstraints);

        txtVehicleMemType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panVehicleDetails.add(txtVehicleMemType, gridBagConstraints);

        txtVehicleMemberName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panVehicleDetails.add(txtVehicleMemberName, gridBagConstraints);

        panVehicleNumber.setLayout(new java.awt.GridBagLayout());
        lblVehicleMemRetireDate.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panVehicleDetails.add(lblVehicleMemRetireDate, gridBagConstraints);

        panVehicleNumber.setLayout(new java.awt.GridBagLayout());
        txtVehicleMemberNum.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panVehicleNumber.add(txtVehicleMemberNum, gridBagConstraints);

        btnVehicleMemNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnVehicleMemNo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnVehicleMemNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnVehicleMemNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnVehicleMemNo.setEnabled(false);
        btnVehicleMemNo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVehicleMemNoActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panVehicleNumber.add(btnVehicleMemNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panVehicleDetails.add(panVehicleNumber, gridBagConstraints);

        panBtnVehicleType.setLayout(new java.awt.GridBagLayout());

        panBtnVehicleType.setMinimumSize(new java.awt.Dimension(95, 35));
        panBtnVehicleType.setPreferredSize(new java.awt.Dimension(95, 35));
        btnVehicleNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif")));
        btnVehicleNew.setToolTipText("New");
        btnVehicleNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnVehicleNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnVehicleNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnVehicleNew.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVehicleNewActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnVehicleType.add(btnVehicleNew, gridBagConstraints);

        btnVehicleSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif")));
        btnVehicleSave.setToolTipText("Save");
        btnVehicleSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnVehicleSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnVehicleSave.setName("btnContactNoAdd");
        btnVehicleSave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnVehicleSave.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVehicleSaveActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnVehicleType.add(btnVehicleSave, gridBagConstraints);

        btnVehicleDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif")));
        btnVehicleDelete.setToolTipText("Delete");
        btnVehicleDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnVehicleDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnVehicleDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnVehicleDelete.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVehicleDeleteActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnVehicleType.add(btnVehicleDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 18);
        panVehicleDetails.add(panBtnVehicleType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 50, 12, 4);
        panVehicleTypeDetails.add(panVehicleDetails, gridBagConstraints);

       tabMasterMaintenance.addTab("Vehicle Type", panVehicleTypeDetails);

   
    //******************************************************************************************************************************
    }

    private void tdtRetirementDtFocusLost(java.awt.event.FocusEvent evt) {
        String rtDate = CommonUtil.convertObjToStr(tdtRetirementDt.getDateValue());
        if (rtDate.length() > 0 && DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(rtDate), (Date) curr_dt.clone()) > 0) {
            ClientUtil.displayAlert("Retirement Date Should be in Future Date");
            tdtRetirementDt.setDateValue("");
        }
    }
     private void txtVehicleMemSalFocusLost(java.awt.event.FocusEvent evt) {
         HashMap whereMap = new HashMap();
      
        String memNo = txtVehicleMemberNum.getText();
      //  if(memNo!=null){
        whereMap.put("PROD_ID", ((ComboBoxModel) cboSchemName.getModel()).getKeyForSelected().toString());
        List custList = ClientUtil.executeQuery("selectSalSecurityLimit", whereMap);
        if (custList != null && custList.size() > 0) {
            HashMap resultMap = (HashMap) custList.get(0);
            if (resultMap.containsKey("SAL_SECURITY_LIMIT") && resultMap.get("SAL_SECURITY_LIMIT") != null) {
                double netWorth = CommonUtil.convertObjToDouble(resultMap.get("SAL_SECURITY_LIMIT")) * CommonUtil.convertObjToDouble(txtVehicleMemSal.getText());
               txtVehicleNetWorth.setText(CommonUtil.convertObjToStr(netWorth));
            }
        }
     }
    private void txtMemberNumFocusLost(java.awt.event.FocusEvent evt) {
        HashMap whereMap = new HashMap();
        String memNo = txtMemberNum.getText();
        whereMap.put("MEMBER_NO", memNo);
        List custList = ClientUtil.executeQuery("getMemberName", whereMap);
        if (custList != null && custList.size() > 0) {
            HashMap resultMap = (HashMap) custList.get(0);
            if (resultMap.containsKey("FNAME") && resultMap.get("FNAME") != null) {
                txtMemberName.setText(CommonUtil.convertObjToStr(resultMap.get("FNAME")));
            }
            if(resultMap.containsKey("RETIREMENT_DT")&&resultMap.get("RETIREMENT_DT")!=null)
            {
             lblSalMemberRetireDateValue.setText(CommonUtil.convertObjToStr(resultMap.get("RETIREMENT_DT")));
            }
        }
    }

    private void txtTotalSalaryFocusLost(java.awt.event.FocusEvent evt) {
        HashMap whereMap = new HashMap();
      
        String memNo = txtMemberNum.getText();
      //  if(memNo!=null){
        whereMap.put("PROD_ID", ((ComboBoxModel) cboSchemName.getModel()).getKeyForSelected().toString());
        List custList = ClientUtil.executeQuery("selectSalSecurityLimit", whereMap);
        if (custList != null && custList.size() > 0) {
            HashMap resultMap = (HashMap) custList.get(0);
            if (resultMap.containsKey("SAL_SECURITY_LIMIT") && resultMap.get("SAL_SECURITY_LIMIT") != null) {
                double netWorth = CommonUtil.convertObjToDouble(resultMap.get("SAL_SECURITY_LIMIT")) * CommonUtil.convertObjToDouble(txtTotalSalary.getText());
                txtNetWorth1.setText(CommonUtil.convertObjToStr(netWorth));
            }
        }
       // }
    }
    
     private void txtMemberTotalSalaryFocusLost(java.awt.event.FocusEvent evt) {
        HashMap whereMap = new HashMap();
        String memNo =txtMemNo.getText();
       // if(memNo!=null){
        whereMap.put("PROD_ID", ((ComboBoxModel) cboSchemName.getModel()).getKeyForSelected().toString());
        List custList = ClientUtil.executeQuery("selectSalSecurityLimit", whereMap);
        if (custList != null && custList.size() > 0) {
            HashMap resultMap = (HashMap) custList.get(0);
            if (resultMap.containsKey("SAL_SECURITY_LIMIT") && resultMap.get("SAL_SECURITY_LIMIT") != null) {
                double netWorth = CommonUtil.convertObjToDouble(resultMap.get("SAL_SECURITY_LIMIT")) * CommonUtil.convertObjToDouble(txtMemberTotalSalary.getText());
                txtMemNetworth.setText(CommonUtil.convertObjToStr(netWorth));
            }
        }
      //  }
    }
    private void btnSalaryNewActionPerformed(java.awt.event.ActionEvent evt) {

        resetSalaryDetails();
        enableDisableSalaryDetails(true);
        enableDisableSalaryBtns(true);
        salarytblSelectedRow = -1;

    }

    private void btnSalarySaveActionPerformed(java.awt.event.ActionEvent evt) {
        if (CommonUtil.convertObjToStr(txtSalaryCertificateNo.getText()).length() > 0) {
            updateSalaryOBFields();
            int rowcount = tblSalary.getRowCount();
            observable.setSalarySecrityTableValue(salarytblSelectedRow, rowcount);
            tblSalary.setModel(observable.getTblSalarySecrityTable());
            enableDisableSalaryDetails(false);
            resetSalaryDetails();
            enableDisableSalaryBtnsNew(true);
            salarytblSelectedRow = -1;
        } else {
            ClientUtil.displayAlert("Please Enter Salary Certificate No");
        }

    }

    private void btnSalaryDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        observable.deleteSalarySecrityTableValue(salarytblSelectedRow);
        resetSalaryDetails();
        enableDisableSalaryBtnsNew(true);
        salarytblSelectedRow = -1;
    }

    private void tblSalaryMousePressed(java.awt.event.MouseEvent evt) {
        salarytblSelectedRow = tblSalary.getSelectedRow();
        observable.showSalaryTableValues(salarytblSelectedRow);
        enableDisableSalaryBtnsNew(false);
        if (viewType == AUTHORIZE || viewType == REJECT) {
            enableDisableSalaryBtns(false);
        }
        updateSalaryUI();
    }

    private void tblMemberTypeMousePressed(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        updateMemberTypeFields();
        updateMode = true;
        updateTab = tblMemberType.getSelectedRow();
        observable.setMemberTypeData(false);
        String st = CommonUtil.convertObjToStr(tblMemberType.getValueAt(tblMemberType.getSelectedRow(), 0));
        observable.populateMemberTypeDetails(st);
        populateMemberTypeFields();
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT
                || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
            btnSecurityMember(false);
            ClientUtil.enableDisable(panMemberDetails, false);
        } else {
            btnSecurityMember(true);
            ClientUtil.enableDisable(panMemberDetails, true);
            btnMemberNew.setEnabled(false);
        }
        txtMembName.setEnabled(false);
        txtMemType.setEnabled(false);
        txtMemNo.setEnabled(false);
    }
private void tblVehicleTypeMousePressed(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        updateVehicleTypeFields();
        updateMode = true;
        updateTab = tblVehicleType.getSelectedRow();
        observable.setVehicleTypeData(false);
        String st = CommonUtil.convertObjToStr(tblVehicleType.getValueAt(tblVehicleType.getSelectedRow(), 0));
        observable.populateVehicleTypeDetails(st);
        populateVehicleTypeFields();
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT
                || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
            btnSecurityVehichle(false);
            ClientUtil.enableDisable(panVehicleDetails, false);
        } else {
            btnSecurityVehichle(true);
            ClientUtil.enableDisable(panVehicleDetails, true);
            btnVehicleNew.setEnabled(false);
        }
        txtVehicleMemberName.setEnabled(false);
        txtVehicleMemType.setEnabled(false);
        txtVehicleMemberNum.setEnabled(false);
    }
    private void btnMemNoActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        //        popUp("SUB_MEMBER_NO");
        viewType = "SUB_MEMBER_NO";
        new CheckCustomerIdUI(this);

    }
        private void btnVehicleMemNoActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        //        popUp("SUB_MEMBER_NO");
        viewType = "SUB_MEMBER_NO_VEHICLE";
        new CheckCustomerIdUI(this);

    }
    private void btnMemberNewActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        //System.out.println("membernew");
        updateMode = false;
        observable.setMemberTypeData(true);
        btnSecurityMember(false);
        btnMemberSave.setEnabled(true);
        ClientUtil.enableDisable(panMemberDetails, true);
        btnMemNo.setEnabled(true);
        txtMembName.setEnabled(false);
        txtMemType.setEnabled(false);
        rdoGahanYesActionPerformed(null);
    }
    private void btnVehicleNewActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        //System.out.println("Vehiclenew");
        updateMode = false;
        observable.resetVehicleTypeDetails();
        resetVehicleTypeDetails();
        observable.setVehicleTypeData(true);
        btnSecurityVehichle(false);
        btnVehicleSave.setEnabled(true);
        ClientUtil.enableDisable(panVehicleDetails, true);
        btnVehicleMemNo.setEnabled(true);
        txtVehicleMemberName.setEnabled(false);
        txtVehicleMemType.setEnabled(false);
        rdoGahanYesActionPerformed(null);
    }

    private void btnVehicleSaveActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        try {
            if (txtVehicleMemberNum.getText().length() == 0) {
                ClientUtil.showAlertWindow("Member number should not be empty");
            } else {
                int no = 0;
                int surNo = 0;
                boolean flag = false;
                HashMap memMap = new HashMap();
                memMap.put("MEMBER_NO", txtVehicleMemberNum.getText());
                List suretyList = ClientUtil.executeQuery("getMaxSurety", memMap);
                if (suretyList != null && suretyList.size() > 0) {
                    HashMap surMap = (HashMap) suretyList.get(0);
                    surNo = CommonUtil.convertObjToInt(surMap.get("MAXIMUM_SURETY"));
                    List countList = ClientUtil.executeQuery("getnoOfSecurityDetailsofMember", memMap);
                    if (countList != null && countList.size() > 0) {
                        HashMap memCountMap = (HashMap) countList.get(0);
                        no = CommonUtil.convertObjToInt(memCountMap.get("TOT_NOS"));
                    }
                    if (surNo > 0) {
                        if (no >= surNo) {
                            List custList = ClientUtil.executeQuery("getSecurityDetailsofMember", memMap);
                            for (int i = 0; i < custList.size(); i++) {
                                HashMap custMap = (HashMap) custList.get(i);
                                if (custMap.get("CUST_ID").toString().equals(txtCustId.getText().trim())) {
                                    flag = true;
                                    break;
                                }
                            }
                            if (!flag) {
                                ClientUtil.showMessageWindow("This Member already Have Maximum no of Surety!!!!");
                                return;
                            }
                        }
                    }
                }
                updateVehicleTypeFields();
                observable.addVehicleTypeTable(updateTab, updateMode);
                tblVehicleType.setModel(observable.getTblVehicleTypeDetails());
                observable.resetVehicleTypeDetails();
                resetVehicleTypeDetails();
                ClientUtil.enableDisable(panVehicleDetails, false);
                btnSecurityVehichle(false);
                btnVehicleNew.setEnabled(true);
                btnVehicleMemNo.setEnabled(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void btnVehicleDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        String s = CommonUtil.convertObjToStr(tblVehicleType.getValueAt(tblVehicleType.getSelectedRow(), 0));
        observable.deleteVehicleTableData(s, tblVehicleType.getSelectedRow());
        observable.resetVehicleTypeDetails();
        resetVehicleTypeDetails();
        ClientUtil.enableDisable(panVehicleDetails, false);
        btnSecurityVehichle(false);
        btnVehicleNew.setEnabled(true);
    }
    private void btnMemberSaveActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        try {
            if (txtMemNo.getText().length() == 0) {
                ClientUtil.showAlertWindow("Member number should not be empty");
            }else if((CommonUtil.convertObjToStr(txtMemId.getText())).equalsIgnoreCase(CommonUtil.convertObjToStr(txtMemNo.getText()))){// Added by nithya on 22-10-2018 for KD 223 - 0015950: loan application ;If Loanee and Surety is same there is no validation
                ClientUtil.showAlertWindow("Lonee Cannot stand as surety");
            }else {
                int no = 0;
                int surNo = 0;
                int maxLoanNo =0;
                double maxSuretyAmount = 0.0;
                int loanNo=0;
                boolean flag = false;
                HashMap memMap = new HashMap();
                memMap.put("MEMBER_NO", txtMemNo.getText());
                List suretyList = ClientUtil.executeQuery("getMaxSurety", memMap);
                if (suretyList != null && suretyList.size() > 0) {
                    HashMap surMap = (HashMap) suretyList.get(0);
                    surNo = CommonUtil.convertObjToInt(surMap.get("MAXIMUM_SURETY"));
                    if (surMap.containsKey("MAXIMUM_LOAN_PER_SURETY") && surMap.get("MAXIMUM_LOAN_PER_SURETY") != null) {
                        maxLoanNo = CommonUtil.convertObjToInt(surMap.get("MAXIMUM_LOAN_PER_SURETY"));
                    }
                    //Added by nithya for KD-2966
                    if (surMap.containsKey("MAXIMUM_SURETY_AMT") && surMap.get("MAXIMUM_SURETY_AMT") != null) {
                        maxSuretyAmount = CommonUtil.convertObjToInt(surMap.get("MAXIMUM_SURETY_AMT"));
                    }
                    List countList = ClientUtil.executeQuery("getnoOfSecurityDetailsofMember", memMap);
                    if (countList != null && countList.size() > 0) {
                        HashMap memCountMap = (HashMap) countList.get(0);
                        no = CommonUtil.convertObjToInt(memCountMap.get("TOT_NOS"));
                    }
                    if (surNo > 0) {
                        if (no >= surNo) {
                            List custList = ClientUtil.executeQuery("getSecurityDetailsofMember", memMap);
                            for (int i = 0; i < custList.size(); i++) {
                                HashMap custMap = (HashMap) custList.get(i);
                                if (custMap.get("CUST_ID").toString().equals(txtCustId.getText().trim())) {
                                    flag = true;
                                    break;
                                }
                            }
                            if (!flag) {
                                ClientUtil.showMessageWindow("This Member already Have Maximum no of Surety!!!!");
                                return;
                            }
                        }
                    }
                    List lonCountList = ClientUtil.executeQuery("getMaxNumberOfLoaneeOfMember", memMap);
                    if (lonCountList != null && lonCountList.size() > 0) {
                        HashMap loanCountMap = (HashMap) lonCountList.get(0);
                        loanNo = CommonUtil.convertObjToInt(loanCountMap.get("TOT_NO_LOAN"));
                        if (maxLoanNo > 0 && loanNo > 0 && loanNo >= maxLoanNo) {
                            ClientUtil.showMessageWindow("This Member already Stand  surety For Maximum no of Loan !!!!");
                            return;
                        }
                    }
                    
                    List loanSuertyAmtLst = ClientUtil.executeQuery("getTotAmtSetAsSuretyForMember", memMap);
                    if(loanSuertyAmtLst != null && loanSuertyAmtLst.size() > 0){
                        HashMap suretyAmtMap = (HashMap)loanSuertyAmtLst.get(0);
                        double totSuretyAmt = CommonUtil.convertObjToDouble(suretyAmtMap.get("TOT_SURETY_AMT"));
                        double networthGiven = CommonUtil.convertObjToDouble(txtMemNetworth.getText());
                        if(maxSuretyAmount > 0  && (totSuretyAmt + networthGiven) > maxSuretyAmount){
                           ClientUtil.showMessageWindow("The surety amount exceeds maximum surety amount Rs."+maxSuretyAmount+"/-for this member !!!"
                                   + "\nThis Member Stand Surety of Amt Rs. " + totSuretyAmt + "/-");
                           txtMemNetworth.setText("");
                           return;
                        }
                    }
                    
                    
                }
         
                 //added by rishad 10/05/2016 
                if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")) {
                    HashMap eligMap = new HashMap();
                    double totSalary = CommonUtil.convertObjToDouble(txtMemberTotalSalary.getText());
                    eligMap.put("SALARY", totSalary);
                    eligMap.put("MEMBER_NO", txtMemNo.getText());
                    eligMap.put("BORROWMEMNO", txtMemId.getText());
                    eligMap.put("APPLIED_AMT", CommonUtil.convertObjToDouble(txtLoanAmt.getText()));
                    eligMap.put("PROD_ID", ((ComboBoxModel) cboSchemName.getModel()).getKeyForSelected().toString());
                    if (chkSettlement.isSelected()) {
                        eligMap.put("SETTILEMENT", "RENEW");
                    } else {
                        eligMap.put("SETTILEMENT", "NEW");
                    }
                    List eligibleList = ClientUtil.executeQuery("getSuretyEligibleAmount", eligMap);
                    if (eligibleList.size() > 0 && eligibleList != null) {
                        HashMap resultMap = new HashMap();
                        resultMap = (HashMap) eligibleList.get(0);
                        String displayStr = "";
                        displayStr += "Existing Loan Balance as Surety  : " + resultMap.get("SURETYSTANDBALOS") + "\n"
                                + "EligbleAmount                     : Rs " + resultMap.get("ELIGIBLEAMT") + "\n"
                                + "Salary NetWorth                    : Rs " + resultMap.get("NETWORTH") + "\n"
                                + "Share ToBeRecovered                    : Rs " + resultMap.get("TOBERECOVERED") + "\n";

                        if (!displayStr.equals("")) {
                            ClientUtil.showMessageWindow("" + displayStr);
                        }
                       // double actualeligibleamt=CommonUtil.convertObjToDouble(txtLoanAmts.getText());
                        double elgibleamount = CommonUtil.convertObjToDouble(resultMap.get("ELIGIBLEAMT"));
                        if (elgibleamount < 0) {
                            ClientUtil.showMessageWindow("Surety Not Eligible For Loan");
                            txtMemberTotalSalary.setText("");
                            return;
                        }
                    } else {
                       ClientUtil.showMessageWindow("No Data Found");
                        return;
                    }
                }
                updateMemberTypeFields();
                observable.addMemberTypeTable(updateTab, updateMode);
                tblMemberType.setModel(observable.getTblMemberTypeDetails());
                observable.resetMemberTypeDetails();
                resetMemberTypeDetails();
                ClientUtil.enableDisable(panMemberDetails, false);
                btnSecurityMember(false);
                btnMemberNew.setEnabled(true);
                btnMemNo.setEnabled(false);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void btnMemberDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        String s = CommonUtil.convertObjToStr(tblMemberType.getValueAt(tblMemberType.getSelectedRow(), 0));
        observable.deleteMemberTableData(s, tblMemberType.getSelectedRow());
        observable.resetMemberTypeDetails();
        resetMemberTypeDetails();
        ClientUtil.enableDisable(panMemberDetails, false);
        btnSecurityMember(false);
        btnMemberNew.setEnabled(true);
    }
 
    private void tblCollateralMousePressed(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        //        updateCollateralFields();
        //System.out.println("fjhg,jhkhkljkl");
        updateMode = true;
        updateTab = tblCollateral.getSelectedRow();
        observable.setCollateralTypeData(false);        
        String st = CommonUtil.convertObjToStr(tblCollateral.getValueAt(tblCollateral.getSelectedRow(), 0));
        observable.populateCollateralDetails(st + "_" + (updateTab + 1));
        populateCollateralFields();
        collateralJointAccountDisplay(txtOwnerMemNo.getText());
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT
                || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW
                || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
            btnSecurityCollateral(false);
            ClientUtil.enableDisable(panCollatetalDetails, false);
        } else {
            btnSecurityCollateral(true);
            ClientUtil.enableDisable(panCollatetalDetails, true);
            btnCollateralNew.setEnabled(false);
        }
        txtOwnerMemNo.setEnabled(false);
        txtOwnerMemberNname.setEnabled(false);
        btnCollateralNew.setEnabled(true);
        observable.setOldSurvyNo(CommonUtil.convertObjToStr(tblCollateral.getValueAt(tblCollateral.getSelectedRow(), 4)));
    }

    private void rdoGahanYesActionPerformed(java.awt.event.ActionEvent evt) {
        //System.out.println("rdoGahanYesActionPerformed   ");
        if (rdoGahanYes.isSelected()) {
            ClientUtil.enableDisable(panCollatetalDetails, false);
            btnDocumentNo.setEnabled(true);
            txtDocumentNo.setEnabled(true);
            btnOwnerMemNo.setEnabled(false);
            panGahanYesNo.setEnabled(true);
            txtPledgeAmount.setEnabled(true);
            ClientUtil.enableDisable(panGahanYesNo, true);
            //Added By Suresh
            txtOwnerMemNo.setEnabled(true);
            btnOwnerMemNo.setEnabled(true);
            resetCollateralDetails();
        } else {
            btnDocumentNo.setEnabled(false);
            btnOwnerMemNo.setEnabled(true);
            ClientUtil.enableDisable(panCollatetalDetails, true);
        }

    }

    private void rdoGahanNoActionPerformed(java.awt.event.ActionEvent evt) {
        //System.out.println("rdoGahanNoActionPerformed   ");
        if (rdoGahanNo.isSelected()) {
            //Commented By Suresh Peringandoor Branch (30-08-2013)
//            if (tblCollateral.getRowCount() != 0) {
//                ClientUtil.displayAlert("Delete All Gahan Records From the Table then Select Gahan No Option");
//                rdoGahanYes.setSelected(true);
//                return;
//            }
            ClientUtil.enableDisable(panCollatetalDetails, true);
            btnDocumentNo.setEnabled(false);
            btnOwnerMemNo.setEnabled(true);
            ClientUtil.enableDisable(panGahanYesNo, true);
            resetCollateralDetails();
            //              panGahanYesNo.setEnabled(true);
        } else {
            btnDocumentNo.setEnabled(true);
            txtDocumentNo.setEnabled(true);
            btnOwnerMemNo.setEnabled(false);
            ClientUtil.enableDisable(panCollatetalDetails, false);
        }

    }

    private void txtDocumentNoFocusLost(java.awt.event.FocusEvent evt) {
        HashMap docMap = new HashMap();
        String docNo = CommonUtil.convertObjToStr(txtDocumentNo.getText());
        docMap.put("DOCUMENT_NUMBER", docNo);
        if (rdoGahanYes.isSelected()) {
            List lst = ClientUtil.executeQuery("getGahanDetailsforLoan", docMap);
            if (lst != null && lst.size() > 0) {
                viewType = "DOCUMENT_NO";
                HashMap resultDocument = (HashMap) lst.get(0);
                fillData(resultDocument);
            } else {
                ClientUtil.displayAlert("Invalid Document No");
                return;
            }
        } else if (rdoGahanNo.isSelected()) {
            List lst = ClientUtil.executeQuery("getGahanAvailableOrNot", docMap);
            if (lst != null && lst.size() > 0) {
                ClientUtil.displayAlert("Document Number already available in Gahan Details" + "\n" + "Enter different document no");
            }
            return;
        }
    }

    private void btnDocumentNoActionPerformed(java.awt.event.ActionEvent evt) {
        //System.out.println("btnDocumentNoActionPerformed   ");
        popUp("DOCUMENT_NO");
    }

    private void txtPledgeAmountFocusLost(java.awt.event.FocusEvent evt) {
        if (rdoGahanYes.isSelected()) {

            HashMap resultMap = observable.validatePledgeAmount(CommonUtil.convertObjToStr(txtDocumentNo.getText()), CommonUtil.convertObjToDouble(txtPledgeAmount.getText()).doubleValue());
            if (resultMap != null && resultMap.size() > 0) {
                ClientUtil.displayAlert("Pledge Amount Should not Exceed available Secuirty value");
                txtPledgeAmount.setText(CommonUtil.convertObjToStr(resultMap.get("PLEDGE_AMT")));
            }
        }

    }

    private void btnCollateralNewActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        updateMode = false;
        observable.setCollateralTypeData(true);
        btnSecurityCollateral(false);
        btnCollateralSave.setEnabled(true);
        //        ClientUtil.enableDisable(panCollatetalDetails,true);
        if (rdoGahanYes.isSelected()) {
            rdoGahanYesActionPerformed(null);
        }
        if (rdoGahanNo.isSelected()) {
            rdoGahanNoActionPerformed(null);
            btnOwnerMemNo.setEnabled(true);
        }
        ClientUtil.enableDisable(panGahanYesNo, true);
        txtOwnerMemberNname.setEnabled(false);
        btnOwnerMemNo.setEnabled(true);
        observable.setOldSurvyNo("");
        updateTab = -1;
    }

    private void btnCollateralSaveActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        try {
            // System.out.println("updateTab====="+updateTab);
            if (txtOwnerMemNo.getText().length() == 0) {
                ClientUtil.showAlertWindow("OwnerMember Number should not be empty");
            } else if (txtSurveyNo.getText().length() == 0) {
                ClientUtil.showAlertWindow("Survey Number should not be empty");
            } else {
                if (observable.getOldSurvyNo() == null || observable.getOldSurvyNo().equals("")) {
                    //System.out.println("getOldSurvyNo");
                    observable.setOldSurvyNo(txtSurveyNo.getText().toString());
                }
                updateCollateralFields();
                rowCount = tblCollateral.getRowCount();
                if (tblCollateral.getSelectedRow() >= 0 && (btnCollateralNew.isEnabled())) {
                    //System.out.println("kiiiii");
                    rowCount = tblCollateral.getSelectedRow() + 1;
                } else {
                   // System.out.println("miiiii");
                    if (rowCount == 0) {
                        rowCount = 1;
                    } else {
                        rowCount = rowCount + 1;
                    }
                }
                //.out.println("rowCount====="+rowCount);
                observable.setRowCoun(rowCount);
                observable.addCollateralTable(updateTab, updateMode);
                tblCollateral.setModel(observable.getTblCollateralDetails());
                observable.resetCollateralDetails();
                resetCollateralDetails();
                ClientUtil.enableDisable(panCollatetalDetails, false);
                btnSecurityCollateral(false);
                btnCollateralNew.setEnabled(true);
                btnOwnerMemNo.setEnabled(false);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void btnCollateralDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        int selRow = tblCollateral.getSelectedRow();
        String s = CommonUtil.convertObjToStr(tblCollateral.getValueAt(tblCollateral.getSelectedRow(), 0));
        observable.deleteCollateralTableData(s + "_" + (selRow + 1), tblCollateral.getSelectedRow());
        updateTab = -1;
        observable.resetCollateralDetails();
        resetCollateralDetails();
        ClientUtil.enableDisable(panCollatetalDetails, false);
        btnSecurityCollateral(false);
        btnCollateralNew.setEnabled(true);
    }

    private void txtOwnerMemNoFocusLost(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        if (txtOwnerMemNo.getText().length() > 0) {
            HashMap listMap = new HashMap();
            listMap.put("MEMBERSHIP_NO", txtOwnerMemNo.getText());
            java.util.List lst = ClientUtil.executeQuery("getMemeberShipDetails", listMap);
            if (lst != null && lst.size() > 0) {
                listMap = (HashMap) lst.get(0);
                viewType = "OWNER_MEMBER_NO";
                fillData(listMap);
            } else {
                ClientUtil.showAlertWindow("Invalid Member No");
                resetCollateralDetails();
                observable.setTxtOwnerMemNo("");
            }
        }
    }

    private void btnOwnerMemNoActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        if (rdoGahanYes.isSelected()) {
            popUp("OWNER_MEMBER_NO_GAHAN");
        } else {
            popUp("OWNER_MEMBER_NO");
        }
    }

    private void txtMemNoFocusLost(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        if (txtMemNo.getText().length() > 0) {
            boolean isClosedShare = checkClosedShare(txtMemNo.getText());
            if(isClosedShare){
                ClientUtil.showAlertWindow("Share Closed");
                resetMemberTypeDetails();
                observable.setTxtMemNo("");
                return;
            }
            HashMap listMap = new HashMap();
            listMap.put("MEMBERSHIP_NO", txtMemNo.getText());
            java.util.List lst = ClientUtil.executeQuery("getMemeberShipDetails", listMap);
            if (lst != null && lst.size() > 0) {
                listMap = (HashMap) lst.get(0);
                viewType = "SUB_MEMBER_NO_FOCUS_LOST";
                fillData(listMap);
            } else {
                ClientUtil.showAlertWindow("Invalid Member No");
                resetMemberTypeDetails();
                observable.setTxtMemNo("");
            }
        }
    }
    
    // Added by nithya on 23-02-2019 for KD 396 - Issue in share -while taking as security for loan
    private boolean checkClosedShare(String shareAcctNo){
        boolean isClosedShare = false;
        HashMap statusMap = new HashMap();
        statusMap.put("MEMBERSHIP_NO", shareAcctNo);
        List statusLst = ClientUtil.executeQuery("getShareAcctClosedStatus", statusMap);
        if(statusLst != null && statusLst.size() > 0){
          statusMap = (HashMap)statusLst.get(0);
          if(statusMap.containsKey("ACCT_STATUS") && statusMap.get("ACCT_STATUS") != null){
              String shareStatus = "";
              if(CommonUtil.convertObjToStr(statusMap.get("ACCT_STATUS")).equalsIgnoreCase("CLOSED")){
                  isClosedShare = true;
              }
          }
        }
        return isClosedShare;
    }

    private void btnDepositNewActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        updateMode = false;
        observable.setDepositTypeData(true);
        enableDisableDepositPanButton(false);
        btnDepositSave.setEnabled(true);
        ClientUtil.enableDisable(panDepositType, false);
        btnDepNo.setEnabled(true);
        cboProductTypeSecurity.setEnabled(true);
        cboDepProdType.setEnabled(true);
    }

    private void btnDepositSaveActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            HashMap whereMap = new HashMap();
            whereMap.put("DEPOSIT_NO", txtDepNo.getText());
            List recordList = ClientUtil.executeQuery("checkDepositNoAlreadyinLoansDeposit", whereMap);
            if ((recordList != null && recordList.size() > 0) && !updateMode) {
                ClientUtil.showMessageWindow("This Deposit has Already been Used as Security !!!");
            } else {
                updateDepositTypeFields();
                observable.addDepositTypeTable(updateTab, updateMode);
                tblDepositDetails.setModel(observable.getTblDepositTypeDetails());
                observable.resetDepositTypeDetails();
            }
            resetDepositTypeDetails();
            ClientUtil.enableDisable(panDepositType, false);
            enableDisableDepositPanButton(false);
            btnDepositNew.setEnabled(true);
            btnDepNo.setEnabled(false);
            calculateTot();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void btnDepositDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        String s = CommonUtil.convertObjToStr(tblDepositDetails.getValueAt(tblDepositDetails.getSelectedRow(), 1));
        observable.deleteDepositTableData(s, tblDepositDetails.getSelectedRow());
        observable.resetDepositTypeDetails();
        resetDepositTypeDetails();
        ClientUtil.enableDisable(panDepositType, false);
        enableDisableDepositPanButton(false);
        btnDepositNew.setEnabled(true);
        btnDepNo.setEnabled(false);
        calculateTot();
        if (tblDepositDetails.getRowCount() == 0) {
            lblTotalDepositValue.setText(CurrencyValidation.formatCrore(String.valueOf("0")));
        }
    }

    public void btnDepNoActionPerformed(java.awt.event.ActionEvent evt) {
        popUp("DEPOSIT_ACC_NO");
    }

    private void cboProductTypeSecurityActionPerformed(java.awt.event.ActionEvent evt) {
        if (cboProductTypeSecurity.getSelectedIndex() > 0) {
            String prodType = ((ComboBoxModel) cboProductTypeSecurity.getModel()).getKeyForSelected().toString();
            observable.setTxtCustId(txtCustId.getText());
            observable.setCbmProdTypeSecurity(prodType);
            cboDepProdType.setModel(observable.getCbmDepProdID());
            if (prodType.equals("TD")) {
                lblProductId2.setText("Product Id");
                lblDepNo.setText("Deposit No");
                lblDepDt.setText("Dep Date");
                lblDepAmount.setText("Dep Amount");
                lblMaturityValue.setText("Maturity Value");
                lblMaturityDt.setText("Maturity Date");
                txtRateOfInterest.setVisible(true);
                lblRateOfInterest.setVisible(true);
            } else {
                lblProductId2.setText("Scheme Name");
                lblDepNo.setText("Chittal No");
                lblDepDt.setText("Scheme StartDt");
                lblDepAmount.setText("Inst Amount");
                lblMaturityValue.setText("Paid Amount");
                lblMaturityDt.setText("Scheme EndDt");
                txtRateOfInterest.setText("");
                txtRateOfInterest.setVisible(false);
                lblRateOfInterest.setVisible(false);
            }
        }

    }

    private void tblDepositDetailsMousePressed(java.awt.event.MouseEvent evt) {
        updateDepositTypeFields();
        updateMode = true;
        updateTab = tblDepositDetails.getSelectedRow();
        observable.setDepositTypeData(false);
        String depProdType = CommonUtil.convertObjToStr(tblDepositDetails.getValueAt(tblDepositDetails.getSelectedRow(), 0));
        observable.setCbmProdTypeSecurity(depProdType);
        cboDepProdType.setModel(observable.getCbmDepProdID());
        String st = CommonUtil.convertObjToStr(tblDepositDetails.getValueAt(tblDepositDetails.getSelectedRow(), 1));
        observable.populateDepositTypeDetails(st);
        populateDepositTypeFields();
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT
                || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
            enableDisableDepositPanButton(false);
            ClientUtil.enableDisable(panDepositType, false);
        } else {

            btnDepNo.setEnabled(true);
            cboProdType.setEnabled(true);
            cboProdId.setEnabled(true);
            txtDepAmount.setEnabled(false);
            txtRateOfInterest.setEnabled(false);
            txtMaturityValue.setEnabled(false);
            txtMaturityDt.setEnabled(false);
            tdtDepDt.setEnabled(false);
            enableDisableDepositPanButton(true);
            btnDepositNew.setEnabled(false);



        }


    }

    private void btnLosNewActionPerformed(java.awt.event.ActionEvent evt) {
        updateLosMode = false;
        observable.setLosTypeData(true);
        enableDisableLosPanButton(false);
        btnLosSave.setEnabled(true);
        enabledesableLOS(true);
        //        ClientUtil.enableDisable(panLType,false);

    }

    private void btnLosSaveActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            HashMap whereMap = new HashMap();
            whereMap.put("SECURITY_NO", txtLosSecurityNo.getText());
            List recordList = ClientUtil.executeQuery("checkDepositNoAlreadyinLoansLOS", whereMap);
            if ((recordList != null && recordList.size() > 0) && !updateMode) {
                ClientUtil.showMessageWindow("This Deposit has Already been Used as Security !!!");
            } else {
                updateLosTypeFields();
                observable.addLosTypeTable(updateTab, updateLosMode);
                tblLosDetails.setModel(observable.getTblLosTypeDetails());
                observable.resetLosTypeDetails();
            }
            resetLosTypeDetails();
            ClientUtil.enableDisable(panLosDetails, false);
            enableDisableLosPanButton(false);
            btnLosNew.setEnabled(true);
            //                btnDepNo.setEnabled(false);
            //                calculateTot();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void btnLosDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        String s = CommonUtil.convertObjToStr(tblLosDetails.getValueAt(tblLosDetails.getSelectedRow(), 2));
        observable.deleteLosTableData(s, tblLosDetails.getSelectedRow());
        observable.resetLosTypeDetails();
        resetLosTypeDetails();
        ClientUtil.enableDisable(panLosDetails, false);
        enableDisableLosPanButton(false);
        btnLosNew.setEnabled(true);

    }

    private void tblLosDetailsMousePressed(java.awt.event.MouseEvent evt) {
        updateLosTypeFields();
        updateMode = true;
        updateTab = tblLosDetails.getSelectedRow();
        observable.setLosTypeData(false);
        String otherInstitution = CommonUtil.convertObjToStr(tblLosDetails.getValueAt(tblLosDetails.getSelectedRow(), 0));
        //             observable.setCbmProdTypeSecurity(otherInstitution);
        //             cboDepProdType.setModel(observable.getCbmDepProdID());
        cboLosOtherInstitution.setSelectedItem(otherInstitution);
        String st = CommonUtil.convertObjToStr(tblLosDetails.getValueAt(tblLosDetails.getSelectedRow(), 2));
        observable.populateLosTypeDetails(st);
        populateLosTypeFields();
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT
                || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
            enableDisableLosPanButton(false);
        } else {
            enableDisableLosPanButton(true);
            btnLosNew.setEnabled(false);
        }
        //    ClientUtil.enableDisable(panLosDetails,false);


    }

    private void resetSalaryDetails() {
        txtSalaryCertificateNo.setText("");
        txtEmployerName.setText("");
        txtAddress.setText("");
        cboCity.setSelectedItem("");
        txtPinCode.setText("");
        txtDesignation.setText("");
        txtContactNo.setText("");
        tdtRetirementDt.setDateValue("");
        txtMemberNum.setText("");
        txtTotalSalary.setText("");
        txtNetWorth1.setText("");
        txtSalaryRemark.setText("");
        txtMemberName.setText("");

    }

    private void enableDisableSalaryDetails(boolean flag) {
        ClientUtil.enableDisable(panSalaryDetails, flag);
    }

    private void enableDisableSalaryBtns(boolean flag) {
        btnSalaryNew.setEnabled(flag);
        btnSalarySave.setEnabled(flag);
        btnSalaryDelete.setEnabled(flag);
    }

    private void updateSalaryOBFields() {
        observable.setTxtSalaryCertificateNo(txtSalaryCertificateNo.getText());
        observable.setTxtEmployerName(txtEmployerName.getText());
        observable.setTxtAddress(txtAddress.getText());
        observable.setCboSecurityCity(CommonUtil.convertObjToStr(cboCity.getSelectedItem()));
        observable.setTxtPinCode(txtPinCode.getText());
        observable.setTxtDesignation(txtDesignation.getText());
        observable.setTxtContactNo(txtContactNo.getText());
        observable.setTdtRetirementDt(tdtRetirementDt.getDateValue());
        observable.setTxtMemberNum(txtMemberNum.getText());
        observable.setTxtTotalSalary(txtTotalSalary.getText());
        observable.setTxtNetWorth(txtNetWorth1.getText());
        observable.setTxtSalaryRemark(txtSalaryRemark.getText());
    }

    private void enableDisableSalaryBtnsNew(boolean isNewEnable) {

        if (isNewEnable) {
            btnSalaryNew.setEnabled(isNewEnable);
            btnSalarySave.setEnabled(false);
            btnSalaryDelete.setEnabled(false);
        } else {
            btnSalaryNew.setEnabled(isNewEnable);
            btnSalarySave.setEnabled(true);
            btnSalaryDelete.setEnabled(true);
        }
    }

    private void updateSalaryUI() {
        txtSalaryCertificateNo.setText(observable.getTxtSalaryCertificateNo());
        txtEmployerName.setText(observable.getTxtEmployerName());
        txtAddress.setText(observable.getTxtAddress());
        cboCity.setSelectedItem(observable.getCboSecurityCity());
        txtPinCode.setText(observable.getTxtPinCode());
        txtDesignation.setText(observable.getTxtDesignation());
        txtContactNo.setText(observable.getTxtContactNo());
        tdtRetirementDt.setDateValue(observable.getTdtRetirementDt());
        txtMemberNum.setText(observable.getTxtMemberNum());
        txtTotalSalary.setText(observable.getTxtTotalSalary());
        txtNetWorth1.setText(observable.getTxtNetWorth());
        txtSalaryRemark.setText(observable.getTxtSalaryRemark());
    }

    public void updateMemberTypeFields() {
        observable.setTxtMemNo(txtMemNo.getText());
        observable.setTxtMembName(txtMembName.getText());
        observable.setTxtMemType(txtMemType.getText());
        observable.setTxtContactNum(txtContactNum.getText());
        observable.setTxtMemNetworth(txtMemNetworth.getText());
        observable.setTxtMemPriority(txtMemPriority.getText());
        observable.setTxtMemberTotalSalary(txtMemberTotalSalary.getText());
    }
    

    public void populateMemberTypeFields() {
        txtMemNo.setText(observable.getTxtMemNo());
        txtMembName.setText(observable.getTxtMembName());
        txtMemberTotalSalary.setText(observable.getTxtMemberTotalSalary());
        txtContactNum.setText(observable.getTxtContactNum());
        txtMemNetworth.setText(observable.getTxtMemNetworth());
        txtMemPriority.setText(observable.getTxtMemPriority());
    }
    
    private void btnSecurityMember(boolean flag) {
        btnMemberNew.setEnabled(flag);
        btnMemberSave.setEnabled(flag);
        btnMemberDelete.setEnabled(flag);
    }
    private void btnSecurityVehichle(boolean flag) {
        btnMemberNew.setEnabled(flag);
        btnMemberSave.setEnabled(flag);
        btnMemberDelete.setEnabled(flag);
    }

    private void resetMemberTypeDetails() {
        txtMemNo.setText("");
        txtMembName.setText("");
        txtMemType.setText("");
        txtContactNum.setText("");
        txtMemNetworth.setText("");
        txtMemPriority.setText("");
        lblMemberRetireDate.setText("");
        txtMemberTotalSalary.setText("");
    }
    public void populateVehicleTypeFields() {
       txtVehicleMemberNum.setText(observable.getTxtVehicleMemNo());
        txtVehicleMemberName.setText(observable.getTxtVehicleMembName());
        txtVehicleMemType.setText(observable.getTxtVehicleMemType());
        txtVehicleContactNum.setText(observable.getTxtVehicleContactNum());
        txtVehicleNo.setText(observable.getTxtVehicleNo());
        txtVehicleRcBookNo.setText(observable.getTxtVehicleRcBookNo());
        txtVehicleType.setText(observable.getTxtVehicleType());
        txtVehicleDate.setDateValue(observable.getTxtVehicleDate());
        txtVehicleDetals.setText(observable.getTxtVehicleDetails());
        txtVehicleMemSal.setText(CommonUtil.convertObjToStr(observable.getTxtVehicleMemSal()));
        txtVehicleNetWorth.setText(CommonUtil.convertObjToStr(observable.getTxtVehicleNetworth()));
                
    }
    public void updateVehicleTypeFields() {
        observable.setTxtVehicleMemNo(txtVehicleMemberNum.getText());
        observable.setTxtVehicleMembName(txtVehicleMemberName.getText());
        observable.setTxtVehicleMemType(txtVehicleMemType.getText());
        observable.setTxtVehicleContactNum(txtVehicleContactNum.getText());
        observable.setTxtVehicleDetails(txtVehicleDetals.getText());
        observable.setTxtVehicleDate(txtVehicleDate.getDateValue());
        observable.setTxtVehicleType(txtVehicleType.getText());
        observable.setTxtVehicleRcBookNo(txtVehicleRcBookNo.getText());
        observable.setTxtVehicleNo(txtVehicleNo.getText());
        observable.setTxtVehicleMemSal(CommonUtil.convertObjToDouble(txtVehicleMemSal.getText()));
        observable.setTxtVehicleNetworth(CommonUtil.convertObjToDouble(txtVehicleNetWorth.getText()));
    }

 private void resetVehicleTypeDetails() {
        txtVehicleMemberNum.setText("");
        txtVehicleMemberName.setText("");
        txtVehicleMemType.setText("");
        txtVehicleContactNum.setText("");
        txtVehicleNo.setText("");
        lblVehicleMemRetireDate.setText("");
        txtVehicleType.setText("");
        txtVehicleRcBookNo.setText("");
        txtVehicleDate.setDateValue("");
        txtVehicleDetals.setText("");
        txtVehicleMemSal.setText("");
        txtVehicleNetWorth.setText("");         
    }
    public void populateCollateralFields() {
        if (observable.isRdoGahanYes()) {
            rdoGahanYes.setSelected(observable.isRdoGahanYes());
        } else {
            rdoGahanNo.setSelected(observable.isRdoGahanNo());
        }
        txtOwnerMemNo.setText(observable.getTxtOwnerMemNo());
        txtOwnerMemberNname.setText(observable.getTxtOwnerMemberNname());
        txtDocumentNo.setText(observable.getTxtDocumentNo());
        cboDocumentType.setSelectedItem(observable.getCboDocumentType());
        tdtDocumentDate.setDateValue(observable.getTdtDocumentDate());
        txtRegisteredOffice.setText(observable.getTxtRegisteredOffice());
        cboPledge.setSelectedItem(CommonUtil.convertObjToStr(observable.getCboPledge()));
        tdtPledgeDate.setDateValue(observable.getTdtPledgeDate());
        txtPledgeNo.setText(observable.getTxtPledgeNo());
        txtPledgeAmount.setText(observable.getTxtPledgeAmount());
        txtVillage.setText(observable.getTxtVillage());
        txtSurveyNo.setText(observable.getTxtSurveyNo());
        txtTotalArea.setText(observable.getTxtTotalArea());
        cboNature.setSelectedItem(CommonUtil.convertObjToStr(observable.getCboNature()));
        cboRight.setSelectedItem(CommonUtil.convertObjToStr(observable.getCboRight()));
        txtAreaParticular.setText(observable.getTxtAreaParticular());
    }

    private void collateralJointAccountDisplay(String memNo) {
        observable.updateCollateralJointDetails(memNo);
        tblJointCollateral.setModel(observable.getTblJointCollateral());

    }

    private void btnSecurityCollateral(boolean flag) {
        btnCollateralNew.setEnabled(flag);
        btnCollateralSave.setEnabled(flag);
        btnCollateralDelete.setEnabled(flag);
    }

    private void resetCollateralDetails() {
        txtOwnerMemNo.setText("");
        txtOwnerMemberNname.setText("");
        txtDocumentNo.setText("");
        //        txtDocumentType.setText("");
        cboDocumentType.setSelectedItem("");
        tdtDocumentDate.setDateValue("");
        txtRegisteredOffice.setText("");
        cboPledge.setSelectedItem("");
        tdtPledgeDate.setDateValue("");
        txtPledgeNo.setText("");
        txtPledgeAmount.setText("");
        txtVillage.setText("");
        txtSurveyNo.setText("");
        txtTotalArea.setText("");
        cboNature.setSelectedItem("");
        cboRight.setSelectedItem("");
        txtAreaParticular.setText("");
    }

    public void updateCollateralFields() {
        observable.setRdoGahanYes(rdoGahanYes.isSelected());
        observable.setRdoGahanNo(rdoGahanNo.isSelected());
        observable.setTxtOwnerMemNo(txtOwnerMemNo.getText());
        observable.setTxtOwnerMemberNname(txtOwnerMemberNname.getText());
        observable.setTxtDocumentNo(txtDocumentNo.getText());
        //        observable.setTxtDocumentType(txtDocumentType.getText());
        observable.setCboDocumentType(CommonUtil.convertObjToStr(cboDocumentType.getSelectedItem()));
        observable.setTdtDocumentDate(tdtDocumentDate.getDateValue());
        observable.setTxtRegisteredOffice(txtRegisteredOffice.getText());
        observable.setCboPledge(CommonUtil.convertObjToStr(cboPledge.getSelectedItem()));
        observable.setTxtPledgeType(txtPledgeType.getText());
        observable.setTdtPledgeDate(tdtPledgeDate.getDateValue());
        observable.setTxtPledgeNo(txtPledgeNo.getText());
        observable.setTxtPledgeAmount(txtPledgeAmount.getText());
        observable.setTxtVillage(txtVillage.getText());
        observable.setTxtSurveyNo(txtSurveyNo.getText());
        observable.setTxtTotalArea(txtTotalArea.getText());
        observable.setCboNature(CommonUtil.convertObjToStr(cboNature.getSelectedItem()));
        observable.setCboRight(CommonUtil.convertObjToStr(cboRight.getSelectedItem()));
        //        observable.setCboDocumentType(CommonUtil.convertObjToStr(cboDocumentType.getSelectedItem()));
        observable.setTxtAreaParticular(txtAreaParticular.getText());
    }

    private void enableDisableDepositPanButton(boolean flag) {
        btnDepositNew.setEnabled(flag);
        btnDepositSave.setEnabled(flag);
        btnDepositDelete.setEnabled(flag);
    }

    public void updateDepositTypeFields() {

        //      observable.setTxtChittalNo(txtChittalNo.getText());
        //        observable.setTxtSubNo(txtSubNo.getText());
        observable.setTxtDepNo(txtDepNo.getText());
        observable.setCboProductTypeSecurity((String) cboProductTypeSecurity.getSelectedItem());
        observable.setCboDepProdID((String) cboDepProdType.getSelectedItem());
        observable.setTdtDepDt(tdtDepDt.getDateValue());
        observable.setTxtDepAmount(txtDepAmount.getText());
        observable.setTxtMaturityDt(txtMaturityDt.getDateValue());
        observable.setTxtMaturityValue(txtMaturityValue.getText());
        observable.setTxtRateOfInterest(txtRateOfInterest.getText());
    }

    private void resetDepositTypeDetails() {
        txtDepNo.setText("");
        cboProductTypeSecurity.setSelectedItem("");
        cboDepProdType.setSelectedItem("");
        tdtDepDt.setDateValue("");
        txtDepAmount.setText("");
        txtRateOfInterest.setText("");
        txtMaturityValue.setText("");
        txtMaturityDt.setDateValue("");
    }

    public void calculateTot() {
        double totDeposit = 0;
        for (int i = 0; i < tblDepositDetails.getRowCount(); i++) {
            totDeposit = totDeposit + CommonUtil.convertObjToDouble(tblDepositDetails.getValueAt(i, 2).toString()).doubleValue();
            lblTotalDepositValue.setText(CurrencyValidation.formatCrore(String.valueOf(totDeposit)));
        }
        setSizeTableData();
    }

    private void setSizeTableData() {
        tblDepositDetails.getColumnModel().getColumn(0).setPreferredWidth(60);
        tblDepositDetails.getColumnModel().getColumn(1).setPreferredWidth(100);
        tblDepositDetails.getColumnModel().getColumn(2).setPreferredWidth(70);
        tblDepositDetails.getColumnModel().getColumn(3).setPreferredWidth(70);
    }

    public void populateDepositTypeFields() {
        txtDepNo.setText(observable.getTxtDepNo());
        //        txtProductId.setText(observable.getTxtProductId());
        cboProductTypeSecurity.setSelectedItem(observable.getCboProductTypeSecurity());
        cboDepProdType.setSelectedItem(observable.getCboDepProdID());
        tdtDepDt.setDateValue(observable.getTdtDepDt());
        txtDepAmount.setText(observable.getTxtDepAmount());
        txtRateOfInterest.setText(observable.getTxtRateOfInterest());
        txtMaturityValue.setText(observable.getTxtMaturityValue());
        txtMaturityDt.setDateValue(observable.getTxtMaturityDt());
    }

    private void enableDisableLosPanButton(boolean flag) {
        btnLosNew.setEnabled(flag);
        btnLosSave.setEnabled(flag);
        btnLosDelete.setEnabled(flag);
    }

    public void enabledesableLOS(boolean filag) {
        cboLosOtherInstitution.setEnabled(filag);
        txtLosName.setEnabled(filag);
        cboLosSecurityType.setEnabled(filag);
        txtLosSecurityNo.setEnabled(filag);
        txtLosAmount.setEnabled(filag);
        tdtLosIssueDate.setEnabled(filag);
        tdtLosMaturityDt.setEnabled(filag);
        txtLosMaturityValue.setEnabled(filag);
        txtLosRemarks.setEnabled(filag);
        txtLosRemarks.setEnabled(filag);
    }

    public void updateLosTypeFields() {

        //      observable.setTxtChittalNo(txtChittalNo.getText());
        //        observable.setTxtSubNo(txtSubNo.getText());
        observable.setTxtDepNo(txtDepNo.getText());
        observable.setCboLosOtherInstitution((String) cboLosOtherInstitution.getSelectedItem());
        observable.setCboLosSecurityType((String) cboLosSecurityType.getSelectedItem());
        observable.setTxtLosAmount(txtLosAmount.getText());
        observable.setTxtLosMaturityValue(txtLosMaturityValue.getText());
        observable.setTxtLosName(txtLosName.getText());
        observable.setTxtLosSecurityNo(txtLosSecurityNo.getText());
        observable.setTxtLosRemarks(txtLosRemarks.getText());
        observable.setTdtLosIssueDate(tdtLosIssueDate.getDateValue());
        observable.setTdtLosMaturityDt(tdtLosMaturityDt.getDateValue());
    }

    private void resetLosTypeDetails() {
        txtLosAmount.setText("");
        cboLosOtherInstitution.setSelectedItem("");
        cboLosSecurityType.setSelectedItem("");
        tdtLosIssueDate.setDateValue("");
        txtLosRemarks.setText("");
        txtLosName.setText("");
        txtLosMaturityValue.setText("");
        tdtLosMaturityDt.setDateValue("");
        txtLosSecurityNo.setText("");
    }

    public void populateLosTypeFields() {
        txtLosSecurityNo.setText(observable.getTxtLosSecurityNo());
        //        txtProductId.setText(observable.getTxtProductId());
        cboLosOtherInstitution.setSelectedItem(observable.getCboLosOtherInstitution());
        cboLosSecurityType.setSelectedItem(observable.getCboLosSecurityType());
        tdtLosIssueDate.setDateValue(observable.getTdtLosIssueDate());
        txtLosMaturityValue.setText(observable.getTxtLosMaturityValue());
        txtLosAmount.setText(observable.getTxtLosAmount());
        txtLosName.setText(observable.getTxtLosName());
        tdtLosMaturityDt.setDateValue(observable.getTdtLosMaturityDt());
        txtLosRemarks.setText(observable.getTxtLosRemarks());
    }

    //security end..
    public void setHelpMessage() {
    }

    /**
     * ********** END OF NEW METHODS **************
     */
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblServicePeriod = new com.see.truetransact.uicomponent.CLabel();
        panLoanApplication = new com.see.truetransact.uicomponent.CPanel();
        tabLoan = new com.see.truetransact.uicomponent.CTabbedPane();
        panLoan = new com.see.truetransact.uicomponent.CPanel();
        panTrans = new com.see.truetransact.uicomponent.CPanel();
        btnScheduler = new com.see.truetransact.uicomponent.CPanel();
        txtCustId = new javax.swing.JTextField();
        lblApplDate = new com.see.truetransact.uicomponent.CLabel();
        lblMembrName = new javax.swing.JLabel();
        lblApplNo = new com.see.truetransact.uicomponent.CLabel();
        lblTotalTransactionAmt = new com.see.truetransact.uicomponent.CLabel();
        txtApplNo = new javax.swing.JTextField();
        lblMembrId = new javax.swing.JLabel();
        tdtApplDt = new com.see.truetransact.uicomponent.CDateField();
        lblApplInwardDate = new com.see.truetransact.uicomponent.CLabel();
        btnSearch = new com.see.truetransact.uicomponent.CButton();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        lblSchemName = new javax.swing.JLabel();
        lblRegStatus = new javax.swing.JLabel();
        lblLoanAmt = new javax.swing.JLabel();
        tdtApplInwrdDt = new com.see.truetransact.uicomponent.CDateField();
        lblTotalTransactionAmtVal = new com.see.truetransact.uicomponent.CLabel();
        txtMemId = new javax.swing.JTextField();
        cboSchemName = new com.see.truetransact.uicomponent.CComboBox();
        cboRegstrStatus = new com.see.truetransact.uicomponent.CComboBox();
        panRemarks = new com.see.truetransact.uicomponent.CPanel();
        scrRemarks = new com.see.truetransact.uicomponent.CScrollPane();
        txaRemarks = new com.see.truetransact.uicomponent.CTextArea();
        lblCustId = new com.see.truetransact.uicomponent.CLabel();
        txtMemName = new com.see.truetransact.uicomponent.CTextField();
        txtLoanAmt = new com.see.truetransact.uicomponent.CTextField();
        jPanel2 = new com.see.truetransact.uicomponent.CPanel();
        jLabel1 = new com.see.truetransact.uicomponent.CLabel();
        txtNextAccNo = new com.see.truetransact.uicomponent.CTextField();
        btnPrintCheck = new com.see.truetransact.uicomponent.CButton();
        chkLoaneeSecurityonly = new com.see.truetransact.uicomponent.CCheckBox();
        lblServiceTax = new com.see.truetransact.uicomponent.CLabel();
        lblServiceTaxval = new com.see.truetransact.uicomponent.CLabel();
        chkSettlement = new com.see.truetransact.uicomponent.CCheckBox();
        btnMemLiability = new com.see.truetransact.uicomponent.CButton();
        lblDob = new com.see.truetransact.uicomponent.CLabel();
        lblDobValue = new com.see.truetransact.uicomponent.CLabel();
        lblRetireMent = new com.see.truetransact.uicomponent.CLabel();
        lblRetireDate = new com.see.truetransact.uicomponent.CLabel();
        cLabel2 = new com.see.truetransact.uicomponent.CLabel();
        txtSalaryField = new com.see.truetransact.uicomponent.CTextField();
        txtCostOfVehicleField = new com.see.truetransact.uicomponent.CTextField();
        lblCostOfVehicle = new com.see.truetransact.uicomponent.CLabel();
        panChrg = new javax.swing.JPanel();
        panChargeDetails = new com.see.truetransact.uicomponent.CPanel();
        panCaste = new com.see.truetransact.uicomponent.CPanel();
        lblCaste = new com.see.truetransact.uicomponent.CLabel();
        txtCaste = new com.see.truetransact.uicomponent.CTextField();
        lblPurposeCode = new com.see.truetransact.uicomponent.CLabel();
        cboPurposeCode = new com.see.truetransact.uicomponent.CComboBox();
        lblServicePeriodValue = new com.see.truetransact.uicomponent.CLabel();
        lblSeviceperiod = new com.see.truetransact.uicomponent.CLabel();
        panSecurityDetails = new com.see.truetransact.uicomponent.CPanel();
        panRepayment = new com.see.truetransact.uicomponent.CPanel();
        panRepaymentDetails = new com.see.truetransact.uicomponent.CPanel();
        panCalacData = new com.see.truetransact.uicomponent.CPanel();
        lblLoanAmts = new javax.swing.JLabel();
        txtLoanAmts = new com.see.truetransact.uicomponent.CTextField();
        lblRepayType = new com.see.truetransact.uicomponent.CLabel();
        cboRepayType = new com.see.truetransact.uicomponent.CComboBox();
        lblRepayFreq_Repayment = new com.see.truetransact.uicomponent.CLabel();
        cboRepayFreq_Repayment = new com.see.truetransact.uicomponent.CComboBox();
        lblFromDt = new com.see.truetransact.uicomponent.CLabel();
        tdtFromDt = new com.see.truetransact.uicomponent.CDateField();
        lblMoratorium_Given = new com.see.truetransact.uicomponent.CLabel();
        chkMoratorium_Given = new com.see.truetransact.uicomponent.CCheckBox();
        lblFacility_Moratorium_Period = new com.see.truetransact.uicomponent.CLabel();
        txtFacility_Moratorium_Period = new com.see.truetransact.uicomponent.CTextField();
        lblNoInstall = new com.see.truetransact.uicomponent.CLabel();
        txtNoInstall = new com.see.truetransact.uicomponent.CTextField();
        lblDueDt = new com.see.truetransact.uicomponent.CLabel();
        tdtDueDt = new com.see.truetransact.uicomponent.CDateField();
        txtInstallAmount = new com.see.truetransact.uicomponent.CTextField();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        panSchedule = new com.see.truetransact.uicomponent.CPanel();
        btnSchedule = new com.see.truetransact.uicomponent.CButton();
        tbrLoanAppl = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace56 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace57 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace58 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace59 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace60 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace61 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        lblSpace62 = new com.see.truetransact.uicomponent.CLabel();
        btnDateChange = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrCustomer = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptNew = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptSave = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        mitClose = new javax.swing.JMenuItem();

        lblServicePeriod.setText("ServicePeriod :");

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setMaximumSize(new java.awt.Dimension(900, 663));
        setMinimumSize(new java.awt.Dimension(900, 663));
        setPreferredSize(new java.awt.Dimension(900, 663));

        panLoanApplication.setMaximumSize(new java.awt.Dimension(1000, 850));
        panLoanApplication.setMinimumSize(new java.awt.Dimension(1000, 850));
        panLoanApplication.setPreferredSize(new java.awt.Dimension(1000, 850));
        panLoanApplication.setLayout(new java.awt.GridBagLayout());

        tabLoan.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        tabLoan.setMinimumSize(new java.awt.Dimension(1000, 850));
        tabLoan.setPreferredSize(new java.awt.Dimension(1000, 850));

        panLoan.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panLoan.setEnabled(false);
        panLoan.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        panLoan.setMinimumSize(new java.awt.Dimension(880, 950));
        panLoan.setName("panMaritalStatus"); // NOI18N
        panLoan.setPreferredSize(new java.awt.Dimension(880, 900));
        panLoan.setLayout(new java.awt.GridBagLayout());

        panTrans.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panTrans.setMaximumSize(new java.awt.Dimension(870, 230));
        panTrans.setMinimumSize(new java.awt.Dimension(870, 230));
        panTrans.setPreferredSize(new java.awt.Dimension(870, 230));
        panTrans.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 120, 5);
        panLoan.add(panTrans, gridBagConstraints);

        btnScheduler.setMaximumSize(new java.awt.Dimension(950, 150));
        btnScheduler.setMinimumSize(new java.awt.Dimension(950, 150));
        btnScheduler.setPreferredSize(new java.awt.Dimension(950, 150));
        btnScheduler.setLayout(new java.awt.GridBagLayout());

        txtCustId.setMaximumSize(new java.awt.Dimension(110, 21));
        txtCustId.setMinimumSize(new java.awt.Dimension(110, 21));
        txtCustId.setPreferredSize(new java.awt.Dimension(110, 21));
        txtCustId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCustIdActionPerformed(evt);
            }
        });
        txtCustId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCustIdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 3, 3, 3);
        btnScheduler.add(txtCustId, gridBagConstraints);

        lblApplDate.setText("Application Date");
        lblApplDate.setToolTipText("Application Date");
        lblApplDate.setFont(new java.awt.Font("DejaVu Sans", 0, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 50, 3, 3);
        btnScheduler.add(lblApplDate, gridBagConstraints);

        lblMembrName.setFont(new java.awt.Font("DejaVu Sans", 0, 12)); // NOI18N
        lblMembrName.setText("Member Name");
        lblMembrName.setToolTipText("Member Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        btnScheduler.add(lblMembrName, gridBagConstraints);

        lblApplNo.setText("Application No");
        lblApplNo.setToolTipText("Application No");
        lblApplNo.setFont(new java.awt.Font("DejaVu Sans", 0, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        btnScheduler.add(lblApplNo, gridBagConstraints);

        lblTotalTransactionAmt.setText("Total Charge Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 50, 3, 3);
        btnScheduler.add(lblTotalTransactionAmt, gridBagConstraints);

        txtApplNo.setMaximumSize(new java.awt.Dimension(110, 21));
        txtApplNo.setMinimumSize(new java.awt.Dimension(110, 21));
        txtApplNo.setPreferredSize(new java.awt.Dimension(110, 21));
        txtApplNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtApplNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        btnScheduler.add(txtApplNo, gridBagConstraints);

        lblMembrId.setFont(new java.awt.Font("DejaVu Sans", 0, 12)); // NOI18N
        lblMembrId.setText("Member ID");
        lblMembrId.setMaximumSize(new java.awt.Dimension(84, 17));
        lblMembrId.setMinimumSize(new java.awt.Dimension(84, 17));
        lblMembrId.setPreferredSize(new java.awt.Dimension(84, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        btnScheduler.add(lblMembrId, gridBagConstraints);

        tdtApplDt.setMaximumSize(new java.awt.Dimension(110, 21));
        tdtApplDt.setMinimumSize(new java.awt.Dimension(110, 21));
        tdtApplDt.setPreferredSize(new java.awt.Dimension(110, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 3, 3);
        btnScheduler.add(tdtApplDt, gridBagConstraints);

        lblApplInwardDate.setText("Application Inward Date");
        lblApplInwardDate.setFont(new java.awt.Font("DejaVu Sans", 0, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 50, 3, 3);
        btnScheduler.add(lblApplInwardDate, gridBagConstraints);

        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSearch.setToolTipText("Search");
        btnSearch.setMaximumSize(new java.awt.Dimension(21, 21));
        btnSearch.setMinimumSize(new java.awt.Dimension(21, 21));
        btnSearch.setNextFocusableComponent(txtCustId);
        btnSearch.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 3, 3, 3);
        btnScheduler.add(btnSearch, gridBagConstraints);

        lblRemarks.setText("Remarks");
        lblRemarks.setFont(new java.awt.Font("DejaVu Sans", 0, 13)); // NOI18N
        lblRemarks.setMaximumSize(new java.awt.Dimension(84, 17));
        lblRemarks.setMinimumSize(new java.awt.Dimension(84, 17));
        lblRemarks.setPreferredSize(new java.awt.Dimension(84, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 50, 3, 3);
        btnScheduler.add(lblRemarks, gridBagConstraints);

        lblSchemName.setFont(new java.awt.Font("DejaVu Sans", 0, 12)); // NOI18N
        lblSchemName.setText("Scheme Name");
        lblSchemName.setMaximumSize(new java.awt.Dimension(100, 17));
        lblSchemName.setMinimumSize(new java.awt.Dimension(100, 17));
        lblSchemName.setPreferredSize(new java.awt.Dimension(100, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 50, 3, 3);
        btnScheduler.add(lblSchemName, gridBagConstraints);

        lblRegStatus.setFont(new java.awt.Font("DejaVu Sans", 0, 12)); // NOI18N
        lblRegStatus.setText("Register Status");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 50, 3, 3);
        btnScheduler.add(lblRegStatus, gridBagConstraints);

        lblLoanAmt.setFont(new java.awt.Font("DejaVu Sans", 0, 12)); // NOI18N
        lblLoanAmt.setText("Loan Amount Applied");
        lblLoanAmt.setMaximumSize(new java.awt.Dimension(150, 17));
        lblLoanAmt.setMinimumSize(new java.awt.Dimension(150, 17));
        lblLoanAmt.setPreferredSize(new java.awt.Dimension(150, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 50, 3, 3);
        btnScheduler.add(lblLoanAmt, gridBagConstraints);

        tdtApplInwrdDt.setMaximumSize(new java.awt.Dimension(110, 21));
        tdtApplInwrdDt.setMinimumSize(new java.awt.Dimension(110, 21));
        tdtApplInwrdDt.setPreferredSize(new java.awt.Dimension(110, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 3);
        btnScheduler.add(tdtApplInwrdDt, gridBagConstraints);

        lblTotalTransactionAmtVal.setMaximumSize(new java.awt.Dimension(110, 21));
        lblTotalTransactionAmtVal.setMinimumSize(new java.awt.Dimension(110, 21));
        lblTotalTransactionAmtVal.setPreferredSize(new java.awt.Dimension(110, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        btnScheduler.add(lblTotalTransactionAmtVal, gridBagConstraints);

        txtMemId.setMaximumSize(new java.awt.Dimension(110, 21));
        txtMemId.setMinimumSize(new java.awt.Dimension(110, 21));
        txtMemId.setPreferredSize(new java.awt.Dimension(110, 21));
        txtMemId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMemIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        btnScheduler.add(txtMemId, gridBagConstraints);

        cboSchemName.setMaximumSize(new java.awt.Dimension(110, 21));
        cboSchemName.setMinimumSize(new java.awt.Dimension(110, 21));
        cboSchemName.setPreferredSize(new java.awt.Dimension(110, 21));
        cboSchemName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSchemNameActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        btnScheduler.add(cboSchemName, gridBagConstraints);

        cboRegstrStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Received", "Sanctioned", "Rejected" }));
        cboRegstrStatus.setMaximumSize(new java.awt.Dimension(110, 21));
        cboRegstrStatus.setMinimumSize(new java.awt.Dimension(110, 21));
        cboRegstrStatus.setPreferredSize(new java.awt.Dimension(110, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        btnScheduler.add(cboRegstrStatus, gridBagConstraints);

        panRemarks.setMaximumSize(new java.awt.Dimension(110, 70));
        panRemarks.setMinimumSize(new java.awt.Dimension(110, 70));
        panRemarks.setPreferredSize(new java.awt.Dimension(110, 70));
        panRemarks.setLayout(new java.awt.GridLayout(1, 0));

        scrRemarks.setMaximumSize(new java.awt.Dimension(110, 30));
        scrRemarks.setMinimumSize(new java.awt.Dimension(110, 30));
        scrRemarks.setPreferredSize(new java.awt.Dimension(110, 30));

        txaRemarks.setMaximumSize(new java.awt.Dimension(110, 30));
        txaRemarks.setMinimumSize(new java.awt.Dimension(110, 30));
        txaRemarks.setPreferredSize(new java.awt.Dimension(110, 30));
        scrRemarks.setViewportView(txaRemarks);

        panRemarks.add(scrRemarks);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 3, 3, 3);
        btnScheduler.add(panRemarks, gridBagConstraints);

        lblCustId.setText("Customer ID ");
        lblCustId.setFont(new java.awt.Font("DejaVu Sans", 0, 13)); // NOI18N
        lblCustId.setMaximumSize(new java.awt.Dimension(100, 21));
        lblCustId.setMinimumSize(new java.awt.Dimension(100, 21));
        lblCustId.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 3, 3, 3);
        btnScheduler.add(lblCustId, gridBagConstraints);

        txtMemName.setMaximumSize(new java.awt.Dimension(110, 21));
        txtMemName.setMinimumSize(new java.awt.Dimension(110, 21));
        txtMemName.setPreferredSize(new java.awt.Dimension(110, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        btnScheduler.add(txtMemName, gridBagConstraints);

        txtLoanAmt.setMaximumSize(new java.awt.Dimension(110, 21));
        txtLoanAmt.setMinimumSize(new java.awt.Dimension(110, 21));
        txtLoanAmt.setPreferredSize(new java.awt.Dimension(110, 21));
        txtLoanAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtLoanAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        btnScheduler.add(txtLoanAmt, gridBagConstraints);

        jPanel2.setMinimumSize(new java.awt.Dimension(290, 25));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel1.setForeground(new java.awt.Color(51, 102, 255));
        jLabel1.setText("Next Account Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel2.add(jLabel1, gridBagConstraints);

        txtNextAccNo.setEnabled(false);
        txtNextAccNo.setMaximumSize(new java.awt.Dimension(110, 21));
        txtNextAccNo.setMinimumSize(new java.awt.Dimension(110, 21));
        txtNextAccNo.setPreferredSize(new java.awt.Dimension(110, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel2.add(txtNextAccNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        btnScheduler.add(jPanel2, gridBagConstraints);

        btnPrintCheck.setText("verify");
        btnPrintCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintCheckActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, -60, 0, 0);
        btnScheduler.add(btnPrintCheck, gridBagConstraints);

        chkLoaneeSecurityonly.setText("LoaneeSecurityOnly");
        chkLoaneeSecurityonly.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkLoaneeSecurityonlyActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        btnScheduler.add(chkLoaneeSecurityonly, gridBagConstraints);

        lblServiceTax.setText("Service Tax");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, -70, 0, 0);
        btnScheduler.add(lblServiceTax, gridBagConstraints);

        lblServiceTaxval.setMaximumSize(new java.awt.Dimension(110, 21));
        lblServiceTaxval.setMinimumSize(new java.awt.Dimension(110, 21));
        lblServiceTaxval.setPreferredSize(new java.awt.Dimension(110, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        btnScheduler.add(lblServiceTaxval, gridBagConstraints);

        chkSettlement.setText("Settlement");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        btnScheduler.add(chkSettlement, gridBagConstraints);

        btnMemLiability.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/Membership_information.gif"))); // NOI18N
        btnMemLiability.setToolTipText("MemberShip Liability Register");
        btnMemLiability.setMaximumSize(new java.awt.Dimension(21, 21));
        btnMemLiability.setMinimumSize(new java.awt.Dimension(21, 21));
        btnMemLiability.setPreferredSize(new java.awt.Dimension(21, 21));
        btnMemLiability.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMemLiabilityActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        btnScheduler.add(btnMemLiability, gridBagConstraints);

        lblDob.setText("DOB");
        lblDob.setToolTipText("DateOfBorth");
        lblDob.setMaximumSize(new java.awt.Dimension(71, 18));
        lblDob.setMinimumSize(new java.awt.Dimension(71, 18));
        lblDob.setPreferredSize(new java.awt.Dimension(71, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        btnScheduler.add(lblDob, gridBagConstraints);

        lblDobValue.setMaximumSize(new java.awt.Dimension(71, 18));
        lblDobValue.setMinimumSize(new java.awt.Dimension(71, 18));
        lblDobValue.setPreferredSize(new java.awt.Dimension(71, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 80, 0, 0);
        btnScheduler.add(lblDobValue, gridBagConstraints);

        lblRetireMent.setText("RetireMentDate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        btnScheduler.add(lblRetireMent, gridBagConstraints);

        lblRetireDate.setMaximumSize(new java.awt.Dimension(71, 18));
        lblRetireDate.setMinimumSize(new java.awt.Dimension(71, 18));
        lblRetireDate.setPreferredSize(new java.awt.Dimension(71, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        btnScheduler.add(lblRetireDate, gridBagConstraints);

        cLabel2.setText("TotalSalary");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, -90, 0, 0);
        btnScheduler.add(cLabel2, gridBagConstraints);

        txtSalaryField.setMinimumSize(new java.awt.Dimension(100, 25));
        txtSalaryField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSalaryFieldActionPerformed(evt);
            }
        });
        txtSalaryField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSalaryFieldFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, -20, 0, 0);
        btnScheduler.add(txtSalaryField, gridBagConstraints);

        txtCostOfVehicleField.setMinimumSize(new java.awt.Dimension(100, 25));
        txtCostOfVehicleField.setPreferredSize(new java.awt.Dimension(100, 25));
        txtCostOfVehicleField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCostOfVehicleFieldActionPerformed(evt);
            }
        });
        txtCostOfVehicleField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCostOfVehicleFieldFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, -50);
        btnScheduler.add(txtCostOfVehicleField, gridBagConstraints);

        lblCostOfVehicle.setText("CostOfVehicle");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        btnScheduler.add(lblCostOfVehicle, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 9;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 50);
        panLoan.add(btnScheduler, gridBagConstraints);

        panChrg.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panChrg.setMinimumSize(new java.awt.Dimension(435, 120));
        panChrg.setPreferredSize(new java.awt.Dimension(435, 120));
        panChrg.setLayout(new java.awt.GridBagLayout());

        panChargeDetails.setMaximumSize(new java.awt.Dimension(425, 110));
        panChargeDetails.setMinimumSize(new java.awt.Dimension(425, 110));
        panChargeDetails.setPreferredSize(new java.awt.Dimension(425, 110));
        panChargeDetails.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        panChrg.add(panChargeDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 50, 0, 0);
        panLoan.add(panChrg, gridBagConstraints);

        panCaste.setMaximumSize(new java.awt.Dimension(370, 100));
        panCaste.setMinimumSize(new java.awt.Dimension(370, 70));
        panCaste.setPreferredSize(new java.awt.Dimension(370, 70));
        panCaste.setLayout(new java.awt.GridBagLayout());

        lblCaste.setText("Caste");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panCaste.add(lblCaste, gridBagConstraints);

        txtCaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCasteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panCaste.add(txtCaste, gridBagConstraints);

        lblPurposeCode.setText("Purpose Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        panCaste.add(lblPurposeCode, gridBagConstraints);

        cboPurposeCode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboPurposeCode.setMinimumSize(new java.awt.Dimension(180, 21));
        cboPurposeCode.setPopupWidth(230);
        cboPurposeCode.setPreferredSize(new java.awt.Dimension(180, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        panCaste.add(cboPurposeCode, gridBagConstraints);

        lblServicePeriodValue.setText("value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        panCaste.add(lblServicePeriodValue, gridBagConstraints);

        lblSeviceperiod.setText("ServicePeriod");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panCaste.add(lblSeviceperiod, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 60, 0, 0);
        panLoan.add(panCaste, gridBagConstraints);

        tabLoan.addTab("Loan Application", panLoan);

        panSecurityDetails.setMaximumSize(new java.awt.Dimension(950, 900));
        panSecurityDetails.setMinimumSize(new java.awt.Dimension(950, 900));
        panSecurityDetails.setPreferredSize(new java.awt.Dimension(950, 900));
        tabLoan.addTab("Security Details", panSecurityDetails);

        panRepayment.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panRepayment.setLayout(new java.awt.GridBagLayout());

        panRepaymentDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panRepaymentDetails.setMinimumSize(new java.awt.Dimension(800, 300));
        panRepaymentDetails.setPreferredSize(new java.awt.Dimension(800, 300));
        panRepaymentDetails.setLayout(new java.awt.GridBagLayout());

        panCalacData.setMaximumSize(new java.awt.Dimension(425, 250));
        panCalacData.setMinimumSize(new java.awt.Dimension(425, 250));
        panCalacData.setPreferredSize(new java.awt.Dimension(425, 250));
        panCalacData.setLayout(new java.awt.GridBagLayout());

        lblLoanAmts.setFont(new java.awt.Font("DejaVu Sans", 0, 13)); // NOI18N
        lblLoanAmts.setText("Eligible   Amount ");
        lblLoanAmts.setMaximumSize(new java.awt.Dimension(131, 18));
        lblLoanAmts.setMinimumSize(new java.awt.Dimension(131, 18));
        lblLoanAmts.setPreferredSize(new java.awt.Dimension(131, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panCalacData.add(lblLoanAmts, gridBagConstraints);

        txtLoanAmts.setMaximumSize(new java.awt.Dimension(110, 21));
        txtLoanAmts.setMinimumSize(new java.awt.Dimension(110, 21));
        txtLoanAmts.setPreferredSize(new java.awt.Dimension(110, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panCalacData.add(txtLoanAmts, gridBagConstraints);

        lblRepayType.setText("Repayment Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panCalacData.add(lblRepayType, gridBagConstraints);

        cboRepayType.setPreferredSize(new java.awt.Dimension(110, 21));
        cboRepayType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboRepayTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panCalacData.add(cboRepayType, gridBagConstraints);

        lblRepayFreq_Repayment.setText("Repayment Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panCalacData.add(lblRepayFreq_Repayment, gridBagConstraints);

        cboRepayFreq_Repayment.setMinimumSize(new java.awt.Dimension(110, 21));
        cboRepayFreq_Repayment.setPreferredSize(new java.awt.Dimension(110, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 2, 6);
        panCalacData.add(cboRepayFreq_Repayment, gridBagConstraints);

        lblFromDt.setText("First InstallmentDate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panCalacData.add(lblFromDt, gridBagConstraints);

        tdtFromDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtFromDtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 5);
        panCalacData.add(tdtFromDt, gridBagConstraints);

        lblMoratorium_Given.setText("Moratorium to be Given");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panCalacData.add(lblMoratorium_Given, gridBagConstraints);

        chkMoratorium_Given.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                chkMoratorium_GivenStateChanged(evt);
            }
        });
        chkMoratorium_Given.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkMoratorium_GivenItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panCalacData.add(chkMoratorium_Given, gridBagConstraints);

        lblFacility_Moratorium_Period.setText("Moratorium Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panCalacData.add(lblFacility_Moratorium_Period, gridBagConstraints);

        txtFacility_Moratorium_Period.setAllowNumber(true);
        txtFacility_Moratorium_Period.setMaximumSize(new java.awt.Dimension(100, 21));
        txtFacility_Moratorium_Period.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFacility_Moratorium_Period.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFacility_Moratorium_PeriodFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCalacData.add(txtFacility_Moratorium_Period, gridBagConstraints);

        lblNoInstall.setText("No. of Installments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panCalacData.add(lblNoInstall, gridBagConstraints);

        txtNoInstall.setAllowNumber(true);
        txtNoInstall.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNoInstall.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNoInstallActionPerformed(evt);
            }
        });
        txtNoInstall.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNoInstallFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCalacData.add(txtNoInstall, gridBagConstraints);

        lblDueDt.setText("Due Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panCalacData.add(lblDueDt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 3, 2, 3);
        panCalacData.add(tdtDueDt, gridBagConstraints);

        txtInstallAmount.setMaximumSize(new java.awt.Dimension(110, 21));
        txtInstallAmount.setMinimumSize(new java.awt.Dimension(110, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panCalacData.add(txtInstallAmount, gridBagConstraints);

        cLabel1.setText("Installment Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        panCalacData.add(cLabel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        panRepaymentDetails.add(panCalacData, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 90, 0, 99);
        panRepayment.add(panRepaymentDetails, gridBagConstraints);

        panSchedule.setMinimumSize(new java.awt.Dimension(100, 100));
        panSchedule.setPreferredSize(new java.awt.Dimension(100, 100));
        panSchedule.setLayout(new java.awt.GridBagLayout());

        btnSchedule.setText("Schedule Calculator");
        btnSchedule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnScheduleActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panSchedule.add(btnSchedule, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 690;
        gridBagConstraints.ipady = -60;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 90, 269, 99);
        panRepayment.add(panSchedule, gridBagConstraints);

        tabLoan.addTab("Repayment Schedule", panRepayment);

        panLoanApplication.add(tabLoan, new java.awt.GridBagConstraints());

        getContentPane().add(panLoanApplication, java.awt.BorderLayout.CENTER);

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setFocusable(false);
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        tbrLoanAppl.add(btnView);

        lblSpace5.setText("     ");
        tbrLoanAppl.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.setFocusable(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        btnNew.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                btnNewFocusLost(evt);
            }
        });
        tbrLoanAppl.add(btnNew);

        lblSpace56.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace56.setText("     ");
        lblSpace56.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoanAppl.add(lblSpace56);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.setFocusable(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrLoanAppl.add(btnEdit);

        lblSpace57.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace57.setText("     ");
        lblSpace57.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace57.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace57.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoanAppl.add(lblSpace57);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.setFocusable(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrLoanAppl.add(btnDelete);

        lblSpace2.setText("     ");
        tbrLoanAppl.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.setFocusable(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrLoanAppl.add(btnSave);

        lblSpace58.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace58.setText("     ");
        lblSpace58.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace58.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace58.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoanAppl.add(lblSpace58);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.setEnabled(false);
        btnCancel.setFocusable(false);
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrLoanAppl.add(btnCancel);

        lblSpace3.setText("     ");
        tbrLoanAppl.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrLoanAppl.add(btnAuthorize);

        lblSpace59.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace59.setText("     ");
        lblSpace59.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace59.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace59.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoanAppl.add(lblSpace59);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.setFocusable(false);
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrLoanAppl.add(btnException);

        lblSpace60.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace60.setText("     ");
        lblSpace60.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace60.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace60.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoanAppl.add(lblSpace60);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrLoanAppl.add(btnReject);

        lblSpace4.setText("     ");
        tbrLoanAppl.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.setFocusable(false);
        tbrLoanAppl.add(btnPrint);

        lblSpace61.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace61.setText("     ");
        lblSpace61.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace61.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace61.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoanAppl.add(lblSpace61);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.setFocusable(false);
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrLoanAppl.add(btnClose);

        lblSpace62.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace62.setText("     ");
        lblSpace62.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace62.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace62.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoanAppl.add(lblSpace62);

        btnDateChange.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/denomination.gif"))); // NOI18N
        btnDateChange.setToolTipText("Exception");
        btnDateChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDateChangeActionPerformed(evt);
            }
        });
        tbrLoanAppl.add(btnDateChange);

        getContentPane().add(tbrLoanAppl, java.awt.BorderLayout.NORTH);

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

        mbrCustomer.setName("mbrCustomer"); // NOI18N

        mnuProcess.setText("Process");
        mnuProcess.setName("mnuProcess"); // NOI18N

        mitNew.setText("New");
        mitNew.setName("mitNew"); // NOI18N
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.setName("mitEdit"); // NOI18N
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.setName("mitDelete"); // NOI18N
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);

        sptNew.setName("sptNew"); // NOI18N
        mnuProcess.add(sptNew);

        mitSave.setText("Save");
        mitSave.setName("mitSave"); // NOI18N
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.setName("mitCancel"); // NOI18N
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);

        sptSave.setName("sptSave"); // NOI18N
        mnuProcess.add(sptSave);

        mitPrint.setText("Print");
        mitPrint.setName("mitPrint"); // NOI18N
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.setName("mitClose"); // NOI18N
        mnuProcess.add(mitClose);

        mbrCustomer.add(mnuProcess);

        setJMenuBar(mbrCustomer);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNoInstallActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNoInstallActionPerformed
        // TODO add your handling code here:
        if (txtNoInstall.getText() != null && !txtNoInstall.getText().equals("")) {
            Installmentchk();
        } else {
            ClientUtil.showMessageWindow("InstallmentNo Cannot be Empty");
            return;
        }
    }//GEN-LAST:event_txtNoInstallActionPerformed

    private void txtNoInstallFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNoInstallFocusLost
        // TODO add your handling code here:
        if (tdtFromDt.getDateValue() != null && !tdtFromDt.getDateValue().equals("")) {
            if (txtNoInstall.getText() != null && !txtNoInstall.getText().equals("")) {
                Installmentchk();
            } else {
                ClientUtil.showMessageWindow("InstallmentNo Cannot be Empty");
                return;
            }
        } else {
            ClientUtil.showMessageWindow("First InstallmentDate Cannot be Empty");
            return;
        }
        String noInstall = txtNoInstall.getText();
        String instAmount = txtInstallAmount.getText();
        if (instAmount != null && instAmount.length() > 0 && noInstall != null && noInstall.length() > 0) {
            double eligble = CommonUtil.convertObjToDouble(txtLoanAmts.getText());
            double totInstAmt = CommonUtil.convertObjToDouble(noInstall) * CommonUtil.convertObjToDouble(instAmount);
            if (totInstAmt > eligble) {
                double noInst=eligble/CommonUtil.convertObjToDouble(instAmount);
                double noOfInstalment=Math.floor(noInst);
                ClientUtil.showMessageWindow("Total Installment Amount Cannot Exceed Eligible Amount .Max Eligible Monthd: "+noOfInstalment);
                txtNoInstall.setText(CommonUtil.convertObjToStr(noOfInstalment));
            }
        }
        
    }//GEN-LAST:event_txtNoInstallFocusLost
    private void Installmentchk() {
        try {
            int noInstallments = 0;
            int noOfInstalmnts;
            if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")) {
                LinkedHashMap arList = new LinkedHashMap();
                HashMap whereMap = new HashMap();
                whereMap.put("CUST_ID", txtCustId.getText());
                int serPeriod = 0;
                String prdid = CommonUtil.convertObjToStr(observable.getCbmSchemName().getKeyForSelected());
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
                int size = tblMemberType.getRowCount();
                arList.put(txtCustId.getText(), serPeriod);
                for (int i = 0; i < size; i++) {
                    whereMap = new HashMap();
                    String memNo = CommonUtil.convertObjToStr(tblMemberType.getValueAt(i, 0));
                    whereMap.put("MEMBER_NO", memNo);
                    List custList = ClientUtil.executeQuery("getMemberName", whereMap);
                    if (custList != null && custList.size() > 0) {
                        HashMap hMap1 = (HashMap) custList.get(0);
                        if (hMap1 != null && hMap1.size() > 0) {
                            String cust_id = CommonUtil.convertObjToStr(hMap1.get("CUST_ID"));
                            if (cust_id != null && cust_id.length() > 0) {
                                HashMap whereMap1 = new HashMap();
                                whereMap1.put("CUST_ID", cust_id);
                                int serPeriod1 = 0;
                                serviceList = ClientUtil.executeQuery("getFutureServicePeriod", whereMap1);
                                if (serviceList != null && serviceList.size() > 0) {
                                    HashMap hMap = (HashMap) serviceList.get(0);
                                    if (hMap != null && hMap.size() > 0 && hMap.containsKey("SERVICE")) {
                                        String ser = CommonUtil.convertObjToStr(hMap.get("SERVICE"));

                                        String[] arr = ser.split("  ");
                                        if (arr.length > 0) {
                                            String yr = arr[0].substring(0, arr[0].indexOf("Y"));
                                            serPeriod1 = serPeriod1 + (CommonUtil.convertObjToInt(yr) * 12);
                                        }
                                        if (arr.length > 1) {
                                            String mnth = arr[1].substring(0, arr[1].indexOf("M"));
                                            serPeriod1 = serPeriod1 + CommonUtil.convertObjToInt(mnth);
                                        }
                                        serPeriod1 = serPeriod1 - ser_period;
                                        arList.put(cust_id, serPeriod1);
                                    }

                                }
                            }
                        }
                    }
                }
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
                    int instNo = CommonUtil.convertObjToInt(txtNoInstall.getText());
                    if (chkPeriod > 0 && instNo > chkPeriod) {
                       // System.out.println("NNNNNNN");
                        ClientUtil.showMessageWindow("InstallmentNo Cannot be exceed more than" + chkPeriod);
                        txtNoInstall.setText(CommonUtil.convertObjToStr(chkPeriod));
                     //   return;
                    }
                }
            }

            if (!cboRepayType.getSelectedItem().equals("User Defined") && !cboRepayType.getSelectedItem().equals("Lump Sum")) {
                noInstallments = minperiodcalculation();
            }
            if ((CommonUtil.convertObjToInt(txtFacility_Moratorium_Period.getText()) > 0) && ((txtFacility_Moratorium_Period.getText()) != null)) {
                noOfInstalmnts = CommonUtil.convertObjToInt(txtNoInstall.getText()) + CommonUtil.convertObjToInt(txtFacility_Moratorium_Period.getText());
            } else {
                noOfInstalmnts = CommonUtil.convertObjToInt(txtNoInstall.getText());
            }
            if (noOfInstalmnts > noInstallments) {
                if (noInstallments > 0) {
                    ClientUtil.showMessageWindow("InstallmentNo Cannot be exceed more than" + noInstallments);
                    txtNoInstall.setText("");
                    return;
                }
            }
            if (noOfInstalmnts > 0) {
                int no_mnths = 0;
                if (cboRepayType.getSelectedIndex() > 0) {
                    if (cboRepayType.getSelectedItem().equals("EMI")) {
                        no_mnths = noOfInstalmnts;
                    }
                    if (cboRepayType.getSelectedItem().equals("Lump Sum")) {
                        no_mnths = noOfInstalmnts;
                    }
                    if (cboRepayType.getSelectedItem().equals("EQI")) {
                        no_mnths = noOfInstalmnts * 4;
                    }
                    if (cboRepayType.getSelectedItem().equals("EYI")) {
                        no_mnths = noOfInstalmnts * 12;
                    }
                    if (cboRepayType.getSelectedItem().equals("EHI")) {
                        no_mnths = noOfInstalmnts * 6;
                    }
                    if (cboRepayType.getSelectedItem().equals("Uniform Principle EMI")) {
                        no_mnths = noOfInstalmnts;
                    }
                    //Added by Jeffin John to set the to date properly
                    Date sanctioDate = DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue());
                    java.util.GregorianCalendar sanCal = new java.util.GregorianCalendar();
                    sanCal.setGregorianChange(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(sanctioDate)));
                    sanCal.setTime(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(sanctioDate)));
                    int currdat = sanctioDate.getDate();
                    Date fromDate = new Date();
                    if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")) {
                        HashMap whereMap = new HashMap();
                        List recoveryParameterList = ClientUtil.executeQuery("getRecoveryParameters", whereMap);
                        if (recoveryParameterList != null & recoveryParameterList.size() > 0) {
                            whereMap = (HashMap) recoveryParameterList.get(0);
                            if (whereMap != null && whereMap.size() > 0) {
                                int firstDay = CommonUtil.convertObjToInt(whereMap.get("FIRST_DAY"));
                                if (currdat <= firstDay) {
                                    sanCal.set(sanCal.DAY_OF_MONTH, sanCal.getActualMaximum(sanCal.DAY_OF_MONTH));
                                    fromDate = sanCal.getTime();
                                } else {
                                    sanCal.set(sanCal.DAY_OF_MONTH, 1);
                                    sanCal.set(sanCal.MONTH, sanCal.get(sanCal.MONTH) + 1);
                                    int lastDayOfMonth = sanCal.getActualMaximum(sanCal.DAY_OF_MONTH);
                                    fromDate = sanCal.getTime();
                                    fromDate.setDate(lastDayOfMonth);
                                }
                            }
                        }
                        for (int i = 0; i < no_mnths - 1; i++) {
                            java.util.GregorianCalendar gCalendar = new java.util.GregorianCalendar();
                            gCalendar.setGregorianChange(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(fromDate)));
                            gCalendar.setTime(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(fromDate)));
                            gCalendar.set(gCalendar.DATE, 1);
                            int curMonth = gCalendar.get(gCalendar.MONTH);
                            int nxtMonth = curMonth + 1;
                            gCalendar.set(gCalendar.MONTH, nxtMonth);
                            gCalendar.set(gCalendar.DATE, gCalendar.getActualMaximum(gCalendar.DATE));
                            fromDate = gCalendar.getTime();
                        }
                    } else {
                        int maximumDate = sanCal.getActualMaximum(sanCal.DAY_OF_MONTH);
                        if (maximumDate <= currdat) {
                            sanCal.set(sanCal.DAY_OF_MONTH, maximumDate);
                        } else {
                            sanCal.set(sanCal.DAY_OF_MONTH, currdat);
                        }
                        fromDate = sanCal.getTime();
                        int currentDate = fromDate.getDate();
                        for (int i = 0; i < no_mnths; i++) {
                            java.util.GregorianCalendar gCalendar = new java.util.GregorianCalendar();
                            gCalendar.setGregorianChange(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(fromDate)));
                            gCalendar.setTime(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(fromDate)));
                            gCalendar.set(gCalendar.DAY_OF_MONTH, 1);
                            gCalendar.set(gCalendar.MONTH, gCalendar.get(gCalendar.MONTH) + 1);
                            int maxDate = gCalendar.getActualMaximum(gCalendar.DAY_OF_MONTH);
                            if (maxDate <= currentDate) {
                                gCalendar.set(gCalendar.DAY_OF_MONTH, maxDate);
                            } else {
                                gCalendar.set(gCalendar.DAY_OF_MONTH, currentDate);
                            }
                            fromDate = gCalendar.getTime();
                        }
                    }
                    tdtDueDt.setDateValue(CommonUtil.convertObjToStr(fromDate));
                    // DateUtil.addDays(
                } else {
                    ClientUtil.showMessageWindow("Please Select Repay Type..");
                    return;
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

    private void tdtFromDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtFromDtFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tdtFromDtFocusLost

    private void cboRepayTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboRepayTypeActionPerformed
        // TODO add your handling code here:
        if (evt.getModifiers() == 16 && CommonUtil.convertObjToStr(cboRepayType.getSelectedItem()).length() > 0) {
            cboRepayTypeActionPerformed();

        }
    }//GEN-LAST:event_cboRepayTypeActionPerformed

    private void cboRepayTypeActionPerformed() {
        ////.out.println("cborepaytypeaction#####"+repayNewMode);
        if ((cboRepayType.getSelectedItem().equals("Lump Sum"))) {
            cboRepayFreq_Repayment.setSelectedItem("Lump Sum");
            // tdtFirstInstall.setEnabled(false);
            cboRepayFreq_Repayment.setEnabled(false);
        } else if (cboRepayType.getSelectedItem().equals("User Defined")) {
            cboRepayFreq_Repayment.setSelectedItem("User Defined");
        } else if (cboRepayType.getSelectedItem().equals("EMI")) {
            // When the Repayment type is EMI the Repayment Frequency
            // must be Monthly
            cboRepayFreq_Repayment.setSelectedItem("Monthly");
            cboRepayFreq_Repayment.setEnabled(false);
            //                if (!((observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) || (viewType.equals(AUTHORIZE)))){
            //                    txtNoInstall.setEditable(true);
            //                    tdtFirstInstall.setEnabled(true);
            //                }
//                if ((tblRepaymentCTable.getRowCount() < 1 && allowMultiRepay) || (dumRowRepay == 0) || (tblRepaymentCTable.getRowCount() == 0)){
//                    allowMultiRepay = true;
//                }
        } else if (cboRepayType.getSelectedItem().equals("EQI")) {
            // When the Repayment type is EQI the Repayment Frequency
            // must be Quaterly
            cboRepayFreq_Repayment.setSelectedItem("Quaterly");
            cboRepayFreq_Repayment.setEnabled(false);
        } else if (cboRepayType.getSelectedItem().equals("EHI")) {
            cboRepayFreq_Repayment.setSelectedItem("Half Yearly");
            cboRepayFreq_Repayment.setEnabled(false);

        } else if (cboRepayType.getSelectedItem().equals("EYI")) {
            // When the Repayment type is EYI the Repayment Frequency
            // must be Yearly
            cboRepayFreq_Repayment.setSelectedItem("Yearly");
            cboRepayFreq_Repayment.setEnabled(false);

        } else if (cboRepayType.getSelectedItem().equals("Uniform Principle EMI")) { //This
            // When the Repayment type is EMI the Repayment Frequency
            // must be Monthly
            cboRepayFreq_Repayment.setSelectedItem("Monthly");
            cboRepayFreq_Repayment.setEnabled(false);
            //                if (!((observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) || (viewType.equals(AUTHORIZE)))){
            //                    txtNoInstall.setEditable(true);
            //                    tdtFirstInstall.setEnabled(true);
            //                }
//                if ((tblRepaymentCTable.getRowCount() < 1 && allowMultiRepay) || (dumRowRepay == 0) || (tblRepaymentCTable.getRowCount() == 0)){
//                    allowMultiRepay = true;
//                }
        } else {
            cboRepayFreq_Repayment.setSelectedItem(" ");
        }

    }

    private java.sql.Timestamp getTimestamp(java.util.Date date) {
        return new java.sql.Timestamp(date.getYear(), date.getMonth(), date.getDate(), date.getHours(), date.getMinutes(), date.getSeconds(), 0);
    }

    private java.math.BigDecimal getBigDecimal(double doubleValue) {
        return new java.math.BigDecimal(doubleValue);
    }

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
    private void btnScheduleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnScheduleActionPerformed
        // TODO add your handling code here:
        // TermLoanInstallmentUI instalmentUI=new TermLoanInstallmentUI();
        TermLoanInstallmentUI.fromDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtFromDt.getDateValue()));
        HashMap whereMap = new HashMap();
        whereMap.put("CATEGORY_ID", "STAFF");
        whereMap.put("AMOUNT", getBigDecimal(CommonUtil.convertObjToDouble(txtLoanAmts.getText()).doubleValue()));
        whereMap.put("PROD_ID", observable.getCbmSchemName().getKeyForSelected());
        whereMap.put("FROM_DATE", setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue())));
        whereMap.put("TO_DATE", setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtDueDt.getDateValue())));
        java.util.ArrayList interestList = (java.util.ArrayList) ClientUtil.executeQuery("getSelectProductTermLoanInterestTO", whereMap);
        //.out.println("int1111111111" + interestList);
        if (interestList != null && interestList.size() > 0) {
            observableInt.setTermLoanInterestTO(interestList, null);
        }

        HashMap repayData = new HashMap();
        String loanType = "";
        if (tdtFromDt != null) {
            repayData.put("FROM_DATE", DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue()));
        }

        repayData.put("TO_DATE", DateUtil.getDateMMDDYYYY(tdtDueDt.getDateValue()));
        repayData.put("REPAYMENT_START_DT", DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue()));
        repayData.put("NO_INSTALL", txtNoInstall.getText());
        repayData.put("ISDURATION_DDMMYY", "YES");
        //            if (rdoInterest_Compound.isSelected()){
        repayData.put("INTEREST_TYPE", "COMPOUND");
        //            }else if (rdoInterest_Simple.isSelected()){
        //                repayData.put("INTEREST_TYPE", "SIMPLE");
        //            }
        repayData.put("DURATION_YY", txtNoInstall.getText());
        //            date=null;
        repayData.put("COMPOUNDING_PERIOD", observable.getCbmRepayFreq_Repayment().getKeyForSelected());//CommonUtil.convertObjToStr(prodLevelValues.get("DEBITINT_COMP_FREQ")));
        if (cboRepayType.getSelectedItem().equals("User Defined")) {
            repayData.put("REPAYMENT_TYPE", observable.getCbmRepayType().getKeyForSelected());
        } else {
            repayData.put("REPAYMENT_TYPE", observable.getCbmRepayType().getKeyForSelected());
        }
        //            repayData.put("EMI_TYPE","UNIFORM_EMI");
        repayData.put("PRINCIPAL_AMOUNT", txtLoanAmts.getText());
        repayData.put("ROUNDING_FACTOR", "1_RUPEE");
        repayData.put("ROUNDING_TYPE", "1_RUPEE");
        repayData.put("REPAYMENT_FREQUENCY", observable.getCbmRepayFreq_Repayment().getKeyForSelected());
        if (CommonUtil.convertObjToDouble(txtInstallAmount.getText()) > 0) {
            repayData.put("INSTALLMENT_AMOUNT", CommonUtil.convertObjToDouble(txtInstallAmount.getText()));
        }
//                if(observable.isDailyLoan()){
//                    repayData.put("REPAYMENT_FREQUENCY", String.valueOf(1));
//                    repayData.put("TO_DATE", DateUtil.getDateMMDDYYYY(tdtTDate.getDateValue()));
//                }




//            if(tblRepaymentCTable.getRowCount()>0 && tblRepaymentCTable.getSelectedRow() !=-1)//purpose existing installment taking from oracle table
//                repayData.put("SCHEDULE_ID", tblRepaymentCTable.getValueAt(tblRepaymentCTable.getSelectedRow(),0));
//            
        //
        interestList = new ArrayList();
        interestList = observableInt.getInterestDetails(tdtFromDt.getDateValue(), tdtDueDt.getDateValue(), DateUtil.getDateMMDDYYYY(tdtDueDt.getDateValue()), DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue()));
        if (interestList != null && interestList.size() > 0) {
            repayData.put("INTEREST", ((HashMap) interestList.get(0)).get("INTEREST"));
            repayData.put("VARIOUS_INTEREST_RATE", interestList);
            repayData.put(loanType, "");
        }
        new TermLoanInstallmentUI(this, repayData,true).show();



    }//GEN-LAST:event_btnScheduleActionPerformed

    private boolean chkAmount() {
        double max_period = 0.0;
        int count = 0;
        double surety = 0.0;
        double ser_period = 0.0;
        boolean flag = false;
        boolean flag1 = false;
        // boolean chk=true;
        max_period = getMaxSerivice_period();
        String prdid = CommonUtil.convertObjToStr(observable.getCbmSchemName().getKeyForSelected());
        double amt = Double.parseDouble(txtLoanAmt.getText());
        HashMap prodMap = new HashMap();
        prodMap.put("PROD_ID", prdid);
        prodMap.put("AMOUNT", amt);
        List prodList = ClientUtil.executeQuery("getServicewiseLoanDetail", prodMap);
        if (prodList != null && prodList.size() > 0) {
            for (int i = 0; i < prodList.size(); i++) {
                HashMap servicewiseMap = (HashMap) prodList.get(i);
                ser_period = CommonUtil.convertObjToDouble(servicewiseMap.get("PAST_SERVICE_PERIOD"));
                surety = CommonUtil.convertObjToDouble(servicewiseMap.get("NO_OF_SURETIES_REQUIRED"));
                if (max_period >= ser_period) {
                    flag = true;
                    count++;
                    if (tblMemberType.getRowCount() >=((int) surety)) {
                        flag1 = true;
                        break;
                    }
                } else {
                    flag = false;
                }
               
            }

            if (count == 0) {
                ClientUtil.showMessageWindow("You are not Eligible for this Loan Amount");
                return false;
            } else if (flag1 == false) {
                ClientUtil.showMessageWindow("Available Surety is " + tblMemberType.getRowCount() + "   Surety required is " + surety);
                return false;
            }
        } else {
            //ClientUtil.showMessageWindow("Please configure Surety Details " );
            return false;
        }
        return true;
    }

    private double getMaxSerivice_period() {
        double sPeriod = 0.0;
        double[] s = new double[1];
        for (int k = 0; k < tblMemberType.getRowCount(); k++) {
            String memno = CommonUtil.convertObjToStr(tblMemberType.getValueAt(k, 0));
            HashMap memMap = new HashMap();
            memMap.put("MEMBERSHIP_NO", memno);
            List aList = ClientUtil.executeQuery("getDOB", memMap);
            if (aList.size() > 0 && aList != null) {
                HashMap dobMap = (HashMap) (aList.get(0));
                Date joiningdt = (Date) dobMap.get("JOINING_DATE");
                long a = DateUtil.dateDiff(joiningdt, curr_dt);
                long b = a / 365;
                if (k == 0) {
                    s[0] = (double) b;
                    //  s[1]=0;
                } else {
                    if (s[0] < (double) b) {
                        s[0] = (double) b;
                    }
                }


            } else {
                ClientUtil.showMessageWindow("Please mark Membership no or 'Joining Date' in Customer screen " + memno);
                return sPeriod;
            }
        }
        String loaneeno = txtMemId.getText();
        HashMap loaneeMap = new HashMap();
        loaneeMap.put("MEMBERSHIP_NO", loaneeno);
        List aList1 = ClientUtil.executeQuery("getDOB", loaneeMap);
        if (aList1.size() > 0 && aList1 != null) {
            HashMap dobMap1 = (HashMap) (aList1.get(0));
            Date joiningdt1 = (Date) dobMap1.get("JOINING_DATE");
            long a = DateUtil.dateDiff(joiningdt1, curr_dt);
            long b = a / 365;
            if (s[0] < (double) b) {
                s[0] = (double) b;
            }
        }
        sPeriod = s[0];
        return sPeriod;
    }

    private void txtLoanAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLoanAmtFocusLost
        // TODO add your handling code here:
        if (!chkSettlement.isSelected()) {
        setImbpSettings();//this line added by Anju Anand for Mantid Id: 0010365
        }
        //charge details
        //Added By Suresh
        if (!chkSettlement.isSelected()) {
            if (txtLoanAmt.getText() != null && !txtLoanAmt.getText().equals("")) {
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {    // Loan Charges
                    prodDesc = CommonUtil.convertObjToStr(cboSchemName.getModel().getSelectedItem());
                    chrgTableEnableDisable();
                    createChargeTable(prodDesc);
                    chargeAmount();
                    tableMouseClicked();
                    txtLoanAmts.setText(txtLoanAmt.getText());

                }
                //comented by rishad 10/05/2016 as per discussion with prasanth 
//                if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")) {
//                    if (!cboSchemName.getSelectedItem().equals("") && cboSchemName.getSelectedItem().toString() != null) {
//                        System.out.println("hiiiiiiii");
//                        chkEligibleAmount();
//                    }
//                }

                // initTableData();

            }

            //added by rishad 03/08/2014    commented at 10/05/2016 as per discussion with prsanth
//            if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y") && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {  //IMBP
//
//                HashMap whereMap = new HashMap();
//                whereMap.put("CUST_ID", txtCustId.getText());
//                whereMap.put("MEM_NO", txtMemId.getText());
//                whereMap.put("DATE", curr_dt);
//                // List imbpLst = ClientUtil.executeQuery("getShareIMBPAmount", whereMap);
//                List imbpLst = ClientUtil.executeQuery("getFuncShareIMBPAmount", whereMap);
//                if (imbpLst != null && imbpLst.size() > 0) {
//                    String displayStr = "";
//                    double imbpAmt = 0.0;
//                    double totalSanctionAmt = 0.0;
//                    double finalTotalSanctionAmt = 0.0;
//                    whereMap = (HashMap) imbpLst.get(0);
//                    System.out.println("ffffffff" + whereMap);
//                    imbpAmt = CommonUtil.convertObjToDouble(whereMap.get("IMBP_AMOUNT")).doubleValue();
//                    System.out.println("rishhhh" + imbpAmt);
//                    double sanctionAmt = CommonUtil.convertObjToDouble(txtLoanAmt.getText()).doubleValue();
//                    if (imbpAmt > 0) {
//                        HashMap loanSanctionMap = new HashMap();
//                        loanSanctionMap.put("CUST_ID", txtCustId.getText());
//                        List loanSanctionLst = ClientUtil.executeQuery("getTotalSanctionAmountOnlyTL", loanSanctionMap);
//                        if (loanSanctionLst != null && loanSanctionLst.size() > 0) {
//                            loanSanctionMap = new HashMap();
//                            for (int j = 0; j < loanSanctionLst.size(); j++) {
//                                loanSanctionMap = (HashMap) loanSanctionLst.get(j);
//                                totalSanctionAmt += CommonUtil.convertObjToDouble(loanSanctionMap.get("SANCTION_AMOUNT")).doubleValue();
//                                displayStr += "Existing Loan No  : " + loanSanctionMap.get("ACCT_NUM") + "\n"
//                                        + "Limit                     : Rs " + loanSanctionMap.get("SANCTION_AMOUNT") + "\n";
//                            }
//                            displayStr += "Total Sanction Amount            :  Rs " + totalSanctionAmt + "\n";
//                            displayStr += "IMBP Balance Amount             :  Rs " + (imbpAmt);
//                            if (!displayStr.equals("")) {
//                                ClientUtil.showMessageWindow("" + displayStr);
//                            }
//                            if (imbpAmt < sanctionAmt) {
//                                ClientUtil.showMessageWindow("Customer Not Eligible For Loan");
//                                txtLoanAmt.setText("");
//                                return;
//                            }
//                        } else {
//                            displayStr += "Eligible Amount :  Rs " + (imbpAmt);
//                            if (!displayStr.equals("")) {
//                                ClientUtil.showMessageWindow("" + displayStr);
//                            }
//                        }
//                    }
//                }
//            }
            if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")) {
                String AcctNum = "";
                HashMap resultMap = new HashMap();
                prodDesc = CommonUtil.convertObjToStr(cboSchemName.getModel().getSelectedItem());
                HashMap whereMap1 = new HashMap();
                whereMap1.put("CUSTID", txtCustId.getText());
                whereMap1.put("PROD_DESC", prodDesc);
                whereMap1.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
                List acctList = ClientUtil.executeQuery("getLoanAcctNum", whereMap1);
                if (acctList != null && acctList.size() > 0) {
                    resultMap = (HashMap) acctList.get(0);
                    if (resultMap != null && resultMap.containsKey("ACCT_NUM")) {
                        AcctNum = CommonUtil.convertObjToStr(resultMap.get("ACCT_NUM"));
                    }
                }
                if (AcctNum != null && AcctNum.length() > 0) {
                    HashMap asAndWhenMap = new HashMap();
                    if (AcctNum != null && AcctNum.length() > 0) {
                        HashMap mapHash = accountClosingob.asAnWhenCustomerComesYesNO(AcctNum);
                        if (mapHash.containsKey("AS_CUSTOMER_COMES") && mapHash.get("AS_CUSTOMER_COMES") != null && mapHash.get("AS_CUSTOMER_COMES").equals("Y")) {
                            asAndWhenMap = interestCalculationTLAD(AcctNum);
                            if (asAndWhenMap != null && asAndWhenMap.size() > 0) {
                                if (mapHash != null && mapHash.size() > 0) {
                                    asAndWhenMap.put("INSTALL_TYPE", mapHash.get("INSTALL_TYPE"));
                                }
                            }
                        }
                    }
                    String prod_id1 = ((ComboBoxModel) cboSchemName.getModel()).getKeyForSelected().toString();
                    String displayloan = "";
                    HashMap dataMap = new HashMap();
                    HashMap chargeMap = new HashMap();
                    HashMap returnMap = observable.getAccountClosingCharges(prod_id1, AcctNum);
                    dataMap = (HashMap) returnMap.get("dataMap");
                    chargeMap = (HashMap) returnMap.get("chargeMap");
                    if (chargeMap != null && chargeMap.get("ACCT_NUM")!=null) {
                            double interest=0.0;
                            double penal=0.0;
                            if (dataMap.get("AccountInterest") != null) {
                            interest = CommonUtil.convertObjToDouble(dataMap.get("AccountInterest"));
                        }
                        if (dataMap.get("AccountPenalInterest") != null) {
                            penal = CommonUtil.convertObjToDouble(dataMap.get("AccountPenalInterest"));
                        }
                        displayloan += "Existing Loan No  : " + chargeMap.get("ACCT_NUM") + "\n"
                                    + "Interest                    : Rs " + interest + "\n"
                                    + "Penal                       : Rs " + penal+ "\n"
                                    + "Principal                   : Rs " + Math.abs(CommonUtil.convertObjToDouble(chargeMap.get("CLEAR_BALANCE"))) + "\n";
                                     ClientUtil.showMessageWindow("" + displayloan);
                    }
                }
            }
        }
        
        //Added by Jeffin John for Mantis - 9696 on 16/10/2014 Only for employees society
        if (chkSettlement.isSelected()) {
            if (txtLoanAmt.getText() != null && !txtLoanAmt.getText().equals("")) {
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                    prodDesc = CommonUtil.convertObjToStr(cboSchemName.getModel().getSelectedItem());
                    chrgTableEnableDisable();
                    createChargeTable(prodDesc);
                    chargeAmount();
                    txtLoanAmts.setText(txtLoanAmt.getText());
                }
               //commented by rishad as per discussion with prsanth 06/09/2016 all checking doing under the function  calling eligibility checking
//                if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")) {
//                    if (!cboSchemName.getSelectedItem().equals("") && cboSchemName.getSelectedItem().toString() != null) {
//                        chkEligibleAmount();
//                    }
//                }
            }
            
            //Checking For Loan Eligable Amount in IMBP_SETTINGS table
            String prodId = CommonUtil.convertObjToStr(observable.getCbmSchemName().getKeyForSelected());
            double sanctionAmt = CommonUtil.convertObjToDouble(txtLoanAmt.getText()).doubleValue();
            HashMap prodMap = new HashMap();
            prodMap.put("PROD_ID", prodId);
            List eligableAmountList = ClientUtil.executeQuery("getElibebleImbpAmount", prodMap);
            if(eligableAmountList!=null && eligableAmountList.size()>0){
                prodMap = (HashMap) eligableAmountList.get(0);
                if(prodMap!=null && prodMap.size()>0 && prodMap.containsKey("LOAN_AMT")){
                    Double loanAmt = CommonUtil.convertObjToDouble(prodMap.get("LOAN_AMT"));
                    if(loanAmt<sanctionAmt){
                        txtLoanAmt.setText("");
                        ClientUtil.showAlertWindow("Maximum Eligable Amount is : "+loanAmt);
                        return;
                    }
                }
            }
        }
    }//GEN-LAST:event_txtLoanAmtFocusLost

    private HashMap interestCalculationTLAD(String accountNo) {
        HashMap map = new HashMap();
        HashMap hash = null;
        try {
            map.put("ACT_NUM", accountNo);
            String prod_id = ((ComboBoxModel) cboSchemName.getModel()).getKeyForSelected().toString();
            map.put("PROD_ID", prod_id);
            map.put("TRANS_DT", curr_dt);
            map.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
            List lst = ClientUtil.executeQuery("IntCalculationDetail", map);
            if (lst == null || lst.isEmpty()) {
                lst = ClientUtil.executeQuery("IntCalculationDetailAD", map);
            }
            if (lst != null && lst.size() > 0) {
                hash = (HashMap) lst.get(0);
                if (hash.get("AS_CUSTOMER_COMES") != null && hash.get("AS_CUSTOMER_COMES").equals("N")) {
                    hash = new HashMap();
                    return hash;
                }
                map.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
                map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                map.putAll(hash);
                map.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");
                map.put("CURR_DATE", ClientUtil.getCurrentDateProperFormat());
                map.put("LOAN_EMI_CLOSE", "LOAN_EMI_CLOSE");
                map.put("PREMATURE_ONEMONTH_INT", "PREMATURE_ONEMONTH_INT");
                map.put("SOURCE_SCREEN", "LOAN_CLOSING");
                hash = accountClosingob.loanInterestCalculationAsAndWhen(map);
                if (hash == null) {
                    hash = new HashMap();
                }
                hash.put("AS_CUSTOMER_COMES", map.get("AS_CUSTOMER_COMES"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return hash;
    }

//    public void initTableData() {
//        // model=new javax.swing.table.DefaultTableModel();
//        System.out.println("NAMEEEEKFMAKFD");
//        tblShare.setModel(new javax.swing.table.DefaultTableModel(
//                setTableData(),
//                new String[]{
//                    "Select", "Share Amount", "Membership Fee"}) {
//            Class[] types = new Class[]{
//                java.lang.Boolean.class,
//                java.lang.String.class,
//                java.lang.String.class,};
//            boolean[] canEdit = new boolean[]{
//                true, false, false
//            };
//
//            public Class getColumnClass(int columnIndex) {
//                return types[columnIndex];
//            }
////            public boolean isCellEditable(int rowIndex, int columnIndex) {
////                if (columnIndex==4 ||columnIndex==6 || columnIndex==7 || columnIndex==10 || columnIndex==11) {
////                    return true;
////                }
////                return canEdit [columnIndex];
////            }
//        });
//        // setWidthColumns();
//
//        tblShare.setCellSelectionEnabled(true);
//        tblShare.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
//            public void propertyChange(java.beans.PropertyChangeEvent evt) {
//                tblSharePropertyChange(evt);
//            }
//        });
//
//        setTableModelListener();
////        if(tblShare.getRowCount()>0)
////        {  
////          // boolean chk=((Boolean)tblShare.getValueAt(tblShare.getSelectedRow(), 0)).booleanValue(); 
////            System.out.println("calcTotal inuit=====");
////            calcTotal(true,tblShare.getSelectedRow(),0);
////        }
//    }
//
//    private Object[][] setTableData() {
//        System.out.println("bjnbdfjbbf>>>>");
//        HashMap whereMap = new HashMap();
//        HashMap whereMap1 = new HashMap();
//        HashMap whereMap2 = new HashMap();;
//        HashMap whereMap3 = new HashMap();
//        System.out.println("txtCustId.getText()sdfgb1212121" + txtCustId.getText());
//        System.out.println("txtMemNo.getText()>>>sdfsf1212121" + txtMemId.getText());
//        whereMap.put("CUST_ID", txtCustId.getText());
//        whereMap.put("MEM_NO", txtMemId.getText());
//        System.out.println("txtMemNo.getText()>>>1212121" + txtMemId.getText());
//        System.out.println("observable.getCbmSchemName().getKeyForSelected())>>>1212121" + observable.getCbmSchemName().getKeyForSelected());
//        whereMap.put("PROD_ID", CommonUtil.convertObjToStr(observable.getCbmSchemName().getKeyForSelected()));
//        List lst1 = ClientUtil.executeQuery("getSelectOutstandingAmount", whereMap);
//        List lst2 = ClientUtil.executeQuery("getSelectFaceValueAdmFeeMaxAdmFee", whereMap);
//        List lst3 = ClientUtil.executeQuery("getSelectMaxShareBorrower", whereMap);
//        System.out.println("lst1>>>" + lst1);
//        System.out.println("lst2>>>" + lst2);
//        System.out.println("lst3>>>" + lst3);
//        if (lst1 != null && lst1.size() > 0) {
//            whereMap1 = (HashMap) lst1.get(0);
//        }
//        if (lst2 != null && lst2.size() > 0) {
//            whereMap2 = (HashMap) lst2.get(0);
//        }
//        if (lst3 != null && lst3.size() > 0) {
//            whereMap3 = (HashMap) lst3.get(0);
//        }
//        //for share amount
//        double shareBal = CommonUtil.convertObjToDouble(whereMap1.get("SHARE_BALANCE"));
//        double loanApplied = CommonUtil.convertObjToDouble(txtLoanAmt.getText());
//        double borrowerShare = CommonUtil.convertObjToDouble(whereMap3.get("MAX_SHARE_BORROWER"));
//        if(borrowerShare<1){
//            ClientUtil.showAlertWindow("Borrower Share is insufficient");
//            clearTblShare();
//        }
//        double shareCalcAmt = loanApplied * borrowerShare / 100;
//        reqShareAmt = shareCalcAmt - shareBal;
//        System.out.println("shareBal>>>>11111" + shareBal);
//        System.out.println("loanApplied>>>>11111" + loanApplied);
//        System.out.println("borrowerShare>>>>11111" + borrowerShare);
//        System.out.println("shareCalcAmt>>>>11111" + shareCalcAmt);
//        System.out.println("reqShareAmt>>>>11111" + reqShareAmt);
//        //for membshp fee
//
//        double admFeeTaken = CommonUtil.convertObjToDouble(whereMap2.get("ADM_FEE"));
//        double faceVal = CommonUtil.convertObjToDouble(whereMap2.get("FACE_VALUE"));
//        double maxAdmFee = CommonUtil.convertObjToDouble(whereMap2.get("MAX_ADM_FEE"));
//        double admFee = reqShareAmt / faceVal * admFeeTaken;
//        System.out.println("admFeeTaken>>>>11111" + admFeeTaken);
//        System.out.println("faceVal>>>>11111" + faceVal);
//        System.out.println("maxAdmFee>>>>11111" + maxAdmFee);
//        System.out.println("admFee>>>>11111" + admFee);
//
//        availShareNo = reqShareAmt / faceVal;
//        System.out.println("availShareNo>>>>11111" + availShareNo);
//
//        if (admFee < maxAdmFee) {
//            membshpFee = maxAdmFee - admFee;
//        }
//        System.out.println("membshpFee>>>>11111" + membshpFee);
//
//        shareFees = reqShareAmt + membshpFee;
//        System.out.println("shareFees11111>>>>" + shareFees);
//        //        globalList=lst;
////        if(reqShareAmt!=0.0){
////            observable.setFinalList(lst);
//        //   model= new DefaultTableModel((ArrayList)lst);
//        totalList = new Object[1][3];
//        //  if(lst!=null && lst.size()>0){
//        //  for(int i = 0;i<lst.size();i++){
//        //  whereMap = (HashMap)lst.get(i);
//        //  ArrayList aList=(ArrayList)lst.get(i);
//        //  globalList=aList;
//        //   return aList;
//        //   for(int j = 0;j<aList.size();j++){
//        totalList[0][0] = true;
//        totalList[0][1] = CommonUtil.convertObjToStr(reqShareAmt);
//        totalList[0][2] = CommonUtil.convertObjToStr(membshpFee);
//        
//        if(borrowerShare<1){
//           // ClientUtil.showAlertWindow("Borrower Share is insufficient");
//            clearTblShare();
//           // totalList = null;
//        }
////                        totalList[i][3] = aList.get(2).toString();
////                        totalList[i][4] = aList.get(3).toString();
////                        totalList[i][5] = aList.get(4).toString();
////                        totalList[i][6] = aList.get(5).toString();
////                        totalList[i][7] = aList.get(6).toString();
////                        totalList[i][8] = aList.get(7).toString();
////                        totalList[i][9] = aList.get(8).toString();
////                        totalList[i][10] = aList.get(9).toString();
////                        totalList[i][11] = aList.get(10).toString();
////                        totalList[i][12] = aList.get(11).toString();
////                        totalList[i][13] = aList.get(12).toString();
////                        totalList[i][14] = aList.get(13).toString();
////                        // System.out.println("DATAA==="+aList.get(j));
////                        if(aList.get(0)!=null && aList.get(0).toString().equals("TL"))
////                        {
////                            getLoanDetails1(aList.get(0).toString(),aList.get(1).toString(),aList.get(2).toString(),aList.get(3).toString()
////                            ,aList.get(4).toString(),aList.get(5).toString(),aList.get(6).toString(),aList.get(7).toString()
////                            ,aList.get(8).toString(),aList.get(9).toString(),aList.get(10).toString(),aList.get(11).toString()
////                            ,aList.get(12).toString(),aList.get(13).toString());
////                        }
////                         if(aList.get(0)!=null && aList.get(0).toString().equals("MDS"))
////                         {
////                             calcEachChittal1(aList.get(0).toString(),aList.get(1).toString(),aList.get(2).toString(),aList.get(3).toString()
////                            ,aList.get(4).toString(),aList.get(5).toString(),aList.get(6).toString(),aList.get(7).toString()
////                            ,aList.get(8).toString(),aList.get(9).toString(),aList.get(10).toString(),aList.get(11).toString()
////                            ,aList.get(12).toString(),aList.get(13).toString());
////                         }
////                         if(aList.get(0)!=null && aList.get(0).toString().equals("AD"))
////                         {
////                             getADDetails(aList.get(0).toString(),aList.get(1).toString(),aList.get(2).toString(),aList.get(3).toString()
////                            ,aList.get(4).toString(),aList.get(5).toString(),aList.get(6).toString(),aList.get(7).toString()
////                            ,aList.get(8).toString(),aList.get(9).toString(),aList.get(10).toString(),aList.get(11).toString()
////                            ,aList.get(12).toString(),aList.get(13).toString());
////                         }
////                        if(aList.get(0)!=null && aList.get(0).toString().equals("SA"))
////                        {
////                            getSADetails(aList.get(0).toString(),aList.get(1).toString(),aList.get(2).toString(),aList.get(3).toString()
////                            ,aList.get(4).toString(),aList.get(5).toString(),aList.get(6).toString(),aList.get(7).toString()
////                            ,aList.get(8).toString(),aList.get(9).toString(),aList.get(10).toString(),aList.get(11).toString()
////                            ,aList.get(12).toString(),aList.get(13).toString());
////                        }
////                         if(aList.get(0)!=null && aList.get(0).toString().equals("TD"))
////                        {
////                            getTDDetails(aList.get(0).toString(),aList.get(1).toString(),aList.get(2).toString(),aList.get(3).toString()
////                            ,aList.get(4).toString(),aList.get(5).toString(),aList.get(6).toString(),aList.get(7).toString()
////                            ,aList.get(8).toString(),aList.get(9).toString(),aList.get(10).toString(),aList.get(11).toString()
////                            ,aList.get(12).toString(),aList.get(13).toString());
////                        }
//        // }
//
//
//        //  }
//        // }
//        return totalList;
////        }else{
////            ClientUtil.displayAlert("No Data!!! ");
////            observable.resetForm();
////          //  txtName.setText("");
////        }
//        // return null;
//
//    }
//
//    private void setTableModelListener() {
//        tableModelListener = new TableModelListener() {
//            public void tableChanged(TableModelEvent e) {
//                if (e.getType() == TableModelEvent.UPDATE) {
//                    System.out.println("Cell " + e.getFirstRow() + ", "
//                            + e.getColumn() + " changed. The new value: "
//                            + tblShare.getModel().getValueAt(e.getFirstRow(),
//                            e.getColumn()));
//                    int row = e.getFirstRow();
//                    int column = e.getColumn();
//
//                    String scheme = CommonUtil.convertObjToStr(tblShare.getValueAt(tblShare.getSelectedRow(), 1));
////                    if (column == 4 || column == 0 ) {//|| column ==6
////                       System.out.println("fi45555555555column=="+column);  
////                        TableModel model = tblShare.getModel();
////                        String acc_no = CommonUtil.convertObjToStr(tblShare.getValueAt(tblShare.getSelectedRow(),3));
////                        String noOfInsPay = CommonUtil.convertObjToStr(tblShare.getValueAt(tblShare.getSelectedRow(),4));
////                        int selectedRow=tblShare.getSelectedRow(); 
////                        System.out.println("AC NO="+acc_no+" finalMap BEDFORE===="+finalMap);
////                        if(scheme.equals("MDS")) {
////                            if (acc_no.indexOf("_")!=-1) {
////                               acc_no = acc_no.substring(0,acc_no.indexOf("_"));
////                            }
////                        }
////                        if(finalMap!=null && finalMap.containsKey(acc_no)) {
////                            finalMap.remove(acc_no);
////                        }
////                         System.out.println("finalMap AFETR===="+finalMap);
////                          boolean chk=((Boolean)tblShare.getValueAt(tblShare.getSelectedRow(), 0)).booleanValue();
////                          if(chk)
////                          {
////                            if(noOfInsPay==null || noOfInsPay.equals("") || CommonUtil.convertObjToDouble(noOfInsPay)<=0) 
////                            {
////                                displayAlert("Paying should not be empty!!!\n");
////                                return;
////                            }
////                          }
////                        if(scheme.equals("TL")) {
////                            getLoanDetails("3",column,chk,"empty",selectedRow);
////                            System.out.println("column =========== "+column);
////                      }
////                        else  if(scheme.equals("TD")) {
////                            getTDDetails(column,chk,"empty",selectedRow);
////                            // calcTotal();
////                        }
////                        else  if(scheme.equals("SA")) {
////                            getSADetails(column,chk,"empty",selectedRow);
////                            // calcTotal();
////                        }
////                        else if(scheme.equals("MDS")) {
////                            //  if(CommonUtil.convertObjToDouble(noOfInsPay).doubleValue()>=1)
////                            // {
////                             java.awt.event.MouseEvent evt=null;
////                          
////                            calcEachChittal(column,chk,"empty",selectedRow);//e.
////                            if(column==3)
////                            {
////                                 System.out.println("in table mousclickkkkkkk "+column);
////                         tblShare.addMouseListener(new java.awt.event.MouseAdapter() {
////            public void mouseClicked(java.awt.event.MouseEvent evt) {
////           //     System.out.println("in table mousclickkkkkkk "+column);
////              //  tblShareMouseClicked(evt);
////            }
////        });
////                            }
////                           // tblShareMouseClicked(null); 
////                           /* if(chittalFlag==false){
////                                String chittalNo ="";
////                                finalMap.put(chittalNo,termMdsChittalMap);
////                            }
////                            chittalFlag=false;*/
////                            //  calcTotal();
////                            // }
////                        }
////                        else if(scheme.equals("AD"))
////                        {
////                             getADDetails(column,chk,"empty",selectedRow);
////                        }
////                        else if(column == 3) {
////                            getSBDetails(column,chk,"empty",selectedRow);
////                        }
////                        System.out.println("calacToatal sel======"+selectedRow+" chk=="+chk+" column==="+column);
////                        calcTotal(chk,selectedRow,column);
////                        
////                    }
//                    /////////////////////////////
//
//                    /////////////////////////////
//
//                }
//            }
//        };
//        tblShare.getModel().addTableModelListener(tableModelListener);
//    }
    private void chkEligibleAmount() {
        double service_period = 0.0;
        String loaneeno = txtMemId.getText().trim();
        HashMap loaneeMap = new HashMap();
        loaneeMap.put("MEMBERSHIP_NO", loaneeno);
        List aList1 = ClientUtil.executeQuery("getDOB", loaneeMap);
        //System.out.println("aList1 aList1" + aList1);
        if (aList1.size() > 0 && aList1 != null) {
            HashMap dobMap1 = (HashMap) (aList1.get(0));
            Date joiningdt1 = (Date) dobMap1.get("JOINING_DATE");
            long a = DateUtil.dateDiff(joiningdt1, curr_dt);
            long b = a / 365;
            service_period = (double) b;
            String prdid = CommonUtil.convertObjToStr(observable.getCbmSchemName().getKeyForSelected());
            HashMap eligMap = new HashMap();
            eligMap.put("PROD_ID", prdid);
            eligMap.put("PERIOD", service_period);
            List eligibleList = ClientUtil.executeQuery("getServicewiseLoanEligibleAmount", eligMap);
            if (eligibleList != null && eligibleList.size() > 0) {
                HashMap amountMap = (HashMap) eligibleList.get(0);
                double min_amt = CommonUtil.convertObjToDouble(amountMap.get("ELG_MIN_AMOUNT"));
                double max_amt = CommonUtil.convertObjToDouble(amountMap.get("ELG_MAX_AMOUNT"));
                double amt = CommonUtil.convertObjToDouble(txtLoanAmt.getText());
                if (amt > min_amt) {
                    if (amt > max_amt) {
                        ClientUtil.showMessageWindow("Loan Amount is greater than the Limit \n"
                                + "Limit is " + min_amt + " - " + max_amt);
                        txtLoanAmt.setText("");
                        chrgTableEnableDisable();
                        return;
                    }
                }
            } else {
                ClientUtil.showMessageWindow("Loan Eligible amount is not set");
                return;
            }

        } else {
            ClientUtil.showMessageWindow("Please mark Membership no or 'Joining Date' in Customer screen ");
			return;
        }
    }

    private void txtMemIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMemIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMemIdActionPerformed

    private void cboSchemNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSchemNameActionPerformed
        // TODO add your handling code here:
        txtLoanAmt.setText("");
        if(CommonUtil.convertObjToStr(cboSchemName.getSelectedItem()).length() > 0){// Added by nithya on 14-02-2019 for KD 403 0019830: SHARE NOT ELIGIABLE FOR LOAN SANCTION 
            checkShareHolder();
        }
    }//GEN-LAST:event_cboSchemNameActionPerformed

    private void txtCustIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCustIdActionPerformed
       txtCustIdActionPerformed();
    }//GEN-LAST:event_txtCustIdActionPerformed
     private void txtCustIdActionPerformed()
{
    // TODO add your handling code here:
    HashMap whereMap1=new HashMap();
    whereMap1.put("CUST_ID",txtCustId.getText());
    HashMap resultMap=new HashMap();
    List appliedList = ClientUtil.executeQuery("getSelectappliedForLoanAppl", whereMap1);
     if (appliedList != null && appliedList.size() > 0) {
        resultMap = (HashMap) appliedList.get(0);
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
        if (resultMap.containsKey("STATUS_REGISTER") && resultMap.get("STATUS_REGISTER").equals("Received")) {
            ClientUtil.showAlertWindow("Loan Allready Applied");
            return;
        }
        }
    }
    HashMap casteMap = new HashMap();
    casteMap.put("CUSTID", txtCustId.getText());
    List casteList = ClientUtil.executeQuery("getSelectCasteForLoanAppl", casteMap);
    //System.out.println("casteList??>>" + casteList);
    if (casteList != null && casteList.size() > 0) {
        casteMap = (HashMap) casteList.get(0);
        String caste = CommonUtil.convertObjToStr(casteMap.get("CASTE"));
        String DOB = CommonUtil.convertObjToStr(casteMap.get("DOB"));
        String RetireDate = CommonUtil.convertObjToStr(casteMap.get("RETIREMENT_DT"));
        if (caste.equals("") || caste == null) {
            txtCaste.setText("Not Specified");
        } else {
            txtCaste.setText(caste);
        }
        if (DOB != null) {
            lblDobValue.setText(DOB);
        }
        if (RetireDate != null) {
            lblRetireDate.setText(RetireDate);
        }
    } else {
        txtCaste.setText("Not Specified");
    }
    if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")) {
        lblServicePeriod.setVisible(true);
        HashMap whereMap = new HashMap();
        whereMap.put("CUST_ID", txtCustId.getText());
        int serPeriod = 0;

        List serviceList = ClientUtil.executeQuery("getFutureServicePeriod", whereMap);
        if (serviceList != null && serviceList.size() > 0) {
            HashMap hMap = (HashMap) serviceList.get(0);
            String ser = "";
            if (hMap != null && hMap.size() > 0 && hMap.containsKey("SERVICE")) {
                ser = CommonUtil.convertObjToStr(hMap.get("SERVICE"));
                String[] arr = ser.split("  ");
                if (arr.length > 0) {
                    ser = arr[0];
                }
                if (arr.length > 1) {
                    //String mnth = arr[1].substring(0, arr[1].indexOf("M"));
                    ser = ser + arr[1];
                }
            }
            lblSeviceperiod.setText("ServicePeriod");
            lblServicePeriodValue.setText(CommonUtil.convertObjToStr(ser));
        }
    }
}
    private void txtCustIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCustIdFocusLost
        // TODO add your handling code here:
        if (txtCustId.getText() != null && !txtCustId.getText().equals("")) {
            getCustNo();
        }
        txtCustIdActionPerformed();
//        HashMap casteMap = new HashMap();
//        casteMap.put("CUSTID", txtCustId.getText());
//        List casteList = ClientUtil.executeQuery("getSelectCasteForLoanAppl", casteMap);
//        if (casteList != null && casteList.size() > 0) {
//            casteMap = (HashMap) casteList.get(0);
//            String caste = CommonUtil.convertObjToStr(casteMap.get("CASTE"));
//            if (caste == null) {
//                txtCaste.setText("Not Specified");
//            } else {
//                txtCaste.setText(caste);
//            }
//        }
    }//GEN-LAST:event_txtCustIdFocusLost

    public void getCustNo() {
        List clkno;
        HashMap where = new HashMap();
        HashMap map = new HashMap();
        HashMap returnMap = new HashMap();
        map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        where.put(CommonConstants.MAP_WHERE, map);
        clkno = (ClientUtil.executeQuery("getSelectCustmrData", where));
        //System.out.println("clock_no size..."+clkno.get(2));
        String custid = txtCustId.getText().trim();
        for (int j = 0; j < clkno.size(); j++) {
            HashMap newMap = new HashMap();
            newMap = (HashMap) clkno.get(j);
           if(newMap.get("CUST_ID")!=null){
            if (custid.equals(newMap.get("CUST_ID").toString())) {
                txtMemId.setText(CommonUtil.convertObjToStr(newMap.get("MEMBERSHIP_NO")));
                txtMemName.setText(CommonUtil.convertObjToStr(newMap.get("FNAME")));
                if (txtMemId.getText() != null && txtMemId.getText().length() > 0) {
                    btnMemLiability.setEnabled(true);
                }
            }}
            
        }
    }
    private void btnNewFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnNewFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_btnNewFocusLost
    //trans details
    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
        //   popUp("custmrData");
        viewType = "custmrData";
        new CheckCustomerIdUI(this);
     txtCustIdActionPerformed();
//        HashMap casteMap = new HashMap();
//        casteMap.put("CUSTID", txtCustId.getText());
//        List casteList = ClientUtil.executeQuery("getSelectCasteForLoanAppl", casteMap);
//        if (casteList != null && casteList.size() > 0) {
//            casteMap = (HashMap) casteList.get(0);
//            String caste = CommonUtil.convertObjToStr(casteMap.get("CASTE"));
//            if (caste == null) {
//                txtCaste.setText("Not Specified");
//            } else {
//                txtCaste.setText(caste);
//            }
//        }

    }//GEN-LAST:event_btnSearchActionPerformed

    private void txtApplNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtApplNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtApplNoActionPerformed

    private void btnDateChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDateChangeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDateChangeActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        //        btnView.setEnabled(false);
        //        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        //        popUp("Enquiry");
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnCancel.setEnabled(true);
        setButtons(false);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        btnCancel.setEnabled(true);
        setButtons(false);

    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed

        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnCancel.setEnabled(true);
        setButtons(false);
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            super.setOpenForEditBy(observable.getStatusBy());
		/*if (fromAuthorizeUI) {
                authorizeListUI.removeSelectedRow();
                this.dispose();
                authorizeListUI.setFocusToTable();
                authorizeListUI.displayDetails("Loan Application Register");
            }*/
        }
    }//GEN-LAST:event_btnAuthorizeActionPerformed

    public void authorizeStatus(String authorizeStatus) {
        if (!viewType.equals(AUTHORIZE)) {
            viewType = AUTHORIZE;
            //            setModified(true);
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            whereMap.put("TRANS_DT", curr_dt);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
              //  mapParam.put(CommonConstants.MAP_NAME, "getLoanApplicationAuthorizeList");
                mapParam.put(CommonConstants.MAP_NAME, "getLoanApplicationAuthorizeListForCashierAuth");
            } else {
                mapParam.put(CommonConstants.MAP_NAME, "getLoanApplicationAuthorizeListWithOutCahsier");
            }
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeLoanApplication");
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            observable.setStatus();

            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        } else if (viewType.equals(AUTHORIZE)) {
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);//commented on 13-Feb-2012
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, ClientUtil.getCurrentDate());
            singleAuthorizeMap.put("APPLICATION_NO", txtApplNo.getText());
            singleAuthorizeMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            ClientUtil.execute("authorizeLoanApplication", singleAuthorizeMap);
//            HashMap shareMap = new HashMap();
//            shareMap.put("OUTSTANDING_AMOUNT", reqShareAmt);
//            shareMap.put("AVAILABLE_NO_SHARES", availShareNo);
//            shareMap.put("MEM_ID", txtMemId.getText());
//            System.out.println("shareMap>>>>" + shareMap);
//            ClientUtil.executeQuery("updateShareAccInfoTOForLoanApplicationRegister", shareMap);
            //security details authorize
            singleAuthorizeMap.put("AUTHORIZESTATUS", authorizeStatus);
            singleAuthorizeMap.put("USER_ID", ProxyParameters.USER_ID);
            singleAuthorizeMap.put("AUTHORIZEDT", ClientUtil.getCurrentDate());
            singleAuthorizeMap.put("ACCT_NUM", txtApplNo.getText());
            singleAuthorizeMap.put("STATUS_DT",curr_dt);
            ClientUtil.execute("authorizeMemberDetails", singleAuthorizeMap);
            ClientUtil.execute("authorizeCollateralDetails", singleAuthorizeMap);
            ClientUtil.execute("authorizeSecurityTermLoanDepositType", singleAuthorizeMap);
            ClientUtil.execute("authorizeSecurityTermLoanLosType", singleAuthorizeMap);
            ClientUtil.execute("authorizeSecurityTermLoanGoldType", singleAuthorizeMap);
            ClientUtil.execute("authorizeVehicleDetails", singleAuthorizeMap);
            ClientUtil.execute("authorizeSalarySecurityDetails", singleAuthorizeMap);
            

            //security end..
                if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
                singleAuthorizeMap.put("SERVICE_TAX_AUTH", "SERVICE_TAX_AUTH");
            }
            // ClientUtil.ex
            observable.setAuthMap(singleAuthorizeMap);
            observable.doAction(authorizeStatus);
            // observable.execute(authorizeStatus);
            viewType = "";
            //            super.setOpenForEditBy(observable.getStatusBy());
            //            super.removeEditLock(txtTokenConfigId.getText());
             if (fromNewAuthorizeUI) {
                newauthorizeListUI.removeSelectedRow();
                this.dispose();
                newauthorizeListUI.setFocusToTable();
                if(isLoanAppln){
                 newauthorizeListUI.displayDetails("Loan Application Register");
                }
                else{
                  newauthorizeListUI.displayDetails("Loan Application With out Transaction");   
                }
                isLoanAppln=false;
            }
            if (fromAuthorizeUI) {
                authorizeListUI.removeSelectedRow();
                this.dispose();
                authorizeListUI.setFocusToTable();
                if(isLoanAppln){
                 authorizeListUI.displayDetails("Loan Application Register");
                }
                else{
                  authorizeListUI.displayDetails("Loan Application With out Transaction");   
                }
                isLoanAppln=false;
            }
            btnCancelActionPerformed(null);
            //  lblStatus.setText(authorizeStatus);
        }
        lblStatus.setText(observable.getLblStatus());
    }

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        mitCloseActionPerformed(evt);
    }//GEN-LAST:event_btnCloseActionPerformed

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {
        cifClosingAlert();
    }

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        transNew = false;
        btnSearch.setEnabled(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        //  callView( ClientConstants.ACTION_STATUS[3]);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_DELETE);
        observable.setStatus();
        popUp("Delete");
        lblStatus.setText(observable.getLblStatus());
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        transNew = false;
        btnSearch.setEnabled(true);
        //  txtLoanAmt.setEditable(false);
        //security details
        txtJewelleryDetails.setEnabled(true);
        txtGoldRemarks.setEnabled(true);
        txtNetWeight.setEnabled(true);
        txtGrossWeight.setEnabled(true);
        txtValueOfGold.setEnabled(true);

        txtSalaryCertificateNo.setEnabled(true);
        txtEmployerName.setEnabled(true);
        txtAddress.setEnabled(true);
        cboCity.setEnabled(true);
        txtPinCode.setEnabled(true);
        txtDesignation.setEnabled(true);
        txtContactNo.setEnabled(true);
        tdtRetirementDt.setEnabled(true);
        txtMemberNum.setEnabled(true);
        txtTotalSalary.setEnabled(true);
        txtNetWorth1.setEnabled(true);
        txtSalaryRemark.setEnabled(true);

        txtDepNo.setEnabled(true);
        txtDepAmount.setEnabled(true);
        txtRateOfInterest.setEnabled(true);
        txtMaturityDt.setEnabled(true);
        txtMaturityValue.setEnabled(true);
        //  cboProdId.setEnabled(true);
        //        cboProdType.setEnabled(true);
        tdtDepDt.setEnabled(true);


        cboLosOtherInstitution.setEnabled(true);
        txtLosName.setEnabled(true);
        txtLosAmount.setEnabled(true);
        txtLosMaturityValue.setEnabled(true);
        txtLosRemarks.setEnabled(true);
        txtLosSecurityNo.setEnabled(true);
        cboLosSecurityType.setEnabled(true);
        tdtLosIssueDate.setEnabled(true);
        tdtLosMaturityDate.setEnabled(true);

        enabledesableLOS(true);
        //security end..
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        // callView(ClientConstants.ACTION_STATUS[2]);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        popUp("Edit");
        txtLoanAmt.setEnabled(false);
        lblStatus.setText(observable.getLblStatus());

    }//GEN-LAST:event_btnEditActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        setModified(false);
        lblDobValue.setText("");
        lblRetireDate.setText("");
        tabLoan.setSelectedIndex(0);
        btnSearch.setEnabled(false);
        chrgTableEnableDisable();
        lblServicePeriod.setVisible(false);
        deletescreenLock();
        viewType = "CANCEL";
        observable.resetForm();
        observable.destroyObjects();
        ClientUtil.clearAll(this);
        setButtonEnableDisable();
        ClientUtil.enableDisable(this, false);
        lblStatus.setText("");
        setModified(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        observable.resetGoldTypeDetails();
        txtNextAccNo.setText("");
        txtNextAccNo.setEnabled(false);
        btnPrintCheck.setEnabled(false);
        lblServicePeriodValue.setText("");
        txtSalaryField.setText("");
        txtCostOfVehicleField.setText("");
        //  totalList=null;
        // tblShare.removeAll();
        // tblShare.remove(0);
        //  clearTblShare();
        txtCaste.setText("");
        cboPurposeCode.setSelectedIndex(0);
        // flag = 0;
        serviceTax_Map = new HashMap();
         if (fromNewAuthorizeUI) {
            this.dispose();
            fromNewAuthorizeUI= false;
            newauthorizeListUI.setFocusToTable();
        }
        if (fromAuthorizeUI) {
            this.dispose();
            fromAuthorizeUI = false;
            authorizeListUI.setFocusToTable();
        }
        if (fromCashierAuthorizeUI) {
            this.dispose();
            fromCashierAuthorizeUI = false;
            CashierauthorizeListUI.setFocusToTable();
        }
        if (fromManagerAuthorizeUI) {
            this.dispose();
            fromManagerAuthorizeUI = false;
            ManagerauthorizeListUI.setFocusToTable();
        }
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        setModified(false);
        HashMap hashmap = new HashMap();
        // double totAmount = 0.0, taxAmt = 0.0;
           if(totAmount>0.0){
                isTransaction ="Y";
           }
           else{
             isTransaction ="N"; 
           }
        updateOBFields();
        
        if ((observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) && cboPurposeCode.getSelectedItem().equals("") || cboPurposeCode.getSelectedItem().equals(null)) {
            ClientUtil.showMessageWindow("Please enter Purpose code");
            return;
        }
        //System.out.println("flag111@@@>>>>" + flag);
        if ((observable.getActionType() == ClientConstants.ACTIONTYPE_NEW)) {

            // if (yesNo == 0) {
            double depAmount = 0.0;
            double netWorth = 0.0;
            double memNetWorth = 0.0;
            double pledgeAmount = 0.0;
            double losAmount = 0.0;
            double vehicleNetworth=0.0;
            double valuOfGold = CommonUtil.convertObjToDouble(txtValueOfGold.getText()).doubleValue();
            for (int i = 0; i < tblSalary.getRowCount(); i++) {
                netWorth = netWorth + CommonUtil.convertObjToDouble(tblSalary.getValueAt(i, 5)).doubleValue();
                String custid = CommonUtil.convertObjToStr(tblSalary.getValueAt(i, 2));
                hashmap.put("CUST_ID", custid);
                hashmap.put("MEMBER_NO", custid);
                List lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashmap);
                if (lst1 != null && lst1.size() > 0) {
                    ClientUtil.displayAlert("Customer is death marked please select another customerId");
                    return;
                }
            }
            for (int j = 0; j < tblMemberType.getRowCount(); j++) {
                memNetWorth = memNetWorth + CommonUtil.convertObjToDouble(tblMemberType.getValueAt(j, 4)).doubleValue();
                String custid = CommonUtil.convertObjToStr(tblMemberType.getValueAt(j, 0));
                hashmap.put("CUST_ID", custid);
                hashmap.put("MEMBER_NO", custid);
                List lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashmap);
                if (lst1 != null && lst1.size() > 0) {
                    ClientUtil.displayAlert("Customer is death marked please select another customerId");
                    return;
                }
            }
             for (int j = 0; j < tblVehicleType.getRowCount(); j++) {
               vehicleNetworth= vehicleNetworth + CommonUtil.convertObjToDouble(tblVehicleType.getValueAt(j, 5)).doubleValue();
                String custid = CommonUtil.convertObjToStr(tblVehicleType.getValueAt(j, 0));
                hashmap.put("CUST_ID", custid);
                hashmap.put("MEMBER_NO", custid);
                List lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashmap);
                if (lst1 != null && lst1.size() > 0) {
                    ClientUtil.displayAlert("Customer is death marked please select another customerId");
                    return;
                }
            }
            for (int k = 0; k < tblCollateral.getRowCount(); k++) {
                pledgeAmount = pledgeAmount + CommonUtil.convertObjToDouble(tblCollateral.getValueAt(k, 3)).doubleValue();
                String custid = CommonUtil.convertObjToStr(tblCollateral.getValueAt(k, 0));
                hashmap.put("CUST_ID", custid);
                hashmap.put("MEMBER_NO", custid);
                List lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashmap);
                if (lst1 != null && lst1.size() > 0) {
                    ClientUtil.displayAlert("Customer is death marked please select another customerId");
                    return;
                }
            }

            for (int l = 0; l < tblDepositDetails.getRowCount(); l++) {
                String depNo = CommonUtil.convertObjToStr(tblDepositDetails.getValueAt(l, 1));
                String prodtype = CommonUtil.convertObjToStr(tblDepositDetails.getValueAt(l, 0));
                String securityAmt = txtLoanAmt.getText();
                // observable.setSecurityAmt(securityAmt);
                HashMap hmap = new HashMap();
                hmap.put("DEPOSIT_NO", depNo);
                if (prodtype.equals("TD") || prodtype.equals("Deposits")) {
                    List lst = ClientUtil.executeQuery("getAvailableBalForDep", hmap);
                    if (lst != null && lst.size() > 0) {
                        hmap = (HashMap) lst.get(0);
                        depAmount = depAmount + CommonUtil.convertObjToDouble(hmap.get("AVAILABLE_BALANCE")).doubleValue();
                        hmap.put("MEMBER_NO", hmap.get("CUST_ID"));
                        List lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashmap);
                        if (lst1 != null && lst1.size() > 0) {
                            ClientUtil.displayAlert("Customer is death marked please select another customerId");
                            return;
                        }
                    }
                } else {
                    String mdsNo = CommonUtil.convertObjToStr(tblDepositDetails.getValueAt(l, 1));
                    hashmap.put("CHITTAL_NO", mdsNo);

                    List lst1 = ClientUtil.executeQuery("getCustIdDeathChecking", hashmap);
                    if (lst1 != null && lst1.size() > 0) {
                        hashmap = (HashMap) lst1.get(0);
                        hashmap.put("MEMBER_NO", hashmap.get("CUST_ID"));
                        lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashmap);
                        if (lst1 != null && lst1.size() > 0) {
                            ClientUtil.displayAlert("Customer is death marked please select another customerId");
                            return;
                        }
                    }
                    depAmount += CommonUtil.convertObjToDouble(tblDepositDetails.getValueAt(l, 3)).doubleValue();

                }

            }
            for (int m = 0; m < tblLosDetails.getRowCount(); m++) {
                losAmount = losAmount + CommonUtil.convertObjToDouble(tblLosDetails.getValueAt(m, 4)).doubleValue();
            }
            double tot = valuOfGold + netWorth + memNetWorth + pledgeAmount + depAmount + losAmount + vehicleNetworth;
            double sanctionAmt = CommonUtil.convertObjToDouble(txtLoanAmts.getText()).doubleValue();
            if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")) {
                if (tot < sanctionAmt) {
                    ClientUtil.showAlertWindow("Security Amount is less than Applied Loan Amount...!!!!");
                    return;
                }
            } else {
                if (tot < sanctionAmt) {
                    int yesNo = 0;
                    String[] options = {"Yes", "No"};
                    yesNo = COptionPane.showOptionDialog(null, "Do you want security validation?", CommonConstants.WARNINGTITLE,
                            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                            null, options, options[0]);
                    if (yesNo == 0) {
                        ClientUtil.showMessageWindow("Please Enter the security details..!!!!");
                        tabLoan.setSelectedIndex(1);
                        return;
                    }
                }
            }
            //}
        }
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panLoan);
        //System.out.println("testing transactionUI.getOutputTO().size() :: " + transactionUI.getOutputTO());
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0) {
            displayAlert(mandatoryMessage);

        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW ||(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT && transactionUI.getOutputTO() != null && transactionUI.getOutputTO().size() > 0)) {
            int transactionSize = 0;
            if (transactionUI.getOutputTO().size() == 0 && CommonUtil.convertObjToDouble(lblTotalTransactionAmtVal.getText()).doubleValue() > 0) { //trans details
                ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
                return;
            } else {
                if ( CommonUtil.convertObjToDouble(lblTotalTransactionAmtVal.getText()).doubleValue() > 0) {
                    amtBorrow = CommonUtil.convertObjToDouble(txtLoanAmt.getText()).doubleValue();
                    transactionSize = (transactionUI.getOutputTO()).size();
                    if (transactionSize != 1 && CommonUtil.convertObjToDouble(lblTotalTransactionAmtVal.getText()).doubleValue() > 0) {
                        ClientUtil.showAlertWindow("Multiple Transactions are Not allowed, Make it one Transaction");
                        return;
                    } else {
                        observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                    }
                } else if (transactionUI.getOutputTO().size() > 0) {
                    observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                }
            }
            if (transactionSize == 0 && CommonUtil.convertObjToDouble(lblTotalTransactionAmtVal.getText()).doubleValue() > 0) {
                ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
                return;
            } else if (transactionSize != 0) {
                if (!transactionUI.isBtnSaveTransactionDetailsFlag()) {
                    ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.SAVE_TX_DETAILS));
                    return;
                }
                if (transactionUI.getOutputTO().size() > 0) {
                    observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                    savePerformed();
                    transactionUI.setButtonEnableDisable(true);
                    transactionUI.cancelAction(false);
                    transactionUI.resetObjects();
                    transactionUI.setCallingApplicantName("");
                    transactionUI.setCallingAmount("");
                }

            } else {
                savePerformed();
                totAmount=0.0;
            }

        } else {
            savePerformed();
            totAmount=0.0;
        }
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        clearTblModel();
        //end..
        btnPrintCheck.setEnabled(false);
        //   btnSearch.setEnabled(false);
        observable.resetGoldTypeDetails();
        tabLoan.setSelectedIndex(0);
        cboPurposeCode.setSelectedIndex(0);
    }//GEN-LAST:event_btnSaveActionPerformed
    private void clearTblModel() {
        String data[][] = null;
        String col[] = {"Member No", "Name", "Type of Member", "Contact No", "Networth"};
        model = new DefaultTableModel(data, col);
        tblMemberType.setModel(model);
    }

//    private void clearTblShare() {
//        String data[][] = null;
//        String col[] = {"Select", "Share Amount", "Membership Fee"};
//        model = new DefaultTableModel(data, col);
//        tblShare.setModel(model);
//    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        setModified(true);
        tabLoan.setSelectedIndex(0);
        lblDobValue.setText("");
        lblRetireDate.setText("");
        lblServicePeriodValue.setText("");
        txtCaste.setText("");
        txtNextAccNo.setText("");
        tdtApplDt.setDateValue(CommonUtil.convertObjToStr(curr_dt));
        tdtApplInwrdDt.setDateValue(CommonUtil.convertObjToStr(curr_dt));
        btnCollateralNew.setEnabled(true);
        btnMemberNew.setEnabled(true);
        btnSalaryNew.setEnabled(true);
        btnDepositNew.setEnabled(true);
        btnLosNew.setEnabled(true);
        lblServicePeriod.setVisible(false);
        txtFacility_Moratorium_Period.setEnabled(false);
        transNew = true;
        btnSearch.setEnabled(true);
        enableDisable(true);
        setButtonEnableDisable();
        // btnEmp.setEnabled(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        //trans details
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        //end..
        observable.resetForm();
        enableDisableSalaryDetails(false);//security details
        ClientUtil.enableDisable(panMemberDetails, false);//security details
        ClientUtil.enableDisable(panCollatetalDetails, false);//security details
        ClientUtil.enableDisable(panDepositDetails, false);//security details
        ClientUtil.enableDisable(panLosDetails, false);//security details
        enableDisableSalaryBtnsNew(true);//security details
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        panChargeDetails.setVisible(true);
        prodDesc = CommonUtil.convertObjToStr(cboSchemName.getModel().getSelectedItem());
        chrgTableEnableDisable();
        createChargeTable(prodDesc);
        // generateID();
        HashMap accNoMap = generateID();
        txtNextAccNo.setText((String) accNoMap.get(CommonConstants.DATA));
        txtNextAccNo.setEnabled(false);
        btnPrintCheck.setEnabled(true);
        //tblShare.removeAll();
        //tblShare.remove(0);
        //   clearTblShare();
        // flag = 0;
        cboPurposeCode.setSelectedIndex(0);
        txtFacility_Moratorium_Period.setEnabled(false);
        txtSalaryField.setText("");
        txtCostOfVehicleField.setText("");
        tdtFromDt.setDateValue(CommonUtil.convertObjToStr(curr_dt));
    }//GEN-LAST:event_btnNewActionPerformed

    public HashMap generateID() {
        HashMap hash = null, result = null;
        try {
            String mapName = "getCurrentID";
            HashMap where = new HashMap();
            where.put("ID_KEY", "APPLICATION_NO"); //Here u have to pass APPLICATION_NO or something else
            where.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            List list = null;
//            sqlMap.executeUpdate("updateIDGenerated", where);  // This update statement just updates curr_value=curr_value+1
            list = (List) ClientUtil.executeQuery(mapName, where);  // This will get u the updated curr_value, prefix and length
            //sqlMap.commitTransaction();

            if (list.size() > 0) {
                hash = (HashMap) list.get(0);
                String strPrefix = "", strLen = "";

                // Prefix for the ID.
                if (hash.containsKey("PREFIX")) {
                    strPrefix = (String) hash.get("PREFIX");
                    if (strPrefix == null || strPrefix.trim().length() == 0) {
                        strPrefix = "";
                    }
                }

                // Maximum Length for the ID
                int len = 10;
                if (hash.containsKey("ID_LENGTH")) {
                    strLen = String.valueOf(hash.get("ID_LENGTH"));
                    if (strLen == null || strLen.trim().length() == 0) {
                        len = 10;
                    } else {
                        len = CommonUtil.convertObjToInt(strLen.trim());
                    }
                }

                int numFrom = strPrefix.trim().length();

                String newID = String.valueOf(hash.get("CURR_VALUE"));
                long d = (long) Double.parseDouble(newID) + 1;
                newID = "";
                newID = "" + d;
                // Number Part of the String and incrementing 1 (ex. only 00085 from OGGOT00085)
                result = new HashMap();
                String genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
                result.put(CommonConstants.DATA, genID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed

    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed

    private void btnPrintCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintCheckActionPerformed
        // TODO add your handling code here:
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panLoan);
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0) {
            displayAlert(mandatoryMessage);
            return;
        }
        int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null, "Do you want to print ?", CommonConstants.WARNINGTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        if (yesNo == 0) {
            com.see.truetransact.clientutil.ttrintegration.TTIntegration ttIntgration = null;
            HashMap reportTransIdMap = new HashMap();
            // reportTransIdMap.put("MEMBER_ID", txtMemId.getText());
            reportTransIdMap.put("AppliedAmount", txtLoanAmt.getText());
            reportTransIdMap.put("AsOnDate", setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtApplDt.getDateValue())));
            String memberId = "";
            for (int i = 0; i < tblMemberType.getRowCount(); i++) {
                if (i == 0) {
                    memberId = tblMemberType.getValueAt(i, 0).toString();
                } else {
                    memberId = memberId + "," + tblMemberType.getValueAt(i, 0).toString();
                }
            }
            reportTransIdMap.put("SecText", memberId);
            reportTransIdMap.put("CustId", txtCustId.getText());
            reportTransIdMap.put("ProdId", CommonUtil.convertObjToStr(((ComboBoxModel) cboSchemName.getModel()).getKeyForSelected()));
            ttIntgration.setParam(reportTransIdMap);
            ttIntgration.integrationForPrint("LA_Verification", true);
        }

    }//GEN-LAST:event_btnPrintCheckActionPerformed
private void txtCasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCasteActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtCasteActionPerformed

    private void chkLoaneeSecurityonlyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkLoaneeSecurityonlyActionPerformed
        // TODO add your handling code here:
        if (chkLoaneeSecurityonly.isSelected() == true) {
            observable.setChkLoaneeSecurityonly(true);
        } else if (chkLoaneeSecurityonly.isSelected() == false) {
            observable.setChkLoaneeSecurityonly(false);
        }
    }//GEN-LAST:event_chkLoaneeSecurityonlyActionPerformed
    private void chkMoratorium_GivenStateChanged() {
        if (chkMoratorium_Given.isSelected()) {
            txtFacility_Moratorium_Period.setEnabled(true);

        } else {
            txtFacility_Moratorium_Period.setEnabled(false);
            txtFacility_Moratorium_Period.setText("");

        }
    }
    private void txtFacility_Moratorium_PeriodFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFacility_Moratorium_PeriodFocusLost
        // TODO add your handling code here:
        txtFacility_Moratorium_PeriodFocusLost();
    }//GEN-LAST:event_txtFacility_Moratorium_PeriodFocusLost
    private void txtFacility_Moratorium_PeriodFocusLost() {
        if (chkMoratorium_Given.isSelected()) {
            int period = CommonUtil.convertObjToInt(txtFacility_Moratorium_Period.getText());
            String periodStr = CommonUtil.convertObjToStr(txtFacility_Moratorium_Period.getText());
            if ((!periodStr.equals("")) && period <= 0) {
                ClientUtil.showAlertWindow("Morotorium Period should be More than Zero");
                txtFacility_Moratorium_Period.setText("");
                return;
            }
        }
//        tdtFDateFocusLost();
//        tdtTDateFocusLost();
//        observable.setTxtFacility_Moratorium_Period(txtFacility_Moratorium_Period.getText());
    }
    private void chkMoratorium_GivenStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_chkMoratorium_GivenStateChanged
        // TODO add your handling code here:
        //        chkMoratorium_GivenStateChanged();
    }//GEN-LAST:event_chkMoratorium_GivenStateChanged

    private void chkMoratorium_GivenItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkMoratorium_GivenItemStateChanged
        // TODO add your handling code here:
        chkMoratorium_GivenStateChanged();
    }//GEN-LAST:event_chkMoratorium_GivenItemStateChanged

    private void btnMemLiabilityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMemLiabilityActionPerformed
        if (txtMemId.getText() != null && txtMemId.getText().length() > 0) {
            TTIntegration ttIntgration = null;
            HashMap paramMap = new HashMap();
            paramMap.put("MemNo", txtMemId.getText());
            paramMap.put("AsOnDt", ClientUtil.getCurrentDate());
            ttIntgration.setParam(paramMap);
            ttIntgration.integration("MemberLiabilityRegisterDet");
        }else{
              ClientUtil.showAlertWindow("Enter a Valid MemberShip Number");
              return;
        }
    }//GEN-LAST:event_btnMemLiabilityActionPerformed

    private void txtSalaryFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSalaryFieldActionPerformed
       
    }//GEN-LAST:event_txtSalaryFieldActionPerformed

    private void txtSalaryFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSalaryFieldFocusLost
      double salary = CommonUtil.convertObjToDouble(txtSalaryField.getText());
        if (salary > 0) {
            HashMap eligMap = new HashMap();
            double totSalary = CommonUtil.convertObjToDouble(txtSalaryField.getText());
            eligMap.put("SALARY", totSalary);
            eligMap.put("MEMBER_NO", txtMemId.getText());
            eligMap.put("APPLIED_AMT", CommonUtil.convertObjToDouble(txtLoanAmt.getText()));
            eligMap.put("PROD_ID", ((ComboBoxModel) cboSchemName.getModel()).getKeyForSelected().toString());
            if (chkSettlement.isSelected()) {
                eligMap.put("SETTILEMENT", "RENEW");
            } else {
                eligMap.put("SETTILEMENT", "NEW");
            }
            if (txtCostOfVehicleField.getText() != null) {
                eligMap.put("COST_OF_VEHICLE", CommonUtil.convertObjToDouble(txtCostOfVehicleField.getText()));
            } else {
                eligMap.put("COST_OF_VEHICLE", 0.0);
            }
            List eligibleList = ClientUtil.executeQuery("getEligibleAmount", eligMap);
            if (eligibleList.size() > 0 && eligibleList != null) {
                HashMap resultMap = new HashMap();
                resultMap = (HashMap) eligibleList.get(0);
                String displayStr = "";
                displayStr += "Existing Loan Balance  : " + resultMap.get("EXISTINGLOANBAL") + "\n"
                        + "EligbleAmount          : Rs " + resultMap.get("ELIGIBLEAMT") + "\n"
                        + "Salary NetWorth        : Rs " + resultMap.get("NETWORTH") + "\n"
                        + "Share ToBeRecovered    : Rs " + resultMap.get("TOBERECOVERED") + "\n"
                        + "Net Amount Recivable    : Rs " + resultMap.get("NETAMTRECBLE") + "\n";



                if (!displayStr.equals("")) {
                    ClientUtil.showMessageWindow("" + displayStr);
                }
                double elgibleamount = CommonUtil.convertObjToDouble(resultMap.get("ELIGIBLEAMT"));
                if (elgibleamount <= 0) {
                    ClientUtil.showMessageWindow("Customer Not Eligible For Loan");
                    txtLoanAmt.setText("");
                    return;
                } else {
                    if (CommonUtil.convertObjToDouble(txtLoanAmt.getText()) > elgibleamount) {
                        txtLoanAmts.setText(CommonUtil.convertObjToStr(elgibleamount));
                    } else {
                        txtLoanAmts.setText(txtLoanAmt.getText());
                    }
                }
            }
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSalaryFieldFocusLost

    private void txtCostOfVehicleFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCostOfVehicleFieldFocusLost
        double costVehicle=CommonUtil.convertObjToDouble(txtCostOfVehicleField.getText());
        if(costVehicle>0){
          HashMap eligMap=new HashMap();
         double totSalary=CommonUtil.convertObjToDouble(txtSalaryField.getText());
         eligMap.put("SALARY",totSalary);
         eligMap.put("MEMBER_NO",txtMemId.getText());
         eligMap.put("APPLIED_AMT",CommonUtil.convertObjToDouble(txtLoanAmt.getText()));
         eligMap.put("PROD_ID",((ComboBoxModel)cboSchemName.getModel()).getKeyForSelected().toString());
         if(chkSettlement.isSelected()){
            eligMap.put("SETTILEMENT", "RENEW");
        } else {
            eligMap.put("SETTILEMENT", "NEW");
        }
        eligMap.put("COST_OF_VEHICLE",CommonUtil.convertObjToDouble(txtCostOfVehicleField.getText()));
        List eligibleList = ClientUtil.executeQuery("getEligibleAmount", eligMap);
        if (eligibleList.size() > 0 && eligibleList != null) {
            HashMap resultMap = new HashMap();
            resultMap = (HashMap) eligibleList.get(0);
            double vehicleamount = CommonUtil.convertObjToDouble(resultMap.get("VEHICLEAMT"));
             String displayStr = "";
            displayStr += "EligbleAmount          : Rs " +vehicleamount+ "\n";
            if (!displayStr.equals("")) {
                ClientUtil.showMessageWindow("" + displayStr);
            }
               if (vehicleamount<= 0) {
                ClientUtil.showMessageWindow("Customer Not Eligible For Loan");
                txtLoanAmt.setText("");
                return;
            } else {
                if (CommonUtil.convertObjToDouble(txtLoanAmt.getText()) >vehicleamount) {
                    txtLoanAmts.setText(CommonUtil.convertObjToStr(vehicleamount));
                } else {
                    txtLoanAmts.setText(txtLoanAmt.getText());
                }
            }
        }}
        
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostOfVehicleFieldFocusLost

    private void txtCostOfVehicleFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCostOfVehicleFieldActionPerformed
       
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostOfVehicleFieldActionPerformed
    /**
     * To populate Comboboxes
     */
    private void initComponentData() {
        cboSchemName.setModel(observable.getCbmSchemName());
        cboPurposeCode.setModel(observable.getCbmPurposeCode());
        cboRepayFreq_Repayment.setModel(observable.getCbmRepayFreq_Repayment());
        cboRepayType.setModel(observable.getCbmRepayType());
        //security details
        //System.out.println("observable.getCbmSecurityCity()>>>>" + observable.getCbmSecurityCity());
        cboCity.setModel(observable.getCbmSecurityCity());
        cboNature.setModel(observable.getCbmNature());
        cboRight.setModel(observable.getCbmRight());
        cboPledge.setModel(observable.getCbmPledge());
        // System.out.println("observable.getCbmDocumentType()>>>>" + observable.getCbmDocumentType());
        cboDocumentType.setModel(observable.getCbmDocumentType());
        // System.out.println("observable.getCbmProdTypeSecurity()>>>>" + observable.getCbmProdTypeSecurity());
        cboProductTypeSecurity.setModel(observable.getCbmProdTypeSecurity());
        // System.out.println("observable.getCbmDepProdID())>>>>" + observable.getCbmDepProdID());
        cboDepProdType.setModel(observable.getCbmDepProdID());
        // System.out.println("observable.getCbmLosSecurityType()>>>>" + observable.getCbmLosSecurityType());
        cboLosSecurityType.setModel(observable.getCbmLosSecurityType());
        // System.out.println("observable.getCbmLosInstitution()>>>>" + observable.getCbmLosInstitution());
        cboLosOtherInstitution.setModel(observable.getCbmLosInstitution());
  
        //security end..

        //        cboRoleCurrBran.setModel(observable.getCbmRoleInCurrBran());
        //        cboRoleTransBran.setModel(observable.getCbmRoleInTranBran());
        //        cboTransBran.setModel(observable.getCbmTransferBran());
    }

    /**
     * To display a popUp window for viewing existing data
     */
    private void popUp(String currAction) {
        viewType = currAction;
        HashMap viewMap = new HashMap();
        if (currAction.equalsIgnoreCase("Edit")) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            // map.put("BRANCH_CODE","0001");
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getLoanApplicationEdit");
        } else if (currAction.equalsIgnoreCase("Delete")) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getLoanApplicationEdit");
        } else if (currAction.equalsIgnoreCase("custmrData")) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            //   viewMap.put(CommonConstants.MAP_NAME, "getSelectCustmrData");

            /*
             * if ((currAction == "custmrData")){ StringBuffer presentCust = new
             * StringBuffer(); int jntAccntTablRow = tblAct_Joint.getRowCount();
             * if(tblAct_Joint.getRowCount()!=0) { for(int i =0,
             * sizeJointAcctAll =
             * tblAct_Joint.getRowCount();i<sizeJointAcctAll;i++){ if(i==0 ||
             * i==sizeJointAcctAll){ presentCust.append("'" +
             * CommonUtil.convertObjToStr(tblAct_Joint.getValueAt(i, 1)) + "'");
             * } else{ presentCust.append("," + "'" +
             * CommonUtil.convertObjToStr(tblAct_Joint.getValueAt(i, 1)) + "'");
             * } } } }
             */
        } else if (currAction.equalsIgnoreCase("SUB_MEMBER_NO")) {  //security details
            viewMap.put(CommonConstants.MAP_NAME, "getMemeberShipDetails");
        } 
        else if (currAction.equalsIgnoreCase("SUB_MEMBER_NO_VEHICLE")) {  //security details
            viewMap.put(CommonConstants.MAP_NAME, "getMemeberShipDetails");
        } 
        else if (currAction.equalsIgnoreCase("OWNER_MEMBER_NO_GAHAN")) {
            viewMap.put(CommonConstants.MAP_NAME, "getMemeberShipDetailsFromGahan");

        } else if (currAction.equalsIgnoreCase("OWNER_MEMBER_NO")) {
            viewMap.put(CommonConstants.MAP_NAME, "getMemeberShipDetails");
        } else if (currAction.equalsIgnoreCase("DOCUMENT_NO")) {
            HashMap whereListMap = new HashMap();
            whereListMap.put("ENTERED_DOCUMENT_NO", verifyDocNo().toString());
            //Added By Suresh
            if (txtOwnerMemNo.getText().length() > 0) {
                whereListMap.put("MEMBERSHIP_NO", txtOwnerMemNo.getText());
            }
            viewMap.put(CommonConstants.MAP_NAME, "getGahanDetailsforLoan");
            viewMap.put(CommonConstants.MAP_WHERE, whereListMap);
        } else if (currAction.equalsIgnoreCase("DEPOSIT_ACC_NO")) {
            HashMap whereMap = new HashMap();
            whereMap.put("SELECTED_BRANCH", ProxyParameters.BRANCH_ID);
            // String prodid = ((ComboBoxModel)cboProdId.getModel()).getKeyForSelected().toString();
            // whereMap.put("PROD_ID",prodid);
            whereMap.put("PROD_ID", CommonUtil.convertObjToStr(observable.getCbmDepProdID().getKeyForSelected()));
            String prodType = ((ComboBoxModel) cboProductTypeSecurity.getModel()).getKeyForSelected().toString();
            if (prodType.equals("TD")) {
                if (observable.isChkLoaneeSecurityonly() == true) {
                     whereMap.put("PRODUCT_ID", CommonUtil.convertObjToStr(observable.getCbmSchemName().getKeyForSelected()));
                    viewMap.put(CommonConstants.MAP_NAME, "getMasterDepositNoForLoanee");
                } else {
                    viewMap.put(CommonConstants.MAP_NAME, "getMDSMasterDepositNo");
                }
            } else {
                if (observable.isChkLoaneeSecurityonly() == true) {
                    whereMap.put("cust_id", txtCustId.getText());
                    whereMap.put("PRODUCT_ID", CommonUtil.convertObjToStr(observable.getCbmSchemName().getKeyForSelected()));
                    viewMap.put(CommonConstants.MAP_NAME, "getMDSChittalNoNew");
                } else {
                    viewMap.put(CommonConstants.MAP_NAME, "getMDSChittalNo");
                }
            }
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        } else if (currAction.equalsIgnoreCase("Enquiry")) { //security end
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getEmpTransferDetailsView");
        }
        new ViewAll(this, viewMap).show();

    }

    //security details
    private StringBuffer verifyDocNo() {
        StringBuffer addExistDoc = new StringBuffer();
        int count = tblCollateral.getRowCount();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                if (i == 0) {
                    addExistDoc.append("'" + CommonUtil.convertObjToStr(tblCollateral.getValueAt(i, 2)) + "'");
                } else {
                    addExistDoc.append("," + "'" + CommonUtil.convertObjToStr(tblCollateral.getValueAt(i, 2)) + "'");
                }
            }
        }
        return addExistDoc;
    }
    //security end..

    /**
     * Called by the Popup window created thru popUp method
     */
    public void fillData(Object map) {
        try {
            setModified(true);
            HashMap hash = (HashMap) map;
             if (hash!=null && hash.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")) {
                isLoanAppln=false;
                if(hash!=null && hash.containsKey("LOAN_APPLICATION")){
                    isLoanAppln=true;
                }
                fromNewAuthorizeUI = true;
                newauthorizeListUI = (NewAuthorizeListUI) hash.get("PARENT");
                hash.remove("PARENT");
                viewType = AUTHORIZE;
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                observable.setStatus();
                transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
                btnSave.setEnabled(false);
                btnCancel.setEnabled(true);
                btnAuthorize.setEnabled(true);
                btnReject.setEnabled(false);
                rejectFlag = 1;
            }
            if (hash!=null && hash.containsKey("FROM_AUTHORIZE_LIST_UI")) {
                isLoanAppln=false;
                if(hash!=null && hash.containsKey("LOAN_APPLICATION")){
                    isLoanAppln=true;
                }
                fromAuthorizeUI = true;
                authorizeListUI = (AuthorizeListUI) hash.get("PARENT");
                hash.remove("PARENT");
                viewType = AUTHORIZE;
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                observable.setStatus();
                transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
                btnSave.setEnabled(false);
                btnCancel.setEnabled(true);
                btnAuthorize.setEnabled(true);
                btnReject.setEnabled(false);
                rejectFlag = 1;
            }
            if (hash!=null && hash.containsKey("FROM_CASHIER_AUTHORIZE_LIST_UI")) {
                fromCashierAuthorizeUI = true;
                CashierauthorizeListUI = (AuthorizeListCreditUI) hash.get("PARENT");
                hash.remove("PARENT");
                viewType = AUTHORIZE;
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                observable.setStatus();
                transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
                btnSave.setEnabled(false);
                btnCancel.setEnabled(true);
                btnAuthorize.setEnabled(true);
                btnReject.setEnabled(false);
                rejectFlag = 1;
            }
            if (hash!=null && hash.containsKey("FROM_MANAGER_AUTHORIZE_LIST_UI")) {
                fromManagerAuthorizeUI = true;
                ManagerauthorizeListUI = (AuthorizeListDebitUI) hash.get("PARENT");
                hash.remove("PARENT");
                viewType = AUTHORIZE;
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                observable.setStatus();
                transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
                btnSave.setEnabled(false);
                btnCancel.setEnabled(true);
                btnAuthorize.setEnabled(true);
                btnReject.setEnabled(false);
                rejectFlag = 1;
            }
            if (viewType != null) {
                if (viewType.equals(ClientConstants.ACTION_STATUS[2])
                        || viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE)
                        || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                    hash.put(CommonConstants.MAP_WHERE, hash.get("APPLICATION_NO"));
                    hash.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                    observable.getData(hash);
                    tblMemberType.setModel(observable.getTblMemberTypeDetails());
                    tblCollateral.setModel(observable.getTblCollateralDetails());
                    tblDepositDetails.setModel(observable.getTblDepositTypeDetails());
                    tblLosDetails.setModel(observable.getTblLosTypeDetails());
                    tblSalary.setModel(observable.getTblSalarySecrityTable());
                    tblVehicleType.setModel(observable.getTblVehicleTypeDetails());
                    if (viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE)
                            || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                        ClientUtil.enableDisable(panLoan, false);

                    } else {
                        ClientUtil.enableDisable(panLoan, true);

                    }
                    setButtonEnableDisable();
                    if (viewType == AUTHORIZE) {
                        btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                        btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                        btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                        btnAuthorize.setEnabled(true);
                        btnAuthorize.requestFocusInWindow();
                        btnAuthorize.setFocusable(true);
                        editChargeTable();
                        editChargeAmount();
                    }
                    //Added By Suresh
//|| viewType.equals(AUTHORIZE)
                    if (viewType.equals("Edit") || viewType.equals("Delete")
                            || viewType.equals("Enquirystatus")) {
                        prodDesc = CommonUtil.convertObjToStr(cboSchemName.getModel().getSelectedItem());
                        editChargeTable();
                        if (tableFlag) {
                            if (viewType.equals("Edit")) {
                                editChargeAmount();                                
                                panChargeDetails.setEnabled(true);
                                srpChargeDetails.setEnabled(true);
                                table.setEnabled(true);
                                for (int i = 0; i < table.getRowCount(); i++) {
                                    boolean chkVal = ((Boolean) table.getValueAt(i, 0)).booleanValue();
                                    if (!chkVal) {
                                        table.isCellEditable(i, 0);
                                       
                                    }
                                }
                            
                            }else{
                            editChargeAmount();
                            panChargeDetails.setEnabled(false);
                            srpChargeDetails.setEnabled(false);
                            table.remove(0);
                            table.setEnabled(false);
                            }
                            
                        }
                    }
                }
                //added by vivek
                if (viewType.equals("custmrData")) {
                    if (!hash.get("STATUS").toString().equals("CLOSED")) {
                        txtCustId.setText(CommonUtil.convertObjToStr(hash.get("CUST_ID")));
                        txtMemName.setText(CommonUtil.convertObjToStr(hash.get("NAME")));
                        // txtMemName.setText((hash.get("NAME").toString()));
                        txtMemId.setText(CommonUtil.convertObjToStr(hash.get("MEMBER_NO")));
                        //  lblBranName.setText(CommonUtil.convertObjToStr(hash.get("BRANCH_NAME")));
                    } else {
                        ClientUtil.showMessageWindow("Customer is not a Member.Cannot Apply for Loan");
                        btnNewActionPerformed(null);
                    }
                }

                //security details
                if (viewType == "SUB_MEMBER_NO") {
                    String memberNo = txtMemNo.getText();
                    if (tblMemberType.getRowCount() > 0) {
                        for (int i = 0; i < tblMemberType.getRowCount(); i++) {
                            String membNo = CommonUtil.convertObjToStr(tblMemberType.getValueAt(i, 0));
                            //comm by jiby
//                            if (memberNo.equalsIgnoreCase(membNo) && !updateMode) {
//                                ClientUtil.displayAlert("Member No Already Exists in this Table");
//                                resetMemberTypeDetails();
//                                btnSecurityMember(false);
//                                btnMemberNew.setEnabled(true);
//                                btnMemNo.setEnabled(false);
//                                ClientUtil.enableDisable(panMemberDetails, false);
//                                return;
//                            }
                            //
                        }
                    }
                    txtMemNo.setText(CommonUtil.convertObjToStr(hash.get("MEMBER_NO")));
                    observable.setTxtMemNo(CommonUtil.convertObjToStr(hash.get("MEMBER_NO")));
                    txtMembName.setText(CommonUtil.convertObjToStr(hash.get("NAME")));
                    lblMemberRetireDate.setText(CommonUtil.convertObjToStr(hash.get("RETIREMENT_DT")));
                    observable.setTxtMembName(CommonUtil.convertObjToStr(hash.get("NAME")));
                   
                    hash.put("SHARE ACCOUNT NO", hash.get("MEMBER_NO"));
                    List lst = ClientUtil.executeQuery("getShareAccInfoTO", hash);
                    if (lst != null && lst.size() > 0) {
                        HashMap resultMap = (HashMap) lst.get(0);
                        txtMemType.setText(CommonUtil.convertObjToStr(resultMap.get("SHARE_TYPE")));
                        observable.setTxtMemNo(CommonUtil.convertObjToStr(resultMap.get("SHARE_TYPE")));
                    }
                }
                 if (viewType == "SUB_MEMBER_NO_VEHICLE") {
                    String memberNo = txtVehicleMemberNum.getText();
                    if (tblVehicleType.getRowCount() > 0) {
                        for (int i = 0; i < tblVehicleType.getRowCount(); i++) {
                            String membNo = CommonUtil.convertObjToStr(tblVehicleType.getValueAt(i, 0));
                        }
                    }
                    txtVehicleMemberNum.setText(CommonUtil.convertObjToStr(hash.get("MEMBER_NO")));
                    observable.setTxtVehicleMemNo(CommonUtil.convertObjToStr(hash.get("MEMBER_NO")));
                    txtVehicleMemberName.setText(CommonUtil.convertObjToStr(hash.get("NAME")));
                    lblVehicleMemRetireDate.setText(CommonUtil.convertObjToStr(hash.get("RETIREMENT_DT")));
                    observable.setTxtVehicleMembName(CommonUtil.convertObjToStr(hash.get("NAME")));
                   
                    hash.put("SHARE ACCOUNT NO", hash.get("MEMBER_NO"));
                    List lst = ClientUtil.executeQuery("getShareAccInfoTO", hash);
                    if (lst != null && lst.size() > 0) {
                        HashMap resultMap = (HashMap) lst.get(0);
                        txtVehicleMemType.setText(CommonUtil.convertObjToStr(resultMap.get("SHARE_TYPE")));
                        observable.setTxtVehicleMemType(CommonUtil.convertObjToStr(resultMap.get("SHARE_TYPE")));
                    }
                }
                if (viewType == "SUB_MEMBER_NO_FOCUS_LOST") {
                    txtMemNo.setText(CommonUtil.convertObjToStr(hash.get("MEMBERSHIP_NO")));
                    observable.setTxtMemNo(CommonUtil.convertObjToStr(hash.get("MEMBERSHIP_NO")));
                    txtMembName.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMER")));
                    observable.setTxtMemNo(CommonUtil.convertObjToStr(hash.get("CUSTOMER")));
                    txtMemType.setText(CommonUtil.convertObjToStr(hash.get("MEMBERSHIP_CLASS")));
                    observable.setTxtMemNo(CommonUtil.convertObjToStr(hash.get("MEMBERSHIP_CLASS")));
                }
                if (viewType == "OWNER_MEMBER_NO") {
                    String memberNo = txtOwnerMemNo.getText();
                    if (tblCollateral.getRowCount() > 0) {
                        for (int i = 0; i < tblCollateral.getRowCount(); i++) {
                            String membNo = CommonUtil.convertObjToStr(tblCollateral.getValueAt(i, 0));
//                            if (memberNo.equalsIgnoreCase(membNo) && !updateMode) {
//                                ClientUtil.displayAlert("Member No Already Exists in this Table");
//                                resetCollateralDetails();
//                                btnSecurityCollateral(false);
//                                btnCollateralNew.setEnabled(true);
//                                btnOwnerMemNo.setEnabled(false);
//                                ClientUtil.enableDisable(panCollatetalDetails, false);
//                                return;
//                            }
                        }
                    }
                    txtOwnerMemNo.setText(CommonUtil.convertObjToStr(hash.get("MEMBERSHIP_NO")));
                    observable.setDocGenId(CommonUtil.convertObjToStr(hash.get("DOCUMENT_GEN_ID")));
                    collateralJointAccountDisplay(txtOwnerMemNo.getText());
                    observable.setTxtOwnerMemNo(txtMemId.getText());//CommonUtil.convertObjToStr(hash.get("MEMBERSHIP_NO")));
                    txtOwnerMemberNname.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMER")));
                    observable.setTxtOwnerMemberNname(CommonUtil.convertObjToStr(hash.get("CUSTOMER")));
                    observable.setDocGenId("");
                }
                if (viewType == "OWNER_MEMBER_NO_GAHAN") {
                    String memberNo = txtOwnerMemNo.getText();
                    if (tblCollateral.getRowCount() > 0) {
                        for (int i = 0; i < tblCollateral.getRowCount(); i++) {
                            String membNo = CommonUtil.convertObjToStr(tblCollateral.getValueAt(i, 0));
//                            if (memberNo.equalsIgnoreCase(membNo) && !updateMode) {
//                                ClientUtil.displayAlert("Member No Already Exists in this Table");
//                                resetCollateralDetails();
//                                btnSecurityCollateral(false);
//                                btnCollateralNew.setEnabled(true);
//                                btnOwnerMemNo.setEnabled(false);
//                                ClientUtil.enableDisable(panCollatetalDetails, false);
//                                return;
//                            }
                        }
                    }
                    txtOwnerMemNo.setText(CommonUtil.convertObjToStr(hash.get("MEMBERSHIP_NO")));
                    observable.setDocGenId(CommonUtil.convertObjToStr(hash.get("DOCUMENT_GEN_ID")));
                    observable.setRdoGahanYes(true);
                    collateralJointAccountDisplay(txtOwnerMemNo.getText());
                    observable.setTxtOwnerMemNo(txtMemId.getText());//CommonUtil.convertObjToStr(hash.get("MEMBERSHIP_NO")));
                    txtOwnerMemberNname.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMER")));
                    observable.setTxtOwnerMemberNname(CommonUtil.convertObjToStr(hash.get("CUSTOMER")));
                    observable.setDocGenId("");
                }

                if (viewType == "DOCUMENT_NO") {
                    HashMap documentMap = new HashMap();
                    String documentNo = txtDocumentNo.getText();
                    double sanctionAmt = CommonUtil.convertObjToDouble(txtLoanAmt.getText()).doubleValue();
                    String loanExpiryDt = CommonUtil.convertObjToStr(tdtApplDt.getDateValue());
                    String loanStartDt = CommonUtil.convertObjToStr(tdtApplDt.getDateValue());

                    if (loanExpiryDt.length() == 0 || sanctionAmt <= 0) {
                        ClientUtil.displayAlert("Enter the Sanction Details ");
                        return;
                    }
                    documentMap.put("DOCUMENT_GEN_ID", CommonUtil.convertObjToStr(hash.get("DOCUMENT_GEN_ID")));
                    documentMap.put("DOCUMENT_NO", CommonUtil.convertObjToStr(hash.get("DOCUMENT_NO")));
                    documentMap.put("SANCTION_AMT", new Double(sanctionAmt));
                    documentMap.put("LOAN_EXPIRY_DT", DateUtil.getDateMMDDYYYY(loanExpiryDt));
                    documentMap.put("ACCT_OPEN_DT", DateUtil.getDateMMDDYYYY(loanStartDt));
                    if (getDocumentDetails(documentMap)) {
                        return;
                    }
                    //                tdtTDate.getText();

                    //                if(tblCollateral.getRowCount()>0) {
                    //                    for(int i=0;i<tblCollateral.getRowCount();i++){
                    //                        String membNo = CommonUtil.convertObjToStr(tblCollateral.getValueAt(i,0));
                    //                        if(memberNo.equalsIgnoreCase(membNo) && !updateMode) {
                    //                            ClientUtil.displayAlert("Member No Already Exists in this Table");
                    //                            resetCollateralDetails();
                    //                            btnSecurityCollateral(false);
                    //                            btnCollateralNew.setEnabled(true);
                    //                            btnOwnerMemNo.setEnabled(false);
                    //                            ClientUtil.enableDisable(panCollatetalDetails,false);
                    //                            return;
                    //                        }
                    //                    }
                    //                }
                    txtOwnerMemNo.setText(CommonUtil.convertObjToStr(hash.get("MEMBERSHIP_NO")));
                    observable.setTxtOwnerMemNo(CommonUtil.convertObjToStr(hash.get("MEMBERSHIP_NO")));
                    txtOwnerMemberNname.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMER")));
                    observable.setTxtOwnerMemberNname(CommonUtil.convertObjToStr(hash.get("CUSTOMER")));
                }
                if (viewType.equals("DEPOSIT_ACC_NO")) {
                    String prodType = ((ComboBoxModel) cboProductTypeSecurity.getModel()).getKeyForSelected().toString();
                    if (prodType.equals("TD")) {
                        txtDepNo.setText(CommonUtil.convertObjToStr(hash.get("ACT_NUM")));
                        HashMap whereMap = new HashMap();
                        whereMap.put("DEPOSIT NO", hash.get("ACT_NUM"));
                       
                        List accountLst = ClientUtil.executeQuery("getSelectDepSubNoAccInfoTO", whereMap);
                        if (accountLst != null && accountLst.size() > 0) {
                            whereMap = (HashMap) accountLst.get(0);
                            tdtDepDt.setDateValue(CommonUtil.convertObjToStr(whereMap.get("DEPOSIT_DT")));
                              if (observable.isChkLoaneeSecurityonly() == true) {
                           txtDepAmount.setText(CommonUtil.convertObjToStr(hash.get("ELIGIBLEAMOUNT")));  
                         }else{
                            txtDepAmount.setText(CommonUtil.convertObjToStr(hash.get("AMOUNT")));
                              }
                            txtRateOfInterest.setText(CommonUtil.convertObjToStr(whereMap.get("RATE_OF_INT")));
                            txtMaturityValue.setText(CommonUtil.convertObjToStr(whereMap.get("MATURITY_AMT")));
                            txtMaturityDt.setDateValue(CommonUtil.convertObjToStr(whereMap.get("MATURITY_DT")));
                        }
                    } else {
                        txtDepNo.setText(CommonUtil.convertObjToStr(hash.get("CHITTAL_NO")));
                        tdtDepDt.setDateValue(CommonUtil.convertObjToStr(hash.get("START_DT")));
                        txtDepAmount.setText(CommonUtil.convertObjToStr(hash.get("INST_AMT")));
                        txtMaturityValue.setText(CommonUtil.convertObjToStr(hash.get("PAID_AMOUNT")));
                        txtMaturityDt.setDateValue(CommonUtil.convertObjToStr(hash.get("END_DT")));
                    }
                    calculateTot();
                }
                //security end..
                if(viewType.equals("Edit") || viewType.equals("Delete")){
                    displayLoanApplication(hash);
                }
                // System.out.println("memname values..23333..>>>>>>>" + txtMembName.getText());
                if (viewType == AUTHORIZE || viewType.equals("Edit") || viewType.equals("Delete")) {
                    String batchid = CommonUtil.convertObjToStr(hash.get("APPLICATION_NO"));
                    getTransDetails(batchid);
                }

            }


            //         if(viewType.equals(ClientConstants.ACTION_STATUS[2]) || viewType.equals(AUTHORIZE)) {
            //            HashMap screenMap = new HashMap();
            //            screenMap.put("TRANS_ID",hash.get("EMP_TRANSFER_ID"));
            //            screenMap.put("USER_ID",ProxyParameters.USER_ID);
            //            screenMap.put("TRANS_DT", ClientUtil.getCurrentDate());
            //            screenMap.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
            //            List lst=ClientUtil.executeQuery("selectauthorizationLock", screenMap);
            //            if(lst !=null && lst.size()>0) {
            //                screenMap=null;
            //                StringBuffer open=new StringBuffer();
            //                for(int i=0;i<lst.size();i++){
            //                    screenMap=(HashMap)lst.get(i);
            //                    open.append("\n"+"User Id  :"+" ");
            //                    open.append(CommonUtil.convertObjToStr(screenMap.get("OPEN_BY"))+"\n");
            //                    open.append("Mode Of Operation  :" +" ");
            //                    open.append(CommonUtil.convertObjToStr(screenMap.get("MODE_OF_OPERATION"))+" ");
            //                    btnSave.setEnabled(false);
            //                    ClientUtil.enableDisable(panInward, false);
            //                    btnEmp.setEnabled(false);
            //                }
            //                ClientUtil.showMessageWindow("already open by"+open);
            //                return;
            //            }
            //            else{
            //                hash.put("TRANS_ID",hash.get("EMP_TRANSFER_ID"));
            //                if(viewType.equals(ClientConstants.ACTION_STATUS[2]))
            //                    hash.put("MODE_OF_OPERATION","EDIT");
            //                if(viewType==AUTHORIZE)
            //                    hash.put("MODE_OF_OPERATION","AUTHORIZE");
            //                   hash.put("USER_ID",TrueTransactMain.USER_ID);
            //                   hash.put("TRANS_DT", ClientUtil.getCurrentDate());
            //                   hash.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
            //                ClientUtil.execute("insertauthorizationLock", hash);
            //            }
            //        }
            //        if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            //            String lockedBy = "";
            //            HashMap Lockmap = new HashMap();
            //            Lockmap.put("SCREEN_ID", getScreenID());
            //            Lockmap.put("RECORD_KEY", CommonUtil.convertObjToStr(hash.get("EMP_TRANSFER_ID")));
            //            Lockmap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
            //            System.out.println("Record Key Map : " + Lockmap);
            //            List lstLock = ClientUtil.executeQuery("selectEditLock", Lockmap);
            //            if (lstLock.size() > 0) {
            //                lockedBy = CommonUtil.convertObjToStr(lstLock.get(0));
            //                if (!lockedBy.equals(ProxyParameters.USER_ID)) {
            //                    btnSave.setEnabled(false);
            //                    ClientUtil.enableDisable(panInward, false);
            //                    btnEmp.setEnabled(false);
            //                } else {
            //                    btnSave.setEnabled(true);
            //                    ClientUtil.enableDisable(panInward, true);
            //                    btnEmp.setEnabled(true);
            //                }
            //            } else {
            //                btnSave.setEnabled(true);
            //                ClientUtil.enableDisable(panInward, true);
            //                btnEmp.setEnabled(true);
            //            }
            //            setOpenForEditBy(lockedBy);
            //            if (lockedBy.length() > 0 && !lockedBy.equals(ProxyParameters.USER_ID)) {
            //                String data = getLockDetails(lockedBy, getScreenID()) ;
            //                ClientUtil.showMessageWindow("Selected Record is Opened/Modified by " + lockedBy + data.toString());
            //                btnSave.setEnabled(false);
            //                ClientUtil.enableDisable(panInward, false);
            //                btnEmp.setEnabled(false);
            //            }
            //        }

        } catch (Exception e) {
            log.error(e);
        }
        if (rejectFlag == 1) {
            btnReject.setEnabled(false);
        }
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
            btnAuthorize.setEnabled(true);
            btnAuthorize.requestFocusInWindow();
            btnAuthorize.setFocusable(true);
        }
    }

    //security details
    private boolean getDocumentDetails(HashMap documentMap) {
        List lst = ClientUtil.executeQuery("getSelectGahanDocumentDetails", documentMap);
        if (lst != null && lst.size() > 0) {
            HashMap dataMap = (HashMap) lst.get(0);
            Date gahanExpDt = (Date) dataMap.get("GAHAN_EXP_DT");
            Date gahanReleaseDt = (Date) dataMap.get("GAHAN_RELEASE_DT");
            double sanctionAmt = CommonUtil.convertObjToDouble(documentMap.get("SANCTION_AMT")).doubleValue();
            double pledgeAmt = CommonUtil.convertObjToDouble(dataMap.get("PLEDGE_AMT")).doubleValue();
            if (gahanExpDt != null) {
                if (DateUtil.dateDiff((Date) documentMap.get("ACCT_OPEN_DT"), gahanExpDt) <= 0) {
                    ClientUtil.displayAlert("Gahan has expired.. " + "\n" + " Select another Gahan number");
                    return true;
                }
                if (DateUtil.dateDiff((Date) documentMap.get("LOAN_EXPIRY_DT"), gahanExpDt) <= 0) {
                    ClientUtil.displayAlert("Gahan expires before the Loan Expiry Date. " + "\n" + " Choose another Gahan number");
                    return true;
                }
            }
            if (gahanReleaseDt != null) {
                if (DateUtil.dateDiff((Date) documentMap.get("ACCT_OPEN_DT"), gahanReleaseDt) <= 0) {
                    ClientUtil.displayAlert("Gahan has been Released on   :" + DateUtil.getStringDate(gahanReleaseDt) + "\n" + "Choose  another Gahan Number");
                    return true;
                }
            }

            updateGahandetails(dataMap);
            //            else if(sanctionAmt>pledgeAmt){
            //                  ClientUtil.displayAlert("Document Security not sufficient for loan");
            //                return true;
            //            }
        } else {
            return true;
        }
        return false;
    }

    private void updateGahandetails(HashMap map) {
        double pledgeAmt = 0;
        double actualPledge = 0;
        //        if(observable.isRdoGahanYes())
        //            rdoGahanYes.setSelected(observable.isRdoGahanYes());
        //        else
        //            rdoGahanNo.setSelected(observable.isRdoGahanNo());
        txtOwnerMemNo.setText(CommonUtil.convertObjToStr(map.get("NATURE")));
        txtOwnerMemberNname.setText(CommonUtil.convertObjToStr(map.get("NATURE")));
        txtDocumentNo.setText(CommonUtil.convertObjToStr(map.get("DOCUMENT_NO")));
        //        txtDocumentType.setText(CommonUtil.convertObjToStr(map.get("DOCUMENT_TYPE")));
        cboDocumentType.setSelectedItem(CommonUtil.convertObjToStr(map.get("DOCUMENT_TYPE")));
        tdtDocumentDate.setDateValue(CommonUtil.convertObjToStr(map.get("DOCUMENT_DT")));
        txtRegisteredOffice.setText(CommonUtil.convertObjToStr(map.get("REGISTRED_OFFICE")));
        cboPledge.setSelectedItem(CommonUtil.convertObjToStr(map.get("PLEDGE")));
        tdtPledgeDate.setDateValue(CommonUtil.convertObjToStr(map.get("PLEDGE_DT")));
        txtPledgeNo.setText(CommonUtil.convertObjToStr(map.get("PLEDGE_NO")));
        actualPledge = checkAvailableSecurity(CommonUtil.convertObjToStr(map.get("DOCUMENT_GEN_ID")));
        pledgeAmt = getGahanAvailableSecurity(actualPledge);
        txtPledgeAmount.setText(String.valueOf(pledgeAmt));
        txtVillage.setText(CommonUtil.convertObjToStr(map.get("VILLAGE")));
        txtSurveyNo.setText(CommonUtil.convertObjToStr(map.get("SARVEY_NO")));
        txtTotalArea.setText(CommonUtil.convertObjToStr(map.get("TOTAL_AREA")));
        cboRight.setSelectedItem(CommonUtil.convertObjToStr(map.get("RIGHT")));
        cboNature.setSelectedItem(CommonUtil.convertObjToStr(map.get("NATURE")));
        txtAreaParticular.setText(observable.getTxtAreaParticular());
        observable.setDocGenId(CommonUtil.convertObjToStr(map.get("DOCUMENT_GEN_ID")));
        observable.addPledgeAmountMap(CommonUtil.convertObjToStr(map.get("DOCUMENT_NO")), actualPledge);
    }

    private double getGahanAvailableSecurity(double maxsecurityAmt) {
        double availableSecuirty = 0;
        double sumGahanTableValue = 0;
        double loanAmt = 0;
        int count = tblCollateral.getRowCount();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                sumGahanTableValue += CommonUtil.convertObjToDouble(tblCollateral.getValueAt(i, 3)).doubleValue();
            }
        }
        loanAmt = CommonUtil.convertObjToDouble(txtLoanAmt.getText()).doubleValue();
        availableSecuirty = loanAmt - sumGahanTableValue;

        if (maxsecurityAmt >= availableSecuirty) {
            return availableSecuirty;
        } else {
            return maxsecurityAmt;
        }
    }

    private double checkAvailableSecurity(String docGenId) {
        HashMap map = new HashMap();
        double availableSecurity = 0;
        if (docGenId != null) {
            map.put("DOC_GEN_ID", docGenId);
            List lst = ClientUtil.executeQuery("getGahanAvailableSecurityforLoan", map);
            if (lst != null && lst.size() > 0) {
                map = (HashMap) lst.get(0);
                availableSecurity = CommonUtil.convertObjToDouble(map.get("AVAILABLE_SECURITY_VALUE")).doubleValue();
            }
        }
        return availableSecurity;
    }
    //security end..

    private void enableDisable(boolean yesno) {
        ClientUtil.enableDisable(this, yesno);
    }

    private void setButtonEnableDisable() {
        chrgTableEnableDisable();
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());

        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());

        btnAuthorize.setEnabled(btnNew.isEnabled());
        btnReject.setEnabled(btnNew.isEnabled());
        btnException.setEnabled(btnNew.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());

        txtApplNo.setEditable(false);
        txtMemId.setEditable(false);
        txtMemName.setEditable(false);


    }

    public void update(Observable observed, Object arg) {
        // removeRadioButtons();
        txtSalaryField.setText(CommonUtil.convertObjToStr(observable.getTxttotSalary()));
        txtCostOfVehicleField.setText(CommonUtil.convertObjToStr(observable.getTxtCostOfVehicle()));
        txtCustId.setText(observable.getTxtCustId());
        txtMemId.setText(observable.getTxtMemId());
        cboRepayType.setSelectedItem(CommonUtil.convertObjToStr(observable.getTxtRepayType()));
        txtNoInstall.setText(CommonUtil.convertObjToStr(observable.getNoOfInstallmnt()));
        txtFacility_Moratorium_Period.setText(CommonUtil.convertObjToStr(observable.getMoratoriumPeriod()));
        if (observable.getCboPurposeCode() != null || !observable.getCboPurposeCode().equals("")) {
            cboPurposeCode.setSelectedItem(observable.getCboPurposeCode().toString());
        }
        if (observable.getTxtMemId() != null || !observable.getTxtMemId().equals("")) {
            HashMap custNamMap = new HashMap();
            custNamMap.put("CUST_ID", observable.getTxtCustId());
            List custNamList = ClientUtil.executeQuery("getSelectCustmrNameData", custNamMap);
            if (custNamList != null && custNamList.size() > 0) {
                custNamMap = (HashMap) custNamList.get(0);
                if (custNamMap != null && custNamMap.containsKey("CUST_NAME")) {
                    txtMemName.setText(CommonUtil.convertObjToStr(custNamMap.get("CUST_NAME")));
                }
            }
        }
        // txtMemName.setText(observable.getTxtMemName());
//        txtApplNo.setVisible(true);
        txtApplNo.setText(observable.getTxtApplNo());
        if (observable.getTdtApplDt() != null) {
            tdtApplDt.setDateValue(getDtPrintValue(String.valueOf(observable.getTdtApplDt())));
        }
        if (observable.getTdtApplInwrdDt() != null) {
            tdtApplInwrdDt.setDateValue(getDtPrintValue(String.valueOf(observable.getTdtApplInwrdDt())));
        }
        cboSchemName.setSelectedItem(observable.getCboSchemName());
        txtLoanAmt.setText(CommonUtil.convertObjToStr(observable.getTxtLoanAmt()));
        //  txtSuretyName.setText(observable.getTxtSuretyName());
        txaRemarks.setText(observable.getTxaRemarks());
        lblStatus.setText(observable.getLblStatus());
        //security details
        txtJewelleryDetails.setText(observable.getTxtJewelleryDetails());
        txtGoldRemarks.setText(observable.getTxtGoldRemarks());
        txtNetWeight.setText(observable.getTxtNetWeight());
        txtValueOfGold.setText(observable.getTxtValueOfGold());
        txtGrossWeight.setText(observable.getTxtGrossWeight());
        //security end..
        lblServiceTaxval.setText(observable.getLblServiceTaxval());
        txtCustIdActionPerformed(null);
        txtLoanAmts.setText(CommonUtil.convertObjToStr(observable.getTxtEligibleAmt()));
        cboRepayFreq_Repayment.setSelectedItem(((ComboBoxModel) cboRepayFreq_Repayment.getModel()).getDataForKey(CommonUtil.convertObjToStr(observable.getCboRepayFreq_Repayment())));
        tdtFromDt.setDateValue(CommonUtil.convertObjToStr(observable.getTdtFromDt()));
        tdtDueDt.setDateValue(CommonUtil.convertObjToStr(observable.getTdtDueDt()));
        txtInstallAmount.setText(CommonUtil.convertObjToStr(observable.getTxtInstallAmount()));
    }

    public void updateOBFields() {

//        System.out.println("membshpFee in save>>>>" + membshpFee);
//        System.out.println("reqShareAmt in save>>>>" + reqShareAmt);
//        observable.setTxtMembShpFee(CommonUtil.convertObjToDouble(membshpFee));
//        observable.setTxtShareAmt(CommonUtil.convertObjToDouble(reqShareAmt));
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            observable.setCboPurposeCode(CommonUtil.convertObjToStr(cboPurposeCode.getSelectedItem()));
        }
        observable.setMoratoriumPeriod(CommonUtil.convertObjToStr(txtFacility_Moratorium_Period.getText()));
        observable.setTxtisTransaction(isTransaction);
        observable.setTxtRepayType(CommonUtil.convertObjToStr(cboRepayType.getSelectedItem()));
        observable.setNoOfInstallmnt(CommonUtil.convertObjToStr(txtNoInstall.getText()));
        observable.setTxtCustId(txtCustId.getText());
        observable.setTxtMemId(txtMemId.getText());
        observable.setTxtMemName(txtMemName.getText());
        observable.setTxtApplNo(txtApplNo.getText());
        observable.setTdtApplDt(getDateValue(tdtApplDt.getDateValue()));
        observable.setTdtApplInwrdDt(getDateValue(tdtApplInwrdDt.getDateValue()));
        observable.setCboSchemName((String) cboSchemName.getSelectedItem());
        observable.setTxtProdId(CommonUtil.convertObjToStr(((ComboBoxModel) cboSchemName.getModel()).getKeyForSelected()));
        observable.setTxtLoanAmt(CommonUtil.convertObjToDouble(txtLoanAmt.getText()));
        observable.setTxaRemarks(txaRemarks.getText());
        observable.setCboRegstrStatus((String) cboRegstrStatus.getSelectedItem());
        //security details
        observable.setTxtJewelleryDetails(txtJewelleryDetails.getText());
        observable.setTxtGrossWeight(txtGrossWeight.getText());
        observable.setTxtGoldRemarks(txtGoldRemarks.getText());
        observable.setTxtValueOfGold(txtValueOfGold.getText());
        observable.setTxtNetWeight(txtNetWeight.getText());
        //security end..
        //validation
        observable.setTdtFromDt(getDateValue(tdtFromDt.getDateValue()));
        observable.setTdtDueDt(getDateValue(tdtDueDt.getDateValue()));
        observable.setServiceTax_Map(serviceTax_Map);
        observable.setLblServiceTaxval(lblServiceTaxval.getText());
        observable.setTxttotSalary(CommonUtil.convertObjToDouble(txtSalaryField.getText()));
        observable.setScreen(this.getScreen());
        observable.setTxtEligibleAmt(CommonUtil.convertObjToDouble(txtLoanAmts.getText()));
        observable.setTxtCostOfVehicle(CommonUtil.convertObjToDouble(txtCostOfVehicleField.getText()));
        observable.setCboRepayFreq_Repayment(CommonUtil.convertObjToInt(((ComboBoxModel) cboRepayFreq_Repayment.getModel()).getKeyForSelected()));
        observable.setTxtInstallAmount(CommonUtil.convertObjToDouble(txtInstallAmount.getText()));
        //
    }

    private void savePerformed() {
        if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y") && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {

            if (!cboRepayType.getSelectedItem().equals("User Defined") && !cboRepayType.getSelectedItem().equals("Lump Sum")) {
                //  minperiodcalculation();
                if (txtNoInstall.getText().equals("") || txtNoInstall.getText() == null) {
                    ClientUtil.showMessageWindow("Please  Enter No of Installments ");
                    return;
                }
            }
        }
        // comentd by rishad 29/08/2016 as per discussion with prashant all checking doing at member saving
//        if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")) {
//            if (tblMemberType.getRowCount() > 0) {
//                boolean yes = chkAmount();
//                if (yes == false) {
//                    ClientUtil.showMessageWindow("Please correct Surety Details");
//                    return;
//                }
//            }
//
//
//        }
        updateOBFields();
        finalizeCharges();
        if (transNew || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            insertTransactionPart();
        }
        //trans detail authorize
        String action = "";
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            action = CommonConstants.TOSTATUS_INSERT;
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            action = CommonConstants.TOSTATUS_UPDATE;
            observable.setAuthorizeStatus(null);
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
            action = CommonConstants.TOSTATUS_DELETE;
        }
        //  saveAction(action);
        observable.doAction(action);
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            HashMap lockMap = new HashMap();
            ArrayList lst = new ArrayList();
            // lst.add("EMP_TRANSFER_ID");
            //lockMap.put("EMP_TRANSFER_ID",CommonUtil.convertObjToStr(lblEmpTransferID.getText()));
            // lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
         /*
             * if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW) {
             * // lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
             * System.out.println("proxy@@@@ggs"+observable.getProxyReturnMap());
             * if (observable.getProxyReturnMap()!=null) { if
             * (observable.getProxyReturnMap().containsKey("APPLICATION_NO")) {
             * displayTransDetail(observable.getProxyReturnMap());//trans
             * details
             * //lockMap.put("EMP_TRANSFER_ID",observable.getProxyReturnMap().get("VISIT_ID"));
             * } }
             *
             * }
             */
            /*
             * if(observable.getResult()!=ClientConstants.ACTIONTYPE_EDIT){
             * displayTransDetail(observable.getProxyReturnMap());//trans
             * details // lockMap.put("EMP_TRANSFER_ID",
             * observable.getTxtEmpTransferID()); }
             */
            if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().get("APPLICATION_NO") != null) {
                displayLoanApplication(observable.getProxyReturnMap());
            }
            //trans details
            if (observable.getProxyReturnMap().get("TRANSFER_TRANS_LIST") != null || observable.getProxyReturnMap().get("CASH_TRANS_LIST") != null) {
                displayTransDetail(observable.getProxyReturnMap());//trans details
            }
            //trans details end..
        /*
             * if(observable.getResult()!=ClientConstants.ACTIONTYPE_DELETE){
             * System.out.println("deleteeee"+observable.getResult());
             * System.out.println("deleteeee"+observable.getProxyReturnMap());
             * displayTransDetail(observable.getProxyReturnMap());//trans
             * details
             *
             * // lockMap.put("EMP_TRANSFER_ID",
             * observable.getTxtEmpTransferID()); }
             */
            // setEditLockMap(lockMap);
            ////  setEditLock();
            //  deletescreenLock();
        }
        observable.resetForm();
        enableDisable(false);
        setButtonEnableDisable();
        panChargeDetails.setVisible(false);
        lblStatus.setText(observable.getLblStatus());
        ClientUtil.enableDisable(this, false);
        observable.setResultStatus();
        lblStatus.setText(observable.getLblStatus());
        observable.destroyObjects();
        //__ Make the Screen Closable..
        setModified(false);
        ClientUtil.clearAll(this);
        observable.ttNotifyObservers();
        btnCancelActionPerformed(null);
    }

    private int minperiodcalculation() {
        int noOfinstmnts = 0;
        if (tdtFromDt.getDateValue() != null && !tdtFromDt.equals("")) {
            if (tdtDueDt.getDateValue() != null && !tdtDueDt.equals("")) {
                double[] s = new double[1];
                HashMap where = new HashMap();
                where.put("BRANCH_ID", "0001");
                double servicePeriod = 0.0;
                List serviceList = ClientUtil.executeQuery("getServiePeriod", where);
                if (serviceList != null && serviceList.size() > 0) {
                    HashMap serviceMap = (HashMap) serviceList.get(0);
                    servicePeriod = CommonUtil.convertObjToInt(serviceMap.get("EMP_SERVICE_PERIOD"));
                } else {
                    //ClientUtil.showMessageWindow("Please Mark 'EMP_SERVICE_PERIOD' ");
                    return noOfinstmnts;
                }
                for (int k = 0; k < tblMemberType.getRowCount() + 1; k++) {
                    String memno;
                    if (k == tblMemberType.getRowCount()) {
                        memno = CommonUtil.convertObjToStr(txtMemId.getText());
                    } else {
                        memno = CommonUtil.convertObjToStr(tblMemberType.getValueAt(k, 0));
                    }
                    HashMap memMap = new HashMap();
                    memMap.put("MEMBERSHIP_NO", memno);
                    List aList = ClientUtil.executeQuery("getDOB", memMap);
                    if (aList.size() > 0 && aList != null) {
                        HashMap dobMap = (HashMap) (aList.get(0));
                        Date dob = (Date) dobMap.get("DOB");
                        long a = DateUtil.dateDiff(dob, curr_dt);
                        long b = a / 365;
                        if (k == 0) {
                            s[0] = (double) b;
                            //  s[1]=0;
                        } else {
                            if (s[0] < (double) b) {
                                s[0] = (double) b;
                            }
                        }

                    } else {
                        //ClientUtil.showMessageWindow("Please Mark Date of Birth of the Member  "+memno);
                        return noOfinstmnts;
                    }
                }
                double least_period = servicePeriod - s[0];
                int close_before = 0;
                HashMap where1 = new HashMap();
                where.put("BRANCH_ID", "0001");
                List closeList = ClientUtil.executeQuery("getMaxSurety", where1);
                if (closeList.size() > 0 && closeList != null) {
                    HashMap clsMap = (HashMap) closeList.get(0);
                    close_before = CommonUtil.convertObjToInt(clsMap.get("CLOSE_BEFORE"));
                }
                double totmonths = least_period * 12;
                int totalmonths = (int) totmonths;
                int minPeriod = totalmonths - close_before;
                if (cboRepayType.getSelectedItem().equals("EMI")) {
                    noOfinstmnts = (int) minPeriod;
                }
                if (cboRepayType.getSelectedItem().equals("EQI")) {
                    noOfinstmnts = (int) minPeriod / 4;
                }
                if (cboRepayType.getSelectedItem().equals("EYI")) {
                    noOfinstmnts = (int) minPeriod / 12;
                }
                if (cboRepayType.getSelectedItem().equals("EHI")) {
                    noOfinstmnts = (int) minPeriod / 6;
                }
                if (cboRepayType.getSelectedItem().equals("Uniform Principle EMI")) {
                    noOfinstmnts = (int) minPeriod;
                }
                HashMap supMap = new HashMap();
                supMap.put("PROD_ID", CommonUtil.convertObjToStr(((ComboBoxModel) cboSchemName.getModel()).getKeyForSelected()));
                List lstSupName = ClientUtil.executeQuery("getMaxPeriodForSelectedItem", supMap);
                HashMap supMap1 = new HashMap();
                if (lstSupName != null && lstSupName.size() > 0) {
                    supMap1 = (HashMap) lstSupName.get(0);
                    int period = (CommonUtil.convertObjToInt(supMap1.get(("MAX_PERIOD"))));
                    int periodInMonths = period / 30;
                    if (noOfinstmnts > periodInMonths) {
                        noOfinstmnts = periodInMonths;
                    }
                }
            } else {
                ClientUtil.showMessageWindow("Please Select Due Date");
                return noOfinstmnts;
            }
        } else {
            ClientUtil.showMessageWindow("Please Select FirstInstallment Date");
            return noOfinstmnts;
        }
        return noOfinstmnts;
    }

    private void setFieldNames() {
        txtCustId.setName("txtCustId");
        txtMemId.setName("txtMemId");
        txtMemName.setName("txtMemName");
        txtApplNo.setName("txtApplNo");
        tdtApplDt.setName("tdtApplDt");
        tdtApplInwrdDt.setName("tdtApplInwrdDt");
        cboSchemName.setName("cboSchemName");
        txtLoanAmt.setName("txtLoanAmt");
//        txtSuretyName.setName("txtSuretyName");
        txaRemarks.setName("txaRemarks");
        cboRegstrStatus.setName("cboRegstrStatus");
        lblCustId.setName("lblCustId");
        lblMembrId.setName("lblMembrId");
        lblMembrName.setName("lblMembrName");
        lblApplNo.setName("lblApplNo");
        lblApplDate.setName("lblApplDate");
        lblApplInwardDate.setName("lblApplInwardDate");
        lblSchemName.setName("lblSchemName");
        lblLoanAmt.setName("lblLoanAmt");
//        lblSuretyName.setName("lblSuretyName");
        lblRemarks.setName("lblRemarks");
        lblRegStatus.setName("lblRegStatus");
        lblStatus.setName("lblStatus");
        lblSpace1.setName("lblSpace1");
        panLoanApplication.setName("panLoanApplication");
        panLoan.setName("panLoan");
        panRemarks.setName("panRemarks");
        lblDob.setName("lblDob");
        lblRetireMent.setName("lblRetireMent");

    }

    private void internationalize() {
        //        java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.customer.customeridchange.CustomerIdChangeRB", ProxyParameters.LANGUAGE);
        //        lblAccountNumber.setText(resourceBundle.getString("lblAccountNumber"));
        //        lblProd.setText(resourceBundle.getString("lblProd"));
        //        lblOldCustNum.setText(resourceBundle.getString("lblOldCustNum"));
        //        lblNewCustNum.setText(resourceBundle.getString("lblNewCustNum"));
        //        lblProdType.setText(resourceBundle.getString("lblProdType"));
    }

    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }

    private void deletescreenLock() {
        HashMap map = new HashMap();
        map.put("USER_ID", ProxyParameters.USER_ID);
        map.put("TRANS_DT", ClientUtil.getCurrentDate());
        map.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
        ClientUtil.execute("DELETE_SCREEN_LOCK", map);
    }

    private String getLockDetails(String lockedBy, String screenId) {
        HashMap map = new HashMap();
        StringBuffer data = new StringBuffer();
        map.put("LOCKED_BY", lockedBy);
        map.put("SCREEN_ID", screenId);
        java.util.List lstLock = ClientUtil.executeQuery("getLockedDetails", map);
        map.clear();
        if (lstLock.size() > 0) {
            map = (HashMap) (lstLock.get(0));
            data.append("\nLog in Time : ").append(map.get("LOCKED_TIME"));
            data.append("\nIP Address : ").append(map.get("IP_ADDR"));
            data.append("\nBranch : ").append(map.get("BRANCH_ID"));
        }
        lstLock = null;
        map = null;
        return data.toString();
    }

    //CHARGE DETAILS
    private String firstEnteredActNo() {
        String sbAcNo = COptionPane.showInputDialog(this, resourceBundle.getString("REMARK_TRANSFER_TRANS"));
        return sbAcNo;
    }

    private void insertTransactionPart() {
        HashMap singleAuthorizeMap = new HashMap();
        java.util.ArrayList arrList = new java.util.ArrayList();
        HashMap authDataMap = new HashMap();
        //  authDataMap.put("ACCT_NUM", observable.getTxtApplNo());
        arrList.add(authDataMap);
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            /*
             * 1 String [] debitType = {"Cash","Transfer"}; int option3 = 0 ;
             * if(option3 == 0){ String transType = ""; System.out.println("!!!
             * transType :"+transType); while
             * (CommonUtil.convertObjToStr(transType).length()==0) {
             * System.out.println("cvvbfgrfr>>>"+authDataMap); transType =
             * (String)COptionPane.showInputDialog(null,"Select Transaction
             * Type", "Transaction type", COptionPane.QUESTION_MESSAGE, null,
             * debitType, ""); if
             * (CommonUtil.convertObjToStr(transType).length()>0) {
             * System.out.println("hjkggcssasa>>>"+authDataMap);
             * authDataMap.put("TRANSACTION_PART","TRANSACTION_PART");
             * authDataMap.put("TRANS_TYPE",transType.toUpperCase());
             * authDataMap.put("LIMIT",txtLoanAmt.getText());
             * authDataMap.put("USER_ID",TrueTransactMain.USER_ID); //
             * authDataMap.put("STATUS",lblStatus.getText());
             * System.out.println("hjkggcssasa>>>2222aasvfd"+authDataMap); 1
             */
            /*
             * if(CommonUtil.convertObjToStr(transType.toUpperCase()).equals("CASH")){
             * boolean flag = true; do { String tokenNo =
             * COptionPane.showInputDialog(this,resourceBundle.getString("REMARK_CASH_TRANS"));
             * if (tokenNo != null && tokenNo.length()>0) { flag =
             * tokenValidation(tokenNo); }else{ flag = true; } if(flag ==
             * false){ ClientUtil.showAlertWindow("Token is invalid or not
             * issued for you. Please verify."); }else{
             * authDataMap.put("TOKEN_NO",tokenNo); } } while (!flag); }
             */
            /*
             * 2
             * if(CommonUtil.convertObjToStr(transType.toUpperCase()).equals("TRANSFER")){
             * boolean flag = true; do { String sbAcNo = firstEnteredActNo();
             * if(sbAcNo!=null && sbAcNo.length()>0){ flag =
             * checkingActNo(sbAcNo); if(flag == false && finalChecking ==
             * false){ ClientUtil.showAlertWindow("Account No is invalid, Please
             * enter correct no"); }else{
             * System.out.println("sbAcNo>>>>>>>>>>>"+sbAcNo);
             * authDataMap.put("CR_ACT_NUM",sbAcNo); } finalChecking = false; }
             * else { ClientUtil.showMessageWindow("Transaction Not Created");
             * flag = true; authDataMap.remove("TRANSACTION_PART"); } } while
             * (!flag); }
             * System.out.println("transop>>>>>>"+transactionUI.getOutputTO());
             * System.out.println("authmap>>>@@@2542"+authDataMap); 2
             */
            observable.setNewTransactionMap(transactionUI.getOutputTO());
        }//else{
        // transType = "Cancel";
        //}
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

    private boolean checkingActNo(String sbAcNo) {
        boolean flag = false;
        HashMap existingMap = new HashMap();
        existingMap.put("ACT_NUM", sbAcNo.toUpperCase());
        List mapDataList = ClientUtil.executeQuery("getAccNoDet", existingMap);
       // System.out.println("#### mapDataList :" + mapDataList);
        if (mapDataList != null && mapDataList.size() > 0) {
            existingMap = (HashMap) mapDataList.get(0);
            String[] obj5 = {"Proceed", "ReEnter"};
            int option4 = COptionPane.showOptionDialog(null, ("Please check whether Account No, Name coreect or not " + "\nOperative AcctNo is : " + existingMap.get("Account Number") + "\nCustomer Name :" + existingMap.get("Customer Name")), ("Transaction Part"),
                    COptionPane.YES_NO_CANCEL_OPTION, COptionPane.QUESTION_MESSAGE, null, obj5, obj5[0]);
            if (option4 == 0) {
                flag = true;
            } else {
                flag = false;
            }
        }
        return flag;
    }
    //CHARGE END..
    /*
     * //Added By Suresh if(!loanType.equals("LTD") &&
     * observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){ // Loan
     * Charges prodDesc =
     * CommonUtil.convertObjToStr(cboProductId.getModel().getSelectedItem());
     * System.out.println("#### prodDesc"+prodDesc); chrgTableEnableDisable();
     * createChargeTable(prodDesc); chargeAmount(); }
     */
    //Added By Suresh CHARGE_AMOUNT_START

    private void createChargeTable(String prodDesc) {
        HashMap tableMap = buildData(prodDesc);
        ArrayList dataList = new ArrayList();
        dataList = (ArrayList) tableMap.get("DATA");
        if (dataList != null && dataList.size() > 0) {
            tableFlag = true;
            ArrayList headers;
            panChargeDetails.setVisible(true);
            SimpleTableModel stm = new SimpleTableModel((ArrayList) tableMap.get("DATA"), (ArrayList) tableMap.get("HEAD"));
            table = new JTable(stm);
            lblTotalTransactionAmtVal.setText("0.0");
            table.setSize(430, 110);
            srpChargeDetails = new javax.swing.JScrollPane(table);
            srpChargeDetails.setMinimumSize(new java.awt.Dimension(430, 110));
            srpChargeDetails.setPreferredSize(new java.awt.Dimension(430, 110));
            panChargeDetails.add(srpChargeDetails, new java.awt.GridBagConstraints());
            table.revalidate();
            //charge details
            //calculating total of selected amount akkuu
            table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                   
                    if (tableFlag == true) {
                     tableMouseClicked();
                    }
                }
            });
            table.addKeyListener(new java.awt.event.KeyAdapter() {

                public void keyReleased(java.awt.event.KeyEvent e) {
                    if (tableFlag == true) {
                        tableMouseClicked();
                    }
                }
            });
            //akkuu

        } else {
            tableFlag = false;
            chrgTableEnableDisable();
        }

    }

       
        
    private void chrgTableEnableDisable() {
        lblTotalTransactionAmtVal.setText(null);
        tableFlag = false;
        panChargeDetails.removeAll();
        panChargeDetails.setVisible(false);
    }

    private HashMap buildData(String prodDesc) {
        HashMap whereMap = new HashMap();
        whereMap.put("SCHEME_ID", prodDesc);
        whereMap.put("DEDUCTION_ACCU", "R");

        List list = ClientUtil.executeQuery("getChargeDetailsData", whereMap);
        //System.out.println("list...>>>>>>>>" + list);
        //System.out.println("list size...>>>>>>>>" + list.size());
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
        //System.out.println("map in creating charges...." + map);
        return map;
    }

    //this function added by Anju Anand for Mantid Id: 0010365: IMBP Limit Setting
    private void setImbpSettings() {
        if (cboSchemName.getSelectedItem().equals("") || txtCustId.getText().equals("")) {
            ClientUtil.showMessageWindow("Please enter all the necessary details..!!!");
            txtLoanAmt.setText("");
            return;
        } else {
            String prodId = "";
            prodId = CommonUtil.convertObjToStr(((ComboBoxModel) cboSchemName.getModel()).getKeyForSelected());
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
                custId = txtCustId.getText();
                String shareNo = "";
                shareNo = txtMemId.getText();
                dataMap.put("SHARE_ACCT_NO", shareNo);
                List shareList = null;
                shareList = ClientUtil.executeQuery("selectShareType", dataMap);
                //System.out.println("shareList: " + shareList);
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
                     // Added by nithya on 21-07-2016 for 4922
                    int maxNoOfLoans = 0;
                    if (imbpMap.containsKey("MAX_NO_OF_LOANS") && imbpMap.get("MAX_NO_OF_LOANS") != null && !imbpMap.get("MAX_NO_OF_LOANS").equals("")) {
                        maxNoOfLoans = CommonUtil.convertObjToInt(imbpMap.get("MAX_NO_OF_LOANS"));
                    }
                    // End
                    double loanAmt = 0;
                    loanAmt = CommonUtil.convertObjToDouble(txtLoanAmt.getText());
                    dataMap.put("CUST_ID", custId);
                    List list = null;
                    list = ClientUtil.executeQuery("getLoanDetails", dataMap);
                    if (list != null && list.size() > 0) {
                        double clearBal = 0;
                        double shadowDebit = 0;
                        double shadowCredit = 0;
                        double totalAmt = 0;
                         // Added for loop by nithya on 21-07-2016 for 4922
                        for (int i = 0; i < list.size(); i++) {
                           HashMap resultMap = new HashMap();
                           resultMap = (HashMap) list.get(i); 
                           clearBal = clearBal + CommonUtil.convertObjToDouble(resultMap.get("CLEAR_BALANCE"));
                           shadowDebit = shadowDebit + CommonUtil.convertObjToDouble(resultMap.get("SHADOW_DEBIT"));
                           shadowCredit = shadowCredit + CommonUtil.convertObjToDouble(resultMap.get("SHADOW_CREDIT"));
                        }
                        if (shadowDebit > 0 || shadowCredit > 0) {
                            ClientUtil.showMessageWindow("Authorization pending for this Customer..Hence new loan cannot be created!!");
                            ClientUtil.clearAll(panLoan);
                            txtLoanAmt.setText("");
                        } else {
                            if (clearBal < 0) {
                                clearBal = clearBal * (-1);
                            }
                            totalAmt = clearBal + loanAmt;
                            if (maxNoOfLoans > 0) {
                                List countList = ClientUtil.executeQuery("getParticularLoanCountForCustomer", dataMap);
                                if (countList != null && countList.size() > 0) {
                                    HashMap countResultMap = new HashMap();
                                    countResultMap = (HashMap) countList.get(0);
                                    if (countResultMap.containsKey("NO_OF_LOANS") && countResultMap.get("NO_OF_LOANS") != null) {
                                        int loanCount = CommonUtil.convertObjToInt(countResultMap.get("NO_OF_LOANS"));
                                        if (loanCount + 1 > maxNoOfLoans) {
                                            ClientUtil.showMessageWindow("This customer cannot be given more than " + maxNoOfLoans + " loans\n Currently " + loanCount + " Loans exist");
                                            txtLoanAmt.setText("");
                                        } else if (totalAmt > maxImbpAmt) {
                                            if (clearBal > maxImbpAmt) {
                                                ClientUtil.showMessageWindow("This customer cannot be given loan as his/her outstanding amount already exceeds the IMBP limit..!!");
                                            } else if (clearBal < maxImbpAmt) {
                                                double amount = 0;
                                                amount = maxImbpAmt - clearBal;
                                                String amt = CurrencyValidation.formatCrore(CommonUtil.convertObjToStr(amount));
                                                ClientUtil.showMessageWindow("The amount entered exceeds IMBP limit amount...This customer can be given loan only for a maximum amount of Rs. " + amt + "/-");
                                            }
                                            txtLoanAmt.setText("");
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
                                txtLoanAmt.setText("");
                            }
                        }
                    } else {
                        if (loanAmt > maxImbpAmt) {
                            String imbpAmt = CurrencyValidation.formatCrore(CommonUtil.convertObjToStr(maxImbpAmt));
                            ClientUtil.showMessageWindow("The loan amount exceeds the IMBP limit amount...Please enter an amount less than Rs. " + imbpAmt + "/-");
                            txtLoanAmt.setText("");
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
            if (col == 0 && ((viewType.equals("Edit")&&(CommonUtil.convertObjToStr(getValueAt(row,0)).equalsIgnoreCase("TRUE"))&&CommonUtil.convertObjToStr(getValueAt(row,6)).equals("Y"))||(CommonUtil.convertObjToStr(getValueAt(row, col+2)).equals("Y")))) {
                tableMouseClicked();
                return false;
            } else {
               if (col != 0) {
                    if (col == 3 && (CommonUtil.convertObjToStr(getValueAt(row, col+2)).equals("Y"))) {
                        tableMouseClicked();
                        return true;
                    } else {
                        tableMouseClicked();
                        return false;
                    }

                } else {
                    tableMouseClicked();
                    //System.out.println("nnn");
                    return true;
                }
            }
        }

        public void setValueAt(Object aValue, int row, int col) {
            ArrayList rowVector = (ArrayList) dataVector.get(row);
            rowVector.set(col, aValue);
        }
    }

    private void chargeAmount() {
        HashMap appraiserMap = new HashMap();
        appraiserMap.put("SCHEME_ID", prodDesc);
        appraiserMap.put("DEDUCTION_ACCU", "R");
        chargelst = ClientUtil.executeQuery("getAllChargeDetailsData", appraiserMap);
        HashMap chargeMap = new HashMap();
        if (chargelst != null && chargelst.size() > 0) {
            for (int i = 0; i < chargelst.size(); i++) {
                String accHead = "";
                String editable = "";
                chargeMap = (HashMap) chargelst.get(i);
                accHead = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_ID"));
                editable = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_EDITABLE"));
                //System.out.println("$#@@$ accHead" + accHead);
                for (int j = 0; j < table.getRowCount(); j++) {
                    if (CommonUtil.convertObjToStr(table.getValueAt(j, 1)).equals(accHead)) {
                        double chargeAmt = 0;
                        if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Sanction Amount")) {
                            chargeAmt = CommonUtil.convertObjToDouble(txtLoanAmt.getText()).doubleValue()
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
                            table.setValueAt(String.valueOf(chargeAmt), j, 3);
                        } else if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Amount Range")) {
                            List chargeslabLst = ClientUtil.executeQuery("getSelectLoanSlabChargesTO", chargeMap);
                            double limit = CommonUtil.convertObjToDouble(txtLoanAmt.getText()).doubleValue();
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

                                        chargeAmt = CommonUtil.convertObjToDouble(txtLoanAmt.getText()).doubleValue() * chargeRate / 100;
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


                            table.setValueAt(String.valueOf(chargeAmt), j, 3);
                        } else if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Flat Charge")) {
                            chargeAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(chargeMap.get("FLAT_CHARGE"))).doubleValue();
                        }
                        chargeMap.put("CHARGE_AMOUNT", String.valueOf(chargeAmt));
                    }
                    if (editable.equals("Y")) {
                        double chargeAmt1 = CommonUtil.convertObjToDouble(table.getValueAt(j, 3));
                        chargeMap.put("CHARGE_AMOUNT", String.valueOf(chargeAmt1));
                    }
                }
            }
            //System.out.println("#$#$$# chargelst:" + chargelst);
            // editChargeTable();
            table.revalidate();
            table.updateUI();
            selectMode = true;//charge details, calculating total of selected amount

            /*
             * table.addMouseListener(new java.awt.event.MouseAdapter() { public
             * void mouseClicked(java.awt.event.MouseEvent evt) {
             * tableMouseClicked(evt); } });
             */
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

    private void editChargeTable() {
        HashMap tableMap = editBuildData(prodDesc);
        ArrayList dataList = new ArrayList();
        dataList = (ArrayList) tableMap.get("DATA");
        //System.out.println("#$#$$# dataList.size():>>>>>>>>>>>" + dataList.size());
        if (dataList != null && dataList.size() > 0) {
            tableFlag = true;
            ArrayList headers;
            panChargeDetails.setVisible(true);
            SimpleTableModel stm = new SimpleTableModel((ArrayList) tableMap.get("DATA"), (ArrayList) tableMap.get("HEAD"));
            
            table = new JTable(stm);
            table.setSize(430, 110);
            //charge details
            //calculating total of selected amount
            //System.out.println("ggsssggkjf>>>>>>>>>>>.####");
            table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                   
                    if (tableFlag == true) {
                     tableMouseClicked();
                    }
                }
            });
            table.addKeyListener(new java.awt.event.KeyAdapter() {

                public void keyReleased(java.awt.event.KeyEvent e) {
                    if (tableFlag == true) {
                        tableMouseClicked();
                    }
                }
            });
            //charge end..
            srpChargeDetails = new javax.swing.JScrollPane(table);
            srpChargeDetails.setMinimumSize(new java.awt.Dimension(430, 110));
            srpChargeDetails.setPreferredSize(new java.awt.Dimension(430, 110));
            panChargeDetails.add(srpChargeDetails, new java.awt.GridBagConstraints());
            table.revalidate();
//            tableMouseClicked(null);
//             table.revalidate();
            
        } else {
            tableFlag = false;
            chrgTableEnableDisable();
        }
    }

    private HashMap editBuildData(String prodDesc) {
        tabEditDataMap= new HashMap();
        HashMap whereMap = new HashMap();
        whereMap.put("SCHEME_ID", prodDesc);
        whereMap.put("DEDUCTION_ACCU", "R");
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
              _heading.add("Editable");
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
                colData.add(CommonUtil.convertObjToStr(obj));
            }
             colData.add("N");
            data.add(colData);
           
        }
        map = new HashMap();
        map.put("HEAD", _heading);
        map.put("DATA", data);
        return map;
    }

    private void editChargeAmount() {
        HashMap appraiserMap = new HashMap();
        appraiserMap.put("SCHEME_ID", prodDesc);
        appraiserMap.put("DEDUCTION_ACCU", "R");
        chargelst = ClientUtil.executeQuery("getAllChargeDetailsData", appraiserMap);
        HashMap chargeMap = new HashMap();
        HashMap whrMap = new HashMap();
        whrMap.put("LINK_BATCH_ID",txtApplNo.getText());
        trans_Det_List = ClientUtil.executeQuery("getChargeTransDetails", whrMap);
        if (trans_Det_List != null && trans_Det_List.size() > 0) {
            for (int i = 0; i < trans_Det_List.size(); i++) {
                HashMap tabMap = (HashMap) trans_Det_List.get(i);
                tabEditDataMap.put(tabMap.get("CHARGE_ID"), tabMap.get("CHARGE_ID"));
            }

        }
        if (chargelst != null && chargelst.size() > 0) {
            for (int i = 0; i < chargelst.size(); i++) {
                String accHead = "";
                chargeMap = (HashMap) chargelst.get(i);
                accHead = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_ID"));
                for (int j = 0; j < table.getRowCount(); j++) {
                    String ac_hd =CommonUtil.convertObjToStr(table.getValueAt(j, 1));
                    if(tabEditDataMap!=null && tabEditDataMap.containsKey(ac_hd)){
                        if(CommonUtil.convertObjToStr(tabEditDataMap.get(ac_hd)).equals(ac_hd)){
                           table.setValueAt(true, j, 0);
                           table.setValueAt("Y", j, 6);
                           table.setCellEditor(null);
                           }
                    }
                    if (CommonUtil.convertObjToStr(table.getValueAt(j, 1)).equals(accHead)) {
                        double chargeAmt = 0;
                        if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Sanction Amount")) {
                            chargeAmt = CommonUtil.convertObjToDouble(txtLoanAmt.getText()).doubleValue()
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
                            table.setValueAt(String.valueOf(chargeAmt), j, 3);
                        } else if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Amount Range")) {
                             List chargeslabLst = ClientUtil.executeQuery("getSelectLoanSlabChargesTO", chargeMap);
                            double limit = CommonUtil.convertObjToDouble(txtLoanAmt.getText()).doubleValue();
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

                                        chargeAmt = CommonUtil.convertObjToDouble(txtLoanAmt.getText()).doubleValue() * chargeRate / 100;
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


                            table.setValueAt(String.valueOf(chargeAmt), j, 3);
                        } else if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Flat Charge")) {
                            chargeAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(chargeMap.get("FLAT_CHARGE"))).doubleValue();
                        }
                        chargeMap.put("CHARGE_AMOUNT", String.valueOf(chargeAmt));
                    }
                }
            }
            //System.out.println("#$#$$# chargelst:" + chargelst);
            //charge details
            //calculating total of selected amount

            table.revalidate();
            table.updateUI();

        }
    }     // CHARGE_AMOUNT_END

    private HashMap serviceTaxAmount(String desc) {// Function modified by nithya 
        HashMap checkForTaxMap = new HashMap();
        String scheme = CommonUtil.convertObjToStr(cboSchemName.getSelectedItem());
        HashMap whereMap = new HashMap();
        whereMap.put("SCHEME_NAME", scheme);
        whereMap.put("CHARGE_DESC", desc);
        String retStr = "";
        List resultList = ClientUtil.executeQuery("getCheckServiceTaxApplicable", whereMap);
        HashMap checkMap = new HashMap();
        if (resultList != null && resultList.size() > 0) {
            checkMap = (HashMap) resultList.get(0);
            if (checkMap != null && checkMap.containsKey("SERVICE_TAX_APPLICABLE") && checkMap.containsKey("SERVICE_TAX_ID")) {
                retStr = CommonUtil.convertObjToStr(checkMap.get("SERVICE_TAX_APPLICABLE"));
                checkForTaxMap.put("SERVICE_TAX_APPLICABLE",checkMap.get("SERVICE_TAX_APPLICABLE"));
                checkForTaxMap.put("SERVICE_TAX_ID",checkMap.get("SERVICE_TAX_ID"));
            }
        }       
        return checkForTaxMap;
    }

    //charge details
    //calculating total of selected amount
    private void tableMouseClicked() {
//       if(aki==0)
//       {aki=1;
//           tableMouseClicked(null);
//       }
        // TODO add your handling code here:
        double totAmount = 0.0;
       // System.out.println("table.getRowCount() " + table.getRowCount());
        //    System.out.println("nnnnnn   "+table.getValueAt(0,0).toString());
//            System.out.println("mmmmm  "+table.getValueAt(1,0).toString());
        // Commenting the code by nithya for service tax to gst conversion
//      if (table != null && table.getModel() != null) {
//        for (int i = 0; i < table.getRowCount(); i++) {
////           System.out.println("#$#$ CommonUtil.convertObjToStr(table.getValueAt(i,0):"+table.getValueAt(i,0));
//            if (CommonUtil.convertObjToStr(table.getValueAt(i, 0)).equals("true")) {
//                String edit = "N";
//                if (viewType!=null && viewType.equals("Edit")) {
//                    edit = CommonUtil.convertObjToStr(table.getValueAt(i, 6));
//                }
//                if (edit!=null && edit.equals("N")) {
//                    totAmount = totAmount + CommonUtil.convertObjToDouble(table.getValueAt(i, 3)).doubleValue();
//                }
//                  if (TrueTransactMain.SERVICE_TAX_REQ.equalsIgnoreCase("Y")) {
//                   String val = serviceTaxAmount(CommonUtil.convertObjToStr(table.getValueAt(i, 2)));
//
//                    if (val != null && val.equals("Y") && edit.equals("N")) {
//                        taxAmt = taxAmt + CommonUtil.convertObjToDouble(table.getValueAt(i, 3));
//                    }
//
//                }
//            }
//        }}
       // End   
        List taxSettingsList = new ArrayList(); 
        if (table != null && table.getModel() != null) {
            HashMap checkForTaxMap = new HashMap();
            for (int i = 0; i < table.getRowCount(); i++) {
                double chrgamt = 0;
                HashMap serviceTaSettingsMap = new HashMap();
                if (CommonUtil.convertObjToStr(table.getValueAt(i, 0)).equals("true")) {
                    String edit = "N";
                    if (viewType != null && viewType.equals("Edit")) {
                        edit = CommonUtil.convertObjToStr(table.getValueAt(i, 6));
                    }
                    if (edit != null && edit.equals("N")) {
                        totAmount = totAmount + CommonUtil.convertObjToDouble(table.getValueAt(i, 3)).doubleValue();
                    }
                    if (TrueTransactMain.SERVICE_TAX_REQ.equalsIgnoreCase("Y")) {
                        checkForTaxMap = serviceTaxAmount(CommonUtil.convertObjToStr(table.getValueAt(i, 2)));
                        // String val = serviceTaxAmount(CommonUtil.convertObjToStr(table.getValueAt(i, 2)));
                        if (checkForTaxMap.containsKey("SERVICE_TAX_APPLICABLE") && checkForTaxMap.get("SERVICE_TAX_APPLICABLE") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_APPLICABLE")).equalsIgnoreCase("Y")) {
                            if (checkForTaxMap.containsKey("SERVICE_TAX_ID") && checkForTaxMap.get("SERVICE_TAX_ID") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_ID")).length() > 0) {
                                chrgamt = CommonUtil.convertObjToDouble(table.getValueAt(i, 3));
                                if (chrgamt > 0) {
                                    serviceTaSettingsMap.put("SETTINGS_ID", checkForTaxMap.get("SERVICE_TAX_ID"));
                                    serviceTaSettingsMap.put(ServiceTaxCalculation.TOT_AMOUNT, CommonUtil.convertObjToStr(chrgamt));
                                    taxSettingsList.add(serviceTaSettingsMap);
                                }
                            }
                        }
                    }
                }
            }
        }
        double serviceTaxAmt = 0;
        //if (taxAmt > 0) {
        if(taxSettingsList != null && taxSettingsList.size() > 0){
            HashMap ser_Tax_Val = new HashMap();
            ser_Tax_Val.put(ServiceTaxCalculation.CURR_DT, getDateValue(tdtApplDt.getDateValue()));
            ser_Tax_Val.put(ServiceTaxCalculation.TOT_AMOUNT, CommonUtil.convertObjToStr((taxAmt)));
            ser_Tax_Val.put("SERVICE_TAX_DATA", taxSettingsList);
            //ser_Tax_Val.put("TEXT_BOX", "TEXT_BOX");
            try {
                objServiceTax = new ServiceTaxCalculation();
                serviceTax_Map = objServiceTax.calculateServiceTax(ser_Tax_Val);
                System.out.println("serviceTax_Map :: " + serviceTax_Map);
                if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.TOT_TAX_AMT)) {
                    String amt = CommonUtil.convertObjToStr(serviceTax_Map.get(ServiceTaxCalculation.TOT_TAX_AMT));
                    
//                    serviceTaxAmt = CommonUtil.convertObjToDouble(roundOffAmt(amt, "NEAREST_VALUE"));
//                    lblServiceTaxval.setText(roundOffAmt(amt, "NEAREST_VALUE"));
//                    serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, roundOffAmt(amt, "NEAREST_VALUE"));
                    
                    serviceTaxAmt = CommonUtil.convertObjToDouble(amt);
                    lblServiceTaxval.setText(amt);
                    serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, amt);
                    
                } else {
                    lblServiceTaxval.setText("0.00");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
               // java.util.logging.Logger.getLogger(LoanApplicationUI.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            lblServiceTaxval.setText("0.00");
            serviceTax_Map = new HashMap();
        }
        lblTotalTransactionAmtVal.setText(CurrencyValidation.formatCrore(String.valueOf(totAmount)));
        transactionUI.resetObjects();
        transactionUI.setCallingAmount("" + (totAmount + serviceTaxAmt));
        transactionUI.setCallingApplicantName(txtMemName.getText());
        transactionUI.setCallingTransType("TRANSFER");
//        }

//        if (evt.getClickCount()==2) {
////            HashMap returnMap = new HashMap();
//            if (returnMap!=null) {
////                returnMap = observable.getProxyReturnMap();
//                if (returnMap.containsKey(table.getValueAt(
//                table.getSelectedRow(), 1))) {
//                    TTException exception = (TTException)returnMap.get(table.getValueAt(
//                    table.getSelectedRow(), 1));
//                    parseException.logException(exception, true);
//                }
//            }
//        }
    }
    //Added By Suresh

    private String roundOffAmt(String amtStr, String method) throws Exception {
        String amt = amtStr;
        DecimalFormat d = new DecimalFormat();
        d.setMaximumFractionDigits(0);
        d.setDecimalSeparatorAlwaysShown(true);
        if (amtStr != null && !amtStr.equals("")) {
            amtStr = d.parse(d.format(new Double(amtStr).doubleValue())).toString();
        }
        Rounding rd = new Rounding();
        int pos = amtStr.indexOf(".");
        long intPart = 0;
        long decPart = 0;
        if (pos >= 0) {
            intPart = new Long(amtStr.substring(0, pos)).longValue();
            decPart = new Long(amtStr.substring(pos + 1)).longValue();
        } else {
            if (amtStr != null && !amtStr.equals("")) {
                intPart = new Long(amtStr).longValue();
            }
        }
        if (method.equals(NEAREST)) {
            decPart = rd.getNearest(decPart, 10);
            amtStr = intPart + "." + decPart;
        } else if (method.equals(LOWER)) {
            decPart = rd.lower(decPart, 10);
            amtStr = intPart + "." + decPart;
        } else if (method.equals(HIGHER)) {
            decPart = rd.higher(decPart, 10);
            amtStr = intPart + "." + decPart;
        } else if (method.equals(NO_ROUND_OFF)) {
//            decPart = rd.higher(decPart,10);
            // amtStr = intPart+"."+decPart;
            if (!amt.equals("")) {
                amtStr = df.format(Double.parseDouble(amt));
            } else {
                amtStr = amt;
            }
        }
        return amtStr;
    }

    private void finalizeCharges() {
        HashMap chargeMap = new HashMap();
        if (chargelst != null && chargelst.size() > 0) {
            for (int i = 0; i < chargelst.size(); i++) {
                String accHead = "";
                chargeMap = (HashMap) chargelst.get(i);
                accHead = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_ID"));
                for (int j = 0; j < table.getRowCount(); j++) {
                    if (CommonUtil.convertObjToStr(table.getValueAt(j, 1)).equals(accHead) && !((Boolean) table.getValueAt(j, 0)).booleanValue()) {
                        chargelst.remove(i--);
                    } else if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT && CommonUtil.convertObjToStr(table.getValueAt(j, 1)).equals(accHead)&&CommonUtil.convertObjToStr(table.getValueAt(j, 6)).equals("Y")){
                        chargelst.remove(i--); 
                    }
                    else {
                        if (CommonUtil.convertObjToStr(table.getValueAt(j, 1)).equals(accHead) && CommonUtil.convertObjToStr(table.getValueAt(j, 5)).equals("Y")) {
                            String chargeAmt = CommonUtil.convertObjToStr(table.getValueAt(j, 3));
                            chargeMap.put("CHARGE_AMOUNT", String.valueOf(chargeAmt));

                        }
                    }
                }
            }
            observable.setChargelst(chargelst);
        }
    }
    private void getTransDetails(String batchId) throws Exception {
        HashMap getTransMap = new HashMap();
        // String branchId=CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
        Date currDt = null;
        String _branchCode = TrueTransactMain.BRANCH_ID;
        currDt = ClientUtil.getCurrentDate();
        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("TRANS_DT", currDt);
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        returnMap = new HashMap();
        List transList = (List) ClientUtil.executeQuery("getTransferDetails", getTransMap);
        if (transList != null && transList.size() > 0) {
            returnMap.put("TRANSFER_TRANS_LIST", transList);
        }
        List cashList = (List) ClientUtil.executeQuery("getCashDetails", getTransMap);
        if (cashList != null && cashList.size() > 0) {
            returnMap.put("CASH_TRANS_LIST", cashList);
        }
        getTransMap.clear();
        getTransMap = null;
        transList = null;
        cashList = null;
        displayTransDetail(returnMap);
    }

    private void displayLoanApplication(HashMap proxyResultMap) {
        try{
        int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null, "Do you want to print Application?", CommonConstants.WARNINGTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        if (yesNo == 0) {
            if (proxyResultMap != null && proxyResultMap.containsKey("APPLICATION_NO")) {
                String applNum = CommonUtil.convertObjToStr(proxyResultMap.get("APPLICATION_NO"));
                HashMap wherMap = new HashMap();
                wherMap.put("APPNO", applNum);
                List transList = (List) ClientUtil.executeQuery("getSelectPurposeCodeFromLoanApplRegstr", wherMap);
                if (transList != null && transList.size() > 0) {
                    HashMap eachmap = (HashMap) transList.get(0);
                    if (eachmap != null && eachmap.size() > 0) {
                        TTIntegration ttIntgration = null;
                        HashMap paramMap = new HashMap();
                        paramMap.put("APPLICATION_NO", applNum);
                        paramMap.put("PROD_ID", CommonUtil.convertObjToStr(eachmap.get("PROD_ID")));
                        paramMap.put("BRANCH_ID", CommonUtil.convertObjToStr(eachmap.get("BRANCH_ID")));
                        ttIntgration.setParam(paramMap);
                        ttIntgration.integrationForPrint("LoanApplicationRegisterDet", false);
                        //   ttIntgration.integration("LoanApplicationRegisterDet");
                    }
                }
            }
        }
        }catch(Exception e){
            ClientUtil.showMessageWindow("File not Found....!");
            btnCancelActionPerformed(null);
        }
    }
    
    // Added by nithya on 14-02-2019 for KD 403 0019830: SHARE NOT ELIGIABLE FOR LOAN SANCTION 
      private void checkShareHolder() {        
        HashMap shareMap = new HashMap();
        String loanType = "";
        HashMap whereMap = new HashMap();
        whereMap.put("PRODUCT_ID", (String) ((ComboBoxModel) cboSchemName.getModel()).getKeyForSelected());
        List chargeList = (List) (ClientUtil.executeQuery("getLoanProdCategory", whereMap));
        if (chargeList != null && chargeList.size() > 0) {
            whereMap = (HashMap) (chargeList.get(0));
            loanType = CommonUtil.convertObjToStr(whereMap.get("AUTHORIZE_REMARK"));
        }
        shareMap.put("PROD_ID", loanType);
        String sharetype = "NOMINAL";        
        List shareList = ClientUtil.executeQuery("getSelectShareProductLoanAcct", shareMap);
        if (shareList != null && shareList.size() > 0) {              
            shareMap.put("SHARE_ACCT_NO",txtMemId.getText());          
            List share = ClientUtil.executeQuery("getShareAccInfoEligibilityDetailsForLoan", shareMap);
            if (share != null && share.size() > 0) {
                shareMap = (HashMap) share.get(0);
                String notElegibleLoan = CommonUtil.convertObjToStr(shareMap.get("NOT_ELIGIBLE_LOAN"));
                // Commented by nithya on 06-08-2019 for KD 579
//                if (notElegibleLoan != null && notElegibleLoan.equals("Y") && shareMap.get("NOT_ELIGIBLE_DT") != null) {
//                    Date eligibal_dt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(shareMap.get("NOT_ELIGIBLE_DT")));
//                    if (DateUtil.dateDiff(eligibal_dt, (Date) curr_dt.clone()) < 0) {                        
//                        ClientUtil.showMessageWindow(" Customer is not eligible for loan till  " + DateUtil.getStringDate(eligibal_dt)); 
//                    }
//                    return;
//                } 
                if (notElegibleLoan != null && notElegibleLoan.equals("Y")){ // Added by nithya on 06-08-2019 for KD 579
                    ClientUtil.showMessageWindow(" Customer is not eligible for loan");
                    btnCancelActionPerformed(null);
                    return;
                }
                else if (shareMap.get("SHARE_TYPE") != null && sharetype.equals(CommonUtil.convertObjToStr(shareMap.get("SHARE_TYPE")))) {
                    ClientUtil.showMessageWindow("Share Type is NOMINAL");
                    return;
                }               
            }
        }
    }
    
    private void displayTransDetail(HashMap proxyResultMap) {
        String cashDisplayStr = "Cash Transaction Details...\n";
        String transferDisplayStr = "Transfer Transaction Details...\n";
        String displayStr = "";
        String transId = "";
        String transType = "",transTypeTrans ="";
        Object keys[] = proxyResultMap.keySet().toArray();
        int cashCount = 0;
        int transferCount = 0;
        List tempList = null;
        HashMap transMap = null;
        String actNum = "";
        HashMap transIdMap = new HashMap();
        HashMap transTypeMap = new HashMap();
        HashMap transTypeTransferMap = new HashMap();
        for (int i = 0; i < keys.length; i++) {
            if (proxyResultMap.get(keys[i]) instanceof String) {
                continue;
            }
            tempList = (List) proxyResultMap.get(keys[i]);
            if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH") != -1) {
                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                        transId = (String) transMap.get("TRANS_ID");
                    }
                    cashDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if (actNum != null && !actNum.equals("")) {
                        cashDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    } else {
                        cashDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    }
                    transTypeMap.put(transMap.get("TRANS_ID"), transMap.get("TRANS_TYPE"));
                    transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "CASH");
                }
                cashCount++;
                transType = "CASH";
            } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER") != -1) {
                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                        transId = (String) transMap.get("BATCH_ID");
                    }
                    transferDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Batch Id : " + transMap.get("BATCH_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if (actNum != null && !actNum.equals("")) {
                        transferDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    } else {
                        transferDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    }
                    transTypeTransferMap.put(transMap.get("SINGLE_TRANS_ID"), "TRANSFER");
                }
                transferCount++;
                transTypeTrans = "TRANSFER";
            }
        }
        if (cashCount > 0) {
            displayStr += cashDisplayStr;
        }
        if (transferCount > 0) {
            displayStr += transferDisplayStr;
        }
        if (!displayStr.equals("")) {
            ClientUtil.showMessageWindow("" + displayStr);
            int yesNo = 0;
            String[] options = {"Yes", "No"};
            yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
            if (yesNo == 0) {
                //            TTIntegration ttIntgration = null;
                //            HashMap paramMap = new HashMap();
                //            paramMap.put("TransId", transId);
                //            paramMap.put("TransDt", observable.getCurrDt());
                //            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                //            ttIntgration.setParam(paramMap);
                //            ttIntgration.integrationForPrint("ReceiptPayment", false);

                TTIntegration ttIntgration = null;
                HashMap printParamMap = new HashMap();
                printParamMap.put("TransDt", observable.getCurrDt());
                printParamMap.put("BranchId", ProxyParameters.BRANCH_ID);
                Object keys1[] = transIdMap.keySet().toArray();
                if (transType!=null && transType.equals("CASH")) {
                    for (int i = 0; i < keys1.length; i++) {
                        printParamMap.put("TransId", keys1[i]);
                        ttIntgration.setParam(printParamMap);
                        /*
                         * if
                         * (CommonUtil.convertObjToStr(transIdMap.get(keys1[i])).equals("TRANSFER"))
                         * { ttIntgration.integrationForPrint("ReceiptPayment");
                         * } else
                         */ if (CommonUtil.convertObjToStr(transTypeMap.get(keys1[i])).equals("DEBIT")) {
                            ttIntgration.integrationForPrint("CashPayment", false);
                        }
                    }
                    if (CommonUtil.convertObjToStr(transTypeMap.get(keys1[0])).equals("DEBIT")) {
                        ttIntgration.integrationForPrint("CashPayment", false);
                    } else {
                        ttIntgration.integrationForPrint("LACashReceipt", false);
                    }
                }
                if (transTypeTrans!=null &&transTypeTrans.equals("TRANSFER")) {
                    Object keys2[] = transTypeTransferMap.keySet().toArray();
                    printParamMap = new HashMap();
                    printParamMap.put("TransDt", observable.getCurrDt());
                    printParamMap.put("BranchId", ProxyParameters.BRANCH_ID);
                    printParamMap.put("TransId", keys2[0]);
                    ttIntgration.setParam(printParamMap);
                    if (CommonUtil.convertObjToStr(transTypeTransferMap.get(keys2[0])).equals("TRANSFER")) {
                        ttIntgration.integrationForPrint("LAReceiptPayment");
                    }
                }
         //   }
            }
        }
    }
    //end...
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDateChange;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnMemLiability;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnPrintCheck;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSchedule;
    private com.see.truetransact.uicomponent.CPanel btnScheduler;
    private com.see.truetransact.uicomponent.CButton btnSearch;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CLabel cLabel2;
    private com.see.truetransact.uicomponent.CComboBox cboPurposeCode;
    private com.see.truetransact.uicomponent.CComboBox cboRegstrStatus;
    private com.see.truetransact.uicomponent.CComboBox cboRepayFreq_Repayment;
    private com.see.truetransact.uicomponent.CComboBox cboRepayType;
    private com.see.truetransact.uicomponent.CComboBox cboSchemName;
    private com.see.truetransact.uicomponent.CCheckBox chkLoaneeSecurityonly;
    private com.see.truetransact.uicomponent.CCheckBox chkMoratorium_Given;
    private com.see.truetransact.uicomponent.CCheckBox chkSettlement;
    private com.see.truetransact.uicomponent.CLabel jLabel1;
    private com.see.truetransact.uicomponent.CPanel jPanel2;
    private com.see.truetransact.uicomponent.CLabel lblApplDate;
    private com.see.truetransact.uicomponent.CLabel lblApplInwardDate;
    private com.see.truetransact.uicomponent.CLabel lblApplNo;
    private com.see.truetransact.uicomponent.CLabel lblCaste;
    private com.see.truetransact.uicomponent.CLabel lblCostOfVehicle;
    private com.see.truetransact.uicomponent.CLabel lblCustId;
    private com.see.truetransact.uicomponent.CLabel lblDob;
    private com.see.truetransact.uicomponent.CLabel lblDobValue;
    private com.see.truetransact.uicomponent.CLabel lblDueDt;
    private com.see.truetransact.uicomponent.CLabel lblFacility_Moratorium_Period;
    private com.see.truetransact.uicomponent.CLabel lblFromDt;
    private javax.swing.JLabel lblLoanAmt;
    private javax.swing.JLabel lblLoanAmts;
    private javax.swing.JLabel lblMembrId;
    private javax.swing.JLabel lblMembrName;
    private com.see.truetransact.uicomponent.CLabel lblMoratorium_Given;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNoInstall;
    private com.see.truetransact.uicomponent.CLabel lblPurposeCode;
    private javax.swing.JLabel lblRegStatus;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblRepayFreq_Repayment;
    private com.see.truetransact.uicomponent.CLabel lblRepayType;
    private com.see.truetransact.uicomponent.CLabel lblRetireDate;
    private com.see.truetransact.uicomponent.CLabel lblRetireMent;
    private javax.swing.JLabel lblSchemName;
    private com.see.truetransact.uicomponent.CLabel lblServicePeriod;
    private com.see.truetransact.uicomponent.CLabel lblServicePeriodValue;
    private com.see.truetransact.uicomponent.CLabel lblServiceTax;
    private com.see.truetransact.uicomponent.CLabel lblServiceTaxval;
    private com.see.truetransact.uicomponent.CLabel lblSeviceperiod;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace56;
    private com.see.truetransact.uicomponent.CLabel lblSpace57;
    private com.see.truetransact.uicomponent.CLabel lblSpace58;
    private com.see.truetransact.uicomponent.CLabel lblSpace59;
    private com.see.truetransact.uicomponent.CLabel lblSpace60;
    private com.see.truetransact.uicomponent.CLabel lblSpace61;
    private com.see.truetransact.uicomponent.CLabel lblSpace62;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTotalTransactionAmt;
    private com.see.truetransact.uicomponent.CLabel lblTotalTransactionAmtVal;
    private com.see.truetransact.uicomponent.CMenuBar mbrCustomer;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panCalacData;
    private com.see.truetransact.uicomponent.CPanel panCaste;
    private com.see.truetransact.uicomponent.CPanel panChargeDetails;
    private javax.swing.JPanel panChrg;
    private com.see.truetransact.uicomponent.CPanel panLoan;
    private com.see.truetransact.uicomponent.CPanel panLoanApplication;
    private com.see.truetransact.uicomponent.CPanel panRemarks;
    private com.see.truetransact.uicomponent.CPanel panRepayment;
    private com.see.truetransact.uicomponent.CPanel panRepaymentDetails;
    private com.see.truetransact.uicomponent.CPanel panSchedule;
    private com.see.truetransact.uicomponent.CPanel panSecurityDetails;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTrans;
    private com.see.truetransact.uicomponent.CScrollPane scrRemarks;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CTabbedPane tabLoan;
    private javax.swing.JToolBar tbrLoanAppl;
    private com.see.truetransact.uicomponent.CDateField tdtApplDt;
    private com.see.truetransact.uicomponent.CDateField tdtApplInwrdDt;
    private com.see.truetransact.uicomponent.CDateField tdtDueDt;
    private com.see.truetransact.uicomponent.CDateField tdtFromDt;
    private com.see.truetransact.uicomponent.CTextArea txaRemarks;
    private javax.swing.JTextField txtApplNo;
    private com.see.truetransact.uicomponent.CTextField txtCaste;
    private com.see.truetransact.uicomponent.CTextField txtCostOfVehicleField;
    private javax.swing.JTextField txtCustId;
    private com.see.truetransact.uicomponent.CTextField txtFacility_Moratorium_Period;
    private com.see.truetransact.uicomponent.CTextField txtInstallAmount;
    private com.see.truetransact.uicomponent.CTextField txtLoanAmt;
    private com.see.truetransact.uicomponent.CTextField txtLoanAmts;
    private javax.swing.JTextField txtMemId;
    private com.see.truetransact.uicomponent.CTextField txtMemName;
    private com.see.truetransact.uicomponent.CTextField txtNextAccNo;
    private com.see.truetransact.uicomponent.CTextField txtNoInstall;
    private com.see.truetransact.uicomponent.CTextField txtSalaryField;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] args) {
        LoanApplicationUI LoanApplication = new LoanApplicationUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(LoanApplication);
        j.show();
        LoanApplication.show();
    }
    //For Security Purpose
    private com.see.truetransact.uicomponent.CLabel lblProductId;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CComboBox cboProdId;
    private com.see.truetransact.uicomponent.CButton btnCollateralDelete;
    private com.see.truetransact.uicomponent.CButton btnCollateralNew;
    private com.see.truetransact.uicomponent.CButton btnCollateralSave;
    private com.see.truetransact.uicomponent.CButton btnMemNo;
    private com.see.truetransact.uicomponent.CButton btnMemberDelete;
    private com.see.truetransact.uicomponent.CButton btnMemberNew;
    private com.see.truetransact.uicomponent.CButton btnMemberSave;
    private com.see.truetransact.uicomponent.CButton btnSalaryDelete;
    private com.see.truetransact.uicomponent.CButton btnSalaryNew;
    private com.see.truetransact.uicomponent.CButton btnSalarySave;
    private com.see.truetransact.uicomponent.CComboBox cboCity;
    private com.see.truetransact.uicomponent.CComboBox cboNature;
    private com.see.truetransact.uicomponent.CLabel lblAddress;
    private com.see.truetransact.uicomponent.CLabel lblCity;
    private com.see.truetransact.uicomponent.CLabel lblContactNo;
    private com.see.truetransact.uicomponent.CLabel lblContactNum;
    private com.see.truetransact.uicomponent.CLabel lblDesignation;
    private com.see.truetransact.uicomponent.CLabel lblDocumentDate;
    private com.see.truetransact.uicomponent.CLabel lblDocumentNo;
    private com.see.truetransact.uicomponent.CLabel lblDocumentType;
    private com.see.truetransact.uicomponent.CLabel lblEmployerName;
    private com.see.truetransact.uicomponent.CLabel lblGahanYesNo;
    private com.see.truetransact.uicomponent.CPanel panGahanYesNo;
    private com.see.truetransact.uicomponent.CRadioButton rdoGahanYes;
    private com.see.truetransact.uicomponent.CRadioButton rdoGahanNo;
    private com.see.truetransact.uicomponent.CButtonGroup rdoGahanGroup;
    private com.see.truetransact.uicomponent.CLabel lblMemName;
    private com.see.truetransact.uicomponent.CLabel lblMemRetireDate;
    private com.see.truetransact.uicomponent.CLabel lblMemNetworth;
    private com.see.truetransact.uicomponent.CLabel lblMemNo;
    private com.see.truetransact.uicomponent.CLabel lblMemType;
    private com.see.truetransact.uicomponent.CLabel lblMemberNum;
    private com.see.truetransact.uicomponent.CLabel lblMemberName;
    private com.see.truetransact.uicomponent.CLabel lblSalMemberRetireDate;
    private com.see.truetransact.uicomponent.CLabel lblSalMemberRetireDateValue;
    
    private com.see.truetransact.uicomponent.CPanel panOwnerMemberNumber;
    private com.see.truetransact.uicomponent.CPanel panDocumentNumber;
    private com.see.truetransact.uicomponent.CLabel lblNature;
    private com.see.truetransact.uicomponent.CLabel lblNetWorth1;
    private com.see.truetransact.uicomponent.CLabel lblVehicleNetWorth;
    private com.see.truetransact.uicomponent.CLabel lblTotalVehicleMemSal;
    private com.see.truetransact.uicomponent.CLabel lblOwnerMemberNname;
    private com.see.truetransact.uicomponent.CLabel lblOwnerMemberNo;
    private com.see.truetransact.uicomponent.CLabel lblPinCode;
    private com.see.truetransact.uicomponent.CLabel lblPledge;
    private com.see.truetransact.uicomponent.CLabel lblPledgeAmount;
    private com.see.truetransact.uicomponent.CLabel lblPledgeDate;
    private com.see.truetransact.uicomponent.CLabel lblPledgeNo;
    private com.see.truetransact.uicomponent.CLabel lblRegisteredOffice;
    private com.see.truetransact.uicomponent.CLabel lblRetirementDt;
    private com.see.truetransact.uicomponent.CLabel lblSalaryCertificateNo;
    private com.see.truetransact.uicomponent.CLabel lblSalaryRemark;
    private com.see.truetransact.uicomponent.CLabel lblSurveyNo;
    private com.see.truetransact.uicomponent.CLabel lblTotalArea;
    private com.see.truetransact.uicomponent.CLabel lblTotalSalary;
    private com.see.truetransact.uicomponent.CLabel lblMemberTotalSalary;
    private com.see.truetransact.uicomponent.CLabel lblVillage;
    private com.see.truetransact.uicomponent.CLabel lblPledgeType;
    private com.see.truetransact.uicomponent.CTextField txtPledgeType;
    //    private com.see.truetransact.uicomponent.CMenuBar mbrCustomer;
    private com.see.truetransact.uicomponent.CPanel panBtnCollateralType;
    private com.see.truetransact.uicomponent.CPanel panBtnMemberType;
    private com.see.truetransact.uicomponent.CButton btnVehicleMemNo;
    private com.see.truetransact.uicomponent.CButton btnVehicleDelete;
    private com.see.truetransact.uicomponent.CButton btnVehicleNew;
    private com.see.truetransact.uicomponent.CButton btnVehicleSave;
    private com.see.truetransact.uicomponent.CLabel lblVehicleMemRetireDate;
    private com.see.truetransact.uicomponent.CLabel lblVehicleNo;
    private com.see.truetransact.uicomponent.CLabel lblVehicleType;
    private com.see.truetransact.uicomponent.CLabel lblVehicleDetails;
    private com.see.truetransact.uicomponent.CLabel lblVehicleRcBookNo;
    private com.see.truetransact.uicomponent.CLabel lblVehicleDate;
    private com.see.truetransact.uicomponent.CLabel lblVehicleContactNum;
    private com.see.truetransact.uicomponent.CLabel lblVehicleMemName;
    private com.see.truetransact.uicomponent.CLabel lblVehicleMemberName;
    private com.see.truetransact.uicomponent.CLabel lblVehicleMemNo;
    private com.see.truetransact.uicomponent.CLabel lblVehicleMemberNum;
    private com.see.truetransact.uicomponent.CLabel lblVehicleMemType;
    private com.see.truetransact.uicomponent.CPanel panBtnVehicleType;
    private com.see.truetransact.uicomponent.CPanel panVehicleDetails;
    private com.see.truetransact.uicomponent.CPanel panVehicleNumber;
    private com.see.truetransact.uicomponent.CScrollPane srpVehicleType;
    private com.see.truetransact.uicomponent.CPanel panVehicleTypeDetails;
    private com.see.truetransact.uicomponent.CPanel panVehicleTypeTable;
    private com.see.truetransact.uicomponent.CTable tblVehicleType;
    private com.see.truetransact.uicomponent.CScrollPane srpTxtAreaVehicledtails;
    private com.see.truetransact.uicomponent.CTextArea txtVehicleDetals;
    private com.see.truetransact.uicomponent.CTextField txtVehicleNo;
    private com.see.truetransact.uicomponent.CTextField txtVehicleRcBookNo;
    private com.see.truetransact.uicomponent.CTextField txtVehicleType;
    private com.see.truetransact.uicomponent.CDateField txtVehicleDate;
    private com.see.truetransact.uicomponent.CTextField txtVehicleContactNum;
    private com.see.truetransact.uicomponent.CTextField txtVehicleMemType;
    private com.see.truetransact.uicomponent.CTextField txtVehicleMemberNum;
    private com.see.truetransact.uicomponent.CTextField txtVehicleMemberName;
    private com.see.truetransact.uicomponent.CPanel panBtnSalaryType;
    private com.see.truetransact.uicomponent.CPanel panCollateralTable;
    private com.see.truetransact.uicomponent.CPanel panCollateralJointTable;
    private com.see.truetransact.uicomponent.CPanel panCollateralTypeDetails;
    private com.see.truetransact.uicomponent.CPanel panCollatetalDetails;
    private com.see.truetransact.uicomponent.CPanel panEmpTransfer;
    private com.see.truetransact.uicomponent.CPanel panMemberDetails;
    private com.see.truetransact.uicomponent.CPanel panMemberNumber;
    private com.see.truetransact.uicomponent.CPanel panMemberTypeDetails;
    private com.see.truetransact.uicomponent.CPanel panMemberTypeTable;
    private com.see.truetransact.uicomponent.CPanel panSalaryTable;
    private com.see.truetransact.uicomponent.CPanel panSalaryDetails;
    private com.see.truetransact.uicomponent.CPanel panAllSalaryDetails;
    private com.see.truetransact.uicomponent.CScrollPane srpCollateralTable;
    private com.see.truetransact.uicomponent.CScrollPane srpCollateralJointTable;
    private com.see.truetransact.uicomponent.CScrollPane srpMemberType;
    private com.see.truetransact.uicomponent.CScrollPane srpSalary;
    private com.see.truetransact.uicomponent.CScrollPane srpTxtAreaParticulars;
    private com.see.truetransact.uicomponent.CTabbedPane tabMasterMaintenance;
    private com.see.truetransact.uicomponent.CTable tblCollateral;
    private com.see.truetransact.uicomponent.CTable tblJointCollateral;
    private com.see.truetransact.uicomponent.CTable tblMemberType;
    private com.see.truetransact.uicomponent.CTable tblSalary;
    private com.see.truetransact.uicomponent.CLabel lblRight;
    private com.see.truetransact.uicomponent.CComboBox cboRight;
    private com.see.truetransact.uicomponent.CDateField tdtDocumentDate;
    private com.see.truetransact.uicomponent.CDateField tdtPledgeDate;
    private com.see.truetransact.uicomponent.CDateField tdtRetirementDt;
    private com.see.truetransact.uicomponent.CTextField txtAddress;
    private com.see.truetransact.uicomponent.CTextArea txtAreaParticular;
    private com.see.truetransact.uicomponent.CTextField txtContactNo;
    private com.see.truetransact.uicomponent.CTextField txtContactNum;
    private com.see.truetransact.uicomponent.CTextField txtDesignation;
    private com.see.truetransact.uicomponent.CTextField txtDocumentNo;
    private com.see.truetransact.uicomponent.CLabel lblJewelleryDetails;
    private com.see.truetransact.uicomponent.CLabel lblGrossWeight;
    private com.see.truetransact.uicomponent.CLabel lblNetWeight;
    private com.see.truetransact.uicomponent.CLabel lblValueOfGold;
    private com.see.truetransact.uicomponent.CLabel lblGoldRemarks;
    private com.see.truetransact.uicomponent.CTextArea txtJewelleryDetails;
    private com.see.truetransact.uicomponent.CTextField txtGrossWeight;
    private com.see.truetransact.uicomponent.CTextField txtNetWeight;
    private com.see.truetransact.uicomponent.CTextField txtValueOfGold;
    private com.see.truetransact.uicomponent.CTextField txtGoldRemarks;
    private com.see.truetransact.uicomponent.CPanel panGoldTypeDetails;
    //    private com.see.truetransact.uicomponent.CTextField txtDocumentType;
    private com.see.truetransact.uicomponent.CComboBox cboDocumentType;
    private com.see.truetransact.uicomponent.CTextField txtEmployerName;
    private com.see.truetransact.uicomponent.CTextField txtMembName;
    private com.see.truetransact.uicomponent.CLabel lblMemberRetireDate;
    private com.see.truetransact.uicomponent.CTextField txtMemNetworth;
    private com.see.truetransact.uicomponent.CTextField txtMemPriority;
    private com.see.truetransact.uicomponent.CLabel lblMemPriority;
    private com.see.truetransact.uicomponent.CTextField txtMemNo;
    private com.see.truetransact.uicomponent.CTextField txtMemType;
    private com.see.truetransact.uicomponent.CTextField txtMemberNum;
    private com.see.truetransact.uicomponent.CTextField txtMemberName;
    private com.see.truetransact.uicomponent.CTextField txtNetWorth1;
    private com.see.truetransact.uicomponent.CTextField txtVehicleNetWorth;
    private com.see.truetransact.uicomponent.CTextField txtVehicleMemSal;
    private com.see.truetransact.uicomponent.CTextField txtOwnerMemberNname;
    private com.see.truetransact.uicomponent.CTextField txtPinCode;
    private com.see.truetransact.uicomponent.CComboBox cboPledge;
    private com.see.truetransact.uicomponent.CTextField txtPledgeAmount;
    private com.see.truetransact.uicomponent.CTextField txtPledgeNo;
    private com.see.truetransact.uicomponent.CTextField txtOwnerMemNo;
    private com.see.truetransact.uicomponent.CButton btnOwnerMemNo;
    private com.see.truetransact.uicomponent.CButton btnDocumentNo;
    private com.see.truetransact.uicomponent.CTextField txtRegisteredOffice;
    private com.see.truetransact.uicomponent.CTextField txtSalaryCertificateNo;
    private com.see.truetransact.uicomponent.CTextField txtSalaryRemark;
    private com.see.truetransact.uicomponent.CTextField txtSurveyNo;
    private com.see.truetransact.uicomponent.CTextField txtTotalArea;
    private com.see.truetransact.uicomponent.CTextField txtTotalSalary;
    private com.see.truetransact.uicomponent.CTextField txtMemberTotalSalary;
    private com.see.truetransact.uicomponent.CTextField txtVillage;
    //    private javax.swing.JScrollPane srpChargeDetails;
    private com.see.truetransact.uicomponent.CPanel panDepositDetails;
    private com.see.truetransact.uicomponent.CLabel lblDepAmount;
    private com.see.truetransact.uicomponent.CLabel lblProductId2;
    private com.see.truetransact.uicomponent.CLabel lblRateOfInterest;
    private com.see.truetransact.uicomponent.CLabel lblDepDt;
    private com.see.truetransact.uicomponent.CTextField txtDepAmount;
    private com.see.truetransact.uicomponent.CTextField txtMaturityValue;
    private com.see.truetransact.uicomponent.CTextField txtRateOfInterest;
    private com.see.truetransact.uicomponent.CLabel lblMaturityDt;
    private com.see.truetransact.uicomponent.CLabel lblMaturityValue;
    private com.see.truetransact.uicomponent.CLabel lblDepNo;
    private com.see.truetransact.uicomponent.CDateField tdtDepDt;
    private com.see.truetransact.uicomponent.CButton btnDepositNew;
    private com.see.truetransact.uicomponent.CButton btnDepositSave;
    private com.see.truetransact.uicomponent.CButton btnDepositDelete;
    private com.see.truetransact.uicomponent.CPanel panBtnDeposit;
    private com.see.truetransact.uicomponent.CDateField txtMaturityDt;
    private com.see.truetransact.uicomponent.CComboBox cboDepProdType;
    private com.see.truetransact.uicomponent.CPanel panDepNo;
    private com.see.truetransact.uicomponent.CButton btnDepNo;
    private com.see.truetransact.uicomponent.CLabel lblProductTypeSecurity;
    private com.see.truetransact.uicomponent.CComboBox cboProductTypeSecurity;
    private com.see.truetransact.uicomponent.CPanel panDepositType;
    private com.see.truetransact.uicomponent.CPanel panDepositTable;
    private com.see.truetransact.uicomponent.CScrollPane srpTableDeposit;
    private com.see.truetransact.uicomponent.CLabel lblTotalDeposit;
    private com.see.truetransact.uicomponent.CLabel lblTotalDepositValue;
    private com.see.truetransact.uicomponent.CTextField txtDepNo;
    private com.see.truetransact.uicomponent.CTable tblDepositDetails;
    private com.see.truetransact.uicomponent.CPanel panOtherSecurityDetails;
    private com.see.truetransact.uicomponent.CLabel lblLosName;
    private com.see.truetransact.uicomponent.CLabel lblLosSecurityType;
    private com.see.truetransact.uicomponent.CLabel lblLosSecurityNo;
    private com.see.truetransact.uicomponent.CLabel lblLosAmount;
    private com.see.truetransact.uicomponent.CTextField txtLosName;
    private com.see.truetransact.uicomponent.CTextField txtLosSecurityNo;
    private com.see.truetransact.uicomponent.CTextField txtLosMaturityvalue;
    private com.see.truetransact.uicomponent.CLabel lblLosIssueDate;
    private com.see.truetransact.uicomponent.CLabel lblLosMaturityValue;
    private com.see.truetransact.uicomponent.CLabel lblLosMaturityDate;
    private com.see.truetransact.uicomponent.CLabel lblLosRemarks;
    private com.see.truetransact.uicomponent.CDateField tdtLosMaturityDate;
    private com.see.truetransact.uicomponent.CButton btnLosNew;
    private com.see.truetransact.uicomponent.CButton btnLosSave;
    private com.see.truetransact.uicomponent.CButton btnLosDelete;
    private com.see.truetransact.uicomponent.CTextField txtLosAmount;
    private com.see.truetransact.uicomponent.CTextField txtLosRemarks;
    private com.see.truetransact.uicomponent.CDateField tdtLosIsuueDate;
    private com.see.truetransact.uicomponent.CTextField txtLosMaturityValue;
    private com.see.truetransact.uicomponent.CDateField tdtLosIssueDate;
    private com.see.truetransact.uicomponent.CLabel lblLosOtherInstitution;
    private com.see.truetransact.uicomponent.CComboBox cboLosOtherInstitution;
    private com.see.truetransact.uicomponent.CComboBox cboLosSecurityType;
    private com.see.truetransact.uicomponent.CPanel panLosTable;
    private com.see.truetransact.uicomponent.CPanel panLosDetails;
    private com.see.truetransact.uicomponent.CPanel panLosBtn;
    private com.see.truetransact.uicomponent.CDateField tdtLosMaturityDt;
    private com.see.truetransact.uicomponent.CScrollPane srpTableLos;
    private com.see.truetransact.uicomponent.CTable tblLosDetails;
}
