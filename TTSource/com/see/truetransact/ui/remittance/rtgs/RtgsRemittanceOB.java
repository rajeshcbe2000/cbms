/*Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemittanceIssueOB.java
 *
 * Created on January 6, 2004, 12:10 PM
 */
package com.see.truetransact.ui.remittance.rtgs;

import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import java.util.Observable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import org.apache.log4j.Logger;
import com.see.truetransact.ui.common.transaction.TransactionOB;
import com.see.truetransact.transferobject.remittance.rtgs.RtgsRemittanceTO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.*;
/**
 *
 * @author Suresh R
 */
public class RtgsRemittanceOB extends com.see.truetransact.uicomponent.CObservable {

    Date curDate = null;
    private String cboProductId = "";
    private String IFSC_Code = "";
    private String cboTransmissionType = "";
    private String txtBeneficiaryBank = "";
    private String txtBeneficiaryBankName = "";
    private String txtBeneficiaryBranch = "";
    private String txtBeneficiaryBranchName = "";
    private String txtBeneficiaryIFSC_Code = "";
    private String txtBeneficiaryName = "";
    private String txtAmt = "";
    private String txtAccountNumber = "";
    private String txtRemarks = "";
    private String txtExchangeCalc = "";
    private String txtExchangeCollect = "";
    private String txtServiceTax = "";
    private String txtCharges = "";
    private String txtTotalAmt = "";
    private String RTGS_ID = "";
    private String slNo = "";
    private ComboBoxModel cbmProductId;
    private ComboBoxModel cbmTransmissionType;
    private boolean newData = false;
    private LinkedHashMap RTGSmap;
    private TransactionOB transactionOB;
    private HashMap oldTransDetMap = null;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private String authStatus = "";
    private final String DELETED_ISSUE_TOs = "DELETED_ISSUE_TOs";
    private final String NOT_DELETED_ISSUE_TOs = "NOT_DELETED_ISSUE_TOs";
    private final String DELETED_TRANS_TOs = "DELETED_TRANS_TOs";
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    private HashMap lookUpHash;
    private HashMap keyValue;
    private HashMap operationMap;
    private LinkedHashMap transactionDetailsTO = null;
    private LinkedHashMap deletedTransactionDetailsTO = null;
    private LinkedHashMap allowedTransactionDetailsTO = null;
    private ArrayList key;
    private ArrayList value;
    private ArrayList tableData = null;
    final ArrayList issueDetailsTitle = new ArrayList(); // Issue Details table Column names
    private int result;
    private int actionType;
    private ProxyFactory proxy;
    private HashMap _authorizeMap;
    private EnhancedTableModel tblIssueDetails;// Issue Details table model
    final RtgsRemittanceRB objRemitIssueRB = new RtgsRemittanceRB();
    private final static Logger log = Logger.getLogger(RtgsRemittanceOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    public boolean depositFlag = false;
    private ArrayList orgORrespDetailsList=new ArrayList();
    private String txtgst= "";
  

    /**
     * Creates a new instance of RemittanceIssueOB
     */
    public RtgsRemittanceOB() throws Exception {
        curDate = ClientUtil.getCurrentDate();
        proxy = ProxyFactory.createProxy();
        setOperationMap();
        fillDropdown();
        setIssueDetailsTitle();
        tblIssueDetails = new EnhancedTableModel(null, issueDetailsTitle);
        makeComboBoxKeyValuesNull();
    }

    /* Sets the HashMap required to set JNDI,Home and Remote*/
    private void setOperationMap() throws Exception {
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "RtgsRemittanceJNDI");
        operationMap.put(CommonConstants.HOME, "RtgsRemittanceHome");
        operationMap.put(CommonConstants.REMOTE, "RtgsRemittance");
        
//        operationMap.put(CommonConstants.JNDI, "RemittanceIssueJNDI");
//        operationMap.put(CommonConstants.HOME, "remittance.RemittanceIssueHome");
//        operationMap.put(CommonConstants.REMOTE, "remittance.RemittanceIssue");
    }

    /* Sets IssueDetails  with table column headers */
    private void setIssueDetailsTitle() throws Exception {
        issueDetailsTitle.add(objRemitIssueRB.getString("tblIssueColumn1"));
        issueDetailsTitle.add(objRemitIssueRB.getString("tblIssueColumn2"));
        issueDetailsTitle.add(objRemitIssueRB.getString("tblIssueColumn3"));
        issueDetailsTitle.add(objRemitIssueRB.getString("tblIssueColumn4"));
    }

    public void fillDropdown() throws Exception {
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME, null);
        final ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("REMITTANCE_ISSUE.TRANSMISSION_TYPE");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get("REMITTANCE_ISSUE.TRANSMISSION_TYPE"));
        cbmTransmissionType = new ComboBoxModel(key, value);
        /* ProdId is taken from Remittance_product */
        lookUpHash.put(CommonConstants.MAP_NAME, "getRTGSProdId");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));
        cbmProductId = new ComboBoxModel(key, value);
        makeComboBoxKeyValuesNull();
    }

    private void makeComboBoxKeyValuesNull() {
        key = null;
        value = null;
        lookUpHash = null;
        keyValue = null;
    }

    private void getKeyValue(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    public void addRTGSDetailsTable(int rowSelected, boolean updateMode) {
        try {
            int rowSel = rowSelected;
            final RtgsRemittanceTO rtgsRemittanceTO = new RtgsRemittanceTO();
            if (RTGSmap == null) {
                RTGSmap = new LinkedHashMap();
            }
            int slno = 0;
            //int nums[] = new int[50];
            //int max = nums[0];
            if (!updateMode) {
                ArrayList data = tblIssueDetails.getDataArrayList();
                slno = serialNo(data);
            } else {
                if (isNewData()) {
                    ArrayList data = tblIssueDetails.getDataArrayList();
                    slno = serialNo(data);
                } else {
                    int b = CommonUtil.convertObjToInt(tblIssueDetails.getValueAt(rowSelected, 0));
                    slno = b;
                }
            }
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isNewData()){
                    rtgsRemittanceTO.setStatus(CommonConstants.STATUS_CREATED);
                }else{
                    rtgsRemittanceTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
            }else{
                rtgsRemittanceTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            rtgsRemittanceTO.setProdId(getCboProductId());
            rtgsRemittanceTO.setIfsc_Code(getIFSC_Code());
            rtgsRemittanceTO.setBeneficiary_Bank(getTxtBeneficiaryBank());
            rtgsRemittanceTO.setBeneficiary_Bank_Name(getTxtBeneficiaryBankName());
            rtgsRemittanceTO.setBeneficiary_Branch(getTxtBeneficiaryBranch());
            rtgsRemittanceTO.setBeneficiary_Branch_Name(getTxtBeneficiaryBranchName());
            rtgsRemittanceTO.setBeneficiary_IFSC_Code(getTxtBeneficiaryIFSC_Code());
            rtgsRemittanceTO.setBeneficiary_Name(getTxtBeneficiaryName());
            rtgsRemittanceTO.setAmount(CommonUtil.convertObjToDouble(getTxtAmt()));
            rtgsRemittanceTO.setAccount_No(getTxtAccountNumber());
            rtgsRemittanceTO.setRemarks(getTxtRemarks());
            rtgsRemittanceTO.setEx_Calculated(CommonUtil.convertObjToDouble(getTxtExchangeCalc()));
            rtgsRemittanceTO.setEx_Collected(CommonUtil.convertObjToDouble(getTxtExchangeCollect()));
            rtgsRemittanceTO.setService_Tax(CommonUtil.convertObjToDouble(getTxtServiceTax()));
            rtgsRemittanceTO.setCharges(CommonUtil.convertObjToDouble(getTxtCharges()));
            double gstAmt = CommonUtil.convertObjToDouble(getTxtgst())/2;
            rtgsRemittanceTO.setCgstAmt(gstAmt);
            rtgsRemittanceTO.setSgstAmt(gstAmt);
            rtgsRemittanceTO.setTotalGstAmt(CommonUtil.convertObjToDouble(getTxtgst()));
            rtgsRemittanceTO.setTotalAmt(CommonUtil.convertObjToDouble(getTxtTotalAmt()));
            rtgsRemittanceTO.setRtgs_ID(getRTGS_ID());
            rtgsRemittanceTO.setSlNo(String.valueOf(slno));
            rtgsRemittanceTO.setStatusDt(curDate);
            rtgsRemittanceTO.setStatusBy(TrueTransactMain.USER_ID);
            rtgsRemittanceTO.setInitiatedBranch(TrueTransactMain.BRANCH_ID);
            RTGSmap.put(slno, rtgsRemittanceTO);
            String sno = String.valueOf(slno);
            updateRTGSTable(rowSel, sno, rtgsRemittanceTO);
            ttNotifyObservers();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void updateRTGSTable(int rowSel, String sno, RtgsRemittanceTO rtgsRemittanceTO) throws Exception {
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append        
        for (int i = tblIssueDetails.getRowCount(), j = 0; i > 0; i--, j++) {
            selectedRow = ((ArrayList) tblIssueDetails.getDataArrayList().get(j)).get(0);
            if (sno.equals(CommonUtil.convertObjToStr(selectedRow))) {
                rowExists = true;
                ArrayList singleRow = new ArrayList();
                ArrayList data = tblIssueDetails.getDataArrayList();
                data.remove(rowSel);
                singleRow.add(sno);
                singleRow.add(CommonUtil.convertObjToStr(getCboProductId()));
                singleRow.add(CommonUtil.convertObjToStr(getTxtAccountNumber()));
                singleRow.add(CommonUtil.convertObjToDouble(getTxtTotalAmt()));
                tblIssueDetails.insertRow(rowSel, singleRow);
                singleRow = null;
            }
        }
        if (!rowExists) {
            ArrayList singleRow = new ArrayList();
            singleRow.add(sno);
            singleRow.add(CommonUtil.convertObjToStr(getCboProductId()));
            singleRow.add(CommonUtil.convertObjToStr(getTxtAccountNumber()));
            singleRow.add(CommonUtil.convertObjToDouble(getTxtTotalAmt()));
            tblIssueDetails.insertRow(tblIssueDetails.getRowCount(), singleRow);
            singleRow = null;
        }
    }

    public int serialNo(ArrayList data) {
        final int dataSize = data.size();
        //int nums[] = new int[50];
        //Changed By kannan on 28-Dec-2016
        int nums[] = new int[250];//maximum table row count 250 only
        int max = nums[0];
        int slno = 0;
        int a = 0;
        slno = dataSize + 1;
        for (int i = 0; i < data.size(); i++) {
            a = CommonUtil.convertObjToInt(tblIssueDetails.getValueAt(i, 0));
            nums[i] = a;
            if (nums[i] > max) {
                max = nums[i];
            }
            slno = max + 1;
        }
        return slno;
    }

    public void populateTableDetails(int row) {
        try {
            final RtgsRemittanceTO rtgsRemittanceTO = (RtgsRemittanceTO) RTGSmap.get(row);
            populateTableData(rtgsRemittanceTO);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void populateTableData(RtgsRemittanceTO rtgsRemittanceTO) throws Exception {
        setCboProductId(CommonUtil.convertObjToStr(rtgsRemittanceTO.getProdId()));
        setIFSC_Code(rtgsRemittanceTO.getIfsc_Code());
        setTxtBeneficiaryBank(rtgsRemittanceTO.getBeneficiary_Bank());
        setTxtBeneficiaryBankName(rtgsRemittanceTO.getBeneficiary_Bank_Name());
        setTxtBeneficiaryBranch(rtgsRemittanceTO.getBeneficiary_Branch());
        setTxtBeneficiaryBranchName(rtgsRemittanceTO.getBeneficiary_Branch_Name());
        setTxtBeneficiaryIFSC_Code(rtgsRemittanceTO.getBeneficiary_IFSC_Code());
        setTxtBeneficiaryName(rtgsRemittanceTO.getBeneficiary_Name());
        setTxtAmt(CommonUtil.convertObjToStr(rtgsRemittanceTO.getAmount()));
        setTxtAccountNumber(rtgsRemittanceTO.getAccount_No());
        setTxtRemarks(rtgsRemittanceTO.getRemarks());
        setTxtExchangeCalc(CommonUtil.convertObjToStr(rtgsRemittanceTO.getEx_Calculated()));
        setTxtExchangeCollect(CommonUtil.convertObjToStr(rtgsRemittanceTO.getEx_Collected()));
        setTxtServiceTax(CommonUtil.convertObjToStr(rtgsRemittanceTO.getService_Tax()));
        //setTxtgst(CommonUtil.convertObjToStr(rtgsRemittanceTO.getSgstAmt() + rtgsRemittanceTO.getCgstAmt()));
        setTxtgst(CommonUtil.convertObjToStr(rtgsRemittanceTO.getTotalGstAmt()));
        setTxtCharges(CommonUtil.convertObjToStr(rtgsRemittanceTO.getCharges()));
        setTxtTotalAmt(CommonUtil.convertObjToStr(rtgsRemittanceTO.getTotalAmt()));
        setRTGS_ID(rtgsRemittanceTO.getRtgs_ID());
        setChanged();
        notifyObservers();
    }

    public void deleteTableData(int val, int row) {
        RtgsRemittanceTO rtgsRemittanceTO = (RtgsRemittanceTO) RTGSmap.get(val);
        Object obj;
        obj = val;
        RTGSmap.remove(val);
        resetTableValues();
        try {
            populateTable();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void populateTable() throws Exception {
        ArrayList DataList = new ArrayList();
        DataList = new ArrayList(RTGSmap.keySet());
        ArrayList addList = new ArrayList(RTGSmap.keySet());
        int length = DataList.size();
        for (int i = 0; i < length; i++) {
            ArrayList incTabRow = new ArrayList();
            RtgsRemittanceTO rtgsRemittanceTO = (RtgsRemittanceTO) RTGSmap.get(addList.get(i));
            incTabRow.add(rtgsRemittanceTO.getSlNo());
            incTabRow.add(rtgsRemittanceTO.getProdId());
            incTabRow.add(rtgsRemittanceTO.getAccount_No());
            incTabRow.add(rtgsRemittanceTO.getTotalAmt());
            tblIssueDetails.addRow(incTabRow);
        }
        notifyObservers();
    }
    
    public void getData(HashMap whereMap) {
        try {
            whereMap.put("FILTERED_LIST", "FILTERED_LIST" + "_" + ProxyParameters.dbDriverName);
            final HashMap data = (HashMap) proxy.executeQuery(whereMap, operationMap);
            System.out.println("#### After DAO Data : " + data);
            HashMap resultMap = new HashMap();
            if (data.containsKey("TRANSFER_TRANS_LIST")) {
                List transList = (List) data.get("TRANSFER_TRANS_LIST");
                resultMap.put("TRANSFER_TRANS_LIST", transList);
                setProxyReturnMap(resultMap);
            }
            if (data.containsKey("TRANSACTION_LIST")) {
                List list = (List) data.get("TRANSACTION_LIST");
                if (!list.isEmpty()) {
                    transactionOB.setDetails(list);
                }
            }
            if (data.containsKey("RTGS_DATA")) {
                if (RTGSmap == null) {
                    RTGSmap = new LinkedHashMap();
                }
                RTGSmap = (LinkedHashMap) data.get("RTGS_DATA");
                ArrayList addList = new ArrayList(RTGSmap.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    RtgsRemittanceTO rtgsRemittanceTO = (RtgsRemittanceTO) RTGSmap.get(addList.get(i));
                    rtgsRemittanceTO.setInitiatedBranch(TrueTransactMain.BRANCH_ID);
                    rtgsRemittanceTO.setSlNo(String.valueOf(i+1));//Added By kannan
                    ArrayList singleRow = new ArrayList();
                    singleRow.add(String.valueOf(i + 1));
                    singleRow.add(rtgsRemittanceTO.getProdId());
                    singleRow.add(rtgsRemittanceTO.getAccount_No());
                    singleRow.add(rtgsRemittanceTO.getTotalAmt());
                    tblIssueDetails.addRow(singleRow);
                }
            }
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }
    
    public void doAction() {
        TTException exception = null;
        try {
            if (actionType != ClientConstants.ACTIONTYPE_CANCEL || get_authorizeMap() != null) {
                doActionPerform();
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            System.err.println("Exception " + e.toString() + "Caught");
            e.printStackTrace();
            if (e instanceof TTException) {
                exception = (TTException) e;
            }
        }
        if (exception != null) {
            HashMap exceptionHashMap = exception.getExceptionHashMap();
            //System.out.println("#### exceptionHashMap : " + exceptionHashMap);
            if (exceptionHashMap != null) {
                ArrayList list = (ArrayList) exceptionHashMap.get(CommonConstants.EXCEPTION_LIST);

                parseException.logException(exception, true);
            } else { // To Display Transaction No showing String message
                parseException.logException(exception, true);
            }
        }
    }
    
    public boolean checkAmount() {  //Added By Suresh R   Ref By Mr Abi and Mr Srinath  06-Jul-2017
        boolean isError = false;
        if (RTGSmap != null && RTGSmap.size() > 0) {
            ArrayList addList = new ArrayList(RTGSmap.keySet());
            double allAmount = 0.0;
            double totalAmount = 0.0;
            for (int i = 0; i < addList.size(); i++) {
                RtgsRemittanceTO objRtgsRemittanceTO = (RtgsRemittanceTO) RTGSmap.get(addList.get(i));
                allAmount = CommonUtil.convertObjToDouble(objRtgsRemittanceTO.getAmount())
                        + CommonUtil.convertObjToDouble(objRtgsRemittanceTO.getEx_Calculated())
                        + CommonUtil.convertObjToDouble(objRtgsRemittanceTO.getEx_Collected())
                        + CommonUtil.convertObjToDouble(objRtgsRemittanceTO.getService_Tax())
                        + CommonUtil.convertObjToDouble(objRtgsRemittanceTO.getCharges());
                totalAmount = CommonUtil.convertObjToDouble(objRtgsRemittanceTO.getTotalAmt());
                if (allAmount > totalAmount || allAmount < totalAmount) {
                    System.out.println("########### allAmount   : " + allAmount);
                    System.out.println("########### totalAmount : " + totalAmount);
                    ClientUtil.showMessageWindow("Total Amount is Wrong, Please Re-Enter the Amount ("
                            + CommonUtil.convertObjToStr(objRtgsRemittanceTO.getBeneficiary_Name()) + " - "
                            + CommonUtil.convertObjToStr(objRtgsRemittanceTO.getAccount_No()) + " - " + i + 1 + " )");
                    return true;
                }
            }
        }
        return isError;
    }
    
    private void doActionPerform() throws Exception {
        TTException exception = null;
        try {
            final HashMap data = new HashMap();
            data.put("COMMAND", getCommand());
            data.put("USER_ID", TrueTransactMain.USER_ID);
            data.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            if (getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                data.put("RTGS_ID", getRTGS_ID());
            }
            if (getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
                data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
            }
            if (RTGSmap != null && RTGSmap.size() > 0) {
                data.put("RTGS_DATA", RTGSmap);
            }
            if (allowedTransactionDetailsTO != null && allowedTransactionDetailsTO.size() > 0) {
                if (transactionDetailsTO == null) {
                    transactionDetailsTO = new LinkedHashMap();
                }
                transactionDetailsTO.put(NOT_DELETED_TRANS_TOs, allowedTransactionDetailsTO);
                data.put("TransactionTO", transactionDetailsTO);
                //Added by kannan        
                if (getOrgORrespDetailsList() != null && getOrgORrespDetailsList().size() > 0) {
                    data.put("ORG_RESP_DETAILS", getOrgORrespDetailsList());
                }
                allowedTransactionDetailsTO = null;
            }
            //Added by Kannan
            data.put(CommonConstants.MODULE, getModule());
            data.put(CommonConstants.SCREEN, getScreen());
            //System.out.println("############# RTGS OB Data : " + data);
            HashMap proxyResultMap = proxy.execute(data, operationMap);
            //System.out.println("############# proxyResultMap : " + proxyResultMap + "actionType" + actionType);
            setProxyReturnMap(proxyResultMap);
            setResult(actionType);
            _authorizeMap = null;
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            setProxyReturnMap(null);
            e.printStackTrace();
            if (e instanceof TTException) {
                exception = (TTException) e;
                parseException.logException(exception, true);
            }
        }
    }

    public void setCbmTransmissionType(ComboBoxModel cbmTransmissionType) {
        this.cbmTransmissionType = cbmTransmissionType;
        setChanged();
    }

    ComboBoxModel getCbmTransmissionType() {
        return cbmTransmissionType;
    }

    public void setCbmProductId(ComboBoxModel cbmProductId) {
        this.cbmProductId = cbmProductId;
        setChanged();
    }

    ComboBoxModel getCbmProductId() {
        return cbmProductId;
    }

    public void resetStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }

    public void ttNotifyObservers() {
        notifyObservers();
    }

    /* To get the type of command */
    private String getCommand() throws Exception {
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

    /* Reset the whole form remittance*/
    public void resetForm() {
        setCboProductId("");
        setCboTransmissionType("");
        setTxtBeneficiaryBank("");
        setTxtBeneficiaryBranch("");
        setTxtBeneficiaryIFSC_Code("");
        setTxtBeneficiaryName("");
        setTxtAmt("");
        setTxtAccountNumber("");
        setTxtRemarks("");
        setTxtExchangeCalc("");
        setTxtExchangeCollect("");
        setTxtServiceTax("");
        setTxtgst("");
        setTxtCharges("");
        setTxtTotalAmt("");
        setRTGS_ID("");
        ttNotifyObservers();
        tblIssueDetails = new EnhancedTableModel(null, issueDetailsTitle);
        tableData = null;
        RTGSmap = null;
        ttNotifyObservers();
    }

    /* Reset the fields present in the issue details */
    public void resetRTGSDetails() {
        //setCboProductId("");
        setCboTransmissionType("");
        //setTxtBeneficiaryBank("");
        setTxtBeneficiaryBranch("");
        setTxtBeneficiaryIFSC_Code("");
        setTxtBeneficiaryName("");
        setTxtAmt("");
        setTxtAccountNumber("");
        setTxtRemarks("");
        setTxtExchangeCalc("");
        setTxtExchangeCollect("");
        setTxtServiceTax("");
        setTxtgst("");
        setTxtCharges("");
        setTxtTotalAmt("");
        //setRTGS_ID("");
        ttNotifyObservers();
    }
    
    public void resetRTGS_ID() {
        setRTGS_ID("");
    }

    public void resetTableValues() {
        tblIssueDetails.setDataArrayList(null, issueDetailsTitle);
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
        // System.out.println("In OB of RemIssue : " + allowedTransactionDetailsTO);
        this.allowedTransactionDetailsTO = allowedTransactionDetailsTO;
    }

    public void setDupTransactionDetailsTO(java.util.LinkedHashMap allowedTransactionDetailsTO) {
        // System.out.println("In OB of RemIssue : " + allowedTransactionDetailsTO);
        this.allowedTransactionDetailsTO = allowedTransactionDetailsTO;
    }

    /**
     * Setter for property transactionOB.
     *
     * @param transactionOB New value of property transactionOB.
     */
    public void setTransactionOB(TransactionOB transactionOB) {
        this.transactionOB = transactionOB;
    }

    // Setter method for cboProductId
    void setCboProductId(String cboProductId) {
        this.cboProductId = cboProductId;
        setChanged();
    }
    // Getter method for cboProductId

    String getCboProductId() {
        return this.cboProductId;
    }

    // Setter method for cboTransmissionType
    void setCboTransmissionType(String cboTransmissionType) {
        this.cboTransmissionType = cboTransmissionType;
        setChanged();
    }
    // Getter method for cboTransmissionType

    String getCboTransmissionType() {
        return this.cboTransmissionType;
    }

    // Setter method for txtAmt
    void setTxtAmt(String txtAmt) {
        this.txtAmt = txtAmt;
        setChanged();
    }
    // Getter method for txtAmt

    String getTxtAmt() {
        return this.txtAmt;
    }

    // Setter method for txtExchangeCollect
    void setTxtExchangeCollect(String txtExchangeCollect) {
        this.txtExchangeCollect = txtExchangeCollect;
        setChanged();
    }
    // Getter method for txtExchangeCollect

    String getTxtExchangeCollect() {
        return this.txtExchangeCollect;
    }

    // Setter method for txtTotalAmt
    public void setTxtTotalAmt(String txtTotalAmt) {
        this.txtTotalAmt = txtTotalAmt;
        setChanged();
    }
    // Getter method for txtTotalAmt

    public String getTxtTotalAmt() {
        return this.txtTotalAmt;
    }

    public String getTxtServiceTax() {
        return txtServiceTax;
    }

    public void setTxtServiceTax(String txtServiceTax) {
        this.txtServiceTax = txtServiceTax;
    }
    
    // Setter method for txtRemarks
    void setTxtRemarks(String txtRemarks) {
        this.txtRemarks = txtRemarks;
        setChanged();
    }
    // Getter method for txtRemarks

    String getTxtRemarks() {
        return this.txtRemarks;
    }

    /**
     * Getter for property oldTransDetMap.
     *
     * @return Value of property oldTransDetMap.
     */
    public java.util.HashMap getOldTransDetMap() {
        return oldTransDetMap;
    }

    /**
     * Setter for property oldTransDetMap.
     *
     * @param oldTransDetMap New value of property oldTransDetMap.
     */
    public void setOldTransDetMap(java.util.HashMap oldTransDetMap) {
        this.oldTransDetMap = oldTransDetMap;
    }

    /**
     * Getter for property txtExchangeCalc.
     *
     * @return Value of property txtExchangeCalc.
     */
    public java.lang.String getTxtExchangeCalc() {
        return txtExchangeCalc;
    }

    /**
     * Setter for property txtExchangeCalc.
     *
     * @param txtExchangeCalc New value of property txtExchangeCalc.
     */
    public void setTxtExchangeCalc(java.lang.String txtExchangeCalc) {
        this.txtExchangeCalc = txtExchangeCalc;
    }

    public String getTxtBeneficiaryBank() {
        return txtBeneficiaryBank;
    }

    public void setTxtBeneficiaryBank(String txtBeneficiaryBank) {
        this.txtBeneficiaryBank = txtBeneficiaryBank;
    }

    public String getTxtBeneficiaryBranch() {
        return txtBeneficiaryBranch;
    }

    public void setTxtBeneficiaryBranch(String txtBeneficiaryBranch) {
        this.txtBeneficiaryBranch = txtBeneficiaryBranch;
    }

    public String getTxtAccountNumber() {
        return txtAccountNumber;
    }

    public void setTxtAccountNumber(String txtAccountNumber) {
        this.txtAccountNumber = txtAccountNumber;
    }

    public boolean isNewData() {
        return newData;
    }

    public void setNewData(boolean newData) {
        this.newData = newData;
    }

    public EnhancedTableModel getTblIssueDetails() {
        return tblIssueDetails;
    }

    public void setTblIssueDetails(EnhancedTableModel tblIssueDetails) {
        this.tblIssueDetails = tblIssueDetails;
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

    public String getSlNo() {
        return slNo;
    }

    public void setSlNo(String slNo) {
        this.slNo = slNo;
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

    public java.util.HashMap get_authorizeMap() {
        return _authorizeMap;
    }

    public void set_authorizeMap(java.util.HashMap _authorizeMap) {
        this._authorizeMap = _authorizeMap;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public String getIFSC_Code() {
        return IFSC_Code;
    }

    public void setIFSC_Code(String IFSC_Code) {
        this.IFSC_Code = IFSC_Code;
    }

    public String getRTGS_ID() {
        return RTGS_ID;
    }

    public void setRTGS_ID(String RTGS_ID) {
        this.RTGS_ID = RTGS_ID;
    }

    public String getTxtBeneficiaryBankName() {
        return txtBeneficiaryBankName;
    }

    public void setTxtBeneficiaryBankName(String txtBeneficiaryBankName) {
        this.txtBeneficiaryBankName = txtBeneficiaryBankName;
    }

    public String getTxtBeneficiaryBranchName() {
        return txtBeneficiaryBranchName;
    }

    public void setTxtBeneficiaryBranchName(String txtBeneficiaryBranchName) {
        this.txtBeneficiaryBranchName = txtBeneficiaryBranchName;
    }

    public String getTxtBeneficiaryIFSC_Code() {
        return txtBeneficiaryIFSC_Code;
    }

    public void setTxtBeneficiaryIFSC_Code(String txtBeneficiaryIFSC_Code) {
        this.txtBeneficiaryIFSC_Code = txtBeneficiaryIFSC_Code;
    }

    public String getTxtBeneficiaryName() {
        return txtBeneficiaryName;
    }

    public void setTxtBeneficiaryName(String txtBeneficiaryName) {
        this.txtBeneficiaryName = txtBeneficiaryName;
    }
    
      public ArrayList getOrgORrespDetailsList() {
        return orgORrespDetailsList;
    }

    public void setOrgORrespDetailsList(ArrayList orgORrespDetailsList) {
        this.orgORrespDetailsList = orgORrespDetailsList;
    }

    public String getTxtCharges() {
        return txtCharges;
    }

    public void setTxtCharges(String txtCharges) {
        this.txtCharges = txtCharges;
    }

    public String getTxtgst() {
        return txtgst;
    }

    public void setTxtgst(String txtgst) {
        this.txtgst = txtgst;
    }    
    
}
