/*
 * TokenConfigUI.java
 *
 * Created on January 20, 2005, 3:03 PM
 */
package com.see.truetransact.ui.locker.lockersurrender;

/**
 *
 * @author ashokvijayakumar @modified : Sunil Added Edit Locking - 07-07-2005
 */
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uivalidation.NumericValidation;

import java.util.Observable;
import java.util.Observer;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Date;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import java.util.List;

import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.ui.common.viewall.NewAuthorizeListUI;
import com.see.truetransact.uivalidation.CurrencyValidation;
import java.awt.CheckboxGroup;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.text.DateFormat;
import javax.swing.ButtonGroup;

public class LockerSurrenderUI extends CInternalFrame implements Observer, UIMandatoryField {

    /**
     * Vairable Declarations
     */
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("com.see.truetransact.ui.locker.lockersurrender.LockerSurrenderRB", ProxyParameters.LANGUAGE);
    //Creating Instance For ResourceBundle-TokenConfigRB
    private LockerSurrenderOB observable; //Reference for the Observable Class TokenConfigOB
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private HashMap mandatoryMap;//Map for putting up MandatoryFields in the UI
    private String viewType = "";//Variable used to store ActionType(New,Edit,Delete)
    private final String EDIT = "Edit";
    private final String AUTHORIZE = "Authorize";//Variable used when btnAuthorize is clicked
    private int selectedRow = -1;
    private int selectedData = 0;
    private int result;
    private boolean isFilled = false;
    private TransactionUI transactionUI = new TransactionUI();
    private String issueID = "";
    private String surOrRenew = "";
    private Date currDt = null;
    private String surrenderID = "";
    private double serviceTaxPercentage = 0;
    private Date expDt = null;
    private int Penal_rate;
    private int no_of_days;
    private int commision;
    private String prodid;
    Rounding rd = new Rounding();
    boolean noSurrenderTrans = false;
    boolean pendingPayment = false;    
    boolean fromAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
    private int rejectFlag = 0;
    private String authorizeStatus = null;
    ButtonGroup chkGroup = new ButtonGroup();
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;
    private List lockerTransHeadList ;
    private ServiceTaxCalculation objServiceTax;
    public HashMap serviceTax_Map;
    //     private boolean renew = false;
    //     private boolean surrender = false;

    /**
     * Creates new form TokenConfigUI
     */
    public LockerSurrenderUI() {
        currDt = ClientUtil.getCurrentDate();
        initForm();
    }

    /**
     * Method which is used to initialize the form TokenConfig
     */
    private void initForm() {
        initComponents();
        setFieldNames();
        setObservable();
        observable.resetForm();
        //        txtNoOfTokens.setText("");
        initComponentData();
        setMandatoryHashMap();
        setMaxLengths();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panStdInstructions1);
        //        ClientUtil.enableDisable(panTokenConfiguration, false);
        setButtonEnableDisable();
        btnLockerNo.setEnabled(false);
        transactionUI.addToScreen(panTransaction);
        observable.setTransactionOB(transactionUI.getTransactionOB());
        ClientUtil.enableDisable(panStdInstructions1, false, false, true);
        cbCustomer.setEnabled(false);
        //        rdoSurrender.setSelected(true);
        //        rdoSurrenderActionPerformed(null);
        panTransaction.setEnabled(false);
        transactionUI.setSourceScreen("LOCKER_SURRENDER");
        lblCurrentDateVal.setText(CommonUtil.convertObjToStr(currDt));
        txtActNum.setEnabled(false);
        //        panLockerOut.setEnabled(false);
        //        ClientUtil.enableDisable(panStdInstructions, false);
        //        ClientUtil.enableDisable(panOperations, false);
    }

    /*
     * Auto Generated Method - setFieldNames() This method assigns name for all
     * the components. Other functions are working based on this name.
     */
    private void setFieldNames() {
        //        tblInstruction2.setName("tblInstruction2");
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
        cboProdID.setName("cboProdID");
        lbSpace2.setName("lbSpace2");
        //        lblEndingTokenNo.setName("lblEndingTokenNo");
        lblMsg.setName("lblMsg");
        //        lblNoOfTokens.setName("lblNoOfTokens");
        //        lblSeriesNo.setName("lblSeriesNo");
        lblSpace1.setName("lblSpace1");
        lblSpace3.setName("lblSpace3");
        lblSpace5.setName("lblSpace5");
        //        lblStartingTokenNo.setName("lblStartingTokenNo");
        lblStatus.setName("lblStatus");
        //        lblTokenType.setName("lblTokenCofigDate");
        mbrTokenConfig.setName("mbrTokenConfig");
        panStatus.setName("panStatus");
        //        panTokenConfiguration.setName("panTokenConfiguration");
        //        txtEndingTokenNo.setName("txtEndingTokenNo");
        //        txtNoOfTokens.setName("txtNoOfTokens");
        //        txtSeriesNo.setName("txtSeriesNo");
        //        txtStartingTokenNo.setName("txtStartingTokenNo");
        //        lblTokenConfigId.setName("lblTokenConfigId");
        //        txtTokenConfigId.setName("txtTokenConfigId");
        //        panLockerOut.setName("panLockerOut");
        //        lblLockerOutDt.setName("lblLockerOutDt");
        //        lblLockerOutDtVal.setName("lblLockerOutDtVal");
        lblProdID.setName("lblProdID");
        lblLockerNo.setName("lblLockerNo");
        lblCustName.setName("lblCustName");
        lblCustNameVal.setName("lblCustNameVal");
        lblCustomerId.setName("lblCustomerId");
        lblCustomerIdVal.setName("lblCustomerIdVal");
        lblDate.setName("lblDate");
        lblDateVal.setName("lblDateVal");
        lblModeOfOp.setName("lblModeOfOp");
        lblModeOfOpVal.setName("lblModeOfOpVal");
        txtServiceTax.setName("txtServiceTax");
        lblServiceTax.setName("lblServiceTax");
        txtLockerNo.setName("txtLockerNo");
        cboProdID.setName("cboProdID");
        //        lblCustId.setName("lblCustId");
        //        lblName.setName("lblName");
        //        lblPassword.setName("lblPassword");
        //        btnLockerOut.setName("btnLockerOut");
        btnLockerNo.setName("btnLockerNo");
        //        btnPasNew.setName("btnPasNew");
        //        btnPastSave.setName("btnPastSave");
        //        btnPasDelete.setName("btnPasDelete");

        //        txtCustId.setName("txtCustId");
        //        txtName.setName("txtName");
        //        txtPassword.setName("txtPassword");
        //
        //        panStdInstructions.setName("panStdInstructions");
        //        panOperations.setName("panOperations");
    }

    /*
     * Auto Generated Method - internationalize() This method used to assign
     * display texts from the Resource Bundle File.
     */
    private void internationalize() {
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        //        lblNoOfTokens.setText(resourceBundle.getString("lblNoOfTokens"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        //        lblSeriesNo.setText(resourceBundle.getString("lblSeriesNo"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        //        lblEndingTokenNo.setText(resourceBundle.getString("lblEndingTokenNo"));
        //        lblStartingTokenNo.setText(resourceBundle.getString("lblStartingTokenNo"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        //        lblTokenType.setText(resourceBundle.getString("lblTokenType"));
        lbSpace2.setText(resourceBundle.getString("lbSpace2"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        //        lblTokenConfigId.setText(resourceBundle.getString("lblTokenConfigId"));
    }

    /**
     * Adds up the Observable to this form
     */
    private void setObservable() {
        try {
            observable = LockerSurrenderOB.getInstance();

            observable.addObserver(this);
            observable.setTransactionOB(transactionUI.getTransactionOB());
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    /*
     * Setting model to the combobox cboTokenType
     */
    private void initComponentData() {
        try {
            cboProdID.setModel(observable.getCbmProdID());
            // txtCollectRentMM.setText("11");
            // txtCollectRentyyyy.setText("2012");
            //            cboTokenType.setModel(observable.getCbmTokenType());
            //            tblInstruction2.setModel(observable.getTbmInstructions2());
        } catch (ClassCastException e) {
            parseException.logException(e, true);
        }
    }

    /*
     * Auto Generated Method - update() This method called by Observable. It
     * updates the UI with Observable's data. If needed add/Remove RadioButtons
     * method need to be added.
     */
    public void update(Observable observed, Object arg) {
        //        txtTokenConfigId.setText(observable.getTxtTokenConfigId());
        removeRadioButtons();
        cboProdID.setSelectedItem(observable.getCboProdID());
        //        txtSeriesNo.setText(observable.getTxtSeriesNo());
        //        txtStartingTokenNo.setText(observable.getTxtStartingTokenNo());
        //        txtEndingTokenNo.setText(observable.getTxtEndingTokenNo());
        //        txtNoOfTokens.setText(observable.getTxtNoOfTokens());
        //        lblDateVal.setText(observable.getLblDateVal());
        //        lblLockerOutDtVal.setText(observable.getLblLockerOutDtVal());
        lblCustNameVal.setText(observable.getLblCustNameVal());
        lblCustomerIdVal.setText(observable.getLblCustomerIdVal());
        transactionUI.setCallingAccNo(observable.getLblCustomerIdVal());
        lblModeOfOpVal.setText(observable.getLblModeOfOpVal());
        txtLockerNo.setText(observable.getTxtLockerNo());
        txtCharges.setText(observable.getTxtCharges());
        txtRefund.setText(observable.getTxtRefund());
        //        txtCustId.setText(observable.getTxtCustId());
        //        txtName.setText(observable.getTxtName());
        //        txtPassword.setText(observable.getTxtPassword());
        lblStatus.setText(observable.getLblStatus());
        //       tblInstruction2.setModel(observable.getTbmInstructions2());
        txtServiceTax.setText(observable.getServiceTax());
        rdoRenew.setSelected(observable.isRenew());
        rdoSurrender.setSelected(observable.isSurrender());
        rdoBreakOpen.setSelected(observable.isBreakOpen());
        rdoRentCollection.setSelected(observable.isRentCollection());
        if (observable.isBreakOpen() == true) {
            cbCustomer.setSelected(observable.isCbCustomer());
            txtBreakOpenRemarks.setText(observable.getBreakOpenRemarks());
        }
        txtCollectRentMM.setText((observable.getCollectRentMM()));
        txtCollectRentyyyy.setText(observable.getCollectRentYYYY());
        // lblCurrentDateVal.setText(observable.getLblCurrentDateVal());
        transactionUI.setCallingProdID(observable.getProdID());
        transactionUI.setCallingApplicantName(observable.getCallingApplicantName());
        System.out.println("setLblNewExpDateVal#$#$#"+observable.getLblNewExpDateVal());
        tdtNewExpDt.setDateValue(CommonUtil.convertObjToStr(observable.getLblNewExpDateVal()));
        System.out.println("setLblNewExpDateVal#$#$#"+tdtNewExpDt.getDateValue());
        chkRenewBfrExp.setSelected(observable.isChkBforeExpDt());
        //  txtSurDate.setText(observable.getTxtSurDate());
        addRadioButtons();
    }

    /*
     * Auto Generated Method - updateOBFields() This method called by Save
     * option of UI. It updates the OB with UI data.
     */
    public void updateOBFields() {
        observable.setCboProdID((String) cboProdID.getSelectedItem());
        observable.setLblDateVal(lblDateVal.getText());
        observable.setLblCustNameVal(lblCustNameVal.getText());
        observable.setCbCustomer(cbCustomer.isSelected());
        observable.setLblCustomerIdVal(lblCustomerIdVal.getText());
        observable.setLblModeOfOpVal(lblModeOfOpVal.getText());
        observable.setTxtLockerNo(txtLockerNo.getText());
        observable.setTxtCharges(txtCharges.getText());
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setServiceTax(txtServiceTax.getText());
        observable.setRenew(rdoRenew.isSelected());
        observable.setBreakOpen(rdoBreakOpen.isSelected());
        observable.setSurrender(rdoSurrender.isSelected());
        observable.setRentCollection(rdoRentCollection.isSelected());
        observable.setCollectRentMM(txtCollectRentMM.getText());
        observable.setCollectRentYYYY(txtCollectRentyyyy.getText());
        observable.setProdID(transactionUI.getCallingProdID());
        //  observable.setLblCurrentDateVal(lblCurrentDateVal.getText());
        observable.setCallingApplicantName(transactionUI.getCallingApplicantName());
        //  observable.setLblCustomerIdVal(transactionUI.getCallingApplicantName());
        observable.setLblNewExpDateVal(tdtNewExpDt.getDateValue());
        observable.setBreakOpenRemarks(txtBreakOpenRemarks.getText());
        observable.setTxtRefund(txtRefund.getText());
        observable.setTxtPenalAmt(txtPenalAmt.getText());
        if(chkDefaulter.isSelected()&& !pendingPayment){
            observable.setChkDefaulter("DEFAULTER");
        }
        if (chkNoTrans.isSelected()) {
            observable.setChkNoTrans("NOTRANS");
        } else {
            observable.setChkNoTrans("");
        }
        observable.setTxtActNum(txtActNum.getText());
        observable.setChkBforeExpDt(chkRenewBfrExp.isSelected());
        observable.setServiceTax_Map(serviceTax_Map);
    }

    /*
     * Auto Generated Method - setMandatoryHashMap()
     *
     * ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
     *
     * This method list out all the Input Fields available in the UI. It needs a
     * class level HashMap variable mandatoryMap.
     */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboTokenType", new Boolean(true));
        mandatoryMap.put("txtSeriesNo", new Boolean(false));
        mandatoryMap.put("txtStartingTokenNo", new Boolean(true));
        mandatoryMap.put("txtEndingTokenNo", new Boolean(true));
        mandatoryMap.put("txtNoOfTokens", new Boolean(true));
    }

    /*
     * Auto Generated Method - getMandatoryHashMap() Getter method for setMandatoryHashMap().
     */
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    /*
     * Auto Generated Method - setHelpMessage() This method shows tooltip help
     * for all the input fields available in the UI. It needs the Mandatory
     * Resource Bundle object. Help display Label name should be lblMsg.
     */
    public void setHelpMessage() {
        //        cboTokenType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboTokenType"));
        //        txtSeriesNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSeriesNo"));
        //        txtStartingTokenNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStartingTokenNo"));
        //        txtEndingTokenNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEndingTokenNo"));
        //        txtNoOfTokens.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoOfTokens"));
        //        txtTokenConfigId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTokenConfigId"));
    }

    /**
     * Used to set Maximum possible lenghts for TextFields
     */
    private void setMaxLengths() {
        //        txtSeriesNo.setMaxLength(16);
        //        txtSeriesNo.setAllowAll(true);
        ////        setValidation(new com.see.truetransact.uivalidation.DefaultValidation());
        //        txtStartingTokenNo.setMaxLength(8);
        //        txtEndingTokenNo.setMaxLength(8);
        txtCharges.setMaxLength(16);
        txtCharges.setValidation(new CurrencyValidation(14, 2));
        txtCollectRentMM.setMaxLength(2);
        txtCollectRentMM.setValidation(new NumericValidation());
        txtCollectRentyyyy.setMaxLength(4);
        txtCollectRentyyyy.setValidation(new NumericValidation());
        txtRefund.setMaxLength(16);
        txtRefund.setValidation(new CurrencyValidation(14, 2));
        txtBreakOpenRemarks.setMaxLength(30);
        txtServiceTax.setMaxLength(16);
        txtServiceTax.setValidation(new CurrencyValidation(14, 2));
        //lblTotAmtVal.setValidation(new CurrencyValidation(14,2));

    }

    /*
     * Makes the button Enable or Disable accordingly when usier clicks new,edit
     * or delete buttons
     */
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
    }

    /**
     * Method used to check whether the Mandatory Fields in the Form are Filled
     * or not
     */
    private String checkMandatory(javax.swing.JComponent component) {
        return new MandatoryCheck().checkMandatory(getClass().getName(), component);
    }

    /**
     * Method used to Give a Alert when any Mandatory Field is not filled by the
     * user
     */
    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }

    private void displayTransDetail(HashMap proxyResultMap) {
        try{
	        System.out.println("@#$@@$@@@$ proxyResultMap : " + proxyResultMap);
	        String cashDisplayStr = "Cash Transaction Details...\n";
	        String transferDisplayStr = "Transfer Transaction Details...\n";
	        String displayStr = "";  
	        String fromTransferID = "";
	        String toTransferID = "";
	        String fromCashID = "";
	        String toCashID = "";
	        HashMap transTypeMap = new HashMap();
	        HashMap transMap = new HashMap();
	        HashMap transCashMap = new HashMap();
	        String actNum = "";
	        String oldBatchId = "";
	        String newBatchId = "";
	        transCashMap.put("BATCH_ID", proxyResultMap.get(CommonConstants.TRANS_ID));
	        transCashMap.put("TRANS_DT", currDt);
	        transCashMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);//AUTHORIZE_STATUS
	        transCashMap.put("AUTHORIZE_STATUS", "AUTHORIZE_STATUS");
	        HashMap transIdMap = new HashMap();
	        List lst = ClientUtil.executeQuery("getDepositAccountTransferDetails", transCashMap);
	        if (lst != null && lst.size() > 0) {
	            displayStr += "Transfer Transaction Details...\n";
	            for (int i = 0; i < lst.size(); i++) {
	                transMap = (HashMap) lst.get(i);
	                displayStr += "Trans Id : " + transMap.get("TRANS_ID")
	                        + "   Batch Id : " + transMap.get("BATCH_ID")
	                        + "   Trans Type : " + transMap.get("TRANS_TYPE");
	                actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
	                if (actNum != null && !actNum.equals("")) {
	                    displayStr += "   Account No : " + transMap.get("ACT_NUM")
	                            + "   Amount : " + transMap.get("AMOUNT") + "\n";
	                } else {
	                    displayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
	                            + "   Amount : " + transMap.get("AMOUNT") + "\n";
	                }
	                System.out.println("rish......"+transMap.get("SINGLE_TRANS_ID"));
	                transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "TRANSFER");
	
	                System.out.println("#### :" + transMap);
	                oldBatchId = newBatchId;
	                if (i == 0) {
	                    fromTransferID = CommonUtil.convertObjToStr(transMap.get("TRANS_ID"));
	                }
	                if (i == lst.size() - 1) {
	                    toTransferID = CommonUtil.convertObjToStr(transMap.get("TRANS_ID"));
	                }
	            }
	        }
	        actNum = CommonUtil.convertObjToStr(proxyResultMap.get(CommonConstants.TRANS_ID));
	        lst = ClientUtil.executeQuery("getCashDetails", transCashMap);
	        if (lst != null && lst.size() > 0) {
	            //system.out.println("eeeeeeeeeeeeeeeeeeeeeeeee");
	            displayStr += "Cash Transaction Details...\n";
	            for (int i = 0; i < lst.size(); i++) {
	                transMap = (HashMap) lst.get(i);
	                displayStr += "Trans Id : " + transMap.get("TRANS_ID")
	                        + "   Trans Type : " + transMap.get("TRANS_TYPE");
	                actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
	                if (actNum != null && !actNum.equals("")) {
	                    displayStr += "   Account No :  " + transMap.get("ACT_NUM")
	                            + "   Amount :  " + transMap.get("AMOUNT") + "\n";
	                } else {
	                    displayStr += "   Ac Hd Desc :  " + transMap.get("AC_HD_ID")
	                            + "   Amount :  " + transMap.get("AMOUNT") + "\n";
	                }
	                 transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "CASH");
	               
	                transTypeMap.put(transMap.get("SINGLE_TRANS_ID"), transMap.get("TRANS_TYPE"));
	                if (i == 0) {
	                    fromCashID = CommonUtil.convertObjToStr(transMap.get("TRANS_ID"));
	                }
	                if (i == lst.size() - 1) {
	                    toCashID = CommonUtil.convertObjToStr(transMap.get("TRANS_ID"));
	                }
	            }
	        }
	        if (!displayStr.equals("")) {
	            ClientUtil.showMessageWindow("" + displayStr);
	        }
	        int yesNo = 0;
	        String[] options = {"Yes", "No"};
	        yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
	                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
	                null, options, options[0]);
	        System.out.println("#$#$$ yesNo : " + yesNo);
	        if (yesNo == 0) {
	            TTIntegration ttIntgration = null;
	            HashMap printParamMap = new HashMap();
	            printParamMap.put("TransDt", currDt);
	            printParamMap.put("BranchId", ProxyParameters.BRANCH_ID);
	            Object keys1[] = transIdMap.keySet().toArray();
	            for (int i = 0; i < keys1.length; i++) {
	                printParamMap.put("TransId", keys1[i]);
	                ttIntgration.setParam(printParamMap);
	                if (CommonUtil.convertObjToStr(transIdMap.get(keys1[i])).equals("TRANSFER")) {
	                    ttIntgration.integrationForPrint("ReceiptPayment");
	                } else if (CommonUtil.convertObjToStr(transTypeMap.get(keys1[i])).equals("DEBIT")) {
	                    ttIntgration.integrationForPrint("CashPayment", false);
	                } else {
	                    ttIntgration.integrationForPrint("CashReceipt", false);
	                }
	            }
	        }
        }catch(Exception e){
        	e.printStackTrace();
        }
    }
    /*
     * Calls the execute method of TerminalOB to do insertion or updation or
     * deletion
     */

    private void saveAction(String status) {
        //To check mandtoryness of the Terminal panAcHdDetails,panAcHeadDetails panel and diplay appropriate
        //error message, else proceed
        if (rdoRenew.isSelected() == true || rdoSurrender.isSelected() == true || rdoBreakOpen.isSelected() == true || rdoRentCollection.isSelected()==true) {
            observable.doAction(status);
            if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().get("SINGLE_TRANS_ID")!=null && observable.getProxyReturnMap().get("SINGLE_TRANS_ID").toString().length()>0) {
                displayTransDetail(observable.getProxyReturnMap());
            }
        } else {
            ClientUtil.showMessageWindow("Select Option Renewal Or Surrender Or BreakOpen");
        }
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {

            if (status == CommonConstants.TOSTATUS_UPDATE) {
                //                    lockMap.put("CONFIG_ID", observable.getTxtTokenConfigId());
            }
            //                setEditLockMap(lockMap);
            //                setEditLock();
            settings();
            btnLockerNo.setEnabled(false);
            transactionUI.resetObjects();
            //                 ClientUtil.enableDisable(panStdInstructions, false);
            //                 ClientUtil.enableDisable(panOperations, false);
        }
    }

    //    }
    /*
     * set the screen after the updation,insertion, deletion
     */
    private void settings() {
        observable.resetForm();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panStdInstructions1, false, false, true);
        setButtonEnableDisable();
        observable.setResultStatus();
        lblExpiryDateVal.setText("");
        lblTotAmtVal.setText("");
        cbCustomer.setEnabled(false);
        transactionUI.setCallingAmount("0");
        lblDateVal.setText("");
//        lblActNum.setText("");
        panTransaction.setEnabled(false);
        rdoRentCollection.setSelected(false);
        chkGroup.clearSelection();
        lblServiceTaxval.setText("");
    }

    /*
     * Does necessary operaion when user clicks the save button
     */
    private void savePerformed() {
        updateOBFields();
        String action;
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            action = CommonConstants.TOSTATUS_INSERT;
            saveAction(action);
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            action = CommonConstants.TOSTATUS_UPDATE;
            saveAction(action);
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
            action = CommonConstants.TOSTATUS_DELETE;
            saveAction(action);
        }
    }

    /*
     * Method used to showPopup ViewAll by Executing a Query
     */
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        //        ArrayList lst = new ArrayList();
        //        lst.add("CONFIG_ID");
        //        viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
        //        lst = null;
        if (currField.equals("LockerNo")) {
            HashMap where = new HashMap();
            HashMap h = new HashMap();
            where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            String PRODUCT_ID = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdID.getModel()).getKeyForSelected());
            where.put("PRODUCT_ID", PRODUCT_ID);
            viewMap.put(CommonConstants.MAP_NAME, "getLockSurList");
            viewMap.put(CommonConstants.MAP_WHERE, where);
            where = null;
            //  h.put("BRANCH_ID",TrueTransactMain.BRANCH_ID);

            //                 new ViewAll(this, viewMap).show();
        }else if (currField.equals("LockerListClosednDue")) {
            HashMap where = new HashMap();
//            HashMap viewMapp = new HashMap();
            where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_NAME, "LockerListClosednDueDtails");
            viewMap.put(CommonConstants.MAP_WHERE, where);
            where = null;
//            new ViewAll(this, viewMap).show();
        } 
        else {
            viewMap.put(CommonConstants.MAP_NAME, "getSelectLockerSurEdit");
            HashMap where = new HashMap();
            where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, where);
            where = null;
        }
        new ViewAll(this, viewMap).show();
    }

    private void removeRadioButtons() {
        rdgSurOrRenew.remove(rdoSurrender);
        rdgSurOrRenew.remove(rdoRenew);
        rdgSurOrRenew.remove(rdoBreakOpen);
        rdgSurOrRenew.remove(rdoRentCollection);
    }

    private void addRadioButtons() {// these r all radio button purpose adding...
        rdgSurOrRenew = new CButtonGroup();
        rdgSurOrRenew.add(rdoSurrender);
        rdgSurOrRenew.add(rdoRenew);
        rdgSurOrRenew.add(rdoBreakOpen);
        rdgSurOrRenew.add(rdoRentCollection);
    }
    /*
     * Fills up the HashMap with data when user selects the row in ViewAll
     * screen
     */

    public void fillData(Object map) {
        setModified(true);
        HashMap hash = (HashMap) map;
        HashMap hhmap = new HashMap();
        //        System.out.println("#@$#$!@!@ hash inside fillData:"+hash);
        transactionUI.resetObjects();
        transactionUI.cancelAction(true);
        hash.put("VIEW_TYPE", viewType);
        System.out.println("map here from authorize list"+map);
         if (hash.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")) {
            fromNewAuthorizeUI= true;
            newauthorizeListUI = (NewAuthorizeListUI) hash.get("PARENT");
            setAuthorizeStatus("AUTHORIZE_BUTTON");
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            btnReject.setEnabled(false);
            rejectFlag = 1;
            btnAuthorize.setEnabled(true);
        }
        if (hash.containsKey("FROM_AUTHORIZE_LIST_UI")) {
            System.out.println("hash.get(PARENT) tD" + hash.get("PARENT"));
            fromAuthorizeUI = true;
            authorizeListUI = (AuthorizeListUI) hash.get("PARENT");
            setAuthorizeStatus("AUTHORIZE_BUTTON");
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            btnReject.setEnabled(false);
            rejectFlag = 1;
            btnAuthorize.setEnabled(true);
        }
        if (viewType != null) {
            if (viewType.equals(ClientConstants.ACTION_STATUS[2])
                    || viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                System.out.println("inside first if");
                transactionUI.displayMode();
                HashMap where = new HashMap();
                where.put("OPERATION_ID", hash.get("OPERATION_ID"));
                observable.setOptID(CommonUtil.convertObjToStr(hash.get("OPERATION_ID")));
                where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                hash.put(CommonConstants.MAP_WHERE, CommonUtil.convertObjToStr(hash.get("REMARKS")));
                hash.put("LOCKER_NUM", CommonUtil.convertObjToStr(hash.get("LOCKER_NUM")));
                hash.put("CUST_ID", CommonUtil.convertObjToStr(hash.get("CUST_ID")));
                hash.put("SURRENDER_ID", CommonUtil.convertObjToStr(hash.get("REMARKS")));
                setSurOrRenew(CommonUtil.convertObjToStr(hash.get("SUR_OR_RENEW")));
                setIssueID(CommonUtil.convertObjToStr(hash.get("ISSUE_ID")));
                setSurrenderID(CommonUtil.convertObjToStr(hash.get("REMARKS")));
                String sdate = CommonUtil.convertObjToStr(hash.get("SURRENDER_DT"));
                observable.setRemarks(getSurrenderID());
                observable.populateData(hash);
                //performRdoAction();
                if(viewType.equals(AUTHORIZE) || viewType.equals(ClientConstants.ACTION_STATUS[2])){
                    txtLockerNo.setText(CommonUtil.convertObjToStr(hash.get("LOCKER_NUM")));
                    cboProdID.setSelectedItem(observable.getCboProdID());
                    txtCollectRentMM.setText(observable.getCollectRentMM());
                    txtCollectRentyyyy.setText(observable.getCollectRentYYYY());
                    txtActNum.setText(getIssueID());
//                    lblActNum.setText(observable.getTxtActNum());
                    chkDefaulter.setEnabled(false);
                    chkNoTrans.setEnabled(false);
                    if (CommonUtil.convertObjToStr(hash.get("DEFAULTER")).equals("DEFAULTER")) {                        
                        chkDefaulter.setSelected(true);  
                        observable.setChkDefaulter("DEFAULTER");
                    } 
                    if (CommonUtil.convertObjToStr(hash.get("NOTRANS")).equals("NOTRANS")) {
                        chkNoTrans.setSelected(true);
                    }
                }
                String lcno = txtLockerNo.getText();
                String ltype = CommonUtil.convertObjToStr(((ComboBoxModel) (cboProdID.getModel())).getKeyForSelected());
                if (observable.getAllowedTransactionDetailsTO() != null && observable.getAllowedTransactionDetailsTO().size() > 0) {
                    panTransaction.setEnabled(true);
                    System.out.println("the values are " + observable.getAllowedTransactionDetailsTO());
                }     
                /**
                 * if
                 * (CommonUtil.convertObjToStr(objLockerIssueTO.getExpDt()).length()>0)
                 * { // If Expiry date is greater than current date // Date
                 * expDt =
                 * (Date)DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hash.get("EXP_DT")));
                 * if (DateUtil.dateDiff(currDt, objLockerIssueTO.getExpDt())<0)
                 * { // displayAlert("This locker already expired...");
                 * lblExpiryDateVal.setText(
                 * CommonUtil.convertObjToStr(objLockerIssueTO.getExpDt()));
                 *
                 * }
                 * }
                 * lblDateVal.setText(DateUtil.getStringDate(objLockerIssueTO.getCreateDt()));
                 */
                isFilled = true;
                setButtonEnableDisable();
                if (viewType.equals(AUTHORIZE)) {
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                    btnCancel.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    lblExpiryDateVal.setText(observable.getDate());
                    lblDateVal.setText(observable.getLblDateVal());
                    btnCancel.setEnabled(true);
                    btnAuthorize.setEnabled(true);
                    btnAuthorize.requestFocusInWindow(); 
                    btnAuthorize.setFocusable(true);
                }

                if (!viewType.equals(AUTHORIZE)) {
                    performRdoAction();

                }
                if (CommonUtil.convertObjToDouble(observable.getTxtCharges()).doubleValue() > 0.0 ||
                        CommonUtil.convertObjToDouble(observable.getTxtRefund()).doubleValue() > 0.0) {
                    lblTotAmtVal.setText(String.valueOf(
                            CommonUtil.convertObjToDouble(txtCharges.getText()).doubleValue()
                            + CommonUtil.convertObjToDouble(txtServiceTax.getText()).doubleValue()));
                    //  btnCancel.setEnabled(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT ? true : false);
                    // btnSave.setEnabled(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT ? false : false);
                    txtLockerNo.setText(CommonUtil.convertObjToStr(hash.get("LOCKER_NUM")));
                    cboProdID.setSelectedItem(observable.getCboProdID());
                    txtCollectRentMM.setText(observable.getCollectRentMM());
                    txtCollectRentyyyy.setText(observable.getCollectRentYYYY());
                    double charges = CommonUtil.convertObjToDouble(observable.getTxtCharges()).doubleValue();
                    double serviceTax = CommonUtil.convertObjToDouble(observable.getServiceTax()).doubleValue();
                    double penal = CommonUtil.convertObjToDouble(observable.getTxtPenalAmt()).doubleValue();
                    txtCharges.setText(observable.getTxtCharges());
                    txtServiceTax.setText(observable.getServiceTax());
                    txtPenalAmt.setText(observable.getTxtPenalAmt());
                    lblTotAmtVal.setText(String.valueOf(charges + serviceTax + penal));
                    btnCancel.setEnabled(true);
                    lblDateVal.setText(observable.getLblDateVal());
                    lblExpiryDateVal.setText(observable.getDate());
                    btnSave.setEnabled(false);
                    txtRefund.setText(observable.getTxtRefund());
                    //com.see.truetransact.transferobject.locker.lockerissue.LockerIssueTO objLockerIssueTO =
                    // (com.see.truetransact.transferobject.locker.lockerissue.LockerIssueTO) hash.get("LockerTO");
                    // lblExpiryDateVal.setText(DateUtil.getStringDate(objLockerIssueTO.getExpDt()));
                    panTransaction.setEnabled(true);
                    if (observable.isSurrender() == true) {
                        lblSurdate.setEnabled(true);
                        lblSurDate.setEnabled(true);
                        lblSurDate.setText(sdate);
                    } else {
                        lblSurDate.setEnabled(false);
                        lblSurdate.setEnabled(false);
                    }
                } 
                /*else if () {
                    panTransaction.setEnabled(true);
                    //btnCancel.setEnabled(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT ? true : false);
                    //  btnCancel.setEnabled(observable.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE? true : false);
                    btnCancel.setEnabled(true);
                    lblDateVal.setText(observable.getLblDateVal());
                    panTransaction.setEnabled(true);
                    lblExpiryDateVal.setText(observable.getDate());
                    if (observable.isSurrender() == true) {
                        lblSurdate.setEnabled(true);
                        lblSurDate.setEnabled(true);
                        lblSurDate.setText(sdate);
                    } else {
                        lblSurDate.setEnabled(false);
                        lblSurdate.setEnabled(false);
                    }
                    btnSave.setEnabled(false);
                }*/
                //update(null,null);
            } else if (viewType.equals("LockerNo")||viewType.equals("LockerListClosednDue")) {
                System.out.println("inside second if");                         
                        System.out.println("pro id"+observable.getCboProdID());
                        HashMap headMap = new HashMap();
                        headMap.put("value",CommonUtil.convertObjToStr(((ComboBoxModel) (cboProdID.getModel())).getKeyForSelected()));
                lockerTransHeadList = ClientUtil.executeQuery("getLockerAccountHeads", headMap);// Added by nithya for GST changes   
                if (viewType.equals("LockerListClosednDue")) {                   
                    rdoSurrender.setSelected(true);
                    pendingPayment = false;
                    lblSurDate.setText(CommonUtil.convertObjToStr(hash.get("CLOSED_DT")));
                    performRdoAction();
                    chkDefaulter.setEnabled(false);
                    chkNoTrans.setEnabled(false);
                    if (CommonUtil.convertObjToStr(hash.get("LOCKER_STATUS_ID")).equals("DEFAULTER")) {
                        chkDefaulter.setSelected(true);                       
                        //((ComboBoxModel) (cboProdID.getModel())).setKeyForSelected(CommonUtil.convertObjToStr(hash.get("PROD_ID")));                       
                        System.out.println("pro id" + observable.getCboProdID());
                        pendingPayment = true;
                        btnSave.setEnabled(true);
                        btnCancel.setEnabled(true);
                        btnNew.setEnabled(false);
                        btnEdit.setEnabled(false);
                        btnDelete.setEnabled(false);
                    } else {
                        btnSave.setEnabled(false);
                        btnCancel.setEnabled(true);
//                        transactionUI.displayMode();
//                        observable.populateData(hash);
//                        if (observable.getAllowedTransactionDetailsTO() != null && observable.getAllowedTransactionDetailsTO().size() > 0) {
//                           panTransaction.setEnabled(true);
//                            System.out.println("the values are " + observable.getAllowedTransactionDetailsTO());
//                        }
                    }              
                    ((ComboBoxModel) (cboProdID.getModel())).setKeyForSelected(CommonUtil.convertObjToStr(hash.get("PROD_ID")));                   
                    System.out.println("pending pay here"+pendingPayment);                    
                }
                prodid= CommonUtil.convertObjToStr(hash.get("PROD_ID"));
               ((ComboBoxModel) (cboProdID.getModel())).setKeyForSelected(CommonUtil.convertObjToStr(hash.get("PROD_ID"))); 
                txtLockerNo.setText(CommonUtil.convertObjToStr(hash.get("LOCKER_NUM")));
                lblCustNameVal.setText(CommonUtil.convertObjToStr(hash.get("NAME")));
                lblCustomerIdVal.setText(CommonUtil.convertObjToStr(hash.get("CUST_ID")));
                lblDateVal.setText(CommonUtil.convertObjToStr(hash.get("ISSUE_DT")));
                lblModeOfOpVal.setText(CommonUtil.convertObjToStr(hash.get("MODE_OF_OPERATION")));
                lblExpiryDateVal.setText(CommonUtil.convertObjToStr(hash.get("EXP_DT")));
                String no = CommonUtil.convertObjToStr(hash.get("ISSUE_ID"));
                txtActNum.setText(no);
                transactionUI.setCallingAccNo(no);
                HashMap hm = new HashMap();
                hm.put("REMARKS", no);
                List s = ClientUtil.executeQuery("getTransDetails", hm);
                if (s != null && s.size() > 0) {
                    System.out.println("inside gettrans details");
                    hm = null;
                    hm = (HashMap) s.get(0);
                    String prodid = CommonUtil.convertObjToStr(hm.get("PRODUCT_ID"));
                    String protype = CommonUtil.convertObjToStr(hm.get("PROD_TYPE"));
                    String cust = CommonUtil.convertObjToStr(hm.get("CUSTOMER_ID_CR"));
                    String cname = CommonUtil.convertObjToStr(hm.get("CUSTOMER_NAME"));
                    transactionUI.setCallingProdID(prodid);
                    transactionUI.setCallingTransProdType(protype);
                    transactionUI.setCallingTransAcctNo(cust);
                }
                expDt = (Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hash.get("EXP_DT")));
                if (rdoBreakOpen.isSelected() == true) {
                    System.out.println("111111");
                    if (cbCustomer.isSelected() == true) {
                        if (DateUtil.dateDiff(expDt, currDt) > 0) {
                            int a = ClientUtil.confirmationAlert("Locker period has expired.Do you want to collect the locker rent,Before break open ?");
                            int b = 0;
                            /*if (a == b) {
                                btnCancelActionPerformed(null);
                            }*/
                        }
                        final String pid = CommonUtil.convertObjToStr(((ComboBoxModel) (cboProdID.getModel())).getKeyForSelected());
                        System.out.println("pid" + pid);
                        setLockerCha(pid);
                        double penal = 0.0;
                        if (txtPenalAmt.getText() != null && !txtPenalAmt.getText().equals("")) {
                            penal = Double.parseDouble(txtPenalAmt.getText());
                        }
                        System.out.println("penal111111" + penal);
                        double db = 0;
                        double dc = 0;
                        double dval = 0;
                        double dd = 0;
                        double ser = 0;
                        double val = 0;
                        if (lblDateVal.getText().length() > 0) {
                            String lval = lblDateVal.getText();
                            java.util.Date ldate = DateUtil.getDateMMDDYYYY(lval);
                            int lday = ldate.getDate();
                            int lmonth = ldate.getMonth() + 1;;
                            int lyear = ldate.getYear() + 1900;
                            String lex = lblExpiryDateVal.getText();


                            java.util.Date edate = DateUtil.getDateMMDDYYYY(lex);
                            int eday = edate.getDate();
                            int emonth = edate.getMonth() + 1;
                            int eyear = edate.getYear() + 1900;
                        }
                        final String prodID = CommonUtil.convertObjToStr(((ComboBoxModel) (cboProdID.getModel())).getKeyForSelected());
                        HashMap stMap = new HashMap();
                        stMap.put("PROD_ID", prodID);
                        stMap.put("TODAY_DT", currDt);
                        stMap.put("CHARGE_TYPE", "BREAK_CHARGES");
                        System.out.println("@#$@#$$#stMap" + stMap);
                        List stList = ClientUtil.executeQuery("startDateforBreakOpen", stMap);
                        System.out.println("stList" + stList);
                        if (stList != null && stList.size() > 0) {
                            System.out.println("stList" + stList);
                            stMap = null;
                            stMap = (HashMap) stList.get(0);
                            double comm = CommonUtil.convertObjToDouble(stMap.get("COMMISION")).doubleValue();
                            double serviceTax = CommonUtil.convertObjToDouble(stMap.get("SERVICE_TAX")).doubleValue();
                            double serv = (comm / 100) * serviceTax;
                            dval = comm + serv;
                            System.out.println("dvalooooooooooo" + dval);
                            txtServiceTax.setText(String.valueOf(serv));
                            txtCharges.setText(String.valueOf(comm));
                            lblTotAmtVal.setText(String.valueOf(comm + serv + penal));
                            transactionUI.setCallingAmount(String.valueOf(dval + penal));
                            transactionUI.setCallingApplicantName(lblCustNameVal.getText());
                            if (dval > 0) {
                                panTransaction.setEnabled(true);
                            }
                        }
                    } else {
                        txtServiceTax.setEnabled(false);
                        txtCharges.setEnabled(false);
                        txtPenalAmt.setEnabled(false);
                        txtPenalAmt.setText("");
                    }

                } else if (rdoSurrender.isSelected() == true) {
                    String lex = lblExpiryDateVal.getText();
                   // txtCharges.setEnabled(false);
                  //  txtServiceTax.setEnabled(false);
                   lblTotAmtVal.setEnabled(false);
                  //  txtPenalAmt.setEnabled(false);
                    lblExpiryDate.setText("Expiry Date");
//                    double db = 0;
//                    double dc = 0;
//                    double dval = 0;
//                    double dd = 0;
//                    double ser = 0;
//                    double val = 0;
//                    double dd1 = 0;
//                    double ser1 = 0;
//                    double val1 = 0;
//                    double dd2 = 0;
//                    double ser2 = 0;
//                    double val2 = 0;
//                    double dd3 = 0;
//                    double ser3 = 0;
//                    double val3 = 0;
//                    java.util.Date edate = DateUtil.getDateMMDDYYYY(lex);
//                    int lday = edate.getDate();
//                    int lmonth = edate.getMonth() + 1;;
//                    int lyear = edate.getYear() + 1900;
//                    java.util.Date ldate = currDt;
//                    java.util.Date c = currDt;
//                    int cday = c.getDate();
//                    int cmonth = c.getMonth() + 1;
//                    int cyear = c.getYear() + 1900;
//                    Date eetempDate;
//                    Date edt;
//                    int realmont = 0;
//                    int m;
//                    int i;
                    txtRefund.setEnabled(false);
                    String prodid = CommonUtil.convertObjToStr(((ComboBoxModel) (cboProdID.getModel())).getKeyForSelected());
                    HashMap surrenderRefundMap = new HashMap();
                    surrenderRefundMap.put("PROD_ID", prodid);
                    List list = ClientUtil.executeQuery("getSurrenderRefund", surrenderRefundMap);
                    surrenderRefundMap.put("EXPIRY_DT",lblExpiryDateVal.getText());
                    surrenderRefundMap.put("CURRENT_DATE",DateUtil.getStringDate(currDt));
                    List listMonths = ClientUtil.executeQuery("getSurrenderRefundMonths", surrenderRefundMap);
                    surrenderRefundMap = new HashMap();
                    surrenderRefundMap = (HashMap) listMonths.get(0);
                    double differenceMonth = CommonUtil.convertObjToDouble(surrenderRefundMap.get("MONTHS"));
                    if(list!=null && list.size()>0){
//                        surrenderRefundMap.put("EXPIRY_DT",lblExpiryDateVal.getText());
//                        surrenderRefundMap.put("CURRENT_DATE",DateUtil.getStringDate(currDt));
//                        list = ClientUtil.executeQuery("getSurrenderRefundMonths", surrenderRefundMap);
                        if(listMonths!=null && listMonths.size()>0){
//                            surrenderRefundMap = new HashMap();
//                            surrenderRefundMap = (HashMap) list.get(0);
//                            double differenceMonth = CommonUtil.convertObjToDouble(surrenderRefundMap.get("MONTHS"));
                            if(differenceMonth>1){
                                chkDefaulter.setEnabled(false);
                                System.out.println("inside refund ad");
                                surrenderRefundMap = new HashMap();
                                final String prodID = CommonUtil.convertObjToStr(((ComboBoxModel) (cboProdID.getModel())).getKeyForSelected());
                                surrenderRefundMap.put("PROD_ID", prodID);
                                surrenderRefundMap.put("TODAY_DT", currDt);
                                surrenderRefundMap.put("CHARGE_TYPE", "RENT_CHARGES");
                                List stList = ClientUtil.executeQuery("getServiceTaxAndComissionForIssue", surrenderRefundMap);
                                if (stList != null && stList.size() > 0) {
                                    surrenderRefundMap = (HashMap) stList.get(0);
                                    //txtCharges.setText(CommonUtil.convertObjToStr(surrenderRefundMap.get("COMMISION")));
                                    double rentAmount = CommonUtil.convertObjToDouble(surrenderRefundMap.get("COMMISION")).doubleValue();
                                    double serviceTax = CommonUtil.convertObjToDouble(surrenderRefundMap.get("SERVICE_TAX")).doubleValue();
                                    double refundAmount = differenceMonth * rentAmount /12;
                                    refundAmount = rd.getNearest((long)(refundAmount*100), 100)/100;
                                    txtRefund.setText(CommonUtil.convertObjToStr(refundAmount));
                                    transactionUI.setCallingAmount(String.valueOf(refundAmount));
                                    transactionUI.setCallingApplicantName(lblCustNameVal.getText());
                                    lblTotAmtVal.setText(CommonUtil.convertObjToStr(refundAmount));
                                    panTransaction.setEnabled(true);
                                }else{
                                    txtRefund.setText("0");
                                    //panTransaction.setEnabled(false);
                                }
                            }
                         
                      }
                        
                    }else{
                        txtRefund.setText("0");
                        //panTransaction.setEnabled(false);
                    }
                    if (differenceMonth < 1) {
                        final String pid = CommonUtil.convertObjToStr(((ComboBoxModel) (cboProdID.getModel())).getKeyForSelected());
                        setLockerCha(pid);                      
                        txtCollectRentyyyyFocusLost(null);
                        panTransaction.setEnabled(true);
                    }
//                    if ((lyear == cyear && lmonth > cmonth) || lyear < cyear) {
//                        // if(observable.isCbRefundYes()==true){
//                        int p = 0;
//                        System.out.println("newdddddddddddddddddddddd");
//                        for (i = lyear; i <= cyear; i++) {
//                            // if((lyear==cyear && lmonth>cmonth)|| lyear<cyear){
//                            // if((lyear<=cyear && lmonth<cmonth)|| lyear>cyear){
//                            //if((lyear=cyear&&lmonth<cmonth) || lyear>cyear){
//                            final String prodID = CommonUtil.convertObjToStr(((ComboBoxModel) (cboProdID.getModel())).getKeyForSelected());
//                            System.out.println("prodID" + prodID);
//                            setLockerCha(prodID);
//                            if (lyear >= i) {
//                                System.out.println("newccccccccccccccccccccccccccc");
//                                HashMap stMap = new HashMap();
//                                Date tempDt = (Date) currDt.clone();
//                                tempDt.setDate(edate.getDate());
//                                tempDt.setMonth(edate.getMonth());
//                                tempDt.setYear(edate.getYear());
//                                Date currDate = (Date) currDt.clone();
//                                currDate.setDate(31);
//                                currDate.setMonth(11);
//                                currDate.setYear(i - 1900);
//                                stMap.put("PROD_ID", prodID);
//                                stMap.put("TODAY_DT", tempDt);
//                                stMap.put("EXP_DT", currDate);
//                                stMap.put("CHARGE_TYPE", "RENT_CHARGES");
//                                System.out.println("@#$@#$$#stMap" + stMap);
//                                List stList = ClientUtil.executeQuery("getServiceTaxAndComission", stMap);
//                                System.out.println("diffcurmont000000000");
//                                int diffcurmont = edate.getMonth() + 1;
//                                int calmonth = 12 - diffcurmont;
//                                System.out.println("calmonth000000000" + calmonth);
//                                if (stList != null && stList.size() > 0) {
//                                    System.out.println("ccccccccccccccccc");
//                                    stMap = null;
//                                    for (int k = 0; k < stList.size(); k++) {
//                                        stMap = (HashMap) stList.get(k);
//                                        System.out.println("stMap" + stMap);
//                                        double comm = CommonUtil.convertObjToDouble(stMap.get("COMMISION")).doubleValue();
//                                        double serviceTax = CommonUtil.convertObjToDouble(stMap.get("SERVICE_TAX")).doubleValue();
//                                        String sdate = CommonUtil.convertObjToStr(stMap.get("START_DT"));
//                                        String eedate = CommonUtil.convertObjToStr(stMap.get("END_DT"));
//                                        System.out.println("eedate" + eedate);
//                                        if (eedate != null && !eedate.equals("")) {
//                                            System.out.println("newlllllllllllllllllllllllllll");
//                                            edt = DateUtil.getDateMMDDYYYY(eedate);
//                                            System.out.println("edt44444444444" + edt);
//                                            Date comparedt = (Date) currDt.clone();
//                                            System.out.println("comparedt44444444" + comparedt);
//                                            comparedt.setDate(edt.getDate());
//                                            comparedt.setMonth(edt.getMonth());
//                                            comparedt.setYear(edt.getYear());
//                                            int eeyear = edt.getYear() + 1900;
//                                            int eemonth = edt.getMonth() + 1;
//                                            int ccalmonth = tempDt.getMonth();
//                                            Date ssdate = DateUtil.getDateMMDDYYYY(sdate);
//                                            int syear = ssdate.getYear() + 1900;
//                                            int smonth = ssdate.getMonth() + 1;
//                                            int diffmonth = (eeyear - syear) * 12 + (eemonth - smonth);
//                                            //realmont=calmonth-diffmonth+1;
//                                            if (eeyear == i && DateUtil.dateDiff(edate, comparedt) >= 0) {
//                                                // if(calmonth<=eemonth)
//                                                // realmont=calmonth;
//                                                //  else if(calmonth>eemonth)
//                                                if (p == 0) {
//                                                    realmont = eemonth - (edate.getMonth() + 1);
//                                                } else {
//                                                    realmont = (eemonth - smonth) + 1;
//                                                }
//                                                double d = comm / 12.0;
//                                                dd3 = dd3 + d * realmont;
//                                                dd = (double) getNearest((long) (dd3 * 100), 100) / 100;
//                                                ser3 = ((d * realmont) / 100) * serviceTax;
//                                                ser = (double) getNearest((long) (ser3 * 100), 100) / 100;
//                                                val3 = dd + ser;
//                                                val = (double) getNearest((long) (val3 * 100), 100) / 100;
//                                                calmonth = calmonth - realmont;
//                                                p++;
//                                            } else if (eeyear > i) {
//                                                if (calmonth != 0) {
//                                                    realmont = calmonth;
//                                                    double d = comm / 12.0;
//                                                    dd = dd3 + d * realmont;
//                                                    dd = (double) getNearest((long) (dd * 100), 100) / 100;
//                                                    ser = ser3 + ((d * realmont) / 100) * serviceTax;
//                                                    ser = (double) getNearest((long) (ser * 100), 100) / 100;
//                                                    val = val3 + dd + ser;
//                                                    val = (double) getNearest((long) (val * 100), 100) / 100;
//                                                    calmonth = calmonth - realmont;
//                                                }
//                                            }
//                                            System.out.println("newllll" + val);
//                                        } else {
//                                            System.out.println("calmonth" + calmonth);
//                                            if (calmonth != 0) {
//                                                double d = comm / 12.0;
//                                                dd = dd3 + d * calmonth;
//                                                dd = (double) getNearest((long) (dd * 100), 100) / 100;
//                                                ser = ser3 + ((d * calmonth) / 100) * serviceTax;
//                                                ser = ser3 + (double) getNearest((long) (ser * 100), 100) / 100;
//                                                val = val3 + dd + ser;
//                                                val = (double) getNearest((long) (val * 100), 100) / 100;
//                                                System.out.println("val" + val);
//                                            }
//                                        }
//                                    }
//                                }
//                            } else if (cyear == i) {
//                                System.out.println("newbbbbbbbbbbbbbbbbbbbbbbbbb");
//                                HashMap stMap = new HashMap();
//                                Date tempDt = (Date) currDt.clone();
//                                tempDt.setDate(1);
//                                tempDt.setMonth(0);
//                                tempDt.setYear(i - 1900);
//                                Date currDate = (Date) currDt.clone();
//                                currDate.setDate(currDt.getDate());
//                                currDate.setMonth(currDt.getMonth());
//                                currDate.setYear(i - 1900);
//                                stMap.put("PROD_ID", prodID);
//                                stMap.put("TODAY_DT", tempDt);
//                                stMap.put("EXP_DT", currDate);
//                                stMap.put("CHARGE_TYPE", "RENT_CHARGES");
//                                List stList = ClientUtil.executeQuery("getServiceTaxAndComission", stMap);
//                                int diffcurmont = currDt.getMonth() + 1;
//                                int calmonth = diffcurmont;
//                                if (stList != null && stList.size() > 0) {
//                                    stMap = null;
//                                    for (int k = 0; k < stList.size(); k++) {
//                                        stMap = (HashMap) stList.get(k);
//                                        double comm = CommonUtil.convertObjToDouble(stMap.get("COMMISION")).doubleValue();
//                                        double serviceTax = CommonUtil.convertObjToDouble(stMap.get("SERVICE_TAX")).doubleValue();
//                                        String sdate = CommonUtil.convertObjToStr(stMap.get("START_DT"));
//                                        String eedate = CommonUtil.convertObjToStr(stMap.get("END_DT"));
//                                        if (eedate != "") {
//                                            edt = DateUtil.getDateMMDDYYYY(eedate);
//                                            int eeyear = edt.getYear() + 1900;
//                                            int eemonth = edt.getMonth() + 1;
//                                            Date ssdate = DateUtil.getDateMMDDYYYY(sdate);
//                                            int syear = ssdate.getYear() + 1900;
//                                            int smonth = ssdate.getMonth() + 1;
//                                            int diffmonth = (eeyear - syear) * 12 + (eemonth - smonth);
//                                            realmont = calmonth - (diffmonth + 1);
//                                            if (eeyear == i) {
//                                                if (calmonth != 0) {
//                                                    if (calmonth <= eemonth) {
//                                                        realmont = calmonth;
//                                                    } else if (calmonth > eemonth) {
//                                                        realmont = eemonth;
//                                                    }
//                                                    double d = comm / 12.0;
//                                                    dd2 = (d * realmont);
//                                                    dd = (double) getNearest((long) (dd2 * 100), 100) / 100;
//                                                    ser2 = ((d * realmont) / 100) * serviceTax;
//                                                    ser = (double) getNearest((long) (ser2 * 100), 100) / 100;
//                                                    val2 = dd2 + ser2;
//                                                    val = (double) getNearest((long) (val2 * 100), 100) / 100;
//                                                    calmonth = calmonth - realmont;
//                                                }
//                                            } else if (eeyear > i) {
//                                                if (calmonth != 0) {
//                                                    realmont = calmonth;
//                                                    double d = comm / 12.0;
//                                                    dd = dd2 + (d * realmont);
//                                                    dd = (double) getNearest((long) (dd * 100), 100) / 100;
//                                                    ser = ser2 + ((d * realmont) / 100) * serviceTax;
//                                                    ser = (double) getNearest((long) (ser * 100), 100) / 100;
//                                                    val = val2 + dd + ser;
//                                                    val = (double) getNearest((long) (val * 100), 100) / 100;
//                                                    calmonth = calmonth - realmont;
//                                                }
//                                            }
//                                        } else {
//                                            if (calmonth != 0) {
//                                                realmont = calmonth;
//                                                double d = comm / 12.0;
//                                                dd = dd2 + (d * realmont);
//                                                dd = (double) getNearest((long) (dd * 100), 100) / 100;
//                                                ser = ser2 + ((d * realmont) / 100) * serviceTax;
//                                                ser = (double) getNearest((long) (ser * 100), 100) / 100;
//                                                val = val2 + dd + ser;
//                                                val = (double) getNearest((long) (val * 100), 100) / 100;
//                                                calmonth = calmonth - realmont;
//                                            }
//                                        }
//                                    }
//                                }
//                                System.out.println("valnnnnnnnnnnnn" + val);
//                                // lyear=lyear+1;
//                            } // lmonth=m;
//                            else {
//                                System.out.println("newaaaaaaaaaaaaaaaaaaa");
//                                HashMap stMap = new HashMap();
//                                Date tempDt = (Date) currDt.clone();
//                                tempDt.setDate(1);
//                                tempDt.setMonth(0);
//                                tempDt.setYear(i - 1900);
//                                Date currDate = (Date) currDt.clone();
//                                currDate.setDate(31);
//                                currDate.setMonth(11);
//                                currDate.setYear(i - 1900);
//                                stMap.put("PROD_ID", prodID);
//                                stMap.put("TODAY_DT", tempDt);
//                                stMap.put("EXP_DT", currDate);
//                                stMap.put("CHARGE_TYPE", "RENT_CHARGES");
//                                List stList = ClientUtil.executeQuery("getServiceTaxAndComission", stMap);
//                                int diffcurmont = edate.getMonth() + 1;
//                                int calmonth = 12;
//                                if (stList != null && stList.size() > 0) {
//                                    stMap = null;
//                                    for (int k = 0; k < stList.size(); k++) {
//                                        stMap = (HashMap) stList.get(k);
//
//                                        double comm = CommonUtil.convertObjToDouble(stMap.get("COMMISION")).doubleValue();
//                                        double serviceTax = CommonUtil.convertObjToDouble(stMap.get("SERVICE_TAX")).doubleValue();
//                                        String sdate = CommonUtil.convertObjToStr(stMap.get("START_DT"));
//                                        String eedate = CommonUtil.convertObjToStr(stMap.get("END_DT"));
//                                        System.out.println("eedatenew" + eedate);
//                                        if (eedate != null && !eedate.equals("")) {
//                                            System.out.println("eedatenew" + eedate);
//                                            edt = DateUtil.getDateMMDDYYYY(eedate);
//                                            int eeyear = edt.getYear() + 1900;
//                                            int eemonth = edt.getMonth() + 1;
//                                            Date ssdate = DateUtil.getDateMMDDYYYY(sdate);
//                                            int syear = ssdate.getYear() + 1900;
//                                            int smonth = ssdate.getMonth() + 1;
//                                            int diffmonth = (eeyear - syear) * 12 + (eemonth - smonth);
//                                            if (eeyear == i) {
//                                                if (calmonth != 0) {
//                                                    if (calmonth <= eemonth) {
//                                                        realmont = calmonth;
//                                                    } else if (calmonth > eemonth) {
//                                                        realmont = eemonth;
//                                                    }
//                                                    double d = comm / 12.0;
//
//                                                    dd1 = dd1 + (d * realmont);
//                                                    dd = (double) getNearest((long) (dd1 * 100), 100) / 100;
//                                                    ser1 = ser1 + ((d * realmont) / 100) * serviceTax;
//                                                    ser = (double) getNearest((long) (ser1 * 100), 100) / 100;
//                                                    val1 = val1 + dd1 + ser1;
//                                                    val = (double) getNearest((long) (val1 * 100), 100) / 100;
//                                                    calmonth = calmonth - realmont;
//                                                }
//                                            } else {
//                                                if (eeyear > i) {
//                                                    if (calmonth != 0) {
//                                                        realmont = calmonth;
//                                                        //  if( calmonth-(diffmonth+1)>0)
//                                                        //   {
//                                                        double d = comm / 12.0;
//                                                        dd = dd1 + (d * realmont);
//                                                        dd = (double) getNearest((long) (dd * 100), 100) / 100;
//                                                        ser = ser1 + ((d * realmont) / 100) * serviceTax;
//                                                        ser = (double) getNearest((long) (ser * 100), 100) / 100;
//                                                        val = val1 + dd + ser;
//                                                        val = (double) getNearest((long) (val * 100), 100) / 100;
//                                                        calmonth = calmonth - realmont;
//                                                    }
//                                                }
//                                            }
//                                            /*
//                                             * else{
//                                             *
//                                             * double d=comm/12.0;
//                                             * dd=d*realmont; dd =
//                                             * (double)getNearest((long)(dd
//                                             * *100),100)/100; ser=(dd/100)*
//                                             * serviceTax; ser =
//                                             * (double)getNearest((long)(ser
//                                             * *100),100)/100; val=dd+ser; val =
//                                             * (double)getNearest((long)(val
//                                             * *100),100)/100;
//                                             * calmonth=calmonth-realmont;
//                                    }
//                                             */
//                                        } else {
//                                            System.out.println("calmonth00000" + calmonth);
//                                            if (calmonth != 0) {
//                                                System.out.println("valnew" + val);
//                                                double d = comm / 12.0;
//                                                dd = dd1 + (d * calmonth);
//                                                dd = (double) getNearest((long) (dd * 100), 100) / 100;
//                                                ser = ser1 + ((d * calmonth) / 100) * serviceTax;
//                                                ser = (double) getNearest((long) (ser * 100), 100) / 100;
//                                                val = val1 + dd + ser;
//                                                val = (double) getNearest((long) (val * 100), 100) / 100;
//                                                calmonth = calmonth - realmont;
//                                                System.out.println("valnew" + val);
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                            db = db + ser;
//                            dc = dc + dd;
//                            dval = db + dc;
//                            dd1 = 0;
//                            dd2 = 0;
//                            dd3 = 0;
//                            val1 = 0;
//                            val2 = 0;
//                            val3 = 0;
//                            ser1 = 0;
//                            ser2 = 0;
//                            ser3 = 0;
//                        }
//                        txtServiceTax.setText(String.valueOf(db));
//                        txtCharges.setText(String.valueOf(dc));
//                        txtRefund.setEnabled(false);
//                        System.out.println("new===1===========" + dval);
//                        double penal = 0.0;
//                        if (!CommonUtil.convertObjToStr(txtPenalAmt.getText()).equals("") && CommonUtil.convertObjToStr(txtPenalAmt.getText()).length() > 0) {
//                            penal = Double.parseDouble(txtPenalAmt.getText());
//                        }
//                        System.out.println("penal===========" + penal);
//                        lblTotAmtVal.setText(String.valueOf(dval + penal));
//                        transactionUI.setCallingAmount(String.valueOf(dval + penal));
//                        transactionUI.setCallingApplicantName(lblCustNameVal.getText());
//                        if (dval > 0) {
//                            panTransaction.setEnabled(true);
//                        }
//
//                    } else {
//                        txtCharges.setEnabled(false);
//                        txtServiceTax.setEnabled(false);
//                        // if(observable.isCbRefundYes()==true){
//                        String n = "Y";
//                        if (!refund.equals(n)) {
//                            txtRefund.setText(CommonUtil.convertObjToStr(new Double(0.0)));
//                        }
//                        if (refund.equals(n)) {
//                            int p = 0;
//                            for (i = cyear; i <= lyear; i++) {
//                                final String prodID = CommonUtil.convertObjToStr(((ComboBoxModel) (cboProdID.getModel())).getKeyForSelected());
//                                if (cyear >= i) {
//                                    HashMap stMap = new HashMap();
//                                    Date tempDt = (Date) currDt.clone();
//                                    tempDt.setDate(31);
//                                    tempDt.setMonth(11);
//                                    tempDt.setYear(i - 1900);
//                                    Date currDate = (Date) currDt.clone();
//                                    currDate.setDate(currDt.getDate());
//                                    currDate.setMonth(currDt.getMonth());
//                                    currDate.setYear(currDt.getYear());
//                                    stMap.put("PROD_ID", prodID);
//                                    stMap.put("TODAY_DT", currDate);
//                                    stMap.put("EXP_DT", tempDt);
//                                    stMap.put("CHARGE_TYPE", "RENT_CHARGES");
//                                    System.out.println("@#$@#$$#stMap" + stMap);
//                                    List stList = ClientUtil.executeQuery("getServiceTaxAndComission", stMap);
//                                    int diffcurmont = currDt.getMonth() + 1;
//                                    int calmonth = 12 - diffcurmont;
//                                    if (stList != null && stList.size() > 0) {
//                                        stMap = null;
//                                        for (int k = 0; k < stList.size(); k++) {
//                                            stMap = (HashMap) stList.get(k);
//                                            double comm = CommonUtil.convertObjToDouble(stMap.get("COMMISION")).doubleValue();
//                                            double serviceTax = CommonUtil.convertObjToDouble(stMap.get("SERVICE_TAX")).doubleValue();
//                                            String sdate = CommonUtil.convertObjToStr(stMap.get("START_DT"));
//                                            String eedate = CommonUtil.convertObjToStr(stMap.get("END_DT"));
//                                            if (eedate != "") {
//                                                edt = DateUtil.getDateMMDDYYYY(eedate);
//                                                Date comparedt = (Date) currDt.clone();
//                                                comparedt.setDate(edt.getDate());
//                                                comparedt.setMonth(edt.getMonth());
//                                                comparedt.setYear(edt.getYear());
//                                                int eeyear = edt.getYear() + 1900;
//                                                int eemonth = edt.getMonth() + 1;
//                                                int ccalmonth = currDt.getMonth() + 1;
//                                                Date ssdate = DateUtil.getDateMMDDYYYY(sdate);
//                                                int syear = ssdate.getYear() + 1900;
//                                                int smonth = ssdate.getMonth() + 1;
//                                                int diffmonth = (eeyear - syear) + (eemonth - smonth);
//                                                realmont = calmonth - diffmonth + 1;
//                                                if (eeyear == currDt.getYear() + 1900 && DateUtil.dateDiff(currDt, comparedt) >= 0) {
//                                                    if (calmonth != 0) {
//                                                        if (DateUtil.dateDiff(currDt, comparedt) >= 0) //if(calmonth<=eemonth)
//                                                        {
//                                                            if (p == 0) {
//                                                                realmont = eemonth - (currDt.getMonth() + 1);
//                                                            } // else
//                                                            // if( DateUtil.dateDiff(currDt,comparedt)>=0)
//                                                            //  realmont=eemonth-(currDt.getMonth()+1);
//                                                            else {
//                                                                realmont = (eemonth - smonth) + 1;
//                                                            }
//                                                        }
//                                                        //else if(calmonth>eemonth )
//                                                        // realmont=eemonth;
//                                                        double d = comm / 12.0;
//                                                        dd1 = dd1 + d * realmont;
//                                                        dd = (double) getNearest((long) (dd1 * 100), 100) / 100;
//                                                        ser1 = ser1 + (dd / 100) * serviceTax;
//                                                        ser = (double) getNearest((long) (ser1 * 100), 100) / 100;
//                                                        val1 = val1 + dd + ser;
//                                                        val = (double) getNearest((long) (val1 * 100), 100) / 100;
//                                                        calmonth = calmonth - realmont;
//                                                        p++;
//                                                    }
//                                                } else if (eeyear > currDt.getYear() + 1900) {
//                                                    if (calmonth != 0) {
//                                                        realmont = calmonth;
//                                                        double d = comm / 12.0;
//                                                        dd = dd1 + d * realmont;
//                                                        dd = (double) getNearest((long) (dd * 100), 100) / 100;
//                                                        ser = ser1 + (dd / 100) * serviceTax;
//                                                        ser = (double) getNearest((long) (ser * 100), 100) / 100;
//                                                        val = val1 + dd + ser;
//                                                        val = (double) getNearest((long) (val * 100), 100) / 100;
//                                                    }
//                                                }
//                                            } else {
//                                                if (calmonth != 0) {
//                                                    double d = comm / 12.0;
//                                                    dd = dd1 + d * calmonth;
//                                                    dd = (double) getNearest((long) (dd * 100), 100) / 100;
//                                                    ser = ser1 + (dd / 100) * serviceTax;
//                                                    ser = (double) getNearest((long) (ser * 100), 100) / 100;
//                                                    val = val1 + dd + ser;
//                                                    val = (double) getNearest((long) (val * 100), 100) / 100;
//                                                }
//                                            }
//                                        }
//                                    }
//                                } else if (lyear == i) {
//                                    HashMap stMap = new HashMap();
//                                    Date tempDt = (Date) currDt.clone();
//                                    tempDt.setDate(31);
//                                    tempDt.setMonth(11);
//                                    tempDt.setYear(i - 1900);
//                                    Date currDate = (Date) currDt.clone();
//                                    currDate.setDate(1);
//                                    currDate.setMonth(0);
//                                    currDate.setYear(i - 1900);
//                                    stMap.put("PROD_ID", prodID);
//                                    stMap.put("TODAY_DT", currDate);
//                                    stMap.put("EXP_DT", tempDt);
//                                    stMap.put("CHARGE_TYPE", "RENT_CHARGES");
//                                    List stList = ClientUtil.executeQuery("getServiceTaxAndComission", stMap);
//                                    int diffcurmont = edate.getMonth() + 1;
//                                    int calmonth = diffcurmont;
//                                    if (stList != null && stList.size() > 0) {
//                                        stMap = null;
//                                        for (int k = 0; k < stList.size(); k++) {
//                                            stMap = (HashMap) stList.get(k);
//                                            double comm = CommonUtil.convertObjToDouble(stMap.get("COMMISION")).doubleValue();
//                                            double serviceTax = CommonUtil.convertObjToDouble(stMap.get("SERVICE_TAX")).doubleValue();
//                                            String sdate = CommonUtil.convertObjToStr(stMap.get("START_DT"));
//                                            String eedate = CommonUtil.convertObjToStr(stMap.get("END_DT"));
//                                            if (eedate != "") {
//                                                edt = DateUtil.getDateMMDDYYYY(eedate);
//                                                int eeyear = edt.getYear() + 1900;
//                                                int eemonth = edt.getMonth() + 1;
//                                                Date ssdate = DateUtil.getDateMMDDYYYY(sdate);
//                                                int syear = ssdate.getYear() + 1900;
//                                                int smonth = ssdate.getMonth() + 1;
//                                                int diffmonth = (eeyear - syear) + (eemonth - smonth);
//                                                realmont = calmonth - diffmonth + 1;
//                                                if (eeyear == i) {
//                                                    if (calmonth != 0) {
//                                                        if (calmonth <= eemonth) {
//                                                            realmont = calmonth;
//                                                        } else if (calmonth > eemonth) {
//                                                            realmont = eemonth;
//                                                        }
//                                                        double d = comm / 12.0;
//
//                                                        dd2 = dd2 + d * realmont;
//                                                        dd = (double) getNearest((long) (dd2 * 100), 100) / 100;
//                                                        ser2 = ser2 + (dd / 100) * serviceTax;
//                                                        ser = (double) getNearest((long) (ser2 * 100), 100) / 100;
//                                                        val2 = val2 + dd + ser;
//                                                        val = (double) getNearest((long) (val2 * 100), 100) / 100;
//                                                        calmonth = calmonth - realmont;
//                                                    }
//                                                } else {
//                                                    if (eeyear > i) {
//                                                        if (calmonth != 0) {
//                                                            realmont = calmonth;
//                                                            double d = comm / 12.0;
//                                                            dd = dd2 + d * realmont;
//                                                            dd = (double) getNearest((long) (dd * 100), 100) / 100;
//                                                            ser = ser2 + (dd / 100) * serviceTax;
//                                                            ser = (double) getNearest((long) (ser * 100), 100) / 100;
//                                                            val = val2 + dd + ser;
//                                                            val = (double) getNearest((long) (val * 100), 100) / 100;
//                                                            calmonth = calmonth - realmont;
//                                                        }
//                                                    }
//                                                }
//                                            } else {
//                                                if (calmonth != 0) {
//                                                    double d = comm / 12.0;
//                                                    dd = dd2 + d * calmonth;
//                                                    dd = (double) getNearest((long) (dd * 100), 100) / 100;
//                                                    ser = ser2 + (dd / 100) * serviceTax;
//                                                    ser = (double) getNearest((long) (ser * 100), 100) / 100;
//                                                    val = val2 + dd + ser;
//                                                    val = (double) getNearest((long) (val * 100), 100) / 100;
//                                                    calmonth = calmonth - realmont;
//                                                }
//                                            }
//                                        }
//                                        // lyear=lyear+1;
//                                    }  // lmonth=m;
//                                } else {
//                                    HashMap stMap = new HashMap();
//                                    Date tempDt = (Date) currDt.clone();
//                                    tempDt.setDate(31);
//                                    tempDt.setMonth(11);
//                                    tempDt.setYear(i - 1900);
//                                    Date currDate = (Date) currDt.clone();
//                                    currDate.setDate(1);
//                                    currDate.setMonth(0);
//                                    currDate.setYear(i - 1900);
//                                    stMap.put("PROD_ID", prodID);
//                                    stMap.put("TODAY_DT", currDate);
//                                    stMap.put("EXP_DT", tempDt);
//                                    stMap.put("CHARGE_TYPE", "RENT_CHARGES");
//                                    List stList = ClientUtil.executeQuery("getServiceTaxAndComission", stMap);
//                                    int diffcurmont = edate.getMonth() + 1;
//                                    int calmonth = 12;
//                                    if (stList != null && stList.size() > 0) {
//                                        stMap = null;
//                                        for (int k = 0; k < stList.size(); k++) {
//                                            stMap = (HashMap) stList.get(k);
//                                            double comm = CommonUtil.convertObjToDouble(stMap.get("COMMISION")).doubleValue();
//                                            double serviceTax = CommonUtil.convertObjToDouble(stMap.get("SERVICE_TAX")).doubleValue();
//                                            String sdate = CommonUtil.convertObjToStr(stMap.get("START_DT"));
//                                            String eedate = CommonUtil.convertObjToStr(stMap.get("END_DT"));
//                                            if (eedate != "") {
//                                                edt = DateUtil.getDateMMDDYYYY(eedate);
//                                                int eeyear = edt.getYear() + 1900;
//                                                int eemonth = edt.getMonth() + 1;
//                                                Date ssdate = DateUtil.getDateMMDDYYYY(sdate);
//                                                int syear = ssdate.getYear() + 1900;
//                                                int smonth = ssdate.getMonth() + 1;
//                                                int diffmonth = (eeyear - syear) * 12 + (eemonth - smonth);
//                                                realmont = calmonth - (diffmonth + 1);
//                                                if (eeyear == i) {
//                                                    if (calmonth != 0) {
//                                                        realmont = eemonth;
//                                                        double d = comm / 12.0;
//                                                        dd3 = dd3 + d * realmont;
//                                                        dd = (double) getNearest((long) (dd3 * 100), 100) / 100;
//                                                        ser3 = ser3 + (dd / 100) * serviceTax;
//                                                        ser = (double) getNearest((long) (ser3 * 100), 100) / 100;
//                                                        val3 = val3 + dd + ser;
//                                                        val = (double) getNearest((long) (val3 * 100), 100) / 100;
//                                                        calmonth = calmonth - realmont;
//                                                    }
//                                                } else if (eeyear > i) {
//                                                    if (calmonth != 0) {
//                                                        realmont = calmonth;
//                                                        double d = comm / 12.0;
//                                                        dd = dd3 + d * realmont;
//                                                        dd = (double) getNearest((long) (dd * 100), 100) / 100;
//                                                        ser = ser3 + (dd / 100) * serviceTax;
//                                                        ser = (double) getNearest((long) (ser * 100), 100) / 100;
//                                                        val = val3 + dd + ser;
//                                                        val = (double) getNearest((long) (val * 100), 100) / 100;
//                                                        calmonth = calmonth - realmont;
//                                                    }
//                                                }
//                                            } else {
//                                                if (calmonth != 0) {
//                                                    double d = comm / 12.0;
//                                                    dd = dd3 + d * calmonth;
//                                                    dd = (double) getNearest((long) (dd * 100), 100) / 100;
//                                                    ser = ser3 + (dd / 100) * serviceTax;
//                                                    ser = (double) getNearest((long) (ser * 100), 100) / 100;
//                                                    val = val3 + dd + ser;
//                                                    val = (double) getNearest((long) (val * 100), 100) / 100;
//                                                    calmonth = calmonth - realmont;
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                                db = db + ser;
//                                dc = dc + dd;
//                                dval = dval + val;
//                                dd1 = 0;
//                                dd2 = 0;
//                                dd3 = 0;
//                                ser1 = 0;
//                                ser2 = 0;
//                                ser3 = 0;
//                                val1 = 0;
//                                val2 = 0;
//                                val3 = 0;
//                            }
//                            // double dbl=9.0;
//                            final String pid = CommonUtil.convertObjToStr(((ComboBoxModel) (cboProdID.getModel())).getKeyForSelected());
//                            setLockerCha(pid);
//                            /*
//                             * new block
//                             */
//                            double penal = 0.0;
//                            if (txtPenalAmt.getText() != null && !txtPenalAmt.getText().equals("")) {
//                                penal = Double.parseDouble(txtPenalAmt.getText());
//                            }
//                            txtServiceTax.setText(String.valueOf(db));
//                            txtCharges.setText(String.valueOf(dc));
//                            txtRefund.setEnabled(false);
//                            System.out.println("new===x===========" + dval);
//                            lblTotAmtVal.setText(String.valueOf(dval + penal));
//                            System.out.println("lblTotAmtVal value" + lblTotAmtVal.getText());
//                            transactionUI.setCallingAmount(String.valueOf(dval + penal));
//                            transactionUI.setCallingApplicantName(lblCustNameVal.getText());
//                            if (dval > 0) {
//                                panTransaction.setEnabled(true);
//                            }
//                            /*
//                             * new block
//                             */
//                            txtRefund.setText(String.valueOf(dc));
//                            if (dc > 0.0) {
//                                transactionUI.setCallingAmount(String.valueOf(dc));
//                                transactionUI.setCallingApplicantName(lblCustNameVal.getText());
//                                panTransaction.setEnabled(true);
//                            }
//                        }
//                    }
                } /*
                 * rdosurrender ends
                 */ else {
                    if (DateUtil.dateDiff(currDt, expDt) < 0) {
                        displayAlert("This locker already expired...");
                        System.out.println("3333");
                    }
                    final String pid = CommonUtil.convertObjToStr(((ComboBoxModel) (cboProdID.getModel())).getKeyForSelected());
                    if ((observable.isRenew()||observable.isRentCollection()) && pid.length() > 0 && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                        //txtLockerNo.setText("");
                        System.out.println("chargers value..." + txtCharges.getText());
                        setLockerCha(pid);
                    } //}
                    else {
                        txtPenalAmt.setText("0");
                    }
                    if (rdoRenew.isSelected() == true||rdoRentCollection.isSelected()==true) {
                        txtCollectRentyyyyFocusLost(null);
                    }
                }
                observable.populateData(hash);
                //                System.out.println("#@$#$!@!@ after observable.populateData:"+hash);
                if (hash.containsKey("SURRENDER_AUTHORIZATION_STATUS") && hash.get("SURRENDER_AUTHORIZATION_STATUS") != null) {
                    HashMap tempMap = (HashMap) hash.get("SURRENDER_AUTHORIZATION_STATUS");
                    if (tempMap != null && tempMap.size() > 0) {
                        if (tempMap.get("AUTHORIZATION_STATUS") == null) {
                            displayAlert("This locker already surrendered. But NOT AUTHORIZED.");
                            btnCancelActionPerformed(null);
                        }
                    }
                }
               calculateServiceTax();
            }
        }
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE){
	        btnAuthorize.setEnabled(true);
	        btnAuthorize.requestFocusInWindow();
	        btnAuthorize.setFocusable(true);
      }
    }

    private void setLockerCha(String prodID) {
        try {
            System.out.println("meeeeeeeeeeee");
            HashMap stMap = new HashMap();
            stMap.put("PROD_ID", prodID);
            stMap.put("TODAY_DT", currDt);
            stMap.put("CHARGE_TYPE", "RENT_CHARGES");
            List stList = ClientUtil.executeQuery("getSelectPenalty", stMap);
            if (stList != null && stList.size() > 0) {
                stMap = null;
                stMap = (HashMap) stList.get(0);
                int count_commi = Integer.parseInt(stMap.get("COUNT_COMM").toString());
                System.out.println("count_commi=======" + count_commi);
                java.util.Date ldate = currDt;
                if (count_commi == 1) {
                    int lyear = expDt.getYear() + 1900;
                    java.util.Date c = currDt;
                    int cyear = c.getYear() + 1900;
                    int year_diff = cyear - lyear;
                    HashMap stMap1 = new HashMap();
                    stMap1.put("PROD_ID", prodID);
                    stMap1.put("TODAY_DT", currDt);
                    stMap1.put("CHARGE_TYPE", "RENT_CHARGES");
                    List stList1 = ClientUtil.executeQuery("getSelectCommission", stMap1);
                    if (stList1 != null && stList1.size() > 0) {
                        stMap1 = null;
                        stMap1 = (HashMap) stList1.get(0);
                        commision = Integer.parseInt(stMap1.get("COMMISION").toString());
                        System.out.println("commision=====" + commision);
                    }
                    HashMap stMap2 = new HashMap();
                    System.out.println("prodID===" + prodID);
                    stMap2.put("PROD_ID", prodID);
                    List stList2 = ClientUtil.executeQuery("getSelectPenalRate", stMap2);
                    System.out.println("stList2=====" + stList2);
                    if (stList2 != null && stList2.size() > 0) {
                        stMap2 = null;
                        stMap2 = (HashMap) stList2.get(0);
                        Penal_rate = 0;
                        if (!CommonUtil.convertObjToStr(stMap2.get("PENAL_RATE_OF_INTEREST")).equals("") && CommonUtil.convertObjToStr(stMap2.get("PENAL_RATE_OF_INTEREST")).length() > 0) {
                            Penal_rate = Integer.parseInt(stMap2.get("PENAL_RATE_OF_INTEREST").toString());
                        }
                        System.out.println("Penal_rate=====" + Penal_rate);
                    }
                    if (year_diff <= 1) {
                        System.out.println("less than one year : " + ldate);
                        System.out.println("less than one year : " + expDt);
                        //int no_of_days1 = (int) ((ldate.getTime() - expDt.getTime()));
                        no_of_days = (int) ((ldate.getTime() - expDt.getTime()) / (1000 * 60 * 60 * 24));
                        double penal_Amt = (commision * no_of_days * Penal_rate) / 36500.0;
                        penal_Amt = (double) getNearest((long) (penal_Amt * 100), 100) / 100;
                        System.out.println("penal_Amt less than one year : " + penal_Amt);
                        if(penal_Amt>0){
                            txtPenalAmt.setText(CommonUtil.convertObjToStr(new Double(penal_Amt)));
                            double total = CommonUtil.convertObjToDouble(lblTotAmtVal.getText());
                            System.out.println("total amount less than one year : " + total);
                            double newTotal = total + penal_Amt;
                            System.out.println("Final total amount less than one year : " + newTotal);
                            lblTotAmtVal.setText(CommonUtil.convertObjToStr(new Double(newTotal)));                        
                        }else{
                            txtPenalAmt.setText(CommonUtil.convertObjToStr(new Double(0)));
                        }
                    } else if (year_diff > 1) {
                        Date due_date = expDt;
                        Date todayDt = ldate;
                        String dDate = getDateddMMyyyy(due_date);
                        System.out.println("dDate================" + dDate);
                        int no_of_days = 0;
                        double penal_Amt = 0.0;
                        java.util.Date dt1 = null;
                        System.out.println("totIn OOOOOOO=====" + lblTotAmtVal.getText());
                        do {
                            no_of_days = (int) ((todayDt.getTime() - due_date.getTime()) / (1000 * 60 * 60 * 24));
                            penal_Amt = penal_Amt + ((commision * no_of_days * Penal_rate) / 36500.0);
                            String dt = "";  // Start date
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(sdf.parse(dDate));
                            cal.add(Calendar.YEAR, 1);  // number of days to add
                            dDate = sdf.format(cal.getTime());
                            dt1 = sdf.parse(dDate);
                            due_date = dt1;
                        } while (DateUtil.dateDiff(todayDt, dt1) < 0);
                        penal_Amt = (double) getNearest((long) (penal_Amt * 100), 100) / 100;
                        if(penal_Amt>0){
                            txtPenalAmt.setText(CommonUtil.convertObjToStr(new Double(penal_Amt)));
                        }else{
                            txtPenalAmt.setText(CommonUtil.convertObjToStr(new Double(0)));
                        }
                    }
                } else if (count_commi > 1) {
                    String incrdDate = null;
                    Date incrUtildt1 = null;
                    double penal_Amt = 0.0;
                    Date due_date = expDt;
                    Date todayDt = ldate;
                    do {
                        String dDate = getDateddMMyyyy(due_date);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(sdf.parse(dDate));
                        cal.add(Calendar.YEAR, 1);
                        incrdDate = sdf.format(cal.getTime());
                        incrUtildt1 = sdf.parse(incrdDate);
                        HashMap stMapn = new HashMap();
                        stMapn.put("PROD_ID", prodID);
                        System.out.println("due_date===" + due_date);
                        stMapn.put("BEG_DAT", due_date);
                        System.out.println("incrUtildt1===" + incrUtildt1);
                        stMapn.put("LAST_DAT", incrUtildt1);
                        stMapn.put("CHARGE_TYPE", "RENT_CHARGES");
                        List stListn = ClientUtil.executeQuery("getSelectCommissionForIncr", stMapn);
                        //System.out.println("incrUtildt1==="+incrUtildt1);
                        if (stListn != null && stListn.size() > 0) {
                            System.out.println("stListn===" + stListn);
                            stMapn = null;
                            stMapn = (HashMap) stListn.get(0);
                            System.out.println("stListn===" + stListn);
                            commision = Integer.parseInt(stMapn.get("COMMISION").toString());
                            System.out.println("commision=====" + commision);
                        } else {
                            HashMap stMapx = new HashMap();
                            stMapx.put("PROD_ID", prodID);
                            stMapx.put("CHARGE_TYPE", "RENT_CHARGES");
                            List stListx = ClientUtil.executeQuery("getSelectCommForIncr", stMapx);
                            if (stListx != null && stListx.size() > 0) {
                                stMapx = null;
                                stMapx = (HashMap) stListx.get(count_commi - 1);
                                System.out.println("stMapx=====" + stMapx);
                                commision = Integer.parseInt(stMapx.get("COMMISION").toString());
                                System.out.println("commision=====" + commision);
                            }
                        }
                        System.out.println("todaydate====" + todayDt);
                        System.out.println("due_date====" + due_date);
                        no_of_days = (int) ((todayDt.getTime() - due_date.getTime()) / (1000 * 60 * 60 * 24));
                        System.out.println("no_of_days=====" + no_of_days);
                        HashMap stMap2 = new HashMap();
                        System.out.println("prodID===" + prodID);
                        stMap2.put("PROD_ID", prodID);
                        List stList2 = ClientUtil.executeQuery("getSelectPenalRate", stMap2);
                        System.out.println("stList2=====" + stList2);
                        if (stList2 != null && stList2.size() > 0) {
                            stMap2 = null;
                            stMap2 = (HashMap) stList2.get(0);
                            Penal_rate = Integer.parseInt(stMap2.get("PENAL_RATE_OF_INTEREST").toString());
                            System.out.println("Penal_rate=====" + Penal_rate);
                        }
                        System.out.println("penal_-amt" + penal_Amt);
                        penal_Amt = penal_Amt + ((commision * no_of_days * Penal_rate) / 36500.0);
                        System.out.println("penal_Amt=====" + penal_Amt);
                        due_date = incrUtildt1;
                        System.out.println("due_date=====" + due_date);
                    } while (DateUtil.dateDiff(todayDt, incrUtildt1) < 0);
                    penal_Amt = (double) getNearest((long) (penal_Amt * 100), 100) / 100;
                    txtPenalAmt.setText(CommonUtil.convertObjToStr(new Double(penal_Amt)));
                    double total = CommonUtil.convertObjToDouble(lblTotAmtVal.getText());
                    System.out.println("total1111111111111111111======" + total);
                    double newTotal = total + penal_Amt;
                    lblTotAmtVal.setText(CommonUtil.convertObjToStr(new Double(newTotal)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getDateddMMyyyy(java.util.Date strDate1) {
        ////////////////////////////
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        // Get the date today using Calendar object.
        // Date today = Calendar.getInstance().getTime();       
        // Using DateFormat format method we can create a string
        // representation of a date with the defined format.
        String strDate = df.format(strDate1);
        //////////////////////////////
        //String strDate=(String)strDate1;
        SimpleDateFormat dateFormat = null;
        java.util.Date varDate = null;
        try {
            //String strDate="23-Mar-2011";
            dateFormat = new SimpleDateFormat("MM/dd/yyyy");//

            varDate = (java.util.Date) dateFormat.parse(strDate);
            dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            //System.out.println("Date :"+dateFormat.format(varDate));

        } catch (Exception e) {
            e.printStackTrace();;
        }
        return dateFormat.format(varDate);
    }

    /**
     * This will show the alertwindow when the user enters the already existing
     * ShareType *
     */
    private int showAlertWindow(String alertMsg) throws Exception {
        int optionSelected = 1;
        String[] options = {resourceBundle.getString("cDialogOK")};
        optionSelected = COptionPane.showOptionDialog(null, resourceBundle.getString(alertMsg), CommonConstants.INFORMATIONTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        return optionSelected;
    }

    public void authorizeStatus(String authorizeStatus) {
        if (!isFilled) {
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "getSelectLockerAuthorize");
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            whereMap.put(CommonConstants.HIERARCHY_ID, ProxyParameters.HIERARCHY_ID);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnSave.setEnabled(false);
        } else if (isFilled) {
            isFilled = false;
            HashMap singleAuthorizeMap = new HashMap();
            ArrayList arrList = new ArrayList();
            HashMap authDataMap = new HashMap();
            authDataMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            authDataMap.put("LOCKER_NUM", txtLockerNo.getText());
            authDataMap.put("CUST_ID", lblCustomerIdVal.getText());
            authDataMap.put("PROD_ID", CommonUtil.convertObjToStr((((ComboBoxModel) (cboProdID).getModel())).getKeyForSelected()));
            authDataMap.put("ISSUE_ID", getSurrenderID());
            authDataMap.put("SUR_OR_RENEW", getSurOrRenew());
            authDataMap.put("DEFAULTER",observable.getChkDefaulter());
            String date = lblExpiryDateVal.getText();
            Date ldate = DateUtil.getDateMMDDYYYY(tdtNewExpDt.getDateValue());
            int mm = CommonUtil.convertObjToInt(txtCollectRentMM.getText());
            int yy = CommonUtil.convertObjToInt(txtCollectRentyyyy.getText());
            Date expyear = currDt;
            if (null != date && !date.equals("") && date.length() > 0) {
                Date dat = DateUtil.getDateMMDDYYYY(date);
                int day = dat.getDate();
                int month = dat.getMonth() + 1;
                int year = dat.getYear() + 1900;
                int eyear = dat.getYear() + 1900;
                Date bdate = (Date) currDt.clone();
                bdate.setDate(dat.getDate());
                bdate.setMonth(dat.getMonth());
                bdate.setYear(dat.getYear());
                expyear = bdate;
                int cyear = currDt.getYear() + 1900;                
                Date ldat = DateUtil.getDate(day, mm, yy);
                int lyear = ldat.getYear();
                int lmon = ldat.getMonth();
                int lday = ldat.getDate();
                Date tempdate = (Date) currDt.clone();
                tempdate.setDate(ldat.getDate());
                tempdate.setMonth(ldat.getMonth());
                tempdate.setYear(ldat.getYear());
                if (rdoSurrender.isSelected() == true) {
                    if (eyear < cyear) {
                        expyear = currDt;
                    } else {
                        expyear = dat;
                        System.out.println("the year is" + expyear);
                    }
                }
                if (rdoRenew.isSelected() == true) {
                    expyear = tempdate;
                }
                if (rdoBreakOpen.isSelected() == true) {
                    expyear = bdate;
                }
            }
            arrList.add(authDataMap);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            singleAuthorizeMap.put("year", ldate);
            singleAuthorizeMap.put("COLLECT_RENT_MONTH",mm);
            singleAuthorizeMap.put("COLLECT_RENT_YEAR",yy);
            if(TrueTransactMain.SERVICE_TAX_REQ.equals("Y")){
            	HashMap serauthMap = new HashMap();
            	serauthMap.put("ACCT_NUM",observable.getTxtLockerNo());
            	serauthMap.put("STATUS", authorizeStatus);
            	serauthMap.put("USER_ID", TrueTransactMain.USER_ID);
            	serauthMap.put("AUTHORIZEDT", currDt);
            	singleAuthorizeMap.put("SER_TAX_AUTH",serauthMap);
	    }
            authorize(singleAuthorizeMap);
            observable.setResultStatus();
        }
    }

    public void authorize(HashMap map) {
        map.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
        //        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        observable.setAuthorizeMap(map);
        observable.doAction("Authorize");
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            observable.resetForm();
             if(fromNewAuthorizeUI){
                this.dispose();
                newauthorizeListUI.removeSelectedRow();
                newauthorizeListUI.setFocusToTable();
                fromNewAuthorizeUI = false;
                newauthorizeListUI.displayDetails("Locker");
            }
            if(fromAuthorizeUI){
                this.dispose();
                authorizeListUI.removeSelectedRow();
                authorizeListUI.setFocusToTable();
                fromAuthorizeUI = false;
                authorizeListUI.displayDetails("Locker");
            }
             btnCancelActionPerformed(null);
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
        }

    }

    /**
     * Method used to do Required operation when user clicks
     * btnAuthorize,btnReject or btnReject *
     */
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdgSurOrRenew = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrTokenConfig = new com.see.truetransact.uicomponent.CToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lbSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace34 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace35 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lbSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace36 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace37 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace38 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace40 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        lblSpace39 = new com.see.truetransact.uicomponent.CLabel();
        btnHistory = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panAccountInfo = new com.see.truetransact.uicomponent.CPanel();
        panStdInstructions1 = new com.see.truetransact.uicomponent.CPanel();
        panChargesServiceTax = new com.see.truetransact.uicomponent.CPanel();
        lblCharges = new com.see.truetransact.uicomponent.CLabel();
        txtCharges = new com.see.truetransact.uicomponent.CTextField();
        lblServiceTax = new com.see.truetransact.uicomponent.CLabel();
        txtServiceTax = new com.see.truetransact.uicomponent.CTextField();
        lblTotalAmt = new com.see.truetransact.uicomponent.CLabel();
        lblPenalAmt = new javax.swing.JLabel();
        txtPenalAmt = new javax.swing.JTextField();
        lblTotAmtVal = new javax.swing.JTextField();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        lblServiceTaxval = new com.see.truetransact.uicomponent.CLabel();
        panDate = new com.see.truetransact.uicomponent.CPanel();
        lblSurdate = new com.see.truetransact.uicomponent.CLabel();
        lblSurDate = new com.see.truetransact.uicomponent.CLabel();
        lblExpiryDate = new com.see.truetransact.uicomponent.CLabel();
        lblExpiryDateVal = new com.see.truetransact.uicomponent.CLabel();
        panParameters = new com.see.truetransact.uicomponent.CPanel();
        rdoRenew = new com.see.truetransact.uicomponent.CRadioButton();
        rdoSurrender = new com.see.truetransact.uicomponent.CRadioButton();
        rdoBreakOpen = new com.see.truetransact.uicomponent.CRadioButton();
        cbCustomer = new com.see.truetransact.uicomponent.CCheckBox();
        rdoRentCollection = new com.see.truetransact.uicomponent.CRadioButton();
        panCurrAndNewDate = new com.see.truetransact.uicomponent.CPanel();
        lblCurrentDate = new com.see.truetransact.uicomponent.CLabel();
        lblCurrentDateVal = new com.see.truetransact.uicomponent.CLabel();
        lblNewExpDate = new com.see.truetransact.uicomponent.CLabel();
        tdtNewExpDt = new com.see.truetransact.uicomponent.CDateField();
        panCurrAndNewDate1 = new com.see.truetransact.uicomponent.CPanel();
        txtRefund = new com.see.truetransact.uicomponent.CTextField();
        lblRefund = new com.see.truetransact.uicomponent.CLabel();
        lblCollectRent = new com.see.truetransact.uicomponent.CLabel();
        panOperations1 = new com.see.truetransact.uicomponent.CPanel();
        txtCollectRentMM = new com.see.truetransact.uicomponent.CTextField();
        txtCollectRentyyyy = new com.see.truetransact.uicomponent.CTextField();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtBreakOpenRemarks = new com.see.truetransact.uicomponent.CTextField();
        chkDefaulter = new com.see.truetransact.uicomponent.CCheckBox();
        lblActNum = new com.see.truetransact.uicomponent.CLabel();
        txtActNum = new com.see.truetransact.uicomponent.CTextField();
        chkNoTrans = new com.see.truetransact.uicomponent.CCheckBox();
        chkRenewBfrExp = new com.see.truetransact.uicomponent.CCheckBox();
        panDate1 = new com.see.truetransact.uicomponent.CPanel();
        lblCustName = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerId = new com.see.truetransact.uicomponent.CLabel();
        lblDate = new com.see.truetransact.uicomponent.CLabel();
        lblModeOfOp = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerIdVal = new com.see.truetransact.uicomponent.CLabel();
        lblCustNameVal = new com.see.truetransact.uicomponent.CLabel();
        lblDateVal = new com.see.truetransact.uicomponent.CLabel();
        lblModeOfOpVal = new com.see.truetransact.uicomponent.CLabel();
        lblLockerNo = new com.see.truetransact.uicomponent.CLabel();
        txtLockerNo = new com.see.truetransact.uicomponent.CTextField();
        btnLockerNo = new com.see.truetransact.uicomponent.CButton();
        lblProdID = new com.see.truetransact.uicomponent.CLabel();
        cboProdID = new com.see.truetransact.uicomponent.CComboBox();
        cButton1 = new com.see.truetransact.uicomponent.CButton();
        panTransaction = new com.see.truetransact.uicomponent.CPanel();
        mbrTokenConfig = new com.see.truetransact.uicomponent.CMenuBar();
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
        setMaximumSize(new java.awt.Dimension(900, 600));
        setMinimumSize(new java.awt.Dimension(900, 600));
        setPreferredSize(new java.awt.Dimension(900, 600));

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
        tbrTokenConfig.add(btnView);

        lbSpace3.setText("     ");
        tbrTokenConfig.add(lbSpace3);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.setFocusable(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnNew);

        lblSpace34.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace34.setText("     ");
        lblSpace34.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace34);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.setFocusable(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnEdit);

        lblSpace35.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace35.setText("     ");
        lblSpace35.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace35.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace35.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace35);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.setFocusable(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnDelete);

        lbSpace2.setText("     ");
        tbrTokenConfig.add(lbSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.setFocusable(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnSave);

        lblSpace36.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace36.setText("     ");
        lblSpace36.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace36.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace36.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace36);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.setEnabled(false);
        btnCancel.setFocusable(false);
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnCancel);

        lblSpace3.setText("     ");
        tbrTokenConfig.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnAuthorize);

        lblSpace37.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace37.setText("     ");
        lblSpace37.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace37.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace37.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace37);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.setFocusable(false);
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnException);

        lblSpace38.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace38.setText("     ");
        lblSpace38.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace38.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace38.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace38);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnReject);

        lblSpace5.setText("     ");
        tbrTokenConfig.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.setFocusable(false);
        tbrTokenConfig.add(btnPrint);

        lblSpace40.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace40.setText("     ");
        lblSpace40.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace40.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace40.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace40);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.setFocusable(false);
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnClose);

        lblSpace39.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace39.setText("     ");
        lblSpace39.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace39.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace39.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace39);

        btnHistory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnHistory.setToolTipText("Closed Deposits");
        btnHistory.setFocusable(false);
        btnHistory.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnHistory.setMinimumSize(new java.awt.Dimension(29, 27));
        btnHistory.setPreferredSize(new java.awt.Dimension(29, 27));
        btnHistory.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHistoryActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnHistory);

        getContentPane().add(tbrTokenConfig, java.awt.BorderLayout.NORTH);

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

        panAccountInfo.setBorder(javax.swing.BorderFactory.createTitledBorder("Account Info."));
        panAccountInfo.setMinimumSize(new java.awt.Dimension(250, 100));
        panAccountInfo.setName("panAccountInfo"); // NOI18N
        panAccountInfo.setPreferredSize(new java.awt.Dimension(250, 100));
        panAccountInfo.setLayout(new java.awt.GridBagLayout());

        panStdInstructions1.setMinimumSize(new java.awt.Dimension(900, 220));
        panStdInstructions1.setPreferredSize(new java.awt.Dimension(900, 220));
        panStdInstructions1.setLayout(new java.awt.GridBagLayout());

        panChargesServiceTax.setBackground(new java.awt.Color(204, 204, 204));
        panChargesServiceTax.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panChargesServiceTax.setMaximumSize(new java.awt.Dimension(775, 30));
        panChargesServiceTax.setMinimumSize(new java.awt.Dimension(775, 30));
        panChargesServiceTax.setPreferredSize(new java.awt.Dimension(775, 30));
        panChargesServiceTax.setLayout(new java.awt.GridBagLayout());

        lblCharges.setText("Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 6, 0, 0);
        panChargesServiceTax.add(lblCharges, gridBagConstraints);

        txtCharges.setAllowNumber(true);
        txtCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCharges.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtChargesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 0, 0);
        panChargesServiceTax.add(txtCharges, gridBagConstraints);

        lblServiceTax.setText("Service Charge");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 4, 0, 0);
        panChargesServiceTax.add(lblServiceTax, gridBagConstraints);

        txtServiceTax.setAllowNumber(true);
        txtServiceTax.setMinimumSize(new java.awt.Dimension(100, 21));
        txtServiceTax.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtServiceTaxFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 4, 0, 0);
        panChargesServiceTax.add(txtServiceTax, gridBagConstraints);

        lblTotalAmt.setText("Total Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 18, 0, 0);
        panChargesServiceTax.add(lblTotalAmt, gridBagConstraints);

        lblPenalAmt.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lblPenalAmt.setText("Penal Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 4, 0, 0);
        panChargesServiceTax.add(lblPenalAmt, gridBagConstraints);

        txtPenalAmt.setEditable(false);
        txtPenalAmt.setEnabled(false);
        txtPenalAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPenalAmt.setPreferredSize(new java.awt.Dimension(100, 21));
        txtPenalAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPenalAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 0, 0);
        panChargesServiceTax.add(txtPenalAmt, gridBagConstraints);

        lblTotAmtVal.setEditable(false);
        lblTotAmtVal.setMinimumSize(new java.awt.Dimension(100, 21));
        lblTotAmtVal.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 0, 10);
        panChargesServiceTax.add(lblTotAmtVal, gridBagConstraints);

        cLabel1.setText("Service Tax");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 0, 0);
        panChargesServiceTax.add(cLabel1, gridBagConstraints);

        lblServiceTaxval.setText("cLabel2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 6, 0, 0);
        panChargesServiceTax.add(lblServiceTaxval, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.ipadx = 105;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 10, 0);
        panStdInstructions1.add(panChargesServiceTax, gridBagConstraints);

        panDate.setMaximumSize(new java.awt.Dimension(400, 22));
        panDate.setMinimumSize(new java.awt.Dimension(400, 22));
        panDate.setPreferredSize(new java.awt.Dimension(400, 22));
        panDate.setLayout(new java.awt.GridBagLayout());

        lblSurdate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSurdate.setText("Surrender Date ");
        lblSurdate.setMaximumSize(new java.awt.Dimension(100, 18));
        lblSurdate.setMinimumSize(new java.awt.Dimension(100, 18));
        lblSurdate.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 10, 1, 0);
        panDate.add(lblSurdate, gridBagConstraints);

        lblSurDate.setMaximumSize(new java.awt.Dimension(80, 18));
        lblSurDate.setMinimumSize(new java.awt.Dimension(80, 18));
        lblSurDate.setPreferredSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panDate.add(lblSurDate, gridBagConstraints);

        lblExpiryDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblExpiryDate.setText("Old Expiry Date ");
        lblExpiryDate.setMaximumSize(new java.awt.Dimension(100, 18));
        lblExpiryDate.setMinimumSize(new java.awt.Dimension(100, 18));
        lblExpiryDate.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panDate.add(lblExpiryDate, gridBagConstraints);

        lblExpiryDateVal.setMaximumSize(new java.awt.Dimension(100, 18));
        lblExpiryDateVal.setMinimumSize(new java.awt.Dimension(100, 18));
        lblExpiryDateVal.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 18, 1, 0);
        panDate.add(lblExpiryDateVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.ipadx = 90;
        gridBagConstraints.ipady = -2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 9, 0, 0);
        panStdInstructions1.add(panDate, gridBagConstraints);

        panParameters.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panParameters.setMaximumSize(new java.awt.Dimension(550, 25));
        panParameters.setMinimumSize(new java.awt.Dimension(550, 25));
        panParameters.setPreferredSize(new java.awt.Dimension(550, 25));
        panParameters.setLayout(new java.awt.GridBagLayout());

        rdgSurOrRenew.add(rdoRenew);
        rdoRenew.setText("Renew");
        rdoRenew.setMaximumSize(new java.awt.Dimension(68, 18));
        rdoRenew.setMinimumSize(new java.awt.Dimension(68, 18));
        rdoRenew.setPreferredSize(new java.awt.Dimension(77, 18));
        rdoRenew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoRenewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panParameters.add(rdoRenew, gridBagConstraints);

        rdgSurOrRenew.add(rdoSurrender);
        rdoSurrender.setText("Surrender");
        rdoSurrender.setMaximumSize(new java.awt.Dimension(90, 18));
        rdoSurrender.setMinimumSize(new java.awt.Dimension(90, 18));
        rdoSurrender.setPreferredSize(new java.awt.Dimension(90, 18));
        rdoSurrender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSurrenderActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panParameters.add(rdoSurrender, gridBagConstraints);

        rdgSurOrRenew.add(rdoBreakOpen);
        rdoBreakOpen.setText("BreakOpen");
        rdoBreakOpen.setMaximumSize(new java.awt.Dimension(91, 21));
        rdoBreakOpen.setMinimumSize(new java.awt.Dimension(91, 21));
        rdoBreakOpen.setPreferredSize(new java.awt.Dimension(91, 21));
        rdoBreakOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoBreakOpenActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panParameters.add(rdoBreakOpen, gridBagConstraints);

        cbCustomer.setText("CustomerRequest");
        cbCustomer.setMaximumSize(new java.awt.Dimension(160, 18));
        cbCustomer.setMinimumSize(new java.awt.Dimension(160, 18));
        cbCustomer.setPreferredSize(new java.awt.Dimension(160, 18));
        cbCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbCustomerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panParameters.add(cbCustomer, gridBagConstraints);

        rdgSurOrRenew.add(rdoRentCollection);
        rdoRentCollection.setText("Rent Collection");
        rdoRentCollection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoRentCollectionActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panParameters.add(rdoRentCollection, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.ipady = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 60, 0, 0);
        panStdInstructions1.add(panParameters, gridBagConstraints);

        panCurrAndNewDate.setMaximumSize(new java.awt.Dimension(400, 25));
        panCurrAndNewDate.setMinimumSize(new java.awt.Dimension(400, 25));
        panCurrAndNewDate.setPreferredSize(new java.awt.Dimension(400, 25));
        panCurrAndNewDate.setLayout(new java.awt.GridBagLayout());

        lblCurrentDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCurrentDate.setText("Current Date ");
        lblCurrentDate.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblCurrentDate.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lblCurrentDate.setMinimumSize(new java.awt.Dimension(100, 18));
        lblCurrentDate.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -23;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panCurrAndNewDate.add(lblCurrentDate, gridBagConstraints);

        lblCurrentDateVal.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lblCurrentDateVal.setMinimumSize(new java.awt.Dimension(100, 18));
        lblCurrentDateVal.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 1, 0);
        panCurrAndNewDate.add(lblCurrentDateVal, gridBagConstraints);

        lblNewExpDate.setText("New Expiry Date");
        lblNewExpDate.setMaximumSize(new java.awt.Dimension(100, 18));
        lblNewExpDate.setMinimumSize(new java.awt.Dimension(100, 18));
        lblNewExpDate.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 10, 1, 0);
        panCurrAndNewDate.add(lblNewExpDate, gridBagConstraints);

        tdtNewExpDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtNewExpDtFocusLost(evt);
            }
        });
        panCurrAndNewDate.add(tdtNewExpDt, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.ipady = -5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 7, 0, 11);
        panStdInstructions1.add(panCurrAndNewDate, gridBagConstraints);

        panCurrAndNewDate1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panCurrAndNewDate1.setMaximumSize(new java.awt.Dimension(450, 25));
        panCurrAndNewDate1.setMinimumSize(new java.awt.Dimension(450, 25));
        panCurrAndNewDate1.setPreferredSize(new java.awt.Dimension(450, 500));
        panCurrAndNewDate1.setLayout(new java.awt.GridBagLayout());

        txtRefund.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 17, 0, 0);
        panCurrAndNewDate1.add(txtRefund, gridBagConstraints);

        lblRefund.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRefund.setText("Refund Amount ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 0, 0);
        panCurrAndNewDate1.add(lblRefund, gridBagConstraints);

        lblCollectRent.setText("Collect Rent Upto");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.ipady = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        panCurrAndNewDate1.add(lblCollectRent, gridBagConstraints);

        panOperations1.setMinimumSize(new java.awt.Dimension(90, 21));
        panOperations1.setPreferredSize(new java.awt.Dimension(80, 21));
        panOperations1.setLayout(new java.awt.GridBagLayout());

        txtCollectRentMM.setMaximumSize(new java.awt.Dimension(35, 21));
        txtCollectRentMM.setMinimumSize(new java.awt.Dimension(35, 21));
        txtCollectRentMM.setPreferredSize(new java.awt.Dimension(35, 21));
        txtCollectRentMM.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCollectRentMMFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 16, 0, 0);
        panOperations1.add(txtCollectRentMM, gridBagConstraints);

        txtCollectRentyyyy.setMaximumSize(new java.awt.Dimension(70, 21));
        txtCollectRentyyyy.setMinimumSize(new java.awt.Dimension(70, 21));
        txtCollectRentyyyy.setPreferredSize(new java.awt.Dimension(70, 21));
        txtCollectRentyyyy.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCollectRentyyyyFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panOperations1.add(txtCollectRentyyyy, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 65;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 4, 0);
        panCurrAndNewDate1.add(panOperations1, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 0);
        panCurrAndNewDate1.add(lblRemarks, gridBagConstraints);

        txtBreakOpenRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 110;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 17, 0, 0);
        panCurrAndNewDate1.add(txtBreakOpenRemarks, gridBagConstraints);

        chkGroup.add(chkDefaulter);
        chkDefaulter.setText("Defaulter");
        chkDefaulter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkDefaulterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 2, 0);
        panCurrAndNewDate1.add(chkDefaulter, gridBagConstraints);

        lblActNum.setText("Account Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        panCurrAndNewDate1.add(lblActNum, gridBagConstraints);

        txtActNum.setMaximumSize(new java.awt.Dimension(100, 21));
        txtActNum.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 24;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 17, 0, 0);
        panCurrAndNewDate1.add(txtActNum, gridBagConstraints);

        chkNoTrans.setText("No Transaction");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 0, 5);
        panCurrAndNewDate1.add(chkNoTrans, gridBagConstraints);

        chkRenewBfrExp.setText("Renew Before Exp. Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        panCurrAndNewDate1.add(chkRenewBfrExp, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 72;
        gridBagConstraints.ipady = 98;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 0);
        panStdInstructions1.add(panCurrAndNewDate1, gridBagConstraints);

        panDate1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panDate1.setMaximumSize(new java.awt.Dimension(400, 22));
        panDate1.setMinimumSize(new java.awt.Dimension(400, 22));
        panDate1.setPreferredSize(new java.awt.Dimension(400, 22));
        panDate1.setLayout(new java.awt.GridBagLayout());

        lblCustName.setText("Customer Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 51, 0, 0);
        panDate1.add(lblCustName, gridBagConstraints);

        lblCustomerId.setText("Customer ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 73, 0, 0);
        panDate1.add(lblCustomerId, gridBagConstraints);

        lblDate.setText("Issue Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 83, 0, 0);
        panDate1.add(lblDate, gridBagConstraints);

        lblModeOfOp.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblModeOfOp.setText("Mode of Operation");
        lblModeOfOp.setMaximumSize(new java.awt.Dimension(120, 18));
        lblModeOfOp.setMinimumSize(new java.awt.Dimension(110, 18));
        lblModeOfOp.setPreferredSize(new java.awt.Dimension(110, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 35, 2, 0);
        panDate1.add(lblModeOfOp, gridBagConstraints);

        lblCustomerIdVal.setMaximumSize(new java.awt.Dimension(100, 21));
        lblCustomerIdVal.setMinimumSize(new java.awt.Dimension(100, 21));
        lblCustomerIdVal.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = -3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 27, 0, 0);
        panDate1.add(lblCustomerIdVal, gridBagConstraints);

        lblCustNameVal.setMaximumSize(new java.awt.Dimension(350, 18));
        lblCustNameVal.setMinimumSize(new java.awt.Dimension(350, 18));
        lblCustNameVal.setPreferredSize(new java.awt.Dimension(350, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.ipadx = -195;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 27, 0, 36);
        panDate1.add(lblCustNameVal, gridBagConstraints);

        lblDateVal.setMaximumSize(new java.awt.Dimension(100, 21));
        lblDateVal.setMinimumSize(new java.awt.Dimension(100, 21));
        lblDateVal.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.ipadx = 11;
        gridBagConstraints.ipady = -4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 29, 0, 0);
        panDate1.add(lblDateVal, gridBagConstraints);

        lblModeOfOpVal.setMaximumSize(new java.awt.Dimension(100, 21));
        lblModeOfOpVal.setMinimumSize(new java.awt.Dimension(100, 21));
        lblModeOfOpVal.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.ipady = -6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 29, 5, 0);
        panDate1.add(lblModeOfOpVal, gridBagConstraints);

        lblLockerNo.setText("Locker No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 86, 0, 0);
        panDate1.add(lblLockerNo, gridBagConstraints);

        txtLockerNo.setEditable(false);
        txtLockerNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtLockerNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLockerNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 27, 0, 0);
        panDate1.add(txtLockerNo, gridBagConstraints);

        btnLockerNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnLockerNo.setToolTipText("Customer Data");
        btnLockerNo.setMinimumSize(new java.awt.Dimension(25, 25));
        btnLockerNo.setPreferredSize(new java.awt.Dimension(25, 25));
        btnLockerNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLockerNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 0);
        panDate1.add(btnLockerNo, gridBagConstraints);

        lblProdID.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblProdID.setText("Product ID");
        lblProdID.setMaximumSize(new java.awt.Dimension(100, 18));
        lblProdID.setMinimumSize(new java.awt.Dimension(100, 18));
        lblProdID.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(16, 45, 0, 0);
        panDate1.add(lblProdID, gridBagConstraints);

        cboProdID.setMaximumSize(new java.awt.Dimension(100, 21));
        cboProdID.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdID.setPopupWidth(225);
        cboProdID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 27, 0, 0);
        panDate1.add(cboProdID, gridBagConstraints);

        cButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PHOTO_SIGN.gif"))); // NOI18N
        cButton1.setToolTipText("Photo & Sign");
        cButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = -24;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 1, 0, 16);
        panDate1.add(cButton1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = -37;
        gridBagConstraints.ipady = 153;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 19, 0, 0);
        panStdInstructions1.add(panDate1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipady = 46;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(34, 16, 0, 6);
        panAccountInfo.add(panStdInstructions1, gridBagConstraints);

        panTransaction.setMinimumSize(new java.awt.Dimension(700, 400));
        panTransaction.setPreferredSize(new java.awt.Dimension(700, 400));
        panTransaction.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.ipady = -180;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(295, 12, 3, 0);
        panAccountInfo.add(panTransaction, gridBagConstraints);

        getContentPane().add(panAccountInfo, java.awt.BorderLayout.CENTER);

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

        mbrTokenConfig.add(mnuProcess);

        setJMenuBar(mbrTokenConfig);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rdoBreakOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoBreakOpenActionPerformed
        // TODO add your handling code here:
        performRdoAction();
    }//GEN-LAST:event_rdoBreakOpenActionPerformed

    private void txtCollectRentyyyyFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCollectRentyyyyFocusLost
        // TODO add your handling code here:
        // transactionUI.setCallingAccNo(lblCustomerIdVal.getText());
        final String prodID = CommonUtil.convertObjToStr(((ComboBoxModel) (cboProdID.getModel())).getKeyForSelected());
        String date = lblDateVal.getText();
        java.util.Date odate = DateUtil.getDateMMDDYYYY(date);
        int day = odate.getDate();
        int month = odate.getMonth() + 1;
        int year = odate.getYear() + 1900;
        System.out.println("odate here "+odate);
        String ldate = lblExpiryDateVal.getText();
        java.util.Date edate = DateUtil.getDateMMDDYYYY(ldate);  
        System.out.println("date from here"+edate);
        boolean rentPaidPartially = false;
        java.util.Date rentPaidDate = null;
        //this block is added to get already paid rent details of this locker by shihad on 28/05/2014
        HashMap newMap = new HashMap();
        List RentPaidList = new ArrayList();
        newMap.put("PROD_ID",prodid);
        newMap.put("LOCKER_NUM",txtLockerNo.getText());
        newMap.put("BRANCH_CODE",ProxyParameters.BRANCH_ID);
        newMap.put("CUST_ID",lblCustomerIdVal.getText());
        newMap.put("ACT_NUM",txtActNum.getText());        
        RentPaidList = ClientUtil.executeQuery("getLockerRentCollectionDetails", newMap);
        System.out.println("new month here"+RentPaidList);
        if (RentPaidList != null && RentPaidList.size() > 0) {
            HashMap monthMap = new HashMap();
            monthMap = (HashMap) RentPaidList.get(0);
            String paidMonth = CommonUtil.convertObjToStr(monthMap.get("COLLECT_RENT_MM"));
            String paidYear = CommonUtil.convertObjToStr(monthMap.get("COLLECT_RENT_YYYY"));
            String paidDay = Integer.toString(edate.getDate());
            String newExpDateRent = paidDay + "/" + paidMonth + "/" + paidYear;
            System.out.println("rentpaidDate here" + newExpDateRent);
            rentPaidDate = DateUtil.getDateMMDDYYYY(newExpDateRent);
             //added by Rishad 21/apr/2020
        if (rdoRentCollection.isSelected() == true) {
            if (rentPaidDate.after(currDt)) {
                            int yesNo = 0;
            String[] options = {"Yes", "No"};
            yesNo = COptionPane.showOptionDialog(null,"Rent Collected Upto Date :" + rentPaidDate + ", Do you want to continue?", CommonConstants.WARNINGTITLE,
                    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
            if (yesNo != 0) {
                btnCancelActionPerformed(null);
                return;
            }
            }
        }
        //end Rishad
            
         if (rentPaidDate.compareTo(edate) > 0) {
                rentPaidPartially = true;
                System.out.println("success here");
            }
        }
        //by shihad on 28/05/2014
        System.out.println("from date here"+edate);        
        int lday = edate.getDate();
        int lmonth = edate.getMonth() + 1;;
        int lyear = edate.getYear() + 1900;
        int yy = currDt.getYear() + 1900;
        int mon = currDt.getMonth() + 1;
        int diffyear = 0;
        int expyear = edate.getYear() + 1900;
        System.out.println("expiry date here before"+expyear);
        /*this if condition added by shihad for checking whether the given month and year is greater than new expiry date, works only for rent collection*/ 
        if (evt != null && (rdoRentCollection.isSelected() == true)) {
            String rentMonth = txtCollectRentMM.getText();
            String rentYear = txtCollectRentyyyy.getText();
            Date curRentMonthYear = DateUtil.getDateMMDDYYYY(lday + "/" + rentMonth + "/" + rentYear);
            System.out.println("hi curmonth year here" + curRentMonthYear);
            Date dateNewExp = DateUtil.getDateMMDDYYYY(tdtNewExpDt.getDateValue());
            if (curRentMonthYear.compareTo(dateNewExp) > 0) {
                ClientUtil.displayAlert("Rent cannot be collected upto the given month");
                return;
            } else {

                expyear = Integer.parseInt(txtCollectRentyyyy.getText());
            }
        }
        /*on 27/05/2014*/ 
        if (lmonth <= mon && lyear <= yy) {
            diffyear = (yy - lyear) + 1;
            expyear = expyear + diffyear;
        } else if (lmonth > mon && lyear <= yy) {
            diffyear = yy - lyear;
            expyear = expyear + diffyear;
        } else if (lyear > yy) {
            diffyear = lyear - yy;
            expyear = expyear + diffyear;
        }
        //  java.util.Date expdate=DateUtil.getDate(day,mm,yyyy);
        //changing the expiry date for locker surrendering
        if (rdoSurrender.isSelected()) {
            String surDate = lblSurDate.getText();
            java.util.Date surrenderDate = DateUtil.getDateMMDDYYYY(surDate);
            System.out.println("surrender date here"+surrenderDate);
            expyear = surrenderDate.getYear() + 1900;
            lmonth = surrenderDate.getMonth() + 1;
            System.out.println("surrender exp date"+expyear);
        }
        if (diffyear > 0) {
//            for(int i=0;i<diffyear;i++){
            System.out.println("inside diff year");
            int newexp = lyear + 1;
            Date newdate = (Date) currDt.clone();
            newdate.setDate(edate.getDate());
            newdate.setMonth(edate.getMonth());
            newdate.setYear(edate.getYear() + diffyear);
            System.out.println("new date" + newdate);
            if (!(DateUtil.dateDiff(currDt, newdate) <= 0)) {
                System.out.println("@@@@@@@date" + DateUtil.dateDiff(currDt, newdate));
                System.out.println("inside datedifffference");
                if (evt == null) {
                    txtCollectRentMM.setText(CommonUtil.convertObjToStr(new Integer(lmonth)));
                    txtCollectRentyyyy.setText(CommonUtil.convertObjToStr(new Integer(expyear)));
                    txtCollectRentMM.setEnabled(false);
                    txtCollectRentyyyy.setEnabled(false);
                }
                if (rdoRentCollection.isSelected() == true) {
                    txtCollectRentMM.setEnabled(true);
                    txtCollectRentyyyy.setEnabled(true);
                }
            }
        }else{
            txtCollectRentMM.setText(CommonUtil.convertObjToStr(new Integer(lmonth)));
            txtCollectRentyyyy.setText(CommonUtil.convertObjToStr(new Integer(lyear)));
        }
        if (rentPaidPartially)//this condition works only if rent is already collected partially, added by shihad on 28/05/2014
        {
            edate = rentPaidDate;  // From date for rent calculation is changed
            lyear = edate.getYear() + 1900;
            System.out.println("lyear here" + lyear);
            ClientUtil.showMessageWindow("Rent collected upto " + (edate.getMonth() + 1) + "/" + lyear);
            System.out.println("new from date for calculation" + edate);
        }
        //by shihad on 28/05/2014
        // HashMap h=new HashMap();
        double db = 0;
        double dc = 0;
        double dval = 0;
        double ser = 0;
        double val = 0;
        double dd = 0;
        Date edt = null;
        double ser1 = 0;
        double val1 = 0;
        double dd1 = 0;
        double ser2 = 0;
        double val2 = 0;
        double dd2 = 0;
        double ser3 = 0;
        double val3 = 0;
        double dd3 = 0;
        int mm = CommonUtil.convertObjToInt(txtCollectRentMM.getText());
        int yyyy = CommonUtil.convertObjToInt(txtCollectRentyyyy.getText());
        java.util.Date exdate = DateUtil.getDate(lday, mm, yyyy);
        if(evt==null){
        	tdtNewExpDt.setDateValue(DateUtil.getStringDate(exdate));
        }
        String lnew = tdtNewExpDt.getDateValue();
        //java.util.Date dt = DateUtil.getDateMMDDYYYY(lnew);
        java.util.Date dt = exdate;
        int ld = dt.getDate();
        int lm = dt.getMonth() + 1;
        int ly = dt.getYear() + 1900;
        int i;
        int realmont = 0;
        int q = 0;
        for (i = lyear; i <= ly; i++) {
            if (lyear >= i) {
                HashMap stMap = new HashMap();
                Date tempDt = (Date) currDt.clone();
                tempDt.setDate(edate.getDate());
                tempDt.setMonth(edate.getMonth());
                tempDt.setYear(i - 1900);
                Date currDate = (Date) currDt.clone();
                currDate.setDate(31);
                currDate.setMonth(11);
                currDate.setYear(i - 1900);
                stMap.put("PROD_ID", prodID);
                stMap.put("TODAY_DT", tempDt);
                stMap.put("EXP_DT", currDate);
                stMap.put("CHARGE_TYPE", "RENT_CHARGES");
                System.out.println("@#$@#$$#stMap" + stMap);
                List stList = ClientUtil.executeQuery("getServiceTaxAndComission", stMap);
                int diffcurmont = edate.getMonth() + 1;
                int calmonth = 12 - diffcurmont;
                if (stList != null && stList.size() > 0) {
                    stMap = null;
                    for (int k = 0; k < stList.size(); k++) {
                        stMap = (HashMap) stList.get(k);
                        double comm = CommonUtil.convertObjToDouble(stMap.get("COMMISION")).doubleValue();
                        double serviceTax = CommonUtil.convertObjToDouble(stMap.get("SERVICE_TAX")).doubleValue();
                        String sdate = CommonUtil.convertObjToStr(stMap.get("START_DT"));
                        String eedate = CommonUtil.convertObjToStr(stMap.get("END_DT"));
                        if (eedate != "") {
                            edt = DateUtil.getDateMMDDYYYY(eedate);
                            Date comparedt = (Date) currDt.clone();
                            comparedt.setDate(edt.getDate());
                            comparedt.setMonth(edt.getMonth());
                            comparedt.setYear(edt.getYear());
                            int eeyear = edt.getYear() + 1900;
                            int eemonth = edt.getMonth() + 1;
                            Date ssdate = DateUtil.getDateMMDDYYYY(sdate);
                            int syear = ssdate.getYear() + 1900;
                            int smonth = ssdate.getMonth() + 1;
                            int diffmonth = (eeyear - syear) + (eemonth - smonth);
                            realmont = calmonth - diffmonth + 1;
                            if (eeyear == i && DateUtil.dateDiff(tempDt, comparedt) >= 0) {
                                if (calmonth != 0) {
                                    if (q == 0) {
                                        realmont = eemonth - (edate.getMonth() + 1);
                                    } else {
                                        realmont = (eemonth - smonth) + 1;
                                    }
                                    double d = comm / 12.0;
                                    dd1 = dd1 + d * realmont;
                                    dd = (double) getNearest((long) (dd1 * 100), 100) / 100;
                                    ser1 = ser1 + ((d * realmont) / 100) * serviceTax;
                                    ser = (double) getNearest((long) (ser1 * 100), 100) / 100;
                                    val1 = val1 + dd + ser;
                                    val = (double) getNearest((long) (val1 * 100), 100) / 100;
                                    calmonth = calmonth - realmont;
                                    q++;
                                }
                            } else if (eeyear > i) {
                                if (calmonth != 0) {
                                    realmont = calmonth;
                                    double d = comm / 12.0;
                                    dd = dd1 + d * realmont;
                                    dd = (double) getNearest((long) (dd * 100), 100) / 100;
                                    ser = ser1 + ((d * realmont) / 100) * serviceTax;
                                    ser = (double) getNearest((long) (ser * 100), 100) / 100;
                                    val = val1 + dd + ser;
                                    val = (double) getNearest((long) (val * 100), 100) / 100;
                                    calmonth = calmonth - realmont;
                                }
                            }
                        } else {
                            if (calmonth != 0) {
                                double d = comm / 12.0;
                                dd = dd1 + d * calmonth;
                                dd = (double) getNearest((long) (dd * 100), 100) / 100;
                                ser = ser1 + ((d * calmonth) / 100) * serviceTax;
                                ser = (double) getNearest((long) (ser * 100), 100) / 100;
                                val = val1 + dd + ser;
                                val = (double) getNearest((long) (val * 100), 100) / 100;
                            }
                        }
                    }
                }
            } else if (ly == i) {
                HashMap stMap = new HashMap();
                Date tempDt = (Date) currDt.clone();
                tempDt.setDate(31);
                tempDt.setMonth(11);
                tempDt.setYear(i - 1900);
                Date currDate = (Date) currDt.clone();
                currDate.setDate(1);
                currDate.setMonth(0);
                currDate.setYear(i - 1900);
                stMap.put("PROD_ID", prodID);
                stMap.put("TODAY_DT", currDate);
                stMap.put("EXP_DT", tempDt);
                stMap.put("CHARGE_TYPE", "RENT_CHARGES");
                List stList = ClientUtil.executeQuery("getServiceTaxAndComission", stMap);
                int diffcurmont = dt.getMonth() + 1;
                int calmonth = diffcurmont;
                if (stList != null && stList.size() > 0) {
                    stMap = null;
                    for (int k = 0; k < stList.size(); k++) {
                        stMap = (HashMap) stList.get(k);
                        double comm = CommonUtil.convertObjToDouble(stMap.get("COMMISION")).doubleValue();
                        double serviceTax = CommonUtil.convertObjToDouble(stMap.get("SERVICE_TAX")).doubleValue();
                        String sdate = CommonUtil.convertObjToStr(stMap.get("START_DT"));
                        String eedate = CommonUtil.convertObjToStr(stMap.get("END_DT"));
                        if (eedate != "") {
                            edt = DateUtil.getDateMMDDYYYY(eedate);
                            int eeyear = edt.getYear() + 1900;
                            int eemonth = edt.getMonth() + 1;
                            Date ssdate = DateUtil.getDateMMDDYYYY(sdate);
                            int syear = ssdate.getYear() + 1900;
                            int smonth = ssdate.getMonth() + 1;
                            int diffmonth = (eeyear - syear) + (eemonth - smonth);
                            realmont = calmonth - diffmonth + 1;
                            if (eeyear == i) {
                                if (calmonth != 0) {
                                    if (calmonth <= eemonth) {
                                        realmont = calmonth;
                                    } else if (calmonth > eemonth) {
                                        realmont = eemonth;
                                    }
                                    double d = comm / 12.0;
                                    dd2 = dd2 + d * realmont;
                                    dd = (double) getNearest((long) (dd2 * 100), 100) / 100;
                                    ser2 = ser2 + ((d * realmont) / 100) * serviceTax;
                                    ser = (double) getNearest((long) (ser2 * 100), 100) / 100;
                                    val2 = val2 + dd + ser;
                                    val = (double) getNearest((long) (val2 * 100), 100) / 100;
                                    calmonth = calmonth - realmont;
                                }
                            } else if (eeyear > i) {
                                if (calmonth != 0) {
                                    realmont = calmonth;
                                    double d = comm / 12.0;
                                    dd = dd2 + d * realmont;
                                    dd = (double) getNearest((long) (dd * 100), 100) / 100;
                                    ser = ser2 + ((d * realmont) / 100) * serviceTax;
                                    ser = (double) getNearest((long) (ser * 100), 100) / 100;
                                    val = val2 + dd + ser;
                                    val = (double) getNearest((long) (val * 100), 100) / 100;
                                    calmonth = calmonth - realmont;
                                }
                            }
                        } else {
                            if (calmonth != 0) {
                                double d = comm / 12.0;
                                dd = dd2 + d * calmonth;
                                dd = (double) getNearest((long) (dd * 100), 100) / 100;
                                ser = ser2 + ((d * calmonth) / 100) * serviceTax;
                                ser = (double) getNearest((long) (ser * 100), 100) / 100;
                                val = val2 + dd + ser;
                                val = (double) getNearest((long) (val * 100), 100) / 100;
                                calmonth = calmonth - realmont;
                            }
                        }
                    }
                    // lyear=lyear+1;
                }  // lmonth=m;
            } else {
                HashMap stMap = new HashMap();
                Date tempDt = (Date) currDt.clone();
                tempDt.setDate(31);
                tempDt.setMonth(11);
                tempDt.setYear(i - 1900);
                Date currDate = (Date) currDt.clone();
                currDate.setDate(1);
                currDate.setMonth(0);
                currDate.setYear(i - 1900);
                stMap.put("PROD_ID", prodID);
                stMap.put("TODAY_DT", currDate);
                stMap.put("EXP_DT", tempDt);
                stMap.put("CHARGE_TYPE", "RENT_CHARGES");
                List stList = ClientUtil.executeQuery("getServiceTaxAndComission", stMap);
                int diffcurmont = edate.getMonth() + 1;
                int calmonth = 12;
                if (stList != null && stList.size() > 0) {
                    stMap = null;
                    for (int k = 0; k < stList.size(); k++) {
                        stMap = (HashMap) stList.get(k);
                        double comm = CommonUtil.convertObjToDouble(stMap.get("COMMISION")).doubleValue();
                        double serviceTax = CommonUtil.convertObjToDouble(stMap.get("SERVICE_TAX")).doubleValue();
                        String sdate = CommonUtil.convertObjToStr(stMap.get("START_DT"));
                        String eedate = CommonUtil.convertObjToStr(stMap.get("END_DT"));
                        if (eedate != "") {
                            edt = DateUtil.getDateMMDDYYYY(eedate);
                            int eeyear = edt.getYear() + 1900;
                            int eemonth = edt.getMonth() + 1;
                            Date ssdate = DateUtil.getDateMMDDYYYY(sdate);
                            int syear = ssdate.getYear() + 1900;
                            int smonth = ssdate.getMonth() + 1;
                            int diffmonth = (eeyear - syear) * 12 + (eemonth - smonth);
                            realmont = calmonth - (diffmonth + 1);
                            if (eeyear == i) {
                                if (calmonth != 0) {
                                    if (calmonth <= eemonth) {
                                        realmont = calmonth;
                                    } else if (calmonth > eemonth) {
                                        realmont = eemonth;
                                    }
                                    double d = comm / 12.0;
                                    dd3 = dd3 + d * realmont;
                                    dd = (double) getNearest((long) (dd3 * 100), 100) / 100;
                                    ser3 = ser3 + ((d * realmont) / 100) * serviceTax;
                                    ser = (double) getNearest((long) (ser3 * 100), 100) / 100;
                                    val3 = val3 + dd + ser;
                                    val = (double) getNearest((long) (val3 * 100), 100) / 100;
                                    calmonth = calmonth - realmont;
                                }
                            } else if (eeyear > i) {
                                if (calmonth != 0) {
                                    realmont = calmonth;
                                    double d = comm / 12.0;
                                    dd = dd3 + d * realmont;
                                    dd = (double) getNearest((long) (dd * 100), 100) / 100;
                                    ser = ser3 + ((d * realmont) / 100) * serviceTax;
                                    ser = (double) getNearest((long) (ser * 100), 100) / 100;
                                    val = val3 + dd + ser;
                                    val = (double) getNearest((long) (val * 100), 100) / 100;
                                    calmonth = calmonth - realmont;
                                }
                            }
                        } else {
                            if (calmonth != 0) {
                                double d = comm / 12.0;
                                dd = dd3 + d * calmonth;
                                dd = (double) getNearest((long) (dd * 100), 100) / 100;
                                ser = ser3 + ((d * calmonth) / 100) * serviceTax;
                                ser = (double) getNearest((long) (ser * 100), 100) / 100;
                                val = val3 + dd + ser;
                                val = (double) getNearest((long) (val * 100), 100) / 100;
                                calmonth = calmonth - realmont;
                            }
                        }
                    }
                }
            }
            db = db + ser;
            dc = dc + dd;
            dval = db + dc;
            dd3 = 0;
            ser3 = 0;
            val3 = 0;
            dd1 = 0;
            dd2 = 0;
            ser1 = 0;
            ser2 = 0;
            val1 = 0;
            val2 = 0;
        }
        double penal = 0.0;
        if (txtPenalAmt.getText() != null && !txtPenalAmt.getText().equals("")) {
            penal = Double.parseDouble(txtPenalAmt.getText());
        }
        txtServiceTax.setText(String.valueOf(db));
        txtCharges.setText(String.valueOf(dc));
        lblTotAmtVal.setText(String.valueOf(dval + penal));
        System.out.println("lblTotAmtVal value" + lblTotAmtVal.getText());
        transactionUI.setCallingAmount(String.valueOf(dval + penal));
        transactionUI.setCallingApplicantName(lblCustNameVal.getText());
    }//GEN-LAST:event_txtCollectRentyyyyFocusLost

    private void txtServiceTaxFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtServiceTaxFocusLost
        // TODO add your handling code here:
        transactionUI.resetObjects();
        transactionUI.cancelAction(true);
        calculateServiceTax();
        double gstVal = CommonUtil.convertObjToDouble(lblServiceTaxval.getText()).doubleValue();
        double com = CommonUtil.convertObjToDouble(txtCharges.getText()).doubleValue();
        double srTax = CommonUtil.convertObjToDouble(txtServiceTax.getText()).doubleValue();
        double penal = CommonUtil.convertObjToDouble(txtPenalAmt.getText()).doubleValue();
        double tot = com + srTax + penal+ gstVal;
        lblTotAmtVal.setText(String.valueOf(tot));
        transactionUI.setCallingAmount(String.valueOf(tot));
        transactionUI.setCallingProdID(observable.getProdID());

    }//GEN-LAST:event_txtServiceTaxFocusLost

    private void cboProdIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdIDActionPerformed
        // TODO add your handling code here:
        final String pid = CommonUtil.convertObjToStr(((ComboBoxModel) (cboProdID.getModel())).getKeyForSelected());
        if (observable.isRenew() && pid.length() > 0 && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            txtLockerNo.setText("");
            setLockerCharges(pid,0);
        }
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            txtLockerNo.setText("");
            lblDateVal.setText("");
            lblCustNameVal.setText("");
            lblCustomerIdVal.setText("");
            lblModeOfOpVal.setText("");
        }
    }//GEN-LAST:event_cboProdIDActionPerformed

    private void setLockerCharges(String prodID,int chargeCount) {
        System.out.println("hhhhhhhhh");
        //System.out.println("penalamt"+pena);
        double penal = 0.0;
        HashMap stMap = new HashMap();
        stMap.put("PROD_ID", prodID);
        stMap.put("TODAY_DT", currDt);
        stMap.put("CHARGE_TYPE", "RENT_CHARGES");
        List stList = ClientUtil.executeQuery("getServiceTaxAndComissionForIssue", stMap);
        if (stList != null && stList.size() > 0) {
            stMap = null;
            stMap = (HashMap) stList.get(0);            
            double comm = CommonUtil.convertObjToDouble(stMap.get("COMMISION")).doubleValue()*chargeCount;
            double serviceTax = CommonUtil.convertObjToDouble(stMap.get("SERVICE_TAX")).doubleValue()*chargeCount;
            txtCharges.setText(CommonUtil.convertObjToStr(comm));
            serviceTaxPercentage = serviceTax;
            serviceTax = comm * serviceTax / 100;
            serviceTax = (double) getNearest((long) (serviceTax * 100), 100) / 100;
            txtServiceTax.setText(String.valueOf(serviceTax));
            if (txtPenalAmt.getText() != null && !txtPenalAmt.getText().equals("")) {
                penal = Double.parseDouble(txtPenalAmt.getText());
            }
            transactionUI.setCallingAmount(String.valueOf(comm + serviceTax + penal));
            lblTotAmtVal.setText(String.valueOf(comm + serviceTax + penal));
            System.out.println("#####rentAmt+servTx" + (comm + serviceTax + penal));
        } else {
            ClientUtil.showMessageWindow("Error In Populating Comission and Service Tax");
        }
    }

    private void rdoSurrenderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSurrenderActionPerformed
        // TODO add your handling code here:
        performRdoAction();
    }//GEN-LAST:event_rdoSurrenderActionPerformed

    private void rdoRenewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoRenewActionPerformed
        // TODO add your handling code here:
        performRdoAction();
    }//GEN-LAST:event_rdoRenewActionPerformed

    private void performRdoAction() {
        ClientUtil.clearAll(this);
        lblExpiryDateVal.setText("");
        if (rdoRenew.isSelected()) {
            lblSurdate.setEnabled(false);
            //            rdoRenew.setSelected(true);
            observable.setSurrender(false);
            observable.setRenew(true);
            observable.setBreakOpen(false);
            observable.setRentCollection(false);
            //            rdoSurrender.setSelected(false);
            lblCurrentDate.setEnabled(false);
            lblCurrentDateVal.setEnabled(false);
            panChargesServiceTax.setEnabled(true);
            panTransaction.setEnabled(true);
            panOperations1.setEnabled(true);
            lblCollectRent.setEnabled(true);
            // lblSurDate.setEnabled(false);
            txtCollectRentMM.setEnabled(true); 
            txtCollectRentyyyy.setEnabled(true);
            lblNewExpDate.setEnabled(true);
            lblRemarks.setEnabled(false);
            txtBreakOpenRemarks.setEnabled(false);
            lblCurrentDate.setEnabled(false);
            cbCustomer.setEnabled(false);
            lblRefund.setEnabled(false);
            txtRefund.setEnabled(false);
            lblExpiryDate.setEnabled(true);
            tdtNewExpDt.setEnabled(true);
            lblSurDate.setEnabled(false);
            chkDefaulter.setEnabled(false);
            txtActNum.setEnabled(false);
            chkNoTrans.setEnabled(false);
            chkRenewBfrExp.setEnabled(true);
        }
        if (rdoSurrender.isSelected()) {
            tdtNewExpDt.setEnabled(false);
            lblSurdate.setEnabled(true);
            chkRenewBfrExp.setEnabled(false);
            chkRenewBfrExp.setSelected(false);
            //            rdoRenew.setSelected(false);
            observable.setSurrender(true);
            observable.setRenew(false);
            observable.setBreakOpen(false);
            observable.setRentCollection(false);
            //            rdoSurrender.setSelected(true);
            panChargesServiceTax.setEnabled(true);
            panOperations1.setEnabled(false);
            lblCollectRent.setEnabled(false);
            panTransaction.setEnabled(false);
            lblCurrentDate.setEnabled(true);
            lblCurrentDateVal.setEnabled(true);
            lblNewExpDate.setEnabled(false);
            // cbRefundYes.setEnabled(true);
            txtCollectRentMM.setEnabled(false);
            txtCollectRentyyyy.setEnabled(false);
            lblRemarks.setEnabled(false);
            txtBreakOpenRemarks.setEnabled(false);
            cbCustomer.setEnabled(false);
            lblRefund.setEnabled(true);
            txtRefund.setEnabled(true);
            lblSurDate.setEnabled(true);
            lblSurDate.setText(CommonUtil.convertObjToStr(currDt));
            //lblExpiryDate.setEnabled(false);
            //lblExpiryDateVal.setEnabled(false);
            chkDefaulter.setEnabled(true);
            txtActNum.setEnabled(false);
            chkNoTrans.setEnabled(true);
        }
        if (rdoBreakOpen.isSelected()) {
            observable.setBreakOpen(true);
            observable.setSurrender(false);
            observable.setRenew(false);
            observable.setRentCollection(false);
            lblCollectRent.setEnabled(false);
            //lblCurrentDate.setEnabled(false);
            lblNewExpDate.setEnabled(false);
            panTransaction.setEnabled(false);
            panChargesServiceTax.setEnabled(true);
            txtCollectRentMM.setEnabled(false);
            txtCollectRentyyyy.setEnabled(false);
            //lblCurrentDate.setEnabled(false);
            lblRemarks.setEnabled(true);
            txtBreakOpenRemarks.setEnabled(true);
            cbCustomer.setEnabled(true);
            lblRefund.setEnabled(false);
            txtRefund.setEnabled(false);
            lblSurdate.setEnabled(false);
            //lblExpiryDate.setEnabled(false);
            //lblSurDate.setEnabled(false);
            chkDefaulter.setEnabled(false);
            txtActNum.setEnabled(false);
            chkNoTrans.setEnabled(false);
            chkRenewBfrExp.setEnabled(false);
            chkRenewBfrExp.setSelected(false);
        }
             if (rdoRentCollection.isSelected()) {
            lblSurdate.setEnabled(false);
            //            rdoRenew.setSelected(true);
            observable.setSurrender(false);
            observable.setRenew(false);
            observable.setBreakOpen(false);
            observable.setRentCollection(true);
            //            rdoSurrender.setSelected(false);
            lblCurrentDate.setEnabled(false);
            lblCurrentDateVal.setEnabled(false);
            panChargesServiceTax.setEnabled(true);
            panTransaction.setEnabled(true);
            panOperations1.setEnabled(true);
            lblCollectRent.setEnabled(true);
            // lblSurDate.setEnabled(false);
            txtCollectRentMM.setEnabled(true);
            txtCollectRentyyyy.setEnabled(true);
            lblNewExpDate.setEnabled(true);
            lblRemarks.setEnabled(false);
            txtBreakOpenRemarks.setEnabled(false);
            lblCurrentDate.setEnabled(false);
            cbCustomer.setEnabled(false);
            lblRefund.setEnabled(false);
            txtRefund.setEnabled(false);
            lblExpiryDate.setEnabled(true);
            tdtNewExpDt.setEnabled(true);
            lblSurDate.setEnabled(false);
            chkDefaulter.setEnabled(false);
            txtActNum.setEnabled(false);
            chkNoTrans.setEnabled(false);
            chkRenewBfrExp.setEnabled(false);
            chkRenewBfrExp.setSelected(false);
        }

    }

    private void txtChargesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtChargesFocusLost
        //        // TODO add your handling code here:
        //        double com = CommonUtil.convertObjToDouble(txtCharges.getText()).doubleValue();
        //        double srTax = CommonUtil.convertObjToDouble(txtServiceTax.getText()).doubleValue();
        //        double tot= com+srTax;
        //        transactionUI.setCallingAmount(String.valueOf(tot));
        // TODO add your handling code here:
//        transactionUI.resetObjects();
//        transactionUI.cancelAction(true);
//        double penal = 0.0;
//        String charges = txtCharges.getText();
//        double chargeVal = CommonUtil.convertObjToDouble(charges).doubleValue();
//        if (charges.length() > 0 && chargeVal > 0) {
//            double serviceTax = chargeVal * serviceTaxPercentage / 100;
//            serviceTax = (double) getNearest((long) (serviceTax * 100), 100) / 100;
//            txtServiceTax.setText(String.valueOf(serviceTax));
//            if (txtPenalAmt.getText() != null && !txtPenalAmt.getText().equals("")) {
//                penal = Double.parseDouble(txtPenalAmt.getText());
//            }
//            System.out.println("hhhhhhhhh" + penal);
//            transactionUI.setCallingAmount(String.valueOf(chargeVal + serviceTax + penal));
//            lblTotAmtVal.setText(String.valueOf(chargeVal + serviceTax + penal));
//
//        } else {
//            txtServiceTax.setText("0");
//            transactionUI.setCallingAmount(String.valueOf(chargeVal));
//            lblTotAmtVal.setText(String.valueOf(chargeVal));
//        }
        calculateServiceTax();
    }//GEN-LAST:event_txtChargesFocusLost
    private void addRow() {
        if (selectedData == 1) {
            observable.setTableValueAt(selectedRow);
            //            observable.resetInstructions();
        } else {
            /**
             * when clicked on the new button related to tblInstruction *
             */
            result = -1;
            result = observable.addTblInstructionData();
        }
    }
    private void btnLockerNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLockerNoActionPerformed
        // TODO add your handling code here:
        tdtNewExpDt.setDateValue("");
        txtCollectRentMM.setText("");
        txtCollectRentyyyy.setText("");
        txtRefund.setText("");
        //lblActNum.setText("");
        callView("LockerNo");
    }//GEN-LAST:event_btnLockerNoActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        callView(ClientConstants.ACTION_STATUS[17]);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        //        setModified(true);
        //        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        //        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        //        btnEdit.setEnabled(false);
        //        btnNew.setEnabled(false);
        //        btnDelete.setEnabled(false);
        //        btnCancel.setEnabled(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        //        txtCharges.setEnabled(false);
        //        txtServiceTax.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // TODO add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed

    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // TODO add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_mitCloseActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // TODO add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // TODO add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        //        if(observable.getAuthorizeStatus()!=null)
        //        super.removeEditLock(txtTokenConfigId.getText());
        setModified(false);
        lblSurDate.setText("");
        lblSurdate.setEnabled(false);
        observable.resetForm();
        //        txtNoOfTokens.setText("");
        ClientUtil.clearAll(this);
        //        ClientUtil.enableDisable(panTokenConfiguration, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        viewType = "";
        settings();
        setModified(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setMainEnableDisable(false);
        //         panLockerOut.setEnabled(false);
        observable.resetInstTbl();
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        btnNew.setEnabled(true);
        btnSave.setEnabled(true);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnCancel.setEnabled(false);
        btnSave.setEnabled(false);
        isFilled = false;
            if (fromNewAuthorizeUI) {
            this.dispose();
            newauthorizeListUI.setFocusToTable();
            fromNewAuthorizeUI = false;
        }
        if (fromAuthorizeUI) {
            this.dispose();
            authorizeListUI.setFocusToTable();
            fromAuthorizeUI = false;
        }
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView(ClientConstants.ACTION_STATUS[3]);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView(ClientConstants.ACTION_STATUS[2]);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        System.out.println("inside save action");
        setModified(false);
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panAccountInfo);
        if (txtLockerNo.getText().length() == 0) {
            mandatoryMessage += resourceBundle.getString("LOCNOWARNING") + "\n";
        }
        if (rdoRenew.isSelected() == false && rdoSurrender.isSelected() == false && rdoBreakOpen.isSelected() == false &&rdoRentCollection.isSelected()==false) {
            mandatoryMessage += resourceBundle.getString("RADIOBITTONSWARNING") + "\n";
        }
        if (observable.isRenew() || observable.isSurrender() || observable.isBreakOpen() || observable. isRentCollection()) {
            if ((observable.isRenew() || observable.isBreakOpen()) && CommonUtil.convertObjToDouble(txtCharges.getText()).doubleValue() == 0) {
                mandatoryMessage += "Charge should not be ZERO\n";
            }
//            if (observable.isSurrender() && CommonUtil.convertObjToDouble(txtCharges.getText()).doubleValue() == 0) {
//                mandatoryMessage += "Charge should not be ZERO\n";
//            }
       
            // To check the transaction amounts tallied...
//            if(((rdoSurrender.isSelected() && CommonUtil.convertObjToDouble(lblTotAmtVal.getText())<=0)||(chkDefaulter.isSelected())) && !pendingPayment)
            if(rdoSurrender.isSelected()&&(chkDefaulter.isSelected()||chkNoTrans.isSelected()) && !pendingPayment)
            {
                System.out.println("inside no trans");
                noSurrenderTrans = true;
            }
            else{
            noSurrenderTrans = false;
            System.out.println("false shiad");
            }
            System.out.println("surrndr trans shi"+noSurrenderTrans);
            if(!noSurrenderTrans){
            java.util.LinkedHashMap transMap = transactionUI.getOutputTO();
            if (transMap != null && transMap.size() > 0) {
                Object[] objKeys = transMap.keySet().toArray();
                TransactionTO objTransactionTO = null;
                double transAmt = 0;
                for (int i = 0; i < objKeys.length; i++) {
                    objTransactionTO = (TransactionTO) transMap.get(objKeys[i]);
                    transAmt = transAmt + objTransactionTO.getTransAmt().doubleValue();
                }
                System.out.println("#$#$ TotalAmount : " + lblTotAmtVal.getText() + " / TransAmt : " + transAmt);
//                if (CommonUtil.convertObjToDouble(lblTotAmtVal.getText()).doubleValue() != transAmt) {
//                    mandatoryMessage += resourceBundle.getString("TRANSAMTWARNING") + "\n";
//                }
            } else {
                mandatoryMessage += resourceBundle.getString("NOTRANSWARNING") + "\n";
             }
            }              
        }
        if (rdoRenew.isSelected()) { // added by shihad for mantis 0010471 on 17.03.2015
            Date oldExpDate = DateUtil.getDateMMDDYYYY(lblExpiryDateVal.getText());
            System.out.println("chkRenewBfrExp.isSelected()"+chkRenewBfrExp.isSelected());
            if (!chkRenewBfrExp.isSelected() && oldExpDate.after(currDt)) {
                ClientUtil.showAlertWindow("Renew Date has not reached");
                btnCancelActionPerformed(null);
                return;
            }
        }
        if (mandatoryMessage.length() > 0) {
            displayAlert(mandatoryMessage);
        } else {
            int transactionSize = 0;
            if (transactionUI.getOutputTO() == null) {
                if (observable.isRenew()) {
                    ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
                    return;
                }
            } else {
                double totalRefundAmount = CommonUtil.convertObjToDouble(txtRefund.getText()).doubleValue();
                double totalAmount = CommonUtil.convertObjToDouble(lblTotAmtVal.getText()).doubleValue();
                double transTotalAmt = CommonUtil.convertObjToDouble(transactionUI.getTransactionOB().getLblTotalTransactionAmtVal()).doubleValue();
                String prodId = CommonUtil.convertObjToStr(((ComboBoxModel) (cboProdID.getModel())).getKeyForSelected());
                HashMap surrenderRefundMap = new HashMap();
                surrenderRefundMap.put("PROD_ID",prodId);
                List list = ClientUtil.executeQuery("getSurrenderRefund", surrenderRefundMap);
                if(list != null && list.size()>0 && rdoSurrender.isSelected()){
                    checkingforTxnAmount(totalAmount,transTotalAmt);
                }else{
                    checkingforTxnAmount(totalAmount,transTotalAmt);
                }
            }
            isFilled = false;
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(true);
            btnException.setEnabled(true);
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void checkingforTxnAmount(double totalAmount,double transTotalAmt){
        int transactionSize = 0;
        System.out.println("tot amount "+totalAmount);
        System.out.println("tot amt hete"+transTotalAmt);
        if (ClientUtil.checkTotalAmountTallied(totalAmount, transTotalAmt) == true || noSurrenderTrans) {
            if(!noSurrenderTrans)
            {  
                System.out.println("inside trans shi");
            transactionSize = (transactionUI.getOutputTO()).size();
            observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
            }
            savePerformed();
        } 
        else if(!noSurrenderTrans) {
            ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NOT_TALLY));
            return;
        }
    }
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.resetForm();
        //        txtNoOfTokens.setText("");
        //        ClientUtil.enableDisable(panTokenConfiguration, true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnLockerNo.setEnabled(true);
        //        ClientUtil.enableDisable(panStdInstructions, true);
        //        ClientUtil.enableDisable(panOperations, false);
        btnLockerNo.setEnabled(true);
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        ClientUtil.enableDisable(panStdInstructions1, true, false, true);
        lblTotAmtVal.setEnabled(false);
        txtLockerNo.setEnabled(false);
        txtPenalAmt.setEnabled(false);
        txtPenalAmt.setEditable(false);
        chkDefaulter.setEnabled(false);
        chkNoTrans.setEnabled(false);
        chkRenewBfrExp.setEnabled(false);
    }//GEN-LAST:event_btnNewActionPerformed

    private void cbCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbCustomerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbCustomerActionPerformed

private void rdoRentCollectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoRentCollectionActionPerformed
// TODO add your handling code here:
    performRdoAction();
}//GEN-LAST:event_rdoRentCollectionActionPerformed

private void txtCollectRentMMFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCollectRentMMFocusLost
// TODO add your handling code here:
    txtCollectRentyyyyFocusLost(evt);
}//GEN-LAST:event_txtCollectRentMMFocusLost

private void btnHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHistoryActionPerformed
    observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
    callView("LockerListClosednDue");
}//GEN-LAST:event_btnHistoryActionPerformed

private void txtLockerNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLockerNoActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtLockerNoActionPerformed

private void chkDefaulterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkDefaulterActionPerformed
// TODO add your handling code here:
    if (chkDefaulter.isSelected()) {
        System.out.println("inside def");
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setMainEnableDisable(false);
    } else {
        System.out.println("inside else def");
        transactionUI.setButtonEnableDisable(false);
        transactionUI.cancelAction(true);
        transactionUI.resetObjects();
        transactionUI.setMainEnableDisable(true);
    }
}//GEN-LAST:event_chkDefaulterActionPerformed

private void tdtNewExpDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtNewExpDtFocusLost
// TODO add your handling code here:
    if(tdtNewExpDt.getDateValue()!=null  && tdtNewExpDt.getDateValue().length()>0){
        System.out.println("herererererere");
        SimpleDateFormat simpleDateformat=new SimpleDateFormat("yyyy");
        final String pid = CommonUtil.convertObjToStr(((ComboBoxModel) (cboProdID.getModel())).getKeyForSelected());
        Date startDt = DateUtil.getDateMMDDYYYY(lblExpiryDateVal.getText());
        Date endDt = DateUtil.getDateMMDDYYYY(tdtNewExpDt.getDateValue());
        int yearDiff = Integer.parseInt(simpleDateformat.format(endDt))- Integer.parseInt(simpleDateformat.format(startDt));
        System.out.println("yearDiff#%#%"+yearDiff);
        setLockerCharges(pid,yearDiff);
    }
}//GEN-LAST:event_tdtNewExpDtFocusLost

    private void txtPenalAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPenalAmtFocusLost
        // TODO add your handling code here:
        calculateServiceTax();
    }//GEN-LAST:event_txtPenalAmtFocusLost

    private void cButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cButton1ActionPerformed
        // TODO add your handling code here:
        String custId = lblCustomerIdVal.getText();
        if (custId != null && !custId.equalsIgnoreCase("")) {
            new com.see.truetransact.ui.common.viewphotosign.ViewPhotoSignUI(custId, "NewActOpening").show();
        }
    }//GEN-LAST:event_cButton1ActionPerformed
    private void btnCheck() {
        btnCancel.setEnabled(true);
        btnSave.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnEdit.setEnabled(false);
    }

    /**
     * Getter for property issueID.
     *
     * @return Value of property issueID.
     */
    public java.lang.String getIssueID() {
        return issueID;
    }

    /**
     * Setter for property issueID.
     *
     * @param issueID New value of property issueID.
     */
    public void setIssueID(java.lang.String issueID) {
        this.issueID = issueID;
    }

    /**
     * Getter for property surOrRenew.
     *
     * @return Value of property surOrRenew.
     */
    public java.lang.String getSurOrRenew() {
        return surOrRenew;
    }

    /**
     * Setter for property surOrRenew.
     *
     * @param surOrRenew New value of property surOrRenew.
     */
    public void setSurOrRenew(java.lang.String surOrRenew) {
        this.surOrRenew = surOrRenew;
    }
    /**
     * // * Getter for property renew. //
     *
     * @return Value of property renew. //
     */
    //    public boolean isRenew() {
    //        return renew;
    //    }
    //
    //    /**
    //     * Setter for property renew.
    //     * @param renew New value of property renew.
    //     */
    //    public void setRenew(boolean renew) {
    //        this.renew = renew;
    //    }
    //
    //    /**
    //     * Getter for property surrender.
    //     * @return Value of property surrender.
    //     */
    //    public boolean isSurrender() {
    //        return surrender;
    //    }
    //
    //    /**
    //     * Setter for property surrender.
    //     * @param surrender New value of property surrender.
    //     */
    //    public void setSurrender(boolean surrender) {
    //        this.surrender = surrender;
    //    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnHistory;
    private com.see.truetransact.uicomponent.CButton btnLockerNo;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CButton cButton1;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CCheckBox cbCustomer;
    private com.see.truetransact.uicomponent.CComboBox cboProdID;
    private com.see.truetransact.uicomponent.CCheckBox chkDefaulter;
    private com.see.truetransact.uicomponent.CCheckBox chkNoTrans;
    private com.see.truetransact.uicomponent.CCheckBox chkRenewBfrExp;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lbSpace3;
    private com.see.truetransact.uicomponent.CLabel lblActNum;
    private com.see.truetransact.uicomponent.CLabel lblCharges;
    private com.see.truetransact.uicomponent.CLabel lblCollectRent;
    private com.see.truetransact.uicomponent.CLabel lblCurrentDate;
    private com.see.truetransact.uicomponent.CLabel lblCurrentDateVal;
    private com.see.truetransact.uicomponent.CLabel lblCustName;
    private com.see.truetransact.uicomponent.CLabel lblCustNameVal;
    private com.see.truetransact.uicomponent.CLabel lblCustomerId;
    private com.see.truetransact.uicomponent.CLabel lblCustomerIdVal;
    private com.see.truetransact.uicomponent.CLabel lblDate;
    private com.see.truetransact.uicomponent.CLabel lblDateVal;
    private com.see.truetransact.uicomponent.CLabel lblExpiryDate;
    private com.see.truetransact.uicomponent.CLabel lblExpiryDateVal;
    private com.see.truetransact.uicomponent.CLabel lblLockerNo;
    private com.see.truetransact.uicomponent.CLabel lblModeOfOp;
    private com.see.truetransact.uicomponent.CLabel lblModeOfOpVal;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNewExpDate;
    private javax.swing.JLabel lblPenalAmt;
    private com.see.truetransact.uicomponent.CLabel lblProdID;
    private com.see.truetransact.uicomponent.CLabel lblRefund;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblServiceTax;
    private com.see.truetransact.uicomponent.CLabel lblServiceTaxval;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace34;
    private com.see.truetransact.uicomponent.CLabel lblSpace35;
    private com.see.truetransact.uicomponent.CLabel lblSpace36;
    private com.see.truetransact.uicomponent.CLabel lblSpace37;
    private com.see.truetransact.uicomponent.CLabel lblSpace38;
    private com.see.truetransact.uicomponent.CLabel lblSpace39;
    private com.see.truetransact.uicomponent.CLabel lblSpace40;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblSurDate;
    private com.see.truetransact.uicomponent.CLabel lblSurdate;
    private javax.swing.JTextField lblTotAmtVal;
    private com.see.truetransact.uicomponent.CLabel lblTotalAmt;
    private com.see.truetransact.uicomponent.CMenuBar mbrTokenConfig;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAccountInfo;
    private com.see.truetransact.uicomponent.CPanel panChargesServiceTax;
    private com.see.truetransact.uicomponent.CPanel panCurrAndNewDate;
    private com.see.truetransact.uicomponent.CPanel panCurrAndNewDate1;
    private com.see.truetransact.uicomponent.CPanel panDate;
    private com.see.truetransact.uicomponent.CPanel panDate1;
    private com.see.truetransact.uicomponent.CPanel panOperations1;
    private com.see.truetransact.uicomponent.CPanel panParameters;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panStdInstructions1;
    private com.see.truetransact.uicomponent.CPanel panTransaction;
    private com.see.truetransact.uicomponent.CButtonGroup rdgSurOrRenew;
    private com.see.truetransact.uicomponent.CRadioButton rdoBreakOpen;
    private com.see.truetransact.uicomponent.CRadioButton rdoRenew;
    private com.see.truetransact.uicomponent.CRadioButton rdoRentCollection;
    private com.see.truetransact.uicomponent.CRadioButton rdoSurrender;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CToolBar tbrTokenConfig;
    private com.see.truetransact.uicomponent.CDateField tdtNewExpDt;
    private com.see.truetransact.uicomponent.CTextField txtActNum;
    private com.see.truetransact.uicomponent.CTextField txtBreakOpenRemarks;
    private com.see.truetransact.uicomponent.CTextField txtCharges;
    private com.see.truetransact.uicomponent.CTextField txtCollectRentMM;
    private com.see.truetransact.uicomponent.CTextField txtCollectRentyyyy;
    private com.see.truetransact.uicomponent.CTextField txtLockerNo;
    private javax.swing.JTextField txtPenalAmt;
    private com.see.truetransact.uicomponent.CTextField txtRefund;
    private com.see.truetransact.uicomponent.CTextField txtServiceTax;
    // End of variables declaration//GEN-END:variables

    public long getNearest(long number, long roundingFactor) {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor % 2) != 0) {
            roundingFactorOdd += 1;
        }
        long mod = number % roundingFactor;
        if ((mod < (roundingFactor / 2)) || (mod < (roundingFactorOdd / 2))) {
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

    /**
     * Getter for property surrenderID.
     *
     * @return Value of property surrenderID.
     */
    public java.lang.String getSurrenderID() {
        return surrenderID;
    }

    /**
     * Setter for property surrenderID.
     *
     * @param surrenderID New value of property surrenderID.
     */
    public void setSurrenderID(java.lang.String surrenderID) {
        this.surrenderID = surrenderID;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
    }

    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }
    
    public Date getProperFormatDate(Object obj) {
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDt = (Date) currDt.clone();
            currDt.setDate(tempDt.getDate());
            currDt.setMonth(tempDt.getMonth());
            currDt.setYear(tempDt.getYear());
        }
        return currDt;
    }
    
    private HashMap getGSTAmountMap(HashMap checkForTaxMap){
        HashMap taxMap = new HashMap();
        if (checkForTaxMap.containsKey("SERVICE_TAX_APPLICABLE") && checkForTaxMap.get("SERVICE_TAX_APPLICABLE") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_APPLICABLE")).equalsIgnoreCase("Y")) {
            if (checkForTaxMap.containsKey("SERVICE_TAX_ID") && checkForTaxMap.get("SERVICE_TAX_ID") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_ID")).length() > 0) {
               taxMap.put("SETTINGS_ID", checkForTaxMap.get("SERVICE_TAX_ID"));    
            }
        }
        return taxMap;
    }
    
    private void calculateServiceTax(){
        // Added by nithya for GST
        HashMap taxMap;
        List taxSettingsList = new ArrayList();
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
                //LOC_RENT_AC_HD,LOC_SUSP_AC_HD,LOC_MISC_AC_HD,LOC_BRK_AC_HD_YN,SERV_TAX_AC_HD,PENAL_INTEREST_AC_HEAD
                if (lockerTransHeadList != null && lockerTransHeadList.size() > 0) {
                    HashMap lockerTransHeadMap = (HashMap) lockerTransHeadList.get(0);
                    //-- GST for Penal --
                    if (txtCharges.getText().length() > 0) {
                        String achd = CommonUtil.convertObjToStr(lockerTransHeadMap.get("LOC_RENT_AC_HD"));
                        HashMap checkForTaxMap = observable.checkServiceTaxApplicable(achd);
                        taxMap = getGSTAmountMap(checkForTaxMap);
                        taxMap.put(ServiceTaxCalculation.TOT_AMOUNT, txtCharges.getText());
                        if (taxMap != null && taxMap.size() > 0) {
                            taxSettingsList.add(taxMap);
                        }
                    }
                    //-- GST for Penal --
                    if (txtServiceTax.getText().length() > 0) {
                        String achd = CommonUtil.convertObjToStr(lockerTransHeadMap.get("SERV_TAX_AC_HD"));
                        HashMap checkForTaxMap = observable.checkServiceTaxApplicable(achd);
                        taxMap = getGSTAmountMap(checkForTaxMap);
                        taxMap.put(ServiceTaxCalculation.TOT_AMOUNT, txtServiceTax.getText());
                        if (taxMap != null && taxMap.size() > 0) {
                            taxSettingsList.add(taxMap);
                        }
                    }
                    //-- GST for Penal --
                    if (txtPenalAmt.getText().length() > 0) {
                        String achd = CommonUtil.convertObjToStr(lockerTransHeadMap.get("PENAL_INTEREST_AC_HEAD"));
                        HashMap checkForTaxMap = observable.checkServiceTaxApplicable(achd);
                        taxMap = getGSTAmountMap(checkForTaxMap);
                        taxMap.put(ServiceTaxCalculation.TOT_AMOUNT, txtPenalAmt.getText());
                        if (taxMap != null && taxMap.size() > 0) {
                            taxSettingsList.add(taxMap);
                        }
                    }
                    System.out.println("taxSettingsList :: " + taxSettingsList);
                    setCaseExpensesAmount(taxSettingsList);
                    if(CommonUtil.convertObjToDouble(lblServiceTaxval.getText()) > 0){
                        double gstVal = CommonUtil.convertObjToDouble(lblServiceTaxval.getText()).doubleValue();
                        double com = CommonUtil.convertObjToDouble(txtCharges.getText()).doubleValue();
                        double srTax = CommonUtil.convertObjToDouble(txtServiceTax.getText()).doubleValue();
                        double penal = CommonUtil.convertObjToDouble(txtPenalAmt.getText()).doubleValue();
                        double tot = com + srTax + penal + gstVal;
                        lblTotAmtVal.setText(String.valueOf(tot));
                        transactionUI.setCallingAmount(String.valueOf(tot));
                    }  
                }
            }
        }
        // End
    }
    
    private void setCaseExpensesAmount(List taxSettingsList) {       
        if (taxSettingsList != null && taxSettingsList.size() > 0) {
            HashMap ser_Tax_Val = new HashMap();
            ser_Tax_Val.put(ServiceTaxCalculation.CURR_DT, currDt);
            ser_Tax_Val.put("SERVICE_TAX_DATA", taxSettingsList);
            try {
                objServiceTax = new ServiceTaxCalculation();
                serviceTax_Map = objServiceTax.calculateServiceTax(ser_Tax_Val);
                if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.TOT_TAX_AMT)) {
                    String amt = CommonUtil.convertObjToStr(serviceTax_Map.get(ServiceTaxCalculation.TOT_TAX_AMT));
                    lblServiceTaxval.setText(amt);
                    observable.setLblServiceTaxval(lblServiceTaxval.getText());
                    serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, amt);
                } else {
                     lblServiceTaxval.setText("0.00");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
}
