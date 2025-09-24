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

public class PaddyPurchaseMasterUI extends CInternalFrame implements UIMandatoryField, Observer{
    PaddyPurchaseMasterMRB objMandatoryRB = new PaddyPurchaseMasterMRB();
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.paddyprocurement.PaddyLocalityMasterRB", ProxyParameters.LANGUAGE);
    PaddyPurchaseMasterOB observable = null;
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
    
    /** Creates new form ShareProductUI */
    public PaddyPurchaseMasterUI() {
        initComponents();
        initStartup();
        transactionUI.addToScreen(panTransaction);
        observable.setTransactionOB(transactionUI.getTransactionOB());
        btnCancelActionPerformed(null);
        btnEdit.setEnabled(false);
    }
    
    private void initStartup() {
        curDate = ClientUtil.getCurrentDate();
        setFieldNames();
        internationalize();
        observable = new PaddyPurchaseMasterOB();
        initComponentData();
        setMaximumLength();
        observable.resetForm();
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();
        setMandatoryHashMap();
        initTableData();
        setHelpMessage();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panPurchaseMaster);
        txtTotalAmount.setEnabled(false);
    }
    private void setHelpMessage(){
        objMandatoryRB = new PaddyPurchaseMasterMRB();
        txtProductCode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtProductCode"));
        txtProductDesc.setHelpMessage(lblMsg, objMandatoryRB.getString("txtProductDesc"));
        txtRatePerKg.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRatePerKg"));
        txtKiloGram.setHelpMessage(lblMsg, objMandatoryRB.getString("txtKiloGram"));
        txtAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAmount"));
        txtlAcreage.setHelpMessage(lblMsg, objMandatoryRB.getString("txtlAcreage"));
        txtBags.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBags"));
        txtCnDNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCnDNo"));
        txtAddress.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAddress"));
        txtName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtName"));
        txtLocalityCode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLocalityCode"));
        txtLocalityName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLocalityName"));
        txtTotalAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTotalAmount"));
    }
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtCnDNo", new Boolean(true));
        mandatoryMap.put("txtName", new Boolean(true));
        mandatoryMap.put("txtLocalityCode", new Boolean(false));
        mandatoryMap.put("txtLocalityName", new Boolean(false));
        mandatoryMap.put("txtProductCode", new Boolean(false));
        mandatoryMap.put("txtRatePerKg", new Boolean(false));
        mandatoryMap.put("txtKiloGram", new Boolean(false));
        mandatoryMap.put("txtAmount", new Boolean(false));
        mandatoryMap.put("txtlAcreage",new Boolean(false));
        mandatoryMap.put("txtBags",new Boolean(false));
        mandatoryMap.put("tdtBillDate", new Boolean(false));
        mandatoryMap.put("txtAddress", new Boolean(false));
        mandatoryMap.put("tdtPurchaseDate", new Boolean(false));
        mandatoryMap.put("txtTotalAmount", new Boolean(false));
    }
    public static void main(String args[]){
        javax.swing.JFrame frame = new javax.swing.JFrame();
        PaddyPurchaseMasterUI ui = new PaddyPurchaseMasterUI();
        frame.getContentPane().add(ui);
        ui.setVisible(true);
        frame.setVisible(true);
        frame.setSize(600,600);
        frame.show();
        ui.show();
    }
    
    
    private void setMaximumLength(){
        txtCnDNo.setMaxLength(16);
        txtCnDNo.setAllowAll(true);
        txtName.setMaxLength(32);
        txtName.setAllowAll(true);
        txtLocalityCode.setMaxLength(32);
        txtLocalityCode.setAllowAll(true);
        txtLocalityName.setMaxLength(32);
        txtLocalityName.setAllowAll(true);
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
        //        cboAcctStatus.setModel(observable.getCbmAcctStatus());
        //        cboConstitution.setModel(observable.getCbmConstitution());
        //        cboShareType.setModel(observable.getCbmShareType());
        //        cboMemDivProdType.setModel(observable.getCbmDivProdType());
        //        cboMemDivPayMode.setModel(observable.getCbmDivPayMode());
    }
    private void setObservable() {
        try{
            observable = PaddyPurchaseMasterOB.getInstance();
            observable.addObserver(this);
        }catch(Exception e){
            System.out.println("Exception is caught "+e);
        }
    }
    private void internationalize() {
        resourceBundle = new PaddyPurchaseMasterRB();
        lblCnDNo.setText(resourceBundle.getString("lblCnDNo"));
        lblName.setText(resourceBundle.getString("lblName"));
        lblBillDate.setText(resourceBundle.getString("lblBillDate"));
        lblAddress.setText(resourceBundle.getString("lblAddress"));
        lblLocalityCode.setText(resourceBundle.getString("lblLocalityCode"));
        lblLocalityName.setText(resourceBundle.getString("lblLocalityName"));
        lblProductCode.setText(resourceBundle.getString("lblProductCode"));
        lblProductDesc.setText(resourceBundle.getString("lblProductDesc"));
        lblRatePerKg.setText(resourceBundle.getString("lblRatePerKg"));
        lblKiloGram.setText(resourceBundle.getString("lblKiloGram"));
        lblBags.setText(resourceBundle.getString("lblBags"));
        lblPurchaseDate.setText(resourceBundle.getString("lblPurchaseDate"));
        lblTotalAmount.setText(resourceBundle.getString("lblTotalAmount"));
        lblAcreage.setText(resourceBundle.getString("lblAcreage"));
        lblAmount.setText(resourceBundle.getString("lblAmount"));
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
        txtProductCode.setName("txtProductCode");
        txtProductDesc.setName("txtProductDesc");
        txtRatePerKg.setName("txtRatePerKg");
        txtKiloGram.setName("txtKiloGram");
        txtAmount.setName("txtAmount");
        txtlAcreage.setName("txtlAcreage");
        txtBags.setName("txtBags");
        txtCnDNo.setName("txtCnDNo");
        txtAddress.setName("txtAddress");
        txtName.setName("txtName");
        txtLocalityCode.setName("txtLocalityCode");
        txtLocalityName.setName("txtLocalityName");
        txtTotalAmount.setName("txtTotalAmount");
        lblCnDNo.setName("lblCnDNo");
        lblName.setName("lblName");
        lblBillDate.setName("lblBillDate");
        lblAddress.setName("lblAddress");
        lblLocalityCode.setName("lblLocalityCode");
        lblLocalityName.setName("lblLocalityName");
        lblProductCode.setName("lblProductCode");
        lblProductDesc.setName("lblProductDesc");
        lblRatePerKg.setName("lblRatePerKg");
        lblKiloGram.setName("lblKiloGram");
        lblBags.setName("lblBags");
        lblPurchaseDate.setName("lblPurchaseDate");
        lblTotalAmount.setName("lblTotalAmount");
        lblAcreage.setName("lblAcreage");
        lblAmount.setName("lblAmount");
        
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
        panPurchaseMaster = new com.see.truetransact.uicomponent.CPanel();
        lblProductDesc = new com.see.truetransact.uicomponent.CLabel();
        lblCnDNo = new com.see.truetransact.uicomponent.CLabel();
        txtCnDNo = new com.see.truetransact.uicomponent.CTextField();
        lblAddress = new com.see.truetransact.uicomponent.CLabel();
        txtAddress = new com.see.truetransact.uicomponent.CTextField();
        lblRatePerKg = new com.see.truetransact.uicomponent.CLabel();
        txtRatePerKg = new com.see.truetransact.uicomponent.CTextField();
        lblAmount = new com.see.truetransact.uicomponent.CLabel();
        txtAmount = new com.see.truetransact.uicomponent.CTextField();
        lblTotalAmount = new com.see.truetransact.uicomponent.CLabel();
        txtTotalAmount = new com.see.truetransact.uicomponent.CTextField();
        lblPurchaseDate = new com.see.truetransact.uicomponent.CLabel();
        tdtPurchaseDate = new com.see.truetransact.uicomponent.CDateField();
        lblLocalityCode = new com.see.truetransact.uicomponent.CLabel();
        panSupplierCode = new com.see.truetransact.uicomponent.CPanel();
        txtLocalityCode = new com.see.truetransact.uicomponent.CTextField();
        btnLocalityCode = new com.see.truetransact.uicomponent.CButton();
        lblLocalityName = new com.see.truetransact.uicomponent.CLabel();
        panProductCode = new com.see.truetransact.uicomponent.CPanel();
        txtProductCode = new com.see.truetransact.uicomponent.CTextField();
        btnProductCode = new com.see.truetransact.uicomponent.CButton();
        lblProductCode = new com.see.truetransact.uicomponent.CLabel();
        lblName = new com.see.truetransact.uicomponent.CLabel();
        txtName = new com.see.truetransact.uicomponent.CTextField();
        panPurchaseInfo = new com.see.truetransact.uicomponent.CPanel();
        srpPurchaseInfo = new com.see.truetransact.uicomponent.CScrollPane();
        tblPurchaseList = new com.see.truetransact.uicomponent.CTable();
        lblBags = new com.see.truetransact.uicomponent.CLabel();
        txtBags = new com.see.truetransact.uicomponent.CTextField();
        lblKiloGram = new com.see.truetransact.uicomponent.CLabel();
        txtKiloGram = new com.see.truetransact.uicomponent.CTextField();
        panPurchaseBtn = new com.see.truetransact.uicomponent.CPanel();
        btnPurchaseNew = new com.see.truetransact.uicomponent.CButton();
        btnPurchaseSave = new com.see.truetransact.uicomponent.CButton();
        btnPurchaseDelete = new com.see.truetransact.uicomponent.CButton();
        txtLocalityName = new com.see.truetransact.uicomponent.CTextField();
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
        setMaximumSize(new java.awt.Dimension(825, 650));
        setMinimumSize(new java.awt.Dimension(825, 650));
        setPreferredSize(new java.awt.Dimension(825, 650));

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

        panPurchaseMaster.setMaximumSize(new java.awt.Dimension(800, 350));
        panPurchaseMaster.setMinimumSize(new java.awt.Dimension(800, 350));
        panPurchaseMaster.setPreferredSize(new java.awt.Dimension(800, 350));
        panPurchaseMaster.setLayout(new java.awt.GridBagLayout());

        lblProductDesc.setText("Product Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        panPurchaseMaster.add(lblProductDesc, gridBagConstraints);

        lblCnDNo.setText("C&D No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPurchaseMaster.add(lblCnDNo, gridBagConstraints);

        txtCnDNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCnDNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCnDNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseMaster.add(txtCnDNo, gridBagConstraints);

        lblAddress.setText("Address");
        lblAddress.setName("lblStreet");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 4);
        panPurchaseMaster.add(lblAddress, gridBagConstraints);

        txtAddress.setMaxLength(256);
        txtAddress.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAddress.setName("txtStreet");
        txtAddress.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPurchaseMaster.add(txtAddress, gridBagConstraints);

        lblRatePerKg.setText("Rate per Unit");
        lblRatePerKg.setName("lblStreet");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPurchaseMaster.add(lblRatePerKg, gridBagConstraints);

        txtRatePerKg.setMaxLength(256);
        txtRatePerKg.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRatePerKg.setName("txtStreet");
        txtRatePerKg.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPurchaseMaster.add(txtRatePerKg, gridBagConstraints);

        lblAmount.setText("Amount");
        lblAmount.setName("lblPincode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPurchaseMaster.add(lblAmount, gridBagConstraints);

        txtAmount.setMaxLength(16);
        txtAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAmount.setName("txtPincode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPurchaseMaster.add(txtAmount, gridBagConstraints);

        lblTotalAmount.setText("Total Amount");
        lblTotalAmount.setName("lblPincode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 4);
        panPurchaseMaster.add(lblTotalAmount, gridBagConstraints);

        txtTotalAmount.setMaxLength(16);
        txtTotalAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTotalAmount.setName("txtPincode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPurchaseMaster.add(txtTotalAmount, gridBagConstraints);

        lblPurchaseDate.setText("Purchase Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panPurchaseMaster.add(lblPurchaseDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panPurchaseMaster.add(tdtPurchaseDate, gridBagConstraints);

        lblLocalityCode.setText("Locality Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        panPurchaseMaster.add(lblLocalityCode, gridBagConstraints);

        panSupplierCode.setMinimumSize(new java.awt.Dimension(121, 21));
        panSupplierCode.setPreferredSize(new java.awt.Dimension(21, 200));
        panSupplierCode.setLayout(new java.awt.GridBagLayout());

        txtLocalityCode.setMinimumSize(new java.awt.Dimension(100, 21));
        panSupplierCode.add(txtLocalityCode, new java.awt.GridBagConstraints());

        btnLocalityCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnLocalityCode.setToolTipText("Account No.");
        btnLocalityCode.setPreferredSize(new java.awt.Dimension(21, 21));
        btnLocalityCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLocalityCodeActionPerformed(evt);
            }
        });
        panSupplierCode.add(btnLocalityCode, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseMaster.add(panSupplierCode, gridBagConstraints);

        lblLocalityName.setText("Locality Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        panPurchaseMaster.add(lblLocalityName, gridBagConstraints);

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
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseMaster.add(panProductCode, gridBagConstraints);

        lblProductCode.setText("Product Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPurchaseMaster.add(lblProductCode, gridBagConstraints);

        lblName.setText("Name");
        lblName.setName("lblStreet");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPurchaseMaster.add(lblName, gridBagConstraints);

        txtName.setMaxLength(256);
        txtName.setMinimumSize(new java.awt.Dimension(100, 21));
        txtName.setName("txtStreet");
        txtName.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPurchaseMaster.add(txtName, gridBagConstraints);

        panPurchaseInfo.setBorder(javax.swing.BorderFactory.createTitledBorder("Purchase Info"));
        panPurchaseInfo.setMinimumSize(new java.awt.Dimension(250, 150));
        panPurchaseInfo.setName("panTransInfo");
        panPurchaseInfo.setPreferredSize(new java.awt.Dimension(250, 150));
        panPurchaseInfo.setLayout(new java.awt.GridBagLayout());

        tblPurchaseList.setModel(new javax.swing.table.DefaultTableModel(
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
        tblPurchaseList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblPurchaseListMousePressed(evt);
            }
        });
        srpPurchaseInfo.setViewportView(tblPurchaseList);

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
        panPurchaseMaster.add(panPurchaseInfo, gridBagConstraints);

        lblBags.setText("Bags");
        lblBags.setName("lblPincode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPurchaseMaster.add(lblBags, gridBagConstraints);

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
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPurchaseMaster.add(txtBags, gridBagConstraints);

        lblKiloGram.setText("Kilo Gram");
        lblKiloGram.setName("lblStreet");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPurchaseMaster.add(lblKiloGram, gridBagConstraints);

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
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPurchaseMaster.add(txtKiloGram, gridBagConstraints);

        panPurchaseBtn.setLayout(new java.awt.GridBagLayout());

        btnPurchaseNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnPurchaseNew.setToolTipText("New");
        btnPurchaseNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnPurchaseNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnPurchaseNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnPurchaseNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPurchaseNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseBtn.add(btnPurchaseNew, gridBagConstraints);

        btnPurchaseSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnPurchaseSave.setToolTipText("Save");
        btnPurchaseSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnPurchaseSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnPurchaseSave.setName("btnContactNoAdd");
        btnPurchaseSave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnPurchaseSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPurchaseSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseBtn.add(btnPurchaseSave, gridBagConstraints);

        btnPurchaseDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnPurchaseDelete.setToolTipText("Delete");
        btnPurchaseDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnPurchaseDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnPurchaseDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnPurchaseDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPurchaseDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseBtn.add(btnPurchaseDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 13);
        panPurchaseMaster.add(panPurchaseBtn, gridBagConstraints);

        txtLocalityName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseMaster.add(txtLocalityName, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panPurchaseMaster.add(tdtBillDate, gridBagConstraints);

        lblBillDate.setText("Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panPurchaseMaster.add(lblBillDate, gridBagConstraints);

        txtlAcreage.setMaxLength(16);
        txtlAcreage.setMinimumSize(new java.awt.Dimension(100, 21));
        txtlAcreage.setName("txtPincode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPurchaseMaster.add(txtlAcreage, gridBagConstraints);

        lblAcreage.setText("Acreage");
        lblAcreage.setName("lblPincode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPurchaseMaster.add(lblAcreage, gridBagConstraints);

        txtProductDesc.setMaxLength(256);
        txtProductDesc.setMinimumSize(new java.awt.Dimension(100, 21));
        txtProductDesc.setName("txtStreet");
        txtProductDesc.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPurchaseMaster.add(txtProductDesc, gridBagConstraints);

        panAccountInfo.add(panPurchaseMaster, new java.awt.GridBagConstraints());

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
    
    private void txtCnDNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCnDNoFocusLost
        // TODO add your handling code here:
        if(txtCnDNo.getText().length()>0){
            txtCnDNo.setText(CommonUtil.convertObjToStr(txtCnDNo.getText()).toUpperCase());
            String cndNo = CommonUtil.convertObjToStr(txtCnDNo.getText());
            HashMap cndNumChkMap = new HashMap();
            cndNumChkMap.put("CND_NUMBER",cndNo);
            List list = ClientUtil.executeQuery("getCndNumberChk", cndNumChkMap);
            if(list!= null && list.size() > 0){
                ClientUtil.showAlertWindow("C & D Number Already exists!!!");
                txtCnDNo.setText("");
            }
        }
    }//GEN-LAST:event_txtCnDNoFocusLost
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_DELETE);
        transactionUI.cancelAction(false);
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
        btnCancel.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void txtBagsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBagsFocusLost
        // TODO add your handling code here:
        if(txtRatePerKg.getText().length()> 0 && txtBags.getText().length() > 0){
            double ratePerKg = 0.0;
            double kiloGram = 0.0;
            ratePerKg = CommonUtil.convertObjToDouble(txtRatePerKg.getText()).doubleValue();
            kiloGram = CommonUtil.convertObjToDouble(txtBags.getText()).doubleValue();
            txtAmount.setText("");
            txtAmount.setText(String.valueOf(ratePerKg * kiloGram ));
            txtAmount.setEnabled(false);
        }
    }//GEN-LAST:event_txtBagsFocusLost
    
    private void btnProductCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductCodeActionPerformed
        // TODO add your handling code here:
        popUp("PRODUCTCODE");
    }//GEN-LAST:event_btnProductCodeActionPerformed
    
    private void btnLocalityCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLocalityCodeActionPerformed
        // TODO add your handling code here:
        popUp("LOCALITYCODE");
    }//GEN-LAST:event_btnLocalityCodeActionPerformed
    
    
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
                viewMap.put(CommonConstants.MAP_NAME, "getPaddyPurchaseTransDelete");
                viewMap.put(CommonConstants.MAP_WHERE, wheres);
            } else if (actionType.equals("DELETE")){
                viewMap.put(CommonConstants.MAP_NAME, "getPaddyPurchaseTransDelete");
                viewMap.put(CommonConstants.MAP_WHERE, wheres);
            }
            else if(actionType.equals("VIEW")){
                HashMap where = new HashMap();
                where.put("BRANCH_ID", getSelectedBranchID());
                viewMap.put(CommonConstants.MAP_NAME, "getPaddyPurchaseTransView");
                viewMap.put(CommonConstants.MAP_WHERE, where);
                where = null;
            }
            else if(actionType.equals("LOCALITYCODE")){
                HashMap where = new HashMap();
                viewMap.put(CommonConstants.MAP_NAME, "getLocalityCodeDetailsPurchase");
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
        if(hash.get("LOCALITY_CODE")!=null){
            txtLocalityCode.setText(CommonUtil.convertObjToStr(hash.get("LOCALITY_CODE")));
            observable.setTxtLocalityCode( txtLocalityCode.getText());
            txtLocalityName.setText(CommonUtil.convertObjToStr(hash.get("LOCALITY_NAME")));
            observable.setTxtLocalityName(txtLocalityName.getText());
            txtLocalityName.setEnabled(false);
            txtLocalityCode.setEnabled(false);
        }
        if(hash.get("ITEM_CODE")!=null){
            txtProductCode.setText(CommonUtil.convertObjToStr(hash.get("ITEM_CODE")));
            observable.setTxtProductCode(txtProductCode.getText());
            txtProductDesc.setText(CommonUtil.convertObjToStr(hash.get("ITEM_DESC")));
            observable.setTxtProductDesc(txtProductDesc.getText());
            txtRatePerKg.setText(CommonUtil.convertObjToStr(hash.get("PURCHASE_PRICE")));
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
        if (observable.getActionType() == (ClientConstants.ACTIONTYPE_EDIT)||observable.getActionType() ==(ClientConstants.ACTIONTYPE_DELETE)|| observable.getActionType() ==(ClientConstants.ACTIONTYPE_AUTHORIZE) ||
        observable.getActionType() ==(ClientConstants.ACTIONTYPE_VIEW) ||
        observable.getActionType() == (ClientConstants.ACTIONTYPE_REJECT)){
            isFilled = true;
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW
            || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
            || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
            || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT){
                observable.populateData(hash);
                observable.setPurchaseId(CommonUtil.convertObjToStr(hash.get("PURCHASE_ID")));
                initTableData();
                btnCancel.setEnabled(true);
                observable.ttNotifyObservers();
            }
        }
        if(viewType == AUTHORIZE){
            ClientUtil.enableDisable(panPurchaseMaster, false);
        }
        if( observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW_MODE){
            ClientUtil.enableDisable(this, false);
        }
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
            
        }
    }
    private void tblPurchaseListMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPurchaseListMousePressed
        familyDetailsFlag = true;
        updateOBFields();
        observable.setNewPurchase(false);
        observable.populatePurchase(tblPurchaseList.getSelectedRow());
        observable.ttNotifyObservers();
        updatePurchase();
        txtTotalAmount.setEnabled(false);
        if(tblPurchaseList.getRowCount() > 0){
            String quantity = String.valueOf(tblPurchaseList.getValueAt(tblPurchaseList.getSelectedRow(), 2));
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
            ClientUtil.enableDisable(panPurchaseMaster,true);
            btnPurchaseDelete.setEnabled(true);
            btnPurchaseNew.setEnabled(false);
            btnPurchaseSave.setEnabled(true);
            
        }else if( observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
        || getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE || actionType.equals("DeletedDetails")){
            //            objEmployeeMasterUISupport.setContactButtonEnableDisableDefault(false,btnContactNew, btnContactDelete, btnContactAdd);
            //            objEmployeeMasterUISupport.setContactAddEnableDisable(false,btnContactAdd);
        }
    }//GEN-LAST:event_tblPurchaseListMousePressed
    
    private void txtKiloGramFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKiloGramFocusLost
        // TODO add your handling code here:
        if(txtRatePerKg.getText().length()> 0 && txtKiloGram.getText().length() > 0){
            double ratePerKg = 0.0;
            double kiloGram = 0.0;
            ratePerKg = CommonUtil.convertObjToDouble(txtRatePerKg.getText()).doubleValue();
            kiloGram = CommonUtil.convertObjToDouble(txtKiloGram.getText()).doubleValue();
            txtAmount.setText("");
            txtAmount.setText(String.valueOf(ratePerKg * kiloGram ));
            txtAmount.setEnabled(false);
        }
    }//GEN-LAST:event_txtKiloGramFocusLost
    
    private void btnPurchaseDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPurchaseDeleteActionPerformed
        // TODO add your handling code here:
        try{
            String alertMsg = "deleteWarningMsg";
            int optionSelected = observable.showAlertWindow(alertMsg);
            if(optionSelected==0){
                observable.deletePurchase(tblPurchaseList.getSelectedRow());
                ClientUtil.clearAll(panPurchaseMaster);
                ClientUtil.enableDisable(panPurchaseMaster,false);
                btnPurchaseNew.setEnabled(true);
                btnPurchaseSave.setEnabled(false);
                btnPurchaseDelete.setEnabled(false);
                totalAmount();
                observable.setTxtTotalAmount(txtTotalAmount.getText());
                transactionUI.setCallingAmount(observable.getTxtTotalAmount());
                updateOBFields();
                familyDetailsFlag = false ;
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnPurchaseDeleteActionPerformed
    
    private void btnPurchaseSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPurchaseSaveActionPerformed
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
                    observable.purchase(-1,familyDetailsFlag);
                }else{
                    
                    observable.purchase(tblPurchaseList.getSelectedRow(),familyDetailsFlag);
                }
                clearWhileSave();
                btnPurchaseNew.setEnabled(true);
                btnPurchaseSave.setEnabled(false);
                btnPurchaseDelete.setEnabled(false);
                totalAmount();
                updateOBFields();
                familyDetailsFlag = false;
                observable.setTxtTotalAmount(txtTotalAmount.getText());
                transactionUI.setCallingAmount(observable.getTxtTotalAmount());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnPurchaseSaveActionPerformed
    private void totalAmount(){
        if(tblPurchaseList.getRowCount() > 0){
            int purchaseList = tblPurchaseList.getRowCount();
            double totalAmount = 0.0;
            for(int i = 0; i<purchaseList ; i++){
                totalAmount += CommonUtil.convertObjToDouble(tblPurchaseList.getValueAt(i, 3)).doubleValue();
            }
            txtTotalAmount.setText(String.valueOf(totalAmount));
            txtTotalAmount.setEnabled(false);
        }
    }
    private void clearWhileNew(){
        txtProductCode.setText("");
        txtProductDesc.setText("");
        txtRatePerKg.setText("");
        txtKiloGram.setText("");
        txtAmount.setText("");
        txtlAcreage.setText("");
        txtBags.setText("");
        txtCnDNo.setEnabled(false);
        txtAddress.setEnabled(false);
        txtName.setEnabled(false);
        txtLocalityCode.setEnabled(false);
        txtLocalityName.setEnabled(false);
        txtTotalAmount.setEnabled(false);
        txtProductCode.setEnabled(true);
        txtProductDesc.setEnabled(true);
        txtRatePerKg.setEnabled(true);
        txtKiloGram.setEnabled(true);
        txtAmount.setEnabled(true);
        txtlAcreage.setEnabled(true);
        txtBags.setEnabled(true);
    }
    private void clearWhileSave(){
        txtProductCode.setText("");
        txtProductDesc.setText("");
        txtRatePerKg.setText("");
        txtKiloGram.setText("");
        txtAmount.setText("");
        txtlAcreage.setText("");
        txtBags.setText("");
        txtCnDNo.setEnabled(false);
        txtAddress.setEnabled(false);
        txtName.setEnabled(false);
        txtLocalityCode.setEnabled(false);
        txtLocalityName.setEnabled(false);
        txtTotalAmount.setEnabled(false);
        txtProductCode.setEnabled(false);
        txtProductDesc.setEnabled(false);
        txtRatePerKg.setEnabled(false);
        txtKiloGram.setEnabled(false);
        txtAmount.setEnabled(false);
        txtlAcreage.setEnabled(false);
        txtBags.setEnabled(false);
    }
    
    private void initTableData(){
        tblPurchaseList.setModel(observable.getTblPurchaseList());
    }
    /* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtCnDNo(CommonUtil.convertObjToStr(txtCnDNo.getText()));
        observable.setTxtName(CommonUtil.convertObjToStr(txtName.getText()));
        observable.setTxtLocalityCode(CommonUtil.convertObjToStr(txtLocalityCode.getText()));
        observable.setTxtLocalityName(CommonUtil.convertObjToStr(txtLocalityName.getText()));
        observable.setTxtProductCode(CommonUtil.convertObjToStr(txtProductCode.getText()));
        observable.setTxtProductDesc(CommonUtil.convertObjToStr(txtProductDesc.getText()));
        observable.setTxtRatePerKg(CommonUtil.convertObjToStr(txtRatePerKg.getText()));
        observable.setTxtKiloGram(CommonUtil.convertObjToStr(txtKiloGram.getText()));
        observable.setTxtAmount(CommonUtil.convertObjToStr(txtAmount.getText()));
        observable.setTxtlAcreage(CommonUtil.convertObjToStr(txtlAcreage.getText()));
        observable.setTxtBags(CommonUtil.convertObjToStr(txtBags.getText()));
        observable.setTdtBillDate(CommonUtil.convertObjToStr(tdtBillDate.getDateValue()));
        observable.setTxtAddress(CommonUtil.convertObjToStr(txtAddress.getText()));
        observable.setTdtPurchaseDate(CommonUtil.convertObjToStr(tdtPurchaseDate.getDateValue()));
        observable.setTxtTotalAmount(CommonUtil.convertObjToStr(txtTotalAmount.getText()));
    }
    private void btnPurchaseNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPurchaseNewActionPerformed
        // TODO add your handling code here:
        updateOBFields();
        observable.setNewPurchase(true);
        txtTotalAmount.setEnabled(false);
        ClientUtil.enableDisable(panPurchaseMaster,true);
        if(tblPurchaseList.getRowCount() > 0){
            observable.resetWhilePurchaseSave();
            clearWhileNew();
        }
        else{
            observable.resetPurchase();
        }
        observable.ttNotifyObservers();
        updatePurchase();
        tdtBillDate.setDateValue(CommonUtil.convertObjToStr(curDate));
        tdtPurchaseDate.setDateValue(CommonUtil.convertObjToStr(curDate));
        ClientUtil.enableDisable(panProductCode,false);
        ClientUtil.enableDisable(panSupplierCode,false);
        btnProductCode.setEnabled(true);
        btnLocalityCode.setEnabled(true);
        btnPurchaseSave.setEnabled(true);
        btnPurchaseDelete.setEnabled(false);
        btnPurchaseNew.setEnabled(false);
        txtTotalAmount.setEnabled(false);
    }//GEN-LAST:event_btnPurchaseNewActionPerformed
    
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
        btnPurchaseNew.setEnabled(true);
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
            super.removeEditLock(txtCnDNo.getText());
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
        panPurchaseMaster.setEnabled(false);
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
        resourceBundle = new PaddyPurchaseMasterRB();
        final String shareAcctMandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panPurchaseMaster);
        StringBuffer strBMandatory = new StringBuffer();
        
        int transactionSize = 0 ;
        if(/*rdoSharewithDrawal.isSelected()==true && */(transactionUI.getOutputTO() == null || (transactionUI.getOutputTO()).size() == 0)){
            strBMandatory.append(resourceBundle.getString("NoRecords"));
            strBMandatory.append("\n");
            //System.out.println("in null chk of UI");
        }
        else{ 
            transactionSize = (transactionUI.getOutputTO()).size();
            observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
        }
        //--- checks the Mandatory data for ShareAccount screen
        if(shareAcctMandatoryMessage.length()>0){
            strBMandatory.append(shareAcctMandatoryMessage);
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
                    //                        int noOfShares = getNoOfShares();
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
        btnSave.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
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
            singleAuthorizeMap.put("PURCHASE_ID", observable.getPurchaseId());
            singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            arrList.add(singleAuthorizeMap);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(authorizeMap,observable.getPurchaseId());
            viewType = -1;
            super.setOpenForEditBy(observable.getStatusBy());
            singleAuthorizeMap = null;
            authorizeMap = null;
            
        }else {
            //__ To Save the data in the Internal Frame...
            HashMap whereMap = new HashMap();
            setModified(true);
            whereMap.put(CommonConstants.MAP_NAME, "getPaddyPurchaseTransAuth");
            //            panEditDelete = DRFTRANSACTION;
            //            pan = DRFTRANSACTION;
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
    public void updatePurchase(){
        txtCnDNo.setText(observable.getTxtCnDNo());
        txtName.setText(observable.getTxtName());
        txtLocalityCode.setText(observable.getTxtLocalityCode());
        txtLocalityName.setText(observable.getTxtLocalityName());
        txtProductCode.setText(observable.getTxtProductCode());
        txtProductDesc.setText(observable.getTxtProductDesc());
        txtRatePerKg.setText(observable.getTxtRatePerKg());
        txtKiloGram.setText(observable.getTxtKiloGram());
        txtAmount.setText(observable.getTxtAmount());
        txtlAcreage.setText(observable.getTxtlAcreage());
        txtBags.setText(observable.getTxtBags());
        tdtBillDate.setDateValue(observable.getTdtBillDate());
        txtAddress.setText(observable.getTxtAddress());
        tdtPurchaseDate.setDateValue(observable.getTdtPurchaseDate());
        txtTotalAmount.setText(observable.getTxtTotalAmount());
    }
    public void update(Observable observed, Object arg) {
        txtCnDNo.setText(observable.getTxtCnDNo());
        txtName.setText(observable.getTxtName());
        txtLocalityCode.setText(observable.getTxtLocalityCode());
        txtLocalityName.setText(observable.getTxtLocalityName());
        txtProductCode.setText(observable.getTxtProductCode());
        txtProductDesc.setText(observable.getTxtProductDesc());
        txtRatePerKg.setText(observable.getTxtRatePerKg());
        txtKiloGram.setText(observable.getTxtKiloGram());
        txtAmount.setText(observable.getTxtAmount());
        txtlAcreage.setText(observable.getTxtlAcreage());
        txtBags.setText(observable.getTxtBags());
        tdtBillDate.setDateValue(observable.getTdtBillDate());
        txtAddress.setText(observable.getTxtAddress());
        tdtPurchaseDate.setDateValue(observable.getTdtPurchaseDate());
        txtTotalAmount.setText(observable.getTxtTotalAmount());
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnLocalityCode;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnProductCode;
    private com.see.truetransact.uicomponent.CButton btnPurchaseDelete;
    private com.see.truetransact.uicomponent.CButton btnPurchaseNew;
    private com.see.truetransact.uicomponent.CButton btnPurchaseSave;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel lblAcreage;
    private com.see.truetransact.uicomponent.CLabel lblAddress;
    private com.see.truetransact.uicomponent.CLabel lblAmount;
    private com.see.truetransact.uicomponent.CLabel lblBags;
    private com.see.truetransact.uicomponent.CLabel lblBillDate;
    private com.see.truetransact.uicomponent.CLabel lblCnDNo;
    private com.see.truetransact.uicomponent.CLabel lblKiloGram;
    private com.see.truetransact.uicomponent.CLabel lblLocalityCode;
    private com.see.truetransact.uicomponent.CLabel lblLocalityName;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblName;
    private com.see.truetransact.uicomponent.CLabel lblProductCode;
    private com.see.truetransact.uicomponent.CLabel lblProductDesc;
    private com.see.truetransact.uicomponent.CLabel lblPurchaseDate;
    private com.see.truetransact.uicomponent.CLabel lblRatePerKg;
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
    private com.see.truetransact.uicomponent.CPanel panPurchaseMaster;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panSupplierCode;
    private com.see.truetransact.uicomponent.CPanel panTransaction;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpPurchaseInfo;
    private com.see.truetransact.uicomponent.CTable tblPurchaseList;
    private javax.swing.JToolBar tbrShareProduct;
    private com.see.truetransact.uicomponent.CDateField tdtBillDate;
    private com.see.truetransact.uicomponent.CDateField tdtPurchaseDate;
    private com.see.truetransact.uicomponent.CTextField txtAddress;
    private com.see.truetransact.uicomponent.CTextField txtAmount;
    private com.see.truetransact.uicomponent.CTextField txtBags;
    private com.see.truetransact.uicomponent.CTextField txtCnDNo;
    private com.see.truetransact.uicomponent.CTextField txtKiloGram;
    private com.see.truetransact.uicomponent.CTextField txtLocalityCode;
    private com.see.truetransact.uicomponent.CTextField txtLocalityName;
    private com.see.truetransact.uicomponent.CTextField txtName;
    private com.see.truetransact.uicomponent.CTextField txtProductCode;
    private com.see.truetransact.uicomponent.CTextField txtProductDesc;
    private com.see.truetransact.uicomponent.CTextField txtRatePerKg;
    private com.see.truetransact.uicomponent.CTextField txtTotalAmount;
    private com.see.truetransact.uicomponent.CTextField txtlAcreage;
    // End of variables declaration//GEN-END:variables
    
}
