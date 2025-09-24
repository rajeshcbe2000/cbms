/*
 * CashTransaction.java
 *
 * Created on February 25, 2004, 11:55 AM
 */
package com.see.truetransact.ui.transaction.cash;

import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
//import com.see.truetransact.uimandatory.MandatoryDBCheck;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.common.authorizewf.AuthorizeWFUI;
//import com.see.truetransact.ui.common.authorizewf.AuthorizeWFCheckUI;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.common.denomination.DenominationUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.LocaleConstants;
import com.see.truetransact.uicomponent.CButtonGroup;// To add and Remove the Radio Buttons...
//import com.see.truetransact.ui.common.viewall.TableDialogUI;
//import com.see.truetransact.ui.common.viewphotosign.ViewPhotoSignUI;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.transaction.common.TransDetailsUI;
import com.see.truetransact.ui.deposit.TermDepositUI;
import com.see.truetransact.transferobject.product.operativeacct.OperativeAcctProductTO;
import com.see.truetransact.ui.transaction.common.TransCommonUI;
import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.*;
import com.see.truetransact.commonutil.Dummy;
import com.see.truetransact.commonutil.SuspiciousAccountValidation;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;
import com.see.truetransact.ui.common.viewall.*;
//import java.util.Observer;
import org.apache.log4j.Logger;
//import java.util.Date;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
import com.see.truetransact.ui.termloan.LoanLiabilityUI;
import java.util.*;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListDebitUI;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListCreditUI;
import com.see.truetransact.ui.customer.SmartCustomerUI;
import java.util.Date;
import javax.swing.*;

/**
 *
 * @author rahul, bala @todoh Add other modules into the transaction
 *
 */
public class CashTransactionUI extends CInternalFrame implements java.util.Observer, UIMandatoryField {

    public GLAccountNumberListUI glAccountNumberListUI;
    private static com.see.truetransact.uicomponent.CDesktopPane dtpTTMain;
    private HashMap mandatoryMap;
    CashTransactionOB observable;
    final int EDIT = 0, DELETE = 1, ACCNO = 2, AUTHORIZE = 3, ACCTHDID = 4, VIEW = 5, LINK_BATCH_TD = 6, LINK_BATCH = 7, DEBIT_DETAILS = 8, PAN_NUM = 9, TELLER_ENTRY_DETIALS = 10;
    int viewType = -1;
    boolean isFilled = false;
    private TransDetailsUI transDetails = null;
    java.util.ResourceBundle resourceBundle, objMandatoryRB;
    private TransCommonUI transCommonUI = null;
    private boolean flag = false;
    private boolean flagDeposit = false;
    private boolean flagDepAuth = false;
    private boolean flagDepLink = false;
    private boolean flagDepositAuthorize = false;
    private boolean afterSaveCancel = false;
    private CTextField txtDepositAmount;
    private double intAmtDep = 0.0;
    TermDepositUI termDepositUI;
    public String designation = "";
    private String custStatus = "";
    private String depBehavesLike = "";
    private HashMap intMap = new HashMap();
    private boolean reconcilebtnDisable = false;
    ArrayList termLoanDetails = null;
    private boolean termLoanDetailsFlag = false;
    HashMap termLoansDetailsMap = null;
    private String depPartialWithDrawalAllowed = "";
    private final static Logger log = Logger.getLogger(CashTransactionUI.class);     //Logger
    List chqBalList = null;
    private HashMap asAndWhenMap = null;
    private long noofInstallment2 = 0;
    boolean fromAuthorizeUI = false;
    String cashNode = "";
    boolean fromCashierAuthorizeUI = false;
    boolean fromManagerAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
    AuthorizeListDebitUI ManagerauthorizeListUI = null;
    AuthorizeListCreditUI CashierauthorizeListUI = null;
    boolean fromSmartCustUI = false;
    SmartCustomerUI smartUI = null;
    private double orgOrRespAmout = 0.0;
    private String orgOrRespBranchId = "";
    private String orgOrRespBranchName = "";
    private String orgBranch = "";
    private String orgBranchName = "";
    private String orgOrRespCategory = "";
    private String orgOrRespAdviceNo = "";
    private java.util.Date orgOrRespAdviceDt = null;
    private String orgOrRespTransType = "";
    private String orgOrRespDetails = "";
    ViewOrgOrRespUI vieworgOrRespUI = null;
    ViewRespUI viewRespUI = null;
    private int rejectFlag = 0;
    public String accNo1 = "";
    public String acctDesc = "";
    public String prodIdforgl = "";
    public String prodtypeforgl = "";
    double totAmt1 = 0.0;
    double penal = 0.0;
    public boolean isDepositIntBulk = false;
    //added by rishad for listing waive detales and enter waive in this grid
    private EditWaiveTableUI editableWaiveUI = null;
    HashMap returnWaiveMap = null;
    private boolean kccFlag = false;
    private double actInterest = 0;
    HashMap serviceTax_Map = new HashMap();
    ServiceTaxCalculation objServiceTax;
    public String linkBatchId = "", single_transId = "";
    HashMap allWaiveMap = new HashMap();
    boolean isValid = false;
    private Date currDt = null;
    private Date interestPaidUpto = null;
    private String lockAccount="";
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;

        
    /**
     * Creates new form CashTransaction
     */
    public CashTransactionUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initSetup();

        transDetails = new TransDetailsUI(panLableValues);
        transDetails.setSourceScreen("CASH");
        lblInputCurrency.setVisible(false);
        cboInputCurrency.setVisible(false);
        lblInputAmt.setVisible(false);
        txtInputAmt.setVisible(false);
        btnCurrencyInfo.setVisible(false);
        btnDelete.setEnabled(false);
        btnDebitDetails.setEnabled(true);
        btnDebitDetails.setVisible(true);
        btnPanNo.setEnabled(false);
        btnViewTermLoanDetails.setVisible(true);
        btnViewTermLoanDetails.setEnabled(false);
        btnWaive.setVisible(true);
        btnWaive.setEnabled(false);
        btnDenomination.setVisible(false);
        rdoTransactionType_Credit.setSelected(true);
        lblLoanStatus.setText("");
    }

    private void initSetup() {
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setObservable();
        initComponentData();                    // Fill all the combo boxes...
        setMaxLenths();                         // To set the Numeric Validation and the Maximum length of the Text fields...
        ClientUtil.enableDisable(this, false);  // Disables all when the screen appears for the 1st time
        setButtonEnableDisable();               // Enables/Disables the necessary buttons and menu items...
        enableDisableButtons(false);                 // To disable the buttons(folder) in the Starting...
        observable.resetForm();                 // To reset all the fields in UI...
        observable.resetLable();                // To reset all the Lables in UI...
        observable.resetStatus();               // To reset the Satus in the UI...

        objMandatoryRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.transaction.cash.CashTransactionMRB", ProxyParameters.LANGUAGE);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panCashTransaction);

        setHelpMessage();

        // Hide unwanted fields
        /*
         * lblInputAmt.setVisible(false); lblInputCurrency.setVisible(false);
         * txtInputAmt.setVisible(false); cboInputCurrency.setVisible(false);
         *
         * lbl .setVisible(false); cboProdType.setVisible(false);
         * btnAccHdId.setVisible(false);
         */

        //(new MandatoryDBCheck()).setComponentInit(getClass().getName(), panCashTransaction);
        getUserDesignation();
        btnAccHdId.setEnabled(false);
        btnAccNo.setEnabled(false);
        btnOrgOrResp.setVisible(false);
    }

    // Creates The Instance of InwardClearingOB
    private void setObservable() {
        //        observable = CashTransactionOB.getInstance(this);
        try {
            observable = new CashTransactionOB();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Authorize Button to be added...
    private void setFieldNames() {
        lblAccNoGl.setName("lblAccNoGl");
        cboProdType.setName("cboProdType");
        lblProdType.setName("lblProdType");
        btnAccHdId.setName("btnAccHdId");
        txtAccHdId.setName("txtAccHdId");
        btnAccNo.setName("btnAccNo");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnSave.setName("btnSave");
        txtAccNo.setName("txtAccNo");
        txtInitiatorChannel.setName("txtInitiatorChannel");
        cboInputCurrency.setName("cboInputCurrency");
        cboInstrumentType.setName("cboInstrumentType");
        cboProdId.setName("cboProdId");
        lblAccHd.setName("lblAccHd");
        lblAccHdDesc.setName("lblAccHdDesc");
        //lblAccHdId.setName("lblAccHdId");
        lblAccName.setName("lblAccName");
        lblAccNo.setName("lblAccNo");
        lblAmount.setName("lblAmount");
        lblInitiatorChannel.setName("lblInitiatorChannel");
        lblInitiatorID.setName("lblInitiatorID");
        lblInitiatorIDDesc.setName("lblInitiatorIDDesc");
        lblInputAmt.setName("lblInputAmt");
        lblInputCurrency.setName("lblInputCurrency");
        lblInstrumentDate.setName("lblInstrumentDate");
        lblInstrumentNo.setName("lblInstrumentNo");
        lblInstrumentType.setName("lblInstrumentType");
        lblMsg.setName("lblMsg");
        lblParticulars.setName("lblParticulars");
        lblNarration.setName("lblNarration");
        lblProdId.setName("lblProdId");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblStatus.setName("lblStatus");
        lblTokenNo.setName("lblTokenNo");
        lblTransactionDate.setName("lblTransactionDate");
        lblTransactionDateDesc.setName("lblTransactionDateDesc");
        lblTransactionID.setName("lblTransactionID");
        lblTransactionIDDesc.setName("lblTransactionIDDesc");
        lblTransactionType.setName("lblTransactionType");
        mbrMain.setName("mbrMain");
        panAccHd.setName("panAccHd");
        panCashTransaction.setName("panCashTransaction");
        panData.setName("panData");
        panLableValues.setName("panLableValues");
        panLables.setName("panLables");
        panInstrumentNo.setName("panInstrumentNo");
        panStatus.setName("panStatus");
        panTransaction.setName("panTransaction");
        panTransactionType.setName("panTransactionType");
        rdoTransactionType_Credit.setName("rdoTransactionType_Credit");
        rdoTransactionType_Debit.setName("rdoTransactionType_Debit");
        tdtInstrumentDate.setName("tdtInstrumentDate");
        txtAmount.setName("txtAmount");
        txtInputAmt.setName("txtInputAmt");
        txtInstrumentNo1.setName("txtInstrumentNo1");
        txtInstrumentNo2.setName("txtInstrumentNo2");
        txtParticulars.setName("txtParticulars");
//      txtNarration.setName("txtNarration");
        txtTokenNo.setName("txtTokenNo");
        lblPanNo.setName("lblPanNo");
        txtPanNo.setName("txtPanNo");
        lblAuthBy.setName("lblAuthBy");
        lblAuthBydesc.setName("lblAuthBydesc");
        btnVer.setName("btnVer");
    }

    private void internationalize() {
        //CashTransactionRB resourceBundle = new CashTransactionRB();
        resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.transaction.cash.CashTransactionRB", ProxyParameters.LANGUAGE);
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblTokenNo.setText(resourceBundle.getString("lblTokenNo"));
        lblInstrumentType.setText(resourceBundle.getString("lblInstrumentType"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        ((javax.swing.border.TitledBorder) panTransaction.getBorder()).setTitle(resourceBundle.getString("panTransaction"));
        lblInitiatorChannel.setText(resourceBundle.getString("lblInitiatorChannel"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblTransactionID.setText(resourceBundle.getString("lblTransactionID"));
        lblInitiatorID.setText(resourceBundle.getString("lblInitiatorID"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblAccName.setText(resourceBundle.getString("lblAccName"));
        lblInputAmt.setText(resourceBundle.getString("lblInputAmt"));
        lblTransactionDate.setText(resourceBundle.getString("lblTransactionDate"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblTransactionDateDesc.setText(resourceBundle.getString("lblTransactionDateDesc"));
        //lblAccHdId.setText(resourceBundle.getString("lblAccHdId"));
        lblAccHdDesc.setText(resourceBundle.getString("lblAccHdDesc"));
        lblAccHd.setText(resourceBundle.getString("lblAccHd"));
        rdoTransactionType_Debit.setText(resourceBundle.getString("rdoTransactionType_Debit"));
        lblAccNo.setText(resourceBundle.getString("lblAccNo"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        rdoTransactionType_Credit.setText(resourceBundle.getString("rdoTransactionType_Credit"));
        lblTransactionType.setText(resourceBundle.getString("lblTransactionType"));
        lblInstrumentDate.setText(resourceBundle.getString("lblInstrumentDate"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        btnAccNo.setText(resourceBundle.getString("btnAccNo"));
        lblAmount.setText(resourceBundle.getString("lblAmount"));
        lblInstrumentNo.setText(resourceBundle.getString("lblInstrumentNo"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblProdId.setText(resourceBundle.getString("lblProdId"));
        lblProdType.setText(resourceBundle.getString("lblProdType"));
        ((javax.swing.border.TitledBorder) panData.getBorder()).setTitle(resourceBundle.getString("panData"));
        lblInputCurrency.setText(resourceBundle.getString("lblInputCurrency"));
        lblParticulars.setText(resourceBundle.getString("lblParticulars"));
        lblNarration.setText(resourceBundle.getString("lblNarration"));
        ((javax.swing.border.TitledBorder) panLableValues.getBorder()).setTitle(resourceBundle.getString("panLableValues"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblTransactionIDDesc.setText(resourceBundle.getString("lblTransactionIDDesc"));
        lblInitiatorIDDesc.setText(resourceBundle.getString("lblInitiatorIDDesc"));
        lblPanNo.setText(resourceBundle.getString("lblPanNo"));
        lblAuthBy.setText(resourceBundle.getString("lblAuthBy"));
        btnVer.setText(resourceBundle.getString("btnVer"));
    }

    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboProdId", new Boolean(true));
        mandatoryMap.put("cboProdType", new Boolean(true));
        mandatoryMap.put("cboAccNo", new Boolean(true));
        mandatoryMap.put("cboInitiatorChannel", new Boolean(true));
        mandatoryMap.put("rdoTransactionType_Debit", new Boolean(true));
        mandatoryMap.put("txtInputAmt", new Boolean(true));
        mandatoryMap.put("cboInputCurrency", new Boolean(true));
        mandatoryMap.put("cboInstrumentType", new Boolean(true));
        mandatoryMap.put("txtInstrumentNo1", new Boolean(true));
        mandatoryMap.put("txtInstrumentNo2", new Boolean(true));
        mandatoryMap.put("tdtInstrumentDate", new Boolean(true));
        mandatoryMap.put("txtTokenNo", new Boolean(true));
        mandatoryMap.put("txtAmount", new Boolean(true));
        mandatoryMap.put("txtAccHdId", new Boolean(true));
        mandatoryMap.put("txtParticulars", new Boolean(true));
//        mandatoryMap.put("txtNarration", new Boolean(false));
    }

    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    // To Reset the Radio Buttons in the UI after any operation, We've to
    //1. Remove the Radio Buttons fron the Radio Groups...
    //2. Add the Radio Buttons Back in The Radio Groups...
    //a.) To Remove the Radio buttons...
    private void removeRadioButtons() {
        //---Account---
        rdoTransactionType.remove(rdoTransactionType_Credit);
        rdoTransactionType.remove(rdoTransactionType_Debit);
    }

    // b.) To Add the Radio buttons...
    private void addRadioButtons() {
        //---Account---
        rdoTransactionType = new CButtonGroup();
        rdoTransactionType.add(rdoTransactionType_Credit);
        rdoTransactionType.add(rdoTransactionType_Debit);
    }

    public void update(Observable observed, Object arg) {

        removeRadioButtons();
        setSelectedBranchID(observable.getSelectedBranchID());
        if (observable.getCbmProdId() != null) {
            cboProdId.setModel(observable.getCbmProdId());
        }
        txtAccNo.setText(observable.getTxtAccNo());
        txtInitiatorChannel.setText(observable.getTxtInitiatorChannel());
        rdoTransactionType_Debit.setSelected(observable.getRdoTransactionType_Debit());
        rdoTransactionType_Credit.setSelected(observable.getRdoTransactionType_Credit());
        txtInputAmt.setText(observable.getTxtInputAmt());
        cboInstrumentType.setSelectedItem(observable.getCboInstrumentType());
        txtInstrumentNo1.setText(observable.getTxtInstrumentNo1());
        txtInstrumentNo2.setText(observable.getTxtInstrumentNo2());
        tdtInstrumentDate.setDateValue(observable.getTdtInstrumentDate());
        txtTokenNo.setText(observable.getTxtTokenNo());
        txtAmount.setText(observable.getTxtAmount());
        txtParticulars.setText(observable.getTxtParticulars());
        txtAreaParticular.setText(observable.getTxtNarration());
        txtAccHdId.setText(observable.getTxtAccHd());
        //        lblAccHdDesc.setText(observable.getLblAccHdDesc());
        //
        //        //To set the  Name of the Account Holder...
          String pType= ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
        // Added by Rajesh
        String prevActNum = observable.getLblAccName();
        //System.out.println("pType@@$$$$>>>" + pType);
        if (pType.length() > 0) {
            if (!pType.equals("GL")) {
                //System.out.println("22###@@@");
                observable.setAccountName(observable.getTxtAccNo());
                lblAccName.setText(observable.getLblAccName());
                lblHouseName.setText(observable.getLblHouseName());
                observable.setAccountHead();
                lblAccHdDesc.setText(observable.getLblAccHdDesc());
            } else {
                //System.out.println("33###@@@>>>" + observable.getLinkBatchIdForGl());
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
                    observable.setAccountName(observable.getTxtAccHd());
//                 setAccountName2(observable.getLinkBatchIdForGl());
                } else {
                    //System.out.println("observable.getTxtAccHd()###@@@>>>" + observable.getTxtAccHd());
                    observable.setAccountName(observable.getTxtAccHd());

                }
                //System.out.println("observable.getLblAccName()??>>>" + observable.getLblAccName());
                lblAccHdDesc.setText(observable.getLblAccName());
            }
            if (pType.equals("GL") && txtAccNo.getText().length() > 0) {
                lblAccName.setText(prevActNum);
            }
        } else {
            lblAccName.setText(observable.getLblAccName());
            
            lblAccHdDesc.setText(observable.getLblAccName());
        }
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
            if (observable.getLinkBatchIdForGl() != null && !observable.getLinkBatchIdForGl().equals("")) {
                setAccountName2(observable.getLinkBatchIdForGl());
            }
        }


        //To set the  Transaction ID, Transaction Date and Initator ID...
        lblTransactionIDDesc.setText(observable.getLblTransactionId());
        lblTransactionDateDesc.setText(observable.getLblTransDate());
        lblInitiatorIDDesc.setText(observable.getLblInitiatorId());
        lblAuthBydesc.setText(observable.getAuthorizeBy());
        //To set the Status...
        lblStatus.setText(observable.getLblStatus());

        addRadioButtons();
        txtPanNo.setText(observable.getTxtPanNo());
        populateInstrumentType();
        lblServiceTaxval.setText(observable.getLblServiceTaxval());
    }

    public String setAccountName(String AccountNo) {
        final HashMap accountNameMap = new HashMap();
        HashMap resultMap = new HashMap();
        String prodType = (String) ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected();
        String pID = !prodType.equals("GL") ? ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString() : "";
        try {
            accountNameMap.put("ACC_NUM", AccountNo);
            final List resultList = ClientUtil.executeQuery("getAccountNumberName" + prodType, accountNameMap);
            if (resultList != null) {
                if (resultList.size() > 0) {
                    if (!prodType.equals("GL")) {
                        HashMap dataMap = new HashMap();
                        accountNameMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
                        List lst = (List) ClientUtil.executeQuery("getProdIdForActNo" + prodType, accountNameMap);
                        if (lst != null) {
                            if (lst.size() > 0) {
                                dataMap = (HashMap) lst.get(0);
                                if (dataMap.get("PROD_ID").equals(pID)) {
                                    resultMap = (HashMap) resultList.get(0);
                                    if(resultMap.containsKey("CUST_ID") && resultMap.get("CUST_ID")!= null){                                        
                                        List panList = ClientUtil.executeQuery("customer.getCustomerPANNumber",resultMap);
                                        if(panList != null && panList.size() > 0){
                                            HashMap panLstMap = (HashMap)panList.get(0);
                                            if(panLstMap.containsKey("PAN_NUMBER") && panLstMap.get("PAN_NUMBER") != null && !panLstMap.get("PAN_NUMBER").equals((""))){
                                                txtPanNo.setText(CommonUtil.convertObjToStr(panLstMap.get("PAN_NUMBER")));
                                                observable.setTxtPanNo(txtPanNo.getText());
                                            }
                                        }
                                        // Added by nithya   - KD-3464
                                        String retireDtAlertRequired = CommonUtil.convertObjToStr(TrueTransactMain.CBMSPARAMETERS.get("RETIRE_DT_ALERT"));
                                        //System.out.println("retireDtAlertRequired :: " + retireDtAlertRequired);
                                        if (retireDtAlertRequired.equals("Y")) {
                                          int retireDtAlertDays = CommonUtil.convertObjToInt(TrueTransactMain.CBMSPARAMETERS.get("RETIRE_DT_ALERT_DAYS"));
                                           List retireDtList = ClientUtil.executeQuery("getBorrowerShareDetails",resultMap);
                                           if(retireDtList != null && retireDtList.size() > 0){
                                               HashMap retireDtMap = (HashMap)retireDtList.get(0);
                                               if(retireDtMap.containsKey("RETIREMENT_DT") && retireDtMap.get("RETIREMENT_DT") != null){
                                                   Date retireDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(retireDtMap.get("RETIREMENT_DT")));
                                                   if(DateUtil.dateDiff(currDt, retireDt) <= retireDtAlertDays){
                                                       ClientUtil.showMessageWindow("Retirement Date is : " + DateUtil.getStringDate(retireDt));
                                                   }
                                               }
                                           }
                                          
                                        }
                                        // End
                                        
                                    }
                                }
                            }
                        }
                    } else {
                        resultMap = (HashMap) resultList.get(0);
                    }

                }
            }
        } catch (Exception e) {
        }
        if (resultMap.containsKey("CUSTOMER_NAME")) {
            if (prodType.equals("OA")) {
                custStatus = CommonUtil.convertObjToStr(resultMap.get("MINOR"));
                String actStatus = CommonUtil.convertObjToStr(resultMap.get("ACT_STATUS_ID"));
                if (custStatus.equals("Y")) {
                    if (rdoTransactionType_Debit.isSelected()) {
                        ClientUtil.displayAlert("MINOR ACCOUNT");
                    }
                }
                if (actStatus.equals("DORMANT")) {
                    ClientUtil.displayAlert("DORMANT ACCOUNT");
                }
            }
            return resultMap.get("CUSTOMER_NAME").toString();

        } else {
            return String.valueOf("");
        }
    }

    public void updateOBFields() {
        observable.setScreen(getScreen());
        observable.setModule(getModule());
        observable.setSelectedBranchID(getSelectedBranchID());
        observable.setTxtScreenName("Cash Transactions");
        observable.setCboProdType((String) cboProdType.getSelectedItem());
        observable.setCboProdId((String) cboProdId.getSelectedItem());
        observable.setTxtAccNo((String) txtAccNo.getText());
        //system.out.println("accNo1???@@@>>>>" + accNo1);
        observable.setAccNumGl(accNo1);
        observable.setTxtInitiatorChannel((String) txtInitiatorChannel.getText());
        observable.setRdoTransactionType_Debit(rdoTransactionType_Debit.isSelected());
        observable.setRdoTransactionType_Credit(rdoTransactionType_Credit.isSelected());
        observable.setTxtInputAmt(txtInputAmt.getText());
        observable.setCboInputCurrency((String) cboInputCurrency.getSelectedItem());
        observable.setCboInstrumentType((String) cboInstrumentType.getSelectedItem());
        observable.setTxtInstrumentNo1(txtInstrumentNo1.getText());
        observable.setTxtInstrumentNo2(txtInstrumentNo2.getText());
        observable.setTdtInstrumentDate(tdtInstrumentDate.getDateValue());
        observable.setTxtTokenNo(txtTokenNo.getText());
        observable.setTxtAmount(txtAmount.getText());
        observable.setTxtNarration(txtAreaParticular.getText());
        //To set the Value of Account HeadAccount Desc...
        observable.setTxtAccHd(txtAccHdId.getText());
        observable.setLblAccHdDesc(lblAccHdDesc.getText());
        observable.setTxtParticulars(txtParticulars.getText());
        //To set the  Name of the Account Holder...
        observable.setLblAccName(lblAccName.getText());
        //To set the  Transaction ID, Transaction Date and Initator ID...
        observable.setLblTransactionId(lblTransactionIDDesc.getText());
        observable.setLblTransDate(lblTransactionDateDesc.getText());
        observable.setLblInitiatorId(lblInitiatorIDDesc.getText());
        observable.setTxtPanNo(txtPanNo.getText());
        observable.setLblServiceTaxval(lblServiceTaxval.getText());
        observable.setServiceTax_Map(serviceTax_Map);
    }

    public void setHelpMessage() {
        cboProdId.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdId"));
        cboProdType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdType"));
        txtAccNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccNo"));
        txtInitiatorChannel.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInitiatorChannel"));
        rdoTransactionType_Debit.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoTransactionType_Debit"));
        txtInputAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInputAmt"));
        cboInputCurrency.setHelpMessage(lblMsg, objMandatoryRB.getString("cboInputCurrency"));
        cboInstrumentType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboInstrumentType"));
        txtInstrumentNo1.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInstrumentNo1"));
        txtInstrumentNo2.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInstrumentNo2"));
        tdtInstrumentDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtInstrumentDate"));
        txtTokenNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTokenNo"));
        txtAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAmount"));
        txtParticulars.setHelpMessage(lblMsg, objMandatoryRB.getString("txtParticulars"));
//        txtNarration.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNarration"));
    }

    // To fill the Data into the Combo Boxes...
    // it invokes the Combo Box model defined in OB class...
    private void initComponentData() {
        //        cboProdId.setModel(observable.getCbmProdId());
        cboProdType.setModel(observable.getCbmProdType());
        cboInputCurrency.setModel(observable.getCbmInputCurrency());
        cboInstrumentType.setModel(observable.getCbmInstrumentType());
    }

    //To set the length of the Text Fields and Numeric Validation...
    private void setMaxLenths() {
        txtInputAmt.setValidation(new CurrencyValidation(13, 2));
        txtAmount.setValidation(new CurrencyValidation(13, 2));
        txtInstrumentNo1.setAllowNumber(true);
        txtInstrumentNo1.setMaxLength(ClientConstants.INSTRUMENT_NO1);
        txtInstrumentNo2.setValidation(new NumericValidation(ClientConstants.INSTRUMENT_NO2, 0));
        txtTokenNo.setAllowNumber(true);
        txtTokenNo.setMaxLength(16);
        txtParticulars.setMaxLength(100);
        txtAccNo.setAllowAll(true);
        txtParticulars.setAllowAll(true);
    }

    private void setEditFieldsEnable(boolean yesno) {
        rdoTransactionType_Credit.setEnabled(yesno);
        rdoTransactionType_Debit.setEnabled(yesno);
        txtAccNo.setEnabled(yesno);
        btnAccNo.setEnabled(yesno);
        btnAccHdId.setEnabled(yesno);
        cboProdId.setEnabled(yesno);
        cboProdType.setEnabled(yesno);
    }
    // To set The Value of the Buttons Depending on the Value or Condition...

    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnSave.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        btnAuthorize.setEnabled(!btnSave.isEnabled());
        btnReject.setEnabled(!btnSave.isEnabled());
        btnException.setEnabled(!btnSave.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
        btnVer.setEnabled(!btnSave.isEnabled());
    }

    // To set The Value of the Buttons Depending on the Value or Condition...
    public void setButtonEnableDisable(boolean flag) {

        btnNew.setEnabled(flag);
        btnSave.setEnabled(flag);
        btnEdit.setEnabled(flag);
        btnDelete.setEnabled(!flag);
        btnAuthorize.setEnabled(flag);
        btnReject.setEnabled(flag);
        btnException.setEnabled(flag);
        mitNew.setEnabled(flag);
        mitEdit.setEnabled(flag);
        mitDelete.setEnabled(!flag);
        btnCancel.setEnabled(!flag);
        mitSave.setEnabled(!flag);
        mitCancel.setEnabled(!flag);
        btnView.setEnabled(flag);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoTransactionType = new com.see.truetransact.uicomponent.CButtonGroup();
        panCashTransaction = new com.see.truetransact.uicomponent.CPanel();
        panData = new com.see.truetransact.uicomponent.CPanel();
        lblProdId = new com.see.truetransact.uicomponent.CLabel();
        cboProdId = new com.see.truetransact.uicomponent.CComboBox();
        lblAccHd = new com.see.truetransact.uicomponent.CLabel();
        panAccHd = new com.see.truetransact.uicomponent.CPanel();
        txtAccHdId = new com.see.truetransact.uicomponent.CTextField();
        btnAccHdId = new com.see.truetransact.uicomponent.CButton();
        lblAccNo = new com.see.truetransact.uicomponent.CLabel();
        lblInitiatorChannel = new com.see.truetransact.uicomponent.CLabel();
        lblTransactionType = new com.see.truetransact.uicomponent.CLabel();
        panTransactionType = new com.see.truetransact.uicomponent.CPanel();
        rdoTransactionType_Debit = new com.see.truetransact.uicomponent.CRadioButton();
        rdoTransactionType_Credit = new com.see.truetransact.uicomponent.CRadioButton();
        lblInputAmt = new com.see.truetransact.uicomponent.CLabel();
        txtInputAmt = new com.see.truetransact.uicomponent.CTextField();
        lblInputCurrency = new com.see.truetransact.uicomponent.CLabel();
        cboInputCurrency = new com.see.truetransact.uicomponent.CComboBox();
        lblInstrumentType = new com.see.truetransact.uicomponent.CLabel();
        cboInstrumentType = new com.see.truetransact.uicomponent.CComboBox();
        lblInstrumentNo = new com.see.truetransact.uicomponent.CLabel();
        panInstrumentNo = new com.see.truetransact.uicomponent.CPanel();
        txtInstrumentNo1 = new com.see.truetransact.uicomponent.CTextField();
        txtInstrumentNo2 = new com.see.truetransact.uicomponent.CTextField();
        lblInstrumentDate = new com.see.truetransact.uicomponent.CLabel();
        tdtInstrumentDate = new com.see.truetransact.uicomponent.CDateField();
        lblTokenNo = new com.see.truetransact.uicomponent.CLabel();
        txtTokenNo = new com.see.truetransact.uicomponent.CTextField();
        lblAmount = new com.see.truetransact.uicomponent.CLabel();
        lblParticulars = new com.see.truetransact.uicomponent.CLabel();
        txtParticulars = new com.see.truetransact.uicomponent.CTextField();
        lblAccName = new com.see.truetransact.uicomponent.CLabel();
        panAcctNo = new com.see.truetransact.uicomponent.CPanel();
        txtAccNo = new com.see.truetransact.uicomponent.CTextField();
        btnAccNo = new com.see.truetransact.uicomponent.CButton();
        lblProdType = new com.see.truetransact.uicomponent.CLabel();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        lblAccHdDesc = new com.see.truetransact.uicomponent.CLabel();
        btnDenomination = new com.see.truetransact.uicomponent.CButton();
        panAmt = new com.see.truetransact.uicomponent.CPanel();
        txtAmount = new com.see.truetransact.uicomponent.CTextField();
        btnCurrencyInfo = new com.see.truetransact.uicomponent.CButton();
        lblSpace = new com.see.truetransact.uicomponent.CLabel();
        btnViewTermLoanDetails = new com.see.truetransact.uicomponent.CButton();
        btnOrgOrResp = new com.see.truetransact.uicomponent.CButton();
        lblPanNo = new com.see.truetransact.uicomponent.CLabel();
        panPanNo = new com.see.truetransact.uicomponent.CPanel();
        txtPanNo = new com.see.truetransact.uicomponent.CTextField();
        btnPanNo = new com.see.truetransact.uicomponent.CButton();
        lblSpace7 = new com.see.truetransact.uicomponent.CLabel();
        lblServiceTax = new com.see.truetransact.uicomponent.CLabel();
        lblServiceTaxval = new com.see.truetransact.uicomponent.CLabel();
        lblHouseName = new com.see.truetransact.uicomponent.CLabel();
        lblNarration = new com.see.truetransact.uicomponent.CLabel();
        panInstrumentNo1 = new com.see.truetransact.uicomponent.CPanel();
        lblAccNoGl = new com.see.truetransact.uicomponent.CLabel();
        txtInitiatorChannel = new com.see.truetransact.uicomponent.CTextField();
        srpTxtAreaParticulars = new com.see.truetransact.uicomponent.CScrollPane();
        txtAreaParticular = new com.see.truetransact.uicomponent.CTextArea();
        btnWaive = new com.see.truetransact.uicomponent.CButton();
        lblLoanStatus = new com.see.truetransact.uicomponent.CLabel();
        panLables = new com.see.truetransact.uicomponent.CPanel();
        panTransaction = new com.see.truetransact.uicomponent.CPanel();
        lblTransactionID = new com.see.truetransact.uicomponent.CLabel();
        lblTransactionIDDesc = new com.see.truetransact.uicomponent.CLabel();
        lblTransactionDate = new com.see.truetransact.uicomponent.CLabel();
        lblTransactionDateDesc = new com.see.truetransact.uicomponent.CLabel();
        lblInitiatorID = new com.see.truetransact.uicomponent.CLabel();
        lblInitiatorIDDesc = new com.see.truetransact.uicomponent.CLabel();
        lblAuthBy = new com.see.truetransact.uicomponent.CLabel();
        lblAuthBydesc = new com.see.truetransact.uicomponent.CLabel();
        panLableValues = new com.see.truetransact.uicomponent.CPanel();
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
        btnVer = new com.see.truetransact.uicomponent.CButton();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace16 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        lblSpace17 = new com.see.truetransact.uicomponent.CLabel();
        btnDebitDetails = new com.see.truetransact.uicomponent.CButton();
        lblSpace18 = new com.see.truetransact.uicomponent.CLabel();
        btnCust = new com.see.truetransact.uicomponent.CButton();
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
        setMinimumSize(new java.awt.Dimension(1000, 675));
        setPreferredSize(new java.awt.Dimension(1000, 675));
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
        });

        panCashTransaction.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panCashTransaction.setMinimumSize(new java.awt.Dimension(1000, 580));
        panCashTransaction.setPreferredSize(new java.awt.Dimension(1000, 580));
        panCashTransaction.setLayout(new java.awt.GridBagLayout());

        panData.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panData.setMinimumSize(new java.awt.Dimension(500, 600));
        panData.setPreferredSize(new java.awt.Dimension(500, 600));
        panData.setLayout(new java.awt.GridBagLayout());

        lblProdId.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblProdId, gridBagConstraints);

        cboProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdId.setPopupWidth(250);
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
        panData.add(cboProdId, gridBagConstraints);

        lblAccHd.setText("Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblAccHd, gridBagConstraints);

        panAccHd.setMinimumSize(new java.awt.Dimension(121, 21));
        panAccHd.setPreferredSize(new java.awt.Dimension(21, 200));
        panAccHd.setLayout(new java.awt.GridBagLayout());

        txtAccHdId.setAllowAll(true);
        txtAccHdId.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccHdId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAccHdIdActionPerformed(evt);
            }
        });
        txtAccHdId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccHdIdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccHd.add(txtAccHdId, gridBagConstraints);

        btnAccHdId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccHdId.setToolTipText("Account Head");
        btnAccHdId.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccHdId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccHdIdActionPerformed(evt);
            }
        });
        panAccHd.add(btnAccHdId, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(panAccHd, gridBagConstraints);

        lblAccNo.setText("Account No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblAccNo, gridBagConstraints);

        lblInitiatorChannel.setText("Initiator Channel Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblInitiatorChannel, gridBagConstraints);

        lblTransactionType.setText("Transaction Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblTransactionType, gridBagConstraints);

        panTransactionType.setMinimumSize(new java.awt.Dimension(160, 23));
        panTransactionType.setPreferredSize(new java.awt.Dimension(150, 23));
        panTransactionType.setLayout(new java.awt.GridBagLayout());

        rdoTransactionType.add(rdoTransactionType_Debit);
        rdoTransactionType_Debit.setText("Payment");
        rdoTransactionType_Debit.setMinimumSize(new java.awt.Dimension(85, 27));
        rdoTransactionType_Debit.setPreferredSize(new java.awt.Dimension(85, 27));
        rdoTransactionType_Debit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTransactionType_DebitActionPerformed(evt);
            }
        });
        panTransactionType.add(rdoTransactionType_Debit, new java.awt.GridBagConstraints());

        rdoTransactionType.add(rdoTransactionType_Credit);
        rdoTransactionType_Credit.setText("Receipt");
        rdoTransactionType_Credit.setMargin(new java.awt.Insets(2, 5, 2, 2));
        rdoTransactionType_Credit.setMaximumSize(new java.awt.Dimension(69, 27));
        rdoTransactionType_Credit.setMinimumSize(new java.awt.Dimension(85, 27));
        rdoTransactionType_Credit.setPreferredSize(new java.awt.Dimension(85, 27));
        rdoTransactionType_Credit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTransactionType_CreditActionPerformed(evt);
            }
        });
        panTransactionType.add(rdoTransactionType_Credit, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 59;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 3, 4, 4);
        panData.add(panTransactionType, gridBagConstraints);

        lblInputAmt.setText("Currency Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblInputAmt, gridBagConstraints);

        txtInputAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInputAmt.setPreferredSize(new java.awt.Dimension(21, 200));
        txtInputAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInputAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(txtInputAmt, gridBagConstraints);

        lblInputCurrency.setText("Currency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblInputCurrency, gridBagConstraints);

        cboInputCurrency.setMinimumSize(new java.awt.Dimension(100, 21));
        cboInputCurrency.setPreferredSize(new java.awt.Dimension(21, 200));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(cboInputCurrency, gridBagConstraints);

        lblInstrumentType.setText("Instrument Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblInstrumentType, gridBagConstraints);

        cboInstrumentType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboInstrumentType.setPreferredSize(new java.awt.Dimension(21, 200));
        cboInstrumentType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboInstrumentTypeActionPerformed(evt);
            }
        });
        cboInstrumentType.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboInstrumentTypeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(cboInstrumentType, gridBagConstraints);

        lblInstrumentNo.setText("Instrument No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblInstrumentNo, gridBagConstraints);

        panInstrumentNo.setMinimumSize(new java.awt.Dimension(150, 21));
        panInstrumentNo.setPreferredSize(new java.awt.Dimension(21, 200));
        panInstrumentNo.setLayout(new java.awt.GridBagLayout());

        txtInstrumentNo1.setMinimumSize(new java.awt.Dimension(50, 21));
        txtInstrumentNo1.setPreferredSize(new java.awt.Dimension(50, 21));
        panInstrumentNo.add(txtInstrumentNo1, new java.awt.GridBagConstraints());

        txtInstrumentNo2.setMinimumSize(new java.awt.Dimension(100, 21));
        panInstrumentNo.add(txtInstrumentNo2, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 4, 5);
        panData.add(panInstrumentNo, gridBagConstraints);

        lblInstrumentDate.setText("Instrument Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblInstrumentDate, gridBagConstraints);

        tdtInstrumentDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtInstrumentDate.setPreferredSize(new java.awt.Dimension(21, 200));
        tdtInstrumentDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtInstrumentDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(tdtInstrumentDate, gridBagConstraints);

        lblTokenNo.setText("Token No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblTokenNo, gridBagConstraints);

        txtTokenNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTokenNo.setPreferredSize(new java.awt.Dimension(21, 200));
        txtTokenNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTokenNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(txtTokenNo, gridBagConstraints);

        lblAmount.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblAmount, gridBagConstraints);

        lblParticulars.setText("Particulars");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblParticulars, gridBagConstraints);

        txtParticulars.setMinimumSize(new java.awt.Dimension(500, 21));
        txtParticulars.setPreferredSize(new java.awt.Dimension(500, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(txtParticulars, gridBagConstraints);

        lblAccName.setForeground(new java.awt.Color(0, 51, 204));
        lblAccName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblAccName.setMaximumSize(new java.awt.Dimension(1000, 21));
        lblAccName.setMinimumSize(new java.awt.Dimension(100, 21));
        lblAccName.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblAccName, gridBagConstraints);

        panAcctNo.setMaximumSize(new java.awt.Dimension(140, 21));
        panAcctNo.setMinimumSize(new java.awt.Dimension(140, 21));
        panAcctNo.setPreferredSize(new java.awt.Dimension(140, 21));
        panAcctNo.setLayout(new java.awt.GridBagLayout());

        txtAccNo.setMaximumSize(new java.awt.Dimension(120, 21));
        txtAccNo.setMinimumSize(new java.awt.Dimension(120, 21));
        txtAccNo.setNextFocusableComponent(cboInstrumentType);
        txtAccNo.setPreferredSize(new java.awt.Dimension(120, 21));
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
        panAcctNo.add(txtAccNo, new java.awt.GridBagConstraints());

        btnAccNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccNo.setToolTipText("Account No.");
        btnAccNo.setMaximumSize(new java.awt.Dimension(21, 25));
        btnAccNo.setMinimumSize(new java.awt.Dimension(21, 25));
        btnAccNo.setPreferredSize(new java.awt.Dimension(21, 25));
        btnAccNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccNoActionPerformed(evt);
            }
        });
        panAcctNo.add(btnAccNo, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(panAcctNo, gridBagConstraints);

        lblProdType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblProdType, gridBagConstraints);

        cboProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdType.setPopupWidth(150);
        cboProdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(cboProdType, gridBagConstraints);

        lblAccHdDesc.setForeground(new java.awt.Color(0, 51, 204));
        lblAccHdDesc.setMinimumSize(new java.awt.Dimension(0, 21));
        lblAccHdDesc.setPreferredSize(new java.awt.Dimension(0, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblAccHdDesc, gridBagConstraints);

        btnDenomination.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/denomination.gif"))); // NOI18N
        btnDenomination.setText("Denomination");
        btnDenomination.setMargin(new java.awt.Insets(2, 4, 2, 4));
        btnDenomination.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDenominationActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panData.add(btnDenomination, gridBagConstraints);

        panAmt.setMaximumSize(new java.awt.Dimension(183, 29));
        panAmt.setMinimumSize(new java.awt.Dimension(183, 29));
        panAmt.setPreferredSize(new java.awt.Dimension(41, 230));
        panAmt.setLayout(new java.awt.GridBagLayout());

        txtAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAmountActionPerformed(evt);
            }
        });
        txtAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtAmountFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAmountFocusLost(evt);
            }
        });
        txtAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAmountKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAmountKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAmountKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAmt.add(txtAmount, gridBagConstraints);

        btnCurrencyInfo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCurrencyInfo.setToolTipText("Save");
        btnCurrencyInfo.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnCurrencyInfo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCurrencyInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCurrencyInfoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAmt.add(btnCurrencyInfo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAmt.add(lblSpace, gridBagConstraints);

        btnViewTermLoanDetails.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnViewTermLoanDetails.setText("View");
        btnViewTermLoanDetails.setMaximumSize(new java.awt.Dimension(72, 27));
        btnViewTermLoanDetails.setMinimumSize(new java.awt.Dimension(82, 27));
        btnViewTermLoanDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewTermLoanDetailsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAmt.add(btnViewTermLoanDetails, gridBagConstraints);

        btnOrgOrResp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnOrgOrResp.setText("v");
        btnOrgOrResp.setMaximumSize(new java.awt.Dimension(82, 27));
        btnOrgOrResp.setMinimumSize(new java.awt.Dimension(82, 27));
        btnOrgOrResp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOrgOrRespActionPerformed(evt);
            }
        });
        panAmt.add(btnOrgOrResp, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(panAmt, gridBagConstraints);

        lblPanNo.setText("PAN No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblPanNo, gridBagConstraints);

        panPanNo.setMaximumSize(new java.awt.Dimension(123, 29));
        panPanNo.setMinimumSize(new java.awt.Dimension(123, 29));
        panPanNo.setPreferredSize(new java.awt.Dimension(200, 29));
        panPanNo.setLayout(new java.awt.GridBagLayout());

        txtPanNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPanNo.add(txtPanNo, gridBagConstraints);

        btnPanNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPanNo.setToolTipText("Save");
        btnPanNo.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnPanNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnPanNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPanNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPanNo.add(btnPanNo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panPanNo.add(lblSpace7, gridBagConstraints);

        lblServiceTax.setText("ServiceTax");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panPanNo.add(lblServiceTax, gridBagConstraints);

        lblServiceTaxval.setMaximumSize(new java.awt.Dimension(70, 18));
        lblServiceTaxval.setMinimumSize(new java.awt.Dimension(70, 18));
        lblServiceTaxval.setPreferredSize(new java.awt.Dimension(70, 18));
        lblServiceTaxval.setVerifyInputWhenFocusTarget(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 0);
        panPanNo.add(lblServiceTaxval, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(panPanNo, gridBagConstraints);

        lblHouseName.setForeground(new java.awt.Color(0, 51, 204));
        lblHouseName.setMaximumSize(new java.awt.Dimension(1000, 21));
        lblHouseName.setMinimumSize(new java.awt.Dimension(100, 21));
        lblHouseName.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblHouseName, gridBagConstraints);

        lblNarration.setText("Member Name/Narration");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblNarration, gridBagConstraints);

        panInstrumentNo1.setPreferredSize(new java.awt.Dimension(21, 200));
        panInstrumentNo1.setLayout(new java.awt.GridBagLayout());

        lblAccNoGl.setForeground(new java.awt.Color(51, 102, 255));
        lblAccNoGl.setText(accNo1);
        lblAccNoGl.setMaximumSize(new java.awt.Dimension(120, 21));
        lblAccNoGl.setMinimumSize(new java.awt.Dimension(120, 21));
        lblAccNoGl.setPreferredSize(new java.awt.Dimension(120, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panInstrumentNo1.add(lblAccNoGl, gridBagConstraints);

        txtInitiatorChannel.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInitiatorChannel.setPreferredSize(new java.awt.Dimension(21, 200));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstrumentNo1.add(txtInitiatorChannel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 4, 5);
        panData.add(panInstrumentNo1, gridBagConstraints);

        srpTxtAreaParticulars.setMinimumSize(new java.awt.Dimension(335, 45));
        srpTxtAreaParticulars.setPreferredSize(new java.awt.Dimension(335, 45));

        txtAreaParticular.setBorder(javax.swing.BorderFactory.createBevelBorder(1));
        txtAreaParticular.setLineWrap(true);
        txtAreaParticular.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        txtAreaParticular.setMinimumSize(new java.awt.Dimension(20, 100));
        txtAreaParticular.setPreferredSize(new java.awt.Dimension(20, 100));
        srpTxtAreaParticulars.setViewportView(txtAreaParticular);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panData.add(srpTxtAreaParticulars, gridBagConstraints);

        btnWaive.setText("Waive");
        btnWaive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWaiveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        panData.add(btnWaive, gridBagConstraints);

        lblLoanStatus.setForeground(new java.awt.Color(255, 0, 0));
        lblLoanStatus.setText("cLabel1");
        lblLoanStatus.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblLoanStatus.setMaximumSize(new java.awt.Dimension(100, 21));
        lblLoanStatus.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(0, 138, 0, 0);
        panData.add(lblLoanStatus, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panCashTransaction.add(panData, gridBagConstraints);

        panLables.setMaximumSize(new java.awt.Dimension(250, 410));
        panLables.setMinimumSize(new java.awt.Dimension(250, 410));
        panLables.setPreferredSize(new java.awt.Dimension(250, 410));
        panLables.setLayout(new java.awt.GridBagLayout());

        panTransaction.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panTransaction.setMinimumSize(new java.awt.Dimension(200, 100));
        panTransaction.setPreferredSize(new java.awt.Dimension(200, 100));
        panTransaction.setLayout(new java.awt.GridBagLayout());

        lblTransactionID.setText("Transaction ID");
        lblTransactionID.setMinimumSize(new java.awt.Dimension(62, 18));
        lblTransactionID.setPreferredSize(new java.awt.Dimension(62, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 22;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panTransaction.add(lblTransactionID, gridBagConstraints);

        lblTransactionIDDesc.setMaximumSize(new java.awt.Dimension(100, 18));
        lblTransactionIDDesc.setMinimumSize(new java.awt.Dimension(100, 18));
        lblTransactionIDDesc.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        panTransaction.add(lblTransactionIDDesc, gridBagConstraints);

        lblTransactionDate.setText("Transaction Date");
        lblTransactionDate.setMaximumSize(new java.awt.Dimension(80, 18));
        lblTransactionDate.setMinimumSize(new java.awt.Dimension(80, 18));
        lblTransactionDate.setPreferredSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        panTransaction.add(lblTransactionDate, gridBagConstraints);

        lblTransactionDateDesc.setMaximumSize(new java.awt.Dimension(100, 18));
        lblTransactionDateDesc.setMinimumSize(new java.awt.Dimension(100, 18));
        lblTransactionDateDesc.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 8);
        panTransaction.add(lblTransactionDateDesc, gridBagConstraints);

        lblInitiatorID.setText("Initiator ID");
        lblInitiatorID.setMaximumSize(new java.awt.Dimension(15, 18));
        lblInitiatorID.setMinimumSize(new java.awt.Dimension(15, 18));
        lblInitiatorID.setPreferredSize(new java.awt.Dimension(15, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 65;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 3, 0);
        panTransaction.add(lblInitiatorID, gridBagConstraints);

        lblInitiatorIDDesc.setMaximumSize(new java.awt.Dimension(100, 18));
        lblInitiatorIDDesc.setMinimumSize(new java.awt.Dimension(100, 18));
        lblInitiatorIDDesc.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 3, 0);
        panTransaction.add(lblInitiatorIDDesc, gridBagConstraints);

        lblAuthBy.setText("Authorized By");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 3, 0);
        panTransaction.add(lblAuthBy, gridBagConstraints);

        lblAuthBydesc.setMaximumSize(new java.awt.Dimension(100, 18));
        lblAuthBydesc.setMinimumSize(new java.awt.Dimension(100, 18));
        lblAuthBydesc.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 3, 8);
        panTransaction.add(lblAuthBydesc, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 277;
        gridBagConstraints.ipady = -45;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panLables.add(panTransaction, gridBagConstraints);

        panLableValues.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panLableValues.setMaximumSize(new java.awt.Dimension(180, 285));
        panLableValues.setMinimumSize(new java.awt.Dimension(180, 265));
        panLableValues.setPreferredSize(new java.awt.Dimension(180, 265));
        panLableValues.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 297;
        gridBagConstraints.ipady = 255;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 0);
        panLables.add(panLableValues, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panCashTransaction.add(panLables, gridBagConstraints);

        getContentPane().add(panCashTransaction, java.awt.BorderLayout.CENTER);

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
        tbrHead.add(btnView);

        lblSpace5.setText("     ");
        tbrHead.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.setFocusable(false);
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
        btnEdit.setFocusable(false);
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
        btnDelete.setFocusable(false);
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
        btnSave.setFocusable(false);
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
        btnCancel.setFocusable(false);
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
        btnException.setFocusable(false);
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

        btnVer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_VER.gif"))); // NOI18N
        btnVer.setToolTipText("Second Authorization");
        btnVer.setMaximumSize(new java.awt.Dimension(29, 27));
        btnVer.setMinimumSize(new java.awt.Dimension(29, 27));
        btnVer.setPreferredSize(new java.awt.Dimension(29, 27));
        btnVer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerActionPerformed(evt);
            }
        });
        tbrHead.add(btnVer);

        lblSpace6.setText("     ");
        tbrHead.add(lblSpace6);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
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

        lblSpace17.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace17.setText("     ");
        lblSpace17.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace17);

        btnDebitDetails.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/denomination.gif"))); // NOI18N
        btnDebitDetails.setToolTipText("Enquiry Of Debit transactions");
        btnDebitDetails.setEnabled(false);
        btnDebitDetails.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDebitDetails.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDebitDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDebitDetailsActionPerformed(evt);
            }
        });
        tbrHead.add(btnDebitDetails);
        btnDebitDetails.getAccessibleContext().setAccessibleDescription("Enquiry Of Debit Transactions");

        lblSpace18.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace18.setText("     ");
        lblSpace18.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace18);

        btnCust.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/membership.jpg"))); // NOI18N
        btnCust.setToolTipText("Customer");
        btnCust.setEnabled(false);
        btnCust.setFocusable(false);
        btnCust.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCust.setMinimumSize(new java.awt.Dimension(21, 21));
        btnCust.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCust.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCust.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustActionPerformed(evt);
            }
        });
        tbrHead.add(btnCust);

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

    private void btnViewTermLoanDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewTermLoanDetailsActionPerformed
        // TODO add your handling code here:
           if (observable.getProdType().equals("TL")) {
                    if (rdoTransactionType_Credit.isSelected() && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                        if (!observable.getInstalType().isEmpty()&&!observable.getInstalType().equals("")&&!observable.getInstalType().equals(null)&&observable.getInstalType().equals("EMI")) {
                             double totalEMIAmt = setEMIAmount();
                            //system.out.println("hvbhjvdbb555");
                            observable.setTxtAmount(String.valueOf(totalEMIAmt)); // Added by nithya on 30-04-2021 for KD-2801
                            txtAmount.setText(observable.getTxtAmount());
                            if(txtAmount.getText().length() > 0){ //Added by nithya
                               totalEMIAmt = setEMILoanWaiveAmount();
                            }
                            observable.setTxtAmount(String.valueOf(totalEMIAmt));
                            txtAmount.setText(observable.getTxtAmount());
                        }
                    }
             }
        String transType = "";
        boolean showDueTable = false;
        boolean penalDepositFlag = false;
        double totWaiveAmt = 0;
//        payment radio button
        boolean debit = rdoTransactionType_Debit.isSelected();
//        reciept radio button
        boolean credit = rdoTransactionType_Credit.isSelected();
        if (debit) {
            transType = CommonConstants.DEBIT;
        } else if (credit) {
            transType = CommonConstants.CREDIT;
        }
        String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
        prodType=observable.getProdType();
        double serviceTaxAmt=CommonUtil.convertObjToDouble(lblServiceTaxval.getText());
        boolean loanFlag=false;
        if ((prodType.equals("AD") || prodType.equals("TL")) ||(prodType.equals("GL")&&observable.getTransModType().equals("TL")) && credit) {
            showDueTable = true;
            loanFlag=true;
        } else {
            showDueTable = false;
            loanFlag=false;
        }
        if(prodType.equals("AD")){
            loanFlag=false;
        }
        double penalAmt = CommonUtil.convertObjToDouble(observable.getDepositPenalAmt()).doubleValue();
        if (penalAmt > 0) {
            penalDepositFlag = true;
        } else {
            penalDepositFlag = false;
        }
        termLoanDetails = new ArrayList();
        termLoansDetailsMap = new HashMap();
        termLoanDetails = transDetails.getTblDataArrayList();
        termLoansDetailsMap.put("DATA", termLoanDetails);
        termLoansDetailsMap.put("WAIVEDATA", returnWaiveMap);
        if(TrueTransactMain.SERVICE_TAX_REQ.equals("Y") && rdoTransactionType_Credit.isSelected() == true){
           if(serviceTaxAmt>0){
                 termLoansDetailsMap.put("SERVICE_TAX",serviceTaxAmt);
           }
        }
//        //added by sreekrishnan for interets Paid upto
//        if ((prodType.equals("AD") || prodType.equals("TL"))){
//            if(interestPaidUpto!=null && !interestPaidUpto.equals("")){
//                termLoansDetailsMap.put("INTEREST_PAID_UPTO", interestPaidUpto);  
//            }
//        } 
        //added by rishad 14/03/2014 for adding waiveamount with cash amount
        if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
            totWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("Total_WaiveAmt"));
        }
        double paidAmt = CommonUtil.convertObjToDouble(txtAmount.getText());
        if (totWaiveAmt > 0) {
            paidAmt += totWaiveAmt;
        }
        String transViewAmount = CommonUtil.convertObjToStr(paidAmt);
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            termLoansDetailsMap.put("ACT_NUM", txtAccNo.getText());
            new ViewLoansTransUI(termLoansDetailsMap, transViewAmount, transType, showDueTable, penalDepositFlag, linkBatchId, prodType).show();
        } else {
            boolean authFlag = true;
            if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y") && rdoTransactionType_Credit.isSelected() == true) {
                termLoansDetailsMap.put("AUTHORIZE_VIEW", "AUTHORIZE_VIEW");
                termLoansDetailsMap.put("SERVICE_TAX", serviceTaxAmt);
                if ((linkBatchId == null || linkBatchId.length() <= 0) && prodType.equals("AD")) {
                    linkBatchId = txtAccNo.getText();
                }
            }
            if (loanFlag) {
                new ViewLoansTransUI(termLoansDetailsMap, transViewAmount, transType, showDueTable, penalDepositFlag, linkBatchId, authFlag).show();
            } else {
                new ViewLoansTransUI(termLoansDetailsMap, transViewAmount, transType, showDueTable, penalDepositFlag, linkBatchId, prodType).show();
            }
        }
        
    }//GEN-LAST:event_btnViewTermLoanDetailsActionPerformed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        // TODO add your handling code here:
        btnVer.setVisible(btnAuthorize.isVisible());
    }//GEN-LAST:event_formInternalFrameOpened

    private void btnVerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
//        observable.setStatus();
//        lblStatus.setText(observable.getLblStatus());
        if (viewType != 10) {
            viewType = TELLER_ENTRY_DETIALS;
            popUp(TELLER_ENTRY_DETIALS);
            btnCheck();
            btnVer.setEnabled(true);
            btnDebitDetails.setEnabled(false);
        } else if (viewType == 10) {
            HashMap where = new HashMap();
            where.put("TRANS_ID", lblTransactionIDDesc.getText());
            where.put("USER", TrueTransactMain.USER_ID);
            where.put("TRANS_DT", currDt.clone());
            where.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
            ClientUtil.execute("updateTellerEntryDetails", where);
            viewType = -1;
            btnCancelActionPerformed(null);
        }
    }//GEN-LAST:event_btnVerActionPerformed

    private void btnPanNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPanNoActionPerformed

        popUp(PAN_NUM);
    }//GEN-LAST:event_btnPanNoActionPerformed

    private void btnDebitDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDebitDetailsActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
//        observable.setStatus();
//        lblStatus.setText(observable.getLblStatus());
        if (viewType != 8) {
            viewType = DEBIT_DETAILS;
            popUp(DEBIT_DETAILS);
            btnCheck();
        } else if (viewType == 8) {
            HashMap where = new HashMap();
            where.put("TRANS_ID", lblTransactionIDDesc.getText());
            where.put("PAYMENT_BY", TrueTransactMain.USER_ID);
            where.put("TRANS_DT", currDt.clone());
            where.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
            ClientUtil.execute("updateDebitDetails", where);
            viewType = -1;
            btnCancelActionPerformed(null);
        }
    }//GEN-LAST:event_btnDebitDetailsActionPerformed

    private void txtAccNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccNoFocusLost
        // TODO add your handling code here:
        noofInstallment2 = 0;
        txtAccNoActionPerformed();
        if (rdoTransactionType_Debit.isSelected()) {
            cboInstrumentType.setEnabled(true);
            txtInstrumentNo1.setEnabled(true);
            txtInstrumentNo2.setEnabled(true);
            tdtInstrumentDate.setEnabled(true);
            //Added by Anju 15/5/2014
            txtParticulars.setText("To Cash");
            cboInstrumentType.setSelectedIndex(1);
            instrumentTypeFocus();
        }
    }//GEN-LAST:event_txtAccNoFocusLost

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        popUp(VIEW);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed

    private void cboInstrumentTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboInstrumentTypeActionPerformed
        // TODO add your handling code here:
        instrumentTypeFocus();
    }//GEN-LAST:event_cboInstrumentTypeActionPerformed
    private HashMap waiveOffEditInterestAmt() {
        double totalWaiveamt = 0;
        double editWaiveOffTransAmt = 0;
        HashMap resultWaiveMap = new HashMap();
        ArrayList singleList = new ArrayList();
        if (editableWaiveUI != null) {
            ArrayList list = editableWaiveUI.getTableData();
            for (int i = 0; i < list.size(); i++) {
                singleList = (ArrayList) list.get(i);
                totalWaiveamt += CommonUtil.convertObjToDouble(singleList.get(2));
                resultWaiveMap.put(singleList.get(0), singleList.get(2));
            }
        }
        resultWaiveMap.put("Total_WaiveAmt", CommonUtil.convertObjToStr(totalWaiveamt));
        return resultWaiveMap;
    }

    private void txtAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmountFocusLost
        // TODO add your handling code here:
        //Added by chithra for mantis:10319: Weekly RD customisation for Ollukkara bank
        String prId = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
        double tAmt = CommonUtil.convertObjToDouble(txtAmount.getText());
        int freq1 = 0;
        double actualDelay = 0.0;
        if (rdoTransactionType_Credit.isSelected() == true && depBehavesLike != null && depBehavesLike.equals("RECURRING") && tAmt > 0) {
            List list = null;
            HashMap hmap = new HashMap();
            String instalPendng = "";
            String depNo = txtAccNo.getText();
            if (depNo.contains("_")) {
                depNo = depNo.substring(0, depNo.lastIndexOf("_"));
            }
            hmap.put("ACC_NUM", depNo);
            hmap.put("CURR_DT", currDt.clone());
            hmap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            HashMap dailyProdID = new HashMap();
            dailyProdID.put("PID", prId);
            List dailyFrequency = ClientUtil.executeQuery("getDailyDepositFrequency", dailyProdID);
            if (dailyFrequency != null && dailyFrequency.size() > 0) {
                HashMap dailyFreq = new HashMap();
                dailyFreq = (HashMap) dailyFrequency.get(0);
                String daily = CommonUtil.convertObjToStr(dailyFreq.get("DEPOSIT_FREQ"));
                freq1 = CommonUtil.convertObjToInt(daily);
                if (freq1 == 7) {
                    list = ClientUtil.executeQuery("getSelectInstalmentPendingForTransDetailsUiForWeek", hmap);
                }
            }

            if (list != null && list.size() > 0) {
                hmap = (HashMap) list.get(0);
                instalPendng = CommonUtil.convertObjToStr(hmap.get("PENDING"));
                actualDelay = (long) CommonUtil.convertObjToInt(instalPendng);
            }

        }
        double transAmt = 0;
        if (CommonUtil.convertObjToStr(txtAmount.getText()).length() > 0) {
            transAmt = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
        }
        if (CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue() > 0) {
            txtAmount.setToolTipText(CommonUtil.currencyToWord(CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue()));
        } else {
            txtAmount.setToolTipText("Zero");
        }

        String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
        if (prodType.equals("GL")) {
            btnPanNo.setEnabled(false);
            txtPanNo.setText("");
        }
       //Added by chithra for mantis: 10345: New Weekly Deposit Schemes For Pudukad SCB
        if (depBehavesLike != null && depBehavesLike.equals("DAILY") && transAmt > 0) {
            HashMap dailyProdID = new HashMap();
            dailyProdID.put("PID", prId);
            List dailyFrequency = ClientUtil.executeQuery("getDailyDepositFrequency", dailyProdID);
            if (dailyFrequency != null && dailyFrequency.size() > 0) {
                HashMap dailyFreq = new HashMap();
                dailyFreq = (HashMap) dailyFrequency.get(0);
                String daily = CommonUtil.convertObjToStr(dailyFreq.get("DEPOSIT_FREQ"));
                String weekly_spec = CommonUtil.convertObjToStr(dailyFreq.get("WEEKLY_SPEC"));
                String variableAmt =  CommonUtil.convertObjToStr(dailyFreq.get("INSTALL_RECURRING_DEPAC"));
                int freq = CommonUtil.convertObjToInt(daily);
                if (freq == 7 && weekly_spec.equals("Y")) {
                    dailyProdID.put("PROD_ID", prId);
                    List maxAmtList = ClientUtil.executeQuery("getDepProdDetails", dailyProdID);
                    if (maxAmtList != null && maxAmtList.size() > 0) {
                        HashMap maxAmtMap = new HashMap();
                        maxAmtMap = (HashMap) maxAmtList.get(0);
                        if (maxAmtMap != null && maxAmtMap.containsKey("MAX_DEPOSIT_AMT")) {
                            double maxAmt = CommonUtil.convertObjToDouble(maxAmtMap.get("MAX_DEPOSIT_AMT"));
                         if(variableAmt!=null && variableAmt.equalsIgnoreCase("V")){
                            if (maxAmt < transAmt) {
                                ClientUtil.displayAlert("Entered Amount Exceeds The Maximum Amount Limit");
                                txtAmount.setText("");
                                txtAmount.grabFocus();
                                return;
                            }
                         }else if(variableAmt!=null && variableAmt.equalsIgnoreCase("F")){
                              txtAmount.setText(CommonUtil.convertObjToStr(maxAmtMap.get("MAX_DEPOSIT_AMT")));
                         }
                        }
                    }

                }
            }
        }
        /*
         * added by nikhil for the view TermLoan details operation
         */
        if ((prodType.equals("TL") || prodType.equals("AD")) && txtAmount.getText().length() > 0) {
            btnViewTermLoanDetails.setVisible(true);
            btnViewTermLoanDetails.setEnabled(true);
            //added by rishad 19/july/2019 for rebateinterest recalculation 
            if (transDetails.getTermLoanCloseCharge().containsKey("REBATE_CALCULATION") && transDetails.getTermLoanCloseCharge().get("REBATE_CALCULATION").equals("Monthly calculation")) {
                double enteredAmount = CommonUtil.convertObjToDouble(txtAmount.getText());
                double rebateInterest = CommonUtil.convertObjToDouble(transDetails.getTermLoanCloseCharge().get("REBATE_INTEREST"));
                double rebatePer = CommonUtil.convertObjToDouble(transDetails.getTermLoanCloseCharge().get("REBATE_PERCENT"));
                String spl_Rebat =  CommonUtil.convertObjToStr(transDetails.getTermLoanCloseCharge().get("SPL_REBATE")); // Added by nithya on 10-01-2020 for KD-1234
                double interest = 0;
                if (transDetails.getTermLoanCloseCharge() != null && transDetails.getTermLoanCloseCharge().containsKey("CURR_MONTH_INT") && !spl_Rebat.equals("Y")) {
                    interest = CommonUtil.convertObjToDouble(transDetails.getTermLoanCloseCharge().get("CURR_MONTH_INT"));
                    if (interest > 0 && rebateInterest > 0) {
                        if (interest > enteredAmount) {
                            double rAmt = enteredAmount *rebatePer/100;
                            rAmt = (double) getNearest((long) (rAmt * 100), 100) / 100;
                            transDetails.modifyTransDataBasedOnTransAmt("Available Rebate Interest", rAmt);
                        } else {
                            double rAmt = interest * rebatePer/100;
                            rAmt = (double) getNearest((long) (rAmt * 100), 100) / 100;
                            transDetails.modifyTransDataBasedOnTransAmt("Available Rebate Interest", rAmt);
                        }
                    }
                }else if (transDetails.getTermLoanCloseCharge() != null && transDetails.getTermLoanCloseCharge().containsKey("CURR_MONTH_INT") && spl_Rebat.equals("Y")) { // Added by nithya on 10-01-2020 for KD-1234
                    double loanIntPercent = 1.0;                    
                    if(transDetails.getTermLoanCloseCharge().containsKey("LOAN_INT_PERCENT") && transDetails.getTermLoanCloseCharge().get("LOAN_INT_PERCENT") != null){
                        loanIntPercent = CommonUtil.convertObjToDouble(transDetails.getTermLoanCloseCharge().get("LOAN_INT_PERCENT"));
                    }
                    interest = CommonUtil.convertObjToDouble(transDetails.getTermLoanCloseCharge().get("CURR_MONTH_INT"));
                    if (interest > 0 && rebateInterest > 0) {
                        if (interest > enteredAmount) {
                            double rAmt = (enteredAmount * loanIntPercent)/rebatePer;                           
                            rAmt = (double) getNearest((long) (rAmt * 100), 100) / 100;
                            transDetails.modifyTransDataBasedOnTransAmt("Available Rebate Interest", rAmt);
                        } else {
                            double rAmt = (interest * loanIntPercent) / rebatePer;
                            rAmt = (double) getNearest((long) (rAmt * 100), 100) / 100;
                            transDetails.modifyTransDataBasedOnTransAmt("Available Rebate Interest", rAmt);
                        }
                    }
                }
            }
//            }
            //end
            //btnWaive.setEnabled(true);
            if (!(observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT)) {
                if (asAndWhenMap != null && asAndWhenMap.size() > 0) {
                    double penalInt = CommonUtil.convertObjToDouble(asAndWhenMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                    if (asAndWhenMap != null && asAndWhenMap.containsKey("PENAL_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("PENAL_WAIVER")).equals("Y") ) {
//                        int result = ClientUtil.confirmationAlert("Do you Want to Waive Penal Interest");
//                        if (result == 0) {
//                            observable.setPenalWaiveOff(true);
//                        } else {
//                            observable.setPenalWaiveOff(false);
//                        }
                        //added by rishad for tooking waive amount from editwaiveUI 14/03/2014
                        if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                            double penalWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("PENAL"));
                            if (penalWaiveAmt > 0) {
                                observable.setPenalWaiveOff(true);
                                observable.setPenalWaiveAmount(penalWaiveAmt);
                            } else {
                                observable.setPenalWaiveOff(false);
                            }
                        }
                    } else {
                        double totalDue = transDetails.calculatetotalRecivableAmountFromAccountClosing();
                        if (asAndWhenMap != null && asAndWhenMap.containsKey("INTEREST") && CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue() > 0
                                && CommonUtil.convertObjToDouble(asAndWhenMap.get("REBATE_INTEREST")).doubleValue() > 0 && transAmt >= (totalDue - CommonUtil.convertObjToDouble(asAndWhenMap.get("REBATE_INTEREST")).doubleValue())) {
                            int result = ClientUtil.confirmationAlert("Do you Want to  give Rebate Interest");
                            if (result == 0) {
                                observable.setRebateInterest(true);
                            } else {
                                observable.setRebateInterest(false);
                            }
                        }
                    }
                    //added by rishad 14/03/2014 
                    if (asAndWhenMap != null && asAndWhenMap.containsKey("INTEREST_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("INTEREST_WAIVER")).equals("Y")) {
                        if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                            double interestWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("INTEREST"));
                            if (interestWaiveAmt > 0) {
                                observable.setInterestWaiveoff(true);
                                observable.setInterestWaiveAmount(interestWaiveAmt);
                            } else {
                                observable.setInterestWaiveoff(false);
                                observable.setInterestWaiveAmount(interestWaiveAmt);
                            }
                        }
                    }
                    if (asAndWhenMap != null && asAndWhenMap.containsKey("NOTICE_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("NOTICE_WAIVER")).equals("Y")) {
                        if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                            double noticeWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("NOTICE CHARGES"));
                            if (noticeWaiveAmt > 0) {
                                observable.setNoticeWaiveoff(true);
                                observable.setNoticeWaiveAmount(noticeWaiveAmt);
                            } else {
                                observable.setNoticeWaiveoff(false);
                            }
                        }
                    }
                    if (asAndWhenMap != null && asAndWhenMap.containsKey("PRINCIPAL_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("PRINCIPAL_WAIVER")).equals("Y")) {
                        if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                            double principalWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("PRINCIPAL"));
                            if (principalWaiveAmt > 0) {
                                observable.setPrincipalwaiveoff(true);
                                observable.setPrincipalWaiveAmount(principalWaiveAmt);
                            } else {
                                observable.setPrincipalwaiveoff(false);
                            }
                        }
                    }
                    if (asAndWhenMap != null && asAndWhenMap.containsKey("ARC_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("ARC_WAIVER")).equals("Y")) {
                        if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                            double arcWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("ARC_COST"));
                            if (arcWaiveAmt > 0) {
                                observable.setArcWaiveOff(true);
                                observable.setArcWaiveAmount(arcWaiveAmt);
                            } else {
                                observable.setArcWaiveOff(false);
                            }
                        }
                    }

                    if (asAndWhenMap != null && asAndWhenMap.containsKey("ARBITRARY_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("ARBITRARY_WAIVER")).equals("Y")) {
                        if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                            double arbitaryWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("ARBITRARY CHARGES"));
                            if (arbitaryWaiveAmt > 0) {
                                observable.setArbitraryWaiveOff(true);
                                observable.setArbitarayWaivwAmount(arbitaryWaiveAmt);
                            } else {
                                observable.setArbitraryWaiveOff(false);
                            }
                        }
                    }
                    
                    if (asAndWhenMap != null && asAndWhenMap.containsKey("RECOVERY_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("RECOVERY_WAIVER")).equals("Y")) {
                        if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                            double recoveryWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("RECOVERY CHARGES"));
                            if (recoveryWaiveAmt > 0) {
                                observable.setRecoveryWaiveOff(true);
                                observable.setRecoveryWaiveAmount(recoveryWaiveAmt);
                            } else {
                                observable.setRecoveryWaiveOff(false);
                            }
                        }
                    }
                    
                    if (asAndWhenMap != null && asAndWhenMap.containsKey("MEASUREMENT_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("MEASUREMENT_WAIVER")).equals("Y")) {
                        if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                            double measurementWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("MEASUREMENT CHARGES"));
                            if (measurementWaiveAmt > 0) {
                                observable.setMeasurementWaiveOff(true);
                                observable.setMeasurementWaiveAmount(measurementWaiveAmt);
                            } else {
                                observable.setMeasurementWaiveOff(false);
                            }
                        }
                    }
                    
                    if (asAndWhenMap != null && asAndWhenMap.containsKey("POSTAGE_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("POSTAGE_WAIVER")).equals("Y")) {
                        if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                            double postageWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("POSTAGE CHARGES"));
                            if (postageWaiveAmt > 0) {
                                observable.setPostageWaiveOff(true);
                                observable.setPostageWaiveAmount(postageWaiveAmt);
                            } else {
                                observable.setPostageWaiveOff(false);
                            }
                        }
                    }
                    if (asAndWhenMap != null && asAndWhenMap.containsKey("LEGAL_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("LEGAL_WAIVER")).equals("Y")) {
                        if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                            double legalWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("LEGAL CHARGES"));
                            if (legalWaiveAmt > 0) {
                                observable.setLegalWaiveOff(true);
                                observable.setLegalWaiveAmount(legalWaiveAmt);
                            } else {
                                observable.setLegalWaiveOff(false);
                            }
                        }
                    }
                    if (asAndWhenMap != null && asAndWhenMap.containsKey("INSURANCE_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("INSURANCE_WAIVER")).equals("Y")) {
                        if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                            double insurenceWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("INSURANCE CHARGES"));
                            if (insurenceWaiveAmt > 0) {
                                observable.setInsuranceWaiveOff(true);
                                observable.setInsuranceWaiveAmont(insurenceWaiveAmt);
                            } else {
                                observable.setInsuranceWaiveOff(false);
                            }
                        }
                    }
                    if (asAndWhenMap != null && asAndWhenMap.containsKey("EP_COST_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("EP_COST_WAIVER")).equals("Y")) {
                        if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                            double epWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("EP_COST"));
                            if (epWaiveAmt > 0) {
                                observable.setEpCostWaiveOff(true);
                                observable.setEpCostWaiveAmount(epWaiveAmt);
                            } else {
                                observable.setEpCostWaiveOff(false);
                            }
                        }
                    }
                    if (asAndWhenMap != null && asAndWhenMap.containsKey("DECREE_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("DECREE_WAIVER")).equals("Y")) {
                        if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                            double degreeWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("EXECUTION DECREE CHARGES"));
                            if (degreeWaiveAmt > 0) {
                                observable.setDecreeWaiveOff(true);
                                observable.setDecreeWaiveAmount(degreeWaiveAmt);
                            } else {
                                observable.setDecreeWaiveOff(false);
                            }
                        }
                    }
                    if (asAndWhenMap != null && asAndWhenMap.containsKey("MISCELLANEOUS_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("MISCELLANEOUS_WAIVER")).equals("Y")) {
                        if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                            double miseWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("MISCELLANEOUS CHARGES"));
                            if (miseWaiveAmt > 0) {
                                observable.setMiscellaneousWaiveOff(true);
                                observable.setMiscellaneousWaiveAmount(miseWaiveAmt);
                            } else {
                                observable.setMiscellaneousWaiveOff(false);
                            }
                        }
                    }
                    if (asAndWhenMap != null && asAndWhenMap.containsKey("ADVERTISE_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("ADVERTISE_WAIVER")).equals("Y")) {
                        if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                            double advertiseWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("ADVERTISE CHARGES"));
                            if (advertiseWaiveAmt > 0) {
                                observable.setAdvertiseWaiveOff(true);
                                observable.setAdvertiseWaiveAmount(advertiseWaiveAmt);
                            } else {
                                observable.setAdvertiseWaiveOff(false);
                            }
                        }
                    }

                    // Added by nithya for 0008470 : overdue interest for EMI Loans
                    if (asAndWhenMap != null && asAndWhenMap.containsKey("OVERDUEINT_WAIVER") && asAndWhenMap.get("OVERDUEINT_WAIVER") != null && CommonUtil.convertObjToStr(asAndWhenMap.get("OVERDUEINT_WAIVER")).equals("Y")) {
                        if (returnWaiveMap != null && returnWaiveMap.size() > 0 && returnWaiveMap.containsKey("EMI_OVERDUE_CHARGE") && returnWaiveMap.get("EMI_OVERDUE_CHARGE") != null) {
                            double overDueIntWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("EMI_OVERDUE_CHARGE"));
                            if (overDueIntWaiveAmt > 0) {
                                observable.setOverDueIntWaiveOff(true);
                                observable.setOverDueIntWaiveAmount(overDueIntWaiveAmt);
                            } else {
                                observable.setOverDueIntWaiveOff(false);
                            }
                        }
                    }
                    // End

                    if (asAndWhenMap != null && asAndWhenMap.containsKey("KOLE_FIELD_EXPENSE_WAIVER") && asAndWhenMap.get("KOLE_FIELD_EXPENSE_WAIVER") != null && CommonUtil.convertObjToStr(asAndWhenMap.get("KOLE_FIELD_EXPENSE_WAIVER")).equals("Y")) {
                        if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                            double koleFieldExpenseWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("KOLEFIELD EXPENSE"));
                            if (koleFieldExpenseWaiveAmt > 0) {
                                observable.setKoleFieldExpenseWaiveOff(true);
                                observable.setKoleFieldExpenseWaiveAmount(koleFieldExpenseWaiveAmt);
                            } else {
                                observable.setKoleFieldExpenseWaiveOff(false);
                            }
                        }
                    }
                    
                    if (asAndWhenMap != null && asAndWhenMap.containsKey("KOLE_FIELD_OPERATION_WAIVER") && asAndWhenMap.get("KOLE_FIELD_OPERATION_WAIVER") != null && CommonUtil.convertObjToStr(asAndWhenMap.get("KOLE_FIELD_OPERATION_WAIVER")).equals("Y")) {
                        if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                            double koleFieldOperationWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("KOLEFIELD OPERATION"));
                            if (koleFieldOperationWaiveAmt > 0) {
                                observable.setKoleFieldOperationWaiveOff(true);
                                observable.setKoleFieldOperationWaiveAmount(koleFieldOperationWaiveAmt);
                            } else {
                                observable.setKoleFieldOperationWaiveOff(false);
                            }
                        }
                    }
                    

                }
            }
        } else {
//             btnViewTermLoanDetails.setVisible(false);
//             btnViewTermLoanDetails.setEnabled(false);
        }
       if (rdoTransactionType_Credit.isSelected() == true) {
            String prodId = "";
            if (!((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString().equals("GL")) {
                prodId = ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString();
            }
            double penalAmt = CommonUtil.convertObjToDouble(transDetails.getPenalAmount()).doubleValue();
            //system.out.println("#####ProdId :" + prodId + "penalAmt :" + penalAmt);
            HashMap prodMap = new HashMap();
            prodMap.put("PROD_ID", prodId.toString());
            List lst = ClientUtil.executeQuery("getBehavesLikeForDeposit", prodMap);
            //system.out.println("######## BHEAVES :" + lst);
            if (lst != null && lst.size() > 0) {
                prodMap = (HashMap) lst.get(0);
                if (prodMap.get("BEHAVES_LIKE").equals("RECURRING")) {
                    HashMap recurringMap = new HashMap();
                    double amount = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                    String depNo = txtAccNo.getText();
                    //system.out.println("########Amount : " + amount + "####DepNo :" + depNo);
                    if (depNo.contains("_")) {
                    depNo = depNo.substring(0, depNo.lastIndexOf("_"));
                    }
                    //system.out.println("######## BHEAVES :" + depNo);
                    recurringMap.put("DEPOSIT_NO", depNo);
                    lst = ClientUtil.executeQuery("getDepositAmountForRecurring", recurringMap);
                    if (lst != null && lst.size() > 0) {
                        recurringMap = (HashMap) lst.get(0);
                        double depAmount = CommonUtil.convertObjToDouble(recurringMap.get("DEPOSIT_AMT")).doubleValue();
                        double finalAmount = 0.0;
                        //double receivablAmt = 0.0;
                        //Commented by nithya on 12-07-2019 for KD 551 - Need to allow future installment for weekly RD
//                        if (freq1 == 7) {
//                            if (tAmt > ((actualDelay + 1) * depAmount)) {
//                                ClientUtil.showMessageWindow("No Future Receipts Allowed");
//                                txtAmount.setText("");
//                                return;
//                            }
//                        }
                        if (penalAmt > 0) {
                            String[] obj = {"Penalty with Installments", "Installments Only."};
                            int option = COptionPane.showOptionDialog(null, ("Select The Desired Option"), ("Receiving Penal with Installments..."),
                                    COptionPane.YES_NO_CANCEL_OPTION, COptionPane.QUESTION_MESSAGE, null, obj, obj[0]);
                            //System.out.println("option===" + option);
                            if (option == 0) {
                               // System.out.println("##### amount : " + amount+ "##### depAmount : " + depAmount+"##### penalAmt : " + penalAmt);
                                if (amount > penalAmt) {
                                    amount = amount - penalAmt;
                                //    System.out.println("###### amount : " + amount+"###### depAmount : " + depAmount);
                                    finalAmount = amount % depAmount;
                                } else {
                                    finalAmount = amount % (penalAmt + depAmount);
                                }
                             //   System.out.println("###### finalAmount : " + finalAmount);
                                if (finalAmount != 0) { //Added By Suresh R  11-Jul-2017
                                    double serviceTax = 0.0;
                                    if (serviceTax_Map != null && serviceTax_Map.containsKey("TOT_TAX_AMT") && serviceTax_Map.get("TOT_TAX_AMT") != null) {
                                        serviceTax = CommonUtil.convertObjToDouble(serviceTax_Map.get("TOT_TAX_AMT")).doubleValue();
                                    }
                                    //ClientUtil.displayAlert("Minimum Amount Should Enter ..." + (depAmount + penalAmt));
                                    ClientUtil.displayAlert("Minimum Amount Should Enter ..." + ((depAmount * noofInstallment2) + penalAmt + serviceTax));
                                    txtAmount.setText("");
                                } else {
                                    observable.setDepositPenalAmt(String.valueOf(penalAmt));
                                    observable.setDepositPenalMonth(transDetails.getPenalMonth());
                                }
                                if (finalAmount >= 0) {
                                    //vivek
                                    // ClientUtil.displayAlert("Minimum Amount Should Enter ..." + (depAmount + penalAmt));
                                    //double receivablAmt = depAmount + penalAmt;
                                    double receivablAmt = depAmount;
                                //    System.out.println("### Penal Amount : " + penalAmt);
                                    double totAmt1 = 0.0;
                                 //   System.out.println("##### noofInstallment : " + noofInstallment2);
                                    totAmt1 = receivablAmt * noofInstallment2;
                                    if (penalAmt > 0) {
                                        totAmt1 = totAmt1 + penalAmt;
                                    }
                                    //
                                    if (totAmt1 > 0.0) {
                                        txtAmount.setText(CommonUtil.convertObjToStr(totAmt1));
                                        if (penalAmt > 0) {
                                            observable.setDepositPenalAmt(String.valueOf(penalAmt));
                                        }
                                    } else {
                                        receivablAmt = depAmount + penalAmt;
                                        txtAmount.setText(CommonUtil.convertObjToStr(receivablAmt));
                                        observable.setDepositPenalAmt(String.valueOf(penalAmt));
                                    }
                                    observable.setDepositPenalMonth(transDetails.getPenalMonth());

                                }
                                if (penalAmt == 0) {
                                    finalAmount = amount % depAmount;
                                    if (finalAmount != 0) {
//                                    ClientUtil.displayAlert("Enter Amount in Multiples of Deposit Amount...\n"
//                                            + "Deposit Amount is : " + depAmount);
//                                    txtAmount.setText("");
                                    }
                                    observable.setDepositPenalAmt(String.valueOf(0.0));
                                    observable.setDepositPenalMonth(String.valueOf(0.0));
                                }
                                calculateRDServiceTaxAmt(penalAmt);// Added by nithya on 28-11-2018 for KD 346 - Gst amount calculation issue at Rd Remittance at cash and Transfer
                            } else if (option == 1) {
                                finalAmount = amount % depAmount;
                                observable.setDepositPenalAmt(String.valueOf(0.0));
                                observable.setDepositPenalMonth(String.valueOf(0.0));
                                if (finalAmount >= 0) {
                                    //System.out.println("jhgjk>>>");
//                                ClientUtil.displayAlert("Enter Amount in Multiples of Deposit Amount...\n"
//                                        + "Deposit Amount is : " + depAmount);
                                    double d1 = depAmount * noofInstallment2;
                                    //System.out.println("d1@@###>>>" + d1);
                                    txtAmount.setText(CommonUtil.convertObjToStr(d1));
                                }
                                calculateRDServiceTaxAmt(0.0);// Added by nithya on 28-11-2018 for KD 346 - Gst amount calculation issue at Rd Remittance at cash and Transfer
                            } else {
                                if (amount > penalAmt) {
                                    amount = amount - penalAmt;
                                    //System.out.println("amount====" + amount);
                                    //System.out.println("depAmount====" + depAmount);
                                    finalAmount = amount % depAmount;
                                } else {
                                    finalAmount = amount % (penalAmt + depAmount);
                                }
                                if (finalAmount >= 0) {
                                    //vivek
                                    // ClientUtil.displayAlert(" Minimum Amount Should Enter ..." + (depAmount + penalAmt));
                                    //double receivablAmt = depAmount + penalAmt;
                                    double receivablAmt = depAmount;
                                    //System.out.println("penalAmt????222@@>>>>" + penalAmt);
                                    double totAmt1 = 0.0;
                                    //System.out.println("#### noofInstallment : " + noofInstallment2);
                                    totAmt1 = receivablAmt * noofInstallment2;
                                    if (penal > 0) {
                                        totAmt1 = totAmt1 + penal;
                                    }
                                    //
                                    if (totAmt1 > 0.0) {
                                        txtAmount.setText(CommonUtil.convertObjToStr(totAmt1));
                                        if (penal > 0) {
                                            observable.setDepositPenalAmt(String.valueOf(penal));
                                        }
                                    } else {
                                        receivablAmt = depAmount + penalAmt;
                                        txtAmount.setText(CommonUtil.convertObjToStr(receivablAmt));
                                        observable.setDepositPenalAmt(String.valueOf(penalAmt));
                                    }
                                    observable.setDepositPenalMonth(transDetails.getPenalMonth());

                                } else {
                                    finalAmount = amount % depAmount;
                                    if (finalAmount != 0) {
                                        //System.out.println("jhgjk##111##>>>");
//                                    ClientUtil.displayAlert("Enter Amount in Multiples of Deposit Amount...\n"
//                                            + "Deposit Amount is : " + depAmount);
//                                    txtAmount.setText("");
                                    }
                                    observable.setDepositPenalAmt(String.valueOf(0.0));
                                    observable.setDepositPenalMonth(String.valueOf(0.0));
                                }
                                calculateRDServiceTaxAmt(0.0);// Added by nithya on 28-11-2018 for KD 346 - Gst amount calculation issue at Rd Remittance at cash and Transfer
                            }
                            //system.out.println("######## BHEAVES REMAINING :" + finalAmount);
                        }
                        recurringMap = null;
                    }
                    prodMap = null;
                    lst = null;
                }
                moreThanLoanAmountAlert();
                if (!prodType.equals("GL")) {
                    double amount = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                    if (amount >= TrueTransactMain.PANAMT) {
                        btnPanNo.setEnabled(true);
                        txtPanNo.setText(observable.getTxtPanNo());
                    } else if (amount < TrueTransactMain.PANAMT) {
                        btnPanNo.setEnabled(false);
                        txtPanNo.setText("");
                    }
                }
            }
            if (rdoTransactionType_Debit.isSelected() == true) {
                String prodId1 = "";
                if (!((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString().equals("GL")) {
                    prodId1 = ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString();
                }
                //system.out.println("#####ProdId :" + prodId);
                HashMap prodMap1 = new HashMap();
                prodMap.put("PROD_ID", prodId.toString());
                lst = ClientUtil.executeQuery("getBehavesLikeForDeposit", prodMap1);
                //system.out.println("######## BHEAVES :" + lst);
                if (lst.size() > 0) {
                    prodMap = (HashMap) lst.get(0);
                    if (prodMap.get("BEHAVES_LIKE").equals("FIXED")) {
                        HashMap recurringMap = new HashMap();
                        double amount = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                        String depNo = txtAccNo.getText();
                        //                    //system.out.println("########Amount : "+amount +"####DepNo :"+depNo);
                        if (depNo.contains("_")) {
                        depNo = depNo.substring(0, depNo.lastIndexOf("_"));
                        }
                        //                    //system.out.println("######## BHEAVES :"+depNo);
                        recurringMap.put("DEPOSIT_NO", depNo);
                        lst = ClientUtil.executeQuery("getInterestDeptIntTable", recurringMap);
                        if (lst.size() > 0) {
                            recurringMap = (HashMap) lst.get(0);
                            double depAmount = CommonUtil.convertObjToDouble(recurringMap.get("PERIODIC_INT_AMT")).doubleValue();
                            double finalAmount = amount % depAmount;
                            //system.out.println("######## BHEAVES REMAINING :" + finalAmount);
                            if (finalAmount == 0) {
                                txtAmount.setEnabled(false);
                                //system.out.println("######## BHEAVES :" + finalAmount);
                            } else {
                                //                            ClientUtil.displayAlert("Enter Amount in Multiples of Deposit Amount... ");
                                //                            txtAmount.setText("");
                            }
                        }
                        recurringMap = null;
                    } else if (prodMap.get("BEHAVES_LIKE").equals("DAILY") && prodMap.get("PARTIAL_WITHDRAWAL_ALLOWED").equals("Y")) {
                        HashMap dailyDepMap = new HashMap();
                        double amount = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                        String depNo = txtAccNo.getText();
                        //                    //system.out.println("########Amount : "+amount +"####DepNo :"+depNo);
                        if (depNo.contains("_")) {
                        depNo = depNo.substring(0, depNo.lastIndexOf("_"));
                        }
                        //                    //system.out.println("######## BHEAVES :"+depNo);
                        dailyDepMap.put("DEPOSIT_NO", depNo);
                        lst = ClientUtil.executeQuery("getDepAvailBalForPartialWithDrawal", dailyDepMap);
                        if (lst.size() > 0) {
                            dailyDepMap = (HashMap) lst.get(0);
                            double depAmount = CommonUtil.convertObjToDouble(dailyDepMap.get("AVAILABLE_BALANCE")).doubleValue();
                            if (depAmount < amount) {
                                //system.out.println("@#$@#$%$^$%^amount" + amount + " :depAmount: " + depAmount);
                                ClientUtil.displayAlert("Amount Greater than available balance!!");
                                txtAmount.setText("");
                                return;
                            }
                        }
                        dailyDepMap = null;

                    }
                    prodMap = null;
                    lst = null;
                }
            }
//            if (CommonUtil.convertObjToDouble(this.txtAmount.getText()).doubleValue() <= 0) {
//                ClientUtil.displayAlert("amount should not be zero or empty");
//                return;
//            }
        }
        // txtAccNoActionPerformed();
        if (rdoTransactionType_Debit.isSelected()) {
            if(observable.getProdType().equals("OA")){
                HashMap sbMap = new HashMap();
                String behaviour = "";
                String prodId = ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString();
                sbMap.put("PRODUCT_ID", prodId);
                List lst = ClientUtil.executeQuery("getOpAcctProductTOByProdId", sbMap);
                if (lst != null && lst.size() > 0) {
                    OperativeAcctProductTO map = (OperativeAcctProductTO) lst.get(0);
                    behaviour = map.getBehavior();
                }
                if(behaviour.equals("SB") && TrueTransactMain.CBMSPARAMETERS.containsKey("SB_CASH_PAYMENT_LIMIT") && TrueTransactMain.CBMSPARAMETERS.get("SB_CASH_PAYMENT_LIMIT") != null && CommonUtil.convertObjToDouble(TrueTransactMain.CBMSPARAMETERS.get("SB_CASH_PAYMENT_LIMIT")) > 0){
                    double sbPaymentLimit = CommonUtil.convertObjToDouble(TrueTransactMain.CBMSPARAMETERS.get("SB_CASH_PAYMENT_LIMIT"));
                    HashMap limitMap = new HashMap();
                    limitMap.put("ACT_NUM",txtAccNo.getText());
                    limitMap.put("TRANS_DT",currDt.clone());
                    List limitList = ClientUtil.executeQuery("getAccountTotalPayment", limitMap);
                    if(limitList != null && limitList.size() > 0){
                        limitMap = (HashMap)limitList.get(0);
                        if(limitMap.containsKey("TOTAL_PAYMENT") && limitMap.get("TOTAL_PAYMENT") != null){
                            double paymentAmt = CommonUtil.convertObjToDouble(txtAmount.getText()) + CommonUtil.convertObjToDouble(limitMap.get("TOTAL_PAYMENT"));
                            if(paymentAmt > sbPaymentLimit){
                                 ClientUtil.displayAlert("Payment Exceeds Limit !!");
                                 txtAmount.setText("");
                                 txtAmount.grabFocus();
                                 return;
                            }
                        }
                    }
                    //getAccountTotalPayment
                    
                }
            }
            cboInstrumentType.setEnabled(true);
            txtInstrumentNo1.setEnabled(true);
            txtInstrumentNo2.setEnabled(true);
            tdtInstrumentDate.setEnabled(true);
            // txtParticulars.setText("To Cash");
            //  cboInstrumentType.setSelectedIndex(1);
            instrumentTypeFocus();
        }
        
        
           
        //Credit & Debit limit checking   
        boolean limitSet = false;
        if (observable.getProdType().equals("AD") || observable.getProdType().equals("TL") || observable.getProdType().equals("GL") || observable.getProdType().equals("SA")) {
            if (rdoTransactionType_Debit.isSelected()) {
                double paymentLimit = 0.0;
                if (observable.getProdType().equals("AD") && TrueTransactMain.CBMSPARAMETERS.containsKey("OD_CASH_PAYMENT_LIMIT") && TrueTransactMain.CBMSPARAMETERS.get("OD_CASH_PAYMENT_LIMIT") != null && CommonUtil.convertObjToDouble(TrueTransactMain.CBMSPARAMETERS.get("OD_CASH_PAYMENT_LIMIT")) > 0) {
                    paymentLimit = CommonUtil.convertObjToDouble(TrueTransactMain.CBMSPARAMETERS.get("OD_CASH_PAYMENT_LIMIT"));
                    limitSet = true;
                } else if (observable.getProdType().equals("TL") && TrueTransactMain.CBMSPARAMETERS.containsKey("TL_CASH_PAYMENT_LIMIT") && TrueTransactMain.CBMSPARAMETERS.get("TL_CASH_PAYMENT_LIMIT") != null && CommonUtil.convertObjToDouble(TrueTransactMain.CBMSPARAMETERS.get("TL_CASH_PAYMENT_LIMIT")) > 0) {
                    paymentLimit = CommonUtil.convertObjToDouble(TrueTransactMain.CBMSPARAMETERS.get("TL_CASH_PAYMENT_LIMIT"));
                    limitSet = true;
                } else if (observable.getProdType().equals("GL") && TrueTransactMain.CBMSPARAMETERS.containsKey("GL_CASH_PAYMENT_LIMIT") && TrueTransactMain.CBMSPARAMETERS.get("GL_CASH_PAYMENT_LIMIT") != null && CommonUtil.convertObjToDouble(TrueTransactMain.CBMSPARAMETERS.get("GL_CASH_PAYMENT_LIMIT")) > 0) {
                    paymentLimit = CommonUtil.convertObjToDouble(TrueTransactMain.CBMSPARAMETERS.get("GL_CASH_PAYMENT_LIMIT"));
                    limitSet = true;
                } else if (observable.getProdType().equals("SA") && TrueTransactMain.CBMSPARAMETERS.containsKey("SA_CASH_PAYMENT_LIMIT") && TrueTransactMain.CBMSPARAMETERS.get("SA_CASH_PAYMENT_LIMIT") != null && CommonUtil.convertObjToDouble(TrueTransactMain.CBMSPARAMETERS.get("SA_CASH_PAYMENT_LIMIT")) > 0) {
                    paymentLimit = CommonUtil.convertObjToDouble(TrueTransactMain.CBMSPARAMETERS.get("SA_CASH_PAYMENT_LIMIT"));
                    limitSet = true;
                }
                if (limitSet) {
                    HashMap limitMap = new HashMap();
                    limitMap.put("ACT_NUM", txtAccNo.getText());
                    limitMap.put("TRANS_DT", currDt.clone());
                    limitMap.put("AC_HD_ID", txtAccHdId.getText());
                    limitMap.put("PROD_TYPE", observable.getProdType());
                    limitMap.put("TRANS_TYPE", "DEBIT");
                    List limitList = ClientUtil.executeQuery("getTotalPaymentReceiptDetailsForAccounts", limitMap);
                    if (limitList != null && limitList.size() > 0) {
                        limitMap = (HashMap) limitList.get(0);
                        if (limitMap.containsKey("TOTAL_PAYMENT_RECEIPT") && limitMap.get("TOTAL_PAYMENT_RECEIPT") != null) {
                            double paymentAmt = CommonUtil.convertObjToDouble(txtAmount.getText()) + CommonUtil.convertObjToDouble(limitMap.get("TOTAL_PAYMENT_RECEIPT"));
                            if (paymentAmt > paymentLimit) {
                                ClientUtil.displayAlert("Cash Payment Exceeds Limit !! Limit Amount" + paymentLimit);
                                txtAmount.setText("");
                                txtAmount.grabFocus();
                                return;
                            }
                        }
                    }
                }
            } else if (rdoTransactionType_Credit.isSelected()) {
                double receiptLimit = 0.0;
                if (observable.getProdType().equals("AD") && TrueTransactMain.CBMSPARAMETERS.containsKey("OD_CASH_RECEIPT_LIMIT") && TrueTransactMain.CBMSPARAMETERS.get("OD_CASH_RECEIPT_LIMIT") != null && CommonUtil.convertObjToDouble(TrueTransactMain.CBMSPARAMETERS.get("OD_CASH_RECEIPT_LIMIT")) > 0) {
                    receiptLimit = CommonUtil.convertObjToDouble(TrueTransactMain.CBMSPARAMETERS.get("OD_CASH_RECEIPT_LIMIT"));
                    limitSet = true;
                } else if (observable.getProdType().equals("TL")) {
                    HashMap hash = new HashMap();
                    hash.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString());
                    List lst = ClientUtil.executeQuery("getLoanBehaves", hash);
                    if (lst.size() > 0) {
                        hash = (HashMap) lst.get(0);
                        if (CommonUtil.convertObjToStr(hash.get("BEHAVES_LIKE")).equals("LOANS_AGAINST_DEPOSITS") && TrueTransactMain.CBMSPARAMETERS.containsKey("DL_CASH_RECEIPT_LIMIT") && TrueTransactMain.CBMSPARAMETERS.get("DL_CASH_RECEIPT_LIMIT") != null && CommonUtil.convertObjToDouble(TrueTransactMain.CBMSPARAMETERS.get("DL_CASH_RECEIPT_LIMIT")) > 0) {
                            receiptLimit = CommonUtil.convertObjToDouble(TrueTransactMain.CBMSPARAMETERS.get("DL_CASH_RECEIPT_LIMIT"));
                            limitSet = true;
                        } else if (TrueTransactMain.CBMSPARAMETERS.containsKey("TL_CASH_RECEIPT_LIMIT") && TrueTransactMain.CBMSPARAMETERS.get("TL_CASH_RECEIPT_LIMIT") != null && CommonUtil.convertObjToDouble(TrueTransactMain.CBMSPARAMETERS.get("TL_CASH_RECEIPT_LIMIT")) > 0) {
                            receiptLimit = CommonUtil.convertObjToDouble(TrueTransactMain.CBMSPARAMETERS.get("TL_CASH_RECEIPT_LIMIT"));
                            limitSet = true;
                        }
                    }
                } else if (observable.getProdType().equals("GL") && TrueTransactMain.CBMSPARAMETERS.containsKey("GL_CASH_RECEIPT_LIMIT") && TrueTransactMain.CBMSPARAMETERS.get("GL_CASH_RECEIPT_LIMIT") != null && CommonUtil.convertObjToDouble(TrueTransactMain.CBMSPARAMETERS.get("GL_CASH_RECEIPT_LIMIT")) > 0) {
                    receiptLimit = CommonUtil.convertObjToDouble(TrueTransactMain.CBMSPARAMETERS.get("GL_CASH_RECEIPT_LIMIT"));
                    limitSet = true;
                } else if (observable.getProdType().equals("SA") && TrueTransactMain.CBMSPARAMETERS.containsKey("SA_CASH_RECEIPT_LIMIT") && TrueTransactMain.CBMSPARAMETERS.get("SA_CASH_RECEIPT_LIMIT") != null && CommonUtil.convertObjToDouble(TrueTransactMain.CBMSPARAMETERS.get("SA_CASH_RECEIPT_LIMIT")) > 0) {
                    receiptLimit = CommonUtil.convertObjToDouble(TrueTransactMain.CBMSPARAMETERS.get("SA_CASH_RECEIPT_LIMIT"));
                    limitSet = true;
                }

                if (limitSet) {
                    HashMap limitMap = new HashMap();
                    limitMap.put("ACT_NUM", txtAccNo.getText());
                    limitMap.put("TRANS_DT", currDt.clone());
                    limitMap.put("AC_HD_ID", txtAccHdId.getText());
                    limitMap.put("PROD_TYPE", observable.getProdType());
                    limitMap.put("TRANS_TYPE", "CREDIT");
                    List limitList = ClientUtil.executeQuery("getTotalPaymentReceiptDetailsForAccounts", limitMap);
                    if (limitList != null && limitList.size() > 0) {
                        limitMap = (HashMap) limitList.get(0);
                        if (limitMap.containsKey("TOTAL_PAYMENT_RECEIPT") && limitMap.get("TOTAL_PAYMENT_RECEIPT") != null) {
                            double paymentAmt = CommonUtil.convertObjToDouble(txtAmount.getText()) + CommonUtil.convertObjToDouble(limitMap.get("TOTAL_PAYMENT_RECEIPT"));
                            if (paymentAmt > receiptLimit) {
                                ClientUtil.displayAlert("Cash Receipt Exceeds Limit !! Limit Amount" + receiptLimit);
                                txtAmount.setText("");
                                txtAmount.grabFocus();
                                return;
                            }
                        }
                    }
                }
            }

        }
        // End
        
        
        
         double taxAmt= CommonUtil.convertObjToDouble(txtAmount.getText()); 
        double serviceTaxAmt = 0;
        String taxApplicable = "";
        if (taxAmt > 0 && TrueTransactMain.SERVICE_TAX_REQ.equals("Y") && rdoTransactionType_Credit.isSelected() == true) {// Changing the code by nithya for converting service tax to GST 
            HashMap whereMap = new HashMap();
            if (((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString().equals("GL")) {
                List taxSettingsList = new ArrayList();
                HashMap taxMap;
                whereMap.put("AC_HD_ID", txtAccHdId.getText());
                List temp = ClientUtil.executeQuery("getCheckServiceTaxApplicableForShare", whereMap);
                if (temp != null && temp.size() > 0) {
                    HashMap value = (HashMap) temp.get(0);
                    if (value != null && value.containsKey("SERVICE_TAX_APPLICABLE")) {
                        taxApplicable = CommonUtil.convertObjToStr(value.get("SERVICE_TAX_APPLICABLE"));
                        if (taxApplicable != null && taxApplicable.equals("Y") && taxAmt > 0) {
                            if (value.containsKey("SERVICE_TAX_ID") && value.get("SERVICE_TAX_ID") != null && CommonUtil.convertObjToStr(value.get("SERVICE_TAX_ID")).length() > 0) {
                                taxMap = new HashMap();
                                taxMap.put("SETTINGS_ID", value.get("SERVICE_TAX_ID"));
                                taxMap.put(ServiceTaxCalculation.TOT_AMOUNT, CommonUtil.convertObjToStr(taxAmt));
                                taxSettingsList.add(taxMap);
                            }
                        }
                    }
                }
                //if (taxApplicable != null && taxApplicable.equals("Y") && taxAmt > 0) {
                  if(taxSettingsList != null && taxSettingsList.size() > 0){
                      HashMap ser_Tax_Val = new HashMap();
                      ser_Tax_Val.put(ServiceTaxCalculation.CURR_DT, currDt.clone());
                      //ser_Tax_Val.put(ServiceTaxCalculation.TOT_AMOUNT, CommonUtil.convertObjToStr(taxAmt)); //Commented by nithya                     
                      try {
                          objServiceTax = new ServiceTaxCalculation();
                          //ser_Tax_Val.put("TEXT_BOX", "TEXT_BOX");
                          ser_Tax_Val.put("SERVICE_TAX_DATA", taxSettingsList);// Added by nithya 
                          serviceTax_Map = objServiceTax.calculateServiceTax(ser_Tax_Val);
                          if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.TOT_TAX_AMT)) {
                              //serviceTax_Map.put("TEXT_BOX", "TEXT_BOX");
                              double amt = CommonUtil.convertObjToDouble(serviceTax_Map.get(ServiceTaxCalculation.TOT_TAX_AMT));
                              //serviceTaxAmt = CommonUtil.convertObjToDouble(objServiceTax.roundOffAmt(amt, "NEAREST_VALUE"));                          
                              //lblServiceTaxval.setText(objServiceTax.roundOffAmt(amt, "NEAREST_VALUE"));
                              amt = Math.round(CommonUtil.convertObjToDouble(amt));
                              lblServiceTaxval.setText(CommonUtil.convertObjToStr(amt));
                              serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, amt);
                              //serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, objServiceTax.roundOffAmt(amt, "NEAREST_VALUE"));
                          } else {
                              lblServiceTaxval.setText("0.00");
                          }

                      } catch (Exception ex) {
                          ex.printStackTrace();
                      }
                  }
            } else if (observable.getProdType().equals("TL") || observable.getProdType().equals("AD")) {
                calculateServiceTaxAmt();
            } 
            // Commented by nithya on 28-11-2018 for KD 346 - Gst amount calculation issue at Rd Remittance at cash and Transfer
//            else if (observable.getProdType().equals("TD") && depBehavesLike != null && depBehavesLike.equals("RECURRING")){
//                calculateRDServiceTaxAmt();
//            }
        } else {
            lblServiceTaxval.setText("0.00");
        }     
            //Added by sreekrishnan
            if(rdoTransactionType_Debit.isSelected() == true && depBehavesLike != null && depBehavesLike.equals("DAILY")){
                String depNo = txtAccNo.getText();
                HashMap dailyMap = new HashMap();
                if (depNo.contains("_")) {
                    depNo = depNo.substring(0, depNo.lastIndexOf("_"));
                }
                dailyMap.put("DEPOSIT_NO", depNo);
                List Dailylst = ClientUtil.executeQuery("getDailyDepositBalnce", dailyMap);
                if (Dailylst.size() > 0 && Dailylst!=null) {
                    dailyMap = (HashMap) Dailylst.get(0);
                    double balAmount = CommonUtil.convertObjToDouble(dailyMap.get("BALANCE")).doubleValue();
                    double amount = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                    if (balAmount < amount) {
                        //system.out.println("@#$@#$%$^$%^amount" + amount + " :depAmount: " + depAmount);
                        ClientUtil.displayAlert("Entered Amount is More than available balance!!");
                        txtAmount.setText("");
                        txtAmount.grabFocus();
                        return;
                    }
                }
            }
    }//GEN-LAST:event_txtAmountFocusLost
    private boolean moreThanLoanAmountAlert() {
        double totWaiveamt = 0;
        if (cboProdType.getSelectedIndex() > 0) {
            String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
            if (rdoTransactionType_Credit.isSelected() == true && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && prodType.equals("TL")) {
                double totalLoanAmt = setEMIAmount();
                HashMap allAmtMap = observable.getALL_LOAN_AMOUNT();
                if (allAmtMap.containsKey("CURR_MONTH_PRINCEPLE") && CommonUtil.convertObjToDouble(allAmtMap.get("CURR_MONTH_PRINCEPLE")).doubleValue() > 0) {
                    totalLoanAmt -= CommonUtil.convertObjToDouble(allAmtMap.get("CURR_MONTH_PRINCEPLE")).doubleValue();
                }
                if (allAmtMap.containsKey("CLEAR_BALANCE") && CommonUtil.convertObjToDouble(allAmtMap.get("CLEAR_BALANCE")).doubleValue() < 0) {
                    totalLoanAmt += -CommonUtil.convertObjToDouble(allAmtMap.get("CLEAR_BALANCE")).doubleValue();
                }
               
                double actualAmt = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                if (CommonUtil.convertObjToDouble(lblServiceTaxval.getText()) > 0) {
                    totalLoanAmt += CommonUtil.convertObjToDouble(lblServiceTaxval.getText());
                    actualAmt += CommonUtil.convertObjToDouble(lblServiceTaxval.getText());
                }
                //Added by rishad for adding totalwaiveamt to paidamount for checking it will exeeding the loanclosing amount
                if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                    totWaiveamt = CommonUtil.convertObjToDouble(returnWaiveMap.get("Total_WaiveAmt"));
                    if (totWaiveamt > 0) {
                        actualAmt = actualAmt + totWaiveamt;
                    }
                }
                if (actualAmt >= totalLoanAmt) {
                    int message = ClientUtil.confirmationAlert("Entered Transaction Amount is equal to/more than the Outstanding Loan Amount," + "\n" + "Do You Want to Close the A/c?");
                    if (message == 0) {
                        HashMap hash = new HashMap();
                        CInternalFrame frm = new com.see.truetransact.ui.operativeaccount.AccountClosingUI("TermLoan");
                        frm.setSelectedBranchID(getSelectedBranchID());
                        TrueTransactMain.showScreen(frm);
                        hash.put("FROM_TRANSACTION_SCREEN", "FROM_TRANSACTION_SCREEN");
                        hash.put("ACCOUNT NUMBER", txtAccNo.getText());
//                        hash.put("PROD_ID",CommonUtil.convertObjToStr(((ComboBoxModel)cboProductID.getModel()).getKeyForSelected()));
                        hash.put("PROD_ID", CommonUtil.convertObjToStr(cboProdId.getSelectedItem()));
                        frm.fillData(hash);
                    }
                    return true;
                }
            }
        }
        return false;
    }
    private void txtAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAmountActionPerformed

        if (CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue() > 0) {
            txtAmount.setToolTipText(CommonUtil.currencyToWord(CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue()));
        } else {
            txtAmount.setToolTipText("Zero");
        }
    }//GEN-LAST:event_txtAmountActionPerformed

    private void txtAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAccNoActionPerformed
        //        // TODO add your handling code here:
        //        HashMap hash = new HashMap();
        //        String ACCOUNTNO = (String) txtAccNo.getText();
        //        if( observable.getProdType().equals("TD")){
        //            if (ACCOUNTNO.lastIndexOf("_")!=-1){
        //                hash.put("ACCOUNTNO", txtAccNo.getText());
        //            }else
        //                hash.put("ACCOUNTNO", txtAccNo.getText()+"_1");
        //        }else{
        //            hash.put("ACCOUNTNO", txtAccNo.getText());
        //        }
        //        hash.put("ACT_NUM", hash.get("ACCOUNTNO"));
        //        hash.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
        //        hash.put("SELECTED_BRANCH",ProxyParameters.BRANCH_ID);
        //        List actlst=null;
        //        List lst=null;
        //        HashMap notClosedMap = new HashMap();
        //        if( observable.getProdType().equals("TD")){
        //            actlst=ClientUtil.executeQuery("getNotClosedDeposits",hash);
        //            if(actlst!=null && actlst.size()>0){
        //                notClosedMap =(HashMap)actlst.get(0);
        //            }
        //        }
        //
        //        if( observable.getProdType().equals("TL"))
        //            actlst=ClientUtil.executeQuery("getActNotCLOSEDTL",hash);
        //
        //        if( observable.getProdType().equals("OA"))
        //            observable.setAccountName(ACCOUNTNO);
        //
        //        if(observable.getProdType().equals("TD") || observable.getProdType().equals("TL")){
        //            if(rdoTransactionType_Debit.isSelected() || rdoTransactionType_Credit.isSelected()){
        //                if(observable.getProdType().equals("TL")){
        //                    if(actlst!=null && actlst.size()>0){
        //                        viewType = ACCNO;
        //                        updateOBFields();
        //                        hash.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
        //                        hash.put("SELECTED_BRANCH",ProxyParameters.BRANCH_ID);
        //                        if( observable.getProdType().equals("TL")) {
        //                            if(rdoTransactionType_Debit.isSelected()) {
        //                                hash.put("PAYMENT","PAYMENT");
        //                                lst=ClientUtil.executeQuery("Cash.getAccountList"
        //                                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(),hash);
        //                            }else if(rdoTransactionType_Credit.isSelected()){
        //                                if(observable.getProdType().equals("TL"))
        //                                    hash.put("RECEIPT","RECEIPT");
        //                                //system.out.println("hash"+hash);
        //                                lst=ClientUtil.executeQuery("Cash.getAccountList"
        //                                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(),hash);
        //                            }
        //                            fillData(hash);
        //                        }
        //                    }else{
        //                        ClientUtil.showAlertWindow(" Invalid Number Choose correct number");
        //                        txtAccNo.setText("");
        ////                        txtAccNo.requestFocus();
        //                    }
        //                }else if(observable.getProdType().equals("TD")){
        //                    viewType = ACCNO;
        //                    updateOBFields();
        //                    hash.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
        //                    hash.put("SELECTED_BRANCH",ProxyParameters.BRANCH_ID);
        //                    if(actlst!=null && actlst.size()>0){
        //                        if(observable.getProdType().equals("TD")){
        //                            hash.put("RECEIPT","RECEIPT");
        //                            if(rdoTransactionType_Debit.isSelected()) {
        //                                //                                if(observable.getProdType().equals("TD")){
        //                                //                                    hash.put("PAYMENT","PAYMENT");
        //                                //                                    lst=ClientUtil.executeQuery("Cash.getAccountList"
        //                                //                                    + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(),hash);
        //                                //                                }else{
        //                                lst=ClientUtil.executeQuery("getDepositHoldersInterest",hash);
        //                                transDetails.setIsDebitSelect(true);
        //                            }else if(rdoTransactionType_Credit.isSelected()){
        //                                lst=ClientUtil.executeQuery("Cash.getAccountList"
        //                                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(),hash);
        //                            }
        //                            hash.put("PRODUCTTYPE",notClosedMap.get("BEHAVES_LIKE"));
        //                            hash.put("TYPE",notClosedMap.get("BEHAVES_LIKE"));
        //                            hash.put("AMOUNT",notClosedMap.get("DEPOSIT_AMT"));
        //                            fillData(hash);
        //                        }
        //                    }else{
        //                        ClientUtil.showAlertWindow(" Invalid Number Choose correct number");
        //                        txtAccNo.setText("");
        //                    }
        //                }
        //            }else{
        //                ClientUtil.showMessageWindow("Select Payment or Receipt ");
        //                txtAccNo.setText("");
        ////                txtAccNo.requestFocus();
        //                return;
        //            }
        //        }else if(observable.getProdType().equals("OA")){
        //            viewType = ACCNO;
        //            HashMap listMap = new HashMap();
        //            if(observable.getLblAccName().length()>0){
        //                updateOBFields();
        //                hash.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
        //                hash.put("SELECTED_BRANCH",ProxyParameters.BRANCH_ID);
        //                lst=ClientUtil.executeQuery("Cash.getAccountList"
        //                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(),hash);
        //                fillData(hash);
        //                observable.setLblAccName("");
        //            }else{
        //                ClientUtil.showAlertWindow(" Invalid Number Choose correct number");
        //                txtAccNo.setText("");
        //            }
        //        }
    }//GEN-LAST:event_txtAccNoActionPerformed
      //Added by chithra for mantis: 10345: New Weekly Deposit Schemes For Pudukad SCB
    private void chkWeeklyDepositReceiptDt(String accNum) {
        if (accNum.length() > 0 && depBehavesLike != null && depBehavesLike.equalsIgnoreCase("DAILY")) {
            HashMap prodMap = new HashMap();
            String depNo = accNum;
            if (depNo.contains("_")) {
            depNo = depNo.substring(0, depNo.lastIndexOf("_"));
            }
            prodMap.put("ACCT_NO", depNo);
            List lst = ClientUtil.executeQuery("getMaxDayEndDT", prodMap);
            if (lst != null && lst.size() > 0) {
                prodMap = (HashMap) lst.get(0);
                if (prodMap != null && prodMap.size() > 0) {
                    String depFre = CommonUtil.convertObjToStr(prodMap.get("DAY_END_DT"));
                    if (depFre != null && !depFre.equals("")) {
                        Calendar a = new GregorianCalendar(2002, 1, 22);
                        a.setTime(DateUtil.getDateMMDDYYYY(depFre));
                        Calendar b = new GregorianCalendar(2015, 0, 12);
                        b.setTime((Date)currDt.clone());
                        int weeks = b.get(Calendar.WEEK_OF_YEAR) - a.get(Calendar.WEEK_OF_YEAR);
                        if (weeks <= 0) {
                            ClientUtil.showAlertWindow("Current week Recipt is already done!!");
                            txtAccNo.setText("");
                            return;
                        }
                    }
                }
            }
        }
    }
    private void txtAccNoActionPerformed() {
        returnWaiveMap = null;
     	if (txtAccNo.getText().length() > 0 && cboProdType.getSelectedIndex() != 1) {
            if (!txtAccNo.getText().equals(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID))) {
                // TODO add your handling code here:
                boolean debit = rdoTransactionType_Debit.isSelected();
                boolean credit = rdoTransactionType_Credit.isSelected();
                if (observable.getKccNature().equals("Y")) {
                    if (debit) {
                        //System.out.println("kccFlag####" + observable.getKccNature());
                        String ACCOUNTNO = txtAccNo.getText();
                        //System.out.println("ACCOUNTNO####PutXTblic" + txtAccNo.getText());
                        //(txtAccNo.getText());
                        //System.out.println("HASH kccRenewalChecking ======" + ACCOUNTNO);
                        HashMap hmap = new HashMap();
                        Date toDate = new Date();
                        hmap.put("ACCOUNTNO", ACCOUNTNO);
                        List kccList1 = ClientUtil.executeQuery("getKccSacntionTodate", hmap);
                        if (kccList1 != null && kccList1.size() > 0) {
                            hmap = (HashMap) kccList1.get(0);
                            toDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hmap.get("TO_DT")));
                            //System.out.println("toDate###" + toDate);
                            if (!toDate.equals("")) {
                                if (toDate.before((Date) currDt.clone())) {
                                    ClientUtil.showAlertWindow("Account not Renewed!Can't make Payments!!");
                                    return;
                                }
                            }
                        }
                    }
                }
//                //Added by sreekrishnan for interestPaidUpto
//                HashMap interestMap = new HashMap();
//                interestMap.put("ACCOUNTNO", txtAccNo.getText());
//                interestMap.put("ASON_DT",currDt.clone());
//                interestMap.put("TRANS_AMOUNT",new Double(0));
//                List interestList = ClientUtil.executeQuery("getInterestPaidUpToDate", interestMap);
//                if (interestList != null && interestList.size() > 0) {
//                    interestMap = (HashMap) interestList.get(0);
//                    interestPaidUpto = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(interestMap.get("INTEREST_UPTO")));
//                    System.out.println("interestPaidUpto###" + interestPaidUpto);                    
//                }
                HashMap hash = new HashMap();
                String ACCOUNTNO = (String) txtAccNo.getText();
                //System.out.println("ACCOUNTNO from action##" + ACCOUNTNO);
//              observable.setProdType("");
                if (/*
                         * (!(observable.getProdType().length()>0)) &&
                         */ACCOUNTNO.length() > 0) {
                    if (observable.checkAcNoWithoutProdType(ACCOUNTNO)) {
                        cboProdId.setModel(observable.getCbmProdId());
                        cboProdIdActionPerformed(null);
//                      txtAccNo.setText(ACCOUNTNO);
                        txtAccNo.setText(observable.getTxtAccNo());
                        ACCOUNTNO = (String) txtAccNo.getText();
                        String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
                        if (debit) {
                            rdoTransactionType_Debit.setSelected(true);
                        }
                        if (credit) {
                            rdoTransactionType_Credit.setSelected(true);
                        }
                        setSelectedBranchID(observable.getSelectedBranchID());
                    } else {
                        String oaActStatus = "";                    
                        HashMap statusMap = new HashMap();
                        statusMap.put(CommonConstants.ACT_NUM, ACCOUNTNO);
                        List statusList =  ClientUtil.executeQuery("getStatusForAccount", statusMap);
                        if(statusList != null && statusList.size() > 0){
                            statusMap = (HashMap)statusList.get(0);
                            if(statusMap.containsKey("ACT_STATUS_ID") && statusMap.get("ACT_STATUS_ID") != null){
                                oaActStatus = "Status :" + CommonUtil.convertObjToStr(statusMap.get("ACT_STATUS_ID"));
                            }
                        }
                        
                        
                        ClientUtil.showAlertWindow("Invalid Account No." + oaActStatus);
                       // txtAccNo.setText("");
                        isValid=true;
                        //Added BY Suresh
                        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                            txtAccNo.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID));
                        }
                        return;
                    }
                }
                System.out.println("observable.getProdType()-------------------"+observable.getProdType());
                if (observable.getProdType().equals("TD")) {
                    if (ACCOUNTNO.lastIndexOf("_") != -1) {
                        hash.put("ACCOUNTNO", txtAccNo.getText());
                    } else {
                        hash.put("ACCOUNTNO", txtAccNo.getText() + "_1");
                    }
                } else {
                    hash.put("ACCOUNTNO", txtAccNo.getText());
                }
                hash.put("ACT_NUM", hash.get("ACCOUNTNO"));
                hash.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
                hash.put("SELECTED_BRANCH", ProxyParameters.BRANCH_ID);
                List actlst = null;
//              List lst=null;
                HashMap notClosedMap = new HashMap();
                if (observable.getProdType().equals("TD")) {
                    if (credit == true) {
                        hash.put("CREDIT_TRANS", "CREDIT_TRANS");
                    }
                    actlst = ClientUtil.executeQuery("getNotClosedDeposits", hash);
                    if (actlst != null && actlst.size() > 0) {
                        notClosedMap = (HashMap) actlst.get(0);
                    }
                }

                if (observable.getProdType().equals("TL") || observable.getProdType().equals("AD")) {
                    actlst = ClientUtil.executeQuery("getActNotCLOSEDTL", hash);
                }

//              if( observable.getProdType().equals("OA"))
                //System.out.println("44###@@@");
                //System.out.println("observable.getProdType()observable.getProdType()" + observable.getProdType());
                observable.setAccountName(ACCOUNTNO);
                lblHouseName.setText(observable.getLblHouseName());
                lblAccName.setText(observable.getLblAccName());
                if (observable.getProdType().equals("TD") || observable.getProdType().equals("TL") || observable.getProdType().equals("AD")) {
                    if (debit || credit) {
                        if (observable.getProdType().equals("TL") || observable.getProdType().equals("AD")) {
                            if (actlst != null && actlst.size() > 0) {

                                viewType = ACCNO;
                                updateOBFields();
                                hash.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
                                hash.put("SELECTED_BRANCH", TrueTransactMain.selBranch);
                                //                        if( observable.getProdType().equals("TL")) {
                                if (debit) {
                                    hash.put("PAYMENT", "PAYMENT");
                                    //                                lst=ClientUtil.executeQuery("Cash.getAccountList"
                                    //                                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(),hash);
                                } else if (credit) {
                                    if (observable.getProdType().equals("TL")) {
                                        hash.put("RECEIPT", "RECEIPT");
                                    }
                                    //system.out.println("hash" + hash);
                                    //                                lst=ClientUtil.executeQuery("Cash.getAccountList"
                                    //                                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(),hash);
                                }



                                //system.out.println("testing........");
                                fillData(hash);
                                //                        }
                            } else {
                                ClientUtil.showAlertWindow(" Invalid Number Choose correct number");
                                txtAccNo.setText("");
                                //                        txtAccNo.requestFocus();
                            }
                        } else if (observable.getProdType().equals("TD")) {
                            viewType = ACCNO;
                            updateOBFields();
                            hash.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
                            hash.put("SELECTED_BRANCH", TrueTransactMain.selBranch);
                            if (actlst != null && actlst.size() > 0) {
                                if (observable.getProdType().equals("TD")) {
                                    hash.put("RECEIPT", "RECEIPT");
                                    if (debit) {
                                        rdoTransactionType_Debit.setSelected(true);
                                        //                                if(observable.getProdType().equals("TD")){
                                        //                                    hash.put("PAYMENT","PAYMENT");
                                        //                                    lst=ClientUtil.executeQuery("Cash.getAccountList"
                                        //                                    + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(),hash);
                                        //                                }else{
                                        //                                lst=ClientUtil.executeQuery("getDepositHoldersInterest",hash);
                                        transDetails.setIsDebitSelect(true);
                                    } else if (credit) {
                                        rdoTransactionType_Credit.setSelected(true);
                                        //                                lst=ClientUtil.executeQuery("Cash.getAccountList"
                                        //                                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(),hash);
                                    }
                                    hash.put("PRODUCTTYPE", notClosedMap.get("BEHAVES_LIKE"));
                                    hash.put("TYPE", notClosedMap.get("BEHAVES_LIKE"));
                                    hash.put("AMOUNT", notClosedMap.get("DEPOSIT_AMT"));
                                    fillData(hash);
                                    if (debit) {
                                        rdoTransactionType_Debit.setSelected(true);
                                    } else if (credit) {
                                        rdoTransactionType_Credit.setSelected(true);
                                    }
                                }
                            } else if (actlst.isEmpty() && credit == true) {
                                ClientUtil.showAlertWindow(" Already Transaction Completed...");
                                rdoTransactionType_Credit.setSelected(true);
                                txtAccNo.setText("");
                                txtAmount.setText("");
                                return;
                            } else {
                                ClientUtil.showAlertWindow(" Invalid Number Choose correct number");
                                txtAccNo.setText("");
                            }
                        }
                    } else {
                        ClientUtil.showMessageWindow("Select Payment or Receipt ");
                        txtAccNo.setText("");
                        return;
                    }
                } else if (observable.getProdType().equals("OA")) {
                    viewType = ACCNO;
                    HashMap listMap = new HashMap();
                    if (observable.getLblAccName().length() > 0) {
                        updateOBFields();
                        hash.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
                        hash.put("SELECTED_BRANCH", TrueTransactMain.selBranch);
                        //                lst=ClientUtil.executeQuery("Cash.getAccountList"
                        //                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(),hash);
                        fillData(hash);
                        observable.setLblAccName("");
                        
                        //Write Code here -- If view not checking                          
                             checkValidStatusForOperativeAccts();                        
                        // End
                        
                    } else {
                        ClientUtil.showAlertWindow(" Invalid Number Choose correct number\n Account Closed");
                        txtAccNo.setText("");
                    }
                }
                if (observable.getProdType().equals("SA")) {
                    viewType = ACCNO;
                    HashMap listMap = new HashMap();
                    if (observable.getLblAccName().length() > 0) {
                        updateOBFields();
                        hash.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
                        hash.put("SELECTED_BRANCH", TrueTransactMain.selBranch);
                        //                lst=ClientUtil.executeQuery("Cash.getAccountList"
                        //                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(),hash);
                        fillData(hash);
                        observable.setLblAccName("");
                    }
                }
                //Added by sreekrishnan
                //System.out.println("observable.getProdType()###@@@"+observable.getProdType());
                if ((observable.getProdType().equals(CommonConstants.TXN_PROD_TYPE_ADVANCES) || observable.getProdType().equals(CommonConstants.TXN_PROD_TYPE_LOANS)) && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                    HashMap hmap = new HashMap();
                    Date toDate = new Date();
                    hmap.put("ACCOUNTNO",ACCOUNTNO);        
                    List kccList1 = ClientUtil.executeQuery("getKccSacntionTodate", hmap);
                    if (kccList1 != null && kccList1.size() > 0) {
                        hmap = (HashMap) kccList1.get(0);
                        toDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hmap.get("TO_DT")));
                        //System.out.println("in focus lost toDate###"+toDate);
                        if(!toDate.equals("")){
                            if (toDate.before((Date) currDt.clone())) {
                                if(observable.getProdType().equals(CommonConstants.TXN_PROD_TYPE_LOANS)){
                                    lblLoanStatus.setText("Loan Period is Over");
                                }else{
                                    lblLoanStatus.setText("Advance Period is Over");
                                }
                            }else{
                               lblLoanStatus.setText("");
                            }
                        }else{
                               lblLoanStatus.setText("");
                            }
                    }else{
                          lblLoanStatus.setText("");
                    }
                }
            }
        }
     isAccountNumberExsistInAuthList(CommonUtil.convertObjToStr(txtAccNo.getText()));
     isAccountNumberLinkedwithATMProd(CommonUtil.convertObjToStr(txtAccNo.getText()));
 //  transDetails.setRepayData(setRepaymentData());       
    }
    
    
      private void checkValidStatusForOperativeAccts() {
        HashMap whereMap = new HashMap();
        whereMap.put("ACT_NUM", txtAccNo.getText());
        List statusList = ClientUtil.executeQuery("getOperativeAccountsStatus", whereMap);
        if (statusList != null && statusList.size() > 0) {
            HashMap statusMap = (HashMap) statusList.get(0);
            if (statusMap.containsKey("ACT_STATUS_ID") && statusMap.get("ACT_STATUS_ID") != null) {
                ClientUtil.showAlertWindow(" Invalid Number\n Account status is :" + CommonUtil.convertObjToStr(statusMap.get("ACT_STATUS_ID")));
                txtAccNo.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID));
                transDetails.setTransDetails(null, null, null);
            }
        }
    }

    
    
    
    
    private void cboInstrumentTypeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboInstrumentTypeFocusLost
        instrumentTypeFocus();
        String instrumentType = CommonUtil.convertObjToStr(((ComboBoxModel) cboInstrumentType.getModel()).getKeyForSelected());
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && cboInstrumentType.getSelectedIndex() > 0 && rdoTransactionType_Debit.isSelected()) {
         String chkbook = "";
        int count = 0;
        HashMap hmap = new HashMap();
        if (chqBalList != null && chqBalList.size() > 0) {
            hmap = (HashMap) chqBalList.get(0);
            chkbook = CommonUtil.convertObjToStr(hmap.get("CHQ_BOOK"));
            count = CommonUtil.convertObjToInt(hmap.get("CNT"));
            hmap = null;
        }
        
          if ((chkbook.equals("Y"))) {
            if (!observable.getProdType().equals("GL")) {
                if (!instrumentType.equals("CHEQUE")) {
                    int yesNo = 0;
                    String[] options = {"Yes", "No"};
                    String actNo = txtAccNo.getText();
                    HashMap mapCheqIsued = new HashMap();
                    mapCheqIsued.put("ACT_NUM", actNo);
                    List lstCheqIssued = ClientUtil.executeQuery("getSelChequeIssueDet", mapCheqIsued);
                    //System.out.println("lstCheqIssued" + lstCheqIssued);
                     //unncommented by rishad 29/01/2015
                    if (lstCheqIssued != null && !lstCheqIssued.isEmpty()) {
                    // if(mapCheqIsued.get(""))
                        yesNo = COptionPane.showOptionDialog(null, "Cheque book is issued to the customer. Do you want to continue?", CommonConstants.WARNINGTITLE,
                                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                                null, options, options[0]);
                    }
                    //system.out.println("#$#$$ yesNo : " + yesNo);
                  if (yesNo != 0) {
                     cboInstrumentType.setSelectedItem(null);
                }
                }
            }}
        }
    }//GEN-LAST:event_cboInstrumentTypeFocusLost

    public void btnCheck() {
        btnCancel.setEnabled(true);
        btnSave.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnEdit.setEnabled(false);
        btnView.setEnabled(false);
    }

    private void instrumentTypeFocus() {
        String instrumentType = CommonUtil.convertObjToStr(((ComboBoxModel) cboInstrumentType.getModel()).getKeyForSelected());
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && cboInstrumentType.getSelectedIndex() > 0) {

            if (instrumentType.equals("VOUCHER")) {
                tdtInstrumentDate.setDateValue(DateUtil.getStringDate(observable.getCurrentDate()));
                tdtInstrumentDate.setEnabled(false);
                txtInstrumentNo1.setEnabled(false);
                txtInstrumentNo2.setEnabled(false);
                
            } else if (instrumentType.equals("WITHDRAW_SLIP")){
                tdtInstrumentDate.setDateValue(DateUtil.getStringDate(observable.getCurrentDate()));
                tdtInstrumentDate.setEnabled(false);
                txtInstrumentNo1.setEnabled(true);
                txtInstrumentNo2.setEnabled(true);
            } else {
                tdtInstrumentDate.setDateValue(DateUtil.getStringDate(observable.getCurrentDate()));
                tdtInstrumentDate.setEnabled(true);
                txtInstrumentNo1.setEnabled(true);
                txtInstrumentNo2.setEnabled(true);
            }
       }
        String chkbook = "";
        int count = 0;
        HashMap hmap = new HashMap();
        if (chqBalList != null && chqBalList.size() > 0) {
            hmap = (HashMap) chqBalList.get(0);
            chkbook = CommonUtil.convertObjToStr(hmap.get("CHQ_BOOK"));
            count = CommonUtil.convertObjToInt(hmap.get("CNT"));
            hmap = null;
        }
//        if ((chkbook.equals("Y"))) {
//            if (!observable.getProdType().equals("GL")) {
//                if (!instrumentType.equals("CHEQUE")) {
//                    int yesNo = 0;
//                    String[] options = {"Yes", "No"};
//                    String actNo = txtAccNo.getText();
//                    HashMap mapCheqIsued = new HashMap();
//                    mapCheqIsued.put("ACT_NUM", actNo);
//                    List lstCheqIssued = ClientUtil.executeQuery("getSelChequeIssueDet", mapCheqIsued);
//                    System.out.println("lstCheqIssued" + lstCheqIssued);
//                     //unncommented by rishad 29/01/2015
//                    if (lstCheqIssued != null && !lstCheqIssued.isEmpty()) {
//                    // if(mapCheqIsued.get(""))
//                        yesNo = COptionPane.showOptionDialog(null, "Cheque book is issued to the customer. Do you want to continue?", CommonConstants.WARNINGTITLE,
//                                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
//                                null, options, options[0]);
//                    }
//                    //system.out.println("#$#$$ yesNo : " + yesNo);
//                  if (yesNo != 0) {
//                     cboInstrumentType.setSelectedItem(null);
//               
//                }
//            }
//        }
       
    }

    private void btnAccHdIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccHdIdActionPerformed
        viewType = ACCTHDID;
        final HashMap viewMap = new HashMap();
        viewMap.put(CommonConstants.MAP_NAME, "Cash.getSelectAcctHead");
        new ViewAll(this, viewMap).show(); 
        if(rdoTransactionType_Credit.isEnabled())
            rdoTransactionType_Credit.setSelected(true);
    }//GEN-LAST:event_btnAccHdIdActionPerformed

	private void txtTokenNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTokenNoFocusLost
            tokenChecking();
    }                /** To display a popUp window for viewing existing data *///GEN-LAST:event_txtTokenNoFocusLost
    private void tokenChecking() {
        // Validate Token in New Entry
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            String tokenNo = CommonUtil.convertObjToStr(txtTokenNo.getText());
            if (!tokenNo.equals("")) {
                HashMap tokenWhereMap = new HashMap();

                // Separating Serias No and Token No
                char[] chrs = tokenNo.toCharArray();
                StringBuffer seriesNo = new StringBuffer();
                int i = 0;
                for (int j = chrs.length; i < j; i++) {
                    if (Character.isDigit(chrs[i])) {
                        break;
                    } else {
                        seriesNo.append(chrs[i]);
                    }
                }

                tokenWhereMap.put("SERIES_NO", seriesNo.toString());
                tokenWhereMap.put("TOKEN_NO", CommonUtil.convertObjToInt(tokenNo.substring(i)));
                tokenWhereMap.put("USER_ID", ProxyParameters.USER_ID);
                tokenWhereMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
                tokenWhereMap.put("CURRENT_DT", observable.getCurrentDate());

                List lst = ClientUtil.executeQuery("validateTokenNo", tokenWhereMap);

                if (((Integer) lst.get(0)).intValue() == 0) {
                    txtTokenNo.setText("");
                    COptionPane.showMessageDialog(this, resourceBundle.getString("tokenMsg"));
                }
            }
        } // Token validation in New
    }

	private void btnCurrencyInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCurrencyInfoActionPerformed
            // Add your handling code here:
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
                HashMap paramMap = new HashMap();
                HashMap whereMap = new HashMap();
                String type = "";
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                        || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                        || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
                    paramMap.put("AUTHORIZE_MODE", "AUTHORIZE_MODE");
                    whereMap.put("AUTHORIZE_MODE", "AUTHORIZE_MODE");
                }
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                    paramMap.put("NEW_MODE", "NEW_MODE");
                    whereMap.put("NEW_MODE", "NEW_MODE");
                }
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    paramMap.put("EDIT_MODE", "EDIT_MODE");
                    whereMap.put("EDIT_MODE", "EDIT_MODE");
                }
                HashMap acctMap = new HashMap();
                acctMap.put("ACCT_HEAD", observable.getTxtAccHd());
                List head = ClientUtil.executeQuery("getSelectReconcileYesNO", acctMap);
                if (head != null && head.size() > 0) {
                    acctMap = (HashMap) head.get(0);
                    if (!acctMap.get("RECONS").equals("") && acctMap.get("RECONS").equals("Y")) {
                        if (acctMap.get("BALANCETYPE").equals("DEBIT") && rdoTransactionType_Credit.isSelected() == true) {
                            type = "DEBIT";
                        } else if (acctMap.get("BALANCETYPE").equals("CREDIT") && rdoTransactionType_Debit.isSelected() == true) {
                            type = "CREDIT";
                        } else if (acctMap.get("BALANCETYPE").equals("DEBIT") && rdoTransactionType_Debit.isSelected() == true) {
                            observable.setReconcile("Y");
                        } else if (acctMap.get("BALANCETYPE").equals("CREDIT") && rdoTransactionType_Credit.isSelected() == true) {
                            observable.setReconcile("Y");
                        }
                        observable.setBalanceType(CommonUtil.convertObjToStr(acctMap.get("BALANCETYPE")));
                    }
                }
                whereMap.put("TRANS_TYPE", type);
                paramMap.put("TRANS_TYPE", type);
                whereMap.put("AC_HEAD_ID", txtAccHdId.getText());
                paramMap.put("AC_HEAD_ID", txtAccHdId.getText());
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                        || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                        || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                        || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
                    whereMap.put("TRANS_ID", lblTransactionIDDesc.getText());
                    paramMap.put("TRANS_ID", lblTransactionIDDesc.getText());
                }
                paramMap.put(CommonConstants.MAP_NAME, "getSelectReconciliationTransaction");
                paramMap.put(CommonConstants.MAP_WHERE, whereMap);
                if (transCommonUI == null) {
                    transCommonUI = new TransCommonUI(paramMap);
                }
                transCommonUI.show();
                transCommonUI.setTransAmount(CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue());
                paramMap = null;
                whereMap = null;
            }
	}//GEN-LAST:event_btnCurrencyInfoActionPerformed

	private void txtInputAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInputAmtFocusLost
            // Add your handling code here:
            //                        String amt = "";
            //                        if (cboInputCurrency.getSelectedIndex() > -1) {
            //                            String type = "";
            //                            if (rdoTransactionType_Credit.isSelected()) {
            //                                type = "CREDIT";
            //                            } else {
            //                                type = "DEBIT";
            //                            }
            //                            if(this.txtInputAmt!=null && this.txtInputAmt.getText().length()>0){
            //                                try {
            //                                    amt = String.valueOf(
            //                                    ClientUtil.convertCurrency(
            //                                    (String) ((ComboBoxModel) cboInputCurrency.getModel()).getKeyForSelected(),
            //                                    lblProductCurrency.getText(),
            //                                    type, new Double(txtInputAmt.getText()).doubleValue()));
            //                                } catch (Exception e) {
            //                                    //system.out.println ("currency conversion error");
            //                                    amt = txtInputAmt.getText();
            //                                }
            //                            }
            //                        }
            //                        txtAmount.setText(amt);
	}//GEN-LAST:event_txtInputAmtFocusLost

	private void btnDenominationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDenominationActionPerformed
            // Add your handling code here:
            denomination();
    }//GEN-LAST:event_btnDenominationActionPerformed

    private void denomination() {
        // Add your handling code here:
        HashMap map = new HashMap();
        if (observable.getDenominationList() != null) {
            map.put("DENOMINATION_LIST", observable.getDenominationList());
        }
        if (rdoTransactionType_Credit.isSelected()) {
            map.put("TRANS_TYPE", "Deposit");
            map.put("CURRENCY_TYPE", LocaleConstants.DEFAULT_CURRENCY); //(String) ((ComboBoxModel) cboInputCurrency.getModel()).getKeyForSelected());
            map.put("AMOUNT", txtAmount.getText());//txtInputAmt.getText());
        } else if (rdoTransactionType_Debit.isSelected()) {
            map.put("TRANS_TYPE", "Withdrawal");
            map.put("CURRENCY_TYPE", LocaleConstants.DEFAULT_CURRENCY); //(String) ((ComboBoxModel) cboInputCurrency.getModel()).getKeyForSelected());
            map.put("AMOUNT", txtAmount.getText());//txtInputAmt.getText());
            //map.put("CURRENCY_TYPE", ClientUtil.getMainCurrency());
            //map.put("AMOUNT", txtAmount.getText());
        }

        DenominationUI dui = new DenominationUI(map);
        ClientUtil.showDialog(dui);
        Double fltDenomination[] = dui.getFltDenomination();
        com.see.truetransact.uicomponent.CTextField txtCount[] = dui.getTxtCount();
        String denominationType[] = dui.getDenominationType();

        String strCnt = "";
        HashMap denominationMap = null;
        ArrayList denominationList = new ArrayList();
        for (int i = 0; i < fltDenomination.length; i++) {
            strCnt = txtCount[i].getText().trim();
            if (strCnt != null && !strCnt.equals("0") && !strCnt.equals("")) {
                denominationMap = new HashMap();
                denominationMap.put("CURRENCY", LocaleConstants.DEFAULT_CURRENCY);
                denominationMap.put("COUNT", new Double(txtCount[i].getText()));
                denominationMap.put("DENOMINATION", fltDenomination[i]);
                denominationMap.put("DENOMINATION_TYPE", denominationType[i]);
                denominationList.add(denominationMap);
            }
        }
        //system.out.println("denominationList:" + denominationList);
        observable.setDenominationList(denominationList);

    }

    private void clearProdFields() {
        txtAccHdId.setText("");
        txtAccNo.setText("");
        lblAccHdDesc.setText("");
        lblAccName.setText("");
        lblHouseName.setText("");
        observable.setLblHouseName("");
    }

    private void cboProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeActionPerformed
        //To Set the Value of Account Head in UI...
        if (cboProdType.getSelectedIndex() > 0) {
            String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
            //system.out.println("***************" + prodType);
            clearProdFields();
            //Added BY Suresh
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && !prodType.equals("AB")) {
                txtAccNo.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID));
            }
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && prodType.equals("TL")|| prodType.equals("AD")) {
                btnWaive.setEnabled(true);
            } else {
                btnWaive.setEnabled(false);
            }
            populateInstrumentType();
            observable.setProdType(prodType);
            //this is for depoists

            if (prodType.equals("GL")) {
                if (TrueTransactMain.BRANCH_ID.equals(CommonUtil.convertObjToStr(TrueTransactMain.selBranch))) {
                    //                observable.setCbmProdId(prodType);
                    //                cboProdId.setModel(observable.getCbmProdId());
                    txtAccNo.setText("");
                    txtPanNo.setText("");
                    btnPanNo.setEnabled(false);
                    btnAccHdId.setEnabled(true);
                    if (!(viewType == EDIT || viewType == DELETE || viewType == AUTHORIZE || viewType == VIEW || viewType == LINK_BATCH || viewType == LINK_BATCH_TD)) {
                        observable.setTxtAccNo("");
                    }
                    setProdEnable(false);
                    cboProdId.setSelectedItem("");
                } else {
                    ClientUtil.displayAlert("InterBranch Transactions Not Allowed For GL");
                    observable.resetForm();
                }
                //txtAccHdId.setEnabled(true);
                txtAccHdId.setEnabled(false);
            } else {
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW
                        || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    productBased();
                }
                setProdEnable(true);
                observable.setCbmProdId(prodType);
                cboProdId.setModel(observable.getCbmProdId());
//                observable.getCbmProdId().setKeyForSelected(observable.getCbmProdId().getDataForKey(observable.getCboProdId()));
                txtAccHdId.setEnabled(false);
                btnAccNo.setEnabled(false);
            }
            btnViewTermLoanDetails.setEnabled(true);

        }
        if (viewType == AUTHORIZE || viewType == LINK_BATCH || viewType == LINK_BATCH_TD) {
            cboProdId.setEnabled(false);
            txtAccNo.setEnabled(false);
            btnAccNo.setEnabled(false);
        }
    }//GEN-LAST:event_cboProdTypeActionPerformed

    private void setProdEnable(boolean isEnable) {
        cboProdId.setEnabled(isEnable);
        txtAccNo.setEnabled(isEnable);
        btnAccNo.setEnabled(isEnable);

        btnAccHdId.setEnabled(!isEnable);

        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            setEditFieldsEnable(false);
        }
        //        btnPhoto.setEnabled(isEnable);
    }

	private void tdtInstrumentDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtInstrumentDateFocusLost
            // Add your handling code here:
            //ClientUtil.validateLTDate(tdtInstrumentDate);
    }//GEN-LAST:event_tdtInstrumentDateFocusLost

	private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
            // Add your handling code here:
            observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
            authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed

	private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
            // Add your handling code here:
            if (termDepositUI != null) {
                if (!termDepositUI.getRenewalTransMap().equals("")) {
                    if (txtAccNo.getText().length() > 0) {
                        termDepositUI.getRenewalTransMap().put("DEPOSIT_AMT_CASH", "DEPOSIT_CASH_REJECTED");
                    } else {
                        termDepositUI.getRenewalTransMap().put("INTEREST_AMT_CASH", "INTEREST_CASH_REJECTED");
                    }
                }
            }
            observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
            authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed

	private void rdoTransactionType_CreditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTransactionType_CreditActionPerformed
            // Add your handling code here:
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
//                if (observable.getProdType().equals("TD") || observable.getProdType().equals("TL")) {
//                    txtAccNo.setText("");
//                }
            }
           if (rdoTransactionType_Credit.isSelected()) {
                String status = "";
                btnOrgOrResp.setText("O");
                String prodId = ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString();
                HashMap where = new HashMap();
                where.put("PRODUCT_ID", prodId);
                List lst = ClientUtil.executeQuery("getOpAcctProductTOByProdId", where);//OperativeAcctProductTO
                if (lst != null && lst.size() > 0) {
                    OperativeAcctProductTO map = (OperativeAcctProductTO) lst.get(0);
                    status = map.getSRemarks();
                }

                if (status.equals("NRE")) {
                    ClientUtil.displayAlert("Credit Transactions Not Allowed For This Product!!!");
                    rdoTransactionType_Debit.setSelected(true);
                }
                txtTokenNo.setText("");


                tdtInstrumentDate.setDateValue("");

                cboInstrumentType.setEnabled(false);
                cboInstrumentType.setSelectedIndex(0);
                txtInstrumentNo1.setEnabled(false);

                txtInstrumentNo2.setEnabled(false);
                tdtInstrumentDate.setEnabled(false);
                txtParticulars.setText("BY CASH");

            }
            String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
            if (!prodType.equals("") && prodType.equals("GL")) {
                HashMap acctMap = new HashMap();
                acctMap.put("ACCT_HEAD", observable.getTxtAccHd());
                List head = ClientUtil.executeQuery("getSelectReconcileYesNO", acctMap);
                if (head != null && head.size() > 0) {
                    acctMap = (HashMap) head.get(0);
                    if (transCommonUI != null) {
                        transCommonUI.dispose();
                        transCommonUI = null;
                    }
                    rdoTransactionType_Debit.setSelected(false);
                    if (!acctMap.get("RECONS").equals("") && acctMap.get("RECONS").equals("Y")) {
                        if (acctMap.get("BALANCETYPE").equals("DEBIT") && rdoTransactionType_Credit.isSelected() == true) {
                            reconcilebtnDisable = true;
                            observable.setReconcile("N");
                            btnCurrencyInfo.setVisible(true);
                            btnCurrencyInfo.setEnabled(true);
                        } else if (acctMap.get("BALANCETYPE").equals("CREDIT") && rdoTransactionType_Debit.isSelected() == true) {
                            reconcilebtnDisable = true;
                            observable.setReconcile("N");
                            btnCurrencyInfo.setVisible(true);
                            btnCurrencyInfo.setEnabled(true);
                        } else if (acctMap.get("BALANCETYPE").equals("DEBIT") && rdoTransactionType_Debit.isSelected() == true) {
                            observable.setReconcile("Y");
                            btnCurrencyInfo.setVisible(false);
                            btnCurrencyInfo.setEnabled(false);
                        } else if (acctMap.get("BALANCETYPE").equals("CREDIT") && rdoTransactionType_Credit.isSelected() == true) {
                            observable.setReconcile("Y");
                            btnCurrencyInfo.setVisible(false);
                            btnCurrencyInfo.setEnabled(false);
                        }
                        observable.setBalanceType(CommonUtil.convertObjToStr(acctMap.get("BALANCETYPE")));
                    } else {
                        btnCurrencyInfo.setVisible(false);
                        btnCurrencyInfo.setEnabled(false);
                    }
                }
            } else {
                btnCurrencyInfo.setVisible(false);
                btnCurrencyInfo.setEnabled(false);
            }
            //                    rdoTransactionType_DebitActionPerformed(evt);

    }//GEN-LAST:event_rdoTransactionType_CreditActionPerformed
    public boolean depositAuthorizationValidation() {
        boolean auth = false;
        if (this.observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                && rdoTransactionType_Credit.isSelected() == true && observable.getProdType() != null && observable.getProdType().equals("TD")) {
            HashMap depAuthMap = new HashMap();
            String AcNo = "";
            if (observable.getTxtAccNo().lastIndexOf("_") != -1) {
                AcNo = observable.getTxtAccNo().substring(0, observable.getTxtAccNo().lastIndexOf("_"));
            }
            depAuthMap.put("DEPOSIT_NO", AcNo);
            List lst = ClientUtil.executeQuery("getSelectRemainingBalance", depAuthMap);
            if (lst != null && lst.size() > 0) {
                depAuthMap = (HashMap) lst.get(0);
                double depositAmt = CommonUtil.convertObjToDouble(depAuthMap.get("DEPOSIT_AMT")).doubleValue();
                double totalBalance = CommonUtil.convertObjToDouble(depAuthMap.get("TOTAL_BALANCE")).doubleValue();
                double remainingAmt = depositAmt - totalBalance;
                if (remainingAmt < CommonUtil.convertObjToDouble(observable.getTxtAmount()).doubleValue()) {
                    if (remainingAmt > 0) {
                        ClientUtil.showAlertWindow("Can not authorize, amount exceeding the deposit amount"
                                + "\n Deposit Amount is :" + depositAmt
                                + "\n Balance Amount to be collected :" + remainingAmt);
                    } else {
                        ClientUtil.showAlertWindow("Can not authorize, amount exceeding the deposit amount"
                                + "\n Deposit Amount is :" + depositAmt
                                + "Please Reject the Transaction ");
                    }
                    auth = true;
                }
            }
        }
        return auth;
    }
	private void rdoTransactionType_DebitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTransactionType_DebitActionPerformed
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
              
//                if (observable.getProdType().equals("TD") || observable.getProdType().equals("TL")) {
//                    txtAccNo.setText("");
//                }
                //                String token="";
                //                List list1;
                //                boolean tkn= false;
                if (rdoTransactionType_Debit.isSelected()) {
                    btnOrgOrResp.setText("R");
                    if (custStatus.equals("Y") && txtAccNo.getText().length() > 0) {
                        ClientUtil.displayAlert("MINOR ACCOUNT");
                    }
//                     if (depBehavesLike.equals("DAILY")) {
//                      ClientUtil.displayAlert("Debit Transactions Not Allowed For This Product!!!");
//                        rdoTransactionType_Credit.setSelected(true);
//                     return;
//                     }
                    //                        txtTokenNo.setEditable(false);
                    //                        txtTokenNo.setEnabled(false);
                    //                        HashMap tokenIssue = new HashMap();
                    //                        tokenIssue.put("CURRENT_DT", currDt.clone());
                    //                        tokenIssue.put("BRANCH_ID",ProxyParameters.BRANCH_ID);
                    //                        tokenIssue.put("RECEIVED_BY", TrueTransactMain.USER_ID);
                    //                        //system.out.println("rdotransaction tokenissue"+tokenIssue);
                    //                        List lst = ClientUtil.executeQuery("getStartTokenNo", tokenIssue);
                    //                        if (lst.size()>0) {
                    //                            for(int i=0;i<lst.size();i++) {
                    //                                if(tkn==true) {
                    //                                    break;
                    //                                }
                    //                                HashMap tokenIssue1 = new HashMap();
                    //                                tokenIssue1 =(HashMap)lst.get(i);
                    //                                tokenIssue1.put("CURRENT_DT", currDt.clone());
                    //                                tokenIssue1.put("BRANCH_ID",ProxyParameters.BRANCH_ID);
                    //                                tokenIssue1.put("RECEIVED_BY", TrueTransactMain.USER_ID);
                    //                                if(tokenIssue1.get("SERIES_NO")==null) {
                    //                                    list1 = ClientUtil.executeQuery("getMaxNoBasedOnTokenNoForPapeToken", tokenIssue1);
                    //                                }
                    //                                else {
                    //                                    list1 = ClientUtil.executeQuery("getMaxNoBasedOnTokenNo", tokenIssue1);
                    //                                }
                    //                                HashMap map = new HashMap();
                    //                                if(list1!=null && list1.size()>0) {
                    //
                    //                                    map=(HashMap)list1.get(0);
                    //                                    if (CommonUtil.convertObjToInt(map.get("TOKEN_NO"))>=(CommonUtil.convertObjToInt(tokenIssue1.get("TOKEN_END_NO")))) {
                    //                                        continue;
                    //                                    }
                    //                                    else {
                    //                                        token=CommonUtil.convertObjToStr(map.get("SERIES_NO"));
                    //                                        int t= CommonUtil.convertObjToInt(map.get("TOKEN_NO"));
                    //                                        long tk=t+1;
                    //                                        int a=0;
                    //                                        int b=0;
                    //                                        while(a==b) {
                    //                                            HashMap where = new HashMap();
                    //                                            where.put("SERIES_NO",token);
                    //                                            where.put("TOKEN_NO",new Long(tk));
                    //                                            where.put("BRANCH_ID",ProxyParameters.BRANCH_ID);
                    //                                            where.put("RECEIVED_BY", TrueTransactMain.USER_ID);
                    //                                            //system.out.println("@@where"+where);
                    //                                            List lists = ClientUtil.executeQuery("chechForTokenLoss", where);
                    //                                            HashMap where1 = new HashMap();
                    //                                            where1=(HashMap)lists.get(0);
                    //                                            a= CommonUtil.convertObjToInt(where1.get("CNT"));
                    //                                            if(a==b) {
                    //                                                token+=tk;
                    //                                                a=1;
                    //                                                tkn=true;
                    //                                                break;
                    //                                            }
                    //                                            else{
                    //                                                tk=tk+1;
                    //                                                int ten=(CommonUtil.convertObjToInt(tokenIssue1.get("TOKEN_END_NO")));
                    //                                                if(tk>ten) {
                    //                                                    ClientUtil.displayAlert("No tokens are available.");
                    //                                                    btnCancelActionPerformed(null);
                    //                                                    return;
                    //                                                }
                    //                                                a=0;
                    //                                            }
                    //                                        }
                    //                                    }
                    //                                }
                    //                                else{
                    //                                    token=CommonUtil.convertObjToStr(tokenIssue1.get("SERIES_NO"));
                    //                                    int t= CommonUtil.convertObjToInt(tokenIssue1.get("TOKEN_START_NO"));
                    //                                    int a=0;
                    //                                    int b=0;
                    //                                    while(a==b) {
                    //                                        HashMap where = new HashMap();
                    //                                        where.put("SERIES_NO",token);
                    //                                        where.put("TOKEN_NO",new Long(t));
                    //                                        where.put("BRANCH_ID",ProxyParameters.BRANCH_ID);
                    //                                        where.put("RECEIVED_BY", TrueTransactMain.USER_ID);
                    //                                        //system.out.println("@@where"+where);
                    //                                        List lists = ClientUtil.executeQuery("chechForTokenLoss", where);
                    //                                        HashMap where1 = new HashMap();
                    //                                        where1=(HashMap)lists.get(0);
                    //                                        a= CommonUtil.convertObjToInt(where1.get("CNT"));
                    //                                        if(a==b) {
                    //                                            token+=t;
                    //                                            a=1;
                    //                                            tkn=true;
                    //                                            break;
                    //                                        }
                    //                                        else{
                    //                                            t=t+1;
                    //                                            int ten=(CommonUtil.convertObjToInt(tokenIssue1.get("TOKEN_END_NO")));
                    //                                            if(t>ten) {
                    //                                                ClientUtil.displayAlert("No tokens are available.");
                    //                                                btnCancelActionPerformed(null);
                    //                                                return;
                    //                                            }
                    //                                            a=0;
                    //                                        }
                    //                                    }
                    //
                    //                                }
                    //
                    //                            }
                    //
                    //
                    //                        }
                    //
                    //
                    //                        else {
                    //                            ClientUtil.displayAlert("No tokens are available.");
                    //                            btnCancelActionPerformed(null);
                    //                            return;
                    //                        }
                    //                        if(tkn==false) {
                    //                            ClientUtil.displayAlert("No tokens are available.");
                    //                            btnCancelActionPerformed(null);
                    //                            return;
                    //                        }
                    //                    txtTokenNo.setText(token);
                    cboInstrumentType.setEnabled(true);
//                    txtInstrumentNo1.setEditable(true);
                    txtInstrumentNo1.setEnabled(true);
//                    txtInstrumentNo2.setEditable(true);
                    txtInstrumentNo2.setEnabled(true);
                    tdtInstrumentDate.setEnabled(true);
                    txtAmount.setEnabled(true);
                    txtPanNo.setText("");
                    btnPanNo.setEnabled(false);
                    if (observable.getProdType().equals("TD")) {
                        txtParticulars.setText("");
                        txtAmount.setText("");
                        if (depBehavesLike.equals("DAILY") && depPartialWithDrawalAllowed.equals("Y")) {
                            txtParticulars.setEnabled(true);
                        } else {
                            txtParticulars.setEnabled(false);
                        }
                    } else {
                        txtParticulars.setText("To Cash ");
                        txtParticulars.setEnabled(true);
//                        txtParticulars.setEditable(true);
                    }
                    if (designation.equals("Teller") && observable.getProdType().equals("OA")) {
//                    txtTokenNo.setEditable(false);
                        txtTokenNo.setEnabled(false);
                    } else {
//                        txtTokenNo.setEditable(true);
                        txtTokenNo.setEnabled(true);
                    }
                }
            }
            String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
            //system.out.println("prodTypeprodType in test1" + prodType);
            if (!prodType.equals("") && prodType.equals("GL")) {
                HashMap acctMap = new HashMap();
                acctMap.put("ACCT_HEAD", observable.getTxtAccHd());
                List head = ClientUtil.executeQuery("getSelectReconcileYesNO", acctMap);
                if (head != null && head.size() > 0) {
                    acctMap = (HashMap) head.get(0);
                    if (transCommonUI != null) {
                        transCommonUI.dispose();
                        transCommonUI = null;
                    }
                    rdoTransactionType_Credit.setSelected(false);
                    if (!acctMap.get("RECONS").equals("") && acctMap.get("RECONS").equals("Y")) {
                        if (acctMap.get("BALANCETYPE").equals("DEBIT") && rdoTransactionType_Credit.isSelected() == true) {
                            reconcilebtnDisable = true;
                            observable.setReconcile("N");
                            btnCurrencyInfo.setVisible(true);
                            btnCurrencyInfo.setEnabled(true);
                        } else if (acctMap.get("BALANCETYPE").equals("CREDIT") && rdoTransactionType_Debit.isSelected() == true) {
                            reconcilebtnDisable = true;
                            observable.setReconcile("N");
                            btnCurrencyInfo.setVisible(true);
                            btnCurrencyInfo.setEnabled(true);
                        } else if (acctMap.get("BALANCETYPE").equals("DEBIT") && rdoTransactionType_Debit.isSelected() == true) {
                            observable.setReconcile("Y");
                            btnCurrencyInfo.setVisible(false);
                            btnCurrencyInfo.setEnabled(false);
                        } else if (acctMap.get("BALANCETYPE").equals("CREDIT") && rdoTransactionType_Credit.isSelected() == true) {
                            observable.setReconcile("Y");
                            btnCurrencyInfo.setVisible(false);
                            btnCurrencyInfo.setEnabled(false);
                        }
                        observable.setBalanceType(CommonUtil.convertObjToStr(acctMap.get("BALANCETYPE")));
                    } else {
                        btnCurrencyInfo.setVisible(false);
                        btnCurrencyInfo.setEnabled(false);
                    }
                }
            } else {
                btnCurrencyInfo.setVisible(false);
                btnCurrencyInfo.setEnabled(false);
            }
            if(observable.getProdType().equals("OA")){
               cboInstrumentType.setSelectedItem(observable.getCbmInstrumentType().getDataForKey("WITHDRAW_SLIP"));
            }
           else if(observable.getProdType().equals("GL")){
               cboInstrumentType.setSelectedItem(observable.getCbmInstrumentType().getDataForKey("VOUCHER"));
            }
            lblServiceTaxval.setText("0.00");
            serviceTax_Map = new HashMap();
            observable.setLblServiceTaxval("0.0");
            observable.setServiceTax_Map(null);
    }//GEN-LAST:event_rdoTransactionType_DebitActionPerformed

    private void populateInstrumentType() {

        ComboBoxModel objModel = new ComboBoxModel();
        objModel.addKeyAndElement("", "");
        String prodType = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdType.getModel()).getKeyForSelected());
        if (!prodType.equals("")) {
            if (prodType.equals("OA") || prodType.equals("SA") || prodType.equals("AD") || prodType.equals("AAD") || prodType.equals("TD") || prodType.equals("AB")) {
                objModel.addKeyAndElement("WITHDRAW_SLIP", observable.getCbmInstrumentType().getDataForKey("WITHDRAW_SLIP"));
                objModel.addKeyAndElement("CHEQUE", observable.getCbmInstrumentType().getDataForKey("CHEQUE"));
                objModel.addKeyAndElement("VOUCHER", observable.getCbmInstrumentType().getDataForKey("VOUCHER"));
            } else if (prodType.equals("TL") || prodType.equals("GL") || prodType.equals("ATL")) {
                objModel.addKeyAndElement("VOUCHER", observable.getCbmInstrumentType().getDataForKey("VOUCHER"));
            }
            cboInstrumentType.setModel(objModel);
            //system.out.println("#$#$#$^% Instrument type : " + observable.getCboInstrumentType());
            cboInstrumentType.setSelectedItem(CommonUtil.convertObjToStr(observable.getCboInstrumentType()));

        }
    }

    public void authorize(HashMap map) {

        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {

//                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);commented by bala 10-aug-2010
            if (observable.isHoAccount()) {
                ArrayList alist = setOrgOrRespDetails();
                if (alist != null && alist.size() > 0) {
                    observable.setOrgRespList(alist);
                }
            }
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            map.put("CORP_LOAN_MAP", transDetails.getCorpDetailMap());  // For Corporate Loan purpose added by Rajesh
            map.put("CASHIER_AUTH_ALLOWED", TrueTransactMain.CASHIER_AUTH_ALLOWED);//added by babu 
            map.put("ACTUAL_INTEREST", actInterest);
            //System.out.println("map IN AUTHORIZATION ===== " + map +" MMMM--->"+actInterest );
           
            observable.setAuthorizeMap(map);
            observable.doAction();
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                super.setOpenForEditBy(observable.getStatusBy());
                super.removeEditLock(lblTransactionIDDesc.getText());
                 if (fromNewAuthorizeUI) {
                    newauthorizeListUI.removeSelectedRow();
                    this.dispose();
                    newauthorizeListUI.setFocusToTable();
                    newauthorizeListUI.displayDetails("Cash Transactions");
                }
                if (fromAuthorizeUI) {
                    authorizeListUI.removeSelectedRow();
                    this.dispose();
                    authorizeListUI.setFocusToTable();
                    //authorizeListUI.displayDetails("Cash Transactions");
                    if(cashNode.equals("Cash Receipt")){
                        authorizeListUI.displayDetails("Cash Receipt");
                    }else{
                        authorizeListUI.displayDetails("Cash Payment");
                    }
                }
                if (fromCashierAuthorizeUI) {
                    CashierauthorizeListUI.removeSelectedRow();
                    this.dispose();
                    CashierauthorizeListUI.setFocusToTable();
                   //  CashierauthorizeListUI.displayDetails();
                }
                if (fromManagerAuthorizeUI) {
                    ManagerauthorizeListUI.removeSelectedRow();
                    this.dispose();
                    ManagerauthorizeListUI.setFocusToTable();
                   // ManagerauthorizeListUI.displayDetails("Cash Transactions");
                }
//            removeEditLock();
            btnCancelActionPerformed(null);
            observable.setResultStatus();
        }
    }
}
//    private void removeEditLock() {
//        //added by rishad 21/07/2015 for locking and removing
//        String acctNo = CommonUtil.convertObjToStr(txtAccNo.getText());
////        if (acctNo.contains("_")) {
////            acctNo = CommonUtil.convertObjToStr(acctNo.substring(0, acctNo.indexOf("_")));
////        }
//        HashMap lockMap = new HashMap();
//        lockMap.put("RECORD_KEY", acctNo);
//        lockMap.put("CUR_DATE", currDt.clone());
//        lockMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
//        ClientUtil.execute("deleteLock", lockMap);
//    }
    
//     private void removeEditLockWithAcct(String acctNo) {
//         
//        //added by rishad 09/09/2015 for locking and removing
//        HashMap lockMap = new HashMap();
//        lockMap.put("RECORD_KEY", acctNo);
//        lockMap.put("CUR_DATE", currDt.clone());
//        lockMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
//        ClientUtil.execute("deleteLock", lockMap);
//    }
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                //added by sreekrishnan 3/3/2016 for avoiding doubling issue
                CommonUtil comm = new CommonUtil();
                final JDialog loading = comm.addProgressBar();
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                    @Override
                    protected Void doInBackground() throws InterruptedException /** Execute some operation */
                    {
                        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
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
        
        if (termDepositUI != null) {
            if (!termDepositUI.getRenewalTransMap().equals("")) {
                if (txtAccNo.getText().length() > 0) {
                    termDepositUI.getRenewalTransMap().put("DEPOSIT_AMT_CASH", "DEPOSIT_CASH_AUTHORIZED");
                } else {
                    termDepositUI.getRenewalTransMap().put("INTEREST_AMT_CASH", "INTEREST_CASH_AUTHORIZED");
                }
            }
        }
        transDetails.setCashAuthorise("auth");
    }//GEN-LAST:event_btnAuthorizeActionPerformed

    public void authorizeStatus(String authorizeStatus) {
        //        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        //        observable.setStatus();
        //System.out.println("inside authorize status>>>> Nidhin");
        if ((viewType == AUTHORIZE && isFilled) || viewType == LINK_BATCH || viewType == LINK_BATCH_TD) {
            //            observable.setResult(ClientConstants.ACTIONTYPE_AUTHORIZE);
//            HashMap transMap = new HashMap();
//            transMap.put("TRANS_ID", lblTransactionIDDesc.getText());
//            List lst = ClientUtil.executeQuery("getTranAuthStatus", transMap);
//            if(lst!=null && lst.size()>0){
//                transMap = (HashMap)lst.get(0);
//                if(transMap.get("AUTHORIZE_STATUS")!=null)
//                    if(transMap.get("AUTHORIZE_STATUS").equals("AUTHORIZED")||transMap.get("AUTHORIZE_STATUS").equals("REJECTED")){
//                    ClientUtil.showMessageWindow("This Transaction has been already "+transMap.get("AUTHORIZE_STATUS")
//                    +" "+transMap.get("AUTHORIZE_BY"));        
//                    btnCancelActionPerformed(null);
//                    return;
//                }
//            }

            if (observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
                int n = ClientUtil.confirmationAlert("Are you sure want to Reject", 1);
                if (n != 0) {
                    return;
                }
            }
            boolean auth = depositAuthorizationValidation();
            if (auth == true) {
                btnCancelActionPerformed(null);
            }
            String screenName = "";
            String singleTransId = "";
            List lts = null;
            HashMap singleAuthorizeMap = new HashMap();
            ArrayList arrList = new ArrayList();
            HashMap authDataMap = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("TRANS_ID", lblTransactionIDDesc.getText());
            whereMap.put("TRANS_DT", currDt.clone());
            whereMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
            whereMap.put("USER_ID", ProxyParameters.USER_ID);
            List lsts = ClientUtil.executeQuery("getCashSingleTransId", whereMap);//Deposit_Interest_Application
            if(lsts != null && lsts.size() > 0){
            whereMap = (HashMap)lsts.get(0);
            }
            if(whereMap != null && whereMap.size() > 0){
	            singleTransId = CommonUtil.convertObjToStr(whereMap.get("SINGLE_TRANS_ID"));
	            screenName = CommonUtil.convertObjToStr(whereMap.get("SCREEN_NAME"));
	            whereMap.put("SINGLE_TRANS_ID",singleTransId);
	            whereMap.put("TRANS_DT", currDt.clone());
                    whereMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
	            lts = ClientUtil.executeQuery("getCashSingleTransLists", whereMap);
            }
//            System.out.println("LLLLLLLL"+lts);
            //System.out.println("scrrn name  : "+screenName);
            if((TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("N")) && 
                    screenName != null && !screenName.equals("") && screenName.startsWith("CU") && !authorizeStatus.equals("REJECTED")){
            if(lts != null && lts.size() > 0){
                //System.out.println("Here.......");
                String remarks = "";
            //want to edit
            if (flag != true) {
                if (authorizeStatus.equals("REJECTED")) {
                    remarks = COptionPane.showInputDialog(this, resourceBundle.getString("REMARK_TITLE_REJE"));
                } else if (authorizeStatus.equals("AUTHORIZED")) {
                    remarks = COptionPane.showInputDialog(this, resourceBundle.getString("REMARK_TITLE_AUTH"));
                } else if (authorizeStatus.equals("EXCEPTION")) {
                    remarks = COptionPane.showInputDialog(this, resourceBundle.getString("REMARK_TITLE_EXCE"));
                }
            }
            for (int j = 0; j < lts.size(); j++) {
            authDataMap = new HashMap();
            if(lts != null && lts.size() > 0){
            whereMap = (HashMap) lts.get(j);
            }
            //System.out.println("ddddddddddddddddd"+CommonUtil.convertObjToStr(whereMap.get("LINK_BATCH_ID")).replaceAll("_1", ""));
            authDataMap.put("ACCOUNT NO", CommonUtil.convertObjToStr(whereMap.get("LINK_BATCH_ID")).replaceAll("_1", ""));

            if (cboProdId.isEnabled()) {
                authDataMap.put("PRODUCT ID", CommonUtil.convertObjToStr(((ComboBoxModel) cboProdId.getModel()).getKeyForSelected()));
            } else {
                authDataMap.put("PRODUCT ID", "");
            }
            //            if(observable.getLinkBathList()!=null && observable.getLinkBathList().size()>0){
            //                for(int i=0;i<observable.getLinkBathList().size();i++){
            //                    HashMap authTransMap=(HashMap)observable.getLinkBathList().get(i);
            //                    authDataMap.put("TRANS_ID", authTransMap.get("TRANS_ID"));
            //                    authDataMap.put("REMARKS", remarks);
            //                    arrList.add(authDataMap);
            //                    authDataMap=new HashMap();
            //                }
            //            }else {
            authDataMap.put("TRANS_ID", CommonUtil.convertObjToStr(whereMap.get("TRANS_ID")));
            authDataMap.put("USER_ID", ProxyParameters.USER_ID);
            authDataMap.put("TRANS_DT",currDt.clone());
            authDataMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
            List lst = ClientUtil.executeQuery("selectauthorizationLock", authDataMap);
            if (lst != null && lst.size() > 0) {
                HashMap map = new HashMap();
                StringBuffer open = new StringBuffer();
                for (int i = 0; i < lst.size(); i++) {
                    map = (HashMap) lst.get(i);
                    open.append("\n" + "User Id  :" + " ");
                    open.append(CommonUtil.convertObjToStr(map.get("OPEN_BY")) + "\n");
                    open.append("Mode Of Operation  :" + " ");
                    open.append(CommonUtil.convertObjToStr(map.get("MODE_OF_OPERATION")) + " ");
                }
                ClientUtil.showMessageWindow("already open by" + open);
                return;
            }
            authDataMap.put("REMARKS", remarks);
            arrList.add(authDataMap);
            }
            }}else{
                System.out.println("else parts.......>");
            String remarks = "";
            //want to edit
            if (flag != true) {
                if (authorizeStatus.equals("REJECTED")) {
                    remarks = COptionPane.showInputDialog(this, resourceBundle.getString("REMARK_TITLE_REJE"));
                } else if (authorizeStatus.equals("AUTHORIZED")) {
                    remarks = COptionPane.showInputDialog(this, resourceBundle.getString("REMARK_TITLE_AUTH"));
                } else if (authorizeStatus.equals("EXCEPTION")) {
                    remarks = COptionPane.showInputDialog(this, resourceBundle.getString("REMARK_TITLE_EXCE"));
                }
            }

            authDataMap.put("ACCOUNT NO", txtAccNo.getText());
            String prodType = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdType.getModel()).getKeyForSelected());
            if((TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) && 
            rdoTransactionType_Debit.isSelected() && prodType != null && !prodType.equals("") && prodType.length()>0 && prodType.equals("TD")){
                singleAuthorizeMap.put("DAILY","DAILY");
            }
            if (cboProdId.isEnabled()) {
                authDataMap.put("PRODUCT ID", CommonUtil.convertObjToStr(((ComboBoxModel) cboProdId.getModel()).getKeyForSelected()));
            } else {
                authDataMap.put("PRODUCT ID", "");
            }
            //            if(observable.getLinkBathList()!=null && observable.getLinkBathList().size()>0){
            //                for(int i=0;i<observable.getLinkBathList().size();i++){
            //                    HashMap authTransMap=(HashMap)observable.getLinkBathList().get(i);
            //                    authDataMap.put("TRANS_ID", authTransMap.get("TRANS_ID"));
            //                    authDataMap.put("REMARKS", remarks);
            //                    arrList.add(authDataMap);
            //                    authDataMap=new HashMap();
            //                }
            //            }else {
            authDataMap.put("TRANS_ID", lblTransactionIDDesc.getText());
            authDataMap.put("USER_ID", ProxyParameters.USER_ID);
            authDataMap.put("TRANS_DT", currDt.clone());
            authDataMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
            List lst = ClientUtil.executeQuery("selectauthorizationLock", authDataMap);
            if (lst != null && lst.size() > 0) {
                HashMap map = new HashMap();
                StringBuffer open = new StringBuffer();
                for (int i = 0; i < lst.size(); i++) {
                    map = (HashMap) lst.get(i);
                    open.append("\n" + "User Id  :" + " ");
                    open.append(CommonUtil.convertObjToStr(map.get("OPEN_BY")) + "\n");
                    open.append("Mode Of Operation  :" + " ");
                    open.append(CommonUtil.convertObjToStr(map.get("MODE_OF_OPERATION")) + " ");
                }
                ClientUtil.showMessageWindow("already open by" + open);
                return;
            }
            authDataMap.put("REMARKS", remarks);
            arrList.add(authDataMap);
            }
            //            }
            singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            // Added by nithya for 4743 SBOD on 07-09-2016
            HashMap inputMap = new HashMap();
            inputMap.put("ACCOUNTNO", txtAccNo.getText());
            List todAccInfoList = ClientUtil.executeQuery("getMinBalance", inputMap);
            HashMap minMap = new HashMap();
            //System.out.println("todAccInfoList :: " + todAccInfoList);
            if (todAccInfoList != null && todAccInfoList.size() > 0) {
                minMap = (HashMap) todAccInfoList.get(0);
                if (minMap.containsKey("TEMP_OD_ALLOWED") && CommonUtil.convertObjToStr(minMap.get("TEMP_OD_ALLOWED")).equalsIgnoreCase("Y")) {
                    if(CommonUtil.convertObjToDouble(minMap.get("TOD_LIMIT")) >= 0){
                      singleAuthorizeMap.put("SB_OD_ALLOWED","SB_OD_ALLOWED");
                    }  
                }
            }
            // End
            if (observable.getLinkMap() != null && observable.getLinkMap().containsKey("AS_CUSTOMER_COMES") && observable.getLinkMap().get("AS_CUSTOMER_COMES").equals("Y")) {
                singleAuthorizeMap.put("DAILY", "DAILY");
            }
            //shadow value clearing issue added buy rishad 16/jan/2020 Issue In Rejection Case(same scenario wll happen authorize also)--Above Block Code Is not working Some case Of Loan (Tried To reproduce Failed
            //in case Of TermLoan Mostly Come As customer Y(later issue raising other than loan below portion of code change to all except AD(in Case Of Ad As customer will vary)
            String prodType = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdType.getModel()).getKeyForSelected());
            if (prodType != null && !prodType.equals("") && prodType.length() > 0 && prodType.equals("TL")) {
                singleAuthorizeMap.put("DAILY", "DAILY");
            }

            if (isDepositIntBulk) {
                singleAuthorizeMap.put("DAILY", "DAILY");
            }
         
            authorize(singleAuthorizeMap);

            //             if (observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED)
            //            lblStatus.setText(authorizeStatus);
            //             else
            isDepositIntBulk = false;
        } else {
            HashMap mapParam = new HashMap();
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                mapParam.put(CommonConstants.MAP_NAME, "getSelectCashTransactionCashierAuthorizeTOList");
            } else {
                mapParam.put(CommonConstants.MAP_NAME, "getSelectCashTransactionAuthorizeTOList");
            }

            HashMap whereParam = new HashMap();
            whereParam.put("USER_ID", ProxyParameters.USER_ID);
            whereParam.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            whereParam.put("DB_DRIVER_NAME", ProxyParameters.dbDriverName);
            whereParam.put("CASHIER_AUTH_ALLOWED", TrueTransactMain.CASHIER_AUTH_ALLOWED);
            whereParam.put("TRANS_DT", currDt.clone());
            mapParam.put(CommonConstants.MAP_WHERE, whereParam);
            //system.out.println("mapparam###" + mapParam);
            viewType = AUTHORIZE;
            isFilled = false;
            AuthorizeWFUI authorizeUI = new AuthorizeWFUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            btnDelete.setEnabled(false);
            //            lblStatus.setText(observable.getLblStatus());
            observable.setStatus();

        }
    }

    private void setRadioButtons() {
        this.removeRadioButtons();
        this.rdoTransactionType_Credit.setEnabled(true);
        this.rdoTransactionType_Debit.setEnabled(true);
        this.rdoTransactionType_Debit.setSelected(false);
        this.rdoTransactionType_Credit.setSelected(false);
        this.rdoTransactionType_DebitActionPerformed(null);
        if (observable.getCr_cash().equalsIgnoreCase("Y")
                && observable.getDr_cash().equalsIgnoreCase("Y")) {
            this.addRadioButtons();
            return;
        } else if (observable.getCr_cash().equalsIgnoreCase("Y")) {
            this.rdoTransactionType_Credit.setSelected(true);
            this.rdoTransactionType_CreditActionPerformed(null);
            this.rdoTransactionType_Debit.setEnabled(false);
        } else if (observable.getDr_cash().equalsIgnoreCase("Y")) {
            this.rdoTransactionType_Credit.setSelected(false);
            this.rdoTransactionType_Debit.setSelected(true);
            this.rdoTransactionType_DebitActionPerformed(null);
            this.rdoTransactionType_Credit.setEnabled(false);            
        }

        this.addRadioButtons();
    }

    private void cboProdIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdIdActionPerformed
        // Add your handling code here:
        //To Set the Value of Account Head in UI...
        if (cboProdId.getSelectedIndex() > 0) {
            clearProdFields();
            String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
            //Added BY Suresh
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && !prodType.equals("AB")) {
                txtAccNo.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID));
            }
            btnAccNo.setEnabled(true);
            observable.setCboProdId((String) cboProdId.getSelectedItem());
            if (observable.getCboProdId().length() > 0) {

                //When the selected Product Id is not empty string
                observable.setAccountHead();
                txtAccHdId.setText(observable.getTxtAccHd());
                lblAccHdDesc.setText(observable.getLblAccHdDesc());
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW
                        || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    this.setRadioButtons();
                    productBased();
                }
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && !(prodType.equals("GL") || prodType.equals("AB"))) {
                    rdoTransactionType_Credit.setSelected(true);
                    String prodId = "";
                    prodId = ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString();
                    txtAccNo.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID) + prodId);
                }
            }
            if (!TrueTransactMain.BRANCH_ID.equals(CommonUtil.convertObjToStr(TrueTransactMain.selBranch))) {
                HashMap InterBranMap = new HashMap();
                InterBranMap.put("AC_HD_ID", observable.getTxtAccHd());
                List lst = ClientUtil.executeQuery("AcHdInterbranchAllowedOrNot", InterBranMap);
                InterBranMap = null;
                if (lst != null && lst.size() > 0) {
                    InterBranMap = (HashMap) lst.get(0);
                    String IbAllowed = CommonUtil.convertObjToStr(InterBranMap.get("INTER_BRANCH_ALLOWED"));
                    if (IbAllowed.equals("N")) {
                        ClientUtil.displayAlert("InterBranch Transactions Not Allowed For This AC_HD");
                        observable.resetForm();
                        clearProdFields();
                    }
                }
            }
            //                ADDED HERE BY NIKHIL FOR PARTIAL WITHDRAWAL
            String prodId = "";
            String prodIdDesc = "";
            prodId = ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString();
            prodIdDesc = ((ComboBoxModel) cboProdId.getModel()).getSelectedItem().toString();
            transDetails.setProdId(prodIdDesc);
            HashMap prodMap = new HashMap();
            prodMap.put("PROD_ID", prodId.toString());
            List behavesLiklst = ClientUtil.executeQuery("getBehavesLikeForDeposit", prodMap);
            //system.out.println("######## BEHAVES :" + behavesLiklst);
            if (behavesLiklst != null && behavesLiklst.size() > 0) {
                prodMap = (HashMap) behavesLiklst.get(0);
                depBehavesLike = CommonUtil.convertObjToStr(prodMap.get("BEHAVES_LIKE"));
                depPartialWithDrawalAllowed = CommonUtil.convertObjToStr(prodMap.get("PARTIAL_WITHDRAWAL_ALLOWED"));
                //system.out.println("$#%#$%#$%behavesLike:" + depBehavesLike);
                if (depBehavesLike.equals("RECURRING")) {
                    btnViewTermLoanDetails.setVisible(true);
                } else {
                    btnViewTermLoanDetails.setVisible(false);
                }
            } else {
                depBehavesLike = "";
                depPartialWithDrawalAllowed = "";
                btnViewTermLoanDetails.setVisible(true);
            }
            //Added By Suresh
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                rdoTransactionType_Credit.setSelected(true);
                rdoTransactionType_CreditActionPerformed(null);
            }
        }
    }//GEN-LAST:event_cboProdIdActionPerformed

    void checkLoanTransaction(HashMap hash) {
        String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
        if (prodType.equals("TL")) {
            String actnum = "";
            if (observable.getTxtAccNo().lastIndexOf("_") != -1) {
                actnum = observable.getTxtAccNo().substring(0, observable.getTxtAccNo().lastIndexOf("_"));
            } else {
                actnum = observable.getTxtAccNo();
            }
            HashMap map = new HashMap();
            map.put("ACCOUNTNO", new String(actnum));
            List lst = ClientUtil.executeQuery("getDisbursementAmtDetailsTL", map);
            double disburseAmt = 0;
            if (lst.size() > 0) {

                map = (HashMap) lst.get(0);
                disburseAmt = CommonUtil.convertObjToDouble(map.get("AMOUNT")).doubleValue();
            } else {
            }
            String availBalance = transDetails.getAvBalance();  //getAvailableBalance();change by abi
            double availableBalnce = Double.parseDouble(availBalance.replaceAll(",", ""));
            String clearBalance = transDetails.getCBalance();
            double clearBal = Double.parseDouble(clearBalance);
            clearBal = -1 * clearBal;
            String limitAmt = observable.getLimitAmount();
            double amtLimit = Double.parseDouble(limitAmt.replaceAll(",", ""));
            String sDebit = transDetails.getShadowDebit();
            //double debit_shadowAmt=Double.parseDouble(sDebit);
            String multiDisburse = transDetails.getIsMultiDisburse();
            double debit_shadowamt = Double.parseDouble(sDebit.replaceAll(",", ""));
            double shadow = debit_shadowamt;
            debit_shadowamt += clearBal + availableBalnce;
            //system.out.println("###clearbalance" + clearBal + "###limitamount" + amtLimit + "shadowdebit" + debit_shadowamt + "     :" + multiDisburse);
            //            if(shadow==availableBalnce)
            //            if( shadow==availableBalnce|| amtLimit ==debit_shadowamt && multiDisburse.equals("N")){
            if (disburseAmt >= amtLimit) {
                //                //system.out.println("if condition inside");
                //                rdoTransactionType_Debit.setSelected(false);
                //                rdoTransactionType_Credit.setSelected(true);
                //                rdoTransactionType_Debit.setEnabled(false);
                //                rdoTransactionType_Credit.setEnabled(false);
            }
        }
    }

    private void productBased() {
        String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
        if (prodType.equals("TD")) {
            this.rdoTransactionType_Debit.setEnabled(true);
            this.rdoTransactionType_Credit.setSelected(false);
            this.txtAmount.setEnabled(true);
//            this.txtAmount.setEditable(true);
        } else {
            //            this.txtAmount.setEnabled(false);
//            this.txtAmount.setEditable(true);
        }

        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            setEditFieldsEnable(false);
        }
    }

	private void btnAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccNoActionPerformed
            // Add your handling code here:
            returnWaiveMap = null;
            noofInstallment2 = 0;
            popUp(ACCNO);
            termLoanDetailsFlag = false;
            termLoansDetailsMap = null;

    }//GEN-LAST:event_btnAccNoActionPerformed

	private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
            // Add your handling code here:
            cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed

	private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
            ////system.out.println("Came in Print Button Click");
            HashMap reportParamMap = new HashMap();
            //System.out.println("Screen ID in UI "+getScreenID());
            com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
            //                HashMap map = new HashMap();
            //                map.put("ACT_NUM",com.see.truetransact.clientutil.ttrintegration.TTIntegration.getActNum());
            //                //system.out.println("&&&AC_NUM"+map);
            //                ClientUtil.executeQuery("updatingPassBookFlag",map);
    }//GEN-LAST:event_btnPrintActionPerformed

	private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
            // Add your handling code here:
            lockAccount=null;
            deletescreenLock();
         //   System.out.println("lock is......"+observable.isIsTransaction());
//            if (observable.isIsTransaction() == false || viewType == AUTHORIZE) {
//                removeEditLock();
//            }
            noofInstallment2 = 0;
            asAndWhenMap = null;
            lblHouseName.setText("");
            lblAccName.setText("");
            cboProdId.setSelectedItem(null);
            transDetails.setTransDetails(null, null, null);
            transDetails.setSourceFrame(null);
            transDetails.setProductId("");//Added by kannan
            transDetails.setRepayData(null);
            //		super.removeEditLock(lblTransactionIDDesc.getText());
            setEditFieldsEnable(true);
            observable.resetLable();                // Reset the Editable Lables in the UI to null...
            setSelectedBranchID(ProxyParameters.BRANCH_ID);
            observable.resetForm();                 // Reset the fields in the UI to null...
            ClientUtil.enableDisable(this, false);  // Disables the panel...
            enableDisableButtons(false);
            setModified(false);
            observable.setInstalType(null);
            if (termDepositUI != null) {
                btnCloseActionPerformed(null);
                if (afterSaveCancel == false) {
                    termDepositUI.disableSaveButton();
                }
            }
            //                        disableButtons();                       // To Disable the folder buttons in the UI...
            setButtonEnableDisable();               // Enables or Disables the buttons and menu Items depending on their previous state...
            if (observable.getActionType() != ClientConstants.ACTIONTYPE_AUTHORIZE) {
                observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);//Sets the Action Type to be performed...
            }
            observable.setCbmProdId("");
            cboProdId.setModel(observable.getCbmProdId());
            observable.setStatus();                 // To set the Value of lblStatus...
            viewType = -1;
            btnDelete.setEnabled(false);
            if (transCommonUI != null) {
                transCommonUI.dispose();
            }
            transCommonUI = null;
            custStatus = "";
//             btnViewTermLoanDetails.setVisible(false);
//             btnViewTermLoanDetails.setEnabled(false);
            termLoansDetailsMap = null;
            termLoanDetails = null;
            termLoanDetailsFlag = false;
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
            lblHouseName.setText("");
            btnViewTermLoanDetails.setEnabled(false);
            btnWaive.setEnabled(false);
            btnOrgOrResp.setVisible(false);
            setOrgBranch("");
            setOrgBranchName("");
            setOrgOrRespAdviceDt(null);
            setOrgOrRespAdviceNo("");
            setOrgOrRespAmout(0.0);
            setOrgOrRespBranchId("");
            setOrgOrRespBranchName("");
            setOrgOrRespCategory("");
            setOrgOrRespDetails("");
            setOrgOrRespTransType("");
            btnCust.setEnabled(false);

//            ArrayList key = new ArrayList();
//            ArrayList value = new ArrayList();
//            key.add("");
//            value.add("");
//            observable.setCbmProdType(new ComboBoxModel(key,value));  
//            lblAccNoGl.setText("");
            lblAccNoGl.setText("");
            lblHouseName.setText("");
            observable.setAccNumGl("");
            observable.setAccNumGlProdType(""); //Added y nithya on 17-Jul-2025 for KD-4108
            accNo1 = "";
            isDepositIntBulk = false;
            btnWaive.setEnabled(false);
            transDetails.setChangeInterest("");
            transDetails.setRepayData(null);
            serviceTax_Map=null;
            linkBatchId="";
            single_transId="";
            returnWaiveMap=new HashMap();
            allWaiveMap=new HashMap();   
            lblLoanStatus.setText("");            
	}//GEN-LAST:event_btnCancelActionPerformed

	private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
            
            // Add your handling code here:
             //added by sreekrishnan for kcc renewalz
//             txtAccNoActionPerformed();
            
                        
            if (observable.getProdType() != null && (observable.getProdType().equals("TL") || observable.getProdType().equals("AD"))) {
                //Suspicious Account Check
                String transactionType = "";
                if (rdoTransactionType_Credit.isSelected()) {
                    transactionType = "CREDIT";
                } else {
                    transactionType = "DEBIT";
                }
                String suspiciousAccountWarning = SuspiciousAccountValidation.checkForSuspiciousActivity(CommonUtil.convertObjToStr(txtAccNo.getText()), transactionType,"CASH");
                if (suspiciousAccountWarning.length() > 0) {
                    ClientUtil.showAlertWindow(suspiciousAccountWarning);
                    //btnCancelActionPerformed(null);
                    return;
                }
                // End
            }
            
            //added by rishad
            if (observable.getProdType() != null && observable.getProdType().equals("TL")) {
            	boolean flag = isAccountNumberExsistInAuthList(CommonUtil.convertObjToStr(txtAccNo.getText()));
            	if(flag){
                	return;
            	}
            }
             if (observable.getProdType().equals("TL")) {
                    if (rdoTransactionType_Credit.isSelected() && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                        if (!observable.getInstalType().isEmpty()&&!observable.getInstalType().equals("")&&!observable.getInstalType().equals(null)&&observable.getInstalType().equals("EMI") && observable.getEMIinSimpleInterest().equalsIgnoreCase("N")) {
                            double totalEMIAmt = setEMIAmount();
                            observable.setTxtAmount(String.valueOf(totalEMIAmt)); // Added by nithya on 30-04-2021 for  KD-2801
                            txtAmount.setText(observable.getTxtAmount());  
                            //system.out.println("hvbhjvdbb555");
                            if(txtAmount.getText().length() > 0){
                               totalEMIAmt = setEMILoanWaiveAmount();
                            }
                            observable.setTxtAmount(String.valueOf(totalEMIAmt));
                            txtAmount.setText(observable.getTxtAmount());                            
                        }
                    }
             }
             //end 
            double penalInt = CommonUtil.convertObjToDouble(asAndWhenMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
            String prodTye = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdType.getModel()).getKeyForSelected());
            //added by sreekrishnan for 0003866
             if(rdoTransactionType_Debit.isSelected() && prodTye.equals("OA")){
                 HashMap debitMap = new HashMap();
                 debitMap.put("ACT_NUM", CommonUtil.convertObjToStr(txtAccNo.getText()));
                 List debitlist = ClientUtil.executeQuery("getDailyMaxTransaction", debitMap);
                 debitMap = null;
                 Date debitDate = new Date();
                 if (debitlist != null && debitlist.size() > 0) {
                    debitMap = (HashMap) debitlist.get(0);
                    if(debitMap!=null && debitMap.size()>0){
                        debitDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(debitMap.get("FINAL_DT")));
                        //System.out.println("debitDate###"+debitDate);
                        if(!debitDate.equals("")){
                            if (debitDate.after((Date) currDt.clone()) || debitDate.equals((Date) currDt.clone())) {
                                observable.setDebitWithdrwalCharge(true);
                            }else{
                                observable.setDebitWithdrwalCharge(false);
                            }
                        }else{
                             observable.setDebitWithdrwalCharge(false);
                        }
                    }else{
                         observable.setDebitWithdrwalCharge(false);
                    }
                }else{
                      observable.setDebitWithdrwalCharge(false);
                }
             }
             //System.out.println("$#@$#@$@#$#@$#@$@3"+observable.isDebitWithdrwalCharge());
             
            if (CommonUtil.convertObjToDouble(penalInt) <= 0 && rdoTransactionType_Credit.isSelected() && (prodTye.equals("TL") || prodTye.equals("AD"))) {
                //  row = new ArrayList();
                HashMap hmap = new HashMap();
                String acNo = CommonUtil.convertObjToStr(txtAccNo.getText());
                hmap.put("ACCT_NUM", acNo);
                List list = ClientUtil.executeQuery("getRebateAllowedForActnum", hmap);
                hmap = null;
                if (list != null && list.size() > 0) {
                    hmap = (HashMap) list.get(0);
                    String rebateCalculation = CommonUtil.convertObjToStr(hmap.get("REBATE_CALCULATION"));
                    String rebateAllowed = CommonUtil.convertObjToStr(hmap.get("REBATE_ALLOWED"));
                    String rebatePercentage = CommonUtil.convertObjToStr(hmap.get("REBATE_PERCENTAGE"));
                    double intper = CommonUtil.convertObjToDouble(hmap.get("INTEREST"));
                    String spl_Rebat = CommonUtil.convertObjToStr(hmap.get("INT_RATE_REBATE"));
                    double loanIntpercentage = CommonUtil.convertObjToDouble(hmap.get("LOAN_INT_PERCENT")); // Added by nithya on 09-01-2020 for KD-1234
                    String Y = "Y";
                    if (rebateAllowed.equals(Y) && !observable.isRebateInterest()) {
                        if (rebateCalculation != null && rebateCalculation.equalsIgnoreCase("Monthly calculation") && !spl_Rebat.equals("Y")) {
                            //System.out.println("bbbbb");
                            double rAmt = CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")) * intper / 100;
                            rAmt = (double) getNearest((long) (rAmt * 100), 100) / 100;
                            if (rAmt > 0) {
                                int result = ClientUtil.confirmationAlert("Do you Want to  give Rebate Interest");
                                if (result == 0) {
                                    observable.setRebateInterest(true);
                                } else {
                                    observable.setRebateInterest(false);
                                }
                            }
                        } else if (rebateCalculation != null && rebateCalculation.equalsIgnoreCase("Monthly calculation") && spl_Rebat.equals("Y")) {
                            if (asAndWhenMap.containsKey("ROI") && asAndWhenMap.get("ROI") != null) {
                                String intRt = CurrencyValidation.formatCrore(String.valueOf(asAndWhenMap.get("ROI")));
                                //double rAmt = CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")) / CommonUtil.convertObjToDouble(intRt);
                                double rAmt = (CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")) * loanIntpercentage) / CommonUtil.convertObjToDouble(intRt); // Added by nithya on 09-01-2020 for KD-1234
                                rAmt = (double) getNearest((long) (rAmt * 100), 100) / 100;
                                if (rAmt > 0) {
                                    int result = ClientUtil.confirmationAlert("Do you Want to  give Rebate Interest");
                                    if (result == 0) {
                                        observable.setRebateInterest(true);
                                    } else {
                                        observable.setRebateInterest(false);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            String prodTe = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdType.getModel()).getKeyForSelected());
            if (rdoTransactionType_Debit.isSelected() && prodTe.equals("SA")) {
                HashMap wheMap = new HashMap();
                wheMap.put("ACCT_NUM", txtAccNo.getText());
                List lt1 = ClientUtil.executeQuery("getNegativeAmtCheckForSA", wheMap);
                if (lt1 != null && lt1.size() > 0) {
                    HashMap tMap = (HashMap) lt1.get(0);
                    String negYn = CommonUtil.convertObjToStr(tMap.get("NEG_AMT_YN"));
                    double clAmt = CommonUtil.convertObjToDouble(tMap.get("CLEAR_BALANCE"));
                    double totAmt =CommonUtil.convertObjToDouble( txtAmount.getText());
                    if (!negYn.equals("Y") && totAmt > clAmt) {
                        int result = ClientUtil.confirmationAlert("The account has _ve/Zero  balance.Do you Want to  continue?");
                        if (result == 0) {
                        } else {
                            return;
                        }
                    }
                }
            }
            // Added By Rishad 12/11/2019 for OtherBank Withdrawal
            if (rdoTransactionType_Credit.isSelected() && prodTe.equals("AB")) {
                double clAmt = CommonUtil.convertObjToDouble(transDetails.getCBalance());
                double totAmt = CommonUtil.convertObjToDouble(txtAmount.getText());
                if (totAmt > clAmt) {
                    int result = ClientUtil.confirmationAlert ("Withdrawal Amount Exeeds Clear Balance.Do you Want to  continue?");
                    if (result == 0) {
                    } else {
                        return;
                    }
                }
            }
            
            if(CommonUtil.convertObjToStr(cboProdType.getSelectedItem()) != null && !CommonUtil.convertObjToStr(cboProdType).equals("")){
                String prodType = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdType.getModel()).getKeyForSelected());
                String acctNo = CommonUtil.convertObjToStr(txtAccNo.getText());
                String prodId = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
                if(prodType!=null && !prodType.equals("GL")){
                    HashMap hMap = new HashMap();
                    hMap.put("PROD_ID", prodId);
                    if(prodType != null && !prodType.equals("") &&  prodType.equals("TD")){
                        if (acctNo.contains("_")) {
                            String tempActNum = CommonUtil.convertObjToStr(acctNo.substring(0, acctNo.indexOf("_")));
                            hMap.put("ACT_NUM", tempActNum);
                        } else {
                            hMap.put("ACT_NUM", acctNo);
                        }
                    }else{
                        hMap.put("ACT_NUM", acctNo);
                    }
                    hMap.put("SELECTED_BRANCH", (((ComboBoxModel) TrueTransactMain.cboBranchList.getModel()).getKeyForSelected()));
                    List kccList1 = ClientUtil.executeQuery("Cash.getAccountList"+prodType, hMap);
                    if(kccList1==null || kccList1.isEmpty()){
                        ClientUtil.showAlertWindow("Invalid Account N/O!!!!");
                        return;
                    }
                }
            }
             //System.out.println("isValid flag="+isValid);
             if(isValid){
                 isValid=false;
                 return;
             }
            if(observable.getKccNature().equals("Y")){
                if(rdoTransactionType_Debit.isSelected()){
                    //System.out.println("kccFlag####"+observable.getKccNature());
                    //String ACCOUNTNO = CommonUtil.convertObjToStr(hash.get("ACCOUNTNO"));
                    String ACCOUNTNO = (String)txtAccNo.getText();
                    //System.out.println("ACCOUNTNO####PutXTblic"+txtAccNo.getText());
                    //(txtAccNo.getText());
                    //System.out.println("HASH kccRenewalChecking ======" + ACCOUNTNO);
                    HashMap hmap = new HashMap();
                    Date toDate = new Date();
                    hmap.put("ACCOUNTNO",ACCOUNTNO);        
                    List kccList1 = ClientUtil.executeQuery("getKccSacntionTodate", hmap);
                    if (kccList1 != null && kccList1.size() > 0) {
                        hmap = (HashMap) kccList1.get(0);
                    	toDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hmap.get("TO_DT")));
                		System.out.println("toDate###"+toDate);
                		if(!toDate.equals("")){
                    		if (toDate.before((Date) currDt.clone())) {
                        		ClientUtil.showMessageWindow("Account not Renewed!Can't make Payments!!");
                            	return;
                    		}
                		}
					}
                }
            }
            txtAmountFocusLost(null);
            String Acno = txtAccNo.getText();
            HashMap WhereMap= new HashMap();
            WhereMap.put("ACNO", Acno);
            List lst1 = ClientUtil.executeQuery("getDeathDetailsForCashAndTransfer",WhereMap);
            if (lst1 != null && lst1.size() > 0 ){
                int yesNo = 0;
                String[] options = {"Yes", "No"};
                yesNo = COptionPane.showOptionDialog(null, "The cutomer is death marked , Do you want to continue?", CommonConstants.WARNINGTITLE,
                        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                        null, options, options[0]);
                if (yesNo != 0) {
                    btnCancelActionPerformed(null);
                    return;
                }
            }
            String prodTypes = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
            String prodId = "";
            //System.out.println("prodTypes#########" + prodTypes);
            observable.setProdCash(prodTypes);
            //   //system.out.println("multttiply111>>>>"+CommonUtil.convertObjToLong(txtAmount.getText())*noofInstallment2);
            //  //system.out.println("hvbhjvdbb222");
            // double amt2=CommonUtil.convertObjToLong(txtAmount.getText())*noofInstallment2;
            //  observable.setTxtAmount(CommonUtil.convertObjToStr(amt2));
            if (!prodTypes.equals("GL")) {
                prodId = ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString();
            }
            boolean save = true;
            if (prodTypes.equals("TD")) {
                observable.setTxtAmount(txtAmount.getText());
                save = observable.calcRecurringDates(prodId);
            }
            if (prodTypes.equals("TD")) {
                HashMap recurrMap = new HashMap();
                recurrMap.put("PROD_ID", prodId);
                List lst = ClientUtil.executeQuery("getBehavesLikeForDeposit", recurrMap);
                if (lst != null && lst.size() > 0) {
                    HashMap recurringMap = (HashMap) lst.get(0);
                    if (!recurringMap.get("BEHAVES_LIKE").equals("RECURRING")&&!recurringMap.get("BEHAVES_LIKE").equals("BENEVOLENT")&&!recurringMap.get("BEHAVES_LIKE").equals("THRIFT")&& !recurringMap.get("BEHAVES_LIKE").equals("DAILY") && rdoTransactionType_Credit.isSelected()) {
                        HashMap validationMap = new HashMap();
                        validationMap.put("DEPOSIT_NO", txtAccNo.getText());
                        List lstDep = ClientUtil.executeQuery("getDepositAmountForTrans", validationMap);
                        if (lstDep != null && lstDep.size() > 0) {
                            validationMap = (HashMap) lstDep.get(0);
                            double totBal = CommonUtil.convertObjToDouble(validationMap.get("TOTAL_BALANCE")).doubleValue();
                            double depAmt = CommonUtil.convertObjToDouble(validationMap.get("DEPOSIT_AMT")).doubleValue();
                            if (totBal == 0) {
                            } else if (depAmt <= totBal) {
                                ClientUtil.displayAlert("Transaction has been already done...");
                                btnCancelActionPerformed(null);
                                return;
                            }
                        }
                    }
                    //                    if(recurringMap.get("BEHAVES_LIKE").equals("RECURRING") && rdoTransactionType_Credit.isSelected()){
                    //                        HashMap validationMap = new HashMap();
                    //                        validationMap.put("DEPOSIT_NO",txtAccNo.getText());
                    //                        List lstDep = ClientUtil.executeQuery("getDepositAmountForTrans", validationMap);
                    //                        if(lstDep !=null && lstDep.size()>0){
                    //                            validationMap =(HashMap)lstDep.get(0);
                    //                            double depAmt = CommonUtil.convertObjToDouble(validationMap.get("DEPOSIT_AMT")).doubleValue();
                    //                            double penalAmt =  CommonUtil.convertObjToDouble(transDetails.getPenalAmount()).doubleValue();
                    //                            double amount = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                    //                            double finalAmount = 0.0;
                    //                            if(depAmt>penalAmt){
                    //                                finalAmount = amount % depAmt;
                    //                                if(finalAmount == penalAmt ){
                    //                                    observable.setDepositPenalAmt(String.valueOf(penalAmt));
                    //                                    observable.setDepositPenalMonth(transDetails.getPenalMonth());
                    //                                }
                    //                            }else{
                    //                                amount = amount - penalAmt;
                    //                                finalAmount = amount % depAmt;
                    //                                if(finalAmount == 0 && penalAmt>0){
                    //                                    observable.setDepositPenalAmt(String.valueOf(penalAmt));
                    //                                    observable.setDepositPenalMonth(transDetails.getPenalMonth());
                    //                                }
                    //                            }
                    //                        }
                    //                    }
                }
            }
            if (prodTypes.equals("TD")) {
                if (flag == true) {
                    String dep = termDepositUI.getTransSomeAmt();
                    if (dep != null && dep.equals("DEP_INTEREST_AMT")) {
                        observable.setDepInterestAmt("DEP_INTEREST_AMT");
                    }
                }
            }

            if (!prodTypes.equals("GL") && txtAccNo.getText().equals(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID)) && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                ClientUtil.displayAlert("Please Enter Account Number...!!!");
                return;
            }
            if (rdoTransactionType_Credit.isSelected()) {
                int credit_cash = CommonUtil.convertObjToInt(txtAmount.getText());
                //System.out.println("TrueTransactMain.CASH_CREDIT" + TrueTransactMain.CASH_CREDIT);
                if (credit_cash > TrueTransactMain.CASH_CREDIT) {
                    ClientUtil.displayAlert("Amount should not be greater than " + TrueTransactMain.CASH_CREDIT);
                    return;
                }
            } else if (rdoTransactionType_Debit.isSelected()) {
                int debit_cash = CommonUtil.convertObjToInt(txtAmount.getText());
                if (debit_cash > TrueTransactMain.CASH_DEBIT) {
                    ClientUtil.displayAlert("Amount should not be greater than " + TrueTransactMain.CASH_DEBIT);
                    return;
                }
            }

            if (save) {
                if (prodTypes.equals("TL") || prodTypes.equals("AD")) {
                    checkDocumentDetail();
                    if (moreThanLoanAmountAlert()) {
                        return;
                    }
                    //                    if(prodTypes.equals("AD") && rdoTransactionType_Debit.isSelected()){
                    //                         boolean expirydate=checkExpiryDate();
                    //                         if(expirydate)
                    //                             return; //NOW NOT  NEEDED
                    //                    }
                }
                updateOBFields();
                StringBuffer mandatoryMessage = new StringBuffer();
                mandatoryMessage.append(new MandatoryCheck().checkMandatory(getClass().getName(), panCashTransaction));

                //system.out.println("observable.getProdType(" + observable.getProdType());
                if (!CommonUtil.convertObjToStr(observable.getProdType()).equals("GL")
                        && CommonUtil.convertObjToStr(observable.getCboProdId()).length() == 0) {
                    mandatoryMessage.append(objMandatoryRB.getString("cboProdId") + "\n");
                }

//                if (CommonUtil.convertObjToStr(observable.getProdType()).equals("GL") &&//commented by abi
//                CommonUtil.convertObjToStr(observable.getTxtAccHd()).length()== 0) {
//                    mandatoryMessage.append(objMandatoryRB.getString("txtAccHdId") + "\n") ;
//                }

                if (!CommonUtil.convertObjToStr(observable.getProdType()).equals("GL")
                        && CommonUtil.convertObjToStr(observable.getTxtAccNo()).length() == 0) {
                    mandatoryMessage.append(objMandatoryRB.getString("txtAccNo") + "\n");
                }

                if (observable.getRdoTransactionType_Debit() == false && observable.getRdoTransactionType_Credit() == false) {
                    mandatoryMessage.append(objMandatoryRB.getString("rdoTransactionType_Debit") + "\n");
                }
                //Commented By Suresh
//                if (CommonUtil.convertObjToDouble(observable.getTxtAmount()).doubleValue() <= 0){
//                    mandatoryMessage.append(objMandatoryRB.getString("txtAmount") + "\n") ;
//                }
                if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE) {
                    if (rdoTransactionType_Debit.isSelected()) {
//                        if(txtTokenNo.getText().equals("") && !(designation.equals("Teller")))
//                            mandatoryMessage.append(objMandatoryRB.getString("txtTokenNo") + "\n") ;
                        double avbal = 0.0;
                        double shadowDeb = 0.0;
                        double enteredAmt = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                        ArrayList tableVal = transDetails.getTblDataArrayList();
                        //system.out.println("tableVal" + tableVal);
                        for (int k = 0; k < tableVal.size(); k++) {
                            ArrayList balList = ((ArrayList) tableVal.get(k));
                            if (balList.get(0).equals("Available Balance")) {
                                String str = CommonUtil.convertObjToStr(balList.get(1));
                                str = str.replaceAll(",", "");
                                avbal = CommonUtil.convertObjToDouble(str).doubleValue();
                            }
                            if (balList.get(0).equals("Shadow Debit")) {
                                String shawdowStr = CommonUtil.convertObjToStr(balList.get(1));
                                shawdowStr = shawdowStr.replaceAll(",", "");
                                shadowDeb = CommonUtil.convertObjToDouble(shawdowStr).doubleValue();
                            }
                        }
                        HashMap hmap = new HashMap();

                        double chkMinVal = 0.00;
                        if (chqBalList != null && chqBalList.size() > 0) {
                            hmap = (HashMap) chqBalList.get(0);
                            double minbalWChk = CommonUtil.convertObjToDouble(hmap.get("MIN_WITH_CHQ")).doubleValue();

                            double minimumbalanceWtChk = CommonUtil.convertObjToDouble(hmap.get("MIN_WITHOUT_CHQ"));
                            //added by Chithra on 21-10-14(merged)
                            if(hmap.containsKey("TEMP_OD_ALLOWED") && CommonUtil.convertObjToStr(hmap.get("TEMP_OD_ALLOWED")).equalsIgnoreCase("Y") ){
                                //avbal += CommonUtil.convertObjToDouble(hmap.get("TOD_LIMIT")); 
                                //avbal = CommonUtil.convertObjToDouble(hmap.get("AVAILABLE_BALANCE")) + CommonUtil.convertObjToDouble(hmap.get("TOD_LIMIT")); // Added by nithya for 4743 SBOD on 07-09-2016
                                if(CommonUtil.convertObjToDouble(hmap.get("TOD_LIMIT")) > 0 ){
                                    if (rdoTransactionType_Debit.isSelected()) {
                                        String todClearBalance = transDetails.getCBalance();
                                        double todClearBal = Double.parseDouble(todClearBalance);
                                        if(todClearBal - enteredAmt < 0){
                                            HashMap renewalCheckMap = new HashMap();
                                            renewalCheckMap.put("ACT_NUM",txtAccNo.getText());
                                            renewalCheckMap.put("TO_DT",currDt.clone());
                                            List chkLst = ClientUtil.executeQuery("checkODForRenewal",renewalCheckMap);
                                            if(chkLst != null && chkLst.size() > 0){
                                                ClientUtil.displayAlert("OD period over. Please renew");
                                                return;
                                            }
                                        }
                                   } 
                                }                                                               
                            }
                            //End...............
                            String chkbook = CommonUtil.convertObjToStr(hmap.get("CHQ_BOOK"));
                            int count = CommonUtil.convertObjToInt(hmap.get("CNT"));
                            if ((chkbook.equals("Y"))) {
//                                if (enteredAmt == (avbal - shadowDeb)) {
//                                    int a = ClientUtil.confirmationAlert("A/c balance will become Zero, Do you want to continue?");

                                chkMinVal = minbalWChk;
                                if (((avbal - shadowDeb) - enteredAmt) <= minbalWChk) {
                                    int m = 0;
                                    if (((avbal - shadowDeb) - enteredAmt) == minbalWChk) {
                                        m = ClientUtil.confirmationAlert("Only Minimum Balance Remaining, Do you want to continue?");
                                    } else if (((avbal - shadowDeb) - enteredAmt) < minbalWChk) {
                                        m = ClientUtil.confirmationAlert("Minimum Balance not maintained, Do you want to continue?");

                                    }
                                    int n = 0;
                                    if (m != n) {
                                        txtAmount.setText("");
                                        txtAmount.grabFocus();
                                        return;
                                    }
                                }
                            } else {

//                                if (enteredAmt == (avbal - shadowDeb)) {
//                                    int a = ClientUtil.confirmationAlert("A/c balance will become Zero, Do you want to continue?");
                                chkMinVal = minimumbalanceWtChk;
                                if (((avbal - shadowDeb) - enteredAmt) <= minimumbalanceWtChk) {
                                    int m = 0;
                                    if (((avbal - shadowDeb) - enteredAmt) == minimumbalanceWtChk) {
                                        m = ClientUtil.confirmationAlert("Only Minimum Balance Remaining, Do you want to continue?");
                                    } else if (((avbal - shadowDeb) - enteredAmt) < minimumbalanceWtChk) {
                                        m = ClientUtil.confirmationAlert("Minimum Balance not maintained, Do you want to continue?");
                                    }
                                    int n = 0;
                                    if (m != n) {
                                        txtAmount.setText("");
                                        txtAmount.grabFocus();
                                        return;
                                    }

                                }
                            }
                        }
                        String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
                        if (!prodType.equals("GL")) {
                            //system.out.println("prodtypeeeeee" + prodType);
                            if (!prodType.equals("SA") && !prodType.equals("AB")) {
                                if (enteredAmt > (avbal - shadowDeb)) { 
                                    //                            if(avbal==0.0){
                                    ClientUtil.displayAlert("Entered Amount Exceeds The Limit");
                                    txtAmount.setText("");
                                    txtAmount.grabFocus();
                                    return;
                                } else if (((avbal - shadowDeb) - enteredAmt) < chkMinVal) {
                                    ClientUtil.displayAlert("Entered Amount Exceeds The Limit");
                                    txtAmount.setText("");
                                    txtAmount.grabFocus();
                                    return;
                                }
                            }
                        }

                        if (rdoTransactionType_Debit.isSelected() && observable.getBalanceType().equals("DEBIT")) {
                            String acchead = txtAccHdId.getText();
                            double limit = 0.0;
                            double annualLimit = 0.0;
                            double percentage = 0.0;
                            double enteredAmount = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                            HashMap hashmap1 = new HashMap();
                            hashmap1.put("ACHEAD", acchead);
                            hashmap1.put("CUR_DATE", currDt.clone());
                            List lmtList = ClientUtil.executeQuery("getLimitAmt", hashmap1);
                            if (lmtList != null && lmtList.size() > 0) {
                                hashmap1 = (HashMap) lmtList.get(0);
                                limit = CommonUtil.convertObjToDouble(hashmap1.get("LIMIT_AMT")).doubleValue();
                                annualLimit = CommonUtil.convertObjToDouble(hashmap1.get("ANNUAL_LIMIT_AMT")).doubleValue();
                                percentage = CommonUtil.convertObjToDouble(hashmap1.get("OVER_DRAW_PER")).doubleValue();
                                annualLimit = annualLimit + (annualLimit * percentage / 100);
                                if (enteredAmount + avbal > annualLimit) {
                                    int c = ClientUtil.confirmationAlert("Entered Amount Exceeds The AnnualLimit,Do u want to continue?");
                                    int d = 0;
                                    if (c != d) {
                                        return;
                                    }
                                }
                                if (enteredAmt > limit) {
                                    int c = ClientUtil.confirmationAlert("Entered Amount Exceeds The Limit amount,Do u want to continue?");
                                    int d = 0;
                                    if (c != d) {
                                        return;
                                    }
                                }
                            }

                        }
                    }
                    if (rdoTransactionType_Credit.isSelected() && observable.getBalanceType().equals("CREDIT")) {
                        String acchead = txtAccHdId.getText();
                        double limit = 0.0;
                        double annualLimit = 0.0;
                        double avbal = 0.0;
                        double shadowDeb = 0.0;
                        double percentage = 0.0;
                        double enteredAmount = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                        HashMap hashmap1 = new HashMap();
                        hashmap1.put("ACHEAD", acchead);
                        hashmap1.put("CUR_DATE", currDt.clone());
                        ArrayList tableVal = transDetails.getTblDataArrayList();
                        //system.out.println("tableVal" + tableVal);
                        for (int k = 0; k < tableVal.size(); k++) {
                            ArrayList balList = ((ArrayList) tableVal.get(k));
                            if (balList.get(0).equals("Available Balance")) {
                                String str = CommonUtil.convertObjToStr(balList.get(1));
                                str = str.replaceAll(",", "");
                                avbal = CommonUtil.convertObjToDouble(str).doubleValue();
                            }
                            if (balList.get(0).equals("Shadow Debit")) {
                                String shawdowStr = CommonUtil.convertObjToStr(balList.get(1));
                                shawdowStr = shawdowStr.replaceAll(",", "");
                                shadowDeb = CommonUtil.convertObjToDouble(shawdowStr).doubleValue();
                            }
                        }
                        List lmtList = ClientUtil.executeQuery("getLimitAmt", hashmap1);
                        if (lmtList != null && lmtList.size() > 0) {
                            hashmap1 = (HashMap) lmtList.get(0);
                            limit = CommonUtil.convertObjToDouble(hashmap1.get("LIMIT_AMT")).doubleValue();
                            annualLimit = CommonUtil.convertObjToDouble(hashmap1.get("ANNUAL_LIMIT_AMT")).doubleValue();
                            percentage = CommonUtil.convertObjToDouble(hashmap1.get("OVER_DRAW_PER")).doubleValue();
                            annualLimit = annualLimit + (annualLimit * percentage / 100);
                            if (enteredAmount + avbal > annualLimit) {
                                int c = ClientUtil.confirmationAlert("Entered Amount Exceeds The AnnualLimit,Do u want to continue?");
                                int d = 0;
                                if (c != d) {
                                    return;
                                }
                            }
                            if (enteredAmount > limit) {
                                int c = ClientUtil.confirmationAlert("Entered Amount Exceeds The Limit amount,Do u want to continue?");
                                int d = 0;
                                if (c != d) {
                                    return;
                                }
                            }
                        }

                    }

                }
                if (!prodTypes.equals("GL")) {
                    if (rdoTransactionType_Credit.isSelected() && CommonUtil.convertObjToDouble(observable.getTxtAmount()).doubleValue() >= TrueTransactMain.PANAMT
                            && txtPanNo.getText().equals("")) {
                        //                        mandatoryMessage.append(objMandatoryRB.getString("txtPanNo") + "\n") ;
                        int a = ClientUtil.confirmationAlert("PAN Number Not Entered.. Do You Want To Continue Without Entering?");
                        int b = 0;
                        if (b != a) {
                            return;
                        }
                    }
                }
                if (observable.isHoAccount()) {
                    ArrayList alist = setOrgOrRespDetails();

                    observable.setOrgRespList(alist);
                    if (rdoTransactionType_Credit.isSelected()) {
                        if (getOrgOrRespBranchId().equals("") || getOrgOrRespDetails().equals("")) {
                            ClientUtil.displayAlert("Enter Orginating Details");
                            return;
                        }
                    } else if (rdoTransactionType_Debit.isSelected()) {
                        if (getOrgBranch().equals("") || getOrgOrRespAdviceNo().equals("") || getOrgOrRespAdviceDt().equals("")) {
                            ClientUtil.displayAlert("Enter Responding Details");
                            return;
                        }
                    }
                }
                if (!TrueTransactMain.BRANCH_ID.equals(CommonUtil.convertObjToStr(TrueTransactMain.selBranch))) {
                    HashMap InterBranMap = new HashMap();
                    InterBranMap.put("AC_HD_ID", observable.getTxtAccHd());
                    List lst = ClientUtil.executeQuery("AcHdInterbranchAllowedOrNot", InterBranMap);
                    InterBranMap = null;
                    if (lst != null && lst.size() > 0) {
                        InterBranMap = (HashMap) lst.get(0);
                        String IbAllowed = CommonUtil.convertObjToStr(InterBranMap.get("INTER_BRANCH_ALLOWED"));
                        if (IbAllowed.equals("N")) {
                            ClientUtil.displayAlert("InterBranch Transactions Not Allowed For This AC_HD");
                            return;
                        }
                    }
                }
                if (prodTypes.equals("OA") && rdoTransactionType_Debit.isSelected() == true) {
                    double transAmt = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                    observable.setCreatingFlexi("");
                    observable.setFlexiAmount(0.0);
                    HashMap flexiMap = new HashMap();
                    flexiMap.put("ACCOUNTNO", txtAccNo.getText());
                    List getList = ClientUtil.executeQuery("getFlexiDetails", flexiMap);
                    if (getList != null && getList.size() > 0) {
                        //system.out.println("getList : " + getList);
                        flexiMap = (HashMap) getList.get(0);
                        double lienAmt = 0.0;
                        double alreadyTransAmt = 0.0;
                        double depAvaialAmt = CommonUtil.convertObjToDouble(flexiMap.get("DEPOSIT_BALANCE")).doubleValue();
                        double depositAmt = CommonUtil.convertObjToDouble(flexiMap.get("DEPOSIT_AMT")).doubleValue();
                        double availBal = CommonUtil.convertObjToDouble(flexiMap.get("AVAILABLE_BALANCE")).doubleValue();
                        double totalBal = CommonUtil.convertObjToDouble(flexiMap.get("AVAILABLE_BALANCE")).doubleValue();
                        double minBal2 = CommonUtil.convertObjToDouble(flexiMap.get("MIN_BAL2_FLEXI")).doubleValue();
                        alreadyTransAmt = CommonUtil.convertObjToDouble(flexiMap.get("SHADOW_DEBIT")).doubleValue();
                        HashMap depositProdMap = new HashMap();
                        double amtMultiples = 0.0;
                        depositProdMap.put("PROD_ID", flexiMap.get("PROD_ID"));
                        List lstProd = ClientUtil.executeQuery("getSchemeIntroDate", depositProdMap);
                        if (lstProd != null && lstProd.size() > 0) {
                            depositProdMap = (HashMap) lstProd.get(0);
                            amtMultiples = CommonUtil.convertObjToDouble(depositProdMap.get("WITHDRAWAL_MULTI")).doubleValue();
                        }
                        HashMap lienMap = new HashMap();
                        lienMap.put("LIEN_AC_NO", txtAccNo.getText());
                        lienMap.put("LIEN_DT", observable.getCurrentDate());
                        List lst = ClientUtil.executeQuery("getDetailsForSBLienActDetails", lienMap);
                        if (lst != null && lst.size() > 0) {
                            lienMap = (HashMap) lst.get(0);
                            HashMap sumOfDepMap = new HashMap();
                            sumOfDepMap.put("FLEXI_ACT_NUM", txtAccNo.getText());
                            List lstSum = ClientUtil.executeQuery("getSelectSumOfDepAmount", sumOfDepMap);
                            if (lstSum != null && lstSum.size() > 0) {
                                sumOfDepMap = (HashMap) lstSum.get(0);
                                depositAmt = CommonUtil.convertObjToDouble(sumOfDepMap.get("TOTAL_BALANCE")).doubleValue();
                            }
                            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                                lienAmt = CommonUtil.convertObjToDouble(lienMap.get("SUM(LIEN_AMOUNT)")).doubleValue();
                                if (lienAmt > 0) {
                                    if (availBal < transAmt) {
                                        ClientUtil.showAlertWindow("Transaction Amount is Exceeding the DepositAmt");
                                        return;
                                    } else if (availBal == transAmt) {
                                        lienAmt = depAvaialAmt;
                                    } else {
                                        lienAmt = transAmt;
                                    }
                                } else {
                                    if (availBal < transAmt) {
                                        ClientUtil.showAlertWindow("Transaction Amount is Exceeding the DepositAmt");
                                        return;
                                    } else {
                                        if (transAmt == availBal) {
                                            lienAmt = depAvaialAmt;
                                        } else if (alreadyTransAmt == 0 && minBal2 >= transAmt) {
                                            if (minBal2 > (availBal - transAmt)) {
                                                if (depositAmt > transAmt) {
                                                    lienAmt = transAmt;
                                                } else {
                                                    lienAmt = depositAmt;
                                                }
                                            } else {
                                                lienAmt = 0.0;
                                            }
                                        } else if (alreadyTransAmt == 0 && minBal2 < transAmt) {
                                            lienAmt = availBal - transAmt;
                                            lienAmt = depositAmt - lienAmt;
                                        } else if (alreadyTransAmt > 0 && minBal2 >= (transAmt + alreadyTransAmt)) {
                                            lienAmt = 0.0;
                                        } else if (alreadyTransAmt > 0 && minBal2 < (transAmt + alreadyTransAmt)) {
                                            if (availBal == (transAmt + alreadyTransAmt)) {
                                                lienAmt = depositAmt;
                                            } else {
                                                lienAmt = availBal - (transAmt + alreadyTransAmt);
                                                lienAmt = depositAmt - lienAmt;
                                            }
                                        }
                                    }
                                }
                                if (availBal > transAmt) {
                                    double balancelienAmt = lienAmt % amtMultiples;
                                    if (balancelienAmt != 0) {
                                        lienAmt = (lienAmt - balancelienAmt) + amtMultiples;
                                    }
                                    if (depositAmt < lienAmt) {
                                        lienAmt = depAvaialAmt;
                                    }
                                } else {
                                    lienAmt = depAvaialAmt;
                                }
                            }
                            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                                ClientUtil.showAlertWindow("Can not Edit this Transaction Please Reject or Delete, and do it fresh transaction...");
                                btnCancelActionPerformed(null);
                                return;
                            }
                            //                        if(observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT){
                            //                            HashMap cashMap = new HashMap();
                            //                            cashMap.put("TRANS_ID",lblTransactionIDValue.getText());
                            //                            //system.out.println("cashMap "+cashMap);
                            //                            lst = ClientUtil.executeQuery("getSBLienTransferAccountNo",cashMap);
                            //                            if(lst !=null && lst.size()>0){
                            //                                cashMap = (HashMap)lst.get(0);
                            //                                double transactionAmt = CommonUtil.convertObjToDouble(cashMap.get("AMOUNT")).doubleValue();
                            //                                String lienNo = CommonUtil.convertObjToStr(cashMap.get("AUTHORIZE_REMARKS"));
                            //                                cashMap.put("LIEN_AC_NO",txtAccountNo.getText());
                            //                                lst = ClientUtil.executeQuery("getDetailsForEditModeSBLienAct",cashMap);
                            //                                if(lst !=null && lst.size()>0){
                            //                                    for(int i = 0; i<lst.size(); i++){
                            //                                        cashMap = (HashMap)lst.get(i);
                            //                                        lienAmt += CommonUtil.convertObjToDouble(cashMap.get("LIEN_AMOUNT")).doubleValue();
                            //                                    }
                            //                                }
                            //                                if(!lienNo.equals("")){
                            //                                    double balance = availBal - alreadyTransAmt;
                            //                                    if(transAmt>(transactionAmt+balance)){
                            //                                        ClientUtil.showAlertWindow("<html>Maximum Amount you can with draw <b>Rs. "+balance+
                            //                                        "</b><br>Already Shadow debit is <b>Rs. "+alreadyTransAmt+"</b></html>");
                            //                                        return;
                            //                                    }else{
                            //                                        double txtAmt = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                            //                                        transAmt = alreadyTransAmt - lienAmt;
                            //                                        if(alreadyTransAmt>minBal2){
                            //                                            if(txtAmt>transAmt)
                            //                                                lienAmt = txtAmt - transAmt;
                            //                                            else{
                            //                                                cashMap.put("LIEN_NO",lienNo);
                            //                                                lst = ClientUtil.executeQuery("getDetailsForEditModeSingleSBLienAct",cashMap);
                            //                                                if(lst !=null && lst.size()>0){
                            //                                                    cashMap = (HashMap)lst.get(0);
                            //                                                    lienAmt = CommonUtil.convertObjToDouble(cashMap.get("LIEN_AMOUNT")).doubleValue();
                            //                                                }
                            //                                                if(transactionAmt>transAmt){
                            //                                                    if(txtAmt>minBal2)
                            //                                                        lienAmt = transactionAmt - (txtAmt + lienAmt);
                            //                                                    else
                            //                                                        lienAmt = 0.0;
                            //                                                }else
                            //                                                    lienAmt = txtAmt;
                            //                                            }
                            //
                            //                                        }else
                            //                                            lienAmt = 0.0;
                            //                                    }
                            //                                }else{//ok d'nt change anything...Sathiya.
                            //                                    double balance = availBal - alreadyTransAmt;
                            //                                    if(transAmt>balance){
                            //                                        ClientUtil.showAlertWindow("<html>Maximum Amount you can with draw <b>Rs. "+balance+
                            //                                        "</b><br>Already Shadow debit is <b>Rs. "+alreadyTransAmt+"</b></html>");
                            //                                        return;
                            //                                    }else{
                            //                                        transAmt = transAmt - transactionAmt + alreadyTransAmt;
                            //                                        if((transAmt+transactionAmt) == availBal)
                            //                                            lienAmt = depositAmt;
                            //                                        else if(transAmt>minBal2){
                            //                                            lienAmt = availBal - transAmt + lienAmt;
                            //                                            lienAmt = depositAmt - lienAmt;
                            //                                        }else
                            //                                            lienAmt = 0.0;
                            //                                    }
                            //                                }
                            //                            }
                            //                        }
                        }
                        lst = null;
                        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW
                                || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                            if (lienAmt > 0) {
                                String[] obj = {"Ok", "Cancel"};
                                int options = COptionPane.showOptionDialog(null, ("Lien Marked for Rs. : " + lienAmt + " on Flexi Deposit"), ("Flexi Account"),
                                        COptionPane.YES_NO_CANCEL_OPTION, COptionPane.QUESTION_MESSAGE, null, obj, obj[0]);
                                if (options == 0) {
                                    observable.setCreatingFlexi("FLEXI_LIEN_CREATION");
                                    observable.setFlexiAmount(lienAmt);
                                } else {
                                    return;
                                }
                            } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                                observable.setCreatingFlexi("FLEXI_LIEN_DELETION");
                            }
                        }
                    }
                    getList = null;
                }
                //                if (prodTypes.equals("OA") && rdoTransactionType_Credit.isSelected() == true &&
                //                txtAccNo.getText().length()>0 && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
                //                    HashMap lienReducingMap = new HashMap();
                //                    lienReducingMap.put("LIEN_AC_NO",txtAccNo.getText());
                //                    List lst = ClientUtil.executeQuery("getSelectReducingLienAmount", lienReducingMap);
                //                    if(lst!=null && lst.size()>0){
                //                        lienReducingMap = (HashMap)lst.get(0);
                //                        if(!lienReducingMap.get("AUTHORIZE_STATUS").equals("") && lienReducingMap.get("AUTHORIZE_STATUS").equals("AUTHORIZED")){
                //                            double lienAmt = CommonUtil.convertObjToDouble(lienReducingMap.get("LIEN_AMOUNT")).doubleValue();
                //                            observable.setCreatingFlexi("FLEXI_LIEN_REDUCING");
                //                            observable.setFlexiAmount(lienAmt);
                //                        }else{
                //                            ClientUtil.showAlertWindow("Authorize Cash Transaction where Lien is Marked...");
                ////                            return;
                //                        }
                //                    }
                //                }
                if (!prodTypes.equals("") && prodTypes.equals("GL")) {
                    HashMap acctMap = new HashMap();
                    acctMap.put("ACCT_HEAD", observable.getTxtAccHd());
                    List head = ClientUtil.executeQuery("getSelectReconcileYesNO", acctMap);
                    if (head != null && head.size() > 0) {
                        acctMap = (HashMap) head.get(0);
                        if (!acctMap.get("RECONS").equals("") && acctMap.get("RECONS").equals("Y")) {
                            if (acctMap.get("BALANCETYPE").equals("DEBIT") && rdoTransactionType_Credit.isSelected() == true) {
                                observable.setReconcile("N");
                                observable.reconcileMap = new HashMap();
                                double txtAmt = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                                if (transCommonUI == null) {
                                    ClientUtil.showAlertWindow("Reconciliation Screen should be selected...");
                                    return;
                                } else {
                                    double reconcileAmt = transCommonUI.getReconciledAmt();
                                    if (reconcileAmt == txtAmt) {
                                        if (transCommonUI.getReturnMap().size() > 0) {
                                            observable.reconcileMap = transCommonUI.getReturnMap();
                                        }
                                    } else {
                                        ClientUtil.showAlertWindow("Selected amount not equal to transaction amount");
                                        return;
                                    }
                                }
                            } else if (acctMap.get("BALANCETYPE").equals("CREDIT") && rdoTransactionType_Debit.isSelected() == true) {
                                observable.setReconcile("N");
                                observable.reconcileMap = new HashMap();
                                double txtAmt = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                                if (transCommonUI == null) {
                                    ClientUtil.showAlertWindow("Reconciliation Screen should be selected...");
                                    return;
                                } else {
                                    double reconcileAmt = transCommonUI.getReconciledAmt();
                                    if (reconcileAmt == txtAmt) {
                                        if (transCommonUI.getReturnMap().size() > 0) {
                                            observable.reconcileMap = transCommonUI.getReturnMap();
                                        }
                                    } else {
                                        ClientUtil.showAlertWindow("Selected amount not equal to transaction amount");
                                        return;
                                    }
                                }
                            } else if (acctMap.get("BALANCETYPE").equals("DEBIT") && rdoTransactionType_Debit.isSelected() == true) {
                                observable.setReconcile("Y");
                            } else if (acctMap.get("BALANCETYPE").equals("CREDIT") && rdoTransactionType_Credit.isSelected() == true) {
                                observable.setReconcile("Y");
                            }
                            observable.setBalanceType(CommonUtil.convertObjToStr(acctMap.get("BALANCETYPE")));
                        }
                    }
                }
                if (observable.getRdoTransactionType_Debit() == true) {
                    String instrumentType = CommonUtil.convertObjToStr(((ComboBoxModel) cboInstrumentType.getModel()).getKeyForSelected());
                    //System.out.println("instrimrnt type......................." + instrumentType);
                    //system.out.println("observable.getCboInstrumentType():" + observable.getCboInstrumentType());
                    if (instrumentType.length() == 0) {
                        mandatoryMessage.append(objMandatoryRB.getString("cboInstrumentType") + "\n");
                    } else if ((instrumentType.equals("DD") || instrumentType.equals("CHEQUE"))
                            && (CommonUtil.convertObjToStr(observable.getTxtInstrumentNo1()).length() == 0
                            || CommonUtil.convertObjToStr(observable.getTxtInstrumentNo2()).length() == 0)) {
                        mandatoryMessage.append(objMandatoryRB.getString("txtInstrumentNo1") + "\n");
                        return;
                    }

                    if (tdtInstrumentDate.getDateValue().equals("")) {
                        mandatoryMessage.append(objMandatoryRB.getString("tdtInstrumentDate") + "\n");
                        return;
                    }
                    //                                                                                                                                                                                                                                    if (CommonUtil.convertObjToDouble(observable.getTxtAmount()).doubleValue() <= 0){
                    //                                                                                                                                                                                                                                        mandatoryMessage.append(objMandatoryRB.getString("txtAmount") + "\n") ;
                    //                                                                                                                                                                                                                                    }
                }
                //final String mandatoryMessage = new MandatoryDBCheck().checkMandatory(getClass().getName(), panCashTransaction);
                if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0) {
                    displayAlert(mandatoryMessage.toString());
                    return;
                } else {
                    if (flagDeposit == true) {
                        double intAmt = CommonUtil.convertObjToDouble(observable.getTxtAmount()).doubleValue();
                        txtDepositAmount.setText(String.valueOf(CommonUtil.convertObjToDouble(txtDepositAmount.getText()).doubleValue() - intAmt));
                        observable.setSelectedBranchID(ProxyParameters.BRANCH_ID);
                        observable.depositRenewalFlag = true;
                        //system.out.println("transfer UI interestAmount : " + intAmt);
                    }
                    if (flag == true) {
                        double intAmt = CommonUtil.convertObjToDouble(observable.getTxtAmount()).doubleValue();
                        txtDepositAmount.setText(String.valueOf(CommonUtil.convertObjToDouble(txtDepositAmount.getText()).doubleValue() - intAmt));
                        observable.depositRenewalFlag = true;
                        //system.out.println("transfer UI interestAmount : " + intAmt);
                    }
                    if (prodTypes.equals("TD") && rdoTransactionType_Debit.isSelected() && flagDeposit == true) {
                        double totAmt = CommonUtil.convertObjToDouble(transDetails.getDepositAmt()).doubleValue();
                        double depAmt = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                        if (depAmt > totAmt) {
                            ClientUtil.showAlertWindow("Amount is exceeding for that Available Balance ...");
                            txtAmount.setText(String.valueOf(0.0));
                            return;
                        }
                    }

                    // For Corporate Loan purpose added by Rajesh
                    if (prodTypes.equals("TL") && transDetails.getCorpDetailMap() != null && transDetails.getCorpDetailMap().size() > 0) {
                        observable.setCorpLoanMap(transDetails.getCorpDetailMap());
                    }
                    //added by rishad 21/07/2015 for avoiding doubling issue
                    CommonUtil comm = new CommonUtil();
                    final JDialog loading = comm.addProgressBar();
                    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                        @Override
                        protected Void doInBackground() throws InterruptedException /** Execute some operation */
                        {
                            observable.doAction();              // To perform the necessary operation depending on the Action type...
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
                    if (prodTypes.equals("OA") && rdoTransactionType_Debit.isSelected() == true) {
                        HashMap desigMap = new HashMap();
                        if (designation.equals("Teller")) {
                            desigMap.put("DESIGNATION", designation);
                            List list = ClientUtil.executeQuery("getTellerAmount", desigMap);
                            HashMap TellerAmt = new HashMap();
                            TellerAmt = (HashMap) list.get(0);
                            double cashDeAmt = CommonUtil.convertObjToDouble(TellerAmt.get("CASH_DEBIT")).doubleValue();
                            double enteredAmt = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                            if (enteredAmt <= cashDeAmt) {
                                int a = ClientUtil.confirmationAlert("Continue With Authorization?");
                                int b = 0;
                                if (b == a) {
                                    viewType = 3;
                                    isFilled = true;
                                    if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                                        lblTransactionIDDesc.setText(CommonUtil.convertObjToStr(observable.getProxyReturnMap().get("TRANS_ID")));
                                    }
                                    authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
                                }
                            }
                        }
                    }
                    // For Varappetty Voucher & Receipt Printing Purpose added by Rajesh
                    String transType = "";
                    if (rdoTransactionType_Debit.isSelected() == true) {
                        transType = "DEBIT";
                    } else {
                        transType = "CREDIT";
                    }
//                    //addeded by rishad 21/07/2015 for locking issued 
//                    if (observable.getResult() == ClientConstants.ACTIONTYPE_FAILED) {
//                     removeEditLock();
//                    }

                    if (observable.getResult() == ClientConstants.ACTIONTYPE_FAILED) {
                        return;
                    } else {
                        observable.resetForm();
                    }
                    if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                        HashMap lockMap = new HashMap();
                        ArrayList lst = new ArrayList();

                        lst.add("SINGLE_TRANS_ID");
                        lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                        if (observable.getProxyReturnMap() != null) {
                            //System.out.println("#$#$$ proxy : " + observable.getProxyReturnMap());
                            if (observable.getProxyReturnMap().containsKey("SINGLE_TRANS_ID")) {
                                lockMap.put("SINGLE_TRANS_ID", observable.getProxyReturnMap().get("SINGLE_TRANS_ID"));
                            }
                            if (observable.getProxyReturnMap().containsKey("SINGLE_TRANS_WAIVEID")) {
                                lockMap.put("SINGLE_TRANS_WAIVEID", observable.getProxyReturnMap().get("SINGLE_TRANS_WAIVEID"));
                            }
                             if (observable.getProxyReturnMap().containsKey("SINGLE_TRANS_REBATEID")) {
                                lockMap.put("SINGLE_TRANS_REBATEID", observable.getProxyReturnMap().get("SINGLE_TRANS_REBATEID"));
                            }
                        }
                        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                            //lockMap.put("TRANS_ID", lblTransactionIDDesc.getText());
                            //System.out.println("#$#$$editt : " + observable.getProxyReturnMap().get("SINGLE_TRANS_ID"));
                            lockMap.put("SINGLE_TRANS_ID", observable.getProxyReturnMap().get("SINGLE_TRANS_ID"));

                        }
                        //System.out.println("#$#$$ lockMap : " + lockMap);
                        if (lockMap.containsKey("SINGLE_TRANS_ID") && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                            int yesNo = 0;
                            String[] options = {"Yes", "No"};
                            yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                                    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                                    null, options, options[0]);
                            //system.out.println("#$#$$ yesNo : " + yesNo);
                            if (yesNo == 0) {
                                TTIntegration ttIntgration = null;
                                if (lockMap.get("SINGLE_TRANS_ID") == null) {
                                    lockMap.put("TransId", lockMap.get("TRANS_ID"));
                                }
                                lockMap.put("TransId", lockMap.get("SINGLE_TRANS_ID"));
                                lockMap.put("TransDt", observable.getCurrentDate());
                                lockMap.put("BranchId", ProxyParameters.BRANCH_ID);
                                ttIntgration.setParam(lockMap);
                                //                                if (((String)TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase().lastIndexOf("POLPULLY")!=-1) {
                                //                                    ttIntgration.integrationForPrint("ReceiptPayment");
                                //                                } else {
                                if (transType.equals("DEBIT")) {
                                    ttIntgration.integrationForPrint("CashPayment");
                                } else {
                                    ttIntgration.integrationForPrint("CashReceipt");
                                }
                                if (lockMap.get("SINGLE_TRANS_WAIVEID") != null) {
                                    //System.out.println("not enter");
                                    ttIntgration = null;
                                    HashMap printParamMap = new HashMap();
                                    printParamMap.put("TransDt", observable.getCurrentDate());
                                    printParamMap.put("BranchId", ProxyParameters.BRANCH_ID);
                                    printParamMap.put("TransId", lockMap.get("SINGLE_TRANS_WAIVEID"));
                                    ttIntgration.setParam(printParamMap);
                                    ttIntgration.integrationForPrint("ReceiptPayment");
                                }
                                 if (lockMap.get("SINGLE_TRANS_REBATEID") != null) {
                                    //System.out.println("not enter");
                                    ttIntgration = null;
                                    HashMap printParamMap = new HashMap();
                                    printParamMap.put("TransDt", observable.getCurrentDate());
                                    printParamMap.put("BranchId", ProxyParameters.BRANCH_ID);
                                    printParamMap.put("TransId", lockMap.get("SINGLE_TRANS_REBATEID"));
                                    ttIntgration.setParam(printParamMap);
                                    ttIntgration.integrationForPrint("CashPayment");
                                }
                                cboProdId.setSelectedItem(" ");
                            }
                        }
                        setEditLockMap(lockMap);
                        setEditLock();

                        observable.resetForm();             // Reset the fields in the UI to null...
                        observable.resetLable();            // Reset the Editable Lables in the UI to null...
                        transDetails.setTransDetails(null, null, null, null);
                        transDetails.setProductId("");//Added by kannan
                        transDetails.setSourceFrame(null);
                        transDetails.setRepayData(null);
                        setEditFieldsEnable(true);
                        if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE) {
                            enableDisableButtons(false);        // To disable the buttons(folder) in the UI...
                        }
                        ClientUtil.enableDisable(this, false);// Disables the panel...
                        setButtonEnableDisable();           // Enables or Disables the buttons and menu Items depending on their previous state...
                        observable.setResultStatus();// To Reset the Value of lblStatus...
                        enableDisableButtons(false);
                        setModified(false);
                        if (termDepositUI != null) {
                            afterSaveCancel = true;
                            btnCloseActionPerformed(null);
                            if (!termDepositUI.getRenewalTransMap().equals("")) {
                                HashMap renewalMap = termDepositUI.getRenewalTransMap();
                                //system.out.println("transfer UI renewalMap : " + renewalMap);
                                if (renewalMap.containsKey("INT_AMT")) {
                                    termDepositUI.setRenewalTransMap(new HashMap());
                                    termDepositUI.changePeriod();
                                } else if (renewalMap.containsKey("INTEREST_AMT_CASH")) {
                                    termDepositUI.setRenewalTransMap(new HashMap());
                                    termDepositUI.addingSomeAmt();
                                    termDepositUI.setRenewalTransMap(new HashMap());
                                    termDepositUI.getRenewalTransMap().put("DEPOSIT_AMT_CASH", "");
                                } else if (renewalMap.containsKey("DEPOSIT_AMT_CASH")) {
                                    termDepositUI.transactionCalling();
                                }
                            }
                        }
                        //                                    this.disableButtons();
                    }
                    if (observable.getActionType() == ClientConstants.ACTIONTYPE_FAILED) {
                        btnCancelActionPerformed(null);
                    }
                }
                viewType = -1;
                observable.setResultStatus();
            } else {
                btnCancelActionPerformed(null);
            }
            btnDelete.setEnabled(false);
            deletescreenLock();
            if (transCommonUI != null) {
                transCommonUI.dispose();
            }
            TrueTransactMain.populateBranches();
            TrueTransactMain.selBranch = ProxyParameters.BRANCH_ID;
            observable.setSelectedBranchID(ProxyParameters.BRANCH_ID);
            setSelectedBranchID(ProxyParameters.BRANCH_ID);
            transCommonUI = null;
            txtAmountKeyPressed(null);
            lblAccNoGl.setText("");
            clearProdFields();
            cboProdId.setSelectedItem("");
            btnWaive.setEnabled(false);
            lblHouseName.setText("");
            lblAccName.setText("");
            cboProdId.setSelectedIndex(0);
            transDetails.setChangeInterest("");
            transDetails.setRepayData(null);
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                if(fromSmartCustUI){
                    //System.out.println("fromSmartCustUI#%#%"+fromSmartCustUI);
                    this.dispose();
                    fromSmartCustUI = false;
                }
            }
}//GEN-LAST:event_btnSaveActionPerformed
    private boolean checkExpiryDate() {
        java.util.Date expiryDt = DateUtil.getDateMMDDYYYY(transDetails.getExpiryDate());
        if (expiryDt != null) {
            if (DateUtil.dateDiff(DateUtil.getDateWithoutMinitues(observable.getCurrentDate()), expiryDt) < 0) {
                int yesno = ClientUtil.confirmationAlert("Limit has Expired Do You Want allow Transaction");
                if (yesno == 1) {
                    observable.setOdExpired(true);
                    return true;

                }
            }
        }
        return false;
    }

    private void deletescreenLock() {
        HashMap map = new HashMap();
        map.put("USER_ID", ProxyParameters.USER_ID);
        map.put("TRANS_DT", currDt.clone());
        map.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
        ClientUtil.execute("DELETE_SCREEN_LOCK", map);
    }

    private void checkDocumentDetail() {
        String AccNo = null;
        if (txtAccNo.getText().lastIndexOf("_") != -1) {
            AccNo = txtAccNo.getText().substring(0, txtAccNo.getText().lastIndexOf("_"));
        } else {
            AccNo = txtAccNo.getText();
        }
        List lst = observable.getDocumentDetails("getSelectTermLoanDocumentTO", AccNo);
        String str = "";
        String doc_form_no = null;
        String is_submited = null;
        HashMap hash = new HashMap();
        for (int i = 0; i < lst.size(); i++) {
            hash = (HashMap) lst.get(i);
            is_submited = (String) hash.get("IS_SUBMITTED") == null ? "N" : (String) hash.get("IS_SUBMITTED");
            doc_form_no = (String) hash.get("DOC_DESC");
            if (is_submited != null) {
                if (!is_submited.equals("Y")) {
                    str = str + doc_form_no + "\n";
                }
            }
        }
        if (str.length() >= 1) {
            ClientUtil.displayAlert(str + "notsubmited");
        }
    }

    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        //        observable.resetForm();                 // Reset the fields in the UI to null...
        //        observable.resetLable();                // Reset the Editable Lables in the UI to null...
        if (ClientUtil.deleteAlert() == 0) {
            observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);//Sets the Action Type to be performed...
            btnSaveActionPerformed(evt);
            btnReject.setEnabled(false);
            btnAuthorize.setEnabled(false);
            btnException.setEnabled(false);
        }
        //        popUp(DELETE);
    }//GEN-LAST:event_btnDeleteActionPerformed

	private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
            // Add your handling code here:

            observable.resetForm();                 // Reset the fields in the UI to null...
            observable.resetLable();                // Reset the Editable Lables in the UI to null...
            observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);//Sets the Action Type to be performed...
            popUp(EDIT);
            if (rdoTransactionType_Credit.isSelected()) {
                txtTokenNo.setText("");
//                txtTokenNo.setEditable(false);
//                txtTokenNo.setEditable(false);
                cboInstrumentType.setSelectedIndex(0);
//                txtInstrumentNo1.setText("");
//                txtInstrumentNo2.setText("");
                tdtInstrumentDate.setDateValue("");

                cboInstrumentType.setEnabled(false);
//                txtInstrumentNo1.setEditable(false);
                txtInstrumentNo1.setEnabled(false);
//                txtInstrumentNo2.setEditable(false);
                txtInstrumentNo2.setEnabled(false);
                tdtInstrumentDate.setEnabled(false);
//                txtParticulars.setEditable(false);

            }
            btnReject.setEnabled(false);
            btnAuthorize.setEnabled(false);
            btnException.setEnabled(false);
            btnPanNo.setEnabled(false);
            String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
            if (!prodType.equals("GL")) {
                double amount = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                if (rdoTransactionType_Credit.isSelected() && amount >= TrueTransactMain.PANAMT) {
                    btnPanNo.setEnabled(true);
                } else if (amount < TrueTransactMain.PANAMT) {
                    btnPanNo.setEnabled(false);
                }
            }
    }//GEN-LAST:event_btnEditActionPerformed

    // To display the All the Product Id's which r having status as
    // created or updated, in a table...
    private void popUp(int field) {
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        viewType = field;
        if (field == EDIT || field == DELETE || field == VIEW) {   //Edit=0 and Delete=1
            ArrayList lst = new ArrayList();
            lst.add("TRANS_ID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            viewMap.put(CommonConstants.MAP_NAME, "viewCashTransaction");//mapped statement: viewCashTransaction---> result map should be a Hashmap...
            whereMap.put("INIT_BRANCH", TrueTransactMain.selBranch);
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        } else if (field == ACCNO) {
            updateOBFields();
            if (observable.getProdType().equals("TD") || observable.getProdType().equals("TL") || observable.getProdType().equals("AB")) {
                if (rdoTransactionType_Debit.isSelected()) {
                    if (observable.getProdType().equals("TL") || observable.getProdType().equals("AB")) {
                        whereMap.put("PAYMENT", "PAYMENT");
                        viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"
                                + ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString());
                    } else {
                        viewMap.put(CommonConstants.MAP_NAME, "getDepositHoldersInterest");

                        transDetails.setIsDebitSelect(true);
                    }
                } else if (rdoTransactionType_Credit.isSelected()) {
                    if (observable.getProdType().equals("TL") || observable.getProdType().equals("AB")) {
                        whereMap.put("RECEIPT", "RECEIPT");
                    }
                    viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"
                            + ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString());
                } else {
                    ClientUtil.showMessageWindow("Select Payment or Receipt ");
                    return;
                }
            } else {
                viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"
                        + ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString());
            }
            whereMap.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
            String act_num = CommonUtil.convertObjToStr(txtAccNo.getText());
            isAccountNumberExsistInAuthList(act_num);
        } else if (field == PAN_NUM) {
            String act_num = CommonUtil.convertObjToStr(txtAccNo.getText());
            if (act_num.lastIndexOf("_") != -1) {
                act_num = act_num.substring(0, act_num.lastIndexOf("_"));
            }
            whereMap.put("ACT_NUM", act_num);
            viewMap.put(CommonConstants.MAP_NAME, "getPanNumber");
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        } else if (field == DEBIT_DETAILS) {
            whereMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
            whereMap.put("CURRENT_DT", observable.getCurrentDate());
            viewMap.put(CommonConstants.MAP_NAME, "DebitTransDetails");
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);

        } else {
            whereMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
            whereMap.put("CURRENT_DT", observable.getCurrentDate());
            viewMap.put(CommonConstants.MAP_NAME, "TellerEntryDetails");
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        }
//            if(whereMap.get("SELECTED_BRANCH")==null)
//                whereMap.put("SELECTED_BRANCH",ProxyParameters.BRANCH_ID);
//            else
        if (field == ACCNO) {
            whereMap.put("SELECTED_BRANCH", TrueTransactMain.selBranch);
            setSelectedBranchID(TrueTransactMain.selBranch);
        } else {
            whereMap.put("SELECTED_BRANCH", getSelectedBranchID());
        }
        whereMap.put("BRANCH_SA", getSelectedBranchID());
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        new ViewAll(this, viewMap).show();

    }

    public boolean isAccountNumberExsistInAuthList(String actNumber) {
        System.out.println("@#%^^^& here 2... "+actNumber);
        boolean flag = false;
        if(actNumber != null && !actNumber.equals("")){
            HashMap whereMap = new HashMap();
            whereMap.put("ACCT_NUM",actNumber);
            whereMap.put("CURR_DT",ClientUtil.getCurrentDate());
            List unAuthList = ClientUtil.executeQuery("getNoOfUnauthorizedTransaction", whereMap);
            if(unAuthList != null && unAuthList.size() >0){
                ClientUtil.showMessageWindow("Pending For Authorization ....!");
                flag = true;
            }
        }
        return flag;
    }
    
    public boolean isAccountNumberLinkedwithATMProd(String actNumber) {
        boolean flag = false;
        if(actNumber != null && !actNumber.equals("")){
            HashMap whereMap = new HashMap();
            whereMap.put("ACCT_NUM",actNumber);
            whereMap.put("CURR_DT",ClientUtil.getCurrentDate());
            List unAuthList = ClientUtil.executeQuery("getIsAccountLinkedwitATM", whereMap);
            if(unAuthList != null && unAuthList.size() >0){
                ClientUtil.showMessageWindow("This account is linked with ATM Product, Not allowed for any Credit/Debit Transactions...!");
                txtAccNo.setText("");
                flag = true;
            }
        }
        return flag;
    }
    
    public void btnDepositClose() {
        flag = true;
        afterSaveCancel = true;
        if (!termDepositUI.getAuthorizeStatus().equals("") && termDepositUI.getAuthorizeStatus().equals("AUTHORIZE_BUTTON")) {
            btnAuthorizeActionPerformed(null);
        }
        if (!termDepositUI.getAuthorizeStatus().equals("") && termDepositUI.getAuthorizeStatus().equals("REJECT_BUTTON")) {
            btnRejectActionPerformed(null);
        }
        btnCloseActionPerformed(null);
    }

    public void btnDepositCancel() {
        afterSaveCancel = true;
        btnCancelActionPerformed(null);
        btnCloseActionPerformed(null);
    }

    public void depositCashAuth(HashMap depAuthMap, TermDepositUI termDepositUI) {
        this.termDepositUI = termDepositUI;
        flagDepositAuthorize = true;
        String actNum = CommonUtil.convertObjToStr(depAuthMap.get("ACT_NUM"));
        if (actNum != null && !actNum.equals("")) {
            flagDepAuth = false;
            observable.setDepLinkBatchId("");
        } else {
            flagDepAuth = true;
            observable.setDepLinkBatchId("DEP_LINK");
        }
        setViewType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        btnSave.setEnabled(false);
        btnCancel.setEnabled(false);
    }

    public HashMap transferingIntAmt(CTextField txtDepositAmount, HashMap intMap, TermDepositUI termDepositUI) {

        if (intMap != null) {
            this.termDepositUI = termDepositUI;
            this.txtDepositAmount = txtDepositAmount;
            flag = true;
            flagDepLink = true;
            //system.out.println("%%%%%%intMap :" + intMap);
            HashMap subNoMap = new HashMap();
            btnNewActionPerformed(null);
            subNoMap.put("DEPOSIT_NO", intMap.get("ACCOUNTNO"));
            subNoMap.put("CUST_ID", intMap.get("CUST_ID"));
            List lst = ClientUtil.executeQuery("getDepositSubNoForSub", subNoMap);
            if (lst.size() > 0) {
                subNoMap = (HashMap) lst.get(0);
                String subNo = CommonUtil.convertObjToStr(subNoMap.get("DEPOSIT_SUB_NO"));
                String depositNo = CommonUtil.convertObjToStr(intMap.get("ACCOUNTNO"));
                String accountNo = depositNo + "_" + subNo;
                intMap.put("ACCOUNTNO", accountNo);
                cboProdType.setSelectedItem(CommonUtil.convertObjToStr(intMap.get("PRODUCT_TYPE")));
                cboProdId.setSelectedItem(CommonUtil.convertObjToStr(intMap.get("PROD_DESC")));
                txtAccHdId.setText(CommonUtil.convertObjToStr(intMap.get("ACCT_HEAD")));
                lblAccHdDesc.setText(CommonUtil.convertObjToStr(intMap.get("PROD_DESC")));
                txtAccNo.setText(CommonUtil.convertObjToStr(intMap.get("ACCOUNTNO")));
                lblAccName.setText(CommonUtil.convertObjToStr(subNoMap.get("DEPOSIT_NAME")));
                cboInstrumentType.setSelectedItem(CommonUtil.convertObjToStr(intMap.get("INST_TYPE")));
                setViewType(2);
                intMap.put("PROD_TYPE", "TD");
                observable.setSelectedBranchID(CommonUtil.convertObjToStr(intMap.get("SELECTED_BRANCH")));
                fillData(intMap);
                cboProdType.setSelectedItem(CommonUtil.convertObjToStr(intMap.get("PRODUCT_TYPE")));
                txtAmount.setText(CommonUtil.convertObjToStr(intMap.get("INT_AMT")));
                cboProdId.setSelectedItem(CommonUtil.convertObjToStr(intMap.get("PROD_DESC")));
                txtAccHdId.setText(CommonUtil.convertObjToStr(intMap.get("ACCT_HEAD")));
                lblAccHdDesc.setText(CommonUtil.convertObjToStr(intMap.get("PROD_DESC")));
                txtAccNo.setText(CommonUtil.convertObjToStr(intMap.get("ACCOUNTNO")));
                lblAccName.setText(CommonUtil.convertObjToStr(subNoMap.get("DEPOSIT_NAME")));
                txtInitiatorChannel.setText("CASHIER");
                observable.setSelectedBranchID(CommonUtil.convertObjToStr(intMap.get("SELECTED_BRANCH")));
                cboInstrumentType.setSelectedItem(CommonUtil.convertObjToStr(intMap.get("INST_TYPE")));
                tdtInstrumentDate.setDateValue(CommonUtil.convertObjToStr(observable.getCurrentDate()));
                txtParticulars.setText(txtAccNo.getText());
                txtParticulars.setEnabled(false);
                cboInstrumentType.setEnabled(false);
                if (flagDepLink == true) {
                    observable.setDepLinkBatchId("DEP_LINK");
                }
                rdoTransactionType_Debit.setSelected(true);
                rdoTransactionType_Credit.setEnabled(false);
                rdoTransactionType_Debit.setEnabled(false);
                btnAccNo.setEnabled(false);
                txtAmount.setEnabled(false);
                cboProdType.setEnabled(false);
                cboProdId.setEnabled(false);
                txtAccNo.setEnabled(false);
                btnNew.setEnabled(false);
                btnEdit.setEnabled(false);
                btnSave.setEnabled(true);
                btnDelete.setEnabled(false);
                btnCancel.setEnabled(false);
                btnView.setEnabled(false);
            }
            intMap.put("CASH_AMT", txtAmount.getText());
        }
        return intMap;
    }

    public HashMap transferingDepAmt(CTextField txtDepositAmount, HashMap intMap, TermDepositUI termDepositUI) {
        if (intMap != null) {
            this.termDepositUI = termDepositUI;
            this.txtDepositAmount = txtDepositAmount;
            flagDeposit = true;
            flagDepLink = true;
            //system.out.println("%%%%%%intMap :" + intMap);
            HashMap subNoMap = new HashMap();
            btnNewActionPerformed(null);
            subNoMap.put("DEPOSIT_NO", intMap.get("ACCOUNTNO"));
            subNoMap.put("CUST_ID", intMap.get("CUST_ID"));
            List lst = ClientUtil.executeQuery("getDepositSubNoForSub", subNoMap);
            if (lst.size() > 0) {
                subNoMap = (HashMap) lst.get(0);
                String subNo = CommonUtil.convertObjToStr(subNoMap.get("DEPOSIT_SUB_NO"));
                String depositNo = CommonUtil.convertObjToStr(intMap.get("ACCOUNTNO"));
                String accountNo = depositNo + "_" + subNo;
                intMap.put("ACCOUNTNO", accountNo);
                cboProdType.setSelectedItem(CommonUtil.convertObjToStr(intMap.get("PRODUCT_TYPE")));
                cboProdId.setSelectedItem(CommonUtil.convertObjToStr(intMap.get("PROD_DESC")));
                txtAccHdId.setText(CommonUtil.convertObjToStr(intMap.get("ACCT_HEAD")));
                lblAccHdDesc.setText(CommonUtil.convertObjToStr(intMap.get("PROD_DESC")));
                txtAccNo.setText(CommonUtil.convertObjToStr(intMap.get("ACCOUNTNO")));
                lblAccName.setText(CommonUtil.convertObjToStr(subNoMap.get("DEPOSIT_NAME")));
                cboInstrumentType.setSelectedItem(CommonUtil.convertObjToStr(intMap.get("INST_TYPE")));
                setViewType(2);
                intMap.put("PROD_TYPE", "TD");
                observable.setSelectedBranchID(CommonUtil.convertObjToStr(intMap.get("SELECTED_BRANCH")));
                fillData(intMap);
                observable.setDepInterestAmt("");
                cboProdType.setSelectedItem(CommonUtil.convertObjToStr(intMap.get("PRODUCT_TYPE")));
                txtAmount.setText(CommonUtil.convertObjToStr(intMap.get("INT_AMT")));
                cboProdId.setSelectedItem(CommonUtil.convertObjToStr(intMap.get("PROD_DESC")));
                txtAccHdId.setText(CommonUtil.convertObjToStr(intMap.get("ACCT_HEAD")));
                lblAccHdDesc.setText(CommonUtil.convertObjToStr(intMap.get("PROD_DESC")));
                txtAccNo.setText(CommonUtil.convertObjToStr(intMap.get("ACCOUNTNO")));
                lblAccName.setText(CommonUtil.convertObjToStr(subNoMap.get("DEPOSIT_NAME")));
                txtInitiatorChannel.setText("CASHIER");
                txtParticulars.setText(txtAccNo.getText());
                txtParticulars.setEnabled(false);
                cboInstrumentType.setEnabled(false);
                observable.setSelectedBranchID(CommonUtil.convertObjToStr(intMap.get("SELECTED_BRANCH")));
                cboInstrumentType.setSelectedItem(CommonUtil.convertObjToStr(intMap.get("INST_TYPE")));
                tdtInstrumentDate.setDateValue(CommonUtil.convertObjToStr(observable.getCurrentDate()));
                if (flagDepLink == true) {
                    observable.setDepLinkBatchId("");
                }
                rdoTransactionType_Debit.setSelected(true);
                rdoTransactionType_Credit.setEnabled(false);
                rdoTransactionType_Debit.setEnabled(false);
                txtAmount.setEnabled(true);
                cboProdType.setEnabled(false);
                cboProdId.setEnabled(false);
                txtAccNo.setEnabled(false);
                btnNew.setEnabled(false);
                btnEdit.setEnabled(false);
                btnSave.setEnabled(true);
                btnDelete.setEnabled(false);
                btnCancel.setEnabled(false);
                btnView.setEnabled(false);
            }
            intMap.put("CASH_AMT", txtAmount.getText());
        }
        return intMap;
    }
    // this method is called automatically from ViewAll...
     public void fillData(Object param) {
         final HashMap hash = (HashMap) param;
        // System.out.println("HASH DATE ======" + hash);
         double depAmt = 0.0;
         double amts=0;
         String emiInSimpleInterest = "N";// Added by nithya on 19-05-2020 for KD-380
           double instalmantAmount = 0;
         observable.setSpecialRD("N"); // Added by nithya on 28-03-2020 for KD-1535
         hash.put("INITIATED_BRANCH", TrueTransactMain.BRANCH_ID);
         //added by sreekrishnan for kcc
         HashMap kccNatureMap = new HashMap();
         HashMap kccMap = new HashMap();
         kccNatureMap.put("ACT_NUM", CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
         //kccNatureMap.put("PROD_ID", hash.get("PROD_ID"));
         List kccList = ClientUtil.executeQuery("getKccNature", kccNatureMap);
         if (kccList != null && kccList.size() > 0) {
             kccMap = (HashMap) kccList.get(0);
             if (kccMap.containsKey("KCC_NATURE")
                     && kccMap.get("KCC_NATURE") != null && kccMap.get("KCC_NATURE").equals("Y")) {
                 observable.setKccNature("Y");
                 //kccRenewalChecking(hash);
             } else {
                 observable.setKccNature("N");
                 kccFlag = false;
             }
         } else {
             observable.setKccNature("N");
         }
        
//        //Added by sreekrishnan for interestPaidUpto
//        HashMap interestMap = new HashMap();
//        if (hash.containsKey("ACCOUNT NO")) {
//            interestMap.put("ACCOUNTNO", CommonUtil.convertObjToStr(hash.get("ACCOUNT NO")));    
//        }else{
//            interestMap.put("ACCOUNTNO", CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
//        } 
//        interestMap.put("ASON_DT",currDt.clone());
//        interestMap.put("TRANS_AMOUNT",new Double(0));
//        List interestList = ClientUtil.executeQuery("getInterestPaidUpToDate", interestMap);
//        if (interestList != null && interestList.size() > 0) {
//            interestMap = (HashMap) interestList.get(0);
//            interestPaidUpto = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(interestMap.get("INTEREST_UPTO")));
//            System.out.println("interestPaidUpto###" + interestPaidUpto);                    
//        }
        if (hash.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")) {
            fromNewAuthorizeUI= true;
            newauthorizeListUI= (NewAuthorizeListUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            btnReject.setEnabled(false);
            rejectFlag = 1;
        }
        if (hash.containsKey("FROM_AUTHORIZE_LIST_UI")) {
            fromAuthorizeUI = true;
            if(hash.containsKey("CASH_RECEIPT") && hash.get("CASH_RECEIPT") != null && hash.get("CASH_RECEIPT").equals("CASH_RECEIPT")){
                cashNode = "Cash Receipt";
            }else{
                cashNode = "Cash Payment";
            }
            authorizeListUI = (AuthorizeListUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            btnReject.setEnabled(false);
            rejectFlag = 1;
        }
        if (hash.containsKey("FROM_CASHIER_AUTHORIZE_LIST_UI")) {
            fromCashierAuthorizeUI = true;
            CashierauthorizeListUI = (AuthorizeListCreditUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            btnReject.setEnabled(false);
            rejectFlag = 1;
        }
        if (hash.containsKey("FROM_MANAGER_AUTHORIZE_LIST_UI")) {
            fromManagerAuthorizeUI = true;
            ManagerauthorizeListUI = (AuthorizeListDebitUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            btnReject.setEnabled(false);
            rejectFlag = 1;
        }

        if (hash.containsKey("FROM_CASHIER_APPROVAL_REJ_UI")) {
            //system.out.println("HASH DATE ======" + hash);
            fromAuthorizeUI = false;
            fromManagerAuthorizeUI = false;
            fromCashierAuthorizeUI = false;
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
            observable.setStatus();
            //  btnSaveDisable();
        }
        
        if (hash.containsKey("FROM_SMART_CUSTOMER_UI")) {
            //System.out.println("HASH DATE ======innnn" + hash);
            fromSmartCustUI= true;
            smartUI = (SmartCustomerUI) hash.get("PARENT");
            hash.remove("PARENT");
            btnNewActionPerformed(null);
            viewType = ACCNO;
            txtAccNo.setText(CommonUtil.convertObjToStr(hash.get("ACT_NUM")));
            txtAccNoFocusLost(null);
            observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
            observable.setStatus();
             // btnSaveDisable();
        }
        
        asAndWhenMap = new HashMap();
        if (viewType == EDIT || viewType == DELETE || viewType == AUTHORIZE || viewType == VIEW || viewType == LINK_BATCH || viewType == LINK_BATCH_TD || viewType == DEBIT_DETAILS || viewType == TELLER_ENTRY_DETIALS) {
            setModified(true);
            isFilled = true;
            hash.put("USER_ID", ProxyParameters.USER_ID);
            hash.put("WHERE", hash.get("TRANS_ID"));
            if (hash.containsKey("TRANS_DT")) {
                hash.put("TRANS_DT", DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hash.get("TRANS_DT"))));
            }
            //log.info("Hash: " + hash);
            if (viewType == EDIT || viewType == DELETE || viewType == AUTHORIZE) {
                if (viewType == EDIT || viewType == AUTHORIZE) {
                    HashMap hashmap1 = new HashMap();
                    hashmap1.put("TRANS_ID", hash.get("TRANS_ID"));
                    //system.out.println("getSelectedBranchID>>111  " + getSelectedBranchID());
                    hashmap1.put("BRANCH_ID", getSelectedBranchID());
                    //system.out.println("hash  " + hash);
                    List lst = ClientUtil.executeQuery("getOrgRespDetails", hashmap1);

                    if (lst != null && lst.size() > 0) {
                        hashmap1 = (HashMap) lst.get(0);
                        setOrgOrRespAdviceDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hashmap1.get("ADVICE_DT"))));
                        setOrgOrRespAdviceNo(CommonUtil.convertObjToStr(hashmap1.get("ADVICE_NO")));
                        setOrgOrRespAmout(CommonUtil.convertObjToDouble(hashmap1.get("AMOUNT")).doubleValue());
                        setOrgOrRespBranchId(CommonUtil.convertObjToStr(hashmap1.get("RESP_BRANCH")));
                        setOrgOrRespBranchName(CommonUtil.convertObjToStr(hashmap1.get("RESP_BRANCH_NAME")));
                        setOrgBranch(CommonUtil.convertObjToStr(hashmap1.get("ORG_BRANCH")));
                        setOrgBranchName(CommonUtil.convertObjToStr(hashmap1.get("ORG_BRANCH_NAME")));
                        setOrgOrRespCategory(CommonUtil.convertObjToStr(hashmap1.get("CATEGORY")));
                        setOrgOrRespDetails(CommonUtil.convertObjToStr(hashmap1.get("DETAILS")));
                        setOrgOrRespTransType(CommonUtil.convertObjToStr(hashmap1.get("TYPE")));
                        btnOrgOrResp.setText(CommonUtil.convertObjToStr(hashmap1.get("TYPE")));
                        btnOrgOrResp.setVisible(true);
                        observable.setHoAccount(true);
                    }

                }

                if (viewType == AUTHORIZE) {
                    HashMap hmap = new HashMap();

                    List mainList = ClientUtil.executeQuery("getSelectNominalMemFee", hash);
                    if (mainList != null && mainList.size() > 0) {
                        hmap = (HashMap) mainList.get(0);
                        String allowAuth = CommonUtil.convertObjToStr(hmap.get("ALLOW_AUTH_BY_STAFF"));
                        if (allowAuth.equals("N")) {
                            hmap.put("USER_ID", TrueTransactMain.USER_ID);
                            hmap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                            hash.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                            //system.out.println("hash" + hash);
                            String accountNo = CommonUtil.convertObjToStr(hash.get("ACCOUNT NO"));
                            //system.out.println("hash accountNo" + accountNo);
                            hmap.put("ACCOUNT NO", accountNo);
                            List lst = ClientUtil.executeQuery("getStaffIdForAccount", hmap);
                            List lst1 = ClientUtil.executeQuery("getStaffIdForLoggedUser", hmap);
                            String staffId = "";
                            String loggedStaffId = "";
                            if (lst != null && lst.size() > 0) {
                                hmap = (HashMap) lst.get(0);
                                staffId = CommonUtil.convertObjToStr(hmap.get("STAFF_ID"));
                            }
                            if (lst1 != null && lst1.size() > 0) {
                                hmap = (HashMap) lst1.get(0);
                                loggedStaffId = CommonUtil.convertObjToStr(hmap.get("STAFF_ID"));
                            }
                            if (!staffId.equals("") || !loggedStaffId.equals("")) {
                                if (staffId.equals(loggedStaffId)) {
                                    ClientUtil.displayAlert("Authorization not allowed in own account");
                                    btnCancelActionPerformed(null);
                                    return;
                                }
                            }
                        }
                    }
                     btnAuthorize.setEnabled(true);
                     btnAuthorize.setFocusable(true);
                     btnAuthorize.setFocusPainted(true);
                     btnAuthorize.requestFocus(true);
                }
                if (viewType == EDIT) {
                    hash.put("MODE_OF_OPERATION", "EDIT");
                }
                if (viewType == DELETE) {
                    hash.put("MODE_OF_OPERATION", "DELETE");
                }
                if (viewType == AUTHORIZE) {
                    hash.put("MODE_OF_OPERATION", "AUTHORIZE");
                }
                hash.put("TRANS_DT", currDt.clone());
                hash.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID); 
                ClientUtil.execute("insertauthorizationLock", hash);
            }
            observable.populateData(hash);// Called to display the Data in the UI fields...
           if (viewType == AUTHORIZE) {
                if (cboProdType.getSelectedIndex() > 0) {
                    String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
                    if (prodType.equals("GL")) {
                        lblAccNoGl.setVisible(true);
                        lblAccNoGl.setText(observable.getLinkBatchIdForGl());
                    } else {
                        lblAccNoGl.setVisible(false);
                    }
                }
                 btnAuthorize.setEnabled(true);
                 btnAuthorize.setFocusable(true);
                 btnAuthorize.setFocusPainted(true);
                 btnAuthorize.requestFocus(true);
            }
//             if(viewType==AUTHORIZE){
//               if(observable.isHoAccount()){
//                   if(observable.getRdoTransactionType_Credit()){
//                       btnOrgOrResp.setText("O");
//                   }else if(observable.getRdoTransactionType_Debit()){
//                       btnOrgOrResp.setText("R");
//                   }
//               }
//             }
            //                as an whencustomerADTL
            //modifed by sreekrishnan for cashier authorization to avoid tableDialogUI
            if (observable.getLinkMap() != null && observable.getLinkMap().size() > 0 && (viewType != LINK_BATCH)) {
                HashMap map = observable.getLinkMap();
                viewType = LINK_BATCH;
                linkBatchId = CommonUtil.convertObjToStr(map.get("ACCT_NUM"));
                HashMap screenMap = new HashMap();
                HashMap map2 = new HashMap();
                screenMap.put("LINK_BATCH_ID",map.get("ACCT_NUM"));
                screenMap.put("TRANS_DT",currDt.clone());
                
                List listData1 = ClientUtil.executeQuery("getCashScreename", screenMap);
                if (listData1 != null && listData1.size() > 0) {
                        map2 = (HashMap) listData1.get(0); 
                        if(map2.containsKey("SINGLE_TRANS_ID")){
                            single_transId=CommonUtil.convertObjToStr(map2.get("SINGLE_TRANS_ID"));
                        }
                        if(map2.get("SCREEN_NAME") != null && map2.get("SCREEN_NAME").toString().equals("Loans/Advances Account Opening")) {
                        //only else condition needed
                        }else{
                             if (map.containsKey("AS_CUSTOMER_COMES") && map.get("AS_CUSTOMER_COMES") != null && map.get("AS_CUSTOMER_COMES").equals("Y")
                             && rdoTransactionType_Credit.isSelected()) {
                             map.put("TRANS_DT", currDt.clone());
                             map.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
                             HashMap selectMap = new HashMap();
                             selectMap.put("LINK_BATCH_ID", map.get("ACCT_NUM"));
                             selectMap.put("TODAY_DT", currDt.clone());
                             selectMap.put("INITIATED_BRANCH", TrueTransactMain.BRANCH_ID);
                             selectMap.put("SINGLE_ID",single_transId);
                             TableDialogUI tableDialogUI = new TableDialogUI("getAllTransactionViewAD", selectMap, "");
                             //Added By Suresh
                             txtAmount.setText(tableDialogUI.getTotalAmtValue());
                             observable.setTxtAmount(txtAmount.getText());
                             tableDialogUI.setTitle("Loan/Advances Authorize Transaction");
                             tableDialogUI.show();
                             if(observable.displayWaive.length()>0){
                                 ClientUtil.showMessageWindow(observable.displayWaive); 
                             }
                        }
                    }
                 }
            }
            //babu
            if (viewType == AUTHORIZE) {//Nidhin
                HashMap selectMap = new HashMap();
                selectMap.put("TRANS_ID", hash.get("TRANS_ID"));
                selectMap.put("TRANS_DT", currDt.clone());
                selectMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                List aList = ClientUtil.executeQuery("getUniqueIdData", selectMap);
                //System.out.println("List ===" + aList);
                if (aList != null && aList.size() > 0) {
                    HashMap map1 = (HashMap) aList.get(0);
                    String Id = CommonUtil.convertObjToStr(map1.get("SCREEN_NAME"));
                  /*  if (Id != null && !Id.equals("") && Id.startsWith("CU")) {
                        HashMap selectMap1 = new HashMap();
                        //  selectMap1.put("LINK_BATCH_ID", map.get("ACCT_NUM"));
                        selectMap1.put("TRANS_ID", hash.get("TRANS_ID"));
                        selectMap1.put("TODAY_DT", currDt.clone());
                        selectMap1.put("INITIATED_BRANCH", TrueTransactMain.BRANCH_ID);
                        TableDialogUI tableDialogUI = new TableDialogUI("getAllTransactionViewInterest", selectMap1, "");
                        //Added By Suresh
                        txtAmount.setText(tableDialogUI.getTotalAmtValue());
                        observable.setTxtAmount(txtAmount.getText());
                        tableDialogUI.setTitle("Deposit Interest Application Transaction");
                        tableDialogUI.show();
                        isDepositIntBulk = true;
                    }else*/ if (Id != null && !Id.equals("") && Id.startsWith("CU")) {
                        HashMap selectMap1 = new HashMap();
                        selectMap1.put("SCREEN_NAME", Id);
                        selectMap1.put("TRANS_ID", hash.get("TRANS_ID"));
                        selectMap1.put("TRANS_DT", currDt.clone());
                        selectMap1.put("TODAY_DT", currDt.clone());
                        selectMap1.put("INITIATED_BRANCH", TrueTransactMain.BRANCH_ID);
                        //  selectMap1.put("LINK_BATCH_ID", map.get("ACCT_NUM"));
                        List depositIntList = ClientUtil.executeQuery("getCashSingleTransId", selectMap1);
                        if(depositIntList != null && depositIntList.size() > 0){
                            HashMap depoMap = (HashMap)depositIntList.get(0);
                            if(depoMap.containsKey("SINGLE_TRANS_ID")  && depoMap.get("SINGLE_TRANS_ID") != null)
                            selectMap1.put("SINGLE_TRANS_ID", CommonUtil.convertObjToStr(depoMap.get("SINGLE_TRANS_ID")));
                        }
                        selectMap1.put("TRANS_ID",null);
                        TableDialogUI tableDialogUI = new TableDialogUI("getAllTransactionViewInterest", selectMap1, "");
                        //Added By Suresh
//                        txtAmount.setText(tableDialogUI.getTotalAmtValue());
//                        observable.setTxtAmount(txtAmount.getText());
                        tableDialogUI.setTitle("Deposit Interest Application Transaction");
                        tableDialogUI.show();
                        if(TrueTransactMain.CASHIER_AUTH_ALLOWED!=null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")){
                            isDepositIntBulk = true;
                        }
                    }
                }
                 btnAuthorize.setEnabled(true);
                 btnAuthorize.setFocusable(true);
                 btnAuthorize.setFocusPainted(true);
                 btnAuthorize.requestFocus(true);
            }
            //
            
            if (observable.getLinkMap() != null && observable.getLinkMap().size() > 0 && observable.getLinkMap().get("ACCT_NUM") != null) {
                //system.out.println("test getLinkMap...........");
                if (observable.getProdType().equals("TL") && observable.getLinkMap().containsKey("BEHAVES_LIKE")
                        && CommonUtil.convertObjToStr(observable.getLinkMap().get("BEHAVES_LIKE")).equals("CORP_LOAN")) {
                    HashMap map = new HashMap();
                    map.put("ACT_NUM", observable.getLinkMap().get("ACCT_NUM"));
                    map.put("CURR_DT", observable.getCurrentDate());
                    List lst = ClientUtil.executeQuery("getDisbursementTrenchDetailsCorpTL", map);
                    if (lst != null && lst.size() > 0) {
                        map = (HashMap) lst.get(0);
                        map.put("BEHAVES_LIKE", observable.getLinkMap().get("BEHAVES_LIKE"));
                        transDetails.setCorpDetailMap(map);
                    } else {
                        ClientUtil.showAlertWindow("Trench details not found...");
                        btnCancelActionPerformed(null);
                        return;
                    }
                }

                long no_of_installment = 0;
                no_of_installment = CommonUtil.convertObjToLong(observable.getLinkMap().get("NO_OF_INSTALLMENT"));
                observable.setProdType(CommonUtil.convertObjToStr(hash.get("TRANS_MOD_TYPE")));
                asAndWhenMap = interestCalculationTLAD((String) observable.getLinkMap().get("ACCT_NUM"), no_of_installment);                
                actInterest=CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST"));
                HashMap intMap=new HashMap();
                intMap.put("TRANS_ID",hash.get("TRANS_ID"));
                intMap.put("TRANS_DT",currDt.clone());
                intMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
                intMap.put("CASH","CASH");
                List intData=ClientUtil.executeQuery("getInterestEntered", intMap);
                double intAmt=0;
                if(intData!=null && intData.size()>0){
                    HashMap dataMap=(HashMap)intData.get(0);
                    intAmt=CommonUtil.convertObjToDouble(dataMap.get("AMOUNT"));
                }
                //System.out.println("actInterest--->"+actInterest+"intAmt  --->"+intAmt+"IMPP--->"+CommonConstants.OPERATE_MODE);
                transDetails.setChangeInterest("N");
                if((observable.getProdType().equals("TL") || observable.getProdType().equals("AD")) ){
                   if(actInterest!=intAmt && CommonUtil.convertObjToStr(CommonConstants.OPERATE_MODE).equals(CommonConstants.IMPLEMENTATION)) {
                    transDetails.setChangeInterest("Y");
                    } 
                }
                if (asAndWhenMap != null && asAndWhenMap.size() > 0) {
                    asAndWhenMap.put("INSTALL_TYPE", observable.getLinkMap().get("INSTALL_TYPE"));
                    transDetails.setAsAndWhenMap(asAndWhenMap);
                }
            }
            //AS END
            // To set the Value of Transaction Id...
            if (observable.getProdType().equals("TD")) {
                if (flagDepAuth == true) {
                    hash.put("ACCOUNT NO", "");
                    observable.setTxtAccNo("");
                    observable.setTxtAccHd(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                    observable.setCboProdType(CommonUtil.convertObjToStr(hash.get("PROD_TYPE")));
                    observable.setProdType(CommonUtil.convertObjToStr(hash.get("PROD_TYPE")));
                    observable.setCboProdId(CommonUtil.convertObjToStr(""));
                }
            }
            final String TRANSID = (String) hash.get("SINGLE_TRANS_ID");
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                int yesNo = 0;
                String[] options = {"Yes", "No"};
                yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                        null, options, options[0]);
                //system.out.println("#$#$$ yesNo : " + yesNo);
                if (yesNo == 0) {
                    TTIntegration ttIntgration = null;
                    HashMap reportTransIdMap = new HashMap();
                    reportTransIdMap.put("TransId", TRANSID);
                    reportTransIdMap.put("TransDt", observable.getCurrentDate());
                    reportTransIdMap.put("BranchId", ProxyParameters.BRANCH_ID);
                    ttIntgration.setParam(reportTransIdMap);
                    String transType = "";
                    if (rdoTransactionType_Debit.isSelected()) {
                        transType = "DEBIT";
                    } else {
                        transType = "CREDIT";
                    }
//                        if (((String)TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase().lastIndexOf("POLPULLY")!=-1) {
//                            ttIntgration.integrationForPrint("ReceiptPayment");
//                        } else {
                    if (transType.equals("DEBIT")) {
                        ttIntgration.integrationForPrint("CashPayment");
                    } else {
                        ttIntgration.integrationForPrint("CashReceipt");
                    }
//                        }
                }
            }

            String ACCOUNTNO = "";
            if (hash.containsKey("ACCOUNT NO") && hash.get("ACCOUNT NO") != null) {
                ACCOUNTNO = (String) hash.get("ACCOUNT NO");
            }
            //                if(observable.getProdType().equals("TL"))  //For Processing Charges
            //                    observable.getLinkBatchId();
            if (observable.getProdType().equals("TD")) {
                transDetails.setIsDebitSelect(true);
            }
            //To Set the Value of Account holder Name and the Balances in UI...
//              
            if (viewType != LINK_BATCH) {
                if (!ACCOUNTNO.equals("")) {
                    //System.out.println("55###@@@");
                    observable.setAccountName(ACCOUNTNO);
                    lblHouseName.setText(observable.getLblHouseName());
                    lblAccName.setText(observable.getLblAccName());
                    if (observable.getProdType().equals("GL")) {
                        //                        observable.setAccountName(observable.getTxtAccHd());
                        ACCOUNTNO = observable.getTxtAccHd();
                    }
                    //System.out.println("ACCOUNTNO" + ACCOUNTNO);;
                    transDetails.setTransDetails(observable.getProdType(), ProxyParameters.BRANCH_ID, ACCOUNTNO);
                    transDetails.setProductId(observable.getCboProdId());//Added by kannan
                    transDetails.setSourceFrame(this);
                } else if (!observable.getTxtAccHd().equals("")) {
                    transDetails.setTransDetails(observable.getProdType(), ProxyParameters.BRANCH_ID, observable.getTxtAccHd());
                    transDetails.setProductId(observable.getCboProdId());//Added by kannan
                    transDetails.setSourceFrame(this);
                }
//                   btnViewTermLoanDetails.setVisible(false);
//                   btnViewTermLoanDetails.setEnabled(false);
            }
            if (viewType == LINK_BATCH) {
                //changed by rish 21/01/2015 
             //   transDetails.setTransDetails("TL", ProxyParameters.BRANCH_ID, ACCOUNTNO);
                 observable.setProdType(CommonUtil.convertObjToStr(hash.get("TRANS_MOD_TYPE")));
                 transDetails.setTransDetails(observable.getProdType(), ProxyParameters.BRANCH_ID, ACCOUNTNO);
                 transDetails.setSourceFrame(this);
//                   btnViewTermLoanDetails.setVisible(true);
//                   btnViewTermLoanDetails.setEnabled(true);
            }
            setButtonEnableDisable();// Enables or Disables the buttons and menu Items depending on their previous state...
            if (observable.getLinkMap().containsKey("AS_CUSTOMER_COMES") && observable.getLinkMap().get("AS_CUSTOMER_COMES") != null && observable.getLinkMap().get("AS_CUSTOMER_COMES").equals("Y")) {
                setButtonEnableDisable(false);
            }
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || viewType == AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW || viewType == LINK_BATCH || viewType == LINK_BATCH_TD) {
                ClientUtil.enableDisable(this, false);
                // Disables the panel...
            } else {
                ClientUtil.enableDisable(this, true);// Enables the panel...
                enableDisableButtons(true);         // To Enable the Buttons(folder) buttons in UI...
                textDisable();                  // To set the Text Boxes for Account No. and InitiatorChannel, nonEditable...
                this.btnDelete.setEnabled(true);
            }
            setEditFieldsEnable(false);
            observable.setStatus();             // To set the Value of lblStatus...

            if (hash.get("INSTRUMENT_NO2") != null && hash.get("INSTRUMENT_NO2").equals("DEPOSIT_PENAL")) {
                viewType = LINK_BATCH_TD;
                HashMap penalMap = new HashMap();
                penalMap.put("ACCT_NUM", hash.get("ACCOUNT NO"));
                penalMap.put("TRANS_ID", hash.get("TRANS_ID"));
                penalMap.put("TRANS_DT", currDt.clone());
                penalMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
                TableDialogUI tableDialogUI = new TableDialogUI("getCashTransactionTOForAuthorzationLinkBatch", penalMap, this);
                tableDialogUI.setTitle("Deposit Transaction");
                tableDialogUI.show();
                observable.setDepositPenalAmt(transDetails.getPenalAmount());
                observable.setDepositPenalMonth(transDetails.getPenalMonth());
                ClientUtil.enableDisable(this, false);
                btnCancel.setEnabled(true);
            }
            if (viewType == AUTHORIZE || viewType == LINK_BATCH || viewType == LINK_BATCH_TD) {
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
            }
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || viewType == AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
                ClientUtil.enableDisable(this, false);// Disables the panel...
                enableDisableButtons(false);
            }
        } else if (viewType == ACCNO) {
            transDetails.setInstallmentToPay(0);
            observable.setAsAnWhenCustomer("");
            String ACCOUNTNO = CommonUtil.convertObjToStr(hash.get("ACCOUNTNO"));
            if (observable.getProdType().equals("TD") && ACCOUNTNO.lastIndexOf("_") == -1) {
                ACCOUNTNO = ACCOUNTNO + "_1";
            }

//            if(lockAccount!=null&&!lockAccount.equals(""))
//            {
//            removeEditLockWithAcct(lockAccount);
//            }
//            //added by rishad 21/07/2015 for locking 
//            HashMap lockMap = new HashMap();
//            lockMap.put("SCREEN_ID", getScreenID());
//            lockMap.put("RECORD_KEY", ACCOUNTNO);
//            lockMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
//            lockMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
//            lockMap.put("CUR_DATE", currDt.clone());
//            List lstLock = ClientUtil.executeQuery("selectEditLock", lockMap);
//            if (lstLock != null && lstLock.size() > 0) {
//                ClientUtil.displayAlert("Account is locked");
//                txtAccNo.setText("");
//                btnCancelActionPerformed(null);
//                return;
//            } else {
//                lockAccount=ACCOUNTNO;
//                lockMap = new HashMap();
//                ArrayList lst = new ArrayList();
//                lst.add("ACCOUNT NUMBER");
//                lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
//                lockMap.put("ACCOUNT NUMBER", ACCOUNTNO);
//                setEditLockMap(lockMap);
//                setEditLock();
//            }
//              setEditLock();
            //end
            lblHouseName.setText(CommonUtil.convertObjToStr(hash.get("HOUSENAME")));
            //System.out.println("filldata###" + ACCOUNTNO);
            observable.setTxtAccNo(ACCOUNTNO);
            HashMap mapHash = observable.asAnWhenCustomerComesYesNO(ACCOUNTNO);
            
             // Added by nithya on 19-05-2020 for KD-380
            if (mapHash != null && mapHash.containsKey("EMI_IN_SIMPLEINTREST") && mapHash.get("EMI_IN_SIMPLEINTREST")!= null && mapHash.get("EMI_IN_SIMPLEINTREST").equals("Y")){
                emiInSimpleInterest = "Y";
                observable.setEMIinSimpleInterest("Y");
            }else{
                observable.setEMIinSimpleInterest("N");
            }
            // End
            
            
            //            observable.setLblAccName(CommonUtil.convertObjToStr(hash.get("CUSTOMERNAME")));
            long noofInstallment = 0;
            if (rdoTransactionType_Credit.isSelected() && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                HashMap transMap = new HashMap();
                transMap.put("LINK_BATCH_ID", ACCOUNTNO);
                transMap.put("TRANS_DT", currDt.clone());
                transMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
                List pendingList = ClientUtil.executeQuery("getPendingTransactionTL", transMap);
                if (pendingList != null && pendingList.size() > 0) {
                    HashMap hashTrans = (HashMap) pendingList.get(0);
                    String trans_actnum = CommonUtil.convertObjToStr(hashTrans.get("LINK_BATCH_ID"));
                    if (trans_actnum.equals(ACCOUNTNO)) {
                        ClientUtil.showMessageWindow(" There is Pending Transaction Plz Authorize OR Reject first  ");
                        hashTrans = null;
                        pendingList = null;
                        observable.setTxtAccNo("");
                        txtAccNo.setText("");
                        return;
                    }
                }
                transDetails.setTransDetails(observable.getProdType(), ProxyParameters.BRANCH_ID, ACCOUNTNO);
                if (observable.getProdType().equals("TL") || observable.getProdType().equals("AD")) {
                      if (mapHash != null && mapHash.containsKey("INSTALL_TYPE")){
                      observable.setInstalType( CommonUtil.convertObjToStr(mapHash.get("INSTALL_TYPE")));}
                     }
                if (observable.getLinkMap().containsKey("AS_CUSTOMER_COMES") && observable.getLinkMap().get("AS_CUSTOMER_COMES") != null && observable.getLinkMap().get("AS_CUSTOMER_COMES").equals("Y")) {
                    if (mapHash != null && mapHash.containsKey("INSTALL_TYPE") && (!(mapHash.get("INSTALL_TYPE").equals("UNIFORM_PRINCIPLE_EMI")||mapHash.get("INSTALL_TYPE").equals("USER_DEFINED")
                           || mapHash.get("INSTALL_TYPE").equals("LUMP_SUM") || mapHash.get("INSTALL_TYPE").equals("EYI")|| mapHash.get("INSTALL_TYPE").equals("EMI"))) && rdoTransactionType_Credit.isSelected() == true && viewType != AUTHORIZE) {
                        //Added By Suresh 
                        String depNo = txtAccNo.getText();
                        HashMap recurringMap = new HashMap();
                        if (depNo.contains("_")) {
                        depNo = depNo.substring(0, depNo.lastIndexOf("_"));}
                        recurringMap.put("DEPOSIT_NO", depNo.trim());
                        double depAmount = 0;
                        List lst = ClientUtil.executeQuery("getDepositAmountForRecurring", recurringMap);
                        if (lst != null && lst.size() > 0) {
                            recurringMap = (HashMap) lst.get(0);
                             depAmount = CommonUtil.convertObjToDouble(recurringMap.get("DEPOSIT_AMT")).doubleValue();
                        }
                        String remark = COptionPane.showInputDialog(this, "INSTALLMENT AMOUNT : Rs " + depAmount + "\n" + "NO OF INSTALLMENT WANT TO PAY");
                        if (CommonUtil.convertObjToStr(remark).equals("")) {
                            remark = "0";
                        }
                        try {
                            noofInstallment = Long.parseLong(remark);
                        } catch (java.lang.NumberFormatException e) {
                            ClientUtil.displayAlert("Invalid Number...");
                            txtAccNoFocusLost(null);
                            return;
                        }
                        //                        noofInstallment=0;
                    }
                }
               
                //System.out.println("$#%#$%#$%behavesLike453535:" + depBehavesLike);
              //                if (depBehavesLike.equals("RECURRING"))  cboProdId.getSelectedItem().equals("Recurring Deposit") || cboProdId.getSelectedItem().equals("Staff R.d") || cboProdId.getSelectedItem().equals("Time Deposit")
                if (depBehavesLike.equals("RECURRING") && rdoTransactionType_Credit.isSelected() == true && viewType != AUTHORIZE) {

                    //   if (cboProdId.getSelectedItem().equals("Recurring Deposit") || cboProdId.getSelectedItem().equals("Staff R.d") || cboProdId.getSelectedItem().equals("Time Deposit") && rdoTransactionType_Credit.isSelected() == true && viewType != AUTHORIZE) {
                    //Added By Suresh 
                    //changed by rishad 07-01-2015 for mantis 0010129
                    String depNo = CommonUtil.convertObjToStr(hash.get("ACCOUNTNO"));
                    HashMap recurringMap = new HashMap();
                    if (depNo.contains("_")) {
                        depNo = depNo.substring(0, depNo.lastIndexOf("_"));
                    }
                    recurringMap.put("DEPOSIT_NO", depNo.trim());
                    double depAmount = 0;
                    List lst = ClientUtil.executeQuery("getDepositAmountForRecurring", recurringMap);
                    if (lst != null && lst.size() > 0) {
                        recurringMap = (HashMap) lst.get(0);
                        depAmount = CommonUtil.convertObjToDouble(recurringMap.get("DEPOSIT_AMT"));
                        amts=CommonUtil.convertObjToDouble(depAmount);
                    }
                    noofInstallment2 = 0;
                        
                        String remark = COptionPane.showInputDialog(this, "INSTALLMENT AMOUNT : Rs " + depAmount + "\n" + "NO OF INSTALLMENT WANT TO PAY");
                   //    String remark = COptionPane.showInputDialog(this, "INSTALLMENT AMOUNT : Rs " + CommonUtil.convertObjToDouble(hash.get("DEPOSIT_AMT")) + "\n" + "NO OF INSTALLMENT WANT TO PAY");
                    //                        int remark=ClientUtil.confirmationAlert("NO OF INSTALLMENT WANT TO PAY");
                    //System.out.println("remark" + remark);
                    if (CommonUtil.convertObjToStr(remark).equals("") || CommonUtil.convertObjToStr(remark) == null) {
                        remark = "0";
                    }
                    try {
                        noofInstallment2 = CommonUtil.convertObjToLong(remark);
                        //System.out.println("##### Initial noofInstallment2 : "+noofInstallment2);
                        //                            observable.setNoofInstallment(noofInstallment);
                    } catch (java.lang.NumberFormatException e) {
                        ClientUtil.displayAlert("Invalid Number...");
                        txtAccNoFocusLost(null);
                        return;
                    }
                    //                        noofInstallment=0;
                }
                //added by rishad 08/10/2015
             //    if ( mapHash.get("INSTALL_TYPE").equals("UNIFORM_PRINCIPLE_EMI") && rdoTransactionType_Credit.isSelected() == true && viewType != AUTHORIZE) {
                //System.out.println("map hash..."+mapHash);
                //System.out.println("emiInSimpleInterest :: " + emiInSimpleInterest);
                if (mapHash != null && mapHash.containsKey("INSTALL_TYPE") && mapHash.get("INSTALL_TYPE").equals("EMI") && rdoTransactionType_Credit.isSelected() == true && viewType != AUTHORIZE && emiInSimpleInterest.equalsIgnoreCase("N")) {
                          String loanNo = CommonUtil.convertObjToStr(hash.get("ACCOUNTNO"));
                        HashMap loanMap = new HashMap();
                        loanMap.put("Act_Num", loanNo);
                         String detail="";
                    List lst = ClientUtil.executeQuery("getLoanAmountForEmi",loanMap );
                        if (lst != null && lst.size() > 0) {
                            loanMap  = (HashMap) lst.get(0);
                          
                             detail = CommonUtil.convertObjToStr(loanMap.get("DETAIL"));
                        }
                    String remark = COptionPane.showInputDialog(this, "INSTALLMENT DETAILS:"+"\n" +detail + "\n" + "NO OF INSTALLMENT WANT TO PAY");
                    //                        int remark=ClientUtil.confirmationAlert("NO OF INSTALLMENT WANT TO PAY");
                    //System.out.println("remark" + remark);
                    if (CommonUtil.convertObjToStr(remark).equals("") || CommonUtil.convertObjToStr(remark) == null) {
                        remark = "0";
                    }
                    try {
                         noofInstallment2 = CommonUtil.convertObjToLong(remark);
                        //System.out.println("noofInstallment2"+ noofInstallment2);
                        //                            observable.setNoofInstallment(noofInstallment);
                    } catch (java.lang.NumberFormatException e) {
                        ClientUtil.displayAlert("Invalid Number...");
                        txtAccNoFocusLost(null);
                        return;
                    }
                    //                        noofInstallment=0;
                }
                
                 // Added by nithya on 19-05-2020 for KD-380
                if (mapHash != null && mapHash.containsKey("INSTALL_TYPE") && mapHash.get("INSTALL_TYPE").equals("EMI") && rdoTransactionType_Credit.isSelected() == true && viewType != AUTHORIZE && emiInSimpleInterest.equalsIgnoreCase("Y")) {
                    String loanNo = CommonUtil.convertObjToStr(hash.get("ACCOUNTNO"));
                    HashMap loanMap = new HashMap();
                    loanMap.put("ACCT_NUM", loanNo);
                    loanMap.put("CURR_DT", currDt.clone());
                    double currentPrincipleDue = 0.0;
                    List emiPricipalDueList = ClientUtil.executeQuery("getEMIInSimpleInterestPrincipleDue", loanMap);
                    if (emiPricipalDueList != null && emiPricipalDueList.size() > 0) {
                        HashMap emiPricipalDueMap = (HashMap) emiPricipalDueList.get(0);
                        if (emiPricipalDueMap.containsKey("PRINCIPAL_DUE") && emiPricipalDueMap.get("PRINCIPAL_DUE") != null) {
                            currentPrincipleDue = CommonUtil.convertObjToDouble(emiPricipalDueMap.get("PRINCIPAL_DUE"));
                            ClientUtil.showMessageWindow("Principal Due :: " + currentPrincipleDue);
                        }
                    }
                }
                
                //System.out.println("behb=aves like"+depBehavesLike);
                if (noofInstallment2 > 0 && depBehavesLike.equals("RECURRING")) {
                    //System.out.println("reached noofInstallment2 > 0");
                    //System.out.println("noofInstallment2"+noofInstallment2);
                    observable.setInstallMentNo(CommonUtil.convertObjToInt(noofInstallment2));
                    transDetails.setTransDetails(observable.getProdType(), ProxyParameters.BRANCH_ID, ACCOUNTNO, noofInstallment2);
               
                }
                  if (mapHash != null && mapHash.containsKey("INSTALL_TYPE")&&mapHash.get("INSTALL_TYPE").equals("EMI")&&noofInstallment2 > 0) {
                    noofInstallment= noofInstallment2 ;
                    transDetails.setTransDetails(observable.getProdType(), ProxyParameters.BRANCH_ID, ACCOUNTNO, noofInstallment2);   
                  }
            }
            //Added by sreekrishnan for avoiding transaction with out authorization of previos one..
            if (observable.getProdType().equals(CommonConstants.TXN_PROD_TYPE_ADVANCES) && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                 if (observable.isAuthorizationPending(ACCOUNTNO)) {
                     ClientUtil.showMessageWindow("There is Pending Transaction Please Authorize OR Reject first  ");
                     observable.setAccNumGl("");
                     observable.setAccNumGlProdType("");
                     txtAccNo.setText("");
                     return;
                 }
            }
            if (observable.getProdType().equals(CommonConstants.TXN_PROD_TYPE_LOANS) && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                 if (observable.isAuthorizationPendingTL(ACCOUNTNO)) {
                     ClientUtil.showMessageWindow("There is Pending Transaction Please Authorize OR Reject first  ");
                     observable.setAccNumGl("");
                     observable.setAccNumGlProdType("");
                     txtAccNo.setText("");
                     return;
                 }
            }
            
            //Added by sreekrishnan
            //System.out.println("observable.getProdType()###@@@"+observable.getProdType());
            if ((observable.getProdType().equals(CommonConstants.TXN_PROD_TYPE_ADVANCES) || observable.getProdType().equals(CommonConstants.TXN_PROD_TYPE_LOANS)) && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                HashMap hmap = new HashMap();
                Date toDate = new Date();
                hmap.put("ACCOUNTNO",ACCOUNTNO);        
                List kccList1 = ClientUtil.executeQuery("getKccSacntionTodate", hmap);
                if (kccList1 != null && kccList1.size() > 0) {
                    hmap = (HashMap) kccList1.get(0);
                    toDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hmap.get("TO_DT")));
                    //System.out.println("toDate###"+toDate);
                    if(!toDate.equals("")){
                        if (toDate.before((Date) currDt.clone())) {
                            if(observable.getProdType().equals(CommonConstants.TXN_PROD_TYPE_LOANS)){
                                lblLoanStatus.setText("Loan Period is Over");
                            }else{
                                lblLoanStatus.setText("Advance Period is Over");
                            }                           
                        }else{
                           lblLoanStatus.setText("");
                        }
                    }else{
                           lblLoanStatus.setText("");
                        }
                }else{
                      lblLoanStatus.setText("");
                }
            }
            
            //System.out.println("11###@@@");
            observable.setAccountName(ACCOUNTNO);
            String act_name = setAccountName(ACCOUNTNO);
            lblHouseName.setText(observable.getLblHouseName());
            lblAccName.setText(observable.getLblAccName());
            //system.out.println("fill###" + observable.getLblAccName());
            transDetails.setCorpDetailMap(new HashMap());
            if (ACCOUNTNO != null && ACCOUNTNO.length() > 0 && rdoTransactionType_Credit.isSelected()) {
                if ((observable.getProdType().equals("TL") || observable.getProdType().equals("AD")) && (mapHash.containsKey("AS_CUSTOMER_COMES") && mapHash.get("AS_CUSTOMER_COMES") != null && mapHash.get("AS_CUSTOMER_COMES").equals("Y"))) {
                    asAndWhenMap = interestCalculationTLAD(ACCOUNTNO, noofInstallment);
                      if (asAndWhenMap != null && asAndWhenMap.size() > 0) {
                        asAndWhenMap.put("INSTALL_TYPE", observable.getLinkMap().get("INSTALL_TYPE"));
                        transDetails.setAsAndWhenMap(asAndWhenMap);
                         //System.out.println("asAndWhenMap tyy--->"+asAndWhenMap);
                        if (asAndWhenMap.containsKey("NO_OF_INSTALLMENT") && CommonUtil.convertObjToLong(asAndWhenMap.get("NO_OF_INSTALLMENT")) > 0) {
                            noofInstallment = CommonUtil.convertObjToLong(asAndWhenMap.get("NO_OF_INSTALLMENT"));
                        }
                       
                    }
                      
                }
            } else if (ACCOUNTNO != null && ACCOUNTNO.length() > 0 && rdoTransactionType_Debit.isSelected()) {  // For Corporate Loan Disbursement added by Rajesh
                if (observable.getProdType().equals("TL") && mapHash.containsKey("BEHAVES_LIKE")
                        && CommonUtil.convertObjToStr(mapHash.get("BEHAVES_LIKE")).equals("CORP_LOAN")) {
                    HashMap map = new HashMap();
                    map.put("ACT_NUM", ACCOUNTNO);
                    map.put("CURR_DT", observable.getCurrentDate());
                    List lst = ClientUtil.executeQuery("getDisbursementTrenchDetailsCorpTL", map);
                    if (lst != null && lst.size() > 0) {
                        map = (HashMap) lst.get(0);
                        map.put("BEHAVES_LIKE", mapHash.get("BEHAVES_LIKE"));
                        transDetails.setCorpDetailMap(map);
                    } else {
                        ClientUtil.showAlertWindow("All trenches have been disbursed...");
                        observable.setLblAccName("");
                        txtAccNo.setText("");
                        return;
                    }
                }
            }
            if (observable.getLblAccName().length() > 1) {
                transDetails.setTransDetails(null, null, null);
                transDetails.setSourceFrame(null);
                transDetails.setTransDetails(observable.getProdType(), ProxyParameters.BRANCH_ID, ACCOUNTNO);
                transDetails.setProductId(observable.getCboProdId());//Added by kannan
                transDetails.setSourceFrame(this);
                if (depBehavesLike.equals("DAILY") && depPartialWithDrawalAllowed.equals("Y")) {
                    //system.out.println("hvbhjvdbb333");
                    observable.setTxtAmount("");
                } else {
                    //system.out.println("hvbhjvdbb444");
                    //system.out.println("(hash.get(\"AMOUNT\"))>>>" + (hash.get("AMOUNT")));
                    //system.out.println("instt no. 111>>>" + noofInstallment2);
                    //system.out.println("hvbhjvdbb111");
                    //commented by rishad some scenarios not working because of this portion of code
//                    double amts = CommonUtil.convertObjToDouble((hash.get("AMOUNT")));
//                    //Added By Suresh R    ClearBalance Should not take for Recurring Deposit.  25-Jul-2017
//                    if (depBehavesLike.equals("RECURRING")) {
//                        amts = CommonUtil.convertObjToDouble((hash.get("DEPOSIT_AMT")));
//                    }
                   // System.out.println("amts" + amts);
                   // System.out.println("-------------"+noofInstallment2);
                    double amt2 = (amts) * noofInstallment2;
                  //  System.out.println("###### Final Amount : " + amt2);
                    double penalAmt = CommonUtil.convertObjToDouble(transDetails.getPenalAmount()).doubleValue();
                  //  System.out.println("###### Penal Amount : " + penalAmt);
                    if (penalAmt > 0 && amt2>0) {
                        amt2 = amt2 + penalAmt;
                    }
                 //   System.out.println("###### Final Amount 1 : " + amt2);
                    if (amt2 != 0 || amt2 != 0.0) {
                        observable.setTxtAmount(CommonUtil.convertObjToStr(amt2));
                    }else{
                        observable.setTxtAmount("");
                    }
                    //  observable.setTxtAmount(CommonUtil.convertObjToStr(hash.get("AMOUNT")));
                }
                observable.setLimitAmount(transDetails.getLimitAmount());
                //system.out.println("limitAmount" + observable.getLimitAmount());
                if (observable.getProdType().equals("TL") || observable.getProdType().equals("AD")) {
                    observable.setALL_LOAN_AMOUNT(transDetails.getTermLoanCloseCharge());
                    observable.getALL_LOAN_AMOUNT().put("NO_OF_INSTALLMENT", new Long(noofInstallment));
                      if (mapHash != null && mapHash.containsKey("INSTALL_TYPE")){
                      observable.setInstalType( CommonUtil.convertObjToStr(mapHash.get("INSTALL_TYPE")));}
                    if (rdoTransactionType_Credit.isSelected() && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                        if (mapHash != null && mapHash.containsKey("INSTALL_TYPE") && (!(mapHash.get("INSTALL_TYPE").equals("UNIFORM_PRINCIPLE_EMI")
                                || mapHash.get("INSTALL_TYPE").equals("USER_DEFINED")|| mapHash.get("INSTALL_TYPE").equals("LUMP_SUM") || mapHash.get("INSTALL_TYPE").equals("EYI")))) {
                           observable.setInstalType( CommonUtil.convertObjToStr(mapHash.get("INSTALL_TYPE")));
                           if(emiInSimpleInterest.equalsIgnoreCase("N")){ //Added by nithya on 19-05-2020 for KD-380 
                           txtAccNo.setText(ACCOUNTNO); // Added by nithya on 30-04-2021 for KD-2801
                           calculateServiceTaxAmt();
                           double totalEMIAmt = setEMIAmount();
                            //system.out.println("hvbhjvdbb555");
                            observable.setTxtAmount(String.valueOf(totalEMIAmt));
                            txtAmount.setEnabled(false);
                           }else{
                            txtAmount.setEnabled(true); 
                           }


                        }
                    }

                    //                            observable.checkBalanceOperativeAccount(); dont delete becase  if product level enable means its working

                    //                            observable.checkBalanceOperativeAccount(); dont deleted

                }
                //                        if(observable.isFlag()){
                //                            transDetails.setLimitAmount(observable.getLimitAmount());
                //                        }
                if (rdoTransactionType_Credit.isSelected() && observable.getProdType().equals("TD")) {
                    double shadowCredit = CommonUtil.convertObjToDouble(transDetails.getShadowCredit()).doubleValue();
                    if (shadowCredit > 0) {
                        ClientUtil.showAlertWindow("Already Transaction is completed, pending for authorization...");
                        btnCancelActionPerformed(null);
                        return;
                    } else {
                        double txtAmt = CommonUtil.convertObjToDouble(hash.get("AMOUNT")).doubleValue();
                        //System.out.println("txtAmt in cash==" + txtAmt);
                        depAmt = CommonUtil.convertObjToDouble(transDetails.getDepositAmt()).doubleValue();
                        //System.out.println("depAmt in cash==" + depAmt);
                        double balance = txtAmt - depAmt;
                        //System.out.println("balance in cash==" + balance);
                        if (balance > 0) {
                            //system.out.println("hvbhjvdbb666");
                            observable.setTxtAmount(String.valueOf(balance));
                            txtAmount.setText(String.valueOf(balance));
                        }
                    }
                }
                if (rdoTransactionType_Debit.isSelected() && observable.getProdType().equals("TD")) {
                    if (depBehavesLike.equals("DAILY") && depPartialWithDrawalAllowed.equals("Y")) {
                        //system.out.println("hvbhjvdbb777");
                        observable.setTxtAmount("");
                        HashMap checkMap = new HashMap();
                        checkMap.put("DEPOSIT_NO", ACCOUNTNO.substring(0, ACCOUNTNO.lastIndexOf("_")));
                        List checkForLienList = ClientUtil.executeQuery("getIsGroupDepositExistsForLoan", checkMap);
                        if (checkForLienList != null && checkForLienList.size() > 0) {
                            ClientUtil.showMessageWindow("Lien exists for this deposit");
                        }
                    } else {
                        if(depBehavesLike.equals("RECURRING")){
                            HashMap roiParamMap = new HashMap();
                            roiParamMap.put("DEPOSIT_NO", ACCOUNTNO.substring(0, ACCOUNTNO.lastIndexOf("_")));
                            roiParamMap.put("PROD_ID",  ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString());
                            List specialRDCompleteLst = ClientUtil.executeQuery("getSpecialRDCompletedStatus", roiParamMap);
                            if(specialRDCompleteLst != null && specialRDCompleteLst.size() > 0){
                                HashMap specialRDMap = (HashMap)specialRDCompleteLst.get(0);
                                if(specialRDMap.containsKey("COMPLETESTATUS") && specialRDMap.get("COMPLETESTATUS") != null){
                                    if(CommonUtil.convertObjToStr(specialRDMap.get("COMPLETESTATUS")).equalsIgnoreCase("Y")){
                                        System.out.println("Special RD.....");                                        
                                        observable.setSpecialRD("Y");
                                    }
                                }
                            }
                        }
                        if(observable.getSpecialRD().equalsIgnoreCase("Y")){
                           txtAmount.setEnabled(true); 
                        }else{
                        //system.out.println("hvbhjvdbb888");
                        observable.setTxtAmount(transDetails.getDepInterestAmt());
                        //                            observable.setTxtAmount("");
                        //                            transDetails.setDepInterestAmt()
                        double intAmt = CommonUtil.convertObjToDouble(transDetails.getDepInterestAmt()).doubleValue();
                        if (intAmt > 0) {
                            txtAmount.setText(transDetails.getDepInterestAmt());
                            txtAmount.setEnabled(false);
                            observable.setDepInterestAmt(txtAmount.getText());
                        } else {
                            ClientUtil.showMessageWindow("Upto Date Interest is Paid...");
                            btnCancelActionPerformed(null);
                            txtAmount.setText("");
                        }
                       }
                    }
                }

                observable.ttNotifyObservers();
                if (rdoTransactionType_Debit.isSelected() && observable.getProdType().equals("TD")) {
                    if (depBehavesLike.equals("DAILY") && depPartialWithDrawalAllowed.equals("Y")) {
                        txtParticulars.setText("");
                    } else if(observable.getSpecialRD().equals("Y")){ // Added by nithya on 08-07-2020 for KD-2066
                        txtParticulars.setEnabled(true);
                        txtParticulars.setText("");
                    } else {
                        txtParticulars.setText("To,Fd Interest " + CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
                    }
                }
                checkLoanTransaction(hash);
                //Added By Suresh
                if (observable.getProdType().equals("TD")) {
                    if (depBehavesLike.equals("RECURRING")) {
                        if (transDetails.isClearTransFlag() == true) {
                            txtAccNo.setText("");
                            txtAmount.setText("");
                            lblAccName.setText("");
                            lblHouseName.setText("");
                        }
                    }
                }
            }
            // Added By kannan
            if (observable.getProdType().equals("OA") && rdoTransactionType_Debit.isSelected()) {
                boolean OverDue = false;
                HashMap operativeMap = new HashMap();
                operativeMap.put("ACT_NUM", observable.getTxtAccNo());
                List allCustLst = ClientUtil.executeQuery("getCustIdfromMembershipLiability", operativeMap);
                if (allCustLst != null && allCustLst.size() > 0) {
                    HashMap SingleMap = new HashMap();
                    SingleMap = (HashMap) allCustLst.get(0);
                    SingleMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                    List dueList = ClientUtil.executeQuery("getSelectMembershipLiabilityDetails", SingleMap);
                    double dueAmt = 0.0;
                    if (dueList != null && dueList.size() > 0) {
                        for (int i = 0; i < dueList.size(); i++) {
                            HashMap OverDueMap = new HashMap();
                            OverDueMap = (HashMap) dueList.get(i);
                            dueAmt = CommonUtil.convertObjToDouble(OverDueMap.get("PRINC_DUE"));
                            if (dueAmt > 0) {
                                i = dueList.size();
                                OverDue = true;
                            }
                        }
                    }
                }
                if (OverDue) {//Un Commented By Revathi.L reff By Mr.Srinath
                    ClientUtil.showAlertWindow("Overdue Loans In A/C, Click Membership Liability Button For More Details");
                }
                List actOperatingModeList = ClientUtil.executeQuery("getCustomerAccountOperatingType", operativeMap);
                if (actOperatingModeList != null && actOperatingModeList.size() > 0) {
                    HashMap actOperatingModeMap = (HashMap) actOperatingModeList.get(0);
                    if (actOperatingModeMap.containsKey("OPT_MODE_ID") && actOperatingModeMap.get("OPT_MODE_ID") != null) {
                        String actOperatingMode = CommonUtil.convertObjToStr(actOperatingModeMap.get("OPT_MODE_ID"));
                        if (actOperatingMode.equalsIgnoreCase("Jointly") || actOperatingMode.equalsIgnoreCase("Authorized Signatory") || actOperatingMode.equalsIgnoreCase("No.1 only") || actOperatingMode.equalsIgnoreCase("No.2 only")) {
                          ClientUtil.showAlertWindow("Operating Type : "+ actOperatingMode);
                        }
                    }
                }
            }
            if (rdoTransactionType_Credit.isSelected() && CommonUtil.convertObjToStr(observable.getProdType()).equals("OA")
                    && CommonUtil.convertObjToStr(transDetails.getAccountStatus()).equals(CommonConstants.COMPLETE_FREEZE)) {
                ClientUtil.displayAlert("This account has been freezed completely,\n cannot make transaction either debit or credit\n Freezed Amount Rs : " + transDetails.getFreezeAmount());
                txtAccNo.setText("");
                return;
            } else if (rdoTransactionType_Debit.isSelected() && CommonUtil.convertObjToStr(observable.getProdType()).equals("OA")) {
                if (CommonUtil.convertObjToStr(transDetails.getAccountStatus()).equals(CommonConstants.COMPLETE_FREEZE)) {
                    ClientUtil.displayAlert("This account has been freezed completely,\n cannot make transaction either debit or credit\n Freezed Amount Rs : " + transDetails.getFreezeAmount());
                    txtAccNo.setText("");
                    return;
                } else if (CommonUtil.convertObjToStr(transDetails.getAccountStatus()).equals(CommonConstants.PARTIAL_FREEZE)) {
                    ClientUtil.displayAlert("This account has been freezed partially,\n cannot make debit transaction\n Freezed Amount Rs : " + transDetails.getFreezeAmount());
                    //txtAccNo.setText("");
                    //return;
                }
            }
            //                } else {
            //                    ClientUtil.displayAlert("Invalid A/c Number or Closed Account...");
            //                }
            String act_num = observable.getTxtAccNo();
            HashMap inputMap = new HashMap();
            inputMap.put("ACCOUNTNO", act_num);
            chqBalList = ClientUtil.executeQuery("getMinBalance", inputMap);
            HashMap lockmap = new HashMap();
            lockmap.put("ACCOUNTNO", act_num);
            List lockList = ClientUtil.executeQuery("getLockStatusForAccounts", lockmap);
            lockmap = null;
            if (lockList != null && lockList.size() > 0) {
                lockmap = (HashMap) lockList.get(0);
                String lockStatus = CommonUtil.convertObjToStr(lockmap.get("LOCK_STATUS"));
                if (lockStatus.equals("Y") && rdoTransactionType_Credit.isSelected()) {
                    ClientUtil.displayAlert("Account is locked");
                    txtAccNo.setText("");
                }
            }
            if(getSelectedBranchID()!=null && !ProxyParameters.BRANCH_ID.equals(getSelectedBranchID())){
                Date selectedBranchDt = ClientUtil.getOtherBranchCurrentDate(getSelectedBranchID());
                Date currentDate = (Date) currDt.clone();
                //System.out.println("selectedBranchDt : "+selectedBranchDt + " currentDate : "+currentDate);
                if(selectedBranchDt == null){
                    ClientUtil.displayAlert("BOD is not completed for the selected branch " +"\n"+"Interbranch Transaction Not allowed");
                    txtAccNo.setText("");
                    return;
                }else if(DateUtil.dateDiff(currentDate, selectedBranchDt)<0){
                    ClientUtil.displayAlert("Application Date is different in the Selected branch " +"\n"+"Interbranch Transaction Not allowed");
                    txtAccNo.setText("");
                    return;
                }else {
                    System.out.println("Continue for interbranch trasactions ...");
                }
            }
            if (depBehavesLike.equals("RECURRING") && rdoTransactionType_Credit.isSelected() == true && viewType != AUTHORIZE) {
                HashMap dailyProdID = new HashMap();
                String prId = ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString();
                int freq = 0;
                dailyProdID.put("PID", prId);
                List dailyFrequency = ClientUtil.executeQuery("getDailyDepositFrequency", dailyProdID);
                if (dailyFrequency != null && dailyFrequency.size() > 0) {
                    HashMap dailyFreq = new HashMap();
                    dailyFreq = (HashMap) dailyFrequency.get(0);
                    String daily = CommonUtil.convertObjToStr(dailyFreq.get("DEPOSIT_FREQ"));
                    freq = CommonUtil.convertObjToInt(daily);
                    if (freq == 7) { //If Condition Added By Suresh R   11-Jul-2017
                        HashMap recurringMap = new HashMap();
                        txtAccNo.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
                        // Added by nithya on 22-12-2016
                        if (observable.getProdType().equals("TD") && CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")).lastIndexOf("_") == -1) {
                            txtAccNo.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")) + "_1");
                        }
                        String depNo = txtAccNo.getText();
//                 double penalAmt = CommonUtil.convertObjToDouble(transDetails.getPenalAmount()).doubleValue();
                        //System.out.println(" ####DepNo12123 :" + depNo);
                        if (depNo.contains("_")) {
                            depNo = depNo.substring(0, depNo.lastIndexOf("_"));
                        }
                        //System.out.println(" ####DepNo2222 :" + depNo);
                        recurringMap.put("DEPOSIT_NO", depNo.trim());
                        List lst = ClientUtil.executeQuery("getDepositAmountForRecurring", recurringMap);
                        if (lst != null && lst.size() > 0) {
                            recurringMap = (HashMap) lst.get(0);
                            double depAmount = CommonUtil.convertObjToDouble(recurringMap.get("DEPOSIT_AMT")).doubleValue();
                            // double receivablAmt=depAmount+penalAmt;//vv
                            //  System.out.println("penalAmt????@@>>>>"+penalAmt);
                            HashMap delayMap = new HashMap();
                            double delayAmt = 0.0;
                            delayMap.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString());
                            delayMap.put("DEPOSIT_AMT", recurringMap.get("DEPOSIT_AMT"));
                            lst = ClientUtil.executeQuery("getSelectDelayedRate", delayMap);
                            if (lst != null && lst.size() > 0) {
                                delayMap = (HashMap) lst.get(0);
                                delayAmt = CommonUtil.convertObjToDouble(delayMap.get("PENAL_INT")).doubleValue();
                                //System.out.println("delayAmt@@1111 in cash>>>>>" + delayAmt);
                            }
                            HashMap hmap = new HashMap();
                            String instalPendng = "";
                            double actualDelay = 0.0;
                            //   System.out.println("acctNo iv>>>"+acctNo);
                            //  String depNo = acctNo;
                            //System.out.println(" ####DepNo ivv in cash>>>>:" + depNo);
                            //depNo = depNo.substring(0, depNo.lastIndexOf("_"));
                            // System.out.println("######## BHEAVES iv in cash>>>>:" + depNo);
                            hmap.put("ACC_NUM", depNo);
                            hmap.put("CURR_DT", currDt.clone());
                            hmap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                            //Added by chithra for mantis:10319: Weekly RD customisation for Ollukkara bank
                            prId = ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString();
                            List list = null;
                            
                            if (depBehavesLike != null && depBehavesLike.equals("RECURRING")) {
                                dailyProdID = new HashMap();
                                dailyProdID.put("PID", prId);
                                dailyFrequency = ClientUtil.executeQuery("getDailyDepositFrequency", dailyProdID);
                                if (dailyFrequency != null && dailyFrequency.size() > 0) {
                                    dailyFreq = new HashMap();
                                    dailyFreq = (HashMap) dailyFrequency.get(0);
                                    daily = CommonUtil.convertObjToStr(dailyFreq.get("DEPOSIT_FREQ"));
                                    freq = CommonUtil.convertObjToInt(daily);
                                    if (freq == 7) {
                                        list = ClientUtil.executeQuery("getSelectInstalmentPendingForTransDetailsUiForWeek", hmap);
                                    } else {
                                        //Added by sreekrishnan for mantis 10452
                                        HashMap paramMap = new HashMap();
                                        List paramList = ClientUtil.executeQuery("getSelectParameterForRdPendingCalc", hmap);
                                        if (paramList != null && paramList.size() > 0) {
                                            paramMap = (HashMap) paramList.get(0);
                                            if (paramMap.containsKey("INCLUDE_FULL_MONTH") && !paramMap.get("INCLUDE_FULL_MONTH").equals("") && paramMap.get("INCLUDE_FULL_MONTH").equals("Y")) {
                                                list = ClientUtil.executeQuery("getSelectInstalmentPendingForTransDetailsUiWithMonthEnd", hmap);
                                            } else {
                                                list = ClientUtil.executeQuery("getSelectInstalmentPendingForTransDetailsUi", hmap);
                                            }
                                        } else {
                                            list = ClientUtil.executeQuery("getSelectInstalmentPendingForTransDetailsUi", hmap);
                                        }
                                    }
                                } else {
                                    //Added by sreekrishnan for mantis 10452
                                    HashMap paramMap = new HashMap();
                                    List paramList = ClientUtil.executeQuery("getSelectParameterForRdPendingCalc", hmap);
                                    if (paramList != null && paramList.size() > 0) {
                                        paramMap = (HashMap) paramList.get(0);
                                        if (paramMap.containsKey("INCLUDE_FULL_MONTH") && !paramMap.get("INCLUDE_FULL_MONTH").equals("") && paramMap.get("INCLUDE_FULL_MONTH").equals("Y")) {
                                            list = ClientUtil.executeQuery("getSelectInstalmentPendingForTransDetailsUiWithMonthEnd", hmap);
                                        } else {
                                            list = ClientUtil.executeQuery("getSelectInstalmentPendingForTransDetailsUi", hmap);
                                        }
                                    } else {
                                        list = ClientUtil.executeQuery("getSelectInstalmentPendingForTransDetailsUi", hmap);
                                    }
                                }

                            }
                            if (list != null && list.size() > 0) {
                                hmap = (HashMap) list.get(0);
                                instalPendng = CommonUtil.convertObjToStr(hmap.get("PENDING"));
                                actualDelay = (long) CommonUtil.convertObjToInt(instalPendng);
                                //  actualDelay1=actualDelay;
                                //System.out.println("actualDelay iv in cash>>>" + actualDelay);
                            }
                            //Commented by nithya on 12-07-2019 for KD 551 - Need to allow future installment for weekly RD
//                            if (freq == 7) {   //Added by chithra for mantis:10319: Weekly RD customisation for Ollukkara bank
//                                if (noofInstallment2 > actualDelay + 1) {
//                                    ClientUtil.showMessageWindow("No Future Receipts Allowed");
//                                    txtAmount.setText("");
//                                    return;
//                                }
//                            }
                            if (actualDelay > 0) {
                                HashMap whr = new HashMap();
                                whr.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
                                List irRDlst = ClientUtil.executeQuery("getIrregularRDApply", whr);
                                if (irRDlst != null && irRDlst.size() > 0) {
                                    HashMap singMap = (HashMap) irRDlst.get(0);
                                    if (singMap != null && singMap.containsKey("RD_IRREGULAR_INSTALLMENTS_DUE")) {
                                        actualDelay = actualDelay - CommonUtil.convertObjToDouble(singMap.get("RD_IRREGULAR_INSTALLMENTS_DUE"));
                                    }
                                }
                            }
                            double cummInst = 0.0;
                            if (actualDelay == noofInstallment2) {
                                cummInst = actualDelay * (actualDelay + 1) / 2;
                            } else {
                                double diff = actualDelay - noofInstallment2;
                                cummInst = (actualDelay * (actualDelay + 1) / 2) - (diff * (diff + 1) / 2);
                            }
                            //  double penal=0.0;
                            penal = depAmount * cummInst * delayAmt / 100;
                            String round = transDetails.getRound();
                            //System.out.println("penal in cashhhh??>>" + penal);


                            if (round.equals("NEAREST_VALUE")) {
                                penal = (double) getNearest((long) (penal * 100), 100) / 100;
                            } else if (round.equals("LOWER_VALUE")) {
                                penal = (double) roundOffLower((long) (penal * 100), 100) / 100;
                            } else if (round.equals("HIGHER_VALUE")) {
                                penal = (double) higher((long) (penal * 100), 100) / 100;
                            } else {
                                //system.out.println(" in no round33333");
                                penal = new Double(penal);
                                //system.out.println("maturityAmt 3333" + maturityAmt);
                            }
                            //System.out.println("penalllllllllllllllllll" + penal);
                            double receivablAmt = depAmount * noofInstallment2;
                            //  double totAmt1=0.0;
                            System.out.println("#### noofInstallment : "+ noofInstallment2);
                            if (penal > 0) {
                                totAmt1 = receivablAmt + penal;
                            } else {
                                totAmt1 = receivablAmt;
                            }
                            //System.out.println("totAmt1???>>>" + totAmt1);
                            txtAmount.setText(CommonUtil.convertObjToStr(totAmt1));

                            //System.out.println("txtAmount.getText??>>>??>>>" + txtAmount.getText());
                        }
                    }
                }
            }
            
            if (ACCOUNTNO.length() > 0) {
                if (observable.checkAcNoWithoutProdType(ACCOUNTNO)) {
                    setSelectedBranchID(observable.getSelectedBranchID());
                }
            }
            String depNum = txtAccNo.getText();
           //Added by chithra for mantis:10345: New Weekly Deposit Schemes For Pudukad SCB 
            if (ACCOUNTNO != null && ACCOUNTNO.length() > 0 && depBehavesLike != null && depBehavesLike.equals("DAILY")) {
                chkWeeklyDepositReceiptDt(ACCOUNTNO);
                String prId = ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString();

                HashMap dailyProdID = new HashMap();
                dailyProdID.put("PID", prId);
                List dailyFrequency = ClientUtil.executeQuery("getDailyDepositFrequency", dailyProdID);
                if (dailyFrequency != null && dailyFrequency.size() > 0) {
                    HashMap dailyFreq = new HashMap();
                    dailyFreq = (HashMap) dailyFrequency.get(0);
                    String daily = CommonUtil.convertObjToStr(dailyFreq.get("DEPOSIT_FREQ"));
                    String weekly_spec = CommonUtil.convertObjToStr(dailyFreq.get("WEEKLY_SPEC"));
                    String variableAmt = CommonUtil.convertObjToStr(dailyFreq.get("INSTALL_RECURRING_DEPAC"));
                    int freq = CommonUtil.convertObjToInt(daily);
                    if (freq == 7 && weekly_spec.equals("Y")) {
                        dailyProdID.put("PROD_ID", prId);
                        List maxAmtList = ClientUtil.executeQuery("getDepProdDetails", dailyProdID);
                        if (maxAmtList != null && maxAmtList.size() > 0) {
                            HashMap maxAmtMap = new HashMap();
                            maxAmtMap = (HashMap) maxAmtList.get(0);
                            if (maxAmtMap != null && maxAmtMap.containsKey("MAX_DEPOSIT_AMT")) {
                                double maxAmt = CommonUtil.convertObjToDouble(maxAmtMap.get("MAX_DEPOSIT_AMT"));
                                if (variableAmt != null && variableAmt.equalsIgnoreCase("F")) {
                                    txtAmount.setText(CommonUtil.convertObjToStr(maxAmtMap.get("MAX_DEPOSIT_AMT")));
                                    txtAmount.setEnabled(false);
                                } else {
                                    txtAmount.setEnabled(true);
                                }
                            }
                        }
                    }
                }
            }
            //Added by sreekrishnan for borrowing
            if (observable.getProdType().equals("AB")) {
                //System.out.println("transDetails.getOtherBankAccountMap()@#$@$@#$"+transDetails.getOtherBankAccountMap());
                observable.setOtherBankMap(transDetails.getOtherBankAccountMap());
            }
           //boolean flag = isAccountNumberExsistInAuthList(CommonUtil.convertObjToStr(txtAccNo.getText()));
           isAccountNumberExsistInAuthList(CommonUtil.convertObjToStr(txtAccNo.getText()));
//            if (flag == true) {
//                txtAccNo.setText("");
//            }
            isAccountNumberLinkedwithATMProd(CommonUtil.convertObjToStr(txtAccNo.getText()));
        } else if (viewType == ACCTHDID) {
            String acHdId = hash.get("A/C HEAD").toString();
            String bankType = CommonConstants.BANK_TYPE;
            //System.out.println("bankType" + bankType);
            String customerAllow = "";
            String hoAc = "";
            cboProdId.setSelectedItem("");
            this.txtAccNo.setText("");
            txtAccHdId.setText(acHdId);

            HashMap hmap = new HashMap();
            hmap.put("ACHEAD", acHdId);
            List list = ClientUtil.executeQuery("getCustomerAlloowProperty", hmap);
            if (list != null && list.size() > 0) {
                hmap = (HashMap) list.get(0);
                customerAllow = CommonUtil.convertObjToStr(hmap.get("ALLOW_CUSTOMER_ACNUM"));
                hoAc = CommonUtil.convertObjToStr(hmap.get("HO_ACCT"));
            }
            if (bankType.equals("DCCB")) {
                if (hoAc.equals("Y")) {
                    btnOrgOrResp.setVisible(true);
                    observable.setHoAccount(true);
                } else {
                    observable.setHoAccount(false);
                    btnOrgOrResp.setVisible(false);
                }
            }
            //system.out.println("customerAllow>>>>" + customerAllow);
            if (customerAllow.equals("Y")) {
                //system.out.println("innnnn");
                // CInternalFrame frm = new CInternalFrame();
                // frm = new com.see.truetransact.ui.transaction.cash.GLAccountNumberListUI(this);
                com.see.truetransact.ui.transaction.cash.GLAccountNumberListUI frm = new com.see.truetransact.ui.transaction.cash.GLAccountNumberListUI(this);
                frm.setSelectedBranchID(getSelectedBranchID());
                //frm.setSize(1000,1000);
                TrueTransactMain.showScreen(frm);

//               final CInternalFrame frame = new CInternalFrame();
//               CDesktopPane desktop = new CDesktopPane();
//               glAccountNumberListUI=new GLAccountNumberListUI();
//        //frame.setLocation(xOffset * openFrameCount, yOffset * openFrameCount);
//        frame.setSize(200, 100);
//        frame.setVisible(true);
//        frame.getContentPane().add(glAccountNumberListUI);
//        desktop.add(frame);

//                GLAccountNumberListUI glAccNo = new GLAccountNumberListUI();
//                glAccNo.show();
//                glAccNo.setVisible(true);
//                String AccNo = COptionPane.showInputDialog(this, "Enter Acc no");
//                hmap.put("ACC_NUM", AccNo);
//                List chkList = ClientUtil.executeQuery("checkAccStatus", hmap);
//                if (chkList != null && chkList.size() > 0) {
//                    hmap = (HashMap) chkList.get(0);
//                    observable.setLblAccName(CommonUtil.convertObjToStr(hmap.get("NAME")));
//                    observable.setTxtAccNo(AccNo);
//                    observable.setClosedAccNo(AccNo);
//                } else {
//                    ClientUtil.displayAlert("Invalid Account number");
//                    txtAccHdId.setText("");
//                    return;
//                }
            }
            observable.setCr_cash(hash.get("CR_CASH").toString());
            observable.setDr_cash(hash.get("DR_CASH").toString());
            observable.setBalanceType((String) hash.get("BALANCETYPE"));
            observable.setTxtAccHd(acHdId);
            observable.setCboProdType((String) cboProdType.getSelectedItem());
           //System.out.println("observable.getProdType(), acHdId " + observable.getProdType() + ":" + acHdId);
            transDetails.setTransDetails(observable.getProdType(), ProxyParameters.BRANCH_ID, acHdId);
            transDetails.setProductId(observable.getCboProdId());//Added by kannan
            transDetails.setSourceFrame(this);
            observable.ttNotifyObservers();            
            this.setRadioButtons();            
        } else if (viewType == PAN_NUM) {
            String panNumber = CommonUtil.convertObjToStr(hash.get("PAN_NUMBER"));
            txtPanNo.setText(panNumber);
            //                observable.setTxtPanNo(panNumber);
        }
        if (rdoTransactionType_Credit.isSelected() && observable.getLinkMap() != null && observable.getLinkMap().containsKey("AS_CUSTOMER_COMES") && observable.getLinkMap().get("AS_CUSTOMER_COMES") != null) {
            observable.setAsAnWhenCustomer(CommonUtil.convertObjToStr(observable.getLinkMap().get("AS_CUSTOMER_COMES")));
        } else {
            observable.setAsAnWhenCustomer("");
        }
        if (termDepositUI != null && flagDepositAuthorize == true) {
            if (termDepositUI.getAuthorizeStatus().equals("AUTHORIZE_BUTTON")) {
                btnAuthorize.setEnabled(true);
                btnReject.setEnabled(false);
                btnView.setEnabled(false);
                btnSave.setEnabled(false);
                btnCancel.setEnabled(false);
            }
            if (termDepositUI.getAuthorizeStatus().equals("REJECT_BUTTON")) {
                btnReject.setEnabled(true);
                btnAuthorize.setEnabled(false);
                btnView.setEnabled(false);
                btnSave.setEnabled(false);
                btnCancel.setEnabled(false);
            }
        }
        if (termDepositUI == null) {
            if (observable.getProdType().equals("TD")) {
                if (rdoTransactionType_Credit.isSelected()) {
                    if (depAmt != 0 && (hash.get("PRODUCTTYPE").equals("RECURRING") || hash.get("PRODUCTTYPE").equals("DAILY")||hash.get("PRODUCTTYPE").equals("BENEVOLENT")||hash.get("PRODUCTTYPE").equals("THRIFT"))) {
                        txtAmount.setEnabled(true);
                    } else {
                        txtAmount.setEnabled(false);
                    }
                }
                if (rdoTransactionType_Debit.isSelected()) {
                    if (depBehavesLike.equals("DAILY") && depPartialWithDrawalAllowed.equals("Y")) {
                        txtAmount.setEnabled(true);
                    } else {
                        if(observable.getSpecialRD().equalsIgnoreCase("Y")){ // added by nithya on 28-02-2020  for KD-1535
                          txtAmount.setEnabled(true);   
                        }else{
                          txtAmount.setEnabled(false);
                        }
                    }
                }
                if (viewType == EDIT || viewType == LINK_BATCH || viewType == LINK_BATCH_TD) {
                    ClientUtil.enableDisable(this, false);
                    btnCancel.setEnabled(true);
                    btnSave.setEnabled(false);
                }
            }
        }
        if ((observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT)
                && !observable.getProdType().equals("") && observable.getProdType().equals("GL")) {
            HashMap acctMap = new HashMap();
            acctMap.put("ACCT_HEAD", observable.getTxtAccHd());
            List head = ClientUtil.executeQuery("getSelectReconcileYesNO", acctMap);
            if (head != null && head.size() > 0) {
                acctMap = (HashMap) head.get(0);
                if (!acctMap.get("RECONS").equals("") && acctMap.get("RECONS").equals("Y")) {
                    if (acctMap.get("BALANCETYPE").equals("DEBIT") && rdoTransactionType_Credit.isSelected() == true) {
                        reconcilebtnDisable = true;
                        observable.setReconcile("N");
                        btnCurrencyInfo.setVisible(true);
                        btnCurrencyInfo.setEnabled(true);
                    } else if (acctMap.get("BALANCETYPE").equals("CREDIT") && rdoTransactionType_Debit.isSelected() == true) {
                        reconcilebtnDisable = true;
                        observable.setReconcile("N");
                        btnCurrencyInfo.setVisible(true);
                        btnCurrencyInfo.setEnabled(true);
                    } else if (acctMap.get("BALANCETYPE").equals("DEBIT") && rdoTransactionType_Debit.isSelected() == true) {
                        observable.setReconcile("Y");
                        btnCurrencyInfo.setVisible(false);
                        btnCurrencyInfo.setEnabled(false);
                    } else if (acctMap.get("BALANCETYPE").equals("CREDIT") && rdoTransactionType_Credit.isSelected() == true) {
                        observable.setReconcile("Y");
                        btnCurrencyInfo.setVisible(false);
                        btnCurrencyInfo.setEnabled(false);
                    }
                    observable.setBalanceType(CommonUtil.convertObjToStr(acctMap.get("BALANCETYPE")));
                } else {
                    btnCurrencyInfo.setVisible(false);
                    btnCurrencyInfo.setEnabled(false);
                }
            }
        } else {
            btnCurrencyInfo.setVisible(false);
            btnCurrencyInfo.setEnabled(false);
        }
        if(!observable.getProdType().equals("") && observable.getProdType().equals("GL")){
            observable.setSelectedBranchID(ProxyParameters.BRANCH_ID);
            setSelectedBranchID(ProxyParameters.BRANCH_ID);
        }
         if (viewType == ACCNO && observable.getProdType().equals("TL")) {
             HashMap retmpap = setRepaymentData();
             if (retmpap != null && retmpap.size() > 0) {
                 transDetails.setRepayData(retmpap);
             }else{
                transDetails.setRepayData(null); 
             }
         }else{
                transDetails.setRepayData(null); 
         }
        if (hash.containsKey("FROM_AUTHORIZE_LIST_UI")) {
            btnDelete.setEnabled(false);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(true);
            btnView.setEnabled(false);
        }
        if (hash.containsKey("FROM_MANAGER_AUTHORIZE_LIST_UI")) {
            btnDelete.setEnabled(false);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(true);
            btnView.setEnabled(false);
        }
        if (hash.containsKey("FROM_CASHIER_AUTHORIZE_LIST_UI")) {
            btnDelete.setEnabled(false);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(true);
            btnView.setEnabled(false);
        }
        String prod = "";
        if (cboProdId.getSelectedItem() != null) {
            prod = cboProdId.getSelectedItem().toString();
        }
        if (!prod.equals("")) {
            transDetails.setProdId(prod);
        }
        if (viewType == 15) {
            String memNo = "";
            viewType = -1;
            if (hash.containsKey("MEMBER_NO")) {
                memNo = hash.get("MEMBER_NO").toString();
                getLiabilityDetails(memNo);
            }
        }
        if (hash.containsKey("LOAN_LIABILITY")) {
            if (hash.get("ACT_NUM").toString() != null) {
                txtAccNo.setText(hash.get("ACT_NUM").toString());
                txtAccNoActionPerformed();
            }
        }
        if (rejectFlag == 1) {
            btnReject.setEnabled(false);
        }
        transDetails.setAccDesc(txtAccHdId.getText());
        
       if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT){
           if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
               btnAuthorize.setEnabled(true);
               btnAuthorize.setFocusable(true);
               btnAuthorize.setFocusPainted(true);
               btnAuthorize.requestFocus(true);
           }
         String prodTye = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdType.getModel()).getKeyForSelected());
        //added by sreekrishnan for 0003866
         if(rdoTransactionType_Debit.isSelected() && prodTye.equals("OA")){
             HashMap debitMap = new HashMap();
             debitMap.put("ACT_NUM", CommonUtil.convertObjToStr(txtAccNo.getText()));
             List debitlist = ClientUtil.executeQuery("getDailyMaxTransaction", debitMap);
             debitMap = null;
             Date debitDate = new Date();
             if (debitlist != null && debitlist.size() > 0) {
                debitMap = (HashMap) debitlist.get(0);
                if(debitMap!=null && debitMap.size()>0){
                    debitDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(debitMap.get("FINAL_DT")));
                    //System.out.println("debitDate###"+debitDate);
                    if(!debitDate.equals("")){
                        if (debitDate.after((Date) currDt.clone()) || debitDate.equals((Date) currDt.clone())) {
                            observable.setDebitWithdrwalCharge(true);
                        }else{
                            observable.setDebitWithdrwalCharge(false);
                        }
                    }else{
                          observable.setDebitWithdrwalCharge(false);
                   }
                }else{
                      observable.setDebitWithdrwalCharge(false);
               }
            }else{
                 observable.setDebitWithdrwalCharge(false);
            }
         }
         //System.out.println("$#@$#@$@#$#@$#@$@3 IN AUTHORIZEEEE"+observable.isDebitWithdrwalCharge());
       }
       if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
           if(depBehavesLike != null && depBehavesLike.equals("DAILY")){
               //System.out.println("nithya..........");
               txtAmount.setEnabled(true);
           }
       }
     }

    public long getNearest(long number, long roundingFactor) {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor % 2) != 0) {
            roundingFactorOdd += 1;
        }
        long mod = number % roundingFactor;
        if ((mod <= (roundingFactor / 2)) || (mod <= (roundingFactorOdd / 2))) {
            return lower(number, roundingFactor);
        } else {
            return higher(number, roundingFactor);
        }
    }

    public long lower(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        return number - mod;
    }

    public long higher(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        if (mod == 0) {
            return number;
        }
        return (number - mod) + roundingFactor;
    }

    public long roundOffLower(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        return number - mod;
    }

    public void fillAccNo(String accNo, String prodId, String prodType) {
        //System.out.println("in fill accno>>>" + accNo);
        //System.out.println("prodId in fillaccno>>>" + prodId);
        //System.out.println("prodType in fillaccno>>>" + prodType);
        accNo1 = accNo;
        prodIdforgl = prodId;
        prodtypeforgl = prodType;
        lblAccNoGl.setVisible(true);
        lblAccNoGl.setEnabled(true);
        if (accNo1 != null) {
            lblAccNoGl.setText(accNo1);
            observable.setAccNumGlProdType(prodType);
            //System.out.println("lblAccNoGl.getText>>>" + lblAccNoGl.getText());
//    observable.setAccountName(lblAccNoGl.getText().toString());
//    lblAccName.setText(observable.getLblAccName());
//    lblHouseName.setText(observable.getLblHouseName());

            //        //To set the  Name of the Account Holder...
            String pType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();

            //   System.out.println("observable.getLblAccName()>>>"+observable.getLblAccName());
            String prevActNum = observable.getLblAccName();
            //System.out.println("pType>>>" + pType);
            if (pType.length() > 0) {
                if (pType.equals("GL")) {
                    setAccountName1(lblAccNoGl.getText().toString());
                    //   System.out.println("observable.getLblAccName()>>>"+observable.getLblAccName());
                    //  lblAccName.setText(observable.getLblAccName());
                    //  System.out.println("observable.getLblHouseName()>>>"+observable.getLblHouseName());
                    //  lblHouseName.setText(observable.getLblHouseName());
                    //    observable.setAccountHead();
                    //    System.out.println("observable.getLblAccHdDesc()@@>>>"+observable.getLblAccHdDesc());
                    //  lblAccHdDesc.setText(observable.getLblAccHdDesc());
                }
            }


        }
    }

    public void setAccountName1(String AccountNo) {
        HashMap resultMap = new HashMap();
        final HashMap accountNameMap = new HashMap();
        List resultList = new ArrayList();
        try {
            if (!prodtypeforgl.equals("")) {
                accountNameMap.put("ACC_NUM", AccountNo);
                String pID = !prodtypeforgl.equals("GL") ? prodIdforgl.toString() : "";
//                if(prodtypeforgl.equals("GL") && AccountNo.length()>0){
//                    resultList = ClientUtil.executeQuery("getAccountNumberNameTL",accountNameMap);
//                } else {
                resultList = ClientUtil.executeQuery("getAccountNumberName" + prodtypeforgl, accountNameMap);
                List custHouseNameList = ClientUtil.executeQuery("getCustomerHouseName", accountNameMap);
                if (custHouseNameList != null && custHouseNameList.size() > 0) {
                    HashMap dataMap = new HashMap();
                    dataMap = (HashMap) custHouseNameList.get(0);
                    lblHouseName.setText(CommonUtil.convertObjToStr(dataMap.get("HOUSE_NAME")));
                    //System.out.println("lblHouseName>>>" + lblHouseName.getText());
                }
//                }
                if (resultList != null && resultList.size() > 0) {
                    if (!prodtypeforgl.equals("GL") && !prodtypeforgl.equals("SH")) {
                        HashMap dataMap = new HashMap();
                        accountNameMap.put("BRANCH_ID", getSelectedBranchID());
                        List lst = (List) ClientUtil.executeQuery("getProdIdForActNo" + prodtypeforgl, accountNameMap);
                        if (lst != null && lst.size() > 0) {
                            dataMap = (HashMap) lst.get(0);
                        }
                        if (dataMap.get("PROD_ID").equals(pID)) {
                            resultMap = (HashMap) resultList.get(0);
                        }
                    } else {
                        resultMap = (HashMap) resultList.get(0);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (resultMap.containsKey("CUSTOMER_NAME")) {
            lblAccName.setText(CommonUtil.convertObjToStr(resultMap.get("MEMBERSHIP_NO")) + ' ' + ' ' + resultMap.get("CUSTOMER_NAME").toString());
            //System.out.println("lblAccName45>>>>>>" + lblAccName.getText());
        } else {
            lblAccName.setText("");
        }
        //            if(resultList != null){
        //                final HashMap resultMap = (HashMap)resultList.get(0);
        //                setLblAccName(resultMap.get("CUSTOMER_NAME").toString());
        //            } else {
        //                setLblAccName("");
        //            }
        //        }catch(Exception e){
        //
        //        }
    }

    public void setAccountName2(String AccountNo) {
        HashMap resultMap = new HashMap();
        final HashMap accountNameMap = new HashMap();
        List resultList = new ArrayList();
        try {
//            if (!prodtypeforgl.equals("")) {
            accountNameMap.put("ACC_NUM", AccountNo);
//                String pID = !prodtypeforgl.equals("GL") ? prodIdforgl.toString() : "";
//                if(prodtypeforgl.equals("GL") && AccountNo.length()>0){
            resultList = ClientUtil.executeQuery("getAccountNumberNameGL", accountNameMap);
//                } else {
            //System.out.println("resultList.size()???@@>>>" + resultList.size());
            if (resultList == null || resultList.size() <= 0) {
                //System.out.println("kmkmf");
                resultList = ClientUtil.executeQuery("getSelectCustomerNameAndMemNoForAccountLevelEntries", accountNameMap);
            }
            List custHouseNameList = ClientUtil.executeQuery("getCustomerHouseName", accountNameMap);
            if (custHouseNameList != null && custHouseNameList.size() > 0) {
                HashMap dataMap = new HashMap();
                dataMap = (HashMap) custHouseNameList.get(0);
                lblHouseName.setText(CommonUtil.convertObjToStr(dataMap.get("HOUSE_NAME")));
                //System.out.println("lblHouseName>>>" + lblHouseName.getText());
            }
//                }
            if (resultList != null && resultList.size() > 0) {
//                    if(!prodtypeforgl.equals("GL") && !prodtypeforgl.equals("SH")) {
//                        HashMap dataMap = new HashMap();
//                        accountNameMap.put("BRANCH_ID", getSelectedBranchID());
//                        List lst = (List) ClientUtil.executeQuery("getProdIdForActNo"+prodtypeforgl,accountNameMap);
//                        if(lst != null && lst.size() > 0)
//                            dataMap = (HashMap) lst.get(0);
//                        if(dataMap.get("PROD_ID").equals(pID)){
//                            resultMap = (HashMap)resultList.get(0);
//                        }
//                    } else {
                resultMap = (HashMap) resultList.get(0);
//                    }
            }
            //System.out.println("resultMap" + resultMap);
            if (resultMap.containsKey("CUSTOMER_NAME") && resultMap.get("CUSTOMER_NAME") != null) {
                lblAccName.setText(CommonUtil.convertObjToStr(resultMap.get("MEMBERSHIP_NO")) + ' ' + ' ' + resultMap.get("CUSTOMER_NAME").toString());
                //System.out.println("lblAccName45>>2222>>>>" + lblAccName.getText());
            } else {
                lblAccName.setText("");
            }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //            if(resultList != null){
        //                final HashMap resultMap = (HashMap)resultList.get(0);
        //                setLblAccName(resultMap.get("CUSTOMER_NAME").toString());
        //            } else {
        //                setLblAccName("");
        //            }
        //        }catch(Exception e){
        //
        //        }
    }

    public void getLiabilityDetails(String MemNo) {
        HashMap paramMap = new HashMap();
        paramMap.put("MemberNo", MemNo);
        ArrayList liabilityList = new ArrayList();
        List aList = ClientUtil.executeQuery("getMemberLiability", paramMap);
        //in future please change this map statement accordingly to get MemberLiability//jiv
        if (aList != null && aList.size() > 0) {
            for (int i = 0; i < aList.size(); i++) {
                HashMap hMap = (HashMap) aList.get(i);
                liabilityList.add(hMap);
            }

            new LoanLiabilityUI(this, paramMap).show();
        }
    }

    private double setEMIAmount() {
        HashMap allAmtMap = new HashMap();
        double totEmiAmount = 0.0;
        allAmtMap = observable.getALL_LOAN_AMOUNT();
        if (allAmtMap != null && allAmtMap.size() > 0) {
            if (allAmtMap.containsKey("POSTAGE CHARGES") && CommonUtil.convertObjToDouble(allAmtMap.get("POSTAGE CHARGES")).doubleValue() > 0) {
                totEmiAmount = CommonUtil.convertObjToDouble(allAmtMap.get("POSTAGE CHARGES")).doubleValue();
            }
            if (allAmtMap.containsKey("ARBITRARY CHARGES") && CommonUtil.convertObjToDouble(allAmtMap.get("ARBITRARY CHARGES")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("ARBITRARY CHARGES")).doubleValue();
            }
            if (allAmtMap.containsKey("RECOVERY CHARGES") && CommonUtil.convertObjToDouble(allAmtMap.get("RECOVERY CHARGES")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("RECOVERY CHARGES")).doubleValue();
            }
             if (allAmtMap.containsKey("MEASUREMENT CHARGES") && CommonUtil.convertObjToDouble(allAmtMap.get("MEASUREMENT CHARGES")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("MEASUREMENT CHARGES")).doubleValue();
            }
            if (allAmtMap.containsKey("LEGAL CHARGES") && CommonUtil.convertObjToDouble(allAmtMap.get("LEGAL CHARGES")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("LEGAL CHARGES")).doubleValue();
            }
            if (allAmtMap.containsKey("INSURANCE CHARGES") && CommonUtil.convertObjToDouble(allAmtMap.get("INSURANCE CHARGES")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("INSURANCE CHARGES")).doubleValue();
            }
            if (allAmtMap.containsKey("NOTICE CHARGES") && CommonUtil.convertObjToDouble(allAmtMap.get("NOTICE CHARGES")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("NOTICE CHARGES"));
            }
            if (allAmtMap.containsKey("MISCELLANEOUS CHARGES") && CommonUtil.convertObjToDouble(allAmtMap.get("MISCELLANEOUS CHARGES")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("MISCELLANEOUS CHARGES")).doubleValue();
            }
            if (allAmtMap.containsKey("EXECUTION DECREE CHARGES") && CommonUtil.convertObjToDouble(allAmtMap.get("EXECUTION DECREE CHARGES")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("EXECUTION DECREE CHARGES")).doubleValue();
            }
            if (allAmtMap.containsKey("ADVERTISE CHARGES") && CommonUtil.convertObjToDouble(allAmtMap.get("ADVERTISE CHARGES")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("ADVERTISE CHARGES")).doubleValue();
            }
            if (allAmtMap.containsKey("PENAL_INT") && CommonUtil.convertObjToDouble(allAmtMap.get("PENAL_INT")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("PENAL_INT")).doubleValue();
            }
            if (allAmtMap.containsKey("CURR_MONTH_INT") && CommonUtil.convertObjToDouble(allAmtMap.get("CURR_MONTH_INT")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("CURR_MONTH_INT")).doubleValue();
            }
            if (allAmtMap.containsKey("CURR_MONTH_PRINCEPLE") && CommonUtil.convertObjToDouble(allAmtMap.get("CURR_MONTH_PRINCEPLE")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("CURR_MONTH_PRINCEPLE")).doubleValue();
            }
            if (allAmtMap.containsKey("OTHER_CHARGES") && CommonUtil.convertObjToDouble(allAmtMap.get("OTHER_CHARGES")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("OTHER_CHARGES")).doubleValue();
            }
            if (allAmtMap.containsKey("EA_COST") && CommonUtil.convertObjToDouble(allAmtMap.get("EA_COST")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("EA_COST")).doubleValue();
            }
            if (allAmtMap.containsKey("EA_EXPENCE") && CommonUtil.convertObjToDouble(allAmtMap.get("EA_EXPENCE")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("EA_EXPENCE")).doubleValue();
            }
            if (allAmtMap.containsKey("ARC_COST") && CommonUtil.convertObjToDouble(allAmtMap.get("ARC_COST")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("ARC_COST")).doubleValue();
            }
            if (allAmtMap.containsKey("ARC_EXPENCE") && CommonUtil.convertObjToDouble(allAmtMap.get("ARC_EXPENCE")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("ARC_EXPENCE")).doubleValue();
            }
            if (allAmtMap.containsKey("EP_COST") && CommonUtil.convertObjToDouble(allAmtMap.get("EP_COST")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("EP_COST")).doubleValue();
            }
            if (allAmtMap.containsKey("EP_EXPENCE") && CommonUtil.convertObjToDouble(allAmtMap.get("EP_EXPENCE")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("EP_EXPENCE")).doubleValue();
            }
            // Added by nithya for 0008470 : overdue interest for EMI Loans
            if (allAmtMap.containsKey("EMI_OVERDUE_CHARGE") && allAmtMap.get("EMI_OVERDUE_CHARGE") != null && CommonUtil.convertObjToDouble(allAmtMap.get("EMI_OVERDUE_CHARGE")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("EMI_OVERDUE_CHARGE")).doubleValue();                
            }
            if(serviceTax_Map != null && serviceTax_Map.containsKey("TOT_TAX_AMT") && serviceTax_Map.get("TOT_TAX_AMT") != null){
               totEmiAmount += CommonUtil.convertObjToDouble(serviceTax_Map.get("TOT_TAX_AMT")).doubleValue();  
            }
            
            if (allAmtMap.containsKey("KOLEFIELD EXPENSE") && allAmtMap.get("KOLEFIELD EXPENSE") != null && CommonUtil.convertObjToDouble(allAmtMap.get("KOLEFIELD EXPENSE")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("KOLEFIELD EXPENSE")).doubleValue();
            } 
            
            if (allAmtMap.containsKey("KOLEFIELD OPERATION") && allAmtMap.get("KOLEFIELD OPERATION") != null && CommonUtil.convertObjToDouble(allAmtMap.get("KOLEFIELD OPERATION")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("KOLEFIELD OPERATION")).doubleValue();
            }
            

        }
        return totEmiAmount;
    }

    private HashMap interestCalculationTLAD(String accountNo, long noOfInstallment) {
        HashMap map = new HashMap();
        HashMap hash = null;
        try {
            String prod_id = "";
            map.put("ACT_NUM", accountNo);
            //		if((ComboBoxModel)cboProdId.getModel()!=null)
            //                if((((ComboBoxModel)cboProdId.getModel()).getKeyForSelected().toString())!=null)
            //                    prod_id=((ComboBoxModel)cboProdId.getModel()).getKeyForSelected().toString();
            map.put("PROD_ID", prod_id);
            map.put("TRANS_DT", currDt.clone());
            map.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
            map.put("INT_CALC_FROM_SCREEN","INT_CALC_FROM_SCREEN");
            String mapNameForCalcInt = "IntCalculationDetail";
            if (observable.getProdType().equals("AD")) {
                mapNameForCalcInt = "IntCalculationDetailAD";
            }
            List lst = ClientUtil.executeQuery(mapNameForCalcInt, map);
            //System.out.println("map before intereest### 2134324324---->" + map);
            if (lst != null && lst.size() > 0) {
                hash = (HashMap) lst.get(0);
                 if (hash.get("AS_CUSTOMER_COMES") != null && hash.get("AS_CUSTOMER_COMES").equals("N")) {
                    hash = new HashMap();
                    return hash;
                }
                map.put("BRANCH_ID", getSelectedBranchID());
                map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                if (noOfInstallment > 0) {
                    map.put("NO_OF_INSTALLMENT", new Long(noOfInstallment));
                }

                //                    InterestCalculationTask interestcalTask=new InterestCalculationTask(header);
                map.putAll(hash);
                map.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");
                map.put("CURR_DATE", observable.getCurrentDate());
                //                    hash =interestcalTask.interestCalcTermLoanAD(map);
                observable.setAsAnWhenCustomer(CommonUtil.convertObjToStr(map.get("AS_CUSTOMER_COMES")));
                hash = observable.loanInterestCalculationAsAndWhen(map);
                if (hash == null) {
                    hash = new HashMap();
                }
                hash.putAll(map);
                hash.put("AS_CUSTOMER_COMES", map.get("AS_CUSTOMER_COMES"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return hash;
    }
    //To enable and/or Disable buttons(folder)...

    private void enableDisableButtons(boolean yesno) {
        btnAccNo.setEnabled(yesno);
        btnAccHdId.setEnabled(yesno);
        btnCurrencyInfo.setEnabled(yesno);
        btnDenomination.setEnabled(yesno);
        btnPanNo.setEnabled(yesno);
    }

    public void getUserDesignation() {
        HashMap desigMap = new HashMap();
        desigMap.put("USER", TrueTransactMain.USER_ID);
        List lst = ClientUtil.executeQuery("getUserDesignation", desigMap);
        desigMap = null;
        if (lst != null && lst.size() > 0) {
            desigMap = (HashMap) lst.get(0);
            designation = CommonUtil.convertObjToStr(desigMap.get("DESIGNATION"));
        }
    }

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);//Sets the Action Type to be performed...
        observable.resetForm();                 // Reset the fields in the UI to null...
        observable.resetLable();                // Reset the Editable Lables in the UI to null...
        setMode(ClientConstants.ACTIONTYPE_NEW);
        ClientUtil.enableDisable(this, true);   // Enables the panel...
        setButtonEnableDisable();               // Enables or Disables the buttons and menu Items depending on their previous state...
        observable.setStatus();                 // To set the Value of lblStatus...
        enableDisableButtons(true);                 // To enable the buttons(folder) in the UI...
        observable.setInitiatorChannelValue();  // To Set Initiator Type as Cashier...
        setModified(true);
        btnDelete.setEnabled(false);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        btnPanNo.setEnabled(false);
        observable.setCbmProdId("");
        cboProdId.setModel(observable.getCbmProdId());
        btnAccHdId.setEnabled(false);
        btnAccNo.setEnabled(false);
        txtAccNo.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID));
        btnCust.setEnabled(true);
//        txtAccNoFocusLost(null);
        //txtAmountFocusLost(null);
        txtAmountKeyPressed(null);
        lblAccNoGl.setText("");
        //Added By Suresh
        rdoTransactionType_Credit.setSelected(true);
        rdoTransactionType_CreditActionPerformed(null);
        observable.setAccNumGl("");
        observable.setAccNumGlProdType("");
        observable.setIsTransaction(false);
        accNo1 = "";
        noofInstallment2 = 0;
        transDetails.setRepayData(null);
        
    }//GEN-LAST:event_btnNewActionPerformed

    // To set the Text Boxes for Account No. and InitiatorChannel, nonEditable...
    private void textDisable() {
        txtInitiatorChannel.setEnabled(false);  //To make this textBox non editable...
        txtAccNo.setEnabled(false);             //To make this textBox non editable...
        //(new MandatoryDBCheck()).setComponentInit(getClass().getName(), panCashTransaction);
    }

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

	private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
            //System.exit(0);
            this.dispose();
    }//GEN-LAST:event_exitForm

    private void btnOrgOrRespActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOrgOrRespActionPerformed
        // TODO add your handling code here:
        String transType = "";


        boolean debit = rdoTransactionType_Debit.isSelected();

        boolean credit = rdoTransactionType_Credit.isSelected();
        if (debit) {
            transType = CommonConstants.DEBIT;
        } else if (credit) {
            transType = CommonConstants.CREDIT;
        }
        double transViewAmount = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
        if (credit) {
            vieworgOrRespUI = new ViewOrgOrRespUI(transViewAmount, transType, this);
            com.see.truetransact.ui.TrueTransactMain.showScreen(vieworgOrRespUI);
        } else if (debit) {
            viewRespUI = new ViewRespUI(transViewAmount, transType, this);
            com.see.truetransact.ui.TrueTransactMain.showScreen(viewRespUI);
        }
//        new ViewOrgOrRespUI(transViewAmount,transType,this).show();  
    }//GEN-LAST:event_btnOrgOrRespActionPerformed

private void btnCustActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustActionPerformed
// TODO add your handling code here:
    if (rdoTransactionType_Debit.isSelected() || rdoTransactionType_Credit.isSelected()) {
        viewType = 15;
        HashMap sourceMap = new HashMap();
        new CheckCustomerIdUI(this, sourceMap);
    } else {
        ClientUtil.showMessageWindow("Select Payment or Receipt ");
    }

}//GEN-LAST:event_btnCustActionPerformed

    private void txtAmountFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmountFocusGained
        // TODO add your handling code here:
        if (CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue() > 0) {
            txtAmount.setToolTipText(CommonUtil.currencyToWord(CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue()));
        } else {
            txtAmount.setToolTipText("Zero");
        }
        
        
    }//GEN-LAST:event_txtAmountFocusGained

    private void txtAmountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAmountKeyPressed
        // TODO add your handling code here:
        if (CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue() > 0) {
            txtAmount.setToolTipText(CommonUtil.currencyToWord(CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue()));
        } else {
            txtAmount.setToolTipText("Zero");
        }
    }//GEN-LAST:event_txtAmountKeyPressed

    private void txtAmountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAmountKeyReleased
        // TODO add your handling code here:
        if (CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue() > 0) {
            txtAmount.setToolTipText(CommonUtil.currencyToWord(CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue()));
        } else {
            txtAmount.setToolTipText("Zero");
        }
    }//GEN-LAST:event_txtAmountKeyReleased

    private void txtAmountKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAmountKeyTyped
        // TODO add your handling code here:
        if (CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue() > 0) {
            txtAmount.setToolTipText(CommonUtil.currencyToWord(CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue()));
        } else {
            txtAmount.setToolTipText("Zero");
        }
    }//GEN-LAST:event_txtAmountKeyTyped

    private void txtAccHdIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccHdIdFocusLost
        // TODO add your handling code here:
        if (txtAccHdId.getText() != null && !txtAccHdId.getText().equalsIgnoreCase("")) {
            HashMap hmap = new HashMap();
            String customerAllow = "";
            hmap.put("ACHEAD", txtAccHdId.getText());
            List list = ClientUtil.executeQuery("getCustomerAlloowProperty", hmap);
            if (list != null && list.size() > 0) {
                hmap = (HashMap) list.get(0);
                customerAllow = CommonUtil.convertObjToStr(hmap.get("ALLOW_CUSTOMER_ACNUM"));
                //  hoAc = CommonUtil.convertObjToStr(hmap.get("HO_ACCT"));
            }
//            if (bankType.equals("DCCB")) {
//                if (hoAc.equals("Y")) {
//                    btnOrgOrResp.setVisible(true);
//                    observable.setHoAccount(true);
//                } else {
//                    observable.setHoAccount(false);
//                    btnOrgOrResp.setVisible(false);
//                }
//            }
            //System.out.println("customerAllow>>>>" + customerAllow);
            if (customerAllow.equals("Y")) {
                //System.out.println("innnnn");
                CInternalFrame frm = new CInternalFrame();
                frm = new com.see.truetransact.ui.transaction.cash.GLAccountNumberListUI(this);
                frm.setSelectedBranchID(getSelectedBranchID());
                //frm.setSize(1000,1000);
                TrueTransactMain.showScreen(frm);

//               final CInternalFrame frame = new CInternalFrame();
//               CDesktopPane desktop = new CDesktopPane();
//               glAccountNumberListUI=new GLAccountNumberListUI();
//        //frame.setLocation(xOffset * openFrameCount, yOffset * openFrameCount);
//        frame.setSize(200, 100);
//        frame.setVisible(true);
//        frame.getContentPane().add(glAccountNumberListUI);
//        desktop.add(frame);

//                GLAccountNumberListUI glAccNo = new GLAccountNumberListUI();
//                glAccNo.show();
//                glAccNo.setVisible(true);
//                String AccNo = COptionPane.showInputDialog(this, "Enter Acc no");
//                hmap.put("ACC_NUM", AccNo);
//                List chkList = ClientUtil.executeQuery("checkAccStatus", hmap);
//                if (chkList != null && chkList.size() > 0) {
//                    hmap = (HashMap) chkList.get(0);
//                    observable.setLblAccName(CommonUtil.convertObjToStr(hmap.get("NAME")));
//                    observable.setTxtAccNo(AccNo);
//                    observable.setClosedAccNo(AccNo);
//                } else {
//                    ClientUtil.displayAlert("Invalid Account number");
//                    txtAccHdId.setText("");
//                    return;
//                }
            }
        }
    }//GEN-LAST:event_txtAccHdIdFocusLost

    private void txtAccHdIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAccHdIdActionPerformed
        // TODO add your handling code here:
//        if(txtAccHdId.getText()!=null && !txtAccHdId.getText().equalsIgnoreCase("")){
//         HashMap hmap = new HashMap();
//         String customerAllow="";
//            hmap.put("ACHEAD", txtAccHdId.getText());
//            List list = ClientUtil.executeQuery("getCustomerAlloowProperty", hmap);
//            if (list != null && list.size() > 0) {
//                hmap = (HashMap) list.get(0);
//                customerAllow = CommonUtil.convertObjToStr(hmap.get("ALLOW_CUSTOMER_ACNUM"));
//              //  hoAc = CommonUtil.convertObjToStr(hmap.get("HO_ACCT"));
//            }
////            if (bankType.equals("DCCB")) {
////                if (hoAc.equals("Y")) {
////                    btnOrgOrResp.setVisible(true);
////                    observable.setHoAccount(true);
////                } else {
////                    observable.setHoAccount(false);
////                    btnOrgOrResp.setVisible(false);
////                }
////            }
//            System.out.println("customerAllow>>>>"+customerAllow);
//        if (customerAllow.equals("Y")) {
//                System.out.println("innnnn");
//                CInternalFrame frm = new CInternalFrame();
//                frm = new com.see.truetransact.ui.transaction.cash.GLAccountNumberListUI(this);
//                frm.setSelectedBranchID(getSelectedBranchID());
//               //frm.setSize(1000,1000);
//                TrueTransactMain.showScreen(frm);
//                
////               final CInternalFrame frame = new CInternalFrame();
////               CDesktopPane desktop = new CDesktopPane();
////               glAccountNumberListUI=new GLAccountNumberListUI();
////        //frame.setLocation(xOffset * openFrameCount, yOffset * openFrameCount);
////        frame.setSize(200, 100);
////        frame.setVisible(true);
////        frame.getContentPane().add(glAccountNumberListUI);
////        desktop.add(frame);
//          
////                GLAccountNumberListUI glAccNo = new GLAccountNumberListUI();
////                glAccNo.show();
////                glAccNo.setVisible(true);
////                String AccNo = COptionPane.showInputDialog(this, "Enter Acc no");
////                hmap.put("ACC_NUM", AccNo);
////                List chkList = ClientUtil.executeQuery("checkAccStatus", hmap);
////                if (chkList != null && chkList.size() > 0) {
////                    hmap = (HashMap) chkList.get(0);
////                    observable.setLblAccName(CommonUtil.convertObjToStr(hmap.get("NAME")));
////                    observable.setTxtAccNo(AccNo);
////                    observable.setClosedAccNo(AccNo);
////                } else {
////                    ClientUtil.displayAlert("Invalid Account number");
////                    txtAccHdId.setText("");
////                    return;
////                }
//            }
//        }
    }//GEN-LAST:event_txtAccHdIdActionPerformed
    public void showEditWaiveTableUI(HashMap totalLoanAmount) {
        ArrayList singleList = new ArrayList();
        HashMap listMap = new HashMap();
        listMap.put("PENAL", CommonUtil.convertObjToDouble(totalLoanAmount.get("PENAL_INT")));
        listMap.put("INTEREST", CommonUtil.convertObjToDouble(totalLoanAmount.get("CURR_MONTH_INT")));
        listMap.put("PRINCIPAL", CommonUtil.convertObjToDouble(totalLoanAmount.get("CURR_MONTH_PRINCEPLE")));
        listMap.put("NOTICE CHARGES", CommonUtil.convertObjToDouble(totalLoanAmount.get("NOTICE CHARGES")));
        listMap.put("ARC_COST", CommonUtil.convertObjToDouble(totalLoanAmount.get("ARC_COST")));
        listMap.put("ARBITRARY CHARGES", CommonUtil.convertObjToDouble(totalLoanAmount.get("ARBITRARY CHARGES")));
        listMap.put("EXECUTION DECREE CHARGES", CommonUtil.convertObjToDouble(totalLoanAmount.get("EXECUTION DECREE CHARGES")));
        listMap.put("POSTAGE CHARGES", CommonUtil.convertObjToDouble(totalLoanAmount.get("POSTAGE CHARGES")));
        listMap.put("ADVERTISE CHARGES", CommonUtil.convertObjToDouble(totalLoanAmount.get("ADVERTISE CHARGES")));
        listMap.put("LEGAL CHARGES", CommonUtil.convertObjToDouble(totalLoanAmount.get("LEGAL CHARGES")));
        listMap.put("INSURANCE CHARGES", CommonUtil.convertObjToDouble(totalLoanAmount.get("INSURANCE CHARGES")));
        listMap.put("EP_COST", CommonUtil.convertObjToDouble(totalLoanAmount.get("EP_COST")));
        listMap.put("MISCELLANEOUS CHARGES", CommonUtil.convertObjToDouble(totalLoanAmount.get("MISCELLANEOUS CHARGES")));
        if(totalLoanAmount.containsKey("RECOVERY CHARGES") && totalLoanAmount.get("RECOVERY CHARGES") != null){
            listMap.put("RECOVERY CHARGES", CommonUtil.convertObjToDouble(totalLoanAmount.get("RECOVERY CHARGES")));
        }
        if(totalLoanAmount.containsKey("MEASUREMENT CHARGES") && totalLoanAmount.get("MEASUREMENT CHARGES") != null){
            listMap.put("MEASUREMENT CHARGES", CommonUtil.convertObjToDouble(totalLoanAmount.get("MEASUREMENT CHARGES")));
        }
        
        if(totalLoanAmount.containsKey("KOLEFIELD EXPENSE") && totalLoanAmount.get("KOLEFIELD EXPENSE") != null){
            listMap.put("KOLEFIELD EXPENSE", CommonUtil.convertObjToDouble(totalLoanAmount.get("KOLEFIELD EXPENSE")));
        }
        
        if(totalLoanAmount.containsKey("KOLEFIELD OPERATION") && totalLoanAmount.get("KOLEFIELD OPERATION") != null){
            listMap.put("KOLEFIELD OPERATION", CommonUtil.convertObjToDouble(totalLoanAmount.get("KOLEFIELD OPERATION")));
        }
        
        // Added by nithya for 0008470 : overdue interest for EMI Loans
        if(totalLoanAmount.containsKey("EMI_OVERDUE_CHARGE") && totalLoanAmount.get("EMI_OVERDUE_CHARGE") != null)
            listMap.put("EMI_OVERDUE_CHARGE", CommonUtil.convertObjToDouble(totalLoanAmount.get("EMI_OVERDUE_CHARGE")));// For overdue interest
        singleList.add(listMap);
        editableWaiveUI = new EditWaiveTableUI("CASH_SCREEN", listMap);
        editableWaiveUI.show();
//        TrueTransactMain.showScreen(editableUI);
    }
    
    private double setEMILoanWaiveAmount() {
        //System.out.println("asAndWhenMap inside setEMILoanWaiveAmount :: " + asAndWhenMap);
        //System.out.println("amount..... " + observable.getTxtAmount());
        double penalWaiveAmt = 0.0;
        double interestWaiveAmt = 0.0;
        double noticeWaiveAmt = 0.0;
        double arcWaiveAmt = 0.0;
        double arbitaryWaiveAmt = 0.0;
        double recoveryWaiveAmt = 0.0;
        double measurementWaiveAmt = 0.0;
        double postageWaiveAmt = 0.0;
        double legalWaiveAmt = 0.0;
        double insurenceWaiveAmt = 0.0;
        double epWaiveAmt = 0.0;
        double degreeWaiveAmt = 0.0;
        double miseWaiveAmt = 0.0;
        double advertiseWaiveAmt = 0.0;
        double overDueIntWaiveAmt = 0.0;
        double koleFieldExpenseWaiveAmt = 0.0;
        double koleFieldOperationWaiveAmt = 0.0;
        
        double totalAmount = CommonUtil.convertObjToDouble(observable.getTxtAmount());
        if (asAndWhenMap != null && asAndWhenMap.containsKey("EMI_IN_SIMPLEINTREST") && asAndWhenMap.get("EMI_IN_SIMPLEINTREST") != null && !asAndWhenMap.get("EMI_IN_SIMPLEINTREST").equals("Y") && returnWaiveMap != null) {
            if (asAndWhenMap.containsKey("PENAL_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("PENAL_WAIVER")).equals("Y") && returnWaiveMap.containsKey("PENAL") && returnWaiveMap.get("PENAL") != null) {
                penalWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("PENAL"));
            }
            if (asAndWhenMap.containsKey("INTEREST_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("INTEREST_WAIVER")).equals("Y") && returnWaiveMap.containsKey("INTEREST") && returnWaiveMap.get("INTEREST") != null) {
                interestWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("INTEREST"));
            }
            if (asAndWhenMap.containsKey("NOTICE_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("NOTICE_WAIVER")).equals("Y") && returnWaiveMap.containsKey("NOTICE CHARGES") && returnWaiveMap.get("NOTICE CHARGES") != null) {
                noticeWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("NOTICE CHARGES"));
            }
            if (asAndWhenMap.containsKey("ARC_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("ARC_WAIVER")).equals("Y") && returnWaiveMap.containsKey("ARC_COST") && returnWaiveMap.get("ARC_COST") != null) {
                arcWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("ARC_COST"));
            }
            if (asAndWhenMap.containsKey("ARBITRARY_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("ARBITRARY_WAIVER")).equals("Y") && returnWaiveMap.containsKey("ARBITRARY CHARGES") && returnWaiveMap.get("ARBITRARY CHARGES") != null) {
                arbitaryWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("ARBITRARY CHARGES"));
            }
            if (asAndWhenMap.containsKey("RECOVERY_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("RECOVERY_WAIVER")).equals("Y") && returnWaiveMap.containsKey("RECOVERY CHARGES") && returnWaiveMap.get("RECOVERY CHARGES") != null) {
                recoveryWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("RECOVERY CHARGES"));
            }
            if (asAndWhenMap.containsKey("MEASUREMENT_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("MEASUREMENT_WAIVER")).equals("Y") && returnWaiveMap.containsKey("MEASUREMENT CHARGES") && returnWaiveMap.get("MEASUREMENT CHARGES") != null) {
                measurementWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("MEASUREMENT CHARGES"));
            }
            if (asAndWhenMap.containsKey("POSTAGE_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("POSTAGE_WAIVER")).equals("Y") && returnWaiveMap.containsKey("POSTAGE CHARGES") && returnWaiveMap.get("POSTAGE CHARGES") != null) {
                postageWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("POSTAGE CHARGES"));
            }
            if (asAndWhenMap.containsKey("MISCELLANEOUS_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("MISCELLANEOUS_WAIVER")).equals("Y") && returnWaiveMap.containsKey("MISCELLANEOUS CHARGES") && returnWaiveMap.get("MISCELLANEOUS CHARGES") != null) {
                miseWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("MISCELLANEOUS CHARGES"));
            }
            if (asAndWhenMap.containsKey("INSURANCE_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("INSURANCE_WAIVER")).equals("Y") && returnWaiveMap.containsKey("INSURANCE CHARGES") && returnWaiveMap.get("INSURANCE CHARGES") != null) {
                insurenceWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("INSURANCE CHARGES"));
            }
            if (asAndWhenMap.containsKey("EP_COST_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("EP_COST_WAIVER")).equals("Y") && returnWaiveMap.containsKey("EP_COST") && returnWaiveMap.get("EP_COST") != null) {
                epWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("EP_COST"));
            }
            if (asAndWhenMap.containsKey("LEGAL_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("LEGAL_WAIVER")).equals("Y") && returnWaiveMap.containsKey("LEGAL CHARGES") && returnWaiveMap.get("LEGAL CHARGES") != null) {
                legalWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("LEGAL CHARGES"));
            }
            if (asAndWhenMap.containsKey("DECREE_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("DECREE_WAIVER")).equals("Y") && returnWaiveMap.containsKey("EXECUTION DECREE CHARGES") && returnWaiveMap.get("EXECUTION DECREE CHARGES") != null) {
                degreeWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("EXECUTION DECREE CHARGES"));
            }
            if (asAndWhenMap.containsKey("ADVERTISE_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("ADVERTISE_WAIVER")).equals("Y") && returnWaiveMap.containsKey("ADVERTISE CHARGES") && returnWaiveMap.get("ADVERTISE CHARGES") != null) {
                advertiseWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("ADVERTISE CHARGES"));
            }
            if (asAndWhenMap.containsKey("OVERDUEINT_WAIVER") && CommonUtil.convertObjToStr(asAndWhenMap.get("OVERDUEINT_WAIVER")).equals("Y") && returnWaiveMap.containsKey("EMI_OVERDUE_CHARGE") && returnWaiveMap.get("EMI_OVERDUE_CHARGE") != null) {
                overDueIntWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("EMI_OVERDUE_CHARGE"));
            }
            
            if (asAndWhenMap.containsKey("KOLE_FIELD_EXPENSE_WAIVER") && asAndWhenMap.get("KOLE_FIELD_EXPENSE_WAIVER") != null && CommonUtil.convertObjToStr(asAndWhenMap.get("KOLE_FIELD_EXPENSE_WAIVER")).equals("Y") && returnWaiveMap.containsKey("KOLEFIELD EXPENSE") && returnWaiveMap.get("KOLEFIELD EXPENSE") != null && returnWaiveMap.get("KOLEFIELD EXPENSE") != null) {
                koleFieldExpenseWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("KOLEFIELD EXPENSE"));
            }
            
            if (asAndWhenMap.containsKey("KOLE_FIELD_OPERATION_WAIVER") && asAndWhenMap.get("KOLE_FIELD_OPERATION_WAIVER") != null && CommonUtil.convertObjToStr(asAndWhenMap.get("KOLE_FIELD_OPERATION_WAIVER")).equals("Y") && returnWaiveMap.containsKey("KOLEFIELD OPERATION") && returnWaiveMap.get("KOLEFIELD OPERATION") != null && returnWaiveMap.get("KOLEFIELD OPERATION") != null) {
                koleFieldExpenseWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("KOLEFIELD OPERATION"));
            }
            
        }

        if (penalWaiveAmt > 0) {
            observable.setPenalWaiveOff(true);
            observable.setPenalWaiveAmount(penalWaiveAmt);
            //System.out.println("emi penal waving ....");
            txtAmount.setText(CommonUtil.convertObjToStr(totalAmount - penalWaiveAmt));
            totalAmount = totalAmount - penalWaiveAmt;
        } else {
            observable.setPenalWaiveOff(false);
        }

        if (interestWaiveAmt > 0) {
            observable.setInterestWaiveoff(true);
            observable.setInterestWaiveAmount(interestWaiveAmt);
            //System.out.println("emi interest waving ....");
            txtAmount.setText(CommonUtil.convertObjToStr(totalAmount - interestWaiveAmt));
            totalAmount = totalAmount - interestWaiveAmt;
        } else {
            observable.setInterestWaiveoff(false);
        }

        if (noticeWaiveAmt > 0) {
            observable.setNoticeWaiveoff(true);
            observable.setNoticeWaiveAmount(noticeWaiveAmt);
            //System.out.println("emi notice waving ....");
            txtAmount.setText(CommonUtil.convertObjToStr(totalAmount - noticeWaiveAmt));
            totalAmount = totalAmount - noticeWaiveAmt;
        } else {
            observable.setNoticeWaiveoff(false);
        }

        if (arcWaiveAmt > 0) {
            observable.setArcWaiveOff(true);
            observable.setArcWaiveAmount(arcWaiveAmt);
            //System.out.println("emi arc cost waving ....");
            txtAmount.setText(CommonUtil.convertObjToStr(totalAmount - arcWaiveAmt));
            totalAmount = totalAmount - arcWaiveAmt;
        } else {
            observable.setArcWaiveOff(false);
        }

        if (arbitaryWaiveAmt > 0) {
            observable.setArbitraryWaiveOff(true);
            observable.setArbitarayWaivwAmount(arbitaryWaiveAmt);
            //System.out.println("emi arc cost waving ....");
            txtAmount.setText(CommonUtil.convertObjToStr(totalAmount - arbitaryWaiveAmt));
            totalAmount = totalAmount - arbitaryWaiveAmt;
        } else {
            observable.setArbitraryWaiveOff(false);
        }

        if (postageWaiveAmt > 0) {
            observable.setPostageWaiveOff(true);
            observable.setPostageWaiveAmount(postageWaiveAmt);
            //System.out.println("emi arc cost waving ....");
            txtAmount.setText(CommonUtil.convertObjToStr(totalAmount - postageWaiveAmt));
            totalAmount = totalAmount - postageWaiveAmt;
        } else {
            observable.setPostageWaiveOff(false);
        }

        if (legalWaiveAmt > 0) {
            observable.setLegalWaiveOff(true);
            observable.setLegalWaiveAmount(legalWaiveAmt);
            //System.out.println("emi arc cost waving ....");
            txtAmount.setText(CommonUtil.convertObjToStr(totalAmount - legalWaiveAmt));
            totalAmount = totalAmount - legalWaiveAmt;
        } else {
            observable.setLegalWaiveOff(false);
        }

        if (insurenceWaiveAmt > 0) {
            observable.setInsuranceWaiveOff(true);
            observable.setInsuranceWaiveAmont(insurenceWaiveAmt);
            //System.out.println("emi arc cost waving ....");
            txtAmount.setText(CommonUtil.convertObjToStr(totalAmount - insurenceWaiveAmt));
            totalAmount = totalAmount - insurenceWaiveAmt;
        } else {
            observable.setInsuranceWaiveOff(false);
        }

        if (epWaiveAmt > 0) {
            observable.setEpCostWaiveOff(true);
            observable.setEpCostWaiveAmount(epWaiveAmt);
            //System.out.println("emi arc cost waving ....");
            txtAmount.setText(CommonUtil.convertObjToStr(totalAmount - epWaiveAmt));
            totalAmount = totalAmount - epWaiveAmt;
        } else {
            observable.setEpCostWaiveOff(false);
        }

        if (degreeWaiveAmt > 0) {
            observable.setDecreeWaiveOff(true);
            observable.setDecreeWaiveAmount(degreeWaiveAmt);
            //System.out.println("emi arc cost waving ....");
            txtAmount.setText(CommonUtil.convertObjToStr(totalAmount - degreeWaiveAmt));
            totalAmount = totalAmount - degreeWaiveAmt;
        } else {
            observable.setDecreeWaiveOff(false);
        }

        if (miseWaiveAmt > 0) {
            observable.setMiscellaneousWaiveOff(true);
            observable.setMiscellaneousWaiveAmount(miseWaiveAmt);
            //System.out.println("emi arc cost waving ....");
            txtAmount.setText(CommonUtil.convertObjToStr(totalAmount - miseWaiveAmt));
            totalAmount = totalAmount - miseWaiveAmt;
        } else {
            observable.setMiscellaneousWaiveOff(false);
        }

        if (advertiseWaiveAmt > 0) {
            observable.setAdvertiseWaiveOff(true);
            observable.setAdvertiseWaiveAmount(advertiseWaiveAmt);
            //System.out.println("emi arc cost waving ....");
            txtAmount.setText(CommonUtil.convertObjToStr(totalAmount - advertiseWaiveAmt));
            totalAmount = totalAmount - advertiseWaiveAmt;
        } else {
            observable.setAdvertiseWaiveOff(false);
        }

        if (overDueIntWaiveAmt > 0) {
            observable.setOverDueIntWaiveOff(true);
            observable.setOverDueIntWaiveAmount(overDueIntWaiveAmt);
            //System.out.println("emi arc cost waving ....");
            txtAmount.setText(CommonUtil.convertObjToStr(totalAmount - overDueIntWaiveAmt));
            totalAmount = totalAmount - overDueIntWaiveAmt;
        } else {
            observable.setOverDueIntWaiveOff(false);
        }
        
        if (koleFieldExpenseWaiveAmt > 0) {
            observable.setKoleFieldExpenseWaiveOff(true);
            observable.setKoleFieldExpenseWaiveAmount(koleFieldExpenseWaiveAmt);
            txtAmount.setText(CommonUtil.convertObjToStr(totalAmount - koleFieldExpenseWaiveAmt));
            totalAmount = totalAmount - koleFieldExpenseWaiveAmt;
        } else {
            observable.setKoleFieldExpenseWaiveOff(false);
        }
        
          if (koleFieldOperationWaiveAmt > 0) {
            observable.setKoleFieldOperationWaiveOff(true);
            observable.setKoleFieldOperationWaiveAmount(koleFieldOperationWaiveAmt);
            txtAmount.setText(CommonUtil.convertObjToStr(totalAmount - koleFieldOperationWaiveAmt));
            totalAmount = totalAmount - koleFieldOperationWaiveAmt;
        } else {
            observable.setKoleFieldOperationWaiveOff(false);
        }
        
        return  totalAmount;
    }
    
    
    private void btnWaiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWaiveActionPerformed
        if (CommonUtil.convertObjToDouble(txtAmount.getText()) > 0.0) {
            allWaiveMap = observable.getALL_LOAN_AMOUNT();
            if (allWaiveMap != null && allWaiveMap.size() > 0) {
                showEditWaiveTableUI(allWaiveMap);
            }
            returnWaiveMap = waiveOffEditInterestAmt();
            // Added by nithya on 10-08-2020 for KD-1982
            if (asAndWhenMap != null && asAndWhenMap.containsKey("INSTALL_TYPE") && asAndWhenMap.get("INSTALL_TYPE") != null && asAndWhenMap.get("INSTALL_TYPE").equals("EMI")
                    && returnWaiveMap != null && returnWaiveMap.size() > 0) {
               double totalEMIAmt = setEMILoanWaiveAmount();
            }
        }else{
            ClientUtil.displayAlert("amount should not be zero or empty");
            return;
        }
       // TODO add your handling code here:
    }//GEN-LAST:event_btnWaiveActionPerformed
    public ArrayList setOrgOrRespDetails() {
        HashMap hmap = new HashMap();
        ArrayList orgOrRespList = new ArrayList();

        hmap.put("OrgOrRespAdviceDt", getOrgOrRespAdviceDt());
        hmap.put("ADVICE_NO", getOrgOrRespAdviceNo());
        hmap.put("OrgOrRespAmout", getOrgOrRespAmout());
        hmap.put("OrgOrRespBranchId", getOrgOrRespBranchId());
        hmap.put("OrgOrRespBranchName", getOrgOrRespBranchName());
        hmap.put("OrgBranchName", getOrgBranchName());
        hmap.put("ORG_BRANCH", getOrgBranch());
        hmap.put("OrgOrRespCategory", getOrgOrRespCategory());
        hmap.put("OrgOrRespDetails", getOrgOrRespDetails());
        hmap.put("OrgOrRespTransType", getOrgOrRespTransType());
        orgOrRespList.add(hmap);

        return orgOrRespList;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable th) {
            th.printStackTrace();
        }

        javax.swing.JFrame jf = new javax.swing.JFrame();
        CashTransactionUI gui = new CashTransactionUI();
        jf.getContentPane().add(gui);
        jf.setSize(536, 566);
        jf.show();
        gui.show();
    }

    /**
     * Getter for property viewType.
     *
     * @return Value of property viewType.
     */
    public int getViewType() {
        return viewType;
    }

    /**
     * Setter for property viewType.
     *
     * @param viewType New value of property viewType.
     */
    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    /**
     * Getter for property intAmtDep.
     *
     * @return Value of property intAmtDep.
     */
    public double getIntAmtDep() {
        return intAmtDep;
    }

    /**
     * Setter for property intAmtDep.
     *
     * @param intAmtDep New value of property intAmtDep.
     */
    public void setIntAmtDep(double intAmtDep) {
        this.intAmtDep = intAmtDep;
    }

    public double getOrgOrRespAmout() {
        return orgOrRespAmout;
    }

    public void setOrgOrRespAmout(double orgOrRespAmout) {
        this.orgOrRespAmout = orgOrRespAmout;
    }

    public String getOrgOrRespBranchName() {
        return orgOrRespBranchName;
    }

    public void setOrgOrRespBranchName(String orgOrRespBranchName) {
        this.orgOrRespBranchName = orgOrRespBranchName;
    }

    public String getOrgOrRespBranchId() {
        return orgOrRespBranchId;
    }

    public void setOrgOrRespBranchId(String orgOrRespBranchId) {
        this.orgOrRespBranchId = orgOrRespBranchId;
    }

    public String getOrgOrRespCategory() {
        return orgOrRespCategory;
    }

    public void setOrgOrRespCategory(String orgOrRespCategory) {
        this.orgOrRespCategory = orgOrRespCategory;
    }

    public Date getOrgOrRespAdviceDt() {
        return orgOrRespAdviceDt;
    }

    public void setOrgOrRespAdviceDt(Date orgOrRespAdviceDt) {
        this.orgOrRespAdviceDt = orgOrRespAdviceDt;
    }

    public String getOrgOrRespAdviceNo() {
        return orgOrRespAdviceNo;
    }

    public void setOrgOrRespAdviceNo(String orgOrRespAdviceNo) {
        this.orgOrRespAdviceNo = orgOrRespAdviceNo;
    }

    public String getOrgOrRespTransType() {
        return orgOrRespTransType;
    }

    public void setOrgOrRespTransType(String orgOrRespTransType) {
        this.orgOrRespTransType = orgOrRespTransType;
    }

    public String getOrgOrRespDetails() {
        return orgOrRespDetails;
    }

    public void setOrgOrRespDetails(String orgOrRespDetails) {
        this.orgOrRespDetails = orgOrRespDetails;
    }

    public String getOrgBranch() {
        return orgBranch;
    }

    public void setOrgBranch(String orgBranch) {
        this.orgBranch = orgBranch;
    }

    public String getOrgBranchName() {
        return orgBranchName;
    }

    public void setOrgBranchName(String orgBranchName) {
        this.orgBranchName = orgBranchName;
    }

    private static class FrameShower implements Runnable {

        final CInternalFrame frame;

        public FrameShower(CInternalFrame frame) {
            this.frame = frame;
        }

        public void run() {
            frame.show();
        }
    }

    private void calculateServiceTaxAmt() {
        String actNum = CommonUtil.convertObjToStr(txtAccNo.getText());
        String prodId = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
       // double taxAmt = observable.calcServiceTaxAmount(actNum, prodId);     
        List taxSettingsList = observable.calcServiceTaxAmount(actNum, prodId);             
        if (taxSettingsList != null && taxSettingsList.size() > 0) {
            HashMap ser_Tax_Val = new HashMap();
            ser_Tax_Val.put(ServiceTaxCalculation.CURR_DT, currDt.clone());
            //ser_Tax_Val.put(ServiceTaxCalculation.TOT_AMOUNT, taxAmt);
            ser_Tax_Val.put("SERVICE_TAX_DATA", taxSettingsList);
            try {
                objServiceTax = new ServiceTaxCalculation();
                //ser_Tax_Val.put("TEXT_BOX","TEXT_BOX");
                serviceTax_Map = objServiceTax.calculateServiceTax(ser_Tax_Val);
                if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.TOT_TAX_AMT)) {
                    String amt = CommonUtil.convertObjToStr(serviceTax_Map.get(ServiceTaxCalculation.TOT_TAX_AMT));
                    //lblServiceTaxval.setText(objServiceTax.roundOffAmt(amt, "NEAREST_VALUE")); 
                    lblServiceTaxval.setText(CommonUtil.convertObjToStr(amt)); // Changed by nithya on 23-04-2020 for KD-1837
                    observable.setLblServiceTaxval(lblServiceTaxval.getText());
                    //serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, objServiceTax.roundOffAmt(amt, "NEAREST_VALUE"));
                    serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, amt);// Changed by nithya on 23-04-2020 for KD-1837
                } else {
                    lblServiceTaxval.setText("0.00");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else {
            lblServiceTaxval.setText("0.00");
        }

    }
    public HashMap setRepaymentData() {
        HashMap repayData = new HashMap();
//    //      HashMap prodLevelValues = observable.getCompFreqRoundOffValues();
        HashMap newmap = new HashMap();
        newmap.put("ACCT_NUM", txtAccNo.getText());
        List lst = ClientUtil.executeQuery("getLoansRepaymentData", newmap);
        if (lst != null && lst.size() > 0) {
            HashMap resltMap = (HashMap) lst.get(0);
            if (resltMap != null && resltMap.size() > 0) {
                repayData.put("ACT_NO", txtAccNo.getText());
                repayData.put("NEW_INSTALLMENT", "NEW_INSTALLMENT");
                repayData.put("FROM_DATE", resltMap.get("FROM_DT"));
                repayData.put("REPAYMENT_START_DT", resltMap.get("FIRST_INSTALL_DT"));
                repayData.put("TO_DATE", resltMap.get("LAST_INSTALL_DT"));
                repayData.put("NO_INSTALL", resltMap.get("NO_INSTALLMENTS"));
                repayData.put("ISDURATION_DDMMYY", "YES");
                repayData.put("INTEREST_TYPE", "COMPOUND");
                repayData.put("DURATION_YY", resltMap.get("NO_INSTALLMENTS"));
                repayData.put("COMPOUNDING_PERIOD", resltMap.get("REPAYMENT_FREQUENCY"));
                repayData.put("REPAYMENT_TYPE", resltMap.get("INSTALL_TYPE"));
                repayData.put("PRINCIPAL_AMOUNT", resltMap.get("LOAN_AMOUNT"));
                repayData.put("ROUNDING_FACTOR", "1_RUPEE");
                HashMap prodLevelValues = observable.getCompFreqRoundOffValues(CommonUtil.convertObjToStr(resltMap.get("PROD_ID")));
                repayData.put("ROUNDING_TYPE", CommonUtil.convertObjToStr(prodLevelValues.get("DEBIT_INT_ROUNDOFF")));
                repayData.put("REPAYMENT_FREQUENCY", resltMap.get("REPAYMENT_FREQUENCY"));
                repayData.put("SCHEDULE_ID", resltMap.get("SCHEDULE_NO"));
                repayData.put("INTEREST", asAndWhenMap.get("ROI"));
                repayData.put("BALANCE_AMT", resltMap.get("LOAN_BALANCE_PRINCIPAL"));
                repayData.put("PAID_AMT", resltMap.get("PAID"));
            }

        }
        //System.out.println("repayData ---------- :" + repayData);
        return repayData;
    }

    public void setServiceTax(String taxAmt, String totAmt, String overDueCharge,String paidInst) {
        lblServiceTaxval.setText(taxAmt);
        txtAmount.setText(totAmt);
        observable.setLblServiceTaxval(taxAmt);
        observable.setTxtAmount(totAmt); 
        observable.setALL_LOAN_AMOUNT(transDetails.getTermLoanCloseCharge());
        observable.getALL_LOAN_AMOUNT().put("NO_OF_INSTALLMENT", new Long(paidInst));          
    }
    
    // nithya 01-11-2018
    private void calculateRDServiceTaxAmt(double penalAmount) {
        String taxApplicable = "";
        HashMap taxMap;
        String actNum = CommonUtil.convertObjToStr(txtAccNo.getText());
        String prodId = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
        //double taxAmt = CommonUtil.convertObjToDouble(transDetails.getPenalAmount());  
        double taxAmt = penalAmount; // Added by nithya on 28-11-2018 for KD 346 - Gst amount calculation issue at Rd Remittance at cash and Transfer
        HashMap prodMap = new HashMap();
        prodMap.put("PROD_ID", prodId);
        List depositClosingHeadLst = ClientUtil.executeQuery("getDepositClosingHeads", prodMap);
        if (depositClosingHeadLst != null && depositClosingHeadLst.size() > 0) {
            HashMap depositClosingHeadMap = (HashMap) depositClosingHeadLst.get(0);
            if (CommonUtil.convertObjToDouble(transDetails.getPenalAmount()) > 0) {
                List taxSettingsList = new ArrayList();
                String achd = CommonUtil.convertObjToStr(depositClosingHeadMap.get("DELAYED_ACHD"));
                HashMap whereMap = new HashMap();
                whereMap.put("AC_HD_ID", achd);
                List temp = ClientUtil.executeQuery("getCheckServiceTaxApplicableForShare", whereMap);
                if (temp != null && temp.size() > 0) {
                    HashMap value = (HashMap) temp.get(0);
                    if (value != null && value.containsKey("SERVICE_TAX_APPLICABLE")) {
                        taxApplicable = CommonUtil.convertObjToStr(value.get("SERVICE_TAX_APPLICABLE"));
                        if (taxApplicable != null && taxApplicable.equals("Y") && taxAmt > 0) {
                            if (value.containsKey("SERVICE_TAX_ID") && value.get("SERVICE_TAX_ID") != null && CommonUtil.convertObjToStr(value.get("SERVICE_TAX_ID")).length() > 0) {
                                taxMap = new HashMap();
                                taxMap.put("SETTINGS_ID", value.get("SERVICE_TAX_ID"));
                                taxMap.put(ServiceTaxCalculation.TOT_AMOUNT, CommonUtil.convertObjToStr(taxAmt));
                                taxSettingsList.add(taxMap);
                            }
                        }
                    }
                }
                if (taxSettingsList != null && taxSettingsList.size() > 0) {
                    HashMap ser_Tax_Val = new HashMap();
                    ser_Tax_Val.put(ServiceTaxCalculation.CURR_DT, currDt.clone());                   
                    ser_Tax_Val.put("SERVICE_TAX_DATA", taxSettingsList);
                    try {
                        objServiceTax = new ServiceTaxCalculation();                    
                        serviceTax_Map = objServiceTax.calculateServiceTax(ser_Tax_Val);
                        if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.TOT_TAX_AMT)) {
                            String amt = CommonUtil.convertObjToStr(serviceTax_Map.get(ServiceTaxCalculation.TOT_TAX_AMT));
                            //lblServiceTaxval.setText(objServiceTax.roundOffAmt(amt, "NEAREST_VALUE"));
                            lblServiceTaxval.setText(CommonUtil.convertObjToStr(amt)); // Changed by nithya on 23-04-2020 for KD-1837
                            observable.setLblServiceTaxval(lblServiceTaxval.getText());
                            //serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, objServiceTax.roundOffAmt(amt, "NEAREST_VALUE"));
                            serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, amt);// Changed by nithya on 23-04-2020 for KD-1837
                        } else {
                            lblServiceTaxval.setText("0.00");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    lblServiceTaxval.setText("0.00");
                }
            }
        }
    }

    // Added by nithya on 30-04-2021 for KD-2844 & KD-2801    
    public void modifyTransData(Object objTextUI) {
        if (observable.getProdType().equals("TL")) {
            if (rdoTransactionType_Credit.isSelected() && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                if (!observable.getInstalType().isEmpty() && !observable.getInstalType().equals("") && !observable.getInstalType().equals(null) && observable.getInstalType().equals("EMI") && observable.getEMIinSimpleInterest().equalsIgnoreCase("N")) {
                    double totalEMIAmt = setEMIAmount();
                    observable.setTxtAmount(String.valueOf(totalEMIAmt));
                    txtAmount.setText(observable.getTxtAmount());
                    if (txtAmount.getText().length() > 0) {
                        totalEMIAmt = setEMILoanWaiveAmount();
                    }
                    observable.setTxtAmount(String.valueOf(totalEMIAmt));
                    txtAmount.setText(observable.getTxtAmount());
                }
            }
        }
        if (observable.getProdType().equals("TD") && depBehavesLike.equals("RECURRING") && rdoTransactionType_Credit.isSelected() == true && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            double amts = 0;
            String depNo = CommonUtil.convertObjToStr(txtAccNo.getText());
            HashMap recurringMap = new HashMap();
            if (depNo.contains("_")) {
                depNo = depNo.substring(0, depNo.lastIndexOf("_"));
            }
            recurringMap.put("DEPOSIT_NO", depNo.trim());
            double depAmount = 0;
            List lst = ClientUtil.executeQuery("getDepositAmountForRecurring", recurringMap);
            if (lst != null && lst.size() > 0) {
                recurringMap = (HashMap) lst.get(0);
                depAmount = CommonUtil.convertObjToDouble(recurringMap.get("DEPOSIT_AMT"));
                amts = CommonUtil.convertObjToDouble(depAmount);
            }
            double amt2 = (amts) * noofInstallment2;
            double penalAmt = CommonUtil.convertObjToDouble(transDetails.getPenalAmount()).doubleValue();
            if (penalAmt > 0 && amt2 > 0) {
                amt2 = amt2 + penalAmt;
                calculateRDServiceTaxAmt(penalAmt);
                if(serviceTax_Map != null && serviceTax_Map.containsKey("TOT_TAX_AMT") && serviceTax_Map.get("TOT_TAX_AMT") != null){
                 amt2 += CommonUtil.convertObjToDouble(serviceTax_Map.get("TOT_TAX_AMT")).doubleValue();  
             }
            }
            if (amt2 != 0 || amt2 != 0.0) {
                observable.setTxtAmount(CommonUtil.convertObjToStr(amt2));
                txtAmount.setText(observable.getTxtAmount());
            } else {
                observable.setTxtAmount("");
                txtAmount.setText(observable.getTxtAmount());
            }
        }
    }
  
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAccHdId;
    private com.see.truetransact.uicomponent.CButton btnAccNo;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCurrencyInfo;
    private com.see.truetransact.uicomponent.CButton btnCust;
    private com.see.truetransact.uicomponent.CButton btnDebitDetails;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDenomination;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnOrgOrResp;
    private com.see.truetransact.uicomponent.CButton btnPanNo;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnVer;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CButton btnViewTermLoanDetails;
    private com.see.truetransact.uicomponent.CButton btnWaive;
    private com.see.truetransact.uicomponent.CComboBox cboInputCurrency;
    private com.see.truetransact.uicomponent.CComboBox cboInstrumentType;
    private com.see.truetransact.uicomponent.CComboBox cboProdId;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CLabel lblAccHd;
    private com.see.truetransact.uicomponent.CLabel lblAccHdDesc;
    private com.see.truetransact.uicomponent.CLabel lblAccName;
    private com.see.truetransact.uicomponent.CLabel lblAccNo;
    private com.see.truetransact.uicomponent.CLabel lblAccNoGl;
    private com.see.truetransact.uicomponent.CLabel lblAmount;
    private com.see.truetransact.uicomponent.CLabel lblAuthBy;
    private com.see.truetransact.uicomponent.CLabel lblAuthBydesc;
    private com.see.truetransact.uicomponent.CLabel lblHouseName;
    private com.see.truetransact.uicomponent.CLabel lblInitiatorChannel;
    private com.see.truetransact.uicomponent.CLabel lblInitiatorID;
    private com.see.truetransact.uicomponent.CLabel lblInitiatorIDDesc;
    private com.see.truetransact.uicomponent.CLabel lblInputAmt;
    private com.see.truetransact.uicomponent.CLabel lblInputCurrency;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentDate;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentNo;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentType;
    private com.see.truetransact.uicomponent.CLabel lblLoanStatus;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNarration;
    private com.see.truetransact.uicomponent.CLabel lblPanNo;
    private com.see.truetransact.uicomponent.CLabel lblParticulars;
    private com.see.truetransact.uicomponent.CLabel lblProdId;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    private com.see.truetransact.uicomponent.CLabel lblServiceTax;
    private com.see.truetransact.uicomponent.CLabel lblServiceTaxval;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace11;
    private com.see.truetransact.uicomponent.CLabel lblSpace12;
    private com.see.truetransact.uicomponent.CLabel lblSpace13;
    private com.see.truetransact.uicomponent.CLabel lblSpace14;
    private com.see.truetransact.uicomponent.CLabel lblSpace15;
    private com.see.truetransact.uicomponent.CLabel lblSpace16;
    private com.see.truetransact.uicomponent.CLabel lblSpace17;
    private com.see.truetransact.uicomponent.CLabel lblSpace18;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblSpace7;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTokenNo;
    private com.see.truetransact.uicomponent.CLabel lblTransactionDate;
    private com.see.truetransact.uicomponent.CLabel lblTransactionDateDesc;
    private com.see.truetransact.uicomponent.CLabel lblTransactionID;
    private com.see.truetransact.uicomponent.CLabel lblTransactionIDDesc;
    private com.see.truetransact.uicomponent.CLabel lblTransactionType;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAccHd;
    private com.see.truetransact.uicomponent.CPanel panAcctNo;
    private com.see.truetransact.uicomponent.CPanel panAmt;
    private com.see.truetransact.uicomponent.CPanel panCashTransaction;
    private com.see.truetransact.uicomponent.CPanel panData;
    private com.see.truetransact.uicomponent.CPanel panInstrumentNo;
    private com.see.truetransact.uicomponent.CPanel panInstrumentNo1;
    private com.see.truetransact.uicomponent.CPanel panLableValues;
    private com.see.truetransact.uicomponent.CPanel panLables;
    private com.see.truetransact.uicomponent.CPanel panPanNo;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTransaction;
    private com.see.truetransact.uicomponent.CPanel panTransactionType;
    private com.see.truetransact.uicomponent.CButtonGroup rdoTransactionType;
    private com.see.truetransact.uicomponent.CRadioButton rdoTransactionType_Credit;
    private com.see.truetransact.uicomponent.CRadioButton rdoTransactionType_Debit;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private com.see.truetransact.uicomponent.CScrollPane srpTxtAreaParticulars;
    private javax.swing.JToolBar tbrHead;
    private com.see.truetransact.uicomponent.CDateField tdtInstrumentDate;
    private com.see.truetransact.uicomponent.CTextField txtAccHdId;
    private com.see.truetransact.uicomponent.CTextField txtAccNo;
    private com.see.truetransact.uicomponent.CTextField txtAmount;
    private com.see.truetransact.uicomponent.CTextArea txtAreaParticular;
    private com.see.truetransact.uicomponent.CTextField txtInitiatorChannel;
    private com.see.truetransact.uicomponent.CTextField txtInputAmt;
    private com.see.truetransact.uicomponent.CTextField txtInstrumentNo1;
    private com.see.truetransact.uicomponent.CTextField txtInstrumentNo2;
    private com.see.truetransact.uicomponent.CTextField txtPanNo;
    private com.see.truetransact.uicomponent.CTextField txtParticulars;
    private com.see.truetransact.uicomponent.CTextField txtTokenNo;
    // End of variables declaration//GEN-END:variables
}
