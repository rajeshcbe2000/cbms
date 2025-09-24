/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * GLEntryUI.java
 *
 * Created on January 3, 2005, 5:04 PM
 */

package com.see.truetransact.ui.generalledger.glentry;

/**
 *
 * @author  ashokvijayakumar
 */

import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.ui.generalledger.glentry.GLEntryRB;
import com.see.truetransact.ui.generalledger.glentry.GLEntryOB;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;

import java.util.Observable;
import java.util.Observer;
import java.util.HashMap;
import javax.swing.JComponent;
import java.util.ArrayList;
import java.util.ResourceBundle;
import com.see.truetransact.clientproxy.ProxyParameters;
import java.util.Date;
public class GLEntryUI extends CInternalFrame implements Observer,UIMandatoryField {
    
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("com.see.truetransact.ui.generalledger.glentry.GLEntryRB", ProxyParameters.LANGUAGE);
    private GLEntryOB observable;
    private HashMap mandatoryMap;
    private String viewType = "";
    private boolean isFilled = false;
    private final String AUTHORIZE = "Authorize";
    private Date currDt = null;
    /** Creates new form GLEntryUI */
    public GLEntryUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setObservable();
        initComponentData();
        setMaxLengths();
        observable.resetForm();
        txtAmount.setValidation(new CurrencyValidation(14,2));
        setButtonEnableDisable();
        ClientUtil.enableDisable(panGLEntry,false);
    }
    
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        btnSave.setName("btnSave");
        lblAcDesc.setName("lblAcDesc");
        lblAcHead.setName("lblAcHead");
        lblAmount.setName("lblAmount");
        lblDescription.setName("lblDescription");
        panGLEntry.setName("panGLEntry");
        txtAcHead.setName("txtAcHead");
        txtAmount.setName("txtAmount");
        lblAccountHeadStatus.setName("lblAccountHeadStatus");
        cboAccountHeadStatus.setName("cboAccountHeadStatus");
    }
    
    /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        lblAcDesc.setText(resourceBundle.getString("lblAcDesc"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblDescription.setText(resourceBundle.getString("lblDescription"));
        lblAcHead.setText(resourceBundle.getString("lblAcHead"));
        lblAmount.setText(resourceBundle.getString("lblAmount"));
        lblAccountHeadStatus.setText(resourceBundle.getString("lblAccountHeadStatus"));
    }
    
    /** Set observable */
    private void setObservable() {
        observable = GLEntryOB .getInstance();
        observable.addObserver(this);
    }
    
    private void initComponentData() {
        cboAccountHeadStatus.setModel(observable.getCbmAccountHeadStatus());
    }
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        txtAmount.setText(observable.getTxtAmount());
        txtAcHead.setText(observable.getTxtAcHead());
        lblDescription.setText(observable.getLblDescription());
        lblStatus.setText(observable.getLblStatus());
        cboAccountHeadStatus.setSelectedItem(observable.getCboAccountHeadStatus());
    }
    
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtAmount(txtAmount.getText());
        observable.setTxtAcHead(txtAcHead.getText());
        observable.setCboAccountHeadStatus(CommonUtil.convertObjToStr(cboAccountHeadStatus.getSelectedItem()));
    }
    
    
/* Auto Generated Method - setMandatoryHashMap()
 
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtGLBalance", new Boolean(true));
        mandatoryMap.put("txtAmount", new Boolean(true));
        mandatoryMap.put("txtAcHead", new Boolean(true));
        mandatoryMap.put("cboAccountHeadStatus", new Boolean(true));
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    private void setMaxLengths(){
        txtAmount.setMaxLength(16);
    }
    
    /** Fills up the UI fields when an row is selected in the Popup window */
    public void fillData(Object  map) {
        HashMap hash = (HashMap) map;
        setModified(true);
        if(viewType.equals(ClientConstants.ACTION_STATUS[1])){
            observable.setTxtAcHead(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
            observable.setLblDescription(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
            fillCboAccountHeadStatus(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
            observable.notifyObservers();
        }else if(viewType.equals(ClientConstants.ACTION_STATUS[2]) || viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE)){
            isFilled = true;
            observable.setTxtAcHead(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
            observable.setLblDescription(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
            fillCboAccountHeadStatus(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
            observable.notifyObservers();
        }
        if(viewType.equals(AUTHORIZE)){
            setModified(false);
            ClientUtil.enableDisable(panGLEntry,false);
        }
        setButtonEnableDisable();
    }
    
    public static void main(String args[]){
        GLEntryUI ui = new GLEntryUI();
        javax.swing.JFrame frame = new javax.swing.JFrame();
        frame.setVisible(true);
        frame.getContentPane().add(ui);
        ui.setVisible(true);
        ui.show();
        frame.setSize(400, 500);
        frame.show();
    }
    
    /** Check Whether the Mandatory Fields are filled up */
    private String checkMandatory(JComponent component){
        return new MandatoryCheck().checkMandatory(getClass().getName(),component);
    }
    
    /** Displays an Alert message when user fails to enter data in the mandatoryfields */
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
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
    
    private void saveAction(String status){
        //To check mandtoryness of the Terminal panAcHdDetails,panAcHeadDetails panel and diplay appropriate
        //error message, else proceed
        try{
            final String mandatoryMessage = checkMandatory(panGLEntry);
            if(mandatoryMessage.length() > 0 && observable.getActionType()!= ClientConstants.ACTIONTYPE_DELETE){
                displayAlert(mandatoryMessage);
            }else{
                observable.doSave(status);
                observable.setResultStatus();
                if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
                    ClientUtil.clearAll(this);
                    observable.resetForm();
                    ClientUtil.enableDisable(panGLEntry, false);
                    observable.setResultStatus();
                    setButtonEnableDisable();
                }
            }
        }catch(Exception e){
            
        }
    }
    
    
    /** Method used to fillthe dropdown cboAccountHeadStatus selected AcHdId's Implementation Status **/
    private void fillCboAccountHeadStatus(String acHdId){
        String actStatus = "";
        double credit = 0;
        double debit = 0;
        HashMap resultMap = new HashMap();
        HashMap where = new HashMap();
        where.put("AC_HD_ID",acHdId);
        where.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        ArrayList resultList =(ArrayList) ClientUtil.executeQuery("getSelectImplStatus",where);
        if(resultList.size() != 0){
            resultMap =(HashMap) resultList.get(0);
            if(resultMap!= null){
                actStatus = CommonUtil.convertObjToStr(resultMap.get("IMPL_STATUS"));
                if(resultMap.get("SHADOW_CREDIT")!=null){
                    if(!resultMap.get("SHADOW_CREDIT").equals("")){
                        credit = Double.parseDouble(CommonUtil.convertObjToStr(resultMap.get("SHADOW_CREDIT")));
                    }
                }
                if(resultMap.get("SHADOW_DEBIT")!=null){
                    if(!resultMap.get("SHADOW_DEBIT").equals("")){
                        debit = Double.parseDouble(CommonUtil.convertObjToStr(resultMap.get("SHADOW_DEBIT")));
                    }
                }
            }
            observable.setTxtAmount(String.valueOf(credit+debit));
            observable.setCboAccountHeadStatus(CommonUtil.convertObjToStr(observable.getCbmAccountHeadStatus().getDataForKey(actStatus)));
        }
    }
    
    private void callView(String viewType){
        HashMap viewMap = new HashMap();
        this.viewType = viewType;
        if(viewType.equals(ClientConstants.ACTION_STATUS[1])){
            viewMap.put(CommonConstants.MAP_NAME, "getSelectAcHdParam");
            HashMap where = new HashMap();
            where.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, where);
        }else if(viewType.equals(ClientConstants.ACTION_STATUS[2]) || viewType.equals(ClientConstants.ACTION_STATUS[3])){
            HashMap where = new HashMap();
            where.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_NAME, "getSelectGL");
            viewMap.put(CommonConstants.MAP_WHERE, where);
            where = null;
        }
        new ViewAll(this, viewMap).show();
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
    }
    
    /* Does the Authorization thru DeathMarkingDA0 class **/
    public void authorize(HashMap map) {
        map.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
        map.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        observable.setAuthorizeMap(map);
        observable.doSave(AUTHORIZE);
        btnCancelActionPerformed(null);
    }
    
    /** Does the authorization for the row selected in the AuthorizationUI screen */
    public void authorizeStatus(String authorizeStatus) {
        viewType = AUTHORIZE;
        if (!isFilled ){
            HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "getGLAuthorizeList");
            HashMap where = new HashMap();
            where.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            where.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, where);
            where = null;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            setModified(true);
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        } else if (isFilled){
            isFilled = false;
            HashMap singleAuthorizeMap = new HashMap();
            ArrayList arrList = new ArrayList();
            HashMap authDataMap = new HashMap();
            authDataMap.put(CommonConstants.BRANCH_ID,TrueTransactMain.BRANCH_ID);
            authDataMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            authDataMap.put(CommonConstants.AUTHORIZEDT,currDt.clone());
            authDataMap.put("AC_HD_ID", txtAcHead.getText());
            arrList.add(authDataMap);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(singleAuthorizeMap);
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

        panGLEntry = new com.see.truetransact.uicomponent.CPanel();
        lblAcHead = new com.see.truetransact.uicomponent.CLabel();
        lblAcDesc = new com.see.truetransact.uicomponent.CLabel();
        lblAmount = new com.see.truetransact.uicomponent.CLabel();
        lblDescription = new com.see.truetransact.uicomponent.CLabel();
        txtAmount = new com.see.truetransact.uicomponent.CTextField();
        panDescription = new com.see.truetransact.uicomponent.CPanel();
        txtAcHead = new com.see.truetransact.uicomponent.CTextField();
        lblAccountHeadStatus = new com.see.truetransact.uicomponent.CLabel();
        cboAccountHeadStatus = new com.see.truetransact.uicomponent.CComboBox();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        tbrGLEntry = new com.see.truetransact.uicomponent.CToolBar();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace26 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace27 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lbSpace2 = new com.see.truetransact.uicomponent.CLabel();
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
        mbrGLEntry = new com.see.truetransact.uicomponent.CMenuBar();
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
        setMinimumSize(new java.awt.Dimension(18, 27));
        setPreferredSize(new java.awt.Dimension(350, 250));

        panGLEntry.setLayout(new java.awt.GridBagLayout());

        lblAcHead.setText("Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGLEntry.add(lblAcHead, gridBagConstraints);

        lblAcDesc.setText("Account Head Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGLEntry.add(lblAcDesc, gridBagConstraints);

        lblAmount.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGLEntry.add(lblAmount, gridBagConstraints);

        lblDescription.setMinimumSize(new java.awt.Dimension(100, 21));
        lblDescription.setPreferredSize(new java.awt.Dimension(200, 21));
        lblDescription.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                lblDescriptionFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGLEntry.add(lblDescription, gridBagConstraints);

        txtAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAmount.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGLEntry.add(txtAmount, gridBagConstraints);

        panDescription.setLayout(new java.awt.GridBagLayout());

        txtAcHead.setEditable(false);
        txtAcHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAcHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAcHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panDescription.add(txtAcHead, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panGLEntry.add(panDescription, gridBagConstraints);

        lblAccountHeadStatus.setText("Account Head Status");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGLEntry.add(lblAccountHeadStatus, gridBagConstraints);

        cboAccountHeadStatus.setMinimumSize(new java.awt.Dimension(100, 21));
        cboAccountHeadStatus.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cboAccountHeadStatusFocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGLEntry.add(cboAccountHeadStatus, gridBagConstraints);

        getContentPane().add(panGLEntry, java.awt.BorderLayout.CENTER);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace, gridBagConstraints);

        lblStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus.setText("                      ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblStatus, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
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
        tbrGLEntry.add(btnNew);

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrGLEntry.add(lblSpace26);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrGLEntry.add(btnEdit);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrGLEntry.add(lblSpace27);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrGLEntry.add(btnDelete);

        lbSpace2.setText("     ");
        tbrGLEntry.add(lbSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrGLEntry.add(btnSave);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrGLEntry.add(lblSpace28);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrGLEntry.add(btnCancel);

        lblSpace3.setText("     ");
        tbrGLEntry.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrGLEntry.add(btnAuthorize);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrGLEntry.add(lblSpace29);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrGLEntry.add(btnException);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrGLEntry.add(lblSpace30);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrGLEntry.add(btnReject);

        lblSpace5.setText("     ");
        tbrGLEntry.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrGLEntry.add(btnPrint);

        lblSpace31.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace31.setText("     ");
        lblSpace31.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrGLEntry.add(lblSpace31);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrGLEntry.add(btnClose);

        getContentPane().add(tbrGLEntry, java.awt.BorderLayout.NORTH);

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

        mbrGLEntry.add(mnuProcess);

        setJMenuBar(mbrGLEntry);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // TODO add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // TODO add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // TODO add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    
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
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        setModified(false);
        savePerformed();
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView(ClientConstants.ACTION_STATUS[3]);
        ClientUtil.enableDisable(panGLEntry,false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView(ClientConstants.ACTION_STATUS[2]);
        ClientUtil.enableDisable(panGLEntry,true);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        setModified(false);
        observable.resetForm();
        ClientUtil.enableDisable(panGLEntry,false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        isFilled = false;
        setModified(true);
        observable.resetForm();
        ClientUtil.enableDisable(panGLEntry, true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        callView(ClientConstants.ACTION_STATUS[1]);
        
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void lblDescriptionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_lblDescriptionFocusLost
        // TODO add your handling code here:
        
    }//GEN-LAST:event_lblDescriptionFocusLost
    
    private void cboAccountHeadStatusFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboAccountHeadStatusFocusGained
        // TODO add your handling code here:
        
        
    }//GEN-LAST:event_cboAccountHeadStatusFocusGained
    
    private void txtAcHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAcHeadFocusLost
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtAcHeadFocusLost
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
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
    private com.see.truetransact.uicomponent.CComboBox cboAccountHeadStatus;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lblAcDesc;
    private com.see.truetransact.uicomponent.CLabel lblAcHead;
    private com.see.truetransact.uicomponent.CLabel lblAccountHeadStatus;
    private com.see.truetransact.uicomponent.CLabel lblAmount;
    private com.see.truetransact.uicomponent.CLabel lblDescription;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace26;
    private com.see.truetransact.uicomponent.CLabel lblSpace27;
    private com.see.truetransact.uicomponent.CLabel lblSpace28;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace30;
    private com.see.truetransact.uicomponent.CLabel lblSpace31;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CMenuBar mbrGLEntry;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panDescription;
    private com.see.truetransact.uicomponent.CPanel panGLEntry;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CToolBar tbrGLEntry;
    private com.see.truetransact.uicomponent.CTextField txtAcHead;
    private com.see.truetransact.uicomponent.CTextField txtAmount;
    // End of variables declaration//GEN-END:variables
    
}
