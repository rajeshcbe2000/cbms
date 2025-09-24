/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * frmRentTrans.java
 *
 * Created on September 12, 2011, 12:08 PM
 */
package com.see.truetransact.ui.roomrent;

import javax.swing.table.DefaultTableModel;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ClientConstants;
import java.util.Observable;
import java.math.BigDecimal;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.clientutil.ClientUtil;
import java.util.Observer;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import com.see.truetransact.ui.common.viewall.ViewAll;
import java.util.ResourceBundle;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.COptionPane;
import java.util.Date;
import java.util.*;
import java.text.SimpleDateFormat;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.ui.common.report.PrintClass;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.ui.common.viewall.NewAuthorizeListUI;
import java.text.*;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListDebitUI;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListCreditUI;
import javax.swing.ButtonGroup;

/**
 *
 * @author  userdd
 */
public class frmRentTrans extends CInternalFrame implements Observer, UIMandatoryField {

    private RentTransOB observable;
    private String[][] tabledata;
    private String[] column;
    private RentTransMRB objMandatoryRB = new RentTransMRB();//Instance for the MandatoryResourceBundle
    private DefaultTableModel model;
    private HashMap mandatoryMap;//Map for putting up MandatoryFields in the UI
    private String viewType = "";//Variable used to store ActionType(New,Edit,Delete)
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private final String AUTHORIZE = "Authorize";//Variable used when btnAuthorize is clicked
    private String strBorrowingNo = "";
//    String buiding_no=txtBuildingNo.getText();
    double noOfInstInFcsLst = 0;
    double penalRate = 0;
    double rentAmount = 0;
    double dedInst = 0;
    double dedDue = 0;
    double overDueAmt = 0;
    double penalAmt = 0;
    double dueAmt = 0;
    Date newRntPrdFrm = null;
    Date newRntPrdTo = null;
    Date jNewRntPrdTo = null;
    Date dtAppLoginDate = null;
    Calendar cal = Calendar.getInstance();
    //On disbursal transaction
    double totalAmount = 0.0;
    double sanctionAmount = 0.0;
    double avalBalance = 0.0;
    double balanceAmt = 0.0;
    double noOfInst = 0.0;
    int intnoOfInstInFcsLst = 0;
    double totPenalInst = 0.0;
    int inttotPenalInst = 0;
    TransactionUI transactionUI = new TransactionUI();
    double amtBorrow = 0.0;
    String multiDis = "";
    boolean fromAuthorizeUI = false;
    boolean fromCashierAuthorizeUI = false;
    boolean fromManagerAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
    AuthorizeListDebitUI ManagerauthorizeListUI = null;
    AuthorizeListCreditUI CashierauthorizeListUI = null;
    public String appLoginDate = CommonUtil.convertObjToStr(ClientUtil.getCurrentDate());
    //  private com.see.truetransact.clientutil.ComboBoxModel cbo;
    // private com.see.truetransact.clientutil.ComboBoxModel cbo1;
    public String rmNumber = "";
    public CommonMethod cm;
    private int rejectFlag = 0;
    private final static double Avg_Millis_Per_Month = (365.24 * 24 * 60 * 60 * 1000) / 12;
    DecimalFormat df = new DecimalFormat("0.##");
    DecimalFormat df1 = new DecimalFormat("0");
    int k = 0;
    PrintClass print = new PrintClass();
    ButtonGroup chkGroup = new ButtonGroup();
    private Date currDate = null;
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;
    private List rentTransHeadList ;
    private ServiceTaxCalculation objServiceTax;
    public HashMap serviceTax_Map;

    /** Creates new form ifrNewBorrowing */
    public frmRentTrans() {
        ProxyParameters.BRANCH_ID = TrueTransactMain.BRANCH_ID;
        setSelectedBranchID(TrueTransactMain.BRANCH_ID);
        currDate = ClientUtil.getCurrentDate();
        initComponents();
        setFieldNames();
        setObservable();
        observable.resetForm();
        setMaxLengths();
        initComponentData();
        setMandatoryHashMap();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panRentTrans, getMandatoryHashMap());
        panTrans.add(transactionUI);
        transactionUI.setSourceScreen("RENTTRANS");
        observable.setTransactionOB(transactionUI.getTransactionOB());
        ClientUtil.enableDisable(panRentTrans, false);
        transactionUI.cancelAction(false);
        setButtonEnableDisable();
        btnClose.setVisible(true);
        tdtClosedDate.setEnabled(false);
        txtBuildingNo.setEnabled(false);
        txtRoomNo.setEnabled(false);
        txtTransDate.setEnabled(false);
        cm = new CommonMethod();
        tdtRentDate.setEnabled(false);
        txtTotal.setEnabled(false);
        tdtRentCDate.setEnabled(false);
        //  tdtRentPto.setEnabled(false);
        tdtRentPFrm.setEnabled(false);
        txtPenelAmt.setEnabled(false);
        btnCancel.setEnabled(true);
//        chkGroup.add(cbClosure);
//        chkGroup.add(cbDefaulter);
    }

    private void initComponentData() {
        try {
            lblBuildingNo.setVisible(false);
            txtBuildingNo.setVisible(false);
            btnBuildingNo.setVisible(false);
            btnCalPen.setEnabled(false);
            lblRentDate.setVisible(false);
            tdtRentDate.setVisible(false);
        } catch (ClassCastException e) {
            //parseException.logException(e,true);
            System.out.println("Error in initComponentData():" + e);
        }
    }

    /* Auto Generated Method - setFieldNames()
    This method assigns name for all the components.
    Other functions are working based on this name. */
    private void setFieldNames() {
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        //   lblMsg.setName("lblMsg");
        tbrSale.setName("tbrSale");
        txtBuildingNo.setName("txtBuildingNo");
        txtRoomNo.setName("txtRoomNo");
        tdtRentDate.setName("tdtRentDate");
        txtTransDate.setName("txtTransDate");
        tdtRentPFrm.setName("tdtRentPFrm");
        tdtRentPto.setName("tdtRentPto");
        txtDueAmt.setName("txtDueAmt");
        txtRentAmt.setName("txtRentAmt");
        txtPenelAmt.setName("txtPenelAmt");
        txtNoticeAmt.setName("txtNoticeAmt");
        txtLegalAmt.setName("txtLegalAmt");
        txtArbAmt.setName("txtArbAmt");
        txtCourtAmt.setName("txtCourtAmt");
        txtExeAmt.setName("txtExeAmt");
        cbClosure.setName("cbClosure");
        tdtClosedDate.setName("tdtClosedDate");

        lblBuildingNo.setName("lblBuildingNo");
        lblRoomNo.setName("lblRoomNo");
        lblRentDate.setName("lblRentDate");
        lblTransDate.setName("lblTransDate");
        lblRentPdFrom.setName("lblRentPdFrom");
        lblRentPdTo.setName("lblRentPdTo");
        lblDueAmt.setName("lblDueAmt");
        lblRentAmt.setName("lblRentAmt");
        lblPenalAmt.setName("lblPenalAmt");
        lblNoticeAmt.setName("lblNoticeAmt");
        lblLegalAmt.setName("lblLegalAmt");
        lblArbAmt.setName("lblArbAmt");
        lblCourtAmt.setName("lblCourtAmt");
        lblExeAmt.setName("lblExeAmt");
        lblClosedDate.setName("lblClosedDate");
        txtTotal.setName("txtTotal");
        tdtRentCDate.setName("tdtRentCDate");
        btnBuildingNo.setName("btnBuildingNo");

    }

    private void setMaxLengths() {
        txtRentAmt.setValidation(new CurrencyValidation());
        txtPenelAmt.setValidation(new CurrencyValidation());
        txtNoticeAmt.setValidation(new CurrencyValidation());
        txtLegalAmt.setValidation(new CurrencyValidation());
        txtArbAmt.setValidation(new CurrencyValidation());
        txtCourtAmt.setValidation(new CurrencyValidation());
        txtExeAmt.setValidation(new CurrencyValidation());
        txtTotal.setValidation(new CurrencyValidation());
        txtDueAmt.setValidation(new CurrencyValidation());
    }

    private void setObservable() {
        try {
            observable = RentTransOB.getInstance();
            observable.addObserver(this);
        } catch (Exception e) {
            //parseException.logException(e,true);
            System.out.println("Error in setObservable():" + e);
        }
    }

    public void getTotalAmount() {
        List taxSettingsList = new ArrayList();
        String rentAmt = txtRentAmt.getText();
        String penelAmt = txtPenelAmt.getText();
        String noticeAmt = txtNoticeAmt.getText();
        String legalAmt = txtLegalAmt.getText();
        String arbAmt = txtArbAmt.getText();
        String courtAmt = txtCourtAmt.getText();
        String exeAmt = txtExeAmt.getText();
        String tot = "";
        double total = 0;
        //   displayAlert("rentAmt="+rentAmt+" penelAmt="+penelAmt+" noticeAmt="+noticeAmt+
        //    " legalAmt="+legalAmt+" arbAmt="+arbAmt+" arbAmt="+arbAmt+" courtAmt="+courtAmt+"exeAmt="+exeAmt);
        if (rentAmt != null && !rentAmt.equalsIgnoreCase("")) {
            total = total + Double.parseDouble(rentAmt);
        }
        if (penelAmt != null && !penelAmt.equalsIgnoreCase("")) {
            total = total + Double.parseDouble(penelAmt);
        }
        if (noticeAmt != null && !noticeAmt.equalsIgnoreCase("")) {
            total = total + Double.parseDouble(noticeAmt);
        }
        if (legalAmt != null && !legalAmt.equalsIgnoreCase("")) {
            total = total + Double.parseDouble(legalAmt);
        }
        if (arbAmt != null && !arbAmt.equalsIgnoreCase("")) {
            total = total + Double.parseDouble(arbAmt);
        }
        if (courtAmt != null && !courtAmt.equalsIgnoreCase("")) {
            total = total + Double.parseDouble(courtAmt);
        }
        if (exeAmt != null && !exeAmt.equalsIgnoreCase("")) {
            total = total + Double.parseDouble(exeAmt);
        }
        // CurrencyValidation.formatCrore(String.valueOf(total));
        txtTotal.setText(CurrencyValidation.formatCrore(String.valueOf(total)));
        txtTotal.setEnabled(false);
         // Added by nithya for GST changes
        HashMap taxMap;
        if(TrueTransactMain.SERVICE_TAX_REQ.equals("Y")){
            if (rentTransHeadList != null && rentTransHeadList.size() > 0) {
                HashMap rentTransHeadMap = (HashMap) rentTransHeadList.get(0);
                //-- GST for Penal --
                if (txtPenelAmt.getText().length() > 0) {
                    String achd = CommonUtil.convertObjToStr(rentTransHeadMap.get("PENEL_AC_HD_ID"));
                    HashMap checkForTaxMap = observable.checkServiceTaxApplicable(achd);                   
                    taxMap = getGSTAmountMap(checkForTaxMap);
                     taxMap.put(ServiceTaxCalculation.TOT_AMOUNT, txtPenelAmt.getText());
                    if (taxMap != null && taxMap.size() > 0) {
                        taxSettingsList.add(taxMap);
                    }
                }
                //-- GST for rent
                if (txtRentAmt.getText().length() > 0) {
                    String achd = CommonUtil.convertObjToStr(rentTransHeadMap.get("RENT_AC_HD_ID"));
                    HashMap checkForTaxMap = observable.checkServiceTaxApplicable(achd);
                    taxMap = getGSTAmountMap(checkForTaxMap);
                    taxMap.put(ServiceTaxCalculation.TOT_AMOUNT, txtRentAmt.getText());
                    if (taxMap != null && taxMap.size() > 0) {
                        taxSettingsList.add(taxMap);
                    }
                }
                //-- GST for notice amount
                if (txtNoticeAmt.getText().length() > 0) {
                    String achd = CommonUtil.convertObjToStr(rentTransHeadMap.get("NOTICE_AC_HD_ID"));
                    HashMap checkForTaxMap = observable.checkServiceTaxApplicable(achd);
                    taxMap = getGSTAmountMap(checkForTaxMap);
                    taxMap.put(ServiceTaxCalculation.TOT_AMOUNT, txtNoticeAmt.getText());
                    if (taxMap != null && taxMap.size() > 0) {
                        taxSettingsList.add(taxMap);
                    }
                }
                //-- GST for legal amount
                if (txtLegalAmt.getText().length() > 0) {
                    String achd = CommonUtil.convertObjToStr(rentTransHeadMap.get("LEGAL_AC_HD_ID"));
                    HashMap checkForTaxMap = observable.checkServiceTaxApplicable(achd);
                    taxMap = getGSTAmountMap(checkForTaxMap);
                    taxMap.put(ServiceTaxCalculation.TOT_AMOUNT, txtLegalAmt.getText());
                    if (taxMap != null && taxMap.size() > 0) {
                        taxSettingsList.add(taxMap);
                    }
                }
                //-- GST for arb amount
                if (txtArbAmt.getText().length() > 0) {
                    String achd = CommonUtil.convertObjToStr(rentTransHeadMap.get("ARB_AC_HD_ID"));
                    HashMap checkForTaxMap = observable.checkServiceTaxApplicable(achd);
                    taxMap = getGSTAmountMap(checkForTaxMap);
                    taxMap.put(ServiceTaxCalculation.TOT_AMOUNT, txtArbAmt.getText());
                    if (taxMap != null && taxMap.size() > 0) {
                        taxSettingsList.add(taxMap);
                    }
                }

                //-- GST for court amount
                if (txtCourtAmt.getText().length() > 0) {
                    String achd = CommonUtil.convertObjToStr(rentTransHeadMap.get("COURT_AC_HD_ID"));
                    HashMap checkForTaxMap = observable.checkServiceTaxApplicable(achd);
                    taxMap = getGSTAmountMap(checkForTaxMap);
                    taxMap.put(ServiceTaxCalculation.TOT_AMOUNT, txtCourtAmt.getText());
                    if (taxMap != null && taxMap.size() > 0) {
                        taxSettingsList.add(taxMap);
                    }
                }
                //-- GST for exec amount
                if (txtExeAmt.getText().length() > 0) {
                    String achd = CommonUtil.convertObjToStr(rentTransHeadMap.get("EXE_AC_HD_ID"));
                    HashMap checkForTaxMap = observable.checkServiceTaxApplicable(achd);
                    taxMap = getGSTAmountMap(checkForTaxMap);
                    taxMap.put(ServiceTaxCalculation.TOT_AMOUNT, txtExeAmt.getText());
                    if (taxMap != null && taxMap.size() > 0) {
                        taxSettingsList.add(taxMap);
                    }
                }
                System.out.println("taxSettingsList :: " + taxSettingsList);
                setCaseExpensesAmount(taxSettingsList);
                if(CommonUtil.convertObjToDouble(lblServiceTaxval.getText()) > 0){
                    total = total + CommonUtil.convertObjToDouble(lblServiceTaxval.getText());
                    txtTotal.setText(CurrencyValidation.formatCrore(String.valueOf(total)));
                }                
            }
        }
        //End
    }
    
    private HashMap getGSTAmountMap(HashMap checkForTaxMap){
        HashMap taxMap = new HashMap();
        if (checkForTaxMap.containsKey("SERVICE_TAX_APPLICABLE") && checkForTaxMap.get("SERVICE_TAX_APPLICABLE") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_APPLICABLE")).equalsIgnoreCase("Y")) {
            if (checkForTaxMap.containsKey("SERVICE_TAX_ID") && checkForTaxMap.get("SERVICE_TAX_ID") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_ID")).length() > 0) {
               taxMap.put("SETTINGS_ID", checkForTaxMap.get("SERVICE_TAX_ID"));    
            }
        }
        return taxMap;
    }
    
    private void setCaseExpensesAmount(List taxSettingsList) {       
        if (taxSettingsList != null && taxSettingsList.size() > 0) {
            HashMap ser_Tax_Val = new HashMap();
            ser_Tax_Val.put(ServiceTaxCalculation.CURR_DT, currDate);
            ser_Tax_Val.put("SERVICE_TAX_DATA", taxSettingsList);
            try {
                objServiceTax = new ServiceTaxCalculation();
                serviceTax_Map = objServiceTax.calculateServiceTax(ser_Tax_Val);
                if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.TOT_TAX_AMT)) {
                    String amt = CommonUtil.convertObjToStr(serviceTax_Map.get(ServiceTaxCalculation.TOT_TAX_AMT));
                    lblServiceTaxval.setText(amt);
                    observable.setLblServiceTaxval(lblServiceTaxval.getText());
                    serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, amt);
                } else {
                     lblServiceTaxval.setText("0.00");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tbrSale = new com.see.truetransact.uicomponent.CToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lbSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace51 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace52 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lbSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace53 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace54 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace55 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace56 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        lblSpace57 = new com.see.truetransact.uicomponent.CLabel();
        btnDefaulterNClosed = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblSpace = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        panRentTrans = new com.see.truetransact.uicomponent.CPanel();
        lblRentCDate = new com.see.truetransact.uicomponent.CLabel();
        txtNoticeAmt = new com.see.truetransact.uicomponent.CTextField();
        tdtRentCDate = new com.see.truetransact.uicomponent.CDateField();
        lblTransDate = new com.see.truetransact.uicomponent.CLabel();
        lblRentPdFrom = new com.see.truetransact.uicomponent.CLabel();
        lblPenalAmt = new com.see.truetransact.uicomponent.CLabel();
        lblRentPdTo = new com.see.truetransact.uicomponent.CLabel();
        lblDueAmt = new com.see.truetransact.uicomponent.CLabel();
        txtTransDate = new com.see.truetransact.uicomponent.CTextField();
        lblRentAmt = new com.see.truetransact.uicomponent.CLabel();
        txtRentAmt = new com.see.truetransact.uicomponent.CTextField();
        txtPenelAmt = new com.see.truetransact.uicomponent.CTextField();
        txtLegalAmt = new com.see.truetransact.uicomponent.CTextField();
        txtCourtAmt = new com.see.truetransact.uicomponent.CTextField();
        tdtClosedDate = new com.see.truetransact.uicomponent.CDateField();
        lblClosedDate = new com.see.truetransact.uicomponent.CLabel();
        lblExeAmt = new com.see.truetransact.uicomponent.CLabel();
        lblCourtAmt = new com.see.truetransact.uicomponent.CLabel();
        lblArbAmt = new com.see.truetransact.uicomponent.CLabel();
        lblLegalAmt = new com.see.truetransact.uicomponent.CLabel();
        lblNoticeAmt = new com.see.truetransact.uicomponent.CLabel();
        txtExeAmt = new com.see.truetransact.uicomponent.CTextField();
        txtArbAmt = new com.see.truetransact.uicomponent.CTextField();
        tdtRentPFrm = new com.see.truetransact.uicomponent.CDateField();
        cbClosure = new com.see.truetransact.uicomponent.CCheckBox();
        tdtRentPto = new com.see.truetransact.uicomponent.CDateField();
        lblBuildingNo = new com.see.truetransact.uicomponent.CLabel();
        lblRoomNo = new com.see.truetransact.uicomponent.CLabel();
        txtBuildingNo = new com.see.truetransact.uicomponent.CTextField();
        txtRoomNo = new com.see.truetransact.uicomponent.CTextField();
        btnBuildingNo = new com.see.truetransact.uicomponent.CButton();
        btnRoomNo = new com.see.truetransact.uicomponent.CButton();
        txtTotal = new com.see.truetransact.uicomponent.CTextField();
        lblTotal = new com.see.truetransact.uicomponent.CLabel();
        txtDueAmt = new com.see.truetransact.uicomponent.CTextField();
        lblRentDate = new com.see.truetransact.uicomponent.CLabel();
        tdtRentDate = new com.see.truetransact.uicomponent.CDateField();
        lblName = new com.see.truetransact.uicomponent.CLabel();
        btnCalPen = new com.see.truetransact.uicomponent.CButton();
        lblInstPend = new com.see.truetransact.uicomponent.CLabel();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        cbDefaulter = new com.see.truetransact.uicomponent.CCheckBox();
        lblAcNumber = new com.see.truetransact.uicomponent.CLabel();
        txtAcNumber = new com.see.truetransact.uicomponent.CTextField();
        txtTotal2 = new com.see.truetransact.uicomponent.CTextField();
        txtTotal3 = new com.see.truetransact.uicomponent.CTextField();
        txtTotal4 = new com.see.truetransact.uicomponent.CTextField();
        jLabel1 = new javax.swing.JLabel();
        lblServiceTaxval = new javax.swing.JLabel();
        panTrans = new com.see.truetransact.uicomponent.CPanel();
        mbrTokenConfig = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptView = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMaximumSize(new java.awt.Dimension(840, 600));
        setMinimumSize(new java.awt.Dimension(840, 600));
        setPreferredSize(new java.awt.Dimension(840, 600));
        getContentPane().setLayout(null);

        tbrSale.setMaximumSize(new java.awt.Dimension(820, 29));
        tbrSale.setMinimumSize(new java.awt.Dimension(820, 29));
        tbrSale.setPreferredSize(new java.awt.Dimension(820, 29));

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setFocusable(false);
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        tbrSale.add(btnView);

        lbSpace3.setText("     ");
        tbrSale.add(lbSpace3);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.setFocusable(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrSale.add(btnNew);

        lblSpace51.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace51.setText("     ");
        lblSpace51.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSale.add(lblSpace51);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.setFocusable(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrSale.add(btnEdit);

        lblSpace52.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace52.setText("     ");
        lblSpace52.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSale.add(lblSpace52);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.setFocusable(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrSale.add(btnDelete);

        lbSpace2.setText("     ");
        tbrSale.add(lbSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.setFocusable(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrSale.add(btnSave);

        lblSpace53.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace53.setText("     ");
        lblSpace53.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSale.add(lblSpace53);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.setEnabled(false);
        btnCancel.setFocusable(false);
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrSale.add(btnCancel);

        lblSpace3.setText("     ");
        tbrSale.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrSale.add(btnAuthorize);

        lblSpace54.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace54.setText("     ");
        lblSpace54.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSale.add(lblSpace54);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.setFocusable(false);
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrSale.add(btnException);

        lblSpace55.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace55.setText("     ");
        lblSpace55.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSale.add(lblSpace55);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrSale.add(btnReject);

        lblSpace5.setText("     ");
        tbrSale.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.setFocusable(false);
        tbrSale.add(btnPrint);

        lblSpace56.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace56.setText("     ");
        lblSpace56.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSale.add(lblSpace56);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.setFocusable(false);
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrSale.add(btnClose);

        lblSpace57.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace57.setText("     ");
        lblSpace57.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace57.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace57.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSale.add(lblSpace57);

        btnDefaulterNClosed.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDefaulterNClosed.setToolTipText("Closed Deposits");
        btnDefaulterNClosed.setFocusable(false);
        btnDefaulterNClosed.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDefaulterNClosed.setMinimumSize(new java.awt.Dimension(29, 27));
        btnDefaulterNClosed.setPreferredSize(new java.awt.Dimension(29, 27));
        btnDefaulterNClosed.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDefaulterNClosed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDefaulterNClosedActionPerformed(evt);
            }
        });
        tbrSale.add(btnDefaulterNClosed);

        getContentPane().add(tbrSale);
        tbrSale.setBounds(0, 0, 820, 30);

        panStatus.setMinimumSize(new java.awt.Dimension(250, 22));
        panStatus.setLayout(new java.awt.GridBagLayout());

        lblStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus.setText("                      ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panStatus.add(lblStatus, gridBagConstraints);

        lblSpace.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panStatus.add(lblSpace, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 664;
        gridBagConstraints.ipady = 22;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panStatus.add(lblMsg, gridBagConstraints);

        getContentPane().add(panStatus);
        panStatus.setBounds(11, 547, 250, 22);

        cPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        cPanel1.setMaximumSize(new java.awt.Dimension(830, 300));
        cPanel1.setMinimumSize(new java.awt.Dimension(830, 300));
        cPanel1.setPreferredSize(new java.awt.Dimension(830, 300));
        cPanel1.setLayout(new java.awt.GridBagLayout());

        panRentTrans.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panRentTrans.setMaximumSize(new java.awt.Dimension(700, 295));
        panRentTrans.setMinimumSize(new java.awt.Dimension(700, 295));
        panRentTrans.setPreferredSize(new java.awt.Dimension(700, 295));
        panRentTrans.setLayout(new java.awt.GridBagLayout());

        lblRentCDate.setText("Current Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 66, 0, 0);
        panRentTrans.add(lblRentCDate, gridBagConstraints);

        txtNoticeAmt.setAllowAll(true);
        txtNoticeAmt.setMinimumSize(new java.awt.Dimension(110, 21));
        txtNoticeAmt.setPreferredSize(new java.awt.Dimension(110, 21));
        txtNoticeAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNoticeAmtFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNoticeAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 45;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 0);
        panRentTrans.add(txtNoticeAmt, gridBagConstraints);

        tdtRentCDate.setEnabled(false);
        tdtRentCDate.setMinimumSize(new java.awt.Dimension(110, 21));
        tdtRentCDate.setPreferredSize(new java.awt.Dimension(110, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 11;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        panRentTrans.add(tdtRentCDate, gridBagConstraints);

        lblTransDate.setText("Transaction Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 66, 0, 0);
        panRentTrans.add(lblTransDate, gridBagConstraints);

        lblRentPdFrom.setText("Rent Period from");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 66, 0, 0);
        panRentTrans.add(lblRentPdFrom, gridBagConstraints);

        lblPenalAmt.setText("Penal Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 20;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 0);
        panRentTrans.add(lblPenalAmt, gridBagConstraints);

        lblRentPdTo.setText("Rent Period to");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 66, 0, 0);
        panRentTrans.add(lblRentPdTo, gridBagConstraints);

        lblDueAmt.setText("Due Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 24;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 66, 0, 0);
        panRentTrans.add(lblDueAmt, gridBagConstraints);

        txtTransDate.setAllowAll(true);
        txtTransDate.setEnabled(false);
        txtTransDate.setMinimumSize(new java.awt.Dimension(110, 21));
        txtTransDate.setPreferredSize(new java.awt.Dimension(110, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 11;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        panRentTrans.add(txtTransDate, gridBagConstraints);

        lblRentAmt.setText("Rent Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 28;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 66, 0, 0);
        panRentTrans.add(lblRentAmt, gridBagConstraints);

        txtRentAmt.setAllowAll(true);
        txtRentAmt.setMinimumSize(new java.awt.Dimension(110, 21));
        txtRentAmt.setPreferredSize(new java.awt.Dimension(110, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 28;
        gridBagConstraints.gridwidth = 11;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 0);
        panRentTrans.add(txtRentAmt, gridBagConstraints);

        txtPenelAmt.setAllowAll(true);
        txtPenelAmt.setMinimumSize(new java.awt.Dimension(110, 21));
        txtPenelAmt.setPreferredSize(new java.awt.Dimension(110, 21));
        txtPenelAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPenelAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 45;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 0);
        panRentTrans.add(txtPenelAmt, gridBagConstraints);

        txtLegalAmt.setAllowAll(true);
        txtLegalAmt.setMinimumSize(new java.awt.Dimension(110, 21));
        txtLegalAmt.setPreferredSize(new java.awt.Dimension(110, 21));
        txtLegalAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtLegalAmtFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtLegalAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 45;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 0);
        panRentTrans.add(txtLegalAmt, gridBagConstraints);

        txtCourtAmt.setAllowAll(true);
        txtCourtAmt.setMinimumSize(new java.awt.Dimension(110, 21));
        txtCourtAmt.setPreferredSize(new java.awt.Dimension(110, 21));
        txtCourtAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCourtAmtFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCourtAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 45;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 0);
        panRentTrans.add(txtCourtAmt, gridBagConstraints);

        tdtClosedDate.setMinimumSize(new java.awt.Dimension(110, 21));
        tdtClosedDate.setPreferredSize(new java.awt.Dimension(110, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 45;
        gridBagConstraints.gridy = 21;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 0);
        panRentTrans.add(tdtClosedDate, gridBagConstraints);

        lblClosedDate.setText("Closed Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 20;
        gridBagConstraints.gridy = 21;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 0);
        panRentTrans.add(lblClosedDate, gridBagConstraints);

        lblExeAmt.setText("Execution Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 20;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridwidth = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 0);
        panRentTrans.add(lblExeAmt, gridBagConstraints);

        lblCourtAmt.setText("Court Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 20;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 0);
        panRentTrans.add(lblCourtAmt, gridBagConstraints);

        lblArbAmt.setText("Arbitration Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 20;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 0);
        panRentTrans.add(lblArbAmt, gridBagConstraints);

        lblLegalAmt.setText("Legal Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 20;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 0);
        panRentTrans.add(lblLegalAmt, gridBagConstraints);

        lblNoticeAmt.setText("Notice Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 20;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 0);
        panRentTrans.add(lblNoticeAmt, gridBagConstraints);

        txtExeAmt.setAllowAll(true);
        txtExeAmt.setMinimumSize(new java.awt.Dimension(110, 21));
        txtExeAmt.setPreferredSize(new java.awt.Dimension(110, 21));
        txtExeAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtExeAmtFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtExeAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 45;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 0);
        panRentTrans.add(txtExeAmt, gridBagConstraints);

        txtArbAmt.setAllowAll(true);
        txtArbAmt.setMinimumSize(new java.awt.Dimension(110, 21));
        txtArbAmt.setPreferredSize(new java.awt.Dimension(110, 21));
        txtArbAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtArbAmtFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtArbAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 45;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 0);
        panRentTrans.add(txtArbAmt, gridBagConstraints);

        tdtRentPFrm.setEnabled(false);
        tdtRentPFrm.setMinimumSize(new java.awt.Dimension(110, 21));
        tdtRentPFrm.setPreferredSize(new java.awt.Dimension(110, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridwidth = 11;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        panRentTrans.add(tdtRentPFrm, gridBagConstraints);

        cbClosure.setText("Closure");
        cbClosure.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbClosureMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 45;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panRentTrans.add(cbClosure, gridBagConstraints);

        tdtRentPto.setEnabled(false);
        tdtRentPto.setMinimumSize(new java.awt.Dimension(110, 21));
        tdtRentPto.setPreferredSize(new java.awt.Dimension(110, 21));
        tdtRentPto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtRentPtoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridwidth = 11;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        panRentTrans.add(tdtRentPto, gridBagConstraints);

        lblBuildingNo.setText("Building No");
        lblBuildingNo.setMaximumSize(new java.awt.Dimension(80, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 66, 0, 0);
        panRentTrans.add(lblBuildingNo, gridBagConstraints);

        lblRoomNo.setText("Room Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 66, 0, 0);
        panRentTrans.add(lblRoomNo, gridBagConstraints);

        txtBuildingNo.setBackground(new java.awt.Color(220, 220, 220));
        txtBuildingNo.setEditable(false);
        txtBuildingNo.setEnabled(false);
        txtBuildingNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 0);
        panRentTrans.add(txtBuildingNo, gridBagConstraints);

        txtRoomNo.setBackground(new java.awt.Color(220, 220, 220));
        txtRoomNo.setEditable(false);
        txtRoomNo.setEnabled(false);
        txtRoomNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        panRentTrans.add(txtRoomNo, gridBagConstraints);

        btnBuildingNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnBuildingNo.setToolTipText("Search");
        btnBuildingNo.setEnabled(false);
        btnBuildingNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuildingNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 19;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.ipady = -2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 2, 0, 0);
        panRentTrans.add(btnBuildingNo, gridBagConstraints);

        btnRoomNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnRoomNo.setToolTipText("Search");
        btnRoomNo.setEnabled(false);
        btnRoomNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRoomNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 19;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 0);
        panRentTrans.add(btnRoomNo, gridBagConstraints);

        txtTotal.setAllowAll(true);
        txtTotal.setEnabled(false);
        txtTotal.setMinimumSize(new java.awt.Dimension(110, 21));
        txtTotal.setPreferredSize(new java.awt.Dimension(110, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 45;
        gridBagConstraints.gridy = 24;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 0);
        panRentTrans.add(txtTotal, gridBagConstraints);

        lblTotal.setText("Total");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 20;
        gridBagConstraints.gridy = 24;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.ipadx = 45;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 0);
        panRentTrans.add(lblTotal, gridBagConstraints);

        txtDueAmt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtDueAmt.setAllowAll(true);
        txtDueAmt.setEnabled(false);
        txtDueAmt.setMinimumSize(new java.awt.Dimension(110, 21));
        txtDueAmt.setPreferredSize(new java.awt.Dimension(110, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 24;
        gridBagConstraints.gridwidth = 11;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        panRentTrans.add(txtDueAmt, gridBagConstraints);

        lblRentDate.setText("Rent Date");
        lblRentDate.setMaximumSize(new java.awt.Dimension(65, 16));
        lblRentDate.setMinimumSize(new java.awt.Dimension(65, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 66, 0, 0);
        panRentTrans.add(lblRentDate, gridBagConstraints);

        tdtRentDate.setAlignmentX(0.0F);
        tdtRentDate.setAlignmentY(0.0F);
        tdtRentDate.setEnabled(false);
        tdtRentDate.setMaximumSize(new java.awt.Dimension(110, 21));
        tdtRentDate.setMinimumSize(new java.awt.Dimension(110, 21));
        tdtRentDate.setPreferredSize(new java.awt.Dimension(110, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 11;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        panRentTrans.add(tdtRentDate, gridBagConstraints);

        lblName.setMaximumSize(new java.awt.Dimension(120, 21));
        lblName.setMinimumSize(new java.awt.Dimension(120, 21));
        lblName.setPreferredSize(new java.awt.Dimension(120, 21));
        lblName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                lblNameFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 9;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 65, 0, 0);
        panRentTrans.add(lblName, gridBagConstraints);

        btnCalPen.setText("Calculate");
        btnCalPen.setMinimumSize(new java.awt.Dimension(75, 25));
        btnCalPen.setPreferredSize(new java.awt.Dimension(75, 25));
        btnCalPen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalPenActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 16;
        gridBagConstraints.gridy = 21;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panRentTrans.add(btnCalPen, gridBagConstraints);

        lblInstPend.setText("Pending Installments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 20;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 25;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        panRentTrans.add(lblInstPend, gridBagConstraints);

        cLabel1.setMaximumSize(new java.awt.Dimension(110, 21));
        cLabel1.setMinimumSize(new java.awt.Dimension(110, 21));
        cLabel1.setPreferredSize(new java.awt.Dimension(110, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 45;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 2, 0, 0);
        panRentTrans.add(cLabel1, gridBagConstraints);

        cbDefaulter.setText("Defaulter");
        cbDefaulter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbDefaulterMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 47;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 155);
        panRentTrans.add(cbDefaulter, gridBagConstraints);

        lblAcNumber.setText("A/c Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 20;
        gridBagConstraints.gridy = 26;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 0, 0);
        panRentTrans.add(lblAcNumber, gridBagConstraints);

        txtAcNumber.setAllowAll(true);
        txtAcNumber.setEnabled(false);
        txtAcNumber.setMinimumSize(new java.awt.Dimension(110, 21));
        txtAcNumber.setPreferredSize(new java.awt.Dimension(110, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 45;
        gridBagConstraints.gridy = 26;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 0, 0);
        panRentTrans.add(txtAcNumber, gridBagConstraints);

        txtTotal2.setAllowAll(true);
        txtTotal2.setEnabled(false);
        txtTotal2.setMinimumSize(new java.awt.Dimension(110, 21));
        txtTotal2.setPreferredSize(new java.awt.Dimension(110, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 45;
        gridBagConstraints.gridy = 24;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 0);
        panRentTrans.add(txtTotal2, gridBagConstraints);

        txtTotal3.setAllowAll(true);
        txtTotal3.setEnabled(false);
        txtTotal3.setMinimumSize(new java.awt.Dimension(110, 21));
        txtTotal3.setPreferredSize(new java.awt.Dimension(110, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 45;
        gridBagConstraints.gridy = 24;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 0);
        panRentTrans.add(txtTotal3, gridBagConstraints);

        txtTotal4.setAllowAll(true);
        txtTotal4.setEnabled(false);
        txtTotal4.setMinimumSize(new java.awt.Dimension(110, 21));
        txtTotal4.setPreferredSize(new java.awt.Dimension(110, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 45;
        gridBagConstraints.gridy = 24;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 0);
        panRentTrans.add(txtTotal4, gridBagConstraints);

        jLabel1.setText("ServiceTax");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 20;
        gridBagConstraints.gridy = 28;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 12, 0, 0);
        panRentTrans.add(jLabel1, gridBagConstraints);

        lblServiceTaxval.setText("jLabel2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 26;
        gridBagConstraints.gridy = 28;
        gridBagConstraints.gridwidth = 19;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 3, 0, 0);
        panRentTrans.add(lblServiceTaxval, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 96;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 3, 2);
        cPanel1.add(panRentTrans, gridBagConstraints);

        getContentPane().add(cPanel1);
        cPanel1.setBounds(10, 30, 800, 310);

        panTrans.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panTrans.setMaximumSize(new java.awt.Dimension(830, 225));
        panTrans.setMinimumSize(new java.awt.Dimension(830, 225));
        panTrans.setPreferredSize(new java.awt.Dimension(830, 225));
        panTrans.setLayout(new java.awt.GridBagLayout());
        getContentPane().add(panTrans);
        panTrans.setBounds(10, 345, 820, 210);

        mbrTokenConfig.setInheritsPopupMenu(true);

        mnuProcess.setText("Rent Transaction");
        mnuProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuProcessActionPerformed(evt);
            }
        });

        mitNew.setText("New");
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptView);

        mitSave.setText("Save");
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitClose.setText("Close");
        mnuProcess.add(mitClose);

        mbrTokenConfig.add(mnuProcess);

        setJMenuBar(mbrTokenConfig);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtExeAmtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtExeAmtFocusGained
        // TODO add your handling code here:
        transactionUI.resetObjects();
    }//GEN-LAST:event_txtExeAmtFocusGained

    private void txtCourtAmtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCourtAmtFocusGained
        // TODO add your handling code here:
        transactionUI.resetObjects();
    }//GEN-LAST:event_txtCourtAmtFocusGained

    private void txtArbAmtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtArbAmtFocusGained
        // TODO add your handling code here:
        transactionUI.resetObjects();
    }//GEN-LAST:event_txtArbAmtFocusGained

    private void txtLegalAmtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLegalAmtFocusGained
        // TODO add your handling code here:
        transactionUI.resetObjects();
    }//GEN-LAST:event_txtLegalAmtFocusGained

    private void txtNoticeAmtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNoticeAmtFocusGained
        // TODO add your handling code here:
        transactionUI.resetObjects();
    }//GEN-LAST:event_txtNoticeAmtFocusGained

    private void lblNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_lblNameFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_lblNameFocusLost

    private void tdtRentPtoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtRentPtoFocusLost
        // TODO add your handling code here:
        //setPenelInterest();
        btnCalPen.setEnabled(true);

    }//GEN-LAST:event_tdtRentPtoFocusLost

    public void calcDed(double i) {

        if (inttotPenalInst != intnoOfInstInFcsLst) {
            Double ded = 0.0;
            int dednew = 0;
            if (inttotPenalInst > noOfInstInFcsLst) {
                // ded= noOfInst - noOfInstInFcsLst;
                Rounding rod = new Rounding();
                int no = (int) rod.getNearest((long) (i * 100), 100) / 100;
                ded = i;
                ded = inttotPenalInst - ded;
                dednew = (int) rod.getNearest((long) (ded * 100), 100) / 100;
                dedInst = (dednew * (dednew + 1)) / 2;
                dedDue = no * rentAmount;
            }
        }
    }
    private void txtExeAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtExeAmtFocusLost
        // TODO add your handling code here:
        getTotalAmount();
        transactionUI.setCallingAmount(txtTotal.getText());
        transactionUI.setCallingTransType("TRANSFER");
        transactionUI.resetObjects();
    }//GEN-LAST:event_txtExeAmtFocusLost

    private void txtCourtAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCourtAmtFocusLost
        // TODO add your handling code here:
        getTotalAmount();
        transactionUI.setCallingAmount(txtTotal.getText());
        transactionUI.setCallingTransType("TRANSFER");
        transactionUI.resetObjects();
    }//GEN-LAST:event_txtCourtAmtFocusLost

    private void txtArbAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtArbAmtFocusLost
        // TODO add your handling code here:
        getTotalAmount();
        transactionUI.setCallingAmount(txtTotal.getText());
        transactionUI.setCallingTransType("TRANSFER");
    }//GEN-LAST:event_txtArbAmtFocusLost

    private void txtLegalAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLegalAmtFocusLost
        // TODO add your handling code here:
        getTotalAmount();
        transactionUI.setCallingAmount(txtTotal.getText());
        transactionUI.setCallingTransType("TRANSFER");

        transactionUI.resetObjects();
    }//GEN-LAST:event_txtLegalAmtFocusLost

    private void txtNoticeAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNoticeAmtFocusLost
        // TODO add your handling code here:
        getTotalAmount();
        transactionUI.setCallingAmount(txtTotal.getText());
        transactionUI.setCallingTransType("TRANSFER");
        transactionUI.resetObjects();
    }//GEN-LAST:event_txtNoticeAmtFocusLost

    private void txtPenelAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPenelAmtFocusLost
        // TODO add your handling code here:
        getTotalAmount();
        transactionUI.setCallingAmount(txtTotal.getText());
        transactionUI.setCallingTransType("TRANSFER");
        transactionUI.resetObjects();
    }//GEN-LAST:event_txtPenelAmtFocusLost

    private void btnRoomNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRoomNoActionPerformed
        // TODO add your handling code here:
        callView("ROOMNUM");
        setRentDate();
    }//GEN-LAST:event_btnRoomNoActionPerformed

    public void calculateRentAmount() {
        HashMap rentMap = new HashMap();
        rentMap.put("CUR_DATE", currDate.clone());
        rentMap.put("ROOM_NO", txtRoomNo.getText());
        //rentMap.put("RM_NO", txtRoomNo.getText());
        System.out.println("rmNumber nithya :: " + rmNumber);
        rentMap.put("RM_NO", rmNumber);
        rentMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        rentMap.put("ASONDT",DateUtil.getDateMMDDYYYY(tdtRentPto.getDateValue()));
        List rentList = ClientUtil.executeQuery("getRoomRentAmount", rentMap);
//        if (rentList != null && rentList.size() > 0) {
//            rentMap = (HashMap) rentList.get(0);
//            if (rentMap.get("DUE_AMOUNT").equals("") && rentMap.get("DUE_AMOUNT") != null) {
//                txtDueAmt.setText(CommonUtil.convertObjToStr(rentMap.get("DUE_AMOUNT")));
//                txtPenelAmt.setText(CommonUtil.convertObjToStr(rentMap.get("PENAL_AMOUNT")));
//            }
//        }
        
        
        Double rentPenalAmnt = 0.0;
        Double rentDueAmnt = 0.0;
        double arcCost = 0.0;
        if(rentList != null && rentList.size() > 0){
            rentMap = (HashMap) rentList.get(0);
            if(rentMap.containsKey("PENAL_AMOUNT") && rentMap.get("PENAL_AMOUNT") != null && !rentMap.get("PENAL_AMOUNT").equals("")){
                rentPenalAmnt = CommonUtil.convertObjToDouble(rentMap.get("PENAL_AMOUNT"));
                txtPenelAmt.setText(CommonUtil.convertObjToStr(rentPenalAmnt));
            }
            if(rentMap.containsKey("DUE_AMOUNT") && rentMap.get("DUE_AMOUNT") != null && !rentMap.get("DUE_AMOUNT").equals("")){
                rentDueAmnt = CommonUtil.convertObjToDouble(rentMap.get("DUE_AMOUNT"));
                //txtDueAmt.setText(CommonUtil.convertObjToStr(rentDueAmnt));
                txtRentAmt.setText(CommonUtil.convertObjToStr(rentDueAmnt));
            }
            if(rentMap.containsKey("ARC_COST") && rentMap.get("ARC_COST") != null && !rentMap.get("ARC_COST").equals("")){
                arcCost = CommonUtil.convertObjToDouble(rentMap.get("ARC_COST"));
                txtArbAmt.setText(CommonUtil.convertObjToStr(arcCost));
            }
            //Added by nithya on 22-12-2021 for KD-3160
            if(rentDueAmnt > 0){
                double tot = rentPenalAmnt + rentDueAmnt + arcCost;
                txtDueAmt.setText(CommonUtil.convertObjToStr(tot)); 
            }
        }
        
    }

    public void setRentDate() {
        try {
            txtDueAmt.setText("");
            cLabel1.setText("");
            txtPenelAmt.setEditable(true);
            txtPenelAmt.setEnabled(true);
            calculateRent();
            String date = cm.getCurrentDate();
            String d = date.substring(2, 10);
            String buiding_no = txtBuildingNo.getText();
            String room_no = txtRoomNo.getText();
            String acNum = txtAcNumber.getText();
            String aDate = "", rentPto = "";
            String rDate = "";
            String acStatus = "", closesDate = "";
            if (buiding_no != null && room_no != null) {
                HashMap singleAuthorizeMap = new HashMap();
                singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDate.clone());
                singleAuthorizeMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                singleAuthorizeMap.put("BUILDING_NO", buiding_no);
                singleAuthorizeMap.put("ROOM_NO", room_no);
                singleAuthorizeMap.put("RRID", acNum);
                List aList = ClientUtil.executeQuery("getRentDate", singleAuthorizeMap);
                for (int i = 0; i < aList.size(); i++) {
                    HashMap map = (HashMap) aList.get(i);
                    if (map.get("RENT_DATE") != null) {
                        rDate = map.get("RENT_DATE").toString();
                        aDate = rDate + d;
                        acStatus = CommonUtil.convertObjToStr(map.get("ACCOUNT_STATUS"));
                        closesDate = CommonUtil.convertObjToStr(map.get("CLOSES_DATE"));
                        // rentPto=String.valueOf(Integer.parseInt(rDate)+1)+d;
                    }
                }
                if (acStatus.equals("CLOSED")) {
                    cbClosure.setSelected(true);
                    cbClosure.setEnabled(false);
                    cbDefaulter.setEnabled(false);
                    btnSave.setEnabled(false);
                } else if (acStatus.equals("DEFAULTER")) {
                    cbDefaulter.setSelected(true);
                    cbDefaulter.setEnabled(true);
                    cbClosure.setEnabled(true);
                    btnSave.setEnabled(true);
                } else {
                    cbDefaulter.setEnabled(true);
                    cbClosure.setEnabled(true);
                    cbDefaulter.setSelected(false);
                    cbClosure.setSelected(false);
                    btnSave.setEnabled(true);
                }

                btnCancel.setEnabled(true);
                tdtRentDate.setDateValue(aDate);
                tdtRentDate.setEnabled(false);
                // tdtRentPto.setEnabled(false);
                tdtRentPFrm.setEnabled(false);
                tdtClosedDate.setDateValue(closesDate);
                //////set the rent period to and rent period from
                java.util.Date dtRntPrdTo = null;
                Date rntPrdTo = null;
                String nwRntPrdFrm = "";
                String nwRntPrdTo = "";
//                  Date newRntPrdFrm=null;
//                  Date newRntPrdTo=null;
                // Date dtAppLoginDate=null;
                List lstRentPrdTo = ClientUtil.executeQuery("getprevRntPrdTo", singleAuthorizeMap);
                HashMap mpRentPrdTo = new HashMap();
                if (lstRentPrdTo.size() > 0 && lstRentPrdTo != null) {
                    mpRentPrdTo = new HashMap();
                    mpRentPrdTo = (HashMap) lstRentPrdTo.get(0);
                    if (mpRentPrdTo == null || mpRentPrdTo.containsValue(null)) {
                        List lstRentPrdTo1 = ClientUtil.executeQuery("getRoomRentOccupiedDate", singleAuthorizeMap);
                        if (lstRentPrdTo1.size() > 0 && lstRentPrdTo1 != null) {
                            mpRentPrdTo = new HashMap();
                            mpRentPrdTo = (HashMap) lstRentPrdTo1.get(0);
                        }
                    }
                    if (mpRentPrdTo != null && !mpRentPrdTo.containsValue(null)) {
                        rntPrdTo = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(mpRentPrdTo.get("RENT_PERIOD_TO")));
                        //SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);
                        //dtRntPrdTo = dateFormatter.parse(rntPrdTo);
                        newRntPrdFrm = rntPrdTo;
//                      cal.setTime(dtRntPrdTo);
//                      cal.add(Calendar.DATE, 1);
//                      newRntPrdFrm=cal.getTime();
                        //// // SimpleDateFormat form1 = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
                        //SimpleDateFormat form1 = new SimpleDateFormat("dd/MM/yyyy");
                        //try {
                        //    nwRntPrdFrm = form1.format(dtRntPrdTo);
                        //} catch (Exception e) {
                        ///    e.printStackTrace();
                        ///}
                        tdtRentPFrm.setDateValue(DateUtil.getStringDate(rntPrdTo));
                        //SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
                        //dtAppLoginDate = format1.parse(appLoginDate);


//                      cal.setTime(dtAppLoginDate);
//                      cal.add(cal.MONTH, 1);
//                      jNewRntPrdTo=cal.getTime();

                        if (!closesDate.equals("") && !closesDate.equals(null)) {
                            newRntPrdTo = DateUtil.getDateMMDDYYYY(closesDate);
                        } else {
                            newRntPrdTo = (Date) currDate.clone();
                        }
                        double RPTM = ((MonthDiff(newRntPrdTo, newRntPrdFrm)));
                        k = (int) Math.round(RPTM);
                        // int k=(int)RPTM;
//                     String RptM1 = df1.format(RPTM);
////                      noOfInstInFcsLst = Double.parseDouble(strNoOfInst);
////                    Rounding rod =new Rounding();
//                    int RTM= Integer.parseInt(RptM1);
//           //   int RTM= (int)rod.getNearest((long)(RPTM *100),100)/100;
                        //   newRntPrdFrm
                        cal.setTime(newRntPrdFrm);
                        cal.add(Calendar.MONTH, k);
                        newRntPrdTo = cal.getTime();

                        try {
                            //nwRntPrdTo = form1.format(newRntPrdTo);
                            tdtRentPto.setDateValue(DateUtil.getStringDate(newRntPrdTo));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }



                        // tdtRentPto.setDateValue(jNewRntPrdTo.toString());

                        ///////////////////////////////////////////////////
                        /////No of instalments pending///////
                        Double Inst = MonthDiff(newRntPrdTo, newRntPrdFrm);
                        String strNoOfInst = df.format(Inst);
                        noOfInst = Double.parseDouble(strNoOfInst);

//                      HashMap mpInstPend=new HashMap();
//                      mpInstPend.put("RENT_PRD_TO", newRntPrdTo);
//                      mpInstPend.put("RENT_PRD_FRM", newRntPrdFrm);
//                      List lstInstPend=ClientUtil.executeQuery("getSelInstPendInMonths",mpInstPend);
//                      mpInstPend= new HashMap();
//                      mpInstPend= (HashMap) lstInstPend.get(0);
//                      double instPend=Double.parseDouble(mpInstPend.get("INST_PEND").toString());

                        ////////////////////////
                        if (noOfInst > 0) {

                            noOfInst = noOfInst - 1;
                            totPenalInst = noOfInst;
                            Rounding rod = new Rounding();
                            inttotPenalInst = (int) rod.getNearest((long) (totPenalInst * 100), 100) / 100;
                            dedDue = (inttotPenalInst + 1) * rentAmount;
                            setPenalDue();

                        } else {
                            txtTotal.setText("0.0");
                        }

                    }
                }
                transactionUI.setCallingAmount(txtTotal.getText());
            } else {
                tdtRentDate.setDateValue("");
                tdtRentDate.setEnabled(false);
                tdtRentPFrm.setEnabled(false);
                tdtRentPFrm.setEnabled(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        tdtRentPFrm.setEnabled(false);
    }

    public void enableDisableRentHistory() {
    }

    public void calculateRent() {
        String buiding_no = txtBuildingNo.getText();
        String room_no = txtRoomNo.getText();
        HashMap singleAuthorizeMap = new HashMap();
        singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
        singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDate.clone());
        singleAuthorizeMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        singleAuthorizeMap.put("BUILDING_NUM", buiding_no);
        singleAuthorizeMap.put("ROOMNUM", room_no);
        singleAuthorizeMap.put("RMNUM", rmNumber);
        List lstRntAmtPenal = ClientUtil.executeQuery("getPenalRate1", singleAuthorizeMap);
        if (lstRntAmtPenal.size() > 0 && lstRntAmtPenal != null) {
            HashMap mpRntAmtPnl = (HashMap) lstRntAmtPenal.get(0);
            String penalRt = mpRntAmtPnl.get("PENEL_RATE").toString();
            String rntAmt = mpRntAmtPnl.get("RENT_AMT").toString();
            penalRate = Double.parseDouble(penalRt);
            rentAmount = Double.parseDouble(rntAmt);
        }
    }

    public static double MonthDiff(Date d1, Date d2) {
        return (d1.getTime() - d2.getTime()) / Avg_Millis_Per_Month;
    }

    public void setPenalDue() {
        double dueMonths = 0;
        double penalMonths = 0;
        int penalMonth = 0;
        int dueMonth = 0;
        Date nwRntPrdTo = null;
        Date nwRntPrdFrm = null;
//        String rentPrdFrm = tdtRentPFrm.getDateValue();
//        String rentPrdTo = tdtRentPto.getDateValue();
//        SimpleDateFormat parser1 = new SimpleDateFormat("dd/MM/yyyy");
        try {
            nwRntPrdTo = DateUtil.getDateMMDDYYYY(tdtRentPto.getDateValue());
            nwRntPrdFrm = DateUtil.getDateMMDDYYYY(tdtRentPFrm.getDateValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Double Inst = MonthDiff(nwRntPrdTo, nwRntPrdFrm);
        String strNoOfInst = df.format(Inst);
        noOfInst = Double.parseDouble(strNoOfInst);
        penalAmt = 0.0;
        if (totPenalInst < 1) {
            long noOfDays = 0;
            noOfDays = DateUtil.dateDiff(newRntPrdFrm, (Date) currDate.clone());
            penalAmt = (noOfDays * penalRate * rentAmount) / 36500;
            overDueAmt = rentAmount;
            penalAmt = 0;
        } else if (totPenalInst >= 1) {
            dueMonths = totPenalInst;
            if (((Date) currDate.clone()).after(newRntPrdFrm)) {
//               if(dedInst>0.0){
                penalMonths = totPenalInst;
//               }
//               else{
//                     penalMonths=totPenalInst-1;
//               }

            }
            Rounding rod = new Rounding();
            penalMonth = (int) rod.getNearest((long) (penalMonths * 100), 100) / 100;
            dueMonth = (int) rod.getNearest((long) (dueMonths * 100), 100) / 100;
        }
        if (penalMonth > 0) {
            Double n = (penalMonth * (penalMonth + 1) / 2) - dedInst;
            penalAmt = (rentAmount * penalRate * n) / 1200;
        }
        // overDueAmt = (dueMonth*rentAmount)-dedDue; 
        overDueAmt = (dueMonth * rentAmount);
        dueAmt = overDueAmt + penalAmt;
        penalAmt = (double) getNearest((long) (penalAmt * 100), 100) / 100;
        txtDueAmt.setText("" + (dedDue + penalAmt));
        txtRentAmt.setText("" + dedDue);
        txtPenelAmt.setText("" + penalAmt);
        double tot = dedDue + penalAmt;
        dedDue = 0;
        //Added by sreekrishnan
        calculateRentAmount();
        getTotalAmount();
        //txtTotal.setText(""+tot);
        transactionUI.setCallingAmount(txtTotal.getText());
        dueAmt = 0;
        dedInst = 0;

        noOfInstInFcsLst = 0;
        penalMonths = Math.floor(penalMonths);
        int pnlMth = CommonUtil.convertObjToInt(Math.floor(penalMonths));
        if (nwRntPrdFrm.compareTo((Date) currDate.clone()) > 0) {
            cLabel1.setText("0");
        } else {
            cLabel1.setText("" + (k - 1));
        }
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

    public void setPenelInterest() {
        try {
            String buiding_no = txtBuildingNo.getText();
            String room_no = txtRoomNo.getText();
            String dueDate = tdtRentDate.getDateValue();
            String currDate = tdtRentCDate.getDateValue();
            String penalGperiod = "";
            String penalRate = "", rentAmt = "";
            double penalAmt = 0;
            if (buiding_no != null && room_no != null) {
                HashMap singleAuthorizeMap1 = new HashMap();
                singleAuthorizeMap1.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                singleAuthorizeMap1.put(CommonConstants.AUTHORIZEDT, currDate);
                singleAuthorizeMap1.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                singleAuthorizeMap1.put("BUILDING_NUM", buiding_no);
                singleAuthorizeMap1.put("ROOMNUM", room_no);
                List aList1 = ClientUtil.executeQuery("getPenalRate1", singleAuthorizeMap1);
                for (int i = 0; i < aList1.size(); i++) {
                    HashMap map = (HashMap) aList1.get(i);
                    if (map.get("PENEL_RATE") != null) {
                        penalRate = map.get("PENEL_RATE").toString();
                    }
                    if (map.get("RENT_AMT") != null) {
                        rentAmt = map.get("RENT_AMT").toString();
                    }

                }
                HashMap singleAuthorizeMap = new HashMap();
                singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDate);
                singleAuthorizeMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                singleAuthorizeMap.put("BUILDING_NO", buiding_no);
                singleAuthorizeMap.put("ROOM_NO", room_no);
                List aList = ClientUtil.executeQuery("getGracePeriod", singleAuthorizeMap);
                for (int i = 0; i < aList.size(); i++) {
                    HashMap map = (HashMap) aList.get(i);
                    if (map.get("PENAL_GR_PERIOD") != null) {
                        penalGperiod = map.get("PENAL_GR_PERIOD").toString();
                    }
                }
                txtPenelAmt.setText("0.00");
                if (penalGperiod != null && !penalGperiod.equalsIgnoreCase("")) {
                    int pgp = Integer.parseInt(penalGperiod);
                    long datediff = getDateDiff(dueDate, currDate);
                    if (datediff > 0) {
                        penalAmt = Double.parseDouble(rentAmt) * datediff * Double.parseDouble(penalRate) / 36500;
                        penalAmt = (double) getNearest((long) (penalAmt * 100), 100) / 100;
                        txtPenelAmt.setText(String.valueOf(penalAmt));
                    }
                }

            }
        } catch (Exception e) {
            System.out.println("Error in setPenelInterest():" + e);
        }
    }

    public long getDateDiff(String d1, String d2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        int y1 = Integer.parseInt(d1.substring(d1.length() - 4, d1.length()));
        int m1 = Integer.parseInt(d1.substring(d1.length() - 7, d1.length() - 5));
        int da1 = Integer.parseInt(d1.substring(0, d1.length() - 8));
        int y2 = Integer.parseInt(d2.substring(d2.length() - 4, d2.length()));
        int m2 = Integer.parseInt(d2.substring(d2.length() - 7, d2.length() - 5));
        int da2 = Integer.parseInt(d2.substring(0, d2.length() - 8));
        cal1.set(y1, m1, da1);
        cal2.set(y2, m2, da2);
        long milis1 = cal1.getTimeInMillis();
        long milis2 = cal2.getTimeInMillis();
        long diff = milis2 - milis1;
        long diffDays = diff / (24 * 60 * 60 * 1000);
        return diffDays;
    }

    public Date getDate(String in) {
        Date theDate = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            theDate = dateFormat.parse(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return theDate;
    }

    public static void main(String args[]) throws Exception {
        try {
            // frmSale objIfrRenewal=new frmSale();
            //  objIfrRenewal.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        //this.dispose();
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        txtBuildingNo.setEnabled(false);
        txtRoomNo.setEnabled(false);
        txtTransDate.setEnabled(false);
        tdtRentDate.setEnabled(false);
        txtTotal.setEnabled(false);
        tdtRentCDate.setEnabled(false);
        //tdtRentPto.setEnabled(false);
        tdtRentPFrm.setEnabled(false);
        txtPenelAmt.setEnabled(false);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        txtBuildingNo.setEnabled(false);
        txtRoomNo.setEnabled(false);
        txtTransDate.setEnabled(false);
        tdtRentDate.setEnabled(false);
        txtTotal.setEnabled(false);
        tdtRentCDate.setEnabled(false);
        //  tdtRentPto.setEnabled(false);
        tdtRentPFrm.setEnabled(false);
        txtPenelAmt.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        txtBuildingNo.setEnabled(false);
        txtRoomNo.setEnabled(false);
        txtTransDate.setEnabled(false);
        tdtRentDate.setEnabled(false);
        txtTotal.setEnabled(false);
        tdtRentCDate.setEnabled(false);
        //  tdtRentPto.setEnabled(false);
        tdtRentPFrm.setEnabled(false);
        txtPenelAmt.setEnabled(false);
        HashMap editLockMap = new HashMap();
        editLockMap.put("SCREEN_ID", getScreenID());
        editLockMap.put("RECORD_KEY", txtRoomNo.getText());
        editLockMap.put("LOCKED_BY", TrueTransactMain.USER_ID);
        editLockMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        editLockMap.put("CUR_DATE", currDate.clone());
        editLockMap.put("RECORD_KEY", "null");
        setEditLockMap(editLockMap);
        //ClientUtil.executeQuery("deleteEditLock", editLockMap);
        removeEditLock(txtRoomNo.getText());
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
            whereMap.put(CommonConstants.HIERARCHY_ID, ProxyParameters.HIERARCHY_ID);
            whereMap.put("TRANS_DT", currDate.clone());
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            //mapParam.put(CommonConstants.MAP_NAME, "getRentTransAuthorizeList");
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                mapParam.put(CommonConstants.MAP_NAME, "getRentTransCashierAuthorizeList");
            } else {
                mapParam.put(CommonConstants.MAP_NAME, "getRentTransAuthorizeList");
            }
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeRentTrans");
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            observable.setStatus();
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
            // tdtRentPto.setEnabled(false);
            tdtRentPFrm.setEnabled(false);
        } else if (viewType.equals(AUTHORIZE)) {
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDate.clone());
            singleAuthorizeMap.put("RTID", observable.getRtId());
            singleAuthorizeMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            if(TrueTransactMain.SERVICE_TAX_REQ.equals("Y")){
            	HashMap serauthMap = new HashMap();
            	serauthMap.put("ACCT_NUM",observable.getCboRoomNo());
            	serauthMap.put("STATUS", authorizeStatus);
            	serauthMap.put("USER_ID", TrueTransactMain.USER_ID);
            	serauthMap.put("AUTHORIZEDT", currDate);
            	singleAuthorizeMap.put("SER_TAX_AUTH",serauthMap);
	    }
            observable.setAuthMap(singleAuthorizeMap);
            observable.execute(authorizeStatus);
            viewType = "";
            if (fromNewAuthorizeUI) {
                newauthorizeListUI.removeSelectedRow();
                this.dispose();
                newauthorizeListUI.setFocusToTable();
                newauthorizeListUI.displayDetails("Rent Transaction");
            }
            if (fromAuthorizeUI) {
                authorizeListUI.removeSelectedRow();
                this.dispose();
                authorizeListUI.setFocusToTable();
                authorizeListUI.displayDetails("Rent Transaction");
            }
            if (fromCashierAuthorizeUI) {
                CashierauthorizeListUI.removeSelectedRow();
                this.dispose();
                CashierauthorizeListUI.setFocusToTable();
            }
            if (fromManagerAuthorizeUI) {
                ManagerauthorizeListUI.removeSelectedRow();
                this.dispose();
                ManagerauthorizeListUI.setFocusToTable();
            }
            btnCancelActionPerformed(null);
            txtBuildingNo.setEnabled(false);
            txtRoomNo.setEnabled(false);
            txtTransDate.setEnabled(false);
            tdtRentDate.setEnabled(false);
            txtTotal.setEnabled(false);
            tdtRentCDate.setEnabled(false);
            //  tdtRentPto.setEnabled(false);
            tdtRentPFrm.setEnabled(false);
            txtPenelAmt.setEnabled(false);
        }
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        setModified(false);
        HashMap editLockMap = new HashMap();
        editLockMap.put("SCREEN_ID", getScreenID());
        editLockMap.put("RECORD_KEY", txtRoomNo.getText());
        editLockMap.put("LOCKED_BY", TrueTransactMain.USER_ID);
        editLockMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        editLockMap.put("CUR_DATE", currDate.clone());
        editLockMap.put("RECORD_KEY", "null");
        setEditLockMap(editLockMap);
        //ClientUtil.executeQuery("deleteEditLock", editLockMap);
        removeEditLock(txtRoomNo.getText());
        observable.resetForm();
        //txtNoOfTokens.setText("");
        if (fromNewAuthorizeUI) {
            newauthorizeListUI.removeSelectedRow();
            this.dispose();
            fromNewAuthorizeUI = false;
            newauthorizeListUI.setFocusToTable();
        }
        if (fromAuthorizeUI) {
            authorizeListUI.removeSelectedRow();
            this.dispose();
            fromAuthorizeUI = false;
            authorizeListUI.setFocusToTable();
        }
        if (fromCashierAuthorizeUI) {
            CashierauthorizeListUI.removeSelectedRow();
            this.dispose();
            fromCashierAuthorizeUI = false;
            CashierauthorizeListUI.setFocusToTable();
        }
        if (fromManagerAuthorizeUI) {
            ManagerauthorizeListUI.removeSelectedRow();
            this.dispose();
            fromManagerAuthorizeUI = false;
            ManagerauthorizeListUI.setFocusToTable();
        }
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panRentTrans, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        btnSave.setEnabled(true);
        setButtonEnableDisable();
        viewType = "";
        //////////////////////////////////////************************
//        btnAuthorize.setEnabled(true);
//        btnReject.setEnabled(true);
//        btnException.setEnabled(true);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        tdtClosedDate.setEnabled(false);
        txtBuildingNo.setEnabled(false);
        txtRoomNo.setEnabled(false);
        txtTransDate.setEnabled(false);
        tdtRentDate.setEnabled(false);
        txtTotal.setEnabled(false);
        tdtRentCDate.setEnabled(false);
        // tdtRentPto.setEnabled(false);
        tdtRentPFrm.setEnabled(false);
        txtPenelAmt.setEnabled(false);
        tdtRentCDate.setDateValue("");
        txtTransDate.setText("");
        cLabel1.setText("");
        btnCalPen.setEnabled(false);
    }//GEN-LAST:event_btnCancelActionPerformed
    public void fillData(Object map) {

        //        setModified(true);
        HashMap hash = (HashMap) map;
        if (hash.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")) {
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
            btnAuthorize.requestFocusInWindow();
            btnAuthorize.setFocusable(true);
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
            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnAuthorize.requestFocusInWindow();
            btnAuthorize.setFocusable(true);
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
            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnAuthorize.requestFocusInWindow();
            btnAuthorize.setFocusable(true);
            btnReject.setEnabled(false);
            rejectFlag = 1;
        }
        if (hash.containsKey("FROM_CASHIER_AUTHORIZE_LIST_UI")) {
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
            btnAuthorize.requestFocusInWindow();
            btnAuthorize.setFocusable(true);
            btnReject.setEnabled(false);
            rejectFlag = 1;
        }
        if (viewType.equals("BUILDING_NUM")) {
            txtBuildingNo.setText(hash.get("BUILDING_NUM").toString());
            rmNumber = hash.get("RMNUMBER").toString();
        }
        if (viewType.equals("ROOMNUM")) {
            // added by shihad for checking pending authorization , mantis 10019
            HashMap paramMap = new HashMap();
            paramMap.put("RRID", hash.get("RRID"));
            paramMap.put("CURDATE", CommonUtil.getProperDate(currDate, currDate));
            List result = ClientUtil.executeQuery("RentTrans.AuthorizationPendingChecking", paramMap);
            HashMap countMap = (HashMap) result.get(0);
            if (CommonUtil.convertObjToInt(countMap.get("COUNT")) > 0) {
                ClientUtil.showAlertWindow("Authorization pending for this account !!");
                btnCancelActionPerformed(null);
                return;
            }
            // on 11 dec 2014
            rentTransHeadList = ClientUtil.executeQuery("RentTrans.getAcHeads", hash);// Added by nithya for GST changes     
            rmNumber = hash.get("RMNUMBER").toString();
            txtRoomNo.setText(hash.get("ROOM NUMBER").toString());
            txtRentAmt.setText(hash.get("RENT_AMT").toString());
            lblName.setText(hash.get("NAME").toString());
            txtBuildingNo.setText(hash.get("BUILDING_NO").toString());

            // txtTotal.setText(hash.get("RENT_AMT").toString());
//                 transactionUI.setCallingAmount(txtTotal.getText());
            transactionUI.setCallingApplicantName(lblName.getText());
            txtRentAmt.setEnabled(false);
            txtAcNumber.setText(CommonUtil.convertObjToStr(hash.get("RRID")));
        }
        if (viewType.equals("RENTHISTORY")) {
            rmNumber = hash.get("RMNUMBER").toString();
            txtRoomNo.setText(hash.get("ROOM NUMBER").toString());
            txtRentAmt.setText(hash.get("RENT_AMT").toString());
            lblName.setText(hash.get("NAME").toString());
            txtBuildingNo.setText(hash.get("BUILDING_NO").toString());

            // txtTotal.setText(hash.get("RENT_AMT").toString());
//                 transactionUI.setCallingAmount(txtTotal.getText());
            transactionUI.setCallingApplicantName(lblName.getText());
            txtRentAmt.setEnabled(false);
            txtAcNumber.setText(CommonUtil.convertObjToStr(hash.get("RRID")));
        }
        //rmNumber="";
        if (viewType != null) {
            if (/*
                     * viewType.equals("BUILDING_NUM") ||
                     * viewType.equals("ROOMNUM") ||
                     */viewType.equals(ClientConstants.ACTION_STATUS[2])
                    || viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                HashMap where = new HashMap();
                /*
                 * if(viewType.equals("BUILDING_NUM")) { //
                 * data.put("BORROWING_DATA", "BORROWING_DATA");
                 * where.put("BUILDING_NUM", hash.get("BUILDING_NUM")); } else
                 * if(viewType.equals("ROOMNUM")) { //
                 * data.put("BORROWING_DATA", "BORROWING_DATA");
                 * where.put("ROOMNUM", hash.get("ROOMNUM")); } else
                {
                 */
                where.put("RTID", hash.get("RTID"));
                // }


                hash.put(CommonConstants.MAP_WHERE, where);
                observable.populateData(hash);


                //   fillTxtNoOfTokens();
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
                    ClientUtil.enableDisable(panRentTrans, false);
                } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    ClientUtil.enableDisable(panRentTrans, true);
                }
                setButtonEnableDisable();
                if (viewType.equals(AUTHORIZE)) {
                    ClientUtil.enableDisable(panRentTrans, false);
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                    btnAuthorize.setEnabled(true);
                    btnAuthorize.requestFocusInWindow();
                    btnAuthorize.setFocusable(true);
                }
                //  setButtonEnableDisable();
            }
        }
        txtBuildingNo.setEnabled(false);
        txtRoomNo.setEnabled(false);
        txtTransDate.setEnabled(false);
        tdtRentDate.setEnabled(false);
        txtTotal.setEnabled(false);
        tdtRentCDate.setEnabled(false);
        // tdtRentPto.setEnabled(false);
        tdtRentPFrm.setEnabled(false);
        txtPenelAmt.setEnabled(false);
        transactionUI.setCallingAmount(txtTotal.getText());
        if (rejectFlag == 1) {
            btnReject.setEnabled(false);
        }
        setModified(true);

        if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
            btnAuthorize.setEnabled(true);
            btnAuthorize.requestFocusInWindow();
            btnAuthorize.setFocusable(true);
        }
    }
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
//        savePerformed();
        setModified(false);
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            int transactionSize = 0;
            if (transactionUI.getOutputTO().size() == 0 && CommonUtil.convertObjToDouble(txtRentAmt.getText()).doubleValue() > 0) {
                if (!cbDefaulter.isSelected()) {
                    ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
                    return;
                }
            } else {
                if (CommonUtil.convertObjToDouble(txtRentAmt.getText()).doubleValue() > 0) {
                    amtBorrow = CommonUtil.convertObjToDouble(txtRentAmt.getText()).doubleValue();
                    transactionSize = (transactionUI.getOutputTO()).size();
                    if (transactionSize != 1 && CommonUtil.convertObjToDouble(txtRentAmt.getText()).doubleValue() > 0) {
                        ClientUtil.showAlertWindow("Multiple Transactions are Not allowed, Make it one Transaction");
                        return;
                    } else {
                        observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                    }
                } else if (transactionUI.getOutputTO().size() > 0) {
                    observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                }
            }
            if (transactionSize == 0 && CommonUtil.convertObjToDouble(txtRentAmt.getText()).doubleValue() > 0) {
                if (!cbDefaulter.isSelected()) {
                    ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
                    return;
                }
            } else if (transactionSize != 0) {
                if (!transactionUI.isBtnSaveTransactionDetailsFlag()) {
                    ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.SAVE_TX_DETAILS));
                    return;
                }
                if (transactionUI.getOutputTO().size() > 0) {
                    observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                }
            }

        }
        savePerformed();
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        tdtClosedDate.setEnabled(false);
        txtBuildingNo.setEnabled(false);
        txtRoomNo.setEnabled(false);
        txtTransDate.setEnabled(false);
        tdtRentDate.setEnabled(false);
        txtTotal.setEnabled(false);
        tdtRentCDate.setEnabled(false);
        // tdtRentPto.setEnabled(false);
        tdtRentPFrm.setEnabled(false);
        txtPenelAmt.setEnabled(false);

    }//GEN-LAST:event_btnSaveActionPerformed
    private void savePerformed() {

        String action = "";
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            action = CommonConstants.TOSTATUS_INSERT;
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            action = CommonConstants.TOSTATUS_UPDATE;
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
            action = CommonConstants.TOSTATUS_DELETE;
        }
        saveAction(action);
    }

    public boolean checkNumber(String value) {
        //String amtRentIn=amountRentText.getText();
        boolean incorrect = true;
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
        // return 
    }

    /* Calls the execute method of TerminalOB to do insertion or updation or deletion */
    private void saveAction(String status) {
        //To check mandtoryness of the Terminal panAcHdDetails,panAcHeadDetails panel and diplay appropriate

        final String mandatoryMessage = checkMandatory(panRentTrans);
        StringBuffer message = new StringBuffer(mandatoryMessage);

        if (txtBuildingNo.getText().equals("")) {
            message.append(objMandatoryRB.getString("cboBuildingNo"));
        }
        if (txtRoomNo.getText().equals("")) {
            message.append(objMandatoryRB.getString("cboRoomNo"));
        }
        //     if(tdtRentDate.getDateValue().equals("")) {
        ///     message.append(objMandatoryRB.getString("tdtRentDate"));
        //   }
       /*
         * if(txtTransDate.getText().equals("")) {
         * message.append(objMandatoryRB.getString("tdtTransDate")); }
         * if(tdtRentPFrm.getDateValue().equals("")) {
         * message.append(objMandatoryRB.getString("tdtRentPFrm")); }
         * if(tdtRentPto.getDateValue().equals("")) {
         * message.append(objMandatoryRB.getString("tdtRentPto")); }
         * if(txtDueAmt.getText().equals("")) {
         * message.append(objMandatoryRB.getString("txtDueAmt"));
        }
         */
        if (txtRentAmt.getText().equals("")) {
            message.append(objMandatoryRB.getString("txtRentAmt"));
        }
        //    if(txtPenelAmt.getText().equals("")) {
        //     message.append(objMandatoryRB.getString("txtPenelAmt"));
        ///   }
        //     if(txtNoticeAmt.getText().equals("")) {
        //       message.append(objMandatoryRB.getString("txtNoticeAmt"));
        //   }
        //      if(txtLegalAmt.getText().equals("")) {
        //        message.append(objMandatoryRB.getString("txtLegalAmt"));
        //    }
        //      if(txtArbAmt.getText().equals("")) {
        //        message.append(objMandatoryRB.getString("txtArbAmt"));
        //     }
        //      if(txtCourtAmt.getText().equals("")) {
        //        message.append(objMandatoryRB.getString("txtCourtAmt"));
        //    }
        //    if(txtExeAmt.getText().equals("")) {
        //       message.append(objMandatoryRB.getString("txtExeAmt"));
        //    }
        //setExpDateOnCalculation();
        if (message.length() > 0) {
            displayAlert(message.toString());
        } else {
            updateOBFields();

            observable.execute(status);
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                HashMap lockMap = new HashMap();
                ArrayList lst = new ArrayList();
                lst.add("RTID");
                lockMap.put(ClientConstants.RECORD_KEY_COL, lst);

                if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().containsKey("INSUFFICIENT_BALANCE")) {
                    ClientUtil.showAlertWindow("Available Balance is Insufficient to process your Transaction");
                    return;
                }
                if (observable.getProxyReturnMap() != null) {
                    if (observable.getProxyReturnMap().containsKey("CONFIG_ID")) {
                        lockMap.put("CONFIG_ID", observable.getProxyReturnMap().get("CONFIG_ID"));
                    }
                    HashMap resultMap = (HashMap) observable.getProxyReturnMap();
                    List transferList = (List) resultMap.get("TRANSFER_TRANS_LIST");
                    List cashList = (List) resultMap.get("CASH_TRANS_LIST");
                    if ((transferList != null && transferList.size() > 0) || (cashList != null && cashList.size() > 0)) {
                        displayTransDetail(observable.getProxyReturnMap());
                    } else {
                        ClientUtil.showMessageWindow("Account closed..payment is pending");
                    }
                }
                if (status == CommonConstants.TOSTATUS_UPDATE) {
                    lockMap.put("RTID", observable.getRtId());
                }
                HashMap editLockMap = new HashMap();
                editLockMap.put("SCREEN_ID", getScreenID());
                editLockMap.put("RECORD_KEY", txtRoomNo.getText());
                editLockMap.put("LOCKED_BY", TrueTransactMain.USER_ID);
                editLockMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
                editLockMap.put("CUR_DATE", currDate.clone());
                editLockMap.put("RECORD_KEY", "null");
                setEditLockMap(editLockMap);
                //ClientUtil.executeQuery("deleteEditLock", editLockMap);
                removeEditLock(txtRoomNo.getText());
                settings();
            }
        }
    }

    private void displayTransDetail(HashMap proxyResultMap) {
        try {
            String cashDisplayStr = "Cash Transaction Details...\n";
            String transferDisplayStr = "Transfer Transaction Details...\n";
            String displayStr = "";
            String transId = "";
            String transType = "";
            Object keys[] = proxyResultMap.keySet().toArray();
            int cashCount = 0;
            int transferCount = 0;
            List tempList = null;
            HashMap transMap = null;
            HashMap transIdMap = new HashMap();
            HashMap transTypeMap = new HashMap();
            String actNum = "";
            for (int i = 0; i < keys.length; i++) {
                if (proxyResultMap.get(keys[i]) instanceof String) {
                    continue;
                }
                tempList = (List) proxyResultMap.get(keys[i]);
                if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH") != -1) {
                    for (int j = 0; j < tempList.size(); j++) {
                        transMap = (HashMap) tempList.get(j);
                        if (j == 0) {
                            transId = (String) transMap.get("SINGLE_TRANS_ID");
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
                        transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "CASH");
                        transTypeMap.put(transMap.get("SINGLE_TRANS_ID"), transMap.get("TRANS_TYPE"));
                    }
                    cashCount++;
                } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER") != -1) {
                    for (int j = 0; j < tempList.size(); j++) {
                        transMap = (HashMap) tempList.get(j);
                        if (j == 0) {
                            transId = (String) transMap.get("SINGLE_TRANS_ID");
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
                        transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "TRANSFER");
                    }
                    transferCount++;
                }
            }
            if (cashCount > 0) {
                displayStr += cashDisplayStr;
            }
            if (transferCount > 0) {
                displayStr += transferDisplayStr;
            }
            ClientUtil.showMessageWindow("" + displayStr);

            int yesNo = 0;
            String[] options = {"Yes", "No"};
            yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
            System.out.println("#$#$$ yesNo : " + yesNo);
            if (yesNo == 0) {
                TTIntegration ttIntgration = null;
                HashMap paramMap = new HashMap();
                //paramMap.put("TransId", transId);
                paramMap.put("TransDt", observable.getCurrDt());
                paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                Object keys1[] = transIdMap.keySet().toArray();
                for (int i = 0; i < keys.length; i++) {
                    paramMap.put("TransId", keys1[i]);
                    ttIntgration.setParam(paramMap);
                    if (CommonUtil.convertObjToStr(transIdMap.get(keys1[i])).equals("TRANSFER")) {
                        ttIntgration.integrationForPrint("ReceiptPayment");
                    } else if (CommonUtil.convertObjToStr(transTypeMap.get(keys1[i])).equals("DEBIT")) {
                        ttIntgration.integrationForPrint("CashPayment", false);
                    } else {
                        ttIntgration.integrationForPrint("CashReceipt", false);
                    }
                }
                //            if (((String)TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase().lastIndexOf("POLPULLY")!=-1) {
                //                ttIntgration.integrationForPrint("ReceiptPayment");
                //            } else {
                //            }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    /** Method used to check whether the Mandatory Fields in the Form are Filled or not */
    private String checkMandatory(javax.swing.JComponent component) {
        //  return new MandatoryCheck().checkMandatory(getClass().getName(), component, getMandatoryHashMap());
        return "";
        //validation error
    }

    /** Method used to Give a Alert when any Mandatory Field is not filled by the user */
    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    /* set the screen after the updation,insertion, deletion */

    private void settings() {
        //   observable.resetForm();
        //  txtNoOfTokens.setText("");
        ClientUtil.clearAll(this);
        //  ClientUtil.enableDisable(panSale, false);
        setButtonEnableDisable();
        //   observable.setResultStatus();
        //    txtSalesmanID.setEnabled(false);
        tdtClosedDate.setEnabled(false);
        txtBuildingNo.setEnabled(false);
        txtRoomNo.setEnabled(false);
        txtTransDate.setEnabled(false);
        tdtRentDate.setEnabled(false);
        txtTotal.setEnabled(false);
        tdtRentCDate.setEnabled(false);
        // tdtRentPto.setEnabled(false);
        tdtRentPFrm.setEnabled(false);
        txtPenelAmt.setEnabled(false);
        tdtRentCDate.setDateValue("");
        txtTransDate.setText("");
        lblServiceTaxval.setText("");
    }

    public Date getDateValue(String date1) {
        java.text.DateFormat formatter;
        Date date = null;
        try {
            // String str_date=date1;
            //  formatter = new SimpleDateFormat("MM/dd/yyyy");
            //  date = (Date)formatter.parse(str_date);  
            //      System.out.println("dateAFETRRR 66666666666=========:"+date); 




            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
            // String s1 = "2008-03-30T15:30:00.000+02:00";
            // date1 = date1.substring(0, date1.indexOf('.'));
            date = new Date(sdf2.format(sdf1.parse(date1)));
        } catch (java.text.ParseException e) {
            System.out.println("Error in getDateValue():" + e);
        }
        return date;
    }

    public void updateOBFields() {
        if (txtRoomNo.getText() != null && !txtRoomNo.getText().equals("")) {
            observable.setCboRoomNo(txtRoomNo.getText());
        }
        if (txtBuildingNo.getText() != null && !txtBuildingNo.getText().equals("")) {
            observable.setCboBuildingNo(txtBuildingNo.getText());
        }
        if (cbClosure.isSelected()) {
            observable.setCbClosure("YES");
            observable.setAccStatus("CLOSED");
        } else {
            observable.setCbClosure("NO");
        }
        if (cbDefaulter.isSelected()) {
            observable.setCbDefaulter("YES");
            observable.setAccStatus("DEFAULTER");
        } else {
            observable.setCbDefaulter("NO");
        }
        if (txtDueAmt.getText() == null || txtDueAmt.getText().trim().equals("")) {
            observable.setTxtDueAmt(Double.valueOf(0.0));
        } else {
            observable.setTxtDueAmt(Double.valueOf(txtDueAmt.getText()));
        }
        if (txtRentAmt.getText() != null && !txtRentAmt.getText().equals("")) {
            observable.setTxtRentAmt(Double.valueOf(txtRentAmt.getText()));
        }
        if (txtPenelAmt.getText() != null && !txtPenelAmt.getText().equals("")) {
            observable.setTxtPenelAmt(Double.valueOf(txtPenelAmt.getText()));
        } else {
            observable.setTxtPenelAmt(Double.valueOf(0.0));
        }
        if (txtNoticeAmt.getText() != null && !txtNoticeAmt.getText().equals("")) {
            observable.setTxtNoticeAmt(Double.valueOf(txtNoticeAmt.getText()));
        } else {
            observable.setTxtNoticeAmt(Double.valueOf(0.0));
        }
        if (txtLegalAmt.getText() != null && !txtLegalAmt.getText().equals("")) {
            observable.setTxtLegalAmt(Double.valueOf(txtLegalAmt.getText()));
        } else {
            observable.setTxtLegalAmt(Double.valueOf(0.0));
        }
        if (txtArbAmt.getText() != null && !txtArbAmt.getText().equals("")) {
            observable.setTxtArbAmt(Double.valueOf(txtArbAmt.getText()));
        } else {
            observable.setTxtArbAmt(Double.valueOf(0.0));
        }
        if (txtCourtAmt.getText() != null && !txtCourtAmt.getText().equals("")) {
            observable.setTxtCourtAmt(Double.valueOf(txtCourtAmt.getText()));
        } else {
            observable.setTxtCourtAmt(Double.valueOf(0.0));
        }
        if (txtExeAmt.getText() != null && !txtExeAmt.getText().equals("")) {
            observable.setTxtExeAmt(Double.valueOf(txtExeAmt.getText()));
        } else {
            observable.setTxtExeAmt(Double.valueOf(0.0));
        }
        if (txtTotal.getText() != null && !txtTotal.getText().equals("")) {
            observable.setTxtRentTotal(Double.valueOf(txtTotal.getText()));
        } else {
            observable.setTxtRentTotal(Double.valueOf(0.0));
        }
        if (tdtRentDate.getDateValue() != null && !tdtRentDate.getDateValue().equals("")) {
            observable.setTdtRentDate(DateUtil.getDateMMDDYYYY(tdtRentDate.getDateValue()));
        }
        if (tdtRentCDate.getDateValue() != null && !tdtRentCDate.getDateValue().equals("")) {
            observable.setTdtRentCDate(DateUtil.getDateMMDDYYYY(tdtRentCDate.getDateValue()));
        }
        // displayAlert("TRANS DATE=="+txtTransDate.getText());
        if (txtTransDate.getText() != null && !txtTransDate.getText().equals("")) {
            observable.setTxtTransDate(DateUtil.getDateMMDDYYYY(txtTransDate.getText()));
        }
        if (tdtRentPFrm.getDateValue() != null && !tdtRentPFrm.getDateValue().equals("")) {
            observable.setTdtRentPFrm(DateUtil.getDateMMDDYYYY(tdtRentPFrm.getDateValue()));
        }
        if (tdtRentPto.getDateValue() != null && !tdtRentPto.getDateValue().equals("")) {
            observable.setTdtRentPto(DateUtil.getDateMMDDYYYY(tdtRentPto.getDateValue()));
        }
        if (cbClosure.isSelected() || cbDefaulter.isSelected()) {
            observable.setTxtRoomStatus("VACANT");
            observable.setTdtClosedDate(DateUtil.getDateMMDDYYYY(tdtClosedDate.getDateValue()));
        } else {
            observable.setTxtRoomStatus("REGISTERED"); // KD-3479
            observable.setTdtClosedDate(null);
        }
        if (txtAcNumber.getText() != null && !txtAcNumber.getText().equals("")) {
            observable.setTxtAcNumber(CommonUtil.convertObjToStr(txtAcNumber.getText()));
        }
        observable.setTxtRmNumber(rmNumber);
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setServiceTax_Map(serviceTax_Map);
        //  txtSalesmanID.setEnabled(false);
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView(ClientConstants.ACTION_STATUS[3]);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_DELETE);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        txtBuildingNo.setEnabled(false);
        txtRoomNo.setEnabled(false);
        txtTransDate.setEnabled(false);
        tdtRentDate.setEnabled(false);
        txtTotal.setEnabled(false);
        tdtRentCDate.setEnabled(false);
        tdtRentPto.setEnabled(false);
        tdtRentPFrm.setEnabled(false);
        btnBuildingNo.setEnabled(false);
        btnRoomNo.setEnabled(false);
        txtPenelAmt.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView(ClientConstants.ACTION_STATUS[2]);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_VIEW);
        btnCancel.setEnabled(true);
        ///////////////////////////////////*************************
        ClientUtil.enableDisable(panRentTrans, false);
        btnSave.setEnabled(false);
//         transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        //transactionUI.resetObjects();
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
//        btnAuthorize.setEnabled(false);
//        btnReject.setEnabled(false);
//        btnException.setEnabled(false);
        txtBuildingNo.setEnabled(false);
        txtRoomNo.setEnabled(false);
        txtTransDate.setEnabled(false);
        tdtRentDate.setEnabled(false);
        txtTotal.setEnabled(false);
        tdtRentCDate.setEnabled(false);
        //  tdtRentPto.setEnabled(false);
        tdtRentPFrm.setEnabled(false);
        btnBuildingNo.setEnabled(true);
        btnRoomNo.setEnabled(true);
        txtPenelAmt.setEnabled(false);
        btnNew.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        setModified(true);
        observable.resetForm();
        //txtNoOfTokens.setText("");
        ClientUtil.enableDisable(panRentTrans, true); //Information always disable
        //    cbo1.removeAllElements();
        // System.out.println("btnNewActionPerformed ACTION STA: "+ClientConstants.ACTIONTYPE_NEW);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        //////////////////////////////////////**************************
//        btnAuthorize.setEnabled(false);
//        btnReject.setEnabled(false);
//        btnException.setEnabled(false);
//        btnSave.setEnabled(true);
        tdtClosedDate.setEnabled(false);
        txtBuildingNo.setEnabled(false);
        txtRoomNo.setEnabled(false);
        tdtRentDate.setEnabled(false);
        txtTransDate.setEnabled(false);
        txtTotal.setEnabled(false);
        tdtRentCDate.setEnabled(false);
        // tdtRentPto.setEnabled(false);
        tdtRentPFrm.setEnabled(false);
        btnBuildingNo.setEnabled(true);
        btnRoomNo.setEnabled(true);
        txtPenelAmt.setEnabled(false);
        txtAcNumber.setEnabled(false);
        // btnCalPen.setEnabled(true);
    }//GEN-LAST:event_btnNewActionPerformed
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        btnSave.setEnabled(!btnSave.isEnabled());
//        btnCancel.setEnabled(btnCancel.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        //  lblStatus.setText(observable.getLblStatus());
        btnView.setEnabled(!btnView.isEnabled());
        //  txtSalesmanID.setEnabled(false);
        txtBuildingNo.setEnabled(false);
        txtRoomNo.setEnabled(false);
        txtTotal.setEnabled(false);
        txtTransDate.setText(appLoginDate);
        txtTransDate.setEnabled(false);
        tdtRentDate.setEnabled(false);
        txtTotal.setEnabled(false);
        tdtRentCDate.setEnabled(false);
        //  tdtRentPto.setEnabled(false);
        //tdtRentCDate.setDateValue(cm.getCurrentDate());
        tdtRentCDate.setDateValue(appLoginDate);
        tdtRentPFrm.setEnabled(false);
        txtDueAmt.setEnabled(false);
        btnBuildingNo.setEnabled(false);
        btnRoomNo.setEnabled(false);
        txtPenelAmt.setEnabled(false);
    }
    private void mnuProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuProcessActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mnuProcessActionPerformed

    private void btnCalPenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalPenActionPerformed
        // TODO add your handling code here:
        txtDueAmt.setText("");
        txtRentAmt.setText("");
        txtPenelAmt.setText("");
        txtTotal.setText("");
        Date nwRntPrdTo = null;
        Date nwRntPrdFrm = null;
        //String rentPrdFrm = tdtRentPFrm.getDateValue();
        //String rentPrdTo = tdtRentPto.getDateValue();
        //SimpleDateFormat parser1 = new SimpleDateFormat("dd/MM/yyyy");
        try {
            nwRntPrdTo = DateUtil.getDateMMDDYYYY(tdtRentPto.getDateValue());
            nwRntPrdFrm = DateUtil.getDateMMDDYYYY(tdtRentPFrm.getDateValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Double Inst = MonthDiff(nwRntPrdTo, nwRntPrdFrm);
        String strNoOfInst = df.format(Inst);
        noOfInstInFcsLst = Double.parseDouble(strNoOfInst);
        //        noOfInstInFcsLst=noOfInstInFcsLst-1;
        Rounding rod = new Rounding();
        intnoOfInstInFcsLst = (int) rod.getNearest((long) (noOfInstInFcsLst * 100), 100) / 100;
        dedDue = intnoOfInstInFcsLst * rentAmount;
        if (noOfInst != noOfInstInFcsLst) {
            calcDed(noOfInstInFcsLst);
        }
        noOfInst = noOfInstInFcsLst;
        setPenalDue();
        transactionUI.setCallingAmount(txtTotal.getText());
        Double penInst = Double.parseDouble(strNoOfInst);
        penInst = Math.floor(penInst);
        //penInst=penInst-1;
        int penIn = CommonUtil.convertObjToInt(penInst);

        if (nwRntPrdFrm.compareTo((Date) currDate.clone()) > 0) {
            cLabel1.setText("0");
        } else {
            cLabel1.setText("" + (k - 1));
        }
        btnCalPen.setEnabled(false);
    }//GEN-LAST:event_btnCalPenActionPerformed

    private void btnBuildingNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuildingNoActionPerformed
        // TODO add your handling code here:
        callView("BUILDING_NUM");
    }//GEN-LAST:event_btnBuildingNoActionPerformed

private void btnDefaulterNClosedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDefaulterNClosedActionPerformed
    observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
    callView("RENTHISTORY");
    setRentDate();
    tdtRentCDate.setDateValue(appLoginDate);
    txtTransDate.setText(appLoginDate);
}//GEN-LAST:event_btnDefaulterNClosedActionPerformed

private void cbClosureMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbClosureMouseClicked
// TODO add your handling code here:
    if (cbClosure.isSelected()) {
        tdtClosedDate.setEnabled(true);
        tdtClosedDate.setDateValue(ClientUtil.getCurrentDateinDDMMYYYY());
    } else {
        tdtClosedDate.setEnabled(false);
        tdtClosedDate.setDateValue(null);
    }
    if (cbClosure.isSelected()) {
        cbClosure.setSelected(true);
        cbDefaulter.setSelected(false);
    }
}//GEN-LAST:event_cbClosureMouseClicked

private void cbDefaulterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbDefaulterMouseClicked
// TODO add your handling code here:
    if (cbDefaulter.isSelected()) {
        cbDefaulter.setSelected(true);
        cbClosure.setSelected(false);
        tdtClosedDate.setEnabled(true);
        tdtClosedDate.setDateValue(ClientUtil.getCurrentDateinDDMMYYYY());
    } else {
        tdtClosedDate.setEnabled(false);
        tdtClosedDate.setDateValue(null);
    }
}//GEN-LAST:event_cbDefaulterMouseClicked

    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();

        mandatoryMap.put("txtBuildingNo", new Boolean(true));
        mandatoryMap.put("txtRoomNo", new Boolean(true));
        //  mandatoryMap.put("tdtRentDate", new Boolean(false));
        //   mandatoryMap.put("tdtTransDate", new Boolean(false));
        //  mandatoryMap.put("tdtRentPFrm", new Boolean(false));
        //   mandatoryMap.put("tdtRentPto", new Boolean(false));
        //   mandatoryMap.put("txtDueAmt", new Boolean(false));
        mandatoryMap.put("txtRentAmt", new Boolean(true));
        //   mandatoryMap.put("txtPenelAmt", new Boolean(false));
        // mandatoryMap.put("txtNoticeAmt", new Boolean(false));
        //   mandatoryMap.put("txtLegalAmt", new Boolean(false));
        //    mandatoryMap.put("txtArbAmt", new Boolean(false));
        //    mandatoryMap.put("txtCourtAmt", new Boolean(false));
        //    mandatoryMap.put("txtExeAmt", new Boolean(false));
        // mandatoryMap.put("cbClosure", new Boolean(false));
        // mandatoryMap.put("tdtClosedDate", new Boolean(false));


    }

    public java.util.HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    public String getDtPrintValue(String strDate) {
        try {
            //     System.out.println("strDate getDtPrintValue99999999999999999999999999999999999999================="+strDate);
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

    public String getDtPrintValue1(String strDate) {
        try {
            //    System.out.println("strDate getDtPrintValue99999999999999999999999999999999999999================="+strDate);
            //create SimpleDateFormat object with source string date format
            SimpleDateFormat sdfSource = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        ArrayList lst = new ArrayList();

        if (viewType.equals(ClientConstants.ACTION_STATUS[2])
                || viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE)
                || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
            lst.add("RTID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            viewMap.put(CommonConstants.MAP_NAME, "RentTrans.getSelectRentTransList");
        } else if (viewType.equals("BUILDING_NUM")) {
            viewMap.put(CommonConstants.MAP_NAME, "RentRegister.getBuildingNo");
            lst.add("BUILDING_NUM");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
        } else if (viewType.equals("ROOMNUM")) {
//             System.out.println("rmNumberm INNNNNNNNNNNNNNNNNNNNNNN=========================="+rmNumber);
//             if(rmNumber!=null)
//             {
            viewMap.put(CommonConstants.MAP_NAME, "RentRegister.getRoomNo");

            lst.add("ROOM NUMBER");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
//             }
        } else if (viewType.equals("RENTHISTORY")) {
            viewMap.put(CommonConstants.MAP_NAME, "RentRegister.getDefaulterNClosedAccounts");
            //lst.add("ROOM NUMBER");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
        }

        HashMap where = new HashMap();
        where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        where.put("BUILDING_NO", rmNumber);
        viewMap.put(CommonConstants.MAP_WHERE, where);
        where = null;
        //  System.out.println("viewMap--inmmmmmmmmmmmmmmmmm-----"+viewMap);
        new ViewAll(this, viewMap).show();

        // new ViewAll(this,
    }
    /* Auto Generated Method - update()
    This method called by Observable. It updates the UI with
    Observable's data. If needed add/Remove RadioButtons
    method need to be added.*/

    public void update(Observable observed, Object arg) {
        txtBuildingNo.setText(observable.getCboBuildingNo());
        txtRoomNo.setText(observable.getCboRoomNo());
        if (observable.getTdtRentDate() != null) {
            tdtRentDate.setDateValue(getDtPrintValue(String.valueOf(observable.getTdtRentDate())));
        }
        if (observable.getTdtRentCDate() != null) {
            tdtRentCDate.setDateValue(getDtPrintValue(String.valueOf(observable.getTdtRentCDate())));
        }
        if (observable.getTxtTransDate() != null) {
            txtTransDate.setText(CommonUtil.convertObjToStr(observable.getTxtTransDate()));
        }
        if (observable.getTdtRentPFrm() != null) {
            tdtRentPFrm.setDateValue(getDtPrintValue(String.valueOf(observable.getTdtRentPFrm())));
        }
        if (observable.getTdtRentPto() != null) {
            tdtRentPto.setDateValue(getDtPrintValue(String.valueOf(observable.getTdtRentPto())));
        }
        if (observable.getTxtExeAmt() != null) {
            txtExeAmt.setText(String.valueOf(observable.getTxtExeAmt()));
        }
        if (observable.getTxtRentTotal() != null) {
            txtTotal.setText(String.valueOf(observable.getTxtRentTotal()));
        }
        if (observable.getTxtCourtAmt() != null) {
            txtCourtAmt.setText(String.valueOf(observable.getTxtCourtAmt()));
        }
        if (observable.getTxtArbAmt() != null) {
            txtArbAmt.setText(String.valueOf(observable.getTxtArbAmt()));
        }
        if (observable.getTxtLegalAmt() != null) {
            txtLegalAmt.setText(String.valueOf(observable.getTxtLegalAmt()));
        }
        if (observable.getTxtNoticeAmt() != null) {
            txtNoticeAmt.setText(String.valueOf(observable.getTxtNoticeAmt()));
        }
        if (observable.getTxtPenelAmt() != null) {
            txtPenelAmt.setText(String.valueOf(observable.getTxtPenelAmt()));
        }
        if (observable.getTxtRentAmt() != null) {
            txtRentAmt.setText(String.valueOf(observable.getTxtRentAmt()));
        }
        if (observable.getTxtDueAmt() != null) {
            txtDueAmt.setText(String.valueOf(observable.getTxtDueAmt()));
        }

        if (observable.getTdtClosedDate() != null) {
            tdtClosedDate.setDateValue(getDtPrintValue(String.valueOf(observable.getTdtClosedDate())));
        }
        if (cbClosure.equals("YES")) {
            cbClosure.setSelected(true);
        } else {
            cbClosure.setSelected(false);
        }

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnBuildingNo;
    private com.see.truetransact.uicomponent.CButton btnCalPen;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDefaulterNClosed;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnRoomNo;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CCheckBox cbClosure;
    private com.see.truetransact.uicomponent.CCheckBox cbDefaulter;
    private javax.swing.JLabel jLabel1;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lbSpace3;
    private com.see.truetransact.uicomponent.CLabel lblAcNumber;
    private com.see.truetransact.uicomponent.CLabel lblArbAmt;
    private com.see.truetransact.uicomponent.CLabel lblBuildingNo;
    private com.see.truetransact.uicomponent.CLabel lblClosedDate;
    private com.see.truetransact.uicomponent.CLabel lblCourtAmt;
    private com.see.truetransact.uicomponent.CLabel lblDueAmt;
    private com.see.truetransact.uicomponent.CLabel lblExeAmt;
    private com.see.truetransact.uicomponent.CLabel lblInstPend;
    private com.see.truetransact.uicomponent.CLabel lblLegalAmt;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblName;
    private com.see.truetransact.uicomponent.CLabel lblNoticeAmt;
    private com.see.truetransact.uicomponent.CLabel lblPenalAmt;
    private com.see.truetransact.uicomponent.CLabel lblRentAmt;
    private com.see.truetransact.uicomponent.CLabel lblRentCDate;
    private com.see.truetransact.uicomponent.CLabel lblRentDate;
    private com.see.truetransact.uicomponent.CLabel lblRentPdFrom;
    private com.see.truetransact.uicomponent.CLabel lblRentPdTo;
    private com.see.truetransact.uicomponent.CLabel lblRoomNo;
    private javax.swing.JLabel lblServiceTaxval;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace51;
    private com.see.truetransact.uicomponent.CLabel lblSpace52;
    private com.see.truetransact.uicomponent.CLabel lblSpace53;
    private com.see.truetransact.uicomponent.CLabel lblSpace54;
    private com.see.truetransact.uicomponent.CLabel lblSpace55;
    private com.see.truetransact.uicomponent.CLabel lblSpace56;
    private com.see.truetransact.uicomponent.CLabel lblSpace57;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTotal;
    private com.see.truetransact.uicomponent.CLabel lblTransDate;
    private com.see.truetransact.uicomponent.CMenuBar mbrTokenConfig;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panRentTrans;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTrans;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CToolBar tbrSale;
    private com.see.truetransact.uicomponent.CDateField tdtClosedDate;
    private com.see.truetransact.uicomponent.CDateField tdtRentCDate;
    private com.see.truetransact.uicomponent.CDateField tdtRentDate;
    private com.see.truetransact.uicomponent.CDateField tdtRentPFrm;
    private com.see.truetransact.uicomponent.CDateField tdtRentPto;
    private com.see.truetransact.uicomponent.CTextField txtAcNumber;
    private com.see.truetransact.uicomponent.CTextField txtArbAmt;
    private com.see.truetransact.uicomponent.CTextField txtBuildingNo;
    private com.see.truetransact.uicomponent.CTextField txtCourtAmt;
    private com.see.truetransact.uicomponent.CTextField txtDueAmt;
    private com.see.truetransact.uicomponent.CTextField txtExeAmt;
    private com.see.truetransact.uicomponent.CTextField txtLegalAmt;
    private com.see.truetransact.uicomponent.CTextField txtNoticeAmt;
    private com.see.truetransact.uicomponent.CTextField txtPenelAmt;
    private com.see.truetransact.uicomponent.CTextField txtRentAmt;
    private com.see.truetransact.uicomponent.CTextField txtRoomNo;
    private com.see.truetransact.uicomponent.CTextField txtTotal;
    private com.see.truetransact.uicomponent.CTextField txtTotal2;
    private com.see.truetransact.uicomponent.CTextField txtTotal3;
    private com.see.truetransact.uicomponent.CTextField txtTotal4;
    private com.see.truetransact.uicomponent.CTextField txtTransDate;
    // End of variables declaration//GEN-END:variables
//     private com.see.truetransact.uicomponent.CTable tblData;
    private com.see.truetransact.clientutil.TableModel tbModel;
}
