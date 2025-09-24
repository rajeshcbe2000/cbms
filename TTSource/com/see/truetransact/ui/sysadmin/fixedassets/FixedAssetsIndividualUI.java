/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * FixedAssetsIndividualUI.java
 *
 * Created on January 20, 2005, 3:03 PM
 */

package com.see.truetransact.ui.sysadmin.fixedassets;

/**
 *
 * @author  ashokvijayakumar
 */
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uivalidation.NumericValidation;
import java.util.ResourceBundle;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uivalidation.DefaultValidation;
import com.see.truetransact.ui.common.authorize.AuthorizeUI ;

import java.util.Observable;
import java.util.Observer;
import java.util.HashMap;
import java.util.ArrayList;
import com.see.truetransact.commonutil.DateUtil;
import java.util.Date;
import com.see.truetransact.clientutil.ComboBoxModel;
//trans details
import com.see.truetransact.ui.common.transaction.TransactionUI;
import java.util.LinkedHashMap;
import java.util.List;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
//end..

public class FixedAssetsIndividualUI extends CInternalFrame implements Observer, UIMandatoryField{
    
    /** Vairable Declarations */
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.sysadmin.fixedassets.FixedAssetsIndividualRB", ProxyParameters.LANGUAGE);//Creating Instance For ResourceBundle-TokenConfigRB
    private FixedAssetsIndividualOB observable; //Reference for the Observable Class TokenConfigOB
    private FixedAssetsIndividualMRB objMandatoryRB = new FixedAssetsIndividualMRB();//Instance for the MandatoryResourceBundle
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private HashMap mandatoryMap;//Map for putting up MandatoryFields in the UI
    private String viewType = "";//Variable used to store ActionType(New,Edit,Delete)
    private final String AUTHORIZE="Authorize";//Variable used when btnAuthorize is clicked
    int updateTab=-1;
    boolean isFilled = false;
    private boolean updateMode = false;
    double amt = 0.0;
    TransactionUI transactionUI = new TransactionUI(); //trans details
    double amtBorrow=0.0; //trans details
    private Date currDt = null;
    /** Creates new form TokenConfigUI */
    public FixedAssetsIndividualUI() {
        currDt = ClientUtil.getCurrentDate();
        initForm();
        txtAmount.setEditable(false);
        //trans details
        panTrans.add(transactionUI);
        transactionUI.setSourceScreen("NEW_BORROW");
        observable.setTransactionOB(transactionUI.getTransactionOB());
        btnEdit.setVisible(false);
        //end..
    }
    
    /** Method which is used to initialize the form TokenConfig */
    private void initForm(){
        initComponents();
        setFieldNames();
        internationalize();
        observable = new FixedAssetsIndividualOB();
        observable.addObserver(this);
        observable.resetForm();
        initComponentData();
        setMandatoryHashMap();
        setHelpMessage();
        setMaxLengths();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panEmpTraning, getMandatoryHashMap());
        ClientUtil.enableDisable(panEmpTraning, false);
        setButtonEnableDisable();
        enableDisablePanButton(false);
        txtTrainingID.setEnabled(false);
        txtSlNo.setEnabled(false);
        
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
        cboBranchId.setName("cboBranchId");
        cboAssetDesc.setName("cboAssetDesc");
        cboAssetType.setName("cboAssetType");
        cboWarrPack.setName("cboWarrPack");
        lbSpace2.setName("lbSpace2");
        lblAssetDesc.setName("lblAssetDesc");
        lblMsg.setName("lblMsg");
        lblAssetNum.setName("lblAssetNum");
        lblAssetType.setName("lblAssetType");
        lblSpace1.setName("lblSpace1");
        lblSpace3.setName("lblSpace3");
        lblSpace5.setName("lblSpace5");
        lblCurrValue.setName("lblCurrValue");
        lblStatus.setName("lblStatus");
        lblFaceVal.setName("lblFaceVal");
        lblComp.setName("lblComp");
        lblBranchId.setName("lblBranchId");
        lblTrainingID.setName("lblTrainingID");
        lblOrdered.setName("lblOrdered");
        lblPurchased.setName("lblPurchased");
        lblQuantity.setName("lblQuantity");
        lblWarranty.setName("lblWarranty");
        mbrTokenConfig.setName("mbrTokenConfig");
        panStatus.setName("panStatus");
        panEmpList.setName("panEmpList");
        panEmpTraning.setName("panEmpTraning");
        tblEmpList.setName("tblEmpList");
        txtAssetNum.setName("txtAssetNum");
        txtCurrValue.setName("txtCurrValue");
        txtComp.setName("txtComp");
        txtTrainingID.setName("txtTrainingID");
        txtFaceVal.setName("txtFaceVal");
        txtQuantity.setName("txtQuantity");
        txtWarranty.setName("txtWarranty");
        tdtOrdered.setName("tdtOrdered");
        tdtPurchased.setName("tdtPurchased");
        panEmpBtn1.setName("panEmpBtn1");
        btnEmpDelete.setName("btnEmpDelete");
        btnEmpNew.setName("btnEmpNew");
        btnEmpSave.setName("btnEmpSave");
        lblInvoiceNo.setName("lblInvoiceNo");
        txtInvoiceNo.setName("txtInvoiceNo");
        lblSlNo.setName("lblSlNo");
        txtSlNo.setName("txtSlNo");
        lblFloor.setName("lblFloor");
        txtFloor.setName("txtFloor");
        lblDepart.setName("lblDepart");
        cboDepart.setName("cboDepart");
        lblWarrantyExpired.setName("lblWarrantyExpired");
        lblWarrantyExpiryDate.setName("lblWarrantyExpiryDate");
    }
    
 /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        lblBranchId.setName(resourceBundle.getString("lblBranchId"));
        lblTrainingID.setText(resourceBundle.getString("lblTrainingID"));
        lblAssetType.setText(resourceBundle.getString("lblAssetType"));
        lblAssetDesc.setText(resourceBundle.getString("lblAssetDesc"));
        lblQuantity.setText(resourceBundle.getString("lblQuantity"));
        lblOrdered.setText(resourceBundle.getString("lblOrdered"));
        lblPurchased.setText(resourceBundle.getString("lblPurchased"));
        lblComp.setText(resourceBundle.getString("lblComp"));
        lblWarranty.setText(resourceBundle.getString("lblWarranty"));
        lblFaceVal.setText(resourceBundle.getString("lblFaceVal"));
        lblCurrValue.setText(resourceBundle.getString("lblCurrValue"));
        lblAssetNum.setText(resourceBundle.getString("lblAssetNum"));
        lblInvoiceNo.setText(resourceBundle.getString("lblInvoiceNo"));
        lblSlNo.setText(resourceBundle.getString("lblSlNo"));
        lblFloor.setText(resourceBundle.getString("lblFloor"));
        lblDepart.setText(resourceBundle.getString("lblDepart"));
        lblWarrantyExpired.setText(resourceBundle.getString("lblWarrantyExpired")); 
    }
    
    /*Setting model to the combobox cboTokenType  */
    private void initComponentData() {
        try{
            cboAssetDesc.setModel(observable.getCbmassetDesc());
            cboAssetType.setModel(observable.getCbmassetType());
            cboWarrPack.setModel(observable.getCbmwarrVal());
            cboBranchId.setModel(observable.getCbmBranchIdVal());
            cboDepart.setModel(observable.getCbmDepart());
        }catch(ClassCastException e){
            parseException.logException(e,true);
        }
    }
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        if (observable.getCbmassetDesc()!=null)
            cboBranchId.setSelectedItem(observable.getCboBranchId());
        cboDepart.setSelectedItem(observable.getCboDepart());
        cboWarrPack.setModel(observable.getCbmwarrVal());
        txtAssetNum.setText(observable.getAssetNum());
        txtInvoiceNo.setText(observable.getTxtInvoiceNo());
        txtCurrValue.setText(observable.getCurrVal());
        txtFaceVal.setText(observable.getFaceVal());
        txtQuantity.setText(observable.getQuantity());
        txtTrainingID.setText(observable.getAssetIndID());
        tdtOrdered.setDateValue(observable.getOrderedDt());
        tdtPurchased.setDateValue(observable.getPurchasedDt());
        //lblStatus.setText(observable.getLblStatus());
        txtWarranty.setText(observable.getWarranty());
        tblEmpList.setModel(observable.getTblEmpDetails());
        txtComp.setText(observable.getCompany());
        cboAssetDesc.setModel(observable.getCbmassetDesc());
        txtSlNo.setText(observable.getTxtSlNo());
        txtFloor.setText(observable.getFloor());
    }
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setAssetIndID(txtTrainingID.getText());
        observable.setCboassetType((String) cboAssetType.getSelectedItem());
        observable.setCboassetDesc((String) cboAssetDesc.getSelectedItem());
        observable.setAssetNum(txtAssetNum.getText());
        observable.setCurrVal(txtCurrValue.getText());
        observable.setFaceVal(txtFaceVal.getText());
        observable.setTxtSlNo(txtSlNo.getText());
        observable.setFloor(txtFloor.getText());
        observable.setOrderedDt(tdtOrdered.getDateValue());
        observable.setPurchasedDt(tdtPurchased.getDateValue());
        observable.setQuantity(txtQuantity.getText());
        observable.setTxtInvoiceNo(txtInvoiceNo.getText());
        observable.setWarranty(txtWarranty.getText());
        observable.setCompany(txtComp.getText());
        observable.setCbowarVal((String) cboWarrPack.getSelectedItem());
        observable.setCboBranchId((String)cboBranchId.getSelectedItem());
        observable.setCboDepart((String)cboDepart.getSelectedItem());
        observable.setTxtAmount(CommonUtil.convertObjToDouble(txtAmount.getText()));
    }
    
 /* Auto Generated Method - setMandatoryHashMap()
  
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
  
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboAssetType", new Boolean(true));
        mandatoryMap.put("cboAssetDesc", new Boolean(true));
        mandatoryMap.put("txtQuantity", new Boolean(true));
        mandatoryMap.put("tdtOrdered", new Boolean(true));
        mandatoryMap.put("tdtPurchased", new Boolean(true));
        mandatoryMap.put("txtComp", new Boolean(true));
        mandatoryMap.put("txtWarranty", new Boolean(true));
        mandatoryMap.put("txtFaceVal", new Boolean(true));
        mandatoryMap.put("txtCurrValue", new Boolean(true));
        mandatoryMap.put("txtAssetNum", new Boolean(true));
        mandatoryMap.put("txtInvoiceNo",new Boolean(true));
        mandatoryMap.put("cboBranchId",new Boolean(false));
        mandatoryMap.put("txtFloor", new Boolean(true));
        mandatoryMap.put("cboDepart",new Boolean(false));
        mandatoryMap.put("txtSlNo",new Boolean(true));
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
        FixedAssetsIndividualMRB objMandatoryRB = new FixedAssetsIndividualMRB();
        cboAssetType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboAssetType"));
        cboAssetDesc.setHelpMessage(lblMsg, objMandatoryRB.getString("cboAssetDesc"));
        txtInvoiceNo.setHelpMessage(lblMsg,objMandatoryRB.getString("txtInvoiceNo"));
        txtQuantity.setHelpMessage(lblMsg, objMandatoryRB.getString("txtQuantity"));
        tdtOrdered.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtOrdered"));
        tdtPurchased.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtPurchased"));
        txtComp.setHelpMessage(lblMsg, objMandatoryRB.getString("txtComp"));
        txtWarranty.setHelpMessage(lblMsg, objMandatoryRB.getString("txtWarranty"));
        txtFaceVal.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFaceVal"));
        txtFloor.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFloor"));
        txtSlNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSlNo"));
        txtCurrValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCurrValue"));
        txtAssetNum.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAssetNum"));
        cboWarrPack.setHelpMessage(lblMsg, objMandatoryRB.getString("cboWarrPack"));
        cboBranchId.setHelpMessage(lblMsg,objMandatoryRB.getString("cboBranchId"));
        cboDepart.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDepart"));
    }
    
    /** Used to set Maximum possible lenghts for TextFields */
    private void setMaxLengths(){
        txtAssetNum.setAllowAll(true);
        txtSlNo.setAllowAll(true);
        txtWarranty.setValidation(new NumericValidation(3,0));
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
        txtAmount.setEditable(false);
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
    
    /* Calls the execute method of TerminalOB to do insertion or updation or deletion */
    private void saveAction(){
        int quantity = Integer.parseInt(txtQuantity.getText());
        if(tblEmpList.getRowCount()!=quantity){
            ClientUtil.showMessageWindow("Number of Rows In Table Should BE Equal To Quantity"+" "+"ie"+" "+quantity);
            return;
        }
        observable.doAction();
        if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
            HashMap lockMap = new HashMap();
            ArrayList lst = new ArrayList();
            lst.add("FIXED_INDIVIDUAL_ID");
            lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
            
            if (observable.getProxyReturnMap()!=null) {
                if (observable.getProxyReturnMap().containsKey("FIXED_INDIVIDUAL_ID")) {
                    lockMap.put("FIXED_INDIVIDUAL_ID", observable.getProxyReturnMap().get("FIXED_INDIVIDUAL_ID"));
                }
            }
            if(observable.getResult()==ClientConstants.ACTIONTYPE_EDIT){
                lockMap.put("FIXED_INDIVIDUAL_ID", observable.getAssetIndID());
            }
            System.out.println("observable.getProxyReturnMap()>>>>>"+observable.getProxyReturnMap());
            if(observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && observable.getActionType() != ClientConstants.ACTIONTYPE_EDIT) {
                displayTransDetail(observable.getProxyReturnMap());//trans details
            }
            
            setEditLockMap(lockMap);
            setEditLock();
            deletescreenLock();
            settings();
        }
    }
    
    private void deletescreenLock(){
        HashMap map=new HashMap();
        map.put("USER_ID",ProxyParameters.USER_ID);
        map.put("TRANS_DT", currDt.clone());
        map.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
        ClientUtil.execute("DELETE_SCREEN_LOCK", map);
    }
    
    /* set the screen after the updation,insertion, deletion */
    private void settings(){
        observable.resetForm();
        setHelpBtnEnableDisable(false);
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panEmpTraning, false);
        setButtonEnableDisable();
        observable.setResultStatus();
        lblStatus.setText(observable.getLblStatus());
    }
    
    /* Method used to showPopup ViewAll by Executing a Query */
    private void callView(String currAction) {
        viewType = currAction;
        HashMap viewMap = new HashMap();
        if (currAction.equalsIgnoreCase("Edit")){
            HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            map.put("CURR_DT",currDt.clone());
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getFixedIndividualDetailsEdit");
        }else if(currAction.equalsIgnoreCase("Delete")){
            HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getFixedIndividualDetailsDelete");
        }else if(currAction.equalsIgnoreCase("Enquiry")){
            HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getFixedIndividualDetailsView");
        }
        new ViewAll(this,viewMap).show();
    }
    
    /* Fills up the HashMap with data when user selects the row in ViewAll screen  */
    public void fillData(Object  map) {
        try {
            isFilled = true;
            setModified(true);
            HashMap hash = (HashMap) map;
            if (viewType != null) {
                if (viewType.equals(ClientConstants.ACTION_STATUS[2]) ||
                viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) ||  viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                    HashMap where = new HashMap();
                    where.put("FID_ID", hash.get("FID_ID"));
                    hash.put("VIEW_TYPE",viewType);
                    where.put(CommonConstants.BRANCH_ID,TrueTransactMain.BRANCH_ID);
                    hash.put(CommonConstants.MAP_WHERE, where);
                    where = null;
                    observable.getData(hash);
                    if (observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                        ClientUtil.enableDisable(panEmpTraning, false);
                        enableDisablePanButton(false);
                    }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                        ClientUtil.enableDisable(panEmpTraning, true);
                        enableDisablePanButton(true);
                    }
                    setButtonEnableDisable();
                    if(viewType.equals(AUTHORIZE) ){
                        ClientUtil.enableDisable(panEmpTraning, false);
                        btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                        btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                        btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                    }
                }
            }
            if(viewType.equals(ClientConstants.ACTION_STATUS[2]) || viewType.equals(AUTHORIZE)) {
                HashMap screenMap = new HashMap();
                screenMap.put("TRANS_ID",hash.get("FID_ID"));
                screenMap.put("USER_ID",ProxyParameters.USER_ID);
                screenMap.put("TRANS_DT", currDt.clone());
                screenMap.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
                java.util.List lst=ClientUtil.executeQuery("selectauthorizationLock", screenMap);
                if(lst !=null && lst.size()>0) {
                    screenMap=null;
                    StringBuffer open=new StringBuffer();
                    for(int i=0;i<lst.size();i++){
                        screenMap=(HashMap)lst.get(i);
                        open.append("\n"+"User Id  :"+" ");
                        open.append(CommonUtil.convertObjToStr(screenMap.get("OPEN_BY"))+"\n");
                        open.append("Mode Of Operation  :" +" ");
                        open.append(CommonUtil.convertObjToStr(screenMap.get("MODE_OF_OPERATION"))+" ");
                        btnSave.setEnabled(false);
                        ClientUtil.enableDisable(panEmpTraning,false);
                        enableDisablePanButton(false);
                    }
                    ClientUtil.showMessageWindow("already open by"+open);
                    return;
                }
                else{
                    hash.put("TRANS_ID",hash.get("FID_ID"));
                    if(viewType.equals(ClientConstants.ACTION_STATUS[2]))
                        hash.put("MODE_OF_OPERATION","EDIT");
                    if(viewType==AUTHORIZE)
                        hash.put("MODE_OF_OPERATION","AUTHORIZE");
                    hash.put("TRANS_ID",CommonUtil.convertObjToStr(hash.get("ACT_NUM")));
                    hash.put("USER_ID",TrueTransactMain.USER_ID);
                    hash.put("TRANS_DT", currDt.clone());
                    hash.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
                    ClientUtil.execute("insertauthorizationLock", hash);
                }
            }
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                String lockedBy = "";
                HashMap Lockmap = new HashMap();
                Lockmap.put("SCREEN_ID", getScreenID());
                Lockmap.put("RECORD_KEY", CommonUtil.convertObjToStr(hash.get("EMP_TRAINING_ID")));
                Lockmap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
                System.out.println("Record Key Map : " + Lockmap);
                java.util.List lstLock = ClientUtil.executeQuery("selectEditLock", Lockmap);
                if (lstLock.size() > 0) {
                    lockedBy = CommonUtil.convertObjToStr(lstLock.get(0));
                    if (!lockedBy.equals(ProxyParameters.USER_ID)) {
                        btnSave.setEnabled(false);
                        ClientUtil.enableDisable(panEmpTraning,false);
                        enableDisablePanButton(false);
                    } else {
                        btnSave.setEnabled(true);
                        ClientUtil.enableDisable(panEmpTraning,true);
                        enableDisablePanButton(true);
                    }
                } else {
                    btnSave.setEnabled(true);
                    ClientUtil.enableDisable(panEmpTraning,true);
                    enableDisablePanButton(true);
                }
                setOpenForEditBy(lockedBy);
                if (lockedBy.length() > 0 && !lockedBy.equals(ProxyParameters.USER_ID)) {
                    String data = getLockDetails(lockedBy, getScreenID()) ;
                    ClientUtil.showMessageWindow("Selected Record is Opened/Modified by " + lockedBy + data.toString());
                    btnSave.setEnabled(false);
                    ClientUtil.enableDisable(panEmpTraning,false);
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    private String getLockDetails(String lockedBy, String screenId){
        HashMap map = new HashMap();
        StringBuffer data = new StringBuffer() ;
        map.put("LOCKED_BY", lockedBy) ;
        map.put("SCREEN_ID", screenId) ;
        java.util.List lstLock = ClientUtil.executeQuery("getLockedDetails", map);
        map.clear();
        if(lstLock.size() > 0){
            map = (HashMap)(lstLock.get(0));
            data.append("\nLog in Time : ").append(map.get("LOCKED_TIME")) ;
            data.append("\nIP Address : ").append(map.get("IP_ADDR")) ;
            data.append("\nBranch : ").append(map.get("BRANCH_ID"));
        }
        lstLock = null ;
        map = null ;
        return data.toString();
    }
    
    /** This will show the alertwindow **/
    private int showAlertWindow(String alertMsg) throws Exception {
        int optionSelected = 1;
        String[] options = {resourceBundle.getString("cDialogOK")};
        optionSelected = COptionPane.showOptionDialog(null,resourceBundle.getString(alertMsg), CommonConstants.INFORMATIONTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        return optionSelected;
    }
    
    /** Method used to do Required operation when user clicks btnAuthorize,btnReject or btnReject **/
    public void authorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled){
            ArrayList arrList = new ArrayList();
            HashMap authorizeMap = new HashMap();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("FID_ID", observable.getAssetIndID());
            singleAuthorizeMap.put("AUTH_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTH_DT",ClientUtil.getCurrentDateWithTime());
            singleAuthorizeMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            arrList.add(singleAuthorizeMap);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(authorizeMap,observable.getAssetIndID());
            viewType = "";
            super.setOpenForEditBy(observable.getStatusBy());
            singleAuthorizeMap = null;
            arrList = null;
            authorizeMap = null;
        }else {
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            mapParam.put(CommonConstants.MAP_NAME, "getFixedIndividualAuthorize");
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }
    public void authorize(HashMap map,String id) {
        System.out.println("Authorize Map : " + map);
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.set_authorizeMap(map);
            observable.doAction();
            if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
                super.setOpenForEditBy(observable.getStatusBy());
                super.removeEditLock(id);
            }
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }
    
    /** Method to make HelpButton btnUserId enable or disable..accroding to Edit,Delete,New,Save Button Clicked */
    private void setHelpBtnEnableDisable(boolean flag){
        //                btnE.setEnabled(flag);
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
    
    private void enableDisablePanButton(boolean flag){
        btnEmpDelete.setEnabled(flag);
        btnEmpSave.setEnabled(flag);
        btnEmpNew.setEnabled(flag);
    }
    
    private void enableDisableButton(boolean flag){
        txtFaceVal.setEnabled(flag);
        txtCurrValue.setEnabled(flag);
        txtAssetNum.setEnabled(flag);
        cboBranchId.setEnabled(flag);
        cboDepart.setEnabled(flag);
        txtSlNo.setEnabled(flag);
        txtFloor.setEnabled(flag);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panEmpTraning = new com.see.truetransact.uicomponent.CPanel();
        panEmpList = new com.see.truetransact.uicomponent.CPanel();
        panTrainingData1 = new com.see.truetransact.uicomponent.CPanel();
        cboBranchId = new com.see.truetransact.uicomponent.CComboBox();
        lblBranchId = new com.see.truetransact.uicomponent.CLabel();
        txtFaceVal = new com.see.truetransact.uicomponent.CTextField();
        lblFaceVal = new com.see.truetransact.uicomponent.CLabel();
        txtCurrValue = new com.see.truetransact.uicomponent.CTextField();
        lblCurrValue = new com.see.truetransact.uicomponent.CLabel();
        panEmpBtn1 = new com.see.truetransact.uicomponent.CPanel();
        btnEmpNew = new com.see.truetransact.uicomponent.CButton();
        btnEmpSave = new com.see.truetransact.uicomponent.CButton();
        btnEmpDelete = new com.see.truetransact.uicomponent.CButton();
        lblAssetNum = new com.see.truetransact.uicomponent.CLabel();
        txtAssetNum = new com.see.truetransact.uicomponent.CTextField();
        lblSlNo = new com.see.truetransact.uicomponent.CLabel();
        cboDepart = new com.see.truetransact.uicomponent.CComboBox();
        lblDepart = new com.see.truetransact.uicomponent.CLabel();
        txtFloor = new com.see.truetransact.uicomponent.CTextField();
        lblFloor = new com.see.truetransact.uicomponent.CLabel();
        txtSlNo = new com.see.truetransact.uicomponent.CTextField();
        srpTokenLost = new com.see.truetransact.uicomponent.CScrollPane();
        tblEmpList = new com.see.truetransact.uicomponent.CTable();
        panTrainingData = new com.see.truetransact.uicomponent.CPanel();
        tdtPurchased = new com.see.truetransact.uicomponent.CDateField();
        tdtOrdered = new com.see.truetransact.uicomponent.CDateField();
        txtQuantity = new com.see.truetransact.uicomponent.CTextField();
        txtTrainingID = new com.see.truetransact.uicomponent.CTextField();
        lblTrainingID = new com.see.truetransact.uicomponent.CLabel();
        lblQuantity = new com.see.truetransact.uicomponent.CLabel();
        lblOrdered = new com.see.truetransact.uicomponent.CLabel();
        lblPurchased = new com.see.truetransact.uicomponent.CLabel();
        lblWarranty = new com.see.truetransact.uicomponent.CLabel();
        cboAssetDesc = new com.see.truetransact.uicomponent.CComboBox();
        lblAssetDesc = new com.see.truetransact.uicomponent.CLabel();
        txtComp = new com.see.truetransact.uicomponent.CTextField();
        lblComp = new com.see.truetransact.uicomponent.CLabel();
        cboWarrPack = new com.see.truetransact.uicomponent.CComboBox();
        txtWarranty = new com.see.truetransact.uicomponent.CTextField();
        cboAssetType = new com.see.truetransact.uicomponent.CComboBox();
        lblAssetType = new com.see.truetransact.uicomponent.CLabel();
        lblInvoiceNo = new com.see.truetransact.uicomponent.CLabel();
        txtInvoiceNo = new com.see.truetransact.uicomponent.CTextField();
        lblWarrantyExpired = new com.see.truetransact.uicomponent.CLabel();
        lblWarrantyExpiryDate = new com.see.truetransact.uicomponent.CLabel();
        lblAmount = new javax.swing.JLabel();
        txtAmount = new javax.swing.JTextField();
        panTrans = new com.see.truetransact.uicomponent.CPanel();
        tbrTokenConfig = new com.see.truetransact.uicomponent.CToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lbSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace34 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace35 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lbSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace36 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace37 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace38 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace39 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrTokenConfig = new com.see.truetransact.uicomponent.CMenuBar();
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
        setMaximumSize(new java.awt.Dimension(800, 625));
        setMinimumSize(new java.awt.Dimension(800, 625));
        setPreferredSize(new java.awt.Dimension(800, 625));

        panEmpTraning.setMaximumSize(new java.awt.Dimension(650, 550));
        panEmpTraning.setMinimumSize(new java.awt.Dimension(650, 650));
        panEmpTraning.setPreferredSize(new java.awt.Dimension(650, 650));
        panEmpTraning.setLayout(new java.awt.GridBagLayout());

        panEmpList.setMaximumSize(new java.awt.Dimension(1000, 450));
        panEmpList.setMinimumSize(new java.awt.Dimension(800, 240));
        panEmpList.setPreferredSize(new java.awt.Dimension(800, 240));
        panEmpList.setLayout(new java.awt.GridBagLayout());

        panTrainingData1.setBorder(javax.swing.BorderFactory.createTitledBorder("Asset List"));
        panTrainingData1.setMaximumSize(new java.awt.Dimension(350, 350));
        panTrainingData1.setMinimumSize(new java.awt.Dimension(335, 160));
        panTrainingData1.setPreferredSize(new java.awt.Dimension(335, 160));
        panTrainingData1.setLayout(new java.awt.GridBagLayout());

        cboBranchId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 1, 25);
        panTrainingData1.add(cboBranchId, gridBagConstraints);

        lblBranchId.setText("Branch Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panTrainingData1.add(lblBranchId, gridBagConstraints);

        txtFaceVal.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFaceVal.setNextFocusableComponent(tdtOrdered);
        txtFaceVal.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 1, 25);
        panTrainingData1.add(txtFaceVal, gridBagConstraints);

        lblFaceVal.setText("Face Value");
        lblFaceVal.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panTrainingData1.add(lblFaceVal, gridBagConstraints);

        txtCurrValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCurrValue.setNextFocusableComponent(txtFaceVal);
        txtCurrValue.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 1, 25);
        panTrainingData1.add(txtCurrValue, gridBagConstraints);

        lblCurrValue.setText("Current Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panTrainingData1.add(lblCurrValue, gridBagConstraints);

        panEmpBtn1.setLayout(new java.awt.GridBagLayout());

        btnEmpNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnEmpNew.setToolTipText("New");
        btnEmpNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnEmpNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnEmpNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnEmpNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmpNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmpBtn1.add(btnEmpNew, gridBagConstraints);

        btnEmpSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnEmpSave.setToolTipText("Save");
        btnEmpSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnEmpSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnEmpSave.setName("btnContactNoAdd");
        btnEmpSave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnEmpSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmpSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmpBtn1.add(btnEmpSave, gridBagConstraints);

        btnEmpDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnEmpDelete.setToolTipText("Delete");
        btnEmpDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnEmpDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnEmpDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnEmpDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmpDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmpBtn1.add(btnEmpDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 13);
        panTrainingData1.add(panEmpBtn1, gridBagConstraints);

        lblAssetNum.setText("Asset Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panTrainingData1.add(lblAssetNum, gridBagConstraints);

        txtAssetNum.setMaxLength(128);
        txtAssetNum.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAssetNum.setName("txtCompany");
        txtAssetNum.setNextFocusableComponent(txtQuantity);
        txtAssetNum.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 1, 25);
        panTrainingData1.add(txtAssetNum, gridBagConstraints);

        lblSlNo.setText("Serial Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panTrainingData1.add(lblSlNo, gridBagConstraints);

        cboDepart.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 1, 25);
        panTrainingData1.add(cboDepart, gridBagConstraints);

        lblDepart.setText("Department");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panTrainingData1.add(lblDepart, gridBagConstraints);

        txtFloor.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFloor.setNextFocusableComponent(txtFaceVal);
        txtFloor.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 1, 25);
        panTrainingData1.add(txtFloor, gridBagConstraints);

        lblFloor.setText("Floor");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panTrainingData1.add(lblFloor, gridBagConstraints);

        txtSlNo.setMaxLength(128);
        txtSlNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSlNo.setName("txtCompany");
        txtSlNo.setNextFocusableComponent(txtQuantity);
        txtSlNo.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 1, 25);
        panTrainingData1.add(txtSlNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 80;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(56, 14, 57, 21);
        panEmpList.add(panTrainingData1, gridBagConstraints);

        srpTokenLost.setMaximumSize(new java.awt.Dimension(310, 450));
        srpTokenLost.setMinimumSize(new java.awt.Dimension(390, 230));
        srpTokenLost.setPreferredSize(new java.awt.Dimension(390, 230));

        tblEmpList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "SL No", "Warranty", "Face Val", "Curr Val", "Asset Num"
            }
        ));
        tblEmpList.setMaximumSize(new java.awt.Dimension(2147483647, 1000));
        tblEmpList.setMinimumSize(new java.awt.Dimension(385, 420));
        tblEmpList.setPreferredScrollableViewportSize(new java.awt.Dimension(300, 1000));
        tblEmpList.setPreferredSize(new java.awt.Dimension(385, 420));
        tblEmpList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblEmpListMousePressed(evt);
            }
        });
        srpTokenLost.setViewportView(tblEmpList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(29, 4, 30, 9);
        panEmpList.add(srpTokenLost, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panEmpTraning.add(panEmpList, gridBagConstraints);

        panTrainingData.setBorder(javax.swing.BorderFactory.createTitledBorder("Individual Asset Details"));
        panTrainingData.setMaximumSize(new java.awt.Dimension(1000, 400));
        panTrainingData.setMinimumSize(new java.awt.Dimension(800, 120));
        panTrainingData.setPreferredSize(new java.awt.Dimension(800, 120));
        panTrainingData.setLayout(new java.awt.GridBagLayout());

        tdtPurchased.setMinimumSize(new java.awt.Dimension(101, 19));
        tdtPurchased.setName("tdtToDate");
        tdtPurchased.setNextFocusableComponent(btnEmpNew);
        tdtPurchased.setPreferredSize(new java.awt.Dimension(101, 19));
        tdtPurchased.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtPurchasedFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 11, 0, 1);
        panTrainingData.add(tdtPurchased, gridBagConstraints);

        tdtOrdered.setMinimumSize(new java.awt.Dimension(101, 19));
        tdtOrdered.setName("tdtFromDate");
        tdtOrdered.setNextFocusableComponent(tdtPurchased);
        tdtOrdered.setPreferredSize(new java.awt.Dimension(101, 19));
        tdtOrdered.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtOrderedFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 2, 0);
        panTrainingData.add(tdtOrdered, gridBagConstraints);

        txtQuantity.setMinimumSize(new java.awt.Dimension(100, 21));
        txtQuantity.setNextFocusableComponent(txtFaceVal);
        txtQuantity.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 2, 0);
        panTrainingData.add(txtQuantity, gridBagConstraints);

        txtTrainingID.setEditable(false);
        txtTrainingID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTrainingID.setNextFocusableComponent(cboBranchId);
        txtTrainingID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTrainingIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 2, 0);
        panTrainingData.add(txtTrainingID, gridBagConstraints);

        lblTrainingID.setText("Asset Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 2, 0);
        panTrainingData.add(lblTrainingID, gridBagConstraints);

        lblQuantity.setText("Quantity");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 2, 0);
        panTrainingData.add(lblQuantity, gridBagConstraints);

        lblOrdered.setText("Ordered Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 2, 0);
        panTrainingData.add(lblOrdered, gridBagConstraints);

        lblPurchased.setText("Purchased Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 2, 0);
        panTrainingData.add(lblPurchased, gridBagConstraints);

        lblWarranty.setText("Warranty");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 2, 0);
        panTrainingData.add(lblWarranty, gridBagConstraints);

        cboAssetDesc.setMinimumSize(new java.awt.Dimension(100, 21));
        cboAssetDesc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboAssetDescActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 2, 0);
        panTrainingData.add(cboAssetDesc, gridBagConstraints);

        lblAssetDesc.setText("Asset Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 2, 0);
        panTrainingData.add(lblAssetDesc, gridBagConstraints);

        txtComp.setMaxLength(128);
        txtComp.setMinimumSize(new java.awt.Dimension(100, 21));
        txtComp.setName("txtCompany");
        txtComp.setNextFocusableComponent(txtQuantity);
        txtComp.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 2, 0);
        panTrainingData.add(txtComp, gridBagConstraints);

        lblComp.setText("Company Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 2, 0);
        panTrainingData.add(lblComp, gridBagConstraints);

        cboWarrPack.setMinimumSize(new java.awt.Dimension(75, 22));
        cboWarrPack.setPopupWidth(150);
        cboWarrPack.setPreferredSize(new java.awt.Dimension(75, 22));
        cboWarrPack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboWarrPackActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 2, 8);
        panTrainingData.add(cboWarrPack, gridBagConstraints);

        txtWarranty.setMaxLength(128);
        txtWarranty.setMinimumSize(new java.awt.Dimension(100, 21));
        txtWarranty.setName("txtCompany");
        txtWarranty.setNextFocusableComponent(txtQuantity);
        txtWarranty.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 2, 0);
        panTrainingData.add(txtWarranty, gridBagConstraints);

        cboAssetType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboAssetType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboAssetTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 2, 0);
        panTrainingData.add(cboAssetType, gridBagConstraints);

        lblAssetType.setText("Asset Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 2, 0);
        panTrainingData.add(lblAssetType, gridBagConstraints);

        lblInvoiceNo.setText("Invoice No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 2, 0);
        panTrainingData.add(lblInvoiceNo, gridBagConstraints);

        txtInvoiceNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInvoiceNo.setNextFocusableComponent(txtFaceVal);
        txtInvoiceNo.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 2, 0);
        panTrainingData.add(txtInvoiceNo, gridBagConstraints);

        lblWarrantyExpired.setText("Warranty Expiry Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 2, 0);
        panTrainingData.add(lblWarrantyExpired, gridBagConstraints);

        lblWarrantyExpiryDate.setText("        ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 65;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 2, 4);
        panTrainingData.add(lblWarrantyExpiryDate, gridBagConstraints);

        lblAmount.setFont(new java.awt.Font("DejaVu Sans", 0, 12)); // NOI18N
        lblAmount.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        panTrainingData.add(lblAmount, gridBagConstraints);

        txtAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAmount.setPreferredSize(new java.awt.Dimension(100, 21));
        txtAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAmountActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 2, 0);
        panTrainingData.add(txtAmount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panEmpTraning.add(panTrainingData, gridBagConstraints);

        panTrans.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panTrans.setMaximumSize(new java.awt.Dimension(800, 190));
        panTrans.setMinimumSize(new java.awt.Dimension(800, 190));
        panTrans.setPreferredSize(new java.awt.Dimension(800, 190));
        panTrans.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 7;
        panEmpTraning.add(panTrans, gridBagConstraints);

        getContentPane().add(panEmpTraning, java.awt.BorderLayout.CENTER);

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
        tbrTokenConfig.add(btnView);

        lbSpace3.setText("     ");
        tbrTokenConfig.add(lbSpace3);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnNew);

        lblSpace34.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace34.setText("     ");
        lblSpace34.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace34);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnEdit);

        lblSpace35.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace35.setText("     ");
        lblSpace35.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace35.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace35.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace35);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnDelete);

        lbSpace2.setText("     ");
        tbrTokenConfig.add(lbSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnSave);

        lblSpace36.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace36.setText("     ");
        lblSpace36.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace36.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace36.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace36);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnCancel);

        lblSpace3.setText("     ");
        tbrTokenConfig.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnAuthorize);

        lblSpace37.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace37.setText("     ");
        lblSpace37.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace37.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace37.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace37);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnException);

        lblSpace38.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace38.setText("     ");
        lblSpace38.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace38.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace38.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace38);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnReject);

        lblSpace5.setText("     ");
        tbrTokenConfig.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrTokenConfig.add(btnPrint);

        lblSpace39.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace39.setText("     ");
        lblSpace39.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace39.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace39.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace39);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnClose);

        getContentPane().add(tbrTokenConfig, java.awt.BorderLayout.NORTH);

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

        mbrTokenConfig.add(mnuProcess);

        setJMenuBar(mbrTokenConfig);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void txtTrainingIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTrainingIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTrainingIDActionPerformed
    
    private void txtAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAmountActionPerformed
    
    private void tblEmpListMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEmpListMousePressed
        // TODO add your handling code here:
        observable.notifyObservers();
        updateOBFields();
        updateMode = true;
        updateTab= tblEmpList.getSelectedRow();
        observable.setNewData(false);
        String st=CommonUtil.convertObjToStr(tblEmpList.getValueAt(tblEmpList.getSelectedRow(),0));
        observable.populateLeaveDetails(st);
        enableDisablePanButton(true);
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE||observable.getActionType()==ClientConstants.ACTIONTYPE_REJECT ||
        observable.getActionType()==ClientConstants.ACTIONTYPE_VIEW || observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE ||
        observable.getActionType()==ClientConstants.ACTIONTYPE_EXCEPTION){
            enableDisablePanButton(false);
        }else{
            if(updateMode){
                String authStatus=CommonUtil.convertObjToStr(tblEmpList.getValueAt(tblEmpList.getSelectedRow(),7));
                if(authStatus.length()>0 && updateMode){
                    ClientUtil.enableDisable(panEmpTraning, false);
                    ClientUtil.enableDisable(panTrainingData, false);
                    enableDisablePanButton(false);
                    btnSave.setEnabled(false);
                    ClientUtil.showAlertWindow("Row Is Already AUTHORIZED/REJECTED..");
                    btnEmpNew.setEnabled(true);
                    return;
                }
            }
            enableDisablePanButton(true);
            btnEmpNew.setEnabled(false);
            enableDisableButton(true);
            txtSlNo.setEnabled(false);
        }
    }//GEN-LAST:event_tblEmpListMousePressed
    
    private void btnEmpDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmpDeleteActionPerformed
        // TODO add your handling code here:
        String s=  CommonUtil.convertObjToStr(tblEmpList.getValueAt(tblEmpList.getSelectedRow(),0));
        observable.deleteTableData(s,tblEmpList.getSelectedRow());
        observable.resetEmpDetails();
        enableDisableButton(false);
        enableDisablePanButton(false);
        btnEmpNew.setEnabled(true);
    }//GEN-LAST:event_btnEmpDeleteActionPerformed
    
    private void btnEmpNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmpNewActionPerformed
        // TODO add your handling code here:
        updateMode = false;
        txtAssetNum.setText("");
        observable.resetEmpDetails();
        observable.setNewData(true);
        enableDisableButton(true);
        txtQuantity.setEnabled(true);
        btnEmpSave.setEnabled(true);
        btnEmpDelete.setEnabled(false);
        btnEmpNew.setEnabled(false);
        //      txtSlNo.setEnabled(false);
    }//GEN-LAST:event_btnEmpNewActionPerformed
    
    private void btnEmpSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmpSaveActionPerformed
        // TODO add your handling code here:
        try{
            String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panEmpTraning, getMandatoryHashMap());
            if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
                displayAlert(mandatoryMessage);
            } else {
                System.out.println("val86877"+tblEmpList.getRowCount());
                if(txtAssetNum.getText().length()>0){
                    String assetNum = txtAssetNum.getText();
                    int quan= Integer.parseInt(txtQuantity.getText());
                    int tableCount = tblEmpList.getRowCount();
                    if(tableCount>=quan && !updateMode){
                        ClientUtil.displayAlert("Cant Enter More than "+""+txtQuantity.getText()+" Entries");
                        observable.resetEmpDetails();
                        enableDisableButton(false);
                        btnEmpSave.setEnabled(false);
                        return;
                    }
                 /*   if(tblEmpList.getRowCount()>0) {
                        for(int i=0;i<tblEmpList.getRowCount();i++){
                            String aNum=CommonUtil.convertObjToStr(tblEmpList.getValueAt(i,1));
                            System.out.println("valjkhg>>>>"+CommonUtil.convertObjToDouble(tblEmpList.getValueAt(i, 6)));
                          //  amt=amt+CommonUtil.convertObjToDouble(tblEmpList.getValueAt(i, 6));
                            if(aNum.equalsIgnoreCase(assetNum) && !updateMode) {
                                ClientUtil.displayAlert("Asset Number Already Exists in this Table");
                                txtAssetNum.setText("");
                                return;
                            }
                        }
                    }
                  */
                    if(tblEmpList.getRowCount()>0) {
                        for(int i=0;i<tblEmpList.getRowCount();i++){
                            String aNum=CommonUtil.convertObjToStr(tblEmpList.getValueAt(i,1));
                            if(aNum.equalsIgnoreCase(assetNum) && i!=updateTab) {
                                ClientUtil.displayAlert("Asset Number Already Exists in this Table");
                                txtAssetNum.setText("");
                                return;
                            }
                        }
                    }
                    
                    updateOBFields();
                    observable.addToTable(updateTab,updateMode);
                    amt=0.0;
                    if(tblEmpList.getRowCount()>0) {
                        for(int i=0;i<tblEmpList.getRowCount();i++){
                            //  String aNum=CommonUtil.convertObjToStr(tblEmpList.getValueAt(i,1));
                            System.out.println("valjkhg>>>>"+CommonUtil.convertObjToDouble(tblEmpList.getValueAt(i, 5)));
                            amt=amt+CommonUtil.convertObjToDouble(tblEmpList.getValueAt(i, 5));
                           /* if(aNum.equalsIgnoreCase(assetNum) && !updateMode) {
                                ClientUtil.displayAlert("Asset Number Already Exists in this Table");
                                txtAssetNum.setText("");
                                return;
                            }
                            */
                        }
                    }
                    txtAmount.setText(CommonUtil.convertObjToStr(amt));
                    
                    //trans details
                    transactionUI.setCallingAmount(""+amt);
                    transactionUI.setCallingApplicantName(txtComp.getText());
                    transactionUI.setCallingTransType("TRANSFER");
                    //end..
                    
                    observable.resetEmpDetails();
                    enableDisablePanButton(false);
                    btnEmpNew.setEnabled(true);
                    enableDisableButton(false);
                    ClientUtil.enableDisable(panTrainingData,false);
                    txtQuantity.setEnabled(true);
                    btnSave.setEnabled(true);
                }
                else{
                    ClientUtil.displayAlert("Asset Number Should Not Be Empty...");
                    return;
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnEmpSaveActionPerformed
    
    private void cboWarrPackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboWarrPackActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboWarrPackActionPerformed
    
    private void cboAssetDescActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboAssetDescActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboAssetDescActionPerformed
    
    private void cboAssetTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboAssetTypeActionPerformed
        // TODO add your handling code here:
        if(cboAssetType.getSelectedIndex()>0){
            HashMap intTangibleMap = new HashMap();
            intTangibleMap.put("ASSET_TYPE",cboAssetType.getSelectedItem());
            observable.setAssetDesc(intTangibleMap);
            cboAssetDesc.setModel(observable.getCbmassetDesc());
        }
    }//GEN-LAST:event_cboAssetTypeActionPerformed
    
    private void tdtPurchasedFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtPurchasedFocusLost
        // TODO add your handling code here
        if(tdtPurchased.getDateValue().length()>0){
            Date toDt = DateUtil.getDateMMDDYYYY(tdtPurchased.getDateValue());
            Date fromDt= DateUtil.getDateMMDDYYYY(tdtOrdered.getDateValue());
            if(toDt.before(fromDt)){
                ClientUtil.displayAlert("Purchased Date Should Be Equal To Or Greater Than From Ordered Date");
                tdtPurchased.setDateValue("");
                return;
            }
        }
    }//GEN-LAST:event_tdtPurchasedFocusLost
    
    private void tdtOrderedFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtOrderedFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tdtOrderedFocusLost
    
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        callView("Enquiry");
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // TODO add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // TODO add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // TODO add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // TODO add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        setModified(false);
        deletescreenLock();
        observable.resetForm();
        observable.setAuthorizeStatus("");
        setHelpBtnEnableDisable(false);
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panEmpTraning, false);
        ClientUtil.enableDisable(panEmpBtn1, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        enableDisablePanButton(false);
        viewType = "";
        lblStatus.setText("             ");
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        btnNew.setEnabled(true);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnSave.setEnabled(false);
        isFilled = false;
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView("Delete");
        lblStatus.setText("Delete");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        btnSave.setEnabled(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView("Edit");
        lblStatus.setText("Edit");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        ClientUtil.enableDisable(panEmpTraning, false);
        enableDisablePanButton(false);
        txtQuantity.setEnabled(true);
        btnSave.setEnabled(true);
        
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panEmpTraning, getMandatoryHashMap());
        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
            displayAlert(mandatoryMessage);
        } else {
            //            if(observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE){
            if(txtAssetNum.getText().length()>0 && observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE){
                ClientUtil.displayAlert("Asset List Not Saved ! ! !");
                return;
            }
            
            //trans details
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE){
                int transactionSize = 0 ;
                if(transactionUI.getOutputTO().size() == 0 && CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue() > 0){
                    ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS)) ;
                    return;
                }else {
                    if(CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue()>0){
                        amtBorrow=CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                        //     System.out.println("txtAmtBorrowed.getText()0000000000====="+txtAmtBorrowed.getText());
                        transactionSize = (transactionUI.getOutputTO()).size();
                        if(transactionSize != 1 && CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue()>0){
                            ClientUtil.showAlertWindow("Multiple Transactions are Not allowed, Make it one Transaction") ;
                            return;
                        }else{
                            observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                        }
                    }else if(transactionUI.getOutputTO().size()>0){
                        System.out.println("transop>>>>>>"+transactionUI.getOutputTO());
                        observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                    }
                }
                if(transactionSize == 0 && CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue()>0){
                    ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS)) ;
                    return;
                }else if(transactionSize != 0 ){
                    if (!transactionUI.isBtnSaveTransactionDetailsFlag()) {
                        ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.SAVE_TX_DETAILS));
                        return;
                    }
                    if(transactionUI.getOutputTO().size()>0){
                        observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                        updateOBFields();
                        saveAction();
                        transactionUI.setButtonEnableDisable(true);
                        transactionUI.cancelAction(false);
                        transactionUI.resetObjects();
                        transactionUI.setCallingApplicantName("");
                        transactionUI.setCallingAmount("");
                        
                    }
                }
                
            }else{
                updateOBFields();
                saveAction();
                
            }
            
            //end..
            
            //            }
            setModified(false);
            //  updateOBFields();
            //  saveAction();
            
            transactionUI.setButtonEnableDisable(true);
            transactionUI.cancelAction(false);
            transactionUI.resetObjects();
            transactionUI.setCallingApplicantName("");
            transactionUI.setCallingAmount("");
            
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(true);
            btnException.setEnabled(true);
            enableDisablePanButton(false);
            ClientUtil.enableDisable(tbrTokenConfig,false);
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        
        setModified(true);
        setHelpBtnEnableDisable(true);
        enableDisablePanButton(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.resetForm();
        txtAmount.setText("");
        observable.setStatus();
        //trans details
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        //end..
        setButtonEnableDisable();
        ClientUtil.enableDisable(panTrainingData,true);
        ClientUtil.enableDisable(panTrainingData1,true);
        txtTrainingID.setEnabled(false);
        txtAmount.setEditable(false);
        //    txtSlNo.setEnabled(false);
        enableDisablePanButton(false);
        btnEmpSave.setEnabled(true);
        ClientUtil.enableDisable(tbrTokenConfig,true);
        btnEmpNew.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnPrint.setEnabled(false);
    }//GEN-LAST:event_btnNewActionPerformed
    
    //trans details
    private void displayTransDetail(HashMap proxyResultMap) {
        System.out.println("@#$@@$@@@$ proxyResultMap : " +proxyResultMap);
        String cashDisplayStr = "Cash Transaction Details...\n";
        String transferDisplayStr = "Transfer Transaction Details...\n";
        String displayStr = "";
        String transId = "";
        String transType = "";
        Object keys[] = proxyResultMap.keySet().toArray();
        System.out.println("keeeeeeeeeeeyyy>>>>>>>>>>>"+keys);
        System.out.println("keys.length>>>>>>>>>>>"+keys.length);
        int cashCount = 0;
        int transferCount = 0;
        List tempList = null;
        HashMap transMap = null;
        String actNum = "";
        HashMap transIdMap = new HashMap();
        HashMap transTypeMap = new HashMap();
        for (int i=0; i<keys.length; i++) {
            System.out.println("proxyResultMap.get(keys[i])>>>>>>"+proxyResultMap.get(keys[i]));
            if (proxyResultMap.get(keys[i]) instanceof String) {
                System.out.println("innnn>>>>sdasd");
                continue;
            }
            System.out.println("tempList>>>>fgfd"+tempList);
            tempList = (List)proxyResultMap.get(keys[i]);
            System.out.println("CommonUtil.convertObjToStr(keys[i]).indexOf>>>>>>>>>>>"+CommonUtil.convertObjToStr(keys[i]).indexOf("CASH"));
            if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH")!=-1) {
                System.out.println("caaaashdisplay>>>>>>>>>>>"+tempList);
                for (int j=0; j<tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j==0) {
                        transId = (String)transMap.get("TRANS_ID");
                    }
                    cashDisplayStr += "Trans Id : "+transMap.get("TRANS_ID")+
                    "   Trans Type : "+transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if(actNum != null && !actNum.equals("")){
                        cashDisplayStr +="   Account No : "+transMap.get("ACT_NUM")+
                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }else{
                        cashDisplayStr += "   Ac Hd Desc : "+transMap.get("AC_HD_ID")+
                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }
                    transTypeMap.put(transMap.get("TRANS_ID"),transMap.get("TRANS_TYPE"));
                    transIdMap.put(transMap.get("TRANS_ID"),"CASH");
                }
                cashCount++;
            } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER")!=-1) {
                for (int j=0; j<tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j==0) {
                        transId = (String)transMap.get("BATCH_ID");
                    }
                    transferDisplayStr += "Trans Id : "+transMap.get("TRANS_ID")+
                    "   Batch Id : "+transMap.get("BATCH_ID")+
                    "   Trans Type : "+transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if(actNum != null && !actNum.equals("")){
                        transferDisplayStr +="   Account No : "+transMap.get("ACT_NUM")+
                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }else{
                        transferDisplayStr += "   Ac Hd Desc : "+transMap.get("AC_HD_ID")+
                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }
                    transIdMap.put(transMap.get("BATCH_ID"),"TRANSFER");
                }
                transferCount++;
            }
        }
        if(cashCount>0){
            System.out.println("cash.....>>>displayy"+cashDisplayStr);
            displayStr+=cashDisplayStr;
        }
        if(transferCount>0){
            System.out.println("cash.....>>>displayy"+transferDisplayStr);
            displayStr+=transferDisplayStr;
        }
        ClientUtil.showMessageWindow(""+displayStr);
        
        int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null,"Do you want to print?", CommonConstants.WARNINGTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        System.out.println("#$#$$ yesNo : "+yesNo);
        if (yesNo==0) {
            //            TTIntegration ttIntgration = null;
            //            HashMap paramMap = new HashMap();
            //            paramMap.put("TransId", transId);
            //            paramMap.put("TransDt", observable.getCurrDt());
            //            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
            //            ttIntgration.setParam(paramMap);
            //            ttIntgration.integrationForPrint("ReceiptPayment", false);
            
            TTIntegration ttIntgration = null;
            HashMap printParamMap = new HashMap();
            printParamMap.put("TransDt", observable.getCurrDt());
            printParamMap.put("BranchId", ProxyParameters.BRANCH_ID);
            Object keys1[] = transIdMap.keySet().toArray();
            for (int i=0; i<keys1.length; i++) {
                printParamMap.put("TransId", keys1[i]);
                ttIntgration.setParam(printParamMap);
                if (CommonUtil.convertObjToStr(transIdMap.get(keys1[i])).equals("TRANSFER")) {
                    ttIntgration.integrationForPrint("ReceiptPayment");
                } else if (CommonUtil.convertObjToStr(transTypeMap.get(keys1[i])).equals("DEBIT")) {
                    ttIntgration.integrationForPrint("CashPayment", false);
                } else {
                    ttIntgration.integrationForPrint("CashReceipt", false);
                }
            }
        }
    }
    //end...
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnEmpDelete;
    private com.see.truetransact.uicomponent.CButton btnEmpNew;
    private com.see.truetransact.uicomponent.CButton btnEmpSave;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboAssetDesc;
    private com.see.truetransact.uicomponent.CComboBox cboAssetType;
    private com.see.truetransact.uicomponent.CComboBox cboBranchId;
    private com.see.truetransact.uicomponent.CComboBox cboDepart;
    private com.see.truetransact.uicomponent.CComboBox cboWarrPack;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lbSpace3;
    private javax.swing.JLabel lblAmount;
    private com.see.truetransact.uicomponent.CLabel lblAssetDesc;
    private com.see.truetransact.uicomponent.CLabel lblAssetNum;
    private com.see.truetransact.uicomponent.CLabel lblAssetType;
    private com.see.truetransact.uicomponent.CLabel lblBranchId;
    private com.see.truetransact.uicomponent.CLabel lblComp;
    private com.see.truetransact.uicomponent.CLabel lblCurrValue;
    private com.see.truetransact.uicomponent.CLabel lblDepart;
    private com.see.truetransact.uicomponent.CLabel lblFaceVal;
    private com.see.truetransact.uicomponent.CLabel lblFloor;
    private com.see.truetransact.uicomponent.CLabel lblInvoiceNo;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblOrdered;
    private com.see.truetransact.uicomponent.CLabel lblPurchased;
    private com.see.truetransact.uicomponent.CLabel lblQuantity;
    private com.see.truetransact.uicomponent.CLabel lblSlNo;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace34;
    private com.see.truetransact.uicomponent.CLabel lblSpace35;
    private com.see.truetransact.uicomponent.CLabel lblSpace36;
    private com.see.truetransact.uicomponent.CLabel lblSpace37;
    private com.see.truetransact.uicomponent.CLabel lblSpace38;
    private com.see.truetransact.uicomponent.CLabel lblSpace39;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTrainingID;
    private com.see.truetransact.uicomponent.CLabel lblWarranty;
    private com.see.truetransact.uicomponent.CLabel lblWarrantyExpired;
    private com.see.truetransact.uicomponent.CLabel lblWarrantyExpiryDate;
    private com.see.truetransact.uicomponent.CMenuBar mbrTokenConfig;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panEmpBtn1;
    private com.see.truetransact.uicomponent.CPanel panEmpList;
    private com.see.truetransact.uicomponent.CPanel panEmpTraning;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTrainingData;
    private com.see.truetransact.uicomponent.CPanel panTrainingData1;
    private com.see.truetransact.uicomponent.CPanel panTrans;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CScrollPane srpTokenLost;
    private com.see.truetransact.uicomponent.CTable tblEmpList;
    private com.see.truetransact.uicomponent.CToolBar tbrTokenConfig;
    private com.see.truetransact.uicomponent.CDateField tdtOrdered;
    private com.see.truetransact.uicomponent.CDateField tdtPurchased;
    private javax.swing.JTextField txtAmount;
    private com.see.truetransact.uicomponent.CTextField txtAssetNum;
    private com.see.truetransact.uicomponent.CTextField txtComp;
    private com.see.truetransact.uicomponent.CTextField txtCurrValue;
    private com.see.truetransact.uicomponent.CTextField txtFaceVal;
    private com.see.truetransact.uicomponent.CTextField txtFloor;
    private com.see.truetransact.uicomponent.CTextField txtInvoiceNo;
    private com.see.truetransact.uicomponent.CTextField txtQuantity;
    private com.see.truetransact.uicomponent.CTextField txtSlNo;
    private com.see.truetransact.uicomponent.CTextField txtTrainingID;
    private com.see.truetransact.uicomponent.CTextField txtWarranty;
    // End of variables declaration//GEN-END:variables
    public static void main(String[] args) {
        FixedAssetsIndividualUI fID = new FixedAssetsIndividualUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(fID);
        j.show();
        fID.show();
    }
}
