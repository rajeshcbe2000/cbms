/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * CurrencyExchangeUI.java
 *
 * Created on January 9, 2004, 5:15 PM
 * Modified on February 11,2004
 *
 * Modifications :
 * 1. Added Importing data from a XML file.
 * 2. Added two text box components which'll show the Imported Transaction ID  and 
 *    imported from Branch Details. 
 * 3. Giving the different implementation for exporting the file.
 *    Earlier it was doing file io operations. Now a DOM is created and then 
 *    it is saved into an XML file.
 * 4. And also requied changes in OB, RB,MRB,HashMap, TO and MAP files. 
 */

package com.see.truetransact.ui.forex;
/*
 * Modified by Hemant
 */
import java.util.Observer;
import java.util.HashMap;
import java.util.Observable;
import java.io.File;
import java.io.FileOutputStream;
import java.awt.Dimension;
import java.awt.Toolkit;

import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.common.denomination.DenominationUI;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.uicomponent.CDialog;
import javax.swing.JFileChooser;
/**
 *
 * @author  bala
 */
public class CurrencyExchangeUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField {
    private CurrencyExchangeOB observable;
    private HashMap mandatoryMap;
    private int viewType = 0;
    
    private final int ACCTNO = 100;
    private final String ACT_NUM = "ACCOUNTNO";
    private final String CUST_NAME = "CUSTOMERNAME";
    private final String CUST_TYPE = "CUSTOMERTYPE";
        
    /** Creates new form CurrencyExchange */
    public CurrencyExchangeUI() {
        initComponents();
        initSetup();
    }
    
    private void initSetup() {
        try{
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setObservable();
        setHelpMessage();
        initComponentData();
        setMaxLength();
        setButtonEnableDisable();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void setInvisible() {
        panAcct.setVisible(false);
        panImpTrDetails.setVisible(false);
        
    }
    
    private void setMaxLength() {
        /*txttransId.setMaxLength(16);
txttransDt.setMaxLength(0);
txtacctNo.setMaxLength(16);
txttouristName.setMaxLength(64);
txtpassportNo.setMaxLength(64);
txttouristNote.setMaxLength(256);
txtinstrumentNo.setMaxLength(64);
txtinstrumentDate.setMaxLength(0);
txtinstrumentDetails.setMaxLength(128);
txttransType.setMaxLength(32);
txttransCurrency.setMaxLength(32);
txttransAmount.setMaxLength(16);
txtconvCurrency.setMaxLength(32);
txtvalueDate.setMaxLength(0);
txtexchangeRate.setMaxLength(16);
txtcommission.setMaxLength(16);
txttotalAmount.setMaxLength(16);
txtremarks.setMaxLength(256);
txtcreatedBy.setMaxLength(64);
txtcreatedDt.setMaxLength(0);
txtauthorizedBy.setMaxLength(64);
txtauthorizedDt.setMaxLength(0);
txtstatus.setMaxLength(32);*/
    }
    
    private void initComponentData() {
        cboTransCurrency.setModel (observable.getCbmTransCurrency());
        cboConvCurrency.setModel (observable.getCbmConvCurrency());
        cboCustType.setModel (observable.getCbmCustType());
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
                 
         if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE) {
            btnDenomination.setEnabled(!btnNew.isEnabled());
            btnExport.setEnabled(!btnNew.isEnabled());
            btnImport.setEnabled(!btnNew.isEnabled());
            ClientUtil.enableDisable(this, !btnNew.isEnabled());
         }
         
         btnAcctNo.setEnabled(false);
         //ClientUtil.enableDisable(panTourist, false);
         ClientUtil.enableDisable(panAcct, false);
         lblStatus.setText(observable.getLblStatus());
    }
    
    private void setFieldNames() {
	btnAcctNo.setName("btnAcctNo");
	btnCancel.setName("btnCancel");
	btnClose.setName("btnClose");
	btnDelete.setName("btnDelete");
	btnDenomination.setName("btnDenomination");
	btnEdit.setName("btnEdit");
	btnNew.setName("btnNew");
	btnPrint.setName("btnPrint");
	btnSave.setName("btnSave");
	cSeparator1.setName("cSeparator1");
	cSeparator2.setName("cSeparator2");
	cboConvCurrency.setName("cboConvCurrency");
	cboCustType.setName("cboCustType");
	cboTransCurrency.setName("cboTransCurrency");
	lblAcctNo.setName("lblAcctNo");
	lblBranchCode.setName("lblBranchCode");
	lblCommission.setName("lblCommission");
	lblConvCurrency.setName("lblConvCurrency");
	lblCustGroup.setName("lblCustGroup");
	lblCustRemarlks.setName("lblCustRemarlks");
	lblCustomerType.setName("lblCustomerType");
	lblDiaComm.setName("lblDiaComm");
	lblDiaCrossCcyRate.setName("lblDiaCrossCcyRate");
	lblDiaEquiAmt.setName("lblDiaEquiAmt");
	lblDiaPaidAmt.setName("lblDiaPaidAmt");
	lblDiaTotAmt.setName("lblDiaTotAmt");
	lblExchangeRate.setName("lblExchangeRate");
	lblMsg.setName("lblMsg");
	lblName.setName("lblName");
	lblRemarks.setName("lblRemarks");
	lblSpace1.setName("lblSpace1");
	lblSpace2.setName("lblSpace2");
	lblSpace3.setName("lblSpace3");
	lblStatus.setName("lblStatus");
	lblTotalAmount.setName("lblTotalAmount");
	lblTouristBankDetails.setName("lblTouristBankDetails");
	lblTouristInstrumentDt.setName("lblTouristInstrumentDt");
	lblTouristInstrumentNo.setName("lblTouristInstrumentNo");
	lblTouristName.setName("lblTouristName");
	lblTouristPassportNo.setName("lblTouristPassportNo");
	lblTouristRemarks.setName("lblTouristRemarks");
	lblTransAmount.setName("lblTransAmount");
	lblTransCurrency.setName("lblTransCurrency");
	lblTransId.setName("lblTransId");
	lblTransType.setName("lblTransType");
	lblType.setName("lblType");
	lblValueDate.setName("lblValueDate");
	mbrCurrencyExchange.setName("mbrCurrencyExchange");
	panAcct.setName("panAcct");
	panAcctNo.setName("panAcctNo");
	panCurrencyExchange.setName("panCurrencyExchange");
	panDiaCalc.setName("panDiaCalc");
	panStatus.setName("panStatus");
	panTourist.setName("panTourist");
	panTransType.setName("panTransType");
	rdoTransType_Deposit.setName("rdoTransType_Deposit");
	rdoTransType_Withdrawal.setName("rdoTransType_Withdrawal");
	tdtTouristInstrumentDt.setName("tdtTouristInstrumentDt");
	txtAcctNo.setName("txtAcctNo");
	txtBranchCode.setName("txtBranchCode");
	txtCommission.setName("txtCommission");
	txtCustGroup.setName("txtCustGroup");
	txtCustRemarks.setName("txtCustRemarks");
	txtDiaComm.setName("txtDiaComm");
	txtDiaCrossCcyRate.setName("txtDiaCrossCcyRate");
	txtDiaEquiAmt.setName("txtDiaEquiAmt");
	txtDiaTotAmt.setName("txtDiaTotAmt");
	txtDiaTransAmt.setName("txtDiaTransAmt");
	txtExchangeRate.setName("txtExchangeRate");
	txtName.setName("txtName");
	txtRemarks.setName("txtRemarks");
	txtTotalAmount.setName("txtTotalAmount");
	txtTouristBankDetails.setName("txtTouristBankDetails");
	txtTouristInstrumentNo.setName("txtTouristInstrumentNo");
	txtTouristName.setName("txtTouristName");
	txtTouristPassportNo.setName("txtTouristPassportNo");
	txtTouristRemarks.setName("txtTouristRemarks");
	txtTransAmount.setName("txtTransAmount");
	txtTransId.setName("txtTransId");
	txtType.setName("txtType");
	txtValueDate.setName("txtValueDate");
        btnExport.setName("btnExport");
        btnImport.setName("btnImport");
        lblFromBranch.setName("lblFromBranch");
        txtFromBranch.setName("txtFromBranch");
        lblImpTrID.setName("lblImpTrID");
        txtImpTrID.setName("txtImpTrID");
    }

    
    private void internationalize() {
        final CurrencyExchangeRB resourceBundle = new CurrencyExchangeRB();
        
	lblConvCurrency.setText(resourceBundle.getString("lblConvCurrency"));
	btnClose.setText(resourceBundle.getString("btnClose"));
	lblTransCurrency.setText(resourceBundle.getString("lblTransCurrency"));
	lblMsg.setText(resourceBundle.getString("lblMsg"));
	lblTransId.setText(resourceBundle.getString("lblTransId"));
	lblSpace2.setText(resourceBundle.getString("lblSpace2"));
	btnSave.setText(resourceBundle.getString("btnSave"));
	lblValueDate.setText(resourceBundle.getString("lblValueDate"));
	lblExchangeRate.setText(resourceBundle.getString("lblExchangeRate"));
	lblSpace3.setText(resourceBundle.getString("lblSpace3"));
	lblStatus.setText(resourceBundle.getString("lblStatus"));
	rdoTransType_Deposit.setText(resourceBundle.getString("rdoTransType_Deposit"));
	lblSpace1.setText(resourceBundle.getString("lblSpace1"));
	btnAcctNo.setText(resourceBundle.getString("btnAcctNo"));
	lblRemarks.setText(resourceBundle.getString("lblRemarks"));
	btnDelete.setText(resourceBundle.getString("btnDelete"));
	lblName.setText(resourceBundle.getString("lblName"));
	lblTotalAmount.setText(resourceBundle.getString("lblTotalAmount"));
	btnEdit.setText(resourceBundle.getString("btnEdit"));
	lblCommission.setText(resourceBundle.getString("lblCommission"));
	rdoTransType_Withdrawal.setText(resourceBundle.getString("rdoTransType_Withdrawal"));
	btnNew.setText(resourceBundle.getString("btnNew"));
	lblTransAmount.setText(resourceBundle.getString("lblTransAmount"));
	btnPrint.setText(resourceBundle.getString("btnPrint"));
	lblTransType.setText(resourceBundle.getString("lblTransType"));
	lblAcctNo.setText(resourceBundle.getString("lblAcctNo"));
    }
    
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("rdoTransType_Deposit", new Boolean(true));
        mandatoryMap.put("cboTransCurrency", new Boolean(true));
        mandatoryMap.put("cboConvCurrency", new Boolean(true));
        mandatoryMap.put("txtValueDate", new Boolean(true));
        mandatoryMap.put("txtExchangeRate", new Boolean(true));
        mandatoryMap.put("txtCommission", new Boolean(true));
        mandatoryMap.put("txtRemarks", new Boolean(true));
        mandatoryMap.put("txtTransId", new Boolean(true));
        mandatoryMap.put("cboCustType", new Boolean(true));
        mandatoryMap.put("txtAcctNo", new Boolean(true));
        mandatoryMap.put("txtName", new Boolean(true));
        mandatoryMap.put("txtCustGroup", new Boolean(true));
        mandatoryMap.put("txtBranchCode", new Boolean(true));
        mandatoryMap.put("txtType", new Boolean(true));
        mandatoryMap.put("txtCustRemarks", new Boolean(true));
        mandatoryMap.put("txtTouristName", new Boolean(true));
        mandatoryMap.put("txtTouristPassportNo", new Boolean(true));
        mandatoryMap.put("txtTouristInstrumentNo", new Boolean(true));
        mandatoryMap.put("tdtTouristInstrumentDt", new Boolean(true));
        mandatoryMap.put("txtTouristBankDetails", new Boolean(true));
        mandatoryMap.put("txtTouristRemarks", new Boolean(true));
        mandatoryMap.put("txtDiaTransAmt", new Boolean(true));
        mandatoryMap.put("txtDiaCrossCcyRate", new Boolean(true));
        mandatoryMap.put("txtDiaEquiAmt", new Boolean(true));
        mandatoryMap.put("txtDiaComm", new Boolean(true));
        mandatoryMap.put("txtDiaTotAmt", new Boolean(true));
        mandatoryMap.put("txtTotalAmount", new Boolean(true));
        mandatoryMap.put("txtTransAmount", new Boolean(true));
        mandatoryMap.put("txtFromBranch", new Boolean(true));
        mandatoryMap.put("txtImpTrID", new Boolean(true));
    }

    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
        
    private void setObservable() {
        try {
            observable = new CurrencyExchangeOB();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void removeRadioButtons() {
        rdgTransType.remove(rdoTransType_Deposit);
        rdgTransType.remove(rdoTransType_Withdrawal);
    }
    
    private void addRadioButtons() {
        rdgTransType = new CButtonGroup();
        rdgTransType.add (rdoTransType_Deposit);
        rdgTransType.add (rdoTransType_Withdrawal);
    }
    
            
    public void update(Observable observed, Object arg) {
        removeRadioButtons();
        
        rdoTransType_Deposit.setSelected(observable.getRdoTransType_Deposit()); 
        rdoTransType_Withdrawal.setSelected(observable.getRdoTransType_Withdrawal()); 
 
        txtValueDate.setText(observable.getTxtValueDate()); 
        txtExchangeRate.setText(observable.getTxtExchangeRate()); 
        txtCommission.setText(observable.getTxtCommission()); 
        txtRemarks.setText(observable.getTxtRemarks()); 
        txtTransId.setText(observable.getTxtTransId()); 
        txtAcctNo.setText(observable.getTxtAcctNo()); 
        txtName.setText(observable.getTxtName()); 
        txtCustGroup.setText(observable.getTxtCustGroup()); 
        txtBranchCode.setText(observable.getTxtBranchCode()); 
        txtType.setText(observable.getTxtType()); 
        txtCustRemarks.setText(observable.getTxtCustRemarks()); 
        txtTouristName.setText(observable.getTxtTouristName()); 
        txtTouristPassportNo.setText(observable.getTxtTouristPassportNo()); 
        txtTouristInstrumentNo.setText(observable.getTxtTouristInstrumentNo()); 
        tdtTouristInstrumentDt.setDateValue(observable.getTdtTouristInstrumentDt()); 
        txtTouristBankDetails.setText(observable.getTxtTouristBankDetails()); 
        txtTouristRemarks.setText(observable.getTxtTouristRemarks()); 
        txtDiaTransAmt.setText(observable.getTxtDiaTransAmt()); 
        txtDiaCrossCcyRate.setText(observable.getTxtDiaCrossCcyRate()); 
        txtDiaEquiAmt.setText(observable.getTxtDiaEquiAmt()); 
        txtDiaComm.setText(observable.getTxtDiaComm()); 
        txtDiaTotAmt.setText(observable.getTxtDiaTotAmt()); 
        txtTotalAmount.setText(observable.getTxtTotalAmount()); 
        txtTransAmount.setText(observable.getTxtTransAmount()); 
        txtFromBranch.setText(observable.getTxtFromBranch());
        txtImpTrID.setText(observable.getTxtImpTrID());
        
        cboCustType.setSelectedItem(((ComboBoxModel) cboCustType.getModel()).getDataForKey(observable.getCboCustType()));
        cboTransCurrency.setSelectedItem(((ComboBoxModel) cboTransCurrency.getModel()).getDataForKey(observable.getCboTransCurrency()));
        cboConvCurrency.setSelectedItem(((ComboBoxModel) cboConvCurrency.getModel()).getDataForKey(observable.getCboConvCurrency()));
        addRadioButtons();
    }
    
    public void updateOBFields() {
        observable.setRdoTransType_Deposit(rdoTransType_Deposit.isSelected()); 
        observable.setRdoTransType_Withdrawal(rdoTransType_Withdrawal.isSelected()); 
        observable.setCboTransCurrency((String) ((ComboBoxModel) cboTransCurrency.getModel()).getKeyForSelected());
        observable.setCboConvCurrency((String) ((ComboBoxModel) cboConvCurrency.getModel()).getKeyForSelected());
        observable.setTxtValueDate(txtValueDate.getText()); 
        observable.setTxtExchangeRate(txtExchangeRate.getText()); 
        observable.setTxtCommission(txtCommission.getText()); 
        observable.setTxtRemarks(txtRemarks.getText()); 
        observable.setTxtTransId(txtTransId.getText()); 
        observable.setCboCustType((String) ((ComboBoxModel) cboCustType.getModel()).getKeyForSelected());
        observable.setTxtAcctNo(txtAcctNo.getText()); 
        observable.setTxtName(txtName.getText()); 
        observable.setTxtCustGroup(txtCustGroup.getText()); 
        observable.setTxtBranchCode(txtBranchCode.getText()); 
        observable.setTxtType(txtType.getText()); 
        observable.setTxtCustRemarks(txtCustRemarks.getText()); 
        observable.setTxtTouristName(txtTouristName.getText()); 
        observable.setTxtTouristPassportNo(txtTouristPassportNo.getText()); 
        observable.setTxtTouristInstrumentNo(txtTouristInstrumentNo.getText()); 
        observable.setTdtTouristInstrumentDt(tdtTouristInstrumentDt.getDateValue()); 
        observable.setTxtTouristBankDetails(txtTouristBankDetails.getText()); 
        observable.setTxtTouristRemarks(txtTouristRemarks.getText()); 
        observable.setTxtDiaTransAmt(txtDiaTransAmt.getText()); 
        observable.setTxtDiaCrossCcyRate(txtDiaCrossCcyRate.getText()); 
        observable.setTxtDiaEquiAmt(txtDiaEquiAmt.getText()); 
        observable.setTxtDiaComm(txtDiaComm.getText()); 
        observable.setTxtDiaTotAmt(txtDiaTotAmt.getText()); 
        observable.setTxtTotalAmount(txtTotalAmount.getText()); 
        observable.setTxtTransAmount(txtTransAmount.getText()); 
        observable.setTxtFromBranch(txtFromBranch.getText());
        observable.setTxtImpTrID(txtImpTrID.getText());
    }
    
    public void setHelpMessage() {
        CurrencyExchangeMRB objMandatoryRB = new CurrencyExchangeMRB();
        
        rdoTransType_Deposit.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoTransType_Deposit"));
        cboTransCurrency.setHelpMessage(lblMsg, objMandatoryRB.getString("cboTransCurrency"));
        cboConvCurrency.setHelpMessage(lblMsg, objMandatoryRB.getString("cboConvCurrency"));
        txtValueDate.setHelpMessage(lblMsg, objMandatoryRB.getString("txtValueDate"));
        txtExchangeRate.setHelpMessage(lblMsg, objMandatoryRB.getString("txtExchangeRate"));
        txtCommission.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCommission"));
        txtRemarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRemarks"));
        txtTransId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTransId"));
        cboCustType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCustType"));
        txtAcctNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAcctNo"));
        txtName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtName"));
        txtCustGroup.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCustGroup"));
        txtBranchCode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBranchCode"));
        txtType.setHelpMessage(lblMsg, objMandatoryRB.getString("txtType"));
        txtCustRemarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCustRemarks"));
        txtTouristName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTouristName"));
        txtTouristPassportNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTouristPassportNo"));
        txtTouristInstrumentNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTouristInstrumentNo"));
        tdtTouristInstrumentDt.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtTouristInstrumentDt"));
        txtTouristBankDetails.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTouristBankDetails"));
        txtTouristRemarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTouristRemarks"));
        txtDiaTransAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDiaTransAmt"));
        txtDiaCrossCcyRate.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDiaCrossCcyRate"));
        txtDiaEquiAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDiaEquiAmt"));
        txtDiaComm.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDiaComm"));
        txtDiaTotAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDiaTotAmt"));
        txtTotalAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTotalAmount"));
        txtTransAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTransAmount"));
    }
    
    private File getExportFileDirectory() throws Exception{
        //System.out.println("Getting error log file");
        final StringBuffer directory = new StringBuffer().append(System.getProperty("user.home")).append("/tt/export/");
        return new File(directory.toString());
    }
    
    private File getImportFileDirectory() throws Exception{
        //System.out.println("Getting error log file");
        final StringBuffer directory = new StringBuffer().append(System.getProperty("user.home")).append("/tt/import/");
        return new File(directory.toString());
    }
    
    public static void main(String[] arg){
        javax.swing.JFrame jf =  new javax.swing.JFrame();
        CurrencyExchangeUI ce = new CurrencyExchangeUI();
        jf.getContentPane().add(ce);
        jf.setSize(600, 700);
        jf.show();
        ce.show();
    } 
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdgTransType = new com.see.truetransact.uicomponent.CButtonGroup();
        panAcct = new com.see.truetransact.uicomponent.CPanel();
        panAcctNo = new com.see.truetransact.uicomponent.CPanel();
        txtAcctNo = new com.see.truetransact.uicomponent.CTextField();
        btnAcctNo = new com.see.truetransact.uicomponent.CButton();
        lblAcctNo = new com.see.truetransact.uicomponent.CLabel();
        lblName = new com.see.truetransact.uicomponent.CLabel();
        lblCustGroup = new com.see.truetransact.uicomponent.CLabel();
        txtName = new com.see.truetransact.uicomponent.CTextField();
        lblBranchCode = new com.see.truetransact.uicomponent.CLabel();
        txtCustGroup = new com.see.truetransact.uicomponent.CTextField();
        txtBranchCode = new com.see.truetransact.uicomponent.CTextField();
        lblType = new com.see.truetransact.uicomponent.CLabel();
        txtType = new com.see.truetransact.uicomponent.CTextField();
        lblCustRemarlks = new com.see.truetransact.uicomponent.CLabel();
        txtCustRemarks = new com.see.truetransact.uicomponent.CTextField();
        panImpTrDetails = new com.see.truetransact.uicomponent.CPanel();
        lblFromBranch = new com.see.truetransact.uicomponent.CLabel();
        lblImpTrID = new com.see.truetransact.uicomponent.CLabel();
        txtFromBranch = new com.see.truetransact.uicomponent.CTextField();
        txtImpTrID = new com.see.truetransact.uicomponent.CTextField();
        btnExport = new com.see.truetransact.uicomponent.CButton();
        btnImport = new com.see.truetransact.uicomponent.CButton();
        lblCustomerType = new com.see.truetransact.uicomponent.CLabel();
        cboCustType = new com.see.truetransact.uicomponent.CComboBox();
        rdoTransType_Deposit = new com.see.truetransact.uicomponent.CRadioButton();
        tbrCurrencyExchange = new javax.swing.JToolBar();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace11 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace12 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace13 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace14 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panCurrencyExchange = new com.see.truetransact.uicomponent.CPanel();
        lblTransType = new com.see.truetransact.uicomponent.CLabel();
        panTransType = new com.see.truetransact.uicomponent.CPanel();
        rdoTransType_Withdrawal = new com.see.truetransact.uicomponent.CRadioButton();
        lblTransCurrency = new com.see.truetransact.uicomponent.CLabel();
        cboTransCurrency = new com.see.truetransact.uicomponent.CComboBox();
        lblTransAmount = new com.see.truetransact.uicomponent.CLabel();
        lblConvCurrency = new com.see.truetransact.uicomponent.CLabel();
        cboConvCurrency = new com.see.truetransact.uicomponent.CComboBox();
        lblValueDate = new com.see.truetransact.uicomponent.CLabel();
        txtValueDate = new com.see.truetransact.uicomponent.CTextField();
        lblExchangeRate = new com.see.truetransact.uicomponent.CLabel();
        txtExchangeRate = new com.see.truetransact.uicomponent.CTextField();
        lblCommission = new com.see.truetransact.uicomponent.CLabel();
        txtCommission = new com.see.truetransact.uicomponent.CTextField();
        lblTotalAmount = new com.see.truetransact.uicomponent.CLabel();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        lblTransId = new com.see.truetransact.uicomponent.CLabel();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        txtTransId = new com.see.truetransact.uicomponent.CTextField();
        panTourist = new com.see.truetransact.uicomponent.CPanel();
        lblTouristName = new com.see.truetransact.uicomponent.CLabel();
        txtTouristName = new com.see.truetransact.uicomponent.CTextField();
        lblTouristPassportNo = new com.see.truetransact.uicomponent.CLabel();
        txtTouristPassportNo = new com.see.truetransact.uicomponent.CTextField();
        lblTouristInstrumentNo = new com.see.truetransact.uicomponent.CLabel();
        txtTouristInstrumentNo = new com.see.truetransact.uicomponent.CTextField();
        lblTouristInstrumentDt = new com.see.truetransact.uicomponent.CLabel();
        tdtTouristInstrumentDt = new com.see.truetransact.uicomponent.CDateField();
        lblTouristBankDetails = new com.see.truetransact.uicomponent.CLabel();
        txtTouristBankDetails = new com.see.truetransact.uicomponent.CTextField();
        lblTouristRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtTouristRemarks = new com.see.truetransact.uicomponent.CTextField();
        panDiaCalc = new com.see.truetransact.uicomponent.CPanel();
        lblDiaPaidAmt = new com.see.truetransact.uicomponent.CLabel();
        txtDiaTransAmt = new com.see.truetransact.uicomponent.CTextField();
        lblDiaCrossCcyRate = new com.see.truetransact.uicomponent.CLabel();
        txtDiaCrossCcyRate = new com.see.truetransact.uicomponent.CTextField();
        lblDiaEquiAmt = new com.see.truetransact.uicomponent.CLabel();
        txtDiaEquiAmt = new com.see.truetransact.uicomponent.CTextField();
        lblDiaComm = new com.see.truetransact.uicomponent.CLabel();
        txtDiaComm = new com.see.truetransact.uicomponent.CTextField();
        lblDiaTotAmt = new com.see.truetransact.uicomponent.CLabel();
        txtDiaTotAmt = new com.see.truetransact.uicomponent.CTextField();
        cSeparator1 = new com.see.truetransact.uicomponent.CSeparator();
        cSeparator2 = new com.see.truetransact.uicomponent.CSeparator();
        txtTotalAmount = new com.see.truetransact.uicomponent.CTextField();
        btnDenomination = new com.see.truetransact.uicomponent.CButton();
        txtTransAmount = new com.see.truetransact.uicomponent.CTextField();
        mbrCurrencyExchange = new com.see.truetransact.uicomponent.CMenuBar();
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

        panAcct.setBorder(javax.swing.BorderFactory.createTitledBorder(" Customer Info "));
        panAcct.setMinimumSize(new java.awt.Dimension(237, 180));
        panAcct.setPreferredSize(new java.awt.Dimension(237, 180));
        panAcct.setLayout(new java.awt.GridBagLayout());

        panAcctNo.setMinimumSize(new java.awt.Dimension(123, 21));
        panAcctNo.setPreferredSize(new java.awt.Dimension(123, 22));
        panAcctNo.setLayout(new java.awt.GridBagLayout());

        txtAcctNo.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panAcctNo.add(txtAcctNo, gridBagConstraints);

        btnAcctNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAcctNo.setToolTipText("Save");
        btnAcctNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAcctNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcctNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAcctNo.add(btnAcctNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panAcct.add(panAcctNo, gridBagConstraints);

        lblAcctNo.setText("           Account No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panAcct.add(lblAcctNo, gridBagConstraints);

        lblName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panAcct.add(lblName, gridBagConstraints);

        lblCustGroup.setText("Customer Group");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panAcct.add(lblCustGroup, gridBagConstraints);

        txtName.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panAcct.add(txtName, gridBagConstraints);

        lblBranchCode.setText("Branch Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panAcct.add(lblBranchCode, gridBagConstraints);

        txtCustGroup.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panAcct.add(txtCustGroup, gridBagConstraints);

        txtBranchCode.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panAcct.add(txtBranchCode, gridBagConstraints);

        lblType.setText("Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panAcct.add(lblType, gridBagConstraints);

        txtType.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panAcct.add(txtType, gridBagConstraints);

        lblCustRemarlks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panAcct.add(lblCustRemarlks, gridBagConstraints);

        txtCustRemarks.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panAcct.add(txtCustRemarks, gridBagConstraints);

        panImpTrDetails.setLayout(new java.awt.GridBagLayout());

        lblFromBranch.setText("Imported From Branch");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panImpTrDetails.add(lblFromBranch, gridBagConstraints);

        lblImpTrID.setText("Imported Transaction ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panImpTrDetails.add(lblImpTrID, gridBagConstraints);

        txtFromBranch.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panImpTrDetails.add(txtFromBranch, gridBagConstraints);

        txtImpTrID.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panImpTrDetails.add(txtImpTrID, gridBagConstraints);

        btnExport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_ClientView.gif"))); // NOI18N
        btnExport.setText("Export as File");
        btnExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportActionPerformed(evt);
            }
        });

        btnImport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_ClientView.gif"))); // NOI18N
        btnImport.setText("Import File");
        btnImport.setMinimumSize(new java.awt.Dimension(132, 26));
        btnImport.setPreferredSize(new java.awt.Dimension(132, 26));
        btnImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportActionPerformed(evt);
            }
        });

        lblCustomerType.setText("Customer Type");

        cboCustType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCustTypeActionPerformed(evt);
            }
        });

        rdgTransType.add(rdoTransType_Deposit);
        rdoTransType_Deposit.setText("Deposit");

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Currency Exchange");
        setMinimumSize(new java.awt.Dimension(506, 650));
        setPreferredSize(new java.awt.Dimension(506, 650));
        getContentPane().setLayout(new java.awt.BorderLayout(10, 10));

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrCurrencyExchange.add(btnNew);

        lblSpace11.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace11.setText("     ");
        lblSpace11.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace11.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace11.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrCurrencyExchange.add(lblSpace11);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrCurrencyExchange.add(btnEdit);

        lblSpace12.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace12.setText("     ");
        lblSpace12.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace12.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace12.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrCurrencyExchange.add(lblSpace12);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrCurrencyExchange.add(btnDelete);

        lblSpace2.setText("     ");
        tbrCurrencyExchange.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrCurrencyExchange.add(btnSave);

        lblSpace13.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace13.setText("     ");
        lblSpace13.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace13.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace13.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrCurrencyExchange.add(lblSpace13);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrCurrencyExchange.add(btnCancel);

        lblSpace3.setText("     ");
        tbrCurrencyExchange.add(lblSpace3);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrCurrencyExchange.add(btnPrint);

        lblSpace14.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace14.setText("     ");
        lblSpace14.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace14.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace14.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrCurrencyExchange.add(lblSpace14);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrCurrencyExchange.add(btnClose);

        getContentPane().add(tbrCurrencyExchange, java.awt.BorderLayout.NORTH);

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

        panCurrencyExchange.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panCurrencyExchange.setMinimumSize(new java.awt.Dimension(400, 400));
        panCurrencyExchange.setPreferredSize(new java.awt.Dimension(400, 400));
        panCurrencyExchange.setLayout(new java.awt.GridBagLayout());

        lblTransType.setText("Transaction Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCurrencyExchange.add(lblTransType, gridBagConstraints);

        panTransType.setLayout(new java.awt.GridBagLayout());

        rdgTransType.add(rdoTransType_Withdrawal);
        rdoTransType_Withdrawal.setSelected(true);
        rdoTransType_Withdrawal.setText("Withdrawal");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panTransType.add(rdoTransType_Withdrawal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCurrencyExchange.add(panTransType, gridBagConstraints);

        lblTransCurrency.setText("Transaction Currency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCurrencyExchange.add(lblTransCurrency, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCurrencyExchange.add(cboTransCurrency, gridBagConstraints);

        lblTransAmount.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCurrencyExchange.add(lblTransAmount, gridBagConstraints);

        lblConvCurrency.setText("Conversion Currency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCurrencyExchange.add(lblConvCurrency, gridBagConstraints);

        cboConvCurrency.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboConvCurrencyActionPerformed(evt);
            }
        });
        cboConvCurrency.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboConvCurrencyFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCurrencyExchange.add(cboConvCurrency, gridBagConstraints);

        lblValueDate.setText("Value Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCurrencyExchange.add(lblValueDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCurrencyExchange.add(txtValueDate, gridBagConstraints);

        lblExchangeRate.setText("Exchange Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCurrencyExchange.add(lblExchangeRate, gridBagConstraints);

        txtExchangeRate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtExchangeRateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCurrencyExchange.add(txtExchangeRate, gridBagConstraints);

        lblCommission.setText("Commission");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCurrencyExchange.add(lblCommission, gridBagConstraints);

        txtCommission.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCommissionFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCurrencyExchange.add(txtCommission, gridBagConstraints);

        lblTotalAmount.setText("Total Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCurrencyExchange.add(lblTotalAmount, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCurrencyExchange.add(lblRemarks, gridBagConstraints);

        lblTransId.setText("Transaction ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCurrencyExchange.add(lblTransId, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCurrencyExchange.add(txtRemarks, gridBagConstraints);

        txtTransId.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCurrencyExchange.add(txtTransId, gridBagConstraints);

        panTourist.setBorder(javax.swing.BorderFactory.createTitledBorder(" Tourist Info "));
        panTourist.setMinimumSize(new java.awt.Dimension(225, 180));
        panTourist.setPreferredSize(new java.awt.Dimension(225, 180));
        panTourist.setLayout(new java.awt.GridBagLayout());

        lblTouristName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTourist.add(lblTouristName, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTourist.add(txtTouristName, gridBagConstraints);

        lblTouristPassportNo.setText("Passport No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTourist.add(lblTouristPassportNo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTourist.add(txtTouristPassportNo, gridBagConstraints);

        lblTouristInstrumentNo.setText("Instrument No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTourist.add(lblTouristInstrumentNo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTourist.add(txtTouristInstrumentNo, gridBagConstraints);

        lblTouristInstrumentDt.setText("Instrument Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTourist.add(lblTouristInstrumentDt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTourist.add(tdtTouristInstrumentDt, gridBagConstraints);

        lblTouristBankDetails.setText("Bank Details");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTourist.add(lblTouristBankDetails, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTourist.add(txtTouristBankDetails, gridBagConstraints);

        lblTouristRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTourist.add(lblTouristRemarks, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTourist.add(txtTouristRemarks, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCurrencyExchange.add(panTourist, gridBagConstraints);

        panDiaCalc.setBorder(javax.swing.BorderFactory.createTitledBorder(" Calculation "));
        panDiaCalc.setMinimumSize(new java.awt.Dimension(213, 149));
        panDiaCalc.setLayout(new java.awt.GridBagLayout());

        lblDiaPaidAmt.setText("Transaction Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiaCalc.add(lblDiaPaidAmt, gridBagConstraints);

        txtDiaTransAmt.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiaCalc.add(txtDiaTransAmt, gridBagConstraints);

        lblDiaCrossCcyRate.setText("Cross Ccy Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiaCalc.add(lblDiaCrossCcyRate, gridBagConstraints);

        txtDiaCrossCcyRate.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiaCalc.add(txtDiaCrossCcyRate, gridBagConstraints);

        lblDiaEquiAmt.setText("Equivalent Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiaCalc.add(lblDiaEquiAmt, gridBagConstraints);

        txtDiaEquiAmt.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiaCalc.add(txtDiaEquiAmt, gridBagConstraints);

        lblDiaComm.setText("Commission");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiaCalc.add(lblDiaComm, gridBagConstraints);

        txtDiaComm.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiaCalc.add(txtDiaComm, gridBagConstraints);

        lblDiaTotAmt.setText("Total Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiaCalc.add(lblDiaTotAmt, gridBagConstraints);

        txtDiaTotAmt.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDiaCalc.add(txtDiaTotAmt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panDiaCalc.add(cSeparator1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panDiaCalc.add(cSeparator2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panCurrencyExchange.add(panDiaCalc, gridBagConstraints);

        txtTotalAmount.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCurrencyExchange.add(txtTotalAmount, gridBagConstraints);

        btnDenomination.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CALC.gif"))); // NOI18N
        btnDenomination.setText("Denomination");
        btnDenomination.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDenominationActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        panCurrencyExchange.add(btnDenomination, gridBagConstraints);

        txtTransAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTransAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCurrencyExchange.add(txtTransAmount, gridBagConstraints);

        getContentPane().add(panCurrencyExchange, java.awt.BorderLayout.CENTER);

        mnuProcess.setText("Process");

        mitNew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        mitNew.setMnemonic('N');
        mitNew.setText("New");
        mnuProcess.add(mitNew);

        mitEdit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        mitEdit.setMnemonic('E');
        mitEdit.setText("Edit");
        mnuProcess.add(mitEdit);

        mitDelete.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        mitDelete.setMnemonic('D');
        mitDelete.setText("Delete");
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptProcess);

        mitSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        mitSave.setMnemonic('S');
        mitSave.setText("Save");
        mnuProcess.add(mitSave);

        mitCancel.setMnemonic('C');
        mitCancel.setText("Cancel");
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitPrint.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        mitPrint.setMnemonic('P');
        mitPrint.setText("Print");
        mnuProcess.add(mitPrint);
        mnuProcess.add(sptPrint);

        mitClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        mitClose.setMnemonic('l');
        mitClose.setText("Close");
        mnuProcess.add(mitClose);

        mbrCurrencyExchange.add(mnuProcess);

        setJMenuBar(mbrCurrencyExchange);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportActionPerformed
        try {
            //viewType = ClientConstants.ACTIONTYPE_IMPORT;
            observable.setActionType (ClientConstants.ACTIONTYPE_IMPORT);
            lblStatus.setText(observable.getLblStatus());
            JFileChooser objJFileChooser = new JFileChooser();
            objJFileChooser.setDialogTitle("Select file to Import");
            objJFileChooser.setSelectedFile(getImportFileDirectory());
            objJFileChooser.showOpenDialog(this);
            String error = observable.importFile(objJFileChooser.getSelectedFile().getAbsolutePath());
            
            if(error!=null && !error.equals("")){
                System.out.println("Error: "+error);
                String message = "";
                if(error.equals("EXIST"))
                    message = "Import Aborted: Transaction Already Imported.";
                if(error.equals("DIFF_BRANCH"))
                    message = "Import Aborted: This file is not for this Branch.";
                COptionPane.showMessageDialog(this, message);
                observable.resetStatus();
                lblStatus.setText(observable.getLblStatus());
                return;
            }
            update(null,null);
            custInfo();
            ClientUtil.enableDisable(this, false);
            btnAcctNo.setEnabled(false);
            
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        // Add your handling code here:
    }//GEN-LAST:event_btnImportActionPerformed
    
    /** Returns the file with path after checking whether the directory exists or
     * not. Creates the directory, if not exists
     * @return Returns exportFile with Path
     */
    private String getExportFile() throws Exception{
        //System.out.println("Getting error log file");
        final StringBuffer directory = new StringBuffer().append(System.getProperty("user.home")).append("/tt/export/");
        final File ttDirectory = new File(directory.toString());
        //Creates the directory, if not exists
        if( !ttDirectory.exists() ) {
            ttDirectory.mkdirs();
        }
        final StringBuffer exportFile = new StringBuffer().append(directory).append(txtBranchCode.getText() + "_" + txtTransId.getText() + ".xml");
        return exportFile.toString();
    }
    
    private String xmlFileContent() {
        StringBuffer strB = new StringBuffer();
        strB.append ("<Branch code=" + txtBranchCode.getText() + ">\n");
        strB.append ("\t<AcctNo>");
        strB.append (txtAcctNo.getText());
        strB.append ("</AcctNo>\n");
        strB.append ("\t<CustGroup>");
        strB.append (txtCustGroup.getText());
        strB.append ("</CustGroup>\n");
        strB.append ("\t<CustRemarks>");
        strB.append (txtCustRemarks.getText());
        strB.append ("</CustRemarks>\n");
        strB.append ("\t<ExchangeRate>");
        strB.append (txtExchangeRate.getText());
        strB.append ("</ExchangeRate>\n");
        strB.append ("\t<Name>");
        strB.append (txtName.getText());
        strB.append ("</Name>\n");
        strB.append ("\t<Remarks>");
        strB.append (txtRemarks.getText());
        strB.append ("</Remarks>\n");
        strB.append ("\t<TotalAmount>");
        strB.append (txtTotalAmount.getText());
        strB.append ("</TotalAmount>\n");
        strB.append ("\t<TouristBankDetails>");
        strB.append (txtTouristBankDetails.getText());
        strB.append ("</TouristBankDetails>\n");
        strB.append ("\t<TouristInstrumentNo>");
        strB.append (txtTouristInstrumentNo.getText());
        strB.append ("</TouristInstrumentNo>\n");
        strB.append ("\t<TouristName>");
        strB.append (txtTouristName.getText());
        strB.append ("</TouristName>\n");
        strB.append ("\t<TouristPassportNo>");
        strB.append (txtTouristPassportNo.getText());
        strB.append ("</TouristPassportNo>\n");
        strB.append ("\t<TouristRemarks>");
        strB.append (txtTouristRemarks.getText());
        strB.append ("</TouristRemarks>\n");
        strB.append ("\t<TransAmount>");
        strB.append (txtTransAmount.getText());
        strB.append ("</TransAmount>\n");
        strB.append ("\t<TransId>");
        strB.append (txtTransId.getText());
        strB.append ("</TransId>\n");
        strB.append ("\t<Type>");
        strB.append (txtType.getText());
        strB.append ("</Type>\n");
        strB.append ("\t<ValueDate>");
        strB.append (txtValueDate.getText());
        strB.append ("</ValueDate>\n");
        strB.append ("\t<CustType>");
        strB.append ((String) cboCustType.getSelectedItem());
        strB.append ("</CustType>\n");
        strB.append ("\t<ConvCurrency>");
        strB.append ((String) cboConvCurrency.getSelectedItem());
        strB.append ("</ConvCurrency>\n");
        strB.append ("\t<TransCurrency>");
        strB.append ((String) cboTransCurrency.getSelectedItem());
        strB.append ("</TransCurrency>\n");
        strB.append ("</Branch>\n");
        return strB.toString();
    }

    private String htmlFileContent() {
        StringBuffer strB = new StringBuffer();
        strB.append ("<html>");
        strB.append("<body><table border=0 width=100%><tr><td><font face=\"Arial\">Branch Code</td><td><font face=\"Arial\">" + txtBranchCode.getText());
        strB.append ("</td><td><font face=\"Arial\">Transaction No.</td><td><font face=\"Arial\">");
        strB.append (txtTransId.getText());
        strB.append ("</td></tr><tr><td colspan=4><HR></td></tr>");
        
        if (txtAcctNo.getText().equals("")) {
            strB.append ("<tr><td><font face=\"Arial\">Tourist Name</td><td><font face=\"Arial\">");
            strB.append (txtTouristName.getText());
            strB.append ("</td><td><font face=\"Arial\">Passport No.</td><td><font face=\"Arial\">");
            strB.append (txtTouristPassportNo.getText());
            strB.append ("</td></tr><tr><td><font face=\"Arial\">Instrument No.</td><td><font face=\"Arial\">");
            strB.append (txtTouristInstrumentNo.getText());
            strB.append ("</td><td><font face=\"Arial\">Tourist Bank Details</td><td><font face=\"Arial\">");
            strB.append (txtTouristBankDetails.getText());
        } else {
            strB.append ("<tr><td><font face=\"Arial\">Account No</td><td><font face=\"Arial\">");
            strB.append (txtAcctNo.getText());
            strB.append ("</td><td><font face=\"Arial\">Customer Name</td><td><font face=\"Arial\">");
            strB.append (txtName.getText());
            strB.append ("</td></tr><tr><td><font face=\"Arial\">Customer Group</td><td><font face=\"Arial\">");
            strB.append (txtCustGroup.getText());
            strB.append ("</td><td><font face=\"Arial\">Customer Type</td><td><font face=\"Arial\">");
            strB.append ((String) cboCustType.getSelectedItem());
        }
        
        strB.append ("</td></tr><tr><td colspan=4><HR></td></tr>");
        strB.append ("<tr><td><font face=\"Arial\">Transaction Type</td><td><font face=\"Arial\">");
        strB.append (txtType.getText());
        strB.append ("</td><td><font face=\"Arial\">Value Date</td><td><font face=\"Arial\">");
        strB.append (txtValueDate.getText());
        strB.append ("</td></tr><tr><td><font face=\"Arial\">Transaction Currency</td><td><font face=\"Arial\">");
        strB.append ((String) cboTransCurrency.getSelectedItem());
        strB.append ("</td><td><font face=\"Arial\">Conversion Currency</td><td><font face=\"Arial\">");
        strB.append ((String) cboConvCurrency.getSelectedItem());
        strB.append ("</td></tr><tr><td><font face=\"Arial\">Transaction Amount</td><td ><font face=\"Arial\">");
        strB.append (txtTransAmount.getText());
        strB.append ("</td></tr><tr><td><font face=\"Arial\">Exchange Rate</td><td ><font face=\"Arial\">");
        strB.append (txtExchangeRate.getText());
        strB.append ("</td></tr><tr><td><font face=\"Arial\">Total Amount</td><td ><font face=\"Arial\"><b>");
        strB.append (txtTotalAmount.getText());
        strB.append ("<b></td></tr><tr><td><font face=\"Arial\">Narration</td><td><font face=\"Arial\">");
        strB.append (txtRemarks.getText());
        strB.append ("</td></tr></table><font><body></html>\n");
        return strB.toString();
    }
    
    private void exportAsXMLFile() {
        try {
            String fileName = getExportFile();
            FileOutputStream fileOutputStream = new FileOutputStream(fileName, true);
            fileOutputStream.write(xmlFileContent().getBytes());
            fileOutputStream.close();
            
            COptionPane.showMessageDialog(this, txtBranchCode.getText() + "_" + txtTransId.getText() + ".xml" + " - File has been exported. Pls send this file to the Appropriate Branch");
        }catch(Exception except) {
            except.printStackTrace();
        }        
    }

    private void btnExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportActionPerformed
        // Add your handling code here:
        String message;
        if(!txtTransId.getText().equals("") && !txtBranchCode.getText().equals("")){
            
            updateOBFields();
            String fileName = observable.exportFile();
            message = fileName + " - File has been exported. Pls send this file to the Appropriate Branch";
        }else
            message = "Can't create export file.";
        COptionPane.showMessageDialog(this, message);
        //exportAsXMLFile();        
    }//GEN-LAST:event_btnExportActionPerformed
    private void txtTransAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTransAmountFocusLost
        // Add your handling code here:
        //txtCommissionFocusLost(evt);
    }//GEN-LAST:event_txtTransAmountFocusLost
    private void btnDenominationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDenominationActionPerformed
        // Add your handling code here:
        denomination();
    }//GEN-LAST:event_btnDenominationActionPerformed
    private void cboCustTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCustTypeActionPerformed
        // Add your handling code here:
        String strCust = (String) cboCustType.getSelectedItem();
        if (!strCust.equals("") && strCust != null) {
            if (strCust.equals("Tourist")) {
                ClientUtil.enableDisable(panTourist, true);
                ClientUtil.enableDisable(panAcct, false);
                btnAcctNo.setEnabled(false);
            } else {
                ClientUtil.enableDisable(panTourist, false);
                ClientUtil.enableDisable(panAcct, true);
                btnAcctNo.setEnabled(true);
            }
        }
    }//GEN-LAST:event_cboCustTypeActionPerformed
    private void denomination() {
        // Add your handling code here:
        HashMap map = new HashMap();
        if (rdoTransType_Deposit.isSelected()) {
            map.put("TRANS_TYPE", "Deposit");
            map.put("CURRENCY_TYPE", (String) ((ComboBoxModel) cboTransCurrency.getModel()).getKeyForSelected());
            map.put("AMOUNT", txtTransAmount.getText());
        } else if (rdoTransType_Withdrawal.isSelected()) {
            map.put("TRANS_TYPE", "Withdrawal");
            map.put("CURRENCY_TYPE", (String) ((ComboBoxModel) cboConvCurrency.getModel()).getKeyForSelected());
            map.put("AMOUNT", txtTotalAmount.getText());
        }

        DenominationUI dui = new DenominationUI(map);
        showDialog(dui);        
    }
    private void cboConvCurrencyFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboConvCurrencyFocusLost
        // Add your handling code here:
        //txtCommissionFocusLost(evt);
    }    private void txtExchangeRateFocusLost(java.awt.event.FocusEvent evt) {//GEN-LAST:event_cboConvCurrencyFocusLost
        // Add your handling code here://GEN-FIRST:event_txtExchangeRateFocusLost
        txtCommissionFocusLost(evt);
    }//GEN-LAST:event_txtExchangeRateFocusLost
    private void cboConvCurrencyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboConvCurrencyActionPerformed
        // Add your handling code here:
        if (cboConvCurrency.getSelectedIndex() > 0 && 
            (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW ||
             observable.getActionType() == ClientConstants.ACTIONTYPE_IMPORT)) {
             
            txtCommission.setText("");     
            HashMap whereMap = new HashMap();
            whereMap.put ("TRANS_CURRENCY", (String) ((ComboBoxModel) cboTransCurrency.getModel()).getKeyForSelected());
            whereMap.put ("CONVERSION_CURRENCY", (String) ((ComboBoxModel) cboConvCurrency.getModel()).getKeyForSelected());
            whereMap.put ("CUSTOMER_TYPE", (String) ((ComboBoxModel) cboCustType.getModel()).getKeyForSelected());

            System.out.println ("whereMap " + whereMap);

            HashMap data = (HashMap) ClientUtil.executeQuery("getSellingBuyingRate", whereMap).get(0);
            System.out.println ("data " + data);

            if (!((String) data.get("PREFERRED")).equalsIgnoreCase("Y")) {
                COptionPane.showMessageDialog(this, "Selected Currency is not a preferred Currency", "Note", COptionPane.ERROR_MESSAGE);
                //System.out.println ("Not prefered");
            } else {
                
                txtCommission.setText(data.get("COMMISSION").toString());
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
                    System.out.println("Inside New.");
                    
                    txtValueDate.setText("");
                    txtExchangeRate.setText("");
                    
                    txtValueDate.setText(DateUtil.getStringDate((java.util.Date) data.get("VALUE_DATE")));

                    Float middleRate = new Float(data.get("MIDDLE_RATE").toString());

                    if (rdoTransType_Deposit.isSelected()) {
                        if (data.get("BUYING_PER") == null) {
                            txtExchangeRate.setText(data.get("BUYING_PRICE").toString());
                        } else {
                            txtExchangeRate.setText(
                                String.valueOf(middleRate.doubleValue() - 
                                    (middleRate.doubleValue() * 
                                    new Float(data.get("BUYING_PER").toString()).doubleValue())
                                    / 100));
                        }

                    } else if (rdoTransType_Withdrawal.isSelected()) {

                        if (data.get("SELLING_PER") == null) {
                            txtExchangeRate.setText(data.get("SELLING_PRICE").toString());
                        } else {
                            txtExchangeRate.setText(
                                String.valueOf(middleRate.doubleValue() + 
                                    (middleRate.doubleValue() * 
                                    new Float (data.get("SELLING_PER").toString()).doubleValue())
                                    / 100));
                        }
                    }
                }
                txtCommissionFocusLost(null); 
            } 
          }  
        /*
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_IMPORT){
            HashMap whereMap = new HashMap();
            whereMap.put ("TRANS_CURRENCY", (String) ((ComboBoxModel) cboTransCurrency.getModel()).getKeyForSelected());
            whereMap.put ("CONVERSION_CURRENCY", (String) ((ComboBoxModel) cboConvCurrency.getModel()).getKeyForSelected());
            whereMap.put ("CUSTOMER_TYPE", (String) ((ComboBoxModel) cboCustType.getModel()).getKeyForSelected());

            System.out.println ("whereMap " + whereMap);

            HashMap data = (HashMap) ClientUtil.executeQuery("getSellingBuyingRate", whereMap).get(0);
            System.out.println ("data " + data);

            if (!((String) data.get("PREFERRED")).equalsIgnoreCase("Y")) {
                COptionPane.showMessageDialog(this, "Selected Currency is not a preferred Currency", "Note", COptionPane.ERROR_MESSAGE);
                //System.out.println ("Not prefered");
            } else {
                txtCommission.setText(data.get("COMMISSION").toString());
                txtCommissionFocusLost(null);
            }   
        }/**/
        
        
    }//GEN-LAST:event_cboConvCurrencyActionPerformed
    private void txtCommissionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCommissionFocusLost
        // Add your handling code here:
        try { 
            double totAmt = Double.parseDouble (txtTransAmount.getText()) * 
                             Double.parseDouble (txtExchangeRate.getText());
                             
            if (rdoTransType_Deposit.isSelected()) {
                totAmt -= Double.parseDouble (txtCommission.getText());
            } else if (rdoTransType_Withdrawal.isSelected()) {
                totAmt += Double.parseDouble (txtCommission.getText());
            }
            txtTotalAmount.setText(String.valueOf(totAmt));
            calculate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtCommissionFocusLost
    public void fillData(Object obj) {
        HashMap hash = (HashMap) obj;
        if (viewType != 0) {
            if (viewType == ClientConstants.ACTIONTYPE_EDIT || 
                viewType == ClientConstants.ACTIONTYPE_DELETE) {
                hash.put(CommonConstants.MAP_WHERE, hash.get("TRANS_ID"));
                observable.populateData(hash);
                calculate();
                setButtonEnableDisable();
                if (viewType == ClientConstants.ACTIONTYPE_DELETE) {
                    ClientUtil.enableDisable(this, false);
                } else if(viewType == ClientConstants.ACTIONTYPE_EDIT && txtImpTrID.getText().equals("") ) 
                        ClientUtil.enableDisable(this, true);
                else{        
                    ClientUtil.enableDisable(this, false);
                    btnExport.setEnabled(false);
                    btnSave.setEnabled(false);
                    btnAcctNo.setEnabled(false);
                }
            } else if (viewType == ACCTNO) {
                txtAcctNo.setText((String) hash.get(ACT_NUM));
            }
            custInfo();
        }
    }
    
    private void custInfo() {
        if (!txtAcctNo.getText().equals("")) {
            HashMap whereMap = new HashMap();
            whereMap.put ("ACCT_NUM", txtAcctNo.getText());

            HashMap data = (HashMap) ClientUtil.executeQuery("getForexCustomer", whereMap).get(0);

            txtBranchCode.setText((String) data.get("BRANCH_CODE"));
            txtCustGroup.setText((String) data.get("CUSTOMERGROUP"));
            txtCustRemarks.setText((String) data.get("REMARKS"));
            txtName.setText((String) data.get("FNAME") + " " + (String) data.get("MNAME") + " " + (String) data.get("LNAME"));
            txtType.setText((String) data.get("CUST_TYPE_ID"));

            ((ComboBoxModel) cboCustType.getModel()).setKeyForSelected((String) data.get("CUST_TYPE_ID"));
        } else {
            cboCustType.setSelectedItem("Tourist");
        }    
    }
    
    private void callView(int currField) {
        viewType = currField;
        
        HashMap viewMap = new HashMap();
        if (currField == ClientConstants.ACTIONTYPE_EDIT  || 
            currField == ClientConstants.ACTIONTYPE_DELETE) {
            observable.setActionType (currField);
            viewMap.put(CommonConstants.MAP_NAME, "getSelectCurrencyExchangeTOList");
        } else {
            viewMap.put(CommonConstants.MAP_NAME, "getAccountList");
        }

        new ViewAll(this, viewMap).show();
    }
    
    private void showDialog(CDialog dialog) {
        ClientUtil.showDialog(dialog);
    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // Add your handling code here:
        PrintUI print = new PrintUI(htmlFileContent());
        showDialog(print);
    }//GEN-LAST:event_btnPrintActionPerformed
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        observable.resetOBFields();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();
    }//GEN-LAST:event_btnCancelActionPerformed
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        updateOBFields();
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_IMPORT) {
            String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), this);
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_IMPORT)
                mandatoryMessage = mandatoryMessage + new MandatoryCheck().checkMandatory(getClass().getName(), panImpTrDetails);
            
            System.out.println (mandatoryMessage);
            observable.execute (CommonConstants.TOSTATUS_INSERT);
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            observable.execute (CommonConstants.TOSTATUS_UPDATE);
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
            observable.execute (CommonConstants.TOSTATUS_DELETE);
        }

        observable.resetOBFields();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();
    }//GEN-LAST:event_btnSaveActionPerformed
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        callView(ClientConstants.ACTIONTYPE_DELETE);
    }//GEN-LAST:event_btnDeleteActionPerformed
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        callView(ClientConstants.ACTIONTYPE_EDIT);
    }//GEN-LAST:event_btnEditActionPerformed
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        ClientUtil.enableDisable(this, true);
        observable.setActionType (ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();        
    }//GEN-LAST:event_btnNewActionPerformed
    private void calculate() {
        /*if (rdoTransType_Deposit.isSelected()) {
            diaCalc.setTitle("Calculation at Deposits");
        } else if (rdoTransType_Withdrawal.isSelected()) {
            diaCalc.setTitle("Calculation at Withdrawals");
        }*/

        txtDiaTransAmt.setText(txtTransAmount.getText());
        txtDiaCrossCcyRate.setText(txtExchangeRate.getText());
        txtDiaComm.setText(txtCommission.getText());
        txtDiaEquiAmt.setText(
            String.valueOf(
                Double.parseDouble(txtDiaTransAmt.getText()) * 
                Double.parseDouble(txtDiaCrossCcyRate.getText())));
        txtDiaTotAmt.setText(txtTotalAmount.getText());

        /*diaCalc.setSize(270, 220);
        showDialog(diaCalc);*/
    }
    private void btnAcctNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcctNoActionPerformed
        // Add your handling code here:
        callView (ACCTNO);
    }//GEN-LAST:event_btnAcctNoActionPerformed
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        this.dispose();
    }//GEN-LAST:event_exitForm
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAcctNo;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDenomination;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnExport;
    private com.see.truetransact.uicomponent.CButton btnImport;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CSeparator cSeparator1;
    private com.see.truetransact.uicomponent.CSeparator cSeparator2;
    private com.see.truetransact.uicomponent.CComboBox cboConvCurrency;
    private com.see.truetransact.uicomponent.CComboBox cboCustType;
    private com.see.truetransact.uicomponent.CComboBox cboTransCurrency;
    private com.see.truetransact.uicomponent.CLabel lblAcctNo;
    private com.see.truetransact.uicomponent.CLabel lblBranchCode;
    private com.see.truetransact.uicomponent.CLabel lblCommission;
    private com.see.truetransact.uicomponent.CLabel lblConvCurrency;
    private com.see.truetransact.uicomponent.CLabel lblCustGroup;
    private com.see.truetransact.uicomponent.CLabel lblCustRemarlks;
    private com.see.truetransact.uicomponent.CLabel lblCustomerType;
    private com.see.truetransact.uicomponent.CLabel lblDiaComm;
    private com.see.truetransact.uicomponent.CLabel lblDiaCrossCcyRate;
    private com.see.truetransact.uicomponent.CLabel lblDiaEquiAmt;
    private com.see.truetransact.uicomponent.CLabel lblDiaPaidAmt;
    private com.see.truetransact.uicomponent.CLabel lblDiaTotAmt;
    private com.see.truetransact.uicomponent.CLabel lblExchangeRate;
    private com.see.truetransact.uicomponent.CLabel lblFromBranch;
    private com.see.truetransact.uicomponent.CLabel lblImpTrID;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblName;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace11;
    private com.see.truetransact.uicomponent.CLabel lblSpace12;
    private com.see.truetransact.uicomponent.CLabel lblSpace13;
    private com.see.truetransact.uicomponent.CLabel lblSpace14;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTotalAmount;
    private com.see.truetransact.uicomponent.CLabel lblTouristBankDetails;
    private com.see.truetransact.uicomponent.CLabel lblTouristInstrumentDt;
    private com.see.truetransact.uicomponent.CLabel lblTouristInstrumentNo;
    private com.see.truetransact.uicomponent.CLabel lblTouristName;
    private com.see.truetransact.uicomponent.CLabel lblTouristPassportNo;
    private com.see.truetransact.uicomponent.CLabel lblTouristRemarks;
    private com.see.truetransact.uicomponent.CLabel lblTransAmount;
    private com.see.truetransact.uicomponent.CLabel lblTransCurrency;
    private com.see.truetransact.uicomponent.CLabel lblTransId;
    private com.see.truetransact.uicomponent.CLabel lblTransType;
    private com.see.truetransact.uicomponent.CLabel lblType;
    private com.see.truetransact.uicomponent.CLabel lblValueDate;
    private com.see.truetransact.uicomponent.CMenuBar mbrCurrencyExchange;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAcct;
    private com.see.truetransact.uicomponent.CPanel panAcctNo;
    private com.see.truetransact.uicomponent.CPanel panCurrencyExchange;
    private com.see.truetransact.uicomponent.CPanel panDiaCalc;
    private com.see.truetransact.uicomponent.CPanel panImpTrDetails;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTourist;
    private com.see.truetransact.uicomponent.CPanel panTransType;
    private com.see.truetransact.uicomponent.CButtonGroup rdgTransType;
    private com.see.truetransact.uicomponent.CRadioButton rdoTransType_Deposit;
    private com.see.truetransact.uicomponent.CRadioButton rdoTransType_Withdrawal;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptPrint;
    private javax.swing.JSeparator sptProcess;
    private javax.swing.JToolBar tbrCurrencyExchange;
    private com.see.truetransact.uicomponent.CDateField tdtTouristInstrumentDt;
    private com.see.truetransact.uicomponent.CTextField txtAcctNo;
    private com.see.truetransact.uicomponent.CTextField txtBranchCode;
    private com.see.truetransact.uicomponent.CTextField txtCommission;
    private com.see.truetransact.uicomponent.CTextField txtCustGroup;
    private com.see.truetransact.uicomponent.CTextField txtCustRemarks;
    private com.see.truetransact.uicomponent.CTextField txtDiaComm;
    private com.see.truetransact.uicomponent.CTextField txtDiaCrossCcyRate;
    private com.see.truetransact.uicomponent.CTextField txtDiaEquiAmt;
    private com.see.truetransact.uicomponent.CTextField txtDiaTotAmt;
    private com.see.truetransact.uicomponent.CTextField txtDiaTransAmt;
    private com.see.truetransact.uicomponent.CTextField txtExchangeRate;
    private com.see.truetransact.uicomponent.CTextField txtFromBranch;
    private com.see.truetransact.uicomponent.CTextField txtImpTrID;
    private com.see.truetransact.uicomponent.CTextField txtName;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    private com.see.truetransact.uicomponent.CTextField txtTotalAmount;
    private com.see.truetransact.uicomponent.CTextField txtTouristBankDetails;
    private com.see.truetransact.uicomponent.CTextField txtTouristInstrumentNo;
    private com.see.truetransact.uicomponent.CTextField txtTouristName;
    private com.see.truetransact.uicomponent.CTextField txtTouristPassportNo;
    private com.see.truetransact.uicomponent.CTextField txtTouristRemarks;
    private com.see.truetransact.uicomponent.CTextField txtTransAmount;
    private com.see.truetransact.uicomponent.CTextField txtTransId;
    private com.see.truetransact.uicomponent.CTextField txtType;
    private com.see.truetransact.uicomponent.CTextField txtValueDate;
    // End of variables declaration//GEN-END:variables
}