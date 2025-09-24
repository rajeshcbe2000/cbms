/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * stateTalukUI.java
 *
 * Created on 19-05-2009 12:26 PM
 */

package com.see.truetransact.ui.sysadmin.stateTalukMaster;

/**
 *
 * @author  : Swaroop
 *   19-05-2009
 */
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Observer;



import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.common.viewall.TableDialogUI;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uicomponent.CButtonGroup;// To add and Remove the Radio Buttons...
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.PincodeValidation_IN;
import com.see.truetransact.uivalidation.DefaultValidation;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.transferobject.sysadmin.stateTalukMaster.StateTalukTO;
import java.util.List;

public class StateTalukUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField  {
    // Variable Declarations
    private StateTalukOB observable;
    final StateTalukRB  resourceBundle = new StateTalukRB();
    private HashMap mandatoryMap;
    private int viewType = -1;
    private final int NEW=0, EDIT=1,DELETE=2,AUTHORIZE=3, VIEW =4;
    boolean isFilled = false;
    boolean stateCodeExist = false;
    int updateTab = -1;
    private boolean updateMode = false;
    private StateTalukTO objStateTalukTO;
    /** Creates new form OtherBankUI */
    public StateTalukUI() {
        initComponents();
        initStartUp();
    }
    private void initStartUp() {
        setFieldNames();
        internationalize();
        setObservable();
        initComponentData();
        setMandatoryHashMap();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panState);
        setMaximumLength();
        setHelpMessage();
        setButtonEnableDisable();
        ClientUtil.enableDisable(this, false);
        enableDisableTaluk_NewSaveDelete(false);
        observable.resetForm();
        observable.resetStatus();
        btnDisDelete.setEnabled(false);
    }
    /* To set Maximum length and for validation */
    private void setMaximumLength() {
        txtStateName.setMaxLength(128);
        txtTalukName.setMaxLength(128);
        txtDistrictName.setMaxLength(128);
        txtStateCode.setMaxLength(16);
        txtDistrictCode.setMaxLength(16);
        txtTalukCode.setMaxLength(16);
        txtStateCode.setValidation(new NumericValidation(ClientConstants.INSTRUMENT_NO2, 0));
        txtTalukCode.setValidation(new NumericValidation(ClientConstants.INSTRUMENT_NO2, 0));
        txtDistrictCode.setValidation(new NumericValidation(ClientConstants.INSTRUMENT_NO2, 0));
        txtStateCode.setAllowNumber(true);
        txtTalukCode.setAllowNumber(true);
        txtDistrictCode.setAllowNumber(true);
        txtStateCode.setAllowAll(false);
        txtTalukCode.setAllowAll(false);
        txtDistrictCode.setAllowAll(false);
    }
    
    private void setObservable() {
//        /* Implementing Singleton pattern */
        observable = StateTalukOB.getInstance();
        observable.addObserver(this);
    }
    /**
     * EnableDisable New Save Delete Buttons of OtherBankBranch Table
     * When New is pressed
     */
    private void enableDisableTaluk_NewSaveDelete(boolean flag) {
        btnTalNew.setEnabled(flag);
        btnTalSave.setEnabled(flag);
        btnTalDelete.setEnabled(flag);
    }
    /**
     * Enable Disable New Save Delete Buttons of OtherBankBranch Table
     * When Save or Delete is invoked
     */
    private void enableDisableTaluk_SaveDelete() {
        btnTalNew.setEnabled(true);
        btnTalSave.setEnabled(false);
        btnTalDelete.setEnabled(false);
    }
    private void enableDisableStateCode(boolean flag) {
        txtStateCode.setEditable(flag);
    }
    /** To display a popUp window for viewing existing data */
    private void popUp() {
        final HashMap viewMap = new HashMap();
        if ( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||  observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW){
            ArrayList lst = new ArrayList();
            lst.add("STATE_CODE");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            viewMap.put(CommonConstants.MAP_NAME, "viewStateCode");
        }
        new ViewAll(this, viewMap).show();
    }
    // Called Automatically when viewAll() is Called...
    public void fillData(Object param) {
        isFilled = true;
        final HashMap hash = (HashMap) param;
        hash.put("WHERE",CommonUtil.convertObjToStr(hash.get("STATE_CODE")));
        System.out.println("@@@hash"+hash);
        observable.populateData(hash);
        observable.setTxtStateCode(CommonUtil.convertObjToStr(hash.get("STATE_CODE")));
        observable.setTxtStateName(CommonUtil.convertObjToStr(hash.get("STATE_NAME")));
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ) {
            enableDisablePanState(true);
            enableDisableStateCode(false);
            setTableOtherBankBranchEnableDisable(true);
            if (tblDistrict.getRowCount() == 0) {
                // At the edit mode if all the rows are deleted
                // enable New Button in Other Bank Branch
                enableDisableTaluk_SaveDelete();
            }
            enableDisablePanDistrict(true);
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW ) {
            enableDisablePanState(false);
            enableDisablePanDistrict(false);
            enableDisablePanTaluk(false);
            setTableOtherBankBranchEnableDisable(false);
        } else {
            enableDisableStateCode(true);
        }
        if(viewType == AUTHORIZE ){
             btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
             btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
             btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
        }
        observable.setStatus();
        setButtonEnableDisable();
    }
    /**
     * To enable disable Bank Code and Branch Code
     * At the time of edit mode these fields are disable
     * Since these are the primary keys for the tables OTHER_BANK and OTHER_BANK_BRANCH
     */
   
    /**
     * Enable Disable OtherBankBranch Fields
     */
    public void enableDisablePanTaluk(boolean flag){
       ClientUtil.enableDisable(panTaluk,flag);
        
    }
     public void enableDisablePanDistrict(boolean flag){
       ClientUtil.enableDisable(panDistrict,flag);
        
    }
    /**
     * Enable Disable OtherBank Fields
     */
    public void enableDisablePanState(boolean flag){
       ClientUtil.enableDisable(panState,flag);
        
    }
    
    /**
     * update the OtherBankBranch details
     */
    private void updateTaluk() {
       txtTalukName.setText(observable.getTxtTalukName());
       txtTalukCode.setText(observable.getTxtTalukCode());
    }
    
    /*To set model for combo boxes*/
    private void initComponentData() {
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
        btnTalDelete.setName("btnDisDelete");
        btnTalNew.setName("btnDisNew");
        btnTalSave.setName("btnDisSave");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        lblStateCode.setName("lblStateCode");
        lblTalukCode.setName("lblTalukCode");
        lblDistrictCode.setName("lblDistrictCode");
        lblStateName.setName("lblStateName");
        lblTalukName.setName("lblCity");
        lblDistrictName.setName("lblDistrictName");
        panMain.setName("panMain");
        panDistrict.setName("panDistrict");
        panTaluk.setName("panTaluk");
        panState.setName("panState");
        txtStateCode.setName("txtStateCode");
        txtStateName.setName("txtStateName");
        txtTalukCode.setName("txtTalukCode");
        txtTalukName.setName("txtTalukName");
        txtDistrictName.setName("txtDistrictName");
        txtDistrictCode.setName("txtDistrictCode");   
        tblDistrict.setName("tblDistrict");
        tblStateDis.setName("tblStateDis");
        btnDisDelete.setName("btnDisDelete");
        btnClear.setName("btnClear");
        btnDisSave.setName("btnDistrictSave");
    }
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        lblStateCode.setText(resourceBundle.getString("lblStateCode"));
        lblStateName.setText(resourceBundle.getString("lblStateName"));
        lblTalukName.setText(resourceBundle.getString("lblTalukName"));
        lblTalukCode.setText(resourceBundle.getString("lblTalukCode"));
        lblDistrictName.setText(resourceBundle.getString("lblDistrictName"));
        btnTalDelete.setText(resourceBundle.getString("btnDisDelete"));
        btnTalNew.setText(resourceBundle.getString("btnDisNew"));
        btnTalSave.setText(resourceBundle.getString("btnDisSave"));
        btnException.setText(resourceBundle.getString("btnException"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnClear.setText(resourceBundle.getString("btnClear"));
        btnDisDelete.setText(resourceBundle.getString("btnDisDelete"));
        btnDisSave.setText(resourceBundle.getString("btnDistrictSave"));
    }
    
    
/* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        StateTalukMRB objMandatoryRB = new StateTalukMRB();
        txtStateName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStateName"));
        txtStateCode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStateCode"));
        txtTalukName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTalukName"));
        txtTalukCode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTalukCode"));
        txtDistrictName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDistrictName"));
        txtDistrictCode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDistrictCode"));
    }
    
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(java.util.Observable observed, Object arg) {
        txtStateCode.setText(observable.getTxtStateCode());
        txtStateName.setText(observable.getTxtStateName());
        txtTalukCode.setText(observable.getTxtTalukCode());
        txtTalukName.setText(observable.getTxtTalukName());
        txtDistrictCode.setText(observable.getTxtDistrictCode());
        txtDistrictName.setText(observable.getTxtDistrictName()); 
        tblDistrict.setModel(observable.getTblDistrict());
        tblStateDis.setModel(observable.getTblStateDis());
    }
   
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtStateCode(txtStateCode.getText());
        observable.setTxtStateName(txtStateName.getText());
        observable.setTxtDistrictCode(txtDistrictCode.getText());
        observable.setTxtDistrictName(txtDistrictName.getText());
        observable.setTxtTalukCode(txtTalukCode.getText());
        observable.setTxtTalukName(txtTalukName.getText());
    }
    
    
/* Auto Generated Method - setMandatoryHashMap()
 
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtStateName", new Boolean(true));
        mandatoryMap.put("txtStateCode", new Boolean(true));
        mandatoryMap.put("txtTalukName", new Boolean(false));
        mandatoryMap.put("txtTalukCode", new Boolean(false));
        mandatoryMap.put("txtDistrictName", new Boolean(false));
        mandatoryMap.put("txtDistrictCode", new Boolean(false));
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

        cButtonGroup1 = new com.see.truetransact.uicomponent.CButtonGroup();
        cButtonGroup2 = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrOtherBank = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace26 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace27 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace28 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace30 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace31 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panMain = new com.see.truetransact.uicomponent.CPanel();
        panTaluk = new com.see.truetransact.uicomponent.CPanel();
        lblTalukCode = new com.see.truetransact.uicomponent.CLabel();
        lblTalukName = new com.see.truetransact.uicomponent.CLabel();
        sptDescription = new com.see.truetransact.uicomponent.CSeparator();
        panButton = new com.see.truetransact.uicomponent.CPanel();
        btnTalNew = new com.see.truetransact.uicomponent.CButton();
        btnTalSave = new com.see.truetransact.uicomponent.CButton();
        btnTalDelete = new com.see.truetransact.uicomponent.CButton();
        txtTalukCode = new com.see.truetransact.uicomponent.CTextField();
        txtTalukName = new com.see.truetransact.uicomponent.CTextField();
        panState = new com.see.truetransact.uicomponent.CPanel();
        lblStateCode = new com.see.truetransact.uicomponent.CLabel();
        lblStateName = new com.see.truetransact.uicomponent.CLabel();
        sptDescription1 = new com.see.truetransact.uicomponent.CSeparator();
        panButton1 = new com.see.truetransact.uicomponent.CPanel();
        txtStateCode = new com.see.truetransact.uicomponent.CTextField();
        txtStateName = new com.see.truetransact.uicomponent.CTextField();
        panDistrict = new com.see.truetransact.uicomponent.CPanel();
        lblDistrictCode = new com.see.truetransact.uicomponent.CLabel();
        lblDistrictName = new com.see.truetransact.uicomponent.CLabel();
        sptDescription2 = new com.see.truetransact.uicomponent.CSeparator();
        panButton2 = new com.see.truetransact.uicomponent.CPanel();
        txtDistrictCode = new com.see.truetransact.uicomponent.CTextField();
        txtDistrictName = new com.see.truetransact.uicomponent.CTextField();
        panButton3 = new com.see.truetransact.uicomponent.CPanel();
        btnClear = new com.see.truetransact.uicomponent.CButton();
        btnDisSave = new com.see.truetransact.uicomponent.CButton();
        btnDisDelete = new com.see.truetransact.uicomponent.CButton();
        panDistrictInfo = new com.see.truetransact.uicomponent.CPanel();
        srpDistrictDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblDistrict = new com.see.truetransact.uicomponent.CTable();
        panStateDisInfo = new com.see.truetransact.uicomponent.CPanel();
        srpStateDisDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblStateDis = new com.see.truetransact.uicomponent.CTable();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrOtherBank = new com.see.truetransact.uicomponent.CMenuBar();
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
        setMaximumSize(new java.awt.Dimension(800, 625));
        setMinimumSize(new java.awt.Dimension(800, 675));
        setPreferredSize(new java.awt.Dimension(800, 625));

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
        tbrOtherBank.add(btnView);

        lblSpace4.setText("     ");
        tbrOtherBank.add(lblSpace4);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrOtherBank.add(btnNew);

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOtherBank.add(lblSpace26);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrOtherBank.add(btnEdit);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOtherBank.add(lblSpace27);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrOtherBank.add(btnDelete);

        lblSpace2.setText("     ");
        tbrOtherBank.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrOtherBank.add(btnSave);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOtherBank.add(lblSpace28);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrOtherBank.add(btnCancel);

        lblSpace3.setText("     ");
        tbrOtherBank.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrOtherBank.add(btnAuthorize);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOtherBank.add(lblSpace29);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrOtherBank.add(btnException);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOtherBank.add(lblSpace30);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.setMaximumSize(new java.awt.Dimension(29, 27));
        btnReject.setMinimumSize(new java.awt.Dimension(29, 27));
        btnReject.setPreferredSize(new java.awt.Dimension(29, 27));
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrOtherBank.add(btnReject);

        lblSpace5.setText("     ");
        tbrOtherBank.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrOtherBank.add(btnPrint);

        lblSpace31.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace31.setText("     ");
        lblSpace31.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOtherBank.add(lblSpace31);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrOtherBank.add(btnClose);

        getContentPane().add(tbrOtherBank, java.awt.BorderLayout.NORTH);

        panMain.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panMain.setMinimumSize(new java.awt.Dimension(600, 625));
        panMain.setPreferredSize(new java.awt.Dimension(600, 625));
        panMain.setLayout(new java.awt.GridBagLayout());

        panTaluk.setLayout(new java.awt.GridBagLayout());

        lblTalukCode.setText("Taluk Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTaluk.add(lblTalukCode, gridBagConstraints);

        lblTalukName.setText("Taluk Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTaluk.add(lblTalukName, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTaluk.add(sptDescription, gridBagConstraints);

        panButton.setLayout(new java.awt.GridBagLayout());

        btnTalNew.setText("New");
        btnTalNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTalNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton.add(btnTalNew, gridBagConstraints);

        btnTalSave.setText("Save");
        btnTalSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTalSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton.add(btnTalSave, gridBagConstraints);

        btnTalDelete.setText("Delete");
        btnTalDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTalDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton.add(btnTalDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTaluk.add(panButton, gridBagConstraints);

        txtTalukCode.setMaxLength(4);
        txtTalukCode.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTaluk.add(txtTalukCode, gridBagConstraints);

        txtTalukName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTaluk.add(txtTalukName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panMain.add(panTaluk, gridBagConstraints);

        panState.setMinimumSize(new java.awt.Dimension(247, 200));
        panState.setPreferredSize(new java.awt.Dimension(247, 200));
        panState.setLayout(new java.awt.GridBagLayout());

        lblStateCode.setText("State Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panState.add(lblStateCode, gridBagConstraints);

        lblStateName.setText("State Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panState.add(lblStateName, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panState.add(sptDescription1, gridBagConstraints);

        panButton1.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panState.add(panButton1, gridBagConstraints);

        txtStateCode.setMinimumSize(new java.awt.Dimension(100, 21));
        txtStateCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtStateCodeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panState.add(txtStateCode, gridBagConstraints);

        txtStateName.setMinimumSize(new java.awt.Dimension(100, 21));
        txtStateName.setNextFocusableComponent(txtDistrictCode);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panState.add(txtStateName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panMain.add(panState, gridBagConstraints);

        panDistrict.setMinimumSize(new java.awt.Dimension(247, 200));
        panDistrict.setPreferredSize(new java.awt.Dimension(247, 200));
        panDistrict.setLayout(new java.awt.GridBagLayout());

        lblDistrictCode.setText("District Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDistrict.add(lblDistrictCode, gridBagConstraints);

        lblDistrictName.setText("District Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDistrict.add(lblDistrictName, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDistrict.add(sptDescription2, gridBagConstraints);

        panButton2.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDistrict.add(panButton2, gridBagConstraints);

        txtDistrictCode.setMaxLength(4);
        txtDistrictCode.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDistrictCode.setNextFocusableComponent(txtDistrictName);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDistrict.add(txtDistrictCode, gridBagConstraints);

        txtDistrictName.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDistrictName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDistrictNameFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDistrict.add(txtDistrictName, gridBagConstraints);

        panButton3.setLayout(new java.awt.GridBagLayout());

        btnClear.setText("New");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton3.add(btnClear, gridBagConstraints);

        btnDisSave.setText("Save");
        btnDisSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDisSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton3.add(btnDisSave, gridBagConstraints);

        btnDisDelete.setText("Delete");
        btnDisDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDisDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton3.add(btnDisDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDistrict.add(panButton3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        panMain.add(panDistrict, gridBagConstraints);

        panDistrictInfo.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panDistrictInfo.setMinimumSize(new java.awt.Dimension(250, 150));
        panDistrictInfo.setName("panTransInfo");
        panDistrictInfo.setPreferredSize(new java.awt.Dimension(250, 150));
        panDistrictInfo.setLayout(new java.awt.GridBagLayout());

        tblDistrict.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Sl.No", "District Code", "District Name", "Taluk Code", "Taluk Name", "Status", "AuthStatus", "Verified"
            }
        ));
        tblDistrict.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDistrictMousePressed(evt);
            }
        });
        srpDistrictDetails.setViewportView(tblDistrict);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panDistrictInfo.add(srpDistrictDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panMain.add(panDistrictInfo, gridBagConstraints);

        panStateDisInfo.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panStateDisInfo.setMinimumSize(new java.awt.Dimension(250, 150));
        panStateDisInfo.setName("panTransInfo");
        panStateDisInfo.setPreferredSize(new java.awt.Dimension(250, 150));
        panStateDisInfo.setLayout(new java.awt.GridBagLayout());

        tblStateDis.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Sl.No", "State Code", "District Code", "District Name", "Status", "AuthStatus", "Verified"
            }
        ));
        tblStateDis.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblStateDisMousePressed(evt);
            }
        });
        srpStateDisDetails.setViewportView(tblStateDis);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStateDisInfo.add(srpStateDisDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panMain.add(panStateDisInfo, gridBagConstraints);

        getContentPane().add(panMain, java.awt.BorderLayout.CENTER);

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

        mnuProcess.setText("Process");

        mitNew.setText("New");
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptDelete);

        mitSave.setText("Save");
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitClose.setText("Close");
        mnuProcess.add(mitClose);

        mbrOtherBank.add(mnuProcess);

        setJMenuBar(mbrOtherBank);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDisSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDisSaveActionPerformed
        // TODO add your handling code here:
        observable.setTxtDistrictCode(txtDistrictCode.getText());
        observable.setTxtDistrictName(txtDistrictName.getText());
        observable.setTxtStateName(txtStateName.getText());
        observable.setTxtStateCode(txtStateCode.getText());
        observable.addToTableState();
    }//GEN-LAST:event_btnDisSaveActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        txtDistrictCode.setText("");
        txtDistrictName.setText("");
        observable.resetDistrict();
        txtDistrictCode.setEnabled(true);
        txtDistrictName.setEnabled(true);
        btnDisSave.setEnabled(true);
        btnDisDelete.setEnabled(true);
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnDisDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDisDeleteActionPerformed
        // TODO add your handling code here:
        int seldisCode=CommonUtil.convertObjToInt(txtDistrictCode.getText());
        int a=ClientUtil.confirmationAlert("By deleting This District, all the Taluk Details Related To This District Will Be Deleted- Press Yes To Continue Else No To Exit!!!");
        int b=0;
        if(a==b){
            int stateTabRowSelected =tblStateDis.getSelectedRow();
            observable.deleteFromTable(stateTabRowSelected);
            txtDistrictCode.setText("");
            txtDistrictName.setText("");
            observable.resetDistrict();
            int dataSize=tblDistrict.getRowCount();
            if(dataSize>=0){
                for (int i=0;i<dataSize;i++){
                    int disCode=CommonUtil.convertObjToInt(tblDistrict.getValueAt(i,1));
                    if(disCode==seldisCode){
          String st=CommonUtil.convertObjToStr(tblDistrict.getValueAt(i,0))+"."+
          CommonUtil.convertObjToStr(txtStateCode.getText())+"."+CommonUtil.convertObjToStr(tblDistrict.getValueAt(i,1))+"."+
          CommonUtil.convertObjToStr(tblDistrict.getValueAt(i,3));
                        observable.deleteTableData(st,i);
                        dataSize=tblDistrict.getRowCount();
                        i--;
                    }
                }
            }
            
        }
        else{
            
        }
    }//GEN-LAST:event_btnDisDeleteActionPerformed

    private void tblStateDisMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblStateDisMousePressed
        // TODO add your handling code here: 
            observable.resetDistrictTable();
            observable.resetTaluk();
            int st_code=CommonUtil.convertObjToInt(tblStateDis.getValueAt(tblStateDis.getSelectedRow(),1));
            int dis_code=CommonUtil.convertObjToInt(tblStateDis.getValueAt(tblStateDis.getSelectedRow(),2));
            observable.getTalukDetailsBasedonrowSelected(st_code,dis_code);
            btnTalNew.setEnabled(true);
            btnDisDelete.setEnabled(true);
            observable.populateDistrictDetails(tblStateDis.getSelectedRow());
            
            if ((observable.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE) ||(observable.getActionType()==ClientConstants.ACTIONTYPE_REJECT)){
                tblStateDis.setValueAt("Yes", tblStateDis.getSelectedRow(),6);
                btnTalNew.setEnabled(false);
            }
            if(CommonUtil.convertObjToStr(tblStateDis.getValueAt(tblStateDis.getSelectedRow(),5)).equalsIgnoreCase("AUTHORIZED"))
            {
               txtDistrictCode.setEnabled(false);
               txtDistrictName.setEnabled(false);
               btnDisSave.setEnabled(false);
               btnDisDelete.setEnabled(false);
            }
            else if((observable.getActionType()==ClientConstants.ACTIONTYPE_NEW)||(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT)){
               txtDistrictCode.setEnabled(true);
               txtDistrictName.setEnabled(true);
               btnDisSave.setEnabled(true);
               btnDisDelete.setEnabled(true);
                
            }
            enableDisablePanTaluk(false);
    }//GEN-LAST:event_tblStateDisMousePressed

    private void txtStateCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtStateCodeFocusLost
        // TODO add your handling code here:
         if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            stateCodeExist = checkStateCode();
            if(stateCodeExist)
                txtStateCode.setText("");
            if (observable.enableNew(txtStateCode.getText())) {
                enableDisableTaluk_NewSaveDelete(false);
                observable.resetTaluk();
                observable.resetDistrict();
                updateOBFields();
                btnTalNew.setEnabled(true);
            } else {
                enableDisableTaluk_NewSaveDelete(false);
            }
        }
    }//GEN-LAST:event_txtStateCodeFocusLost

    private void btnTalDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTalDeleteActionPerformed
        // TODO add your handling code here:
        String st=CommonUtil.convertObjToStr(tblDistrict.getValueAt(tblDistrict.getSelectedRow(),0))+"."+
          CommonUtil.convertObjToStr(txtStateCode.getText())+"."+CommonUtil.convertObjToStr(tblDistrict.getValueAt(tblDistrict.getSelectedRow(),1))+"."+
          CommonUtil.convertObjToStr(tblDistrict.getValueAt(tblDistrict.getSelectedRow(),3));
        observable.deleteTableData(st,tblDistrict.getSelectedRow());
        observable.resetState();
        observable.resetDistrict();
        observable.resetTaluk();
        ClientUtil.clearAll(panTaluk);
        ClientUtil.enableDisable(panTaluk,false);
    }//GEN-LAST:event_btnTalDeleteActionPerformed

    private void tblDistrictMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDistrictMousePressed
        // TODO add your handling code here:
//         updateOBFields();
         btnTalNew.setEnabled(true);
         updateMode = true;
          observable.setNewIncomeDet(false);
          String st=CommonUtil.convertObjToStr(tblDistrict.getValueAt(tblDistrict.getSelectedRow(),0))+"."+
          CommonUtil.convertObjToStr(txtStateCode.getText())+"."+CommonUtil.convertObjToStr(tblDistrict.getValueAt(tblDistrict.getSelectedRow(),1))+"."+
          CommonUtil.convertObjToStr(tblDistrict.getValueAt(tblDistrict.getSelectedRow(),3));
         observable.populateDetails(st);  
       if ((observable.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE) ||(observable.getActionType()==ClientConstants.ACTIONTYPE_REJECT)){
               tblDistrict.setValueAt("YES", tblDistrict.getSelectedRow(),7);
       }
        observable.ttNotifyObservers();
        //To enable contact buttons for NEW & EDIT
        if( observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            ClientUtil.enableDisable(panTaluk,true);
            ClientUtil.enableDisable(panDistrict,true);
            btnTalDelete.setEnabled(true);
            btnTalSave.setEnabled(true);
            updateTab = tblDistrict.getSelectedRow();
        }else if( observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE  ||(observable.getActionType()==ClientConstants.ACTIONTYPE_REJECT)
                 ||(observable.getActionType()==ClientConstants.ACTIONTYPE_VIEW)){
                  btnTalNew.setEnabled(false);
                  txtTalukCode.setText(CommonUtil.convertObjToStr(tblDistrict.getValueAt(tblDistrict.getSelectedRow(),3)));
                  txtTalukName.setText(CommonUtil.convertObjToStr(tblDistrict.getValueAt(tblDistrict.getSelectedRow(),4)));
        }
         if(CommonUtil.convertObjToStr(tblDistrict.getValueAt(tblDistrict.getSelectedRow(),6)).equalsIgnoreCase("AUTHORIZED"))
            {
               txtTalukCode.setEnabled(false);
               txtTalukName.setEnabled(false);
               btnTalSave.setEnabled(false);
               btnTalDelete.setEnabled(false);
            }
            else if((observable.getActionType()==ClientConstants.ACTIONTYPE_NEW)||(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT)){
               txtTalukCode.setEnabled(true);
               txtTalukName.setEnabled(true);
               btnTalSave.setEnabled(true);
               btnTalDelete.setEnabled(true);
                
            }
    }//GEN-LAST:event_tblDistrictMousePressed

    private void btnTalSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTalSaveActionPerformed
        // TODO add your handling code here:
        try{
            
            if(txtDistrictCode.getText().length()<=0 || txtDistrictName.getText().length()<=0){
                 ClientUtil.displayAlert("Enter District Details");
                 return;
            }else{
                 int taluktblsize=tblDistrict.getRowCount();
                 if(taluktblsize>0){
                     for(int k=0; k<taluktblsize;k++){
                       String talName=CommonUtil.convertObjToStr(tblDistrict.getValueAt(k,4));
                          if(talName.equalsIgnoreCase(txtTalukName.getText())) {
                              ClientUtil.displayAlert("Taluk Name Already Exists in Table");
                              return;
                          }
                     }
                 }
                   updateOBFields();
                 observable.addToTable(updateTab,updateMode);
                    ClientUtil.clearAll(panTaluk);
                    ClientUtil.enableDisable(panTaluk,false);
                    observable.resetTaluk();
                }
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnTalSaveActionPerformed

    private void txtDistrictNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDistrictNameFocusLost
        // TODO add your handling code here:
        if(txtDistrictCode.getText().length()>0 && txtDistrictName.getText().length()>0){
                if (observable.enableNew(txtDistrictCode.getText())) {
                    enableDisableTaluk_SaveDelete();
                    enableDisablePanTaluk(false);
                    observable.resetTaluk();
                    updateTaluk();
                } else {
                    enableDisableTaluk_NewSaveDelete(true);
                }
        }
        else{
            enableDisableTaluk_NewSaveDelete(false);
            btnTalNew.setEnabled(true);
        }
    }//GEN-LAST:event_txtDistrictNameFocusLost

    private void btnTalNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTalNewActionPerformed
        // TODO add your handling code here:
        observable.resetDistrict();
        observable.setNewIncomeDet(true);
        updateTaluk();
        enableDisableTaluk_NewSaveDelete(true);
        enableDisablePanTaluk(true);
        txtTalukName.setText("");
        updateMode = false;
        updateTab = -1;
        int txtdisCode=CommonUtil.convertObjToInt(txtDistrictCode.getText());
       int tabSize=tblDistrict.getRowCount();
       if(tabSize>=0){
           for(int i=0;i<tabSize;i++){
               int tbldisCode=CommonUtil.convertObjToInt(tblDistrict.getValueAt(i,1));
               int tbltalCode=CommonUtil.convertObjToInt(tblDistrict.getValueAt(i,3));
               if(txtdisCode==tbldisCode){
                   int number=0;
                  int maxNum[]= new int[150];
                  int max= maxNum[0];
                  maxNum[i]=tbltalCode;
                  if(maxNum[i]>max)
                      max=maxNum[i];
                   number=++max;
                  txtTalukCode.setText(String.valueOf(number));
               }      
           }
            if(txtTalukCode.getText().length()<=0 && txtTalukCode.getText().equals("")){
                  txtTalukCode.setText("1");
               }
       }
       else{
            txtTalukCode.setText("1");
       }
        btnTalSave.setEnabled(true);
        btnTalDelete.setEnabled(true);
    }//GEN-LAST:event_btnTalNewActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
//        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        viewType = VIEW;
        popUp();
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed
    private boolean checkStateCode()  {
        boolean exist = false;
        if (observable.isStateCodeAlreadyExist(txtStateCode.getText())) {
            exist = true;
            displayAlert(resourceBundle.getString("StateCodeCount"));
        } else {
            exist = false;
        }
        return exist;
    }                   
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPrintActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
//        // TODO add your handling code here:
//        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
//        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
//        // TODO add your handling code here:
       observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
       authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    public void authorizeStatus(String authorizeStatus) {
        if (viewType != AUTHORIZE){
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "viewAuthorizeState");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeDistrict");
            mapParam.put(CommonConstants.UPDATE_MAP_WHERE, "authorizeTaluk");
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            isFilled = false;
            authorizeUI.show();
            btnSave.setEnabled(false);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
        } else if (viewType == AUTHORIZE && isFilled){
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("USER_ID", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("STATE_CODE", txtStateCode.getText());
            singleAuthorizeMap.put("CURR_DATE", ClientUtil.getCurrentDate());
              int tblStateSize=tblStateDis.getRowCount();
            for(int i=0;i<tblStateSize;i++){
                String ver= CommonUtil.convertObjToStr(tblStateDis.getValueAt(i,6));
                if(ver=="Yes"){
                 singleAuthorizeMap.put("DISTRICT_CODE", CommonUtil.convertObjToStr(tblStateDis.getValueAt(i,2)));
                 ClientUtil.execute("authorizeDistrict", singleAuthorizeMap);    
                }
            }
              java.util.LinkedHashMap talukMap = observable.getIncParMap();
              ArrayList addList =new ArrayList(talukMap.keySet());
//              int tblSize=talukMap.size();
            for(int i=0;i<addList.size();i++){
             objStateTalukTO = (StateTalukTO) talukMap.get(addList.get(i));
             String verification=CommonUtil.convertObjToStr(objStateTalukTO.getVerification());
                if(verification=="YES"){
                 singleAuthorizeMap.put("TALUK_CODE", CommonUtil.convertObjToStr(objStateTalukTO.getTalukCode()));  
                 singleAuthorizeMap.put("DISTRICT_CODE", CommonUtil.convertObjToStr(objStateTalukTO.getDisCode()));
                 ClientUtil.execute("authorizeTaluk", singleAuthorizeMap);    
                }
            }
            
            ClientUtil.execute("authorizeState", singleAuthorizeMap);
            viewType = -1;
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
           lblStatus.setText(observable.getLblStatus());
        }
      
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        super.removeEditLock(txtStateCode.getText());
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();
        enableDisableTaluk_NewSaveDelete(false);
        viewType = -1;
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        observable.resetForm();
        observable.setStatus();
        
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
//        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panState);
//
//        if (stateCodeExist) {
//            mandatoryMessage += resourceBundle.getString("StateCodeCount");
//        }
//        if (mandatoryMessage.length() > 0){
//            displayAlert(mandatoryMessage);
//        }
//        else {
            savePerformed();
//        }
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnSaveActionPerformed
    /* action to perform when  main save button is pressed */
    private void savePerformed(){
        updateOBFields();
        observable.doAction();
        ClientUtil.enableDisable(this, false);
        enableDisableTaluk_NewSaveDelete(false);
        setButtonEnableDisable();
        super.removeEditLock(txtStateCode.getText());
        observable.resetForm();
        observable.setResultStatus();
        lblStatus.setText(observable.getLblStatus());
        
        
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        viewType = DELETE;
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        popUp();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        viewType = EDIT;
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        popUp();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnTalNew.setEnabled(true);
    }//GEN-LAST:event_btnEditActionPerformed
    /**
     * To enable disable table Issue details
     */
    private void setTableOtherBankBranchEnableDisable(boolean flag){
       tblDistrict .setEnabled(flag);
    }
    // To set The Value of the Buttons Depending on the Value or Condition...
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
        btnView.setEnabled(!btnView.isEnabled());
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        viewType = NEW;
        setTableOtherBankBranchEnableDisable(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        enableDisablePanState(true);
        enableDisablePanDistrict(true);
        setButtonEnableDisable();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        observable.setStatus();
        observable.resetForm();
        txtStateCode.setEditable(true);
        
    }//GEN-LAST:event_btnNewActionPerformed
  public int getMaxNumber(int num, int i) {
      int MaxNo=0;
      int maxnNum[]=new int[150];
       int max=maxnNum[0];
       maxnNum[i]=num;
       if(maxnNum[i]>max)
         max=maxnNum[i];
      MaxNo=max++;
      return MaxNo;
  }
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
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDisDelete;
    private com.see.truetransact.uicomponent.CButton btnDisSave;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnTalDelete;
    private com.see.truetransact.uicomponent.CButton btnTalNew;
    private com.see.truetransact.uicomponent.CButton btnTalSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CButtonGroup cButtonGroup1;
    private com.see.truetransact.uicomponent.CButtonGroup cButtonGroup2;
    private com.see.truetransact.uicomponent.CLabel lblDistrictCode;
    private com.see.truetransact.uicomponent.CLabel lblDistrictName;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace26;
    private com.see.truetransact.uicomponent.CLabel lblSpace27;
    private com.see.truetransact.uicomponent.CLabel lblSpace28;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace30;
    private com.see.truetransact.uicomponent.CLabel lblSpace31;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStateCode;
    private com.see.truetransact.uicomponent.CLabel lblStateName;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTalukCode;
    private com.see.truetransact.uicomponent.CLabel lblTalukName;
    private com.see.truetransact.uicomponent.CMenuBar mbrOtherBank;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panButton;
    private com.see.truetransact.uicomponent.CPanel panButton1;
    private com.see.truetransact.uicomponent.CPanel panButton2;
    private com.see.truetransact.uicomponent.CPanel panButton3;
    private com.see.truetransact.uicomponent.CPanel panDistrict;
    private com.see.truetransact.uicomponent.CPanel panDistrictInfo;
    private com.see.truetransact.uicomponent.CPanel panMain;
    private com.see.truetransact.uicomponent.CPanel panState;
    private com.see.truetransact.uicomponent.CPanel panStateDisInfo;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTaluk;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private com.see.truetransact.uicomponent.CSeparator sptDescription;
    private com.see.truetransact.uicomponent.CSeparator sptDescription1;
    private com.see.truetransact.uicomponent.CSeparator sptDescription2;
    private com.see.truetransact.uicomponent.CScrollPane srpDistrictDetails;
    private com.see.truetransact.uicomponent.CScrollPane srpStateDisDetails;
    private com.see.truetransact.uicomponent.CTable tblDistrict;
    private com.see.truetransact.uicomponent.CTable tblStateDis;
    private javax.swing.JToolBar tbrOtherBank;
    private com.see.truetransact.uicomponent.CTextField txtDistrictCode;
    private com.see.truetransact.uicomponent.CTextField txtDistrictName;
    private com.see.truetransact.uicomponent.CTextField txtStateCode;
    private com.see.truetransact.uicomponent.CTextField txtStateName;
    private com.see.truetransact.uicomponent.CTextField txtTalukCode;
    private com.see.truetransact.uicomponent.CTextField txtTalukName;
    // End of variables declaration//GEN-END:variables
    
}
