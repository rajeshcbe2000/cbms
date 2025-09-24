/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 * DeathMarkingUI.java
 *
 * Created on May 26, 2004, 4:35 PM
 */

package com.see.truetransact.ui.sysadmin.noticereportparameters;

/**
 *
 * @author Swaroop
 */

import java.util.Observer;
import java.util.Observable;
import java.util.HashMap;
import java.util.Date;
import java.util.ArrayList;

import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.ResourceBundle;
import com.see.truetransact.clientproxy.ProxyParameters;
import java.util.List;
import java.awt.Font;
//import javax.swing.text.StyleConstants;
//import javax.swing.text.Document;
//import javax.swing.text.SimpleAttributeSet;
import java.awt.Color;
import javax.swing.text.BadLocationException;
import javax.swing.JTextPane;
import javax.swing.text.*;
//import javax.swing.text.Style;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.AbstractDocument.DefaultDocumentEvent;
import javax.swing.ImageIcon;
import java.io.File;
import java.io.FileReader;


public class NoticePeriodUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField{
    
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("com.see.truetransact.ui.sysadmin.noticereportparameters.NoticePeriodRB", ProxyParameters.LANGUAGE);
    private HashMap mandatoryMap;
    private NoticePeriodOB observable;
   private  NoticePeriodMRB objMandatoryRB;
    private String viewType= "";
    final String AUTHORIZE="Authorize";
    private boolean isFilled = false;
    private Date curDate = null;
    
    /** Creates new form DeathMarkingUI */
    public NoticePeriodUI() {
        curDate = ClientUtil.getCurrentDate();
        initComponents();
        initUIComponents();
    }
    
    /** Initialise the ui components */
    private void initUIComponents(){
        setFieldNames();
        internationalize();
        setObservable();
        setMandatoryHashMap();
        setHelpMessage();
        setMaximumLength();
        initComponentData();
        observable.resetForm();
        ClientUtil.enableDisable(panMain, false);
        setButtonEnableDisable();
        txtCustomerId.setEnabled(false);
    }
    
    /** Sets the names to each Uicomponents */
    private void setFieldNames() {
        btnNew.setName("btnNew");
        btnDelete.setName("btnDelete");
        btnCancel.setName("btnCancel");
        btnAuthorize.setName("btnAuthorize");
        btnReject.setName("btnReject");
        btnException.setName("btnException");
        btnClose.setName("btnClose");
        btnEdit.setName("btnEdit");
        btnPrint.setName("btnPrint");
        btnSave.setName("btnSave");
        txtCustomerId.setName("txtCustomerId");
        lblReportID.setName("lblReportID");
        lblMsg.setName("lblMsg");
        lblCustomerName.setName("lblCustomerName");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblStatus.setName("lblStatus");
        mbrDeathMarking.setName("mbrDeathMarking");
        panId.setName("panId");
        panMain.setName("panMain");
        panStatus.setName("panStatus");
        txtReportName.setName("txtReportName");
        cboColNames.setName("cboColNames");
        lblLan.setName("lblLan");
        lblValues.setName("lblValues");
        lblGr.setName("lblGr");
        cboLan.setName("cboLan");
        cboGrDet.setName("cboGrDet");
        lblRepotHeading.setName("lblRepotHeading");
        txtRepotHeading.setName("txtRepotHeading");
        jTextPaneData.setName("jTextPaneData");
    }
    
    /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnException.setText(resourceBundle.getString("btnException"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblCustomerName.setText(resourceBundle.getString("lblCustomerName"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblReportID.setText(resourceBundle.getString("lblReportID"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
    }
    
    
    /* Auto Generated Method - setMandatoryHashMap()
      This method list out all the Input Fields available in the UI.
      It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboGrDet", new Boolean(true));
        mandatoryMap.put("cboLan", new Boolean(true));
        mandatoryMap.put("txtReportName",new Boolean(true));
        mandatoryMap.put("jTextPaneData",new Boolean(true));
        mandatoryMap.put("txtRepotHeading",new Boolean(true));
        
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
        objMandatoryRB = new NoticePeriodMRB();
        cboLan.setHelpMessage(lblMsg, objMandatoryRB.getString("cboLan"));
        cboGrDet.setHelpMessage(lblMsg, objMandatoryRB.getString("cboGrDet"));
        txtReportName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtReportName"));
        txtRepotHeading.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRepotHeading"));
    }
    
     /* Auto Generated Method - update()
        This method called by Observable. It updates the UI with
        Observable's data. If needed add/Remove RadioButtons
        method need to be added.*/
    public void update(Observable observed, Object arg) {
        txtReportName.setText(observable.getTxtReportName());
        txtCustomerId.setText(observable.getTxtCustomerId());
        cboLan.setModel(observable.getCbmLan());
        cboGrDet.setModel(observable.getCbmGrDetails());
        txtRepotHeading.setText(observable.getReportHeading());
        String lang= ((ComboBoxModel)cboLan.getModel()).getKeyForSelected().toString();
        String dup = observable.getTxtEnteredData();
        ArrayList list = observable.getCbmColName().getKeys();
        StyleContext sc = new StyleContext();
        DefaultStyledDocument doc = new DefaultStyledDocument(sc);
        jTextPaneData.setStyledDocument(doc);
        try{
            doc.insertString(0, observable.getTxtEnteredData(), null);
            if(lang.equalsIgnoreCase("KANNADA")){
            Style kannadaStyle = sc.addStyle("KANNADA", null);
            kannadaStyle.addAttribute(StyleConstants.Foreground, Color.BLACK);
            kannadaStyle.addAttribute(StyleConstants.FontSize, new Integer(18));
            kannadaStyle.addAttribute(StyleConstants.FontFamily, "BRH Kannada");
            doc.setParagraphAttributes(0, doc.getLength(), kannadaStyle, true);
            }
            else if(lang.equalsIgnoreCase("HINDI")){
                Style HindiStyle = sc.addStyle("HINDI", null);
            HindiStyle.addAttribute(StyleConstants.Foreground, Color.BLACK);
            HindiStyle.addAttribute(StyleConstants.FontSize, new Integer(18));
            HindiStyle.addAttribute(StyleConstants.FontFamily, "Kruti Dev 010");
            doc.setParagraphAttributes(0, doc.getLength(), HindiStyle, true);
            }
            else if(lang.equalsIgnoreCase("ENGLISH")){
            Style enStyle = sc.addStyle("ENGLISH", null);
            enStyle.addAttribute(StyleConstants.Foreground, Color.BLACK);
            enStyle.addAttribute(StyleConstants.FontSize, new Integer(16));
            enStyle.addAttribute(StyleConstants.FontFamily, "MS Sans Serif");
            doc.setParagraphAttributes(0, doc.getLength(), enStyle, true);
            }
            
            Style englishStyle = sc.addStyle("ENGLISH", null);
            englishStyle.addAttribute(StyleConstants.Foreground, Color.BLACK);
            englishStyle.addAttribute(StyleConstants.FontFamily, "MS Sans Serif");
            
            if(list!=null && list.size()>0){
                int a=0;
                String str="";
                for(int i=0;i<list.size();i++){
                    str = "&"+CommonUtil.convertObjToStr(list.get(i))+"&";
                    if((a=dup.indexOf(str,a))!=-1){                      
                        doc.setCharacterAttributes(a, str.length(), englishStyle, false);
                         int nextOcc=dup.indexOf(str,a+1);
                          while(nextOcc>0){
                           doc.setCharacterAttributes(nextOcc, str.length(), englishStyle, false);
                           nextOcc=dup.indexOf(str,nextOcc+1);
                          }
                    }
                }
                jTextPaneData.setDocument(doc);
            }
     
        }
        catch (Exception ble){
        }
        setFont();
    }
    
    /* Auto Generated Method - updateOBFields()
     This method called by Save option of UI.
     It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtCustomerId(txtCustomerId.getText());
        observable.setSelectedBranchID(getSelectedBranchID());
        observable.setTxtEnteredData(jTextPaneData.getText());
        observable.setTxtReportName(txtReportName.getText());
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setCboLan((String) cboLan.getSelectedItem());
        observable.setCboGrDetails((String) cboGrDet.getSelectedItem());
        observable.setReportHeading(txtRepotHeading.getText());
    }
    
    /** Sets the textfields to a maximumlength */
    private void setMaximumLength(){
        txtCustomerId.setMaxLength(16);
    }
    
    /** Sets the datamodel for the Combobox */
    private void initComponentData(){
        cboColNames.setModel(observable.getCbmColName());
        cboLan.setModel(observable.getCbmLan());
        cboGrDet.setModel(observable.getCbmGrDetails());
    }
    
    /** Adds this ui as a observer to a observable class */
    private void setObservable() {
        observable = NoticePeriodOB.getInstance();
        observable.addObserver(this);
    }
    
    /** Makes the buttons either enable or disable */
    private void setButtonEnableDisable(){
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
    
    /* Does necessary operaion when user clicks the save button */
    private void savePerformed(){
        updateOBFields();
        observable.doAction();
        ClientUtil.clearAll(this);
        isFilled = false;
        if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
            HashMap lockMap = new HashMap();
            ArrayList lst = new ArrayList();
            lst.add("REPORT_ID");
            lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
            if (observable.getProxyReturnMap()!=null) {
                if (observable.getProxyReturnMap().containsKey("REPORT_ID")) {
                    lockMap.put("REPORT_ID", observable.getProxyReturnMap().get("REPORT_ID"));
                }
            }
            if (viewType.equals(ClientConstants.ACTION_STATUS[2])) {
                lockMap.put("REPORT_ID", observable.getTxtCustomerId());
            }
            setEditLockMap(lockMap);
            setEditLock();
            settings();
        }
    }
    
    /* set the screen after the updation,insertion, deletion */
    private void settings(){
//        observable.resetForm();
//        ClientUtil.clearAll(this);
//        isFilled = false;
//        ClientUtil.enableDisable(panMain, false);
//        setButtonEnableDisable();
          btnCancelActionPerformed(null);
         observable.setResultStatus();
    }
    
    /** This will check the mandatoriness of the fields in the UI */
    private String checkMandatory(javax.swing.JComponent component){
        return new MandatoryCheck().checkMandatory(getClass().getName(), component);
    }
    
    /** Display the alertmessage when the mandatory fields are left empty */
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    /** This method is called up whenever editbuton,deletebutton or the helpbutton is clicked */
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        whereMap.put("BRANCH_ID", getSelectedBranchID());
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        if (currField.equals(ClientConstants.ACTION_STATUS[2])  ||
        currField.equals(ClientConstants.ACTION_STATUS[3])) {
            viewMap.put(CommonConstants.MAP_NAME, "getSelectNotPeriodDet");
        } else {
            viewMap.put(CommonConstants.MAP_NAME, "getSelectNotPeriodDet");
        }
        new ViewAll(this,viewMap).show();
    }
    
    /* Fills up the HashMap with data when user selects the row in ViewAll screen  */
    public void fillData(Object  map) {
        try{
            setModified(true);
            HashMap hash = (HashMap) map;
            final String report_id = CommonUtil.convertObjToStr(hash.get("REPORT_ID"));
            if (viewType != null) {
                if (viewType.equals(ClientConstants.ACTION_STATUS[2]) ||
                viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) ) {
                    isFilled = true;
                    hash.put(CommonConstants.MAP_WHERE, hash.get("REPORT_ID"));
                    hash.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
                    observable.populateData(hash);
                    if (observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE) {
                        ClientUtil.enableDisable(panMain, false);
                    }
                    if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT) {
                        ClientUtil.enableDisable(panMain, true);
                    }
                    if(viewType.equals(AUTHORIZE)){
                        ClientUtil.enableDisable(panMain, false);
                    }
                    setButtonEnableDisable();
                }
                
                updateOBFields();
                observable.notifyObservers();
            }
        }catch(Exception e){
        }
    }
    
    /** Does the authorization for the row selected in the AuthorizationUI screen */
    public void authorizeStatus(String authorizeStatus) {
        viewType = AUTHORIZE;
        if (!isFilled ){
            setModified(true);
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getNotPerAuthorizeList");
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        } else if (isFilled){
            isFilled = false;
            HashMap singleAuthorizeMap = new HashMap();
            ArrayList arrList = new ArrayList();
            HashMap authDataMap = new HashMap();
            authDataMap.put("AUTH_BY", TrueTransactMain.USER_ID);
            authDataMap.put("AUTH_DT",ClientUtil.getCurrentDateWithTime());
            authDataMap.put("REPORT_ID", txtCustomerId.getText());
            authDataMap.put("AUTH_STATUS", authorizeStatus);
            arrList.add(authDataMap);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(singleAuthorizeMap);
            
        }
    }
    
    /* Does the Authorization thru DeathMarkingDA0 class **/
    public void authorize(HashMap map) {
        map.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        observable.setAuthorizeMap(map);
        observable.doAction();
        btnCancelActionPerformed(null);
    }
    
    
    /* This shows the Alert when the user data entry is wrong **/
    private int showAlertWindow(String alertMsg) throws Exception {
        int optionSelected = 1;
        String[] options = {resourceBundle.getString("cDialogOK")};
        optionSelected = COptionPane.showOptionDialog(null,resourceBundle.getString(alertMsg), CommonConstants.INFORMATIONTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        return optionSelected;
    }
    
    public void checkForUniqueReportName(){
        HashMap reMap  = new HashMap();
        String repname=txtReportName.getText();
        reMap.put("REPORT_NAME",repname);
        List repNameList = ClientUtil.executeQuery("checkforUniqueRepName", reMap);
        if(repNameList!=null && repNameList.size()>0){
            reMap= null;
            for(int i=0;i<repNameList.size();i++){
                reMap= (HashMap) repNameList.get(0);
                String str= (String)reMap.get("REPORT_NAME");
                if(str.equalsIgnoreCase(repname)){
                    ClientUtil.displayAlert("Report Name Already Used");
                    txtReportName.setText("");
                    return;
                }
            }
        }
    }
    
    public void setFont(){
         if (cboLan.getSelectedIndex() > 0) {
            String lang = ((ComboBoxModel)cboLan.getModel()).getKeyForSelected().toString();
            if(lang.equalsIgnoreCase("KANNADA")){
                Font font = new Font("BRH Kannada",0, 18);
                jTextPaneData.setFont(font);
            }
            else if(lang.equalsIgnoreCase("HINDI")){
                Font font = new Font("Kruti Dev 010",0, 18);
                jTextPaneData.setFont(font);
            }
            else if(lang.equalsIgnoreCase("ENGLISH")){
                Font font = new Font("MS Sans Serif",0, 16);
                jTextPaneData.setFont(font);
            }
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

        tbrDeathMarking = new javax.swing.JToolBar();
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
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace31 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panMain = new com.see.truetransact.uicomponent.CPanel();
        panId = new com.see.truetransact.uicomponent.CPanel();
        lblReportID = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerName = new com.see.truetransact.uicomponent.CLabel();
        panDepositNumber = new com.see.truetransact.uicomponent.CPanel();
        txtCustomerId = new com.see.truetransact.uicomponent.CTextField();
        txtReportName = new com.see.truetransact.uicomponent.CTextField();
        cboColNames = new com.see.truetransact.uicomponent.CComboBox();
        cboGrDet = new com.see.truetransact.uicomponent.CComboBox();
        cboLan = new com.see.truetransact.uicomponent.CComboBox();
        lblValues = new com.see.truetransact.uicomponent.CLabel();
        lblLan = new com.see.truetransact.uicomponent.CLabel();
        lblGr = new com.see.truetransact.uicomponent.CLabel();
        txtRepotHeading = new com.see.truetransact.uicomponent.CTextField();
        lblRepotHeading = new com.see.truetransact.uicomponent.CLabel();
        srpTransDetails = new com.see.truetransact.uicomponent.CScrollPane();
        jTextPaneData = new javax.swing.JTextPane();
        mbrDeathMarking = new com.see.truetransact.uicomponent.CMenuBar();
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
        setPreferredSize(new java.awt.Dimension(850, 550));

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrDeathMarking.add(btnNew);

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrDeathMarking.add(lblSpace26);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrDeathMarking.add(btnEdit);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrDeathMarking.add(lblSpace27);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrDeathMarking.add(btnDelete);

        lblSpace2.setText("     ");
        tbrDeathMarking.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrDeathMarking.add(btnSave);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrDeathMarking.add(lblSpace28);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrDeathMarking.add(btnCancel);

        lblSpace3.setText("     ");
        tbrDeathMarking.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrDeathMarking.add(btnAuthorize);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrDeathMarking.add(lblSpace29);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrDeathMarking.add(btnException);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrDeathMarking.add(lblSpace30);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrDeathMarking.add(btnReject);

        lblSpace4.setText("     ");
        tbrDeathMarking.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrDeathMarking.add(btnPrint);

        lblSpace31.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace31.setText("     ");
        lblSpace31.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrDeathMarking.add(lblSpace31);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrDeathMarking.add(btnClose);

        getContentPane().add(tbrDeathMarking, java.awt.BorderLayout.NORTH);

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

        panMain.setMinimumSize(new java.awt.Dimension(724, 466));
        panMain.setPreferredSize(new java.awt.Dimension(965, 600));
        panMain.setLayout(new java.awt.GridBagLayout());

        panId.setMinimumSize(new java.awt.Dimension(750, 80));
        panId.setPreferredSize(new java.awt.Dimension(800, 80));
        panId.setLayout(new java.awt.GridBagLayout());

        lblReportID.setText("Report Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panId.add(lblReportID, gridBagConstraints);

        lblCustomerName.setText("ReportName");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panId.add(lblCustomerName, gridBagConstraints);

        panDepositNumber.setLayout(new java.awt.GridBagLayout());

        txtCustomerId.setEditable(false);
        txtCustomerId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositNumber.add(txtCustomerId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panId.add(panDepositNumber, gridBagConstraints);

        txtReportName.setEditable(false);
        txtReportName.setMinimumSize(new java.awt.Dimension(200, 21));
        txtReportName.setPreferredSize(new java.awt.Dimension(200, 21));
        txtReportName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtReportNameActionPerformed(evt);
            }
        });
        txtReportName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtReportNameFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panId.add(txtReportName, gridBagConstraints);

        cboColNames.setMinimumSize(new java.awt.Dimension(150, 21));
        cboColNames.setPreferredSize(new java.awt.Dimension(150, 21));
        cboColNames.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboColNamesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panId.add(cboColNames, gridBagConstraints);

        cboGrDet.setMinimumSize(new java.awt.Dimension(150, 21));
        cboGrDet.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panId.add(cboGrDet, gridBagConstraints);

        cboLan.setMinimumSize(new java.awt.Dimension(150, 21));
        cboLan.setPreferredSize(new java.awt.Dimension(150, 21));
        cboLan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLanActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panId.add(cboLan, gridBagConstraints);

        lblValues.setText("Values");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panId.add(lblValues, gridBagConstraints);

        lblLan.setText("Language");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panId.add(lblLan, gridBagConstraints);

        lblGr.setText("Guaranotor");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panId.add(lblGr, gridBagConstraints);

        txtRepotHeading.setEditable(false);
        txtRepotHeading.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panId.add(txtRepotHeading, gridBagConstraints);

        lblRepotHeading.setText("ReportHeading");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panId.add(lblRepotHeading, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 24, 4, 0);
        panMain.add(panId, gridBagConstraints);

        srpTransDetails.setMaximumSize(new java.awt.Dimension(605, 210));
        srpTransDetails.setMinimumSize(new java.awt.Dimension(605, 210));
        srpTransDetails.setPreferredSize(new java.awt.Dimension(605, 210));

        jTextPaneData.setMinimumSize(new java.awt.Dimension(600, 200));
        jTextPaneData.setPreferredSize(new java.awt.Dimension(600, 200));
        srpTransDetails.setViewportView(jTextPaneData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panMain.add(srpTransDetails, gridBagConstraints);

        getContentPane().add(panMain, java.awt.BorderLayout.CENTER);

        mbrDeathMarking.setName("mbrCustomer");

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

        mbrDeathMarking.add(mnuProcess);

        setJMenuBar(mbrDeathMarking);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cboLanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLanActionPerformed
        // TODO add your handling code here:
        if(evt.getModifiers()==16){
            setFont();
        }
    }//GEN-LAST:event_cboLanActionPerformed

    private void txtReportNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtReportNameFocusLost
        // TODO add your handling code here:
        checkForUniqueReportName();
    }//GEN-LAST:event_txtReportNameFocusLost

    private void txtReportNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtReportNameActionPerformed
        // TODO add your handling code here:
        checkForUniqueReportName();
    }//GEN-LAST:event_txtReportNameActionPerformed

    private void cboColNamesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboColNamesActionPerformed
        // TODO add your handling code here:
        if (cboColNames.getSelectedIndex() > 0) {
            String colName= "&"+((ComboBoxModel)cboColNames.getModel()).getKeyForSelected().toString()+"&";
            String lang= ((ComboBoxModel)cboLan.getModel()).getKeyForSelected().toString();
            SimpleAttributeSet actNum = new SimpleAttributeSet();
            StyleConstants.setForeground(actNum, Color.BLACK);
            StyleConstants.setFontFamily(actNum, "MS Sans Serif");
            Document d = jTextPaneData.getDocument();
            jTextPaneData.requestFocus();
            try {
                d.insertString(jTextPaneData.getCaretPosition(), colName, actNum);
                if(lang.equalsIgnoreCase("KANNADA")){
                    StyleConstants.setFontFamily(actNum, "BRH Kannada");
                    StyleConstants.setForeground(actNum, Color.BLACK);
                    d.insertString(jTextPaneData.getCaretPosition(), " ", actNum);
                }
                else if(lang.equalsIgnoreCase("HINDI")){
                    StyleConstants.setFontFamily(actNum, "Kruti Dev 010");
                    StyleConstants.setForeground(actNum, Color.BLACK);
                    d.insertString(jTextPaneData.getCaretPosition(), " ", actNum);
                }
                else if(lang.equalsIgnoreCase("ENGLISH")){
                    StyleConstants.setFontFamily(actNum, "MS Sans Serif");
                    StyleConstants.setForeground(actNum, Color.BLACK);
                    d.insertString(jTextPaneData.getCaretPosition(), " ", actNum);
                }
//                cboColNames.setSelectedItem("");
//                colName="";
            } catch (BadLocationException ble) {
            }
        }
    }//GEN-LAST:event_cboColNamesActionPerformed
            
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        btnSaveActionPerformed(evt);
        
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
        btnNewActionPerformed(evt);
        
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
        
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView("Delete");
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView("Edit");
        txtCustomerId.setEnabled(false);
        cboLan.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
                
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        setModified(false);
        String mandatoryMessage = checkMandatory(panMain);
        if(mandatoryMessage.length() > 0){
            displayAlert(mandatoryMessage);
        }else{
            savePerformed();
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        setModified(false);
        observable.resetForm();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panMain, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        setModified(true);
        observable.resetForm();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panMain, true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        txtCustomerId.setEnabled(false);
    }//GEN-LAST:event_btnNewActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        javax.swing.JFrame frame=new javax.swing.JFrame();
        NoticePeriodUI  tui = new NoticePeriodUI();
        frame.getContentPane().add(tui);
        frame.setSize(600,400);
        frame.show();
        tui.show();
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
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CComboBox cboColNames;
    private com.see.truetransact.uicomponent.CComboBox cboGrDet;
    private com.see.truetransact.uicomponent.CComboBox cboLan;
    private javax.swing.JTextPane jTextPaneData;
    private com.see.truetransact.uicomponent.CLabel lblCustomerName;
    private com.see.truetransact.uicomponent.CLabel lblGr;
    private com.see.truetransact.uicomponent.CLabel lblLan;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblReportID;
    private com.see.truetransact.uicomponent.CLabel lblRepotHeading;
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
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblValues;
    private com.see.truetransact.uicomponent.CMenuBar mbrDeathMarking;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panDepositNumber;
    private com.see.truetransact.uicomponent.CPanel panId;
    private com.see.truetransact.uicomponent.CPanel panMain;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptEdit;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpTransDetails;
    private javax.swing.JToolBar tbrDeathMarking;
    private com.see.truetransact.uicomponent.CTextField txtCustomerId;
    private com.see.truetransact.uicomponent.CTextField txtReportName;
    private com.see.truetransact.uicomponent.CTextField txtRepotHeading;
    // End of variables declaration//GEN-END:variables
    
}
