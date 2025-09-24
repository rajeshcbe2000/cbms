/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AppraiserCommisionUI.java
 *
 * Created on February 2, 2005, 12:20 PM
 */

package com.see.truetransact.ui.termloan.appraiserCommision;

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
public class AppraiserCommisionUI extends CInternalFrame implements java.util.Observer, UIMandatoryField{
    HashMap mandatoryMap;
    AppraiserCommisionOB observable;
//    AgentRB resourceBundle = new AgentRB();
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.appraiserCommision.AppraiserCommisionRB", ProxyParameters.LANGUAGE);
    
    final int EDIT=0,DELETE=1,AUTHORIZE=2, AGENTID=3, ACCNO =4, DEPOSITNO =5, VIEW = 10;
    final int SUSPPROD_TYPE=6,SUSPPROD_ID=7,SUSPPROD_ACCN=8;
    int viewType=-1;
    
    /** Creates new form AgentUI */
    public AppraiserCommisionUI() {
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
    }
    
    private void setObservable() {
        observable = AppraiserCommisionOB.getInstance();
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
        lblName.setName("lblName");
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
        lblCustName.setName("lblCustName");
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
        lblName.setText(resourceBundle.getString("lblName"));
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
        lblCustName.setText(resourceBundle.getString("lblCustName"));
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
        cboCreditProductType.setSelectedItem(((ComboBoxModel)cboCreditProductType.getModel()).getDataForKey(observable.getTxtDepositCreditedTo()));
        observable.cboCreditProductId();
        cboCreditProductID.setModel(observable.getCboCreditProductID());
        cboCreditProductID.setSelectedItem(((ComboBoxModel)cboCreditProductID.getModel()).getDataForKey(observable.getTxtDepositCreditedProdId()));
        txtCollSuspACNum.setText(CommonUtil.convertObjToStr(observable.getTxtCollSuspACNum()));
        lblCustNameVal.setText(observable.getLblCustNameVal());
        lblDepositName.setText(observable.getLblDepositCustName());
        txtDepositCreditedTo.setText(CommonUtil.convertObjToStr(observable.getDpacnum()));
        if(observable.getActionType()!=ClientConstants.ACTIONTYPE_NEW && observable.getLastComPaidDt().getDate()>0)
            lblLstComPaiddtVal.setText(DateUtil.getStringDate(DateUtil.addDays(observable.getLastComPaidDt(),-1)));
        
    }
    
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtAgentID(txtAgentID.getText());
        observable.setTdtApptDate(tdtApptDate.getDateValue());
        observable.setTxtRemarks(txtRemarks.getText());
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
        AppraiserCommisionMRB objMandatoryRB = new AppraiserCommisionMRB();
        txtAgentID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAgentID"));
        tdtApptDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtApptDate"));
        txtRemarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRemarks"));
//        txtCommisionCreditedTo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCommisionCreditedTo"));
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
        lblName = new com.see.truetransact.uicomponent.CLabel();
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
        lblCustName = new com.see.truetransact.uicomponent.CLabel();
        lblLstComPaiddtVal = new com.see.truetransact.uicomponent.CLabel();
        lblLstComPaidDt = new com.see.truetransact.uicomponent.CLabel();
        lblCreditProductId = new com.see.truetransact.uicomponent.CLabel();
        lblCreditProductType = new com.see.truetransact.uicomponent.CLabel();
        cboCreditProductType = new com.see.truetransact.uicomponent.CComboBox();
        cboCreditProductID = new com.see.truetransact.uicomponent.CComboBox();
        lblSecurityDeposit = new com.see.truetransact.uicomponent.CLabel();
        panAgentDisplay = new com.see.truetransact.uicomponent.CPanel();
        srpAgent = new com.see.truetransact.uicomponent.CScrollPane();
        tblAgent = new com.see.truetransact.uicomponent.CTable();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus1 = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        tbrLoantProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace34 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace35 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace37 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace36 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace38 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace39 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
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
        setMinimumSize(new java.awt.Dimension(450, 650));
        setPreferredSize(new java.awt.Dimension(450, 650));

        panAgent.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panAgent.setFocusCycleRoot(true);
        panAgent.setMinimumSize(new java.awt.Dimension(450, 500));
        panAgent.setPreferredSize(new java.awt.Dimension(450, 600));
        panAgent.setLayout(new java.awt.GridBagLayout());

        panAgentData.setBorder(javax.swing.BorderFactory.createTitledBorder("Agent Details"));
        panAgentData.setFocusCycleRoot(true);
        panAgentData.setMinimumSize(new java.awt.Dimension(350, 400));
        panAgentData.setPreferredSize(new java.awt.Dimension(350, 400));
        panAgentData.setLayout(new java.awt.GridBagLayout());

        panAgentID.setLayout(new java.awt.GridBagLayout());

        txtAgentID.setEditable(false);
        txtAgentID.setEnabled(false);
        txtAgentID.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
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
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAgentData.add(panAgentID, gridBagConstraints);

        panOA.setLayout(new java.awt.GridBagLayout());

        txtCommisionCreditedTo.setEditable(false);
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
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
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
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAgentData.add(panOA, gridBagConstraints);

        panDep.setLayout(new java.awt.GridBagLayout());

        txtDepositCreditedTo.setEditable(false);
        txtDepositCreditedTo.setEnabled(false);
        txtDepositCreditedTo.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
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
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAgentData.add(panDep, gridBagConstraints);

        panSuspAcc.setLayout(new java.awt.GridBagLayout());

        txtCollSuspACNum.setEditable(false);
        txtCollSuspACNum.setEnabled(false);
        txtCollSuspACNum.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
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
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAgentData.add(panSuspAcc, gridBagConstraints);

        lblAgentID.setText("Agent ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAgentData.add(lblAgentID, gridBagConstraints);

        lblName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAgentData.add(lblName, gridBagConstraints);

        lblNameValue.setMaximumSize(new java.awt.Dimension(150, 20));
        lblNameValue.setMinimumSize(new java.awt.Dimension(150, 20));
        lblNameValue.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAgentData.add(lblNameValue, gridBagConstraints);

        lblApptDate.setText("Appointed Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAgentData.add(lblApptDate, gridBagConstraints);

        tdtApptDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtApptDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAgentData.add(tdtApptDate, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAgentData.add(lblRemarks, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAgentData.add(txtRemarks, gridBagConstraints);

        lblCommisionCreditedTo.setText("Commision Credited To");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAgentData.add(lblCommisionCreditedTo, gridBagConstraints);

        lblDepositCreditedTo.setText("Security Deposit Credited To");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAgentData.add(lblDepositCreditedTo, gridBagConstraints);

        lblProdIdlName.setMaximumSize(new java.awt.Dimension(150, 20));
        lblProdIdlName.setMinimumSize(new java.awt.Dimension(150, 20));
        lblProdIdlName.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAgentData.add(lblProdIdlName, gridBagConstraints);

        lblDepositName.setMaximumSize(new java.awt.Dimension(150, 20));
        lblDepositName.setMinimumSize(new java.awt.Dimension(150, 20));
        lblDepositName.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAgentData.add(lblDepositName, gridBagConstraints);

        lblCollSuspProdtype.setText("lblCollSuspProdtype");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAgentData.add(lblCollSuspProdtype, gridBagConstraints);

        lblCollSuspProdID.setText("lblCollSuspProdID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAgentData.add(lblCollSuspProdID, gridBagConstraints);

        lblCollSuspACnum.setText("lblCollSuspACNum");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAgentData.add(lblCollSuspACnum, gridBagConstraints);

        cboProductType.setMaximumSize(new java.awt.Dimension(100, 21));
        cboProductType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAgentData.add(cboProductType, gridBagConstraints);

        cboProductID.setMaximumSize(new java.awt.Dimension(100, 21));
        cboProductID.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAgentData.add(cboProductID, gridBagConstraints);

        lblCustNameVal.setMaximumSize(new java.awt.Dimension(150, 20));
        lblCustNameVal.setMinimumSize(new java.awt.Dimension(150, 20));
        lblCustNameVal.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAgentData.add(lblCustNameVal, gridBagConstraints);

        lblCustName.setText("lblCollSuspACNum");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAgentData.add(lblCustName, gridBagConstraints);

        lblLstComPaiddtVal.setText("LastComPaidDtVal");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAgentData.add(lblLstComPaiddtVal, gridBagConstraints);

        lblLstComPaidDt.setText("LastComPaidDt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAgentData.add(lblLstComPaidDt, gridBagConstraints);

        lblCreditProductId.setText("Security Deposit Prod Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAgentData.add(lblCreditProductId, gridBagConstraints);

        lblCreditProductType.setText("Security Deposit Prod Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAgentData.add(lblCreditProductType, gridBagConstraints);

        cboCreditProductType.setMaximumSize(new java.awt.Dimension(100, 21));
        cboCreditProductType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCreditProductType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCreditProductTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAgentData.add(cboCreditProductType, gridBagConstraints);

        cboCreditProductID.setMaximumSize(new java.awt.Dimension(100, 21));
        cboCreditProductID.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAgentData.add(cboCreditProductID, gridBagConstraints);

        lblSecurityDeposit.setText("Security Deposit Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAgentData.add(lblSecurityDeposit, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgent.add(panAgentData, gridBagConstraints);

        panAgentDisplay.setMinimumSize(new java.awt.Dimension(400, 140));
        panAgentDisplay.setPreferredSize(new java.awt.Dimension(400, 150));
        panAgentDisplay.setLayout(new java.awt.GridBagLayout());

        srpAgent.setMinimumSize(new java.awt.Dimension(400, 125));
        srpAgent.setPreferredSize(new java.awt.Dimension(400, 125));

        tblAgent.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Deposit No", "Customer Name", "Date of Deposit", "Deposit Amount"
            }
        ));
        srpAgent.setViewportView(tblAgent);

        panAgentDisplay.add(srpAgent, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgent.add(panAgentDisplay, gridBagConstraints);

        getContentPane().add(panAgent, java.awt.BorderLayout.CENTER);

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
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.setEnabled(false);
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

        lblSpace34.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace34.setText("     ");
        lblSpace34.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace34);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnEdit);

        lblSpace35.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace35.setText("     ");
        lblSpace35.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace35.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace35.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace35);

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

        lblSpace37.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace37.setText("     ");
        lblSpace37.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace37.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace37.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace37);

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

        lblSpace36.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace36.setText("     ");
        lblSpace36.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace36.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace36.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace36);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnException);

        lblSpace38.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace38.setText("     ");
        lblSpace38.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace38.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace38.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace38);

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

        lblSpace39.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace39.setText("     ");
        lblSpace39.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace39.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace39.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace39);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnClose);

        getContentPane().add(tbrLoantProduct, java.awt.BorderLayout.NORTH);

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

    private void cboCreditProductTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCreditProductTypeActionPerformed
        // TODO add your handling code here:        
         if(cboCreditProductType.getSelectedIndex() > 0) {
            String prodType = ((ComboBoxModel)cboCreditProductType.getModel()).getKeyForSelected().toString();
            observable.setCboCreditProductType(prodType);
            if (prodType!=null && prodType.equals("GL")) {
                cboCreditProductID.setSelectedItem("");
                cboCreditProductID.setEnabled(false);
                txtDepositCreditedTo.setText("");
                lblSecurityDeposit.setText("A/C Head Name");
                observable.setCboCreditProductType(prodType);
            } else {
                observable.setCboCreditProductType(prodType);
                txtDepositCreditedTo.setText("");
                observable.cboCreditProductId();
                cboCreditProductID.setModel(observable.getCboCreditProductID());
                cboCreditProductID.setEnabled(true);
                cboCreditProductID.setSelectedItem(observable.getCboCreditProductID().getDataForKey(observable.getCboCreditProductID()));
                lblSecurityDeposit.setText("Name");
            }
        }        
    }//GEN-LAST:event_cboCreditProductTypeActionPerformed

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
        Date daybeDt=ClientUtil.getCurrentDate();
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
                 lblCustName.setText("A/C Head Name");
                 txtCollSuspACNum.setText("");
                 observable.setTxtCollSuspProdtype(prodType);
             } else {
                 txtCollSuspACNum.setText("");
                 observable.setCboCollSuspPdType(prodType);
                 observable.populateCboCollSuspPdId();
                 cboProductID.setModel(observable.getCboCollSuspProdID());
                 cboProductID.setEnabled(true);
                 cboProductID.setSelectedItem(observable.getCboCollSuspProdID().getDataForKey(observable.getGetCboCollSuspPDID()));
                 lblCustName.setText("Name");
             }
         }
    }//GEN-LAST:event_cboProductTypeActionPerformed

    private void btnAgentIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgentIDActionPerformed
        // TODO add your handling code here:
        popUp(AGENTID);
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
         observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
         authorizeStatus(CommonConstants.STATUS_REJECTED);
       
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        cboProductID.setEnabled(false);
        cboProductType.setEnabled(false);
       
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
            mapParam.put(CommonConstants.MAP_NAME, "getAgentAuthorizeList");
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
//            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            
//            whereMap.put("BRANCHID",TrueTransactMain.BRANCH_ID);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeAgentData");
            
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
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT,ClientUtil.getCurrentDate());
            
            System.out.println("singleAuthorizeMap: " + singleAuthorizeMap);
            
            ClientUtil.execute("authorizeAgentData", singleAuthorizeMap);
            
            super.setOpenForEditBy(observable.getStatusBy());
            super.removeEditLock(txtAgentID.getText());
           
            viewType = -1;
            btnSave.setEnabled(true);
           
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
        
        if(observable.getAuthorizeStatus()!=null){
            super.removeEditLock(txtAgentID.getText());
        }
        setModified(false);
        observable.resetForm();                 //__ Reset the fields in the UI to null...
        observable.resetTable();
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
        lblCustNameVal.setText("");
        cboProductID.setSelectedItem("");
        cboProductType.setSelectedItem("");
        txtCollSuspACNum.setText("");
        lblCustNameVal.setText("");
        txtCollSuspACNum.setText("");
        cboCreditProductID.setSelectedItem("");
        lblDepositName.setText("");
        lblLstComPaiddtVal.setText("");
        
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        updateOBFields();
        //To display an alert if the mandatory fields are not properly inputted, else proceed with normal operation
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panAgent);
        
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
                observable.resetForm();                    //__ Reset the fields in the UI to null...
                observable.resetTable();
                ClientUtil.enableDisable(this, false);     //__ Disables the panel...
                btnAgentID.setEnabled(false);
                setButtonEnableDisable();                  //__ Enables or Disables the buttons and menu Items depending on their previous state...
                observable.setResultStatus();              //__ To Reset the Value of lblStatus...
                txtDepositCreditedTo.setText("");
                txtCollSuspACNum.setText("");
            }
        }
        
         //__ Make the Screen Closable..
        setModified(false);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        lblCustNameVal.setText("");
        txtCollSuspACNum.setText("");
        cboCreditProductID.setSelectedItem("");
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
        popUp(EDIT);
         btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        btnCommisionCreditedTo.setEnabled(true);
        btnDepositCreditedTo.setEnabled(true);
        btnCollSuspACNum.setEnabled(true);
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
        }
    }
    
    // this method is called automatically from ViewAll...
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        HashMap OAprodDescMap = new HashMap();
        HashMap depProdDescMap = new HashMap();
        
        System.out.println("hash:  " + hash);
        
        if (viewType==EDIT || viewType==DELETE || viewType==AUTHORIZE || viewType==VIEW) {
            hash.put("AGENTID", hash.get("AGENT ID"));
            hash.put("BRANCHID", getSelectedBranchID());
            
            observable.populateData(hash);     //__ Called to display the Data in the UI fields...
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
            }
            
            btnAgentID.setEnabled(false);
            observable.setStatus();             //__ To set the Value of lblStatus...
            setButtonEnableDisable();           //__ Enables or Disables the buttons and menu Items depending on their previous state...
            
        }else if (viewType==AGENTID) {
            //__ To Set the Data Regarding the Customer in the Screen...
            txtAgentID.setText(CommonUtil.convertObjToStr(hash.get("AGENT ID")));
            lblNameValue.setText(CommonUtil.convertObjToStr(hash.get("AGENT NAME")));
            
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
            if(((ComboBoxModel)cboCreditProductType.getModel()).getKeyForSelected().equals("GL")){
                txtDepositCreditedTo.setText(CommonUtil.convertObjToStr(hash.get("A/C HEAD")));
                lblDepositName.setText(CommonUtil.convertObjToStr(hash.get("A/C HEAD DESCRIPTION")));
            }else{
                if(((ComboBoxModel)cboCreditProductType.getModel()).getKeyForSelected().equals("TD")){
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
        cboCreditProductID.setSelectedItem("");
        lblDepositName.setText("");
        lblCustNameVal.setText("");
        
//        btnCollSuspProdId.setEnabled(true);
//        btnCollSuspProdtype.setEnabled(true);
        //__ To Save the data in the Internal Frame...
        
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
    private void initComponentData() {
        try{
            cboProductType.setModel(observable.getCboCollSuspProdtype());
            cboProductID.setModel(observable.getCboCollSuspProdID());
            cboCreditProductType.setModel(observable.getCboCreditProductType());
            cboCreditProductID.setModel(observable.getCboCreditProductID());
//            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new AppraiserCommisionUI().show();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAgentID;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCollSuspACNum;
    private com.see.truetransact.uicomponent.CButton btnCommisionCreditedTo;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDepositCreditedTo;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboCreditProductID;
    private com.see.truetransact.uicomponent.CComboBox cboCreditProductType;
    private com.see.truetransact.uicomponent.CComboBox cboProductID;
    private com.see.truetransact.uicomponent.CComboBox cboProductType;
    private com.see.truetransact.uicomponent.CLabel lblAgentID;
    private com.see.truetransact.uicomponent.CLabel lblApptDate;
    private com.see.truetransact.uicomponent.CLabel lblCollSuspACnum;
    private com.see.truetransact.uicomponent.CLabel lblCollSuspProdID;
    private com.see.truetransact.uicomponent.CLabel lblCollSuspProdtype;
    private com.see.truetransact.uicomponent.CLabel lblCommisionCreditedTo;
    private com.see.truetransact.uicomponent.CLabel lblCreditProductId;
    private com.see.truetransact.uicomponent.CLabel lblCreditProductType;
    private com.see.truetransact.uicomponent.CLabel lblCustName;
    private com.see.truetransact.uicomponent.CLabel lblCustNameVal;
    private com.see.truetransact.uicomponent.CLabel lblDepositCreditedTo;
    private com.see.truetransact.uicomponent.CLabel lblDepositName;
    private com.see.truetransact.uicomponent.CLabel lblLstComPaidDt;
    private com.see.truetransact.uicomponent.CLabel lblLstComPaiddtVal;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblName;
    private com.see.truetransact.uicomponent.CLabel lblNameValue;
    private com.see.truetransact.uicomponent.CLabel lblProdIdlName;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblSecurityDeposit;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace34;
    private com.see.truetransact.uicomponent.CLabel lblSpace35;
    private com.see.truetransact.uicomponent.CLabel lblSpace36;
    private com.see.truetransact.uicomponent.CLabel lblSpace37;
    private com.see.truetransact.uicomponent.CLabel lblSpace38;
    private com.see.truetransact.uicomponent.CLabel lblSpace39;
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
    private com.see.truetransact.uicomponent.CPanel panAgent;
    private com.see.truetransact.uicomponent.CPanel panAgentData;
    private com.see.truetransact.uicomponent.CPanel panAgentDisplay;
    private com.see.truetransact.uicomponent.CPanel panAgentID;
    private com.see.truetransact.uicomponent.CPanel panDep;
    private com.see.truetransact.uicomponent.CPanel panOA;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panSuspAcc;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptPrint;
    private javax.swing.JSeparator sptProcess;
    private com.see.truetransact.uicomponent.CScrollPane srpAgent;
    private com.see.truetransact.uicomponent.CTable tblAgent;
    private javax.swing.JToolBar tbrLoantProduct;
    private com.see.truetransact.uicomponent.CDateField tdtApptDate;
    private com.see.truetransact.uicomponent.CTextField txtAgentID;
    private com.see.truetransact.uicomponent.CTextField txtCollSuspACNum;
    private com.see.truetransact.uicomponent.CTextField txtCommisionCreditedTo;
    private com.see.truetransact.uicomponent.CTextField txtDepositCreditedTo;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    // End of variables declaration//GEN-END:variables
    
}
