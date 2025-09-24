/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * RestoreStockUI.java
 *
 * Created on April , 2016, 10.30 AM
 */

package com.see.truetransact.ui.trading.restorestock;

/**
 *
 * @author  Revathi L
 */
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.PercentageValidation;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import java.util.ResourceBundle;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RestoreStockUI extends CInternalFrame implements UIMandatoryField,Observer{
    ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.trading.restorestock.RestoreStockRB", ProxyParameters.LANGUAGE);
    private HashMap mandatoryMap;
    private RestoreStockMRB objMandatoryRB = new RestoreStockMRB();
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private RestoreStockOB observable;
    private String viewType = "";
    private final String AUTHORIZE = "Authorize";
    boolean isFilled = false;
    private List finalList = null;
    
    /** Creates new form TDSConfigUI */
    public RestoreStockUI() {
        initForm();
    }
    
    /** Method called from consturctor to initialize the form **/
    private void initForm(){
        initComponents();
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setHelpMessage();
        setMaxLengths();
        setObservable();
        initComponentData();
        observable.resetForm();
        ClientUtil.enableDisable(panRestoreStock, false);
        setButtonEnableDisable();
        setBtnEnableDisable(false);
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
        lbSpace2.setName("lbSpace2");
        lblMsg.setName("lblMsg");
        lblSpace1.setName("lblSpace1");
        lblSpace3.setName("lblSpace3");
        lblSpace5.setName("lblSpace5");
        lblStatus.setName("lblStatus");
        mbrTDSConfig.setName("mbrTDSConfig");
        panStatus.setName("panStatus");
    }
    
   /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lbSpace2.setText(resourceBundle.getString("lbSpace2"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
    }
    
  /* Auto Generated Method - setMandatoryHashMap()
   
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
   
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("tdtStartDate", new Boolean(true));
        mandatoryMap.put("tdtEndDate", new Boolean(true));
        mandatoryMap.put("txtTdsId", new Boolean(true));
        mandatoryMap.put("txtCutOfAmount", new Boolean(true));
        mandatoryMap.put("cboScope", new Boolean(true));
        mandatoryMap.put("cboCustTypeVal", new Boolean(true));
        mandatoryMap.put("txtPercentage", new Boolean(true));
        mandatoryMap.put("rdoCutOff_Yes", new Boolean(true));
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
    }
    
    /** This method sets the Maximum allowed lenght to the textfields **/
    private void setMaxLengths(){
    txtTotalRestoreAmt.setValidation(new CurrencyValidation(14, 2));
    }
    
    /**  This method is to add this class as an Observer to an Observable **/
    private void setObservable(){
        try{
            observable =RestoreStockOB.getInstance();
            observable.addObserver(this);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /*Setting model to the combobox cboScope  */
    private void initComponentData() {
        try{
        }catch(ClassCastException e){
            parseException.logException(e,true);
        }
    }
    
    /*Makes the button Enable or Disable accordingly when usier clicks new,edit or delete buttons */
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
        btnView.setEnabled(!btnView.isEnabled());
    }
    
    private void setBtnEnableDisable(boolean flag) {
        btnFromProdID.setEnabled(flag);
        btnToProdID.setEnabled(flag);
        btnFromPvID.setEnabled(flag);
        btnToPvID.setEnabled(flag);
        tdtFromDate.setEnabled(flag);
        tdtToDate.setEnabled(flag);
        btnDisplay.setEnabled(flag);
    }
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setLblRestoreID(CommonUtil.convertObjToStr(lblRestoreID.getText()));
        observable.setFromProdID(CommonUtil.convertObjToStr(txtFromProdID.getText()));
        observable.setToProdID(CommonUtil.convertObjToStr(txtToProdID.getText()));
        observable.setFromPvID(CommonUtil.convertObjToStr(txtFromPvID.getText()));
        observable.setToPvID(CommonUtil.convertObjToStr(txtToPvID.getText()));
        observable.setLblRestoreID(CommonUtil.convertObjToStr(lblRestoreID.getText()));
        observable.setFromDt(CommonUtil.convertObjToStr(tdtFromDate.getDateValue()));
        observable.setToDt(CommonUtil.convertObjToStr(tdtToDate.getDateValue()));
        observable.setRestoreTotAmt(CommonUtil.convertObjToStr(txtTotalRestoreAmt.getText()));
    }
    
    /* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
    }
    
    /* Method used to showPopup ViewAll by Executing a Query */
    private void callView(String currField) {
         viewType = currField;
        HashMap viewMap = new HashMap();
        if (currField.equalsIgnoreCase("FROM_PROD_ID") || currField.equalsIgnoreCase("TO_PROD_ID")) {
            HashMap map = new HashMap();
            if (viewType.equalsIgnoreCase("TO_PROD_ID")) {
                map.put("TO_PROD_ID", "TO_PROD_ID");
                map.put("PRODUCT_ID", CommonUtil.convertObjToStr(txtFromProdID.getText()));
            }
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getTradingProductList");
            new ViewAll(this, viewMap).show();
        }else if (currField.equalsIgnoreCase("FROM_PV_ID") || currField.equalsIgnoreCase("TO_PV_ID")) {
            HashMap map = new HashMap();
            if (viewType.equalsIgnoreCase("TO_PV_ID")) {
                map.put("TO_PV_ID", "TO_PV_ID");
                map.put("PV_ID", CommonUtil.convertObjToStr(txtFromPvID.getText()));
            }
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getFromPVIDList");
            new ViewAll(this, viewMap).show();
        }else if (currField.equalsIgnoreCase("Edit")){
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getEditRestoreStock");
            new ViewAll(this, viewMap).show();
        }
    }
    
    /* Fills up the HashMap with data when user selects the row in ViewAll screen  */
    public void fillData(Object  map) {
        HashMap hash = (HashMap) map;
        isFilled = true;
        if (viewType.equalsIgnoreCase("FROM_PROD_ID")) {
            txtFromProdID.setText(CommonUtil.convertObjToStr(hash.get("PRODUCT_ID")));
        } else if (viewType.equalsIgnoreCase("TO_PROD_ID")) {
            txtToProdID.setText(CommonUtil.convertObjToStr(hash.get("PRODUCT_ID")));
        }else if (viewType.equalsIgnoreCase("FROM_PV_ID")) {
            txtFromPvID.setText(CommonUtil.convertObjToStr(hash.get("PV_ID")));
        }else if (viewType.equalsIgnoreCase("TO_PV_ID")) {
            txtToPvID.setText(CommonUtil.convertObjToStr(hash.get("PV_ID")));
        }else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
            lblRestoreID.setText(CommonUtil.convertObjToStr(hash.get("RESTORE_ID")));
            txtFromProdID.setText(CommonUtil.convertObjToStr(hash.get("FROM_PROD_ID")));
            txtToProdID.setText(CommonUtil.convertObjToStr(hash.get("TO_PROD_ID")));
            txtFromPvID.setText(CommonUtil.convertObjToStr(hash.get("FROM_PV_ID")));
            txtToPvID.setText(CommonUtil.convertObjToStr(hash.get("TO_PV_ID")));
            tdtFromDate.setDateValue(CommonUtil.convertObjToStr(hash.get("FROM_DT")));
            tdtToDate.setDateValue(CommonUtil.convertObjToStr(hash.get("TO_DT")));
            chkSelectAll.setSelected(true);
            setButtonEnableDisable();
            observable.getData(hash);
            tblRestoreDetails.setModel(observable.getTblRestoreDetails());
            chkSelectAll.setEnabled(true);
            setSizeRestoreTableData();
            totalRestoreAmountCalc();
            setRightAlignment(15);
        }else if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION
                || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
            lblRestoreID.setText(CommonUtil.convertObjToStr(hash.get("RESTORE_ID")));
            txtFromProdID.setText(CommonUtil.convertObjToStr(hash.get("FROM_PROD_ID")));
            txtToProdID.setText(CommonUtil.convertObjToStr(hash.get("TO_PROD_ID")));
            txtFromPvID.setText(CommonUtil.convertObjToStr(hash.get("FROM_PV_ID")));
            txtToPvID.setText(CommonUtil.convertObjToStr(hash.get("TO_PV_ID")));
            tdtFromDate.setDateValue(CommonUtil.convertObjToStr(hash.get("FROM_DT")));
            tdtToDate.setDateValue(CommonUtil.convertObjToStr(hash.get("TO_DT")));
            chkSelectAll.setSelected(true);
            setButtonEnableDisable();
            observable.getData(hash);
            tblRestoreDetails.setModel(observable.getTblRestoreDetails());
            chkSelectAll.setEnabled(false);
            setSizeRestoreTableData();
            totalRestoreAmountCalc();
            setRightAlignment(15);
        }
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }
    
    /** Method used to check whether the Mandatory Fields in the Form are Filled or not */
    private String checkMandatory(javax.swing.JComponent component){
        return new MandatoryCheck().checkMandatory(getClass().getName(), component);
    }
    
    /** Method used to Give a Alert when any Mandatory Field is not filled by the user */
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
  
    
    /* set the screen after the updation,insertion, deletion */
    private void settings(){
        observable.resetForm();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panRestoreStock, false);
        setButtonEnableDisable();
        observable.setResultStatus();
    }
    
   
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoCutOff = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrTDSConfig = new com.see.truetransact.uicomponent.CToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace73 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace74 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lbSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace75 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace76 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace77 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace78 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panRestoreStock = new com.see.truetransact.uicomponent.CPanel();
        panSearch = new com.see.truetransact.uicomponent.CPanel();
        lblFromDate = new com.see.truetransact.uicomponent.CLabel();
        tdtFromDate = new com.see.truetransact.uicomponent.CDateField();
        lblToDate = new com.see.truetransact.uicomponent.CLabel();
        tdtToDate = new com.see.truetransact.uicomponent.CDateField();
        panToPvID = new com.see.truetransact.uicomponent.CPanel();
        txtToPvID = new com.see.truetransact.uicomponent.CTextField();
        btnToPvID = new com.see.truetransact.uicomponent.CButton();
        lblToPvID = new com.see.truetransact.uicomponent.CLabel();
        panFromProdID = new com.see.truetransact.uicomponent.CPanel();
        txtFromProdID = new com.see.truetransact.uicomponent.CTextField();
        btnFromProdID = new com.see.truetransact.uicomponent.CButton();
        lblFromProdID = new com.see.truetransact.uicomponent.CLabel();
        panFromPvID = new com.see.truetransact.uicomponent.CPanel();
        txtFromPvID = new com.see.truetransact.uicomponent.CTextField();
        btnFromPvID = new com.see.truetransact.uicomponent.CButton();
        lblFromPvID = new com.see.truetransact.uicomponent.CLabel();
        panToProdID = new com.see.truetransact.uicomponent.CPanel();
        txtToProdID = new com.see.truetransact.uicomponent.CTextField();
        btnToProdID = new com.see.truetransact.uicomponent.CButton();
        lblToProdID = new com.see.truetransact.uicomponent.CLabel();
        panProcess = new com.see.truetransact.uicomponent.CPanel();
        btnDisplay = new com.see.truetransact.uicomponent.CButton();
        panGroupData = new com.see.truetransact.uicomponent.CPanel();
        srpRestoreDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblRestoreDetails = new com.see.truetransact.uicomponent.CTable();
        panTotal = new com.see.truetransact.uicomponent.CPanel();
        lblTotalPayment = new com.see.truetransact.uicomponent.CLabel();
        txtTotalRestoreAmt = new com.see.truetransact.uicomponent.CTextField();
        lblAllignment3 = new com.see.truetransact.uicomponent.CLabel();
        panSelectAll = new com.see.truetransact.uicomponent.CPanel();
        lblSelectAll = new com.see.truetransact.uicomponent.CLabel();
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        lblRestoreID = new com.see.truetransact.uicomponent.CLabel();
        mbrTDSConfig = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptView = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(920, 640));
        setPreferredSize(new java.awt.Dimension(920, 640));

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
        tbrTDSConfig.add(btnView);

        lblSpace4.setText("     ");
        tbrTDSConfig.add(lblSpace4);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrTDSConfig.add(btnNew);

        lblSpace73.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace73.setText("     ");
        lblSpace73.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTDSConfig.add(lblSpace73);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrTDSConfig.add(btnEdit);

        lblSpace74.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace74.setText("     ");
        lblSpace74.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTDSConfig.add(lblSpace74);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrTDSConfig.add(btnDelete);

        lbSpace2.setText("     ");
        tbrTDSConfig.add(lbSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrTDSConfig.add(btnSave);

        lblSpace75.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace75.setText("     ");
        lblSpace75.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTDSConfig.add(lblSpace75);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrTDSConfig.add(btnCancel);

        lblSpace3.setText("     ");
        tbrTDSConfig.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrTDSConfig.add(btnAuthorize);

        lblSpace76.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace76.setText("     ");
        lblSpace76.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace76.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace76.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTDSConfig.add(lblSpace76);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrTDSConfig.add(btnException);

        lblSpace77.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace77.setText("     ");
        lblSpace77.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace77.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace77.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTDSConfig.add(lblSpace77);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrTDSConfig.add(btnReject);

        lblSpace5.setText("     ");
        tbrTDSConfig.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrTDSConfig.add(btnPrint);

        lblSpace78.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace78.setText("     ");
        lblSpace78.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace78.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace78.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTDSConfig.add(lblSpace78);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrTDSConfig.add(btnClose);

        getContentPane().add(tbrTDSConfig, java.awt.BorderLayout.NORTH);

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

        panRestoreStock.setLayout(new java.awt.GridBagLayout());

        panSearch.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panSearch.setMinimumSize(new java.awt.Dimension(810, 100));
        panSearch.setPreferredSize(new java.awt.Dimension(810, 120));
        panSearch.setLayout(new java.awt.GridBagLayout());

        lblFromDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFromDate.setText("From Date ");
        lblFromDate.setMaximumSize(new java.awt.Dimension(80, 18));
        lblFromDate.setMinimumSize(new java.awt.Dimension(80, 18));
        lblFromDate.setPreferredSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(lblFromDate, gridBagConstraints);

        tdtFromDate.setEnabled(false);
        tdtFromDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtFromDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(tdtFromDate, gridBagConstraints);

        lblToDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblToDate.setText("To Date ");
        lblToDate.setMaximumSize(new java.awt.Dimension(80, 18));
        lblToDate.setMinimumSize(new java.awt.Dimension(80, 18));
        lblToDate.setPreferredSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(lblToDate, gridBagConstraints);

        tdtToDate.setEnabled(false);
        tdtToDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtToDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(tdtToDate, gridBagConstraints);

        panToPvID.setLayout(new java.awt.GridBagLayout());

        txtToPvID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtToPvID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtToPvIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panToPvID.add(txtToPvID, gridBagConstraints);

        btnToPvID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnToPvID.setEnabled(false);
        btnToPvID.setMaximumSize(new java.awt.Dimension(21, 21));
        btnToPvID.setMinimumSize(new java.awt.Dimension(21, 21));
        btnToPvID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnToPvID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToPvIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panToPvID.add(btnToPvID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(panToPvID, gridBagConstraints);

        lblToPvID.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblToPvID.setText("To PV ID");
        lblToPvID.setMaximumSize(new java.awt.Dimension(80, 18));
        lblToPvID.setMinimumSize(new java.awt.Dimension(80, 18));
        lblToPvID.setPreferredSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(lblToPvID, gridBagConstraints);

        panFromProdID.setLayout(new java.awt.GridBagLayout());

        txtFromProdID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFromProdID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFromProdIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panFromProdID.add(txtFromProdID, gridBagConstraints);

        btnFromProdID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnFromProdID.setEnabled(false);
        btnFromProdID.setMaximumSize(new java.awt.Dimension(21, 21));
        btnFromProdID.setMinimumSize(new java.awt.Dimension(21, 21));
        btnFromProdID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnFromProdID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFromProdIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panFromProdID.add(btnFromProdID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(panFromProdID, gridBagConstraints);

        lblFromProdID.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFromProdID.setText("From Prod ID");
        lblFromProdID.setMaximumSize(new java.awt.Dimension(80, 18));
        lblFromProdID.setMinimumSize(new java.awt.Dimension(80, 18));
        lblFromProdID.setPreferredSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(lblFromProdID, gridBagConstraints);

        panFromPvID.setLayout(new java.awt.GridBagLayout());

        txtFromPvID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFromPvID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFromPvIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panFromPvID.add(txtFromPvID, gridBagConstraints);

        btnFromPvID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnFromPvID.setEnabled(false);
        btnFromPvID.setMaximumSize(new java.awt.Dimension(21, 21));
        btnFromPvID.setMinimumSize(new java.awt.Dimension(21, 21));
        btnFromPvID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnFromPvID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFromPvIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panFromPvID.add(btnFromPvID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(panFromPvID, gridBagConstraints);

        lblFromPvID.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFromPvID.setText("From PV ID");
        lblFromPvID.setMaximumSize(new java.awt.Dimension(80, 18));
        lblFromPvID.setMinimumSize(new java.awt.Dimension(80, 18));
        lblFromPvID.setPreferredSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(lblFromPvID, gridBagConstraints);

        panToProdID.setLayout(new java.awt.GridBagLayout());

        txtToProdID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtToProdID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtToProdIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panToProdID.add(txtToProdID, gridBagConstraints);

        btnToProdID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnToProdID.setEnabled(false);
        btnToProdID.setMaximumSize(new java.awt.Dimension(21, 21));
        btnToProdID.setMinimumSize(new java.awt.Dimension(21, 21));
        btnToProdID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnToProdID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToProdIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panToProdID.add(btnToProdID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(panToProdID, gridBagConstraints);

        lblToProdID.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblToProdID.setText("To Prod ID");
        lblToProdID.setMaximumSize(new java.awt.Dimension(80, 18));
        lblToProdID.setMinimumSize(new java.awt.Dimension(80, 18));
        lblToProdID.setPreferredSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(lblToProdID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRestoreStock.add(panSearch, gridBagConstraints);

        panProcess.setMinimumSize(new java.awt.Dimension(850, 30));
        panProcess.setPreferredSize(new java.awt.Dimension(850, 30));
        panProcess.setLayout(new java.awt.GridBagLayout());

        btnDisplay.setBackground(new java.awt.Color(204, 204, 204));
        btnDisplay.setForeground(new java.awt.Color(255, 0, 0));
        btnDisplay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/Down_Arrow.gif"))); // NOI18N
        btnDisplay.setText("Display ");
        btnDisplay.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnDisplay.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnDisplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDisplayActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panProcess.add(btnDisplay, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 12;
        panRestoreStock.add(panProcess, gridBagConstraints);

        panGroupData.setBorder(javax.swing.BorderFactory.createTitledBorder("Restore Stock Details"));
        panGroupData.setMinimumSize(new java.awt.Dimension(900, 280));
        panGroupData.setPreferredSize(new java.awt.Dimension(900, 500));
        panGroupData.setLayout(new java.awt.GridBagLayout());

        srpRestoreDetails.setPreferredSize(new java.awt.Dimension(454, 344));

        tblRestoreDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "Prod ID", "Prod Name", "Type", "Stock ID", "Avail Qty", "Phy Qty", "Diff", "Purh Price", "Mrp", "Sales Price", "Restore Qty", "Restore Amt", "Total Amt"
            }
        ));
        tblRestoreDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(450, 350));
        tblRestoreDetails.setSelectionBackground(new java.awt.Color(204, 204, 255));
        tblRestoreDetails.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tblRestoreDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblRestoreDetailsMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblRestoreDetailsMouseReleased(evt);
            }
        });
        tblRestoreDetails.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tblRestoreDetailsFocusLost(evt);
            }
        });
        tblRestoreDetails.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblRestoreDetailsKeyReleased(evt);
            }
        });
        srpRestoreDetails.setViewportView(tblRestoreDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panGroupData.add(srpRestoreDetails, gridBagConstraints);

        panTotal.setMinimumSize(new java.awt.Dimension(850, 28));
        panTotal.setPreferredSize(new java.awt.Dimension(850, 28));
        panTotal.setLayout(new java.awt.GridBagLayout());

        lblTotalPayment.setForeground(new java.awt.Color(0, 0, 255));
        lblTotalPayment.setText("Total Restore Amount : ");
        lblTotalPayment.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 40, 0, 0);
        panTotal.add(lblTotalPayment, gridBagConstraints);

        txtTotalRestoreAmt.setBackground(new java.awt.Color(204, 204, 204));
        txtTotalRestoreAmt.setMinimumSize(new java.awt.Dimension(110, 21));
        txtTotalRestoreAmt.setPreferredSize(new java.awt.Dimension(110, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panTotal.add(txtTotalRestoreAmt, gridBagConstraints);

        lblAllignment3.setForeground(new java.awt.Color(0, 51, 255));
        lblAllignment3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAllignment3.setMaximumSize(new java.awt.Dimension(500, 18));
        lblAllignment3.setMinimumSize(new java.awt.Dimension(550, 18));
        lblAllignment3.setPreferredSize(new java.awt.Dimension(600, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        panTotal.add(lblAllignment3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panGroupData.add(panTotal, gridBagConstraints);

        panSelectAll.setMinimumSize(new java.awt.Dimension(95, 17));
        panSelectAll.setPreferredSize(new java.awt.Dimension(95, 17));
        panSelectAll.setLayout(new java.awt.GridBagLayout());

        lblSelectAll.setText("Select All");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSelectAll.add(lblSelectAll, gridBagConstraints);

        chkSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelectAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 5, 1);
        panSelectAll.add(chkSelectAll, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.2;
        panGroupData.add(panSelectAll, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        panRestoreStock.add(panGroupData, gridBagConstraints);

        lblRestoreID.setForeground(new java.awt.Color(51, 51, 255));
        lblRestoreID.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblRestoreID.setMaximumSize(new java.awt.Dimension(200, 18));
        lblRestoreID.setMinimumSize(new java.awt.Dimension(200, 18));
        lblRestoreID.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRestoreStock.add(lblRestoreID, gridBagConstraints);

        getContentPane().add(panRestoreStock, java.awt.BorderLayout.CENTER);

        mnuProcess.setText("Process");

        mitNew.setText("New");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptView);

        mitSave.setText("Save");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrTDSConfig.add(mnuProcess);

        setJMenuBar(mbrTDSConfig);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        callView(ClientConstants.ACTION_STATUS[17]);
    }//GEN-LAST:event_btnViewActionPerformed
            
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // TODO add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // TODO add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // TODO add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // TODO add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
        //        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        updateAuthorizeStatus(CommonConstants.STATUS_REJECTED);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        updateAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
       if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0) {
                if (observable.getProxyReturnMap().containsKey("TRANSFER_TRANS_LIST")) {
                    displayTransDetail(observable.getProxyReturnMap());
                    observable.setProxyReturnMap(null);
                }
            }
        }
        btnCancel.setEnabled(true);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        ClientUtil.clearAll(this);
        lblRestoreID.setText("");
        ClientUtil.enableDisable(panRestoreStock, false);
        setBtnEnableDisable(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        setModified(false);
        
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        finalList = observable.getFinalList();
        HashMap dataMap = new HashMap();
        HashMap deleteMap = new HashMap();
        HashMap singleMap = new HashMap();
        if (finalList != null && finalList.size() > 0) {
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT && tblRestoreDetails.getRowCount() > 0) {
                for (int j = 0; j < tblRestoreDetails.getRowCount(); j++) {
                    dataMap = (HashMap) finalList.get(j);
                    singleMap = new HashMap();
                    if (!((Boolean) tblRestoreDetails.getValueAt(j, 0)).booleanValue()) {
                        singleMap.put("PV_ID", CommonUtil.convertObjToStr(dataMap.get("PV_ID")));
                        singleMap.put("SL_NO", CommonUtil.convertObjToStr(dataMap.get("SL_NO")));
                        deleteMap.put(CommonUtil.convertObjToStr(dataMap.get("SL_NO")), singleMap);
                    }
                }
            }
            for (int i = 0; i < finalList.size(); i++) {
                dataMap = new HashMap();
                String rowNo = "";
                dataMap = (HashMap) finalList.get(i);
                rowNo = CommonUtil.convertObjToStr(dataMap.get("ROW_NO"));
                for (int j = 0; j < tblRestoreDetails.getRowCount(); j++) {
                    if (CommonUtil.convertObjToStr(tblRestoreDetails.getValueAt(j, 1)).equals(rowNo) && !((Boolean) tblRestoreDetails.getValueAt(j, 0)).booleanValue()) {
                        finalList.remove(i--);
                    }
                    if (CommonUtil.convertObjToStr(tblRestoreDetails.getValueAt(j, 1)).equals(rowNo) && ((Boolean) tblRestoreDetails.getValueAt(j, 0)).booleanValue()) {
                        dataMap.put("RESTORE_QTY", CommonUtil.convertObjToStr(tblRestoreDetails.getValueAt(j, 14)));
                        dataMap.put("RESTORE_AMT", CommonUtil.convertObjToStr(tblRestoreDetails.getValueAt(j, 15)));
                    }
                }
            }
            if (finalList != null && finalList.size() > 0) {
                observable.setFinalList(finalList);
               if(deleteMap!=null && deleteMap.size()>0){
                  observable.setDeleteMap(deleteMap);
               }
                savePerformed();
            }
            btnReject.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnException.setEnabled(true);
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView( ClientConstants.ACTION_STATUS[3]);
         btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView(ClientConstants.ACTION_STATUS[2]);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        observable.resetForm();
        ClientUtil.enableDisable(panRestoreStock, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        HashMap checkAuthMap = new HashMap();
        List checkAuthLst = ClientUtil.executeQuery("checkRestoreStockAuthStatus", checkAuthMap);
        if (checkAuthLst != null && checkAuthLst.size() > 0) {
            ClientUtil.showMessageWindow("Previous Restore Record pending Authorization !!!");
            return;
        }
        setButtonEnableDisable();
        setBtnEnableDisable(true);
        //__ To Save the data in the Internal Frame...
        setModified(true);
         btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnNewActionPerformed

    private void tdtFromDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtFromDateFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tdtFromDateFocusLost

    private void tdtToDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtToDateFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tdtToDateFocusLost

    private void txtToPvIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToPvIDFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtToPvIDFocusLost

    private void btnToPvIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToPvIDActionPerformed
        // TODO add your handling code here:
        callView("TO_PV_ID");
    }//GEN-LAST:event_btnToPvIDActionPerformed

    private void txtFromProdIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFromProdIDFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFromProdIDFocusLost

    private void btnFromProdIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFromProdIDActionPerformed
        // TODO add your handling code here:
        callView("FROM_PROD_ID");
    }//GEN-LAST:event_btnFromProdIDActionPerformed

    private void txtFromPvIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFromPvIDFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFromPvIDFocusLost

    private void btnFromPvIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFromPvIDActionPerformed
        // TODO add your handling code here:
        callView("FROM_PV_ID");
    }//GEN-LAST:event_btnFromPvIDActionPerformed

    private void txtToProdIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToProdIDFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtToProdIDFocusLost

    private void btnToProdIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToProdIDActionPerformed
        // TODO add your handling code here:
        callView("TO_PROD_ID");
    }//GEN-LAST:event_btnToProdIDActionPerformed

    private void btnDisplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDisplayActionPerformed
        // TODO add your handling code here:
        try {
            if (tblRestoreDetails.getRowCount() > 0) {
                observable.resetForm();
            }
            if ((txtFromProdID.getText().length() > 0 && txtToProdID.getText().length() > 0)
                    || (tdtFromDate.getDateValue().length() > 0 && tdtToDate.getDateValue().length() > 0)
                    || (txtFromPvID.getText().length() > 0 && txtToPvID.getText().length() > 0)) {
                HashMap hash = new HashMap();
                if (txtFromProdID.getText().length() > 0 && txtToProdID.getText().length() > 0) {
                    hash.put("FROM_PROD_ID", txtFromProdID.getText());
                    hash.put("TO_PROD_ID", txtToProdID.getText());
                }else if((txtFromProdID.getText().length() > 0 && txtToProdID.getText().length() == 0)
                        ||(txtFromProdID.getText().length() == 0 && txtToProdID.getText().length() > 0)){
                    ClientUtil.showMessageWindow("Enter From/TO Product ID");
                    txtFromProdID.setText("");
                    txtToProdID.setText("");
                    return;
                }
                if (tdtFromDate.getDateValue().length() > 0 && tdtToDate.getDateValue().length() > 0) {
                    Date fromDt = (DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue()));
                    Date toDt = (DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue()));
                    hash.put("FROM_DT", DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtFromDate.getDateValue())));
                    hash.put("TO_DT", DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtToDate.getDateValue())));
                    if (DateUtil.dateDiff(toDt, fromDt) > 0) {
                        ClientUtil.showMessageWindow("From Date should not be greater than To Date!!!");
                        tdtFromDate.setDateValue("");
                        tdtToDate.setDateValue("");
                        return;
                    }
                }else if((tdtFromDate.getDateValue().length() > 0 && tdtToDate.getDateValue().length() == 0)
                        ||(tdtFromDate.getDateValue().length() == 0 && tdtFromDate.getDateValue().length() > 0)){
                    ClientUtil.showMessageWindow("Enter From/To Date");
                    tdtFromDate.setDateValue("");
                    tdtFromDate.setDateValue("");
                    return;
                }
                if (txtFromPvID.getText().length() > 0 && txtToPvID.getText().length() > 0) {
                    hash.put("FROM_PV_ID", txtFromPvID.getText());
                    hash.put("TO_PV_ID", txtToPvID.getText());
                }else if((txtFromPvID.getText().length() > 0 && txtToPvID.getText().length() == 0)
                        ||(txtFromPvID.getText().length() == 0 && txtToPvID.getText().length() > 0)){
                    ClientUtil.showMessageWindow("Enter From/To PV ID");
                    txtFromPvID.setText("");
                    txtToPvID.setText("");
                    return;
                }
                observable.resetRestoretockTblDetails();
                observable.displayRestoreDetails(hash);
                tblRestoreDetails.setModel(observable.getTblRestoreDetails());
                chkSelectAll.setEnabled(true);
                setSizeRestoreTableData();
//            btnTblClear.setEnabled(true);
//            tdtFromDt.setEnabled(false);
//            tdtToDt.setEnabled(false);

            } else {
                ClientUtil.showMessageWindow("Please select any one filter options...");
                return;
            }
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }//GEN-LAST:event_btnDisplayActionPerformed

    private void tblRestoreDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblRestoreDetailsMouseClicked
        // TODO add your handling code here:
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            if (tblRestoreDetails.getSelectedColumn() == 0) {
                String st = CommonUtil.convertObjToStr(tblRestoreDetails.getValueAt(tblRestoreDetails.getSelectedRow(), 0));
                if (st.equals("true")) {
                    tblRestoreDetails.setValueAt(new Boolean(false), tblRestoreDetails.getSelectedRow(), 0);
                    tblRestoreDetails.setValueAt(CommonUtil.convertObjToInt(String.valueOf("0")), tblRestoreDetails.getSelectedRow(), 14);
                    tblRestoreDetails.setValueAt(CommonUtil.convertObjToDouble(String.valueOf("0.0")), tblRestoreDetails.getSelectedRow(), 15);
                    totalRestoreAmountCalc();
                } else {
                    tblRestoreDetails.setValueAt(new Boolean(true), tblRestoreDetails.getSelectedRow(), 0);
                }
            }
            if (tblRestoreDetails.getSelectedColumn() == 14) {
                String st = CommonUtil.convertObjToStr(tblRestoreDetails.getValueAt(tblRestoreDetails.getSelectedRow(), 0));
                if (st.equals("true")) {
                    observable.cellRestoreEditableColumnTrue();
                } else {
                    observable.cellRestoreEditableColumnFalse();
                }
            }
        }
    }//GEN-LAST:event_tblRestoreDetailsMouseClicked

    private void tblRestoreDetailsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblRestoreDetailsMouseReleased
        // TODO add your handling code here:
       
    }//GEN-LAST:event_tblRestoreDetailsMouseReleased

    private void tblRestoreDetailsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblRestoreDetailsFocusLost
        // TODO add your handling code here:
        
    }//GEN-LAST:event_tblRestoreDetailsFocusLost

    private void tblRestoreDetailsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblRestoreDetailsKeyReleased
        // TODO add your handling code here:
        int diff = 0;
        int reQty = 0;
        double purchPrice = 0.0;
        double returnTot = 0.0;
        if ((tblRestoreDetails.getRowCount() > 0) && (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW||
                observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT)) {
            diff = CommonUtil.convertObjToInt(tblRestoreDetails.getValueAt(tblRestoreDetails.getSelectedRow(), 9));
            reQty = CommonUtil.convertObjToInt(tblRestoreDetails.getValueAt(tblRestoreDetails.getSelectedRow(), 14));
            if (diff < reQty) {
                ClientUtil.showMessageWindow("Restore Qty should not be more than Dificit Qty.");
                tblRestoreDetails.setValueAt(CommonUtil.convertObjToInt(String.valueOf("0")), tblRestoreDetails.getSelectedRow(), 14);
                tblRestoreDetails.setValueAt(CommonUtil.convertObjToDouble(String.valueOf("0")), tblRestoreDetails.getSelectedRow(), 15);
                return;
            } else {
                purchPrice = CommonUtil.convertObjToDouble(tblRestoreDetails.getValueAt(tblRestoreDetails.getSelectedRow(), 10));
                returnTot = purchPrice * reQty;
                Rounding rod = new Rounding();
                returnTot = (double) rod.getNearest((long) (returnTot * 100), 100) / 100;
                if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
                tblRestoreDetails.setValueAt(CommonUtil.convertObjToDouble(String.valueOf(returnTot)), tblRestoreDetails.getSelectedRow(), 15);
                }else{
                   tblRestoreDetails.setValueAt((String.valueOf(returnTot)), tblRestoreDetails.getSelectedRow(), 15); 
                }
            }
            totalRestoreAmountCalc();
        }
       
    }//GEN-LAST:event_tblRestoreDetailsKeyReleased

    private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
        // TODO add your handling code here:
        boolean flag;
        if (chkSelectAll.isSelected() == true) {
            flag = true;
        } else {
            flag = false;
        }
        double totAmount = 0;
        for (int i = 0; i < tblRestoreDetails.getRowCount(); i++) {
            tblRestoreDetails.setValueAt(new Boolean(flag), i, 0);
            if (flag == false) {
                tblRestoreDetails.setValueAt(CommonUtil.convertObjToInt(String.valueOf("0")), i, 14);
                tblRestoreDetails.setValueAt(CommonUtil.convertObjToDouble(String.valueOf("0")), i, 15);
            }
        }
        totalRestoreAmountCalc();
    }//GEN-LAST:event_chkSelectAllActionPerformed
   private double totalRestoreAmountCalc() {
        double totAmt = 0.0;
        if (tblRestoreDetails.getRowCount() > 0) {
            for (int i = 0; i < tblRestoreDetails.getRowCount(); i++) {
                String Amt = CommonUtil.convertObjToStr(tblRestoreDetails.getValueAt(i, 15));
                Amt = Amt.replace(",", "");
                totAmt = totAmt + CommonUtil.convertObjToDouble(Amt);
            }
            txtTotalRestoreAmt.setText(CommonUtil.convertObjToStr(new Double(totAmt)));
        }
        return totAmt;
    }
    
     private void setRightAlignment(int col) {
        javax.swing.table.DefaultTableCellRenderer r = new javax.swing.table.DefaultTableCellRenderer();
        r.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        tblRestoreDetails.getColumnModel().getColumn(col).setCellRenderer(r);
        tblRestoreDetails.getColumnModel().getColumn(col).sizeWidthToFit();
    }
     
     private void setSizeRestoreTableData() {
        tblRestoreDetails.getColumnModel().getColumn(0).setPreferredWidth(2);
        tblRestoreDetails.getColumnModel().getColumn(1).setPreferredWidth(2);
        tblRestoreDetails.getColumnModel().getColumn(2).setPreferredWidth(30);
        tblRestoreDetails.getColumnModel().getColumn(3).setPreferredWidth(30);
        tblRestoreDetails.getColumnModel().getColumn(4).setPreferredWidth(120);
        tblRestoreDetails.getColumnModel().getColumn(5).setPreferredWidth(10);
        tblRestoreDetails.getColumnModel().getColumn(6).setPreferredWidth(30);
        tblRestoreDetails.getColumnModel().getColumn(7).setPreferredWidth(30);
        tblRestoreDetails.getColumnModel().getColumn(8).setPreferredWidth(25);
        tblRestoreDetails.getColumnModel().getColumn(9).setPreferredWidth(10);
        tblRestoreDetails.getColumnModel().getColumn(10).setPreferredWidth(40);
        tblRestoreDetails.getColumnModel().getColumn(11).setPreferredWidth(15);
        tblRestoreDetails.getColumnModel().getColumn(12).setPreferredWidth(35);
        tblRestoreDetails.getColumnModel().getColumn(13).setPreferredWidth(30);
        tblRestoreDetails.getColumnModel().getColumn(14).setPreferredWidth(45);
        tblRestoreDetails.getColumnModel().getColumn(15).setPreferredWidth(45);
    }
     
     private void savePerformed() {
        updateOBFields();
        String action;
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW ){
            action=CommonConstants.TOSTATUS_INSERT;
            saveAction(action);
        }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
            action=CommonConstants.TOSTATUS_UPDATE;
            saveAction(action);
        }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE){
            action=CommonConstants.TOSTATUS_DELETE;
            saveAction(action);
        }
        
        //__ Make the Screen Closable..
        setModified(false);
    }
     
     private void saveAction(String status){
        observable.doAction();
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            if ((observable.getActionType() == ClientConstants.ACTIONTYPE_NEW)) {
                if (observable.getProxyReturnMap().containsKey("RESTORE_ID")) {
                    ClientUtil.showMessageWindow("RESTORE ID : " + observable.getProxyReturnMap().get("RESTORE_ID"));
                } 
            }
        }
        btnCancelActionPerformed(null);
    }
     
     private void updateAuthorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled) {
            ArrayList arrList = new ArrayList();
            HashMap authorizeMap = new HashMap();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("RESTORE_ID", lblRestoreID.getText());
            singleAuthorizeMap.put("TOTAL_AMOUNT", txtTotalRestoreAmt.getText());
            singleAuthorizeMap.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTHORIZED_DT", ClientUtil.getCurrentDateWithTime());
            arrList.add(singleAuthorizeMap);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            if (tblRestoreDetails.getRowCount() > 0 && CommonUtil.convertObjToDouble(txtTotalRestoreAmt.getText()) > 0) {
                authorize(authorizeMap);
            }
            viewType = "";
            super.setOpenForEditBy(observable.getStatusBy());
            singleAuthorizeMap = null;
            arrList = null;
            authorizeMap = null;
        } else {
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getEditRestoreStock");
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }
    
    public void authorize(HashMap map) {
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.setAuthorizeMap(map);
            updateOBFields();
            observable.doAction();
            setMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }
    
    private void displayTransDetail(HashMap proxyResultMap) {
        String cashDisplayStr = "Cash Transaction Details...\n";
        String transferDisplayStr = "Transfer Transaction Details...\n";
        String displayStr = "";
        String transId = "";
        String transMode = "";
        Object keys[] = proxyResultMap.keySet().toArray();
        int cashCount = 0;
        int transferCount = 0;
        List tempList = null;
        HashMap transMap = null;
        String actNum = "";
        List transIdList = new ArrayList();
        for (int i = 0; i < keys.length; i++) {
            if (proxyResultMap.get(keys[i]) instanceof String) {
                continue;
            }
            tempList = (List) proxyResultMap.get(keys[i]);
            if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER") != -1) {
                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                        transId = (String) transMap.get("BATCH_ID");
                        transIdList.add(transId);
                        transMode = "TRANSFER";
                    }
                    transferDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Batch Id : " + transMap.get("BATCH_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if (actNum != null && !actNum.equals("")) {
                        transferDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    } else {
                        transferDisplayStr += "   Account Head : " + transMap.get("AC_HD_ID")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    }
                }
                transferCount++;
            }
        }
        if (cashCount > 0) {
            displayStr += cashDisplayStr;
        }
        if (transferCount > 0) {
            displayStr += transferDisplayStr;
        }
        ClientUtil.showMessageWindow("" + displayStr);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDisplay;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnFromProdID;
    private com.see.truetransact.uicomponent.CButton btnFromPvID;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnToProdID;
    private com.see.truetransact.uicomponent.CButton btnToPvID;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lblAllignment3;
    private com.see.truetransact.uicomponent.CLabel lblFromDate;
    private com.see.truetransact.uicomponent.CLabel lblFromProdID;
    private com.see.truetransact.uicomponent.CLabel lblFromPvID;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblRestoreID;
    private com.see.truetransact.uicomponent.CLabel lblSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace73;
    private com.see.truetransact.uicomponent.CLabel lblSpace74;
    private com.see.truetransact.uicomponent.CLabel lblSpace75;
    private com.see.truetransact.uicomponent.CLabel lblSpace76;
    private com.see.truetransact.uicomponent.CLabel lblSpace77;
    private com.see.truetransact.uicomponent.CLabel lblSpace78;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblToDate;
    private com.see.truetransact.uicomponent.CLabel lblToProdID;
    private com.see.truetransact.uicomponent.CLabel lblToPvID;
    private com.see.truetransact.uicomponent.CLabel lblTotalPayment;
    private com.see.truetransact.uicomponent.CMenuBar mbrTDSConfig;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panFromProdID;
    private com.see.truetransact.uicomponent.CPanel panFromPvID;
    private com.see.truetransact.uicomponent.CPanel panGroupData;
    private com.see.truetransact.uicomponent.CPanel panProcess;
    private com.see.truetransact.uicomponent.CPanel panRestoreStock;
    private com.see.truetransact.uicomponent.CPanel panSearch;
    private com.see.truetransact.uicomponent.CPanel panSelectAll;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panToProdID;
    private com.see.truetransact.uicomponent.CPanel panToPvID;
    private com.see.truetransact.uicomponent.CPanel panTotal;
    private com.see.truetransact.uicomponent.CButtonGroup rdoCutOff;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CScrollPane srpRestoreDetails;
    private com.see.truetransact.uicomponent.CTable tblRestoreDetails;
    private com.see.truetransact.uicomponent.CToolBar tbrTDSConfig;
    private com.see.truetransact.uicomponent.CDateField tdtFromDate;
    private com.see.truetransact.uicomponent.CDateField tdtToDate;
    private com.see.truetransact.uicomponent.CTextField txtFromProdID;
    private com.see.truetransact.uicomponent.CTextField txtFromPvID;
    private com.see.truetransact.uicomponent.CTextField txtToProdID;
    private com.see.truetransact.uicomponent.CTextField txtToPvID;
    private com.see.truetransact.uicomponent.CTextField txtTotalRestoreAmt;
    // End of variables declaration//GEN-END:variables
    
}
