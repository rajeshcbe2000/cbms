/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareProductUI.java
 *
 * Created on November 23, 2004, 4:00 PM
 */

package com.see.truetransact.ui.product.share;

/**
 *
 * @author Ashok
 *  @modified : Sunil
 *      Added Edit Locking - 08-07-2005
 */
import java.util.ArrayList;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.product.share.ShareProductOB;
import com.see.truetransact.ui.product.share.ShareProductMRB;
import com.see.truetransact.ui.product.share.ShareProductRB;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.PercentageValidation;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.ui.customer.CheckGahanCustomerUI;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.Date;
import java.util.List;

public class ShareProductUI extends CInternalFrame implements UIMandatoryField,Observer{
    
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private HashMap mandatoryMap;
    private ShareProductOB observable;
    private ShareProductMRB objMandatoryRB;
    private ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.product.share.ShareProductRB", ProxyParameters.LANGUAGE);
    private String viewType = new String();
    final String AUTHORIZE="Authorize";
    private boolean dataExists = false;
    private int tblRowCount;
    private boolean loanTypeExists = false;
    private List itemsSubmittingList =  new ArrayList();
    private List mandatoryData = new ArrayList();
    private List mandatoryAddrData = new ArrayList();
    private int existingRowcount;
    private String VIEW_TYPE_SHARE_SUSPENSE_ACCT = "SHARE_SUSPENSE_ACCT";
    private String VIEW_TYPE_SHARE_ACCT = "SHARE_ACCT";
    private String VIEW_TYPE_MEMBERSHIP_FEE_ACCT = "MEMBERSHIP_FEE_ACCT";
    private String VIEW_TYPE_APPLICATION_FEE_ACCT = "APPLICATION_FEE_ACCT";
    private String VIEW_TYPE_SHARE_FEE_ACCT = "SHARE_FEE_ACCT";
    private String VIEW_TYPE_DIV_PAYABLE_ACCT = "DIV_PAYABLE_ACCT";
    private String VIEW_TYPE_DIV_PAID_ACCT = "DIV_PAID_ACCT";
    
    /** Creates new form ShareProductUI */
    public ShareProductUI() {
        initUIComponents();
    }
    
    /** Initialsises the UIComponents */
    private void initUIComponents(){
        initComponents();
        setFieldNames();
        setMaxLength();
        internationalize();
        setMandatoryHashMap();
        setHelpMessage();
        setObservable();
        observable.resetForm();
        initComponentData();
        ClientUtil.enableDisable(panShareProduct, false);
        setButtonEnableDisable();
        tabShareProduct.resetVisits();
//        panDivident Contains the previous Design, Replaced by panDividentNew as per CBMS requirement
        panDivident.setVisible(false);
        panDividentNew.setVisible(true);
        panDivPayableAcHd.setVisible(false);
        lblSharedivPayableAcct.setVisible(false);
        itemsSubmittingList.clear();
        disDesc();
    }
    public void disDesc()
    {
        txtShareSuspAccountDesc.setEnabled(false);
        txtShareAccountDesc.setEnabled(false);
        txtMemFeeAcctDesc.setEnabled(false);
        txtApplFeeAcctDesc.setEnabled(false);
        txtShareFeeAcctDesc.setEnabled(false);
        txtDivPaidAcctDesc.setEnabled(false);
        txtDivPayableAcctDesc.setEnabled(false);
        txtUnClaimedDivTransferAcHdDesc.setEnabled(false);
        txtIncomeAccountHeadDesc.setEnabled(false);
    }
    
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        btnApplFeeAcct.setName("btnApplFeeAcct");
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnMemFeeAcct.setName("btnMemFeeAcct");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        btnShareAccount.setName("btnShareAccount");
        btnShareFeeAcct.setName("btnShareFeeAcct");
        btnShareSuspAccount.setName("btnShareSuspAccount");
        btnTabDelete.setName("btnTabDelete");
        btnTabNew.setName("btnTabNew");
        btnTabSave.setName("btnTabSave");
        cboAdditionalShareRefund.setName("cboAdditionalShareRefund");
        cboDivAppFrequency.setName("cboDivAppFrequency");
        cboDivCalcFrequency.setName("cboDivCalcFrequency");
        cboLoanType.setName("cboLoanType");
        cboNomineePeriod.setName("cboNomineePeriod");
        cboRefundPeriod.setName("cboRefundPeriod");
        cboShareType.setName("cboShareType");
        cboUnclaimedDivPeriod.setName("cboUnclaimedDivPeriod");
        chkReqActHolder.setName("chkReqActHolder");
        lblAdditionalShareRefund.setName("lblAdditionalShareRefund");
        lblAdmissionFee.setName("lblAdmissionFeeFixed");
        lblApplFeeAcct.setName("lblApplFeeAcct");
        lblApplicationFee.setName("lblApplicationFee");
        lblCalculatedDate.setName("lblCalculatedDate");
        lblDivAppFrequency.setName("lblDivAppFrequency");
        lblDivCalcFrequency.setName("lblDivCalcFrequency");
        lblDividentPercentage.setName("lblDividentPercentage");
        //        lblDueDate.setName("lblDueDate");
        lblFaceValue.setName("lblFaceValue");
        lblConsiderSalaryRecovery.setName("lblConsiderSalaryRecovery");
        lblAuthorizedCapital.setName("lblAuthorizedCapital");
//        lblLoanAvailingLimit.setName("lblLoanAvailingLimit");
        lblLoanType.setName("lblLoanType");
//        lblMaxLoanLimit.setName("lblMaxLoanLimit");
        lblMaxShareHolding.setName("lblMaxShareHolding");
        lblMemFeeAcct.setName("lblMemFeeAcct");
        lblMsg.setName("lblMsg");
        lblNomineePeriod.setName("lblNomineePeriod");
        lblNommineeAllowed.setName("lblNommineeAllowed");
        lblPaidCapital.setName("lblPaidCapital");
        lblRefundPeriod.setName("lblRefundPeriod");
        lblRefundinaYear.setName("lblRefundinaYear");
        lblReqActHolder.setName("lblReqActHolder");
        lblShareAccount.setName("lblShareAccount");
        lblShareFee.setName("lblShareFee");
        lblShareFeeAcct.setName("lblShareFeeAcct");
        lblShareSuspAccount.setName("lblShareSuspAccount");
        lblShareType.setName("lblShareType");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblStatus.setName("lblStatus");
        lblSubscribedCapital.setName("lblSubscribedCapital");
//        lblSurityLimit.setName("lblSurityLimit");
        lblUnClaimedDivPeriod.setName("lblUnClaimedDivPeriod");
        mbrShareProduct.setName("mbrShareProduct");
        panAccountDetails.setName("panAccountDetails");
        panAcctDet.setName("panAcctDet");
        panAdditonalShareRefund.setName("panAdditonalShareRefund");
        panApplFeeAcct.setName("panApplFeeAcct");
        panBorrowing.setName("panBorrowing");
        panDivident.setName("panDivident");
        panLoan.setName("panLoan");
        panLoanOperations.setName("panLoanOperations");
        panLoanType.setName("panLoanType");
        panMemFeeAcct.setName("panMemFeeAcct");
        panNominee.setName("panNominee");
        panRefundPeriod.setName("panRefundPeriod");
        panShare.setName("panShare");
        panShareAccount.setName("panShareAccount");
        panShareFeeAcct.setName("panShareFeeAcct");
        panShareProduct.setName("panShareProduct");
        panShareSuspAccount.setName("panShareSuspAccount");
        panStatus.setName("panStatus");
        panTblLoanType.setName("panTblLoanType");
        panUInClaimedDivident.setName("panUInClaimedDivident");
        srpLoanType.setName("srpLoanType");
        tabShareProduct.setName("tabShareProduct");
        tblLoanType.setName("tblLoanType");
        tdtCalculatedDate.setName("tdtCalculatedDate");
        //        tdtDueDate.setName("tdtDueDate");
        txtAdditionalShareRefund.setName("txtAdditionalShareRefund");
        txtAdmissionFee.setName("txtAdmissionFee");
        txtApplFeeAcct.setName("txtApplFeeAcct");
        txtApplicationFee.setName("txtApplicationFee");
        txtDividentPercentage.setName("txtDividentPercentage");
        txtFaceValue.setName("txtFaceValue");
        txtIssuedCapital.setName("txtIssuedCapital");
        txtLoanAvailingLimit.setName("txtLoanAvailingLimit");
        txtMaxLoanLimit.setName("txtMaxLoanLimit");
        txtMaxShareHolding.setName("txtMaxShareHolding");
        txtMemFeeAcct.setName("txtMemFeeAcct");
        txtNomineePeriod.setName("txtNomineePeriod");
        txtNomineeaAllowed.setName("txtNomineeaAllowed");
        txtPaidupCapital.setName("txtPaidupCapital");
        txtRefundPeriod.setName("txtRefundPeriod");
        txtRefundinaYear.setName("txtRefundinaYear");
        txtShareAccount.setName("txtShareAccount");
        txtShareFee.setName("txtShareFee");
        txtShareFeeAcct.setName("txtShareFeeAcct");
        txtShareSuspAccount.setName("txtShareSuspAccount");
        txtSubscribedCapital.setName("txtSubscribedCapital");
//        txtSurityLimit.setName("txtSurityLimit");
        txtUnclaimedDivPeriod.setName("txtUnclaimedDivPeriod");
        lblMinIntialShares.setName("lblMinIntialShares");
        txtMinIntialshares.setName("txtMinIntialshares");
        txtMaxLoanAmt.setName("txtMaxLoanAmt");
    }
    
    
    
    
    /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        lblFaceValue.setText(resourceBundle.getString("lblFaceValue"));
        lblConsiderSalaryRecovery.setText(resourceBundle.getString("lblConsiderSalaryRecovery"));
        lblDivAppFrequency.setText(resourceBundle.getString("lblDivAppFrequency"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblMaxShareHolding.setText(resourceBundle.getString("lblMaxShareHolding"));
        lblApplicationFee.setText(resourceBundle.getString("lblApplicationFee"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblRefundinaYear.setText(resourceBundle.getString("lblRefundinaYear"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblShareType.setText(resourceBundle.getString("lblShareType"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblSubscribedCapital.setText(resourceBundle.getString("lblSubscribedCapital"));
        lblPaidCapital.setText(resourceBundle.getString("lblPaidCapital"));
        lblUnClaimedDivPeriod.setText(resourceBundle.getString("lblUnClaimedDivPeriod"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblShareFee.setText(resourceBundle.getString("lblShareFee"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblAdmissionFee.setText(resourceBundle.getString("lblAdmissionFeeFixed"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblDivCalcFrequency.setText(resourceBundle.getString("lblDivCalcFrequency"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        chkReqActHolder.setText(resourceBundle.getString("chkReqActHolder"));
        lblAuthorizedCapital.setText(resourceBundle.getString("lblAuthorizedCapital"));
        lblReqActHolder.setText(resourceBundle.getString("lblReqActHolder"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        ((javax.swing.border.TitledBorder)panDivident.getBorder()).setTitle(resourceBundle.getString("panDivident"));
        lblLoanType.setText(resourceBundle.getString("lblLoanType"));
//        lblLoanAvailingLimit.setText(resourceBundle.getString("lblLoanAvailingLimit"));
        btnTabNew.setText(resourceBundle.getString("btnTabNew"));
        btnTabSave.setText(resourceBundle.getString("btnTabSave"));
        btnTabDelete.setText(resourceBundle.getString("btnTabDelete"));
        lblMinIntialShares.setText(resourceBundle.getString("lblMinIntialShares"));
        
    }
    
/* Auto Generated Method - setMandatoryHashMap()
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboAdditionalShareRefund", new Boolean(false));
        mandatoryMap.put("cboUnclaimedDivPeriod", new Boolean(false));
        mandatoryMap.put("cboShareType", new Boolean(true));
        mandatoryMap.put("cboDivCalcFrequency", new Boolean(false));
        mandatoryMap.put("txtMaxShareHolding", new Boolean(true));
        mandatoryMap.put("cboNomineePeriod", new Boolean(false));
        mandatoryMap.put("txtAdmissionFee", new Boolean(true));
        mandatoryMap.put("tdtDueDate", new Boolean(false));
//        mandatoryMap.put("txtSurityLimit", new Boolean(false));
        mandatoryMap.put("txtAdditionalShareRefund", new Boolean(false));
        mandatoryMap.put("txtDividentPercentage", new Boolean(false));
        mandatoryMap.put("txtNomineeaAllowed", new Boolean(true));
        mandatoryMap.put("cboRefundPeriod", new Boolean(false));
        mandatoryMap.put("txtMaxLoanLimit", new Boolean(false));
        mandatoryMap.put("txtNomineePeriod", new Boolean(false));
        mandatoryMap.put("cboLoanType", new Boolean(false));
        mandatoryMap.put("tdtCalculatedDate", new Boolean(false));
        mandatoryMap.put("cboDivAppFrequency", new Boolean(false));
        mandatoryMap.put("txtApplicationFee", new Boolean(true));
        mandatoryMap.put("txtShareFee", new Boolean(true));
        mandatoryMap.put("txtSubscribedCapital", new Boolean(false));
        mandatoryMap.put("txtRefundPeriod", new Boolean(false));
        mandatoryMap.put("txtFaceValue", new Boolean(true));
        mandatoryMap.put("txtShareSuspAccount", new Boolean(true));
        mandatoryMap.put("txtLoanAvailingLimit", new Boolean(false));
        mandatoryMap.put("txtPaidupCapital", new Boolean(false));
        mandatoryMap.put("chkReqActHolder", new Boolean(false));
        mandatoryMap.put("txtSecuredAdvances", new Boolean(false));
        mandatoryMap.put("txtRefundinaYear", new Boolean(false));
        mandatoryMap.put("txtUnclaimedDivPeriod", new Boolean(false));
        mandatoryMap.put("txtUnsecuredAdvances", new Boolean(false));
        mandatoryMap.put("txtIssuedCapital", new Boolean(true));
        mandatoryMap.put("txtShareAccount", new Boolean(true));
        mandatoryMap.put("txtMemFeeAcct", new Boolean(true));
        mandatoryMap.put("txtApplFeeAcct", new Boolean(true));
        mandatoryMap.put("txtShareFeeAcct", new Boolean(true));
        mandatoryMap.put("txtMaxLoanAmt", new Boolean(false));
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    
    /* Creates the insstance of ShareProduct which acts as  Observable to
     *ShareProduct UI */
    private void setObservable() {
        try{
            observable = ShareProductOB.getInstance();
            observable.addObserver(this);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /* Sets the model for the comboboxes in the UI    */
    private void initComponentData() {
        try{
            cboDivAppFrequency.setModel(observable.getCbmDivAppFrequency());
            cboDivCalcFrequency.setModel(observable.getCbmDivCalcFrequency());
            cboShareType.setModel(observable.getCbmShareType());
            cboUnclaimedDivPeriod.setModel(observable.getCbmUnclaimedDivPeriod());
            cboNomineePeriod.setModel(observable.getCbmNomineePeriod());
            cboRefundPeriod.setModel(observable.getCbmRefundPeriod());
            cboAdditionalShareRefund.setModel(observable.getCbmAdditionalShareRefund());
            cboLoanType.setModel(observable.getCbmLoanType());
            tblLoanType.setModel(observable.getTbmLoanType());
//            changed by nikhil
            cboDivCalcFrequencyNew.setModel(observable.getCbmDivCalcFrequencyNew());
            cboDivCalcType.setModel(observable.getCbmDivCalcType());
            cboDividendRounding.setModel(observable.getCbmDividendRounding());
            cboProdTypeCr.setModel(observable.getCbmPensionProducts());
        }catch(ClassCastException e){
            parseException.logException(e,true);
        }
    }
    
/* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        objMandatoryRB = new ShareProductMRB();
        txtAdmissionFee.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAdmissionFee"));
        txtApplicationFee.setHelpMessage(lblMsg, objMandatoryRB.getString("txtApplicationFee"));
        txtFaceValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFaceValue"));
        txtRefundinaYear.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRefundinaYear"));
        txtMaxShareHolding.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMaxShareHolding"));
        txtSubscribedCapital.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSubscribedCapital"));
        txtPaidupCapital.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPaidupCapital"));
        txtShareFee.setHelpMessage(lblMsg, objMandatoryRB.getString("txtShareFee"));
        txtIssuedCapital.setHelpMessage(lblMsg, objMandatoryRB.getString("txtIssuedCapital"));
        chkReqActHolder.setHelpMessage(lblMsg, objMandatoryRB.getString("chkReqActHolder"));
        cboDivCalcFrequency.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDivCalcFrequency"));
        cboDivAppFrequency.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDivAppFrequency"));
        cboUnclaimedDivPeriod.setHelpMessage(lblMsg, objMandatoryRB.getString("cboUnclaimedDivPeriod"));
        txtUnclaimedDivPeriod.setHelpMessage(lblMsg, objMandatoryRB.getString("txtUnclaimedDivPeriod"));
        cboShareType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboShareType"));
        txtNomineeaAllowed.setHelpMessage(lblMsg, objMandatoryRB.getString("cboShareType"));
        txtNomineePeriod.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNomineeaAllowed"));
        cboNomineePeriod.setHelpMessage(lblMsg, objMandatoryRB.getString("cboNomineePeriod"));
        txtRefundinaYear.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRefundinaYear"));
        txtRefundPeriod.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRefundPeriod"));
        cboRefundPeriod.setHelpMessage(lblMsg, objMandatoryRB.getString("cboRefundPeriod"));
        txtAdditionalShareRefund.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAdditionalShareRefund"));
        cboAdditionalShareRefund.setHelpMessage(lblMsg, objMandatoryRB.getString("cboAdditionalShareRefund"));
        txtDividentPercentage.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDividentPercentage"));
        tdtCalculatedDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtCalculatedDate"));
        //        tdtDueDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDueDate"));
        txtMaxLoanLimit.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMaxLoanLimit"));
//        txtSurityLimit.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSurityLimit"));
        txtShareSuspAccount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtShareSuspAccount"));
        cboLoanType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboLoanType"));
        txtLoanAvailingLimit.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLoanAvailingLimit"));
        txtShareAccount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtShareAccount"));
        txtMemFeeAcct.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMemFeeAcct"));
        txtApplFeeAcct.setHelpMessage(lblMsg, objMandatoryRB.getString("txtApplFeeAcct"));
        txtShareFeeAcct.setHelpMessage(lblMsg, objMandatoryRB.getString("txtShareFeeAcct"));
        txtMaxLoanAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMaxLoanAmt"));
    }
    public String getAccHdDesc(String accHdId)
    {
        HashMap map1= new HashMap();
        map1.put("ACCHD_ID",accHdId);
        List list1= ClientUtil.executeQuery("getSelectAcchdDesc", map1);
        if(!list1.isEmpty())
        {
            HashMap map2= new HashMap();
            map2= (HashMap)list1.get(0);
            String accHeadDesc= map2.get("AC_HD_DESC").toString();
            return accHeadDesc;
        }
        else
        {
            return "";
        }
    }
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        System.out.println("getShareCertificte^^##^+1111"+observable.getShareCertificte());
        String manData = observable.Data;
        String manAddrData = observable.addrData;
        System.out.println("manDatamanData"+manData);
        ArrayList dataList=new ArrayList();
        if(manData!=null){
        String[] dataArray = manData.split(",");
        for(int i=0;i<dataArray.length;i++){
         dataList.add(dataArray[i]);
        }
        }
        if (dataList.contains("CASTE")) {
            chkCaste.setSelected(true);
        }
         if (dataList.contains("AMSAM")) {
            chkAmsam.setSelected(true);
        }
         if (dataList.contains("DESAM")) {
            chkDesam.setSelected(true);
        }
         if (dataList.contains("WARD_NO")) {
            chkWardNo.setSelected(true);
        }
          if (dataList.contains("DOB")) {
            chkDob.setSelected(true);
        }
           if (dataList.contains("RELIGION")) {
            chkReligion.setSelected(true);
        }
         if(manAddrData!=null){
        String[] dataAddrArray = manAddrData.split(",");
        for(int i=0;i<dataAddrArray.length;i++){
          dataList.add(dataAddrArray[i]);
        }
        }
                
        if (dataList.contains("STREET")) {
            chkStreet.setSelected(true);
        }
        if (dataList.contains("TALUK")) {
            chkTaluk.setSelected(true);
        }
        
        if (dataList.contains("CITY")) {
            chkCity.setSelected(true);
        }
        if (dataList.contains("AREA")) {
            chkArea.setSelected(true);
        }
       
        if (dataList.contains("VILLAGE")) {
            chkVillage.setSelected(true);
        }
       
        
         if (dataList.contains("PIN_CODE")) {
            chkPinCode.setSelected(true);
        }
       
     
        removeRadioButtons();
        String s = observable.getTxtDivPaidAcct();
        cboShareType.setSelectedItem(observable.getCboShareType());
        txtFaceValue.setText(observable.getTxtFaceValue());
        txtIssuedCapital.setText(observable.getTxtIssuedCapital());
        txtSubscribedCapital.setText(observable.getTxtSubscribedCapital());
        txtPaidupCapital.setText(observable.getTxtPaidupCapital());
        txtMaxShareHolding.setText(observable.getTxtMaxShareHolding());
        if (observable.getRdoAdmissionFeeType().equals("FIXED")) {
            rdoAdmissionFeeType_Fixed.setSelected(true);
            txtAdmissionFeeMin.setEnabled(false);
            txtAdmissionFeeMax.setEnabled(false);
            txtAdmissionFee.setValidation(new CurrencyValidation(14, 2));
            lblAdmissionFee.setText(resourceBundle.getString("lblAdmissionFeeFixed"));
        } else if (CommonUtil.convertObjToStr(observable.getRdoAdmissionFeeType()).equals("PERCENT")) {
            rdoAdmissionFeeType_Percent.setSelected(true);
            txtAdmissionFeeMin.setEnabled(true);
            txtAdmissionFeeMax.setEnabled(true);
            txtAdmissionFee.setValidation(new NumericValidation(2,2));
            lblAdmissionFee.setText(resourceBundle.getString("lblAdmissionFeePercent"));
        }
        txtAdmissionFee.setText(observable.getTxtAdmissionFee());
        txtAdmissionFeeMin.setText(observable.getTxtAdmissionFeeMin());
        txtAdmissionFeeMax.setText(observable.getTxtAdmissionFeeMax());
        txtApplicationFee.setText(observable.getTxtApplicationFee());
        txtShareFee.setText(observable.getTxtShareFee());
        chkReqActHolder.setSelected(observable.getChkReqActHolder());
        
        System.out.println("observable.getChkDrfAllowed() :: " + observable.getChkDrfAllowed());
        if(observable.getChkDrfAllowed().equals("Y")){
            chkDRF.setSelected(true);
        }else{
            chkDRF.setSelected(false); 
        }
        
        String  considerRecovery = CommonUtil.convertObjToStr(observable.getChkConsiderSalaryRecovery());
        if(considerRecovery.length()>0 && considerRecovery.equals("Y"))
            chkConsiderSalaryRecovery.setSelected(true);
        else
            chkConsiderSalaryRecovery.setSelected(false);
        if(observable.getChkOutstandingRequired().equals("Y"))
            chkOutstandingRequired.setSelected(true);
        else
            chkOutstandingRequired.setSelected(false);
//        added by Nikhil for Share Subsidy
        if(CommonUtil.convertObjToStr(observable.getChkSubsidyForSCST()).equals("Y")){
            chkSubsidyForSCST.setSelected(true);
            visibleSubsidyShare(true);
        }else if(CommonUtil.convertObjToStr(observable.getChkSubsidyForSCST()).equals("N")){
            chkSubsidyForSCST.setSelected(false);
            visibleSubsidyShare(false);
        }
        txtCustomerShare.setText(observable.getTxtCustomerShare());
        txtGovernmentShare.setText(observable.getTxtGovernmentShare());
        txtGovtSubsidyAccountHead.setText(observable.getTxtGovtSubsidyAccountHead());
         if(!txtGovtSubsidyAccountHead.getText().equals(""))
        {
            btnGovtSubsidyAccountHead.setToolTipText(getAccHdDesc(observable.getTxtGovtSubsidyAccountHead()));
        }
        txtNomineeaAllowed.setText(observable.getTxtNomineeaAllowed());
        cboNomineePeriod.setSelectedItem(observable.getCboNomineePeriod());
        txtNomineePeriod.setText(observable.getTxtNomineePeriod());
        txtShareSuspAccount.setText(observable.getTxtShareSuspAccount());
        if(!txtShareSuspAccount.getText().equals(""))
        {
            txtShareSuspAccountDesc.setText(getAccHdDesc(observable.getTxtShareSuspAccount()));
            txtShareSuspAccountDesc.setEnabled(false);
        }
        txtShareAccount.setText(observable.getTxtShareAccount());
         if(!txtShareAccount.getText().equals(""))
        {
            txtShareAccountDesc.setText(getAccHdDesc(observable.getTxtShareAccount()));
            txtShareAccountDesc.setEnabled(false);
        }
        txtMemFeeAcct.setText(observable.getTxtMemFeeAcct());
         if(!txtMemFeeAcct.getText().equals(""))
        {
            txtMemFeeAcctDesc.setText(getAccHdDesc(observable.getTxtMemFeeAcct()));
            txtMemFeeAcctDesc.setEnabled(false);
        }
        txtApplFeeAcct.setText(observable.getTxtApplFeeAcct());
         if(!txtApplFeeAcct.getText().equals(""))
        {
            txtApplFeeAcctDesc.setText(getAccHdDesc(observable.getTxtApplFeeAcct()));
            txtApplFeeAcctDesc.setEnabled(false);
        }
        txtShareFeeAcct.setText(observable.getTxtShareFeeAcct());
        if(!txtShareFeeAcct.getText().equals(""))
        {
            txtShareFeeAcctDesc.setText(getAccHdDesc(observable.getTxtShareFeeAcct()));
            txtShareFeeAcctDesc.setEnabled(false);
        }
        txtRefundinaYear.setText(observable.getTxtRefundinaYear());
        txtRefundPeriod.setText(observable.getTxtRefundPeriod());
        cboRefundPeriod.setSelectedItem(observable.getCboRefundPeriod());
        cboAdditionalShareRefund.setSelectedItem(observable.getCboAdditionalShareRefund());
        txtAdditionalShareRefund.setText(observable.getTxtAdditionalShareRefund());
//        txtSurityLimit.setText(observable.getTxtSurityLimit());
        txtMaxLoanLimit.setText(observable.getTxtMaxLoanLimit());
        tdtCalculatedDate.setDateValue(observable.getTdtCalculatedDate());
        //        tdtDueDate.setDateValue(observable.getTdtDueDate());
        txtDividentPercentage.setText(observable.getTxtDividentPercentage());
        cboDivCalcFrequency.setSelectedItem(observable.getCboDivCalcFrequency());
        
//        added by nikhil
        cboDivCalcFrequencyNew.setSelectedItem(observable.getCboDivCalcFrequencyNew());
        txtMinDividendAmount.setText(observable.getTxtMinDividendAmount());
        cboDivCalcType.setSelectedItem(observable.getCboDivCalcType());
        cboDividendRounding.setSelectedItem(observable.getCboDividendRounding());
        rdoRatificationYes.setSelected(observable.getRdoRatificationYes());
        rdoRatificationNo.setSelected(observable.getRdoRatificationNo());
        txtMinIntialshares.setText(observable.getTxtMinIntialshares());
        cboDivAppFrequency.setSelectedItem(observable.getCboDivAppFrequency());
        cboUnclaimedDivPeriod.setSelectedItem(observable.getCboUnclaimedDivPeriod());
        txtUnclaimedDivPeriod.setText(observable.getTxtUnclaimedDivPeriod());
         txtDivPaidAcct.setText(observable.getTxtDivPaidAcct());
         rdoFullClosureYes.setSelected(observable.getRdoFullClosureYes());
         rdoFullClosureNo.setSelected(observable.getRdoFullClosureNo());
         if(!txtDivPaidAcct.getText().equals(""))
        {
            txtDivPaidAcctDesc.setText(getAccHdDesc(observable.getTxtDivPaidAcct()));
            txtDivPaidAcctDesc.setEnabled(false);
        }
        txtDivPayableAcct.setText(observable.getTxtDivPayableAcct());
         if(!txtDivPayableAcct.getText().equals(""))
        {
            txtDivPayableAcctDesc.setText(getAccHdDesc(observable.getTxtDivPayableAcct()));
            txtDivPayableAcctDesc.setEnabled(false);
        }
        txtNumPatternFollowedPrefix.setText(observable.getNumPatternPrefix());
        txtNumPatternFollowedSuffix.setText(observable.getNumPatternSuffix());
        tdtDivRunDate.setDateValue(CommonUtil.convertObjToStr(observable.getLstRunDate()));
        tdtLastuncaimedRunDate.setDateValue(CommonUtil.convertObjToStr(observable.getUnclaimedRunDate()));
        tdtLastuncaimedUpTo.setDateValue(CommonUtil.convertObjToStr(observable.getUnclaimedTransferDate()));
        txtUnClaimedDivTransferAcHd.setText(CommonUtil.convertObjToStr(observable.getUnClaimedDivTransferAcHd()));
        if(!txtUnClaimedDivTransferAcHd.getText().equals(""))
        {
            txtUnClaimedDivTransferAcHdDesc.setText(getAccHdDesc(observable.getUnClaimedDivTransferAcHd()));
            txtUnClaimedDivTransferAcHdDesc.setEnabled(false);
        }
       txtIncomeAccountHead.setText(CommonUtil.convertObjToStr(observable.getTxtIncomeAccountHead()));
       if(!txtIncomeAccountHead.getText().equals(""))
        {
            txtIncomeAccountHeadDesc.setText(getAccHdDesc(observable.getTxtIncomeAccountHead()));
            txtIncomeAccountHeadDesc.setEnabled(false);
        }
        txtPensionAge.setText(CommonUtil.convertObjToStr(observable.getPensionAge()));
        txtShareDuration.setText(CommonUtil.convertObjToStr(observable.getSharePeriod()));
        txtMinPension.setText(CommonUtil.convertObjToStr(observable.getMinPension()));
        cboProdTypeCr.setSelectedItem(observable.getCbmPensionProducts().getDataForKey(observable.getPensionProductType()));
        if(observable.getPensionProductType()!=null && !observable.getPensionProductType().equals("GL")){
            txtTransProductId.setText(observable.getPensionProductId());    
        }
        if(observable.getShareCertificte().equals("Y")){
            chkShareCerti.setSelected(true);
        }else{
            chkShareCerti.setSelected(false);
        }
        txtAcctNo.setText(observable.getPensionAcNo());
        cboLoanType.setSelectedItem(observable.getCboLoanType());
        txtLoanAvailingLimit.setText(observable.getTxtLoanAvailingLimit());
        tblLoanType.setModel(observable.getTbmLoanType());
        lblStatus.setText(observable.getLblStatus());
        txtMaxLoanAmt.setText(observable.getTxtMaxLoanAmt());
        System.out.println("getShareCertificte^^##^+"+observable.getShareCertificte());
        
        addRadioButtons();
    }
    
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setItemsSubmittingList(mandatoryData);
        observable.setItemsSubmittingAddrList(mandatoryAddrData);
        observable.setCboShareType((String) cboShareType.getSelectedItem());
        observable.setTxtFaceValue(txtFaceValue.getText());
        observable.setTxtIssuedCapital(txtIssuedCapital.getText());
        observable.setTxtSubscribedCapital(txtSubscribedCapital.getText());
        observable.setTxtPaidupCapital(txtPaidupCapital.getText());
        observable.setTxtMaxShareHolding(txtMaxShareHolding.getText());
        observable.setTxtAdmissionFee(txtAdmissionFee.getText());
        observable.setTxtAdmissionFeeMax(txtAdmissionFeeMax.getText());
        observable.setTxtAdmissionFeeMin(txtAdmissionFeeMin.getText());
        if(rdoAdmissionFeeType_Fixed.isSelected()){
            observable.setRdoAdmissionFeeType("FIXED");
        }else if(rdoAdmissionFeeType_Percent.isSelected()){
            observable.setRdoAdmissionFeeType("PERCENT");
        }
        if(rdoRatificationYes.isSelected()==true){
            observable.setRdoRatificationYes(true);
            observable.setRdoRatificationNo(false);
        }
        else if(rdoRatificationNo.isSelected()==true) {
            observable.setRdoRatificationYes(false);
            observable.setRdoRatificationNo(true);
        }
        if(rdoFullClosureYes.isSelected()==true){
            observable.setRdoFullClosureYes(true);
            observable.setRdoFullClosureNo(false);
        }else if(rdoFullClosureNo.isSelected()==true){
            observable.setRdoFullClosureYes(false);
            observable.setRdoFullClosureNo(true);
        }
        if (chkOutstandingRequired.isSelected()) {
            observable.setChkOutstandingRequired("Y");
            System.out.println("getChkOutstandingRequired() :"+observable.getChkOutstandingRequired());
        } else {
            observable.setChkOutstandingRequired("N");
            System.out.println("getChkOutstandingRequired() :"+observable.getChkOutstandingRequired());
        }
        observable.setTxtApplicationFee(txtApplicationFee.getText());
        observable.setTxtShareFee(txtShareFee.getText());
        observable.setChkReqActHolder(chkReqActHolder.isSelected());
        if(chkConsiderSalaryRecovery.isSelected()==true)
            observable.setChkConsiderSalaryRecovery(CommonUtil.convertObjToStr("Y"));
        else
            observable.setChkConsiderSalaryRecovery(CommonUtil.convertObjToStr("N"));
//         if(chkOutstandingRequired.isSelected()==true)
//            observable.setChkOutstandingRequired(CommonUtil.convertObjToStr("Y"));
//        else
//            observable.setChkOutstandingRequired(CommonUtil.convertObjToStr("N"));
//        added by nikhil for SHare Subsidy
        if(chkSubsidyForSCST.isSelected()==true){
            observable.setChkSubsidyForSCST(CommonUtil.convertObjToStr("Y"));
        }
        else{
            observable.setChkSubsidyForSCST(CommonUtil.convertObjToStr("N"));
        }
        observable.setTxtCustomerShare(txtCustomerShare.getText());
        observable.setTxtGovernmentShare(txtGovernmentShare.getText());
        observable.setTxtGovtSubsidyAccountHead(txtGovtSubsidyAccountHead.getText());
        
        
        observable.setTxtNomineeaAllowed(txtNomineeaAllowed.getText());
        observable.setCboNomineePeriod((String) cboNomineePeriod.getSelectedItem());
        observable.setTxtNomineePeriod(txtNomineePeriod.getText());
        observable.setTxtShareSuspAccount(txtShareSuspAccount.getText());
        observable.setTxtShareAccount(txtShareAccount.getText());
        observable.setTxtMemFeeAcct(txtMemFeeAcct.getText());
        observable.setTxtApplFeeAcct(txtApplFeeAcct.getText());
        observable.setTxtShareFeeAcct(txtShareFeeAcct.getText());
        observable.setTxtRefundinaYear(txtRefundinaYear.getText());
        observable.setTxtRefundPeriod(txtRefundPeriod.getText());
        observable.setCboRefundPeriod((String) cboRefundPeriod.getSelectedItem());
        observable.setCboAdditionalShareRefund((String) cboAdditionalShareRefund.getSelectedItem());
        observable.setTxtAdditionalShareRefund(txtAdditionalShareRefund.getText());
        //        observable.setTxtSurityLimit(txtSurityLimit.getText());
        observable.setTxtMaxLoanLimit(txtMaxLoanLimit.getText());
        observable.setTdtCalculatedDate(tdtCalculatedDate.getDateValue());
        //        observable.setTdtDueDate(tdtDueDate.getDateValue());
        observable.setTxtDividentPercentage(txtDividentPercentage.getText());
        observable.setCboDivCalcFrequency((String) cboDivCalcFrequency.getSelectedItem());
        
        //        added by nikhil
        observable.setCboDivCalcFrequencyNew((String) cboDivCalcFrequencyNew.getSelectedItem());
        observable.setCboDividendRounding((String) cboDividendRounding.getSelectedItem());
        observable.setCboDivCalcType((String) cboDivCalcType.getSelectedItem());
        observable.setTxtMinDividendAmount(txtMinDividendAmount.getText());
        observable.setTxtMinIntialShares(txtMinIntialshares.getText());
        observable.setCboDivAppFrequency((String) cboDivAppFrequency.getSelectedItem());
        observable.setCboUnclaimedDivPeriod((String) cboUnclaimedDivPeriod.getSelectedItem());
        observable.setTxtUnclaimedDivPeriod(txtUnclaimedDivPeriod.getText());
        observable.setCboLoanType(CommonUtil.convertObjToStr(cboLoanType.getSelectedItem()));
        observable.setTxtLoanAvailingLimit(txtLoanAvailingLimit.getText());
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setTxtDivPaidAcct(CommonUtil.convertObjToStr(txtDivPaidAcct.getText()));
        observable.setTxtDivPayableAcct(CommonUtil.convertObjToStr(txtDivPayableAcct.getText()));
        observable.setLstRunDate(DateUtil.getDateMMDDYYYY(tdtDivRunDate.getDateValue()));
        observable.setUnclaimedRunDate(DateUtil.getDateMMDDYYYY(tdtLastuncaimedRunDate.getDateValue()));
        observable.setUnclaimedTransferDate(DateUtil.getDateMMDDYYYY(tdtLastuncaimedUpTo.getDateValue()));
        observable.setUnClaimedDivTransferAcHd(txtUnClaimedDivTransferAcHd.getText());
        observable.setTxtIncomeAccountHead(txtIncomeAccountHead.getText());
        observable.setNumPatternPrefix(txtNumPatternFollowedPrefix.getText());
        observable. setNumPatternSuffix(txtNumPatternFollowedSuffix.getText());
        observable.setTxtMaxLoanAmt(txtMaxLoanAmt.getText());
        observable.setPensionAge(CommonUtil.convertObjToDouble(txtPensionAge.getText()));
        observable.setSharePeriod(CommonUtil.convertObjToDouble(txtShareDuration.getText()));
        observable.setMinPension(CommonUtil.convertObjToDouble(txtMinPension.getText()));
        observable.setPensionProductType(CommonUtil.convertObjToStr(((ComboBoxModel) cboProdTypeCr.getModel()).getKeyForSelected()));
        if(!observable.getPensionProductType().equals("GL")){
            observable.setPensionProductId(CommonUtil.convertObjToStr(txtTransProductId.getText()));
        }
        observable.setPensionAcNo(CommonUtil.convertObjToStr(txtAcctNo.getText()));
        if (chkShareCerti.isSelected()) {
            observable.setShareCertificte("Y");
        } else {
            observable.setShareCertificte("N");
        }
        
        if(chkDRF.isSelected()){
            observable.setChkDrfAllowed("Y");
        }else{
           observable.setChkDrfAllowed("N"); 
        }
        
    }
    
    /** Enabling and Disabling of Buttons after Save,Edit,Delete operations */
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
        //        tdtDueDate.setEnabled(false);
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
            cboShareType.setEnabled(false);
            cboShareType.setEditable(false);
        }
        
    }
    
    /** Setting up Lengths for the TextFields in theu UI */
    private void setMaxLength(){
        txtFaceValue.setMaxLength(16);
        txtIssuedCapital.setMaxLength(16);
        txtSubscribedCapital.setMaxLength(16);
        txtPaidupCapital.setMaxLength(16);
        txtRefundinaYear.setMaxLength(8);
        txtRefundPeriod.setMaxLength(4);
        txtAdditionalShareRefund.setMaxLength(4);
        txtMaxShareHolding.setMaxLength(8);
        txtDividentPercentage.setMaxLength(6);
        txtUnclaimedDivPeriod.setMaxLength(5);
        txtAdmissionFee.setMaxLength(16);
        txtAdmissionFeeMin.setValidation(new CurrencyValidation(14,2));
        txtAdmissionFeeMax.setValidation(new CurrencyValidation(14,2));
        txtApplicationFee.setMaxLength(16);
        txtShareFee.setMaxLength(16);
        txtNomineeaAllowed.setMaxLength(1);
        txtMaxLoanLimit.setValidation(new NumericValidation(3,2));
        //        txtSurityLimit.setMaxLength(16);
        txtShareSuspAccount.setMaxLength(16);
        txtShareAccount.setMaxLength(16);
        txtMemFeeAcct.setMaxLength(16);
        txtApplFeeAcct.setMaxLength(16);
        txtShareFeeAcct.setMaxLength(16);
        txtLoanAvailingLimit.setValidation(new NumericValidation(3,2));
        txtNomineePeriod.setMaxLength(6);
       txtIncomeAccountHead.setAllowAll(true);
       txtMinIntialshares.setMaxLength(16);
       txtMinIntialshares.setValidation(new NumericValidation(3,2));
        txtNumPatternFollowedPrefix.setAllowAll(true);
        txtNumPatternFollowedSuffix.setAllowAll(true);
        txtMaxLoanAmt.setValidation(new CurrencyValidation(14,2));
        txtMinPension.setValidation(new CurrencyValidation(14,2));
    }
    
    /** Making the btnShareAccount enable or disable according to the actiontype **/
    private void setHelpButtonEnableDisable(boolean enable){
        btnShareSuspAccount.setEnabled(enable);
        btnShareAccount.setEnabled(enable);
        btnMemFeeAcct.setEnabled(enable);
        btnApplFeeAcct.setEnabled(enable);
        btnShareFeeAcct.setEnabled(enable);
        btnDivPayableAcct.setEnabled(enable);
        btnDivPaidAcct.setEnabled(enable);
        btnUnClaimedDivTransferAcHd.setEnabled(enable);
        btnIncomeAccountHead.setEnabled(enable);
        btnGovtSubsidyAccountHead.setEnabled(enable);
    }
    
    /* Does necessary operaion when user clicks the save button */
    private void savePerformed(){
        if(chkCaste.isSelected()==true){
            mandatoryData.add("CASTE");
        }
        if(chkReligion.isSelected()==true){
            mandatoryData.add("RELIGION");
        }
        if(chkAmsam.isSelected()==true){
            mandatoryData.add("AMSAM");
        }
        if(chkDesam.isSelected()){
            mandatoryData.add("DESAM");
        }
       
        if(chkDob.isSelected()==true){
            mandatoryData.add("DOB");
        }
        
        if(chkWardNo.isSelected()==true){
            mandatoryData.add("WARD_NO");
        }
         if(chkArea.isSelected()==true){
            mandatoryAddrData.add("AREA");
        }
        if(chkCity.isSelected()==true){
            mandatoryAddrData.add("CITY");
        }
        if(chkPinCode.isSelected()==true){
            mandatoryAddrData.add("PIN_CODE");
        }
        if(chkStreet.isSelected()==true){
            mandatoryAddrData.add("STREET");
        }
        if(chkVillage.isSelected()==true){
            mandatoryAddrData.add("VILLAGE");
        }
        if(chkTaluk.isSelected()==true){
            mandatoryAddrData.add("TALUK");
        }
         updateOBFields();
         String action;
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW ){
            action=CommonConstants.TOSTATUS_INSERT;
            saveAction(action);
        }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
            action=CommonConstants.TOSTATUS_UPDATE;
            saveAction(action);
        }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE){
            action=CommonConstants.TOSTATUS_DELETE;
            saveAction(action);
        }
    }
    
    /* Calls the execute method of ShareProductOB to do insertion or updation or deletion */
    private void saveAction(String status){
        //To check mandtoryness of the Terminal panAcHdDetails,panAcHeadDetails panel and diplay appropriate
        //error message, else proceed
        try{
            final String mandatoryMessage = checkMandatory(panShareProduct);
            String alertMsg ="";
            if(mandatoryMessage.length() > 0 ){
                displayAlert(mandatoryMessage);
            }else{
                if(observable.getCbmUnclaimedDivPeriod().getKeyForSelected()!=null){
                    if(!ClientUtil.validPeriodMaxLength(txtUnclaimedDivPeriod, CommonUtil.convertObjToStr(observable.getCbmUnclaimedDivPeriod().getKeyForSelected()))){
                        alertMsg = "unclaimedmsg";
                        showAlertWindow(alertMsg);
                        return;
                    }
                }
                
                if(observable.getCbmNomineePeriod().getKeyForSelected()!=null){
                    if(!ClientUtil.validPeriodMaxLength(txtNomineePeriod, CommonUtil.convertObjToStr(observable.getCbmNomineePeriod().getKeyForSelected()))){
                        alertMsg = "nomineeMsg";
                        showAlertWindow(alertMsg);
                        return;
                    }
                }
                
                
                if(observable.getCbmAdditionalShareRefund().getKeyForSelected()!=null){
                    if(!ClientUtil.validPeriodMaxLength(txtAdditionalShareRefund, CommonUtil.convertObjToStr(observable.getCbmAdditionalShareRefund().getKeyForSelected()))){
                        alertMsg = "shareMsg";
                        showAlertWindow(alertMsg);
                        return;
                    }
                }
                
                if(txtRefundPeriod.getText().length()!=0 && observable.getCbmRefundPeriod().getKeyForSelected()!=null){
                    if(!ClientUtil.validPeriodMaxLength(txtRefundPeriod, CommonUtil.convertObjToStr(observable.getCbmRefundPeriod().getKeyForSelected()))){
                        alertMsg = "refundMsg";
                        showAlertWindow(alertMsg);
                        return;
                    }
                }
//                added by nikhil for Share Subsidy
                if(chkSubsidyForSCST.isSelected()){
                    if(CommonUtil.convertObjToStr(txtCustomerShare.getText()).length()==0 ||
                    CommonUtil.convertObjToStr(txtCustomerShare.getText()).length()==0){
                        alertMsg = "SubsidyMsg";
                        showAlertWindow(alertMsg);
                        return;
                    }
                }
                //Added by sreekrishnan
                if(txtPensionAge.getText()!=null && txtPensionAge.getText().length()>0 && CommonUtil.convertObjToDouble(txtPensionAge.getText())>0){
                    String productType = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdTypeCr.getModel()).getKeyForSelected());
                    if(!(txtShareDuration.getText()!=null && txtShareDuration.getText().length()>0) && CommonUtil.convertObjToDouble(txtShareDuration.getText())>0){
                        alertMsg = "sharePeriod";
                        showAlertWindow(alertMsg);
                        return;
                    }else if(!(txtMinPension.getText()!=null && txtMinPension.getText().length()>0 && CommonUtil.convertObjToDouble(txtMinPension.getText())>0)){
                        alertMsg = "minPension";
                        showAlertWindow(alertMsg);
                        return;
                    }else if(!(cboProdTypeCr.getSelectedItem()!=null && !cboProdTypeCr.getSelectedItem().equals(""))){
                        alertMsg = "pensionProductType";
                        showAlertWindow(alertMsg);
                        return;
                    }else if(productType!=null && productType.length()>0 && !productType.equals("GL")){                        
                         if(!(txtTransProductId.getText()!=null && txtTransProductId.getText().length()>0)){
                            alertMsg = "pensionProductId";
                            showAlertWindow(alertMsg);
                            return;
                        }
                    }else if(!(txtAcctNo.getText()!=null && txtAcctNo.getText().length()>0)){
                        alertMsg = "pensionAccount";
                        showAlertWindow(alertMsg);
                        return;
                    }
                }
                observable.execute(status);
                //__ if the Action is not Falied, Reset the fields...
                if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
                    super.removeEditLock(((ComboBoxModel)cboShareType.getModel()).getKeyForSelected().toString());
                    settings();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    /** This will checks whether the Mandatory fields in the UI are filled up, If not filled up
     *it will retun an MandatoryMessage*/
    private String checkMandatory(javax.swing.JComponent component){
        return new MandatoryCheck().checkMandatory(getClass().getName(), component);
    }
    
    /** This will Display the Mandatory Message in a Dialog Box */
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    /* set the screen after the updation,insertion, deletion */
    private void settings(){
        observable.resetForm();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panShareProduct, false);
        setButtonEnableDisable();
        setHelpButtonEnableDisable(false);
        setBtnPanLoanOperationsEnable(false);
        observable.setResultStatus();
    }
    
    private void setBtnPanLoanOperationsEnable(boolean flag){
        btnTabNew.setEnabled(flag);
        btnTabSave.setEnabled(flag);
        btnTabDelete.setEnabled(flag);
    }
    
    /** This will show a popup screen which shows all tbe Rows.of the table */
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        HashMap where_map = new HashMap();
        if (currField.equals(ClientConstants.ACTION_STATUS[2])  ||
        currField.equals(ClientConstants.ACTION_STATUS[3]) || currField.equals(ClientConstants.ACTION_STATUS[17])) {
            ArrayList lst = new ArrayList();
            lst.add("SHARE_TYPE");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            viewMap.put(CommonConstants.MAP_NAME, "getSelectShareProductMap");
        }else if(currField.equals("PROD_ID")){  
            where_map.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
            String prodType = CommonUtil.convertObjToStr((((ComboBoxModel) (cboProdTypeCr).getModel())).getKeyForSelected());
            viewMap.put(CommonConstants.MAP_NAME, "InterMaintenance.getProductData" + CommonUtil.convertObjToStr(observable.getCbmPensionProducts().getKeyForSelected()));
            viewMap.put(CommonConstants.MAP_WHERE, where_map);
        } else if (viewType.equals("ACCT_NO")) {
            System.out.println("observable.getPensionProductType()%#%#%"+observable.getPensionProductType());
                if(!observable.getPensionProductType().equals("GL")){
                where_map.put("PROD_ID", txtTransProductId.getText());
                where_map.put("SELECTED_BRANCH", TrueTransactMain.BRANCH_ID);
                viewMap.put(CommonConstants.MAP_WHERE, where_map);
                if (observable.getPensionProductType().equals("TD") || observable.getPensionProductType().equals("TL") || observable.getPensionProductType().equals("AB")) {
                    if (observable.getPensionProductType().equals("TL") || observable.getPensionProductType().equals("AB")) {
                        where_map.put("RECEIPT", "RECEIPT");
                    }
                    viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"
                            + CommonUtil.convertObjToStr(((ComboBoxModel) cboProdTypeCr.getModel()).getKeyForSelected()));
                } else {
                    viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"
                            + CommonUtil.convertObjToStr(((ComboBoxModel) cboProdTypeCr.getModel()).getKeyForSelected()));
                }   
            }else{
                   viewMap.put(CommonConstants.MAP_NAME, "getSelectAccountHead"); 
                }
        }else {
            viewMap.put(CommonConstants.MAP_NAME, "getSelectAccountHead");
        }
        new ViewAll(this,viewMap).show();
    }
    
    /* This method is used to fill up all tbe UIFields after the user
     *selects the desired row in the popup */
    public void fillData(Object  map) {
        setModified(true);
        HashMap hash = (HashMap) map;
        if (viewType != null) {
            if (viewType.equals(ClientConstants.ACTION_STATUS[2]) ||
            viewType.equals(ClientConstants.ACTION_STATUS[3])|| viewType.equals(AUTHORIZE) ||
            viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                hash.put(CommonConstants.MAP_WHERE, hash.get("SHARE_TYPE"));
                hash.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                
                if (viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) ||
                viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                    ClientUtil.enableDisable(panShareProduct, false);
                    setHelpButtonEnableDisable(false);
                    observable.createDeletedMap();
                    ClientUtil.enableDisable(panLoanType,false);
                } else {
                    ClientUtil.enableDisable(panShareProduct, true);
                    setHelpButtonEnableDisable(true);
                    ClientUtil.enableDisable(panLoanType,false);
                    btnTabNew.setEnabled(true);
                }
                observable.populateData(hash);  
                System.out.println("setPensionAge#%#%#% vvvvvvvv"+observable.getPensionAge());

                
                System.out.println("gettext@##@%#%@"+txtPensionAge.getText());
                setButtonEnableDisable();
                if(viewType ==  AUTHORIZE) {
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                }
            } else if(viewType.equals(VIEW_TYPE_SHARE_SUSPENSE_ACCT)){
                txtShareSuspAccount.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                txtShareSuspAccountDesc.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
                txtShareSuspAccountDesc.setEnabled(false);
            } 
            else if(viewType.equals(VIEW_TYPE_SHARE_ACCT)){
                txtShareAccount.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                txtShareAccountDesc.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
                txtShareAccountDesc.setEnabled(false);
            } 
            else if(viewType.equals(VIEW_TYPE_MEMBERSHIP_FEE_ACCT)){
                txtMemFeeAcct.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                txtMemFeeAcctDesc.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
                txtMemFeeAcctDesc.setEnabled(false);
            } 
            else if(viewType.equals(VIEW_TYPE_APPLICATION_FEE_ACCT)){
                txtApplFeeAcct.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                txtApplFeeAcctDesc.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
                txtApplFeeAcctDesc.setEnabled(false);
            }
            else if(viewType.equals(VIEW_TYPE_SHARE_FEE_ACCT)){
                txtShareFeeAcct.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                txtShareFeeAcctDesc.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
                txtShareFeeAcctDesc.setEnabled(false);
            }
            else if(viewType.equals(VIEW_TYPE_DIV_PAYABLE_ACCT)){
                txtDivPayableAcct.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                 txtDivPayableAcctDesc.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
                 txtDivPayableAcctDesc.setEnabled(false);
            }
            else if(viewType.equals(VIEW_TYPE_DIV_PAID_ACCT)){
                txtDivPaidAcct.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                txtDivPaidAcctDesc.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
                txtDivPaidAcctDesc.setEnabled(false);
            } 
            else if(viewType.equals("UNCLAIMEDTRANSFERACHD")){
                txtUnClaimedDivTransferAcHd.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                txtUnClaimedDivTransferAcHdDesc.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
                txtUnClaimedDivTransferAcHdDesc.setEnabled(false);
            }
            else if(viewType.equals("INCOMEACCOUNTHEAD")){
                txtIncomeAccountHead.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                txtIncomeAccountHeadDesc.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_DESC")));
                txtIncomeAccountHeadDesc.setEnabled(false);
              System.out.println("the head is"+ txtIncomeAccountHead.getText());
            }else if(viewType.equals("GOVTSUBSIDYACCOUNTHEAD")){
                txtGovtSubsidyAccountHead.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
            }else if (viewType == "PROD_ID") {
                txtTransProductId.setText(CommonUtil.convertObjToStr(hash.get("PROD_ID")));
                observable.setPensionProductId(txtTransProductId.getText());
            }else if (viewType == "ACCT_NO") {
                if(!observable.getPensionProductType().equals("GL")){
                    txtAcctNo.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
                }else{
                    txtAcctNo.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                }
                observable.setPensionAcNo(txtAcctNo.getText());
            }
        }
       // update(null,null);
    }
    
    /** This will show the alertwindow when the user enters the already existing ShareType **/
    private int showAlertWindow(String alertMsg) throws Exception {
        int optionSelected = 1;
        String[] options = {resourceBundle.getString("cDialogOK")};
        optionSelected = COptionPane.showOptionDialog(null,resourceBundle.getString(alertMsg), CommonConstants.INFORMATIONTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        return optionSelected;
    }
    
    
    /** This will do necessary operation for authorization **/
    public void authorizeStatus(String authorizeStatus) {
        if (!viewType.equals(AUTHORIZE)){
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "getShareProductAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeShareProduct");
            mapParam.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            setModified(true);
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        } else if (viewType.equals(AUTHORIZE)){
            String strWarnMsg = tabShareProduct.isAllTabsVisited();
            if (strWarnMsg.length() > 0){
                displayAlert(strWarnMsg);
                return;
            }
            strWarnMsg = null;
            tabShareProduct.resetVisits();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, ClientUtil.getCurrentDate());
            singleAuthorizeMap.put("SHARE_TYPE", observable.getCbmShareType().getKeyForSelected());
            ClientUtil.execute("authorizeShareProduct", singleAuthorizeMap);
            viewType = "";
            btnCancelActionPerformed(null);
        }
    }
    
    public static void main(String args[]){
        javax.swing.JFrame frame = new javax.swing.JFrame();
        ShareProductUI ui = new ShareProductUI();
        frame.getContentPane().add(ui);
        ui.setVisible(true);
        frame.setVisible(true);
        frame.setSize(600,600);
        frame.show();
        ui.show();
        
        
    }
    
    /** Method to do the validation of Subscribed Capital and paidup captial to be greater
     *than the Authorized Capital **/
    private void captitalValidation(CTextField txtAuthorizedCapital, CTextField txtCapital){
        Double  authorizedCapital = CommonUtil.convertObjToDouble(txtAuthorizedCapital.getText());
        Double capital = CommonUtil.convertObjToDouble(txtCapital.getText());
        if(capital.doubleValue()>authorizedCapital.doubleValue()){
            txtCapital.setText("");
        }
        
    }
    
    
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdgAdmissionFeeType = new com.see.truetransact.uicomponent.CButtonGroup();
        panShareProduct = new com.see.truetransact.uicomponent.CPanel();
        tabShareProduct = new com.see.truetransact.uicomponent.CTabbedPane();
        panShare = new com.see.truetransact.uicomponent.CPanel();
        lblShareType = new com.see.truetransact.uicomponent.CLabel();
        cboShareType = new com.see.truetransact.uicomponent.CComboBox();
        lblFaceValue = new com.see.truetransact.uicomponent.CLabel();
        txtFaceValue = new com.see.truetransact.uicomponent.CTextField();
        lblAuthorizedCapital = new com.see.truetransact.uicomponent.CLabel();
        txtIssuedCapital = new com.see.truetransact.uicomponent.CTextField();
        lblSubscribedCapital = new com.see.truetransact.uicomponent.CLabel();
        txtSubscribedCapital = new com.see.truetransact.uicomponent.CTextField();
        lblPaidCapital = new com.see.truetransact.uicomponent.CLabel();
        txtPaidupCapital = new com.see.truetransact.uicomponent.CTextField();
        lblMaxShareHolding = new com.see.truetransact.uicomponent.CLabel();
        txtMaxShareHolding = new com.see.truetransact.uicomponent.CTextField();
        lblMinIntialShares = new com.see.truetransact.uicomponent.CLabel();
        txtMinIntialshares = new com.see.truetransact.uicomponent.CTextField();
        lblAdmissionFeeType = new com.see.truetransact.uicomponent.CLabel();
        panAutoRenewal1 = new com.see.truetransact.uicomponent.CPanel();
        rdoAdmissionFeeType_Fixed = new com.see.truetransact.uicomponent.CRadioButton();
        rdoAdmissionFeeType_Percent = new com.see.truetransact.uicomponent.CRadioButton();
        lblAdmissionFee = new com.see.truetransact.uicomponent.CLabel();
        txtAdmissionFee = new com.see.truetransact.uicomponent.CTextField();
        lblAdmissionFeeMin = new com.see.truetransact.uicomponent.CLabel();
        txtAdmissionFeeMin = new com.see.truetransact.uicomponent.CTextField();
        lblAdmissionFeeMax = new com.see.truetransact.uicomponent.CLabel();
        txtAdmissionFeeMax = new com.see.truetransact.uicomponent.CTextField();
        lblApplicationFee = new com.see.truetransact.uicomponent.CLabel();
        txtApplicationFee = new com.see.truetransact.uicomponent.CTextField();
        lblShareFee = new com.see.truetransact.uicomponent.CLabel();
        txtShareFee = new com.see.truetransact.uicomponent.CTextField();
        lblReqActHolder = new com.see.truetransact.uicomponent.CLabel();
        chkReqActHolder = new com.see.truetransact.uicomponent.CCheckBox();
        lblNommineeAllowed = new com.see.truetransact.uicomponent.CLabel();
        txtNomineeaAllowed = new com.see.truetransact.uicomponent.CTextField();
        lblNomineePeriod = new com.see.truetransact.uicomponent.CLabel();
        panNominee = new com.see.truetransact.uicomponent.CPanel();
        cboNomineePeriod = new com.see.truetransact.uicomponent.CComboBox();
        txtNomineePeriod = new com.see.truetransact.uicomponent.CTextField();
        lblRefundinaYear = new com.see.truetransact.uicomponent.CLabel();
        txtRefundinaYear = new com.see.truetransact.uicomponent.CTextField();
        lblRefundPeriod = new com.see.truetransact.uicomponent.CLabel();
        panRefundPeriod = new com.see.truetransact.uicomponent.CPanel();
        txtRefundPeriod = new com.see.truetransact.uicomponent.CTextField();
        cboRefundPeriod = new com.see.truetransact.uicomponent.CComboBox();
        lblAdditionalShareRefund = new com.see.truetransact.uicomponent.CLabel();
        panAdditonalShareRefund = new com.see.truetransact.uicomponent.CPanel();
        cboAdditionalShareRefund = new com.see.truetransact.uicomponent.CComboBox();
        txtAdditionalShareRefund = new com.see.truetransact.uicomponent.CTextField();
        lblRatification = new com.see.truetransact.uicomponent.CLabel();
        panRatification = new com.see.truetransact.uicomponent.CPanel();
        rdoRatificationYes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoRatificationNo = new com.see.truetransact.uicomponent.CRadioButton();
        lblNumPatternFollowed = new com.see.truetransact.uicomponent.CLabel();
        txtNumPatternFollowedPrefix = new com.see.truetransact.uicomponent.CTextField();
        txtNumPatternFollowedSuffix = new com.see.truetransact.uicomponent.CTextField();
        lblConsiderSalaryRecovery = new com.see.truetransact.uicomponent.CLabel();
        chkConsiderSalaryRecovery = new com.see.truetransact.uicomponent.CCheckBox();
        chkSubsidyForSCST = new com.see.truetransact.uicomponent.CCheckBox();
        lblSubsidyForSCST2 = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerShare = new com.see.truetransact.uicomponent.CLabel();
        txtCustomerShare = new com.see.truetransact.uicomponent.CTextField();
        lblGovernmentShare = new com.see.truetransact.uicomponent.CLabel();
        txtGovernmentShare = new com.see.truetransact.uicomponent.CTextField();
        lblOutstandingReq = new com.see.truetransact.uicomponent.CLabel();
        chkOutstandingRequired = new com.see.truetransact.uicomponent.CCheckBox();
        chkShareCerti = new com.see.truetransact.uicomponent.CCheckBox();
        lblFullClosure = new com.see.truetransact.uicomponent.CLabel();
        panFullClosure = new com.see.truetransact.uicomponent.CPanel();
        rdoFullClosureYes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoFullClosureNo = new com.see.truetransact.uicomponent.CRadioButton();
        chkDRF = new com.see.truetransact.uicomponent.CCheckBox();
        panBorrowing = new com.see.truetransact.uicomponent.CPanel();
        panLoan = new com.see.truetransact.uicomponent.CPanel();
        panTblLoanType = new com.see.truetransact.uicomponent.CPanel();
        srpLoanType = new com.see.truetransact.uicomponent.CScrollPane();
        tblLoanType = new com.see.truetransact.uicomponent.CTable();
        panLoanOperations = new com.see.truetransact.uicomponent.CPanel();
        btnTabNew = new com.see.truetransact.uicomponent.CButton();
        btnTabSave = new com.see.truetransact.uicomponent.CButton();
        btnTabDelete = new com.see.truetransact.uicomponent.CButton();
        panLoanType = new com.see.truetransact.uicomponent.CPanel();
        lblLoanType = new com.see.truetransact.uicomponent.CLabel();
        cboLoanType = new com.see.truetransact.uicomponent.CComboBox();
        lblPercentageLoanAmount = new com.see.truetransact.uicomponent.CLabel();
        txtLoanAvailingLimit = new com.see.truetransact.uicomponent.CTextField();
        lblPercentageLoanAmt = new com.see.truetransact.uicomponent.CLabel();
        txtMaxLoanLimit = new com.see.truetransact.uicomponent.CTextField();
        lblShareSubscriptionBorrower = new com.see.truetransact.uicomponent.CLabel();
        lblShareSubscriptionSurity = new com.see.truetransact.uicomponent.CLabel();
        lblMaxLoanAmt = new com.see.truetransact.uicomponent.CLabel();
        txtMaxLoanAmt = new com.see.truetransact.uicomponent.CTextField();
        panDivident = new com.see.truetransact.uicomponent.CPanel();
        lblCalculatedDate = new com.see.truetransact.uicomponent.CLabel();
        tdtCalculatedDate = new com.see.truetransact.uicomponent.CDateField();
        lblDivRunDate = new com.see.truetransact.uicomponent.CLabel();
        tdtDivRunDate = new com.see.truetransact.uicomponent.CDateField();
        lblDividentPercentage = new com.see.truetransact.uicomponent.CLabel();
        txtDividentPercentage = new com.see.truetransact.uicomponent.CTextField();
        lblDivCalcFrequency = new com.see.truetransact.uicomponent.CLabel();
        cboDivCalcFrequency = new com.see.truetransact.uicomponent.CComboBox();
        lblDivAppFrequency = new com.see.truetransact.uicomponent.CLabel();
        cboDivAppFrequency = new com.see.truetransact.uicomponent.CComboBox();
        lblLastuncaimedUpTo = new com.see.truetransact.uicomponent.CLabel();
        tdtLastuncaimedUpTo = new com.see.truetransact.uicomponent.CDateField();
        lblLastuncaimedRunDate = new com.see.truetransact.uicomponent.CLabel();
        tdtLastuncaimedRunDate = new com.see.truetransact.uicomponent.CDateField();
        panDividentNew = new com.see.truetransact.uicomponent.CPanel();
        lblMinDividendAmount = new com.see.truetransact.uicomponent.CLabel();
        txtMinDividendAmount = new com.see.truetransact.uicomponent.CTextField();
        lblDivCalcFrequencyNew = new com.see.truetransact.uicomponent.CLabel();
        cboDivCalcFrequencyNew = new com.see.truetransact.uicomponent.CComboBox();
        lblDivCalcType = new com.see.truetransact.uicomponent.CLabel();
        cboDivCalcType = new com.see.truetransact.uicomponent.CComboBox();
        lblDividendRounding = new com.see.truetransact.uicomponent.CLabel();
        lblUnClaimedDivPeriod = new com.see.truetransact.uicomponent.CLabel();
        panUInClaimedDivident = new com.see.truetransact.uicomponent.CPanel();
        cboUnclaimedDivPeriod = new com.see.truetransact.uicomponent.CComboBox();
        txtUnclaimedDivPeriod = new com.see.truetransact.uicomponent.CTextField();
        cboDividendRounding = new com.see.truetransact.uicomponent.CComboBox();
        panAccountDetails = new com.see.truetransact.uicomponent.CPanel();
        panAcctDet = new com.see.truetransact.uicomponent.CPanel();
        panShareSuspAccount = new com.see.truetransact.uicomponent.CPanel();
        txtShareSuspAccount = new com.see.truetransact.uicomponent.CTextField();
        btnShareSuspAccount = new com.see.truetransact.uicomponent.CButton();
        txtShareSuspAccountDesc = new com.see.truetransact.uicomponent.CTextField();
        lblShareSuspAccount = new com.see.truetransact.uicomponent.CLabel();
        panShareAccount = new com.see.truetransact.uicomponent.CPanel();
        txtShareAccount = new com.see.truetransact.uicomponent.CTextField();
        btnShareAccount = new com.see.truetransact.uicomponent.CButton();
        txtShareAccountDesc = new com.see.truetransact.uicomponent.CTextField();
        lblShareAccount = new com.see.truetransact.uicomponent.CLabel();
        panMemFeeAcct = new com.see.truetransact.uicomponent.CPanel();
        txtMemFeeAcct = new com.see.truetransact.uicomponent.CTextField();
        btnMemFeeAcct = new com.see.truetransact.uicomponent.CButton();
        txtMemFeeAcctDesc = new com.see.truetransact.uicomponent.CTextField();
        lblMemFeeAcct = new com.see.truetransact.uicomponent.CLabel();
        panApplFeeAcct = new com.see.truetransact.uicomponent.CPanel();
        txtApplFeeAcct = new com.see.truetransact.uicomponent.CTextField();
        btnApplFeeAcct = new com.see.truetransact.uicomponent.CButton();
        txtApplFeeAcctDesc = new com.see.truetransact.uicomponent.CTextField();
        lblApplFeeAcct = new com.see.truetransact.uicomponent.CLabel();
        panShareFeeAcct = new com.see.truetransact.uicomponent.CPanel();
        txtShareFeeAcct = new com.see.truetransact.uicomponent.CTextField();
        btnShareFeeAcct = new com.see.truetransact.uicomponent.CButton();
        txtShareFeeAcctDesc = new com.see.truetransact.uicomponent.CTextField();
        lblShareFeeAcct = new com.see.truetransact.uicomponent.CLabel();
        lblSharedivPaidAcct = new com.see.truetransact.uicomponent.CLabel();
        panDivPayableAcHd = new com.see.truetransact.uicomponent.CPanel();
        txtDivPayableAcct = new com.see.truetransact.uicomponent.CTextField();
        btnDivPayableAcct = new com.see.truetransact.uicomponent.CButton();
        txtDivPayableAcctDesc = new com.see.truetransact.uicomponent.CTextField();
        lblSharedivPayableAcct = new com.see.truetransact.uicomponent.CLabel();
        panDivPaidAcHd = new com.see.truetransact.uicomponent.CPanel();
        txtDivPaidAcct = new com.see.truetransact.uicomponent.CTextField();
        btnDivPaidAcct = new com.see.truetransact.uicomponent.CButton();
        txtDivPaidAcctDesc = new com.see.truetransact.uicomponent.CTextField();
        lblUnClaimedDivTransferAcHd = new com.see.truetransact.uicomponent.CLabel();
        panUnClaimedDivTransferAcHd = new com.see.truetransact.uicomponent.CPanel();
        txtUnClaimedDivTransferAcHd = new com.see.truetransact.uicomponent.CTextField();
        btnUnClaimedDivTransferAcHd = new com.see.truetransact.uicomponent.CButton();
        txtUnClaimedDivTransferAcHdDesc = new com.see.truetransact.uicomponent.CTextField();
        lblncomeAccountHead = new com.see.truetransact.uicomponent.CLabel();
        panIncomeAccountHead = new com.see.truetransact.uicomponent.CPanel();
        txtIncomeAccountHead = new com.see.truetransact.uicomponent.CTextField();
        btnIncomeAccountHead = new com.see.truetransact.uicomponent.CButton();
        txtIncomeAccountHeadDesc = new com.see.truetransact.uicomponent.CTextField();
        panGovtSubsidyAccountHead = new com.see.truetransact.uicomponent.CPanel();
        txtGovtSubsidyAccountHead = new com.see.truetransact.uicomponent.CTextField();
        btnGovtSubsidyAccountHead = new com.see.truetransact.uicomponent.CButton();
        lbGovtSubsidyAccountHead = new com.see.truetransact.uicomponent.CLabel();
        jPanel1 = new javax.swing.JPanel();
        panGldRenewal = new com.see.truetransact.uicomponent.CPanel();
        lblCaste = new com.see.truetransact.uicomponent.CLabel();
        lblReligion = new com.see.truetransact.uicomponent.CLabel();
        lblWardNo = new com.see.truetransact.uicomponent.CLabel();
        chkCaste = new com.see.truetransact.uicomponent.CCheckBox();
        chkReligion = new com.see.truetransact.uicomponent.CCheckBox();
        chkWardNo = new com.see.truetransact.uicomponent.CCheckBox();
        lblDob = new com.see.truetransact.uicomponent.CLabel();
        chkDesam = new com.see.truetransact.uicomponent.CCheckBox();
        lblArea = new com.see.truetransact.uicomponent.CLabel();
        lblStreet = new com.see.truetransact.uicomponent.CLabel();
        lblCity = new com.see.truetransact.uicomponent.CLabel();
        lblPincode = new com.see.truetransact.uicomponent.CLabel();
        lblTaluk = new com.see.truetransact.uicomponent.CLabel();
        lblVillage = new com.see.truetransact.uicomponent.CLabel();
        lblDesam = new com.see.truetransact.uicomponent.CLabel();
        lblAmsam = new com.see.truetransact.uicomponent.CLabel();
        chkVillage = new com.see.truetransact.uicomponent.CCheckBox();
        chkTaluk = new com.see.truetransact.uicomponent.CCheckBox();
        chkPinCode = new com.see.truetransact.uicomponent.CCheckBox();
        chkCity = new com.see.truetransact.uicomponent.CCheckBox();
        chkStreet = new com.see.truetransact.uicomponent.CCheckBox();
        chkArea = new com.see.truetransact.uicomponent.CCheckBox();
        chkDob = new com.see.truetransact.uicomponent.CCheckBox();
        chkAmsam = new com.see.truetransact.uicomponent.CCheckBox();
        panPensionScheme = new com.see.truetransact.uicomponent.CPanel();
        panPension = new com.see.truetransact.uicomponent.CPanel();
        panDetails = new com.see.truetransact.uicomponent.CPanel();
        panAccountDetails1 = new com.see.truetransact.uicomponent.CPanel();
        cboProdTypeCr = new com.see.truetransact.uicomponent.CComboBox();
        lblProdTypeCr = new com.see.truetransact.uicomponent.CLabel();
        lblAccNo = new com.see.truetransact.uicomponent.CLabel();
        lblProdIdCr = new com.see.truetransact.uicomponent.CLabel();
        panAccHd = new com.see.truetransact.uicomponent.CPanel();
        txtAcctNo = new com.see.truetransact.uicomponent.CTextField();
        btnAccNo = new com.see.truetransact.uicomponent.CButton();
        panAcctNo = new com.see.truetransact.uicomponent.CPanel();
        panDebitAccHead = new com.see.truetransact.uicomponent.CPanel();
        txtTransProductId = new com.see.truetransact.uicomponent.CTextField();
        btnTransProductId = new com.see.truetransact.uicomponent.CButton();
        txtPensionAge = new com.see.truetransact.uicomponent.CTextField();
        txtShareDuration = new com.see.truetransact.uicomponent.CTextField();
        txtMinPension = new com.see.truetransact.uicomponent.CTextField();
        lblPAge = new com.see.truetransact.uicomponent.CLabel();
        lblPeriod = new com.see.truetransact.uicomponent.CLabel();
        lblAmount = new com.see.truetransact.uicomponent.CLabel();
        tbrShareProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace7 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace8 = new com.see.truetransact.uicomponent.CLabel();
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
        mbrShareProduct = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptNew = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptSave = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMaximumSize(new java.awt.Dimension(850, 600));
        setMinimumSize(new java.awt.Dimension(850, 600));
        setPreferredSize(new java.awt.Dimension(850, 600));

        panShareProduct.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panShareProduct.setLayout(new java.awt.GridBagLayout());

        tabShareProduct.setMinimumSize(new java.awt.Dimension(769, 321));
        tabShareProduct.setNextFocusableComponent(txtDividentPercentage);
        tabShareProduct.setPreferredSize(new java.awt.Dimension(823, 609));

        panShare.setLayout(new java.awt.GridBagLayout());

        lblShareType.setText("Share Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 4, 1);
        panShare.add(lblShareType, gridBagConstraints);

        cboShareType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboShareType.setNextFocusableComponent(txtShareSuspAccount);
        cboShareType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboShareTypeActionPerformed(evt);
            }
        });
        cboShareType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboShareTypeItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panShare.add(cboShareType, gridBagConstraints);

        lblFaceValue.setText("Face Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(lblFaceValue, gridBagConstraints);

        txtFaceValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFaceValue.setNextFocusableComponent(txtIssuedCapital);
        txtFaceValue.setValidation(new CurrencyValidation(14,2));
        txtFaceValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFaceValueFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(txtFaceValue, gridBagConstraints);

        lblAuthorizedCapital.setText("Authorized Capital");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(lblAuthorizedCapital, gridBagConstraints);

        txtIssuedCapital.setMinimumSize(new java.awt.Dimension(100, 21));
        txtIssuedCapital.setNextFocusableComponent(txtSubscribedCapital);
        txtIssuedCapital.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(txtIssuedCapital, gridBagConstraints);

        lblSubscribedCapital.setText("Subscribed Capital");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(lblSubscribedCapital, gridBagConstraints);

        txtSubscribedCapital.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSubscribedCapital.setNextFocusableComponent(txtPaidupCapital);
        txtSubscribedCapital.setValidation(new CurrencyValidation(14,2));
        txtSubscribedCapital.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSubscribedCapitalFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(txtSubscribedCapital, gridBagConstraints);

        lblPaidCapital.setText("Paidup Capital");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(lblPaidCapital, gridBagConstraints);

        txtPaidupCapital.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPaidupCapital.setNextFocusableComponent(txtMaxShareHolding);
        txtPaidupCapital.setValidation(new CurrencyValidation(14,2));
        txtPaidupCapital.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPaidupCapitalFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(txtPaidupCapital, gridBagConstraints);

        lblMaxShareHolding.setText("Maximum Shareholding");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(lblMaxShareHolding, gridBagConstraints);

        txtMaxShareHolding.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMaxShareHolding.setNextFocusableComponent(txtAdmissionFee);
        txtMaxShareHolding.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(txtMaxShareHolding, gridBagConstraints);

        lblMinIntialShares.setText("Minimum No.of Initial Shares");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(lblMinIntialShares, gridBagConstraints);

        txtMinIntialshares.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMinIntialshares.setNextFocusableComponent(txtAdmissionFee);
        txtMinIntialshares.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(txtMinIntialshares, gridBagConstraints);

        lblAdmissionFeeType.setText("Admission Fee Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(lblAdmissionFeeType, gridBagConstraints);

        panAutoRenewal1.setMinimumSize(new java.awt.Dimension(100, 16));
        panAutoRenewal1.setPreferredSize(new java.awt.Dimension(140, 18));
        panAutoRenewal1.setLayout(new java.awt.GridBagLayout());

        rdgAdmissionFeeType.add(rdoAdmissionFeeType_Fixed);
        rdoAdmissionFeeType_Fixed.setText("Fixed");
        rdoAdmissionFeeType_Fixed.setMaximumSize(new java.awt.Dimension(60, 18));
        rdoAdmissionFeeType_Fixed.setMinimumSize(new java.awt.Dimension(60, 18));
        rdoAdmissionFeeType_Fixed.setPreferredSize(new java.awt.Dimension(50, 18));
        rdoAdmissionFeeType_Fixed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoAdmissionFeeType_FixedActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panAutoRenewal1.add(rdoAdmissionFeeType_Fixed, gridBagConstraints);

        rdgAdmissionFeeType.add(rdoAdmissionFeeType_Percent);
        rdoAdmissionFeeType_Percent.setText("Percent");
        rdoAdmissionFeeType_Percent.setMaximumSize(new java.awt.Dimension(70, 18));
        rdoAdmissionFeeType_Percent.setMinimumSize(new java.awt.Dimension(70, 18));
        rdoAdmissionFeeType_Percent.setPreferredSize(new java.awt.Dimension(45, 18));
        rdoAdmissionFeeType_Percent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoAdmissionFeeType_PercentActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panAutoRenewal1.add(rdoAdmissionFeeType_Percent, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panShare.add(panAutoRenewal1, gridBagConstraints);

        lblAdmissionFee.setText("Admission Fee");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(lblAdmissionFee, gridBagConstraints);

        txtAdmissionFee.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAdmissionFee.setNextFocusableComponent(txtAdmissionFeeMin);
        txtAdmissionFee.setValidation(new CurrencyValidation(14,2));
        txtAdmissionFee.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAdmissionFeeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(txtAdmissionFee, gridBagConstraints);

        lblAdmissionFeeMin.setText("Min Admission Fee");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(lblAdmissionFeeMin, gridBagConstraints);

        txtAdmissionFeeMin.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAdmissionFeeMin.setNextFocusableComponent(txtAdmissionFeeMax);
        txtAdmissionFeeMin.setValidation(new CurrencyValidation(14,2));
        txtAdmissionFeeMin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAdmissionFeeMinActionPerformed(evt);
            }
        });
        txtAdmissionFeeMin.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAdmissionFeeMinFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(txtAdmissionFeeMin, gridBagConstraints);

        lblAdmissionFeeMax.setText("Max Admission Fee");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(lblAdmissionFeeMax, gridBagConstraints);

        txtAdmissionFeeMax.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAdmissionFeeMax.setNextFocusableComponent(txtApplicationFee);
        txtAdmissionFeeMax.setValidation(new CurrencyValidation(14,2));
        txtAdmissionFeeMax.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAdmissionFeeMaxFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(txtAdmissionFeeMax, gridBagConstraints);

        lblApplicationFee.setText("Application Fee");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(lblApplicationFee, gridBagConstraints);

        txtApplicationFee.setMinimumSize(new java.awt.Dimension(100, 21));
        txtApplicationFee.setNextFocusableComponent(txtShareFee);
        txtApplicationFee.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(txtApplicationFee, gridBagConstraints);

        lblShareFee.setText("Share Fee");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(lblShareFee, gridBagConstraints);

        txtShareFee.setMinimumSize(new java.awt.Dimension(100, 21));
        txtShareFee.setNextFocusableComponent(chkReqActHolder);
        txtShareFee.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(txtShareFee, gridBagConstraints);

        lblReqActHolder.setText("Require Existing Accountholder");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(lblReqActHolder, gridBagConstraints);

        chkReqActHolder.setNextFocusableComponent(txtNomineeaAllowed);
        chkReqActHolder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                chkReqActHolderMouseClicked(evt);
            }
        });
        chkReqActHolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkReqActHolderActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panShare.add(chkReqActHolder, gridBagConstraints);

        lblNommineeAllowed.setText("No. Of Nominee Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(lblNommineeAllowed, gridBagConstraints);

        txtNomineeaAllowed.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNomineeaAllowed.setNextFocusableComponent(txtNomineePeriod);
        txtNomineeaAllowed.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(txtNomineeaAllowed, gridBagConstraints);

        lblNomineePeriod.setText("Nominal Member Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(lblNomineePeriod, gridBagConstraints);

        panNominee.setLayout(new java.awt.GridBagLayout());

        cboNomineePeriod.setMinimumSize(new java.awt.Dimension(70, 21));
        cboNomineePeriod.setNextFocusableComponent(txtRefundinaYear);
        cboNomineePeriod.setPreferredSize(new java.awt.Dimension(70, 21));
        cboNomineePeriod.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboNomineePeriodFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        panNominee.add(cboNomineePeriod, gridBagConstraints);

        txtNomineePeriod.setMinimumSize(new java.awt.Dimension(50, 21));
        txtNomineePeriod.setNextFocusableComponent(cboNomineePeriod);
        txtNomineePeriod.setPreferredSize(new java.awt.Dimension(50, 21));
        txtNomineePeriod.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panNominee.add(txtNomineePeriod, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panShare.add(panNominee, gridBagConstraints);

        lblRefundinaYear.setText("Refund in a Year");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(lblRefundinaYear, gridBagConstraints);

        txtRefundinaYear.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRefundinaYear.setNextFocusableComponent(txtRefundPeriod);
        txtRefundinaYear.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(txtRefundinaYear, gridBagConstraints);

        lblRefundPeriod.setText("Lock up Period for Refund of Shares");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panShare.add(lblRefundPeriod, gridBagConstraints);

        panRefundPeriod.setLayout(new java.awt.GridBagLayout());

        txtRefundPeriod.setMinimumSize(new java.awt.Dimension(50, 21));
        txtRefundPeriod.setNextFocusableComponent(cboRefundPeriod);
        txtRefundPeriod.setPreferredSize(new java.awt.Dimension(50, 21));
        txtRefundPeriod.setValidation(new NumericValidation());
        txtRefundPeriod.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRefundPeriodFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRefundPeriod.add(txtRefundPeriod, gridBagConstraints);

        cboRefundPeriod.setMinimumSize(new java.awt.Dimension(70, 21));
        cboRefundPeriod.setNextFocusableComponent(txtAdditionalShareRefund);
        cboRefundPeriod.setPreferredSize(new java.awt.Dimension(70, 21));
        cboRefundPeriod.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboRefundPeriodFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        panRefundPeriod.add(cboRefundPeriod, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panShare.add(panRefundPeriod, gridBagConstraints);

        lblAdditionalShareRefund.setText("Lock up Period for Additional Shares");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panShare.add(lblAdditionalShareRefund, gridBagConstraints);

        panAdditonalShareRefund.setLayout(new java.awt.GridBagLayout());

        cboAdditionalShareRefund.setMinimumSize(new java.awt.Dimension(70, 21));
        cboAdditionalShareRefund.setNextFocusableComponent(cboAdditionalShareRefund);
        cboAdditionalShareRefund.setPreferredSize(new java.awt.Dimension(70, 21));
        cboAdditionalShareRefund.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboAdditionalShareRefundFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        panAdditonalShareRefund.add(cboAdditionalShareRefund, gridBagConstraints);

        txtAdditionalShareRefund.setMinimumSize(new java.awt.Dimension(50, 21));
        txtAdditionalShareRefund.setNextFocusableComponent(cboAdditionalShareRefund);
        txtAdditionalShareRefund.setPreferredSize(new java.awt.Dimension(50, 21));
        txtAdditionalShareRefund.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panAdditonalShareRefund.add(txtAdditionalShareRefund, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panShare.add(panAdditonalShareRefund, gridBagConstraints);

        lblRatification.setText("Ratification Required");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.ipadx = 14;
        gridBagConstraints.ipady = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 1);
        panShare.add(lblRatification, gridBagConstraints);

        panRatification.setMinimumSize(new java.awt.Dimension(100, 16));
        panRatification.setPreferredSize(new java.awt.Dimension(140, 18));
        panRatification.setLayout(new java.awt.GridBagLayout());

        rdgAdmissionFeeType.add(rdoRatificationYes);
        rdoRatificationYes.setText("Yes");
        rdoRatificationYes.setMaximumSize(new java.awt.Dimension(60, 18));
        rdoRatificationYes.setMinimumSize(new java.awt.Dimension(60, 18));
        rdoRatificationYes.setPreferredSize(new java.awt.Dimension(50, 18));
        rdoRatificationYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoRatificationYesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panRatification.add(rdoRatificationYes, gridBagConstraints);

        rdgAdmissionFeeType.add(rdoRatificationNo);
        rdoRatificationNo.setText("No");
        rdoRatificationNo.setMaximumSize(new java.awt.Dimension(70, 18));
        rdoRatificationNo.setMinimumSize(new java.awt.Dimension(70, 18));
        rdoRatificationNo.setPreferredSize(new java.awt.Dimension(45, 18));
        rdoRatificationNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoRatificationNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panRatification.add(rdoRatificationNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panShare.add(panRatification, gridBagConstraints);

        lblNumPatternFollowed.setText("Numbering Pattern to be Followed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 4, 4);
        panShare.add(lblNumPatternFollowed, gridBagConstraints);

        txtNumPatternFollowedPrefix.setMinimumSize(new java.awt.Dimension(50, 21));
        txtNumPatternFollowedPrefix.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panShare.add(txtNumPatternFollowedPrefix, gridBagConstraints);

        txtNumPatternFollowedSuffix.setMinimumSize(new java.awt.Dimension(50, 21));
        txtNumPatternFollowedSuffix.setPreferredSize(new java.awt.Dimension(55, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.insets = new java.awt.Insets(1, 48, 1, 3);
        panShare.add(txtNumPatternFollowedSuffix, gridBagConstraints);

        lblConsiderSalaryRecovery.setText("Do not Consider For Salary Recovery");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(lblConsiderSalaryRecovery, gridBagConstraints);

        chkConsiderSalaryRecovery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkConsiderSalaryRecoveryActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panShare.add(chkConsiderSalaryRecovery, gridBagConstraints);

        chkSubsidyForSCST.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSubsidyForSCSTActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panShare.add(chkSubsidyForSCST, gridBagConstraints);

        lblSubsidyForSCST2.setText("Subsidy Available For SC/ST Mem");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(lblSubsidyForSCST2, gridBagConstraints);

        lblCustomerShare.setText("Customer's Share");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(lblCustomerShare, gridBagConstraints);

        txtCustomerShare.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCustomerShare.setValidation(new CurrencyValidation(14,2));
        txtCustomerShare.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCustomerShareFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(txtCustomerShare, gridBagConstraints);

        lblGovernmentShare.setText("Governments's Share");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(lblGovernmentShare, gridBagConstraints);

        txtGovernmentShare.setMinimumSize(new java.awt.Dimension(100, 21));
        txtGovernmentShare.setValidation(new CurrencyValidation(14,2));
        txtGovernmentShare.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtGovernmentShareFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(txtGovernmentShare, gridBagConstraints);

        lblOutstandingReq.setText("Is Outstanding required");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(lblOutstandingReq, gridBagConstraints);

        chkOutstandingRequired.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkOutstandingRequiredActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panShare.add(chkOutstandingRequired, gridBagConstraints);

        chkShareCerti.setText("Share Certificate Print");
        chkShareCerti.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 80);
        panShare.add(chkShareCerti, gridBagConstraints);

        lblFullClosure.setText("Full Closure Required");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShare.add(lblFullClosure, gridBagConstraints);

        panFullClosure.setMinimumSize(new java.awt.Dimension(100, 16));
        panFullClosure.setPreferredSize(new java.awt.Dimension(140, 18));
        panFullClosure.setLayout(new java.awt.GridBagLayout());

        rdoFullClosureYes.setText("Yes");
        rdoFullClosureYes.setMaximumSize(new java.awt.Dimension(60, 18));
        rdoFullClosureYes.setMinimumSize(new java.awt.Dimension(60, 18));
        rdoFullClosureYes.setPreferredSize(new java.awt.Dimension(50, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panFullClosure.add(rdoFullClosureYes, gridBagConstraints);

        rdoFullClosureNo.setText("No");
        rdoFullClosureNo.setMaximumSize(new java.awt.Dimension(70, 18));
        rdoFullClosureNo.setMinimumSize(new java.awt.Dimension(70, 18));
        rdoFullClosureNo.setPreferredSize(new java.awt.Dimension(45, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panFullClosure.add(rdoFullClosureNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panShare.add(panFullClosure, gridBagConstraints);

        chkDRF.setText("DRF Allowed");
        chkDRF.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panShare.add(chkDRF, gridBagConstraints);

        tabShareProduct.addTab("Share", panShare);

        panBorrowing.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        panBorrowing.setMinimumSize(new java.awt.Dimension(764, 350));
        panBorrowing.setPreferredSize(new java.awt.Dimension(818, 350));
        panBorrowing.setLayout(new java.awt.GridBagLayout());

        panLoan.setBorder(javax.swing.BorderFactory.createTitledBorder("Share Link To Borrowing"));
        panLoan.setMinimumSize(new java.awt.Dimension(840, 201));
        panLoan.setLayout(new java.awt.GridBagLayout());

        panTblLoanType.setLayout(new java.awt.GridBagLayout());

        srpLoanType.setMinimumSize(new java.awt.Dimension(400, 100));

        tblLoanType.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Loan Type", "Borrower Share %", "Surity Share %"
            }
        ));
        tblLoanType.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblLoanTypeMousePressed(evt);
            }
        });
        srpLoanType.setViewportView(tblLoanType);

        panTblLoanType.add(srpLoanType, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panLoan.add(panTblLoanType, gridBagConstraints);

        panLoanOperations.setLayout(new java.awt.GridBagLayout());

        btnTabNew.setText("New");
        btnTabNew.setEnabled(false);
        btnTabNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTabNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanOperations.add(btnTabNew, gridBagConstraints);

        btnTabSave.setText("Save");
        btnTabSave.setEnabled(false);
        btnTabSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTabSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanOperations.add(btnTabSave, gridBagConstraints);

        btnTabDelete.setText("Delete");
        btnTabDelete.setEnabled(false);
        btnTabDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTabDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanOperations.add(btnTabDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panLoan.add(panLoanOperations, gridBagConstraints);

        panLoanType.setMinimumSize(new java.awt.Dimension(420, 175));
        panLoanType.setPreferredSize(new java.awt.Dimension(420, 175));
        panLoanType.setLayout(new java.awt.GridBagLayout());

        lblLoanType.setText("Loan Category Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanType.add(lblLoanType, gridBagConstraints);

        cboLoanType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboLoanType.setPopupWidth(170);
        cboLoanType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLoanTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanType.add(cboLoanType, gridBagConstraints);

        lblPercentageLoanAmount.setText("(% of Loan Amount)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panLoanType.add(lblPercentageLoanAmount, gridBagConstraints);

        txtLoanAvailingLimit.setMinimumSize(new java.awt.Dimension(50, 21));
        txtLoanAvailingLimit.setPreferredSize(new java.awt.Dimension(50, 21));
        txtLoanAvailingLimit.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanType.add(txtLoanAvailingLimit, gridBagConstraints);

        lblPercentageLoanAmt.setText("(% of Loan Amount)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 48, 4, 4);
        panLoanType.add(lblPercentageLoanAmt, gridBagConstraints);

        txtMaxLoanLimit.setMinimumSize(new java.awt.Dimension(50, 21));
        txtMaxLoanLimit.setPreferredSize(new java.awt.Dimension(50, 21));
        txtMaxLoanLimit.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanType.add(txtMaxLoanLimit, gridBagConstraints);

        lblShareSubscriptionBorrower.setText("Share Subscription By Borrower");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 0, 4);
        panLoanType.add(lblShareSubscriptionBorrower, gridBagConstraints);

        lblShareSubscriptionSurity.setText("Share Subscription By Surety");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 106, 0, 4);
        panLoanType.add(lblShareSubscriptionSurity, gridBagConstraints);

        lblMaxLoanAmt.setText("Max.Loan Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 48, 0, 4);
        panLoanType.add(lblMaxLoanAmt, gridBagConstraints);

        txtMaxLoanAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMaxLoanAmt.setNextFocusableComponent(cboDivAppFrequency);
        txtMaxLoanAmt.setValidation(new PercentageValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanType.add(txtMaxLoanAmt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        panLoan.add(panLoanType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBorrowing.add(panLoan, gridBagConstraints);

        panDivident.setBorder(javax.swing.BorderFactory.createTitledBorder("Share Link To Dividend"));
        panDivident.setMaximumSize(new java.awt.Dimension(691, 154));
        panDivident.setMinimumSize(new java.awt.Dimension(691, 154));
        panDivident.setPreferredSize(new java.awt.Dimension(691, 154));
        panDivident.setLayout(new java.awt.GridBagLayout());

        lblCalculatedDate.setText("Div Paid Up To");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDivident.add(lblCalculatedDate, gridBagConstraints);

        tdtCalculatedDate.setNextFocusableComponent(tdtDivRunDate);
        tdtCalculatedDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtCalculatedDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDivident.add(tdtCalculatedDate, gridBagConstraints);

        lblDivRunDate.setText("Div Run Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 4);
        panDivident.add(lblDivRunDate, gridBagConstraints);

        tdtDivRunDate.setNextFocusableComponent(txtUnclaimedDivPeriod);
        tdtDivRunDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tdtDivRunDateFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtDivRunDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDivident.add(tdtDivRunDate, gridBagConstraints);

        lblDividentPercentage.setText("Percentage of Dividend");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDivident.add(lblDividentPercentage, gridBagConstraints);

        txtDividentPercentage.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDividentPercentage.setNextFocusableComponent(cboDivAppFrequency);
        txtDividentPercentage.setValidation(new PercentageValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDivident.add(txtDividentPercentage, gridBagConstraints);

        lblDivCalcFrequency.setText("Dividend Calculation Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDivident.add(lblDivCalcFrequency, gridBagConstraints);

        cboDivCalcFrequency.setMinimumSize(new java.awt.Dimension(100, 21));
        cboDivCalcFrequency.setNextFocusableComponent(tdtCalculatedDate);
        cboDivCalcFrequency.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboDivCalcFrequencyActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDivident.add(cboDivCalcFrequency, gridBagConstraints);

        lblDivAppFrequency.setText("Dividend Application Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDivident.add(lblDivAppFrequency, gridBagConstraints);

        cboDivAppFrequency.setMinimumSize(new java.awt.Dimension(100, 21));
        cboDivAppFrequency.setNextFocusableComponent(cboDivCalcFrequency);
        cboDivAppFrequency.setPopupWidth(170);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDivident.add(cboDivAppFrequency, gridBagConstraints);

        lblLastuncaimedUpTo.setText("Last Unclaimed Div Transfer Up To");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 4);
        panDivident.add(lblLastuncaimedUpTo, gridBagConstraints);

        tdtLastuncaimedUpTo.setNextFocusableComponent(txtUnclaimedDivPeriod);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDivident.add(tdtLastuncaimedUpTo, gridBagConstraints);

        lblLastuncaimedRunDate.setText("Unclaimed Div Run Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 4);
        panDivident.add(lblLastuncaimedRunDate, gridBagConstraints);

        tdtLastuncaimedRunDate.setNextFocusableComponent(txtUnclaimedDivPeriod);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDivident.add(tdtLastuncaimedRunDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panBorrowing.add(panDivident, gridBagConstraints);

        panDividentNew.setBorder(javax.swing.BorderFactory.createTitledBorder("Share Link To Dividend"));
        panDividentNew.setMaximumSize(new java.awt.Dimension(691, 154));
        panDividentNew.setMinimumSize(new java.awt.Dimension(691, 154));
        panDividentNew.setPreferredSize(new java.awt.Dimension(691, 154));
        panDividentNew.setLayout(new java.awt.GridBagLayout());

        lblMinDividendAmount.setText("Minimum Dividend Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDividentNew.add(lblMinDividendAmount, gridBagConstraints);

        txtMinDividendAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMinDividendAmount.setNextFocusableComponent(cboDivAppFrequency);
        txtMinDividendAmount.setValidation(new PercentageValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDividentNew.add(txtMinDividendAmount, gridBagConstraints);

        lblDivCalcFrequencyNew.setText("Dividend Calculation Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDividentNew.add(lblDivCalcFrequencyNew, gridBagConstraints);

        cboDivCalcFrequencyNew.setMinimumSize(new java.awt.Dimension(100, 21));
        cboDivCalcFrequencyNew.setNextFocusableComponent(tdtCalculatedDate);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDividentNew.add(cboDivCalcFrequencyNew, gridBagConstraints);

        lblDivCalcType.setText("Dividend Calc Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDividentNew.add(lblDivCalcType, gridBagConstraints);

        cboDivCalcType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboDivCalcType.setNextFocusableComponent(cboDivCalcFrequency);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDividentNew.add(cboDivCalcType, gridBagConstraints);

        lblDividendRounding.setText("Rounding");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 4);
        panDividentNew.add(lblDividendRounding, gridBagConstraints);

        lblUnClaimedDivPeriod.setText("Unclaimed Dividend Period Limit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 4);
        panDividentNew.add(lblUnClaimedDivPeriod, gridBagConstraints);

        panUInClaimedDivident.setLayout(new java.awt.GridBagLayout());

        cboUnclaimedDivPeriod.setMinimumSize(new java.awt.Dimension(100, 21));
        cboUnclaimedDivPeriod.setPreferredSize(new java.awt.Dimension(70, 21));
        cboUnclaimedDivPeriod.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboUnclaimedDivPeriodFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panUInClaimedDivident.add(cboUnclaimedDivPeriod, gridBagConstraints);

        txtUnclaimedDivPeriod.setMinimumSize(new java.awt.Dimension(100, 21));
        txtUnclaimedDivPeriod.setNextFocusableComponent(cboUnclaimedDivPeriod);
        txtUnclaimedDivPeriod.setPreferredSize(new java.awt.Dimension(50, 21));
        txtUnclaimedDivPeriod.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panUInClaimedDivident.add(txtUnclaimedDivPeriod, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        panDividentNew.add(panUInClaimedDivident, gridBagConstraints);

        cboDividendRounding.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDividentNew.add(cboDividendRounding, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panBorrowing.add(panDividentNew, gridBagConstraints);

        tabShareProduct.addTab("Borrowing And Dividend\n", panBorrowing);

        panAccountDetails.setLayout(new java.awt.GridBagLayout());

        panAcctDet.setBorder(javax.swing.BorderFactory.createTitledBorder("Account Details"));
        panAcctDet.setMinimumSize(new java.awt.Dimension(630, 340));
        panAcctDet.setPreferredSize(new java.awt.Dimension(630, 340));
        panAcctDet.setLayout(new java.awt.GridBagLayout());

        panShareSuspAccount.setMinimumSize(new java.awt.Dimension(330, 29));
        panShareSuspAccount.setPreferredSize(new java.awt.Dimension(330, 29));
        panShareSuspAccount.setLayout(new java.awt.GridBagLayout());

        txtShareSuspAccount.setEditable(false);
        txtShareSuspAccount.setAllowAll(true);
        txtShareSuspAccount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtShareSuspAccount.setNextFocusableComponent(btnShareSuspAccount);
        txtShareSuspAccount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtShareSuspAccountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShareSuspAccount.add(txtShareSuspAccount, gridBagConstraints);

        btnShareSuspAccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnShareSuspAccount.setMinimumSize(new java.awt.Dimension(21, 21));
        btnShareSuspAccount.setNextFocusableComponent(txtFaceValue);
        btnShareSuspAccount.setPreferredSize(new java.awt.Dimension(21, 21));
        btnShareSuspAccount.setEnabled(false);
        btnShareSuspAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShareSuspAccountActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panShareSuspAccount.add(btnShareSuspAccount, gridBagConstraints);

        txtShareSuspAccountDesc.setMinimumSize(new java.awt.Dimension(190, 21));
        txtShareSuspAccountDesc.setPreferredSize(new java.awt.Dimension(190, 21));
        panShareSuspAccount.add(txtShareSuspAccountDesc, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panAcctDet.add(panShareSuspAccount, gridBagConstraints);

        lblShareSuspAccount.setText("Share Suspense Account");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctDet.add(lblShareSuspAccount, gridBagConstraints);

        panShareAccount.setMinimumSize(new java.awt.Dimension(330, 29));
        panShareAccount.setPreferredSize(new java.awt.Dimension(330, 29));
        panShareAccount.setLayout(new java.awt.GridBagLayout());

        txtShareAccount.setEditable(false);
        txtShareAccount.setAllowAll(true);
        txtShareAccount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtShareAccount.setNextFocusableComponent(btnShareSuspAccount);
        txtShareAccount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtShareAccountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShareAccount.add(txtShareAccount, gridBagConstraints);

        btnShareAccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnShareAccount.setMinimumSize(new java.awt.Dimension(21, 21));
        btnShareAccount.setNextFocusableComponent(txtFaceValue);
        btnShareAccount.setPreferredSize(new java.awt.Dimension(21, 21));
        btnShareAccount.setEnabled(false);
        btnShareAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShareAccountActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panShareAccount.add(btnShareAccount, gridBagConstraints);

        txtShareAccountDesc.setMinimumSize(new java.awt.Dimension(190, 21));
        txtShareAccountDesc.setPreferredSize(new java.awt.Dimension(190, 21));
        panShareAccount.add(txtShareAccountDesc, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panAcctDet.add(panShareAccount, gridBagConstraints);

        lblShareAccount.setText("Share Account");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctDet.add(lblShareAccount, gridBagConstraints);

        panMemFeeAcct.setMinimumSize(new java.awt.Dimension(330, 29));
        panMemFeeAcct.setPreferredSize(new java.awt.Dimension(330, 29));
        panMemFeeAcct.setLayout(new java.awt.GridBagLayout());

        txtMemFeeAcct.setEditable(false);
        txtMemFeeAcct.setAllowAll(true);
        txtMemFeeAcct.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMemFeeAcct.setNextFocusableComponent(btnShareSuspAccount);
        txtMemFeeAcct.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMemFeeAcctFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMemFeeAcct.add(txtMemFeeAcct, gridBagConstraints);

        btnMemFeeAcct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnMemFeeAcct.setMinimumSize(new java.awt.Dimension(21, 21));
        btnMemFeeAcct.setNextFocusableComponent(txtFaceValue);
        btnMemFeeAcct.setPreferredSize(new java.awt.Dimension(21, 21));
        btnMemFeeAcct.setEnabled(false);
        btnMemFeeAcct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMemFeeAcctActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panMemFeeAcct.add(btnMemFeeAcct, gridBagConstraints);

        txtMemFeeAcctDesc.setMinimumSize(new java.awt.Dimension(190, 21));
        txtMemFeeAcctDesc.setPreferredSize(new java.awt.Dimension(190, 21));
        panMemFeeAcct.add(txtMemFeeAcctDesc, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panAcctDet.add(panMemFeeAcct, gridBagConstraints);

        lblMemFeeAcct.setText("Membership Fee Account");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctDet.add(lblMemFeeAcct, gridBagConstraints);

        panApplFeeAcct.setMinimumSize(new java.awt.Dimension(330, 29));
        panApplFeeAcct.setPreferredSize(new java.awt.Dimension(330, 29));
        panApplFeeAcct.setLayout(new java.awt.GridBagLayout());

        txtApplFeeAcct.setEditable(false);
        txtApplFeeAcct.setAllowAll(true);
        txtApplFeeAcct.setMinimumSize(new java.awt.Dimension(100, 21));
        txtApplFeeAcct.setNextFocusableComponent(btnShareSuspAccount);
        txtApplFeeAcct.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtApplFeeAcctFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panApplFeeAcct.add(txtApplFeeAcct, gridBagConstraints);

        btnApplFeeAcct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnApplFeeAcct.setMinimumSize(new java.awt.Dimension(21, 21));
        btnApplFeeAcct.setNextFocusableComponent(txtFaceValue);
        btnApplFeeAcct.setPreferredSize(new java.awt.Dimension(21, 21));
        btnApplFeeAcct.setEnabled(false);
        btnApplFeeAcct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApplFeeAcctActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panApplFeeAcct.add(btnApplFeeAcct, gridBagConstraints);

        txtApplFeeAcctDesc.setMinimumSize(new java.awt.Dimension(190, 21));
        txtApplFeeAcctDesc.setPreferredSize(new java.awt.Dimension(190, 21));
        panApplFeeAcct.add(txtApplFeeAcctDesc, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panAcctDet.add(panApplFeeAcct, gridBagConstraints);

        lblApplFeeAcct.setText("Application Fee Account");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctDet.add(lblApplFeeAcct, gridBagConstraints);

        panShareFeeAcct.setMinimumSize(new java.awt.Dimension(330, 29));
        panShareFeeAcct.setPreferredSize(new java.awt.Dimension(330, 29));
        panShareFeeAcct.setLayout(new java.awt.GridBagLayout());

        txtShareFeeAcct.setEditable(false);
        txtShareFeeAcct.setAllowAll(true);
        txtShareFeeAcct.setMinimumSize(new java.awt.Dimension(100, 21));
        txtShareFeeAcct.setNextFocusableComponent(btnShareSuspAccount);
        txtShareFeeAcct.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtShareFeeAcctFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShareFeeAcct.add(txtShareFeeAcct, gridBagConstraints);

        btnShareFeeAcct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnShareFeeAcct.setMinimumSize(new java.awt.Dimension(21, 21));
        btnShareFeeAcct.setNextFocusableComponent(txtFaceValue);
        btnShareFeeAcct.setPreferredSize(new java.awt.Dimension(21, 21));
        btnShareFeeAcct.setEnabled(false);
        btnShareFeeAcct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShareFeeAcctActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panShareFeeAcct.add(btnShareFeeAcct, gridBagConstraints);

        txtShareFeeAcctDesc.setMinimumSize(new java.awt.Dimension(190, 21));
        txtShareFeeAcctDesc.setPreferredSize(new java.awt.Dimension(190, 21));
        panShareFeeAcct.add(txtShareFeeAcctDesc, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panAcctDet.add(panShareFeeAcct, gridBagConstraints);

        lblShareFeeAcct.setText("Share Fee Account");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctDet.add(lblShareFeeAcct, gridBagConstraints);

        lblSharedivPaidAcct.setText("Share Dividend  Paid Account");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctDet.add(lblSharedivPaidAcct, gridBagConstraints);

        panDivPayableAcHd.setMinimumSize(new java.awt.Dimension(330, 29));
        panDivPayableAcHd.setPreferredSize(new java.awt.Dimension(330, 29));
        panDivPayableAcHd.setLayout(new java.awt.GridBagLayout());

        txtDivPayableAcct.setEditable(false);
        txtDivPayableAcct.setAllowAll(true);
        txtDivPayableAcct.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDivPayableAcct.setNextFocusableComponent(btnShareSuspAccount);
        txtDivPayableAcct.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDivPayableAcctFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDivPayableAcHd.add(txtDivPayableAcct, gridBagConstraints);

        btnDivPayableAcct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDivPayableAcct.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDivPayableAcct.setNextFocusableComponent(txtFaceValue);
        btnDivPayableAcct.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDivPayableAcct.setEnabled(false);
        btnDivPayableAcct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDivPayableAcctActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panDivPayableAcHd.add(btnDivPayableAcct, gridBagConstraints);

        txtDivPayableAcctDesc.setMinimumSize(new java.awt.Dimension(190, 21));
        txtDivPayableAcctDesc.setPreferredSize(new java.awt.Dimension(190, 21));
        panDivPayableAcHd.add(txtDivPayableAcctDesc, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panAcctDet.add(panDivPayableAcHd, gridBagConstraints);

        lblSharedivPayableAcct.setText("Share Dividend  Payable Account");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctDet.add(lblSharedivPayableAcct, gridBagConstraints);

        panDivPaidAcHd.setMinimumSize(new java.awt.Dimension(330, 29));
        panDivPaidAcHd.setPreferredSize(new java.awt.Dimension(330, 29));
        panDivPaidAcHd.setLayout(new java.awt.GridBagLayout());

        txtDivPaidAcct.setEditable(false);
        txtDivPaidAcct.setAllowAll(true);
        txtDivPaidAcct.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDivPaidAcct.setNextFocusableComponent(btnShareSuspAccount);
        txtDivPaidAcct.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDivPaidAcctFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDivPaidAcHd.add(txtDivPaidAcct, gridBagConstraints);

        btnDivPaidAcct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDivPaidAcct.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDivPaidAcct.setNextFocusableComponent(txtFaceValue);
        btnDivPaidAcct.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDivPaidAcct.setEnabled(false);
        btnDivPaidAcct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDivPaidAcctActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panDivPaidAcHd.add(btnDivPaidAcct, gridBagConstraints);

        txtDivPaidAcctDesc.setMinimumSize(new java.awt.Dimension(190, 21));
        txtDivPaidAcctDesc.setPreferredSize(new java.awt.Dimension(190, 21));
        panDivPaidAcHd.add(txtDivPaidAcctDesc, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panAcctDet.add(panDivPaidAcHd, gridBagConstraints);

        lblUnClaimedDivTransferAcHd.setText("UnClaimed Dividend Transfer Account");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctDet.add(lblUnClaimedDivTransferAcHd, gridBagConstraints);

        panUnClaimedDivTransferAcHd.setMinimumSize(new java.awt.Dimension(330, 29));
        panUnClaimedDivTransferAcHd.setPreferredSize(new java.awt.Dimension(330, 29));
        panUnClaimedDivTransferAcHd.setLayout(new java.awt.GridBagLayout());

        txtUnClaimedDivTransferAcHd.setEditable(false);
        txtUnClaimedDivTransferAcHd.setAllowAll(true);
        txtUnClaimedDivTransferAcHd.setMinimumSize(new java.awt.Dimension(100, 21));
        txtUnClaimedDivTransferAcHd.setNextFocusableComponent(btnShareSuspAccount);
        txtUnClaimedDivTransferAcHd.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtUnClaimedDivTransferAcHdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panUnClaimedDivTransferAcHd.add(txtUnClaimedDivTransferAcHd, gridBagConstraints);

        btnUnClaimedDivTransferAcHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnUnClaimedDivTransferAcHd.setMinimumSize(new java.awt.Dimension(21, 21));
        btnUnClaimedDivTransferAcHd.setNextFocusableComponent(txtFaceValue);
        btnUnClaimedDivTransferAcHd.setPreferredSize(new java.awt.Dimension(21, 21));
        btnUnClaimedDivTransferAcHd.setEnabled(false);
        btnUnClaimedDivTransferAcHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnClaimedDivTransferAcHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panUnClaimedDivTransferAcHd.add(btnUnClaimedDivTransferAcHd, gridBagConstraints);

        txtUnClaimedDivTransferAcHdDesc.setMinimumSize(new java.awt.Dimension(190, 21));
        txtUnClaimedDivTransferAcHdDesc.setPreferredSize(new java.awt.Dimension(190, 21));
        txtUnClaimedDivTransferAcHdDesc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtUnClaimedDivTransferAcHdDescFocusLost(evt);
            }
        });
        panUnClaimedDivTransferAcHd.add(txtUnClaimedDivTransferAcHdDesc, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panAcctDet.add(panUnClaimedDivTransferAcHd, gridBagConstraints);

        lblncomeAccountHead.setText("Income Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctDet.add(lblncomeAccountHead, gridBagConstraints);

        panIncomeAccountHead.setMinimumSize(new java.awt.Dimension(330, 29));
        panIncomeAccountHead.setPreferredSize(new java.awt.Dimension(330, 29));
        panIncomeAccountHead.setLayout(new java.awt.GridBagLayout());

        txtIncomeAccountHead.setEditable(false);
        txtIncomeAccountHead.setAllowAll(true);
        txtIncomeAccountHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtIncomeAccountHead.setNextFocusableComponent(btnShareSuspAccount);
        txtIncomeAccountHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIncomeAccountHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIncomeAccountHead.add(txtIncomeAccountHead, gridBagConstraints);

        btnIncomeAccountHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnIncomeAccountHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnIncomeAccountHead.setNextFocusableComponent(txtFaceValue);
        btnIncomeAccountHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnIncomeAccountHead.setEnabled(false);
        btnIncomeAccountHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIncomeAccountHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panIncomeAccountHead.add(btnIncomeAccountHead, gridBagConstraints);

        txtIncomeAccountHeadDesc.setMinimumSize(new java.awt.Dimension(190, 21));
        txtIncomeAccountHeadDesc.setPreferredSize(new java.awt.Dimension(190, 21));
        panIncomeAccountHead.add(txtIncomeAccountHeadDesc, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panAcctDet.add(panIncomeAccountHead, gridBagConstraints);

        panGovtSubsidyAccountHead.setLayout(new java.awt.GridBagLayout());

        txtGovtSubsidyAccountHead.setEditable(false);
        txtGovtSubsidyAccountHead.setAllowAll(true);
        txtGovtSubsidyAccountHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtGovtSubsidyAccountHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtGovtSubsidyAccountHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGovtSubsidyAccountHead.add(txtGovtSubsidyAccountHead, gridBagConstraints);

        btnGovtSubsidyAccountHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnGovtSubsidyAccountHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnGovtSubsidyAccountHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnGovtSubsidyAccountHead.setEnabled(false);
        btnGovtSubsidyAccountHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGovtSubsidyAccountHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panGovtSubsidyAccountHead.add(btnGovtSubsidyAccountHead, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        panAcctDet.add(panGovtSubsidyAccountHead, gridBagConstraints);

        lbGovtSubsidyAccountHead.setText("Govt. Subsidy Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctDet.add(lbGovtSubsidyAccountHead, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(panAcctDet, gridBagConstraints);

        tabShareProduct.addTab("Account Details", panAccountDetails);

        panGldRenewal.setMinimumSize(new java.awt.Dimension(250, 300));
        panGldRenewal.setPreferredSize(new java.awt.Dimension(250, 300));
        panGldRenewal.setLayout(new java.awt.GridBagLayout());

        lblCaste.setText("Caste");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGldRenewal.add(lblCaste, gridBagConstraints);

        lblReligion.setText("Religion");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGldRenewal.add(lblReligion, gridBagConstraints);

        lblWardNo.setText("Ward No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGldRenewal.add(lblWardNo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGldRenewal.add(chkCaste, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGldRenewal.add(chkReligion, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGldRenewal.add(chkWardNo, gridBagConstraints);

        lblDob.setText("DOB");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGldRenewal.add(lblDob, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGldRenewal.add(chkDesam, gridBagConstraints);

        lblArea.setText("Area");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGldRenewal.add(lblArea, gridBagConstraints);

        lblStreet.setText("Street");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGldRenewal.add(lblStreet, gridBagConstraints);

        lblCity.setText("City");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGldRenewal.add(lblCity, gridBagConstraints);

        lblPincode.setText("Pincode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGldRenewal.add(lblPincode, gridBagConstraints);

        lblTaluk.setText("Taluk");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGldRenewal.add(lblTaluk, gridBagConstraints);

        lblVillage.setText("Village");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGldRenewal.add(lblVillage, gridBagConstraints);

        lblDesam.setText("Desam");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGldRenewal.add(lblDesam, gridBagConstraints);

        lblAmsam.setText("Amsam");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGldRenewal.add(lblAmsam, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGldRenewal.add(chkVillage, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGldRenewal.add(chkTaluk, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGldRenewal.add(chkPinCode, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGldRenewal.add(chkCity, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGldRenewal.add(chkStreet, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGldRenewal.add(chkArea, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGldRenewal.add(chkDob, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGldRenewal.add(chkAmsam, gridBagConstraints);

        jPanel1.add(panGldRenewal);

        tabShareProduct.addTab("Member Data", jPanel1);

        panPensionScheme.add(panPension);

        panDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Pension Details"));
        panDetails.setPreferredSize(new java.awt.Dimension(450, 400));
        panDetails.setLayout(new java.awt.GridBagLayout());

        panAccountDetails1.setBorder(javax.swing.BorderFactory.createTitledBorder("Debit Account Details"));
        panAccountDetails1.setMinimumSize(new java.awt.Dimension(300, 130));
        panAccountDetails1.setPreferredSize(new java.awt.Dimension(300, 130));
        panAccountDetails1.setLayout(new java.awt.GridBagLayout());

        cboProdTypeCr.setToolTipText("Debit Account Product");
        cboProdTypeCr.setMinimumSize(new java.awt.Dimension(150, 21));
        cboProdTypeCr.setNextFocusableComponent(txtTransProductId);
        cboProdTypeCr.setPopupWidth(160);
        cboProdTypeCr.setPreferredSize(new java.awt.Dimension(150, 21));
        cboProdTypeCr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdTypeCrActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccountDetails1.add(cboProdTypeCr, gridBagConstraints);

        lblProdTypeCr.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccountDetails1.add(lblProdTypeCr, gridBagConstraints);

        lblAccNo.setText("Account No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccountDetails1.add(lblAccNo, gridBagConstraints);

        lblProdIdCr.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccountDetails1.add(lblProdIdCr, gridBagConstraints);

        panAccHd.setMinimumSize(new java.awt.Dimension(121, 21));
        panAccHd.setPreferredSize(new java.awt.Dimension(21, 200));
        panAccHd.setLayout(new java.awt.GridBagLayout());

        txtAcctNo.setToolTipText("Debit Account Number / Debit Account Head");
        txtAcctNo.setAllowAll(true);
        txtAcctNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAcctNo.setNextFocusableComponent(btnAccNo);
        txtAcctNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAcctNoActionPerformed(evt);
            }
        });
        txtAcctNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAcctNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccHd.add(txtAcctNo, gridBagConstraints);

        btnAccNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccNo.setToolTipText("Account Number");
        btnAccNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccNoActionPerformed(evt);
            }
        });
        panAccHd.add(btnAccNo, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 2);
        panAccountDetails1.add(panAccHd, gridBagConstraints);

        panAcctNo.setMinimumSize(new java.awt.Dimension(121, 21));
        panAcctNo.setPreferredSize(new java.awt.Dimension(21, 200));
        panAcctNo.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, -8, 1, 0);
        panAccountDetails1.add(panAcctNo, gridBagConstraints);

        panDebitAccHead.setLayout(new java.awt.GridBagLayout());

        txtTransProductId.setToolTipText("Debit Product");
        txtTransProductId.setAllowAll(true);
        txtTransProductId.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTransProductId.setNextFocusableComponent(btnTransProductId);
        txtTransProductId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTransProductIdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDebitAccHead.add(txtTransProductId, gridBagConstraints);

        btnTransProductId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnTransProductId.setNextFocusableComponent(txtAcctNo);
        btnTransProductId.setPreferredSize(new java.awt.Dimension(21, 21));
        btnTransProductId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransProductIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDebitAccHead.add(btnTransProductId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(0, -28, 0, 0);
        panAccountDetails1.add(panDebitAccHead, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(16, 2, 3, 2);
        panDetails.add(panAccountDetails1, gridBagConstraints);

        txtPensionAge.setToolTipText("Pension Age");
        txtPensionAge.setAllowNumber(true);
        txtPensionAge.setNextFocusableComponent(txtShareDuration);
        txtPensionAge.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPensionAgeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 150);
        panDetails.add(txtPensionAge, gridBagConstraints);

        txtShareDuration.setToolTipText("Share Taken Upto");
        txtShareDuration.setAllowNumber(true);
        txtShareDuration.setNextFocusableComponent(txtMinPension);
        txtShareDuration.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtShareDurationFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 150);
        panDetails.add(txtShareDuration, gridBagConstraints);

        txtMinPension.setToolTipText("Mininum Pension Amount");
        txtMinPension.setNextFocusableComponent(cboProdTypeCr);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 1, 0, 150);
        panDetails.add(txtMinPension, gridBagConstraints);

        lblPAge.setText("Pension Age");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 60, 0, 3);
        panDetails.add(lblPAge, gridBagConstraints);

        lblPeriod.setText("Share Run Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 63, 0, 4);
        panDetails.add(lblPeriod, gridBagConstraints);

        lblAmount.setText("Min Pension Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 60, 0, 4);
        panDetails.add(lblAmount, gridBagConstraints);

        panPensionScheme.add(panDetails);

        tabShareProduct.addTab("Pension Scheme", panPensionScheme);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panShareProduct.add(tabShareProduct, gridBagConstraints);

        getContentPane().add(panShareProduct, java.awt.BorderLayout.CENTER);

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
        tbrShareProduct.add(btnView);

        lblSpace5.setText("     ");
        tbrShareProduct.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnNew);

        lblSpace7.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace7.setText("     ");
        lblSpace7.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace7.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace7.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace7);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnEdit);

        lblSpace8.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace8.setText("     ");
        lblSpace8.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace8.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace8.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace8);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnDelete);

        lblSpace2.setText("     ");
        tbrShareProduct.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnSave);

        lblSpace13.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace13.setText("     ");
        lblSpace13.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace13.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace13.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace13);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnCancel);

        lblSpace3.setText("     ");
        tbrShareProduct.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnAuthorize);

        lblSpace14.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace14.setText("     ");
        lblSpace14.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace14.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace14.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace14);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnException);

        lblSpace15.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace15.setText("     ");
        lblSpace15.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace15.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace15.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace15);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnReject);

        lblSpace4.setText("     ");
        tbrShareProduct.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrShareProduct.add(btnPrint);

        lblSpace16.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace16.setText("     ");
        lblSpace16.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace16.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace16.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace16);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnClose);

        getContentPane().add(tbrShareProduct, java.awt.BorderLayout.NORTH);

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

        mbrShareProduct.setName("mbrCustomer"); // NOI18N

        mnuProcess.setText("Process");
        mnuProcess.setName("mnuProcess"); // NOI18N

        mitNew.setText("New");
        mitNew.setName("mitNew"); // NOI18N
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.setName("mitEdit"); // NOI18N
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.setName("mitDelete"); // NOI18N
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);

        sptNew.setName("sptNew"); // NOI18N
        mnuProcess.add(sptNew);

        mitSave.setText("Save");
        mitSave.setName("mitSave"); // NOI18N
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.setName("mitCancel"); // NOI18N
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);

        sptSave.setName("sptSave"); // NOI18N
        mnuProcess.add(sptSave);

        mitPrint.setText("Print");
        mitPrint.setName("mitPrint"); // NOI18N
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.setName("mitClose"); // NOI18N
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrShareProduct.add(mnuProcess);

        setJMenuBar(mbrShareProduct);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtUnClaimedDivTransferAcHdDescFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUnClaimedDivTransferAcHdDescFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUnClaimedDivTransferAcHdDescFocusLost

    private void txtGovtSubsidyAccountHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGovtSubsidyAccountHeadFocusLost
        // TODO add your handling code here:
        if(!txtGovtSubsidyAccountHead.getText().equals(""))
        {
            btnGovtSubsidyAccountHead.setToolTipText(getAccHdDesc(txtGovtSubsidyAccountHead.getText()));
        }
    }//GEN-LAST:event_txtGovtSubsidyAccountHeadFocusLost

    private void txtIncomeAccountHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIncomeAccountHeadFocusLost
        // TODO add your handling code here:
        if(!txtIncomeAccountHead.getText().equals(""))
        {
            txtIncomeAccountHeadDesc.setText(getAccHdDesc(txtIncomeAccountHead.getText()));
        }
    }//GEN-LAST:event_txtIncomeAccountHeadFocusLost

    private void txtUnClaimedDivTransferAcHdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUnClaimedDivTransferAcHdFocusLost
        // TODO add your handling code here:
        if(!txtUnClaimedDivTransferAcHd.getText().equals(""))
        {
            txtUnClaimedDivTransferAcHdDesc.setText(getAccHdDesc(txtUnClaimedDivTransferAcHd.getText()));
        }
    }//GEN-LAST:event_txtUnClaimedDivTransferAcHdFocusLost

    private void txtDivPaidAcctFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDivPaidAcctFocusLost
        // TODO add your handling code here:
        if(!txtDivPaidAcct.getText().equals(""))
        {
            txtDivPaidAcctDesc.setText(getAccHdDesc(txtDivPaidAcct.getText()));
        }
    }//GEN-LAST:event_txtDivPaidAcctFocusLost

    private void txtDivPayableAcctFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDivPayableAcctFocusLost
        // TODO add your handling code here:
        if(!txtDivPayableAcct.getText().equals(""))
        {
            txtDivPayableAcctDesc.setText(getAccHdDesc(txtDivPayableAcct.getText()));
        }
    }//GEN-LAST:event_txtDivPayableAcctFocusLost

    private void txtShareFeeAcctFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtShareFeeAcctFocusLost
        // TODO add your handling code here:
        if(!txtShareFeeAcct.getText().equals(""))
        {
            txtShareFeeAcctDesc.setText(getAccHdDesc(txtShareFeeAcct.getText()));
            
        }
    }//GEN-LAST:event_txtShareFeeAcctFocusLost

    private void txtApplFeeAcctFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtApplFeeAcctFocusLost
        // TODO add your handling code here:
        if(!txtApplFeeAcct.getText().equals(""))
        {
            txtApplFeeAcctDesc.setText(getAccHdDesc(txtApplFeeAcct.getText()));
        }
    }//GEN-LAST:event_txtApplFeeAcctFocusLost

    private void txtMemFeeAcctFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMemFeeAcctFocusLost
        // TODO add your handling code here:
        if(!txtMemFeeAcct.getText().equals(""))
        {
            txtMemFeeAcctDesc.setText(getAccHdDesc(txtMemFeeAcct.getText()));
        }
    }//GEN-LAST:event_txtMemFeeAcctFocusLost

    private void txtShareAccountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtShareAccountFocusLost
        // TODO add your handling code here:
         if(!txtShareAccount.getText().equals(""))
        {
            txtShareAccountDesc.setText(getAccHdDesc(txtShareAccount.getText()));
        }
    }//GEN-LAST:event_txtShareAccountFocusLost

    private void txtShareSuspAccountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtShareSuspAccountFocusLost
        // TODO add your handling code here:
        if(!txtShareSuspAccount.getText().equals(""))
        {
            txtShareSuspAccountDesc.setText(getAccHdDesc(txtShareSuspAccount.getText()));
        }
    }//GEN-LAST:event_txtShareSuspAccountFocusLost

    private void txtAdmissionFeeMinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAdmissionFeeMinActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAdmissionFeeMinActionPerformed

    private void btnGovtSubsidyAccountHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGovtSubsidyAccountHeadActionPerformed
        // TODO add your handling code here:
        callView("GOVTSUBSIDYACCOUNTHEAD");
    }//GEN-LAST:event_btnGovtSubsidyAccountHeadActionPerformed

    private void txtFaceValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFaceValueFocusLost
        // TODO add your handling code here:
        txtCustomerShare.setText("");
        txtGovernmentShare.setText("");
    }//GEN-LAST:event_txtFaceValueFocusLost

    private void txtGovernmentShareFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGovernmentShareFocusLost
        // TODO add your handling code here:
        if(CommonUtil.convertObjToStr(txtGovernmentShare.getText()).length()>0){
            if(CommonUtil.convertObjToStr(txtFaceValue.getText()).length()>0){
                validateFaceValueSubsidy("GOVERNMENTS_SHARE");
            }else{
                ClientUtil.showAlertWindow("Kindly Enter The Face Value Before adding subsidy details!!");
                txtCustomerShare.setText("");
                txtGovernmentShare.setText("");
            }
        }
    }//GEN-LAST:event_txtGovernmentShareFocusLost

    private void txtCustomerShareFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCustomerShareFocusLost
        // TODO add your handling code here:
        if(CommonUtil.convertObjToStr(txtCustomerShare.getText()).length()>0){
            if(CommonUtil.convertObjToStr(txtFaceValue.getText()).length()>0){
                validateFaceValueSubsidy("CUSTOMERS_SHARE");
            }else{
                ClientUtil.showAlertWindow("Kindly Enter The Face Value Before adding subsidy details!!");
                txtCustomerShare.setText("");
                txtGovernmentShare.setText("");
            }
        }
    }//GEN-LAST:event_txtCustomerShareFocusLost

//added by nikhil for subsidy
    private void validateFaceValueSubsidy(String subsidyShare){
        double faceValue = CommonUtil.convertObjToDouble(txtFaceValue.getText()).doubleValue();
        double customersShare = 0.0;
        double governMentsShare = 0.0;
        if(subsidyShare.equals("CUSTOMERS_SHARE")){
            customersShare = CommonUtil.convertObjToDouble(txtCustomerShare.getText()).doubleValue();
            System.out.println("@#$@#$@#$customersShare"+customersShare);
            if(customersShare>faceValue){
               ClientUtil.showAlertWindow("Customer's Share cannot be greater than Face Value!!");
               txtCustomerShare.setText("");
               txtGovernmentShare.setText("");
            }else{
                governMentsShare = faceValue - customersShare;
                txtGovernmentShare.setText(String.valueOf(governMentsShare));
            }
        }else if(subsidyShare.equals("GOVERNMENTS_SHARE")){
            governMentsShare = CommonUtil.convertObjToDouble(txtGovernmentShare.getText()).doubleValue();
            System.out.println("#@$@$#@#$governMentsShare:"+governMentsShare);
            if(governMentsShare>faceValue){
               ClientUtil.showAlertWindow("Governments's Share cannot be greater than Face Value!!");
               txtCustomerShare.setText("");
               txtGovernmentShare.setText("");
            }else{
                customersShare  = faceValue - governMentsShare;
                txtCustomerShare.setText(String.valueOf(customersShare));
            }
        }
        
    }
    private void chkSubsidyForSCSTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSubsidyForSCSTActionPerformed
        // TODO add your handling code here:
        if(chkSubsidyForSCST.isSelected()){
            visibleSubsidyShare(true);
        }else{
            visibleSubsidyShare(false);
            txtCustomerShare.setText("");
            txtGovernmentShare.setText("");
        }
    }//GEN-LAST:event_chkSubsidyForSCSTActionPerformed

    private void visibleSubsidyShare(boolean flag){
        txtCustomerShare.setVisible(flag);
        txtGovernmentShare.setVisible(flag);
        lblCustomerShare.setVisible(flag);
        lblGovernmentShare.setVisible(flag);
        
    }
    private void btnIncomeAccountHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIncomeAccountHeadActionPerformed
      callView("INCOMEACCOUNTHEAD");
    }//GEN-LAST:event_btnIncomeAccountHeadActionPerformed

    private void rdoAdmissionFeeType_PercentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoAdmissionFeeType_PercentActionPerformed
        // TODO add your handling code here:
           txtAdmissionFee.setValidation(new NumericValidation(2,2));
        txtAdmissionFee.setText("");
        lblAdmissionFee.setText(resourceBundle.getString("lblAdmissionFeePercent"));
         txtAdmissionFeeMin.setEnabled(true);
         txtAdmissionFeeMax.setEnabled(true);
    }//GEN-LAST:event_rdoAdmissionFeeType_PercentActionPerformed

    private void rdoAdmissionFeeType_FixedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoAdmissionFeeType_FixedActionPerformed
        // TODO add your handling code here:
        txtAdmissionFee.setValidation(new CurrencyValidation(14,2));
        txtAdmissionFee.setText("");
        lblAdmissionFee.setText(resourceBundle.getString("lblAdmissionFeeFixed"));
        txtAdmissionFeeMin.setEnabled(false);
        txtAdmissionFeeMax.setEnabled(false);
        txtAdmissionFeeMin.setText("");
        txtAdmissionFeeMax.setText("");
        
    }//GEN-LAST:event_rdoAdmissionFeeType_FixedActionPerformed
    
    private void txtAdmissionFeeMaxFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAdmissionFeeMaxFocusLost
        // TODO add your handling code here:
        if(rdoAdmissionFeeType_Percent.isSelected()){
            if(txtAdmissionFeeMax.getText().length() > 0){
                double maxFeeAmt = CommonUtil.convertObjToDouble(txtAdmissionFeeMax.getText()).doubleValue();
                if(txtAdmissionFeeMin.getText().length() > 0){
                    double minFeeAmt = CommonUtil.convertObjToDouble(txtAdmissionFeeMin.getText()).doubleValue();
                    if(maxFeeAmt < minFeeAmt){
                        ClientUtil.showAlertWindow("Max fee Should be Greater than Min Fee!!");
                        txtAdmissionFeeMax.setText("");
                        txtAdmissionFeeMax.requestFocusInWindow();
                        
                    }
                }
            }
        }
    }//GEN-LAST:event_txtAdmissionFeeMaxFocusLost
    
    private void txtAdmissionFeeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAdmissionFeeFocusLost
        // TODO add your handling code here:
//        if(rdoAdmissionFeeType_Fixed.isSelected()){
//            if(txtAdmissionFee.getText().length() > 0){
//                double fixedAmt = CommonUtil.convertObjToDouble(txtAdmissionFee.getText()).doubleValue();
//                if(txtAdmissionFeeMin.getText().length() > 0){
//                    double minFeeAmt = CommonUtil.convertObjToDouble(txtAdmissionFeeMin.getText()).doubleValue();
//                    if(fixedAmt < minFeeAmt){
//                        ClientUtil.showAlertWindow("Addmission fee Should be Greater than minimum Amount!!");
//                        txtAdmissionFee.setText("");
//                        txtAdmissionFee.requestFocusInWindow();
//                    }
//                }
//                if(txtAdmissionFeeMax.getText().length() > 0){
//                    double maxFeeAmt = CommonUtil.convertObjToDouble(txtAdmissionFeeMax.getText()).doubleValue();
//                    if(fixedAmt > maxFeeAmt){
//                        ClientUtil.showAlertWindow("Addmission fee Should be Lesser than maximum Amount!!");
//                        txtAdmissionFee.setText("");
//                        txtAdmissionFee.requestFocusInWindow();
//                    }
//                }
//            }
//        }
    }//GEN-LAST:event_txtAdmissionFeeFocusLost
    
    private void txtAdmissionFeeMinFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAdmissionFeeMinFocusLost
        // TODO add your handling code here:
        if(rdoAdmissionFeeType_Percent.isSelected()){
            if(txtAdmissionFeeMin.getText().length() > 0){
                double minFeeAmt = CommonUtil.convertObjToDouble(txtAdmissionFeeMin.getText()).doubleValue();
                
                if(txtAdmissionFeeMax.getText().length() > 0){
                    double maxFeeAmt = CommonUtil.convertObjToDouble(txtAdmissionFeeMax.getText()).doubleValue();
                    if(minFeeAmt > maxFeeAmt){
                        ClientUtil.showAlertWindow("Min fee Should be lesser than max Fee!!");
                        txtAdmissionFeeMin.setText("");
                        txtAdmissionFeeMin.requestFocusInWindow();
                        
                    }
                }
            }
        }
    }//GEN-LAST:event_txtAdmissionFeeMinFocusLost
    
    private void rdoRatificationNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoRatificationNoActionPerformed
        // TODO add your handling code here:
     
    }//GEN-LAST:event_rdoRatificationNoActionPerformed
    
    private void rdoRatificationYesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoRatificationYesActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_rdoRatificationYesActionPerformed
    
    private void btnUnClaimedDivTransferAcHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUnClaimedDivTransferAcHdActionPerformed
        // TODO add your handling code here:
        callView("UNCLAIMEDTRANSFERACHD");
    }//GEN-LAST:event_btnUnClaimedDivTransferAcHdActionPerformed
    
    private void btnDivPaidAcctActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDivPaidAcctActionPerformed
        // TODO add your handling code here:
        callView(VIEW_TYPE_DIV_PAID_ACCT);
    }//GEN-LAST:event_btnDivPaidAcctActionPerformed
    
    private void btnDivPayableAcctActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDivPayableAcctActionPerformed
        // TODO add your handling code here:
        callView(VIEW_TYPE_DIV_PAYABLE_ACCT);
    }//GEN-LAST:event_btnDivPayableAcctActionPerformed
    
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        callView(ClientConstants.ACTION_STATUS[17]);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed
    
    private void cboDivCalcFrequencyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboDivCalcFrequencyActionPerformed
        // TODO add your handling code here:
        if(cboDivCalcFrequency.getSelectedIndex()!=0 && !tdtCalculatedDate.getDateValue().equals("")){
            Date calculatedDate = DateUtil.getDateMMDDYYYY(tdtCalculatedDate.getDateValue());
            String frequency = CommonUtil.convertObjToStr(observable.getCbmDivCalcFrequency().getKeyForSelected());
            int days = Integer.parseInt(frequency);
            Date dueDate = DateUtil.addDays(calculatedDate, days);
            //            tdtDueDate.setDateValue(DateUtil.getStringDate(dueDate));
        }
    }//GEN-LAST:event_cboDivCalcFrequencyActionPerformed
    
    private void tdtDivRunDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtDivRunDateFocusGained
        // TODO add your handling code here:
        
    }//GEN-LAST:event_tdtDivRunDateFocusGained
    
    private void tdtCalculatedDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtCalculatedDateFocusLost
        // TODO add your handling code here:
        if(cboDivCalcFrequency.getSelectedIndex()!=0 && !tdtCalculatedDate.getDateValue().equals("")){
            Date calculatedDate = DateUtil.getDateMMDDYYYY(tdtCalculatedDate.getDateValue());
            String frequency = CommonUtil.convertObjToStr(observable.getCbmDivCalcFrequency().getKeyForSelected());
            int days = Integer.parseInt(frequency);
            Date dueDate = DateUtil.addDays(calculatedDate, days);
            //            tdtDueDate.setDateValue(DateUtil.getStringDate(dueDate));
        }
    }//GEN-LAST:event_tdtCalculatedDateFocusLost
    
    private void txtPaidupCapitalFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPaidupCapitalFocusLost
        // TODO add your handling code here:
        if(!txtIssuedCapital.getText().equals("") && !txtPaidupCapital.getText().equals("")){
            captitalValidation(txtIssuedCapital, txtPaidupCapital);
        }
    }//GEN-LAST:event_txtPaidupCapitalFocusLost
    
    private void txtSubscribedCapitalFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSubscribedCapitalFocusLost
        // TODO add your handling code here:
        if(!txtIssuedCapital.getText().equals("") && !txtSubscribedCapital.getText().equals("")){
            captitalValidation(txtIssuedCapital, txtSubscribedCapital);
        }
    }//GEN-LAST:event_txtSubscribedCapitalFocusLost
    
    private void btnShareFeeAcctActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShareFeeAcctActionPerformed
        // TODO add your handling code here:
        callView(VIEW_TYPE_SHARE_FEE_ACCT);
    }//GEN-LAST:event_btnShareFeeAcctActionPerformed
    
    private void btnApplFeeAcctActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApplFeeAcctActionPerformed
        // TODO add your handling code here:
        callView(VIEW_TYPE_APPLICATION_FEE_ACCT);
    }//GEN-LAST:event_btnApplFeeAcctActionPerformed
    
    private void btnMemFeeAcctActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMemFeeAcctActionPerformed
        // TODO add your handling code here:
        callView(VIEW_TYPE_MEMBERSHIP_FEE_ACCT);
    }//GEN-LAST:event_btnMemFeeAcctActionPerformed
    
    private void btnShareAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShareAccountActionPerformed
        // TODO add your handling code here:
        callView(VIEW_TYPE_SHARE_ACCT);
    }//GEN-LAST:event_btnShareAccountActionPerformed
    
    private void cboUnclaimedDivPeriodFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboUnclaimedDivPeriodFocusLost
        if(txtUnclaimedDivPeriod.getText().length()!=0 && observable.getCbmUnclaimedDivPeriod().getKeyForSelected()!=null){
            ClientUtil.validPeriodMaxLength(txtUnclaimedDivPeriod, CommonUtil.convertObjToStr(observable.getCbmUnclaimedDivPeriod().getKeyForSelected()));
        }
    }//GEN-LAST:event_cboUnclaimedDivPeriodFocusLost
    
    private void cboNomineePeriodFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboNomineePeriodFocusLost
        // TODO add your handling code here:
        if(txtNomineePeriod.getText().length()!=0 && observable.getCbmNomineePeriod().getKeyForSelected()!=null){
            ClientUtil.validPeriodMaxLength(txtNomineePeriod, CommonUtil.convertObjToStr(observable.getCbmNomineePeriod().getKeyForSelected()));
        }
    }//GEN-LAST:event_cboNomineePeriodFocusLost
    
    private void cboAdditionalShareRefundFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboAdditionalShareRefundFocusLost
        // TODO add your handling code here:
        if(txtAdditionalShareRefund.getText().length()!=0 && observable.getCbmAdditionalShareRefund().getKeyForSelected()!=null){
            ClientUtil.validPeriodMaxLength(txtAdditionalShareRefund, CommonUtil.convertObjToStr(observable.getCbmAdditionalShareRefund().getKeyForSelected()));
        }
    }//GEN-LAST:event_cboAdditionalShareRefundFocusLost
    
    private void cboRefundPeriodFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboRefundPeriodFocusLost
        // TODO add your handling code here:
        if(txtRefundPeriod.getText().length()!=0 && observable.getCbmRefundPeriod().getKeyForSelected()!=null){
            ClientUtil.validPeriodMaxLength(txtRefundPeriod, CommonUtil.convertObjToStr(observable.getCbmRefundPeriod().getKeyForSelected()));
        }
    }//GEN-LAST:event_cboRefundPeriodFocusLost
    
    private void txtRefundPeriodFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRefundPeriodFocusLost
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtRefundPeriodFocusLost
    
    private void cboLoanTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLoanTypeActionPerformed
        // TODO add your handling code here:
        loanTypeExists = false;
        if(tblLoanType.getModel().getRowCount() != 0){
            for(int i=0;i<tblLoanType.getModel().getRowCount();i++){
                if(observable.getCbmLoanType().getKeyForSelected().equals(tblLoanType.getValueAt(i,0))){
                    txtLoanAvailingLimit.setText(CommonUtil.convertObjToStr(tblLoanType.getValueAt(i,1)));
                    txtMaxLoanLimit.setText(CommonUtil.convertObjToStr(tblLoanType.getValueAt(i,2)));
                    txtMaxLoanAmt.setText(CommonUtil.convertObjToStr(tblLoanType.getValueAt(i,3)));
//                    txtSurityLimit.setText(CommonUtil.convertObjToStr(tblLoanType.getValueAt(i,3)));
                    loanTypeExists = true;
                    existingRowcount = i;
                    break;
                }
            }
            if(!loanTypeExists){
                updateOBFields();
                observable.resetLoanDetailsExceptLoanType();
            }
        }
    }//GEN-LAST:event_cboLoanTypeActionPerformed
    
    private void btnTabDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTabDeleteActionPerformed
        // TODO add your handling code here:
        observable.removeSelectedRow(tblLoanType.getSelectedRow());
        btnTabDelete.setEnabled(false);
        btnTabSave.setEnabled(false);
        ClientUtil.enableDisable(panLoanType,false);
    }//GEN-LAST:event_btnTabDeleteActionPerformed
    
    private void tblLoanTypeMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblLoanTypeMousePressed
        // TODO add your handling code here:
        if(observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && !viewType.equals(AUTHORIZE)){
            dataExists = true;
            updateOBFields();
            tblRowCount = tblLoanType.getSelectedRow();
            observable.populateSelectedRow(tblRowCount);
            btnTabSave.setEnabled(true);
            btnTabDelete.setEnabled(true);
            ClientUtil.enableDisable(panLoanType,true);
            cboLoanType.setEnabled(false);
            System.out.println("Data Type Exists"+ dataExists);
        }
        
        
    }//GEN-LAST:event_tblLoanTypeMousePressed
    
    private void btnTabSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTabSaveActionPerformed
        // TODO add your handling code here:
        String mandatoryMessage = checkMandatory(panLoanType);
        if(cboLoanType.getSelectedItem().equals("")){
            mandatoryMessage += objMandatoryRB.getString("cboLoanType");
        }
        if(txtMaxLoanLimit.getText().equals("")){
            mandatoryMessage += objMandatoryRB.getString("txtMaxLoanLimit");
        }
         if(txtMaxLoanAmt.getText().equals("")){
            mandatoryMessage += objMandatoryRB.getString("txtMaxLoanAmt");
        }
        if(mandatoryMessage.length() > 0 ){
            displayAlert(mandatoryMessage);
        }else{
            updateOBFields();
            if(loanTypeExists){
                observable.createLoanDataMap(loanTypeExists, existingRowcount);
            }else{
                observable.createLoanDataMap(dataExists, tblRowCount);
            }
            btnTabSave.setEnabled(false);
            btnTabDelete.setEnabled(false);
            ClientUtil.enableDisable(panLoanType,false);
        }
    }//GEN-LAST:event_btnTabSaveActionPerformed
    
    private void btnTabNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTabNewActionPerformed
        // TODO add your handling code here:
        updateOBFields();
        observable.resetLoanDetails();
        dataExists = false;
        tblRowCount = tblLoanType.getModel().getRowCount();
        btnTabSave.setEnabled(true);
        ClientUtil.enableDisable(panLoanType,true);
        
    }//GEN-LAST:event_btnTabNewActionPerformed
    
    private void tdtDivRunDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtDivRunDateFocusLost
        // TODO add your handling code here:
        //        ClientUtil.validateToDate(tdtDueDate,tdtCalculatedDate.getDateValue());
    }//GEN-LAST:event_tdtDivRunDateFocusLost
    
    private void btnShareSuspAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShareSuspAccountActionPerformed
        // TODO add your handling code here:
        callView(VIEW_TYPE_SHARE_SUSPENSE_ACCT);
    }//GEN-LAST:event_btnShareSuspAccountActionPerformed
    
    private void cboShareTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboShareTypeItemStateChanged
        // TODO add your handling code here:
        
        
    }//GEN-LAST:event_cboShareTypeItemStateChanged
    
    private void cboShareTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboShareTypeActionPerformed
        // TODO add your handling code here:
        try{
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
                if(observable.isShareTypeExists(CommonUtil.convertObjToStr(observable.getCbmShareType().getKeyForSelected()))){
                    final String message= "existingMsg";
                    showAlertWindow(message);
                    cboShareType.setSelectedItem("");
                    txtShareSuspAccount.requestFocus();
                }
            }
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }//GEN-LAST:event_cboShareTypeActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:]
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        disDesc();
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // TODO add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // TODO add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
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
        super.removeEditLock(((ComboBoxModel)cboShareType.getModel()).getKeyForSelected().toString());
        observable.resetForm();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panShareProduct, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setBtnPanLoanOperationsEnable(false);
        setButtonEnableDisable();
        setHelpButtonEnableDisable(false);
        viewType = "";
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        visibleSubsidyShare(false);
        itemsSubmittingList = new ArrayList();
        chkCaste.setSelected(false);
        chkReligion.setSelected(false);
        chkWardNo.setSelected(false);
        chkAmsam.setSelected(false);
        chkDesam.setSelected(false);
        chkVillage.setSelected(false);
        chkTaluk.setSelected(false);
        chkPinCode.setSelected(false);
        chkCity.setSelected(false);
        chkStreet.setSelected(false);
        chkArea.setSelected(false);
        chkDob.setSelected(false);
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView("Delete");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        mandatoryData.clear();
        mandatoryAddrData.clear();
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView("Edit");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        txtNumPatternFollowedPrefix.setEnabled(false);
        txtNumPatternFollowedSuffix.setEnabled(false);
        disDesc();
    }//GEN-LAST:event_btnEditActionPerformed
     private void addRadioButtons() {
        rdgAdmissionFeeType = new CButtonGroup();
        rdgAdmissionFeeType.add(rdoRatificationYes);
        rdgAdmissionFeeType.add(rdoRatificationNo);
        //  rdgAdmissionFeeType.add(rdoAdmissionFeeType_Fixed);
        //rdgAdmissionFeeType.add(rdoAdmissionFeeType_Percent);

    }

    private void removeRadioButtons() {
        rdgAdmissionFeeType.remove(rdoRatificationYes);
        rdgAdmissionFeeType.remove(rdoRatificationNo);
        //rdgAdmissionFeeType.remove(rdoAdmissionFeeType_Fixed);
        // rdgAdmissionFeeType.remove(rdoAdmissionFeeType_Percent);

    }

//   private void calloption(String fieldVal) {
//        HashMap WhereMap = new HashMap();
//        WhereMap.put("fieldval", fieldVal);
//        WhereMap.put("SHARE_TYPE", observable.getCbmShareType().getKeyForSelected());
//        ClientUtil.executeQuery("updateShareProductTO", WhereMap);
//    }    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        setModified(false);
        savePerformed();
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.resetForm();
        ClientUtil.enableDisable(panShareProduct, true);
        ClientUtil.enableDisable(panLoanType,false);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        setHelpButtonEnableDisable(true);
        btnTabNew.setEnabled(true);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        chkConsiderSalaryRecovery.setSelected(true);
        txtUnClaimedDivTransferAcHd.setText("");
        txtUnClaimedDivTransferAcHdDesc.setText("");
        disDesc();
        itemsSubmittingList = new ArrayList();
    }//GEN-LAST:event_btnNewActionPerformed

private void chkOutstandingRequiredActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkOutstandingRequiredActionPerformed

}//GEN-LAST:event_chkOutstandingRequiredActionPerformed
   

private void chkConsiderSalaryRecoveryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkConsiderSalaryRecoveryActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_chkConsiderSalaryRecoveryActionPerformed

private void chkReqActHolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkReqActHolderActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_chkReqActHolderActionPerformed

private void chkReqActHolderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_chkReqActHolderMouseClicked
// TODO add your handling code here:
}//GEN-LAST:event_chkReqActHolderMouseClicked

private void cboProdTypeCrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeCrActionPerformed
    // TODO add your handling code here:    
    if(cboProdTypeCr.getSelectedItem()!=null && !cboProdTypeCr.getSelectedItem().equals("")){
        if(cboProdTypeCr.getSelectedItem().equals("General Ledger")){
            txtTransProductId.setEnabled(false);
            btnTransProductId.setEnabled(false);
        }else{
            txtTransProductId.setEnabled(true);
            btnTransProductId.setEnabled(true);
        }
        observable.setPensionProductType(CommonUtil.convertObjToStr(((ComboBoxModel) cboProdTypeCr.getModel()).getKeyForSelected()));
    }
}//GEN-LAST:event_cboProdTypeCrActionPerformed

public static boolean isNumeric(String str) {
        try {
            //Integer.parseInt(str);
            Float.parseFloat(str);
            //   System.out.println("ddd"+d);
        } catch(NumberFormatException nfe) {
            return false;
        }
        return true;
    }

private void txtAcctNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAcctNoActionPerformed

}//GEN-LAST:event_txtAcctNoActionPerformed

private void txtAcctNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAcctNoFocusLost

}//GEN-LAST:event_txtAcctNoFocusLost

private void btnAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccNoActionPerformed
    callView("ACCT_NO");
}//GEN-LAST:event_btnAccNoActionPerformed

private void txtTransProductIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTransProductIdFocusLost
// TODO add your handling code here: 
    if (txtTransProductId.getText() != null && txtTransProductId.getText().length() > 0) {
        
    }
}//GEN-LAST:event_txtTransProductIdFocusLost

private void btnTransProductIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransProductIdActionPerformed
    callView("PROD_ID");
}//GEN-LAST:event_btnTransProductIdActionPerformed

private void txtPensionAgeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPensionAgeFocusLost
// TODO add your handling code here:  
    if(txtPensionAge.getText()!=null && txtPensionAge.getText().length()>0){
        if(!isNumeric(txtPensionAge.getText())){
            ClientUtil.showAlertWindow("Enter Numeric Values!!");
            txtPensionAge.setText("");
            txtPensionAge.grabFocus();
        }
    }
}//GEN-LAST:event_txtPensionAgeFocusLost

private void txtShareDurationFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtShareDurationFocusLost
// TODO add your handling code here:
    if(txtShareDuration.getText()!=null && txtShareDuration.getText().length()>0){
        if(!isNumeric(txtShareDuration.getText())){
            ClientUtil.showAlertWindow("Enter Numeric Values!!");
            txtShareDuration.setText("");
            txtShareDuration.grabFocus();
        }
    }
}//GEN-LAST:event_txtShareDurationFocusLost
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
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAccNo;
    private com.see.truetransact.uicomponent.CButton btnApplFeeAcct;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDivPaidAcct;
    private com.see.truetransact.uicomponent.CButton btnDivPayableAcct;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnGovtSubsidyAccountHead;
    private com.see.truetransact.uicomponent.CButton btnIncomeAccountHead;
    private com.see.truetransact.uicomponent.CButton btnMemFeeAcct;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnShareAccount;
    private com.see.truetransact.uicomponent.CButton btnShareFeeAcct;
    private com.see.truetransact.uicomponent.CButton btnShareSuspAccount;
    private com.see.truetransact.uicomponent.CButton btnTabDelete;
    private com.see.truetransact.uicomponent.CButton btnTabNew;
    private com.see.truetransact.uicomponent.CButton btnTabSave;
    private com.see.truetransact.uicomponent.CButton btnTransProductId;
    private com.see.truetransact.uicomponent.CButton btnUnClaimedDivTransferAcHd;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboAdditionalShareRefund;
    private com.see.truetransact.uicomponent.CComboBox cboDivAppFrequency;
    private com.see.truetransact.uicomponent.CComboBox cboDivCalcFrequency;
    private com.see.truetransact.uicomponent.CComboBox cboDivCalcFrequencyNew;
    private com.see.truetransact.uicomponent.CComboBox cboDivCalcType;
    private com.see.truetransact.uicomponent.CComboBox cboDividendRounding;
    private com.see.truetransact.uicomponent.CComboBox cboLoanType;
    private com.see.truetransact.uicomponent.CComboBox cboNomineePeriod;
    private com.see.truetransact.uicomponent.CComboBox cboProdTypeCr;
    private com.see.truetransact.uicomponent.CComboBox cboRefundPeriod;
    private com.see.truetransact.uicomponent.CComboBox cboShareType;
    private com.see.truetransact.uicomponent.CComboBox cboUnclaimedDivPeriod;
    private com.see.truetransact.uicomponent.CCheckBox chkAmsam;
    private com.see.truetransact.uicomponent.CCheckBox chkArea;
    private com.see.truetransact.uicomponent.CCheckBox chkCaste;
    private com.see.truetransact.uicomponent.CCheckBox chkCity;
    private com.see.truetransact.uicomponent.CCheckBox chkConsiderSalaryRecovery;
    private com.see.truetransact.uicomponent.CCheckBox chkDRF;
    private com.see.truetransact.uicomponent.CCheckBox chkDesam;
    private com.see.truetransact.uicomponent.CCheckBox chkDob;
    private com.see.truetransact.uicomponent.CCheckBox chkOutstandingRequired;
    private com.see.truetransact.uicomponent.CCheckBox chkPinCode;
    private com.see.truetransact.uicomponent.CCheckBox chkReligion;
    private com.see.truetransact.uicomponent.CCheckBox chkReqActHolder;
    private com.see.truetransact.uicomponent.CCheckBox chkShareCerti;
    private com.see.truetransact.uicomponent.CCheckBox chkStreet;
    private com.see.truetransact.uicomponent.CCheckBox chkSubsidyForSCST;
    private com.see.truetransact.uicomponent.CCheckBox chkTaluk;
    private com.see.truetransact.uicomponent.CCheckBox chkVillage;
    private com.see.truetransact.uicomponent.CCheckBox chkWardNo;
    private javax.swing.JPanel jPanel1;
    private com.see.truetransact.uicomponent.CLabel lbGovtSubsidyAccountHead;
    private com.see.truetransact.uicomponent.CLabel lblAccNo;
    private com.see.truetransact.uicomponent.CLabel lblAdditionalShareRefund;
    private com.see.truetransact.uicomponent.CLabel lblAdmissionFee;
    private com.see.truetransact.uicomponent.CLabel lblAdmissionFeeMax;
    private com.see.truetransact.uicomponent.CLabel lblAdmissionFeeMin;
    private com.see.truetransact.uicomponent.CLabel lblAdmissionFeeType;
    private com.see.truetransact.uicomponent.CLabel lblAmount;
    private com.see.truetransact.uicomponent.CLabel lblAmsam;
    private com.see.truetransact.uicomponent.CLabel lblApplFeeAcct;
    private com.see.truetransact.uicomponent.CLabel lblApplicationFee;
    private com.see.truetransact.uicomponent.CLabel lblArea;
    private com.see.truetransact.uicomponent.CLabel lblAuthorizedCapital;
    private com.see.truetransact.uicomponent.CLabel lblCalculatedDate;
    private com.see.truetransact.uicomponent.CLabel lblCaste;
    private com.see.truetransact.uicomponent.CLabel lblCity;
    private com.see.truetransact.uicomponent.CLabel lblConsiderSalaryRecovery;
    private com.see.truetransact.uicomponent.CLabel lblCustomerShare;
    private com.see.truetransact.uicomponent.CLabel lblDesam;
    private com.see.truetransact.uicomponent.CLabel lblDivAppFrequency;
    private com.see.truetransact.uicomponent.CLabel lblDivCalcFrequency;
    private com.see.truetransact.uicomponent.CLabel lblDivCalcFrequencyNew;
    private com.see.truetransact.uicomponent.CLabel lblDivCalcType;
    private com.see.truetransact.uicomponent.CLabel lblDivRunDate;
    private com.see.truetransact.uicomponent.CLabel lblDividendRounding;
    private com.see.truetransact.uicomponent.CLabel lblDividentPercentage;
    private com.see.truetransact.uicomponent.CLabel lblDob;
    private com.see.truetransact.uicomponent.CLabel lblFaceValue;
    private com.see.truetransact.uicomponent.CLabel lblFullClosure;
    private com.see.truetransact.uicomponent.CLabel lblGovernmentShare;
    private com.see.truetransact.uicomponent.CLabel lblLastuncaimedRunDate;
    private com.see.truetransact.uicomponent.CLabel lblLastuncaimedUpTo;
    private com.see.truetransact.uicomponent.CLabel lblLoanType;
    private com.see.truetransact.uicomponent.CLabel lblMaxLoanAmt;
    private com.see.truetransact.uicomponent.CLabel lblMaxShareHolding;
    private com.see.truetransact.uicomponent.CLabel lblMemFeeAcct;
    private com.see.truetransact.uicomponent.CLabel lblMinDividendAmount;
    private com.see.truetransact.uicomponent.CLabel lblMinIntialShares;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNomineePeriod;
    private com.see.truetransact.uicomponent.CLabel lblNommineeAllowed;
    private com.see.truetransact.uicomponent.CLabel lblNumPatternFollowed;
    private com.see.truetransact.uicomponent.CLabel lblOutstandingReq;
    private com.see.truetransact.uicomponent.CLabel lblPAge;
    private com.see.truetransact.uicomponent.CLabel lblPaidCapital;
    private com.see.truetransact.uicomponent.CLabel lblPercentageLoanAmount;
    private com.see.truetransact.uicomponent.CLabel lblPercentageLoanAmt;
    private com.see.truetransact.uicomponent.CLabel lblPeriod;
    private com.see.truetransact.uicomponent.CLabel lblPincode;
    private com.see.truetransact.uicomponent.CLabel lblProdIdCr;
    private com.see.truetransact.uicomponent.CLabel lblProdTypeCr;
    private com.see.truetransact.uicomponent.CLabel lblRatification;
    private com.see.truetransact.uicomponent.CLabel lblRefundPeriod;
    private com.see.truetransact.uicomponent.CLabel lblRefundinaYear;
    private com.see.truetransact.uicomponent.CLabel lblReligion;
    private com.see.truetransact.uicomponent.CLabel lblReqActHolder;
    private com.see.truetransact.uicomponent.CLabel lblShareAccount;
    private com.see.truetransact.uicomponent.CLabel lblShareFee;
    private com.see.truetransact.uicomponent.CLabel lblShareFeeAcct;
    private com.see.truetransact.uicomponent.CLabel lblShareSubscriptionBorrower;
    private com.see.truetransact.uicomponent.CLabel lblShareSubscriptionSurity;
    private com.see.truetransact.uicomponent.CLabel lblShareSuspAccount;
    private com.see.truetransact.uicomponent.CLabel lblShareType;
    private com.see.truetransact.uicomponent.CLabel lblSharedivPaidAcct;
    private com.see.truetransact.uicomponent.CLabel lblSharedivPayableAcct;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace13;
    private com.see.truetransact.uicomponent.CLabel lblSpace14;
    private com.see.truetransact.uicomponent.CLabel lblSpace15;
    private com.see.truetransact.uicomponent.CLabel lblSpace16;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace7;
    private com.see.truetransact.uicomponent.CLabel lblSpace8;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblStreet;
    private com.see.truetransact.uicomponent.CLabel lblSubscribedCapital;
    private com.see.truetransact.uicomponent.CLabel lblSubsidyForSCST2;
    private com.see.truetransact.uicomponent.CLabel lblTaluk;
    private com.see.truetransact.uicomponent.CLabel lblUnClaimedDivPeriod;
    private com.see.truetransact.uicomponent.CLabel lblUnClaimedDivTransferAcHd;
    private com.see.truetransact.uicomponent.CLabel lblVillage;
    private com.see.truetransact.uicomponent.CLabel lblWardNo;
    private com.see.truetransact.uicomponent.CLabel lblncomeAccountHead;
    private com.see.truetransact.uicomponent.CMenuBar mbrShareProduct;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAccHd;
    private com.see.truetransact.uicomponent.CPanel panAccountDetails;
    private com.see.truetransact.uicomponent.CPanel panAccountDetails1;
    private com.see.truetransact.uicomponent.CPanel panAcctDet;
    private com.see.truetransact.uicomponent.CPanel panAcctNo;
    private com.see.truetransact.uicomponent.CPanel panAdditonalShareRefund;
    private com.see.truetransact.uicomponent.CPanel panApplFeeAcct;
    private com.see.truetransact.uicomponent.CPanel panAutoRenewal1;
    private com.see.truetransact.uicomponent.CPanel panBorrowing;
    private com.see.truetransact.uicomponent.CPanel panDebitAccHead;
    private com.see.truetransact.uicomponent.CPanel panDetails;
    private com.see.truetransact.uicomponent.CPanel panDivPaidAcHd;
    private com.see.truetransact.uicomponent.CPanel panDivPayableAcHd;
    private com.see.truetransact.uicomponent.CPanel panDivident;
    private com.see.truetransact.uicomponent.CPanel panDividentNew;
    private com.see.truetransact.uicomponent.CPanel panFullClosure;
    private com.see.truetransact.uicomponent.CPanel panGldRenewal;
    private com.see.truetransact.uicomponent.CPanel panGovtSubsidyAccountHead;
    private com.see.truetransact.uicomponent.CPanel panIncomeAccountHead;
    private com.see.truetransact.uicomponent.CPanel panLoan;
    private com.see.truetransact.uicomponent.CPanel panLoanOperations;
    private com.see.truetransact.uicomponent.CPanel panLoanType;
    private com.see.truetransact.uicomponent.CPanel panMemFeeAcct;
    private com.see.truetransact.uicomponent.CPanel panNominee;
    private com.see.truetransact.uicomponent.CPanel panPension;
    private com.see.truetransact.uicomponent.CPanel panPensionScheme;
    private com.see.truetransact.uicomponent.CPanel panRatification;
    private com.see.truetransact.uicomponent.CPanel panRefundPeriod;
    private com.see.truetransact.uicomponent.CPanel panShare;
    private com.see.truetransact.uicomponent.CPanel panShareAccount;
    private com.see.truetransact.uicomponent.CPanel panShareFeeAcct;
    private com.see.truetransact.uicomponent.CPanel panShareProduct;
    private com.see.truetransact.uicomponent.CPanel panShareSuspAccount;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTblLoanType;
    private com.see.truetransact.uicomponent.CPanel panUInClaimedDivident;
    private com.see.truetransact.uicomponent.CPanel panUnClaimedDivTransferAcHd;
    private com.see.truetransact.uicomponent.CButtonGroup rdgAdmissionFeeType;
    private com.see.truetransact.uicomponent.CRadioButton rdoAdmissionFeeType_Fixed;
    private com.see.truetransact.uicomponent.CRadioButton rdoAdmissionFeeType_Percent;
    private com.see.truetransact.uicomponent.CRadioButton rdoFullClosureNo;
    private com.see.truetransact.uicomponent.CRadioButton rdoFullClosureYes;
    private com.see.truetransact.uicomponent.CRadioButton rdoRatificationNo;
    private com.see.truetransact.uicomponent.CRadioButton rdoRatificationYes;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpLoanType;
    private com.see.truetransact.uicomponent.CTabbedPane tabShareProduct;
    private com.see.truetransact.uicomponent.CTable tblLoanType;
    private javax.swing.JToolBar tbrShareProduct;
    private com.see.truetransact.uicomponent.CDateField tdtCalculatedDate;
    private com.see.truetransact.uicomponent.CDateField tdtDivRunDate;
    private com.see.truetransact.uicomponent.CDateField tdtLastuncaimedRunDate;
    private com.see.truetransact.uicomponent.CDateField tdtLastuncaimedUpTo;
    public static com.see.truetransact.uicomponent.CTextField txtAcctNo;
    private com.see.truetransact.uicomponent.CTextField txtAdditionalShareRefund;
    private com.see.truetransact.uicomponent.CTextField txtAdmissionFee;
    private com.see.truetransact.uicomponent.CTextField txtAdmissionFeeMax;
    private com.see.truetransact.uicomponent.CTextField txtAdmissionFeeMin;
    private com.see.truetransact.uicomponent.CTextField txtApplFeeAcct;
    private com.see.truetransact.uicomponent.CTextField txtApplFeeAcctDesc;
    private com.see.truetransact.uicomponent.CTextField txtApplicationFee;
    private com.see.truetransact.uicomponent.CTextField txtCustomerShare;
    private com.see.truetransact.uicomponent.CTextField txtDivPaidAcct;
    private com.see.truetransact.uicomponent.CTextField txtDivPaidAcctDesc;
    private com.see.truetransact.uicomponent.CTextField txtDivPayableAcct;
    private com.see.truetransact.uicomponent.CTextField txtDivPayableAcctDesc;
    private com.see.truetransact.uicomponent.CTextField txtDividentPercentage;
    private com.see.truetransact.uicomponent.CTextField txtFaceValue;
    private com.see.truetransact.uicomponent.CTextField txtGovernmentShare;
    private com.see.truetransact.uicomponent.CTextField txtGovtSubsidyAccountHead;
    private com.see.truetransact.uicomponent.CTextField txtIncomeAccountHead;
    private com.see.truetransact.uicomponent.CTextField txtIncomeAccountHeadDesc;
    private com.see.truetransact.uicomponent.CTextField txtIssuedCapital;
    private com.see.truetransact.uicomponent.CTextField txtLoanAvailingLimit;
    private com.see.truetransact.uicomponent.CTextField txtMaxLoanAmt;
    private com.see.truetransact.uicomponent.CTextField txtMaxLoanLimit;
    private com.see.truetransact.uicomponent.CTextField txtMaxShareHolding;
    private com.see.truetransact.uicomponent.CTextField txtMemFeeAcct;
    private com.see.truetransact.uicomponent.CTextField txtMemFeeAcctDesc;
    private com.see.truetransact.uicomponent.CTextField txtMinDividendAmount;
    private com.see.truetransact.uicomponent.CTextField txtMinIntialshares;
    private com.see.truetransact.uicomponent.CTextField txtMinPension;
    private com.see.truetransact.uicomponent.CTextField txtNomineePeriod;
    private com.see.truetransact.uicomponent.CTextField txtNomineeaAllowed;
    private com.see.truetransact.uicomponent.CTextField txtNumPatternFollowedPrefix;
    private com.see.truetransact.uicomponent.CTextField txtNumPatternFollowedSuffix;
    private com.see.truetransact.uicomponent.CTextField txtPaidupCapital;
    private com.see.truetransact.uicomponent.CTextField txtPensionAge;
    private com.see.truetransact.uicomponent.CTextField txtRefundPeriod;
    private com.see.truetransact.uicomponent.CTextField txtRefundinaYear;
    private com.see.truetransact.uicomponent.CTextField txtShareAccount;
    private com.see.truetransact.uicomponent.CTextField txtShareAccountDesc;
    private com.see.truetransact.uicomponent.CTextField txtShareDuration;
    private com.see.truetransact.uicomponent.CTextField txtShareFee;
    private com.see.truetransact.uicomponent.CTextField txtShareFeeAcct;
    private com.see.truetransact.uicomponent.CTextField txtShareFeeAcctDesc;
    private com.see.truetransact.uicomponent.CTextField txtShareSuspAccount;
    private com.see.truetransact.uicomponent.CTextField txtShareSuspAccountDesc;
    private com.see.truetransact.uicomponent.CTextField txtSubscribedCapital;
    private com.see.truetransact.uicomponent.CTextField txtTransProductId;
    private com.see.truetransact.uicomponent.CTextField txtUnClaimedDivTransferAcHd;
    private com.see.truetransact.uicomponent.CTextField txtUnClaimedDivTransferAcHdDesc;
    private com.see.truetransact.uicomponent.CTextField txtUnclaimedDivPeriod;
    // End of variables declaration//GEN-END:variables
    
}
