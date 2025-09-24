/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * chequeBookUI.java
 *
 * Created on January 19, 2004, 12:57 PM
 */

package com.see.truetransact.ui.supporting.chequebook;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;

import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.CMandatoryDialog;

import com.see.truetransact.clientproxy.ProxyParameters;

import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.uicomponent.CInternalFrame;

import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;

import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;

import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.common.viewall.TableDialogUI;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.ui.transaction.common.TransDetailsUI;
import com.see.truetransact.ui.common.viewphotosign.ViewPhotoSignUI;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;


//__ To test the code...
//import com.see.truetransact.ui.supporting.generateseries.GenerateSeries;

import com.see.truetransact.ui.common.viewall.NewAuthorizeListUI;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.Observable;
import java.lang.Double;
import java.util.Date;
/**
 *
 * @author  rahul
 */
public class ChequeBookUI extends CInternalFrame implements java.util.Observer, UIMandatoryField {
    HashMap mandatoryMap;
    ChequeBookOB observable;
    final int EDIT=0,DELETE=1,ACCNOCHEQUE=2,ACCNOSTOP=3,ACCNOLOOSE=4,AUTHORIZE=5, VIEW=10,ECSSTOP=7;
    int viewType=-1;
    final int CHEQUE=0,STOP=1,LEAF=2,REVOKE=3, ENQUIRY=4, ECS=6;
    int pan=-1;
    int panEditDelete=-1;
    int view = -1;
    private final int INFO_MESSAGE = 1;
    private String chqIssueId = "" ;
    String CHEQUEAUTHID = "";
    String Status = "";
     HashMap calMap= new HashMap();
    boolean isFilled = false;
    boolean isAuth = false;
    boolean AUTH = false;
    private boolean isRevoked = false;
    private boolean iEcsRevoked = false;
    private String stopStatus = "";
    boolean fromAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
    
    private Date currDt = null;
    private TransDetailsUI transDetails = null;
    private int rejectFlag=0;
    private String Trans_Id ="";
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;
    
    //ChequeBookRB resourceBundle = new ChequeBookRB();
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.supporting.chequebook.ChequeBookRB", ProxyParameters.LANGUAGE);
     TransactionUI transactionUI = new TransactionUI(); 
    /** Creates new form chequeBookUI */
    
    public ChequeBookUI(String selectedBranchId) {
        try{
            currDt = ClientUtil.getCurrentDate();
            setSelectedBranchID(selectedBranchId);
            initComponents();
            initSetup();
        }catch(Exception E){
            E.printStackTrace();
        }
    }
    
    
    private void initSetup() throws Exception{
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setObservable();
        initComponentData();
        setMaxLenths();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panChequeBookIssue);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panStopPaymentIssue);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panLooseLeafIssue);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panEcsStopPaymentIssue);
        ClientUtil.enableDisable(this, false); //__ Disables all when the screen appears for the 1st time
        disableButtons();                      //__ To Disable the Folder buttons...
        resetUI(4);                            //__  to Reset all the Fields and Status in UI...
        setButtonEnableDisable();              //__ Enables/Disables the necessary buttons and menu items...
        resetPanels();                         //__ To Reset the Value of the Selected Panle of the Panels...
        observable.resetStatus();              //__ to reset the status
        
        transDetails = new TransDetailsUI(panTransDetails);//__ To Show the Details of the Account Holder
        
        setHelpMessage();
        
        panChequeDetails.setVisible(true);
//        txtSeriesNoTo.setVisible(false);
//        txtSeriesFrom.setVisible(false);
        txtEndingchequeNo.setEditable(false);
        txtEndingCheque.setEditable(false);
        txtSeriesFrom.setEditable(false);
        txtSeriesNoTo.setEditable(false);
        lblReasonForRevoke.setVisible(false);
        txtStopReasonForRevoke.setVisible(false);
        lblChqRevokeDt.setVisible(false);
        lblChqRevokeDtVal.setVisible(false);
        lblChqStopDt.setVisible(false);
        lblChqStopDtVal.setVisible(false);
        lblEcsReasonForRevoke.setVisible(false);
        txtEcsStopReasonForRevoke.setVisible(false);
         lblEcsRevokeDt.setVisible(false);
        lblChqRevokeDtVal.setVisible(false);
    }
    
    private void setObservable() throws Exception {
        System.out.println("$$$$$$$$$$$$$$getSelectedBranchID() : " + getSelectedBranchID());
        observable = new ChequeBookOB(getSelectedBranchID());
        observable.addObserver(this);
    }
    
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        btnAccNo.setName("btnAccNo");
        btnAccountNo.setName("btnAccountNo");
        btnAccountNumber.setName("btnAccountNumber");
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnNew.setName("btnNew");
        btnPaymentRevoke.setName("btnPaymentRevoke");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        cboMethodOfDelivery.setName("cboMethodOfDelivery");
        cboNoOfLeaves.setName("cboNoOfLeaves");
        cboProdId.setName("cboProdId");
        cboProdType.setName("cboProdType");
        cboProductID.setName("cboProductID");
        cboProductId.setName("cboProductId");
        cboProductType.setName("cboProductType");
        cboProduct_Type.setName("cboProduct_Type");
        cboUsage.setName("cboUsage");
        lblAccHd.setName("lblAccHd");
        lblAccHdDesc.setName("lblAccHdDesc");
        lblAccHdId.setName("lblAccHdId");
        lblAccHead.setName("lblAccHead");
        lblAccHeadDesc.setName("lblAccHeadDesc");
//        lblAccHeadId.setName("lblAccHeadId");
        lblAccNo.setName("lblAccNo");
        lblAccounNumber.setName("lblAccounNumber");
        lblAccountHead.setName("lblAccountHead");
        lblAccountHeadDesc.setName("lblAccountHeadDesc");
        lblAccountHeadId.setName("lblAccountHeadId");
        lblAccountNo.setName("lblAccountNo");
        lblChargesCollected.setName("lblChargesCollected");
        lblChequeAmt.setName("lblChequeAmt");
        lblChequeDate.setName("lblChequeDate");
        lblCustId.setName("lblCustId");
        lblCustIdDesc.setName("lblCustIdDesc");
//        lblCustName.setName("lblCustName");
        lblCustNameValue.setName("lblCustNameValue");
//        lblCustomName.setName("lblCustomName");
        lblCustomNameValue.setName("lblCustomNameValue");
        lblCustomerID.setName("lblCustomerID");
        lblCustomerIDValue.setName("lblCustomerIDValue");
        lblCustomerId.setName("lblCustomerId");
        lblCustomerIdDesc.setName("lblCustomerIdDesc");
        lblCustomerName.setName("lblCustomerName");
        lblCustomerNameValue.setName("lblCustomerNameValue");
        lblEndChequeNo.setName("lblEndChequeNo");
        lblEndingCheque.setName("lblEndingCheque");
        lblLeaf.setName("lblLeaf");
        lblLeafNo.setName("lblLeafNo");
        lblMethodOfDelivery.setName("lblMethodOfDelivery");
        lblMsg.setName("lblMsg");
        lblNamesOfAccount.setName("lblNamesOfAccount");
        lblNoOfChequeBooks.setName("lblNoOfChequeBooks");
        lblNoOfLeaves.setName("lblNoOfLeaves");
        lblPayeeName.setName("lblPayeeName");
        lblProdId.setName("lblProdId");
        lblProdType.setName("lblProdType");
        lblProductID.setName("lblProductID");
        lblProductId.setName("lblProductId");
        lblProductType.setName("lblProductType");
        lblProduct_Type.setName("lblProduct_Type");
        lblReasonStopPayment.setName("lblReasonStopPayment");
        lblRemark.setName("lblRemark");
        lblRemarks.setName("lblRemarks");
        lblSeriesNoFrom.setName("lblSeriesNoFrom");
        lblSeriesNoTo.setName("lblSeriesNoTo");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpaces.setName("lblSpaces");
        lblStartChequeNo.setName("lblStartChequeNo");
        lblStartingCheque.setName("lblStartingCheque");
        lblStatus1.setName("lblStatus1");
        lblStopPaymentCharges.setName("lblStopPaymentCharges");
        lblUsage.setName("lblUsage");
        lblChqStopDt.setName("lblChqStopDt");
        lblChqRevokeDt.setName("lblChqRevokeDt");
        lblChqStopDtVal.setName("lblChqStopDtVal");
        lblChqRevokeDtVal.setName("lblChqRevokeDtVal");
        mbrLoanProduct.setName("mbrLoanProduct");
        panAccNo.setName("panAccNo");
        panAccounNumber.setName("panAccounNumber");
        panAccountNo.setName("panAccountNo");
        panCheque.setName("panCheque");
        panChequeBook.setName("panChequeBook");
        panChequeBookIssue.setName("panChequeBookIssue");
        panChequeDetails.setName("panChequeDetails");
        panCustomer.setName("panCustomer");
        panData.setName("panData");
        panEndChequeNo.setName("panEndChequeNo");
        panEndingCheque.setName("panEndingCheque");
        panLeaf.setName("panLeaf");
        panLeafNo.setName("panLeafNo");
        panLooseAccountHead.setName("panLooseAccountHead");
        panLooseLeafIssue.setName("panLooseLeafIssue");
        panPaymentIssues.setName("panPaymentIssues");
        panPaymentRevoke.setName("panPaymentRevoke");
        panProdLeaf.setName("panProdLeaf");
        panProduct.setName("panProduct");
        panProductData.setName("panProductData");
        panSeriesNo.setName("panSeriesNo");
        panStartChequeNo.setName("panStartChequeNo");
        panStartingCheque.setName("panStartingCheque");
        panStatus.setName("panStatus");
        panStopPaymentIssue.setName("panStopPaymentIssue");
        panTable.setName("panTable");
        panTransDetails.setName("panTransDetails");
        rdoLeaf_Bulk.setName("rdoLeaf_Bulk");
        rdoLeaf_single.setName("rdoLeaf_single");
        sptChequeIssue.setName("sptChequeIssue");
        sptPaymentIssues.setName("sptPaymentIssues");
        sptProductData.setName("sptProductData");
        srpChequeDetails.setName("srpChequeDetails");
        tabChequeBook.setName("tabChequeBook");
        tblChequeDetails.setName("tblChequeDetails");
        tdtChequeDate.setName("tdtChequeDate");
        txtAccNo.setName("txtAccNo");
        txtAccounNumber.setName("txtAccounNumber");
        txtAccountNo.setName("txtAccountNo");
        txtChargesCollected.setName("txtChargesCollected");
        txtChequeAmt.setName("txtChequeAmt");
        txtEndCheque.setName("txtEndCheque");
        txtEndingCheque.setName("txtEndingCheque");
        txtEndingChequeNo.setName("txtEndingChequeNo");
        txtEndingchequeNo.setName("txtEndingchequeNo");
        txtLeafNo1.setName("txtLeafNo1");
        txtLeafNo2.setName("txtLeafNo2");
        txtNamesOfAccount.setName("txtNamesOfAccount");
        txtNoOfChequeBooks.setName("txtNoOfChequeBooks");
        txtPayeeName.setName("txtPayeeName");
        cboReasonStopPayment.setName("cboReasonStopPayment");
        txtRemark.setName("txtRemark");
        txtRemarks.setName("txtRemarks");
        txtSeriesFrom.setName("txtSeriesFrom");
        txtSeriesNoTo.setName("txtSeriesNoTo");
        txtStartCheque.setName("txtStartCheque");
        txtStartchequeNo.setName("txtStartchequeNo");
        txtStartingCheque.setName("txtStartingCheque");
        txtStartingChequeNo.setName("txtStartingChequeNo");
        txtStopPaymentCharges.setName("txtStopPaymentCharges");
        lblReasonForRevoke.setName("lblReasonForRevoke");
        txtStopReasonForRevoke.setName("txtStopReasonForRevoke");
        panEcsStopPaymentIssue.setName("panEcsStopPaymentIssue");
        cboEcsProductType.setName("cboEcsProductType");
        cboEcsProductId.setName("cboEcsProductId");
        txtEcsAccNo.setName("txtEcsAccNo");
        btnEcsAccNo.setName("btnEcsAccNo");
        txtEndEcs1.setName("txtEndEcs1");
        txtEndEcsNo2.setName("txtEndEcsNo2");
        tdtEcsDate.setName("tdtEcsDate");
        txtEcsAmt.setName("txtEcsAmt");
        txtEcsPayeeName.setName("txtEcsPayeeName");
        txtEcsStopPaymentCharges.setName("txtEcsStopPaymentCharges");
        cboEcsReasonStopPayment.setName("cboEcsReasonStopPayment");
        txtEcsStopReasonForRevoke.setName("txtEcsStopReasonForRevoke");
        btnEcsPaymentRevoke.setName("btnEcsPaymentRevoke");
        lblCustIdDesc1.setName("lblCustIdDesc1");
        lblCustNameValue1.setName("lblCustNameValue1");
        lblEcsRevokeDt.setName("lblEcsRevokeDt");
        lblEcsRevokeDtVal.setName("lblEcsRevokeDtVal");
        lblEcsRevokeDt.setName("lblEcsRevokeDt");
        lblChqRevokeDtVal.setName("lblChqRevokeDtVal");
        lblEcsStopDt.setName("lblEcsStopDt");
        lblEcsStopDtVal.setName("lblEcsStopDtVal");
        lblChqStopId.setName("lblChqStopId");
        lblEcsId.setName("lblEcsId");
    }
    
    private void internationalize() {
        //        ChequeBookRB resourceBundle = new ChequeBookRB();
        //
        lblChqStopDtVal.setText(resourceBundle.getString("lblChqStopDtVal"));
        lblChqRevokeDtVal.setText(resourceBundle.getString("lblChqRevokeDtVal"));
        lblChqStopDt.setText(resourceBundle.getString("lblChqStopDt"));
        lblChqRevokeDt.setText(resourceBundle.getString("lblChqRevokeDt"));
        lblAccounNumber.setText(resourceBundle.getString("lblAccounNumber"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        //        lblUnpaidChequesValue.setText(resourceBundle.getString("lblUnpaidChequesValue"));
        //        lblAvailableBalance.setText(resourceBundle.getString("lblAvailableBalance"));
        lblSeriesNoTo.setText(resourceBundle.getString("lblSeriesNoTo"));
//        lblCustName.setText(resourceBundle.getString("lblCustName"));
        //        lblChequeIssued.setText(resourceBundle.getString("lblChequeIssued"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        rdoLeaf_Bulk.setText(resourceBundle.getString("rdoLeaf_Bulk"));
        //        lblTotalBalance.setText(resourceBundle.getString("lblTotalBalance"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
//        lblAccHeadId.setText(resourceBundle.getString("lblAccHeadId"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblChequeDate.setText(resourceBundle.getString("lblChequeDate"));
        //        lblUnpaidCheques.setText(resourceBundle.getString("lblUnpaidCheques"));
        lblCustomerID.setText(resourceBundle.getString("lblCustomerID"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblNoOfLeaves.setText(resourceBundle.getString("lblNoOfLeaves"));
        lblCustId.setText(resourceBundle.getString("lblCustId"));
        btnPaymentRevoke.setText(resourceBundle.getString("btnPaymentRevoke"));
        //        lblTotalBalanceValue.setText(resourceBundle.getString("lblTotalBalanceValue"));
        lblAccountHeadDesc.setText(resourceBundle.getString("lblAccountHeadDesc"));
//        lblCustomName.setText(resourceBundle.getString("lblCustomName"));
        //        lblChequeIssuedValue.setText(resourceBundle.getString("lblChequeIssuedValue"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblSpaces.setText(resourceBundle.getString("lblSpaces"));
        lblCustomNameValue.setText(resourceBundle.getString("lblCustomNameValue"));
        lblStartChequeNo.setText(resourceBundle.getString("lblStartChequeNo"));
        lblProductID.setText(resourceBundle.getString("lblProductID"));
        lblStartingCheque.setText(resourceBundle.getString("lblStartingCheque"));
        lblCustomerIdDesc.setText(resourceBundle.getString("lblCustomerIdDesc"));
        lblAccHdId.setText(resourceBundle.getString("lblAccHdId"));
        lblAccHdDesc.setText(resourceBundle.getString("lblAccHdDesc"));
        lblRemark.setText(resourceBundle.getString("lblRemark"));
        lblAccHd.setText(resourceBundle.getString("lblAccHd"));
        lblAccNo.setText(resourceBundle.getString("lblAccNo"));
        lblSeriesNoFrom.setText(resourceBundle.getString("lblSeriesNoFrom"));
        ((javax.swing.border.TitledBorder)panPaymentIssues.getBorder()).setTitle(resourceBundle.getString("panPaymentIssues"));
        lblMethodOfDelivery.setText(resourceBundle.getString("lblMethodOfDelivery"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblAccHead.setText(resourceBundle.getString("lblAccHead"));
        lblAccountNo.setText(resourceBundle.getString("lblAccountNo"));
        lblStopPaymentCharges.setText(resourceBundle.getString("lblStopPaymentCharges"));
        btnAccountNo.setText(resourceBundle.getString("btnAccountNo"));
        lblAccHeadDesc.setText(resourceBundle.getString("lblAccHeadDesc"));
        lblAccountHead.setText(resourceBundle.getString("lblAccountHead"));
        lblCustomerNameValue.setText(resourceBundle.getString("lblCustomerNameValue"));
        lblEndingCheque.setText(resourceBundle.getString("lblEndingCheque"));
        rdoLeaf_single.setText(resourceBundle.getString("rdoLeaf_single"));
        lblStatus1.setText(resourceBundle.getString("lblStatus1"));
        lblCustIdDesc.setText(resourceBundle.getString("lblCustIdDesc"));
        btnAccountNumber.setText(resourceBundle.getString("btnAccountNumber"));
        ((javax.swing.border.TitledBorder)panChequeDetails.getBorder()).setTitle(resourceBundle.getString("panChequeDetails"));
        lblPayeeName.setText(resourceBundle.getString("lblPayeeName"));
        lblProductId.setText(resourceBundle.getString("lblProductId"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblCustNameValue.setText(resourceBundle.getString("lblCustNameValue"));
        lblChargesCollected.setText(resourceBundle.getString("lblChargesCollected"));
        btnAccNo.setText(resourceBundle.getString("btnAccNo"));
        //        lblAvailableBalanceValue.setText(resourceBundle.getString("lblAvailableBalanceValue"));
        //        lblChequesReturnedValue.setText(resourceBundle.getString("lblChequesReturnedValue"));
        lblRemarks.setText(resourceBundle.getString("lblRemarks"));
        lblCustomerName.setText(resourceBundle.getString("lblCustomerName"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        //        lblChequesReturned.setText(resourceBundle.getString("lblChequesReturned"));
        lblCustomerIDValue.setText(resourceBundle.getString("lblCustomerIDValue"));
        lblNamesOfAccount.setText(resourceBundle.getString("lblNamesOfAccount"));
        lblEndChequeNo.setText(resourceBundle.getString("lblEndChequeNo"));
        lblNoOfChequeBooks.setText(resourceBundle.getString("lblNoOfChequeBooks"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblLeaf.setText(resourceBundle.getString("lblLeaf"));
        lblProdId.setText(resourceBundle.getString("lblProdId"));
        lblReasonStopPayment.setText(resourceBundle.getString("lblReasonStopPayment"));
        lblLeafNo.setText(resourceBundle.getString("lblLeafNo"));
        lblChequeAmt.setText(resourceBundle.getString("lblChequeAmt"));
        lblAccountHeadId.setText(resourceBundle.getString("lblAccountHeadId"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblCustomerId.setText(resourceBundle.getString("lblCustomerId"));
        lblUsage.setText(resourceBundle.getString("lblUsage"));
        
        lblProdType.setText(resourceBundle.getString("lblProdType"));
        lblProductType.setText(resourceBundle.getString("lblProductType"));
        lblProduct_Type.setText(resourceBundle.getString("lblProduct_Type"));
    }
    
    
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboProdId", new Boolean(true));
        mandatoryMap.put("txtAccountNo", new Boolean(true));
        mandatoryMap.put("txtNamesOfAccount", new Boolean(true));
        mandatoryMap.put("cboMethodOfDelivery", new Boolean(true));
        mandatoryMap.put("cboNoOfLeaves", new Boolean(true));
        mandatoryMap.put("txtNoOfChequeBooks", new Boolean(true));
        mandatoryMap.put("txtChargesCollected", new Boolean(true));
        mandatoryMap.put("txtRemarks", new Boolean(true));
        mandatoryMap.put("txtSeriesFrom", new Boolean(true));
        mandatoryMap.put("txtSeriesNoTo", new Boolean(true));
        mandatoryMap.put("txtStartingCheque", new Boolean(true));
        mandatoryMap.put("txtStartingChequeNo", new Boolean(true));
        mandatoryMap.put("txtEndingCheque", new Boolean(true));
        mandatoryMap.put("txtEndingchequeNo", new Boolean(true));
        mandatoryMap.put("cboProductId", new Boolean(true));
        mandatoryMap.put("txtAccNo", new Boolean(true));
        mandatoryMap.put("txtStartCheque", new Boolean(true));
        mandatoryMap.put("txtStartchequeNo", new Boolean(true));
        mandatoryMap.put("txtEndCheque", new Boolean(true));
        mandatoryMap.put("txtEndingChequeNo", new Boolean(true));
        mandatoryMap.put("rdoLeaf", new Boolean(true));
        mandatoryMap.put("tdtChequeDate", new Boolean(true));
        mandatoryMap.put("txtChequeAmt", new Boolean(true));
        mandatoryMap.put("txtPayeeName", new Boolean(true));
        mandatoryMap.put("txtStopPaymentCharges", new Boolean(true));
        mandatoryMap.put("cboReasonStopPayment", new Boolean(true));
        mandatoryMap.put("cboProductID", new Boolean(true));
        mandatoryMap.put("txtAccounNumber", new Boolean(true));
        mandatoryMap.put("txtLeafNo1", new Boolean(true));
        mandatoryMap.put("txtLeafNo2", new Boolean(true));
        mandatoryMap.put("txtRemark", new Boolean(true));
        mandatoryMap.put("cboUsage", new Boolean(true));
        
        mandatoryMap.put("cboProdType", new Boolean(true));
        mandatoryMap.put("cboProductType", new Boolean(true));
        mandatoryMap.put("cboProduct_Type", new Boolean(true));
        
        mandatoryMap.put("cboEcsProductType", new Boolean(true));
        mandatoryMap.put("cboEcsProductId", new Boolean(true));
        mandatoryMap.put("txtEcsAccNo", new Boolean(true));
        mandatoryMap.put("txtEndEcs1", new Boolean(true));
        mandatoryMap.put("txtEndEcs2", new Boolean(true));
        mandatoryMap.put("cboEcsReasonStopPayment", new Boolean(true));
        
    }
    
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    // To Reset the Radio Buttons in the UI after any operation, We've to
    //1. Remove the Radio Buttons fron the Radio Groups...
    //2. Add the Radio Buttons Back in The Radio Groups...
    //a.) To Remove the Radio buttons...
    private void removeRadioButtons() {
        rdoLeaf.remove(rdoLeaf_single);
        rdoLeaf.remove(rdoLeaf_Bulk);
    }
    
    // b.) To Add the Radio buttons...
    private void addRadioButtons() {
        
        rdoLeaf = new CButtonGroup();
        rdoLeaf.add(rdoLeaf_single);
        rdoLeaf.add(rdoLeaf_Bulk);
    }
    
    
    public void update(Observable observed, Object arg) {
        removeRadioButtons();
        if(pan == CHEQUE || panEditDelete == CHEQUE){
            //            System.out.println("in Update: " + "CHEQUE ISSUE");
            
            //            cboProdType.setSelectedItem(observable.getCboProdType());
            cboProdId.setSelectedItem(observable.getCboProdId());
            txtAccountNo.setText(observable.getTxtAccountNo());
            txtNamesOfAccount.setText(observable.getTxtNamesOfAccount());
            cboMethodOfDelivery.setSelectedItem(observable.getCboMethodOfDelivery());
            
            txtNoOfChequeBooks.setText(observable.getTxtNoOfChequeBooks());
            txtChargesCollected.setText(observable.getTxtChargesCollected());
            txtRemarks.setText(observable.getTxtRemarks());
            txtSeriesFrom.setText(observable.getTxtSeriesFrom());
            txtSeriesNoTo.setText(observable.getTxtSeriesNoTo());
            txtStartingCheque.setText(observable.getTxtStartingCheque());
            txtStartingChequeNo.setText(observable.getTxtStartingChequeNo());
            txtEndingCheque.setText(observable.getTxtEndingcheque());
            txtEndingchequeNo.setText(observable.getTxtEndingchequeNo());
            cboUsage.setSelectedItem(observable.getCboUsage());
            cboNoOfLeaves.setSelectedItem(observable.getCboNoOfLeaves());
            
            // to set the values in table...
            tblChequeDetails.setModel(observable.getTblCheque());
            // To set the Value of Account head in "Cheque book Issue"...
            lblAccountHeadId.setText(observable.getLblAccountHeadProdId());
            lblAccountHeadDesc.setText(observable.getLblAccountHeadDesc());
            
            //Cheque Book Issue...
            lblCustomerIdDesc.setText(observable.getLblCuctId());
            lblCustomerNameValue.setText(observable.getLblCustName());
            //            lblAvailableBalanceValue.setText(observable.getLblClearBalance());
            //            lblTotalBalanceValue.setText(observable.getLblTotalBalance());
            
            // To set the Values of Unpaid, Returned, Issued Cheques...
            //            lblUnpaidChequesValue.setText(observable.getLblUnpaidCheques());
            //            lblChequesReturnedValue.setText(observable.getLblChequesReturned());
            //            lblChequeIssuedValue.setText(observable.getLblChequesIssued());
            
        }else if(pan == STOP || panEditDelete == STOP){
            //            System.out.println("in Update: " + "CHEQUE STOP");
            
            //            cboProductType.setSelectedItem(observable.getCboProductType());
            cboProductId.setSelectedItem(observable.getCboProductId());
            txtAccNo.setText(observable.getTxtAccNo());
            txtStartCheque.setText(observable.getTxtStartCheque());
            txtStartchequeNo.setText(observable.getTxtStartchequeNo());
            txtEndCheque.setText(observable.getTxtEndCheque());
            txtEndingChequeNo.setText(observable.getTxtEndingChequeNo());
            rdoLeaf_single.setSelected(observable.getRdoLeaf_single());
            rdoLeaf_singleActionPerformed(null);
            rdoLeaf_Bulk.setSelected(observable.getRdoLeaf_Bulk());
            tdtChequeDate.setDateValue(observable.getTdtChequeDate());
            txtChequeAmt.setText(observable.getTxtChequeAmt());
            txtPayeeName.setText(observable.getTxtPayeeName());
            txtStopPaymentCharges.setText(observable.getTxtStopPaymentCharges());
            cboReasonStopPayment.setSelectedItem(observable.getCboReasonStopPayment());
            
            // To set the Value of Account head in "Stop Payment"...
//            lblAccHeadId.setText(observable.getLblProductIdAccHdId());
            lblAccHeadDesc.setText(observable.getLblProductIdAccHdDesc());
            
            //Stop Payment Issues...
//            lblCustIdDesc.setText(observable.getLblCustStopId());
            lblCustNameValue.setText(observable.getLblCustStopName());
            txtStopReasonForRevoke.setText(observable.getAuthRemarks());
            lblChqRevokeDtVal.setText(observable.getLblChqRevokeDtVal());
            lblChqStopDtVal.setText(observable.getLblChqStopDtVal());
            lblChqStopId.setText(observable.getLblCustStopId());
        }else if(pan == LEAF || panEditDelete == LEAF){
            //            System.out.println("in Update: " + "Loose Leaf");
            
            //            cboProduct_Type.setSelectedItem(observable.getCboProduct_Type());
            cboProductID.setSelectedItem(observable.getCboProductID());
            txtAccounNumber.setText(observable.getTxtAccounNumber());
            txtLeafNo1.setText(observable.getTxtLeafNo1());
            txtLeafNo2.setText(observable.getTxtLeafNo2());
            txtRemark.setText(observable.getTxtRemark());
            
            // To set the Value of Account head in "Loose Leaf"...
            lblAccHdId.setText(observable.getLblProductIDAccHdId());
            lblAccHdDesc.setText(observable.getLblProductIDAccHdDesc());
            
            //Loose Leaf Issue...
            lblCustomerIDValue.setText(observable.getLblCustLooseId());
            lblCustomNameValue.setText(observable.getLblCustLooseName());
        }
        else if(pan == ECS || panEditDelete == ECS){
            cboEcsProductId.setSelectedItem(observable.getCboEcsProdId());
            txtEcsAccNo.setText(observable.getEcsAcctNo());
            txtEcsPayeeName.setText(observable.getEcsPayeeName());
            txtEcsStopPaymentCharges.setText(observable.getEcsStopPayChrg());
            txtEndEcs1.setText(observable.getEcsEndChqNo1());
            txtEndEcsNo2.setText(observable.getEcsEndChqNo2());
            txtEcsAmt.setText(observable.getEcsAmt());
            cboEcsReasonStopPayment.setSelectedItem(observable.getCboEcsReasonStopPayment());
            tdtEcsDate.setDateValue(observable.getEcsDt());
            txtEcsStopReasonForRevoke.setText(observable.getAuthRemarks());
            lblEcsRevokeDtVal.setText(observable.getLblEcsRevokeDtVal());
            lblEcsStopDtVal.setText(observable.getEcsStopDt());
            lblEcsId.setText(observable.getEcsStopId());
        }
        //To set the Status...
        lblStatus1.setText(observable.getLblStatus());
        addRadioButtons();
    }
    
    public void updateOBFields() {
        observable.setScreen(getScreen());
        observable.setModule(getModule());
        observable.setSelectedBranchID(getSelectedBranchID());
        if(pan == CHEQUE || panEditDelete == CHEQUE){
            observable.setCboProdType((String) cboProdType.getSelectedItem());
            observable.setCboProdId((String) cboProdId.getSelectedItem());
            observable.setTxtAccountNo(txtAccountNo.getText());
            observable.setTxtNamesOfAccount(txtNamesOfAccount.getText());
            observable.setCboMethodOfDelivery((String) cboMethodOfDelivery.getSelectedItem());
            observable.setCboNoOfLeaves((String) cboNoOfLeaves.getSelectedItem());
            observable.setTxtNoOfChequeBooks(txtNoOfChequeBooks.getText());
            observable.setTxtChargesCollected(txtChargesCollected.getText());
            observable.setTxtRemarks(txtRemarks.getText());
            observable.setTxtSeriesFrom(txtSeriesFrom.getText());
            observable.setTxtSeriesNoTo(txtSeriesNoTo.getText());
            observable.setTxtStartingCheque(txtStartingCheque.getText());
            observable.setTxtStartingChequeNo(txtStartingChequeNo.getText());
            observable.setTxtEndingcheque(txtEndingCheque.getText());
            observable.setTxtEndingchequeNo(txtEndingchequeNo.getText());
            observable.setCboUsage((String) cboUsage.getSelectedItem());
            //*************************************************************************
            observable.setTblCheque((com.see.truetransact.clientutil.EnhancedTableModel)tblChequeDetails.getModel());
            
            //To set the Value of Account Head, Product-Id and Account Desc in Cheque Book issue...
            observable.setLblAccountHeadProdId(lblAccountHeadId.getText());
            observable.setLblAccountHeadDesc(lblAccountHeadDesc.getText());
            
            //Cheque Book issue...
            observable.setLblCuctId(lblCustomerIdDesc.getText());
            //            observable.setLblClearBalance(lblAvailableBalanceValue.getText());
            //            observable.setLblTotalBalance(lblTotalBalanceValue.getText());
            observable.setLblCustName(lblCustomerNameValue.getText());
            
            // To set the Values of Unpaid, Returned, Issued Cheques...
            //            observable.setLblUnpaidCheques(lblUnpaidChequesValue.getText());
            //            observable.setLblChequesReturned(lblChequesReturnedValue.getText());
            //            observable.setLblChequesIssued(lblChequeIssuedValue.getText());
            
        }else if(pan == STOP || panEditDelete == STOP){
            observable.setCboProductType((String) cboProductType.getSelectedItem());
            observable.setCboProductId((String) cboProductId.getSelectedItem());
            observable.setTxtAccNo(txtAccNo.getText());
            observable.setTxtStartCheque(txtStartCheque.getText());
            observable.setTxtStartchequeNo(txtStartchequeNo.getText());
            observable.setTxtEndCheque(txtEndCheque.getText());
            observable.setTxtEndingChequeNo(txtEndingChequeNo.getText());
            observable.setRdoLeaf_single(rdoLeaf_single.isSelected());
            observable.setRdoLeaf_Bulk(rdoLeaf_Bulk.isSelected());
            observable.setTdtChequeDate(tdtChequeDate.getDateValue());
            observable.setTxtChequeAmt(txtChequeAmt.getText());
            observable.setTxtPayeeName(txtPayeeName.getText());
            observable.setTxtStopPaymentCharges(txtStopPaymentCharges.getText());
            observable.setCboReasonStopPayment((String)cboReasonStopPayment.getSelectedItem());
            
            //To set the Value of Account Head, Product-Id and Account Desc in Stop Payment...
//            observable.setLblProductIdAccHdId(lblAccHeadId.getText());
            observable.setLblProductIdAccHdDesc(lblAccHeadDesc.getText());
            
            //Stop Payment Issue...
            observable.setLblCustStopId(lblCustIdDesc.getText());
            observable.setLblCustStopName(lblCustNameValue.getText());
            observable.setAuthRemarks(txtStopReasonForRevoke.getText());
            
            
        }else if(pan == LEAF || panEditDelete == LEAF){
            observable.setCboProduct_Type((String) cboProduct_Type.getSelectedItem());
            observable.setCboProductID((String) cboProductID.getSelectedItem());
            observable.setTxtAccounNumber(txtAccounNumber.getText());
            observable.setTxtLeafNo1(txtLeafNo1.getText());
            observable.setTxtLeafNo2(txtLeafNo2.getText());
            observable.setTxtRemark(txtRemark.getText());
            //To set the Value of Account Head, Product-Id and Account Desc in Loose Leaf...
            observable.setLblProductIDAccHdId(lblAccHdId.getText());
            observable.setLblProductIDAccHdDesc(lblAccHdDesc.getText());
            //Loose Leaf Issue...
            observable.setLblCustLooseId(lblCustomerIDValue.getText());
            observable.setLblCustLooseName(lblCustomNameValue.getText());
        }
         else if(pan == ECS || panEditDelete == ECS){
                observable.setCboEcsProdType((String)cboEcsProductType.getSelectedItem());
                observable.setCboEcsProdId((String) cboEcsProductId.getSelectedItem());
                observable.setEcsAcctNo(txtEcsAccNo.getText());
                observable.setEcsAmt(txtEcsAmt.getText());
                observable.setEcsEndChqNo1(txtEndEcs1.getText());
                observable.setEcsEndChqNo2(txtEndEcsNo2.getText());
                observable.setEcsPayeeName(txtEcsPayeeName.getText());
                observable.setEcsStopPayChrg(txtEcsStopPaymentCharges.getText());
                observable.setCboEcsReasonStopPayment((String)cboEcsReasonStopPayment.getSelectedItem());
                observable.setEcsDt(tdtEcsDate.getDateValue());
                observable.setAuthRemarks(txtEcsStopReasonForRevoke.getText());
            }
    }
    
    public void setHelpMessage() {
        ChequeBookMRB objMandatoryRB = new ChequeBookMRB();
        cboProdId.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdId"));
        txtAccountNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccountNo"));
        txtNamesOfAccount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNamesOfAccount"));
        cboMethodOfDelivery.setHelpMessage(lblMsg, objMandatoryRB.getString("cboMethodOfDelivery"));
        cboNoOfLeaves.setHelpMessage(lblMsg, objMandatoryRB.getString("cboNoOfLeaves"));
        txtNoOfChequeBooks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoOfChequeBooks"));
        txtChargesCollected.setHelpMessage(lblMsg, objMandatoryRB.getString("txtChargesCollected"));
        txtRemarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRemarks"));
        txtSeriesFrom.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSeriesFrom"));
        txtSeriesNoTo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSeriesNoTo"));
        txtStartingCheque.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStartingCheque"));
        txtStartingChequeNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStartingChequeNo"));
        txtEndingCheque.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEndingCheque"));
        txtEndingchequeNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEndingchequeNo"));
        cboProductId.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProductId"));
        txtAccNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccNo"));
        txtStartCheque.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStartCheque"));
        txtStartchequeNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStartchequeNo"));
        txtEndCheque.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEndCheque"));
        txtEndingChequeNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEndingChequeNo"));
        rdoLeaf_single.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoLeaf_single"));
        tdtChequeDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtChequeDate"));
        txtChequeAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtChequeAmt"));
        txtPayeeName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPayeeName"));
        txtStopPaymentCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStopPaymentCharges"));
        cboReasonStopPayment.setHelpMessage(lblMsg, objMandatoryRB.getString("cboReasonStopPayment"));
        cboProductID.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProductID"));
        txtAccounNumber.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccounNumber"));
        txtLeafNo1.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLeafNo1"));
        txtLeafNo2.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLeafNo2"));
        txtRemark.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRemark"));
        cboUsage.setHelpMessage(lblMsg, objMandatoryRB.getString("cboUsage"));
        
        cboEcsProductType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboEcsProductType"));
        cboProductType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProductType"));
        cboProduct_Type.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProduct_Type"));
        
        cboEcsProductId.setHelpMessage(lblMsg, objMandatoryRB.getString("cboEcsProductId"));
        txtEcsAccNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEcsAccNo"));
        txtEndEcsNo2.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEndEcs2"));
        txtEndEcs1.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEndEcs1"));
        cboEcsReasonStopPayment.setHelpMessage(lblMsg, objMandatoryRB.getString("cboEcsReasonStopPayment"));
    }
    
    // To fill the Data into the Combo Boxes...
    // it invokes the Combo Box model defined in OB class...
    private void initComponentData() {
        cboProdType.setModel(observable.getCbmProdType());
        cboProductType.setModel(observable.getCbmProductType());
        cboProduct_Type.setModel(observable.getCbmProduct_Type());
        
        cboMethodOfDelivery.setModel(observable.getCbmMethodOfDelivery());
        cboUsage.setModel(observable.getCbmUsage());
        
        cboReasonStopPayment.setModel(observable.getCbmReasonStopPayment());
             
        cboEcsProductType.setModel(observable.getCbmEcsProdType());
        cboEcsReasonStopPayment.setModel(observable.getCbmEcsReasonForStopPayment());
        //        cboProdId.setModel(observable.getCbmProdId());
        //        cboProductId.setModel(observable.getCbmProductId());
        //        cboProductID.setModel(observable.getCbmProductID());
    }
    
    //To set the length of the Text Fields and Numeric Validation...
    private void setMaxLenths() {
        //        txtNoOfLeaves.setMaxLength(3);
        //        txtNoOfLeaves.setValidation(new NumericValidation());
        txtAccountNo.setAllowAll(true);
        txtAccNo.setAllowAll(true);
        txtAccounNumber.setAllowAll(true);
        txtNoOfChequeBooks.setMaxLength(3);
        txtNoOfChequeBooks.setValidation(new NumericValidation());
        
        txtStartingCheque.setMaxLength(ClientConstants.INSTRUMENT_NO1);
        txtStartingChequeNo.setMaxLength(ClientConstants.INSTRUMENT_NO2);
        txtStartingChequeNo.setValidation(new NumericValidation());
        
        txtEndingCheque.setMaxLength(ClientConstants.INSTRUMENT_NO1);
        txtEndingchequeNo.setMaxLength(ClientConstants.INSTRUMENT_NO2);
        txtEndingchequeNo.setValidation(new NumericValidation());
        
        txtSeriesFrom.setMaxLength(ClientConstants.CHEQUE_SERIES);
        txtSeriesFrom.setValidation(new NumericValidation());
        
        txtSeriesNoTo.setMaxLength(ClientConstants.CHEQUE_SERIES);
        txtSeriesNoTo.setValidation(new NumericValidation());
        
        txtChargesCollected.setMaxLength(16);
        txtChargesCollected.setValidation(new CurrencyValidation());
        
        txtRemarks.setMaxLength(256);
        txtNamesOfAccount.setMaxLength(128);
        
        // Stop Payment...
        txtStartCheque.setMaxLength(ClientConstants.INSTRUMENT_NO1);
        txtStartchequeNo.setMaxLength(ClientConstants.INSTRUMENT_NO2);
        txtStartchequeNo.setValidation(new NumericValidation());
        
        txtEndCheque.setMaxLength(ClientConstants.INSTRUMENT_NO1);
        txtEndingChequeNo.setMaxLength(ClientConstants.INSTRUMENT_NO2);
        txtEndingChequeNo.setValidation(new NumericValidation());
        
        txtPayeeName.setMaxLength(64);
        
        txtChequeAmt.setMaxLength(16);
        txtChequeAmt.setValidation(new NumericValidation());
        
        txtStopPaymentCharges.setMaxLength(16);
        txtStopPaymentCharges.setValidation(new CurrencyValidation());
        
//        cboReasonStopPayment.setMaxLength(256);
        
        //Loose Leaf...
        txtLeafNo1.setMaxLength(ClientConstants.INSTRUMENT_NO1);
        txtLeafNo2.setMaxLength(ClientConstants.INSTRUMENT_NO2);
        txtLeafNo2.setValidation(new NumericValidation());
        
        txtRemark.setMaxLength(256);
        txtEndEcs1.setAllowAll(true);
        txtEndEcsNo2.setAllowAll(true);
        txtEcsAmt.setValidation(new CurrencyValidation());
        txtEcsStopPaymentCharges.setValidation(new CurrencyValidation());
    }
    
    // To set The Value of the Buttons Depending on the Value or Condition...
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

        rdoLeaf = new com.see.truetransact.uicomponent.CButtonGroup();
        panChequeBook = new com.see.truetransact.uicomponent.CPanel();
        lblSpaces = new com.see.truetransact.uicomponent.CLabel();
        tabChequeBook = new com.see.truetransact.uicomponent.CTabbedPane();
        panChequeBookIssue = new com.see.truetransact.uicomponent.CPanel();
        panProductData = new com.see.truetransact.uicomponent.CPanel();
        panProduct = new com.see.truetransact.uicomponent.CPanel();
        lblProdId = new com.see.truetransact.uicomponent.CLabel();
        cboProdId = new com.see.truetransact.uicomponent.CComboBox();
        lblAccountNo = new com.see.truetransact.uicomponent.CLabel();
        panAccountNo = new com.see.truetransact.uicomponent.CPanel();
        txtAccountNo = new com.see.truetransact.uicomponent.CTextField();
        btnAccountNo = new com.see.truetransact.uicomponent.CButton();
        lblNamesOfAccount = new com.see.truetransact.uicomponent.CLabel();
        txtNamesOfAccount = new com.see.truetransact.uicomponent.CTextField();
        lblMethodOfDelivery = new com.see.truetransact.uicomponent.CLabel();
        cboMethodOfDelivery = new com.see.truetransact.uicomponent.CComboBox();
        lblNoOfLeaves = new com.see.truetransact.uicomponent.CLabel();
        lblNoOfChequeBooks = new com.see.truetransact.uicomponent.CLabel();
        lblChargesCollected = new com.see.truetransact.uicomponent.CLabel();
        txtChargesCollected = new com.see.truetransact.uicomponent.CTextField();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        lblUsage = new com.see.truetransact.uicomponent.CLabel();
        cboUsage = new com.see.truetransact.uicomponent.CComboBox();
        txtNoOfChequeBooks = new com.see.truetransact.uicomponent.CTextField();
        cboNoOfLeaves = new com.see.truetransact.uicomponent.CComboBox();
        lblProdType = new com.see.truetransact.uicomponent.CLabel();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        sptProductData = new com.see.truetransact.uicomponent.CSeparator();
        panData = new com.see.truetransact.uicomponent.CPanel();
        lblAccountHead = new com.see.truetransact.uicomponent.CLabel();
        lblAccountHeadId = new com.see.truetransact.uicomponent.CLabel();
        lblAccountHeadDesc = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerId = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerIdDesc = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerName = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerNameValue = new com.see.truetransact.uicomponent.CLabel();
        panTransDetails = new com.see.truetransact.uicomponent.CPanel();
        sptChequeIssue = new com.see.truetransact.uicomponent.CSeparator();
        panChequeDetails = new com.see.truetransact.uicomponent.CPanel();
        panSeriesNo = new com.see.truetransact.uicomponent.CPanel();
        lblSeriesNoFrom = new com.see.truetransact.uicomponent.CLabel();
        txtSeriesFrom = new com.see.truetransact.uicomponent.CTextField();
        lblSeriesNoTo = new com.see.truetransact.uicomponent.CLabel();
        txtSeriesNoTo = new com.see.truetransact.uicomponent.CTextField();
        lblStartingCheque = new com.see.truetransact.uicomponent.CLabel();
        panStartingCheque = new com.see.truetransact.uicomponent.CPanel();
        txtStartingCheque = new com.see.truetransact.uicomponent.CTextField();
        txtStartingChequeNo = new com.see.truetransact.uicomponent.CTextField();
        lblEndingCheque = new com.see.truetransact.uicomponent.CLabel();
        panEndingCheque = new com.see.truetransact.uicomponent.CPanel();
        txtEndingCheque = new com.see.truetransact.uicomponent.CTextField();
        txtEndingchequeNo = new com.see.truetransact.uicomponent.CTextField();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        srpChequeDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblChequeDetails = new com.see.truetransact.uicomponent.CTable();
        panStopPaymentIssue = new com.see.truetransact.uicomponent.CPanel();
        panPaymentIssues = new com.see.truetransact.uicomponent.CPanel();
        panCustomer = new com.see.truetransact.uicomponent.CPanel();
        lblProductId = new com.see.truetransact.uicomponent.CLabel();
        cboProductId = new com.see.truetransact.uicomponent.CComboBox();
        lblAccNo = new com.see.truetransact.uicomponent.CLabel();
        panAccNo = new com.see.truetransact.uicomponent.CPanel();
        txtAccNo = new com.see.truetransact.uicomponent.CTextField();
        btnAccNo = new com.see.truetransact.uicomponent.CButton();
        lblCustId = new com.see.truetransact.uicomponent.CLabel();
        lblCustIdDesc = new com.see.truetransact.uicomponent.CLabel();
        lblCustNameValue = new com.see.truetransact.uicomponent.CLabel();
        lblStartChequeNo = new com.see.truetransact.uicomponent.CLabel();
        panStartChequeNo = new com.see.truetransact.uicomponent.CPanel();
        txtStartCheque = new com.see.truetransact.uicomponent.CTextField();
        txtStartchequeNo = new com.see.truetransact.uicomponent.CTextField();
        lblEndChequeNo = new com.see.truetransact.uicomponent.CLabel();
        panEndChequeNo = new com.see.truetransact.uicomponent.CPanel();
        txtEndCheque = new com.see.truetransact.uicomponent.CTextField();
        txtEndingChequeNo = new com.see.truetransact.uicomponent.CTextField();
        lblLeaf = new com.see.truetransact.uicomponent.CLabel();
        panLeaf = new com.see.truetransact.uicomponent.CPanel();
        rdoLeaf_single = new com.see.truetransact.uicomponent.CRadioButton();
        rdoLeaf_Bulk = new com.see.truetransact.uicomponent.CRadioButton();
        lblProductType = new com.see.truetransact.uicomponent.CLabel();
        cboProductType = new com.see.truetransact.uicomponent.CComboBox();
        lblChqStopId = new com.see.truetransact.uicomponent.CLabel();
        sptPaymentIssues = new com.see.truetransact.uicomponent.CSeparator();
        panCheque = new com.see.truetransact.uicomponent.CPanel();
        lblChequeDate = new com.see.truetransact.uicomponent.CLabel();
        tdtChequeDate = new com.see.truetransact.uicomponent.CDateField();
        lblChequeAmt = new com.see.truetransact.uicomponent.CLabel();
        txtChequeAmt = new com.see.truetransact.uicomponent.CTextField();
        lblPayeeName = new com.see.truetransact.uicomponent.CLabel();
        txtPayeeName = new com.see.truetransact.uicomponent.CTextField();
        lblStopPaymentCharges = new com.see.truetransact.uicomponent.CLabel();
        txtStopPaymentCharges = new com.see.truetransact.uicomponent.CTextField();
        lblReasonStopPayment = new com.see.truetransact.uicomponent.CLabel();
        cboReasonStopPayment = new com.see.truetransact.uicomponent.CComboBox();
        lblAccHeadDesc = new com.see.truetransact.uicomponent.CLabel();
        lblAccHead = new com.see.truetransact.uicomponent.CLabel();
        lblReasonForRevoke = new com.see.truetransact.uicomponent.CLabel();
        txtStopReasonForRevoke = new com.see.truetransact.uicomponent.CTextField();
        lblChqStopDt = new com.see.truetransact.uicomponent.CLabel();
        lblChqStopDtVal = new com.see.truetransact.uicomponent.CLabel();
        lblChqRevokeDtVal = new com.see.truetransact.uicomponent.CLabel();
        lblChqRevokeDt = new com.see.truetransact.uicomponent.CLabel();
        panPaymentRevoke = new com.see.truetransact.uicomponent.CPanel();
        btnPaymentRevoke = new com.see.truetransact.uicomponent.CButton();
        panLooseLeafIssue = new com.see.truetransact.uicomponent.CPanel();
        panProdLeaf = new com.see.truetransact.uicomponent.CPanel();
        lblProductID = new com.see.truetransact.uicomponent.CLabel();
        cboProductID = new com.see.truetransact.uicomponent.CComboBox();
        lblAccHd = new com.see.truetransact.uicomponent.CLabel();
        panLooseAccountHead = new com.see.truetransact.uicomponent.CPanel();
        lblAccHdId = new com.see.truetransact.uicomponent.CLabel();
        lblAccHdDesc = new com.see.truetransact.uicomponent.CLabel();
        lblAccounNumber = new com.see.truetransact.uicomponent.CLabel();
        panAccounNumber = new com.see.truetransact.uicomponent.CPanel();
        txtAccounNumber = new com.see.truetransact.uicomponent.CTextField();
        btnAccountNumber = new com.see.truetransact.uicomponent.CButton();
        lblCustomerID = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerIDValue = new com.see.truetransact.uicomponent.CLabel();
        lblCustomNameValue = new com.see.truetransact.uicomponent.CLabel();
        lblLeafNo = new com.see.truetransact.uicomponent.CLabel();
        panLeafNo = new com.see.truetransact.uicomponent.CPanel();
        txtLeafNo1 = new com.see.truetransact.uicomponent.CTextField();
        txtLeafNo2 = new com.see.truetransact.uicomponent.CTextField();
        lblRemark = new com.see.truetransact.uicomponent.CLabel();
        txtRemark = new com.see.truetransact.uicomponent.CTextField();
        lblProduct_Type = new com.see.truetransact.uicomponent.CLabel();
        cboProduct_Type = new com.see.truetransact.uicomponent.CComboBox();
        panEcsStopPaymentIssue = new com.see.truetransact.uicomponent.CPanel();
        panPaymentIssues1 = new com.see.truetransact.uicomponent.CPanel();
        panCustomer1 = new com.see.truetransact.uicomponent.CPanel();
        lblEcsProductId = new com.see.truetransact.uicomponent.CLabel();
        cboEcsProductId = new com.see.truetransact.uicomponent.CComboBox();
        lblEcsAccNo = new com.see.truetransact.uicomponent.CLabel();
        panAccNo1 = new com.see.truetransact.uicomponent.CPanel();
        txtEcsAccNo = new com.see.truetransact.uicomponent.CTextField();
        btnEcsAccNo = new com.see.truetransact.uicomponent.CButton();
        lblEcsCustId = new com.see.truetransact.uicomponent.CLabel();
        lblCustIdDesc1 = new com.see.truetransact.uicomponent.CLabel();
        lblCustNameValue1 = new com.see.truetransact.uicomponent.CLabel();
        panStartChequeNo1 = new com.see.truetransact.uicomponent.CPanel();
        lblEcsNo = new com.see.truetransact.uicomponent.CLabel();
        panEndEcsNo1 = new com.see.truetransact.uicomponent.CPanel();
        txtEndEcs1 = new com.see.truetransact.uicomponent.CTextField();
        txtEndEcsNo2 = new com.see.truetransact.uicomponent.CTextField();
        lblEcsProductType = new com.see.truetransact.uicomponent.CLabel();
        cboEcsProductType = new com.see.truetransact.uicomponent.CComboBox();
        lblEcsId = new com.see.truetransact.uicomponent.CLabel();
        sptPaymentIssues1 = new com.see.truetransact.uicomponent.CSeparator();
        panCheque1 = new com.see.truetransact.uicomponent.CPanel();
        lblEcsDate = new com.see.truetransact.uicomponent.CLabel();
        tdtEcsDate = new com.see.truetransact.uicomponent.CDateField();
        lblEcsAmt = new com.see.truetransact.uicomponent.CLabel();
        txtEcsAmt = new com.see.truetransact.uicomponent.CTextField();
        lblEcsPayeeName = new com.see.truetransact.uicomponent.CLabel();
        txtEcsPayeeName = new com.see.truetransact.uicomponent.CTextField();
        lblEcsStopPaymentCharges = new com.see.truetransact.uicomponent.CLabel();
        txtEcsStopPaymentCharges = new com.see.truetransact.uicomponent.CTextField();
        lblEcsReasonStopPayment = new com.see.truetransact.uicomponent.CLabel();
        cboEcsReasonStopPayment = new com.see.truetransact.uicomponent.CComboBox();
        lblEcsReasonForRevoke = new com.see.truetransact.uicomponent.CLabel();
        txtEcsStopReasonForRevoke = new com.see.truetransact.uicomponent.CTextField();
        lblEcsRevokeDt = new com.see.truetransact.uicomponent.CLabel();
        lblEcsRevokeDtVal = new com.see.truetransact.uicomponent.CLabel();
        lblEcsStopDt = new com.see.truetransact.uicomponent.CLabel();
        lblEcsStopDtVal = new com.see.truetransact.uicomponent.CLabel();
        panPaymentRevoke1 = new com.see.truetransact.uicomponent.CPanel();
        btnEcsPaymentRevoke = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus1 = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        tbrLoantProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
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
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace22 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        mbrLoanProduct = new com.see.truetransact.uicomponent.CMenuBar();
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
        setMinimumSize(new java.awt.Dimension(800, 650));
        setPreferredSize(new java.awt.Dimension(800, 650));

        panChequeBook.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panChequeBook.setLayout(new java.awt.GridBagLayout());

        lblSpaces.setMinimumSize(new java.awt.Dimension(3, 15));
        lblSpaces.setPreferredSize(new java.awt.Dimension(3, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panChequeBook.add(lblSpaces, gridBagConstraints);

        panChequeBookIssue.setLayout(new java.awt.GridBagLayout());

        panProductData.setLayout(new java.awt.GridBagLayout());

        panProduct.setLayout(new java.awt.GridBagLayout());

        lblProdId.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProduct.add(lblProdId, gridBagConstraints);

        cboProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdId.setPopupWidth(215);
        cboProdId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProduct.add(cboProdId, gridBagConstraints);

        lblAccountNo.setText("Account No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProduct.add(lblAccountNo, gridBagConstraints);

        panAccountNo.setLayout(new java.awt.GridBagLayout());

        txtAccountNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccountNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAccountNoActionPerformed(evt);
            }
        });
        txtAccountNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccountNoFocusLost(evt);
            }
        });
        panAccountNo.add(txtAccountNo, new java.awt.GridBagConstraints());

        btnAccountNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccountNo.setToolTipText("Account No");
        btnAccountNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccountNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccountNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountNo.add(btnAccountNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProduct.add(panAccountNo, gridBagConstraints);

        lblNamesOfAccount.setText("Names of Account");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProduct.add(lblNamesOfAccount, gridBagConstraints);

        txtNamesOfAccount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProduct.add(txtNamesOfAccount, gridBagConstraints);

        lblMethodOfDelivery.setText("Method of Delivery");
        lblMethodOfDelivery.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProduct.add(lblMethodOfDelivery, gridBagConstraints);

        cboMethodOfDelivery.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProduct.add(cboMethodOfDelivery, gridBagConstraints);

        lblNoOfLeaves.setText("No. of Leaves");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProduct.add(lblNoOfLeaves, gridBagConstraints);

        lblNoOfChequeBooks.setText("No. of Cheque Books");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProduct.add(lblNoOfChequeBooks, gridBagConstraints);

        lblChargesCollected.setText("Charges Collected");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProduct.add(lblChargesCollected, gridBagConstraints);

        txtChargesCollected.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProduct.add(txtChargesCollected, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProduct.add(lblRemarks, gridBagConstraints);

        txtRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProduct.add(txtRemarks, gridBagConstraints);

        lblUsage.setText("Usage");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProduct.add(lblUsage, gridBagConstraints);

        cboUsage.setMinimumSize(new java.awt.Dimension(100, 21));
        cboUsage.setPopupWidth(150);
        cboUsage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboUsageActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProduct.add(cboUsage, gridBagConstraints);

        txtNoOfChequeBooks.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNoOfChequeBooks.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNoOfChequeBooksFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProduct.add(txtNoOfChequeBooks, gridBagConstraints);

        cboNoOfLeaves.setMinimumSize(new java.awt.Dimension(100, 21));
        cboNoOfLeaves.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboNoOfLeavesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProduct.add(cboNoOfLeaves, gridBagConstraints);

        lblProdType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProduct.add(lblProdType, gridBagConstraints);

        cboProdType.setPopupWidth(110);
        cboProdType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboProdTypeItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProduct.add(cboProdType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductData.add(panProduct, gridBagConstraints);

        sptProductData.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductData.add(sptProductData, gridBagConstraints);

        panData.setLayout(new java.awt.GridBagLayout());

        lblAccountHead.setText("Account Head");
        lblAccountHead.setMaximumSize(new java.awt.Dimension(85, 18));
        lblAccountHead.setMinimumSize(new java.awt.Dimension(85, 18));
        lblAccountHead.setPreferredSize(new java.awt.Dimension(85, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblAccountHead, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblAccountHeadId, gridBagConstraints);

        lblAccountHeadDesc.setPreferredSize(new java.awt.Dimension(120, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblAccountHeadDesc, gridBagConstraints);

        lblCustomerId.setText("Customer Id");
        lblCustomerId.setMaximumSize(new java.awt.Dimension(73, 18));
        lblCustomerId.setMinimumSize(new java.awt.Dimension(73, 18));
        lblCustomerId.setPreferredSize(new java.awt.Dimension(73, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblCustomerId, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblCustomerIdDesc, gridBagConstraints);

        lblCustomerName.setText("Customer Name");
        lblCustomerName.setMaximumSize(new java.awt.Dimension(96, 18));
        lblCustomerName.setMinimumSize(new java.awt.Dimension(96, 18));
        lblCustomerName.setPreferredSize(new java.awt.Dimension(96, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblCustomerName, gridBagConstraints);

        lblCustomerNameValue.setForeground(new java.awt.Color(0, 51, 204));
        lblCustomerNameValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblCustomerNameValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductData.add(panData, gridBagConstraints);

        panTransDetails.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductData.add(panTransDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panChequeBookIssue.add(panProductData, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChequeBookIssue.add(sptChequeIssue, gridBagConstraints);

        panChequeDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Cheque Book"));
        panChequeDetails.setLayout(new java.awt.GridBagLayout());

        panSeriesNo.setLayout(new java.awt.GridBagLayout());

        lblSeriesNoFrom.setText("Series No.From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSeriesNo.add(lblSeriesNoFrom, gridBagConstraints);

        txtSeriesFrom.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSeriesNo.add(txtSeriesFrom, gridBagConstraints);

        lblSeriesNoTo.setText("Series No.To");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSeriesNo.add(lblSeriesNoTo, gridBagConstraints);

        txtSeriesNoTo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSeriesNo.add(txtSeriesNoTo, gridBagConstraints);

        lblStartingCheque.setText("Starting Cheque No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSeriesNo.add(lblStartingCheque, gridBagConstraints);

        panStartingCheque.setLayout(new java.awt.GridBagLayout());

        txtStartingCheque.setMinimumSize(new java.awt.Dimension(50, 21));
        txtStartingCheque.setPreferredSize(new java.awt.Dimension(50, 21));
        txtStartingCheque.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtStartingChequeFocusLost(evt);
            }
        });
        panStartingCheque.add(txtStartingCheque, new java.awt.GridBagConstraints());

        txtStartingChequeNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtStartingChequeNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtStartingChequeNoFocusLost(evt);
            }
        });
        panStartingCheque.add(txtStartingChequeNo, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSeriesNo.add(panStartingCheque, gridBagConstraints);

        lblEndingCheque.setText("Ending Cheque No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSeriesNo.add(lblEndingCheque, gridBagConstraints);

        panEndingCheque.setLayout(new java.awt.GridBagLayout());

        txtEndingCheque.setMinimumSize(new java.awt.Dimension(50, 21));
        txtEndingCheque.setPreferredSize(new java.awt.Dimension(50, 21));
        panEndingCheque.add(txtEndingCheque, new java.awt.GridBagConstraints());

        txtEndingchequeNo.setMinimumSize(new java.awt.Dimension(100, 21));
        panEndingCheque.add(txtEndingchequeNo, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSeriesNo.add(panEndingCheque, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChequeDetails.add(panSeriesNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panChequeBookIssue.add(panChequeDetails, gridBagConstraints);

        panTable.setLayout(new java.awt.GridBagLayout());

        srpChequeDetails.setMinimumSize(new java.awt.Dimension(380, 140));
        srpChequeDetails.setPreferredSize(new java.awt.Dimension(480, 140));

        tblChequeDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Issue Date", "Series No.", "Serial No.", "Starting Cheque No.", "Ending Cheque No."
            }
        ));
        tblChequeDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(415, 140));
        tblChequeDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblChequeDetailsMousePressed(evt);
            }
        });
        srpChequeDetails.setViewportView(tblChequeDetails);

        panTable.add(srpChequeDetails, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChequeBookIssue.add(panTable, gridBagConstraints);

        tabChequeBook.addTab("Cheque Book Issue", panChequeBookIssue);

        panStopPaymentIssue.setLayout(new java.awt.GridBagLayout());

        panPaymentIssues.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panPaymentIssues.setLayout(new java.awt.GridBagLayout());

        panCustomer.setLayout(new java.awt.GridBagLayout());

        lblProductId.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomer.add(lblProductId, gridBagConstraints);

        cboProductId.setPopupWidth(215);
        cboProductId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomer.add(cboProductId, gridBagConstraints);

        lblAccNo.setText("Account No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomer.add(lblAccNo, gridBagConstraints);

        panAccNo.setLayout(new java.awt.GridBagLayout());

        txtAccNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAccNoActionPerformed(evt);
            }
        });
        txtAccNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccNo.add(txtAccNo, gridBagConstraints);

        btnAccNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccNo.setToolTipText("Account No");
        btnAccNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccNo.add(btnAccNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomer.add(panAccNo, gridBagConstraints);

        lblCustId.setText("Customer Id");
        lblCustId.setMaximumSize(new java.awt.Dimension(70, 21));
        lblCustId.setMinimumSize(new java.awt.Dimension(70, 21));
        lblCustId.setPreferredSize(new java.awt.Dimension(70, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomer.add(lblCustId, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomer.add(lblCustIdDesc, gridBagConstraints);

        lblCustNameValue.setForeground(new java.awt.Color(0, 51, 204));
        lblCustNameValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 40, 4, 4);
        panCustomer.add(lblCustNameValue, gridBagConstraints);

        lblStartChequeNo.setText("Starting Cheque No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomer.add(lblStartChequeNo, gridBagConstraints);

        panStartChequeNo.setLayout(new java.awt.GridBagLayout());

        txtStartCheque.setMinimumSize(new java.awt.Dimension(50, 21));
        txtStartCheque.setPreferredSize(new java.awt.Dimension(50, 21));
        panStartChequeNo.add(txtStartCheque, new java.awt.GridBagConstraints());

        txtStartchequeNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtStartchequeNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtStartchequeNoFocusLost(evt);
            }
        });
        panStartChequeNo.add(txtStartchequeNo, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomer.add(panStartChequeNo, gridBagConstraints);

        lblEndChequeNo.setText("Ending Cheque No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomer.add(lblEndChequeNo, gridBagConstraints);

        panEndChequeNo.setLayout(new java.awt.GridBagLayout());

        txtEndCheque.setMinimumSize(new java.awt.Dimension(50, 21));
        txtEndCheque.setPreferredSize(new java.awt.Dimension(50, 21));
        panEndChequeNo.add(txtEndCheque, new java.awt.GridBagConstraints());

        txtEndingChequeNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtEndingChequeNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEndingChequeNoFocusLost(evt);
            }
        });
        panEndChequeNo.add(txtEndingChequeNo, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomer.add(panEndChequeNo, gridBagConstraints);

        lblLeaf.setText("Leaf");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomer.add(lblLeaf, gridBagConstraints);

        panLeaf.setLayout(new java.awt.GridBagLayout());

        rdoLeaf.add(rdoLeaf_single);
        rdoLeaf_single.setText("Single");
        rdoLeaf_single.setMaximumSize(new java.awt.Dimension(55, 21));
        rdoLeaf_single.setMinimumSize(new java.awt.Dimension(55, 21));
        rdoLeaf_single.setPreferredSize(new java.awt.Dimension(61, 21));
        rdoLeaf_single.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoLeaf_singleActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panLeaf.add(rdoLeaf_single, gridBagConstraints);

        rdoLeaf.add(rdoLeaf_Bulk);
        rdoLeaf_Bulk.setText("Bulk");
        rdoLeaf_Bulk.setMaximumSize(new java.awt.Dimension(51, 21));
        rdoLeaf_Bulk.setMinimumSize(new java.awt.Dimension(51, 21));
        rdoLeaf_Bulk.setPreferredSize(new java.awt.Dimension(51, 21));
        rdoLeaf_Bulk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoLeaf_BulkActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panLeaf.add(rdoLeaf_Bulk, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomer.add(panLeaf, gridBagConstraints);

        lblProductType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomer.add(lblProductType, gridBagConstraints);

        cboProductType.setPopupWidth(110);
        cboProductType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboProductTypeItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomer.add(cboProductType, gridBagConstraints);

        lblChqStopId.setText("Chq Stop ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomer.add(lblChqStopId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPaymentIssues.add(panCustomer, gridBagConstraints);

        sptPaymentIssues.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPaymentIssues.add(sptPaymentIssues, gridBagConstraints);

        panCheque.setLayout(new java.awt.GridBagLayout());

        lblChequeDate.setText("Cheque Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheque.add(lblChequeDate, gridBagConstraints);

        tdtChequeDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtChequeDate.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheque.add(tdtChequeDate, gridBagConstraints);

        lblChequeAmt.setText("Cheque Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheque.add(lblChequeAmt, gridBagConstraints);

        txtChequeAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheque.add(txtChequeAmt, gridBagConstraints);

        lblPayeeName.setText("Payee Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheque.add(lblPayeeName, gridBagConstraints);

        txtPayeeName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheque.add(txtPayeeName, gridBagConstraints);

        lblStopPaymentCharges.setText("Stop Payment Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheque.add(lblStopPaymentCharges, gridBagConstraints);

        txtStopPaymentCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheque.add(txtStopPaymentCharges, gridBagConstraints);

        lblReasonStopPayment.setText("Reason to Stop Payment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheque.add(lblReasonStopPayment, gridBagConstraints);

        cboReasonStopPayment.setPopupWidth(130);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheque.add(cboReasonStopPayment, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheque.add(lblAccHeadDesc, gridBagConstraints);

        lblAccHead.setText("Account Head");
        lblAccHead.setMaximumSize(new java.awt.Dimension(83, 21));
        lblAccHead.setMinimumSize(new java.awt.Dimension(83, 21));
        lblAccHead.setPreferredSize(new java.awt.Dimension(83, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheque.add(lblAccHead, gridBagConstraints);

        lblReasonForRevoke.setText("Reason For Revoke");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheque.add(lblReasonForRevoke, gridBagConstraints);

        txtStopReasonForRevoke.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheque.add(txtStopReasonForRevoke, gridBagConstraints);

        lblChqStopDt.setText("Chq Stop Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheque.add(lblChqStopDt, gridBagConstraints);

        lblChqStopDtVal.setText("Stop date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheque.add(lblChqStopDtVal, gridBagConstraints);

        lblChqRevokeDtVal.setText("Revoke date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheque.add(lblChqRevokeDtVal, gridBagConstraints);

        lblChqRevokeDt.setText("Chq Revoke Date ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheque.add(lblChqRevokeDt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPaymentIssues.add(panCheque, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panStopPaymentIssue.add(panPaymentIssues, gridBagConstraints);

        panPaymentRevoke.setLayout(new java.awt.GridBagLayout());

        btnPaymentRevoke.setText("STOP PAYMENT INSTRUCTION REVOKE");
        btnPaymentRevoke.setToolTipText("Payment Revoke");
        btnPaymentRevoke.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPaymentRevokeActionPerformed(evt);
            }
        });
        panPaymentRevoke.add(btnPaymentRevoke, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panStopPaymentIssue.add(panPaymentRevoke, gridBagConstraints);

        tabChequeBook.addTab("Stop payment Issue", panStopPaymentIssue);

        panLooseLeafIssue.setLayout(new java.awt.GridBagLayout());

        panProdLeaf.setBorder(javax.swing.BorderFactory.createTitledBorder("Leaf Details"));
        panProdLeaf.setMinimumSize(new java.awt.Dimension(365, 330));
        panProdLeaf.setPreferredSize(new java.awt.Dimension(365, 330));
        panProdLeaf.setLayout(new java.awt.GridBagLayout());

        lblProductID.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProdLeaf.add(lblProductID, gridBagConstraints);

        cboProductID.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductID.setPopupWidth(215);
        cboProductID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProdLeaf.add(cboProductID, gridBagConstraints);

        lblAccHd.setText("Account Head");
        lblAccHd.setMaximumSize(new java.awt.Dimension(83, 21));
        lblAccHd.setMinimumSize(new java.awt.Dimension(83, 21));
        lblAccHd.setPreferredSize(new java.awt.Dimension(83, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProdLeaf.add(lblAccHd, gridBagConstraints);

        panLooseAccountHead.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLooseAccountHead.add(lblAccHdId, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLooseAccountHead.add(lblAccHdDesc, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProdLeaf.add(panLooseAccountHead, gridBagConstraints);

        lblAccounNumber.setText("Account No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProdLeaf.add(lblAccounNumber, gridBagConstraints);

        panAccounNumber.setLayout(new java.awt.GridBagLayout());

        txtAccounNumber.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccounNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAccounNumberActionPerformed(evt);
            }
        });
        txtAccounNumber.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccounNumberFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panAccounNumber.add(txtAccounNumber, gridBagConstraints);

        btnAccountNumber.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccountNumber.setToolTipText("Account No");
        btnAccountNumber.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccountNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccountNumberActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccounNumber.add(btnAccountNumber, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProdLeaf.add(panAccounNumber, gridBagConstraints);

        lblCustomerID.setText("Customer Id");
        lblCustomerID.setMaximumSize(new java.awt.Dimension(71, 21));
        lblCustomerID.setMinimumSize(new java.awt.Dimension(71, 21));
        lblCustomerID.setPreferredSize(new java.awt.Dimension(71, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProdLeaf.add(lblCustomerID, gridBagConstraints);

        lblCustomerIDValue.setMaximumSize(new java.awt.Dimension(99, 21));
        lblCustomerIDValue.setMinimumSize(new java.awt.Dimension(99, 21));
        lblCustomerIDValue.setPreferredSize(new java.awt.Dimension(99, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProdLeaf.add(lblCustomerIDValue, gridBagConstraints);

        lblCustomNameValue.setForeground(new java.awt.Color(0, 51, 204));
        lblCustomNameValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblCustomNameValue.setMinimumSize(new java.awt.Dimension(99, 21));
        lblCustomNameValue.setPreferredSize(new java.awt.Dimension(99, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 25, 4, 4);
        panProdLeaf.add(lblCustomNameValue, gridBagConstraints);

        lblLeafNo.setText("Leaf No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProdLeaf.add(lblLeafNo, gridBagConstraints);

        panLeafNo.setLayout(new java.awt.GridBagLayout());

        txtLeafNo1.setMinimumSize(new java.awt.Dimension(50, 21));
        txtLeafNo1.setPreferredSize(new java.awt.Dimension(50, 21));
        panLeafNo.add(txtLeafNo1, new java.awt.GridBagConstraints());

        txtLeafNo2.setMinimumSize(new java.awt.Dimension(100, 21));
        panLeafNo.add(txtLeafNo2, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProdLeaf.add(panLeafNo, gridBagConstraints);

        lblRemark.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProdLeaf.add(lblRemark, gridBagConstraints);

        txtRemark.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProdLeaf.add(txtRemark, gridBagConstraints);

        lblProduct_Type.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProdLeaf.add(lblProduct_Type, gridBagConstraints);

        cboProduct_Type.setPopupWidth(110);
        cboProduct_Type.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboProduct_TypeItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProdLeaf.add(cboProduct_Type, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLooseLeafIssue.add(panProdLeaf, gridBagConstraints);

        tabChequeBook.addTab("Loose Leaf Issue", panLooseLeafIssue);

        panEcsStopPaymentIssue.setLayout(new java.awt.GridBagLayout());

        panPaymentIssues1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panPaymentIssues1.setLayout(new java.awt.GridBagLayout());

        panCustomer1.setLayout(new java.awt.GridBagLayout());

        lblEcsProductId.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomer1.add(lblEcsProductId, gridBagConstraints);

        cboEcsProductId.setPopupWidth(215);
        cboEcsProductId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboEcsProductIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomer1.add(cboEcsProductId, gridBagConstraints);

        lblEcsAccNo.setText("Account No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomer1.add(lblEcsAccNo, gridBagConstraints);

        panAccNo1.setLayout(new java.awt.GridBagLayout());

        txtEcsAccNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEcsAccNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccNo1.add(txtEcsAccNo, gridBagConstraints);

        btnEcsAccNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnEcsAccNo.setToolTipText("Account No");
        btnEcsAccNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnEcsAccNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEcsAccNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccNo1.add(btnEcsAccNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomer1.add(panAccNo1, gridBagConstraints);

        lblEcsCustId.setText("Customer Id");
        lblEcsCustId.setMaximumSize(new java.awt.Dimension(70, 21));
        lblEcsCustId.setMinimumSize(new java.awt.Dimension(70, 21));
        lblEcsCustId.setPreferredSize(new java.awt.Dimension(70, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomer1.add(lblEcsCustId, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomer1.add(lblCustIdDesc1, gridBagConstraints);

        lblCustNameValue1.setForeground(new java.awt.Color(0, 51, 204));
        lblCustNameValue1.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 20, 4, 4);
        panCustomer1.add(lblCustNameValue1, gridBagConstraints);

        panStartChequeNo1.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomer1.add(panStartChequeNo1, gridBagConstraints);

        lblEcsNo.setText("Ecs Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomer1.add(lblEcsNo, gridBagConstraints);

        panEndEcsNo1.setLayout(new java.awt.GridBagLayout());

        txtEndEcs1.setMinimumSize(new java.awt.Dimension(70, 21));
        txtEndEcs1.setPreferredSize(new java.awt.Dimension(70, 21));
        panEndEcsNo1.add(txtEndEcs1, new java.awt.GridBagConstraints());

        txtEndEcsNo2.setMinimumSize(new java.awt.Dimension(70, 21));
        txtEndEcsNo2.setPreferredSize(new java.awt.Dimension(70, 21));
        panEndEcsNo1.add(txtEndEcsNo2, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomer1.add(panEndEcsNo1, gridBagConstraints);

        lblEcsProductType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomer1.add(lblEcsProductType, gridBagConstraints);

        cboEcsProductType.setPopupWidth(110);
        cboEcsProductType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboEcsProductTypeItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomer1.add(cboEcsProductType, gridBagConstraints);

        lblEcsId.setText("Ecs ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomer1.add(lblEcsId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPaymentIssues1.add(panCustomer1, gridBagConstraints);

        sptPaymentIssues1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPaymentIssues1.add(sptPaymentIssues1, gridBagConstraints);

        panCheque1.setLayout(new java.awt.GridBagLayout());

        lblEcsDate.setText("Ecs Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheque1.add(lblEcsDate, gridBagConstraints);

        tdtEcsDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtEcsDate.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheque1.add(tdtEcsDate, gridBagConstraints);

        lblEcsAmt.setText("Ecs Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheque1.add(lblEcsAmt, gridBagConstraints);

        txtEcsAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheque1.add(txtEcsAmt, gridBagConstraints);

        lblEcsPayeeName.setText("Payee Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheque1.add(lblEcsPayeeName, gridBagConstraints);

        txtEcsPayeeName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheque1.add(txtEcsPayeeName, gridBagConstraints);

        lblEcsStopPaymentCharges.setText("Stop Payment Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheque1.add(lblEcsStopPaymentCharges, gridBagConstraints);

        txtEcsStopPaymentCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheque1.add(txtEcsStopPaymentCharges, gridBagConstraints);

        lblEcsReasonStopPayment.setText("Reason to Stop Payment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheque1.add(lblEcsReasonStopPayment, gridBagConstraints);

        cboEcsReasonStopPayment.setPopupWidth(130);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheque1.add(cboEcsReasonStopPayment, gridBagConstraints);

        lblEcsReasonForRevoke.setText("Reason For Revoke");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheque1.add(lblEcsReasonForRevoke, gridBagConstraints);

        txtEcsStopReasonForRevoke.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheque1.add(txtEcsStopReasonForRevoke, gridBagConstraints);

        lblEcsRevokeDt.setText("Ecs Revoke Date ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheque1.add(lblEcsRevokeDt, gridBagConstraints);

        lblEcsRevokeDtVal.setText("Revoke date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheque1.add(lblEcsRevokeDtVal, gridBagConstraints);

        lblEcsStopDt.setText("Ecs Stop Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheque1.add(lblEcsStopDt, gridBagConstraints);

        lblEcsStopDtVal.setText("Stop date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheque1.add(lblEcsStopDtVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPaymentIssues1.add(panCheque1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEcsStopPaymentIssue.add(panPaymentIssues1, gridBagConstraints);

        panPaymentRevoke1.setLayout(new java.awt.GridBagLayout());

        btnEcsPaymentRevoke.setText("STOP PAYMENT INSTRUCTION REVOKE");
        btnEcsPaymentRevoke.setToolTipText("Payment Revoke");
        btnEcsPaymentRevoke.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEcsPaymentRevokeActionPerformed(evt);
            }
        });
        panPaymentRevoke1.add(btnEcsPaymentRevoke, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEcsStopPaymentIssue.add(panPaymentRevoke1, gridBagConstraints);

        tabChequeBook.addTab("ECS Stop payment ", panEcsStopPaymentIssue);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panChequeBook.add(tabChequeBook, gridBagConstraints);

        getContentPane().add(panChequeBook, java.awt.BorderLayout.CENTER);

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
        btnView.setFocusable(false);
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
        btnNew.setFocusable(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnNew);

        lblSpace17.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace17.setText("     ");
        lblSpace17.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace17);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.setFocusable(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnEdit);

        lblSpace18.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace18.setText("     ");
        lblSpace18.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace18);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.setFocusable(false);
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
        btnSave.setFocusable(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnSave);

        lblSpace19.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace19.setText("     ");
        lblSpace19.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace19);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.setEnabled(false);
        btnCancel.setFocusable(false);
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

        lblSpace20.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace20.setText("     ");
        lblSpace20.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace20);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.setFocusable(false);
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnException);

        lblSpace21.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace21.setText("     ");
        lblSpace21.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace21);

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
        btnPrint.setFocusable(false);
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnPrint);

        lblSpace22.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace22.setText("     ");
        lblSpace22.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace22);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.setFocusable(false);
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

        mbrLoanProduct.add(mnuProcess);

        setJMenuBar(mbrLoanProduct);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEcsPaymentRevokeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEcsPaymentRevokeActionPerformed
        // TODO add your handling code here:
         iEcsRevoked = true;
        ClientUtil.enableDisable(this, true);
        btnEcsPaymentRevoke.setEnabled(false);  
       ClientUtil.enableDisable(panEcsStopPaymentIssue, false);
       lblEcsReasonForRevoke.setVisible(true);
        txtEcsStopReasonForRevoke.setVisible(true);
        txtEcsStopReasonForRevoke.setEnabled(true);
        txtEcsStopReasonForRevoke.setEditable(true);
        lblEcsRevokeDt.setVisible(true);
        lblEcsRevokeDtVal.setVisible(true);
        Date curDt = (Date) currDt.clone();
        lblEcsRevokeDtVal.setText(CommonUtil.convertObjToStr(curDt));
        observable.setStopStat("REVOKED");
    }//GEN-LAST:event_btnEcsPaymentRevokeActionPerformed

    private void cboEcsProductIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboEcsProductIdActionPerformed
        // TODO add your handling code here:
        observable.setCboEcsProdId((String) cboEcsProductId.getSelectedItem());
        if( observable.getCboEcsProdId().length() > 0){
            //When the selected Product Id is not empty string
            final String PRODTYPE = CommonUtil.convertObjToStr((((ComboBoxModel)(cboEcsProductType).getModel())).getKeyForSelected());
            final String PRODID = CommonUtil.convertObjToStr((observable.getCbmEcsProdId()).getKeyForSelected());
            HashMap resultMap = observable.setAccountHead(PRODTYPE, PRODID);
            
//            observable.setLblProductIdAccHdId(CommonUtil.convertObjToStr(resultMap.get("AC_HEAD")));
//            observable.setLblProductIdAccHdDesc(CommonUtil.convertObjToStr(resultMap.get("AC_HEAD_DESC")));
//            
//            // To set the Value of Account head in "Loose Leaf"...
////            lblAccHeadId.setText(observable.getLblProductIdAccHdId());
//            lblAccHeadDesc.setText(observable.getLblProductIdAccHdDesc());
//            //            observable.ttNotifyObservers();
        }
    }//GEN-LAST:event_cboEcsProductIdActionPerformed

    private void txtEcsAccNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEcsAccNoFocusLost
        // TODO add your handling code here:
        observable.looseLeaf = false;
        observable.chequeBook = false;
        observable.stopPayment = false;
        observable.setCboEcsProdType("");;
        String ACCOUNTNO = txtEcsAccNo.getText();
        if ((!(observable.getCboEcsProdType().length()>0)) && ACCOUNTNO.length()>0) {
            observable.ecsStopPayment = true;
            if (observable.checkAcNoWithoutProdType(ACCOUNTNO)) {
                cboEcsProductId.setModel(observable.getCbmEcsProdId());
                txtEcsAccNo.setText(String.valueOf(ACCOUNTNO));
                cboProductIdActionPerformed(null);
                String prodType = ((ComboBoxModel)cboEcsProductType.getModel()).getKeyForSelected().toString();
            } else {
                ClientUtil.showAlertWindow("Invalid Account No.");
                txtEcsAccNo.setText("");
                return;
            }
        }
        if (txtEcsAccNo.getText().length()>0) {
            getAccountDetails(txtEcsAccNo, 
            ((ComboBoxModel)cboEcsProductType.getModel()).getKeyForSelected().toString(), 
            ((ComboBoxModel)cboEcsProductId.getModel()).getKeyForSelected().toString(),ECS);
        }
    }//GEN-LAST:event_txtEcsAccNoFocusLost

    private void btnEcsAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEcsAccNoActionPerformed
        // TODO add your handling code here:
        popUp(ECSSTOP);
    }//GEN-LAST:event_btnEcsAccNoActionPerformed

    private void cboEcsProductTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboEcsProductTypeItemStateChanged
        // TODO add your handling code here:
         observable.setCboEcsProdType(CommonUtil.convertObjToStr(cboEcsProductType.getSelectedItem()));
        if(observable.getCboEcsProdType().length() > 1){
            final String type = CommonUtil.convertObjToStr((((ComboBoxModel)(cboEcsProductType).getModel())).getKeyForSelected());
            if(observable.getActionType()== ClientConstants.ACTIONTYPE_NEW||observable.getActionType()== ClientConstants.ACTIONTYPE_VIEW){
                observable.resetLable();
                observable.resetEcsStopPaymentForm();
                observable.getProductID(type);
                observable.ttNotifyObservers();
            }
            cboEcsProductId.setModel(observable.getCbmEcsProdId());
        }
    }//GEN-LAST:event_cboEcsProductTypeItemStateChanged

    private void txtAccounNumberFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccounNumberFocusLost
        // TODO add your handling code here:
        observable.setCboProduct_Type("");
        String ACCOUNTNO = txtAccounNumber.getText();
        observable.looseLeaf = false;
        observable.chequeBook = false;
        observable.stopPayment = false;
        if ((!(observable.getCboProduct_Type().length()>0)) && ACCOUNTNO.length()>0) {
            observable.looseLeaf = true;
            if (observable.checkAcNoWithoutProdType(ACCOUNTNO)) {
                cboProductID.setModel(observable.getCbmProductID());
                txtAccounNumber.setText(String.valueOf(ACCOUNTNO));
                cboProductIDActionPerformed(null);
                String prodType = ((ComboBoxModel)cboProduct_Type.getModel()).getKeyForSelected().toString();
            } else {
                ClientUtil.showAlertWindow("Invalid Account No.");
                txtAccounNumber.setText("");
                return;
            }
        }        
        if (txtAccounNumber.getText().length()>0) {
            getAccountDetails(txtAccounNumber, 
            ((ComboBoxModel)cboProduct_Type.getModel()).getKeyForSelected().toString(), 
            ((ComboBoxModel)cboProductID.getModel()).getKeyForSelected().toString(),ACCNOLOOSE);
        }
    }//GEN-LAST:event_txtAccounNumberFocusLost

    private void txtAccountNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccountNoFocusLost
        // TODO add your handling code here:
        observable.looseLeaf = false;
        observable.chequeBook = false;
        observable.stopPayment = false;
        observable.setCboProdType("");
        String ACCOUNTNO = txtAccountNo.getText();
        if ((!(observable.getCboProdType().length()>0)) && ACCOUNTNO.length()>0) {
            observable.chequeBook = true;
            if (observable.checkAcNoWithoutProdType(ACCOUNTNO)) {
                cboProdId.setModel(observable.getCbmProdId());
                txtAccountNo.setText(String.valueOf(ACCOUNTNO));
                String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
            } else {
                ClientUtil.showAlertWindow("Invalid Account No.");
                txtAccountNo.setText("");
                return;
            }
        }
        if (txtAccountNo.getText().length()>0) {
            getAccountDetails(txtAccountNo, 
            ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(),
            ((ComboBoxModel)cboProdId.getModel()).getKeyForSelected().toString(),ACCNOCHEQUE);
        }

    }//GEN-LAST:event_txtAccountNoFocusLost

    private void txtAccNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccNoFocusLost
        // TODO add your handling code here:
        observable.looseLeaf = false;
        observable.chequeBook = false;
        observable.stopPayment = false;
        observable.setCboProductType("");
        String ACCOUNTNO = txtAccNo.getText();
        if ((!(observable.getCboProductType().length()>0)) && ACCOUNTNO.length()>0) {
            observable.stopPayment = true;
            if (observable.checkAcNoWithoutProdType(ACCOUNTNO)) {
                cboProductId.setModel(observable.getCbmProductId());
                txtAccNo.setText(String.valueOf(ACCOUNTNO));
                cboProductIdActionPerformed(null);
                String prodType = ((ComboBoxModel)cboProductType.getModel()).getKeyForSelected().toString();
            } else {
                ClientUtil.showAlertWindow("Invalid Account No.");
                txtAccNo.setText("");
                return;
            }
        }
        if (txtAccNo.getText().length()>0) {
            getAccountDetails(txtAccNo, 
            ((ComboBoxModel)cboProductType.getModel()).getKeyForSelected().toString(), 
            ((ComboBoxModel)cboProductId.getModel()).getKeyForSelected().toString(),ACCNOSTOP);
        }
//        getAccountDetails(txtAccNo, ((ComboBoxModel)cboProduct_Type.getModel()).getKeyForSelected().toString(), 
//        ((ComboBoxModel)cboProductID.getModel()).getKeyForSelected().toString(),ACCNOSTOP);
    }//GEN-LAST:event_txtAccNoFocusLost

    private void txtAccounNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAccounNumberActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtAccounNumberActionPerformed

    private void txtAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAccNoActionPerformed
        // TODO add your handling code here:
//        if (txtAccNo.getText().length()>0) {
//            getAccountDetails(txtAccNo, 
//            ((ComboBoxModel)cboProductType.getModel()).getKeyForSelected().toString(), 
//            ((ComboBoxModel)cboProductId.getModel()).getKeyForSelected().toString(),ACCNOSTOP);
//        }
    }//GEN-LAST:event_txtAccNoActionPerformed

    private void txtAccountNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAccountNoActionPerformed
        // TODO add your handling code here:
//        if (txtAccountNo.getText().length()>0) {
//            getAccountDetails(txtAccountNo, 
//            ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(), 
//            ((ComboBoxModel)cboProdId.getModel()).getKeyForSelected().toString(),ACCNOCHEQUE);
//        }
    }//GEN-LAST:event_txtAccountNoActionPerformed

    private void getAccountDetails(com.see.truetransact.uicomponent.CTextField txtActNum, String prodType, String prodID, int field) {
        HashMap hash = new HashMap();
        String ACCOUNTNO = (String) txtActNum.getText();
        List lst = null;
        updateOBFields();
        hash.put("PROD_ID", prodID);
        hash.put(CommonConstants.BRANCH_ID,ProxyParameters.BRANCH_ID);
        hash.put(CommonConstants.ACT_NUM,ACCOUNTNO);
        lst=ClientUtil.executeQuery("getChequeAccountList" + prodType, hash);
        if(lst!=null && lst.size()>0){
            HashMap map=new HashMap();
            map=(HashMap)lst.get(0);
            hash.put("ACCOUNTNO",ACCOUNTNO);
            hash.put("CUSTOMERID",map.get("CustomerID"));
            hash.put("CUSTOMERNAME",map.get("CustomerName"));
            viewType=field;
            fillData(hash);
            observable.setLblCustName("");
        }else{
            ClientUtil.showAlertWindow("Invalid Account Number...");
            txtActNum.setText("");
            txtActNum.requestFocus();
        }
        hash = null;
        lst = null;
    }
    
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        if(panChequeBookIssue.isShowing()==true){
            panEditDelete=CHEQUE;
             txtStartingCheque.setEditable(true);
             txtStartingChequeNo.setEditable(true);
            
            
        }else if(panStopPaymentIssue.isShowing()==true){
            panEditDelete=STOP;
            
        }else if(panLooseLeafIssue.isShowing()==true){
            panEditDelete=LEAF;
        }
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus1.setText(observable.getLblStatus());
        view=ENQUIRY;
//        popUp(VIEW);
        btnCheck();
        txtAccountNo.setEnabled(true);
        txtAccNo.setEnabled(true);
        txtAccounNumber.setEnabled(true);
    }//GEN-LAST:event_btnViewActionPerformed

    private void txtNoOfChequeBooksFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNoOfChequeBooksFocusLost
        // TODO add your handling code here:
//         if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            txtSeriesFrom.setText("");
            txtSeriesNoTo.setText("");
            txtStartingCheque.setText("");
            txtStartingChequeNo.setText("");
            txtEndingCheque.setText("");
            txtEndingchequeNo.setText("");
            cboNoOfLeaves.setSelectedItem("");
//        }
    }//GEN-LAST:event_txtNoOfChequeBooksFocusLost

    private void txtStartingChequeNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtStartingChequeNoFocusLost
        // TODO add your handling code here:
        if(txtNoOfChequeBooks.getText().equals("")){
            // ClientUtil.displayAlert("Enter No of Cheque books");
        }
        else if(cboNoOfLeaves.getSelectedItem().equals("")){
         //   ClientUtil.displayAlert("Select No of Leaves");
        }
        else{
        HashMap map = new HashMap();
        HashMap dataMap = new HashMap();
        boolean isTrue = false;
        map.put("USAGE", CommonUtil.convertObjToStr(((ComboBoxModel) cboUsage.getModel()).getKeyForSelected()));
        map.put("LEAVESNUM", CommonUtil.convertObjToDouble(((ComboBoxModel) cboNoOfLeaves.getModel()).getKeyForSelected()));
        map.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        long leaves = CommonUtil.convertObjToLong(map.get("LEAVESNUM"));
        long noOfCheqBook = CommonUtil.convertObjToLong(txtNoOfChequeBooks.getText());
        long startCheqNo = CommonUtil.convertObjToLong(txtStartingChequeNo.getText());
        long totalLeaves = leaves * noOfCheqBook;
        //        System.out.println("@@@@@@@@map"+map);
        List lst = (List) ClientUtil.executeQuery("getItemID", map);
        map = null;
        if(lst != null)
            if(lst.size() > 0){
                map = (HashMap) lst.get(0);
                lst = null;
                map.put("INSTRUMENT_PREFIX", txtStartingCheque.getText());
                map.put("START_CHK_NO", CommonUtil.convertObjToStr(startCheqNo));
                map.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
                               System.out.println("@@@@@@@@map"+map);
                lst = (List) ClientUtil.executeQuery("checkInventoryDetails", map);
                if(lst != null)
                    if(lst.size() > 0){
                        dataMap = (HashMap) lst.get(0);
                        List lstData = (List) ClientUtil.executeQuery("getIssuedCheques",map);
                        if(lstData != null)
                            if(lstData.size() > 0){
                                ClientUtil.showAlertWindow("ChequeNo already issued");
                                lstData = null;
                                txtStartingChequeNo.setText("");
                                txtEndingCheque.setText("");
                                txtEndingchequeNo.setText("");
                            }else{
                                long startStartLeaf = CommonUtil.convertObjToLong(dataMap.get("LEAVES_SLNO_FROM"));
                                long result = startCheqNo - startStartLeaf;
                                long finalRes = result % leaves;
                                if(finalRes == 0){
                                    isTrue = true;
                                    System.out.println("###");
                                }else{
                                    ClientUtil.showAlertWindow("Incorrect Starting ChequeNo");
                                    txtStartingChequeNo.setText("");
                                    txtEndingCheque.setText("");
                                    txtEndingchequeNo.setText("");
                                }
                            }
                        if(isTrue == true){
                            
                            long endCheqNo = startCheqNo + (totalLeaves - 1);
                            HashMap data = new HashMap();
                            data.put("START_CHK_NO", CommonUtil.convertObjToStr(endCheqNo)); //endChequeNo as START_CHK_NO to check whether end chequeNo is already issue
                            data.put("INSTRUMENT_PREFIX", txtStartingCheque.getText());
                            data.put("BRANCH_ID", ProxyParameters.BRANCH_ID); 
                            data.put("ITEM_ID", CommonUtil.convertObjToStr(map.get("ITEM_ID")));
                            List checkLst = (List) ClientUtil.executeQuery("getIssuedCheques",data);
                            List checkLst1 = (List) ClientUtil.executeQuery("checkInventoryDetails", data);
                            if((checkLst != null) && checkLst.size() > 0){
                               ClientUtil.showAlertWindow("Incorrect ChequeNo Series");
                               txtStartingChequeNo.setText("");
                               txtEndingCheque.setText("");
                               txtEndingchequeNo.setText("");
                            }
                            else if((checkLst1.size() <= 0)){
                                ClientUtil.showAlertWindow("Incorrect ChequeNo Series");
                                txtStartingChequeNo.setText("");
                                txtEndingCheque.setText("");
                                txtEndingchequeNo.setText("");
                            }
                            else{
                            txtEndingchequeNo.setText(CommonUtil.convertObjToStr(data.get("START_CHK_NO")));//endChequeNo as START_CHK_NO
                            txtEndingCheque.setText(txtStartingCheque.getText());
                            lstData = (List) ClientUtil.executeQuery("findInvenData", map);
                            if(lstData != null)
                                if(lstData.size() > 0 && lstData.size() < 2){
                                    lstData = null;
				    map.put("TRANS_TYPE", "TRANS_IN");
                                    lstData = (List) ClientUtil.executeQuery("getFirstBookSeries", map);
                                    if(lstData != null)
                                        if(lstData.size() > 0){
                                            HashMap book = new HashMap();
                                            book = (HashMap) lstData.get(0);
                                            long bookSerialNo1 = CommonUtil.convertObjToLong(book.get("BOOK_SLNO"));
                                            txtSeriesFrom.setText(String.valueOf(bookSerialNo1));
                                            long bookSerialNo2 = bookSerialNo1 + (noOfCheqBook - 1);
                                            txtSeriesNoTo.setText(String.valueOf(bookSerialNo2));
                                        }
                                        
                                }else{
                                     lstData = null;
                                     map.put("TRANS_TYPE", "TRANS_OUT");
                                     lstData = (List) ClientUtil.executeQuery("getOtherBookSeries", map);
                                     if(lstData != null)
                                         if(lstData.size() > 0){
                                             HashMap book = new HashMap();
                                             book = (HashMap) lstData.get(0);
                                             long bookSerialNo1 = CommonUtil.convertObjToLong(book.get("BOOK_SLNO"));
                                             txtSeriesFrom.setText(String.valueOf(bookSerialNo1));
                                             long bookSerialNo2 = bookSerialNo1 + (noOfCheqBook - 1);
                                             txtSeriesNoTo.setText(String.valueOf(bookSerialNo2));
                                         }
                                }
                            }
                        }
                        
                    }else{
                        ClientUtil.showAlertWindow("Incorrect ChequeNo");
                        txtStartingChequeNo.setText("");
                        txtEndingCheque.setText("");
                        txtEndingchequeNo.setText("");
                    }
            }
        }
//        else
//            ClientUtil.displayAlert("Enter No of Cheque books");
    }//GEN-LAST:event_txtStartingChequeNoFocusLost

    private void tblChequeDetailsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblChequeDetailsMousePressed
        // TODO add your handling code here:
    HashMap whereMap = new HashMap();
    whereMap.put("ACCT_NO", txtAccountNo.getText());
    String fromNo = CommonUtil.convertObjToStr(tblChequeDetails.getValueAt(tblChequeDetails.getSelectedRow(), 3));
    whereMap.put("FROM_CHQ_NO1", fromNo.substring(0,fromNo.indexOf(" ")));
    fromNo = fromNo.substring(fromNo.indexOf(" ")+1,fromNo.length());
    System.out.println("### FromNo :"+fromNo+":");
    String toNo = CommonUtil.convertObjToStr(tblChequeDetails.getValueAt(tblChequeDetails.getSelectedRow(), 4));
    whereMap.put("TO_CHQ_NO1", toNo.substring(0,toNo.indexOf(" ")));
    toNo = toNo.substring(toNo.indexOf(" ")+1,toNo.length());
    System.out.println("### ToNo :"+toNo+":");
    whereMap.put("FROM_CHQ_NO2", fromNo);
    whereMap.put("TO_CHQ_NO2", toNo);
    System.out.println("###$$% whereMap : "+whereMap);
    TableDialogUI tableData = new TableDialogUI("getIssuedChequeDetails", whereMap);
    tableData.setTitle("Cheque book leaves details");
    tableData.show(); 
    }//GEN-LAST:event_tblChequeDetailsMousePressed
    
    private void cboProduct_TypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboProduct_TypeItemStateChanged
        // TODO add your handling code here:
        observable.setCboProduct_Type(CommonUtil.convertObjToStr(cboProduct_Type.getSelectedItem()));
        if(observable.getCboProduct_Type().length() > 1){
            final String type = CommonUtil.convertObjToStr((((ComboBoxModel)(cboProduct_Type).getModel())).getKeyForSelected());
            observable.getProductID(type);
            if(observable.getActionType()== ClientConstants.ACTIONTYPE_NEW){
                observable.resetLable();
                observable.resetStopPaymentForm();
                observable.ttNotifyObservers();
            }
            cboProductID.setModel(observable.getCbmProductID());
        }
    }//GEN-LAST:event_cboProduct_TypeItemStateChanged
    
    private void cboProductTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboProductTypeItemStateChanged
        // TODO add your handling code here:
        observable.setCboProductType(CommonUtil.convertObjToStr(cboProductType.getSelectedItem()));
        if(observable.getCboProductType().length() > 1){
            final String type = CommonUtil.convertObjToStr((((ComboBoxModel)(cboProductType).getModel())).getKeyForSelected());
            if(observable.getActionType()== ClientConstants.ACTIONTYPE_NEW||observable.getActionType()== ClientConstants.ACTIONTYPE_VIEW){
                observable.resetLable();
                observable.resetStopPaymentForm();
                observable.getProductID(type);
                observable.ttNotifyObservers();
            }
            cboProductId.setModel(observable.getCbmProductId());
        }
    }//GEN-LAST:event_cboProductTypeItemStateChanged
    
    private void cboProdTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboProdTypeItemStateChanged
        // TODO add your handling code here:
        observable.setCboProdType(CommonUtil.convertObjToStr(cboProdType.getSelectedItem()));
        if(observable.getCboProdType().length() > 1){
            final String type = CommonUtil.convertObjToStr((((ComboBoxModel)(cboProdType).getModel())).getKeyForSelected());
            observable.setProdTypeTO(type);
            //            System.out.println("ProdTypeTO" + observable.getProdTypeTO());
            if(observable.getActionType()== ClientConstants.ACTIONTYPE_NEW||observable.getActionType()== ClientConstants.ACTIONTYPE_VIEW){
                observable.resetLable();
                observable.resetTable();     // Reset the table in UI...
                observable.resetChequeIssueForm();
                observable.getProductID(type);
                observable.ttNotifyObservers();
            }
            cboProdId.setModel(observable.getCbmProdId());
        }
    }//GEN-LAST:event_cboProdTypeItemStateChanged
    
    private void cboNoOfLeavesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboNoOfLeavesFocusLost
        // TODO add your handling code here:
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW
        || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            observable.setCboNoOfLeaves((String) cboNoOfLeaves.getSelectedItem());
            
            if(observable.getCboNoOfLeaves().length() > 0){
                observable.setTxtNoOfChequeBooks(txtNoOfChequeBooks.getText());
                observable.setCboUsage(CommonUtil.convertObjToStr(cboUsage.getSelectedItem()));
                observable.setCboNoOfLeaves(CommonUtil.convertObjToStr(cboNoOfLeaves.getSelectedItem()));
                
                //__ To set the total Charges depending on the Number of the Cheques to be issued...
                final String PRODTYPE = CommonUtil.convertObjToStr((((ComboBoxModel)(cboProdType).getModel())).getKeyForSelected());
                final String PRODID = CommonUtil.convertObjToStr((((ComboBoxModel)(cboProdId).getModel())).getKeyForSelected());
                final String ACTNUM = CommonUtil.convertObjToStr(txtAccountNo.getText());
                String charges = CommonUtil.convertObjToStr(observable.setCharges(PRODTYPE, PRODID, ACTNUM));
                txtChargesCollected.setText(charges);
                if(CommonUtil.convertObjToDouble( txtChargesCollected.getText()).doubleValue()>0){
                    callServiceTaxCalculation(CommonUtil.convertObjToStr( txtChargesCollected.getText()));
                }
                
                //                //__ To set the Data for the Cheque-book Series ans Cheque Leaves...
                //                HashMap resultMap = observable.getChequeBookData(CommonUtil.convertObjToInt(txtNoOfChequeBooks.getText()));
                //
                //                //__ If the ResultMap contain the relevant data...Proceed
                //                if(resultMap.containsKey("BOOK_SLNO_FROM")){
                //                    long chequeBooks = CommonUtil.convertObjToLong(txtNoOfChequeBooks.getText());
                //                    long seriesFrom = CommonUtil.convertObjToLong(resultMap.get("BOOK_SLNO_FROM"));
                //                    long seriesTo = CommonUtil.convertObjToLong(resultMap.get("BOOK_SLNO_TO"));
                //
                //                    long difference = seriesTo - seriesFrom;
                //                    if(difference <  chequeBooks){
                //                        // Display alert... displayAlert(mandatoryMessage);
                //                        displayAlert(resourceBundle.getString("CHEQUEBOOKWARNING") +"\n" + resourceBundle.getString("CHEQUEBOOKWARNING") + difference);
                //                        txtNoOfChequeBooks.setText(String.valueOf(difference));
                //                        chequeBooks = difference;
                //                    }
                //                    if(chequeBooks > 0){
                //                        txtSeriesFrom.setText(String.valueOf(seriesFrom));
                //                        txtSeriesNoTo.setText(String.valueOf(seriesFrom + chequeBooks - 1));
                //
                //                        final String prefix = CommonUtil.convertObjToStr(resultMap.get("INSTRUMENT_PREFIX"));
                //                        txtStartingCheque.setText(prefix);
                //                        txtEndingCheque.setText(prefix);
                //
                //                        long chequeLeaves = CommonUtil.convertObjToLong(((ComboBoxModel) cboNoOfLeaves.getModel()).getKeyForSelected());
                //                        long leavesFrom = CommonUtil.convertObjToLong(resultMap.get("LEAVES_SLNO_FROM"));
                //                        txtStartingChequeNo.setText(String.valueOf(leavesFrom));
                //                        txtEndingchequeNo.setText(String.valueOf(leavesFrom - 1 + (chequeLeaves * chequeBooks)));
                //                    }
                //                }
            }
        }
//        if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            txtSeriesFrom.setText("");
            txtSeriesNoTo.setText("");
            txtStartingCheque.setText("");
            txtStartingChequeNo.setText("");
            txtEndingCheque.setText("");
            txtEndingchequeNo.setText("");
//        }
    }//GEN-LAST:event_cboNoOfLeavesFocusLost
    
    private void cboUsageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboUsageActionPerformed
        // TODO add your handling code here:
        observable.setCboUsage((String) cboUsage.getSelectedItem());
        //--- If an item is selected from cbo Usage, set appropriate the Leaves Per Book
        //--- to that Usage else set it to nothing
        if(observable.getCboUsage().length() > 0){
            observable.getLeavesPerBook(CommonUtil.convertObjToStr(((ComboBoxModel) cboUsage.getModel()).getKeyForSelected()));
            cboNoOfLeaves.setModel(observable.getCbmNoOfLeaves());
        }
    }//GEN-LAST:event_cboUsageActionPerformed
    
    private void txtStartchequeNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtStartchequeNoFocusLost
        // Add your handling code here:
        if(!observable.getRdoLeaf_Bulk()){
            observable.setTxtStartCheque(txtStartCheque.getText());
            if(txtStartCheque.getText().equals("") || txtStartchequeNo.getText().equals("")){
                displayAlert(resourceBundle.getString("WARNING"));
            }else{
                //__ To set the total Charges...
                final String PRODTYPE = CommonUtil.convertObjToStr((((ComboBoxModel)(cboProductType).getModel())).getKeyForSelected());
                final String PRODID = CommonUtil.convertObjToStr((((ComboBoxModel)(cboProductId).getModel())).getKeyForSelected());
                final String ACTNUM = CommonUtil.convertObjToStr(txtAccNo.getText());
                
                
                observable.setStopPaymentCharges(PRODTYPE, PRODID, ACTNUM);
                txtStopPaymentCharges.setText(observable.getTxtStopPaymentCharges());
                if(CommonUtil.convertObjToDouble(observable.getTxtStopPaymentCharges()).doubleValue()>0){
//                    String calAt=CommonUtil.convertObjToStr(observable.getTxtStopPaymentCharges());
                    callServiceTaxCalculation(CommonUtil.convertObjToStr(observable.getTxtStopPaymentCharges()));
                }
                //                updateOBFields();
                //                observable.ttNotifyObservers();
            }
        }
    }//GEN-LAST:event_txtStartchequeNoFocusLost
    
    private void txtEndingChequeNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEndingChequeNoFocusLost
        // Add your handling code here:
        if(observable.getRdoLeaf_Bulk()){
            if(!((txtStartCheque.getText().equals(txtEndCheque.getText()))
            &&  (CommonUtil.convertObjToDouble(txtStartchequeNo.getText()).doubleValue()  < CommonUtil.convertObjToDouble(txtEndingChequeNo.getText()).doubleValue()))){
                displayAlert(resourceBundle.getString("WARNING"));
            }else{
                observable.setTxtStartchequeNo(txtStartchequeNo.getText());
                observable.setTxtEndingChequeNo(txtEndingChequeNo.getText());
                
                //__ To set the total Charges...
                final String PRODTYPE = CommonUtil.convertObjToStr((((ComboBoxModel)(cboProductType).getModel())).getKeyForSelected());
                final String PRODID = CommonUtil.convertObjToStr((((ComboBoxModel)(cboProductId).getModel())).getKeyForSelected());
                final String ACTNUM = CommonUtil.convertObjToStr(txtAccNo.getText());
                
                observable.setStopPaymentCharges(PRODTYPE, PRODID, ACTNUM);
                
                txtStopPaymentCharges.setText(observable.getTxtStopPaymentCharges());
                updateOBFields();
                observable.ttNotifyObservers();
            }
        }
    }//GEN-LAST:event_txtEndingChequeNoFocusLost
    
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
            singleAuthorizeMap.put("USER_ID", TrueTransactMain.USER_ID);
            if(panChequeBookIssue.isShowing()==true){
                pan = CHEQUE;
                observable.setTransId(Trans_Id);
                ArrayList arrList = new ArrayList();
                HashMap authDataMap = new HashMap();
                authDataMap.put("TRANSACTION ID", observable.getTransId());
                authDataMap.put("CHQ_ISSUE_ID", CHEQUEAUTHID);
                authDataMap.put("TRANSACTION TYPE", "TRANS_OUT");
                authDataMap.put(CommonConstants.STATUS, observable.getStatus());
                authDataMap.put("AUTHORIZEDT", currDt.clone());
                authDataMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                authDataMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
                authDataMap.put("ACCOUNT NUMBER", CommonUtil.convertObjToStr(txtAccountNo.getText()));
                authDataMap.put("PRODUCT ID", CommonUtil.convertObjToStr((observable.getCbmProdId()).getKeyForSelected()));
                authDataMap.put("PRODUCT TYPE", CommonUtil.convertObjToStr((((ComboBoxModel)(cboProdType).getModel())).getKeyForSelected()));
                authDataMap.put("CHARGES", CommonUtil.convertObjToStr(txtChargesCollected.getText()));
                
                //__ Data to calculate the Book Series and Cheque No Series...
                authDataMap.put("USAGE", CommonUtil.convertObjToStr((((ComboBoxModel)(cboUsage).getModel())).getKeyForSelected()));
                authDataMap.put("ITEM_TYPE", "CHEQUES");
                authDataMap.put("NO OF BOOKS", CommonUtil.convertObjToStr(txtNoOfChequeBooks.getText()));
                authDataMap.put("NO OF LEAVES", CommonUtil.convertObjToStr((((ComboBoxModel)(cboNoOfLeaves).getModel())).getKeyForSelected()));
                authDataMap.put("BK_SERIES_FROM", CommonUtil.convertObjToStr(txtSeriesFrom.getText()));
                authDataMap.put("BK_SERIES_TO", CommonUtil.convertObjToStr(txtSeriesNoTo.getText()));
                authDataMap.put("START_LEAF_NO2", CommonUtil.convertObjToStr(txtStartingChequeNo.getText()));
                authDataMap.put("END_LEAF_NO2", CommonUtil.convertObjToStr(txtEndingchequeNo.getText()));
                 authDataMap.put("INSTRUMENT_PREFIX", CommonUtil.convertObjToStr(txtStartingCheque.getText()));
                authDataMap.put("SERVICE_TAX",calMap);
                arrList.add(authDataMap);
                //                System.out.println("authDataMap: " + authDataMap);
                
                singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
                singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
//                singleAuthorizeMap.put("SERVICE_TAX",calMap);
                authorize(singleAuthorizeMap);
                if(isAuth){
                    super.setOpenForEditBy(observable.getStatusBy());
                    super.removeEditLock(observable.getChequeIssueId());
                    isAuth = false;
                }
            }
            else if(panStopPaymentIssue.isShowing()==true){
                pan = STOP;
                ArrayList arrList = new ArrayList();
                HashMap authDataMap = new HashMap();
                authDataMap.put("AUTHSTATUS", observable.getAuthStatus());
                authDataMap.put("CHQ_STOP_ID", observable.getChequeStopId());
                authDataMap.put("STOP_STATUS", getStopStatus());
                authDataMap.put(CommonConstants.STATUS, observable.getStatus());
                authDataMap.put("AUTHORIZEDT", currDt.clone());
                authDataMap.put("REMARKS", txtStopReasonForRevoke.getText());
                authDataMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                authDataMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
                authDataMap.put("ACCOUNT NUMBER", CommonUtil.convertObjToStr(txtAccNo.getText()));
                authDataMap.put("PRODUCT ID", CommonUtil.convertObjToStr((observable.getCbmProductId()).getKeyForSelected()));
                authDataMap.put("PRODUCT TYPE", CommonUtil.convertObjToStr((((ComboBoxModel)(cboProductType).getModel())).getKeyForSelected()));
                if(observable.getStopStat().equalsIgnoreCase("STOPPED"))
                authDataMap.put("CHARGES", CommonUtil.convertObjToStr(txtStopPaymentCharges.getText()));
                else
                 authDataMap.put("CHARGES", String.valueOf(0));
                authDataMap.put("SERVICE_TAX",calMap);
                arrList.add(authDataMap);
                System.out.println("authDataMap: " + authDataMap);
                
                singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
                singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                authorize(singleAuthorizeMap);
                if(isAuth){
                    super.setOpenForEditBy(observable.getStatusBy());
                    super.removeEditLock(observable.getChequeStopId());
                    isAuth = false;
                }
                
            }else if(panLooseLeafIssue.isShowing()==true){
                
                pan = LEAF;
                
                singleAuthorizeMap.put("CHQ_LEAF_ID", observable.getChequeLeafId());
                singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
                singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                singleAuthorizeMap.put("AUTHORIZEDT", currDt.clone());
                singleAuthorizeMap.put("REMARKS", "");
                
                ClientUtil.execute("authLooseLeafIssue", singleAuthorizeMap);
                super.setOpenForEditBy(observable.getStatusBy());
                super.removeEditLock(observable.getChequeLeafId());
                viewType = -1;
                btnSave.setEnabled(true);
                 if(fromNewAuthorizeUI){
                    this.dispose();
                    newauthorizeListUI.setFocusToTable();
                    newauthorizeListUI.removeSelectedRow();
                    newauthorizeListUI.displayDetails("Loose Leaf Issue");
                }
                if(fromAuthorizeUI){
                    this.dispose();
                    authorizeListUI.setFocusToTable();
                    authorizeListUI.removeSelectedRow();
                    authorizeListUI.displayDetails("Loose Leaf Issue");
                }
                btnCancelActionPerformed(null);
            }
            else if(panEcsStopPaymentIssue.isShowing()==true){
                pan = ECS;
                ArrayList arrList = new ArrayList();
                HashMap authEcsDataMap = new HashMap();
                authEcsDataMap.put("AUTHSTATUS", observable.getAuthStatus());
                authEcsDataMap.put("ECS_STOP_ID", observable.getEcsStopId());
                authEcsDataMap.put("STOP_STATUS", getStopStatus());
                authEcsDataMap.put(CommonConstants.STATUS, observable.getStatus());
                authEcsDataMap.put("AUTHORIZEDT", currDt.clone());
                authEcsDataMap.put("REMARKS", txtEcsStopReasonForRevoke.getText());
                authEcsDataMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                authEcsDataMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
                authEcsDataMap.put("ACCOUNT NUMBER", CommonUtil.convertObjToStr(txtEcsAccNo.getText()));
                authEcsDataMap.put("PRODUCT ID", CommonUtil.convertObjToStr((observable.getCbmEcsProdId()).getKeyForSelected()));
                authEcsDataMap.put("PRODUCT TYPE", CommonUtil.convertObjToStr((((ComboBoxModel)(cboEcsProductType).getModel())).getKeyForSelected()));
                if(observable.getStopStat().equalsIgnoreCase("STOPPED"))
                authEcsDataMap.put("CHARGES", CommonUtil.convertObjToStr(txtEcsStopPaymentCharges.getText()));
                else
                 authEcsDataMap.put("CHARGES", String.valueOf(0));
                authEcsDataMap.put("SERVICE_TAX",calMap);
                arrList.add(authEcsDataMap);
                System.out.println("authDataMap: " + authEcsDataMap);
                
                singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
                singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                authorize(singleAuthorizeMap);
                if(isAuth){
                    super.setOpenForEditBy(observable.getStatusBy());
                    super.removeEditLock(observable.getChequeStopId());
                    isAuth = false;
                }
                
            }
        } else{
            HashMap mapParam = new HashMap();
            //__ To Save the data in the Internal Frame...
            setModified(true);
            
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            
            //__ WhereMap Common for all...
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            
            if(panChequeBookIssue.isShowing()==true){
                mapParam.put(CommonConstants.MAP_NAME, "getSelectChequeBookIssueTOList");
                viewType = AUTHORIZE;
                isFilled = false;
                panEditDelete=CHEQUE;
                AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
                pan = CHEQUE;
                authorizeUI.show();
                
            }else if(panStopPaymentIssue.isShowing()==true){
                mapParam.put(CommonConstants.MAP_NAME, "getSelectStopPaymentIssueTOList");
                viewType = AUTHORIZE;
                isFilled = false;
                panEditDelete=STOP;
                AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
                pan = STOP;
                authorizeUI.show();
                
            }else if(panLooseLeafIssue.isShowing()==true){
                mapParam.put(CommonConstants.MAP_NAME, "getSelectLooseLeafIssueTOList");
                mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authLooseLeafIssue");
                viewType = AUTHORIZE;
                isFilled = false;
                panEditDelete=LEAF;
                AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
                authorizeUI.show();
            }
            else if(panEcsStopPaymentIssue.isShowing()==true){
                mapParam.put(CommonConstants.MAP_NAME, "getSelectEcsStopPaymentIssueTOList");
                viewType = AUTHORIZE;
                isFilled = false;
                panEditDelete=ECS;
                AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
                pan = ECS;
                authorizeUI.show();
            }
            
            whereMap = null;
            btnSave.setEnabled(false);
            
            //__ If there's no data to be Authorized, call Cancel action...
//            if(!isModified()){
//                setButtonEnableDisable();
//                btnCancelActionPerformed(null);
//            }
        }
    }
    
    public void authorize(HashMap map) {
        try{
            //__ If the ChequeBook data is To be Authorized...
            //        if(panChequeBookIssue.isShowing()){
            //            final String authStatus = CommonUtil.convertObjToStr(map.get(CommonConstants.AUTHORIZESTATUS));
            
            //            //__ If the Authorization is selectec, generate the chequebook data...
            //            if(authStatus.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)){
            //                authData(map);
            ////                ArrayList dataList = (ArrayList)map.get(CommonConstants.AUTHORIZEDATA);
            ////                ArrayList authList;
            ////                System.out.println("dataList: " + dataList);
            ////
            ////                int size = dataList.size();
            ////
            ////                //__ To Display the data Generated...
            ////                StringBuffer str = new StringBuffer();
            ////                System.out.println("Size of Auth List: " + dataList.size());
            ////                for(int i=0; i<size; i++){
            ////                    HashMap authDataMap = (HashMap) dataList.get(i);
            ////                    System.out.println("authDataMap: " + authDataMap);
            ////
            ////
            ////
            ////
            ////                    //__ To Calculate the Book Series and ChequeBook Series...
            //////                    dataMap = generateChequeNos(authDataMap);
            ////
            ////                    HashMap dataMap;
            ////                    //__ Testing the code...
            //////                    GenerateSeries gs = new GenerateSeries();
            //////                    gs.setItemType("CHEQUES");
            //////                    gs.setItemSubType(CommonUtil.convertObjToStr(authDataMap.get("USAGE")));
            //////                    gs.setBranchCode(getSelectedBranchID());
            //////                    gs.setBooks(CommonUtil.convertObjToLong(authDataMap.get("NO OF BOOKS")));
            //////                    gs.setLeaves(CommonUtil.convertObjToLong(authDataMap.get("NO OF LEAVES")));
            ////
            //////                    dataMap = gs.seriesData();
            ////
            //////                    if(dataMap.containsKey("CHQ_BK_SERIES_FROM")){
            ////                     if(dataMap.containsKey("BK_SERIES_FROM")){
            ////                        str.append("\n\n");
            ////                        str.append(resourceBundle.getString("lblAccounNumber") + ": " + CommonUtil.convertObjToStr(authDataMap.get("ACCOUNT NUMBER")) + "\n");
            //////                        str.append(resourceBundle.getString("lblSeriesNoFrom") + ": " + CommonUtil.convertObjToStr(dataMap.get("CHQ_BK_SERIES_FROM")) + "\n" );
            //////                        str.append(resourceBundle.getString("lblSeriesNoTo") + ": " + CommonUtil.convertObjToStr(dataMap.get("CHQ_BK_SERIES_TO")) + "\n" );
            //////
            //////                        str.append(resourceBundle.getString("lblStartChequeNo") + ": " +  CommonUtil.convertObjToStr(dataMap.get("START_CHQ_NO1")) + " " +
            //////                        CommonUtil.convertObjToStr(dataMap.get("START_CHQ_NO2")) + "\n" );
            //////
            //////                        str.append(resourceBundle.getString("lblEndChequeNo") + ": " + CommonUtil.convertObjToStr(dataMap.get("END_CHQ_NO1")) + " " +
            //////                        CommonUtil.convertObjToStr(dataMap.get("END_CHQ_NO2")) + "\n" );
            ////
            ////                        str.append(resourceBundle.getString("lblSeriesNoFrom") + ": " + CommonUtil.convertObjToStr(dataMap.get("BK_SERIES_FROM")) + "\n" );
            ////                        str.append(resourceBundle.getString("lblSeriesNoTo") + ": " + CommonUtil.convertObjToStr(dataMap.get("BK_SERIES_TO")) + "\n" );
            ////
            ////                        str.append(resourceBundle.getString("lblStartChequeNo") + ": " +  CommonUtil.convertObjToStr(dataMap.get("START_LEAF_NO1")) + " " +
            ////                        CommonUtil.convertObjToStr(dataMap.get("START_LEAF_NO2")) + "\n" );
            ////
            ////                        str.append(resourceBundle.getString("lblEndChequeNo") + ": " + CommonUtil.convertObjToStr(dataMap.get("END_LEAF_NO1")) + " " +
            ////                        CommonUtil.convertObjToStr(dataMap.get("END_LEAF_NO2")) + "\n" );
            ////
            ////                        authDataMap.putAll(dataMap);
            ////                        authList = new ArrayList();
            ////                        authList.add(authDataMap);
            ////
            ////                        System.out.println("In UI AuthDataMap: " + authDataMap);
            ////                        map.put(CommonConstants.AUTHORIZEDATA, authList);
            ////                        authData(map);
            ////
            ////                        authList = null;
            ////                    }else{
            ////                        StringBuffer alert = new StringBuffer();
            ////                        alert.append(resourceBundle.getString("CHEQUEBOOKWARNING") + "\n");
            ////                        alert.append(resourceBundle.getString("AVAILBOOKS") + CommonUtil.convertObjToDouble(dataMap.get("AVAILABLE_BOOKS")) + "\n");
            ////                        alert.append(resourceBundle.getString("ORDEREDBOOKS") + CommonUtil.convertObjToDouble(authDataMap.get("NO OF BOOKS")) + "\n\n");
            ////                        alert.append(resourceBundle.getString("REORDERBOOKS"));
            ////                        displayAlert(alert.toString());
            ////
            ////                        isFilled = false;
            ////                        viewType = -1;
            ////                        //                    btnSave.setEnabled(true);
            ////                        observable.setResultStatus();
            ////                        observable.setAuthorizeMap(null);
            ////                        btnCancelActionPerformed(null);
            ////
            ////                        break;
            ////                    }
            ////
            ////                }
            ////                //__ To Display the ChequeBook details of all records, which are Authorized...
            ////                if(str.toString().length() > 0){
            ////                    displayInfo(str.toString());
            ////                }
            //            }
            //            else{
            //                authData(map);
            //            }
            //        }
            //        //__ If the ChequeStop Payment or Loose Leaf data is to be Authorized...
            //        else{
            //            authData(map);
            //        }
//            HashMap data = new HashMap();
//            data.put("DATA", map.get("AUTHORIZEDATA"));
//            if(data.containsKey("CHQ_ISSUE_ID")){
//              map.put("TRANSACTION TYPE", "TRANS_OUT");
//              map.put("ITEM_TYPE", "CHEQUES");
//           }
            System.out.println("### in authorize() map : "+map);
            authData(map);
            if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
                isAuth = true;
                isFilled = false;
                viewType = -1;
                btnSave.setEnabled(true);
                if (fromNewAuthorizeUI) {
                    newauthorizeListUI.removeSelectedRow();
                    this.dispose();
                    newauthorizeListUI.setFocusToTable();
                    if (panEditDelete == CHEQUE) {
                        newauthorizeListUI.displayDetails("Cheque Book Issue");
                    }
                    if (panEditDelete == STOP) {
                        newauthorizeListUI.displayDetails("Stop Payment Issue");
                    }
                    if (panEditDelete == ECS) {
                        newauthorizeListUI.displayDetails("ECS Stop Payment");
                    }
                }
                if (fromAuthorizeUI) {
                    authorizeListUI.removeSelectedRow();
                    this.dispose();
                    authorizeListUI.setFocusToTable();
                    if(panEditDelete == CHEQUE)
                        authorizeListUI.displayDetails("Cheque Book Issue");
                    if(panEditDelete == STOP)
                        authorizeListUI.displayDetails("Stop Payment Issue");
                    if(panEditDelete == ECS)
                    authorizeListUI.displayDetails("ECS Stop Payment");
                }
                btnCancelActionPerformed(null);
                observable.setResultStatus();
                
            }
            observable.setAuthorizeMap(null);
            
        }catch(Exception e){
            System.out.println("Error in authorize()");
            e.printStackTrace();
        }
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
    //__ To Send Auth Data to OB...
    private void authData(HashMap map){
        observable.setAuthorizeMap(map);
        observable.doAction(pan);
        
    }
    
    //    private HashMap generateChequeNos(HashMap inputMap){
    //        HashMap resultMap = new HashMap();
    //
    //        //__ To set the Data for the Cheque-book Series and Cheque Leaves...
    //        try{
    //            resultMap = observable.getChequeBookData(CommonUtil.convertObjToStr(inputMap.get("USAGE")), CommonUtil.convertObjToStr(inputMap.get("NO OF LEAVES")));
    //            if(resultMap.containsKey("BOOK_SLNO_FROM")){
    //                long chequeBooks = CommonUtil.convertObjToLong(inputMap.get("NO OF BOOKS"));
    //                long seriesFrom = CommonUtil.convertObjToLong(resultMap.get("BOOK_SLNO_FROM"));
    //                long seriesTo = CommonUtil.convertObjToLong(resultMap.get("BOOK_SLNO_TO"));
    //
    //                long difference = (seriesTo - seriesFrom) + 1;
    //                System.out.println("chequeBooks: " + chequeBooks);
    //                System.out.println("seriesFrom : " + seriesFrom);
    //                System.out.println("seriesTo   : " + seriesTo);
    //
    //
    //                if(chequeBooks > 0){
    //                    if(difference >=  chequeBooks){
    //                        final String prefix = CommonUtil.convertObjToStr(resultMap.get("INSTRUMENT_PREFIX"));
    //                        long leavesFrom = CommonUtil.convertObjToLong(resultMap.get("LEAVES_SLNO_FROM"));
    //                        long chequeLeaves = CommonUtil.convertObjToLong(inputMap.get("NO OF LEAVES"));
    //                        long toSeries = seriesFrom + chequeBooks - 1;
    //
    //                        resultMap.put("CHQ_BK_SERIES_FROM", String.valueOf(seriesFrom));
    //                        resultMap.put("CHQ_BK_SERIES_TO", String.valueOf(toSeries));
    //                        resultMap.put("START_CHQ_NO1", prefix);
    //                        resultMap.put("START_CHQ_NO2", String.valueOf(leavesFrom));
    //                        resultMap.put("END_CHQ_NO1", prefix);
    //                        resultMap.put("END_CHQ_NO2", String.valueOf(leavesFrom - 1 + (chequeLeaves * chequeBooks)));
    //
    //                        //__ if the Series is Over...
    //                        if(toSeries >= seriesTo){
    //                            resultMap.put("SERIES_OVER", "YES");
    //                        }
    //                    }else{
    //                        resultMap.put("AVAILABLE_BOOKS",String.valueOf(difference));
    //                    }
    //                }
    //            }
    //        }catch(Exception E){
    //            System.out.println("Error While generating the Cheque Nos...");
    //            E.printStackTrace();
    //        }
    //        return resultMap;
    //    }
    private void rdoLeaf_BulkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoLeaf_BulkActionPerformed
        // Add your handling code here:
        observable.setRdoLeaf_Bulk(rdoLeaf_Bulk.isSelected());
        txtEndCheque.setEnabled(true);
        txtEndCheque.setEditable(true);
        
        txtEndingChequeNo.setEnabled(true);
        txtEndingChequeNo.setEditable(true);
    }//GEN-LAST:event_rdoLeaf_BulkActionPerformed
    
    private void rdoLeaf_singleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoLeaf_singleActionPerformed
        // Add your handling code here:
        observable.setRdoLeaf_single(rdoLeaf_single.isSelected());
        if(rdoLeaf_single.isSelected()) {
            txtEndCheque.setText("");
            txtEndCheque.setEnabled(false);
            txtEndCheque.setEditable(false);
            
            txtEndingChequeNo.setText("");
            txtEndingChequeNo.setEnabled(false);
            txtEndingChequeNo.setEditable(false);
        }
    }//GEN-LAST:event_rdoLeaf_singleActionPerformed
    
    private void btnPaymentRevokeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPaymentRevokeActionPerformed
        // Add your handling code here:
        isRevoked = true;
        ClientUtil.enableDisable(this, true);
        btnPaymentRevoke.setEnabled(false);
        lblReasonForRevoke.setVisible(true);
        txtStopReasonForRevoke.setVisible(true);
        txtStopReasonForRevoke.setEnabled(true);
        txtStopReasonForRevoke.setEditable(true);
        lblChqRevokeDt.setVisible(true);
        lblChqRevokeDtVal.setVisible(true);
        Date curDt = (Date) currDt.clone();
        lblChqRevokeDtVal.setText(CommonUtil.convertObjToStr(curDt));
        setEnableDisableChqPan(false);
        observable.setStopStat("REVOKED");
//        observable.paymentRevoke();
//        resetUI(STOP);                       //__ to Reset all the Fields and Status in UI...
//        setButtonEnableDisable();            //__ Enables/Disables the necessary buttons and menu items...
//        ClientUtil.enableDisable(this, false); //__  Disables the panel...
//        observable.setResultStatus();        //__ To Reset the Value of lblStatus...
        
//        resetPanels();
//        
//        //__ Make the Screen Closable..
//        setModified(false);
    }//GEN-LAST:event_btnPaymentRevokeActionPerformed
    
    private void btnAccountNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccountNumberActionPerformed
        // Add your handling code here:
        popUp(ACCNOLOOSE);
    }//GEN-LAST:event_btnAccountNumberActionPerformed
    
    private void btnAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccNoActionPerformed
        // Add your handling code here:
        popUp(ACCNOSTOP);
    }//GEN-LAST:event_btnAccNoActionPerformed
    //The Product Id ComboBox in Loose Leaf Issues...
    private void cboProductIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductIDActionPerformed
        // Add your handling code here:
        //To Set the Value of Account Head in UI...
        observable.setCboProductID((String) cboProductID.getSelectedItem());
        if( observable.getCboProductID().length() > 0){
            //When the selected Product Id is not empty string
            final String PRODTYPE = CommonUtil.convertObjToStr((((ComboBoxModel)(cboProduct_Type).getModel())).getKeyForSelected());
            final String PRODID = CommonUtil.convertObjToStr((observable.getCbmProductID()).getKeyForSelected());
            HashMap resultMap = observable.setAccountHead(PRODTYPE, PRODID);
            
            observable.setLblProductIDAccHdId(CommonUtil.convertObjToStr(resultMap.get("AC_HEAD")));
            observable.setLblProductIDAccHdDesc(CommonUtil.convertObjToStr(resultMap.get("AC_HEAD_DESC")));
            
            //__ To set the Value of Account head in "Loose Leaf"...
            lblAccHdId.setText(observable.getLblProductIDAccHdId());
            lblAccHdDesc.setText(observable.getLblProductIDAccHdDesc());
            //            observable.ttNotifyObservers();
        }
    }//GEN-LAST:event_cboProductIDActionPerformed
    //The Product Id ComboBox in Stop Payment Issues...
    private void cboProductIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductIdActionPerformed
        // Add your handling code here:
        //To Set the Value of Account Head in UI...
        observable.setCboProductId((String) cboProductId.getSelectedItem());
        if( observable.getCboProductId().length() > 0){
            //When the selected Product Id is not empty string
            final String PRODTYPE = CommonUtil.convertObjToStr((((ComboBoxModel)(cboProductType).getModel())).getKeyForSelected());
            final String PRODID = CommonUtil.convertObjToStr((observable.getCbmProductId()).getKeyForSelected());
            HashMap resultMap = observable.setAccountHead(PRODTYPE, PRODID);
            
            observable.setLblProductIdAccHdId(CommonUtil.convertObjToStr(resultMap.get("AC_HEAD")));
            observable.setLblProductIdAccHdDesc(CommonUtil.convertObjToStr(resultMap.get("AC_HEAD_DESC")));
            
            // To set the Value of Account head in "Loose Leaf"...
//            lblAccHeadId.setText(observable.getLblProductIdAccHdId());
            lblAccHeadDesc.setText(observable.getLblProductIdAccHdDesc());
            //            observable.ttNotifyObservers();
        }
    }//GEN-LAST:event_cboProductIdActionPerformed
    
    private void btnAccountNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccountNoActionPerformed
        // Add your handling code here:
        popUp(ACCNOCHEQUE);
    }//GEN-LAST:event_btnAccountNoActionPerformed
    // The Product Id ComboBox in Cheque book issue...
    private void cboProdIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdIdActionPerformed
        // Add your handling code here:
        //To Set the Value of Account Head in UI...
        observable.setCboProdId((String) cboProdId.getSelectedItem());
        if( observable.getCboProdId().length() > 0){
            final String PRODTYPE = CommonUtil.convertObjToStr((((ComboBoxModel)(cboProdType).getModel())).getKeyForSelected());
            final String PRODID = CommonUtil.convertObjToStr((observable.getCbmProdId()).getKeyForSelected());
            
            //When the selected Product Id is not empty string
            HashMap resultMap = observable.setAccountHead(PRODTYPE, PRODID);
            observable.setLblAccountHeadProdId(CommonUtil.convertObjToStr(resultMap.get("AC_HEAD")));
            observable.setLblAccountHeadDesc(CommonUtil.convertObjToStr(resultMap.get("AC_HEAD_DESC")));
            
            System.out.println("Achd Map : " +resultMap);
            
            // To set the Value of Account head in "Cheque book Issue"...
            lblAccountHeadId.setText(observable.getLblAccountHeadProdId());
            lblAccountHeadDesc.setText(observable.getLblAccountHeadDesc());
            
            //            observable.ttNotifyObservers();
        }
    }//GEN-LAST:event_cboProdIdActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
        //        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // Add your handling code here:
         HashMap reportParamMap = new HashMap();
 com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed
    private void setEnableDisableChequeDetails(boolean value){
//        ClientUtil.enableDisable(panSeriesNo, value);
        
        cboProdType.setEnabled(value);
        cboProdId.setEnabled(value);
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW)
            txtAccountNo.setEnabled(true);
        else
            txtAccountNo.setEnabled(false);
        txtNamesOfAccount.setEnabled(value);
        btnAccountNo.setEnabled(value);
        cboUsage.setEnabled(value);
        
        //        txtNoOfChequeBooks.setEditable(value);
        //        txtNoOfChequeBooks.setEnabled(value);
        //        cboNoOfLeaves.setEnabled(value);
    }
    private void setEnableDisableChqPan(boolean flag){
        cboProductType.setEnabled(flag);
        cboProductId.setEnabled(flag);
        txtAccNo.setEnabled(flag);
        btnAccNo.setEnabled(flag);
        panLeaf.setEnabled(flag);
        panStartChequeNo.setEnabled(flag);
        panEndChequeNo.setEnabled(flag);
        tdtChequeDate.setEnabled(flag);
        txtChequeAmt.setEnabled(flag);
        txtPayeeName.setEnabled(flag);
        txtStopPaymentCharges.setEnabled(flag);
        cboReasonStopPayment.setEnabled(flag);
        rdoLeaf_single.setEnabled(flag);
        rdoLeaf_Bulk.setEnabled(flag);
        txtStartCheque.setEnabled(flag);
        txtStartchequeNo.setEnabled(flag);
        txtEndCheque.setEnabled(flag);
        txtEndingChequeNo.setEnabled(flag);
    }
    private void setEnableDisableStopDetails(boolean value){
        cboProductType.setEnabled(value);
        cboProductId.setEnabled(value);
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW)
            txtAccNo.setEnabled(true);
        else
            txtAccNo.setEnabled(false);
        btnAccNo.setEnabled(value);
    }
    
    private void setEnableDisableLeafDetails(boolean value){
        cboProduct_Type.setEnabled(value);
        cboProductID.setEnabled(value);
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW)
            txtAccounNumber.setEnabled(true);
        else
            txtAccounNumber.setEnabled(false);
        btnAccountNumber.setEnabled(value);
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
//        super.removeEditLock(chqIssueId);
        if(pan!=-1){
            resetUI(pan);// to Reset all the Fields and Status in UI...
        }else{
            resetUI(panEditDelete);
        }
        
        //        if(AUTH){
        //            btnSave.setEnabled(true);
        //        }
        
        //__
        if(!btnSave.isEnabled()){
            btnSave.setEnabled(true);
        }
        if(panChequeBookIssue.isShowing()==true){
            if(observable.getAuthorizeStatus()!=null)
                super.removeEditLock(observable.getChequeIssueId()); 

        }else if(panStopPaymentIssue.isShowing()==true){
            isRevoked = false;
            if(observable.getAuthorizeStatus()!=null)
                super.removeEditLock(observable.getChequeStopId()); 

        }else if(panLooseLeafIssue.isShowing()==true){
            if(observable.getAuthorizeStatus()!=null)
                super.removeEditLock(observable.getChequeLeafId()); 

        }
        else if(panEcsStopPaymentIssue.isShowing()==true){
            if(observable.getAuthorizeStatus()!=null)
                super.removeEditLock(observable.getEcsStopId()); 

        }
        
        setButtonEnableDisable();    //__ Enables/Disables the necessary buttons and menu items...
        ClientUtil.enableDisable(this, false); //__ Disables the panel...
        disableButtons();                      //__ to Disable the Folder buttons...
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);  //__ Sets the Action Type to be performed...
        observable.setStatus();                                       //__ To set the Value of lblStatus...
        isFilled = false;
        viewType = -1;
        resetPanels();            //__ To Reset the Value of the Selected Panle of the Panels...
        lblChqStopDtVal.setText("");
        
        //__ Make the Screen Closable..
        setModified(false);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        btnView.setEnabled(true);
        txtStopReasonForRevoke.setVisible(false);
        lblReasonForRevoke.setVisible(false);
        txtEcsStopReasonForRevoke.setVisible(false);
        lblEcsReasonForRevoke.setVisible(false);
        lblEcsRevokeDt.setVisible(false);
        lblEcsRevokeDtVal.setVisible(false);
        lblCustIdDesc1.setText("");
        lblCustNameValue1.setText("");
        lblCustIdDesc.setText("");
        iEcsRevoked=false;
         if (fromNewAuthorizeUI) {
            this.dispose();
            fromNewAuthorizeUI= false;
            newauthorizeListUI.setFocusToTable();
        }
        if (fromAuthorizeUI) {
            this.dispose();
            fromAuthorizeUI = false;
            authorizeListUI.setFocusToTable();
        }
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        HashMap whereMap = new HashMap();
        if(cboProdId.getSelectedIndex()>0 && cboProdId.getSelectedItem()!=null)
        {
        String prodID = ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString();
        System.out.println("prodID 0000  ==== "+prodID);
        if(prodID!=null && !prodID.equals("") && prodID.length()>0){
            System.out.println("prodType btnSaveActionPerformed : " + prodID);
            whereMap.put(" prodID", prodID);
            System.out.println("whereMap btnSaveActionPerformed : " + whereMap);
            List minBalLst = ClientUtil.executeQuery("getMinimumBalance", whereMap);
            if (minBalLst != null && minBalLst.size() > 0) {
                HashMap minBalMap = (HashMap) minBalLst.get(0);
                System.out.println("minBalLst : " + minBalMap.get("MIN_BAL_WT_CHK"));
                double minBal = CommonUtil.convertObjToDouble(minBalMap.get("MIN_BAL_WT_CHK"));
                HashMap newwhereMap = new HashMap();
                String actNo = txtAccountNo.getText();
                System.out.println("actNo : " + actNo);
                newwhereMap.put("actNo", actNo);
                List clearBalanceLst = ClientUtil.executeQuery("getClearBalanceForCheque", newwhereMap);
                if (clearBalanceLst != null && clearBalanceLst.size() > 0) {
                    HashMap clearBalanceMap = (HashMap) clearBalanceLst.get(0);
                    System.out.println("clearBalanceLst : " + clearBalanceMap.get("CLEAR_BAL_FOR_CK"));
                    double clearBal = CommonUtil.convertObjToDouble(clearBalanceMap.get("CLEAR_BAL_FOR_CK"));
                    if (minBal > clearBal) {
                        ClientUtil.displayAlert("Available balance is less than minimum balance");
                        return;
                    }
                    }
                }
            }
        }
        //Commented by sreekrishnan for stop payment issue
         //if(txtNoOfChequeBooks.getText().equals("")){
           //  ClientUtil.displayAlert("Enter No of Cheque books");
             //return;
        //}
         //if(cboNoOfLeaves.getSelectedItem().equals("")){
           // ClientUtil.displayAlert("Select No of Leaves");
            //return;
        //}
        updateOBFields();
        String mandatoryMessage = "";
        StringBuffer str  =  new StringBuffer();
        if(panChequeBookIssue.isShowing()==true){
            //To display an alert if the mandatory fields are not properly inputted, else proceed with normal operation
            mandatoryMessage =  new MandatoryCheck().checkMandatory(getClass().getName(), panChequeBookIssue);
            if(mandatoryMessage.length() > 0){
                str.append(mandatoryMessage + "\n");
            }
            
            //__ if the No of Cheque Books Ordered are not greater than 0, Display Alert...
            if(!(CommonUtil.convertObjToDouble(txtNoOfChequeBooks.getText()).doubleValue() > 0)){
                str.append(resourceBundle.getString("CHEQUE_BOOK_NOWARNING"));
            }
            
        }else if(panStopPaymentIssue.isShowing()==true){
            //To display an alert if the mandatory fields are not properly inputted, else proceed with normal operation
            mandatoryMessage =  new MandatoryCheck().checkMandatory(getClass().getName(), panStopPaymentIssue);
            if(mandatoryMessage.length() > 0){
                str.append(mandatoryMessage + "\n");
            }
            
        }else if(panLooseLeafIssue.isShowing()==true){
            //To display an alert if the mandatory fields are not properly inputted, else proceed with normal operation
            mandatoryMessage =  new MandatoryCheck().checkMandatory(getClass().getName(), panLooseLeafIssue);
            if(mandatoryMessage.length() > 0){
                str.append(mandatoryMessage + "\n");
            }
            
        }
        else if(panEcsStopPaymentIssue.isShowing()==true){
            //To display an alert if the mandatory fields are not properly inputted, else proceed with normal operation
            mandatoryMessage =  new MandatoryCheck().checkMandatory(getClass().getName(), panEcsStopPaymentIssue);
            if(mandatoryMessage.length() > 0){
                str.append(mandatoryMessage + "\n");
            }
            
        }
        
        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && str.toString().length() > 0 ){
            displayAlert(str.toString());
        }
        else if(!observable.chkMinBalance() && panChequeBookIssue.isShowing()==true){
            ClientUtil.showAlertWindow("Available Balance is less than Minimum Balance");
        } //Commented by vinay 04-feb-08 for testing
        else{
            if(pan!=-1){
                 if(isRevoked){
                    observable.paymentRevoke();
                    resetUI(STOP);                       //__ to Reset all the Fields and Status in UI...
                    setButtonEnableDisable();            //__ Enables/Disables the necessary buttons and menu items...
                    ClientUtil.enableDisable(this, false); //__  Disables the panel...
                    observable.setResultStatus();
                    btnSave.setEnabled(false);
                }
                 else if(iEcsRevoked){
                    observable.EcspaymentRevoke();
                    resetUI(ECS);                       //__ to Reset all the Fields and Status in UI...
                    setButtonEnableDisable();            //__ Enables/Disables the necessary buttons and menu items...
                    ClientUtil.enableDisable(this, false); //__  Disables the panel...
                    observable.setResultStatus();
                    btnSave.setEnabled(false);
                }else{
                    observable.doAction(pan);    //__ To perform the necessary operation depending on the Action type...
                }
             }else if(panEditDelete!=-1){
                if(isRevoked){
                    observable.paymentRevoke();
                    resetUI(STOP);                       //__ to Reset all the Fields and Status in UI...
                    setButtonEnableDisable();            //__ Enables/Disables the necessary buttons and menu items...
                    ClientUtil.enableDisable(this, false); //__  Disables the panel...
                    observable.setResultStatus(); 
                }
                else if(iEcsRevoked){
                    observable.EcspaymentRevoke();
                    resetUI(ECS);                       //__ to Reset all the Fields and Status in UI...
                    setButtonEnableDisable();            //__ Enables/Disables the necessary buttons and menu items...
                    ClientUtil.enableDisable(this, false); //__  Disables the panel...
                    observable.setResultStatus(); 
                }else{
                    System.out.println("panEditDelete");
                    observable.doAction(panEditDelete); //__ To perform the necessary operation depending on the Action type...
                    }
                }
            
            /*
             * If the Result is Failed, don't clear the fields...
             */
            if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
                
                HashMap lockMap = new HashMap();
                ArrayList lst = new ArrayList();
                if(panChequeBookIssue.isShowing()==true){
                    lst.add("CHQ_ISSUE_ID");
                    lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                    if (observable.getProxyReturnMap()!=null) {
                        if (observable.getProxyReturnMap().containsKey("CHQ_ISSUE_ID")) {
                            lockMap.put("CHQ_ISSUE_ID", observable.getProxyReturnMap().get("CHQ_ISSUE_ID"));
                        }
                    }
                    if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                        lockMap.put("CHQ_ISSUE_ID", observable.getChequeIssueId());
                    }
                    setEditLockMap(lockMap);
                    setEditLock();
                }else if(panStopPaymentIssue.isShowing()==true){
                    lst.add("CHQ_STOP_ID");
                    lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                    if (observable.getProxyReturnMap()!=null) {
                        if (observable.getProxyReturnMap().containsKey("CHQ_STOP_ID")) {
                            lockMap.put("CHQ_STOP_ID", observable.getProxyReturnMap().get("CHQ_STOP_ID"));
                        }
                    }
                    if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                        lockMap.put("CHQ_STOP_ID", observable.getChequeStopId());
                    }
                    setEditLockMap(lockMap);
                    setEditLock();
                }else if(panLooseLeafIssue.isShowing()==true){
                    lst.add("CHQ_LEAF_ID");
                    lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                    if (observable.getProxyReturnMap()!=null) {
                        if (observable.getProxyReturnMap().containsKey("CHQ_LEAF_ID")) {
                            lockMap.put("CHQ_LEAF_ID", observable.getProxyReturnMap().get("CHQ_LEAF_ID"));
                        }
                    }
                    if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                        lockMap.put("CHQ_LEAF_ID", observable.getChequeLeafId());
                    }
                    setEditLockMap(lockMap);
                    setEditLock();
                }
                else if(panEcsStopPaymentIssue.isShowing()==true){
                    lst.add("ECS_STOP_ID");
                    lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                    if (observable.getProxyReturnMap()!=null) {
                        if (observable.getProxyReturnMap().containsKey("ECS_STOP_ID")) {
                            lockMap.put("ECS_STOP_ID", observable.getProxyReturnMap().get("ECS_STOP_ID"));
                        }
                    }
                    if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                        lockMap.put("ECS_STOP_ID", observable.getEcsStopId());
                    }
                    setEditLockMap(lockMap);
                    setEditLock();
                }
                if(pan != -1){
                    resetUI(pan);// to Reset all the Fields and Status in UI...
                }else{
                    resetUI(panEditDelete);// to Reset all the Fields and Status in UI...
                }
                ClientUtil.enableDisable(this, false);// Disables the panel...
                disableButtons();// To Disable the Folder buttons...
                setButtonEnableDisable();// Enables or Disables the buttons and menu Items depending on their previous state...
                observable.setResultStatus();// To Reset the Value of lblStatus...
                
                resetPanels();// To Reset the Value of the Selected Panle of the Panels...
                
                //__ Make the Screen Closable..
                setModified(false);
            }
        }
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    
    private void displayInfo(String message){
        CMandatoryDialog cmd = new CMandatoryDialog(INFO_MESSAGE);
        cmd.setMessage(message);
        cmd.show();
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        if(panChequeBookIssue.isShowing()==true){
            panEditDelete=CHEQUE;
        }else if(panStopPaymentIssue.isShowing()==true){
            panEditDelete=STOP;
        }else if(panLooseLeafIssue.isShowing()==true){
            panEditDelete=LEAF;
        }
        else if(panEcsStopPaymentIssue.isShowing()==true){
            panEditDelete=ECS;
        }
        resetUI(panEditDelete);// to Reset all the Fields and Status in UI...
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);//Sets the Action Type to be performed...
        //observable.setStatus();// To set the Value of lblStatus...
        popUp(DELETE);
         btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        if(panChequeBookIssue.isShowing()==true){
            panEditDelete=CHEQUE;
             txtStartingCheque.setEditable(true);
             txtStartingChequeNo.setEditable(true);
            
            
        }else if(panStopPaymentIssue.isShowing()==true){
            panEditDelete=STOP;
            
        }else if(panLooseLeafIssue.isShowing()==true){
            panEditDelete=LEAF;
        }
        else if(panEcsStopPaymentIssue.isShowing()==true){
            panEditDelete=ECS;
        }
        resetUI(panEditDelete);// to Reset all the Fields and Status in UI...
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);//Sets the Action Type to be performed...
        popUp(EDIT);
       
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
    // To display the All the Product Id's which r having status as
    // created or updated, in a table...
    private void popUp(int field) {
        ViewAll viewAll;
        final HashMap viewMap = new HashMap();
        
        HashMap whereListMap = new HashMap();
        //__ AccountNo selection is Account Dependent...
        whereListMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
        
        viewMap.put(CommonConstants.MAP_WHERE, whereListMap);
        
        viewType = field;
        String title = "";
        if(field==EDIT || field==DELETE){         //__ Edit=0 and Delete=1
            ArrayList lst = new ArrayList();
            if(panEditDelete==CHEQUE){
                viewMap.put(CommonConstants.MAP_NAME, "viewChequeBook");
                title = resourceBundle.getString("chequeBookIssue");
                lst.add("CHQ_ISSUE_ID");   
               
            }else if(panEditDelete==STOP){
                 viewMap.put(CommonConstants.MAP_NAME, "viewStopPayment");
                     title = resourceBundle.getString("chequeStopPayment");
                     lst.add("CHQ_STOP_ID");  
                
            }else if(panEditDelete==LEAF){
                 viewMap.put(CommonConstants.MAP_NAME, "viewLooseLeaf");
                title = resourceBundle.getString("chequeLooseLeaf");
                lst.add("CHQ_LEAF_ID");
                
            }
            else if(panEditDelete==ECS){
                 viewMap.put(CommonConstants.MAP_NAME, "viewEcsPayment");
                     title = resourceBundle.getString("EcsStopPayment");
                     lst.add("ECS_STOP_ID");  
                
            }
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            
            viewAll = new ViewAll(this, viewMap);
            
        }else{
            
            String prodType = "";
            //__ To get the Product Type...
            if(field == ACCNOCHEQUE){
                prodType = CommonUtil.convertObjToStr((((ComboBoxModel)(cboProdType).getModel())).getKeyForSelected());
                
            }else if(field == ACCNOSTOP){
                prodType = CommonUtil.convertObjToStr((((ComboBoxModel)(cboProductType).getModel())).getKeyForSelected());
                
            }else if(field == ACCNOLOOSE){
                prodType = CommonUtil.convertObjToStr((((ComboBoxModel)(cboProduct_Type).getModel())).getKeyForSelected());
                
            }
            else if(field == ECSSTOP){
                prodType = CommonUtil.convertObjToStr((((ComboBoxModel)(cboEcsProductType).getModel())).getKeyForSelected());
                
            }
            updateOBFields();
            
            System.out.println("PRODTYPE: " +prodType);
            viewMap.put(CommonConstants.MAP_NAME, "getChequeAccountList" + prodType);
            
            
            if(panChequeBookIssue.isShowing()==true){
                if(view==ENQUIRY){
                    viewMap.put(CommonConstants.MAP_NAME, "viewChequeBook");
                    title = resourceBundle.getString("chequeBookIssue");
                }
                else{
                    whereListMap.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
                    title = resourceBundle.getString("chequeBookProdId") + (String) cboProdId.getSelectedItem();
                }
            }else if(panStopPaymentIssue.isShowing()==true){
                if(view==ENQUIRY){
                    viewMap.put(CommonConstants.MAP_NAME, "viewStopPayment");
                    title = resourceBundle.getString("EcsStopPayment");
                }
                else{
                    whereListMap.put("PROD_ID", ((ComboBoxModel) cboProductId.getModel()).getKeyForSelected());
                    title = resourceBundle.getString("chequeStopProdId") + (String) cboProductId.getSelectedItem();
                }
            }else if(panLooseLeafIssue.isShowing()==true){
                if(view==ENQUIRY){
                    viewMap.put(CommonConstants.MAP_NAME, "viewLooseLeaf");
                    title = resourceBundle.getString("chequeLooseLeaf");
                }
                else{
                    whereListMap.put("PROD_ID", ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected());
                    title = resourceBundle.getString("chequeLooseProdId") + (String) cboProductID.getSelectedItem();
                }
            }
            else if(panEcsStopPaymentIssue.isShowing()==true){
                if(view==ENQUIRY){
                    viewMap.put(CommonConstants.MAP_NAME, "viewEcsPayment");
                    title = resourceBundle.getString("EcsStopPayment");
                }
                else{
                    whereListMap.put("PROD_ID", ((ComboBoxModel) cboEcsProductId.getModel()).getKeyForSelected());
                    title = resourceBundle.getString("EcsLooseProdId") + (String) cboEcsProductId.getSelectedItem();
                }
            }
            viewAll = new ViewAll(this, viewMap, true);
        }
        
        //        ViewAll viewAll = new ViewAll(this, viewMap);
        viewAll.setTitle(title);
        viewAll.show();
    }
    
    
    private void fillChequeData(HashMap hash) {
        
        final String CHEQUEID = CommonUtil.convertObjToStr(hash.get("CHQ_ISSUE_ID"));
        final String ACCOUNTNO = CommonUtil.convertObjToStr(hash.get("ACCOUNT NUMBER"));
        final String PRODTYPE = CommonUtil.convertObjToStr(hash.get("PRODUCT TYPE"));
        final String AUTHSTATUS = CommonUtil.convertObjToStr(hash.get("AUTHORIZE STATUS"));
        final String TRANSACTIONID = CommonUtil.convertObjToStr(hash.get("TRANSACTION ID"));
        CHEQUEAUTHID = CHEQUEID;
        if(hash.containsKey("STATUS")){
            Status = CommonUtil.convertObjToStr(hash.get("STATUS"));
        }
        hash.put(CommonConstants.MAP_WHERE, CHEQUEID);
        //        hash.put(CommonConstants.MAP_WHERE, TRANSACTIONID);
        hash.put(CommonConstants.MAP_NAME, "getSelectChequeBookTO");
        
        //        System.out.println("Hash: " + hash);
        // Called to display the Data in the UI fields...
        observable.populateData(hash, panEditDelete);
        observable.setTransId(TRANSACTIONID);
        cboProdType.setSelectedItem(observable.getCboProdType());
        transDetails.setTransDetails(observable.getProdTypeTO(), getSelectedBranchID(), ACCOUNTNO);
        
        if ((observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT)
        && (!AUTHSTATUS.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED))){
            panChequeEnable();// To enable the ChequeBookIssue panel...
        }
        
        //        if(AUTHSTATUS.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)){
        //            btnSave.setEnabled(true);
        //            AUTH = true;
        //        }
        
        
        /*
         * To Diable the Cheque Details Data Panel...
         */
        setEnableDisableChequeDetails(false);
        
        observable.setChequeIssueId(CHEQUEID);    //__ To set the value of Cheque id in Observable...
        observable.setTxtAccountNo(ACCOUNTNO);    //__ TO Set the value of AccountNo in OB...
        
        //__ To get the Customer Name...
        HashMap resultMap = observable.setCustName(PRODTYPE, ACCOUNTNO);
        observable.setLblCustName(CommonUtil.convertObjToStr(resultMap.get("CUSTOMER_NAME")));
        
        resultMap = null;
        //__ To get the Available and Total Balance...
        resultMap = observable.setCustomer(ACCOUNTNO);
        
        observable.setLblCuctId(CommonUtil.convertObjToStr(resultMap.get("CUST_ID")));
        observable.chequeIssued();
        
        observable.fillChequeTab(ACCOUNTNO); //__ To Set the Value in Table...fillChequeTab
        observable.ttNotifyObservers();
    }
    
    private void fillStopData(HashMap hash){
        final String STOPID = CommonUtil.convertObjToStr(hash.get("CHQ_STOP_ID"));
        final String ACCOUNTNO = CommonUtil.convertObjToStr(hash.get("ACCOUNT NUMBER"));
        final String PRODTYPE = CommonUtil.convertObjToStr(hash.get("PRODUCT TYPE"));
        hash.put(CommonConstants.MAP_WHERE, STOPID);
        
        hash.put(CommonConstants.MAP_NAME, "getSelectChequeBookStopPaymentTO");
        
        observable.populateData(hash, panEditDelete); //__ Called to display the Data in the UI fields...
        
        final String AUTHSTATUS = observable.getAuthStatus();
        
        cboProductType.setSelectedItem(observable.getCboProductType());
        setStopStatus(CommonUtil.convertObjToStr(hash.get("STOP_STATUS")));
        if((CommonUtil.convertObjToStr(hash.get("STOP_STATUS")).equalsIgnoreCase("REVOKED")))
                {
                    txtStopReasonForRevoke.setVisible(true);
                    lblReasonForRevoke.setVisible(true);
                    lblChqRevokeDt.setVisible(true);
                    lblChqRevokeDtVal.setVisible(true);
                }
        
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            if((AUTHSTATUS.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) ||
            ((CommonUtil.convertObjToStr(hash.get("STOP_STATUS")).equalsIgnoreCase("REVOKED")) &&
            (AUTHSTATUS.equalsIgnoreCase(CommonConstants.STATUS_REJECTED)))){
                btnPaymentRevoke.setEnabled(true);
            }else{
                panStopEnable();   //__ To enable the StopPaymentIssue panel...
            }
        }
        
        
        //__ To Disable Fields at the time of Edit...
        setEnableDisableStopDetails(false);
        
        //        btnPaymentRevoke.setEnabled(true);
        observable.setChequeStopId(STOPID); //__ to set the value of Cheque id in Observable...
        observable.setTxtAccNo(ACCOUNTNO);  //__ TO Set the value of AccountNo in OB...
        
        HashMap resultMap = observable.setCustName(PRODTYPE, ACCOUNTNO);    //__To Set the Value of Account holder Name in UI...
        observable.setLblCustStopName(CommonUtil.convertObjToStr(resultMap.get("CUSTOMER_NAME")));
        
        HashMap result = observable.setCustomer(ACCOUNTNO); //__ To select the value of custId...
        observable.setLblCustStopId(CommonUtil.convertObjToStr(result.get("CUST_ID")));
        observable.ttNotifyObservers();
    }
    private void fillEcsStopData(HashMap hash){
        final String ECSSTOPID = CommonUtil.convertObjToStr(hash.get("ECS_STOP_ID"));
        final String ACCOUNTNO = CommonUtil.convertObjToStr(hash.get("ACCOUNT NUMBER"));
        final String PRODTYPE = CommonUtil.convertObjToStr(hash.get("PRODUCT TYPE"));
        hash.put(CommonConstants.MAP_WHERE, ECSSTOPID);
        hash.put(CommonConstants.MAP_NAME, "getSelectEcsStopPaymentTO");
        
        observable.populateData(hash, panEditDelete);
         cboEcsProductType.setSelectedItem(observable.getCboEcsProdType());
        final String AUTHSTATUS = observable.getAuthStatus();
        observable.setEcsStopId(ECSSTOPID); //__ to set the value of Ecs id in Observable...
        observable.setEcsAcctNo(ACCOUNTNO);  //__ TO Set the value of AccountNo in OB...
        
        HashMap resultMap = observable.setCustName(PRODTYPE, ACCOUNTNO);    //__To Set the Value of Account holder Name in UI...
        observable.setLblCustEcsStopName(CommonUtil.convertObjToStr(resultMap.get("CUSTOMER_NAME")));
        
        HashMap result = observable.setCustomer(ACCOUNTNO); //__ To select the value of custId...
        lblCustIdDesc1.setText(CommonUtil.convertObjToStr(result.get("CUST_ID")));
        lblCustNameValue1.setText(CommonUtil.convertObjToStr(resultMap.get("CUSTOMER_NAME")));
        observable.setLblCustEcsStopId(CommonUtil.convertObjToStr(result.get("CUST_ID")));
        if((CommonUtil.convertObjToStr(hash.get("STOP_STATUS")).equalsIgnoreCase("REVOKED")))
                {
                    txtEcsStopReasonForRevoke.setVisible(true);
                    lblEcsReasonForRevoke.setVisible(true);
                    lblEcsRevokeDt.setVisible(true);
                    lblEcsRevokeDtVal.setVisible(true);
                }
         if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            if((AUTHSTATUS.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) ||
            ((CommonUtil.convertObjToStr(hash.get("STOP_STATUS")).equalsIgnoreCase("REVOKED")) &&
            (AUTHSTATUS.equalsIgnoreCase(CommonConstants.STATUS_REJECTED)))){
                btnEcsPaymentRevoke.setEnabled(true);
                btnEcsAccNo.setEnabled(false);
            }else{
                 panEcsEnable();   //__ To enable the StopPaymentIssue panel...
            }
        }
        observable.ttNotifyObservers();
    }
    
    private void fillLeafData(HashMap hash){
        final String LEAFID = CommonUtil.convertObjToStr(hash.get("CHQ_LEAF_ID"));
        final String ACCOUNTNO = CommonUtil.convertObjToStr( hash.get("ACCOUNT NUMBER"));
        final String PRODTYPE = CommonUtil.convertObjToStr(hash.get("PRODUCT TYPE"));
        System.out.println("PRODTYPE@@@@@: " + PRODTYPE);
        hash.put(CommonConstants.MAP_WHERE, LEAFID);
        hash.put(CommonConstants.MAP_NAME, "getSelectChequeBookLooseLeafTO");
        
        observable.populateData(hash, panEditDelete);   //__ Called to display the Data in the UI fields...
        
        cboProduct_Type.setSelectedItem(observable.getCboProduct_Type());
        
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT)
            panLeafEnable();    //__ To enable the LooseLeafIssue panel...
        
        //__ To Disable Fields at the time of Edit...
        setEnableDisableLeafDetails(false);
        
        observable.setChequeLeafId(LEAFID); //__ to set the value of Cheque id in Observable...
        observable.setTxtAccounNumber(ACCOUNTNO);   //__ TO Set the value of AccountNo in OB...
        
        HashMap resultMap = observable.setCustName(PRODTYPE, ACCOUNTNO); //To Set the Value of Account holder Name in UI...
        observable.setLblCustLooseName(CommonUtil.convertObjToStr(resultMap.get("CUSTOMER_NAME")));
        
        HashMap result = observable.setCustomer(ACCOUNTNO);// To select the value of custId, Clear Balance, total Balance...
        observable.setLblCustLooseId(CommonUtil.convertObjToStr(result.get("CUST_ID")));
        observable.ttNotifyObservers();
    }
    
    private void fillChequeAccount(HashMap hash){
        
        final String ACCOUNTNO = CommonUtil.convertObjToStr(hash.get("ACCOUNTNO"));
        observable.setTxtAccountNo(ACCOUNTNO);// TO Set the value of AccountNo in OB...
        observable.setTxtNamesOfAccount(CommonUtil.convertObjToStr(hash.get("NAME")));
        
        //        HashMap resultMap =observable.setCustName(ACCOUNTNO);//To Set the Value of Account holder Name in UI...
        //        observable.setLblCustName(resultMap.get("CUSTOMER_NAME").toString());
        observable.setLblCustName(CommonUtil.convertObjToStr(hash.get("CUSTOMERNAME")));
        observable.setLblCuctId(CommonUtil.convertObjToStr(hash.get("CUSTOMERID")));
        
        HashMap result = observable.setCustomer(ACCOUNTNO);// To select the value of custId, Clear Balance, total Balance...
        //        System.out.println("observable.getProdTypeTO(): " + observable.getProdTypeTO());
        transDetails.setTransDetails(observable.getProdTypeTO(), getSelectedBranchID(), ACCOUNTNO);
        observable.chequeIssued();
        observable.fillChequeTab(ACCOUNTNO);// To Set the Value in Table...fillChequeTab
    }
    
    private void fillStopAccount(HashMap hash){
        final String ACCOUNTNO = CommonUtil.convertObjToStr(hash.get("ACCOUNTNO"));
        
        txtAccNo.setText(ACCOUNTNO);
        lblCustIdDesc.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMERID")));
        lblCustNameValue.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMERNAME")));
        System.out.println("prod_id!!!!!"+hash);
    }
    private void fillEcsStopAccount(HashMap hash){
        final String ACCOUNTNO = CommonUtil.convertObjToStr(hash.get("ACCOUNTNO"));
        
        txtEcsAccNo.setText(ACCOUNTNO);
        lblCustIdDesc1.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMERID")));
        lblCustNameValue1.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMERNAME")));
    }
    
    private void fillLeafAccount(HashMap hash){
        final String ACCOUNTNO = CommonUtil.convertObjToStr(hash.get("ACCOUNTNO"));
        txtAccounNumber.setText(ACCOUNTNO);
        lblCustomerIDValue.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMERID")));
        lblCustomNameValue.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMERNAME")));
        lblCustomNameValue.setToolTipText(CommonUtil.convertObjToStr(hash.get("CUSTOMERNAME")));
    }
    
    // this method is called automatically from ViewAll...
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        if(hash.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")){
            if (hash.containsKey("Cheque")) {
                panEditDelete = CHEQUE;
                tabChequeBook.setSelectedIndex(0);
            }
            if (hash.containsKey("Leaf")) {
                panEditDelete = LEAF;
                tabChequeBook.setSelectedIndex(2);
            }
            if (hash.containsKey("Stop")) {
                panEditDelete = STOP;
                tabChequeBook.setSelectedIndex(1);
            }
            if (hash.containsKey("ECS")) {
                panEditDelete = ECS;
                tabChequeBook.setSelectedIndex(3);
            }
                fromNewAuthorizeUI = true;
                newauthorizeListUI = (NewAuthorizeListUI) hash.get("PARENT");
                hash.remove("PARENT");
                Trans_Id = CommonUtil.convertObjToStr(hash.get("TRANSACTION ID"));
                viewType = AUTHORIZE;
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                observable.setStatus();
                transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
                btnSave.setEnabled(false);
                btnCancel.setEnabled(true);
                btnAuthorize.setEnabled(true);
                btnAuthorize.requestFocusInWindow();
                btnAuthorize.setFocusable(true);
                btnReject.setEnabled(false);
                rejectFlag=1;
             }
        if(hash.containsKey("FROM_AUTHORIZE_LIST_UI")){
            if (hash.containsKey("Cheque")) {
                panEditDelete = CHEQUE;
                tabChequeBook.setSelectedIndex(0);
            }
            if (hash.containsKey("Leaf")) {
                panEditDelete = LEAF;
                tabChequeBook.setSelectedIndex(2);
            }
            if (hash.containsKey("Stop")) {
                panEditDelete = STOP;
                tabChequeBook.setSelectedIndex(1);
            }
            if (hash.containsKey("ECS")) {
                panEditDelete = ECS;
                tabChequeBook.setSelectedIndex(3);
            }
            System.out.println("FROM_AUTHORIZE_LIST_UI");
                fromAuthorizeUI = true;
                authorizeListUI = (AuthorizeListUI) hash.get("PARENT");
                hash.remove("PARENT");
                Trans_Id = CommonUtil.convertObjToStr(hash.get("TRANSACTION ID"));
                viewType = AUTHORIZE;
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                observable.setStatus();
                transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
                btnSave.setEnabled(false);
                btnCancel.setEnabled(true);
                btnAuthorize.setEnabled(true);
                btnAuthorize.requestFocusInWindow();
                btnAuthorize.setFocusable(true);
                btnReject.setEnabled(false);
                rejectFlag=1;
             }
        chqIssueId = "" ;
        if (viewType==EDIT || viewType==DELETE || viewType==AUTHORIZE || viewType == VIEW) {
            System.out.println("123");
            chqIssueId = (String)hash.get("CHQ_ISSUE_ID");
            isFilled = true;
            System.out.println("panEditDeletepanEditDelete"+panEditDelete);
            if(panEditDelete==CHEQUE){
                System.out.println("kkkkkkkkk111111111");//__ pan selected is Cheque book issue...
                fillChequeData(hash);
                if(CommonUtil.convertObjToDouble(observable.getTxtChargesCollected()).doubleValue()>0){
                callServiceTaxCalculation(CommonUtil.convertObjToStr(observable.getTxtChargesCollected()));
                }
            }else if(panEditDelete==STOP){ 
                System.out.println("llllllllllllll");//__ pan selected is Stop Payment issue...
                fillStopData(hash);
                callServiceTaxCalculation(CommonUtil.convertObjToStr(observable.getTxtStopPaymentCharges()));
                lblChqStopDt.setVisible(true);
                lblChqStopDtVal.setVisible(true);
            }else if(panEditDelete==LEAF){       //__ pan selected is Loose Leaf issue...
                System.out.println("zzzzzzzzzzzzzzzz");
                fillLeafData(hash);
            }
            else if(panEditDelete==ECS){       //__ pan selected is Stop Payment issue...
                System.out.println("xxxxxxxxxxxxx");
                fillEcsStopData(hash);
                 callServiceTaxCalculation(CommonUtil.convertObjToStr(observable.getEcsStopPayChrg()));
            }
            setButtonEnableDisable();           //__ Enables/Disables the necessary buttons and menu items...
            observable.setStatus();             //__ To set the Value of lblStatus...
            if(viewType==AUTHORIZE) {
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                btnAuthorize.setEnabled(true);
                btnAuthorize.requestFocusInWindow();
                btnAuthorize.setFocusable(true);
            }
        }else if (viewType==ACCNOCHEQUE) {
            if( observable.getCboProdId().length() > 0){
                fillChequeAccount(hash);
            }
        }else if (viewType==ACCNOSTOP) {
            if( observable.getCboProductId().length() > 0){
                fillStopAccount(hash);
                setStopPaymentCharges();
            }
        }else if (viewType==ACCNOLOOSE) {
            if( observable.getCboProductID().length() > 0){
                fillLeafAccount(hash);
            }
        }
        else if (viewType==ECSSTOP) {
            if( observable.getCboEcsProdId().length() > 0){
                fillEcsStopAccount(hash);
                setStopPaymentCharges();
            }
        }
        if (viewType==EDIT || viewType==DELETE || viewType==AUTHORIZE || viewType == VIEW) {
            if(panEditDelete==STOP||panEditDelete==ECS){       //__ pan selected is Stop Payment issue...
                if ((observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) || 
                (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT)){
                    if((CommonUtil.convertObjToStr(hash.get("STOP_STATUS")).equalsIgnoreCase("STOPPED")) &&
                    (CommonUtil.convertObjToStr(hash.get("AUTHORIZE_STATUS")).equalsIgnoreCase(""))){
                        btnSave.setEnabled(true);
                    }else{
                        btnSave.setEnabled(true);  //CHANGED FROM FALSE
                    }
                }
            }
        }
        //__ To Save the data in the Internal Frame...
        setModified(true);
        if(rejectFlag==1){
           btnReject.setEnabled(false);
       }
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE){
	        btnAuthorize.setEnabled(true);
	        btnAuthorize.requestFocusInWindow();
	        btnAuthorize.setFocusable(true);
      }
    }
    
    //To Disable buttons(folder)...
    private void disableButtons() {
        btnAccountNo.setEnabled(false);
        btnAccNo.setEnabled(false);
        btnAccountNumber.setEnabled(false);
        btnPaymentRevoke.setEnabled(false);
        btnEcsPaymentRevoke.setEnabled(false);
    }
    
    //To reset the Panel Status...
    private void resetPanels() {
        pan=-1;
        panEditDelete=-1;
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        // Testing for the Selection of Panels...
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);   //Sets the Action Type to be performed...
        if(panChequeBookIssue.isShowing()==true){
            panChequeEnable();    // To enable the ChequeBookIssue panel...
            pan=CHEQUE;
        }else if(panStopPaymentIssue.isShowing()==true){
            isRevoked = false;
            panStopEnable();    // To enable the StopPaymentIssue panel...
            Date curDt = (Date) currDt.clone();
            observable.setLblChqStopDtVal(DateUtil.getStringDate(curDt));
            pan=STOP;
        }else if(panLooseLeafIssue.isShowing()==true){
            panLeafEnable();   // To enable the LooseLeafIssue panel...
            pan=LEAF;
        }
        else if(panEcsStopPaymentIssue.isShowing()==true){
            panEcsStopEnable();   // To enable the EcsStopPaymentIssue panel...
            Date curDt = (Date) currDt.clone();
            observable.setEcsDt(DateUtil.getStringDate(curDt));
            pan=ECS;
            txtEcsStopReasonForRevoke.setVisible(false);
            lblEcsReasonForRevoke.setVisible(false);
        }
        resetUI(pan);      // to Reset all the Fields and Status in UI...
//        if(panStopPaymentIssue.isShowing()==true){
//            Date curDt = currDt.clone();
////            lblChqStopDtVal.setText(DateUtil.getStringDate(curDt));
//            observable.setLblChqRevokeDtVal(DateUtil.getStringDate(curDt));
//        }
        setButtonEnableDisable();     // Enables/Disables the necessary buttons and menu items...
        btnSave.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        observable.setStatus();      // To set the Value of lblStatus...
        
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }//GEN-LAST:event_btnNewActionPerformed
    // to Reset all the Fields and Status in UI...
    private void resetUI(int value){
        observable.resetTable();     // Reset the table in UI...
        observable.resetLable();     // Reset the Editable Lables in the UI to null...
        rdoLeaf_BulkActionPerformed(null);
        if(value == CHEQUE){
            System.out.println("Reset Cheque");
            observable.setCboProdType("");
            observable.setCboProdId("");
            observable.resetChequeIssueForm();
            
            transDetails.setTransDetails(null,null,null);
            
        }else if(value == STOP){
            System.out.println("Reset Stop");
            observable.setCboProductType("");
            observable.setCboProductId("");
            observable.resetStopPaymentForm();
            
        }else if(value == LEAF){
            observable.setCboProduct_Type("");
            observable.setCboProductID("");
            observable.resetLooseLeafForm();
            
        }
        else if(value == ECS){
            observable.resetEcsStopPaymentForm();
            
        }else if(value == 4){
            observable.setCboProdType("");
            observable.setCboProdId("");
            observable.resetChequeIssueForm();
            
            observable.setCboProductType("");
            observable.setCboProductID("");
            observable.resetStopPaymentForm();
            
            observable.setCboProduct_Type("");
            observable.setCboProductID("");
            observable.resetLooseLeafForm();
        }
        observable.ttNotifyObservers();
        cboProdType.setSelectedItem(observable.getCboProdType());
        cboProductType.setSelectedItem(observable.getCboProductType());
        cboProduct_Type.setSelectedItem(observable.getCboProduct_Type());
        cboEcsProductType.setSelectedItem(observable.getCboEcsProdType());
    }
    // To enable the ChequeBookIssue panel...
    private void panChequeEnable(){
        ClientUtil.enableDisable(panChequeBookIssue, true);// Enables all when the New button is pressed...
        btnAccountNo.setEnabled(true);
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW)
            txtAccountNo.setEnabled(true);
        else
            txtAccountNo.setEnabled(false);
        txtNamesOfAccount.setEnabled(false);
        txtNoOfChequeBooks.setEditable(true);
        txtNoOfChequeBooks.setEnabled(true);
        
        //        txtNoOfLeaves.setEnabled(false);
        txtChargesCollected.setEnabled(false);
        
        //ClientUtil.enableDisable(panSeriesNo, false);
    }
    
    // To enable the StopPaymentIssue panel...
    private void panStopEnable(){
        ClientUtil.enableDisable(panStopPaymentIssue, true);// Enables all when the New button is pressed...
        btnAccNo.setEnabled(true);
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW){
            txtAccNo.setEnabled(true);
        }
        else{
            txtAccNo.setEnabled(false);
        txtStopPaymentCharges.setEnabled(false);
        }
    }
    private void panEcsStopEnable(){
        ClientUtil.enableDisable(panEcsStopPaymentIssue, true);// Enables all when the New button is pressed...
        btnEcsAccNo.setEnabled(true);
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW)
            txtEcsAccNo.setEnabled(true);
        else{
            txtEcsAccNo.setEnabled(false);
        txtEcsStopPaymentCharges.setEnabled(false);
        }
    }
    private void callServiceTaxCalculation(String calAmt){
         calMap= new HashMap();
                   calMap.put(CommonConstants.BRANCH_ID,TrueTransactMain.BRANCH_ID);
                   calMap.put("CAL_AMT",calAmt);
                   calMap.put("ST_CAL","ST_CAL");
                   if(panChequeBookIssue.isShowing()==true)
                   calMap.put("CHARGE_TYPE","CHEQUE_BOOK_ISSUE_CHG");
                   else 
                   calMap.put("CHARGE_TYPE","CHQ_STOP_CHARGES");    
                   HashMap viewMap= new HashMap();
                   viewMap.put("SERVICE_TAX",calMap);
                   
                List clLst=ClientUtil.executeQuery("",viewMap);
                System.out.println("clLst----->"+clLst);
                if(clLst!=null && clLst.size()>0){
                    calMap=(HashMap)clLst.get(0);
//                    ClientUtil.displayAlert("Service Tax "+calMap.get("SERVICE_TAX")+"\n"+
//                    "Cess1 Tax :"+calMap.get("CESS1_TAX")+"\n"+
//                    "Cess2 Tax "+calMap.get("CESS1_TAX"));
//                    System.out.println("calMap----->"+calMap);
                   ClientUtil.showAlertWindow("Service Tax :   "+calMap.get("SERVICE_TAX")+"\n"+
                    "Cess1 Tax :    "+calMap.get("CESS1_TAX")+"\n"+
                    "Cess2 Tax :    "+calMap.get("CESS2_TAX"));
                    System.out.println("calMap----->"+calMap);
                }
    }
    // To enable the LooseLeafIssue panel...
    private void panLeafEnable(){
        ClientUtil.enableDisable(panLooseLeafIssue, true);// Enables all when the New button is pressed...
        btnAccountNumber.setEnabled(true);
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW)
            txtAccounNumber.setEnabled(true);
        else
            txtAccounNumber.setEnabled(false);
    }
    
    private void panEcsEnable(){
        ClientUtil.enableDisable(panEcsStopPaymentIssue, true);// Enables all when the New button is pressed...
        btnEcsAccNo.setEnabled(true);
        btnEcsPaymentRevoke.setEnabled(false);
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW)
            txtEcsAccNo.setEnabled(true);
        else
            txtEcsAccNo.setEnabled(false);
    }
    
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void mitPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitPrintActionPerformed
        // Add your handling code here:
        btnPrintActionPerformed(evt);
    }//GEN-LAST:event_mitPrintActionPerformed
    
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

    private void txtStartingChequeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtStartingChequeFocusLost
        if (txtStartingCheque.getText() != null && txtStartingCheque.getText().length() > 0) {
            HashMap map = new HashMap();
            map.put("INSTRUMENT_PREFIX", txtStartingCheque.getText());
            map.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            List lstData = (List) ClientUtil.executeQuery("getNextChequeNO", map);
            if (lstData != null && lstData.size() > 0) {
                HashMap whrMap = (HashMap) lstData.get(0);
                if (whrMap != null && whrMap.size() > 0) {
                    txtStartingChequeNo.setText(CommonUtil.convertObjToStr(whrMap.get("CHQ_NO")));
                    txtStartingChequeNoFocusLost(null);
                }

            }
        }
    }//GEN-LAST:event_txtStartingChequeFocusLost

    /**
     * Getter for property stopStatus.
     * @return Value of property stopStatus.
     */
    public java.lang.String getStopStatus() {
        return stopStatus;
    }    
    
    /**
     * Setter for property stopStatus.
     * @param stopStatus New value of property stopStatus.
     */
    public void setStopStatus(java.lang.String stopStatus) {
        this.stopStatus = stopStatus;
    }
    public void setStopPaymentCharges(){
        String prodId="";
        if(panStopPaymentIssue.isShowing()==true)
            prodId= ((ComboBoxModel)cboProductId.getModel()).getKeyForSelected().toString();
        else if(panEcsStopPaymentIssue.isShowing()==true)
            prodId= ((ComboBoxModel)cboEcsProductId.getModel()).getKeyForSelected().toString();
        HashMap prodIdMap=new HashMap();
        prodIdMap.put("PROD_ID",prodId);
        List lst=ClientUtil.executeQuery("getStopPaymentChrgs",prodIdMap);
        if(lst!=null && lst.size()>0){
        prodIdMap= null;
        prodIdMap=(HashMap)lst.get(0);
        if(panStopPaymentIssue.isShowing()==true)
            txtStopPaymentCharges.setText(CommonUtil.convertObjToStr(prodIdMap.get("CHARGE")));
        else if(panEcsStopPaymentIssue.isShowing()==true)
            txtEcsStopPaymentCharges.setText(CommonUtil.convertObjToStr(prodIdMap.get("CHARGE")));
     }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAccNo;
    private com.see.truetransact.uicomponent.CButton btnAccountNo;
    private com.see.truetransact.uicomponent.CButton btnAccountNumber;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEcsAccNo;
    private com.see.truetransact.uicomponent.CButton btnEcsPaymentRevoke;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPaymentRevoke;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboEcsProductId;
    private com.see.truetransact.uicomponent.CComboBox cboEcsProductType;
    private com.see.truetransact.uicomponent.CComboBox cboEcsReasonStopPayment;
    private com.see.truetransact.uicomponent.CComboBox cboMethodOfDelivery;
    private com.see.truetransact.uicomponent.CComboBox cboNoOfLeaves;
    private com.see.truetransact.uicomponent.CComboBox cboProdId;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CComboBox cboProductID;
    private com.see.truetransact.uicomponent.CComboBox cboProductId;
    private com.see.truetransact.uicomponent.CComboBox cboProductType;
    private com.see.truetransact.uicomponent.CComboBox cboProduct_Type;
    private com.see.truetransact.uicomponent.CComboBox cboReasonStopPayment;
    private com.see.truetransact.uicomponent.CComboBox cboUsage;
    private com.see.truetransact.uicomponent.CLabel lblAccHd;
    private com.see.truetransact.uicomponent.CLabel lblAccHdDesc;
    private com.see.truetransact.uicomponent.CLabel lblAccHdId;
    private com.see.truetransact.uicomponent.CLabel lblAccHead;
    private com.see.truetransact.uicomponent.CLabel lblAccHeadDesc;
    private com.see.truetransact.uicomponent.CLabel lblAccNo;
    private com.see.truetransact.uicomponent.CLabel lblAccounNumber;
    private com.see.truetransact.uicomponent.CLabel lblAccountHead;
    private com.see.truetransact.uicomponent.CLabel lblAccountHeadDesc;
    private com.see.truetransact.uicomponent.CLabel lblAccountHeadId;
    private com.see.truetransact.uicomponent.CLabel lblAccountNo;
    private com.see.truetransact.uicomponent.CLabel lblChargesCollected;
    private com.see.truetransact.uicomponent.CLabel lblChequeAmt;
    private com.see.truetransact.uicomponent.CLabel lblChequeDate;
    private com.see.truetransact.uicomponent.CLabel lblChqRevokeDt;
    private com.see.truetransact.uicomponent.CLabel lblChqRevokeDtVal;
    private com.see.truetransact.uicomponent.CLabel lblChqStopDt;
    private com.see.truetransact.uicomponent.CLabel lblChqStopDtVal;
    private com.see.truetransact.uicomponent.CLabel lblChqStopId;
    private com.see.truetransact.uicomponent.CLabel lblCustId;
    private com.see.truetransact.uicomponent.CLabel lblCustIdDesc;
    private com.see.truetransact.uicomponent.CLabel lblCustIdDesc1;
    private com.see.truetransact.uicomponent.CLabel lblCustNameValue;
    private com.see.truetransact.uicomponent.CLabel lblCustNameValue1;
    private com.see.truetransact.uicomponent.CLabel lblCustomNameValue;
    private com.see.truetransact.uicomponent.CLabel lblCustomerID;
    private com.see.truetransact.uicomponent.CLabel lblCustomerIDValue;
    private com.see.truetransact.uicomponent.CLabel lblCustomerId;
    private com.see.truetransact.uicomponent.CLabel lblCustomerIdDesc;
    private com.see.truetransact.uicomponent.CLabel lblCustomerName;
    private com.see.truetransact.uicomponent.CLabel lblCustomerNameValue;
    private com.see.truetransact.uicomponent.CLabel lblEcsAccNo;
    private com.see.truetransact.uicomponent.CLabel lblEcsAmt;
    private com.see.truetransact.uicomponent.CLabel lblEcsCustId;
    private com.see.truetransact.uicomponent.CLabel lblEcsDate;
    private com.see.truetransact.uicomponent.CLabel lblEcsId;
    private com.see.truetransact.uicomponent.CLabel lblEcsNo;
    private com.see.truetransact.uicomponent.CLabel lblEcsPayeeName;
    private com.see.truetransact.uicomponent.CLabel lblEcsProductId;
    private com.see.truetransact.uicomponent.CLabel lblEcsProductType;
    private com.see.truetransact.uicomponent.CLabel lblEcsReasonForRevoke;
    private com.see.truetransact.uicomponent.CLabel lblEcsReasonStopPayment;
    private com.see.truetransact.uicomponent.CLabel lblEcsRevokeDt;
    private com.see.truetransact.uicomponent.CLabel lblEcsRevokeDtVal;
    private com.see.truetransact.uicomponent.CLabel lblEcsStopDt;
    private com.see.truetransact.uicomponent.CLabel lblEcsStopDtVal;
    private com.see.truetransact.uicomponent.CLabel lblEcsStopPaymentCharges;
    private com.see.truetransact.uicomponent.CLabel lblEndChequeNo;
    private com.see.truetransact.uicomponent.CLabel lblEndingCheque;
    private com.see.truetransact.uicomponent.CLabel lblLeaf;
    private com.see.truetransact.uicomponent.CLabel lblLeafNo;
    private com.see.truetransact.uicomponent.CLabel lblMethodOfDelivery;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNamesOfAccount;
    private com.see.truetransact.uicomponent.CLabel lblNoOfChequeBooks;
    private com.see.truetransact.uicomponent.CLabel lblNoOfLeaves;
    private com.see.truetransact.uicomponent.CLabel lblPayeeName;
    private com.see.truetransact.uicomponent.CLabel lblProdId;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    private com.see.truetransact.uicomponent.CLabel lblProductID;
    private com.see.truetransact.uicomponent.CLabel lblProductId;
    private com.see.truetransact.uicomponent.CLabel lblProductType;
    private com.see.truetransact.uicomponent.CLabel lblProduct_Type;
    private com.see.truetransact.uicomponent.CLabel lblReasonForRevoke;
    private com.see.truetransact.uicomponent.CLabel lblReasonStopPayment;
    private com.see.truetransact.uicomponent.CLabel lblRemark;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblSeriesNoFrom;
    private com.see.truetransact.uicomponent.CLabel lblSeriesNoTo;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace17;
    private com.see.truetransact.uicomponent.CLabel lblSpace18;
    private com.see.truetransact.uicomponent.CLabel lblSpace19;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace20;
    private com.see.truetransact.uicomponent.CLabel lblSpace21;
    private com.see.truetransact.uicomponent.CLabel lblSpace22;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpaces;
    private com.see.truetransact.uicomponent.CLabel lblStartChequeNo;
    private com.see.truetransact.uicomponent.CLabel lblStartingCheque;
    private com.see.truetransact.uicomponent.CLabel lblStatus1;
    private com.see.truetransact.uicomponent.CLabel lblStopPaymentCharges;
    private com.see.truetransact.uicomponent.CLabel lblUsage;
    private com.see.truetransact.uicomponent.CMenuBar mbrLoanProduct;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAccNo;
    private com.see.truetransact.uicomponent.CPanel panAccNo1;
    private com.see.truetransact.uicomponent.CPanel panAccounNumber;
    private com.see.truetransact.uicomponent.CPanel panAccountNo;
    private com.see.truetransact.uicomponent.CPanel panCheque;
    private com.see.truetransact.uicomponent.CPanel panCheque1;
    private com.see.truetransact.uicomponent.CPanel panChequeBook;
    private com.see.truetransact.uicomponent.CPanel panChequeBookIssue;
    private com.see.truetransact.uicomponent.CPanel panChequeDetails;
    private com.see.truetransact.uicomponent.CPanel panCustomer;
    private com.see.truetransact.uicomponent.CPanel panCustomer1;
    private com.see.truetransact.uicomponent.CPanel panData;
    private com.see.truetransact.uicomponent.CPanel panEcsStopPaymentIssue;
    private com.see.truetransact.uicomponent.CPanel panEndChequeNo;
    private com.see.truetransact.uicomponent.CPanel panEndEcsNo1;
    private com.see.truetransact.uicomponent.CPanel panEndingCheque;
    private com.see.truetransact.uicomponent.CPanel panLeaf;
    private com.see.truetransact.uicomponent.CPanel panLeafNo;
    private com.see.truetransact.uicomponent.CPanel panLooseAccountHead;
    private com.see.truetransact.uicomponent.CPanel panLooseLeafIssue;
    private com.see.truetransact.uicomponent.CPanel panPaymentIssues;
    private com.see.truetransact.uicomponent.CPanel panPaymentIssues1;
    private com.see.truetransact.uicomponent.CPanel panPaymentRevoke;
    private com.see.truetransact.uicomponent.CPanel panPaymentRevoke1;
    private com.see.truetransact.uicomponent.CPanel panProdLeaf;
    private com.see.truetransact.uicomponent.CPanel panProduct;
    private com.see.truetransact.uicomponent.CPanel panProductData;
    private com.see.truetransact.uicomponent.CPanel panSeriesNo;
    private com.see.truetransact.uicomponent.CPanel panStartChequeNo;
    private com.see.truetransact.uicomponent.CPanel panStartChequeNo1;
    private com.see.truetransact.uicomponent.CPanel panStartingCheque;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panStopPaymentIssue;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CPanel panTransDetails;
    private com.see.truetransact.uicomponent.CButtonGroup rdoLeaf;
    private com.see.truetransact.uicomponent.CRadioButton rdoLeaf_Bulk;
    private com.see.truetransact.uicomponent.CRadioButton rdoLeaf_single;
    private javax.swing.JSeparator sptCancel;
    private com.see.truetransact.uicomponent.CSeparator sptChequeIssue;
    private com.see.truetransact.uicomponent.CSeparator sptPaymentIssues;
    private com.see.truetransact.uicomponent.CSeparator sptPaymentIssues1;
    private javax.swing.JSeparator sptPrint;
    private javax.swing.JSeparator sptProcess;
    private com.see.truetransact.uicomponent.CSeparator sptProductData;
    private com.see.truetransact.uicomponent.CScrollPane srpChequeDetails;
    private com.see.truetransact.uicomponent.CTabbedPane tabChequeBook;
    private com.see.truetransact.uicomponent.CTable tblChequeDetails;
    private javax.swing.JToolBar tbrLoantProduct;
    private com.see.truetransact.uicomponent.CDateField tdtChequeDate;
    private com.see.truetransact.uicomponent.CDateField tdtEcsDate;
    private com.see.truetransact.uicomponent.CTextField txtAccNo;
    private com.see.truetransact.uicomponent.CTextField txtAccounNumber;
    private com.see.truetransact.uicomponent.CTextField txtAccountNo;
    private com.see.truetransact.uicomponent.CTextField txtChargesCollected;
    private com.see.truetransact.uicomponent.CTextField txtChequeAmt;
    private com.see.truetransact.uicomponent.CTextField txtEcsAccNo;
    private com.see.truetransact.uicomponent.CTextField txtEcsAmt;
    private com.see.truetransact.uicomponent.CTextField txtEcsPayeeName;
    private com.see.truetransact.uicomponent.CTextField txtEcsStopPaymentCharges;
    private com.see.truetransact.uicomponent.CTextField txtEcsStopReasonForRevoke;
    private com.see.truetransact.uicomponent.CTextField txtEndCheque;
    private com.see.truetransact.uicomponent.CTextField txtEndEcs1;
    private com.see.truetransact.uicomponent.CTextField txtEndEcsNo2;
    private com.see.truetransact.uicomponent.CTextField txtEndingCheque;
    private com.see.truetransact.uicomponent.CTextField txtEndingChequeNo;
    private com.see.truetransact.uicomponent.CTextField txtEndingchequeNo;
    private com.see.truetransact.uicomponent.CTextField txtLeafNo1;
    private com.see.truetransact.uicomponent.CTextField txtLeafNo2;
    private com.see.truetransact.uicomponent.CTextField txtNamesOfAccount;
    private com.see.truetransact.uicomponent.CTextField txtNoOfChequeBooks;
    private com.see.truetransact.uicomponent.CTextField txtPayeeName;
    private com.see.truetransact.uicomponent.CTextField txtRemark;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    private com.see.truetransact.uicomponent.CTextField txtSeriesFrom;
    private com.see.truetransact.uicomponent.CTextField txtSeriesNoTo;
    private com.see.truetransact.uicomponent.CTextField txtStartCheque;
    private com.see.truetransact.uicomponent.CTextField txtStartchequeNo;
    private com.see.truetransact.uicomponent.CTextField txtStartingCheque;
    private com.see.truetransact.uicomponent.CTextField txtStartingChequeNo;
    private com.see.truetransact.uicomponent.CTextField txtStopPaymentCharges;
    private com.see.truetransact.uicomponent.CTextField txtStopReasonForRevoke;
    // End of variables declaration//GEN-END:variables
    
}
