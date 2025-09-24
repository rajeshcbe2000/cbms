/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * OutwardClearingUI.java
 *
 * Created on January 9, 2004, 3:29 PM
 *
 * The Query fired on edit and deleted button clicked event, is yet to be finalized as
 * te sum of Amount field is not giving correct value.
 */

package com.see.truetransact.ui.transaction.clearing.outward;

/**
 * @author  Hemant
 * @ Modified K.R.Jayakrishnan
 * @modified Bala
 */

import java.util.HashMap;
import java.util.Observer;
import java.util.Observable;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.transferobject.transaction.clearing.outward.OutwardClearingTO;
import com.see.truetransact.transferobject.transaction.clearing.outward.OutwardClearingPISTO;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.ui.common.viewall.TableDialogUI;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.ui.transaction.common.TransDetailsUI;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uicomponent.COptionPane;
import javax.swing.JFrame;
import java.awt.Color;
import com.see.truetransact.ui.TrueTransactMain;
public class OutwardClearingUI extends CInternalFrame implements Observer,UIMandatoryField {
   
    Date curDate = null;
    private HashMap mandatoryMap;
    private HashMap tempMap;  // Declared by Rajesh
    private boolean tblPISDPressedOrNot = false;
    private OutwardClearingRB resourceBundle = new OutwardClearingRB();
    private OutwardClearingOB observable;
    private OutwardClearingMRB objMandatoryRB;
    private String status1;
    private TransDetailsUI transDetails = null;
    
    private String viewType = "";
    private String real=null;
    private String view=null;
    private boolean isFilled=false;
    private boolean isNew=false;
    /** Creates new form OutwardClearingUI */
    public OutwardClearingUI() {
        curDate = ClientUtil.getCurrentDate();
        initComponents();
        settingUpUI();
        btnReject.setVisible(true);
    }
    private void  settingUpUI(){
        setObservable();
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        initComponentData();
        setComponentsLength();
        setButtonEnableDisable();
        setHelpMessage();
        ClientUtil.enableDisable(panMainOC, false);
        transDetails = new TransDetailsUI(panLableValues);
        setInvisible();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panInstrDetOC);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panInsidePISD);
    }
    private void setInvisible(){
        this.btnEndID.setVisible(false);
        this.btnEndPISD.setVisible(false);
        lblInstrDetailsCurrency.setVisible(false);
        cboInstrDetailsCurrency.setVisible(false);
    }
    public static void main(String [] str){
        JFrame jf = new JFrame();
        OutwardClearingUI oc = new OutwardClearingUI();
        jf.getContentPane().add(oc);
        jf.show();
        oc.show();
    }
    
    // Setting the Observable Instance to observable object.
    private void setObservable() {
        observable = new OutwardClearingOB();
        observable.addObserver(this);
    }
    private void setComponentsLength(){
        txtAccNoPISD.setAllowAll(true);
        txtBatchIdOC.setMaxLength(16);
        txtInstrumentNo1ID.setAllowNumber(true);
        txtInstrumentNo1ID.setMaxLength(ClientConstants.INSTRUMENT_NO1);
        txtInstrumentNo2ID.setValidation(new NumericValidation(ClientConstants.INSTRUMENT_NO2, 0));
        //        txtPayeeNameID.setMaxLength(64);
        //        txtDrawerNameID.setMaxLength(64);
        txtDrawerAccNoID.setAllowNumber(true);
        txtDrawerAccNoID.setMaxLength(32);
        txtRemarksID.setMaxLength(128);
        txtAmountID.setValidation(new CurrencyValidation(14,2));
        txtAmountPISD.setValidation(new CurrencyValidation(14,2));
        txtBankCodeID.setValidation(new NumericValidation(8,0));
        txtBranchCodeID.setValidation(new NumericValidation(8,0));
        txtBankCodeID.setAllowNumber(true);
        txtBranchCodeID.setAllowNumber(true);
        txtBankCodeID.setAllowAll(false);
        txtBranchCodeID.setAllowAll(false);
    }
    //Setting name to all the Components in UI.
    private void setFieldNames() {
        btnAccNoPISD.setName("btnAccNoPISD");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnDeleteID.setName("btnDeleteID");
        btnDeletePISD.setName("btnDeletePISD");
        btnView.setName("btnView");
        btnEdit.setName("btnEdit");
        btnEndID.setName("btnEndID");
        btnEndPISD.setName("btnEndPISD");
        btnNew.setName("btnNew");
        btnNewID.setName("btnNewID");
        btnNewPISD.setName("btnNewPISD");
        btnPrint.setName("btnPrint");
        btnSave.setName("btnSave");
        btnSaveID.setName("btnSaveID");
        btnSavePISD.setName("btnSavePISD");
        cboClearingTypeID.setName("cboClearingTypeID");
        cboInstrumentTypeID.setName("cboInstrumentTypeID");
        cboInstrDetailsCurrency.setName("cboInstrDetailsCurrency");
        cboProdIdPISD.setName("cboProdIdPISD");
        cboScheduleNo.setName("cboScheduleNo");
        dtdInstrumentDtID.setName("dtdInstrumentDtID");
        dtclearingDtID.setName("dtclearingDtID");
        lblAccHeadPISD.setName("lblAccHeadPISD");
//        lblAccHolderNamePISD.setName("lblAccHolderNamePISD");
        lblAccHolderNameValuePISD.setName("lblAccHolderNameValuePISD");
        lblAccNoPISD.setName("lblAccNoPISD");
        lblAmountID.setName("lblAmountID");
        lblAmountPISD.setName("lblAmountPISD");
        lblBankCodeID.setName("lblBankCodeID");
        lblBookedInstr.setName("lblBookedInstr");
        lblBatchIdOC.setName("lblBatchIdOC");
        lblValNoInstrBooked.setName("lblValNoInstrBooked");
        lblBranchCodeID.setName("lblBranchCodeID");
        lblDrawerAccNoID.setName("lblDrawerAccNoID");
        //        lblDrawerNameID.setName("lblDrawerNameID");
        lblInstrumentDtID.setName("lblInstrumentDtID");
        lblInstrumentNo1ID.setName("lblInstrumentNo1ID");
        lblInstrumentNo2ID.setName("lblInstrumentNo2ID");
        lblInstrumentTypeID.setName("lblInstrumentTypeID");
        lblInstrDetailsCurrency.setName("lblInstrDetailsCurrency");
        lblMsg.setName("lblMsg");
        //        lblPayeeNameID.setName("lblPayeeNameID");
        lblProdIdPISD.setName("lblProdIdPISD");
        lblBankNameID.setName("lblBankNameID");
        lblBankNameIDValue.setName("lblBankNameIDValue");
        lblBranchNameID.setName("lblBranchNameID");
        lblBranchNameIDValue.setName("lblBranchNameIDValue");
        
        lblRemarksID.setName("lblRemarksID");
        lblRemarksPISD.setName("lblRemarksPISD");
        lblSpace.setName("lblSpace");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblStatus.setName("lblStatus");
        lblTotalAmountID.setName("lblTotalAmountID");
        lblTotalAmountPISD.setName("lblTotalAmountPISD");
        lblTotalAmountValueID.setName("lblTotalAmountValueID");
        lblTotalAmountValuePISD.setName("lblTotalAmountValuePISD");
        lblScheduleNo.setName("lblScheduleNo");
        lblBookedAmt.setName("lblBookedAmt");
        lblValAmount.setName("lblValAmount");
        mbrMain.setName("mbrMain");
        panBatchIdOC.setName("panBatchIdOC");
        panButtonID.setName("panButtonID");
        panButtonPISD.setName("panButtonPISD");
        panInsideDI.setName("panInsideDI");
        panInsidePISD.setName("panInsidePISD");
        panInstrDetOC.setName("panInstrDetOC");
        panMainOC.setName("panMainOC");
        panPayInSlipDetOC.setName("panPayInSlipDetOC");
        panStatus.setName("panStatus");
        panTotalAmountID.setName("panTotalAmountID");
        panTotalAmountPISD.setName("panTotalAmountPISD");
        sptHorizontalUpPISD.setName("sptHorizontalUpPISD");
        sptVerticalID.setName("sptVerticalID");
        sptVerticalPISD.setName("sptVerticalPISD");
        srpTCID.setName("srpTCID");
        srpTCPISD.setName("srpTCPISD");
        tblID.setName("tblID");
        tblPISD.setName("tblPISD");
        txtAccNoPISD.setName("txtAccNoPISD");
        txtAmountID.setName("txtAmountID");
        txtAmountPISD.setName("txtAmountPISD");
        txtBankCodeID.setName("txtBankCodeID");
        txtBranchCodeID.setName("txtBranchCodeID");
        //        cboBankCodeID.setName("cboBankCodeID");
        txtBatchIdOC.setName("txtBatchIdOC");
        //        cboBranchCodeID.setName("cboBranchCodeID");
        txtDrawerAccNoID.setName("txtDrawerAccNoID");
        //        txtDrawerNameID.setName("txtDrawerNameID");
        txtInstrumentNo1ID.setName("txtInstrumentNo1ID");
        txtInstrumentNo2ID.setName("txtInstrumentNo2ID");
        //        txtPayeeNameID.setName("txtPayeeNameID");
        txtRemarksID.setName("txtRemarksID");
        txtRemarksPISD.setName("txtRemarksPISD");
    }
    
    //To change the Lables Caption at run time. It takes Data from RB Class.
    private void internationalize() {
        btnEndPISD.setText(resourceBundle.getString("btnEndPISD"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblInstrumentNo1ID.setText(resourceBundle.getString("lblInstrumentNo1ID"));
        lblInstrumentNo2ID.setText(resourceBundle.getString("lblInstrumentNo2ID"));
        lblAccNoPISD.setText(resourceBundle.getString("lblAccNoPISD"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblProdIdPISD.setText(resourceBundle.getString("lblProdIdPISD"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblRemarksID.setText(resourceBundle.getString("lblRemarksID"));
        lblRemarksPISD.setText(resourceBundle.getString("lblRemarksPISD"));
        lblBranchCodeID.setText(resourceBundle.getString("lblBranchCodeID"));
        lblTotalAmountValueID.setText(resourceBundle.getString("lblTotalAmountValueID"));
        btnNewPISD.setText(resourceBundle.getString("btnNewPISD"));
        btnNewID.setText(resourceBundle.getString("btnNewID"));
        lblAmountPISD.setText(resourceBundle.getString("lblAmountPISD"));
        btnEndID.setText(resourceBundle.getString("btnEndID"));
        btnView.setText(resourceBundle.getString("btnView"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblDrawerAccNoID.setText(resourceBundle.getString("lblDrawerAccNoID"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblTotalAmountPISD.setText(resourceBundle.getString("lblTotalAmountPISD"));
        lblTotalAmountValuePISD.setText(resourceBundle.getString("lblTotalAmountValuePISD"));
        btnSavePISD.setText(resourceBundle.getString("btnSavePISD"));
        ((javax.swing.border.TitledBorder)panPayInSlipDetOC.getBorder()).setTitle(resourceBundle.getString("panPayInSlipDetOC"));
        btnDeletePISD.setText(resourceBundle.getString("btnDeletePISD"));
        ((javax.swing.border.TitledBorder)panInstrDetOC.getBorder()).setTitle(resourceBundle.getString("panInstrDetOC"));
//        lblAccHolderNamePISD.setText(resourceBundle.getString("lblAccHolderNamePISD"));
        lblTotalAmountID.setText(resourceBundle.getString("lblTotalAmountID"));
        lblInstrumentDtID.setText(resourceBundle.getString("lblInstrumentDtID"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        //        lblDrawerNameID.setText(resourceBundle.getString("lblDrawerNameID"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblBatchIdOC.setText(resourceBundle.getString("lblBatchIdOC"));
        lblAmountID.setText(resourceBundle.getString("lblAmountID"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnAccNoPISD.setText(resourceBundle.getString("btnAccNoPISD"));
        lblAccHeadPISD.setText(resourceBundle.getString("lblAccHeadPISD"));
        lblSpace.setText(resourceBundle.getString("lblSpace"));
        lblAccHolderNameValuePISD.setText(resourceBundle.getString("lblAccHolderNameValuePISD"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblInstrumentTypeID.setText(resourceBundle.getString("lblInstrumentTypeID"));
        lblInstrDetailsCurrency.setText(resourceBundle.getString("lblInstrDetailsCurrency"));
        //        lblPayeeNameID.setText(resourceBundle.getString("lblPayeeNameID"));
        lblBankNameID.setText(resourceBundle.getString("lblBankNameID"));
        lblBranchNameID.setText(resourceBundle.getString("lblBranchNameID"));
        lblBankNameIDValue.setText(resourceBundle.getString("lblBankNameIDValue"));
        lblBranchNameIDValue.setText(resourceBundle.getString("lblBranchNameIDValue"));
        btnDeleteID.setText(resourceBundle.getString("btnDeleteID"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        ((javax.swing.border.TitledBorder)panMainOC.getBorder()).setTitle(resourceBundle.getString("panMainOC"));
        btnSaveID.setText(resourceBundle.getString("btnSaveID"));
        lblBankCodeID.setText(resourceBundle.getString("lblBankCodeID"));
        lblClearingTypeID.setText(resourceBundle.getString("lblClearingTypeID"));
        lblScheduleNo.setText(resourceBundle.getString("lblScheduleNo"));
        lblClearingDate.setText(resourceBundle.getString("lblClearingDate"));
        lblBookedInstr.setText(resourceBundle.getString("lblBookedInstr"));
        lblBookedAmt.setText(resourceBundle.getString("lblBookedAmt"));
    }
    
    // To Declare Mandatory Fields in UI.
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtBatchIdOC", new Boolean(true));
        mandatoryMap.put("cboClearingTypeID", new Boolean(true));
        mandatoryMap.put("cboInstrumentTypeID", new Boolean(true));
        mandatoryMap.put("cboInstrDetailsCurrency", new Boolean(false));
        mandatoryMap.put("dtdInstrumentDtID", new Boolean(true));
        mandatoryMap.put("txtInstrumentNo1ID", new Boolean(true));
        mandatoryMap.put("txtInstrumentNo2ID", new Boolean(true));
        mandatoryMap.put("txtAmountID", new Boolean(true));
        mandatoryMap.put("txtDrawerAccNoID", new Boolean(true));
        mandatoryMap.put("txtDrawerNameID", new Boolean(true));
        mandatoryMap.put("txtPayeeNameID", new Boolean(true));
        mandatoryMap.put("txtBankCodeID", new Boolean(true));
        mandatoryMap.put("txtBranchCodeID", new Boolean(true));
        mandatoryMap.put("txtRemarksID", new Boolean(false));
        mandatoryMap.put("cboProdIdPISD", new Boolean(true));
        mandatoryMap.put("txtAccNoPISD", new Boolean(true));
        mandatoryMap.put("txtAmountPISD", new Boolean(true));
        mandatoryMap.put("txtConvAmt", new Boolean(true));
        mandatoryMap.put("txtRemarksPISD", new Boolean(false));
    }
    
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    //Implementation of update() method of Observer interface.
    public void update(Observable observed, Object arg) {
        if(txtRemarksID.getText().equalsIgnoreCase("Proceeds Received")){
            cboClearingTypeID.setSelectedItem(observable.getCboClearingTypeID());
            lblValScheduleNo.setText(observable.getLblValScheduleNo());
            cboScheduleNo.setModel(observable.getCbmScheduleNo());
            cboScheduleNo.setSelectedItem(observable.getCbmScheduleNo().getDataForKey(observable.getCboScheduleNo()));
            this.lblCreatedByValue.setText(observable.getCreatedBy());
            lblValNoInstrBooked.setText(observable.getLblValNoInstrBooked());
            lblValAmount.setText(observable.getLblValAmount());
        }else{
            cboProductType.setSelectedItem(
            ((ComboBoxModel) cboProductType.getModel()).getDataForKey(
            observable.getCboProductType()));

            txtBatchIdOC.setText(observable.getTxtBatchIdOC());
            cboClearingTypeID.setSelectedItem(observable.getCboClearingTypeID());
            cboInstrumentTypeID.setSelectedItem(observable.getCboInstrumentTypeID());
            cboInstrDetailsCurrency.setSelectedItem(observable.getCboInstrDetailsCurrency());
            txtInstrumentNo1ID.setText(observable.getTxtInstrumentNo1ID());
            txtInstrumentNo2ID.setText(observable.getTxtInstrumentNo2ID());
            txtAmountID.setText(observable.getTxtAmountID());
            txtDrawerAccNoID.setText(observable.getTxtDrawerAccNoID());
            //        txtDrawerNameID.setText(observable.getTxtDrawerNameID());
            //        txtPayeeNameID.setText(observable.getTxtPayeeNameID());
            //        cboBankCodeID.setSelectedItem(observable.getCbmBankCodeID().getDataForKey(observable.getCboBankCodeID()));
            txtBankCodeID.setText(observable.getTxtBankCode());
            txtBankCodeIDActionPerformed(null);
            txtBranchCodeID.setText(observable.getTxtBranchCode());
            txtBranchCodeIDActionPerformed(null);
            if (CommonUtil.convertObjToStr(observable.getCboBankCodeID()).length() > 0) {
                observable.getBranchData();
                //            cboBranchCodeID.setSelectedItem(observable.getCbmBranchCodeID().getDataForKey(observable.getCboBranchCodeID()));
            }

            txtRemarksID.setText(observable.getTxtRemarksID());

            //        if (CommonUtil.convertObjToStr(observable.getCboProductType()).length() > 0) {
            observable.setCbmProdIdPISD(observable.getCboProductType());
            cboProdIdPISD.setModel(observable.getCbmProdIdPISD());
            cboProdIdPISD.setSelectedItem(observable.getCbmProdIdPISD().getDataForKey(observable.getCboProdIdPISD()));
            cboScheduleNo.setModel(observable.getCbmScheduleNo());
            cboScheduleNo.setSelectedItem(observable.getCbmScheduleNo().getDataForKey(observable.getCboScheduleNo()));
            //        }

            txtAccNoPISD.setText(observable.getTxtAccNoPISD());
            txtAmountPISD.setText(observable.getTxtAmountPISD());
            //        txtConvAmt.setText(observable.getTxtConvAmt());
            txtRemarksPISD.setText(observable.getTxtRemarksPISD());
            dtdInstrumentDtID.setDateValue(observable.getDtdInstrumentDtID());
            dtclearingDtID.setDateValue(observable.getDtclearingDtID());
            tblID.setModel(observable.getTbmID());
            tblPISD.setModel(observable.getTbmPISD());
            lblTotalAmountValueID.setText(observable.getLblTotalAmountValueID());
            lblTotalAmountValuePISD.setText(observable.getLblTotalAmountValuePISD());
            lblAccHolderNameValuePISD.setText(observable.getLblAccHolderNameValuePISD());
            lblValScheduleNo.setText(observable.getLblValScheduleNo());
            lblValAmount.setText(observable.getLblValAmount());
            lblValNoInstrBooked.setText(observable.getLblValNoInstrBooked());
            this.txtAccHeadValuePISD.setText(observable.getLblAccHead());

            if(observable.getOperation()!=ClientConstants.ACTIONTYPE_NEW)
                this.lblCreatedByValue.setText(observable.getCreatedBy());
            this.lblAccountHeadDescValue.setText(observable.getLblAccHeadDesc());
        }
    }
    
    //Setting Observable data.
    public void updateOBFields() {
         if(txtRemarksID.getText().equalsIgnoreCase("Proceeds Received")){
            observable.setCboClearingTypeID((String) cboClearingTypeID.getSelectedItem());
            observable.setLblValScheduleNo(lblValScheduleNo.getText());
            observable.setCboScheduleNo((String)((ComboBoxModel)this.cboScheduleNo.getModel()).getKeyForSelected());
            observable.setCreatedBy(this.lblCreatedByValue.getText());
        }else{
        observable.setCboProductType((String)((ComboBoxModel)this.cboProductType.getModel()).getKeyForSelected());
        observable.setTxtBatchIdOC(txtBatchIdOC.getText());
        observable.setCboClearingTypeID((String) cboClearingTypeID.getSelectedItem());
        observable.setCboInstrumentTypeID((String) cboInstrumentTypeID.getSelectedItem());
        observable.setCboInstrDetailsCurrency((String) cboInstrDetailsCurrency.getSelectedItem());
        observable.setTxtInstrumentNo1ID(txtInstrumentNo1ID.getText());
        observable.setTxtInstrumentNo2ID(txtInstrumentNo2ID.getText());
        observable.setTxtAmountID(txtAmountID.getText());
        observable.setTxtDrawerAccNoID(txtDrawerAccNoID.getText());
        //        observable.setTxtDrawerNameID(txtDrawerNameID.getText());
        //        observable.setTxtPayeeNameID(txtPayeeNameID.getText());
        //        observable.setCboBankCodeID((String)((ComboBoxModel)this.cboBankCodeID.getModel()).getKeyForSelected());
        observable.setTxtBankCode(txtBankCodeID.getText());
        observable.setTxtBranchCode(txtBranchCodeID.getText());
        observable.setCboScheduleNo((String)((ComboBoxModel)this.cboScheduleNo.getModel()).getKeyForSelected());
        //        if (cboBranchCodeID.getModel() != null) {
        //            observable.setCboBranchCodeID((String)((ComboBoxModel)this.cboBranchCodeID.getModel()).getKeyForSelected());
        //        }
        observable.setTxtRemarksID(txtRemarksID.getText());
        
        if (cboProdIdPISD.getModel() != null) {
            observable.setCboProdIdPISD((String)((ComboBoxModel)this.cboProdIdPISD.getModel()).getKeyForSelected());
        }
        observable.setTxtAccNoPISD(txtAccNoPISD.getText());
        observable.setTxtAmountPISD(txtAmountPISD.getText());
        //        observable.setTxtConvAmt(txtConvAmt.getText());
        observable.setTxtRemarksPISD(txtRemarksPISD.getText());
        observable.setLblTotalAmountValuePISD(lblTotalAmountValuePISD.getText());
        observable.setLblValScheduleNo(lblValScheduleNo.getText());
        observable.setDtdInstrumentDtID(dtdInstrumentDtID.getDateValue());
        observable.setDtclearingDtID(dtclearingDtID.getDateValue());
        observable.setCreatedBy(this.lblCreatedByValue.getText());
        observable.setLblAccHead(this.txtAccHeadValuePISD.getText());
        }
        
    }
    
    public void setHelpMessage() {
        objMandatoryRB = new OutwardClearingMRB();
        txtBatchIdOC.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBatchIdOC"));
        cboClearingTypeID.setHelpMessage(lblMsg, objMandatoryRB.getString("cboClearingTypeID"));
        cboInstrumentTypeID.setHelpMessage(lblMsg, objMandatoryRB.getString("cboInstrumentTypeID"));
        cboScheduleNo.setHelpMessage(lblMsg, objMandatoryRB.getString("cboScheduleNo"));
        cboInstrDetailsCurrency.setHelpMessage(lblMsg, objMandatoryRB.getString("cboInstrDetailsCurrency"));
        dtdInstrumentDtID.setHelpMessage(lblMsg, objMandatoryRB.getString("dtdInstrumentDtID"));
        txtInstrumentNo1ID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInstrumentNo1ID"));
        txtInstrumentNo2ID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInstrumentNo2ID"));
        txtAmountID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAmountID"));
        txtDrawerAccNoID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDrawerAccNoID"));
        //        txtDrawerNameID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDrawerNameID"));
        //        txtPayeeNameID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPayeeNameID"));
        //        cboBankCodeID.setHelpMessage(lblMsg, objMandatoryRB.getString("cboBankCodeID"));
        //        cboBranchCodeID.setHelpMessage(lblMsg, objMandatoryRB.getString("cboBranchCodeID"));
        txtBankCodeID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBankCodeID"));
        txtBranchCodeID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBranchCodeID"));
        
        txtRemarksID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRemarksID"));
        cboProdIdPISD.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdIdPISD"));
        txtAccNoPISD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccNoPISD"));
        txtAmountPISD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAmountPISD"));
        //        txtConvAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtConvAmt"));
        txtRemarksPISD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRemarksPISD"));
    }
    //This method'll initialized the ComboBoxes with ComboBoxModel in OB.
    private void initComponentData() {
        try{
            cboClearingTypeID.setModel(observable.getCbmClearingTypeID());
            cboInstrumentTypeID.setModel(observable.getCbmInstrumentTypeID());
            //            cboBankCodeID.setModel(observable.getCbmBankCodeID());
            cboProdIdPISD.setModel(observable.getCbmProdIdPISD());
            cboProductType.setModel(observable.getCbmProdType());
            //            cboScheduleNo.setModel(observable.getCbmScheduleNo());
            tblID.setModel(observable.getTbmID());
            tblPISD.setModel(observable.getTbmPISD());
        }catch(Exception e){
            e.printStackTrace();
        }
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
        
        btnNewID.setEnabled(!btnNew.isEnabled());
        btnSaveID.setEnabled(!btnNew.isEnabled());
        btnDeleteID.setEnabled(!btnNew.isEnabled());
        
        //btnEndID.setEnabled(!btnNew.isEnabled());
        
        btnNewPISD.setEnabled(!btnNew.isEnabled());
        btnSavePISD.setEnabled(!btnNew.isEnabled());
        btnDeletePISD.setEnabled(!btnNew.isEnabled());
        
        //btnEndPISD.setEnabled(!btnNew.isEnabled());
        
        btnAccNoPISD.setEnabled(!btnNew.isEnabled());
        
        //        btnAccNo1.setEnabled(!btnNew.isEnabled());
        //        btnShadowCredit.setEnabled(!btnNew.isEnabled());
        //        btnShadowDebit.setEnabled(!btnNew.isEnabled());
        btnAccountHead.setEnabled(!btnNew.isEnabled());
        btnBankCode.setEnabled(! btnNew.isEnabled());
        btnBranchCode.setEnabled(! btnNew.isEnabled());
        btnAuthorize.setEnabled(btnNew.isEnabled());
        btnReject.setEnabled(btnNew.isEnabled());
        btnRegularize.setEnabled(btnNew.isEnabled());
        btnException.setEnabled(btnNew.isEnabled());
        btnView1.setEnabled(!btnView1.isEnabled());
        
    }
    //Setting the Instruments panel buttons.Enabled New and End Instrument and Disable Save and Delete.
    private void setIDButtons(){
        btnNewID.setEnabled(true);
        btnSaveID.setEnabled(false);
        btnDeleteID.setEnabled(false);
        
        //btnEndID.setEnabled(true);
    }
    //Setting the Pay In Slip panel buttons.Enabled New and End Instrument and Disable Save and Delete.
    private void setPISDButtons(){
        btnNewPISD.setEnabled(true);
        btnSavePISD.setEnabled(false);
        btnDeletePISD.setEnabled(false);
        
        btnAccNoPISD.setEnabled(true);
        
        btnAccountHead.setEnabled(true);
    }
    private void disableIDButtons(){
        btnNewID.setEnabled(false);
        btnSaveID.setEnabled(false);
        btnDeleteID.setEnabled(false);
    }
    private void disablePISDButtons(){
        btnNewPISD.setEnabled(false);
        btnSavePISD.setEnabled(false);
        btnDeletePISD.setEnabled(false);
        btnAccNoPISD.setEnabled(false);
        btnAccountHead.setEnabled(false);
    }
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        if (currField.equals("Edit") || currField.equals("Delete") || currField.equals("View") || currField.equals("Enquirystatus")){
            ArrayList lst=new ArrayList();
            lst.add("BATCHID");
            viewMap.put(ClientConstants.RECORD_KEY_COL,lst);
            viewMap.put("MAPNAME", "getSelectOutwardClearingTOList");
            lst = null;
        }else {
            viewMap.put(CommonConstants.MAP_NAME, "Transfer.getAccountList"+((ComboBoxModel)this.cboProductType.getModel()).getKeyForSelected());
            HashMap whereListMap = new HashMap();
            whereListMap.put("PROD_ID", ((ComboBoxModel) cboProdIdPISD.getModel()).getKeyForSelected());
            whereListMap.put("SELECTED_BRANCH", getSelectedBranchID());
            
            ArrayList presentActNums = new ArrayList();
            int rowCount = this.tblPISD.getRowCount();
            
            if(rowCount!=0) {
                String actNum = "";
                for(int i=0;i<rowCount;i++){
                    actNum = CommonUtil.convertObjToStr(tblPISD.getValueAt(i,2));
                    if (actNum != null && !actNum.equals(""))
                        presentActNums.add(actNum);
                }
            }
            if (presentActNums.size()!=0)
                whereListMap.put("ACT NUM",presentActNums);
            System.out.println("#### " + presentActNums.size());
            viewMap.put(CommonConstants.MAP_WHERE, whereListMap);
        }
        new ViewAll(this, viewMap).show();
    }
    //This method  fill sthe data inthe UI components after selecting a row in ViewAll.
    public void fillData(Object obj) {
        setModified(true);
        HashMap hash = (HashMap) obj;
        
        if (tblPISDPressedOrNot == false)
            tempMap = (HashMap) obj;
        if (viewType != null) {
            if (viewType.equals("Edit") || viewType.equals("Delete") || 
            viewType.equals("AUTHORIZE") || viewType.equals("REALIZE") || viewType.equals("Enquirystatus")) {
                isFilled = true;
                txtBatchIdOC.setText((String)hash.get("BATCHID"));
                observable.setTxtBatchIdOC(txtBatchIdOC.getText());
                if(!viewType.equals("AUTHORIZE") && !viewType.equals("REALIZE")){
                    hash.put(CommonConstants.MAP_WHERE, hash.get("BATCHID"));
                } else {
                    hash.put(CommonConstants.MAP_WHERE, hash.get("BID"));
                }
                if(viewType.equals("REALIZE")){
                 HashMap hmap=new HashMap();
                      hmap.put("BID",hash.get("BID"));
                       String accountNo="";
                       List mainList=ClientUtil.executeQuery("getSelectNominalMemFee", hmap);
                        List aclist=ClientUtil.executeQuery("getAcNoForOutWardClearing", hmap);
                        
                       if(mainList!=null && mainList.size()>0){
                           hmap=(HashMap)mainList.get(0);
                           String allowAuth=CommonUtil.convertObjToStr(hmap.get("ALLOW_AUTH_BY_STAFF"));
                           if(allowAuth.equals("N")){
                            
                              if(aclist!=null && aclist.size()>0){ 
                               hmap=(HashMap)aclist.get(0);
                               hmap.put("USER_ID",TrueTransactMain.USER_ID);
                               hmap.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);    
                              
                               accountNo=CommonUtil.convertObjToStr(hmap.get("ACT_NUM"));
                               hmap.put("ACCOUNT NO",accountNo);
                              List lst=ClientUtil.executeQuery("getStaffIdForAccount", hmap);
                              List lst1=ClientUtil.executeQuery("getStaffIdForLoggedUser", hmap);
                              String staffId="";
                              String loggedStaffId="";
                              if(lst!=null && lst.size()>0){
                                 hmap=(HashMap)lst.get(0);
                                 staffId=CommonUtil.convertObjToStr(hmap.get("STAFF_ID"));
                                  }
                              if(lst1!=null && lst1.size()>0){
                                 hmap=(HashMap)lst1.get(0);
                                 loggedStaffId=CommonUtil.convertObjToStr(hmap.get("STAFF_ID"));
                                 }
                              if(!staffId.equals("") || !loggedStaffId.equals("")){
                                if(staffId.equals(loggedStaffId)){
                                    ClientUtil.displayAlert("Authorization not allowed in own account");
                                    btnCancelActionPerformed(null);
                                    return;
                                }
                              }
                            }
                          }
                       }
                     }
                observable.fetchData(hash);
                System.out.println("#### observable.getOperation : if 11 ACTIONTYPE_REALIZE :  "+observable.getOperation());
                if (observable.getOperation()==ClientConstants.ACTIONTYPE_DELETE ||
                observable.getOperation()==ClientConstants.ACTIONTYPE_AUTHORIZE ||
                observable.getOperation()==ClientConstants.ACTIONTYPE_EXCEPTION ||
                observable.getOperation()==ClientConstants.ACTIONTYPE_REALIZE ||
                observable.getOperation()==ClientConstants.ACTIONTYPE_REJECT ||
                observable.getOperation()==ClientConstants.ACTIONTYPE_VIEW){
                    
                    ClientUtil.enableDisable(panMainOC, false);
                    setButtonEnableDisable();
                    disableIDButtons();
                    disablePISDButtons();
                    
                    if(observable.getOperation()==ClientConstants.ACTIONTYPE_AUTHORIZE ||
                    observable.getOperation()==ClientConstants.ACTIONTYPE_EXCEPTION ||
                    observable.getOperation()==ClientConstants.ACTIONTYPE_REJECT){
                        this.btnRegularize.setEnabled(false);
                        this.btnNew.setEnabled(false);
                        this.btnEdit.setEnabled(false);
                        this.btnDelete.setEnabled(false);
                        this.btnCancel.setEnabled(true);
                        this.btnAuthorize.setEnabled(true);
                        this.btnReject.setEnabled(true);
                        this.btnException.setEnabled(true);
                    }else if (observable.getOperation()==ClientConstants.ACTIONTYPE_REALIZE) {
                        this.btnNew.setEnabled(false);
                        this.btnEdit.setEnabled(false);
                        this.btnDelete.setEnabled(false);
                        this.btnSave.setEnabled(false);
                        this.btnRegularize.setEnabled(true);
                        this.btnReject.setEnabled(true);
                    }
                }else{
                    ClientUtil.enableDisable(panMainOC, true);
                    setButtonEnableDisable();
                    setIDButtons();
                    ClientUtil.enableDisable(panPayInSlipDetOC,false);
                    disablePISDButtons();
                    ClientUtil.enableDisable(panComponentsID,false);
                    btnNewPISD.setEnabled(true);
                }
                //setButtonEnableDisable();
                update(null, null);
            }else if (viewType.equals("AccountNoPISD")) {
                //System.out.println("hash from filled"+hash);
                String prodType = ((ComboBoxModel)cboProductType.getModel()).getKeyForSelected().toString();
                observable.setCboProductType(prodType);
                String ACCOUNTNO = (String) hash.get("ACCOUNTNO");
                if(prodType.equals("TD") && ACCOUNTNO.lastIndexOf("_")==-1){
                    hash.put("ACCOUNTNO", hash.get("ACCOUNTNO")+"_1");
                    ACCOUNTNO=ACCOUNTNO+"_1";
                }
                if (prodType.equals("TD") && isNew) {
                    
                    observable.setTxtAmountPISD(CommonUtil.convertObjToStr(hash.get("AMOUNT")));
                    txtAmountPISD.setText(observable.getTxtAmountPISD());
                    
                    this.txtAmountPISD.setEnabled(false);
                } else {
                    this.txtAmountPISD.setEnabled(true);
                }
                //                observable.getAccNoDetails((String)hash.get("ACCOUNTNO"));
                //                System.out.println("hash" + hash);
                //To Set the Value of Account holder Name and the Balances in UI...
                if(!ACCOUNTNO.equals("")){
                    observable.setAccountName(ACCOUNTNO);
                    //                    lblAccHolderNameValuePISD.setText(observable.getLblAccHolderNameValuePISD());
                    if(observable.getLblAccHolderNameValuePISD()== null || observable.getLblAccHolderNameValuePISD().equals("")){
                        ClientUtil.displayAlert("Account Number  not valied");
                    }else{
                        transDetails.setTransDetails(observable.getCboProductType(), ProxyParameters.BRANCH_ID, ACCOUNTNO);
                    }
                } else if (!observable.getLblAccHead().equals("")) {
                    transDetails.setTransDetails(observable.getCboProductType(), ProxyParameters.BRANCH_ID, ACCOUNTNO);
                }
                
            }else if (viewType.equals("AccHeadForPISD")) {
                //                cboProdIdPISD.setSelectedItem("");
                //                this.txtAccNoPISD.setText("");
                this.btnAccNoPISD.setEnabled(false);
                //cboProdIdPISD.setSelectedItem("");
                txtAccHeadValuePISD.setText((String) hash.get("A/C HEAD"));
                lblAccountHeadDescValue.setText((String) hash.get("A/C HEAD DESCRIPTION"));
            }
            else if(viewType.equals("BANK_CODE")){
                System.out.println("hash"+hash);
                observable.setTxtBankCode(CommonUtil.convertObjToStr(hash.get("BANK_CODE")));
                txtBankCodeID.setText( observable.getTxtBankCode());
                lblBankNameIDValue.setText(CommonUtil.convertObjToStr(hash.get("BANK_NAME")));
            }
            else if(viewType.equals("BANK_BRANCH")){
                observable.setTxtBranchCode(CommonUtil.convertObjToStr(hash.get("BRANCH_CODE")));
                txtBranchCodeID.setText(observable.getTxtBranchCode());
                lblBranchNameIDValue.setText(CommonUtil.convertObjToStr(hash.get("BRANCH_NAME")));
            }
        }
        if(view=="view"){
            ClientUtil.enableDisable(this,false);
            btnDisableForView();
            view=null;
        }
        
    }
    private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
        
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tbrAdvances = new javax.swing.JToolBar();
        btnView1 = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace17 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace18 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace19 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace20 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace21 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnRegularize = new com.see.truetransact.uicomponent.CButton();
        lblSpace22 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace23 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panMainOC = new com.see.truetransact.uicomponent.CPanel();
        panBatchIdOC = new com.see.truetransact.uicomponent.CPanel();
        lblBatchIdOC = new com.see.truetransact.uicomponent.CLabel();
        txtBatchIdOC = new com.see.truetransact.uicomponent.CTextField();
        lblCreatedBy = new com.see.truetransact.uicomponent.CLabel();
        lblCreatedByValue = new com.see.truetransact.uicomponent.CLabel();
        panOutDetails = new com.see.truetransact.uicomponent.CPanel();
        lblBookedInstr = new com.see.truetransact.uicomponent.CLabel();
        lblBookedAmt = new com.see.truetransact.uicomponent.CLabel();
        lblValNoInstrBooked = new com.see.truetransact.uicomponent.CLabel();
        lblValAmount = new com.see.truetransact.uicomponent.CLabel();
        btnView = new com.see.truetransact.uicomponent.CButton();
        panInstrDetOC = new com.see.truetransact.uicomponent.CPanel();
        panButtonID = new com.see.truetransact.uicomponent.CPanel();
        btnNewID = new com.see.truetransact.uicomponent.CButton();
        btnSaveID = new com.see.truetransact.uicomponent.CButton();
        btnDeleteID = new com.see.truetransact.uicomponent.CButton();
        btnEndID = new com.see.truetransact.uicomponent.CButton();
        sptVerticalID = new com.see.truetransact.uicomponent.CSeparator();
        srpTCID = new com.see.truetransact.uicomponent.CScrollPane();
        tblID = new com.see.truetransact.uicomponent.CTable();
        panTotalAmountID = new com.see.truetransact.uicomponent.CPanel();
        lblTotalAmountID = new com.see.truetransact.uicomponent.CLabel();
        lblTotalAmountValueID = new com.see.truetransact.uicomponent.CLabel();
        panComponentsID = new com.see.truetransact.uicomponent.CPanel();
        panInsideDI = new com.see.truetransact.uicomponent.CPanel();
        lblClearingTypeID = new com.see.truetransact.uicomponent.CLabel();
        lblScheduleNo = new com.see.truetransact.uicomponent.CLabel();
        lblInstrumentNo1ID = new com.see.truetransact.uicomponent.CLabel();
        lblInstrumentDtID = new com.see.truetransact.uicomponent.CLabel();
        cboClearingTypeID = new com.see.truetransact.uicomponent.CComboBox();
        cboScheduleNo = new com.see.truetransact.uicomponent.CComboBox();
        dtdInstrumentDtID = new com.see.truetransact.uicomponent.CDateField();
        txtInstrumentNo1ID = new com.see.truetransact.uicomponent.CTextField();
        txtInstrumentNo2ID = new com.see.truetransact.uicomponent.CTextField();
        lblInstrumentNo2ID = new com.see.truetransact.uicomponent.CLabel();
        lblAmountID = new com.see.truetransact.uicomponent.CLabel();
        txtAmountID = new com.see.truetransact.uicomponent.CTextField();
        cboInstrDetailsCurrency = new com.see.truetransact.uicomponent.CComboBox();
        lblInstrDetailsCurrency = new com.see.truetransact.uicomponent.CLabel();
        cboInstrumentTypeID = new com.see.truetransact.uicomponent.CComboBox();
        lblInstrumentTypeID = new com.see.truetransact.uicomponent.CLabel();
        panDraweeAccNo = new com.see.truetransact.uicomponent.CPanel();
        lblRemarksID = new com.see.truetransact.uicomponent.CLabel();
        txtRemarksID = new com.see.truetransact.uicomponent.CTextField();
        lblBranchCodeID = new com.see.truetransact.uicomponent.CLabel();
        lblBankCodeID = new com.see.truetransact.uicomponent.CLabel();
        lblBranchNameID = new com.see.truetransact.uicomponent.CLabel();
        lblBranchNameIDValue = new com.see.truetransact.uicomponent.CLabel();
        lblBankNameID = new com.see.truetransact.uicomponent.CLabel();
        lblBankNameIDValue = new com.see.truetransact.uicomponent.CLabel();
        lblDrawerAccNoID = new com.see.truetransact.uicomponent.CLabel();
        txtDrawerAccNoID = new com.see.truetransact.uicomponent.CTextField();
        lblValScheduleNo = new com.see.truetransact.uicomponent.CLabel();
        lblClearingDate = new com.see.truetransact.uicomponent.CLabel();
        dtclearingDtID = new com.see.truetransact.uicomponent.CDateField();
        txtBankCodeID = new com.see.truetransact.uicomponent.CTextField();
        txtBranchCodeID = new com.see.truetransact.uicomponent.CTextField();
        btnBranchCode = new com.see.truetransact.uicomponent.CButton();
        btnBankCode = new com.see.truetransact.uicomponent.CButton();
        panPayInSlipDetOC = new com.see.truetransact.uicomponent.CPanel();
        panInsidePISD = new com.see.truetransact.uicomponent.CPanel();
        panPayInSlip = new com.see.truetransact.uicomponent.CPanel();
        lblProdIdPISD = new com.see.truetransact.uicomponent.CLabel();
        lblAccHeadPISD = new com.see.truetransact.uicomponent.CLabel();
        lblAccNoPISD = new com.see.truetransact.uicomponent.CLabel();
        cboProdIdPISD = new com.see.truetransact.uicomponent.CComboBox();
        btnAccNoPISD = new com.see.truetransact.uicomponent.CButton();
        txtAccNoPISD = new com.see.truetransact.uicomponent.CTextField();
        sptHorizontalUpPISD = new com.see.truetransact.uicomponent.CSeparator();
        cboProductType = new com.see.truetransact.uicomponent.CComboBox();
        lblProductType = new com.see.truetransact.uicomponent.CLabel();
        lblAccountHeadDesc = new com.see.truetransact.uicomponent.CLabel();
        lblAccountHeadDescValue = new com.see.truetransact.uicomponent.CLabel();
        txtAccHeadValuePISD = new com.see.truetransact.uicomponent.CTextField();
        btnAccountHead = new com.see.truetransact.uicomponent.CButton();
        panButtonPISD = new com.see.truetransact.uicomponent.CPanel();
        btnNewPISD = new com.see.truetransact.uicomponent.CButton();
        btnSavePISD = new com.see.truetransact.uicomponent.CButton();
        btnDeletePISD = new com.see.truetransact.uicomponent.CButton();
        btnEndPISD = new com.see.truetransact.uicomponent.CButton();
        lblAccHolderNameValuePISD = new com.see.truetransact.uicomponent.CLabel();
        lblAmountPISD = new com.see.truetransact.uicomponent.CLabel();
        txtAmountPISD = new com.see.truetransact.uicomponent.CTextField();
        lblRemarksPISD = new com.see.truetransact.uicomponent.CLabel();
        txtRemarksPISD = new com.see.truetransact.uicomponent.CTextField();
        panLableValues = new com.see.truetransact.uicomponent.CPanel();
        srpTCPISD = new com.see.truetransact.uicomponent.CScrollPane();
        tblPISD = new com.see.truetransact.uicomponent.CTable();
        sptVerticalPISD = new com.see.truetransact.uicomponent.CSeparator();
        panTotalAmountPISD = new com.see.truetransact.uicomponent.CPanel();
        lblTotalAmountPISD = new com.see.truetransact.uicomponent.CLabel();
        lblTotalAmountValuePISD = new com.see.truetransact.uicomponent.CLabel();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace = new com.see.truetransact.uicomponent.CLabel();
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
        setMaximumSize(new java.awt.Dimension(850, 662));
        setMinimumSize(new java.awt.Dimension(850, 662));
        setPreferredSize(new java.awt.Dimension(850, 662));
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                frameClosed(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        btnView1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView1.setToolTipText("Enquiry");
        btnView1.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView1.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView1.setEnabled(false);
        btnView1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnView1ActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnView1);

        lblSpace4.setText("     ");
        tbrAdvances.add(lblSpace4);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnNew);

        lblSpace17.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace17.setText("     ");
        lblSpace17.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace17);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnEdit);

        lblSpace18.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace18.setText("     ");
        lblSpace18.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace18);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnDelete);

        lblSpace2.setText("     ");
        tbrAdvances.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnSave);

        lblSpace19.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace19.setText("     ");
        lblSpace19.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace19);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnCancel);

        lblSpace3.setText("     ");
        tbrAdvances.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnAuthorize);

        lblSpace20.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace20.setText("     ");
        lblSpace20.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace20);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnException);

        lblSpace21.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace21.setText("     ");
        lblSpace21.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace21);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnReject);

        lblSpace5.setText("     ");
        tbrAdvances.add(lblSpace5);

        btnRegularize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/regularisation.gif"))); // NOI18N
        btnRegularize.setToolTipText("Regularize");
        btnRegularize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegularizeActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnRegularize);

        lblSpace22.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace22.setText("     ");
        lblSpace22.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace22);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnPrint);

        lblSpace23.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace23.setText("     ");
        lblSpace23.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace23.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace23.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace23);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnClose);

        getContentPane().add(tbrAdvances, java.awt.BorderLayout.NORTH);

        panMainOC.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panMainOC.setMinimumSize(new java.awt.Dimension(840, 610));
        panMainOC.setPreferredSize(new java.awt.Dimension(840, 610));
        panMainOC.setLayout(new java.awt.GridBagLayout());

        panBatchIdOC.setMinimumSize(new java.awt.Dimension(360, 23));
        panBatchIdOC.setPreferredSize(new java.awt.Dimension(360, 23));
        panBatchIdOC.setLayout(new java.awt.GridBagLayout());

        lblBatchIdOC.setText("Batch Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBatchIdOC.add(lblBatchIdOC, gridBagConstraints);

        txtBatchIdOC.setEditable(false);
        txtBatchIdOC.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBatchIdOC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBatchIdOCActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBatchIdOC.add(txtBatchIdOC, gridBagConstraints);

        lblCreatedBy.setText("Created By");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 25, 4, 4);
        panBatchIdOC.add(lblCreatedBy, gridBagConstraints);

        lblCreatedByValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblCreatedByValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBatchIdOC.add(lblCreatedByValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMainOC.add(panBatchIdOC, gridBagConstraints);

        panOutDetails.setMinimumSize(new java.awt.Dimension(480, 23));
        panOutDetails.setPreferredSize(new java.awt.Dimension(480, 23));
        panOutDetails.setLayout(new java.awt.GridBagLayout());

        lblBookedInstr.setText("No Instrument Booked");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOutDetails.add(lblBookedInstr, gridBagConstraints);

        lblBookedAmt.setText("BookedAmount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 15, 4, 4);
        panOutDetails.add(lblBookedAmt, gridBagConstraints);

        lblValNoInstrBooked.setMinimumSize(new java.awt.Dimension(50, 16));
        lblValNoInstrBooked.setPreferredSize(new java.awt.Dimension(50, 16));
        lblValNoInstrBooked.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                lblValNoInstrBookedFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOutDetails.add(lblValNoInstrBooked, gridBagConstraints);

        lblValAmount.setMinimumSize(new java.awt.Dimension(80, 16));
        lblValAmount.setPreferredSize(new java.awt.Dimension(80, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOutDetails.add(lblValAmount, gridBagConstraints);

        btnView.setText("View");
        btnView.setMaximumSize(new java.awt.Dimension(63, 23));
        btnView.setMinimumSize(new java.awt.Dimension(63, 23));
        btnView.setPreferredSize(new java.awt.Dimension(63, 23));
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOutDetails.add(btnView, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMainOC.add(panOutDetails, gridBagConstraints);

        panInstrDetOC.setBorder(javax.swing.BorderFactory.createTitledBorder("Instrument Details"));
        panInstrDetOC.setMaximumSize(new java.awt.Dimension(644, 259));
        panInstrDetOC.setLayout(new java.awt.GridBagLayout());

        panButtonID.setLayout(new java.awt.GridBagLayout());

        btnNewID.setText("New");
        btnNewID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButtonID.add(btnNewID, gridBagConstraints);

        btnSaveID.setText("Save");
        btnSaveID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButtonID.add(btnSaveID, gridBagConstraints);

        btnDeleteID.setText("Delete");
        btnDeleteID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButtonID.add(btnDeleteID, gridBagConstraints);

        btnEndID.setText("End Instruments");
        btnEndID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEndIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButtonID.add(btnEndID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panInstrDetOC.add(panButtonID, gridBagConstraints);

        sptVerticalID.setOrientation(javax.swing.SwingConstants.VERTICAL);
        sptVerticalID.setMinimumSize(new java.awt.Dimension(2, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstrDetOC.add(sptVerticalID, gridBagConstraints);

        srpTCID.setMinimumSize(new java.awt.Dimension(260, 100));
        srpTCID.setPreferredSize(new java.awt.Dimension(260, 100));

        tblID.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "S. N.", "Clearing ID", "Instrument No", "Drawer' Acc No", "Amount"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblID.setMinimumSize(new java.awt.Dimension(150, 0));
        tblID.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblIDMousePressed(evt);
            }
        });
        srpTCID.setViewportView(tblID);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstrDetOC.add(srpTCID, gridBagConstraints);

        panTotalAmountID.setLayout(new java.awt.GridBagLayout());

        lblTotalAmountID.setText("Total Instruments' Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotalAmountID.add(lblTotalAmountID, gridBagConstraints);

        lblTotalAmountValueID.setText("Value");
        lblTotalAmountValueID.setMinimumSize(new java.awt.Dimension(100, 16));
        lblTotalAmountValueID.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotalAmountID.add(lblTotalAmountValueID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panInstrDetOC.add(panTotalAmountID, gridBagConstraints);

        panComponentsID.setMaximumSize(new java.awt.Dimension(530, 197));
        panComponentsID.setMinimumSize(new java.awt.Dimension(530, 197));
        panComponentsID.setPreferredSize(new java.awt.Dimension(530, 197));
        panComponentsID.setLayout(new java.awt.GridBagLayout());

        panInsideDI.setMaximumSize(new java.awt.Dimension(239, 197));
        panInsideDI.setLayout(new java.awt.GridBagLayout());

        lblClearingTypeID.setText("Clearing Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInsideDI.add(lblClearingTypeID, gridBagConstraints);

        lblScheduleNo.setText("ScheduleNo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInsideDI.add(lblScheduleNo, gridBagConstraints);

        lblInstrumentNo1ID.setText("Instrument No Part- I");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInsideDI.add(lblInstrumentNo1ID, gridBagConstraints);

        lblInstrumentDtID.setText("Instrument Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInsideDI.add(lblInstrumentDtID, gridBagConstraints);

        cboClearingTypeID.setMinimumSize(new java.awt.Dimension(100, 21));
        cboClearingTypeID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboClearingTypeIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInsideDI.add(cboClearingTypeID, gridBagConstraints);

        cboScheduleNo.setMinimumSize(new java.awt.Dimension(100, 21));
        cboScheduleNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboScheduleNoActionPerformed(evt);
            }
        });
        cboScheduleNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboScheduleNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInsideDI.add(cboScheduleNo, gridBagConstraints);

        dtdInstrumentDtID.setMinimumSize(new java.awt.Dimension(100, 21));
        dtdInstrumentDtID.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInsideDI.add(dtdInstrumentDtID, gridBagConstraints);

        txtInstrumentNo1ID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInstrumentNo1ID.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInsideDI.add(txtInstrumentNo1ID, gridBagConstraints);

        txtInstrumentNo2ID.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInsideDI.add(txtInstrumentNo2ID, gridBagConstraints);

        lblInstrumentNo2ID.setText("Instrument No Part- II");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInsideDI.add(lblInstrumentNo2ID, gridBagConstraints);

        lblAmountID.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInsideDI.add(lblAmountID, gridBagConstraints);

        txtAmountID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAmountID.setValidation(new NumericValidation());
        txtAmountID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAmountIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInsideDI.add(txtAmountID, gridBagConstraints);

        cboInstrDetailsCurrency.setMinimumSize(new java.awt.Dimension(0, 0));
        cboInstrDetailsCurrency.setPreferredSize(new java.awt.Dimension(0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInsideDI.add(cboInstrDetailsCurrency, gridBagConstraints);

        lblInstrDetailsCurrency.setText("Currency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInsideDI.add(lblInstrDetailsCurrency, gridBagConstraints);

        cboInstrumentTypeID.setMinimumSize(new java.awt.Dimension(100, 21));
        cboInstrumentTypeID.setPopupWidth(200);
        cboInstrumentTypeID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboInstrumentTypeIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInsideDI.add(cboInstrumentTypeID, gridBagConstraints);

        lblInstrumentTypeID.setText("Instrument Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInsideDI.add(lblInstrumentTypeID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panComponentsID.add(panInsideDI, gridBagConstraints);

        panDraweeAccNo.setMaximumSize(new java.awt.Dimension(270, 180));
        panDraweeAccNo.setMinimumSize(new java.awt.Dimension(270, 180));
        panDraweeAccNo.setPreferredSize(new java.awt.Dimension(270, 180));
        panDraweeAccNo.setLayout(new java.awt.GridBagLayout());

        lblRemarksID.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panDraweeAccNo.add(lblRemarksID, gridBagConstraints);

        txtRemarksID.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panDraweeAccNo.add(txtRemarksID, gridBagConstraints);

        lblBranchCodeID.setText("Branch Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panDraweeAccNo.add(lblBranchCodeID, gridBagConstraints);

        lblBankCodeID.setText("Bank Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panDraweeAccNo.add(lblBankCodeID, gridBagConstraints);

        lblBranchNameID.setText("Branch Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panDraweeAccNo.add(lblBranchNameID, gridBagConstraints);

        lblBranchNameIDValue.setForeground(new java.awt.Color(0, 51, 204));
        lblBranchNameIDValue.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        lblBranchNameIDValue.setMaximumSize(new java.awt.Dimension(180, 21));
        lblBranchNameIDValue.setMinimumSize(new java.awt.Dimension(180, 21));
        lblBranchNameIDValue.setPreferredSize(new java.awt.Dimension(180, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panDraweeAccNo.add(lblBranchNameIDValue, gridBagConstraints);

        lblBankNameID.setText("Bank Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panDraweeAccNo.add(lblBankNameID, gridBagConstraints);

        lblBankNameIDValue.setForeground(new java.awt.Color(0, 51, 204));
        lblBankNameIDValue.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        lblBankNameIDValue.setMaximumSize(new java.awt.Dimension(180, 21));
        lblBankNameIDValue.setMinimumSize(new java.awt.Dimension(180, 21));
        lblBankNameIDValue.setPreferredSize(new java.awt.Dimension(180, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panDraweeAccNo.add(lblBankNameIDValue, gridBagConstraints);

        lblDrawerAccNoID.setText("Drawer Acc No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panDraweeAccNo.add(lblDrawerAccNoID, gridBagConstraints);

        txtDrawerAccNoID.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panDraweeAccNo.add(txtDrawerAccNoID, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panDraweeAccNo.add(lblValScheduleNo, gridBagConstraints);

        lblClearingDate.setText("Clearing Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panDraweeAccNo.add(lblClearingDate, gridBagConstraints);

        dtclearingDtID.setMinimumSize(new java.awt.Dimension(100, 21));
        dtclearingDtID.setPreferredSize(new java.awt.Dimension(100, 21));
        dtclearingDtID.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panDraweeAccNo.add(dtclearingDtID, gridBagConstraints);

        txtBankCodeID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBankCodeID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBankCodeIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panDraweeAccNo.add(txtBankCodeID, gridBagConstraints);

        txtBranchCodeID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBranchCodeID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBranchCodeIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panDraweeAccNo.add(txtBranchCodeID, gridBagConstraints);

        btnBranchCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnBranchCode.setMaximumSize(new java.awt.Dimension(21, 21));
        btnBranchCode.setMinimumSize(new java.awt.Dimension(21, 21));
        btnBranchCode.setPreferredSize(new java.awt.Dimension(21, 21));
        btnBranchCode.setEnabled(false);
        btnBranchCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBranchCodeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panDraweeAccNo.add(btnBranchCode, gridBagConstraints);

        btnBankCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnBankCode.setMaximumSize(new java.awt.Dimension(21, 21));
        btnBankCode.setMinimumSize(new java.awt.Dimension(21, 21));
        btnBankCode.setPreferredSize(new java.awt.Dimension(21, 21));
        btnBankCode.setEnabled(false);
        btnBankCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBankCodeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panDraweeAccNo.add(btnBankCode, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panComponentsID.add(panDraweeAccNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panInstrDetOC.add(panComponentsID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panMainOC.add(panInstrDetOC, gridBagConstraints);

        panPayInSlipDetOC.setBorder(javax.swing.BorderFactory.createTitledBorder("Pay In Slip Details"));
        panPayInSlipDetOC.setMinimumSize(new java.awt.Dimension(828, 350));
        panPayInSlipDetOC.setPreferredSize(new java.awt.Dimension(961, 350));
        panPayInSlipDetOC.setLayout(new java.awt.GridBagLayout());

        panInsidePISD.setMinimumSize(new java.awt.Dimension(500, 350));
        panInsidePISD.setPreferredSize(new java.awt.Dimension(500, 350));
        panInsidePISD.setLayout(new java.awt.GridBagLayout());

        panPayInSlip.setMaximumSize(new java.awt.Dimension(260, 280));
        panPayInSlip.setMinimumSize(new java.awt.Dimension(260, 280));
        panPayInSlip.setPreferredSize(new java.awt.Dimension(260, 280));
        panPayInSlip.setLayout(new java.awt.GridBagLayout());

        lblProdIdPISD.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panPayInSlip.add(lblProdIdPISD, gridBagConstraints);

        lblAccHeadPISD.setText("Account Head ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panPayInSlip.add(lblAccHeadPISD, gridBagConstraints);

        lblAccNoPISD.setText("Account Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panPayInSlip.add(lblAccNoPISD, gridBagConstraints);

        cboProdIdPISD.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdIdPISD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdIdPISDActionPerformed(evt);
            }
        });
        cboProdIdPISD.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboProdIdPISDFocusLost(evt);
            }
        });
        cboProdIdPISD.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboProdIdPISDItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 0);
        panPayInSlip.add(cboProdIdPISD, gridBagConstraints);

        btnAccNoPISD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccNoPISD.setMaximumSize(new java.awt.Dimension(21, 21));
        btnAccNoPISD.setMinimumSize(new java.awt.Dimension(21, 21));
        btnAccNoPISD.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccNoPISD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccNoPISDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        panPayInSlip.add(btnAccNoPISD, gridBagConstraints);

        txtAccNoPISD.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccNoPISD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAccNoPISDActionPerformed(evt);
            }
        });
        txtAccNoPISD.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccNoPISDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 3, 0);
        panPayInSlip.add(txtAccNoPISD, gridBagConstraints);

        sptHorizontalUpPISD.setMinimumSize(new java.awt.Dimension(0, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 7, 0);
        panPayInSlip.add(sptHorizontalUpPISD, gridBagConstraints);

        cboProductType.setMaximumSize(new java.awt.Dimension(100, 21));
        cboProductType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 0);
        panPayInSlip.add(cboProductType, gridBagConstraints);

        lblProductType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panPayInSlip.add(lblProductType, gridBagConstraints);

        lblAccountHeadDesc.setText("Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panPayInSlip.add(lblAccountHeadDesc, gridBagConstraints);

        lblAccountHeadDescValue.setForeground(new java.awt.Color(0, 51, 204));
        lblAccountHeadDescValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblAccountHeadDescValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblAccountHeadDescValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panPayInSlip.add(lblAccountHeadDescValue, gridBagConstraints);

        txtAccHeadValuePISD.setEditable(false);
        txtAccHeadValuePISD.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panPayInSlip.add(txtAccHeadValuePISD, gridBagConstraints);

        btnAccountHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccountHead.setMaximumSize(new java.awt.Dimension(21, 21));
        btnAccountHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnAccountHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccountHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccountHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 1);
        panPayInSlip.add(btnAccountHead, gridBagConstraints);

        panButtonPISD.setLayout(new java.awt.GridBagLayout());

        btnNewPISD.setText("New");
        btnNewPISD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewPISDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButtonPISD.add(btnNewPISD, gridBagConstraints);

        btnSavePISD.setText("Save");
        btnSavePISD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSavePISDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButtonPISD.add(btnSavePISD, gridBagConstraints);

        btnDeletePISD.setText("Delete");
        btnDeletePISD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeletePISDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButtonPISD.add(btnDeletePISD, gridBagConstraints);

        btnEndPISD.setText("End Pay in Slip");
        btnEndPISD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEndPISDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButtonPISD.add(btnEndPISD, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panPayInSlip.add(panButtonPISD, gridBagConstraints);

        lblAccHolderNameValuePISD.setForeground(new java.awt.Color(0, 51, 204));
        lblAccHolderNameValuePISD.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblAccHolderNameValuePISD.setMinimumSize(new java.awt.Dimension(100, 16));
        lblAccHolderNameValuePISD.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 25, 0, 0);
        panPayInSlip.add(lblAccHolderNameValuePISD, gridBagConstraints);

        lblAmountPISD.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panPayInSlip.add(lblAmountPISD, gridBagConstraints);

        txtAmountPISD.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAmountPISD.setValidation(new NumericValidation());
        txtAmountPISD.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAmountPISDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 0);
        panPayInSlip.add(txtAmountPISD, gridBagConstraints);

        lblRemarksPISD.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panPayInSlip.add(lblRemarksPISD, gridBagConstraints);

        txtRemarksPISD.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 0);
        panPayInSlip.add(txtRemarksPISD, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInsidePISD.add(panPayInSlip, gridBagConstraints);

        panLableValues.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panLableValues.setMinimumSize(new java.awt.Dimension(217, 308));
        panLableValues.setPreferredSize(new java.awt.Dimension(217, 308));
        panLableValues.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panInsidePISD.add(panLableValues, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panPayInSlipDetOC.add(panInsidePISD, gridBagConstraints);

        srpTCPISD.setMinimumSize(new java.awt.Dimension(260, 100));
        srpTCPISD.setPreferredSize(new java.awt.Dimension(260, 100));

        tblPISD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "S. No", "Pay In ID", "Prod ID", "Account No", "Amount"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPISD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblPISDMousePressed(evt);
            }
        });
        srpTCPISD.setViewportView(tblPISD);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panPayInSlipDetOC.add(srpTCPISD, gridBagConstraints);

        sptVerticalPISD.setOrientation(javax.swing.SwingConstants.VERTICAL);
        sptVerticalPISD.setMinimumSize(new java.awt.Dimension(2, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPayInSlipDetOC.add(sptVerticalPISD, gridBagConstraints);

        panTotalAmountPISD.setLayout(new java.awt.GridBagLayout());

        lblTotalAmountPISD.setText("Total Pay In Slip Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotalAmountPISD.add(lblTotalAmountPISD, gridBagConstraints);

        lblTotalAmountValuePISD.setText("Amount");
        lblTotalAmountValuePISD.setMinimumSize(new java.awt.Dimension(100, 16));
        lblTotalAmountValuePISD.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotalAmountPISD.add(lblTotalAmountValuePISD, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panPayInSlipDetOC.add(panTotalAmountPISD, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panMainOC.add(panPayInSlipDetOC, gridBagConstraints);

        getContentPane().add(panMainOC, java.awt.BorderLayout.CENTER);

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

    private void cboProdIdPISDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboProdIdPISDFocusLost
        // TODO add your handling code here:
        txtAccNoPISD.setText("");
    }//GEN-LAST:event_cboProdIdPISDFocusLost

    private void txtAccNoPISDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccNoPISDFocusLost
        // TODO add your handling code here:
        if(txtAccNoPISD.getText().length()>0){
            observable.setCboProductType("");
            String ACCOUNTNO = txtAccNoPISD.getText();
            if ((!(observable.getCboProductType().length()>0)) && ACCOUNTNO.length()>0) {
                if (observable.checkAcNoWithoutProdType(ACCOUNTNO)) {
                    txtAccNoPISD.setText(observable.getTxtAccNoPISD());
                    cboProdIdPISD.setModel(observable.getCbmProdIdPISD());
                    cboProdIdPISDItemStateChanged(null);
                    String prodType = ((ComboBoxModel)cboProductType.getModel()).getKeyForSelected().toString();
                } else {
                    ClientUtil.showAlertWindow("Invalid Account No.");
                    txtAccNoPISD.setText("");
                    return;
                }
            }
            HashMap hash = new HashMap();
            hash.put("ACCOUNTNO",txtAccNoPISD.getText());
            viewType = "AccountNoPISD";
            fillData(hash);
        }
    }//GEN-LAST:event_txtAccNoPISDFocusLost

    private void btnView1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnView1ActionPerformed
        // TODO add your handling code here:
        observable.setOperation(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        this.lblStatus.setText(observable.getLblStatus());
        callView("Enquirystatus");
        btnCheck();
    }//GEN-LAST:event_btnView1ActionPerformed

    private void cboInstrumentTypeIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboInstrumentTypeIDActionPerformed
        // TODO add your handling code here:
        cboInstrumentTypeIDActionPerformed();
//        txtInstrumentNo1ID.setText(CommonUtil.convertObjToStr(((ComboBoxModel)cboInstrumentTypeID.getModel()).getKeyForSelected()));
    }//GEN-LAST:event_cboInstrumentTypeIDActionPerformed
    private void cboInstrumentTypeIDActionPerformed(){
        txtInstrumentNo1ID.setText(CommonUtil.convertObjToStr(((ComboBoxModel)cboInstrumentTypeID.getModel()).getKeyForSelected()));
    }
    private void cboScheduleNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboScheduleNoFocusLost
        // TODO add your handling code here:
        if(observable.getOperation()==ClientConstants.ACTIONTYPE_NEW){
            if (observable.getCboScheduleNo()!=null && observable.getCboScheduleNo().length()>0)
                if (observable.checkForLotSize()) {
                    displayAlert("Lot size exceeding for this Schedule...");
                    cboScheduleNo.setSelectedItem("");
                    //            cboScheduleNo.requestFocus();
                }
        }
    }//GEN-LAST:event_cboScheduleNoFocusLost
    
    private void lblValNoInstrBookedFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_lblValNoInstrBookedFocusLost
        // TODO add your handling code here:
        
    }//GEN-LAST:event_lblValNoInstrBookedFocusLost
    
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        if(view=="view"){
            viewType="AUTHORIZE";
            HashMap ViewMap=new HashMap();
            HashMap where =new HashMap();
            ViewMap.put(CommonConstants.MAP_NAME,"getViewOutwardClearing");
            String Schno=null;
            if(CommonUtil.convertObjToStr(cboScheduleNo.getSelectedItem()).length()>0)
                Schno=CommonUtil.convertObjToStr(cboScheduleNo.getSelectedItem());
            
            where.put("SCHEDULE_NO", Schno);
            where.put("CLEARING_TYPE", cboClearingTypeID.getSelectedItem());
            
            Date IsDt = DateUtil.getDateMMDDYYYY(dtclearingDtID.getDateValue());
            if(IsDt != null){
            Date isDate = (Date)curDate.clone();
            isDate.setDate(IsDt.getDate());
            isDate.setMonth(IsDt.getMonth());
            isDate.setYear(IsDt.getYear());
            where.put("OUTWARD_DT", isDate);
            }else{
               where.put("OUTWARD_DT",DateUtil.getDateMMDDYYYY(dtclearingDtID.getDateValue())); 
            }
//            where.put("OUTWARD_DT",DateUtil.getDateMMDDYYYY(dtclearingDtID.getDateValue()));
            ViewMap.put(CommonConstants.MAP_WHERE,where);
            System.out.println("ViewMap========"+ViewMap);
            new ViewAll(this, ViewMap).show();
            ClientUtil.enableDisable(this,false);
            btnNewID.setEnabled(false);
            btnNewPISD.setEnabled(false);
            view=null;
        }
        else{
            btnCancelActionPerformed(null);
            view="view";
            ClientUtil.enableDisable(this,false);
            btnDisableForView();
            dtclearingDtID.setEnabled(true);
        }
        
        
        
        
    }//GEN-LAST:event_btnViewActionPerformed

    public void btnCheck(){
         btnCancel.setEnabled(true);
         btnSave.setEnabled(false);
         btnNew.setEnabled(false);
         btnDelete.setEnabled(false);
         btnAuthorize.setEnabled(false);
         btnReject.setEnabled(false);
         btnException.setEnabled(false);
         btnEdit.setEnabled(false);
     }
    
    private void btnDisableForView() {
        btnNew.setEnabled(false);
        
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        mitNew.setEnabled(false);
        mitEdit.setEnabled(false);
        mitDelete.setEnabled(false);
        
        btnSave.setEnabled(false);
        btnCancel.setEnabled(false);
        mitSave.setEnabled(false);
        mitCancel.setEnabled(false);
        
        
        btnNewID.setEnabled(false);
        btnSaveID.setEnabled(false);
        btnDeleteID.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnRegularize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnAccountHead.setEnabled(false);
        btnAccNoPISD.setEnabled(false);
        btnBankCode.setEnabled(false);
        btnBranchCode.setEnabled(false);
        btnCancel.setEnabled(true);
        cboClearingTypeID.setEnabled(true);
        cboScheduleNo.setEnabled(true);
        txtAmountPISD.setEditable(false);
        txtAccHeadValuePISD.setEditable(false);
    }
    
    private void txtAmountIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmountIDFocusLost
        // TODO add your handling code here:
        observable.setTxtAmountID(txtAmountID.getText());        
        if(observable.checkForHighValue()) {
            displayAlert("Amount given is less for this Clearing type...");
            txtAmountID.setText("");
            //           txtAmountID.requestFocus();
        }
       if(CommonUtil.convertObjToDouble(txtAmountID.getText()).doubleValue()>0){             
             txtAmountID.setToolTipText(CommonUtil.currencyToWord(CommonUtil.convertObjToDouble(txtAmountID.getText()).doubleValue()));             
         }else{
             txtAmountID.setToolTipText("Zero");
         }
    }//GEN-LAST:event_txtAmountIDFocusLost
    
    private void txtBatchIdOCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBatchIdOCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBatchIdOCActionPerformed
    
    private void txtBranchCodeIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBranchCodeIDActionPerformed
        // TODO add your handling code here:
        String otherBranchCode=txtBranchCodeID.getText();
        observable.setTxtBranchCode(otherBranchCode);
        if(otherBranchCode !=null && otherBranchCode.length()>0){
            HashMap hash=new HashMap();
            hash.put("BANK_CODE",observable.getTxtBankCode());
            hash.put("OTHER_BRANCH_CODE",otherBranchCode);
            List lst=ClientUtil.executeQuery("Outward.getSelectBankBranch", hash);
            if(lst==null || lst.size()==0){
                ClientUtil.displayAlert("Enter Valide Branch Code");
                txtBranchCodeID.setText("");
            } else {
                lblBranchNameIDValue.setText(CommonUtil.convertObjToStr(((HashMap)lst.get(0)).get("BRANCH_NAME")));
            }
            
        }
    }//GEN-LAST:event_txtBranchCodeIDActionPerformed
    
    private void txtBankCodeIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBankCodeIDActionPerformed
        // TODO add your handling code here:
        String txtBankCodes=txtBankCodeID.getText();
        observable.setTxtBankCode(txtBankCodes);
        //         viewType="BANK_CODE";
        if(txtBankCodes!=null && txtBankCodes.length()>0){
            HashMap Map=new HashMap();
            Map.put("BANK_CODE",txtBankCodes);
            List lst =ClientUtil.executeQuery("Outward.getSelectBankCode", Map);
            System.out.println("lst####"+lst);
            if(lst==null || lst.size()==0){
                ClientUtil.displayAlert("Invalide BankCode");
                txtBankCodeID.setText("");
            } else {
                lblBankNameIDValue.setText(CommonUtil.convertObjToStr(((HashMap)lst.get(0)).get("BANK_NAME")));
            }
        }
    }//GEN-LAST:event_txtBankCodeIDActionPerformed
    
    private void btnBranchCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBranchCodeActionPerformed
        // TODO add your handling code here:
        viewType="BANK_BRANCH";
        HashMap viewMap=new HashMap();
        HashMap hash=new HashMap();
        hash.put("BANK_CODE",observable.getTxtBankCode());
        viewMap.put(CommonConstants.MAP_NAME,"Outward.getSelectBankBranch");
        viewMap.put(CommonConstants.MAP_WHERE,hash);
        new ViewAll(this,viewMap).show();
        
    }//GEN-LAST:event_btnBranchCodeActionPerformed
    
    private void btnBankCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBankCodeActionPerformed
        // TODO add your handling code here:
        viewType="BANK_CODE";
        HashMap viewMap=new HashMap();
        viewMap.put(CommonConstants.MAP_NAME,"Outward.getSelectBankCode");
        new ViewAll(this,viewMap).show();
    }//GEN-LAST:event_btnBankCodeActionPerformed
    
    private void cboScheduleNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboScheduleNoActionPerformed
        // TODO add your handling code here:
        if(cboScheduleNo.getSelectedItem()!=null && !cboScheduleNo.getSelectedItem().equals("")){
            //         if (observable.getCboScheduleNo()!=null && observable.getCboScheduleNo().length()>0){
            observable.setCboScheduleNo((String)cboScheduleNo.getSelectedItem());
            if((view!=null && view.equals("view"))|| observable.getOperation()==ClientConstants.ACTIONTYPE_REALIZE){
                observable.getViewClearingDate();
            }else{
                
                observable.getClearingDate();
            }
            //         if (observable.getCboScheduleNo()!=null && observable.getCboScheduleNo().length()>0){
            if(observable.getOperation()==ClientConstants.ACTIONTYPE_NEW){
                
                if (observable.checkForLotSize()) {
                    displayAlert("Lot size exceeding for this Schedule...");
                    cboScheduleNo.setSelectedItem("");
                    //            cboScheduleNo.requestFocus();
                }}
        }
    }//GEN-LAST:event_cboScheduleNoActionPerformed
    
    private void txtAccNoPISDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAccNoPISDActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtAccNoPISDActionPerformed
    
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
        HashMap reportParamMap = new HashMap();
        com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed
        private void cboProductTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductTypeActionPerformed
            //        String oldProd = CommonUtil.convertObjToStr(((ComboBoxModel)cboProductType.getModel()).getDataForKey(observable.getCboProductType()));
            //        String newProd = CommonUtil.convertObjToStr(cboProductType.getSelectedItem());
            //        String setSel = CommonUtil.convertObjToStr(cboProdIdPISD.getSelectedItem());
            if(cboProductType.getSelectedIndex() > 0) {
                String prodType = ((ComboBoxModel)cboProductType.getModel()).getKeyForSelected().toString();
                observable.setCboProductType(prodType);
                if (prodType.equals("GL")) {
                    cboProdIdPISD.setSelectedItem("");
                    txtAccNoPISD.setText("");
                    setProdEnable(false);
                    //                prodChange(oldProd,newProd);
                } else {
                    setProdEnable(true);
                    observable.setCbmProdIdPISD(prodType);
                    System.out.println("prodType : " + prodType);
                    cboProdIdPISD.setModel(observable.getCbmProdIdPISD());
                    //                prodChange(oldProd,newProd);
                    cboProdIdPISD.setSelectedItem(observable.getCbmProdIdPISD().getDataForKey(observable.getCboProdIdPISD()));
                }
            }
    }//GEN-LAST:event_cboProductTypeActionPerformed
        
        //--- Resets the PaySlip Details if the OldProduct and newProduct are not the same
        private void prodChange(String oldProd,String newProd){
            if(!oldProd.equals(newProd)){
                resetPayInslipDetOnProdChange();
            }
        }
        
        //--- To reset the PayInSlipDetails when the Product is changed
        private void resetPayInslipDetOnProdChange(){
            cboProdIdPISD.setSelectedItem("");
            txtAccNoPISD.setText("");
            txtAccHeadValuePISD.setText("");
            lblAccountHeadDescValue.setText("");
            lblAccHolderNameValuePISD.setText("");
            transDetails.setTransDetails(null,null,null);
        }
        
        private void setProdEnable(boolean isEnable) {
            cboProdIdPISD.setEnabled(isEnable);
            btnAccNoPISD.setEnabled(isEnable);
            btnAccountHead.setEnabled(!isEnable);
        }
        public void callingFromLodgement(HashMap billsMap){
            observable.setOperation(ClientConstants.ACTIONTYPE_NEW);
            ClientUtil.enableDisable(panInsideDI,true);
            ClientUtil.enableDisable(panDraweeAccNo,true);
            btnNewID.setEnabled(false);
            btnDeleteID.setEnabled(false);
            btnSaveID.setEnabled(true);
            txtInstrumentNo2ID.setText(CommonUtil.convertObjToStr(billsMap.get("INST_NO2")));
            txtAmountID.setText(CommonUtil.convertObjToStr(billsMap.get("AMOUNT")));
            txtBankCodeID.setText(CommonUtil.convertObjToStr(billsMap.get("BANK_CODE")));
            lblBankNameIDValue.setText(CommonUtil.convertObjToStr(billsMap.get("BANK_NAME")));
            txtBranchCodeID.setText(CommonUtil.convertObjToStr(billsMap.get("BRANCH_CODE")));
            lblBranchNameIDValue.setText(CommonUtil.convertObjToStr(billsMap.get("BRANCH_NAME")));
            ClientUtil.enableDisable(panPayInSlip,true);
            btnNewPISD.setEnabled(false);
            btnSavePISD.setEnabled(true);
            btnDeletePISD.setEnabled(false);
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(false);
            btnRegularize.setEnabled(false);
            btnEdit.setEnabled(false);
            btnNew.setEnabled(false);
            btnDelete.setEnabled(false);
            btnReject.setEnabled(false);
            btnSave.setEnabled(true);
            cboProductType.setSelectedItem(CommonUtil.convertObjToStr("General Ledger"));
            cboInstrumentTypeID.setSelectedItem(CommonUtil.convertObjToStr(billsMap.get("INSTRUMENT_TYPE")));
            cboInstrumentTypeIDActionPerformed();
            txtAccHeadValuePISD.setText(CommonUtil.convertObjToStr(billsMap.get("BILLSHEAD")));
            lblAccountHeadDescValue.setText(CommonUtil.convertObjToStr(billsMap.get("BILLSHEADDESC")));
            txtAmountPISD.setText(CommonUtil.convertObjToStr(billsMap.get("AMOUNT")));
            txtRemarksID.setText(CommonUtil.convertObjToStr(billsMap.get("REMARKS")));
            txtRemarksPISD.setText(CommonUtil.convertObjToStr(billsMap.get("LODGE_ID")));
            dtdInstrumentDtID.setDateValue(CommonUtil.convertObjToStr(billsMap.get("REMIT_INST_DT")));
            btnBankCode.setEnabled(true);
            btnBranchCode.setEnabled(true);
            txtAccNoPISD.setEditable(false);
            txtAccNoPISD.setEnabled(false);
            dtclearingDtID.setDateValue(CommonUtil.convertObjToStr(curDate));
            dtclearingDtID.setEnabled(false);
        }
    private void btnAccountHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccountHeadActionPerformed
        
        viewType = "AccHeadForPISD";
        final HashMap viewMap = new HashMap();
        viewMap.put(CommonConstants.MAP_NAME, "Outward.getSelectAcctHead");
        new ViewAll(this, viewMap).show();
    }            private void btnRegularizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-LAST:event_btnAccountHeadActionPerformed
        //        int yesno = COptionPane.showConfirmDialog(null, "Outward Returns should be completed before Regularization. Do you want to Continue ?", "Note", COptionPane.YES_NO_OPTION);
        //        if (yesno == COptionPane.YES_OPTION) {
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnRegularize.setEnabled(true);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnReject.setEnabled(false);
        
                observable.setOperation(ClientConstants.ACTIONTYPE_REALIZE);//GEN-FIRST:event_btnRegularizeActionPerformed
                //                  if(  real==null  ){
                
                //                      cboScheduleNo.setEnabled(true);
                //                      cboClearingTypeID.setEnabled(true);
                //                      dtclearingDtID.setEnabled(true);
                //                      real="real";
                //                      btnDisableForView();
                //                      btnRegularize.setEnabled(true);
                //                      btnCancel.setEnabled(true);
                //                      btnReject.setEnabled(false);
                //                  }
                //                  else{
                realizeUI(CommonConstants.STATUS_REALIZED);
                //                  }
                //        }
                btnBankCode.setEnabled(false);
                btnBranchCode.setEnabled(false);
                HashMap outcountMap=new HashMap();
                outcountMap.put("BATCH_ID",txtBatchIdOC.getText());
                outcountMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
                outcountMap.put("OUTWARD_DT", curDate.clone());
                List countOCRel= ClientUtil.executeQuery("getOutwardBatchCount",outcountMap);
                outcountMap=new HashMap();
                outcountMap=(HashMap)countOCRel.get(0);
                int count=CommonUtil.convertObjToInt(outcountMap.get("COUNT"));
                if( count>1){
                    isFilled=false;
                    ClientUtil.displayAlert("More then one instrument's are  Add \n Realize Through Realize All Screen");
                    btnCancel.setEnabled(true);
                    
                }
                 btnRegularize.setEnabled(true);
    }//GEN-LAST:event_btnRegularizeActionPerformed
    private void realizeUI(String realizationStatus){
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        
        if (viewType == "REALIZE" && isFilled) {
            HashMap singleRealizeMap = new HashMap();
            ArrayList arrList = new ArrayList();
            HashMap authDataMap = new HashMap();
/*            authDataMap.put("STATUS", realizationStatus);
            authDataMap.put("USER_ID", ProxyParameters.USER_ID);
            authDataMap.put("BID", txtBatchIdOC.getText());
            authDataMap.put("AUTHORIZEDT", curDate.clone());
            authDataMap.put("OUTWARD_AMOUNT",this.lblTotalAmountValueID.getText());
            authDataMap.put("PAY_IN_SLIP_AMOUNT",this.lblTotalAmountValuePISD.getText());*/
            //        if(status1.equals(CommonConstants.STATUS_REALIZED) && realizationStatus.equals(CommonConstants.STATUS_REJECTED)){
            //             observable.populateClearingType();
            //             if(observable.getBouncingClearingType()!= null && observable.getBouncingClearingType().length > 0){
            //                String bClearingType = (String) COptionPane.showInputDialog(null, resourceBundle.getString("BOUNCECLEARINGTYPE"), resourceBundle.getString("B_C_TYPE"), COptionPane.PLAIN_MESSAGE, null, observable.getBouncingClearingType(), observable.getBouncingClearingType()[0]);
            //                authDataMap.put("BounceClearingType", bClearingType);
            //
            //            }
            ////                 real=null;
            //        }
            authDataMap.put("BID", txtBatchIdOC.getText());
            authDataMap.put("CLEARING_TYPE", tempMap.get("CLEARING_TYPE")) ;
            authDataMap.put("SCHEDULE_NO", tempMap.get("SCHEDULE_NO"));
            authDataMap.put("CREATED_BY", tempMap.get("CREATED_BY"));
            authDataMap.put("INSTRUMENT_NO1", tempMap.get("INSTRUMENT_NO1"));
            authDataMap.put("INSTRUMENT_NO2", tempMap.get("INSTRUMENT_NO2"));

            authDataMap.put("OUTWARD_AMOUNT", tempMap.get("AMOUNT")); //  </OUTWARD_AMOUNT/>
            authDataMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            authDataMap.put("BATCH_STATUS", tempMap.get("BATCH_STATUS"));
            authDataMap.put("HIERARCHY_ID", tempMap.get("HIERARCHY_ID"));
            authDataMap.put("AUTHORIZEDT", curDate);
            authDataMap.put("BRANCH_CODE", tempMap.get("BRANCH_CODE"));
            authDataMap.put("USER_ID", tempMap.get("USER_ID"));
            arrList.add(authDataMap);
            singleRealizeMap.put(CommonConstants.AUTHORIZESTATUS, realizationStatus);
            singleRealizeMap.put("PREVIOUS_STATUS", status1);
            singleRealizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(singleRealizeMap);
            tempMap = new HashMap();
            viewType = "";
        } else {
            HashMap mapParam = new HashMap();
            bankButtonEnableDisable(false);
            btnCancelActionPerformed(null);
            
//            if(!realizationStatus.equalsIgnoreCase(CommonConstants.STATUS_REALIZED)){
         
               String  mapName = "getSelectOutwardClearingAuthorizeTOList";
                HashMap whereMap=new HashMap();
                whereMap.put("INITIATED_BRANCH",TrueTransactMain.BRANCH_ID);
                whereMap.put("OUTWARD_DT",curDate.clone());
                mapParam.put(CommonConstants.MAP_WHERE,whereMap);
                
//            }
            if(realizationStatus.equalsIgnoreCase(CommonConstants.STATUS_REALIZED)){
                
                mapName = "getSelectOutwardClearingRealizeTOList";
                mapParam.put("MULTISELECT", "Y");
                //                HashMap where=new HashMap();
                //                if(cboClearingTypeID.getSelectedItem()!=null && (!cboClearingTypeID.getSelectedItem().equals(""))  )
                //                  where.put("CLEARING_TYPE",cboClearingTypeID.getSelectedItem());
                //                if(cboScheduleNo.getSelectedItem()!=null && (!cboScheduleNo.getSelectedItem().equals("")) )
                //                where.put("SCHEDULE_NO",cboScheduleNo.getSelectedItem());
                //                if(dtclearingDtID.getDateValue()!=null &&!dtclearingDtID.getDateValue().equals(""))
                //                 where.put("OUTWARD_DT", DateUtil.getDateMMDDYYYY(dtclearingDtID.getDateValue()));
                //                mapParam.put(CommonConstants.MAP_WHERE, where);
                //                btnReject.setEnabled(true);
            }
            mapParam.put(CommonConstants.MAP_NAME,mapName);
            
            viewType = "REALIZE";
            status1= realizationStatus;
            isFilled = false;
            AuthorizeUI realizeUI = new AuthorizeUI(this, mapParam);
            if(realizationStatus.equalsIgnoreCase(CommonConstants.STATUS_REALIZED)){
                realizeUI.setAuthorize(false);
                realizeUI.setException(false);
                realizeUI.setReject(true);
                realizeUI.setRealize(true);
                realizeUI.setTitle("Realize");
                //                btnCancel.setEnabled(true);
            }
            realizeUI.show();
            btnSave.setEnabled(false);
        }
    }
    
    
    public void authorize(HashMap map) {
        //        //System.out.println ("Realize Map : " + map);
        
        if(status1.equals(CommonConstants.STATUS_REALIZED) && CommonUtil.convertObjToStr(map.get(CommonConstants.AUTHORIZESTATUS)).equals(CommonConstants.STATUS_REJECTED)){
            observable.populateClearingType();
            if(observable.getBouncingClearingType()!= null && observable.getBouncingClearingType().length > 0){
                String bClearingType = (String) COptionPane.showInputDialog(null, resourceBundle.getString("BOUNCECLEARINGTYPE"), resourceBundle.getString("B_C_TYPE"), COptionPane.PLAIN_MESSAGE, null, observable.getBouncingClearingType(), observable.getBouncingClearingType()[0]);
                map.put("BounceClearingType", bClearingType);
                
            }
            //                 real=null;
        }
        String remark = COptionPane.showInputDialog(this, CommonUtil.convertObjToStr(map.get(CommonConstants.AUTHORIZESTATUS)) + resourceBundle.getString("REMARK"),"");
        if(remark!=null) {
            map.put("REMARKS", remark);
            
            //        if(observable.getOperation()==ClientConstants.ACTIONTYPE_AUTHORIZE)
            //            batchTallying(map);
            
            observable.setAuthorizeMap(map);
            observable.doAction(CommonConstants.STATUS_REALIZED);
            if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
                super.setOpenForEditBy(observable.getStatusBy());
                super.removeEditLock(txtBatchIdOC.getText());
            }
            
            observable.setAuthorizeMap(null);
            btnCancelActionPerformed(null);
            observable.setResultStatus();
        }
        else {
            observable.setLblStatus(ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL]);
        }
        
        lblStatus.setText(observable.getLblStatus());
    }
    
    private void batchTallying(HashMap map) {
        ArrayList selectedBatch = (ArrayList)map.get(CommonConstants.AUTHORIZEDATA);
        int size = selectedBatch.size();
        HashMap objBatch;
        String outwardAmt,payInSlipAmt;
        for(int i=0;i<size;i++) {
            objBatch = (HashMap)selectedBatch.get(i);
            outwardAmt=(String)objBatch.get("OUTWARD_AMOUNT");
            payInSlipAmt=(String)objBatch.get("PAY_IN_SLIP_AMOUNT");
            if(!outwardAmt.equalsIgnoreCase(payInSlipAmt)) {
                COptionPane.showMessageDialog(this,resourceBundle.getString("dialogForDiffAmt"));
                selectedBatch.remove(i);
                i--;
                size=size-1;
            }
        }
        map.put(CommonConstants.AUTHORIZEDATA, selectedBatch);
    }
    
    private int checkTally(){
        String outwardAmt = lblTotalAmountValueID.getText();
        String payInSlipAmt = lblTotalAmountValuePISD.getText();
        if(outwardAmt==null || payInSlipAmt==null ||
        outwardAmt.equalsIgnoreCase("0.0") || payInSlipAmt.equalsIgnoreCase("0.0")) {
            COptionPane.showMessageDialog(this,resourceBundle.getString("WARNING_MINENTRY"));
            return -1;
        }
        if(!(lblTotalAmountValueID.getText().equals(lblTotalAmountValuePISD.getText()))){
            COptionPane checkAmt = new COptionPane();
            String[] options = {resourceBundle.getString("warningMessageOK")};
            int j=checkAmt.showOptionDialog(null,resourceBundle.getString("dialogForDiffAmt"),CommonConstants.WARNINGTITLE,COptionPane.OK_OPTION,COptionPane.WARNING_MESSAGE, null, options, options[0]);
            return -1;
        }
        if(tblID.getRowCount() > 1 && tblPISD.getRowCount() > 1){
            COptionPane checkAmt = new COptionPane();
            String[] options = {resourceBundle.getString("warningMessageOK")};
            int j=checkAmt.showOptionDialog(null,resourceBundle.getString("WARNING_COUNTTALLY"),CommonConstants.WARNINGTITLE,COptionPane.OK_OPTION,COptionPane.WARNING_MESSAGE, null, options, options[0]);
            return -1;
        }
        return 0;
    }    private void txtAmountPISDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmountPISDFocusLost
        //        String amt = txtAmountPISD.getText();
        //        try {
        //            if(amt!=null && amt.length()>0){
        //                String covAmt = String.valueOf(
        //                    ClientUtil.convertCurrency(
        //                    observable.getProdCurrency(),
        //                    ClientUtil.getMainCurrency(),
        //                    "CREDIT", new Double(txtAmountPISD.getText()).doubleValue()));
        //
        //                observable.setTxtConvAmt(covAmt);
        //            }
        //        } catch (Exception e) {
        //            observable.setTxtConvAmt(txtAmountPISD.getText());
        //        }
        if(CommonUtil.convertObjToDouble(txtAmountPISD.getText()).doubleValue()>0){             
             txtAmountPISD.setToolTipText(CommonUtil.currencyToWord(CommonUtil.convertObjToDouble(txtAmountPISD.getText()).doubleValue()));             
         }else{
             txtAmountPISD.setToolTipText("Zero");
         }
    }//GEN-LAST:event_txtAmountPISDFocusLost
        private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
            observable.setOperation(ClientConstants.ACTIONTYPE_EXCEPTION);
            realizeUI(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
            private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
                observable.setOperation(ClientConstants.ACTIONTYPE_REJECT);
                realizeUI(CommonConstants.STATUS_REJECTED);
                btnBankCode.setEnabled(false);
                btnBranchCode.setEnabled(false);
                btnCancel.setEnabled(true);
                btnAuthorize.setEnabled(false);
                btnRegularize.setEnabled(false);
                btnEdit.setEnabled(false);
                btnNew.setEnabled(false);
                btnDelete.setEnabled(false);
                btnReject.setEnabled(true);
    }//GEN-LAST:event_btnRejectActionPerformed
                private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
                    observable.setOperation(ClientConstants.ACTIONTYPE_AUTHORIZE);
                    realizeUI(CommonConstants.STATUS_AUTHORIZED);
                    btnBankCode.setEnabled(false);
                    btnBranchCode.setEnabled(false);
                    btnCancel.setEnabled(true);
                    btnAuthorize.setEnabled(true);
                    btnRegularize.setEnabled(false);
                    btnEdit.setEnabled(false);
                    btnNew.setEnabled(false);
                    btnDelete.setEnabled(false);
                    btnReject.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
                    private void cboClearingTypeIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboClearingTypeIDActionPerformed
                        observable.setCboClearingTypeID((String) cboClearingTypeID.getSelectedItem());
                        
                        //--- If an item is selected from cboClearingTypeID set the Schedule No appropriate
                        //--- to that Clearing Type  Id else set it to nothing
                        if (observable.getCboClearingTypeID().length() > 0) {
                            System.out.println("view  ------------"+view);
                            if((view!=null && view.equals("view"))|| observable.getOperation()==ClientConstants.ACTIONTYPE_REALIZE){
                                observable.getViewScheduleNoForProd();
                            } else {
                                if (observable.getOperation()!=ClientConstants.ACTIONTYPE_REALIZE) {
                                    observable.getScheduleNoForProd();
                                    updateClearingTypeAndScheduleNo();
                                }
                            }
                        } else if (observable.getCboClearingTypeID().length() == 0) {
                            observable.setLblValScheduleNo("");
                            updateClearingTypeAndScheduleNo();
                        }
                        
                        if (observable.getOperation() == ClientConstants.ACTIONTYPE_NEW && cboClearingTypeID.getSelectedIndex() > 0) {
                            //            if (observable.getLblValScheduleNo() == null || observable.getLblValScheduleNo().trim().length() == 0 ) {
                            //                cboClearingTypeID.setSelectedIndex(0);
                            //                COptionPane.showMessageDialog(this, resourceBundle.getString("WARNING_CREATE"));
                            //            }
                        }
    }//GEN-LAST:event_cboClearingTypeIDActionPerformed
                    private void updateClearingTypeAndScheduleNo(){
                        cboClearingTypeID.setSelectedItem(observable.getCboClearingTypeID());
                        lblValScheduleNo.setText(observable.getLblValScheduleNo());
                    }
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
        private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
            cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
            private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
                btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
                private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
                    btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
                    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
                        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed
                        private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
                            btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
                            private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
                                btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
                                private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
                                    ClientUtil.enableDisable(panMainOC, false);
                                    setModified(false);
                                    transDetails.setTransDetails(null,null,null);
                                    setButtonEnableDisable();
                                    observable.btnEndPISDAction();
                                    txtRemarksID.setText("");
                                    observable.resetOB();
                                    
                                    
                                    this.lblStatus.setText(observable.getLblStatus());
                                    
                                    observable.setOperation(ClientConstants.ACTIONTYPE_CANCEL);
                                    observable.setStatus();
                                    this.lblStatus.setText(observable.getLblStatus());
                                    
                                    cboClearingTypeID.setSelectedItem("");
                                    lblValScheduleNo.setText("");
                                    
                                    resetUIFields();
                                    
                                    this.disableIDButtons();
                                    this.disablePISDButtons();
                                    btnReject.setEnabled(true);
                                    btnAuthorize.setEnabled(true);
                                    btnException.setEnabled(true);
                                    lblBranchNameIDValue.setText("");
                                    lblBankNameIDValue.setText("");
                                    view=null;
                                    real=null;
                                    
    }//GEN-LAST:event_btnCancelActionPerformed
                                    private void frameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_frameClosed
                                        btnCloseActionPerformed(null);
    }//GEN-LAST:event_frameClosed
                                        private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
                                            observable.setOperation(ClientConstants.ACTIONTYPE_DELETE);
                                            observable.setStatus();
                                            this.lblStatus.setText(observable.getLblStatus());
                                            callView("Delete");
                                            btnReject.setEnabled(false);
                                            btnAuthorize.setEnabled(false);
                                            btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
                                            private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
                                                observable.setOperation(ClientConstants.ACTIONTYPE_EDIT);
                                                observable.setStatus();
                                                this.lblStatus.setText(observable.getLblStatus());
                                                callView("Edit");
                                                btnReject.setEnabled(false);
                                                btnAuthorize.setEnabled(false);
                                                btnException.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
                                                    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
                                                       
                                                        if(checkTally()>=0) {
                                                            this.btnEndPISDActionPerformed(null);
                                                            
                                                            int operation = observable.getOperation();
                                                            
                                                            if (operation==ClientConstants.ACTIONTYPE_NEW) {
                                                                operation=observable.doAction(CommonConstants.TOSTATUS_INSERT);
                                                            } else if (operation==ClientConstants.ACTIONTYPE_EDIT) {
                                                                operation=observable.doAction(CommonConstants.TOSTATUS_UPDATE);
                                                            } else if (operation==ClientConstants.ACTIONTYPE_DELETE) {
                                                                operation=observable.doAction(CommonConstants.TOSTATUS_DELETE);
                                                            }
                                                            if(operation==1) {
                                                                HashMap lockMap = new HashMap();
                                                                ArrayList lst = new ArrayList();
                                                                lst.add("BATCHID");
                                                                lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                                                                if (observable.getProxyReturnMap()!=null) {
                                                                    if (observable.getProxyReturnMap().containsKey(CommonConstants.TRANS_ID)) {
                                                                        lockMap.put("BATCHID", observable.getProxyReturnMap().get(CommonConstants.TRANS_ID));
                                                                    }
                                                                }
                                                                if (observable.getOperation()==ClientConstants.ACTIONTYPE_EDIT) {
                                                                    lockMap.put("BATCHID", observable.getTxtBatchIdOC());
                                                                }
                                                                setEditLockMap(lockMap);
                                                                setEditLock();
                                                                
                                                                observable.resetOB();
                                                                setModified(false);
                                                                tblID.setModel(observable.getTbmID());
                                                                tblPISD.setModel(observable.getTbmPISD());
                                                                
                                                                resetUIFields();
                                                                transDetails.setTransDetails(null,null,null);
                                                                setButtonEnableDisable();
                                                            }
                                                            if (observable.getOperation()==ClientConstants.ACTIONTYPE_FAILED)
                                                                btnCancelActionPerformed(null);
                                                            observable.setResultStatus();
                                                            this.lblStatus.setText(observable.getLblStatus());
                                                        }else{
                                                            this.btnNewID.setEnabled(true);
                                                        }
    }//GEN-LAST:event_btnSaveActionPerformed
                                                    private void resetUIFields(){
                                                        this.viewType="";
                                                        this.isFilled=false;
                                                        this.isNew=false;
                                                    }
    private void btnAccNoPISDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccNoPISDActionPerformed
        callView("AccountNoPISD");
    }//GEN-LAST:event_btnAccNoPISDActionPerformed
        private void cboProdIdPISDItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboProdIdPISDItemStateChanged
            if(this.cboProdIdPISD.getSelectedIndex()>0) {
                if (!((String)cboProdIdPISD.getSelectedItem()).equals("")){
                    observable.cboProdIdPISDChanged((String)cboProdIdPISD.getSelectedItem());
                    txtAccHeadValuePISD.setText(observable.getLblAccHead());
                    lblAccountHeadDescValue.setText(observable.getLblAccHeadDesc());
                } else {
                    txtAccHeadValuePISD.setText("");
                    this.lblAccountHeadDescValue.setText("");
                    return;
                }
            } //if (observable.getCboProductType().equals("GL")) {
            //                observable.cboProdIdPISDChanged("");
            //                txtAccHeadValuePISD.setText(observable.getLblAccHead());
            //                lblAccountHeadDescValue.setText(observable.getLblAccHeadDesc());
            //        }
    }//GEN-LAST:event_cboProdIdPISDItemStateChanged
            private void cboProdIdPISDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdIdPISDActionPerformed
                    }//GEN-LAST:event_cboProdIdPISDActionPerformed
                private void btnEndPISDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEndPISDActionPerformed
                    ClientUtil.enableDisable(panMainOC,false);
                    this.disablePISDButtons();
                    observable.btnEndPISDAction();
                    cboClearingTypeID.setSelectedItem("");
                    lblValScheduleNo.setText("");
    }//GEN-LAST:event_btnEndPISDActionPerformed
                    private void tblPISDMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPISDMousePressed
                        isNew = false;
                        int index = tblPISD.getSelectedRow();
                        if(observable.rowSelectedPISDAction(index) == 1) {
                            cboProdIdPISDItemStateChanged(null);
                            String tempVT = viewType;
                            viewType = "AccountNoPISD";
                            HashMap hash = new HashMap();
                            hash.put("ACCOUNTNO",txtAccNoPISD.getText());
                            tblPISDPressedOrNot = true;
                            fillData(hash);
                            tblPISDPressedOrNot = false;
                            viewType = tempVT;
                            if( observable.getOperation()==ClientConstants.ACTIONTYPE_EDIT ||
                            observable.getOperation()==ClientConstants.ACTIONTYPE_NEW) {
                                this.setPISDButtons();
                                btnSavePISD.setEnabled(true);
                                btnDeletePISD.setEnabled(true);
                                ClientUtil.enableDisable(panInsidePISD,true);
                                txtAmountPISD.setEditable(true);
                                btnAccNoPISD.setEnabled(true);
                                txtAmountPISD.setEnabled(true);
                                txtAmountPISD.setEditable(true);
                                btnAccNoPISD.setEnabled(true);
                                btnAccountHead.setEnabled(false);
                                if (observable.getCboProductType().equals("GL")) {
                                    txtAccNoPISD.setEnabled(false);
                                    txtAccNoPISD.setEditable(false);
                                    cboProdIdPISD.setEnabled(false);
                                    btnAccNoPISD.setEnabled(false);
                                    btnAccountHead.setEnabled(true);
                                }
                            }
                            
                            else{
                                cboProdIdPISD.setEnabled(false);
                                btnAccountHead.setEnabled(false);
                                txtAccHeadValuePISD.setEditable(false);
                                txtAccHeadValuePISD.setEnabled(false);
                                txtAmountPISD.setEnabled(false);
                                txtAmountPISD.setEditable(false);
                                btnAccNoPISD.setEnabled(false);
                                
                            }
                        }
    }//GEN-LAST:event_tblPISDMousePressed
                        private void btnDeletePISDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeletePISDActionPerformed
                            observable.btnDeletePISDAction(tblPISD.getSelectedRow());
                            btnSavePISD.setEnabled(false);
                            btnDeletePISD.setEnabled(false);
                            transDetails.setTransDetails(null, null, null);
                            ClientUtil.enableDisable(panInsidePISD,false);
    }//GEN-LAST:event_btnDeletePISDActionPerformed
                            private void btnSavePISDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSavePISDActionPerformed
                                String Acno=txtAccNoPISD.getText();
                                HashMap hashmap=new HashMap();
                                hashmap.put("ACNO",Acno);
                                List lst1=ClientUtil.executeQuery("getDeathDetailsForCashAndTransfer", hashmap);
                                if(lst1!=null && lst1.size()>0){
                                    int a=ClientUtil.confirmationAlert("The Account is Death marked, Do you want to continue?");
                                    int b=0;
                                    if(a!=b)
                                        return;
                                }
                                if(checkInvalidAmount(this.txtAmountPISD.getText())) {
                                    return;
                                }
                                updateOBFields();
                                if(isNew)
                                    observable.btnSavePISDAction(-1);
                                else
                                    observable.btnSavePISDAction(this.tblPISD.getSelectedRow());
                                btnSavePISD.setEnabled(false);
                                btnDeletePISD.setEnabled(false);
                                ClientUtil.enableDisable(panInsidePISD,false);
                                transDetails.setTransDetails(null, null, null);
                                this.disablePISDButtons();
                                this.btnNewPISD.setEnabled(true);
                                btnNewPISD.setEnabled(true);
    }//GEN-LAST:event_btnSavePISDActionPerformed
                            
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        setModified(true);
        ClientUtil.enableDisable(panMainOC, true);
        setButtonEnableDisable();
        setIDButtons();
        ClientUtil.enableDisable(panPayInSlipDetOC,false);
        disablePISDButtons();
        ClientUtil.enableDisable(panComponentsID,false);
        observable.setOperation(ClientConstants.ACTIONTYPE_NEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        this.lblCreatedByValue.setText(ProxyParameters.USER_ID);
        this.btnNewPISD.setEnabled(true);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        bankButtonEnableDisable(false);
        txtInstrumentNo1ID.setEnabled(false);
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void bankButtonEnableDisable(boolean var){
        btnBankCode.setEnabled(var);
        btnBranchCode.setEnabled(var);
    }
    private void btnEndIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEndIDActionPerformed
            }//GEN-LAST:event_btnEndIDActionPerformed
        private void btnDeleteIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteIDActionPerformed
            observable.btnDeleteIDAction(this.tblID.getSelectedRow());
            btnSaveID.setEnabled(false);
            btnDeleteID.setEnabled(false);
            ClientUtil.enableDisable(panComponentsID,false);
            
    }//GEN-LAST:event_btnDeleteIDActionPerformed
            private void tblIDMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblIDMousePressed
                isNew = false;
                int index = tblID.getSelectedRow();
                if(observable.rowSelectedIDAction(index) == 1 &&
                (observable.getOperation()==ClientConstants.ACTIONTYPE_EDIT ||
                observable.getOperation()==ClientConstants.ACTIONTYPE_NEW)){
                    
                    btnSaveID.setEnabled(true);
                    btnDeleteID.setEnabled(true);
                    ClientUtil.enableDisable(panComponentsID,true);
                    dtclearingDtID.setEnabled(false);
                    txtInstrumentNo1ID.setEnabled(false);
                    if(observable.getTbmID().getRowCount()>0){
                        cboClearingTypeID.setEnabled(false);
                    }
                } else{
                    btnAccNoPISD.setEnabled(false);
                    cboProdIdPISD.setEnabled(false);
                    
                }
                
    }//GEN-LAST:event_tblIDMousePressed
            private boolean checkInvalidAmount(String amt){
                int amtValue = CommonUtil.convertObjToInt(amt);
                if(amtValue<=0) {
                    COptionPane.showMessageDialog(this,resourceBundle.getString("WARNING_INVALIDAMT"));
                    return true;
                }
                return false;
            }
    private void btnSaveIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveIDActionPerformed
        //        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panInstrDetOC); panComponentsID
        txtRemarksID.setText("");
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panComponentsID);
        if (mandatoryMessage.length() > 0){
            displayAlert(mandatoryMessage);
            return;
        }


        if(checkInvalidAmount(this.txtAmountID.getText())) {
            return;
        }
        
        if(observable.getLotSize()<= CommonUtil.convertObjToInt(lblValNoInstrBooked.getText())+tblID.getRowCount()){
            displayAlert("Lot size exceeding for this Schedule...");
            return;
        }
        //        if(tblID.getRowCount()>=1){
        //            displayAlert("cannot add more than one instrument ");
        //            btnNewIDActionPerformed(null);
        //            lblBankNameIDValue.setText("");
        //            lblBranchNameIDValue.setText("");
        //            return;
        //        }
        updateOBFields();
        if(this.isNew)
            observable.btnSaveIDAction(-1);
        else
            observable.btnSaveIDAction(this.tblID.getSelectedRow());
        
        btnSaveID.setEnabled(false);
        btnDeleteID.setEnabled(false);
        lblValScheduleNo.setText("");
        cboClearingTypeID.setSelectedItem("");
        dtclearingDtID.setDateValue(" ");
        ClientUtil.enableDisable(panComponentsID,false);
        lblBankNameIDValue.setText("");
        lblBranchNameIDValue.setText("");
        btnNewID.setEnabled(true);
        
    }//GEN-LAST:event_btnSaveIDActionPerformed
        private void btnNewPISDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewPISDActionPerformed
            setPISDButtons();
            observable.btnNewPISDAction();
            transDetails.setTransDetails(null, null, null);
            btnSavePISD.setEnabled(true);
            ClientUtil.enableDisable(panInsidePISD,true);
            txtAmountPISD.setEditable(true);
            txtAmountPISD.setEnabled(true);
            this.isNew=true;
            if(tblID.getRowCount()>1 && tblPISD.getRowCount()>=1){
                displayAlert("Cannot Add More Than One Instrument ");
                //            lblBankNameIDValue.setText("");
                //            lblBranchNameIDValue.setText("");
                btnNewPISD.setEnabled(false);
                return;
            }
            btnNewPISD.setEnabled(false);
            
    }//GEN-LAST:event_btnNewPISDActionPerformed
            private void btnNewIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewIDActionPerformed
                observable.btnNewIDAction();
                btnSaveID.setEnabled(true);
                ClientUtil.enableDisable(panComponentsID,true);
                dtclearingDtID.setEnabled(false);
                txtInstrumentNo1ID.setEnabled(false);
                if(observable.getTbmID().getRowCount()>0){
                    cboClearingTypeID.setEnabled(false);
                }
                this.isNew=true;
                bankButtonEnableDisable(true);
                if(tblID.getRowCount()>=1 && tblPISD.getRowCount()>1){
                    displayAlert("Cannot Add More Than One Instrument ");
                    lblBankNameIDValue.setText("");
                    lblBranchNameIDValue.setText("");
                    btnNewID.setEnabled(false);
                    return;
                }
                btnNewID.setEnabled(false);
    }//GEN-LAST:event_btnNewIDActionPerformed

            /**
             * Getter for property viewType.
             * @return Value of property viewType.
             */
            public java.lang.String getViewType() {
                return viewType;
            }
            
            /**
             * Setter for property viewType.
             * @param viewType New value of property viewType.
             */
            public void setViewType(java.lang.String viewType) {
                this.viewType = viewType;
            }
            
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAccNoPISD;
    private com.see.truetransact.uicomponent.CButton btnAccountHead;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnBankCode;
    private com.see.truetransact.uicomponent.CButton btnBranchCode;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDeleteID;
    private com.see.truetransact.uicomponent.CButton btnDeletePISD;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnEndID;
    private com.see.truetransact.uicomponent.CButton btnEndPISD;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnNewID;
    private com.see.truetransact.uicomponent.CButton btnNewPISD;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnRegularize;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSaveID;
    private com.see.truetransact.uicomponent.CButton btnSavePISD;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CButton btnView1;
    private com.see.truetransact.uicomponent.CComboBox cboClearingTypeID;
    private com.see.truetransact.uicomponent.CComboBox cboInstrDetailsCurrency;
    private com.see.truetransact.uicomponent.CComboBox cboInstrumentTypeID;
    private com.see.truetransact.uicomponent.CComboBox cboProdIdPISD;
    private com.see.truetransact.uicomponent.CComboBox cboProductType;
    private com.see.truetransact.uicomponent.CComboBox cboScheduleNo;
    private com.see.truetransact.uicomponent.CDateField dtclearingDtID;
    private com.see.truetransact.uicomponent.CDateField dtdInstrumentDtID;
    private com.see.truetransact.uicomponent.CLabel lblAccHeadPISD;
    private com.see.truetransact.uicomponent.CLabel lblAccHolderNameValuePISD;
    private com.see.truetransact.uicomponent.CLabel lblAccNoPISD;
    private com.see.truetransact.uicomponent.CLabel lblAccountHeadDesc;
    private com.see.truetransact.uicomponent.CLabel lblAccountHeadDescValue;
    private com.see.truetransact.uicomponent.CLabel lblAmountID;
    private com.see.truetransact.uicomponent.CLabel lblAmountPISD;
    private com.see.truetransact.uicomponent.CLabel lblBankCodeID;
    private com.see.truetransact.uicomponent.CLabel lblBankNameID;
    private com.see.truetransact.uicomponent.CLabel lblBankNameIDValue;
    private com.see.truetransact.uicomponent.CLabel lblBatchIdOC;
    private com.see.truetransact.uicomponent.CLabel lblBookedAmt;
    private com.see.truetransact.uicomponent.CLabel lblBookedInstr;
    private com.see.truetransact.uicomponent.CLabel lblBranchCodeID;
    private com.see.truetransact.uicomponent.CLabel lblBranchNameID;
    private com.see.truetransact.uicomponent.CLabel lblBranchNameIDValue;
    private com.see.truetransact.uicomponent.CLabel lblClearingDate;
    private com.see.truetransact.uicomponent.CLabel lblClearingTypeID;
    private com.see.truetransact.uicomponent.CLabel lblCreatedBy;
    private com.see.truetransact.uicomponent.CLabel lblCreatedByValue;
    private com.see.truetransact.uicomponent.CLabel lblDrawerAccNoID;
    private com.see.truetransact.uicomponent.CLabel lblInstrDetailsCurrency;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentDtID;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentNo1ID;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentNo2ID;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentTypeID;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblProdIdPISD;
    private com.see.truetransact.uicomponent.CLabel lblProductType;
    private com.see.truetransact.uicomponent.CLabel lblRemarksID;
    private com.see.truetransact.uicomponent.CLabel lblRemarksPISD;
    private com.see.truetransact.uicomponent.CLabel lblScheduleNo;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace17;
    private com.see.truetransact.uicomponent.CLabel lblSpace18;
    private com.see.truetransact.uicomponent.CLabel lblSpace19;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace20;
    private com.see.truetransact.uicomponent.CLabel lblSpace21;
    private com.see.truetransact.uicomponent.CLabel lblSpace22;
    private com.see.truetransact.uicomponent.CLabel lblSpace23;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTotalAmountID;
    private com.see.truetransact.uicomponent.CLabel lblTotalAmountPISD;
    private com.see.truetransact.uicomponent.CLabel lblTotalAmountValueID;
    private com.see.truetransact.uicomponent.CLabel lblTotalAmountValuePISD;
    private com.see.truetransact.uicomponent.CLabel lblValAmount;
    private com.see.truetransact.uicomponent.CLabel lblValNoInstrBooked;
    private com.see.truetransact.uicomponent.CLabel lblValScheduleNo;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panBatchIdOC;
    private com.see.truetransact.uicomponent.CPanel panButtonID;
    private com.see.truetransact.uicomponent.CPanel panButtonPISD;
    private com.see.truetransact.uicomponent.CPanel panComponentsID;
    private com.see.truetransact.uicomponent.CPanel panDraweeAccNo;
    private com.see.truetransact.uicomponent.CPanel panInsideDI;
    private com.see.truetransact.uicomponent.CPanel panInsidePISD;
    private com.see.truetransact.uicomponent.CPanel panInstrDetOC;
    private com.see.truetransact.uicomponent.CPanel panLableValues;
    private com.see.truetransact.uicomponent.CPanel panMainOC;
    private com.see.truetransact.uicomponent.CPanel panOutDetails;
    private com.see.truetransact.uicomponent.CPanel panPayInSlip;
    private com.see.truetransact.uicomponent.CPanel panPayInSlipDetOC;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTotalAmountID;
    private com.see.truetransact.uicomponent.CPanel panTotalAmountPISD;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private com.see.truetransact.uicomponent.CSeparator sptHorizontalUpPISD;
    private com.see.truetransact.uicomponent.CSeparator sptVerticalID;
    private com.see.truetransact.uicomponent.CSeparator sptVerticalPISD;
    private com.see.truetransact.uicomponent.CScrollPane srpTCID;
    private com.see.truetransact.uicomponent.CScrollPane srpTCPISD;
    private com.see.truetransact.uicomponent.CTable tblID;
    private com.see.truetransact.uicomponent.CTable tblPISD;
    private javax.swing.JToolBar tbrAdvances;
    private com.see.truetransact.uicomponent.CTextField txtAccHeadValuePISD;
    private com.see.truetransact.uicomponent.CTextField txtAccNoPISD;
    private com.see.truetransact.uicomponent.CTextField txtAmountID;
    private com.see.truetransact.uicomponent.CTextField txtAmountPISD;
    private com.see.truetransact.uicomponent.CTextField txtBankCodeID;
    private com.see.truetransact.uicomponent.CTextField txtBatchIdOC;
    private com.see.truetransact.uicomponent.CTextField txtBranchCodeID;
    private com.see.truetransact.uicomponent.CTextField txtDrawerAccNoID;
    private com.see.truetransact.uicomponent.CTextField txtInstrumentNo1ID;
    private com.see.truetransact.uicomponent.CTextField txtInstrumentNo2ID;
    private com.see.truetransact.uicomponent.CTextField txtRemarksID;
    private com.see.truetransact.uicomponent.CTextField txtRemarksPISD;
    // End of variables declaration//GEN-END:variables
}
