/* Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ReturnOfInstrumentsUI.java
 *
 * Created on April 5, 2004, 12:15 PM
 */

package com.see.truetransact.ui.clearing.returns;

/**
 *
 * @author  Ashok
 */

import com.see.truetransact.ui.clearing.returns.ReturnOfInstrumentsRB;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.ui.clearing.returns.ReturnOfInstrumentsOB;
import com.see.truetransact.ui.clearing.returns.ReturnOfInstrumentsMRB;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientproxy.ProxyParameters;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;
import java.util.Date;

public class ReturnOfInstrumentsUI extends com.see.truetransact.uicomponent.CInternalFrame implements UIMandatoryField,Observer{
    
    //    private ReturnOfInstrumentsRB resourceBundle =new ReturnOfInstrumentsRB();
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.clearing.returns.ReturnOfInstrumentsRB", ProxyParameters.LANGUAGE);
    private HashMap mandatoryMap;
    private ReturnOfInstrumentsOB observable;
    private ReturnOfInstrumentsMRB objMandatoryRB;
    private int viewType= -1;
    private final String RETURN_ID = "RETURN_ID";
    final int EDIT=0, DELETE=1, AUTHORIZE=2, BATCHNO=3;
    boolean isFilled = false;
    boolean focus = false;
    private Date currDt = null;
    
    /** Creates new form ReturnOfInstrumentsUI */
    public ReturnOfInstrumentsUI() {
        currDt = ClientUtil.getCurrentDate();
        initUI();
    }
    
    /** Intitialises the Screen */
    private void initUI(){
        initComponents();
        setFieldNames();
        internationalize();
        setMaxLengths();
        setObservable();
        initComponentData();
        setMandatoryHashMap();
        setHelpMessage();
        ClientUtil.enableDisable(panReturnsMain, false);
        setButtonEnableDisable();
        
        btnBatchNO.setEnabled(false);
    }
    
    /** Sets the Name to the Fields in the UI */
    private void setFieldNames() {
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnSave.setName("btnSave");
        btnAuthorize.setName("btnAuthorize");
        btnReject.setName("btnReject");
        btnException.setName("btnException");
        cboClearingType.setName("cboClearingType");
        cboReturnType.setName("cboReturnType");
        chkPresentAgain.setName("chkPresentAgain");
        lblBankPres.setName("lblBankPres");
        lblBkPresented.setName("lblBkPresented");
        lblBrPresented.setName("lblBrPresented");
        lblBrachPres.setName("lblBrachPres");
        lblClearingDate.setName("lblClearingDate");
        lblClearingSerialNo.setName("lblClearingSerialNo");
        lblClearingType.setName("lblClearingType");
        lblInstAmount.setName("lblInstAmount");
        lblInstDate.setName("lblInstDate");
        lblInstrumentAmount.setName("lblInstrumentAmount");
        lblInstrumentDate.setName("lblInstrumentDate");
        lblMsg.setName("lblMsg");
        lblPresentAgain.setName("lblPresentAgain");
        lblReturnType.setName("lblReturnType");
        lblScInstBelongs.setName("lblScInstBelongs");
        lblSchInstBelongs.setName("lblSchInstBelongs");
        sptVertical.setName("sptVertical");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblStatus.setName("lblStatus");
        mbrReturns.setName("mbrReturns");
        panReturnsLabels.setName("panReturnsLabels");
        panReturnsMain.setName("panReturnsMain");
        panReturnsSub.setName("panReturnsSub");
        panStatus.setName("panStatus");
        tdtClearingDate.setName("tdtClearingDate");
        txtInstrumentNo1.setName("txtInstrumentNo1");
        txtBatchId.setName("txtBatchId");
        lblReturnId.setName("lblReturnId");
        lblRetId.setName("lblRetId");
        
        btnBatchNO.setName("btnBatchNO");
        txtInstrumentNo2.setName("txtInstrumentNo2");
    }
    
    /** Internationalizing the Fields in the UI */
    private void internationalize() {
        lblScInstBelongs.setText(resourceBundle.getString("lblScInstBelongs"));
        lblPresentAgain.setText(resourceBundle.getString("lblPresentAgain"));
        chkPresentAgain.setText(resourceBundle.getString("chkPresentAgain"));
        lblInstrumentAmount.setText(resourceBundle.getString("lblInstrumentAmount"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblInstrumentDate.setText(resourceBundle.getString("lblInstrumentDate"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblClearingSerialNo.setText(resourceBundle.getString("lblClearingSerialNo"));
        lblReturnId.setText(resourceBundle.getString("lblReturnId"));
        lblRetId.setText(resourceBundle.getString("lblRetId"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblClearingType.setText(resourceBundle.getString("lblClearingType"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        
        lblClearingDate.setText(resourceBundle.getString("lblClearingDate"));
        
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblReturnType.setText(resourceBundle.getString("lblReturnType"));
        lblSchInstBelongs.setText(resourceBundle.getString("lblSchInstBelongs"));
        
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblBkPresented.setText(resourceBundle.getString("lblBkPresented"));
        lblBrachPres.setText(resourceBundle.getString("lblBrachPres"));
        lblInstDate.setText(resourceBundle.getString("lblInstDate"));
        lblBrPresented.setText(resourceBundle.getString("lblBrPresented"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblBankPres.setText(resourceBundle.getString("lblBankPres"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblInstAmount.setText(resourceBundle.getString("lblInstAmount"));
    }
    
    /** Setting the Mandatoriness of the certain Fields */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtInstrumentNo1", new Boolean(true));
        mandatoryMap.put("txtInstrumentNo2", new Boolean(true));
        mandatoryMap.put("txtBatchId", new Boolean(true));
        mandatoryMap.put("chkPresentAgain", new Boolean(false));
        mandatoryMap.put("cboReturnType", new Boolean(true));
        mandatoryMap.put("cboClearingType", new Boolean(true));
        mandatoryMap.put("tdtClearingDate", new Boolean(true));
    }
    
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    public void setMaxLengths(){
        txtInstrumentNo1.setMaxLength(16);
        txtInstrumentNo2.setMaxLength(16);
        txtInstrumentNo1.setAllowNumber(true);
        txtInstrumentNo2.setAllowNumber(true);
    }
    
    /** Setting the observable for the UI */
    private void setObservable() {
        try{
            observable = ReturnOfInstrumentsOB.getInstance();
            observable.addObserver(this);
        }catch(Exception e){
            
        }
    }
    
    public void update(Observable observed, Object arg) {
        lblRetId.setText(observable.getLblReturnId());
        txtInstrumentNo1.setText(observable.getTxtInstrumentNo1());
        txtInstrumentNo2.setText(observable.getTxtInstrumentNo2());
        txtBatchId.setText(observable.getTxtBatchId());
        chkPresentAgain.setSelected(observable.getChkPresentAgain());
        cboReturnType.setSelectedItem(observable.getCboReturnType());
        cboClearingType.setSelectedItem(observable.getCboClearingType());
        tdtClearingDate.setDateValue(observable.getTdtClearingDate());
    }
    
    
    /** Sets the OBFileds with the Fields filled up in the UI */
    public void updateOBFields() {
        observable.setLblReturnId(lblRetId.getText());
        observable.setTxtInstrumentNo1(txtInstrumentNo1.getText());
        observable.setTxtInstrumentNo2(txtInstrumentNo2.getText());
        observable.setTxtBatchId(txtBatchId.getText());
        observable.setChkPresentAgain(chkPresentAgain.isSelected());
        observable.setCboReturnType((String) cboReturnType.getSelectedItem());
        observable.setCboClearingType((String) cboClearingType.getSelectedItem());
        observable.setTdtClearingDate(tdtClearingDate.getDateValue());
        
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setSelectedBranchID(getSelectedBranchID());
    }
    
    /** Setting the HelpMessage for the Mandatory Fields */
    public void setHelpMessage() {
        objMandatoryRB = new ReturnOfInstrumentsMRB();
        txtInstrumentNo1.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInstrumentNo1"));
        txtInstrumentNo2.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInstrumentNo2"));
        txtBatchId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBatchId"));
        chkPresentAgain.setHelpMessage(lblMsg, objMandatoryRB.getString("chkPresentAgain"));
        cboReturnType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboReturnType"));
        cboClearingType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboClearingType"));
        tdtClearingDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtClearingDate"));
    }
    
    /** Setting the DataModel to the ComboBoxes, cboReturnType and cboClearingType */
    public void initComponentData(){
        cboReturnType.setModel(observable.getReturnTypeModel());
        cboClearingType.setModel(observable.getClearingTypeModel());
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
    
    /** Do either insertion,updation or deletion based on the ActionType */
    private void savePerformed(){
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
    }
    
    private void saveAction(String status){
        //To check mandtoryness of the Terminal panBranchDet panel and diplay appropriate
        //error message, else proceed
        StringBuffer str = new StringBuffer();
        final String mandatoryMessage = checkMandatory(panReturnsMain);
        if(mandatoryMessage.length() > 0){
            str.append(mandatoryMessage + "\n");
        }
        //__ Validation of the Instrument...
        if(txtInstrumentNo1.getText().equalsIgnoreCase("") || txtInstrumentNo2.getText().equalsIgnoreCase("")){
            str.append(resourceBundle.getString("INSTRUMENT_WARNING") + "\n");
        }else{
            String message = validateInstrumentNo();
            if(message.length() > 0){
                str.append(message + "\n");
            }
        }
        
        if(observable.getActionType()!=ClientConstants.ACTIONTYPE_DELETE && str.toString().length() > 0 ){
            displayAlert(str.toString());
        }
        else{
            observable.execute(status);
            if(observable.getActionType()!=ClientConstants.ACTIONTYPE_FAILED){
                settings();
                btnBatchNO.setEnabled(false);
            }
            
            //__ Make the Screen Closable..
            setModified(false);
        }
        
    }
    
    /** Checking the Mandotoriness of the Mandatory fields */
    private String checkMandatory(javax.swing.JComponent component){
        return new MandatoryCheck().checkMandatory(getClass().getName(), component);
    }
    
    /** Displaying the Alert Message if the Mandatory Field is empty */
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    /** Setting the Screen after the Save Action is completed */
    private void settings(){
        observable.resetForm();
        clearLabels();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panReturnsSub, false);
        setButtonEnableDisable();
        observable.setResultStatus();
    }
    
    /** Called when TextField txtBatchId looses its focus */
    private HashMap getInstrumentData(){
        HashMap wheres=new HashMap();
        wheres.put("INSRU_NO1",txtInstrumentNo1.getText());
        wheres.put("INSRU_NO2",txtInstrumentNo2.getText());
        wheres.put("BATCH_ID",txtBatchId.getText());
        wheres.put("BRANCH_ID", getSelectedBranchID());
        System.out.println("wheres : " + wheres);
        wheres = observable.getResultMap(wheres);
        System.out.println("wheres : " + wheres);
        return wheres;
    }
    
    private void setLableData(HashMap resultMap){
        lblSchInstBelongs.setText(CommonUtil.convertObjToStr(resultMap.get("SCHEDULE_NO")));
        lblBankPres.setText(CommonUtil.convertObjToStr(resultMap.get("BANK_CODE")));
        lblBrachPres.setText(CommonUtil.convertObjToStr(resultMap.get("BRANCH_CODE")));
        lblInstDate.setText(DateUtil.getStringDate((Date)resultMap.get("INSTRUMENT_DT")));
        lblInstAmount.setText(CommonUtil.convertObjToStr(resultMap.get("AMOUNT")));
    }
    
    /**  Fills up the viewAll table */
    private void callView(int currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        HashMap where = new HashMap();
        where.put("BRANCH_ID", getSelectedBranchID());
        viewMap.put(CommonConstants.MAP_WHERE, where);
        
        if (viewType==EDIT  || viewType ==DELETE || viewType == AUTHORIZE) {
            viewMap.put(CommonConstants.MAP_NAME, "viewReturnOfInstrumentData");
            
        }else{
            viewMap.put(CommonConstants.MAP_NAME, "getBatchIdData");
        }
        new ViewAll(this,viewMap).show();
    }
    
    /** Fills up all the Fields in the UI when a row in the table of ViewAll screen is selected */
    public void fillData(Object  map) {
        HashMap hash = (HashMap) map;
        
        if(viewType==EDIT  || viewType ==DELETE || viewType == AUTHORIZE) {
            isFilled = true;
            HashMap where = new HashMap();
            where.put("RETURN_ID", hash.get("RETURN ID"));
            where.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            hash.put(CommonConstants.MAP_WHERE, where);
            observable.populateData(hash);
            HashMap resultMap = getInstrumentData();
            if(resultMap != null){
                setLableData(resultMap);
            }
            
            if (observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT) {
                ClientUtil.enableDisable(panReturnsSub, true);
                btnBatchNO.setEnabled(true);
            } else if(observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE){
                ClientUtil.enableDisable(panReturnsSub, false);
                btnBatchNO.setEnabled(false);
            }
            if(viewType==AUTHORIZE){
                ClientUtil.enableDisable(panReturnsSub, false);
                btnBatchNO.setEnabled(false);
            }
            setButtonEnableDisable();
        }else{
            txtBatchId.setText(CommonUtil.convertObjToStr(hash.get("BATCH ID")));
        }
        
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }
    
    /** Clear up the Labels after Edit,Delete and save operation */
    private void clearLabels(){
        lblSchInstBelongs.setText("");
        lblBankPres.setText("");
        lblBrachPres.setText("");
        lblInstDate.setText("");
        lblInstAmount.setText("");
    }
    
    public static void main(String args[]){
        javax.swing.JFrame frame=new javax.swing.JFrame();
        ReturnOfInstrumentsUI retui=new ReturnOfInstrumentsUI();
        frame.getContentPane().add(retui);
        frame.setSize(700, 400);
        frame.show();
        retui.show();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        tbrReturns = new javax.swing.JToolBar();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace24 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace25 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace26 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace27 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace28 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panReturnsMain = new com.see.truetransact.uicomponent.CPanel();
        panReturnsSub = new com.see.truetransact.uicomponent.CPanel();
        lblReturnType = new com.see.truetransact.uicomponent.CLabel();
        lblClearingSerialNo = new com.see.truetransact.uicomponent.CLabel();
        txtInstrumentNo1 = new com.see.truetransact.uicomponent.CTextField();
        lblPresentAgain = new com.see.truetransact.uicomponent.CLabel();
        chkPresentAgain = new com.see.truetransact.uicomponent.CCheckBox();
        cboReturnType = new com.see.truetransact.uicomponent.CComboBox();
        lblClearingType = new com.see.truetransact.uicomponent.CLabel();
        cboClearingType = new com.see.truetransact.uicomponent.CComboBox();
        lblClearingDate = new com.see.truetransact.uicomponent.CLabel();
        panClearingDate = new com.see.truetransact.uicomponent.CPanel();
        tdtClearingDate = new com.see.truetransact.uicomponent.CDateField();
        lblBatchId = new com.see.truetransact.uicomponent.CLabel();
        txtBatchId = new com.see.truetransact.uicomponent.CTextField();
        lblReturnId = new com.see.truetransact.uicomponent.CLabel();
        lblRetId = new com.see.truetransact.uicomponent.CLabel();
        btnBatchNO = new com.see.truetransact.uicomponent.CButton();
        txtInstrumentNo2 = new com.see.truetransact.uicomponent.CTextField();
        panReturnsLabels = new com.see.truetransact.uicomponent.CPanel();
        lblScInstBelongs = new com.see.truetransact.uicomponent.CLabel();
        lblSchInstBelongs = new com.see.truetransact.uicomponent.CLabel();
        lblBkPresented = new com.see.truetransact.uicomponent.CLabel();
        lblBankPres = new com.see.truetransact.uicomponent.CLabel();
        lblBrPresented = new com.see.truetransact.uicomponent.CLabel();
        lblBrachPres = new com.see.truetransact.uicomponent.CLabel();
        lblInstrumentDate = new com.see.truetransact.uicomponent.CLabel();
        lblInstDate = new com.see.truetransact.uicomponent.CLabel();
        lblInstrumentAmount = new com.see.truetransact.uicomponent.CLabel();
        lblInstAmount = new com.see.truetransact.uicomponent.CLabel();
        sptVertical = new com.see.truetransact.uicomponent.CSeparator();
        mbrReturns = new com.see.truetransact.uicomponent.CMenuBar();
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
        setMinimumSize(new java.awt.Dimension(570, 350));
        setPreferredSize(new java.awt.Dimension(570, 350));

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

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrReturns.add(btnNew);

        lblSpace24.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace24.setText("     ");
        lblSpace24.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrReturns.add(lblSpace24);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrReturns.add(btnEdit);

        lblSpace25.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace25.setText("     ");
        lblSpace25.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrReturns.add(lblSpace25);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrReturns.add(btnDelete);

        lblSpace2.setText("     ");
        tbrReturns.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrReturns.add(btnSave);

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrReturns.add(lblSpace26);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrReturns.add(btnCancel);

        lblSpace3.setText("     ");
        tbrReturns.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrReturns.add(btnAuthorize);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrReturns.add(lblSpace27);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrReturns.add(btnException);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrReturns.add(lblSpace28);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrReturns.add(btnReject);

        lblSpace4.setText("     ");
        tbrReturns.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrReturns.add(btnPrint);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrReturns.add(lblSpace29);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrReturns.add(btnClose);

        getContentPane().add(tbrReturns, java.awt.BorderLayout.NORTH);

        panReturnsMain.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panReturnsMain.setMinimumSize(new java.awt.Dimension(566, 270));
        panReturnsMain.setPreferredSize(new java.awt.Dimension(568, 270));
        panReturnsMain.setLayout(new java.awt.GridBagLayout());

        panReturnsSub.setLayout(new java.awt.GridBagLayout());

        lblReturnType.setText("Return Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReturnsSub.add(lblReturnType, gridBagConstraints);

        lblClearingSerialNo.setText("Instrument No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReturnsSub.add(lblClearingSerialNo, gridBagConstraints);

        txtInstrumentNo1.setMaximumSize(new java.awt.Dimension(50, 21));
        txtInstrumentNo1.setMinimumSize(new java.awt.Dimension(50, 21));
        txtInstrumentNo1.setPreferredSize(new java.awt.Dimension(50, 21));
        txtInstrumentNo1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInstrumentNo1FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReturnsSub.add(txtInstrumentNo1, gridBagConstraints);

        lblPresentAgain.setText("Present Again");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReturnsSub.add(lblPresentAgain, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReturnsSub.add(chkPresentAgain, gridBagConstraints);

        cboReturnType.setMaximumSize(new java.awt.Dimension(100, 21));
        cboReturnType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReturnsSub.add(cboReturnType, gridBagConstraints);

        lblClearingType.setText("Clearing Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReturnsSub.add(lblClearingType, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReturnsSub.add(cboClearingType, gridBagConstraints);

        lblClearingDate.setText("Clearing Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReturnsSub.add(lblClearingDate, gridBagConstraints);

        panClearingDate.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClearingDate.add(tdtClearingDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panReturnsSub.add(panClearingDate, gridBagConstraints);

        lblBatchId.setText("BatchId");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReturnsSub.add(lblBatchId, gridBagConstraints);

        txtBatchId.setMaximumSize(new java.awt.Dimension(100, 21));
        txtBatchId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReturnsSub.add(txtBatchId, gridBagConstraints);

        lblReturnId.setText("ReturnId");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReturnsSub.add(lblReturnId, gridBagConstraints);

        lblRetId.setMaximumSize(new java.awt.Dimension(100, 21));
        lblRetId.setMinimumSize(new java.awt.Dimension(100, 21));
        lblRetId.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panReturnsSub.add(lblRetId, gridBagConstraints);

        btnBatchNO.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnBatchNO.setMaximumSize(new java.awt.Dimension(21, 21));
        btnBatchNO.setMinimumSize(new java.awt.Dimension(21, 21));
        btnBatchNO.setPreferredSize(new java.awt.Dimension(21, 21));
        btnBatchNO.setEnabled(false);
        btnBatchNO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatchNOActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panReturnsSub.add(btnBatchNO, gridBagConstraints);

        txtInstrumentNo2.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInstrumentNo2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInstrumentNo2FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        panReturnsSub.add(txtInstrumentNo2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panReturnsMain.add(panReturnsSub, gridBagConstraints);

        panReturnsLabels.setLayout(new java.awt.GridBagLayout());

        lblScInstBelongs.setText("Schedule to which Instrument Belongs");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReturnsLabels.add(lblScInstBelongs, gridBagConstraints);

        lblSchInstBelongs.setMaximumSize(new java.awt.Dimension(100, 21));
        lblSchInstBelongs.setMinimumSize(new java.awt.Dimension(100, 21));
        lblSchInstBelongs.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReturnsLabels.add(lblSchInstBelongs, gridBagConstraints);

        lblBkPresented.setText("Bank to which Presented");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReturnsLabels.add(lblBkPresented, gridBagConstraints);

        lblBankPres.setMaximumSize(new java.awt.Dimension(100, 21));
        lblBankPres.setMinimumSize(new java.awt.Dimension(100, 21));
        lblBankPres.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReturnsLabels.add(lblBankPres, gridBagConstraints);

        lblBrPresented.setText("Branch to which Presented");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReturnsLabels.add(lblBrPresented, gridBagConstraints);

        lblBrachPres.setMaximumSize(new java.awt.Dimension(100, 21));
        lblBrachPres.setMinimumSize(new java.awt.Dimension(100, 21));
        lblBrachPres.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReturnsLabels.add(lblBrachPres, gridBagConstraints);

        lblInstrumentDate.setText("Instrument Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReturnsLabels.add(lblInstrumentDate, gridBagConstraints);

        lblInstDate.setMaximumSize(new java.awt.Dimension(100, 21));
        lblInstDate.setMinimumSize(new java.awt.Dimension(100, 21));
        lblInstDate.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReturnsLabels.add(lblInstDate, gridBagConstraints);

        lblInstrumentAmount.setText("Instrument Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReturnsLabels.add(lblInstrumentAmount, gridBagConstraints);

        lblInstAmount.setMaximumSize(new java.awt.Dimension(100, 21));
        lblInstAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        lblInstAmount.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReturnsLabels.add(lblInstAmount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        panReturnsMain.add(panReturnsLabels, gridBagConstraints);

        sptVertical.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReturnsMain.add(sptVertical, gridBagConstraints);

        getContentPane().add(panReturnsMain, java.awt.BorderLayout.CENTER);

        mbrReturns.setName("mbrCustomer");

        mnuProcess.setText("Process");
        mnuProcess.setName("mnuProcess");
        mnuProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuProcessActionPerformed(evt);
            }
        });

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

        mbrReturns.add(mnuProcess);

        setJMenuBar(mbrReturns);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void btnBatchNOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatchNOActionPerformed
        // TODO add your handling code here:
        callView(BATCHNO);
    }//GEN-LAST:event_btnBatchNOActionPerformed
    
    private void txtInstrumentNo1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInstrumentNo1FocusLost
        // TODO add your handling code here:
        if(!focus){
            focus = true;
            String message = validateInstrumentNo();
            if(!message.equalsIgnoreCase("")){
                displayAlert(message);
            }
        }
        focus = false;
    }//GEN-LAST:event_txtInstrumentNo1FocusLost
    
    private void txtInstrumentNo2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInstrumentNo2FocusLost
        // TODO add your handling code here:
        if(!focus){
            focus = true;
            String message = validateInstrumentNo();
            if(!message.equalsIgnoreCase("")){
                displayAlert(message);
            }
        }
        focus = false;
    }//GEN-LAST:event_txtInstrumentNo2FocusLost
    private String validateInstrumentNo(){
        String message = "";
        //__ If the Instrument No is Not Specified, Display Alert...
        if(!txtInstrumentNo1.getText().equalsIgnoreCase("") && !txtInstrumentNo2.getText().equalsIgnoreCase("")){
            HashMap resultMap = getInstrumentData();
            if(resultMap != null){
                setLableData(resultMap);
                cboClearingType.setSelectedItem(((ComboBoxModel) cboClearingType.getModel()).getDataForKey(CommonUtil.convertObjToStr(resultMap.get("CLEARING_TYPE"))));
                tdtClearingDate.setDateValue(DateUtil.getStringDate((Date)resultMap.get("CLEARING_DT")));
            }else{
                message = resourceBundle.getString("INST_WARNING");
            }
        }
        return message;
    }
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
        authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    public void authorizeStatus(String authorizeStatus) {
        if (viewType != AUTHORIZE){
            //            viewType = AUTHORIZE;
            //            HashMap mapParam = new HashMap();
            //            HashMap where = new HashMap();
            //            where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            //            mapParam.put(CommonConstants.MAP_WHERE, where);
            //            mapParam.put(CommonConstants.MAP_NAME, "getReturnInstrumentsAuthorizeList");
            //            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeReturnInstruments");
            //
            //            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            //            authorizeUI.show();
            //            btnSave.setEnabled(false);
            //
            HashMap mapParam = new HashMap();
            
            //__ To Save the data in the Internal Frame...
            setModified(true);
            
            HashMap where = new HashMap();
            where.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            mapParam.put(CommonConstants.MAP_WHERE, where);
            mapParam.put(CommonConstants.AUTHORIZESTATUS,authorizeStatus);
            mapParam.put(CommonConstants.MAP_NAME, "getReturnInstrumentsAuthorizeList");
            viewType = AUTHORIZE;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        } else if (viewType == AUTHORIZE){
            HashMap singleAuthorizeMap = new HashMap();
            ArrayList arrList = new ArrayList();
            HashMap authDataMap = new HashMap();
            
            authDataMap.put(RETURN_ID, lblRetId.getText());
            authDataMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            authDataMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            authDataMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
            arrList.add(authDataMap);
            
            singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(singleAuthorizeMap);
        }
    }
    
    public void authorize(HashMap map) {
        System.out.println("Authorize Map : " + map);
        ///observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        observable.setAuthMap(map);
        observable.execute("");
        if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
            btnCancelActionPerformed(null);
            observable.setAuthMap(null);
            observable.setResultStatus();
        }
    }
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
        //        this.dispose();
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        btnDeleteActionPerformed(evt);
        //        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        //        callView("Delete");
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView(DELETE);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        btnCancelActionPerformed(evt);
        
        //        observable.resetForm();
        //        clearLabels();
        //        ClientUtil.clearAll(this);
        //        ClientUtil.enableDisable(panReturnsSub, false);
        //        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        //        setButtonEnableDisable();
        
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        observable.resetForm();
        clearLabels();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panReturnsSub, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        btnBatchNO.setEnabled(false);
        
        //__ Make the Screen Closable..
        setModified(false);
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        btnEditActionPerformed(evt);
        //        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        //        callView("Edit");
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView(EDIT);
        setModified(true);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        btnSaveActionPerformed(evt);
        //        savePerformed();
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        savePerformed();
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
        
        btnNewActionPerformed(evt);
        //        observable.resetForm();
        //        clearLabels();
        //        ClientUtil.enableDisable(panReturnsMain, true);
        //        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        //        setButtonEnableDisable();
        
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void mnuProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuProcessActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_mnuProcessActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        observable.resetForm();
        clearLabels();
        ClientUtil.enableDisable(panReturnsMain, true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        btnBatchNO.setEnabled(true);
        
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
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnBatchNO;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CComboBox cboClearingType;
    private com.see.truetransact.uicomponent.CComboBox cboReturnType;
    private com.see.truetransact.uicomponent.CCheckBox chkPresentAgain;
    private com.see.truetransact.uicomponent.CLabel lblBankPres;
    private com.see.truetransact.uicomponent.CLabel lblBatchId;
    private com.see.truetransact.uicomponent.CLabel lblBkPresented;
    private com.see.truetransact.uicomponent.CLabel lblBrPresented;
    private com.see.truetransact.uicomponent.CLabel lblBrachPres;
    private com.see.truetransact.uicomponent.CLabel lblClearingDate;
    private com.see.truetransact.uicomponent.CLabel lblClearingSerialNo;
    private com.see.truetransact.uicomponent.CLabel lblClearingType;
    private com.see.truetransact.uicomponent.CLabel lblInstAmount;
    private com.see.truetransact.uicomponent.CLabel lblInstDate;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentAmount;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentDate;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblPresentAgain;
    private com.see.truetransact.uicomponent.CLabel lblRetId;
    private com.see.truetransact.uicomponent.CLabel lblReturnId;
    private com.see.truetransact.uicomponent.CLabel lblReturnType;
    private com.see.truetransact.uicomponent.CLabel lblScInstBelongs;
    private com.see.truetransact.uicomponent.CLabel lblSchInstBelongs;
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
    private com.see.truetransact.uicomponent.CMenuBar mbrReturns;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panClearingDate;
    private com.see.truetransact.uicomponent.CPanel panReturnsLabels;
    private com.see.truetransact.uicomponent.CPanel panReturnsMain;
    private com.see.truetransact.uicomponent.CPanel panReturnsSub;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CSeparator sptVertical;
    private javax.swing.JToolBar tbrReturns;
    private com.see.truetransact.uicomponent.CDateField tdtClearingDate;
    private com.see.truetransact.uicomponent.CTextField txtBatchId;
    private com.see.truetransact.uicomponent.CTextField txtInstrumentNo1;
    private com.see.truetransact.uicomponent.CTextField txtInstrumentNo2;
    // End of variables declaration//GEN-END:variables
    
}
