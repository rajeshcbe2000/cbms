/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ChargesServiceTaxUI.java
 *
 * Created on August 6, 2003, 10:53 AM
 */

package com.see.truetransact.ui.transaction.chargesServiceTax;

import java.util.HashMap;
import java.util.Observable;
import org.apache.log4j.Logger;
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
import java.util.List;
import java.util.LinkedHashMap;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.ui.transaction.common.TransDetailsUI;
import com.see.truetransact.ui.common.authorize.AuthorizeUI ;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.transferobject.product.operativeacct.OperativeAcctProductTO;
import com.see.truetransact.ui.product.operativeacct.OperativeAcctProductOB;
import com.see.truetransact.ui.transaction.chargesServiceTax.ChargesServiceTaxOB;
import com.see.truetransact.transferobject.operativeaccount.AccountClosingTO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.commonutil.DateUtil;
import java.util.Date;

/** This form is used to manipulate chargesServiceTax related functionality
 * @author Sathiya added transaction UI
 */
public class ChargesServiceTaxUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField{
    final int AUTHORIZE=3, CANCEL=0 ;
    int viewType=-1;
    private HashMap mandatoryMap;
    private ChargesServiceTaxOB observable;
    private final static Logger log = Logger.getLogger(ChargesServiceTaxUI.class);
    private TransactionUI transactionUI = new TransactionUI();
    private TransDetailsUI transDetailsUI = null;
    ChargesServiceTaxRB chargesServiceTaxRB = new ChargesServiceTaxRB();
    private String prodType="";
    private LinkedHashMap transactionDetailsTO = null;
    private String transId = "";
    private Date currDt = null;
    /** Creates new form ChargesServiceTaxUI */
    public ChargesServiceTaxUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initStartUp();
        transactionUI.setSourceScreen("CHARGES_SERVICETAX");
        transactionUI.addToScreen(panTransaction);
        transactionUI.setTransactionMode(CommonConstants.DEBIT) ;
        transDetailsUI =  new TransDetailsUI(panAccountHead);
        observable.setTransactionOB(transactionUI.getTransactionOB());
    }
    
    private void initStartUp(){
        setMandatoryHashMap();
        setFieldNames();
        internationalize();        
        observable = new ChargesServiceTaxOB();
        observable.setProdType(prodType);
        observable.addObserver(this);
        update(observable, null);
        initComponentData();
        enableDisable(false);
        setButtonEnableDisable();
        setMaxLength();
        btnAccountHead.setEnabled(false);
        panAcctNum.setVisible(false);
        lblAccountNumber.setVisible(false);
        lblCustomerName.setVisible(false);
        lblCustomerNameDisplay.setVisible(false);
        btnException.setVisible(false);
    }
    
    private void setMaxLength() {
        txtAccountHead.setMaxLength(22);
        txtAccountNumber.setMaxLength(16);
        txtAccountNumber.setAllowAll(true);
        txtAmount.setMaxLength(16);
        txtAmount.setValidation(new CurrencyValidation(14,2));
        txtService_tax_amt.setMaxLength(16);
        txtService_tax_amt.setValidation(new CurrencyValidation(14,2));
        txtTotal_amt.setMaxLength(16);
        txtTotal_amt.setValidation(new CurrencyValidation(14,2));
        txtChargeDetails.setMaxLength(64);
        txtChargeDetails.setAllowAll(true);
    }
    
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtAmount", new Boolean(true));
        mandatoryMap.put("txtAccountHead", new Boolean(true));
        mandatoryMap.put("txtChargeDetails", new Boolean(true));
    }
    
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    private void updateAuthorizeStatus(String authorizeStatus) {
        if (viewType != AUTHORIZE){
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            mapParam.put(CommonConstants.MAP_NAME, "getSelectChargesServiceAuthorizeTOList");
            viewType = AUTHORIZE;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            //            isFilled = false;
            authorizeUI.show();
            btnSave.setEnabled(false);
            observable.setStatus();
        } else if (viewType == AUTHORIZE){
            HashMap authDataMap = new HashMap();
            if(!transId.equals("") && transId.length()>0){
                authDataMap.put("TRANS_ID", transId);
                authDataMap.put("USER_ID",ProxyParameters.USER_ID);
                authDataMap.put("TRANS_DT", currDt.clone());
                authDataMap.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
                List lst=ClientUtil.executeQuery("selectauthorizationLock", authDataMap);
                if(lst !=null && lst.size()>0){
                    HashMap map=new HashMap();
                    StringBuffer open=new StringBuffer();
                    for(int i=0;i<lst.size();i++){
                        map=(HashMap)lst.get(i);
                        open.append ("\n"+"User Id  :"+" "+CommonUtil.convertObjToStr(map.get("OPEN_BY"))+"\n");
                        open.append("Mode Of Operation  :" +" "+CommonUtil.convertObjToStr(map.get("MODE_OF_OPERATION"))+"\n");
                        open.append("Please Cancel this transaction...");                
                    }
                    ClientUtil.showMessageWindow("already open by"+open);           
                    return;
                }
            }
            ArrayList arrList = new ArrayList();
            HashMap authorizeMap = new HashMap();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            arrList.add(singleAuthorizeMap);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(authorizeMap);
            super.setOpenForEditBy(observable.getStatusBy());
            btnCancelActionPerformed(null);
            lblStatus.setText(authorizeStatus);
        }
    }
    
    public void authorize(HashMap map) {
        System.out.println("Authorize Map : " + map);
        
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.setResult(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setAuthorizeMap(map);
            observable.doAction();
            observable.setAuthorizeMap(null);
            observable.setResultStatus();
            
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

        rdoTransactionType = new com.see.truetransact.uicomponent.CButtonGroup();
        panAccountInfo = new com.see.truetransact.uicomponent.CPanel();
        panAccountInfoInner = new com.see.truetransact.uicomponent.CPanel();
        panProductID = new com.see.truetransact.uicomponent.CPanel();
        lblProductID = new com.see.truetransact.uicomponent.CLabel();
        cboProductType = new com.see.truetransact.uicomponent.CComboBox();
        lblAccountHead = new com.see.truetransact.uicomponent.CLabel();
        lblAmount = new com.see.truetransact.uicomponent.CLabel();
        txtAmount = new com.see.truetransact.uicomponent.CTextField();
        lblServicTaxAmt = new com.see.truetransact.uicomponent.CLabel();
        txtService_tax_amt = new com.see.truetransact.uicomponent.CTextField();
        lblChargeDetails = new com.see.truetransact.uicomponent.CLabel();
        txtChargeDetails = new com.see.truetransact.uicomponent.CTextField();
        lblAccountHeadDisplay = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerName = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerNameDisplay = new com.see.truetransact.uicomponent.CLabel();
        lblTotalAmt = new com.see.truetransact.uicomponent.CLabel();
        txtTotal_amt = new com.see.truetransact.uicomponent.CTextField();
        panAcctHead = new com.see.truetransact.uicomponent.CPanel();
        txtAccountHead = new com.see.truetransact.uicomponent.CTextField();
        btnAccountHead = new com.see.truetransact.uicomponent.CButton();
        lblProductType = new com.see.truetransact.uicomponent.CLabel();
        cboProductID = new com.see.truetransact.uicomponent.CComboBox();
        lblAccountNumber = new com.see.truetransact.uicomponent.CLabel();
        panAcctNum = new com.see.truetransact.uicomponent.CPanel();
        txtAccountNumber = new com.see.truetransact.uicomponent.CTextField();
        btnAccountNumber = new com.see.truetransact.uicomponent.CButton();
        sptAccountInfo = new com.see.truetransact.uicomponent.CSeparator();
        panAccountHead = new com.see.truetransact.uicomponent.CPanel();
        panTransaction = new com.see.truetransact.uicomponent.CPanel();
        tbrOperativeAcctProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace30 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace31 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace32 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace33 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace34 = new com.see.truetransact.uicomponent.CLabel();
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
        setMinimumSize(new java.awt.Dimension(800, 600));
        setPreferredSize(new java.awt.Dimension(800, 600));

        panAccountInfo.setMinimumSize(new java.awt.Dimension(736, 300));
        panAccountInfo.setPreferredSize(new java.awt.Dimension(736, 300));
        panAccountInfo.setLayout(new java.awt.GridBagLayout());

        panAccountInfoInner.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panAccountInfoInner.setMinimumSize(new java.awt.Dimension(728, 280));
        panAccountInfoInner.setPreferredSize(new java.awt.Dimension(728, 280));
        panAccountInfoInner.setLayout(new java.awt.GridBagLayout());

        panProductID.setMinimumSize(new java.awt.Dimension(350, 200));
        panProductID.setPreferredSize(new java.awt.Dimension(350, 200));
        panProductID.setLayout(new java.awt.GridBagLayout());

        lblProductID.setText("Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblProductID, gridBagConstraints);

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
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(cboProductType, gridBagConstraints);

        lblAccountHead.setText("Account Head Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblAccountHead, gridBagConstraints);

        lblAmount.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblAmount, gridBagConstraints);

        txtAmount.setMaxLength(4);
        txtAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAmountActionPerformed(evt);
            }
        });
        txtAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(txtAmount, gridBagConstraints);

        lblServicTaxAmt.setText("Service Tax Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblServicTaxAmt, gridBagConstraints);

        txtService_tax_amt.setMaxLength(16);
        txtService_tax_amt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtService_tax_amt.setValidation(new CurrencyValidation());
        txtService_tax_amt.setEnabled(false);
        txtService_tax_amt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtService_tax_amtActionPerformed(evt);
            }
        });
        txtService_tax_amt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtService_tax_amtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(txtService_tax_amt, gridBagConstraints);

        lblChargeDetails.setText("Particulars");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblChargeDetails, gridBagConstraints);

        txtChargeDetails.setMaxLength(16);
        txtChargeDetails.setMinimumSize(new java.awt.Dimension(250, 21));
        txtChargeDetails.setPreferredSize(new java.awt.Dimension(230, 21));
        txtChargeDetails.setEnabled(false);
        txtChargeDetails.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtChargeDetailsFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(txtChargeDetails, gridBagConstraints);

        lblAccountHeadDisplay.setForeground(new java.awt.Color(0, 51, 204));
        lblAccountHeadDisplay.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblAccountHeadDisplay.setMaximumSize(new java.awt.Dimension(2250, 21));
        lblAccountHeadDisplay.setMinimumSize(new java.awt.Dimension(225, 21));
        lblAccountHeadDisplay.setPreferredSize(new java.awt.Dimension(225, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblAccountHeadDisplay, gridBagConstraints);

        lblCustomerName.setText("Customer Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblCustomerName, gridBagConstraints);

        lblCustomerNameDisplay.setMinimumSize(new java.awt.Dimension(225, 21));
        lblCustomerNameDisplay.setPreferredSize(new java.awt.Dimension(225, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblCustomerNameDisplay, gridBagConstraints);

        lblTotalAmt.setText("Total Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblTotalAmt, gridBagConstraints);

        txtTotal_amt.setMaxLength(16);
        txtTotal_amt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTotal_amt.setValidation(new CurrencyValidation());
        txtTotal_amt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTotal_amtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(txtTotal_amt, gridBagConstraints);

        panAcctHead.setLayout(new java.awt.GridBagLayout());

        txtAccountHead.setMaxLength(10);
        txtAccountHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccountHead.setEnabled(false);
        txtAccountHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccountHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        panAcctHead.add(txtAccountHead, gridBagConstraints);

        btnAccountHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccountHead.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnAccountHead.setMaximumSize(new java.awt.Dimension(21, 21));
        btnAccountHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnAccountHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccountHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccountHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panAcctHead.add(btnAccountHead, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductID.add(panAcctHead, gridBagConstraints);

        lblProductType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblProductType, gridBagConstraints);

        cboProductID.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductID.setPopupWidth(210);
        cboProductID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(cboProductID, gridBagConstraints);

        lblAccountNumber.setText("Account Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblAccountNumber, gridBagConstraints);

        panAcctNum.setLayout(new java.awt.GridBagLayout());

        txtAccountNumber.setMaxLength(10);
        txtAccountNumber.setMinimumSize(new java.awt.Dimension(100, 21));
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
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductID.add(panAcctNum, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panAccountInfoInner.add(panProductID, gridBagConstraints);

        sptAccountInfo.setOrientation(javax.swing.SwingConstants.VERTICAL);
        sptAccountInfo.setPreferredSize(new java.awt.Dimension(5, 5));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = 1.0;
        panAccountInfoInner.add(sptAccountInfo, gridBagConstraints);

        panAccountHead.setMinimumSize(new java.awt.Dimension(350, 175));
        panAccountHead.setPreferredSize(new java.awt.Dimension(350, 175));
        panAccountHead.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccountInfoInner.add(panAccountHead, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountInfo.add(panAccountInfoInner, gridBagConstraints);

        panTransaction.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountInfo.add(panTransaction, gridBagConstraints);

        getContentPane().add(panAccountInfo, java.awt.BorderLayout.CENTER);

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.setEnabled(false);
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

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace29);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnEdit);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace30);

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

        lblSpace31.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace31.setText("     ");
        lblSpace31.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace31);

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
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnAuthorize);

        lblSpace32.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace32.setText("     ");
        lblSpace32.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace32);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnException);

        lblSpace33.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace33.setText("     ");
        lblSpace33.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace33.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace33.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace33);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
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

        lblSpace34.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace34.setText("     ");
        lblSpace34.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace34);

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

        mbrCustomer.setName("mbrCustomer");

        mnuProcess.setText("Process");
        mnuProcess.setName("mnuProcess");

        mitNew.setText("New");
        mitNew.setName("mitNew");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.setName("mitEdit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.setName("mitDelete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);

        sptNew.setName("sptNew");
        mnuProcess.add(sptNew);

        mitSave.setText("Save");
        mitSave.setName("mitSave");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.setName("mitCancel");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);

        sptSave.setName("sptSave");
        mnuProcess.add(sptSave);

        mitPrint.setText("Print");
        mitPrint.setName("mitPrint");
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.setName("mitClose");
        mnuProcess.add(mitClose);

        mbrCustomer.add(mnuProcess);

        setJMenuBar(mbrCustomer);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmountFocusLost
        // TODO add your handling code here:
        double amount = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
        double total_amt = amount * 10.3 /100;
        total_amt = (double)getNearest((long)(total_amt *100),100)/100;   
        if(total_amt<=0.0)
            total_amt = 1;
        txtService_tax_amt.setText(String.valueOf(total_amt));
        txtTotal_amt.setText(String.valueOf(total_amt + amount));
        transactionUI.setCallingAmount(String.valueOf(total_amt + amount));
        observable.setTotal_amt(String.valueOf(txtTotal_amt.getText()));
        observable.setAmount(String.valueOf(txtTotal_amt.getText()));
        observable.setService_tax_amt(String.valueOf(txtService_tax_amt.getText()));
    }//GEN-LAST:event_txtAmountFocusLost
    
    public long roundOffLower(long number,long roundingFactor) {
        long mod = number%roundingFactor;
        return number-mod;
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
    private void txtAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAmountActionPerformed

    private void btnAccountNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccountNumberActionPerformed
        // TODO add your handling code here:
        popUp();

    }//GEN-LAST:event_btnAccountNumberActionPerformed

    private void cboProductIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboProductIDActionPerformed
    
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        popUp();
    }//GEN-LAST:event_btnViewActionPerformed
    
    private void txtService_tax_amtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtService_tax_amtActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtService_tax_amtActionPerformed
    
    private void txtChargeDetailsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtChargeDetailsFocusLost

    }//GEN-LAST:event_txtChargeDetailsFocusLost
    
    private void txtService_tax_amtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtService_tax_amtFocusLost

        double amount = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
        double Service_tax_amt = CommonUtil.convertObjToDouble(txtService_tax_amt.getText()).doubleValue();
        if(Service_tax_amt <=0){
            ClientUtil.showAlertWindow("can't make Zero");
            Service_tax_amt = 1;
        }
        txtService_tax_amt.setText(String.valueOf(Service_tax_amt));
        txtTotal_amt.setText(String.valueOf(Service_tax_amt + amount));
        transactionUI.setCallingAmount(String.valueOf(Service_tax_amt + amount));
        
    }//GEN-LAST:event_txtService_tax_amtFocusLost
    
    private void txtTotal_amtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTotal_amtFocusLost

    }//GEN-LAST:event_txtTotal_amtFocusLost
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        updateAuthorizeStatus(CommonConstants.STATUS_EXCEPTION);
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        updateAuthorizeStatus(CommonConstants.STATUS_REJECTED);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        btnReject.setEnabled(true);
        btnAccountHead.setEnabled(false);
        transactionUI.setCallingUiMode(observable.getActionType());
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        updateAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnCancel.setEnabled(true);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnAccountHead.setEnabled(false);
        transactionUI.setCallingUiMode(observable.getActionType());
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        mitCloseActionPerformed(evt);
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {
        cifClosingAlert();
    }
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        observable.resetForm();
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_DELETE);
        popUp();
        observable.setStatus();
        btnDelete.setEnabled(false);
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnAccountHead.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);        
        transactionUI.setCallingUiMode(observable.getActionType());
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        observable.resetForm();
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_EDIT);
        popUp();
        observable.setStatus();
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
        btnNew.setEnabled(false);
        btnAccountHead.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        txtChargeDetails.setEnabled(true);
        transactionUI.setCallingUiMode(observable.getActionType());
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
        private void deletescreenLock(){
            HashMap map=new HashMap();
            map.put("USER_ID",ProxyParameters.USER_ID);
            map.put("TRANS_DT", currDt.clone());
            map.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
            ClientUtil.execute("DELETE_SCREEN_LOCK", map);
        }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        if(observable.getAuthorizeStatus()!=null)
            super.removeEditLock(txtAccountNumber.getText());
        deletescreenLock();
        if (observable.getActionType()!=ClientConstants.ACTIONTYPE_AUTHORIZE) {
            observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_CANCEL);
        }
        viewType = CANCEL ;
        
        setModified(false);
        observable.resetForm();
        transDetailsUI.setTransDetails(null, null, null);
        enableDisable(false);
        setButtonEnableDisable();
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        if (observable.getActionType()!=ClientConstants.ACTIONTYPE_AUTHORIZE)
            observable.setStatus();
        setModified(false);
       observable.setAuthorizeMap(new HashMap());
       txtTotal_amt.setText("");
       txtAmount.setText("");
       txtService_tax_amt.setText("");
       lblAccountHeadDisplay.setText("");
       lblCustomerNameDisplay.setText("");
       btnAccountHead.setEnabled(false);
       btnDelete.setEnabled(true);
       btnNew.setEnabled(true);
       btnEdit.setEnabled(true);
       btnSave.setEnabled(false);
       btnCancel.setEnabled(true);
       btnAuthorize.setEnabled(true);
       btnReject.setEnabled(true);
       transId = "";
    }//GEN-LAST:event_btnCancelActionPerformed
    private void savePerformed(){
        updateOBFields();
        observable.doAction();
        
        if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
            HashMap lockMap = new HashMap();
            ArrayList lst = new ArrayList();
            lst.add("ACCOUNT NUMBER");
            lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW)
                if (observable.getProxyReturnMap()!=null) {
                    if (observable.getProxyReturnMap().containsKey("ISSUE_ID")) {
                        lockMap.put("ACCOUNT NUMBER",observable.getProxyReturnMap().get("ACT_NUM"));
                    }
                }
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ) {
//                lockMap.put("ACCOUNT NUMBER", observable.getTxtAccountNumber());
            }
            setEditLockMap(lockMap);
            setEditLock();
            
            ClientUtil.enableDisable(this, false);
            setButtonEnableDisable();
            observable.resetForm();
            observable.setResultStatus();
            transactionUI.resetObjects();
            transDetailsUI.setTransDetails(null, null, null);
        }else{
            btnNew.enable(false);
            btnEdit.enable(false);
            btnDelete.enable(false);
            btnSave.enable(true);
            btnCancel.enable(true);
        }
        
    }
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        //System.out.println("observable.getActionType() : " + observable.getActionType());
        ChargesServiceTaxRB chargesServiceTaxRB = new ChargesServiceTaxRB();
        double totalClosingAmt = CommonUtil.convertObjToDouble(txtTotal_amt.getText()).doubleValue();
        double transDetAmt = CommonUtil.convertObjToDouble(transactionUI.getCallingAmount()).doubleValue();
        if((observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) && 
        (!CommonUtil.convertObjToStr(transDetailsUI.getUnclearBalance()).equals("0.00"))){
            displayAlert(chargesServiceTaxRB.getString("UnclearBalance"));
            return;
        }else{
            int transactionSize = 0 ;
            if(transactionUI.getOutputTO() == null && totalClosingAmt!=0){
                ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS)) ;
            }
            else{
                transactionSize = (transactionUI.getOutputTO()).size();
                observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
            }
            
            if(transactionSize == 0 && totalClosingAmt!=0){
                ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS)) ;
            }
            else if(transactionSize != 0 || totalClosingAmt ==0){
                if (!transactionUI.isBtnSaveTransactionDetailsFlag()) {
                    ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.SAVE_TX_DETAILS));
                }else{
                    double transTotalAmt = CommonUtil.convertObjToDouble(transactionUI.getTransactionOB().getLblTotalTransactionAmtVal()).doubleValue();
                    if (ClientUtil.checkTotalAmountTallied(totalClosingAmt, transTotalAmt) == false && totalClosingAmt!=0)
                        ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NOT_TALLY));
                    else{
                        //To display an alert if the mandatory fields are not properly inputted, else proceed with normal operation
                        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panProductID);
                        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
                            displayAlert(mandatoryMessage);
                            return;
                        }else if(txtAccountHead.getText().length() == 0){
                            ClientUtil.displayAlert("Account Head Should not be empty...");
                            return;
                        }else{
                            savePerformed();
                            btnCancelActionPerformed(null);
                            setModified(false);
                            observable.setResultStatus();
                        }
                    }
                }
            }
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        enableDisable(true);
        txtAccountNumber.setEnabled(true);
        txtAccountNumber.setEditable(true);
        setButtonEnableDisable();
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setModified(true);
        observable.resetForm();
        observable.setStatus();
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        txtTotal_amt.setEnabled(false);
        btnDelete.setEnabled(false);
        btnAccountHead.setEnabled(true);
        cboProductType.setSelectedItem("General Ledger");
        cboProductType.setEnabled(false);
    }//GEN-LAST:event_btnNewActionPerformed
    
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
    
    private void txtAccountHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccountHeadFocusLost
        
    }//GEN-LAST:event_txtAccountHeadFocusLost
    
    private void btnAccountHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccountHeadActionPerformed
        popUp();
    }//GEN-LAST:event_btnAccountHeadActionPerformed
    
    private void cboProductTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductTypeActionPerformed
        String prodType = ((ComboBoxModel)cboProductType.getModel()).getKeyForSelected().toString();
        if(prodType!=null && prodType.length()>0){
            observable.setMainProductTypeValue(prodType);  
            if(prodType.equals("GL")){
                txtAccountNumber.setEnabled(false);
                btnAccountHead.setEnabled(true);
                btnAccountNumber.setEnabled(false);
                cboProductID.setEnabled(false);
                txtAccountHead.setEnabled(false);
                cboProductID.setSelectedItem("");
                lblCustomerNameDisplay.setText("");
                txtAccountNumber.setText("");
            }else{
                txtAccountHead.setEnabled(false);
                btnAccountHead.setEnabled(false);
                btnAccountNumber.setEnabled(true);
                txtAccountNumber.setEnabled(true);
                cboProductID.setEnabled(true);
                lblAccountHeadDisplay.setText("");
                txtAccountHead.setText("");
                lblCustomerNameDisplay.setText("");
                cboProductID.setSelectedItem("");
                observable.getProducts();
                cboProductID.setModel(observable.getCbmProductID());
            }
        }
    }//GEN-LAST:event_cboProductTypeActionPerformed
    
    /** To populate Comboboxes */
    private void initComponentData() {
        cboProductID.setModel(observable.getCbmProductID());
        cboProductType.setModel(observable.getCbmProductType());
    }
    
    /** To display a popUp window for viewing existing data */
    private void popUp(){
        HashMap testMap = null;
        //To display customer info based on the selected ProductID
        if( observable.getActionType() == ClientConstants.ACTIONTYPE_NEW ){
            testMap = accountViewMap();
        }
        //To display the existing accounts which are set to closed
        else if( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW){
            testMap = accountEditMap();
        }
        new com.see.truetransact.ui.common.viewall.ViewAll(this, testMap).show();
        
    }
    private void clearPreviousAccountDetails(){
        this.txtAccountNumber.setText("");
        this.lblCustomerNameDisplay.setText("");
        observable.setCustomerName("");
        transDetailsUI.setTransDetails(null, null, null);
    }

    /** Called by the Popup window created thru popUp method */
    public void fillData(Object obj) {
        try{
            final HashMap hash = (HashMap) obj;
            System.out.println("filldata####"+hash);
            if( observable.getActionType() == ClientConstants.ACTIONTYPE_NEW ){
                fillDataNew(hash);
            }else if( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || 
            observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || 
            observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW){
               fillDataEdit(hash);
               btnAccountHead.setEnabled(false);
                if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                   ClientUtil.enableDisable(panProductID,true);
                   cboProductType.setEnabled(false);
                   cboProductID.setEnabled(false);
                   txtAccountHead.setEnabled(false);
                   txtAccountNumber.setEnabled(false);                   
                   txtTotal_amt.setEnabled(false);
                   btnSave.setEnabled(true);
                }                
                if(observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
                   ClientUtil.enableDisable(panProductID,false);
                }
            }else if( observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || 
            observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT || 
            observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION){
                hash.put("TRANS_ID", hash.get("TRANS_ID"));
                fillDataEdit(hash);
                setButton4Authorize();
                transactionUI.setCallingUiMode(viewType);
                ClientUtil.enableDisable(this,false);
            }
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || 
            observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE){
                HashMap lockMap = new HashMap();
                transId = CommonUtil.convertObjToStr(hash.get("TRANS_ID"));
                if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT)
                    lockMap.put("MODE_OF_OPERATION","EDIT");
                if(observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE)
                    lockMap.put("MODE_OF_OPERATION","DELETE");
                if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE)
                    lockMap.put("MODE_OF_OPERATION","AUTHORIZE");
                lockMap.put("USER_ID",ProxyParameters.USER_ID);
                lockMap.put("TRANS_ID",transId);
                lockMap.put("TRANS_DT", currDt.clone());
                lockMap.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
                ClientUtil.execute("insertauthorizationLock", lockMap);                
            }
            transactionUI.setSourceAccountNumber(txtAccountNumber.getText());
            transactionUI.setCallingApplicantName(lblCustomerNameDisplay.getText());
            if(viewType==AUTHORIZE) {
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
            }
        }catch(Exception e){
            log.error(e);
        }
    }

    /** To fillData for a new entry */
    private void fillDataNew(HashMap hash){
        if(!hash.isEmpty()){
            prodType = ((ComboBoxModel)cboProductType.getModel()).getKeyForSelected().toString();
            if(prodType.equals("TD")){
                hash.put("ACCOUNTNO", hash.get("ACCOUNTNO")+"_1");
            }
            txtAccountNumber.setText((String)hash.get("ACCOUNTNO"));
            if( txtAccountNumber.getText().length() > 0 ){
                transDetailsUI.setTransDetails(prodType, ProxyParameters.BRANCH_ID, txtAccountNumber.getText());
                txtAccountNumber.setText((String)hash.get("ACCOUNTNO"));
                lblCustomerNameDisplay.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMERNAME")));
            }else{
                observable.setAccountHeadId(CommonUtil.convertObjToStr(hash.get("A/C HEAD")));
                txtAccountHead.setText(observable.getAccountHeadId());
                lblAccountHeadDisplay.setText(CommonUtil.convertObjToStr(hash.get("A/C HEAD DESCRIPTION")));
                transDetailsUI.setTransDetails(prodType, ProxyParameters.BRANCH_ID, txtAccountHead.getText());
            }
        }else{
            this.txtAccountNumber.setText("");
            this.lblCustomerName.setText("");
            transDetailsUI.setTransDetails(null, null, null);
        }
    }
    
    /** To fillData for existing entry */
    private void fillDataEdit(HashMap hash){
        String acctNum = null ;
        String prodType = null;
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW){
            acctNum = hash.get("TRANS_ID").toString();
            prodType = hash.get("AC_HEAD").toString();
        } else {
            acctNum =  hash.get("ACCOUNT NUMBER").toString();
        }
        transDetailsUI.setTransDetails("GL",ProxyParameters.BRANCH_ID,prodType);
        HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE,acctNum);
        observable.getData(where);
        setModified(true);
        observable.ttNotifyObservers();
        updateOBFields();
        observable.ttNotifyObservers();
    }
    
    /** To get popUp data for a new entry */
    private HashMap accountViewMap(){
        final HashMap testMap = new HashMap();
        final HashMap whereMap = new HashMap();
        prodType = ((ComboBoxModel)cboProductType.getModel()).getKeyForSelected().toString();
        if(prodType.equals("GL")){
            testMap.put(CommonConstants.MAP_NAME, "Transfer.getSelectAcctHeadCR");
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            testMap.put(CommonConstants.MAP_WHERE, testMap);
        }else{
            testMap.put(CommonConstants.MAP_NAME, "Transfer.getAccountList"+((ComboBoxModel)this.cboProductType.getModel()).getKeyForSelected());
            testMap.put("PROD_ID", ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected());
            testMap.put("SELECTED_BRANCH", TrueTransactMain.selBranch);
            testMap.put(CommonConstants.MAP_WHERE, testMap);
        }
        return testMap;
    }
    
    /** To get popUp data for already existing entries for modification */
    private HashMap accountEditMap(){
        final HashMap testMap = new HashMap();
        testMap.put("MAPNAME", "getSelectChargesServiceAuthorizeTOList");
        HashMap whereMap = new HashMap();
        whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        testMap.put(CommonConstants.MAP_WHERE, whereMap);
        return testMap;
    }
    
    /** To display customer related details based on account number */
    private void enableDisable(boolean yesno){
        ClientUtil.enableDisable(this, yesno);
        txtAccountNumber.setEnabled(false);
        txtAccountNumber.setEditable(false);
    }
    
    private void setButtonEnableDisable() {
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
        
        btnAccountNumber.setEnabled(!btnNew.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
    }
    
    private void setButton4Authorize() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(btnNew.isEnabled());
        btnDelete.setEnabled(btnNew.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        
        btnSave.setEnabled(btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
    }
    
    public void update(Observable observed, Object arg) {
        cboProductType.setSelectedItem((observable.getCbmProductType()).getDataForKey(observable.getCboProductType()));
        cboProductID.setSelectedItem((observable.getCbmProductID()).getDataForKey(observable.getCboProductID()));
        txtAccountNumber.setText(observable.getTxtAccountNumber());
        txtAccountHead.setText(observable.getAccountHeadId());
        txtAmount.setText(observable.getAmount());
        txtService_tax_amt.setText(observable.getService_tax_amt());
        txtTotal_amt.setText(observable.getTotal_amt());
        txtChargeDetails.setText(observable.getParticulars());
        lblStatus.setText(observable.getLblStatus());
    }
    
    public void updateOBFields() {
        observable.setCboProductType((String)(((ComboBoxModel)(cboProductType).getModel())).getKeyForSelected());
        observable.setAccountHeadId(txtAccountHead.getText());
        observable.setCboProductID((String)(((ComboBoxModel)(cboProductID).getModel())).getKeyForSelected());
        observable.setTxtAccountNumber(txtAccountNumber.getText());
        observable.setAccountHeadId(txtAccountHead.getText());
        observable.setAmount(txtAmount.getText());
        observable.setService_tax_amt(txtService_tax_amt.getText());
        observable.setTotal_amt(txtTotal_amt.getText());
        observable.setParticulars(CommonUtil.convertObjToStr(txtChargeDetails.getText()));
    }
    
    private void setFieldNames() {
        btnAccountNumber.setName("btnAccountNumber");
        btnSave.setName("btnSave");
        cboProductType.setName("cboProductType");
        cboProductID.setName("cboProductID");
        txtAccountHead.setName("txtAccountHead");
        txtAccountNumber.setName("txtAccountNumber");
        lblAmount.setName("lblAmount");
        lblAccountHead.setName("lblAccountHead");
        lblAccountHeadDisplay.setName("lblAccountHeadDisplay");
        lblAccountNumber.setName("lblAccountNumber");
        lblChargeDetails.setName("lblChargeDetails");
        lblCustomerName.setName("lblCustomerName");
        lblCustomerNameDisplay.setName("lblCustomerNameDisplay");
        lblServicTaxAmt.setName("lblServicTaxAmt");
        lblTotalAmt.setName("lblTotalAmt");
        lblProductID.setName("lblProductID");
        mbrCustomer.setName("mbrCustomer");
        panAccountHead.setName("panAccountHead");
        panAccountInfo.setName("panAccountInfo");
        panProductID.setName("panProductID");
        sptAccountInfo.setName("sptAccountInfo");
        txtAmount.setName("txtAmount");
        txtAccountNumber.setName("txtAccountNumber");
        txtService_tax_amt.setName("txtService_tax_amt");
        txtTotal_amt.setName("txtTotal_amt");
        txtChargeDetails.setName("txtChargeDetails");
    }
    
    private void internationalize() {
        ChargesServiceTaxRB resourceBundle = new ChargesServiceTaxRB();
        lblCustomerName.setText(resourceBundle.getString("lblCustomerName"));
        lblAccountHeadDisplay.setText(resourceBundle.getString("lblAccountHeadDisplay"));
        lblAccountNumber.setText(resourceBundle.getString("lblAccountNumber"));
        lblAccountHead.setText(resourceBundle.getString("lblAccountHead"));
        btnAccountNumber.setText(resourceBundle.getString("btnAccountNumber"));
        lblProductID.setText(resourceBundle.getString("lblProductID"));
        lblChargeDetails.setText(resourceBundle.getString("lblChargeDetails"));
        lblAmount.setText(resourceBundle.getString("lblAmount"));
        lblCustomerNameDisplay.setText(resourceBundle.getString("lblCustomerNameDisplay"));
        lblServicTaxAmt.setText(resourceBundle.getString("lblServicTaxAmt"));
        lblTotalAmt.setText(resourceBundle.getString("lblTotalAmt"));
    }
    
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAccountHead;
    private com.see.truetransact.uicomponent.CButton btnAccountNumber;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
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
    private com.see.truetransact.uicomponent.CLabel lblAccountHead;
    private com.see.truetransact.uicomponent.CLabel lblAccountHeadDisplay;
    private com.see.truetransact.uicomponent.CLabel lblAccountNumber;
    private com.see.truetransact.uicomponent.CLabel lblAmount;
    private com.see.truetransact.uicomponent.CLabel lblChargeDetails;
    private com.see.truetransact.uicomponent.CLabel lblCustomerName;
    private com.see.truetransact.uicomponent.CLabel lblCustomerNameDisplay;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblProductID;
    private com.see.truetransact.uicomponent.CLabel lblProductType;
    private com.see.truetransact.uicomponent.CLabel lblServicTaxAmt;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace30;
    private com.see.truetransact.uicomponent.CLabel lblSpace31;
    private com.see.truetransact.uicomponent.CLabel lblSpace32;
    private com.see.truetransact.uicomponent.CLabel lblSpace33;
    private com.see.truetransact.uicomponent.CLabel lblSpace34;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTotalAmt;
    private com.see.truetransact.uicomponent.CMenuBar mbrCustomer;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAccountHead;
    private com.see.truetransact.uicomponent.CPanel panAccountInfo;
    private com.see.truetransact.uicomponent.CPanel panAccountInfoInner;
    private com.see.truetransact.uicomponent.CPanel panAcctHead;
    private com.see.truetransact.uicomponent.CPanel panAcctNum;
    private com.see.truetransact.uicomponent.CPanel panProductID;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTransaction;
    private com.see.truetransact.uicomponent.CButtonGroup rdoTransactionType;
    private com.see.truetransact.uicomponent.CSeparator sptAccountInfo;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private javax.swing.JToolBar tbrOperativeAcctProduct;
    private com.see.truetransact.uicomponent.CTextField txtAccountHead;
    private com.see.truetransact.uicomponent.CTextField txtAccountNumber;
    private com.see.truetransact.uicomponent.CTextField txtAmount;
    private com.see.truetransact.uicomponent.CTextField txtChargeDetails;
    private com.see.truetransact.uicomponent.CTextField txtService_tax_amt;
    private com.see.truetransact.uicomponent.CTextField txtTotal_amt;
    // End of variables declaration//GEN-END:variables
    
    public static void main(String[] args) {
        ChargesServiceTaxUI ac = new ChargesServiceTaxUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(ac);
        j.show();
        ac.show();
    }

    /**
     * Getter for property viewType.
     * @return Value of property viewType.
     */
    public int getViewType() {
        return viewType;
    }
    
    /**
     * Setter for property viewType.
     * @param viewType New value of property viewType.
     */
    public void setViewType(int viewType) {
        this.viewType = viewType;
    }    
}
