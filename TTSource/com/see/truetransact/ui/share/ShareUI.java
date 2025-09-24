/*
 * ShareUI.java
 *
 * Created on December 23, 2004, 2:27 PM
 */

package com.see.truetransact.ui.share;


import com.see.truetransact.ui.common.resolutionstatus.ResolutionStatusUI;
import com.see.truetransact.ui.deposit.CommonMethods;
import com.see.truetransact.ui.deposit.CommonRB;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uivalidation.DefaultValidation;   // Added by Rajesh
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.COptionPane;
import java.util.ArrayList;
import java.util.List;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import java.util.Observable;
import java.util.HashMap;
import com.see.truetransact.ui.common.nominee.*;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.termloan.customerDetailsScreen.CustomerDetailsScreenUI;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;

import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.ui.common.viewall.NewAuthorizeListUI;
import com.see.truetransact.ui.customer.IndividualCustUI;
import com.see.truetransact.ui.share.DrfTransactionUI;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListCreditUI;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListDebitUI;        
import com.see.truetransact.ui.termloan.loanapplicationregister.LoanApplicationUI;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.Date;
import javax.swing.JDialog;
import javax.swing.SwingWorker;
/**
 *
 * @author  K.R.Jayakrishnan
 */
public class ShareUI extends com.see.truetransact.uicomponent.CInternalFrame implements com.see.truetransact.uimandatory.UIMandatoryField, java.util.Observer {
    private ShareOB observable;
    private ShareRB resourceBundle ;
    HashMap mandatoryMap;
    String viewType = ClientConstants.VIEW_TYPE_CANCEL;
    boolean isFilled = false;
    CommonRB objCommRB = new CommonRB();
    boolean isTblSharAcctClicked = false;
    boolean shareAcctDetNewPressed = false;
    private int ok = 0;
    private int yes = 0;
    private int no = 1;
    String prodId=null;
    private int cancel = 2;
    private String head_office = "";
    private final String AUTHORIZE = "AUTHORIZE";
    private final String SEND_TO_RESOLUTION = "SENDTORESOLUTION";
    private final String SHARE_ACCT_NO = "SHARE ACCOUNT NO";
    private final String SHARE_APPL_NO = "SHARE APPLICATION NO";
    private final String SHARE_DET_NO ="SHARE DETAIL NO";
    private final String SHARE_TYPE ="SHARE TYPE";  // Added by Rajesh
    private final String CUSTOMER_ID = "CUSTOMER ID";
    private final String CUST_ID = "CUST_ID";
    private final String JOINT_ACCOUNT =   "JOINT ACCOUNT";
    private final String CBO_JOINT_ACCOUNT =    "Joint Account";
    final int DELETE = 1;
    final int DRFTRANSACTION = 0;
    final String SCREEN = "SA";
    int panEditDelete = -1;
    private String shareAccountDetailNo = "";
    private String prodType ="";
    private int shareAcctDetNo = -1;
    private String shareDetNo = "";
    boolean shareAcctDetFilled = false;
    private TransactionUI transactionUI = new TransactionUI();
    private  double toShareAmt=0.0;
    private boolean isCustomerEditmode=false;
    private TransactionTO transactionTo;
    ArrayList DeleteTranslist=null;
    int minShares=0;
    int maxShare=0;
    boolean save=false;
    private double customersShare = 0.0;
    private double govtsShare = 0.0;
    private boolean subsidyFlag = false;
    private boolean subsidyGiven = false;
    //     added by Nikhil for DRF Applicable
    private String drfApplicable = "";
    private boolean drfRecieptFlag = false;
    private boolean drfEnable = true;
    private List authLst = null; 
    /**
     * Declare a new instance of the NomineeUI
     */
    NomineeUI nomineeUi = new NomineeUI(SCREEN, true);
    NomineeUI drfNomineeUi = new NomineeUI(SCREEN, true);//Added By Revathi.L
    IndividualCustUI individualCustUI;
    DrfTransactionUI drfTransactionUI=null;
    boolean fromAuthorizeUI = false;
    boolean fromCashierAuthorizeUI = false;
    boolean fromManagerAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
    AuthorizeListDebitUI ManagerauthorizeListUI=null;
    AuthorizeListCreditUI CashierauthorizeListUI=null;
    private ShareMRB obj =new ShareMRB();
    private int rejectFlag=0;
    private String shareAccNo = "";
    private String shareNoAvailable = "";
    private String BATCH_ID = "";
    private String TRANS_ID = "";
    HashMap tempProxyReturnMap = new HashMap();
    int flg=0;
    private String shareFeeTax="",memFeeTax="",applFeeTax="";
    private String shareFeeTaxSettingId="",memFeeTaxSettingId="",applFeeTaxSettingId="";
    private ServiceTaxCalculation objServiceTax;
    HashMap serviceTax_Map=new HashMap();
    private final String NEAREST="NEAREST_VALUE";
    private final String HIGHER="HIGHER_VALUE";
    private final String LOWER="LOWER_VALUE";
    private final String NO_ROUND_OFF="NO_ROUND_OFF";
    DecimalFormat df =new DecimalFormat("##.00");
    private String authorizeListScreen = "";
    private Date currDt = null;
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;
    boolean drfExist = false;//Added By Revathi.L
     boolean result = false;  //added by Rishad 
    /** Creates new form ShareUI */
    public ShareUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initStartup();
        
        //Commented for the temperory purpose
        // If we are implementing Truetransact branchwise, Send to Resolution & Share Resolution option in HO are not possible
        // By Rajesh
        transactionUI.addToScreen(panTransaction);
        observable.setTransactionOB(transactionUI.getTransactionOB());
        transactionUI.setSourceScreen("SHARE_SCREEN");
        tabShare.resetVisits();
        txtNextAccNo.setEnabled(false);
        txtNextAccNo.setText("");
    }
    
    private void setObservable(){
        //        observable = ShareOB.getInstance();
        try{
            observable = new ShareOB();
            observable.addObserver(this);
            observable.setTransactionOB(transactionUI.getTransactionOB());
        } catch(Exception e){
            
        }
    }
    
    private void initStartup() {
        setFieldNames();
        internationalize();
        setObservable();
        /**
         * To add the Nominee Tab
         */
        tabShare.add(nomineeUi,"Nominee", 1);
        nomineeUi.disableNewButton(false);
        initTableData();
        initComponentData();
        setMaximumLength();
        observable.resetForm();
        ClientUtil.enableDisable(this, false);
        setBtnForTabs();
        setButtonEnableDisable();
        setMandatoryHashMap();
        observable.setStatus();
        setHelpMessage();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panAccountOpening);
         new MandatoryCheck().putMandatoryMarks(getClass().getName(),panShareAcctNo);
        
        //  panShareDetnew MandatoryCheck().putMandatoryMarks(getClass().getName(),panInsideGeneralRemittance);
        //--Hide the resolution no from Other Details Pan
        txtResolutionNo.setVisible(false);
        btnIdPrint.setEnabled(false);
        lblResolutionNo.setVisible(false);
        btnDivAcNoFileOpen.setEnabled(true);
        //        txtMemFee.setEditable(false);
        //        txtMemFee.setEnabled(false);
        txtShareValue.setEditable(false);
        txtShareValue.setEnabled(false);
        txtShareApplFee.setEditable(false);
        txtShareApplFee.setEnabled(false);
        txtShareMemFee.setEditable(false);
        txtShareMemFee.setEnabled(false);
        txtTotShareFee.setEditable(false);
        txtTotShareFee.setEnabled(false);
        txtShareTotAmount.setEditable(false);
        txtShareTotAmount.setEnabled(false);
        // Enable disable for the temperory purpose
        // If we are implementing Truetransact branchwise, Send to Resolution & Share Resolution option in HO are not possible
        // By Rajesh
        btnResolution.setVisible(false);
        //        tabTransaction.remove(panTransaction);
        ClientUtil.enableDisable(panShareTrans,false);
        panShareTrans.setVisible(false);
        rdoShareAddition.setSelected(false);
        rdoSharewithDrawal.setSelected(false);
        panCustomerName.setVisible(false);
//        btnReject.setEnabled(true);
        String telk_sal=CommonConstants.SAL_REC_MODULE;
        if(telk_sal.equals("N")){
            panShareImbp.setVisible(false);
        }
        lblDRFAmt.setVisible(false);
        lblDrfAmtVal.setVisible(false);
        lblDrfAmtVal.setAllowAll(true);
        txtFromSl_No.setAllowNumber(true);
        txtToSL_No.setAllowNumber(true);
        btnDelete.setEnabled(true);
    }
    
    private void setBtnForTabs(){
        setBtnShareAcctDet(false);
        setBtnShareJoint(false);
    }
    
    /** Sets the ComboBoxModel for all the Combos */
    private void initComponentData() {
        cboAcctStatus.setModel(observable.getCbmAcctStatus());
        cboConstitution.setModel(observable.getCbmConstitution());
        cboShareType.setModel(observable.getCbmShareType());
        cboMemDivProdType.setModel(observable.getCbmDivProdType());
        cboMemDivPayMode.setModel(observable.getCbmDivPayMode());
//        added by Nikhil for DRF Applicable
        cboDrfProdId.setModel(observable.getCbmDrfProdId());
    }
    
    private void setHelpMessage(){
        
    }
    
    private void setMaximumLength(){
        txtShareAcctNo.setMaxLength(16);
        txtApplicationNo.setMaxLength(16);
        txtCustId.setMaxLength(16);
        txtResolutionNo.setMaxLength(32);
        txtResolutionNo1.setMaxLength(64);    // This line added by Rajesh
        txtResolutionNo1.setAllowAll(true);   // This line added by Rajesh
        txtDivAcNo.setAllowAll(true);
        txtApplFee.setMaxLength(16);
        txtApplFee.setValidation(new CurrencyValidation());
        txtMemFee.setMaxLength(16);
        txtMemFee.setValidation(new CurrencyValidation());
        txtShareFee.setMaxLength(16);
        txtShareFee.setValidation(new CurrencyValidation());
        txtShareAmt.setMaxLength(16);
        txtShareAmt.setValidation(new CurrencyValidation());
        //        txtShareDetShareNoFrom.setMaxLength(16);
        //        txtShareDetShareNoFrom.setValidation(new NumericValidation());
        txtNoShares.setValidation(new  NumericValidation());
        txtTotShareFee.setValidation(new CurrencyValidation(13,2));
        txtShareValue.setValidation(new CurrencyValidation());
        txtShareApplFee.setValidation(new CurrencyValidation(13, 2));
        txtShareMemFee.setValidation(new CurrencyValidation(13,2));
        txtShareTotAmount.setValidation(new CurrencyValidation());
        txtIDCardNo.setMaxLength(32);
        //        added by Nikhil for duplicate ID
        txtIDResolutionNo.setMaxLength(32);
        txtImbp.setMaxLength(32);
        txtImbp.setValidation(new CurrencyValidation());
        txtEmpRefNoNew.setAllowAll(true);
        txtEmpRefNoOld.setAllowAll(true);
        
        txtMobileNo.setValidation(new NumericValidation());
        //        txtShareDetShareNoTo.setMaxLength(16);
        //        txtShareDetShareNoTo.setValidation(new NumericValidation());
        //        txtShareDetNoOfShares.setValidation(new NumericValidation());
        //        txtShareDetShareNoTo.setMaxLength(8);
    }
    private void initTableData() {
        tblDrfTransaction.setModel(observable.getTblDrfTransaction());

    }
    private void setButtonEnableDisable() {
            btnNew.setEnabled(!btnNew.isEnabled());
            btnEdit.setEnabled(!btnEdit.isEnabled());
            // btnDelete.setEnabled(!btnDelete.isEnabled());
            mitNew.setEnabled(btnNew.isEnabled());
            mitEdit.setEnabled(btnEdit.isEnabled());
            mitDelete.setEnabled(btnDelete.isEnabled());
            btnSave.setEnabled(!btnNew.isEnabled());
            btnCancel.setEnabled(!btnNew.isEnabled());
            mitSave.setEnabled(btnSave.isEnabled());
            mitCancel.setEnabled(btnCancel.isEnabled());
            //        setDelBtnEnableDisable(false);
            setAuthBtnEnableDisable();
    }
    
    //    private void setDelBtnEnableDisable(boolean enableDisable){
    //        btnDelete.setEnabled(enableDisable );
    //        mitDelete.setEnabled(enableDisable);
    //    }
    private void setAuthBtnEnableDisable(){
        boolean enableDisable = !btnSave.isEnabled();
        btnAuthorize.setEnabled(enableDisable);
        btnException.setEnabled(enableDisable);
        btnReject.setEnabled(enableDisable);
        mitAuthorize.setEnabled(enableDisable);
        mitException.setEnabled(enableDisable);
        mitReject.setEnabled(enableDisable);
    }
    
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnCustomerIdFileOpen.setName("btnCustomerIdFileOpen");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        btnShareAcctDetDel.setName("btnShareAcctDetDel");
        btnShareAcctDetNew.setName("btnShareAcctDetNew");
        btnShareAcctDetSave.setName("btnShareAcctDetSave");
        cboAcctStatus.setName("cboAcctStatus");
        cboConstitution.setName("cboConstitution");
        cboShareType.setName("cboShareType");
        cboCommAddrType.setName("cboCommAddrType");
        chkNotEligibleStatus.setName("chkNotEligibleStatus");
        lblAcctStatus.setName("lblAcctStatus");
        lblConstitution.setName("lblConstitution");
        lblApplFee.setName("lblApplFee");
        lblArea.setName("lblArea");
        lblCity.setName("lblCity");
        lblConnGrpDet.setName("lblConnGrpDet");
        lblCountry.setName("lblCountry");
        lblCustId.setName("lblCustId");
        lblCustomerName.setName("lblCustomerName");
        lblDateOfBirth.setName("lblDateOfBirth");
        lblDirRelDet.setName("lblDirRelDet");
        lblDtIssId.setName("lblDtIssId");
        lblDtNotEligiblePeriod.setName("lblDtNotEligiblePeriod");
        lblDtShareDetIssShareCert.setName("lblDtShareDetIssShareCert");
        lblMemFee.setName("lblMemFeeFixed");
        lblMsg.setName("lblMsg");
        lblNotEligibleStatus.setName("lblNotEligibleStatus");
        lblPin.setName("lblPin");
        lblPropertyDetails.setName("lblPropertyDetails");
        lblRelativeDetails.setName("lblRelativeDetails");
        lblResolutionNo.setName("lblResolutionNo");
        lblShareAcctNo.setName("lblShareAcctNo");
        lblIDCardNo.setName("lblIDCardNo");
        txtIDCardNo.setName("txtIDCardNo");
        lblShareAmt.setName("lblShareAmt");
        lblShareDetNoOfShares.setName("lblShareDetNoOfShares");
        //        lblShareDetShareAcctNo.setName("lblShareDetShareAcctNo");
        lblShareDetShareNoFrom.setName("lblShareDetShareNoFrom");
        lblShareDetShareNoTo.setName("lblShareDetShareNoTo");
        lblShareFee.setName("lblShareFee");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace5.setName("lblSpace5");
        lblState.setName("lblState");
        lblStatus.setName("lblStatus");
        lblStreet.setName("lblStreet");
        lblValArea.setName("lblValArea");
        lblValCity.setName("lblValCity");
        lblValCountry.setName("lblValCountry");
        lblValCustomerName.setName("lblValCustomerName");
        lblValDateOfBirth.setName("lblValDateOfBirth");
        lblValPin.setName("lblValPin");
        lblValState.setName("lblValState");
        lblValStreet.setName("lblValStreet");
        lblWelFund.setName("lblWelFund");
        mbrMain.setName("mbrMain");
        panAccountOpening.setName("panAccountOpening");
        panBtnShareAcctDet.setName("panBtnShareAcctDet");
        panCustId.setName("panCustId");
        panCustomerName.setName("panCustomerName");
        panDireRelDet.setName("panDireRelDet");
        panShare.setName("panShare");
        panShareAcctDet.setName("panShareAcctDet");
        panShareAcctDetails.setName("panShareAcctDetails");
        panShareAcctInfo.setName("panShareAcctInfo");
        panShareDet.setName("panShareDet");
        panStatus.setName("panStatus");
        panTblShareAcctDet.setName("panTblShareAcctDet");
        panLoanDetails.setName("panLoanDetails");
        srpLoanAcctHolder.setName("srpLoanAcctHolder");
        srpTblShareAccntDet.setName("srpTblShareAccntDet");
        tabShare.setName("tabShare");
        tblShareAcctDet.setName("tblShareAcctDet");
        tdtIssId.setName("tdtIssId");
        tdtNotEligiblePeriod.setName("tdtNotEligiblePeriod");
        tdtShareDetIssShareCert.setName("tdtShareDetIssShareCert");
        txtApplFee.setName("txtApplFee");
        txtConnGrpDet.setName("txtConnGrpDet");
        txtCustId.setName("txtCustId");
        txtDirRelDet.setName("txtDirRelDet");
        txtMemFee.setName("txtMemFee");
        txtPropertyDetails.setName("txtPropertyDetails");
        txtRelativeDetails.setName("txtRelativeDetails");
        txtResolutionNo.setName("txtResolutionNo");
        txtShareAcctNo.setName("txtShareAcctNo");
        txtApplicationNo.setName("txtApplicationNo");
        txtShareAmt.setName("txtShareAmt");
        //        txtShareDetNoOfShares.setName("txtShareDetNoOfShares");
        //        txtShareDetShareAcctNo.setName("txtShareDetShareAcctNo");
        //        txtShareDetShareNoFrom.setName("txtShareDetShareNoFrom");
        //        txtShareDetShareNoTo.setName("txtShareDetShareNoTo");
        txtShareFee.setName("txtShareFee");
        txtWelFund.setName("txtWelFund");
        txtRemarks.setName("txtRemarks");
        txtResolutionNo1.setName("txtResolutionNo1");
        lblResolutionNo1.setName("lblResolutionNo1");
        lblMemDivAcNo.setName("lblMemDivAcNo");
        lblMemDivProdId.setName("lblMemDivProdId");
        lblMemDivProdType.setName("lblMemDivProdType");
        lblEmpRefNoNew.setName("lblEmpRefNoNew");
        lblEmpRefNoOld.setName("lblEmpRefNoOld");
        lblImbp.setName("lblImbp");
        txtEmpRefNoNew.setName("txtEmpRefNoNew");
        txtEmpRefNoOld.setName("txtEmpRefNoOld");
        txtImbp.setName("txtImbp");
    }
    
    /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        resourceBundle = new ShareRB();
        btnClose.setText(resourceBundle.getString("btnClose"));
        ((javax.swing.border.TitledBorder)panShareAcctDet.getBorder()).setTitle(resourceBundle.getString("panShareAcctDet"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblShareDetShareNoFrom.setText(resourceBundle.getString("lblShareDetShareNoFrom"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblPin.setText(resourceBundle.getString("lblPin"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblValDateOfBirth.setText(resourceBundle.getString("lblValDateOfBirth"));
        lblValStreet.setText(resourceBundle.getString("lblValStreet"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblValCity.setText(resourceBundle.getString("lblValCity"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        chkNotEligibleStatus.setText(resourceBundle.getString("chkNotEligibleStatus"));
        lblCustId.setText(resourceBundle.getString("lblCustId"));
        //        lblShareDetShareAcctNo.setText(resourceBundle.getString("lblShareDetShareAcctNo"));
        lblConstitution.setText(resourceBundle.getString("lblConstitution"));
        lblWelFund.setText(resourceBundle.getString("lblWelFund"));
        lblCountry.setText(resourceBundle.getString("lblCountry"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblValCustomerName.setText(resourceBundle.getString("lblValCustomerName"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblDtNotEligiblePeriod.setText(resourceBundle.getString("lblDtNotEligiblePeriod"));
        lblShareDetNoOfShares.setText(resourceBundle.getString("lblShareDetNoOfShares"));
        lblRelativeDetails.setText(resourceBundle.getString("lblRelativeDetails"));
        lblDateOfBirth.setText(resourceBundle.getString("lblDateOfBirth"));
        lblStreet.setText(resourceBundle.getString("lblStreet"));
        lblCity.setText(resourceBundle.getString("lblCity"));
        lblValArea.setText(resourceBundle.getString("lblValArea"));
        lblMemFee.setText(resourceBundle.getString("lblMemFeeFixed"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblValCountry.setText(resourceBundle.getString("lblValCountry"));
        lblApplFee.setText(resourceBundle.getString("lblApplFee"));
        btnShareAcctDetDel.setText(resourceBundle.getString("btnShareAcctDetDel"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblShareFee.setText(resourceBundle.getString("lblShareFee"));
        lblAcctStatus.setText(resourceBundle.getString("lblAcctStatus"));
        lblValPin.setText(resourceBundle.getString("lblValPin"));
        btnShareAcctDetNew.setText(resourceBundle.getString("btnShareAcctDetNew"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblShareAcctNo.setText(resourceBundle.getString("lblShareAcctNo"));
        lblIDCardNo.setText(resourceBundle.getString("lblIDCardNo"));
        lblDirRelDet.setText(resourceBundle.getString("lblDirRelDet"));
        btnCustomerIdFileOpen.setText(resourceBundle.getString("btnCustomerIdFileOpen"));
        lblValState.setText(resourceBundle.getString("lblValState"));
        lblPropertyDetails.setText(resourceBundle.getString("lblPropertyDetails"));
        lblArea.setText(resourceBundle.getString("lblArea"));
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        lblCustomerName.setText(resourceBundle.getString("lblCustomerName"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblConnGrpDet.setText(resourceBundle.getString("lblConnGrpDet"));
        lblResolutionNo.setText(resourceBundle.getString("lblResolutionNo"));
        lblDtIssId.setText(resourceBundle.getString("lblDtIssId"));
        lblShareAmt.setText(resourceBundle.getString("lblShareAmt"));
        btnShareAcctDetSave.setText(resourceBundle.getString("btnShareAcctDetSave"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblNotEligibleStatus.setText(resourceBundle.getString("lblNotEligibleStatus"));
        lblDtShareDetIssShareCert.setText(resourceBundle.getString("lblDtShareDetIssShareCert"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblState.setText(resourceBundle.getString("lblState"));
        lblShareDetShareNoTo.setText(resourceBundle.getString("lblShareDetShareNoTo"));
        lblMemDivAcNo.setText(resourceBundle.getString("lblMemDivAcNo"));
        lblMemDivProdId.setText(resourceBundle.getString("lblMemDivProdId"));
        lblMemDivProdType.setText(resourceBundle.getString("lblMemDivProdType"));
        lblEmpRefNoNew.setText(resourceBundle.getString("lblEmpRefNoNew"));
        lblImbp.setText(resourceBundle.getString("lblImbp"));
        lblEmpRefNoOld.setText(resourceBundle.getString("lblEmpRefNoOld"));
        //        resourceBundle = null;
    }
    
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        removeRadioButtons();
        txtShareAcctNo.setText(observable.getTxtShareAcctNo());
        txtApplicationNo.setText(observable.getTxtApplicationNo());
        txtResolutionNo.setText(observable.getTxtResolutionNo());
        txtPropertyDetails.setText(observable.getTxtPropertyDetails());
        txtRelativeDetails.setText(observable.getTxtRelativeDetails());
        txtConnGrpDet.setText(observable.getTxtConnGrpDet());
        txtCustId.setText(observable.getTxtCustId());
        txtApplFee.setText(observable.getTxtApplFee());
        txtMemFee.setText(observable.getTxtMemFee());
        txtDirRelDet.setText(observable.getTxtDirRelDet());
        txtWelFund.setText(observable.getTxtWelFund());
        tdtIssId.setDateValue(observable.getTdtIssId());
        tdtNotEligiblePeriod.setDateValue(observable.getTdtNotEligiblePeriod());
        chkNotEligibleStatus.setSelected(observable.getChkNotEligibleStatus());
        txtShareFee.setText(observable.getTxtShareFee());
        txtShareAmt.setText(observable.getTxtShareAmt());
        //        txtShareDetShareAcctNo.setText(observable.getTxtShareDetShareAcctNo());
        //        txtShareDetShareNoFrom.setText(observable.getTxtShareDetShareNoFrom());
        //        txtShareDetShareNoTo.setText(observable.getTxtShareDetShareNoTo());
        //        txtShareDetNoOfShares.setText(observable.getTxtShareDetNoOfShares());
        txtFromSl_No.setText(CommonUtil.convertObjToStr(observable.getTxtFromSL_No()));
        txtToSL_No.setText(CommonUtil.convertObjToStr(observable.getTxtToSL_No()));
        txtResolutionNo1.setText(observable.getTxtResolutionNo1());
        tdtShareDetIssShareCert.setDateValue(observable.getTdtShareDetIssShareCert());
        tblShareAcctDet.setModel(observable.getTblShareAcctDet());
        tblJointAcctHolder.setModel(observable.getTblJointAccnt());
        txtRemarks.setText(observable.getTxtRemarks());
        lblStatus.setText(observable.getLblStatus());
        cboAcctStatus.setSelectedItem(observable.getCboAcctStatus());
        cboConstitution.setSelectedItem(observable.getCboConstitution());
        cboShareType.setSelectedItem(observable.getCboShareType());
        cboCommAddrType.setSelectedItem(observable.getCboCommAddrType());
        tblLoanAcctHolder.setModel(observable.getTblShareLoanDet());
        //        cboMemDivProdType.setSelectedItem(observable.getCboDivProdType());
        //        cboMemDivProdID.setSelectedItem(observable.getCboDivProdId());
        cboMemDivProdID.setSelectedItem(observable.getCboDivProdId());
        txtDivAcNo.setText(observable.getTxtDivAcNo());
        //        cboMemDivPayMode.setSelectedItem(observable.getCboDivPayMode());
        
        
        txtShareValue.setText(observable.getTxtShareValue());
        txtTotShareFee.setText(observable.getTxtTotShareFee());
        txtShareApplFee.setText(observable.getTxtShareApplFee());
        txtShareMemFee.setText(observable.getTxtShareMemFee());
        txtShareTotAmount.setText(observable.getTxtShareTotAmount());
        txtNoShares.setText(observable.getTxtNoShares());
        lblDivAmtVal.setText(observable.getLblDivAmt());
        lblTotshareBalVal.setText(observable.getLblBalanceAmt());
        txtIDCardNo.setText(observable.getTxtIDCardNo());
        //        added by nikhil for Duplicate ID
        txtIDResolutionNo.setText(observable.getTxtIDResolutionNo());
        tdtIDIssuedDt.setDateValue(CommonUtil.convertObjToStr(observable.getTdtIDIssuedDt()));
        tdtIDResolutionDt.setDateValue(CommonUtil.convertObjToStr(observable.getTdtIDResolutionDt()));
        if(CommonUtil.convertObjToStr(observable.getChkDuplicateIDCardYN()).equals("Y")){
            chkDuplicateIDCardYN.setSelected(true);
        }else{
            chkDuplicateIDCardYN.setSelected(false);
        }
        //        added by Nikhil for DRF Applicable
        if(CommonUtil.convertObjToStr(observable.getChkDrfApplicableYN()).equals("Y") || observable.isDrfApplicableFlag()==true){
            chkDrfApplicableYN.setSelected(true);
            lblDrfProdId.setVisible(true);
            cboDrfProdId.setVisible(true);
        cboDrfProdId.setSelectedItem(observable.getCboDrfProdId());
            lblDRFAmt.setVisible(true);
            lblDrfAmtVal.setVisible(true);
        }else{
            chkDrfApplicableYN.setSelected(false);
            lblDrfProdId.setVisible(false);
            cboDrfProdId.setVisible(false);
            cboDrfProdId.setSelectedItem("");
            lblDRFAmt.setVisible(false);
            lblDrfAmtVal.setVisible(false);
        }
        
        
        txtImbp.setText(observable.getTxtImbp());
        txtEmpRefNoNew.setText(observable.getTxtEmpRefNoNew());
        txtEmpRefNoOld.setText(observable.getTxtEmpRefNoOld());
        lblServiceTaxval.setText(observable.getLblServiceTaxval());
        lblBalToBePaid.setText(CommonUtil.convertObjToStr("Principle Outstanding :  "+observable.balance));
        addRadioButtons();
        txtMobileNo.setText(observable.getTxtMobileNo());
        chkMobileBanking.setSelected(observable.getIsMobileBanking());
        tdtMobileSubscribedFrom.setDateValue(observable.getTdtMobileSubscribedFrom());
    }
    
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtShareAcctNo(txtShareAcctNo.getText());
        observable.setTxtApplicationNo(txtApplicationNo.getText());
        observable.setTxtResolutionNo(txtResolutionNo.getText());
        observable.setTxtFromSL_No(CommonUtil.convertObjToInt(txtFromSl_No.getText()));
        observable.setTxtToSL_No(CommonUtil.convertObjToInt(txtToSL_No.getText()));
        observable.setTxtPropertyDetails(txtPropertyDetails.getText());
        observable.setTxtRelativeDetails(txtRelativeDetails.getText());
        observable.setTxtConnGrpDet(txtConnGrpDet.getText());
        observable.setTxtCustId(txtCustId.getText());
        observable.setCboAcctStatus((String) cboAcctStatus.getSelectedItem());
        observable.setCboConstitution((String) cboConstitution.getSelectedItem());
        observable.setCboCommAddrType((String) cboCommAddrType.getSelectedItem());
        observable.setCboShareType((String) cboShareType.getSelectedItem());
        observable.setTxtApplFee(txtApplFee.getText());
        observable.setTxtMemFee(txtMemFee.getText());
        observable.setTxtDirRelDet(txtDirRelDet.getText());
        observable.setTxtWelFund(txtWelFund.getText());
        observable.setTdtIssId(tdtIssId.getDateValue());
        observable.setTdtNotEligiblePeriod(tdtNotEligiblePeriod.getDateValue());
        observable.setChkNotEligibleStatus(chkNotEligibleStatus.isSelected());
        observable.setTxtShareFee(txtShareFee.getText());
        observable.setTxtShareAmt(txtShareAmt.getText());
        observable.setTxtShareTotAmount(txtShareTotAmount.getText());
        //        observable.setTxtShareDetShareAcctNo(txtShareDetShareAcctNo.getText());
        observable.setTxtShareDetShareNoFrom("ADD");
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT && rdoSharewithDrawal.isSelected() == true) {
            observable.setTxtShareDetShareNoFrom("WITHDRAWAL");
        }
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT && rdoShareDifferentialAmount.isSelected() == true) {
           observable.setTxtShareDetShareNoFrom("DIFFERENTIAL");
           HashMap transMap = (HashMap) observable.getAllowedTransactionDetailsTO();
            TransactionTO transTo = new TransactionTO();
            if(transMap!=null && transMap.size()>0 && transMap.containsKey("1")){
                transTo = (TransactionTO)transMap.get("1");
                if(transTo!=null){
                    observable.setTxtShareTotAmount(CommonUtil.convertObjToStr(transTo.getTransAmt()));
                }
            }
        }
        //        observable.setTxtShareDetShareNoTo(txtShareDetShareNoTo.getText());
        //        observable.setTxtShareDetNoOfShares(txtShareDetNoOfShares.getText());
        observable.setTxtResolutionNo1(txtResolutionNo1.getText());
        observable.setTdtShareDetIssShareCert(tdtShareDetIssShareCert.getDateValue());
        observable.setTxtRemarks(txtRemarks.getText());
        observable.setCboDivProdType((String)cboMemDivProdType.getSelectedItem());
        observable.setCboDivProdId((String)cboMemDivProdID.getSelectedItem());
        //        observable.setCboDivProdType((String)cboMemDivProdType.getSelectedItem());
        observable.setTxtDivAcNo(txtDivAcNo.getText());
        observable.setCboDivPayMode((String)cboMemDivPayMode.getSelectedItem());
        observable.setTxtShareValue(txtShareValue.getText()) ;
        observable.setTxtTotShareFee(txtTotShareFee.getText());
        observable.setTxtShareApplFee(txtShareApplFee.getText());
        observable.setTxtShareMemFee(txtShareMemFee.getText());
        //observable.setTxtShareTotAmount(txtShareTotAmount.getText());
        observable.setTxtNoShares(txtNoShares.getText());
        //        observable.setLblStatus(observable.getLblStatus())
        observable.setTxtIDCardNo(txtIDCardNo.getText());
        //        added by nikhil for duplicate ID
        observable.setTxtIDResolutionNo(txtIDResolutionNo.getText());
        observable.setTdtIDResolutionDt(tdtIDResolutionDt.getDateValue());
        observable.setTdtIDIssuedDt(tdtIDIssuedDt.getDateValue());
        if(chkDuplicateIDCardYN.isSelected()){
            observable.setChkDuplicateIDCardYN("Y");
        }else{
            observable.setChkDuplicateIDCardYN("N");
        }
        //        added by Nikhil for DRF Applicable
        if(chkDrfApplicableYN.isSelected()){
            observable.setChkDrfApplicableYN("Y");
        }else{
            observable.setChkDrfApplicableYN("N");
        }
        String prodID = CommonUtil.convertObjToStr(((ComboBoxModel)cboDrfProdId.getModel()).getKeyForSelected());
        observable.setCboDrfProdId(prodID);
        
        observable.setTxtImbp(txtImbp.getText());
        observable.setTxtEmpRefNoNew(txtEmpRefNoNew.getText());
        observable.setTxtEmpRefNoOld(txtEmpRefNoOld.getText());
        observable.setServiceTax_Map(serviceTax_Map);
        observable.setLblServiceTaxval(lblServiceTaxval.getText());
        observable.setTxtMobileNo(txtMobileNo.getText());
        observable.setIsMobileBanking(chkMobileBanking.isSelected());
        observable.setTdtMobileSubscribedFrom(tdtMobileSubscribedFrom.getDateValue());
    }
    
    
/* Auto Generated Method - setMandatoryHashMap()
 
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtShareAcctNo", new Boolean(false));
        mandatoryMap.put("txtResolutionNo", new Boolean(false));
        mandatoryMap.put("txtPropertyDetails", new Boolean(false));
        mandatoryMap.put("txtRelativeDetails", new Boolean(false));
        mandatoryMap.put("txtConnGrpDet", new Boolean(false));
        mandatoryMap.put("txtCustId", new Boolean(true));
        mandatoryMap.put("cboAcctStatus", new Boolean(true));
        mandatoryMap.put("cboConstitution", new Boolean(true));
        mandatoryMap.put("cboShareType",new Boolean(true));
        mandatoryMap.put("cboCommAddrType",new Boolean(true));
        mandatoryMap.put("txtApplFee", new Boolean(true));
        mandatoryMap.put("txtMemFee", new Boolean(true));
        mandatoryMap.put("txtDirRelDet", new Boolean(false));
        mandatoryMap.put("txtWelFund", new Boolean(true));
        mandatoryMap.put("txtDupIdCard", new Boolean(true));
        mandatoryMap.put("tdtIssId", new Boolean(false));
        mandatoryMap.put("tdtNotEligiblePeriod", new Boolean(false));
        mandatoryMap.put("chkNotEligibleStatus", new Boolean(false));
        mandatoryMap.put("txtShareFee", new Boolean(true));
        mandatoryMap.put("txtShareAmt", new Boolean(true));
        mandatoryMap.put("txtShareDetShareAcctNo", new Boolean(true));
        mandatoryMap.put("txtShareDetShareNoFrom", new Boolean(false));
        mandatoryMap.put("txtShareDetShareNoTo", new Boolean(false));
        mandatoryMap.put("txtShareDetNoOfShares", new Boolean(true));
        mandatoryMap.put("tdtShareDetIssShareCert", new Boolean(false));
        mandatoryMap.put("txtRemarks", new Boolean(true));
        mandatoryMap.put("txtResolutionNo1", new Boolean(false));
        mandatoryMap.put("cboMemDivPayMode",new Boolean(true));
        mandatoryMap.put("txtIDCardNo",new Boolean(false));
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    
    
    public static void main(java.lang.String[] args) {
        javax.swing.JFrame frm = new javax.swing.JFrame();
        ShareUI share = new ShareUI();
        frm.getContentPane().add(share);
        share.show();
        frm.setSize(800, 650);
        frm.show();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rgbExistCust = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrShareAcct = new javax.swing.JToolBar();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace8 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace9 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace10 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace11 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace12 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnResolution = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnLiabilityReport = new com.see.truetransact.uicomponent.CButton();
        lblSpace13 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace14 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        jLabel3 = new com.see.truetransact.uicomponent.CLabel();
        txtNextAccNo = new com.see.truetransact.uicomponent.CTextField();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panShare = new com.see.truetransact.uicomponent.CPanel();
        tabShare = new com.see.truetransact.uicomponent.CTabbedPane();
        panAccountOpening = new com.see.truetransact.uicomponent.CPanel();
        panShareAcctInfo = new com.see.truetransact.uicomponent.CPanel();
        panShareDet = new com.see.truetransact.uicomponent.CPanel();
        panShareAcctNo = new javax.swing.JPanel();
        lblShareAcctNo = new com.see.truetransact.uicomponent.CLabel();
        lblCustId = new com.see.truetransact.uicomponent.CLabel();
        panCustId = new com.see.truetransact.uicomponent.CPanel();
        txtCustId = new com.see.truetransact.uicomponent.CTextField();
        btnCustomerIdFileOpen = new com.see.truetransact.uicomponent.CButton();
        lblAcctStatus = new com.see.truetransact.uicomponent.CLabel();
        cboAcctStatus = new com.see.truetransact.uicomponent.CComboBox();
        lblConstitution = new com.see.truetransact.uicomponent.CLabel();
        cboConstitution = new com.see.truetransact.uicomponent.CComboBox();
        lblValCustName = new com.see.truetransact.uicomponent.CLabel();
        lblShareType = new com.see.truetransact.uicomponent.CLabel();
        cboShareType = new com.see.truetransact.uicomponent.CComboBox();
        lblApplFee = new com.see.truetransact.uicomponent.CLabel();
        txtApplFee = new com.see.truetransact.uicomponent.CTextField();
        lblMemFee = new com.see.truetransact.uicomponent.CLabel();
        txtMemFee = new com.see.truetransact.uicomponent.CTextField();
        lblShareFee = new com.see.truetransact.uicomponent.CLabel();
        txtShareFee = new com.see.truetransact.uicomponent.CTextField();
        lblExistCust = new javax.swing.JLabel();
        panExistCust = new javax.swing.JPanel();
        rdoExistCustYes = new javax.swing.JRadioButton();
        rdoExistCustNo = new javax.swing.JRadioButton();
        panCustId3 = new com.see.truetransact.uicomponent.CPanel();
        btnShareActNo = new com.see.truetransact.uicomponent.CButton();
        txtShareAcctNo = new com.see.truetransact.uicomponent.CTextField();
        panDireRelDet = new com.see.truetransact.uicomponent.CPanel();
        lblDtIssId = new com.see.truetransact.uicomponent.CLabel();
        tdtIssId = new com.see.truetransact.uicomponent.CDateField();
        lblNotEligibleStatus = new com.see.truetransact.uicomponent.CLabel();
        lblDtNotEligiblePeriod = new com.see.truetransact.uicomponent.CLabel();
        tdtNotEligiblePeriod = new com.see.truetransact.uicomponent.CDateField();
        chkNotEligibleStatus = new com.see.truetransact.uicomponent.CCheckBox();
        txtShareAmt = new com.see.truetransact.uicomponent.CTextField();
        lblShareAmt = new com.see.truetransact.uicomponent.CLabel();
        lblCommAddrType = new com.see.truetransact.uicomponent.CLabel();
        cboCommAddrType = new com.see.truetransact.uicomponent.CComboBox();
        cboMemDivProdID = new com.see.truetransact.uicomponent.CComboBox();
        cboMemDivProdType = new com.see.truetransact.uicomponent.CComboBox();
        lblMemDivProdType = new com.see.truetransact.uicomponent.CLabel();
        lblMemDivProdId = new com.see.truetransact.uicomponent.CLabel();
        lblMemDivAcNo = new com.see.truetransact.uicomponent.CLabel();
        panCustId1 = new com.see.truetransact.uicomponent.CPanel();
        txtDivAcNo = new com.see.truetransact.uicomponent.CTextField();
        btnDivAcNoFileOpen = new com.see.truetransact.uicomponent.CButton();
        cboMemDivPayMode = new com.see.truetransact.uicomponent.CComboBox();
        lblMemDivPayMode = new com.see.truetransact.uicomponent.CLabel();
        panShareTrans = new com.see.truetransact.uicomponent.CPanel();
        lblTotNoOfSharesCount1 = new com.see.truetransact.uicomponent.CLabel();
        rdoShareAddition = new com.see.truetransact.uicomponent.CRadioButton();
        rdoSharewithDrawal = new com.see.truetransact.uicomponent.CRadioButton();
        lblTransType = new com.see.truetransact.uicomponent.CLabel();
        rdoShareDifferentialAmount = new com.see.truetransact.uicomponent.CRadioButton();
        panShareImbp = new javax.swing.JPanel();
        lblEmpRefNoNew = new com.see.truetransact.uicomponent.CLabel();
        lblEmpRefNoOld = new com.see.truetransact.uicomponent.CLabel();
        txtEmpRefNoOld = new com.see.truetransact.uicomponent.CTextField();
        panCustId2 = new com.see.truetransact.uicomponent.CPanel();
        lblImbp = new com.see.truetransact.uicomponent.CLabel();
        txtEmpRefNoNew = new com.see.truetransact.uicomponent.CTextField();
        txtImbp = new com.see.truetransact.uicomponent.CTextField();
        jPanel2 = new com.see.truetransact.uicomponent.CPanel();
        panCustomerSide = new com.see.truetransact.uicomponent.CPanel();
        panCustomerName = new com.see.truetransact.uicomponent.CPanel();
        lblValCustomerName = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerName = new com.see.truetransact.uicomponent.CLabel();
        lblDateOfBirth = new com.see.truetransact.uicomponent.CLabel();
        lblValDateOfBirth = new com.see.truetransact.uicomponent.CLabel();
        lblValStreet = new com.see.truetransact.uicomponent.CLabel();
        lblStreet = new com.see.truetransact.uicomponent.CLabel();
        lblArea = new com.see.truetransact.uicomponent.CLabel();
        lblValArea = new com.see.truetransact.uicomponent.CLabel();
        lblValCity = new com.see.truetransact.uicomponent.CLabel();
        lblCity = new com.see.truetransact.uicomponent.CLabel();
        lblCountry = new com.see.truetransact.uicomponent.CLabel();
        lblValCountry = new com.see.truetransact.uicomponent.CLabel();
        lblValPin = new com.see.truetransact.uicomponent.CLabel();
        lblPin = new com.see.truetransact.uicomponent.CLabel();
        lblState = new com.see.truetransact.uicomponent.CLabel();
        lblValState = new com.see.truetransact.uicomponent.CLabel();
        panJointAcctHolder = new com.see.truetransact.uicomponent.CPanel();
        srpJointAcctHolder = new com.see.truetransact.uicomponent.CScrollPane();
        tblJointAcctHolder = new com.see.truetransact.uicomponent.CTable();
        panJointAcctButton = new com.see.truetransact.uicomponent.CPanel();
        btnJointAcctNew = new com.see.truetransact.uicomponent.CButton();
        btnJointAcctDel = new com.see.truetransact.uicomponent.CButton();
        btnJointAcctToMain = new com.see.truetransact.uicomponent.CButton();
        panCudtomerIDDetails = new com.see.truetransact.uicomponent.CPanel();
        lblDuplicateIDCardYN = new com.see.truetransact.uicomponent.CLabel();
        chkDuplicateIDCardYN = new com.see.truetransact.uicomponent.CCheckBox();
        lblIDCardNo = new com.see.truetransact.uicomponent.CLabel();
        txtIDCardNo = new com.see.truetransact.uicomponent.CTextField();
        lblApplicationNo = new com.see.truetransact.uicomponent.CLabel();
        txtIDResolutionNo = new com.see.truetransact.uicomponent.CTextField();
        lblIDResolutionNo = new com.see.truetransact.uicomponent.CLabel();
        lblIDIssuedDt = new com.see.truetransact.uicomponent.CLabel();
        tdtIDResolutionDt = new com.see.truetransact.uicomponent.CDateField();
        lblIDResolutionDt = new com.see.truetransact.uicomponent.CLabel();
        tdtIDIssuedDt = new com.see.truetransact.uicomponent.CDateField();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        btnShareAppNo = new com.see.truetransact.uicomponent.CButton();
        txtApplicationNo = new com.see.truetransact.uicomponent.CTextField();
        panDrfApplicablle = new com.see.truetransact.uicomponent.CPanel();
        lblTotNoOfSharesCount2 = new com.see.truetransact.uicomponent.CLabel();
        lblTransType1 = new com.see.truetransact.uicomponent.CLabel();
        chkDrfApplicableYN = new com.see.truetransact.uicomponent.CCheckBox();
        lblDrfProdId = new com.see.truetransact.uicomponent.CLabel();
        cboDrfProdId = new com.see.truetransact.uicomponent.CComboBox();
        lblDRFAmt = new com.see.truetransact.uicomponent.CLabel();
        lblDrfAmtVal = new com.see.truetransact.uicomponent.CTextField();
        btnIdPrint = new com.see.truetransact.uicomponent.CButton();
        lblServiceTax = new com.see.truetransact.uicomponent.CLabel();
        lblServiceTaxval = new com.see.truetransact.uicomponent.CLabel();
        tabTransaction = new com.see.truetransact.uicomponent.CTabbedPane();
        panShareAcctDet = new com.see.truetransact.uicomponent.CPanel();
        panTblShareAcctDet = new com.see.truetransact.uicomponent.CPanel();
        srpTblShareAccntDet = new com.see.truetransact.uicomponent.CScrollPane();
        tblShareAcctDet = new com.see.truetransact.uicomponent.CTable();
        panShareAcctDetails = new com.see.truetransact.uicomponent.CPanel();
        lblNoShares = new com.see.truetransact.uicomponent.CLabel();
        txtNoShares = new com.see.truetransact.uicomponent.CTextField();
        txtShareMemFee = new com.see.truetransact.uicomponent.CTextField();
        lblShareDetShareNoFrom = new com.see.truetransact.uicomponent.CLabel();
        lblShareDetShareNoTo = new com.see.truetransact.uicomponent.CLabel();
        txtTotShareFee = new com.see.truetransact.uicomponent.CTextField();
        txtShareApplFee = new com.see.truetransact.uicomponent.CTextField();
        lblShareDetNoOfShares = new com.see.truetransact.uicomponent.CLabel();
        lblDtShareDetIssShareCert = new com.see.truetransact.uicomponent.CLabel();
        tdtShareDetIssShareCert = new com.see.truetransact.uicomponent.CDateField();
        lblResolutionNo1 = new com.see.truetransact.uicomponent.CLabel();
        txtResolutionNo1 = new com.see.truetransact.uicomponent.CTextField();
        lblTotCollectionAmount = new com.see.truetransact.uicomponent.CLabel();
        txtShareTotAmount = new com.see.truetransact.uicomponent.CTextField();
        lblShareValue = new com.see.truetransact.uicomponent.CLabel();
        txtShareValue = new com.see.truetransact.uicomponent.CTextField();
        panBtnShareAcctDet = new com.see.truetransact.uicomponent.CPanel();
        btnShareAcctDetNew = new com.see.truetransact.uicomponent.CButton();
        btnShareAcctDetSave = new com.see.truetransact.uicomponent.CButton();
        btnShareAcctDetDel = new com.see.truetransact.uicomponent.CButton();
        RsolutionSearch = new com.see.truetransact.uicomponent.CButton();
        FromSL_No = new com.see.truetransact.uicomponent.CLabel();
        txtFromSl_No = new com.see.truetransact.uicomponent.CTextField();
        ToSL_No = new com.see.truetransact.uicomponent.CLabel();
        txtToSL_No = new com.see.truetransact.uicomponent.CTextField();
        lblMaxSlNoValue = new com.see.truetransact.uicomponent.CLabel();
        lblMaxSlNo = new com.see.truetransact.uicomponent.CLabel();
        panBtnShareAcctDet1 = new com.see.truetransact.uicomponent.CPanel();
        lblTotNoOfShares = new com.see.truetransact.uicomponent.CLabel();
        lblTotshareBalVal = new com.see.truetransact.uicomponent.CLabel();
        lblDivAmtVal = new com.see.truetransact.uicomponent.CLabel();
        lblDivAmt = new com.see.truetransact.uicomponent.CLabel();
        lblTotshareBal = new com.see.truetransact.uicomponent.CLabel();
        lblTotNoOfSharesCount = new com.see.truetransact.uicomponent.CLabel();
        panTransaction = new com.see.truetransact.uicomponent.CPanel();
        panOtherDetails = new com.see.truetransact.uicomponent.CPanel();
        panOtherDet = new com.see.truetransact.uicomponent.CPanel();
        lblResolutionNo = new com.see.truetransact.uicomponent.CLabel();
        lblPropertyDetails = new com.see.truetransact.uicomponent.CLabel();
        lblRelativeDetails = new com.see.truetransact.uicomponent.CLabel();
        lblConnGrpDet = new com.see.truetransact.uicomponent.CLabel();
        lblWelFund = new com.see.truetransact.uicomponent.CLabel();
        lblDirRelDet = new com.see.truetransact.uicomponent.CLabel();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        srpPropertyDetails = new com.see.truetransact.uicomponent.CScrollPane();
        txtPropertyDetails = new com.see.truetransact.uicomponent.CTextArea();
        srpDirRelDet = new com.see.truetransact.uicomponent.CScrollPane();
        txtDirRelDet = new com.see.truetransact.uicomponent.CTextArea();
        srpRelativeDetails = new com.see.truetransact.uicomponent.CScrollPane();
        txtRelativeDetails = new com.see.truetransact.uicomponent.CTextArea();
        srpConnGrpDet = new com.see.truetransact.uicomponent.CScrollPane();
        txtConnGrpDet = new com.see.truetransact.uicomponent.CTextArea();
        srpWelFund = new com.see.truetransact.uicomponent.CScrollPane();
        txtWelFund = new com.see.truetransact.uicomponent.CTextArea();
        srpRemarks = new com.see.truetransact.uicomponent.CScrollPane();
        txtRemarks = new com.see.truetransact.uicomponent.CTextArea();
        txtResolutionNo = new com.see.truetransact.uicomponent.CTextField();
        srpDrfTransaction = new com.see.truetransact.uicomponent.CScrollPane();
        tblDrfTransaction = new com.see.truetransact.uicomponent.CTable();
        panMobileBanking = new com.see.truetransact.uicomponent.CPanel();
        chkMobileBanking = new com.see.truetransact.uicomponent.CCheckBox();
        lblMobileNo = new com.see.truetransact.uicomponent.CLabel();
        txtMobileNo = new com.see.truetransact.uicomponent.CTextField();
        tdtMobileSubscribedFrom = new com.see.truetransact.uicomponent.CDateField();
        lblMobileSubscribedFrom = new com.see.truetransact.uicomponent.CLabel();
        panLoanDetails = new com.see.truetransact.uicomponent.CPanel();
        srpLoanAcctHolder = new com.see.truetransact.uicomponent.CScrollPane();
        tblLoanAcctHolder = new com.see.truetransact.uicomponent.CTable();
        lblBalToBePaid = new com.see.truetransact.uicomponent.CLabel();
        mbrMain = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptDelete = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitAuthorize = new javax.swing.JMenuItem();
        mitReject = new javax.swing.JMenuItem();
        mitException = new javax.swing.JMenuItem();
        sptException = new javax.swing.JSeparator();
        mitResolution = new javax.swing.JMenuItem();
        sptResolution = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setMinimumSize(new java.awt.Dimension(875, 700));
        setPreferredSize(new java.awt.Dimension(875, 700));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrShareAcct.add(btnNew);

        lblSpace8.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace8.setText("     ");
        lblSpace8.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace8.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace8.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareAcct.add(lblSpace8);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrShareAcct.add(btnEdit);

        lblSpace9.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace9.setText("     ");
        lblSpace9.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace9.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace9.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareAcct.add(lblSpace9);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrShareAcct.add(btnDelete);

        lblSpace2.setText("     ");
        tbrShareAcct.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrShareAcct.add(btnSave);

        lblSpace10.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace10.setText("     ");
        lblSpace10.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace10.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace10.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareAcct.add(lblSpace10);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.setEnabled(false);
        btnCancel.setFocusable(false);
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrShareAcct.add(btnCancel);

        lblSpace3.setText("     ");
        tbrShareAcct.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrShareAcct.add(btnAuthorize);

        lblSpace11.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace11.setText("     ");
        lblSpace11.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace11.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace11.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareAcct.add(lblSpace11);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrShareAcct.add(btnException);

        lblSpace12.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace12.setText("     ");
        lblSpace12.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace12.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace12.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareAcct.add(lblSpace12);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.setMaximumSize(new java.awt.Dimension(29, 27));
        btnReject.setMinimumSize(new java.awt.Dimension(29, 27));
        btnReject.setPreferredSize(new java.awt.Dimension(29, 27));
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrShareAcct.add(btnReject);

        lblSpace6.setText("     ");
        tbrShareAcct.add(lblSpace6);

        btnResolution.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/regularisation.gif"))); // NOI18N
        btnResolution.setToolTipText("Send to Resolution");
        btnResolution.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResolutionActionPerformed(evt);
            }
        });
        tbrShareAcct.add(btnResolution);

        lblSpace5.setText("     ");
        tbrShareAcct.add(lblSpace5);

        btnLiabilityReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/Membership_information.gif"))); // NOI18N
        btnLiabilityReport.setToolTipText("Membership Liability Report");
        btnLiabilityReport.setFocusable(false);
        btnLiabilityReport.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLiabilityReport.setMaximumSize(new java.awt.Dimension(30, 27));
        btnLiabilityReport.setMinimumSize(new java.awt.Dimension(30, 27));
        btnLiabilityReport.setPreferredSize(new java.awt.Dimension(30, 27));
        btnLiabilityReport.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLiabilityReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLiabilityReportActionPerformed(evt);
            }
        });
        tbrShareAcct.add(btnLiabilityReport);

        lblSpace13.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace13.setText("     ");
        lblSpace13.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace13.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace13.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareAcct.add(lblSpace13);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrShareAcct.add(btnPrint);

        lblSpace14.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace14.setText("     ");
        lblSpace14.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace14.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace14.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareAcct.add(lblSpace14);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrShareAcct.add(btnClose);

        cLabel1.setMaximumSize(new java.awt.Dimension(160, 18));
        cLabel1.setMinimumSize(new java.awt.Dimension(160, 18));
        cLabel1.setPreferredSize(new java.awt.Dimension(160, 18));
        tbrShareAcct.add(cLabel1);

        jLabel3.setForeground(new java.awt.Color(51, 102, 255));
        jLabel3.setText("Next Account Number");
        tbrShareAcct.add(jLabel3);

        txtNextAccNo.setEditable(false);
        txtNextAccNo.setEnabled(false);
        txtNextAccNo.setMaximumSize(new java.awt.Dimension(110, 21));
        txtNextAccNo.setMinimumSize(new java.awt.Dimension(110, 21));
        txtNextAccNo.setPreferredSize(new java.awt.Dimension(110, 21));
        tbrShareAcct.add(txtNextAccNo);

        getContentPane().add(tbrShareAcct, new java.awt.GridBagConstraints());

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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(panStatus, gridBagConstraints);

        panShare.setLayout(new java.awt.GridBagLayout());

        tabShare.setMinimumSize(new java.awt.Dimension(875, 525));
        tabShare.setPreferredSize(new java.awt.Dimension(875, 500));

        panAccountOpening.setMaximumSize(new java.awt.Dimension(875, 420));
        panAccountOpening.setMinimumSize(new java.awt.Dimension(877, 418));
        panAccountOpening.setPreferredSize(new java.awt.Dimension(877, 418));
        panAccountOpening.setLayout(new java.awt.GridBagLayout());

        panShareAcctInfo.setMaximumSize(new java.awt.Dimension(880, 300));
        panShareAcctInfo.setMinimumSize(new java.awt.Dimension(880, 309));
        panShareAcctInfo.setPreferredSize(new java.awt.Dimension(880, 300));
        panShareAcctInfo.setLayout(new java.awt.GridBagLayout());

        panShareDet.setMaximumSize(new java.awt.Dimension(557, 235));
        panShareDet.setMinimumSize(new java.awt.Dimension(557, 235));
        panShareDet.setPreferredSize(new java.awt.Dimension(557, 235));
        panShareDet.setLayout(new java.awt.GridBagLayout());

        panShareAcctNo.setMaximumSize(new java.awt.Dimension(255, 320));
        panShareAcctNo.setMinimumSize(new java.awt.Dimension(245, 225));
        panShareAcctNo.setPreferredSize(new java.awt.Dimension(245, 225));
        panShareAcctNo.setLayout(new java.awt.GridBagLayout());

        lblShareAcctNo.setText("Share Acct No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panShareAcctNo.add(lblShareAcctNo, gridBagConstraints);

        lblCustId.setText("Customer Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panShareAcctNo.add(lblCustId, gridBagConstraints);

        panCustId.setLayout(new java.awt.GridBagLayout());

        txtCustId.setEnabled(false);
        txtCustId.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCustId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCustIdActionPerformed(evt);
            }
        });
        txtCustId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCustIdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panCustId.add(txtCustId, gridBagConstraints);

        btnCustomerIdFileOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCustomerIdFileOpen.setEnabled(false);
        btnCustomerIdFileOpen.setMaximumSize(new java.awt.Dimension(21, 21));
        btnCustomerIdFileOpen.setMinimumSize(new java.awt.Dimension(21, 21));
        btnCustomerIdFileOpen.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCustomerIdFileOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustomerIdFileOpenActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCustId.add(btnCustomerIdFileOpen, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panShareAcctNo.add(panCustId, gridBagConstraints);

        lblAcctStatus.setText("Account Status");
        lblAcctStatus.setToolTipText("Account Status");
        lblAcctStatus.setMaximumSize(new java.awt.Dimension(120, 18));
        lblAcctStatus.setMinimumSize(new java.awt.Dimension(110, 18));
        lblAcctStatus.setPreferredSize(new java.awt.Dimension(110, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panShareAcctNo.add(lblAcctStatus, gridBagConstraints);

        cboAcctStatus.setMinimumSize(new java.awt.Dimension(100, 21));
        cboAcctStatus.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panShareAcctNo.add(cboAcctStatus, gridBagConstraints);

        lblConstitution.setText("Constitution");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panShareAcctNo.add(lblConstitution, gridBagConstraints);

        cboConstitution.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "----Select----" }));
        cboConstitution.setMinimumSize(new java.awt.Dimension(100, 21));
        cboConstitution.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboConstitutionActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panShareAcctNo.add(cboConstitution, gridBagConstraints);

        lblValCustName.setForeground(new java.awt.Color(0, 51, 204));
        lblValCustName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblValCustName.setMaximumSize(new java.awt.Dimension(120, 17));
        lblValCustName.setMinimumSize(new java.awt.Dimension(120, 17));
        lblValCustName.setPreferredSize(new java.awt.Dimension(120, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 4);
        panShareAcctNo.add(lblValCustName, gridBagConstraints);

        lblShareType.setText("Share Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panShareAcctNo.add(lblShareType, gridBagConstraints);

        cboShareType.setMaximumSize(new java.awt.Dimension(100, 21));
        cboShareType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboShareType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboShareTypeActionPerformed(evt);
            }
        });
        cboShareType.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboShareTypeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panShareAcctNo.add(cboShareType, gridBagConstraints);

        lblApplFee.setText("Application Fee");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panShareAcctNo.add(lblApplFee, gridBagConstraints);

        txtApplFee.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panShareAcctNo.add(txtApplFee, gridBagConstraints);

        lblMemFee.setText("Membership Fee");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panShareAcctNo.add(lblMemFee, gridBagConstraints);

        txtMemFee.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panShareAcctNo.add(txtMemFee, gridBagConstraints);

        lblShareFee.setText("Share Fee");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panShareAcctNo.add(lblShareFee, gridBagConstraints);

        txtShareFee.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panShareAcctNo.add(txtShareFee, gridBagConstraints);

        lblExistCust.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lblExistCust.setText("Existing Customer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        panShareAcctNo.add(lblExistCust, gridBagConstraints);

        panExistCust.setMinimumSize(new java.awt.Dimension(104, 19));
        panExistCust.setPreferredSize(new java.awt.Dimension(104, 19));
        panExistCust.setLayout(new java.awt.GridBagLayout());

        rdoExistCustYes.setSelected(true);
        rdoExistCustYes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        panExistCust.add(rdoExistCustYes, gridBagConstraints);

        rdoExistCustNo.setText("No");
        rdoExistCustNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoExistCustNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        panExistCust.add(rdoExistCustNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 1);
        panShareAcctNo.add(panExistCust, gridBagConstraints);

        panCustId3.setMinimumSize(new java.awt.Dimension(121, 21));
        panCustId3.setPreferredSize(new java.awt.Dimension(121, 21));
        panCustId3.setLayout(new java.awt.GridBagLayout());

        btnShareActNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnShareActNo.setEnabled(false);
        btnShareActNo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnShareActNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnShareActNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnShareActNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShareActNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCustId3.add(btnShareActNo, gridBagConstraints);

        txtShareAcctNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtShareAcctNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtShareAcctNoActionPerformed(evt);
            }
        });
        txtShareAcctNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtShareAcctNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCustId3.add(txtShareAcctNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panShareAcctNo.add(panCustId3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panShareDet.add(panShareAcctNo, gridBagConstraints);

        panDireRelDet.setMaximumSize(new java.awt.Dimension(290, 200));
        panDireRelDet.setMinimumSize(new java.awt.Dimension(280, 195));
        panDireRelDet.setPreferredSize(new java.awt.Dimension(280, 195));
        panDireRelDet.setLayout(new java.awt.GridBagLayout());

        lblDtIssId.setText("Date of issue of Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDireRelDet.add(lblDtIssId, gridBagConstraints);

        tdtIssId.setEnabled(false);
        tdtIssId.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtIssId.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDireRelDet.add(tdtIssId, gridBagConstraints);

        lblNotEligibleStatus.setText("Not Eligible for loan");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDireRelDet.add(lblNotEligibleStatus, gridBagConstraints);

        lblDtNotEligiblePeriod.setText("Not Eligible For Loan Upto");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDireRelDet.add(lblDtNotEligiblePeriod, gridBagConstraints);

        tdtNotEligiblePeriod.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtNotEligiblePeriod.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDireRelDet.add(tdtNotEligiblePeriod, gridBagConstraints);

        chkNotEligibleStatus.setPreferredSize(new java.awt.Dimension(10, 19));
        chkNotEligibleStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkNotEligibleStatusActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDireRelDet.add(chkNotEligibleStatus, gridBagConstraints);

        txtShareAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDireRelDet.add(txtShareAmt, gridBagConstraints);

        lblShareAmt.setText("Share Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDireRelDet.add(lblShareAmt, gridBagConstraints);

        lblCommAddrType.setText("Communication Addr Type");
        lblCommAddrType.setToolTipText("Communication Addr. Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDireRelDet.add(lblCommAddrType, gridBagConstraints);

        cboCommAddrType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDireRelDet.add(cboCommAddrType, gridBagConstraints);

        cboMemDivProdID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMemDivProdIDActionPerformed(evt);
            }
        });
        cboMemDivProdID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboMemDivProdIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDireRelDet.add(cboMemDivProdID, gridBagConstraints);

        cboMemDivProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboMemDivProdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMemDivProdTypeActionPerformed(evt);
            }
        });
        cboMemDivProdType.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboMemDivProdTypeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDireRelDet.add(cboMemDivProdType, gridBagConstraints);

        lblMemDivProdType.setText("Dividend Prod Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDireRelDet.add(lblMemDivProdType, gridBagConstraints);

        lblMemDivProdId.setText("Dividend Prod Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDireRelDet.add(lblMemDivProdId, gridBagConstraints);

        lblMemDivAcNo.setText("Dividend Account No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDireRelDet.add(lblMemDivAcNo, gridBagConstraints);

        panCustId1.setLayout(new java.awt.GridBagLayout());

        txtDivAcNo.setEnabled(false);
        txtDivAcNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDivAcNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDivAcNoActionPerformed(evt);
            }
        });
        txtDivAcNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDivAcNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panCustId1.add(txtDivAcNo, gridBagConstraints);

        btnDivAcNoFileOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDivAcNoFileOpen.setEnabled(false);
        btnDivAcNoFileOpen.setMaximumSize(new java.awt.Dimension(21, 21));
        btnDivAcNoFileOpen.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDivAcNoFileOpen.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDivAcNoFileOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDivAcNoFileOpenActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCustId1.add(btnDivAcNoFileOpen, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDireRelDet.add(panCustId1, gridBagConstraints);

        cboMemDivPayMode.setMinimumSize(new java.awt.Dimension(100, 21));
        cboMemDivPayMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMemDivPayModeActionPerformed(evt);
            }
        });
        cboMemDivPayMode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboMemDivPayModeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDireRelDet.add(cboMemDivPayMode, gridBagConstraints);

        lblMemDivPayMode.setText("Dividend Pay Mode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDireRelDet.add(lblMemDivPayMode, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panShareDet.add(panDireRelDet, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panShareAcctInfo.add(panShareDet, gridBagConstraints);

        panShareTrans.setMaximumSize(new java.awt.Dimension(270, 23));
        panShareTrans.setMinimumSize(new java.awt.Dimension(500, 23));
        panShareTrans.setPreferredSize(new java.awt.Dimension(270, 23));
        panShareTrans.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panShareTrans.add(lblTotNoOfSharesCount1, gridBagConstraints);

        rdoShareAddition.setText("Addition");
        rdoShareAddition.setMaximumSize(new java.awt.Dimension(90, 18));
        rdoShareAddition.setMinimumSize(new java.awt.Dimension(90, 18));
        rdoShareAddition.setPreferredSize(new java.awt.Dimension(90, 18));
        rdoShareAddition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoShareAdditionActionPerformed(evt);
            }
        });
        rdoShareAddition.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                rdoShareAdditionFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panShareTrans.add(rdoShareAddition, gridBagConstraints);

        rdoSharewithDrawal.setText("withDrawal");
        rdoSharewithDrawal.setMaximumSize(new java.awt.Dimension(45, 18));
        rdoSharewithDrawal.setMinimumSize(new java.awt.Dimension(90, 18));
        rdoSharewithDrawal.setPreferredSize(new java.awt.Dimension(90, 18));
        rdoSharewithDrawal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSharewithDrawalActionPerformed(evt);
            }
        });
        rdoSharewithDrawal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                rdoSharewithDrawalFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panShareTrans.add(rdoSharewithDrawal, gridBagConstraints);

        lblTransType.setText("TransType");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panShareTrans.add(lblTransType, gridBagConstraints);

        rdoShareDifferentialAmount.setText("Share - Differential Amount");
        rdoShareDifferentialAmount.setMaximumSize(new java.awt.Dimension(280, 18));
        rdoShareDifferentialAmount.setMinimumSize(new java.awt.Dimension(280, 18));
        rdoShareDifferentialAmount.setPreferredSize(new java.awt.Dimension(200, 18));
        rdoShareDifferentialAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoShareDifferentialAmountActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panShareTrans.add(rdoShareDifferentialAmount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panShareAcctInfo.add(panShareTrans, gridBagConstraints);

        panShareImbp.setMaximumSize(new java.awt.Dimension(245, 320));
        panShareImbp.setMinimumSize(new java.awt.Dimension(540, 50));
        panShareImbp.setPreferredSize(new java.awt.Dimension(230, 330));
        panShareImbp.setLayout(new java.awt.GridBagLayout());

        lblEmpRefNoNew.setText("Employer Ref No.(New)");
        lblEmpRefNoNew.setMinimumSize(new java.awt.Dimension(146, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panShareImbp.add(lblEmpRefNoNew, gridBagConstraints);

        lblEmpRefNoOld.setText("Employer Ref No.(Old)");
        lblEmpRefNoOld.setMinimumSize(new java.awt.Dimension(145, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 1, 2, 4);
        panShareImbp.add(lblEmpRefNoOld, gridBagConstraints);

        txtEmpRefNoOld.setMinimumSize(new java.awt.Dimension(99, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panShareImbp.add(txtEmpRefNoOld, gridBagConstraints);

        panCustId2.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panShareImbp.add(panCustId2, gridBagConstraints);

        lblImbp.setText("IMBP");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panShareImbp.add(lblImbp, gridBagConstraints);

        txtEmpRefNoNew.setMinimumSize(new java.awt.Dimension(100, 21));
        txtEmpRefNoNew.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEmpRefNoNewFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panShareImbp.add(txtEmpRefNoNew, gridBagConstraints);

        txtImbp.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panShareImbp.add(txtImbp, gridBagConstraints);

        jPanel2.setMinimumSize(new java.awt.Dimension(290, 25));
        jPanel2.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        panShareImbp.add(jPanel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panShareAcctInfo.add(panShareImbp, gridBagConstraints);

        panCustomerSide.setBorder(javax.swing.BorderFactory.createTitledBorder("Customer Details"));
        panCustomerSide.setMinimumSize(new java.awt.Dimension(365, 235));
        panCustomerSide.setPreferredSize(new java.awt.Dimension(365, 235));
        panCustomerSide.setLayout(new java.awt.GridBagLayout());

        panCustomerName.setMinimumSize(new java.awt.Dimension(10, 5));
        panCustomerName.setPreferredSize(new java.awt.Dimension(10, 5));
        panCustomerName.setLayout(new java.awt.GridBagLayout());

        lblValCustomerName.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        lblValCustomerName.setMinimumSize(new java.awt.Dimension(70, 10));
        lblValCustomerName.setPreferredSize(new java.awt.Dimension(70, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCustomerName.add(lblValCustomerName, gridBagConstraints);

        lblCustomerName.setText("Customer Name");
        lblCustomerName.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCustomerName.add(lblCustomerName, gridBagConstraints);

        lblDateOfBirth.setText("Date of Birth");
        lblDateOfBirth.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCustomerName.add(lblDateOfBirth, gridBagConstraints);

        lblValDateOfBirth.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCustomerName.add(lblValDateOfBirth, gridBagConstraints);

        lblValStreet.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        lblValStreet.setMinimumSize(new java.awt.Dimension(70, 10));
        lblValStreet.setPreferredSize(new java.awt.Dimension(70, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCustomerName.add(lblValStreet, gridBagConstraints);

        lblStreet.setText("Street");
        lblStreet.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCustomerName.add(lblStreet, gridBagConstraints);

        lblArea.setText("Area");
        lblArea.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCustomerName.add(lblArea, gridBagConstraints);

        lblValArea.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        lblValArea.setMinimumSize(new java.awt.Dimension(70, 10));
        lblValArea.setPreferredSize(new java.awt.Dimension(70, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCustomerName.add(lblValArea, gridBagConstraints);

        lblValCity.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        lblValCity.setMinimumSize(new java.awt.Dimension(50, 10));
        lblValCity.setPreferredSize(new java.awt.Dimension(50, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCustomerName.add(lblValCity, gridBagConstraints);

        lblCity.setText("City");
        lblCity.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCustomerName.add(lblCity, gridBagConstraints);

        lblCountry.setText("Country");
        lblCountry.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCustomerName.add(lblCountry, gridBagConstraints);

        lblValCountry.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        lblValCountry.setMinimumSize(new java.awt.Dimension(50, 10));
        lblValCountry.setPreferredSize(new java.awt.Dimension(50, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCustomerName.add(lblValCountry, gridBagConstraints);

        lblValPin.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        lblValPin.setMinimumSize(new java.awt.Dimension(50, 10));
        lblValPin.setPreferredSize(new java.awt.Dimension(50, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCustomerName.add(lblValPin, gridBagConstraints);

        lblPin.setText("Pin");
        lblPin.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCustomerName.add(lblPin, gridBagConstraints);

        lblState.setText("State");
        lblState.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCustomerName.add(lblState, gridBagConstraints);

        lblValState.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        lblValState.setMinimumSize(new java.awt.Dimension(50, 10));
        lblValState.setPreferredSize(new java.awt.Dimension(50, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCustomerName.add(lblValState, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.weightx = 1.0;
        panCustomerSide.add(panCustomerName, gridBagConstraints);

        panJointAcctHolder.setMinimumSize(new java.awt.Dimension(300, 100));
        panJointAcctHolder.setPreferredSize(new java.awt.Dimension(300, 100));
        panJointAcctHolder.setLayout(new java.awt.GridBagLayout());

        srpJointAcctHolder.setMinimumSize(new java.awt.Dimension(310, 100));
        srpJointAcctHolder.setPreferredSize(new java.awt.Dimension(310, 100));

        tblJointAcctHolder.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Cust. Id", "Type", "Main / Joint", "Title 5"
            }
        ));
        tblJointAcctHolder.setMinimumSize(new java.awt.Dimension(300, 100));
        tblJointAcctHolder.setPreferredScrollableViewportSize(new java.awt.Dimension(300, 400));
        tblJointAcctHolder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblJointAcctHolderMousePressed(evt);
            }
        });
        srpJointAcctHolder.setViewportView(tblJointAcctHolder);

        panJointAcctHolder.add(srpJointAcctHolder, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panCustomerSide.add(panJointAcctHolder, gridBagConstraints);

        panJointAcctButton.setLayout(new java.awt.GridBagLayout());

        btnJointAcctNew.setText("New");
        btnJointAcctNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJointAcctNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panJointAcctButton.add(btnJointAcctNew, gridBagConstraints);

        btnJointAcctDel.setText("Delete");
        btnJointAcctDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJointAcctDelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panJointAcctButton.add(btnJointAcctDel, gridBagConstraints);

        btnJointAcctToMain.setText("To Main");
        btnJointAcctToMain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJointAcctToMainActionPerformed(evt);
            }
        });
        panJointAcctButton.add(btnJointAcctToMain, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panCustomerSide.add(panJointAcctButton, gridBagConstraints);

        panCudtomerIDDetails.setMinimumSize(new java.awt.Dimension(130, 70));
        panCudtomerIDDetails.setPreferredSize(new java.awt.Dimension(330, 70));
        panCudtomerIDDetails.setLayout(new java.awt.GridBagLayout());

        lblDuplicateIDCardYN.setText("Duplicate ID");
        lblDuplicateIDCardYN.setName("lblProfession"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panCudtomerIDDetails.add(lblDuplicateIDCardYN, gridBagConstraints);

        chkDuplicateIDCardYN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                chkDuplicateIDCardYNMousePressed(evt);
            }
        });
        chkDuplicateIDCardYN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkDuplicateIDCardYNActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panCudtomerIDDetails.add(chkDuplicateIDCardYN, gridBagConstraints);

        lblIDCardNo.setText("ID Card No");
        lblIDCardNo.setName("lblProfession"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panCudtomerIDDetails.add(lblIDCardNo, gridBagConstraints);

        txtIDCardNo.setAllowAll(true);
        txtIDCardNo.setMinimumSize(new java.awt.Dimension(80, 21));
        txtIDCardNo.setPreferredSize(new java.awt.Dimension(80, 21));
        txtIDCardNo.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panCudtomerIDDetails.add(txtIDCardNo, gridBagConstraints);

        lblApplicationNo.setText("Appl No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCudtomerIDDetails.add(lblApplicationNo, gridBagConstraints);

        txtIDResolutionNo.setAllowAll(true);
        txtIDResolutionNo.setMinimumSize(new java.awt.Dimension(80, 21));
        txtIDResolutionNo.setPreferredSize(new java.awt.Dimension(80, 21));
        txtIDResolutionNo.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panCudtomerIDDetails.add(txtIDResolutionNo, gridBagConstraints);

        lblIDResolutionNo.setText("Resol. ID");
        lblIDResolutionNo.setName("lblProfession"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panCudtomerIDDetails.add(lblIDResolutionNo, gridBagConstraints);

        lblIDIssuedDt.setText("Issued Dt");
        lblIDIssuedDt.setName("lblProfession"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCudtomerIDDetails.add(lblIDIssuedDt, gridBagConstraints);

        tdtIDResolutionDt.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtIDResolutionDt.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCudtomerIDDetails.add(tdtIDResolutionDt, gridBagConstraints);

        lblIDResolutionDt.setText("Resol. Dt");
        lblIDResolutionDt.setName("lblProfession"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panCudtomerIDDetails.add(lblIDResolutionDt, gridBagConstraints);

        tdtIDIssuedDt.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtIDIssuedDt.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCudtomerIDDetails.add(tdtIDIssuedDt, gridBagConstraints);

        cPanel1.setLayout(new java.awt.GridBagLayout());

        btnShareAppNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnShareAppNo.setEnabled(false);
        btnShareAppNo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnShareAppNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnShareAppNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnShareAppNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShareAppNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        cPanel1.add(btnShareAppNo, gridBagConstraints);

        txtApplicationNo.setAllowAll(true);
        txtApplicationNo.setMinimumSize(new java.awt.Dimension(80, 21));
        txtApplicationNo.setPreferredSize(new java.awt.Dimension(80, 21));
        txtApplicationNo.setValidation(new DefaultValidation());
        txtApplicationNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtApplicationNoActionPerformed(evt);
            }
        });
        txtApplicationNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtApplicationNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        cPanel1.add(txtApplicationNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        panCudtomerIDDetails.add(cPanel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panCustomerSide.add(panCudtomerIDDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panShareAcctInfo.add(panCustomerSide, gridBagConstraints);

        panDrfApplicablle.setMaximumSize(new java.awt.Dimension(350, 45));
        panDrfApplicablle.setMinimumSize(new java.awt.Dimension(350, 45));
        panDrfApplicablle.setPreferredSize(new java.awt.Dimension(350, 45));
        panDrfApplicablle.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panDrfApplicablle.add(lblTotNoOfSharesCount2, gridBagConstraints);

        lblTransType1.setText("DRF Applicable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDrfApplicablle.add(lblTransType1, gridBagConstraints);

        chkDrfApplicableYN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkDrfApplicableYNActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDrfApplicablle.add(chkDrfApplicableYN, gridBagConstraints);

        lblDrfProdId.setText("Drf Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDrfApplicablle.add(lblDrfProdId, gridBagConstraints);

        cboDrfProdId.setMinimumSize(new java.awt.Dimension(80, 22));
        cboDrfProdId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboDrfProdIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panDrfApplicablle.add(cboDrfProdId, gridBagConstraints);

        lblDRFAmt.setText("DRF Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        panDrfApplicablle.add(lblDRFAmt, gridBagConstraints);

        lblDrfAmtVal.setMinimumSize(new java.awt.Dimension(80, 18));
        lblDrfAmtVal.setPreferredSize(new java.awt.Dimension(80, 18));
        lblDrfAmtVal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                lblDrfAmtValFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                lblDrfAmtValFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDrfApplicablle.add(lblDrfAmtVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panShareAcctInfo.add(panDrfApplicablle, gridBagConstraints);

        btnIdPrint.setText("ID Print ");
        btnIdPrint.setMaximumSize(new java.awt.Dimension(97, 21));
        btnIdPrint.setMinimumSize(new java.awt.Dimension(97, 21));
        btnIdPrint.setPreferredSize(new java.awt.Dimension(97, 21));
        btnIdPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIdPrintActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 19);
        panShareAcctInfo.add(btnIdPrint, gridBagConstraints);

        lblServiceTax.setText("Service Tax");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        panShareAcctInfo.add(lblServiceTax, gridBagConstraints);

        lblServiceTaxval.setMaximumSize(new java.awt.Dimension(75, 18));
        lblServiceTaxval.setMinimumSize(new java.awt.Dimension(75, 18));
        lblServiceTaxval.setOpaque(true);
        lblServiceTaxval.setPreferredSize(new java.awt.Dimension(75, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        panShareAcctInfo.add(lblServiceTaxval, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panAccountOpening.add(panShareAcctInfo, gridBagConstraints);

        tabTransaction.setMaximumSize(new java.awt.Dimension(705, 298));
        tabTransaction.setMinimumSize(new java.awt.Dimension(705, 243));
        tabTransaction.setPreferredSize(new java.awt.Dimension(705, 298));

        panShareAcctDet.setBorder(javax.swing.BorderFactory.createTitledBorder("Share Account Details"));
        panShareAcctDet.setMaximumSize(new java.awt.Dimension(552, 180));
        panShareAcctDet.setMinimumSize(new java.awt.Dimension(552, 180));
        panShareAcctDet.setPreferredSize(new java.awt.Dimension(552, 180));
        panShareAcctDet.setLayout(new java.awt.GridBagLayout());

        panTblShareAcctDet.setMinimumSize(new java.awt.Dimension(380, 150));
        panTblShareAcctDet.setPreferredSize(new java.awt.Dimension(380, 150));
        panTblShareAcctDet.setLayout(new java.awt.GridBagLayout());

        srpTblShareAccntDet.setMinimumSize(new java.awt.Dimension(400, 150));
        srpTblShareAccntDet.setPreferredSize(new java.awt.Dimension(400, 150));

        tblShareAcctDet.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Serial No.", "Issue Date ", "No of Shares", "Share Value", "Status", "add/withdrawl"
            }
        ));
        tblShareAcctDet.setMinimumSize(new java.awt.Dimension(225, 16));
        tblShareAcctDet.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tblShareAcctDetMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblShareAcctDetMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblShareAcctDetMouseReleased(evt);
            }
        });
        srpTblShareAccntDet.setViewportView(tblShareAcctDet);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 13, 0);
        panTblShareAcctDet.add(srpTblShareAccntDet, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 27;
        gridBagConstraints.ipady = -8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 1, 1, 1);
        panShareAcctDet.add(panTblShareAcctDet, gridBagConstraints);

        panShareAcctDetails.setMaximumSize(new java.awt.Dimension(405, 265));
        panShareAcctDetails.setMinimumSize(new java.awt.Dimension(405, 265));
        panShareAcctDetails.setPreferredSize(new java.awt.Dimension(405, 265));
        panShareAcctDetails.setLayout(new java.awt.GridBagLayout());

        lblNoShares.setText(" No. of Shares ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(33, 40, 0, 0);
        panShareAcctDetails.add(lblNoShares, gridBagConstraints);

        txtNoShares.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNoShares.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNoSharesActionPerformed(evt);
            }
        });
        txtNoShares.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNoSharesFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNoSharesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(32, 0, 0, 0);
        panShareAcctDetails.add(txtNoShares, gridBagConstraints);

        txtShareMemFee.setEnabled(false);
        txtShareMemFee.setMinimumSize(new java.awt.Dimension(100, 21));
        txtShareMemFee.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtShareMemFeeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panShareAcctDetails.add(txtShareMemFee, gridBagConstraints);

        lblShareDetShareNoFrom.setText("Total Membership Fee");
        lblShareDetShareNoFrom.setToolTipText("Total Membership Fee");
        lblShareDetShareNoFrom.setMaximumSize(new java.awt.Dimension(140, 18));
        lblShareDetShareNoFrom.setMinimumSize(new java.awt.Dimension(140, 18));
        lblShareDetShareNoFrom.setPreferredSize(new java.awt.Dimension(140, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        panShareAcctDetails.add(lblShareDetShareNoFrom, gridBagConstraints);

        lblShareDetShareNoTo.setText("Share Fee");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        panShareAcctDetails.add(lblShareDetShareNoTo, gridBagConstraints);

        txtTotShareFee.setEnabled(false);
        txtTotShareFee.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTotShareFee.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTotShareFeeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
        panShareAcctDetails.add(txtTotShareFee, gridBagConstraints);

        txtShareApplFee.setMinimumSize(new java.awt.Dimension(100, 21));
        txtShareApplFee.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtShareApplFeeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panShareAcctDetails.add(txtShareApplFee, gridBagConstraints);

        lblShareDetNoOfShares.setText("Total Application Fee");
        lblShareDetNoOfShares.setToolTipText("Total Application Fee");
        lblShareDetNoOfShares.setMaximumSize(new java.awt.Dimension(135, 18));
        lblShareDetNoOfShares.setMinimumSize(new java.awt.Dimension(135, 18));
        lblShareDetNoOfShares.setPreferredSize(new java.awt.Dimension(135, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 9, 0, 0);
        panShareAcctDetails.add(lblShareDetNoOfShares, gridBagConstraints);

        lblDtShareDetIssShareCert.setText("Resolution Dt");
        lblDtShareDetIssShareCert.setToolTipText("Resolution Date ");
        lblDtShareDetIssShareCert.setMaximumSize(new java.awt.Dimension(85, 18));
        lblDtShareDetIssShareCert.setMinimumSize(new java.awt.Dimension(85, 18));
        lblDtShareDetIssShareCert.setPreferredSize(new java.awt.Dimension(85, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        panShareAcctDetails.add(lblDtShareDetIssShareCert, gridBagConstraints);

        tdtShareDetIssShareCert.setEnabled(false);
        tdtShareDetIssShareCert.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtShareDetIssShareCert.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
        panShareAcctDetails.add(tdtShareDetIssShareCert, gridBagConstraints);

        lblResolutionNo1.setText("Resolution No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 46, 0, 0);
        panShareAcctDetails.add(lblResolutionNo1, gridBagConstraints);

        txtResolutionNo1.setEnabled(false);
        txtResolutionNo1.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panShareAcctDetails.add(txtResolutionNo1, gridBagConstraints);

        lblTotCollectionAmount.setText("Total Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 54, 0, 0);
        panShareAcctDetails.add(lblTotCollectionAmount, gridBagConstraints);

        txtShareTotAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panShareAcctDetails.add(txtShareTotAmount, gridBagConstraints);

        lblShareValue.setText("Share Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 60, 0, 0);
        panShareAcctDetails.add(lblShareValue, gridBagConstraints);

        txtShareValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtShareValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtShareValueActionPerformed(evt);
            }
        });
        txtShareValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtShareValueFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panShareAcctDetails.add(txtShareValue, gridBagConstraints);

        panBtnShareAcctDet.setMaximumSize(new java.awt.Dimension(250, 27));
        panBtnShareAcctDet.setMinimumSize(new java.awt.Dimension(250, 27));
        panBtnShareAcctDet.setPreferredSize(new java.awt.Dimension(250, 27));
        panBtnShareAcctDet.setLayout(new java.awt.GridBagLayout());

        btnShareAcctDetNew.setText("New");
        btnShareAcctDetNew.setToolTipText("New");
        btnShareAcctDetNew.setMaximumSize(new java.awt.Dimension(95, 27));
        btnShareAcctDetNew.setMinimumSize(new java.awt.Dimension(95, 27));
        btnShareAcctDetNew.setPreferredSize(new java.awt.Dimension(95, 27));
        btnShareAcctDetNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShareAcctDetNewActionPerformed(evt);
            }
        });
        panBtnShareAcctDet.add(btnShareAcctDetNew, new java.awt.GridBagConstraints());

        btnShareAcctDetSave.setText("Save");
        btnShareAcctDetSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShareAcctDetSaveActionPerformed(evt);
            }
        });
        panBtnShareAcctDet.add(btnShareAcctDetSave, new java.awt.GridBagConstraints());

        btnShareAcctDetDel.setText("Delete");
        btnShareAcctDetDel.setToolTipText("Delete");
        btnShareAcctDetDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShareAcctDetDelActionPerformed(evt);
            }
        });
        panBtnShareAcctDet.add(btnShareAcctDetDel, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 23, 33, -30);
        panShareAcctDetails.add(panBtnShareAcctDet, gridBagConstraints);

        RsolutionSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        RsolutionSearch.setEnabled(false);
        RsolutionSearch.setMaximumSize(new java.awt.Dimension(21, 21));
        RsolutionSearch.setMinimumSize(new java.awt.Dimension(21, 21));
        RsolutionSearch.setPreferredSize(new java.awt.Dimension(21, 21));
        RsolutionSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RsolutionSearchActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panShareAcctDetails.add(RsolutionSearch, gridBagConstraints);

        FromSL_No.setText("FromSL_No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 13, 0, 0);
        panShareAcctDetails.add(FromSL_No, gridBagConstraints);

        txtFromSl_No.setMaximumSize(new java.awt.Dimension(50, 21));
        txtFromSl_No.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFromSl_No.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFromSl_NoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
        panShareAcctDetails.add(txtFromSl_No, gridBagConstraints);

        ToSL_No.setText("ToSL_No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        panShareAcctDetails.add(ToSL_No, gridBagConstraints);

        txtToSL_No.setMaximumSize(new java.awt.Dimension(100, 21));
        txtToSL_No.setMinimumSize(new java.awt.Dimension(100, 21));
        txtToSL_No.setNextFocusableComponent(btnShareAcctDetSave);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
        panShareAcctDetails.add(txtToSL_No, gridBagConstraints);

        lblMaxSlNoValue.setText("Max SL_No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(33, 14, 0, 0);
        panShareAcctDetails.add(lblMaxSlNoValue, gridBagConstraints);

        lblMaxSlNo.setText("10");
        lblMaxSlNo.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblMaxSlNo.setMinimumSize(new java.awt.Dimension(100, 21));
        lblMaxSlNo.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(32, 0, 0, 3);
        panShareAcctDetails.add(lblMaxSlNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 45;
        gridBagConstraints.ipady = -47;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panShareAcctDet.add(panShareAcctDetails, gridBagConstraints);

        panBtnShareAcctDet1.setMinimumSize(new java.awt.Dimension(400, 27));
        panBtnShareAcctDet1.setPreferredSize(new java.awt.Dimension(400, 27));
        panBtnShareAcctDet1.setLayout(new java.awt.GridBagLayout());

        lblTotNoOfShares.setText(" Total Shares : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 0, 0);
        panBtnShareAcctDet1.add(lblTotNoOfShares, gridBagConstraints);

        lblTotshareBalVal.setMaximumSize(new java.awt.Dimension(120, 21));
        lblTotshareBalVal.setMinimumSize(new java.awt.Dimension(120, 21));
        lblTotshareBalVal.setPreferredSize(new java.awt.Dimension(120, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -45;
        gridBagConstraints.ipady = -8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 6, 0, 0);
        panBtnShareAcctDet1.add(lblTotshareBalVal, gridBagConstraints);

        lblDivAmtVal.setMaximumSize(new java.awt.Dimension(100, 21));
        lblDivAmtVal.setMinimumSize(new java.awt.Dimension(100, 21));
        lblDivAmtVal.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -69;
        gridBagConstraints.ipady = -3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 0, 17);
        panBtnShareAcctDet1.add(lblDivAmtVal, gridBagConstraints);

        lblDivAmt.setText("Dividend Amt :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 0, 0);
        panBtnShareAcctDet1.add(lblDivAmt, gridBagConstraints);

        lblTotshareBal.setText("Balance : ");
        lblTotshareBal.setToolTipText("Balance : ");
        lblTotshareBal.setMaximumSize(new java.awt.Dimension(65, 18));
        lblTotshareBal.setMinimumSize(new java.awt.Dimension(65, 18));
        lblTotshareBal.setPreferredSize(new java.awt.Dimension(65, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        panBtnShareAcctDet1.add(lblTotshareBal, gridBagConstraints);

        lblTotNoOfSharesCount.setMaximumSize(new java.awt.Dimension(100, 21));
        lblTotNoOfSharesCount.setMinimumSize(new java.awt.Dimension(100, 21));
        lblTotNoOfSharesCount.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = -63;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        panBtnShareAcctDet1.add(lblTotNoOfSharesCount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipady = -4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 10, 1, 1);
        panShareAcctDet.add(panBtnShareAcctDet1, gridBagConstraints);

        tabTransaction.addTab("Share Account Details", panShareAcctDet);

        panTransaction.setMinimumSize(new java.awt.Dimension(552, 95));
        panTransaction.setPreferredSize(new java.awt.Dimension(552, 95));
        panTransaction.setLayout(new java.awt.GridBagLayout());
        tabTransaction.addTab("Transaction Details", panTransaction);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        panAccountOpening.add(tabTransaction, gridBagConstraints);

        tabShare.addTab("Share Account Information", panAccountOpening);

        panOtherDetails.setLayout(new java.awt.GridBagLayout());

        panOtherDet.setMaximumSize(new java.awt.Dimension(500, 700));
        panOtherDet.setMinimumSize(new java.awt.Dimension(475, 700));
        panOtherDet.setPreferredSize(new java.awt.Dimension(475, 700));
        panOtherDet.setLayout(new java.awt.GridBagLayout());

        lblResolutionNo.setText("Resolution No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherDet.add(lblResolutionNo, gridBagConstraints);

        lblPropertyDetails.setText("Property Details");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherDet.add(lblPropertyDetails, gridBagConstraints);

        lblRelativeDetails.setText("Relative Mem Details");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherDet.add(lblRelativeDetails, gridBagConstraints);

        lblConnGrpDet.setText("Connected Group Details");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherDet.add(lblConnGrpDet, gridBagConstraints);

        lblWelFund.setText("Welfare Fund paid Details");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherDet.add(lblWelFund, gridBagConstraints);

        lblDirRelDet.setText("Director Relative Details");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherDet.add(lblDirRelDet, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        panOtherDet.add(lblRemarks, gridBagConstraints);

        srpPropertyDetails.setMaximumSize(new java.awt.Dimension(304, 54));
        srpPropertyDetails.setMinimumSize(new java.awt.Dimension(304, 54));

        txtPropertyDetails.setPreferredSize(new java.awt.Dimension(300, 50));
        srpPropertyDetails.setViewportView(txtPropertyDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherDet.add(srpPropertyDetails, gridBagConstraints);

        srpDirRelDet.setMaximumSize(new java.awt.Dimension(304, 54));
        srpDirRelDet.setMinimumSize(new java.awt.Dimension(304, 54));

        txtDirRelDet.setPreferredSize(new java.awt.Dimension(300, 50));
        srpDirRelDet.setViewportView(txtDirRelDet);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherDet.add(srpDirRelDet, gridBagConstraints);

        srpRelativeDetails.setMaximumSize(new java.awt.Dimension(304, 54));
        srpRelativeDetails.setMinimumSize(new java.awt.Dimension(304, 54));

        txtRelativeDetails.setPreferredSize(new java.awt.Dimension(300, 50));
        srpRelativeDetails.setViewportView(txtRelativeDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherDet.add(srpRelativeDetails, gridBagConstraints);

        srpConnGrpDet.setMaximumSize(new java.awt.Dimension(304, 54));
        srpConnGrpDet.setMinimumSize(new java.awt.Dimension(304, 54));

        txtConnGrpDet.setPreferredSize(new java.awt.Dimension(300, 50));
        srpConnGrpDet.setViewportView(txtConnGrpDet);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherDet.add(srpConnGrpDet, gridBagConstraints);

        srpWelFund.setMaximumSize(new java.awt.Dimension(304, 54));
        srpWelFund.setMinimumSize(new java.awt.Dimension(304, 54));

        txtWelFund.setLineWrap(true);
        txtWelFund.setWrapStyleWord(true);
        txtWelFund.setMaximumSize(new java.awt.Dimension(300, 50));
        txtWelFund.setMinimumSize(new java.awt.Dimension(300, 50));
        txtWelFund.setPreferredSize(new java.awt.Dimension(300, 50));
        srpWelFund.setViewportView(txtWelFund);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherDet.add(srpWelFund, gridBagConstraints);

        srpRemarks.setMaximumSize(new java.awt.Dimension(304, 54));
        srpRemarks.setMinimumSize(new java.awt.Dimension(304, 54));

        txtRemarks.setPreferredSize(new java.awt.Dimension(300, 50));
        srpRemarks.setViewportView(txtRemarks);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherDet.add(srpRemarks, gridBagConstraints);

        txtResolutionNo.setMaximumSize(new java.awt.Dimension(300, 21));
        txtResolutionNo.setMinimumSize(new java.awt.Dimension(300, 21));
        txtResolutionNo.setPreferredSize(new java.awt.Dimension(300, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherDet.add(txtResolutionNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipady = -262;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 384);
        panOtherDetails.add(panOtherDet, gridBagConstraints);

        srpDrfTransaction.setMaximumSize(new java.awt.Dimension(350, 100));
        srpDrfTransaction.setMinimumSize(new java.awt.Dimension(350, 100));
        srpDrfTransaction.setPreferredSize(new java.awt.Dimension(350, 100));

        tblDrfTransaction.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Asset ID", "Face Value", "Current Value", "Sale Amount"
            }
        ));
        tblDrfTransaction.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblDrfTransaction.setMaximumSize(new java.awt.Dimension(350, 150));
        tblDrfTransaction.setMinimumSize(new java.awt.Dimension(350, 150));
        tblDrfTransaction.setOpaque(false);
        tblDrfTransaction.setPreferredScrollableViewportSize(new java.awt.Dimension(350, 150));
        tblDrfTransaction.setPreferredSize(new java.awt.Dimension(350, 150));
        srpDrfTransaction.setViewportView(tblDrfTransaction);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = -20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 167, 0, 0);
        panOtherDetails.add(srpDrfTransaction, gridBagConstraints);

        panMobileBanking.setBorder(javax.swing.BorderFactory.createTitledBorder("Mobile Banking"));
        panMobileBanking.setMinimumSize(new java.awt.Dimension(400, 80));
        panMobileBanking.setPreferredSize(new java.awt.Dimension(400, 80));
        panMobileBanking.setLayout(new java.awt.GridBagLayout());

        chkMobileBanking.setText("Required");
        chkMobileBanking.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkMobileBanking.setMinimumSize(new java.awt.Dimension(80, 21));
        chkMobileBanking.setPreferredSize(new java.awt.Dimension(80, 21));
        chkMobileBanking.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkMobileBankingActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panMobileBanking.add(chkMobileBanking, gridBagConstraints);

        lblMobileNo.setText("Mobile No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panMobileBanking.add(lblMobileNo, gridBagConstraints);

        txtMobileNo.setAllowAll(true);
        txtMobileNo.setMaxLength(16);
        txtMobileNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMobileNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMobileNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panMobileBanking.add(txtMobileNo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panMobileBanking.add(tdtMobileSubscribedFrom, gridBagConstraints);

        lblMobileSubscribedFrom.setText("Subscribed From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panMobileBanking.add(lblMobileSubscribedFrom, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        panOtherDetails.add(panMobileBanking, gridBagConstraints);

        tabShare.addTab("Other Details", panOtherDetails);

        panLoanDetails.setLayout(new java.awt.GridBagLayout());

        srpLoanAcctHolder.setMinimumSize(new java.awt.Dimension(610, 500));
        srpLoanAcctHolder.setOpaque(false);
        srpLoanAcctHolder.setPreferredSize(new java.awt.Dimension(610, 500));

        tblLoanAcctHolder.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cust Id", "Prod Desc", "Loan No", "Loan Limit Amt", "Loan Dt", "Present Out Standing"
            }
        ));
        tblLoanAcctHolder.setCellSelectionEnabled(true);
        tblLoanAcctHolder.setMinimumSize(new java.awt.Dimension(600, 2000));
        tblLoanAcctHolder.setOpaque(false);
        tblLoanAcctHolder.setPreferredScrollableViewportSize(new java.awt.Dimension(600, 1000));
        tblLoanAcctHolder.setPreferredSize(new java.awt.Dimension(600, 2000));
        srpLoanAcctHolder.setViewportView(tblLoanAcctHolder);
        tblLoanAcctHolder.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panLoanDetails.add(srpLoanAcctHolder, gridBagConstraints);

        lblBalToBePaid.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblBalToBePaid.setText("                      ");
        lblBalToBePaid.setMaximumSize(new java.awt.Dimension(200, 21));
        lblBalToBePaid.setMinimumSize(new java.awt.Dimension(200, 21));
        lblBalToBePaid.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panLoanDetails.add(lblBalToBePaid, gridBagConstraints);

        tabShare.addTab("Loan  Details", panLoanDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panShare.add(tabShare, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(panShare, gridBagConstraints);

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

        mitAuthorize.setText("Authorize");
        mitAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitAuthorizeActionPerformed(evt);
            }
        });
        mnuProcess.add(mitAuthorize);

        mitReject.setText("Reject");
        mitReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitRejectActionPerformed(evt);
            }
        });
        mnuProcess.add(mitReject);

        mitException.setText("Exception");
        mitException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitExceptionActionPerformed(evt);
            }
        });
        mnuProcess.add(mitException);
        mnuProcess.add(sptException);

        mitResolution.setText("Resolution");
        mitResolution.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitResolutionActionPerformed(evt);
            }
        });
        mnuProcess.add(mitResolution);
        mnuProcess.add(sptResolution);

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

    private void chkDrfApplicableYNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkDrfApplicableYNActionPerformed
        // TODO add your handling code here:
        if(chkDrfApplicableYN.isSelected()){
            cboDrfProdId.setVisible(true);
            lblDrfProdId.setVisible(true);
        } else {
            observable.setChkDrfApplicableYN("N");
            observable.setCboDrfProdId("");
            drfRecieptFlag = false;
            cboDrfProdId.setVisible(false);
            cboDrfProdId.setSelectedItem("");
            lblDrfProdId.setVisible(false);
            lblDRFAmt.setVisible(false);
            lblDrfAmtVal.setVisible(false);
            lblDrfAmtVal.setText("");
            drfApplicable = null;
            observable.setProductAmount("");
        }
//        if ((chkDrfApplicableYN.isSelected() == false) && (drfNomineeUi.getTblRowCount() > 0)) {//Added By  and commented By Revathi.L reff By Srinath sir 19/07/2017 In future if required then Enable the code.
//            //--- if Nominee details is unchecked, then display the warning message
//            //--- if YES, reset the Nominee details
//            int yesNo = 0;
//            String[] options = {"Yes", "No"};
//            yesNo = COptionPane.showOptionDialog(null, "This will reset the DRF Nominee Details data. Are you sure to reset it?", CommonConstants.WARNINGTITLE,
//                    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
//                    null, options, options[0]);
//            if (yesNo == 0) {
//                drfNomineeUi.resetTable();
//                drfNomineeUi.resetNomineeData();
//                drfNomineeUi.resetNomineeTab();
//                drfNomineeUi.disableNewButton(false);
//                tabShare.remove(drfNomineeUi);
//            } else {
//                chkDrfApplicableYN.setSelected(true);
//            }
//        }
    }//GEN-LAST:event_chkDrfApplicableYNActionPerformed

    private void cboDrfProdIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboDrfProdIdActionPerformed
        // TODO add your handling code here:
//        String prodID = CommonUtil.convertObjToStr(cboDrfProdId.getSelectedItem());
         String prodID = ((ComboBoxModel)cboDrfProdId.getModel()).getKeyForSelected().toString();
         String prod= cboDrfProdId.getSelectedItem().toString();
                
                if (prodID.length()>0) {
                    lblDRFAmt.setVisible(true);
                    lblDrfAmtVal.setVisible(true);
                    HashMap drfProdDetailsMap = new HashMap();
                    drfProdDetailsMap.put("PROD_ID",prodID);
                    drfProdDetailsMap.put("CURRENT_DATE",observable.getCurDate());
                    List getDrfProdDetails = ClientUtil.executeQuery("getDrfProductDetailsForTrans", drfProdDetailsMap);
                    if(getDrfProdDetails!= null && getDrfProdDetails.size() > 0) {
                        drfProdDetailsMap = (HashMap) getDrfProdDetails.get(0);
                        observable.setProductPaymentAmount(CommonUtil.convertObjToStr(drfProdDetailsMap.get("PAYMENT")));
                        observable.setProductAmount(CommonUtil.convertObjToStr(drfProdDetailsMap.get("AMOUNT")));
                        lblDrfAmtVal.setText(CommonUtil.convertObjToStr(drfProdDetailsMap.get("AMOUNT")));
                        
//                        calcTotalShareDetails();
            } else {
                ClientUtil.showMessageWindow("Effective Date of " + prod + " is lesser than Current Date");
                lblDrfAmtVal.setText("");
            }
            drfApplicable = CommonUtil.convertObjToStr(txtShareAcctNo.getText());
            drfRecieptFlag = true;
            HashMap drfMap = new HashMap();//Added By Revathi.L
            drfMap.put("PROD_ID", prodID);
            List drfLst = ClientUtil.executeQuery("CheckNomineeReqired", drfMap);
            if(drfLst!=null && drfLst.size()>0){
                drfMap = (HashMap)drfLst.get(0);
                if(CommonUtil.convertObjToStr(drfMap.get("NOMINEE_REQUIRED")).equals("Y")){
                    tabShare.add(drfNomineeUi,"DRF Nominee");
                    drfNomineeUi.disableNewButton(true);
                }else{
                    tabShare.remove(drfNomineeUi);
                }
            }
            
        } else {
            drfApplicable = "";
            drfRecieptFlag = false;
        }
    }//GEN-LAST:event_cboDrfProdIdActionPerformed
    
    private void btnJointAcctToMainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJointAcctToMainActionPerformed
        // TODO add your handling code here:
        updateOBFields();
        setBtnShareJoint(false);
        btnJointAcctNew.setEnabled(true);
        observable.moveToMain(CommonUtil.convertObjToStr(tblJointAcctHolder.getValueAt(0, 1)), CommonUtil.convertObjToStr(tblJointAcctHolder.getValueAt(tblJointAcctHolder.getSelectedRow(), 1)), tblJointAcctHolder.getSelectedRow());
        observable.ttNotifyObservers();
    }//GEN-LAST:event_btnJointAcctToMainActionPerformed
    
    private void btnJointAcctDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJointAcctDelActionPerformed
        // TODO add your handling code here:
        updateOBFields();
        setBtnShareJoint(false);
        btnJointAcctNew.setEnabled(true);
        observable.delJointAccntHolder(CommonUtil.convertObjToStr(tblJointAcctHolder.getValueAt(tblJointAcctHolder.getSelectedRow(), 1)), tblJointAcctHolder.getSelectedRow());
        observable.resetCustDetails();
        observable.ttNotifyObservers();
    }//GEN-LAST:event_btnJointAcctDelActionPerformed
    
    private void btnJointAcctNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJointAcctNewActionPerformed
        if(tblJointAcctHolder.getRowCount()!=0){ //--- If the Main Accnt Holder is selected,
            //            callView(JOINT_ACCOUNT);               //--- allow the user to add Jnt Acct Holder
            viewType = JOINT_ACCOUNT;
            new CheckCustomerIdUI(this);
        } else {  //--- else if the Main Acct Holder is not selected, prompt the user to select
            CommonMethods.showDialogOk(objCommRB.getString("selectMainAccntHolder")); //--- the Main Acct. holder
            btnCustomerIdFileOpen.requestFocus(true);
        }
        
    }//GEN-LAST:event_btnJointAcctNewActionPerformed
    
    private void tblJointAcctHolderMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblJointAcctHolderMousePressed
        // TODO add your handling code here:
        tblJointAcctHolderRowSelected(tblJointAcctHolder.getSelectedRow());
        //        }
        //--- To disable all the fields in Authorize status and sendToResol.
        if(viewType.equals(ClientConstants.VIEW_TYPE_DELETE) || viewType.equals(ClientConstants.VIEW_TYPE_AUTHORIZE) || viewType.equals(ClientConstants.VIEW_TYPE_SEND_TO_RESOLUTION)){
            setBtnShareJoint(false);
        }
        if(tblJointAcctHolder.getSelectedRowCount()>0 && evt.getClickCount() == 2){
            new CustomerDetailsScreenUI(CommonUtil.convertObjToStr(tblJointAcctHolder.getValueAt(tblJointAcctHolder.getSelectedRow(), 1))).show();
        }
        
    }//GEN-LAST:event_tblJointAcctHolderMousePressed
    
    private void txtApplicationNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtApplicationNoFocusLost
        // TODO add your handling code here:
        if (CommonUtil.convertObjToStr(txtApplicationNo.getText()).length() > 0 && CommonUtil.convertObjToStr(txtCustId.getText()).length() == 0) {
            
            txtShareApplNoFocusLost();
        }
    }//GEN-LAST:event_txtApplicationNoFocusLost
    
    private void tblShareAcctDetMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblShareAcctDetMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tblShareAcctDetMouseReleased
    
    private void tblShareAcctDetMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblShareAcctDetMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_tblShareAcctDetMouseExited
    
    private void rdoExistCustYesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoExistCustYesActionPerformed
        // TODO add your handling code here:
        txtShareAcctNo.setVisible(true);
        lblShareAcctNo.setVisible(true);
    }//GEN-LAST:event_rdoExistCustYesActionPerformed
    
    private void rdoExistCustNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoExistCustNoActionPerformed
        // TODO add your handling code here:
        txtShareAcctNo.setVisible(false);
        lblShareAcctNo.setVisible(false);
        if(rdoExistCustNo.isSelected() == true){
            txtShareAcctNo.setText("");
            txtCustId.setText("");
            lblValCustName.setText("");
            nomineeUi.setMainCustomerId(txtCustId.getText());
            drfNomineeUi.setMainCustomerId(txtCustId.getText());//Added By Revathi.L
            //            lblSecurityCustNameValue.setText("");
            //lblDocumentCustNameValue.setText("");
            lblShareAcctNo.setVisible(false);
            txtShareAcctNo.setVisible(false);
            //tblBorrowerTabCTable.revalidate();
            txtCustId.setEnabled(true);
            individualCustUI = new IndividualCustUI();
            com.see.truetransact.ui.TrueTransactMain.showScreen(individualCustUI);
            individualCustUI.loanCreationCustId(this);
            
        }
    }//GEN-LAST:event_rdoExistCustNoActionPerformed
    
    private void chkDuplicateIDCardYNMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_chkDuplicateIDCardYNMousePressed
        // TODO add your handling code here:

        if(CommonUtil.convertObjToStr(txtIDResolutionNo.getText()).length()>0 && observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
            int yesNo = 0;
            String[] options = {"Yes", "No"};
            yesNo = COptionPane.showOptionDialog(null,"The ID Resolution Number and Date Already Entered!!Do you want to change?", CommonConstants.WARNINGTITLE,
            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
            null, options, options[0]);
            if (yesNo==0) {
                duplicateIDVisible(true);
                txtIDResolutionNo.setEnabled(true);
                txtIDResolutionNo.setText("");
                tdtIDResolutionDt.setEnabled(true);
                tdtIDResolutionDt.setDateValue("");
            }
        }
    }//GEN-LAST:event_chkDuplicateIDCardYNMousePressed
    
    private void chkDuplicateIDCardYNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkDuplicateIDCardYNActionPerformed
        // TODO add your handling code here:
        if(chkDuplicateIDCardYN.isSelected()){
            duplicateIDVisible(true);
        }else{
            duplicateIDVisible(false);
        }
    }//GEN-LAST:event_chkDuplicateIDCardYNActionPerformed
        
    private void txtCustIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCustIdFocusLost
        // TODO add your handling code here:
        HashMap hash = new HashMap();
        if(viewType==ClientConstants.VIEW_TYPE_EDIT){
            if (CommonUtil.convertObjToStr(txtCustId.getText()).length() > 0 && CommonUtil.convertObjToStr(txtShareAcctNo.getText()).length() == 0) {
                txtShareAcctNoFocusLost(hash);
        }
        } else if (viewType == ClientConstants.VIEW_TYPE_NEW || viewType == CUSTOMER_ID) {
            
            viewType=CUSTOMER_ID;
            hash.put(CUSTOMER_ID, txtCustId.getText());
            fillData(hash);
        }
    }//GEN-LAST:event_txtCustIdFocusLost
    
    private void txtNoSharesFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNoSharesFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoSharesFocusGained
    
    private void txtShareApplFeeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtShareApplFeeFocusLost
        // TODO add your handling code here:.
        if(rdoSharewithDrawal.isSelected()==false){
            calcTotalShareDetails();
        }
        
        //if(rdoShareAddition.isSelected() && TrueTransactMain.SERVICE_TAX_REQ .equals("Y")){
        if(TrueTransactMain.SERVICE_TAX_REQ .equals("Y")){
            serviceTaxCalculation();
        }
    }//GEN-LAST:event_txtShareApplFeeFocusLost
    
    private void txtCustIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCustIdActionPerformed
        // TODO add your handling code here:
        //         HashMap hash = new HashMap();
        //         viewType=CUSTOMER_ID;
        //        hash.put(CUSTOMER_ID, txtCustId.getText());
        //        fillData(hash);
    }//GEN-LAST:event_txtCustIdActionPerformed
    
    private HashMap txtShareAcctNoFocusLost(HashMap map){
        HashMap whereMap =new HashMap();
        if(CommonUtil.convertObjToStr(txtShareAcctNo.getText()).length()>0 ||(isCustomerEditmode && map!=null && map.size()>0)){
            String shareNo="";
            if(map !=null && map.containsKey("MEMBER_NO")){
                whereMap.put("SHARE_ACCT_NO",map.get("MEMBER_NO"));
                shareNo=CommonUtil.convertObjToStr(map.get("MEMBER_NO"));
            } else {
                whereMap.put("SHARE_ACCT_NO", CommonUtil.convertObjToStr(txtShareAcctNo.getText()));
                shareNo=CommonUtil.convertObjToStr(txtShareAcctNo.getText());
            }
            if(!observable.checkAcNoWithoutProdType(shareNo,null,true)){
                ClientUtil.displayAlert("Please Enter Correct Share No");
                return null;
            }
        } else if (CommonUtil.convertObjToStr(txtCustId.getText()).length() > 0) {
            whereMap.put("CUST_ID", CommonUtil.convertObjToStr(txtCustId.getText()));
            if(!observable.checkAcNoWithoutProdType(null,CommonUtil.convertObjToStr(txtCustId.getText()),true)){
                ClientUtil.displayAlert("Please Enter Correct Customer Id");
                return null;
            }
        }
        
        whereMap.put("SELECTED_BRANCH_ID", observable.getSelectedBranchID());
        List list = ClientUtil.executeQuery("viewAllShareAcct", whereMap);
        if (list != null && list.size() > 0) {
            whereMap = (HashMap) list.get(0);
            if (isCustomerEditmode) {
                isCustomerEditmode = false;
                return whereMap;
            }
            fillData(whereMap);
        }
        populateFieldsEditMode();
        return null;
    }
    
    private void txtShareApplNoFocusLost() {
        HashMap whereMap=new HashMap();
        String ii=txtApplicationNo.getText().toString();
        if(CommonUtil.convertObjToStr(txtApplicationNo.getText()).length()>0){
            whereMap.put("SHARE_APPL_NO", CommonUtil.convertObjToStr(txtApplicationNo.getText()));
            viewType="EDIT";
        } else if (CommonUtil.convertObjToStr(txtCustId.getText()).length() > 0) {
            whereMap.put("CUST_ID", CommonUtil.convertObjToStr(txtCustId.getText()));
            if(!observable.checkAcNoWithoutProdType(null,CommonUtil.convertObjToStr(txtCustId.getText()),true)){
                ClientUtil.displayAlert("Please Enter Correct Customer Id");
                return;
            }
        }
        whereMap.put("SELECTED_BRANCH_ID",TrueTransactMain.BRANCH_ID);
        whereMap.put("AUTHORIZESTATUS","AUTHORIZESTATUS");
        List list = ClientUtil.executeQuery("viewAllShareApplNo", whereMap);
        if(list !=null && list.size()>0){
            whereMap=(HashMap)list.get(0);
            fillData(whereMap);
            
        }
        populateFieldsEditMode();
        rdoShareAddition.setEnabled(false);
        rdoSharewithDrawal.setEnabled(false);
        rdoShareDifferentialAmount.setEnabled(false);
        
        tblShareAcctDet.setEnabled(false);
        
//        transactionUI.se
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        transactionUI.setButtonEnableDisable(false);
        btnShareAcctDetNew.setEnabled(false);
    }
        
    private void txtDivAcNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDivAcNoActionPerformed
        // TODO add your handling code here:        
        HashMap hash = new HashMap();
        String ACCOUNTNO = (String) txtDivAcNo.getText();
        observable.setCboDivProdType("");
        if ((!(observable.getCboDivProdType().length()>0)) && ACCOUNTNO.length()>0) {
            if (observable.checkAcNoWithoutProdType(ACCOUNTNO,null,false)) {
                txtDivAcNo.setText(observable.getTxtDivAcNo());
                ACCOUNTNO = (String) txtDivAcNo.getText();
                cboMemDivProdID.setModel(observable.getCbmDivProdID());
                cboMemDivProdIDActionPerformed(null);
                //                txtDivAcNo.setText(ACCOUNTNO);
                String prodType = ((ComboBoxModel)cboMemDivProdType.getModel()).getKeyForSelected().toString();
                
                
            } else {
                ClientUtil.showAlertWindow("Invalid Account No.");
                txtDivAcNo.setText("");
                return;
            }
        }
        if( observable.getCboDivProdType().equals("TD")){
            if (ACCOUNTNO.lastIndexOf("_")!=-1){
                hash.put("ACCOUNTNO", txtDivAcNo.getText());
            } else {
                hash.put("ACCOUNTNO", txtDivAcNo.getText()+"_1");
            }
        }else{
            hash.put("ACCOUNTNO", txtDivAcNo.getText());
        }
        hash.put("ACT_NUM", hash.get("ACCOUNTNO"));
        hash.put("PROD_ID", ((ComboBoxModel) cboMemDivProdID.getModel()).getKeyForSelected());
        hash.put("SELECTED_BRANCH",TrueTransactMain.BRANCH_ID);
        List actlst=null;
        //        List lst=null;
        HashMap notClosedMap = new HashMap();
        
        
        
        
        if (observable.getCboDivProdType().equals("OA")) {
            observable.setTxtDivAcNo(ACCOUNTNO);
        }
        
        //        if(observable.getCboDivProdType().equals("TD") || observable.getCboDivProdType().equals("TL")){
        //            if(debit || credit){
        //                if(observable.getProdType().equals("TL")){
        //                    if(actlst!=null && actlst.size()>0){
        //                        viewType = ACCNO;
        //                        updateOBFields();
        //                        hash.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
        //                        hash.put("SELECTED_BRANCH",ProxyParameters.BRANCH_ID);
        //                        if( observable.getProdType().equals("TL")) {
        //                            if(debit) {
        //                                hash.put("PAYMENT","PAYMENT");
        ////                                lst=ClientUtil.executeQuery("Cash.getAccountList"
        ////                                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(),hash);
        //                            }else if(credit){
        //                                if(observable.getProdType().equals("TL"))
        //                                    hash.put("RECEIPT","RECEIPT");
        //                                System.out.println("hash"+hash);
        ////                                lst=ClientUtil.executeQuery("Cash.getAccountList"
        ////                                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(),hash);
        //                            }
        //                            fillData(hash);
        //                        }
        //                    }else{
        //                        ClientUtil.showAlertWindow(" Invalid Number Choose correct number");
        //                        txtAccNo.setText("");
        //                        //                        txtAccNo.requestFocus();
        //                    }
        //                }else if(observable.getProdType().equals("TD")){
        //                    viewType = ACCNO;
        //                    updateOBFields();
        //                    hash.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
        //                    hash.put("SELECTED_BRANCH",ProxyParameters.BRANCH_ID);
        //                    if(actlst!=null && actlst.size()>0){
        //                        if(observable.getProdType().equals("TD")){
        //                            hash.put("RECEIPT","RECEIPT");
        //                            if(debit) {
        //				rdoTransactionType_Debit.setSelected(true);
        //                                //                                if(observable.getProdType().equals("TD")){
        //                                //                                    hash.put("PAYMENT","PAYMENT");
        //                                //                                    lst=ClientUtil.executeQuery("Cash.getAccountList"
        //                                //                                    + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(),hash);
        //                                //                                }else{
        ////                                lst=ClientUtil.executeQuery("getDepositHoldersInterest",hash);
        //                                transDetails.setIsDebitSelect(true);
        //                            }else if(credit){
        //                                rdoTransactionType_Credit.setSelected(true);
        ////                                lst=ClientUtil.executeQuery("Cash.getAccountList"
        ////                                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(),hash);
        //                            }
        //                            hash.put("PRODUCTTYPE",notClosedMap.get("BEHAVES_LIKE"));
        //                            hash.put("TYPE",notClosedMap.get("BEHAVES_LIKE"));
        //                            hash.put("AMOUNT",notClosedMap.get("DEPOSIT_AMT"));
        //                            fillData(hash);
        //                            if(debit) {
        //				rdoTransactionType_Debit.setSelected(true);
        //                            }else if(credit) {
        //                                rdoTransactionType_Credit.setSelected(true);
        //                            }
        //                        }
        //                    }else if(actlst.isEmpty() && credit == true){
        //                        ClientUtil.showAlertWindow(" Already Transaction Completed...");
        //                        rdoTransactionType_Credit.setSelected(true);
        //                        txtAccNo.setText("");
        //                        txtAmount.setText("");
        //                        return;
        //                    }else{
        //                        ClientUtil.showAlertWindow(" Invalid Number Choose correct number");
        //                        txtAccNo.setText("");
        //                    }
        //                }
        //            }else{
        //                ClientUtil.showMessageWindow("Select Payment or Receipt ");
        //                txtAccNo.setText("");
        //                return;
        //            }
        //        }else if(observable.getCboDivProdType().equals("OA")){
        //            viewType = ACCNO;
        //            HashMap listMap = new HashMap();
        //            if(observable.getLblAccName().length()>0){
        //                updateOBFields();
        //                hash.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
        //                hash.put("SELECTED_BRANCH",ProxyParameters.BRANCH_ID);
        ////                lst=ClientUtil.executeQuery("Cash.getAccountList"
        ////                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(),hash);
        //                fillData(hash);
        //                observable.setLblAccName("");
        //            }else{
        //                ClientUtil.showAlertWindow(" Invalid Number Choose correct number");
        //                txtAccNo.setText("");
        //            }
        //        }
        
        
        viewType = CUSTOMER_ID;
        HashMap listMap = new HashMap();
        
        //                updateOBFields();
        cboMemDivPayMode.setSelectedItem("Transfer");
        if (CommonUtil.convertObjToStr(observable.getTxtCustId()).length() > 0) {
            hash.put(CUSTOMER_ID, CommonUtil.convertObjToStr(observable.getTxtCustId()));
        }
        fillData(hash);
        
        
        
        
    }//GEN-LAST:event_txtDivAcNoActionPerformed
    
    private void cboMemDivProdIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboMemDivProdIDFocusLost
        // TODO add your handling code here:
        //        if(CommonUtil.convertObjToStr(cboMemDivProdType.getSelectedItem()).equals("Remittance")){
        //            btnDivAcNoFileOpen.setEnabled(false);
        //            lblMemDivAcNo.setName("Favouring Name");
        //        }
        //        else{
        //            lblMemDivAcNo.setName("Dividend Account No");
        //            btnDivAcNoFileOpen.setEnabled(false);
        //        }
    }//GEN-LAST:event_cboMemDivProdIDFocusLost
    private void calcTotalShareDetails(){
          double drfTransAmt = 0.0; 
        double shareApplFees = CommonUtil.convertObjToDouble(txtShareApplFee.getText()).doubleValue();
        double shareMemFees = CommonUtil.convertObjToDouble(txtShareMemFee.getText()).doubleValue();
        double totalShareFees = CommonUtil.convertObjToDouble(txtTotShareFee.getText()).doubleValue();
        double totalShareValue = CommonUtil.convertObjToDouble(txtShareValue.getText()).doubleValue();
        double totalShareAmount = totalShareValue+totalShareFees+shareMemFees+shareApplFees+CommonUtil.convertObjToDouble(observable.getLblServiceTaxval());
        observable.setTxtShareTotAmount(CommonUtil.convertObjToStr(totalShareAmount));
        drfTransAmt=CommonUtil.convertObjToDouble(lblDrfAmtVal.getText());
        txtShareTotAmount.setText(String.valueOf(totalShareAmount));
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        transactionUI.setChitType("");
        transactionUI.setSchemeName("");
//        added by Nikhil for DRF Applicable 
		if( observable.getActionType()==ClientConstants.ACTIONTYPE_NEW){
        if(drfRecieptFlag && drfApplicable.length()>0){ 
            if(observable.getProductAmount()!= null){
            drfTransAmt = CommonUtil.convertObjToDouble(observable.getProductAmount()).doubleValue();
            }
        }
    }
        //        added by Nikhil for subsidy
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW
                && (CommonUtil.convertObjToStr(observable.getCaste()).equals("SC") || CommonUtil.convertObjToStr(observable.getCaste()).equals("ST")) && subsidyFlag) {
            double transAmtAftSubsidy = CommonUtil.convertObjToDouble(observable.getTxtShareTotAmount()).doubleValue()
                    - CommonUtil.convertObjToDouble(observable.getGovtsShare()).doubleValue() + drfTransAmt;
            transactionUI.setCallingAmount(String.valueOf(transAmtAftSubsidy));
            subsidyGiven = true;
            
        }else{
            //            transactionUI.setCallingAmount(observable.getTxtShareTotAmount());
            double shareTotAmt = CommonUtil.convertObjToDouble(observable.getTxtShareTotAmount()).doubleValue() + drfTransAmt; 
            //            transactionUI.setCallingAmount(observable.getTxtShareTotAmount());
            transactionUI.setCallingAmount(String.valueOf(shareTotAmt));
             txtShareTotAmount.setText(String.valueOf(shareTotAmt));
            subsidyGiven = true;
        }
    }
    
    private void calcTotalShareAmount(){
        double shareApplFees = CommonUtil.convertObjToDouble(txtShareApplFee.getText()).doubleValue();
        double shareMemFees = CommonUtil.convertObjToDouble(txtShareMemFee.getText()).doubleValue();
        double totalShareFees = CommonUtil.convertObjToDouble(txtTotShareFee.getText()).doubleValue();
        double totalShareValue = CommonUtil.convertObjToDouble(txtShareValue.getText()).doubleValue();
        double totalShareAmount = totalShareValue+totalShareFees+shareMemFees+shareApplFees;
        if( tblShareAcctDet.getRowCount()>0){
            if(chkDrfApplicableYN.isSelected()==true && cboDrfProdId.getSelectedItem()!=null && tblShareAcctDet.getSelectedRow()==0){
                totalShareAmount=totalShareAmount+CommonUtil.convertObjToDouble(observable.getProductAmount()).doubleValue();
        txtShareTotAmount.setText(String.valueOf(totalShareAmount));
            }else{
                txtShareTotAmount.setText(String.valueOf(totalShareAmount));
            }
        }else{
            txtShareTotAmount.setText(String.valueOf(totalShareAmount));
        }
    }
  
    private void getHeadsForService() {
        HashMap whereMap = new HashMap();
        whereMap.put("SHARE_TYPE", CommonUtil.convertObjToStr(observable.getCbmShareType().getKeyForSelected()));
        List list = ClientUtil.executeQuery("getFeeData", whereMap);
        if (list != null && list.size() > 0) {
            HashMap res = (HashMap) list.get(0);
            if (res != null && res.containsKey("SHARE_FEE_ACHD")) {
                whereMap = new HashMap();
                whereMap.put("AC_HD_ID", res.get("SHARE_FEE_ACHD"));
                List temp = ClientUtil.executeQuery("getCheckServiceTaxApplicableForShare", whereMap);
                if (temp != null && temp.size() > 0) {
                    HashMap value = (HashMap) temp.get(0);
                    if (value != null && value.containsKey("SERVICE_TAX_APPLICABLE") && value.containsKey("SERVICE_TAX_ID")) {
                        shareFeeTax = CommonUtil.convertObjToStr(value.get("SERVICE_TAX_APPLICABLE"));
                        shareFeeTaxSettingId = CommonUtil.convertObjToStr(value.get("SERVICE_TAX_ID"));
                    }
                }
            }
            if (res != null && res.containsKey("MEMBERSHIP_FEE_ACHD")) {
                whereMap = new HashMap();
                whereMap.put("AC_HD_ID", res.get("MEMBERSHIP_FEE_ACHD"));
                List temp = ClientUtil.executeQuery("getCheckServiceTaxApplicableForShare", whereMap);
                if (temp != null && temp.size() > 0) {
                    HashMap value = (HashMap) temp.get(0);
                    if (value != null && value.containsKey("SERVICE_TAX_APPLICABLE")) {
                        memFeeTax = CommonUtil.convertObjToStr(value.get("SERVICE_TAX_APPLICABLE"));
                        memFeeTaxSettingId = CommonUtil.convertObjToStr(value.get("SERVICE_TAX_ID"));
                    }
                }
            }
            if (res != null && res.containsKey("APPLICATION_FEE_ACHD")) {
                whereMap = new HashMap();
                whereMap.put("AC_HD_ID", res.get("APPLICATION_FEE_ACHD"));
                List temp = ClientUtil.executeQuery("getCheckServiceTaxApplicableForShare", whereMap);
                if (temp != null && temp.size() > 0) {
                    HashMap value = (HashMap) temp.get(0);
                    if (value != null && value.containsKey("SERVICE_TAX_APPLICABLE")) {
                        applFeeTax = CommonUtil.convertObjToStr(value.get("SERVICE_TAX_APPLICABLE"));
                        applFeeTaxSettingId = CommonUtil.convertObjToStr(value.get("SERVICE_TAX_ID"));
                    }
                }
            }
        }
    }
    
    private void serviceTaxCalculation() {
        double shareApplFee = 0.0;
        double ShareMemFee = 0.0;
        HashMap taxMap;
        double totShreFee = 0.0;
        double totAmt = 0.0;
        double taxAmt = 0.0;
        double totShareValue = 0.0;
        //totShreFee = CommonUtil.convertObjToDouble(txtNoShares.getText()).doubleValue()
          //      * CommonUtil.convertObjToDouble(txtShareFee.getText()).doubleValue();
        totShreFee = CommonUtil.convertObjToDouble(txtTotShareFee.getText());
        shareApplFee = CommonUtil.convertObjToDouble(txtShareApplFee.getText());
        ShareMemFee = CommonUtil.convertObjToDouble(txtShareMemFee.getText());
        totShareValue = CommonUtil.convertObjToDouble(txtNoShares.getText()).doubleValue()
                * CommonUtil.convertObjToDouble(txtShareAmt.getText()).doubleValue();
        totAmt = totShareValue + totShreFee + shareApplFee + ShareMemFee;
        List taxSettingsList = new ArrayList();
        if (memFeeTax != null && memFeeTax.equals("Y") && memFeeTaxSettingId.length() > 0) {
            if (ShareMemFee > 0) {
                taxMap = new HashMap();
                taxMap.put("SETTINGS_ID", memFeeTaxSettingId);
                taxMap.put(ServiceTaxCalculation.TOT_AMOUNT, ShareMemFee);
                taxSettingsList.add(taxMap);
            }
            taxAmt = taxAmt + ShareMemFee;
        }
        if (shareFeeTax != null && shareFeeTax.equals("Y") && shareFeeTaxSettingId.length() > 0) {
            if (totShreFee > 0) {
                taxMap = new HashMap();
                taxMap.put("SETTINGS_ID", shareFeeTaxSettingId);
                taxMap.put(ServiceTaxCalculation.TOT_AMOUNT, totShreFee);
                taxSettingsList.add(taxMap);
            }
            taxAmt = taxAmt + totShreFee;
        }
        if (applFeeTax != null && applFeeTax.equals("Y") && applFeeTaxSettingId.length() > 0) {
            if (shareApplFee > 0) {
                taxMap = new HashMap();
                taxMap.put("SETTINGS_ID", applFeeTaxSettingId);
                taxMap.put(ServiceTaxCalculation.TOT_AMOUNT, shareApplFee);
                taxSettingsList.add(taxMap);
            }
            taxAmt = taxAmt + shareApplFee;
        }
        //if (taxAmt > 0) {
        if (taxSettingsList != null && taxSettingsList.size() > 0) {
            HashMap ser_Tax_Val = new HashMap();
            ser_Tax_Val.put(ServiceTaxCalculation.CURR_DT, currDt.clone());
            ser_Tax_Val.put(ServiceTaxCalculation.TOT_AMOUNT, CurrencyValidation.formatCrore(String.valueOf(taxAmt)));
            ser_Tax_Val.put("SERVICE_TAX_DATA", taxSettingsList);
            try {
                objServiceTax = new ServiceTaxCalculation();
                serviceTax_Map = objServiceTax.calculateServiceTax(ser_Tax_Val);
                if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.TOT_TAX_AMT)) {
                    String amt = CommonUtil.convertObjToStr(serviceTax_Map.get(ServiceTaxCalculation.TOT_TAX_AMT));
//                            lblServiceTaxval.setText(roundOffAmt(amt, "NEAREST_VALUE"));
//                            observable.setLblServiceTaxval(lblServiceTaxval.getText());
//                            serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, roundOffAmt(amt, "NEAREST_VALUE"));
                    lblServiceTaxval.setText(amt);
                    observable.setLblServiceTaxval(lblServiceTaxval.getText());
                    serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, amt);
                } else {
                    lblServiceTaxval.setText("0.00");
                    observable.setLblServiceTaxval(lblServiceTaxval.getText());
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                java.util.logging.Logger.getLogger(LoanApplicationUI.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            lblServiceTaxval.setText("0.00");
            observable.setLblServiceTaxval(lblServiceTaxval.getText());
        }
        double totshareAmt = CommonUtil.convertObjToDouble(observable.getLblServiceTaxval())
                + totAmt;
        observable.setTxtShareTotAmount(CommonUtil.convertObjToStr(totshareAmt));
        if (chkDrfApplicableYN.isSelected() == true && cboDrfProdId.getSelectedItem() != null && drfExist == false) {
            observable.setTxtShareTotAmount(CommonUtil.convertObjToStr(new Double(totshareAmt + CommonUtil.convertObjToDouble(observable.getProductAmount()).doubleValue())));
        }
        txtShareTotAmount.setText(observable.getTxtShareTotAmount());
    }
    
    
    private void txtNoSharesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNoSharesFocusLost
        // TODO add your handling code here:
        int Existiongshare=0;
        int newshare=0;
        double totShareValue=0.0;
       double totsharewithoutmeb=0;
        double totShreFee=0.0;
        double totAmt=0.0;
        double shareApplFee=0.0;
        double ShareMemFee=0.0;
        //        added by nikhil
        double subsidyAmt = 0.0;
        double taxAmt = 0;
        List taxSettingsList = new ArrayList();
        HashMap taxMap;
      
        int noshares= CommonUtil.convertObjToInt(txtNoShares.getText());
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            if (noshares > maxShare) {
                ClientUtil.showAlertWindow(" No Of Shares  should be equal to or lessthan than the maximum  shares" + "-" + maxShare);
                txtNoShares.setText("");
                txtShareValue.setText("");
                txtShareTotAmount.setText("");
                txtNoShares.setText("");
                txtShareApplFee.setText("");
                txtTotShareFee.setText("");
                return;
        }
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            HashMap hmap=new HashMap();//txtShareAcctNo
            hmap.put("SHARE ACCOUNT NO",txtShareAcctNo.getText());
            
        List list=ClientUtil.executeQuery("getExistingShare",hmap);
        hmap=null; 
        
            if( list != null && list.size()>0){
            String newField=observable.getNewMode();
            hmap=(HashMap)list.get(0);
          
          Existiongshare=CommonUtil.convertObjToInt(hmap.get("NO_OF_SHARES")); 
      
        }
           newshare=Existiongshare+noshares; 
            if(rdoShareAddition.isSelected()==true){
                if (newshare > maxShare) {
            ClientUtil.showAlertWindow(" No Of Shares + Existing Share  should be equal to or lessthan than the maximum  shares"+"-"+maxShare);
            txtNoShares.setText("");
            return;
        }
            } else if (rdoSharewithDrawal.isSelected() == true) {
                if (noshares > Existiongshare) {
                  ClientUtil.showAlertWindow(" No Of Shares  should be equal to or lessthan than the Existingshare  shares"+"-"+Existiongshare);
            txtNoShares.setText("");
            return;
             }
            }
            
        }
        if(noshares<minShares){
            ClientUtil.showAlertWindow(" No Of Shares  should be equal to or greater than the minimum initial shares"+"-"+minShares);
            txtNoShares.setText("");
            return;
        }else{
            observable.setTxtNoShares(txtNoShares.getText());
            if(CommonUtil.convertObjToDouble(txtNoShares.getText()).doubleValue()>0){
                observable.setTxtNoShares(txtNoShares.getText());
                //            added by nikhil for subsidy
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW
                        && (CommonUtil.convertObjToStr(observable.getCaste()).equals("SC") || CommonUtil.convertObjToStr(observable.getCaste()).equals("ST")) && subsidyFlag) {
                    //                totShareValue = CommonUtil.convertObjToDouble(txtNoShares.getText()).doubleValue()* customersShare;
                    totShareValue = CommonUtil.convertObjToDouble(txtNoShares.getText()).doubleValue()
                            * CommonUtil.convertObjToDouble(txtShareAmt.getText()).doubleValue();
                    subsidyAmt = CommonUtil.convertObjToDouble(txtNoShares.getText()).doubleValue()* govtsShare;
                    observable.setGovtsShare(String.valueOf(subsidyAmt));
                    subsidyGiven = true;
                }else{
                    totShareValue = CommonUtil.convertObjToDouble(txtNoShares.getText()).doubleValue()
                            * CommonUtil.convertObjToDouble(txtShareAmt.getText()).doubleValue();
                    subsidyGiven = false;
                }
                observable.setTxtShareValue(CommonUtil.convertObjToStr(new Double(totShareValue)));
                totShreFee = CommonUtil.convertObjToDouble(txtNoShares.getText()).doubleValue()
                        * CommonUtil.convertObjToDouble(txtShareFee.getText()).doubleValue();
                
                
                  //getShareFeeCalculationType
                String shareType = CommonUtil.convertObjToStr(((ComboBoxModel)(cboShareType.getModel())).getKeyForSelected());
                HashMap hmap = new HashMap();
                hmap.put("SHARE_TYPE", shareType);
                List list = ClientUtil.executeQuery("getShareFeeCalculationType", hmap);
                if(list != null && list.size() > 0){
                    HashMap shareFeeMap = (HashMap)list.get(0);
                    if(shareFeeMap.containsKey("SHARE_FEE_CONSTANT") && shareFeeMap.get("SHARE_FEE_CONSTANT") != null){
                        String shareFeeCalcType = CommonUtil.convertObjToStr(shareFeeMap.get("SHARE_FEE_CONSTANT"));
                        if(shareFeeCalcType.equals("Y")){
                           totShreFee =  CommonUtil.convertObjToDouble(txtShareFee.getText()).doubleValue();
                           if(rdoShareAddition.isSelected()){
                               HashMap edMap = new HashMap();
                               edMap.put("SHARE_NO", CommonUtil.convertObjToStr(txtShareAcctNo.getText()));
                               List lst = ClientUtil.executeQuery("getSumOfShareFee", edMap);
                               if(lst != null && lst.size() > 0){
                                   HashMap shareFeePaidMap = (HashMap)lst.get(0);
                                   if(shareFeePaidMap != null && shareFeePaidMap.containsKey("TOTAL_SHARE_FEE") && shareFeePaidMap.get("TOTAL_SHARE_FEE") != null){
                                       double paidAmt = CommonUtil.convertObjToDouble(shareFeePaidMap.get("TOTAL_SHARE_FEE"));
                                       if(totShreFee > paidAmt){
                                           totShreFee = totShreFee - paidAmt;
                                       }else{
                                           totShreFee = 0.0;
                                       }
                                   }
                               }
                           }
                        }
                    }
                }
                
                
                observable.setTxtTotShareFee(CommonUtil.convertObjToStr(new Double(totShreFee)));
                observable.setTxtShareApplFee(txtApplFee.getText());
                calcShareMemValue(totShareValue);
                //            observable.setTxtShareMemFee(txtMemFee.getText());
                
                
                if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                    HashMap edMap = new HashMap();
                    edMap.put("SHARE_NO",CommonUtil.convertObjToStr(txtShareAcctNo.getText()));
                    List lst=ClientUtil.executeQuery("alreadyShareTakenList",edMap);
                    if(lst!=null && lst.size() >0){
                        int cnt = Integer.parseInt(CommonUtil.convertObjToStr(lst.get(0)));
                        if (cnt>0) {
                            observable.setTxtShareApplFee("0");
                            //                        observable.setTxtShareMemFee("0");
                        }
                    }
                }
                shareApplFee=CommonUtil.convertObjToDouble(observable.getTxtShareApplFee()).doubleValue();
                ShareMemFee= CommonUtil.convertObjToDouble(observable.getTxtShareMemFee()).doubleValue();
                totAmt=totShareValue+totShreFee+shareApplFee+ShareMemFee;
                 totsharewithoutmeb=totShareValue+totShreFee+shareApplFee;
                if(rdoSharewithDrawal.isSelected()==true){
                    observable.setTxtTotShareFee(CommonUtil.convertObjToStr(new Double(0.0)));
                    totShreFee=0.0;
                    shareApplFee=0.0;
                    ShareMemFee=0.0;
                }
                totsharewithoutmeb=totShareValue+totShreFee+shareApplFee;
                totAmt=totShareValue+totShreFee+shareApplFee+ShareMemFee;
                if(chkDrfApplicableYN.isSelected()==true && cboDrfProdId.getSelectedItem()!=null && (observable.getActionType()==ClientConstants.ACTIONTYPE_NEW || observable.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE)){
                    observable.setTxtShareTotAmount(CommonUtil.convertObjToStr(new Double(totAmt+CommonUtil.convertObjToDouble(observable.getProductAmount()).doubleValue())));
                }else{
                    if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT && rdoShareAddition.isSelected() && chkDrfApplicableYN.isSelected()==true && cboDrfProdId.getSelectedItem()!=null && drfExist==false){//if condition changed By Revathi.L
                          observable.setTxtShareTotAmount(CommonUtil.convertObjToStr(new Double(totAmt+CommonUtil.convertObjToDouble(observable.getProductAmount()).doubleValue())));
                    }else{
                observable.setTxtShareTotAmount(CommonUtil.convertObjToStr(new Double(totAmt)));
                    }
                }
                 //Added by chithra For service Tax
                
                if (memFeeTax != null && memFeeTax.equals("Y")&& memFeeTaxSettingId.length() > 0) {
                    if(ShareMemFee > 0){
                        taxMap = new HashMap();
                        taxMap.put("SETTINGS_ID", memFeeTaxSettingId);
                        taxMap.put(ServiceTaxCalculation.TOT_AMOUNT, ShareMemFee);
                        taxSettingsList.add(taxMap);
                    }
                    taxAmt = taxAmt + ShareMemFee;
                }
                if (shareFeeTax != null && shareFeeTax.equals("Y") && shareFeeTaxSettingId.length() > 0) {
                    if(totShreFee > 0){
                        taxMap = new HashMap();
                        taxMap.put("SETTINGS_ID", shareFeeTaxSettingId);
                        taxMap.put(ServiceTaxCalculation.TOT_AMOUNT, totShreFee);
                        taxSettingsList.add(taxMap);
                    }
                    taxAmt = taxAmt + totShreFee;
                }
                if (applFeeTax != null && applFeeTax.equals("Y") && applFeeTaxSettingId.length() > 0) {
                    if(shareApplFee > 0){
                        taxMap = new HashMap();
                        taxMap.put("SETTINGS_ID", applFeeTaxSettingId);
                        taxMap.put(ServiceTaxCalculation.TOT_AMOUNT, shareApplFee);
                        taxSettingsList.add(taxMap);
                    }
                    taxAmt = taxAmt + shareApplFee;
                }
                //if (taxAmt > 0) {
                if(taxSettingsList != null && taxSettingsList.size() > 0){
                    HashMap ser_Tax_Val = new HashMap();
                    ser_Tax_Val.put(ServiceTaxCalculation.CURR_DT, currDt.clone());
                    ser_Tax_Val.put(ServiceTaxCalculation.TOT_AMOUNT, CurrencyValidation.formatCrore(String.valueOf(taxAmt)));
                    ser_Tax_Val.put("SERVICE_TAX_DATA", taxSettingsList);
                    try {
                        objServiceTax = new ServiceTaxCalculation();
                        serviceTax_Map = objServiceTax.calculateServiceTax(ser_Tax_Val);
                        if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.TOT_TAX_AMT)) {
                            String amt = CommonUtil.convertObjToStr(serviceTax_Map.get(ServiceTaxCalculation.TOT_TAX_AMT));
//                            lblServiceTaxval.setText(roundOffAmt(amt, "NEAREST_VALUE"));
//                            observable.setLblServiceTaxval(lblServiceTaxval.getText());
//                            serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, roundOffAmt(amt, "NEAREST_VALUE"));
                            lblServiceTaxval.setText(amt);
                            observable.setLblServiceTaxval(lblServiceTaxval.getText());
                            serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, amt);
                        } else {
                            lblServiceTaxval.setText("0.00");
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        java.util.logging.Logger.getLogger(LoanApplicationUI.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else {
                    lblServiceTaxval.setText("0.00");
                }
                double totshareAmt = CommonUtil.convertObjToDouble(observable.getLblServiceTaxval())
                        + CommonUtil.convertObjToDouble(observable.getTxtShareTotAmount());
                observable.setTxtShareTotAmount(CommonUtil.convertObjToStr(totshareAmt));
                 //end service Tax
            }else{
                ClientUtil.displayAlert("Please Enter The No of Shares");
                observable.setTxtTotShareFee("");
                observable.setTxtShareApplFee("");
                observable.setTxtShareMemFee("");
                observable.setTxtShareValue("");
                observable.setTxtShareTotAmount("");
            }
            txtShareValue.setEnabled(false);
            txtShareTotAmount.setEnabled(false);
            
            //        if(rdoSharewithDrawal.isSelected()==true && observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT
            //        && CommonUtil.convertObjToInt(txtNoShares.getText())>CommonUtil.convertObjToInt(lblTotNoOfSharesCount)){
            //           ClientUtil.displayAlert("No of Shares Less than no of withdrawl");
            //            txtNoShares.setText("");
            //            observable.setTxtTotShareFee("");
            //            observable.setTxtShareApplFee("");
            //            observable.setTxtShareMemFee("");
            //            observable.setTxtShareValue("");
            //            observable.setTxtShareTotAmount("");
            //        }
            observable.ttNotifyObservers();
//            System.out.println("noshares @22>>>"+noshares);
//          txtNoShares.setText(CommonUtil.convertObjToStr(noshares));  
           
        }        
    }//GEN-LAST:event_txtNoSharesFocusLost
    private String roundOffAmt(String amtStr, String method) throws Exception {
        String amt = amtStr;
        DecimalFormat d = new DecimalFormat();
        d.setMaximumFractionDigits(0);
        d.setDecimalSeparatorAlwaysShown(true);
        if (amtStr != null && !amtStr.equals("")) {
            amtStr = d.parse(d.format(new Double(amtStr).doubleValue())).toString();
        }
        Rounding rd = new Rounding();
        int pos = amtStr.indexOf(".");
        long intPart = 0;
        long decPart = 0;
        if (pos >= 0) {
            intPart = new Long(amtStr.substring(0, pos)).longValue();
            decPart = new Long(amtStr.substring(pos + 1)).longValue();
        } else {
            if (amtStr != null && !amtStr.equals("")) {
                intPart = new Long(amtStr).longValue();
            }
        }
        if (method.equals(NEAREST)) {
            decPart = rd.getNearest(decPart, 10);
            amtStr = intPart + "." + decPart;
        } else if (method.equals(LOWER)) {
            decPart = rd.lower(decPart, 10);
            amtStr = intPart + "." + decPart;
        } else if (method.equals(HIGHER)) {
            decPart = rd.higher(decPart, 10);
            amtStr = intPart + "." + decPart;
        } else if (method.equals(NO_ROUND_OFF)) {
//            decPart = rd.higher(decPart,10);
            // amtStr = intPart+"."+decPart;
            if (!amt.equals("")) {
                amtStr = df.format(Double.parseDouble(amt));
            } else {
                amtStr = amt;
            }
        }
        return amtStr;
    }
    public void calcShareMemValue(double totalShareAmt){
        HashMap memberShipFeeMap = new HashMap();
        memberShipFeeMap = observable.getAdmissionFeeMap();
        double maxAdmAmount = CommonUtil.convertObjToDouble(memberShipFeeMap.get("ADMISSION_FEE_MAX")).doubleValue();
        double minAdmAmount = CommonUtil.convertObjToDouble(memberShipFeeMap.get("ADMISSION_FEE_MIN")).doubleValue();
        if(CommonUtil.convertObjToStr(memberShipFeeMap.get("ADMISSION_FEE_TYPE")).equals("FIXED")){
            double finalMemFee2 = CommonUtil.convertObjToDouble(memberShipFeeMap.get("ADMISSION_FEE")).doubleValue();
            double finalMemFee = CommonUtil.convertObjToDouble(memberShipFeeMap.get("ADMISSION_FEE")).doubleValue();
            if(observable.getActionType()!=ClientConstants.ACTIONTYPE_EDIT){
                observable.setTxtMemFee(txtMemFee.getText());
                observable.setTxtShareMemFee(txtMemFee.getText());
            }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                HashMap edMap = new HashMap();
                edMap.put("SHARE_NO",CommonUtil.convertObjToStr(txtShareAcctNo.getText()));
                List lst=ClientUtil.executeQuery("getSumOfShareMemFee",edMap);
                if(lst!=null && lst.size() >0){
                    HashMap memSumMap = new HashMap();
                    memSumMap = (HashMap) lst.get(0);
                    double sumMemberFee = CommonUtil.convertObjToDouble(memSumMap.get("TOTAL_MEM_FEE")).doubleValue();
                    finalMemFee = finalMemFee + sumMemberFee;
                    if(finalMemFee > maxAdmAmount && finalMemFee > minAdmAmount){
                        txtShareMemFee.setText(String.valueOf(maxAdmAmount - sumMemberFee));
                        txtMemFee.setText(String.valueOf(maxAdmAmount));
                    }else if(finalMemFee < maxAdmAmount && finalMemFee < minAdmAmount){
                        txtShareMemFee.setText(String.valueOf(minAdmAmount - sumMemberFee));
                        txtMemFee.setText(String.valueOf(minAdmAmount));
                    }else{
                        txtShareMemFee.setText(String.valueOf(finalMemFee2));
                        txtMemFee.setText(String.valueOf(finalMemFee2 + sumMemberFee));
                    }
                    observable.setTxtMemFee(txtMemFee.getText());
                    observable.setTxtShareMemFee(txtShareMemFee.getText());
                    //                    observable.setTxtShareMemFee(String.valueOf(CommonUtil.convertObjToDouble(txtMemFee.getText()).doubleValue() +sumMemberFee));
                }
            }
        }else if(CommonUtil.convertObjToStr(memberShipFeeMap.get("ADMISSION_FEE_TYPE")).equals("PERCENT")){
            double admPercentValue = CommonUtil.convertObjToDouble(memberShipFeeMap.get("ADMISSION_FEE")).doubleValue();
            double finalMemFee = (totalShareAmt*admPercentValue)/100;
            double finalMemFee2 = (totalShareAmt*admPercentValue)/100;
            if(observable.getActionType()!=ClientConstants.ACTIONTYPE_EDIT){
                if(finalMemFee > maxAdmAmount && finalMemFee > minAdmAmount){
                    txtMemFee.setText(String.valueOf(maxAdmAmount));
                }else if(finalMemFee < maxAdmAmount && finalMemFee < minAdmAmount){
                    txtMemFee.setText(String.valueOf(minAdmAmount));
                }else{
                    txtMemFee.setText(String.valueOf(finalMemFee));
                }
                observable.setTxtMemFee(txtMemFee.getText());
                observable.setTxtShareMemFee(txtMemFee.getText());
            }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                HashMap edMap = new HashMap();
                edMap.put("SHARE_NO",CommonUtil.convertObjToStr(txtShareAcctNo.getText()));
                List lst=ClientUtil.executeQuery("getSumOfShareMemFee",edMap);
                if(lst!=null && lst.size() >0){
                    HashMap memSumMap = new HashMap();
                    memSumMap = (HashMap) lst.get(0);
                    double sumMemberFee = CommonUtil.convertObjToDouble(memSumMap.get("TOTAL_MEM_FEE")).doubleValue();
                    finalMemFee = finalMemFee + sumMemberFee;
                    if(finalMemFee > maxAdmAmount && finalMemFee > minAdmAmount){
                        txtShareMemFee.setText(String.valueOf(maxAdmAmount - sumMemberFee));
                        txtMemFee.setText(String.valueOf(maxAdmAmount));
                    }else if(finalMemFee < maxAdmAmount && finalMemFee < minAdmAmount){
                        txtShareMemFee.setText(String.valueOf(minAdmAmount - sumMemberFee));
                        txtMemFee.setText(String.valueOf(minAdmAmount));
                    }else{
                        txtShareMemFee.setText(String.valueOf(finalMemFee2));
                        txtMemFee.setText(String.valueOf(finalMemFee2 + sumMemberFee));
                    }
                    observable.setTxtMemFee(txtMemFee.getText());
                    observable.setTxtShareMemFee(txtShareMemFee.getText());
                    //                    observable.setTxtShareMemFee(String.valueOf(CommonUtil.convertObjToDouble(txtMemFee.getText()).doubleValue() +sumMemberFee));
                }
            }
        }
        if(rdoSharewithDrawal.isSelected()==true){
          txtShareMemFee.setText("0.0");  
          observable.setTxtShareMemFee("");
        }
    }
    private void tblShareAcctDetMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblShareAcctDetMousePressed

        tblShareAcctDetRowMouseClicked(-1);
        
        if(CommonUtil.convertObjToStr(observable.getAuthorize()).equals("AUTHORIZED")){
            ClientUtil.enableDisable(panShareAcctDet, false);
            //            panShareAcctDetails.setEnabled(false);
            observable.resetShareAcctDet();
            btnShareAcctDetDel.setEnabled(false);
            btnShareAcctDetSave.setEnabled(false);
            btnShareAcctDetNew.setEnabled(true);
        }
        if(rdoSharewithDrawal.isSelected()==true){
            txtResolutionNo1.setEnabled(false);
            txtResolutionNo1.setEditable(false);
            tdtShareDetIssShareCert.setEnabled(false);
            
        }
        calcTotalShareAmount();
        //rdoSharewithDrawal.setSelected(false);
        System.out.println("@#@#@ table values : "+tblShareAcctDet.getSelectedRow());
        String status = "";
        if (CommonUtil.convertObjToStr(tblShareAcctDet.getValueAt(tblShareAcctDet.getSelectedRow(), 5)).length() > 0) {
            status = CommonUtil.convertObjToStr(tblShareAcctDet.getValueAt(tblShareAcctDet.getSelectedRow(), 5));
        }
        String auth="AUTHORIZED";
        
        if(status.equals(auth) ){
            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
        }else{
            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        }
        if(tblShareAcctDet!=null && tblShareAcctDet.getRowCount()>0){
            double shaeVal=CommonUtil.convertObjToDouble(txtShareValue.getText()).doubleValue();
            String diff=CommonUtil.convertObjToStr(tblShareAcctDet.getValueAt(tblShareAcctDet.getSelectedRow(), 4));
             String authorize=CommonUtil.convertObjToStr(tblShareAcctDet.getValueAt(tblShareAcctDet.getSelectedRow(), 5));
            if(diff.equals("DIFFERENTIAL") && !authorize.equals("AUTHORIZED")){
                ClientUtil.enableDisable(panShareAcctDetails,false);
                txtShareValue.setEnabled(true);
                double changedVal=CommonUtil.convertObjToDouble(txtShareValue.getText()).doubleValue();
                transactionUI.setCallingAmount(txtShareValue.getText());
            }
        
        }
//        if(viewType=="EDIT" && flg==1 && !rdoShareAddition.isEnabled())
//        {
//            btnShareAcctDetNew.setEnabled(false);
//            flg=0;
//        }
    }//GEN-LAST:event_tblShareAcctDetMousePressed
    
    private void cboMemDivPayModeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboMemDivPayModeFocusLost
        // TODO add your handling code here:
        //        if(((ComboBoxModel)cboMemDivPayMode.getModel()).getKeyForSelected().toString().equals("CASH")){
        //            cboMemDivProdID.setEnabled(false);
        //            cboMemDivProdType.setEnabled(false);
        //            txtDivAcNo.setEnabled(false);
        //            txtDivAcNo.setEditable(false);
        //        }else if(((ComboBoxModel)cboMemDivPayMode.getModel()).getKeyForSelected().toString().equals("TRANSFER")){
        //             cboMemDivProdID.setEnabled(true);
        //            cboMemDivProdType.setEnabled(true);
        //            txtDivAcNo.setEnabled(true);
        //            txtDivAcNo.setEditable(true);
        //        }else{
        //            ClientUtil.displayAlert("please Select The Dividend Pay Mode");
        //        }
    }//GEN-LAST:event_cboMemDivPayModeFocusLost
    
    private void addRadioButtons() {// these r all radio button purpose adding...
        rgbExistCust = new CButtonGroup();
        rgbExistCust.add(rdoExistCustYes);
        rgbExistCust.add(rdoExistCustNo);
        
    }
    
    private void removeRadioButtons() {
        rgbExistCust.remove(rdoExistCustNo);
        rgbExistCust.remove(rdoExistCustYes);
        
    }
    
    private void cboMemDivPayModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMemDivPayModeActionPerformed
        // TODO add your handling code here:
        cboCreditAccDetails();
    }//GEN-LAST:event_cboMemDivPayModeActionPerformed
    private void cboCreditAccDetails(){
        String installType = ((ComboBoxModel)cboMemDivPayMode.getModel()).getKeyForSelected().toString();        // TODO add your handling code here:
        if(installType.equals("TRANSFER")){
            cboMemDivProdType.setEnabled(true);
            cboMemDivProdID.setEnabled(true);
            txtDivAcNo.setEnabled(true);
            btnDivAcNoFileOpen.setEnabled(true);
            //            cboMemDivProdType.setSelectedItem(observable.get());
            //            cboMemDivProdID.setSelectedItem(observable.getCboProdId());
            //            txtCustomerIdCr.setText(observable.getCustomerIdCr());
            //            lblCustomerNameCrValue.setText(observable.getCustomerNameCrValue());
        }else{
            cboMemDivProdType.setEnabled(false);
            cboMemDivProdID.setEnabled(false);
            txtDivAcNo.setEnabled(false);
            btnDivAcNoFileOpen.setEnabled(false);
            cboMemDivProdType.setSelectedItem("");
            cboMemDivProdID.setSelectedItem("");
            txtDivAcNo.setText("");
            txtDivAcNo.setText("");
            
        }
    }
    private void cboMemDivProdIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMemDivProdIDActionPerformed
        // TODO add your handling code here:
        if (cboMemDivProdID.getSelectedIndex() > 0) {
            clearProdFields();
            
            observable.setCboDivProdId((String) cboMemDivProdID.getSelectedItem());
            //            if( observable.getCboDivProdId().length() > 0){
            //
            //                //When the selected Product Id is not empty string
            //                observable.setAccountHead();
            //                txtAccHdId.setText(observable.getTxtAccHd());
            //                lblAccHdDesc.setText(observable.getLblAccHdDesc());
            //                if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW ||
            //                observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT ) {
            //                    this.setRadioButtons();
            //                    productBased();
            //                }
            //            }
            //            if(!TrueTransactMain.BRANCH_ID.equals(CommonUtil.convertObjToStr(TrueTransactMain.selBranch))){
            //                        HashMap InterBranMap = new HashMap();
            //                        InterBranMap.put("AC_HD_ID",observable.getTxtAccHd());
            //                        List lst = ClientUtil.executeQuery("AcHdInterbranchAllowedOrNot", InterBranMap);
            //                        InterBranMap=null;
            //                        if(lst!=null && lst.size()>0){
            //                            InterBranMap=(HashMap)lst.get(0);
            //                            String IbAllowed=CommonUtil.convertObjToStr(InterBranMap.get("INTER_BRANCH_ALLOWED"));
            //                            if(IbAllowed.equals("N")){
            //                                ClientUtil.displayAlert("InterBranch Transactions Not Allowed For This AC_HD");
            //                                observable.resetForm();
            //                                clearProdFields();
            //                            }
            //                        }
            //                    }
        }
    }//GEN-LAST:event_cboMemDivProdIDActionPerformed
    
    private void btnDivAcNoFileOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDivAcNoFileOpenActionPerformed
        // TODO add your handling code here:
        callView("DIVACNO");
    }//GEN-LAST:event_btnDivAcNoFileOpenActionPerformed
    
    private void cboMemDivProdTypeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboMemDivProdTypeFocusLost
    }//GEN-LAST:event_cboMemDivProdTypeFocusLost
    
    private void cboMemDivProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMemDivProdTypeActionPerformed
        // TODO add your handling code here:
        ArrayList key = new ArrayList();
        ArrayList value = new ArrayList();
        key.add("");
        value.add("");
        observable.setCbmDivProdID(new ComboBoxModel(key,value));
        cboMemDivProdID.setModel(observable.getCbmDivProdID());
        
        prodType = ((ComboBoxModel)cboMemDivProdType.getModel()).getKeyForSelected().toString();
        if(prodType!=null && prodType.length() > 0){
            if(prodType.equals("GL")){
                cboMemDivProdID.setEnabled(false);
            }else{
                cboMemDivProdID.setEnabled(true);
                observable.callProdIdsForProductType(prodType);
                cboMemDivProdID.setModel(observable.getCbmDivProdID());
            }
            
            if(prodType.equals("RM")){
                btnDivAcNoFileOpen.setEnabled(false);
                lblMemDivAcNo.setName("Favouring Name");
            } else {
                lblMemDivAcNo.setName("Dividend Account No");
            }
        } else {
                key = new ArrayList();
                value = new ArrayList();
                key.add("");
                value.add("");
                observable.setCbmDivProdID(new ComboBoxModel(key,value));
            }
        
    }//GEN-LAST:event_cboMemDivProdTypeActionPerformed
    
    private void mitResolutionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitResolutionActionPerformed
        btnResolutionActionPerformed(evt);
    }//GEN-LAST:event_mitResolutionActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void mitExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitExceptionActionPerformed
        btnExceptionActionPerformed(evt);
    }//GEN-LAST:event_mitExceptionActionPerformed
    
    private void mitRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitRejectActionPerformed
        btnRejectActionPerformed(evt);
    }//GEN-LAST:event_mitRejectActionPerformed
    
    private void mitAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitAuthorizeActionPerformed
        btnAuthorizeActionPerformed(evt);
    }//GEN-LAST:event_mitAuthorizeActionPerformed
    
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
    
    private void btnResolutionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResolutionActionPerformed
        //--- If it is null, create the observable
        //        if(observable==null){
        //            setObservable();
        //        }
        setModified(true);
        resolutionStatus(CommonConstants.STATUS_SEND_TO_RESOLUTION);
    }//GEN-LAST:event_btnResolutionActionPerformed
    private void resolutionStatus(String resolutionStatus) {
        if (viewType == SEND_TO_RESOLUTION && isFilled){
            HashMap singleResolutionMap = new HashMap();
            singleResolutionMap.put("STATUS", resolutionStatus);
            singleResolutionMap.put("USER_ID", TrueTransactMain.USER_ID);
            //            singleResolutionMap.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
            singleResolutionMap.put("SHARE ACCOUNT NO", txtShareAcctNo.getText());
            singleResolutionMap.put("SHARE ACCOUNT DETAIL NO", shareAccountDetailNo);
            boolean isExec = ClientUtil.executeWithResult("sendToResolutionShare", singleResolutionMap);
            //            ArrayList arrList = null;
            //            if (isExec)
            //                arrList = observable.populateShareSuspense(singleResolutionMap);
            //            if (arrList!=null)
            //                observable.doCOShareSuspense(arrList);
            btnCancelActionPerformed(null);
        } else{
            viewType = SEND_TO_RESOLUTION;
            final HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            mapParam.put(CommonConstants.MAP_NAME, "getSelectShareResolutionTOList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "sendToResolutionShare");
            mapParam.put(CommonConstants.SENDTORESOLUTIONSTATUS, resolutionStatus);
            isFilled = false;
            final ResolutionStatusUI resolutionStatusUI = new ResolutionStatusUI(this, mapParam);
            resolutionStatusUI.show();
            btnCancel.setEnabled(true);
            whereMap = null;
        }
    }
    private void cboShareTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboShareTypeActionPerformed
        observable.setCboShareType(CommonUtil.convertObjToStr(cboShareType.getSelectedItem()));
        if (cboShareType.getSelectedIndex() > 0) {
            //Check here
            HashMap checkMap = new HashMap();
            checkMap.put("SHARE_TYPE", CommonUtil.convertObjToStr(((ComboBoxModel) (cboShareType.getModel())).getKeyForSelected()));
            //DRF_ALLOWED
            List drfList = ClientUtil.executeQuery("getDRFAllowedForShareType", checkMap);
            if (drfList != null && drfList.size() > 0) {
                checkMap = (HashMap) drfList.get(0);
                if (checkMap.containsKey("DRF_ALLOWED") && checkMap.get("DRF_ALLOWED") != null && checkMap.get("DRF_ALLOWED").equals("N")) {
                    chkDrfApplicableYN.setEnabled(false);
                }else{
                    chkDrfApplicableYN.setEnabled(true);
                }
            }
            // End
        }
        if(!viewType.equals(ClientConstants.VIEW_TYPE_EDIT) && !viewType.equals(ClientConstants.VIEW_TYPE_DELETE)){
            updateFeeOB();
            if(!cboShareType.getSelectedItem().equals("")){
                observable.populateFeeDetails();
                nomineeUi.setMaxNominee(observable.maxNominee);
            }
            updateFee();
        }
        final String shareType = CommonUtil.convertObjToStr(((ComboBoxModel)(cboShareType.getModel())).getKeyForSelected());
        HashMap hmap=new HashMap();
        hmap.put("SHARE_TYPE",shareType);
        List list=ClientUtil.executeQuery("getMinIntialShares", hmap);
        hmap=null;
        if( list != null && list.size()>0){
            String newField=observable.getNewMode();
            hmap=(HashMap)list.get(0);
            minShares=CommonUtil.convertObjToInt(hmap.get("MIN_INTIAL_SHARES"));
            String prefix=CommonUtil.convertObjToStr(hmap.get("NUM_PATTERN_PREFIX"));
            String suffix=CommonUtil.convertObjToStr(hmap.get("NUM_PATTERN_SUFFIX"));
            maxShare=CommonUtil.convertObjToInt(hmap.get("MAXIMUM_SHARE"));
            
            String shareNo=prefix+suffix;
            if (newField.equals("true")) {
                txtShareAcctNo.setText(shareNo);
            }
            //            added by nikhil
            if(CommonUtil.convertObjToStr(hmap.get("SUBSIDY_FOR_SCST")).length()>0 && CommonUtil.convertObjToStr(hmap.get("SUBSIDY_FOR_SCST")).equals("Y")){
                subsidyFlag = true;
                customersShare = CommonUtil.convertObjToDouble(hmap.get("CUSTOMERS_SHARE")).doubleValue();
                //                observable.setCustomersShare(String.valueOf(customersShare));
                govtsShare = CommonUtil.convertObjToDouble(hmap.get("GOVTS_SHARE")).doubleValue();
                //                observable.setGovtsShare(String.valueOf(govtsShare));
            }
        }
        
       // generateID();
        HashMap accNoMap=generateID();
       txtNextAccNo.setText((String) accNoMap.get(CommonConstants.DATA));
       
       //Set of code added by Jeffin John on 07-06-2014 for Manti ID - 9229
       
       HashMap shareMap = null;
        List headOfficeList = ClientUtil.executeQuery("getHeadOffice", shareMap);
        if(headOfficeList != null && headOfficeList.size()>0){
            HashMap headOfficeMap = (HashMap) headOfficeList.get(0);
            if(headOfficeMap != null && headOfficeMap.containsKey("HEAD_OFFICE")){
                head_office = CommonUtil.convertObjToStr(headOfficeMap.get("HEAD_OFFICE"));
            }
        }
        if(!(head_office.equals(ProxyParameters.BRANCH_ID))){
            if(cboShareType.getSelectedItem().equals("A Class") && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
                ClientUtil.showAlertWindow("New A Class share is not allowed");
                cboShareType.setSelectedItem("");
                txtShareAmt.setText("");
                txtShareAcctNo.setText("New");
                return;
            }
        }
        if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
            getHeadsForService();
        }
        
       //Added by sreekrishnan
        HashMap maxMap = new HashMap();
        maxMap.put("SHARE_TYPE",shareType);
        List maxlist=ClientUtil.executeQuery("getMaxShareToNo", maxMap);
        if( maxlist != null && maxlist.size()>0){
            maxMap=(HashMap)maxlist.get(0);
            lblMaxSlNo.setText(CommonUtil.convertObjToStr(maxMap.get("MAX_SL_NO")));
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || rdoShareAddition.isSelected()){
              txtFromSl_No.setText(CommonUtil.convertObjToStr(maxMap.get("MAX_SL_NO")));// Added by nithya on 07-09-2020 for KD-2227
            }
            txtFromSl_NoFocusLost(null);
        }
    }//GEN-LAST:event_cboShareTypeActionPerformed
    
    public HashMap generateID() {
         HashMap hash = null, result = null;
        try {
            String mapName = "getCurrentID";
            HashMap where = new HashMap();
            where.put("ID_KEY", "SHARE_APPL_NO"); //Here u have to pass SHARE_APPL_NO or something else
            List list = null;
//            sqlMap.executeUpdate("updateIDGenerated", where);  // This update statement just updates curr_value=curr_value+1
            list = (List) ClientUtil.executeQuery(mapName, where);  // This will get u the updated curr_value, prefix and length
            //sqlMap.commitTransaction();

            if (list.size() > 0) {
                hash = (HashMap) list.get(0);
                String strPrefix = "", strLen = "";

                // Prefix for the ID.
                if (hash.containsKey("PREFIX")) {
                    strPrefix = (String) hash.get("PREFIX");
                    if (strPrefix == null || strPrefix.trim().length() == 0) {
                        strPrefix = "";
                    }
                }

                // Maximum Length for the ID
                int len = 10;
                if (hash.containsKey("ID_LENGTH")) {
                    strLen = String.valueOf(hash.get("ID_LENGTH"));
                    if (strLen == null || strLen.trim().length() == 0) {
                        len = 10;
                    } else {
                        len = Integer.parseInt(strLen.trim());
                    }
                }

                int numFrom = strPrefix.trim().length();

                String newID = String.valueOf(hash.get("CURR_VALUE"));
                long d = (long) Double.parseDouble(newID) + 1;
                newID = "";
                newID = "" + d;
                // Number Part of the String and incrementing 1 (ex. only 00085 from OGGOT00085)
                result = new HashMap();
                String genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
                result.put(CommonConstants.DATA, genID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    private void updateFeeOB(){
        observable.setTxtApplFee(txtApplFee.getText());
        observable.setTxtMemFee(txtMemFee.getText());
        observable.setTxtShareFee(txtShareFee.getText());
        observable.setTxtShareAmt(txtShareAmt.getText());
        enableDisableShareFees(false);
    }

    private void enableDisableShareFees(boolean flag){
        txtApplFee.setEnabled(flag);
        txtMemFee.setEnabled(flag);
        txtShareFee.setEnabled(flag);
        txtShareAmt.setEnabled(flag);
    }
    
    private void updateFee(){
        txtApplFee.setText(observable.getTxtApplFee());
        //        if(CommonUtil.convertObjToStr(observable.getTxtMemFee()).equals("FIXED")){
        //            txtMemFee.setValidation(new CurrencyValidation(14,2));
        //            lblMemFee.setText(resourceBundle.getString("lblMemFeeFixed"));
        //        }else{
        //            txtMemFee.setValidation(new NumericValidation(2,2));
        //            lblMemFee.setText(resourceBundle.getString("lblMemFeePercent"));
        //        }
        txtMemFee.setText(observable.getTxtMemFee());
        txtShareFee.setText(observable.getTxtShareFee());
        txtShareAmt.setText(observable.getTxtShareAmt());
        lblServiceTaxval.setText(observable.getLblServiceTaxval());
    }
    
    private void txtShareMemFeeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtShareMemFeeFocusLost
        //        if (txtShareDetNoOfShares.getText().length()!=0 && txtShareDetShareNoFrom.getText().length()!=0) {
        //            int noOfShares = Integer.parseInt(txtShareDetNoOfShares.getText());
        //            int start = Integer.parseInt(txtShareDetShareNoFrom.getText());
        //            int end = start+noOfShares-1;
        ////            txtShareDetShareNoTo.setText(String.valueOf(end));
        //            updateOBShareAcctDet();
        //            HashMap whereMap = new HashMap();
        //            whereMap.put("SHARE_NO", txtShareDetShareNoFrom.getText());
        //            if (isTblSharAcctClicked) {
        //                whereMap.put("SHARE_NO_FROM", txtShareDetShareNoFrom.getText());
        ////                whereMap.put("SHARE_NO_TO", txtShareDetShareNoTo.getText());
        //            }
        //            if (observable.validateShareNos(whereMap)) {
        //                displayAlert("Given share No/Nos exist.");
        //                txtShareDetShareNoFrom.setText("");
        ////                txtShareDetShareNoTo.setText("");
        //            }
        //        }
        //        noOfShares();
        //        if(txtShareDetShareNoFrom.getText().trim().equals("")){
        //            observable.setTxtShareDetNoOfShares("");
        //            txtShareDetNoOfShares.setText(observable.getTxtShareDetNoOfShares());
        //        }
        calcTotalShareDetails();
         //if(rdoShareAddition.isSelected() && TrueTransactMain.SERVICE_TAX_REQ .equals("Y")){
        if(TrueTransactMain.SERVICE_TAX_REQ .equals("Y")){
            serviceTaxCalculation();
         }
    }//GEN-LAST:event_txtShareMemFeeFocusLost
    //    //--- To calculate the No. of shares from the ShareNoFrom  and the ShareNoTo txtFields
    //    private void noOfShares(){
    //        int shareNoFrom = 0;
    //        int shareNoTo = 0;
    //        int noOfShares = 0;
    //        if((txtShareDetShareNoFrom.getText()!=null) && (txtShareDetShareNoFrom.getText().length()!=0)){
    //            shareNoFrom = Integer.parseInt(txtShareDetShareNoFrom.getText());
    //        }
    //        if((txtShareDetShareNoTo.getText()!=null) && (txtShareDetShareNoTo.getText().length()!=0)){
    //            shareNoTo = Integer.parseInt(txtShareDetShareNoTo.getText());
    //        }
    //        if((shareNoTo==shareNoFrom && shareNoTo==0)   || (shareNoTo==0 && shareNoFrom>0)){
    //            noOfShares = 1;
    //        } else {
    //            noOfShares = shareNoTo - shareNoFrom + 1;
    //        }
    //        observable.setTxtShareDetNoOfShares(String.valueOf(noOfShares));
    //        txtShareDetNoOfShares.setText(observable.getTxtShareDetNoOfShares());
    //    }
    private void txtTotShareFeeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTotShareFeeFocusLost
        //        if(txtShareDetShareNoFrom.getText()!=null && !txtShareDetShareNoFrom.getText().trim().equals("")){
        //            noOfShares();
        //        } else {
        //            observable.setTxtShareDetShareNoTo("");
        //            txtShareDetShareNoTo.setText(observable.getTxtShareDetShareNoTo());
        //        }
        calcTotalShareDetails();
    }//GEN-LAST:event_txtTotShareFeeFocusLost
    
    private void chkNotEligibleStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkNotEligibleStatusActionPerformed
        if(chkNotEligibleStatus.isSelected()==true){
            tdtNotEligiblePeriod.setEnabled(true);
        } else {
            tdtNotEligiblePeriod.setEnabled(false);
        }
    }//GEN-LAST:event_chkNotEligibleStatusActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        //--- If it is null, create the observable
        //        if(observable==null){
        //            setObservable();
        //        }
        viewType = ClientConstants.VIEW_TYPE_AUTHORIZE;
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
        nomineeUi.disableNewButton(false);
        nomineeUi.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        //--- If it is null, create the observable
        //        if(observable==null){
        //            setObservable();
        //        }
        
        viewType = ClientConstants.VIEW_TYPE_AUTHORIZE;
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        nomineeUi.disableNewButton(false);
        nomineeUi.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        //--- If it is null, create the observable
        //        if(observable==null){
        //            setObservable();
        //        }
        viewType = ClientConstants.VIEW_TYPE_AUTHORIZE;
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        nomineeUi.disableNewButton(false);
        nomineeUi.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        drfNomineeUi.disableNewButton(false);//Added By Revathi.L
        drfNomineeUi.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        ClientUtil.enableDisable(panShareTrans,false);
        panShareTrans.setVisible(true);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
        
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    /** Does the authorization for the row selected in the AuthorizationUI screen */
    public void authorizeStatus(String authorizeStatus) {
        StringBuffer manData = new StringBuffer();
        HashMap supMap = new HashMap();
        supMap.put("SHARE_TYPE", CommonUtil.convertObjToStr(observable.getCbmShareType().getKeyForSelected()));
        List lstSupName = ClientUtil.executeQuery("getMandatoryDataForSelectItem", supMap);
        HashMap supMapData = new HashMap();
        if (lstSupName != null && lstSupName.size() > 0) {
            supMapData = (HashMap) lstSupName.get(0);
        }
        String popData = CommonUtil.convertObjToStr(supMapData.get("MANDATORY_DATA"));
        String popAddrData = CommonUtil.convertObjToStr(supMapData.get("MANDATORY_ADDR_DATA"));
        ArrayList dataList = new ArrayList();
        if (popData != null && popData.length() > 0 && !popData.equalsIgnoreCase("")) {
            String[] dataArray = popData.split(",");
            for (int i = 0; i < dataArray.length; i++) {
                supMap.put("CUST_ID", txtCustId.getText());
                supMap.put("FIELD", dataArray[i]);
                lstSupName = ClientUtil.executeQuery("getMandatoryDataCheckFunction", supMap);
                dataList.add(lstSupName.get(0));
            }
            if (popAddrData != null && popData.length() > 0 && !popAddrData.equalsIgnoreCase("")) {
                String[] dataAddrArray = popAddrData.split(",");
                for (int i = 0; i < dataAddrArray.length; i++) {
                    supMap.put("CUST_ID", txtCustId.getText());
                    supMap.put("FIELD", dataAddrArray[i]);
                    lstSupName = ClientUtil.executeQuery("getMandatoryDataCheckFunctionCustAddr", supMap);
                    dataList.add(lstSupName.get(0));
                }
            }
            if (dataList.size() > 0 || !dataList.equals("null")) {
                for (int i = 0; i < dataList.size(); i++) {
                    HashMap newmap = new HashMap();
                    newmap = (HashMap) (dataList.get(i));
                    if (newmap.containsKey("DATA") && newmap.get("DATA") != null) {
                        manData.append(newmap.get("DATA"));
                        manData.append("\n");
                    }
                }
                if (!(manData.toString().equals(""))) {
                    ClientUtil.displayAlert(manData + "for the  Customer");
                    btnCancelActionPerformed(null);
                    return;
                    //            setAuthBtnEnableDisable();
                    //            btnDelete.setEnabled(false);
                }
            }
        }
        if (viewType == ClientConstants.VIEW_TYPE_AUTHORIZE && isFilled) {
            //Changed By Suresh
            //            String isAllTabsVisited = tabShare.isAllTabsVisited();
            //            //--- If all the tabs are not visited, then show the Message
            //            if(isAllTabsVisited.length()>0){
            //                CommonMethods.displayAlert(isAllTabsVisited);
            //            } else {

            HashMap status = new HashMap();
            status.put("ACC_NO", txtShareAcctNo.getText());
            List statusList = ClientUtil.executeQuery("getClosedStatus", status);
            if (statusList != null && statusList.size() > 0) {
                HashMap statusMap = (HashMap) statusList.get(0);
                String cStatus = statusMap.get("STATUS").toString();
                if (cStatus.equals("CLOSED")) {
                    HashMap updateMap = new HashMap();
                    updateMap.put("CUST_ID", txtCustId.getText());
                    ClientUtil.execute("getUpdateClosedStatus", updateMap);
                }
            }

            HashMap singleAuthorizeMap = new HashMap();
            ArrayList arrList = new ArrayList();
            HashMap authDataMap = new HashMap();
            HashMap listMap = new HashMap();
            if (TRANS_ID != null && TRANS_ID.length() > 0) {
                authDataMap.put("TRANS_ID", TRANS_ID);
                TRANS_ID = "";
            } else if (BATCH_ID != null && BATCH_ID.length() > 0) {
                authDataMap.put("BATCH_ID", BATCH_ID);
                BATCH_ID = "";

            }
            /*commented by rishad 07/mar/2018 list contains multple values .customer may be choose any order.we cant take first index value from list.it will 
            effect authorize transation */
//            if (authLst != null && authLst.size() > 0) {
//                listMap = (HashMap) authLst.get(0);
//                authDataMap.put("BATCH_ID", CommonUtil.convertObjToStr(listMap.get("BATCH_ID")));
//                authDataMap.put("TRANS_ID", CommonUtil.convertObjToStr(listMap.get("TRANS_ID")));
//            } else {
//                if (BATCH_ID != null || !BATCH_ID.equals("")) {
//                    authDataMap.put("BATCH_ID", BATCH_ID);
//                }
//                BATCH_ID = "";
//            }
            authDataMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            authDataMap.put(SHARE_ACCT_NO, txtShareAcctNo.getText());
            authDataMap.put(SHARE_APPL_NO, txtApplicationNo.getText());
            authDataMap.put("CASHIER_AUTH_ALLOWED", TrueTransactMain.CASHIER_AUTH_ALLOWED);
            authDataMap.put(SHARE_DET_NO, shareDetNo);
            authDataMap.put(SHARE_TYPE, observable.getCbmShareType().getKeyForSelected());
            arrList.add(authDataMap);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            singleAuthorizeMap.put("CASHIER_AUTH_ALLOWED", TrueTransactMain.CASHIER_AUTH_ALLOWED);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(singleAuthorizeMap);
            authLst = null;
            viewType = ClientConstants.VIEW_TYPE_CANCEL;
            ClientUtil.enableDisable(panTransaction, false);
            if (chkDrfApplicableYN.isSelected()) {
                observable.setDrfApplicableFlag(true);
            } else {
                observable.setDrfApplicableFlag(false);
            }
            // If we are implementing Truetransact branchwise, Send to Resolution & Share Resolution option in HO are not possible
            // By Rajesh
            //                transactionUI.resetObjects();
            //            }

        } else {
            HashMap mapParam = new HashMap();
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                mapParam.put(CommonConstants.MAP_NAME, "viewAllShareAcctCashierAuthorizeTOList");
            } else //            mapParam.put(CommonConstants.MAP_NAME, "viewAllShareAcctAuthorizeTOList");
            {
                mapParam.put(CommonConstants.MAP_NAME, "viewAllShareAcctAuthorizeTOList11");
            }
            //            mapParam.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
            whereMap.put("TRANS_DT", currDt.clone());
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            whereMap.put("CASHIER_AUTH_ALLOWED", TrueTransactMain.CASHIER_AUTH_ALLOWED);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap.put(CommonConstants.HIERARCHY_ID, ProxyParameters.HIERARCHY_ID);
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                authLst = ClientUtil.executeQuery("viewAllShareAcctCashierAuthorizeTOList", whereMap);
                if (authLst != null && authLst.size() > 0) {
                    mapParam.put("viewAllShareAcctCashierAuthorizeTOList", authLst);
                }
            } else {
                authLst = ClientUtil.executeQuery("viewAllShareAcctAuthorizeTOList11", whereMap);
                if (authLst != null && authLst.size() > 0) {
                    mapParam.put("viewAllShareAcctAuthorizeTOList11", authLst);
                }
            }
            //if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("N")) {
            if (!(authLst != null && authLst.size() > 0)) {
                mapParam.put(CommonConstants.MAP_NAME, "viewAllShareAcctAuthorizeTOListWithoutShareDetails");
            }
            //}
            whereMap = null;

            //            mapParam.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            viewType = ClientConstants.VIEW_TYPE_AUTHORIZE;
            btnSave.setEnabled(false);
           
        }
    }

    public void authorize(HashMap map){
        map.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        if(rdoShareAddition.isSelected()==false && rdoSharewithDrawal.isSelected()==false && rdoShareDifferentialAmount.isSelected()==false){
            ClientUtil.displayAlert("Transaction Not Proper withdrawal Or add not Selected");
        }else{
            double noOfShares=CommonUtil.convertObjToDouble(txtNoShares.getText()).doubleValue();
            double outStandingAmount=CommonUtil.convertObjToDouble(txtShareValue.getText()).doubleValue();
            map.put("SHARE_ACCT_NO",CommonUtil.convertObjToStr(txtShareAcctNo.getText()));
            map.put("APPLN_DT",currDt.clone());
            List transList=ClientUtil.executeQuery("getShareTranDetailsForAuthorization", map);
            if(!(transList != null && transList.size() > 0)){
                rdoShareAddition.setSelected(false);
                rdoSharewithDrawal.setSelected(false);
                rdoShareDifferentialAmount.setSelected(false);
            }
            if(rdoShareAddition.isSelected()==true && rdoSharewithDrawal.isSelected()==false && rdoShareDifferentialAmount.isSelected()==false){
                map.put("TRANS_TYPE","ADD");
                map.put("AVILABLE_NO_SHARES",new Double(noOfShares));
                map.put("OUTSTANDING_AMOUNT", new Double(outStandingAmount));
                
            } else if (rdoShareAddition.isSelected() == false && rdoSharewithDrawal.isSelected() == true && rdoShareDifferentialAmount.isSelected() == false) {
                map.put("TRANS_TYPE","WITHDRAWAL");
                map.put("AVILABLE_NO_SHARES",new Double(noOfShares*-1));
                map.put("OUTSTANDING_AMOUNT", new Double(outStandingAmount*-1));
            } else if (rdoShareAddition.isSelected() == false && rdoSharewithDrawal.isSelected() == false && rdoShareDifferentialAmount.isSelected() == true) {
                map.put("TRANS_TYPE","DIFFERENTIAL");
                map.put("AVILABLE_NO_SHARES",new Double(0.0));
                List cashTransList = new ArrayList();
                if(tempProxyReturnMap!=null && tempProxyReturnMap.containsKey("CASH_TRANS_LIST")){
                    cashTransList = (ArrayList) tempProxyReturnMap.get("CASH_TRANS_LIST");
                    if(cashTransList!=null && cashTransList.size()>0){
                        for(int i = 0 ; i<cashTransList.size() ; i++){
                            tempProxyReturnMap = (HashMap) cashTransList.get(i);
                        }
                    }
                }
                if(tempProxyReturnMap!=null && tempProxyReturnMap.containsKey("TRANSFER_TRANS_LIST")){ // Added by nithya on 23-08-2019 for  KD 595 - While Doing the transaction of Share - Differential the Outstanding Balance @ Share Master table not updating. 
                    cashTransList = (ArrayList) tempProxyReturnMap.get("TRANSFER_TRANS_LIST");
                    if(cashTransList!=null && cashTransList.size()>0){
                        for(int i = 0 ; i<cashTransList.size() ; i++){
                            tempProxyReturnMap = (HashMap) cashTransList.get(i);
                        }
                    }
                }
                map.put("OUTSTANDING_AMOUNT", CommonUtil.convertObjToDouble(tempProxyReturnMap.get("AMOUNT")).doubleValue());
                tempProxyReturnMap = null;
            }else if(rdoShareAddition.isSelected() == false && rdoSharewithDrawal.isSelected() == false && rdoShareDifferentialAmount.isSelected() == false){
                map.put("TRANS_TYPE","");
                map.put("AVILABLE_NO_SHARES",new Double(0.0));
                map.put("OUTSTANDING_AMOUNT", new Double(0.0)); 
            }
            
            
            observable.setAuthorizeMap(map);
            try{
                
                   CommonUtil comm = new CommonUtil();
                    final JDialog loading = comm.addProgressBar();
                    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                        
                        @Override
                        protected Void doInBackground() throws InterruptedException, Exception /** Execute some operation */
                        {
                           observable.doAction(nomineeUi.getNomineeOB(),drfNomineeUi.getNomineeOB()); 
                           // System.out.println("result 222 OB : ");// To perform the necessary operation depending on the Action type...
                            return null;
                        }

                        @Override
                        protected void done() {
                            loading.dispose();
                        }
                    };
                    worker.execute();
                    loading.show();
                    try {
                        worker.get();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                     
                
               
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        
        if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
            isFilled = false;
            super.setOpenForEditBy(observable.getStatusBy());
            super.removeEditLock(txtShareAcctNo.getText());
            
            observable.setResultStatus();
            if (fromNewAuthorizeUI) {
               newauthorizeListUI.removeSelectedRow();
                this.dispose();
                newauthorizeListUI.setFocusToTable();
                if(authorizeListScreen.equals("share")){
                   newauthorizeListUI.displayDetails("Share");
                }
                if(authorizeListScreen.equals("shareAccount")){
                  newauthorizeListUI.displayDetails("Share Account");
                }
            }
            if (fromAuthorizeUI) {
                authorizeListUI.removeSelectedRow();
                this.dispose();
                authorizeListUI.setFocusToTable();
                if(authorizeListScreen.equals("share")){
                    authorizeListUI.displayDetails("Share");
                }
                if(authorizeListScreen.equals("shareAccount")){
                    authorizeListUI.displayDetails("Share Account");
                }
            }
            if (fromCashierAuthorizeUI) {
                CashierauthorizeListUI.removeSelectedRow();
                this.dispose();
                CashierauthorizeListUI.setFocusToTable();
            } 
            if (fromManagerAuthorizeUI) {
                ManagerauthorizeListUI.removeSelectedRow();
                this.dispose();
                ManagerauthorizeListUI.setFocusToTable();
            }
            cancelOperation();
            
        }
    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
//        closeOperation();
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    private void closeOperation(){
        viewType = ClientConstants.VIEW_TYPE_CANCEL;
        cancelOperation();
        this.dispose();
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        setModified(false);
        if( viewType.equals(ClientConstants.VIEW_TYPE_EDIT) || viewType.equals(ClientConstants.VIEW_TYPE_DELETE)){
            super.removeEditLock(txtShareAcctNo.getText());
        }
        viewType=ClientConstants.VIEW_TYPE_CANCEL;
        cancelOperation();
        observable.setSelectedBranchID(ProxyParameters.BRANCH_ID);
        txtNoShares.setText("");
        // If we are implementing Truetransact branchwise, Send to Resolution & Share Resolution option in HO are not possible
        // By Rajesh
        //        resetTransactionUI();
        btnDelete.setEnabled(false);
//        btnReject.setEnabled(false);
        rdoShareDifferentialAmount.setSelected(false);
        save=false;
        chkClr();
        txtNextAccNo.setText("");
        btnShareAppNo.setEnabled(false);
        observable.setDrfApplicableFlag(false);
        txtShareValue.setText("");
        txtShareTotAmount.setText("");
        txtNoShares.setText("");
        txtShareApplFee.setText("");
        txtTotShareFee.setText("");
        observable.balance=0;
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        lblMaxSlNo.setText("");
        txtFromSl_No.setText("");
        txtToSL_No.setText("");
        tabShare.remove(drfNomineeUi);
        btnShareActNo.setEnabled(false);
        btnNew.setEnabled(true);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        drfExist = false;
        ClientUtil.enableDisable(panMobileBanking, false);
        chkMobileBanking.setSelected(false);
        tdtMobileSubscribedFrom.setDateValue("");
       
    }//GEN-LAST:event_btnCancelActionPerformed
    private void resetTransactionUI(){
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
    }
    
    private void chkClr(){
        chkDrfApplicableYN.setSelected(false);
         if(chkDrfApplicableYN.isSelected()){
            cboDrfProdId.setVisible(true);
            lblDrfProdId.setVisible(true);
        }else{
            cboDrfProdId.setVisible(false);
            lblDrfProdId.setVisible(false);
            lblDRFAmt.setVisible(false);
            lblDrfAmtVal.setVisible(false);
            lblDrfAmtVal.setText("");
        } 
         txtNextAccNo.setText("");
         txtNextAccNo.setEnabled(false);
    }
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        viewType=ClientConstants.VIEW_TYPE_DELETE;
        //--- If it is null, create the observable
        //        if(observable==null){
        //            setObservable();
        //        }
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        popUp();
        nomineeUi.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        disableScreen();
    }//GEN-LAST:event_btnDeleteActionPerformed
    private void disableScreen(){
        btnCustomerIdFileOpen.setEnabled(false);
        //        setBtnShareAcctDet(false);
        setBtnShareJoint(false);
        ClientUtil.enableDisable(this, false);
        //        tblsEnableDisable(false);
    }
    
    //    private void tblsEnableDisable(boolean val){
    //        tblShareAcctDet.setEnabled(val);
    //        tblJointAcctHolder.setEnabled(val);
    //    }
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        viewType = ClientConstants.VIEW_TYPE_EDIT;
        //--- If it is null, create the observable
        drfRecieptFlag=false;
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        //        if(observable==null){
        //            setObservable();
        //        }
        
        //        try{
        //            observable.fillDropdown();
        //        } catch (Exception e){
        //            e.printStackTrace();
        //        }
        //        initComponentData();
        
        //        ACTIONEDITDELETE = 1;
        ClientUtil.enableDisable(this,false);
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
          setButtonEnableDisable();

        //        observable.setStatus();
        //        popUp();  //commented by abi
        txtShareAcctNo.setEnabled(true);
        btnShareActNo.setEnabled(true);
        btnIdPrint.setEnabled(true);
        txtCustId.setEnabled(true);
        txtApplicationNo.setEnabled(true);
        txtEmpRefNoNew.setEnabled(true);
        txtImbp.setEnabled(true);
        txtEmpRefNoOld.setEnabled(true);
        btnCustomerIdFileOpen.setEnabled(true);
        //        added by Nikhil for Duplicate ID
        txtIDCardNo.setEnabled(true);
        chkDuplicateIDCardYN.setVisible(true);
        chkDuplicateIDCardYN.setEnabled(true);
        lblDuplicateIDCardYN.setVisible(true);
        drfEnable = true;
        /**
         * if(!cboShareType.getSelectedItem().equals("")){
         * observable.populateFeeDetails();
         * nomineeUi.setMaxNominee(observable.maxNominee);
         * }
         * updateFee();
         * ClientUtil.enableDisable(panShareTrans,true);
         * panShareTrans.setVisible(true);
         * rdoShareAddition.setEnabled(true);
         * rdoSharewithDrawal.setEnabled(true);
         * transactionUI.setButtonEnableDisable(true);
         * ClientUtil.enableDisable(panShareTrans,false);  commented by abi
         * if(shareValue()!=0.0){
         * transactionUI.setSourceScreen("REMITISSUE");
         * transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_EDIT);
         * }
         * ClientUtil.enableDisable(panShareTrans,true);
         * btnShareAcctDetNew.setEnabled(false);**/
        //        System.out.println("EDIT cbmAcctStatus.getKeyForSelected():" + observable.getCbmAcctStatus().getKeyForSelected());
        btnShareAppNo.setEnabled(true);
        nomineeUi.setViewType(viewType);
        drfNomineeUi.setViewType(viewType);
        nomineeUi.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        drfNomineeUi.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        btnEdit.setEnabled(false);
        btnCancel.setEnabled(true);
        btnSave.setEnabled(true);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        ClientUtil.enableDisable(panMobileBanking, true);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void populateFieldsEditMode(){
        if(!cboShareType.getSelectedItem().equals("")){
            observable.populateFeeDetails();
            nomineeUi.setMaxNominee(observable.maxNominee);
        }
        updateFee();
        ClientUtil.enableDisable(panShareTrans,true);
        panShareTrans.setVisible(true);
        rdoShareAddition.setEnabled(true);
        rdoSharewithDrawal.setEnabled(true);
        transactionUI.setButtonEnableDisable(true);
        ClientUtil.enableDisable(panShareTrans,false);
        if(shareValue()!=0.0){
            transactionUI.setSourceScreen("SHARE_SCREEN");
            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_EDIT);
        }
        ClientUtil.enableDisable(panShareTrans,true);
        //        added by Nikhil for Drf Applicable
        observable.checkForDrfApplicable();
//        if(observable.isDrfApplicableFlag()){//commented By Revathi.L
//            panDrfApplicablle.setVisible(true);
//        }
        panDrfApplicablle.setVisible(true);//Added By Revathi.L
        btnShareAcctDetNew.setEnabled(false);
        if(observable.getFullClosureReq()!=null && observable.getFullClosureReq().equals("Y")){
            txtShareValue.setEnabled(false);
            rdoSharewithDrawal.setText("Closure");
        }else{
          txtShareValue.setEnabled(true);
            rdoSharewithDrawal.setText("withDrawal");  
        }
    }

    /** To display a popUp window for viewing existing data */
    private void popUp() {
        observable.setStatus();
        final HashMap viewMap = new HashMap();
        //--- If Action type is EDIT or DELETE show the popup Screen
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||  observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
            ArrayList lst = new ArrayList();
            lst.add("SHARE ACCOUNT NO");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.SELECTED_BRANCH_ID,getSelectedBranchID());
            if(observable.getActionType()!=ClientConstants.ACTIONTYPE_EDIT){
                whereMap.put(CommonConstants.AUTHORIZESTATUS,"AUTHORIZESTATUS");
            }
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "viewAllShareAcct");
            if(observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE){
                viewMap.put(CommonConstants.MAP_NAME, "deleteAllShareAcct");
            }
            
            new ViewAll(this,viewMap).show();
        }
    }
    
    private StringBuffer validateForMaxShares(StringBuffer strBMandatory){
        int maxShare = 0;
        HashMap whereMap = new HashMap();
        whereMap.put("SHARE_TYPE", CommonUtil.convertObjToStr(observable.getCbmShareType().getKeyForSelected()));
        List list = ClientUtil.executeQuery("getShareProdDetails", whereMap);
        if(list.size()>0){
            HashMap hash = (HashMap) list.get(0);
            maxShare = CommonUtil.convertObjToInt(hash.get("MAXIMUM_SHARE"));
            //            int shareAcctDetAllSize = observable.shareAcctDetAll.size();
            //            int noOfShares = 0;
            //            HashMap singleRec;
            //            //--- adds the No. of shares
            //            for(int i=0;i<shareAcctDetAllSize;i++){
            //                singleRec = (HashMap)observable.shareAcctDetAll.get(String.valueOf(i));
            //                //--- Take account of only non-deleted shares
            //                if(!singleRec.get("Status").equals("DELETED")){
            //                    noOfShares = noOfShares + CommonUtil.convertObjToInt(singleRec.get("NoOfShares"));
            //                }
            //            }
            int noOfShares = getNoOfShares();
            //--- If  no. of shares exceeds the max.share, give alert
            //                if(noOfShares>maxShare){
            //                resourceBundle = new ShareRB();
            //                strBMandatory.append(resourceBundle.getString("NoOfShareExceed"));
            //                strBMandatory.append(maxShare);
            //                strBMandatory.append("\n");
            //                resourceBundle = null;
            //            }
        }
        return strBMandatory;
    }
    
    private void  updateOBFieldsForDiffAmt(){        
         if(tblShareAcctDet!=null && tblShareAcctDet.getRowCount()>0){
             for(int i=0;i<tblShareAcctDet.getRowCount();i++){
             double shaeVal=CommonUtil.convertObjToDouble(txtShareValue.getText()).doubleValue();
             String diff=CommonUtil.convertObjToStr(tblShareAcctDet.getValueAt(i, 4));
             String auth =CommonUtil.convertObjToStr(tblShareAcctDet.getValueAt(i, 5));
             if(diff.equals("DIFFERENTIAL") && !auth.equals("AUTHORIZED")){
                 observable.setTxtNoShares(CommonUtil.convertObjToStr(tblShareAcctDet.getValueAt(i, 2)));
                 observable.setTxtShareTotAmount(CommonUtil.convertObjToStr(tblShareAcctDet.getValueAt(i, 3)));
                 observable.setTxtShareDetShareAcctNo(CommonUtil.convertObjToStr(tblShareAcctDet.getValueAt(i, 0)));
                 
             } 
             } 
         }       
    }
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        if (rdoShareDifferentialAmount.isSelected() == true) {
            HashMap hashmap = new HashMap();
            String custid = CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(txtCustId.getText()));
            hashmap.put("CUST_ID", custid);  
            List lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashmap);
            if (lst1 != null && lst1.size() > 0) {
                ClientUtil.displayAlert("Customer is death marked please select another customerId");
                return;
            }
        } 
        String popData="";
         String popAddrData="";
        setModified(false);
        StringBuffer manData = new StringBuffer();
        HashMap supMap = new HashMap();
        supMap.put("SHARE_TYPE", CommonUtil.convertObjToStr(observable.getCbmShareType().getKeyForSelected()));
        List lstSupName = ClientUtil.executeQuery("getMandatoryDataForSelectItem", supMap);
        HashMap supMapData = new HashMap();
        if (lstSupName != null && lstSupName.size() > 0) {
            supMapData = (HashMap) lstSupName.get(0);
        }
        if(supMapData.get("MANDATORY_DATA") != null   &&  supMapData.containsKey("MANDATORY_DATA")){
         popData = CommonUtil.convertObjToStr(supMapData.get("MANDATORY_DATA"));
        }
        if(supMapData.get("MANDATORY_ADDR_DATA") != null   &&  supMapData.containsKey("MANDATORY_ADDR_DATA")){
         popAddrData = CommonUtil.convertObjToStr(supMapData.get("MANDATORY_ADDR_DATA"));
        }
        ArrayList dataList = new ArrayList();
        if (popData != null && popData.length() > 0 && !popData.equalsIgnoreCase("")) {
            String[] dataArray = popData.split(",");
            for (int i = 0; i < dataArray.length; i++) {
                supMap.put("CUST_ID", txtCustId.getText());
                supMap.put("FIELD", dataArray[i]);
                lstSupName = ClientUtil.executeQuery("getMandatoryDataCheckFunction", supMap);
                dataList.add(lstSupName.get(0));
            }
            if (popAddrData != null && popData.length() > 0 && !popAddrData.equalsIgnoreCase("")) {
                String[] dataAddrArray = popAddrData.split(",");
                for (int i = 0; i < dataAddrArray.length; i++) {
                    supMap.put("CUST_ID", txtCustId.getText());
                    supMap.put("FIELD", dataAddrArray[i]);
                    lstSupName = ClientUtil.executeQuery("getMandatoryDataCheckFunctionCustAddr", supMap);
                    dataList.add(lstSupName.get(0));
                }
            }
            if (dataList.size() > 0 || !dataList.equals("null")) {
                for (int i = 0; i < dataList.size(); i++) {
                    HashMap newmap = new HashMap();
                    newmap = (HashMap) (dataList.get(i));
                    if (newmap.containsKey("DATA") && newmap.get("DATA") != null) {
                        manData.append(newmap.get("DATA"));
                        manData.append("\n");
                    }
                }
                if (!(manData.toString().equals(""))) {
                    ClientUtil.displayAlert(manData + "for the  Customer");
//                    btnCancelActionPerformed(null);
//                    return;
                    //            setAuthBtnEnableDisable();
                    //            btnDelete.setEnabled(false);
                }
            }
        }
        //--- If ShareAccctDet New is pressed, then alert to save the data
        // String shareType=CommonUtil.convertObjToStr(cboShareType.getSelectedItem());
         String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panShareAcctNo);
        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
            if(CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).length()>0 && CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")){
                if (txtEmpRefNoNew.getText().equals("")) {
                mandatoryMessage=mandatoryMessage+ "Employer Ref No.(New) can not be Empty !!!";
                }
            }
            
            displayAlert(mandatoryMessage);
             return;
        }
        
        if(CommonConstants.SAL_REC_MODULE!=null && !CommonConstants.SAL_REC_MODULE.equals("") && CommonConstants.SAL_REC_MODULE.equals("Y")){
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                if (txtEmpRefNoNew.getText().equals("") || txtEmpRefNoOld.getText().equals("")) {
                    ClientUtil.showMessageWindow("Please enter New and Old Employee Reference Number!!!");
                    return;
                }
            }
        }
        if (mandatoryMessage.isEmpty()) {
             if(CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).length()>0 && CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")){
                if (txtEmpRefNoNew.getText().equals("")) {
                mandatoryMessage=mandatoryMessage+ "Employer Ref No.(New) can not be Empty !!!";
           displayAlert(mandatoryMessage);
           return;
           }
             }
        }
//        if(CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).length()>0 && CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")){
//            ClientUtil.showAlertWindow("Employer Ref No can not be Empty !!!");
//                        return;
//        }
         
            HashMap hashmap=new HashMap();
            if(tblJointAcctHolder!=null && tblJointAcctHolder.getRowCount()>0){
                int row= tblJointAcctHolder.getRowCount();
                for(int i=0;i<row;i++){
                    String custid=CommonUtil.convertObjToStr(tblJointAcctHolder.getValueAt(i,1));
                    hashmap.put("CUST_ID",custid);
                    hashmap.put("MEMBER_NO",custid);
                    List lst1=ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashmap);
                    if(lst1!=null && lst1.size()>0){
                        if(rdoShareAddition.isSelected()==true || rdoShareDifferentialAmount.isSelected()==true){
                        ClientUtil.displayAlert("Customer is death marked please select another customerId");
                        return;
                        }else if(rdoSharewithDrawal.isSelected()==true){ 
                            int confirm=ClientUtil.confirmationAlert("Customer is death marked please select another customerId Do you want to continue");
                        if(confirm==1){
                            return;
                        }
                        }
                    }
                }
                
            }
          
        int addShares = 0;
        int withdrawShares = 0;
        boolean val_flag = true;
        for (int i = 0; i < tblShareAcctDet.getRowCount(); i++) {
            if (tblShareAcctDet.getValueAt(i, 4).equals("ADD")) {
                addShares = addShares + CommonUtil.convertObjToInt(tblShareAcctDet.getValueAt(i, 2));
            }
            if (tblShareAcctDet.getValueAt(i, 4).equals("WITHDRAWAL")) {
                withdrawShares = withdrawShares + CommonUtil.convertObjToInt(tblShareAcctDet.getValueAt(i, 2));
            }
        }
           if (addShares == withdrawShares && rdoSharewithDrawal.isSelected()) {
            HashMap shareAcctNoMap = new HashMap();
            shareAcctNoMap.put("MEMBER_NO", CommonUtil.convertObjToStr(txtShareAcctNo.getText()));
            List balDivPayList = ClientUtil.executeQuery("getUnpaidShareDetails", shareAcctNoMap);
            if (balDivPayList != null && balDivPayList.size() > 0) {
                displayAlert("Share dividend payment is pending for this share holder!!!!!!!!!!");
                //continue;
            }
            shareAcctNoMap = new HashMap();
            shareAcctNoMap.put("CUST_ID", CommonUtil.convertObjToStr(txtCustId.getText()));
            List balPayList = ClientUtil.executeQuery("getSBAccoutDebitBalanceForTOD", shareAcctNoMap);
            if (balPayList != null) {
                for (int k = 0; k < balPayList.size(); k++) {
                    HashMap singMap = (HashMap) balPayList.get(k);
                    if (singMap != null && singMap.containsKey("AVAILABLE_BALANCE")) {
                        double avilableBalance = CommonUtil.convertObjToDouble(singMap.get("AVAILABLE_BALANCE"));
                        if (avilableBalance < 0) {
                            ClientUtil.showAlertWindow("Over Due Amount For SB Account is  not Cleared!!!");
                            observable.setAllowedTransactionDetailsTO(null);
                            val_flag = false;
                            break;

                        }
                    }
                }
            }
            if (!val_flag) {
                return;
            }
            int confirm = ClientUtil.confirmationAlert("Do you want to close the share account");
            if (confirm == 0) {
                observable.setShareStatus("CLOSED");
            }
        }
        
        final String shareType = CommonUtil.convertObjToStr(((ComboBoxModel) (cboShareType.getModel())).getKeyForSelected());
        HashMap hmap = new HashMap();
        String Ratification = "N";
        hmap.put("SHARE_TYPE", shareType);
        List list = ClientUtil.executeQuery("getShareProductRatification", hmap);
        if (list != null && list.size() > 0) {
            hmap = null;

            hmap = (HashMap) list.get(0);
            String ratification = CommonUtil.convertObjToStr(hmap.get("RATIFICATION_REQUIRED"));
            String ratif = "Y";

            if (ratification.equals(ratif)) {
                observable.setTxtRatification(ratif);
            } else {
                observable.setTxtRatification(Ratification);
            }
        } else {
            observable.setTxtRatification(Ratification);
        }
        /* if(ratification.equals(ratif)){
             if(txtApplicationNo.getText().length()<=0 || CommonUtil.convertObjToStr(txtApplicationNo.getText()).equals("New")){
                 observable.setTxtRatification(ratif);
                 int c = ClientUtil.confirmationAlert("Application No not Entered ,System will Generate ,Do you want Continue");
                 int d= 0;
                 if(c!=d)
         
                     return;
         
             }
         }
         else
        if(txtShareAcctNo.getText().length()<=0 || CommonUtil.convertObjToStr(txtShareAcctNo.getText()).equals("New")){
            int c = ClientUtil.confirmationAlert("Share No not Entered ,System will Generate ,Do you want Continue");
            int d= 0;
            if(c!=d)
                return;
             }*/
        
        if (rdoShareDifferentialAmount.isSelected()) {
            if ((transactionUI.getOutputTO() == null || (transactionUI.getOutputTO()).size() == 0) && !observable.getDeletion().equals("Deleted")) {
                ClientUtil.displayAlert("There are no transaction details");
                observable.setAllowedTransactionDetailsTO(null);

            } else {
                observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                observable.setDueAmt("True");
                try {
                    updateOBFields();
                    updateOBFieldsForDiffAmt();
                     observable.setProxyReturnMap(null);
                      CommonUtil comm = new CommonUtil();
                    final JDialog loading = comm.addProgressBar();
                    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                        
                        @Override
                        protected Void doInBackground() throws InterruptedException, Exception /** Execute some operation */
                        {
                           observable.doAction(nomineeUi.getNomineeOB(),drfNomineeUi.getNomineeOB());    
                         //  System.out.println("result 111 OB : ");// To perform the necessary operation depending on the Action type...
                            return null;
                        }

                        @Override
                        protected void done() {
                            loading.dispose();
                        }
                    };
                    worker.execute();
                    loading.show();
                    try {
                        worker.get();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                     
                     
                   

                    if (observable.getProxyReturnMap()!=null) {
                        if (observable.getProxyReturnMap().containsKey("CASH_TRANS_LIST") || observable.getProxyReturnMap().containsKey("TRANSFER_TRANS_LIST")) {
                            displayTransDetail(observable.getProxyReturnMap());
                            observable.setProxyReturnMap(null);
                            
                        }
                       
                    }
                     btnCancelActionPerformed(null);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            
            
        } else if (shareAcctDetNewPressed == false && !rdoShareDifferentialAmount.isSelected()) {
            resourceBundle = new ShareRB();
            final String shareAcctMandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panShareDet);
            //        final String othersMandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panOtherDet);
            StringBuffer strBMandatory = new StringBuffer();
            
            int transactionSize = 0 ;
            //        //System.out.println("transactionUI.getOutputTO() : " + transactionUI.getOutputTO());
            //            if (!(viewType.equals(ClientConstants.VIEW_TYPE_DELETE)|| viewType.equals(ClientConstants.VIEW_TYPE_AUTHORIZE) || viewType.equals(ClientConstants.VIEW_TYPE_SEND_TO_RESOLUTION))){
            // If we are implementing Truetransact branchwise, Send to Resolution & Share Resolution option in HO are not possible
            // By Rajesh
            if(/*rdoSharewithDrawal.isSelected()==true && */(transactionUI.getOutputTO() == null || (transactionUI.getOutputTO()).size() == 0)){
                //            //System.out.println("observable:" + observable);
                //            //System.out.println("resourceBundle:" + resourceBundle);
                //            //System.out.println("resourceBundleNoRecords:" + resourceBundle.getString("NoRecords"));
                //            observable.showAlertWindow(resourceBundle.getString("NoRecords"));
                strBMandatory.append(resourceBundle.getString("NoRecords"));
                strBMandatory.append("\n");
                observable.setAllowedTransactionDetailsTO(null);
                //System.out.println("in null chk of UI");
            } else { //if(rdoShareAddition.isSelected()==false){
                transactionSize = (transactionUI.getOutputTO()).size();
                observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
            }
            //            }
            //            }
            //--- checks the Mandatory data for ShareAccount screen
            if(shareAcctMandatoryMessage.length()>0){
                strBMandatory.append(shareAcctMandatoryMessage);
            }
            
            //--- if the Constitution JointAccount is selected and there is no joint account holder alert user
            if(CommonUtil.convertObjToStr(observable.getCbmConstitution().getKeyForSelected()).equals("JOINT_ACCOUNT") && tblJointAcctHolder.getRowCount()<2){
                strBMandatory.append("Select the Joint Account holder");
                strBMandatory.append("\n");
            }
            
            if(cboMemDivPayMode.getSelectedItem().equals("")){
                strBMandatory.append("Please select the  Dividend pay Mode!!!");
                strBMandatory.append("\n");
            }
            
            if(chkNotEligibleStatus.isSelected()==true){
                if(tdtNotEligiblePeriod.getDateValue() == null || tdtNotEligiblePeriod.getDateValue().length()==0){
                    strBMandatory.append(resourceBundle.getString("NonEligiblePeriodEmpty"));
                    strBMandatory.append("\n");
                }
            }
            
            //--- checks the Mandatory data for ShareAccountDetails
            if(tblShareAcctDet.getRowCount()==0 && !observable.membershipType.equals("NOMINAL")){
                strBMandatory.append(resourceBundle.getString("ShareAccountDetailsEmpty"));
                strBMandatory.append("\n");
            }
            //--- To Check if the Total Share of the Nominee(s) is 100% or not...
            String alert = nomineeUi.validateData();
            if(!alert.equalsIgnoreCase("")){
                strBMandatory.append(alert);
            }
            //--- Validates Maximum no.of shares for an account
            validateForMaxShares(strBMandatory);
            //---- Validates for the Issued capital
            validateForIssuedCapital(strBMandatory);
            
            String strMandatory = strBMandatory.toString();
            
            HashMap exMap=new HashMap();
            exMap.put("SHARE_ACCT_NO",txtShareAcctNo.getText());
            List lst =ClientUtil.executeQuery("getShareExists", exMap);
            if(lst!=null && lst.size()>0){
                strBMandatory.append("Share No Already Exists");
                
            }
            
            //--- checks whether the Mandatory fields are entered
            //            if(strMandatory.length()>0){        //--- if all the mandatory fields are not entered,
            //                CommonMethods.displayAlert(strMandatory);     //--- display the alert
            //            }else
            //                if(strMandatory.length()==0){ //--- if all the values are entered, save the data
            //Call transaction screen here
            //If transactions exist, proceed to save them
            if (transactionSize  > 0/* && rdoSharewithDrawal.isSelected()==true*/) {
                String auth="";
                int row=tblShareAcctDet.getSelectedRow();
                if(row>=0){
                    auth=CommonUtil.convertObjToStr(tblShareAcctDet.getValueAt(row, 5));
                }
                if (!transactionUI.isBtnSaveTransactionDetailsFlag() && !auth.equals("AUTHORIZED")) {
                    observable.showAlertWindow(resourceBundle.getString("saveInTxDetailsTable"));
                    return;
                } else if (transactionUI.isBtnSaveTransactionDetailsFlag() && !auth.equals("AUTHORIZED")) {
                    int noOfShares = getNoOfShares();
                    double transTotalAmt = CommonUtil.convertObjToDouble(transactionUI.getTransactionOB().getLblTotalTransactionAmtVal()).doubleValue();
                    double shareTotalAmt = 0;
                    //                        double shareTotalAmt = getTotShareTransAmt();
                    if (rdoSharewithDrawal.isSelected()==true) {
                        shareTotalAmt = shareValue();
                    } else {
                        shareTotalAmt = getTotShareTransAmt();
                        //                            CommonUtil.convertObjToDouble(observable.getTxtShareTotAmount()).doubleValue();
                    }
                    //                    added here by nikhil for subsidy
                    if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW
                            && (CommonUtil.convertObjToStr(observable.getCaste()).equals("SC") || CommonUtil.convertObjToStr(observable.getCaste()).equals("ST")) && subsidyFlag) {
                        shareTotalAmt = shareTotalAmt - CommonUtil.convertObjToDouble(observable.getGovtsShare()).doubleValue();
                        subsidyGiven = true;
                    }else{
                        subsidyGiven = false;
                    }
                    double drfTransAmt = 0.0;
                    if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW){
                        drfApplicable = CommonUtil.convertObjToStr(txtShareAcctNo.getText());
                        if(chkDrfApplicableYN.isSelected()){
                            drfRecieptFlag = true;
                            if(observable.getProductAmount() != null){
                                drfTransAmt = CommonUtil.convertObjToDouble(observable.getProductAmount()).doubleValue();
                            }
                        }else{
                            drfRecieptFlag = false;
                        }
                    }
                    //                        CommonUtil.convertObjToDouble(observable.getTxtShareTotAmount()).doubleValue();
                    //                        + CommonUtil.convertObjToDouble(txtShareFee.getText()).doubleValue() * noOfShares
                    //                        + CommonUtil.convertObjToDouble(txtApplFee.getText()).doubleValue()
                    //                        + CommonUtil.convertObjToDouble(txtShareAmt.getText()).doubleValue() * noOfShares;
//                    added by nikhil for DRF Applicable
//                    if(drfRecieptFlag){
//                        shareTotalAmt+=drfTransAmt;
//                    }
                    if (ClientUtil.checkTotalAmountTallied(shareTotalAmt, transTotalAmt) == false){
                        ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NOT_TALLY));
                        return;
                    } else {
                        observable.noOfShares = getNoOfShares();
                        savePerformed();
                    }
                }
            }
            //            } else {
            // If we are implementing Truetransact branchwise, Send to Resolution & Share Resolution option in HO are not possible
            // By Rajesh  (The following if commented)
            //                    if ((viewType.equals(ClientConstants.VIEW_TYPE_EDIT)||viewType.equals(ClientConstants.VIEW_TYPE_DELETE)|| viewType.equals(ClientConstants.VIEW_TYPE_AUTHORIZE) || viewType.equals(ClientConstants.VIEW_TYPE_SEND_TO_RESOLUTION))){
            observable.noOfShares = getNoOfShares();
            
            String del=observable.getDeletion();
            if (transactionSize>0) {
                savePerformed();
            }else{
                if(del.equals("Deleted") || save==false){
                    savePerformed();
                }else{
                    ClientUtil.displayAlert("There are no transaction details");
                    return;
                }
            }
            drfRecieptFlag = false;
            //                    }
            //            }
            
            //End of Transaction call
            
            //            }
            resourceBundle = null;
        } else {
            CommonMethods.displayAlert(resourceBundle.getString("saveAcctDet"));
        }
        btnShareAppNo.setEnabled(false);
        btnCancelActionPerformed(null);
        TrueTransactMain.populateBranches();
        TrueTransactMain.selBranch = ProxyParameters.BRANCH_ID;
        observable.setSelectedBranchID(ProxyParameters.BRANCH_ID);
        setSelectedBranchID(ProxyParameters.BRANCH_ID);
        btnSave.setEnabled(false);
        btnCancel.setEnabled(false);
    }//GEN-LAST:event_btnSaveActionPerformed
    //---- Calculate the No.Of  Shares

    private double shareValue(){
        double shareCount=0.0;
        if(tblShareAcctDet.getRowCount()>0){
            for (int i=0;i<tblShareAcctDet.getRowCount();i++){
                
                
                if(!CommonUtil.convertObjToStr(tblShareAcctDet.getValueAt(i,5)).equals("AUTHORIZED")){
                    if (rdoShareAddition.isSelected()) {
                        shareCount=shareCount+CommonUtil.convertObjToDouble(tblShareAcctDet.getValueAt(i,3)).doubleValue();
                    }
                    shareCount=shareCount+CommonUtil.convertObjToDouble(tblShareAcctDet.getValueAt(i,3)).doubleValue();
                }
                
            }
            
        }
        
        return  shareCount;
        
        
    }

    private int getNoOfShares(){
        int noOfShares = 0;
        //---Get the No.of shares
        if(observable.shareAcctDetAll != null){
            int shareAcctDetAllSize = observable.shareAcctDetAll.size();
            HashMap singleRec;
            //--- adds the No. of shares
            for(int i=0;i<shareAcctDetAllSize;i++){
                singleRec = (HashMap)observable.shareAcctDetAll.get(String.valueOf(i));
                //--- Take account of only non-deleted shares
                if(singleRec!=null&& singleRec.size()>0){
                    if(!singleRec.get("Status").equals("DELETED")){
                        if (singleRec.get("ShareNoFrom").equals("ADD")) {
                            noOfShares = noOfShares + CommonUtil.convertObjToInt(singleRec.get("NoOfShares"));
                        } else if (singleRec.get("ShareNoFrom").equals("WITHDRAWAL")) {
                            noOfShares = noOfShares - CommonUtil.convertObjToInt(singleRec.get("NoOfShares"));
                    }
                }
            }
        }
        }
        return noOfShares;
    }
    
    private double getTotShareTransAmt(){
        double noOfShares = 0;
        //---Get the No.of shares
        if(observable.shareAcctDetAll != null){
            int shareAcctDetAllSize = observable.shareAcctDetAll.size();
            HashMap singleRec;
            //--- adds the No. of shares
            
            
            
            
            for(int i=0;i<shareAcctDetAllSize;i++){
                singleRec = (HashMap)observable.shareAcctDetAll.get(String.valueOf(i));
                //--- Take account of only non-deleted shares
                if(singleRec!=null&& singleRec.size()>0){
                    if(!singleRec.get("Status").equals("DELETED")){
                        if (singleRec.get("ShareNoFrom").equals("ADD")) {
                            noOfShares = noOfShares + CommonUtil.convertObjToDouble(singleRec.get("SharesTotValue")).doubleValue();
                        } else {
                            noOfShares = noOfShares - CommonUtil.convertObjToDouble(singleRec.get("SharesTotValue")).doubleValue();
                    }
                }
            }
        }
        }
        return noOfShares;
    }
    
    /* To display an alert message if any of the mandatory fields is not inputed */
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    private StringBuffer validateForIssuedCapital(StringBuffer strBMandatory){
        //--- If there are no previous alert message thenc check, else don't do
        if(strBMandatory.length()==0){
            HashMap whereMap = new HashMap();
            whereMap.put("SHARE_TYPE", CommonUtil.convertObjToStr(observable.getCbmShareType().getKeyForSelected()));
            List listProd = ClientUtil.executeQuery("getShareProdDetails", whereMap);
            int issuedCapital = 0;
            int faceValue = 0;
            int noOfShares = 0;
            int presentCapital = 0;
            if(listProd.size()>0){
                HashMap hash = (HashMap) listProd.get(0);
                faceValue = CommonUtil.convertObjToInt(hash.get("FACE_VALUE"));
                issuedCapital = CommonUtil.convertObjToInt(hash.get("ISSUED_CAPITAL"));
            }
            whereMap = new HashMap();
            whereMap.put("SHARE_TYPE", CommonUtil.convertObjToStr(observable.getCbmShareType().getKeyForSelected()));
            whereMap.put("SHARE_ACCT_NO", txtShareAcctNo.getText());
            List list = (List) ClientUtil.executeQuery("getNoOfShares", whereMap);
            int listSize = list.size();
            if(listSize>0){
                HashMap noOfSharesHash = (HashMap)list.get(0);
                noOfShares = CommonUtil.convertObjToInt(noOfSharesHash.get("SUM"));
            }
            HashMap singleRec;
            int shareAcctDetAllSize = observable.shareAcctDetAll.size();
            //--- adds the No. of shares
            noOfShares = getNoOfShares();
            //            for(int i=0;i<shareAcctDetAllSize;i++){
            //                singleRec = (HashMap)observable.shareAcctDetAll.get(String.valueOf(i));
            //                //--- Take account of only non-deleted shares
            //                if(!singleRec.get("Status").equals("DELETED")){
            //                    noOfShares = noOfShares + CommonUtil.convertObjToInt(singleRec.get("NoOfShares"));
            //                }
            //            }
            //---- calc. presentSubscribeCapital
            presentCapital = noOfShares*faceValue;
            if(presentCapital>issuedCapital){
                ShareRB resourceBundle = new ShareRB();
                strBMandatory.append(resourceBundle.getString("MoreThanIssuedCapital"));
                strBMandatory.append(issuedCapital);
                strBMandatory.append("\n");
                resourceBundle = null;
            }
        }
        return strBMandatory;
    }
    
    private void savePerformed(){
        try{
            updateOBFields();
      
            String ShareDetShareNoFrom=observable.getTxtShareDetShareNoFrom();
            observable.setResult(observable.getActionType());
            HashMap hmap=null;
            observable.setProxyReturnMap(hmap);
//            added by Nikhil for DRF Applicable
            if (drfRecieptFlag && drfApplicable.length() > 0) {
                observable.setDrfApplicableFlag(true);
            }
            if (chkDrfApplicableYN.isSelected() && drfExist == false) {
                observable.setDrfApplicableFlag(true);
            } else {
                observable.setDrfApplicableFlag(false);
            }
//            boolean result = false;
            CommonUtil comm = new CommonUtil();
            final JDialog loading = comm.addProgressBar();
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
//                private boolean result=false;//inside declaration

                @Override
                protected Void doInBackground() throws InterruptedException /**
                 * Execute some operation
                 */
                {
                    try {
//                        result = result;
                        // System.out.println("result before OB : "+result);
                        result = observable.doAction(nomineeUi.getNomineeOB(), drfNomineeUi.getNomineeOB());              // To perform the necessary operation depending on the Action type...                        
                        //System.out.println("result from OB : "+result);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void done() {
                    loading.dispose();
                }
            };
            worker.execute();
            loading.show();
            try {
                worker.get();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            
            System.out.println("result final : "+result);
            observable.setTxtShareDetShareNoFrom(ShareDetShareNoFrom);
            if (result) {
                //                super.removeEditLock(txtShareAcctNo.getText());
                if (observable.getProxyReturnMap() != null) {
                    if (observable.getProxyReturnMap().containsKey("CASH_TRANS_LIST") || observable.getProxyReturnMap().containsKey("TRANSFER_TRANS_LIST")) {

                        displayTransDetail(observable.getProxyReturnMap());
                        observable.setProxyReturnMap(null);

                    }
                }
                //                added by Nikhil
                if (drfRecieptFlag && drfApplicable.length() > 0) {
                    //                    commented here since it should do internal transaction and not display a new window
                    //                    drfTransactionUI = new DrfTransactionUI();
                    //                    com.see.truetransact.ui.TrueTransactMain.showScreen(drfTransactionUI);
                    //                    drfTransactionUI.drfAccountCreationForShare(txtShareAcctNo.getText());
                    //                    drfRecieptFlag = false;
                }

                observable.makeToNull();
                // If we are implementing Truetransact branchwise, Send to Resolution & Share Resolution option in HO are not possible
                // By Rajesh
                //                transactionUI.resetObjects();
                viewType = ClientConstants.VIEW_TYPE_CANCEL;
                cancelOperation();
                nomineeUi.resetTable();
                nomineeUi.resetNomineeData();
                nomineeUi.getNomineeOB().ttNotifyObservers();
                nomineeUi.disableNewButton(false);
            }
            observable.setResultStatus();
            txtNextAccNo.setText("");
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void displayTransDetail(HashMap proxyResultMap) {
       
        String cashDisplayStr = "Cash Transaction Details...\n";
        String transferDisplayStr = "Transfer Transaction Details...\n";
        String displayStr = "";
        String transId = "";
        String linkBatchId = "";
        String transType = "";
        Object keys[] = proxyResultMap.keySet().toArray();
        int cashCount = 0;
        int transferCount = 0;
        List tempList = null;
        HashMap transMap = null;
        String actNum = "";
        String susidyString = "Customer Eligible For Government Subsidy \n";
        for (int i=0; i<keys.length; i++) {
            if (proxyResultMap.get(keys[i]) instanceof String) {
                continue;
            }
            tempList = (List)proxyResultMap.get(keys[i]);
            if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH")!=-1) {
                for (int j=0; j<tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j==0) {
                        transId = (String)transMap.get("SINGLE_TRANS_ID");
                        linkBatchId  = (String)transMap.get("LINK_BATCH_ID");
                    }
                    cashDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if(actNum != null && !actNum.equals("")){
                        cashDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    }else{
                        List lst=ClientUtil.executeQuery("getACHeadDetails", transMap);
                        if(lst!=null && lst.size()>0){
                            HashMap hmap=(HashMap)lst.get(0);
                            String desc=CommonUtil.convertObjToStr(hmap.get("A/C HEAD DESCRIPTION"));
                            cashDisplayStr += "   Account Head Desc: "+desc;
                        }
                        cashDisplayStr += "   Account Head : " + transMap.get("AC_HD_ID")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    }
                }
                cashCount++;
            } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER")!=-1) {
                for (int j=0; j<tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j==0) {
                        transId = (String)transMap.get("SINGLE_TRANS_ID");
                        linkBatchId  = (String)transMap.get("LINK_BATCH_ID");
                    }
                    transferDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Batch Id : " + transMap.get("BATCH_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if(actNum != null && !actNum.equals("")){
                        transferDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    }else{
                        List lst=ClientUtil.executeQuery("getACHeadDetails", transMap);
                        if(lst!=null && lst.size()>0){
                            HashMap hmap=(HashMap)lst.get(0);
                            String desc=CommonUtil.convertObjToStr(hmap.get("A/C HEAD DESCRIPTION"));
                            transferDisplayStr += "   Account Head Desc: "+desc;
                        }
                        transferDisplayStr += "   Account Head : " + transMap.get("AC_HD_ID")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    }
                }
                transferCount++;
            }
        }
        //        added by Nikhil for subsidy
        if(subsidyFlag && subsidyGiven){
            displayStr+=susidyString;
            subsidyFlag = false;
            subsidyGiven = false;
        }
        if(cashCount>0){
            displayStr+=cashDisplayStr;
        }
        if(transferCount>0){
            displayStr+=transferDisplayStr;
        }
        if(!displayStr.equals("")) {
            ClientUtil.showMessageWindow(""+displayStr);
        }
        //        ClientUtil.showMessageWindow(""+displayStr);
        if(!viewType.equals(ClientConstants.VIEW_TYPE_AUTHORIZE)){
        int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null,"Do you want to print?", CommonConstants.WARNINGTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        if (yesNo==0) {
            TTIntegration ttIntgration = null;
            HashMap paramMap = new HashMap();
            paramMap.put("TransId", transId);
            paramMap.put("TransDt", observable.getCurDate());
            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
            ttIntgration.setParam(paramMap);
            //            if (((String)TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase().lastIndexOf("POLPULLY")!=-1) {
            //                ttIntgration.integrationForPrint("ReceiptPayment");
            //            } else {
            String reportName = "";
            if(transferCount>0){
                reportName = "ReceiptPayment";
            } else if (observable.getTxtShareDetShareNoFrom().equals("WITHDRAWAL")) {
                reportName = "CashPayment";
            } else {
                reportName = "CashReceipt";
            }
            ttIntgration.integrationForPrint(reportName, false);
         }
        //Share certificate print
         printShareCertificate(transId,linkBatchId);
        }
    }
    
    private void printShareCertificate(String transId,String linkBatchId){
        final String shareType = CommonUtil.convertObjToStr(((ComboBoxModel)(cboShareType.getModel())).getKeyForSelected());
        if(shareType!=null && shareType.length()>0){
            HashMap shareMap = new HashMap();
            shareMap.put("SHARE_TYPE", shareType);
            List shareProd = ClientUtil.executeQuery("getShareCertificatePrint", shareMap);            
            if(shareProd!=null && shareProd.size()>0){
                shareMap = (HashMap) shareProd.get(0);
                if(shareMap!=null && shareMap.get("SHARE_CERTIFICATE_PRINT")!=null && shareMap.get("SHARE_CERTIFICATE_PRINT").equals("Y")){
                    TTIntegration ttIntgration = null;
                    HashMap paramMap = new HashMap();
                    paramMap.put("MemberNo", txtShareAcctNo.getText());
                    paramMap.put("Trans_Dt", currDt.clone());   
                    paramMap.put("Sl_No", linkBatchId.substring(linkBatchId.indexOf("_")+1,linkBatchId.length()));                    
                    ttIntgration.setParam(paramMap);
                    if(viewType.equals(ClientConstants.VIEW_TYPE_EDIT) && rdoSharewithDrawal.isSelected()){
                        int yesNo = 0;
                        String[] options = {"Yes", "No"};
                        yesNo = COptionPane.showOptionDialog(null,"Do you want to print Share Certificate?", CommonConstants.WARNINGTITLE,
                        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                        null, options, options[0]);                        
                        if (yesNo==0) {
                            ttIntgration.integrationForPrint("ShareCertificate", false);   
                        }
                    }else{
                        ttIntgration.integrationForPrint("ShareCertificate", false);
                    }
                }
            }                    
        }        
    }
    private void tblShareAcctDetRowMouseClicked(int intShareAcctDetNo){
        try {
            if(!(viewType.equals(ClientConstants.VIEW_TYPE_AUTHORIZE) && shareAcctDetFilled == true)){
                isTblSharAcctClicked = true;
                List translist=observable.getTrans();
                if (translist == null) {
                    observable.shareAcctDetStatus = CommonConstants.STATUS_CREATED;
                } else if (translist.size() != tblShareAcctDet.getRowCount()) {
                    observable.shareAcctDetStatus = CommonConstants.STATUS_CREATED;
                }else{
                    observable.shareAcctDetStatus = CommonConstants.STATUS_MODIFIED;
                }
                int rowcount = -1;
                if(intShareAcctDetNo==-1){
                    
                    rowcount = (int)(Integer.parseInt(CommonUtil.convertObjToStr(tblShareAcctDet.getValueAt(tblShareAcctDet.getSelectedRow(),0))));
                } else {
                    //                    rowcount = (int)(Integer.parseInt(CommonUtil.convertObjToStr(tblShareAcctDet.getValueAt(tblShareAcctDet.getSelectedRow(),0))));
                    rowcount = intShareAcctDetNo;
                    //                    if(observable.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE)
                    //                    rowcount=shareRowCount(intShareAcctDetNo);
                    //                    rowcount = intShareAcctDetNo;
                    
                    
                }
//                tblShareAcctDetRowSelected(rowcount-1);
                if(viewType.equals(ClientConstants.VIEW_TYPE_AUTHORIZE)){
                    rowcount=tblShareAcctDet.getRowCount()-1;
                }else{
                rowcount=tblShareAcctDet.getSelectedRow();
                }
                tblShareAcctDetRowSelected(rowcount);
                //                txtShareDetShareAcctNo.setEnabled(false);
                // The following 4 lines commented by Rajesh
                //                txtShareDetShareNoFrom.setEnabled(false);
                //                txtShareDetShareNoTo.setEnabled(false);
                //                txtResolutionNo1.setEnabled(false);
                //                tdtShareDetIssShareCert.setEnabled(false);
                //---Sets the getSelectedRowCount to the Row that is selected
                if(observable.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType()==ClientConstants.ACTIONTYPE_REJECT){
                    rowcount=shareRowCount(intShareAcctDetNo);
                    rowcount=rowcount-1;
                    observable.shareAcctDetGetSelectedRowCount=rowcount;
                } else {
                    observable.shareAcctDetGetSelectedRowCount = tblShareAcctDet.getSelectedRow();
                }
                
                if(CommonUtil.convertObjToStr(tblShareAcctDet.getValueAt(observable.shareAcctDetGetSelectedRowCount,4)).equals("ADD")){
                    rdoShareAddition.setSelected(true);
                    rdoSharewithDrawal.setSelected(false);
                    rdoShareDifferentialAmount.setSelected(false);
                    //                    tabTransaction.remove(panTransaction);
                    //                     tabTransaction.remove(1);
                }else if(CommonUtil.convertObjToStr(tblShareAcctDet.getValueAt(observable.shareAcctDetGetSelectedRowCount,4)).equals("WITHDRAWAL")){
                    rdoSharewithDrawal.setSelected(true);
                    rdoShareAddition.setSelected(false);
                    rdoShareDifferentialAmount.setSelected(false);
                    //                    tabTransaction.add(panTransaction,"TransactionDetails");
                }else{
                    rdoShareAddition.setSelected(false);
                    rdoSharewithDrawal.setSelected(false);
                    rdoShareDifferentialAmount.setSelected(true);
                }
                //--- To disable all the fields in Authorize status and sendToResol.
                if(viewType.equals(ClientConstants.VIEW_TYPE_DELETE) || viewType.equals(ClientConstants.VIEW_TYPE_AUTHORIZE) || viewType.equals(ClientConstants.VIEW_TYPE_SEND_TO_RESOLUTION)){
                    ClientUtil.enableDisable(panShareAcctDetails, false);
                    setBtnShareAcctDet(false);
                }
                //--- To disable the No.Of Shares in Edit mode
                //--- coz it shouldn't be allowed for edition after transaction
                //--- Load the Transaction Details for that ShareSubAccnt. if present
                if (viewType.equals(ClientConstants.VIEW_TYPE_EDIT)||viewType.equals(ClientConstants.VIEW_TYPE_DELETE)|| viewType.equals(ClientConstants.VIEW_TYPE_AUTHORIZE) || viewType.equals(ClientConstants.VIEW_TYPE_SEND_TO_RESOLUTION)){
                    // The following if condition given by Rajesh
                    //                    if (viewType.equals(ClientConstants.VIEW_TYPE_EDIT) && observable.getAuthorize().equals(""))
                    ////                        txtShareDetNoOfShares.setEnabled(true);
                    //                    else
                    ////                        txtShareDetNoOfShares.setEnabled(false);
                    String where = "";
                    if(intShareAcctDetNo==-1){
                        //                        where = txtShareDetShareAcctNo.getText() + "_" + CommonUtil.convertObjToStr(tblShareAcctDet.getValueAt(tblShareAcctDet.getSelectedRow(),0));
                    } else {
                        //                        where = txtShareDetShareAcctNo.getText() + "_" + String.valueOf(intShareAcctDetNo);
                    }
                    HashMap whereMap = new HashMap();
                    whereMap.put("BATCH_ID", where);
                    //                    List list = (List)ClientUtil.executeQuery("getSelectRemittanceIssueTransactionHash", whereMap);
                    
                    if(viewType.equals(ClientConstants.VIEW_TYPE_EDIT)) {
                        List transList=observable.getTrans();
                        int n=0;
                        int k= transList.size();
                        
                       int no= CommonUtil.convertObjToInt(tblShareAcctDet.getValueAt(tblShareAcctDet.getSelectedRow(),0));
                        //  if(observable.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE ){
                        
                        //   transactionTo=(TransactionTO)transList.get(rowcount);
                        //   }
                        //   else{
                        
                        String shno =txtShareAcctNo.getText();
                        String shpllno=txtApplicationNo.getText();
                        String shareno=shno+"_"+no;
                        String sapplno=shpllno+"_"+no;
                        if (transList != null) {
                            for (int i = 0; i < k; i++) {
                                transactionTo = (TransactionTO) transList.get(i);
                                if (transactionTo != null && !transactionTo.getBatchId().equals("")) {
                                    String batchid = transactionTo.getTransId();
                                    transactionTo = null;
                                    if (batchid.equals(shareno) || batchid.equals(sapplno)) {
                                        transactionTo = (TransactionTO) transList.get(i);
                                        i = k + 1;
                                        observable.shareAcctDetStatus = CommonConstants.STATUS_MODIFIED;
                                    } else {
                                        observable.shareAcctDetStatus = CommonConstants.STATUS_CREATED;
                                    }
                                }
                            }
                        }
                        HashMap h=new HashMap();
                        ArrayList arraylst=new ArrayList();
                        arraylst.add( transactionTo);
                        observable.setTransDetails(arraylst);
                        arraylst=null;
                        
                    }
                    
                    
                } else {
                    //                    txtShareDetNoOfShares.setEnabled(true);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    private void tblShareAcctDetRowSelected(int rowSelected){
        setShareAcctDetNewAndTblPress();
        btnShareAcctDetDel.setEnabled(true);
        observable.populateShareAcctDetFields(rowSelected);
        updateShareAcctDet();
    }
    
    private void btnShareAcctDetDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShareAcctDetDelActionPerformed
        shareAcctDetNewPressed = false;
        if(tblShareAcctDet!=null && tblShareAcctDet.getRowCount()>0){
            String diff=CommonUtil.convertObjToStr(tblShareAcctDet.getValueAt(tblShareAcctDet.getSelectedRow(), 4));
            String DetNo=CommonUtil.convertObjToStr(tblShareAcctDet.getValueAt(tblShareAcctDet.getSelectedRow(), 0));           
            observable.setShareAcctStatusDueAmt("DELETE");                        
            observable.setShareAcctStatusAddWithdra(DetNo);
        }
        
        updateOBShareAcctDet();
        observable. getTransDetails();
        observable.setDeletion("Deleted");
        int rowSelected=tblShareAcctDet.getSelectedRow();
        observable.populateShareAcctDetFields(rowSelected);
        observable.getShareDet();
        //observable.ShareAcctDetData();
        observable.shareAcctDetStatus = CommonConstants.STATUS_DELETED;
        setShareAcctDetFields(false);
        setBtnShareAcctDet(false);
        btnShareAcctDetNew.setEnabled(true);
        observable.delShareAcctDet(0,true);
        observable.resetShareAcctDet();
        updateShareAcctDet();
        totalShareCount();
        
        transactionUI.resetObjects();
        
    }//GEN-LAST:event_btnShareAcctDetDelActionPerformed
    private void updateShareAcctDet(){
        //        txtShareDetShareAcctNo.setText(observable.getTxtShareDetShareAcctNo());
        //        txtShareDetShareNoFrom.setText(observable.getTxtShareDetShareNoFrom());
        //        txtShareDetShareNoTo.setText(observable.getTxtShareDetShareNoTo());
        //        txtShareDetNoOfShares.setText(observable.getTxtShareDetNoOfShares());
        txtResolutionNo1.setText(observable.getTxtResolutionNo1());
        tdtShareDetIssShareCert.setDateValue(observable.getTdtShareDetIssShareCert());
        txtShareValue.setText(observable.getTxtShareValue());
        txtTotShareFee.setText(observable.getTxtTotShareFee());
        txtShareApplFee.setText(observable.getTxtShareApplFee());
        txtShareMemFee.setText(observable.getTxtShareMemFee());
        txtFromSl_No.setText(CommonUtil.convertObjToStr(observable.getTxtFromSL_No()));
        txtToSL_No.setText(CommonUtil.convertObjToStr(observable.getTxtToSL_No()));
        Double svalue=CommonUtil.convertObjToDouble( txtShareValue.getText());
        Double sfee=CommonUtil.convertObjToDouble(txtTotShareFee.getText());
        Double memfee=CommonUtil.convertObjToDouble( txtShareMemFee.getText());
        Double sapplfee=CommonUtil.convertObjToDouble( txtShareApplFee.getText());
        double tot=svalue.doubleValue()+sfee.doubleValue()+memfee.doubleValue()+sapplfee.doubleValue();
        double amt=CommonUtil.convertObjToDouble(observable.getProductAmount()).doubleValue();
        txtShareTotAmount.setText(observable.getTxtShareTotAmount());
        txtNoShares.setText(observable.getTxtNoShares());
        txtShareTotAmount.setText(observable.getTxtShareTotAmount());
        if( tblShareAcctDet.getRowCount()>0){
            if(chkDrfApplicableYN.isSelected()==true && cboDrfProdId.getSelectedItem()!=null && tblShareAcctDet.getSelectedRow()==0){
                txtShareTotAmount.setText(CommonUtil.convertObjToStr(new Double(tot+amt)));
            }else{
        txtShareTotAmount.setText(CommonUtil.convertObjToStr(new Double(tot)));
            }
        }else{
            txtShareTotAmount.setText(CommonUtil.convertObjToStr(new Double(tot)));
        }
    }
    private void btnShareAcctDetSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShareAcctDetSaveActionPerformed
        save=true;
        shareAcctDetNewPressed = false;
        StringBuffer strBMandatory = new StringBuffer();
        
        //        int getComCnt = panShareAcctDetails.getComponentCount();
        //        for(int i=0;i<getComCnt;i++){
        //           //System.out.println("panShareAcctDetails.getComponent(): i" + i + panShareAcctDetails.getComponent(i));
        //        }
        
        if(tblShareAcctDet!=null && tblShareAcctDet.getRowCount()>0){
           for(int i=0;i<tblShareAcctDet.getRowCount();i++){
               String diff=CommonUtil.convertObjToStr(tblShareAcctDet.getValueAt(i, 4));
               String auth=CommonUtil.convertObjToStr(tblShareAcctDet.getValueAt(i, 5));
               String DetNo=CommonUtil.convertObjToStr(tblShareAcctDet.getValueAt(i, 0));
               if(diff.equals("DIFFERENTIAL") && !auth.equals("AUTHORIZED")){
                   observable.setShareAcctStatusDueAmt("UPDATE");
                   observable.setShareAcctStatusAddWithdra(DetNo);
               }
           }
        }
        
        if(CommonUtil.convertObjToInt(txtNoShares.getText())>0) {
            String strShareAcctDetailsMandatory = new MandatoryCheck().checkMandatory(getClass().getName(), panShareAcctDetails);
            if (rdoSharewithDrawal.isSelected() == true && observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                strBMandatory.append(strShareAcctDetailsMandatory);
                 strBMandatory.append("\n");
                if (txtResolutionNo1.getText().toString().equals("")) {
                  strBMandatory.append(obj.getString("txtResolutionNo1"));
                } else if (tdtShareDetIssShareCert.getDateValue().toString().equals("")) {
                  strBMandatory.append(obj.getString("tdtShareDetIssShareCert"));
                 }
            } else {//--- checks the Mandatory data for ShareAccountDet screen
            if(strShareAcctDetailsMandatory.length()>0){
                strBMandatory.append(strShareAcctDetailsMandatory);
                strBMandatory.append("\n");
            }
           }
            //        //--- checks whether ShareNoFrom is Lesser or not
            //        if(txtShareDetNoOfShares.getText().startsWith("-")){
            //            ShareRB resourceBundle = new ShareRB();
            //            strBMandatory.append(resourceBundle.getString("ShareNoFromLesserShareNoTo"));
            //            strBMandatory.append("\n");
            //            resourceBundle = null;
            //        }
            
            //        validateForUniqueShareNo(strBMandatory);
            //ADDED BY VIVEK
            HashMap sanMap = new HashMap();
            double sanAmt = 0.0;
            sanMap.put("MEM_NO",txtShareAcctNo.getText());
            List lst=  ClientUtil.executeQuery("getSanctionAmtValueForLoan",sanMap);
            if(lst!=null && lst.size()>0){
            HashMap resultDocument=(HashMap)lst.get(0);
                sanAmt = CommonUtil.convertObjToDouble(resultDocument.get("BALANCE"));
            }
            String shareType = CommonUtil.convertObjToStr(((ComboBoxModel)(cboShareType.getModel())).getKeyForSelected());
            sanMap.put("SHARETYPE",shareType);
            List bLst=  ClientUtil.executeQuery("getBorrowerSuretyShareValue",sanMap);
            double borrowerAmt = 0.0;
            double surityAmt = 0.0;
            double surityBorrowerAmt = 0.0;
            double borrowerPerc = 0.0;
            double surityPerc = 0.0;
            if(bLst!=null && bLst.size()>0){
                HashMap resultDoc=(HashMap)bLst.get(0);
                borrowerPerc = CommonUtil.convertObjToDouble(resultDoc.get("BORROWER_SHARE"));
                surityPerc = CommonUtil.convertObjToDouble(resultDoc.get("SURITY_SHARE"));
                
                
            borrowerAmt = sanAmt * (borrowerPerc/100);
            surityAmt = sanAmt * (surityPerc/100);
            surityBorrowerAmt = borrowerAmt + surityAmt;
            
            }else{
                ClientUtil.displayAlert("Share percentage not mentioned in share product");
                return;
            }
            if (strBMandatory.length() > 1){
                CommonMethods.displayAlert(strBMandatory.toString());
                return;
            } //        else if(rdoSharewithDrawal.isSelected()==true && observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT
            //        && CommonUtil.convertObjToInt(txtNoShares.getText())>CommonUtil.convertObjToInt(lblTotNoOfSharesCount.getText())){
            //            ClientUtil.displayAlert("No of Shares Less than no of withdrawl");
            //        }
            //
            else if(rdoSharewithDrawal.isSelected()==true && observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT
            && CommonUtil.convertObjToInt(txtShareTotAmount.getText())>CommonUtil.convertObjToInt(lblTotshareBalVal.getText())){
                ClientUtil.displayAlert("Balance is less than withdrawl amount");
            } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT && (rdoShareAddition.isSelected() == false && rdoSharewithDrawal.isSelected() == false && rdoShareDifferentialAmount.isSelected() == false)) {
                ClientUtil.showAlertWindow("Select Addition or withDrawal");
                return;
            }else if(chkDrfApplicableYN.isSelected()==true && CommonUtil.convertObjToStr(cboDrfProdId.getSelectedItem()).equals("")){               
                    ClientUtil.displayAlert("Please select DRF Prod id");
                    return;
            } else if (rdoSharewithDrawal.isSelected() == true && observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT && surityBorrowerAmt > CommonUtil.convertObjToDouble(lblTotshareBalVal.getText())) {
                int confirm = ClientUtil.confirmationAlert("Required Share "+surityBorrowerAmt+" is less than Balance Share "+lblTotshareBalVal.getText()+", Do you want to continue?");
                if(confirm == 0){
                    toShareAmt=CommonUtil.convertObjToDouble(observable.getTxtShareTotAmount()).doubleValue();
                    shareAcctDetSave(); //--- Save the Data
                    totalShareCount();
                    isTblSharAcctClicked = false;
                }
            } else {
                toShareAmt=CommonUtil.convertObjToDouble(observable.getTxtShareTotAmount()).doubleValue();
                shareAcctDetSave(); //--- Save the Data
                totalShareCount();
                isTblSharAcctClicked = false;
                
                
            }
            if(tblShareAcctDet.getRowCount()>0){
                setBtnShareAcctDet(false);
                
                // If we are implementing Truetransact branchwise, Send to Resolution & Share Resolution option in HO are not possible
                // By Rajesh
                btnShareAcctDetNew.setEnabled(true);
                //--- To set the Transaction details
                int noOfShares = getNoOfShares();
                double shareTotalAmt = CommonUtil.convertObjToDouble(txtMemFee.getText()).doubleValue()
                + CommonUtil.convertObjToDouble(txtShareFee.getText()).doubleValue() * noOfShares
                + CommonUtil.convertObjToDouble(txtApplFee.getText()).doubleValue()
                + CommonUtil.convertObjToDouble(txtShareAmt.getText()).doubleValue() * noOfShares;
                // If we are implementing Truetransact branchwise, Send to Resolution & Share Resolution option in HO are not possible
                // By Rajesh
                //            transactionUI.setCallingApplicantName(lblValCustName.getText());
                //            transactionUI.setCallingAmount(String.valueOf(shareTotalAmt));
                //            transactionUI.updateTransactions();
                
                
                
            }
            
            
        } else{
            ClientUtil.displayAlert("Please enter No Of Shares");
        }
        
    }//GEN-LAST:event_btnShareAcctDetSaveActionPerformed
    //    private StringBuffer validateForUniqueShareNo(StringBuffer strBMandatory){
    //        //--- If there are no previous alert message thenc check, else don't do
    //        if(strBMandatory.length()==0){
    //            HashMap dataHash;
    //            int presentShareNoFrom = CommonUtil.convertObjToInt(txtShareDetShareNoFrom.getText());
    //            int presentShareNoTo = CommonUtil.convertObjToInt(txtShareDetShareNoTo.getText());
    //            HashMap whereMap = new HashMap();
    //            whereMap.put("SHARE_TYPE", CommonUtil.convertObjToStr(observable.getCbmShareType().getKeyForSelected()));
    //            whereMap.put("SHARE_ACCT_NO", txtShareAcctNo.getText());
    //            List list = (List) ClientUtil.executeQuery("getShareAcctDetails", whereMap);
    //            int listSize = list.size();
    //            //--- Repeat the checking with all the records available in database
    //            for(int i=0;i<listSize;i++){
    //                dataHash = new HashMap();
    //                dataHash = (HashMap) list.get(i);
    //                int shareNoFrom = CommonUtil.convertObjToInt(dataHash.get("SHARE_NO_FROM"));
    //                int shareNoTo = CommonUtil.convertObjToInt(dataHash.get("SHARE_NO_TO"));
    //                //--- do check for all the ShareNos.that are b/w ShareNoFrom & ShareNoTo
    //                for(int j=presentShareNoFrom; j<=presentShareNoTo; j++){
    //                    //--- If the shareNo is already present, then alert the user
    //                    if(j>=shareNoFrom && j<=shareNoTo){
    //                        strBMandatory.append(j);
    //                        strBMandatory.append(" is already present.");
    //                        strBMandatory.append("\n");
    //                    }
    //                }
    //            }
    //            //--- if the ShareAcctDet is not null, continue checking with those datas
    //            if(observable.shareAcctDetAll != null){
    //                int shareAcctDetAllSize = observable.shareAcctDetAll.size();
    //                //--- do check for all the available datas
    //                for(int i=0;i<shareAcctDetAllSize;i++){
    //                    dataHash = new HashMap();
    //                    dataHash = (HashMap) observable.shareAcctDetAll.get(String.valueOf(i));
    //                    //--- If it is not in the Edit mode, then continue checking
    //                    if(isTblSharAcctClicked == false){
    //                        if(!dataHash.get("Status").equals("DELETED")){
    //                            int shareNoFrom = CommonUtil.convertObjToInt(dataHash.get("ShareNoFrom"));
    //                            int shareNoTo = CommonUtil.convertObjToInt(dataHash.get("ShareNoTo"));
    //                            //--- do check for all the ShareNos.that are b/w ShareNoFrom & ShareNoTo
    //                            for(int j=presentShareNoFrom; j<=presentShareNoTo; j++){
    //                                //--- If the shareNo is already present, then alert the user
    //                                if(j>=shareNoFrom && j<=shareNoTo){
    //                                    strBMandatory.append(j);
    //                                    strBMandatory.append(" is already present.");
    //                                    strBMandatory.append("\n");
    //                                }
    //                            }
    //                        }
    //                        //--- else , check whether the current selected row and the checking row are both the same
    //                    } else if(isTblSharAcctClicked == true) {
    //                        if(!String.valueOf(i).equals(String.valueOf(observable.shareAcctDetGetSelectedRowCount))){
    //                            if(!dataHash.get("Status").equals("DELETED")){
    //                                int shareNoFrom = CommonUtil.convertObjToInt(dataHash.get("ShareNoFrom"));
    //                                int shareNoTo = CommonUtil.convertObjToInt(dataHash.get("ShareNoTo"));
    //                                //--- do check for all the ShareNos.that are b/w ShareNoFrom & ShareNoTo
    //                                for(int j=presentShareNoFrom; j<=presentShareNoTo; j++){
    //                                    //--- If the shareNo is already present, then alert the user
    //                                    if(j>=shareNoFrom && j<=shareNoTo){
    //                                        strBMandatory.append(j);
    //                                        strBMandatory.append(" is already present.");
    //                                        strBMandatory.append("\n");
    //                                    }
    //                                }
    //                            }
    //                        }
    //
    //                    }
    //                }
    //            }
    //        }
    //        return strBMandatory;
    //    }
    
    /**
     * To display the data in ShareAcctDet Ctable and
     * to save the data in HashMap for further Processing
     */
    private void shareAcctDetSave(){
        updateOBFields();
        observable.populateShareAcctTbl();
        //        if(rdoSharewithDrawal.isSelected()==true){
        String apno= txtApplicationNo.getText();
        String acno=txtShareAcctNo.getText();
        HashMap hmap=new HashMap();
        String nshare="";
        double sharefee=0.0;
        double memfee=0.0;
        double sharevalue=0.0;
        double shareapplFee=0.0;
        double tot=0.0;
        String accNo="";
        String appNo="";
        
        if(!acno.equals(accNo)){
            
            hmap.put("SHARE ACCOUNT NO" , acno);
            List lst=ClientUtil.executeQuery("getShareAccDetails" , hmap);
            hmap=null;
            if(lst.size()>0){
                hmap=(HashMap)lst.get(0);
                nshare=CommonUtil.convertObjToStr( hmap.get("NO_OF_SHARES"));
                sharefee=CommonUtil.convertObjToDouble( hmap.get("SHARE_FEE")).doubleValue();
                memfee=CommonUtil.convertObjToDouble( hmap.get("SHARE_MEM_FEE")).doubleValue();
                sharevalue=CommonUtil.convertObjToDouble( hmap.get("SHARE_VALUE")).doubleValue();
                shareapplFee =CommonUtil.convertObjToDouble(hmap.get("SHARE_APPL_FEE")).doubleValue();
                tot=sharefee+memfee+sharevalue+shareapplFee;
            }
        } else {
            hmap.put("SHARE APPLICATION NO" , apno);
            List lst=ClientUtil.executeQuery("getShareAppDetails" , hmap);
            hmap=null;
            if(lst.size()>0){
                hmap=(HashMap)lst.get(0);
                nshare=CommonUtil.convertObjToStr( hmap.get("NO_OF_SHARES"));
                sharefee=CommonUtil.convertObjToDouble( hmap.get("SHARE_FEE")).doubleValue();
                memfee=CommonUtil.convertObjToDouble( hmap.get("SHARE_MEM_FEE")).doubleValue();
                sharevalue=CommonUtil.convertObjToDouble( hmap.get("SHARE_VALUE")).doubleValue();
                shareapplFee =CommonUtil.convertObjToDouble(hmap.get("SHARE_APPL_FEE")).doubleValue();
                tot=sharefee+memfee+sharevalue+shareapplFee;
            }
        }
        double total= CommonUtil.convertObjToDouble(txtShareTotAmount.getText()).doubleValue();
        String amount=transactionUI.getCallingAmount();
        //        if(total!=tot){
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        transactionUI.setChitType("");
        transactionUI.setSchemeName("");
//        added by Nikhil for DRF Applicable 
        double drfTransAmt = 0.0;
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            if (drfRecieptFlag && drfApplicable.length() > 0) {
                if (observable.getProductAmount() != null) {
                    drfTransAmt = CommonUtil.convertObjToDouble(observable.getProductAmount()).doubleValue();
                }
            }
        }
        //            added by nikhil for subsidy
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW
                && (CommonUtil.convertObjToStr(observable.getCaste()).equals("SC") || CommonUtil.convertObjToStr(observable.getCaste()).equals("ST")) && subsidyFlag) {
            double transAmtAftSubsidy = CommonUtil.convertObjToDouble(observable.getTxtShareTotAmount()).doubleValue()
                    - CommonUtil.convertObjToDouble(observable.getGovtsShare()).doubleValue();//+drfTransAmt;//commented by jiv
            transactionUI.setCallingAmount(String.valueOf(transAmtAftSubsidy));
        }else{
//            added by Nikhil for DRF applicable
            double shareTotAmt = CommonUtil.convertObjToDouble(observable.getTxtShareTotAmount()).doubleValue();
//            transactionUI.setCallingAmount(observable.getTxtShareTotAmount());
            transactionUI.setCallingAmount(String.valueOf(shareTotAmt));
            
            //KD-3609 : drf transaction comes up wrongly in additional share screen 
            if(rdoShareAddition.isSelected()){
                setTransAmountShareAddition();
            }
            
        }
        //        }
        transactionUI.setCallingApplicantName(lblValCustName.getText());
        //        }
        transactionUI.resetObjects();
        if(observable.shareAcctDetK==ok || observable.shareAcctDetK==cancel){
            observable.resetShareAcctDet();
            setShareAcctDetFields(false);
            setBtnShareAcctDet(false);
            btnShareAcctDetNew.setEnabled(true);
            transactionUI.setCallingApplicantName(lblValCustName.getText());
            observable.ttNotifyObservers();
        } else {
            observable.ttNotifyObservers();
        }
    }
    
    /**
     * Validates for the Duplication of Share Account Holder
     */
    private HashMap validateForDuplicateAcctHolder(){
        HashMap retMap = new HashMap();
        String tmpShareAcctNo = "";
        String custId = txtCustId.getText();
        String constitution = CommonUtil.convertObjToStr(observable.getCbmConstitution().getKeyForSelected());
        //listShareAcct Select * from share_acct where cust_id=CUST_ID and constitution=CONSTITUTION where status != 'deleted'
        HashMap whereMap = new HashMap();
        whereMap.put("CUST_ID", custId);
        whereMap.put("CONSTITUTION", constitution);
        List listShareAcct = (List)ClientUtil.executeQuery("shareAcctForCustAndConst",whereMap);
        //--- If the Customer and the same constituion is present, check whetehr it is joint
        //--- else proceed with normal flow
        if (listShareAcct.size() > 0) {

            tmpShareAcctNo = CommonUtil.convertObjToStr(((HashMap) listShareAcct.get(0)).get("SHARE_ACCT_NO"));
            //--- If it is Joint Account Holder , then check for the Joint Holders r the same
            if (CommonUtil.convertObjToStr(observable.getCbmConstitution().getKeyForSelected()).equals("JOINT_ACCOUNT")) {
                int listShareAcctSize = listShareAcct.size();
                HashMap shareAcctMapFromList = new HashMap();
                //--- Check the data for each ShareAccount in DB
                for (int j = 0; j < listShareAcctSize; j++) {
                    shareAcctMapFromList = (HashMap) listShareAcct.get(j);
                    tmpShareAcctNo = CommonUtil.convertObjToStr(shareAcctMapFromList.get("SHARE_ACCT_NO"));
                    //list Select * from SHAREJOINT where share_acct = SHARE_ACCT and status != 'DELETED'
                    List listJointShareAcct = (List) ClientUtil.executeQuery("shareAcctJointForShareAcctNo", shareAcctMapFromList);
                    int listJointShareAcctSize = listJointShareAcct.size();
                    ArrayList listJointCustomerInDB = new ArrayList();
                    ArrayList listJointCustomerInUI = new ArrayList();
                    HashMap shareJointAcctHolder = new HashMap();
                    //--- Get the joint CustomerID from DB
                    for (int i = 0; i < listJointShareAcctSize; i++) {
                        shareJointAcctHolder = (HashMap) listJointShareAcct.get(i);
                        listJointCustomerInDB.add(shareJointAcctHolder.get("CUST_ID"));
                    }
                    int sizeOfJointAcctHolders = tblJointAcctHolder.getRowCount();
                    //--- Get the joint CustomerID from UI
                    for (int k = 1; k < sizeOfJointAcctHolders; k++) {
                        listJointCustomerInUI.add(tblJointAcctHolder.getValueAt(k, 1));
                    }
                    //--- Check whtehr both Joint acct holder in UI and in DB r same.
                    if (listJointCustomerInUI.containsAll(listJointCustomerInDB) && listJointCustomerInDB.containsAll(listJointCustomerInUI)) {
                        retMap.put("ISDUPLICATED", "YES");
                        retMap.put("SHARE_ACCT", tmpShareAcctNo);
                        break;
                    } else {
                        // PROCEED NORMAL FLOW
                        retMap.put("ISDUPLICATED", "NO");
                        //                        retMap.put("SHARE_ACCT", null);
                    }
                }
            } else {
                // SHOW ALERT MESSAGE
                if (TrueTransactMain.MULTI_SHARE_ALLOWED.equals("Y")) {
                    retMap.put("ISDUPLICATED", "NO");
                } else {
                    retMap.put("ISDUPLICATED", "YES");
                    retMap.put("SHARE_ACCT", tmpShareAcctNo);
                }
            }
        } else {
            // PROCEED NORMAL FLOW
            retMap.put("ISDUPLICATED", "NO");
            //            retMap.put("SHARE_ACCT", null);
        }
        return retMap;
    }
    private void btnShareAcctDetNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShareAcctDetNewActionPerformed
        shareAcctDetNewPressed = true;
        boolean drfAllowed = true;
        resourceBundle = new ShareRB();
        updateOBFields();
     
        if (rdoShareAddition.isSelected() == true) {
            HashMap hashmap = new HashMap();
            String custid = CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(txtCustId.getText()));
            hashmap.put("CUST_ID", custid);  
            List lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashmap);
            if (lst1 != null && lst1.size() > 0) {
                ClientUtil.displayAlert("Customer is death marked please select another customerId");
                return;
            }
        }
        if (rdoSharewithDrawal.isSelected() == true) {
            HashMap hashmap = new HashMap();
            String custid = CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(txtCustId.getText()));
            hashmap.put("CUST_ID", custid);  
            List lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashmap);
            if (lst1 != null && lst1.size() > 0) {
                ClientUtil.showMessageWindow("Customer is death marked please select another customerId");
            }
        }

        //Check here
        HashMap checkMap = new HashMap();
        checkMap.put("SHARE_TYPE",CommonUtil.convertObjToStr(((ComboBoxModel) (cboShareType.getModel())).getKeyForSelected()));
        //DRF_ALLOWED
         List drfList = ClientUtil.executeQuery("getDRFAllowedForShareType", checkMap);
            if (drfList != null && drfList.size() > 0) {
                checkMap = (HashMap)drfList.get(0);
                if(checkMap.containsKey("DRF_ALLOWED") && checkMap.get("DRF_ALLOWED")!= null && checkMap.get("DRF_ALLOWED").equals("N")){
                    drfAllowed = false;
                }
            }
        // End

        //--- Check whether the ShareType is selected
        if(!chkDrfApplicableYN.isSelected() && drfEnable == true && (observable.getActionType()==ClientConstants.ACTIONTYPE_NEW ||
                observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT) && drfExist==false && drfAllowed){//if condition changed By Revathi.L
            int yesNo = 0;
            String[] options = {"Yes", "No"};
            yesNo = COptionPane.showOptionDialog(null,"Do you want to include DRF transaction?", CommonConstants.WARNINGTITLE,
            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
            null, options, options[0]);
            if (yesNo==0) {
               chkDrfApplicableYN.setSelected(true);
               cboDrfProdId.setVisible(true);
               cboDrfProdId.setEnabled(true);
               chkDrfApplicableYN.setEnabled(true);
               return ;
            }if(yesNo==1){
                chkDrfApplicableYN.setSelected(false);
                //chkDrfApplicableYN.setEnabled(false);
                cboDrfProdId.setVisible(false);
            }
            drfEnable = false;
        }
        
        //If condition added by Jeffin John on 20-05-2014 for Mantis ID-9064
        
        if(rdoSharewithDrawal.isSelected() == true && observable.getActionType()== ClientConstants.ACTIONTYPE_EDIT){
            txtShareApplFee.setText("0");
            txtTotShareFee.setText("0");
            txtShareValue.setText(lblTotshareBalVal.getText());
            txtNoShares.setText(lblTotNoOfSharesCount.getText());
            txtShareTotAmount.setText(lblTotshareBalVal.getText());
            observable.setTxtShareValue(txtShareValue.getText());
            observable.setTxtNoShares(txtNoShares.getText());
            observable.setTxtShareApplFee(txtApplFee.getText());
            observable.setTxtTotShareFee(txtTotShareFee.getText());
        }
        if (chkDrfApplicableYN.isSelected() == true && CommonUtil.convertObjToStr(cboDrfProdId.getSelectedItem()).equals("")) {
            ClientUtil.displayAlert("Please select DRF Prod id");
            return;
        }
        if(chkDrfApplicableYN.isSelected()){
            chkDrfApplicableYN.setEnabled(false);
            cboDrfProdId.setEnabled(false);
            lblDrfAmtVal.setEnabled(false);
        }else{
            chkDrfApplicableYN.setEnabled(false);
        }
        
        if (!cboMemDivPayMode.getSelectedItem().equals("")) {
            if (!cboShareType.getSelectedItem().equals("")) {
                //--- Check whether the Customer is selected
                if(!txtCustId.getText().equals("")){
                    //--- Check whether the Constitution is selected
                    if(!cboConstitution.getSelectedItem().equals("")){
                    /* Commented for Additional Shares purpose - Rajesh
                    HashMap duplicateData = validateForDuplicateAcctHolder();
                    String isDuplicated = CommonUtil.convertObjToStr(duplicateData.get("ISDUPLICATED"));
                            //--- If the Customer has already Share aacount, then alert him
                            if(isDuplicated.equals("NO")){
                     **/
                        // Added the following for Additional Shares purpose - Rajesh
                        HashMap whereMap = new HashMap();
                        whereMap.put("CUST_ID", txtCustId.getText());
                        List listCnt = (List)ClientUtil.executeQuery("getCountForNotAuthShareAcctForCust",whereMap);
                        if(listCnt!=null && listCnt.size() >0){
                            //                            int cnt = Integer.parseInt(CommonUtil.convertObjToStr(listCnt.get(0)));
                            int cnt = 0;
                            if (cnt<=0) {
                                ClientUtil.enableDisable(panShareTrans,true);
                                // End for Additional Shares purpose - Rajesh
                                observable.shareAcctDetStatus = CommonConstants.STATUS_CREATED;
                                observable.shareAcctDetMode=0;
                                if(chkmoreThanone()){
                                    ClientUtil.displayAlert("Already Record in The list!!!");
                                }else{
                                    if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT && (rdoShareAddition.isSelected()==false && rdoSharewithDrawal.isSelected()==false)) {
                                        ClientUtil.showAlertWindow("Select Addition or withDrawal");
                                        return; 
                                    }else{
                                        if(rdoSharewithDrawal.isSelected()==true){
                                            txtResolutionNo1.setEnabled(false);
                                            txtResolutionNo1.setEditable(false);
                                            RsolutionSearch.setEnabled(false);
                                            tdtShareDetIssShareCert.setEnabled(false);
                                        }
                                        
                                        setShareAcctDetNewAndTblPress();
                                        ClientUtil.enableDisable(panShareAcctDet, true);
                                        if (!rdoSharewithDrawal.isSelected()) {
                                            observable.resetShareAcctDet();
                                        }

                                        observable.setTxtShareDetShareAcctNo(observable.getTxtShareAcctNo());
                                        //                                txtShareDetShareAcctNo.setText(observable.getTxtShareDetShareAcctNo());
                                        //                                txtShareDetShareAcctNo.setEnabled(false);
                                        //            txtShareDetNoOfShares.setEnabled(false);
                                        // If we are implementing Truetransact branchwise, Send to Resolution & Share Resolution option in HO are not possible
                                        // By Rajesh  // The following four lines given true
                                        //                                txtShareDetShareNoFrom.setEnabled(true);
                                        //                                txtShareDetShareNoTo.setEnabled(false);
                                        txtResolutionNo1.setEnabled(true);
                                         RsolutionSearch.setEnabled(true);
                                        tdtShareDetIssShareCert.setEnabled(true);
                                        // Commented For Additional Shares purpose - Rajesh
                                        //                            } else {
                                        //                                ClientUtil.showAlertWindow(resourceBundle.getString("shareAcctExists") + CommonUtil.convertObjToStr(duplicateData.get("SHARE_ACCT")));
                                        //                            }
                                        // End (Commented For Additional Shares purpose - Rajesh)
                                        // Added the following for Additional Shares purpose - Rajesh
                                    }
                                }
                            } else {
                                ClientUtil.showAlertWindow(resourceBundle.getString("authExistShareAcct"));
                            }
                            // End for Additional Shares purpose - Rajesh
                        }
                    } else {
                        ClientUtil.showAlertWindow(resourceBundle.getString("selectTheConstitution"));
                    }
                } else {
                    ClientUtil.showAlertWindow(resourceBundle.getString("selectTheCustomer"));
                }
            } else {
                ClientUtil.showAlertWindow(resourceBundle.getString("selectTheShareType"));
            }
        } else {
            ClientUtil.showAlertWindow(resourceBundle.getString("selectTheShare Dividend pay Mode"));
        }
        observable.ttNotifyObservers();
        if (rdoSharewithDrawal.isSelected()) {
            txtNoShares.setEnabled(false);
            txtNoShares.setEditable(false);
        }
         ClientUtil.enableDisable(panShareAcctDetails, true); 
        if (rdoSharewithDrawal.isSelected()) {
            txtNoShares.setEnabled(false);
            if (observable.getFullClosureReq() != null && observable.getFullClosureReq().equals("Y")) {
                txtShareValue.setEnabled(false);
            } else {
                txtShareValue.setEnabled(true);
            }
        } else {
            txtShareValue.setEnabled(true);
            txtNoShares.setEnabled(true);
        }
    }//GEN-LAST:event_btnShareAcctDetNewActionPerformed
    private void setShareAcctDetNewAndTblPress(){
        updateOBShareAcctDet();
        setShareAcctDetFields(true);
        setBtnShareAcctDet(false);
        btnShareAcctDetSave.setEnabled(true);
    }
   private void enableDisableShareAmt(){
       if (rdoSharewithDrawal.isSelected()) {
            if (observable.getFullClosureReq() != null && observable.getFullClosureReq().equals("Y")) {
                txtShareValue.setEnabled(false);
            } else {
                txtShareValue.setEnabled(true);
            }
            transactionUI.setTransactionMode(CommonConstants.CREDIT);
        } else {
            txtShareValue.setEnabled(true);            
        }
       //panShareAcctDetails.setEnabled(false);
       
        ClientUtil.enableDisable(panShareAcctDetails, false); 
        btnShareAcctDetNew.setEnabled(true);
        
   } 
    private void updateOBShareAcctDet(){
        //        observable.setTxtShareDetNoOfShares(txtShareDetNoOfShares.getText());
        observable.setTxtResolutionNo1(txtResolutionNo1.getText());
        observable.setTxtShareTotAmount(txtShareTotAmount.getText()) ;
        if(observable.getActionType()== ClientConstants.ACTIONTYPE_EDIT && rdoShareDifferentialAmount.isSelected()==true){
            observable.setTxtShareTotAmount(CommonUtil.convertObjToStr(transactionUI.getCallingAmount()));
        }
        //        observable.setTxtShareDetShareAcctNo(txtShareDetShareAcctNo.getText());
        //        observable.setTxtShareDetShareNoFrom(txtShareDetShareNoFrom.getText());
        //        observable.setTxtShareDetShareNoTo(txtShareDetShareNoTo.getText());
        observable.setTxtShareValue(txtShareValue.getText()) ;
        observable.setTxtTotShareFee(txtTotShareFee.getText()) ;
        observable.setTxtShareApplFee(txtShareApplFee.getText()) ;
        observable.setTxtShareMemFee(txtShareMemFee.getText()) ;
        observable.setTdtShareDetIssShareCert(tdtShareDetIssShareCert.getDateValue());
        
    }
    
    private void setShareAcctDetFields(boolean val){
        ClientUtil.enableDisable(panShareAcctDet, val);
        //        txtShareDetShareNoTo.setEnabled(false);
    }

    /** Populates the Joint Account Holder data in the Screen
     *  for the row passed as argument.
     *  @param  int rowSelected is passed as argument
     */
    private void tblJointAcctHolderRowSelected(int rowSelected){
        if(tblJointAcctHolder.getSelectedRow()!=0){
            setBtnShareJoint(true);
        } else if(cboConstitution.getSelectedItem().equals(CBO_JOINT_ACCOUNT)){
            setBtnShareJoint(false);
            btnJointAcctNew.setEnabled(true);
        }
        HashMap cust = new HashMap();
        cust.put(CUST_ID,tblJointAcctHolder.getValueAt(tblJointAcctHolder.getSelectedRow(), 1));
        observable.populateScreen(cust,true);
        updateCustomerDetails();
        cust = null;
    }
    private void cboConstitutionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboConstitutionActionPerformed
        //        System.out.println("START ACTION CommonUtil.convertObjToStr(observable.getCbmConstitution().getKeyForSelected()):" + CommonUtil.convertObjToStr(observable.getCbmConstitution().getKeyForSelected()));
        //--- If some data is seleted, check whether it is Joint Account
        observable.setCboConstitution(CommonUtil.convertObjToStr(cboConstitution.getSelectedItem()));
        if((!cboConstitution.getSelectedItem().equals(""))&&(cboConstitution.getSelectedItem()!= null)){
            String joint="Joint Account";
            if(cboConstitution.getSelectedItem().equals(joint)){
                ClientUtil.displayAlert("Joint Account is not Allowed");
                return;
            }
            //--- If Selected data is "Joint Account", enable the New Button
            if(cboConstitution.getSelectedItem().equals(CBO_JOINT_ACCOUNT)){
                btnJointAcctNew.setEnabled(true);
            } else {
                checkJointAccntHolderForData();
            }
            //--- Else if no data is seleted, disable the Buttons
        } else if (cboConstitution.getSelectedItem().equals("")) {
            checkJointAccntHolderForData();
        }
        
        //        System.out.println("END ACTION CommonUtil.convertObjToStr(observable.getCbmConstitution().getKeyForSelected()):" + CommonUtil.convertObjToStr(observable.getCbmConstitution().getKeyForSelected()));
    }//GEN-LAST:event_cboConstitutionActionPerformed
    /** Displays the Alert message for Joint Account Holder Data and
     *  gives the Option to reset it.
     */
    private void checkJointAccntHolderForData(){
        if(tblJointAcctHolder.getRowCount()>1){
            int reset = CommonMethods.showDialogYesNo(objCommRB.getString("dialogForJointAccntHolder"));
            if(reset==yes){ //--- If Yes, disable Joint Account Holder Tab
                observable.resetJntAccntHoldTbl();
                CustInfoDisplay(txtCustId.getText());
                setBtnShareJoint(false);
                observable.ttNotifyObservers();
            } else if(reset==no){ //--- If No, don't disable Joint Account Holder Tab.
                observable.setCboConstitution(CBO_JOINT_ACCOUNT);
                cboConstitution.setSelectedItem(observable.getCboConstitution());
            }
        } else {
            setBtnShareJoint(false);
        }
    }
    
    private void CustInfoDisplay(String custId){
        HashMap hash = new HashMap();
        hash.put(CUST_ID,custId);
        observable.populateScreen(hash,false);
        updateCustomerDetails();
        txtCustId.setText(observable.getTxtCustId());
        cboCommAddrType.setModel(observable.getCbmCommAddrType());
        tblJointAcctHolder.setModel(observable.getTblJointAccnt());
        if(observable.membershipType.equals("NOMINAL")){
            btnShareAcctDetNew.setEnabled(false);
        } else {
            btnShareAcctDetNew.setEnabled(true);
        }
        hash = null;
    }
    
    private void JointAcctDisplay(String custId){
        HashMap hash = new HashMap();
        hash.put(CUST_ID,custId);
        observable.populateJointAccntTable(hash);
        tblJointAcctHolder.setModel(observable.getTblJointAccnt());
        btnJointAcctDel.setEnabled(false);
        btnJointAcctToMain.setEnabled(false);
        hash = null;
    }
    
    private void updateCustomerDetails(){
        lblValCustomerName.setText(observable.getLblValCustomerName());
        lblValDateOfBirth.setText(observable.getLblValDateOfBirth());
        lblValStreet.setText(observable.getLblValStreet());
        lblValArea.setText(observable.getLblValArea());
        lblValCity.setText(observable.getLblValCity());
        lblValState.setText(observable.getLblValState());
        lblValCountry.setText(observable.getLblValCountry());
        lblValPin.setText(observable.getLblValPin());
        lblValCustName.setText(observable.getLblValCustName());
    }
    private void btnCustomerIdFileOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerIdFileOpenActionPerformed
        //        callView(CUSTOMER_ID);
        String shareType1 = "";
        if (TrueTransactMain.MULTI_SHARE_ALLOWED.equals("Y") && observable.getActionType() != ClientConstants.ACTIONTYPE_EDIT) {
        
            if (cboShareType.getSelectedIndex() > 0) {
                shareType1 = CommonUtil.convertObjToStr(((ComboBoxModel) (cboShareType.getModel())).getKeyForSelected());
            } else {
                ClientUtil.showMessageWindow("Select Share Type ");
                return;
            }
        }
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT) {
            viewType = ClientConstants.VIEW_TYPE_EDIT;
            HashMap map=new HashMap();
            map.put("SHARE_ACCT","SHARE_ACCT");
            isCustomerEditmode=true;
            new CheckCustomerIdUI(this,map);
            
        }else{
            viewType = CUSTOMER_ID;
            HashMap map=new HashMap();
            if (TrueTransactMain.MULTI_SHARE_ALLOWED.equals("Y")) {
                map.put("SHARE_TYPE", shareType1);
            } else {
            map.put("SHARE_ACCT_NEW","SHARE_ACCT_NEW");
            }
            new CheckCustomerIdUI(this,map);
        }
    }//GEN-LAST:event_btnCustomerIdFileOpenActionPerformed
    private void callView(String currField) {
        updateOBFields();
        viewType = currField;
        //--- If Customer Id is selected OR JointAccnt New is clciked, show the popup Screen of Customer Table
        if ((currField == CUSTOMER_ID) || (currField == JOINT_ACCOUNT)) {
            HashMap viewMap = new HashMap();
            StringBuffer presentCust = new StringBuffer();
            int jntAccntTablRow = tblJointAcctHolder.getRowCount();
            if (tblJointAcctHolder.getRowCount() != 0) {
                for (int i = 0, sizeJointAcctAll = tblJointAcctHolder.getRowCount(); i < sizeJointAcctAll; i++) {
                    if (i == 0 || i == sizeJointAcctAll) {
                        presentCust.append("'" + CommonUtil.convertObjToStr(tblJointAcctHolder.getValueAt(i, 1)) + "'");
                    } else {
                        presentCust.append("," + "'" + CommonUtil.convertObjToStr(tblJointAcctHolder.getValueAt(i, 1)) + "'");
                    }
                }
            }
            viewMap.put("MAPNAME", "getSelectAccInfoTOList");
            HashMap whereMap = new HashMap();
            whereMap.put("SHARE", "minor");
            whereMap.put("CUSTOMER_ID", presentCust);
            whereMap.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, viewMap).show();
        }
        if (currField.equals("DIVACNO")) {
            if (!prodType.equals("")) {
                HashMap viewMap = new HashMap();
                if (!prodType.equals("GL")) {
                    viewMap.put("MAPNAME", "Cash.getAccountList" + prodType);
                } else {
                    viewMap.put("MAPNAME", "Cash.getSelectAcctHead");
                }
                HashMap hash = new HashMap();
                hash.put("PROD_ID", CommonUtil.convertObjToStr(((ComboBoxModel) cboMemDivProdID.getModel()).getKeyForSelected()));
                hash.put("SELECTED_BRANCH", getSelectedBranchID());

                viewMap.put(CommonConstants.MAP_WHERE, hash);
                new ViewAll(this, viewMap, true).show();
                viewMap = null;
            } else {
                ClientUtil.displayAlert("Pleas Select the ProdType");
            }
        }
        if (currField.equals("SHREAPPNO")) {
            HashMap viewMap = new HashMap();
            viewMap.put("MAPNAME", "getSelShreAppNo");
            HashMap hash = new HashMap();
            viewMap.put(CommonConstants.MAP_WHERE, hash);
            new ViewAll(this, viewMap).show();
            //viewMap=null;
        }
        if (currField.equals("SHARE_ACT_NUM")) {
            HashMap viewMap = new HashMap();
            viewMap.put("MAPNAME", "viewAllShareAcct");
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, viewMap).show();
        }
        observable.ttNotifyObservers();
        if (currField.equals("RESOLUTIONNO")) {
            HashMap viewMap = new HashMap();
            viewMap.put("MAPNAME", "getBoardResolutionAuth");
            new ViewAll(this, viewMap).show();
            //viewMap=null;   
        }
    }
    
    /** Called by the Popup window created thru popUp method
     * @param param
     */
    public void fillData(Object param){
        HashMap hash = (HashMap) param;
        System.out.println("hash=================" + hash);
        if (hash.containsKey("FROM_TERM_LOAN")) {
//          observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
//          viewType=ClientConstants.VIEW_TYPE_EDIT;
            shareAccNo = CommonUtil.convertObjToStr(hash.get("SHARE ACCOUNT NO"));
            shareNoAvailable = CommonUtil.convertObjToStr(hash.get("SHARE_AVAIL_NO"));
            txtShareAcctNo.setText(shareAccNo);
            txtNoShares.setText(shareNoAvailable);
            btnEditActionPerformed(null);
            HashMap shareAvailMap = new HashMap();
            txtShareAcctNoFocusLost(shareAvailMap);
            rdoShareAddition.setSelected(true);
            rdoShareAdditionActionPerformed(null);
//          btnShareAcctDetNew.setSelected(true);
            btnShareAcctDetNewActionPerformed(null);
            txtNoShares.setText(shareNoAvailable);
            txtNoSharesFocusLost(null);
        }
        if (hash.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")) {
            if (hash.containsKey("BATCH_ID")) {
                BATCH_ID = CommonUtil.convertObjToStr(hash.get("BATCH_ID"));
            }
            if (hash.containsKey("SHARE")) {
                authorizeListScreen = "share";
            }
            if (hash.containsKey("SHARE_ACCT")) {
                authorizeListScreen = "shareAccount";
            }
            fromNewAuthorizeUI = true;
            newauthorizeListUI = (NewAuthorizeListUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = ClientConstants.VIEW_TYPE_AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            btnReject.setEnabled(false);
            rejectFlag = 1;
        }
        if (hash.containsKey("FROM_AUTHORIZE_LIST_UI")) {
            if (hash.containsKey("BATCH_ID")) {
                BATCH_ID = CommonUtil.convertObjToStr(hash.get("BATCH_ID"));
            }
            if (hash.containsKey("SHARE")) {
                authorizeListScreen = "share";
            }
            if (hash.containsKey("SHARE_ACCT")) {
                authorizeListScreen = "shareAccount";
            }
            fromAuthorizeUI = true;
            authorizeListUI = (AuthorizeListUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = ClientConstants.VIEW_TYPE_AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            btnReject.setEnabled(false);
            rejectFlag = 1;
        }
        if (hash.containsKey("FROM_MANAGER_AUTHORIZE_LIST_UI")) {
            if (hash.containsKey("BATCH_ID")) {
                BATCH_ID = CommonUtil.convertObjToStr(hash.get("BATCH_ID"));
            }
            fromManagerAuthorizeUI = true;
            ManagerauthorizeListUI = (AuthorizeListDebitUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = ClientConstants.VIEW_TYPE_AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            btnReject.setEnabled(false);
            rejectFlag = 1;
        }
        if (hash.containsKey("FROM_CASHIER_AUTHORIZE_LIST_UI")) {
            fromCashierAuthorizeUI = true;
            CashierauthorizeListUI = (AuthorizeListCreditUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = ClientConstants.VIEW_TYPE_AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            btnReject.setEnabled(false);
            rejectFlag = 1;
        }
        if (hash.containsKey("FROM_CASHIER_APPROVAL_REJ_UI")) {
            fromAuthorizeUI = false;
            fromCashierAuthorizeUI = false;
            fromManagerAuthorizeUI = false;
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
            observable.setStatus();
        }
        //added by rishad 07/mar/2018
        if (viewType == ClientConstants.VIEW_TYPE_AUTHORIZE) {
            if (hash.containsKey("TRANS_ID")) {
                TRANS_ID = CommonUtil.convertObjToStr(hash.get("TRANS_ID"));
            }
        }
        if (hash.get("SHARE DETAIL NO") != null) {
            shareDetNo = CommonUtil.convertObjToStr(hash.get("SHARE DETAIL NO"));
        }
        if (hash.get("ACCOUNTNO") != null) {
            String ACCOUNTNO = (String) hash.get("ACCOUNTNO");
            if (prodType.equals("TD") && ACCOUNTNO.lastIndexOf("_") == -1) {
                hash.put("ACCOUNTNO", hash.get("ACCOUNTNO") + "_1");
            }
            txtDivAcNo.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
            observable.setTxtDivAcNo(txtDivAcNo.getText());
        }
        if (hash.containsKey("SHARE_APPL_NO")) {
            observable.resetForm();
            txtApplicationNo.setText(CommonUtil.convertObjToStr(hash.get("SHARE_APPL_NO")));
            observable.setTxtApplicationNo(txtApplicationNo.getText());
        } else if (prodType.equals("GL")) {
            txtDivAcNo.setText(CommonUtil.convertObjToStr(hash.get("A/C HEAD")));
            observable.setTxtDivAcNo(txtDivAcNo.getText());
        } 
        if (hash.containsKey("SHARE ACCOUNT DETAIL NO")) {
            shareAccountDetailNo = CommonUtil.convertObjToStr(hash.get("SHARE ACCOUNT DETAIL NO"));
        }
        //--- If View Type is "EDIT" or "DELETE" the fill the UI From the appropriate
        //--- Table, else fill the required UI according to the View Type
        if (viewType.equals(ClientConstants.VIEW_TYPE_EDIT) || viewType.equals(ClientConstants.VIEW_TYPE_DELETE) || viewType.equals(ClientConstants.VIEW_TYPE_AUTHORIZE) || viewType.equals(ClientConstants.VIEW_TYPE_SEND_TO_RESOLUTION)
                || viewType.equals("SHARE_ACT_NUM")) {
            panEditDelete = DRFTRANSACTION;
            hash.put("DRF_PROD_ID", prodId);
            observable.populateDrfTransData(hash, panEditDelete);
            initTableData();
            isFilled = true;
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                    || viewType == ClientConstants.VIEW_TYPE_AUTHORIZE || viewType == ClientConstants.VIEW_TYPE_SEND_TO_RESOLUTION) {
                //                if(viewType == ClientConstants.VIEW_TYPE_AUTHORIZE){
//                if(isCustomerEditmode) {// Commented by nithya on 05-12-2017
//                 hash=txtShareAcctNoFocusLost(hash);
//                }

                if (observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
                    //  if(!hash.containsKey("FROM_CASHIER_APPROVAL_REJ_UI")){
                    HashMap hmap = new HashMap();
                    hmap.put("SHARE_ACCT_NO", hash.get("SHARE ACCOUNT NO"));
                    List lst = ClientUtil.executeQuery("getShareDetForReject", hmap);
                    if (lst != null && lst.size() > 0) {
                        hmap = (HashMap) lst.get(0);
                        double count = CommonUtil.convertObjToDouble(hmap.get("COUNT")).doubleValue();
                        if (count > 1) {
                            ClientUtil.displayAlert("Additional Share Exists.Use Delete button to Reject");
                            btnReject.setEnabled(false);
                            btnCancel.setEnabled(true);
                            return;
                        }
                    }
                }
                hash.put("AUTH_TRANS_DETAILS", "AUTH_TRANS_DETAILS");
                actionEditDelete(hash);
          
                txtShareAcctNo.setEnabled(false);
                txtCustId.setEnabled(false);
                cboShareType.setEnabled(false);
                observable.ttNotifyObservers();
                populateFieldsEditMode();
                //                added by  Nikhil for subsidy
                if(tblJointAcctHolder.getRowCount()>0){
                    String custCaste = CommonUtil.convertObjToStr(tblJointAcctHolder.getValueAt(0,4));
                    int shareDetailsCount = tblShareAcctDet.getRowCount();
                    if((custCaste.equals("SC") || custCaste.equals("ST")) && shareDetailsCount == 1){
                        subsidyGiven = true;
                    }else{
                        subsidyGiven = false;
                    }
                    
                }
                if (chkDuplicateIDCardYN.isSelected()) {
                    duplicateIDVisible(true);
                } else {
                    duplicateIDVisible(false);
                }
                if (chkNotEligibleStatus.isSelected() == true) {
                    tdtNotEligiblePeriod.setEnabled(true);
                } else {
                    tdtNotEligiblePeriod.setEnabled(false);
                }
                // If we are implementing Truetransact branchwise, Send to Resolution & Share Resolution option in HO are not possible
                // By Rajesh
                //                transactionUI.setButtonEnableDisable(true);
            }
            //--- disable the customerSelection in Edit Mode
            btnCustomerIdFileOpen.setEnabled(false);
            totalShareCount();
            // Added by nithya on 05-12-2017 
            if (CommonUtil.convertObjToStr(txtCustId.getText()).length() > 0) {
                isCustomerEditmode = true;
            }
            if (!(viewType.equals(ClientConstants.VIEW_TYPE_AUTHORIZE))) {// Added by nithya on 16-04-2018 for KDSA 111
                if (isCustomerEditmode) {
                    hash = txtShareAcctNoFocusLost(hash);
                }
            }
            if ((viewType.equals("SHARE_ACT_NUM") || viewType.equals(ClientConstants.VIEW_TYPE_EDIT)) && CommonUtil.convertObjToInt(observable.balance) > 0) {
//                 ClientUtil.showAlertWindow("Principle Outstanding  : "+CommonUtil.convertObjToStr(observable.balance));
                int confirm = ClientUtil.confirmationAlert("Liability Exists, Do you want to continue?");
                if (confirm == 1) {
                    btnCancelActionPerformed(null);
                    return;
                }

            }
        } else if (viewType.equals(CUSTOMER_ID)) {
            // Added to validate the customer already having shares - Rajesh
            if (hash.containsKey("CUST_ID")) {
                hash.put(CUSTOMER_ID, hash.get("CUST_ID"));
            }
            HashMap hashmap = new HashMap();
            String custid = CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(hash.get(CUSTOMER_ID)));
            hashmap.put("CUST_ID", custid);
            List lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashmap);
            if (lst1 != null && lst1.size() > 0) {
                ClientUtil.displayAlert("Customer is death marked please select another customerId");
                txtCustId.setText("");
                return;
            }
            txtCustId.setText(CommonUtil.convertObjToStr(hash.get(CUSTOMER_ID)));
            HashMap duplicateData = validateForDuplicateAcctHolder();
            String isDuplicated = CommonUtil.convertObjToStr(duplicateData.get("ISDUPLICATED"));
            //--- If the Customer has already Share aacount, then alert him
            if (isDuplicated.equals("NO")) {
                CustInfoDisplay(CommonUtil.convertObjToStr(hash.get(CUSTOMER_ID)));
            } else if (observable.getActionType() != ClientConstants.ACTIONTYPE_EDIT) {
                resourceBundle = new ShareRB();
                ClientUtil.showAlertWindow(resourceBundle.getString("shareAcctExists") + CommonUtil.convertObjToStr(duplicateData.get("SHARE_ACCT")));
                txtCustId.setText("");
                resourceBundle = null;
            }
        } else if (viewType.equals(JOINT_ACCOUNT)) {
            //            JointAcctDisplay(CommonUtil.convertObjToStr(hash.get(CUSTOMER_ID)));
            JointAcctDisplay(CommonUtil.convertObjToStr(hash.get(CUST_ID)));
        }
        if (viewType.equals(ClientConstants.VIEW_TYPE_DELETE) || viewType.equals(ClientConstants.VIEW_TYPE_AUTHORIZE) || viewType.equals(ClientConstants.VIEW_TYPE_SEND_TO_RESOLUTION)) {
            nomineeUi.disableNewButton(false);
        }
        updateCustomerDetails();
        nomineeUi.setMainCustomerId(txtCustId.getText());
        drfNomineeUi.setMainCustomerId(txtCustId.getText());
        btnShareAcctDetNew.setEnabled(true);
        //--- To disable all the fields in Authorize status and sendToResol.
        if (viewType.equals(ClientConstants.VIEW_TYPE_DELETE) || viewType.equals(ClientConstants.VIEW_TYPE_AUTHORIZE) || viewType.equals(ClientConstants.VIEW_TYPE_SEND_TO_RESOLUTION)) {
            disableScreen();
            tblShareAcctDet.setEnabled(false);
        } else {
            tblShareAcctDet.setEnabled(true);
        }
        //--- To enable only the selected ShareAcctDetNo.
        if (viewType.equals(ClientConstants.VIEW_TYPE_AUTHORIZE)) {
            shareAcctDetFilled = false;
            shareAcctDetNo = CommonUtil.convertObjToInt(hash.get("SHARE DETAIL NO"));
            tblShareAcctDetRowMouseClicked(shareAcctDetNo);
            shareAcctDetFilled = true;

            // If we are implementing Truetransact branchwise, Send to Resolution & Share Resolution option in HO are not possible
            // By Rajesh
            //            transactionUI.setMainEnableDisable(false);
        }
        //        if(viewType == ClientConstants.VIEW_TYPE_AUTHORIZE){
        if (observable.getProxyReturnMap() != null) {
            if (observable.getProxyReturnMap().containsKey("CASH_TRANS_LIST") || observable.getProxyReturnMap().containsKey("TRANSFER_TRANS_LIST")) {
                displayTransDetail(observable.getProxyReturnMap());
                tempProxyReturnMap = observable.getProxyReturnMap();
                observable.setProxyReturnMap(null);
                subsidyFlag = false;
            }
        }
        //        }
        //        txtShareDetNoOfShares.setEnabled(false);
        //--- disable the fee and isssue id during edit mode
        if (viewType.equals(ClientConstants.VIEW_TYPE_EDIT)||viewType.equals(ClientConstants.VIEW_TYPE_DELETE)|| viewType.equals(ClientConstants.VIEW_TYPE_AUTHORIZE)){
            txtApplFee.setEnabled(false);
            txtShareFee.setEnabled(false);
            txtShareAmt.setEnabled(false);
            txtMemFee.setEnabled(false);
            tdtIssId.setEnabled(false);
            if(viewType.equals(ClientConstants.VIEW_TYPE_AUTHORIZE)){
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                 btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
            }
        }
        if(hash.containsKey("FROM_AUTHORIZE_LIST_UI")){
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(true);
        }
        if(hash.containsKey("FROM_MANAGER_AUTHORIZE_LIST_UI")){
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(true);
        }
        if(hash.containsKey("FROM_CASHIER_AUTHORIZE_LIST_UI")){
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(true);
        }
        if (hash.containsKey("SHARE ACCOUNT NO")) {
            List lst = ClientUtil.executeQuery("getClosedShareAccInfo", hash);
            if (lst != null && lst.size() > 0) {
                btnSave.setEnabled(false);
                HashMap hmap = (HashMap) lst.get(0);
                ClientUtil.displayAlert("Share account is closed On" + CommonUtil.convertObjToStr(hmap.get("STATUS_DT")));
            }
        }

        if (viewType.equals(ClientConstants.VIEW_TYPE_AUTHORIZE)) {
            HashMap customerMap = new HashMap();
            customerMap.put("CUST_ID", txtCustId.getText());
            List lst = ClientUtil.executeQuery("getCustomerStatusForDep", customerMap);
            if (lst != null && lst.size() > 0) {
                customerMap = (HashMap) lst.get(0);
                ClientUtil.showAlertWindow("Please First Authorize Customer Id Creation...");
                btnCancelActionPerformed(null);
                return;
            }
            btnAuthorize.setEnabled(true);
            btnAuthorize.requestFocusInWindow();
        }
        if (rejectFlag == 1) {
            btnReject.setEnabled(false);
        }
        if (hash.containsKey("FROM_TERM_LOAN")) {
            txtNoShares.setText(CommonUtil.convertObjToStr(shareNoAvailable));
            btnSave.setEnabled(true);
        }
        if (hash.containsKey("RESOLUTION_ID")) {
            String rsolutionno = CommonUtil.convertObjToStr(hash.get("RESOLUTION_ID"));
            txtResolutionNo1.setText(rsolutionno);
            tdtShareDetIssShareCert.setDateValue(CommonUtil.convertObjToStr(hash.get("RESOLUTION_DATE")));
        }
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            btnSave.setEnabled(true);
            btnCancel.setEnabled(true);
            btnNew.setEnabled(false);
            btnEdit.setEnabled(false);
            setAuthBtnEnableDisable();
        }      
    }
    
    /*To get the data and populating on the screen,set the status and enabling the apt components*/
    private void actionEditDelete(HashMap hash){
        //fromActionEditHash = true;
        observable.resetForm();
        btnCustomerIdFileOpen.setEnabled(true);
        ClientUtil.enableDisable(panShareDet, true);
        ClientUtil.enableDisable(panOtherDet, true);
        observable.setStatus();
        // If we are implementing Truetransact branchwise, Send to Resolution & Share Resolution option in HO are not possible
        // By Rajesh
        //        observable.setTransactionOB(transactionUI.getTransactionOB());
        if (hash.containsKey("MEMBER_NO")) {
            hash.put("SHARE ACCOUNT NO",hash.get("MEMBER_NO"));
        }
        
        observable.populateData(hash,nomineeUi.getNomineeOB(),drfNomineeUi.getNomineeOB());

        cboCommAddrType.setModel(observable.getCbmCommAddrType());
        setButtonEnableDisable();
        txtShareAcctNo.setEditable(true);
        txtShareAcctNo.setEnabled(true);
        txtShareAcctNo.setText(observable.getTxtShareAcctNo());
        txtShareAcctNo.setEditable(false);
        txtShareAcctNo.setEnabled(false);
        txtCustId.setEnabled(false);
        txtApplicationNo.setEditable(true);
        txtApplicationNo.setEnabled(true);
        txtApplicationNo.setText(observable.getTxtApplicationNo());
        txtApplicationNo.setEditable(false);
        txtApplicationNo.setEnabled(false);
        nomineeUi.callMaxDel(txtShareAcctNo.getText());
        nomineeUi.resetNomineeTab();
        nomineeUi.setMaxNominee(observable.maxNominee);
        //Added By Revathi.L
        drfNomineeUi.callMaxDel(txtShareAcctNo.getText());
        drfNomineeUi.resetNomineeTab();
        drfNomineeUi.setMaxNominee(observable.maxNominee);
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        setModified(true);
        viewType = ClientConstants.VIEW_TYPE_NEW;
        //--- If it is null, create the observable
        //        if(observable==null){
        //            setObservable();
        //        }
        //
        //        try{
        //            observable.fillDropdown();
        //        } catch (Exception e){
        //            e.printStackTrace();
        //        }
        //        initComponentData();
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        observable.resetForm();
        btnCustomerIdFileOpen.setEnabled(true);
        ClientUtil.enableDisable(panShareAcctInfo, true);
        ClientUtil.enableDisable(panOtherDet, true);
        setButtonEnableDisable();
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.setStatus();
        setBtnShareAcctDet(false);
        setBtnShareJoint(false);
        btnIdPrint.setEnabled(false);
        observable.setTxtShareAcctNo(observable.NEW);
        observable.setTxtApplicationNo(observable.NEW);
        txtShareAcctNo.setText(observable.getTxtShareAcctNo());
        txtShareAcctNo.setEnabled(true);
        txtApplicationNo.setText(observable.getTxtApplicationNo());
        txtApplicationNo.setEnabled(true);
        txtCustId.setEnabled(true);
        tdtNotEligiblePeriod.setEnabled(false);
        cboAcctStatus.setEnabled(false);
        cboAcctStatus.setSelectedItem("Provisional");
        panShareTrans.setVisible(false);
        //        added by Nikhil for DRF Applicable
        panDrfApplicablle.setVisible(true);
        ClientUtil.enableDisable(panDrfApplicablle,true);
        nomineeUi.resetNomineeTab();
        transactionUI.resetObjects();
        rdoShareAddition.setSelected(false);
        rdoSharewithDrawal.setSelected(false);
        //        tabTransaction.remove(panTransaction);
        //        tabTransaction.remove(1);
        //        added by nikhil
        txtIDCardNo.setEnabled(true);
        tdtIDIssuedDt.setVisible(true);
        lblDuplicateIDCardYN.setVisible(false);
        chkDuplicateIDCardYN.setVisible(false);
        chkDrfApplicableYN.setSelected(false);
        txtApplicationNo.setEnabled(true);
        duplicateIDVisible(false);
        // If we are implementing Truetransact branchwise, Send to Resolution & Share Resolution option in HO are not possible
        // By Rajesh
        //        transactionUI.cancelAction(false);
        //        transactionUI.setButtonEnableDisable(true);
        //        transactionUI.resetObjects();
        //        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW); //Set in NEW, EDIT, DELETE only the respective modes
        
        tdtIssId.setEnabled(false);
        observable.setNewMode("true");
        drfEnable = true;
        cboConstitution.setSelectedItem("Individual");
        cboMemDivPayMode.setSelectedItem("Cash");
        btnShareAppNo.setEnabled(false);
        chkClr();
        btnDelete.setEnabled(false);
        drfNomineeUi.setActionType(ClientConstants.ACTIONTYPE_NEW);
        ClientUtil.enableDisable(panMobileBanking, true);
    }//GEN-LAST:event_btnNewActionPerformed

    private void cboShareTypeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboShareTypeFocusLost
        // TODO add your handling code here:
       // generateID();
        HashMap accNoMap=generateID();
       txtNextAccNo.setText((String) accNoMap.get(CommonConstants.DATA));
    }//GEN-LAST:event_cboShareTypeFocusLost

private void rdoShareDifferentialAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoShareDifferentialAmountActionPerformed
        // TODO add your handling code here:
        
        
         if(tblShareAcctDet!=null && tblShareAcctDet.getRowCount()>0){
             for(int i=0;i<tblShareAcctDet.getRowCount();i++){
                 String auth=CommonUtil.convertObjToStr(tblShareAcctDet.getValueAt(i, 5));
                 if(!auth.equals("AUTHORIZED")){
                     ClientUtil.displayAlert("Alredy record in the List");
                     return;
                 }
             }
         }
         rdoSharewithDrawal.setSelected(false);
         rdoShareAddition.setSelected(false);
         enableDisableShareAmt();
         String custid=txtCustId.getText();
         String shareType=cboShareType.getSelectedItem().toString();
         HashMap hmap=new HashMap();
         double facevalue=0.0;
         double paidDiffAmt =0.0;
         double paidShares =0.0;
         if(rdoShareDifferentialAmount.isSelected()){
             hmap.put("CUST_ID",custid);
             hmap.put("SHARE_TYPE",shareType);
             List lst=ClientUtil.executeQuery("getNoOfSharesForDueAmt",hmap);
             List lst1=ClientUtil.executeQuery("getFaceValue",hmap);
                     List lst2=ClientUtil.executeQuery("getNoOfSharesForDiffAmtPaid",hmap);
                    if(lst2!=null && lst2.size()>0){
                        hmap=(HashMap)lst2.get(0);
                        paidDiffAmt=paidDiffAmt+CommonUtil.convertObjToDouble(hmap.get("TOTAL_SHARE_AMOUNT")).doubleValue();
                         paidShares=paidShares+CommonUtil.convertObjToDouble(hmap.get("NO_OF_SHARES")).doubleValue();
                    }
             if(lst1!=null && lst1.size()>0){
                 hmap=(HashMap)lst1.get(0);
                 facevalue=CommonUtil.convertObjToDouble(hmap.get("FACE_VALUE")).doubleValue();
             }
             if(lst!=null && lst.size()>0){
                 hmap=(HashMap)lst.get(0);
                 double noOfShares=CommonUtil.convertObjToDouble(hmap.get("NO_OF_SHARES")).doubleValue();
                 double amt=CommonUtil.convertObjToDouble(hmap.get("TOTAL_SHARE_AMOUNT")).doubleValue();
                 double changedAmt=noOfShares*facevalue;
                 amt=amt+paidDiffAmt;
                 paidShares=amt/paidShares;
                 amt=changedAmt-amt;
                 if(facevalue==paidShares){
                     ClientUtil.displayAlert("No differential amount to be collected");
                     rdoShareDifferentialAmount.setSelected(false);
                     return;
                     
            } else if (amt > 0) {
                     
                     int confirm= ClientUtil.confirmationAlert("Differential  Share Amount to be paid is Rs"+amt+ "Do you want to continue ?");
                     if(confirm==0){
                         transactionUI.setCallingAmount(CommonUtil.convertObjToStr(new Double(amt)));
                         transactionUI.setCallingApplicantName(lblValCustName.getText());
                         txtNoShares.setText(CommonUtil.convertObjToStr(new Double(noOfShares)));
                         txtShareValue.setText(CommonUtil.convertObjToStr(new Double(amt)));
                         txtShareApplFee.setText("0.0");
                         txtShareMemFee.setText("0.0");
                         txtTotShareFee.setText("0.0");
                         txtShareTotAmount.setText(CommonUtil.convertObjToStr(new Double(amt)));
                         txtResolutionNo1.setText("0.0");
                         tdtShareDetIssShareCert.setDateValue("");
                         btnShareAcctDetSave.setEnabled(true);
                         txtShareValue.setEnabled(true);
                         observable.shareAcctDetStatus = CommonConstants.STATUS_CREATED;
                         observable.shareAcctDetMode=0;
                     }
                 }
             }
         }
         
}//GEN-LAST:event_rdoShareDifferentialAmountActionPerformed

private void rdoSharewithDrawalFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rdoSharewithDrawalFocusLost
        // TODO add your handling code here:
        //        if(rdoSharewithDrawal.getAction()==null)
        //            rdoShareAddition.setSelected(true);
}//GEN-LAST:event_rdoSharewithDrawalFocusLost

private void rdoSharewithDrawalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSharewithDrawalActionPerformed
        // TODO add your handling code here:
        rdoShareAddition.setSelected(false);
        rdoShareDifferentialAmount.setSelected(false);
        if(rdoShareAddition.isSelected() || rdoSharewithDrawal.isSelected()){
            btnShareAcctDetNew.setEnabled(true);
        }else{
            btnShareAcctDetNew.setEnabled(false);
        }
        //        tabTransaction.add(panTransaction,"TransactionDetails");
        //        rdoShareAddition.setVisible(false);
        enableDisableShareAmt();
}//GEN-LAST:event_rdoSharewithDrawalActionPerformed

private void rdoShareAdditionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rdoShareAdditionFocusLost
        // TODO add your handling code here:
        //        if(rdoShareAddition.getAction()==null)
        //            rdoSharewithDrawal.setSelected(true);
}//GEN-LAST:event_rdoShareAdditionFocusLost

private void rdoShareAdditionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoShareAdditionActionPerformed
        // TODO add your handling code here:
        rdoSharewithDrawal.setSelected(false);
        rdoShareDifferentialAmount.setSelected(false);
        
        if(rdoShareAddition.isSelected() || rdoSharewithDrawal.isSelected()){
            btnShareAcctDetNew.setEnabled(true);
        }else{
            btnShareAcctDetNew.setEnabled(false);
        }
        //added by jiv
         panDrfApplicablle.setVisible(true);
	//Added By Revathi.L
         drfExist = false;
         HashMap drfMap = new HashMap();
         drfMap.put("SHARE_ACCT_NO", txtShareAcctNo.getText());
         List drfLst = ClientUtil.executeQuery("checkDrfExistOrNot", drfMap);
         if(drfLst!=null && drfLst.size()>0){
             drfExist = true;
         }
         
//        ClientUtil.enableDisable(panDrfApplicablle,true);
//         chkClr();
        //
        //        tabTransaction.remove(1);
        //         rdoSharewithDrawal.setVisible(false);
         
        enableDisableShareAmt();
}//GEN-LAST:event_rdoShareAdditionActionPerformed

    private void btnShareAppNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShareAppNoActionPerformed
        // TODO add your handling code here:
        callView("SHREAPPNO");
        txtApplicationNoFocusLost(null);
        flg=1;
    }//GEN-LAST:event_btnShareAppNoActionPerformed

    private void txtApplicationNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtApplicationNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtApplicationNoActionPerformed

private void RsolutionSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RsolutionSearchActionPerformed

 
            //viewMap=null;
    callView("RESOLUTIONNO");
    // TODO add your handling code here:
}//GEN-LAST:event_RsolutionSearchActionPerformed
    private void lblDrfAmtValFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_lblDrfAmtValFocusLost
        if(lblDrfAmtVal.getText()!=null){
        observable.setProductAmount(lblDrfAmtVal.getText());
        }
    }//GEN-LAST:event_lblDrfAmtValFocusLost

private void btnIdPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIdPrintActionPerformed
// TODO add your handling code here:
    int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null,"Do you want to print?", CommonConstants.WARNINGTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        if (yesNo==0) {
            TTIntegration ttIntgration = null;
            HashMap paramMap = new HashMap();
            paramMap.put("shareAccNo",observable.getTxtShareAcctNo());
            //Added By Suresh
            ttIntgration.setParam(paramMap);
            String reportName = "";
                reportName = "ShareHolderCardId";
            ttIntgration.integrationForPrint(reportName, false);
            
        }
}//GEN-LAST:event_btnIdPrintActionPerformed

private void txtNoSharesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNoSharesActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtNoSharesActionPerformed

private void txtShareValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtShareValueFocusLost
    // TODO add your handling code here:
    //Below code for txtShareValueFocusLost Added by Jeffin John on 16-05-2014 for Mantis ID- 9064
    double amtEntered = 0.0;
    double faceValue = 0.0;
    int noOfShares = 0;
    double balAmount = CommonUtil.convertObjToDouble(lblTotshareBalVal.getText());
    if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT && rdoSharewithDrawal.isSelected() == true) {
        txtNoShares.setEditable(false);
        txtNoShares.setEnabled(false);
        if (CommonUtil.convertObjToDouble(txtShareValue.getText()) != 0 && CommonUtil.convertObjToDouble(txtShareValue.getText()) > 0 && txtShareValue.getText() != null) {
            amtEntered = CommonUtil.convertObjToDouble(txtShareValue.getText());
            if (amtEntered > balAmount) {
                ClientUtil.showAlertWindow("Amount cant be greater than Balance amount!!!!");
                txtShareValue.setText("");
                txtShareTotAmount.setText("");
                txtNoShares.setText("");
                txtShareApplFee.setText("");
                txtTotShareFee.setText("");
                return;
            } else if (amtEntered == balAmount) {
                txtShareApplFee.setText("0");
                txtTotShareFee.setText("0");
                txtNoShares.setText(lblTotNoOfSharesCount.getText());
                txtShareTotAmount.setText(txtShareValue.getText());
                txtShareTotAmount.setEnabled(false);
                txtShareTotAmount.setEditable(false);
                observable.setTxtShareValue(txtShareValue.getText()) ;
                observable.setTxtTotShareFee(txtTotShareFee.getText()) ;
                observable.setTxtShareApplFee(txtShareApplFee.getText()) ;
                observable.setTxtShareMemFee(txtShareMemFee.getText()) ;
                observable.setTxtShareTotAmount(txtShareTotAmount.getText()) ;
            } else {
                faceValue = CommonUtil.convertObjToDouble(txtShareAmt.getText());
                if (((balAmount - amtEntered) % faceValue) != 0) {
                    ClientUtil.showAlertWindow("While partial withdrawal , after withdrawing amount , multiples of face value should remain as balance");
                    txtShareValue.setText("");
                    txtShareTotAmount.setText("");
                    txtNoShares.setText("");
                    txtShareApplFee.setText("");
                    txtTotShareFee.setText("");
                } else {
                    txtShareApplFee.setText("0");
                    txtTotShareFee.setText("0");
                    noOfShares = CommonUtil.convertObjToInt(amtEntered / faceValue);
                    txtNoShares.setText(CommonUtil.convertObjToStr(noOfShares));
                    txtShareTotAmount.setText(txtShareValue.getText());
                    txtShareTotAmount.setEnabled(false);
                    txtShareTotAmount.setEditable(false);
                    observable.setTxtShareValue(txtShareValue.getText()) ;
                    observable.setTxtTotShareFee(txtTotShareFee.getText()) ;
                    observable.setTxtShareApplFee(txtShareApplFee.getText()) ;
                    observable.setTxtShareMemFee(txtShareMemFee.getText()) ;
                    observable.setTxtShareTotAmount(txtShareTotAmount.getText()) ;
                }
            }
        } else {
            ClientUtil.showAlertWindow("Please Enter the amount to withdraw!!!!!!!");
            return;
        }
    }
}//GEN-LAST:event_txtShareValueFocusLost

private void txtShareValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtShareValueActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtShareValueActionPerformed

    private void btnLiabilityReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLiabilityReportActionPerformed

        TTIntegration ttIntgration = null;
        HashMap paramMap = new HashMap();
        paramMap.put("MemNo", txtShareAcctNo.getText());
        paramMap.put("AsOnDt", currDt.clone());
        ttIntgration.setParam(paramMap);
        ttIntgration.integration("MemberLiabilityRegisterDet");
    }//GEN-LAST:event_btnLiabilityReportActionPerformed

    private void txtEmpRefNoNewFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEmpRefNoNewFocusLost
        // TODO add your handling code here:
        //added by Anju Anand for Mantis Id: 10395
        if (CommonConstants.SAL_REC_MODULE != null && !CommonConstants.SAL_REC_MODULE.equals("") && CommonConstants.SAL_REC_MODULE.equals("Y")) {
            String empRefNoNew = "";
            String customerId = "";
            empRefNoNew = txtEmpRefNoNew.getText();
            customerId = txtCustId.getText();
            HashMap dataMap = new HashMap();
            dataMap.put("EMP_REF_NO", empRefNoNew);
            List list = null;
            list = ClientUtil.executeQuery("selectExistingEmpRefNo", dataMap);
            if (list != null && list.size() > 0) {
                HashMap resultMap = new HashMap();
                for (int i = 0; i < list.size(); i++) {
                    resultMap = (HashMap) list.get(i);
                    String custId = "";
                    custId = CommonUtil.convertObjToStr(resultMap.get("CUST_ID"));
                    if (customerId.equals(custId)) {
                    } else {
                        ClientUtil.showMessageWindow("This Employee Reference Number already exists..! Please change the number..!!");
                        txtEmpRefNoNew.setText("");
                        return;
                    }
                }
            }
        }
    }//GEN-LAST:event_txtEmpRefNoNewFocusLost

private void txtFromSl_NoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFromSl_NoFocusLost
// TODO add your handling code here:
    //Added by sreekrishnan
    if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
        if(CommonUtil.convertObjToDouble(txtNoShares.getText())>0){
            if(CommonUtil.convertObjToDouble(lblMaxSlNo.getText())>0){
                if(CommonUtil.convertObjToDouble(txtFromSl_No.getText())>0){
                    txtToSL_No.setText(CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(txtFromSl_No.getText())+(CommonUtil.convertObjToDouble(txtNoShares.getText())-1)));
                }
            }
        }
    }
}//GEN-LAST:event_txtFromSl_NoFocusLost

    private void btnShareActNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShareActNoActionPerformed
        // TODO add your handling code here:
        viewType = ClientConstants.VIEW_TYPE_EDIT;
        callView("SHARE_ACT_NUM");
    }//GEN-LAST:event_btnShareActNoActionPerformed

    private void txtShareAcctNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtShareAcctNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtShareAcctNoActionPerformed

    private void txtShareAcctNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtShareAcctNoFocusLost
        // TODO add your handling code here:
        HashMap whereMap =new HashMap();
        if (CommonUtil.convertObjToStr(txtShareAcctNo.getText()).length() > 0 && CommonUtil.convertObjToStr(txtCustId.getText()).length() == 0) {
            observable.balance=0;
            txtShareAcctNoFocusLost(whereMap);
        }
           // TODO add your handling code here:
        if (txtShareAcctNo.getText().length() > 0) {
            double dueAmount = 0.0;
            txtShareAcctNo.setText(CommonUtil.convertObjToStr(txtShareAcctNo.getText()).toUpperCase());
            observable.resetDrfTransListTable();
            String shareNo= CommonUtil.convertObjToStr(txtShareAcctNo.getText());
            HashMap supMap = new HashMap();
            supMap.put("SHARE_NO", shareNo);
            List lstSupName = ClientUtil.executeQuery("getDRFProdIdForSelectedItem", supMap);
            HashMap supMap1 = new HashMap();
            HashMap supMap2 = new HashMap();
            String payMode = null;
            if (lstSupName != null && lstSupName.size() > 0) {
                supMap1 = (HashMap) lstSupName.get(0);
                prodId = CommonUtil.convertObjToStr(supMap1.get("DRF_PROD_ID"));
                payMode = CommonUtil.convertObjToStr(supMap1.get("RECIEPT_OR_PAYMENT"));
            }

            HashMap memberDetMap = new HashMap();
            memberDetMap.put("MEMBERSHIP_NO", CommonUtil.convertObjToStr(txtShareAcctNo.getText()));
            memberDetMap.put("PROD_ID", CommonUtil.convertObjToStr(prodId));
//            this query is to see if the member no entered is correct
            if (payMode == "PAYMENT") {
                List list1 = ClientUtil.executeQuery("getPaymentDetails", memberDetMap);
                List list = ClientUtil.executeQuery("getRecieptDetails", memberDetMap);
                memberDetMap.put("PAYMENT", "PAYMENT");
                List unauthList = ClientUtil.executeQuery("getSelectUnAuthList", memberDetMap);
                if (unauthList != null && unauthList.size() > 0) {
                    ClientUtil.displayAlert("Already Record exists for authorization can not add a new record");
                    txtShareAcctNo.setText("");
                    return;
                }
                int z = 0;
                if (list != null && list.size() > 0) {

                    if (list1 != null && list1.size() > 0 && z == 0) {

                        int a = ClientUtil.confirmationAlert("Already One Payment is there, Do you want to continue?");
                        int b = 0;
                        z = 1;
                        if (a != b) {
                            txtShareAcctNo.setText("");
                            return;
                        }
                    }
                } else {
                    ClientUtil.displayAlert("No receipt entry available for this member hence no payment can be made");
                    txtShareAcctNo.setText("");
                    return;
                }
            }
            if (payMode=="RECIEPT") {
                memberDetMap.put("RECIEPT", "RECIEPT");
                List unauthList = ClientUtil.executeQuery("getSelectUnAuthList", memberDetMap);
                if (unauthList != null && unauthList.size() > 0) {
                    ClientUtil.displayAlert("Already Record exists for authorization can not add a new record");
                    txtShareAcctNo.setText("");
                    return;
                }
            }
            List memberDetails = ClientUtil.executeQuery("getMemberDetailsForDrf", memberDetMap);
            if (memberDetails != null & memberDetails.size() > 0) {
                memberDetMap = (HashMap) memberDetails.get(0);
                memberDetMap.put("PROD_ID", CommonUtil.convertObjToStr(prodId));
                List memberDrfTransDetails = ClientUtil.executeQuery("getMemberDrfTransDetails", memberDetMap);
                if (memberDrfTransDetails != null && memberDrfTransDetails.size() > 0) {
                    double amountPaid = 0.0;
                    observable.populateDrfTransTable(memberDrfTransDetails);
                    for (int i = 0; i < memberDrfTransDetails.size(); i++) {
                        HashMap memberTransDetailsMap = (HashMap) memberDrfTransDetails.get(i);
                        amountPaid = amountPaid + CommonUtil.convertObjToDouble(memberTransDetailsMap.get("AMOUNT")).doubleValue();
                        memberTransDetailsMap.put("PROD_ID", CommonUtil.convertObjToStr(prodId));
                    }
                } else {
                    HashMap memberTransDetailsMap = new HashMap();
                    memberTransDetailsMap.put("PROD_ID", CommonUtil.convertObjToStr(prodId));
                }
            } else {
                ClientUtil.showAlertWindow("Entered Membership Number not found");
            }
        }
        //ClientUtil.displayAlert("Principle Outstanding  : "+(CommonUtil.convertObjToStr(observable.balance)));
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
        btnNew.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
    }//GEN-LAST:event_txtShareAcctNoFocusLost
    private void EnableDisbleMobileBanking(boolean flag) {
        txtMobileNo.setEnabled(flag);
        tdtMobileSubscribedFrom.setEnabled(flag);
    }
    private void chkMobileBankingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkMobileBankingActionPerformed
        // TODO add your handling code here:
        if (chkMobileBanking.isSelected()) {
            if(txtCustId.getText().length()>0){
                long mobileNo = observable.getMobileNo(CommonUtil.convertObjToStr(txtCustId.getText()));
                if(mobileNo != 0){
                    txtMobileNo.setText(CommonUtil.convertObjToStr(mobileNo));
                    tdtMobileSubscribedFrom.setDateValue(CommonUtil.convertObjToStr(currDt.clone()));
                }
            }
            observable.setIsMobileBanking(chkMobileBanking.isSelected());
            EnableDisbleMobileBanking(true);
        } else {
            EnableDisbleMobileBanking(false);
            txtMobileNo.setText("");
            tdtMobileSubscribedFrom.setDateValue("");
        }
    }//GEN-LAST:event_chkMobileBankingActionPerformed

    private void txtMobileNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMobileNoFocusLost
        // TODO add your handling code here:
        tdtMobileSubscribedFrom.setDateValue(CommonUtil.convertObjToStr(currDt.clone()));
    }//GEN-LAST:event_txtMobileNoFocusLost

    private void txtDivAcNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDivAcNoFocusLost
        // TODO add your handling code here:
        // Added by nithya for KD-3476
        HashMap hash = new HashMap();
        String ACCOUNTNO = (String) txtDivAcNo.getText();
        observable.setCboDivProdType("");
        if ((!(observable.getCboDivProdType().length()>0)) && ACCOUNTNO.length()>0) {
            if (observable.checkAcNoWithoutProdType(ACCOUNTNO,null,false)) {
                txtDivAcNo.setText(observable.getTxtDivAcNo());
                ACCOUNTNO = (String) txtDivAcNo.getText();
                cboMemDivProdID.setModel(observable.getCbmDivProdID());
                cboMemDivProdIDActionPerformed(null);
                //                txtDivAcNo.setText(ACCOUNTNO);
                String prodType = ((ComboBoxModel)cboMemDivProdType.getModel()).getKeyForSelected().toString();
                
                
            } else {
                ClientUtil.showAlertWindow("Invalid Account No.");
                txtDivAcNo.setText("");
                return;
            }
        }
    }//GEN-LAST:event_txtDivAcNoFocusLost
    
    private void lblDrfAmtValFocusGained(java.awt.event.FocusEvent evt) {                                         
          if(lblDrfAmtVal.getText()!=null){
           observable.setProductAmount(lblDrfAmtVal.getText());
          }
}                                               
    private void duplicateIDVisible(boolean duplicateFlag){
        txtIDResolutionNo.setVisible(duplicateFlag);
        lblIDResolutionNo.setVisible(duplicateFlag);
        lblIDResolutionDt.setVisible(duplicateFlag);
        tdtIDResolutionDt.setVisible(duplicateFlag);
        txtIDResolutionNo.setEnabled(duplicateFlag);
        tdtIDResolutionDt.setEnabled(duplicateFlag);
        
    }

    private void cancelOperation(){
        if(observable!=null){
            System.out.println("rishad eentered");
            isFilled = false;
            observable.resetForm();
            setLables();
            btnCustomerIdFileOpen.setEnabled(false);
            setBtnShareAcctDet(false);
            setBtnShareJoint(false);
            ClientUtil.enableDisable(this, false);
            nomineeUi.resetTable();
            nomineeUi.resetNomineeData();
            nomineeUi.resetNomineeTab();
            nomineeUi.disableNewButton(false);
            drfNomineeUi.resetTable();//Added By Revathi.L
            drfNomineeUi.resetNomineeData();
            drfNomineeUi.resetNomineeTab();
            drfNomineeUi.disableNewButton(false);
            observable.makeToNull();
            observable.ttNotifyObservers();
            cboMemDivPayMode.setSelectedItem("");
            cboMemDivProdID.setSelectedItem("");
            cboMemDivProdType.setSelectedItem("");
            cboDrfProdId.setSelectedItem("");
            lblTotNoOfSharesCount.setText("");
            panShareTrans.setVisible(false);
            panDrfApplicablle.setVisible(false);
            rdoShareAddition.setSelected(false);
            rdoSharewithDrawal.setSelected(false);
            rdoShareAddition.setSelected(false);
            rdoSharewithDrawal.setSelected(false);
            customersShare = 0.0;
            govtsShare = 0.0;
            subsidyFlag = false;
            cboDrfProdId.setVisible(false);
            lblDrfProdId.setVisible(false);
            resetTransactionUI();
              if (fromNewAuthorizeUI) {
                this.dispose();
                fromNewAuthorizeUI = false;
                newauthorizeListUI.setFocusToTable();
            }
            if (fromAuthorizeUI) {
                this.dispose();
                fromAuthorizeUI = false;
                authorizeListUI.setFocusToTable();
            }
             if (fromCashierAuthorizeUI) {
                this.dispose();
                fromCashierAuthorizeUI = false;
                CashierauthorizeListUI.setFocusToTable();
            }
            if (fromManagerAuthorizeUI) {
                this.dispose();
                fromManagerAuthorizeUI = false;
                ManagerauthorizeListUI.setFocusToTable();
            }
            //            transactionUI.resetObjects();
            //            tabTransaction.remove(1);
            //            observable = null;
              btnNew.setEnabled(false);
              setButtonEnableDisable();
        }
        shareFeeTax="";memFeeTax="";applFeeTax="";
        
    }
    
    private void clearProdFields() {
        //            txtAccHdId.setText("");
        //        txtDivAcNo.setText("");
        //            lblAccHdDesc.setText("");
        //            lblAccName.setText("");
    }

    public boolean chkmoreThanone(){
        if(tblShareAcctDet.getRowCount()>0){
            for (int i=0;i<tblShareAcctDet.getRowCount();i++){
                if(CommonUtil.convertObjToStr(tblShareAcctDet.getValueAt(i,5)).equals("")){
                    return true;
                }
            }
            
        }
        
        return false;
    }
    
    public void totalShareCount(){
        double shareCount=0;
        if(tblShareAcctDet.getRowCount()>0){
            for (int i=0;i<tblShareAcctDet.getRowCount();i++){
                if(CommonUtil.convertObjToStr(tblShareAcctDet.getValueAt(i,5)).equals("AUTHORIZED")){
                    if(CommonUtil.convertObjToStr(tblShareAcctDet.getValueAt(i,4)).equals("ADD")){
                        shareCount=shareCount+CommonUtil.convertObjToDouble(tblShareAcctDet.getValueAt(i,2)).doubleValue();
                        
                    } else if (CommonUtil.convertObjToStr(tblShareAcctDet.getValueAt(i, 4)).equals("WITHDRAWAL")) {
                        shareCount=shareCount-CommonUtil.convertObjToDouble(tblShareAcctDet.getValueAt(i,2)).doubleValue();
                    }
                }
            }
            
        }
        
        lblTotNoOfSharesCount.setText(CommonUtil.convertObjToStr(new Double(shareCount)));
    }
    
    //KD-3609 : drf transaction comes up wrongly in additional share screen 
    private void setTransAmountShareAddition() {
        double shareApplFee = 0.0;
        double ShareMemFee = 0.0;
        HashMap taxMap;
        double totShreFee = 0.0;
        double totAmt = 0.0;
        double taxAmt = 0.0;
        double totShareValue = 0.0;
        totShreFee = CommonUtil.convertObjToDouble(txtNoShares.getText()).doubleValue()
                * CommonUtil.convertObjToDouble(txtShareFee.getText()).doubleValue();
        shareApplFee = CommonUtil.convertObjToDouble(txtShareApplFee.getText());
        ShareMemFee = CommonUtil.convertObjToDouble(txtShareMemFee.getText());
        totShareValue = CommonUtil.convertObjToDouble(txtNoShares.getText()).doubleValue()
                * CommonUtil.convertObjToDouble(txtShareAmt.getText()).doubleValue();
        totAmt = totShareValue + totShreFee + shareApplFee + ShareMemFee;       
       
        double totshareAmt = CommonUtil.convertObjToDouble(observable.getLblServiceTaxval())
                + totAmt;
        if (chkDrfApplicableYN.isSelected() == true && cboDrfProdId.getSelectedItem() != null && drfExist == false) {
            totshareAmt += CommonUtil.convertObjToDouble(observable.getProductAmount()).doubleValue();
        }
        transactionUI.setCallingAmount(String.valueOf(totshareAmt));
    }
    
    public int shareRowCount(int rowCount){
        int shareCount=0;
        if(tblShareAcctDet.getRowCount()>0){
            for (int i=0;i<tblShareAcctDet.getRowCount();i++){
                
                shareCount=CommonUtil.convertObjToInt(tblShareAcctDet.getValueAt(i,0));
                if (rowCount == shareCount) {
                    return i+1;
            }
            }
            
        }
        
        return  shareCount;
    }
    
    public void setLables(){
        lblValArea.setText(observable.getLblValArea());
        lblValCity.setText(observable.getLblValCity());
        lblValCountry.setText(observable.getLblValCountry());
        lblValCustomerName.setText(observable.getLblValCustomerName());
        lblValDateOfBirth.setText(observable.getLblValDateOfBirth());
        lblValPin.setText(observable.getLblValPin());
        lblValState.setText(observable.getLblValState());
        lblValStreet.setText(observable.getLblValStreet());
        lblValCustName.setText(observable.getLblValCustName());
    }
    
    private void setBtnShareAcctDet(boolean val){
        btnShareAcctDetNew.setEnabled(val);
        btnShareAcctDetSave.setEnabled(val);
        btnShareAcctDetDel.setEnabled(val);
    }
    
    private void setBtnShareJoint(boolean val){
        btnJointAcctDel.setEnabled(val);
        btnJointAcctNew.setEnabled(val);
        btnJointAcctToMain.setEnabled(val);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CLabel FromSL_No;
    private com.see.truetransact.uicomponent.CButton RsolutionSearch;
    private com.see.truetransact.uicomponent.CLabel ToSL_No;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCustomerIdFileOpen;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDivAcNoFileOpen;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnIdPrint;
    private com.see.truetransact.uicomponent.CButton btnJointAcctDel;
    private com.see.truetransact.uicomponent.CButton btnJointAcctNew;
    private com.see.truetransact.uicomponent.CButton btnJointAcctToMain;
    private com.see.truetransact.uicomponent.CButton btnLiabilityReport;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnResolution;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnShareAcctDetDel;
    private com.see.truetransact.uicomponent.CButton btnShareAcctDetNew;
    private com.see.truetransact.uicomponent.CButton btnShareAcctDetSave;
    private com.see.truetransact.uicomponent.CButton btnShareActNo;
    private com.see.truetransact.uicomponent.CButton btnShareAppNo;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CComboBox cboAcctStatus;
    private com.see.truetransact.uicomponent.CComboBox cboCommAddrType;
    private com.see.truetransact.uicomponent.CComboBox cboConstitution;
    private com.see.truetransact.uicomponent.CComboBox cboDrfProdId;
    private com.see.truetransact.uicomponent.CComboBox cboMemDivPayMode;
    private com.see.truetransact.uicomponent.CComboBox cboMemDivProdID;
    private com.see.truetransact.uicomponent.CComboBox cboMemDivProdType;
    private com.see.truetransact.uicomponent.CComboBox cboShareType;
    private com.see.truetransact.uicomponent.CCheckBox chkDrfApplicableYN;
    private com.see.truetransact.uicomponent.CCheckBox chkDuplicateIDCardYN;
    private com.see.truetransact.uicomponent.CCheckBox chkMobileBanking;
    private com.see.truetransact.uicomponent.CCheckBox chkNotEligibleStatus;
    private com.see.truetransact.uicomponent.CLabel jLabel3;
    private com.see.truetransact.uicomponent.CPanel jPanel2;
    private com.see.truetransact.uicomponent.CLabel lblAcctStatus;
    private com.see.truetransact.uicomponent.CLabel lblApplFee;
    private com.see.truetransact.uicomponent.CLabel lblApplicationNo;
    private com.see.truetransact.uicomponent.CLabel lblArea;
    private com.see.truetransact.uicomponent.CLabel lblBalToBePaid;
    private com.see.truetransact.uicomponent.CLabel lblCity;
    private com.see.truetransact.uicomponent.CLabel lblCommAddrType;
    private com.see.truetransact.uicomponent.CLabel lblConnGrpDet;
    private com.see.truetransact.uicomponent.CLabel lblConstitution;
    private com.see.truetransact.uicomponent.CLabel lblCountry;
    private com.see.truetransact.uicomponent.CLabel lblCustId;
    private com.see.truetransact.uicomponent.CLabel lblCustomerName;
    private com.see.truetransact.uicomponent.CLabel lblDRFAmt;
    private com.see.truetransact.uicomponent.CLabel lblDateOfBirth;
    private com.see.truetransact.uicomponent.CLabel lblDirRelDet;
    private com.see.truetransact.uicomponent.CLabel lblDivAmt;
    private com.see.truetransact.uicomponent.CLabel lblDivAmtVal;
    private com.see.truetransact.uicomponent.CTextField lblDrfAmtVal;
    private com.see.truetransact.uicomponent.CLabel lblDrfProdId;
    private com.see.truetransact.uicomponent.CLabel lblDtIssId;
    private com.see.truetransact.uicomponent.CLabel lblDtNotEligiblePeriod;
    private com.see.truetransact.uicomponent.CLabel lblDtShareDetIssShareCert;
    private com.see.truetransact.uicomponent.CLabel lblDuplicateIDCardYN;
    private com.see.truetransact.uicomponent.CLabel lblEmpRefNoNew;
    private com.see.truetransact.uicomponent.CLabel lblEmpRefNoOld;
    private javax.swing.JLabel lblExistCust;
    private com.see.truetransact.uicomponent.CLabel lblIDCardNo;
    private com.see.truetransact.uicomponent.CLabel lblIDIssuedDt;
    private com.see.truetransact.uicomponent.CLabel lblIDResolutionDt;
    private com.see.truetransact.uicomponent.CLabel lblIDResolutionNo;
    private com.see.truetransact.uicomponent.CLabel lblImbp;
    private com.see.truetransact.uicomponent.CLabel lblMaxSlNo;
    private com.see.truetransact.uicomponent.CLabel lblMaxSlNoValue;
    private com.see.truetransact.uicomponent.CLabel lblMemDivAcNo;
    private com.see.truetransact.uicomponent.CLabel lblMemDivPayMode;
    private com.see.truetransact.uicomponent.CLabel lblMemDivProdId;
    private com.see.truetransact.uicomponent.CLabel lblMemDivProdType;
    private com.see.truetransact.uicomponent.CLabel lblMemFee;
    private com.see.truetransact.uicomponent.CLabel lblMobileNo;
    private com.see.truetransact.uicomponent.CLabel lblMobileSubscribedFrom;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNoShares;
    private com.see.truetransact.uicomponent.CLabel lblNotEligibleStatus;
    private com.see.truetransact.uicomponent.CLabel lblPin;
    private com.see.truetransact.uicomponent.CLabel lblPropertyDetails;
    private com.see.truetransact.uicomponent.CLabel lblRelativeDetails;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblResolutionNo;
    private com.see.truetransact.uicomponent.CLabel lblResolutionNo1;
    private com.see.truetransact.uicomponent.CLabel lblServiceTax;
    private com.see.truetransact.uicomponent.CLabel lblServiceTaxval;
    private com.see.truetransact.uicomponent.CLabel lblShareAcctNo;
    private com.see.truetransact.uicomponent.CLabel lblShareAmt;
    private com.see.truetransact.uicomponent.CLabel lblShareDetNoOfShares;
    private com.see.truetransact.uicomponent.CLabel lblShareDetShareNoFrom;
    private com.see.truetransact.uicomponent.CLabel lblShareDetShareNoTo;
    private com.see.truetransact.uicomponent.CLabel lblShareFee;
    private com.see.truetransact.uicomponent.CLabel lblShareType;
    private com.see.truetransact.uicomponent.CLabel lblShareValue;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace10;
    private com.see.truetransact.uicomponent.CLabel lblSpace11;
    private com.see.truetransact.uicomponent.CLabel lblSpace12;
    private com.see.truetransact.uicomponent.CLabel lblSpace13;
    private com.see.truetransact.uicomponent.CLabel lblSpace14;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblSpace8;
    private com.see.truetransact.uicomponent.CLabel lblSpace9;
    private com.see.truetransact.uicomponent.CLabel lblState;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblStreet;
    private com.see.truetransact.uicomponent.CLabel lblTotCollectionAmount;
    private com.see.truetransact.uicomponent.CLabel lblTotNoOfShares;
    private com.see.truetransact.uicomponent.CLabel lblTotNoOfSharesCount;
    private com.see.truetransact.uicomponent.CLabel lblTotNoOfSharesCount1;
    private com.see.truetransact.uicomponent.CLabel lblTotNoOfSharesCount2;
    private com.see.truetransact.uicomponent.CLabel lblTotshareBal;
    private com.see.truetransact.uicomponent.CLabel lblTotshareBalVal;
    private com.see.truetransact.uicomponent.CLabel lblTransType;
    private com.see.truetransact.uicomponent.CLabel lblTransType1;
    private com.see.truetransact.uicomponent.CLabel lblValArea;
    private com.see.truetransact.uicomponent.CLabel lblValCity;
    private com.see.truetransact.uicomponent.CLabel lblValCountry;
    private com.see.truetransact.uicomponent.CLabel lblValCustName;
    private com.see.truetransact.uicomponent.CLabel lblValCustomerName;
    private com.see.truetransact.uicomponent.CLabel lblValDateOfBirth;
    private com.see.truetransact.uicomponent.CLabel lblValPin;
    private com.see.truetransact.uicomponent.CLabel lblValState;
    private com.see.truetransact.uicomponent.CLabel lblValStreet;
    private com.see.truetransact.uicomponent.CLabel lblWelFund;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitAuthorize;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitException;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitReject;
    private javax.swing.JMenuItem mitResolution;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAccountOpening;
    private com.see.truetransact.uicomponent.CPanel panBtnShareAcctDet;
    private com.see.truetransact.uicomponent.CPanel panBtnShareAcctDet1;
    private com.see.truetransact.uicomponent.CPanel panCudtomerIDDetails;
    private com.see.truetransact.uicomponent.CPanel panCustId;
    private com.see.truetransact.uicomponent.CPanel panCustId1;
    private com.see.truetransact.uicomponent.CPanel panCustId2;
    private com.see.truetransact.uicomponent.CPanel panCustId3;
    private com.see.truetransact.uicomponent.CPanel panCustomerName;
    private com.see.truetransact.uicomponent.CPanel panCustomerSide;
    private com.see.truetransact.uicomponent.CPanel panDireRelDet;
    private com.see.truetransact.uicomponent.CPanel panDrfApplicablle;
    private javax.swing.JPanel panExistCust;
    private com.see.truetransact.uicomponent.CPanel panJointAcctButton;
    private com.see.truetransact.uicomponent.CPanel panJointAcctHolder;
    private com.see.truetransact.uicomponent.CPanel panLoanDetails;
    private com.see.truetransact.uicomponent.CPanel panMobileBanking;
    private com.see.truetransact.uicomponent.CPanel panOtherDet;
    private com.see.truetransact.uicomponent.CPanel panOtherDetails;
    private com.see.truetransact.uicomponent.CPanel panShare;
    private com.see.truetransact.uicomponent.CPanel panShareAcctDet;
    private com.see.truetransact.uicomponent.CPanel panShareAcctDetails;
    private com.see.truetransact.uicomponent.CPanel panShareAcctInfo;
    private javax.swing.JPanel panShareAcctNo;
    private com.see.truetransact.uicomponent.CPanel panShareDet;
    private javax.swing.JPanel panShareImbp;
    private com.see.truetransact.uicomponent.CPanel panShareTrans;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTblShareAcctDet;
    private com.see.truetransact.uicomponent.CPanel panTransaction;
    private javax.swing.JRadioButton rdoExistCustNo;
    private javax.swing.JRadioButton rdoExistCustYes;
    private com.see.truetransact.uicomponent.CRadioButton rdoShareAddition;
    private com.see.truetransact.uicomponent.CRadioButton rdoShareDifferentialAmount;
    private com.see.truetransact.uicomponent.CRadioButton rdoSharewithDrawal;
    private com.see.truetransact.uicomponent.CButtonGroup rgbExistCust;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private javax.swing.JSeparator sptException;
    private javax.swing.JSeparator sptResolution;
    private com.see.truetransact.uicomponent.CScrollPane srpConnGrpDet;
    private com.see.truetransact.uicomponent.CScrollPane srpDirRelDet;
    private com.see.truetransact.uicomponent.CScrollPane srpDrfTransaction;
    private com.see.truetransact.uicomponent.CScrollPane srpJointAcctHolder;
    private com.see.truetransact.uicomponent.CScrollPane srpLoanAcctHolder;
    private com.see.truetransact.uicomponent.CScrollPane srpPropertyDetails;
    private com.see.truetransact.uicomponent.CScrollPane srpRelativeDetails;
    private com.see.truetransact.uicomponent.CScrollPane srpRemarks;
    private com.see.truetransact.uicomponent.CScrollPane srpTblShareAccntDet;
    private com.see.truetransact.uicomponent.CScrollPane srpWelFund;
    private com.see.truetransact.uicomponent.CTabbedPane tabShare;
    private com.see.truetransact.uicomponent.CTabbedPane tabTransaction;
    private com.see.truetransact.uicomponent.CTable tblDrfTransaction;
    private com.see.truetransact.uicomponent.CTable tblJointAcctHolder;
    private com.see.truetransact.uicomponent.CTable tblLoanAcctHolder;
    private com.see.truetransact.uicomponent.CTable tblShareAcctDet;
    private javax.swing.JToolBar tbrShareAcct;
    private com.see.truetransact.uicomponent.CDateField tdtIDIssuedDt;
    private com.see.truetransact.uicomponent.CDateField tdtIDResolutionDt;
    private com.see.truetransact.uicomponent.CDateField tdtIssId;
    private com.see.truetransact.uicomponent.CDateField tdtMobileSubscribedFrom;
    private com.see.truetransact.uicomponent.CDateField tdtNotEligiblePeriod;
    private com.see.truetransact.uicomponent.CDateField tdtShareDetIssShareCert;
    private com.see.truetransact.uicomponent.CTextField txtApplFee;
    private com.see.truetransact.uicomponent.CTextField txtApplicationNo;
    private com.see.truetransact.uicomponent.CTextArea txtConnGrpDet;
    public static com.see.truetransact.uicomponent.CTextField txtCustId;
    private com.see.truetransact.uicomponent.CTextArea txtDirRelDet;
    private com.see.truetransact.uicomponent.CTextField txtDivAcNo;
    private com.see.truetransact.uicomponent.CTextField txtEmpRefNoNew;
    private com.see.truetransact.uicomponent.CTextField txtEmpRefNoOld;
    private com.see.truetransact.uicomponent.CTextField txtFromSl_No;
    private com.see.truetransact.uicomponent.CTextField txtIDCardNo;
    private com.see.truetransact.uicomponent.CTextField txtIDResolutionNo;
    private com.see.truetransact.uicomponent.CTextField txtImbp;
    private com.see.truetransact.uicomponent.CTextField txtMemFee;
    private com.see.truetransact.uicomponent.CTextField txtMobileNo;
    private com.see.truetransact.uicomponent.CTextField txtNextAccNo;
    private com.see.truetransact.uicomponent.CTextField txtNoShares;
    private com.see.truetransact.uicomponent.CTextArea txtPropertyDetails;
    private com.see.truetransact.uicomponent.CTextArea txtRelativeDetails;
    private com.see.truetransact.uicomponent.CTextArea txtRemarks;
    private com.see.truetransact.uicomponent.CTextField txtResolutionNo;
    private com.see.truetransact.uicomponent.CTextField txtResolutionNo1;
    private com.see.truetransact.uicomponent.CTextField txtShareAcctNo;
    private com.see.truetransact.uicomponent.CTextField txtShareAmt;
    private com.see.truetransact.uicomponent.CTextField txtShareApplFee;
    private com.see.truetransact.uicomponent.CTextField txtShareFee;
    private com.see.truetransact.uicomponent.CTextField txtShareMemFee;
    private com.see.truetransact.uicomponent.CTextField txtShareTotAmount;
    private com.see.truetransact.uicomponent.CTextField txtShareValue;
    private com.see.truetransact.uicomponent.CTextField txtToSL_No;
    private com.see.truetransact.uicomponent.CTextField txtTotShareFee;
    private com.see.truetransact.uicomponent.CTextArea txtWelFund;
    // End of variables declaration//GEN-END:variables
}
