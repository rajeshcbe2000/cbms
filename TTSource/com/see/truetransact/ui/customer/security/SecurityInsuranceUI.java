    /*
	* Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
    *
    * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
    * 
    * SecurityInsuranceUI.java
    *
    * Created on January 12, 2005, 5:20 PM
    */

package com.see.truetransact.ui.customer.security;

import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observer;
import java.util.Observable;
/**
 *
 * @author  152713
 */
public class SecurityInsuranceUI extends CInternalFrame  implements java.util.Observer,UIMandatoryField {
    HashMap mandatoryMap;
    SecurityInsuranceRB resourceBundle = new SecurityInsuranceRB();
    
    SecurityOB observableSecurity;
    InsuranceOB observableInsurance;
    private boolean updateSecurity = false;
    private boolean updateInsurance = false;
    private boolean isFilled = false;
    
    private final static Logger log = Logger.getLogger(SecurityInsuranceUI.class);
    private final        String AUTHORIZE = "AUTHORIZE";
    private final        String GOLD_SILVER_ORNAMENTS = "GOLD_SILVER_ORNAMENTS";
    
    int result;
    int rowSecurity  = -1;
    int rowInsurance = -1;
    
    private String viewType = "";
    
    
    /** Creates new form SecurityInsuranceUI */
    public SecurityInsuranceUI() {
        initComponents();
        setFieldNames();
        internationalize();
        setMaxLength();
        setObservable();
        ClientUtil.enableDisable(this, false);
        allEnableDisable();
        initComponentData();
        setMandatoryHashMap();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panPolicy);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panPolicyNature);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panSecDetails);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panSecNature);
        setHelpMessage();
        observableSecurity.resetForm();
        observableSecurity.resetStatus();
    }
    private void allEnableDisable(){
        setButtonEnableDisable();
        txtTotalSecurity_Value.setEnabled(true);
        txtTotalSecurity_Value.setEditable(false);
        setAllSecurityDetailsEnableDisable(false);
        setAllSecurityBtnsEnableDisable(false);
        setAllInsuranceDetailsEnableDisable(false);
        setAllInsuranceBtnsEnableDisable(false);
        txtSecurityNo.setEditable(false);
    }
    
    private void setObservable(){
        observableSecurity = SecurityOB.getInstance();
        observableSecurity.addObserver(this);
        observableInsurance = InsuranceOB.getInstance();
        observableInsurance.addObserver(this);
    }
    
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        lblInsuranceNo_Disp.setName("lblInsuranceNo_Disp");
        cboInsuranceNo.setName("cboInsuranceNo");
        panSecurityDetails_security.setName("panSecurityDetails_security");
        panLoanAvailed.setName("panLoanAvailed");
        panLoanTable.setName("panLoanTable");
        srpLoanTable.setName("srpLoanTable");
        tblLoanTable.setName("tblLoanTable");
        tabSecurityDetails.setName("tabSecurityDetails");
        lblCustID.setName("lblCustID");
        lblCustID_Disp.setName("lblCustID_Disp");
        lblCustName.setName("lblCustName");
        lblCustName_Disp.setName("lblCustName_Disp");
        lblCustEmail_ID.setName("lblCustEmail_ID");
        lblCustEmail_ID_Disp.setName("lblCustEmail_ID_Disp");
        lblCustCity.setName("lblCustCity");
        lblCustCity_Disp.setName("lblCustCity_Disp");
        lblCustStreet.setName("lblCustStreet");
        lblCustStreet_Disp.setName("lblCustStreet_Disp");
        lblCustPin.setName("lblCustPin");
        lblCustPin_Disp.setName("lblCustPin_Disp");
        lblNetWeight.setName("lblNetWeight");
        lblGrossWeight.setName("lblGrossWeight");
        txtGrossWeight.setName("txtGrossWeight");
        txtNetWeight.setName("txtNetWeight");
        
        PanSecurityDetails.setName("PanSecurityDetails");
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnDelete_Insurance.setName("btnDelete_Insurance");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnNew.setName("btnNew");
        btnNew_Insurance.setName("btnNew_Insurance");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        btnSave_Insurance.setName("btnSave_Insurance");
        btnSecurityDelete.setName("btnSecurityDelete");
        btnSecurityNew.setName("btnSecurityNew");
        btnSecuritySave.setName("btnSecuritySave");
        cboForMillIndus.setName("cboForMillIndus");
        cboNatureCharge.setName("cboNatureCharge");
        cboNatureRisk.setName("cboNatureRisk");
        cboSecurityCate.setName("cboSecurityCate");
        cboSecurityNo_Insurance.setName("cboSecurityNo_Insurance");
        cboStockStateFreq.setName("cboStockStateFreq");
        chkSelCommodityItem.setName("chkSelCommodityItem");
        lblAson.setName("lblAson");
        lblDateCharge.setName("lblDateCharge");
        lblDateInspection.setName("lblDateInspection");
        lblExpityDate.setName("lblExpityDate");
        lblForMillIndus.setName("lblForMillIndus");
        lblFromDate.setName("lblFromDate");
        lblInsureCompany.setName("lblInsureCompany");
        lblInsuranceNo_Display.setName("lblInsuranceNo_Display");
        lblAvalSecVal.setName("lblAvalSecVal");
        lblNatureCharge.setName("lblNatureCharge");
        lblNatureRisk.setName("lblNatureRisk");
        lblParticulars.setName("lblParticulars");
        lblPolicyAmt.setName("lblPolicyAmt");
        lblPolicyDate.setName("lblPolicyDate");
        lblPolicyNumber.setName("lblPolicyNumber");
        lblPremiumAmt.setName("lblPremiumAmt");
        lblRemark_Insurance.setName("lblRemark_Insurance");
        lblSecurityCate.setName("lblSecurityCate");
        lblSecurityNo.setName("lblSecurityNo");
        lblSecurityNo_Insurance.setName("lblSecurityNo_Insurance");
        lblSecurityType.setName("lblSecurityType");
        lblSecurityValue.setName("lblSecurityValue");
        lblSelCommodityItem.setName("lblSelCommodityItem");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblStockStateFreq.setName("lblStockStateFreq");
        lblToDate.setName("lblToDate");
        lblTotalSecurity_Value.setName("lblTotalSecurity_Value");
        lblspace3.setName("lblspace3");
        mbrSecInsur.setName("mbrSecInsur");
        panInsuranceCTable.setName("panInsuranceCTable");
        panInsuranceDetails.setName("panInsuranceDetails");
        panInsuranceToolBars.setName("panInsuranceToolBars");
        panInsureDetails_details.setName("panInsureDetails_details");
        panPolicy.setName("panPolicy");
        panPolicyNature.setName("panPolicyNature");
        panSecDetails.setName("panSecDetails");
        panSecNature.setName("panSecNature");
        panSecurity.setName("panSecurity");
        panSecurityDetails.setName("panSecurityDetails");
        panSecurityDetails_Cust.setName("panSecurityDetails_Cust");
        panSecurityDetails_security.setName("panSecurityDetails_security");
        panSecurityNature.setName("panSecurityNature");
        panSecurityTable.setName("panSecurityTable");
        panSecurityTableMain.setName("panSecurityTableMain");
        panSecurityTools.setName("panSecurityTools");
        panSecurityType.setName("panSecurityType");
        panTotalSecurity_Value.setName("panTotalSecurity_Value");
        rdoSecurityType_Collateral.setName("rdoSecurityType_Collateral");
        rdoSecurityType_Primary.setName("rdoSecurityType_Primary");
        sptInsureDetails_Vert.setName("sptInsureDetails_Vert");
        sptSecurityDetails_Vert.setName("sptSecurityDetails_Vert");
        srpInsuranceCTable.setName("srpInsuranceCTable");
        srpSecurityTable.setName("srpSecurityTable");
        tblInsurance.setName("tblInsurance");
        tblSecurityTable.setName("tblSecurityTable");
        tdtAson.setName("tdtAson");
        tdtDateCharge.setName("tdtDateCharge");
        tdtDateInspection.setName("tdtDateInspection");
        tdtExpityDate.setName("tdtExpityDate");
        tdtFromDate.setName("tdtFromDate");
        tdtPolicyDate.setName("tdtPolicyDate");
        tdtToDate.setName("tdtToDate");
        txtInsureCompany.setName("txtInsureCompany");
        txtInsuranceNo.setName("txtInsuranceNo");
        txtAvalSecVal.setName("txtAvalSecVal");
        txtAreaParticular.setName("txtAreaParticular");
        txtPolicyAmt.setName("txtPolicyAmt");
        txtPolicyNumber.setName("txtPolicyNumber");
        txtPremiumAmt.setName("txtPremiumAmt");
        txtRemark_Insurance.setName("txtRemark_Insurance");
        txtSecurityNo.setName("txtSecurityNo");
        txtSecurityValue.setName("txtSecurityValue");
        txtTotalSecurity_Value.setName("txtTotalSecurity_Value");
    }
    
    /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        btnSecurityDelete.setText(resourceBundle.getString("btnSecurityDelete"));
        lblInsuranceNo_Display.setText(resourceBundle.getString("lblInsuranceNo_Display"));
        lblInsuranceNo_Disp.setText(resourceBundle.getString("lblInsuranceNo_Disp"));
        lblNetWeight.setText(resourceBundle.getString("lblNetWeight"));
        lblGrossWeight.setText(resourceBundle.getString("lblGrossWeight"));
        lblPremiumAmt.setText(resourceBundle.getString("lblPremiumAmt"));
        lblInsureCompany.setText(resourceBundle.getString("lblInsureCompany"));
        lblForMillIndus.setText(resourceBundle.getString("lblForMillIndus"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblSecurityType.setText(resourceBundle.getString("lblSecurityType"));
        lblDateInspection.setText(resourceBundle.getString("lblDateInspection"));
        lblPolicyNumber.setText(resourceBundle.getString("lblPolicyNumber"));
        rdoSecurityType_Primary.setText(resourceBundle.getString("rdoSecurityType_Primary"));
        lblNatureCharge.setText(resourceBundle.getString("lblNatureCharge"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblCustID.setText(resourceBundle.getString("lblCustID"));
        lblCustID_Disp.setText(resourceBundle.getString("lblCustID_Disp"));
        lblCustName.setText(resourceBundle.getString("lblCustName"));
        lblCustName_Disp.setText(resourceBundle.getString("lblCustName_Disp"));
        lblCustEmail_ID.setText(resourceBundle.getString("lblCustEmail_ID"));
        lblCustEmail_ID_Disp.setText(resourceBundle.getString("lblCustEmail_ID_Disp"));
        lblCustCity.setText(resourceBundle.getString("lblCustCity"));
        lblCustCity_Disp.setText(resourceBundle.getString("lblCustCity_Disp"));
        lblCustStreet.setText(resourceBundle.getString("lblCustStreet"));
        lblCustStreet_Disp.setText(resourceBundle.getString("lblCustStreet_Disp"));
        lblCustPin.setText(resourceBundle.getString("lblCustPin"));
        lblCustPin_Disp.setText(resourceBundle.getString("lblCustPin_Disp"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblPolicyAmt.setText(resourceBundle.getString("lblPolicyAmt"));
        btnSecurityNew.setText(resourceBundle.getString("btnSecurityNew"));
        lblPolicyDate.setText(resourceBundle.getString("lblPolicyDate"));
        ((javax.swing.border.TitledBorder)panSecurityDetails_Cust.getBorder()).setTitle(resourceBundle.getString("panSecurityDetails_Cust"));
        //        ((javax.swing.border.TitledBorder)panSecurityDetails.getBorder()).setTitle(resourceBundle.getString("panSecurityDetails"));
        ((javax.swing.border.TitledBorder)panInsuranceDetails.getBorder()).setTitle(resourceBundle.getString("panInsuranceDetails"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblSecurityNo.setText(resourceBundle.getString("lblSecurityNo"));
        lblFromDate.setText(resourceBundle.getString("lblFromDate"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblSecurityValue.setText(resourceBundle.getString("lblSecurityValue"));
        lblDateCharge.setText(resourceBundle.getString("lblDateCharge"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblAvalSecVal.setText(resourceBundle.getString("lblAvalSecVal"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnSecuritySave.setText(resourceBundle.getString("btnSecuritySave"));
        lblAson.setText(resourceBundle.getString("lblAson"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblSelCommodityItem.setText(resourceBundle.getString("lblSelCommodityItem"));
        chkSelCommodityItem.setText(resourceBundle.getString("chkSelCommodityItem"));
        lblStockStateFreq.setText(resourceBundle.getString("lblStockStateFreq"));
        lblExpityDate.setText(resourceBundle.getString("lblExpityDate"));
        lblSecurityCate.setText(resourceBundle.getString("lblSecurityCate"));
        lblNatureRisk.setText(resourceBundle.getString("lblNatureRisk"));
        rdoSecurityType_Collateral.setText(resourceBundle.getString("rdoSecurityType_Collateral"));
        lblToDate.setText(resourceBundle.getString("lblToDate"));
        lblParticulars.setText(resourceBundle.getString("lblParticulars"));
        lblTotalSecurity_Value.setText(resourceBundle.getString("lblTotalSecurity_Value"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblSecurityNo_Insurance.setText(resourceBundle.getString("lblSecurityNo_Insurance"));
        btnNew_Insurance.setText(resourceBundle.getString("btnNew_Insurance"));
        btnSave_Insurance.setText(resourceBundle.getString("btnSave_Insurance"));
        btnDelete_Insurance.setText(resourceBundle.getString("btnDelete_Insurance"));
        lblRemark_Insurance.setText(resourceBundle.getString("lblRemark_Insurance"));
    }
    
    
    private void removeSecurityRadioBtns(){
        rdoSecurityType.remove(rdoSecurityType_Primary);
        rdoSecurityType.remove(rdoSecurityType_Collateral);
    }
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        removeSecurityRadioBtns();
        txtSecurityNo.setText(observableSecurity.getTxtSecurityNo());
        cboSecurityCate.setSelectedItem(observableSecurity.getCboSecurityCate());
        rdoSecurityType_Primary.setSelected(observableSecurity.getRdoSecurityType_Primary());
        rdoSecurityType_Collateral.setSelected(observableSecurity.getRdoSecurityType_Collateral());
        txtSecurityValue.setText(observableSecurity.getTxtSecurityValue());
        tdtAson.setDateValue(observableSecurity.getTdtAson());
        txtAreaParticular.setText(observableSecurity.getTxtParticulars());
        cboNatureCharge.setSelectedItem(observableSecurity.getCboNatureCharge());
        tdtDateCharge.setDateValue(observableSecurity.getTdtDateCharge());
        chkSelCommodityItem.setSelected(observableSecurity.getChkSelCommodityItem());
        cboForMillIndus.setSelectedItem(observableSecurity.getCboForMillIndus());
        tdtDateInspection.setDateValue(observableSecurity.getTdtDateInspection());
        cboStockStateFreq.setSelectedItem(observableSecurity.getCboStockStateFreq());
        tdtFromDate.setDateValue(observableSecurity.getTdtFromDate());
        tdtToDate.setDateValue(observableSecurity.getTdtToDate());
        txtAvalSecVal.setText(observableSecurity.getTxtAvalSecVal());
        txtTotalSecurity_Value.setText(observableSecurity.getTxtTotalSecurity_Value());
        txtNetWeight.setText(observableSecurity.getTxtWeight());
        txtGrossWeight.setText(observableSecurity.getTxtGrossWeight());
        cboInsuranceNo.setSelectedItem(observableSecurity.getCboInsuranceNo());
        cboSecurityNo_Insurance.setSelectedItem(observableInsurance.getCboSecurityNo_Insurance());
        txtRemark_Insurance.setText(observableInsurance.getTxtRemark_Insurance());
        tblSecurityTable.setModel(observableSecurity.getTblSecurityTab());
        tblInsurance.setModel(observableInsurance.getTblInsuranceTab());
        tblLoanTable.setModel(observableSecurity.getTblSecurityLoanTab());
        txtInsuranceNo.setText(observableInsurance.getTxtInsuranceNo());
        txtInsureCompany.setText(observableInsurance.getTxtInsureCompany());
        txtPolicyNumber.setText(observableInsurance.getTxtPolicyNumber());
        txtPolicyAmt.setText(observableInsurance.getTxtPolicyAmt());
        tdtPolicyDate.setDateValue(observableInsurance.getTdtPolicyDate());
        txtPremiumAmt.setText(observableInsurance.getTxtPremiumAmt());
        tdtExpityDate.setDateValue(observableInsurance.getTdtExpityDate());
        cboNatureRisk.setSelectedItem(observableInsurance.getCboNatureRisk());
        lblStatus.setText(observableSecurity.getLblStatus());
        lblCustCity_Disp.setText(observableSecurity.getLblCustCity_Disp());
        lblCustID_Disp.setText(observableSecurity.getLblCustID_Disp());
        lblCustName_Disp.setText(observableSecurity.getLblCustName_Disp());
        lblCustEmail_ID_Disp.setText(observableSecurity.getLblCustEmail_ID_Disp());
        lblCustPin_Disp.setText(observableSecurity.getLblCustPin_Disp());
        lblCustStreet_Disp.setText(observableSecurity.getLblCustStreet_Disp());
        addSecurityRadioBtns();
    }
    
    private void addSecurityRadioBtns(){
        rdoSecurityType = new CButtonGroup();
        rdoSecurityType.add(rdoSecurityType_Primary);
        rdoSecurityType.add(rdoSecurityType_Collateral);
    }
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observableInsurance.setSelectedBranchID(getSelectedBranchID());
        observableSecurity.setSelectedBranchID(getSelectedBranchID());
        observableSecurity.setTxtSecurityNo(txtSecurityNo.getText());
        observableSecurity.setCboSecurityCate(CommonUtil.convertObjToStr(cboSecurityCate.getSelectedItem()));
        observableSecurity.setRdoSecurityType_Primary(rdoSecurityType_Primary.isSelected());
        observableSecurity.setRdoSecurityType_Collateral(rdoSecurityType_Collateral.isSelected());
        observableSecurity.setTxtSecurityValue(txtSecurityValue.getText());
        observableSecurity.setTdtAson(tdtAson.getDateValue());
        observableSecurity.setTxtParticulars(txtAreaParticular.getText());
        observableSecurity.setCboNatureCharge(CommonUtil.convertObjToStr(cboNatureCharge.getSelectedItem()));
        observableSecurity.setTdtDateCharge(tdtDateCharge.getDateValue());
        observableSecurity.setChkSelCommodityItem(chkSelCommodityItem.isSelected());
        observableSecurity.setCboForMillIndus(CommonUtil.convertObjToStr(cboForMillIndus.getSelectedItem()));
        observableSecurity.setTdtDateInspection(tdtDateInspection.getDateValue());
        observableSecurity.setTxtTotalSecurity_Value(txtTotalSecurity_Value.getText());
        observableSecurity.setCboStockStateFreq(CommonUtil.convertObjToStr(cboStockStateFreq.getSelectedItem()));
        observableSecurity.setTdtFromDate(tdtFromDate.getDateValue());
        observableSecurity.setTdtToDate(tdtToDate.getDateValue());
        observableSecurity.setTxtAvalSecVal(txtAvalSecVal.getText());
        observableSecurity.setLblCustCity_Disp(lblCustCity_Disp.getText());
        observableSecurity.setLblCustID_Disp(lblCustID_Disp.getText());
        observableSecurity.setLblCustName_Disp(lblCustName_Disp.getText());
        observableSecurity.setLblCustEmail_ID_Disp(lblCustEmail_ID_Disp.getText());
        observableSecurity.setLblCustPin_Disp(lblCustPin_Disp.getText());
        observableSecurity.setLblCustStreet_Disp(lblCustStreet_Disp.getText());
        observableSecurity.setTxtWeight(txtNetWeight.getText());
        observableSecurity.setTxtGrossWeight(txtGrossWeight.getText());
        if (tblLoanTable != null && tblLoanTable.getRowCount() > 0){
            observableSecurity.setTblSecurityLoanTab((com.see.truetransact.clientutil.EnhancedTableModel) tblLoanTable.getModel());
        }
        observableSecurity.setCboInsuranceNo(CommonUtil.convertObjToStr(cboInsuranceNo.getSelectedItem()));
        observableInsurance.setTxtInsuranceNo(txtInsuranceNo.getText());
        observableInsurance.setTxtInsureCompany(txtInsureCompany.getText());
        observableInsurance.setTxtPolicyNumber(txtPolicyNumber.getText());
        observableInsurance.setTxtPolicyAmt(txtPolicyAmt.getText());
        observableInsurance.setTdtPolicyDate(tdtPolicyDate.getDateValue());
        observableInsurance.setTxtPremiumAmt(txtPremiumAmt.getText());
        observableInsurance.setTdtExpityDate(tdtExpityDate.getDateValue());
        observableInsurance.setCboNatureRisk(CommonUtil.convertObjToStr(cboNatureRisk.getSelectedItem()));
        observableInsurance.setCboSecurityNo_Insurance(CommonUtil.convertObjToStr(cboSecurityNo_Insurance.getSelectedItem()));
        observableInsurance.setTxtRemark_Insurance(txtRemark_Insurance.getText());
    }
    
    
/* Auto Generated Method - setMandatoryHashMap()
 
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtSecurityNo", new Boolean(true));
        mandatoryMap.put("txtWeight", new Boolean(true));
        mandatoryMap.put("cboSecurityCate", new Boolean(true));
        mandatoryMap.put("rdoSecurityType_Primary", new Boolean(true));
        mandatoryMap.put("txtSecurityValue", new Boolean(true));
        mandatoryMap.put("tdtAson", new Boolean(true));
        mandatoryMap.put("txtParticulars", new Boolean(true));
        mandatoryMap.put("tdtFromDate", new Boolean(true));
        mandatoryMap.put("tdtToDate", new Boolean(true));
        mandatoryMap.put("cboNatureCharge", new Boolean(true));
        mandatoryMap.put("tdtDateCharge", new Boolean(true));
        mandatoryMap.put("chkSelCommodityItem", new Boolean(true));
        mandatoryMap.put("cboForMillIndus", new Boolean(true));
        mandatoryMap.put("tdtDateInspection", new Boolean(true));
        mandatoryMap.put("cboStockStateFreq", new Boolean(true));
        mandatoryMap.put("txtAvalSecVal", new Boolean(true));
        mandatoryMap.put("txtTotalSecurity_Value", new Boolean(true));
        mandatoryMap.put("txtInsureCompany", new Boolean(true));
        mandatoryMap.put("txtPolicyNumber", new Boolean(true));
        mandatoryMap.put("txtPolicyAmt", new Boolean(true));
        mandatoryMap.put("tdtPolicyDate", new Boolean(true));
        mandatoryMap.put("cboSecurityNo_Insurance", new Boolean(true));
        mandatoryMap.put("txtPremiumAmt", new Boolean(true));
        mandatoryMap.put("tdtExpityDate", new Boolean(true));
        mandatoryMap.put("cboNatureRisk", new Boolean(true));
        mandatoryMap.put("txtRemark_Insurance", new Boolean(true));
        mandatoryMap.put("cboInsuranceNo", new Boolean(true));
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
        SecurityInsuranceMRB objMandatoryRB = new SecurityInsuranceMRB();
        cboInsuranceNo.setHelpMessage(lblMsg, objMandatoryRB.getString("cboInsuranceNo"));
        txtSecurityNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSecurityNo"));
        txtNetWeight.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNetWeight"));
        cboSecurityCate.setHelpMessage(lblMsg, objMandatoryRB.getString("cboSecurityCate"));
        rdoSecurityType_Primary.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoSecurityType_Primary"));
        txtSecurityValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSecurityValue"));
        tdtAson.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtAson"));
        txtAreaParticular.setHelpMessage(lblMsg, objMandatoryRB.getString("txtParticulars"));
        tdtFromDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtFromDate"));
        tdtToDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtToDate"));
        cboNatureCharge.setHelpMessage(lblMsg, objMandatoryRB.getString("cboNatureCharge"));
        tdtDateCharge.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDateCharge"));
        chkSelCommodityItem.setHelpMessage(lblMsg, objMandatoryRB.getString("chkSelCommodityItem"));
        cboForMillIndus.setHelpMessage(lblMsg, objMandatoryRB.getString("cboForMillIndus"));
        tdtDateInspection.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDateInspection"));
        cboStockStateFreq.setHelpMessage(lblMsg, objMandatoryRB.getString("cboStockStateFreq"));
        txtAvalSecVal.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAvalSecVal"));
        txtTotalSecurity_Value.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTotalSecurity_Value"));
        txtInsureCompany.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInsureCompany"));
        txtPolicyNumber.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPolicyNumber"));
        txtPolicyAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPolicyAmt"));
        tdtPolicyDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtPolicyDate"));
        cboSecurityNo_Insurance.setHelpMessage(lblMsg, objMandatoryRB.getString("cboSecurityNo_Insurance"));
        txtPremiumAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPremiumAmt"));
        tdtExpityDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtExpityDate"));
        cboNatureRisk.setHelpMessage(lblMsg, objMandatoryRB.getString("cboNatureRisk"));
        txtRemark_Insurance.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRemark_Insurance"));
    }
    
    private void setMaxLength(){
        txtInsuranceNo.setValidation(new NumericValidation(16, 0));
        txtSecurityNo.setMaxLength(16);
        txtSecurityNo.setValidation(new NumericValidation(16, 0));
        txtSecurityValue.setMaxLength(16);
        txtSecurityValue.setValidation(new CurrencyValidation(14,2));
        txtAreaParticular.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                // Add your handling code here:
                    /*if( uiComponentValidation != null ){
                        uiComponentValidation.validateEvent(evt);
                    }*/
                int length = txtAreaParticular.getText().length();
                if( length >= 250 ){
                    if( checkInValidCharacter(evt.getKeyChar())){
                        evt.consume();
                    }
                }
            }
        }
        );
        //        txtParticulars.setMaxLength(128);
        //        txtParticulars.setBounds(150,150,150,150);
        txtAvalSecVal.setMaxLength(16);
        txtAvalSecVal.setValidation(new CurrencyValidation(14,2));
        txtInsureCompany.setMaxLength(64);
        txtPolicyAmt.setMaxLength(16);
        txtPolicyAmt.setValidation(new CurrencyValidation(14,2));
        txtPremiumAmt.setMaxLength(16);
        txtPremiumAmt.setValidation(new CurrencyValidation(14,2));
        txtTotalSecurity_Value.setValidation(new CurrencyValidation());
        txtPolicyNumber.setMaxLength(32);
        txtPolicyNumber.setAllowNumber(true);
        txtNetWeight.setValidation(new NumericValidation(4, 6));
        txtGrossWeight.setValidation(new NumericValidation(4, 6));
        txtRemark_Insurance.setMaxLength(128);
    }
    
    private boolean checkInValidCharacter(char keyChar){
        if (!((keyChar == java.awt.event.KeyEvent.VK_BACK_SPACE) ||
        (keyChar == java.awt.event.KeyEvent.VK_DELETE))) {
            return true;
        }
        return false;
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
        btnView.setEnabled(!btnView.isEnabled());
        
        setAuthBtnEnableDisable();
    }
    
    /** To Enable or Disable Authorize, Rejection and Exception Button */
    private void setAuthBtnEnableDisable(){
        final boolean enableDisable = !btnSave.isEnabled();
        btnAuthorize.setEnabled(enableDisable);
        btnException.setEnabled(enableDisable);
        btnReject.setEnabled(enableDisable);
        mitAuthorize.setEnabled(enableDisable);
        mitException.setEnabled(enableDisable);
        mitReject.setEnabled(enableDisable);
    }
    
    private void initComponentData() {
        cboStockStateFreq.setModel(observableSecurity.getCbmStockStateFreq());
        cboSecurityCate.setModel(observableSecurity.getCbmSecurityCate());
        cboNatureCharge.setModel(observableSecurity.getCbmNatureCharge());
        cboForMillIndus.setModel(observableSecurity.getCbmForMillIndus());
        cboInsuranceNo.setModel(observableSecurity.getCbmInsuranceNo());
        cboNatureRisk.setModel(observableInsurance.getCbmNatureRisk());
        cboSecurityNo_Insurance.setModel(observableInsurance.getCbmSecurityNo_Insurance());
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoSecurityType = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrSecInsur = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblspace4 = new com.see.truetransact.uicomponent.CLabel();
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
        lblspace3 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace31 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        PanSecurityDetails = new com.see.truetransact.uicomponent.CPanel();
        panSecurityDetails = new com.see.truetransact.uicomponent.CPanel();
        tabSecurityDetails = new com.see.truetransact.uicomponent.CTabbedPane();
        panSecurityDetails_security = new com.see.truetransact.uicomponent.CPanel();
        panSecDetails = new com.see.truetransact.uicomponent.CPanel();
        lblSecurityNo = new com.see.truetransact.uicomponent.CLabel();
        txtSecurityNo = new com.see.truetransact.uicomponent.CTextField();
        lblSecurityCate = new com.see.truetransact.uicomponent.CLabel();
        cboSecurityCate = new com.see.truetransact.uicomponent.CComboBox();
        lblSecurityValue = new com.see.truetransact.uicomponent.CLabel();
        txtSecurityValue = new com.see.truetransact.uicomponent.CTextField();
        lblAson = new com.see.truetransact.uicomponent.CLabel();
        tdtAson = new com.see.truetransact.uicomponent.CDateField();
        lblFromDate = new com.see.truetransact.uicomponent.CLabel();
        tdtFromDate = new com.see.truetransact.uicomponent.CDateField();
        lblToDate = new com.see.truetransact.uicomponent.CLabel();
        tdtToDate = new com.see.truetransact.uicomponent.CDateField();
        lblSecurityType = new com.see.truetransact.uicomponent.CLabel();
        panSecurityType = new com.see.truetransact.uicomponent.CPanel();
        rdoSecurityType_Primary = new com.see.truetransact.uicomponent.CRadioButton();
        rdoSecurityType_Collateral = new com.see.truetransact.uicomponent.CRadioButton();
        txtGrossWeight = new com.see.truetransact.uicomponent.CTextField();
        lblGrossWeight = new com.see.truetransact.uicomponent.CLabel();
        panSecNature = new com.see.truetransact.uicomponent.CPanel();
        lblNatureCharge = new com.see.truetransact.uicomponent.CLabel();
        cboNatureCharge = new com.see.truetransact.uicomponent.CComboBox();
        lblDateCharge = new com.see.truetransact.uicomponent.CLabel();
        tdtDateCharge = new com.see.truetransact.uicomponent.CDateField();
        lblSelCommodityItem = new com.see.truetransact.uicomponent.CLabel();
        chkSelCommodityItem = new com.see.truetransact.uicomponent.CCheckBox();
        lblForMillIndus = new com.see.truetransact.uicomponent.CLabel();
        cboForMillIndus = new com.see.truetransact.uicomponent.CComboBox();
        lblDateInspection = new com.see.truetransact.uicomponent.CLabel();
        tdtDateInspection = new com.see.truetransact.uicomponent.CDateField();
        lblStockStateFreq = new com.see.truetransact.uicomponent.CLabel();
        cboStockStateFreq = new com.see.truetransact.uicomponent.CComboBox();
        lblNetWeight = new com.see.truetransact.uicomponent.CLabel();
        txtNetWeight = new com.see.truetransact.uicomponent.CTextField();
        lblAvalSecVal = new com.see.truetransact.uicomponent.CLabel();
        txtAvalSecVal = new com.see.truetransact.uicomponent.CTextField();
        lblInsuranceNo_Disp = new com.see.truetransact.uicomponent.CLabel();
        cboInsuranceNo = new com.see.truetransact.uicomponent.CComboBox();
        sptSecurityDetails_Vert = new com.see.truetransact.uicomponent.CSeparator();
        panSecurityTools = new com.see.truetransact.uicomponent.CPanel();
        btnSecurityNew = new com.see.truetransact.uicomponent.CButton();
        btnSecuritySave = new com.see.truetransact.uicomponent.CButton();
        btnSecurityDelete = new com.see.truetransact.uicomponent.CButton();
        panparticulars = new com.see.truetransact.uicomponent.CPanel();
        lblParticulars = new com.see.truetransact.uicomponent.CLabel();
        srpTxtAreaParticulars = new com.see.truetransact.uicomponent.CScrollPane();
        txtAreaParticular = new com.see.truetransact.uicomponent.CTextArea();
        panLoanAvailed = new com.see.truetransact.uicomponent.CPanel();
        panLoanTable = new com.see.truetransact.uicomponent.CPanel();
        srpLoanTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblLoanTable = new com.see.truetransact.uicomponent.CTable();
        panSecurityTableMain = new com.see.truetransact.uicomponent.CPanel();
        panSecurityTable = new com.see.truetransact.uicomponent.CPanel();
        srpSecurityTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblSecurityTable = new com.see.truetransact.uicomponent.CTable();
        panTotalSecurity_Value = new com.see.truetransact.uicomponent.CPanel();
        lblTotalSecurity_Value = new com.see.truetransact.uicomponent.CLabel();
        txtTotalSecurity_Value = new com.see.truetransact.uicomponent.CTextField();
        panInsuranceDetails = new com.see.truetransact.uicomponent.CPanel();
        panInsureDetails_details = new com.see.truetransact.uicomponent.CPanel();
        panPolicy = new com.see.truetransact.uicomponent.CPanel();
        lblInsureCompany = new com.see.truetransact.uicomponent.CLabel();
        txtInsureCompany = new com.see.truetransact.uicomponent.CTextField();
        lblPolicyNumber = new com.see.truetransact.uicomponent.CLabel();
        txtPolicyNumber = new com.see.truetransact.uicomponent.CTextField();
        lblPolicyAmt = new com.see.truetransact.uicomponent.CLabel();
        txtPolicyAmt = new com.see.truetransact.uicomponent.CTextField();
        lblPolicyDate = new com.see.truetransact.uicomponent.CLabel();
        tdtPolicyDate = new com.see.truetransact.uicomponent.CDateField();
        lblInsuranceNo_Display = new com.see.truetransact.uicomponent.CLabel();
        txtInsuranceNo = new com.see.truetransact.uicomponent.CTextField();
        sptInsureDetails_Vert = new com.see.truetransact.uicomponent.CSeparator();
        panPolicyNature = new com.see.truetransact.uicomponent.CPanel();
        lblPremiumAmt = new com.see.truetransact.uicomponent.CLabel();
        txtPremiumAmt = new com.see.truetransact.uicomponent.CTextField();
        lblExpityDate = new com.see.truetransact.uicomponent.CLabel();
        tdtExpityDate = new com.see.truetransact.uicomponent.CDateField();
        lblNatureRisk = new com.see.truetransact.uicomponent.CLabel();
        cboNatureRisk = new com.see.truetransact.uicomponent.CComboBox();
        txtRemark_Insurance = new com.see.truetransact.uicomponent.CTextField();
        lblRemark_Insurance = new com.see.truetransact.uicomponent.CLabel();
        lblSecurityNo_Insurance = new com.see.truetransact.uicomponent.CLabel();
        cboSecurityNo_Insurance = new com.see.truetransact.uicomponent.CComboBox();
        panInsuranceToolBars = new com.see.truetransact.uicomponent.CPanel();
        btnNew_Insurance = new com.see.truetransact.uicomponent.CButton();
        btnSave_Insurance = new com.see.truetransact.uicomponent.CButton();
        btnDelete_Insurance = new com.see.truetransact.uicomponent.CButton();
        panInsuranceCTable = new com.see.truetransact.uicomponent.CPanel();
        srpInsuranceCTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblInsurance = new com.see.truetransact.uicomponent.CTable();
        panSecurityDetails_Cust = new com.see.truetransact.uicomponent.CPanel();
        panSecurity = new com.see.truetransact.uicomponent.CPanel();
        lblCustID = new com.see.truetransact.uicomponent.CLabel();
        lblCustID_Disp = new com.see.truetransact.uicomponent.CLabel();
        lblCustName = new com.see.truetransact.uicomponent.CLabel();
        lblCustName_Disp = new com.see.truetransact.uicomponent.CLabel();
        lblCustEmail_ID = new com.see.truetransact.uicomponent.CLabel();
        lblCustEmail_ID_Disp = new com.see.truetransact.uicomponent.CLabel();
        panSecurityNature = new com.see.truetransact.uicomponent.CPanel();
        lblCustStreet = new com.see.truetransact.uicomponent.CLabel();
        lblCustStreet_Disp = new com.see.truetransact.uicomponent.CLabel();
        lblCustPin = new com.see.truetransact.uicomponent.CLabel();
        lblCustPin_Disp = new com.see.truetransact.uicomponent.CLabel();
        lblCustCity = new com.see.truetransact.uicomponent.CLabel();
        lblCustCity_Disp = new com.see.truetransact.uicomponent.CLabel();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrSecInsur = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptProcess = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitAuthorize = new javax.swing.JMenuItem();
        mitException = new javax.swing.JMenuItem();
        mitReject = new javax.swing.JMenuItem();
        sptException = new javax.swing.JSeparator();
        sptPrint = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMaximumSize(new java.awt.Dimension(850, 675));
        setMinimumSize(new java.awt.Dimension(850, 675));
        setPreferredSize(new java.awt.Dimension(850, 675));

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
        tbrSecInsur.add(btnView);

        lblspace4.setMaximumSize(new java.awt.Dimension(15, 15));
        lblspace4.setMinimumSize(new java.awt.Dimension(15, 15));
        lblspace4.setPreferredSize(new java.awt.Dimension(15, 15));
        tbrSecInsur.add(lblspace4);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrSecInsur.add(btnNew);

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSecInsur.add(lblSpace26);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrSecInsur.add(btnEdit);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSecInsur.add(lblSpace27);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrSecInsur.add(btnDelete);

        lblSpace2.setText("     ");
        tbrSecInsur.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrSecInsur.add(btnSave);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSecInsur.add(lblSpace28);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrSecInsur.add(btnCancel);

        lblSpace3.setText("     ");
        tbrSecInsur.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setMaximumSize(new java.awt.Dimension(29, 27));
        btnAuthorize.setMinimumSize(new java.awt.Dimension(29, 27));
        btnAuthorize.setPreferredSize(new java.awt.Dimension(29, 27));
        btnAuthorize.setEnabled(false);
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrSecInsur.add(btnAuthorize);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSecInsur.add(lblSpace29);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setMaximumSize(new java.awt.Dimension(29, 27));
        btnException.setMinimumSize(new java.awt.Dimension(29, 27));
        btnException.setPreferredSize(new java.awt.Dimension(29, 27));
        btnException.setEnabled(false);
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrSecInsur.add(btnException);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSecInsur.add(lblSpace30);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setMaximumSize(new java.awt.Dimension(29, 27));
        btnReject.setMinimumSize(new java.awt.Dimension(29, 27));
        btnReject.setPreferredSize(new java.awt.Dimension(29, 27));
        btnReject.setEnabled(false);
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrSecInsur.add(btnReject);

        lblspace3.setMaximumSize(new java.awt.Dimension(15, 15));
        lblspace3.setMinimumSize(new java.awt.Dimension(15, 15));
        lblspace3.setPreferredSize(new java.awt.Dimension(15, 15));
        tbrSecInsur.add(lblspace3);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrSecInsur.add(btnPrint);

        lblSpace31.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace31.setText("     ");
        lblSpace31.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSecInsur.add(lblSpace31);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrSecInsur.add(btnClose);

        getContentPane().add(tbrSecInsur, java.awt.BorderLayout.NORTH);

        PanSecurityDetails.setMinimumSize(new java.awt.Dimension(830, 650));
        PanSecurityDetails.setPreferredSize(new java.awt.Dimension(830, 650));
        PanSecurityDetails.setLayout(new java.awt.GridBagLayout());

        panSecurityDetails.setMinimumSize(new java.awt.Dimension(825, 335));
        panSecurityDetails.setPreferredSize(new java.awt.Dimension(825, 335));
        panSecurityDetails.setLayout(new java.awt.GridBagLayout());

        tabSecurityDetails.setMinimumSize(new java.awt.Dimension(513, 325));
        tabSecurityDetails.setPreferredSize(new java.awt.Dimension(513, 325));

        panSecurityDetails_security.setMinimumSize(new java.awt.Dimension(508, 185));
        panSecurityDetails_security.setPreferredSize(new java.awt.Dimension(508, 185));
        panSecurityDetails_security.setLayout(new java.awt.GridBagLayout());

        panSecDetails.setMinimumSize(new java.awt.Dimension(240, 190));
        panSecDetails.setPreferredSize(new java.awt.Dimension(240, 190));
        panSecDetails.setLayout(new java.awt.GridBagLayout());

        lblSecurityNo.setText("Security No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panSecDetails.add(lblSecurityNo, gridBagConstraints);

        txtSecurityNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 1);
        panSecDetails.add(txtSecurityNo, gridBagConstraints);

        lblSecurityCate.setText("Security Category");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panSecDetails.add(lblSecurityCate, gridBagConstraints);

        cboSecurityCate.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboSecurityCate.setMinimumSize(new java.awt.Dimension(100, 21));
        cboSecurityCate.setPopupWidth(250);
        cboSecurityCate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSecurityCateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 1);
        panSecDetails.add(cboSecurityCate, gridBagConstraints);

        lblSecurityValue.setText("Security Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panSecDetails.add(lblSecurityValue, gridBagConstraints);

        txtSecurityValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSecurityValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSecurityValueFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 1);
        panSecDetails.add(txtSecurityValue, gridBagConstraints);

        lblAson.setText("As On");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panSecDetails.add(lblAson, gridBagConstraints);

        tdtAson.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtAson.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtAson.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtAsonFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 1);
        panSecDetails.add(tdtAson, gridBagConstraints);

        lblFromDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panSecDetails.add(lblFromDate, gridBagConstraints);

        tdtFromDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtFromDate.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtFromDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtFromDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 1);
        panSecDetails.add(tdtFromDate, gridBagConstraints);

        lblToDate.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panSecDetails.add(lblToDate, gridBagConstraints);

        tdtToDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtToDate.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtToDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtToDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 1);
        panSecDetails.add(tdtToDate, gridBagConstraints);

        lblSecurityType.setText("Security Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panSecDetails.add(lblSecurityType, gridBagConstraints);

        panSecurityType.setLayout(new java.awt.GridBagLayout());

        rdoSecurityType.add(rdoSecurityType_Primary);
        rdoSecurityType_Primary.setText("Primary");
        rdoSecurityType_Primary.setMinimumSize(new java.awt.Dimension(90, 21));
        rdoSecurityType_Primary.setPreferredSize(new java.awt.Dimension(90, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panSecurityType.add(rdoSecurityType_Primary, gridBagConstraints);

        rdoSecurityType.add(rdoSecurityType_Collateral);
        rdoSecurityType_Collateral.setText("Collateral");
        rdoSecurityType_Collateral.setMinimumSize(new java.awt.Dimension(90, 21));
        rdoSecurityType_Collateral.setPreferredSize(new java.awt.Dimension(90, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panSecurityType.add(rdoSecurityType_Collateral, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        panSecDetails.add(panSecurityType, gridBagConstraints);

        txtGrossWeight.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 1);
        panSecDetails.add(txtGrossWeight, gridBagConstraints);

        lblGrossWeight.setText("Gross Weight");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panSecDetails.add(lblGrossWeight, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panSecurityDetails_security.add(panSecDetails, gridBagConstraints);

        panSecNature.setMinimumSize(new java.awt.Dimension(250, 200));
        panSecNature.setPreferredSize(new java.awt.Dimension(250, 200));
        panSecNature.setLayout(new java.awt.GridBagLayout());

        lblNatureCharge.setText("Nature of Charge");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 2);
        panSecNature.add(lblNatureCharge, gridBagConstraints);

        cboNatureCharge.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboNatureCharge.setMinimumSize(new java.awt.Dimension(100, 21));
        cboNatureCharge.setPopupWidth(175);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 4);
        panSecNature.add(cboNatureCharge, gridBagConstraints);

        lblDateCharge.setText("Date of Charge");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 2);
        panSecNature.add(lblDateCharge, gridBagConstraints);

        tdtDateCharge.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtDateCharge.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 1, 4);
        panSecNature.add(tdtDateCharge, gridBagConstraints);

        lblSelCommodityItem.setText("Selective Commodity Item");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 2);
        panSecNature.add(lblSelCommodityItem, gridBagConstraints);

        chkSelCommodityItem.setMinimumSize(new java.awt.Dimension(87, 21));
        chkSelCommodityItem.setPreferredSize(new java.awt.Dimension(87, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 1, 4);
        panSecNature.add(chkSelCommodityItem, gridBagConstraints);

        lblForMillIndus.setText("For Mills / Industrial Users");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 2);
        panSecNature.add(lblForMillIndus, gridBagConstraints);

        cboForMillIndus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboForMillIndus.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 1, 4);
        panSecNature.add(cboForMillIndus, gridBagConstraints);

        lblDateInspection.setText("Date of Inspection");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 2);
        panSecNature.add(lblDateInspection, gridBagConstraints);

        tdtDateInspection.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 1, 4);
        panSecNature.add(tdtDateInspection, gridBagConstraints);

        lblStockStateFreq.setText("Stock Statement Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 2);
        panSecNature.add(lblStockStateFreq, gridBagConstraints);

        cboStockStateFreq.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboStockStateFreq.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 1, 4);
        panSecNature.add(cboStockStateFreq, gridBagConstraints);

        lblNetWeight.setText(" Net Weight");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 2);
        panSecNature.add(lblNetWeight, gridBagConstraints);

        txtNetWeight.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 4);
        panSecNature.add(txtNetWeight, gridBagConstraints);

        lblAvalSecVal.setText("Available Security Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 2);
        panSecNature.add(lblAvalSecVal, gridBagConstraints);

        txtAvalSecVal.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 1, 4);
        panSecNature.add(txtAvalSecVal, gridBagConstraints);

        lblInsuranceNo_Disp.setText("Insurance No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 2);
        panSecNature.add(lblInsuranceNo_Disp, gridBagConstraints);

        cboInsuranceNo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboInsuranceNo.setMaximumSize(new java.awt.Dimension(100, 21));
        cboInsuranceNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 1, 4);
        panSecNature.add(cboInsuranceNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 4);
        panSecurityDetails_security.add(panSecNature, gridBagConstraints);

        sptSecurityDetails_Vert.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panSecurityDetails_security.add(sptSecurityDetails_Vert, gridBagConstraints);

        panSecurityTools.setMinimumSize(new java.awt.Dimension(217, 30));
        panSecurityTools.setPreferredSize(new java.awt.Dimension(217, 30));
        panSecurityTools.setLayout(new java.awt.GridBagLayout());

        btnSecurityNew.setText("New");
        btnSecurityNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSecurityNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSecurityTools.add(btnSecurityNew, gridBagConstraints);

        btnSecuritySave.setText("Save");
        btnSecuritySave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSecuritySaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSecurityTools.add(btnSecuritySave, gridBagConstraints);

        btnSecurityDelete.setText("Delete");
        btnSecurityDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSecurityDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSecurityTools.add(btnSecurityDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panSecurityDetails_security.add(panSecurityTools, gridBagConstraints);

        panparticulars.setMinimumSize(new java.awt.Dimension(500, 60));
        panparticulars.setPreferredSize(new java.awt.Dimension(500, 60));
        panparticulars.setLayout(new java.awt.GridBagLayout());

        lblParticulars.setText("Particulars");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panparticulars.add(lblParticulars, gridBagConstraints);

        srpTxtAreaParticulars.setMinimumSize(new java.awt.Dimension(400, 60));
        srpTxtAreaParticulars.setPreferredSize(new java.awt.Dimension(400, 60));

        txtAreaParticular.setBorder(javax.swing.BorderFactory.createBevelBorder(1));
        txtAreaParticular.setLineWrap(true);
        srpTxtAreaParticulars.setViewportView(txtAreaParticular);

        panparticulars.add(srpTxtAreaParticulars, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        panSecurityDetails_security.add(panparticulars, gridBagConstraints);

        tabSecurityDetails.addTab("Security Details", panSecurityDetails_security);

        panLoanAvailed.setMinimumSize(new java.awt.Dimension(508, 200));
        panLoanAvailed.setPreferredSize(new java.awt.Dimension(508, 200));
        panLoanAvailed.setLayout(new java.awt.GridBagLayout());

        panLoanTable.setMinimumSize(new java.awt.Dimension(508, 220));
        panLoanTable.setPreferredSize(new java.awt.Dimension(508, 220));
        panLoanTable.setLayout(new java.awt.GridBagLayout());

        srpLoanTable.setMinimumSize(new java.awt.Dimension(508, 220));
        srpLoanTable.setPreferredSize(new java.awt.Dimension(508, 220));

        tblLoanTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        srpLoanTable.setViewportView(tblLoanTable);

        panLoanTable.add(srpLoanTable, new java.awt.GridBagConstraints());

        panLoanAvailed.add(panLoanTable, new java.awt.GridBagConstraints());

        tabSecurityDetails.addTab("Loans Availed Against This Security.", panLoanAvailed);

        panSecurityDetails.add(tabSecurityDetails, new java.awt.GridBagConstraints());

        panSecurityTableMain.setMinimumSize(new java.awt.Dimension(278, 155));
        panSecurityTableMain.setPreferredSize(new java.awt.Dimension(278, 155));
        panSecurityTableMain.setLayout(new java.awt.GridBagLayout());

        panSecurityTable.setMinimumSize(new java.awt.Dimension(270, 205));
        panSecurityTable.setPreferredSize(new java.awt.Dimension(270, 225));
        panSecurityTable.setLayout(new java.awt.GridBagLayout());

        srpSecurityTable.setMinimumSize(new java.awt.Dimension(270, 185));
        srpSecurityTable.setPreferredSize(new java.awt.Dimension(270, 185));

        tblSecurityTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Security No.", "Value", "Category", "As on"
            }
        ));
        tblSecurityTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblSecurityTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblSecurityTableMousePressed(evt);
            }
        });
        srpSecurityTable.setViewportView(tblSecurityTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        panSecurityTable.add(srpSecurityTable, gridBagConstraints);

        panTotalSecurity_Value.setLayout(new java.awt.GridBagLayout());

        lblTotalSecurity_Value.setText("Total Security Value(%)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotalSecurity_Value.add(lblTotalSecurity_Value, gridBagConstraints);

        txtTotalSecurity_Value.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotalSecurity_Value.add(txtTotalSecurity_Value, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panSecurityTable.add(panTotalSecurity_Value, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSecurityTableMain.add(panSecurityTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panSecurityDetails.add(panSecurityTableMain, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        PanSecurityDetails.add(panSecurityDetails, gridBagConstraints);

        panInsuranceDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Insurance Details"));
        panInsuranceDetails.setMinimumSize(new java.awt.Dimension(825, 160));
        panInsuranceDetails.setPreferredSize(new java.awt.Dimension(825, 160));
        panInsuranceDetails.setLayout(new java.awt.GridBagLayout());

        panInsureDetails_details.setMinimumSize(new java.awt.Dimension(525, 113));
        panInsureDetails_details.setPreferredSize(new java.awt.Dimension(525, 113));
        panInsureDetails_details.setLayout(new java.awt.GridBagLayout());

        panPolicy.setMaximumSize(new java.awt.Dimension(240, 110));
        panPolicy.setMinimumSize(new java.awt.Dimension(240, 110));
        panPolicy.setPreferredSize(new java.awt.Dimension(240, 110));
        panPolicy.setLayout(new java.awt.GridBagLayout());

        lblInsureCompany.setText("Insurance Company");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panPolicy.add(lblInsureCompany, gridBagConstraints);

        txtInsureCompany.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panPolicy.add(txtInsureCompany, gridBagConstraints);

        lblPolicyNumber.setText("Policy Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panPolicy.add(lblPolicyNumber, gridBagConstraints);

        txtPolicyNumber.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panPolicy.add(txtPolicyNumber, gridBagConstraints);

        lblPolicyAmt.setText("Policy Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panPolicy.add(lblPolicyAmt, gridBagConstraints);

        txtPolicyAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPolicyAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPolicyAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panPolicy.add(txtPolicyAmt, gridBagConstraints);

        lblPolicyDate.setText("Policy Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panPolicy.add(lblPolicyDate, gridBagConstraints);

        tdtPolicyDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtPolicyDate.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtPolicyDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtPolicyDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panPolicy.add(tdtPolicyDate, gridBagConstraints);

        lblInsuranceNo_Display.setText("Insurance No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panPolicy.add(lblInsuranceNo_Display, gridBagConstraints);

        txtInsuranceNo.setMaximumSize(new java.awt.Dimension(100, 21));
        txtInsuranceNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panPolicy.add(txtInsuranceNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        panInsureDetails_details.add(panPolicy, gridBagConstraints);

        sptInsureDetails_Vert.setOrientation(javax.swing.SwingConstants.VERTICAL);
        sptInsureDetails_Vert.setMinimumSize(new java.awt.Dimension(3, 0));
        sptInsureDetails_Vert.setPreferredSize(new java.awt.Dimension(3, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panInsureDetails_details.add(sptInsureDetails_Vert, gridBagConstraints);

        panPolicyNature.setMaximumSize(new java.awt.Dimension(220, 110));
        panPolicyNature.setMinimumSize(new java.awt.Dimension(250, 110));
        panPolicyNature.setPreferredSize(new java.awt.Dimension(250, 110));
        panPolicyNature.setLayout(new java.awt.GridBagLayout());

        lblPremiumAmt.setText("Premium Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panPolicyNature.add(lblPremiumAmt, gridBagConstraints);

        txtPremiumAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPremiumAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPremiumAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panPolicyNature.add(txtPremiumAmt, gridBagConstraints);

        lblExpityDate.setText("Expiry Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panPolicyNature.add(lblExpityDate, gridBagConstraints);

        tdtExpityDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtExpityDate.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtExpityDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtExpityDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panPolicyNature.add(tdtExpityDate, gridBagConstraints);

        lblNatureRisk.setText("Nature of Risk Covered");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panPolicyNature.add(lblNatureRisk, gridBagConstraints);

        cboNatureRisk.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboNatureRisk.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panPolicyNature.add(cboNatureRisk, gridBagConstraints);

        txtRemark_Insurance.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panPolicyNature.add(txtRemark_Insurance, gridBagConstraints);

        lblRemark_Insurance.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panPolicyNature.add(lblRemark_Insurance, gridBagConstraints);

        lblSecurityNo_Insurance.setText("Sanction No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panPolicyNature.add(lblSecurityNo_Insurance, gridBagConstraints);

        cboSecurityNo_Insurance.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboSecurityNo_Insurance.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panPolicyNature.add(cboSecurityNo_Insurance, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 4);
        panInsureDetails_details.add(panPolicyNature, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panInsuranceDetails.add(panInsureDetails_details, gridBagConstraints);

        panInsuranceToolBars.setMinimumSize(new java.awt.Dimension(215, 27));
        panInsuranceToolBars.setPreferredSize(new java.awt.Dimension(215, 27));
        panInsuranceToolBars.setLayout(new java.awt.GridBagLayout());

        btnNew_Insurance.setText("New");
        btnNew_Insurance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNew_InsuranceActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panInsuranceToolBars.add(btnNew_Insurance, gridBagConstraints);

        btnSave_Insurance.setText("Save");
        btnSave_Insurance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSave_InsuranceActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panInsuranceToolBars.add(btnSave_Insurance, gridBagConstraints);

        btnDelete_Insurance.setText("Delete");
        btnDelete_Insurance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelete_InsuranceActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panInsuranceToolBars.add(btnDelete_Insurance, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 6, 8);
        panInsuranceDetails.add(panInsuranceToolBars, gridBagConstraints);

        panInsuranceCTable.setMinimumSize(new java.awt.Dimension(275, 130));
        panInsuranceCTable.setPreferredSize(new java.awt.Dimension(275, 130));
        panInsuranceCTable.setLayout(new java.awt.GridBagLayout());

        srpInsuranceCTable.setMinimumSize(new java.awt.Dimension(270, 130));
        srpInsuranceCTable.setPreferredSize(new java.awt.Dimension(270, 130));

        tblInsurance.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3"
            }
        ));
        tblInsurance.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblInsuranceMousePressed(evt);
            }
        });
        srpInsuranceCTable.setViewportView(tblInsurance);

        panInsuranceCTable.add(srpInsuranceCTable, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panInsuranceDetails.add(panInsuranceCTable, gridBagConstraints);
        panInsuranceCTable.getAccessibleContext().setAccessibleName("Insurance Detail Table");
        panInsuranceCTable.getAccessibleContext().setAccessibleDescription("Insurance Detail Table");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        PanSecurityDetails.add(panInsuranceDetails, gridBagConstraints);

        panSecurityDetails_Cust.setBorder(javax.swing.BorderFactory.createTitledBorder("Customer Details"));
        panSecurityDetails_Cust.setMinimumSize(new java.awt.Dimension(825, 80));
        panSecurityDetails_Cust.setPreferredSize(new java.awt.Dimension(825, 80));
        panSecurityDetails_Cust.setLayout(new java.awt.GridBagLayout());

        panSecurity.setMaximumSize(new java.awt.Dimension(350, 55));
        panSecurity.setMinimumSize(new java.awt.Dimension(350, 55));
        panSecurity.setPreferredSize(new java.awt.Dimension(350, 55));
        panSecurity.setLayout(new java.awt.GridBagLayout());

        lblCustID.setText("Customer ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panSecurity.add(lblCustID, gridBagConstraints);

        lblCustID_Disp.setText("P0001");
        lblCustID_Disp.setMaximumSize(new java.awt.Dimension(125, 15));
        lblCustID_Disp.setMinimumSize(new java.awt.Dimension(125, 15));
        lblCustID_Disp.setPreferredSize(new java.awt.Dimension(125, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panSecurity.add(lblCustID_Disp, gridBagConstraints);

        lblCustName.setText("Customer Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panSecurity.add(lblCustName, gridBagConstraints);

        lblCustName_Disp.setForeground(new java.awt.Color(0, 51, 204));
        lblCustName_Disp.setText("CS");
        lblCustName_Disp.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblCustName_Disp.setMaximumSize(new java.awt.Dimension(275, 15));
        lblCustName_Disp.setMinimumSize(new java.awt.Dimension(275, 15));
        lblCustName_Disp.setPreferredSize(new java.awt.Dimension(275, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panSecurity.add(lblCustName_Disp, gridBagConstraints);

        lblCustEmail_ID.setText("Phone");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panSecurity.add(lblCustEmail_ID, gridBagConstraints);

        lblCustEmail_ID_Disp.setText("080-54545445");
        lblCustEmail_ID_Disp.setMaximumSize(new java.awt.Dimension(175, 15));
        lblCustEmail_ID_Disp.setMinimumSize(new java.awt.Dimension(175, 15));
        lblCustEmail_ID_Disp.setPreferredSize(new java.awt.Dimension(175, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panSecurity.add(lblCustEmail_ID_Disp, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 55, 0, 4);
        panSecurityDetails_Cust.add(panSecurity, gridBagConstraints);

        panSecurityNature.setMaximumSize(new java.awt.Dimension(275, 55));
        panSecurityNature.setMinimumSize(new java.awt.Dimension(275, 55));
        panSecurityNature.setPreferredSize(new java.awt.Dimension(275, 55));
        panSecurityNature.setLayout(new java.awt.GridBagLayout());

        lblCustStreet.setText("Street");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panSecurityNature.add(lblCustStreet, gridBagConstraints);

        lblCustStreet_Disp.setText("000000");
        lblCustStreet_Disp.setMaximumSize(new java.awt.Dimension(175, 15));
        lblCustStreet_Disp.setMinimumSize(new java.awt.Dimension(325, 15));
        lblCustStreet_Disp.setPreferredSize(new java.awt.Dimension(325, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 1);
        panSecurityNature.add(lblCustStreet_Disp, gridBagConstraints);

        lblCustPin.setText("PIN Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panSecurityNature.add(lblCustPin, gridBagConstraints);

        lblCustPin_Disp.setText("Jogupal");
        lblCustPin_Disp.setMaximumSize(new java.awt.Dimension(175, 15));
        lblCustPin_Disp.setMinimumSize(new java.awt.Dimension(175, 15));
        lblCustPin_Disp.setPreferredSize(new java.awt.Dimension(175, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panSecurityNature.add(lblCustPin_Disp, gridBagConstraints);

        lblCustCity.setText("City");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panSecurityNature.add(lblCustCity, gridBagConstraints);

        lblCustCity_Disp.setText("G Street");
        lblCustCity_Disp.setMaximumSize(new java.awt.Dimension(175, 15));
        lblCustCity_Disp.setMinimumSize(new java.awt.Dimension(175, 15));
        lblCustCity_Disp.setPreferredSize(new java.awt.Dimension(175, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panSecurityNature.add(lblCustCity_Disp, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panSecurityDetails_Cust.add(panSecurityNature, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        PanSecurityDetails.add(panSecurityDetails_Cust, gridBagConstraints);

        getContentPane().add(PanSecurityDetails, java.awt.BorderLayout.CENTER);

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
        mnuProcess.setMinimumSize(new java.awt.Dimension(73, 19));

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

        mitAuthorize.setText("Authorize");
        mitAuthorize.setEnabled(false);
        mitAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitAuthorizeActionPerformed(evt);
            }
        });
        mnuProcess.add(mitAuthorize);

        mitException.setText("Exception");
        mitException.setEnabled(false);
        mitException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitExceptionActionPerformed(evt);
            }
        });
        mnuProcess.add(mitException);

        mitReject.setText("Rejection");
        mitReject.setEnabled(false);
        mitReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitRejectActionPerformed(evt);
            }
        });
        mnuProcess.add(mitReject);
        mnuProcess.add(sptException);
        mnuProcess.add(sptPrint);

        mitPrint.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        mitPrint.setMnemonic('P');
        mitPrint.setText("Print");
        mitPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitPrintActionPerformed(evt);
            }
        });
        mnuProcess.add(mitPrint);

        mitClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        mitClose.setMnemonic('l');
        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrSecInsur.add(mnuProcess);

        setJMenuBar(mbrSecInsur);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observableSecurity.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observableSecurity.setStatus();
        lblStatus.setText(observableSecurity.getLblStatus());
        popUp("Enquiry");
        btnCheck();
        
    }//GEN-LAST:event_btnViewActionPerformed
    
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
        HashMap reportParamMap = new HashMap();
        com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed
    
    private void mitRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitRejectActionPerformed
        // TODO add your handling code here:
        btnRejectActionPerformed(evt);
    }//GEN-LAST:event_mitRejectActionPerformed
    
    private void mitExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitExceptionActionPerformed
        // TODO add your handling code here:
        btnExceptionActionPerformed(evt);
    }//GEN-LAST:event_mitExceptionActionPerformed
    
    private void mitAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitAuthorizeActionPerformed
        // TODO add your handling code here:
        btnAuthorizeActionPerformed(evt);
    }//GEN-LAST:event_mitAuthorizeActionPerformed
    
    private void txtPremiumAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPremiumAmtFocusLost
        // TODO add your handling code here:
        txtPremiumAmtFocusLost();
    }//GEN-LAST:event_txtPremiumAmtFocusLost
    private void txtPremiumAmtFocusLost() {
        // TODO add your handling code here:
        int policyAmt = CommonUtil.convertObjToInt(txtPolicyAmt.getText());
        int premiumAmt = CommonUtil.convertObjToInt(txtPremiumAmt.getText());
        if (txtPremiumAmt.getText().length() > 0 && (policyAmt <= premiumAmt || premiumAmt == 0)){
            txtPremiumAmt.setText("");
            observableInsurance.setTxtPremiumAmt("");
        }
    }
    private void txtPolicyAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPolicyAmtFocusLost
        // TODO add your handling code here:
        txtPolicyAmtFocusLost();
    }//GEN-LAST:event_txtPolicyAmtFocusLost
    private void txtPolicyAmtFocusLost() {
        // TODO add your handling code here:
        int policyAmt = CommonUtil.convertObjToInt(txtPolicyAmt.getText());
        int premiumAmt = CommonUtil.convertObjToInt(txtPremiumAmt.getText());
        if (txtPolicyAmt.getText().length() > 0 && (policyAmt <= premiumAmt || policyAmt == 0)){
            txtPolicyAmt.setText("");
            observableInsurance.setTxtPolicyAmt("");
        }
    }
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        mitCloseActionPerformed();
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void mitPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitPrintActionPerformed
        // TODO add your handling code here:
        mitPrintActionPerformed();
    }//GEN-LAST:event_mitPrintActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // TODO add your handling code here:
        mitCancelActionPerformed();
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // TODO add your handling code here:
        mitSaveActionPerformed();
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // TODO add your handling code here:
        mitDeleteActionPerformed();
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // TODO add your handling code here:
        mitEditActionPerformed();
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
        mitNewActionPerformed();
    }//GEN-LAST:event_mitNewActionPerformed
    private void mitCloseActionPerformed() {
        btnCloseActionPerformed(null);
    }    private void mitPrintActionPerformed() {
        // TODO add your handling code here:
        //        btnPrintActionPerformed();
    }    private void mitCancelActionPerformed() {
        // TODO add your handling code here:
        btnCancelActionPerformed(null);
    }    private void mitSaveActionPerformed() {
        // TODO add your handling code here:
        btnSaveActionPerformed(null);
    }    private void mitDeleteActionPerformed() {
        // TODO add your handling code here:
        btnDeleteActionPerformed(null);
    }    private void mitEditActionPerformed() {
        // TODO add your handling code here:
        btnEditActionPerformed(null);
    }    private void mitNewActionPerformed() {
        // TODO add your handling code here:
        btnNewActionPerformed(null);
    }
    private void cboSecurityCateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSecurityCateActionPerformed
        // TODO add your handling code here:
        cboSecurityCateActionPerformed();
    }//GEN-LAST:event_cboSecurityCateActionPerformed
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
    private void cboSecurityCateActionPerformed() {
        if (!((observableSecurity.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) || (viewType.equals(AUTHORIZE)))){
            if (chkForGoldSilverOrnaments()){
                txtNetWeight.setVisible(true);
                txtGrossWeight.setVisible(true);
                txtNetWeight.setEditable(true);
                txtNetWeight.setEnabled(true);
                txtGrossWeight.setEditable(true);
                txtGrossWeight.setEnabled(true);
                lblNetWeight.setVisible(true);
                lblGrossWeight.setVisible(true);
                
                
            }else{
                // Disable and clear the Weight field
                // When Security Category is other than Gold/Silver Ornaments
                txtNetWeight.setVisible(false);
                txtGrossWeight.setVisible(false);
                lblNetWeight.setVisible(false);
                lblGrossWeight.setVisible(false);
                txtNetWeight.setEditable(false);
                txtNetWeight.setEnabled(false);
                txtGrossWeight.setEditable(false);
                txtGrossWeight.setEnabled(false);
                
                observableSecurity.setTxtWeight("");
                txtNetWeight.setText(observableSecurity.getTxtWeight());
                observableSecurity.setTxtGrossWeight("");
                txtGrossWeight.setText(observableSecurity.getTxtGrossWeight());
                
            }
        }else if(chkForGoldSilverOrnaments()){
            txtNetWeight.setVisible(true);
            txtGrossWeight.setVisible(true);
            lblGrossWeight.setVisible(true);
            lblNetWeight.setVisible(true);
            txtNetWeight.setEditable(false);
            txtNetWeight.setEnabled(false);
            txtGrossWeight.setEditable(false);
            txtGrossWeight.setEnabled(false);
            
        }else if(!chkForGoldSilverOrnaments()){
            txtNetWeight.setVisible(false);
            txtGrossWeight.setVisible(false);
            lblGrossWeight.setVisible(false);
            lblNetWeight.setVisible(false);
            observableSecurity.setTxtWeight("");
            txtNetWeight.setText(observableSecurity.getTxtWeight());
            observableSecurity.setTxtGrossWeight("");
            txtGrossWeight.setText(observableSecurity.getTxtGrossWeight());
            
        }
    }
    private boolean chkForGoldSilverOrnaments(){
        boolean isWeightEnable = false;
        String strSecCateKey = CommonUtil.convertObjToStr(((ComboBoxModel)cboSecurityCate.getModel()).getKeyForSelected());
        if (strSecCateKey.equals(GOLD_SILVER_ORNAMENTS)){
            isWeightEnable = true;
        }
        strSecCateKey = null;
        return isWeightEnable;
    }
    private void txtSecurityValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSecurityValueFocusLost
        // TODO add your handling code here:
        txtSecurityValueFocusLost();
    }//GEN-LAST:event_txtSecurityValueFocusLost
    private void txtSecurityValueFocusLost() {
        //        if (rowSecurity == -1 ){   //&& updateSecurity == false
        //            txtAvalSecVal.setText(txtSecurityValue.getText());
        //        }
        
        String securityValue=txtSecurityValue.getText();
        String availSecurityValue=txtAvalSecVal.getText();
        StringBuffer stringBuffer=new StringBuffer();
        HashMap selectAvilMap=new HashMap();
        if(securityValue!=null && availSecurityValue !=null) {
            if(securityValue.equals(availSecurityValue))
                return;
            selectAvilMap.put("SECURITY_NO",txtSecurityNo.getText());
            selectAvilMap.put("CUST_ID",lblCustID_Disp.getText());
            java.util.List availList=ClientUtil.executeQuery("getCustSecurityDetails",selectAvilMap);
            if(availList !=null && availList.size()>0){
                for(int i=0;i<availList.size();i++){
                    HashMap getListMap=(HashMap)availList.get(i);
                    double dbSecurityValue=CommonUtil.convertObjToDouble(getListMap.get("SECURITY_VALUE")).doubleValue();
                    double dbAvailSecurityValue=CommonUtil.convertObjToDouble(getListMap.get("AVAILABLE_SECURITY_VALUE")).doubleValue();
                    double enterSecurityValue=CommonUtil.convertObjToDouble(securityValue).doubleValue();
                    if(dbSecurityValue<=enterSecurityValue){
                        double diffSecurity=enterSecurityValue-dbSecurityValue;
                        dbAvailSecurityValue+=diffSecurity;
                        txtAvalSecVal.setText(String.valueOf(dbAvailSecurityValue));
                        
                    }else{
                        double diffSecurity=dbSecurityValue-enterSecurityValue;
                        dbAvailSecurityValue-=diffSecurity;
                        if(dbAvailSecurityValue<0){
                            stringBuffer.append("\n"+"LoanAccountNo    :"  + CommonUtil.convertObjToStr(getListMap.get("ACCT_NUM"))+"\n"+
                            "Make it as Particaly secured In Loan Modual"+
                            "\n"+"LoanSecurityValue  :"+String .valueOf(enterSecurityValue-dbAvailSecurityValue));
                            txtSecurityValue.setText(String .valueOf(dbSecurityValue));
                        }
                    }
                    //                 txtAvalSecVal.setText(String.valueOf(dbAvailSecurityValue));
                    getListMap=null;
                }
                if(stringBuffer !=null && stringBuffer.length()>0)
                    ClientUtil.showMessageWindow(new String(stringBuffer));
            }else{
                txtAvalSecVal.setText(txtSecurityValue.getText());
                
            }
            availList=null;
            selectAvilMap=null;
            stringBuffer=null;
        }
        
    }
    private void tdtExpityDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtExpityDateFocusLost
        // TODO add your handling code here:
        tdtExpityDateFocusLost();
    }//GEN-LAST:event_tdtExpityDateFocusLost
    private void tdtExpityDateFocusLost(){
        // To check whether this To date is greater than this details From date
        ClientUtil.validateToDate(tdtExpityDate, tdtPolicyDate.getDateValue());
    }
    private void tdtPolicyDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtPolicyDateFocusLost
        // TODO add your handling code here:
        tdtPolicyDateFocusLost();
    }//GEN-LAST:event_tdtPolicyDateFocusLost
    private void tdtPolicyDateFocusLost(){
        // To check whether this From date is less than this details To date
        ClientUtil.validateFromDate(tdtPolicyDate, tdtExpityDate.getDateValue());
    }
    private void tdtToDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtToDateFocusLost
        // TODO add your handling code here:
        tdtToDateFocusLost();
    }//GEN-LAST:event_tdtToDateFocusLost
    private void tdtToDateFocusLost(){
        // To check whether this To date is greater than this details From date
        ClientUtil.validateToDate(tdtToDate, tdtFromDate.getDateValue());
    }
    private void tdtFromDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtFromDateFocusLost
        // TODO add your handling code here:
        tdtFromDateFocusLost();
    }//GEN-LAST:event_tdtFromDateFocusLost
    private void tdtFromDateFocusLost(){
        // To check whether this From date is less than this details To date
        ClientUtil.validateFromDate(tdtFromDate, tdtToDate.getDateValue());
    }
    private void tdtAsonFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtAsonFocusLost
        // TODO add your handling code here:
        tdtAsonFocusLost();
    }//GEN-LAST:event_tdtAsonFocusLost
    private void tdtAsonFocusLost(){
        // To check the entered date is less than or equal to current date
        ClientUtil.validateLTDate(tdtAson);
    }
    private void btnDelete_InsuranceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete_InsuranceActionPerformed
        // TODO add your handling code here:
        btnDelete_InsuranceActionPerformed();
    }//GEN-LAST:event_btnDelete_InsuranceActionPerformed
    private void btnDelete_InsuranceActionPerformed() {
        String strWarning = observableInsurance.securityExistWarning(rowInsurance, CommonUtil.convertObjToStr(cboSecurityNo_Insurance.getSelectedItem()));
        if (strWarning.length() <= 0){
            updateOBFields();
            setInsuranceNewOnlyEnable();
            setAllInsuranceDetailsEnableDisable(false);
            observableInsurance.deleteInsuranceTabRecord(rowInsurance);
            observableInsurance.resetInsuranceDetails();
            rowInsurance = -1;
            updateInsurance = false;
            observableSecurity.ttNotifyObservers();
        }else{
            displayAlert(strWarning);
        }
    }
    private void btnSave_InsuranceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSave_InsuranceActionPerformed
        // TODO add your handling code here:
        btnSave_InsuranceActionPerformed();
    }//GEN-LAST:event_btnSave_InsuranceActionPerformed
    private void btnSave_InsuranceActionPerformed() {
        /* mandatoryMessage1 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        final String mandatoryMessage1 = new MandatoryCheck().checkMandatory(getClass().getName(), panPolicy);
        final String mandatoryMessage2 = new MandatoryCheck().checkMandatory(getClass().getName(), panPolicyNature);
        String mandatoryMessage3 = "";
        if (CommonUtil.convertObjToStr(cboSecurityNo_Insurance.getSelectedItem()).length() > 0){
            mandatoryMessage3 = observableSecurity.isInsuranceNoSelectedInSecurityDetails(txtInsuranceNo.getText(), (((ComboBoxModel)cboSecurityNo_Insurance.getModel()).getKeyForSelected()));
        }
        /* mandatoryMessage2 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        if (mandatoryMessage1.length() > 0 || mandatoryMessage2.length() > 0 || mandatoryMessage3.length() > 0){
            displayAlert(mandatoryMessage1+mandatoryMessage2+mandatoryMessage3);
        }else{
            updateOBFields();
            if (observableInsurance.addInsuranceDetails(rowInsurance, updateInsurance) == 1){
                // Donot reset the fields when return value is 1(Option is No for replacing the existing record)
            }else{
                // To reset the Fields
                setInsuranceNewOnlyEnable();
                observableInsurance.resetInsuranceDetails();
                setAllInsuranceDetailsEnableDisable(false);
                updateInsurance = false;
            }
            observableSecurity.ttNotifyObservers();
        }
    }
    private void btnNew_InsuranceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNew_InsuranceActionPerformed
        // TODO add your handling code here:
        btnNew_InsuranceActionPerformed();
    }//GEN-LAST:event_btnNew_InsuranceActionPerformed
    private void btnNew_InsuranceActionPerformed() {
        updateOBFields();
        observableInsurance.resetInsuranceDetails();
        setAllInsuranceDetailsEnableDisable(true);
        setInsuranceDeleteOnlyDisable();
        rowInsurance = -1;
        updateInsurance = false;
        observableSecurity.ttNotifyObservers();
        setModified(true);
    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        authEnableDisable();
        observableSecurity.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeActionPerformed(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        authEnableDisable();
        observableSecurity.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeActionPerformed(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        authEnableDisable();
        observableSecurity.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeActionPerformed(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    private void authEnableDisable(){
        setAllSecurityDetailsEnableDisable(false);
        setAllInsuranceDetailsEnableDisable(false);
        setAllInsuranceBtnsEnableDisable(false);
        txtSecurityNo.setEditable(false);
    }
    
    // Actions have to be taken when Authorize button is pressed
    private void authorizeActionPerformed(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled) {
            HashMap singleAuthorizeMap = new HashMap();
            java.util.ArrayList arrList = new java.util.ArrayList();
            HashMap authDataMap = new HashMap();
            
            authDataMap.put("CUSTOMER ID", lblCustID_Disp.getText());
            
            arrList.add(authDataMap);
            
            singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            
            authorize(singleAuthorizeMap);
            observableSecurity.setAuthorizeMap(null);
        } else {
            HashMap mapParam = new HashMap();
            
            HashMap authorizeMapCondition = new HashMap();
            authorizeMapCondition.put("STATUS_BY", TrueTransactMain.USER_ID);
            authorizeMapCondition.put("BRANCH_CODE", getSelectedBranchID());
            authorizeMapCondition.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, authorizeMapCondition);
            mapParam.put(CommonConstants.MAP_NAME, "getSelectCust_Security_Insurance_AuthorizeTOList");
            viewType = AUTHORIZE;
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            //            btnSaveDisable();
            //            setAuthBtnEnableDisable();
            btnNew.setEnabled(false);
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
            btnCancel.setEnabled(true);
            
            authorizeMapCondition = null;
            //__ If there's no data to be Authorized, call Cancel action...
            //            if(!isModified()){
            //                setButtonEnableDisable();
            //                btnCancelActionPerformed(null);
            //            }
        }
    }
    
    private void btnSaveDisable(){
        btnSave.setEnabled(false);
        mitSave.setEnabled(false);
    }
    
    public void authorize(HashMap map) {
        observableSecurity.setAuthorizeMap(map);
        observableSecurity.doAction();
        if(observableSecurity.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
            super.setOpenForEditBy(observableSecurity.getStatusBy());
            super.removeEditLock(lblCustID_Disp.getText());
            isFilled = false;
            btnCancelActionPerformed(null);
            observableSecurity.setResultStatus();
        }
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        btncancelActionPerformed();
    }//GEN-LAST:event_btnCancelActionPerformed
    private void btncancelActionPerformed(){
        
        if(observableSecurity.getAuthorizeStatus1()!=null){
            super.removeEditLock(lblCustID_Disp.getText());
        }
        observableSecurity.resetForm();
        ClientUtil.enableDisable(this, false);// Disables the panel...
        observableSecurity.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        observableSecurity.setStatus();
        setButtonEnableDisable();
        setAllInsuranceBtnsEnableDisable(false);
        setAllSecurityBtnsEnableDisable(false);
        setModified(false);
        isFilled = false;
        viewType = "";
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        btnNew.setEnabled(true);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnCancel.setEnabled(false);
        btnSave.setEnabled(false);
        btnView.setEnabled(true);
    }
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        btnsaveActionPerformed();
    }//GEN-LAST:event_btnSaveActionPerformed
    private void btnsaveActionPerformed(){
        //observable.setAuthorizeNo();
        if (checkFieldsWhenMainSavePressed()){
            updateOBFields();
            observableSecurity.doAction();
            HashMap lockMap = new HashMap();
            ArrayList lst = new ArrayList();
            lst.add("CUSTOMER ID");
            lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lockMap.put("CUSTOMER ID",lblCustID_Disp.getText());
            setEditLockMap(lockMap);
            setEditLock();
            observableSecurity.resetForm();
            ClientUtil.enableDisable(this, false);
            observableSecurity.setResultStatus();
            observableSecurity.destroyObjects();
            observableSecurity.createObject();
            observableSecurity.ttNotifyObservers();
            setButtonEnableDisable();
            setAllInsuranceBtnsEnableDisable(false);
            setAllSecurityBtnsEnableDisable(false);
            if(observableSecurity.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
                setModified(false);
            }
        }
    }
    
    private boolean checkFieldsWhenMainSavePressed(){
        if (!((observableSecurity.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) || (viewType.equals(AUTHORIZE)))){
            StringBuffer mandatoryMessage = new StringBuffer("");
            if (!(tblSecurityTable.getRowCount() > 0)){
                mandatoryMessage.append(resourceBundle.getString("securityValueWarning"));
            }
            StringBuffer stbWarnMsg = new StringBuffer("");
            stbWarnMsg.append(observableSecurity.chkAllInsuranceHavingSecurity());
            stbWarnMsg.append(observableSecurity.chkSecValueGTInsuAmt());
            if (stbWarnMsg.length() > 0){
                mandatoryMessage.append(stbWarnMsg.toString());
            }
            stbWarnMsg = null;
            if (updateSecurity){
                mandatoryMessage.append("\n");
                mandatoryMessage.append(resourceBundle.getString("securityTableWarning"));
            }
            if (updateInsurance){
                mandatoryMessage.append("\n");
                mandatoryMessage.append(resourceBundle.getString("insuranceTableWarning"));
            }
            if (mandatoryMessage.length() > 0){
                displayAlert(mandatoryMessage.toString());
                return false;
            }
        }
        return true;
    }
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        btndeleteActionPerformed();
    }//GEN-LAST:event_btnDeleteActionPerformed
    private void btndeleteActionPerformed(){
        observableSecurity.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        setAllInsuranceBtnsEnableDisable(false); // To disable the Tool buttons for all the CTable
        setAllSecurityBtnsEnableDisable(false);
        popUp("Delete");
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnNew.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
    }
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        btneditActionPerformed();
    }//GEN-LAST:event_btnEditActionPerformed
    private void btneditActionPerformed(){
        observableSecurity.createObject();
        observableSecurity.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        popUp("Edit");
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnNew.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        updateSecurity = false;
        updateInsurance = false;
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        btnnewActionPerformed();
    }//GEN-LAST:event_btnNewActionPerformed
    private void btnnewActionPerformed(){
        observableSecurity.resetForm();
        ClientUtil.enableDisable(this, false);// Enables the panel...
        observableSecurity.setActionType(ClientConstants.ACTIONTYPE_NEW);
//        popUp("New");
        viewType = "New";
        HashMap sourceMap = new HashMap();
        sourceMap.put("SECURITY","SECURITY");
        new CheckCustomerIdUI(this,sourceMap);
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnNew.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        updateSecurity = false;
        updateInsurance = false;
        setModified(true);
    }
    private void btnSecurityDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSecurityDeleteActionPerformed
        // TODO add your handling code here:
        btnsecurityDeleteActionPerformed();
    }//GEN-LAST:event_btnSecurityDeleteActionPerformed
    // Actions have to be taken when Security Details Delete button pressed
    private void btnsecurityDeleteActionPerformed(){
        String strWarnMsg = observableSecurity.insuranceExistWarning(rowSecurity, CommonUtil.convertObjToStr(cboInsuranceNo.getSelectedItem()));
        if (strWarnMsg.length() <= 0){
            updateOBFields();
            observableSecurity.deleteSecurityTabRecord(rowSecurity);
            setAllSecurityDetailsEnableDisable(false);
            setSecurityBtnsOnlyNewEnable();
            rowSecurity = -1;
            observableSecurity.resetSecurityDetails();
            observableSecurity.ttNotifyObservers();
            updateSecurity = false;
        }else{
            displayAlert(strWarnMsg);
        }
    }
    private void btnSecuritySaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSecuritySaveActionPerformed
        // TODO add your handling code here:
        btnsecuritySaveActionPerformed();
    }//GEN-LAST:event_btnSecuritySaveActionPerformed
    // Actions have to be taken when Security Details Save button pressed
    private void btnsecuritySaveActionPerformed(){
        final String mandatoryMessage1 = new MandatoryCheck().checkMandatory(getClass().getName(), panSecDetails);
        /* mandatoryMessage1 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        
        final String mandatoryMessage2 = new MandatoryCheck().checkMandatory(getClass().getName(), panSecNature);
        /* mandatoryMessage2 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        
        // Check for Security Category, if it is Gold/Silver Ornaments then Weight should be entered
        String strSecCateKeySelected = CommonUtil.convertObjToStr(((ComboBoxModel) cboSecurityCate.getModel()).getKeyForSelected());
        String mandatoryMessage3 = "";
        String mandatoryMessage4 = "";
        if (CommonUtil.convertObjToStr(cboInsuranceNo.getSelectedItem()).length() > 0){
            mandatoryMessage4 = observableInsurance.isSecurityNoSelectedInInsuranceDetails(txtSecurityNo.getText(), (((ComboBoxModel)cboInsuranceNo.getModel()).getKeyForSelected()));
        }
        if (strSecCateKeySelected.equals(GOLD_SILVER_ORNAMENTS) && txtNetWeight.getText().length() == 0 && txtGrossWeight.getText().length() == 0){
            SecurityInsuranceMRB objMandatoryMRB = new SecurityInsuranceMRB();
            mandatoryMessage3 = objMandatoryMRB.getString("txtWeight");
            objMandatoryMRB = null;
        }
        if (mandatoryMessage1.length() > 0 || mandatoryMessage2.length() > 0 || mandatoryMessage3.length() > 0 || mandatoryMessage4.length() > 0){
            displayAlert(mandatoryMessage1+mandatoryMessage2+mandatoryMessage3+mandatoryMessage4);
        }else{
            updateOBFields();
            if (observableSecurity.addSecurityDetails(rowSecurity, updateSecurity) == 1){
                // Donot reset the fields when return value is 1(Option is No for replacing the existing record)
            }else{
                // To reset the Fields
                setAllSecurityDetailsEnableDisable(false);
                setSecurityBtnsOnlyNewEnable();
                observableSecurity.resetSecurityDetails();
                updateSecurity = false;
            }
            observableSecurity.ttNotifyObservers();
        }
    }
    private void btnSecurityNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSecurityNewActionPerformed
        // TODO add your handling code here:
        btnsecurityNewActionPerformed();
        
    }//GEN-LAST:event_btnSecurityNewActionPerformed
    // Actions have to be taken when Security Details New button is pressed
    private void btnsecurityNewActionPerformed(){
        updateOBFields();
        updateSecurity = false;
        observableSecurity.resetSecurityDetails();
        setAllSecurityDetailsEnableDisable(true);
        setSecurityBtnsOnlyNewSaveEnable();
        setDefaultValWhenSecurityNewBtnActionPerformed();
        rowSecurity = -1;
        observableSecurity.ttNotifyObservers();
        setModified(true);
    }
    private void setDefaultValWhenSecurityNewBtnActionPerformed(){
        observableSecurity.setDefaultValWhenSecurityNewBtnActionPerformed();
    }
    private void tblInsuranceMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInsuranceMousePressed
        // TODO add your handling code here:
        tblInsuranceMousePressed();
    }//GEN-LAST:event_tblInsuranceMousePressed
    // Actions have to be taken when a record from Insurance Details have been chosen
    private void tblInsuranceMousePressed(){
        if (tblInsurance.getSelectedRow() >= 0){
            // If the table is in editable mode
            setAllInsuranceBtnsEnableDisable(true);
            setAllInsuranceDetailsEnableDisable(true);
            updateOBFields();
            observableInsurance.populateInsuranceDetails(tblInsurance.getSelectedRow());
            if ((observableSecurity.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) || (viewType.equals(AUTHORIZE) ||viewType.equals("Enquiry")|| observableSecurity.getLblStatus().equals(ClientConstants.ACTION_STATUS[17]))){
                // If the record is populated for Delete or Authorization
                setAllInsuranceBtnsEnableDisable(false);
                setAllInsuranceDetailsEnableDisable(false);
            }else{
                setAllInsuranceBtnsEnableDisable(true);
                setAllInsuranceDetailsEnableDisable(true);
                updateInsurance = true;
            }
            rowInsurance = tblInsurance.getSelectedRow();
        }
    }
    private void tblSecurityTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSecurityTableMousePressed
        // TODO add your handling code here:
        tblsecurityTableMousePressed();
    }//GEN-LAST:event_tblSecurityTableMousePressed
    // Actions have to be taken when a record from Security Details have been chosen
    private void tblsecurityTableMousePressed(){
        updateOBFields();
        if (tblSecurityTable.getSelectedRow() >= 0){
            // If the table is in editable mode
            setAllSecurityDetailsEnableDisable(true);
            setAllSecurityBtnsEnableDisable(true);
            int insuranceTabRowToPopulate = observableSecurity.populateSecurityDetails(tblSecurityTable.getSelectedRow());
            if (insuranceTabRowToPopulate >= 0){
                javax.swing.ListSelectionModel selectionModel = tblInsurance.getSelectionModel();
                selectionModel.setSelectionInterval(insuranceTabRowToPopulate, insuranceTabRowToPopulate);
                tblInsuranceMousePressed();
            }
            if ((observableSecurity.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) ||viewType.equals("Enquiry") || (viewType.equals(AUTHORIZE))){
                // If the record is populated for Delete or Authorization
                setAllSecurityDetailsEnableDisable(false);
                setAllSecurityBtnsEnableDisable(false);
            }else{
                setAllSecurityDetailsEnableDisable(true);
                setAllSecurityBtnsEnableDisable(true);
                updateSecurity = true;
            }
            HashMap whereMap = new HashMap();
            HashMap keyMap = new HashMap();
            
            keyMap.put("CUST_ID", observableSecurity.getLblCustID_Disp());
            keyMap.put("SECURITY_NO", observableSecurity.getTxtSecurityNo());
            
            whereMap.put(CommonConstants.MAP_NAME, "getSelectLoanAvailedList");
            whereMap.put(CommonConstants.MAP_WHERE, keyMap);
            
            boolean haveData = ClientUtil.setTableModel(whereMap, tblLoanTable, false);
            
            if (!haveData){
                observableSecurity.resetSecurityLoanTab();
                tblLoanTable.setModel(observableSecurity.getTblSecurityLoanTab());
            }
            
            whereMap = null;
            keyMap = null;
            rowSecurity = tblSecurityTable.getSelectedRow();
        }
        observableSecurity.ttNotifyObservers();
    }
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    
    private void popUp(String field) {
        final HashMap viewMap = new HashMap();
        viewType = field;
        lblStatus.setText(ClientConstants.ACTION_STATUS[0]);
        HashMap whereConditionMap = new HashMap();
        whereConditionMap.put("BRANCH_CODE", getSelectedBranchID());
        viewMap.put(CommonConstants.MAP_WHERE, whereConditionMap);
        if(field.equals("Edit") || field.equals("Delete")) {
            
            //            super.removeEditLock(lblCustID_Disp.getText());
            
            ArrayList lst = new ArrayList();
            lst.add("CUSTOMER ID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            
            viewMap.put(CommonConstants.MAP_NAME, "popUpEditSecurityInsurance");
        }else if(field.equals("New")) {
            viewMap.put(CommonConstants.MAP_NAME, "popUpNewSecurityInsurance");
        }
        else if(field.equals("Enquiry")) {
            viewMap.put(CommonConstants.MAP_NAME, "popUpEditSecurityInsurance");
        }
        new ViewAll(this, viewMap).show();
    }
    
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        
        if (viewType != null) {
            if (viewType.equals("New") || viewType.equals("Edit") || viewType.equals("Delete") || viewType.equals(AUTHORIZE)|| viewType.equals("Enquiry")) {
                isFilled = true;
                if (viewType.equals(AUTHORIZE)){
                    hash.put(CommonConstants.MAP_WHERE, hash.get("CUSTOMER ID"));
                    observableSecurity.populateData(hash);
                }else if (viewType.equals("New")){
                    if(hash.containsKey("CUST_ID") ||hash.containsKey("NAME")){
                        hash.put("CUSTOMER ID",hash.get("CUST_ID"));
                        hash.put("CUSTOMER NAME",hash.get("NAME"));
                    }
                    observableSecurity.populateCustDetails(hash);
                }else{
                    hash.put(CommonConstants.MAP_WHERE, hash.get("CUSTOMER ID"));
                    observableSecurity.populateData(hash);
                }
                //                observableSecurity.ttNotifyObservers();
                if ((observableSecurity.getActionType() == ClientConstants.ACTIONTYPE_DELETE) || viewType.equals("Enquiry")|| (viewType.equals(AUTHORIZE))){
                    ClientUtil.enableDisable(this, false);
                    if(viewType==AUTHORIZE) {
                        btnAuthorize.setEnabled(observableSecurity.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                        btnReject.setEnabled(observableSecurity.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                        btnException.setEnabled(observableSecurity.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                    }
                }else{
                    setSecurityBtnsOnlyNewEnable();
                    setInsuranceNewOnlyEnable();
                    ClientUtil.enableDisable(this, true);
                    setAllInsuranceDetailsEnableDisable(false);
                    setAllSecurityDetailsEnableDisable(false);
                }
                observableSecurity.setStatus();
                //                setButtonEnableDisable();
                observableSecurity.ttNotifyObservers();
            }
        }
        setModified(true);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        javax.swing.JFrame frm = new javax.swing.JFrame();
        SecurityInsuranceUI ui = new SecurityInsuranceUI();
        frm.getContentPane().add(ui);
        ui.show();
        frm.setSize(600, 500);
        frm.show();
    }
    
    private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    private void setAllSecurityDetailsEnableDisable(boolean val){
        ClientUtil.enableDisable(panSecurityDetails_security, val);
        txtSecurityNo.setEditable(false);
        txtAvalSecVal.setEditable(false);
        //        rdoSecurityType_Collateral.setEnabled(val);
        //        rdoSecurityType_Primary.setEnabled(val);
        //        txtSecurityValue.setEnabled(val);
        //        txtWeight.setEnabled(txtWeight.isEnabled());
        //        tdtAson.setEnabled(val);
        //        txtParticulars.setEnabled(val);
        //        cboSecurityCate.setEnabled(val);
        //        tdtToDate.setEnabled(val);
        //        tdtFromDate.setEnabled(val);
        //        txtAvalSecVal.setEnabled(val);
        //        cboNatureCharge.setEnabled(val);
        //        tdtDateCharge.setEnabled(val);
        //        chkSelCommodityItem.setEnabled(val);
        //        cboForMillIndus.setEnabled(val);
        //        tdtDateInspection.setEnabled(val);
        //        cboStockStateFreq.setEnabled(val);
        //        cboInsuranceNo.setEnabled(val);
    }
    private void setSecurityBtnsOnlyNewEnable(){
        btnSecurityDelete.setEnabled(false);
        btnSecurityNew.setEnabled(true);
        btnSecuritySave.setEnabled(false);
    }
    private void setSecurityBtnsOnlyNewSaveEnable(){
        btnSecurityDelete.setEnabled(false);
        btnSecurityNew.setEnabled(true);
        btnSecuritySave.setEnabled(true);
    }
    private void setAllSecurityBtnsEnableDisable(boolean val){
        btnSecurityDelete.setEnabled(val);
        btnSecurityNew.setEnabled(val);
        btnSecuritySave.setEnabled(val);
    }
    
    private void setAllInsuranceDetailsEnableDisable(boolean val){
        ClientUtil.enableDisable(panInsureDetails_details, val);
        txtInsuranceNo.setEditable(false);
        //        txtInsureCompany.setEnabled(val);
        //        txtPolicyNumber.setEnabled(val);
        //        txtPolicyAmt.setEnabled(val);
        //        txtPremiumAmt.setEnabled(val);
        //        tdtPolicyDate.setEnabled(val);
        //        tdtExpityDate.setEnabled(val);
        //        cboNatureRisk.setEnabled(val);
        //        cboSecurityNo_Insurance.setEnabled(val);
        //        txtRemark_Insurance.setEnabled(val);
    }
    
    private void setAllInsuranceBtnsEnableDisable(boolean val){
        btnNew_Insurance.setEnabled(val);
        btnSave_Insurance.setEnabled(val);
        btnDelete_Insurance.setEnabled(val);
    }
    
    private void setInsuranceNewOnlyEnable(){
        btnNew_Insurance.setEnabled(true);
        btnSave_Insurance.setEnabled(false);
        btnDelete_Insurance.setEnabled(false);
    }
    
    private void setInsuranceDeleteOnlyDisable(){
        btnNew_Insurance.setEnabled(true);
        btnSave_Insurance.setEnabled(true);
        btnDelete_Insurance.setEnabled(false);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CPanel PanSecurityDetails;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDelete_Insurance;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnNew_Insurance;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSave_Insurance;
    private com.see.truetransact.uicomponent.CButton btnSecurityDelete;
    private com.see.truetransact.uicomponent.CButton btnSecurityNew;
    private com.see.truetransact.uicomponent.CButton btnSecuritySave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboForMillIndus;
    private com.see.truetransact.uicomponent.CComboBox cboInsuranceNo;
    private com.see.truetransact.uicomponent.CComboBox cboNatureCharge;
    private com.see.truetransact.uicomponent.CComboBox cboNatureRisk;
    private com.see.truetransact.uicomponent.CComboBox cboSecurityCate;
    private com.see.truetransact.uicomponent.CComboBox cboSecurityNo_Insurance;
    private com.see.truetransact.uicomponent.CComboBox cboStockStateFreq;
    private com.see.truetransact.uicomponent.CCheckBox chkSelCommodityItem;
    private com.see.truetransact.uicomponent.CLabel lblAson;
    private com.see.truetransact.uicomponent.CLabel lblAvalSecVal;
    private com.see.truetransact.uicomponent.CLabel lblCustCity;
    private com.see.truetransact.uicomponent.CLabel lblCustCity_Disp;
    private com.see.truetransact.uicomponent.CLabel lblCustEmail_ID;
    private com.see.truetransact.uicomponent.CLabel lblCustEmail_ID_Disp;
    private com.see.truetransact.uicomponent.CLabel lblCustID;
    private com.see.truetransact.uicomponent.CLabel lblCustID_Disp;
    private com.see.truetransact.uicomponent.CLabel lblCustName;
    private com.see.truetransact.uicomponent.CLabel lblCustName_Disp;
    private com.see.truetransact.uicomponent.CLabel lblCustPin;
    private com.see.truetransact.uicomponent.CLabel lblCustPin_Disp;
    private com.see.truetransact.uicomponent.CLabel lblCustStreet;
    private com.see.truetransact.uicomponent.CLabel lblCustStreet_Disp;
    private com.see.truetransact.uicomponent.CLabel lblDateCharge;
    private com.see.truetransact.uicomponent.CLabel lblDateInspection;
    private com.see.truetransact.uicomponent.CLabel lblExpityDate;
    private com.see.truetransact.uicomponent.CLabel lblForMillIndus;
    private com.see.truetransact.uicomponent.CLabel lblFromDate;
    private com.see.truetransact.uicomponent.CLabel lblGrossWeight;
    private com.see.truetransact.uicomponent.CLabel lblInsuranceNo_Disp;
    private com.see.truetransact.uicomponent.CLabel lblInsuranceNo_Display;
    private com.see.truetransact.uicomponent.CLabel lblInsureCompany;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNatureCharge;
    private com.see.truetransact.uicomponent.CLabel lblNatureRisk;
    private com.see.truetransact.uicomponent.CLabel lblNetWeight;
    private com.see.truetransact.uicomponent.CLabel lblParticulars;
    private com.see.truetransact.uicomponent.CLabel lblPolicyAmt;
    private com.see.truetransact.uicomponent.CLabel lblPolicyDate;
    private com.see.truetransact.uicomponent.CLabel lblPolicyNumber;
    private com.see.truetransact.uicomponent.CLabel lblPremiumAmt;
    private com.see.truetransact.uicomponent.CLabel lblRemark_Insurance;
    private com.see.truetransact.uicomponent.CLabel lblSecurityCate;
    private com.see.truetransact.uicomponent.CLabel lblSecurityNo;
    private com.see.truetransact.uicomponent.CLabel lblSecurityNo_Insurance;
    private com.see.truetransact.uicomponent.CLabel lblSecurityType;
    private com.see.truetransact.uicomponent.CLabel lblSecurityValue;
    private com.see.truetransact.uicomponent.CLabel lblSelCommodityItem;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace26;
    private com.see.truetransact.uicomponent.CLabel lblSpace27;
    private com.see.truetransact.uicomponent.CLabel lblSpace28;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace30;
    private com.see.truetransact.uicomponent.CLabel lblSpace31;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblStockStateFreq;
    private com.see.truetransact.uicomponent.CLabel lblToDate;
    private com.see.truetransact.uicomponent.CLabel lblTotalSecurity_Value;
    private com.see.truetransact.uicomponent.CLabel lblspace3;
    private com.see.truetransact.uicomponent.CLabel lblspace4;
    private com.see.truetransact.uicomponent.CMenuBar mbrSecInsur;
    private javax.swing.JMenuItem mitAuthorize;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitException;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitReject;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panInsuranceCTable;
    private com.see.truetransact.uicomponent.CPanel panInsuranceDetails;
    private com.see.truetransact.uicomponent.CPanel panInsuranceToolBars;
    private com.see.truetransact.uicomponent.CPanel panInsureDetails_details;
    private com.see.truetransact.uicomponent.CPanel panLoanAvailed;
    private com.see.truetransact.uicomponent.CPanel panLoanTable;
    private com.see.truetransact.uicomponent.CPanel panPolicy;
    private com.see.truetransact.uicomponent.CPanel panPolicyNature;
    private com.see.truetransact.uicomponent.CPanel panSecDetails;
    private com.see.truetransact.uicomponent.CPanel panSecNature;
    private com.see.truetransact.uicomponent.CPanel panSecurity;
    private com.see.truetransact.uicomponent.CPanel panSecurityDetails;
    private com.see.truetransact.uicomponent.CPanel panSecurityDetails_Cust;
    private com.see.truetransact.uicomponent.CPanel panSecurityDetails_security;
    private com.see.truetransact.uicomponent.CPanel panSecurityNature;
    private com.see.truetransact.uicomponent.CPanel panSecurityTable;
    private com.see.truetransact.uicomponent.CPanel panSecurityTableMain;
    private com.see.truetransact.uicomponent.CPanel panSecurityTools;
    private com.see.truetransact.uicomponent.CPanel panSecurityType;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTotalSecurity_Value;
    private com.see.truetransact.uicomponent.CPanel panparticulars;
    private com.see.truetransact.uicomponent.CButtonGroup rdoSecurityType;
    private com.see.truetransact.uicomponent.CRadioButton rdoSecurityType_Collateral;
    private com.see.truetransact.uicomponent.CRadioButton rdoSecurityType_Primary;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptException;
    private com.see.truetransact.uicomponent.CSeparator sptInsureDetails_Vert;
    private javax.swing.JSeparator sptPrint;
    private javax.swing.JSeparator sptProcess;
    private com.see.truetransact.uicomponent.CSeparator sptSecurityDetails_Vert;
    private com.see.truetransact.uicomponent.CScrollPane srpInsuranceCTable;
    private com.see.truetransact.uicomponent.CScrollPane srpLoanTable;
    private com.see.truetransact.uicomponent.CScrollPane srpSecurityTable;
    private com.see.truetransact.uicomponent.CScrollPane srpTxtAreaParticulars;
    private com.see.truetransact.uicomponent.CTabbedPane tabSecurityDetails;
    private com.see.truetransact.uicomponent.CTable tblInsurance;
    private com.see.truetransact.uicomponent.CTable tblLoanTable;
    private com.see.truetransact.uicomponent.CTable tblSecurityTable;
    private javax.swing.JToolBar tbrSecInsur;
    private com.see.truetransact.uicomponent.CDateField tdtAson;
    private com.see.truetransact.uicomponent.CDateField tdtDateCharge;
    private com.see.truetransact.uicomponent.CDateField tdtDateInspection;
    private com.see.truetransact.uicomponent.CDateField tdtExpityDate;
    private com.see.truetransact.uicomponent.CDateField tdtFromDate;
    private com.see.truetransact.uicomponent.CDateField tdtPolicyDate;
    private com.see.truetransact.uicomponent.CDateField tdtToDate;
    private com.see.truetransact.uicomponent.CTextArea txtAreaParticular;
    private com.see.truetransact.uicomponent.CTextField txtAvalSecVal;
    private com.see.truetransact.uicomponent.CTextField txtGrossWeight;
    private com.see.truetransact.uicomponent.CTextField txtInsuranceNo;
    private com.see.truetransact.uicomponent.CTextField txtInsureCompany;
    private com.see.truetransact.uicomponent.CTextField txtNetWeight;
    private com.see.truetransact.uicomponent.CTextField txtPolicyAmt;
    private com.see.truetransact.uicomponent.CTextField txtPolicyNumber;
    private com.see.truetransact.uicomponent.CTextField txtPremiumAmt;
    private com.see.truetransact.uicomponent.CTextField txtRemark_Insurance;
    private com.see.truetransact.uicomponent.CTextField txtSecurityNo;
    private com.see.truetransact.uicomponent.CTextField txtSecurityValue;
    private com.see.truetransact.uicomponent.CTextField txtTotalSecurity_Value;
    // End of variables declaration//GEN-END:variables
    
}
