/* Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemittancePaymentUI.java
 *
 * Created on December 26, 2003, 5:34 PM
 */

package com.see.truetransact.ui.remittance;

import java.util.Observable;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import java.util.HashMap;
import java.util.Observer;
import com.see.truetransact.transferobject.remittance.RemittancePaymentTO;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.ui.remittance.RemittancePaymentOB;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CButtonGroup;// To add and Remove the Radio Buttons...

import com.see.truetransact.ui.common.transaction.TransactionUI;

/**
 *
 * @author  Lohith R.
 * @modified  Sunil Sreedharan
 *  Changes Done :
 *      a. Added Transaction 20-June-2005
 *      b. Added Lookup for Remittance issue 20-June-2005
 *      c. Added Authorization code 20-June-2005
 *      d. Removed unnecessary code 21-June-2005
 *      e. Added Payment validation at Branch and Bank as per product config 21-June-2005
 *
 *      TODO : Retrieve Cancel Charges from product and populate. 
 *             Currently the query has been written, lookup entries have not been added.
 */
public class RemittancePaymentUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer,UIMandatoryField {
    
    private RemittancePaymentOB observable;
    private RemittancePaymentRB resourceBundle = new RemittancePaymentRB();
    HashMap mandatoryMap;
    final int EDIT=0,DELETE=1,NEW=2,SERIALNUM=2, VIEW1 =5;
    final int CREDITACCOUNTHEAD=2,DEBITACCOUNTHEAD=3,ISSUED=1,NOT_ISSUED =2;
    int ACTION=-1, AUTHORIZE = 100, viewType=-1;
    private String view=null;
    private String city = "";
    private String category = "";
    private int checkSerialNo = 1;
    private StringBuffer message;
    private String warningMsg = new String();
    private HashMap resultMap = new HashMap();
    private boolean isFilled = false;
    private RemittancePaymentMRB objMandatoryRB ;
    private String issueCode = new String();
    private String code = new String();
    private Date currDt = null;
    private TransactionUI transactionUI = new TransactionUI();
    
    /** Creates new form RemittancePaymentUI */
    public RemittancePaymentUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initStartUP();
        
        transactionUI.addToScreen(panTransaction);
        observable.setTransactionOB(transactionUI.getTransactionOB());
    }
    
    /** Initialzation of UI */
    private void initStartUP(){
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setObservable();
        initComponentData();
        setMaximumLength();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panMain);
        setHelpMessage();
        transactionUI.setSourceScreen("REMITPAYMENT");
        observable.resetStatus();
        observable.resetForm();
        resetLabel();
//        btnView.setEnabled(false);
        btnSerialNo.setEnabled(false);
        txtCharges.setEnabled(false);
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();
        btnView1.setEnabled(!btnView1.isEnabled());
        txtPayAmount.setEnabled(false);
    }
    
    private void setFieldNames() {
        btnCancel.setName("btnCancel");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnClose.setName("btnClose");
        btnNew.setName("btnNew");
        btnSave.setName("btnSave");
        btnPrint.setName("btnPrint");
        PaymentDt.setName("PaymentDt");
        cboInstrumentType.setName("cboInstrumentType");
        cboPayStatus.setName("cboPayStatus");
        lblPayStatus.setName("lblPayStatus");
        lblDateIssueValue.setName("lblDateIssueValue");
        lblAddress.setName("lblAddress");
        lblCharges.setName("lblCharges");
        lblServiceTax.setName("lblServiceTax");
        lblRemitPayId.setName("lblRemitPayId");
        lblRemitPayIdValue.setName("lblRemitPayIdValue");
        lblDateIssue.setName("lblDateIssue");
        lblFavouring.setName("lblFavouring");
        lblInstrumentType.setName("lblInstrumentType");
        lblAccHeadBal.setName("lblAccHeadBal");
        lblAccHead.setName("lblAccHead");
        lblIssue.setName("lblIssue");
        lblIssueCode.setName("lblIssueCode");
        lblMsg.setName("lblMsg");
        lblPayAmount.setName("lblPayAmount");
        lblInstrumentNumber.setName("lblInstrumentNumber");
        lblRemarks.setName("lblRemarks");
        lblSerialNumber.setName("lblSerialNumber");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblStatus.setName("lblStatus");
        mbrMain.setName("mbrMain");
        panInstrumentDetails.setName("panInstrumentDetails");
        panIssueCode.setName("panIssueCode");
        panStatus.setName("panStatus");
        txtAddress.setName("txtAddress");
        lblBranchCodeValue.setName("lblBranchCodeValue");
        txtCharges.setName("txtCharges");
        txtServiceTax.setName("txtServiceTax");
        lblFavouringValue.setName("lblFavouringValue");
        lblIssueBankValue.setName("lblIssueBankValue");
        txtPayAmount.setName("txtPayAmount");
        txtNumber1.setName("txtNumber1");
        txtNumber2.setName("txtNumber2");
        txtRemarks.setName("txtRemarks");
        txtSerialNumber.setName("txtSerialNumber");
    }
    
    private void internationalize() {
        ((javax.swing.border.TitledBorder)panInstrumentDetails.getBorder()).setTitle(resourceBundle.getString("panInstrumentDetails"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblPayStatus.setText(resourceBundle.getString("lblPayStatus"));
        lblPayDate.setText(resourceBundle.getString("lblPayDate"));
        lblDateIssue.setText(resourceBundle.getString("lblDateIssue"));
        lblSerialNumber.setText(resourceBundle.getString("lblSerialNumber"));
        lblCharges.setText(resourceBundle.getString("lblCharges"));
        lblServiceTax.setText(resourceBundle.getString("lblServiceTax"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblAddress.setText(resourceBundle.getString("lblAddress"));
        lblRemitPayId.setText(resourceBundle.getString("lblRemitPayId"));
        lblRemarks.setText(resourceBundle.getString("lblRemarks"));
        lblFavouring.setText(resourceBundle.getString("lblFavouring"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblInstrumentType.setText(resourceBundle.getString("lblInstrumentType"));
        lblAccHeadBal.setText(resourceBundle.getString("lblAccHeadBal"));
        lblAccHead.setText(resourceBundle.getString("lblAccHead"));
        lblIssueCode.setText(resourceBundle.getString("lblIssueCode"));
        lblPayAmount.setText(resourceBundle.getString("lblPayAmount"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblInstrumentNumber.setText(resourceBundle.getString("lblInstrumentNumber"));
        lblIssue.setText(resourceBundle.getString("lblIssue"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
    }
    
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboInstrumentType", new Boolean(true));
        mandatoryMap.put("txtSerialNumber", new Boolean(true));
        mandatoryMap.put("txtCreditHead", new Boolean(false));
        mandatoryMap.put("txtDebitHead", new Boolean(false));
        mandatoryMap.put("cboPayStatus", new Boolean(true));
        mandatoryMap.put("txtCharges", new Boolean(false));
        mandatoryMap.put("txtServiceTax", new Boolean(false));
        mandatoryMap.put("txtPayAmount", new Boolean(true));
        mandatoryMap.put("txtCreditNo", new Boolean(false));
        mandatoryMap.put("txtDebitNo", new Boolean(false));
        mandatoryMap.put("txtRemarks", new Boolean(false));
        mandatoryMap.put("txtAddress", new Boolean(false));
        mandatoryMap.put("txtNumber1", new Boolean(true));
        mandatoryMap.put("txtNumber2", new Boolean(true));
        
    }
    
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    private void setObservable() {
        /* Implementing Singleton pattern */
        observable = RemittancePaymentOB.getInstance();
        observable.addObserver(this);
    }
    
    private void initComponentData() {
        cboInstrumentType.setModel(observable.getCbmInstrumentType());
        cboPayStatus.setModel(observable.getCbmPayStatus());
    }
    
    private void setMaximumLength(){
        txtSerialNumber.setMaxLength(32);
        txtAddress.setMaxLength(32);
        txtPayAmount.setMaxLength(16);
        txtPayAmount.setValidation(new NumericValidation());
        txtNumber2.setValidation(new NumericValidation());
        txtCharges.setMaxLength(16);
        txtCharges.setValidation(new NumericValidation());
        txtServiceTax.setMaxLength(16);
        txtServiceTax.setValidation(new NumericValidation());
        txtRemarks.setMaxLength(256);
    }
    
    public void setHelpMessage() {
        objMandatoryRB = new RemittancePaymentMRB();
        cboInstrumentType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboInstrumentType"));
        txtSerialNumber.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSerialNumber"));
        txtCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCharges"));
        txtServiceTax.setHelpMessage(lblMsg, objMandatoryRB.getString("txtServiceTax"));
        txtPayAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPayAmount"));
        txtRemarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRemarks"));
        txtAddress.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAddress"));
        txtNumber1.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNumber1"));
        txtNumber2.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNumber2"));
    }
    
    public void update(Observable observed, Object arg) {
        removeRadioButtons();
        rdoCancel.setSelected(observable.isRdoCancel());
        rdoPayment.setSelected(observable.isRdoPayment());
        lblRemitPayIdValue.setText(observable.getLblRemitPayId());
        cboInstrumentType.setSelectedItem(observable.getCboInstrumentType());
        txtSerialNumber.setText(observable.getTxtSerialNumber());
        txtCharges.setText(observable.getTxtCharges());
        txtServiceTax.setText(observable.getTxtServiceTax());
        txtPayAmount.setText(observable.getTxtPayAmount());
        txtRemarks.setText(observable.getTxtRemarks());
        txtAddress.setText(observable.getTxtAddress());
        txtNumber1.setText(observable.getTxtNumber1());
        txtNumber2.setText(observable.getTxtNumber2());
        cboPayStatus.setSelectedItem(observable.getCboPayStatus());
        lblPayableAmountValue.setText(observable.getLblPayableAmount());
        lblFavouringValue.setText(observable.getLblFavouring());
        lblDateIssueValue.setText(observable.getLblDateIssue());
        lblIssueBankValue.setText(observable.getLblIssueBank());
        lblBranchCodeValue.setText(observable.getLblBranchCode());
        lblStatus.setText(observable.getLblStatus());
        PaymentDt.setDateValue(observable.getPaymentDt());
        lblDupRevValue.setText(observable.getLblDupRevValue());
        addRadioButtons();
    }
    
    /** To clear the Text of Labels after doing save operation */
    private void resetLabel(){
        lblRemitPayIdValue.setText("");
        lblFavouringValue.setText("");
        lblBranchCodeValue.setText("");
        lblIssueBankValue.setText("");
        lblDateIssueValue.setText("");
    }
    
    public void authorizeStatus(String authorizeStatus) {
        if (!isFilled ){
//            AUTHORIZE = ClientConstants.ACTIONTYPE_AUTHORIZE ;
//            observable.setActionType(AUTHORIZE) ;
            HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "getRemitPaymentAuthorizeList");
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
           // btnSave.setEnabled(false);
            //btnDelete.setEnabled(false);
            
        } else if (isFilled){
            isFilled = false;
            HashMap singleAuthorizeMap = new HashMap();
            ArrayList arrList = new ArrayList();
            HashMap authDataMap = new HashMap();
            authDataMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            authDataMap.put("REMIT_PAY_ID", lblRemitPayIdValue.getText());
            authDataMap.put("VARIABLE_NO", txtSerialNumber.getText());
            arrList.add(authDataMap);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(singleAuthorizeMap);
            observable.setResultStatus();
            
        }
    }
    
    public void authorize(HashMap map) {
        map.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
//        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        observable.setAuthorizeMap(map);
        observable.doAction();
        if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
        super.setOpenForEditBy(observable.getStatusBy());
        super.removeEditLock(lblRemitPayIdValue.getText());
        observable.resetForm();
        btnCancelActionPerformed(null);
        observable.setResultStatus();
        }
//      lblStatus.setText(ClientConstants.ACTION_STATUS[observable.getActionType()]); 
    }
    
    /** Display message  */
    private void ShowDialogue(String warningMessage){
        String[] options = {resourceBundle.getString("cDialogOK")};
        if(resourceBundle.getString(warningMessage).equals(resourceBundle.getString("printedNumberMsg"))){
            COptionPane.showOptionDialog(null,resourceBundle.getString(warningMessage)+ observable.getDuplicateDt(), CommonConstants.WARNINGTITLE,
            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
            null, options, options[0]);
        }else{
            COptionPane.showOptionDialog(null,resourceBundle.getString(warningMessage), CommonConstants.WARNINGTITLE,
            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
            null, options, options[0]);
        }
        return;
    }
    
    public static void main(String args[]){
        try{
            javax.swing.JFrame frame = new javax.swing.JFrame();
            RemittancePaymentUI ui = new RemittancePaymentUI();
            frame.getContentPane().add(ui);
            frame.setSize(600,600);
            frame.show();
            ui.show();
        }catch(Exception e){
            e.printStackTrace();
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

        cButtonGroup1 = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrMain = new javax.swing.JToolBar();
        btnView1 = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace17 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace18 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace19 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace20 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace21 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace22 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panMain = new com.see.truetransact.uicomponent.CPanel();
        panInstrumentDetails = new com.see.truetransact.uicomponent.CPanel();
        panIssueCode = new com.see.truetransact.uicomponent.CPanel();
        lblIssueCode = new com.see.truetransact.uicomponent.CLabel();
        lblIssueBankValue = new com.see.truetransact.uicomponent.CLabel();
        lblBranchCodeValue = new com.see.truetransact.uicomponent.CLabel();
        lblInstrumentType = new com.see.truetransact.uicomponent.CLabel();
        cboInstrumentType = new com.see.truetransact.uicomponent.CComboBox();
        lblIssue = new com.see.truetransact.uicomponent.CLabel();
        lblSerialNumber = new com.see.truetransact.uicomponent.CLabel();
        lblDateIssue = new com.see.truetransact.uicomponent.CLabel();
        lblInstrumentNumber = new com.see.truetransact.uicomponent.CLabel();
        lblDateIssueValue = new com.see.truetransact.uicomponent.CLabel();
        lblRemitPayId = new com.see.truetransact.uicomponent.CLabel();
        lblRemitPayIdValue = new com.see.truetransact.uicomponent.CLabel();
        panInstrumentNumber = new com.see.truetransact.uicomponent.CPanel();
        txtNumber1 = new com.see.truetransact.uicomponent.CTextField();
        txtNumber2 = new com.see.truetransact.uicomponent.CTextField();
        lblAddress = new com.see.truetransact.uicomponent.CLabel();
        txtAddress = new com.see.truetransact.uicomponent.CTextField();
        lblFavouring = new com.see.truetransact.uicomponent.CLabel();
        lblFavouringValue = new com.see.truetransact.uicomponent.CLabel();
        lblPayAmount = new com.see.truetransact.uicomponent.CLabel();
        txtPayAmount = new com.see.truetransact.uicomponent.CTextField();
        lblCharges = new com.see.truetransact.uicomponent.CLabel();
        txtCharges = new com.see.truetransact.uicomponent.CTextField();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        lblPayStatus = new com.see.truetransact.uicomponent.CLabel();
        cboPayStatus = new com.see.truetransact.uicomponent.CComboBox();
        cSeparator1 = new com.see.truetransact.uicomponent.CSeparator();
        lblPayableAmount = new com.see.truetransact.uicomponent.CLabel();
        lblPayableAmountValue = new com.see.truetransact.uicomponent.CLabel();
        panSerialNo = new com.see.truetransact.uicomponent.CPanel();
        txtSerialNumber = new com.see.truetransact.uicomponent.CTextField();
        btnSerialNo = new com.see.truetransact.uicomponent.CButton();
        lblAccHeadBalDisplay = new javax.swing.JLabel();
        lblAccHeadBal = new com.see.truetransact.uicomponent.CLabel();
        lblAccHead = new com.see.truetransact.uicomponent.CLabel();
        lblAccHeadProdIdDisplay = new javax.swing.JLabel();
        btnView = new com.see.truetransact.uicomponent.CButton();
        PaymentDt = new com.see.truetransact.uicomponent.CDateField();
        lblPayDate = new com.see.truetransact.uicomponent.CLabel();
        txtServiceTax = new com.see.truetransact.uicomponent.CTextField();
        lblServiceTax = new com.see.truetransact.uicomponent.CLabel();
        lblDupRevValue = new com.see.truetransact.uicomponent.CLabel();
        rdoPayment = new com.see.truetransact.uicomponent.CRadioButton();
        rdoCancel = new com.see.truetransact.uicomponent.CRadioButton();
        panTransaction = new com.see.truetransact.uicomponent.CPanel();
        mbrMain = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptDelete = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Payment");
        setFont(new java.awt.Font("Microsoft Sans Serif", 0, 11)); // NOI18N
        setMinimumSize(new java.awt.Dimension(825, 650));
        setPreferredSize(new java.awt.Dimension(825, 650));

        tbrMain.setAlignmentY(0.5F);
        tbrMain.setEnabled(false);
        tbrMain.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 11)); // NOI18N
        tbrMain.setMinimumSize(new java.awt.Dimension(28, 28));
        tbrMain.setPreferredSize(new java.awt.Dimension(28, 28));

        btnView1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView1.setToolTipText("Enquiry");
        btnView1.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView1.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView1.setEnabled(false);
        btnView1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnView1ActionPerformed(evt);
            }
        });
        tbrMain.add(btnView1);

        lblSpace5.setText("     ");
        tbrMain.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrMain.add(btnNew);

        lblSpace17.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace17.setText("     ");
        lblSpace17.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMain.add(lblSpace17);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrMain.add(btnEdit);

        lblSpace18.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace18.setText("     ");
        lblSpace18.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMain.add(lblSpace18);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrMain.add(btnDelete);

        lblSpace1.setText("     ");
        tbrMain.add(lblSpace1);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrMain.add(btnSave);

        lblSpace19.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace19.setText("     ");
        lblSpace19.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMain.add(lblSpace19);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrMain.add(btnCancel);

        lblSpace2.setText("     ");
        tbrMain.add(lblSpace2);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrMain.add(btnAuthorize);

        lblSpace20.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace20.setText("     ");
        lblSpace20.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMain.add(lblSpace20);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrMain.add(btnException);

        lblSpace21.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace21.setText("     ");
        lblSpace21.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMain.add(lblSpace21);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrMain.add(btnReject);

        lblSpace4.setText("     ");
        tbrMain.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrMain.add(btnPrint);

        lblSpace22.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace22.setText("     ");
        lblSpace22.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMain.add(lblSpace22);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrMain.add(btnClose);

        getContentPane().add(tbrMain, java.awt.BorderLayout.NORTH);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace3.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace3, gridBagConstraints);

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

        panMain.setMinimumSize(new java.awt.Dimension(590, 400));
        panMain.setPreferredSize(new java.awt.Dimension(590, 400));
        panMain.setLayout(new java.awt.GridBagLayout());

        panInstrumentDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Instrument Details"));
        panInstrumentDetails.setMinimumSize(new java.awt.Dimension(760, 340));
        panInstrumentDetails.setPreferredSize(new java.awt.Dimension(760, 340));
        panInstrumentDetails.setLayout(new java.awt.GridBagLayout());

        panIssueCode.setLayout(new java.awt.GridBagLayout());

        lblIssueCode.setText("-");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panIssueCode.add(lblIssueCode, gridBagConstraints);

        lblIssueBankValue.setMaximumSize(new java.awt.Dimension(30, 21));
        lblIssueBankValue.setMinimumSize(new java.awt.Dimension(30, 21));
        lblIssueBankValue.setPreferredSize(new java.awt.Dimension(30, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panIssueCode.add(lblIssueBankValue, gridBagConstraints);

        lblBranchCodeValue.setMaximumSize(new java.awt.Dimension(80, 21));
        lblBranchCodeValue.setMinimumSize(new java.awt.Dimension(80, 21));
        lblBranchCodeValue.setPreferredSize(new java.awt.Dimension(80, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panIssueCode.add(lblBranchCodeValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panInstrumentDetails.add(panIssueCode, gridBagConstraints);

        lblInstrumentType.setText("Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panInstrumentDetails.add(lblInstrumentType, gridBagConstraints);

        cboInstrumentType.setMinimumSize(new java.awt.Dimension(101, 21));
        cboInstrumentType.setPopupWidth(120);
        cboInstrumentType.setPreferredSize(new java.awt.Dimension(101, 21));
        cboInstrumentType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboInstrumentTypeActionPerformed(evt);
            }
        });
        cboInstrumentType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboInstrumentTypeItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panInstrumentDetails.add(cboInstrumentType, gridBagConstraints);

        lblIssue.setText("Issue Bank - Branch Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 34, 1, 4);
        panInstrumentDetails.add(lblIssue, gridBagConstraints);

        lblSerialNumber.setText("Serial Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panInstrumentDetails.add(lblSerialNumber, gridBagConstraints);

        lblDateIssue.setText("Date of Issue");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panInstrumentDetails.add(lblDateIssue, gridBagConstraints);

        lblInstrumentNumber.setText("Instrument Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panInstrumentDetails.add(lblInstrumentNumber, gridBagConstraints);

        lblDateIssueValue.setMaximumSize(new java.awt.Dimension(100, 21));
        lblDateIssueValue.setMinimumSize(new java.awt.Dimension(100, 21));
        lblDateIssueValue.setPreferredSize(new java.awt.Dimension(100, 21));
        lblDateIssueValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                lblDateIssueValueFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panInstrumentDetails.add(lblDateIssueValue, gridBagConstraints);

        lblRemitPayId.setText("Remit PayId");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panInstrumentDetails.add(lblRemitPayId, gridBagConstraints);

        lblRemitPayIdValue.setMaximumSize(new java.awt.Dimension(100, 21));
        lblRemitPayIdValue.setMinimumSize(new java.awt.Dimension(100, 21));
        lblRemitPayIdValue.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panInstrumentDetails.add(lblRemitPayIdValue, gridBagConstraints);

        panInstrumentNumber.setLayout(new java.awt.GridBagLayout());

        txtNumber1.setEditable(false);
        txtNumber1.setMinimumSize(new java.awt.Dimension(50, 21));
        txtNumber1.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panInstrumentNumber.add(txtNumber1, gridBagConstraints);

        txtNumber2.setEditable(false);
        txtNumber2.setMinimumSize(new java.awt.Dimension(50, 21));
        txtNumber2.setPreferredSize(new java.awt.Dimension(50, 21));
        txtNumber2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNumber2FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInstrumentNumber.add(txtNumber2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panInstrumentDetails.add(panInstrumentNumber, gridBagConstraints);

        lblAddress.setText("Address");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panInstrumentDetails.add(lblAddress, gridBagConstraints);

        txtAddress.setMinimumSize(new java.awt.Dimension(101, 21));
        txtAddress.setPreferredSize(new java.awt.Dimension(101, 21));
        txtAddress.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panInstrumentDetails.add(txtAddress, gridBagConstraints);

        lblFavouring.setText("Favouring");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panInstrumentDetails.add(lblFavouring, gridBagConstraints);

        lblFavouringValue.setMaximumSize(new java.awt.Dimension(200, 21));
        lblFavouringValue.setMinimumSize(new java.awt.Dimension(200, 21));
        lblFavouringValue.setPreferredSize(new java.awt.Dimension(200, 21));
        lblFavouringValue.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                lblFavouringValueMouseMoved(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panInstrumentDetails.add(lblFavouringValue, gridBagConstraints);

        lblPayAmount.setText("Issue Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panInstrumentDetails.add(lblPayAmount, gridBagConstraints);

        txtPayAmount.setEditable(false);
        txtPayAmount.setMinimumSize(new java.awt.Dimension(101, 21));
        txtPayAmount.setPreferredSize(new java.awt.Dimension(101, 21));
        txtPayAmount.setValidation(new CurrencyValidation());
        txtPayAmount.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panInstrumentDetails.add(txtPayAmount, gridBagConstraints);

        lblCharges.setText("Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panInstrumentDetails.add(lblCharges, gridBagConstraints);

        txtCharges.setMinimumSize(new java.awt.Dimension(101, 21));
        txtCharges.setPreferredSize(new java.awt.Dimension(101, 21));
        txtCharges.setValidation(new NumericValidation());
        txtCharges.setEnabled(false);
        txtCharges.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtChargesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panInstrumentDetails.add(txtCharges, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panInstrumentDetails.add(lblRemarks, gridBagConstraints);

        txtRemarks.setMinimumSize(new java.awt.Dimension(101, 21));
        txtRemarks.setPreferredSize(new java.awt.Dimension(101, 21));
        txtRemarks.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panInstrumentDetails.add(txtRemarks, gridBagConstraints);

        lblPayStatus.setText("Transaction Mode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panInstrumentDetails.add(lblPayStatus, gridBagConstraints);

        cboPayStatus.setMinimumSize(new java.awt.Dimension(100, 21));
        cboPayStatus.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboPayStatusItemStateChanged(evt);
            }
        });
        cboPayStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPayStatusActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panInstrumentDetails.add(cboPayStatus, gridBagConstraints);

        cSeparator1.setMinimumSize(new java.awt.Dimension(100, 2));
        cSeparator1.setPreferredSize(new java.awt.Dimension(100, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 6, 4);
        panInstrumentDetails.add(cSeparator1, gridBagConstraints);

        lblPayableAmount.setText("Amount Payable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panInstrumentDetails.add(lblPayableAmount, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panInstrumentDetails.add(lblPayableAmountValue, gridBagConstraints);

        panSerialNo.setLayout(new java.awt.GridBagLayout());

        txtSerialNumber.setEditable(false);
        txtSerialNumber.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSerialNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSerialNumberActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSerialNo.add(txtSerialNumber, gridBagConstraints);

        btnSerialNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSerialNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSerialNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSerialNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panSerialNo.add(btnSerialNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panInstrumentDetails.add(panSerialNo, gridBagConstraints);

        lblAccHeadBalDisplay.setFont(new java.awt.Font("MS Sans Serif", 0, 12)); // NOI18N
        lblAccHeadBalDisplay.setMaximumSize(new java.awt.Dimension(60, 16));
        lblAccHeadBalDisplay.setMinimumSize(new java.awt.Dimension(60, 16));
        lblAccHeadBalDisplay.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panInstrumentDetails.add(lblAccHeadBalDisplay, gridBagConstraints);

        lblAccHeadBal.setText("Act. Head Balance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panInstrumentDetails.add(lblAccHeadBal, gridBagConstraints);

        lblAccHead.setText("Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panInstrumentDetails.add(lblAccHead, gridBagConstraints);

        lblAccHeadProdIdDisplay.setFont(new java.awt.Font("MS Sans Serif", 0, 12)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panInstrumentDetails.add(lblAccHeadProdIdDisplay, gridBagConstraints);

        btnView.setText("View");
        btnView.setMaximumSize(new java.awt.Dimension(63, 23));
        btnView.setMinimumSize(new java.awt.Dimension(63, 23));
        btnView.setPreferredSize(new java.awt.Dimension(63, 23));
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panInstrumentDetails.add(btnView, gridBagConstraints);

        PaymentDt.setMinimumSize(new java.awt.Dimension(100, 21));
        PaymentDt.setPreferredSize(new java.awt.Dimension(100, 21));
        PaymentDt.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panInstrumentDetails.add(PaymentDt, gridBagConstraints);

        lblPayDate.setText("Payment Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panInstrumentDetails.add(lblPayDate, gridBagConstraints);

        txtServiceTax.setMinimumSize(new java.awt.Dimension(101, 21));
        txtServiceTax.setPreferredSize(new java.awt.Dimension(101, 21));
        txtServiceTax.setValidation(new NumericValidation());
        txtServiceTax.setEnabled(false);
        txtServiceTax.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtServiceTaxFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panInstrumentDetails.add(txtServiceTax, gridBagConstraints);

        lblServiceTax.setText("Service Tax");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panInstrumentDetails.add(lblServiceTax, gridBagConstraints);

        lblDupRevValue.setText("DUP/REV");
        lblDupRevValue.setMaximumSize(new java.awt.Dimension(100, 14));
        lblDupRevValue.setMinimumSize(new java.awt.Dimension(100, 14));
        lblDupRevValue.setPreferredSize(new java.awt.Dimension(100, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panInstrumentDetails.add(lblDupRevValue, gridBagConstraints);

        cButtonGroup1.add(rdoPayment);
        rdoPayment.setText("Payment");
        rdoPayment.setMaximumSize(new java.awt.Dimension(68, 18));
        rdoPayment.setMinimumSize(new java.awt.Dimension(68, 18));
        rdoPayment.setPreferredSize(new java.awt.Dimension(77, 18));
        rdoPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPaymentActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panInstrumentDetails.add(rdoPayment, gridBagConstraints);

        cButtonGroup1.add(rdoCancel);
        rdoCancel.setText("Cancellation");
        rdoCancel.setMaximumSize(new java.awt.Dimension(85, 21));
        rdoCancel.setMinimumSize(new java.awt.Dimension(85, 21));
        rdoCancel.setPreferredSize(new java.awt.Dimension(100, 18));
        rdoCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoCancelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panInstrumentDetails.add(rdoCancel, gridBagConstraints);

        panMain.add(panInstrumentDetails, new java.awt.GridBagConstraints());

        panTransaction.setMinimumSize(new java.awt.Dimension(700, 400));
        panTransaction.setPreferredSize(new java.awt.Dimension(700, 400));
        panTransaction.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panMain.add(panTransaction, gridBagConstraints);

        getContentPane().add(panMain, java.awt.BorderLayout.CENTER);

        mnuProcess.setText("Process");
        mnuProcess.setToolTipText("Menu");

        mitNew.setText("New");
        mitNew.setToolTipText("");
        mitNew.setEnabled(false);
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.setEnabled(false);
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.setEnabled(false);
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);

        sptDelete.setEnabled(false);
        mnuProcess.add(sptDelete);

        mitSave.setText("Save");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancle");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);

        sptCancel.setEnabled(false);
        mnuProcess.add(sptCancel);

        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrMain.add(mnuProcess);

        setJMenuBar(mbrMain);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rdoCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCancelActionPerformed
        // TODO add your handling code here:
        ClientUtil.enableDisable(panSerialNo,true);
        btnSerialNo.setEnabled(true);
        observable.setCboPayStatus("Cancel");
        cboPayStatus.setEnabled(false);
        txtNumber1.setEnabled(true);
        txtNumber2.setEnabled(true);
        observable.setRdoCancel(true);
        observable.setRdoPayment(false);
        update(null,null);
        clearItems();
    }//GEN-LAST:event_rdoCancelActionPerformed

    private void rdoPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPaymentActionPerformed
        // TODO add your handling code here:
        ClientUtil.enableDisable(panSerialNo,true);
        btnSerialNo.setEnabled(true);
        observable.setCboPayStatus("Paid");
        cboPayStatus.setEnabled(false);
        txtNumber1.setEnabled(true);
        txtNumber2.setEnabled(true);
        observable.setRdoPayment(true);
        observable.setRdoCancel(false);
        update(null,null);
        clearItems();
    }//GEN-LAST:event_rdoPaymentActionPerformed
private void clearItems(){
    txtSerialNumber.setText("");
    txtNumber1.setText("");
    txtNumber2.setText("");
    txtPayAmount.setText("");
    txtCharges.setText("");
    txtServiceTax.setText("");
    lblPayableAmountValue.setText("");
    lblIssueBankValue.setText("");
    lblBranchCodeValue.setText("");
    lblFavouringValue.setText("");
    lblDateIssueValue.setText("");
    lblRemitPayIdValue.setText("");
}
    private void lblFavouringValueMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblFavouringValueMouseMoved
        // TODO add your handling code here:
        lblFavouringValue.setToolTipText(observable.getLblFavouring());
    }//GEN-LAST:event_lblFavouringValueMouseMoved

    private void txtServiceTaxFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtServiceTaxFocusLost
        // TODO add your handling code here:
        double amountPayable = 0 ; 
        amountPayable = CommonUtil.convertObjToDouble(txtPayAmount.getText()).doubleValue() - (CommonUtil.convertObjToDouble(txtCharges.getText()).doubleValue() + 
            CommonUtil.convertObjToDouble(txtServiceTax.getText()).doubleValue());
        lblPayableAmountValue.setText(String.valueOf(amountPayable));
        observable.setLblPayableAmount(lblPayableAmountValue.getText());
        transactionUI.setCallingAmount(lblPayableAmountValue.getText());
    }//GEN-LAST:event_txtServiceTaxFocusLost

    private void cboPayStatusItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboPayStatusItemStateChanged
        // TODO add your handling code here:
//            if(observable.getCbmPayStatus().getKeyForSelected().equals("CANCEL")){
//            txtCharges.setText(observable.executeQueryForCharge(CommonUtil.convertObjToStr((((ComboBoxModel)(cboInstrumentType).getModel())).getKeyForSelected()),getCategory(),txtPayAmount.getText(), "CANCELLATION_CHARGE",getCity(),lblIssueBankValue.getText(),lblBranchCodeValue.getText()));
//             txtChargesFocusLost();
//        } 
    }//GEN-LAST:event_cboPayStatusItemStateChanged
private void cboPayStatusItem(){
      if(observable.getCbmPayStatus().getKeyForSelected().equals("CANCEL")){
            txtCharges.setText(observable.executeQueryForCharge(CommonUtil.convertObjToStr((((ComboBoxModel)(cboInstrumentType).getModel())).getKeyForSelected()),getCategory(),txtPayAmount.getText(), "CANCELLATION_CHARGE",getCity(),lblIssueBankValue.getText(),lblBranchCodeValue.getText()));
             txtChargesFocusLost();
             observable.setTxtServiceTax(txtServiceTax.getText());
             observable.setTxtCharges(txtCharges.getText());
        } 
      if(observable.getCbmPayStatus().getKeyForSelected().equals("PAID")){
          txtChargesFocusLost();
             observable.setTxtServiceTax(txtServiceTax.getText());
             observable.setTxtCharges(txtCharges.getText());
      }
}
    private void btnView1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnView1ActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        popUpItems(VIEW1);
        btnCheck();
    }//GEN-LAST:event_btnView1ActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        if(view=="view"){
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
            HashMap ViewMap=new HashMap();
            HashMap where =new HashMap();
            ViewMap.put(CommonConstants.MAP_NAME,"getPaidIssues");
            String instType=null;
            if(CommonUtil.convertObjToStr(cboInstrumentType.getSelectedItem()).length()>0){
                instType = CommonUtil.convertObjToStr((((ComboBoxModel)(cboInstrumentType).getModel())).getKeyForSelected());
            where.put("INSTRU_TYPE", instType);
            if(CommonUtil.convertObjToStr(cboPayStatus.getSelectedItem()).length()>0)
                where.put("PAY_STATUS", CommonUtil.convertObjToStr((((ComboBoxModel)(cboPayStatus).getModel())).getKeyForSelected()));
            where.put("PAYMENT_DT",DateUtil.getDateMMDDYYYY(PaymentDt.getDateValue()));
            where.put("BRANCH_ID",TrueTransactMain.BRANCH_ID);
            ViewMap.put(CommonConstants.MAP_WHERE,where);
            System.out.println("ViewMap========"+ViewMap);
            new ViewAll(this, ViewMap).show();
            }else{
                ClientUtil.displayAlert("ProductID cannot be empty");
                ClientUtil.enableDisable(this,false);
                view=null;
            }
//            ClientUtil.enableDisable(this,false);
//            view=null;
        }
        else{
            btnCancelActionPerformed(null);
            btnView.setEnabled(true); 
            view="view";
            ClientUtil.enableDisable(this,false);
            PaymentDt.setEnabled(true);
            PaymentDt.setDateValue("");
            cboInstrumentType.setEnabled(true);
            cboPayStatus.setEnabled(true);
            btnAuthorize.setEnabled(false);
            btnReject.setEnabled(false);
            btnException.setEnabled(false);
            btnDelete.setEnabled(false);
            btnEdit.setEnabled(false);
            btnNew.setEnabled(false);
            btnCancel.setEnabled(true);
        }
    }//GEN-LAST:event_btnViewActionPerformed

    private void txtSerialNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSerialNumberActionPerformed
        // TODO add your handling code here:
       
        HashMap hash = new HashMap();
        HashMap where = new HashMap();
        where.put("VARIABLE_NO", txtSerialNumber.getText());
        where.put("PROD_ID", ((ComboBoxModel)cboInstrumentType.getModel()).getKeyForSelected());
        java.util.List lst = ClientUtil.executeQuery("getVariableNum",where);
        where = null;
        System.out.println("!!!!!######lst"+ lst );
        viewType=SERIALNUM;
        int lstSize = lst.size();
         if(lstSize > 0){
             HashMap obj=(HashMap)lst.get(0);
             fillData(obj);
         }
        else{
           ClientUtil.displayAlert("Enter Correct Variable Number");
           txtSerialNumber.setText("");
           clearItems();
        }
     
    }//GEN-LAST:event_txtSerialNumberActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
         HashMap reportParamMap = new HashMap();
 com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnSerialNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSerialNoActionPerformed
        getIssueDetails();
        transactionUI.resetObjects();
    }//GEN-LAST:event_btnSerialNoActionPerformed
    
    private void cboInstrumentTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboInstrumentTypeItemStateChanged
        
    }//GEN-LAST:event_cboInstrumentTypeItemStateChanged
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
    private void getIssueDetails(){
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
            ComboBoxModel cbModel = (ComboBoxModel)cboInstrumentType.getModel();
            if(cbModel.getKeyForSelected()!=null && !cbModel.getKeyForSelected().equals("")){
                popUpItems(NEW);
            }
        }
    }
    
    private double getCancelCharges(){
        HashMap where = new HashMap();
        where.put("PROD_ID", ((ComboBoxModel)cboInstrumentType.getModel()).getKeyForSelected());
        java.util.List chargeList = ClientUtil.executeQuery("getCancelCharges", where);
        where = null;
        if(chargeList != null && chargeList.size() > 0 ){
            return CommonUtil.convertObjToDouble(chargeList.get(0)).doubleValue();
        }
        else{
            return 0 ;
        }
        
    }
    
    private void cboPayStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPayStatusActionPerformed
        if(observable.getCbmPayStatus().getKeyForSelected().equals("CANCEL") && observable.getActionType()==ClientConstants.ACTIONTYPE_NEW){
//            txtCharges.setText(String.valueOf(getCancelCharges()));
            txtCharges.setEnabled(true);
            txtCharges.setEditable(true);
            txtServiceTax.setEnabled(true);
            txtServiceTax.setEditable(true);
        }
        else if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW){
            txtCharges.setText("0");
            txtCharges.setEnabled(false);
            txtServiceTax.setText("0");
            txtCharges.setEditable(false);
            txtServiceTax.setEnabled(false);
            txtServiceTax.setEditable(false);
            lblPayableAmountValue.setText(observable.getTxtPayAmount());
            transactionUI.setCallingAmount(lblPayableAmountValue.getText());
        }
//        txtChargesFocusLost(null);
    }//GEN-LAST:event_cboPayStatusActionPerformed
    
    private void txtChargesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtChargesFocusLost
//        double amountPayable = 0 ;
//        if(txtCharges.getText().length() > 0  && !txtCharges.getText().equals("")){
//            txtServiceTax.setText(observable.calServiceTax(txtCharges.getText(),CommonUtil.convertObjToStr((((ComboBoxModel)(cboInstrumentType).getModel())).getKeyForSelected()),getCategory(),txtPayAmount.getText(), "CANCELLATION_CHARGE",getCity(),lblIssueBankValue.getText(),lblBranchCodeValue.getText()));
//            amountPayable = CommonUtil.convertObjToDouble(txtPayAmount.getText()).doubleValue() - (CommonUtil.convertObjToDouble(txtCharges.getText()).doubleValue() + 
//            CommonUtil.convertObjToDouble(txtServiceTax.getText()).doubleValue());
//            lblPayableAmountValue.setText(String.valueOf(amountPayable));
//        }else{
//            lblPayableAmountValue.setText(String.valueOf(amountPayable));
//        }
//        observable.setLblPayableAmount(lblPayableAmountValue.getText());
//        transactionUI.setCallingAmount(lblPayableAmountValue.getText());
//        System.out.println("txtNumber1.getText() : " + txtNumber1.getText());
////        transactionUI.setCallingInst1(txtNumber1.getText());
////        transactionUI.setCallingInst2(txtNumber2.getText());
//        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW){
        txtChargesFocusLost();
       
//        }
    }//GEN-LAST:event_txtChargesFocusLost
    private void txtChargesFocusLost(){
//     if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW){   
         double amountPayable = 0 ;
        if(txtCharges.getText().length() > 0  && !txtCharges.getText().equals("")){
            txtServiceTax.setText(observable.calServiceTax(txtCharges.getText(),CommonUtil.convertObjToStr((((ComboBoxModel)(cboInstrumentType).getModel())).getKeyForSelected()),getCategory(),txtPayAmount.getText(), "CANCELLATION_CHARGE",getCity(),lblIssueBankValue.getText(),lblBranchCodeValue.getText()));
            amountPayable = CommonUtil.convertObjToDouble(txtPayAmount.getText()).doubleValue() - (CommonUtil.convertObjToDouble(txtCharges.getText()).doubleValue() + 
            CommonUtil.convertObjToDouble(txtServiceTax.getText()).doubleValue());
            lblPayableAmountValue.setText(String.valueOf(amountPayable));
        }else{
            lblPayableAmountValue.setText(String.valueOf(amountPayable));
        }
        observable.setLblPayableAmount(lblPayableAmountValue.getText());
        transactionUI.setCallingAmount(lblPayableAmountValue.getText());
        System.out.println("txtNumber1.getText() : " + txtNumber1.getText());
//        transactionUI.setCallingInst1(txtNumber1.getText());
//        transactionUI.setCallingInst2(txtNumber2.getText());
//     }
    }
    private void resetVal(){
        //To be called on change of Prod id.... This is not the general form reset
        //Use observable.reset form for all fields reset....
        txtSerialNumber.setEditable(true);
        lblIssueCode.setText("");
        lblIssueBankValue.setText("");
        lblBranchCodeValue.setText("");
        txtNumber1.setText("");
        txtNumber2.setText("");
        txtCharges.setText("");
        txtServiceTax.setText("");
        txtRemarks.setText("");
        txtPayAmount.setText("");
        lblPayableAmountValue.setText("");
        observable.setLblPayableAmount("");
        lblDateIssueValue.setText("");
        lblFavouringValue.setText("");
        txtSerialNumber.setText("");
        txtAddress.setText("");
        cboPayStatus.setSelectedItem("");
    }
    private void updateAcctHeadBal(){
        lblAccHeadProdIdDisplay.setText(observable.getLblAccHeadProdIdDisplay());
        lblAccHeadBalDisplay.setText(observable.getLblAccHeadBalDisplay());
    }
    private void cboInstrumentTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboInstrumentTypeActionPerformed
        
//        resetVal();
        transactionUI.setCallingProdID(CommonUtil.convertObjToStr((((ComboBoxModel)(cboInstrumentType).getModel())).getKeyForSelected()));
        observable.setCboInstrumentType(CommonUtil.convertObjToStr(cboInstrumentType.getSelectedItem()));
        String prodID = observable.getCboInstrumentType();
        if( prodID != null ){
            prodID = CommonUtil.convertObjToStr((((ComboBoxModel)(cboInstrumentType).getModel())).getKeyForSelected());
        }
        if(prodID.length() > 0){
            observable.getAccountHeadForProduct(prodID);
            updateAcctHeadBal();
        }else if(observable.getCboInstrumentType().length() == 0){
            observable.setLblAccHeadProdIdDisplay("");
            observable.setLblAccHeadBalDisplay("");
            updateAcctHeadBal();
        }
    }//GEN-LAST:event_cboInstrumentTypeActionPerformed
    
    private void txtNumber2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumber2FocusLost
        // TODO add your handling code here:
//        if(txtSerialNumber.getText().length() > 0  && !txtSerialNumber.getText().equals("")){
//            HashMap resultMap = new HashMap();
//            resultMap = observable.getResultMap("getIssueDetails", txtSerialNumber.getText());
//            String number1 = new String();
//            String number2 = new String();
//            if(resultMap != null){
//                number1 = CommonUtil.convertObjToStr(resultMap.get("INSTRUMENT_NO1"));
//                number2 = CommonUtil.convertObjToStr(resultMap.get("INSTRUMENT_NO2"));
//                observable.setDuplicateDt(DateUtil.getStringDate((Date)resultMap.get("DUPLICATE_DT")));
//            }
//            if( !txtNumber1.getText().equals(number1) || !txtNumber2.getText().equals(number2) ){
//                try{
//                    ShowDialogue("printedNumberMsg");
//                    txtNumber1.setText("");
//                    txtNumber2.setText("");
//                }catch(Exception e){
//                }
//            }
//        }
        if((!txtNumber1.getText().equals("")) && (!txtNumber2.getText().equals(""))){
            HashMap param = new HashMap();
            param.put("INSTRUMENT_NO1", CommonUtil.convertObjToStr(txtNumber1.getText()));
            param.put("INSTRUMENT_NO2", CommonUtil.convertObjToStr(txtNumber2.getText()));
            param.put("PROD_ID", ((ComboBoxModel)cboInstrumentType.getModel()).getKeyForSelected());
            param.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            param.put("BANK_CODE", TrueTransactMain.BANK_ID);
        if(rdoPayment.isSelected()){
            List lst = ClientUtil.executeQuery("getPayableIssuesValid", param);
            if(lst != null && lst.size()>0){
                HashMap obj=(HashMap)lst.get(0);
                  HashMap parMap = new HashMap();
//            boolean isLapsed = false;
            parMap.put("PROD_ID", obj.get("PROD_ID"));
            Date issueDate = (Date)obj.get("BATCH_DT");
            Date curDt = (Date) currDt.clone();
            List lapseLst = (List) ClientUtil.executeQuery("getLapsePrd", parMap);
            parMap = null;
            if(lapseLst != null && lapseLst.size() > 0){
                parMap = (HashMap)lapseLst.get(0);
                double period = CommonUtil.convertObjToDouble(parMap.get("LAPSE_PERIOD")).doubleValue();
                double days = (curDt.getTime() - issueDate.getTime())/1000/60/60/24;
                if(days > period){
                    ShowDialogue("warningMsg");
//                    warningMsg = new String(resourceBundle.getString("warningMsg"));
//                    isLapsed = true;
                   btnSave.setEnabled(false); 
//                   this.transactionUI = transactionUI;
//                   transactionUI.setMainEnableDisable(false);
                   cboPayStatus.setEnabled(false);
                   txtRemarks.setEditable(false);
                   txtRemarks.setEnabled(false);
                }else{
//                    this.transactionUI = transactionUI;
//                    transactionUI.setButtonEnableDisable(true);
                     btnSave.setEnabled(true);
                     cboPayStatus.setEnabled(true);
                     txtRemarks.setEditable(true);
                     txtRemarks.setEnabled(true);
                }
            }
                fillData(obj);
            }else{
                ClientUtil.showMessageWindow("Invalid Instrument Number");
                clearItems();
            }
        }else if(rdoCancel.isSelected()){
            List lst = ClientUtil.executeQuery("getCancelIssuesValid", param);
            if(lst != null && lst.size()>0){
                HashMap obj=(HashMap)lst.get(0);
                fillData(obj);
            }else{
                ClientUtil.showMessageWindow("Invalid Instrument Number");
                clearItems();
            }
        }else{
            ClientUtil.showAlertWindow("Select Payment or Cancel");
        }
        }else{
            ClientUtil.showAlertWindow("Enter Instrument Prefix");
        }
    }//GEN-LAST:event_txtNumber2FocusLost
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_REJECT);
        btnNew.setEnabled(false);
        btnSave.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        txtCharges.setEnabled(false);
        txtServiceTax.setEnabled(false);
        
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
         transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_EXCEPTION);
        btnNew.setEnabled(false);
        btnSave.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        txtCharges.setEnabled(false);
        txtServiceTax.setEnabled(false);
        
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
        btnNew.setEnabled(false);
        btnSave.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        txtCharges.setEnabled(false);
        txtServiceTax.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    private void lblDateIssueValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_lblDateIssueValueFocusLost
        // TODO add your handling code here:
//        Date issueDt = currDt.clone();
//        if(!lblDateIssueValue.getText().equals("") && lblDateIssueValue.getText().length() > 0){
//            issueDt = DateUtil.getDateMMDDYYYY(lblDateIssueValue.getText());
//        }
//        Date currentDt = currDt.clone();
//        if(!txtSerialNumber.getText().equals("") && txtSerialNumber.getText().length() > 0){
//            HashMap resultMap = observable.getResultMap("getElapsedPeriod", txtSerialNumber.getText());
//            if(resultMap != null){
//                double period = CommonUtil.convertObjToDouble(resultMap.get("LAPSE_PERIOD")).doubleValue();
//                double days = (currentDt.getTime() - issueDt.getTime())/1000/60/60/24;
//                if(days > period){
//                    try{
//                        ShowDialogue("warningMsg");
//                        lblDateIssueValue.setText("");
//                        warningMsg = new String(resourceBundle.getString("warningMsg"));
//                    }catch(Exception e){
//                    }
//                }
//            }
//        }
        
    }//GEN-LAST:event_lblDateIssueValueFocusLost
      
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        observable.resetForm();
        resetLabel();
        observable.resetStatus();
        if(observable.getAuthorizeStatus()!=null)
            super.removeEditLock(lblRemitPayIdValue.getText());    
        setButtonEnableDisable();
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        btnSerialNo.setEnabled(false);
        btnView.setEnabled(true);
        setCategory("");
        setCity("");
        //btnNew.setEnabled(false);
        //btnSave.setEnabled(false);
        //btnEdit.setEnabled(false);
        //btnCancel.setEnabled(true);
        ClientUtil.enableDisable(this, false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setMainEnableDisable(false);
        isFilled = false;
        setModified(false);
//        rdoCancel.setSelected(false);
//        rdoPayment.setSelected(false);
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        btnSaveActionPerformed(evt);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        
        int transactionSize = 0 ;
        if(transactionUI.getOutputTO() == null){
            ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS)) ;
        }
        else{
            transactionSize = (transactionUI.getOutputTO()).size();
            observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
        }
        
        if(transactionSize == 0){
            ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS)) ;
        }
        else if(transactionSize != 0){
            if ((!transactionUI.isBtnSaveTransactionDetailsFlag()) && observable.getActionType()!=ClientConstants.ACTIONTYPE_DELETE) {
                ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.SAVE_TX_DETAILS));
            }
            else{
                updateOBFields();
                HashMap mapData = new HashMap();
                double transTotalAmt = CommonUtil.convertObjToDouble(transactionUI.getTransactionOB().getLblTotalTransactionAmtVal()).doubleValue();
                double totalClosingAmt = CommonUtil.convertObjToDouble(observable.getLblPayableAmount()).doubleValue();
                
                mapData.put("VARIABLE_NO",CommonUtil.convertObjToStr(observable.getTxtSerialNumber()));
                List lst = (List) ClientUtil.executeQuery("checkStopPayment", mapData);
                mapData = null;
                System.out.println("totalClosingAmt : " + totalClosingAmt);
                if (ClientUtil.checkTotalAmountTallied(totalClosingAmt, transTotalAmt) == false)
                    ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NOT_TALLY));
                else{
                    
                    String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panMain);
                    StringBuffer message = new StringBuffer(mandatoryMessage + warningMsg);
                    
                    /* mandatoryMessage length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
                    if(message.length() > 0 && !message.equals("")){
                        displayAlert(message.toString());
                    }
                    else if(lst != null)
                        if(lst.size() > 0){
                            mapData = (HashMap) lst.get(0);
                            if(CommonUtil.convertObjToStr(mapData.get("STOP_STATUS")).equalsIgnoreCase("STOPPED")){
                                if((CommonUtil.convertObjToStr(mapData.get("STOP_STATUS")).equalsIgnoreCase("STOPPED")) &&
                                (CommonUtil.convertObjToStr(mapData.get("AUTHORIZE_STATUS")).equalsIgnoreCase(""))){
                                    ClientUtil.showAlertWindow(cboInstrumentType.getSelectedItem()+" stopped and waiting for verification");
                                }
                                else{
                                    ClientUtil.showAlertWindow(cboInstrumentType.getSelectedItem()+" stopped");
                                }
                            }else{
                                if((CommonUtil.convertObjToStr(mapData.get("STOP_STATUS")).equalsIgnoreCase("REVOKED")) &&
                                (CommonUtil.convertObjToStr(mapData.get("AUTHORIZE_STATUS")).equalsIgnoreCase(""))){
                                    ClientUtil.showAlertWindow(cboInstrumentType.getSelectedItem()+" revoked and waiting for verification");
                                }else if((CommonUtil.convertObjToStr(mapData.get("STOP_STATUS")).equalsIgnoreCase("REVOKED")) &&
                                (CommonUtil.convertObjToStr(mapData.get("AUTHORIZE_STATUS")).equalsIgnoreCase("REJECTED"))){
                                    ClientUtil.showAlertWindow(cboInstrumentType.getSelectedItem()+" stopped");
                                }
                            }
                        }
                    
                        else {
                            savePerformed();
                            setModified(false);
                        }
                    message = null;
                }
            }
        }
        transactionUI.setSaveEnableValue(0);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        btnSerialNo.setEnabled(false);
        PaymentDt.setDateValue(" ");
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_DELETE);
        popUpItems(DELETE);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        ClientUtil.enableDisable(this, false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_EDIT);
        observable.setStatus();
        popUpItems(EDIT);
        txtCharges.setEditable(true);
        txtServiceTax.setEditable(true);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        //observable.resetForm();
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.setStatus();
        observable.setOldTransDetMap(null);
        ClientUtil.enableDisable(this, true);
        setButtonEnableDisable();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        txtSerialNumber.setEditable(true);
        btnSerialNo.setEnabled(true);
        PaymentDt.setEnabled(false);
        btnView.setEnabled(true);
        cboPayStatus.setSelectedItem(observable.getCbmPayStatus().getDataForKey("PAID"));
        txtCharges.setText("0.00");
        txtCharges.setEditable(false);
        txtServiceTax.setText("0.00");
        txtServiceTax.setEditable(false);
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        ClientUtil.enableDisable(panSerialNo,false);
        btnSerialNo.setEnabled(false);
        txtPayAmount.setEnabled(false);
        cboPayStatus.setEnabled(false);
        txtNumber1.setEnabled(false);
        txtNumber2.setEnabled(false);
    }//GEN-LAST:event_btnNewActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    
    /** This method helps in popoualting the data from the data base
     * @param Action the argument is passed according to the command issued
     */
    private void popUpItems(int Action) {
        final HashMap viewMap = new HashMap();
        ACTION=Action;
        if(Action == NEW){
            if(rdoPayment.isSelected()){
                HashMap where = new HashMap();
                where.put("PROD_ID", ((ComboBoxModel)cboInstrumentType.getModel()).getKeyForSelected());
                where.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                where.put("BANK_CODE", TrueTransactMain.BANK_ID);
                viewMap.put(CommonConstants.MAP_WHERE, where);
                where = null ;
                viewMap.put(CommonConstants.MAP_NAME, "getPayableIssues");
            }else if(rdoCancel.isSelected()){
                HashMap where = new HashMap();
                where.put("PROD_ID", ((ComboBoxModel)cboInstrumentType.getModel()).getKeyForSelected());
                where.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                where.put("BANK_CODE", TrueTransactMain.BANK_ID);
                viewMap.put(CommonConstants.MAP_WHERE, where);
                where = null ;
                viewMap.put(CommonConstants.MAP_NAME, "getCancelIssues");
            }else{
                ClientUtil.showAlertWindow("Select Payment Or Cancellation option");
            }
        }
        else if (Action == EDIT || Action == DELETE || Action == VIEW1){
            ArrayList lst = new ArrayList();
            lst.add("REMIT_PAY_ID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            lblStatus.setText(ClientConstants.ACTION_STATUS[0]);
            viewMap.put(CommonConstants.MAP_NAME, "ViewAllRemittancePaymentTO");
        }
        updateOBFields();
        new ViewAll(this, viewMap).show();
    }
    
    /** This method helps in filling the data frm the data base to respective txt fields
     * @param param The selected data from the viewAll() is passed as a param
     */
    public void fillData(Object param) {
        //System.out.println("~~param: " + param);
         final HashMap hash = (HashMap) param;
        System.out.println("~~~~~~~~~~hash : " + hash);
        setCity(CommonUtil.convertObjToStr(hash.get("CITY")));
        setCategory(CommonUtil.convertObjToStr(hash.get("CATEGORY")));
        ClientUtil.enableDisable(panInstrumentDetails, true);
        if(ACTION == NEW || viewType == NEW){
            System.out.println("hash : " + hash);
            if(rdoPayment.isSelected()){
            HashMap parMap = new HashMap();
//            boolean isLapsed = false;
            parMap.put("PROD_ID", hash.get("PROD_ID"));
            Date issueDate = (Date)hash.get("BATCH_DT");
            Date curDt = (Date) currDt.clone();
            List lapseLst = (List) ClientUtil.executeQuery("getLapsePrd", parMap);
            parMap = null;
            if(lapseLst != null && lapseLst.size() > 0){
                parMap = (HashMap)lapseLst.get(0);
                double period = CommonUtil.convertObjToDouble(parMap.get("LAPSE_PERIOD")).doubleValue();
                double days = (curDt.getTime() - issueDate.getTime())/1000/60/60/24;
                if(days > period){
                    ShowDialogue("warningMsg");
//                    warningMsg = new String(resourceBundle.getString("warningMsg"));
//                    isLapsed = true;
                   btnSave.setEnabled(false); 
//                   this.transactionUI = transactionUI;
//                   transactionUI.setMainEnableDisable(false);
                   cboPayStatus.setEnabled(false);
                   txtRemarks.setEditable(false);
                   txtRemarks.setEnabled(false);
                }else{
//                    this.transactionUI = transactionUI;
//                    transactionUI.setButtonEnableDisable(true);
                     btnSave.setEnabled(true);
                     cboPayStatus.setEnabled(true);
                     txtRemarks.setEditable(true);
                     txtRemarks.setEnabled(true);
                }
            }
            }
       
       
//        if(!txtSerialNumber.getText().equals("") && txtSerialNumber.getText().length() > 0){
//            HashMap resultMap = observable.getResultMap("getElapsedPeriod", txtSerialNumber.getText());
//            if(resultMap != null){
//                double period = CommonUtil.convertObjToDouble(resultMap.get("LAPSE_PERIOD")).doubleValue();
//                double days = (currentDt.getTime() - issueDt.getTime())/1000/60/60/24;
//                if(days > period){
//                    try{
//                        ShowDialogue("warningMsg");
//                        lblDateIssueValue.setText("");
//                        warningMsg = new String(resourceBundle.getString("warningMsg"));
//                    }catch(Exception e){
//                    }
//                }
//            }
//        }
//            parMap = null;
//            if(!isLapsed){
                observable.setCboInstrumentType((String)((ComboBoxModel) cboInstrumentType.getModel()).getKeyForSelected());
                observable.setTxtSerialNumber(CommonUtil.convertObjToStr(hash.get("VARIABLE_NO")));
                observable.setTxtPayAmount(CommonUtil.convertObjToStr(hash.get("AMOUNT")));
                observable.setTxtNumber1(CommonUtil.convertObjToStr(hash.get("INSTRUMENT_NO1")));
                observable.setTxtNumber2(CommonUtil.convertObjToStr(hash.get("INSTRUMENT_NO2")));
                observable.setLblFavouring(CommonUtil.convertObjToStr(hash.get("FAVOURING")));
                observable.setLblDateIssue(CommonUtil.convertObjToStr(hash.get("BATCH_DT")));
                observable.setLblIssueBank(CommonUtil.convertObjToStr(hash.get("DRAWEE_BANK")));
                observable.setLblBranchCode(CommonUtil.convertObjToStr(hash.get("DRAWEE_BRANCH_CODE")));
                if((CommonUtil.convertObjToStr(hash.get("REMARKS")).equals("DUPLICATE")) || (CommonUtil.convertObjToStr(hash.get("REMARKS")).equals("REVALIDATED"))){
                    observable.setLblDupRevValue(CommonUtil.convertObjToStr(hash.get("REMARKS")));
                    if(CommonUtil.convertObjToStr(hash.get("REMARKS")).equals("DUPLICATE"))
                        lblDateIssue.setText("Duplicate Date");
                    else
                        lblDateIssue.setText("Revalidated Date");
                }else{
                    lblDateIssue.setText(resourceBundle.getString("lblDateIssue"));
                    observable.setLblDupRevValue("");
                }
                double amountLocal = CommonUtil.convertObjToDouble(hash.get("AMOUNT")).doubleValue();
//                observable.setTxtCharges("0");
                observable.setLblPayableAmount(String.valueOf(amountLocal));
                transactionUI.setSaveEnableValue(1);
                btnSerialNo.setEnabled(true);
                txtChargesFocusLost(null);
               
//            }
                if(rdoPayment.isSelected()){
                    observable.setCboPayStatus("Paid");
//                    cboPayStatus.setSelectedItem("Paid");
                }else if(rdoCancel.isSelected()){
                    observable.setCboPayStatus("Cancel");
//                    cboPayStatus.setSelectedItem("Cancel");
                }else{
                    ClientUtil.showAlertWindow("Select Payment Or Cancellation option");
                }
                update(null, null);
                cboPayStatusItem();
                cboPayStatus.setEnabled(false);
                PaymentDt.setEnabled(false);
        }
        
        if(ACTION == EDIT || ACTION == DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT
        || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION || ACTION == VIEW1){
            isFilled = true;
            actionEditDelete(hash);
            setButtonEnableDisable();
            cboInstrumentType.setEnabled(false);
            btnSerialNo.setEnabled(false);
            cboPayStatus.setEnabled(false);
            PaymentDt.setEnabled(false);
            rdoPayment.setEnabled(false);
            rdoCancel.setEnabled(false);
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT
            || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION){
                ClientUtil.enableDisable(panInstrumentDetails,false);
                 btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                 btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                 btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
            }
//            lblPayableAmountValue.setText(String.valueOf(CommonUtil.convertObjToDouble(observable.getTxtPayAmount())));
//            lblPayableAmountValue.setText(CommonUtil.convertObjToDouble(observable.getTxtPayAmount()));
//            observable.setLblPayableAmount(String.valueOf(CommonUtil.convertObjToDouble(observable.getTxtPayAmount())));
        if(observable.getCboPayStatus().equals("Cancel")){
            txtCharges.setEnabled(true);
            txtServiceTax.setEnabled(true);
        }else{
            txtCharges.setEnabled(false);
            txtServiceTax.setEnabled(false);
        }
        txtSerialNumber.setEnabled(false);
        txtNumber1.setEnabled(false);
        txtNumber2.setEnabled(false);
        }
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
            ClientUtil.enableDisable(this,false);
        }
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        if(view=="view"){
            observable.setActionType(ClientConstants.ACTIONTYPE_VIEW_MODE);
            ClientUtil.enableDisable(this,false);
            btnDelete.setEnabled(false);
            btnEdit.setEnabled(false);
            btnNew.setEnabled(false);
            btnSave.setEnabled(false);
            btnAuthorize.setEnabled(false);
            btnReject.setEnabled(false);
            btnException.setEnabled(false);
            btnCancel.setEnabled(true);
            observable.setLblStatus("View");
            lblStatus.setText(observable.getLblStatus());
            view=null;
        }
        txtPayAmount.setEnabled(false);
    }
    
    /*To get the data and populating on the screen,set the status and enabling the apt components*/
    /** To get the data and populating on the screen
     * @param hash the hash map to
     */
    private void actionEditDelete(HashMap hash){
        observable.setStatus();
        hash.put(CommonConstants.MAP_WHERE, hash.get("REMIT_PAY_ID"));
        observable.populateData(hash);
        //txtChargesFocusLost(null);
    }
    
    private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
     private void removeRadioButtons() {
        cButtonGroup1.remove(rdoPayment);
        cButtonGroup1.remove(rdoCancel);
//        cButtonGroup2.remove(cRadio_DB_Yes);
//        cButtonGroup2.remove(cRadio_DB_No);
    }
    
    // b.) To Add the Radio buttons...
    private void addRadioButtons() {
        cButtonGroup1 = new CButtonGroup();
        cButtonGroup1.add(rdoPayment);
        cButtonGroup1.add(rdoCancel);
//        cButtonGroup2.add(cRadio_DB_Yes);
//        cButtonGroup2.add(cRadio_DB_No);
    }
    private void savePerformed(){
        updateOBFields();
        observable.doAction();
        if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
            HashMap lockMap = new HashMap();
            ArrayList lst = new ArrayList();
            lst.add("REMIT_PAY_ID");
            lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
            if (observable.getProxyReturnMap()!=null) {
                if (observable.getProxyReturnMap().containsKey("REMIT_PAY_ID")) {
                    lockMap.put("REMIT_PAY_ID", observable.getProxyReturnMap().get("REMIT_PAY_ID"));
                }
            }
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                lockMap.put("REMIT_PAY_ID", observable.getLblRemitPayId());
            }
            setEditLockMap(lockMap);
            setEditLock();
        }
        ClientUtil.enableDisable(this, false);
        isFilled=false;
        observable.setResultStatus();
        observable.resetForm();
        resetLabel();
        setButtonEnableDisable();
        ClientUtil.enableDisable(this, false);
        transactionUI.resetObjects();
    }
    
    private void updateOBFields() {
        /* display on UI for combo box */
        observable.setRdoCancel(rdoCancel.isSelected());
        observable.setRdoPayment(rdoPayment.isSelected());
        observable.setCboInstrumentType((String) cboInstrumentType.getSelectedItem());
        
        /* display on UI  for text box */
        observable.setLblRemitPayId(lblRemitPayIdValue.getText());
        observable.setTxtSerialNumber(txtSerialNumber.getText());
        observable.setTxtCharges(txtCharges.getText());
        observable.setTxtServiceTax(txtServiceTax.getText());
        observable.setTxtPayAmount(txtPayAmount.getText());
        observable.setLblPayableAmount(lblPayableAmountValue.getText());
        observable.setTxtRemarks(txtRemarks.getText());
        observable.setTxtAddress(txtAddress.getText());
        observable.setTxtNumber1(txtNumber1.getText());
        observable.setTxtNumber2(txtNumber2.getText());
        observable.setLblFavouring(lblFavouringValue.getText());
        observable.setLblDateIssue(lblDateIssueValue.getText());
        observable.setLblIssueBank(lblIssueBankValue.getText());
        observable.setLblBranchCode(lblBranchCodeValue.getText());
        observable.setPaymentDt(PaymentDt.getDateValue());
        observable.setModule(getModule());
        observable.setScreen(getScreen());
    }
    
    
    /*This method performs enable and the disable of the necessary buttons*/
    private void setButtonEnableDisable() {
        
        /*btnNew.setEnabled(!btnNew.isEnabled());
        btnSave.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(btnNew.isEnabled());
        btnDelete.setEnabled(btnNew.isEnabled());
        mitNew.setEnabled(!mitNew.isEnabled());
        mitEdit.setEnabled(mitNew.isEnabled());
        mitDelete.setEnabled(mitNew.isEnabled());
         
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(!mitNew.isEnabled());
        mitCancel.setEnabled(!mitNew.isEnabled());
        lblStatus.setText(observable.getLblStatus());*/
        btnNew.setEnabled(!btnNew.isEnabled());
        btnSave.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(btnNew.isEnabled());
        btnDelete.setEnabled(btnNew.isEnabled());
        mitNew.setEnabled(!mitNew.isEnabled());
        mitEdit.setEnabled(mitNew.isEnabled());
        mitDelete.setEnabled(mitNew.isEnabled());
        
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(!mitNew.isEnabled());
        mitCancel.setEnabled(!mitNew.isEnabled());
        lblStatus.setText(observable.getLblStatus());
    }
    
    /**
     * Getter for property city.
     * @return Value of property city.
     */
    public java.lang.String getCity() {
        return city;
    }
    
    /**
     * Setter for property city.
     * @param city New value of property city.
     */
    public void setCity(java.lang.String city) {
        this.city = city;
    }
    
    /**
     * Getter for property category.
     * @return Value of property category.
     */
    public java.lang.String getCategory() {
        return category;
    }
    
    /**
     * Setter for property category.
     * @param category New value of property category.
     */
    public void setCategory(java.lang.String category) {
        this.category = category;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CDateField PaymentDt;
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
    private com.see.truetransact.uicomponent.CButton btnSerialNo;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CButton btnView1;
    private com.see.truetransact.uicomponent.CButtonGroup cButtonGroup1;
    private com.see.truetransact.uicomponent.CSeparator cSeparator1;
    private com.see.truetransact.uicomponent.CComboBox cboInstrumentType;
    private com.see.truetransact.uicomponent.CComboBox cboPayStatus;
    private com.see.truetransact.uicomponent.CLabel lblAccHead;
    private com.see.truetransact.uicomponent.CLabel lblAccHeadBal;
    private javax.swing.JLabel lblAccHeadBalDisplay;
    private javax.swing.JLabel lblAccHeadProdIdDisplay;
    private com.see.truetransact.uicomponent.CLabel lblAddress;
    private com.see.truetransact.uicomponent.CLabel lblBranchCodeValue;
    private com.see.truetransact.uicomponent.CLabel lblCharges;
    private com.see.truetransact.uicomponent.CLabel lblDateIssue;
    private com.see.truetransact.uicomponent.CLabel lblDateIssueValue;
    private com.see.truetransact.uicomponent.CLabel lblDupRevValue;
    private com.see.truetransact.uicomponent.CLabel lblFavouring;
    private com.see.truetransact.uicomponent.CLabel lblFavouringValue;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentNumber;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentType;
    private com.see.truetransact.uicomponent.CLabel lblIssue;
    private com.see.truetransact.uicomponent.CLabel lblIssueBankValue;
    private com.see.truetransact.uicomponent.CLabel lblIssueCode;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblPayAmount;
    private com.see.truetransact.uicomponent.CLabel lblPayDate;
    private com.see.truetransact.uicomponent.CLabel lblPayStatus;
    private com.see.truetransact.uicomponent.CLabel lblPayableAmount;
    private com.see.truetransact.uicomponent.CLabel lblPayableAmountValue;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblRemitPayId;
    private com.see.truetransact.uicomponent.CLabel lblRemitPayIdValue;
    private com.see.truetransact.uicomponent.CLabel lblSerialNumber;
    private com.see.truetransact.uicomponent.CLabel lblServiceTax;
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
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panInstrumentDetails;
    private com.see.truetransact.uicomponent.CPanel panInstrumentNumber;
    private com.see.truetransact.uicomponent.CPanel panIssueCode;
    private com.see.truetransact.uicomponent.CPanel panMain;
    private com.see.truetransact.uicomponent.CPanel panSerialNo;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTransaction;
    private com.see.truetransact.uicomponent.CRadioButton rdoCancel;
    private com.see.truetransact.uicomponent.CRadioButton rdoPayment;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private javax.swing.JToolBar tbrMain;
    private com.see.truetransact.uicomponent.CTextField txtAddress;
    private com.see.truetransact.uicomponent.CTextField txtCharges;
    private com.see.truetransact.uicomponent.CTextField txtNumber1;
    private com.see.truetransact.uicomponent.CTextField txtNumber2;
    private com.see.truetransact.uicomponent.CTextField txtPayAmount;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    private com.see.truetransact.uicomponent.CTextField txtSerialNumber;
    private com.see.truetransact.uicomponent.CTextField txtServiceTax;
    // End of variables declaration//GEN-END:variables
}