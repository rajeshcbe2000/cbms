/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DepositMultiRenewal.java
 *
 * Created on October 10th, 2011, 11:03 PM
 */
package com.see.truetransact.ui.deposit.multiplerenewal;

/**
 *
 * @author
 */
import java.util.*;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.ui.common.viewall.TextUI;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.COptionPane;
import java.awt.Color;
import javax.swing.table.*;
import java.awt.Component;
import javax.swing.*;

public class DepositMultiRenewal extends CInternalFrame implements Observer {

    /**
     * Vairable Declarations
     */
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private String viewType = "";//Variable used to store ActionType(New,Edit,Delete)
    DepositMultiRenewalOB observable = null;
    private boolean selectMode = false;
    private Date currDate = null;
    private HashMap returnMap = null;
    ArrayList colorList = new ArrayList();
    private int flagLien = 0;
    private Boolean flagMaturity;
    List finalList = new ArrayList();
    List calFreqAccountList = new ArrayList();
    /**
     * Creates new form TokenConfigUI
     */
    public DepositMultiRenewal() {
        returnMap = null;
        flagMaturity = new Boolean(true);
        currDate = ClientUtil.getCurrentDate();
        initForm();
        rdoRenewalWithdrawingInt_No.setSelected(true);
        rdoRenewalWithdrawingInt_NoActionPerformed(null);
    }

    /**
     * Method which is used to initialize the form TokenConfig
     */
    private void initForm() {
        initComponents();
        observable = new DepositMultiRenewalOB();
        initTableData();
        txtProductID.setAllowAll(true);
        btnCalculate.setEnabled(false);
        btnProcess.setEnabled(false);
        ClientUtil.enableDisable(panDepositInterestApplication, true);
        btnProductID.setEnabled(true);
        btnCalculate.setEnabled(false);
        txtProductID.setEnabled(true);
        manualEntryEnable(false);
        cboRenewalInterestTransProdType.setModel(observable.getCbmProdType());
        cboRenewalInterestTransMode.setModel(observable.getCbmRenewalInterestTransMode());
        cboCategory.setModel(observable.getCbmCategory());
        txtProductID.setEnabled(false);
        btnProductID.setEnabled(false);
        btnClose.setEnabled(false);
        btnClear.setEnabled(false);
        btnNew.setEnabled(true);
        btnNew.setVisible(false);
        btnNewActionPerformed(null);
    }

    private void initTableData() {
        tblDepositInterestApplication.setModel(observable.getTblDepositInterestApplication());
    }

    private void manualEntryEnable(boolean flag) {
    }

    /* Auto Generated Method - update()
    This method called by Observable. It updates the UI with
    Observable's data. If needed add/Remove RadioButtons
    method need to be added.*/
    public void update(Observable observed, Object arg) {
    }

    /* Auto Generated Method - updateOBFields()
    This method called by Save option of UI.
    It updates the OB with UI data.*/
    public void updateOBFields() {
    }

    /* Auto Generated Method - setMandatoryHashMap()
    
    ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
    
    This method list out all the Input Fields available in the UI.
    It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
    }

    /**
     * Used to set Maximum possible lenghts for TextFields
     */
    private void setMaxLengths() {
    }

    /**
     * Method used to Give a Alert when any Mandatory Field is not filled by the
     * user
     */
    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }

    /* Method used to showPopup ViewAll by Executing a Query */
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        HashMap hash = new HashMap();
        if (flagMaturity) {
            hash.put("MATURITY", flagMaturity);
        }
        if (currField.equalsIgnoreCase("PROD_DETAILS")) {
            List prodID = new ArrayList();
            viewMap.put(CommonConstants.MAP_NAME, "getFixedDepositProductsMultiple");
        } else if (currField.equalsIgnoreCase("FROM") || currField.equalsIgnoreCase("TO") || currField.equalsIgnoreCase("DEPOSIT_NO_MANUAL")) {
            hash.put("PROD_ID", txtProductID.getText());

            hash.put("CURR_DATE", getProperDate(currDate));
            hash.put("PROD_ID", txtProductID.getText());
            hash.put(CommonConstants.BRANCH_ID, com.see.truetransact.ui.TrueTransactMain.selBranch);
            viewMap.put(CommonConstants.MAP_WHERE, hash);
            //  viewMap.put(CommonConstants.MAP_NAME, "TDCharges.getAcctList");
            viewMap.put(CommonConstants.MAP_NAME, "viewAllRenewalAccInfoMultiple");
        } else if (currField.equalsIgnoreCase("OPERATIVE_NO")) {
            String str = CommonUtil.convertObjToStr(observable.getCbmOAProductID().getKeyForSelected());
            hash.put("PROD_ID", str);
            hash.put("SELECTED_BRANCH", getSelectedBranchID());
            viewMap.put(CommonConstants.MAP_WHERE, hash);
            // viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountListOA");
            viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList" + ((ComboBoxModel) cboRenewalInterestTransProdType.getModel()).getKeyForSelected().toString());
        } else if (currField == "RENEWAL_INT_TRANS_ACC_NO") {
            //   HashMap viewMap = new HashMap();
            String prodType = ((ComboBoxModel) cboRenewalInterestTransProdType.getModel()).getKeyForSelected().toString();
            if (!prodType.equals("GL")) {
                viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"
                        + ((ComboBoxModel) cboRenewalInterestTransProdType.getModel()).getKeyForSelected().toString());
            } else {
                viewMap.put(CommonConstants.MAP_NAME, "Cash.getSelectAcctHead");
                //                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString());
            }
            HashMap whereMap = new HashMap();
            whereMap.put("PROD_ID", ((ComboBoxModel) cboRenewalInterestTransProdId.getModel()).getKeyForSelected());
//            if (whereMap.get("SELECTED_BRANCH") == null ) {
//                whereMap.put("SELECTED_BRANCH", ProxyParameters.BRANCH_ID);
//            } else {
//                whereMap.put("SELECTED_BRANCH", getSelectedBranchID());
//            }
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            //  new ViewAll(this, viewMap).show();
        }
        new ViewAll(this, viewMap).show();
    }

    /* Fills up the HashMap with data when user selects the row in ViewAll screen  */
    public void fillData(Object map) {
        try {
            HashMap hash = (HashMap) map;
            System.out.println("#@@# Hash :" + hash);
            if (viewType != null) {
                if (viewType.equalsIgnoreCase("PROD_DETAILS")) {
                    txtProductID.setText(CommonUtil.convertObjToStr(hash.get("PROD_ID")));
                    //    lblProductName.setText(CommonUtil.convertObjToStr(hash.get("PROD_DESC")));
                    btnProductID.setEnabled(true);
                }
            }

            if (viewType.equals("Customer")) {
                //__ To reset the data for the Previous selected Customer..
                final String CUSTID = CommonUtil.convertObjToStr(hash.get("CUST_ID"));
                txtCustID.setText(CUSTID);
                //__ To set the Name of the Customer...
                final String CUSTNAME = CommonUtil.convertObjToStr(hash.get("NAME"));
                lblCustName.setText(CUSTNAME);
                txtFromAccount.setText("");
                txtToAccount.setText("");

            }
            if (viewType.equals("CUSTOMER ID")) {
                CustInfoDisplay(CommonUtil.convertObjToStr(hash.get("CUSTOMER ID")));
            }
            if (viewType.equals("FROM")) {
                txtFromAccount.setText(CommonUtil.convertObjToStr(hash.get("DEPOSIT NO")));
            }

            if (viewType.equals("TO")) {
                txtToAccount.setText(CommonUtil.convertObjToStr(hash.get("DEPOSIT NO")));
                if(txtFromAccount.getText()!=null && txtFromAccount.getText().length()>0){
                    if(txtToAccount.getText()!=null && txtToAccount.getText().length()>0){
                        String category = observable.getDepositCategoryForCustomer(txtFromAccount.getText(),txtToAccount.getText());
                        if(category!=null && category.length()>0){
                            cboCategory.setSelectedItem(observable.getCbmCategory().getDataForKey(category));
                        }
                    }
                }
            }
            if (viewType.equals("RENEWAL_INT_TRANS_ACC_NO")) {
                //  renewalSubNo = true;
                String prodType = ((ComboBoxModel) cboRenewalInterestTransProdType.getModel()).getKeyForSelected().toString();
                if (prodType != null && !prodType.equals("GL")) {
                    if (prodType.equals("TD")) {
                        hash.put("ACCOUNTNO", hash.get("ACCOUNTNO") + "_1");
                    }
                    txtRenewalIntCustomerIdCr.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
                    //   lblRenewalInterestCustNameVal.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMERNAME")));
                } else {
                    txtRenewalIntCustomerIdCr.setText(CommonUtil.convertObjToStr(hash.get("A/C HEAD")));
                    //    lblRenewalInterestCustNameVal.setText(CommonUtil.convertObjToStr(hash.get("A/C HEAD DESCRIPTION")));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CustInfoDisplay(String custId) {

        HashMap custMap = new HashMap();
        custMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        custMap.put("CUST_ID", custId);
        List lst = ClientUtil.executeQuery("getDepositCustomerName", custMap);
        if (lst != null && lst.size() > 0) {
            custMap = (HashMap) lst.get(0);
            lblCustName.setText(CommonUtil.convertObjToStr(custMap.get("FNAME")));
            lst.clear();
            custMap.clear();
        } else {
            ClientUtil.showAlertWindow("Invalid Customer or This Customer not having Deposit A/cs...");
        }
        lst = null;
        custMap = null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panDepositInterestApplication = new com.see.truetransact.uicomponent.CPanel();
        panProductDetails = new com.see.truetransact.uicomponent.CPanel();
        lblCustName = new com.see.truetransact.uicomponent.CLabel();
        panFromTonum = new com.see.truetransact.uicomponent.CPanel();
        lblCustID = new com.see.truetransact.uicomponent.CLabel();
        panCustID = new com.see.truetransact.uicomponent.CPanel();
        lblFromAccount = new com.see.truetransact.uicomponent.CLabel();
        lblToAccount = new com.see.truetransact.uicomponent.CLabel();
        panToAccount2 = new com.see.truetransact.uicomponent.CPanel();
        txtCustID = new com.see.truetransact.uicomponent.CTextField();
        btnCustID = new com.see.truetransact.uicomponent.CButton();
        txtFromAccount = new com.see.truetransact.uicomponent.CTextField();
        btnFromAccount = new com.see.truetransact.uicomponent.CButton();
        txtToAccount = new com.see.truetransact.uicomponent.CTextField();
        btnToAccount = new com.see.truetransact.uicomponent.CButton();
        panProductIdMain = new com.see.truetransact.uicomponent.CPanel();
        lblProductID = new com.see.truetransact.uicomponent.CLabel();
        panProductID = new com.see.truetransact.uicomponent.CPanel();
        txtProductID = new com.see.truetransact.uicomponent.CTextField();
        btnProductID = new com.see.truetransact.uicomponent.CButton();
        panProcessButton = new com.see.truetransact.uicomponent.CPanel();
        btnCalculate = new com.see.truetransact.uicomponent.CButton();
        panNewPeriodOfDeposit = new com.see.truetransact.uicomponent.CPanel();
        txtRenewalPeriodOfDeposit_Years = new com.see.truetransact.uicomponent.CTextField();
        lblRenewalPeriod_Years = new com.see.truetransact.uicomponent.CLabel();
        txtRenewalPeriodOfDeposit_Months = new com.see.truetransact.uicomponent.CTextField();
        lblRenewalPeriod_Months = new com.see.truetransact.uicomponent.CLabel();
        txtRenewalPeriodOfDeposit_Days = new com.see.truetransact.uicomponent.CTextField();
        lblRenewalPeriod_Days = new com.see.truetransact.uicomponent.CLabel();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        cboRenewalInterestTransMode = new com.see.truetransact.uicomponent.CComboBox();
        cboRenewalInterestTransProdType = new com.see.truetransact.uicomponent.CComboBox();
        cboRenewalInterestTransProdId = new com.see.truetransact.uicomponent.CComboBox();
        btnRenewalIntCustomerIdFileOpenCr = new com.see.truetransact.uicomponent.CButton();
        txtRenewalIntCustomerIdCr = new com.see.truetransact.uicomponent.CTextField();
        lblRenewalInterestTransAccNo = new com.see.truetransact.uicomponent.CLabel();
        lblRenewalInterestTransProdId = new com.see.truetransact.uicomponent.CLabel();
        lblRenewalInterestTransProdType = new com.see.truetransact.uicomponent.CLabel();
        lblRenewalInterestTransMode = new com.see.truetransact.uicomponent.CLabel();
        rdoRenewalWithdrawingInt_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoRenewalWithdrawingInt_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblRenewalWithdrawingIntAmt = new com.see.truetransact.uicomponent.CLabel();
        lblCategory = new com.see.truetransact.uicomponent.CLabel();
        cboCategory = new com.see.truetransact.uicomponent.CComboBox();
        panProductTableData = new com.see.truetransact.uicomponent.CPanel();
        srpDepositInterestApplication = new com.see.truetransact.uicomponent.CScrollPane();
        tblDepositInterestApplication = new com.see.truetransact.uicomponent.CTable();
        panSelectAll = new com.see.truetransact.uicomponent.CPanel();
        lblSelectAll = new com.see.truetransact.uicomponent.CLabel();
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        panProcess = new com.see.truetransact.uicomponent.CPanel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        btnClear = new com.see.truetransact.uicomponent.CButton();
        btnProcess = new com.see.truetransact.uicomponent.CButton();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblTotRecord = new com.see.truetransact.uicomponent.CLabel();
        lblTotRecordval = new com.see.truetransact.uicomponent.CLabel();
        lblTotInterest = new com.see.truetransact.uicomponent.CLabel();
        lblTotIntVal = new com.see.truetransact.uicomponent.CLabel();
        cLabel2 = new com.see.truetransact.uicomponent.CLabel();
        lblTotTDSVal = new com.see.truetransact.uicomponent.CLabel();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMaximumSize(new java.awt.Dimension(980, 640));
        setMinimumSize(new java.awt.Dimension(980, 640));
        setPreferredSize(new java.awt.Dimension(980, 640));
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        panDepositInterestApplication.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panDepositInterestApplication.setMaximumSize(new java.awt.Dimension(950, 450));
        panDepositInterestApplication.setMinimumSize(new java.awt.Dimension(950, 450));
        panDepositInterestApplication.setPreferredSize(new java.awt.Dimension(950, 450));
        panDepositInterestApplication.setRequestFocusEnabled(false);
        panDepositInterestApplication.setLayout(null);

        panProductDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Deposit Details"));
        panProductDetails.setMaximumSize(new java.awt.Dimension(940, 140));
        panProductDetails.setMinimumSize(new java.awt.Dimension(940, 140));
        panProductDetails.setPreferredSize(new java.awt.Dimension(940, 140));
        panProductDetails.setLayout(null);

        lblCustName.setForeground(new java.awt.Color(0, 51, 204));
        lblCustName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        panProductDetails.add(lblCustName);
        lblCustName.setBounds(0, 0, 0, 0);

        panFromTonum.setMaximumSize(new java.awt.Dimension(550, 110));
        panFromTonum.setMinimumSize(new java.awt.Dimension(550, 110));
        panFromTonum.setPreferredSize(new java.awt.Dimension(550, 110));
        panFromTonum.setLayout(null);

        lblCustID.setText("Member/Customer ID");
        lblCustID.setToolTipText("Member/Customer ID");
        lblCustID.setMaximumSize(new java.awt.Dimension(160, 18));
        lblCustID.setMinimumSize(new java.awt.Dimension(160, 18));
        lblCustID.setPreferredSize(new java.awt.Dimension(160, 18));
        panFromTonum.add(lblCustID);
        lblCustID.setBounds(0, 10, 140, 18);

        panCustID.setLayout(new java.awt.GridBagLayout());
        panFromTonum.add(panCustID);
        panCustID.setBounds(130, 50, 123, 21);

        lblFromAccount.setText("FromAccount");
        lblFromAccount.setToolTipText("FromAccount");
        lblFromAccount.setMaximumSize(new java.awt.Dimension(90, 18));
        lblFromAccount.setMinimumSize(new java.awt.Dimension(90, 18));
        lblFromAccount.setPreferredSize(new java.awt.Dimension(90, 18));
        panFromTonum.add(lblFromAccount);
        lblFromAccount.setBounds(468, 10, 90, 18);

        lblToAccount.setText("To Account");
        lblToAccount.setToolTipText("To Account");
        lblToAccount.setMaximumSize(new java.awt.Dimension(75, 18));
        lblToAccount.setMinimumSize(new java.awt.Dimension(75, 18));
        lblToAccount.setPreferredSize(new java.awt.Dimension(75, 18));
        panFromTonum.add(lblToAccount);
        lblToAccount.setBounds(700, 10, 80, 18);

        panToAccount2.setLayout(new java.awt.GridBagLayout());
        panFromTonum.add(panToAccount2);
        panToAccount2.setBounds(0, 0, 0, 0);

        txtCustID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCustID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCustIDFocusLost(evt);
            }
        });
        panFromTonum.add(txtCustID);
        txtCustID.setBounds(140, 10, 100, 21);

        btnCustID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCustID.setEnabled(false);
        btnCustID.setMinimumSize(new java.awt.Dimension(21, 21));
        btnCustID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCustID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustIDActionPerformed(evt);
            }
        });
        panFromTonum.add(btnCustID);
        btnCustID.setBounds(250, 10, 21, 21);

        txtFromAccount.setAllowAll(true);
        txtFromAccount.setMinimumSize(new java.awt.Dimension(120, 21));
        txtFromAccount.setPreferredSize(new java.awt.Dimension(120, 21));
        txtFromAccount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFromAccountFocusLost(evt);
            }
        });
        panFromTonum.add(txtFromAccount);
        txtFromAccount.setBounds(560, 10, 120, 21);

        btnFromAccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnFromAccount.setEnabled(false);
        btnFromAccount.setMinimumSize(new java.awt.Dimension(21, 21));
        btnFromAccount.setPreferredSize(new java.awt.Dimension(21, 21));
        btnFromAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFromAccountActionPerformed(evt);
            }
        });
        panFromTonum.add(btnFromAccount);
        btnFromAccount.setBounds(680, 10, 21, 21);

        txtToAccount.setAllowAll(true);
        txtToAccount.setMinimumSize(new java.awt.Dimension(120, 21));
        txtToAccount.setPreferredSize(new java.awt.Dimension(120, 21));
        txtToAccount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtToAccountFocusLost(evt);
            }
        });
        panFromTonum.add(txtToAccount);
        txtToAccount.setBounds(780, 10, 120, 21);

        btnToAccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnToAccount.setEnabled(false);
        btnToAccount.setMinimumSize(new java.awt.Dimension(21, 21));
        btnToAccount.setPreferredSize(new java.awt.Dimension(21, 21));
        btnToAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToAccountActionPerformed(evt);
            }
        });
        panFromTonum.add(btnToAccount);
        btnToAccount.setBounds(900, 10, 21, 21);

        panProductIdMain.setMaximumSize(new java.awt.Dimension(230, 100));
        panProductIdMain.setMinimumSize(new java.awt.Dimension(230, 100));
        panProductIdMain.setPreferredSize(new java.awt.Dimension(230, 100));
        panProductIdMain.setLayout(new java.awt.GridBagLayout());

        lblProductID.setText("Product ID");
        lblProductID.setToolTipText("Product ID");
        lblProductID.setMaximumSize(new java.awt.Dimension(72, 18));
        lblProductID.setMinimumSize(new java.awt.Dimension(72, 18));
        lblProductID.setPreferredSize(new java.awt.Dimension(72, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panProductIdMain.add(lblProductID, gridBagConstraints);

        panProductID.setLayout(new java.awt.GridBagLayout());

        txtProductID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtProductID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtProductIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panProductID.add(txtProductID, gridBagConstraints);

        btnProductID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnProductID.setEnabled(false);
        btnProductID.setMinimumSize(new java.awt.Dimension(21, 21));
        btnProductID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnProductID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductID.add(btnProductID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 6);
        panProductIdMain.add(panProductID, gridBagConstraints);

        panFromTonum.add(panProductIdMain);
        panProductIdMain.setBounds(270, 0, 200, 40);

        panProductDetails.add(panFromTonum);
        panFromTonum.setBounds(10, 20, 920, 40);

        panProcessButton.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panProcessButton.setMinimumSize(new java.awt.Dimension(140, 80));
        panProcessButton.setPreferredSize(new java.awt.Dimension(140, 80));
        panProcessButton.setLayout(null);

        btnCalculate.setText("Display");
        btnCalculate.setToolTipText("Display");
        btnCalculate.setMaximumSize(new java.awt.Dimension(96, 21));
        btnCalculate.setMinimumSize(new java.awt.Dimension(96, 21));
        btnCalculate.setPreferredSize(new java.awt.Dimension(96, 21));
        btnCalculate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalculateActionPerformed(evt);
            }
        });
        panProcessButton.add(btnCalculate);
        btnCalculate.setBounds(90, 60, 90, 21);

        panNewPeriodOfDeposit.setLayout(new java.awt.GridBagLayout());

        txtRenewalPeriodOfDeposit_Years.setAllowNumber(true);
        txtRenewalPeriodOfDeposit_Years.setMinimumSize(new java.awt.Dimension(30, 21));
        txtRenewalPeriodOfDeposit_Years.setPreferredSize(new java.awt.Dimension(30, 21));
        panNewPeriodOfDeposit.add(txtRenewalPeriodOfDeposit_Years, new java.awt.GridBagConstraints());

        lblRenewalPeriod_Years.setText("Yrs");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 2);
        panNewPeriodOfDeposit.add(lblRenewalPeriod_Years, gridBagConstraints);

        txtRenewalPeriodOfDeposit_Months.setAllowNumber(true);
        txtRenewalPeriodOfDeposit_Months.setMinimumSize(new java.awt.Dimension(30, 21));
        txtRenewalPeriodOfDeposit_Months.setPreferredSize(new java.awt.Dimension(30, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 2);
        panNewPeriodOfDeposit.add(txtRenewalPeriodOfDeposit_Months, gridBagConstraints);

        lblRenewalPeriod_Months.setText("Months");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 2);
        panNewPeriodOfDeposit.add(lblRenewalPeriod_Months, gridBagConstraints);

        txtRenewalPeriodOfDeposit_Days.setAllowAll(true);
        txtRenewalPeriodOfDeposit_Days.setAllowNumber(true);
        txtRenewalPeriodOfDeposit_Days.setMinimumSize(new java.awt.Dimension(45, 21));
        txtRenewalPeriodOfDeposit_Days.setPreferredSize(new java.awt.Dimension(30, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 2);
        panNewPeriodOfDeposit.add(txtRenewalPeriodOfDeposit_Days, gridBagConstraints);

        lblRenewalPeriod_Days.setText("Days");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 2);
        panNewPeriodOfDeposit.add(lblRenewalPeriod_Days, gridBagConstraints);

        panProcessButton.add(panNewPeriodOfDeposit);
        panNewPeriodOfDeposit.setBounds(54, 10, 220, 29);

        cLabel1.setText("Period");
        panProcessButton.add(cLabel1);
        cLabel1.setBounds(10, 10, 50, 30);

        panProductDetails.add(panProcessButton);
        panProcessButton.setBounds(640, 70, 290, 100);

        cPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        cPanel1.setLayout(null);

        cboRenewalInterestTransMode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "----Select----" }));
        cboRenewalInterestTransMode.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRenewalInterestTransMode.setPopupWidth(100);
        cboRenewalInterestTransMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboRenewalInterestTransModeActionPerformed(evt);
            }
        });
        cboRenewalInterestTransMode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboRenewalInterestTransModeFocusLost(evt);
            }
        });
        cPanel1.add(cboRenewalInterestTransMode);
        cboRenewalInterestTransMode.setBounds(500, 10, 110, 21);

        cboRenewalInterestTransProdType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "----Select----" }));
        cboRenewalInterestTransProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRenewalInterestTransProdType.setPopupWidth(200);
        cboRenewalInterestTransProdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboRenewalInterestTransProdTypeActionPerformed(evt);
            }
        });
        cPanel1.add(cboRenewalInterestTransProdType);
        cboRenewalInterestTransProdType.setBounds(90, 40, 100, 21);

        cboRenewalInterestTransProdId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "----Select----" }));
        cboRenewalInterestTransProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRenewalInterestTransProdId.setPopupWidth(200);
        cboRenewalInterestTransProdId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboRenewalInterestTransProdIdActionPerformed(evt);
            }
        });
        cPanel1.add(cboRenewalInterestTransProdId);
        cboRenewalInterestTransProdId.setBounds(280, 40, 100, 21);

        btnRenewalIntCustomerIdFileOpenCr.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnRenewalIntCustomerIdFileOpenCr.setMaximumSize(new java.awt.Dimension(21, 21));
        btnRenewalIntCustomerIdFileOpenCr.setMinimumSize(new java.awt.Dimension(21, 21));
        btnRenewalIntCustomerIdFileOpenCr.setPreferredSize(new java.awt.Dimension(21, 21));
        btnRenewalIntCustomerIdFileOpenCr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRenewalIntCustomerIdFileOpenCrActionPerformed(evt);
            }
        });
        cPanel1.add(btnRenewalIntCustomerIdFileOpenCr);
        btnRenewalIntCustomerIdFileOpenCr.setBounds(590, 40, 21, 21);

        txtRenewalIntCustomerIdCr.setEditable(false);
        txtRenewalIntCustomerIdCr.setMinimumSize(new java.awt.Dimension(100, 21));
        cPanel1.add(txtRenewalIntCustomerIdCr);
        txtRenewalIntCustomerIdCr.setBounds(460, 40, 130, 21);

        lblRenewalInterestTransAccNo.setText("Account No");
        lblRenewalInterestTransAccNo.setToolTipText("Account No");
        lblRenewalInterestTransAccNo.setMaximumSize(new java.awt.Dimension(75, 18));
        lblRenewalInterestTransAccNo.setMinimumSize(new java.awt.Dimension(75, 18));
        lblRenewalInterestTransAccNo.setPreferredSize(new java.awt.Dimension(75, 18));
        cPanel1.add(lblRenewalInterestTransAccNo);
        lblRenewalInterestTransAccNo.setBounds(380, 40, 80, 20);

        lblRenewalInterestTransProdId.setText("Product Id");
        lblRenewalInterestTransProdId.setToolTipText("Product Id");
        lblRenewalInterestTransProdId.setMaximumSize(new java.awt.Dimension(64, 18));
        lblRenewalInterestTransProdId.setMinimumSize(new java.awt.Dimension(64, 18));
        lblRenewalInterestTransProdId.setPreferredSize(new java.awt.Dimension(64, 18));
        cPanel1.add(lblRenewalInterestTransProdId);
        lblRenewalInterestTransProdId.setBounds(200, 40, 80, 18);

        lblRenewalInterestTransProdType.setText("Product Type");
        lblRenewalInterestTransProdType.setToolTipText("Product Type");
        lblRenewalInterestTransProdType.setFont(new java.awt.Font("MS Sans Serif", 0, 12)); // NOI18N
        lblRenewalInterestTransProdType.setMaximumSize(new java.awt.Dimension(85, 16));
        lblRenewalInterestTransProdType.setMinimumSize(new java.awt.Dimension(85, 16));
        lblRenewalInterestTransProdType.setPreferredSize(new java.awt.Dimension(85, 16));
        cPanel1.add(lblRenewalInterestTransProdType);
        lblRenewalInterestTransProdType.setBounds(1, 40, 90, 16);

        lblRenewalInterestTransMode.setText("Mode of Withdrawal");
        lblRenewalInterestTransMode.setToolTipText("Mode of Withdrawal");
        lblRenewalInterestTransMode.setMaximumSize(new java.awt.Dimension(128, 18));
        lblRenewalInterestTransMode.setPreferredSize(new java.awt.Dimension(128, 18));
        cPanel1.add(lblRenewalInterestTransMode);
        lblRenewalInterestTransMode.setBounds(370, 10, 130, 18);

        rdoRenewalWithdrawingInt_Yes.setText("Yes");
        rdoRenewalWithdrawingInt_Yes.setMaximumSize(new java.awt.Dimension(50, 18));
        rdoRenewalWithdrawingInt_Yes.setMinimumSize(new java.awt.Dimension(50, 17));
        rdoRenewalWithdrawingInt_Yes.setPreferredSize(new java.awt.Dimension(50, 18));
        rdoRenewalWithdrawingInt_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoRenewalWithdrawingInt_YesActionPerformed(evt);
            }
        });
        cPanel1.add(rdoRenewalWithdrawingInt_Yes);
        rdoRenewalWithdrawingInt_Yes.setBounds(240, 10, 60, 18);

        rdoRenewalWithdrawingInt_No.setText("No");
        rdoRenewalWithdrawingInt_No.setMaximumSize(new java.awt.Dimension(50, 17));
        rdoRenewalWithdrawingInt_No.setMinimumSize(new java.awt.Dimension(50, 17));
        rdoRenewalWithdrawingInt_No.setPreferredSize(new java.awt.Dimension(45, 18));
        rdoRenewalWithdrawingInt_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoRenewalWithdrawingInt_NoActionPerformed(evt);
            }
        });
        cPanel1.add(rdoRenewalWithdrawingInt_No);
        rdoRenewalWithdrawingInt_No.setBounds(300, 10, 45, 18);

        lblRenewalWithdrawingIntAmt.setText("Want to withdraw interest amount ?");
        lblRenewalWithdrawingIntAmt.setToolTipText("Want to withdraw interest amount ?");
        lblRenewalWithdrawingIntAmt.setMaximumSize(new java.awt.Dimension(225, 18));
        lblRenewalWithdrawingIntAmt.setPreferredSize(new java.awt.Dimension(225, 18));
        cPanel1.add(lblRenewalWithdrawingIntAmt);
        lblRenewalWithdrawingIntAmt.setBounds(5, 10, 240, 20);

        lblCategory.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCategory.setText("Category");
        cPanel1.add(lblCategory);
        lblCategory.setBounds(20, 70, 60, 18);

        cboCategory.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "----Select----" }));
        cboCategory.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCategory.setPopupWidth(200);
        cboCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCategoryActionPerformed(evt);
            }
        });
        cboCategory.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboCategoryFocusLost(evt);
            }
        });
        cPanel1.add(cboCategory);
        cboCategory.setBounds(90, 70, 170, 20);

        panProductDetails.add(cPanel1);
        cPanel1.setBounds(10, 70, 620, 100);

        panDepositInterestApplication.add(panProductDetails);
        panProductDetails.setBounds(10, 20, 950, 180);
        panProductDetails.getAccessibleContext().setAccessibleName("");

        panProductTableData.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panProductTableData.setMinimumSize(new java.awt.Dimension(830, 360));
        panProductTableData.setPreferredSize(new java.awt.Dimension(830, 360));
        panProductTableData.setLayout(null);

        srpDepositInterestApplication.setMinimumSize(new java.awt.Dimension(810, 335));
        srpDepositInterestApplication.setPreferredSize(new java.awt.Dimension(810, 335));
        srpDepositInterestApplication.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                srpDepositInterestApplicationMouseClicked(evt);
            }
        });

        tblDepositInterestApplication.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "Cust ID", "Account No", "Name", "Dep AmT", "Dep Date", "Mat Date", "From Date", "To Date", "Interest", "SI A/c No", "Cal Freq"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblDepositInterestApplication.setPreferredScrollableViewportSize(new java.awt.Dimension(806, 331));
        tblDepositInterestApplication.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDepositInterestApplicationMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblDepositInterestApplicationMouseReleased(evt);
            }
        });
        tblDepositInterestApplication.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tblDepositInterestApplicationFocusLost(evt);
            }
        });
        srpDepositInterestApplication.setViewportView(tblDepositInterestApplication);

        panProductTableData.add(srpDepositInterestApplication);
        srpDepositInterestApplication.setBounds(5, 32, 930, 300);

        panSelectAll.setMaximumSize(new java.awt.Dimension(200, 27));
        panSelectAll.setMinimumSize(new java.awt.Dimension(200, 27));
        panSelectAll.setPreferredSize(new java.awt.Dimension(200, 27));
        panSelectAll.setLayout(new java.awt.GridBagLayout());

        lblSelectAll.setText("Select All");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 19, 5, 0);
        panSelectAll.add(lblSelectAll, gridBagConstraints);

        chkSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelectAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 5, 1);
        panSelectAll.add(chkSelectAll, gridBagConstraints);

        panProductTableData.add(panSelectAll);
        panSelectAll.setBounds(10, 10, 110, 20);

        panDepositInterestApplication.add(panProductTableData);
        panProductTableData.setBounds(10, 200, 950, 340);
        panProductTableData.getAccessibleContext().setAccessibleName("Deposit Multiple Renewal");

        panProcess.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panProcess.setMinimumSize(new java.awt.Dimension(880, 30));
        panProcess.setPreferredSize(new java.awt.Dimension(880, 30));
        panProcess.setLayout(new java.awt.GridBagLayout());

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProcess.add(btnClose, gridBagConstraints);

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProcess.add(btnClear, gridBagConstraints);

        btnProcess.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnProcess.setText("PROCESS");
        btnProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panProcess.add(btnProcess, gridBagConstraints);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        panProcess.add(btnNew, gridBagConstraints);

        lblTotRecord.setText("No of Records:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panProcess.add(lblTotRecord, gridBagConstraints);

        lblTotRecordval.setForeground(new java.awt.Color(255, 0, 0));
        lblTotRecordval.setMaximumSize(new java.awt.Dimension(50, 18));
        lblTotRecordval.setMinimumSize(new java.awt.Dimension(50, 18));
        lblTotRecordval.setPreferredSize(new java.awt.Dimension(50, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProcess.add(lblTotRecordval, gridBagConstraints);

        lblTotInterest.setText("Total Interest:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProcess.add(lblTotInterest, gridBagConstraints);

        lblTotIntVal.setForeground(new java.awt.Color(255, 0, 0));
        lblTotIntVal.setMaximumSize(new java.awt.Dimension(75, 21));
        lblTotIntVal.setMinimumSize(new java.awt.Dimension(75, 21));
        lblTotIntVal.setPreferredSize(new java.awt.Dimension(75, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panProcess.add(lblTotIntVal, gridBagConstraints);

        cLabel2.setText("Total TDS : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 0;
        panProcess.add(cLabel2, gridBagConstraints);

        lblTotTDSVal.setMaximumSize(new java.awt.Dimension(75, 21));
        lblTotTDSVal.setMinimumSize(new java.awt.Dimension(75, 21));
        lblTotTDSVal.setPreferredSize(new java.awt.Dimension(75, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 0;
        panProcess.add(lblTotTDSVal, gridBagConstraints);

        panDepositInterestApplication.add(panProcess);
        panProcess.setBounds(10, 540, 950, 40);

        getContentPane().add(panDepositInterestApplication, java.awt.BorderLayout.CENTER);

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

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblDepositInterestApplicationFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblDepositInterestApplicationFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tblDepositInterestApplicationFocusLost

    private void srpDepositInterestApplicationMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_srpDepositInterestApplicationMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_srpDepositInterestApplicationMouseClicked
    public boolean checkAcNoValid(String actNum) {
        HashMap mapData = new HashMap();
        boolean isExists = false;
        try {//dont delete chck selectalldao
            mapData.put("ACT_NUM", actNum);
            List mapDataList = ClientUtil.executeQuery("getActNumFromAllProducts", mapData);
            //System.out.println("#### mapDataList :" + mapDataList);
            if (mapDataList != null && mapDataList.size() > 0) {
                mapData = (HashMap) mapDataList.get(0);
                isExists = true;
                txtRenewalIntCustomerIdCr.setText(CommonUtil.convertObjToStr(mapData.get("ACT_NUM")));
                observable.getCbmProdType().setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                observable.getCbmOAProductID().setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_ID")));
//                 cboOAProductID.setModel(observable.getCbmOAProductID());
                //              cboProdType.setModel(observable.getCbmProdType());
                //               
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
    private void tblDepositInterestApplicationMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDepositInterestApplicationMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tblDepositInterestApplicationMouseReleased
    private Date getProperDate(Date sourceDate) {
        Date targetDate = (Date) currDate.clone();
        targetDate.setDate(sourceDate.getDate());
        targetDate.setMonth(sourceDate.getMonth());
        targetDate.setYear(sourceDate.getYear());
        return targetDate;
    }
    private void btnToAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToAccountActionPerformed
        // TODO add your handling code here:
        if (txtProductID.getText().length() < 0) {
            ClientUtil.showAlertWindow("Please select Product Id!!!");
            return;
        }
        callView("TO");
    }//GEN-LAST:event_btnToAccountActionPerformed

    private void btnFromAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFromAccountActionPerformed
        // TODO add your handling code here:
        if (txtProductID.getText().length() < 0) {
            ClientUtil.showAlertWindow("Please select Product Id!!!");
            return;
        }
        callView("FROM");
    }//GEN-LAST:event_btnFromAccountActionPerformed

    private void txtCustIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCustIDFocusLost
        // TODO add your handling code here:

        String txtCustomer = txtCustID.getText();
        if (txtCustomer.length() > 0) {
            HashMap cust = new HashMap();
            cust.put("CUSTOMER ID", txtCustomer);
            viewType = "CUSTOMER ID";
            fillData(cust);
        }
    }//GEN-LAST:event_txtCustIDFocusLost

    private void btnCustIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustIDActionPerformed
        // TODO add your handling code here:
        viewType = "Customer";
        new CheckCustomerIdUI(this);
    }//GEN-LAST:event_btnCustIDActionPerformed

    private void txtProductIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtProductIDFocusLost
        // TODO add your handling code here:
        if (getScreenID() != null && !getScreenID().equals("") && getScreenID().length() > 0) {
            insertScreenLock(getScreenID(), "EDIT");
        }
        if (txtProductID.getText().length() > 0) {
            HashMap whereMap = new HashMap();
            whereMap.put("PROD_ID", txtProductID.getText());
            whereMap.put("BEHAVES_LIKE", "FIXED");
            List lst = ClientUtil.executeQuery("getAcctHead", whereMap);
            if (lst != null && lst.size() > 0) {
                whereMap = (HashMap) lst.get(0);
                observable.setTxtProductID(txtProductID.getText());
//                lblProductName.setText(CommonUtil.convertObjToStr(whereMap.get("PROD_DESC")));
                enableDisable(true);
            } else {
                ClientUtil.displayAlert("Invalid Product ID !!! ");
                txtProductID.setText("");
                observable.setTxtProductID("");
                enableDisable(false);
            }

        }
    }//GEN-LAST:event_txtProductIDFocusLost

    private void enableDisable(boolean enable) {
        ClientUtil.enableDisable(panCustID, enable, false, true);
        //   ClientUtil.enableDisable(panFromAccount, enable, false, true);
        //    ClientUtil.enableDisable(panToAccount, enable, false, true);
        //    ClientUtil.enableDisable(panTransType, enable, false, true);
        //    ClientUtil.enableDisable(panSINumber, enable, false, true);
        btnCalculate.setEnabled(enable);
        //    cboSIProductID.setEnabled(enable);
        //  chkExcludeLienDeposit.setEnabled(enable);
    }

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:.
        deleteScreenLock(getScreenID(), "EDIT");
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        observable.resetForm();
        enableDisable(false);
        ClientUtil.enableDisable(panDepositInterestApplication, true);
        ClientUtil.clearAll(this);
        btnProductID.setEnabled(true);
        txtProductID.setEnabled(true);
        btnCalculate.setEnabled(false);
        btnProcess.setEnabled(false);
        chkSelectAll.setEnabled(false);
//        lblProductName.setText("");
        lblCustName.setText("");
//        lblTotalTransactionAmtVal.setText("");//added by Ajay Sharma on 17-May-2014 as after clear transaction system is not clearing total amount.

        observable.removeTargetALLTransactionList();

        observable.setAccountsList(null);
        colorList = null;
        manualEntryEnable(false);
        deleteScreenLock(getScreenID(), "EDIT");
        btnNew.setVisible(false);
        btnNewActionPerformed(null);
        txtProductID.setEnabled(true);
        btnProductID.setEnabled(true);
        btnFromAccount.setEnabled(true);
        btnToAccount.setEnabled(true);
        observable.setFinalMap(null);
         rdoRenewalWithdrawingInt_No.setSelected(true);
        rdoRenewalWithdrawingInt_NoActionPerformed(null);
        cboCategory.setEnabled(true);
        cboCategory.setSelectedIndex(0);
        lblTotRecordval.setText("");
        lblTotIntVal.setText("");
        lblTotTDSVal.setText(""); // Added by nithya on 06-02-2020 for KD-1090
        //setModified(false);
        // chkExcludeLienDeposit.setEnabled(true);
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
        // TODO add your handling code here:
        btnProcess.setEnabled(false);
        //added by rishad 04/09/2015 for avoiding doubling issue
        CommonUtil comm = new CommonUtil();
        final JDialog loading = comm.addProgressBar();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws InterruptedException /** Execute some operation */
            {
                saveperformed();
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
    }//GEN-LAST:event_btnProcessActionPerformed
    private void saveperformed() {
        HashMap finalMap = observable.getFinalMap();
        String actNumList = "";
        String paramAct = "";
        boolean yesToContinue = true;
        for (int j = 0; j < tblDepositInterestApplication.getRowCount(); j++) { //added by shihad
            String acNo = CommonUtil.convertObjToStr(tblDepositInterestApplication.getValueAt(j, 1));
            boolean slect = ((Boolean) tblDepositInterestApplication.getValueAt(j, 0)).booleanValue();
            if (slect && finalMap.containsKey(acNo)) {
                actNumList += "'" + acNo + "_1',";
            }
        }
        if (actNumList != null && actNumList.length() > 0) {
            paramAct = actNumList.substring(0, actNumList.length() - 1);
            HashMap param = new HashMap();
            param.put("ACTSTRING", paramAct);
            List pendingList = ClientUtil.executeQuery("getPendingAuthTransactions", param);
            String pendAccounts = "";
            List pendAcctList = new ArrayList();
            if (pendingList != null && pendingList.size() > 0) {
                for (int i = 0; i < pendingList.size(); i++) {
                    HashMap linkMap = new HashMap();
                    linkMap = (HashMap) pendingList.get(i);
                    pendAcctList.add(CommonUtil.convertObjToStr(linkMap.get("LINK_BATCH_ID")));
                    String act = CommonUtil.convertObjToStr(linkMap.get("LINK_BATCH_ID")).substring(0, CommonUtil.convertObjToStr(linkMap.get("LINK_BATCH_ID")).length() - 2);
                    pendAccounts += act + "\n"; //Pending authorization of Interest payment\n
                }
                if (pendAcctList.size() > 0) {
                    for (int i = 0; i < pendAcctList.size(); i++) {
                        for (int j = 0; j < tblDepositInterestApplication.getRowCount(); j++) {
                            String acNo = CommonUtil.convertObjToStr(tblDepositInterestApplication.getValueAt(j, 1));
                            boolean slect = ((Boolean) tblDepositInterestApplication.getValueAt(j, 0)).booleanValue();
                            if (slect && CommonUtil.convertObjToStr(pendAcctList.get(i)).equals(acNo + "_1")) {
                                tblDepositInterestApplication.setValueAt(false, j, 0);
//                            finalMap.remove(acNo); 
                            }
                        }
                    }
                    if (pendAccounts.length() > 0) {
                        int yesNo = 0;
                        String[] options = {"Yes", "No"};
                        yesNo = COptionPane.showOptionDialog(null, "Transaction pending for authorization"
                                + "\n" + pendAccounts + "\nDo you want to Continue?", CommonConstants.WARNINGTITLE,
                                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                                null, options, options[0]);
                        if (yesNo == 0) {
                            System.out.println("yes");
                            yesToContinue = true;
                        } else {
                            System.out.println("no");
                            yesToContinue = false;
                            return;
                        }
                    }
                }// 
            }
        }
        if (yesToContinue) {
            for (int j = 0; j < tblDepositInterestApplication.getRowCount(); j++) {
                String acNo = CommonUtil.convertObjToStr(tblDepositInterestApplication.getValueAt(j, 1));
                boolean slect = ((Boolean) tblDepositInterestApplication.getValueAt(j, 0)).booleanValue();
                if (!slect && finalMap.containsKey(acNo)) {
                    finalMap.remove(acNo);
                }
            }
            HashMap term = new HashMap();
            term.put("finalMap", finalMap);
            observable.doAction(term);
            HashMap returnMap = observable.getProxyReturnMap();
            if (returnMap.containsKey("STATUS")) {
                String retStatus = CommonUtil.convertObjToStr(returnMap.get("STATUS"));
                if (retStatus != null && retStatus.equals("S")) {
                    String depositNo = "";
                    for (int j = 0; j < tblDepositInterestApplication.getRowCount(); j++) {
                        boolean slect = ((Boolean) tblDepositInterestApplication.getValueAt(j, 0)).booleanValue();
                        if (slect) {

                            if (j == 0) {
                                depositNo = depositNo + "'" + tblDepositInterestApplication.getValueAt(j, 1).toString() + "_1" + "'";
                            } else {
                                depositNo = depositNo + ",'" + tblDepositInterestApplication.getValueAt(j, 1).toString() + "_1" + "'";
                            }

                        }
                    }
                    if (depositNo != null && depositNo.charAt(0) == ',') {
                        depositNo = depositNo.substring(1, depositNo.length());
                    }
                    HashMap dMap = new HashMap();
                    //depositNo=depositNo.substring(0,depositNo.length()-1);
                    dMap.put("DEP_NO", depositNo);
                    dMap.put("TRANS_DT", currDate.clone());
                    String frmBatchId = "", toBatchId = "", singleTransId = "";
                    List dataList = ClientUtil.executeQuery("getAllMultileBatchIDForDeposits", dMap);
                    if (dataList.size() > 0) {
                        for (int i = 0; i < dataList.size(); i++) {
                            HashMap dataMap = (HashMap) dataList.get(i);
                            if (i == 0) {
                                frmBatchId = CommonUtil.convertObjToStr(dataMap.get("BATCH_ID"));
                            }
                            if (i == (dataList.size() - 1)) {
                                toBatchId = CommonUtil.convertObjToStr(dataMap.get("BATCH_ID"));
                            }
                            singleTransId = CommonUtil.convertObjToStr(dataMap.get("SINGLE_TRANS_ID"));
                        }

                    }

                    ClientUtil.showMessageWindow("Completed Succesfully. From Batch Id " + toBatchId + " To Batch Id " + frmBatchId);
                    //Print purpose
                    int yesNo = 0;
                    String[] options = {"Yes", "No"};
                    yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                            null, options, options[0]);
                    System.out.println("#$#$$ yesNo : " + yesNo);
                    //                            System.out.println("#$#$$ transList.size() : "+transList.size());
                    if (yesNo == 0) {
                        TTIntegration ttIntgration = null;
                        HashMap paramMap = null;
                        paramMap = new HashMap();
                        paramMap.put("TransId", singleTransId);
                        paramMap.put("TransDt", currDate);
                        paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                        ttIntgration.setParam(paramMap);
                        ttIntgration.integrationForPrint("MultipleDepositTransfer");
                        String installType = ((ComboBoxModel) cboRenewalInterestTransMode.getModel()).getKeyForSelected().toString();
                        if (rdoRenewalWithdrawingInt_Yes.isSelected() && installType != null && installType.equals("CASH")) {
                            ttIntgration.integrationForPrint("MultipleDepositCash");
                        }
                    }

                    //return;
                } else {
                    //  ClientUtil.showMessageWindow("Failure");
                    // return;
                }
            }
            btnClearActionPerformed(null);
            finalMap = new HashMap();
            TrueTransactMain.populateBranches();
            TrueTransactMain.selBranch = ProxyParameters.BRANCH_ID;
            observable.setSelectedBranchID(ProxyParameters.BRANCH_ID);
            setSelectedBranchID(ProxyParameters.BRANCH_ID);
        }
    }
    private void setColour() {
        /* Set a cellrenderer to this table in order format the date */
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (colorList.contains(String.valueOf(row))) {
                    setForeground(Color.RED);
//                    if(flagLien == 1)
//                    tblDepositInterestApplication.remove(row);

                } else {
                    setForeground(Color.BLACK);
                }

                // Set oquae
                this.setOpaque(true);
                return this;
            }
        };
        tblDepositInterestApplication.setDefaultRenderer(Object.class, renderer);
    }

    private void tblDepositInterestApplicationMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDepositInterestApplicationMouseClicked
        // TODO add your handling code here:

        List LienList = null;
        if (selectMode == true && tblDepositInterestApplication.getSelectedColumn() == 0) {
            String st = CommonUtil.convertObjToStr(tblDepositInterestApplication.getValueAt(tblDepositInterestApplication.getSelectedRow(), 0));

            if (st.equals("true")) {
                tblDepositInterestApplication.setValueAt(new Boolean(false), tblDepositInterestApplication.getSelectedRow(), 0);
            } else {
                tblDepositInterestApplication.setValueAt(new Boolean(true), tblDepositInterestApplication.getSelectedRow(), 0);
            }
            double totAmount = 0,totintamt=0, totTDSAmt = 0.0;
            int c=0;
            for (int i = 0; i < tblDepositInterestApplication.getRowCount(); i++) {
                st = CommonUtil.convertObjToStr(tblDepositInterestApplication.getValueAt(i, 0));
                if (st.equals("true")) {
                    totAmount = totAmount + CommonUtil.convertObjToDouble(tblDepositInterestApplication.getValueAt(i, 9)).doubleValue();
                    c++;
                    totintamt = totintamt + CommonUtil.convertObjToDouble(tblDepositInterestApplication.getValueAt(i, 7));
                    totTDSAmt = totTDSAmt + CommonUtil.convertObjToDouble(tblDepositInterestApplication.getValueAt(i, 15)); // Added by nithya on 06-02-2020 for KD-1090
                }
            }
            if (c > 0) {
                lblTotRecordval.setText(CommonUtil.convertObjToStr(c));
                lblTotIntVal.setText(CommonUtil.convertObjToStr(totintamt));
                lblTotTDSVal.setText(CommonUtil.convertObjToStr(totTDSAmt)); // Added by nithya on 06-02-2020 for KD-1090
            } else {
                lblTotRecordval.setText("");
                lblTotIntVal.setText("");
                lblTotTDSVal.setText(""); // Added by nithya on 06-02-2020 for KD-1090
            }
//            lblTotalTransactionAmtVal.setText(CurrencyValidation.formatCrore(String.valueOf(totAmount)));
        }
        //Added by sreekrishnan for death marking
        if(tblDepositInterestApplication.getRowCount()>0){
            if(observable.getFinalMap()!=null && observable.getFinalMap().size()>0){                
                HashMap dataMap = new HashMap();
                dataMap = (HashMap) observable.getFinalMap().get(tblDepositInterestApplication.getValueAt(tblDepositInterestApplication.getSelectedRow(), 1));
                HashMap renMap = (HashMap) dataMap.get("RENEWALMAP");                
                if(renMap!=null && renMap.size()>0 && renMap.containsKey("CUST_STATUS") && renMap.get("CUST_STATUS").equals("DEAD")){
                    tblDepositInterestApplication.setValueAt(new Boolean(false), tblDepositInterestApplication.getSelectedRow(), 0);
                     ClientUtil.showMessageWindow("Deposit Customer is Death Marked,Please contact the Administrator!!!");
                } 
            }
        }
        if (evt.getClickCount() == 2) {
            if (returnMap != null) {
                if (returnMap.containsKey(tblDepositInterestApplication.getValueAt(
                        tblDepositInterestApplication.getSelectedRow(), 1))) {
                    TTException exception = (TTException) returnMap.get(tblDepositInterestApplication.getValueAt(
                            tblDepositInterestApplication.getSelectedRow(), 1));
                    parseException.logException(exception, true);

                }
            }
            //Added By Suresh
            if ((tblDepositInterestApplication.getSelectedColumn() == 9) && ((Boolean) tblDepositInterestApplication.getValueAt(tblDepositInterestApplication.getSelectedRow(), 0)).booleanValue()) {
                if (CommonUtil.convertObjToDouble(tblDepositInterestApplication.getValueAt(tblDepositInterestApplication.getSelectedRow(), 9).toString()).doubleValue() > 0) {
                }
            }
            String lienAccno = CommonUtil.convertObjToStr(tblDepositInterestApplication.getValueAt(tblDepositInterestApplication.getSelectedRow(), 2));
            HashMap hmap = new HashMap();
            hmap.put("DEPOSIT_NO", lienAccno);
            LienList = ClientUtil.executeQuery("getLienAccNoForDispiaying", hmap);
            if (LienList != null && LienList.size() > 0) {
                ArrayList displayList = new ArrayList();
                for (int i = 0; i < LienList.size(); i++) {
                    hmap = (HashMap) LienList.get(i);
                    displayList.add(hmap);
                }
//                new LienDetailsUI(displayList).show();
            }
            HashMap reportParamMap = new HashMap();
            // TODO add your handling code here:
            String actNum = (String) tblDepositInterestApplication.getValueAt(tblDepositInterestApplication.getSelectedRow(), 2);
//            System.out.println("actNum"+actNum);
//            reportParamMap.put("AccountNo", actNum);
//             com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
            com.see.truetransact.clientutil.ttrintegration.TTIntegration ttIntgration = null;
            HashMap prodMap = new HashMap();

            HashMap paramMap = new HashMap();
            paramMap.put("AccountNo", actNum);
            paramMap.put("FromDt", currDate.clone());
            paramMap.put("ToDt", currDate.clone());
            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
            ttIntgration.setParam(paramMap);
            if (actNum.lastIndexOf('_') != -1) {
                actNum = actNum.substring(0, actNum.lastIndexOf("_"));
            }
            prodMap.put("ACT_NUM", actNum);
            List lst = ClientUtil.executeQuery("getBehavesLikeForDepositNo", prodMap);
            if (lst != null && lst.size() > 0) {
                prodMap = (HashMap) lst.get(0);
                if (prodMap.get("BEHAVES_LIKE").equals("DAILY")) {
                    ttIntgration.integration("DailyLedger");
                } else {
                    ttIntgration.integration("TDLedger");
                }
            }
        }



    }//GEN-LAST:event_tblDepositInterestApplicationMouseClicked
    //Added By Suresh

    public void enteredAmount() {
        double intAmount = CommonUtil.convertObjToDouble(tblDepositInterestApplication.getValueAt(tblDepositInterestApplication.getSelectedRow(), 9).toString()).doubleValue();
        String intPayAcno = CommonUtil.convertObjToStr(tblDepositInterestApplication.getValueAt(tblDepositInterestApplication.getSelectedRow(), 10));
        double clearBal = 0.0;
        ArrayList list = observable.getAccountsList();
        HashMap rdListMap = new HashMap();
        rdListMap = (HashMap) list.get(tblDepositInterestApplication.getSelectedRow());
        String type = CommonUtil.convertObjToStr(rdListMap.get("ACCT_TYPE"));
        double amount = CommonUtil.convertObjToDouble(rdListMap.get("AMOUNT")).doubleValue();
        rdListMap.put("ACT_NUM", intPayAcno);
        List loanList = ClientUtil.executeQuery("LoneFacilityDetailAD", rdListMap);
        if (loanList != null && loanList.size() > 0) {
            rdListMap = (HashMap) loanList.get(0);
            clearBal = CommonUtil.convertObjToDouble(rdListMap.get("CLEAR_BALANCE")).doubleValue();
        }
        HashMap amountMap = new HashMap();
        amountMap.put("TITLE", "Interest Amount");
        if (type.equals("LOANS_AGAINST_DEPOSITS")) {

            amountMap.put("TOTAL_DEMAND", new Double(amount));
            amountMap.put("CLEAR_BALANCE", new Double(clearBal));
        }
        amountMap.put("TOLERANCE_AMT", CommonConstants.TOLERANCE_AMT);
        amountMap.put("SELECTED_AMT", String.valueOf(intAmount));
        amountMap.put("CALCULATED_AMT", String.valueOf(intAmount));
        TextUI textUI = new TextUI(this, this, amountMap);
    }

    public void modifyTransData(Object obj) {
        TextUI objTextUI = (TextUI) obj;
        String selectedDepNo = "";
        String enteredData = objTextUI.getTxtData();
        double intAmt = CommonUtil.convertObjToDouble(enteredData).doubleValue();
        selectedDepNo = CommonUtil.convertObjToStr(tblDepositInterestApplication.getValueAt(tblDepositInterestApplication.getSelectedRow(), 2));
        //        System.out.println("############## enteredData  : "+Double.parseDouble(enteredData));
        //        System.out.println("##### TextUI Data : "+enteredData);
        //        System.out.println("##### Before intList : "+observable.getFinalList().size());
        //        System.out.println("##### Before intList : "+observable.getFinalList());
        if (observable.getFinalList() != null && observable.getFinalList().size() > 0) {
            List interestList = null;
            for (int i = 0; i < observable.getFinalList().size(); i++) {
                String depNo = "";
                String siNo = "";
                interestList = new ArrayList();
                interestList = (ArrayList) observable.getFinalList().get(i);
                depNo = CommonUtil.convertObjToStr(interestList.get(2));
                siNo = CommonUtil.convertObjToStr(interestList.get(10));
                if (selectedDepNo.equals(depNo)) {
                    interestList.set(9, new Double(intAmt));
                }
                if (siNo == null || siNo.length() <= 0) {
                    siNo = "";
                    interestList.set(10, siNo);
                }
            }
        }
        ArrayList list = observable.getAccountsList();
        //        System.out.println("f##### After intList : "+observable.getFinalList());
        observable.updateInterestData();
        tblDepositInterestApplication.setModel(observable.getTblDepositInterestApplication());
        setSizeTableData();
    }

    private void btnCalculateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalculateActionPerformed
        // TODO add your handling code here:
        ClientUtil.enableDisable(panDepositInterestApplication, false);
        btnCalculateActionPerformed();
    }//GEN-LAST:event_btnCalculateActionPerformed

    private void btnCalculateActionPerformed() {
        String actNumStr = "";
        String custIDStr = "";
        String dispString = "";
        if (txtRenewalPeriodOfDeposit_Years.getText().length() == 0 && txtRenewalPeriodOfDeposit_Months.getText().length() == 0
                && txtRenewalPeriodOfDeposit_Days.getText().length() == 0) {
            ClientUtil.showAlertWindow("Period Should Not be empty.\n");
            return;
        }
        //added by rishad for 0010776: Deposit Renewal issue
        String renewalIntMode = ((ComboBoxModel) cboRenewalInterestTransMode.getModel()).getKeyForSelected().toString();
        String renewalIntProdType = ((ComboBoxModel) cboRenewalInterestTransProdType.getModel()).getKeyForSelected().toString();
        String renewalIntProdId = "";
        if (renewalIntProdType != null && !renewalIntProdType.equals("") && !renewalIntProdType.equals("GL")) {
            renewalIntProdId = ((ComboBoxModel) cboRenewalInterestTransProdId.getModel()).getKeyForSelected().toString();
        }
        if (rdoRenewalWithdrawingInt_Yes.isSelected() == true && renewalIntMode.equals("")) {
            ClientUtil.showAlertWindow("Select the mode of withdrawal");
            return;
        } else if (rdoRenewalWithdrawingInt_Yes.isSelected() == true && renewalIntMode.equals("TRANSFER") && renewalIntProdType.equals("")) {
            ClientUtil.showAlertWindow("Select ProductType...");
            return;
        } else if (rdoRenewalWithdrawingInt_Yes.isSelected() == true && renewalIntMode.equals("TRANSFER") && !renewalIntProdType.equals("GL") && renewalIntProdId.equals("")) {
            ClientUtil.showAlertWindow("Select ProductId...");
            return;
        } else if (rdoRenewalWithdrawingInt_Yes.isSelected() == true && renewalIntMode.equals("TRANSFER") && txtRenewalIntCustomerIdCr.getText().length() == 0) {
            ClientUtil.showAlertWindow("Select Account Number...");
            return;
        }
        if (txtProductID.getText().length() > 0) {
            int opt = 0;
            if (txtFromAccount.getText().length() == 0 && txtToAccount.getText().length() >= 0) {
                actNumStr = "From Account No not selected.\n";
                ClientUtil.displayAlert("Enter From AccountNo");
                return;
            }
        }
        displayInterestDetails();
        chkSelectAll.setEnabled(true);
    }

    private void displayInterestDetails() {
//        btnRePrint.setEnabled(false);
        selectMode = true;
        observable.setTxtProductID(txtProductID.getText());
        tblDepositInterestApplication.setEnabled(true);
        btnProductID.setEnabled(false);
        btnProcess.setEnabled(true);
        txtProductID.setEnabled(false);
        HashMap dataMap = new HashMap();
        if (txtCustID.getText() != null && (!txtCustID.getText().equals(""))) {
            dataMap.put("CUST_ID", CommonUtil.convertObjToStr(txtCustID.getText()));
        }

        if (txtFromAccount.getText() != null && (!txtFromAccount.getText().equals(""))) {
            dataMap.put("ACT_FROM", CommonUtil.convertObjToStr(txtFromAccount.getText()));
        }
        if (txtToAccount.getText() != null && (!txtToAccount.getText().equals(""))) {
            dataMap.put("ACT_TO", CommonUtil.convertObjToStr(txtToAccount.getText()));
        }

        Date tempDt = (Date) currDate.clone();
        dataMap.put("CURR_DT", tempDt);
        dataMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.selBranch);
        if (rdoRenewalWithdrawingInt_Yes.isSelected()) {
            dataMap.put("INT_WITHDRAWING", "INT_WITHDRAWING");
            String installType = ((ComboBoxModel) cboRenewalInterestTransMode.getModel()).getKeyForSelected().toString();
            dataMap.put("INT_MODE", installType);
            if (installType.equals("TRANSFER")) {
                String prodType = ((ComboBoxModel) cboRenewalInterestTransProdType.getModel()).getKeyForSelected().toString();
                dataMap.put("INT_PROD_TYPE", prodType);
                String prodId = "";
                if (((ComboBoxModel) cboRenewalInterestTransProdId.getModel()).getKeyForSelected() != null) {
                    prodId = ((ComboBoxModel) cboRenewalInterestTransProdId.getModel()).getKeyForSelected().toString();
                }
                dataMap.put("INT_PROD_ID", prodId);
                dataMap.put("INT_AC_NO", txtRenewalIntCustomerIdCr.getText());
            }
        }
        if (txtRenewalPeriodOfDeposit_Years.getText().length() > 0) {
            dataMap.put("PERIOD_YEARS", txtRenewalPeriodOfDeposit_Years.getText());
        }
        if (txtRenewalPeriodOfDeposit_Months.getText().length() > 0) {
            dataMap.put("PERIOD_MONTHS", txtRenewalPeriodOfDeposit_Months.getText());
        }
        if (txtRenewalPeriodOfDeposit_Days.getText().length() > 0) {
            dataMap.put("PERIOD_DAYS", txtRenewalPeriodOfDeposit_Days.getText());
        }
        observable.setCboCategory(CommonUtil.convertObjToStr(cboCategory.getSelectedItem()));
        String category = "";
        if (cboCategory.getSelectedItem() != null && !cboCategory.getSelectedItem().equals("")) {
            category = ((ComboBoxModel) cboCategory.getModel()).getKeyForSelected().toString();
            dataMap.put("CATEGORY", category);
        }
        observable.insertTableData(dataMap);
        tblDepositInterestApplication.setModel(observable.getTblDepositInterestApplication());
        List tabList = observable.getFinalList();
        setSizeTableData();
        if (tblDepositInterestApplication.getRowCount() == 0) {
            ClientUtil.showMessageWindow(" No Data !!! ");
            btnProcess.setEnabled(false);
        } else {
            enableDisable(false);
        }

    }
    //Added By Suresh
    private void setSizeTableData() {
        javax.swing.table.TableColumn col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(0));
        col.setMaxWidth(40);
        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(1));
        col.setMaxWidth(180);
        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(2));
        col.setMaxWidth(120);
        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(3));
        col.setMaxWidth(150);
        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(4));
        col.setMaxWidth(65);
        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(5));
        col.setMaxWidth(65);
        col = tblDepositInterestApplication.getColumn(tblDepositInterestApplication.getColumnName(6));
        col.setMaxWidth(65);

    }

    private void btnProductIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductIDActionPerformed
        // TODO add your handling code here:
        /*if (validateScreenLock(getScreenID())) {
        System.out.println("inside validate");
        return;
        }
        insertScreenLock(getScreenID(), "EDIT");*/
        callView("PROD_DETAILS");
        if (txtProductID.getText().length() > 0) {
            enableDisable(true);
//            chkSelectMaturity.setEnabled(true);
            //     chkSelectMaturity.setSelected(true);
        }
    }//GEN-LAST:event_btnProductIDActionPerformed

private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
    if (getScreenID() != null && !getScreenID().equals("") && getScreenID().length() > 0) {
        deleteScreenLock(getScreenID(), "EDIT");
    }
    this.dispose();
}//GEN-LAST:event_formInternalFrameClosing

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        panProductIdMain.setEnabled(true);
        panFromTonum.setEnabled(true);
        cPanel1.setEnabled(true);
        txtProductID.setEnabled(true);
        btnProductID.setEnabled(true);
        btnFromAccount.setEnabled(true);
        btnToAccount.setEnabled(true);
        btnClear.setEnabled(true);
        btnCustID.setEnabled(true);
        txtCustID.setEnabled(true);
        txtRenewalPeriodOfDeposit_Years.setEnabled(true);
        txtRenewalPeriodOfDeposit_Months.setEnabled(true);
        txtRenewalPeriodOfDeposit_Days.setEnabled(true);

        // txtToAccount.setEnabled(true);
        // btnToAccount.setEnabled(true);
        rdoRenewalWithdrawingInt_Yes.setEnabled(true);
        rdoRenewalWithdrawingInt_No.setEnabled(true);
        cboRenewalInterestTransMode.setEnabled(true);
        cboRenewalInterestTransProdType.setEnabled(true);
        txtRenewalIntCustomerIdCr.setEnabled(true);
        btnCalculate.setEnabled(true);
        btnProcess.setEnabled(true);
cboCategory.setEnabled(true);
    }//GEN-LAST:event_btnNewActionPerformed

private void cboRenewalInterestTransModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboRenewalInterestTransModeActionPerformed
    // TODO add your handling code here:
    //  cboCreditRenewalIntDetails();
    String installType = ((ComboBoxModel) cboRenewalInterestTransMode.getModel()).getKeyForSelected().toString();
    if (installType.equals("TRANSFER")) {
        cboRenewalInterestTransProdType.setEnabled(true);
        cboRenewalInterestTransProdId.setEnabled(true);
        txtRenewalIntCustomerIdCr.setEnabled(true);
    } else {
        cboRenewalInterestTransProdType.setSelectedIndex(0);
        cboRenewalInterestTransProdId.setSelectedIndex(0);
        txtRenewalIntCustomerIdCr.setText("");
        cboRenewalInterestTransProdType.setEnabled(false);
        cboRenewalInterestTransProdId.setEnabled(false);
        txtRenewalIntCustomerIdCr.setEnabled(false);
    }
}//GEN-LAST:event_cboRenewalInterestTransModeActionPerformed

private void cboRenewalInterestTransModeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboRenewalInterestTransModeFocusLost
    // TODO add your handling code here:
    //Added BY Suresh
    //setRenewalDetails();
}//GEN-LAST:event_cboRenewalInterestTransModeFocusLost

private void cboRenewalInterestTransProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboRenewalInterestTransProdTypeActionPerformed
    // TODO add your handling code here:
    if (cboRenewalInterestTransProdType.getSelectedIndex() > 0) {

        String prodType = ((ComboBoxModel) cboRenewalInterestTransProdType.getModel()).getKeyForSelected().toString();
        // String prodType = ((ComboBoxModel) cboRenewalInterestTransProdType.getModel()).getKeyForSelected().toString();
        observable.setCbmRenewalInterestTransProdId(prodType);
        if (prodType.equals("GL")) {
            cboRenewalInterestTransProdId.setSelectedItem("");
            cboRenewalInterestTransProdId.setEnabled(false);
            txtRenewalIntCustomerIdCr.setText("");
            lblRenewalInterestTransAccNo.setText("Account Head Id");
            //  lblRenewalInterestCustName.setText("A/c Hd Description");
            //   lblRenewalInterestCustNameVal.setText("");
            btnRenewalIntCustomerIdFileOpenCr.setEnabled(true);
        } else if (prodType.equals("TD") || prodType.equals("TL") || prodType.equals("AD") || prodType.equals("RM")) {
            ClientUtil.showAlertWindow("Not Possible to credit");
            cboRenewalInterestTransProdId.setSelectedItem("");
            cboRenewalInterestTransProdId.setEnabled(false);
            txtRenewalIntCustomerIdCr.setText("");
            // lblRenewalInterestCustNameVal.setText("");
            btnRenewalIntCustomerIdFileOpenCr.setEnabled(false);
            return;
        } else {
            cboRenewalInterestTransProdId.setSelectedItem("");
            cboRenewalInterestTransProdId.setEnabled(true);
            //  lblAccountNo.setText("Account No");
            //  lblRenewalInterestCustName.setText("Customer Name");
            btnRenewalIntCustomerIdFileOpenCr.setEnabled(true);
           // txtRenewalIntCustomerIdCr.setEnabled(false);
            txtRenewalIntCustomerIdCr.setText("");
            // lblRenewalInterestCustNameVal.setText("");
        }
        //            if(!prodType.equals("RM")){
        cboRenewalInterestTransProdId.setModel(observable.getCbmRenewalInterestTransProdId());
        if (!prodType.equals("GL") && cboRenewalInterestTransProdId.getSelectedItem().equals("")) {
            //    cboRenewalInterestTransProdId.setSelectedItem(observable.getCboRenewalInterestTransProdId());
        }
        //            }else
        //                cboRenewalInterestTransProdId.setSelectedItem("PAY ORDR");
    }
}//GEN-LAST:event_cboRenewalInterestTransProdTypeActionPerformed

private void cboRenewalInterestTransProdIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboRenewalInterestTransProdIdActionPerformed
    // TODO add your handling code here:
    txtRenewalIntCustomerIdCr.setText("");

}//GEN-LAST:event_cboRenewalInterestTransProdIdActionPerformed

private void btnRenewalIntCustomerIdFileOpenCrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRenewalIntCustomerIdFileOpenCrActionPerformed
    // TODO add your handling code here:
    if (cboRenewalInterestTransProdType.getSelectedIndex() > 0) {
        callView("RENEWAL_INT_TRANS_ACC_NO");
    } else {
        ClientUtil.showAlertWindow("Product Type should not be empty...");
        return;
    }

}//GEN-LAST:event_btnRenewalIntCustomerIdFileOpenCrActionPerformed

private void rdoRenewalWithdrawingInt_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoRenewalWithdrawingInt_YesActionPerformed
    // TODO add your handling code here:
    rdoRenewalWithdrawingInt_No.setSelected(false);
    cboRenewalInterestTransMode.setSelectedIndex(0);
    cboRenewalInterestTransMode.setEnabled(true);
    cboRenewalInterestTransMode.setSelectedIndex(0);
    cboRenewalInterestTransMode.setEnabled(true);
    cboRenewalInterestTransProdId.setSelectedIndex(0);
    cboRenewalInterestTransProdId.setEnabled(true);
    txtRenewalIntCustomerIdCr.setText("");
//txtRenewalIntCustomerIdCr.setEnabled(true);
    btnRenewalIntCustomerIdFileOpenCr.setEnabled(true);
}//GEN-LAST:event_rdoRenewalWithdrawingInt_YesActionPerformed

private void rdoRenewalWithdrawingInt_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoRenewalWithdrawingInt_NoActionPerformed
    // TODO add your handling code here:
    rdoRenewalWithdrawingInt_Yes.setSelected(false);
    cboRenewalInterestTransMode.setSelectedIndex(0);
    cboRenewalInterestTransMode.setEnabled(false);
    cboRenewalInterestTransMode.setSelectedIndex(0);
    cboRenewalInterestTransMode.setEnabled(false);
    cboRenewalInterestTransProdId.setSelectedIndex(0);
    cboRenewalInterestTransProdId.setEnabled(false);
    txtRenewalIntCustomerIdCr.setText("");
    txtRenewalIntCustomerIdCr.setEnabled(false);
    btnRenewalIntCustomerIdFileOpenCr.setEnabled(false);
}//GEN-LAST:event_rdoRenewalWithdrawingInt_NoActionPerformed

private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
    // TODO add your handling code here:
    boolean flag;
    if (chkSelectAll.isSelected() == true) {
        flag = true;
    } else {
        flag = false;
    }
    double totAmount = 0,totIntAmount=0, totTDSAmount = 0.0;;
    int c=0;
    for (int i = 0; i < tblDepositInterestApplication.getRowCount(); i++) {
        tblDepositInterestApplication.setValueAt(new Boolean(flag), i, 0);
        if(tblDepositInterestApplication.getRowCount()>0){
            if(observable.getFinalMap()!=null && observable.getFinalMap().size()>0){                
                HashMap dataMap = new HashMap();
                dataMap = (HashMap) observable.getFinalMap().get(tblDepositInterestApplication.getValueAt(i, 1));
                HashMap renMap = (HashMap) dataMap.get("RENEWALMAP");                
                if(renMap!=null && renMap.size()>0 && renMap.containsKey("CUST_STATUS") && renMap.get("CUST_STATUS").equals("DEAD")){
                    tblDepositInterestApplication.setValueAt(new Boolean(false), i, 0);
                     //ClientUtil.showMessageWindow("Deposit Customer is Death Marked,Please contact the Administrator!!!");
                }else{
                    c++; 
                } 
            }
        }
        if(flag){
          //c++;  
          totIntAmount =  totIntAmount + CommonUtil.convertObjToDouble(tblDepositInterestApplication.getValueAt(i, 7));
          totTDSAmount = totTDSAmount + CommonUtil.convertObjToDouble(tblDepositInterestApplication.getValueAt(i, 15)); // Added by nithya on 06-02-2020 for KD-1090
        }
      //  totAmount = totAmount + CommonUtil.convertObjToDouble(tblDepositInterestApplication.getValueAt(i, 9)).doubleValue();
    }
   if (chkSelectAll.isSelected() == true) {
    lblTotRecordval.setText(CommonUtil.convertObjToStr(c));
    lblTotIntVal.setText(CommonUtil.convertObjToStr(totIntAmount));
    lblTotTDSVal.setText(CommonUtil.convertObjToStr(totTDSAmount)); // Added by nithya on 06-02-2020 for KD-1090
   }
   else{
     lblTotRecordval.setText("");
     lblTotIntVal.setText("");
     lblTotTDSVal.setText(""); // Added by nithya on 06-02-2020 for KD-1090
   }
  //  lblTotalTransactionAmtVal.setText(CurrencyValidation.formatCrore(String.valueOf(totAmount)));
}//GEN-LAST:event_chkSelectAllActionPerformed

private void cboCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCategoryActionPerformed
   
}//GEN-LAST:event_cboCategoryActionPerformed

private void cboCategoryFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboCategoryFocusLost
    // TODO add your handling code here:
  
}//GEN-LAST:event_cboCategoryFocusLost

    private void txtFromAccountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFromAccountFocusLost
        // TODO add your handling code here:
        if (txtProductID.getText().length() > 0) {
            HashMap hash = new HashMap();
            hash.put("PROD_ID", CommonUtil.convertObjToStr(txtProductID.getText()));
            hash.put("CURR_DATE", getProperDate((Date)currDate.clone()));
            hash.put("ACCT_NUM", CommonUtil.convertObjToStr(txtFromAccount.getText()));
            hash.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(com.see.truetransact.ui.TrueTransactMain.selBranch));
            List lst = ClientUtil.executeQuery("viewAllRenewalAccInfoMultiple", hash);
            if (lst != null && lst.size() <= 0) {
                ClientUtil.displayAlert("Invalid Account Number");
                txtFromAccount.setText("");
            }
        } else {
            ClientUtil.displayAlert("Invalid Product ID");
            txtToAccount.setText("");
            txtFromAccount.setText("");
        }
    }//GEN-LAST:event_txtFromAccountFocusLost

    private void txtToAccountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToAccountFocusLost
        // TODO add your handling code here:
        if (txtProductID.getText().length() > 0) {
            HashMap hash = new HashMap();
            hash.put("PROD_ID", CommonUtil.convertObjToStr(txtProductID.getText()));
            hash.put("CURR_DATE", getProperDate((Date)currDate.clone()));
            hash.put("ACCT_NUM", CommonUtil.convertObjToStr(txtToAccount.getText()));
            hash.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(com.see.truetransact.ui.TrueTransactMain.selBranch));
            List lst = ClientUtil.executeQuery("viewAllRenewalAccInfoMultiple", hash);
            if (lst != null && lst.size() <= 0) {
                ClientUtil.displayAlert("Invalid Account Number");
                txtToAccount.setText("");
            }//Added by sreekrishnan
            else{
                if(txtFromAccount.getText()!=null && txtFromAccount.getText().length()>0){
                    if(txtToAccount.getText()!=null && txtToAccount.getText().length()>0){
                        String category = observable.getDepositCategoryForCustomer(txtFromAccount.getText(),txtToAccount.getText());
                        if(category!=null && category.length()>0){
                            cboCategory.setSelectedItem(observable.getCbmCategory().getDataForKey(category));
                        }
                    }
                }
            }
            
        } else {
            ClientUtil.displayAlert("Invalid Product ID");
            txtToAccount.setText("");
            txtFromAccount.setText("");
        }
    }//GEN-LAST:event_txtToAccountFocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnCalculate;
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCustID;
    private com.see.truetransact.uicomponent.CButton btnFromAccount;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnProcess;
    private com.see.truetransact.uicomponent.CButton btnProductID;
    private com.see.truetransact.uicomponent.CButton btnRenewalIntCustomerIdFileOpenCr;
    private com.see.truetransact.uicomponent.CButton btnToAccount;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CLabel cLabel2;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CComboBox cboCategory;
    private com.see.truetransact.uicomponent.CComboBox cboRenewalInterestTransMode;
    private com.see.truetransact.uicomponent.CComboBox cboRenewalInterestTransProdId;
    private com.see.truetransact.uicomponent.CComboBox cboRenewalInterestTransProdType;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblCategory;
    private com.see.truetransact.uicomponent.CLabel lblCustID;
    private com.see.truetransact.uicomponent.CLabel lblCustName;
    private com.see.truetransact.uicomponent.CLabel lblFromAccount;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblProductID;
    private com.see.truetransact.uicomponent.CLabel lblRenewalInterestTransAccNo;
    private com.see.truetransact.uicomponent.CLabel lblRenewalInterestTransMode;
    private com.see.truetransact.uicomponent.CLabel lblRenewalInterestTransProdId;
    private com.see.truetransact.uicomponent.CLabel lblRenewalInterestTransProdType;
    private com.see.truetransact.uicomponent.CLabel lblRenewalPeriod_Days;
    private com.see.truetransact.uicomponent.CLabel lblRenewalPeriod_Months;
    private com.see.truetransact.uicomponent.CLabel lblRenewalPeriod_Years;
    private com.see.truetransact.uicomponent.CLabel lblRenewalWithdrawingIntAmt;
    private com.see.truetransact.uicomponent.CLabel lblSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblToAccount;
    private com.see.truetransact.uicomponent.CLabel lblTotIntVal;
    private com.see.truetransact.uicomponent.CLabel lblTotInterest;
    private com.see.truetransact.uicomponent.CLabel lblTotRecord;
    private com.see.truetransact.uicomponent.CLabel lblTotRecordval;
    private com.see.truetransact.uicomponent.CLabel lblTotTDSVal;
    private com.see.truetransact.uicomponent.CPanel panCustID;
    private com.see.truetransact.uicomponent.CPanel panDepositInterestApplication;
    private com.see.truetransact.uicomponent.CPanel panFromTonum;
    private com.see.truetransact.uicomponent.CPanel panNewPeriodOfDeposit;
    private com.see.truetransact.uicomponent.CPanel panProcess;
    private com.see.truetransact.uicomponent.CPanel panProcessButton;
    private com.see.truetransact.uicomponent.CPanel panProductDetails;
    private com.see.truetransact.uicomponent.CPanel panProductID;
    private com.see.truetransact.uicomponent.CPanel panProductIdMain;
    private com.see.truetransact.uicomponent.CPanel panProductTableData;
    private com.see.truetransact.uicomponent.CPanel panSelectAll;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panToAccount2;
    private com.see.truetransact.uicomponent.CRadioButton rdoRenewalWithdrawingInt_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoRenewalWithdrawingInt_Yes;
    private com.see.truetransact.uicomponent.CScrollPane srpDepositInterestApplication;
    private com.see.truetransact.uicomponent.CTable tblDepositInterestApplication;
    private com.see.truetransact.uicomponent.CTextField txtCustID;
    private com.see.truetransact.uicomponent.CTextField txtFromAccount;
    private com.see.truetransact.uicomponent.CTextField txtProductID;
    private com.see.truetransact.uicomponent.CTextField txtRenewalIntCustomerIdCr;
    private com.see.truetransact.uicomponent.CTextField txtRenewalPeriodOfDeposit_Days;
    private com.see.truetransact.uicomponent.CTextField txtRenewalPeriodOfDeposit_Months;
    private com.see.truetransact.uicomponent.CTextField txtRenewalPeriodOfDeposit_Years;
    private com.see.truetransact.uicomponent.CTextField txtToAccount;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] args) {
        DepositMultiRenewal fad = new DepositMultiRenewal();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(fad);
        j.show();
        fad.show();
    }
}
