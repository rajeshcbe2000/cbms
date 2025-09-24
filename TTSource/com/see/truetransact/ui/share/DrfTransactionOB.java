/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DeathReliefMasterOB.java
 *
 * Created on Fri Aug 05 13:20:23 GMT+05:30 2011
 */
package com.see.truetransact.ui.share;

import java.util.Observable;
import com.see.truetransact.transferobject.share.DrfTransactionTO;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;
import com.see.truetransact.transferobject.deposit.JointAccntTO;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.*;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.TableUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.ui.common.introducer.*;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.deposit.CommonRB;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.JointAcctHolderManipulation;
import com.see.truetransact.ui.common.transaction.TransactionOB;
import com.see.truetransact.ui.share.*;
import java.util.Date;
import com.see.truetransact.ui.common.transaction.TransactionOB; //trans details
import org.apache.log4j.Logger;

import java.io.OutputStream;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.*;
import oracle.sql.*;
import oracle.jdbc.driver.*;
import com.see.truetransact.commonutil.Dummy;
import java.util.Set;

/**
 *
 * @author
 */
public class DrfTransactionOB extends CObservable {

    private int screenCustType;
    private Date curDate = null;
    private ProxyFactory proxy;
    private final static Logger log = Logger.getLogger(DrfTransactionOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private HashMap map;
    private TransactionOB transactionOB;
    private int actionType;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int result;
    private LinkedHashMap allowedTransactionDetailsTO = null;
    private EnhancedTableModel tblDrfTransaction;
    private LinkedHashMap drfTransMap;
    private DrfTransactionTO objDrfTransactionTO = new DrfTransactionTO();
    HashMap data = new HashMap();
    private ArrayList drfTransList;
    private String txtDrfTransMemberNo = "";
    private String txtDrfTransName = "";
    private String txtResolutionNo = "";
    private String tdtResolutionDate = null;
    //private Date tdtResolutionDate = null;
    private String txtDrfTransAmount = "";
    private String cboDrfTransProdID = "";
    private String drfProductAmount = "";
    private String drfProdPaymentAmt = "";
    private HashMap _authorizeMap;
    private String lblDrfTransAddressCont = "";
    private String rdoDrfTransaction = "";
    private String chkDueAmtPayment = "";
    private String cboProdId = "";
    DrfTransactionRB objDrfTransactionRB = new DrfTransactionRB();
    double totCurrent = 0;
    double totSaleAmount = 0;
    double TOTAL = 0;
    Date currDt = null;
    private LinkedHashMap transactionDetailsTO = null;
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    private ComboBoxModel cbmDrfTransProdID;
    private String drfTransID = "";
//    added by Nikhil
    private boolean shareAccOpn = false;
    List bufferList = new ArrayList();

    public DrfTransactionOB(int param) {
        screenCustType = param;
        try {
            curDate = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "DrfTransactionJNDI");
            map.put(CommonConstants.HOME, "serverside.share.DrfTransactionHome");
            map.put(CommonConstants.REMOTE, "serverside.share.DrfTransaction");
            createDrfTransTable();
            fillDropdown();

        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void fillDropdown() {
        HashMap param = new HashMap();
        param.put(CommonConstants.MAP_NAME, "getDrfProductLookUp");
        param.put(CommonConstants.PARAMFORQUERY, null);
        HashMap where = new HashMap();
        where = null;
        HashMap keyValue = (HashMap) ClientUtil.populateLookupData(param).get(CommonConstants.DATA);
        cbmDrfTransProdID = new ComboBoxModel((ArrayList) keyValue.get(CommonConstants.KEY), (ArrayList) keyValue.get(CommonConstants.VALUE));
        System.out.println("@@@@cbmDrfTransProdID:" + cbmDrfTransProdID);
    }

    public void resetDrfTransDetails() {
        resetForm();
        setChanged();
        ttNotifyObservers();
    }

    public void resetForm() {
        drfTransList = null;
        drfTransMap = null;
        setTxtDrfTransAmount("");
        setTxtDrfTransMemberNo("");
        setTxtDrfTransName("");
        setRdoDrfTransaction("");
        setChkDueAmtPayment("");
        setCboDrfTransProdID("");
        setDrfProdPaymentAmt("");
        setDrfProductAmount("");
        setLblDrfTransAddressCont("");
        setDrfTransID("");
        resetDrfTransListTable();
        setTxtResolutionNo("");
        setTdtResolutionDate(null);
        makeToNull();
        setChanged();
        ttNotifyObservers();
    }

    private void createDrfTransTable() throws Exception {
        final ArrayList drfTransColoumn = new ArrayList();
        drfTransColoumn.add("Transaction ID");
        drfTransColoumn.add("Reciept/Payment");
        drfTransColoumn.add("Amount");
        tblDrfTransaction = new EnhancedTableModel(null, drfTransColoumn);

    }

    public void resetDrfTransListTable() {
        for (int i = tblDrfTransaction.getRowCount(); i > 0; i--) {
            tblDrfTransaction.removeRow(0);
        }
    }

    public void populateData(HashMap whereMap) {
        log.info("In populateData()");
        final HashMap mapData;
        try {
            //            setDivAcno(CommonUtil.convertObjToStr(whereMap.get("DRF_TRANS_ID")));
            //                setDivAmt(CommonUtil.convertObjToDouble(whereMap.get("AMOUNT")));
            //                setDivUptoDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(whereMap.get("DIVIDEND_DT"))));
            setCboDrfTransProdID(CommonUtil.convertObjToStr(getCbmDrfTransProdID().getDataForKey(CommonUtil.convertObjToStr(whereMap.get("DRF_PROD_ID")))));
            //                String where = (String) whereMap.get("DRF_TRANS_ID");
            mapData = proxy.executeQuery(whereMap, map);
            System.out.println("mapData" + mapData);
            if (mapData.containsKey("TRANSACTION_LIST")) {
                List list = (List) mapData.get("TRANSACTION_LIST");
                if (!list.isEmpty()) {
                    transactionOB.setDetails(list);
                }
            }

        } catch (Exception e) {
            System.out.println("Error In populateData()");
            parseException.logException(e, true);
        }
    }

    public void populateDrfTransData(HashMap hashMap, int panEditDelete) {
        String resNo = "";
        Date resDate = null;
        String resoDate="";
        HashMap whereMap = new HashMap();
        LinkedHashMap dataMap = null;
        List rowList = new ArrayList();
        List depreciationList = new ArrayList();
        String mapNameDT = "";
        String mapNameED = "";
        String mapNameGA = "";
        hashMap.put("PROD_ID", hashMap.get("DRF_PROD_ID"));
        whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        whereMap.put("DRF_TRANS_ID", drfTransID);
        whereMap.put("DRF_TRANS_ID", drfTransID);
        HashMap drfTransTableMap = new HashMap();
        if (getActionType() != ClientConstants.ACTIONTYPE_NEW) {
            //Changed BY Suresh
//            mapNameDT = "getSelectDrfTransAuthList";
            mapNameDT = "getMemberDrfTransDetails";
            //            mapNameED = "getSelectPromotionEditTO";
            List list = ClientUtil.executeQuery(mapNameDT, hashMap);
            System.out.println("list in populateDrfTransData" + list);
            for (int i = 0; i < list.size(); i++) {
                System.out.println("iiiiiiiiiiii" + i);
                ArrayList technicalTabRow = new ArrayList();
                drfTransTableMap = (HashMap) list.get(i);
                technicalTabRow = new ArrayList();
                technicalTabRow.add(CommonUtil.convertObjToStr(drfTransTableMap.get("DRF_TRANS_ID")));
                technicalTabRow.add(CommonUtil.convertObjToStr(drfTransTableMap.get("RECIEPT_OR_PAYMENT")));
                technicalTabRow.add(CommonUtil.convertObjToStr(drfTransTableMap.get("AMOUNT")));
                tblDrfTransaction.insertRow(tblDrfTransaction.getRowCount(), technicalTabRow);
                if (!CommonUtil.convertObjToStr(drfTransTableMap.get("RESOLUTION_NO")).equals("")) {
                    resNo = CommonUtil.convertObjToStr(drfTransTableMap.get("RESOLUTION_NO"));
                }
                if (!CommonUtil.convertObjToStr(drfTransTableMap.get("RESOLUTION_DATE")).equals("")) {
                  //  resDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(drfTransTableMap.get("RESOLUTION_DATE")));
               resoDate=CommonUtil.convertObjToStr(drfTransTableMap.get("RESOLUTION_DATE"));
                }

            }

            System.out.println("resNoresNoresNo" + resNo);
            setTxtResolutionNo(resNo);
             //setTdtResolutionDate(resDate);
            setTdtResolutionDate(resoDate);

        }
        whereMap = null;
    }

    public void populateDrfTransTable(List drfTransList) {
        System.out.println("######drfTransList:" + drfTransList);

        HashMap drfTransTableMap = new HashMap();
        int length = drfTransList.size();
        for (int i = 0; i < length; i++) {
            ArrayList technicalTabRow = new ArrayList();
            drfTransTableMap = (HashMap) drfTransList.get(i);
            technicalTabRow = new ArrayList();
            technicalTabRow.add(CommonUtil.convertObjToStr(drfTransTableMap.get("DRF_TRANS_ID")));
            technicalTabRow.add(CommonUtil.convertObjToStr(drfTransTableMap.get("RECIEPT_OR_PAYMENT")));
            technicalTabRow.add(CommonUtil.convertObjToStr(drfTransTableMap.get("AMOUNT")));
            tblDrfTransaction.insertRow(tblDrfTransaction.getRowCount(), technicalTabRow);

        }

    }

    private void insertData() throws Exception {
        setDrfTransData();
    }

    private void updateData() throws Exception {
        setDrfTransData();
    }

    private void deleteData() throws Exception {
        setDrfTransData();
    }

    private DrfTransactionTO setDrfTransDataTO() {
        objDrfTransactionTO = new DrfTransactionTO();
        objDrfTransactionTO.setTxtDrfTransAmount(CommonUtil.convertObjToDouble(getTxtDrfTransAmount()));
        objDrfTransactionTO.setCboDrfTransProdID(getCboDrfTransProdID());
        objDrfTransactionTO.setTxtDrfTransMemberNo(getTxtDrfTransMemberNo());
        objDrfTransactionTO.setTxtDrfTransName(getTxtDrfTransName());
        objDrfTransactionTO.setDrfProdPaymentAmt(getDrfProdPaymentAmt());
        objDrfTransactionTO.setDrfProductAmount(getDrfProductAmount());
        objDrfTransactionTO.setDrfTransID(getDrfTransID());
        if (getRdoDrfTransaction().equals("RECIEPT")) {
            objDrfTransactionTO.setRdoDrfTransaction("RECIEPT");
        } else if (getRdoDrfTransaction().equals("PAYMENT")) {
            objDrfTransactionTO.setRdoDrfTransaction("PAYMENT");
        }
        objDrfTransactionTO.setChkDueAmtPayment(getChkDueAmtPayment());
        objDrfTransactionTO.setStatus(getAction());
        objDrfTransactionTO.setStatusBy(ProxyParameters.USER_ID);
        objDrfTransactionTO.setStatusDate(curDate);
        objDrfTransactionTO.setCommand(getCommand());
        objDrfTransactionTO.setCboDrfProdId(getCboProdId());
        objDrfTransactionTO.setResolutionNo(getTxtResolutionNo());
       
        objDrfTransactionTO.setResolutionDate( DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtResolutionDate())));
       // objDrfTransactionTO.setResolutionDate(getTdtResolutionDate());
        System.out.println("#@$@#$objDrfTransactionTO!??????????????:" + objDrfTransactionTO.getCboDrfProdId());
        return objDrfTransactionTO;
    }

    private void setDrfTransData() {
        objDrfTransactionTO = new DrfTransactionTO();
        objDrfTransactionTO = setDrfTransDataTO();
        System.out.println("@#$@#$@#$objDrfTransactionTO:" + objDrfTransactionTO);
        if (objDrfTransactionTO != null) {
            System.out.println("objDrfTransactionTO:" + objDrfTransactionTO);
            data.put("DrfTransactionTO", objDrfTransactionTO);
        }
//        added by Nikhil for 
        if (getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            if (shareAccOpn) {
                System.out.println("@#$@#$@#$@#$inside here!!");
                data.put("SHARE_ACCT_OPEN", "SHARE_ACCT_OPEN");
            }
            setShareAccOpn(false);
        }
        if (allowedTransactionDetailsTO != null && allowedTransactionDetailsTO.size() > 0) {
            if (transactionDetailsTO == null) {
                transactionDetailsTO = new LinkedHashMap();
            }
            transactionDetailsTO.put(NOT_DELETED_TRANS_TOs, allowedTransactionDetailsTO);
            data.put("TransactionTO", transactionDetailsTO);
            allowedTransactionDetailsTO = null;
        }
        //passing total Interest 
        data.put("TOTAL", "" + getTOTAL());
        //passing interest Details
        data.put("INTEREST_DETAILS_LIST", getBufferList());
    }

    private String getCommand() {
        String command = null;
        System.out.println("actionType : " + actionType);
        switch (actionType) {
            case ClientConstants.ACTIONTYPE_NEW:
                command = CommonConstants.TOSTATUS_INSERT;
                break;
            case ClientConstants.ACTIONTYPE_EDIT:
                command = CommonConstants.TOSTATUS_UPDATE;
                break;
            case ClientConstants.ACTIONTYPE_DELETE:
                command = CommonConstants.TOSTATUS_DELETE;
                break;
            case ClientConstants.ACTIONTYPE_AUTHORIZE:
                command = CommonConstants.STATUS_AUTHORIZED;
                break;
            case ClientConstants.ACTIONTYPE_REJECT:
                command = CommonConstants.STATUS_REJECTED;
                break;
            case ClientConstants.ACTIONTYPE_EXCEPTION:
                command = CommonConstants.STATUS_EXCEPTION;
            default:
        }
        // System.out.println("command : " + command);
        return command;
    }

    private String getAction() {
        String action = null;
        // System.out.println("actionType : " + actionType);
        switch (actionType) {
            case ClientConstants.ACTIONTYPE_NEW:
                action = CommonConstants.STATUS_CREATED;
                break;
            case ClientConstants.ACTIONTYPE_EDIT:
                action = CommonConstants.STATUS_MODIFIED;
                break;
            case ClientConstants.ACTIONTYPE_DELETE:
                action = CommonConstants.STATUS_DELETED;
                break;
            case ClientConstants.ACTIONTYPE_AUTHORIZE:
                action = CommonConstants.STATUS_AUTHORIZED;
                break;
            case ClientConstants.ACTIONTYPE_REJECT:
                action = CommonConstants.STATUS_REJECTED;
                break;
            case ClientConstants.ACTIONTYPE_EXCEPTION:
                action = CommonConstants.STATUS_EXCEPTION;
                break;
            default:
        }
        return action;
    }

    public HashMap doAction() {
        HashMap proxyResultMap = new HashMap();
        try {
            if (data == null) {
                data = new HashMap();
            }

            if (actionType != ClientConstants.ACTIONTYPE_CANCEL) {
                switch (actionType) {
                    case ClientConstants.ACTIONTYPE_NEW:
                        insertData();
                        break;
                    case ClientConstants.ACTIONTYPE_EDIT:
                        updateData();
                        break;
                    case ClientConstants.ACTIONTYPE_DELETE:
                        deleteData();
                        break;
                    default:
                    // throw new ActionNotFoundException();
                }

                if (actionType != ClientConstants.ACTIONTYPE_CANCEL || get_authorizeMap() != null) {
                    if (get_authorizeMap() != null) {
                        data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
                    }
                    if (allowedTransactionDetailsTO != null && allowedTransactionDetailsTO.size() > 0) {
                        if (transactionDetailsTO == null) {
                            transactionDetailsTO = new LinkedHashMap();
                        }
                        transactionDetailsTO.put(NOT_DELETED_TRANS_TOs, allowedTransactionDetailsTO);
                        data.put("TransactionTO", transactionDetailsTO);
                        allowedTransactionDetailsTO = null;
                        //                        data.put("SALE_LIST",drfTransList);
                    }
                    _authorizeMap = null;
                }

                data.put(CommonConstants.MODULE, getModule());
                data.put(CommonConstants.SCREEN, getScreen());
                data.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
                data.put("COMMAND", getCommand());
                data.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                data.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                data.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
                System.out.println("!@#@@@####data" + data);
                proxyResultMap = proxy.execute(data, map);
                setProxyReturnMap(proxyResultMap);
                System.out.println("######## proxyResultMap :" + proxyResultMap);
                data = null;
                setResult(actionType);
                if (proxyResultMap != null && proxyResultMap.containsKey("DRF_TRAN_ID")) {
                    ClientUtil.showMessageWindow("DRF Tran Id: " + CommonUtil.convertObjToStr(proxyResultMap.get("DRF_TRAN_ID")));
                    setProxyReturnMap(proxyResultMap);
                }
                actionType = ClientConstants.ACTIONTYPE_CANCEL;
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
        }
        return proxyResultMap;
    }

    protected void makeToNull() {
        objDrfTransactionTO = null;
        drfTransMap = null;

    }

    public int showAlertWindow(String amtLimit) {
        int option = 1;
        try {
            String[] options = {objDrfTransactionRB.getString("cDialogOK")};
            option = COptionPane.showOptionDialog(null, amtLimit, CommonConstants.WARNINGTITLE,
                    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
        return option;
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

    /**
     * Getter for property actionType.
     *
     * @return Value of property actionType.
     */
    public int getActionType() {
        return actionType;
    }

    /**
     * Setter for property actionType.
     *
     * @param actionType New value of property actionType.
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public void ttNotifyObservers() {
        System.out.println("gggggggggggggggg");
        notifyObservers();
    }

    /**
     * To set the status based on ActionType, either New, Edit, etc.,
     */
    public void setStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }

    /**
     * To update the Status based on result performed by doAction() method
     */
    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }

    /**
     * Getter for property lblStatus.
     *
     * @return Value of property lblStatus.
     */
    public java.lang.String getLblStatus() {
        return lblStatus;
    }

    /**
     * Setter for property lblStatus.
     *
     * @param lblStatus New value of property lblStatus.
     */
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
    }

    /**
     * Getter for property result.
     *
     * @return Value of property result.
     */
    public int getResult() {
        return result;
    }

    /**
     * Setter for property result.
     *
     * @param result New value of property result.
     */
    public void setResult(int result) {
        this.result = result;
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
     * Getter for property txtDrfTransMemberNo.
     *
     * @return Value of property txtDrfTransMemberNo.
     */
    public java.lang.String getTxtDrfTransMemberNo() {
        return txtDrfTransMemberNo;
    }

    /**
     * Setter for property txtDrfTransMemberNo.
     *
     * @param txtDrfTransMemberNo New value of property txtDrfTransMemberNo.
     */
    public void setTxtDrfTransMemberNo(java.lang.String txtDrfTransMemberNo) {
        this.txtDrfTransMemberNo = txtDrfTransMemberNo;
    }

    /**
     * Getter for property txtDrfTransName.
     *
     * @return Value of property txtDrfTransName.
     */
    public java.lang.String getTxtDrfTransName() {
        return txtDrfTransName;
    }

    /**
     * Setter for property txtDrfTransName.
     *
     * @param txtDrfTransName New value of property txtDrfTransName.
     */
    public void setTxtDrfTransName(java.lang.String txtDrfTransName) {
        this.txtDrfTransName = txtDrfTransName;
    }

    /**
     * Getter for property txtDrfTransAmount.
     *
     * @return Value of property txtDrfTransAmount.
     */
    public java.lang.String getTxtDrfTransAmount() {
        return txtDrfTransAmount;
    }

    /**
     * Setter for property txtDrfTransAmount.
     *
     * @param txtDrfTransAmount New value of property txtDrfTransAmount.
     */
    public void setTxtDrfTransAmount(java.lang.String txtDrfTransAmount) {
        this.txtDrfTransAmount = txtDrfTransAmount;
    }

    /**
     * Getter for property _authorizeMap.
     *
     * @return Value of property _authorizeMap.
     */
    public java.util.HashMap get_authorizeMap() {
        return _authorizeMap;
    }

    /**
     * Setter for property _authorizeMap.
     *
     * @param _authorizeMap New value of property _authorizeMap.
     */
    public void set_authorizeMap(java.util.HashMap _authorizeMap) {
        this._authorizeMap = _authorizeMap;
    }

    /**
     * Getter for property lblDrfTransAddressCont.
     *
     * @return Value of property lblDrfTransAddressCont.
     */
    public java.lang.String getLblDrfTransAddressCont() {
        return lblDrfTransAddressCont;
    }

    /**
     * Setter for property lblDrfTransAddressCont.
     *
     * @param lblDrfTransAddressCont New value of property
     * lblDrfTransAddressCont.
     */
    public void setLblDrfTransAddressCont(java.lang.String lblDrfTransAddressCont) {
        this.lblDrfTransAddressCont = lblDrfTransAddressCont;
    }

    /**
     * Getter for property rdoDrfTransaction.
     *
     * @return Value of property rdoDrfTransaction.
     */
    public java.lang.String getRdoDrfTransaction() {
        return rdoDrfTransaction;
    }

    /**
     * Setter for property rdoDrfTransaction.
     *
     * @param rdoDrfTransaction New value of property rdoDrfTransaction.
     */
    public void setRdoDrfTransaction(java.lang.String rdoDrfTransaction) {
        this.rdoDrfTransaction = rdoDrfTransaction;
    }

    /**
     * Getter for property chkDueAmtPayment.
     *
     * @return Value of property chkDueAmtPayment.
     */
    public java.lang.String getChkDueAmtPayment() {
        return chkDueAmtPayment;
    }

    /**
     * Setter for property chkDueAmtPayment.
     *
     * @param chkDueAmtPayment New value of property chkDueAmtPayment.
     */
    public void setChkDueAmtPayment(java.lang.String chkDueAmtPayment) {
        this.chkDueAmtPayment = chkDueAmtPayment;
    }

    /**
     * Getter for property cbmDrfTransProdID.
     *
     * @return Value of property cbmDrfTransProdID.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDrfTransProdID() {
        return cbmDrfTransProdID;
    }

    /**
     * Setter for property cbmDrfTransProdID.
     *
     * @param cbmDrfTransProdID New value of property cbmDrfTransProdID.
     */
    public void setCbmDrfTransProdID(com.see.truetransact.clientutil.ComboBoxModel cbmDrfTransProdID) {
        this.cbmDrfTransProdID = cbmDrfTransProdID;
    }

    /**
     * Getter for property cboDrfTransProdID.
     *
     * @return Value of property cboDrfTransProdID.
     */
    public java.lang.String getCboDrfTransProdID() {
        return cboDrfTransProdID;
    }

    /**
     * Setter for property cboDrfTransProdID.
     *
     * @param cboDrfTransProdID New value of property cboDrfTransProdID.
     */
    public void setCboDrfTransProdID(java.lang.String cboDrfTransProdID) {
        this.cboDrfTransProdID = cboDrfTransProdID;
    }

    /**
     * Getter for property drfProductAmount.
     *
     * @return Value of property drfProductAmount.
     */
    public java.lang.String getDrfProductAmount() {
        return drfProductAmount;
    }

    /**
     * Setter for property drfProductAmount.
     *
     * @param drfProductAmount New value of property drfProductAmount.
     */
    public void setDrfProductAmount(java.lang.String drfProductAmount) {
        this.drfProductAmount = drfProductAmount;
    }

    /**
     * Getter for property drfProdPaymentAmt.
     *
     * @return Value of property drfProdPaymentAmt.
     */
    public java.lang.String getDrfProdPaymentAmt() {
        return drfProdPaymentAmt;
    }

    /**
     * Setter for property drfProdPaymentAmt.
     *
     * @param drfProdPaymentAmt New value of property drfProdPaymentAmt.
     */
    public void setDrfProdPaymentAmt(java.lang.String drfProdPaymentAmt) {
        this.drfProdPaymentAmt = drfProdPaymentAmt;
    }

    /**
     * Getter for property tblDrfTransaction.
     *
     * @return Value of property tblDrfTransaction.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblDrfTransaction() {
        return tblDrfTransaction;
    }

    /**
     * Setter for property tblDrfTransaction.
     *
     * @param tblDrfTransaction New value of property tblDrfTransaction.
     */
    public void setTblDrfTransaction(com.see.truetransact.clientutil.EnhancedTableModel tblDrfTransaction) {
        this.tblDrfTransaction = tblDrfTransaction;
    }

    /**
     * Getter for property drfTransID.
     *
     * @return Value of property drfTransID.
     */
    public java.lang.String getDrfTransID() {
        return drfTransID;
    }

    /**
     * Setter for property drfTransID.
     *
     * @param drfTransID New value of property drfTransID.
     */
    public void setDrfTransID(java.lang.String drfTransID) {
        this.drfTransID = drfTransID;
    }

    /**
     * Getter for property shareAccOpn.
     *
     * @return Value of property shareAccOpn.
     */
    public boolean isShareAccOpn() {
        return shareAccOpn;
    }

    /**
     * Setter for property shareAccOpn.
     *
     * @param shareAccOpn New value of property shareAccOpn.
     */
    public void setShareAccOpn(boolean shareAccOpn) {
        this.shareAccOpn = shareAccOpn;
    }

    /**
     * Getter for property bufferList.
     *
     * @return Value of property bufferList.
     */
    public List getBufferList() {
        return bufferList;
    }

    /**
     * Setter for property bufferList.
     *
     * @param bufferList New value of property bufferList.
     */
    public void setBufferList(List bufferList) {
        this.bufferList = bufferList;
    }

    /**
     * Getter for property cboProdId.
     *
     * @return Value of property cboProdId.
     */
    public String getCboProdId() {
        return cboProdId;
    }

    /**
     * Setter for property cboProdId.
     *
     * @param cboProdId New value of property cboProdId.
     */
    public void setCboProdId(String cboProdId) {
        this.cboProdId = cboProdId;
    }

    /**
     * Getter for property txtRecoveryHead.
     *
     * @return Value of property txtRecoveryHead.
     */
    /**
     * Getter for property TOTAL.
     *
     * @return Value of property TOTAL.
     */
    public double getTOTAL() {
        return TOTAL;
    }

    /**
     * Setter for property TOTAL.
     *
     * @param TOTAL New value of property TOTAL.
     */
    public void setTOTAL(double TOTAL) {
        this.TOTAL = TOTAL;
    }

    public String getTdtResolutionDate() {
        return tdtResolutionDate;
    }

    public void setTdtResolutionDate(String tdtResolutionDate) {
        this.tdtResolutionDate = tdtResolutionDate;
    }

//    public Date getTdtResolutionDate() {
//        return tdtResolutionDate;
//    }
//
//    public void setTdtResolutionDate(Date tdtResolutionDate) {
//        this.tdtResolutionDate = tdtResolutionDate;
//    }

    public String getTxtResolutionNo() {
        return txtResolutionNo;
    }

    public void setTxtResolutionNo(String txtResolutionNo) {
        this.txtResolutionNo = txtResolutionNo;
    }
}