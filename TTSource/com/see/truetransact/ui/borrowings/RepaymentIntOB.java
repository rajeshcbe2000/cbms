/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * RepaymentIntOB.java
 *
 * Created on September 12, 2011, 12:27 PM
 */
package com.see.truetransact.ui.borrowings;

import org.apache.log4j.Logger;
//import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
//import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
//import com.see.truetransact.commonutil.CommonUtil;
//import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.transferobject.borrowings.repayment.BorrowingRepIntClsTO;
//import com.ibatis.db.sqlmap.SqlMap;
import java.util.Date;
import com.see.truetransact.ui.common.transaction.TransactionOB;

/**
 *
 * @author user
 */
public class RepaymentIntOB extends CObservable {

    private final static Logger log = Logger.getLogger(RepaymentIntOB.class);//Creating Instace of Log
    private ProxyFactory proxy;
    private int _actionType;//This vairable is used to type of action 1-New 2-Edit 3-Delete
    private int _result;
    private static RepaymentIntOB objOB;//Singleton Object Reference
    private HashMap map;//HashMap for setting key,values for Jndi,home,remote
    private HashMap lookUpHash;//HashMap used for puting a mapname with a key which is to be used for filling ComboBoxModel
    private ArrayList key, value;//ArrayList for populating the ComboBoxModel key-Storing keys value-for Storing Values
    private HashMap keyValue;//Which holds the resultset values as key,value pairs for populatine ComboBoxModel
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    Date closeDt = null;
    private String closeStatus = "";
    private HashMap authMap = new HashMap();
    private TransactionOB transactionOB;
    private LinkedHashMap allowedTransactionDetailsTO = null;
    private LinkedHashMap transactionDetailsTO = null;
    private LinkedHashMap deletedTransactionDetailsTO = null;
    private final String DELETED_TRANS_TOs = "DELETED_TRANS_TOs";
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    Date currDt = null;
    boolean chkClose = false;
    private String txtBorrowingNo = "", txtAgency = "", txtRepIntClsNo = "",
            txtBorrowingRefNo = "", txtType = "", txtaDescription = "",
            txtPrinRepFrq = "",
            txtIntRepFrq = "", txtMorotorium = "";
    Date tdtDateSanctioned = null, tdtDateExpiry = null, tdtLastRepaidDate = null, tdtIntPaid = null;
    Double txtAmtSanctioned, txtAmtBorrowed, txtRateInterest, txtnoofInstall, txtIntPayable, txtPenalPayable, txtPrinRepaid,
            txtIntRepaid, txtPenalRepaid, txtChargesRepaid, txtPrinBalance, txtIntBalance, txtPenalBalance, txtChargesBalance, txtCurrBal, txtTotR, txtTotD;
    //cheque details
    private String txtCheckNo = "";
    private Date tdtCheckDate = null;
    private Date tdtClosedDte = null;

    public static RepaymentIntOB getInstance() throws Exception {
        return objOB;
    }

    public int getActionType() {
        return _actionType;
    }

    public void setActionType(int actionType) {
        _actionType = actionType;
        // System.out.println("setActionType IN"+actionType);
        setStatus();
        setChanged();
    }

    public void setStatus() {
        // this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
    }

    /**
     * Creates a new instance of RepaymentIntOB
     */
    public RepaymentIntOB() {

        try {
            proxy = ProxyFactory.createProxy();
            setOperationMap();
            currDt = ClientUtil.getCurrentDate();
            //initUIComboBoxModel();
            // fillDropdown();
        } catch (Exception e) {
            //parseException.logException(e,true);
            System.out.println("Error in NewBorrowingOB():" + e);
        }
    }

    static {
        try {
            log.info("Creating ParameterOB...");
            objOB = new RepaymentIntOB();
        } catch (Exception e) {
            // parseException.logException(e,true);
            System.out.println("Error in static():" + e);
        }
    }

    private void setOperationMap() throws Exception {
        map = new HashMap();
        map.put(CommonConstants.JNDI, "BorrowingRepIntClsJNDI");
        map.put(CommonConstants.HOME, "borrowingRepIntCls.BorrowingRepIntClsHome");
        map.put(CommonConstants.REMOTE, "borrowingRepIntCls.BorrowingRepIntCls");

    }

    public void populateData(HashMap whereMap) {
        HashMap mapData = null;
        try {
            mapData = proxy.executeQuery(whereMap, map);
            System.out.println("@#$@#$#@$mapData:" + mapData);
            BorrowingRepIntClsTO objTO = (BorrowingRepIntClsTO) ((List) mapData.get("BorrowingRepIntClsTO")).get(0);
            // setBorrData(mapData);
            setBorrowingRepIntClsTO(objTO);
            if (mapData.containsKey("TRANSACTION_LIST")) {
                List list = (List) mapData.get("TRANSACTION_LIST");
                if (!list.isEmpty()) {
                    transactionOB.setDetails(list);
                }
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
            e.printStackTrace();

        }
    }

    //This fn is set the values into repay/int/closure TO
    private void setBorrowingRepIntClsTO(BorrowingRepIntClsTO objTO) {
        setRepIntClsNo(objTO.getRepintclsNo());
        setTxtBorrowingNo(objTO.getBorrowingNo());
        setTxtAgency(objTO.getAgencyCode());
        setTxtBorrowingRefNo(objTO.getBorrowingrefNo());
        setTxtType(objTO.getType());
        setTxtaDescription(objTO.getDescription());
        setTxtRateInterest(objTO.getRateofInt());
        setTxtnoofInstall(objTO.getNoofInstallments());
        setTxtPrinRepFrq(objTO.getPrinRepFrq());
        setTxtIntRepFrq(objTO.getIntRepFrq());
        setTxtMorotorium(objTO.getMorotorium());
        setTdtDateSanctioned(objTO.getSanctionDate());
        setTdtDateExpiry(objTO.getSanctionExpDate());
        setTxtAmtSanctioned(objTO.getSanctionAmt());
        setTxtAmtBorrowed(objTO.getAmtBorrowed());
        setTdtLastRepaidDate(objTO.getLastRepaiddate());
        setTdtIntPaid(objTO.getDateIntPaid());
        //cheque details
        setTdtCheckDate(objTO.getCheckDate());
        setTxtCheckNo(objTO.getCheckNo());
        //end...
        setTxtIntPayable(objTO.getIntPayable());
        setTxtPenalPayable(objTO.getPenalPayable());
        setTxtPrinRepaid(objTO.getPrincipalRepaid());
        if (CommonUtil.convertObjToStr(objTO.getCloseStatus()).equals("CLOSED")) {
            chkClose = true;
        } else {
            chkClose = false;
        }
        setTdtClosedDte(objTO.getClosedDate());
        setTxtIntRepaid(objTO.getIntRepaid());
        setTxtPenalRepaid(objTO.getPenalRepaid());
        setTxtChargesRepaid(objTO.getChargesRepaid());
        setTxtPrinBalance(objTO.getPrincipalBal());
        setTxtIntBalance(objTO.getInterestBal());
        setTxtPenalBalance(objTO.getPenalBal());
        setTxtChargesBalance(objTO.getChargesBal());
        // System.out.println("CURR BALANCE IN JJ======================"+objTO.getCurrBal());
        setTxtCurrBal(objTO.getCurrBal());
        setTxtTotD(objTO.getTotalD());
        setTxtTotR(objTO.getTotalR());
        notifyObservers();
    }

    //This fn get the values in Repayment/Interest/Closure TO 
    private BorrowingRepIntClsTO getBorrowingRepIntClsTO(String command) {
        BorrowingRepIntClsTO objTO = new BorrowingRepIntClsTO();
        objTO.setCommand(command);
        objTO.setRepintclsNo(getRepIntClsNo());
        objTO.setBorrowingNo(getTxtBorrowingNo());

        objTO.setAgencyCode(getTxtAgency());
        objTO.setBorrowingrefNo(getTxtBorrowingRefNo());

        objTO.setType(getTxtType());
        objTO.setDescription(getTxtaDescription());
        objTO.setSanctionDate(getTdtDateSanctioned());
        objTO.setSanctionAmt(getTxtAmtSanctioned());
        objTO.setRateofInt(getTxtRateInterest());
        objTO.setNoofInstallments(getTxtnoofInstall());
        objTO.setPrinRepFrq(getTxtPrinRepFrq());
        objTO.setIntRepFrq(getTxtIntRepFrq());
        objTO.setMorotorium(getTxtMorotorium());
        objTO.setSanctionExpDate(getTdtDateExpiry());
        objTO.setAmtBorrowed(getTxtAmtBorrowed());
        objTO.setLastRepaiddate(getTdtLastRepaidDate());
        objTO.setDateIntPaid(getTdtIntPaid());
        //cheque details
        objTO.setCheckDate(getTdtCheckDate());
        objTO.setCheckNo(getTxtCheckNo());
        //end...
        objTO.setClosedDate(getTdtClosedDte());
        objTO.setIntPayable(getTxtIntPayable());
        objTO.setPenalPayable(getTxtPenalPayable());
        objTO.setPrincipalRepaid(getTxtPrinRepaid());
        objTO.setIntRepaid(getTxtIntRepaid());
        objTO.setPenalRepaid(getTxtPenalRepaid());
        objTO.setChargesRepaid(getTxtChargesRepaid());
        objTO.setPrincipalBal(getTxtPrinBalance());
        objTO.setInterestBal(getTxtIntBalance());
        objTO.setPenalBal(getTxtPenalBalance());
        objTO.setChargesBal(getTxtChargesBalance());
        objTO.setCurrBal(getTxtCurrBal());
        objTO.setTotalD(getTxtTotD());
        objTO.setTotalR(getTxtTotR());
        //  objTO.setBranchId("0001");
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            objTO.setAuthorizeStatus("");
            objTO.setAuthorizeBy("");
            objTO.setAuthorizeDte(null);
            objTO.setStatus("CREATED");
            objTO.setCreatedBy("");
        }

        return objTO;
    }

    public void resetForm() {
        setRepIntClsNo("");
        setTxtBorrowingNo("");
        setTxtAgency("");
        setTxtBorrowingRefNo("");
        setTxtType("");
        setTxtaDescription("");
        setTxtRateInterest(null);
        setTxtnoofInstall(null);
        setTxtIntRepFrq("");
        setTxtMorotorium("");
        setTdtDateSanctioned(null);
        setTdtDateExpiry(null);
        setTxtAmtSanctioned(null);
        setTxtAmtBorrowed(null);
        setTxtAmtBorrowed(null);
        setTxtPrinRepFrq("");
        setTxtPrinRepaid(null);
        setTdtClosedDte(null);
        setTxtPrinBalance(null);
        setTxtPenalPayable(null);
        setTxtPenalRepaid(null);
        setTxtPenalBalance(null);
        setTxtIntBalance(null);
        setTxtIntRepFrq(null);
        setTxtIntPayable(null);
        setTxtIntRepaid(null);
        setTxtChargesRepaid(null);
        setTxtChargesBalance(null);
        setTxtCurrBal(null);
        setTxtTotD(null);
        setTxtTotR(null);
        notifyObservers();

    }

    public void setResult(int result) {
        _result = result;
        setResultStatus();
        setChanged();
    }

    /**
     * To update the Status based on result performed by doAction() method
     */
    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }

    public int getResult() {
        return _result;
    }

    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
    }

    public java.lang.String getRepIntClsNo() {
        return txtRepIntClsNo;
    }

    /**
     * Setter for property txtBorrowingNo.
     *
     * @param txtBorrowingNo New value of property txtBorrowingNo.
     */
    public void setRepIntClsNo(java.lang.String RepIntClsNo) {
        this.txtRepIntClsNo = RepIntClsNo;
    }

    public void execute(String command) {
        try {
            HashMap term = new HashMap();
            term.put(CommonConstants.MODULE, getModule());
            term.put(CommonConstants.SCREEN, getScreen());
            term.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
            term.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            if (!command.equals(CommonConstants.AUTHORIZESTATUS)) {
                if (chkClose) {
                    term.put("ACT_CLOSING", "ACT_CLOSING");
                }
                term.put("BorrowingRepIntClsTO", getBorrowingRepIntClsTO(command));
                if (transactionDetailsTO == null) {
                    transactionDetailsTO = new LinkedHashMap();
                }
                if (deletedTransactionDetailsTO != null) {
                    transactionDetailsTO.put(DELETED_TRANS_TOs, deletedTransactionDetailsTO);
                    deletedTransactionDetailsTO = null;
                }
                transactionDetailsTO.put(NOT_DELETED_TRANS_TOs, allowedTransactionDetailsTO);
                allowedTransactionDetailsTO = null;
                term.put("TransactionTO", transactionDetailsTO);
            }

            if (getAuthMap() != null && getAuthMap().size() > 0) {
                if (getAuthMap() != null) {
                    term.put(CommonConstants.AUTHORIZEMAP, getAuthMap());
                }
                if (chkClose) {
                    term.put("ACT_CLOSING", "ACT_CLOSING");
                    term.put("BORROWING_NO", getTxtBorrowingNo());
                }
                if (allowedTransactionDetailsTO != null && allowedTransactionDetailsTO.size() > 0) {
                    if (transactionDetailsTO == null) {
                        transactionDetailsTO = new LinkedHashMap();
                    }
                    transactionDetailsTO.put(NOT_DELETED_TRANS_TOs, allowedTransactionDetailsTO);
                    term.put("TransactionTO", transactionDetailsTO);
                    allowedTransactionDetailsTO = null;
                }
                authMap = null;
            }
            HashMap proxyReturnMap = proxy.execute(term, map);
            chkClose = false;
            setProxyReturnMap(proxyReturnMap);
            setResult(getActionType());
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            //parseException.logException(e,true);
            System.out.println("Error in execute():" + e);
        }
    }

    /**
     * Getter for property txtBorrowingNo.
     *
     * @return Value of property txtBorrowingNo.
     */
    public java.lang.String getTxtBorrowingNo() {
        return txtBorrowingNo;
    }

    /**
     * Setter for property txtBorrowingNo.
     *
     * @param txtBorrowingNo New value of property txtBorrowingNo.
     */
    public void setTxtBorrowingNo(java.lang.String txtBorrowingNo) {
        this.txtBorrowingNo = txtBorrowingNo;
    }

    /**
     * Getter for property txtAgency.
     *
     * @return Value of property txtAgency.
     */
    public java.lang.String getTxtAgency() {
        return txtAgency;
    }

    /**
     * Setter for property txtAgency.
     *
     * @param txtAgency New value of property txtAgency.
     */
    public void setTxtAgency(java.lang.String txtAgency) {
        this.txtAgency = txtAgency;
    }

    public boolean isChkClose() {
        return chkClose;
    }

    public void setChkClose(boolean chkClose) {
        this.chkClose = chkClose;
    }

    public Date getTdtClosedDte() {
        return tdtClosedDte;
    }

    public void setTdtClosedDte(Date tdtClosedDte) {
        this.tdtClosedDte = tdtClosedDte;
    }

    /**
     * Getter for property txtBorrowingRefNo.
     *
     * @return Value of property txtBorrowingRefNo.
     */
    public java.lang.String getTxtBorrowingRefNo() {
        return txtBorrowingRefNo;
    }

    /**
     * Setter for property txtBorrowingRefNo.
     *
     * @param txtBorrowingRefNo New value of property txtBorrowingRefNo.
     */
    public void setTxtBorrowingRefNo(java.lang.String txtBorrowingRefNo) {
        this.txtBorrowingRefNo = txtBorrowingRefNo;
    }

    /**
     * Getter for property txtType.
     *
     * @return Value of property txtType.
     */
    public java.lang.String getTxtType() {
        return txtType;
    }

    /**
     * Setter for property txtType.
     *
     * @param txtType New value of property txtType.
     */
    public void setTxtType(java.lang.String txtType) {
        this.txtType = txtType;
    }

    /**
     * Getter for property txtaDescription.
     *
     * @return Value of property txtaDescription.
     */
    public java.lang.String getTxtaDescription() {
        return txtaDescription;
    }

    /**
     * Setter for property txtaDescription.
     *
     * @param txtaDescription New value of property txtaDescription.
     */
    public void setTxtaDescription(java.lang.String txtaDescription) {
        this.txtaDescription = txtaDescription;
    }

    /**
     * Getter for property txtPrinRepFrq.
     *
     * @return Value of property txtPrinRepFrq.
     */
    public java.lang.String getTxtPrinRepFrq() {
        return txtPrinRepFrq;
    }

    /**
     * Setter for property txtPrinRepFrq.
     *
     * @param txtPrinRepFrq New value of property txtPrinRepFrq.
     */
    public void setTxtPrinRepFrq(java.lang.String txtPrinRepFrq) {
        this.txtPrinRepFrq = txtPrinRepFrq;
    }

    /**
     * Getter for property txtIntRepFrq.
     *
     * @return Value of property txtIntRepFrq.
     */
    public java.lang.String getTxtIntRepFrq() {
        return txtIntRepFrq;
    }

    /**
     * Setter for property txtIntRepFrq.
     *
     * @param txtIntRepFrq New value of property txtIntRepFrq.
     */
    public void setTxtIntRepFrq(java.lang.String txtIntRepFrq) {
        this.txtIntRepFrq = txtIntRepFrq;
    }

    /**
     * Getter for property txtMorotorium.
     *
     * @return Value of property txtMorotorium.
     */
    public java.lang.String getTxtMorotorium() {
        return txtMorotorium;
    }

    /**
     * Setter for property txtMorotorium.
     *
     * @param txtMorotorium New value of property txtMorotorium.
     */
    public void setTxtMorotorium(java.lang.String txtMorotorium) {
        this.txtMorotorium = txtMorotorium;
    }

    /**
     * Getter for property tdtDateSanctioned.
     *
     * @return Value of property tdtDateSanctioned.
     */
    public java.util.Date getTdtDateSanctioned() {
        return tdtDateSanctioned;
    }

    /**
     * Setter for property tdtDateSanctioned.
     *
     * @param tdtDateSanctioned New value of property tdtDateSanctioned.
     */
    public void setTdtDateSanctioned(java.util.Date tdtDateSanctioned) {
        this.tdtDateSanctioned = tdtDateSanctioned;
    }

    /**
     * Getter for property tdtDateExpiry.
     *
     * @return Value of property tdtDateExpiry.
     */
    public java.util.Date getTdtDateExpiry() {
        return tdtDateExpiry;
    }

    /**
     * Setter for property tdtDateExpiry.
     *
     * @param tdtDateExpiry New value of property tdtDateExpiry.
     */
    public void setTdtDateExpiry(java.util.Date tdtDateExpiry) {
        this.tdtDateExpiry = tdtDateExpiry;
    }

    /**
     * Getter for property txtAmtSanctioned.
     *
     * @return Value of property txtAmtSanctioned.
     */
    public java.lang.Double getTxtAmtSanctioned() {
        return txtAmtSanctioned;
    }

    /**
     * Setter for property txtAmtSanctioned.
     *
     * @param txtAmtSanctioned New value of property txtAmtSanctioned.
     */
    public void setTxtAmtSanctioned(java.lang.Double txtAmtSanctioned) {
        this.txtAmtSanctioned = txtAmtSanctioned;
    }

    /**
     * Getter for property txtAmtBorrowed.
     *
     * @return Value of property txtAmtBorrowed.
     */
    public java.lang.Double getTxtAmtBorrowed() {
        return txtAmtBorrowed;
    }

    /**
     * Setter for property txtAmtBorrowed.
     *
     * @param txtAmtBorrowed New value of property txtAmtBorrowed.
     */
    public void setTxtAmtBorrowed(java.lang.Double txtAmtBorrowed) {
        this.txtAmtBorrowed = txtAmtBorrowed;
    }

    /**
     * Getter for property txtRateInterest.
     *
     * @return Value of property txtRateInterest.
     */
    public java.lang.Double getTxtRateInterest() {
        return txtRateInterest;
    }

    /**
     * Setter for property txtRateInterest.
     *
     * @param txtRateInterest New value of property txtRateInterest.
     */
    public void setTxtRateInterest(java.lang.Double txtRateInterest) {
        this.txtRateInterest = txtRateInterest;
    }

    /**
     * Getter for property txtnoofInstall.
     *
     * @return Value of property txtnoofInstall.
     */
    public java.lang.Double getTxtnoofInstall() {
        return txtnoofInstall;
    }

    /**
     * Setter for property txtnoofInstall.
     *
     * @param txtnoofInstall New value of property txtnoofInstall.
     */
    public void setTxtnoofInstall(java.lang.Double txtnoofInstall) {
        this.txtnoofInstall = txtnoofInstall;
    }

    /**
     * Getter for property txtIntPayable.
     *
     * @return Value of property txtIntPayable.
     */
    public java.lang.Double getTxtIntPayable() {
        return txtIntPayable;
    }

    /**
     * Setter for property txtIntPayable.
     *
     * @param txtIntPayable New value of property txtIntPayable.
     */
    public void setTxtIntPayable(java.lang.Double txtIntPayable) {
        this.txtIntPayable = txtIntPayable;
    }

    /**
     * Getter for property txtPenalPayable.
     *
     * @return Value of property txtPenalPayable.
     */
    public java.lang.Double getTxtPenalPayable() {
        return txtPenalPayable;
    }

    /**
     * Setter for property txtPenalPayable.
     *
     * @param txtPenalPayable New value of property txtPenalPayable.
     */
    public void setTxtPenalPayable(java.lang.Double txtPenalPayable) {
        this.txtPenalPayable = txtPenalPayable;
    }

    /**
     * Getter for property txtPrinRepaid.
     *
     * @return Value of property txtPrinRepaid.
     */
    public java.lang.Double getTxtPrinRepaid() {
        return txtPrinRepaid;
    }

    /**
     * Setter for property txtPrinRepaid.
     *
     * @param txtPrinRepaid New value of property txtPrinRepaid.
     */
    public void setTxtPrinRepaid(java.lang.Double txtPrinRepaid) {
        this.txtPrinRepaid = txtPrinRepaid;
    }

    /**
     * Getter for property txtIntRepaid.
     *
     * @return Value of property txtIntRepaid.
     */
    public java.lang.Double getTxtIntRepaid() {
        return txtIntRepaid;
    }

    /**
     * Setter for property txtIntRepaid.
     *
     * @param txtIntRepaid New value of property txtIntRepaid.
     */
    public void setTxtIntRepaid(java.lang.Double txtIntRepaid) {
        this.txtIntRepaid = txtIntRepaid;
    }

    /**
     * Getter for property txtPenalRepaid.
     *
     * @return Value of property txtPenalRepaid.
     */
    public java.lang.Double getTxtPenalRepaid() {
        return txtPenalRepaid;
    }

    /**
     * Setter for property txtPenalRepaid.
     *
     * @param txtPenalRepaid New value of property txtPenalRepaid.
     */
    public void setTxtPenalRepaid(java.lang.Double txtPenalRepaid) {
        this.txtPenalRepaid = txtPenalRepaid;
    }

    /**
     * Getter for property txtChargesRepaid.
     *
     * @return Value of property txtChargesRepaid.
     */
    public java.lang.Double getTxtChargesRepaid() {
        return txtChargesRepaid;
    }

    /**
     * Setter for property txtChargesRepaid.
     *
     * @param txtChargesRepaid New value of property txtChargesRepaid.
     */
    public void setTxtChargesRepaid(java.lang.Double txtChargesRepaid) {
        this.txtChargesRepaid = txtChargesRepaid;
    }

    /**
     * Getter for property txtPrinBalance.
     *
     * @return Value of property txtPrinBalance.
     */
    public java.lang.Double getTxtPrinBalance() {
        return txtPrinBalance;
    }

    /**
     * Setter for property txtPrinBalance.
     *
     * @param txtPrinBalance New value of property txtPrinBalance.
     */
    public void setTxtPrinBalance(java.lang.Double txtPrinBalance) {
        this.txtPrinBalance = txtPrinBalance;
    }

    /**
     * Getter for property txtIntBalance.
     *
     * @return Value of property txtIntBalance.
     */
    public java.lang.Double getTxtIntBalance() {
        return txtIntBalance;
    }

    /**
     * Setter for property txtIntBalance.
     *
     * @param txtIntBalance New value of property txtIntBalance.
     */
    public void setTxtIntBalance(java.lang.Double txtIntBalance) {
        this.txtIntBalance = txtIntBalance;
    }

    /**
     * Getter for property txtPenalBalance.
     *
     * @return Value of property txtPenalBalance.
     */
    public java.lang.Double getTxtPenalBalance() {
        return txtPenalBalance;
    }

    /**
     * Setter for property txtPenalBalance.
     *
     * @param txtPenalBalance New value of property txtPenalBalance.
     */
    public void setTxtPenalBalance(java.lang.Double txtPenalBalance) {
        this.txtPenalBalance = txtPenalBalance;
    }

    /**
     * Getter for property txtChargesBalance.
     *
     * @return Value of property txtChargesBalance.
     */
    public java.lang.Double getTxtChargesBalance() {
        return txtChargesBalance;
    }

    /**
     * Setter for property txtChargesBalance.
     *
     * @param txtChargesBalance New value of property txtChargesBalance.
     */
    public void setTxtChargesBalance(java.lang.Double txtChargesBalance) {
        this.txtChargesBalance = txtChargesBalance;
    }

    /**
     * Getter for property tdtIntPaid.
     *
     * @return Value of property tdtIntPaid.
     */
    public java.util.Date getTdtIntPaid() {
        return tdtIntPaid;
    }

    /**
     * Setter for property tdtIntPaid.
     *
     * @param tdtIntPaid New value of property tdtIntPaid.
     */
    public void setTdtIntPaid(java.util.Date tdtIntPaid) {
        this.tdtIntPaid = tdtIntPaid;
    }

    /**
     * Getter for property tdtLastRepaidDate.
     *
     * @return Value of property tdtLastRepaidDate.
     */
    public java.util.Date getTdtLastRepaidDate() {
        return tdtLastRepaidDate;
    }

    /**
     * Setter for property tdtLastRepaidDate.
     *
     * @param tdtLastRepaidDate New value of property tdtLastRepaidDate.
     */
    public void setTdtLastRepaidDate(java.util.Date tdtLastRepaidDate) {
        this.tdtLastRepaidDate = tdtLastRepaidDate;
    }

    /**
     * Getter for property transactionOB.
     *
     * @return Value of property transactionOB.
     */
    public com.see.truetransact.ui.common.transaction.TransactionOB getTransactionOB() {
        return transactionOB;
    }

    /**
     * Setter for property transactionOB.
     *
     * @param transactionOB New value of property transactionOB.
     */
    public void setTransactionOB(com.see.truetransact.ui.common.transaction.TransactionOB transactionOB) {
        this.transactionOB = transactionOB;
    }

    public Date getCloseDt() {
        return closeDt;
    }

    public void setCloseDt(Date closeDt) {
        this.closeDt = closeDt;
    }

    public String getCloseStatus() {
        return closeStatus;
    }

    public void setCloseStatus(String closeStatus) {
        this.closeStatus = closeStatus;
    }

    /**
     * Getter for property deletedTransactionDetailsTO.
     *
     * @return Value of property deletedTransactionDetailsTO.
     */
    public java.util.LinkedHashMap getDeletedTransactionDetailsTO() {
        return deletedTransactionDetailsTO;
    }

    /**
     * Setter for property deletedTransactionDetailsTO.
     *
     * @param deletedTransactionDetailsTO New value of property
     * deletedTransactionDetailsTO.
     */
    public void setDeletedTransactionDetailsTO(java.util.LinkedHashMap deletedTransactionDetailsTO) {
        this.deletedTransactionDetailsTO = deletedTransactionDetailsTO;
    }

    /**
     * Getter for property allowedTransactionDetailsTO.
     *
     * @return Value of property allowedTransactionDetailsTO.
     */
    public java.util.LinkedHashMap getAllowedTransactionDetailsTO() {
        return allowedTransactionDetailsTO;
    }

    /**
     * Setter for property allowedTransactionDetailsTO.
     *
     * @param allowedTransactionDetailsTO New value of property
     * allowedTransactionDetailsTO.
     */
    public void setAllowedTransactionDetailsTO(java.util.LinkedHashMap allowedTransactionDetailsTO) {
        this.allowedTransactionDetailsTO = allowedTransactionDetailsTO;
    }

    /**
     * Getter for property currDt.
     *
     * @return Value of property currDt.
     */
    public java.util.Date getCurrDt() {
        return currDt;
    }

    /**
     * Setter for property currDt.
     *
     * @param currDt New value of property currDt.
     */
    public void setCurrDt(java.util.Date currDt) {
        this.currDt = currDt;
    }

    /**
     * Getter for property authMap.
     *
     * @return Value of property authMap.
     */
    public java.util.HashMap getAuthMap() {
        return authMap;
    }

    /**
     * Setter for property authMap.
     *
     * @param authMap New value of property authMap.
     */
    public void setAuthMap(java.util.HashMap authMap) {
        this.authMap = authMap;
    }

    /**
     * Getter for property txtCurrBal.
     *
     * @return Value of property txtCurrBal.
     */
    public java.lang.Double getTxtCurrBal() {
        return txtCurrBal;
    }

    /**
     * Setter for property txtCurrBal.
     *
     * @param txtCurrBal New value of property txtCurrBal.
     */
    public void setTxtCurrBal(java.lang.Double txtCurrBal) {
        this.txtCurrBal = txtCurrBal;
    }

    /**
     * Getter for property txtTotR.
     *
     * @return Value of property txtTotR.
     */
    public java.lang.Double getTxtTotR() {
        return txtTotR;
    }

    /**
     * Setter for property txtTotR.
     *
     * @param txtTotR New value of property txtTotR.
     */
    public void setTxtTotR(java.lang.Double txtTotR) {
        this.txtTotR = txtTotR;
    }

    /**
     * Getter for property txtTotD.
     *
     * @return Value of property txtTotD.
     */
    public java.lang.Double getTxtTotD() {
        return txtTotD;
    }

    /**
     * Setter for property txtTotD.
     *
     * @param txtTotD New value of property txtTotD.
     */
    public void setTxtTotD(java.lang.Double txtTotD) {
        this.txtTotD = txtTotD;
    }

    /**
     * Getter for property txtCheckNo.
     *
     * @return Value of property txtCheckNo.
     */
    public String getTxtCheckNo() {
        return txtCheckNo;
    }

    /**
     * Setter for property txtCheckNo.
     *
     * @param txtCheckNo New value of property txtCheckNo.
     */
    public void setTxtCheckNo(String txtCheckNo) {
        this.txtCheckNo = txtCheckNo;
    }

    /**
     * Getter for property tdtCheckDate.
     *
     * @return Value of property tdtCheckDate.
     */
    public Date getTdtCheckDate() {
        return tdtCheckDate;
    }

    /**
     * Setter for property tdtCheckDate.
     *
     * @param tdtCheckDate New value of property tdtCheckDate.
     */
    public void setTdtCheckDate(Date tdtCheckDate) {
        this.tdtCheckDate = tdtCheckDate;
    }

}
