/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AgentUI.java
 *
 * Created on February 2, 2005, 12:20 PM
 */

package com.see.truetransact.ui.agent;

import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;
import java.util.List;
/**
 *
 * @author  152721
 */
public class AgentUI extends CInternalFrame implements java.util.Observer ,UIMandatoryField{
    HashMap mandatoryMap;
    AgentOB observable;
//    AgentRB resourceBundle = new AgentRB();
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.agent.AgentRB", ProxyParameters.LANGUAGE);
    
    final int EDIT=0,DELETE=1,AUTHORIZE=2, AGENTID=3, ACCNO =4, DEPOSITNO =5, VIEW = 10,EDIT_LEAVE_DETAILS = 12;
    final int SUSPPROD_TYPE=6,SUSPPROD_ID=7,SUSPPROD_ACCN=8,LAGENT_ID=9,CAGENT_ID=11;
    int viewType=-1;
    private Date currDt = null;
    private boolean updateMode = false;
    private boolean updateAgentLeaveMode = false;
    int updateTab = -1;
    /** Creates new form AgentUI */
    public AgentUI() {
        initComponents();
        initSetup();
        initComponentData();
    }
    
    private void initSetup(){
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setObservable();
        setMaxLenths();
        
        ClientUtil.enableDisable(this, false); //__ Disables all when the screen appears for the 1st time
        setButtonEnableDisable();              //__ Enables/Disables the necessary buttons and menu items...
        observable.resetForm();                //__ Resets the Data in the Form...
        observable.resetTable();
        observable.resetStatus();              //__ to reset the status...
        btnAgentID.setEnabled(false);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panAgent);        
        btnCommisionCreditedTo.setEnabled(false);
        btnDepositCreditedTo.setEnabled(false);
//        srpAgentCommision.setVisible(false);
        btnCollSuspACNum.setEnabled(false);
//        btnCollSuspProdId.setEnabled(false);
//        btnCollSuspProdtype.setEnabled(false);
        currDt = ClientUtil.getCurrentDate();
        btnProdNew.setEnabled(false);
        btnProdSave.setEnabled(false);
        btnProdDelete.setEnabled(false);
        btnAgentLeaveNew.setEnabled(false);
        btnAgentLeaveSave.setEnabled(false);
        btnAgentLeaveDelete.setEnabled(false);
        btnLAgentID.setEnabled(false);
        btnCAgentId.setEnabled(false);
    }
    
    private void setObservable() {
        observable = AgentOB.getInstance();
        observable.addObserver(this);
    }
    
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        btnAgentID.setName("btnAgentID");
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
        
        btnCommisionCreditedTo.setName("btnCommisionCreditedTo");
        btnDepositCreditedTo.setName("btnDepositCreditedTo");
//        srpAgentCommision.setName("srpAgentCommision");
        
        srpAgent.setName("srpAgent");
        tblAgent.setName("tblAgent");
        lblAgentID.setName("lblAgentID");
        lblApptDate.setName("lblApptDate");
        lblMsg.setName("lblMsg");
//        lblName.setName("lblName");
        lblNameValue.setName("lblNameValue");
        lblRemarks.setName("lblRemarks");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblStatus1.setName("lblStatus1");
        
        lblCommisionCreditedTo.setName("lblCommisionCreditedTo");
        lblDepositCreditedTo.setName("lblDepositCreditedTo");
        lblProdIdlName.setName("lblProdIdlName");
        lblDepositName.setName("lblDepositName");
        
        mbrAgent.setName("mbrAgent");
        panAgent.setName("panAgent");
        panAgentData.setName("panAgentData");
        panAgentDisplay.setName("panAgentDisplay");
        panStatus.setName("panStatus");
        tdtApptDate.setName("tdtApptDate");
        txtAgentID.setName("txtAgentID");
        
        txtCommisionCreditedTo.setName("txtCommisionCreditedTo");
        txtDepositCreditedTo.setName("txtDepositCreditedTo");
        txtRemarks.setName("txtRemarks");
        cboProductType.setName("cboProductType");
        txtCollSuspACNum.setName("txtCollSuspACNum");
        btnCollSuspACNum.setName("btnCollSuspACNum");
//        btnCollSuspProdId.setName("btnCollSuspProdId");
//        btnCollSuspProdtype.setName("btnCollSuspProdtype");
        lblCollSuspACnum.setName("lblCollSuspACnum");
        lblCollSuspProdID.setName("lblCollSuspProdID");
        lblCollSuspProdtype.setName("lblCollSuspProdtype");
//        lblCustName.setName("lblCustName");
        lblLstComPaidDt.setName("lblLstComPaidDt");
        lblLstComPaiddtVal.setName("lblLstComPaiddtVal");
    }
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblApptDate.setText(resourceBundle.getString("lblApptDate"));
        btnClose.setText(resourceBundle.getString("btnClose"));
//        lblName.setText(resourceBundle.getString("lblName"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblStatus1.setText(resourceBundle.getString("lblStatus1"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblAgentID.setText(resourceBundle.getString("lblAgentID"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        btnAgentID.setText(resourceBundle.getString("btnAgentID"));
        lblNameValue.setText(resourceBundle.getString("lblNameValue"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblRemarks.setText(resourceBundle.getString("lblRemarks"));
        lblCommisionCreditedTo.setText(resourceBundle.getString("lblCommisionCreditedTo"));
        lblDepositCreditedTo.setText(resourceBundle.getString("lblDepositCreditedTo"));
        btnCommisionCreditedTo.setText(resourceBundle.getString("btnCommisionCreditedTo"));
        btnDepositCreditedTo.setText(resourceBundle.getString("btnDepositCreditedTo"));
        lblProdIdlName.setText(resourceBundle.getString("lblProdIdlName"));
        lblDepositName.setText(resourceBundle.getString("lblDepositName"));
        btnCollSuspACNum.setText(resourceBundle.getString("btnCollSuspACNum"));
//        btnCollSuspProdId.setText(resourceBundle.getString("btnCollSuspProdId"));
//        btnCollSuspProdtype.setText(resourceBundle.getString("btnCollSuspProdtype"));
        lblCollSuspACnum.setText(resourceBundle.getString("lblCollSuspACnum"));
        lblCollSuspProdID.setText(resourceBundle.getString("lblCollSuspProdID"));
        lblCollSuspProdtype.setText(resourceBundle.getString("lblCollSuspProdtype"));
//        lblCustName.setText(resourceBundle.getString("lblCustName"));
        lblCustNameVal.setText(resourceBundle.getString("lblCustNameVal"));
        lblLstComPaidDt.setText(resourceBundle.getString("lblLstComPaidDt"));
        lblLstComPaiddtVal.setText(resourceBundle.getString("lblLstComPaiddtVal"));
    }
    
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        txtAgentID.setText(observable.getTxtAgentID());
        tdtApptDate.setDateValue(observable.getTdtApptDate());
        txtRemarks.setText(observable.getTxtRemarks());
        if (CommonUtil.convertObjToStr(observable.getAgentType()).equals("A")) {//Added By Revathi.L
            rdoAgent.setSelected(true);
            rdoDealer.setSelected(false);
        } else if (CommonUtil.convertObjToStr(observable.getAgentType()).equals("D")) {
            rdoAgent.setSelected(false);
            rdoDealer.setSelected(true);
        }
        txtCommisionCreditedTo.setText(observable.getOperativeAcc());
        txtDepositCreditedTo.setText(observable.getDepositAcc());
        lblNameValue.setText(observable.getLblName());
        lblDepositName.setText(observable.getLblDepositName());
        lblProdIdlName.setText(observable.getLblProdIdlName());
        
        // to set the values in table...
        tblAgent.setModel(observable.getTblAgent());
        
        //To set the Status...
        lblStatus1.setText(observable.getLblStatus());
        cboProductType.setSelectedItem(((ComboBoxModel)cboProductType.getModel()).getDataForKey(observable.getTxtCollSuspProdtype()));
        observable.populateCboCollSuspPdId();
        cboProductID.setModel(observable.getCboCollSuspProdID());
        cboProductID.setSelectedItem(((ComboBoxModel)cboProductID.getModel()).getDataForKey(observable.getTxtCollSuspProdID()));
        //cboDailyProductType.setSelectedItem(((ComboBoxModel)cboDailyProductType.getModel()).getDataForKey(observable.getTxtDepositCreditedTo()));
        cboCreditProductType.setSelectedItem(((ComboBoxModel)cboCreditProductType.getModel()).getDataForKey(observable.getTxtDepositCreditedTo()));
        observable.cboCreditProductId();
        cboCreditProductID.setModel(observable.getCboCreditProductID());
        cboCreditProductID.setSelectedItem(((ComboBoxModel)cboCreditProductID.getModel()).getDataForKey(observable.getTxtDepositCreditedProdId()));
        txtCollSuspACNum.setText(CommonUtil.convertObjToStr(observable.getTxtCollSuspACNum()));
        lblCustNameVal.setText(observable.getLblCustNameVal());
        lblDepositName.setText(observable.getLblDepositCustName());
        txtDepositCreditedTo.setText(CommonUtil.convertObjToStr(observable.getDpacnum()));
        if(observable.getActionType()!=ClientConstants.ACTIONTYPE_NEW && observable.getLastComPaidDt()!=null 
                && !observable.getLastComPaidDt().equals(""))
            lblLstComPaiddtVal.setText(DateUtil.getStringDate(DateUtil.addDays(observable.getLastComPaidDt(),-1)));
		//Commented and following code Added By Revathi.L
//        cboAgentRegion.setSelectedItem(((ComboBoxModel) cboAgentRegion.getModel()).getDataForKey(observable.getCboRegion()));
//        cboAgentTransactionType.setSelectedItem(((ComboBoxModel) cboAgentTransactionType.getModel()).getDataForKey(observable.getCboTransactionType()));
        cboAgentRegion.setSelectedItem((observable.getCboRegion()));
        cboAgentTransactionType.setSelectedItem((observable.getCboTransactionType()));
    }
    
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtAgentID(txtAgentID.getText());
        observable.setTdtApptDate(tdtApptDate.getDateValue());
        observable.setTxtRemarks(txtRemarks.getText());
        if (rdoAgent.isSelected()) {//Added By Revathi.L
            observable.setAgentType("A");
        } else if (rdoDealer.isSelected()) {
            observable.setAgentType("D");
        }
        observable.setDepositAcc(txtDepositCreditedTo.getText());
        observable.setOperativeAcc(txtCommisionCreditedTo.getText());
        observable.setLblProdIdlName(lblProdIdlName.getText());
        observable.setDpacnum(txtDepositCreditedTo.getText());
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW)
        observable.setLastComPaidDt(DateUtil.getDateMMDDYYYY(tdtApptDate.getDateValue()));
    
        //__ To update the BranchSelected...
        observable.setSelectedBranchID(getSelectedBranchID());
        ((ComboBoxModel)cboProductType.getModel()).getKeyForSelected().toString();
        observable.setTxtCollSuspProdtype(((ComboBoxModel)cboProductType.getModel()).getKeyForSelected().toString());
        if(!((ComboBoxModel)cboProductType.getModel()).getKeyForSelected().toString().equals("GL")){
            if(cboProductType.getSelectedItem()!=null && !cboProductType.getSelectedItem().equals("")){
                String prod_type=((ComboBoxModel)cboProductType.getModel()).getKeyForSelected().toString();
                if(cboProductID.getSelectedItem()!=null && !cboProductID.getSelectedItem().equals("")){
                    String prod_id=((ComboBoxModel)cboProductID.getModel()).getKeyForSelected().toString();
                    observable.setTxtCollSuspProdID(((ComboBoxModel)cboProductID.getModel()).getKeyForSelected().toString());
                    observable.setTxtCollSuspProdtype(prod_type);
                    observable.setTxtCollSuspProdID(prod_id);
                    System.out.println("prod_type"+prod_type+"prod_id"+prod_id);
                    observable.setTxtCollSuspACNum(txtCollSuspACNum.getText());
                }
            }            
            //            observable.setTxtCollSuspProdID(((ComboBoxModel)cboProductID.getModel()).getKeyForSelected().toString());
        } else{
            observable.setTxtCollSuspACNum(txtCollSuspACNum.getText());
            String prod_type=((ComboBoxModel)cboProductType.getModel()).getKeyForSelected().toString();
            System.out.println("prod_type"+prod_type);
            observable.setTxtCollSuspProdtype(prod_type);
        }
        observable.setTxtDepositCreditedTo(((ComboBoxModel)cboCreditProductType.getModel()).getKeyForSelected().toString());
        if(!((ComboBoxModel)cboCreditProductType.getModel()).getKeyForSelected().toString().equals("GL")){
            if(cboCreditProductType.getSelectedItem()!=null && !cboCreditProductType.getSelectedItem().equals("")){
                String prod_type=((ComboBoxModel)cboCreditProductType.getModel()).getKeyForSelected().toString();
                if(cboCreditProductID.getSelectedItem()!=null && !cboCreditProductID.getSelectedItem().equals("")){
                    String prod_id=((ComboBoxModel)cboCreditProductID.getModel()).getKeyForSelected().toString();
                    observable.setTxtDepositCreditedProdId(((ComboBoxModel)cboCreditProductID.getModel()).getKeyForSelected().toString());
                    observable.setTxtDepositCreditedTo(prod_type);
                    observable.setTxtDepositCreditedProdId(prod_id);
                    System.out.println("prod_type"+prod_type+"prod_id"+prod_id);
//                    observable.setTxtDepositCreditedTo(txtDepositCreditedTo.getText());
                }
            }            
        } else{
//            observable.setTxtDepositCreditedTo(txtDepositCreditedTo.getText());
            String prod_type=((ComboBoxModel)cboCreditProductType.getModel()).getKeyForSelected().toString();
            System.out.println("prod_type"+prod_type);
            observable.setTxtDepositCreditedTo(prod_type);
            observable.setCboCreditProductType(prod_type);
        }
        if(cboAgentRegion.getSelectedItem()!= null && !cboAgentRegion.getSelectedItem().equals("")){
            observable.setCboRegion(((ComboBoxModel)cboAgentRegion.getModel()).getKeyForSelected().toString());
        }
        if(cboAgentTransactionType.getSelectedItem()!= null && !cboAgentTransactionType.getSelectedItem().equals("")){
            observable.setCboTransactionType(((ComboBoxModel)cboAgentTransactionType.getModel()).getKeyForSelected().toString());
        }
    }
    
    public void updateAgentLeaveOBFields() {
        observable.setTxtLAgentID(txtLAgentID.getText());
        observable.setTxtCAgentId(txtCAgentId.getText());
        observable.setLblLAgentName(lblLAgentName.getText());
        observable.setLblCAgentName(lblCAgentName.getText());
        observable.setTdtFromDate(tdtFromDate.getDateValue());
        observable.setTdtToDate(tdtToDate.getDateValue());
        observable.setCboLRegionValue(((ComboBoxModel)cboLRegion.getModel()).getKeyForSelected().toString());
        observable.setCboCRegionValue(((ComboBoxModel)cboCRegion.getModel()).getKeyForSelected().toString());
        observable.setCboTxnTypeValue(((ComboBoxModel)cboTxnType.getModel()).getKeyForSelected().toString());
    }
    
    public void updateAgentLeaveFields() {
        txtLAgentID.setText(observable.getTxtLAgentID());
        txtCAgentId.setText(observable.getTxtCAgentId());
        lblLAgentName.setText(observable.getLblLAgentName());
        lblCAgentName.setText(observable.getLblCAgentName());
        tdtFromDate.setDateValue(observable.getTdtFromDate());
        tdtToDate.setDateValue(observable.getTdtToDate());
        cboLRegion.setSelectedItem(((ComboBoxModel)cboLRegion.getModel()).getDataForKey(observable.getCboLRegion()));
        cboCRegion.setSelectedItem(((ComboBoxModel)cboCRegion.getModel()).getDataForKey(observable.getCboCRegion()));
        cboTxnType.setSelectedItem(((ComboBoxModel)cboTxnType.getModel()).getDataForKey(observable.getCboTxnType()));
    }
    
/* Auto Generated Method - setMandatoryHashMap()
 
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtRemarks", new Boolean(false));
        mandatoryMap.put("txtAgentID", new Boolean(false));
        mandatoryMap.put("tdtApptDate", new Boolean(false));
//        mandatoryMap.put("txtOperative Acc No", new Boolean(true));
//        mandatoryMap.put("cboProductType", new Boolean(true));
//        mandatoryMap.put("txtCollSuspACNum", new Boolean(true));
//        mandatoryMap.put("txtCommisionCreditedTo",new Boolean(true));
//        mandatoryMap.put("cboCreditProductType",new Boolean(true));
//        mandatoryMap.put("txtDepositCreditedTo",new Boolean(true));
//        mandatoryMap.put("cboProductType",new Boolean(false));
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
        AgentMRB objMandatoryRB = new AgentMRB();
        txtAgentID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAgentID"));
        tdtApptDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtApptDate"));
        txtRemarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRemarks"));
        txtCommisionCreditedTo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCommisionCreditedTo"));
        txtDepositCreditedTo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDepositCreditedTo"));
        
    }
    
    private void setMaxLenths() {
        txtAgentID.setMaxLength(16);
        txtCommisionCreditedTo.setMaxLength(16);
        txtDepositCreditedTo.setMaxLength(16);
        txtRemarks.setMaxLength(256);
    }
    
    // To set The Value of the Buttons Depending on the Value or Condition...
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
        btnView.setEnabled(!btnView.isEnabled());
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
        lblStatus1 = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        tbrLoantProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
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
        tabAgent = new com.see.truetransact.uicomponent.CTabbedPane();
        panAgent = new com.see.truetransact.uicomponent.CPanel();
        panAgentData = new com.see.truetransact.uicomponent.CPanel();
        panAgentID = new com.see.truetransact.uicomponent.CPanel();
        txtAgentID = new com.see.truetransact.uicomponent.CTextField();
        btnAgentID = new com.see.truetransact.uicomponent.CButton();
        panOA = new com.see.truetransact.uicomponent.CPanel();
        txtCommisionCreditedTo = new com.see.truetransact.uicomponent.CTextField();
        btnCommisionCreditedTo = new com.see.truetransact.uicomponent.CButton();
        panDep = new com.see.truetransact.uicomponent.CPanel();
        txtDepositCreditedTo = new com.see.truetransact.uicomponent.CTextField();
        btnDepositCreditedTo = new com.see.truetransact.uicomponent.CButton();
        panSuspAcc = new com.see.truetransact.uicomponent.CPanel();
        txtCollSuspACNum = new com.see.truetransact.uicomponent.CTextField();
        btnCollSuspACNum = new com.see.truetransact.uicomponent.CButton();
        lblAgentID = new com.see.truetransact.uicomponent.CLabel();
        lblNameValue = new com.see.truetransact.uicomponent.CLabel();
        lblApptDate = new com.see.truetransact.uicomponent.CLabel();
        tdtApptDate = new com.see.truetransact.uicomponent.CDateField();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        lblCommisionCreditedTo = new com.see.truetransact.uicomponent.CLabel();
        lblDepositCreditedTo = new com.see.truetransact.uicomponent.CLabel();
        lblProdIdlName = new com.see.truetransact.uicomponent.CLabel();
        lblDepositName = new com.see.truetransact.uicomponent.CLabel();
        lblCollSuspProdtype = new com.see.truetransact.uicomponent.CLabel();
        lblCollSuspProdID = new com.see.truetransact.uicomponent.CLabel();
        lblCollSuspACnum = new com.see.truetransact.uicomponent.CLabel();
        cboProductType = new com.see.truetransact.uicomponent.CComboBox();
        cboProductID = new com.see.truetransact.uicomponent.CComboBox();
        lblCustNameVal = new com.see.truetransact.uicomponent.CLabel();
        lblLstComPaiddtVal = new com.see.truetransact.uicomponent.CLabel();
        lblLstComPaidDt = new com.see.truetransact.uicomponent.CLabel();
        lblDailyProductId = new com.see.truetransact.uicomponent.CLabel();
        lblDailyProductType = new com.see.truetransact.uicomponent.CLabel();
        cboDailyProductType = new com.see.truetransact.uicomponent.CComboBox();
        cboDailyProductID = new com.see.truetransact.uicomponent.CComboBox();
        lblCreditProductType1 = new com.see.truetransact.uicomponent.CLabel();
        cboCreditProductType = new com.see.truetransact.uicomponent.CComboBox();
        cboCreditProductID = new com.see.truetransact.uicomponent.CComboBox();
        lblCreditProductId = new com.see.truetransact.uicomponent.CLabel();
        panSHGBtn = new com.see.truetransact.uicomponent.CPanel();
        btnProdNew = new com.see.truetransact.uicomponent.CButton();
        btnProdSave = new com.see.truetransact.uicomponent.CButton();
        btnProdDelete = new com.see.truetransact.uicomponent.CButton();
        srpProdDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblProdDetails = new com.see.truetransact.uicomponent.CTable();
        cboAgentRegion = new com.see.truetransact.uicomponent.CComboBox();
        lblAgentRegion = new com.see.truetransact.uicomponent.CLabel();
        cboAgentTransactionType = new com.see.truetransact.uicomponent.CComboBox();
        lblAgentTransactionType = new com.see.truetransact.uicomponent.CLabel();
        panAgentType = new com.see.truetransact.uicomponent.CPanel();
        rdoAgent = new com.see.truetransact.uicomponent.CRadioButton();
        rdoDealer = new com.see.truetransact.uicomponent.CRadioButton();
        lblAgentType = new com.see.truetransact.uicomponent.CLabel();
        panAgentDisplay = new com.see.truetransact.uicomponent.CPanel();
        srpAgent = new com.see.truetransact.uicomponent.CScrollPane();
        tblAgent = new com.see.truetransact.uicomponent.CTable();
        panAgent1 = new com.see.truetransact.uicomponent.CPanel();
        panAgenLeaveData = new com.see.truetransact.uicomponent.CPanel();
        srpAgentLeaveDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblAgentLeaveDetails = new com.see.truetransact.uicomponent.CTable();
        panAgentLeaveBtn1 = new com.see.truetransact.uicomponent.CPanel();
        lblAgentID1 = new com.see.truetransact.uicomponent.CLabel();
        panAgentID1 = new com.see.truetransact.uicomponent.CPanel();
        txtLAgentID = new com.see.truetransact.uicomponent.CTextField();
        btnLAgentID = new com.see.truetransact.uicomponent.CButton();
        lblCommisionCreditedTo1 = new com.see.truetransact.uicomponent.CLabel();
        lblLAgentName = new com.see.truetransact.uicomponent.CLabel();
        lblCreditProductType3 = new com.see.truetransact.uicomponent.CLabel();
        cboLRegion = new com.see.truetransact.uicomponent.CComboBox();
        lblDepositCreditedTo1 = new com.see.truetransact.uicomponent.CLabel();
        panDep1 = new com.see.truetransact.uicomponent.CPanel();
        txtCAgentId = new com.see.truetransact.uicomponent.CTextField();
        btnCAgentId = new com.see.truetransact.uicomponent.CButton();
        lblApptDate1 = new com.see.truetransact.uicomponent.CLabel();
        lblCAgentName = new com.see.truetransact.uicomponent.CLabel();
        lblCreditProductType2 = new com.see.truetransact.uicomponent.CLabel();
        cboCRegion = new com.see.truetransact.uicomponent.CComboBox();
        lblFromDate = new com.see.truetransact.uicomponent.CLabel();
        tdtFromDate = new com.see.truetransact.uicomponent.CDateField();
        lblCollSuspProdtype1 = new com.see.truetransact.uicomponent.CLabel();
        tdtToDate = new com.see.truetransact.uicomponent.CDateField();
        lblCollSuspProdID1 = new com.see.truetransact.uicomponent.CLabel();
        cboTxnType = new com.see.truetransact.uicomponent.CComboBox();
        panAgentLeaveBtn = new com.see.truetransact.uicomponent.CPanel();
        btnAgentLeaveNew = new com.see.truetransact.uicomponent.CButton();
        btnAgentLeaveSave = new com.see.truetransact.uicomponent.CButton();
        btnAgentLeaveDelete = new com.see.truetransact.uicomponent.CButton();
        mbrAgent = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptProcess = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        sptPrint = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(675, 680));
        setPreferredSize(new java.awt.Dimension(675, 680));

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace1.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace1, gridBagConstraints);

        lblStatus1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus1.setText("                      ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblStatus1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblMsg, gridBagConstraints);

        getContentPane().add(panStatus, java.awt.BorderLayout.SOUTH);

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnView);

        lblSpace5.setText("     ");
        tbrLoantProduct.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnNew);

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace26);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnEdit);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace27);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnDelete);

        lblSpace2.setText("     ");
        tbrLoantProduct.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnSave);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace28);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnCancel);

        lblSpace3.setText("     ");
        tbrLoantProduct.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnAuthorize);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace29);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnException);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace30);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnReject);

        lblSpace4.setText("     ");
        tbrLoantProduct.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnPrint);

        lblSpace31.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace31.setText("     ");
        lblSpace31.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace31);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnClose);

        getContentPane().add(tbrLoantProduct, java.awt.BorderLayout.NORTH);

        tabAgent.setMinimumSize(new java.awt.Dimension(635, 640));
        tabAgent.setPreferredSize(new java.awt.Dimension(635, 640));

        panAgent.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panAgent.setFocusCycleRoot(true);
        panAgent.setMinimumSize(new java.awt.Dimension(580, 500));
        panAgent.setPreferredSize(new java.awt.Dimension(580, 500));
        panAgent.setLayout(new java.awt.GridBagLayout());

        panAgentData.setBorder(javax.swing.BorderFactory.createTitledBorder("Agent Details"));
        panAgentData.setFocusCycleRoot(true);
        panAgentData.setMinimumSize(new java.awt.Dimension(625, 450));
        panAgentData.setPreferredSize(new java.awt.Dimension(625, 450));
        panAgentData.setLayout(new java.awt.GridBagLayout());

        panAgentID.setMinimumSize(new java.awt.Dimension(121, 29));
        panAgentID.setLayout(new java.awt.GridBagLayout());

        txtAgentID.setEditable(false);
        txtAgentID.setEnabled(false);
        txtAgentID.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panAgentID.add(txtAgentID, gridBagConstraints);

        btnAgentID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAgentID.setToolTipText("Agent ID");
        btnAgentID.setMinimumSize(new java.awt.Dimension(21, 21));
        btnAgentID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAgentID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgentIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAgentID.add(btnAgentID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAgentData.add(panAgentID, gridBagConstraints);

        panOA.setMinimumSize(new java.awt.Dimension(121, 29));
        panOA.setLayout(new java.awt.GridBagLayout());

        txtCommisionCreditedTo.setEditable(false);
        txtCommisionCreditedTo.setAllowAll(true);
        txtCommisionCreditedTo.setEnabled(false);
        txtCommisionCreditedTo.setOpaque(false);
        txtCommisionCreditedTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCommisionCreditedToActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panOA.add(txtCommisionCreditedTo, gridBagConstraints);

        btnCommisionCreditedTo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCommisionCreditedTo.setToolTipText("Agent ID");
        btnCommisionCreditedTo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnCommisionCreditedTo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCommisionCreditedTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCommisionCreditedToActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panOA.add(btnCommisionCreditedTo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAgentData.add(panOA, gridBagConstraints);

        panDep.setMinimumSize(new java.awt.Dimension(121, 25));
        panDep.setPreferredSize(new java.awt.Dimension(121, 25));
        panDep.setLayout(new java.awt.GridBagLayout());

        txtDepositCreditedTo.setEditable(false);
        txtDepositCreditedTo.setEnabled(false);
        txtDepositCreditedTo.setMinimumSize(new java.awt.Dimension(100, 24));
        txtDepositCreditedTo.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panDep.add(txtDepositCreditedTo, gridBagConstraints);

        btnDepositCreditedTo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDepositCreditedTo.setToolTipText("Agent ID");
        btnDepositCreditedTo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDepositCreditedTo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDepositCreditedTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDepositCreditedToActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDep.add(btnDepositCreditedTo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAgentData.add(panDep, gridBagConstraints);

        panSuspAcc.setMinimumSize(new java.awt.Dimension(125, 25));
        panSuspAcc.setPreferredSize(new java.awt.Dimension(125, 25));
        panSuspAcc.setLayout(new java.awt.GridBagLayout());

        txtCollSuspACNum.setEditable(false);
        txtCollSuspACNum.setEnabled(false);
        txtCollSuspACNum.setMinimumSize(new java.awt.Dimension(100, 24));
        txtCollSuspACNum.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panSuspAcc.add(txtCollSuspACNum, gridBagConstraints);

        btnCollSuspACNum.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCollSuspACNum.setToolTipText("Agent ID");
        btnCollSuspACNum.setMinimumSize(new java.awt.Dimension(21, 21));
        btnCollSuspACNum.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCollSuspACNum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCollSuspACNumActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSuspAcc.add(btnCollSuspACNum, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAgentData.add(panSuspAcc, gridBagConstraints);

        lblAgentID.setText("Agent ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 3);
        panAgentData.add(lblAgentID, gridBagConstraints);

        lblNameValue.setForeground(new java.awt.Color(0, 51, 204));
        lblNameValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblNameValue.setMaximumSize(new java.awt.Dimension(200, 20));
        lblNameValue.setMinimumSize(new java.awt.Dimension(200, 20));
        lblNameValue.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAgentData.add(lblNameValue, gridBagConstraints);

        lblApptDate.setText("Appointed Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAgentData.add(lblApptDate, gridBagConstraints);

        tdtApptDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtApptDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAgentData.add(tdtApptDate, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAgentData.add(lblRemarks, gridBagConstraints);

        txtRemarks.setMinimumSize(new java.awt.Dimension(100, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAgentData.add(txtRemarks, gridBagConstraints);

        lblCommisionCreditedTo.setText("Commision Credited To");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAgentData.add(lblCommisionCreditedTo, gridBagConstraints);

        lblDepositCreditedTo.setText("Security Deposit Credited To");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAgentData.add(lblDepositCreditedTo, gridBagConstraints);

        lblProdIdlName.setMaximumSize(new java.awt.Dimension(200, 20));
        lblProdIdlName.setMinimumSize(new java.awt.Dimension(200, 20));
        lblProdIdlName.setPreferredSize(new java.awt.Dimension(200, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAgentData.add(lblProdIdlName, gridBagConstraints);

        lblDepositName.setForeground(new java.awt.Color(0, 51, 204));
        lblDepositName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblDepositName.setMaximumSize(new java.awt.Dimension(200, 20));
        lblDepositName.setMinimumSize(new java.awt.Dimension(200, 20));
        lblDepositName.setPreferredSize(new java.awt.Dimension(200, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAgentData.add(lblDepositName, gridBagConstraints);

        lblCollSuspProdtype.setText("lblCollSuspProdtype");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAgentData.add(lblCollSuspProdtype, gridBagConstraints);

        lblCollSuspProdID.setText("lblCollSuspProdID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAgentData.add(lblCollSuspProdID, gridBagConstraints);

        lblCollSuspACnum.setText("lblCollSuspACNum");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAgentData.add(lblCollSuspACnum, gridBagConstraints);

        cboProductType.setMaximumSize(new java.awt.Dimension(150, 21));
        cboProductType.setMinimumSize(new java.awt.Dimension(150, 21));
        cboProductType.setPreferredSize(new java.awt.Dimension(150, 21));
        cboProductType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAgentData.add(cboProductType, gridBagConstraints);

        cboProductID.setMaximumSize(new java.awt.Dimension(150, 21));
        cboProductID.setMinimumSize(new java.awt.Dimension(150, 21));
        cboProductID.setPreferredSize(new java.awt.Dimension(150, 21));
        cboProductID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAgentData.add(cboProductID, gridBagConstraints);

        lblCustNameVal.setForeground(new java.awt.Color(0, 51, 204));
        lblCustNameVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblCustNameVal.setMaximumSize(new java.awt.Dimension(175, 20));
        lblCustNameVal.setMinimumSize(new java.awt.Dimension(175, 20));
        lblCustNameVal.setPreferredSize(new java.awt.Dimension(175, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 120, 0, 4);
        panAgentData.add(lblCustNameVal, gridBagConstraints);

        lblLstComPaiddtVal.setText("LastComPaidDtVal");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAgentData.add(lblLstComPaiddtVal, gridBagConstraints);

        lblLstComPaidDt.setText("LastComPaidDt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAgentData.add(lblLstComPaidDt, gridBagConstraints);

        lblDailyProductId.setText(" Daily Prod Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAgentData.add(lblDailyProductId, gridBagConstraints);

        lblDailyProductType.setText("Daily Prod Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAgentData.add(lblDailyProductType, gridBagConstraints);

        cboDailyProductType.setToolTipText("");
        cboDailyProductType.setMaximumSize(new java.awt.Dimension(150, 21));
        cboDailyProductType.setMinimumSize(new java.awt.Dimension(150, 21));
        cboDailyProductType.setPreferredSize(new java.awt.Dimension(150, 21));
        cboDailyProductType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboDailyProductTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAgentData.add(cboDailyProductType, gridBagConstraints);

        cboDailyProductID.setMaximumSize(new java.awt.Dimension(150, 21));
        cboDailyProductID.setMinimumSize(new java.awt.Dimension(150, 21));
        cboDailyProductID.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAgentData.add(cboDailyProductID, gridBagConstraints);

        lblCreditProductType1.setText("Security Deposit Prod Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAgentData.add(lblCreditProductType1, gridBagConstraints);

        cboCreditProductType.setMaximumSize(new java.awt.Dimension(150, 21));
        cboCreditProductType.setMinimumSize(new java.awt.Dimension(150, 21));
        cboCreditProductType.setPreferredSize(new java.awt.Dimension(150, 21));
        cboCreditProductType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCreditProductTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAgentData.add(cboCreditProductType, gridBagConstraints);

        cboCreditProductID.setMaximumSize(new java.awt.Dimension(150, 21));
        cboCreditProductID.setMinimumSize(new java.awt.Dimension(150, 21));
        cboCreditProductID.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAgentData.add(cboCreditProductID, gridBagConstraints);

        lblCreditProductId.setText("Security Deposit Prod Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAgentData.add(lblCreditProductId, gridBagConstraints);

        panSHGBtn.setMinimumSize(new java.awt.Dimension(95, 35));
        panSHGBtn.setPreferredSize(new java.awt.Dimension(95, 35));
        panSHGBtn.setLayout(new java.awt.GridBagLayout());

        btnProdNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnProdNew.setToolTipText("New");
        btnProdNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnProdNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnProdNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnProdNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProdNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSHGBtn.add(btnProdNew, gridBagConstraints);

        btnProdSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnProdSave.setToolTipText("Save");
        btnProdSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnProdSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnProdSave.setName("btnContactNoAdd"); // NOI18N
        btnProdSave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnProdSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProdSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSHGBtn.add(btnProdSave, gridBagConstraints);

        btnProdDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnProdDelete.setToolTipText("Delete");
        btnProdDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnProdDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnProdDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnProdDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProdDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSHGBtn.add(btnProdDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 112, 0, 0);
        panAgentData.add(panSHGBtn, gridBagConstraints);

        srpProdDetails.setMinimumSize(new java.awt.Dimension(50, 30));
        srpProdDetails.setNextFocusableComponent(btnAuthorize);
        srpProdDetails.setPreferredSize(new java.awt.Dimension(40, 40));

        tblProdDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ProdType", "ProdId"
            }
        ));
        tblProdDetails.setPreferredSize(new java.awt.Dimension(250, 300));
        tblProdDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblProdDetailsMousePressed(evt);
            }
        });
        srpProdDetails.setViewportView(tblProdDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.ipadx = 155;
        gridBagConstraints.ipady = 44;
        panAgentData.add(srpProdDetails, gridBagConstraints);

        cboAgentRegion.setMaximumSize(new java.awt.Dimension(100, 21));
        cboAgentRegion.setMinimumSize(new java.awt.Dimension(100, 21));
        cboAgentRegion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboAgentRegionActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAgentData.add(cboAgentRegion, gridBagConstraints);

        lblAgentRegion.setText("Region");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAgentData.add(lblAgentRegion, gridBagConstraints);

        cboAgentTransactionType.setMaximumSize(new java.awt.Dimension(100, 21));
        cboAgentTransactionType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboAgentTransactionType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboAgentTransactionTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAgentData.add(cboAgentTransactionType, gridBagConstraints);

        lblAgentTransactionType.setText("Transaction Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAgentData.add(lblAgentTransactionType, gridBagConstraints);

        panAgentType.setLayout(new java.awt.GridBagLayout());

        rdoAgent.setText("Agent");
        rdoAgent.setMinimumSize(new java.awt.Dimension(65, 21));
        rdoAgent.setPreferredSize(new java.awt.Dimension(65, 21));
        rdoAgent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoAgentActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panAgentType.add(rdoAgent, gridBagConstraints);

        rdoDealer.setText("Dealer");
        rdoDealer.setMinimumSize(new java.awt.Dimension(65, 21));
        rdoDealer.setPreferredSize(new java.awt.Dimension(65, 21));
        rdoDealer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDealerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panAgentType.add(rdoDealer, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 1);
        panAgentData.add(panAgentType, gridBagConstraints);

        lblAgentType.setText("Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        panAgentData.add(lblAgentType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgent.add(panAgentData, gridBagConstraints);

        panAgentDisplay.setMinimumSize(new java.awt.Dimension(625, 140));
        panAgentDisplay.setPreferredSize(new java.awt.Dimension(625, 140));
        panAgentDisplay.setLayout(new java.awt.GridBagLayout());

        srpAgent.setMinimumSize(new java.awt.Dimension(500, 140));
        srpAgent.setPreferredSize(new java.awt.Dimension(500, 140));

        tblAgent.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Deposit No", "Customer Name", "Date of Deposit", "Deposit Amount"
            }
        ));
        tblAgent.setPreferredSize(new java.awt.Dimension(400, 0));
        srpAgent.setViewportView(tblAgent);

        panAgentDisplay.add(srpAgent, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgent.add(panAgentDisplay, gridBagConstraints);

        tabAgent.addTab("Agent Creation", panAgent);

        panAgent1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panAgent1.setFocusCycleRoot(true);
        panAgent1.setMinimumSize(new java.awt.Dimension(620, 600));
        panAgent1.setPreferredSize(new java.awt.Dimension(620, 600));
        panAgent1.setLayout(new java.awt.GridBagLayout());

        panAgenLeaveData.setBorder(javax.swing.BorderFactory.createTitledBorder("Agent Details"));
        panAgenLeaveData.setFocusCycleRoot(true);
        panAgenLeaveData.setMinimumSize(new java.awt.Dimension(610, 500));
        panAgenLeaveData.setPreferredSize(new java.awt.Dimension(610, 500));
        panAgenLeaveData.setLayout(new java.awt.GridBagLayout());

        srpAgentLeaveDetails.setMinimumSize(new java.awt.Dimension(600, 800));
        srpAgentLeaveDetails.setPreferredSize(new java.awt.Dimension(600, 800));

        tblAgentLeaveDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Slno", "LAgentName", "LAgentId", "CAgentName", "CAgentId", "FromDate", "ToDate", "Status", "Authorize Status"
            }
        ));
        tblAgentLeaveDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblAgentLeaveDetailsMousePressed(evt);
            }
        });
        srpAgentLeaveDetails.setViewportView(tblAgentLeaveDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panAgenLeaveData.add(srpAgentLeaveDetails, gridBagConstraints);

        panAgentLeaveBtn1.setMinimumSize(new java.awt.Dimension(600, 150));
        panAgentLeaveBtn1.setPreferredSize(new java.awt.Dimension(600, 150));
        panAgentLeaveBtn1.setLayout(new java.awt.GridBagLayout());

        lblAgentID1.setText("Leave Taking Agent ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 1);
        panAgentLeaveBtn1.add(lblAgentID1, gridBagConstraints);

        panAgentID1.setMinimumSize(new java.awt.Dimension(127, 32));
        panAgentID1.setPreferredSize(new java.awt.Dimension(127, 32));
        panAgentID1.setLayout(new java.awt.GridBagLayout());

        txtLAgentID.setEditable(false);
        txtLAgentID.setEnabled(false);
        txtLAgentID.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panAgentID1.add(txtLAgentID, gridBagConstraints);

        btnLAgentID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnLAgentID.setToolTipText("Agent ID");
        btnLAgentID.setMinimumSize(new java.awt.Dimension(21, 21));
        btnLAgentID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnLAgentID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLAgentIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAgentID1.add(btnLAgentID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 1);
        panAgentLeaveBtn1.add(panAgentID1, gridBagConstraints);

        lblCommisionCreditedTo1.setText("Leave Taking Agent Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 1);
        panAgentLeaveBtn1.add(lblCommisionCreditedTo1, gridBagConstraints);

        lblLAgentName.setForeground(new java.awt.Color(51, 0, 255));
        lblLAgentName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblLAgentName.setMaximumSize(new java.awt.Dimension(200, 20));
        lblLAgentName.setMinimumSize(new java.awt.Dimension(100, 20));
        lblLAgentName.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 4);
        panAgentLeaveBtn1.add(lblLAgentName, gridBagConstraints);

        lblCreditProductType3.setText("Leave Taking Agent Region");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 1);
        panAgentLeaveBtn1.add(lblCreditProductType3, gridBagConstraints);

        cboLRegion.setMaximumSize(new java.awt.Dimension(100, 21));
        cboLRegion.setMinimumSize(new java.awt.Dimension(100, 21));
        cboLRegion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLRegionActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 4);
        panAgentLeaveBtn1.add(cboLRegion, gridBagConstraints);

        lblDepositCreditedTo1.setText("Collecting Agent Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 1);
        panAgentLeaveBtn1.add(lblDepositCreditedTo1, gridBagConstraints);

        panDep1.setMinimumSize(new java.awt.Dimension(127, 32));
        panDep1.setLayout(new java.awt.GridBagLayout());

        txtCAgentId.setEditable(false);
        txtCAgentId.setEnabled(false);
        txtCAgentId.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panDep1.add(txtCAgentId, gridBagConstraints);

        btnCAgentId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCAgentId.setToolTipText("Agent ID");
        btnCAgentId.setMinimumSize(new java.awt.Dimension(21, 21));
        btnCAgentId.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCAgentId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCAgentIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDep1.add(btnCAgentId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 1);
        panAgentLeaveBtn1.add(panDep1, gridBagConstraints);

        lblApptDate1.setText("Collecting Agent Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 1);
        panAgentLeaveBtn1.add(lblApptDate1, gridBagConstraints);

        lblCAgentName.setForeground(new java.awt.Color(51, 0, 255));
        lblCAgentName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblCAgentName.setMaximumSize(new java.awt.Dimension(200, 20));
        lblCAgentName.setMinimumSize(new java.awt.Dimension(100, 20));
        lblCAgentName.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 1);
        panAgentLeaveBtn1.add(lblCAgentName, gridBagConstraints);

        lblCreditProductType2.setText("Collecting Agent Region");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 1);
        panAgentLeaveBtn1.add(lblCreditProductType2, gridBagConstraints);

        cboCRegion.setMaximumSize(new java.awt.Dimension(100, 21));
        cboCRegion.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCRegion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCRegionActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 4);
        panAgentLeaveBtn1.add(cboCRegion, gridBagConstraints);

        lblFromDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 1);
        panAgentLeaveBtn1.add(lblFromDate, gridBagConstraints);

        tdtFromDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtFromDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 4);
        panAgentLeaveBtn1.add(tdtFromDate, gridBagConstraints);

        lblCollSuspProdtype1.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 1);
        panAgentLeaveBtn1.add(lblCollSuspProdtype1, gridBagConstraints);

        tdtToDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtToDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 4);
        panAgentLeaveBtn1.add(tdtToDate, gridBagConstraints);

        lblCollSuspProdID1.setText("Allowed Transaction Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 1);
        panAgentLeaveBtn1.add(lblCollSuspProdID1, gridBagConstraints);

        cboTxnType.setMaximumSize(new java.awt.Dimension(100, 21));
        cboTxnType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboTxnType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTxnTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 4);
        panAgentLeaveBtn1.add(cboTxnType, gridBagConstraints);

        panAgentLeaveBtn.setMinimumSize(new java.awt.Dimension(100, 35));
        panAgentLeaveBtn.setPreferredSize(new java.awt.Dimension(100, 35));
        panAgentLeaveBtn.setLayout(new java.awt.GridBagLayout());

        btnAgentLeaveNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnAgentLeaveNew.setToolTipText("New");
        btnAgentLeaveNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnAgentLeaveNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnAgentLeaveNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnAgentLeaveNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgentLeaveNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgentLeaveBtn.add(btnAgentLeaveNew, gridBagConstraints);

        btnAgentLeaveSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnAgentLeaveSave.setToolTipText("Save");
        btnAgentLeaveSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnAgentLeaveSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnAgentLeaveSave.setName("btnContactNoAdd"); // NOI18N
        btnAgentLeaveSave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnAgentLeaveSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgentLeaveSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgentLeaveBtn.add(btnAgentLeaveSave, gridBagConstraints);

        btnAgentLeaveDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnAgentLeaveDelete.setToolTipText("Delete");
        btnAgentLeaveDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnAgentLeaveDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnAgentLeaveDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnAgentLeaveDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgentLeaveDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgentLeaveBtn.add(btnAgentLeaveDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAgentLeaveBtn1.add(panAgentLeaveBtn, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgenLeaveData.add(panAgentLeaveBtn1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgent1.add(panAgenLeaveData, gridBagConstraints);

        tabAgent.addTab("Agent Leave Details", panAgent1);

        getContentPane().add(tabAgent, java.awt.BorderLayout.LINE_END);

        mnuProcess.setText("Process");

        mitNew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        mitNew.setMnemonic('N');
        mitNew.setText("New");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        mitEdit.setMnemonic('E');
        mitEdit.setText("Edit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        mitDelete.setMnemonic('D');
        mitDelete.setText("Delete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptProcess);

        mitSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
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

        mitPrint.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        mitPrint.setMnemonic('P');
        mitPrint.setText("Print");
        mitPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitPrintActionPerformed(evt);
            }
        });
        mnuProcess.add(mitPrint);
        mnuProcess.add(sptPrint);

        mitClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        mitClose.setMnemonic('l');
        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrAgent.add(mnuProcess);

        setJMenuBar(mbrAgent);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cboDailyProductTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboDailyProductTypeActionPerformed
        // TODO add your handling code here:        
         if(cboDailyProductType.getSelectedIndex() > 0) {
            String prodType = ((ComboBoxModel)cboDailyProductType.getModel()).getKeyForSelected().toString();
            observable.setDailyProdType(prodType);
            if (prodType!=null && prodType.equals("GL")) {
                cboDailyProductID.setSelectedItem("");
                cboDailyProductID.setEnabled(false);
//                lblSecurityDeposit.setText("A/C Head Name");
                observable.setDailyProdType(prodType);
            } else {
                observable.setDailyProdType(prodType);
                observable.cboDailyProductID();
                cboDailyProductID.setModel(observable.getCboDailyProductID());
                cboDailyProductID.setEnabled(true);
                cboDailyProductID.setSelectedItem(observable.getCboCreditProductID().getDataForKey(observable.getCboDailyProductID()));
//                lblSecurityDeposit.setText("Name");
            }
        }        
    }//GEN-LAST:event_cboDailyProductTypeActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus1.setText(observable.getLblStatus());
        popUp(VIEW);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed

    private void tdtApptDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtApptDateFocusLost
        // TODO add your handling code here:
        Date daybeDt=(Date) currDt.clone();
        Date appDt= DateUtil.getDateMMDDYYYY(tdtApptDate.getDateValue());
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW){
           if(appDt!=null)
            if(DateUtil.dateDiff(daybeDt,appDt)>0){
            ClientUtil.displayAlert("Appointed Date is grater then DaybeginDate");
            tdtApptDate.setDateValue("");
        }
        }

    }//GEN-LAST:event_tdtApptDateFocusLost

    private void cboProductIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboProductIDActionPerformed

    private void cboProductTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductTypeActionPerformed
        // TODO add your handling code here:
          
         if(cboProductType.getSelectedIndex() > 0) {
             String prodType = ((ComboBoxModel)cboProductType.getModel()).getKeyForSelected().toString();
             observable.setCboCollSuspPdType(prodType);
             if (prodType.equals("GL")) {
                 cboProductID.setSelectedItem("");
                 cboProductID.setEnabled(false);
//                 lblCustName.setText("A/C Head Name");
                 txtCollSuspACNum.setText("");
                 observable.setTxtCollSuspProdtype(prodType);
             } else {
                 txtCollSuspACNum.setText("");
                 observable.setCboCollSuspPdType(prodType);
                 observable.populateCboCollSuspPdId();
                 cboProductID.setModel(observable.getCboCollSuspProdID());
                 cboProductID.setEnabled(true);
                 cboProductID.setSelectedItem(observable.getCboCollSuspProdID().getDataForKey(observable.getGetCboCollSuspPDID()));
//                 lblCustName.setText("Name");
             }
         }
    }//GEN-LAST:event_cboProductTypeActionPerformed

    private void btnAgentIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgentIDActionPerformed
        // TODO add your handling code here:
//        popUp(AGENTID);
        if (rdoAgent.isSelected() == true || rdoDealer.isSelected() == true) {//Added By Revathi.L
        viewType = AGENTID;
        HashMap sourceMap = new HashMap();
        sourceMap.put("AGENT","AGENT");
        sourceMap.put("AGENT_ID","AGENT_ID");
        new CheckCustomerIdUI(this,sourceMap);
        } else {
            ClientUtil.showMessageWindow("Please select Agent/Dealer");
            return;
        }
    }//GEN-LAST:event_btnAgentIDActionPerformed

    private void txtCommisionCreditedToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCommisionCreditedToActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCommisionCreditedToActionPerformed

    private void btnCommisionCreditedToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCommisionCreditedToActionPerformed
        popUp(ACCNO);        // TODO add your handling code here:
    }//GEN-LAST:event_btnCommisionCreditedToActionPerformed

    private void btnDepositCreditedToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDepositCreditedToActionPerformed
        popUp(DEPOSITNO);        // TODO add your handling code here:
    }//GEN-LAST:event_btnDepositCreditedToActionPerformed

    private void btnCollSuspACNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCollSuspACNumActionPerformed
        // TODO add your handling code here:
        popUp(SUSPPROD_ACCN);
    }//GEN-LAST:event_btnCollSuspACNumActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        if(tabAgent.getSelectedIndex() == 0){
            observable.setSelectedTab(0);
        }else{
            observable.setSelectedTab(1);
        }
         observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
         authorizeStatus(CommonConstants.STATUS_REJECTED);
       
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        if(tabAgent.getSelectedIndex() == 0){
            observable.setSelectedTab(0);
        }else{
            observable.setSelectedTab(1);
        }
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        cboProductID.setEnabled(false);
        cboProductType.setEnabled(false);
        btnProdSave.setEnabled(false);
        btnProdDelete.setEnabled(false);
        cboCreditProductID.setEnabled(false);
        btnAgentLeaveNew.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
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
    public void authorizeStatus(String authorizeStatus) {
      
        if (viewType != AUTHORIZE){
            viewType = AUTHORIZE;
            
            //__ To Save the data in the Internal Frame...
            setModified(true);
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            if(tabAgent.getSelectedIndex() == 0){
            mapParam.put(CommonConstants.MAP_NAME, "getAgentAuthorizeList");
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
//            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            
//            whereMap.put("BRANCHID",TrueTransactMain.BRANCH_ID);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeAgentData");
            }else if(tabAgent.getSelectedIndex() == 1){
                mapParam.put(CommonConstants.MAP_NAME, "getAgentLeaveAuthorizeList");
                whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
                whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            }
            whereMap = null;
            
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
           
            btnSave.setEnabled(false);
            
            //__ If there's no data to be Authorized, call Cancel action...  
            if(!isModified()){
                setButtonEnableDisable();
                btnCancelActionPerformed(null);     
            }
            
        } else if (viewType == AUTHORIZE){
//            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AGENT ID",txtAgentID.getText());
//            singleAuthorizeMap.put(CommonConstants.BRANCH_ID,TrueTransactMain.BRANCH_ID);
            singleAuthorizeMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT,currDt.clone());
            if(rdoAgent.isSelected()==true){//Added By Revathi.L
                singleAuthorizeMap.put("TYPE", "A");
            }else{
                singleAuthorizeMap.put("TYPE", "D");
            }
            if(tabAgent.getSelectedIndex() == 0){           
            System.out.println("singleAuthorizeMap: " + singleAuthorizeMap);            
            ClientUtil.execute("authorizeAgentData", singleAuthorizeMap);
            
            super.setOpenForEditBy(observable.getStatusBy());
            super.removeEditLock(txtAgentID.getText());
           
            viewType = -1;
            btnSave.setEnabled(true);
            }else if(tabAgent.getSelectedIndex() == 1){
                try{
                    observable.setAuthorizeMap(singleAuthorizeMap);
                    observable.doActionPerform();// To perform the necessary operation depending on the Action type...
                }catch(Exception e){
                    
                }
            }
//             observable.setLblStatus(ClientConstants.RESULT_STATUS[observable.getResult()]); 
            btnCancelActionPerformed(null);
            lblStatus1.setText(authorizeStatus);
             
        }
    }    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
//        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPrintActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        ClientUtil.clearAll(this);
        if(observable.getAuthorizeStatus()!=null){
            super.removeEditLock(txtAgentID.getText());
        }
        btnCollSuspACNum.setEnabled(false);
        setModified(false);
        observable.resetForm();                 //__ Reset the fields in the UI to null...
        observable.resetTable();
        observable.resetProdTable();
        observable.resetAgentLeaveTable();
        btnLAgentID.setEnabled(false);
        btnCAgentId.setEnabled(false);
        observable.resetAgentLeaveDetails();
        ClientUtil.enableDisable(this, false);  //__ Disables the panel...
        btnAgentID.setEnabled(false);
        btnDepositCreditedTo.setEnabled(false);
        btnCommisionCreditedTo.setEnabled(false);
        
        if(!btnSave.isEnabled()){
            btnSave.setEnabled(true);
        }
        setButtonEnableDisable();               //__ Enables or Disables the buttons and menu Items depending on their previous state...
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);//Sets the Action Type to be performed...
        observable.setStatus();                 //__ To set the Value of lblStatus..
        
        viewType = -1;
        
        //__ Make the Screen Closable..
        setModified(false);
         btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        cboProductID.setSelectedItem("");
        cboProductType.setSelectedItem("");
        txtCollSuspACNum.setText("");
        lblCustNameVal.setText("");
        txtCollSuspACNum.setText("");
        cboDailyProductID.setSelectedItem("");
        lblDepositName.setText("");
        lblLstComPaiddtVal.setText("");
        btnProdNew.setEnabled(false);
        btnProdSave.setEnabled(false);
        btnProdDelete.setEnabled(false);
        
        lblLAgentName.setText("");
        lblCAgentName.setText("");
        cboCreditProductID.setSelectedItem("");
        cboAgentRegion.setSelectedItem("");
        cboAgentTransactionType.setSelectedItem("");
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        if(tabAgent.getSelectedIndex() == 0){
            observable.setSelectedTab(0);
            if (!(rdoAgent.isSelected() || rdoDealer.isSelected())) {   //Added By Revathi.L   Ref. Mr Abi
                ClientUtil.showMessageWindow("Please Select Type Agent/Dealer !!!");
                return;
            }
        updateOBFields();
        //To display an alert if the mandatory fields are not properly inputted, else proceed with normal operation
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panAgent);
        if(!(tblProdDetails.getRowCount()>0)){
            mandatoryMessage = "Please select Daily products for Agent!!!";
        }
        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
            displayAlert(mandatoryMessage);
            return;
        }else{
            observable.doAction();// To perform the necessary operation depending on the Action type...
            //__ If the Operation is Not Failed, Clear the Screen...
            if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
                HashMap lockMap = new HashMap();
                ArrayList lst = new ArrayList();
                lst.add("AGENT ID");
                lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                if (observable.getProxyReturnMap()!=null) {
                    if (observable.getProxyReturnMap().containsKey("AGENT ID")) {
                        lockMap.put("AGENT ID",observable.getProxyReturnMap().get("AGENT ID"));
                    }
                }
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    lockMap.put("AGENT ID", txtAgentID.getText());
                }
                setEditLockMap(lockMap);
                setEditLock();
                
                if( viewType == EDIT ){
//                    super.removeEditLock(txtAgentID.getText());
                }
                btnCancelActionPerformed(null);
            }
        }
        }else if(tabAgent.getSelectedIndex() == 1){
			observable.setSelectedTab(1);
            if(tblAgentLeaveDetails.getRowCount()==0){
                ClientUtil.showAlertWindow("Please enter atleast one leave details");
            }else{
                observable.doAction();
                btnCancelActionPerformed(null);
            }
        }
        
         //__ Make the Screen Closable..
        setModified(false);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        lblCustNameVal.setText("");
        txtCollSuspACNum.setText("");
        cboDailyProductID.setSelectedItem("");
        lblDepositName.setText("");
        lblLstComPaiddtVal.setText("");
        btnCommisionCreditedTo.setEnabled(false);
        btnDepositCreditedTo.setEnabled(false);
        btnCollSuspACNum.setEnabled(false);
        
    }//GEN-LAST:event_btnSaveActionPerformed
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE); //__ Sets the Action Type to be performed...
        popUp(DELETE);
         btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        cboProductID.setEnabled(false);
        
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT); //__ Sets the Action Type to be performed...
        if(tabAgent.getSelectedIndex() == 0){                    
            popUp(EDIT);
            btnReject.setEnabled(false);
            btnAuthorize.setEnabled(false);
            btnException.setEnabled(false);
            btnCommisionCreditedTo.setEnabled(true);
            btnDepositCreditedTo.setEnabled(true);
            btnCollSuspACNum.setEnabled(true);
            //setButtonEnableDisable(); 
            btnSave.setEnabled(true);
            btnCancel.setEnabled(true);
            btnProdNew.setEnabled(true);
            btnProdSave.setEnabled(false);
            btnProdDelete.setEnabled(false);
            cboDailyProductID.setEnabled(false);
            cboDailyProductType.setEnabled(false); 
        }else if(tabAgent.getSelectedIndex() == 1){
            popUp(EDIT_LEAVE_DETAILS);            
            btnSave.setEnabled(true);
            btnCancel.setEnabled(true);
            btnNew.setEnabled(false);
            btnDelete.setEnabled(false);
            btnView.setEnabled(false);
            btnAuthorize.setEnabled(false);
            btnReject.setEnabled(false);
            btnEdit.setEnabled(false);
        }
    }//GEN-LAST:event_btnEditActionPerformed
    private void popUp(int field) {
        final HashMap viewMap = new HashMap();
        viewType = field;
        HashMap whereMap = new HashMap();
        HashMap operativeMap = new HashMap();
        HashMap depositMap = new HashMap();
        
        
        
        whereMap.put("BRANCHID", getSelectedBranchID());
        
        if(field==EDIT || field==DELETE || field== AUTHORIZE || field== VIEW){ //Edit=0 and Delete=1
            
            ArrayList lst = new ArrayList();
            lst.add("AGENT ID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "viewAgentData");
            new ViewAll(this, viewMap, false).show();
            
//            viewMap.put(CommonConstants.MAP_WHERE, operativeMap);
//            viewMap.put(CommonConstants.MAP_NAME, "viewOperativeDetails");
//            new ViewAll(this, viewMap, false).show();
//
//            viewMap.put(CommonConstants.MAP_WHERE, depositMap);
//            viewMap.put(CommonConstants.MAP_NAME, "viewDepositIdForCustomer");
//            new ViewAll(this, viewMap, false).show();
            
        }else if(field==AGENTID){
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "getAgentDetails");
            new ViewAll(this, viewMap, true).show();
        } else if(field == ACCNO) {
            operativeMap.put("AGENT_ID",txtAgentID.getText());
            viewMap.put(CommonConstants.MAP_WHERE, operativeMap);
            viewMap.put(CommonConstants.MAP_NAME, "getOperativeDetails");
            new ViewAll(this, viewMap, true).show();
            System.out.println("getOperativeDetails :" +operativeMap);            
        } else if(field == DEPOSITNO) {
//            depositMap.put("AGENT_ID",txtAgentID.getText());
//            viewMap.put(CommonConstants.MAP_WHERE, depositMap);
//            System.out.println("depositMap : " +depositMap);
//            viewMap.put(CommonConstants.MAP_NAME, "getOperativeDetails");
////            viewMap.put(CommonConstants.MAP_NAME, "getDepositIdForCustomer");
//            new ViewAll(this, viewMap, true).show();
            if(cboCreditProductType.getSelectedIndex() > 0){
                if(((ComboBoxModel)cboCreditProductType.getModel()).getKeyForSelected().equals("GL")){
                     viewMap.put(CommonConstants.MAP_NAME,"Cash.getSelectAcctHead");
                     new ViewAll(this, viewMap, true).show();
                } else{
                     String prodType=((ComboBoxModel)cboCreditProductType.getModel()).getKeyForSelected().toString();
                     if(cboCreditProductID.getSelectedIndex()>0){
                         String prodID=((ComboBoxModel)cboCreditProductID.getModel()).getKeyForSelected().toString();
                         depositMap.put("PROD_ID",prodID);
                         depositMap.put("SELECTED_BRANCH", TrueTransactMain.BRANCH_ID);
                         viewMap.put(CommonConstants.MAP_NAME,"Cash.getAccountList"+prodType);
                         viewMap.put(CommonConstants.MAP_WHERE,depositMap);
                         new ViewAll(this, viewMap, true).show();
                     }else{
                        ClientUtil.displayAlert("Product Id Should not be Empty!!!");
                        return;                         
                     }
                }  
            }else{
                ClientUtil.displayAlert("Product Type Should not be Empty!!!");
                return;
            }
        } else if(field==SUSPPROD_ACCN){
            if(cboProductType.getSelectedIndex()>0){
                if(((ComboBoxModel)cboProductType.getModel()).getKeyForSelected().equals("GL")){
                    viewMap.put(CommonConstants.MAP_NAME,"Cash.getSelectAcctHead");
                    new ViewAll(this, viewMap, true).show();
                } else{
                    String prodType=((ComboBoxModel)cboProductType.getModel()).getKeyForSelected().toString();
                    if(cboProductID.getSelectedIndex()>0){
                        String prodID=((ComboBoxModel)cboProductID.getModel()).getKeyForSelected().toString();
                        depositMap.put("PROD_ID",prodID);
                        depositMap.put("SELECTED_BRANCH", TrueTransactMain.BRANCH_ID);
                        viewMap.put(CommonConstants.MAP_NAME,"Cash.getAccountList"+prodType);
                        viewMap.put(CommonConstants.MAP_WHERE,depositMap);
                        new ViewAll(this, viewMap, true).show();
                    }else{
                        ClientUtil.displayAlert("Product Id Should not be Empty!!!");
                        return;
                    }
                }
            }else{
                ClientUtil.displayAlert("Product Type Should not be Empty!!!");
                return;
            }
        } else if (field==SUSPPROD_ID){
            
        } else if(field==SUSPPROD_TYPE){
            depositMap.put("TYPE", "PRODUCTTYPE");
            viewMap.put(CommonConstants.MAP_WHERE,depositMap);
            viewMap.put(CommonConstants.MAP_NAME, "getLookUpUI");
            new ViewAll(this, viewMap, true).show();
        } else if(field == LAGENT_ID){
            depositMap.put("BRANCH_ID", getSelectedBranchID());
            depositMap.put("CHECK_DATE",currDt.clone());
            viewMap.put(CommonConstants.MAP_WHERE,depositMap);
            viewMap.put(CommonConstants.MAP_NAME, "viewAgentLeaveDetailsData");
            new ViewAll(this, viewMap, true).show();
        } else if(field == CAGENT_ID){
            if(txtLAgentID.getText().length()>0){
                depositMap.remove("BRANCH_ID");
                depositMap.put("AGENT_ID", txtLAgentID.getText());
                depositMap.put("CHECK_DATE",currDt.clone());                
                viewMap.put(CommonConstants.MAP_WHERE,depositMap);
                viewMap.put(CommonConstants.MAP_NAME, "viewAgentLeaveDetailsData");
                new ViewAll(this, viewMap, true).show();
            }else{
                ClientUtil.showAlertWindow("Please select leave taking agent id");
                return;
            }
        }else if(field == EDIT_LEAVE_DETAILS){
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "viewAgentLeaveDetailsRecord");
            new ViewAll(this, viewMap, false).show();
        }
    }
    
    // this method is called automatically from ViewAll...
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        HashMap OAprodDescMap = new HashMap();
        HashMap depProdDescMap = new HashMap();
        
        System.out.println("hash:  " + hash);
        
        if (viewType==EDIT || viewType==DELETE || viewType==AUTHORIZE || viewType==VIEW) {
            if(tabAgent.getSelectedIndex() == 0){
            hash.put("AGENTID", hash.get("AGENT ID"));
            hash.put("BRANCHID", getSelectedBranchID());            
                if (CommonUtil.convertObjToStr(hash.get("AGENT_TYPE")).length() > 0) {//Added By Revathi.L
                    if (CommonUtil.convertObjToStr(hash.get("AGENT_TYPE")).equals("AGENT")) {
                        hash.put("TYPE", "A");
                    } else {
                        hash.put("TYPE", "D");
                    }
                }
            observable.populateData(hash);   
            tblProdDetails.setModel(observable.getTblProdDetails());
            //__ Called to display the Data in the UI fields...
            String OAaccountNo = CommonUtil.convertObjToStr(hash.get("OPERATIVE ACCOUNT NO"));
            //__ To Set the Value of the Agent Name...
            observable.setLblName(CommonUtil.convertObjToStr(hash.get("AGENT NAME")));
            observable.getOperativeProdId(OAaccountNo);
            lblProdIdlName.setText(observable.getLblProdIdlName());
            if(txtDepositCreditedTo.getText()!=null){
                String depositNo = txtDepositCreditedTo.getText();
                observable.getDepositProdId(CommonUtil.convertObjToStr(txtDepositCreditedTo.getText()));
                lblDepositName.setText(observable.getLblDepositName());
            }
            observable.setAgentTabData();
            }else if(tabAgent.getSelectedIndex() == 1){
                hash.put("L_AGENT_ID", hash.get("AGENT ID"));
                hash.put("BRANCHID", getSelectedBranchID());            
                observable.populateData(hash);  
                tblAgentLeaveDetails.setModel(observable.getTblAgentLeaveDetails());
                btnAgentLeaveNew.setEnabled(true);
            }
            //__ If the Action type is Delete...
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || viewType==AUTHORIZE || viewType==VIEW ) {
                ClientUtil.enableDisable(this, false);     //__ Disables the panel...
                
                //__ If the Action Type is Edit...
            }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                ClientUtil.enableDisable(this, true);     //__ Anable the panel...
                
                //__ Get the Auth Status...
                final String AUTHSTATUS = observable.getAuthStatus();
                if(AUTHSTATUS.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)){
                    //__ Make Remarks as Editable an rest all UnEditable...
                    setPanEnable();
                    
                }else{
                    txtAgentID.setEditable(false);
                    //                    txtCommisionCreditedTo.setEditable(false);
                    txtDepositCreditedTo.setEditable(true);
                }
            }
            if(viewType==AUTHORIZE) {
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                cboCreditProductID.setEnabled(false);
            }
            
            btnAgentID.setEnabled(false);
            observable.setStatus();             //__ To set the Value of lblStatus...
            setButtonEnableDisable();           //__ Enables or Disables the buttons and menu Items depending on their previous state...
            ClientUtil.enableDisable(panAgentType, false);
        }else if (viewType==AGENTID) {
            //Added By Revathi.L Reff by srinath sir 27/06/2017
            HashMap agentMap = new HashMap();
            agentMap.put("AGENT_ID", hash.get("CUST_ID"));
            if (rdoAgent.isSelected() == true) {
                agentMap.put("TYPE","A");
            }else{
                agentMap.put("TYPE","D");
            }
            List checkAgentLst = ClientUtil.executeQuery("checkingAgentIDExist", agentMap);
            if (checkAgentLst != null && checkAgentLst.size() > 0) {
                ClientUtil.showMessageWindow("Already Agent ID record exists for this customer.");
                return;
            }
            agentMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            List agentLst = ClientUtil.executeQuery("getOperativeDetailsForAgentMaster", agentMap);
            if (agentLst != null && agentLst.size() > 0) {
            } else {
                ClientUtil.showMessageWindow("Please create SB Account for this customer");
                return;
            }
            if(hash.containsKey("CUST_ID") ||hash.containsKey("NAME")){
                hash.put("AGENT ID",hash.get("CUST_ID"));
                hash.put("AGENT NAME",hash.get("NAME"));
            }
            //__ To Set the Data Regarding the Customer in the Screen...
            txtAgentID.setText(CommonUtil.convertObjToStr(hash.get("AGENT ID")));
            lblNameValue.setText(CommonUtil.convertObjToStr(hash.get("AGENT NAME")));
            ClientUtil.enableDisable(panAgentType, false);
            
            //            observable.setTxtAgentID(CommonUtil.convertObjToStr(hash.get("AGENT ID")));
            //            observable.setLblName(CommonUtil.convertObjToStr(hash.get("AGENT NAME")));
            //            observable.setAgentTabData();
            //
            //            observable.ttNotifyObservers();
            
        } else if(viewType == ACCNO) {
            txtCommisionCreditedTo.setText(CommonUtil.convertObjToStr(hash.get("OA_ACT_NUM")));
            //            OAprodDescMap.put("PROD_DESC", hash.get("PROD_ID"));
            //            List lst = ClientUtil.executeQuery("getOAProdDescription", hash);
            //            OAprodDescMap = (HashMap)lst.get(0);
            lblProdIdlName.setText(lblNameValue.getText());
            //            lblProdIdlName.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMERNAME")));
        } else if(viewType == DEPOSITNO) {
            //            depProdDescMap = new HashMap();
            //            depProdDescMap.put("PROD_ID",((ComboBoxModel)cboCreditProductID.getModel()).getKeyForSelected().toString());
            //            List lst = ClientUtil.executeQuery("getOAProdDescription", depProdDescMap);
            //            if(lst!=null && lst.size()>0){
            //                depProdDescMap = (HashMap)lst.get(0);
            //if(((ComboBoxModel)cboDailyProductType.getModel()).getKeyForSelected().equals("GL")){
            if (((ComboBoxModel) cboCreditProductType.getModel()).getKeyForSelected().equals("GL")) {
                txtDepositCreditedTo.setText(CommonUtil.convertObjToStr(hash.get("A/C HEAD")));
                lblDepositName.setText(CommonUtil.convertObjToStr(hash.get("A/C HEAD DESCRIPTION")));
            }else{
                if(((ComboBoxModel)cboDailyProductType.getModel()).getKeyForSelected().equals("TD")){
                    hash.put("ACCOUNTNO", hash.get("ACCOUNTNO")+"_1");
                }
                txtDepositCreditedTo.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
                lblDepositName.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMERNAME")));
            }
        } else if(viewType==SUSPPROD_ACCN){
            if(((ComboBoxModel)cboProductType.getModel()).getKeyForSelected().equals("GL")){
                txtCollSuspACNum.setText(CommonUtil.convertObjToStr(hash.get("A/C HEAD")));
                lblCustNameVal.setText(CommonUtil.convertObjToStr(hash.get("A/C HEAD DESCRIPTION")));
                //                  lblCustName.setText("A/C HEAD NAME");
            } else{
                if(((ComboBoxModel)cboProductType.getModel()).getKeyForSelected().equals("TD")){
                    hash.put("ACCOUNTNO", hash.get("ACCOUNTNO")+"_1");
                }
                txtCollSuspACNum.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
                lblCustNameVal.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMERNAME")));
                //             lblCustName.setText("NAME");
                
            }
        } else if(viewType == LAGENT_ID){
            txtLAgentID.setText(CommonUtil.convertObjToStr(hash.get("AGENT ID")));
            lblLAgentName.setText(CommonUtil.convertObjToStr(hash.get("AGENT NAME")));
        } else if(viewType == CAGENT_ID){
            txtCAgentId.setText(CommonUtil.convertObjToStr(hash.get("AGENT ID")));
            lblCAgentName.setText(CommonUtil.convertObjToStr(hash.get("AGENT NAME")));
        }else if(viewType == EDIT_LEAVE_DETAILS){
            hash.put("L_AGENT_ID", hash.get("AGENT ID"));
            hash.put("BRANCHID", getSelectedBranchID());            
            observable.populateData(hash);  
            tblAgentLeaveDetails.setModel(observable.getTblAgentLeaveDetails());
            btnAgentLeaveNew.setEnabled(true);
        }
        
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }
    
    private void setPanEnable(){
        txtAgentID.setEditable(false);
        txtAgentID.setEnabled(false);
        
        tdtApptDate.setEnabled(false);
        
        txtRemarks.setEnabled(true);
        
        txtRemarks.setEditable(true);
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        if(tabAgent.getSelectedIndex() == 0){
            observable.resetForm();               // to Reset all the Fields and Status in UI...
            observable.resetTable();
            setButtonEnableDisable();             // Enables/Disables the necessary buttons and menu items...
            ClientUtil.enableDisable(this, true); // Enables the panel...
            observable.setActionType(ClientConstants.ACTIONTYPE_NEW);   //Sets the Action Type to be performed...
            observable.setStatus();               // To set the Value of lblStatus...

            txtAgentID.setEnabled(true);
            btnAgentID.setEnabled(true);
            btnReject.setEnabled(false);
            btnAuthorize.setEnabled(false);
            btnException.setEnabled(false);
            btnCommisionCreditedTo.setEnabled(true);
            btnDepositCreditedTo.setEnabled(true);        
            btnCollSuspACNum.setEnabled(true);
            cboDailyProductID.setSelectedItem("");
            lblDepositName.setText("");
            lblCustNameVal.setText("");
            btnProdNew.setEnabled(true);
            btnProdSave.setEnabled(false);
            btnProdDelete.setEnabled(false);
            cboDailyProductID.setEnabled(false);
            cboDailyProductType.setEnabled(false);        
    //        btnCollSuspProdId.setEnabled(true);
    //        btnCollSuspProdtype.setEnabled(true);
            //__ To Save the data in the Internal Frame...
        }else if(tabAgent.getSelectedIndex() == 1){
            observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
            btnAgentLeaveNew.setEnabled(true);
            btnReject.setEnabled(false);
            btnAuthorize.setEnabled(false);
            btnException.setEnabled(false);
            btnSave.setEnabled(true);
            btnCancel.setEnabled(iconable);
            btnNew.setEnabled(false);
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
            btnSave.setEnabled(true);
            btnCancel.setEnabled(true);
            btnView.setEnabled(false);
        }
        setModified(true);
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        btnCloseActionPerformed(null);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void mitPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitPrintActionPerformed
        // TODO add your handling code here:
        btnPrintActionPerformed(null);
    }//GEN-LAST:event_mitPrintActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // TODO add your handling code here:
        btnCancelActionPerformed(null);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // TODO add your handling code here:
        btnSaveActionPerformed(null);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // TODO add your handling code here:
        btnDeleteActionPerformed(null);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // TODO add your handling code here:
        btnEditActionPerformed(null);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
        btnNewActionPerformed(null);
    }//GEN-LAST:event_mitNewActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm

private void cboCreditProductTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCreditProductTypeActionPerformed
// TODO add your handling code here:
    if(cboCreditProductType.getSelectedIndex() > 0) {
            String prodType = ((ComboBoxModel)cboCreditProductType.getModel()).getKeyForSelected().toString();
            observable.setCboCreditPdType(prodType);
            if (prodType!=null && prodType.equals("GL")) {
//                cboDailyProductID.setSelectedItem("");
//                cboDailyProductID.setEnabled(false);
                cboCreditProductID.setSelectedItem("");
                cboCreditProductID.setEnabled(false);
                txtDepositCreditedTo.setText("");
//                lblSecurityDeposit.setText("A/C Head Name");
                observable.setCboCreditPdType(prodType);
            } else {
                observable.setCboCreditPdType(prodType);
                txtDepositCreditedTo.setText("");
                observable.cboCreditProductId();
                cboCreditProductID.setModel(observable.getCboCreditProductID());
                cboCreditProductID.setEnabled(true);
                cboCreditProductID.setSelectedItem(observable.getCboCreditProductID().getDataForKey(observable.getCboCreditProductID()));
//                lblSecurityDeposit.setText("Name");
            }
        }   
}//GEN-LAST:event_cboCreditProductTypeActionPerformed

private void btnProdNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProdNewActionPerformed
    // TODO add your handling code here:
    cboDailyProductID.setEnabled(true);
    cboDailyProductType.setEnabled(true); 
    cboDailyProductID.setSelectedItem("");
    cboDailyProductType.setSelectedItem("");
    updateMode = false;
    btnProdSave.setEnabled(true);
    btnProdDelete.setEnabled(false);
}//GEN-LAST:event_btnProdNewActionPerformed

private void btnProdSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProdSaveActionPerformed
    // TODO add your handling code here:
        try {
            if(txtAgentID.getText()!=null && !txtAgentID.getText().equals("") && txtAgentID.getText().length()>0){
                if(CommonUtil.convertObjToStr(cboDailyProductType.getSelectedItem())!=null && CommonUtil.convertObjToStr(cboDailyProductID.getSelectedItem())!=null
                        && !CommonUtil.convertObjToStr(cboDailyProductType.getSelectedItem()).equals("") && !CommonUtil.convertObjToStr(cboDailyProductID.getSelectedItem()).equals("")){                     
                    observable.setTxtAgentID(txtAgentID.getText());
                    observable.setDailyProdType(CommonUtil.convertObjToStr(((ComboBoxModel) cboDailyProductType.getModel()).getKeyForSelected()));
                    observable.setDailyProdID(CommonUtil.convertObjToStr(((ComboBoxModel) cboDailyProductID.getModel()).getKeyForSelected()));
                    for (int i = 0; i < tblProdDetails.getRowCount(); i++) {
                        if(CommonUtil.convertObjToStr(tblProdDetails.getValueAt(i, 1)).equals(CommonUtil.convertObjToStr(observable.getDailyProdID()))) {
                            ClientUtil.showAlertWindow("Daily product Id already present in grid!!");
                            return;
                        }
                    }
                    observable.addToTable(updateTab, updateMode);
                    tblProdDetails.setModel(observable.getTblProdDetails());
                    cboDailyProductID.setSelectedItem("");
                    cboDailyProductType.setSelectedItem("");
                    btnProdNew.setEnabled(true); 
                    btnProdSave.setEnabled(false);
                    cboDailyProductID.setEnabled(false);
                    cboDailyProductType.setEnabled(false);
                } else{
                    ClientUtil.showAlertWindow("Daily product type and Daily product Id should't be empty!!!");
                    return;
                }
            }else{
                ClientUtil.showAlertWindow("Please select the Agent Id!!!");
                return;
            } 
    } catch (Exception e) {
        e.printStackTrace();
    }
}//GEN-LAST:event_btnProdSaveActionPerformed

private void btnProdDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProdDeleteActionPerformed
    // TODO add your handling code here:
    try{
        String prodId = CommonUtil.convertObjToStr(tblProdDetails.getValueAt(tblProdDetails.getSelectedRow(), 1));
        observable.deleteTableData(prodId, tblProdDetails.getSelectedRow());
        observable.resetProdValues();
        cboDailyProductID.setSelectedItem("");
        cboDailyProductType.setSelectedItem("");
        btnProdNew.setEnabled(true);
        btnProdDelete.setEnabled(false);
        btnProdSave.setEnabled(false);
    } catch (Exception e) {
        e.printStackTrace();
    }
}//GEN-LAST:event_btnProdDeleteActionPerformed

private void tblProdDetailsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProdDetailsMousePressed
// TODO add your handling code here:
    updateMode = true;
    updateTab = tblProdDetails.getSelectedRow();
    observable.setNewData(false);
    observable.updatTabelData(CommonUtil.convertObjToStr(tblProdDetails.getValueAt(tblProdDetails.getSelectedRow(), 1)));
    cboDailyProductType.setSelectedItem(observable.getCboDailyProductType().getDataForKey(observable.getDailyProdType()));
    cboDailyProductID.setSelectedItem(observable.getCboDailyProductID().getDataForKey(observable.getDailyProdID()));
    if(observable.getActionType() != ClientConstants.ACTIONTYPE_AUTHORIZE){
        btnProdNew.setEnabled(true);
        btnProdSave.setEnabled(false);
        btnProdDelete.setEnabled(true);
        cboDailyProductID.setEnabled(false);
        cboDailyProductType.setEnabled(false);
    }else{
        btnProdNew.setEnabled(false);
        btnProdSave.setEnabled(false);
        btnProdDelete.setEnabled(false);
        cboDailyProductID.setEnabled(false);
        cboDailyProductType.setEnabled(false);
    }
}//GEN-LAST:event_tblProdDetailsMousePressed

    private void cboAgentRegionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboAgentRegionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboAgentRegionActionPerformed

    private void cboAgentTransactionTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboAgentTransactionTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboAgentTransactionTypeActionPerformed

    private void btnLAgentIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLAgentIDActionPerformed
        // TODO add your handling code here:
        viewType = LAGENT_ID;
        popUp(LAGENT_ID);
    }//GEN-LAST:event_btnLAgentIDActionPerformed

    private void btnCAgentIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCAgentIdActionPerformed
        // TODO add your handling code here:
        viewType = CAGENT_ID;
        popUp(CAGENT_ID);
    }//GEN-LAST:event_btnCAgentIdActionPerformed

    private void tdtToDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtToDateFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tdtToDateFocusLost

    private void cboTxnTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTxnTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboTxnTypeActionPerformed

    private void cboLRegionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLRegionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboLRegionActionPerformed

    private void tdtFromDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtFromDateFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tdtFromDateFocusLost
    public void populateAgentLeaveDetails() {
        txtLAgentID.setText(observable.getTxtLAgentID());
        txtCAgentId.setText(observable.getTxtCAgentId());
        lblLAgentName.setText(observable.getLblLAgentName());
        lblCAgentName.setText(observable.getLblCAgentName());
        tdtFromDate.setDateValue(observable.getTdtFromDate());
        tdtToDate.setDateValue(observable.getTdtToDate());
        cboLRegion.setSelectedItem(((ComboBoxModel)cboLRegion.getModel()).getDataForKey(observable.getCboLRegionValue()));
        cboCRegion.setSelectedItem(((ComboBoxModel)cboCRegion.getModel()).getDataForKey(observable.getCboCRegionValue()));
        cboTxnType.setSelectedItem(((ComboBoxModel)cboTxnType.getModel()).getDataForKey(observable.getCboTxnTypeValue()));
    }
    private void btnAgentLeaveNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgentLeaveNewActionPerformed
        // TODO add your handling code here:
        updateTab = -1;
        updateAgentLeaveMode = false;
        resetAgentLeaveDetails();
        if(tblAgentLeaveDetails.getRowCount()>0){
            if(updateAgentLeaveMode){
                observable.setNewData(false);
            }else{
                observable.setNewData(true);
            }
            String agentId = CommonUtil.convertObjToStr(tblAgentLeaveDetails.getValueAt(tblAgentLeaveDetails.getRowCount()-1,1));            
            String agentName = CommonUtil.convertObjToStr(tblAgentLeaveDetails.getValueAt(tblAgentLeaveDetails.getRowCount()-1,2));            
            txtLAgentID.setText(CommonUtil.convertObjToStr(agentId));
            lblLAgentName.setText(CommonUtil.convertObjToStr(agentName));
            cboLRegion.setSelectedItem(((ComboBoxModel)cboLRegion.getModel()).getDataForKey(observable.getCboLRValue()));
            cboLRegion.setEnabled(true);
            btnLAgentID.setEnabled(false);
        }else{
            updateAgentLeaveMode = false;
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
                btnLAgentID.setEnabled(true);
                cboLRegion.setEnabled(true);
                btnCAgentId.setEnabled(true);
            }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                observable.setNewData(true);
                btnLAgentID.setEnabled(false);
                lblLAgentName.setEnabled(true);
                cboLRegion.setEnabled(false);            
            }
        }
        btnCAgentId.setEnabled(true);
        lblCAgentName.setEnabled(true);
        cboCRegion.setEnabled(true);
        tdtFromDate.setEnabled(true);
        tdtToDate.setEnabled(true);
        cboTxnType.setEnabled(true);
        btnAgentLeaveNew.setEnabled(false);
        btnAgentLeaveSave.setEnabled(true);
        btnAgentLeaveDelete.setEnabled(false);
    }//GEN-LAST:event_btnAgentLeaveNewActionPerformed

    private void btnAgentLeaveSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgentLeaveSaveActionPerformed
        // TODO add your handling code here:
        if(txtLAgentID.getText().length() == 0){
            ClientUtil.showAlertWindow("Leave taking agent id should not be empty");
            return;
        }else if(CommonUtil.convertObjToStr(cboLRegion.getSelectedItem()).equals("")){
            ClientUtil.showAlertWindow("Leave taking agent region should not be empty");
            return;            
        }else if(txtCAgentId.getText().length() == 0){
            ClientUtil.showAlertWindow("Collecting agent id should not be empty");
            return;            
        }else if(CommonUtil.convertObjToStr(cboCRegion.getSelectedItem()).equals("")){
            ClientUtil.showAlertWindow("Collecting agent region should not be empty");
            return;            
        }else if(tdtFromDate.getDateValue().equals("")){
            ClientUtil.showAlertWindow("From date should not be empty");
            return;            
        }else if(tdtToDate.getDateValue().equals("")){
            ClientUtil.showAlertWindow("To Date should not be empty");
            return;            
        }else if(CommonUtil.convertObjToStr(cboTxnType.getSelectedItem()).equals("")){
            ClientUtil.showAlertWindow("Transaction type should not be empty");
            return;            
        }else{
            if(!CommonUtil.convertObjToStr(tdtFromDate.getDateValue()).equals("") && DateUtil.dateDiff((Date)currDt.clone(), DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue()))<0){
                ClientUtil.showAlertWindow("From date should be greater than today's date");
                return;                            
            }else if(!CommonUtil.convertObjToStr(tdtToDate.getDateValue()).equals("") && DateUtil.dateDiff((Date)currDt.clone(), DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue()))<0){
                ClientUtil.showAlertWindow("To date should be greater than today's date");
                return;            
            }else if(DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue()), DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue()))<0){
                ClientUtil.showAlertWindow("To date should be greater than from date");
                return;            
            }else{
                updateAgentLeaveOBFields();
                observable.addToAgentLeaveDetailsTable(updateTab,updateAgentLeaveMode);
                tblAgentLeaveDetails.setModel(observable.getTblAgentLeaveDetails());
                observable.resetAgentLeaveDetails();
                resetAgentLeaveDetails();
                ClientUtil.enableDisable(panAgenLeaveData,false);
                enableDisableButton(false);
                btnAgentLeaveNew.setEnabled(true);
                btnAgentLeaveSave.setEnabled(false);
                btnAgentLeaveDelete.setEnabled(false);
                updateAgentLeaveMode = false;                
            }
        }
    }//GEN-LAST:event_btnAgentLeaveSaveActionPerformed
    private void resetAgentLeaveDetails(){
//        txtLAgentID.setText("");
//        lblLAgentName.setText("");
//        cboLRegion.setSelectedItem("");
        txtCAgentId.setText("");
        lblCAgentName.setText("");
        cboCRegion.setSelectedItem("");
        tdtFromDate.setDateValue("");
        tdtToDate.setDateValue("");
        cboTxnType.setSelectedItem("");
    }
    private void btnAgentLeaveDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgentLeaveDeleteActionPerformed
        // TODO add your handling code here:
        String s =  CommonUtil.convertObjToStr(tblAgentLeaveDetails.getValueAt(tblAgentLeaveDetails.getSelectedRow(),0));
        String status = CommonUtil.convertObjToStr(tblAgentLeaveDetails.getValueAt(tblAgentLeaveDetails.getSelectedRow(),8));
        if(status!= null && status.length()>0 && (status.equals("AUTHORIZED") || status.equals("REJECTED"))){
            ClientUtil.showAlertWindow("Selected details already authorized");
            btnAgentLeaveNew.setEnabled(true);
            btnAgentLeaveDelete.setEnabled(false);
            btnAgentLeaveSave.setEnabled(false);
            return;
        }else{
            observable.deleteAgentLeaveTableData(s,tblAgentLeaveDetails.getSelectedRow());
            observable.resetAgentLeaveDetails();
            resetAgentLeaveDetails();
            ClientUtil.enableDisable(panAgenLeaveData,false);
            enableDisableButton(false);
            btnAgentLeaveSave.setEnabled(false);
            btnAgentLeaveNew.setEnabled(true);
            btnAgentLeaveNew.setEnabled(false);
            
        }
    }//GEN-LAST:event_btnAgentLeaveDeleteActionPerformed
    private void enableDisableButton(boolean flag){
        txtLAgentID.setEnabled(false);
        //lblLAgentName.setEnabled(false);
        cboLRegion.setEnabled(false);
        txtCAgentId.setEnabled(false);
        //lblCAgentName.setEnabled(false);
        cboCRegion.setEnabled(false);
        tdtFromDate.setEnabled(false);
        tdtToDate.setEnabled(false);
        cboTxnType.setEnabled(false);
    }
    private void cboCRegionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCRegionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboCRegionActionPerformed

    private void tblAgentLeaveDetailsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAgentLeaveDetailsMousePressed
                // TODO add your handling code here:
//        updateAgentLeaveOBFields();
        updateAgentLeaveMode = true;
        updateTab = tblAgentLeaveDetails.getSelectedRow();
        observable.setNewData(false);
        String st = CommonUtil.convertObjToStr(tblAgentLeaveDetails.getValueAt(tblAgentLeaveDetails.getSelectedRow(),0));        
        String authorizeStatus = CommonUtil.convertObjToStr(tblAgentLeaveDetails.getValueAt(tblAgentLeaveDetails.getSelectedRow(),8));        
        observable.populateAgentLeaveDetails(st);
//        updateAgentLeaveFields();
        populateAgentLeaveDetails();
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE||observable.getActionType()==ClientConstants.ACTIONTYPE_REJECT ||
        observable.getActionType()==ClientConstants.ACTIONTYPE_VIEW || observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE ||
        observable.getActionType()==ClientConstants.ACTIONTYPE_EXCEPTION){
            enableDisableButton(false);
            ClientUtil.enableDisable(panAgenLeaveData,false);            
        }else {
            enableDisableButton(true);
            btnAgentLeaveNew.setEnabled(false);
            btnAgentLeaveDelete.setEnabled(true);
            btnAgentLeaveSave.setEnabled(true);
            ClientUtil.enableDisable(panAgenLeaveData,true);
//            txtNoOfCheques.setEnabled(false);
            if(authorizeStatus != null && authorizeStatus.length()>0 && (authorizeStatus.equals("AUTHORIZED")||authorizeStatus.equals("REJECTED"))){
                enableDisableButton(false);                
                btnAgentLeaveNew.setEnabled(true);
                btnAgentLeaveDelete.setEnabled(false);
                btnAgentLeaveSave.setEnabled(false);
            }            
        }
        if(observable.getActionType()!=ClientConstants.ACTIONTYPE_NEW && tblAgentLeaveDetails.getSelectedRowCount()>0 && evt.getClickCount() == 2){
            HashMap whereMap = new HashMap();
            //     whereMap.put("BORROWING_NO", txtSBInternalAccNo.getText());
            String slNo = CommonUtil.convertObjToStr(tblAgentLeaveDetails.getValueAt(tblAgentLeaveDetails.getSelectedRow(), 0));
            whereMap.put("SL_NO", slNo);
            //            TableDialogUI tableData = new TableDialogUI("getInvestmentIssuedChequeDetails", whereMap);
            //            tableData.setTitle("Cheque Book Details");
            //            tableData.show();
        }
    }//GEN-LAST:event_tblAgentLeaveDetailsMousePressed

    private void rdoAgentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoAgentActionPerformed
        rdoDealer.setSelected(false);
        rdoAgent.setSelected(true);
    }//GEN-LAST:event_rdoAgentActionPerformed

    private void rdoDealerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDealerActionPerformed
        // TODO add your handling code here:
        rdoDealer.setSelected(true);
        rdoAgent.setSelected(false);
    }//GEN-LAST:event_rdoDealerActionPerformed
    
private void initComponentData() {
        try{
            cboProductType.setModel(observable.getCboCollSuspProdtype());
            cboProductID.setModel(observable.getCboCollSuspProdID());
            cboCreditProductType.setModel(observable.getCboCreditProductType());
            cboDailyProductType.setModel(observable.getCboDailyProductType());
            cboDailyProductID.setModel(observable.getCboCreditProductID());
            
            cboAgentRegion.setModel(observable.getCboAgentRegion());
            cboAgentTransactionType.setModel(observable.getCboAgentTransactionType());
            cboLRegion.setModel(observable.getCboLRegion());
            cboCRegion.setModel(observable.getCboCRegion());
            cboTxnType.setModel(observable.getCboTxnType());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new AgentUI().show();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAgentID;
    private com.see.truetransact.uicomponent.CButton btnAgentLeaveDelete;
    private com.see.truetransact.uicomponent.CButton btnAgentLeaveNew;
    private com.see.truetransact.uicomponent.CButton btnAgentLeaveSave;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCAgentId;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCollSuspACNum;
    private com.see.truetransact.uicomponent.CButton btnCommisionCreditedTo;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDepositCreditedTo;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnLAgentID;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnProdDelete;
    private com.see.truetransact.uicomponent.CButton btnProdNew;
    private com.see.truetransact.uicomponent.CButton btnProdSave;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboAgentRegion;
    private com.see.truetransact.uicomponent.CComboBox cboAgentTransactionType;
    private com.see.truetransact.uicomponent.CComboBox cboCRegion;
    private com.see.truetransact.uicomponent.CComboBox cboCreditProductID;
    private com.see.truetransact.uicomponent.CComboBox cboCreditProductType;
    private com.see.truetransact.uicomponent.CComboBox cboDailyProductID;
    private com.see.truetransact.uicomponent.CComboBox cboDailyProductType;
    private com.see.truetransact.uicomponent.CComboBox cboLRegion;
    private com.see.truetransact.uicomponent.CComboBox cboProductID;
    private com.see.truetransact.uicomponent.CComboBox cboProductType;
    private com.see.truetransact.uicomponent.CComboBox cboTxnType;
    private com.see.truetransact.uicomponent.CLabel lblAgentID;
    private com.see.truetransact.uicomponent.CLabel lblAgentID1;
    private com.see.truetransact.uicomponent.CLabel lblAgentRegion;
    private com.see.truetransact.uicomponent.CLabel lblAgentTransactionType;
    private com.see.truetransact.uicomponent.CLabel lblAgentType;
    private com.see.truetransact.uicomponent.CLabel lblApptDate;
    private com.see.truetransact.uicomponent.CLabel lblApptDate1;
    private com.see.truetransact.uicomponent.CLabel lblCAgentName;
    private com.see.truetransact.uicomponent.CLabel lblCollSuspACnum;
    private com.see.truetransact.uicomponent.CLabel lblCollSuspProdID;
    private com.see.truetransact.uicomponent.CLabel lblCollSuspProdID1;
    private com.see.truetransact.uicomponent.CLabel lblCollSuspProdtype;
    private com.see.truetransact.uicomponent.CLabel lblCollSuspProdtype1;
    private com.see.truetransact.uicomponent.CLabel lblCommisionCreditedTo;
    private com.see.truetransact.uicomponent.CLabel lblCommisionCreditedTo1;
    private com.see.truetransact.uicomponent.CLabel lblCreditProductId;
    private com.see.truetransact.uicomponent.CLabel lblCreditProductType1;
    private com.see.truetransact.uicomponent.CLabel lblCreditProductType2;
    private com.see.truetransact.uicomponent.CLabel lblCreditProductType3;
    private com.see.truetransact.uicomponent.CLabel lblCustNameVal;
    private com.see.truetransact.uicomponent.CLabel lblDailyProductId;
    private com.see.truetransact.uicomponent.CLabel lblDailyProductType;
    private com.see.truetransact.uicomponent.CLabel lblDepositCreditedTo;
    private com.see.truetransact.uicomponent.CLabel lblDepositCreditedTo1;
    private com.see.truetransact.uicomponent.CLabel lblDepositName;
    private com.see.truetransact.uicomponent.CLabel lblFromDate;
    private com.see.truetransact.uicomponent.CLabel lblLAgentName;
    private com.see.truetransact.uicomponent.CLabel lblLstComPaidDt;
    private com.see.truetransact.uicomponent.CLabel lblLstComPaiddtVal;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNameValue;
    private com.see.truetransact.uicomponent.CLabel lblProdIdlName;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
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
    private com.see.truetransact.uicomponent.CLabel lblStatus1;
    private com.see.truetransact.uicomponent.CMenuBar mbrAgent;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAgenLeaveData;
    private com.see.truetransact.uicomponent.CPanel panAgent;
    private com.see.truetransact.uicomponent.CPanel panAgent1;
    private com.see.truetransact.uicomponent.CPanel panAgentData;
    private com.see.truetransact.uicomponent.CPanel panAgentDisplay;
    private com.see.truetransact.uicomponent.CPanel panAgentID;
    private com.see.truetransact.uicomponent.CPanel panAgentID1;
    private com.see.truetransact.uicomponent.CPanel panAgentLeaveBtn;
    private com.see.truetransact.uicomponent.CPanel panAgentLeaveBtn1;
    private com.see.truetransact.uicomponent.CPanel panAgentType;
    private com.see.truetransact.uicomponent.CPanel panDep;
    private com.see.truetransact.uicomponent.CPanel panDep1;
    private com.see.truetransact.uicomponent.CPanel panOA;
    private com.see.truetransact.uicomponent.CPanel panSHGBtn;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panSuspAcc;
    private com.see.truetransact.uicomponent.CRadioButton rdoAgent;
    private com.see.truetransact.uicomponent.CRadioButton rdoDealer;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptPrint;
    private javax.swing.JSeparator sptProcess;
    private com.see.truetransact.uicomponent.CScrollPane srpAgent;
    private com.see.truetransact.uicomponent.CScrollPane srpAgentLeaveDetails;
    private com.see.truetransact.uicomponent.CScrollPane srpProdDetails;
    private com.see.truetransact.uicomponent.CTabbedPane tabAgent;
    private com.see.truetransact.uicomponent.CTable tblAgent;
    private com.see.truetransact.uicomponent.CTable tblAgentLeaveDetails;
    private com.see.truetransact.uicomponent.CTable tblProdDetails;
    private javax.swing.JToolBar tbrLoantProduct;
    private com.see.truetransact.uicomponent.CDateField tdtApptDate;
    private com.see.truetransact.uicomponent.CDateField tdtFromDate;
    private com.see.truetransact.uicomponent.CDateField tdtToDate;
    private com.see.truetransact.uicomponent.CTextField txtAgentID;
    private com.see.truetransact.uicomponent.CTextField txtCAgentId;
    private com.see.truetransact.uicomponent.CTextField txtCollSuspACNum;
    private com.see.truetransact.uicomponent.CTextField txtCommisionCreditedTo;
    private com.see.truetransact.uicomponent.CTextField txtDepositCreditedTo;
    private com.see.truetransact.uicomponent.CTextField txtLAgentID;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    // End of variables declaration//GEN-END:variables
    
}
