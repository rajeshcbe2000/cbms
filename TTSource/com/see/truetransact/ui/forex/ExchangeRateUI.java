/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ExchangeRateUI.java
 *
 * Created on January 12, 2004, 11:50 AM
 */

package com.see.truetransact.ui.forex;

import java.util.HashMap;
import java.util.ArrayList;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.ui.common.viewall.ViewAll;
import java.util.Observer;
import java.util.Observable;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;

/**
 *
 * @author  amathan
 */
public class ExchangeRateUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer,UIMandatoryField{
    
    private ExchangeRateOB observable;
    private HashMap mandatoryMap;
    private int viewType = -1;
    final int AUTHORIZE=3;
    boolean isFilled = false;
    
    /** Creates new form ExchangeRateUI */
    public ExchangeRateUI() {
        initComponents();
        initStartup();
        panID.setVisible(false);
    }
    
    private void initStartup() {
        lblBuySellType.setVisible(false);
        cboBuySellType.setVisible(false);
        lblConversionCurrency.setVisible(false);
        cboConversionCurrency.setVisible(false);
        lblNotionalRate.setVisible(false);
        txtNotionalRate.setVisible(false);
        setFieldNames();
        internationalize();
        setMaximumLength();
        setObservable();
        initComponentData();
        observable.setCurrencyTable();
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();
        setMandatoryHashMap();
        setHelpMessage();
        observable.resetStatus();
        observable.resetForm();
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
    }
    
    private void initComponentData() {
        cboTransCurrency.setModel(observable.getCbmTransCurrency());
        cboConversionCurrency.setModel(observable.getCbmConversionCurrency());
        cboCustomerType.setModel(observable.getCbmCustomerType());
//        cboCurrBuying.setModel(observable.getCbmCurrBuying() );
//        cboCurrSelling.setModel(observable.getCbmCurrSelling());
    }
    
    private void setObservable() {
        //observable = new ExchangeRateOB();
        
        /* Implementing Singleton pattern */
        observable = ExchangeRateOB.getInstance();
        observable.addObserver(this);
    }
    
    /* Auto Generated Method - setFieldNames()
       This method assigns name for all the components.
       Other functions are working based on this name. */
    private void setFieldNames() {
	btnCancel.setName("btnCancel");
	btnClose.setName("btnClose");
	btnDelete.setName("btnDelete");
	btnEdit.setName("btnEdit");
	btnNew.setName("btnNew");
	btnPrint.setName("btnPrint");
	btnSave.setName("btnSave");
	cboBuySellType.setName("cboBuySellType");
	cboConversionCurrency.setName("cboConversionCurrency");
	cboCustomerType.setName("cboCustomerType");
	cboTransCurrency.setName("cboTransCurrency");
	chkPreferred.setName("chkPreferred");
	lblBillBuying.setName("lblBillBuying");
	lblBillBuyingComm.setName("lblBillBuyingComm");
	lblBillBuyingCommPer.setName("lblBillBuyingCommPer");
	lblBillBuyingPer.setName("lblBillBuyingPer");
	lblBillBuyingSlab.setName("lblBillBuyingSlab");
	lblBillSelling.setName("lblBillSelling");
	lblBillSellingPer.setName("lblBillSellingPer");
	lblBuySellType.setName("lblBuySellType");
	lblBuyingPer.setName("lblBuyingPer");
	lblConversionCurrency.setName("lblConversionCurrency");
	lblCurrBuying.setName("lblCurrBuying");
	lblCurrBuyingComm.setName("lblCurrBuyingComm");
	lblCurrBuyingCommPer.setName("lblCurrBuyingCommPer");
	lblCurrBuyingSlab.setName("lblCurrBuyingSlab");
	lblCurrSelling.setName("lblCurrSelling");
	lblCustomerType.setName("lblCustomerType");
	lblDDBuying.setName("lblDDBuying");
	lblDDBuyingComm.setName("lblDDBuyingComm");
	lblDDBuyingCommPer.setName("lblDDBuyingCommPer");
	lblDDBuyingPer.setName("lblDDBuyingPer");
	lblDDBuyingSlab.setName("lblDDBuyingSlab");
	lblDDSelling.setName("lblDDSelling");
	lblDDSellingPer.setName("lblDDSellingPer");
	lblExchangeId.setName("lblExchangeId");
	lblMsg.setName("lblMsg");
	lblNotionalRate.setName("lblNotionalRate");
	lblPreferred.setName("lblPreferred");
	lblRemarks.setName("lblRemarks");
	lblSellingPer.setName("lblSellingPer");
	lblSpace1.setName("lblSpace1");
	lblSpace2.setName("lblSpace2");
	lblSpace3.setName("lblSpace3");
	lblStatus.setName("lblStatus");
	lblTCBuying.setName("lblTCBuying");
	lblTCBuyingComm.setName("lblTCBuyingComm");
	lblTCBuyingCommPer.setName("lblTCBuyingCommPer");
	lblTCBuyingPer.setName("lblTCBuyingPer");
	lblTCBuyingSlab.setName("lblTCBuyingSlab");
	lblTCSelling.setName("lblTCSelling");
	lblTCSellingPer.setName("lblTCSellingPer");
	lblTTBuying.setName("lblTTBuying");
	lblTTBuyingComm.setName("lblTTBuyingComm");
	lblTTBuyingCommPer.setName("lblTTBuyingCommPer");
	lblTTBuyingPer.setName("lblTTBuyingPer");
	lblTTBuyingSlab.setName("lblTTBuyingSlab");
	lblTTSelling.setName("lblTTSelling");
	lblTTSellingPer.setName("lblTTSellingPer");
	mbrMain.setName("mbrMain");
	panBillBuying.setName("panBillBuying");
	panBillSelling.setName("panBillSelling");
	panBuying.setName("panBuying");
	panCurrencyTbl.setName("panCurrencyTbl");
	panDDBuying.setName("panDDBuying");
	panDDSelling.setName("panDDSelling");
	panExchangeRate.setName("panExchangeRate");
	panID.setName("panID");
	panOthers.setName("panOthers");
	panSelling.setName("panSelling");
	panStatus.setName("panStatus");
	panTCBuying.setName("panTCBuying");
	panTCSelling.setName("panTCSelling");
	panTTBuying.setName("panTTBuying");
	panTTSelling.setName("panTTSelling");
	sptBill.setName("sptBill");
	sptCurr.setName("sptCurr");
	sptDD.setName("sptDD");
	sptTC.setName("sptTC");
	sptTT.setName("sptTT");
	srpCurrencyTbl.setName("srpCurrencyTbl");
	tblCurrency.setName("tblCurrency");
	txtBillBuying.setName("txtBillBuying");
	txtBillBuyingComm.setName("txtBillBuyingComm");
	txtBillBuyingSlab.setName("txtBillBuyingSlab");
	txtBillSelling.setName("txtBillSelling");
	txtCurrBuying.setName("txtCurrBuying");
	txtCurrBuyingComm.setName("txtCurrBuyingComm");
	txtCurrBuyingSlab.setName("txtCurrBuyingSlab");
	txtCurrSelling.setName("txtCurrSelling");
	txtDDBuying.setName("txtDDBuying");
	txtDDBuyingComm.setName("txtDDBuyingComm");
	txtDDBuyingSlab.setName("txtDDBuyingSlab");
	txtDDSelling.setName("txtDDSelling");
	txtExchangeId.setName("txtExchangeId");
	txtNotionalRate.setName("txtNotionalRate");
	txtRemarks.setName("txtRemarks");
	txtTCBuying.setName("txtTCBuying");
	txtTCBuyingComm.setName("txtTCBuyingComm");
	txtTCBuyingSlab.setName("txtTCBuyingSlab");
	txtTCSelling.setName("txtTCSelling");
	txtTTBuying.setName("txtTTBuying");
	txtTTBuyingComm.setName("txtTTBuyingComm");
	txtTTBuyingSlab.setName("txtTTBuyingSlab");
	txtTTSelling.setName("txtTTSelling");
	txtTransCurrency.setName("txtTransCurrency");
    }

    /* Auto Generated Method - internationalize()
       This method used to assign display texts from 
       the Resource Bundle File. */
    private void internationalize() {
        final ExchangeRateRB resourceBundle = new ExchangeRateRB();
	btnClose.setText(resourceBundle.getString("btnClose"));
	lblNotionalRate.setText(resourceBundle.getString("lblNotionalRate"));
	lblDDBuying.setText(resourceBundle.getString("lblDDBuying"));
	lblBillBuyingComm.setText(resourceBundle.getString("lblBillBuyingComm"));
	lblDDBuyingCommPer.setText(resourceBundle.getString("lblDDBuyingCommPer"));
	lblMsg.setText(resourceBundle.getString("lblMsg"));
	lblTTBuyingPer.setText(resourceBundle.getString("lblTTBuyingPer"));
	lblBillSelling.setText(resourceBundle.getString("lblBillSelling"));
	lblTCBuying.setText(resourceBundle.getString("lblTCBuying"));
	txtTransCurrency.setText(resourceBundle.getString("txtTransCurrency"));
	lblTCBuyingPer.setText(resourceBundle.getString("lblTCBuyingPer"));
	lblExchangeId.setText(resourceBundle.getString("lblExchangeId"));
	lblTCBuyingComm.setText(resourceBundle.getString("lblTCBuyingComm"));
	lblSpace2.setText(resourceBundle.getString("lblSpace2"));
	lblBillBuyingSlab.setText(resourceBundle.getString("lblBillBuyingSlab"));
	lblSpace3.setText(resourceBundle.getString("lblSpace3"));
	lblBillBuyingCommPer.setText(resourceBundle.getString("lblBillBuyingCommPer"));
	lblSpace1.setText(resourceBundle.getString("lblSpace1"));
	lblTCBuyingSlab.setText(resourceBundle.getString("lblTCBuyingSlab"));
	lblDDBuyingSlab.setText(resourceBundle.getString("lblDDBuyingSlab"));
	lblCurrBuying.setText(resourceBundle.getString("lblCurrBuying"));
	btnEdit.setText(resourceBundle.getString("btnEdit"));
	lblBuySellType.setText(resourceBundle.getString("lblBuySellType"));
	lblCurrBuyingCommPer.setText(resourceBundle.getString("lblCurrBuyingCommPer"));
	lblDDSellingPer.setText(resourceBundle.getString("lblDDSellingPer"));
	lblTCSelling.setText(resourceBundle.getString("lblTCSelling"));
	lblBuyingPer.setText(resourceBundle.getString("lblBuyingPer"));
	lblPreferred.setText(resourceBundle.getString("lblPreferred"));
	btnPrint.setText(resourceBundle.getString("btnPrint"));
	chkPreferred.setText(resourceBundle.getString("chkPreferred"));
	lblCurrBuyingComm.setText(resourceBundle.getString("lblCurrBuyingComm"));
	lblDDSelling.setText(resourceBundle.getString("lblDDSelling"));
	lblDDBuyingPer.setText(resourceBundle.getString("lblDDBuyingPer"));
	lblBillBuyingPer.setText(resourceBundle.getString("lblBillBuyingPer"));
	lblBillBuying.setText(resourceBundle.getString("lblBillBuying"));
	btnSave.setText(resourceBundle.getString("btnSave"));
	lblTTBuyingCommPer.setText(resourceBundle.getString("lblTTBuyingCommPer"));
	lblConversionCurrency.setText(resourceBundle.getString("lblConversionCurrency"));
	lblTTSelling.setText(resourceBundle.getString("lblTTSelling"));
	lblStatus.setText(resourceBundle.getString("lblStatus"));
	lblCustomerType.setText(resourceBundle.getString("lblCustomerType"));
	lblTTBuying.setText(resourceBundle.getString("lblTTBuying"));
	lblRemarks.setText(resourceBundle.getString("lblRemarks"));
	btnDelete.setText(resourceBundle.getString("btnDelete"));
	lblBillSellingPer.setText(resourceBundle.getString("lblBillSellingPer"));
	lblCurrBuyingSlab.setText(resourceBundle.getString("lblCurrBuyingSlab"));
	lblTTSellingPer.setText(resourceBundle.getString("lblTTSellingPer"));
	lblTCBuyingCommPer.setText(resourceBundle.getString("lblTCBuyingCommPer"));
	btnNew.setText(resourceBundle.getString("btnNew"));
	lblTTBuyingSlab.setText(resourceBundle.getString("lblTTBuyingSlab"));
	lblCurrSelling.setText(resourceBundle.getString("lblCurrSelling"));
	lblSellingPer.setText(resourceBundle.getString("lblSellingPer"));
	lblDDBuyingComm.setText(resourceBundle.getString("lblDDBuyingComm"));
	btnCancel.setText(resourceBundle.getString("btnCancel"));
	lblTTBuyingComm.setText(resourceBundle.getString("lblTTBuyingComm"));
	lblTCSellingPer.setText(resourceBundle.getString("lblTCSellingPer"));
}

    /* Auto Generated Method - setMandatoryHashMap()
       This method list out all the Input Fields available in the UI.
       It needs a class level HashMap variable mandatoryMap. */
	public void setMandatoryHashMap() {
		mandatoryMap = new HashMap();
		mandatoryMap.put("txtExchangeId", new Boolean(true));
		mandatoryMap.put("cboTransCurrency", new Boolean(true));
		mandatoryMap.put("cboConversionCurrency", new Boolean(true));
		mandatoryMap.put("cboCustomerType", new Boolean(true));
		mandatoryMap.put("chkPreferred", new Boolean(true));
		mandatoryMap.put("txtRemarks", new Boolean(true));
		mandatoryMap.put("txtCurrBuying", new Boolean(true));
		mandatoryMap.put("txtCurrSelling", new Boolean(true));
		mandatoryMap.put("cboBuySellType", new Boolean(true));
		mandatoryMap.put("txtNotionalRate", new Boolean(true));
		mandatoryMap.put("txtTTBuying", new Boolean(true));
		mandatoryMap.put("txtBillBuying", new Boolean(true));
		mandatoryMap.put("txtBillSelling", new Boolean(true));
		mandatoryMap.put("txtDDBuying", new Boolean(true));
		mandatoryMap.put("txtDDSelling", new Boolean(true));
		mandatoryMap.put("txtTCBuying", new Boolean(true));
		mandatoryMap.put("txtTCSelling", new Boolean(true));
		mandatoryMap.put("txtTTSelling", new Boolean(true));
		mandatoryMap.put("txtTTBuyingSlab", new Boolean(true));
		mandatoryMap.put("txtTTBuyingComm", new Boolean(true));
		mandatoryMap.put("txtTCBuyingComm", new Boolean(true));
		mandatoryMap.put("txtTCBuyingSlab", new Boolean(true));
		mandatoryMap.put("txtBillBuyingSlab", new Boolean(true));
		mandatoryMap.put("txtBillBuyingComm", new Boolean(true));
		mandatoryMap.put("txtDDBuyingSlab", new Boolean(true));
		mandatoryMap.put("txtDDBuyingComm", new Boolean(true));
		mandatoryMap.put("txtCurrBuyingSlab", new Boolean(true));
		mandatoryMap.put("txtCurrBuyingComm", new Boolean(true));
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
		txtExchangeId.setText(observable.getTxtExchangeId()); 
		cboTransCurrency.setSelectedItem(observable.getCboTransCurrency()); 
//		cboConversionCurrency.setSelectedItem(observable.getCboConversionCurrency()); 
		cboCustomerType.setSelectedItem(observable.getCboCustomerType()); 
		chkPreferred.setSelected(observable.getChkPreferred()); 
		txtRemarks.setText(observable.getTxtRemarks()); 
		txtCurrBuying.setText(observable.getTxtCurrBuying()); 
		txtCurrSelling.setText(observable.getTxtCurrSelling()); 
		cboBuySellType.setSelectedItem(observable.getCboBuySellType()); 
		txtNotionalRate.setText(observable.getTxtNotionalRate()); 
		txtTTBuying.setText(observable.getTxtTTBuying()); 
		txtBillBuying.setText(observable.getTxtBillBuying()); 
		txtBillSelling.setText(observable.getTxtBillSelling()); 
		txtDDBuying.setText(observable.getTxtDDBuying()); 
		txtDDSelling.setText(observable.getTxtDDSelling()); 
		txtTCBuying.setText(observable.getTxtTCBuying()); 
		txtTCSelling.setText(observable.getTxtTCSelling()); 
		txtTTSelling.setText(observable.getTxtTTSelling()); 
		txtTTBuyingSlab.setText(observable.getTxtTTBuyingSlab()); 
		txtTTBuyingComm.setText(observable.getTxtTTBuyingComm()); 
		txtTCBuyingComm.setText(observable.getTxtTCBuyingComm()); 
		txtTCBuyingSlab.setText(observable.getTxtTCBuyingSlab()); 
		txtBillBuyingSlab.setText(observable.getTxtBillBuyingSlab()); 
		txtBillBuyingComm.setText(observable.getTxtBillBuyingComm()); 
		txtDDBuyingSlab.setText(observable.getTxtDDBuyingSlab()); 
		txtDDBuyingComm.setText(observable.getTxtDDBuyingComm()); 
		txtCurrBuyingSlab.setText(observable.getTxtCurrBuyingSlab()); 
		txtCurrBuyingComm.setText(observable.getTxtCurrBuyingComm()); 
                // UNGENERATED CODE
                lblStatus.setText(observable.getLblStatus());
                tblCurrency.setModel(observable.getTblCurrency());
                
	}

/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI. 
   It updates the OB with UI data.*/
	public void updateOBFields() {
		observable.setTxtExchangeId(txtExchangeId.getText()); 
		observable.setCboTransCurrency((String) cboTransCurrency.getSelectedItem()); 
//		observable.setCboConversionCurrency((String) cboConversionCurrency.getSelectedItem()); 
		observable.setCboCustomerType((String) cboCustomerType.getSelectedItem()); 
		observable.setChkPreferred(chkPreferred.isSelected()); 
		observable.setTxtRemarks(txtRemarks.getText()); 
		observable.setTxtCurrBuying(txtCurrBuying.getText()); 
		observable.setTxtCurrSelling(txtCurrSelling.getText()); 
		observable.setCboBuySellType((String) cboBuySellType.getSelectedItem()); 
		observable.setTxtNotionalRate(txtNotionalRate.getText()); 
		observable.setTxtTTBuying(txtTTBuying.getText()); 
		observable.setTxtBillBuying(txtBillBuying.getText()); 
		observable.setTxtBillSelling(txtBillSelling.getText()); 
		observable.setTxtDDBuying(txtDDBuying.getText()); 
		observable.setTxtDDSelling(txtDDSelling.getText()); 
		observable.setTxtTCBuying(txtTCBuying.getText()); 
		observable.setTxtTCSelling(txtTCSelling.getText()); 
		observable.setTxtTTSelling(txtTTSelling.getText()); 
		observable.setTxtTTBuyingSlab(txtTTBuyingSlab.getText()); 
		observable.setTxtTTBuyingComm(txtTTBuyingComm.getText()); 
		observable.setTxtTCBuyingComm(txtTCBuyingComm.getText()); 
		observable.setTxtTCBuyingSlab(txtTCBuyingSlab.getText()); 
		observable.setTxtBillBuyingSlab(txtBillBuyingSlab.getText()); 
		observable.setTxtBillBuyingComm(txtBillBuyingComm.getText()); 
		observable.setTxtDDBuyingSlab(txtDDBuyingSlab.getText()); 
		observable.setTxtDDBuyingComm(txtDDBuyingComm.getText()); 
		observable.setTxtCurrBuyingSlab(txtCurrBuyingSlab.getText()); 
		observable.setTxtCurrBuyingComm(txtCurrBuyingComm.getText()); 
                /** UNGENERATED CODE **/
                observable.setTblCurrency((EnhancedTableModel)tblCurrency.getModel());
	}
/* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle 
   object. Help display Label name should be lblMsg. */
	public void setHelpMessage() {
                final ExchangeRateMRB objMandatoryRB = new ExchangeRateMRB();
		txtExchangeId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtExchangeId"));
		cboTransCurrency.setHelpMessage(lblMsg, objMandatoryRB.getString("cboTransCurrency"));
		cboConversionCurrency.setHelpMessage(lblMsg, objMandatoryRB.getString("cboConversionCurrency"));
		cboCustomerType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCustomerType"));
		chkPreferred.setHelpMessage(lblMsg, objMandatoryRB.getString("chkPreferred"));
		txtRemarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRemarks"));
		txtCurrBuying.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCurrBuying"));
		txtCurrSelling.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCurrSelling"));
		cboBuySellType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboBuySellType"));
		txtNotionalRate.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNotionalRate"));
		txtTTBuying.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTTBuying"));
		txtBillBuying.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBillBuying"));
		txtBillSelling.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBillSelling"));
		txtDDBuying.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDDBuying"));
		txtDDSelling.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDDSelling"));
		txtTCBuying.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTCBuying"));
		txtTCSelling.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTCSelling"));
		txtTTSelling.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTTSelling"));
		txtTTBuyingSlab.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTTBuyingSlab"));
		txtTTBuyingComm.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTTBuyingComm"));
		txtTCBuyingComm.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTCBuyingComm"));
		txtTCBuyingSlab.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTCBuyingSlab"));
		txtBillBuyingSlab.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBillBuyingSlab"));
		txtBillBuyingComm.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBillBuyingComm"));
		txtDDBuyingSlab.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDDBuyingSlab"));
		txtDDBuyingComm.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDDBuyingComm"));
		txtCurrBuyingSlab.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCurrBuyingSlab"));
		txtCurrBuyingComm.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCurrBuyingComm"));
	}
    
    public void setMaximumLength(){
//        txtBillBuying.setMaxLength(5);
        txtBillBuying.setValidation(new NumericValidation(2,2));
//        txtDDBuying.setMaxLength(5);
        txtDDBuying.setValidation(new NumericValidation(2,2));
//        txtTTBuying.setMaxLength(5);
        txtTTBuying.setValidation(new NumericValidation(2,2));
//        txtCurrBuying.setMaxLength(5);
        txtCurrBuying.setValidation(new NumericValidation(2,2));
//        txtTCBuying.setMaxLength(5);
        txtTCBuying.setValidation(new NumericValidation(2,2));        
//        txtDDSelling.setMaxLength(5);
        txtDDSelling.setValidation(new NumericValidation(2,2));
//        txtTTSelling.setMaxLength(5);
        txtTTSelling.setValidation(new NumericValidation(2,2));
//        txtCurrSelling.setMaxLength(5);
        txtCurrSelling.setValidation(new NumericValidation(2,2));
//        txtTCSelling.setMaxLength(5);
        txtTCSelling.setValidation(new NumericValidation(2,2));
//        txtBillSelling.setMaxLength(5);
        txtBillSelling.setValidation(new NumericValidation(2,2));
//        txtTTBuyingComm.setMaxLength(5);
        txtTTBuyingComm.setValidation(new NumericValidation(2,2));
//        txtBillBuyingComm.setMaxLength(5);
        txtBillBuyingComm.setValidation(new NumericValidation(2,2));
//        txtDDBuyingComm.setMaxLength(5);
        txtDDBuyingComm.setValidation(new NumericValidation(2,2));
//        txtCurrBuyingComm.setMaxLength(5);
        txtCurrBuyingComm.setValidation(new NumericValidation(2,2));
//        txtTCBuyingComm.setMaxLength(5);
        txtTCBuyingComm.setValidation(new NumericValidation(2,2));
//        txtNotionalRate.setMaxLength(5);
        txtNotionalRate.setValidation(new NumericValidation(2,2));
//        txtTTBuyingSlab.setMaxLength(16);
        txtTTBuyingSlab.setValidation(new CurrencyValidation(14,2));
//        txtBillBuyingSlab.setMaxLength(16);
        txtBillBuyingSlab.setValidation(new CurrencyValidation(14,2));
//        txtDDBuyingSlab.setMaxLength(16);
        txtDDBuyingSlab.setValidation(new CurrencyValidation(14,2));
//        txtCurrBuyingSlab.setMaxLength(16);
        txtCurrBuyingSlab.setValidation(new CurrencyValidation(14,2));
//        txtTCBuyingSlab.setMaxLength(16);
        txtTCBuyingSlab.setValidation(new CurrencyValidation(14,2));
        txtRemarks.setMaxLength(256);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panExchangeRate = new com.see.truetransact.uicomponent.CPanel();
        panID = new com.see.truetransact.uicomponent.CPanel();
        lblExchangeId = new com.see.truetransact.uicomponent.CLabel();
        txtExchangeId = new com.see.truetransact.uicomponent.CTextField();
        panOthers = new com.see.truetransact.uicomponent.CPanel();
        txtTransCurrency = new com.see.truetransact.uicomponent.CLabel();
        lblConversionCurrency = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerType = new com.see.truetransact.uicomponent.CLabel();
        cboTransCurrency = new com.see.truetransact.uicomponent.CComboBox();
        cboConversionCurrency = new com.see.truetransact.uicomponent.CComboBox();
        cboCustomerType = new com.see.truetransact.uicomponent.CComboBox();
        lblCurrBuying = new com.see.truetransact.uicomponent.CLabel();
        lblCurrSelling = new com.see.truetransact.uicomponent.CLabel();
        lblPreferred = new com.see.truetransact.uicomponent.CLabel();
        chkPreferred = new com.see.truetransact.uicomponent.CCheckBox();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        panBuying = new com.see.truetransact.uicomponent.CPanel();
        lblBuyingPer = new com.see.truetransact.uicomponent.CLabel();
        txtCurrBuying = new com.see.truetransact.uicomponent.CTextField();
        panSelling = new com.see.truetransact.uicomponent.CPanel();
        lblSellingPer = new com.see.truetransact.uicomponent.CLabel();
        txtCurrSelling = new com.see.truetransact.uicomponent.CTextField();
        lblBuySellType = new com.see.truetransact.uicomponent.CLabel();
        cboBuySellType = new com.see.truetransact.uicomponent.CComboBox();
        lblBillBuying = new com.see.truetransact.uicomponent.CLabel();
        lblBillSelling = new com.see.truetransact.uicomponent.CLabel();
        lblDDBuying = new com.see.truetransact.uicomponent.CLabel();
        lblDDSelling = new com.see.truetransact.uicomponent.CLabel();
        lblTCBuying = new com.see.truetransact.uicomponent.CLabel();
        lblTCSelling = new com.see.truetransact.uicomponent.CLabel();
        lblTTBuying = new com.see.truetransact.uicomponent.CLabel();
        lblNotionalRate = new com.see.truetransact.uicomponent.CLabel();
        txtNotionalRate = new com.see.truetransact.uicomponent.CTextField();
        panTTBuying = new com.see.truetransact.uicomponent.CPanel();
        txtTTBuying = new com.see.truetransact.uicomponent.CTextField();
        lblTTBuyingPer = new com.see.truetransact.uicomponent.CLabel();
        panBillBuying = new com.see.truetransact.uicomponent.CPanel();
        txtBillBuying = new com.see.truetransact.uicomponent.CTextField();
        lblBillBuyingPer = new com.see.truetransact.uicomponent.CLabel();
        panBillSelling = new com.see.truetransact.uicomponent.CPanel();
        txtBillSelling = new com.see.truetransact.uicomponent.CTextField();
        lblBillSellingPer = new com.see.truetransact.uicomponent.CLabel();
        panDDBuying = new com.see.truetransact.uicomponent.CPanel();
        txtDDBuying = new com.see.truetransact.uicomponent.CTextField();
        lblDDBuyingPer = new com.see.truetransact.uicomponent.CLabel();
        panDDSelling = new com.see.truetransact.uicomponent.CPanel();
        txtDDSelling = new com.see.truetransact.uicomponent.CTextField();
        lblDDSellingPer = new com.see.truetransact.uicomponent.CLabel();
        panTCBuying = new com.see.truetransact.uicomponent.CPanel();
        txtTCBuying = new com.see.truetransact.uicomponent.CTextField();
        lblTCBuyingPer = new com.see.truetransact.uicomponent.CLabel();
        panTCSelling = new com.see.truetransact.uicomponent.CPanel();
        txtTCSelling = new com.see.truetransact.uicomponent.CTextField();
        lblTCSellingPer = new com.see.truetransact.uicomponent.CLabel();
        lblTTSelling = new com.see.truetransact.uicomponent.CLabel();
        panTTSelling = new com.see.truetransact.uicomponent.CPanel();
        txtTTSelling = new com.see.truetransact.uicomponent.CTextField();
        lblTTSellingPer = new com.see.truetransact.uicomponent.CLabel();
        lblTCBuyingSlab = new com.see.truetransact.uicomponent.CLabel();
        txtTTBuyingSlab = new com.see.truetransact.uicomponent.CTextField();
        lblTCBuyingComm = new com.see.truetransact.uicomponent.CLabel();
        txtTTBuyingComm = new com.see.truetransact.uicomponent.CTextField();
        lblTTBuyingCommPer = new com.see.truetransact.uicomponent.CLabel();
        sptTC = new com.see.truetransact.uicomponent.CSeparator();
        lblTCBuyingCommPer = new com.see.truetransact.uicomponent.CLabel();
        txtTCBuyingComm = new com.see.truetransact.uicomponent.CTextField();
        lblTTBuyingComm = new com.see.truetransact.uicomponent.CLabel();
        txtTCBuyingSlab = new com.see.truetransact.uicomponent.CTextField();
        lblTTBuyingSlab = new com.see.truetransact.uicomponent.CLabel();
        sptTT = new com.see.truetransact.uicomponent.CSeparator();
        lblBillBuyingSlab = new com.see.truetransact.uicomponent.CLabel();
        txtBillBuyingSlab = new com.see.truetransact.uicomponent.CTextField();
        lblBillBuyingComm = new com.see.truetransact.uicomponent.CLabel();
        txtBillBuyingComm = new com.see.truetransact.uicomponent.CTextField();
        lblBillBuyingCommPer = new com.see.truetransact.uicomponent.CLabel();
        sptBill = new com.see.truetransact.uicomponent.CSeparator();
        lblDDBuyingSlab = new com.see.truetransact.uicomponent.CLabel();
        txtDDBuyingSlab = new com.see.truetransact.uicomponent.CTextField();
        lblDDBuyingComm = new com.see.truetransact.uicomponent.CLabel();
        txtDDBuyingComm = new com.see.truetransact.uicomponent.CTextField();
        lblDDBuyingCommPer = new com.see.truetransact.uicomponent.CLabel();
        sptDD = new com.see.truetransact.uicomponent.CSeparator();
        lblCurrBuyingSlab = new com.see.truetransact.uicomponent.CLabel();
        txtCurrBuyingSlab = new com.see.truetransact.uicomponent.CTextField();
        lblCurrBuyingComm = new com.see.truetransact.uicomponent.CLabel();
        txtCurrBuyingComm = new com.see.truetransact.uicomponent.CTextField();
        lblCurrBuyingCommPer = new com.see.truetransact.uicomponent.CLabel();
        sptCurr = new com.see.truetransact.uicomponent.CSeparator();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        cLabel2 = new com.see.truetransact.uicomponent.CLabel();
        cLabel3 = new com.see.truetransact.uicomponent.CLabel();
        cLabel4 = new com.see.truetransact.uicomponent.CLabel();
        cLabel5 = new com.see.truetransact.uicomponent.CLabel();
        cLabel6 = new com.see.truetransact.uicomponent.CLabel();
        cLabel7 = new com.see.truetransact.uicomponent.CLabel();
        cLabel8 = new com.see.truetransact.uicomponent.CLabel();
        cLabel9 = new com.see.truetransact.uicomponent.CLabel();
        cLabel10 = new com.see.truetransact.uicomponent.CLabel();
        panCurrencyTbl = new com.see.truetransact.uicomponent.CPanel();
        srpCurrencyTbl = new com.see.truetransact.uicomponent.CScrollPane();
        tblCurrency = new com.see.truetransact.uicomponent.CTable();
        tbrHead = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
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
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace14 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace15 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace16 = new com.see.truetransact.uicomponent.CLabel();
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
        sptView = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Exchange Rate");
        setPreferredSize(new java.awt.Dimension(640, 580));

        panExchangeRate.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panExchangeRate.setMinimumSize(new java.awt.Dimension(535, 505));
        panExchangeRate.setPreferredSize(new java.awt.Dimension(635, 595));
        panExchangeRate.setLayout(new java.awt.GridBagLayout());

        panID.setLayout(new java.awt.GridBagLayout());

        lblExchangeId.setText("Exchange Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panID.add(lblExchangeId, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panID.add(txtExchangeId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panExchangeRate.add(panID, gridBagConstraints);

        panOthers.setMinimumSize(new java.awt.Dimension(405, 520));
        panOthers.setPreferredSize(new java.awt.Dimension(405, 580));
        panOthers.setLayout(new java.awt.GridBagLayout());

        txtTransCurrency.setText("Transaction Currency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthers.add(txtTransCurrency, gridBagConstraints);

        lblConversionCurrency.setText("Conversion Currency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthers.add(lblConversionCurrency, gridBagConstraints);

        lblCustomerType.setText("Customer Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthers.add(lblCustomerType, gridBagConstraints);

        cboTransCurrency.setMinimumSize(new java.awt.Dimension(150, 21));
        cboTransCurrency.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthers.add(cboTransCurrency, gridBagConstraints);

        cboConversionCurrency.setMinimumSize(new java.awt.Dimension(150, 21));
        cboConversionCurrency.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthers.add(cboConversionCurrency, gridBagConstraints);

        cboCustomerType.setMinimumSize(new java.awt.Dimension(150, 21));
        cboCustomerType.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthers.add(cboCustomerType, gridBagConstraints);

        lblCurrBuying.setText("Currency Buying");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthers.add(lblCurrBuying, gridBagConstraints);

        lblCurrSelling.setText("Currency Selling");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthers.add(lblCurrSelling, gridBagConstraints);

        lblPreferred.setText("Preferred");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthers.add(lblPreferred, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthers.add(chkPreferred, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthers.add(lblRemarks, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthers.add(txtRemarks, gridBagConstraints);

        panBuying.setMinimumSize(new java.awt.Dimension(50, 21));
        panBuying.setPreferredSize(new java.awt.Dimension(50, 21));
        panBuying.setLayout(new java.awt.GridBagLayout());

        lblBuyingPer.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panBuying.add(lblBuyingPer, gridBagConstraints);

        txtCurrBuying.setMinimumSize(new java.awt.Dimension(35, 21));
        txtCurrBuying.setPreferredSize(new java.awt.Dimension(35, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBuying.add(txtCurrBuying, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 4, 4);
        panOthers.add(panBuying, gridBagConstraints);

        panSelling.setMinimumSize(new java.awt.Dimension(50, 21));
        panSelling.setPreferredSize(new java.awt.Dimension(50, 21));
        panSelling.setLayout(new java.awt.GridBagLayout());

        lblSellingPer.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panSelling.add(lblSellingPer, gridBagConstraints);

        txtCurrSelling.setMinimumSize(new java.awt.Dimension(35, 21));
        txtCurrSelling.setPreferredSize(new java.awt.Dimension(35, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSelling.add(txtCurrSelling, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 4);
        panOthers.add(panSelling, gridBagConstraints);

        lblBuySellType.setText("Buying/Selling Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthers.add(lblBuySellType, gridBagConstraints);

        cboBuySellType.setMinimumSize(new java.awt.Dimension(150, 21));
        cboBuySellType.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthers.add(cboBuySellType, gridBagConstraints);

        lblBillBuying.setText("Bill Buying");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthers.add(lblBillBuying, gridBagConstraints);

        lblBillSelling.setText("Bill Selling");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthers.add(lblBillSelling, gridBagConstraints);

        lblDDBuying.setText("DD Buying");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthers.add(lblDDBuying, gridBagConstraints);

        lblDDSelling.setText("DD Selling");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthers.add(lblDDSelling, gridBagConstraints);

        lblTCBuying.setText("TC Buying");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthers.add(lblTCBuying, gridBagConstraints);

        lblTCSelling.setText("TC Selling");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthers.add(lblTCSelling, gridBagConstraints);

        lblTTBuying.setText("TT Buying");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthers.add(lblTTBuying, gridBagConstraints);

        lblNotionalRate.setText("Notional Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthers.add(lblNotionalRate, gridBagConstraints);

        txtNotionalRate.setMinimumSize(new java.awt.Dimension(50, 21));
        txtNotionalRate.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 4, 4);
        panOthers.add(txtNotionalRate, gridBagConstraints);

        panTTBuying.setMinimumSize(new java.awt.Dimension(50, 21));
        panTTBuying.setPreferredSize(new java.awt.Dimension(50, 21));
        panTTBuying.setLayout(new java.awt.GridBagLayout());

        txtTTBuying.setMinimumSize(new java.awt.Dimension(35, 21));
        txtTTBuying.setPreferredSize(new java.awt.Dimension(35, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panTTBuying.add(txtTTBuying, gridBagConstraints);

        lblTTBuyingPer.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panTTBuying.add(lblTTBuyingPer, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 4, 4);
        panOthers.add(panTTBuying, gridBagConstraints);

        panBillBuying.setMinimumSize(new java.awt.Dimension(50, 21));
        panBillBuying.setPreferredSize(new java.awt.Dimension(50, 21));
        panBillBuying.setLayout(new java.awt.GridBagLayout());

        txtBillBuying.setMinimumSize(new java.awt.Dimension(35, 21));
        txtBillBuying.setPreferredSize(new java.awt.Dimension(35, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBillBuying.add(txtBillBuying, gridBagConstraints);

        lblBillBuyingPer.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panBillBuying.add(lblBillBuyingPer, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 4, 4);
        panOthers.add(panBillBuying, gridBagConstraints);

        panBillSelling.setMinimumSize(new java.awt.Dimension(50, 21));
        panBillSelling.setPreferredSize(new java.awt.Dimension(50, 21));
        panBillSelling.setLayout(new java.awt.GridBagLayout());

        txtBillSelling.setMinimumSize(new java.awt.Dimension(35, 21));
        txtBillSelling.setPreferredSize(new java.awt.Dimension(35, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBillSelling.add(txtBillSelling, gridBagConstraints);

        lblBillSellingPer.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panBillSelling.add(lblBillSellingPer, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 4);
        panOthers.add(panBillSelling, gridBagConstraints);

        panDDBuying.setMinimumSize(new java.awt.Dimension(50, 21));
        panDDBuying.setPreferredSize(new java.awt.Dimension(50, 21));
        panDDBuying.setLayout(new java.awt.GridBagLayout());

        txtDDBuying.setMinimumSize(new java.awt.Dimension(35, 21));
        txtDDBuying.setPreferredSize(new java.awt.Dimension(35, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDDBuying.add(txtDDBuying, gridBagConstraints);

        lblDDBuyingPer.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panDDBuying.add(lblDDBuyingPer, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 4, 4);
        panOthers.add(panDDBuying, gridBagConstraints);

        panDDSelling.setMinimumSize(new java.awt.Dimension(50, 21));
        panDDSelling.setPreferredSize(new java.awt.Dimension(50, 21));
        panDDSelling.setLayout(new java.awt.GridBagLayout());

        txtDDSelling.setMinimumSize(new java.awt.Dimension(35, 21));
        txtDDSelling.setPreferredSize(new java.awt.Dimension(35, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDDSelling.add(txtDDSelling, gridBagConstraints);

        lblDDSellingPer.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panDDSelling.add(lblDDSellingPer, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 4);
        panOthers.add(panDDSelling, gridBagConstraints);

        panTCBuying.setMinimumSize(new java.awt.Dimension(50, 21));
        panTCBuying.setPreferredSize(new java.awt.Dimension(50, 21));
        panTCBuying.setLayout(new java.awt.GridBagLayout());

        txtTCBuying.setMinimumSize(new java.awt.Dimension(35, 21));
        txtTCBuying.setPreferredSize(new java.awt.Dimension(35, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panTCBuying.add(txtTCBuying, gridBagConstraints);

        lblTCBuyingPer.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panTCBuying.add(lblTCBuyingPer, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 4, 4);
        panOthers.add(panTCBuying, gridBagConstraints);

        panTCSelling.setMinimumSize(new java.awt.Dimension(50, 21));
        panTCSelling.setPreferredSize(new java.awt.Dimension(50, 21));
        panTCSelling.setLayout(new java.awt.GridBagLayout());

        txtTCSelling.setMinimumSize(new java.awt.Dimension(35, 21));
        txtTCSelling.setPreferredSize(new java.awt.Dimension(35, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panTCSelling.add(txtTCSelling, gridBagConstraints);

        lblTCSellingPer.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panTCSelling.add(lblTCSellingPer, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 4);
        panOthers.add(panTCSelling, gridBagConstraints);

        lblTTSelling.setText("TT Selling");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthers.add(lblTTSelling, gridBagConstraints);

        panTTSelling.setMinimumSize(new java.awt.Dimension(50, 21));
        panTTSelling.setPreferredSize(new java.awt.Dimension(50, 21));
        panTTSelling.setLayout(new java.awt.GridBagLayout());

        txtTTSelling.setMinimumSize(new java.awt.Dimension(35, 21));
        txtTTSelling.setPreferredSize(new java.awt.Dimension(35, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panTTSelling.add(txtTTSelling, gridBagConstraints);

        lblTTSellingPer.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panTTSelling.add(lblTTSellingPer, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 4);
        panOthers.add(panTTSelling, gridBagConstraints);

        lblTCBuyingSlab.setText("Min. Charge");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthers.add(lblTCBuyingSlab, gridBagConstraints);

        txtTTBuyingSlab.setMinimumSize(new java.awt.Dimension(35, 21));
        txtTTBuyingSlab.setPreferredSize(new java.awt.Dimension(35, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 4, 4);
        panOthers.add(txtTTBuyingSlab, gridBagConstraints);

        lblTCBuyingComm.setText("Commission");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthers.add(lblTCBuyingComm, gridBagConstraints);

        txtTTBuyingComm.setMinimumSize(new java.awt.Dimension(35, 21));
        txtTTBuyingComm.setPreferredSize(new java.awt.Dimension(35, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panOthers.add(txtTTBuyingComm, gridBagConstraints);

        lblTTBuyingCommPer.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 5;
        panOthers.add(lblTTBuyingCommPer, gridBagConstraints);

        sptTC.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 4, 0);
        panOthers.add(sptTC, gridBagConstraints);

        lblTCBuyingCommPer.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 13;
        panOthers.add(lblTCBuyingCommPer, gridBagConstraints);

        txtTCBuyingComm.setMinimumSize(new java.awt.Dimension(35, 21));
        txtTCBuyingComm.setPreferredSize(new java.awt.Dimension(35, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panOthers.add(txtTCBuyingComm, gridBagConstraints);

        lblTTBuyingComm.setText("Commission");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthers.add(lblTTBuyingComm, gridBagConstraints);

        txtTCBuyingSlab.setMinimumSize(new java.awt.Dimension(35, 21));
        txtTCBuyingSlab.setPreferredSize(new java.awt.Dimension(35, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 4, 4);
        panOthers.add(txtTCBuyingSlab, gridBagConstraints);

        lblTTBuyingSlab.setText("Min. Charge");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthers.add(lblTTBuyingSlab, gridBagConstraints);

        sptTT.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 4, 0);
        panOthers.add(sptTT, gridBagConstraints);

        lblBillBuyingSlab.setText("Min. Charge");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthers.add(lblBillBuyingSlab, gridBagConstraints);

        txtBillBuyingSlab.setMinimumSize(new java.awt.Dimension(35, 21));
        txtBillBuyingSlab.setPreferredSize(new java.awt.Dimension(35, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 4, 4);
        panOthers.add(txtBillBuyingSlab, gridBagConstraints);

        lblBillBuyingComm.setText("Commission");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthers.add(lblBillBuyingComm, gridBagConstraints);

        txtBillBuyingComm.setMinimumSize(new java.awt.Dimension(35, 21));
        txtBillBuyingComm.setPreferredSize(new java.awt.Dimension(35, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panOthers.add(txtBillBuyingComm, gridBagConstraints);

        lblBillBuyingCommPer.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 7;
        panOthers.add(lblBillBuyingCommPer, gridBagConstraints);

        sptBill.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 4, 0);
        panOthers.add(sptBill, gridBagConstraints);

        lblDDBuyingSlab.setText("Min. Charge");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthers.add(lblDDBuyingSlab, gridBagConstraints);

        txtDDBuyingSlab.setMinimumSize(new java.awt.Dimension(35, 21));
        txtDDBuyingSlab.setPreferredSize(new java.awt.Dimension(35, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 4, 4);
        panOthers.add(txtDDBuyingSlab, gridBagConstraints);

        lblDDBuyingComm.setText("Commission");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthers.add(lblDDBuyingComm, gridBagConstraints);

        txtDDBuyingComm.setMinimumSize(new java.awt.Dimension(35, 21));
        txtDDBuyingComm.setPreferredSize(new java.awt.Dimension(35, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panOthers.add(txtDDBuyingComm, gridBagConstraints);

        lblDDBuyingCommPer.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 9;
        panOthers.add(lblDDBuyingCommPer, gridBagConstraints);

        sptDD.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 4, 0);
        panOthers.add(sptDD, gridBagConstraints);

        lblCurrBuyingSlab.setText("Min. Charge");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthers.add(lblCurrBuyingSlab, gridBagConstraints);

        txtCurrBuyingSlab.setMinimumSize(new java.awt.Dimension(35, 21));
        txtCurrBuyingSlab.setPreferredSize(new java.awt.Dimension(35, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 4, 4);
        panOthers.add(txtCurrBuyingSlab, gridBagConstraints);

        lblCurrBuyingComm.setText("Commission");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthers.add(lblCurrBuyingComm, gridBagConstraints);

        txtCurrBuyingComm.setMinimumSize(new java.awt.Dimension(35, 21));
        txtCurrBuyingComm.setPreferredSize(new java.awt.Dimension(35, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panOthers.add(txtCurrBuyingComm, gridBagConstraints);

        lblCurrBuyingCommPer.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 11;
        panOthers.add(lblCurrBuyingCommPer, gridBagConstraints);

        sptCurr.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 4, 0);
        panOthers.add(sptCurr, gridBagConstraints);

        cLabel1.setText("(-)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panOthers.add(cLabel1, gridBagConstraints);

        cLabel2.setText("(+)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panOthers.add(cLabel2, gridBagConstraints);

        cLabel3.setText("(-)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panOthers.add(cLabel3, gridBagConstraints);

        cLabel4.setText("(+)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panOthers.add(cLabel4, gridBagConstraints);

        cLabel5.setText("(-)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panOthers.add(cLabel5, gridBagConstraints);

        cLabel6.setText("(+)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panOthers.add(cLabel6, gridBagConstraints);

        cLabel7.setText("(-)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panOthers.add(cLabel7, gridBagConstraints);

        cLabel8.setText("(+)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panOthers.add(cLabel8, gridBagConstraints);

        cLabel9.setText("(-)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panOthers.add(cLabel9, gridBagConstraints);

        cLabel10.setText("(+)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panOthers.add(cLabel10, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panExchangeRate.add(panOthers, gridBagConstraints);

        panCurrencyTbl.setMinimumSize(new java.awt.Dimension(355, 580));
        panCurrencyTbl.setPreferredSize(new java.awt.Dimension(355, 580));
        panCurrencyTbl.setLayout(new java.awt.GridBagLayout());

        srpCurrencyTbl.setMinimumSize(new java.awt.Dimension(350, 505));
        srpCurrencyTbl.setPreferredSize(new java.awt.Dimension(350, 240));

        tblCurrency.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Transaction Currency"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCurrency.setMinimumSize(new java.awt.Dimension(345, 200));
        tblCurrency.setPreferredSize(new java.awt.Dimension(345, 505));
        tblCurrency.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblCurrencyMousePressed(evt);
            }
        });
        srpCurrencyTbl.setViewportView(tblCurrency);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panCurrencyTbl.add(srpCurrencyTbl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panExchangeRate.add(panCurrencyTbl, gridBagConstraints);

        getContentPane().add(panExchangeRate, java.awt.BorderLayout.CENTER);

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

        lblSpace11.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace11.setText("     ");
        lblSpace11.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace11.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace11.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace11);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrHead.add(btnEdit);

        lblSpace12.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace12.setText("     ");
        lblSpace12.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace12.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace12.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace12);

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

        lblSpace13.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace13.setText("     ");
        lblSpace13.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace13.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace13.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace13);

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

        lblSpace14.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace14.setText("     ");
        lblSpace14.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace14.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace14.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace14);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrHead.add(btnException);

        lblSpace15.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace15.setText("     ");
        lblSpace15.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace15.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace15.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace15);

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
        tbrHead.add(btnPrint);

        lblSpace16.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace16.setText("     ");
        lblSpace16.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace16.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace16.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace16);

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

        mbrMain.add(mnuProcess);

        setJMenuBar(mbrMain);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        popUp();
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed

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
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    public void authorizeStatus(String authorizeStatus) {        
         if (viewType == AUTHORIZE && isFilled){
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("USER_ID", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("EXCHANGE ID", txtExchangeId.getText());
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, txtExchangeId.getText());
            ClientUtil.execute("authorizeExchangeRate", singleAuthorizeMap);
            
            btnCancelActionPerformed(null);            
        } else{
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "getSelectExchangeRateAuthorizeTOList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeExchangeRate");                    
            isFilled = false;            
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
        }
    }
    
    private void tblCurrencyMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCurrencyMousePressed
        // Add your handling code here:    
        if (observable.getActionType()== ClientConstants.ACTIONTYPE_NEW || observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
            populateCurrency(tblCurrency.getSelectedRow());
        }
    }//GEN-LAST:event_tblCurrencyMousePressed

    public void populateCurrency(int row){
        final String transCurrencyVal = ((String)((com.see.truetransact.clientutil.EnhancedTableModel)tblCurrency.getModel()).getValueAt(row, 0));
        cboTransCurrency.setSelectedItem(transCurrencyVal.substring(transCurrencyVal.indexOf('(')+1,transCurrencyVal.indexOf(')')));        
     }
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:        
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:        
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);        
//        viewType = ClientConstants.ACTIONTYPE_DELETE;
        popUp();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        ClientUtil.enableDisable(this,false);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:    
        ClientUtil.enableDisable(this,false);       
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
//        viewType = ClientConstants.ACTIONTYPE_EDIT;
        popUp();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
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
    /** To display a popUp window for viewing existing data */    
    private void popUp() {
        viewType = observable.getActionType();
        lblStatus.setText(ClientConstants.ACTION_STATUS[0]);
        final HashMap viewMap = new HashMap();
        if ( viewType == ClientConstants.ACTIONTYPE_EDIT ||  viewType == ClientConstants.ACTIONTYPE_DELETE ||  viewType == ClientConstants.ACTIONTYPE_VIEW ){
            viewMap.put(CommonConstants.MAP_NAME, "viewExchangeRate");
        }
        new ViewAll(this, viewMap).show();
    }
    
    /** Called by the Popup window created thru popUp method
     * @param param
     */    
    public void fillData(Object param) {    
        isFilled = true;    
        final HashMap hash = (HashMap) param;
        if (viewType == ClientConstants.ACTIONTYPE_EDIT || viewType == ClientConstants.ACTIONTYPE_DELETE || viewType == AUTHORIZE || viewType == ClientConstants.ACTIONTYPE_VIEW ){   
            if (viewType != AUTHORIZE){
                if (viewType != ClientConstants.ACTIONTYPE_DELETE ){
                    ClientUtil.enableDisable(this,true);
                }
                observable.setActionType(viewType); 
            }
            actionEditDelete(hash);    
        }
        if( viewType == ClientConstants.ACTIONTYPE_VIEW)
        {
             ClientUtil.enableDisable(this,false);
             observable.setActionType(viewType); 
        }
        if(viewType==AUTHORIZE) {
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                }
    }
        
    /*To get the data and populating on the screen,set the status and enabling the apt components*/
    private void actionEditDelete(HashMap hash){
        observable.setStatus();
        observable.populateData(hash);
        setButtonEnableDisable();
    }
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panOthers);
        /* mandatoryMessage length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        if (mandatoryMessage.length() > 0 && observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE){
            displayAlert(mandatoryMessage);
        }else{
            savePerformed();
        }
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
        
    /** To perform all actions for Save functionality when the mandatory conditions are satisfied
     */    
    private void savePerformed(){
        updateOBFields();
        observable.doAction();
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();
        observable.resetForm();
        observable.setResultStatus();
    }
    
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:        
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        observable.resetForm();
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable(); 
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        observable.setStatus();
        viewType = 0;
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:        
        observable.resetForm();
        ClientUtil.enableDisable(this, true);
//        tdtValueDate.setDateValue(DateUtil.getStringDate(currDt.clone()));
        setButtonEnableDisable();
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.setStatus();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
//        tdtValueDate.setDateValue(DateUtil.getStringDate(currDt.clone()));
        //observable.ttNotifyObservers();
    }//GEN-LAST:event_btnNewActionPerformed
    
    
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
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CLabel cLabel10;
    private com.see.truetransact.uicomponent.CLabel cLabel2;
    private com.see.truetransact.uicomponent.CLabel cLabel3;
    private com.see.truetransact.uicomponent.CLabel cLabel4;
    private com.see.truetransact.uicomponent.CLabel cLabel5;
    private com.see.truetransact.uicomponent.CLabel cLabel6;
    private com.see.truetransact.uicomponent.CLabel cLabel7;
    private com.see.truetransact.uicomponent.CLabel cLabel8;
    private com.see.truetransact.uicomponent.CLabel cLabel9;
    private com.see.truetransact.uicomponent.CComboBox cboBuySellType;
    private com.see.truetransact.uicomponent.CComboBox cboConversionCurrency;
    private com.see.truetransact.uicomponent.CComboBox cboCustomerType;
    private com.see.truetransact.uicomponent.CComboBox cboTransCurrency;
    private com.see.truetransact.uicomponent.CCheckBox chkPreferred;
    private com.see.truetransact.uicomponent.CLabel lblBillBuying;
    private com.see.truetransact.uicomponent.CLabel lblBillBuyingComm;
    private com.see.truetransact.uicomponent.CLabel lblBillBuyingCommPer;
    private com.see.truetransact.uicomponent.CLabel lblBillBuyingPer;
    private com.see.truetransact.uicomponent.CLabel lblBillBuyingSlab;
    private com.see.truetransact.uicomponent.CLabel lblBillSelling;
    private com.see.truetransact.uicomponent.CLabel lblBillSellingPer;
    private com.see.truetransact.uicomponent.CLabel lblBuySellType;
    private com.see.truetransact.uicomponent.CLabel lblBuyingPer;
    private com.see.truetransact.uicomponent.CLabel lblConversionCurrency;
    private com.see.truetransact.uicomponent.CLabel lblCurrBuying;
    private com.see.truetransact.uicomponent.CLabel lblCurrBuyingComm;
    private com.see.truetransact.uicomponent.CLabel lblCurrBuyingCommPer;
    private com.see.truetransact.uicomponent.CLabel lblCurrBuyingSlab;
    private com.see.truetransact.uicomponent.CLabel lblCurrSelling;
    private com.see.truetransact.uicomponent.CLabel lblCustomerType;
    private com.see.truetransact.uicomponent.CLabel lblDDBuying;
    private com.see.truetransact.uicomponent.CLabel lblDDBuyingComm;
    private com.see.truetransact.uicomponent.CLabel lblDDBuyingCommPer;
    private com.see.truetransact.uicomponent.CLabel lblDDBuyingPer;
    private com.see.truetransact.uicomponent.CLabel lblDDBuyingSlab;
    private com.see.truetransact.uicomponent.CLabel lblDDSelling;
    private com.see.truetransact.uicomponent.CLabel lblDDSellingPer;
    private com.see.truetransact.uicomponent.CLabel lblExchangeId;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNotionalRate;
    private com.see.truetransact.uicomponent.CLabel lblPreferred;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblSellingPer;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace11;
    private com.see.truetransact.uicomponent.CLabel lblSpace12;
    private com.see.truetransact.uicomponent.CLabel lblSpace13;
    private com.see.truetransact.uicomponent.CLabel lblSpace14;
    private com.see.truetransact.uicomponent.CLabel lblSpace15;
    private com.see.truetransact.uicomponent.CLabel lblSpace16;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTCBuying;
    private com.see.truetransact.uicomponent.CLabel lblTCBuyingComm;
    private com.see.truetransact.uicomponent.CLabel lblTCBuyingCommPer;
    private com.see.truetransact.uicomponent.CLabel lblTCBuyingPer;
    private com.see.truetransact.uicomponent.CLabel lblTCBuyingSlab;
    private com.see.truetransact.uicomponent.CLabel lblTCSelling;
    private com.see.truetransact.uicomponent.CLabel lblTCSellingPer;
    private com.see.truetransact.uicomponent.CLabel lblTTBuying;
    private com.see.truetransact.uicomponent.CLabel lblTTBuyingComm;
    private com.see.truetransact.uicomponent.CLabel lblTTBuyingCommPer;
    private com.see.truetransact.uicomponent.CLabel lblTTBuyingPer;
    private com.see.truetransact.uicomponent.CLabel lblTTBuyingSlab;
    private com.see.truetransact.uicomponent.CLabel lblTTSelling;
    private com.see.truetransact.uicomponent.CLabel lblTTSellingPer;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panBillBuying;
    private com.see.truetransact.uicomponent.CPanel panBillSelling;
    private com.see.truetransact.uicomponent.CPanel panBuying;
    private com.see.truetransact.uicomponent.CPanel panCurrencyTbl;
    private com.see.truetransact.uicomponent.CPanel panDDBuying;
    private com.see.truetransact.uicomponent.CPanel panDDSelling;
    private com.see.truetransact.uicomponent.CPanel panExchangeRate;
    private com.see.truetransact.uicomponent.CPanel panID;
    private com.see.truetransact.uicomponent.CPanel panOthers;
    private com.see.truetransact.uicomponent.CPanel panSelling;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTCBuying;
    private com.see.truetransact.uicomponent.CPanel panTCSelling;
    private com.see.truetransact.uicomponent.CPanel panTTBuying;
    private com.see.truetransact.uicomponent.CPanel panTTSelling;
    private com.see.truetransact.uicomponent.CSeparator sptBill;
    private javax.swing.JSeparator sptCancel;
    private com.see.truetransact.uicomponent.CSeparator sptCurr;
    private com.see.truetransact.uicomponent.CSeparator sptDD;
    private com.see.truetransact.uicomponent.CSeparator sptTC;
    private com.see.truetransact.uicomponent.CSeparator sptTT;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CScrollPane srpCurrencyTbl;
    private com.see.truetransact.uicomponent.CTable tblCurrency;
    private javax.swing.JToolBar tbrHead;
    private com.see.truetransact.uicomponent.CTextField txtBillBuying;
    private com.see.truetransact.uicomponent.CTextField txtBillBuyingComm;
    private com.see.truetransact.uicomponent.CTextField txtBillBuyingSlab;
    private com.see.truetransact.uicomponent.CTextField txtBillSelling;
    private com.see.truetransact.uicomponent.CTextField txtCurrBuying;
    private com.see.truetransact.uicomponent.CTextField txtCurrBuyingComm;
    private com.see.truetransact.uicomponent.CTextField txtCurrBuyingSlab;
    private com.see.truetransact.uicomponent.CTextField txtCurrSelling;
    private com.see.truetransact.uicomponent.CTextField txtDDBuying;
    private com.see.truetransact.uicomponent.CTextField txtDDBuyingComm;
    private com.see.truetransact.uicomponent.CTextField txtDDBuyingSlab;
    private com.see.truetransact.uicomponent.CTextField txtDDSelling;
    private com.see.truetransact.uicomponent.CTextField txtExchangeId;
    private com.see.truetransact.uicomponent.CTextField txtNotionalRate;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    private com.see.truetransact.uicomponent.CTextField txtTCBuying;
    private com.see.truetransact.uicomponent.CTextField txtTCBuyingComm;
    private com.see.truetransact.uicomponent.CTextField txtTCBuyingSlab;
    private com.see.truetransact.uicomponent.CTextField txtTCSelling;
    private com.see.truetransact.uicomponent.CTextField txtTTBuying;
    private com.see.truetransact.uicomponent.CTextField txtTTBuyingComm;
    private com.see.truetransact.uicomponent.CTextField txtTTBuyingSlab;
    private com.see.truetransact.uicomponent.CTextField txtTTSelling;
    private com.see.truetransact.uicomponent.CLabel txtTransCurrency;
    // End of variables declaration//GEN-END:variables
    
}
