/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareProductUI.java
 *
 * Created on November 23, 2004, 4:00 PM
 */

package com.see.truetransact.ui.paddyprocurement;

/**
 *
 * @author
 */

import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uivalidation.DefaultValidation;
import com.see.truetransact.ui.common.resolutionstatus.ResolutionStatusUI;
import com.see.truetransact.ui.deposit.CommonMethods;
import com.see.truetransact.ui.deposit.CommonRB;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uivalidation.DefaultValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uivalidation.EmailValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.ToDateValidation;
import com.see.truetransact.uivalidation.PincodeValidation_IN;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.COptionPane;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Observer;
import java.util.Observable;
import org.apache.log4j.Logger;
import java.util.HashMap;
import com.see.truetransact.ui.common.nominee.*;
import com.see.truetransact.ui.common.transaction.TransactionUI;

public class PaddySaleMasterUI extends CInternalFrame implements UIMandatoryField, Observer{
    
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.paddyprocurement.PaddyLocalityMasterRB", ProxyParameters.LANGUAGE);
    PaddySaleMasterOB observable = null;
    private Date curDate = null;
    private int viewType=-1;
    private TransactionUI transactionUI = new TransactionUI();
    HashMap mandatoryMap;
    private String mandatoryMessage="";
    private boolean familyDetailsFlag = false;
    final int AUTHORIZE=3;
    final int DELETE = 1;
    private String actionType = "";
    boolean isFilled = false;
    boolean flag = false;
    PaddySaleMasterMRB objMandatoryRB = new PaddySaleMasterMRB();
    
    /** Creates new form ShareProductUI */
    public PaddySaleMasterUI() {
        initComponents();
        initStartup();
        transactionUI.addToScreen(panTransaction);
        observable.setTransactionOB(transactionUI.getTransactionOB());
        btnCancelActionPerformed(null);
    }
    
    private void initStartup() {
        curDate = ClientUtil.getCurrentDate();
        setFieldNames();
        internationalize();
        observable = new PaddySaleMasterOB();
        initComponentData();
        setMaximumLength();
        observable.resetForm();
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();
        setMandatoryHashMap();
        initTableData();
        setHelpMessage();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panSaleMaster);
        txtTotalAmount.setEnabled(false);
        btnEdit.setEnabled(false);
        btnCancelActionPerformed(null);
    }
    private void setHelpMessage(){
        objMandatoryRB = new PaddySaleMasterMRB();
        txtBillNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBillNo"));
        txtName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtName"));
        txtProductCode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtProductCode"));
        txtProductDesc.setHelpMessage(lblMsg, objMandatoryRB.getString("txtProductDesc"));
        txtRatePerKg.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRatePerKg"));
        txtKiloGram.setHelpMessage(lblMsg, objMandatoryRB.getString("txtKiloGram"));
        txtAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAmount"));
        txtlAcreage.setHelpMessage(lblMsg, objMandatoryRB.getString("txtlAcreage"));
        txtBags.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBags"));
        tdtBillDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtBillDate"));
        txtAddress.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAddress"));
        tdtSaleDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtSaleDate"));
        txtTotalAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTotalAmount"));
    }
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtBillNo", new Boolean(true));
        mandatoryMap.put("txtName", new Boolean(true));
        mandatoryMap.put("txtProductCode", new Boolean(false));
        mandatoryMap.put("txtProductDesc", new Boolean(false));
        mandatoryMap.put("txtRatePerKg", new Boolean(false));
        mandatoryMap.put("txtKiloGram", new Boolean(false));
        mandatoryMap.put("txtAmount", new Boolean(false));
        mandatoryMap.put("txtlAcreage",new Boolean(false));
        mandatoryMap.put("txtBags",new Boolean(false));
        mandatoryMap.put("tdtBillDate", new Boolean(false));
        mandatoryMap.put("txtAddress", new Boolean(false));
        mandatoryMap.put("tdtSaleDate", new Boolean(false));
        mandatoryMap.put("txtTotalAmount", new Boolean(false));
    }
    public static void main(String args[]){
        javax.swing.JFrame frame = new javax.swing.JFrame();
        PaddySaleMasterUI ui = new PaddySaleMasterUI();
        frame.getContentPane().add(ui);
        ui.setVisible(true);
        frame.setVisible(true);
        frame.setSize(600,600);
        frame.show();
        ui.show();
    }
    
    
    private void setMaximumLength(){
        txtBillNo.setMaxLength(16);
        txtBillNo.setAllowAll(true);
        txtName.setMaxLength(32);
        txtName.setAllowAll(true);
        txtProductCode.setMaxLength(32);
        txtProductCode.setAllowAll(true);
        txtProductDesc.setMaxLength(32);
        txtProductDesc.setAllowAll(true);
        txtRatePerKg.setMaxLength(32);
        txtRatePerKg.setValidation(new CurrencyValidation());
        txtKiloGram.setMaxLength(32);
        txtKiloGram.setValidation(new NumericValidation());
        txtAmount.setMaxLength(32);
        txtAmount.setValidation(new CurrencyValidation());
        txtlAcreage.setMaxLength(32);
        txtlAcreage.setValidation(new NumericValidation());
        txtBags.setMaxLength(32);
        txtBags.setValidation(new NumericValidation());
        txtAddress.setMaxLength(64);
        txtAddress.setAllowAll(true);
        txtTotalAmount.setMaxLength(64);
        txtTotalAmount.setValidation(new CurrencyValidation());
    }
    
    /** Sets the ComboBoxModel for all the Combos */
    private void initComponentData() {
    }
    private void setObservable() {
        try{
            observable = PaddySaleMasterOB.getInstance();
            observable.addObserver(this);
        }catch(Exception e){
            System.out.println("Exception is caught "+e);
        }
    }
    private void internationalize() {
        resourceBundle = new PaddySaleMasterRB();
        lblBillNo.setText(resourceBundle.getString("lblBillNo"));
        lblBillDate.setText(resourceBundle.getString("lblBillDate"));
        lblName.setText(resourceBundle.getString("lblName"));
        lblAddress.setText(resourceBundle.getString("lblAddress"));
        lblProductCode.setText(resourceBundle.getString("lblProductCode"));
        lblProductDesc.setText(resourceBundle.getString("lblProductDesc"));
        lblRatePerKg.setText(resourceBundle.getString("lblRatePerKg"));
        lblKiloGram.setText(resourceBundle.getString("lblKiloGram"));
        lblBags.setText(resourceBundle.getString("lblBags"));
        lblAmount.setText(resourceBundle.getString("lblAmount"));
        lblSaleDate.setText(resourceBundle.getString("lblSaleDate"));
        lblAcreage.setText(resourceBundle.getString("lblAcreage"));
        lblTotalAmount.setText(resourceBundle.getString("lblTotalAmount"));
        resourceBundle = null;
    }
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
        txtBillNo.setName("txtBillNo");
        txtName.setName("txtName");
        txtProductCode.setName("txtProductCode");
        txtProductDesc.setName("txtProductDesc");
        txtRatePerKg.setName("txtRatePerKg");
        txtKiloGram.setName("txtKiloGram");
        txtAmount.setName("txtAmount");
        txtlAcreage.setName("txtlAcreage");
        txtBags.setName("txtBags");
        tdtBillDate.setName("tdtBillDate");
        txtAddress.setName("txtAddress");
        tdtSaleDate.setName("tdtSaleDate");
        txtTotalAmount.setName("txtTotalAmount");
        lblBillNo.setName("lblBillNo");
        lblBillDate.setName("lblBillDate");
        lblName.setName("lblName");
        lblAddress.setName("lblAddress");
        lblProductCode.setName("lblProductCode");
        lblProductDesc.setName("lblProductDesc");
        lblRatePerKg.setName("lblRatePerKg");
        lblKiloGram.setName("lblKiloGram");
        lblBags.setName("lblBags");
        lblAmount.setName("lblAmount");
        lblSaleDate.setName("lblSaleDate");
        lblAcreage.setName("lblAcreage");
        lblTotalAmount.setName("lblTotalAmount");
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

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
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panAccountInfo = new com.see.truetransact.uicomponent.CPanel();
        panTransaction = new com.see.truetransact.uicomponent.CPanel();
        panSaleMaster = new com.see.truetransact.uicomponent.CPanel();
        lblProductDesc = new com.see.truetransact.uicomponent.CLabel();
        lblBillNo = new com.see.truetransact.uicomponent.CLabel();
        txtBillNo = new com.see.truetransact.uicomponent.CTextField();
        lblAddress = new com.see.truetransact.uicomponent.CLabel();
        txtAddress = new com.see.truetransact.uicomponent.CTextField();
        lblRatePerKg = new com.see.truetransact.uicomponent.CLabel();
        txtRatePerKg = new com.see.truetransact.uicomponent.CTextField();
        lblAmount = new com.see.truetransact.uicomponent.CLabel();
        txtAmount = new com.see.truetransact.uicomponent.CTextField();
        lblTotalAmount = new com.see.truetransact.uicomponent.CLabel();
        txtTotalAmount = new com.see.truetransact.uicomponent.CTextField();
        lblSaleDate = new com.see.truetransact.uicomponent.CLabel();
        tdtSaleDate = new com.see.truetransact.uicomponent.CDateField();
        panProductCode = new com.see.truetransact.uicomponent.CPanel();
        txtProductCode = new com.see.truetransact.uicomponent.CTextField();
        btnProductCode = new com.see.truetransact.uicomponent.CButton();
        lblProductCode = new com.see.truetransact.uicomponent.CLabel();
        lblName = new com.see.truetransact.uicomponent.CLabel();
        txtName = new com.see.truetransact.uicomponent.CTextField();
        panPurchaseInfo = new com.see.truetransact.uicomponent.CPanel();
        srpPurchaseInfo = new com.see.truetransact.uicomponent.CScrollPane();
        tblSaleList = new com.see.truetransact.uicomponent.CTable();
        lblBags = new com.see.truetransact.uicomponent.CLabel();
        txtBags = new com.see.truetransact.uicomponent.CTextField();
        lblKiloGram = new com.see.truetransact.uicomponent.CLabel();
        txtKiloGram = new com.see.truetransact.uicomponent.CTextField();
        panPurchaseBtn = new com.see.truetransact.uicomponent.CPanel();
        btnSaleNew = new com.see.truetransact.uicomponent.CButton();
        btnSaleSave = new com.see.truetransact.uicomponent.CButton();
        btnSaleDelete = new com.see.truetransact.uicomponent.CButton();
        tdtBillDate = new com.see.truetransact.uicomponent.CDateField();
        lblBillDate = new com.see.truetransact.uicomponent.CLabel();
        txtlAcreage = new com.see.truetransact.uicomponent.CTextField();
        lblAcreage = new com.see.truetransact.uicomponent.CLabel();
        txtProductDesc = new com.see.truetransact.uicomponent.CTextField();
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
        setPreferredSize(new java.awt.Dimension(800, 400));

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

        panAccountInfo.setLayout(new java.awt.GridBagLayout());

        panTransaction.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountInfo.add(panTransaction, gridBagConstraints);

        panSaleMaster.setMaximumSize(new java.awt.Dimension(800, 350));
        panSaleMaster.setMinimumSize(new java.awt.Dimension(800, 350));
        panSaleMaster.setPreferredSize(new java.awt.Dimension(800, 350));
        panSaleMaster.setLayout(new java.awt.GridBagLayout());

        lblProductDesc.setText("Product Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 6);
        panSaleMaster.add(lblProductDesc, gridBagConstraints);

        lblBillNo.setText("Bill No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panSaleMaster.add(lblBillNo, gridBagConstraints);

        txtBillNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBillNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBillNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSaleMaster.add(txtBillNo, gridBagConstraints);

        lblAddress.setText("Address");
        lblAddress.setName("lblStreet");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 4);
        panSaleMaster.add(lblAddress, gridBagConstraints);

        txtAddress.setMaxLength(256);
        txtAddress.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAddress.setName("txtStreet");
        txtAddress.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panSaleMaster.add(txtAddress, gridBagConstraints);

        lblRatePerKg.setText("Rate per Unit");
        lblRatePerKg.setName("lblStreet");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panSaleMaster.add(lblRatePerKg, gridBagConstraints);

        txtRatePerKg.setMaxLength(256);
        txtRatePerKg.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRatePerKg.setName("txtStreet");
        txtRatePerKg.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panSaleMaster.add(txtRatePerKg, gridBagConstraints);

        lblAmount.setText("Amount");
        lblAmount.setName("lblPincode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panSaleMaster.add(lblAmount, gridBagConstraints);

        txtAmount.setMaxLength(16);
        txtAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAmount.setName("txtPincode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panSaleMaster.add(txtAmount, gridBagConstraints);

        lblTotalAmount.setText("Total Amount");
        lblTotalAmount.setName("lblPincode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 4);
        panSaleMaster.add(lblTotalAmount, gridBagConstraints);

        txtTotalAmount.setMaxLength(16);
        txtTotalAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTotalAmount.setName("txtPincode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panSaleMaster.add(txtTotalAmount, gridBagConstraints);

        lblSaleDate.setText("Sale Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panSaleMaster.add(lblSaleDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panSaleMaster.add(tdtSaleDate, gridBagConstraints);

        panProductCode.setMinimumSize(new java.awt.Dimension(121, 21));
        panProductCode.setPreferredSize(new java.awt.Dimension(21, 200));
        panProductCode.setLayout(new java.awt.GridBagLayout());

        txtProductCode.setMinimumSize(new java.awt.Dimension(100, 21));
        panProductCode.add(txtProductCode, new java.awt.GridBagConstraints());

        btnProductCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnProductCode.setToolTipText("Account No.");
        btnProductCode.setPreferredSize(new java.awt.Dimension(21, 21));
        btnProductCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductCodeActionPerformed(evt);
            }
        });
        panProductCode.add(btnProductCode, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panSaleMaster.add(panProductCode, gridBagConstraints);

        lblProductCode.setText("Product Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panSaleMaster.add(lblProductCode, gridBagConstraints);

        lblName.setText("Name");
        lblName.setName("lblStreet");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panSaleMaster.add(lblName, gridBagConstraints);

        txtName.setMaxLength(256);
        txtName.setMinimumSize(new java.awt.Dimension(100, 21));
        txtName.setName("txtStreet");
        txtName.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panSaleMaster.add(txtName, gridBagConstraints);

        panPurchaseInfo.setBorder(javax.swing.BorderFactory.createTitledBorder("Sale Info"));
        panPurchaseInfo.setMinimumSize(new java.awt.Dimension(250, 150));
        panPurchaseInfo.setName("panTransInfo");
        panPurchaseInfo.setPreferredSize(new java.awt.Dimension(250, 150));
        panPurchaseInfo.setLayout(new java.awt.GridBagLayout());

        tblSaleList.setModel(new javax.swing.table.DefaultTableModel(
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
        tblSaleList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblSaleListMousePressed(evt);
            }
        });
        srpPurchaseInfo.setViewportView(tblSaleList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panPurchaseInfo.add(srpPurchaseInfo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panSaleMaster.add(panPurchaseInfo, gridBagConstraints);

        lblBags.setText("Bags");
        lblBags.setName("lblPincode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panSaleMaster.add(lblBags, gridBagConstraints);

        txtBags.setMaxLength(16);
        txtBags.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBags.setName("txtPincode");
        txtBags.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBagsFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panSaleMaster.add(txtBags, gridBagConstraints);

        lblKiloGram.setText("Kilo Gram");
        lblKiloGram.setName("lblStreet");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panSaleMaster.add(lblKiloGram, gridBagConstraints);

        txtKiloGram.setMaxLength(256);
        txtKiloGram.setMinimumSize(new java.awt.Dimension(100, 21));
        txtKiloGram.setName("txtStreet");
        txtKiloGram.setValidation(new DefaultValidation());
        txtKiloGram.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtKiloGramFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panSaleMaster.add(txtKiloGram, gridBagConstraints);

        panPurchaseBtn.setLayout(new java.awt.GridBagLayout());

        btnSaleNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnSaleNew.setToolTipText("New");
        btnSaleNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnSaleNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnSaleNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnSaleNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaleNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseBtn.add(btnSaleNew, gridBagConstraints);

        btnSaleSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSaleSave.setToolTipText("Save");
        btnSaleSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnSaleSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnSaleSave.setName("btnContactNoAdd");
        btnSaleSave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnSaleSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaleSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseBtn.add(btnSaleSave, gridBagConstraints);

        btnSaleDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnSaleDelete.setToolTipText("Delete");
        btnSaleDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnSaleDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnSaleDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnSaleDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaleDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseBtn.add(btnSaleDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 13);
        panSaleMaster.add(panPurchaseBtn, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panSaleMaster.add(tdtBillDate, gridBagConstraints);

        lblBillDate.setText("Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panSaleMaster.add(lblBillDate, gridBagConstraints);

        txtlAcreage.setMaxLength(16);
        txtlAcreage.setMinimumSize(new java.awt.Dimension(100, 21));
        txtlAcreage.setName("txtPincode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panSaleMaster.add(txtlAcreage, gridBagConstraints);

        lblAcreage.setText("Acreage");
        lblAcreage.setName("lblPincode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panSaleMaster.add(lblAcreage, gridBagConstraints);

        txtProductDesc.setMaxLength(256);
        txtProductDesc.setMinimumSize(new java.awt.Dimension(100, 21));
        txtProductDesc.setName("txtStreet");
        txtProductDesc.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panSaleMaster.add(txtProductDesc, gridBagConstraints);

        panAccountInfo.add(panSaleMaster, new java.awt.GridBagConstraints());

        getContentPane().add(panAccountInfo, java.awt.BorderLayout.CENTER);

        mbrShareProduct.setName("mbrCustomer");

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

    private void txtBillNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBillNoFocusLost
        // TODO add your handling code here:
        if(txtBillNo.getText().length()>0){
            txtBillNo.setText(CommonUtil.convertObjToStr(txtBillNo.getText()).toUpperCase());
            String billNo = CommonUtil.convertObjToStr(txtBillNo.getText());
            HashMap billNumChkMap = new HashMap();
            billNumChkMap.put("BILL_NUMBER",billNo);
            List list = ClientUtil.executeQuery("getBillNumberChk", billNumChkMap);
            if(list!= null && list.size() > 0){
                ClientUtil.showAlertWindow("Bill Number Already exists!!!");
                txtBillNo.setText("");
            }
        }
    }//GEN-LAST:event_txtBillNoFocusLost
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_DELETE);
        transactionUI.cancelAction(false);
        //        resetUI();
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        viewType = ClientConstants.ACTIONTYPE_DELETE;
        observable.setStatus();
        popUp("DELETE");
        lblStatus.setText("Delete");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        btnSave.setEnabled(true);
        btnDelete.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void txtBagsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBagsFocusLost
        // TODO add your handling code here:
        if(txtRatePerKg.getText().length()> 0 && txtBags.getText().length() > 0){
            double ratePerKg = 0.0;
            double kiloGram = 0.0;
            ratePerKg = CommonUtil.convertObjToDouble(txtRatePerKg.getText()).doubleValue();
            kiloGram = CommonUtil.convertObjToDouble(txtBags.getText()).doubleValue();
            HashMap kiloGramMap = new HashMap();
            kiloGramMap.put("ITEM_CODE",String.valueOf(txtProductCode.getText()));
            List checkKGQuantityLst = ClientUtil.executeQuery("getAvailablePaddyStock",kiloGramMap);
            double kgStock = 0.0;
            if(checkKGQuantityLst != null && checkKGQuantityLst.size() >0){
                kiloGramMap = new HashMap();
                kiloGramMap = (HashMap)checkKGQuantityLst.get(0);
                kgStock = CommonUtil.convertObjToDouble(kiloGramMap.get("QUANTITY")).doubleValue();
            }
            if(kgStock >= kiloGram){
                txtAmount.setText("");
                txtAmount.setText(String.valueOf(ratePerKg * kiloGram ));
                txtAmount.setEnabled(false);
            }
            else{
                txtBags.setText("");
                ClientUtil.showAlertWindow("Contains jus " +String.valueOf(kgStock) + " Bags in stock,Please Enter a value below it.");
            }
        }
    }//GEN-LAST:event_txtBagsFocusLost
    
    private void btnProductCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductCodeActionPerformed
        // TODO add your handling code here:
        popUp("PRODUCTCODE");
    }//GEN-LAST:event_btnProductCodeActionPerformed
    
    
    private void popUp(String actionType){
        this.actionType = actionType;
        if(actionType != null){
            final HashMap viewMap = new HashMap();
            HashMap wheres = new HashMap();
            
            if(actionType.equals(ClientConstants.ACTION_STATUS[2]))  {
                //                for Edit
                ArrayList lst = new ArrayList();
                lst.add("DRF NO");
                viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
                viewMap.put(CommonConstants.MAP_NAME, "getPaddySaleTransDelete");
                viewMap.put(CommonConstants.MAP_WHERE, wheres);
            } else if (actionType.equals("DELETE")){
                viewMap.put(CommonConstants.MAP_NAME, "getPaddySaleTransDelete");
                viewMap.put(CommonConstants.MAP_WHERE, wheres);
            }
            else if(actionType.equals("VIEW")){
                HashMap where = new HashMap();
                where.put("BRANCH_ID", getSelectedBranchID());
                viewMap.put(CommonConstants.MAP_NAME, "getPaddySaleTransView");
                viewMap.put(CommonConstants.MAP_WHERE, where);
                where = null;
            }
            else if(actionType.equals("PRODUCTCODE")){
                HashMap where = new HashMap();
                viewMap.put(CommonConstants.MAP_NAME, "getProductCodeDetailsPurchase");
                viewMap.put(CommonConstants.MAP_WHERE, where);
                where = null;
            }
            
            new com.see.truetransact.ui.common.viewall.ViewAll(this, viewMap,true).show();
        }
        
    }
    
    /** To get data based on customer id received from the popup and populate into the
     * screen
     */
    public void fillData(Object obj) {
        isFilled = true;
        setModified(true);
        final HashMap hash = (HashMap) obj;
        System.out.println("@@@@hash"+hash);
        String st = CommonUtil.convertObjToStr(hash.get("STATUS"));
        if(st.equalsIgnoreCase("DELETED")) {
            flag = true;
        }
        if(hash.get("ITEM_CODE")!=null){
            txtProductCode.setText(CommonUtil.convertObjToStr(hash.get("ITEM_CODE")));
            observable.setTxtProductCode(txtProductCode.getText());
            txtProductDesc.setText(CommonUtil.convertObjToStr(hash.get("ITEM_DESC")));
            observable.setTxtProductDesc(txtProductDesc.getText());
            txtRatePerKg.setText(CommonUtil.convertObjToStr(hash.get("SELLING_PRICE")));
            observable.setTxtRatePerKg(txtRatePerKg.getText());
            if(CommonUtil.convertObjToStr(hash.get("UNIT")).equals("BAGS")){
                lblBags.setVisible(true);
                txtBags.setVisible(true);
                txtKiloGram.setVisible(false);
                lblKiloGram.setVisible(false);
            }else if(CommonUtil.convertObjToStr(hash.get("UNIT")).equals("KILO GRAMS")){
                lblBags.setVisible(false);
                txtBags.setVisible(false);
                txtKiloGram.setVisible(true);
                lblKiloGram.setVisible(true);
            }
            txtRatePerKg.setEnabled(false);
            txtProductDesc.setEnabled(false);
            txtProductCode.setEnabled(false);
        }
        if (observable.getActionType() == (ClientConstants.ACTIONTYPE_EDIT)|| observable.getActionType() ==(ClientConstants.ACTIONTYPE_VIEW)|| observable.getActionType() ==(ClientConstants.ACTIONTYPE_DELETE)|| observable.getActionType() ==(ClientConstants.ACTIONTYPE_AUTHORIZE) ||
        observable.getActionType() == (ClientConstants.ACTIONTYPE_REJECT)){
            isFilled = true;
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW
            || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
            || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
            || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT){
                observable.populateData(hash);
                observable.setSaleID(CommonUtil.convertObjToStr(hash.get("SALE_ID")));
                initTableData();
                btnCancel.setEnabled(true);
                observable.ttNotifyObservers();
            }
        }
        if(viewType == AUTHORIZE){
            ClientUtil.enableDisable(panSaleMaster, false);
        }
        if( observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW_MODE){
            ClientUtil.enableDisable(this, false);
        }
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
            
        }
    }
    private void tblSaleListMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSaleListMousePressed
        familyDetailsFlag = true;
        updateOBFields();
        observable.setNewSale(false);
        observable.populateSale(tblSaleList.getSelectedRow());
        observable.ttNotifyObservers();
        updateSale();
        txtTotalAmount.setEnabled(false);
        if(tblSaleList.getRowCount() > 0){
            String quantity = String.valueOf(tblSaleList.getValueAt(tblSaleList.getSelectedRow(), 2));
            System.out.println("@#$@#$#@$quantity"+quantity);
            if(!quantity.equals("") || quantity.length() > 0){
                txtBags.setVisible(false);
                lblBags.setVisible(false);
                txtKiloGram.setVisible(true);
                lblKiloGram.setVisible(true);
            }else{
                txtBags.setVisible(true);
                lblBags.setVisible(true);
                txtKiloGram.setVisible(false);
                lblKiloGram.setVisible(false);
            }
        }
        //To enable contact buttons for NEW & EDIT
        if( observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            ClientUtil.enableDisable(panSaleMaster,true);
            btnSaleDelete.setEnabled(true);
            btnSaleNew.setEnabled(false);
            btnSaleSave.setEnabled(true);
            
        }else if( observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
        || getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE || actionType.equals("DeletedDetails")){
            //            objEmployeeMasterUISupport.setContactButtonEnableDisableDefault(false,btnContactNew, btnContactDelete, btnContactAdd);
            //            objEmployeeMasterUISupport.setContactAddEnableDisable(false,btnContactAdd);
        }
    }//GEN-LAST:event_tblSaleListMousePressed
    
    private void txtKiloGramFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKiloGramFocusLost
        // TODO add your handling code here:
        if(txtRatePerKg.getText().length()> 0 && txtKiloGram.getText().length() > 0){
            double ratePerKg = 0.0;
            double kiloGram = 0.0;
            ratePerKg = CommonUtil.convertObjToDouble(txtRatePerKg.getText()).doubleValue();
            kiloGram = CommonUtil.convertObjToDouble(txtKiloGram.getText()).doubleValue();
            HashMap kiloGramMap = new HashMap();
            kiloGramMap.put("ITEM_CODE",String.valueOf(txtProductCode.getText()));
            List checkKGQuantityLst = ClientUtil.executeQuery("getAvailablePaddyStock",kiloGramMap);
            double kgStock = 0.0;
            if(checkKGQuantityLst != null && checkKGQuantityLst.size() >0){
                kiloGramMap = new HashMap();
                kiloGramMap = (HashMap)checkKGQuantityLst.get(0);
                kgStock = CommonUtil.convertObjToDouble(kiloGramMap.get("QUANTITY")).doubleValue();
            }
            if(kgStock >= kiloGram){
                txtAmount.setText("");
                txtAmount.setText(String.valueOf(ratePerKg * kiloGram ));
                txtAmount.setEnabled(false);
            }
            else{
                txtKiloGram.setText("");
                ClientUtil.showAlertWindow("Contains jus " +String.valueOf(kgStock) + " KG's in stock,Please Enter a value below it.");
            }
        }
    }//GEN-LAST:event_txtKiloGramFocusLost
    
    private void btnSaleDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaleDeleteActionPerformed
        // TODO add your handling code here:
        try{
            String alertMsg = "deleteWarningMsg";
            int optionSelected = observable.showAlertWindow(alertMsg);
            if(optionSelected==0){
                observable.deleteSale(tblSaleList.getSelectedRow());
                ClientUtil.clearAll(panSaleMaster);
                ClientUtil.enableDisable(panSaleMaster,false);
                btnSaleNew.setEnabled(true);
                btnSaleSave.setEnabled(false);
                btnSaleDelete.setEnabled(false);
                totalAmount();
                observable.setTxtTotalAmount(txtTotalAmount.getText());
                transactionUI.setCallingAmount(observable.getTxtTotalAmount());
                updateOBFields();
                familyDetailsFlag = false ;
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnSaleDeleteActionPerformed
    
    private void btnSaleSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaleSaveActionPerformed
        try{
            updateOBFields();
            
            //            mandatoryMessage = objEmployeeMasterUISupport.checkMandatory(CLASSNAME,panPurchaseMaster);
            //To check whether all the mandatory fields of Contact address have been entered.
            //If not entered properly display alert message, else proceed
            if( mandatoryMessage.length() > 0 ){
                //                objEmployeeMasterUISupport.displayAlert(mandatoryMessage);
            }else{
                if(familyDetailsFlag == false){
                    //if the row is empty
                    observable.sale(-1,familyDetailsFlag);
                }else{
                    
                    observable.sale(tblSaleList.getSelectedRow(),familyDetailsFlag);
                }
                clearWhileSave();
                btnSaleNew.setEnabled(true);
                btnSaleSave.setEnabled(false);
                btnSaleDelete.setEnabled(false);
                totalAmount();
                updateOBFields();
                familyDetailsFlag = false;
                observable.setTxtTotalAmount(txtTotalAmount.getText());
                transactionUI.setCallingAmount(observable.getTxtTotalAmount());
            }
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnSaleSaveActionPerformed
    private void totalAmount(){
        if(tblSaleList.getRowCount() > 0){
            int saleList = tblSaleList.getRowCount();
            double totalAmount = 0.0;
            for(int i = 0; i<saleList ; i++){
                totalAmount += CommonUtil.convertObjToDouble(tblSaleList.getValueAt(i, 3)).doubleValue();
            }
            txtTotalAmount.setText(String.valueOf(totalAmount));
            txtTotalAmount.setEnabled(false);
        }
    }
    private void clearWhileSave(){
        txtProductCode.setText("");
        txtProductDesc.setText("");
        txtRatePerKg.setText("");
        txtKiloGram.setText("");
        txtAmount.setText("");
        txtlAcreage.setText("");
        txtBags.setText("");
        txtBillNo.setEnabled(false);
        txtAddress.setEnabled(false);
        txtName.setEnabled(false);
        txtTotalAmount.setEnabled(false);
        txtProductCode.setEnabled(false);
        txtProductDesc.setEnabled(false);
        txtRatePerKg.setEnabled(false);
        txtKiloGram.setEnabled(false);
        txtAmount.setEnabled(false);
        txtlAcreage.setEnabled(false);
        txtBags.setEnabled(false);
    }
    private void clearWhileNew(){
        txtProductCode.setText("");
        txtProductDesc.setText("");
        txtRatePerKg.setText("");
        txtKiloGram.setText("");
        txtAmount.setText("");
        txtlAcreage.setText("");
        txtBags.setText("");
        txtBillNo.setEnabled(false);
        txtAddress.setEnabled(false);
        txtName.setEnabled(false);
        txtTotalAmount.setEnabled(false);
        txtProductCode.setEnabled(true);
        txtProductDesc.setEnabled(true);
        txtRatePerKg.setEnabled(true);
        txtKiloGram.setEnabled(true);
        txtAmount.setEnabled(true);
        txtlAcreage.setEnabled(true);
        txtBags.setEnabled(true);
    }
    
    private void initTableData(){
        tblSaleList.setModel(observable.getTblSaleList());
    }
    /* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtBillNo(CommonUtil.convertObjToStr(txtBillNo.getText()));
        observable.setTxtName(CommonUtil.convertObjToStr(txtName.getText()));
        observable.setTxtProductCode(CommonUtil.convertObjToStr(txtProductCode.getText()));
        observable.setTxtProductDesc(CommonUtil.convertObjToStr(txtProductDesc.getText()));
        observable.setTxtRatePerKg(CommonUtil.convertObjToStr(txtRatePerKg.getText()));
        observable.setTxtKiloGram(CommonUtil.convertObjToStr(txtKiloGram.getText()));
        observable.setTxtAmount(CommonUtil.convertObjToStr(txtAmount.getText()));
        observable.setTxtlAcreage(CommonUtil.convertObjToStr(txtlAcreage.getText()));
        observable.setTxtBags(CommonUtil.convertObjToStr(txtBags.getText()));
        observable.setTdtBillDate(CommonUtil.convertObjToStr(tdtBillDate.getDateValue()));
        observable.setTxtAddress(CommonUtil.convertObjToStr(txtAddress.getText()));
        observable.setTdtSaleDate(CommonUtil.convertObjToStr(tdtSaleDate.getDateValue()));
        observable.setTxtTotalAmount(CommonUtil.convertObjToStr(txtTotalAmount.getText()));
    }
    private void btnSaleNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaleNewActionPerformed
        // TODO add your handling code here:
        updateOBFields();
        observable.setNewSale(true);
        txtTotalAmount.setEnabled(false);
        ClientUtil.enableDisable(panSaleMaster,true);
        if(tblSaleList.getRowCount() > 0){
            observable.resetWhileSaleSave();
            clearWhileNew();
        }
        else{
            observable.resetSale();
        }
        observable.ttNotifyObservers();
        updateSale();
        tdtBillDate.setDateValue(CommonUtil.convertObjToStr(curDate));
        tdtSaleDate.setDateValue(CommonUtil.convertObjToStr(curDate));
        ClientUtil.enableDisable(panProductCode,false);
        btnProductCode.setEnabled(true);
        btnSaleSave.setEnabled(true);
        btnSaleDelete.setEnabled(false);
        btnSaleNew.setEnabled(false);
        txtTotalAmount.setEnabled(false);
    }//GEN-LAST:event_btnSaleNewActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        viewType = ClientConstants.ACTIONTYPE_NEW;
        observable.setStatus();
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        observable.resetForm();
        setButtonEnableDisable();
        transactionUI.resetObjects();
        btnSaleNew.setEnabled(true);
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        setAuthBtnEnableDisable();
    }
    
    private void setAuthBtnEnableDisable(){
        boolean enableDisable = !btnSave.isEnabled();
        btnAuthorize.setEnabled(enableDisable);
        btnException.setEnabled(enableDisable);
        btnReject.setEnabled(enableDisable);
    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        if( actionType.equals(ClientConstants.VIEW_TYPE_EDIT) || actionType.equals(ClientConstants.VIEW_TYPE_DELETE)){
            //            super.removeEditLock(txtCnDNo.getText());
        }
        actionType=ClientConstants.VIEW_TYPE_CANCEL;
        cancelOperation();
        if(observable.getAuthorizeStatus()!=null)
            //        super.removeEditLock(observable.getID());
            setModified(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        viewType = 0;
        ClientUtil.enableDisable(this,false,false,true);
        setButtonEnableDisable();
        observable.setStatus();
        ClientUtil.clearAll(this);
        if(getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE){
            cifClosingAlert();
        }
        setModified(false);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        btnNew.setEnabled(true);
        btnView.setEnabled(true);
        btnSave.setEnabled(false);
        btnDelete.setEnabled(true);
        panPurchaseBtn.setEnabled(false);
        panSaleMaster.setEnabled(false);
        btnEdit.setEnabled(false);
        lblStatus.setText("            ");
        observable.resetForm();
        btnClose.setEnabled(true);
        
    }//GEN-LAST:event_btnCancelActionPerformed
    private void cancelOperation(){
        if(observable!=null){
            isFilled = false;
            observable.resetForm();
            ClientUtil.enableDisable(this, false);
            setButtonEnableDisable();
            observable.makeToNull();
            observable.ttNotifyObservers();
            resetTransactionUI();
        }
    }
    
    private void resetTransactionUI(){
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
    }
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
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        viewType = ClientConstants.ACTIONTYPE_AUTHORIZE;
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
        btnReject.setEnabled(false);
        btnException.setEnabled(true);
        btnSave.setEnabled(false);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        resourceBundle = new PaddySaleMasterRB();
        final String shareAcctMandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panSaleMaster);
        StringBuffer strBMandatory = new StringBuffer();
        int transactionSize = 0 ;
        if(/*rdoSharewithDrawal.isSelected()==true && */(transactionUI.getOutputTO() == null || (transactionUI.getOutputTO()).size() == 0)){
            strBMandatory.append(resourceBundle.getString("NoRecords"));
            strBMandatory.append("\n");
            //System.out.println("in null chk of UI");
        }
        else{ //if(rdoShareAddition.isSelected()==false){
            transactionSize = (transactionUI.getOutputTO()).size();
            observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
        }
        
        String strMandatory = strBMandatory.toString();
        
        //--- checks whether the Mandatory fields are entered
        if(strMandatory.length()>0){        //--- if all the mandatory fields are not entered,
            CommonMethods.displayAlert(strMandatory);     //--- display the alert
        }else if(strMandatory.length()==0){ //--- if all the values are entered, save the data
            //Call transaction screen here
            //If transactions exist, proceed to save them
            if (transactionSize  > 0/* && rdoSharewithDrawal.isSelected()==true*/) {
                if (!transactionUI.isBtnSaveTransactionDetailsFlag()) {
                    observable.showAlertWindow(resourceBundle.getString("saveInTxDetailsTable"));
                } else if (transactionUI.isBtnSaveTransactionDetailsFlag()) {
                    double transTotalAmt = CommonUtil.convertObjToDouble(transactionUI.getTransactionOB().getLblTotalTransactionAmtVal()).doubleValue();
                    if (ClientUtil.checkTotalAmountTallied(CommonUtil.convertObjToDouble(txtTotalAmount.getText()).doubleValue(), transTotalAmt) == false)
                        ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NOT_TALLY));
                    else {
                        savePerformed();
                        observable.setStatus();
                        observable.setResultStatus();
                        lblStatus.setText(observable.getLblStatus());
                    }
                }
            } else {
                savePerformed();
                observable.setStatus();
                observable.setResultStatus();
                lblStatus.setText(observable.getLblStatus());
            }
            
            //End of Transaction call
            
            
            resourceBundle = null;
        } else {
            CommonMethods.displayAlert(resourceBundle.getString("saveAcctDet"));
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void savePerformed(){
        try{
            updateOBFields();
            observable.setTxtTotalAmount(txtTotalAmount.getText());
            observable.setResult(observable.getActionType());
            observable.doAction();
            observable.makeToNull();
            btnCancelActionPerformed(null);
            observable.ttNotifyObservers();
            observable.setResultStatus();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        viewType = ClientConstants.ACTIONTYPE_VIEW;
        observable.setStatus();
        popUp("VIEW");
        lblStatus.setText("Enquiry");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        btnSave.setEnabled(true);
        btnDelete.setEnabled(false);
        btnView.setEnabled(true);
    }//GEN-LAST:event_btnViewActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        viewType = ClientConstants.ACTIONTYPE_AUTHORIZE;
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnSave.setEnabled(false);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(true);
        
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    public void authorizeStatus(String authorizeStatus) {
        if (viewType== ClientConstants.ACTIONTYPE_AUTHORIZE && isFilled){
            
            ArrayList arrList = new ArrayList();
            HashMap authorizeMap = new HashMap();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("AUTH_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTH_DT",ClientUtil.getCurrentDateWithTime());
            singleAuthorizeMap.put("SALE_ID", observable.getSaleID());
            singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            arrList.add(singleAuthorizeMap);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(authorizeMap,observable.getSaleID());
            viewType = -1;
            super.setOpenForEditBy(observable.getStatusBy());
            singleAuthorizeMap = null;
            authorizeMap = null;
        }else {
            //__ To Save the data in the Internal Frame...
            HashMap whereMap = new HashMap();
            setModified(true);
            whereMap.put(CommonConstants.MAP_NAME, "getPaddySaleTransAuth");
            whereMap.put(CommonConstants.HIERARCHY_ID, ProxyParameters.HIERARCHY_ID);
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            viewType = AUTHORIZE;
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, whereMap);
            authorizeUI.show();
            whereMap = null;
        }
    }
    
    public void authorize(HashMap map,String id) {
        System.out.println("Authorize Map : " + map);
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.set_authorizeMap(map);
            observable.doAction();
            if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
                super.setOpenForEditBy(observable.getStatusBy());
                super.removeEditLock(id);
            }
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
        }
        if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
            isFilled = false;
            super.setOpenForEditBy(observable.getStatusBy());
            observable.setResultStatus();
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
        viewType = ClientConstants.ACTIONTYPE_AUTHORIZE;
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        btnReject.setEnabled(true);
        btnException.setEnabled(false);
        btnSave.setEnabled(false);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    public void updateSale(){
        txtBillNo.setText(observable.getTxtBillNo());
        txtName.setText(observable.getTxtName());
        txtProductCode.setText(observable.getTxtProductCode());
        txtProductDesc.setText(observable.getTxtProductDesc());
        txtRatePerKg.setText(observable.getTxtRatePerKg());
        txtKiloGram.setText(observable.getTxtKiloGram());
        txtAmount.setText(observable.getTxtAmount());
        txtlAcreage.setText(observable.getTxtlAcreage());
        txtBags.setText(observable.getTxtBags());
        tdtBillDate.setDateValue(observable.getTdtBillDate());
        txtAddress.setText(observable.getTxtAddress());
        tdtSaleDate.setDateValue(observable.getTdtSaleDate());
        txtTotalAmount.setText(observable.getTxtTotalAmount());
    }
    public void update(Observable observed, Object arg) {
        txtBillNo.setText(observable.getTxtBillNo());
        txtName.setText(observable.getTxtName());
        txtProductCode.setText(observable.getTxtProductCode());
        txtProductDesc.setText(observable.getTxtProductDesc());
        txtRatePerKg.setText(observable.getTxtRatePerKg());
        txtKiloGram.setText(observable.getTxtKiloGram());
        txtAmount.setText(observable.getTxtAmount());
        txtlAcreage.setText(observable.getTxtlAcreage());
        txtBags.setText(observable.getTxtBags());
        tdtBillDate.setDateValue(observable.getTdtBillDate());
        txtAddress.setText(observable.getTxtAddress());
        tdtSaleDate.setDateValue(observable.getTdtSaleDate());
        txtTotalAmount.setText(observable.getTxtTotalAmount());
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnProductCode;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSaleDelete;
    private com.see.truetransact.uicomponent.CButton btnSaleNew;
    private com.see.truetransact.uicomponent.CButton btnSaleSave;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel lblAcreage;
    private com.see.truetransact.uicomponent.CLabel lblAddress;
    private com.see.truetransact.uicomponent.CLabel lblAmount;
    private com.see.truetransact.uicomponent.CLabel lblBags;
    private com.see.truetransact.uicomponent.CLabel lblBillDate;
    private com.see.truetransact.uicomponent.CLabel lblBillNo;
    private com.see.truetransact.uicomponent.CLabel lblKiloGram;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblName;
    private com.see.truetransact.uicomponent.CLabel lblProductCode;
    private com.see.truetransact.uicomponent.CLabel lblProductDesc;
    private com.see.truetransact.uicomponent.CLabel lblRatePerKg;
    private com.see.truetransact.uicomponent.CLabel lblSaleDate;
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
    private com.see.truetransact.uicomponent.CLabel lblTotalAmount;
    private com.see.truetransact.uicomponent.CMenuBar mbrShareProduct;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAccountInfo;
    private com.see.truetransact.uicomponent.CPanel panProductCode;
    private com.see.truetransact.uicomponent.CPanel panPurchaseBtn;
    private com.see.truetransact.uicomponent.CPanel panPurchaseInfo;
    private com.see.truetransact.uicomponent.CPanel panSaleMaster;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTransaction;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpPurchaseInfo;
    private com.see.truetransact.uicomponent.CTable tblSaleList;
    private javax.swing.JToolBar tbrShareProduct;
    private com.see.truetransact.uicomponent.CDateField tdtBillDate;
    private com.see.truetransact.uicomponent.CDateField tdtSaleDate;
    private com.see.truetransact.uicomponent.CTextField txtAddress;
    private com.see.truetransact.uicomponent.CTextField txtAmount;
    private com.see.truetransact.uicomponent.CTextField txtBags;
    private com.see.truetransact.uicomponent.CTextField txtBillNo;
    private com.see.truetransact.uicomponent.CTextField txtKiloGram;
    private com.see.truetransact.uicomponent.CTextField txtName;
    private com.see.truetransact.uicomponent.CTextField txtProductCode;
    private com.see.truetransact.uicomponent.CTextField txtProductDesc;
    private com.see.truetransact.uicomponent.CTextField txtRatePerKg;
    private com.see.truetransact.uicomponent.CTextField txtTotalAmount;
    private com.see.truetransact.uicomponent.CTextField txtlAcreage;
    // End of variables declaration//GEN-END:variables
    
}
