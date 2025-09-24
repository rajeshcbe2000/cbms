/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * GLLimitUI.java
 *
 * Created on June 7, 2005, 5:29 PM
 */

package com.see.truetransact.ui.rbi;

/**
 *
 * @author  ashokvijayakumar
 */
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.PercentageValidation;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Observer;
import java.util.Observable;
import javax.swing.JComponent;
import java.util.ArrayList;
import java.util.Date;
public class GLLimitUI extends CInternalFrame implements Observer,UIMandatoryField{
    
    
    ResourceBundle resourceBundle = ResourceBundle.getBundle("com.see.truetransact.ui.generalledger.gllimit.GLLimitRB", ProxyParameters.LANGUAGE);
    private HashMap mandatoryMap;
    private GLLimitOB observable;
    private GLLimitMRB objMandatoryRB = new GLLimitMRB();
    private String branchGroup = new String();
    private String viewType = "";
    private final String AUTHORIZE = "Authorize";
    private final String GROUP_ID = "GROUP_ID";
    private final String AC_HD_ID = "AC_HD_ID";
    private final String YES = "Y";
    private final String NO = "N";
    private Date currDt = null;
    
    /** Creates new form GLLimitUI */
    public GLLimitUI() {
        currDt = ClientUtil.getCurrentDate();
        initGLLimitForm();
    }
    
    private void initGLLimitForm(){
        initComponents();
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setHelpMessage();
        setObservable();
        setMaxLengths();
        setButtonEnableDisable();
        initTblModel();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panGLLimit);
        observable.resetForm();
        ClientUtil.enableDisable(panGLLimit,false);
        btnAdd.setEnabled(false);
    }
    
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        btnAdd.setName("btnAdd");
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
        lblAccountHead.setName("lblAccountHead");
        lblGLGroup.setName("lblGLGroup");
        lblLimit.setName("lblLimit");
        lblMsg.setName("lblMsg");
        lblSpace.setName("lblSpace");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace5.setName("lblSpace5");
        lblSpace6.setName("lblSpace6");
        lblStatus.setName("lblStatus");
        mbrMain.setName("mbrMain");
        panActHeadDetails.setName("panActHeadDetails");
        panGLLimit.setName("panGLLimit");
        panLimitDetails.setName("panLimitDetails");
        panSave.setName("panSave");
        panStatus.setName("panStatus");
        srpActHeadDetails.setName("srpActHeadDetails");
        tblActHeadDetails.setName("tblActHeadDetails");
        txtAccountHead.setName("txtAccountHead");
        txtGLGroup.setName("txtGLGroup");
        txtLimit.setName("txtLimit");
        txtAnnualLimit.setName("txtAnnualLimit");
        txtOverDraw.setName("txtOverDraw");
        lblInterBranchTransAllowed.setName("lblInterBranchTransAllowed");
        chkInterBranchTransAllowed.setName("chkInterBranchTransAllowed");
    }
    
    /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblGLGroup.setText(resourceBundle.getString("lblGLGroup"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblAccountHead.setText(resourceBundle.getString("lblAccountHead"));
        lblSpace.setText(resourceBundle.getString("lblSpace"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnAdd.setText(resourceBundle.getString("btnAdd"));
        lblLimit.setText(resourceBundle.getString("lblLimit"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace6.setText(resourceBundle.getString("lblSpace6"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblAnnualLimit.setText(resourceBundle.getString("lblAnnualLimit"));
        lblOverDraw.setText(resourceBundle.getString("lblOverDraw"));
        lblInterBranchTransAllowed.setText(resourceBundle.getString("lblInterBranchTransAllowed"));
    }
    
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        txtGLGroup.setText(observable.getTxtGLGroup());
        txtAccountHead.setText(observable.getTxtAccountHead());
        txtLimit.setText(observable.getTxtLimit());
        txtAnnualLimit.setText(observable.getTxtAnnualLimit());
        txtOverDraw.setText(observable.getTxtOverDraw());
        chkInterBranchTransAllowed.setSelected(observable.getChkInterBranchTransAllowed());
        lblStatus.setText(observable.getLblStatus());
    }
    
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtGLGroup(txtGLGroup.getText());
        observable.setTxtAccountHead(txtAccountHead.getText());
        observable.setTxtLimit(txtLimit.getText());
        observable.setTxtAnnualLimit(txtAnnualLimit.getText());
        observable.setTxtOverDraw(txtOverDraw.getText());
        observable.setChkInterBranchTransAllowed(chkInterBranchTransAllowed.isSelected());
    }
    
    
/* Auto Generated Method - setMandatoryHashMap()
 
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtGLGroup", new Boolean(true));
        mandatoryMap.put("txtAccountHead", new Boolean(true));
        mandatoryMap.put("txtLimit", new Boolean(true));
        mandatoryMap.put("txtAnnualLimit", new Boolean(true));
        mandatoryMap.put("txtOverDraw", new Boolean(true));
        mandatoryMap.put("chkInterBranchTransAllowed", new Boolean(true));
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
        txtGLGroup.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGLGroup"));
        txtAccountHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccountHead"));
        txtLimit.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLimit"));
        txtAnnualLimit.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAnnualLimit"));
        txtOverDraw.setHelpMessage(lblMsg,objMandatoryRB.getString("txtOverDraw"));
        chkInterBranchTransAllowed.setHelpMessage(lblMsg,objMandatoryRB.getString("chkInterBranchTransAllowed"));
    }
    
    /** This will create an instance of Observable and thereby add this instance
     * an observer of that observable **/
    private void setObservable(){
        try{
            observable = GLLimitOB.getInstance();
            observable.addObserver(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /** This is used to set the tablemodel to the tblActHeadDetails **/
    private void initTblModel(){
        tblActHeadDetails.setModel(observable.getTmlActHeadDetails());
    }
    
    /** This is used to set the Maximum allowed lengths to the TextFields in the UI **/
    private void setMaxLengths(){
        txtLimit.setAllowNumber(true);
        txtGLGroup.setMaxLength(16);
        txtAccountHead.setMaxLength(16);
        txtLimit.setMaxLength(16);
    }
    
    // To enable or disable the Buttons
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        
        btnSave.setEnabled(!btnSave.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
    }
    
    public static void main(String args[]){
        javax.swing.JFrame jFrame = new javax.swing.JFrame();
        GLLimitUI frame = new GLLimitUI();
        jFrame.getContentPane().add(frame);
        jFrame.setSize(500, 500);
        jFrame.setVisible(true);
        frame.setVisible(true);
    }
    
    private void callView(int currField) {
        HashMap viewMap = new HashMap();
        if(currField == ClientConstants.ACTIONTYPE_NEW){
            viewMap.put(CommonConstants.MAP_NAME, "viewBranchGLGroup");
        }else if(currField == ClientConstants.ACTIONTYPE_EDIT|| currField==ClientConstants.ACTIONTYPE_DELETE){
            viewMap.put(CommonConstants.MAP_NAME, "viewGLLimit");
        }
        new ViewAll(this, viewMap).show();
    }
    
    public void fillData(Object  map) {
        HashMap hash = (HashMap) map;
        branchGroup = CommonUtil.convertObjToStr(hash.get("GROUP_ID"));
        hash.put("GROUP_ID",hash.get("GROUP_ID"));
        txtGLGroup.setText(branchGroup);
        HashMap whereMap = new HashMap();
        whereMap.put(CommonConstants.MAP_WHERE,hash);
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW){
            whereMap.put(CommonConstants.MAP_NAME, "viewActHeadDetails");
        }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE || observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT ||
        observable.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE){
            whereMap.put(CommonConstants.MAP_NAME, "getSelectGLLimitTO");
        }
        observable.populateData(whereMap);
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW || observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
            ClientUtil.enableDisable(panGLLimit,true);
        }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE){
            ClientUtil.enableDisable(panGLLimit,false);
        }
        setButtonEnableDisable();
        populateTblActHeadDetails();
        setModified(true);
    }
    
    private String checkMandatory(JComponent component){
        return new MandatoryCheck().checkMandatory(getClass().getName(),component);
    }
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    private void populateTblActHeadDetails(){
        tblActHeadDetails.setModel(observable.getTmlActHeadDetails());
        tblActHeadDetails.revalidate();
    }
    
    /** Method used to Authorization operation **/
    public void authorizeStatus(String authorizeStatus) {
        //updateOBFields();
        if (!viewType.equals(AUTHORIZE)){
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            setModified(true);  //__ To Save the data in the Internal Frame...
            HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "getGLLimitAuthorizeList");
            mapParam.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeGLLimit");
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
            btnAdd.setEnabled(false);
        } else if (viewType.equals(AUTHORIZE) && tblActHeadDetails.getRowCount()!=0){
            viewType = "";
            if(txtAccountHead.getText().length()==0){
                displayAlert(objMandatoryRB.getString("txtAccountHead"));
                return;
            }else{
                HashMap singleAuthorizeMap = new HashMap();
                singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
                singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                singleAuthorizeMap.put(GROUP_ID,CommonUtil.convertObjToStr(txtGLGroup.getText()));
                singleAuthorizeMap.put(AC_HD_ID,CommonUtil.convertObjToStr(txtAccountHead.getText()));
                singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
                ClientUtil.execute("authorizeGLLimit", singleAuthorizeMap);
                btnSave.setEnabled(true);
                btnCancelActionPerformed(null);
                observable.setResult(observable.getActionType());
            }
            
        }
    }
    
    /** This method is used to make the fields editable when the row in the table is clicked **/
    private void setFieldsEditable(boolean flag){
        txtLimit.setEditable(flag);
        txtAnnualLimit.setEditable(flag);
        txtOverDraw.setEditable(flag);
    }
    
    /** This method is used to reset the Fields of UI, after the row of data is saved
     *in the table **/
    private void resetFields(){
        txtAccountHead.setText("");
        txtLimit.setText("");
        txtAnnualLimit.setText("");
        txtOverDraw.setText("");
        chkInterBranchTransAllowed.setSelected(false);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        tbrGLLimit = new javax.swing.JToolBar();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panGLLimit = new com.see.truetransact.uicomponent.CPanel();
        panLimitDetails = new com.see.truetransact.uicomponent.CPanel();
        lblGLGroup = new com.see.truetransact.uicomponent.CLabel();
        txtGLGroup = new com.see.truetransact.uicomponent.CTextField();
        lblAccountHead = new com.see.truetransact.uicomponent.CLabel();
        txtAccountHead = new com.see.truetransact.uicomponent.CTextField();
        lblLimit = new com.see.truetransact.uicomponent.CLabel();
        txtLimit = new com.see.truetransact.uicomponent.CTextField();
        panSave = new com.see.truetransact.uicomponent.CPanel();
        btnAdd = new com.see.truetransact.uicomponent.CButton();
        lblAnnualLimit = new com.see.truetransact.uicomponent.CLabel();
        lblOverDraw = new com.see.truetransact.uicomponent.CLabel();
        txtAnnualLimit = new com.see.truetransact.uicomponent.CTextField();
        txtOverDraw = new com.see.truetransact.uicomponent.CTextField();
        lblInterBranchTransAllowed = new com.see.truetransact.uicomponent.CLabel();
        chkInterBranchTransAllowed = new com.see.truetransact.uicomponent.CCheckBox();
        panActHeadDetails = new com.see.truetransact.uicomponent.CPanel();
        srpActHeadDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblActHeadDetails = new com.see.truetransact.uicomponent.CTable();
        mbrMain = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptDelete = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();
        mitNew = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setPreferredSize(new java.awt.Dimension(730, 370));
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif")));
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        tbrGLLimit.add(btnNew);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif")));
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        tbrGLLimit.add(btnEdit);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif")));
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        tbrGLLimit.add(btnDelete);

        lblSpace2.setText("     ");
        tbrGLLimit.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif")));
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        tbrGLLimit.add(btnSave);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif")));
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        tbrGLLimit.add(btnCancel);

        lblSpace3.setText("     ");
        tbrGLLimit.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif")));
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });

        tbrGLLimit.add(btnAuthorize);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif")));
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });

        tbrGLLimit.add(btnReject);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif")));
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });

        tbrGLLimit.add(btnException);

        lblSpace5.setText("     ");
        tbrGLLimit.add(lblSpace5);

        lblSpace6.setText("     ");
        tbrGLLimit.add(lblSpace6);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif")));
        btnPrint.setToolTipText("Print");
        tbrGLLimit.add(btnPrint);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif")));
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        tbrGLLimit.add(btnClose);

        getContentPane().add(tbrGLLimit, java.awt.BorderLayout.NORTH);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace, gridBagConstraints);

        lblStatus.setBorder(new javax.swing.border.EtchedBorder());
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

        panGLLimit.setLayout(new java.awt.GridBagLayout());

        panLimitDetails.setLayout(new java.awt.GridBagLayout());

        lblGLGroup.setText("GL Group");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLimitDetails.add(lblGLGroup, gridBagConstraints);

        txtGLGroup.setEditable(false);
        txtGLGroup.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLimitDetails.add(txtGLGroup, gridBagConstraints);

        lblAccountHead.setText("Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLimitDetails.add(lblAccountHead, gridBagConstraints);

        txtAccountHead.setEditable(false);
        txtAccountHead.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLimitDetails.add(txtAccountHead, gridBagConstraints);

        lblLimit.setText("Amount Limit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLimitDetails.add(lblLimit, gridBagConstraints);

        txtLimit.setEditable(false);
        txtLimit.setMinimumSize(new java.awt.Dimension(100, 21));
        txtLimit.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLimitDetails.add(txtLimit, gridBagConstraints);

        btnAdd.setText("Save");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        panSave.add(btnAdd);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLimitDetails.add(panSave, gridBagConstraints);

        lblAnnualLimit.setText("Annual Limit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLimitDetails.add(lblAnnualLimit, gridBagConstraints);

        lblOverDraw.setText("Over Draw Percentage");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLimitDetails.add(lblOverDraw, gridBagConstraints);

        txtAnnualLimit.setEditable(false);
        txtAnnualLimit.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLimitDetails.add(txtAnnualLimit, gridBagConstraints);

        txtOverDraw.setEditable(false);
        txtOverDraw.setValidation(new PercentageValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLimitDetails.add(txtOverDraw, gridBagConstraints);

        lblInterBranchTransAllowed.setText("Inter Branch Transaction Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLimitDetails.add(lblInterBranchTransAllowed, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLimitDetails.add(chkInterBranchTransAllowed, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 124, 24);
        panGLLimit.add(panLimitDetails, gridBagConstraints);

        panActHeadDetails.setLayout(new java.awt.GridBagLayout());

        srpActHeadDetails.setMinimumSize(new java.awt.Dimension(390, 250));
        srpActHeadDetails.setPreferredSize(new java.awt.Dimension(450, 404));
        tblActHeadDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Act. Head", "Amt. Limit", "Ann. Limit", "Over Draw %", "Branch Trans."
            }
        ));
        tblActHeadDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblActHeadDetailsMouseClicked(evt);
            }
        });

        srpActHeadDetails.setViewportView(tblActHeadDetails);

        panActHeadDetails.add(srpActHeadDetails, new java.awt.GridBagConstraints());

        panGLLimit.add(panActHeadDetails, new java.awt.GridBagConstraints());

        getContentPane().add(panGLLimit, java.awt.BorderLayout.CENTER);

        mnuProcess.setText("Process");
        mitEdit.setMnemonic('E');
        mitEdit.setText("Edit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });

        mnuProcess.add(mitEdit);

        mitDelete.setMnemonic('D');
        mitDelete.setText("Delete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });

        mnuProcess.add(mitDelete);

        mnuProcess.add(sptDelete);

        mitSave.setMnemonic('S');
        mitSave.setText("Save");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });

        mnuProcess.add(mitSave);

        mitCancel.setMnemonic('C');
        mitCancel.setText("Cancel");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });

        mnuProcess.add(mitCancel);

        mnuProcess.add(sptCancel);

        mitClose.setMnemonic('L');
        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });

        mnuProcess.add(mitClose);

        mitNew.setMnemonic('N');
        mitNew.setText("New");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });

        mnuProcess.add(mitNew);

        mbrMain.add(mnuProcess);

        setJMenuBar(mbrMain);

        pack();
    }//GEN-END:initComponents
    
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
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
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
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        observable.resetForm();
        populateTblActHeadDetails();
        ClientUtil.enableDisable(panGLLimit, false);
        setButtonEnableDisable();
        btnAdd.setEnabled(false);
        setModified(false);
        viewType = "";
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView(ClientConstants.ACTIONTYPE_DELETE);
        
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView(ClientConstants.ACTIONTYPE_EDIT);
        
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        observable.setTxtGLGroup(branchGroup);
        observable.doAction();
        observable.resetForm();
        populateTblActHeadDetails();
        setModified(false);
        btnAdd.setEnabled(false);
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        final String mandatoryMessage = checkMandatory(panGLLimit);
        if(mandatoryMessage.length() > 0 ){
            displayAlert(mandatoryMessage);
        }else{
            ArrayList rowData = new ArrayList();
            rowData.add(txtAccountHead.getText());
            rowData.add(CommonUtil.convertObjToDouble(txtLimit.getText()));
            if(!txtAnnualLimit.getText().equals("") || txtAnnualLimit.getText().length()!=0){
                rowData.add(CommonUtil.convertObjToDouble(txtAnnualLimit.getText()));
            }else{
                rowData.add(null);
            }
            if(txtOverDraw.getText().length()!=0){
                rowData.add(CommonUtil.convertObjToDouble(txtOverDraw.getText()));
            }else{
                rowData.add(null);
            }
            if(chkInterBranchTransAllowed.isSelected()){
                rowData.add(YES);
            }else{
                rowData.add(NO);
            }
            observable.updateRowData(tblActHeadDetails.getSelectedRow(),rowData);
            populateTblActHeadDetails();
            resetFields();
            setFieldsEditable(false);
        }
        
    }//GEN-LAST:event_btnAddActionPerformed
    
    private void tblActHeadDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblActHeadDetailsMouseClicked
        // TODO add your handling code here:
        updateOBFields();//To retain the Group Head
        setFieldsEditable(true);
        observable.populateSelectedRow(tblActHeadDetails.getSelectedRow());
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE || observable.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE){
            btnAdd.setEnabled(false);
            setFieldsEditable(false);
        }else{
            btnAdd.setEnabled(true);
            setFieldsEditable(true);
        }
        
    }//GEN-LAST:event_tblActHeadDetailsMouseClicked
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        callView(ClientConstants.ACTIONTYPE_NEW);
        
    }//GEN-LAST:event_btnNewActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAdd;
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
    private com.see.truetransact.uicomponent.CCheckBox chkInterBranchTransAllowed;
    private com.see.truetransact.uicomponent.CLabel lblAccountHead;
    private com.see.truetransact.uicomponent.CLabel lblAnnualLimit;
    private com.see.truetransact.uicomponent.CLabel lblGLGroup;
    private com.see.truetransact.uicomponent.CLabel lblInterBranchTransAllowed;
    private com.see.truetransact.uicomponent.CLabel lblLimit;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblOverDraw;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panActHeadDetails;
    private com.see.truetransact.uicomponent.CPanel panGLLimit;
    private com.see.truetransact.uicomponent.CPanel panLimitDetails;
    private com.see.truetransact.uicomponent.CPanel panSave;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private com.see.truetransact.uicomponent.CScrollPane srpActHeadDetails;
    private com.see.truetransact.uicomponent.CTable tblActHeadDetails;
    private javax.swing.JToolBar tbrGLLimit;
    private com.see.truetransact.uicomponent.CTextField txtAccountHead;
    private com.see.truetransact.uicomponent.CTextField txtAnnualLimit;
    private com.see.truetransact.uicomponent.CTextField txtGLGroup;
    private com.see.truetransact.uicomponent.CTextField txtLimit;
    private com.see.truetransact.uicomponent.CTextField txtOverDraw;
    // End of variables declaration//GEN-END:variables
    
}
