/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AccountClosingUI.java
 *
 * Created on August 6, 2003, 10:53 AM
 */
package com.see.truetransact.ui.termloan.InterestReport;

import java.util.HashMap;
import java.util.Observable;
import org.apache.log4j.Logger;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.ToDateValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.ui.transaction.common.TransDetailsUI;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.transferobject.product.operativeacct.OperativeAcctProductTO;
import com.see.truetransact.ui.product.operativeacct.OperativeAcctProductOB;
import com.see.truetransact.ui.operativeaccount.AccountClosingOB;
import com.see.truetransact.transferobject.operativeaccount.AccountClosingTO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.termloan.InterestReport.InterestReportOB;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;

import java.util.*;
import javax.swing.ScrollPaneConstants;

/**
 * This form is used to manipulate AccountClosing related functionality Author
 * Nikhil
 */
public class InterestReportUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField {

    final int AUTHORIZE = 3, CANCEL = 0;
    int viewType = -1;
    private HashMap mandatoryMap;
    private InterestReportOB observable;
    TTIntegration ttIntegration = null;
    private final static Logger log = Logger.getLogger(InterestReportUI.class);
    private TransactionUI transactionUI = new TransactionUI();
    private TransDetailsUI transDetailsUI = null;
    //    AccountClosingRB accountClosingRB = new AccountClosingRB();
//    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan. ", ProxyParameters.LANGUAGE);
    private String prodType = "";
    private boolean depositFlag;
    private LinkedHashMap transactionDetailsTO = null;
    private int ACT_NUM = 200, EDIT = 1, TXT_NO = 100, ACT_NUM_TO = 300, TXT_NO_TO = 400;
    boolean generateNotice = false;
    private boolean btnMainNewPressed = false;
    private boolean btnSubNewPressed = false;
    private boolean mainDetailEnable = false;
    private boolean btnEditPressed = false;
    boolean isFilled = false;
    private String actNumStatus = "";
    private Date currDt = null;
    private String closedAcct="";

    public InterestReportUI(String prodType) {
        this.prodType = prodType;
        System.out.println("termloan#####" + prodType);
        initComponents();
        setFieldNames();
        setMaxlength();
    }

    /**
     * Creates new form AccountClosingUI
     */
    public InterestReportUI() {
        initComponents();
        setFieldNames();
        internationalize();
        observable = new InterestReportOB();
        initComponentData();
        setMandatoryHashMap();
        observable.resetForm();
        menuEnableDisable();
        ClientUtil.enableDisable(this, false);
        buttonEnableDisable(false);
        btnSave.setVisible(false);
        txtAccountNumber.setAllowAll(true);
        txtAccountNumberTo.setAllowAll(true);
        txtAccountNumber.setMaxLength(16);
        txtAccountNumberTo.setMaxLength(16);
        setMaxlength();
        currDt = ClientUtil.getCurrentDate();
        ClientUtil.enableDisable(panButtonCharges1, false);
        enableDisableDate(false);
        tdtPassDate.setVisible(false);
        chkCurrDate.setVisible(false);
        lblCalcIntUpto.setVisible(false);
    }

    public Date getProperDateFormat(Object obj) {
        Date passDt = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            passDt = (Date) currDt.clone();
            passDt.setDate(tempDt.getDate());
            passDt.setMonth(tempDt.getMonth());
            passDt.setYear(tempDt.getYear());
        }
        return passDt;
    }

    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboProductType", new Boolean(true));
        mandatoryMap.put("cboProductID", new Boolean(true));
        mandatoryMap.put("txtAccountNumber", new Boolean(true));
        mandatoryMap.put("cboChargesType", new Boolean(true));
        mandatoryMap.put("tdtChargesDate", new Boolean(true));
        mandatoryMap.put("txtChargesAmount", new Boolean(true));

    }

    public HashMap getMandatoryHashMap() {
        return null;
    }

    public void update(Observable o, Object arg) {
        cboProductType.setSelectedItem(((ComboBoxModel) cboProductType.getModel()).getDataForKey(observable.getProdType()));
        cboProductID.setSelectedItem(
                ((ComboBoxModel) cboProductID.getModel()).getDataForKey(observable.getCboProductID()));
    }

    private void initComponentData() {
        cboProductType.setModel(observable.getCbmProdType());
        cboProductID.setModel(observable.getCbmProductID());
        System.out.println((observable.getCbmProdType().getKeys()));
    }
    /*
     * Auto Generated Method - setFieldNames() This method assigns name for all
     * the components. Other functions are working based on this name.
     */

    private void setFieldNames() {
        btnAccountNumber.setName("btnAccountNumber");
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnChargeSave.setName("btnChargeSave");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        btnView.setName("btnView");
        cboProductID.setName("cboProductID");
        cboProductType.setName("cboProductType");
//        lblAccountHead.setName("lblAccountHead");
//        lblAccountHeadDisplay.setName("lblAccountHeadDisplay");
        lblClosedAccounts.setName("lblAccountNumber");
        lblCustomerNameDisplay.setName("lblCustomerNameDisplay");
        lblMsg.setName("lblMsg");
        lblProductID.setName("lblProductID");
        lblProductType.setName("lblProductType");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace5.setName("lblSpace5");
        lblStatus.setName("lblStatus");
        mbrCustomer.setName("mbrCustomer");
        panAccountInfo.setName("panAccountInfo");
        panAcctNum.setName("panAcctNum");
        panProductID.setName("panProductID");
        panStatus.setName("panStatus");
        panButtonCharges.setName("panButtonCharges");
        txtAccountNumber.setName("txtAccountNumber");
    }

    private void setMaxlength() {
    }

    private void internationalize() {
//        btnAccountNumber.setText(resourceBundle.getString("btnAccountNumber"));
    }

    private void menuEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        btnSave.setEnabled(!btnSave.isEnabled());
        btnCancel.setEnabled(!btnCancel.isEnabled());
        btnAuthorize.setEnabled(!btnAuthorize.isEnabled());
        btnReject.setEnabled(!btnReject.isEnabled());
    }

    private void buttonEnableDisable(boolean flag) {
        btnAccountNumber.setEnabled(flag);
        btnChargeSave.setEnabled(flag);
    }

    public void fillData(Object obj) {
        isFilled = true;
        HashMap map = (HashMap) obj;
        System.out.println("filldata###" + map);
        if (viewType == ACT_NUM || viewType == EDIT || viewType == AUTHORIZE || viewType == TXT_NO || viewType == ACT_NUM_TO || viewType == TXT_NO_TO) {
            //         observable.populateData(map);
            if (viewType == ACT_NUM || viewType == TXT_NO) {
                txtAccountNumber.setText(CommonUtil.convertObjToStr(map.get("ACCOUNTNO")));
                observable.setTxtAccountNumber(CommonUtil.convertObjToStr(map.get("ACCOUNTNO")));
                lblCustomerNameDisplay.setText(CommonUtil.convertObjToStr(map.get("CUSTOMERNAME")));
            } else if (viewType == ACT_NUM_TO || viewType == TXT_NO_TO) {
                txtAccountNumberTo.setText(CommonUtil.convertObjToStr(map.get("ACCOUNTNO")));
                observable.setTxtAccountNumberTo(CommonUtil.convertObjToStr(map.get("ACCOUNTNO")));
                lblCustomerNameDisplayTo.setText(CommonUtil.convertObjToStr(map.get("CUSTOMERNAME")));
            }
        }
    }

    public HashMap asAnWhenCustomerComesYesNO(String acct_no) {
        HashMap map = new HashMap();
        map.put("ACT_NUM", acct_no);
        map.put("TRANS_DT", currDt);		//Added By Suresh R
        map.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
        List lst = ClientUtil.executeQuery("IntCalculationDetail", map);
        if (lst == null || lst.isEmpty()) {
            lst = ClientUtil.executeQuery("IntCalculationDetailAD", map);
        }
        if (lst != null && lst.size() > 0) {
            map = (HashMap) lst.get(0);
            //            setLinkMap(map);
        }
        return map;
    }

    private void setObservable() {
        observable = InterestReportOB.getInstance();
        observable.addObserver(this);
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

        rdoFromDateGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        panAccountInfo = new com.see.truetransact.uicomponent.CPanel();
        panProductID = new com.see.truetransact.uicomponent.CPanel();
        lblProductType = new com.see.truetransact.uicomponent.CLabel();
        cboProductType = new com.see.truetransact.uicomponent.CComboBox();
        lblProductID = new com.see.truetransact.uicomponent.CLabel();
        cboProductID = new com.see.truetransact.uicomponent.CComboBox();
        lblClosedAccounts = new com.see.truetransact.uicomponent.CLabel();
        panAcctNum = new com.see.truetransact.uicomponent.CPanel();
        txtAccountNumber = new com.see.truetransact.uicomponent.CTextField();
        btnAccountNumber = new com.see.truetransact.uicomponent.CButton();
        lblCustomerNameDisplay = new com.see.truetransact.uicomponent.CLabel();
        panButtonCharges = new com.see.truetransact.uicomponent.CPanel();
        btnChargeSave = new com.see.truetransact.uicomponent.CButton();
        panAcctNum1 = new com.see.truetransact.uicomponent.CPanel();
        txtAccountNumberTo = new com.see.truetransact.uicomponent.CTextField();
        btnAccountNumberTo = new com.see.truetransact.uicomponent.CButton();
        lblAccountNumber1 = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerNameDisplayTo = new com.see.truetransact.uicomponent.CLabel();
        panButtonCharges1 = new com.see.truetransact.uicomponent.CPanel();
        lblCalcIntUpto = new com.see.truetransact.uicomponent.CLabel();
        chkCurrDate = new com.see.truetransact.uicomponent.CCheckBox();
        tdtPassDate = new com.see.truetransact.uicomponent.CDateField();
        lblAccountNumber2 = new com.see.truetransact.uicomponent.CLabel();
        tdtFromDate = new com.see.truetransact.uicomponent.CDateField();
        tdtToDate = new com.see.truetransact.uicomponent.CDateField();
        lblToDate = new com.see.truetransact.uicomponent.CLabel();
        lblFromDate = new com.see.truetransact.uicomponent.CLabel();
        lblAccountNumber5 = new com.see.truetransact.uicomponent.CLabel();
        rdoPeriodRange = new com.see.truetransact.uicomponent.CRadioButton();
        rdoAcctOpenDt = new com.see.truetransact.uicomponent.CRadioButton();
        rdoLastIntCalcDt = new com.see.truetransact.uicomponent.CRadioButton();
        tbrOperativeAcctProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace70 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace71 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace72 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace73 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace74 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace75 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
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

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setMinimumSize(new java.awt.Dimension(680, 600));
        setPreferredSize(new java.awt.Dimension(680, 600));

        panAccountInfo.setMinimumSize(new java.awt.Dimension(552, 429));
        panAccountInfo.setPreferredSize(new java.awt.Dimension(552, 429));
        panAccountInfo.setLayout(new java.awt.GridBagLayout());

        panProductID.setMinimumSize(new java.awt.Dimension(500, 250));
        panProductID.setPreferredSize(new java.awt.Dimension(500, 250));
        panProductID.setLayout(new java.awt.GridBagLayout());

        lblProductType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblProductType, gridBagConstraints);

        cboProductType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductType.setPopupWidth(210);
        cboProductType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 2, 6, 6);
        panProductID.add(cboProductType, gridBagConstraints);

        lblProductID.setText("Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panProductID.add(lblProductID, gridBagConstraints);

        cboProductID.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductID.setPopupWidth(210);
        cboProductID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(cboProductID, gridBagConstraints);

        lblClosedAccounts.setText("From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblClosedAccounts, gridBagConstraints);

        panAcctNum.setLayout(new java.awt.GridBagLayout());

        txtAccountNumber.setMaxLength(10);
        txtAccountNumber.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccountNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAccountNumberActionPerformed(evt);
            }
        });
        txtAccountNumber.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccountNumberFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        panAcctNum.add(txtAccountNumber, gridBagConstraints);

        btnAccountNumber.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccountNumber.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnAccountNumber.setMaximumSize(new java.awt.Dimension(21, 21));
        btnAccountNumber.setMinimumSize(new java.awt.Dimension(21, 21));
        btnAccountNumber.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccountNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccountNumberActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panAcctNum.add(btnAccountNumber, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductID.add(panAcctNum, gridBagConstraints);

        lblCustomerNameDisplay.setForeground(new java.awt.Color(0, 51, 204));
        lblCustomerNameDisplay.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblCustomerNameDisplay.setMinimumSize(new java.awt.Dimension(170, 21));
        lblCustomerNameDisplay.setPreferredSize(new java.awt.Dimension(170, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductID.add(lblCustomerNameDisplay, gridBagConstraints);

        panButtonCharges.setMinimumSize(new java.awt.Dimension(315, 35));
        panButtonCharges.setPreferredSize(new java.awt.Dimension(315, 35));
        panButtonCharges.setLayout(new java.awt.GridBagLayout());

        btnChargeSave.setText("Report");
        btnChargeSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChargeSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panButtonCharges.add(btnChargeSave, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 21;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 1, 0);
        panProductID.add(panButtonCharges, gridBagConstraints);

        panAcctNum1.setLayout(new java.awt.GridBagLayout());

        txtAccountNumberTo.setMaxLength(10);
        txtAccountNumberTo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccountNumberTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAccountNumberToActionPerformed(evt);
            }
        });
        txtAccountNumberTo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccountNumberToFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        panAcctNum1.add(txtAccountNumberTo, gridBagConstraints);

        btnAccountNumberTo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccountNumberTo.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnAccountNumberTo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnAccountNumberTo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnAccountNumberTo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccountNumberTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccountNumberToActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panAcctNum1.add(btnAccountNumberTo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panProductID.add(panAcctNum1, gridBagConstraints);

        lblAccountNumber1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAccountNumber1.setText("To Acct No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panProductID.add(lblAccountNumber1, gridBagConstraints);

        lblCustomerNameDisplayTo.setForeground(new java.awt.Color(0, 51, 204));
        lblCustomerNameDisplayTo.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblCustomerNameDisplayTo.setMinimumSize(new java.awt.Dimension(170, 21));
        lblCustomerNameDisplayTo.setPreferredSize(new java.awt.Dimension(170, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panProductID.add(lblCustomerNameDisplayTo, gridBagConstraints);

        panButtonCharges1.setMinimumSize(new java.awt.Dimension(410, 32));
        panButtonCharges1.setPreferredSize(new java.awt.Dimension(410, 32));
        panButtonCharges1.setLayout(new java.awt.GridBagLayout());

        lblCalcIntUpto.setText("Calculate Interest Upto");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 12);
        panButtonCharges1.add(lblCalcIntUpto, gridBagConstraints);

        chkCurrDate.setText("Current Date");
        chkCurrDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCurrDateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panButtonCharges1.add(chkCurrDate, gridBagConstraints);

        tdtPassDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtPassDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panButtonCharges1.add(tdtPassDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.gridwidth = 5;
        panProductID.add(panButtonCharges1, gridBagConstraints);

        lblAccountNumber2.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblAccountNumber2, gridBagConstraints);

        tdtFromDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtFromDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(tdtFromDate, gridBagConstraints);

        tdtToDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtToDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(tdtToDate, gridBagConstraints);

        lblToDate.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblToDate, gridBagConstraints);

        lblFromDate.setText("From Acct No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblFromDate, gridBagConstraints);

        lblAccountNumber5.setText("From Acct No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblAccountNumber5, gridBagConstraints);

        rdoFromDateGroup.add(rdoPeriodRange);
        rdoPeriodRange.setText("PeriodRange");
        rdoPeriodRange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPeriodRangeActionPerformed(evt);
            }
        });
        rdoPeriodRange.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                rdoPeriodRangeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        panProductID.add(rdoPeriodRange, gridBagConstraints);

        rdoFromDateGroup.add(rdoAcctOpenDt);
        rdoAcctOpenDt.setText("ActOpen/Mig Dt");
        rdoAcctOpenDt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoAcctOpenDtActionPerformed(evt);
            }
        });
        rdoAcctOpenDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                rdoAcctOpenDtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panProductID.add(rdoAcctOpenDt, gridBagConstraints);

        rdoFromDateGroup.add(rdoLastIntCalcDt);
        rdoLastIntCalcDt.setText("LastIntCalcDt");
        rdoLastIntCalcDt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoLastIntCalcDtActionPerformed(evt);
            }
        });
        rdoLastIntCalcDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                rdoLastIntCalcDtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        panProductID.add(rdoLastIntCalcDt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panAccountInfo.add(panProductID, gridBagConstraints);

        getContentPane().add(panAccountInfo, java.awt.BorderLayout.CENTER);

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
        tbrOperativeAcctProduct.add(btnView);

        lblSpace5.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnNew);

        lblSpace70.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace70.setText("     ");
        lblSpace70.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace70);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnEdit);

        lblSpace71.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace71.setText("     ");
        lblSpace71.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace71);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnDelete);

        lblSpace2.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnSave);

        lblSpace72.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace72.setText("     ");
        lblSpace72.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace72);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnCancel);

        lblSpace3.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.setEnabled(false);
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnAuthorize);

        lblSpace73.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace73.setText("     ");
        lblSpace73.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace73);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.setEnabled(false);
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnException);

        lblSpace74.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace74.setText("     ");
        lblSpace74.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace74);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.setEnabled(false);
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnReject);

        lblSpace4.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrOperativeAcctProduct.add(btnPrint);

        lblSpace75.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace75.setText("     ");
        lblSpace75.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace75);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnClose);

        getContentPane().add(tbrOperativeAcctProduct, java.awt.BorderLayout.NORTH);

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

    private void txtAccountNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAccountNumberActionPerformed
        // TODO add your handling code here:
        //        callView(TXT_NO);
    }//GEN-LAST:event_txtAccountNumberActionPerformed

    private void setMainDetail() {
        cboProductType.setEnabled(true);
        cboProductID.setEnabled(true);
        txtAccountNumber.setEnabled(true);
        txtAccountNumberTo.setEnabled(true);
        chkCurrDate.setEnabled(true);
        chkCurrDate.setSelected(true);
        rdoAcctOpenDt.setEnabled(true);
        rdoLastIntCalcDt.setEnabled(true);
        rdoPeriodRange.setEnabled(true);
        tdtPassDate.setDateValue(DateUtil.getStringDate(currDt));
        cboProductType.setSelectedItem("");
        cboProductID.setSelectedItem("");
        txtAccountNumber.setText("");
        txtAccountNumberTo.setText("");
        txtAccountNumberTo.setText("");
        //lblClosedAccounts.setText("");
       // lblAccountNumber1.setText("");
    }

    private void btnChargeSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChargeSaveActionPerformed

        actNumStatus = "";
        String prodType="";
        String prodId="";
        if (txtAccountNumber.getText().length() > 0 && txtAccountNumberTo.getText().length() <= 0) {
            actNumStatus = "FROM_ACCT";
        } else if (txtAccountNumber.getText().length() > 0 && txtAccountNumberTo.getText().length() > 0) {
            actNumStatus = "BOTH_ACCT";
        } else {
            actNumStatus = "ALL_ACCT";
        }
//        if (!chkCurrDate.isSelected() && CommonUtil.convertObjToStr(tdtPassDate.getDateValue()).equals("")) {//comment by abi
//            ClientUtil.showAlertWindow("Date not entered...");
//            return;
//        }
        prodType =CommonUtil.convertObjToStr(((ComboBoxModel)cboProductType.getModel()).getKeyForSelected());
        prodId =CommonUtil.convertObjToStr(((ComboBoxModel)cboProductID.getModel()).getKeyForSelected());
        if((prodType.equals("AD") || prodType.equals("TL"))&& prodId.length()>0 ){
         HashMap map=   interestCalculationAD();
         if(map !=null && map.size()>0){
              generateNotice();
         }
        }
        
       

    }//GEN-LAST:event_btnChargeSaveActionPerformed
     private HashMap interestCalculationAD() {
        HashMap map = new HashMap();
        HashMap hash = null;
        try {
            String prod_id = "";
            map.put("ACT_FROM", txtAccountNumber.getText());
            map.put("ACT_NUM", txtAccountNumber.getText());
            map.put("ACT_TO", txtAccountNumberTo.getText());
            map.put("FROM_INTERST_REPORT_UI", "FROM_INTERST_REPORT_UI");
            
            map.put("ACT_NUM", txtAccountNumber.getText());
            map.put("ACT_TO", txtAccountNumberTo.getText());
            //		if((ComboBoxModel)cboProdId.getModel()!=null)
            //                if((((ComboBoxModel)cboProdId.getModel()).getKeyForSelected().toString())!=null)
            //                    prod_id=((ComboBoxModel)cboProdId.getModel()).getKeyForSelected().toString();
            map.put("PROD_ID", prod_id);
            map.put("TRANS_DT", currDt);
            map.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
            String mapNameForCalcInt = "IntCalculationDetail";
            if (observable.getProdType().equals("AD")) {
                mapNameForCalcInt = "IntCalculationDetailAD";
            }
            List lst = ClientUtil.executeQuery(mapNameForCalcInt, map);
            if (lst != null && lst.size() > 0) {
                hash = (HashMap) lst.get(0);
                if (hash.get("AS_CUSTOMER_COMES") != null && hash.get("AS_CUSTOMER_COMES").equals("N")) {
                   
                }
                map.put("BRANCH_ID", getSelectedBranchID());  // Changed by Rajesh to get interest for Other branch a/cs
                map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
               

                //                    InterestCalculationTask interestcalTask=new InterestCalculationTask(header);
       if( (rdoPeriodRange.isSelected())&&  (CommonUtil.convertObjToStr(tdtFromDate.getDateValue()).length()==0 ||  
               CommonUtil.convertObjToStr(tdtToDate.getDateValue()).length()==0)){
             ClientUtil.displayAlert("Please Select Dates");
             map =new HashMap();
             return map;
       }
                 map.putAll(hash);
                if(CommonUtil.convertObjToStr(tdtFromDate.getDateValue()).length()>0 &&  CommonUtil.convertObjToStr(tdtToDate.getDateValue()).length()>0){
                    map.put("LAST_INT_CALC_DT", DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue()));
                    map.put("CURR_DATE", DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue()));
                    map.put("FROM_PERIOD", "FROM_PERIOD");
                    
                }else if(rdoAcctOpenDt.isSelected()){
                    map.put("ACCOUNT_OPEN_DT", "ACCOUNT_OPEN_DT");
                    map.put("CURR_DATE", currDt);
                
                } else if(rdoLastIntCalcDt.isSelected()){
                 map.put("ACCOUNT_LAST_INT_CALC_DT", "ACCOUNT_LAST_INT_CALC_DT");
                 map.put("CURR_DATE", currDt);
                }else {
                     map.remove("LAST_INT_CALC_DT");
//                    if(hash.containsKey("DC_MERGED_DT") && hash.get("DC_MERGED_DT") !=null){
//                    if(DateUtil.dateDiff((Date)hash.get("DC_MERGED_DT"), (Date)hash.get("ACCT_OPEN_DT"))>0){
//                         map.put("LAST_INT_CALC_DT", hash.get("ACCT_OPEN_DT"));
//                        }else{
//                         map.put("LAST_INT_CALC_DT", hash.get("DC_MERGED_DT"));
//                         
//                    }
//                    }
                    map.put("CURR_DATE", currDt);
                }
                
               
                map.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");
                
               
                // System.out.println("map before intereest###" + map);
                //                    hash =interestcalTask.interestCalcTermLoanAD(map);
               // observable.setAsAnWhenCustomer(CommonUtil.convertObjToStr(map.get("AS_CUSTOMER_COMES")));
                hash = observable.loanInterestCalculationAsAndWhen(map);
                if (hash == null) {
                    hash = new HashMap();
                }
                hash.putAll(map);
                // System.out.println("hashinterestoutput###" + hash);
                hash.put("AS_CUSTOMER_COMES", map.get("AS_CUSTOMER_COMES"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return hash;
    }

    public void generateNotice() {
        HashMap obj = new HashMap();
        obj.put("PROD_ID", observable.getCbmProductID().getKeyForSelected());
        obj.put("PROD_TYPE", observable.getCbmProdType().getKeyForSelected());
        obj.put(CommonConstants.MAP_WHERE, observable.getTxtAccountNumber());
        obj.put("ACCOUNTNO", observable.getTxtAccountNumber());
        obj.put("ACCT_NUM", observable.getTxtAccountNumber());
        obj.put("ACT_NUM", observable.getTxtAccountNumber());
        obj.put("ACT_NUM_TO", observable.getTxtAccountNumberTo());
        obj.put("ACT_STATUS", actNumStatus);
        obj.put("CURR_DATE", getProperDateFormat(tdtPassDate.getDateValue()));
        obj.put("USER_ID", TrueTransactMain.USER_ID);
        System.out.println("@#$@#$@#$@#$obj" + obj);
     //   observable.getInterestAmount(obj);  comment by abi
        Date param1 = (Date)currDt.clone();
        Date date = new Date();
        ttIntegration = null;
        HashMap paramMap = new HashMap();
        paramMap.put("BranchCode", ProxyParameters.BRANCH_ID);
        paramMap.put("DAY_END_DT", currDt);
        paramMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
        if (!actNumStatus.equals("ALL_ACCT")) {
            paramMap.put("ACT_NUM", observable.getTxtAccountNumber());
            if (observable.getTxtAccountNumberTo().length() > 0) {
                paramMap.put("ACT_NUM_TO", observable.getTxtAccountNumberTo());
            } else {
                paramMap.put("ACT_NUM_TO", observable.getTxtAccountNumber());
            }
        }
        paramMap.put("USER_ID", TrueTransactMain.USER_ID);
        paramMap.put("Param1", param1);
        ttIntegration.setParam(paramMap);
        generateNotice = true;
        String reportName = "interestReport";
        ttIntegration.integrationForPrint(reportName, true);
    }

    private void updateOBFields() {
        observable.setCboProductType(((ComboBoxModel) cboProductType.getModel()).getKeyForSelected().toString());
        observable.setCboProductID(((ComboBoxModel) cboProductID.getModel()).getKeyForSelected().toString());
//        observable.setCboChargesType(((ComboBoxModel)cboChargesType.getModel()).getKeyForSelected().toString());
//        observable.setTxtChargesAmount(new Double(txtChargesAmount.getText()));
    }

    private void resetFields() {
//        cboChargesType.setSelectedItem("");
//        txtChargesAmount.setText("");
//        tdtChargesDate.setDateValue("");
    }
    private void cboProductTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductTypeActionPerformed
        // TODO add your handling code here:
        if (cboProductType.getSelectedIndex() > 0) {
            String prodType = ((ComboBoxModel) cboProductType.getModel()).getKeyForSelected().toString();
            cboProductID.setModel(new ComboBoxModel());
            observable.setProdType(prodType);
            observable.getProducts();
            cboProductID.setModel(observable.getCbmProductID());
        }
    }//GEN-LAST:event_cboProductTypeActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authroizeActionPerformed();


    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authroizeActionPerformed();


    }//GEN-LAST:event_btnAuthorizeActionPerformed
    private void authroizeActionPerformed() {
        if (viewType == AUTHORIZE && isFilled) {
            viewType = 0;
            HashMap map = new HashMap();
            map.put("ACT_NUM", txtAccountNumber.getText());
            map.put("AUTHORIZE_BY", TrueTransactMain.USER_ID);
            map.put("AUTHORIZE_DATE", currDt);
            map.put("AUTHORIZE_STATUS", "AUTHORIZED");
            observable.setAuthorizeMap(map);
            observable.doAction();
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                observable.resetForm();
            }
            btnCancelActionPerformed();
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
            }
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
            }
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
            }
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
            observable.setResultStatus();
        } else {
            HashMap mapParam = new HashMap();
            HashMap map = new HashMap();
            map.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            mapParam.put(CommonConstants.MAP_NAME, "viewChargesDetailsForAuthorize");
            isFilled = false;
            mapParam.put(CommonConstants.MAP_WHERE, map);
            viewType = AUTHORIZE;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            menuEnableDisable();
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
            btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
            btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
            //            if(observable.getActionType()!=ClientConstants.ACTIONTYPE_AUTHORIZE){
            //                btnReject.setEnabled(!btnReject.isEnabled());
            //
            //            }else{
            //                btnAuthorize.setEnabled(!btnAuthorize.isEnabled());
            //            }

        }
    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {
        btnCloseActionPerformed(evt);
    }

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
    }//GEN-LAST:event_btnDeleteActionPerformed
    private void popUp(String field) {

        final HashMap viewMap = new HashMap();
        String loanStatus = "";
        if (field.equals("CLOSED_ACCT")) {
            loanStatus = field;
            field = "Enquirystatus";
        }
        //if ( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||  observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
        if (field.equals("Edit") || field.equals("Delete") || field.equals("Enquirystatus")) {
            viewType = EDIT;
            //            super.removeEditLock(lblBorrowerNo_2.getText()); remove only accour authorization by abi

            //            ArrayList lst = new ArrayList();
            //            lst.add("BORROWER NO");
            //            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            //            lst = null;

            lblStatus.setText(ClientConstants.ACTION_STATUS[0]);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
            HashMap editMapCondition = new HashMap();
            editMapCondition.put("BRANCH_ID", getSelectedBranchID());
            if (loanStatus.length() > 0) {
                editMapCondition.put("CLOSED_ACCT", "CLOSED_ACCT");
            }
            viewMap.put(CommonConstants.MAP_WHERE, editMapCondition);
            //            if (loanType.equals("LTD"))
            viewMap.put(CommonConstants.MAP_NAME, "viewChargesDetails");
            //
            //mapped statement: viewTermLoan---> result map should be a Hashmap in OB...


        }
        //            updateOBFields();
        //            viewMap.put(CommonConstants.MAP_NAME, "Cash.getGuarantorAccountList"+strSelectedProdType);
        HashMap whereListMap = new HashMap();
        //            whereListMap.put("CUST_ID", txtCustomerID_GD.getText());
        //            whereListMap.put("PROD_ID", strSelectedProdID);
        viewMap.put(CommonConstants.MAP_WHERE, whereListMap);
        //        }
        new ViewAll(this, viewMap).show();
    }
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        btnEditPressed = true;
        popUp("Edit");
    }//GEN-LAST:event_btnEditActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
    }//GEN-LAST:event_mitCancelActionPerformed
    private void menuEnable(boolean flag) {
        btnNew.setEnabled(flag);
        btnEdit.setEnabled(flag);
        btnDelete.setEnabled(flag);
        btnSave.setEnabled(!flag);
        btnCancel.setEnabled(!flag);
        btnAuthorize.setEnabled(flag);
        btnReject.setEnabled(flag);
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        btnCancelActionPerformed();
        menuEnable(true);
        setMainDetail();
        lblCustomerNameDisplay.setText("");
        lblCustomerNameDisplayTo.setText("");

    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnCancelActionPerformed() {
        observable.resetForm();
        btnEditPressed = false;
        btnMainNewPressed = false;
        ClientUtil.enableDisable(this, false);
        menuEnableDisable();
        buttonEnableDisable(false);
        observable.setAuthorizeMap(null);
        isFilled = false;
    }

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());

        menuEnableDisable();
        buttonEnableDisable(true);
        btnMainNewPressed = true;
        //        btnChargeNewActionPerformed(evt);
        setMainDetail();
        enableDisableDate(true);
        resetFields();
    }//GEN-LAST:event_btnNewActionPerformed

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        menuEnableDisable();
        buttonEnableDisable(true);
    }//GEN-LAST:event_mitEditActionPerformed

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        //        btnNewActionPerformed(evt);
        btnMainNewPressed = false;
        btnMainNewPressed = false;

    }//GEN-LAST:event_mitNewActionPerformed

    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed

    private void txtAccountNumberFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccountNumberFocusLost
        //        txtAccountNumberActionPerformed(null);
        if (txtAccountNumber.getText().length()>0) {
            callView(TXT_NO);
        }
    }//GEN-LAST:event_txtAccountNumberFocusLost

    private void btnAccountNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccountNumberActionPerformed
        callView(ACT_NUM);
    }//GEN-LAST:event_btnAccountNumberActionPerformed
    private void callView(int currField) {
        HashMap viewMap = new HashMap();
        HashMap whereListMap = new HashMap();
        viewType = currField;
        if (currField == ACT_NUM || currField == ACT_NUM_TO) {
            //            this.txtAccountNo.setText("");
            //if(chkClosedAccount.isSelected()){
                    viewMap.put(CommonConstants.MAP_NAME, "Transfer.getClosedAccountListCharges");
                    whereListMap.put(closedAcct, closedAcct);
//            }
//            else{
//                    viewMap.put(CommonConstants.MAP_NAME, "Transfer.getAccountListCharges");
//            }
           
            whereListMap.put("PROD_ID", ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected());
            whereListMap.put("SELECTED_BRANCH", TrueTransactMain.selBranch);
            
            viewMap.put(CommonConstants.MAP_WHERE, whereListMap);
            System.out.println("viewMap:" + viewMap);
            new ViewAll(this, viewMap).show();
        }
        if (currField == TXT_NO || currField == TXT_NO_TO) {
             whereListMap = new HashMap();
            if (currField == TXT_NO) {
                whereListMap.put("ACCOUNTNO", txtAccountNumber.getText());
            } else if (currField == TXT_NO_TO) {
                whereListMap.put("ACCOUNTNO", txtAccountNumberTo.getText());
            }
//            if(chkClosedAccount.isSelected()){
//                whereListMap.put(closedAcct, closedAcct);
//            }
            whereListMap.put("PROD_ID", ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected());
            List lst = ClientUtil.executeQuery("TermLoanChargescustomerName", whereListMap);
            System.out.println("@#$@#$@#$lst:" + lst);
            if (lst != null && lst.size() > 0) {
                whereListMap = (HashMap) lst.get(0);
                whereListMap.put("SELECTED_BRANCH", TrueTransactMain.selBranch);
                if (currField == TXT_NO) {
                    lblCustomerNameDisplay.setText(CommonUtil.convertObjToStr(whereListMap.get("Name")));
                    whereListMap.put("ACCOUNTNO", txtAccountNumber.getText());
                } else if (currField == TXT_NO_TO) {
                    lblCustomerNameDisplayTo.setText(CommonUtil.convertObjToStr(whereListMap.get("Name")));
                    whereListMap.put("ACCOUNTNO", txtAccountNumberTo.getText());
                }
                whereListMap.put("CUSTOMERNAME", whereListMap.get("NAME"));
                whereListMap.put("PROD_ID", ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected());

            } else {
                ClientUtil.displayAlert("Invalid Account No...");
                if (currField == TXT_NO) {
                    txtAccountNumber.setText("");
                    lblCustomerNameDisplay.setText("");
                } else if (currField == TXT_NO_TO) {
                    txtAccountNumberTo.setText("");
                    lblCustomerNameDisplayTo.setText("");
                }
                return;
            }
            fillData(whereListMap);
        }
        if (lblCustomerNameDisplay.getText().equals("")) {
            txtAccountNumber.setText("");
        }
        if (lblCustomerNameDisplayTo.getText().equals("")) {
            txtAccountNumberTo.setText("");
        }
    }
    private void cboProductIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductIDActionPerformed
        //To get the AccountHead details for a proper ProductID
        String prodId = ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected().toString();
        if (prodId != null && prodId.length() > 0) {
            observable.setCboProductID(prodId);
            observable.getAccountHeadForProductId(prodId);

//            lblAccountHeadDisplay.setText(observable.getAccountHeadId());
        }
    }//GEN-LAST:event_cboProductIDActionPerformed

    private void txtAccountNumberToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAccountNumberToActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAccountNumberToActionPerformed

    private void txtAccountNumberToFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccountNumberToFocusLost
        // TODO add your handling code here:
        if (txtAccountNumberTo.getText().length()>0) {
            callView(TXT_NO_TO);
        }
    }//GEN-LAST:event_txtAccountNumberToFocusLost

    private void btnAccountNumberToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccountNumberToActionPerformed
        callView(ACT_NUM_TO);
    }//GEN-LAST:event_btnAccountNumberToActionPerformed

    private void chkCurrDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkCurrDateActionPerformed
        // TODO add your handling code here:
        tdtPassDate.setDateValue(DateUtil.getStringDate(currDt));
        if (chkCurrDate.isSelected()) {
            tdtPassDate.setEnabled(false);
            resetFromDate();
        } else {
            tdtPassDate.setEnabled(true);
        }
    }//GEN-LAST:event_chkCurrDateActionPerformed

    private void tdtPassDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtPassDateFocusLost
        // TODO add your handling code here:
     
    }//GEN-LAST:event_tdtPassDateFocusLost

    private void tdtToDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtToDateFocusLost
        // TODO add your handling code here:
        
         if(CommonUtil.convertObjToStr(tdtToDate.getDateValue()).length()>0){
             if(DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtToDate.getDateValue())),
                     DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtFromDate.getDateValue())))>0){
                  ClientUtil.displayAlert("Selected Date Should not less then from date");
                 tdtToDate.setDateValue("");
             }
         }
    }//GEN-LAST:event_tdtToDateFocusLost

    private void tdtFromDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtFromDateFocusLost
        // TODO add your handling code here:
            if(CommonUtil.convertObjToStr(tdtFromDate.getDateValue()).length()>0){
             if(validateDates(CommonUtil.convertObjToStr(tdtFromDate.getDateValue()))){
                 tdtFromDate.setDateValue("");
             }
         }
    }//GEN-LAST:event_tdtFromDateFocusLost

    private void rdoAcctOpenDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rdoAcctOpenDtFocusLost
        // TODO add your handling code here:
     
    }//GEN-LAST:event_rdoAcctOpenDtFocusLost

    private void rdoLastIntCalcDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rdoLastIntCalcDtFocusLost
        // TODO add your handling code here:
    
    }//GEN-LAST:event_rdoLastIntCalcDtFocusLost

    private void rdoPeriodRangeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rdoPeriodRangeFocusLost
        // TODO add your handling code here:
     
    }//GEN-LAST:event_rdoPeriodRangeFocusLost

    private void rdoLastIntCalcDtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoLastIntCalcDtActionPerformed
        // TODO add your handling code here:
            if(rdoLastIntCalcDt.isSelected()){
            enableDisableDate(false);
            resetFromDate();
       }
    }//GEN-LAST:event_rdoLastIntCalcDtActionPerformed

    private void rdoAcctOpenDtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoAcctOpenDtActionPerformed
        // TODO add your handling code here:
           if(rdoAcctOpenDt.isSelected()){
            enableDisableDate(false);
            resetFromDate();
       }
    }//GEN-LAST:event_rdoAcctOpenDtActionPerformed

    private void rdoPeriodRangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPeriodRangeActionPerformed
        // TODO add your handling code here:
          if(rdoPeriodRange.isSelected()){
            enableDisableDate(true);
            resetFromDate();
       }
    }//GEN-LAST:event_rdoPeriodRangeActionPerformed
   private boolean validateDates(String dt){
       if(CommonUtil.convertObjToStr(dt).length()>0)
        if(DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(dt), currDt) <0){
            ClientUtil.displayAlert("Selected Date Should not be Current Date");
             return true;
        }
       return false;
   }
    
    private void enableDisableDate(boolean flag){  //added by abi
        tdtFromDate.setEnabled(flag);
        tdtToDate.setEnabled(flag);
     }
     private void resetFromDate(){ //added by abi
        tdtFromDate.setDateValue("");
        tdtToDate.setDateValue("");
     }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAccountNumber;
    private com.see.truetransact.uicomponent.CButton btnAccountNumberTo;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnChargeSave;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboProductID;
    private com.see.truetransact.uicomponent.CComboBox cboProductType;
    private com.see.truetransact.uicomponent.CCheckBox chkCurrDate;
    private com.see.truetransact.uicomponent.CLabel lblAccountNumber1;
    private com.see.truetransact.uicomponent.CLabel lblAccountNumber2;
    private com.see.truetransact.uicomponent.CLabel lblAccountNumber5;
    private com.see.truetransact.uicomponent.CLabel lblCalcIntUpto;
    private com.see.truetransact.uicomponent.CLabel lblClosedAccounts;
    private com.see.truetransact.uicomponent.CLabel lblCustomerNameDisplay;
    private com.see.truetransact.uicomponent.CLabel lblCustomerNameDisplayTo;
    private com.see.truetransact.uicomponent.CLabel lblFromDate;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblProductID;
    private com.see.truetransact.uicomponent.CLabel lblProductType;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace70;
    private com.see.truetransact.uicomponent.CLabel lblSpace71;
    private com.see.truetransact.uicomponent.CLabel lblSpace72;
    private com.see.truetransact.uicomponent.CLabel lblSpace73;
    private com.see.truetransact.uicomponent.CLabel lblSpace74;
    private com.see.truetransact.uicomponent.CLabel lblSpace75;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblToDate;
    private com.see.truetransact.uicomponent.CMenuBar mbrCustomer;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAccountInfo;
    private com.see.truetransact.uicomponent.CPanel panAcctNum;
    private com.see.truetransact.uicomponent.CPanel panAcctNum1;
    private com.see.truetransact.uicomponent.CPanel panButtonCharges;
    private com.see.truetransact.uicomponent.CPanel panButtonCharges1;
    private com.see.truetransact.uicomponent.CPanel panProductID;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CRadioButton rdoAcctOpenDt;
    private com.see.truetransact.uicomponent.CButtonGroup rdoFromDateGroup;
    private com.see.truetransact.uicomponent.CRadioButton rdoLastIntCalcDt;
    private com.see.truetransact.uicomponent.CRadioButton rdoPeriodRange;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private javax.swing.JToolBar tbrOperativeAcctProduct;
    private com.see.truetransact.uicomponent.CDateField tdtFromDate;
    private com.see.truetransact.uicomponent.CDateField tdtPassDate;
    private com.see.truetransact.uicomponent.CDateField tdtToDate;
    private com.see.truetransact.uicomponent.CTextField txtAccountNumber;
    private com.see.truetransact.uicomponent.CTextField txtAccountNumberTo;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] args) {
        InterestReportUI ac = new InterestReportUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(ac);
        j.show();
        ac.show();
    }

    /**
     * Getter for property mandatoryMap.
     *
     * @return Value of property mandatoryMap.
     */
    public java.util.HashMap getMandatoryMap() {
        return mandatoryMap;
    }

    /**
     * Setter for property mandatoryMap.
     *
     * @param mandatoryMap New value of property mandatoryMap.
     */
    public void setMandatoryMap(java.util.HashMap mandatoryMap) {
        this.mandatoryMap = mandatoryMap;
    }
}
