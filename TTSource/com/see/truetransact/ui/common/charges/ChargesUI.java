/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ChargesUI.java
 *
 * Created on December 22, 2004, 5:15 PM
 */

package com.see.truetransact.ui.common.charges;

import java.util.Observer;
import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.MandatoryDBCheck;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.PercentageValidation;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;         //added by Rajesh
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.transferobject.common.charges.ChargesTO;
import java.util.Date;
/**
 *
 * @author  152691
 */
public class ChargesUI extends com.see.truetransact.uicomponent.CInternalFrame implements com.see.truetransact.uimandatory.UIMandatoryField, java.util.Observer{
    HashMap mandatoryMap = null;
    private ChargesOB observable ;
    final int EDIT=0,DELETE=1,CUSTOMERID=2;
    int ACTION=-1;
    int rowSelected = -1, updateTab = -1;
    final String AUTHORIZE="Authorize";
    private String viewType= "";
    private boolean tableCliked=false;
    private ArrayList key=new ArrayList();
    private ArrayList value=new ArrayList();
    private Date currDt = null;
    /** Creates new form ChargesUI */
    public ChargesUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        setFieldNames();
        setMandatoryHashMap();
        internationalize();
        setObservable();
        initComponentData();
        setMaxLength();
        setButtonEnableDisable();
        enableDisableBtnGrp(false);
        enableForm(false);
        ClientUtil.enableDisable(this, false);
        btnEdit.setVisible(false) ;
        btnDelete.setVisible(false);
    }
    
    private void initComponentData() {
        cboChargeType.setModel(observable.getCbmChargeType());
        cboRateType.setModel(observable.getCbmRateType());
        cboProductType.setModel(observable.getCbmProductType());
        tblData.setModel(observable.getTblData());
        //cboProductId.setModel(observable.getCbmProductId());
    }
    
    private void setObservable() {
        /* Implementing Singleton pattern */
        observable = ChargesOB.getInstance();
        observable.addObserver(this);
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
        cboChargeType.setName("cboChargeType");
        cboRateType.setName("cboRateType");
        lblChargeType.setName("lblChargeType");
        lblFixedRate.setName("lblFixedRate");
        lblForEvery.setName("lblForEvery");
        lblFrmAmt.setName("lblFrmAmt");
        lblPercentage.setName("lblPercentage");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace5.setName("lblSpace5");
        lblToAmt.setName("lblToAmt");
        panCharges.setName("panCharges");
        panForEvery.setName("panForEvery");
        txtFixRate.setName("txtFixRate");
        txtForEvery.setName("txtForEvery");
        txtFromAmt.setName("txtFromAmt");
        txtPercent.setName("txtPercent");
        txtRateVal.setName("txtRateVal");
        txtToAmt.setName("txtToAmt");
    }
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        ChargesRB resourceBundle = new ChargesRB() ;
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblFixedRate.setText(resourceBundle.getString("lblFixedRate"));
        btnException.setText(resourceBundle.getString("btnException"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblForEvery.setText(resourceBundle.getString("lblForEvery"));
        lblPercentage.setText(resourceBundle.getString("lblPercentage"));
        lblChargeType.setText(resourceBundle.getString("lblChargeType"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblFrmAmt.setText(resourceBundle.getString("lblFrmAmt"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblToAmt.setText(resourceBundle.getString("lblToAmt"));
    }
    
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        cboProductType.setSelectedItem(observable.getCboProductType());
        cboChargeType.setSelectedItem(observable.getCboChargeType());
        cboProductId.setSelectedItem(observable.getCboProductId());
        txtPercent.setText(observable.getTxtPercent());
        txtFixRate.setText(observable.getTxtFixRate());
        tdtStartDt.setDateValue(observable.getTdtFromDt());
        tdtEndDt.setDateValue(observable.getTdtToDt());
        txtFromAmt.setText(observable.getTxtFromAmt());
        txtToAmt.setText(observable.getTxtToAmt());
        txtForEvery.setText(observable.getTxtForEvery());
        cboRateType.setSelectedItem(observable.getCboRateType());
        txtRateVal.setText(observable.getTxtRateVal());
        tblData.setModel(observable.getTblData());
    }
    
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setCboProductId((String) cboProductId.getSelectedItem());
        observable.setCboProductType((String)cboProductType.getSelectedItem());
        observable.setCboChargeType((String) cboChargeType.getSelectedItem());
        observable.setTxtPercent(txtPercent.getText());
        observable.setTxtFixRate(txtFixRate.getText());
        observable.setTdtFromDt(tdtStartDt.getDateValue());
        observable.setTdtToDt(tdtEndDt.getDateValue());
        observable.setTxtFromAmt(txtFromAmt.getText());
        observable.setTxtToAmt(txtToAmt.getText());
        observable.setTxtForEvery(txtForEvery.getText());
        observable.setTxtRateVal(txtRateVal.getText());
        observable.setCboRateType((String) cboRateType.getSelectedItem());
        System.out.println("Out from updateOBFields...");
    }
    
    
/* Auto Generated Method - setMandatoryHashMap()
 
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboProductId", new Boolean(true));
        mandatoryMap.put("cboProductType", new Boolean(true));
        mandatoryMap.put("cboChargeType", new Boolean(true));
        mandatoryMap.put("txtPercent", new Boolean(true));
        mandatoryMap.put("txtFixRate", new Boolean(true));
        mandatoryMap.put("txtFromAmt", new Boolean(true));
        mandatoryMap.put("txtToAmt", new Boolean(true));
        mandatoryMap.put("txtForEvery", new Boolean(true));
        mandatoryMap.put("txtRateVal", new Boolean(true));
        mandatoryMap.put("cboRateType", new Boolean(true));
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tbrOperativeAcctProduct = new javax.swing.JToolBar();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace7 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace8 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace9 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace10 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace11 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace12 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panChargeMain = new com.see.truetransact.uicomponent.CPanel();
        panCharges = new com.see.truetransact.uicomponent.CPanel();
        lblPercentage = new com.see.truetransact.uicomponent.CLabel();
        txtPercent = new com.see.truetransact.uicomponent.CTextField();
        lblFixedRate = new com.see.truetransact.uicomponent.CLabel();
        txtFixRate = new com.see.truetransact.uicomponent.CTextField();
        lblFrmAmt = new com.see.truetransact.uicomponent.CLabel();
        txtFromAmt = new com.see.truetransact.uicomponent.CTextField();
        lblToAmt = new com.see.truetransact.uicomponent.CLabel();
        txtToAmt = new com.see.truetransact.uicomponent.CTextField();
        panForEvery = new com.see.truetransact.uicomponent.CPanel();
        txtForEvery = new com.see.truetransact.uicomponent.CTextField();
        txtRateVal = new com.see.truetransact.uicomponent.CTextField();
        cboRateType = new com.see.truetransact.uicomponent.CComboBox();
        lblForEvery = new com.see.truetransact.uicomponent.CLabel();
        panButtons = new com.see.truetransact.uicomponent.CPanel();
        btnTabNew = new com.see.truetransact.uicomponent.CButton();
        btnTabSave = new com.see.truetransact.uicomponent.CButton();
        btnTabDelete = new com.see.truetransact.uicomponent.CButton();
        lblChargeType = new com.see.truetransact.uicomponent.CLabel();
        cboChargeType = new com.see.truetransact.uicomponent.CComboBox();
        lblProductType = new com.see.truetransact.uicomponent.CLabel();
        lblProductId = new com.see.truetransact.uicomponent.CLabel();
        cboProductId = new com.see.truetransact.uicomponent.CComboBox();
        cboProductType = new com.see.truetransact.uicomponent.CComboBox();
        sptCharges = new com.see.truetransact.uicomponent.CSeparator();
        tdtStartDt = new com.see.truetransact.uicomponent.CDateField();
        lblStartDt = new com.see.truetransact.uicomponent.CLabel();
        tdtEndDt = new com.see.truetransact.uicomponent.CDateField();
        lblEndDt = new com.see.truetransact.uicomponent.CLabel();
        btnGetCharges = new com.see.truetransact.uicomponent.CButton();
        panChargeTable = new com.see.truetransact.uicomponent.CPanel();
        cScrollPane1 = new com.see.truetransact.uicomponent.CScrollPane();
        tblData = new com.see.truetransact.uicomponent.CTable();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
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
        setFont(new java.awt.Font("SansSerif", 0, 10)); // NOI18N
        setMinimumSize(new java.awt.Dimension(750, 500));
        setPreferredSize(new java.awt.Dimension(750, 500));

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnNew);

        lblSpace7.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace7.setText("     ");
        lblSpace7.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace7.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace7.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace7);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnEdit);

        lblSpace8.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace8.setText("     ");
        lblSpace8.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace8.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace8.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace8);

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

        lblSpace9.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace9.setText("     ");
        lblSpace9.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace9.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace9.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace9);

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

        lblSpace10.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace10.setText("     ");
        lblSpace10.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace10.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace10.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace10);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnException);

        lblSpace11.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace11.setText("     ");
        lblSpace11.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace11.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace11.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace11);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnReject);

        lblSpace5.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrOperativeAcctProduct.add(btnPrint);

        lblSpace12.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace12.setText("     ");
        lblSpace12.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace12.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace12.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace12);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnClose);

        getContentPane().add(tbrOperativeAcctProduct, java.awt.BorderLayout.NORTH);

        panChargeMain.setLayout(new java.awt.GridBagLayout());

        panCharges.setMinimumSize(new java.awt.Dimension(350, 400));
        panCharges.setPreferredSize(new java.awt.Dimension(350, 400));
        panCharges.setLayout(new java.awt.GridBagLayout());

        lblPercentage.setText("Percentage");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(lblPercentage, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(txtPercent, gridBagConstraints);

        lblFixedRate.setText("Fixed Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(lblFixedRate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(txtFixRate, gridBagConstraints);

        lblFrmAmt.setText("From Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(lblFrmAmt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(txtFromAmt, gridBagConstraints);

        lblToAmt.setText("To Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(lblToAmt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(txtToAmt, gridBagConstraints);

        panForEvery.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panForEvery.add(txtForEvery, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panForEvery.add(txtRateVal, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panForEvery.add(cboRateType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(panForEvery, gridBagConstraints);

        lblForEvery.setText("For every");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(lblForEvery, gridBagConstraints);

        panButtons.setLayout(new java.awt.GridBagLayout());

        btnTabNew.setText("New");
        btnTabNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTabNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButtons.add(btnTabNew, gridBagConstraints);

        btnTabSave.setText("Save");
        btnTabSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTabSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButtons.add(btnTabSave, gridBagConstraints);

        btnTabDelete.setText("Delete");
        btnTabDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTabDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButtons.add(btnTabDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(panButtons, gridBagConstraints);

        lblChargeType.setText("Types of Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(lblChargeType, gridBagConstraints);

        cboChargeType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboChargeTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(cboChargeType, gridBagConstraints);

        lblProductType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(lblProductType, gridBagConstraints);

        lblProductId.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(lblProductId, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(cboProductId, gridBagConstraints);

        cboProductType.setPreferredSize(new java.awt.Dimension(27, 22));
        cboProductType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 75;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panCharges.add(cboProductType, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(sptCharges, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(tdtStartDt, gridBagConstraints);

        lblStartDt.setText("Effective Start Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(lblStartDt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(tdtEndDt, gridBagConstraints);

        lblEndDt.setText("Effective End Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(lblEndDt, gridBagConstraints);

        btnGetCharges.setText("...");
        btnGetCharges.setPreferredSize(new java.awt.Dimension(23, 25));
        btnGetCharges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGetChargesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(btnGetCharges, gridBagConstraints);

        panChargeMain.add(panCharges, new java.awt.GridBagConstraints());

        panChargeTable.setMinimumSize(new java.awt.Dimension(400, 300));
        panChargeTable.setPreferredSize(new java.awt.Dimension(400, 300));
        panChargeTable.setLayout(new java.awt.GridBagLayout());

        cScrollPane1.setPreferredSize(new java.awt.Dimension(400, 400));

        tblData.setModel(new javax.swing.table.DefaultTableModel(
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
        tblData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDataMousePressed(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tblDataMouseExited(evt);
            }
        });
        cScrollPane1.setViewportView(tblData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panChargeTable.add(cScrollPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 20, 0);
        panChargeMain.add(panChargeTable, gridBagConstraints);

        getContentPane().add(panChargeMain, java.awt.BorderLayout.CENTER);

        panStatus.setMinimumSize(new java.awt.Dimension(110, 19));
        panStatus.setPreferredSize(new java.awt.Dimension(110, 19));
        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace4.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace4, gridBagConstraints);

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

        mnuProcess.setText("Process");
        mnuProcess.setToolTipText("Menu");

        mitNew.setText("New");
        mitNew.setToolTipText("");
        mitNew.setEnabled(false);
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.setEnabled(false);
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.setEnabled(false);
        mnuProcess.add(mitDelete);

        sptDelete.setEnabled(false);
        mnuProcess.add(sptDelete);

        mitSave.setText("Save");
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancle");
        mnuProcess.add(mitCancel);

        sptCancel.setEnabled(false);
        mnuProcess.add(sptCancel);

        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mitClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mitCloseMouseClicked(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrMain.add(mnuProcess);

        setJMenuBar(mbrMain);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblDataMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_tblDataMouseExited

    private void btnTabDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTabDeleteActionPerformed
        // TODO add your handling code here:
        if(tblData.getSelectedRow()!= -1)
        observable.deleteTableRow(tblData.getSelectedRow());
        else
        {
            ClientUtil.showMessageWindow("choose any one record from table");
            return;
        }
    }//GEN-LAST:event_btnTabDeleteActionPerformed

    private void tblDataMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMousePressed
        // TODO add your handling code here:
        tableCliked=true;
        observable.setObservableValues(tblData.getSelectedRow());
        
        
    }//GEN-LAST:event_tblDataMousePressed

    private void btnTabSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTabSaveActionPerformed
        // TODO add your handling code here:
        String startDt=tdtStartDt.getDateValue();
        String fromAmt=txtFromAmt.getText();
        String nofolio=txtPercent.getText();
        if(! (startDt !=null && startDt.length()>0)){
            ClientUtil.showMessageWindow("Enter the Start Dt");
            return;
        }else if(! (fromAmt !=null && fromAmt.length()>0)){
             ClientUtil.showMessageWindow("Enter the From Amt");
             return;
             
        }else if(! (nofolio !=null && nofolio.length()>0)){
            String chargetype=CommonUtil.convertObjToStr(((ComboBoxModel)cboChargeType.getModel()).getKeyForSelected());
            if(chargetype.equals("FOLIOCHG"))
                ClientUtil.showMessageWindow("Enter the No Of Folio");
            else
                ClientUtil.showMessageWindow("Enter the Percentage");
            return;
        }else{

        updateOBFields();
        observable.setChargeTableValues(tableCliked,tblData.getSelectedRow());
        resetFields();
        enableDetailFields(false);
        }
    }//GEN-LAST:event_btnTabSaveActionPerformed

    private void btnTabNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTabNewActionPerformed
        // TODO add your handling code here:
        enableDetailFields(true);
        resetNewValues();
        tableCliked=false;
        
    }//GEN-LAST:event_btnTabNewActionPerformed
private void resetNewValues(){
    tdtStartDt.setDateValue("");
    tdtEndDt.setDateValue("");
    txtFromAmt.setText("");
    txtToAmt.setText("");
    txtPercent.setText("");
    txtFixRate.setText("");
    txtForEvery.setText("");
    cboRateType.setSelectedItem("");
    txtRateVal.setText("");
}
    private void cboChargeTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboChargeTypeActionPerformed
        // TODO add your handling code here:
//        if((ComboBoxModel)cboProductId.getModel()).getSelectedItem().equals(""))))
        enableForm(true);
        String chargeType=CommonUtil.convertObjToStr(((ComboBoxModel)cboChargeType.getModel()).getKeyForSelected());
        if(chargeType !=null && (!chargeType.equals("")))
        if(chargeType.equals("FOLIOCHG")){
            lblPercentage.setText("NumFreeFolio");
            visibleValue(false);
        }
        else{
            lblPercentage.setText("Percentage");
            visibleValue(true);
        }
    }//GEN-LAST:event_cboChargeTypeActionPerformed
private void visibleValue(boolean flag){
        txtFixRate.setVisible(flag);
        txtForEvery.setVisible(flag);
        lblFixedRate.setVisible(flag);
        lblForEvery.setVisible(flag);
        cboRateType.setVisible(flag);
        txtRateVal.setVisible(flag);
        btnTabDelete.setVisible(flag);
}
    private void cboProductTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductTypeActionPerformed
        // TODO add your handling code here:
        String prodType=CommonUtil.convertObjToStr(((ComboBoxModel)cboProductType.getModel()).getKeyForSelected());
        if (prodType!=null && prodType.length()>0)
        if(prodType.equals("GL")){
            cboProductId.setEnabled(false);
            cboProductId.setSelectedItem("");
        }else{
            cboProductId.setEnabled(true);
            observable.setCbmProductId(prodType);
            cboProductId.setModel(observable.getCbmProductId());
        }

        
    }//GEN-LAST:event_cboProductTypeActionPerformed
private void getKeyValue(HashMap keyValue){
    key=(ArrayList) keyValue.get(CommonConstants.KEY);
    value=(ArrayList) keyValue.get(CommonConstants.VALUE);
    
}
    private void btnGetChargesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGetChargesActionPerformed
        // TODO add your handling code here:
        observable.setCboProductType((String)((ComboBoxModel)cboProductType.getModel()).getKeyForSelected());
        observable.setCboProductId((String)((ComboBoxModel)cboProductId.getModel()).getKeyForSelected());
        observable.setCboChargeType((String)((ComboBoxModel)cboChargeType.getModel()).getKeyForSelected());
        enableMasterFields(false);
        btnTabNew.setEnabled(true);
        observable.getSavedCharges();

    }//GEN-LAST:event_btnGetChargesActionPerformed

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed

    private void mitCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mitCloseMouseClicked
        // TODO add your handling code here:
    }/**///GEN-LAST:event_mitCloseMouseClicked
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        lblStatus.setText(observable.getLblStatus());
        popUpItems(DELETE);
        for(int i=0;i<observable.getDatabaseOp().size();i++){
            ChargesTO chargesto =(ChargesTO)observable.getDatabaseOp().get(i);
            chargesto.setStatus("DELETED");
            observable.getDatabaseOp().set(i,chargesto);
//        savePerformed();
        }
//        ClientUtil.enableDisable(this,false);
         btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        observable.resetStatus();
        setButtonEnableDisable();
//        panCharges.setEnabled(true);
        observable.resetMaster();
        resetFields();
        ClientUtil.enableDisable(panCharges, false);
        btnTabNew.setEnabled(false);
        btnTabSave.setEnabled(false);
        btnTabDelete.setEnabled(false);
        viewType = "";
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        tableCliked=false;
//       cboProductType.setEnabled(false); 
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        popUpItems(EDIT);
        cboProductId.setSelectedItem(observable.getCboProductId());
         btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed

    /** This method helps in popoualting the data from the data base
     * @param Action the argument is passed according to the command issued
     */
    private void popUpItems(int Action) {
        if (Action == EDIT || Action == DELETE){
            lblStatus.setText(ClientConstants.ACTION_STATUS[0]);
        }
        final HashMap viewMap = new HashMap();
        ACTION=Action;
        if ( Action == EDIT || Action == DELETE){
            viewMap.put(CommonConstants.MAP_NAME, "getAllChargesTO");
        }
        new ViewAll(this, viewMap).show();
    }    
    
    private void enableMasterFields(boolean var){
        cboProductId.setEnabled(var);
        cboProductType.setEnabled(var);
        cboChargeType.setEnabled(var);
        btnGetCharges.setEnabled(var) ;
    }
    
    private void enableDetailFields (boolean var) {
        tdtStartDt.setEnabled(var);
        tdtEndDt.setEnabled(var);
        txtFromAmt.setEnabled(var);
        txtToAmt.setEnabled(var);
        txtPercent.setEnabled(var);
        txtFixRate.setEnabled(var);
        txtForEvery.setEnabled(var);
        cboRateType.setEnabled(var);
        txtRateVal.setEnabled(var);
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.setStatus();
        ClientUtil.enableDisable(this, true);
        enableForm(false);
        setButtonEnableDisable();
        enableMasterFields(true);
         btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
//        observable.resetMaster();
//        resetFields();
        
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
//        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
        
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        boolean maxAmt=chekAmountValidation();
        if(maxAmt){
            ClientUtil.showMessageWindow("max amount 999999999 shoude be set last slap rate");
            return;
        }
        final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panCharges);
        /* mandatoryMessage length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
       
        System.out.println("observable.getDatabaseOp() = " + observable.getDatabaseOp());
        if (mandatoryMessage.length() > 0){
            displayAlert(mandatoryMessage);
        }else{
            System.out.println("In Else Case");
            savePerformed();
            btnCancelActionPerformed(evt) ;
             btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    //checking for max amount is9 9999999
    private boolean chekAmountValidation(){
        int count=observable.getDatabaseOp().size();
        for(int j=0;j<count;j++){
            ChargesTO chargesto=(ChargesTO)observable.getDatabaseOp().get(j);
            if((j+1)==count){
                if(99999999==chargesto.getToAmt().doubleValue()){
                    return false;
                    
                }
            }
        }
       return true; 
    }
    private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
   
    private void disableEditFields(){
        cboProductType.setEnabled(false);
        cboProductId.setEnabled(false);
        cboChargeType.setEnabled(false);
        txtFromAmt.setEnabled(false);
        txtToAmt.setEnabled(false);
    }
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        HashMap tempHash = new HashMap();
        if (ACTION == EDIT || ACTION==DELETE){
            setButtonEnableDisable();
        }
        ClientUtil.enableDisable(this, true);
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || viewType == AUTHORIZE){
            tempHash.put(CommonConstants.PRODUCT_TYPE, hash.get("PRODUCT_TYPE"));
            tempHash.put(CommonConstants.PRODUCT_ID, hash.get("PRODUCT_ID"));
            tempHash.put(CommonConstants.CHRG_TYPE, hash.get("CHARGE_TYPE"));
            System.out.println("Hash : "+hash+" / tempHash : "+tempHash);
            actionEditDelete(tempHash);
            ClientUtil.enableDisable(panCharges,false);
            btnCancel.enable(!btnNew.isEnabled());
            //disableEditFields();
            if(viewType==AUTHORIZE) {
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
             }
        }
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
    }
    
    private void actionEditDelete(HashMap hash){
            observable.setStatus();
            observable.populateData(hash);
    }
    
    
    private void savePerformed(){
        updateOBFields();
        observable.doAction();
        //ClientUtil.enableDisable(this, false);
        //setButtonEnableDisable();
        resetFields();
        observable.setResultStatus();
        lblStatus.setText(observable.getLblStatus());
    }
    
    private void resetFields(){
        observable.resetForm();
    }
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(btnNew.isEnabled());
        btnDelete.setEnabled(btnNew.isEnabled());//!
        mitNew.setEnabled(!mitNew.isEnabled());
        mitEdit.setEnabled(mitNew.isEnabled());
        mitDelete.setEnabled(mitNew.isEnabled());
        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(!mitNew.isEnabled());
        mitCancel.setEnabled(!mitNew.isEnabled());
        lblStatus.setText(observable.getLblStatus());
    }

    private void enableDisableBtnGrp(boolean val){
        btnTabDelete.setEnabled(val) ;
        btnTabSave.setEnabled(val);
        btnTabNew.setEnabled(val);
    }
    
    public void enableForm(boolean val){
        cboChargeType.setEnabled(val);
        cboProductId.setEnabled(val);
        cboProductType.setEnabled(val);
        cboRateType.setEnabled(val);
        tdtStartDt.setEnabled(val);
        tdtEndDt.setEnabled(val);
        txtFixRate.setEnabled(val);
        txtForEvery.setEnabled(val);
        txtFromAmt.setEnabled(val);
        txtPercent.setEnabled(val);
        txtRateVal.setEnabled(val);
        txtToAmt.setEnabled(val);
        btnTabDelete.setEnabled(val) ;
        btnTabSave.setEnabled(val);
        btnTabNew.setEnabled(val);
    }
    /** To perform the appropriate operation */
    
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnGetCharges;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnTabDelete;
    private com.see.truetransact.uicomponent.CButton btnTabNew;
    private com.see.truetransact.uicomponent.CButton btnTabSave;
    private com.see.truetransact.uicomponent.CScrollPane cScrollPane1;
    private com.see.truetransact.uicomponent.CComboBox cboChargeType;
    private com.see.truetransact.uicomponent.CComboBox cboProductId;
    private com.see.truetransact.uicomponent.CComboBox cboProductType;
    private com.see.truetransact.uicomponent.CComboBox cboRateType;
    private com.see.truetransact.uicomponent.CLabel lblChargeType;
    private com.see.truetransact.uicomponent.CLabel lblEndDt;
    private com.see.truetransact.uicomponent.CLabel lblFixedRate;
    private com.see.truetransact.uicomponent.CLabel lblForEvery;
    private com.see.truetransact.uicomponent.CLabel lblFrmAmt;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblPercentage;
    private com.see.truetransact.uicomponent.CLabel lblProductId;
    private com.see.truetransact.uicomponent.CLabel lblProductType;
    private com.see.truetransact.uicomponent.CLabel lblSpace10;
    private com.see.truetransact.uicomponent.CLabel lblSpace11;
    private com.see.truetransact.uicomponent.CLabel lblSpace12;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace7;
    private com.see.truetransact.uicomponent.CLabel lblSpace8;
    private com.see.truetransact.uicomponent.CLabel lblSpace9;
    private com.see.truetransact.uicomponent.CLabel lblStartDt;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblToAmt;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panButtons;
    private com.see.truetransact.uicomponent.CPanel panChargeMain;
    private com.see.truetransact.uicomponent.CPanel panChargeTable;
    private com.see.truetransact.uicomponent.CPanel panCharges;
    private com.see.truetransact.uicomponent.CPanel panForEvery;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptCancel;
    private com.see.truetransact.uicomponent.CSeparator sptCharges;
    private javax.swing.JSeparator sptDelete;
    private com.see.truetransact.uicomponent.CTable tblData;
    private javax.swing.JToolBar tbrOperativeAcctProduct;
    private com.see.truetransact.uicomponent.CDateField tdtEndDt;
    private com.see.truetransact.uicomponent.CDateField tdtStartDt;
    private com.see.truetransact.uicomponent.CTextField txtFixRate;
    private com.see.truetransact.uicomponent.CTextField txtForEvery;
    private com.see.truetransact.uicomponent.CTextField txtFromAmt;
    private com.see.truetransact.uicomponent.CTextField txtPercent;
    private com.see.truetransact.uicomponent.CTextField txtRateVal;
    private com.see.truetransact.uicomponent.CTextField txtToAmt;
    // End of variables declaration//GEN-END:variables
    
    
    private void setMaxLength(){
        //Set max length for fields
        txtFixRate.setMaxLength(15);
        txtForEvery.setMaxLength(15);
        txtFromAmt.setMaxLength(15);
        txtPercent.setMaxLength(15);
        txtRateVal.setMaxLength(15);
        txtToAmt.setMaxLength(15);
        
        //Allow only numeric values
        txtFixRate.setValidation(new CurrencyValidation());
        txtForEvery.setValidation(new CurrencyValidation());
        txtFromAmt.setValidation(new CurrencyValidation());
        txtPercent.setValidation(new PercentageValidation());
        txtRateVal.setValidation(new CurrencyValidation(3,2));
        txtToAmt.setValidation(new CurrencyValidation());
        
    }
    public static void main(String args[]){
        javax.swing.JFrame f = new javax.swing.JFrame();
        ChargesUI ch = new ChargesUI();
        f.getContentPane().add(ch);
        ch.show();
        ch.setSize(900, 600);
        f.show();
        f.setSize(900, 600);
    }
    
/** Does the authorization for the row selected in the AuthorizationUI screen */
     public void authorizeStatus(String authorizeStatus) {
        
        if (!viewType.equals(AUTHORIZE)){
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getChargesAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeCharges");
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            System.out.println("Entered Authorise...." + viewType);
        } else if (viewType.equals(AUTHORIZE)){
            Date curDate = (Date) currDt.clone();
            System.out.println("In Authorise....");
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("USER_ID", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("CHARGE TYPE", (String)((ComboBoxModel)cboChargeType.getModel()).getKeyForSelected());
            singleAuthorizeMap.put("PRODUCT TYPE", (String)((ComboBoxModel)cboProductType.getModel()).getKeyForSelected());
            singleAuthorizeMap.put("FROM AMT", txtFromAmt.getText());
            singleAuthorizeMap.put("PRODUCT ID", (String)((ComboBoxModel)cboProductId.getModel()).getKeyForSelected());
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, curDate);
            
            Date StDt = DateUtil.getDateMMDDYYYY(tdtStartDt.getDateValue());
            if(StDt != null){
            Date stDate = (Date) curDate.clone();
            stDate.setDate(StDt.getDate());
            stDate.setMonth(StDt.getMonth());
            stDate.setYear(StDt.getYear());
//            singleAuthorizeMap.put("START DT", DateUtil.getDateMMDDYYYY(tdtStartDt.getDateValue()));
            singleAuthorizeMap.put("START DT", stDate);
            }else{
                 singleAuthorizeMap.put("START DT", DateUtil.getDateMMDDYYYY(tdtStartDt.getDateValue()));
            }
            
            Date EndDt = DateUtil.getDateMMDDYYYY(tdtEndDt.getDateValue());
            if(EndDt != null){
            Date endDate = (Date) curDate.clone();
            endDate.setDate(EndDt.getDate());
            endDate.setMonth(EndDt.getMonth());
            endDate.setYear(EndDt.getYear());
//            singleAuthorizeMap.put("END DT", DateUtil.getDateMMDDYYYY(tdtEndDt.getDateValue()));
            singleAuthorizeMap.put("END DT", endDate);
            }else{
                 singleAuthorizeMap.put("END DT", DateUtil.getDateMMDDYYYY(tdtEndDt.getDateValue()));
            }
            
            singleAuthorizeMap.put("TO AMT", txtToAmt.getText());
            System.out.println("Before ClientUtil.execute....");
            ClientUtil.execute("authorizeCharges", singleAuthorizeMap);
            viewType = "";
            btnCancelActionPerformed(null);
        }
    }    
    
}
