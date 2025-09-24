/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * .
 *
 * ShareProductUI.java
 *
 * Created on November 23, 2004, 4:00 PM
 */
package com.see.truetransact.ui.suspenseaccount;

/**
 *
 * @author
 *
 */
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.customer.IndividualCustUI;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.uicomponent.COptionPane;
import java.util.Observer;
import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SuspenseAccountMasterUI extends CInternalFrame implements UIMandatoryField, Observer {

    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.suspenseaccount.SuspenseAccountMasterRB", ProxyParameters.LANGUAGE);
    HashMap mandatoryMap = new HashMap();
    private SuspenseAccountMasterOB observable;
    SuspenseAccountMasterMRB objMandatoryRB;
    MandatoryCheck objMandatoryCheck = new MandatoryCheck();
    boolean flag = false;
    final int AUTHORIZE = 3;
    final int DELETE = 1;
    private String actionType = "";
    private int viewType = -1;
    private String view = "";
    boolean isFilled = false;
    final int LIABILITYGL = 8;
    final int EXPENDITUREGL = 9;
    IndividualCustUI individualcustUI = null;
    String refno = "";
    private TransactionUI transactionUI = new TransactionUI();
    private Date curDate = null;

    /**
     * Creates new form ShareProductUI
     */
    public SuspenseAccountMasterUI() {
        initComponents();
        setFieldNames();
        internationalize();
        observable = new SuspenseAccountMasterOB();
        observable.addObserver(this);
        setHelpMessage();
        //  setObservable();
        initComponentData();
        setMandatoryHashMap();
        setMaxLengths();
        ClientUtil.enableDisable(panSuspenseAccountMaster, false, false, true);
        setButtonEnableDisable();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panSuspenseAccountMaster);
        btnView.setEnabled(true);
        visibleFalse();
        if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")) {
            panSalaryRecovery.setVisible(true);
            //panLockStatus.setVisible(true);
        } else {
            panSalaryRecovery.setVisible(false);
            /// panLockStatus.setVisible(false);
        }
        panTransaction.setVisible(false);        
        chkClose.setVisible(false);   
        chkTransaction.setVisible(false);
        curDate = ClientUtil.getCurrentDate();
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
        lblStatus.setText(observable.getLblStatus());
    }

    private void setEnableDisable() {
        txtSuspenseProdDescription.setEnabled(false);
        txtPrefix.setEnabled(false);
        txtAccRefNo.setEnabled(false);
        txtSuspenseActNum.setEnabled(false);
        tdtSuspenseOpenDate.setEnabled(false);
        btnSave.setEnabled(true);
    }

    private void setMaxLengths() {
        txtSuspenseActNum.setMaxLength(64);
        txtSuspenseActNum.setAllowAll(true);
        txtMemberNumber.setMaxLength(64);
        txtMemberNumber.setAllowAll(true);
        txtCustomerId.setMaxLength(64);
        txtCustomerId.setAllowAll(true);
        txtAccRefNo.setMaxLength(20);
        txtAccRefNo.setAllowAll(true);
    }

    private void initComponentData() {
        cboSuspenseProdID.setModel(observable.getCbmSuspenseProdID());
//        cboAgentId.setModel(observable.getCbmagentID());
        //rdoExistCustYes.setSelected(true);
    }

    private void setObservable() {
        try {
            observable = SuspenseAccountMasterOB.getInstance();
            observable.addObserver(this);
        } catch (Exception e) {
            System.out.println("Exception is caught " + e);
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
//        btnPurchaseAcHd.setName("btnPurchaseAcHd");
//        btnPurchaseReturnAcHd.setName("btnPurchaseReturnAcHd");
        btnReject.setName("btnReject");
//        btnSalesAcHd.setName("btnSalesAcHd");
//        btnSalesReturnAcHd.setName("btnSalesReturnAcHd");
        btnSave.setName("btnSave");
//        btnTaxAcHd.setName("btnTaxAcHd");
        btnView.setName("btnView");
//        cboUnit.setName("cboUnit");
        lblMsg.setName("lblMsg");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace5.setName("lblSpace5");
        lblStatus.setName("lblStatus");
        mbrShareProduct.setName("mbrShareProduct");
        panSuspenseAccountMaster.setName("panSuspenseAccountMaster");
        panStatus.setName("panStatus");
        rdoExistCustNo.setName("rdoExistCustNo");
        rdoExistCustYes.setName("rdoExistCustYes");
        lblExistingCust.setName("lblExistingCust");
        lblSuspenseProdID.setName("lblSuspenseProdID");
        lblSuspenseOpenDate.setName("lblSuspenseOpenDate");
        lblMemberNumber.setName("lblSuspenseOpenDate");
        lblSuspenseOpenDate.setName("lblSuspenseOpenDate");
        lblCustomerId.setName("lblCustomerId");
        lblMemberNumber.setName("lblMemberNumber");
        cboSuspenseProdID.setName("cboSuspenseProdID");
        tdtSuspenseOpenDate.setName("tdtSuspenseOpenDate");
        txtMemberNumber.setName("txtMemberNumber");
        txtCustomerId.setName("txtCustomerId");
    }

    /* Auto Generated Method - internationalize()
     This method used to assign display texts from
     the Resource Bundle File. */
    private void internationalize() {
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        btnView.setText(resourceBundle.getString("btnView"));
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
    }

    /* Auto Generated Method - update()
     This method called by Observable. It updates the UI with
     Observable's data. If needed add/Remove RadioButtons
     method need to be added.*/
    public void update(Observable observed, Object arg) {
        removeRadioButtons();
        cboSuspenseProdID.setSelectedItem(observable.getCboSuspenseProdID());
        txtSuspenseProdDescription.setText(observable.getTxtSuspenseProdDescription());
        txtSuspenseActNum.setText(observable.getTxtSuspenseActNum());
        tdtSuspenseOpenDate.setDateValue(CommonUtil.convertObjToStr(observable.getTdtSuspenseOpenDate()));
        txtMemberNumber.setText(observable.getTxtMemberNumber());
        txtCustomerId.setText(observable.getTxtCustomerId());
        txtName.setText(observable.getTxtName());
        txtAddress.setText(observable.getTxtAddress());
        tdtIntCalcUpToDt.setDateValue(observable.getTdtIntCalcUpToDt());
//        cboAgentId.setSelectedItem(((ComboBoxModel) cboAgentId.getModel()).getDataForKey(observable.getCboagentId()));
        addRadioButtons();
        txtPrefix.setText(observable.getTxtPrefix());
        txtAccRefNo.setText(observable.getTxtAccRefNo());
        if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y") && observable.getActionType() != ClientConstants.ACTIONTYPE_NEW) {
            String salaryRecovery = CommonUtil.convertObjToStr(observable.getRdoSalaryRecovery());
            if (salaryRecovery.equals("Y")) {
                rdoSalaryRecovery_Yes.setSelected(true);
            } else if (salaryRecovery.equals("N")) {
                rdoSalaryRecovery_No.setSelected(true);
            } else if (salaryRecovery.equals("")) {
                rdoSalaryRecovery_No.setSelected(false);
                rdoSalaryRecovery_Yes.setSelected(false);
            }
            salaryRecovery = "";
        }
        lblBalance.setText("Total Balance : "+CommonUtil.formatCrore(CommonUtil.convertObjToStr(observable.getBalace())));
        //Added By Revathi.L
        txtAgentID.setText(CommonUtil.convertObjToStr(observable.getTxtAgentID()));
        displayAgentName();
        txtDealerID.setText(CommonUtil.convertObjToStr(observable.getTxtDealerID()));
        displayDealerName();
    }
    //
    private void displayAgentName() {
        if (txtAgentID.getText().length() > 0) {
            HashMap custMap = new HashMap();
            custMap.put("CUST_ID", txtAgentID.getText());
            List namelst = ClientUtil.executeQuery("getCustNameForDeposit", custMap);
            if (namelst.size() > 0) {
                custMap = (HashMap) namelst.get(0);
                lblAgentIDVal.setText(CommonUtil.convertObjToStr(custMap.get("CUSTOMER_NAME")));
            }
        }
    }
    private void displayDealerName() {
        if (txtDealerID.getText().length() > 0) {
            HashMap custMap = new HashMap();
            custMap.put("CUST_ID", txtDealerID.getText());
            List namelst = ClientUtil.executeQuery("getCustNameForDeposit", custMap);
            if (namelst.size() > 0) {
                custMap = (HashMap) namelst.get(0);
                lblDealerIDVal.setText(CommonUtil.convertObjToStr(custMap.get("CUSTOMER_NAME")));
            }
        }
    }

    /* Auto Generated Method - updateOBFields()
     This method called by Save option of UI.
     It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setCboSuspenseProdID(CommonUtil.convertObjToStr(cboSuspenseProdID.getSelectedItem()));
//        observable.setCboagentId((String) ((ComboBoxModel) cboAgentId.getModel()).getKeyForSelected());
        observable.setTxtSuspenseProdDescription(txtSuspenseProdDescription.getText());
        observable.setTxtSuspenseActNum(txtSuspenseActNum.getText());
        observable.setTdtSuspenseOpenDate(tdtSuspenseOpenDate.getDateValue());
        observable.setTxtMemberNumber(txtMemberNumber.getText());
        observable.setTxtCustomerId(txtCustomerId.getText());
        observable.setTxtName(txtName.getText());
        observable.setTxtAddress(txtAddress.getText());
        observable.setTxtPrefix(txtPrefix.getText());
        observable.setTxtAccRefNo((txtAccRefNo.getText()));
        observable.setTdtIntCalcUpToDt(tdtIntCalcUpToDt.getDateValue());
        if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")) {
            if (rdoSalaryRecovery_Yes.isSelected() == true) {
                observable.setRdoSalaryRecovery("Y");
            } else {
                observable.setRdoSalaryRecovery("N");
            }
        } else {
            observable.setRdoSalaryRecovery("");
        }
        //Added by sreekrishnan
        if(chkClose.isSelected())
            observable.setCloseAccount("Y");
        else
            observable.setCloseAccount("N");
        if(chkTransaction.isSelected())
            observable.setCloseWithTransaction("Y");
        else
            observable.setCloseWithTransaction("N");
         //Added By Revathi.L
        observable.setTxtAgentID(CommonUtil.convertObjToStr(txtAgentID.getText()));
        observable.setTxtDealerID(CommonUtil.convertObjToStr(txtDealerID.getText()));
    }

    /* Auto Generated Method - setMandatoryHashMap()
 
     ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
     This method list out all the Input Fields available in the UI.
     It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtProductDesc", new Boolean(true));
        mandatoryMap.put("txtProductCode", new Boolean(true));
        mandatoryMap.put("txtPurchasePrice", new Boolean(true));
        mandatoryMap.put("txtSellingPrice", new Boolean(true));
        mandatoryMap.put("cboUnit", new Boolean(true));
        mandatoryMap.put("txtQty", new Boolean(true));
        mandatoryMap.put("txtReorderLevel", new Boolean(true));
        mandatoryMap.put("txtPurchaseAcHd", new Boolean(true));
        mandatoryMap.put("txtSalesAcHd", new Boolean(true));
        mandatoryMap.put("txtTaxAcHd", new Boolean(true));
        mandatoryMap.put("txtPurchaseReturnAcHd", new Boolean(true));
        mandatoryMap.put("txtSalesReturnAcHd", new Boolean(true));
        mandatoryMap.put("cboSuspenseProdID", new Boolean(true));
        mandatoryMap.put("tdtSuspenseOpenDate", new Boolean(true));
        mandatoryMap.put("txtMemberNumber", new Boolean(true));
        mandatoryMap.put("txtCustomerId", new Boolean(true));
    }

    /* Auto Generated Method - getMandatoryHashMap()
     Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    /* Auto Generated Method - setHelpMessage()
     This method shows tooltip help for all the input fields
     available in the UI. It needs the Mandatory Resource Bundle
     object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        objMandatoryRB = new SuspenseAccountMasterMRB();
        //        txtItemCode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtItemCode"));
        //        txtItemDesc.setHelpMessage(lblMsg, objMandatoryRB.getString("txtItemDesc"));
        //        txtPurchasePrice.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPurchasePrice"));
        //        txtSellingPrice.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSellingPrice"));
        //        cboUnit.setHelpMessage(lblMsg, objMandatoryRB.getString("cboUnit"));
        //        txtQty.setHelpMessage(lblMsg, objMandatoryRB.getString("txtQty"));
        //        txtOrderLevel.setHelpMessage(lblMsg, objMandatoryRB.getString("txtOrderLevel"));
        //        txtPurchaseAcHd.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPurchaseAcHd"));
        //        txtSalesAcHd.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSalesAcHd"));
        //        txtTaxAcHd.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTaxAcHd"));
        //        txtPurchaseReturnAcHd.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPurchaseReturnAcHd"));
        //        txtSalesReturnAcHd.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSalesReturnAcHd"));
    }

    public static void main(String args[]) {
        javax.swing.JFrame frame = new javax.swing.JFrame();
        SuspenseAccountMasterUI ui = new SuspenseAccountMasterUI();
        frame.getContentPane().add(ui);
        ui.setVisible(true);
        frame.setVisible(true);
        frame.setSize(600, 600);
        frame.show();
        ui.show();


    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        rdgExistingCustomer = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrShareProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
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
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace56 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        panSuspenseAccountMaster = new com.see.truetransact.uicomponent.CPanel();
        panAccountDetails = new com.see.truetransact.uicomponent.CPanel();
        lblSuspenseActNum = new com.see.truetransact.uicomponent.CLabel();
        txtSuspenseActNum = new com.see.truetransact.uicomponent.CTextField();
        lblName = new com.see.truetransact.uicomponent.CLabel();
        lblAddress = new com.see.truetransact.uicomponent.CLabel();
        lblMemberNumber = new com.see.truetransact.uicomponent.CLabel();
        txtMemberNumber = new com.see.truetransact.uicomponent.CTextField();
        btnMemberNumber = new com.see.truetransact.uicomponent.CButton();
        lblCustomerId = new com.see.truetransact.uicomponent.CLabel();
        txtCustomerId = new com.see.truetransact.uicomponent.CTextField();
        btnCustomerId = new com.see.truetransact.uicomponent.CButton();
        lblExistingCust = new javax.swing.JLabel();
        cboSuspenseProdID = new com.see.truetransact.uicomponent.CComboBox();
        txtSuspenseProdDescription = new com.see.truetransact.uicomponent.CTextField();
        txtAddress = new com.see.truetransact.uicomponent.CTextField();
        txtName = new com.see.truetransact.uicomponent.CTextField();
        lblSuspenseOpenDate = new com.see.truetransact.uicomponent.CLabel();
        tdtSuspenseOpenDate = new com.see.truetransact.uicomponent.CDateField();
        lblSuspenseProdID = new com.see.truetransact.uicomponent.CLabel();
        panRdoExistCust = new javax.swing.JPanel();
        rdoExistCustNo = new javax.swing.JRadioButton();
        rdoExistCustYes = new javax.swing.JRadioButton();
        lblAccRefNo = new com.see.truetransact.uicomponent.CLabel();
        txtPrefix = new com.see.truetransact.uicomponent.CTextField();
        txtAccRefNo = new com.see.truetransact.uicomponent.CTextField();
        panSalaryRecovery = new com.see.truetransact.uicomponent.CPanel();
        panSalaryRecoveryValue = new com.see.truetransact.uicomponent.CPanel();
        rdoSalaryRecovery_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoSalaryRecovery_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblSalaryRecovery = new com.see.truetransact.uicomponent.CLabel();
        lblSuspenseProdDescription = new com.see.truetransact.uicomponent.CLabel();
        chkClose = new com.see.truetransact.uicomponent.CCheckBox();
        chkTransaction = new com.see.truetransact.uicomponent.CCheckBox();
        lblBalance = new com.see.truetransact.uicomponent.CLabel();
        lblAgentId = new com.see.truetransact.uicomponent.CLabel();
        panAgentID2 = new com.see.truetransact.uicomponent.CPanel();
        txtAgentID = new com.see.truetransact.uicomponent.CTextField();
        btnAgentID = new com.see.truetransact.uicomponent.CButton();
        lblAgentIDVal = new com.see.truetransact.uicomponent.CLabel();
        lblDealer = new com.see.truetransact.uicomponent.CLabel();
        panAgentID3 = new com.see.truetransact.uicomponent.CPanel();
        txtDealerID = new com.see.truetransact.uicomponent.CTextField();
        btnDealerID = new com.see.truetransact.uicomponent.CButton();
        lblDealerIDVal = new com.see.truetransact.uicomponent.CLabel();
        lblIntCalcUpToDt = new com.see.truetransact.uicomponent.CLabel();
        tdtIntCalcUpToDt = new com.see.truetransact.uicomponent.CDateField();
        panTransaction = new com.see.truetransact.uicomponent.CPanel();
        mbrShareProduct = new com.see.truetransact.uicomponent.CMenuBar();
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
        setResizable(true);
        setMaximumSize(new java.awt.Dimension(800, 400));
        setMinimumSize(new java.awt.Dimension(800, 400));
        setPreferredSize(new java.awt.Dimension(800, 650));

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
        tbrShareProduct.add(btnView);

        lblSpace5.setText("     ");
        tbrShareProduct.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnNew);

        lblSpace51.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace51.setText("     ");
        lblSpace51.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace51);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnEdit);

        lblSpace52.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace52.setText("     ");
        lblSpace52.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace52);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnDelete);

        lblSpace2.setText("     ");
        tbrShareProduct.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnSave);

        lblSpace53.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace53.setText("     ");
        lblSpace53.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace53);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnCancel);

        lblSpace3.setText("     ");
        tbrShareProduct.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnAuthorize);

        lblSpace54.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace54.setText("     ");
        lblSpace54.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace54);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnException);

        lblSpace55.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace55.setText("     ");
        lblSpace55.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace55);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnReject);

        lblSpace4.setText("     ");
        tbrShareProduct.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrShareProduct.add(btnPrint);

        lblSpace56.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace56.setText("     ");
        lblSpace56.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace56);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnClose);

        getContentPane().add(tbrShareProduct, java.awt.BorderLayout.NORTH);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace1.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace1, gridBagConstraints);

        lblStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus.setText("                      ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblStatus, gridBagConstraints);

        getContentPane().add(panStatus, java.awt.BorderLayout.SOUTH);

        panSuspenseAccountMaster.setMaximumSize(new java.awt.Dimension(1000, 350));
        panSuspenseAccountMaster.setMinimumSize(new java.awt.Dimension(600, 350));
        panSuspenseAccountMaster.setPreferredSize(new java.awt.Dimension(600, 350));
        panSuspenseAccountMaster.setLayout(new java.awt.GridBagLayout());

        panAccountDetails.setMinimumSize(new java.awt.Dimension(800, 350));
        panAccountDetails.setPreferredSize(new java.awt.Dimension(800, 350));
        panAccountDetails.setLayout(new java.awt.GridBagLayout());

        lblSuspenseActNum.setText("Account Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAccountDetails.add(lblSuspenseActNum, gridBagConstraints);

        txtSuspenseActNum.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(txtSuspenseActNum, gridBagConstraints);

        lblName.setText("Name");
        lblName.setName("lblStreet"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAccountDetails.add(lblName, gridBagConstraints);

        lblAddress.setText("Address");
        lblAddress.setName("lblPincode"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAccountDetails.add(lblAddress, gridBagConstraints);

        lblMemberNumber.setText("Member Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(lblMemberNumber, gridBagConstraints);

        txtMemberNumber.setEditable(false);
        txtMemberNumber.setMaximumSize(new java.awt.Dimension(100, 21));
        txtMemberNumber.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(txtMemberNumber, gridBagConstraints);

        btnMemberNumber.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnMemberNumber.setEnabled(false);
        btnMemberNumber.setMaximumSize(new java.awt.Dimension(21, 21));
        btnMemberNumber.setMinimumSize(new java.awt.Dimension(21, 21));
        btnMemberNumber.setPreferredSize(new java.awt.Dimension(21, 21));
        btnMemberNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMemberNumberActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountDetails.add(btnMemberNumber, gridBagConstraints);

        lblCustomerId.setText("Customer ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(lblCustomerId, gridBagConstraints);

        txtCustomerId.setEditable(false);
        txtCustomerId.setMaximumSize(new java.awt.Dimension(100, 21));
        txtCustomerId.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCustomerId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCustomerIdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(txtCustomerId, gridBagConstraints);

        btnCustomerId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCustomerId.setEnabled(false);
        btnCustomerId.setMaximumSize(new java.awt.Dimension(21, 21));
        btnCustomerId.setMinimumSize(new java.awt.Dimension(21, 21));
        btnCustomerId.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCustomerId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustomerIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountDetails.add(btnCustomerId, gridBagConstraints);

        lblExistingCust.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lblExistingCust.setText("Existing Customer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panAccountDetails.add(lblExistingCust, gridBagConstraints);
        lblExistingCust.getAccessibleContext().setAccessibleName("lblExistingCust");

        cboSuspenseProdID.setMinimumSize(new java.awt.Dimension(100, 21));
        cboSuspenseProdID.setPopupWidth(250);
        cboSuspenseProdID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSuspenseProdIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panAccountDetails.add(cboSuspenseProdID, gridBagConstraints);

        txtSuspenseProdDescription.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(txtSuspenseProdDescription, gridBagConstraints);

        txtAddress.setMinimumSize(new java.awt.Dimension(175, 21));
        txtAddress.setPreferredSize(new java.awt.Dimension(175, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(txtAddress, gridBagConstraints);

        txtName.setMinimumSize(new java.awt.Dimension(175, 21));
        txtName.setPreferredSize(new java.awt.Dimension(175, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(txtName, gridBagConstraints);

        lblSuspenseOpenDate.setText("Opening date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panAccountDetails.add(lblSuspenseOpenDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panAccountDetails.add(tdtSuspenseOpenDate, gridBagConstraints);

        lblSuspenseProdID.setText("Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(lblSuspenseProdID, gridBagConstraints);

        panRdoExistCust.setLayout(new java.awt.GridBagLayout());

        rdgExistingCustomer.add(rdoExistCustNo);
        rdoExistCustNo.setText("No");
        rdoExistCustNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoExistCustNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRdoExistCust.add(rdoExistCustNo, gridBagConstraints);

        rdgExistingCustomer.add(rdoExistCustYes);
        rdoExistCustYes.setSelected(true);
        rdoExistCustYes.setText("Yes");
        rdoExistCustYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoExistCustYesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRdoExistCust.add(rdoExistCustYes, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountDetails.add(panRdoExistCust, gridBagConstraints);

        lblAccRefNo.setText("A/C Ref No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panAccountDetails.add(lblAccRefNo, gridBagConstraints);

        txtPrefix.setBackground(new java.awt.Color(220, 220, 220));
        txtPrefix.setEditable(false);
        txtPrefix.setEnabled(false);
        txtPrefix.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(txtPrefix, gridBagConstraints);

        txtAccRefNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAccountDetails.add(txtAccRefNo, gridBagConstraints);

        panSalaryRecovery.setMinimumSize(new java.awt.Dimension(305, 30));
        panSalaryRecovery.setPreferredSize(new java.awt.Dimension(305, 30));
        panSalaryRecovery.setLayout(new java.awt.GridBagLayout());

        panSalaryRecoveryValue.setMinimumSize(new java.awt.Dimension(95, 27));
        panSalaryRecoveryValue.setPreferredSize(new java.awt.Dimension(95, 27));
        panSalaryRecoveryValue.setLayout(new java.awt.GridBagLayout());

        rdoSalaryRecovery_Yes.setText("Yes");
        rdoSalaryRecovery_Yes.setMinimumSize(new java.awt.Dimension(50, 18));
        rdoSalaryRecovery_Yes.setPreferredSize(new java.awt.Dimension(50, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panSalaryRecoveryValue.add(rdoSalaryRecovery_Yes, gridBagConstraints);

        rdoSalaryRecovery_No.setText("No");
        rdoSalaryRecovery_No.setMaximumSize(new java.awt.Dimension(44, 18));
        rdoSalaryRecovery_No.setMinimumSize(new java.awt.Dimension(44, 18));
        rdoSalaryRecovery_No.setPreferredSize(new java.awt.Dimension(44, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panSalaryRecoveryValue.add(rdoSalaryRecovery_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 1, 0);
        panSalaryRecovery.add(panSalaryRecoveryValue, gridBagConstraints);

        lblSalaryRecovery.setText("Salary Recovery");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 1, 0);
        panSalaryRecovery.add(lblSalaryRecovery, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 3;
        panAccountDetails.add(panSalaryRecovery, gridBagConstraints);

        lblSuspenseProdDescription.setText("Product Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAccountDetails.add(lblSuspenseProdDescription, gridBagConstraints);

        chkClose.setText("Close Account");
        chkClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountDetails.add(chkClose, gridBagConstraints);

        chkTransaction.setText("Need Transaction");
        chkTransaction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkTransactionActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountDetails.add(chkTransaction, gridBagConstraints);

        lblBalance.setMinimumSize(new java.awt.Dimension(200, 21));
        lblBalance.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountDetails.add(lblBalance, gridBagConstraints);

        lblAgentId.setText("Agent ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        panAccountDetails.add(lblAgentId, gridBagConstraints);

        panAgentID2.setMinimumSize(new java.awt.Dimension(120, 29));
        panAgentID2.setPreferredSize(new java.awt.Dimension(120, 29));
        panAgentID2.setLayout(new java.awt.GridBagLayout());

        txtAgentID.setEditable(false);
        txtAgentID.setEnabled(false);
        txtAgentID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAgentID.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panAgentID2.add(txtAgentID, gridBagConstraints);

        btnAgentID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAgentID.setToolTipText("Agent ID");
        btnAgentID.setMinimumSize(new java.awt.Dimension(21, 21));
        btnAgentID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAgentID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgentIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAgentID2.add(btnAgentID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAccountDetails.add(panAgentID2, gridBagConstraints);

        lblAgentIDVal.setForeground(new java.awt.Color(0, 51, 204));
        lblAgentIDVal.setMaximumSize(new java.awt.Dimension(150, 20));
        lblAgentIDVal.setMinimumSize(new java.awt.Dimension(150, 20));
        lblAgentIDVal.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAccountDetails.add(lblAgentIDVal, gridBagConstraints);

        lblDealer.setText("Dealer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        panAccountDetails.add(lblDealer, gridBagConstraints);

        panAgentID3.setMinimumSize(new java.awt.Dimension(120, 29));
        panAgentID3.setPreferredSize(new java.awt.Dimension(120, 29));
        panAgentID3.setLayout(new java.awt.GridBagLayout());

        txtDealerID.setEditable(false);
        txtDealerID.setEnabled(false);
        txtDealerID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDealerID.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panAgentID3.add(txtDealerID, gridBagConstraints);

        btnDealerID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDealerID.setToolTipText("Agent ID");
        btnDealerID.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDealerID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDealerID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDealerIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAgentID3.add(btnDealerID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAccountDetails.add(panAgentID3, gridBagConstraints);

        lblDealerIDVal.setForeground(new java.awt.Color(0, 51, 204));
        lblDealerIDVal.setMaximumSize(new java.awt.Dimension(150, 20));
        lblDealerIDVal.setMinimumSize(new java.awt.Dimension(150, 20));
        lblDealerIDVal.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAccountDetails.add(lblDealerIDVal, gridBagConstraints);

        lblIntCalcUpToDt.setText("Int Calc Up To Dt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        panAccountDetails.add(lblIntCalcUpToDt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panAccountDetails.add(tdtIntCalcUpToDt, gridBagConstraints);

        panSuspenseAccountMaster.add(panAccountDetails, new java.awt.GridBagConstraints());

        panTransaction.setMinimumSize(new java.awt.Dimension(800, 235));
        panTransaction.setPreferredSize(new java.awt.Dimension(800, 235));
        panTransaction.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panSuspenseAccountMaster.add(panTransaction, gridBagConstraints);

        getContentPane().add(panSuspenseAccountMaster, java.awt.BorderLayout.CENTER);

        mbrShareProduct.setName("mbrCustomer"); // NOI18N

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
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrShareProduct.add(mnuProcess);

        setJMenuBar(mbrShareProduct);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rdoExistCustNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoExistCustNoActionPerformed
        // TODO add your handling code here:
        txtSuspenseActNum.setVisible(false);
        lblSuspenseActNum.setVisible(false);
        if (rdoExistCustNo.isSelected() == true) {
            txtSuspenseActNum.setText("");
            txtCustomerId.setText("");
            // lblCustName.setText("");
            // nomineeUi.setMainCustomerId(txtCustomerId.getText());
            //            lblSecurityCustNameValue.setText("");
            //lblDocumentCustNameValue.setText("");
            lblSuspenseActNum.setVisible(false);
            txtSuspenseActNum.setVisible(false);
            //tblBorrowerTabCTable.revalidate();
            txtCustomerId.setEnabled(true);
            individualcustUI = new IndividualCustUI();
            com.see.truetransact.ui.TrueTransactMain.showScreen(individualcustUI);
            individualcustUI.loanCreationCustId(this);
        }
    }//GEN-LAST:event_rdoExistCustNoActionPerformed

    private void removeRadioButtons() {

        rdgExistingCustomer.remove(rdoExistCustNo);
        rdgExistingCustomer.remove(rdoExistCustYes);
    }

    private void addRadioButtons() // these r all radio button purpose adding...
    {
        rdgExistingCustomer = new CButtonGroup();
        rdgExistingCustomer.add(rdoExistCustNo);
        rdgExistingCustomer.add(rdoExistCustYes);
    }
    private void rdoExistCustYesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoExistCustYesActionPerformed
        // TODO add your handling code here:
        txtSuspenseActNum.setVisible(true);
        lblSuspenseActNum.setVisible(true);
    }//GEN-LAST:event_rdoExistCustYesActionPerformed

    private void cboSuspenseProdIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSuspenseProdIDActionPerformed
        // TODO add your handling code here:
        if (cboSuspenseProdID.getSelectedIndex() > 0) {
            HashMap hashMap = new HashMap();
            //String prod_Desc = (String) hashMap.get("PROD_DESC");
            String prod_Desc = (String) (observable.getCbmSuspenseProdID().getKeyForSelected());
            System.out.println("prod_Desc" + prod_Desc);
            txtSuspenseProdDescription.setText(prod_Desc);
            // hashMap.put("PROD_ID",cboSuspenseProdID.getSelectedItem());
            hashMap.put("PROD_ID", prod_Desc.trim());
            List lst = ClientUtil.executeQuery("getSuspenseProdDescription", hashMap);
            hashMap = new HashMap();
            hashMap = (HashMap) lst.get(0);
            System.out.println("hashMap" + hashMap);
            String prefix = (String) hashMap.get("SUS_PREFIX");
            txtPrefix.setText(prefix);
            List nextAcList = null;
            HashMap whereMap = new HashMap();
            whereMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            whereMap.put("PRODUCT_ID", prod_Desc.trim());
            nextAcList = (List) (ClientUtil.executeQuery("getSelectNextAccNo", whereMap));
            if (nextAcList != null && nextAcList.size() > 0) {
                String nxtAcNum = CommonUtil.convertObjToStr((nextAcList.get(0)));
                txtSuspenseActNum.setText(String.valueOf(nxtAcNum));
        } else {
                System.out.println("elseeeeeeee");
            txtSuspenseActNum.setText("");
            }
            
            HashMap checkMap = new HashMap();
            checkMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            checkMap.put("PROD_ID", (String) ((ComboBoxModel) cboSuspenseProdID.getModel()).getKeyForSelected());
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
        else {
            txtSuspenseProdDescription.setText("");
        }
    }//GEN-LAST:event_cboSuspenseProdIDActionPerformed

    private void btnMemberNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMemberNumberActionPerformed
        // TODO add your handling code here:
        //        popUp("MEMBER_NO");
        actionType = "MEMBER_NO";
        new CheckCustomerIdUI(this);
        getEmpRefNo();//this line added by Anju Anand for Mantis Id: 10394
    }//GEN-LAST:event_btnMemberNumberActionPerformed

    private void btnCustomerIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerIdActionPerformed
        // TODO add your handling code here:
        actionType = "CUSTOMER_ID";
        new CheckCustomerIdUI(this);
        getEmpRefNo();//this line added by Anju Anand for Mantis Id: 10394
    }//GEN-LAST:event_btnCustomerIdActionPerformed

    private void txtSuspenseActNumFocusLost(java.awt.event.FocusEvent evt) {                                            
        // TODO add your handling code here:
//        if(txtItemCode.getText().length()>0){
//            txtItemCode.setText(CommonUtil.convertObjToStr(txtItemCode.getText()).toUpperCase());
//            String itemCode = CommonUtil.convertObjToStr(txtItemCode.getText());
//            HashMap locCodeChkMap = new HashMap();
//            locCodeChkMap.put("ITEM_CODE",itemCode);
//            List list = ClientUtil.executeQuery("getItemCodeChk", locCodeChkMap);
//            if(list!= null && list.size() > 0){
//                ClientUtil.showAlertWindow("Item Code Already exists!!!");
//                txtItemCode.setText("");
//            }
//        }
    }                                           

    private void callView(String currField) {//Added By Revathi.L
        HashMap viewMap = new HashMap();
        HashMap whereMap;
        view = currField;
         if (currField.equals("AGENT_ID")) {
            HashMap where = new HashMap();
            //commented by rishad 29/aug/208
           // where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            where.put("TYPE", "A");
            where.put("AGENT", "AGENT");
            viewMap.put(CommonConstants.MAP_NAME, "getDealerDetails");
            viewMap.put(CommonConstants.MAP_WHERE, where);
            new ViewAll(this, viewMap).show();
        }else if (currField.equals("DEALER_ID")) {
            HashMap where = new HashMap();
              //commented by rishad 29/aug/208
           //where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            where.put("TYPE", "D");
            where.put("DEALER", "DEALER");
            viewMap.put(CommonConstants.MAP_NAME, "getDealerDetails");
            viewMap.put(CommonConstants.MAP_WHERE, where);
            new ViewAll(this, viewMap).show();
        }
        //        new ViewAll(this, viewMap).show();
    }

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        viewType = ClientConstants.ACTIONTYPE_DELETE;
        observable.setStatus();
        popUp(ClientConstants.ACTION_STATUS[3]);
        ClientUtil.enableDisable(this, false);
        ClientUtil.enableDisable(panSuspenseAccountMaster, false);
        lblStatus.setText(observable.getLblStatus());
        txtAccRefNo.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void visibleFalse() {
//        btnSalesAcHd.setVisible(false);
//        btnPurchaseAcHd.setVisible(false);
//        lblPurchaseReturnAcHd.setVisible(false);
//        txtPurchaseReturnAcHd.setVisible(false);
//        lblSalesReturnAcHd.setVisible(false);
//        txtSalesReturnAcHd.setVisible(false);
//        lblTaxAcHd.setVisible(false);
//        txtTaxAcHd.setVisible(false);
//        btnPurchaseReturnAcHd.setVisible(false);
//        btnSalesReturnAcHd.setVisible(false);
//        btnTaxAcHd.setVisible(false);
    }
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        viewType = ClientConstants.ACTIONTYPE_EDIT;
        observable.setStatus();
        popUp(ClientConstants.ACTION_STATUS[2]);
        setModified(true);
        observable.setStatus();
        btnEdit.setEnabled(false);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        ClientUtil.enableDisable(this, false);
        btnCancel.setEnabled(true);
        btnSave.setEnabled(true);
        lblStatus.setText(observable.getLblStatus());
        ClientUtil.enableDisable(panSuspenseAccountMaster, true);
        lblStatus.setText(observable.getLblStatus());
        txtSuspenseActNum.setEnabled(false);
//        btnSuspenseActHead.setEnabled(true);
//        txtSuspenseActHead.setEnabled(false);
        btnMemberNumber.setEnabled(true);
        btnCustomerId.setEnabled(true);
        tdtSuspenseOpenDate.setEnabled(false);
        txtSuspenseProdDescription.setEnabled(false);
        txtPrefix.setEnabled(false);
        txtAccRefNo.setEnabled(false);
        chkClose.setVisible(true);
        chkClose.setEnabled(true);
        chkTransaction.setVisible(true);
        chkTransaction.setEnabled(false);
        btnAgentID.setEnabled(true);
        btnDealerID.setEnabled(true);
    }//GEN-LAST:event_btnEditActionPerformed
    private void popUp(String actionType) {
        this.actionType = actionType;
        if (actionType != null) {
            final HashMap viewMap = new HashMap();
            HashMap wheres = new HashMap();
            if (actionType.equals(ClientConstants.ACTION_STATUS[2])) {
                //                for Edit
                ArrayList lst = new ArrayList();
//                lst.add("DRF NO");
                lst.add("SUSPENSE_ACCT_NUM");
                viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
                viewMap.put(CommonConstants.MAP_NAME, "getSelectSuspenseAccountTOList");
                viewMap.put(CommonConstants.MAP_WHERE, wheres);
            } else if (actionType.equals(ClientConstants.ACTION_STATUS[3])) {
                viewMap.put(CommonConstants.MAP_NAME, "getSelectSuspenseAccountTOList");
                viewMap.put(CommonConstants.MAP_WHERE, wheres);
            } else if (actionType.equals("ViewDetails")) {
                HashMap where = new HashMap();
                where.put("BRANCH_ID", getSelectedBranchID());
                viewMap.put(CommonConstants.MAP_NAME, "getSelectSuspenseAccountTOList");
                viewMap.put(CommonConstants.MAP_WHERE, where);
                where = null;
            } else if (actionType.equals("MEMBER_NO")) {
                viewMap.put(CommonConstants.MAP_NAME, "getMemeberShipDetails");
            }

            new com.see.truetransact.ui.common.viewall.ViewAll(this, viewMap, true).show();
        }

    }

    /**
     * To get data based on customer id received from the popup and populate
     * into the screen
     */
    public void fillData(Object obj) {
        isFilled = true;
        setModified(true);
        final HashMap hash = (HashMap) obj;
        System.out.println("@@@@hash" + hash);
        String st = CommonUtil.convertObjToStr(hash.get("STATUS"));
        if (st.equalsIgnoreCase("DELETED")) {
            flag = true;
        }
        if (hash.get("MEMBER_NO") != null && actionType.equals("MEMBER_NO")) {
            txtMemberNumber.setText(CommonUtil.convertObjToStr(hash.get("MEMBER_NO")));
            txtMemberNumber.setEnabled(false);
            txtName.setText(CommonUtil.convertObjToStr(hash.get("NAME")));
            txtName.setEnabled(false);
            txtCustomerId.setText(CommonUtil.convertObjToStr(hash.get("CUST_ID")));
            txtCustomerId.setEnabled(false);
            txtAddress.setText(CommonUtil.convertObjToStr(hash.get("ADDRESS")));
            txtAddress.setEnabled(false);
        }
        if (hash.get("CUST_ID") != null && actionType.equals("CUSTOMER_ID")) {
            txtMemberNumber.setText(CommonUtil.convertObjToStr(hash.get("MEMBER_NO")));
            txtMemberNumber.setEnabled(false);
            txtName.setText(CommonUtil.convertObjToStr(hash.get("NAME")));
            txtName.setEnabled(false);
            txtCustomerId.setText(CommonUtil.convertObjToStr(hash.get("CUST_ID")));
            txtCustomerId.setEnabled(false);
            txtAddress.setText(CommonUtil.convertObjToStr(hash.get("ADDRESS")));
            txtAddress.setEnabled(false);
        } else if (actionType.equals(ClientConstants.ACTION_STATUS[2])
                || actionType.equals(ClientConstants.ACTION_STATUS[3]) || actionType.equals("DeletedDetails") || actionType.equals("ViewDetails") || viewType == AUTHORIZE
                || getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
            HashMap map = new HashMap();
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
                map.put("SUSPENSE_ACCT_NUM", hash.get("SUSPENSE_ACCT_NUM"));
                map.put(CommonConstants.MAP_WHERE, hash.get("SUSPENSE_ACCT_NUM"));
                map.put("TRANS_DT", curDate.clone());
                map.put("TRANS_ID", hash.get("BATCH_ID"));
            } else {
                map.put("SUSPENSE_ACCT_NUM", hash.get("SUSPENSE_ACCT_NUM"));
                map.put(CommonConstants.MAP_WHERE, hash.get("SUSPENSE_ACCT_NUM"));
            }       
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
                        panTransaction.setVisible(true);  
                observable.setTransactionOB(transactionUI.getTransactionOB());
            }
            observable.getData(map); 
            System.out.println("");
            if(transactionUI.getOutputTO()!=null && transactionUI.getOutputTO().size()>0){
                transactionUI.setSourceScreen("SUSPENSE_ACCOUNT_MASTER");
                transactionUI.addToScreen(panTransaction);
            }
            setButtonEnableDisable();
            
            //For EDIT option enable disable fields and controls appropriately
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                ClientUtil.enableDisable(panSuspenseAccountMaster, true);
            }
            observable.setStatus();
            if (getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE) {

                btnAuthorize.setVisible(false);
                btnReject.setVisible(false);
                btnException.setVisible(false);
            }

            if (viewType == AUTHORIZE) {
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);

            }
        }
        if (viewType == AUTHORIZE) {
            ClientUtil.enableDisable(panSuspenseAccountMaster, false);
        }
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW_MODE) {
            ClientUtil.enableDisable(this, false);
        }
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
        }
        if (view.equals("AGENT_ID")) {//Added By Revathi.L
            txtAgentID.setText(CommonUtil.convertObjToStr(hash.get("AGENT_ID")));
            lblAgentIDVal.setText(CommonUtil.convertObjToStr(hash.get("AGENT_NAME")));
            setEnableDisable();
        }
        if (view.equals("DEALER_ID")) {//Added By Revathi.L
            txtDealerID.setText(CommonUtil.convertObjToStr(hash.get("DEALER_ID")));
            lblDealerIDVal.setText(CommonUtil.convertObjToStr(hash.get("DEALER_NAME")));
            setEnableDisable();
        }
    }

    protected void getCustomerAddressDetails(String value) {
        HashMap custAddressMap = new HashMap();
        custAddressMap.put("CUST_ID", value);
        custAddressMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        List lst = ClientUtil.executeQuery("getSelectAccInfoDisplay", custAddressMap);
        if (lst != null && lst.size() > 0) {
            custAddressMap = (HashMap) lst.get(0);
            System.out.println("324@$@#$@#4custAddressMap :" + custAddressMap);
            txtName.setText(CommonUtil.convertObjToStr(custAddressMap.get("Name")));
            txtAddress.setText(CommonUtil.convertObjToStr(custAddressMap.get("STREET")) + ", " + CommonUtil.convertObjToStr(custAddressMap.get("CITY1")) + " ," + CommonUtil.convertObjToStr(custAddressMap.get("CITY1")) + " ,");
            txtName.setEnabled(false);
            txtAddress.setEnabled(false);
            txtMemberNumber.setText(CommonUtil.convertObjToStr(custAddressMap.get("MEMBERSHIP_NO")));
            txtMemberNumber.setEnabled(false);
        }
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        ClientUtil.enableDisable(panSuspenseAccountMaster, true, false, true);
        observable.resetForm();
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        viewType = ClientConstants.ACTIONTYPE_NEW;
        observable.setStatus();
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();
        ClientUtil.clearAll(panSuspenseAccountMaster);
        ClientUtil.enableDisable(panSuspenseAccountMaster, true);
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        lblStatus.setText(observable.getLblStatus());
        txtSuspenseActNum.setEnabled(false);
        txtSuspenseProdDescription.setEnabled(false);
        //rdoExistCustYes.setSelected(true);  
        txtPrefix.setEnabled(false);
        tdtIntCalcUpToDt.setDateValue(DateUtil.getStringDate(curDate));
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        viewType = 0;
        ClientUtil.enableDisable(this, false, false, true);
        HashMap map = new HashMap();
//        map.put("SCREEN_ID",getScreen());
        map.put("SCREEN_ID", getScreenID());
        map.put("RECORD_KEY", this.txtSuspenseActNum.getText());
        map.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        map.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        map.put("CUR_DATE", ClientUtil.getCurrentDate());
        System.out.println("Record Key Map : " + map);
        ClientUtil.execute("deleteEditLock", map);
        observable.resetForm();
        setButtonEnableDisable();
        observable.setStatus();
        ClientUtil.clearAll(this);
//        if(getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE){
//            cifClosingAlert();
//        }
        setModified(false);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        btnNew.setEnabled(true);
        btnView.setEnabled(true);
        btnSave.setEnabled(false);
        btnDelete.setEnabled(true);
        btnEdit.setEnabled(true);
        btnClose.setEnabled(true);
        lblStatus.setText("            ");
        panTransaction.setVisible(false); 
        transactionUI.cancelAction(true);
        transactionUI.setButtonEnableDisable(false);
        transactionUI.resetObjects();
        chkClose.setVisible(false);   
        chkTransaction.setVisible(false);
        lblBalance.setText("");
        lblAgentIDVal.setText("");//Added by Revathi.L
        lblDealerIDVal.setText("");//Added by Revathi.L
    }//GEN-LAST:event_btnCancelActionPerformed

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mitNewActionPerformed

    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mitSaveActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mitEditActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mitCancelActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
        txtAccRefNo.setEnabled(false);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        updateOBFields();
        String mandatoryMessage = objMandatoryCheck.checkMandatory(getClass().getName(), panSuspenseAccountMaster);
        if (mandatoryMessage.length() > 0) {
            displayAlert(mandatoryMessage);
        } else {
            //Added by sreekrishnan
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){              
                int transactionSize = 0 ;
                if(chkClose.isSelected() && chkTransaction.isSelected()){
                    if(transactionUI.getOutputTO().size() == 0 && chkClose.isSelected() && chkTransaction.isSelected()){
                        ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS)) ;
                        return;
                    }else {
                        if(observable.getBalace() >0 && chkClose.isSelected() && chkTransaction.isSelected()){
                            transactionSize = (transactionUI.getOutputTO()).size();
                            if(transactionSize != 1 && observable.getBalace()>0){
                                ClientUtil.showAlertWindow("Multiple Transactions are Not allowed, Make it one Transaction") ;
                                return;
                            }else{
                                observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                            }
                        }else if(transactionUI.getOutputTO().size()>0){
                            observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                        }
                    }
                    if(transactionSize == 0 && chkClose.isSelected() && chkTransaction.isSelected()){
                        ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS)) ;
                        return;
                    }else if(transactionSize != 0 ){
                        if (!transactionUI.isBtnSaveTransactionDetailsFlag()) {
                            ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.SAVE_TX_DETAILS));
                            return;
                        }
                         if(transactionUI.getOutputTO().size()>0){
                            observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                            observable.doAction();
                        }
                    }
                }else{
                    observable.doAction();
                }
            }else{
                observable.doAction();
            }
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                System.out.println("observable.getProxyReturnMap()$^##^#"+observable.getProxyReturnMap());
                if (observable.getProxyReturnMap()!=null) {
                    if (observable.getProxyReturnMap().containsKey("CASH_TRANS_LIST") || observable.getProxyReturnMap().containsKey("TRANSFER_TRANS_LIST")) {
                        displayTransDetail(observable.getProxyReturnMap());
                    }
                }
                btnCancelActionPerformed(null);
                observable.resetForm();
                btnCancel.setEnabled(true);
                observable.setStatus();
                observable.setResultStatus();
                lblStatus.setText(observable.getLblStatus());
                lblBalance.setText("");
            }
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
            observable.ttNotifyObservers();
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    /* To display an alert message if any of the mandatory fields is not inputed */

    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }

        private void displayTransDetail(HashMap proxyResultMap) {
        System.out.println("@#$@@$@@@$ proxyResultMap : " +proxyResultMap);
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
        String actNum = "";
        for (int i=0; i<keys.length; i++) {
            if (proxyResultMap.get(keys[i]) instanceof String) {
                continue;
            }
            tempList = (List)proxyResultMap.get(keys[i]);
            if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH")!=-1) {
                for (int j=0; j<tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j==0) {
                        transId = (String)transMap.get("SINGLE_TRANS_ID");
                    }
                    cashDisplayStr += "Trans Id : "+transMap.get("TRANS_ID")+
                    "   Trans Type : "+transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if(actNum != null && !actNum.equals("")){
                        cashDisplayStr +="   Account No : "+transMap.get("ACT_NUM")+
                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }else{
                        cashDisplayStr += "   Ac Hd Desc : "+transMap.get("AC_HD_ID")+
                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }
                    transIdMap.put(transMap.get("SINGLE_TRANS_ID"),"CASH");
                }
                cashCount++;
                transType = "CASH";
            } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER")!=-1) {
                for (int j=0; j<tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j==0) {
                        transId = (String)transMap.get("SINGLE_TRANS_ID");
                    }
                    transferDisplayStr += "Trans Id : "+transMap.get("TRANS_ID")+
                    "   Batch Id : "+transMap.get("BATCH_ID")+
                    "   Trans Type : "+transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if(actNum != null && !actNum.equals("")){
                        transferDisplayStr +="   Account No : "+transMap.get("ACT_NUM")+
                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }else{
                        transferDisplayStr += "   Ac Hd Desc : "+transMap.get("AC_HD_ID")+
                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }
                      transIdMap.put(transMap.get("SINGLE_TRANS_ID"),"TRANSFER");
                }
                transferCount++;
                transType = "TRANSFER";
            }
        }
        if(cashCount>0){
            displayStr+=cashDisplayStr;
        }
        if(transferCount>0){
            displayStr+=transferDisplayStr;
        }
        ClientUtil.showMessageWindow(""+displayStr);
        int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null,"Do you want to print?", CommonConstants.WARNINGTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        System.out.println("#$#$$ yesNo : "+yesNo);
        if (yesNo==0) {
            TTIntegration ttIntgration = null;
            HashMap paramMap = new HashMap();
            Date curDate1=(Date) curDate.clone();
            paramMap.put("TransDt", curDate1);
            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
            //Added By Suresh
            if(transType.equals("CASH")){
                paramMap.put("TransId", transId);
            ttIntgration.setParam(paramMap);
            String reportName = "";
                reportName = "CashPayment";
            ttIntgration.integrationForPrint(reportName, false);
            }
            if(transType.equals("TRANSFER")){
                Object keys1[] = transIdMap.keySet().toArray();
                for (int i=0; i<keys1.length; i++) {
                    paramMap.put("TransId", keys1[i]);
                    ttIntgration.setParam(paramMap);
                    if (CommonUtil.convertObjToStr(transIdMap.get(keys1[i])).equals("TRANSFER")) {
                        ttIntgration.integrationForPrint("ReceiptPayment");
                    }
                }
            }
        }
    }
        
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        popUp("ViewDetails");
        ClientUtil.enableDisable(this, false);
        ClientUtil.enableDisable(panSuspenseAccountMaster, false);
        lblStatus.setText(observable.getLblStatus());
        btnSave.setEnabled(false);
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        observable.setStatus();
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        txtAccRefNo.setEnabled(false);
        lblBalance.setText("");
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    public void authorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled) {
            observable.setResult(observable.getActionType());
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            if (authorizeStatus.equalsIgnoreCase("REJECTED") && flag == true) {
                singleAuthorizeMap.put("DELETESTATUS", "MODIFIED");
                singleAuthorizeMap.put(CommonConstants.STATUS, "REJECTED");
                singleAuthorizeMap.put("DELETEREMARKS", "");
                singleAuthorizeMap.put("STATUSCHECK", "");
            }
            singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, ClientUtil.getCurrentDate());
            singleAuthorizeMap.put("SUSPENSE_ACCT_NUM", observable.getTxtSuspenseActNum());
            singleAuthorizeMap.put("SUSPENSE_ACCOUNT", "SUSPENSE_ACCOUNT");
            //
            System.out.println("!@#$@#$@#$singleAuthorizeMap:" + singleAuthorizeMap);
            observable.authorizeSuspenseMaster(singleAuthorizeMap);
            super.setOpenForEditBy(observable.getStatusBy());
            //            super.removeEditLock(this.txtItemCode.getText());
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
            //            lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
            viewType = 0;
        } else {
            viewType = AUTHORIZE;
            final HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            whereMap.put("TRANS_DT", curDate);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            //mapParam.put(CommonConstants.MAP_NAME, "getSuspenseAccountMasterAuthMode");
            mapParam.put(CommonConstants.MAP_NAME, "getSuspenseAccountMasterTransaction");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeSuspenseAccountMaster");
            isFilled = false;
            setModified(true);
            final AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            lblStatus.setText(observable.getLblStatus());
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        }
    }
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mitCloseActionPerformed

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        txtAccRefNo.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void txtCustomerIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCustomerIdFocusLost
        // TODO add your handling code here:
       
    }//GEN-LAST:event_txtCustomerIdFocusLost

private void chkCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkCloseActionPerformed
// TODO add your handling code here:
    if(chkClose.isSelected()){
        chkTransaction.setEnabled(true);
    }else{
        chkTransaction.setEnabled(false);
        chkTransaction.setSelected(false);
        panTransaction.setVisible(false); 
        transactionUI.cancelAction(true);
        transactionUI.setButtonEnableDisable(false);
        transactionUI.resetObjects();
    }
}//GEN-LAST:event_chkCloseActionPerformed

private void chkTransactionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkTransactionActionPerformed
// TODO add your handling code here:
    if(chkTransaction.isSelected()){
        if(observable.getBalace()>0){
            panTransaction.setVisible(true);      
            transactionUI.setSourceScreen("SUSPENSE_ACCOUNT_MASTER");
            transactionUI.addToScreen(panTransaction);
            observable.setTransactionOB(transactionUI.getTransactionOB());
            transactionUI.setButtonEnableDisable(true);
            transactionUI.cancelAction(false);
            transactionUI.resetObjects();
            transactionUI.setProdType();
            transactionUI.setCallingAmount(CommonUtil.convertObjToStr(observable.getBalace()));
            transactionUI.setCallingApplicantName(observable.getTxtName());
            transactionUI.setCallingTransType("CASH");
        }else{
            ClientUtil.showMessageWindow("Balance Nill!!");
            panTransaction.setVisible(false); 
            transactionUI.cancelAction(true);
            transactionUI.setButtonEnableDisable(false);
            transactionUI.resetObjects();
            chkTransaction.setSelected(false);
            return;
        }
    }else{
        panTransaction.setVisible(false); 
        transactionUI.cancelAction(true);
        transactionUI.setButtonEnableDisable(false);
        transactionUI.resetObjects();
    }
}//GEN-LAST:event_chkTransactionActionPerformed

    private void btnAgentIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgentIDActionPerformed
        // TODO add your handling code here
        callView("AGENT_ID");
    }//GEN-LAST:event_btnAgentIDActionPerformed

    private void btnDealerIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDealerIDActionPerformed
        // TODO add your handling code here:
        callView("DEALER_ID");
    }//GEN-LAST:event_btnDealerIDActionPerformed

    //this function added by Anju Anand for Mantis Id: 10394
    private void getEmpRefNo() {
        String custId = "";
        custId = txtCustomerId.getText();
        HashMap dataMap = new HashMap();
        dataMap.put("CUST_ID", custId);
        List list = ClientUtil.executeQuery("getEmpRefNo", dataMap);
        if (list != null && list.size() > 0) {
            HashMap resultMap = new HashMap();
            resultMap = (HashMap) list.get(0);
            String empRefNo = "";
            empRefNo = CommonUtil.convertObjToStr(resultMap.get("EMP_REFNO_NEW"));
            if (empRefNo != null) {
                txtAccRefNo.setText(empRefNo);
                txtAccRefNo.setEnabled(false);
            } else {
                txtAccRefNo.setEnabled(true);
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAgentID;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCustomerId;
    private com.see.truetransact.uicomponent.CButton btnDealerID;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnMemberNumber;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboSuspenseProdID;
    private com.see.truetransact.uicomponent.CCheckBox chkClose;
    private com.see.truetransact.uicomponent.CCheckBox chkTransaction;
    private com.see.truetransact.uicomponent.CLabel lblAccRefNo;
    private com.see.truetransact.uicomponent.CLabel lblAddress;
    private com.see.truetransact.uicomponent.CLabel lblAgentIDVal;
    private com.see.truetransact.uicomponent.CLabel lblAgentId;
    private com.see.truetransact.uicomponent.CLabel lblBalance;
    private com.see.truetransact.uicomponent.CLabel lblCustomerId;
    private com.see.truetransact.uicomponent.CLabel lblDealer;
    private com.see.truetransact.uicomponent.CLabel lblDealerIDVal;
    private javax.swing.JLabel lblExistingCust;
    private com.see.truetransact.uicomponent.CLabel lblIntCalcUpToDt;
    private com.see.truetransact.uicomponent.CLabel lblMemberNumber;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblName;
    private com.see.truetransact.uicomponent.CLabel lblSalaryRecovery;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace51;
    private com.see.truetransact.uicomponent.CLabel lblSpace52;
    private com.see.truetransact.uicomponent.CLabel lblSpace53;
    private com.see.truetransact.uicomponent.CLabel lblSpace54;
    private com.see.truetransact.uicomponent.CLabel lblSpace55;
    private com.see.truetransact.uicomponent.CLabel lblSpace56;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblSuspenseActNum;
    private com.see.truetransact.uicomponent.CLabel lblSuspenseOpenDate;
    private com.see.truetransact.uicomponent.CLabel lblSuspenseProdDescription;
    private com.see.truetransact.uicomponent.CLabel lblSuspenseProdID;
    private com.see.truetransact.uicomponent.CMenuBar mbrShareProduct;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAccountDetails;
    private com.see.truetransact.uicomponent.CPanel panAgentID2;
    private com.see.truetransact.uicomponent.CPanel panAgentID3;
    private javax.swing.JPanel panRdoExistCust;
    private com.see.truetransact.uicomponent.CPanel panSalaryRecovery;
    private com.see.truetransact.uicomponent.CPanel panSalaryRecoveryValue;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panSuspenseAccountMaster;
    private com.see.truetransact.uicomponent.CPanel panTransaction;
    private com.see.truetransact.uicomponent.CButtonGroup rdgExistingCustomer;
    private javax.swing.JRadioButton rdoExistCustNo;
    private javax.swing.JRadioButton rdoExistCustYes;
    private com.see.truetransact.uicomponent.CRadioButton rdoSalaryRecovery_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoSalaryRecovery_Yes;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private javax.swing.JToolBar tbrShareProduct;
    private com.see.truetransact.uicomponent.CDateField tdtIntCalcUpToDt;
    private com.see.truetransact.uicomponent.CDateField tdtSuspenseOpenDate;
    private com.see.truetransact.uicomponent.CTextField txtAccRefNo;
    private com.see.truetransact.uicomponent.CTextField txtAddress;
    private com.see.truetransact.uicomponent.CTextField txtAgentID;
    public static com.see.truetransact.uicomponent.CTextField txtCustomerId;
    private com.see.truetransact.uicomponent.CTextField txtDealerID;
    private com.see.truetransact.uicomponent.CTextField txtMemberNumber;
    private com.see.truetransact.uicomponent.CTextField txtName;
    private com.see.truetransact.uicomponent.CTextField txtPrefix;
    private com.see.truetransact.uicomponent.CTextField txtSuspenseActNum;
    private com.see.truetransact.uicomponent.CTextField txtSuspenseProdDescription;
    // End of variables declaration//GEN-END:variables
}
