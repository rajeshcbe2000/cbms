/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * GLLimitUI.java
 *
 * Created on June 7, 2005, 5:29 PM
 */

package com.see.truetransact.ui.generalledger.gllimit;

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
    private String transnew="";
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
        btnGlGroup.setEnabled(false);
        btnAccountHead.setEnabled(false);
        btnNew1.setEnabled(false);       
        btnDelete1.setEnabled(false);
        
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
        //lblSpace6.setName("lblSpace6");
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
        tdtFrmPeriod.setName("tdtFrmPeriod");
        tdtToPeriod.setName("tdtToPeriod");
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
        //lblSpace6.setText(resourceBundle.getString("lblSpace6"));
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
        if(observable.getChkInterBranchTransAllowed()){
            chkInterBranchTransAllowed.setSelected(true);
        }else{
            chkInterBranchTransAllowed.setSelected(false);
        }
        lblStatus.setText(observable.getLblStatus());
        tdtFrmPeriod.setDateValue(observable.getFrmPeriod());
        tdtToPeriod.setDateValue(observable.getToPeriod());
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
        observable.setFrmPeriod(tdtFrmPeriod.getDateValue());
        observable.setToPeriod(tdtToPeriod.getDateValue());
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
        mandatoryMap.put("tdtFrmPeriod", new Boolean(true));
        mandatoryMap.put("tdtToPeriod", new Boolean(true));
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
    
    private void callView(String currField) {
        HashMap viewMap = new HashMap();
        if(currField.equals("GlGroup") ){
            viewMap.put(CommonConstants.MAP_NAME, "viewBranchGLGroup");
        }else if(currField.equals("AccountHead")){
            HashMap hmap=new HashMap();
            hmap.put("GROUP_ID",txtGLGroup.getText());
            viewMap.put(CommonConstants.MAP_NAME, "viewActHeadDetails");
            viewMap.put(CommonConstants.MAP_WHERE,hmap);
        }else if(currField.equals("Edit")){
            viewMap.put(CommonConstants.MAP_NAME, "getEditListForGLLimit");
        }else if(currField.equals("Delete")){
            viewMap.put(CommonConstants.MAP_NAME, "getDeleteListForGLLimit");
        }
        new ViewAll(this, viewMap).show();
    }
    
    public void fillData(Object  map) {
        HashMap hash = (HashMap) map;
        if(viewType.equals("GlGroup")){
        branchGroup = CommonUtil.convertObjToStr(hash.get("GROUP_ID"));
        hash.put("GROUP_ID",hash.get("GROUP_ID"));
        txtGLGroup.setText(branchGroup);
        }else  if(viewType.equals("AccountHead") || observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT ||  viewType == AUTHORIZE || observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE){
            String acchead=CommonUtil.convertObjToStr(hash.get("AC_HD_ID"));
            String status="";
            if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT ||  viewType == AUTHORIZE || observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE){
                if(viewType == AUTHORIZE){
                    branchGroup = CommonUtil.convertObjToStr(hash.get("GROUP_ID"));
                }else{
                    branchGroup = CommonUtil.convertObjToStr(hash.get("BRANCH_GROUP"));
                    
                }
                txtGLGroup.setText(branchGroup);
            }
            hash.put("AC_HD_ID",acchead);
            txtAccountHead.setText(acchead);
            
        HashMap whereMap = new HashMap();
        whereMap.put(CommonConstants.MAP_WHERE,hash);
            whereMap.put(CommonConstants.MAP_NAME, "getSelectGLLimitTO");
        observable.populateData(whereMap);
            if(observable.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE){
            ClientUtil.enableDisable(panGLLimit,false);
        }
            
        populateTblActHeadDetails();
        setModified(true);
    }
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
                Integer slno=0;
                singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
                singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                singleAuthorizeMap.put(GROUP_ID,CommonUtil.convertObjToStr(txtGLGroup.getText()));
                singleAuthorizeMap.put(AC_HD_ID,CommonUtil.convertObjToStr(txtAccountHead.getText()));
                for(int i=0;i<tblActHeadDetails.getRowCount();i++){
                    String status=CommonUtil.convertObjToStr(tblActHeadDetails.getValueAt(i,7));
                    if(!status.equals("AUTHORIZED")){
                        slno= CommonUtil.convertObjToInt(tblActHeadDetails.getValueAt(i,0));
                    }
                }
                singleAuthorizeMap.put("SLNO",slno);
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
        txtLimit.setEnabled(flag);
        txtAnnualLimit.setEnabled(flag);
        txtOverDraw.setEnabled(flag);
        tdtFrmPeriod.setEnabled(flag);
        tdtToPeriod.setEnabled(flag);
        chkInterBranchTransAllowed.setEnabled(flag);
    }
    
    /** This method is used to reset the Fields of UI, after the row of data is saved
     *in the table **/
    private void resetFields(){
        txtLimit.setText("");
        txtAnnualLimit.setText("");
        txtOverDraw.setText("");
        chkInterBranchTransAllowed.setSelected(false);
        tdtFrmPeriod.setDateValue("");
        tdtToPeriod.setDateValue("");
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tbrGLLimit = new javax.swing.JToolBar();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace30 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace31 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace32 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace33 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace34 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panGLLimit = new com.see.truetransact.uicomponent.CPanel();
        panLimitDetails = new com.see.truetransact.uicomponent.CPanel();
        lblGLGroup = new com.see.truetransact.uicomponent.CLabel();
        lblAccountHead = new com.see.truetransact.uicomponent.CLabel();
        lblLimit = new com.see.truetransact.uicomponent.CLabel();
        txtLimit = new com.see.truetransact.uicomponent.CTextField();
        panSave = new com.see.truetransact.uicomponent.CPanel();
        btnNew1 = new com.see.truetransact.uicomponent.CButton();
        btnAdd = new com.see.truetransact.uicomponent.CButton();
        btnDelete1 = new com.see.truetransact.uicomponent.CButton();
        lblAnnualLimit = new com.see.truetransact.uicomponent.CLabel();
        lblOverDraw = new com.see.truetransact.uicomponent.CLabel();
        txtAnnualLimit = new com.see.truetransact.uicomponent.CTextField();
        txtOverDraw = new com.see.truetransact.uicomponent.CTextField();
        lblInterBranchTransAllowed = new com.see.truetransact.uicomponent.CLabel();
        chkInterBranchTransAllowed = new com.see.truetransact.uicomponent.CCheckBox();
        lblFrmPeriod = new com.see.truetransact.uicomponent.CLabel();
        lblToPeriod = new com.see.truetransact.uicomponent.CLabel();
        tdtFrmPeriod = new com.see.truetransact.uicomponent.CDateField();
        tdtToPeriod = new com.see.truetransact.uicomponent.CDateField();
        panGlGroup = new com.see.truetransact.uicomponent.CPanel();
        txtGLGroup = new com.see.truetransact.uicomponent.CTextField();
        btnGlGroup = new com.see.truetransact.uicomponent.CButton();
        panGlGroup1 = new com.see.truetransact.uicomponent.CPanel();
        txtAccountHead = new com.see.truetransact.uicomponent.CTextField();
        btnAccountHead = new com.see.truetransact.uicomponent.CButton();
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
        setMinimumSize(new java.awt.Dimension(830, 510));
        setPreferredSize(new java.awt.Dimension(830, 510));

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrGLLimit.add(btnNew);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrGLLimit.add(lblSpace29);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrGLLimit.add(btnEdit);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrGLLimit.add(lblSpace30);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
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

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrGLLimit.add(btnSave);

        lblSpace31.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace31.setText("     ");
        lblSpace31.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrGLLimit.add(lblSpace31);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrGLLimit.add(btnCancel);

        lblSpace3.setText("     ");
        tbrGLLimit.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrGLLimit.add(btnAuthorize);

        lblSpace32.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace32.setText("     ");
        lblSpace32.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrGLLimit.add(lblSpace32);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrGLLimit.add(btnException);

        lblSpace33.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace33.setText("     ");
        lblSpace33.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace33.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace33.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrGLLimit.add(lblSpace33);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrGLLimit.add(btnReject);

        lblSpace5.setText("     ");
        tbrGLLimit.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrGLLimit.add(btnPrint);

        lblSpace34.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace34.setText("     ");
        lblSpace34.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrGLLimit.add(lblSpace34);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
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

        panGLLimit.setLayout(new java.awt.GridBagLayout());

        panLimitDetails.setMinimumSize(new java.awt.Dimension(410, 274));
        panLimitDetails.setPreferredSize(new java.awt.Dimension(410, 274));
        panLimitDetails.setLayout(new java.awt.GridBagLayout());

        lblGLGroup.setText("GL Group");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLimitDetails.add(lblGLGroup, gridBagConstraints);

        lblAccountHead.setText("Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLimitDetails.add(lblAccountHead, gridBagConstraints);

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

        btnNew1.setText("New");
        btnNew1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNew1ActionPerformed(evt);
            }
        });
        panSave.add(btnNew1);

        btnAdd.setText("Save");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        panSave.add(btnAdd);

        btnDelete1.setText("Delete");
        btnDelete1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelete1ActionPerformed(evt);
            }
        });
        panSave.add(btnDelete1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
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
        txtAnnualLimit.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAnnualLimit.setValidation(new CurrencyValidation(14,2));
        txtAnnualLimit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAnnualLimitActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLimitDetails.add(txtAnnualLimit, gridBagConstraints);

        txtOverDraw.setEditable(false);
        txtOverDraw.setMinimumSize(new java.awt.Dimension(100, 21));
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
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 4, 4);
        panLimitDetails.add(chkInterBranchTransAllowed, gridBagConstraints);

        lblFrmPeriod.setText("From Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLimitDetails.add(lblFrmPeriod, gridBagConstraints);

        lblToPeriod.setText("To Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLimitDetails.add(lblToPeriod, gridBagConstraints);

        tdtFrmPeriod.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtFrmPeriodFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 3, 4, 4);
        panLimitDetails.add(tdtFrmPeriod, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 3, 4, 4);
        panLimitDetails.add(tdtToPeriod, gridBagConstraints);

        panGlGroup.setMinimumSize(new java.awt.Dimension(145, 23));
        panGlGroup.setPreferredSize(new java.awt.Dimension(138, 23));
        panGlGroup.setLayout(new java.awt.GridBagLayout());

        txtGLGroup.setMinimumSize(new java.awt.Dimension(120, 21));
        txtGLGroup.setPreferredSize(new java.awt.Dimension(150, 21));
        panGlGroup.add(txtGLGroup, new java.awt.GridBagConstraints());

        btnGlGroup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnGlGroup.setMaximumSize(new java.awt.Dimension(21, 21));
        btnGlGroup.setMinimumSize(new java.awt.Dimension(21, 21));
        btnGlGroup.setPreferredSize(new java.awt.Dimension(21, 21));
        btnGlGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGlGroupActionPerformed(evt);
            }
        });
        panGlGroup.add(btnGlGroup, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 2);
        panLimitDetails.add(panGlGroup, gridBagConstraints);

        panGlGroup1.setMinimumSize(new java.awt.Dimension(145, 23));
        panGlGroup1.setPreferredSize(new java.awt.Dimension(138, 23));
        panGlGroup1.setLayout(new java.awt.GridBagLayout());

        txtAccountHead.setMinimumSize(new java.awt.Dimension(120, 21));
        txtAccountHead.setPreferredSize(new java.awt.Dimension(150, 21));
        panGlGroup1.add(txtAccountHead, new java.awt.GridBagConstraints());

        btnAccountHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccountHead.setMaximumSize(new java.awt.Dimension(21, 21));
        btnAccountHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnAccountHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccountHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccountHeadActionPerformed(evt);
            }
        });
        panGlGroup1.add(btnAccountHead, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 2);
        panLimitDetails.add(panGlGroup1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 24);
        panGLLimit.add(panLimitDetails, gridBagConstraints);

        panActHeadDetails.setLayout(new java.awt.GridBagLayout());

        srpActHeadDetails.setMinimumSize(new java.awt.Dimension(390, 250));
        srpActHeadDetails.setPreferredSize(new java.awt.Dimension(450, 404));

        tblActHeadDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Act. Head", "Amt. Limit", "Ann. Limit", "Over Draw %", "From Period", "To Period", "Branch Trans."
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
    }// </editor-fold>//GEN-END:initComponents
    
    private void txtAnnualLimitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAnnualLimitActionPerformed
        // TODO add your handling code here:
        double limit=0.0;
        double annualLimit=0.0;
       limit =CommonUtil.convertObjToDouble(txtLimit.getText()).doubleValue();
       annualLimit=CommonUtil.convertObjToDouble(txtAnnualLimit.getText()).doubleValue();
       if(annualLimit<limit){
           ClientUtil.displayAlert("AnnualLimit should be greater than the limit amount");
           txtAnnualLimit.setText("");
           return;
       }
        
    }//GEN-LAST:event_txtAnnualLimitActionPerformed

    private void btnDelete1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete1ActionPerformed
        // TODO add your handling code here:
    ClientUtil.enableDisable(panLimitDetails,false);
    resetFields();
    btnNew1.setEnabled(true);
    String slno=CommonUtil.convertObjToStr(tblActHeadDetails.getValueAt(tblActHeadDetails.getSelectedRow(),0));
    String ACHD=CommonUtil.convertObjToStr(tblActHeadDetails.getValueAt(tblActHeadDetails.getSelectedRow(),1));
    observable.setTxtAccountHead(ACHD);
    observable.setSlNo(slno);
    observable.deleteRowData(tblActHeadDetails.getSelectedRow());
    btnDelete1.setEnabled(false);
    transnew="true";
    
    }//GEN-LAST:event_btnDelete1ActionPerformed

    private void tdtFrmPeriodFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtFrmPeriodFocusLost
        // TODO add your handling code here:
//        if(tdtFrmPeriod.getDateValue().length()>0){
//            java.util.Date dt=DateUtil.getDateMMDDYYYY(tdtFrmPeriod.getDateValue());
//            if(DateUtil.dateDiff(dt,currDt.clone())<0){
//                ClientUtil.displayAlert("FromDate should be lesser than or eaqual to current date");
//                return;
//            }
//        }
    }//GEN-LAST:event_tdtFrmPeriodFocusLost

    private void btnNew1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNew1ActionPerformed
        // TODO add your handling code here:
        String status="";
        for(int i=0;i<tblActHeadDetails.getRowCount();i++){
           status= CommonUtil.convertObjToStr(tblActHeadDetails.getValueAt(i,7));
        }
        if(status.equals("AUTHORIZED")){
            txtLimit.setEnabled(true);
            txtAnnualLimit.setEnabled(true);
            txtOverDraw.setEnabled(true);
            chkInterBranchTransAllowed.setEnabled(true);
            tdtFrmPeriod.setEnabled(true);
            tdtToPeriod.setEnabled(true);
            btnAdd.setEnabled(true);
            observable.setTable("new");
            btnNew1.setEnabled(false);
            btnAccountHead.setEnabled(true);
            btnSave.setEnabled(true);
            resetFields();
        }else{
            ClientUtil.displayAlert("Already Record exists for authorization can not add a new record");
            return;
        }
        transnew="false";
    }//GEN-LAST:event_btnNew1ActionPerformed

    private void btnAccountHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccountHeadActionPerformed
        // TODO add your handling code here:
        viewType="AccountHead";
        callView("AccountHead");
    }//GEN-LAST:event_btnAccountHeadActionPerformed

    private void btnGlGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGlGroupActionPerformed
        // TODO add your handling code here:
        viewType="GlGroup";
        callView("GlGroup");
    }//GEN-LAST:event_btnGlGroupActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        authorizeStatus(CommonConstants.STATUS_REJECTED);
          setButtonEnableDisable();
        btnSave.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        setButtonEnableDisable();
        btnSave.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
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
        setModified(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        observable.resetForm();
        populateTblActHeadDetails();
        ClientUtil.enableDisable(panGLLimit, false);
        setButtonEnableDisable();
        btnAdd.setEnabled(false);
        btnNew1.setEnabled(false);
        btnDelete1.setEnabled(false);
        setModified(false);
        tdtFrmPeriod.setDateValue("");
        tdtToPeriod.setDateValue("");
        viewType = "";
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnGlGroup.setEnabled(false);
        btnAccountHead.setEnabled(false);
        btnSave.setEnabled(false);
        
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView("Delete");
        setButtonEnableDisable();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnSave.setEnabled(true);
        //        callView(ClientConstants.ACTIONTYPE_DELETE);
        
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        btnNew1.setEnabled(true);
        btnAdd.setEnabled(false);
        btnDelete.setEnabled(false);
        setButtonEnableDisable();
        btnDelete.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        chkInterBranchTransAllowed.setEnabled(false);
        btnSave.setEnabled(true);
//        callView(ClientConstants.ACTIONTYPE_EDIT);
        callView("Edit");
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        if(transnew.equals("false")){
            ClientUtil.displayAlert("please save the details in the table");
            return;
        }
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
            HashMap hmap=new HashMap();
            
            int slno=1;
            if(observable.getTable().equals("new")){
            hmap.put("AC_HD_ID",txtAccountHead.getText());
            java.util.List list=ClientUtil.executeQuery("getMaxSlNo", hmap);
            
            if(list!=null && list.size()>0){
                hmap=(HashMap)list.get(0);
                slno=CommonUtil.convertObjToInt(hmap.get("SLNO"));
                slno=slno+1;
            }
            }else{
               slno=CommonUtil.convertObjToInt(observable.getSlNo()); 
            }
            rowData.add(CommonUtil.convertObjToStr(new Integer(slno)));
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
            if(!tdtFrmPeriod.getDateValue().equals("") || tdtFrmPeriod.getDateValue().length()!=0){
                rowData.add(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtFrmPeriod.getDateValue())));
            }else{
                rowData.add(null);
            }
             if(!tdtToPeriod.getDateValue().equals("") || tdtToPeriod.getDateValue().length()!=0){
                 rowData.add(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtToPeriod.getDateValue())));
             }else{
                 rowData.add(null);
             }
            if(observable.getTable().equals("new")){
                rowData.add(null);
            }else{
                rowData.add(CommonUtil.convertObjToStr(tblActHeadDetails.getValueAt(tblActHeadDetails.getSelectedRow(),7)));
            }
            if(chkInterBranchTransAllowed.isSelected()){
                rowData.add(YES);
            }else{
                rowData.add(NO);
            }
            
            observable.updateRowData(tblActHeadDetails.getSelectedRow(),rowData);
            populateTblActHeadDetails();
            resetFields();
            btnAccountHead.setEnabled(true);
            if( observable.getActionType()==ClientConstants.ACTIONTYPE_NEW){
                btnNew1.setEnabled(false);
            }else{
                btnNew1.setEnabled(true);
                btnDelete1.setEnabled(false);
            }
            btnAdd.setEnabled(false);
            setFieldsEditable(false);
            transnew="true";
        }
        
    }//GEN-LAST:event_btnAddActionPerformed
    
    private void tblActHeadDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblActHeadDetailsMouseClicked
        // TODO add your handling code here:
        btnNew1.setEnabled(false);
        btnDelete1.setEnabled(true);
        btnAdd.setEnabled(true);
        observable.setTable("update");
        updateOBFields();//To retain the Group Head
        setFieldsEditable(true);
        observable.populateSelectedRow(tblActHeadDetails.getSelectedRow());
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE || observable.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE){
            btnAdd.setEnabled(false);
            btnDelete1.setEnabled(false);
            setFieldsEditable(false);
        }else if(CommonUtil.convertObjToStr(tblActHeadDetails.getValueAt(tblActHeadDetails.getSelectedRow(),7)).equals("AUTHORIZED")){
            ClientUtil.enableDisable(panGLLimit,false);
            btnNew1.setEnabled(true);
            btnDelete1.setEnabled(false);
            btnAdd.setEnabled(false);
        }else{
            btnAdd.setEnabled(true);
            setFieldsEditable(true);
        }
        
    }//GEN-LAST:event_tblActHeadDetailsMouseClicked
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        btnNew1.setEnabled(true);
        txtGLGroup.setEnabled(true);
        txtAccountHead.setEnabled(true);
        btnAccountHead.setEnabled(true);
        btnGlGroup.setEnabled(true);
         txtLimit.setEnabled(true);
        txtAnnualLimit.setEnabled(true);
        txtOverDraw.setEnabled(true);
        chkInterBranchTransAllowed.setEnabled(true);
        tdtFrmPeriod.setEnabled(true);
        tdtToPeriod.setEnabled(true);
        btnAdd.setEnabled(true);
        observable.setTable("new");
        btnNew1.setEnabled(false);
        btnAccountHead.setEnabled(true);
        setButtonEnableDisable();
        transnew="false";
        btnSave.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        
        
    }//GEN-LAST:event_btnNewActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAccountHead;
    private com.see.truetransact.uicomponent.CButton btnAdd;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDelete1;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnGlGroup;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnNew1;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CCheckBox chkInterBranchTransAllowed;
    private com.see.truetransact.uicomponent.CLabel lblAccountHead;
    private com.see.truetransact.uicomponent.CLabel lblAnnualLimit;
    private com.see.truetransact.uicomponent.CLabel lblFrmPeriod;
    private com.see.truetransact.uicomponent.CLabel lblGLGroup;
    private com.see.truetransact.uicomponent.CLabel lblInterBranchTransAllowed;
    private com.see.truetransact.uicomponent.CLabel lblLimit;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblOverDraw;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace30;
    private com.see.truetransact.uicomponent.CLabel lblSpace31;
    private com.see.truetransact.uicomponent.CLabel lblSpace32;
    private com.see.truetransact.uicomponent.CLabel lblSpace33;
    private com.see.truetransact.uicomponent.CLabel lblSpace34;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblToPeriod;
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
    private com.see.truetransact.uicomponent.CPanel panGlGroup;
    private com.see.truetransact.uicomponent.CPanel panGlGroup1;
    private com.see.truetransact.uicomponent.CPanel panLimitDetails;
    private com.see.truetransact.uicomponent.CPanel panSave;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private com.see.truetransact.uicomponent.CScrollPane srpActHeadDetails;
    private com.see.truetransact.uicomponent.CTable tblActHeadDetails;
    private javax.swing.JToolBar tbrGLLimit;
    private com.see.truetransact.uicomponent.CDateField tdtFrmPeriod;
    private com.see.truetransact.uicomponent.CDateField tdtToPeriod;
    private com.see.truetransact.uicomponent.CTextField txtAccountHead;
    private com.see.truetransact.uicomponent.CTextField txtAnnualLimit;
    private com.see.truetransact.uicomponent.CTextField txtGLGroup;
    private com.see.truetransact.uicomponent.CTextField txtLimit;
    private com.see.truetransact.uicomponent.CTextField txtOverDraw;
    // End of variables declaration//GEN-END:variables
    
}
