/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PassbookDataEntryOB.java
 *
 * 
 */
package com.see.truetransact.ui.passbookDataEntry;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.transferobject.passbookDataEntry.PassbookDataEntryTO;
import com.see.truetransact.uicomponent.CObservable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author anjuanand
 */
public class PassbookDataEntryOB extends CObservable {

    private HashMap operationMap;
    private int actionType;
    private int result;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger log = Logger.getLogger(com.see.truetransact.ui.payroll.pfMaster.PFMasterUI.class);
    private ProxyFactory proxy = null;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private Date curDate = null;
    private static Date currDt = null;
    private ArrayList key;
    private ArrayList value;
    private ArrayList bankKey;
    private ArrayList bankValue;
    private ArrayList branchKey;
    private ArrayList branchValue;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private String cboBankHead = "";
    private String txtBranchHead = "";
    private String cboInstrumentType = "";
    private Date tdtDate = null;
    private String txtTransactionID = "";
    private String txtInstrumentNo1 = "";
    private String txtInstrumentNo2 = "";
    private Date tdtInstrumentDate = null;
    private String txaParticulars = "";
    private double txtAmount = 0;
    private double txtBalance = 0;
    private boolean rdoPayment = false;
    private boolean rdoReceipt = false;
    private ComboBoxModel cbmBankHead;
    private ComboBoxModel cbmBranchHead;
    private ComboBoxModel cbmInstrumentType;
    private String bankName = "";
    private String branchName = "";
    private int srlNo = 0;
    private double balance = 0;
    private double amount = 0;
    private int srl = 0;
    private double txtTotalAmt = 0;
    private TableModel tbmDataEntry;
    private ArrayList recordData, deleteData;
    private PassbookDataEntryTO objPassBookEntryDataTO;
    private String accType = "";

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }

    public double getTxtTotalAmt() {
        return txtTotalAmt;
    }

    public void setTxtTotalAmt(double txtTotalAmt) {
        this.txtTotalAmt = txtTotalAmt;
    }

    public int getSrl() {
        return srl;
    }

    public void setSrl(int srl) {
        this.srl = srl;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public TableModel getTbmDataEntry() {
        return tbmDataEntry;
    }

    public void setTbmDataEntry(TableModel tbmDataEntry) {
        this.tbmDataEntry = tbmDataEntry;
    }

    public int getSrlNo() {
        return srlNo;
    }

    public void setSrlNo(int srlNo) {
        this.srlNo = srlNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public ComboBoxModel getCbmInstrumentType() {
        return cbmInstrumentType;
    }

    public void setCbmInstrumentType(ComboBoxModel cbmInstrumentType) {
        this.cbmInstrumentType = cbmInstrumentType;
        setChanged();
    }

    public ComboBoxModel getCbmBankHead() {
        return cbmBankHead;
    }

    public void setCbmBankHead(ComboBoxModel cbmBankHead) {
        this.cbmBankHead = cbmBankHead;
    }

    public ComboBoxModel getCbmBranchHead() {
        return cbmBranchHead;
    }

    public void setCbmBranchHead(ComboBoxModel cbmBranchHead) {
        this.cbmBranchHead = cbmBranchHead;
    }

    public String getTxaParticulars() {
        return txaParticulars;
    }

    public void setTxaParticulars(String txaParticulars) {
        this.txaParticulars = txaParticulars;
    }

    public double getTxtAmount() {
        return txtAmount;
    }

    public void setTxtAmount(double txtAmount) {
        this.txtAmount = txtAmount;
    }

    public double getTxtBalance() {
        return txtBalance;
    }

    public void setTxtBalance(double txtBalance) {
        this.txtBalance = txtBalance;
    }

    public boolean isRdoPayment() {
        return rdoPayment;
    }

    public void setRdoPayment(boolean rdoPayment) {
        this.rdoPayment = rdoPayment;
    }

    public boolean isRdoReceipt() {
        return rdoReceipt;
    }

    public void setRdoReceipt(boolean rdoReceipt) {
        this.rdoReceipt = rdoReceipt;
    }

    public String getCboBankHead() {
        return cboBankHead;
    }

    public void setCboBankHead(String cboBankHead) {
        this.cboBankHead = cboBankHead;
    }

    public String getTxtBranchHead() {
        return txtBranchHead;
    }

    public void setTxtBranchHead(String txtBranchHead) {
        this.txtBranchHead = txtBranchHead;
    }

    public String getCboInstrumentType() {
        return cboInstrumentType;
    }

    public void setCboInstrumentType(String cboInstrumentType) {
        this.cboInstrumentType = cboInstrumentType;
    }

    public Date getTdtDate() {
        return tdtDate;
    }

    public void setTdtDate(Date tdtDate) {
        this.tdtDate = tdtDate;
    }

    public Date getTdtInstrumentDate() {
        return tdtInstrumentDate;
    }

    public void setTdtInstrumentDate(Date tdtInstrumentDate) {
        this.tdtInstrumentDate = tdtInstrumentDate;
    }

    public String getTxtInstrumentNo1() {
        return txtInstrumentNo1;
    }

    public void setTxtInstrumentNo1(String txtInstrumentNo1) {
        this.txtInstrumentNo1 = txtInstrumentNo1;
    }

    public String getTxtInstrumentNo2() {
        return txtInstrumentNo2;
    }

    public void setTxtInstrumentNo2(String txtInstrumentNo2) {
        this.txtInstrumentNo2 = txtInstrumentNo2;
    }

    public String getTxtTransactionID() {
        return txtTransactionID;
    }

    public void setTxtTransactionID(String txtTransactionID) {
        this.txtTransactionID = txtTransactionID;
    }
    private static PassbookDataEntryOB passBookOB;

    static {
        try {
            log.info("In PassbookDataEntryOB Declaration");
            currDt = ClientUtil.getCurrentDate();
            passBookOB = new PassbookDataEntryOB();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public static PassbookDataEntryOB getInstance() {
        return passBookOB;
    }

    /**
     * Creates a new instance of PassbookDataEntryOB
     */
    public PassbookDataEntryOB() throws Exception {
        setOperationMap();
        setTable();
        recordData = new ArrayList();
        deleteData = new ArrayList();
        initianSetup();
    }

    private void initianSetup() throws Exception {
        setOperationMap();
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
        fillDropdown();
    }

    // Set the value of JNDI and the Session Bean...
    private void setOperationMap() throws Exception {
        log.info("In setOperationMap()");
        proxy = ProxyFactory.createProxy();
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "PassBookDataEntryJNDI");
        operationMap.put(CommonConstants.HOME, "passbookDataEntry.PassbookDataEntryHome");
        operationMap.put(CommonConstants.REMOTE, "passbookDataEntry.PassbookDataEntry");
    }

    private void getKeyValue(HashMap keyValue) throws Exception {
        log.info("getKeyValue");
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
        bankKey = (ArrayList) keyValue.get("BANK_KEY");
        bankValue = (ArrayList) keyValue.get("BANK_VALUE");
        branchKey = (ArrayList) keyValue.get("BRANCH_KEY");
        branchValue = (ArrayList) keyValue.get("BRANCH_VALUE");
    }

    public void fillDropdown() throws Exception {
        log.info("fillDropdown");
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME, null);
        final ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("INSTRUMENTTYPE");

        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get("INSTRUMENTTYPE"));
        int idx = key.indexOf("ONLINE_TRANSFER");
        key.remove(idx);
        value.remove(idx);
        cbmInstrumentType = new ComboBoxModel(key, value);
    }

    private void fillData(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
        bankKey = (ArrayList) keyValue.get("BANK_KEY");
        bankValue = (ArrayList) keyValue.get("BANK_VALUE");
        branchKey = (ArrayList) keyValue.get("BRANCH_KEY");
        branchValue = (ArrayList) keyValue.get("BRANCH_VALUE");
    }

    public List setBankNames() {
        List lst = (List) ClientUtil.executeQuery("getBankNames", null);
        if (lst != null && lst.size() > 0) {
            getMap(lst);
            cbmBankHead = new ComboBoxModel(bankKey, bankValue);
            setCbmBankHead(cbmBankHead);
            return lst;
        }
        return null;
    }

    private void getMap(List list) {
        if (list != null && list.size() > 0) {
            bankKey = new ArrayList();
            bankValue = new ArrayList();
            bankKey.add("");
            bankValue.add("");
            branchKey = new ArrayList();
            branchValue = new ArrayList();
            branchKey.add("");
            branchValue.add("");
            for (int i = 0, j = list.size(); i < j; i++) {
                bankKey.add(((HashMap) list.get(i)).get("BANK_KEY"));
                bankValue.add(((HashMap) list.get(i)).get("BANK_VALUE"));
                branchKey.add(((HashMap) list.get(i)).get("BRANCH_KEY"));
                branchValue.add(((HashMap) list.get(i)).get("BRANCH_VALUE"));
            }
        }
    }

    public Date getCurrentDate() {
        return (Date) curDate.clone();
    }

    private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) curDate.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }

    public void populateOB(HashMap dataMap) throws Exception {
        final List listData;
        HashMap data = new HashMap();
        data.put("BANK_CODE", dataMap.get("BANK_CODE"));
        data.put("BRANCH_CODE", dataMap.get("BRANCH_CODE"));
        data.put("SRL_NO", dataMap.get("SRL_NO"));
        listData = ClientUtil.executeQuery("getAllPassbookDetails", data);
        if (listData != null && listData.size() > 0) {
            HashMap hash = new HashMap();
            hash = (HashMap) listData.get(0);
            setTxtAmount(CommonUtil.convertObjToDouble(hash.get("AMOUNT")));
            setTxtBalance(CommonUtil.convertObjToDouble(hash.get("BALANCE")));
            setTxtInstrumentNo1(CommonUtil.convertObjToStr(hash.get("INSTRUMENT_NO1")));
            setTxtInstrumentNo2(CommonUtil.convertObjToStr(hash.get("INSTRUMENT_NO2")));
            setTxtTransactionID(CommonUtil.convertObjToStr(hash.get("TRANS_ID")));
            setTxaParticulars(CommonUtil.convertObjToStr(hash.get("PARTICULARS")));
            setCboInstrumentType(CommonUtil.convertObjToStr(hash.get("INST_TYPE")));
            setTdtDate((Date) hash.get("TRANS_DATE"));
            setSrlNo(CommonUtil.convertObjToInt(hash.get("SRL_NO")));
            setSrl(CommonUtil.convertObjToInt(hash.get("SRL_NO")));
            setTdtInstrumentDate((Date) hash.get("INST_DT"));
            setCboBankHead(CommonUtil.convertObjToStr(hash.get("BANK_CODE")));
            setTxtBranchHead(CommonUtil.convertObjToStr(hash.get("BRANCH_CODE")));
            if (hash.get("TRANS_TYPE").equals("DEBIT")) {
                setRdoPayment(true);
                setRdoReceipt(false);
            } else if (hash.get("TRANS_TYPE").equals("CREDIT")) {
                setRdoPayment(false);
                setRdoReceipt(true);
            }
        }
        populateTable(listData);
    }

    public void doAction() {
        log.info("In doAction()");
        try {
            //If actionType such as NEW, EDIT, DELETE, then proceed
            if (actionType != ClientConstants.ACTIONTYPE_CANCEL) {
                //If actionType has got propervalue then doActionPerform, else throw error
                if (getCommand() != null) {
                    doActionPerform();
                }
            }
        } catch (Exception e) {
            log.info("Error In doAction()");
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
        }
    }

    /**
     * To perform the necessary action
     */
    private void doActionPerform() throws Exception {
        log.info("In doActionPerform()");
        try {
            final HashMap data = new HashMap();
            final PassbookDataEntryTO objPassBookDataTO = setPassBookData();
            final PassbookDataEntryTO objOtherBankDataTO = setOtherBankData();
            objPassBookDataTO.setCommand(getCommand());
            objOtherBankDataTO.setCommand(getCommand());
            if (getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                HashMap dataMap = new HashMap();
                dataMap.put("BANK_CODE", objOtherBankDataTO.getBankCode());
                dataMap.put("BRANCH_CODE", objOtherBankDataTO.getBranchCode());
                int srlNo = 0;
                srlNo = getMaxSrlNo(dataMap);
                objOtherBankDataTO.setSrlNo(srlNo);
                objPassBookDataTO.setSrlNo(srlNo);
            } else if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                objOtherBankDataTO.setSrlNo(getSrl());
                objPassBookDataTO.setSrlNo(getSrl());
            }
            data.put("OtherBankData", objOtherBankDataTO);
            data.put("PassBookTransactionData", objPassBookDataTO);
            data.put("TRANS_DATA", recordData);
            HashMap proxyResultMap = proxy.execute(data, operationMap);
            resetForm();
        } catch (Exception e) {
            log.info("Error In doActionPerform()");
            e.printStackTrace();
            ClientUtil.showMessageWindow(e.getMessage());
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
        }
    }

    // to decide which action Should be performed...
    private String getCommand() throws Exception {
        log.info("In getCommand()");

        String command = null;
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
            default:
        }
        return command;
    }

    private String getAction() {
        log.info("In getAction()");
        String action = null;
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
            default:
        }
        return action;
    }

    // Returns the Current Value of Action type...
    public int getActionType() {
        return actionType;
    }

    // Sets the value of the Action Type to the poperation we want to execute...
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }

    // To set and change the Status of the lable STATUS
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }

    public int getResult() {
        return this.result;
    }

    // To set the Value of the lblStatus...
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }

    public String getLblStatus() {
        return lblStatus;
    }

    //To reset the Value of lblStatus after each save action...
    public void resetStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
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

    public void ttNotifyObservers() {
        setChanged();
        notifyObservers();
    }

    public void resetForm() {
        setTxtAmount(0);
        setTxtBalance(0);
        setTxtInstrumentNo1("");
        setTxtInstrumentNo2("");
        setTxaParticulars("");
        setTxtTransactionID("");
        setTdtDate(null);
        setTdtInstrumentDate(null);
        setCboBankHead("");
        setTxtBranchHead("");
        setCboInstrumentType("");
        setRdoPayment(false);
        setRdoReceipt(false);
        recordData = new ArrayList();
        this.tbmDataEntry.setData(new ArrayList());
        this.tbmDataEntry.fireTableDataChanged();
        this.recordData.clear();
        this.deleteData.clear();
    }

    private PassbookDataEntryTO setPassBookData() {
        log.info("in setPassBookData");
        PassbookDataEntryTO objPassBookDataTo = new PassbookDataEntryTO();
        objPassBookDataTo.setAmount(getTxtAmount());
        objPassBookDataTo.setInstDate(getTdtInstrumentDate());
        objPassBookDataTo.setInstNo1(getTxtInstrumentNo1());
        objPassBookDataTo.setInstNo2(getTxtInstrumentNo2());
        objPassBookDataTo.setInstType(getCboInstrumentType());
        objPassBookDataTo.setParticulars(getTxaParticulars());
        objPassBookDataTo.setTransDate(getTdtDate());
        objPassBookDataTo.setTransId(getTxtTransactionID());
        objPassBookDataTo.setBalance(getTxtBalance());
        if (isRdoPayment() == true) {
            objPassBookDataTo.setTransType("DEBIT");
        } else if (isRdoReceipt() == true) {
            objPassBookDataTo.setTransType("CREDIT");
        }
        return objPassBookDataTo;
    }

    private PassbookDataEntryTO setOtherBankData() {
        log.info("in setPassBookData");
        PassbookDataEntryTO objPassBookDataTo = new PassbookDataEntryTO();
        objPassBookDataTo.setBankCode(getCboBankHead());
        objPassBookDataTo.setBranchCode(getTxtBranchHead());
        objPassBookDataTo.setAccType(getAccType());
        return objPassBookDataTo;
    }

    List getBankCode(HashMap dataMap) {
        List bankCodeList = ClientUtil.executeQuery("getOtherBankCode", dataMap);
        if (bankCodeList != null && bankCodeList.size() > 0) {
        }
        return bankCodeList;
    }

    List getBranchCode(HashMap dataMap) {
        List branchCodeList = ClientUtil.executeQuery("getOtherBranchCode", dataMap);
        if (branchCodeList != null && branchCodeList.size() > 0) {
        }
        return branchCodeList;
    }

    public double getPassBookMaxBalance(HashMap dataMap) {
        List passBookList = ClientUtil.executeQuery("getPassBookMaxBalance", dataMap);
        if (passBookList != null && passBookList.size() > 0) {
            HashMap passbookMap = new HashMap();
            passbookMap = (HashMap) passBookList.get(0);
            double balance = 0;
            balance = CommonUtil.convertObjToDouble(passbookMap.get("BALANCE"));
            int srlNo = 1;
            srlNo = CommonUtil.convertObjToInt(passbookMap.get("SRL_NO"));
            setSrlNo(srlNo);
            return balance;
        }
        return 0;
    }

    public int getMaxSrlNo(HashMap dataMap) {
        List passBookList = ClientUtil.executeQuery("getPassBookMaxSrlNo", dataMap);
        if (passBookList != null && passBookList.size() > 0) {
            HashMap passbookMap = new HashMap();
            passbookMap = (HashMap) passBookList.get(0);
            int srlNo = 1;
            srlNo = CommonUtil.convertObjToInt(passbookMap.get("SRLNO"));
            return srlNo;
        }
        return 1;
    }

    public boolean chkTransID(HashMap dataMap) {
        List transIdList = ClientUtil.executeQuery("getPassBookTransID", dataMap);
        if (transIdList != null && transIdList.size() > 0) {
            HashMap transIdMap = new HashMap();
            transIdMap = (HashMap) transIdList.get(0);
            String transId = "";
            transId = CommonUtil.convertObjToStr(transIdMap.get("TRANS_ID"));
            return true;
        }
        return false;
    }

    public boolean chkTransDate(HashMap dataMap) {
        List transIdList = ClientUtil.executeQuery("getPassBkTransDate", dataMap);
        if (transIdList != null && transIdList.size() > 0) {
            HashMap transIdMap = new HashMap();
            transIdMap = (HashMap) transIdList.get(0);
            Date transDate = null;
            transDate = (Date) transIdMap.get("TRANS_DATE");
            return true;
        }
        return false;
    }

    List setBranchNames(HashMap branchMap) {
        List lst = (List) ClientUtil.executeQuery("getOtherBranch", branchMap);
        if (lst != null && lst.size() > 0) {
            getMap(lst);
            cbmBranchHead = new ComboBoxModel(branchKey, branchValue);
            setCbmBranchHead(cbmBranchHead);
            return lst;
        }
        return null;
    }

    public void setTable() {
        ArrayList columnHeader = new ArrayList();
        columnHeader.add("Trans ID");
        columnHeader.add("Amount");
        columnHeader.add("Trans Date");
        columnHeader.add("Trans Type");
        ArrayList data = new ArrayList();
        tbmDataEntry = new TableModel(data, columnHeader);
    }

    public int insertIntoTableData(int rowNo) {
        objPassBookEntryDataTO = (PassbookDataEntryTO) setPassBookData();
        ArrayList row = new ArrayList();
        if (rowNo == -1) {
            if (objPassBookEntryDataTO != null) {
                row.add(objPassBookEntryDataTO);
                recordData.add(objPassBookEntryDataTO);
                ArrayList irRow = this.setRow(objPassBookEntryDataTO);
                tbmDataEntry.insertRow(tbmDataEntry.getRowCount(), irRow);
            }
        } else {
            objPassBookEntryDataTO = updateTableDataTO((PassbookDataEntryTO) recordData.get(rowNo), objPassBookEntryDataTO);
            ArrayList irRow = setRow(objPassBookEntryDataTO);
            recordData.set(rowNo, objPassBookEntryDataTO);
            tbmDataEntry.removeRow(rowNo);
            tbmDataEntry.insertRow(rowNo, irRow);
        }
        tbmDataEntry.fireTableDataChanged();
        ttNotifyObservers();
        return 0;
    }

    private ArrayList setRow(PassbookDataEntryTO objPassBookEntryDataTO) {
        ArrayList row = new ArrayList();
        row.add(objPassBookEntryDataTO.getTransId());
        row.add(objPassBookEntryDataTO.getAmount());
        row.add(objPassBookEntryDataTO.getTransDate());
        if (objPassBookEntryDataTO.getTransType().equals("DEBIT")) {
            row.add("Debit");
        } else if (objPassBookEntryDataTO.getTransType().equals("CREDIT")) {
            row.add("Credit");
        }
        return row;
    }

    private PassbookDataEntryTO updateTableDataTO(PassbookDataEntryTO oldPassBookEntryDataTO, PassbookDataEntryTO newPassBookEntryDataTO) {
        oldPassBookEntryDataTO.setTransId(newPassBookEntryDataTO.getTransId());
        oldPassBookEntryDataTO.setAmount(newPassBookEntryDataTO.getAmount());
        oldPassBookEntryDataTO.setTransDate(newPassBookEntryDataTO.getTransDate());
        oldPassBookEntryDataTO.setTransType(newPassBookEntryDataTO.getTransType());
        return oldPassBookEntryDataTO;
    }

    void deleteTblData(int rowSelected) {
        objPassBookEntryDataTO = (PassbookDataEntryTO) recordData.get(rowSelected);
        deleteData.add(objPassBookEntryDataTO);
        recordData.remove(rowSelected);
        tbmDataEntry.removeRow(rowSelected);
        tbmDataEntry.fireTableDataChanged();
    }

    void populateTableData(int rowNum) {
        objPassBookEntryDataTO = (PassbookDataEntryTO) recordData.get(rowNum);
        this.setTableValues(objPassBookEntryDataTO);
        ttNotifyObservers();
    }

    private void setTableValues(PassbookDataEntryTO objPassBookEntryDataTO) {
        setTxtTransactionID(CommonUtil.convertObjToStr(objPassBookEntryDataTO.getTransId()));
        setTxtAmount(CommonUtil.convertObjToDouble(objPassBookEntryDataTO.getAmount()));
        setTdtDate(objPassBookEntryDataTO.getTransDate());
        if (objPassBookEntryDataTO.getTransType().equals("DEBIT")) {
            setRdoPayment(true);
            setRdoReceipt(false);
        } else if (objPassBookEntryDataTO.getTransType().equals("CREDIT")) {
            setRdoPayment(false);
            setRdoReceipt(true);
        }
        setTxtInstrumentNo1(objPassBookEntryDataTO.getInstNo1());
        setTxtInstrumentNo2(objPassBookEntryDataTO.getInstNo2());
        setCboInstrumentType(objPassBookEntryDataTO.getInstType());
        setTdtInstrumentDate(objPassBookEntryDataTO.getInstDate());
        setTxaParticulars(objPassBookEntryDataTO.getParticulars());
    }

    private void populateTable(List lstData) {
        recordData = new ArrayList();
        int size = lstData.size();
        setTable();
        for (int i = 0; i < size; i++) {
            PassbookDataEntryTO objPassBookDataTo = new PassbookDataEntryTO();
            HashMap newMap = new HashMap();
            newMap = (HashMap) lstData.get(i);
            objPassBookDataTo.setSrlNo(CommonUtil.convertObjToInt(newMap.get("SRL_NO")));
            objPassBookDataTo.setTransId(CommonUtil.convertObjToStr(newMap.get("TRANS_ID")));
            objPassBookDataTo.setTransDate((Date) newMap.get("TRANS_DATE"));
            objPassBookDataTo.setAmount(CommonUtil.convertObjToDouble(newMap.get("AMOUNT")));
            objPassBookDataTo.setInstDate((Date) newMap.get("INST_DT"));
            objPassBookDataTo.setInstNo1(CommonUtil.convertObjToStr(newMap.get("INSTRUMENT_NO1")));
            objPassBookDataTo.setInstNo2(CommonUtil.convertObjToStr(newMap.get("INSTRUMENT_NO2")));
            objPassBookDataTo.setParticulars(CommonUtil.convertObjToStr(newMap.get("PARTICULARS")));
            objPassBookDataTo.setInstType(CommonUtil.convertObjToStr(newMap.get("INST_TYPE")));
            if (newMap.get("TRANS_TYPE").equals("DEBIT")) {
                objPassBookDataTo.setTransType("DEBIT");
            } else if (newMap.get("TRANS_TYPE").equals("CREDIT")) {
                objPassBookDataTo.setTransType("CREDIT");
            }
            recordData.add(objPassBookDataTo);
            ArrayList irRow = this.setRow(objPassBookDataTo);
            tbmDataEntry.insertRow(tbmDataEntry.getRowCount(), irRow);
        }
        tbmDataEntry.fireTableDataChanged();
        ttNotifyObservers();
    }
}
