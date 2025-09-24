/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RelationshipsUI.java
 *
 * Created on July 15, 2004, 3:29 PM
 */

package com.see.truetransact.ui.privatebanking.comlogs.relationships;

/**
 *
 * @author Ashok
 */

import java.util.HashMap;
import java.util.Observer;
import java.util.Observable;

import com.see.truetransact.ui.privatebanking.comlogs.relationships.RelationshipsRB;
import com.see.truetransact.ui.privatebanking.comlogs.relationships.RelationshipsOB;
import com.see.truetransact.ui.privatebanking.comlogs.relationships.RelationshipsMRB;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.TrueTransactMain;

public class RelationshipsUI extends com.see.truetransact.uicomponent.CInternalFrame implements UIMandatoryField,Observer {
    
    private RelationshipsRB resourceBundle = new RelationshipsRB();
    private RelationshipsMRB objMandatoryRB;
    private HashMap mandatoryMap;
    private RelationshipsOB observable;
    private String viewType = "";
    private final String MEMBER="ORD_ID";
    private final String EMPID = "EMPLOYEE_CODE";
    private String authorizeType = "";
    private final String AUTHORIZE = "Authorize";
    
    /** Creates new form RelationshipsUI */
    public RelationshipsUI() {
        initGUI();
    }
    
    /** Initialises the GUI */
    private void initGUI(){
        initComponents();
        setFieldNames();
        setObservable();
        internationalize();
        setMandatoryHashMap();
        setHelpMessage();
        setMaximumLengths();
        observable.resetForm();
        clearLabels();
        initComponentData();
        ClientUtil.enableDisable(panRelationships,false);
        setButtonEnableDisable();
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
        btnMember.setName("btnMember");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        txtInitiatedBy.setName("txtInitiatedBy");
        cboSource.setName("cboSource");
        cboSubType.setName("cboSubType");
        cboType.setName("cboType");
        lblBankerName.setName("lblBankerName");
        lblContactDate.setName("lblContactDate");
        lblContactDescription.setName("lblContactDescription");
        lblInitiatedBy.setName("lblInitiatedBy");
        lblLeadRSO.setName("lblLeadRSO");
        lblMember.setName("lblMember");
        txtMember.setName("txtMember");
        lblMsg.setName("lblMsg");
        lblRelationshipId.setName("lblRelationship");
        lblRelationshipIdValue.setName("lblRelationshipValue");
        lblRelationship.setName("lblRelationship");
        lblRelationshipValue.setName("lblRelationshipValue");
        lblSource.setName("lblSource");
        lblSourceReference.setName("lblSourceReference");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblStatus.setName("lblStatus");
        lblSubType.setName("lblSubType");
        lblType.setName("lblType");
        mbrRelationships.setName("mbrRelationships");
        panMember.setName("panMember");
        panRelationships.setName("panRelationships");
        panStatus.setName("panStatus");
        srpContactDescription.setName("srpContactDescription");
        tdtContactDate.setName("tdtContactDate");
        txaContactDescription.setName("txaContactDescription");
        txtBankerName.setName("txtBankerName");
        txtLeadRSO.setName("txtLeadRSO");
        txtSourceReference.setName("txtSourceReference");
    }
    
    /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblContactDate.setText(resourceBundle.getString("lblContactDate"));
        lblRelationshipId.setText(resourceBundle.getString("lblRelationshipId"));
        lblRelationship.setText(resourceBundle.getString("lblRelationship"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        btnMember.setText(resourceBundle.getString("btnMember"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblSourceReference.setText(resourceBundle.getString("lblSourceReference"));
        lblLeadRSO.setText(resourceBundle.getString("lblLeadRSO"));
        lblType.setText(resourceBundle.getString("lblType"));
        lblMember.setText(resourceBundle.getString("lblMember"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblRelationshipIdValue.setText(resourceBundle.getString("lblRelationshipIdValue"));
        lblRelationshipValue.setText(resourceBundle.getString("lblRelationshipValue"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblInitiatedBy.setText(resourceBundle.getString("lblInitiatedBy"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblSource.setText(resourceBundle.getString("lblSource"));
        lblContactDescription.setText(resourceBundle.getString("lblContactDescription"));
        lblSubType.setText(resourceBundle.getString("lblSubType"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblBankerName.setText(resourceBundle.getString("lblBankerName"));
    }
    
    
/* Auto Generated Method - setMandatoryHashMap()
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtMember", new Boolean(true));
        mandatoryMap.put("txtBankerName", new Boolean(true));
        mandatoryMap.put("txtInitiatedBy", new Boolean(true));
        mandatoryMap.put("txtLeadRSO", new Boolean(true));
        mandatoryMap.put("txaContactDescription", new Boolean(true));
        mandatoryMap.put("cboType", new Boolean(true));
        mandatoryMap.put("cboSource", new Boolean(true));
        mandatoryMap.put("txtSourceReference", new Boolean(true));
        mandatoryMap.put("tdtContactDate", new Boolean(true));
        mandatoryMap.put("cboSubType", new Boolean(true));
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
        objMandatoryRB = new RelationshipsMRB();
        txtMember.setHelpMessage(lblMsg,objMandatoryRB.getString("txtMember"));
        txtBankerName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBankerName"));
        txtInitiatedBy.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInitiatedBy"));
        txtLeadRSO.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLeadRSO"));
        txaContactDescription.setHelpMessage(lblMsg, objMandatoryRB.getString("txaContactDescription"));
        cboType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboType"));
        cboSource.setHelpMessage(lblMsg, objMandatoryRB.getString("cboSource"));
        txtSourceReference.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSourceReference"));
        tdtContactDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtContactDate"));
        cboSubType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboSubType"));
    }
    
    /** Setting the observable for RelationshipsUI */
    private void setObservable(){
        try{
            observable = RelationshipsOB.getInstance();
            observable.addObserver(this);
        }catch(Exception e){
        }
    }
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        lblRelationshipIdValue.setText(observable.getLblRelationshipId());
        txtMember.setText(observable.getTxtMember());
        txtBankerName.setText(observable.getTxtBankerName());
        txtInitiatedBy.setText(observable.getTxtInitiatedBy());
        txtLeadRSO.setText(observable.getTxtLeadRSO());
        txaContactDescription.setText(observable.getTxaContactDescription());
        cboType.setSelectedItem(observable.getCboType());
        cboSource.setSelectedItem(observable.getCboSource());
        txtSourceReference.setText(observable.getTxtSourceReference());
        tdtContactDate.setDateValue(observable.getTdtContactDate());
        cboSubType.setSelectedItem(observable.getCboSubType());
    }
    
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setLblRelationshipId(lblRelationshipIdValue.getText());
        observable.setTxtMember(txtMember.getText());
        observable.setTxtBankerName(txtBankerName.getText());
        observable.setTxtInitiatedBy((String) txtInitiatedBy.getText());
        observable.setTxtLeadRSO(txtLeadRSO.getText());
        observable.setTxaContactDescription(txaContactDescription.getText());
        observable.setCboType((String) cboType.getSelectedItem());
        observable.setCboSource((String) cboSource.getSelectedItem());
        observable.setTxtSourceReference(txtSourceReference.getText());
        observable.setTdtContactDate(tdtContactDate.getDateValue());
        observable.setCboSubType((String) cboSubType.getSelectedItem());
    }
    
    /** Sets the Maximum allowed lenght to the Textfields in the ui */
    private void setMaximumLengths(){
        txtMember.setMaxLength(32);
        txtBankerName.setMaxLength(64);
        txtInitiatedBy.setMaxLength(32);
        txtLeadRSO.setMaxLength(32);
        txaContactDescription.setTabSize(1024);
        txtSourceReference.setMaxLength(32);
    }
    
    /** Sets the model for the Comboboxes in the UI */
    private void initComponentData(){
        cboType.setModel(observable.getCbmType());
        cboSource.setModel(observable.getCbmSource());
        cboSubType.setModel(observable.getCbmSubType());
    }
    
    /** Clears the Labels in the ui */
    private void clearLabels(){
        lblRelationshipIdValue.setText("");
        lblRelationshipValue.setText("");
    }
    
    /** Enables or Disables the Button */
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
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE){
            btnMember.setEnabled(btnNew.isEnabled());
            btnInitiatedBy.setEnabled(btnNew.isEnabled());
            btnLeadRSO.setEnabled(btnNew.isEnabled());
        } else {
            btnMember.setEnabled(!btnNew.isEnabled());
            btnInitiatedBy.setEnabled(!btnNew.isEnabled());
            btnLeadRSO.setEnabled(!btnNew.isEnabled());
        }
        if(observable.getLblStatus().equals(ClientConstants.RESULT_STATUS[2]) || observable.getLblStatus().equals(ClientConstants.RESULT_STATUS[3]) || observable.getLblStatus().equals(ClientConstants.RESULT_STATUS[1])){
            btnMember.setEnabled(!btnClose.isEnabled());
            btnInitiatedBy.setEnabled(!btnClose.isEnabled());
            btnLeadRSO.setEnabled(!btnClose.isEnabled());
        }
    }
    
    /** This method shows the ViewAll Screen with the rows filled up according to query executed */
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        if (currField.equals(ClientConstants.ACTION_STATUS[2])  ||
        currField.equals(ClientConstants.ACTION_STATUS[3])) {
            viewMap.put(CommonConstants.MAP_NAME, "getSelectPvtRelationships");
        } else if(currField.equals("Member")){
            viewMap.put(CommonConstants.MAP_NAME, "getSelectPvtOrderMaster");
        }else{
            viewMap.put(CommonConstants.MAP_NAME, "getSelectBankEmployee");
        }
        new ViewAll(this,viewMap).show();
        viewMap = null;
    }
    
    /** This method fill up the uifields with row seleced in the view all Screen */
    public void fillData(Object  map) {
        try{
            HashMap hash = (HashMap) map;
            if (viewType != null) {
                if (viewType.equals(ClientConstants.ACTION_STATUS[2]) ||
                viewType.equals(ClientConstants.ACTION_STATUS[3]) ) {
                    hash.put(CommonConstants.MAP_WHERE, hash.get("RELATE_ID"));
                    observable.populateData(hash);
                    hash = null;
                    if (viewType.equals(ClientConstants.ACTION_STATUS[3])) {
                        ClientUtil.enableDisable(panRelationships, false);
                    } else {
                        ClientUtil.enableDisable(panRelationships, true);
                    }
                    setButtonEnableDisable();
                }else{
                    if(viewType.equals("Member")){
                        txtMember.setText( CommonUtil.convertObjToStr(hash.get(MEMBER)));
                    }else if(viewType.equals("InitiatedBy")){
                        txtInitiatedBy.setText( CommonUtil.convertObjToStr(hash.get(EMPID)));
                    }else{
                        txtLeadRSO.setText( CommonUtil.convertObjToStr(hash.get(EMPID)));
                    }
                }
                String where = txtMember.getText();
                HashMap lblMap = observable.getLabelMap(where);
                lblRelationshipValue.setText(CommonUtil.convertObjToStr(lblMap.get("MEMBER RELATION")));
                lblMap = null;
            }
        }catch(Exception e){
        }
    }
    
    /** This checks the mandatoriness of the UI fields and return the mandaory message
     *if mandatoriness is not satisfied */
    private String checkMandatory(javax.swing.JComponent component){
        return new MandatoryCheck().checkMandatory(getClass().getName(), component);
    }
    
    /** Displays the AlertMessage */
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    /* Calls the execute method of RelationshipsOB to do insertion or updation or deletion */
    private void saveAction(String status){
        
        final String mandatoryMessage = checkMandatory(panRelationships);
        if(mandatoryMessage.length() > 0 ){
            displayAlert(mandatoryMessage);
        }
        else{
            observable.execute(status);
            settings();
        }
    }
    
    /* set the screen after the updation,insertion, deletion */
    private void settings(){
        observable.resetForm();
        clearLabels();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panRelationships, false);
        setButtonEnableDisable();
        observable.setResultStatus();
    }
    
    /* Does necessary operaion when user clicks the save button */
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
    
    /** Called to do authorize operations like Authorize,Reject,Exception */
    public void authorizeStatus(String authorizeStatus) {
        if (!authorizeType.equals(AUTHORIZE)){
            authorizeType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "getRelationshipsAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeRelationships");
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            mapParam = null;
            authorizeUI.show();
            btnSave.setEnabled(false);
        } else if (authorizeType.equals(AUTHORIZE)){
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("USER_ID", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("RELATE_ID", lblRelationshipIdValue.getText());
            
            ClientUtil.execute("authorizeRelationships", singleAuthorizeMap);
            singleAuthorizeMap = null;
            authorizeType = "";
            btnCancelActionPerformed(null);
        }
    }
    
    public static void main(String args[]){
        javax.swing.JFrame frame=new javax.swing.JFrame();
        RelationshipsUI  tui = new RelationshipsUI();
        frame.getContentPane().add(tui);
        frame.setSize(480,400);
        frame.show();
        tui.show();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panRelationships = new com.see.truetransact.uicomponent.CPanel();
        lblMember = new com.see.truetransact.uicomponent.CLabel();
        panMember = new com.see.truetransact.uicomponent.CPanel();
        btnMember = new com.see.truetransact.uicomponent.CButton();
        txtMember = new com.see.truetransact.uicomponent.CTextField();
        lblRelationship = new com.see.truetransact.uicomponent.CLabel();
        lblRelationshipValue = new com.see.truetransact.uicomponent.CLabel();
        lblBankerName = new com.see.truetransact.uicomponent.CLabel();
        txtBankerName = new com.see.truetransact.uicomponent.CTextField();
        lblInitiatedBy = new com.see.truetransact.uicomponent.CLabel();
        lblLeadRSO = new com.see.truetransact.uicomponent.CLabel();
        lblContactDescription = new com.see.truetransact.uicomponent.CLabel();
        srpContactDescription = new com.see.truetransact.uicomponent.CScrollPane();
        txaContactDescription = new com.see.truetransact.uicomponent.CTextArea();
        lblType = new com.see.truetransact.uicomponent.CLabel();
        cboType = new com.see.truetransact.uicomponent.CComboBox();
        lblSource = new com.see.truetransact.uicomponent.CLabel();
        cboSource = new com.see.truetransact.uicomponent.CComboBox();
        lblSourceReference = new com.see.truetransact.uicomponent.CLabel();
        txtSourceReference = new com.see.truetransact.uicomponent.CTextField();
        lblContactDate = new com.see.truetransact.uicomponent.CLabel();
        tdtContactDate = new com.see.truetransact.uicomponent.CDateField();
        lblSubType = new com.see.truetransact.uicomponent.CLabel();
        cboSubType = new com.see.truetransact.uicomponent.CComboBox();
        panInitiatedBy = new com.see.truetransact.uicomponent.CPanel();
        txtInitiatedBy = new com.see.truetransact.uicomponent.CTextField();
        btnInitiatedBy = new com.see.truetransact.uicomponent.CButton();
        panLeadRSO = new com.see.truetransact.uicomponent.CPanel();
        txtLeadRSO = new com.see.truetransact.uicomponent.CTextField();
        btnLeadRSO = new com.see.truetransact.uicomponent.CButton();
        lblRelationshipId = new com.see.truetransact.uicomponent.CLabel();
        lblRelationshipIdValue = new com.see.truetransact.uicomponent.CLabel();
        tbrRelationships = new javax.swing.JToolBar();
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
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrRelationships = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptNew = new javax.swing.JSeparator();
        sptEdit = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptSave = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(450, 350));
        setPreferredSize(new java.awt.Dimension(450, 350));

        panRelationships.setLayout(new java.awt.GridBagLayout());

        lblMember.setText("Member");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRelationships.add(lblMember, gridBagConstraints);

        panMember.setLayout(new java.awt.GridBagLayout());

        btnMember.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnMember.setMaximumSize(new java.awt.Dimension(21, 21));
        btnMember.setMinimumSize(new java.awt.Dimension(21, 21));
        btnMember.setPreferredSize(new java.awt.Dimension(21, 21));
        btnMember.setEnabled(false);
        btnMember.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMemberActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMember.add(btnMember, gridBagConstraints);

        txtMember.setEditable(false);
        txtMember.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMember.add(txtMember, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        panRelationships.add(panMember, gridBagConstraints);

        lblRelationship.setText("Relationship");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 4, 4);
        panRelationships.add(lblRelationship, gridBagConstraints);

        lblRelationshipValue.setMaximumSize(new java.awt.Dimension(100, 21));
        lblRelationshipValue.setMinimumSize(new java.awt.Dimension(100, 21));
        lblRelationshipValue.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 4, 4);
        panRelationships.add(lblRelationshipValue, gridBagConstraints);

        lblBankerName.setText("Banker Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRelationships.add(lblBankerName, gridBagConstraints);

        txtBankerName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRelationships.add(txtBankerName, gridBagConstraints);

        lblInitiatedBy.setText("Initiated By");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRelationships.add(lblInitiatedBy, gridBagConstraints);

        lblLeadRSO.setText("Lead RSO");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRelationships.add(lblLeadRSO, gridBagConstraints);

        lblContactDescription.setText("Contact Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRelationships.add(lblContactDescription, gridBagConstraints);

        srpContactDescription.setMinimumSize(new java.awt.Dimension(24, 48));
        srpContactDescription.setPreferredSize(new java.awt.Dimension(4, 48));
        srpContactDescription.setViewportView(txaContactDescription);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panRelationships.add(srpContactDescription, gridBagConstraints);

        lblType.setText("Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRelationships.add(lblType, gridBagConstraints);

        cboType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRelationships.add(cboType, gridBagConstraints);

        lblSource.setText("Source");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRelationships.add(lblSource, gridBagConstraints);

        cboSource.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRelationships.add(cboSource, gridBagConstraints);

        lblSourceReference.setText("Source Reference");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRelationships.add(lblSourceReference, gridBagConstraints);

        txtSourceReference.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRelationships.add(txtSourceReference, gridBagConstraints);

        lblContactDate.setText("Contact Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRelationships.add(lblContactDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRelationships.add(tdtContactDate, gridBagConstraints);

        lblSubType.setText("Sub- Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 8, 4);
        panRelationships.add(lblSubType, gridBagConstraints);

        cboSubType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 8, 4);
        panRelationships.add(cboSubType, gridBagConstraints);

        panInitiatedBy.setLayout(new java.awt.GridBagLayout());

        txtInitiatedBy.setEditable(false);
        txtInitiatedBy.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInitiatedBy.add(txtInitiatedBy, gridBagConstraints);

        btnInitiatedBy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnInitiatedBy.setMaximumSize(new java.awt.Dimension(21, 21));
        btnInitiatedBy.setMinimumSize(new java.awt.Dimension(21, 21));
        btnInitiatedBy.setPreferredSize(new java.awt.Dimension(21, 21));
        btnInitiatedBy.setEnabled(false);
        btnInitiatedBy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInitiatedByActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInitiatedBy.add(btnInitiatedBy, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRelationships.add(panInitiatedBy, gridBagConstraints);

        panLeadRSO.setLayout(new java.awt.GridBagLayout());

        txtLeadRSO.setEditable(false);
        txtLeadRSO.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeadRSO.add(txtLeadRSO, gridBagConstraints);

        btnLeadRSO.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnLeadRSO.setMaximumSize(new java.awt.Dimension(21, 21));
        btnLeadRSO.setMinimumSize(new java.awt.Dimension(21, 21));
        btnLeadRSO.setPreferredSize(new java.awt.Dimension(21, 21));
        btnLeadRSO.setEnabled(false);
        btnLeadRSO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLeadRSOActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panLeadRSO.add(btnLeadRSO, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRelationships.add(panLeadRSO, gridBagConstraints);

        lblRelationshipId.setText("Relationship Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 4, 4);
        panRelationships.add(lblRelationshipId, gridBagConstraints);

        lblRelationshipIdValue.setMaximumSize(new java.awt.Dimension(100, 21));
        lblRelationshipIdValue.setMinimumSize(new java.awt.Dimension(100, 21));
        lblRelationshipIdValue.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 4, 4);
        panRelationships.add(lblRelationshipIdValue, gridBagConstraints);

        getContentPane().add(panRelationships, java.awt.BorderLayout.CENTER);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrRelationships.add(btnNew);

        lblSpace24.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace24.setText("     ");
        lblSpace24.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrRelationships.add(lblSpace24);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrRelationships.add(btnEdit);

        lblSpace25.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace25.setText("     ");
        lblSpace25.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrRelationships.add(lblSpace25);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrRelationships.add(btnDelete);

        lblSpace2.setText("     ");
        tbrRelationships.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrRelationships.add(btnSave);

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrRelationships.add(lblSpace26);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrRelationships.add(btnCancel);

        lblSpace3.setText("     ");
        tbrRelationships.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrRelationships.add(btnAuthorize);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrRelationships.add(lblSpace27);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrRelationships.add(btnException);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrRelationships.add(lblSpace28);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrRelationships.add(btnReject);

        lblSpace4.setText("     ");
        tbrRelationships.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrRelationships.add(btnPrint);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrRelationships.add(lblSpace29);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrRelationships.add(btnClose);

        getContentPane().add(tbrRelationships, java.awt.BorderLayout.NORTH);

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

        mbrRelationships.setName("mbrCustomer");

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

        sptEdit.setName("sptNew");
        mnuProcess.add(sptEdit);

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

        mbrRelationships.add(mnuProcess);

        setJMenuBar(mbrRelationships);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // TODO add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
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
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView("Delete");
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView("Edit");
        
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        savePerformed();
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void btnLeadRSOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLeadRSOActionPerformed
        // TODO add your handling code here:
        callView("LeadRSO");
    }//GEN-LAST:event_btnLeadRSOActionPerformed
    
    private void btnInitiatedByActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInitiatedByActionPerformed
        // TODO add your handling code here:
        callView("InitiatedBy");
    }//GEN-LAST:event_btnInitiatedByActionPerformed
    
    private void btnMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMemberActionPerformed
        // TODO add your handling code here:
        callView("Member");
    }//GEN-LAST:event_btnMemberActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        observable.resetForm();
        clearLabels();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panRelationships, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        observable.resetForm();
        clearLabels();
        ClientUtil.enableDisable(panRelationships, true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
    }//GEN-LAST:event_btnNewActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnInitiatedBy;
    private com.see.truetransact.uicomponent.CButton btnLeadRSO;
    private com.see.truetransact.uicomponent.CButton btnMember;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CComboBox cboSource;
    private com.see.truetransact.uicomponent.CComboBox cboSubType;
    private com.see.truetransact.uicomponent.CComboBox cboType;
    private com.see.truetransact.uicomponent.CLabel lblBankerName;
    private com.see.truetransact.uicomponent.CLabel lblContactDate;
    private com.see.truetransact.uicomponent.CLabel lblContactDescription;
    private com.see.truetransact.uicomponent.CLabel lblInitiatedBy;
    private com.see.truetransact.uicomponent.CLabel lblLeadRSO;
    private com.see.truetransact.uicomponent.CLabel lblMember;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblRelationship;
    private com.see.truetransact.uicomponent.CLabel lblRelationshipId;
    private com.see.truetransact.uicomponent.CLabel lblRelationshipIdValue;
    private com.see.truetransact.uicomponent.CLabel lblRelationshipValue;
    private com.see.truetransact.uicomponent.CLabel lblSource;
    private com.see.truetransact.uicomponent.CLabel lblSourceReference;
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
    private com.see.truetransact.uicomponent.CLabel lblSubType;
    private com.see.truetransact.uicomponent.CLabel lblType;
    private com.see.truetransact.uicomponent.CMenuBar mbrRelationships;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panInitiatedBy;
    private com.see.truetransact.uicomponent.CPanel panLeadRSO;
    private com.see.truetransact.uicomponent.CPanel panMember;
    private com.see.truetransact.uicomponent.CPanel panRelationships;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptEdit;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpContactDescription;
    private javax.swing.JToolBar tbrRelationships;
    private com.see.truetransact.uicomponent.CDateField tdtContactDate;
    private com.see.truetransact.uicomponent.CTextArea txaContactDescription;
    private com.see.truetransact.uicomponent.CTextField txtBankerName;
    private com.see.truetransact.uicomponent.CTextField txtInitiatedBy;
    private com.see.truetransact.uicomponent.CTextField txtLeadRSO;
    private com.see.truetransact.uicomponent.CTextField txtMember;
    private com.see.truetransact.uicomponent.CTextField txtSourceReference;
    // End of variables declaration//GEN-END:variables
    
}
