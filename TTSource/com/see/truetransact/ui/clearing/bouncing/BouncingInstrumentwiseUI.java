/******************************************************************************
 * 1> Proper data to be filled in Reason for Bouncing and  Clearing Type      *
 *    (Lookup_Master --> LOOKUP_ID = INWARD.BOUNCTING_REASON )                *
 *    (Lookup_Master --> LOOKUP_ID = INWARD.CLEARING_TYPE )                   *
 *****************************************************************************/


/* Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BouncingInstrumentwiseUI.java
 *
 * Created on April 7, 2004, 10:45 AM
 */

package com.see.truetransact.ui.clearing.bouncing;

import com.see.truetransact.ui.clearing.bouncing.BouncingInstrumentwiseRB;
import com.see.truetransact.ui.clearing.bouncing.BouncingInstrumentwiseOB;
import com.see.truetransact.ui.clearing.bouncing.BouncingInstrumentwiseMRB;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.clientproxy.ProxyParameters;

import com.see.truetransact.ui.TrueTransactMain;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Observer;
import java.util.Observable;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author  Lohith R.
 */
public class BouncingInstrumentwiseUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer,UIMandatoryField {
    private BouncingInstrumentwiseOB observable;
    HashMap mandatoryMap;
    final int EDIT=0,DELETE=1,SERIALNUMBER=2, AUTHORIZE=3;
    int ACTION=-1;
    boolean isFilled = false;
    String BOUNCING_ID = "";
    private Date currDt = null;
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.clearing.bouncing.BouncingInstrumentwiseRB", ProxyParameters.LANGUAGE);
    
    
    /** Creates new form BouncingInstrumentwiseUI */
    public BouncingInstrumentwiseUI() {
        initComponents();
        initStartUP();
    }
    
    /** Initialzation of UI */
    private void initStartUP(){
        currDt = ClientUtil.getCurrentDate();
        setObservable();
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        initComponentData();
        setMaximumLength();
        setHelpMessage();
        observable.resetStatus();
        observable.resetForm();
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();
    }
    
    /** Implementing Singleton pattern */
    private void setObservable() {
        observable = BouncingInstrumentwiseOB.getInstance();
        observable.addObserver(this);
    }
    
    /** Auto Generated Method - setFieldNames()
     * This method assigns name for all the components.
     * Other functions are working based on this name. */
    private void setFieldNames() {
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnSave.setName("btnSave");
        btnException.setName("btnException");
        btnAuthorize.setName("btnAuthorize");
        btnReject.setName("btnReject");
        btnClearingSerialNo.setName("btnClearingSerialNo");
        cboBouncingType.setName("cboBouncingType");
        cboClearingType.setName("cboClearingType");
        //        cboReasonforBouncing.setName("cboReasonforBouncing");
        txtReasonforBouncing.setName("txtReasonforBouncing");
        chkPresentAgain.setName("chkPresentAgain");
        lblAccountHd.setName("lblAccountHd");
        lblAccountHead.setName("lblAccountHead");
        lblAccountNum.setName("lblAccountNum");
        lblAccountNumber.setName("lblAccountNumber");
        lblAmount.setName("lblAmount");
        lblAmt.setName("lblAmt");
        lblBankCd.setName("lblBankCd");
        lblBankCode.setName("lblBankCode");
        lblBouncingType.setName("lblBouncingType");
        lblBranchCd.setName("lblBranchCd");
        lblBranchCode.setName("lblBranchCode");
        lblClearDt.setName("lblClearDt");
        lblClearType.setName("lblClearType");
        lblClearingDate.setName("lblClearingDate");
        lblClearingDt.setName("lblClearingDt");
        lblClearingSerialNo.setName("lblClearingSerialNo");
        lblClearingTy.setName("lblClearingTy");
        lblClearingType.setName("lblClearingType");
        lblInstrumentDate.setName("lblInstrumentDate");
        lblInstrumentDt.setName("lblInstrumentDt");
        lblInstrumentNum.setName("lblInstrumentNum");
        lblInstrumentNumber.setName("lblInstrumentNumber");
        lblInstrumentTy.setName("lblInstrumentTy");
        lblInstrumentType.setName("lblInstrumentType");
        lblInwardScheduleNo.setName("lblInwardScheduleNo");
        lblMsg.setName("lblMsg");
        lblName.setName("lblName");
        lblNm.setName("lblNm");
        lblPresentAgain.setName("lblPresentAgain");
        lblReasonforBouncing.setName("lblReasonforBouncing");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblStatus.setName("lblStatus");
        mbrMain.setName("mbrMain");
        panBouncingType.setName("panBouncingType");
        panClearingType.setName("panClearingType");
        panMain.setName("panMain");
        panStatus.setName("panStatus");
        dateClearingDate.setName("dateClearingDate");
        txtClearingSerialNo.setName("txtClearingSerialNo");
        txtInwardScheduleNo.setName("txtInwardScheduleNo");
    }
    
    /** Auto Generated Method - internationalize()
     * This method used to assign display texts from
     * the Resource Bundle File. */
    private void internationalize() {
//        BouncingInstrumentwiseRB resourceBundle = new BouncingInstrumentwiseRB();
        lblInstrumentDt.setText(resourceBundle.getString("lblInstrumentDt"));
        lblPresentAgain.setText(resourceBundle.getString("lblPresentAgain"));
        lblInwardScheduleNo.setText(resourceBundle.getString("lblInwardScheduleNo"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblAccountNumber.setText(resourceBundle.getString("lblAccountNumber"));
        lblAccountHead.setText(resourceBundle.getString("lblAccountHead"));
        lblInstrumentType.setText(resourceBundle.getString("lblInstrumentType"));
        lblInstrumentDate.setText(resourceBundle.getString("lblInstrumentDate"));
        lblBranchCode.setText(resourceBundle.getString("lblBranchCode"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblClearingSerialNo.setText(resourceBundle.getString("lblClearingSerialNo"));
        lblBranchCd.setText(resourceBundle.getString("lblBranchCd"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblClearingType.setText(resourceBundle.getString("lblClearingType"));
        lblBankCd.setText(resourceBundle.getString("lblBankCd"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblInstrumentNum.setText(resourceBundle.getString("lblInstrumentNum"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblNm.setText(resourceBundle.getString("lblNm"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblInstrumentNumber.setText(resourceBundle.getString("lblInstrumentNumber"));
        lblClearingDate.setText(resourceBundle.getString("lblClearingDate"));
        lblInstrumentTy.setText(resourceBundle.getString("lblInstrumentTy"));
        lblClearingDt.setText(resourceBundle.getString("lblClearingDt"));
        lblAmount.setText(resourceBundle.getString("lblAmount"));
        lblBouncingType.setText(resourceBundle.getString("lblBouncingType"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblName.setText(resourceBundle.getString("lblName"));
        lblClearType.setText(resourceBundle.getString("lblClearType"));
        lblClearDt.setText(resourceBundle.getString("lblClearDt"));
        chkPresentAgain.setText(resourceBundle.getString("chkPresentAgain"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblAccountNum.setText(resourceBundle.getString("lblAccountNum"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblBankCode.setText(resourceBundle.getString("lblBankCode"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        btnClearingSerialNo.setText(resourceBundle.getString("btnClearingSerialNo"));
        lblAccountHd.setText(resourceBundle.getString("lblAccountHd"));
        lblAmt.setText(resourceBundle.getString("lblAmt"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblClearingTy.setText(resourceBundle.getString("lblClearingTy"));
        lblReasonforBouncing.setText(resourceBundle.getString("lblReasonforBouncing"));
        
        btnException.setText(resourceBundle.getString("btnException"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnReject.setText(resourceBundle.getString("btnReject"));
    }
    
    /** Auto Generated Method - setMandatoryHashMap()
     * This method list out all the Input Fields available in the UI.
     * It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboBouncingType", new Boolean(true));
        mandatoryMap.put("txtClearingSerialNo", new Boolean(true));
        mandatoryMap.put("txtInwardScheduleNo", new Boolean(true));
        //        mandatoryMap.put("cboReasonforBouncing", new Boolean(true));
        mandatoryMap.put("txtReasonforBouncing", new Boolean(true));
        mandatoryMap.put("chkPresentAgain", new Boolean(false));
        mandatoryMap.put("cboClearingType", new Boolean(true));
        mandatoryMap.put("dateClearingDate", new Boolean(true));
    }
    
    /** Auto Generated Method - getMandatoryHashMap()
     * Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    /** Auto Generated Method - update()
     * This method called by Observable. It updates the UI with
     * Observable's data. If needed add/Remove RadioButtons
     * method need to be added.*/
    public void update(Observable observed, Object arg) {
        cboBouncingType.setSelectedItem(observable.getCboBouncingType());
        txtClearingSerialNo.setText(observable.getTxtClearingSerialNo());
        txtInwardScheduleNo.setText(observable.getTxtInwardScheduleNo());
        //        cboReasonforBouncing.setSelectedItem(observable.getCboReasonforBouncing());
        
        txtReasonforBouncing.setText(observable.getTxtReasonforBouncing());
        
        chkPresentAgain.setSelected(observable.getChkPresentAgain());
        cboClearingType.setSelectedItem(observable.getCboClearingType());
        dateClearingDate.setDateValue(observable.getdateClearingDate());
        
        lblAccountHead.setText(observable.getLblAccountHead());
        lblAccountNumber.setText(observable.getLblAccountNumber());
        lblAmount.setText(observable.getLblAmount());
        lblBankCode.setText(observable.getLblBankCode());
        lblBranchCode.setText(observable.getLblBranchCode());
        lblClearingDate.setText(observable.getLblClearingDate());
        lblClearingType.setText(observable.getLblClearingType());
        lblInstrumentDate.setText(observable.getLblInstrumentDate());
        lblInstrumentNumber.setText(observable.getLblInstrumentNumber());
        lblInstrumentType.setText(observable.getLblInstrumentType());
        lblName.setText(observable.getLblName());
    }
    
    /** Auto Generated Method - updateOBFields()
     * This method called by Save option of UI.
     * It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setCboBouncingType((String) cboBouncingType.getSelectedItem());
        observable.setTxtClearingSerialNo(txtClearingSerialNo.getText());
        observable.setTxtInwardScheduleNo(txtInwardScheduleNo.getText());
        observable.setTxtReasonforBouncing(txtReasonforBouncing.getText());
        observable.setChkPresentAgain(chkPresentAgain.isSelected());
        observable.setCboClearingType((String) cboClearingType.getSelectedItem());
        observable.setdateClearingDate(dateClearingDate.getDateValue());
    }
    
    /** Auto Generated Method - setHelpMessage()
     * This method shows tooltip help for all the input fields
     * available in the UI. It needs the Mandatory Resource Bundle
     * object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        BouncingInstrumentwiseMRB objMandatoryRB = new BouncingInstrumentwiseMRB();
        cboBouncingType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboBouncingType"));
        txtClearingSerialNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtClearingSerialNo"));
        txtInwardScheduleNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInwardScheduleNo"));
        //        cboReasonforBouncing.setHelpMessage(lblMsg, objMandatoryRB.getString("cboReasonforBouncing"));
        txtReasonforBouncing.setHelpMessage(lblMsg, objMandatoryRB.getString("txtReasonforBouncing"));
        chkPresentAgain.setHelpMessage(lblMsg, objMandatoryRB.getString("chkPresentAgain"));
        cboClearingType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboClearingType"));
        dateClearingDate.setHelpMessage(lblMsg, objMandatoryRB.getString("dateClearingDate"));
    }
    
    private void setMaximumLength(){
        txtClearingSerialNo.setMaxLength(16);
        txtInwardScheduleNo.setMaxLength(16);
    }
    
    private void initComponentData() {
        cboBouncingType.setModel(observable.getCbmBouncingType());
        //        cboReasonforBouncing.setModel(observable.getCbmReasonforBouncing());
        cboClearingType.setModel(observable.getCbmClearingType());
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tbrMain = new javax.swing.JToolBar();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace24 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace25 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace26 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace27 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace28 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panMain = new com.see.truetransact.uicomponent.CPanel();
        panClearingType = new com.see.truetransact.uicomponent.CPanel();
        lblInstrumentNum = new com.see.truetransact.uicomponent.CLabel();
        lblInstrumentTy = new com.see.truetransact.uicomponent.CLabel();
        lblClearingDt = new com.see.truetransact.uicomponent.CLabel();
        lblClearingTy = new com.see.truetransact.uicomponent.CLabel();
        lblInstrumentDt = new com.see.truetransact.uicomponent.CLabel();
        lblAccountHd = new com.see.truetransact.uicomponent.CLabel();
        lblAccountNum = new com.see.truetransact.uicomponent.CLabel();
        lblAmt = new com.see.truetransact.uicomponent.CLabel();
        lblNm = new com.see.truetransact.uicomponent.CLabel();
        lblBankCd = new com.see.truetransact.uicomponent.CLabel();
        lblBranchCd = new com.see.truetransact.uicomponent.CLabel();
        lblClearingType = new com.see.truetransact.uicomponent.CLabel();
        lblClearingDate = new com.see.truetransact.uicomponent.CLabel();
        lblInstrumentType = new com.see.truetransact.uicomponent.CLabel();
        lblInstrumentNumber = new com.see.truetransact.uicomponent.CLabel();
        lblInstrumentDate = new com.see.truetransact.uicomponent.CLabel();
        lblAccountHead = new com.see.truetransact.uicomponent.CLabel();
        lblAccountNumber = new com.see.truetransact.uicomponent.CLabel();
        lblAmount = new com.see.truetransact.uicomponent.CLabel();
        lblName = new com.see.truetransact.uicomponent.CLabel();
        lblBankCode = new com.see.truetransact.uicomponent.CLabel();
        lblBranchCode = new com.see.truetransact.uicomponent.CLabel();
        panBouncingType = new com.see.truetransact.uicomponent.CPanel();
        lblBouncingType = new com.see.truetransact.uicomponent.CLabel();
        lblClearingSerialNo = new com.see.truetransact.uicomponent.CLabel();
        lblInwardScheduleNo = new com.see.truetransact.uicomponent.CLabel();
        lblReasonforBouncing = new com.see.truetransact.uicomponent.CLabel();
        lblPresentAgain = new com.see.truetransact.uicomponent.CLabel();
        lblClearType = new com.see.truetransact.uicomponent.CLabel();
        lblClearDt = new com.see.truetransact.uicomponent.CLabel();
        cboBouncingType = new com.see.truetransact.uicomponent.CComboBox();
        txtClearingSerialNo = new com.see.truetransact.uicomponent.CTextField();
        chkPresentAgain = new com.see.truetransact.uicomponent.CCheckBox();
        cboClearingType = new com.see.truetransact.uicomponent.CComboBox();
        txtInwardScheduleNo = new com.see.truetransact.uicomponent.CTextField();
        btnClearingSerialNo = new com.see.truetransact.uicomponent.CButton();
        dateClearingDate = new com.see.truetransact.uicomponent.CDateField();
        txtReasonforBouncing = new com.see.truetransact.uicomponent.CTextField();
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
        setMaximumSize(new java.awt.Dimension(550, 300));
        setMinimumSize(new java.awt.Dimension(550, 400));
        setPreferredSize(new java.awt.Dimension(550, 400));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        tbrMain.setEnabled(false);
        tbrMain.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 11)); // NOI18N
        tbrMain.setMinimumSize(new java.awt.Dimension(28, 28));
        tbrMain.setPreferredSize(new java.awt.Dimension(28, 28));

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrMain.add(btnNew);

        lblSpace24.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace24.setText("     ");
        lblSpace24.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMain.add(lblSpace24);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrMain.add(btnEdit);

        lblSpace25.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace25.setText("     ");
        lblSpace25.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMain.add(lblSpace25);

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

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMain.add(lblSpace26);

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

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMain.add(lblSpace27);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrMain.add(btnException);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMain.add(lblSpace28);

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

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMain.add(lblSpace29);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrMain.add(btnClose);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(tbrMain, gridBagConstraints);

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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(panStatus, gridBagConstraints);

        panMain.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panMain.setMinimumSize(new java.awt.Dimension(525, 300));
        panMain.setPreferredSize(new java.awt.Dimension(525, 300));
        panMain.setLayout(new java.awt.GridBagLayout());

        panClearingType.setMinimumSize(new java.awt.Dimension(250, 242));
        panClearingType.setPreferredSize(new java.awt.Dimension(250, 242));
        panClearingType.setLayout(new java.awt.GridBagLayout());

        lblInstrumentNum.setText("Instrument Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panClearingType.add(lblInstrumentNum, gridBagConstraints);

        lblInstrumentTy.setText("Instrument Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panClearingType.add(lblInstrumentTy, gridBagConstraints);

        lblClearingDt.setText("Clearing Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panClearingType.add(lblClearingDt, gridBagConstraints);

        lblClearingTy.setText("Clearing Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panClearingType.add(lblClearingTy, gridBagConstraints);

        lblInstrumentDt.setText("Instrument Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panClearingType.add(lblInstrumentDt, gridBagConstraints);

        lblAccountHd.setText("Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panClearingType.add(lblAccountHd, gridBagConstraints);

        lblAccountNum.setText("Account Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panClearingType.add(lblAccountNum, gridBagConstraints);

        lblAmt.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panClearingType.add(lblAmt, gridBagConstraints);

        lblNm.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panClearingType.add(lblNm, gridBagConstraints);

        lblBankCd.setText("Bank Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panClearingType.add(lblBankCd, gridBagConstraints);

        lblBranchCd.setText("Branch Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panClearingType.add(lblBranchCd, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panClearingType.add(lblClearingType, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panClearingType.add(lblClearingDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panClearingType.add(lblInstrumentType, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panClearingType.add(lblInstrumentNumber, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panClearingType.add(lblInstrumentDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panClearingType.add(lblAccountHead, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panClearingType.add(lblAccountNumber, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panClearingType.add(lblAmount, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panClearingType.add(lblName, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panClearingType.add(lblBankCode, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panClearingType.add(lblBranchCode, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panMain.add(panClearingType, gridBagConstraints);

        panBouncingType.setMinimumSize(new java.awt.Dimension(278, 282));
        panBouncingType.setPreferredSize(new java.awt.Dimension(278, 283));
        panBouncingType.setLayout(new java.awt.GridBagLayout());

        lblBouncingType.setText("Bouncing Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(7, 4, 7, 4);
        panBouncingType.add(lblBouncingType, gridBagConstraints);

        lblClearingSerialNo.setText("Inward Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(7, 4, 7, 4);
        panBouncingType.add(lblClearingSerialNo, gridBagConstraints);

        lblInwardScheduleNo.setText("Inward Schedule No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(7, 4, 7, 4);
        panBouncingType.add(lblInwardScheduleNo, gridBagConstraints);

        lblReasonforBouncing.setText("Reason for Bouncing");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(7, 4, 7, 4);
        panBouncingType.add(lblReasonforBouncing, gridBagConstraints);

        lblPresentAgain.setText("Present Again");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(7, 4, 7, 4);
        panBouncingType.add(lblPresentAgain, gridBagConstraints);

        lblClearType.setText("Clearing Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(7, 4, 7, 4);
        panBouncingType.add(lblClearType, gridBagConstraints);

        lblClearDt.setText("Clearing Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(7, 4, 7, 4);
        panBouncingType.add(lblClearDt, gridBagConstraints);

        cboBouncingType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 4, 7, 0);
        panBouncingType.add(cboBouncingType, gridBagConstraints);

        txtClearingSerialNo.setEditable(false);
        txtClearingSerialNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 4, 7, 0);
        panBouncingType.add(txtClearingSerialNo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 4, 7, 0);
        panBouncingType.add(chkPresentAgain, gridBagConstraints);

        cboClearingType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 4, 7, 0);
        panBouncingType.add(cboClearingType, gridBagConstraints);

        txtInwardScheduleNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 4, 7, 0);
        panBouncingType.add(txtInwardScheduleNo, gridBagConstraints);

        btnClearingSerialNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnClearingSerialNo.setMinimumSize(new java.awt.Dimension(28, 21));
        btnClearingSerialNo.setPreferredSize(new java.awt.Dimension(28, 21));
        btnClearingSerialNo.setEnabled(false);
        btnClearingSerialNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearingSerialNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 4, 15);
        panBouncingType.add(btnClearingSerialNo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 4, 7, 0);
        panBouncingType.add(dateClearingDate, gridBagConstraints);

        txtReasonforBouncing.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBouncingType.add(txtReasonforBouncing, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panMain.add(panBouncingType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.weighty = 4.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        getContentPane().add(panMain, gridBagConstraints);

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
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    public void authorizeStatus(String authorizeStatus) {
        if (ACTION == AUTHORIZE && isFilled) {
            HashMap singleAuthorizeMap = new HashMap();
            ArrayList arrList = new ArrayList();
            HashMap authDataMap = new HashMap();
            
            authDataMap.put("BOUNCING ID", BOUNCING_ID);
            authDataMap.put("INWARD ID", txtClearingSerialNo.getText());
            authDataMap.put("USER_ID", TrueTransactMain.USER_ID);
            authDataMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            authDataMap.put("AUTHORIZEDT", currDt.clone());
            arrList.add(authDataMap);
            
            singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(singleAuthorizeMap);
            isFilled = false;
        } else {
            HashMap mapParam = new HashMap();
            
            //__ To Save the data in the Internal Frame...
            setModified(true);
            
            mapParam.put(CommonConstants.MAP_NAME, "getSelectBouncingInstrumentAuthList");
            ACTION = AUTHORIZE;
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
        }
    }
    
    
    public void authorize(HashMap map) {
        System.out.println("Authorize Map : " + map);
        ///observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        observable.setAuthorizeMap(map);
        observable.doAction();
        if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
            btnCancelActionPerformed(null);
            observable.setAuthorizeMap(null);
            observable.setResultStatus();
        }
    }
    private void btnClearingSerialNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearingSerialNoActionPerformed
        // Add your handling code here:
        popUpItems(SERIALNUMBER);
    }//GEN-LAST:event_btnClearingSerialNoActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_btnPrintActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        observable.resetForm();
        observable.resetStatus();
        setButtonEnableDisable();
        setClearingSerialNoEnableDisable();
        ClientUtil.enableDisable(this, false);
        
         //__ Make the Screen Closable..
        setModified(false);
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panMain);
        /* mandatoryMessage length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        if (mandatoryMessage.length() > 0){
            displayAlert(mandatoryMessage);
        }else{
            savePerformed();
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        popUpItems(DELETE);
        ClientUtil.enableDisable(this, false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        observable.setStatus();
        popUpItems(EDIT);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.setStatus();
        ClientUtil.enableDisable(this, true);
        setButtonEnableDisable();
        setClearingSerialNoEnableDisable();
        ClientUtil.enableDisable(this.txtClearingSerialNo, false);
        
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }//GEN-LAST:event_btnNewActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    
    /**
     * @param args the command line arguments
     */
/*    public static void main(String args[]) {
        new BouncingInstrumentwiseUI().show();
    }
 
 */
    
    /** This method helps in popoualting the data from the data base
     * @param Action argument is passed according to the command issued
     */
    private void popUpItems(int Action) {
        final HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        if (Action == EDIT || Action == DELETE){
            lblStatus.setText(ClientConstants.ACTION_STATUS[0]);
        }
        ACTION=Action;
        if ( Action == EDIT || Action == DELETE){
            viewMap.put(CommonConstants.MAP_NAME, "ViewAllBouncingInstrumentwiseTO");
        }else{
            viewMap.put(CommonConstants.MAP_NAME, "Bouncing_Instrumentwise.ViewSerialNumber");
        }
        updateOBFields();
        new ViewAll(this, viewMap).show();
    }
    
    /** This method helps in filling the data frm the data base to respective txt fields
     * @param param selected data from the viewAll() is passed as a param
     */
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        ClientUtil.enableDisable(this, true);
        if(ACTION == EDIT || ACTION == DELETE || ACTION == AUTHORIZE){
            isFilled = true;
            actionEditDelete(hash);
            setButtonEnableDisable();
            if(ACTION != EDIT){
                ClientUtil.enableDisable(this, false);
            }
            
        }else{
            observable.setTxtClearingSerialNo(CommonUtil.convertObjToStr(hash.get("INWARD ID")));
        }
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            setClearingSerialNoEnableDisable();
        }
        observable.setLabels();
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        
        
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }
    
    private void actionEditDelete(HashMap hash){
        HashMap whereMap = new HashMap();
        observable.setStatus();
        BOUNCING_ID = CommonUtil.convertObjToStr(hash.get("BOUNCING ID"));
        whereMap.put(CommonConstants.MAP_WHERE, BOUNCING_ID);
        observable.setbouncingId(BOUNCING_ID);
        observable.populateData(whereMap);
    }
    
    /** This method displays the alert message if any of the text fields are empty */
    private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    private void savePerformed(){
        updateOBFields();
        observable.doAction();
        ClientUtil.enableDisable(this, false);
        observable.setResultStatus();
        observable.resetForm();
        setButtonEnableDisable();
        setClearingSerialNoEnableDisable();
        ClientUtil.enableDisable(this, false);
        
         //__ Make the Screen Closable..
        setModified(false);
    }
    
    private void setClearingSerialNoEnableDisable(){
        btnClearingSerialNo.setEnabled(!btnNew.isEnabled());
        txtClearingSerialNo.setEnabled(btnNew.isEnabled());
        txtClearingSerialNo.setEditable(btnNew.isEnabled());
    }
    
    /** This method performs enable and the disable of the necessary buttons */
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(btnNew.isEnabled());
        btnDelete.setEnabled(btnNew.isEnabled());
        mitNew.setEnabled(!mitNew.isEnabled());
        mitEdit.setEnabled(mitNew.isEnabled());
        mitDelete.setEnabled(mitNew.isEnabled());
        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(!mitNew.isEnabled());
        mitCancel.setEnabled(!mitNew.isEnabled());
        lblStatus.setText(observable.getLblStatus());
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClearingSerialNo;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CComboBox cboBouncingType;
    private com.see.truetransact.uicomponent.CComboBox cboClearingType;
    private com.see.truetransact.uicomponent.CCheckBox chkPresentAgain;
    private com.see.truetransact.uicomponent.CDateField dateClearingDate;
    private com.see.truetransact.uicomponent.CLabel lblAccountHd;
    private com.see.truetransact.uicomponent.CLabel lblAccountHead;
    private com.see.truetransact.uicomponent.CLabel lblAccountNum;
    private com.see.truetransact.uicomponent.CLabel lblAccountNumber;
    private com.see.truetransact.uicomponent.CLabel lblAmount;
    private com.see.truetransact.uicomponent.CLabel lblAmt;
    private com.see.truetransact.uicomponent.CLabel lblBankCd;
    private com.see.truetransact.uicomponent.CLabel lblBankCode;
    private com.see.truetransact.uicomponent.CLabel lblBouncingType;
    private com.see.truetransact.uicomponent.CLabel lblBranchCd;
    private com.see.truetransact.uicomponent.CLabel lblBranchCode;
    private com.see.truetransact.uicomponent.CLabel lblClearDt;
    private com.see.truetransact.uicomponent.CLabel lblClearType;
    private com.see.truetransact.uicomponent.CLabel lblClearingDate;
    private com.see.truetransact.uicomponent.CLabel lblClearingDt;
    private com.see.truetransact.uicomponent.CLabel lblClearingSerialNo;
    private com.see.truetransact.uicomponent.CLabel lblClearingTy;
    private com.see.truetransact.uicomponent.CLabel lblClearingType;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentDate;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentDt;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentNum;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentNumber;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentTy;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentType;
    private com.see.truetransact.uicomponent.CLabel lblInwardScheduleNo;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblName;
    private com.see.truetransact.uicomponent.CLabel lblNm;
    private com.see.truetransact.uicomponent.CLabel lblPresentAgain;
    private com.see.truetransact.uicomponent.CLabel lblReasonforBouncing;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace24;
    private com.see.truetransact.uicomponent.CLabel lblSpace25;
    private com.see.truetransact.uicomponent.CLabel lblSpace26;
    private com.see.truetransact.uicomponent.CLabel lblSpace27;
    private com.see.truetransact.uicomponent.CLabel lblSpace28;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panBouncingType;
    private com.see.truetransact.uicomponent.CPanel panClearingType;
    private com.see.truetransact.uicomponent.CPanel panMain;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private javax.swing.JToolBar tbrMain;
    private com.see.truetransact.uicomponent.CTextField txtClearingSerialNo;
    private com.see.truetransact.uicomponent.CTextField txtInwardScheduleNo;
    private com.see.truetransact.uicomponent.CTextField txtReasonforBouncing;
    // End of variables declaration//GEN-END:variables
}