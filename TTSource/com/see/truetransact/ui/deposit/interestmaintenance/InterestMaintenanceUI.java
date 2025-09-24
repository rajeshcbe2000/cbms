/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * InterestMaintenanceUI.java
 *
 * Created on May 21, 2004, 12:37 PM
 */

package com.see.truetransact.ui.deposit.interestmaintenance;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uicomponent.CButtonGroup;
import org.apache.log4j.Logger;

import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.Observable;
import java.util.Date;

/**
 *
 * @author  rahul
 */
public class InterestMaintenanceUI extends CInternalFrame implements java.util.Observer, UIMandatoryField {
    private HashMap mandatoryMap;
    InterestMaintenanceOB observable;
    
    final int EDIT=0, DELETE=1, ACCNUMBER=2,AUTHORIZE=3, VIEW=4;
    int viewType=-1, updateTab = -1;
    boolean isFilled = false;
    int rowSelected = -1;
    int tabFlag = 0;
    final String OPERAIVE = "OA";
    final String ADVANCES = "AD";
    final String TERMLOAN = "TL";
    final String AGRITERMLOAN = "ATL";
    final String AGRIADVANCES = "AAD";
    final String DEPOSITS = "TD";
    boolean flag =false;
    //    final InterestMaintenanceRB resourceBundle = new InterestMaintenanceRB();
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.deposit.interestmaintenance.InterestMaintenanceRB", ProxyParameters.LANGUAGE);
    
    //Logger
    private final static Logger log = Logger.getLogger(InterestMaintenanceUI.class);
    private boolean tablePressed = false;
    private Date currDt = null;
    /** Creates new form InterestMaintenanceUI */
    public InterestMaintenanceUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initSetup();
    }
    
    private void initSetup(){
        setFieldNames();
        internationalize();
        setMaxLenths();
        setMandatoryHashMap();
        setObservable();
        initComponentData();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panGroupnfo);
        ClientUtil.enableDisable(this, false);
        //ClientUtil.enableDisable(panCalculations,false);
        tabSaveDeleteButtons();
        btnTabNew.setEnabled(false);
        
        setButtonEnableDisable();   // To enable and disable the main buttons and the menu items...
        resetUI();
        setHelpMessage();
        
        //__ To reset the value of the visited tabs...
        tabInterestMaintenance.resetVisits();
    }
    
    /*
     * Creates the instance of OB
     */
    private void setObservable() {
        observable = InterestMaintenanceOB.getInstance();
        observable.addObserver(this);
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
        btnTabDelete.setName("btnTabDelete");
        btnTabNew.setName("btnTabNew");
        btnTabSave.setName("btnTabSave");
        cboFromAmount.setName("cboFromAmount");
        cboFromPeriod.setName("cboFromPeriod");
        cboProdType.setName("cboProdType");
        cboToAmount.setName("cboToAmount");
        cboToPeriod.setName("cboToPeriod");
        lblAgainstInterest.setName("lblAgainstInterest");
        lblAgainstInterest_Per.setName("lblAgainstInterest_Per");
        lblDate.setName("lblDate");
        lblODI.setName("lblODI");
        lblStatementPenal.setName("lblFloatingRate_Per");
        lblFromAmount.setName("lblFromAmount");
        lblFromPeriod.setName("lblFromPeriod");
        lblGroupId.setName("lblGroupId");
        lblGroupIdDesc.setName("lblGroupIdDesc");
        lblGroupName.setName("lblGroupName");
        lblInterExpiry.setName("lblInterExpiry");
        lblInterExpiry_Per.setName("lblInterExpiry_Per");
        lblLimitAmt.setName("lblLimitAmt");
        lblMsg.setName("lblMsg");
        lblODI.setName("lblODI");
        lblODI_Per.setName("lblODI_Per");
        lblPenalInterest.setName("lblPenalInterest");
        lblPenalInterestPer.setName("lblPenalInterestPer");
        lblProdType.setName("lblProdType");
        lblRateInterest.setName("lblRateInterest");
        lblRateInterestPer.setName("lblRateInterestPer");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpaces.setName("lblSpaces");
        lblStatus.setName("lblStatus");
        lblToAmount.setName("lblToAmount");
        lblToDate.setName("lblToDate");
        lblToPeriod.setName("lblToPeriod");
        mbrMain.setName("mbrMain");
        panButtons.setName("panButtons");
        panCalculations.setName("panCalculations");
        panFromPeriod.setName("panFromPeriod");
        panGroupData.setName("panGroupData");
        panGroupnfo.setName("panGroupnfo");
        panInterestCalc.setName("panInterestCalc");
        panInterestCalculation.setName("panInterestCalculation");
        panInterestGroup.setName("panInterestGroup");
        panInterestMaintenance.setName("panInterestMaintenance");
        panInterestTable.setName("panInterestTable");
        panStatus.setName("panStatus");
        panToPeriod.setName("panToPeriod");
        srpCategory.setName("srpCategory");
        srpInterestTable.setName("srpInterestTable");
        srpProduct.setName("srpProduct");
        tabInterestMaintenance.setName("tabInterestMaintenance");
        tblCategory.setName("tblCategory");
        tblInterestTable.setName("tblInterestTable");
        tblProduct.setName("tblProduct");
        tdtDate.setName("tdtDate");
        tdtToDate.setName("tdtToDate");
        txtAgainstInterest.setName("txtAgainstInterest");
        txtStatementPenal.setName("txtStatementPenal");
        txtFromPeriod.setName("txtFromPeriod");
        txtGroupName.setName("txtGroupName");
        txtInterExpiry.setName("txtInterExpiry");
        txtLimitAmt.setName("txtLimitAmt");
        txtODI.setName("txtODI");
        txtPenalInterest.setName("txtPenalInterest");
        txtRateInterest.setName("txtRateInterest");
        txtToPeriod.setName("txtToPeriod");
    }
    
    
    
    
    
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        final InterestMaintenanceRB resourceBundle = new InterestMaintenanceRB();
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnTabSave.setText(resourceBundle.getString("btnTabSave"));
        lblGroupId.setText(resourceBundle.getString("lblGroupId"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblFromAmount.setText(resourceBundle.getString("lblFromAmount"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblProdType.setText(resourceBundle.getString("lblProdType"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblPenalInterest.setText(resourceBundle.getString("lblPenalInterest"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblAgainstInterest.setText(resourceBundle.getString("lblAgainstInterest"));
        lblRateInterest.setText(resourceBundle.getString("lblRateInterest"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblSpaces.setText(resourceBundle.getString("lblSpaces"));
        btnTabNew.setText(resourceBundle.getString("btnTabNew"));
        lblToAmount.setText(resourceBundle.getString("lblToAmount"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblRateInterestPer.setText(resourceBundle.getString("lblRateInterestPer"));
        lblToPeriod.setText(resourceBundle.getString("lblToPeriod"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblLimitAmt.setText(resourceBundle.getString("lblLimitAmt"));
        lblStatementPenal.setText(resourceBundle.getString("lblStatementPenal"));
        lblInterExpiry_Per.setText(resourceBundle.getString("lblInterExpiry_Per"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblStatement_Per.setText(resourceBundle.getString("lblStatement_Per"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblODI_Per.setText(resourceBundle.getString("lblODI_Per"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnTabDelete.setText(resourceBundle.getString("btnTabDelete"));
        ((javax.swing.border.TitledBorder)panGroupData.getBorder()).setTitle(resourceBundle.getString("panGroupData"));
        lblGroupIdDesc.setText(resourceBundle.getString("lblGroupIdDesc"));
        lblInterExpiry.setText(resourceBundle.getString("lblInterExpiry"));
        lblODI.setText(resourceBundle.getString("lblODI"));
        lblDate.setText(resourceBundle.getString("lblDate"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblToDate.setText(resourceBundle.getString("lblToDate"));
        lblFromPeriod.setText(resourceBundle.getString("lblFromPeriod"));
        lblPenalInterestPer.setText(resourceBundle.getString("lblPenalInterestPer"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblGroupName.setText(resourceBundle.getString("lblGroupName"));
        lblAgainstInterest_Per.setText(resourceBundle.getString("lblAgainstInterest_Per"));
    }
    
/* Auto Generated Method - setMandatoryHashMap()
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtGroupName", new Boolean(true));
        mandatoryMap.put("cboProdType", new Boolean(true));
        mandatoryMap.put("tdtDate", new Boolean(true));
        mandatoryMap.put("txtFromPeriod", new Boolean(true));
        mandatoryMap.put("cboFromPeriod", new Boolean(true));
        mandatoryMap.put("txtToPeriod", new Boolean(true));
        mandatoryMap.put("cboToPeriod", new Boolean(true));
        mandatoryMap.put("txtRateInterest", new Boolean(true));
        mandatoryMap.put("txtPenalInterest", new Boolean(true));
        mandatoryMap.put("cboFromAmount", new Boolean(true));
        mandatoryMap.put("cboToAmount", new Boolean(true));
        mandatoryMap.put("tdtToDate", new Boolean(true));
        mandatoryMap.put("txtAgainstInterest", new Boolean(true));
        mandatoryMap.put("txtLimitAmt", new Boolean(true));
        mandatoryMap.put("txtInterExpiry", new Boolean(true));
        mandatoryMap.put("txtPLR", new Boolean(true));
        mandatoryMap.put("txtFloatingRate", new Boolean(true));
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
        /* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        cboProdType.setSelectedItem(observable.getCboProdType());
        txtGroupName.setText(observable.getTxtGroupName());
        tdtDate.setDateValue(observable.getTdtDate());
        txtFromPeriod.setText(observable.getTxtFromPeriod());
        cboFromPeriod.setSelectedItem(observable.getCboFromPeriod());
        txtToPeriod.setText(observable.getTxtToPeriod());
        cboToPeriod.setSelectedItem(observable.getCboToPeriod());
        txtRateInterest.setText(observable.getTxtRateInterest());
        txtPenalInterest.setText(observable.getTxtPenalInterest());
        cboFromAmount.setSelectedItem(observable.getCboFromAmount());
        cboToAmount.setSelectedItem(observable.getCboToAmount());
        tdtToDate.setDateValue(observable.getTdtToDate());
        txtAgainstInterest.setText(observable.getTxtAgainstInterest());
        txtLimitAmt.setText(observable.getTxtLimitAmt());
        txtInterExpiry.setText(observable.getTxtInterExpiry());
        txtODI.setText(observable.getTxtODI());
        txtStatementPenal.setText(observable.getTxtStatementPenal());
        
        tblProduct.setModel(observable.getTblProduct());
        tblCategory.setModel(observable.getTblCategory());
        tblInterestTable.setModel(observable.getTblInterest());
        
        lblGroupIdDesc.setText(observable.getLblRoiGroupId());
        //To set the Status...
        lblStatus.setText(observable.getLblStatus());
        removeRadioButtons();
//        rdoInterestType_Debit.setSelected(observable.isRdoInterestType_Debit());
//        rdoInterestType_Credit.setSelected(observable.isRdoInterestType_Credit());
        addRadioButtons();
    }
    
    private void removeRadioButtons() {
        rdgInterestType.remove(rdoInterestType_Debit);
        rdgInterestType.remove(rdoInterestType_Credit);
        rdRateTypegroup.remove(rdorateType_Normal);
        rdRateTypegroup.remove(rdorateType_Spl);
        
    }
    
    private void addRadioButtons() {// these r all radio button purpose adding...
        rdgInterestType = new CButtonGroup();
        rdgInterestType.add(rdoInterestType_Debit);
        rdgInterestType.add(rdoInterestType_Credit);
        rdRateTypegroup = new CButtonGroup(); 
        rdRateTypegroup.add(rdorateType_Normal);
        rdRateTypegroup.add(rdorateType_Spl);
       
    }
        /* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtGroupName(txtGroupName.getText());
        observable.setCboProdType((String) cboProdType.getSelectedItem());
        observable.setTdtDate(tdtDate.getDateValue());
        observable.setTxtFromPeriod(txtFromPeriod.getText());
        observable.setCboFromPeriod((String) cboFromPeriod.getSelectedItem());
        observable.setTxtToPeriod(txtToPeriod.getText());
        observable.setCboToPeriod((String) cboToPeriod.getSelectedItem());
        observable.setTxtRateInterest(txtRateInterest.getText());
        observable.setTxtPenalInterest(txtPenalInterest.getText());
        observable.setCboFromAmount((String) cboFromAmount.getSelectedItem());
        observable.setCboToAmount((String) cboToAmount.getSelectedItem());
        observable.setTdtToDate(tdtToDate.getDateValue());
        observable.setTxtAgainstInterest(txtAgainstInterest.getText());
        observable.setTxtLimitAmt(txtLimitAmt.getText());
        observable.setTxtInterExpiry(txtInterExpiry.getText());
        observable.setTxtODI(txtODI.getText());
        observable.setTxtStatementPenal(txtStatementPenal.getText());
        
        observable.setTblProduct((com.see.truetransact.clientutil.EnhancedTableModel)tblProduct.getModel());
        observable.setTblCategory((com.see.truetransact.clientutil.EnhancedTableModel)tblCategory.getModel());
        observable.setRdoInterestType_Debit(rdoInterestType_Debit.isSelected());
        observable.setRdoInterestType_Credit(rdoInterestType_Credit.isSelected());
        //added by rishad 30-07-2014
        observable.setRdoRateType_Normal(rdorateType_Normal.isSelected());
        observable.setRdoRateType_Spl(rdorateType_Spl.isSelected());
    }
    
        /* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        InterestMaintenanceMRB objMandatoryRB = new InterestMaintenanceMRB();
        txtGroupName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGroupName"));
        tdtDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDate"));
        cboFromAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("cboFromAmount"));
        cboToAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("cboToAmount"));
        txtFromPeriod.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFromPeriod"));
        cboFromPeriod.setHelpMessage(lblMsg, objMandatoryRB.getString("cboFromPeriod"));
        txtToPeriod.setHelpMessage(lblMsg, objMandatoryRB.getString("txtToPeriod"));
        cboToPeriod.setHelpMessage(lblMsg, objMandatoryRB.getString("cboToPeriod"));
        txtRateInterest.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRateInterest"));
        txtPenalInterest.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPenalInterest"));
        cboProdType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdType"));
    }
    
    // To fill the Data into the Combo Boxes...
    // it invokes the Combo Box model defined in OB class...
    private void initComponentData() {
        cboFromAmount.setModel(observable.getCbmFromAmount());
        cboToAmount.setModel(observable.getCbmToAmount());
        cboFromPeriod.setModel(observable.getCbmFromPeriod());
        cboToPeriod.setModel(observable.getCbmToPeriod());
        cboProdType.setModel(observable.getCbmProdType());
    }
    
    private void setMaxLenths() {
        txtGroupName.setMaxLength(64);
        txtFromPeriod.setValidation(new NumericValidation());
        
        txtToPeriod.setMaxLength(6);
        txtToPeriod.setValidation(new NumericValidation());
        
        txtRateInterest.setMaxLength(5);
        txtRateInterest.setValidation(new NumericValidation(2,2));
        
        txtPenalInterest.setMaxLength(5);
        txtPenalInterest.setValidation(new NumericValidation(2,2));
        
        txtAgainstInterest.setMaxLength(5);
        txtAgainstInterest.setValidation(new NumericValidation(2,2));
        
        txtLimitAmt.setMaxLength(5);
        txtLimitAmt.setValidation(new CurrencyValidation(2,2));
        
        txtInterExpiry.setMaxLength(5);
        txtInterExpiry.setValidation(new NumericValidation(2,2));
        
        txtStatementPenal.setMaxLength(5);
        txtStatementPenal.setValidation(new NumericValidation(2,2));
        
        txtODI.setMaxLength(5);
        txtODI.setValidation(new NumericValidation(2,2));
    }
    
    // To set The Status of the Buttons Depending on the Condition...
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

        rdgInterestType = new com.see.truetransact.uicomponent.CButtonGroup();
        rdRateTypegroup = new com.see.truetransact.uicomponent.CButtonGroup();
        panInterestMaintenance = new com.see.truetransact.uicomponent.CPanel();
        lblSpaces = new com.see.truetransact.uicomponent.CLabel();
        tabInterestMaintenance = new com.see.truetransact.uicomponent.CTabbedPane();
        panInterestGroup = new com.see.truetransact.uicomponent.CPanel();
        panGroupnfo = new com.see.truetransact.uicomponent.CPanel();
        lblGroupId = new com.see.truetransact.uicomponent.CLabel();
        lblGroupIdDesc = new com.see.truetransact.uicomponent.CLabel();
        lblGroupName = new com.see.truetransact.uicomponent.CLabel();
        txtGroupName = new com.see.truetransact.uicomponent.CTextField();
        lblProdType = new com.see.truetransact.uicomponent.CLabel();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        panGroupData = new com.see.truetransact.uicomponent.CPanel();
        srpProduct = new com.see.truetransact.uicomponent.CScrollPane();
        tblProduct = new com.see.truetransact.uicomponent.CTable();
        srpCategory = new com.see.truetransact.uicomponent.CScrollPane();
        tblCategory = new com.see.truetransact.uicomponent.CTable();
        panModeOfOpening = new com.see.truetransact.uicomponent.CPanel();
        rdoInterestType_Debit = new com.see.truetransact.uicomponent.CRadioButton();
        rdoInterestType_Credit = new com.see.truetransact.uicomponent.CRadioButton();
        lblInterestType = new com.see.truetransact.uicomponent.CLabel();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        rdorateType_Normal = new com.see.truetransact.uicomponent.CRadioButton();
        rdorateType_Spl = new com.see.truetransact.uicomponent.CRadioButton();
        panInterestCalc = new com.see.truetransact.uicomponent.CPanel();
        panInterestCalculation = new com.see.truetransact.uicomponent.CPanel();
        panCalculations = new com.see.truetransact.uicomponent.CPanel();
        lblDate = new com.see.truetransact.uicomponent.CLabel();
        tdtDate = new com.see.truetransact.uicomponent.CDateField();
        lblFromAmount = new com.see.truetransact.uicomponent.CLabel();
        lblToAmount = new com.see.truetransact.uicomponent.CLabel();
        lblFromPeriod = new com.see.truetransact.uicomponent.CLabel();
        panFromPeriod = new com.see.truetransact.uicomponent.CPanel();
        txtFromPeriod = new com.see.truetransact.uicomponent.CTextField();
        cboFromPeriod = new com.see.truetransact.uicomponent.CComboBox();
        lblToPeriod = new com.see.truetransact.uicomponent.CLabel();
        panToPeriod = new com.see.truetransact.uicomponent.CPanel();
        txtToPeriod = new com.see.truetransact.uicomponent.CTextField();
        cboToPeriod = new com.see.truetransact.uicomponent.CComboBox();
        lblRateInterest = new com.see.truetransact.uicomponent.CLabel();
        txtRateInterest = new com.see.truetransact.uicomponent.CTextField();
        lblRateInterestPer = new com.see.truetransact.uicomponent.CLabel();
        lblPenalInterest = new com.see.truetransact.uicomponent.CLabel();
        txtPenalInterest = new com.see.truetransact.uicomponent.CTextField();
        lblPenalInterestPer = new com.see.truetransact.uicomponent.CLabel();
        panButtons = new com.see.truetransact.uicomponent.CPanel();
        btnTabNew = new com.see.truetransact.uicomponent.CButton();
        btnTabSave = new com.see.truetransact.uicomponent.CButton();
        btnTabDelete = new com.see.truetransact.uicomponent.CButton();
        cboFromAmount = new com.see.truetransact.uicomponent.CComboBox();
        cboToAmount = new com.see.truetransact.uicomponent.CComboBox();
        lblToDate = new com.see.truetransact.uicomponent.CLabel();
        tdtToDate = new com.see.truetransact.uicomponent.CDateField();
        lblAgainstInterest = new com.see.truetransact.uicomponent.CLabel();
        txtAgainstInterest = new com.see.truetransact.uicomponent.CTextField();
        lblLimitAmt = new com.see.truetransact.uicomponent.CLabel();
        txtLimitAmt = new com.see.truetransact.uicomponent.CTextField();
        lblInterExpiry = new com.see.truetransact.uicomponent.CLabel();
        txtInterExpiry = new com.see.truetransact.uicomponent.CTextField();
        lblODI = new com.see.truetransact.uicomponent.CLabel();
        txtODI = new com.see.truetransact.uicomponent.CTextField();
        lblODI_Per = new com.see.truetransact.uicomponent.CLabel();
        lblStatementPenal = new com.see.truetransact.uicomponent.CLabel();
        txtStatementPenal = new com.see.truetransact.uicomponent.CTextField();
        lblStatement_Per = new com.see.truetransact.uicomponent.CLabel();
        lblAgainstInterest_Per = new com.see.truetransact.uicomponent.CLabel();
        lblInterExpiry_Per = new com.see.truetransact.uicomponent.CLabel();
        lblLimitAmt_Per = new com.see.truetransact.uicomponent.CLabel();
        panInterestTable = new com.see.truetransact.uicomponent.CPanel();
        srpInterestTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblInterestTable = new com.see.truetransact.uicomponent.CTable();
        tbrHead = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
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
        mbrMain = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptDelete = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(800, 595));
        setPreferredSize(new java.awt.Dimension(800, 595));

        panInterestMaintenance.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panInterestMaintenance.setMinimumSize(new java.awt.Dimension(872, 561));
        panInterestMaintenance.setPreferredSize(new java.awt.Dimension(872, 561));
        panInterestMaintenance.setLayout(new java.awt.GridBagLayout());

        lblSpaces.setMinimumSize(new java.awt.Dimension(3, 15));
        lblSpaces.setPreferredSize(new java.awt.Dimension(3, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panInterestMaintenance.add(lblSpaces, gridBagConstraints);

        tabInterestMaintenance.setMinimumSize(new java.awt.Dimension(860, 534));
        tabInterestMaintenance.setPreferredSize(new java.awt.Dimension(860, 534));

        panInterestGroup.setLayout(new java.awt.GridBagLayout());

        panGroupnfo.setLayout(new java.awt.GridBagLayout());

        lblGroupId.setText("Interest Rate Group ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGroupnfo.add(lblGroupId, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGroupnfo.add(lblGroupIdDesc, gridBagConstraints);

        lblGroupName.setText("Interest Rate Group Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGroupnfo.add(lblGroupName, gridBagConstraints);

        txtGroupName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGroupnfo.add(txtGroupName, gridBagConstraints);

        lblProdType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGroupnfo.add(lblProdType, gridBagConstraints);

        cboProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboProdTypeItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGroupnfo.add(cboProdType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterestGroup.add(panGroupnfo, gridBagConstraints);

        panGroupData.setBorder(javax.swing.BorderFactory.createTitledBorder("Group Data"));
        panGroupData.setLayout(new java.awt.GridBagLayout());

        srpProduct.setMinimumSize(new java.awt.Dimension(300, 150));
        srpProduct.setPreferredSize(new java.awt.Dimension(300, 150));

        tblProduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Check", "Product ID", "Product Description"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        srpProduct.setViewportView(tblProduct);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGroupData.add(srpProduct, gridBagConstraints);

        srpCategory.setMinimumSize(new java.awt.Dimension(300, 150));
        srpCategory.setPreferredSize(new java.awt.Dimension(300, 150));

        tblCategory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Check", "Category", "Category Description"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        srpCategory.setViewportView(tblCategory);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGroupData.add(srpCategory, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterestGroup.add(panGroupData, gridBagConstraints);

        panModeOfOpening.setMinimumSize(new java.awt.Dimension(330, 40));
        panModeOfOpening.setPreferredSize(new java.awt.Dimension(330, 40));
        panModeOfOpening.setLayout(new java.awt.GridBagLayout());

        rdgInterestType.add(rdoInterestType_Debit);
        rdoInterestType_Debit.setText("Debit");
        rdoInterestType_Debit.setMaximumSize(new java.awt.Dimension(68, 15));
        rdoInterestType_Debit.setMinimumSize(new java.awt.Dimension(66, 15));
        rdoInterestType_Debit.setPreferredSize(new java.awt.Dimension(66, 18));
        rdoInterestType_Debit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoInterestType_DebitActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 10, 1, 0);
        panModeOfOpening.add(rdoInterestType_Debit, gridBagConstraints);

        rdgInterestType.add(rdoInterestType_Credit);
        rdoInterestType_Credit.setText("Credit");
        rdoInterestType_Credit.setMaximumSize(new java.awt.Dimension(85, 21));
        rdoInterestType_Credit.setMinimumSize(new java.awt.Dimension(85, 15));
        rdoInterestType_Credit.setPreferredSize(new java.awt.Dimension(85, 18));
        rdoInterestType_Credit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoInterestType_CreditActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panModeOfOpening.add(rdoInterestType_Credit, gridBagConstraints);

        lblInterestType.setText("Interest Type");
        lblInterestType.setMinimumSize(new java.awt.Dimension(98, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panModeOfOpening.add(lblInterestType, gridBagConstraints);

        cLabel1.setText("Rate Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panModeOfOpening.add(cLabel1, gridBagConstraints);

        rdRateTypegroup.add(rdorateType_Normal);
        rdorateType_Normal.setText("N.Rate");
        rdorateType_Normal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdorateType_NormalActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panModeOfOpening.add(rdorateType_Normal, gridBagConstraints);

        rdRateTypegroup.add(rdorateType_Spl);
        rdorateType_Spl.setText("Sp.Rate");
        rdorateType_Spl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdorateType_SplActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        panModeOfOpening.add(rdorateType_Spl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInterestGroup.add(panModeOfOpening, gridBagConstraints);

        tabInterestMaintenance.addTab("Interest Rate Group", panInterestGroup);

        panInterestCalc.setMinimumSize(new java.awt.Dimension(855, 508));
        panInterestCalc.setPreferredSize(new java.awt.Dimension(855, 508));
        panInterestCalc.setLayout(new java.awt.GridBagLayout());

        panInterestCalculation.setMinimumSize(new java.awt.Dimension(755, 508));
        panInterestCalculation.setPreferredSize(new java.awt.Dimension(755, 508));
        panInterestCalculation.setLayout(new java.awt.GridBagLayout());

        panCalculations.setLayout(new java.awt.GridBagLayout());

        lblDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCalculations.add(lblDate, gridBagConstraints);

        tdtDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtDate.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCalculations.add(tdtDate, gridBagConstraints);

        lblFromAmount.setText("From Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCalculations.add(lblFromAmount, gridBagConstraints);

        lblToAmount.setText("To Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCalculations.add(lblToAmount, gridBagConstraints);

        lblFromPeriod.setText("From Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCalculations.add(lblFromPeriod, gridBagConstraints);

        panFromPeriod.setLayout(new java.awt.GridBagLayout());

        txtFromPeriod.setMinimumSize(new java.awt.Dimension(50, 21));
        txtFromPeriod.setPreferredSize(new java.awt.Dimension(50, 21));
        panFromPeriod.add(txtFromPeriod, new java.awt.GridBagConstraints());

        cboFromPeriod.setMinimumSize(new java.awt.Dimension(100, 21));
        panFromPeriod.add(cboFromPeriod, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCalculations.add(panFromPeriod, gridBagConstraints);

        lblToPeriod.setText("To Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCalculations.add(lblToPeriod, gridBagConstraints);

        panToPeriod.setLayout(new java.awt.GridBagLayout());

        txtToPeriod.setMinimumSize(new java.awt.Dimension(50, 21));
        txtToPeriod.setPreferredSize(new java.awt.Dimension(50, 21));
        panToPeriod.add(txtToPeriod, new java.awt.GridBagConstraints());

        cboToPeriod.setMinimumSize(new java.awt.Dimension(100, 21));
        panToPeriod.add(cboToPeriod, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCalculations.add(panToPeriod, gridBagConstraints);

        lblRateInterest.setText("Rate of Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCalculations.add(lblRateInterest, gridBagConstraints);

        txtRateInterest.setMinimumSize(new java.awt.Dimension(50, 21));
        txtRateInterest.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCalculations.add(txtRateInterest, gridBagConstraints);

        lblRateInterestPer.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCalculations.add(lblRateInterestPer, gridBagConstraints);

        lblPenalInterest.setText("Penal Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCalculations.add(lblPenalInterest, gridBagConstraints);

        txtPenalInterest.setMinimumSize(new java.awt.Dimension(50, 21));
        txtPenalInterest.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCalculations.add(txtPenalInterest, gridBagConstraints);

        lblPenalInterestPer.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCalculations.add(lblPenalInterestPer, gridBagConstraints);

        panButtons.setLayout(new java.awt.GridBagLayout());

        btnTabNew.setText("New");
        btnTabNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTabNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButtons.add(btnTabNew, gridBagConstraints);

        btnTabSave.setText("Save");
        btnTabSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTabSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButtons.add(btnTabSave, gridBagConstraints);

        btnTabDelete.setText("Delete");
        btnTabDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTabDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButtons.add(btnTabDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCalculations.add(panButtons, gridBagConstraints);

        cboFromAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCalculations.add(cboFromAmount, gridBagConstraints);

        cboToAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCalculations.add(cboToAmount, gridBagConstraints);

        lblToDate.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCalculations.add(lblToDate, gridBagConstraints);

        tdtToDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtToDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCalculations.add(tdtToDate, gridBagConstraints);

        lblAgainstInterest.setText("Agains Clearing Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCalculations.add(lblAgainstInterest, gridBagConstraints);

        txtAgainstInterest.setMinimumSize(new java.awt.Dimension(50, 21));
        txtAgainstInterest.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCalculations.add(txtAgainstInterest, gridBagConstraints);

        lblLimitAmt.setText("Additional Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCalculations.add(lblLimitAmt, gridBagConstraints);

        txtLimitAmt.setMinimumSize(new java.awt.Dimension(50, 21));
        txtLimitAmt.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCalculations.add(txtLimitAmt, gridBagConstraints);

        lblInterExpiry.setText("Interest for Expiry of Limit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCalculations.add(lblInterExpiry, gridBagConstraints);

        txtInterExpiry.setMinimumSize(new java.awt.Dimension(50, 21));
        txtInterExpiry.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCalculations.add(txtInterExpiry, gridBagConstraints);

        lblODI.setText("OD Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCalculations.add(lblODI, gridBagConstraints);

        txtODI.setMinimumSize(new java.awt.Dimension(50, 21));
        txtODI.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCalculations.add(txtODI, gridBagConstraints);

        lblODI_Per.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCalculations.add(lblODI_Per, gridBagConstraints);

        lblStatementPenal.setText("Statement not given Penal");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCalculations.add(lblStatementPenal, gridBagConstraints);

        txtStatementPenal.setMinimumSize(new java.awt.Dimension(50, 21));
        txtStatementPenal.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCalculations.add(txtStatementPenal, gridBagConstraints);

        lblStatement_Per.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCalculations.add(lblStatement_Per, gridBagConstraints);

        lblAgainstInterest_Per.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCalculations.add(lblAgainstInterest_Per, gridBagConstraints);

        lblInterExpiry_Per.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCalculations.add(lblInterExpiry_Per, gridBagConstraints);

        lblLimitAmt_Per.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCalculations.add(lblLimitAmt_Per, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterestCalculation.add(panCalculations, gridBagConstraints);

        panInterestTable.setMinimumSize(new java.awt.Dimension(550, 500));
        panInterestTable.setPreferredSize(new java.awt.Dimension(550, 500));
        panInterestTable.setLayout(new java.awt.GridBagLayout());

        srpInterestTable.setMinimumSize(new java.awt.Dimension(450, 400));
        srpInterestTable.setPreferredSize(new java.awt.Dimension(550, 400));

        tblInterestTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "From Date", "To Date", "From Amount", "To Amount", "From Period", "To Period", "Rate of Interest"
            }
        ));
        tblInterestTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblInterestTableMousePressed(evt);
            }
        });
        srpInterestTable.setViewportView(tblInterestTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panInterestTable.add(srpInterestTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterestCalculation.add(panInterestTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panInterestCalc.add(panInterestCalculation, gridBagConstraints);

        tabInterestMaintenance.addTab("Interest Calculation", panInterestCalc);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterestMaintenance.add(tabInterestMaintenance, gridBagConstraints);

        getContentPane().add(panInterestMaintenance, java.awt.BorderLayout.CENTER);

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
        tbrHead.add(btnView);

        lblSpace5.setText("     ");
        tbrHead.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrHead.add(btnNew);

        lblSpace24.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace24.setText("     ");
        lblSpace24.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace24);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrHead.add(btnEdit);

        lblSpace25.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace25.setText("     ");
        lblSpace25.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace25);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrHead.add(btnDelete);

        lblSpace2.setText("     ");
        tbrHead.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrHead.add(btnSave);

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace26);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrHead.add(btnCancel);

        lblSpace3.setText("     ");
        tbrHead.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrHead.add(btnAuthorize);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace27);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrHead.add(btnException);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace28);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrHead.add(btnReject);

        lblSpace4.setText("     ");
        tbrHead.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrHead.add(btnPrint);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace29);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrHead.add(btnClose);

        getContentPane().add(tbrHead, java.awt.BorderLayout.NORTH);

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
        mnuProcess.add(sptDelete);

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

        mbrMain.add(mnuProcess);

        setJMenuBar(mbrMain);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rdoInterestType_CreditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoInterestType_CreditActionPerformed
        // TODO add your handling code here:
        if (CommonUtil.convertObjToStr(cboProdType.getSelectedItem()).length()>0) {
            observable.setRdoInterestType_Credit(true);
            observable.setRdoInterestType_Debit(false);
            observable.populateInterestTable("CREDIT");
            observable.resetComponents();
            observable.ttNotifyObservers();
        }
    }//GEN-LAST:event_rdoInterestType_CreditActionPerformed

    private void rdoInterestType_DebitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoInterestType_DebitActionPerformed
        // TODO add your handling code here:
        if (CommonUtil.convertObjToStr(cboProdType.getSelectedItem()).length()>0) {
            observable.setRdoInterestType_Debit(true);
            observable.setRdoInterestType_Credit(false);
            observable.populateInterestTable("DEBIT");
            observable.resetComponents();
            observable.ttNotifyObservers();
        }
    }//GEN-LAST:event_rdoInterestType_DebitActionPerformed
    
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        popUp(VIEW);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed
    
    private void tdtToDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtToDateFocusLost
        // TODO add your handling code here:
        ClientUtil.validateToDate(tdtToDate, tdtDate.getDateValue());
    }//GEN-LAST:event_tdtToDateFocusLost
    
    private void tdtDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtDateFocusLost
        // TODO add your handling code here:
        ClientUtil.validateFromDate(tdtDate, tdtToDate.getDateValue());
    }//GEN-LAST:event_tdtDateFocusLost
    
    private void cboProdTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboProdTypeItemStateChanged
        // TODO add your handling code here:
        observable.setCboProdType(CommonUtil.convertObjToStr(cboProdType.getSelectedItem()));
        if(observable.getCboProdType().length() > 1){
            observable.resetProductTab();
            String id = "";
            id = CommonUtil.convertObjToStr((((ComboBoxModel)(cboProdType).getModel())).getKeyForSelected());
            if(id.length() > 0){
                System.out.println("id: " +id);
                resetVisible();
                //__ To get the Prod Id'd depending on the Product Type selected...
                observable.getProductData(id);
                if(id.equalsIgnoreCase(TERMLOAN) || id.equalsIgnoreCase(ADVANCES)){
                    lblFromPeriod.setText("Repayment Period From");
                    lblToPeriod.setText("Repayment Period To");
                }else {
                    lblToPeriod.setText(resourceBundle.getString("lblToPeriod"));
                    lblFromPeriod.setText(resourceBundle.getString("lblFromPeriod"));
                }
                if(id.equalsIgnoreCase(OPERAIVE)){
                    setVisibleOA();
                    if(observable.getActionType() != ClientConstants.ACTIONTYPE_NEW){
                        if(!observable.getIsTableSet()){
                            observable.setInterestTabTitleOA();
                        }
                    }else{
                        observable.setInterestTabTitleOA();
                    }
                    //                    observable.setInterestTabTitleOA();
                }else if(id.equalsIgnoreCase(TERMLOAN)){
                    setVisibleTL();
                    if(observable.getActionType() != ClientConstants.ACTIONTYPE_NEW){
                        if(!observable.getIsTableSet()){
                            observable.setInterestTabTitle();
                        }
                    }else{
                        observable.setInterestTabTitle();
                    }
                    //                    observable.setInterestTabTitle();
                    
                }else if(id.equalsIgnoreCase(DEPOSITS)){
                    setVisibleTD();
                    //                    observable.setInterestTabTitle();
                    if(observable.getActionType() != ClientConstants.ACTIONTYPE_NEW){
                        if(!observable.getIsTableSet()){
                            observable.setInterestTabTitle();
                        }
                    }else{
                        observable.setInterestTabTitle();
                    }
                }else if(id.equalsIgnoreCase(ADVANCES)){
//                    setVisibleTD();
                    //                    observable.setInterestTabTitle();
                    if(observable.getActionType() != ClientConstants.ACTIONTYPE_NEW){
                        if(!observable.getIsTableSet()){
                            observable.setInterestTabTitle();
                        }
                    }else{
                        observable.setInterestTabTitle();
                    }
                }
                
                tblInterestTable.setModel(observable.getTblInterest());
            }
        }
    }//GEN-LAST:event_cboProdTypeItemStateChanged
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
    
    private void setVisibleOA(){
        lblFromAmount.setVisible(false);
        cboFromAmount.setVisible(false);
        lblToAmount.setVisible(false);
        cboToAmount.setVisible(false);
        lblFromPeriod.setVisible(false);
        panFromPeriod.setVisible(false);
        lblToPeriod.setVisible(false);
        panToPeriod.setVisible(false);
        lblPenalInterest.setVisible(true); //Made true by Rajesh
        txtPenalInterest.setVisible(true); //Made true by Rajesh
        lblPenalInterestPer.setVisible(true); //Made true by Rajesh
        lblODI.setVisible(true); //Made true by Rajesh
        txtODI.setVisible(true); //Made true by Rajesh
        lblODI_Per.setVisible(true); //Made true by Rajesh
        lblStatementPenal.setVisible(false);
        txtStatementPenal.setVisible(false);
        lblStatement_Per.setVisible(false);
        lblAgainstInterest.setVisible(false);
        txtAgainstInterest.setVisible(false);
        lblAgainstInterest_Per.setVisible(false);
        lblLimitAmt.setVisible(true); //Made true by Rajesh
        txtLimitAmt.setVisible(true); //Made true by Rajesh
        lblLimitAmt_Per.setVisible(true); //Made true by Rajesh
        lblInterExpiry.setVisible(false);
        txtInterExpiry.setVisible(false);
        lblInterExpiry_Per.setVisible(false);
        
        //        tblInterestTable.getColumn(resourceBundle.getString("tblInter3")).setMaxWidth(0);
        //        tblInterestTable.getColumn(resourceBundle.getString("tblInter4")).setMaxWidth(0);
        //        tblInterestTable.getColumn(resourceBundle.getString("tblInter5")).setMaxWidth(0);
        //        tblInterestTable.getColumn(resourceBundle.getString("tblInter6")).setMaxWidth(0);
    }
    
    private void setVisibleTD(){
        lblODI.setVisible(false);
        txtODI.setVisible(false);
        lblODI_Per.setVisible(false);
        lblStatementPenal.setVisible(false);
        txtStatementPenal.setVisible(false);
        lblStatement_Per.setVisible(false);
        lblAgainstInterest.setVisible(false);
        txtAgainstInterest.setVisible(false);
        lblAgainstInterest_Per.setVisible(false);
        lblLimitAmt.setVisible(false);
        txtLimitAmt.setVisible(false);
        lblInterExpiry.setVisible(false);
        txtInterExpiry.setVisible(false);
        lblInterExpiry_Per.setVisible(false);
    }
    
    private void setVisibleTL(){
        lblStatementPenal.setVisible(false);
        txtStatementPenal.setVisible(false);
        lblStatement_Per.setVisible(false);
        lblLimitAmt.setVisible(false);
        txtLimitAmt.setVisible(false);
        lblInterExpiry.setVisible(false);
        txtInterExpiry.setVisible(false);
        lblInterExpiry_Per.setVisible(false);
    }
    
    private void resetVisible(){
        lblFromAmount.setVisible(true);
        cboFromAmount.setVisible(true);
        lblToAmount.setVisible(true);
        cboToAmount.setVisible(true);
        lblFromPeriod.setVisible(true);
        panFromPeriod.setVisible(true);
        lblToPeriod.setVisible(true);
        panToPeriod.setVisible(true);
        lblPenalInterest.setVisible(true);
        txtPenalInterest.setVisible(true);
        lblPenalInterestPer.setVisible(true);
        lblODI.setVisible(true);
        txtODI.setVisible(true);
        lblODI_Per.setVisible(true);
        lblStatementPenal.setVisible(true);
        txtStatementPenal.setVisible(true);
        lblStatement_Per.setVisible(true);
        lblAgainstInterest.setVisible(true);
        txtAgainstInterest.setVisible(true);
        lblAgainstInterest_Per.setVisible(true);
        lblLimitAmt.setVisible(true);
        txtLimitAmt.setVisible(true);
        lblInterExpiry.setVisible(true);
        txtInterExpiry.setVisible(true);
        lblInterExpiry_Per.setVisible(true);
    }
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        tblProduct.setEnabled(false);
        tblCategory.setEnabled(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    public void authorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled){
            System.out.println("inside reject Authorize");
            //__ To check if all the tabs have been visited or not...
            //Changed By Suresh
//            String warningMessage = tabInterestMaintenance.isAllTabsVisited();
            
//            if(warningMessage.length() > 0){
//                displayAlert(warningMessage);
//                
//            }else{
                //__ To reset the value of the visited tabs...
                tabInterestMaintenance.resetVisits();
                
                HashMap singleAuthorizeMap = new HashMap();
                singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
                singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                singleAuthorizeMap.put("ROI GROUPID", lblGroupIdDesc.getText());
                singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
                
                ClientUtil.execute("authInterMaintenance", singleAuthorizeMap);
                ClientUtil.execute("authInterMaintenanceForDepositRoiGroupTypeRate", singleAuthorizeMap);
                
                btnSave.setEnabled(true);
                viewType = -1;
                super.setOpenForEditBy(observable.getStatusBy());
                super.removeEditLock(lblGroupIdDesc.getText());
                
                btnCancelActionPerformed(null);
//            }
        }else{
            System.out.println("inside else part of authorize");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            
            HashMap mapParam = new HashMap();
            
            //__ To Save the data in the Internal Frame...
            setModified(true);
            
            mapParam.put(CommonConstants.MAP_NAME, "getSelectInterMaintenance");
            
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            
            //            whereMap.put("BRANCHID",TrueTransactMain.BRANCH_ID);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authInterMaintenance");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authInterMaintenanceForDepositRoiGroupTypeRate");
            
            whereMap = null;
            isFilled = false;
            
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            lblStatus.setText(ClientConstants.RESULT_STATUS[authorizeUI.getResultStatus()]);
            btnSave.setEnabled(false);
            btnReject.setEnabled(true);
            
            //__ If there's no data to be Authorized, call Cancel action...
            if(!isModified()){
                setButtonEnableDisable();
                btnCancelActionPerformed(null);
            }
        }
    }
    private void tblInterestTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInterestTableMousePressed
        // Add your handling code here:
        updateOBFields();
        int selectedRow = tblInterestTable.getSelectedRow();
        observable.populateInterestTab(selectedRow);
        
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
            ClientUtil.enableDisable(panCalculations,false);
            ClientUtil.enableDisable(panGroupData,false);
        }
        if ((observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE)
        && (observable.getActionType() != ClientConstants.ACTIONTYPE_AUTHORIZE)){
            String authorizeStatus = observable.getAuthorizeStatus();
            if(authorizeStatus!=null && authorizeStatus.length()>0){
                if(authorizeStatus.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)){
                    ClientUtil.enableDisable(panCalculations,false);
                    ClientUtil.enableDisable(panGroupData,false);
                    btnTabDelete.setEnabled(false);
                    btnTabSave.setEnabled(false);
                }
            }
            else{
                ClientUtil.enableDisable(panCalculations,true);
                setInterTabEnableDisable(true);
                tabNewButtons();
                try{
                    componentEnableDisable();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            
        }else{
            ClientUtil.enableDisable(panCalculations,false);
            
        }
        
        updateTab = 1;
        rowSelected = selectedRow;
        observable.ttNotifyObservers();
        tablePressed = true;
    }//GEN-LAST:event_tblInterestTableMousePressed
    //__ To Set the Fields at the time of Row Selected as Enable/Disable...
    private void setInterTabEnableDisable(boolean value){
        tdtDate.setEnabled(value);
        tdtToDate.setEnabled(value);
        cboFromAmount.setEnabled(value);
        cboToAmount.setEnabled(value);
        ClientUtil.enableDisable(panFromPeriod,value);
        ClientUtil.enableDisable(panToPeriod,value);
    }
    private void btnTabDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTabDeleteActionPerformed
        // Add your handling code here:
        updateOBFields();
        flag = true;
        observable.deleteInterestTab(rowSelected);
        ClientUtil.enableDisable(panCalculations,false);
        tabSaveDeleteButtons();
        observable.resetTable();
        observable.ttNotifyObservers();
        rowSelected = -1;
    }//GEN-LAST:event_btnTabDeleteActionPerformed
    private void tabSaveDeleteButtons(){
        btnTabNew.setEnabled(true);
        btnTabSave.setEnabled(false);
        btnTabDelete.setEnabled(false);
    }
    
    private void tabNewButtons(){
        btnTabNew.setEnabled(true);
        btnTabSave.setEnabled(true);
        btnTabDelete.setEnabled(true);
    }
    private void tabNewButtonsDisable(){
        btnTabNew.setEnabled(false);
        btnTabSave.setEnabled(true);
        btnTabDelete.setEnabled(true);
    }
    private void btnTabSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTabSaveActionPerformed
        // Add your handling code here:
        if(rdorateType_Normal.isSelected()){
        if(txtRateInterest.getText().equals("") || txtRateInterest.getText().equals("0") /*|| txtPenalInterest.getText().equals("") || txtPenalInterest.getText().equals("0")*/){
            ClientUtil.displayAlert("Interest rate and penal interest should be greater than zero!!!!!!");
            return;
        }
        }
        int result=0;
        updateOBFields();
        final String FROMAMT = CommonUtil.convertObjToStr(((ComboBoxModel)(cboFromAmount.getModel())).getKeyForSelected());
        final String TOAMT = CommonUtil.convertObjToStr(((ComboBoxModel)(cboToAmount.getModel())).getKeyForSelected());
        try{
            ClientUtil.enableDisable(panCalculations, false);
            String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panCalculations);
            StringBuffer strBAlert = new StringBuffer();
            if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
                strBAlert.append(mandatoryMessage+"\n");
            }
            
            //            if(Double.parseDouble(TOAMT) < Double.parseDouble(FROMAMT)){
            //                strBAlert.append(resourceBundle.getString("AMOUNT_WARNING")+"\n");
            //            } if(observable.period()==1){
            //                strBAlert.append(resourceBundle.getString("RANGE_WARNING")+"\n");
            //            }
            
            if(CommonUtil.convertObjToDouble(TOAMT).doubleValue() < CommonUtil.convertObjToDouble(FROMAMT).doubleValue()){
                strBAlert.append(resourceBundle.getString("AMOUNT_WARNING")+"\n");
            } if(observable.period()==1){
                strBAlert.append(resourceBundle.getString("RANGE_WARNING")+"\n");
            }
            //            if( (Double.parseDouble(txtRateInterest.getText()) > 100)
            //            ||(Double.parseDouble(txtPenalInterest.getText()) > 100)){
            //                strBAlert.append(resourceBundle.getString("RATE_WARNING")+"\n");
            //            }
            
            if((CommonUtil.convertObjToDouble(txtRateInterest.getText()).doubleValue() > 100)
            ||(CommonUtil.convertObjToDouble(txtPenalInterest.getText()).doubleValue() > 100)){
                strBAlert.append(resourceBundle.getString("RATE_WARNING")+"\n");
            }
            
            //            if(Double.parseDouble(txtPenalInterest.getText()) > Double.parseDouble(txtRateInterest.getText())){
            //                strBAlert.append(resourceBundle.getString("PENAL_WARNING")+"\n");
            //            }
            
            if(CommonUtil.convertObjToDouble(txtPenalInterest.getText()).doubleValue() > CommonUtil.convertObjToDouble(txtRateInterest.getText()).doubleValue()){
                strBAlert.append(resourceBundle.getString("PENAL_WARNING")+"\n");
            }
            
            if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && strBAlert.toString().length() > 0 ){
                displayAlert(strBAlert.toString());
            }else{
                final String PRODTYPE = CommonUtil.convertObjToStr((((ComboBoxModel)(cboProdType).getModel())).getKeyForSelected());
                result = observable.addTabData(updateTab, rowSelected);
                //check&make enable disable
                //                if (!tablePressed)
                boolean maxAmtRun = false;
                if(rowSelected!=-1) {
                    if(PRODTYPE.equalsIgnoreCase(TERMLOAN) || PRODTYPE.equalsIgnoreCase(ADVANCES) ||
                    PRODTYPE.equalsIgnoreCase(AGRITERMLOAN) || PRODTYPE.equalsIgnoreCase(AGRIADVANCES))
                        
                        if (observable.maxAmtPeriodCheck(rowSelected)) {
                            btnTabNewActionPerformed();
                            componentEnableDisable();
                            if(observable.setNextValue())
                                cboToAmount.setEnabled(true);
                            else
                                cboToAmount.setEnabled(false);
                            observable.ttNotifyObservers();
                            tabNewButtonsDisable();
                        }
                    maxAmtRun = true;
                }
                if(PRODTYPE.equalsIgnoreCase(TERMLOAN) || PRODTYPE.equalsIgnoreCase(ADVANCES) ||
                PRODTYPE.equalsIgnoreCase(AGRITERMLOAN) || PRODTYPE.equalsIgnoreCase(AGRIADVANCES))
                    if((!maxAmtRun) && observable.slabBasedEnableDisable()){
                        btnTabNewActionPerformed();
                        //                     if(!(observable.isMinPeriod() && observable.isMaxPeriod() && observable.isMinAmount()&& observable.isMaxAmount())){
                        componentEnableDisable();
                        if(observable.setNextValue())
                            cboToAmount.setEnabled(true);
                        else
                            cboToAmount.setEnabled(false);
                        //                     }
                        //                     tdtDate.setEnabled(false);
                        //                    cboFromAmount.setEnabled(false);
                        //                    cboToAmount.setEnabled(false);
                        //                    txtFromPeriod.setEnabled(false);
                        //                    cboFromPeriod.setEnabled(false);
                        //                    tdtToDate.setEnabled(false);
                        //                    if(observable.setNextValue())
                        //                        cboToAmount.setEnabled(true);
                        //                    else
                        //                        cboToAmount.setEnabled(false);
                        observable.ttNotifyObservers();
                        tabNewButtonsDisable();
                        
                    }
                //                else
                //                    observable.setNextSlabList(null);
                tablePressed = false;
                if (result == 1){
                    ClientUtil.enableDisable(panCalculations, true);
                    tabNewButtons();
                }else{
                    if (observable.getActionType()!=ClientConstants.ACTIONTYPE_EDIT && observable.getActionType()!=ClientConstants.ACTIONTYPE_NEW)
                        ClientUtil.enableDisable(panCalculations,false);
                    
                    if(observable.getNextSlabList()==null || observable.getNextSlabList().isEmpty()){
                        observable.resetTable();
                        tabSaveDeleteButtons();
                    }
                }
                observable.ttNotifyObservers();
                updateTab = -1;
                rowSelected = -1;
            }
            btnTabNew.setEnabled(true);
        }catch (Exception e){
            System.out.println("Error in btnTabSaveActionPerformed");
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnTabSaveActionPerformed
    
    private void componentEnableDisable()throws Exception{
        String prodType=CommonUtil.convertObjToStr(((ComboBoxModel)cboProdType.getModel()).getKeyForSelected());
        if(!(prodType.equals("OA")||prodType.equals("TD"))){
        tdtDate.setEnabled(false);
        cboFromAmount.setEnabled(false);
        cboToAmount.setEnabled(false);
        txtFromPeriod.setEnabled(false);
        cboFromPeriod.setEnabled(false);
        tdtToDate.setEnabled(false);
        cboToAmount.setEnabled(true);
        
        } 
        
    }
    private void btnTabNewActionPerformed(){
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panGroupnfo);
        tablePressed=false;
        if (!observable.isRdoInterestType_Debit() && !observable.isRdoInterestType_Credit())
            mandatoryMessage += resourceBundle.getString("INT_TYPE_WARNING");
        if(observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0){
            displayAlert(mandatoryMessage);
        }else{
            updateOBFields();
            observable.resetTable();
            ClientUtil.enableDisable(panCalculations, true);
            
            tabNewButtons();
            updateTab = -1;
            rowSelected = -1;
            observable.ttNotifyObservers();
            final String PRODTYPE = CommonUtil.convertObjToStr((((ComboBoxModel)(cboProdType).getModel())).getKeyForSelected());
            //__  To put the default values into Amount and period range...
            if(PRODTYPE.equalsIgnoreCase(OPERAIVE)){
                cboFromAmount.setSelectedIndex(1);
                cboToAmount.setSelectedIndex(cboToAmount.getItemCount() - 1);
                
                txtFromPeriod.setText("1");
                cboFromPeriod.setSelectedItem(((ComboBoxModel) cboFromPeriod.getModel()).getDataForKey("DAYS"));
                
                txtToPeriod.setText("99");
                cboToPeriod.setSelectedItem(((ComboBoxModel) cboToPeriod.getModel()).getDataForKey("YEARS"));
            }
            if(PRODTYPE.equalsIgnoreCase(TERMLOAN) || PRODTYPE.equalsIgnoreCase(ADVANCES) ||
            PRODTYPE.equalsIgnoreCase(AGRITERMLOAN) || PRODTYPE.equalsIgnoreCase(AGRIADVANCES))
                if((!flag) && observable.getNextSlabList()==null ) {//|| observable.getNextSlabList().isEmpty()
                    cboFromAmount.setSelectedItem(((ComboBoxModel) cboFromAmount.getModel()).getDataForKey("1"));
                    txtFromPeriod.setText("1");
                    cboFromPeriod.setSelectedItem(((ComboBoxModel) cboFromPeriod.getModel()).getDataForKey("DAYS"));
                    cboFromAmount.setEnabled(false);
                    txtFromPeriod.setEnabled(false);
                    cboFromPeriod.setEnabled(false);
                    
                }
        }
    }
    private void btnTabNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTabNewActionPerformed
        // Add your handling code here:
        //
        btnTabNewActionPerformed();
        
    }//GEN-LAST:event_btnTabNewActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
        //        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_btnPrintActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        if(observable.getAuthorizeStatus()!=null)
            super.removeEditLock(lblGroupIdDesc.getText());
        //__ To reset the value of the visited tabs...
        tabInterestMaintenance.resetVisits();
        
        
        resetUI();
        //__ New Code...
        observable.setInterestTabTitle();
        ClientUtil.enableDisable(this, false);  //__ Disables the panel...
        setButtonEnableDisable();   //__ Enables or Disables the buttons and menu Items depending on their previous state...
        tabSaveDeleteButtons();
        btnTabNew.setEnabled(false);
        observable.resetDataList();
        
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);    //__ Sets the Action Type to be performed...
        observable.setStatus(); //__ To set the Value of lblStatus...
        
        isFilled = false;
        viewType = -1;
        resetVisible();
        observable.resetTableValues();
        //__ Make the Screen Closable..
        setModified(false);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnCancelActionPerformed
    private void resetUI(){
        observable.resetStatus();   //__ To reset the status
        observable.resetForm();     //__ Reset the fields in the UI to null...
        observable.resetLable();    //__ Reset the Editable Lables in the UI to null...
        observable.resetInterTab();
        observable.resetProductTab();
        observable.resetCategoryTab();
        
        observable.setIsTableSet(false);
    }
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        observable.setRdoRateType_Normal(rdorateType_Normal.isSelected());
        observable.setRdoRateType_Spl(rdorateType_Spl.isSelected());
        int a = tblInterestTable.getRowCount();
        if(a==0){
            displayAlert("Please set interest rate before saving data!!!!!!!!");
            return;
        }
      
                //check allslab rate amt period max reached or not
        
        String prodType=CommonUtil.convertObjToStr(((ComboBoxModel)cboProdType.getModel()).getKeyForSelected());
//        boolean checkSlabRateUI = observable.checkSlabRateUI();
//        boolean checkSlabRateMainSave = observable.checkSlabRateMainSave();
        
        if(!(prodType.equals("OA") )){
            if(observable.checkSlabRateUI())
                return;
            if(observable.checkSlabRateMainSave())
                return;
        }
        //        if(rowSelected!=-1)
        //        if(!observable.maxAmtPeriodCheck(rowSelected))//tblInterestTable.getModel().getRowCount()-1
        //            return;
        updateOBFields();
        
        //__ To display an alert if the mandatory fields are not properly inputted, else proceed with normal operation
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panGroupnfo);
        boolean value = observable.sortData();
        
        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
            displayAlert(mandatoryMessage);
        }else{
            if(observable.getActionType() != ClientConstants.ACTIONTYPE_NEW && !(observable.getRowCount() > 0)){
                displayAlert(resourceBundle.getString("NOROWWARNING"));
                
            }else{
                if(value){
                    boolean displayAlert = false;
                    if (tblInterestTable.getColumnCount()>3) {
                        String TOAMT = CommonUtil.convertObjToStr(tblInterestTable.getValueAt( a-1, 3));
                        String TOPERIOD =CommonUtil.convertObjToStr(tblInterestTable.getValueAt(a-1, 5));
                        if ((!(TOAMT.equals("9999999999") && (TOPERIOD.equals("364635 Days")|| TOPERIOD.equals("999 Years") || TOPERIOD.equals("11988 Months"))))&& flag == false) {//jeffin
                            displayAlert = true;
                        }
                    }
                    if (displayAlert) {
                        displayAlert(resourceBundle.getString("AMOUNTWARNING"));
                    }
                    else{
                        observable.doAction();  //__ To perform the necessary operation depending on the Action type...
                        HashMap checkDuplicate = (HashMap) observable.getProxyReturnMap();
                        if(checkDuplicate != null && !checkDuplicate.isEmpty() && checkDuplicate.containsKey("DUPLICATE")){
                            ClientUtil.displayAlert("Duplicate group Exists");
                            return;
                        }
                        if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
                            HashMap lockMap = new HashMap();
                            ArrayList lst = new ArrayList();
                            lst.add("ROI GROUPID");
                            lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                            if (observable.getProxyReturnMap()!=null) {
                                if (observable.getProxyReturnMap().containsKey("ROI GROUPID")) {
                                    lockMap.put("ROI GROUPID", observable.getProxyReturnMap().get("ROI GROUPID"));
                                }
                            }
                            if( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                                lockMap.put("ROI GROUPID", lblGroupIdDesc.getText());
                            }
                            setEditLockMap(lockMap);
                            setEditLock();
                            
                            //                        super.removeEditLock(lblGroupIdDesc.getText());
                            resetUI();
                            //__ New Code...
                            observable.setInterestTabTitle();
                            
                            ClientUtil.enableDisable(this, false);  //__ Disables the panel...
                            setButtonEnableDisable();   //__ Enables or Disables the buttons and menu Items depending on their previous state...
                            btnTabNew.setEnabled(false);
                            observable.setResultStatus();   //__ To Reset the Value of lblStatus...
                            observable.resetDataList();
                        }
                    }
                }
            }
        }
        //__ Make the Screen Closable..
        setModified(false);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        flag= false;
    }//GEN-LAST:event_btnSaveActionPerformed
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        resetUI();
        //__ Sets the Action Type to be performed...
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        popUp(EDIT);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        resetUI();
        //__ Sets the Action Type to be performed...
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        popUp(EDIT);
        txtGroupName.setEditable(false);
        tblCategory.setEnabled(true);
        tblProduct.setEnabled(true);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
    private void popUp(int field) {
        final HashMap viewMap = new HashMap();
        viewType = field;
        if(field==EDIT || field==DELETE || field==AUTHORIZE || field==VIEW ){
            ArrayList lst = new ArrayList();
            lst.add("ROI GROUPID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            viewMap.put(CommonConstants.MAP_NAME, "viewInterestMaintenance");
        }
        //__ Do not want the Clear Button in this Case...
        new ViewAll(this, viewMap, false).show();
    }
    
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        if (viewType==EDIT || viewType==DELETE || viewType==AUTHORIZE || viewType==VIEW) {
            isFilled = true;
            hash.put(CommonConstants.MAP_WHERE, hash.get("ROI GROUPID"));
            final String prodType = CommonUtil.convertObjToStr(hash.get("PRODUCT TYPE"));
            if(prodType.equalsIgnoreCase(OPERAIVE)){
                observable.setInterestTabTitleOA();
            }else{
                observable.setInterestTabTitle();
            }
            tblInterestTable.setModel(observable.getTblInterest());
            
            
            observable.getProductData(prodType);
            observable.getCategoryData();
            observable.populateData(hash);  //__ Called to display the Data in the UI fields...
            /**
             * To Set the Flag so as not to reset the Data of the Prod Table...
             */
            observable.ttNotifyObservers();
            //__ To set the Value of Transaction Id...
            
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE ||viewType==AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
                ClientUtil.enableDisable(this, false);     //__ Disables the panel...
                tabSaveDeleteButtons();
                btnTabNew.setEnabled(false);
            }else{
                ClientUtil.enableDisable(this, true);     //__ Enables the panel...
                ClientUtil.enableDisable(panCalculations,false);
                tabSaveDeleteButtons();
            }
            setButtonEnableDisable();                     //__ Enables or Disables the buttons and menu Items depending on their previous state...
            observable.setStatus();                       //__ To set the Value of lblStatus...
            
            cboProdType.setEnabled(false);
            
            ArrayList data = (observable.getTblProduct()).getDataArrayList();
            final int dataSize = data.size();
            List resultList = observable.getProductTabList();
            System.out.println("resultList in UI: " + resultList);
            //            System.out.println("Size of resultList in UI: " + resultList.size());
            
            final int length = resultList.size();
            String prodTabId = "";
            for(int j = 0; j < length; j++){
                prodTabId = CommonUtil.convertObjToStr(((HashMap)resultList.get(j)).get("PROD_ID"));
                for (int i=0;i<dataSize;i++){
                    if((observable.getTblProduct()).getValueAt(i, 1).equals(prodTabId) ){
                        (observable.getTblProduct()).setValueAt(new Boolean(true), i, 0);
                    }
                }
            }
            if (viewType== AUTHORIZE ) {
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
            }
        }
        
        //__ To Save the data in the Internal Frame...
        setModified(true);
        rdoInterestType_Debit.setEnabled(true);
        rdoInterestType_Credit.setEnabled(true);
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        observable.resetForm();              //__ Reset the fields in the UI to null...
        observable.resetLable();             //__ Reset the Editable Lables in the UI to null...
        ClientUtil.enableDisable(this, true);//__ Enables the panel...
        
        ClientUtil.enableDisable(panCalculations,false);
        tabSaveDeleteButtons();
        btnTabNew.setEnabled(true);
        
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);//Sets the Action Type to be performed...
        setButtonEnableDisable();           //__ Enables or Disables the buttons and menu Items depending on their previous state...
        observable.setStatus();             //__ To set the Value of lblStatus...
        /** To get the Data for the Tables at the time of New...
         */
        //observable.getProductData();
        observable.getCategoryData();
        //        observable.getInterestRateData();
        observable.ttNotifyObservers();
        
        //__ To Save the data in the Internal Frame...
        setModified(true);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnNewActionPerformed
    
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
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm

    private void rdorateType_NormalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdorateType_NormalActionPerformed
     observable.setRdoRateType_Normal(true);
     observable.setRdoRateType_Spl(false);
     observable.resetComponents();
     observable.ttNotifyObservers();         
 
        // TODO add your handling code here:
    }//GEN-LAST:event_rdorateType_NormalActionPerformed

    private void rdorateType_SplActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdorateType_SplActionPerformed
        observable.setRdoRateType_Spl(true);
        observable.setRdoRateType_Normal(false);
       observable.resetComponents();
      observable.ttNotifyObservers();       // TODO add your handling code here:
    }//GEN-LAST:event_rdorateType_SplActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new InterestMaintenanceUI().show();
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
    private com.see.truetransact.uicomponent.CButton btnTabDelete;
    private com.see.truetransact.uicomponent.CButton btnTabNew;
    private com.see.truetransact.uicomponent.CButton btnTabSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CComboBox cboFromAmount;
    private com.see.truetransact.uicomponent.CComboBox cboFromPeriod;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CComboBox cboToAmount;
    private com.see.truetransact.uicomponent.CComboBox cboToPeriod;
    private com.see.truetransact.uicomponent.CLabel lblAgainstInterest;
    private com.see.truetransact.uicomponent.CLabel lblAgainstInterest_Per;
    private com.see.truetransact.uicomponent.CLabel lblDate;
    private com.see.truetransact.uicomponent.CLabel lblFromAmount;
    private com.see.truetransact.uicomponent.CLabel lblFromPeriod;
    private com.see.truetransact.uicomponent.CLabel lblGroupId;
    private com.see.truetransact.uicomponent.CLabel lblGroupIdDesc;
    private com.see.truetransact.uicomponent.CLabel lblGroupName;
    private com.see.truetransact.uicomponent.CLabel lblInterExpiry;
    private com.see.truetransact.uicomponent.CLabel lblInterExpiry_Per;
    private com.see.truetransact.uicomponent.CLabel lblInterestType;
    private com.see.truetransact.uicomponent.CLabel lblLimitAmt;
    private com.see.truetransact.uicomponent.CLabel lblLimitAmt_Per;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblODI;
    private com.see.truetransact.uicomponent.CLabel lblODI_Per;
    private com.see.truetransact.uicomponent.CLabel lblPenalInterest;
    private com.see.truetransact.uicomponent.CLabel lblPenalInterestPer;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    private com.see.truetransact.uicomponent.CLabel lblRateInterest;
    private com.see.truetransact.uicomponent.CLabel lblRateInterestPer;
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
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpaces;
    private com.see.truetransact.uicomponent.CLabel lblStatementPenal;
    private com.see.truetransact.uicomponent.CLabel lblStatement_Per;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblToAmount;
    private com.see.truetransact.uicomponent.CLabel lblToDate;
    private com.see.truetransact.uicomponent.CLabel lblToPeriod;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panButtons;
    private com.see.truetransact.uicomponent.CPanel panCalculations;
    private com.see.truetransact.uicomponent.CPanel panFromPeriod;
    private com.see.truetransact.uicomponent.CPanel panGroupData;
    private com.see.truetransact.uicomponent.CPanel panGroupnfo;
    private com.see.truetransact.uicomponent.CPanel panInterestCalc;
    private com.see.truetransact.uicomponent.CPanel panInterestCalculation;
    private com.see.truetransact.uicomponent.CPanel panInterestGroup;
    private com.see.truetransact.uicomponent.CPanel panInterestMaintenance;
    private com.see.truetransact.uicomponent.CPanel panInterestTable;
    private com.see.truetransact.uicomponent.CPanel panModeOfOpening;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panToPeriod;
    private com.see.truetransact.uicomponent.CButtonGroup rdRateTypegroup;
    private com.see.truetransact.uicomponent.CButtonGroup rdgInterestType;
    private com.see.truetransact.uicomponent.CRadioButton rdoInterestType_Credit;
    private com.see.truetransact.uicomponent.CRadioButton rdoInterestType_Debit;
    private com.see.truetransact.uicomponent.CRadioButton rdorateType_Normal;
    private com.see.truetransact.uicomponent.CRadioButton rdorateType_Spl;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private com.see.truetransact.uicomponent.CScrollPane srpCategory;
    private com.see.truetransact.uicomponent.CScrollPane srpInterestTable;
    private com.see.truetransact.uicomponent.CScrollPane srpProduct;
    private com.see.truetransact.uicomponent.CTabbedPane tabInterestMaintenance;
    private com.see.truetransact.uicomponent.CTable tblCategory;
    private com.see.truetransact.uicomponent.CTable tblInterestTable;
    private com.see.truetransact.uicomponent.CTable tblProduct;
    private javax.swing.JToolBar tbrHead;
    private com.see.truetransact.uicomponent.CDateField tdtDate;
    private com.see.truetransact.uicomponent.CDateField tdtToDate;
    private com.see.truetransact.uicomponent.CTextField txtAgainstInterest;
    private com.see.truetransact.uicomponent.CTextField txtFromPeriod;
    private com.see.truetransact.uicomponent.CTextField txtGroupName;
    private com.see.truetransact.uicomponent.CTextField txtInterExpiry;
    private com.see.truetransact.uicomponent.CTextField txtLimitAmt;
    private com.see.truetransact.uicomponent.CTextField txtODI;
    private com.see.truetransact.uicomponent.CTextField txtPenalInterest;
    private com.see.truetransact.uicomponent.CTextField txtRateInterest;
    private com.see.truetransact.uicomponent.CTextField txtStatementPenal;
    private com.see.truetransact.uicomponent.CTextField txtToPeriod;
    // End of variables declaration//GEN-END:variables
    
}
